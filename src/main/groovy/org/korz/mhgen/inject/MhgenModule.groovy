package org.korz.mhgen.inject

import com.google.common.reflect.ClassPath
import com.google.inject.AbstractModule
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class MhgenModule extends AbstractModule{
    @Override
    protected void configure() {
        def classloader = Thread.currentThread().contextClassLoader
        bind(ClassPath).toInstance(ClassPath.from(classloader))
        log.debug('Dependencies configured')
    }
}
