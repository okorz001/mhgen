package org.korz.mhgen.core

import groovy.transform.Canonical
import spock.lang.Specification
import spock.lang.Unroll

class ParamParserSpec extends Specification {
    enum Color {
        RED,
        GREEN,
        BLUE
    }

    @Canonical
    static class Params {
        byte aByte
        Byte aByteBoxed
        short aShort
        Short aShortBoxed
        int aInt
        Integer aIntBoxed
        long aLong
        Long aLongBoxed
        BigInteger aBigInt
        float aFloat
        Float aFloatBoxed
        double aDouble
        Double aDoubleBoxed
        BigDecimal aBigDecimal
        char aChar
        Character aCharBoxed
        String aString
        Color aEnum
    }

    def PARSER = new ParamParser()

    @Unroll
    def 'Parse #prop: #value'() {
        when:
        def input = [:]
        input[prop] = [value] as String[]
        def params = PARSER.parse(input, Params)

        then:
        params.getProperty(prop) == expected

        where:
        prop           | value || expected
        'aByte'        | '1'   || 1
        'aByteBoxed'   | '2'   || 2
        'aShort'       | '3'   || 3
        'aShortBoxed'  | '4'   || 4
        'aInt'         | '5'   || 5
        'aIntBoxed'    | '6'   || 6
        'aLong'        | '7'   || 7
        'aLongBoxed'   | '8'   || 8
        'aBigInt'      | '9'   || 9
        'aFloat'       | '0.5' || 0.5
        'aFloatBoxed'  | '1.5' || 1.5
        'aDouble'      | '2.5' || 2.5
        'aDoubleBoxed' | '3.5' || 3.5
        'aBigDecimal'  | '4.5' || 4.5
        'aChar'        | 'a'   || 'a' as char
        'aCharBoxed'   | 'b'   || 'b' as Character
        'aString'      | 'd'   || 'd'
        'aEnum'        | 'RED' || Color.RED
    }

    @Unroll
    def 'Rejects #prop: #value'() {
        when:
        def input = [:]
        input[prop] = [value] as String[]
        def params = PARSER.parse(input, Params)

        then:
        thrown(IllegalArgumentException)

        where:
        prop           | value
        'unknownProp'  | 'zzz'
        'aByte'        | 'a'
        'aByteBoxed'   | 'b'
        'aShort'       | 'c'
        'aShortBoxed'  | 'd'
        'aInt'         | 'e'
        'aIntBoxed'    | 'f'
        'aLong'        | 'g'
        'aLongBoxed'   | 'h'
        'aBigInt'      | 'i'
        'aFloat'       | 'j'
        'aFloatBoxed'  | 'k'
        'aDouble'      | 'l'
        'aDoubleBoxed' | 'm'
        'aBigDecimal'  | 'n'
        'aChar'        | 'aa'
        'aCharBoxed'   | 'ab'
        'aEnum'        | 'FISH'
    }
}
