package webservice.boletoonline.bancoBrasil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse.Status;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.boletoonline.bancoBrasil.comuns.BancoBrasilBoletoOnlineVO;
import webservice.boletoonline.bancoBrasil.comuns.BeneficiarioVO;
import webservice.boletoonline.bancoBrasil.comuns.DescontoVO;
import webservice.boletoonline.bancoBrasil.comuns.ErrorBbWS;
import webservice.boletoonline.bancoBrasil.comuns.JurosVO;
import webservice.boletoonline.bancoBrasil.comuns.MultaVO;
import webservice.boletoonline.bancoBrasil.comuns.PagadorVO;
import webservice.boletoonline.bancoBrasil.comuns.RegistroRetornoBoletoBb;
import webservice.boletoonline.bancoBrasil.comuns.TokenVO;

public class BancoBrasilBoletoOnline extends ControleAcesso {

	private static final long serialVersionUID = 6689805544901408697L;

	public ContaReceberVO contaReceberVO;
	public ControleRemessaContaReceberVO crcrVO;
	public ConfiguracaoGeralSistemaVO config;
	public ConfiguracaoFinanceiroVO configFin;
	public ErrorBbWS erro = null;
	static final String PATTERN ="dd.MM.yyyy";

	public BancoBrasilBoletoOnline(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO confiFin) {
		this.contaReceberVO = contareceberVO;
		this.crcrVO = crcrVO;
		this.config = config;
		this.configFin = confiFin;
	}

	public RegistroRetornoBoletoBb enviarBoletoRemessaOnlineBancoBrasil() throws Exception {
		TokenVO tokenVO  = autenticar() ;	
		if (Uteis.isAtributoPreenchido(tokenVO.getToken_type())) {
			BancoBrasilBoletoOnlineVO enviarBoletoRemessaOnlineBancoBrasil = montarDadosBoletoBancoBrasil();
			final String dadosEntradaJson = new Gson().toJson(enviarBoletoRemessaOnlineBancoBrasil);		
			return	enviar(tokenVO, dadosEntradaJson);
		}
		return new RegistroRetornoBoletoBb();
	}
	
	private TokenVO autenticar() throws Exception {		
		String url = null;		
		String basicAuthUserName = null;
		String basicAuthPassword = null ;
		TokenVO tokenVO   = null ;
		HttpResponse<String> response = null ;
		try { 
			url = getContaReceberVO().getContaCorrenteVO().getAmbienteContaCorrenteEnum().
					isHomologacao() ? "https://oauth.hm.bb.com.br/oauth/token" : "https://oauth.bb.com.br/oauth/token";		
			basicAuthUserName  = getContaReceberVO().getContaCorrenteVO().getTokenClienteRegistroRemessaOnline();	
			basicAuthPassword  = getContaReceberVO().getContaCorrenteVO().getTokenKeyRegistroRemessaOnline();
		      response = Unirest.post(url).
		    		 basicAuth(basicAuthUserName, basicAuthPassword).
		    		 header("Content-Type", "application/x-www-form-urlencoded").
					field("grant_type", "client_credentials").					
		            field("scope", "cobrancas.boletos-info cobrancas.boletos-requisicao").asString();			 	
				    if(!response.isSuccess()) {
				    	throw new Exception(response.getBody());			    	
				    }
		            tokenVO  = new Gson().fromJson(response.getBody(), TokenVO.class);	    
		
		} catch (Exception e) {
			erro = new Gson().fromJson(e.getMessage(), ErrorBbWS.class);
			tratarMensagemErroWebService(e.getMessage()  + ": "+response.getStatus());
		}finally {			
			url= null;	
			basicAuthUserName = null ;
			basicAuthPassword = null ;
			response = null ;
		}
		return tokenVO ;		
	}
  
 

