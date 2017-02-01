package smspanel

import grails.test.mixin.Mock
import spock.lang.Specification

@Mock(Contact)
class ScheduleMessagesDtoSpec extends Specification {

    def 'messages constraint, disallow null or empty list'() {
        given:
        ScheduleMessagesDto dto = new ScheduleMessagesDto(messages: [])

        when:
        dto.validate()

        then:
        dto.hasErrors()
        dto.errors.allErrors[0].code == 'noMessages'

        when:
        dto.messages = null
        dto.validate()

        then:
        dto.hasErrors()
        dto.errors.allErrors[0].code == 'noMessages'
    }

    def 'messages constraint, nested elements'() {
        given:
        Contact sampleContact = new Contact(
                name: 'John',
                groups: 'W',
                phone: '+48 123 456 789'
        ).save()
        ScheduleMessagesDto dto = new ScheduleMessagesDto(
                messages: [new ScheduleSingleMessageDto(contactId: 42, content: 'Hey')]
        )

        when:
        dto.validate()

        then:
        dto.hasErrors()
        dto.errors.allErrors[0].field == 'messages[0].contactId'
        dto.errors.allErrors[0].code == 'nonexistentContact'
    }
}
