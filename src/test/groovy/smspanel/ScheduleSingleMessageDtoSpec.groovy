package smspanel

import grails.test.mixin.Mock
import spock.lang.Specification

@Mock(Contact)
class ScheduleSingleMessageDtoSpec extends Specification {

    def 'contactId constraint'() {
        given:
        ScheduleSingleMessageDto dto = new ScheduleSingleMessageDto()
        Contact sampleContact = new Contact(
                name: 'John',
                groups: 'W',
                phone: '+48 123 456 789'
        ).save()

        when:
        dto.contactId = sampleContact.id
        dto.validate(['contactId'])

        then:
        !dto.hasErrors()

        when:
        dto.contactId = 42
        dto.validate(['contactId'])

        then:
        dto.hasErrors()
        dto.errors.allErrors[0].code == 'nonexistentContact'
    }
}
