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
                    id    : it.id,
                    name  : it.name,
                    groups: it.groups,
                    phone : it.phone
            ]
        } as JSON)
    }
}
