package br.com.campeonatinho.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.campeonatinho.entity.Liga;

public class LigaDAO extends DAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public void criarLiga(Liga liga) {

		try {
			super.openSessionWithTransaction();
			super.session.save(liga);
			super.transaction.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}

	public Liga criarLigaRetornaObj(Liga liga) {

		try {
			super.openSessionWithTransaction();
			super.session.save(liga);
			super.transaction.commit();

			return liga;
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}

	public Liga buscaLigaPeloId(Integer idLiga) {

		try {

			super.openSession();
			super.query = super.session.createQuery("SELECT l FROM Liga as l WHERE l.id=:param1");
			super.query.setParameter("param1", idLiga);

			return (Liga) super.query.uniqueResult();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Liga> buscaLigaPeloUsuario(Integer idUsuario) {

		List<Liga> ligas = new ArrayList<>();

		try {

			super.openSession();
			super.query = super.session.createQuery("SELECT l FROM Liga as l WHERE l.usuario.id=:param1");
			super.query.setParameter("param1", idUsuario);
			ligas = super.query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			super.session.close();
		}

		return ligas;
	}

	public List<Liga> buscaTodasLigas() {

		List<Liga> ligas = new ArrayList<>();

		try {
			super.openSession();
			super.query = super.session.createQuery("SELECT l FROM Liga as l");
			ligas = super.query.list();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			super.session.close();
		}

		return ligas;
	}
	
	public void excluirLiga(Liga liga) {
		
		try {
			super.openSessionWithTransaction();
			super.session.delete(liga);
			super.transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			super.session.close();
		}
	}

}
