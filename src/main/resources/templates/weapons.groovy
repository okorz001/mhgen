import org.korz.mhgen.models.Weapon
import org.korz.mhgen.models.WeaponType
import org.korz.mhgen.servlets.WeaponsServlet

modelTypes = {
    WeaponsServlet.Params params
    List<Weapon> ws
}

def displayName(thing) {
    thing.toString().split(/_/).collect { String it ->
        it.substring(0, 1) + it.substring(1).toLowerCase()
    }.join(' ')
}

layout 'layout', true,
    title: 'Weapons TEST',
    content: contents {
        form(method: 'get') {
            select(name: 'type') {
                WeaponType.values().each {
                    Map attrs = [value: it.toString()]
                    if (it == params.type) {
                        attrs.selected = 'selected'
                    }
                    option(attrs, displayName(it))
                }
            }
            select(name: 'slots') {
                [0, 1, 2, 3].each {
                    Map attrs = [value: it.toString()]
                    if (it == params.slots) {
                        attrs.selected = 'selected'
                    }
                    option(attrs, it.toString())
                }
            }
            button('Update', type: 'submit')
        }
        table(id: 'weapons') {
            thead {
                tr {
                    th('Type')
                    th('Name')
                    th('Slots')
                    th('Raw')
                    th('Element')
                    th('Affinity')
                    th('Sharpness')
                    th('Other')
                }
            }
            tbody {
                ws.each { Weapon w ->
                    tr {
                        td {
                            img(class: 'weapon',
                                alt: w.type,
                                src: w.type.icon)
                        }
                        td(w.name)
                        td(w.slots)
                        td(w.raw)
                        td {
                            w.elements.each {
                                img(class: 'element',
                                    alt: it.key,
                                    src: it.key.icon)
                                yield it.value
                                br()
                            }
                        }
                        td("${w.affinity}%")
                        td {
                            if (w.sharpnesses) {
                                svg(class: 'sharpness', viewBox: '0 0 40 10') {
                                    def x = 0
                                    w.sharpnesses[0].each {
                                        rect(class: it.key.toString().toLowerCase(),
                                             x: x,
                                             y: 0,
                                             width: it.value,
                                             height: 10)
                                        x += it.value
                                    }
                                }
                            }
                        }
                        td(class: 'other') {
                            if (w.shellType) {
                                div("${w.shellType} ${w.shellLevel}")
                            }
                            if (w.phialType) {
                                div(w.phialType)
                            }
                            if (w.charges) {
                                div {
                                    w.charges.each {
                                        yield "${it.key} ${it.value}"
                                        br()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }