package smspanel

import grails.transaction.Transactional

@Transactional
class FakeSmsService implements SmsService {
    
    boolean send(String content, String phone) {
        log.info "Sms was sent: ${phone}: [${content}]"
        true
    }
}
