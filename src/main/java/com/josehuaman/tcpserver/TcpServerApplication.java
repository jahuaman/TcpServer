package com.josehuaman.tcpserver;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.josehuaman.tcpserver.domain.SocketServer;

@SpringBootApplication
public class TcpServerApplication {

	public static void main(String[] args) throws BeansException, IOException {
		//SpringApplication.run(TcpServerApplication.class, args);
		ApplicationContext applicationContext = SpringApplication.run(TcpServerApplication.class, args);
		
		applicationContext.getBean(SocketServer.class).rodar();
	}

}
