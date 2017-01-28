import smspanel.Contact
import smspanel.Role
import smspanel.User
import smspanel.UserRole

class BootStrap {

    def init = { servletContext ->
        sampleUsers()
        sampleContacts()
    }

    static void sampleUsers() {
        Role adminUserRole = new Role(authority: 'ROLE_ADMIN_USER').save(flush: true)

        ['bob', 'alice'].each {
            User user = new User(username: it, password: it).save(flush: true)
            new UserRole(user: user, role: adminUserRole).save(flush: true)
        }
    }
    
    static void sampleContacts() {
        new Contact(
                firstName: 'John',
                lastName: 'Doe',
                groupName: 'A',
                phone: '+48 123 456 789'
        ).save(flush: true)
        
        new Contact(
                firstName: 'Jan',
                lastName: 'Kowalski',
                groupName: 'A',
                phone: '+48 987 654 321'
        ).save(flush: true)
    }
}
