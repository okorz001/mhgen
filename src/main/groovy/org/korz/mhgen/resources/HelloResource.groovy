package org.korz.mhgen.resources

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path('/')
@Produces(MediaType.TEXT_PLAIN)
class HelloResource {
    @GET
    String get() {
        return 'Hello world!'
    }
}
