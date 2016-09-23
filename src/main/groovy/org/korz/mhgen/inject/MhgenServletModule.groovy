package org.korz.mhgen.inject

import com.google.inject.servlet.ServletModule
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.korz.mhgen.filters.CacheControlFilter
import org.korz.mhgen.filters.DefaultServletFilter
import org.korz.mhgen.servlets.NotFoundServlet
import org.korz.mhgen.servlets.WeaponsServlet

@CompileStatic
@Slf4j
class MhgenServletModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter('/assets/*').through(CacheControlFilter, [ttl: '60'])
        filter('/assets/*').through(DefaultServletFilter)
        serve('/weapons').with(WeaponsServlet)
        serve('/*').with(NotFoundServlet)
        log.debug('Servlet configured')
    }
}
