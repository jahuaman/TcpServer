package com.josehuaman.TcpServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.josehuaman.tcpserver.dto.MessageFormat;

@SpringBootTest
class MensageFormatTest {
	
	@Test
	@DisplayName("Deve verificar se o CRC8 da mensagem é igual ao cálculo da mesma")
	public void verificaCalculoCrc8DaEstruturaDaMensagem() throws DecoderException {
		//entrada da mensagem na estrutura [INIT, BYTES, FRAME,  ...DATA... , CRC, END] 
		Integer[] msgArray = {0x0A, 0x10, 0xA1, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64, 0xDC, 0x0D};
		MessageFormat messageFormat = new MessageFormat(Arrays.asList(msgArray));
		//crc8 é o valor da penúltima posição da estrutra
		assertEquals(msgArray[msgArray.length-2], Integer.parseInt(messageFormat.calcCrc8(),16));
	}
	
	@Test
	@DisplayName("A mensagem não deve receber uma lista vazia")
	public void listaVaziaEstruturaDaMensagem() throws DecoderException {
		assertThrows(IndexOutOfBoundsException.class, () -> {
			new MessageFormat(new ArrayList<Integer>());
		});
	}
	
	@Test
	@DisplayName("A mensagem deve conter pelo menos 5 elementos")
	public void verificaTamanhoDaEstruturaDaMensagem() throws DecoderException {
		assertThrows(IllegalArgumentException.class, () -> {
			Integer[] array = {0x0A, 0x10, 0xA1, 0x48};
			new MessageFormat(Arrays.asList(array));
		});
	}
	
	
	
}
