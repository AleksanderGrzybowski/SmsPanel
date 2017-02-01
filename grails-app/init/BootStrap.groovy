import groovy.util.logging.Log
import smspanel.Contact
import smspanel.Role
import smspanel.SmsService
import smspanel.User
import smspanel.UserRole

@Log
class BootStrap {
    
    SmsService smsService

    def init = { servletContext ->
        sampleUsers()
        importContacts()
        ensureSmsProviderIsAccessible()
    }

    void sampleUsers() {
        Role adminUserRole = new Role(authority: 'ROLE_ADMIN_USER').save(flush: true)

        ['bob', 'alice'].each {
            User user = new User(username: it, password: it).save(flush: true)
            new UserRole(user: user, role: adminUserRole).save(flush: true)
        }
    }


    void importContacts() {
        log.info 'Importing contacts'

        def contactsFile
        if (System.getenv('CONTACTS')) {
            contactsFile = new File(System.getenv('CONTACTS'))
            log.info "Importing from external file ${contactsFile}"
        } else {
            contactsFile = System.getResource("/sample.csv")
            log.info 'Importing sample'
        }
        contactsFile.text
                .split('\n')
                .findAll { String line -> line.trim() != '' }
                .collect { String line -> line.split(';') }
                .collect { [name: it[0], groups: it[1], phone: it[2]] }
                .each { new Contact(it).save(failOnError: true, flush: true) }
    }
    
    void ensureSmsProviderIsAccessible() {
        try {
            BigDecimal balance = smsService.accountBalance()
            log.info "SMS provider is accessible, account balance: ${balance}" 
        } catch (Exception e) {
            throw new RuntimeException('Error accessing sms provider api', e)
        }
    }
}
