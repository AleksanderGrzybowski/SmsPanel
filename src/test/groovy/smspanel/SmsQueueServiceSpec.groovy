package smspanel

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.lang3.time.DateUtils
import spock.lang.Specification

@TestFor(SmsQueueService)
@Mock([Contact, SmsQueueEntry])
class SmsQueueServiceSpec extends Specification {

    void "should list all queue entries, newest on the top"() {
        given:
        SmsQueueEntry entry1 = new SmsQueueEntry(
                contact: new Contact(
                        firstName: 'John',
                        lastName: 'Doe',
                        groupName: 'W',
                        phone: '+48 123 456 789'
                ).save(),
                content: 'message 1',
                status: MessageStatus.SENT,
                dateSent: new Date()
        ).save()
        SmsQueueEntry entry2 = new SmsQueueEntry(
                contact: new Contact(
                        firstName: 'Mark',
                        lastName: 'Smith',
                        groupName: 'A',
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
                firstName: 'Mark',
                lastName: 'Smith',
                groupName: 'A',
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
}
