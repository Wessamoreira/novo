package webservice.boletoonline.sicredi;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.boletoonline.sicredi.comuns.ErrorSicrediWS;
import webservice.boletoonline.sicredi.comuns.ResponseSicrediVO;
import webservice.boletoonline.sicredi.comuns.SicrediBoletoOnlineVO;

public class SicrediBoletoOnline  extends ControleAcesso {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1148150391320790405L;
	  
	
	public ContaReceberVO contaReceberVO;
	public ControleRemessaContaReceberVO crcrVO;
	public ConfiguracaoGeralSistemaVO config;
	public ConfiguracaoFinanceiroVO configFin;
	public ErrorSicrediWS erro = null;
	private static final String PATTERN =  "dd/MM/yyyy";
	

	public SicrediBoletoOnline(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO confiFin) {
		this.contaReceberVO = contareceberVO;
		this.crcrVO = crcrVO;
		this.config = config;
		this.configFin = confiFin;
	}

	public void enviarBoletoRemessaOnlineSicredi(UsuarioVO usuarioVO) throws Exception {
		//foi criado este recurso para recuperar os dados atualizados da conta corrente pois a quando o sistema sei sobe  os dados da conta corrente ficam gravados  em memoria ,
		// com isso ao alterar os dados em na base de dados  e ao recarregar a conta corrente novamente  ele ira buscar  os dados da conta corrente q existe em memoria  que neste caso estara desatualizados  ;
	if(Uteis.isAtributoPreenchido(getContaReceberVO().getContaCorrenteVO().getChaveAutenticacaoClienteRegistroRemessaOnline())) {				 
			
			ContaCorrenteVO contaatualizada  = getFacadeFactory().getContaCorrenteFacade().consultarChaveTransacaoClienteRemessaOnlineAndDataExpiracaoPorChavePrimariaUnica(getContaReceberVO().getContaCorrenteVO().getCodigo(),   usuarioVO);
			getContaReceberVO().getContaCorrenteVO().setDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline(contaatualizada.getDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline());
			getContaReceberVO().getContaCorrenteVO().setChaveTransacaoClienteRegistroRemessaOnline(contaatualizada.getChaveTransacaoClienteRegistroRemessaOnline());
			if(UteisData.getDataComMinutos(getContaReceberVO().getContaCorrenteVO().getDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline()).compareTo(new Date()) > 0) {		
				if (Uteis.isAtributoPreenchido(getContaReceberVO().getContaCorrenteVO().getChaveTransacaoClienteRegistroRemessaOnline())) {
					verificaConexao();
						SicrediBoletoOnlineVO enviarBoletoRemessaOnlineSicredi = montarDadosBoletoSicredi();
						final String dadosEntradaJson = new Gson().toJson(enviarBoletoRemessaOnlineSicredi);
						enviar(dadosEntradaJson);
					
					
				}else {
					throw new Exception("A requisição é inválida, chave de transação invalida.");
	
				}
			}else {
				    verificaConexao();			  
				    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").create();			    
					ResponseSicrediVO responseSicredi = gson.fromJson(autenticar(), ResponseSicrediVO.class);				
					getContaReceberVO().getContaCorrenteVO().setChaveTransacaoClienteRegistroRemessaOnline(responseSicredi.getChaveTransacao());
					getContaReceberVO().getContaCorrenteVO().setDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline(responseSicredi.getDataExpiracao());
					getFacadeFactory().getContaCorrenteFacade().alterarChaveTransacaoRemessaOnlineContaCorrente(
			                                getContaReceberVO().getContaCorrenteVO().getCodigo(),
	                                        getContaReceberVO().getContaCorrenteVO().getChaveTransacaoClienteRegistroRemessaOnline(), 
	                                        getContaReceberVO().getContaCorrenteVO().getDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline(),
							                                                                                    usuarioVO);
					if (Uteis.isAtributoPreenchido(getContaReceberVO().getContaCorrenteVO().getChaveTransacaoClienteRegistroRemessaOnline())) {
						SicrediBoletoOnlineVO enviarBoletoRemessaOnlineSicredi = montarDadosBoletoSicredi();
						final String dadosEntradaJson = new Gson().toJson(enviarBoletoRemessaOnlineSicredi);
						enviar(dadosEntradaJson);
					}
				
			}
		
		 }else {
			 throw new Exception("A requisição é inválida, chave de autenticação invalida.");
		 }
		
	}
	
	
	
