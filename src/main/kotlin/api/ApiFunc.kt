package org.hezistudio.api

import net.mamoe.mirai.contact.Member
import org.hezistudio.MoneySaver
import org.hezistudio.TableOfMoney
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

/**根据Member返回查询结果
 * @return 0:口袋钱钱(即现金) 1:仓库钱钱(即存款)*/
fun getMemberMoneyData(member: Member):List<Long>{
    val db = MoneySaver.dbConnection()
    val l = arrayListOf<Long>()
    transaction(db) {
        val q = TableOfMoney.select {
            (TableOfMoney.bid eq member.bot.id) and
            (TableOfMoney.gid eq member.group.id) and
            (TableOfMoney.uid eq member.id)
        }
        if (q.empty()){
            TableOfMoney.insert {
                it[bid] = member.bot.id
                it[gid] = member.group.id
                it[uid] = member.id
                it[money] = 0
                it[saved] = 0
            }
            l.add(0)
            l.add(0)
        }else{
            val r = q.first()
            l.add(r[TableOfMoney.money])
            l.add(r[TableOfMoney.saved])
        }
    }
    return l.toList()
}

/**设置玩家钱数*/
fun setMemberMoney(member: Member,isSaved:Boolean,count:Long):Boolean{
    try{
        val db = MoneySaver.dbConnection()
        transaction(db) {
            val q = TableOfMoney.select {
                (TableOfMoney.bid eq member.bot.id) and
                        (TableOfMoney.gid eq member.group.id) and
                        (TableOfMoney.uid eq member.id)
            }
            if (q.empty()) {
                TableOfMoney.insert {
                    it[bid] = member.bot.id
                    it[gid] = member.group.id
                    it[uid] = member.id
                    if (isSaved) {
                        it[money] = 0
                        it[saved] = count
                    } else {
                        it[money] = count
                        it[saved] = 0
                    }
                }
            } else {
                TableOfMoney.update({
                    (TableOfMoney.bid eq member.bot.id) and
                            (TableOfMoney.gid eq member.group.id) and
                            (TableOfMoney.uid eq member.id)
                }) {
                    if (isSaved) {
                        it[saved] = count
                    } else {
                        it[money] = count
                    }
                }
            }
        }
        return true
    }catch (e:Exception){
        println(e)
        return false
    }

}

