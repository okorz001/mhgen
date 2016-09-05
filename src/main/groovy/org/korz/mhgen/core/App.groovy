package org.korz.mhgen.core
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.server.ResourceConfig

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class App extends ResourceConfig {
    App() {
        this.applicationName = 'mhgen'
        this.registerInstances(this.binder)
        log.info('App created')
    }

    def getBinder() {
        return new AbstractBinder() {
            @Override
            protected void configure() {
                this.bind(new Db()).to(Db)
                log.info('Binder configured')
            }
        }
    }
}
