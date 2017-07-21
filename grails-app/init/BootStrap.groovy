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

        String contactsFileText
        String filename = System.getenv('CONTACTS_CSV')
        
        if (filename) {
            log.info "Importing from external file ${filename}"
            contactsFileText = new File(filename).text
        } else {
            log.info 'Importing sample'
            contactsFileText = sampleCsv()
        }
        
        contactsFileText
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
    
    private String sampleCsv() {
        '''
Nora James;A;48123456789
Kelli Hollowa;B;48123456789
Tamara Barton;A,B;48123456789
Myra Cruz;C,A;48123456789
Elmer Page;C,B;48123456789
Delores Garcia;A,B,C;48123456789
Jordan Holland;C;48123456789
Sharon Fleming;D;48123456789
Marion Silva;D,A;48123456789
Theodore Shaw;B,A;48123456789
'''
    }
}
