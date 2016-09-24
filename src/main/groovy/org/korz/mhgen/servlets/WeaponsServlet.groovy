package org.korz.mhgen.servlets

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.korz.mhgen.core.ParamParser
import org.korz.mhgen.core.Templates
import org.korz.mhgen.models.WeaponType
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

    static class Params {
        boolean isFinal = true
        WeaponType type
        int slots

        // Skills
        int sharpnessUp
    }

    @Inject
    ParamParser parser

    @Inject
    WeaponService service

    @Inject
    Templates templates

    @Override
    void doGet(HttpServletRequest req, HttpServletResponse resp) {
        def params = parser.parse(req.parameterMap, Params)
        def results = service.weapons
        if (params.isFinal) {
            results = results.findAll { it.isFinal }
        }
        if (params.type) {
            results = results.findAll { it.type == params.type }
        }
        if (params.slots) {
            results = results.findAll { it.slots >= params.slots }
        }

        def context = [pjax: false, params: params, ws: results]
        templates.render('weapons', context, resp.writer)
    }
}
