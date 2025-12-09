package sms;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.human.gateway.client.ClienteHuman;
import com.human.gateway.client.MensagemIndividual;
import com.human.gateway.client.Retorno;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.SMSVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

public class RobbuSms {

	public static String enviarMensagem(ConfiguracaoGeralSistemaVO conf, SMSVO sms) throws Exception {
		if (Uteis.validarEnvioEmail(conf.getIpServidor()) && !sms.getCelular().equals("") && sms.getCelular() != null && !sms.getMensagem().equals("") && sms.getMensagem() != null) {
			String retorno = executarChamadaWebService(conf, Uteis.formatarTelefoneParaEnvioSms(sms.getCelular()), sms.getMensagem(), sms.getNomeDest(), sms.getCodigoDest(), sms.getCpfDest(), sms.getMatriculaDest());
			return "EnvioOK";
		}
		return "";
	}
	
	public static String executarChamadaWebService(ConfiguracaoGeralSistemaVO conf, String celular, String texto, String nomePessoa, String codigoPessoa, String cpfPessoa, String matriculaAluno) throws Exception {
		HttpURLConnection request = (HttpURLConnection) new URL("https://api.robbu.global/v1/prodesp/sendmessage").openConnection();

		try {
			// Define que a conexão pode enviar informações e obtê-las de volta:
			request.setDoOutput(true);
			request.setDoInput(true);

			// Define o content-type:
			request.setRequestProperty("Content-Type", "application/json");
			//request.setRequestProperty("Authorization", "Bearer cXVlcnktc2VpLWNsaWVudHE1Yw==");

			// Define o método da requisição:
			request.setRequestMethod("POST");

			// Conecta na URL:
			request.connect();

			JSONObject my_obj = new JSONObject();
			my_obj.put("invenioPrivateToken", conf.getSenhaSMS());
			my_obj.put("invenioLoginWithPrivateToken", conf.getUsernameSMS());
			my_obj.put("text", texto);
			my_obj.put("channel", "2");
			JSONObject destination = new JSONObject();
			destination.put("countryCode", "55");
			destination.put("phoneNumber", celular);
			my_obj.put("destination", destination);
			JSONObject contact = new JSONObject();
			contact.put("name", nomePessoa);
			contact.put("customCode", codigoPessoa);
			contact.put("id", cpfPessoa);
			contact.put("tag", matriculaAluno);
			contact.put("updateIfExists", false);
			my_obj.put("contact", contact);

			// Escreve o objeto JSON usando o OutputStream da requisição:
			try (OutputStream outputStream = request.getOutputStream()) {
				outputStream.write(my_obj.toString().getBytes("UTF-8"));
			}

			// Caso você queira usar o código HTTP para fazer alguma coisa, descomente esta
			// linha.
			int response = request.getResponseCode();

			ByteArrayOutputStream os;
			try (InputStream is = request.getInputStream()) {
				os = new ByteArrayOutputStream();
				int b;
				while ((b = is.read()) != -1) {
					os.write(b);
				}
			}
			return new String(os.toByteArray());
		} finally {
			request.disconnect();
		}

	}	
	
}
