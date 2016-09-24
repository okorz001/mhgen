package org.korz.mhgen.inject

import com.google.inject.AbstractModule
import groovy.sql.Sql
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.korz.mhgen.core.Db

@CompileStatic
@Slf4j
class MhgenModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(Sql).toInstance(Db.sql)
        log.debug('Dependencies configured')
    }
}
