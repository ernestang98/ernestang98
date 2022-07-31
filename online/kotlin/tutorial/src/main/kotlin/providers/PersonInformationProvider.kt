package providers

interface PersonInformationProvider : IdentificationNumberGenerator {
    fun printDetails() {
        println("default method called")
    }
}