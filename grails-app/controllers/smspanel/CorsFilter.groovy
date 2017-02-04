package smspanel

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * I'm not sure if this is entirely correct from browser point of view.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Headers", "origin, authorization, accept, content-type, x-requested-with")
        resp.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS")
        resp.addHeader("Access-Control-Max-Age", "3600")
        resp.addHeader("Access-Control-Allow-Origin", "*")
        resp.addHeader("Access-Control-Allow-Credentials", "true")

        if (req.method != "OPTIONS") chain.doFilter(req, resp)
    }
}