package enums

enum class EntityType {
    EASY, MEDIUM, HARD, SPEC;
    fun getFormattedName() = name.toLowerCase().capitalize()
}