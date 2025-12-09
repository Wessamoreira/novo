package webservice.boletoonline.itau;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.boleto.GeradorDeDigitoItau;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.boletoonline.itau.comuns.BeneficiarioVO;
import webservice.boletoonline.itau.comuns.ErrorItauWS;
import webservice.boletoonline.itau.comuns.GrupoDescontoVO;
import webservice.boletoonline.itau.comuns.ItauBoletoOnlineVO;
import webservice.boletoonline.itau.comuns.JurosVO;
import webservice.boletoonline.itau.comuns.MoedaVO;
import webservice.boletoonline.itau.comuns.MultaVO;
import webservice.boletoonline.itau.comuns.PagadorVO;
import webservice.boletoonline.itau.comuns.RecebimentoDivergenteVO;
import webservice.boletoonline.itau.comuns.TipoCobranca;
import webservice.boletoonline.itau.comuns.TipoRegistro;
import webservice.boletoonline.itau.comuns.TokenVO;

public class ItauBoletoOnline extends ControleAcesso {

	private static final long serialVersionUID = 6689805544901408697L;

	public ContaReceberVO contaReceberVO;
	public ControleRemessaContaReceberVO crcrVO;
	public ConfiguracaoGeralSistemaVO config;
	public ConfiguracaoFinanceiroVO configFin;
	public ErrorItauWS erro = null;

	public ItauBoletoOnline(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO confiFin) {
		this.contaReceberVO = contareceberVO;
		this.crcrVO = crcrVO;
		this.config = config;
		this.configFin = confiFin;
	}

	public void enviarBoletoRemessaOnlineItau() throws Exception {
		TokenVO tokenVO = new Gson().fromJson(autenticar(), TokenVO.class);
		if (Uteis.isAtributoPreenchido(tokenVO.getToken_type())) {
			ItauBoletoOnlineVO enviarBoletoRemessaOnlineItau = montarDadosBoletoItau();
			final String dadosEntradaJson = new Gson().toJson(enviarBoletoRemessaOnlineItau);
			enviar(tokenVO, dadosEntradaJson);
		}
	}
	
	private String autenticar() throws Exception {
		RestTemplate restTemplate1 = new RestTemplate();
		ResponseEntity<String> response = null;
		RequestEntity<String> request = null;
		StringBuilder sb = new StringBuilder();
		String url = null;
		try {
			//url = getContaReceberVO().getContaCorrenteVO().getAmbienteContaCorrenteEnum().isHomologacao() ? "https://oauth.itau.com.br/identity/connect/token" : "https://autorizador-boletos.itau.com.br";
			url = "https://oauth.itau.com.br/identity/connect/token";
			sb.append("scope=readonly");
			sb.append("&client_id=").append(getContaReceberVO().getContaCorrenteVO().getTokenClienteRegistroRemessaOnline());
			sb.append("&client_secret=").append(getContaReceberVO().getContaCorrenteVO().getTokenKeyRegistroRemessaOnline());
			sb.append("&grant_type=client_credentials");
			request = RequestEntity.post(new URI(url)).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(sb.toString());
			response = restTemplate1.exchange(request, String.class);	
		} catch (HttpClientErrorException hcee) {
			erro = new Gson().fromJson(hcee.getResponseBodyAsString(), ErrorItauWS.class);
			tratarMensagemErroWebService(hcee.getMessage());
		} catch (Exception e) {
			tratarMensagemErroWebService(e.getMessage());
		}finally {
			sb = null;
			url= null;
			restTemplate1 = null;
			request= null;
		}
		return response.getBody();
	}


