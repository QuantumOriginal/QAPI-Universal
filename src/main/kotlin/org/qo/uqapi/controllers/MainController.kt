package org.qo.uqapi.controllers

import jakarta.servlet.http.HttpServletRequest
import org.apache.coyote.Response
import org.qo.uqapi.Responses
import org.qo.uqapi.core.ServerFunctions
import org.qo.uqapi.utils.IPUtil
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.bind.annotation.*

@EnableScheduling
@RestController
@RequestMapping("/v1")
class MainController {
    @GetMapping("/connected")
    fun returnConnectedServers(): ResponseEntity<String>{
        return Responses.success(ServerFunctions.getOnlineServer())
    }
    @PostMapping("/connect")
    fun registerServerConnection(request: HttpServletRequest, @RequestBody auth:String): ResponseEntity<String>{
        var success = false
        IPUtil().getIpAddr(request)?.let { success = ServerFunctions.acceptServerConnection(it, auth) }
        if (success){
            return Responses.success(success.toString())
        } else {
            return Responses.denied("auth parameter is required.")
        }
    }
    @PostMapping("/upload/status")
    fun handleUploadStatus(@RequestBody data:String, req:HttpServletRequest): ResponseEntity<String>{
        if (IPUtil().getIpAddr(req)?.let { ServerFunctions.isServerAvailable(it) } == true){
            ServerFunctions.handleStatusUpload(data, req)
            return Responses.success("ok")
        }
        return Responses.denied("invalid http request.")
    }
    @PostMapping("/upload/chat")
    fun handleChatUpload(@RequestBody data:String, req:HttpServletRequest): ResponseEntity<String>{
        if (IPUtil().getIpAddr(req)?.let { ServerFunctions.isServerAvailable(it) } == true){
            ServerFunctions.handleChatUpload(data, req)
            return Responses.success("ok")
        }
        return Responses.denied("invalid http request.")
    }
    @GetMapping("/download/chat")
    fun handleChatQuery(): ResponseEntity<String>{
        return ServerFunctions.getChatList()
    }
}