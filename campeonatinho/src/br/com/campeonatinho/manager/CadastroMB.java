package br.com.campeonatinho.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.campeonatinho.entity.Usuario;
import br.com.campeonatinho.persistence.UsuarioDAO;
import br.com.campeonatinho.utils.Encrypt;
import br.com.campeonatinho.utils.EnvioEmail;
import br.com.campeonatinho.utils.Perfil;

@ManagedBean(name = "cadastroMB")
@RequestScoped
public class CadastroMB {

	private Usuario usuario;
	private String emailAtivacao;
	private String idAtivacao;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getEmailAtivacao() {
		return emailAtivacao;
	}

	public void setEmailAtivacao(String emailAtivacao) {
		this.emailAtivacao = emailAtivacao;
	}

	public String getIdAtivacao() {
		return idAtivacao;
	}

	public void setIdAtivacao(String idAtivacao) {
		this.idAtivacao = idAtivacao;
	}

	@PostConstruct
	public void init() {
		this.usuario = new Usuario();
		this.emailAtivacao = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("emailAtivacao");
		this.idAtivacao = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("idAtivacao");
	}

	public void validacaoCadastro() {

		FacesContext fc = FacesContext.getCurrentInstance();

		if (this.usuario != null) {
			List<String> erros = new ArrayList<>();
			if (this.usuario.getSenha().length() < 4) {
				erros.add(erros.size() + 1 + ". A senha deve possuir mais que 3 caracteres.");
			}
			
			if (this.usuario.getSenha().length() > 10) {
				erros.add(erros.size() + 1 + ". A senha deve possuir no máximo 10 caracteres.");
			}

			if (!this.usuario.getEmail().contains("@")) {
				erros.add(erros.size() + 1 + ". Digite um e-mail válido");
			}

			if (erros.isEmpty()) {
				this.cadastrar();
			} else {
				for (String erro : erros) {
					fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, erro, null));
				}
			}
		}
		
		this.usuario = new Usuario();

	}

	public void cadastrar() {

		FacesContext fc = FacesContext.getCurrentInstance();

		try {
			this.usuario.setAtivo('N');
			this.usuario.setEmail(this.usuario.getEmail().toLowerCase());
			this.usuario.setPerfil(Perfil.USER);
			EnvioEmail.enviarEmailConfirmacao(this.usuario.getEmail());
			new UsuarioDAO().cadastro(usuario);

			fc.addMessage("formCadastro",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cadastro realizado com sucesso", null));
			fc.addMessage("formCadastro",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Ative seu cadastro no e-mail", null));
			
		} catch (Exception e) {
			e.printStackTrace();
			if (e != null) {
				if (e.getCause() != null) {
					if (e.getCause().toString().contains("Duplicate entry")) {
						fc.addMessage("formCadastro",
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"E-mail já existe em nosso banco de dados",
										"Favor, informar outro e-mail ou recuperar sua senha"));
					}
					
					if (e.getCause().toString().contains("Missing domain in string")) {
						fc.addMessage("formCadastro",
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Domínio inválido, favor, cheque seu e-mail",
										""));
					}
				} else {
					fc.addMessage("formCadastro", new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Cadastro não realizado", "Favor, verificar os dados informados"));
				}
			}

		} finally {
			this.usuario = new Usuario();
		}
	}

	public void ativacaoUsuario() {

		FacesContext fc = FacesContext.getCurrentInstance();
		
		if ((this.emailAtivacao != null) && (this.idAtivacao != null)) {
			try {
				if (Encrypt.geraChaveAtivacao(emailAtivacao).equals(idAtivacao)) {
					if (new UsuarioDAO().ativarUsuario(emailAtivacao)) {
						fc.addMessage(null, new FacesMessage("E-mail " + emailAtivacao + " ativado com sucesso!"));
					} else {
						fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "E-mail já ativado em nosso banco de dados!", null));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} 
	}

}
