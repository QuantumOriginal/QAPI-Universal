package org.qo.uqapi

import com.nimbusds.jose.shaded.gson.JsonObject
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

class Responses {
    fun failed(input: String?): ResponseEntity<String> {
        val returnObject: JsonObject = JsonObject()
        returnObject.addProperty("code", -1)
        returnObject.addProperty("message", input)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return ResponseEntity<String>(returnObject.toString(), headers, HttpStatus.NOT_ACCEPTABLE)
    }

    fun success(input: String?): ResponseEntity<String> {
        val returnObject: JsonObject = JsonObject()
        returnObject.addProperty("code", 0)
        returnObject.addProperty("message", input)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return ResponseEntity<String>(returnObject.toString(), headers, HttpStatus.OK)
    }

    fun denied(input: String?): ResponseEntity<String> {
        val returnObject: JsonObject = JsonObject()
        returnObject.addProperty("code", 1)
        returnObject.addProperty("message", input)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return ResponseEntity<String>(returnObject.toString(), headers, HttpStatus.FORBIDDEN)
    }
}