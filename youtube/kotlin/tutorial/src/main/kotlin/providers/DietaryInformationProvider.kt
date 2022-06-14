package providers

interface DietaryInformationProvider {

    val dietaryNeeds : List<String>

    fun whyIsDietImportant() {
        println("I don't know why, this is just to test the need for overriding")
    }

    fun whyIsDietImportantOverrideThis()

    fun printDietaryNeeds()
}