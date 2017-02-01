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
                name: 'John',
                groups: 'W',
                phone: '+48 123 456 789'
        ).save()
        
        when:
        controller.list()

        then:
        1 * contactService.list() >> [sampleContact]
        response.json.size() == 1
        response.json[0].id == sampleContact.id
        response.json[0].name == sampleContact.name
        response.json[0].groups == sampleContact.groups
        response.json[0].phone == sampleContact.phone
    }
}
