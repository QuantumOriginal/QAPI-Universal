package org.qo.uqapi.core

import com.google.gson.*
import jakarta.servlet.http.HttpServletRequest
import org.qo.uqapi.Responses
import org.qo.uqapi.utils.FileUtils
import org.qo.uqapi.utils.IPUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

object ServerFunctions {
    val ConnectedServerMap = ConcurrentHashMap<Server, Status>()
    private val validated_servers_list = "servers.json"
    private val gson = Gson()
    val chat_list: MutableList<ChatComponent> = mutableListOf()

    fun acceptServerConnection(addr: String, auth: String): Boolean {
        val fileContent = FileUtils().readString(validated_servers_list)
        val serverlistArr = JsonParser.parseString(fileContent).asJsonArray
        for (jsonElement in serverlistArr) {
            val serverObj = jsonElement.asJsonObject
            val name = serverObj.get("name").asString
            val address = serverObj.get("address").asString
            val authcode = serverObj.get("auth").asString
            val serverInstance = Server(name, address, authcode, System.currentTimeMillis())
            if (addr == address && auth == authcode) {
                ConnectedServerMap[serverInstance] = Status()
                return true
            }
        }
        return false
    }

    fun getOnlineServer(): String {
        return gson.toJson(ConnectedServerMap)
    }

    fun isServerAvailable(addr: String): Boolean {
        val fileContent = FileUtils().readString(validated_servers_list)
        return fileContent!!.contains(addr)
    }

    fun handleStatusUpload(statusObject: String, req: HttpServletRequest) {
        val status = gson.fromJson(statusObject, Status::class.java)
        if (status != null) {
            val ipAddr = IPUtil().getIpAddr(req)
            ConnectedServerMap.forEach { (server, _) ->
                if (server.addr == ipAddr) {
                    ConnectedServerMap[server] = status
                    server.latestHeartBeat = System.currentTimeMillis()
                }
            }
        }
    }

    fun handleChatUpload(chatObject: String, req: HttpServletRequest) {
        val chatObj = JsonParser.parseString(chatObject).asJsonObject
        val ipAddr = IPUtil().getIpAddr(req)
        val serverInstance = ConnectedServerMap.keys.find { it.addr == ipAddr }

        if (serverInstance != null) {
            val sender = chatObj.get("sender").asString
            val dateFormat = SimpleDateFormat("yyyy-M-d HH:mm:ss")
            val time = dateFormat.format(Date())
            val type = Type.valueOf(chatObj.get("type").asString)

            val chatComponent = ChatComponent(sender, serverInstance.name, time, type)
            chat_list.add(chatComponent)
        }
    }

    fun getChatList(): ResponseEntity<String>{
        if (chat_list.size == 0){
            return Responses.failed("chat list is empty")
        } else {
            val jsonArray: JsonArray = gson.toJsonTree(chat_list).asJsonArray
            return Responses.success(jsonArray.toString())
        }
    }

    data class Server(
        val name: String,
        val addr: String,
        val auth: String,
        var latestHeartBeat: Long
    )

    data class Status(
        var status: Int = -1,
        // -1:DIED 0:WORK 1:FULL 2:MAINTAINED
        var latestMspt: Double = 0.0,
        var latestMsptList: ArrayList<Double> = ArrayList(),
        var serverPlayerCount: Int = 0,
        var serverPlayerList: ArrayList<Player> = ArrayList()
    )

    data class Player(
        val name: String,
        val IPaddress: String,
        val isOP: Boolean,
        val location: DoubleArray = DoubleArray(3),
        val PlayerDimension: String
    )

    data class ChatComponent(val sender: String, val server: String, val time: String, val type: Type)

    enum class Type {
        CHAT,
        BROADCAST
    }
}

@Component("ServerAlive")
class ServerAlive {
    @Scheduled(fixedDelay = 3000)
    fun run() {
        ServerFunctions.ConnectedServerMap.forEach { (server, status) ->
            if (System.currentTimeMillis() - server.latestHeartBeat > 3000) {
                status.status = -1
                println("server $server. ${status.status} OFFLINE")
            }
        }
    }
}
