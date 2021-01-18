package com.josehuaman.tcpserver.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.josehuaman.tcpserver.entity.Mensagens;

@Repository
public interface MensagensRepository extends JpaRepository<Mensagens, Integer> {
	
	
}
