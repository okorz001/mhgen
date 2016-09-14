package org.korz.mhgen.core

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

@CompileStatic
@Slf4j
class CacheControlFilter implements Filter {
    int ttl

    @Override
    void init(FilterConfig config) throws ServletException {
        this.ttl = config.getInitParameter('ttl') as int ?: 60
        log.info("Caching assets for ${ttl} seconds")
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ((HttpServletResponse) response).setHeader('Cache-Control', "public,max-age=${ttl}");
        chain.doFilter(request, response);
    }

    @Override
    void destroy() {
    }
}
