package br.com.campeonatinho.persistence;

import java.util.ArrayList;
import java.util.List;

import br.com.campeonatinho.entity.Usuario;
import br.com.campeonatinho.utils.Constantes;
import br.com.campeonatinho.utils.Encrypt;

public class UsuarioDAO extends DAO {

	public void cadastro(Usuario usuario) throws Exception {

		try {
			if (usuario != null) {
				super.openSessionWithTransaction();
				Encrypt.encryptPassword(usuario);
				super.session.save(usuario);
				super.transaction.commit();
			}
		} catch (Exception e) {

			throw e;
		} finally {
			super.session.close();
		}
	}

	public Usuario login(Usuario usuario) {

		try {
			if (usuario != null) {
				super.openSession();
				super.query = super.session.createQuery(
						"SELECT u FROM Usuario as u WHERE u.email=:param1 AND u.senha=:param2 AND u.ativo=:param3");
				super.query.setParameter("param1", usuario.getEmail());
				Encrypt.encryptPassword(usuario);
				super.query.setParameter("param2", usuario.getSenha());
				super.query.setParameter("param3", Constantes.ATIVACAO_POSITVO);
				Usuario usuarioEncontrado = (Usuario) super.query.uniqueResult();

				return usuarioEncontrado;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			super.session.close();
		}

		return null;
	}

	public boolean update(Usuario usuario) {

		try {
			super.openSessionWithTransaction();
			super.transaction.begin();
			super.session.update(usuario);
			super.transaction.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			super.session.close();
		}

		return false;
	}

	public boolean ativarUsuario(String email) {

		try {
			super.openSessionWithTransaction();
			super.query = super.session.createQuery("SELECT u FROM Usuario as u WHERE u.email=:param1");
			super.query.setParameter("param1", email);
			Usuario usuario = (Usuario) super.query.uniqueResult();

			if (usuario != null) {
				if (Character.compare(usuario.getAtivo(), Constantes.ATIVACAO_POSITVO) != 0) {
					super.query = super.session.createQuery("UPDATE Usuario SET ativo='Y' WHERE email=:param1");
					super.query.setParameter("param1", usuario.getEmail());
					super.query.executeUpdate();
					super.transaction.commit();
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			super.session.close();
		}

		return false;
	}

	public List<Usuario> buscaTodosUsuarios() {

		List<Usuario> usuarios = new ArrayList<>();

		try {
			super.openSession();
			super.query = super.session.createQuery("SELECT u FROM Usuario as u");
			usuarios =  super.query.list();
			
			return usuarios;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			super.session.close();
		}

		return null;
	}
	
}