	public  void verificaConexao() throws Exception {
		try {
			URL url = new URL(
					"https://cobrancaonline.sicredi.com.br/sicredi-cobranca-ws-ecomm-api/ecomm/v1/boleto/health");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int status  = con.getResponseCode();
			con.disconnect();
			if(status != 200 ) {				
			  tratarMensagemErroWebService(String.valueOf(status));
			}
			
		} catch (MalformedURLException ex) {			
			tratarMensagemErroWebService(ex.getMessage());			
		} catch (IOException ex) {
			tratarMensagemErroWebService(ex.getMessage());
		}

		
	}
	
	public String  autenticar() throws Exception {
		String url = "https://cobrancaonline.sicredi.com.br/sicredi-cobranca-ws-ecomm-api/ecomm/v1/boleto/autenticacao";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<String> response = null ;
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);		
			headers.add("token",getContaReceberVO().getContaCorrenteVO().getChaveAutenticacaoClienteRegistroRemessaOnline());			
			HttpEntity<String> request = new HttpEntity<String>( headers);
			response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				erro = new Gson().fromJson(response.getBody(), ErrorSicrediWS.class);
				tratarMensagemErroWebService(response.getStatusCode().toString());
			}
		
		} catch (HttpClientErrorException hcee) {
			erro = new Gson().fromJson(hcee.getResponseBodyAsString(), ErrorSicrediWS.class);
			tratarMensagemErroWebService(hcee.getMessage());
		} catch (Exception e) {
			tratarMensagemErroWebService(e.getMessage());
		}finally {
			url = null;
			restTemplate = null;
			headers= null;
		}
		return response.getBody();
	}
	
	
	


	private void enviar( String dadosEntradaJson) throws Exception {
		String url = "https://cobrancaonline.sicredi.com.br/sicredi-cobranca-ws-ecomm-api/ecomm/v1/boleto/emissao";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);		
			headers.add("token", getContaReceberVO().getContaCorrenteVO().getChaveTransacaoClienteRegistroRemessaOnline());			
			HttpEntity<String> request = new HttpEntity<String>(dadosEntradaJson, headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			if (!response.getStatusCode().equals(HttpStatus.CREATED)) {
				erro = new Gson().fromJson(response.getBody(), ErrorSicrediWS.class);
				tratarMensagemErroWebService(response.getStatusCode().toString());
			}else {
				ResponseSicrediVO responseSicredi =  new Gson().fromJson(response.getBody(), ResponseSicrediVO.class);
			}
		} catch (HttpClientErrorException hcee) {
			erro = new Gson().fromJson(hcee.getResponseBodyAsString(), ErrorSicrediWS.class);
			tratarMensagemErroWebService(hcee.getMessage());
		} catch (Exception e) {
			tratarMensagemErroWebService(e.getMessage());
		}finally {
			url = null;
			restTemplate = null;
			headers= null;
		}
	}
	
	
	public List<ResponseSicrediVO>  consultaBoletosRegistrados(Date periodoInicial , Date periodoFinal,Boolean consultarPeloNossoNumero) throws Exception {	
		
		 String url = "https://cobrancaonline.sicredi.com.br/sicredi-cobranca-ws-ecomm-api/ecomm/v1/boleto/consulta?";
		 String agencia = getContaReceberVO().getContaCorrenteVO().getAgencia().getNumeroAgencia();   
		 String digitoAgencia = getContaReceberVO().getContaCorrenteVO().getAgencia().getDigito();
		 String cedente =  getContaReceberVO().getContaCorrenteVO().getConvenio();
		 String nossoNumero = getCrcrVO().getNossoNumero() ;
		 if(consultarPeloNossoNumero) {
		     url = url +  "agencia="+agencia +"&posto="+digitoAgencia+"&cedente="+cedente+ "&nossoNumero="+nossoNumero+"";		 
 
		 }else {
			 
			 url = url +  "agencia="+agencia +"&cedente="+cedente+ "&posto="+digitoAgencia+"&dataInicio="+Uteis.getData(periodoInicial, "dd/MM/yyyy")+"&dataFim="+Uteis.getData(periodoFinal, "dd/MM/yyyy")+"&tipoData=DATA_EMISSAO";		 
		 }
		 
		
	   
		 RestTemplate restTemplate = new RestTemplate();
		 HttpHeaders headers = new HttpHeaders();
		 ResponseEntity<String> response = null ;
		try {
			
			headers.setContentType(MediaType.APPLICATION_JSON);		
			headers.add("token", getContaReceberVO().getContaCorrenteVO().getChaveTransacaoClienteRegistroRemessaOnline());			
			HttpEntity<String> request = new HttpEntity<String>( headers);
			response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				erro = new Gson().fromJson(response.getBody(), ErrorSicrediWS.class);
				tratarMensagemErroWebService(response.getStatusCode().toString());
			}
		
		} catch (HttpClientErrorException hcee) {
			erro = new Gson().fromJson(hcee.getResponseBodyAsString(), ErrorSicrediWS.class);
			tratarMensagemErroWebService(hcee.getMessage());
		} catch (Exception e) {
			tratarMensagemErroWebService(e.getMessage());
		}finally {
			url = null;
			restTemplate = null;
			headers= null;
		}		
		Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").create();	
		if(consultarPeloNossoNumero) {
			List<ResponseSicrediVO> lista = new ArrayList<ResponseSicrediVO>(0);
			lista.add(gson.fromJson(response.getBody(), ResponseSicrediVO.class));
			return lista;
		}else {
		  return gson.fromJson(response.getBody(),new TypeToken<List<ResponseSicrediVO>>() {}.getType()); 
		}
	}
		
		
		
		
		
		
		
		
		
	

	
	private SicrediBoletoOnlineVO montarDadosBoletoSicredi() throws Exception {
		
		SicrediBoletoOnlineVO boletoOnlineVO = new SicrediBoletoOnlineVO();
		
		boletoOnlineVO.setAgencia(getContaReceberVO().getContaCorrenteVO().getAgencia().getNumeroAgencia());
		boletoOnlineVO.setPosto(!getContaReceberVO().getContaCorrenteVO().getAgencia().getDigito().equals("") ? getContaReceberVO().getContaCorrenteVO().getAgencia().getDigito() : "74" );
		boletoOnlineVO.setCedente(getContaReceberVO().getContaCorrenteVO().getConvenio());
		boletoOnlineVO.setTipoPessoa(String.valueOf(getCrcrVO().getCodigoInscricao()));
		boletoOnlineVO.setCpfCnpj(Uteis.removerMascara(getCrcrVO().getNumeroInscricao()));
		boletoOnlineVO.setNome(Uteis.removerAcentuacao(getCrcrVO().getNomeSacado()));
		boletoOnlineVO.setCep(Uteis.removerMascara(getCrcrVO().getCep()));		
		boletoOnlineVO.setEspecieDocumento("K");		
		boletoOnlineVO.setSeuNumero(getCrcrVO().getNossoNumero());
		boletoOnlineVO.setDataVencimento(Uteis.getData(getCrcrVO().getDataVencimento(), PATTERN));
		boletoOnlineVO.setValor(Uteis.arrendondarForcando2CadasDecimais(getCrcrVO().getValorComAcrescimo()));
		boletoOnlineVO.setNossoNumero(getCrcrVO().getNossoNumero());	
		boletoOnlineVO.setTipoDesconto("A");
		
		if (getCrcrVO().getValorDescontoDataLimite() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto() == null ||
				(getCrcrVO().getDataLimiteConcessaoDesconto() != null && UteisData.getCompareData(getCrcrVO().getDataLimiteConcessaoDesconto(), new Date()) >= 0))) {
			
			boletoOnlineVO.setDataDesconto1(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto(), PATTERN));
			
			boletoOnlineVO.setValorDesconto1(Uteis.arrendondarForcando2CadasDecimais(new Double(Uteis.truncar(getCrcrVO().getValorDescontoDataLimite(), 2))));			
			
		} else {
			if (getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || 
					(getCrcrVO().getDataLimiteConcessaoDesconto2() != null && UteisData.getCompareData(getCrcrVO().getDataLimiteConcessaoDesconto2(), new Date()) >= 0))) {
				
				boletoOnlineVO.setDataDesconto2(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto2(), PATTERN));
				boletoOnlineVO.setValorDesconto2(Uteis.arrendondarForcando2CadasDecimais(new Double(Uteis.truncar(getCrcrVO().getValorDescontoDataLimite2(), 2))));		
				
		
			} else if (getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null ||
					(getCrcrVO().getDataLimiteConcessaoDesconto3() != null && UteisData.getCompareData(getCrcrVO().getDataLimiteConcessaoDesconto3(), new Date()) >= 0))) {
				
				
				boletoOnlineVO.setDataDesconto3(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3(), PATTERN));
				boletoOnlineVO.setValorDesconto3(Uteis.arrendondarForcando2CadasDecimais(new Double(Uteis.truncar(getCrcrVO().getValorDescontoDataLimite2(), 2))));		
				
			} 
		}
		
		boletoOnlineVO.setTipoJuros("B");
		
		Double juro = 0.01;
		
		Double valorTitulo = Uteis.arrendondarForcando4CadasDecimais(getConfigFin().getCobrarJuroMultaSobreValorCheioConta()	? getCrcrVO().getValorComAcrescimo()	: getCrcrVO().getValor() - getCrcrVO().getValorAbatimento());
		
		Double juroFinal = (valorTitulo * juro) / 30;
		if(juroFinal < 0.01) {
			juroFinal = 0.01;
		}
		String juroStr = "";
		if (juroFinal.toString().length() > 5) {
			juroStr = (juroFinal.toString()).substring(0, 5);
		} else {
			juroStr = juroFinal.toString();
		}
		Double juroDouble = new Double(juroStr);
		if (juroDouble > 0) {		
		boletoOnlineVO.setJuros(1);
		}else {
			boletoOnlineVO.setJuros(0);
		}
		
		boletoOnlineVO.setEndereco(Uteis.removerAcentuacao(getCrcrVO().getLogradouro()));
		boletoOnlineVO.setCidade(Uteis.removerAcentuacao(getCrcrVO().getCidade()));
		boletoOnlineVO.setUf(getCrcrVO().getEstado());
		boletoOnlineVO.setTelefone(Uteis.removeCaractersEspeciais(Uteis.removerMascara(getCrcrVO().getTelefoneSacado().replaceAll("\\s+",""))));
	  
		return boletoOnlineVO;
	}

	

	private void tratarMensagemErroWebService(String mensagemRetorno) throws Exception {		
		if(Uteis.isAtributoPreenchido(erro.getCodigo())) {			
			throw new Exception("Codigo Erro : " + erro.getCodigo() +"  "+erro.getMensagem() +" "+ erro.getParametro() );
		}else {
			throw new Exception(mensagemRetorno);
			
		}
		
	}
	
	

	
	

	
	

	


	public ContaReceberVO getContaReceberVO() {
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}

	public ControleRemessaContaReceberVO getCrcrVO() {
		return crcrVO;
	}

	public void setCrcrVO(ControleRemessaContaReceberVO crcrVO) {
		this.crcrVO = crcrVO;
	}

	public ConfiguracaoGeralSistemaVO getConfig() {
		return config;
	}

	public void setConfig(ConfiguracaoGeralSistemaVO config) {
		this.config = config;
	}

	public ConfiguracaoFinanceiroVO getConfigFin() {
		return configFin;
	}

	public void setConfigFin(ConfiguracaoFinanceiroVO configFin) {
		this.configFin = configFin;
	}
	
	
	
}
