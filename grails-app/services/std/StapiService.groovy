package std

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper

@Transactional
class StapiService {
    private static final String ST_TOKEN = System.getenv('st_token')
    private static final String BASE_URL = "https://api.smartthings.com"

    def serviceMethod() {

    }

    // Using only groovy, no micronaut
    def refresh(Map args) {
        log.debug("ST_TOKEN = ${ST_TOKEN}")
        log.debug("BASE_URL = ${BASE_URL}")
        def conn = new URL("${BASE_URL}/v1/devices").openConnection() as HttpURLConnection

        // set some headers
        conn.setRequestProperty( 'User-Agent', 'groovy-2.5.6' )
        conn.setRequestProperty( 'Accept', 'application/json' )
        conn.setRequestProperty( 'Authorization', "Bearer ${ST_TOKEN}")

        // get the response code - automatically sends the request
        def code = conn.responseCode
        def content = conn.inputStream.text
        log.debug("Response code = ${code}")

        def jsonSluper = new JsonSlurper()
        def jsonObj = jsonSluper.parseText(content)
//        log.debug("jsonObj = ${jsonObj}")

        return null
    }

    def updateDeviceEntries(JsonSlurper jsonObj) {

    }
}
