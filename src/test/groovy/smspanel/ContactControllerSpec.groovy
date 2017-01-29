package smspanel

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ContactController)
class ContactControllerSpec extends Specification {

    def contactService

    Contact sampleContact = new Contact(
            firstName: 'John',
            lastName: 'Doe',
            groupName: 'W',
            phone: '+48 123 456 789'
    )

    def setup() {
        controller.contactService = contactService = Mock(ContactService)
    }

    def "should list all contacts with correct format"() {
        when:
        controller.list()

        then:
        1 * contactService.list() >> [sampleContact]
        response.json.size() == 1
        response.json[0].firstName == sampleContact.firstName
        response.json[0].lastName == sampleContact.lastName
        response.json[0].groupName == sampleContact.groupName
        response.json[0].phone == sampleContact.phone
    }
}
