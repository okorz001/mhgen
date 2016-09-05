package org.korz.mhgen.models

class Weapon extends Item {
    private static final BigDecimal CRITICAL_MODIFIER = 0.25\

    static WeaponType getType(String name) {
        return WeaponType.valueOf(name.toUpperCase().replace(' ', '_'))
    }

    static Element getElement(String name) {
        if (!name) return null
        if (name == 'Blastblight') return Element.BLAST
        return Element.valueOf(name.toUpperCase())
    }

    boolean isFinal
    WeaponType type
    int slots
    int affinity
    int attack
    Element element
    int elementAttack
    Element element2
    int elementAttack2

    Weapon(row) {
        super(row)
        this.isFinal = row.final
        this.type = getType(row.wtype)
        this.slots = row.num_slots
        this.affinity = row.affinity as int
        this.attack = row.attack
        this.element = getElement(row.element)
        this.elementAttack = row.element_attack
        this.element2 = getElement(row.element_2)
        this.elementAttack2 = row.element_2_attack
    }

    BigDecimal getEffectiveAttack(int extraAffinity = 0) {
        return this.attack * (100 + ((this.affinity + extraAffinity) * CRITICAL_MODIFIER)) / 100;
    }
}
