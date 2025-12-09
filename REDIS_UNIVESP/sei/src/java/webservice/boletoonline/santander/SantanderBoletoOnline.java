package webservice.boletoonline.santander;

import java.io.File;
import java.net.URI;
import java.security.KeyStore;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.thoughtworks.xstream.XStream;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.EnvelopeBoleto;
import webservice.boletoonline.santander.classes.EntryVO;
import webservice.boletoonline.santander.classes.ImplCreateVO;
import webservice.boletoonline.santander.classes.SoapenvBodyVO;
import webservice.boletoonline.santander.classes.SoapenvEnvelopeVO;
import webservice.boletoonline.santander.classes.TicketRequestVO;
import webservice.nfse.goiania.EmptyConverter;

@SuppressWarnings("rawtypes")
public class SantanderBoletoOnline extends ControleAcesso {

	private static final long serialVersionUID = -5616498034461435883L;
	
	public ContaReceberVO contareceberVO;
	public ControleRemessaContaReceberVO crcrVO;
	public ConfiguracaoGeralSistemaVO config;
	public ConfiguracaoFinanceiroVO configFin;
	private KeyStore rep;
	private KeyStore repCadeiaCert;
	
	private final XStream xstream;
	private boolean enableValidation;
	
	public SantanderBoletoOnline(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO confiFin) {
		setContareceberVO(contareceberVO);
		setCrcrVO(crcrVO);
		setConfig(config);
		setConfigFin(configFin);
		xstream = xstream();
		enableValidation = true;
	}
	
	public void enviarBoletoRemessaOnlineSantander () throws Exception {
		String xml = this.gerarXMLObterTicket();
		//System.out.println(xml);
		xml = xml.replaceAll(">-", ">");
    	String nomeCertificado = getContareceberVO().getContaCorrenteVO().getArquivoCertificadoVO().getNome();
    	String caminhoCertificado = getConfig().getLocalUploadArquivoFixo() + File.separator + getContareceberVO().getContaCorrenteVO().getArquivoCertificadoVO().getPastaBaseArquivo() + File.separator + nomeCertificado;			
		String senhaCertificado = contareceberVO.getContaCorrenteVO().getSenhaCertificado();

		String codticket = "";
		String ticket = "";
		String retorno = enviar(xml, "https://ymbdlb.santander.com.br/dl-ticket-services/TicketEndpointService", caminhoCertificado, senhaCertificado);
		
		if (retorno.contains("<retCode>")) {
			codticket = retorno.substring(retorno.indexOf("<retCode>"), retorno.indexOf("</retCode>"));
			codticket = codticket.replaceAll("<retCode>", "");
		}
		if (retorno.contains("<ticket>") && codticket.equals("0")) {
			ticket = retorno.substring(retorno.indexOf("<ticket>"), retorno.indexOf("</ticket>"));
			ticket = ticket.replaceAll("<ticket>", "");
		} else {
			throw new Exception("Erro na obtenção do ticket de autênticação.");
		}
		String xmlInclusaoTitulo = getXmlInclusaoTitulo();
		xmlInclusaoTitulo = xmlInclusaoTitulo.replaceAll("dtNsuValue", Uteis.getData(new Date(), "ddMMyyyy")); 
		xmlInclusaoTitulo = xmlInclusaoTitulo.replaceAll("estacaoValue", getContareceberVO().getContaCorrenteVO().getCodigoEstacaoRemessa()); // codigo estacao no santander.
		// usar TST na frente quando for ambiente de teste.
		Integer incremental = getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalPorContaCorrente(getContareceberVO().getContaCorrenteVO().getCodigo(), null);
		xmlInclusaoTitulo = xmlInclusaoTitulo.replaceAll("nsuValue", incremental.toString()); 
		//xmlInclusaoTitulo = xmlInclusaoTitulo.replaceAll("nsuValue", "TST"+getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalPorContaCorrente(getContareceberVO().getContaCorrenteVO().getCodigo(), null)); 
		xmlInclusaoTitulo = xmlInclusaoTitulo.replaceAll("ticketValue", ticket); 
		//xmlInclusaoTitulo = xmlInclusaoTitulo.replaceAll("ambienteValue", "T"); 
		xmlInclusaoTitulo = xmlInclusaoTitulo.replaceAll("ambienteValue", "P"); 		
		retorno = enviar(xmlInclusaoTitulo, "https://ymbcash.santander.com.br/ymbsrv/CobrancaEndpointService", caminhoCertificado, senhaCertificado);
		String situacao = "";
		String msgErro = "";
		if (retorno.contains("<situacao>")) {
			situacao = retorno.substring(retorno.indexOf("<situacao>"), retorno.indexOf("</situacao>"));
			situacao = situacao.replaceAll("<situacao>", "");
		}
		if (situacao.equals("20")) {
			msgErro = retorno.substring(retorno.indexOf("<descricaoErro>"), retorno.indexOf("</descricaoErro>"));
			msgErro = msgErro.replaceAll("<descricaoErro>", "");
			if (!msgErro.equalsIgnoreCase("@ERYKE0001 - NOSSO NUMERO JA EXISTENTE") && !msgErro.equalsIgnoreCase("@ERYKE0001 - TITULO EXISTENTE")) {
				throw new Exception(msgErro);				
			}			
		}
        getFacadeFactory().getControleRemessaMXFacade().alterarIncrementalPorContaCorrente(getContareceberVO().getContaCorrenteVO().getCodigo(), incremental + 1, null, null);
	}

