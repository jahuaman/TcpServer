package com.josehuaman.tcpserver.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;

@Entity
public class Mensagens implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@NotNull
	@JoinColumn(name = "mensagen")
	private String mensagem;
	
	@NotNull
	@JoinColumn(name = "dt_inclusao")
	private LocalDateTime dtInclusao;

	public Mensagens(String mensagem) {
		this.mensagem = mensagem;
		this.dtInclusao = LocalDateTime.now();	
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public LocalDateTime getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(LocalDateTime dtInclusao) {
		this.dtInclusao = dtInclusao;
	}
	
	
	
}
