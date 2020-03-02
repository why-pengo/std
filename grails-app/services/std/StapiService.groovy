package std

import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper
import std.Device
import std.States

@Transactional
class StapiService {
    private static final String ST_TOKEN = System.getenv('st_token')
    private static final String BASE_URL = "https://api.smartthings.com"

    def serviceMethod() {

    }

    // Using only groovy, no micronaut
    def callAPI(String path) {
//        log.debug("ST_TOKEN = ${ST_TOKEN}")
//        log.debug("BASE_URL = ${BASE_URL}")
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
                def persistentDevice = Device.findByDeviceId(device.deviceId)
                if (persistentDevice == null) {
                    persistentDevice = new Device()
                    persistentDevice.deviceId = device.deviceId
                }
                String rv = getValueByKey(device, "name")
                persistentDevice.name = rv
                rv = getValueByKey(device, "label")
                persistentDevice.label = rv
                rv = getValueByKey(device, "deviceType")
                persistentDevice.deviceType = rv
                rv = getValueByKey(device, "deviceNetworkType")
                persistentDevice.deviceNetworkType = rv
//                rv = getValueByKey(device, "NoSuchKey")

                // Now get states
                if (device.label != "Home Hub") {  // name "SmartThings v2 Hub"
                    rv = getCurrentStates(device.deviceId)
//                    persistentDevice.states = rv.inspect() // serialize to string
                    def persistentState = new States()
                    persistentState.device = persistentDevice
                    persistentState.state = rv.inspect()
                    persistentState.save()
                }

                persistentDevice.save()
                log.debug("persistentDevice.id = ${persistentDevice.id}")
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
            return main
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
