package org.korz.mhgen.core
import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.Options
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache
import com.github.jknack.handlebars.io.ClassPathTemplateLoader
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.korz.mhgen.models.Sharpness

import java.math.RoundingMode

@CompileStatic
@Slf4j
class Templates {
    final Handlebars engine

    Templates() {
        this.engine = new Handlebars()
            .with(new ClassPathTemplateLoader(prefix: '/templates', suffix: '.hbs'))
            .with(new ConcurrentMapTemplateCache())

        this.engine.registerHelper('displayName', { Object thing, Options opts ->
            def x = thing.toString().split('_').collect { String it ->
                "${it.charAt(0)}${it.substring(1).toLowerCase()}"
            }.join(' ')
            return x
        })

        this.engine.registerHelper('getKey', { Map ctx, Options opts ->
            ctx[opts.param(0)]
        })

        this.engine.registerHelper('slots', { int slots, Options opts ->
            'O' * slots + '-' * (3 - slots)
        })

        this.engine.registerHelper('weaponImage', { Object weaponType, Options opts ->
            "/assets/weapons/${weaponType.toString().toLowerCase()}.png"
        })

        this.engine.registerHelper('elementImage', { Object element, Options opts ->
            "/assets/elements/${element.toString().toLowerCase()}.png"
        })

        this.engine.registerHelper('round', { Number n, Options opts ->
           n.toBigDecimal().setScale(opts.hash('digits', 2) as int, RoundingMode.HALF_UP).toString()
        })

        this.engine.registerHelper('sharpnessSvg', { Map<Sharpness, Integer> sharpness, Options opts ->
            def height = 30
            def html = new StringBuilder()
            html << """<svg viewBox="0 0 80 ${height}" width="100" height="${height}">"""

            def x = 0
            Sharpness.values().each { Sharpness color ->
                def value = 2 * sharpness[color]
                def fill = opts.hash(color.toString().toLowerCase())
                html << """<rect x="${x}" y="0" width="${value}" height="${height}" fill="${fill}"/>"""
                x += value
            }

            html << """</svg>"""
            return new Handlebars.SafeString(html.toString())
        })
    }

    String render(String name, Object context) {
        return this.engine.compile(name).apply(context)
    }

    String renderPage(String title, String name, Object context) {
        return this.render('layout', [title: title, partial: name, context: context])
    }
}
