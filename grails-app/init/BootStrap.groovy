import grails.util.Environment
import groovy.util.logging.Log
import smspanel.*

@Log
class BootStrap {

    public static final String USERS_ENV_VAR = 'USERS'
    SmsService smsService

    def init = { servletContext ->
        createUsers()
        importContacts()
        ensureSmsProviderIsAccessible()
    }

    private void createUsers() {
        Role adminRole = new Role(authority: 'ROLE_ADMIN_USER').save(flush: true)

        if (Environment.developmentMode) {
            List<String> sampleUsers = ['bob', 'alice']
            log.info("App running in development mode, creating sample users ${sampleUsers}")

            sampleUsers.each {
                createUser(it, it, adminRole)
            }
        } else {
            String usersString = System.getenv(USERS_ENV_VAR)
            log.info("App running in production mode, looking for users in environment var ${USERS_ENV_VAR}")
            
            if (!usersString) {
                throw new RuntimeException('Users definition string not given')
            }

            usersString.split(" ").each { usersAndPassword ->
                List<String> split = usersAndPassword.split(":")
                log.info("Creating user ${split[0]}")
                
                createUser(split[0], split[1], adminRole)
            }
        }
    }
    
    private void createUser(String username, String password, role) {
        log.info("Creating user ${username}")
        User user = new User(username: username, password: password).save(flush: true)
        new UserRole(user: user, role: role).save(flush: true)
    }

    private void importContacts() {
        log.info 'Importing contacts'

        def contactsFile
        if (System.getenv('CONTACTS_CSV')) {
            contactsFile = new File(System.getenv('CONTACTS_CSV'))
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
    
    private void ensureSmsProviderIsAccessible() {
        try {
            BigDecimal balance = smsService.accountBalance()
            log.info "SMS provider is accessible, account balance: ${balance}" 
        } catch (Exception e) {
            throw new RuntimeException('Error accessing sms provider api', e)
        }
    }
}
