package org.korz.mhgen.inject

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.servlet.GuiceServletContextListener
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class ContextListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        log.debug('Creating injector')
        return Guice.createInjector(new MhgenModule(), new MhgenServletModule())
    }
}
