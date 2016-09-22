package org.korz.mhgen.filters

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Singleton
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
@Singleton
@Slf4j
class CacheControlFilter implements Filter {

    int ttl

    @Override
    void init(FilterConfig config) {
        ttl = config.getInitParameter('ttl') as int
    }

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse resp,
                         FilterChain chain) {
        if (req instanceof HttpServletRequest &&
            resp instanceof HttpServletResponse) {
            log.debug("Caching for ${ttl} seconds: ${req.requestURI}")
            resp.setHeader('Cache-Control', "public,max-age=${ttl}");
        }
        chain.doFilter(req, resp);
    }

    @Override
    void destroy() {
    }
}
