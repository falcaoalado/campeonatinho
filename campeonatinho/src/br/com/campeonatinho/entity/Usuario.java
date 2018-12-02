package br.com.campeonatinho.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

import br.com.campeonatinho.utils.Perfil;

@Table(name = "usuario")
@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Expose
	private Integer id;
	@Column(length = 150, unique = true)
	@Expose
	private String email;
	@Column(length = 255, nullable = false)
	@Expose
	private String senha;
	@Column (nullable = false)
	@Expose
	private char ativo;
	@Enumerated(EnumType.STRING)
	@Column(length = 4)
	@Expose
	private Perfil perfil;
	@OneToMany(mappedBy = "usuario", targetEntity = Liga.class, cascade = CascadeType.ALL)
	private List<Liga> ligas;

	public Usuario() {
	}

	public Usuario(Integer id, String email, String senha, char ativo, Perfil perfil) {
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.ativo = ativo;
		this.perfil = perfil;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<Liga> getLigas() {
		return ligas;
	}

	public void setLigas(List<Liga> ligas) {
		this.ligas = ligas;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public char getAtivo() {
		return ativo;
	}

	public void setAtivo(char ativo) {
		this.ativo = ativo;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", email=" + email + ", ativo=" + ativo + ", perfil=" + perfil + "]";
	}

}
