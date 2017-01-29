package smspanel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('ROLE_ADMIN_USER')
class ContactController {

    ContactService contactService

    def list() {
        List<Contact> all = contactService.list()
        render(all.collect {
            [
                    firstName: it.firstName,
                    lastName : it.lastName,
                    groupName: it.groupName,
                    phone    : it.phone
            ]
        } as JSON)
    }
}
