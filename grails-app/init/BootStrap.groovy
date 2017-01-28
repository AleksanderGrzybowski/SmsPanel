import smspanel.Role
import smspanel.User
import smspanel.UserRole

class BootStrap {

    def init = { servletContext ->
        sampleUsers()
    }

    static void sampleUsers() {
        Role adminUserRole = new Role(authority: 'ROLE_ADMIN_USER').save(flush: true)

        ['bob', 'alice'].each {
            User user = new User(username: it, password: it).save(flush: true)
            new UserRole(user: user, role: adminUserRole).save(flush: true)
        }
    }
}
