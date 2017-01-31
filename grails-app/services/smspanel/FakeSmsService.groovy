package smspanel

import grails.transaction.Transactional

import java.math.RoundingMode

@Transactional
class FakeSmsService implements SmsService {

    boolean send(String content, String phone) {
        log.info "Sms was sent: ${phone}: [${content}]"
        true
    }

    @Override
    BigDecimal accountBalance() {
        return new BigDecimal(Math.random() * 100).setScale(2, RoundingMode.CEILING)
    }
}
