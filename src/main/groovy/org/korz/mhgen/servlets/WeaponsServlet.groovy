package org.korz.mhgen.servlets

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.korz.mhgen.core.ParamParser
import org.korz.mhgen.core.Templates
import org.korz.mhgen.models.ElementType
import org.korz.mhgen.models.PhialType
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
    static enum Sort {
        RAW,
        ELEMENT,
        EFFECTIVE_RAW,
        EFFECTIVE_ELEMENT
    }

    static class Params {
        boolean isFinal = true
        WeaponType weaponType
        int slots
        ElementType elementType
        PhialType phialType

        // Skills
        int sharpnessUp

        Sort sort = Sort.RAW
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
        if (params.weaponType) {
            results = results.findAll { it.type == params.weaponType }
        }
        if (params.slots) {
            results = results.findAll { it.slots >= params.slots }
        }
        if (params.elementType) {
            results = results.findAll { it.elements[params.elementType] }
        }
        if (params.phialType) {
            results = results.findAll { it.phialType == params.phialType }
        }

        results = results.sort {
            switch (params.sort) {
                case Sort.RAW:
                    return -it.raw
                case Sort.ELEMENT:
                    return -it.elements[params.elementType]
            }
        }

        def context = [pjax: false, params: params, ws: results]
        templates.render('weapons', context, resp.writer)
    }
}
