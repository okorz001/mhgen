package org.korz.mhgen.resources
import org.korz.mhgen.core.Db
import org.korz.mhgen.models.Element
import org.korz.mhgen.models.Weapon
import org.korz.mhgen.models.WeaponType

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.DefaultValue
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@CompileStatic
@Path('/weapons')
@Produces(MediaType.APPLICATION_JSON)
@Singleton
@Slf4j
class WeaponsResource {
    enum Sort {
        ATTACK,
        EFFECTIVE_ATTACK,
        ELEMENT
    }

    List<Weapon> weapons = []

    @Inject
    WeaponsResource(Db db) {
        db.sql.eachRow("select i.name, i.rarity, w.* from items i JOIN weapons w using (_id)") {
            this.weapons += new Weapon(it)
        }
        log.info("Loaded ${this.weapons.size()} weapons")
    }

    @GET
    List<Weapon> get(@QueryParam('final') @DefaultValue('true') boolean isFinal,
                     @QueryParam('type') WeaponType type,
                     @QueryParam('slots') @DefaultValue('0') int slots,
                     @QueryParam('element') Element element,
                     @QueryParam('sort') @DefaultValue('ATTACK') Sort sort) {
        def count = 100
        return this.weapons.findAll {
            if (isFinal && !it.isFinal) return false
            if (type && it.type != type) return false
            if (it.slots < slots) return false
            if (element && it.element != element && it.element2 != element) return false
            return true
        }.sort {
            switch(sort) {
                case Sort.ATTACK:
                    return -it.attack
                case Sort.EFFECTIVE_ATTACK:
                    return -it.getEffectiveAttack()
                case Sort.ELEMENT:
                    def value = it.element2 == element ? it.elementAttack2 : it.elementAttack
                    if (it.elementAttack2) value /= 2
                    return -value
            }
        }.findAll {
            count-- > 0
        }
    }
}
