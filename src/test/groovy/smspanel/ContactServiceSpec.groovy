package smspanel

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(ContactService)
@Mock(Contact)
class ContactServiceSpec extends Specification {

    def "should list all contacts"() {
        given:
        Contact c1 = new Contact(name: 'John').save(validate: false)
        Contact c2 = new Contact(name: 'Tom').save(validate: false)

        expect:
        service.list() == [c1, c2]
    }
}
