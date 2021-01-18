package com.josehuaman.tcpserver.dto;

import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.josehuaman.tcpserver.util.CRC8;

/**
 * Classe que representa a estrutura básica da mensagem do protocolo
 * 
 * @author José Huamán
 *
 */
public class MessageFormat {
	
	public final static int INIT = 0x0A;
	public final static int END = 0x0D;
	public final static int FRAME_ACK = 0xA0;
	public final static int FRAME_MESSAGE_TEXT = 0xA1;
	public final static int FRAME_INFO_USER = 0xA2;
	public final static int FRAME_GET_DATETIME = 0xA3;
	
	private final int init;
	private final int bytes;
	private final int frame;
	private final Data data;
	private final int crc;
	private final int end;
	private final List<Integer> message;
	
	/**
	 * Construtor que recebe a mensagem em forma de Lista de enteros
	 * @param message
	 */
	public MessageFormat(List<Integer> message) {
		this.init = INIT; //message.get(0);
		this.bytes = message.get(1);
		this.frame = message.get(2);
		this.data = new Data(message.subList(3, message.size()-2));
		this.crc = message.get(message.size()-2);
		this.end = END; //message.get(message.size()-1);
		this.message = message;
	}
	
	public byte[] toBytes() {
		byte[] array = new byte[message.size()];
		for(int i = 0; i < message.size(); i++) array[i] = message.get(i).byteValue();
		return array;
	}
	
	public String toHex() {
		return Hex.encodeHexString(toBytes());
	}
	
	/**
	 * Método que calcula o CRC8 da mensagem
	 * 
	 * @return
	 * @throws DecoderException
	 */
	public String calcCrc8() throws DecoderException {
		return calcCrc8(String.format("%02x", this.bytes & 0xff) 
				+ String.format("%02x", this.frame & 0xff) 
				+ data.getHexString());
	}
	
	/**
	 * Método que recebe uma sequencia de valores em fomato hexadecimal
	 * retorna em hexadecimal string. Exemplo: A0
	 * 
	 * @param sequence
	 * @return
	 * @throws DecoderException
	 */
	public String calcCrc8(String sequence) throws DecoderException {
		byte[] b = Hex.decodeHex(sequence);
		return Hex.encodeHexString(new byte[] {CRC8.calc(b, b.length)}).toUpperCase();
	}
	
	/**
	 * Verifica se a mensagem chegou sem erros.
	 * 
	 * @return
	 * @throws DecoderException
	 */
	public boolean isValid() throws DecoderException {
		return this.crc == Integer.parseInt(calcCrc8(), 16);
	}

	/**
	 * Resposta ACK
	 *  
	 * @return
	 */
	public byte[] ackOut() {
		byte[] ack = {INIT,5,(byte) FRAME_ACK,40,END};
		return ack;
	}
	
	public int getInit() {
		return init;
	}

	public int getBytes() {
		return bytes;
	}

	public int getFrame() {
		return frame;
	}

	public Data getData() {
		return data;
	}

	public int getCrc() {
		return crc;
	}

	public int getEnd() {
		return end;
	}

	
}
