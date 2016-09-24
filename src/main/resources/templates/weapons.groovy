import org.korz.mhgen.models.Weapon

modelTypes = {
    List<Weapon> ws
}

layout 'layout', true,
    title: 'Weapons TEST',
    content: contents {
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