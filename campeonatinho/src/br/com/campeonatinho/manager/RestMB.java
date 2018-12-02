package br.com.campeonatinho.manager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import br.com.campeonatinho.entity.Liga;

@ManagedBean(name = "restMB")
@RequestScoped
public class RestMB {

	private List<Liga> ligas;

	public List<Liga> getLigas() {
		return ligas;
	}

	public void setLigas(List<Liga> ligas) {
		this.ligas = ligas;
	}

	@PostConstruct
	public void init() {
		try {
			String sURL = "http://campeonatinho.ddns.net:8443/campeonatinho/campeonatinho/administracao/rest/liga/ligas"; // just
			// Connect to the URL using java's native library
			URL url = new URL(sURL);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setRequestMethod("GET");
			request.connect();
			StringBuilder result = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream()));
			System.out.println(request.getContent().toString());
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println(result);
			// Convert to a JSON object to print data
			JsonParser jp = new JsonParser(); // from gson
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); // Convert the input
																									// stream to a json
																									// element
			JsonArray rootobj = root.getAsJsonArray(); // May be an array, may be an object.

			List<Liga> ligas = new ArrayList<>();
			for (JsonElement json : rootobj) {
				ligas.add(new Gson().fromJson(json, Liga.class));
			}

			this.ligas = ligas;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
