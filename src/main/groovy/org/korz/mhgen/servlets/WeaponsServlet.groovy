package org.korz.mhgen.servlets

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.korz.mhgen.core.ParamParser
import org.korz.mhgen.core.Templates
import org.korz.mhgen.models.ElementType
import org.korz.mhgen.models.PhialType
import org.korz.mhgen.models.Weapon
import org.korz.mhgen.models.WeaponType
import org.korz.mhgen.services.DamageService
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
        EFFECTIVE_RAW,
        EFFECTIVE_ELEMENT,
        DISPLAY_RAW,
        DISPLAY_ELEMENT,
    }

    static class Params {
        boolean isFinal = true
        WeaponType weaponType
        int slots
        ElementType elementType
        PhialType phialType

        // Skills
        int attackUp
        boolean critBoost
        boolean critElement
        int criticalUp
        int sharpnessUp

        Sort sort = Sort.EFFECTIVE_RAW
    }

    static class Result {
        Weapon weapon
        BigDecimal raw
        Map<ElementType, BigDecimal> elements = [:]
    }

    @Inject
    ParamParser parser

    @Inject
    WeaponService weaponService

    @Inject
    DamageService damageService

    @Inject
    Templates templates

    @Override
    void doGet(HttpServletRequest req, HttpServletResponse resp) {
        def params = parser.parse(req.parameterMap, Params)
        def weapons = weaponService.weapons
        if (params.isFinal) {
            weapons = weapons.findAll { it.isFinal }
        }
        if (params.weaponType) {
            weapons = weapons.findAll { it.type == params.weaponType }
        }
        if (params.slots) {
            weapons = weapons.findAll { it.slots >= params.slots }
        }
        if (params.elementType) {
            weapons = weapons.findAll { it.elements[params.elementType] }
        }
        if (params.phialType) {
            weapons = weapons.findAll { it.phialType == params.phialType }
        }

        def results = weapons.collect { weapon ->
            Result result = new Result()
            result.weapon = weapon
            result.raw = damageService.getEffectiveRaw(weapon,
                                                       params.attackUp,
                                                       params.critBoost,
                                                       params.criticalUp,
                                                       params.sharpnessUp)
            weapon.elements.each {
                def attack = damageService.getEffectiveElement(weapon,
                                                               it.key,
                                                               params.attackUp,
                                                               params.critElement,
                                                               params.criticalUp,
                                                               params.sharpnessUp)
                result.elements[it.key] = attack
            }
            return result
        }

        results = results.sort {
            switch (params.sort) {
                case Sort.EFFECTIVE_RAW:
                    return -it.raw
                case Sort.EFFECTIVE_ELEMENT:
                    return -it.elements[params.elementType]
                case Sort.DISPLAY_RAW:
                    return -it.weapon.raw
                case Sort.DISPLAY_ELEMENT:
                    return -it.weapon.elements[params.elementType]
            }
        }

        def context = [pjax: false, params: params, results: results]
        templates.render('weapons', context, resp.writer)
    }
}
