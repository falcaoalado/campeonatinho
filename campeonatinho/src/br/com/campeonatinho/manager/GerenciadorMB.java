package br.com.campeonatinho.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.campeonatinho.entity.Liga;
import br.com.campeonatinho.entity.Partida;
import br.com.campeonatinho.entity.Time;
import br.com.campeonatinho.entity.Usuario;
import br.com.campeonatinho.persistence.LigaDAO;
import br.com.campeonatinho.persistence.PartidaDAO;
import br.com.campeonatinho.persistence.TimeDAO;
import br.com.campeonatinho.utils.Constantes;

@ManagedBean(name = "gerenciadorMB")
@ViewScoped
public class GerenciadorMB {

	private Liga liga;
	private List<Time> times;
	private List<Partida> partidas;
	private List<Partida> partidasEfetuadas;
	private Integer idLiga;
	private List<Integer> posicoes;
	private Partida partidaSelecionada;

	private Time campeao;

	public Partida getPartidaSelecionada() {
		return partidaSelecionada;
	}

	public void setPartidaSelecionada(Partida partidaSelecionada) {
		this.partidaSelecionada = partidaSelecionada;
	}

	public Time getCampeao() {
		return campeao;
	}

	public void setCampeao(Time campeao) {
		this.campeao = campeao;
	}

	public List<Partida> getPartidasEfetuadas() {
		return partidasEfetuadas;
	}

	public void setPartidasEfetuadas(List<Partida> partidasEfetuadas) {
		this.partidasEfetuadas = partidasEfetuadas;
	}

	public List<Integer> getPosicoes() {
		return posicoes;
	}

	public void setPosicoes(List<Integer> posicoes) {
		this.posicoes = posicoes;
	}

	public List<Partida> getPartidas() {
		return partidas;
	}

	public void setPartidas(List<Partida> partidas) {
		this.partidas = partidas;
	}

	public List<Time> getTimes() {
		return times;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	public Integer getIdLiga() {
		return idLiga;
	}

	public void setIdLiga(Integer idLiga) {
		this.idLiga = idLiga;
	}

	@PostConstruct
	public void init() {
		this.idLiga = Integer.parseInt(
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idliga"));
		this.liga = new LigaDAO().buscaLigaPeloId(idLiga);
		this.times = this.timesComPontuacao();
		this.partidas = new PartidaDAO().buscaPartidaPelaLiga(idLiga);
		this.partidasEfetuadas = new PartidaDAO().buscaPartidaEfetuadaPelaLiga(idLiga);
		if (this.partidas.size() == 0) {
			this.verificaCampeao();
		}
		this.verificaPermissao();
		for (Partida partida : this.partidas) {
			partida.setLiga(this.liga);
		}
	}

	private void verificaCampeao() {

		this.times.sort(new Comparator<Time>() {
			@Override
			public int compare(Time o1, Time o2) {
				// TODO Auto-generated method stub
				return o2.getPontos().compareTo(o1.getPontos());
			}
		});

		this.campeao = this.times.get(0);
	}

	public void verificaPermissao() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		Usuario verificaUsuario = (Usuario) httpSession.getAttribute("usuarioLogado");
		System.out.println(verificaUsuario);
		System.out.println(this.liga.getUsuario());
		System.out.println(verificaUsuario.getId().compareTo(this.liga.getUsuario().getId()));
		if (verificaUsuario.getId().compareTo(this.liga.getUsuario().getId()) != 0) {
			try {
				fc.getExternalContext().redirect("inicio.jsf");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
	}

	public void atualizarPartidas() {

		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			new PartidaDAO().atualizarPartidas(this.partidas);
			this.partidasEfetuadas = new PartidaDAO().buscaPartidaEfetuadaPelaLiga(idLiga);
			this.times = this.timesComPontuacao();
			fc.addMessage("formPartida", new FacesMessage("Partidas atualizadas com sucesso!"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Time> timesComPontuacao() {

		Map<Time, List<Partida>> pontuacao = new HashMap<>();
		ArrayList<Time> times = new ArrayList<>();

		for (Time time : new TimeDAO().buscaTimes(idLiga)) {
			pontuacao.put(time, new PartidaDAO().buscaPartidaPeloTime(time.getId()));
		}

		for (Map.Entry<Time, List<Partida>> map : pontuacao.entrySet()) {

			Integer pontos = 0;
			Time time = map.getKey();
			for (Partida partida : map.getValue()) {
				if ((partida.getGolsTimeCasa() != null) && (partida.getGolsTimeVisitante() != null)) {
					if (time.getId().compareTo(partida.getCasa().getId()) == 0) {
						pontos += this.calculaPontos(partida.getGolsTimeCasa(), partida.getGolsTimeVisitante());
						time.setGolsPro(time.getGolsPro() + partida.getGolsTimeCasa());
						time.setGolsContra(time.getGolsContra() + partida.getGolsTimeVisitante());
					} else if (time.getId().compareTo(partida.getVisitante().getId()) == 0) {
						pontos += this.calculaPontos(partida.getGolsTimeVisitante(), partida.getGolsTimeCasa());
						time.setGolsPro(time.getGolsPro() + partida.getGolsTimeVisitante());
						time.setGolsContra(time.getGolsContra() + partida.getGolsTimeCasa());
					}
				}
			}
			time.setPontos(pontos);
			times.add(time);
		}

		times.sort(new Comparator<Time> () {

			@Override
			public int compare(Time time, Time time2) {
				if (time2.getPontos().compareTo(time.getPontos()) != 0) {
					return time2.getPontos().compareTo(time.getPontos());
				} else {

					if (time2.getGolsPro() > time.getGolsPro()) {
						return 1;
					}
					
					if (time2.getGolsPro() < time.getGolsPro()) {
						return -1;
					}
					
					if (time2.getGolsContra() > time.getGolsContra()) {
						return -1;
					}
					
					if (time2.getGolsContra() < time.getGolsContra()) {
						return 1;
					}
				}
				return time.getNome().compareTo(time2.getNome());
			}
			
		});
		return times;

	}

	private Integer calculaPontos(Integer golsTime, Integer golsTimeAdversario) {
		if (golsTime > golsTimeAdversario) {
			return Constantes.PONTOS_VITORIA;
		} else if (golsTime < golsTimeAdversario) {
			return Constantes.PONTOS_DERROTA;
		} else {
			return Constantes.PONTOS_EMPATE;
		}
	}

	public void atualizarPartida() {
		try {
			new PartidaDAO().update(this.partidaSelecionada);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}