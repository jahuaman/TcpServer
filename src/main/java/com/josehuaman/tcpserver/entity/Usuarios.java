package com.josehuaman.tcpserver.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;

import com.josehuaman.tcpserver.dto.Data;

@Entity
public class Usuarios implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@NotNull
	@JoinColumn(name = "idade")
	private Integer idade;
	
	@NotNull
	@JoinColumn(name = "peso")
	private Integer peso;
	
	@NotNull
	@JoinColumn(name = "altura")
	private Integer altura;
	
	@NotNull
	@JoinColumn(name = "tamanho_nome")
	private Integer tamanhoNome;
	
	@NotNull
	@JoinColumn(name = "mensagen")
	private String nome;
	
	@NotNull
	@JoinColumn(name = "dt_inclusao")
	private LocalDateTime dtInclusao;
	
	public Usuarios(Data data) {
		this.idade = data.getIdade();
		this.peso = data.getPeso();
		this.altura = data.getAltura();
		this.tamanhoNome = data.getTamanhoNome();
		this.nome = data.getNome();
		this.dtInclusao = LocalDateTime.now();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public Integer getAltura() {
		return altura;
	}

	public void setAltura(Integer altura) {
		this.altura = altura;
	}

	public Integer getTamanhoNome() {
		return tamanhoNome;
	}

	public void setTamanhoNome(Integer tamanhoNome) {
		this.tamanhoNome = tamanhoNome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDateTime getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(LocalDateTime dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	
	
	
	
}
