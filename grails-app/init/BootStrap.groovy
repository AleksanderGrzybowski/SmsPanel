import groovy.util.logging.Log
import smspanel.Contact
import smspanel.Role
import smspanel.User
import smspanel.UserRole

@Log
class BootStrap {

    def init = { servletContext ->
        sampleUsers()
        importContacts()
    }

    static void sampleUsers() {
        Role adminUserRole = new Role(authority: 'ROLE_ADMIN_USER').save(flush: true)

        ['bob', 'alice'].each {
            User user = new User(username: it, password: it).save(flush: true)
            new UserRole(user: user, role: adminUserRole).save(flush: true)
        }
    }


    static void importContacts() {
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
                .collect { [firstName: it[0], lastName: it[1], groupName: it[2], phone: it[3]] }
                .each { new Contact(it).save(failOnError: true, flush: true) }
    }
}
