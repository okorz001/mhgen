package org.korz.mhgen.models

import groovy.transform.CompileStatic

@CompileStatic
enum Sharpness {
    RED(raw: 0.50, element: 0.25),
    ORANGE(raw: 0.75, element: 0.50),
    YELLOW(raw: 1.0, element: 0.75),
    GREEN(raw: 1.05, element: 1.0),
    BLUE(raw: 1.20, element: 1.0625),
    WHITE(raw: 1.32, element: 1.125)

    final BigDecimal raw
    final BigDecimal element
}