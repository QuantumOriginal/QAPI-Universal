package org.qo.uqapi.utils

import jakarta.servlet.http.HttpServletRequest
import java.net.InetAddress
import java.net.UnknownHostException

class IPUtil {
    fun getIpAddr(request: HttpServletRequest): String? {
        var ipAddress: String? = null
        try {
            ipAddress = request.getHeader("x-forwarded-for")
            if (ipAddress == null || ipAddress.length == 0 || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.getHeader("Proxy-Client-IP")
            }
            if (ipAddress == null || ipAddress.length == 0 || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP")
            }
            if (ipAddress == null || ipAddress.length == 0 || "unknown".equals(ipAddress, ignoreCase = true)) {
                ipAddress = request.remoteAddr
                if (ipAddress == "127.0.0.1") {
                    var inet: InetAddress? = null
                    try {
                        inet = InetAddress.getLocalHost()
                    } catch (e: UnknownHostException) {
                        e.printStackTrace()
                    }
                    ipAddress = inet!!.hostAddress
                }
            }
            if (ipAddress != null && ipAddress.length > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","))
                }
            }
        } catch (e: Exception) {
            ipAddress = ""
        }
        return ipAddress
    }
}