package org.korz.mhgen.models

import groovy.transform.CompileStatic
import groovy.transform.Immutable

@CompileStatic
@Immutable
class Weapon {
    String name
    boolean isFinal
    WeaponType type
    int slots
    int raw
    LinkedHashMap<ElementType, Integer> elements
    int affinity
    List<Map<SharpnessType, Integer>> sharpnesses
    ShellType shellType
    Integer shellLevel
    PhialType phialType
    LinkedHashMap<ChargeType, Integer> charges
}
