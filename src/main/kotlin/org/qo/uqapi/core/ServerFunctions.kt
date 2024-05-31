package org.qo.uqapi.core

import com.google.gson.*
import org.qo.uqapi.utils.FileUtils
import kotlin.collections.HashMap

class ServerFunctions {
    val ConnectedServerMap = HashMap<String, String>()
    val validated_servers_list = "servers.json"
    fun acceptServerConnection(addr: String, auth: String): Boolean {
        val fileContent = FileUtils().readString(validated_servers_list)
        val serverlistArr = JsonParser.parseString(fileContent).asJsonArray
        for (jsonElement in serverlistArr) {
            val serverObj = jsonElement.asJsonObject
            val name = serverObj.get("name").asString
            val address = serverObj.get("address").asString
            val authcode = serverObj.get("auth").asString

            if (addr == address && auth == authcode) {
                ConnectedServerMap[name] = address
                return true
            }
        }
        return false
    }
    fun getOnlineServer():String{
        val gson = Gson()
        return gson.toJson(ConnectedServerMap)
    }
}