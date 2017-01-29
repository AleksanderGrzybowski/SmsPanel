package smspanel

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import java.text.SimpleDateFormat

import static smspanel.SmsQueueController.*

@TestFor(SmsQueueController)
@Mock(SmsQueueEntry)
class SmsQueueControllerSpec extends Specification {

    def smsQueueService

    def sampleEntry = new SmsQueueEntry(
            contact: new Contact(
                    firstName: 'John',
                    lastName: 'Doe',
                    groupName: 'W',
                    phone: '+48 123 456 789'
            ),
            content: 'message 1',
            status: MessageStatus.SENT,
            dateSent: new Date()
    )

    def setup() {
        controller.smsQueueService = smsQueueService = Mock(SmsQueueService)
    }

    def "should list all queue entries"() {
        when:
        controller.list()

        then:
        1 * smsQueueService.list() >> [sampleEntry]
        response.json.size() == 1
        response.json[0].id == sampleEntry.id
        response.json[0].contact.firstName == sampleEntry.contact.firstName
        response.json[0].contact.lastName == sampleEntry.contact.lastName
        response.json[0].content == sampleEntry.content
        response.json[0].status == sampleEntry.status.name()
        response.json[0].dateSent == new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sampleEntry.dateSent)
    }
    
    def "should schedule new messages"() {
        when:
        controller.scheduleNewMessages(new ScheduleMessagesDto(
                messages: [
                        new ScheduleSingleMessageDto(
                                contactId: 1,
                                content: 'first message'
                        ),
                        new ScheduleSingleMessageDto(
                                contactId: 2,
                                content: 'second message'
                        )
                ]
        ))
        
        then:
        response.status == 200
        response.json.sentCount == 2
        1 * smsQueueService.scheduleNewMessage(1, 'first message')
        1 * smsQueueService.scheduleNewMessage(2, 'second message')
        
    }

}
