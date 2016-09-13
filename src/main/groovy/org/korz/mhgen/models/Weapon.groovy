package org.korz.mhgen.models

//@CompileStatic
//@Immutable
class Weapon extends Item {
    static WeaponType getType(String name) {
        return WeaponType.valueOf(name.toUpperCase().replace(' ', '_'))
    }

    static Element getElement(String name) {
        if (!name) return null
        if (name == 'Blastblight') return Element.BLAST
        return Element.valueOf(name.toUpperCase())
    }

    static Map<Sharpness, Integer> getSharpness(String value) {
        if (!value) return null
        def values = value.split('\\.').collect { it as int }
        return Collections.unmodifiableMap([
            (Sharpness.RED): values[0],
            (Sharpness.ORANGE): values[1],
            (Sharpness.YELLOW): values[2],
            (Sharpness.GREEN): values[3],
            (Sharpness.BLUE): values[4],
            (Sharpness.WHITE): values[5],
        ])
    }

    boolean isFinal
    WeaponType type
    int slots
    int raw
    Map<Element, Integer> elements
    int affinity
    List<Map<Sharpness, Integer>> sharpness

    Weapon(row) {
        super(row)
        this.isFinal = row.final
        this.type = getType(row.wtype)
        this.slots = row.num_slots
        this.raw = row.attack
        this.affinity = row.affinity as int

        this.elements = new LinkedHashMap()
        if (row.element) {
            this.elements[getElement(row.element)] = row.element_attack
            if (row.element_2) {
                this.elements[getElement(row.element_2)] = row.element_2_attack
            }
        }

        if (row.sharpness) {
            this.sharpness = row.sharpness.split(' ').collect { String data -> getSharpness(data) }
        }
    }
}
