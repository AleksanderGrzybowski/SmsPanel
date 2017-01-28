package smspanel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('ROLE_ADMIN_USER')
class ContactController {

    ContactService contactService

    def list() {
        render (contactService.list() as JSON)
    }
}
