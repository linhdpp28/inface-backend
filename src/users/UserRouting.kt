package linhdo.backend.inface.users

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.routing.Route
import io.ktor.routing.post

data class Register(val username: String, val email: String, val password: String)

fun Route.user(userDao: UserDao) {

    post("/user/register") {
        val registration = call.receive<Register>()
        val username = registration.username
        val email = registration.email
        val password = registration.password

//        val userId = userDao.createUserAndGetId(username, email, password)
    }
}