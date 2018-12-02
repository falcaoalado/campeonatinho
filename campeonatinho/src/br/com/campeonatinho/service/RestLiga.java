package br.com.campeonatinho.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.campeonatinho.persistence.LigaDAO;

@Path("/liga")
public class RestLiga {
	
	@GET
	@Path("/ligas")
	@Produces(MediaType.APPLICATION_JSON)
	public String ligas() {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			return gson.toJson(new LigaDAO().buscaTodasLigas());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "error";
	}
}
