package ru.skillbranch.devintensive.extentions

import ru.skillbranch.devintensive.models.User
import ru.skillbranch.devintensive.models.UserView
import ru.skillbranch.devintensive.utils.Utils

fun User.toUserView():UserView{
    val nickname = Utils.transliteration("$firstName $lastName")
    val initials = Utils.toInitials(firstName, lastName)
    val status = if (lastVisit == null) "Еще ни разу не был" else if (isOnline) "online" else "последний раз был ${lastVisit!!.humanizeDiff()}"
    return UserView(id,
            fullName = "$firstName $lastName",
            nickName = nickname,
            avatar = avatar,
            status = status,
            initials = initials
    )
}


