package financeiro;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.ExcluirJsonStrategy;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.Usuario;
import negocio.facade.jdbc.financeiro.ContaReceber;
import webservice.servicos.objetos.IntegracaoSerasaApiGeoRSVO;

public class TesteArredondamento {
	
	@Before
    public void before() throws Exception {
		
    }
	
    @After
    public void after() throws Exception {
    	
    }
	
	
	public void teste() throws Exception {
		  /*String doubleVal = "0.059";
		  String doubleVal1 = "0.065";
		  BigDecimal bdTest = new BigDecimal(  doubleVal);
		  BigDecimal bdTest1 = new BigDecimal(  doubleVal1 );
		  bdTest = bdTest.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		  bdTest1 = bdTest1.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		  System.out.println("bdTest:"+bdTest); //1.75
		  System.out.println("bdTest1:"+bdTest1);//0.75, no problem*/
		/*Double db = new Double(32411.40);
		Double db1 = new Double(321.00);
		Double db2= new Double(321.01);
		Double db3= new Double(321.99);
		Double db4= new Double(321.45);
		
		Integer a = UteisTexto.converteStringParaInteiro(db.toString());
		Integer a1 = UteisTexto.converteStringParaInteiro(db1.toString());
		Integer a2 = UteisTexto.converteStringParaInteiro(db2.toString());
		Integer a3 = UteisTexto.converteStringParaInteiro(db3.toString());
		Integer a4 = UteisTexto.converteStringParaInteiro(db4.toString());
		
		System.out.println("bdTest1:"+a);
		System.out.println("bdTest1:"+a1);
		System.out.println("bdTest1:"+a2);
		System.out.println("bdTest1:"+a3);
		System.out.println("bdTest1:"+a4);*/
		System.out.println("bdTest1:"+Integer.parseInt("003530"));
	}
	

