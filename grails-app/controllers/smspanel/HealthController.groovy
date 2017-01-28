package smspanel

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import java.lang.management.ManagementFactory

@Secured('isAnonymous()')
class HealthController {

    def index() {
        render([status: 'ok', uptimeMilliseconds: ManagementFactory.runtimeMXBean.uptime] as JSON)
    }
}
