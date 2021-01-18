package com.josehuaman.tcpserver.domain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.josehuaman.tcpserver.service.MensagensService;
import com.josehuaman.tcpserver.service.UsuariosService;

@Component
public class SocketServer {
	private ServerSocket servidor;
	private ExecutorService threadPool;
	private AtomicBoolean estaRodando;
	
	@Autowired
	MensagensService mensagensService;

	@Autowired
	UsuariosService usuariosService;

	public SocketServer() throws IOException {
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + "  INFO Iniciando Servidor TCP na porta 12345");
		this.servidor = new ServerSocket(12345);
		this.threadPool = Executors.newCachedThreadPool();
		this.estaRodando = new AtomicBoolean(true);
	}
	
	public void rodar() throws IOException {
		while (this.estaRodando.get()) {
			try {
				Socket socket = servidor.accept();
				// uma vez aceita, cada cliente têm uma nova porta diferente
				System.out.println("Aceitando novo cliente, porta: " + socket.getPort());
	
				// objeto que vai processar as tarefas dos clientes
				ClientTask clientTask = new ClientTask(socket, mensagensService, usuariosService);
				threadPool.execute(clientTask);
			}
			catch (SocketException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		SocketServer tcpServer = new SocketServer();
		tcpServer.rodar();
	}
}
