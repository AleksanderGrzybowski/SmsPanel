package smspanel

import com.messagebird.MessageBirdClient
import com.messagebird.MessageBirdService
import com.messagebird.MessageBirdServiceImpl
import grails.transaction.Transactional

@Transactional
class MessageBirdSmsService implements SmsService {

    String apiKey
    String sourceNumber

    MessageBirdSmsService(String apiKey, String sourceNumber) {
        if (apiKey == null || apiKey == '' || sourceNumber == null || sourceNumber == '') {
            throw new IllegalArgumentException("Credentials or source number not provided")
        }

        this.apiKey = apiKey
        this.sourceNumber = sourceNumber
    }

    boolean send(String content, String phone) {
        MessageBirdService messageBirdService = new MessageBirdServiceImpl(apiKey)
        MessageBirdClient messageBirdClient = new MessageBirdClient(messageBirdService)

        try {
            messageBirdClient.sendMessage(sourceNumber, content, [new BigInteger(phone)])
            return true
        } catch (Exception e) {
            log.error(e)
            return false
        }
    }
}
