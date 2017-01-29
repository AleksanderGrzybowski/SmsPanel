import grails.util.Environment
import smspanel.CorsFilter
import smspanel.FakeSmsService
import smspanel.SerwerSmsPlSmsService

// Place your Spring DSL code here
beans = {
    corsFilter(CorsFilter)
    
    if (Environment.current == Environment.PRODUCTION) {
        smsService(SerwerSmsPlSmsService, System.getenv('API_USERNAME'), System.getenv('API_PASSWORD'))
        smsService(SerwerSmsPlSmsService, 'webapi_kelog', 'ma7Ii0vees')
    }
    
    if (Environment.current == Environment.DEVELOPMENT) {
        smsService(FakeSmsService)
    }
}
