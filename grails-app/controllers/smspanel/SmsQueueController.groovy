package smspanel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import java.text.SimpleDateFormat

@Secured('ROLE_ADMIN_USER')
class SmsQueueController {

    SmsQueueService smsQueueService

    def list() {
        List<SmsQueueEntry> all = smsQueueService.list()
        render(all.collect {
            [
                    id: it.id,
                    contact : [
                            name: it.contact.name
                    ],
                    content : it.content,
                    status  : it.status.name(),
                    dateSent: new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(it.dateSent)
            ]
        } as JSON)
    }
    
    def scheduleNewMessages(ScheduleMessagesDto dto) {
        dto.messages.each {
            smsQueueService.scheduleNewMessage(it.contactId, it.content)
        }
        
        response.status = 200
        render ([sentCount: dto.messages.size()] as JSON)
    }
    
    def accountBalance() {
        render ([balance: smsQueueService.accountBalance().toString()] as JSON)
    }
}
