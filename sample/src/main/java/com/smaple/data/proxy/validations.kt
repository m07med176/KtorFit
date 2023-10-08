package com.smaple.data.proxy

fun isValidURL(url: String?): Boolean {
    if (url==null) return false
    val regex = """^(https?)://[^\s/$.?#].[^\s]*$""".toRegex()
    return regex.matches(url)
}

fun removeLeadingSlash(input: String): String {
    return input.removePrefix("/")
}

fun handleBackSlash(url: String): String {
    return if (!url.endsWith("/")) {
        "$url/"
    } else {
        url
    }
}

fun resolveSlashes(input: String):String =
    removeLeadingSlash(input)
        .apply { removeExtraSlashes(input) }
        .apply { handleBackSlash(this) }

fun removeExtraSlashes(input: String): String  = input.replace(Regex("/+"), "/")

object TestValidation{
    @JvmStatic
    fun main(array: Array<String>){
            val urls = listOf(
                "https://dummyjson.com",
                "ftp://example.com",
                "http://localhost:8080/",
                "invalid-url",
                "https://with spaces.com"
            )


            for (url in urls) {
                val isValid = isValidURL(url)
                if (isValid){

                    println("$url is valid: ${handleBackSlash(url)}")
                }
            }


            val input1 = "/example/path"
            val input2 = "noLeadingSlash"

            val result1 = removeLeadingSlash(input1)
            val result2 = removeLeadingSlash(input2)

            println(result1) // Output: "example/path"
            println(result2) // Output: "noLeadingSlash"
        }

}