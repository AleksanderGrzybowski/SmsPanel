package smspanel

class UrlMappings {

    static mappings = {
        "/health"(controller: 'health', action: 'index')

        "/api/contacts"(controller: 'contact') {
            action = [GET: 'list']
        }

        "500"(controller: 'error')
        "404"(view: '/notFound')
    }
}
