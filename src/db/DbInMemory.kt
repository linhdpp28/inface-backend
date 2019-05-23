package linhdo.backend.inface.db

import org.jetbrains.exposed.sql.Database

object DbInMemory {
    val instance by lazy {
        Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
    }
}