package br.com.campeonatinho.persistence;

import java.util.Date;
import java.util.List;

import br.com.campeonatinho.entity.Partida;

public class PartidaDAO extends DAO {

	public void gravarPartidas(List<Partida> partidas) {

		try {
			super.openSessionWithTransaction();
			for (Partida partida : partidas) {
				partida.setEfetuada(0);
				super.session.save(partida);
			}
			super.transaction.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}
	
	public void update(Partida partida) {

		try {
			super.openSessionWithTransaction();
			super.session.update(partida);
			super.transaction.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Partida> buscaPartidaPelaLiga(Integer idLiga) {

		try {
			super.openSessionWithTransaction();
			super.query = super.session.createQuery("SELECT p FROM Partida AS p WHERE p.liga.id=:param1 AND p.efetuada=:param2");
			super.query.setParameter("param1", idLiga);
			super.query.setParameter("param2", 0);
			return super.query.list();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Partida> buscaPartidaEfetuadaPelaLiga(Integer idLiga) {

		try {
			super.openSessionWithTransaction();
			super.query = super.session.createQuery("SELECT p FROM Partida AS p WHERE p.liga.id=:param1 AND p.efetuada=:param2");
			super.query.setParameter("param1", idLiga);
			super.query.setParameter("param2", 1);
			return super.query.list();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Partida> buscaPartidaPeloTime(Integer idTime) {

		try {
			super.openSessionWithTransaction();
			super.query = super.session
					.createQuery("SELECT p FROM Partida AS p WHERE p.casa.id=:param1 OR p.visitante.id=:param1");
			super.query.setParameter("param1", idTime);
			return super.query.list();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}

	public void atualizarPartidas(List<Partida> partidas) {

		try {
			super.openSessionWithTransaction();
			for (Partida partida : partidas) {
				if (partida.getEfetuada() != 1) {
					if ((partida.getGolsTimeCasa() != null) && (partida.getGolsTimeVisitante() != null)) {
						partida.setUltimaAlteracao(new Date());
						partida.setEfetuada(1);
						super.session.update(partida);
					}
				}
			}
			super.transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			super.session.close();
		}
	}

	public List<Partida> buscaTodasPartidas() {

		try {
			super.openSessionWithTransaction();
			super.query = super.session.createQuery("SELECT p FROM Partida AS p");
			return super.query.list();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}

	public void excluirPartidasPeloIdLiga(Integer idLiga) {
		try {
			super.openSessionWithTransaction();
			super.query = super.session.createQuery("DELETE FROM Partida AS p WHERE p.liga.id=:param1");
			super.query.setParameter("param1", idLiga);
			System.out.println(super.query.executeUpdate());
			super.transaction.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}
}
