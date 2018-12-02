package br.com.campeonatinho.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.UploadedFile;

import br.com.campeonatinho.entity.Liga;
import br.com.campeonatinho.entity.Partida;
import br.com.campeonatinho.entity.Time;
import br.com.campeonatinho.entity.Usuario;
import br.com.campeonatinho.persistence.LigaDAO;
import br.com.campeonatinho.persistence.PartidaDAO;
import br.com.campeonatinho.persistence.TimeDAO;

@ManagedBean(name = "ligaMB")
@ViewScoped
public class LigaMB {

	private Liga liga;
	private List<Integer> quantidadeTimes;
	private Integer quantidadeSelecionada;
	private UploadedFile file;
	private Boolean formTimes;
	private List<Time> times;

	public List<Time> getTimes() {
		return times;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
	}

	public Boolean getFormTimes() {
		return formTimes;
	}

	public void setFormTimes(Boolean formTimes) {
		this.formTimes = formTimes;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	public Integer getQuantidadeSelecionada() {
		return quantidadeSelecionada;
	}

	public void setQuantidadeSelecionada(Integer quantidadeSelecionada) {
		this.quantidadeSelecionada = quantidadeSelecionada;
	}

	public List<Integer> getQuantidadeTimes() {
		return quantidadeTimes;
	}

	public void setQuantidadeTimes(List<Integer> quantidadeTimes) {
		this.quantidadeTimes = quantidadeTimes;
	}

	@PostConstruct
	public void init() {
		this.geraQuantidadeTimes();
		this.liga = new Liga();
		this.times = new ArrayList<>();
	}

	public void criarLiga() {

		FacesContext fc = FacesContext.getCurrentInstance();

		try {

			HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(true);
			Usuario usuarioLogado = (Usuario) httpSession.getAttribute("usuarioLogado");
			this.liga.setCriacaoLiga(new Date());
			this.liga.setUsuario(usuarioLogado);
			System.out.println(file);

			if (this.file != null) {
				this.liga.setLogo(this.salvarImagem());
			}
			Liga ligaCriada = new LigaDAO().criarLigaRetornaObj(liga);
			this.salvarTimes(ligaCriada);
			this.gerarPartidas(ligaCriada);
			fc.addMessage("crialiga", new FacesMessage("Liga criada com sucesso."));
		} catch (Exception e) {
			e.printStackTrace();
			fc.addMessage("crialiga", new FacesMessage("Deu merda"));
		} finally {
			this.liga = new Liga();
			this.times = new ArrayList<>();
			this.formTimes = false;
		}
	}

	private void gerarPartidas(Liga ligaCriada) {
		
		List<Partida> partidas = new ArrayList<>();
		
		for (int x = 0 ; x < this.times.size() ; x++) {
			for (int j = x+1 ; j < this.times.size() ; j++) {
				Partida partida = new Partida();
				partida.setCasa(this.times.get(x));
				partida.setVisitante(this.times.get(j));
				partida.setLiga(ligaCriada);
				partidas.add(partida);
			}
		}
		
		new PartidaDAO().gravarPartidas(partidas);
	}

	private void salvarTimes(Liga liga) {
		for (Time time : this.times) {
			time.setLiga(liga);
		}
		new TimeDAO().criarTimes(this.times);
	}

	private String salvarImagem() {

		try {
			byte arq[] = this.file.getContents();
			String uuid = UUID.randomUUID().toString();
			String caminho = File.separator + "home/falke/campeonatinho/campeonatinho/WebContent/imagens"
					+ File.separator + uuid + ".jpg";
			System.out.println(caminho);
			FileOutputStream fos = new FileOutputStream(caminho);
			fos.write(arq);
			fos.close();

			return uuid + ".jpg";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void geraQuantidadeTimes() {

		this.quantidadeTimes = new ArrayList<>();
		int contador = 13;

		for (int x = 2; x < contador; x += 2) {
			this.quantidadeTimes.add(x);
		}
	}

	public void ativaFormTimes() {

		this.formTimes = true;
		this.times = new ArrayList<>();

		for (int x = 0; x < quantidadeSelecionada; x++) {
			this.times.add(new Time());
		}
	}
	
	public String excluirLiga(Liga liga) {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			new PartidaDAO().excluirPartidasPeloIdLiga(liga.getId());
			new TimeDAO().excluirTimesPeloIdLiga(liga.getId());
			new LigaDAO().excluirLiga(liga);
			fc.addMessage("mensagemliga", new FacesMessage("Liga excluida com sucesso!"));
		} catch (Exception e) {
			fc.addMessage("mensagemliga", new FacesMessage("Algo errado não está certo"));
			throw e;
		}
		
		return "/campeonatinho/minhasligas.jsf?faces-redirect=true";
	}
}