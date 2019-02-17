package com.alloba.commandcenterclient

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.net.Inet4Address

@RunWith(SpringRunner::class)
@SpringBootTest
class CommandCenterClientApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun getLocalIp(){
		val localIp = Inet4Address.getLocalHost().hostAddress
		println(localIp)
	}

}

