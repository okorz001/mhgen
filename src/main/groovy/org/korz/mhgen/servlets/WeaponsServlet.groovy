package org.korz.mhgen.servlets

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.korz.mhgen.core.Templates
import org.korz.mhgen.services.WeaponService

import javax.inject.Inject
import javax.inject.Singleton
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@CompileStatic
@Singleton
@Slf4j
class WeaponsServlet extends HttpServlet {

    @Inject
    WeaponService service

    @Inject
    Templates templates

    @Override
    void doGet(HttpServletRequest req, HttpServletResponse resp) {
        def context = [pjax: false,
                       ws: service.weapons]
        templates.render('weapons', context, resp.writer)
    }
}
