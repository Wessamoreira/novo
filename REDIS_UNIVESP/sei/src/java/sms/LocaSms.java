package sms;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.human.gateway.client.ClienteHuman;
import com.human.gateway.client.MensagemIndividual;
import com.human.gateway.client.Retorno;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

public class LocaSms {

	public static String enviarMensagem(ConfiguracaoGeralSistemaVO conf, String celular, String texto) throws Exception {
		if (Uteis.validarEnvioEmail(conf.getIpServidor()) && !celular.equals("") && celular != null && !texto.equals("") && texto != null) {
			//String urlEnvio = "http://54.173.24.177/painel/api.ashx?action=sendsms&lgn="+conf.getUsernameLocaSMS();
			String urlEnvio = "http://209.133.205.2/painel/api.ashx?action=sendsms&lgn="+conf.getUsernameSMS();
			//urlEnvio += "6291813128";
			urlEnvio += "&pwd=" + conf.getSenhaSMS();
			
			//urlEnvio += "929231"; 		
			String textoEncode = URLEncoder.encode(texto, "UTF-8");
			urlEnvio += "&msg=" + textoEncode; 
			//celular = "6291813128";
			urlEnvio += "&numbers=" + celular;
			URL url = new URL(urlEnvio);
			Reader br = new InputStreamReader(url.openStream());

			JSONParser parser = new JSONParser();
			JSONObject jsonObjeto = (JSONObject) parser.parse(br);
			System.out.println(jsonObjeto);
			
			JSONObject jsonResponse = (JSONObject) jsonObjeto;
						
			String msgRetorno = "";
			if (jsonResponse != null) {
				msgRetorno = (String) jsonResponse.get("msg");
			} else {
				msgRetorno = "";
			}
//			String status = (String) jsonObjeto.get("status");
			if (!msgRetorno.equals("SUCESSO")) {
				throw new Exception("Erro ao enviar sms" + msgRetorno);
			}
			return "EnvioOK";
		}
		return "";
	}
}
