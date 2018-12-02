package br.com.campeonatinho.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "partida")
@Entity
public class Partida implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "id_liga", nullable = false)
	private Liga liga;
	@ManyToOne
	@JoinColumn(name = "id_casa", referencedColumnName = "id")
	private Time casa;
	@ManyToOne
	@JoinColumn(name = "id_visitante", referencedColumnName = "id")
	private Time visitante;
	@Column
	private Integer golsTimeCasa;
	@Column
	private Integer golsTimeVisitante;
	@Temporal(value = TemporalType.DATE)
	private Date ultimaAlteracao;
	@Column
	private Integer efetuada;

	public Partida(Integer id, Integer golsTimeCasa, Integer golsTimeVisitante, Date ultimaAlteracao,
			Integer efetuada) {
		this.id = id;
		this.golsTimeCasa = golsTimeCasa;
		this.golsTimeVisitante = golsTimeVisitante;
		this.ultimaAlteracao = ultimaAlteracao;
		this.efetuada = efetuada;
	}

	/**
	 * 
	 */
	public Partida() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Time getCasa() {
		return casa;
	}

	public void setCasa(Time casa) {
		this.casa = casa;
	}

	public Time getVisitante() {
		return visitante;
	}

	public void setVisitante(Time visitante) {
		this.visitante = visitante;
	}

	public Integer getGolsTimeCasa() {
		return golsTimeCasa;
	}

	public void setGolsTimeCasa(Integer golsTimeCasa) {
		this.golsTimeCasa = golsTimeCasa;
	}

	public Integer getGolsTimeVisitante() {
		return golsTimeVisitante;
	}

	public void setGolsTimeVisitante(Integer golsTimeVisitante) {
		this.golsTimeVisitante = golsTimeVisitante;
	}

	public Date getUltimaAlteracao() {
		return ultimaAlteracao;
	}

	public void setUltimaAlteracao(Date ultimaAlteracao) {
		this.ultimaAlteracao = ultimaAlteracao;
	}

	public Integer getEfetuada() {
		return efetuada;
	}

	public void setEfetuada(Integer efetuada) {
		this.efetuada = efetuada;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

}
