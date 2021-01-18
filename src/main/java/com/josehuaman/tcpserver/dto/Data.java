package com.josehuaman.tcpserver.dto;

import java.util.List;

/**
 * Classe para armazenar o corpo da mensagem
 * 
 * @author José Huamán
 *
 */
public class Data {

	private final List<Integer> listData;
	
	public Data(List<Integer> listData) {
		this.listData = listData;
	}
	
	public int[] getArray() {
		int[] array = new int[listData.size()];
		for(int i = 0; i < listData.size(); i++) array[i] = listData.get(i);
		return array;
	}
	
	public char[] getChar() {
		char[] array = new char[listData.size()];
		for(int i = 0; i < listData.size(); i++) array[i] = (char) listData.get(i).intValue();
		return array;
	}
	
	public byte[] toBytes() {
		byte[] array = new byte[listData.size()];
		for(int i = 0; i < listData.size(); i++) array[i] = listData.get(i).byteValue();
		return array;
	}
	
	public String toAscii() {
		return new String(getChar());
	}
	
	public String getHexString() {
		String[] array = new String[listData.size()];
		for(int i = 0; i < listData.size(); i++) array[i] = String.format("%02x", listData.get(i) & 0xff);
		return String.join("", array);
	}
	
	//pelas regras das informações dos usuários
	
	public int getIdade() {
		return getArray()[0];
	}
	
	public int getPeso() {
		return getArray()[1];
	}
	
	public int getAltura() {
		return getArray()[2];
	}
	
	public int getTamanhoNome() {
		return getArray()[3];
	}
	
	public String getNome() {
		char[] array = new char[listData.size()-4];
		for(int i = 0; i < listData.size()-4; i++) array[i] = (char) listData.get(i+4).intValue();
		return new String(array);
	}
	
	public String getDataUser() {
		return String.valueOf(getArray()[0]) + String.valueOf(getArray()[1]) + String.valueOf(getArray()[2]) + String.valueOf(getArray()[3]) + getNome(); 
	}
	
}
