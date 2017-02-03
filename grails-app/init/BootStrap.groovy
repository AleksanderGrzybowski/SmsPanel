import grails.converters.JSON
import grails.util.Environment
import groovy.util.logging.Log
import smspanel.*

@Log
class BootStrap {
    
    SmsService smsService

    def init = { servletContext ->
        users()
        importContacts()
        ensureSmsProviderIsAccessible()
    }

    void users() {
        Role adminRole = new Role(authority: 'ROLE_ADMIN_USER').save(flush: true)

        if (Environment.developmentMode) {
            log.info('App running in development mode, creating sample users')

            ['bob', 'alice'].each {
                createUser(it, it, adminRole)
            }
        } else {
            String credentialsFilename = System.getenv('CREDENTIALS')
            log.info("App running in production mode, looking for users in ${credentialsFilename}")
            
            if (!credentialsFilename) {
                throw new RuntimeException('Credentials filename not given')
            }
            
            File file = new File(credentialsFilename)
            if (!file.exists()) {
                throw new RuntimeException('Credentials file does not exist')
            }

            JSON.parse(file.text).each {
                createUser(it.username, it.password, adminRole)
            }
        }
    }
    
    private void createUser(String username, String password, role) {
        log.info("Creating user ${username}")
        User user = new User(username: username, password: password).save(flush: true)
        new UserRole(user: user, role: role).save(flush: true)
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
