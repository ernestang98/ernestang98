/*
   THIS IS ON COMPANION OBJECTS - 1:45:06
*/
package models

import providers.IdentificationNumberGenerator

class Entity private constructor(val entityName: String) {
    // companion objects are objects scoped to the instance of another class (makes it accessible from another class)
//    companion object {
//        private fun getEntityName(): String {
//            return "Entity"
//        }
//        fun create() = Entity(getEntityName())
//    }

    // Companion objects can also be named
//    companion object Factory {
//        private fun getEntityName(): String {
//            return "Entity"
//        }
//        fun create() = Entity(getEntityName())
//    }

    companion object Factory : IdentificationNumberGenerator {
        override fun getID(): String {
            return "ENTITY-404"
        }
        private fun getEntityName(): String {
            return "Entity"
        }
        fun create() = Entity(getEntityName())
    }

}