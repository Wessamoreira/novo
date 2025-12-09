package sms;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.utilitarias.Uteis;

public class FacilitaSms {
	
	public static String enviarMensagem(ConfiguracaoGeralSistemaVO conf, String celular, String texto) throws Exception {
		if (Uteis.validarEnvioEmail(conf.getIpServidor()) && !celular.equals("") && celular != null && !texto.equals("") && texto != null) {
			
			String urlEnvio  = "http://api.facilitamovel.com.br/api/simpleSend.ft?user="+conf.getUsernameSMS() ;
						
			urlEnvio += "&password=" + conf.getSenhaSMS();
			
			
			//&destinatario=5191561100			
			urlEnvio += "&destinatario=" + celular;
			
			//&externalkey=123
			//urlEnvio += "externalkey=123";
			
			//&msg=Ola%20Facilita";	
			String textoEncode = URLEncoder.encode(texto, "UTF-8");
			urlEnvio += "&msg=" + textoEncode; 
			
			URL url = new URL(urlEnvio);
			Reader br = new InputStreamReader(url.openStream());
			
			int data = br.read();
			String  resultado ="" ;
			while(data != -1){
				resultado += (char) data;			   
			    data = br.read();
			}
			String msgRetorno = "";
			int  resultadoretorno = Integer.parseInt(resultado.substring(0, 1));		
			
			if(resultadoretorno == 1 ) {
				msgRetorno = "Usuário ou Senha enviados na URL estão inválidos, ou a conta pode estar inativa/cancelada";
			}else if(resultadoretorno == 2) {
				msgRetorno="Usuário não possúi créditos na plataforma";
			}else if(resultadoretorno == 3 ) {
				msgRetorno ="Número de Celular enviado por parâmetro, está inválido" ;
			}else if(resultadoretorno == 4 ) {
				msgRetorno="A mensagem passada está vazia, ou possui características de uma mensagem inválida";
			}else {
				msgRetorno="SUCESSO";
			}			
			if (!msgRetorno.equals("SUCESSO")) {
				throw new Exception("Erro ao enviar sms" + msgRetorno);
			}
			return "EnvioOK";
		}
		return "";
	}
}
