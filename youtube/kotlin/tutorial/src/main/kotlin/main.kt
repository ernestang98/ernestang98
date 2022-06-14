/*
 Link: https://www.youtube.com/watch?v=F9UC9DY-vIU&t=4817s
 */

import enums.EntityType
import models.*
import providers.PersonInformationProvider
import kotlin.random.Random

// val cannot be reassigned, var can be reassigned
const val firstName: String = "Ernest"
var lastName: String? = null

fun greeting() {
    println("hello World")
}

fun greetingAndProvideLastName(): String {
    println("hello World")
    return "Ang"
}

fun customGreeting(greetingString: String = "Good Morning") {
    println(greetingString)
}

fun manyGreetings(greeting: String = "Good", vararg suffix: String) {
    suffix.forEach { println("$greeting $it") }
}

fun iterateArray(array: Array<String> = arrayOf(), forEach: Boolean = false, forLoop: Boolean = false) {
    if (array.isEmpty()) {
        println("empty array")
    }
    else {
        println(array[0])
        println(array.get(0))
        println(array.size)

        if (forLoop) for (i in array) println(i)

        // default iterator is "it"
        // works for listOf, mapOf
        if (forEach) array.forEach { println(it) }
        if (forEach) array.forEach { item -> println(item) }
        if (forEach) array.forEachIndexed { index, item -> println("$item is at index $index") }

    }
}

fun printFilteredStrings(list: List<String>, predicate: (String) -> Boolean) {
    list.forEach { it ->
        if (predicate(it)) {
            println(it)
        }
    }
}

fun printFilteredStringsNullable(list: List<String>, predicate: ((String) -> Boolean)?) {
    list.forEach { it ->
        if (predicate?.invoke(it) == true) {
            println(it)
        }
    }
}

// storing functions as variables
val anotherPredicate : (String) -> Boolean = { iter ->
    iter.startsWith("J")
}

fun getAnotherAnotherPredicate() : (String) -> Boolean {
    return { iter -> iter.startsWith("J") }
}

fun EntitySealed.Medium.extensionFunctions() {
    println("Extended function for EntitySealed's Medium Class")
}

val EntitySealed.Medium.INFO : String
        get() = "INFO"

fun main() {

    // ---- PART 1 ----

//    lastName = "Ang"
    if (lastName == null) {
        println(firstName)
    }
    else {
        println("$firstName $lastName")
    }

    if (lastName == null) lastName = greetingAndProvideLastName() else greeting()

    println("$firstName $lastName")
    customGreeting()
    customGreeting("Good Night")

    val hobbies = arrayOf("History", "Running", "Music")

    iterateArray(hobbies, true, false)

    manyGreetings("Good", "afternoon", "morning")

    val greetingSuffices = arrayOf("afternoon", "morning")

    manyGreetings("Good", *greetingSuffices)

    manyGreetings(greeting = "Good", suffix = greetingSuffices)

    val person = Person()
    person.firstName = "Steven"
    println("${person.firstName} ${person.lastName}")
    person.nickname = "Stephen"
    println(person.nickname)

    person.printDetails()

    val personTwo = PersonTwo()
    println("${personTwo.firstName} ${personTwo.lastName}")

    val student = Student()
    println(student.getID())
    /*
    When you use a print function within println, kotlin auto assumes
    that return type of that print function is kotlin.Unit
    println(student.printDietaryNeeds())
     */
    student.printDietaryNeeds()

    /* smart casting happening here:
       in the example they give, BasicInfoProv inherits SessionInfoProvider and overrides getSessionId to return
       "session". When BasicInfoProv calls getSessionId(), it auto smart casts it to SessionInfoProv without
       having to smart cast BasicInfoProv to SessionInfoProv
     */
    student.whyIsDietImportant()
    student.whyIsDietImportantOverrideThis()

    student.whoAmI = "Secondary"
    println(student.whoAmI)

    // Anonymous objects are useful in allowing you to not have to create a new name class when
    // you are overriding classes (android development - OnClickListener())
    val customPersonInformationProvider = object : PersonInformationProvider {
        override fun getID(): String {
            return "UNKNOWN-" + Random.nextInt(1, 100).toString()
        }

    }

    println(customPersonInformationProvider.getID())


    // ---- PART 2 ----

//    val entity = Entity.Companion.create()
    val element = Entity.Factory.create()
    println(element.entityName)
    println(Entity.getID())

    val entityTwo = EntityTwoFactory.create(EntityType.HARD)
    println(entityTwo)

    val sealedEntity : EntitySealed = EntitySealedFactory.create(EntityType.EASY)
    val msg = when(sealedEntity) {
        EntitySealed.Spec -> "SPEC Class"
        is EntitySealed.Easy -> "EASY Class"
        is EntitySealed.Medium -> "MEDIUM Class"
        is EntitySealed.Hard -> "HARD Class"
    }
    println(msg)

    val e1 : EntitySealed = EntitySealedFactory.create(EntityType.EASY)
    val e2 : EntitySealed = EntitySealedFactory.create(EntityType.EASY)
    val e3 = EntitySealed.Easy("id", "name")
    val e4 = EntitySealed.Easy("id", "name")
    val e5 = e4.copy()
    val e6 = e4.copy(id = "id2")
    if (e1 == e2) println("they are equal") else println("they are not equal")
    if (e3 == e4) println("they are equal") else println("they are not equal")

    val e7 = EntitySealed.Medium("id", "name")
    e7.extensionFunctions()
    println(e7.INFO)
    val e8 = EntitySealedFactory.create(EntityType.MEDIUM)
    if (e8 is EntitySealed.Medium) e8.extensionFunctions()



    // ---- PART 3 ----

    val theList: List<String> = listOf("Kotlin", "Java", "C", "Kubernetes")
    printFilteredStrings(theList, { iter ->
        iter.startsWith("K")
    })
    printFilteredStrings(theList) { iter ->
        iter.startsWith("K")
    }
    printFilteredStringsNullable(theList, null)
    printFilteredStrings(theList, getAnotherAnotherPredicate())
    printFilteredStrings(theList, anotherPredicate)

    val theListWithNull: List<String?> = listOf("Kotlin",null, "Java", "JavaScript", "C", "Kubernetes", null)

    theListWithNull
        .filterNotNull()
        .filter{
            it.startsWith("J")
        }
        .map {
            it.length
        }
        .forEach {
            println(it)
        }

    theListWithNull
        .filterNotNull()
        .take(3)
        .forEach {
            println(it)
        }

    theListWithNull
        .filterNotNull()
        .takeLast(3)
        .forEach {
            println(it)
        }

    theListWithNull
        .filterNotNull()
        .associate { it to it.length }
        .forEach {
            println("${it.value}. ${it.key}")
        }

    println(theListWithNull.first())
    println(theListWithNull.last())
    println(theListWithNull.filterNotNull().last())
    println(theListWithNull.filterNotNull().findLast{ iter -> iter.startsWith("Java") })
    println(theListWithNull.filterNotNull().findLast{ iter -> iter.startsWith("foo") })
    println(theListWithNull.filterNotNull().findLast{ iter -> iter.startsWith("foo") }.orEmpty())

}
