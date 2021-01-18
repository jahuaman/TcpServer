package com.josehuaman.tcpserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.josehuaman.tcpserver.entity.Mensagens;
import com.josehuaman.tcpserver.respository.MensagensRepository;

@Service
public class MensagensService {

	@Autowired
	MensagensRepository mensagensRepository;
	
	public Mensagens save(Mensagens mensagens) {
		return mensagensRepository.save(mensagens);
	}
	
}
