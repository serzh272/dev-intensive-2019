package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User (val id:String,
            var firstName:String?,
            var lastName:String?,
            var avatar:String?,
            var rating:Int=0,
            var respect:Int = 0,
            var lastVisit:Date? = Date(),
            var isOnline: Boolean = false
){
    var introBit:String

    constructor(id: String, firstName: String?, lastName: String?):this(id=id, firstName=firstName, lastName=lastName, avatar = null)

    constructor(id: String): this(id=id, firstName="John", lastName="Doe")

    init {
        introBit = getIntro()
        println("It's Alive!!! \n" +
                "${if(lastName==="Doe") "His name id $firstName $lastName" else "And his name is $firstName $lastName!!!"}\n")
    }

    private fun getIntro() ="""
        tu tu ru tuuuuuu!!!
        tu tu ru tuuuuuuuuuu...
        
        tu tu ru tuuuuuu!!!
        tu tu ru tuuuuuuuuuu...
        ${"\n"}
        $firstName $lastName
    """.trimIndent()

//    override fun toString(): String {
//        return "$firstName $lastName"
//    }

    fun printMe() = println("""            
            id: $id
            firstName: $firstName
            lastName: $lastName
            avatar: $avatar
            rating: $rating
            respect: $respect
            lastVisit: $lastVisit
            isOnline: $isOnline
        """.trimIndent())

    companion object Factory{
        private var lastId : Int = -1
        fun makeUser(fullName:String?):User{
            val (firstName, lastName) = Utils.parseFullName(fullName)
            lastId++
            if (firstName.isNullOrEmpty() && lastName.isNullOrEmpty()){
                return User("$lastId")
            }
            else if(firstName.isNullOrEmpty()){
                return User("$lastId", "", lastName)
            }else if(lastName.isNullOrEmpty()){
                return User("$lastId", firstName, "")
            }
            return User("$lastId", firstName, lastName)
        }
    }

    data class Builder(var id:String = "0",
    var firstName:String? = "John",
    var lastName:String? = "Doe",
    var avatar:String? = null,
    var rating:Int=0,
    var respect:Int = 0,
    var lastVisit:Date? = Date(),
    var isOnline: Boolean = false){
        fun id(value:String) = apply { id = value }
        fun firstName(value: String) = apply { firstName = value }
        fun lastName(value: String) = apply { lastName = value }
        fun avatar(value:String?) = apply { avatar = value }
        fun rating(value:Int) = apply { rating = value }
        fun respect(value:Int) = apply { respect = value }
        fun lastVisit(value: Date) = apply { lastVisit = value }
        fun isOnline(value:Boolean) = apply { isOnline = value }
        fun build():User{
            return User(id, firstName, lastName, avatar, rating, respect, lastVisit, isOnline)
        }
    }
}