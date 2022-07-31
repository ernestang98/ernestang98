package models

class PersonTwo(val lastName: String, val firstName: String) {
    init {
        println("initializing person...")
    }

    constructor(): this("Angsto", "Ernesto") {
        println("constructing person")
    }

    init {
        println("second time initializing person...")
    }

}