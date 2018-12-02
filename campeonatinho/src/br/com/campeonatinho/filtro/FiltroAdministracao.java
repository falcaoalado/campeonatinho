package br.com.campeonatinho.filtro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.campeonatinho.entity.Usuario;
import br.com.campeonatinho.utils.Perfil;

@WebFilter("/campeonatinho/administracao/*")
public class FiltroAdministracao implements Filter {

    public FiltroAdministracao() {
    }

    public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		
		if ((usuario != null) && (usuario.getPerfil().compareTo(Perfil.ADM) == 0)) {
			chain.doFilter(request, response);
		} else {
			session.setAttribute("error", new String("Usuário não logado ou não tem permissão para acessar"));
			res.sendRedirect("../login.jsf");
			session.invalidate();
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
