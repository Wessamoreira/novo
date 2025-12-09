package webservice.nfse.generic;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ClienteNotaFiscalServicoEletronica {
	
	private final String PREFIX = "/rest/recepcao/";
	
	public NFSeVO gerarNFSe(NFSeVO nfseVO, String url) throws Exception {
		if (nfseVO == null) {
			throw new Exception("NFSe não foi informada!");
		}
		if (url == null || url.trim().isEmpty()) {
			throw new Exception("URL do webservice NFSe não foi cadastrada!");
		}
		String retorno = sendPost(nfseVO, url, "gerarNFSe");
		nfseVO = converterObjeto(nfseVO, retorno);
//		if (nfseVO != null) {
//			if (nfseVO.getUrlVisualizacao() != null) {
//				System.out.println("\nSituação Lote: "+ nfseVO.getSituacaoLoteRPS().getNome());
//				System.out.println("Protocolo: "+ nfseVO.getProtocolo());
//				System.out.println("Código Verificação: "+ nfseVO.getCodigoVerificacao());
//				System.out.println("NFS-e: "+ nfseVO.getNumeroNFSe());
//				System.out.println("Impressão:");
//				System.out.println(nfseVO.getUrlVisualizacao());
//			}
//			System.out.println("\nMensagens:");
//			for (String msg : nfseVO.getMensagens()) {
//				System.out.println(msg);
//			}
//		}
		return nfseVO;
	}
	
//	public void enviar() throws Exception {
//		String retorno = sendPost("enviar");
//		converterObjeto(retorno);
//	}
//	
//	public void consultar() throws Exception {
//		String retorno = sendPost("consultar");
//		converterObjeto(retorno);
//	}
//	
//	public void consultarSituacaoLote(NFSeVO nfseVO, String url) throws Exception {
//		for (int i = 0; i < 10; i++) {
//			System.out.println(i+1);
//			String retorno = sendPost("consultarSituacaoEnvio");
//			converterObjeto(retorno);
//			if(nfseVO.getSituacaoLoteRPS() != null) {
//				System.out.println(nfseVO.getSituacaoLoteRPS().getNome());
//				if (nfseVO.getSituacaoLoteRPS().equals(SituacaoLoteRPSEnum.PROCESSADO_COM_ERRO) ||
//						nfseVO.getSituacaoLoteRPS().equals(SituacaoLoteRPSEnum.PROCESSADO_COM_SUCESSO)) {
//					break;
//				}
//			}
//		}
//	}
	
	public NFSeVO imprimir(NFSeVO nfseVO, String url) throws Exception {
		if (nfseVO == null) {
			throw new Exception("NFSe não foi informada!");
		}
		if (url == null || url.trim().isEmpty()) {
			throw new Exception("URL do webservice NFSe não foi cadastrada!");
		}
		String retorno = sendPost(nfseVO, url, "imprimir");
		nfseVO = converterObjeto(nfseVO, retorno);
		if (nfseVO.getUrlVisualizacao() != null) {
			System.out.println("Impressão:");
			System.out.println(nfseVO.getUrlVisualizacao());
		}
		return nfseVO;
	}

	public NFSeVO cancelar(NFSeVO nfseVO, String url) throws Exception {
		if (nfseVO == null) {
			throw new Exception("NFSe não foi informada!");
		}
		if (url == null || url.trim().isEmpty()) {
			throw new Exception("URL do webservice NFSe não foi cadastrada!");
		}
		String retorno = sendPost(nfseVO, url, "cancelar");
		nfseVO = converterObjeto(nfseVO, retorno);
//		if (nfseVO.getUrlVisualizacao() != null) {
//			System.out.println("Protocolo: "+ nfseVO.getProtocolo());
//			System.out.println("Código Verificação: "+ nfseVO.getCodigoVerificacao());
//			System.out.println("NFS-e: "+ nfseVO.getNumeroNFSe());
//			if (nfseVO.getDataCancelamento() != null) {
//				System.out.println("Dt. Cancelamento: "+ new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(nfseVO.getDataCancelamento()));
//			}
//			System.out.println("Impressão:");
//			System.out.println(nfseVO.getUrlVisualizacao());
//		}
		return nfseVO;
	}
	
	public NFSeVO consultarNfseServicoPrestado(NFSeVO nfseVO, String url) throws Exception {
		String retorno = sendPost(nfseVO, url, "consultarNfseServicoPrestado");
		converterObjeto(nfseVO, retorno);
//		if(nfseVO != null) {
//			System.out.println("\nMensagens:");
//			for (String msg : nfseVO.getMensagens()) {
//				System.out.println(msg);
//			}
//		}
		return nfseVO;
	}
 
	private NFSeVO converterObjeto(NFSeVO nfseVO, String retorno) {
		Gson gson = new Gson();
		Type type = new TypeToken<NFSeVO>() {}.getType();
		nfseVO = gson.fromJson(retorno.toString(), type);
		return nfseVO;
	}

	private String sendPost(NFSeVO nfseVO, String url, String metodo) throws Exception {
 		URL obj = new URL(url + PREFIX + metodo);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
 		Gson gson = new Gson();
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(gson.toJson(nfseVO));
		wr.flush();
		wr.close();
 		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url + PREFIX + metodo);
		System.out.println("Post parameters : " + gson.toJson(nfseVO));
		System.out.println("Response Code : " + responseCode);
 		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String inputLine;
		StringBuffer response = new StringBuffer();
 		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 		return response.toString();
 	}

}