	private RegistroRetornoBoletoBb  enviar(TokenVO tokenVO, String dadosEntradaJson) throws Exception {	
		   String url = null;		
		   HttpResponse<String> response = null ;
		   RegistroRetornoBoletoBb regRetorno = null;
			try {
				url = getContaReceberVO().getContaCorrenteVO().getAmbienteContaCorrenteEnum().
					   isHomologacao() ? "https://api.hm.bb.com.br/cobrancas/v2/boletos?" : "https://api.bb.com.br/cobrancas/v2/boletos?";    
				 response = Unirest.post(url).
				          header("Authorization", "Bearer "+ tokenVO.getAccess_token()).
				          header("Content-Type", "application/json").
				          queryString("gw-dev-app-key", getContaReceberVO().getContaCorrenteVO().getTokenIdRegistroRemessaOnline()).body(dadosEntradaJson).
				          asString();				    
			    if(!response.isSuccess()) {
			    	throw new Exception(response.getBody());			    	
			    } 
			    regRetorno = new Gson().fromJson(response.getBody(), RegistroRetornoBoletoBb.class);
			    regRetorno.setStatusCode(response.getStatus());
			  } catch (Exception e) {
					erro = new Gson().fromJson(e.getMessage(), ErrorBbWS.class);
					tratarMensagemErroWebService(e.getMessage()  + ": "+response.getStatus());
			}finally {
				url = null;
				response = null ;					
			}
			return regRetorno ;
	}
	
	
	
