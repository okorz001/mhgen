import org.korz.mhgen.models.ElementType
import org.korz.mhgen.models.WeaponType
import org.korz.mhgen.servlets.WeaponsServlet

modelTypes = {
    WeaponsServlet.Params params
    List<WeaponsServlet.Result> results
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
            select(name: 'weaponType') {
                // Don't need to select this one since it's first anyway.
                option(value: '', 'All')

                WeaponType.values().each {
                    Map attrs = [value: it.toString()]
                    if (it == params.weaponType) {
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
            select(name: 'elementType') {
                // Don't need to select this one since it's first anyway.
                option(value: '', 'All')

                ElementType.values().each {
                    Map attrs = [value: it.toString()]
                    if (it == params.elementType) {
                        attrs.selected = 'selected'
                    }
                    option(attrs, displayName(it))
                }
            }
            select(name: 'sharpnessUp') {
                [0, 1, 2].each {
                    Map attrs = [value: it.toString()]
                    if (it == params.sharpnessUp) {
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
                results.each { WeaponsServlet.Result result ->
                    def w = result.weapon
                    tr {
                        td {
                            img(class: 'weapon',
                                alt: w.type,
                                src: w.type.icon)
                        }
                        td(w.name)
                        td(w.slots)
                        td("${w.raw} (${result.raw})")
                        td {
                            w.elements.each { element, value ->
                                img(class: 'element',
                                    alt: element,
                                    src: element.icon)
                                yield "${value} (${result.elements[element]})"
                                br()
                            }
                        }
                        td("${w.affinity}%")
                        td {
                            if (w.sharpnesses) {
                                svg(class: 'sharpness', viewBox: '0 0 40 10') {
                                    def x = 0
                                    w.sharpnesses[params.sharpnessUp].each {
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