package org.korz.mhgen.core

import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration
import groovy.text.markup.TemplateResolver
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Inject
import javax.inject.Singleton

@CompileStatic
@Singleton
@Slf4j
class Templates {

    static final String TEMPLATE_DIR = 'templates/'
    static final String TEMPLATE_EXTENSION = '.groovy'

    private static class Resolver implements TemplateResolver {
        ClassLoader loader

        @Override
        void configure(ClassLoader loader, TemplateConfiguration config) {
            this.loader = loader
        }

        @Override
        URL resolveTemplate(String path) {
            loader.getResource(TEMPLATE_DIR + path + TEMPLATE_EXTENSION)
        }
    }

    private MarkupTemplateEngine engine

    @Inject
    Templates() {
        def loader = this.class.classLoader
        def config = new TemplateConfiguration(autoIndent: true,
                                               autoNewLine: true)
        def resolver = new Resolver()
        engine = new MarkupTemplateEngine(loader, config, resolver)

    }

    void render(String name, Map context, Writer writer) {
        engine.createTemplateByPath(name).make(context).writeTo(writer)
    }
}
