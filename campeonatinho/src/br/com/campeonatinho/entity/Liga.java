package br.com.campeonatinho.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

@Table(name="liga")
@Entity
public class Liga implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	private Integer id;
	@Expose
	@Column(length = 355)
	private String logo;
	@Expose
	@Column(length = 50, nullable = false)
	private String nome;
	@Expose
	@Temporal(TemporalType.DATE)
	private Date criacaoLiga;
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	@OneToMany(mappedBy = "liga")
	private List<Partida> partidas;
	
	/**
	 * 
	 */
	public Liga() {
	}

	/**
	 * @param id
	 * @param logo
	 * @param nome
	 * @param criacaoLiga
	 */
	public Liga(Integer id, String logo, String nome, Date criacaoLiga) {
		this.id = id;
		this.logo = logo;
		this.nome = nome;
		this.criacaoLiga = criacaoLiga;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getCriacaoLiga() {
		return criacaoLiga;
	}

	public void setCriacaoLiga(Date criacaoLiga) {
		this.criacaoLiga = criacaoLiga;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Partida> getPartidas() {
		return partidas;
	}

	public void setPartidas(List<Partida> partidas) {
		this.partidas = partidas;
	}

	@Override
	public String toString() {
		return "Liga [id=" + id + ", logo=" + logo + ", nome=" + nome + ", criacaoLiga=" + criacaoLiga + "]";
	}
}
