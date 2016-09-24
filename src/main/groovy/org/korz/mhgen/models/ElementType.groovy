package org.korz.mhgen.models

import groovy.transform.CompileStatic

@CompileStatic
enum ElementType {
    FIRE,
    WATER,
    THUNDER,
    ICE,
    DRAGON,
    POISON,
    PARALYSIS,
    SLEEP,
    BLAST

    String getIcon() {
        "/assets/elements/${toString().toLowerCase()}.png"
    }
}
