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
    def callAPI(String path) {
        log.debug("ST_TOKEN = ${ST_TOKEN}")
        log.debug("BASE_URL = ${BASE_URL}")
        def conn = new URL("${BASE_URL}${path}").openConnection() as HttpURLConnection

        // set some headers
        conn.setRequestProperty('User-Agent', GroovySystem.version)
        conn.setRequestProperty('Accept', 'application/json')
        conn.setRequestProperty('Authorization', "Bearer ${ST_TOKEN}")

        // get the response code - automatically sends the request
        def code = conn.responseCode
        def content = conn.inputStream.text
        log.debug("Response code = ${code}")

        def jsonSluper = new JsonSlurper()
        def jsonObj = jsonSluper.parseText(content)

        return ['code': code, 'jsonObj': jsonObj]
    }

    def refresh() {
        String path = "/v1/devices"
        Map rv = callAPI(path)
        if (rv.code == 200)
            updateDeviceEntries(rv.jsonObj)

        return null
    }

    def updateDeviceEntries(Map jsonObj) {
        def devices = jsonObj.items

        log.debug("json contains ${devices.size()} items.")
        devices.eachWithIndex { Map device, Integer idx ->
            if (device.containsKey('deviceId')) {
                log.debug("idx = ${idx}")
                log.debug("found deviceId = ${device.deviceId}")
                String rv = getValueByKey(device, "name")
                rv = getValueByKey(device, "label")
                rv = getValueByKey(device, "deviceType")
                rv = getValueByKey(device, "deviceNetworkType")
                rv = getValueByKey(device, "NoSuchKey")

                // Now get states
                if (device.label != "Home Hub")   // name "SmartThings v2 Hub"
                    rv = getCurrentStates(device.deviceId)
            }
        }
    }

    def getCurrentStates(deviceId) {
        // https://api.smartthings.com/v1/devices/b84970e6-eff5-45de-bbd4-a182b5e85717/states
        String path = "/v1/devices/${deviceId}/states"
        Map rv = callAPI(path)
        if (rv.code == 200) {
            def main = rv.jsonObj.main
            main.each {k, v ->
                log.debug("k = ${k}, v = ${v}")
            }
        }

        return null
    }

    def getValueByKey(Map device, String key) {
        if (device.containsKey(key)) {
            log.debug("${key} = ${device[key]}")
            return device[key]
        } else {
            String msg = "${key} not found!"
            log.debug(msg)
            return msg
        }
    }
}
