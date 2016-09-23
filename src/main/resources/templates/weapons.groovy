modelTypes = {
}

layout 'layout', true,
    title: 'Weapons TEST',
    content: contents {
        p("Hello world!!")
        hr()
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
                // TODO
            }
        }
    }