	private static String enviar(String xml, String urlChamar, String caminhoCertificado, String senhaCertificado) throws Exception {
		try {
	    	//caminhoCertificado = "C:\\Users\\THYAGO JAYME DINIZ\\Desktop\\Certificados\\Certificado Digital - Goiania - ok\\IPOG  INSTITUTO DE POSGRADUACAO E GRADUACAO LTD04688977000102.pfx";
			EnvelopeBoleto envelopeBoleto = new EnvelopeBoleto();
	    	envelopeBoleto.setXml(xml);
	    	envelopeBoleto.setUrlChamar(urlChamar);
	    	envelopeBoleto.setCaminhoCertificado(caminhoCertificado);
	    	envelopeBoleto.setSenhaCertificado(senhaCertificado);
	    	String url = "http://localhost:8097/enviarBoleto";
	    	RestTemplate restTemplate = new RestTemplate();
			RequestEntity<EnvelopeBoleto> request = RequestEntity.post(new URI(url)).contentType(MediaType.APPLICATION_JSON).body(envelopeBoleto);
	    	ResponseEntity<String> response = restTemplate.exchange(request, String.class);
	    	if(response.getStatusCode().equals(HttpStatus.BAD_REQUEST)){
	    		throw new Exception(response.getBody());
	    	}
	    	return response.getBody();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public String gerarXMLObterTicket() {
		SoapenvEnvelopeVO soap = new SoapenvEnvelopeVO();
		EntryVO e1 = new EntryVO();
		e1.setKey("CONVENIO.COD-BANCO");
		e1.setValue("0033");
		EntryVO e2 = new EntryVO();
		e2.setKey("CONVENIO.COD-CONVENIO");
		e2.setValue(getContareceberVO().getContaCorrenteVO().getConvenio());
		EntryVO e3 = new EntryVO();
		e3.setKey("PAGADOR.TP-DOC");
		if (Uteis.removeCaractersEspeciais(crcrVO.getNumeroInscricao().toString()).replaceAll(" ", "").length() > 12) {
			e3.setValue("02");
		} else {
			e3.setValue("01");
		}
		EntryVO e4 = new EntryVO();
		e4.setKey("PAGADOR.NUM-DOC");
		Uteis.checkState(e3.getValue().equals("01") && !Uteis.verificaCPF(crcrVO.getNumeroInscricao().toString()), "O cpf informado para a pessoa " + getCrcrVO().getNomeSacado()+ " não é um cpf valido. Por favor verificar o Cadastro.");
		Uteis.checkState(e3.getValue().equals("02") && !Uteis.validaCNPJ(crcrVO.getNumeroInscricao().toString()), "O CNPJ informado para a pessoa " + getCrcrVO().getNomeSacado()+ " não é um cnpj valido. Por favor verificar o Cadastro.");
		e4.setValue(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(crcrVO.getNumeroInscricao().toString()).replaceAll(" ", ""), 14));
		EntryVO e5 = new EntryVO();
		e5.setKey("PAGADOR.NOME");
		e5.setValue(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getNomeSacado())).substring(0, Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getNomeSacado())).length() > 40 ? 40 : Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getNomeSacado())).length()));
		EntryVO e6 = new EntryVO();
		e6.setKey("PAGADOR.ENDER");
		e6.setValue(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getLogradouro())).substring(0, Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getLogradouro())).length() > 40 ? 40 : Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getLogradouro())).length()));		
		EntryVO e7 = new EntryVO();
		e7.setKey("PAGADOR.BAIRRO");
		e7.setValue(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getBairro())).substring(0,  Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getBairro())).length() > 30 ? 30 : Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getBairro())).length()));
		EntryVO e28 = new EntryVO();
		e28.setKey("PAGADOR.CIDADE");
		e28.setValue(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getCidade())).substring(0, Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getCidade())).length() > 20 ? 20 : Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getCidade())).length()));
		EntryVO e8 = new EntryVO();
		e8.setKey("PAGADOR.UF");
		e8.setValue(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getCrcrVO().getEstado())));
		EntryVO e9 = new EntryVO();
		e9.setKey("PAGADOR.CEP");
		if (getCrcrVO().getCep().length() > 0) {
			e9.setValue(Uteis.removerMascara(Uteis.removerAcentuacao(getCrcrVO().getCep())).substring(0, 5) + Uteis.removerMascara(Uteis.removerAcentuacao(getCrcrVO().getCep())).substring(5));
		} else {
			e9.setValue("");
		}
		EntryVO e10 = new EntryVO();
		e10.setKey("TITULO.NOSSO-NUMERO");
		e10.setValue(getCrcrVO().getNossoNumero());
		EntryVO e11 = new EntryVO();
		e11.setKey("TITULO.SEU-NUMERO");
		e11.setValue(getCrcrVO().getNossoNumero());
		EntryVO e12 = new EntryVO();
		e12.setKey("TITULO.DT-VENCTO");
		e12.setValue(Uteis.getData(getCrcrVO().getDataVencimento(), "ddMMyyyy"));
		EntryVO e13 = new EntryVO();
		e13.setKey("TITULO.DT-EMISSAO");
		Date dataEmissao = new Date();
		if (getCrcrVO().getValorDescontoDataLimite() != 0 
        		&& (getCrcrVO().getDataLimiteConcessaoDesconto() == null || 
        			(getCrcrVO().getDataLimiteConcessaoDesconto() != null && 
        						(getCrcrVO().getDataLimiteConcessaoDesconto().compareTo(new Date()) <0 || Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto()).equals(Uteis.getData(new Date()))))
        			)
        	) {
			dataEmissao = Uteis.obterDataPassada(new Date(), 1);
		}
		e13.setValue(Uteis.getData(dataEmissao, "ddMMyyyy"));
		EntryVO e14 = new EntryVO();
		e14.setKey("TITULO.ESPECIE");
		e14.setValue("04");
		EntryVO e15 = new EntryVO();
		e15.setKey("TITULO.VL-NOMINAL");
        if (getContareceberVO().getContaCorrenteVO().getCarteiraRegistrada() && getContareceberVO().getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
    		e15.setValue(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorBaseComAcrescimo())), 13));
        } else {
	        //VALOR DO TITULO
    		e15.setValue(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorComAcrescimo())), 13));
        }		
		EntryVO e16 = new EntryVO();
		e16.setKey("TITULO.PC-MULTA");
		e16.setValue("00200");
		EntryVO e17 = new EntryVO();
		e17.setKey("TITULO.QT-DIAS-MULTA");
		e17.setValue("01");
		EntryVO e18 = new EntryVO();
		e18.setKey("TITULO.PC-JURO");
		e18.setValue("00100");
		String tpDesc = "0";
		String vlDesconto1 = "";
		String dataLimiteDesconto1 = "";
		String vlDesconto2 = "";
		String dataLimiteDesconto2 = "";
		String vlDesconto3 = "";
		String dataLimiteDesconto3 = "";
		
		if (getCrcrVO().getValorDescontoDataLimite() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto() == null || (getCrcrVO().getDataLimiteConcessaoDesconto() != null && (getCrcrVO().getDataLimiteConcessaoDesconto().compareTo(new Date()) >0|| Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto()).equals(Uteis.getData(new Date())))))) {        
			tpDesc = "1";
			vlDesconto1 = Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite())), 13);
            dataLimiteDesconto1 = Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto(), "ddMMyyyy");
        } else if (getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null && (getCrcrVO().getDataLimiteConcessaoDesconto2().compareTo(new Date()) >= 0)) || Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto2()).equals(Uteis.getData(new Date())))) {
        	tpDesc = "1";
        	vlDesconto1 = Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite2())), 13);    			
            dataLimiteDesconto1 = Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto2(), "ddMMyyyy");
            getCrcrVO().setDescontoDataLimite2Aplicado(true);        
    	} else if (getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null && (getCrcrVO().getDataLimiteConcessaoDesconto3().compareTo(new Date()) >= 0 || Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3()).equals(Uteis.getData(new Date())))))) {
    		tpDesc = "1";
    		vlDesconto1 = Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite3())), 13);    			
            dataLimiteDesconto1 = Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3(), "ddMMyyyy");
            getCrcrVO().setDescontoDataLimite3Aplicado(true);
        }
		
		if (!getCrcrVO().getDescontoDataLimite2Aplicado() && getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null && (getCrcrVO().getDataLimiteConcessaoDesconto2().compareTo(new Date()) >= 0 || Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto2()).equals(Uteis.getData(new Date())))))) {
			tpDesc = "1";
			vlDesconto2 = Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite2())), 13);
	        dataLimiteDesconto2 = Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto2(), "ddMMyyyy");
		} else if (!getCrcrVO().getDescontoDataLimite3Aplicado() &&  getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null && (getCrcrVO().getDataLimiteConcessaoDesconto3().compareTo(new Date()) >= 0 || Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3()).equals(Uteis.getData(new Date())))))) {
			tpDesc = "1";
			vlDesconto2 = Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite3())), 13);
		    dataLimiteDesconto2 = Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3(), "ddMMyyyy");
		    getCrcrVO().setDescontoDataLimite3Aplicado(true);
		}		
		
		if (!getCrcrVO().getDescontoDataLimite3Aplicado() && getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null && (getCrcrVO().getDataLimiteConcessaoDesconto3().compareTo(new Date()) >= 0 || Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3()).equals(Uteis.getData(new Date())))))) {
			tpDesc = "1";
			vlDesconto3 = Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite3())), 13);
	        dataLimiteDesconto3 = Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3(), "ddMMyyyy");
		}
		
		EntryVO e19 = new EntryVO();
		e19.setKey("TITULO.TP-DESC");
		e19.setValue(tpDesc);
		//Desconto
		EntryVO e20 = new EntryVO();
		e20.setKey("TITULO.VL-DESC");		
		e20.setValue(vlDesconto1);		
		EntryVO e21 = new EntryVO();
		e21.setKey("TITULO.DT-LIMI-DESC");
		e21.setValue(dataLimiteDesconto1);
		//Desconto 2
		EntryVO VL_DESC2 = new EntryVO();
		VL_DESC2.setKey("TITULO.VL-DESC2");		
		VL_DESC2.setValue(vlDesconto2);
		EntryVO DT_LIMI_DESC2 = new EntryVO();
		DT_LIMI_DESC2.setKey("TITULO.DT-LIMI-DESC2");		
		DT_LIMI_DESC2.setValue(dataLimiteDesconto2);
		//Desconto 3
		EntryVO VL_DESC3 = new EntryVO();
		VL_DESC3.setKey("TITULO.VL-DESC3");		
		VL_DESC3.setValue(vlDesconto3);
		EntryVO DT_LIMI_DESC3 = new EntryVO();
		DT_LIMI_DESC3.setKey("TITULO.DT-LIMI-DESC3");		
		DT_LIMI_DESC3.setValue(dataLimiteDesconto3);
		
		
		EntryVO e22 = new EntryVO();
		e22.setKey("TITULO.VL-ABATIMENTO");
		
		//Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorDescontoDataLimite())), 13)		
        if (getContareceberVO().getContaCorrenteVO().getCarteiraRegistrada() && getContareceberVO().getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
        	if (getCrcrVO().getValorBaseComAcrescimo() > 0 && getCrcrVO().getValorBaseComAcrescimo() > getCrcrVO().getValorComAcrescimo()) {
        		Double valorDescFinal = getCrcrVO().getValorBaseComAcrescimo() - getCrcrVO().getValorComAcrescimo();
        		e22.setValue(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(valorDescFinal)), 13));
        	} else {
        		if (getCrcrVO().getValorAbatimento() > 0) {
        			e22.setValue(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorAbatimento())), 13));
            	} else {
            		e22.setValue(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(0)), 13));
            	}
        	}
        } else {
        	if (getCrcrVO().getValorAbatimento() > 0) {
        		e22.setValue(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorAbatimento())), 13));
        	} else {
        		e22.setValue(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(0)), 13));
        	}
        }               
		
		EntryVO e23 = new EntryVO();
		e23.setKey("TITULO.TP-PROTESTO");
		if (getContareceberVO().getContaCorrenteVO().getHabilitarProtestoBoleto()) {
			e23.setValue("1");	
		} else {
			e23.setValue("0");
		}
		EntryVO e24 = new EntryVO();
		e24.setKey("TITULO.QT-DIAS-PROTESTO");
		if (getContareceberVO().getContaCorrenteVO().getHabilitarProtestoBoleto()) {		
			e24.setValue(getContareceberVO().getContaCorrenteVO().getQtdDiasProtestoBoleto_Str());
		} else {
			e24.setValue("00");
		}
		EntryVO e26 = new EntryVO();
		e26.setKey("TITULO.QT-DIAS-BAIXA");
		e26.setValue(getContareceberVO().getContaCorrenteVO().getQtdDiasBaixaAutTitulo() > 100 ? "99" : getContareceberVO().getContaCorrenteVO().getQtdDiasBaixaAutTitulo().toString());
		EntryVO e27 = new EntryVO();
		e27.setKey("MENSAGEM");
		e27.setValue("");
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e1);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e2);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e3);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e4);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e5);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e6);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e7);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e28);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e8);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e9);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e10);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e11);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e12);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e13);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e14);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e15);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e16);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e17);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e18);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e19);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e20);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e21);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(VL_DESC2);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(DT_LIMI_DESC2);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(VL_DESC3);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(DT_LIMI_DESC3);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e22);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e23);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e24);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e26);
		soap.getSoapenvBody().getImplCreate().getTicketRequest().getListaEntry().add(e27);
		xstream.alias("soapenv:Envelope", SoapenvEnvelopeVO.class);
		xstream.aliasField("soapenv:Header", SoapenvEnvelopeVO.class, "soapenvHeader");		
		xstream.aliasField("soapenv:Body", SoapenvEnvelopeVO.class, "soapenvBody");		
		xstream.alias("impl:create", ImplCreateVO.class);
		xstream.aliasField("impl:create", SoapenvBodyVO.class, "implCreate");
		xstream.aliasField("TicketRequest", ImplCreateVO.class, "ticketRequest");
		xstream.alias("TicketRequest", TicketRequestVO.class);
		xstream.aliasField("dados", TicketRequestVO.class, "listaEntry");
		xstream.alias("entry", EntryVO.class);
		xstream.aliasAttribute(SoapenvEnvelopeVO.class, "xmlns1", "xmlns:soapenv");
		xstream.aliasAttribute(SoapenvEnvelopeVO.class, "xmlns2", "xmlns:impl");
		String xml = xstream.toXML(soap);
		return xml;
	}

	public ContaReceberVO getContareceberVO() {
		if (contareceberVO == null) {
			contareceberVO = new ContaReceberVO();
		}
		return contareceberVO;
	}

	public void setContareceberVO(ContaReceberVO contareceberVO) {
		this.contareceberVO = contareceberVO;
	}

	public ConfiguracaoGeralSistemaVO getConfig() {
		if (config == null) {
			config = new ConfiguracaoGeralSistemaVO();
		}
		return config;
	}

	public void setConfig(ConfiguracaoGeralSistemaVO config) {
		this.config = config;
	}

	public ControleRemessaContaReceberVO getCrcrVO() {
		if (crcrVO == null) {
			crcrVO = new ControleRemessaContaReceberVO();
		}
		return crcrVO;
	}

	public void setCrcrVO(ControleRemessaContaReceberVO crcrVO) {
		this.crcrVO = crcrVO;
	}

	public ConfiguracaoFinanceiroVO getConfigFin() {
		if (configFin == null) {
			configFin = new ConfiguracaoFinanceiroVO();
		}
		return configFin;
	}

	public void setConfigFin(ConfiguracaoFinanceiroVO configFin) {
		this.configFin = configFin;
	}

	private XStream xstream() {
		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.registerConverter(new EmptyConverter());
		
		xstream.autodetectAnnotations(true);
		return xstream;
	}
	
	public String getXmlInclusaoTitulo() {
		return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:impl=\"http://impl.webservice.ymb.app.bsbr.altec.com/\">" + 
				"<soapenv:Header/>" + 
				"<soapenv:Body>" + 
				"<impl:registraTitulo>" + 
				"<dto>" + 
				"<dtNsu>dtNsuValue</dtNsu>" + 
				"<estacao>estacaoValue</estacao>" + 
				"<nsu>nsuValue</nsu>" + 
				"<ticket>ticketValue</ticket>" + 
				"<tpAmbiente>ambienteValue</tpAmbiente>" + 
				"</dto>" + 
				"</impl:registraTitulo>" + 
				"</soapenv:Body>" + 
				"</soapenv:Envelope>";
	}
}
