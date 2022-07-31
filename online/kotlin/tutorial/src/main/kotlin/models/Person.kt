package models

import providers.PersonInformationProvider
import kotlin.random.Random

// Play around with how kotlin models work
// adding val into params auto sets it as the properties, no need to define and add init/constructor
// When implementing an interface, if you are only using some of the methods, then you have to put abstract

// http://kotlin-quick-reference.com/102c-R-open-final-classes.html
// https://kotlinlang.org/docs/visibility-modifiers.html#classes-and-interfaces
// https://discuss.kotlinlang.org/t/kotlin-unit-print-on-result-solved/20094/2

@Suppress("JoinDeclarationAndAssignment")
open class Person(lastName: String = "Doe", firstName: String = "John", ) : PersonInformationProvider {

    // 4 types: internal protected public private
    var lastName: String
    var firstName: String
    open var whoAmI = "I am a person!"

    internal var nickname: String? = null
        set(value) {
            field = value
            println("Setting nickname as $field")
        }
        get() {
            println("returning $field...")
            return field
        }

    init {
        this.lastName = lastName
        this.firstName = firstName
    }

    override fun getID() : String {
        return Random.nextInt(1, 100).toString()
    }

    override fun printDetails() {
        super.printDetails()
        val nicknameToPrint = this.nickname ?: "No Nickname"
        println("${this.firstName} ${this.lastName} (${nicknameToPrint}), ID Number: ${getID()}")
    }

}