	private void tratarMensagemErroWebService(String mensagemRetorno) throws Exception {
		String msg = erro == null ? mensagemRetorno : erro.getMensagem() + " - " + erro.getMensagemCampos();
		if (mensagemRetorno.contains("400")) {
			throw new Exception("A requisição é inválida, em geral conteúdo malformado." + msg);
		}
		if (mensagemRetorno.contains("401")) {
			throw new Exception("O usuário e senha ou token de acesso são inválidos." + msg);
		}
		if (mensagemRetorno.contains("403")) {
			throw new Exception("O acesso à API está bloqueado ou o usuário está bloqueado. " + msg);
		}
		if (mensagemRetorno.contains("404")) {
			throw new Exception("O endereço acessado não existe. " + msg);
		}
		if (mensagemRetorno.contains("422")) {
			throw new Exception("A Requisição é válida, mas os dados passados não são válidos." + msg);
		}
		if (mensagemRetorno.contains("429")) {
			throw new Exception("O usuário atingiu o limite de requisições." + msg);
		}
		if (mensagemRetorno.contains("500")) {
			throw new Exception("Houve um erro interno do servidor ao processar a requisição.   Este erro pode ter sido causado por entrada mal formatada. Favor rever a sua entrada." + msg);
		}
		throw new Exception("Erro Não tratato Web Service Banco Brasil: " + msg);
	}

	
	private BancoBrasilBoletoOnlineVO montarDadosBoletoBancoBrasil() throws Exception {
		
		BancoBrasilBoletoOnlineVO boletoOnlineVO = new BancoBrasilBoletoOnlineVO();	
		
		boletoOnlineVO.setNumeroConvenio(getContaReceberVO().getContaCorrenteVO().getConvenio());
		boletoOnlineVO.setDataVencimento(Uteis.getData(getCrcrVO().getDataVencimento(), PATTERN));		
		boletoOnlineVO.setNumeroCarteira(getCrcrVO().getCarteira());	
		if(Uteis.isAtributoPreenchido(getContaReceberVO().getContaCorrenteVO().getVariacaoCarteira())) {		
			Integer valor = Uteis.getValorInteiro(getContaReceberVO().getContaCorrenteVO().getVariacaoCarteira());
			if(Uteis.isAtributoPreenchido(valor)) {
				boletoOnlineVO.setNumeroVariacaoCarteira(valor);
			}else {
				boletoOnlineVO.setNumeroVariacaoCarteira(35);
			}		
		}else {
		   boletoOnlineVO.setNumeroVariacaoCarteira(35);
		}
		boletoOnlineVO.setCodigoModalidade(1);
		boletoOnlineVO.setDataEmissao(Uteis.getData(new Date(), PATTERN));
		validarDadosParaAbatimento(boletoOnlineVO);	 
		boletoOnlineVO.setCodigoTipoTitulo("4");
		boletoOnlineVO.setDescricaoTipoTitulo("DS");
		boletoOnlineVO.setIndicadorPermissaoRecebimentoParcial("N");
		boletoOnlineVO.setNumeroTituloBeneficiario(getCrcrVO().getNossoNumero());	
		boletoOnlineVO.setNumeroTituloCliente(montarDadosNumeroTituloCliente(boletoOnlineVO));	
		montarDadosDesconto(boletoOnlineVO);
		boletoOnlineVO.setJurosMora(montarDadosJuros());
		boletoOnlineVO.setMulta(montarDadosMulta());		
		boletoOnlineVO.setPagador(montarDadosPagador());	
		boletoOnlineVO.setBeneficiarioFinal(montarDadosBeneficiario());
		boletoOnlineVO.setIndicadorPix("N");
		
		if(getContaReceberVO().getContaCorrenteVO().getPermiteRecebimentoBoletoVencidoRemessaOnline()) {
			boletoOnlineVO.setIndicadorAceiteTituloVencido("S");	
			boletoOnlineVO.setNumeroDiasLimiteRecebimento(getContaReceberVO().getContaCorrenteVO().getNumeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline());	
		}else {
			boletoOnlineVO.setIndicadorAceiteTituloVencido("N");	
			boletoOnlineVO.setNumeroDiasLimiteRecebimento(0);	
		}
		if (getContaReceberVO().getContaCorrenteVO().getHabilitarProtestoBoleto()) {
			boletoOnlineVO.setQuantidadeDiasProtesto(getContaReceberVO().getContaCorrenteVO().getQtdDiasProtestoBoleto());
			boletoOnlineVO.setCodigoAceite("N");
		} else {
			boletoOnlineVO.setCodigoAceite("A");
		}   
		if (getContaReceberVO().getContaCorrenteVO().getCarteiraRegistrada() && getContaReceberVO().getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
			boletoOnlineVO.setValorOriginal((Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorBaseComAcrescimo())));
		} else {
			boletoOnlineVO.setValorOriginal((Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorComAcrescimo())));
		}
		
		
		
		//boletoOnlineVO.setQuantidadeDiasNegativacao(0);
		//boletoOnlineVO.setOrgaoNegativador("");
		
		//boletoOnlineVO.setCampoUtilizacaoBeneficiario("");
		//boletoOnlineVO.setMensagemBloquetoOcorrencia("");
		return boletoOnlineVO;
	}

	private void validarDadosParaAbatimento(BancoBrasilBoletoOnlineVO boletoOnlineVO) {
		// ABATIMENTO
		if (getContaReceberVO().getContaCorrenteVO().getCarteiraRegistrada() && getContaReceberVO().getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
			// VALOR DO TITULO
			if (getCrcrVO().getValorBaseComAcrescimo() > 0 && getCrcrVO().getValorBaseComAcrescimo() > getCrcrVO().getValorComAcrescimo()) {
				Double valorDescFinal = getCrcrVO().getValorBaseComAcrescimo() - getCrcrVO().getValorComAcrescimo();
				boletoOnlineVO.setValorAbatimento((Uteis.arrendondarForcando2CadasDecimaisStr(valorDescFinal)));
			} else {
				if (getCrcrVO().getValorAbatimento() > 0) {
					boletoOnlineVO.setValorAbatimento((Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorAbatimento())));
				} else {
					boletoOnlineVO.setValorAbatimento("0");
				}
			}
		} else {
			if (getCrcrVO().getValorAbatimento() > 0) {
				boletoOnlineVO.setValorAbatimento((Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorAbatimento())) );
			} else {
				boletoOnlineVO.setValorAbatimento("0");
			}
		}
	}

	
	private void montarDadosDesconto(BancoBrasilBoletoOnlineVO boletoOnlineVO) throws Exception {
		
		if (getCrcrVO().getValorDescontoDataLimite() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto() == null || (getCrcrVO().getDataLimiteConcessaoDesconto() != null && UteisData.getCompareData(getCrcrVO().getDataLimiteConcessaoDesconto(), new Date()) >= 0))) {
			DescontoVO desconto = new DescontoVO();			
			desconto.setTipo(1);                                                                   
			desconto.setDataExpiracao(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto(), PATTERN));			
			desconto.setValor((Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite())));
			boletoOnlineVO.setDesconto(desconto);
		}
		if (getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null && UteisData.getCompareData(getCrcrVO().getDataLimiteConcessaoDesconto2(), new Date()) >= 0))) {
			DescontoVO segundoDesconto = new DescontoVO();			
			//segundoDesconto.setTipo(1);       
			segundoDesconto.setDataExpiracao(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto2(), PATTERN));
			segundoDesconto.setValor(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite2())), 17));
			boletoOnlineVO.setSegundoDesconto(segundoDesconto);
		}
		if (getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null && UteisData.getCompareData(getCrcrVO().getDataLimiteConcessaoDesconto3(), new Date()) >= 0))) {
			DescontoVO terceiroDesconto = new DescontoVO();			
			//terceiroDesconto.setTipo(1);       
			terceiroDesconto.setDataExpiracao(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3(), PATTERN));
			terceiroDesconto.setValor(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite3())), 17));
			boletoOnlineVO.setTerceiroDesconto(terceiroDesconto);
		}		
	}

	private MultaVO montarDadosMulta() {
		MultaVO multaVO = new MultaVO();
		multaVO.setTipo(2);
		multaVO.setData(Uteis.getData(Uteis.obterDataAvancada(getCrcrVO().getDataVencimento(), 1),PATTERN));	
		multaVO.setValor("");    	
		multaVO.setPorcentagem(Uteis.arrendondarForcando2CadasDecimaisStr(getConfigFin().getPercentualMultaPadrao()));// 2% de multa ao mes		
		return multaVO;
	}

	private JurosVO montarDadosJuros() {
		JurosVO jurosVO = new JurosVO();
		//Para boletos com valores menores que 30 reias nao pode ser cobrado juros pois o mesmo gerar o valor menor que 0,01 ao dia sendo assim o banco nao aceita.
		if (getConfigFin().getUtilizarIntegracaoFinanceira() || getCrcrVO().getValorComAcrescimo() < 30.00) {
			jurosVO.setTipo(3);
			jurosVO.setValor("");
			jurosVO.setPorcentagem("");
			
		} else {
			jurosVO.setTipo(2);
			jurosVO.setValor("");
			jurosVO.setPorcentagem(Uteis.arrendondarForcando2CadasDecimaisStr(getConfigFin().getPercentualJuroPadrao()));
			//jurosVO.setPorcentagem("1.00");					
		}
		return jurosVO;
	}

	private PagadorVO montarDadosPagador() {
		PagadorVO pagadorVO = new PagadorVO();
		Uteis.checkState((getCrcrVO().getCodigoInscricao().equals(0) || getCrcrVO().getCodigoInscricao().equals(1)) && !Uteis.verificaCPF(getCrcrVO().getNumeroInscricao().toString()), "O cpf informado para a pessoa " + getCrcrVO().getNomeSacado() + " não é um cpf valido. Por favor verificar o Cadastro.");
		Uteis.checkState(getCrcrVO().getCodigoInscricao().equals(2) && !Uteis.validaCNPJ(getCrcrVO().getNumeroInscricao().toString()), "O CNPJ informado para a pessoa " + getCrcrVO().getNomeSacado() + " não é um cnpj valido. Por favor verificar o Cadastro.");
		pagadorVO.setTipoInscricao(getCrcrVO().getCodigoInscricao());
		pagadorVO.setNumeroInscricao(Uteis.removerMascara(getCrcrVO().getNumeroInscricao()));
		pagadorVO.setNome(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getNomeSacado())), 30));
		pagadorVO.setEndereco(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getLogradouro())), 30));
		pagadorVO.setCep(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerMascara(getCrcrVO().getCep()), 8));
		pagadorVO.setCidade(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getCidade())), 30));
		pagadorVO.setBairro(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getBairro())), 30));
		pagadorVO.setUf(Uteis.copiarDelimitandoTamanhoDoTexto(getCrcrVO().getEstado(), 2));		
        pagadorVO.setTelefone(Uteis.removerMascara(Uteis.retirarMascaraTelefone(getCrcrVO().getTelefoneSacado())));
		return pagadorVO;
	}

	private BeneficiarioVO montarDadosBeneficiario() {
		BeneficiarioVO beneficiarioVO = new BeneficiarioVO();
		beneficiarioVO.setTipoInscricao(2);
		beneficiarioVO.setNumeroInscricao(Uteis.removerMascara(getCrcrVO().getCnpj()));
		beneficiarioVO.setNome(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getUnidadeEnsino().getNome())),30));		
		return beneficiarioVO;
	}
	
	
	private String  montarDadosNumeroTituloCliente(BancoBrasilBoletoOnlineVO boletoOnlineVO) {		
		StringBuilder sb = new StringBuilder();
		try {			
			sb.append("000");
			sb.append(getContaReceberVO().getContaCorrenteVO().getConvenio());			
			sb.append(Uteis.preencherComZerosPosicoesVagas(getCrcrVO().getNossoNumero(),10));			
			return sb.toString();			
		} finally {			
			sb = null;
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