	private void enviar(TokenVO tokenVO, String dadosEntradaJson) throws Exception {
		String url = "https://gerador-boletos.itau.com.br/router-gateway-app/public/codigo_barras/registro";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Accept", "application/vnd.itau");
			headers.add("access_token", tokenVO.getAccess_token());
			headers.add("itau-chave", getContaReceberVO().getContaCorrenteVO().getTokenIdRegistroRemessaOnline());
			headers.add("identificador", Uteis.preencherComZerosPosicoesVagas(Uteis.removerMascara(getCrcrVO().getCnpj()), 14));
			HttpEntity<String> request = new HttpEntity<String>(dadosEntradaJson, headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
			if (!response.getStatusCode().equals(HttpStatus.OK)) {
				erro = new Gson().fromJson(response.getBody(), ErrorItauWS.class);
				tratarMensagemErroWebService(response.getStatusCode().toString());
			}
		} catch (HttpClientErrorException hcee) {
			erro = new Gson().fromJson(hcee.getResponseBodyAsString(), ErrorItauWS.class);
			tratarMensagemErroWebService(hcee.getMessage());
		} catch (Exception e) {
			tratarMensagemErroWebService(e.getMessage());
		}finally {
			url = null;
			restTemplate = null;
			headers= null;
		}
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
		throw new Exception("Erro Não tratato Web Service Itau: " + msg);
	}

	
	private ItauBoletoOnlineVO montarDadosBoletoItau() throws Exception {
		ItauBoletoOnlineVO boletoOnlineVO = new ItauBoletoOnlineVO();
		boletoOnlineVO.setTipo_ambiente(getContaReceberVO().getContaCorrenteVO().getAmbienteContaCorrenteEnum().isHomologacao() ? 1 : 2);
		boletoOnlineVO.setTipo_registro(TipoRegistro.REGISTRO.getCodigo());
		boletoOnlineVO.setTipo_cobranca(TipoCobranca.BOLETO.getCodigo());
		boletoOnlineVO.setTipo_produto("00006");
		boletoOnlineVO.setSubproduto("00008");
		boletoOnlineVO.setIdentificador_titulo_empresa("");
		boletoOnlineVO.setUso_banco("");
		boletoOnlineVO.setTipo_carteira_titulo(Integer.parseInt(getCrcrVO().getCarteira()));
		boletoOnlineVO.setData_vencimento(Uteis.getData(getCrcrVO().getDataVencimento(), "yyyy-MM-dd"));
		if (getContaReceberVO().getContaCorrenteVO().getCarteiraRegistrada() && getContaReceberVO().getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
			boletoOnlineVO.setValor_cobrado(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorBaseComAcrescimo())), 13));
		} else {
			boletoOnlineVO.setValor_cobrado(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorComAcrescimo())), 13));
		}
		if (getContaReceberVO().getContaCorrenteVO().getHabilitarProtestoBoleto()) {
			boletoOnlineVO.setTitulo_aceite("N");
		} else {
			boletoOnlineVO.setTitulo_aceite("S");
		}
		boletoOnlineVO.setEspecie("01");
		boletoOnlineVO.setData_emissao(Uteis.getData(getCrcrVO().getDataEmissao(), "yyyy-MM-dd"));
		//boletoOnlineVO.setData_limite_pagamento(Uteis.getData(getCrcrVO().getDataVencimento(), "yyyy-MM-dd"));
		boletoOnlineVO.setTipo_pagamento(3);
		boletoOnlineVO.setIndicador_pagamento_parcial(false);
		boletoOnlineVO.setQuantidade_pagamento_parcial(0);
		boletoOnlineVO.setQuantidade_parcelas(0);

		// boletoOnlineVO.setInstrucao_cobranca_1("");
		// boletoOnlineVO.setQuantidade_dias_1("");
		// boletoOnlineVO.setData_instrucao_1("");
		//
		// boletoOnlineVO.setInstrucao_cobranca_2("");
		// boletoOnlineVO.setQuantidade_dias_2("");
		// boletoOnlineVO.setData_instrucao_2("");
		//
		// boletoOnlineVO.setInstrucao_cobranca_3("");
		// boletoOnlineVO.setQuantidade_dias_3("");
		// boletoOnlineVO.setData_instrucao_3("");
		boletoOnlineVO.setBeneficiario(montarDadosBeneficiario());
		boletoOnlineVO.setPagador(montarDadosPagador());
		boletoOnlineVO.setRecebimento_divergente(montarDadosRecebimentoDivergente());
		boletoOnlineVO.setMoeda(new MoedaVO("09", ""));
		boletoOnlineVO.setJuros(montarDadosJuros());
		boletoOnlineVO.setMulta(montarDadosMulta());
		boletoOnlineVO.setGrupo_desconto(montarDadosGrupoDesconto());
		montarDadosNossoNumero(boletoOnlineVO);
		validarDadosParaAbatimento(boletoOnlineVO);
		return boletoOnlineVO;
	}

	private void validarDadosParaAbatimento(ItauBoletoOnlineVO boletoOnlineVO) {
		// ABATIMENTO
		if (getContaReceberVO().getContaCorrenteVO().getCarteiraRegistrada() && getContaReceberVO().getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
			// VALOR DO TITULO
			if (getCrcrVO().getValorBaseComAcrescimo() > 0 && getCrcrVO().getValorBaseComAcrescimo() > getCrcrVO().getValorComAcrescimo()) {
				Double valorDescFinal = getCrcrVO().getValorBaseComAcrescimo() - getCrcrVO().getValorComAcrescimo();
				boletoOnlineVO.setValor_abatimento(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(valorDescFinal)), 17));
			} else {
				if (getCrcrVO().getValorAbatimento() > 0) {
					boletoOnlineVO.setValor_abatimento(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorAbatimento())), 17));
				} else {
					boletoOnlineVO.setValor_abatimento("0");
				}
			}
		} else {
			if (getCrcrVO().getValorAbatimento() > 0) {
				boletoOnlineVO.setValor_abatimento(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorAbatimento())), 17));
			} else {
				boletoOnlineVO.setValor_abatimento("0");
			}
		}
	}

	private void montarDadosNossoNumero(ItauBoletoOnlineVO boletoOnlineVO) {
		GeradorDeDigitoItau gd = new GeradorDeDigitoItau();
		StringBuilder sb = new StringBuilder();
		try {
			boletoOnlineVO.setCodigo_barras(getContaReceberVO().getCodigoBarra());
			boletoOnlineVO.setNosso_numero(getCrcrVO().getNossoNumero());
			boletoOnlineVO.setSeu_numero(getCrcrVO().getNossoNumero());
			sb.append(boletoOnlineVO.getBeneficiario().getAgencia_beneficiario());
			sb.append(boletoOnlineVO.getBeneficiario().getConta_beneficiario());
			sb.append(boletoOnlineVO.getTipo_carteira_titulo());
			sb.append(boletoOnlineVO.getNosso_numero());
			boletoOnlineVO.setDigito_verificador_nosso_numero(gd.calculaDVNossoNumero(sb.toString()));
		} finally {
			gd = null;
			sb = null;
		}
	}

	private List<GrupoDescontoVO> montarDadosGrupoDesconto() throws Exception {
		List<GrupoDescontoVO> lista = new ArrayList<>();
		GrupoDescontoVO grupoDescontoVO = null;
		if (getCrcrVO().getValorDescontoDataLimite() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto() == null || (getCrcrVO().getDataLimiteConcessaoDesconto() != null && UteisData.getCompareData(getCrcrVO().getDataLimiteConcessaoDesconto(), new Date()) >= 0))) {
			grupoDescontoVO = new GrupoDescontoVO();
			grupoDescontoVO.setTipo_desconto(1);
			grupoDescontoVO.setData_desconto(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto(), "yyyy-MM-dd"));
			grupoDescontoVO.setValor_desconto(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite())), 17));
			lista.add(grupoDescontoVO);
		}
		if (getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null && UteisData.getCompareData(getCrcrVO().getDataLimiteConcessaoDesconto2(), new Date()) >= 0))) {
			grupoDescontoVO = new GrupoDescontoVO();
			grupoDescontoVO.setTipo_desconto(1);
			grupoDescontoVO.setData_desconto(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto2(), "yyyy-MM-dd"));
			grupoDescontoVO.setValor_desconto(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite2())), 17));
			lista.add(grupoDescontoVO);
		}
		if (getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null && UteisData.getCompareData(getCrcrVO().getDataLimiteConcessaoDesconto3(), new Date()) >= 0))) {
			grupoDescontoVO = new GrupoDescontoVO();
			grupoDescontoVO.setTipo_desconto(1);
			grupoDescontoVO.setData_desconto(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3(), "yyyy-MM-dd"));
			grupoDescontoVO.setValor_desconto(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite3())), 17));
			lista.add(grupoDescontoVO);
		}
		if (lista.isEmpty()) {
			grupoDescontoVO = new GrupoDescontoVO();
			grupoDescontoVO.setTipo_desconto(0);
			lista.add(grupoDescontoVO);
		}
		return lista;
	}

	private MultaVO montarDadosMulta() {
		MultaVO multaVO = new MultaVO();
		multaVO.setTipo_multa(2);
		multaVO.setData_multa("");
		multaVO.setValor_multa("");
		multaVO.setPercentual_multa("000000200000");// 2% de multa ao mes
		return multaVO;
	}

	private JurosVO montarDadosJuros() {
		JurosVO jurosVO = new JurosVO();
		//Para boletos com valores menores que 30 reias nao pode ser cobrado juros pois o mesmo gerar o valor menor que 0,01 ao dia sendo assim o banco nao aceita.
		if (configFin.getUtilizarIntegracaoFinanceira() || getCrcrVO().getValorComAcrescimo() < 30.00) {
			jurosVO.setTipo_juros(5);
			jurosVO.setData_juros("");
			jurosVO.setValor_juros("");
			jurosVO.setPercentual_juros("");
		} else {
			jurosVO.setTipo_juros(3);
			jurosVO.setData_juros("");
			jurosVO.setValor_juros("");
			jurosVO.setPercentual_juros("000000100000");// 1% de juro ao mes
		}

		return jurosVO;
	}

	private PagadorVO montarDadosPagador() {
		PagadorVO pagadorVO = new PagadorVO();
		Uteis.checkState((getCrcrVO().getCodigoInscricao().equals(0) || getCrcrVO().getCodigoInscricao().equals(1)) && !Uteis.verificaCPF(getCrcrVO().getNumeroInscricao().toString()), "O cpf informado para a pessoa " + getCrcrVO().getNomeSacado() + " não é um cpf valido. Por favor verificar o Cadastro.");
		Uteis.checkState(getCrcrVO().getCodigoInscricao().equals(2) && !Uteis.validaCNPJ(getCrcrVO().getNumeroInscricao().toString()), "O CNPJ informado para a pessoa " + getCrcrVO().getNomeSacado() + " não é um cnpj valido. Por favor verificar o Cadastro.");
		pagadorVO.setCpf_cnpj_pagador(Uteis.preencherComZerosPosicoesVagas(Uteis.removerMascara(getCrcrVO().getNumeroInscricao()), 14));
		pagadorVO.setNome_pagador(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getNomeSacado())), 30));
		pagadorVO.setLogradouro_pagador(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getLogradouro())), 40));
		pagadorVO.setBairro_pagador(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(getCrcrVO().getBairro()), 15));
		pagadorVO.setCidade_pagador(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(getCrcrVO().getCidade()), 20));
		pagadorVO.setUf_pagador(Uteis.copiarDelimitandoTamanhoDoTexto(getCrcrVO().getEstado(), 2));
		pagadorVO.setCep_pagador(Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerMascara(getCrcrVO().getCep()), 8));
		return pagadorVO;
	}

	private BeneficiarioVO montarDadosBeneficiario() {
		BeneficiarioVO beneficiarioVO = new BeneficiarioVO();
		beneficiarioVO.setCpf_cnpj_beneficiario(Uteis.preencherComZerosPosicoesVagas(Uteis.removerMascara(getCrcrVO().getCnpj()), 14));
		beneficiarioVO.setAgencia_beneficiario(Uteis.preencherComZerosPosicoesVagas(getContaReceberVO().getContaCorrenteVO().getAgencia().getNumeroAgencia(), 4));
		beneficiarioVO.setConta_beneficiario(Uteis.preencherComZerosPosicoesVagas(getContaReceberVO().getContaCorrenteVO().getNumero(), 7));
		beneficiarioVO.setDigito_verificador_conta_beneficiario(getContaReceberVO().getContaCorrenteVO().getDigito());
		return beneficiarioVO;
	}

	private RecebimentoDivergenteVO montarDadosRecebimentoDivergente() {
		RecebimentoDivergenteVO recebimentodivergente = new RecebimentoDivergenteVO();
		recebimentodivergente.setTipo_autorizacao_recebimento("3");
		return recebimentodivergente;
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
