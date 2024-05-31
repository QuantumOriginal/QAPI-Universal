package org.qo.uqapi.controllers

import jakarta.servlet.http.HttpServletRequest
import org.apache.coyote.Response
import org.qo.uqapi.Responses
import org.qo.uqapi.core.ServerFunctions
import org.qo.uqapi.utils.IPUtil
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class MainController {
    @GetMapping("/connected")
    fun returnConnectedServers(): ResponseEntity<String>{
        return Responses().success(ServerFunctions().getOnlineServer())
    }
    @PostMapping("/connect")
    fun registerServerConnection(request: HttpServletRequest, @RequestBody auth:String): ResponseEntity<String>{
        var success = false
        IPUtil().getIpAddr(request)?.let { success = ServerFunctions().acceptServerConnection(it, auth) }
        if (success){
            return Responses().success(success.toString())
        } else {
            return Responses().denied("auth parameter is required.")
        }
    }
}