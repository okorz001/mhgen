package org.korz.mhgen.models

import groovy.transform.CompileStatic

@CompileStatic
enum ElementType {
    FIRE(element: true, status: false),
    WATER(element: true, status: false),
    THUNDER(element: true, status: false),
    ICE(element: true, status: false),
    DRAGON(element: true, status: false),
    POISON(element: false, status: true),
    PARALYSIS(element: false, status: true),
    SLEEP(element: false, status: true),
    BLAST(element: false, status: false)

    boolean element
    boolean status

    String getIcon() {
        "/assets/elements/${toString().toLowerCase()}.png"
    }
}
