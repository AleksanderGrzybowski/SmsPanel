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
                            firstName: it.contact.firstName,
                            lastName : it.contact.lastName
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
    
    static class ScheduleMessagesDto {
        List<ScheduleSingleMessageDto> messages
    }
    
    static class ScheduleSingleMessageDto {
        Long contactId
        String content
    }
}
