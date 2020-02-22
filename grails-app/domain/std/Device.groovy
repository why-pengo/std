package std

class Device {
    String deviceId
    String name
    String label
    String deviceType
    String deviceNetworkType
    String states

    static constraints = {
        states maxSize: 5000
    }
}
