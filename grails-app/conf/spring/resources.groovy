import grails.util.Environment
import smspanel.CorsFilter
import smspanel.FakeSmsService
import smspanel.MessageBirdSmsService

// Place your Spring DSL code here
beans = {
    corsFilter(CorsFilter)
    
    if (Environment.current == Environment.PRODUCTION) {
        smsService(MessageBirdSmsService, System.getenv('API_KEY'), System.getenv('SOURCE_PHONE'))
    }
    
    if (Environment.current == Environment.DEVELOPMENT) {
        smsService(FakeSmsService)
    }
}
