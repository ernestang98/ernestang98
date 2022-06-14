package models

import providers.DietaryInformationProvider

class Student : Person(), DietaryInformationProvider {

    override val dietaryNeeds: List<String> = listOf("Salmon", "Turkey")

    override fun whyIsDietImportantOverrideThis() {
        println("I am overriding this!")
    }

    override fun printDietaryNeeds() {
        println("I cannot eat: ${dietaryNeeds.joinToString()}")
    }

    override fun getID() : String {
        super.getID()
        println("Hi I am a student... Getting my ID...")
        return "STUDENT-" + super.getID()
    }

    override var whoAmI: String = "I am a student"
        set (value) {
            field = "I am a $value student "
        }
}