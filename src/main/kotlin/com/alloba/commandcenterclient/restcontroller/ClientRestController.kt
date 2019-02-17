package com.alloba.commandcenterclient.restcontroller

import com.alloba.commandcenterclient.models.Command
import com.alloba.commandcenterclient.models.CommandList
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.Inet4Address

@RestController
@RequestMapping("client")
class ClientRestController {
    var serverAddress: String = "not specified"
    lateinit var availableCommands: CommandList

    @Value("\${application.commandsfile.location}")
    lateinit var commandsFileLocation:String
    @Value("\${server.port:}")
    lateinit var applicationPort:String

    @GetMapping("execute/{commandName}")
    fun executeCommandEndpoint(@PathVariable("commandName") commandName:String) :String{
        if(! ::availableCommands.isInitialized)
            availableCommands = getRefreshCommandList(commandsFileLocation)
        if(availableCommands.commandList.none { it.name == commandName })
            throw RestClientException("command does not exist on this client")

        val executeStatus = executeCommand(availableCommands.commandList.find { it.name == commandName })

        return if(executeStatus)
            "Command $commandName executed successfully"
        else
            throw RestClientException("Command $commandName unable to execute")
    }

    @GetMapping("commandList")
    fun getCommandListEndpoint(): CommandList{
        if(! ::availableCommands.isInitialized)
            availableCommands = getRefreshCommandList(commandsFileLocation)
        return availableCommands
    }

    @GetMapping("registerWithServer/{serverLocation}")
    fun registerWithServerEndpoint(@PathVariable("serverLocation") serverLocation:String):String {
        serverAddress = serverLocation

        //this only works if the right ethernet adapter is chosen (had issues with hyperV adapters getting picked first)
        //maybe have some logic in the future that looks through all options for things that start with '192.168'
        val clientLocalIp:String = Inet4Address.getLocalHost().hostAddress
        val serverUrl = "http://$serverAddress/server/registerClient/$clientLocalIp:$applicationPort"

        return try {
            val request = RestTemplate()
            request.getForObject(serverUrl, String::class.java)
            "Registered with server $serverLocation"
        }
        catch (e:RuntimeException){
            "Failed to register with server $serverLocation"
        }
    }

    @GetMapping("getRefreshCommandList")
    fun reloadCommandListEndpoint():String{
        availableCommands = getRefreshCommandList(commandsFileLocation)
        return "Command List reloaded from $commandsFileLocation"
    }
}

fun getRefreshCommandList(fileLocation:String): CommandList{
    try {
        val inputJson =  File(fileLocation).readText()
        val objectMapper = ObjectMapper().registerKotlinModule()
        return objectMapper.readValue(inputJson)
    }
    catch (e: IOException){
        throw RestClientException("unable to parse command file at location $fileLocation")
    }
}

fun executeCommand(command: Command?): Boolean{
    if(command == null)
        return false

    return try {
        val builder = ProcessBuilder(command.type, "/c", command.exec)
        val reader = InputStreamReader(builder.start().inputStream)
        val lines = reader.readLines() //todo: should do something with the output other than spitting it to the console.
        println(lines)
        true
    } catch (e:RuntimeException) {
        false
    }
}