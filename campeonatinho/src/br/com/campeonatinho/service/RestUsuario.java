package br.com.campeonatinho.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.campeonatinho.persistence.UsuarioDAO;

@Path("/usuario")
public class RestUsuario {

	@GET
	@Path("/usuarios")
	@Produces(MediaType.APPLICATION_JSON)
	public String usuarios() {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			return gson.toJson(new UsuarioDAO().buscaTodosUsuarios());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
