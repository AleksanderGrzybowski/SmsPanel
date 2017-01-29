package smspanel

import grails.transaction.Transactional

@Transactional
class SmsQueueService {

    List<SmsQueueEntry> list() {
        SmsQueueEntry.list().sort { a, b -> b.dateSent <=> a.dateSent }
    }
    
    void scheduleNewMessage(Long contactId, String content) {
        new SmsQueueEntry(
                contact: Contact.findById(contactId),
                content: content,
                status: MessageStatus.PENDING,
                dateSent: new Date()
        ).save(failOnError: true)
    }
}
