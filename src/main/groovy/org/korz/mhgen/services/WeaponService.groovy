package org.korz.mhgen.services

import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.korz.mhgen.models.ChargeType
import org.korz.mhgen.models.ElementType
import org.korz.mhgen.models.PhialType
import org.korz.mhgen.models.SharpnessType
import org.korz.mhgen.models.ShellType
import org.korz.mhgen.models.Weapon
import org.korz.mhgen.models.WeaponType

import javax.inject.Inject
import javax.inject.Singleton

@CompileStatic
@Singleton
@Slf4j
class WeaponService {

    static final String QUERY = '''
        SELECT i.name, w.*
        FROM items i
        JOIN weapons w USING (_id)
    '''

    List<Weapon> weapons

    @Inject
    WeaponService(Sql sql) {
        weapons = []
        sql.eachRow(QUERY) { weapons += parseWeapon((GroovyResultSet) it) }
        weapons = weapons.asImmutable()
        log.debug("Loaded ${weapons.size()} weapons")
    }

    private static Weapon parseWeapon(GroovyResultSet row) {
        def data = [:]
        data.name = row.getString('name')
        data.isFinal = row.getBoolean('final')
        data.type = getType(row.getString('wtype'))
        data.slots = row.getInt('num_slots')
        data.raw = row.getInt('attack')

        data.elements = ['element', 'element_2']
            .findAll { row.getString(it) }
            .collectEntries {
                [getElement(row.getString(it)), row.getInt("${it}_attack")]
            }

        data.affinity = row.getInt('affinity')

        def sharpnesses = row.getString('sharpness') ?: ''
        data.sharpnesses = sharpnesses.split(/ /)
            .findAll { it != '' }
            .collect {
                def types = SharpnessType.values().toList()
                def values = it.split(/\./).collect { it as int }
                // TODO: ghetto zip
                return types.collectEntries { [it, values[it.ordinal()]] }
            }

        def shelling = row.getString('shelling_type')
        if (shelling) {
            def parts = shelling.split(/ /)
            data.shellType = getShell(parts[0])
            data.shellLevel = parts[1] as int
        }

        data.phialType = getPhial(row.getString('phial'))

        def charges = row.getString('charges') ?: ''
        data.charges = charges.split(/\|/)
            .findAll { it != '' }
            .findAll { it != 'Rapid 0' }
            .collectEntries {
                def parts = it.split(/ /)
                return [getCharge(parts[0]), parts[1] as int]
            }

        return new Weapon(data)
    }

    private static WeaponType getType(String name) {
        return WeaponType.valueOf(name.toUpperCase().replace(' ', '_'))
    }

    private static ElementType getElement(String name) {
        if (!name) return null
        if (name == 'Blastblight') return ElementType.BLAST
        return ElementType.valueOf(name.toUpperCase())
    }

    private static ChargeType getCharge(String name) {
        return ChargeType.valueOf(name.toUpperCase())
    }

    private static ShellType getShell(String name) {
        return ShellType.valueOf(name.toUpperCase())
    }

    private static PhialType getPhial(String name) {
        if (!name) return null
        if (name == 'Exaust') return PhialType.EXHAUST
        return PhialType.valueOf(name.toUpperCase())
    }
}
