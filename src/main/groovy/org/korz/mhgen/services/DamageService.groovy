package org.korz.mhgen.services

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.korz.mhgen.models.ElementType
import org.korz.mhgen.models.SharpnessType
import org.korz.mhgen.models.Weapon

import javax.inject.Singleton
import java.math.RoundingMode

@CompileStatic
@Singleton
@Slf4j
class DamageService {

    static final int[] ATTACK_UP = [0, 10, 15, 20]
    static final int[] CRITICAL_UP = [0, 10, 20, 30]

    private static SharpnessType getMaxSharpness(Weapon weapon,
                                                 int sharpnessUp) {
        if (!weapon.sharpnesses) {
            return null
        }
        def sharpness = weapon.sharpnesses[sharpnessUp]
        return sharpness.keySet().findAll { sharpness[it] }.max()
    }

    private static BigDecimal round(BigDecimal n) {
        n.setScale(2, RoundingMode.HALF_UP)
    }

    BigDecimal getEffectiveRaw(Weapon weapon,
                               int attackUp,
                               boolean critBoost,
                               int criticalUp,
                               int sharpnessUp) {
        def sharpness = getMaxSharpness(weapon, sharpnessUp)

        def attack = weapon.raw + ATTACK_UP[attackUp]
        if (weapon.type.blademaster) {
            attack += sharpness.bludgeoner
        }

        def affinity = weapon.affinity + CRITICAL_UP[criticalUp]
        if (affinity >= 10) {
            //affinity += 5
        }

        def crit = 0.25
        // Critical Boost does not affect negative affinity
        if (critBoost && affinity > 0) {
            crit += 0.15
        }

        return calculateDamage(attack, affinity, crit, sharpness?.raw)
    }

    BigDecimal getEffectiveElement(Weapon weapon,
                                   ElementType element,
                                   int attackUp,
                                   boolean critElement,
                                   int criticalUp,
                                   int sharpnessUp) {
        def sharpness = getMaxSharpness(weapon, sharpnessUp)

        def attack = weapon.elements[element] + ATTACK_UP[attackUp]

        def affinity = weapon.affinity + CRITICAL_UP[criticalUp]
        if (affinity >= 10) {
            //affinity += 5
        }

        // Critical Element does not affect negative affinity
        def crit = 0.0
        if (critElement && affinity > 0) {
            crit += weapon.type.critElement
        }

        return calculateDamage(attack, affinity, crit, sharpness?.element)
    }

    BigDecimal calculateDamage(int attack,
                               int affinity,
                               BigDecimal critical,
                               BigDecimal sharpness) {
        def effectiveCrit = 1.0 + (affinity / 100) * critical
        return round(attack * effectiveCrit * (sharpness ?: 1.0))
    }
}
