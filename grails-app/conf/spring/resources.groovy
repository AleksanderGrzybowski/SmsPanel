import grails.util.Environment
import smspanel.CorsFilter
import smspanel.FakeSmsService
import smspanel.BramkaSmsService

// Place your Spring DSL code here
beans = {
    corsFilter(CorsFilter)
    
    if (Environment.current == Environment.PRODUCTION) {
        smsService(BramkaSmsService, System.getenv('API_USERNAME'), System.getenv('API_PASSWORD'))
    }
    
    if (Environment.current == Environment.DEVELOPMENT) {
        smsService(FakeSmsService)
    }
}
