package smspanel

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.lang3.time.DateUtils
import spock.lang.Specification

@TestFor(SmsQueueService)
@Mock([Contact, SmsQueueEntry])
class SmsQueueServiceSpec extends Specification {

    SmsService smsService
    MessageSanitizationService messageSanitizationService

    void setup() {
        service.smsService = smsService = Mock(SmsService)
        service.messageSanitizationService = messageSanitizationService = Mock(MessageSanitizationService) {
            sanitize(_) >> { String message -> message }
        }
    }

    void "should list all queue entries, newest on the top"() {
        given:
        SmsQueueEntry entry1 = new SmsQueueEntry(
                contact: new Contact(
                        name: 'John',
                        groups: 'W',
                        phone: '+48 123 456 789'
                ).save(),
                content: 'message 1',
                status: MessageStatus.SENT,
                dateSent: new Date()
        ).save()
        SmsQueueEntry entry2 = new SmsQueueEntry(
                contact: new Contact(
                        name: 'Mark',
                        groups: 'A',
                        phone: '+48 987 654 321'
                ).save(),
                content: 'message 2',
                status: MessageStatus.SENT,
                dateSent: DateUtils.addDays(new Date(), -1)
        ).save()

        when:
        List<SmsQueueEntry> entries = service.list()

        then:
        entries == [entry1, entry2]
    }

    def "should create new sms queue entry, to schedule new message"() {
        given:
        Contact contact = new Contact(
                name: 'Mark',
                groups: 'A',
                phone: '+48 987 654 321'
        ).save()

        when:
        service.scheduleNewMessage(contact.id, 'Hello')

        then:
        SmsQueueEntry.count() == 1
        SmsQueueEntry entry = SmsQueueEntry.first()
        entry.contact == contact
        entry.content == 'Hello'
        entry.status == MessageStatus.PENDING
    }

    def "should send message, and, if it was successfully sent, set SENT status"() {
        given:
        Contact contact = new Contact(
                name: 'John',
                groups: 'W',
                phone: '+48 123 456 789'
        ).save()
        SmsQueueEntry entry = new SmsQueueEntry(
                contact: contact,
                content: 'message 1',
                status: MessageStatus.PENDING,
                dateSent: new Date()
        ).save()
        new SmsQueueEntry(
                contact: contact,
                content: 'message 2',
                status: MessageStatus.SENT,
                dateSent: new Date()
        ).save()

        when:
        service.sendAllPendingMessages()

        then:
        1 * smsService.send('message 1', '+48 123 456 789') >> true
        entry.status == MessageStatus.SENT
    }

    def "should send message and, if it was not successfully sent, set FAILED status"() {
        given:
        Contact contact = new Contact(
                name: 'John',
                groups: 'W',
                phone: '+48 123 456 789'
        ).save()
        SmsQueueEntry entry = new SmsQueueEntry(
                contact: contact,
                content: 'message 1',
                status: MessageStatus.PENDING,
                dateSent: new Date()
        ).save()
        new SmsQueueEntry(
                contact: contact,
                content: 'message 2',
                status: MessageStatus.SENT,
                dateSent: new Date()
        ).save()

        when:
        service.sendAllPendingMessages()

        then:
        1 * smsService.send('message 1', '+48 123 456 789') >> false
        entry.status == MessageStatus.FAILED
    }

    def "should delegate returning balance to underlying service"() {
        when:
        service.accountBalance()

        then:
        1 * smsService.accountBalance()
    }
}
