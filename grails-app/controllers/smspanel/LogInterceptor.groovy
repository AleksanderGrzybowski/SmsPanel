package smspanel

class LogInterceptor {

    def springSecurityService

    LogInterceptor() {
        matchAll()
    }

    boolean before() {
        String username = springSecurityService.currentUser?.username ?: '-'
        String ip = request.remoteAddr
        String url = request.requestURL

        log.info("${ip} | ${username} | ${url}")

        true
    }
}
