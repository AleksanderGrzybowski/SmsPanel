package smspanel

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(MessageSanitizationService)
class MessageSanitizationServiceSpec extends Specification {

    def 'should replace Polish characters with their ASCII equivalents'() {
        expect:
        after == service.sanitize(before)

        where:
        before              | after
        ''                  | ''
        ' '                 | ' '
        'hello'             | 'hello'
        'zażółć gęślą jaźń' | 'zazolc gesla jazn'
        'ZAŻÓŁĆ GĘŚLĄ JAŹŃ' | 'ZAZOLC GESLA JAZN'
    }

    def 'should remove non-polish and strange Unicode characters'() {
        expect:
        after == service.sanitize(before)

        where:
        before       | after
        'aufräumen'  | 'aufrumen'
        'hi ☃ there' | 'hi  there'
    }
}
