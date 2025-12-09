package negocio.comuns.utilitarias;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import javapns.Push;
import negocio.comuns.utilitarias.dominios.PlataformaEnum;


import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import negocio.comuns.utilitarias.dominios.PlataformaEnum;

/**
 * Responsável por executar o envio de mensagens push para clientes do tipo ANDROID e IOS.
 * 
 * @author Victor Hugo - 03 de nov de 2016
 * 
 */
public class PushNotificacao {

	private static final Logger logger = Logger.getLogger(PushNotificacao.class);

	private static final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

	//private static final String PWD_APNS = "mobOtm1016";
	
	/**
	 * Responsável por executar o envio de mensagens push para clientes do tipo ANDROID e IOS. <br>
	 * - O parâmetro idDevice é o ID do dispositivo que deve receber o push. <br>
	 * - O parâmetro título é utilizado apenas em dispositivos ANDROID. <br>
	 * - O parâmetro message é a mensagem a ser enviada ao dispositivo.
	 * 
	 * @author Wellington Rodrigues - 28 de out de 2016
	 * 
	 * @param device
	 * @param idDevice
	 * @param title
	 * @param message
	 */
	public static void send(PlataformaEnum device, String tokenCelular, String title, String message, String senderKey, byte[] iosCertificate, String senhaCertificadoAPNS, Boolean certificadoAPNSProducao) {
	    try {
//			if (PlataformaEnum.android.equals(device)) {
				pushNotificationFCM(tokenCelular, senderKey, title, message);
//			} else {
//				pushNotificationAPNS(idDevice, iosCertificate, message, senhaCertificadoAPNS, certificadoAPNSProducao);
//				}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private static void pushNotificationFCM(String idDevice, String senderKey, String title, String message) {
		try {
			URL url = new URL(API_URL_FCM);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "key=" + senderKey);
			conn.setRequestProperty("Content-Type", "application/json");

			JSONObject json = new JSONObject();
			json.put("to", idDevice);
			JSONObject info = new JSONObject();
			info.put("title", title);
			info.put("body", message);
			info.put("icon", "ic_notification");
			json.put("notification", info);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json.toString());
			wr.flush();
			conn.getInputStream();
		} catch (Exception e) {
			logger.error(e);
		}
	}

        private static void pushNotificationAPNS(String idDevice, byte[] senderKey, String message, String senhaCertificadoAPNS, Boolean certificadoAPNSProducao) {
        	try {
        	    Push.alert(message, senderKey, senhaCertificadoAPNS, certificadoAPNSProducao, idDevice);
        	} catch (Exception e) {
        	    logger.error(e);
        	}
        }
        
        
        
        
}
