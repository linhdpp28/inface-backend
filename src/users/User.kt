package linhdo.backend.inface.users

import kotlinx.io.core.Closeable
import linhdo.backend.inface.db.DbInFile
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Users : UUIDTable() {
    val profileAvatarLink: Column<String> = varchar("profile_avatar_link", 256) //https://....
    val username: Column<String> = varchar("username", 256).uniqueIndex() // must different another users
    val email: Column<String> = varchar("email", 128) // abc@def.ghi... | must different another users
    val password: Column<String> = varchar("password", 64)// hash that field
}

class User(uid: EntityID<UUID>) : UUIDEntity(uid) {
    companion object : UUIDEntityClass<User>(Users)

    var profileAvatarLink by Users.profileAvatarLink
    var username by Users.username
    var email by Users.email
    var password by Users.password
}

interface IUserDao : Closeable {
    fun createUserAndGetId(mUsername: String, mEmail: String, mPassword: String): UUID?
    fun getAllUser(): List<User>
    fun findUserById(userId: UUID): User?
    fun findUserByUsername(username: String): User?
    fun findUserByEmail(email: String): User?
    fun updateProfileAvatarLink(userId: UUID, link: String)
}

class UserDao : IUserDao {
    private val db by lazy {
        DbInFile.instance
    }

    override fun createUserAndGetId(mUsername: String, mEmail: String, mPassword: String): UUID? {
        return transaction(db) {
            User.new {
                username = mUsername
                email = mEmail
                password = mPassword
                profileAvatarLink = ""
            }.id.value
        }
    }

    override fun getAllUser(): List<User> = transaction(db) {
        User.all().sortedBy { it.username }
    }

    override fun findUserById(userId: UUID): User? = transaction(db) {
        User.findById(userId)
    }

    override fun findUserByUsername(username: String): User? = transaction(db) {
        User.find { Users.username eq username }.singleOrNull()
    }

    override fun findUserByEmail(email: String): User? = transaction(db) {
        val users = User.find { Users.email eq email }
        when {
            users.count() > 1 -> null
            users.empty() -> null
            else -> users.first()
        }
    }

    override fun updateProfileAvatarLink(userId: UUID, link: String) {
        transaction(db) {
            User.findById(userId)?.profileAvatarLink = link
        }
    }

    override fun close() {

    }
}