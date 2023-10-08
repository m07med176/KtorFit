package com.smaple.data.test

data class Person(val name: String, val age: Int)
class Test{
    fun testFunction(name: String = "this is my name"){
        println(name)
    }
}
// https://medium.com/@amoljp19/mastering-kotlin-reflection-bce2a561a467
/**
 * ## Reflection
 * **allows you to examine and manipulate the structure of your code at runtime**
 * **Kotlin reflection provides a set of APIs that enable introspection, dynamic loading, and modification of classes, objects, properties, and functions.**
 */
object LearningReflection {
    @JvmStatic
    fun main(args: Array<String>) {
        reflectMethodsOfClass()
    }
}

fun reflectMethodsOfClass(){
    val test = Test()
    val properties = test.javaClass.declaredMethods

}

fun reflectProperitiesOfClass(){
    val person = Person("Alice", 25)
    val properties = person.javaClass.declaredFields

    for (property in properties) {
        property.isAccessible = true
        val value = property.get(person)
        println("${property.name}: $value")
    }
}
/*
- CV P
- WO G/L
- RSkl
- PrepFoWr
 */
