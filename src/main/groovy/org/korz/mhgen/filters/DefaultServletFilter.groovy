package org.korz.mhgen.filters

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Singleton
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.RequestDispatcher
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@CompileStatic
@Singleton
@Slf4j
class DefaultServletFilter implements Filter {

    RequestDispatcher defaultServlet

    @Override
    void init(FilterConfig config) {
        // Get the default servlet
        defaultServlet = config.servletContext.getNamedDispatcher('default')
    }

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse resp,
                         FilterChain chain) {
        if (req instanceof HttpServletRequest) {
            log.debug("Forwarding to default servlet: ${req.requestURI}")
            defaultServlet.forward(req, resp)
        }
        else {
            chain.doFilter(req, resp)
        }
    }

    @Override
    void destroy() {
    }
}
