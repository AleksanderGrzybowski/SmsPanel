package smspanel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('ROLE_ADMIN_USER')
class ContactController {

    ContactService contactService

    def list() {
        render(contactService.list().collect {
            [
                    id    : it.id,
                    name  : it.name,
                    groups: it.groups,
                    phone : it.phone
            ]
        } as JSON)
    }
}
