package org.korz.mhgen.models

import groovy.transform.CompileStatic

@CompileStatic
enum WeaponType {
    BOW(blademaster: false, critElement: 0.35),
    CHARGE_BLADE(blademaster: true, critElement: 0.25),
    DUAL_BLADES(blademaster: true, critElement: 0.35),
    GREAT_SWORD(blademaster: true, critElement: 0.20),
    GUNLANCE(blademaster: true, critElement: 0.25),
    HAMMER(blademaster: true, critElement: 0.25),
    HEAVY_BOWGUN(blademaster: false, critElement: 0.30),
    HUNTING_HORN(blademaster: true, critElement: 0.25),
    INSECT_GLAIVE(blademaster: true, critElement: 0.25),
    LANCE(blademaster: true, critElement: 0.25),
    LIGHT_BOWGUN(blademaster: false, critElement: 0.30),
    LONG_SWORD(blademaster: true, critElement: 0.25),
    SWITCH_AXE(blademaster: true, critElement: 0.25),
    SWORD_AND_SHIELD(blademaster: true, critElement: 0.35)

    final boolean blademaster
    final BigDecimal critElement

    boolean isGunner() {
        !blademaster
    }

    String getIcon() {
        "/assets/weapons/${toString().toLowerCase()}.png"
    }
}
