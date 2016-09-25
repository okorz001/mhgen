package org.korz.mhgen.core

import com.google.common.primitives.Primitives
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
            // Only parse parameters that are present.
            .findAll { params.containsKey(it.name) }
            // Consume the parameter so we can check for unknowns later.
            .each { parseParam(it, params.remove(it.name)[0]) }
        // Leftover params are unknown.
        if (params) {
            def msg = "Unknown params: ${params.keySet()}"
            throw new IllegalArgumentException(msg)
        }
        return bean
    }

    private static parseParam(PropertyValue prop, String value) {
        // Allow empty parameters.
        if (!value) {
            return
        }

        log.debug("${prop.name} => ${prop.type}")
        // Box primitive types so they are easier to work with.
        def type = Primitives.wrap(prop.type)

        if (Number.isAssignableFrom(type)) {
            log.debug("${prop.name} is a number!")
            prop.value = value as BigDecimal
            if (prop.value as String != value) {
                def msg = "Cannot parse param [${prop.name}] of type " +
                    "[${prop.type}] from value [${value}]"
                // TODO: we've already clobbered prop.value at this point
                throw new IllegalArgumentException(msg)
            }
        }
        else if (Character.isAssignableFrom(type)) {
            log.debug("${prop.name} is a char!")
            if (value.size() > 1) {
                def msg = "Cannot parse param [${prop.name}] of type " +
                    "[${prop.type}] from value [${value}]"
                throw new IllegalArgumentException(msg)
            }
            prop.value = value[0]
        }
        else if (CharSequence.isAssignableFrom(type)) {
            log.debug("${prop.name} is a string!")
            prop.value = value
        }
        else if (Enum.isAssignableFrom(type)) {
            log.debug("${prop.name} is an enum!")
            try {
                prop.value = prop.type.invokeMethod('valueOf', value)
            }
            catch (e) {
                def msg = "Cannot parse param [${prop.name}] of type " +
                    "[${prop.type}] from value [${value}]"
                throw new IllegalArgumentException(msg, e)
            }
        }
        else {
            def msg = "Cannot parse param [${prop.name}] of type " +
                "[${prop.type}]"
            throw new IllegalArgumentException(msg)
        }
    }
}
