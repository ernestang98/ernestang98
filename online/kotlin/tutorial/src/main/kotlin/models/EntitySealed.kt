/*
   SEALED CLASSES: allow us to define restricted class hierarchies, all extending a base type
   DATA CLASSES: concise immutable data types, generate hashcode() toString() automatically
*/
package models

import enums.EntityType
import java.util.*

object EntitySealedFactory {
    fun create(type: EntityType) : EntitySealed {
        val id = "Entity-" + UUID.randomUUID().toString()
        val name = when(type) {
            EntityType.EASY -> type.name
            EntityType.MEDIUM -> type.getFormattedName()
            EntityType.HARD -> "h@rd"
            EntityType.SPEC -> type.getFormattedName()
        }
        return when (type) {
            EntityType.EASY -> EntitySealed.Easy(id, name)
            EntityType.MEDIUM -> EntitySealed.Medium(id, name)
            EntityType.HARD -> EntitySealed.Hard(id, name, .2f)
            EntityType.SPEC -> EntitySealed.Spec
        }
    }
}

sealed class EntitySealed() {
    object Spec : EntitySealed() {
        val name = "Spec"
    }
    data class Easy(val id: String, val name: String): EntitySealed() {
        override fun equals(other: Any?): Boolean {
            return super.equals(other)
        }

        override fun hashCode(): Int {
            return super.hashCode()
        }

        override fun toString(): String {
            return super.toString()
        }
    }
    data class Medium(val id: String, val name: String): EntitySealed()
    data class Hard(val id: String, val name: String, val multiplier : Float): EntitySealed()
}