import org.korz.mhgen.models.ElementType
import org.korz.mhgen.models.WeaponType
import org.korz.mhgen.servlets.WeaponsServlet

modelTypes = {
    WeaponsServlet.Params params
    List<WeaponsServlet.Result> results
}

String displayName(thing) {
    thing.toString().split(/_/).collect { String it ->
        it.substring(0, 1) + it.substring(1).toLowerCase()
    }.join(' ')
}

class Option {
    String name
    Object value
}

void createOptions(List<Option> values, Object selected = null) {
    values.each {
        Map attrs = [value: it.value ? it.value.toString(): '']
        if (it.value == selected) {
            attrs.selected = 'selected'
        }
        option(attrs, it.name)
    }
}

layout 'layout', true,
    title: 'Weapons TEST',
    content: contents {
        form(method: 'get') {
            select(name: 'weaponType') {
                def options = [new Option(name: 'All')]
                WeaponType.values().each {
                    options << new Option(name: displayName(it), value: it)
                }
                createOptions(options, params.weaponType)
            }
            select(name: 'slots') {
                def options = [0, 1, 2, 3].collect {
                    new Option(name: it.toString(), value: it)
                }
                createOptions(options, params.slots)
            }
            select(name: 'elementType') {
                def options = [new Option(name: 'All')]
                ElementType.values().each {
                    options << new Option(name: displayName(it), value: it)
                }
                createOptions(options, params.elementType)
            }
            select(name: 'sharpnessUp') {
                def options = [0, 1, 2].collect {
                    new Option(name: it.toString(), value: it)
                }
                createOptions(options, params.sharpnessUp)
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