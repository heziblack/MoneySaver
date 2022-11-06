package org.hezistudio

import org.jetbrains.exposed.dao.id.LongIdTable

object TableOfMoney:LongIdTable() {
    val uid = long("uid")
    val gid = long("gid")
    val bid = long("bid")
    val money = long("u_money")
    val saved = long("u_saved")
}