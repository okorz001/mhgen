package org.korz.mhgen.core
import org.sqlite.JDBC

import groovy.sql.Sql
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
class Db {
    final static Class DRIVER = JDBC
    final static def URL = 'jdbc:sqlite::resource:mhgen.db'

    Sql sql

    Db() {
        log.info("Connecting to ${URL} with ${DRIVER}")
        this.sql = Sql.newInstance(driver: DRIVER.canonicalName, url: URL)
    }
}
