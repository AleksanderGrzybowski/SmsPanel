package smspanel

import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder

import java.math.RoundingMode

@Transactional
class BramkaSmsService implements SmsService {

    static final String API_SEND_URL = "https://api.gsmservice.pl/v5/send.php"
    static final String API_BALANCE_URL = "https://api.gsmservice.pl/v5/balance.php"
    
    String apiUsername
    String apiPassword

    static final int SMS_TYPE_ECO = 3

    BramkaSmsService(String apiUsername, String apiPassword) {
        if (apiUsername == null || apiUsername == '' || apiPassword == null || apiPassword == '') {
            throw new IllegalArgumentException("Credentials number not provided")
        }

        this.apiUsername = apiUsername
        this.apiPassword = apiPassword
    }

    boolean send(String content, String phone) {
        log.info('Sending sms through bramkasms.pl')

        String result = ''

        new HTTPBuilder(API_SEND_URL).post(
                body: [
                        login    : apiUsername,
                        pass     : apiPassword,
                        recipient: phone,
                        message  : content,
                        msg_type : SMS_TYPE_ECO
                ],
                requestContentType: ContentType.URLENC
        ) { response, reader ->
            result = reader.toString()
        }

        if (result.startsWith('OK')) {
            log.info("Sending successful, response = ${result}")
            return true
        } else {
            log.error("Sending failed, response = ${result}")
            return false
        }
    }

    @Override
    BigDecimal accountBalance() {
        String result = ''

        new HTTPBuilder(API_BALANCE_URL).post( // yes, POST :(
                body: [
                        login: apiUsername,
                        pass : apiPassword
                ],
                requestContentType: ContentType.URLENC
        ) { response, reader ->
            result = reader.toString()
        }
        
        if (!result.startsWith('OK')) {
            throw new AssertionError()
        }
        
        return new BigDecimal(result.split("\\|")[2]).setScale(2, RoundingMode.CEILING)
    }
}
