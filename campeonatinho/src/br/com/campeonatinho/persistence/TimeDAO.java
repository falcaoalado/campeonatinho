package br.com.campeonatinho.persistence;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;

import br.com.campeonatinho.entity.Time;

public class TimeDAO extends DAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public void criarTime(Time time) {

		try {
			super.openSessionWithTransaction();
			super.session.save(time);
			super.transaction.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}

	public void criarTimes(List<Time> times) {

		try {
			super.openSessionWithTransaction();
			for (Time time : times) {
				super.session.save(time);
			}
			super.transaction.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}

	public List<Time> buscaTimes(Integer idLiga) {

		try {
			super.openSessionWithTransaction();
			super.query = super.session.createQuery("SELECT t FROM Time AS t WHERE t.liga.id=:param1");
			super.query.setParameter("param1", idLiga);
			super.transaction.commit();
			return super.query.list();
		} catch (Exception e) {
			throw e;
		} finally {
			super.session.close();
		}
	}

	public void excluirTimesPeloIdLiga(Integer idLiga) {
		try {
			super.openSessionWithTransaction();
			super.query = super.session.createQuery("DELETE FROM Time AS t WHERE t.liga.id=:param1");
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