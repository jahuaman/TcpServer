package com.josehuaman.tcpserver.domain;


import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.josehuaman.tcpserver.dto.Data;
import com.josehuaman.tcpserver.dto.MessageFormat;
import com.josehuaman.tcpserver.entity.Mensagens;
import com.josehuaman.tcpserver.entity.Usuarios;
import com.josehuaman.tcpserver.service.MensagensService;
import com.josehuaman.tcpserver.service.UsuariosService;

/**
 * Classe que processa a requisição do cliente
 * 
 * @author José Huamán
 *
 */
public class ClientTask implements Runnable {
	
	private Socket socket;
	private MensagensService mensagensService;
	private UsuariosService usuariosService;
	
	public ClientTask(Socket socket, MensagensService mensagensService, UsuariosService usuariosService) {
		this.socket = socket;
		this.mensagensService = mensagensService;
		this.usuariosService = usuariosService;
	}
	
	@Override
	public void run() {
		
		try (InputStream inputStream = socket.getInputStream();
			   OutputStream outputStream = socket.getOutputStream()) {
	
			List<Integer> listInt = getList(inputStream);
			if (listInt.size() < 7) return;
			
			//inputStream é alocado na estrutura da mensagem
	    MessageFormat messageFormat = new MessageFormat(listInt);
	    if (!messageFormat.isValid()) return;
	    
	    //salva no log
	    saveLogRecebido(messageFormat);
	    
	    //1. Mensagem de texto
	    if (messageFormat.getFrame() == MessageFormat.FRAME_MESSAGE_TEXT) {
	    	//Responder ao cliente com ACK
	    	outputStream.write(messageFormat.ackOut());
	    	//armazenar no BD a mensagem e data de recebimento
	    	mensagensService.save(new Mensagens(messageFormat.getData().toAscii()));	    	
	    }
	    //2. info de usuario
	    else if (messageFormat.getFrame() == MessageFormat.FRAME_INFO_USER) {
	    	//Responder ao cliente com ACK
	    	outputStream.write(messageFormat.ackOut());
	    	//armazenar no BD a mensagem e data de recebimento
	    	Usuarios usuarios = new Usuarios(messageFormat.getData());
	    	usuariosService.save(usuarios);
	    }
	    //3. solicita data e hora atual
	    else if (messageFormat.getFrame() == MessageFormat.FRAME_GET_DATETIME) {
	    	//Responder ao cliente a data e hora atual
	    	outputStream.write(getDateTime(messageFormat));
	    }
	    saveLogEnviado(messageFormat);
	        
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	
	
	/**
	 * Retorna lista de enteros do inputStream
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private List<Integer> getList(InputStream inputStream) throws IOException {
		List<Integer> list = new ArrayList<Integer>();
    int byteData;
    do {
    	byteData = inputStream.read();
    	list.add(byteData);
    }
    while (byteData != MessageFormat.END && byteData != -1 );//0x0d END
    return list;
	}
	
	/**
	 * Retorna a lista bytes da data e hora atual da zona horaria especificada
	 * 
	 * @param messageFormat
	 * @return
	 * @throws NumberFormatException
	 * @throws DecoderException
	 */
	private byte[] getDateTime(MessageFormat messageFormat) throws NumberFormatException, DecoderException {
		String[] dateFormat = getDataTimeFormat(messageFormat).split(" ");//format
		String dateFormatString = "";
		for (String df : dateFormat) {
			dateFormatString = dateFormatString + convertToHexString(Integer.parseInt(df));
		}
		int sizeArrayByte = 11;//tamanho da mensagem de saida
		String byteFrameData = convertToHexString(sizeArrayByte) + convertToHexString(MessageFormat.FRAME_GET_DATETIME) + dateFormatString;
		byte[] dateTime = new byte[sizeArrayByte];
		dateTime[0] = MessageFormat.INIT;
		dateTime[1] = (byte) sizeArrayByte;
		dateTime[2] = (byte) MessageFormat.FRAME_GET_DATETIME;
		//data
		for (int i = 0; i < dateFormat.length; i++) {
			dateTime[i+3] = Integer.valueOf(dateFormat[i]).byteValue();
		}
		//crc
		dateTime[9] = Integer.valueOf(messageFormat.calcCrc8(byteFrameData),16).byteValue();
		dateTime[10] = MessageFormat.END;
		return dateTime;
	}
	
	private String getDataTimeFormat(MessageFormat messageFormat) {
		//Responder com data e hora de fuso
		String zone = messageFormat.getData().toAscii();//example: America/Sao_Paulo
		LocalDateTime d = LocalDateTime.now(ZoneId.of(zone));//data e hora atual da zona
		String dataTimeFormat = d.format(DateTimeFormatter.ofPattern("dd MM yy HH mm ss"));
		return dataTimeFormat;
	}
	
	private String convertToHexString(int value) {
		return String.format("%02x", value & 0xff);
	}
	
	/**Métodos para log*/
	
	private void saveLogRecebido(MessageFormat messageFormat) throws IOException {
		try(FileWriter arq = new FileWriter("myLog.log", true);
				PrintWriter gravarArq = new PrintWriter(arq)) {
			Data data  = messageFormat.getData();
			String dataStr = messageFormat.getFrame() == MessageFormat.FRAME_INFO_USER ? data.getDataUser():data.toAscii();
			gravarArq.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) 
					+ " RECEIVED: " + messageFormat.toHex()  
					+ " DATA: " + dataStr);
		}
	}
	
	private void saveLogEnviado(MessageFormat messageFormat) throws IOException, NumberFormatException, DecoderException {
		try(FileWriter arq = new FileWriter("myLog.log", true);
				PrintWriter gravarArq = new PrintWriter(arq)) {
			String dataStr = messageFormat.getFrame() == MessageFormat.FRAME_GET_DATETIME ? " DATA: " + getDataTimeFormat(messageFormat):"";
			String enviado = messageFormat.getFrame() == MessageFormat.FRAME_GET_DATETIME ? Hex.encodeHexString(getDateTime(messageFormat)):Hex.encodeHexString(messageFormat.ackOut());
			gravarArq.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) 
					+ " TRANSMITTED: " + enviado
					+ dataStr);
		}
	}
}
