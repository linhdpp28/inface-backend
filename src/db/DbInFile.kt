package linhdo.backend.inface.db

import org.jetbrains.exposed.sql.Database

object DbInFile {
    val instance by lazy {
        Database.connect("jdbc:sqlite:/data/inface.db", "org.sqlite.JDBC")
    }
}