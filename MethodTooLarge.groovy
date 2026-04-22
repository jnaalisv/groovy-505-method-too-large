
static def generateSpec(def count) {
    def assertions = (1..count).collect { i ->
        "assert obj.field${i % 10} == 'value${i % 10}'"
    }.join("\n\t\t\t\t")

    def spec = """\
@Grab('org.spockframework:spock-core:2.4-groovy-5.0')
import spock.lang.Specification

class MethodTooLargeReproducerSpec extends Specification {

    def "verifyAll with ${count} assertions"() {
        given:
            def obj = [field0: 'value0', field1: 'value1', field2: 'value2',
                       field3: 'value3', field4: 'value4', field5: 'value5',
                       field6: 'value6', field7: 'value7', field8: 'value8',
                       field9: 'value9']
        expect:
            verifyAll {
            \t${assertions}
            }
    }
}
"""
}

def gcl = new GroovyClassLoader()

(589..597).step(1).each { count ->
    def src = generateSpec(count)
    try {
        gcl.parseClass(src)
        println "OK:     ${count}"
    } catch (Exception e) {
        println "FAILED: ${count} — ${e.message}"
        System.exit(1)
    }
}