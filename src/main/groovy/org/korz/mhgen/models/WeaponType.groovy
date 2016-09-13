package org.korz.mhgen.models

import groovy.transform.CompileStatic

@CompileStatic
enum WeaponType {
    BOW(critElement: 0.35),
    CHARGE_BLADE(critElement: 0.25),
    DUAL_BLADES(critElement: 0.35),
    GREAT_SWORD(critElement: 0.20),
    GUNLANCE(critElement: 0.25),
    HAMMER(critElement: 0.25),
    HEAVY_BOWGUN(critElement: 0.30),
    HUNTING_HORN(critElement: 0.25),
    INSECT_GLAIVE(critElement: 0.25),
    LANCE(critElement: 0.25),
    LIGHT_BOWGUN(critElement: 0.30),
    LONG_SWORD(critElement: 0.25),
    SWITCH_AXE(critElement: 0.25),
    SWORD_AND_SHIELD(critElement: 0.35)

    final BigDecimal critElement
}
