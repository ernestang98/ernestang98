/*
THIS IS ON OBJECT DECLARATIONS - 1:49:51
 */

package models

import enums.EntityType
import providers.IdentificationNumberGenerator
import java.util.*

class EntityTwo constructor(private val id: String, private val name: String, private val difficulty: String) {
    override fun toString(): String {
        return "ID is $id, Name is $name, and Type is $difficulty"
    }
}

object EntityTwoFactory : IdentificationNumberGenerator {
    override fun getID(): String {
        return "Entity-" + UUID.randomUUID().toString()
    }
    fun create(type: EntityType) : EntityTwo {
        val difficulty = when(type) {
            EntityType.EASY -> type.name
            EntityType.MEDIUM -> type.getFormattedName()
            EntityType.HARD -> "h@rd"
            EntityType.SPEC -> type.getFormattedName()
        }
        return EntityTwo(this.getID(), "John Doe", difficulty)
    }
}