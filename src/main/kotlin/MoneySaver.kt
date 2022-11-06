package org.hezistudio

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object MoneySaver : KotlinPlugin(
    JvmPluginDescription(
        id = "org.hezistudio.money-saver",
        name = "钱钱存储器",
        version = "0.1.0",
    ) {
        author("HeziBlack")
    }
) {
    private val dbName = "moneyData.db3"
    private val dbConnect = "jdbc:sqlite:"
    private val dbDriver = "org.sqlite.JDBC"

    override fun onEnable() {
        initDatabase()
        logger.info { "Plugin loaded" }
    }

    private fun initDatabase(){
        val dbFile = File(dataFolder, dbName)
        if (!dbFile.exists()){
            if (!dataFolder.exists()){
                dataFolder.mkdirs()
            }
            dbFile.createNewFile()
        }
        val db = Database.connect("${dbConnect}${dbFile.toURI()}", dbDriver)
        transaction(db) {
            SchemaUtils.create(TableOfMoney)
        }
    }

    fun dbConnection():Database{
        return Database.connect(dbConnect+File(dataFolder, dbName).toURI(), dbDriver)
    }

}