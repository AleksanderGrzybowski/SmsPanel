package smspanel

import grails.converters.JSON

class ErrorController  {
    def index() {
        response.status = 500
        Throwable exception = request.exception?.cause?.target
        render([message: exception.message, 'class': exception?.class] as JSON)
    }
}
