package smspanel

import grails.transaction.Transactional
import panel.SerwerSMS

@Transactional
class SerwerSmsPlSmsService implements SmsService {
    
    String apiUsername, apiPassword
    
    SerwerSmsPlSmsService(String apiUsername, String apiPassword) {
        if (apiPassword == null || apiUsername == '' || apiPassword == null || apiPassword == '') {
            throw new IllegalArgumentException("Credentials not provided")
        }
        
        this.apiUsername = apiUsername
        this.apiPassword = apiPassword
    }
    
    boolean send(String content, String phone) {
        SerwerSMS sms = new SerwerSMS(apiUsername, apiPassword, 0)
        sms.wiadomosc = content
        sms.sms([phone] as String[])
        
        true
    }
}
