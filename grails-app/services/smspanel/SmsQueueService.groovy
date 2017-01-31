package smspanel

import grails.transaction.Transactional

@Transactional
class SmsQueueService {
    
    SmsService smsService

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
    
    void sendAllPendingMessages() {
        SmsQueueEntry.findAllByStatus(MessageStatus.PENDING).each {
            if (smsService.send(it.content, it.contact.phone)) {
                it.status = MessageStatus.SENT
            } else {
                it.status = MessageStatus.FAILED
            }
            it.save(failOnError: true)
        }
    }
    
    BigDecimal accountBalance() {
        return smsService.accountBalance()
    }
}
