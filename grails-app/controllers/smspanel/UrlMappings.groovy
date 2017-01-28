package smspanel

class UrlMappings {

    static mappings = {

        "/health"(controller: 'health', action: 'index')
        
        "500"(controller: 'error')
        "404"(view: '/notFound')
    }
}
