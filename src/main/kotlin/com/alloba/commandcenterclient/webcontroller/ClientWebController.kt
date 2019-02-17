package com.alloba.commandcenterclient.webcontroller

import com.alloba.commandcenterclient.restcontroller.ClientRestController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.net.Inet4Address

@Controller
@RequestMapping("client")
class ClientWebController{
    @Autowired
    lateinit var  clientRestController: ClientRestController

    @GetMapping("", "index")
    fun getIndex(model:Model) : String{
        model.addAttribute("commandsFileLocation", clientRestController.commandsFileLocation)
        model.addAttribute("serverAddress", clientRestController.serverAddress)
        model.addAttribute("clientAddress", Inet4Address.getLocalHost().hostAddress)
        model.addAttribute("port", clientRestController.applicationPort)
        return "index"
    }
}