package org.korz.mhgen.core

import com.google.common.primitives.Primitives
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.inject.Singleton

@CompileStatic
@Singleton
@Slf4j
class ParamParser {
    def <T> T parse(Map<String, String[]> params, Class<T> beanClass) {
        return parse(params, beanClass.newInstance())
    }

    def <T> T parse(Map<String, String[]> params, T bean) {
        // Make a copy so we can destroy it.
        params = new HashMap<String, String[]>(params)
        bean.metaPropertyValues
            .findAll { it.name != 'class' }
            .sort { it.name }
            .each {
                if (!params.containsKey(it.name)) {
                    return
                }

                log.debug("${it.name} => ${it.type}")
                // Box primitive types so they are easier to work with.
                def type = Primitives.wrap(it.type)
                // Consume the parameter so we can check for unknowns later.
                def value = params.remove(it.name)[0]

                if (Number.isAssignableFrom(type)) {
                    log.debug("${it.name} is a number!")
                    // TODO: check for overflow
                    it.value = value as BigDecimal
                }
                else if (Character.isAssignableFrom(type)) {
                    log.debug("${it.name} is a char!")
                    if (value.size() > 1) {
                        def msg = "Cannot parse param [${it.name}] of type " +
                            "[${it.type}] from value [${value}]"
                        throw new IllegalArgumentException(msg)
                    }
                    it.value = value[0]
                }
                else if (CharSequence.isAssignableFrom(type)) {
                    log.debug("${it.name} is a string!")
                    it.value = value
                }
                else if (Enum.isAssignableFrom(type)) {
                    log.debug("${it.name} is an enum!")
                    it.value = it.type.invokeMethod('valueOf', value)
                }
                else {
                    def msg = "Cannot parse param [${it.name}] of type " +
                        "[${it.type}]"
                    throw new IllegalArgumentException(msg)
                }
            }

        // Params are consumed when parsing, so any leftovers are unknown
        if (params) {
            def msg = "Unknown params: ${params.keySet}"
            throw new IllegalArgumentException(msg)
        }

        return bean
    }
}
