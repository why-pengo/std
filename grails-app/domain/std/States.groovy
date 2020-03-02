package std

class States {
    Device device
    String state

    static constraints = {
        state maxSize: 5000
    }
}
