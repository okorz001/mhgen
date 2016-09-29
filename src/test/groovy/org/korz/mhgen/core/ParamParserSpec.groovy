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
        boolean aBoolean
        Boolean aBooleanBoxed
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
        prop            | value   || expected
        'aByte'         | '1'     || 1
        'aByteBoxed'    | '2'     || 2
        'aShort'        | '3'     || 3
        'aShortBoxed'   | '4'     || 4
        'aInt'          | '5'     || 5
        'aIntBoxed'     | '6'     || 6
        'aLong'         | '7'     || 7
        'aLongBoxed'    | '8'     || 8
        'aBigInt'       | '9'     || 9
        'aFloat'        | '0.1'   || 0.1f
        'aFloatBoxed'   | '1.1'   || 1.1f
        'aDouble'       | '2.1'   || 2.1d
        'aDoubleBoxed'  | '3.1'   || 3.1d
        'aBigDecimal'   | '4.1'   || 4.1
        'aChar'         | 'a'     || 'a' as char
        'aCharBoxed'    | 'b'     || 'b' as Character
        'aString'       | 'd'     || 'd'
        'aEnum'         | 'RED'   || Color.RED
        'aBoolean'      | 'true'  || true
        'aBoolean'      | 'false' || false
        'aBoolean'      | '1'     || true
        'aBoolean'      | '0'     || false
        'aBooleanBoxed' | 'true'  || true
        'aBooleanBoxed' | 'false' || false
        'aBooleanBoxed' | '1'     || true
        'aBooleanBoxed' | '0'     || false
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
        prop            | value
        // Unknown parameter
        'unknownParam'  | 'zzz'
        // Not a number
        'aByte'         | 'a'
        'aByteBoxed'    | 'b'
        'aShort'        | 'c'
        'aShortBoxed'   | 'd'
        'aInt'          | 'e'
        'aIntBoxed'     | 'f'
        'aLong'         | 'g'
        'aLongBoxed'    | 'h'
        'aBigInt'       | 'i'
        'aFloat'        | 'j'
        'aFloatBoxed'   | 'k'
        'aDouble'       | 'l'
        'aDoubleBoxed'  | 'm'
        'aBigDecimal'   | 'n'
        // Not a single character
        'aChar'         | 'aa'
        'aCharBoxed'    | 'ab'
        // Unknown enum value
        'aEnum'         | 'FISH'
        // Overflow
        'aByte'         | '128'
        'aByte'         | '-129'
        'aByteBoxed'    | '128'
        'aByteBoxed'    | '-129'
        'aShort'        | '32768'
        'aShort'        | '-32769'
        'aShortBoxed'   | '32768'
        'aShortBoxed'   | '-32769'
        'aInt'          | '2147483648'
        'aInt'          | '-2147483649'
        'aIntBoxed'     | '2147483648'
        'aIntBoxed'     | '-2147483649'
        'aLong'         | '9223372036854775808'
        'aLong'         | '-9223372036854775809'
        'aLongBoxed'    | '9223372036854775808'
        'aLongBoxed'    | '-9223372036854775809'
        // Not a known value
        'aBoolean'      | 'spam'
        'aBooleanBoxed' | 'eggs'
    }
}
