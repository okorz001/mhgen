package org.korz.mhgen.resources

import groovy.sql.Sql

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path('/')
@Produces(MediaType.TEXT_PLAIN)
class HelloResource {
    @GET
    String get() {
        Sql sql = Sql.newInstance([driver: 'org.sqlite.JDBC',
                                   url: 'jdbc:sqlite::resource:org/korz/mhgen/mhgen.db'])
        def count
        sql.eachRow('select count(*) as count from `armor`') {
            count = it.count
        }
        sql.close()
        return count
    }
}
