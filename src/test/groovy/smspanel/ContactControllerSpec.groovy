package smspanel

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@Mock(Contact)
@TestFor(ContactController)
class ContactControllerSpec extends Specification {

    def contactService

    def setup() {
        controller.contactService = contactService = Mock(ContactService)
    }

    def "should list all contacts with correct format"() {
        given:
        Contact sampleContact = new Contact(
                firstName: 'John',
                lastName: 'Doe',
                groupName: 'W',
                phone: '+48 123 456 789'
        ).save()
        
        when:
        controller.list()

        then:
        1 * contactService.list() >> [sampleContact]
        response.json.size() == 1
        response.json[0].id == sampleContact.id
        response.json[0].firstName == sampleContact.firstName
        response.json[0].lastName == sampleContact.lastName
        response.json[0].groupName == sampleContact.groupName
        response.json[0].phone == sampleContact.phone
    }
}
