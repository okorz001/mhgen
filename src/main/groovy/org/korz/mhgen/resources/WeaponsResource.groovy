package org.korz.mhgen.resources

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.korz.mhgen.core.Db
import org.korz.mhgen.core.Templates
import org.korz.mhgen.models.ChargeType
import org.korz.mhgen.models.ElementType
import org.korz.mhgen.models.PhialType
import org.korz.mhgen.models.SharpnessType
import org.korz.mhgen.models.ShellType
import org.korz.mhgen.models.Weapon
import org.korz.mhgen.models.WeaponType

import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

//@CompileStatic
@Path('/weapons')
@Produces(MediaType.TEXT_HTML)
@Singleton
@Slf4j
class WeaponsResource {
    enum Sort {
        RAW,
        ELEMENT,
        EFFECTIVE_RAW,
        EFFECTIVE_ELEMENT
    }

    static class WeaponView {
        Weapon weapon
        int attackUp
        int criticalUp
        int elementUp
        int sharpnessUp
        boolean bludgeoner
        boolean critBoost
        boolean critElement

        BigDecimal getRaw() {
            def base = this.weapon.raw
            if (this.attackUp) {
                // S 10, M 15, L 20, XL 25
                base += 5 * (this.attackUp + 1)
            }
            if (this.bludgeoner && this.weapon.sharpness) {
                base += bestSharpness(this.sharpness).bludgeoner
            }
            def affinity = this.weapon.affinity
            if (this.criticalUp) {
                // +1 10, +2 20, +3 30
                affinity += this.criticalUp * 10
            }
            def crit = this.critBoost ? 0.40 : 0.25
            def sharpness = this.weapon.sharpness ? bestSharpness(this.sharpness).raw : 1.0
            return damage(base, affinity, crit, sharpness)
        }

        Map<ElementType, BigDecimal> getElements() {
            this.weapon.elements.collectEntries(new LinkedHashMap()) { element, value ->
                def base = value
                if (this.weapon.elements.size() == 2) {
                    // Dual Blades with two elements are effective half, since only one blade has each element
                    base /= 2
                }
                if (this.elementUp) {
                    // TODO: how much does Fire Atk +1, et al add?
                }
                def affinity = this.weapon.affinity
                if (this.criticalUp) {
                    // +1 10, +2 20, +3 30
                    affinity += this.criticalUp * 10
                }
                def crit = this.critElement ? this.weapon.type.critElement : 0.0
                def sharpness = this.weapon.sharpness ? bestSharpness(this.sharpness).element : 1.0
                return [element, damage(base, affinity, crit, sharpness)]
            }
        }

        Map<SharpnessType, Integer> getSharpness() {
            if (this.weapon.sharpness) {
                return this.weapon.sharpness[this.sharpnessUp]
            }
            return null
        }

        private static SharpnessType bestSharpness(Map<SharpnessType, ?> sharpness) {
            if (sharpness[SharpnessType.WHITE]) {
                return SharpnessType.WHITE
            }
            else if (sharpness[SharpnessType.BLUE]) {
                return SharpnessType.BLUE
            }
            else if (sharpness[SharpnessType.GREEN]) {
                return SharpnessType.GREEN
            }
            else if (sharpness[SharpnessType.YELLOW]) {
                return SharpnessType.YELLOW
            }
            else if (sharpness[SharpnessType.ORANGE]) {
                return SharpnessType.ORANGE
            }
            return SharpnessType.RED
        }

        private static BigDecimal damage(int base,
                                         int affinity,
                                         BigDecimal crit,
                                         BigDecimal sharpness) {
            return base * (1 + affinity / 100.0 * crit) * sharpness
        }
    }

    List<Weapon> weapons = []
    Templates templates

    @Inject
    WeaponsResource(Db db, Templates templates) {
        db.sql.eachRow("select i.name, i.rarity, w.* from items i JOIN weapons w using (_id)") {
            //this.weapons += new Weapon(it)
        }
        log.info("Loaded ${this.weapons.size()} weapons")
        this.templates = templates
    }

    @GET
    String get(@QueryParam('final') @DefaultValue('true') boolean isFinal,
               @QueryParam('type') WeaponType type,
               @QueryParam('slots') int slots,
               @QueryParam('element') ElementType element,
               @QueryParam('sort') @DefaultValue('EFFECTIVE_RAW') Sort sort,
               @QueryParam('shellType') ShellType shellType,
               @QueryParam('shellLevel') int shellLevel,
               @QueryParam('phialType') PhialType phialType,
               @QueryParam('chargeType') ChargeType chargeType,
               @QueryParam('chargeLevel') int chargeLevel,
               // Skills which affect damage formula:
               @QueryParam('attackUp') int attackUp,
               @QueryParam('criticalUp') int criticalUp,
               @QueryParam('elementUp') int elementUp,
               @QueryParam('sharpnessUp') int sharpnessUp,
               @QueryParam('bludgeoner') boolean bludgeoner,
               @QueryParam('critBoost') boolean critBoost,
               @QueryParam('critElement') boolean critElement) {
        def weapons = this.weapons
        // Filter global weapons
        if (isFinal) {
            weapons = weapons.findAll { it.isFinal }
        }
        if (type) {
            weapons = weapons.findAll { it.type == type }
        }
        if (slots) {
            weapons = weapons.findAll { it.slots >= slots }
        }
        if (element) {
            weapons = weapons.findAll { it.elements[element] }
        }
        if (shellType) {
            weapons = weapons.findAll { it.shellType == shellType }
        }
        if (shellLevel) {
            weapons = weapons.findAll { it.shellLevel >= shellLevel }
        }
        if (phialType) {
            weapons = weapons.findAll { it.phialType == phialType }
        }
        if (chargeType || chargeLevel) {
            weapons = weapons.findAll {
                def charges = it.charges
                if (chargeType) {
                    charges = charges.findAll { it.type == chargeType }
                }
                if (chargeLevel) {
                    charges = charges.findAll { (it.level as int) >= chargeLevel }
                }
                return charges
            }
        }
        // Transform global weapons to request scoped view
        weapons = weapons.collect { new WeaponView(weapon: it,
                                                   attackUp: attackUp,
                                                   criticalUp: criticalUp,
                                                   elementUp: elementUp,
                                                   sharpnessUp: sharpnessUp,
                                                   bludgeoner: bludgeoner,
                                                   critBoost: critBoost,
                                                   critElement: critElement) }
        // Sort views
        weapons = weapons.sort { WeaponView view ->
            switch(sort) {
                case Sort.RAW:
                    return -view.weapon.raw
                case Sort.ELEMENT:
                    return -view.weapon.elements[element]
                case Sort.EFFECTIVE_RAW:
                    return -view.raw
                case Sort.EFFECTIVE_ELEMENT:
                    return -view.elements[element]
            }
        }
        // Truncate views
        def count = 100
        weapons = weapons.findAll { count-- > 0 }
        // Render template
        /*
        return this.templates.renderPage('Weapons', 'weapons', [types: WeaponType.values(),
                                                                elements: ElementType.values(),
                                                                weapons: weapons])
    */
    }
}
