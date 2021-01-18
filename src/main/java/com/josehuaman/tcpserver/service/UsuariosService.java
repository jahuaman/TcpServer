package com.josehuaman.tcpserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.josehuaman.tcpserver.entity.Usuarios;
import com.josehuaman.tcpserver.respository.UsuariosRepository;

@Service
public class UsuariosService {

	@Autowired
	UsuariosRepository usuariosRepository;
	
	public Usuarios save(Usuarios usuarios) {
		return usuariosRepository.save(usuarios);
	}
}
