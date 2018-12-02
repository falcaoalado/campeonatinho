package br.com.campeonatinho.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.campeonatinho.entity.Liga;
import br.com.campeonatinho.entity.Partida;
import br.com.campeonatinho.entity.Usuario;
import br.com.campeonatinho.persistence.LigaDAO;
import br.com.campeonatinho.persistence.PartidaDAO;
import br.com.campeonatinho.persistence.UsuarioDAO;
import br.com.campeonatinho.utils.Encrypt;
import br.com.campeonatinho.utils.Perfil;

@ManagedBean(name = "usuarioMB")
@SessionScoped
public class UsuarioMB {

	private Usuario usuario;
	private Usuario usuarioLogado;
	private String ativarUsuario;
	private List<Liga> ligas;
	private Perfil perfil;
	private String alterarSenha;

	// Administrador
	private List<Liga> todasLigas;
	private List<Usuario> todosUsuarios;
	private List<Partida> todasPartidas;

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getAtivarUsuario() {
		return ativarUsuario;
	}

	public void setAtivarUsuario(String ativarUsuario) {
		this.ativarUsuario = ativarUsuario;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public List<Liga> getLigas() {
		return ligas;
	}

	public void setLigas(List<Liga> ligas) {
		this.ligas = ligas;
	}

	public List<Liga> getTodasLigas() {
		return todasLigas;
	}

	public void setTodasLigas(List<Liga> todasLigas) {
		this.todasLigas = todasLigas;
	}

	public List<Usuario> getTodosUsuarios() {
		return todosUsuarios;
	}

	public void setTodosUsuarios(List<Usuario> todosUsuarios) {
		this.todosUsuarios = todosUsuarios;
	}

	public List<Partida> getTodasPartidas() {
		return todasPartidas;
	}

	public void setTodasPartidas(List<Partida> todasPartidas) {
		this.todasPartidas = todasPartidas;
	}

	public String getAlterarSenha() {
		return alterarSenha;
	}

	public void setAlterarSenha(String alterarSenha) {
		this.alterarSenha = alterarSenha;
	}

	@PostConstruct
	public void init() {
		this.usuario = new Usuario();
		this.carregaRelatorios();
	}

	public void inicializaPagina() {
		this.buscaLigasUsuario();
		this.carregaRelatorios();
	}

	public void carregaRelatorios() {
		if (this.usuarioLogado != null) {
			if (this.usuarioLogado.getPerfil().compareTo(Perfil.ADM) == 0) {
				this.todasLigas = new LigaDAO().buscaTodasLigas();
				this.todosUsuarios = new UsuarioDAO().buscaTodosUsuarios();
				this.todasPartidas = new PartidaDAO().buscaTodasPartidas();
			}
		}
	}

	public String logar() {

		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			List<String> erros = new ArrayList<>();

			if (this.usuario.getEmail().isEmpty()) {
				erros.add(erros.size() + 1 + ". Campo 'e-mail' em branco.");
			}

			if (this.usuario.getSenha().isEmpty()) {
				erros.add(erros.size() + 1 + ". Campo 'senha' em branco.");
			}

			if (erros.isEmpty()) {
				this.usuarioLogado = new UsuarioDAO().login(this.usuario);
				if (this.usuarioLogado != null) {
					HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
					session.setAttribute("usuarioLogado", this.usuarioLogado);
					return "/campeonatinho/inicio.xhtml?faces-redirect=true";
				} else {
					fc.addMessage("form1", new FacesMessage("Senha incorreta"));
				}
			} else {

				for (String erro : erros) {
					fc.addMessage("form1", new FacesMessage(erro));
				}
			}
		} catch (Exception e) {

		}

		return null;
	}

	public void verificaError() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession httpSession = (HttpSession) fc.getExternalContext().getSession(true);
		if (httpSession.getAttribute("error") != null) {
			fc.addMessage("form1",
					new FacesMessage(FacesMessage.SEVERITY_FATAL, httpSession.getAttribute("error").toString(), null));
		}

		httpSession.invalidate();
	}

	public String deslogar() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession httpSession = (HttpSession) fc.getExternalContext().getSession(true);
		httpSession.invalidate();

		return "/campeonatinho/inicio.xhtml?faces-redirect=true";
	}

	public void buscaLigasUsuario() {
		try {
			HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(true);
			Usuario usuarioLogado = (Usuario) httpSession.getAttribute("usuarioLogado");
			this.ligas = new LigaDAO().buscaLigaPeloUsuario(usuarioLogado.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mudarPerfil(Usuario usuario) {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			if (usuario.getPerfil().compareTo(Perfil.ADM) == 0) {
				usuario.setPerfil(Perfil.USER);
			} else {
				usuario.setPerfil(Perfil.ADM);
			}
			new UsuarioDAO().update(usuario);
			fc.addMessage(null, new FacesMessage("Perfil alterado com sucesso!"));
		} catch (Exception e) {
			throw e;
		}
	}

	public void alterarSenha() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if (this.alterarSenha.length() < 4) {
			fc.addMessage(null, new FacesMessage("Senha deverá conter mais que 3 caracteres"));
		} else if (this.alterarSenha.length() > 10) {
			fc.addMessage(null, new FacesMessage("Senha deverá conter até 10 caracteres"));
		} else {
			try {
				this.usuarioLogado.setSenha(this.alterarSenha);
				Encrypt.encryptPassword(this.usuarioLogado);
				new UsuarioDAO().update(this.usuarioLogado);
				fc.addMessage(null, new FacesMessage("Senha alterada com sucesso!"));
			} catch (Exception e) {
				fc.addMessage(null, new FacesMessage("Deu ruim"));
			}
		}
		this.alterarSenha = "";
	}

	public void desativarUsuario(Usuario usuario) {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			usuario.setAtivo('N');
			new UsuarioDAO().update(usuario);
			fc.addMessage(null, new FacesMessage("Usuário desativado!"));
		} catch (Exception e) {
			fc.addMessage(null, new FacesMessage("Deu ruim"));
			throw e;
		}
	}

	public void ativarUsuario(Usuario usuario) {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			usuario.setAtivo('Y');
			new UsuarioDAO().update(usuario);
			fc.addMessage(null, new FacesMessage("Usuário ativado!"));
		} catch (Exception e) {
			fc.addMessage(null, new FacesMessage("Deu ruim"));
			throw e;
		}
	}
}
