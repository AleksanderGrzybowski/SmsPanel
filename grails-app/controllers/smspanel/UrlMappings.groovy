package smspanel

class UrlMappings {

    static mappings = {
        "/health"(controller: 'health', action: 'index')

        "/api/contacts"(controller: 'contact') {
            action = [GET: 'list']
        }
        "/api/queue"(controller: 'smsQueue') {
            action = [GET: 'list', POST: 'scheduleNewMessages']
        }
        "/api/accountBalance"(controller: 'smsQueue') {
            action = [GET: 'accountBalance']
        }

        "500"(controller: 'error')
        "404"(view: '/notFound')
    }
}
