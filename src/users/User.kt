package linhdo.backend.inface.users

import java.util.*

data class User(
    private val userId: String = "User${UUID.randomUUID().timestamp()}",
    var profileAvatarLink: String = "",
    var username: String = "",
    var email: String = "",
    var password: String = "encrypt that field"
)