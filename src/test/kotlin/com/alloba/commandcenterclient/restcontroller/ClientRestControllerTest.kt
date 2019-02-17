package com.alloba.commandcenterclient.restcontroller

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.client.RestClientException

@RunWith(SpringRunner::class)
@SpringBootTest
class ClientRestControllerTest {
    @Autowired
    lateinit var clientController:ClientRestController

    val commandsFileLocation = "C:\\Projects\\command-center-client\\src\\main\\resources\\commands.json"

    @Before
    fun setUp() {
    }

    @Test
    fun executeCommandEndpoint() {
        try {
            clientController.commandsFileLocation = "garbage/gerrr"
            clientController.executeCommandEndpoint("commandNameWrong")
        }
        catch (e:RestClientException){
            "Expected Behavior"
        }

        //this relies on the specific location of the file... won't work if it ever changes :)
        clientController.commandsFileLocation = commandsFileLocation
        clientController.reloadCommandListEndpoint()
        clientController.executeCommandEndpoint("testCommand")
    }

    @Test
    fun getCommandListEndpoint() {
        clientController.commandsFileLocation = commandsFileLocation
        clientController.reloadCommandListEndpoint()

        assertNotNull(clientController.getCommandListEndpoint())
    }

    @Test
    fun registerWithServerEndpoint() {
        fail("not implemented yet")
    }

    @Test
    fun reloadCommandListEndpoint() {
        //this is essentially being tested as required functionality elsewhere, but i'll have it here as well for now
        clientController.commandsFileLocation = commandsFileLocation
        clientController.reloadCommandListEndpoint()
    }
}