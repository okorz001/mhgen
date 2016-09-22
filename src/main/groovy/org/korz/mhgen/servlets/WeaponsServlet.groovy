package org.korz.mhgen.servlets

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Singleton
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
@Singleton
@Slf4j
class WeaponsServlet extends HttpServlet {

    @Override
    void doGet(HttpServletRequest req, HttpServletResponse resp) {
        def path = req.requestURI
        log.info("GET ${path}")
        resp.writer << "HELLO THERE!!"
    }
}
