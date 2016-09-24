package org.korz.mhgen.models

import groovy.transform.CompileStatic

@CompileStatic
enum SharpnessType {
    RED(raw: 0.50, element: 0.25, bludgeoner: 30),
    ORANGE(raw: 0.75, element: 0.50, bludgeoner: 30),
    YELLOW(raw: 1.0, element: 0.75, bludgeoner: 25),
    GREEN(raw: 1.05, element: 1.0, bludgeoner: 15),
    BLUE(raw: 1.20, element: 1.0625, bludgeoner: 0),
    WHITE(raw: 1.32, element: 1.125, bludgeoner: 0)

    final BigDecimal raw
    final BigDecimal element
    final int bludgeoner
}
