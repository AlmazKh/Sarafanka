package com.almaz.sarafanka.core.model

data class Contact(val id: String, val name:String) {
    var numbers = HashSet<String>()
    var emails = ArrayList<String>()
    var user: User? = null
}
