package br.com.campeonatinho.persistence;



import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class DAO {

	Session session;
	Transaction transaction;
	Criteria criteria;
	Query query;
	
	public void openSession() {
		this.session = HibernateUtil.getSessionFactory().openSession();
	}
	
	public void openSessionWithTransaction() {
		this.openSession();
		this.transaction = this.session.beginTransaction();
	}
}
