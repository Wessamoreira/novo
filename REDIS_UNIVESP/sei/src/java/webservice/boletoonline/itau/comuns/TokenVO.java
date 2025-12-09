package webservice.boletoonline.itau.comuns;

import java.net.URI;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import negocio.comuns.utilitarias.StreamSeiException;

public class TokenVO {

	private String access_token;
	private Integer expires_in;
	private String token_type;
	
	public static TokenVO autenticarTokenPorRestTemplate(String url, String basicAuthUserName, String basicAuthPassword, String body  ) throws Exception {
		ResponseEntity<String> response = null;
		RequestEntity<String> request = null;
		request = RequestEntity.post(new URI(url))
				.header("Authorization", "Basic " +  Base64.getEncoder().encodeToString((basicAuthUserName+":"+basicAuthPassword).getBytes()))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(body);
		response = new RestTemplate().exchange(request, String.class);
		return new Gson().fromJson(response.getBody(), TokenVO.class);
	}	
	
	public static TokenVO autenticarBasicaTokenPorUnirest(String url, String basicAuthUserName, String basicAuthPassword, Map<String, Object> fields) {
		HttpResponse<String> response = Unirest.post(url)
				.basicAuth(basicAuthUserName, basicAuthPassword)
				.header("Content-Type", "application/x-www-form-urlencoded")
				.fields(fields).asString();
		return new Gson().fromJson(response.getBody(), TokenVO.class);
	}
	
	public static TokenVO autenticarFiedsTokenPorUnirest(String url, Map<String, Object> fields) {
		HttpResponse<String> response = Unirest.post(url)
				.header("Content-Type", "application/x-www-form-urlencoded")
				.fields(fields).asString();
		if (response != null && !response.isSuccess()) {
			throw new StreamSeiException((String) response.getBody().toString());
		}
		return new Gson().fromJson(response.getBody(), TokenVO.class);
	}
	

	public String getAccess_token() {
		if (access_token == null) {
			access_token = "";
		}
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public Integer getExpires_in() {
		if (expires_in == null) {
			expires_in = 0;
		}
		return expires_in;
	}

	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

	public String getToken_type() {
		if (token_type == null) {
			token_type = "";
		}
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

}
