package smspanel

import grails.transaction.Transactional
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder

@Transactional
class BramkaSmsService implements SmsService {

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

        new HTTPBuilder("https://api.gsmservice.pl/v5/send.php").post(
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
}