	public void testeDigitoVerificadorNossoNumero() {
		String nossoNumero = "200092538"; //nosso numero que esta no cliente
		String nossoNumeroUtilizado = "0092538"; //nosso numero que realmente foi utilizado para gerar o digito verificado
		System.out.println(Uteis.getModulo11Santander(nossoNumero.substring(0, nossoNumero.length()-1), 8));
		
	}
	
	
	public void testeDigitoVerificadorNossoNumeroSicredi() {
		ContaReceberVO obj = new ContaReceberVO();
		obj.getContaCorrenteVO().getAgencia().setNumeroAgencia("0710");
		obj.getContaCorrenteVO().setCodigoCedente("1701713");
		String nossoNumero = "20213652"; //nosso numero que esta no cliente
		try {
			ContaReceber contaReceber = new ContaReceber();
			System.out.println(contaReceber.getModulo11Sicred(nossoNumero, obj));	
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public void testeDataModelo() {
		try {
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			DataModelo listaDocumentosPendentes = new DataModelo();
			listaDocumentosPendentes.setLimitePorPagina(10);
			listaDocumentosPendentes.setOffset(0);
			listaDocumentosPendentes.setTotalRegistrosEncontrados(10);
			PessoaVO pessoa = new PessoaVO();
			pessoa.setCodigo(1);
			List lista = new ArrayList();
			lista.add(pessoa);
			listaDocumentosPendentes.setListaConsulta(lista);
			String json = gson.toJson(listaDocumentosPendentes);
			System.out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testeDataModelo1() {
		try {
			Uteis.checkState(!Uteis.validaCPF(Uteis.removerAcentos(Uteis.removeCaractersEspeciais("408167956")).replaceAll(" ", "")), "erro");
			System.out.println("certo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void testeSerasaSenha64() {
		try {
			String textoOriginal = "290919911";
			
			System.out.println("Texto original: " + textoOriginal);
			
			String textoSerializado = Base64.getEncoder().encodeToString(textoOriginal.getBytes());

		    System.out.println("Texto em Base64: " + textoSerializado);

		    String textoDeserializado = new String(Base64.getDecoder().decode(textoSerializado));

		    System.out.println("Texto deserializado: "+ textoDeserializado);
		    
		    
		    IntegracaoSerasaApiGeoRSVO isag = new IntegracaoSerasaApiGeoRSVO();
			isag.setSenha_api(Base64.getEncoder().encodeToString(textoOriginal.getBytes()));
			
			//Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(isag);
			System.out.println("Texto json: "+ json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testeretonrojson() {
		try {
			String textoOriginal = "{\"success\":true}{\"dados_comprador\":{\"codigo\":200,\"resposta\":\"Sucesso\",\"status\":\"Registro marcado para negativa\\u00e7\\u00e3o\",\"id_transacao\":\"VQXGZSBOZ1vCVWF7DuBYDkXeV5FGVENUVQNG#_192705#\",\"postback\":\"https:\\/\\/endlfb64urxc9.x.pipedream.net\\/\"}}";
			if(textoOriginal.contains("id_transacao")) {
				int tamanhoInicial = textoOriginal.indexOf("id_transacao");
				String id_transacao = textoOriginal.substring(tamanhoInicial, textoOriginal.length()-1);
				System.out.println(id_transacao.substring(id_transacao.indexOf(":")+2, id_transacao.indexOf(",")-1));
			}
			for (String bodyresposta : textoOriginal.split(",")) {
				if(bodyresposta.contains("id_transacao")) {
					int tamanhoInicial = bodyresposta.indexOf(":");
					System.out.println(bodyresposta.substring(tamanhoInicial+2, bodyresposta.length()-1));
					break;
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testeretonrojson1() {
		String textoOriginal = "{\"success\":true}{\"dados_comprador\":{\"codigo\":200,\"resposta\":\"Sucesso\",\"status\":\"Registro marcado para negativa\\u00e7\\u00e3o\",\"id_transacao\":\"VQXGZSBOZ1vCVWF7DuBYDkXeV5FGVENUVQNG#_192705#\",\"postback\":\"https:\\/\\/endlfb64urxc9.x.pipedream.net\\/\"}}";
		try {
			System.out.println("Texto original: " + textoOriginal);
			
			String textoSerializado = Base64.getEncoder().encodeToString(textoOriginal.getBytes());

		    System.out.println("Texto em Base64: " + textoSerializado);

		    String textoDeserializado = new String(Base64.getDecoder().decode(textoSerializado));

		    System.out.println("Texto deserializado: "+ textoDeserializado);
		    
		    
		    IntegracaoSerasaApiGeoRSVO isag = new IntegracaoSerasaApiGeoRSVO();
			isag.setSenha_api(Base64.getEncoder().encodeToString(textoOriginal.getBytes()));
			
			//Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(isag);
			System.out.println("Texto json: "+ json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testeretonrojson3() {
		try {
			String textoOriginal = "{\"success\":true}{\"dados_comprador\":{\"codigo\":200,\"resposta\":\"Sucesso\",\"status\":\"Registro marcado para negativa\\u00e7\\u00e3o\",\"id_transacao\":\"VQXGZSBOZ1vCVWF7DuBYDkXeV5FGVENUVQNG#_192705#\",\"postback\":\"https:\\/\\/endlfb64urxc9.x.pipedream.net\\/\"}}";
			if(textoOriginal.contains("id_transacao")) {
				int tamanhoInicial = textoOriginal.indexOf("id_transacao");
				String id_transacao = textoOriginal.substring(tamanhoInicial, textoOriginal.length()-1);
				System.out.println(id_transacao.substring(id_transacao.indexOf(":")+2, id_transacao.indexOf(",")-1));
			}
			for (String bodyresposta : textoOriginal.split(",")) {
				if(bodyresposta.contains("id_transacao")) {
					int tamanhoInicial = bodyresposta.indexOf(":");
					System.out.println(bodyresposta.substring(tamanhoInicial+2, bodyresposta.length()-1));
					break;
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
