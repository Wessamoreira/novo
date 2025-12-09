package integracoes.cobranca;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class CECAM {
	
	private static String getUrlCECAM() {
		return "http://cecam.com/FAED";
	}
	
	private static String getUrlTokenLogin() {
		return getUrlCECAM() + "/login/GerarTokenRA";
	}
	
	private static String getUrlConsultaSituacaoBoleto(String token, Integer curso) {
		return getUrlCECAM() + "/ConsultaBoletos/consultarBoletoAluno?Token=" + token + "&CDCURSO=" + curso;
	}
	
	private static String getUrlConsultaSituacaoMatricula(String token) {
		return getUrlCECAM() + "/ConsultaBoletos/consultarSituacaoMatricula?Token=" + token;
	}
	
	private static String gerarTokenLogin(String matricula) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(getUrlTokenLogin());
		List<NameValuePair> arguments = new ArrayList<>(2);
        arguments.add(new BasicNameValuePair("RA", matricula));
        arguments.add(new BasicNameValuePair("Senha", "System"));
		try {
	        post.setEntity(new UrlEncodedFormEntity(arguments));
			HttpResponse response = client.execute(post);
			System.out.println(EntityUtils.toString(response.getEntity()));
//			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
//			StringBuffer resultado = new StringBuffer();
//			String line = "";
//			while ((line = rd.readLine()) != null) {
//				resultado.append(line);
//			}
//			return resultado.toString();
			return "ok";
		} catch (Exception e) {
			return "nok";
		}
	}
	
	public static String consultarSituacaoMatricula(String matricula) {
		try {
			HttpGet get = new HttpGet(getUrlConsultaSituacaoMatricula(gerarTokenLogin(matricula)));
			HttpClient client = HttpClientBuilder.create().build();
//			HttpResponse response = client.execute(get);
//			return realizarExtracaoSituacao(EntityUtils.toString(response.getEntity()));
			return realizarExtracaoSituacao("{\"CodigoRetorno\":10,\"MensagemRetorno\":\"Apto\"}");
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public static String consultarSituacaoBoleto(String matricula, Integer curso) {
		try {
			HttpGet get = new HttpGet(getUrlConsultaSituacaoBoleto(gerarTokenLogin(matricula), curso));
			HttpClient client = HttpClientBuilder.create().build();
//			HttpResponse response = client.execute(get);
//			return realizarExtracaoSituacao(EntityUtils.toString(response.getEntity()));
			return realizarExtracaoSituacao("{\"CodigoRetorno\":8,\"MensagemRetorno\":\"Boleto Pago\"}");
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public static String realizarExtracaoSituacao(String json) throws Exception {
		return json.substring(json.indexOf("MensagemRetorno") + 18, json.length() - 2);
	}

}