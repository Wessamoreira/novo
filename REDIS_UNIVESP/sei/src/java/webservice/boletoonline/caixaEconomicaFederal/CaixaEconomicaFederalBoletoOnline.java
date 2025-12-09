package webservice.boletoonline.caixaEconomicaFederal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.EnvelopeBoleto;
import webservice.boletoonline.caixaEconomicaFederal.classes.DadosBoletoOnlineCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.DescontosBoletoOnlineCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.EnderecoBoletoOnlineCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.ExternoVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.FichaCompensacaoBoletoOnlineCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.Juros_moraCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.MultaBoletoOnlineCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.OperacaoVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.PagadorBoletoOnlineCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.PagamentoBoletoOnlineCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.PosVencimentoBoletoOnlineCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.ReciboPagadorBoletoOnlineCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.SibarVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.SoapEnvBodyCaixaEconomicaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.SoapenvEnvelopeCaixaVO;
import webservice.boletoonline.caixaEconomicaFederal.classes.TituloVO;
import webservice.nfse.goiania.EmptyConverter;

@SuppressWarnings("rawtypes")
public class CaixaEconomicaFederalBoletoOnline extends ControleAcesso {

	private static final long serialVersionUID = -5616498034461435883L;
	
	private ContaReceberVO contareceberVO;
	private ControleRemessaContaReceberVO crcrVO;
	private ConfiguracaoGeralSistemaVO config;
	private ConfiguracaoFinanceiroVO configFin;	
	private  XStream xstream;	
    private static final  String operacaoIncluiBoleto = "INCLUI_BOLETO";
    private static final  String operacaoAlteraBoleto = "ALTERA_BOLETO";
    private static final  String operacaoBaixaBoleto = " BAIXA_BOLETO";
    private static final  String operacaoConsultaBoleto = "CONSULTA_BOLETO";
    private static final  String PATERN = "yyyy-MM-dd";
    private static final  String PATERN2 = "ddMMyyyy";
    private static final  String PATERN3 = "yyyyMMddHHmmss";
    
	
	public CaixaEconomicaFederalBoletoOnline(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO configFin) {
		setContareceberVO(contareceberVO);
		setCrcrVO(crcrVO);
		setConfig(config);
		setConfigFin(configFin);
		this.xstream = xstream();		
	}
	
	public void enviarBoletoRemessaOnlineCaixaEconomica () throws Exception {
		String xml = this.gerarXML(operacaoIncluiBoleto);	
		String retorno = enviar(xml, SOAPConstants.SOAP_1_1_PROTOCOL ,operacaoIncluiBoleto);
		tratarRetorno(retorno);	
		
		
	}
	
	public void consultarBoletoRemessaOnlineCaixaEconomica () throws Exception {
		String xml = this.gerarXML(operacaoConsultaBoleto);	
		String retorno = enviar(xml, SOAPConstants.SOAP_1_1_PROTOCOL ,operacaoConsultaBoleto);
		tratarRetorno(retorno);		
	}
	public void baixaBoletoRemessaOnlineCaixaEconomica () throws Exception {
		String xml = this.gerarXML(operacaoBaixaBoleto);	
		String retorno = enviar(xml, SOAPConstants.SOAP_1_1_PROTOCOL ,operacaoBaixaBoleto);
		tratarRetorno(retorno);		
	}
	
	
	
	private void tratarRetorno(String retorno) throws Exception {		
		String codigoRetorno = "";
		String OrigemRetorno ="";
		String msg_retorno ="";
		String excecao ="";
		String OrigemRetorno2 ="";
		String codigoRetorno2 = "";
		String msg_retorno2 ="";	
		
		if(retorno.contains("<COD_RETORNO>")) {
			codigoRetorno =	retorno.substring(retorno.indexOf("<COD_RETORNO>"), retorno.indexOf("</COD_RETORNO>"));
			codigoRetorno = codigoRetorno.replaceAll("<COD_RETORNO>","");			
		}
		if(retorno.contains("<ORIGEM_RETORNO>")) {
			OrigemRetorno  =retorno.substring(retorno.indexOf("<ORIGEM_RETORNO>"), retorno.indexOf("</ORIGEM_RETORNO>"));
			OrigemRetorno = OrigemRetorno.replaceAll("<ORIGEM_RETORNO>", "");
		}
		if(retorno.contains("<MSG_RETORNO>")) {
			msg_retorno =retorno.substring(retorno.indexOf("<MSG_RETORNO>"), retorno.indexOf("</MSG_RETORNO>"));
			msg_retorno = msg_retorno.replaceAll("<MSG_RETORNO>", "");
		}
		if(retorno.contains("<EXCECAO>")) {
			 excecao =	retorno.substring(retorno.indexOf("<EXCECAO>"), retorno.indexOf("</EXCECAO>"));
			 excecao =excecao.replaceAll("<EXCECAO>", "");
		}
		
		if(retorno.contains("<DADOS><CONTROLE_NEGOCIAL><ORIGEM_RETORNO>")) {
			OrigemRetorno2  =	retorno.substring(retorno.indexOf("<DADOS><CONTROLE_NEGOCIAL><ORIGEM_RETORNO>"), retorno.indexOf("</ORIGEM_RETORNO><COD_RETORNO>"));
			OrigemRetorno2 = OrigemRetorno2.replaceAll("<DADOS><CONTROLE_NEGOCIAL><ORIGEM_RETORNO>", "");
		}
		
		if(retorno.contains("</ORIGEM_RETORNO><COD_RETORNO>")) {
			codigoRetorno2  =	retorno.substring(retorno.indexOf("</ORIGEM_RETORNO><COD_RETORNO>"), retorno.indexOf("</COD_RETORNO><MENSAGENS>"));
			codigoRetorno2 = codigoRetorno2.replaceAll("</ORIGEM_RETORNO><COD_RETORNO>", "");
		}
		if(retorno.contains("<MENSAGENS><RETORNO>")) {
			msg_retorno2  =	retorno.substring(retorno.indexOf("<MENSAGENS><RETORNO>"), retorno.indexOf("</RETORNO></MENSAGENS>"));
			msg_retorno2 = msg_retorno2.replaceAll("<MENSAGENS><RETORNO>", "");
		}
		
		if(!codigoRetorno.equals("00")) {
			throw new Exception("Origem  do Erro :"+OrigemRetorno +"."  +"Motivo Erro :"+msg_retorno );			
		}
		
		if(!codigoRetorno2.equals("0")) {
			throw new Exception(msg_retorno2);		
			
		}
	}

	private static String enviar( String envelope, String protocolo, String operacao) throws Exception {
		SOAPConnection con = null;
		ByteArrayOutputStream in = new ByteArrayOutputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SOAPMessage message;
		MessageFactory factory;
		MimeHeaders header = new MimeHeaders();
		URL url = null;
		SOAPMessage res = null;
		try {
			factory = MessageFactory.newInstance(protocolo);
			try {
				url = new URL("https://barramento.caixa.gov.br/sibar/ManutencaoCobrancaBancaria/Boleto/Externo");
				
				if(operacao.equals(operacaoConsultaBoleto)) {
					url = new URL("https://barramento.caixa.gov.br/sibar/ConsultaCobrancaBancaria/Boleto");	
				}			
				message = factory.createMessage(header, new ByteArrayInputStream(envelope.getBytes()));
				header.addHeader("Content-Type", "application/soap+xml");
				header.getAllHeaders();				
				message.getMimeHeaders().addHeader("SOAPAction", operacaoIncluiBoleto);
				con = SOAPConnectionFactory.newInstance().createConnection();
				res = con.call(message, url);
				in = new ByteArrayOutputStream();
				message.writeTo(in);				
				out = new ByteArrayOutputStream();
				res.writeTo(out);
				return out.toString().replaceAll("&lt;", "<").replaceAll("&gt;", ">");
			} catch (IOException ex) {
				throw new Exception(ex.getMessage());
			}
		} catch (SOAPException ex) {
			throw new Exception(ex.getMessage());
		} finally {
			if (con != null) {
				con.close();
			}
			in.close();
			out.close();
			in = null;
			out = null;
			message = null;
			factory = null;
			header = null;
			url = null;
			res = null;
		}
	}
	
	public String gerarXML(String operacao) {
		SoapenvEnvelopeCaixaVO soapCaixa = new SoapenvEnvelopeCaixaVO();
	    
		gerarDadosHeaderBoleto(soapCaixa ,operacao);	  
	    if(operacao.equals(operacaoIncluiBoleto)) {  
	    	gerarDadosEntradaServicoIncluiBoleto(soapCaixa, operacao);	    	
	    }
	    if(operacao.equals(operacaoAlteraBoleto)) {	    	
	    	gerarDadosEntradaServicoAlteraBoleto(soapCaixa ,operacao);
	    }
	    if(operacao.equals(operacaoBaixaBoleto)) {
	    	gerarDadosEntradaServicoBaixaBoleto(soapCaixa ,operacao);
	    }
	    if(operacao.equals(operacaoConsultaBoleto)) {
	    	gerarDadosEntradaServicoConsultaBoleto(soapCaixa ,operacao);
	    }
		String xml = xstream.toXML(soapCaixa).replaceAll("__", "_").replaceAll(">-", ">");		
		return xml;
	}
	
	private void gerarDadosHeaderBoleto(SoapenvEnvelopeCaixaVO soapCaixa,String operacao) {
		//topo 	
	   	  xstream.aliasAttribute(SoapenvEnvelopeCaixaVO.class, "xmlns1", "xmlns:soapenv");	 
	   	  xstream.aliasAttribute(SoapenvEnvelopeCaixaVO.class, "xmlns3", "xmlns:sib");	
	   	  xstream.aliasAttribute(SoapenvEnvelopeCaixaVO.class, "xmlns2", "xmlns:manutencaocobrancabancaria");
	   	  
		  xstream.alias("MENSAGEM", String.class );		
		  xstream.aliasField(operacao, DadosBoletoOnlineCaixaEconomicaVO.class, "operacaoVO");
		  xstream.aliasField("manutencaocobrancabancaria:SERVICO_ENTRADA",SoapEnvBodyCaixaEconomicaVO.class, "externoVO");
		  
		  soapCaixa.setXmlns1("http://schemas.xmlsoap.org/soap/envelope/");
		  soapCaixa.setXmlns2("http://caixa.gov.br/sibar/manutencao_cobranca_bancaria/boleto/externo");
		  soapCaixa.setXmlns3("http://caixa.gov.br/sibar");
		
		if(operacao.equals(operacaoConsultaBoleto)) {		  
		  soapCaixa.setXmlns2("http://caixa.gov.br/sibar/consulta_cobranca_bancaria/boleto");
		  xstream.aliasAttribute(SoapenvEnvelopeCaixaVO.class, "xmlns2", "xmlns:consultacobrancabancaria");
		  xstream.aliasField("consultacobrancabancaria:SERVICO_ENTRADA",SoapEnvBodyCaixaEconomicaVO.class, "externoVO");		  
		}	
		//sibar
		soapCaixa.getSoapenvBodyCaixaEconomicaVO().getExternoVO().setSibar(gerarSibar(operacao));		
	}
	
	private void gerarDadosEntradaServicoIncluiBoleto(SoapenvEnvelopeCaixaVO soapCaixa,String operacao) {		
        soapCaixa.getSoapenvBodyCaixaEconomicaVO().getExternoVO().getDados().setOperacaoVO(gerarOperacao(operacao));
        soapCaixa.getSoapenvBodyCaixaEconomicaVO().getExternoVO().getDados().getOperacao().setTitulo(this.gerarTitulo(operacao));
	}
	
	private void gerarDadosEntradaServicoAlteraBoleto(SoapenvEnvelopeCaixaVO soapCaixa,String operacao) {
		soapCaixa.getSoapenvBodyCaixaEconomicaVO().getExternoVO().getDados().setOperacaoVO(gerarOperacao(operacao));
        soapCaixa.getSoapenvBodyCaixaEconomicaVO().getExternoVO().getDados().getOperacao().setTitulo(this.gerarTitulo(operacao));		
	}
	
	private void gerarDadosEntradaServicoBaixaBoleto(SoapenvEnvelopeCaixaVO soapCaixa,String operacao) {
		soapCaixa.getSoapenvBodyCaixaEconomicaVO().getExternoVO().getDados().setOperacaoVO(gerarOperacao(operacao));		
	}
	
	private void gerarDadosEntradaServicoConsultaBoleto(SoapenvEnvelopeCaixaVO soapCaixa,String operacao) {
		soapCaixa.getSoapenvBodyCaixaEconomicaVO().getExternoVO().getDados().setOperacaoVO(gerarOperacao(operacao));		
	}
	

	private OperacaoVO gerarOperacao(String operacao) {
         OperacaoVO operacaoVO = new OperacaoVO();
         operacaoVO.setCodigo_beneficiario(getContareceberVO().getContaCorrenteVO().getCodigoCedente());   
         if(operacao.equals(operacaoBaixaBoleto) ||operacao.equals(operacaoConsultaBoleto)  ) {
        	 operacaoVO.setNossoNumero("14"+getCrcrVO().getNossoNumero());
         } 		
		return operacaoVO;
	}
	
	private TituloVO gerarTitulo(String operacao) {
        TituloVO  tituloVO = new TituloVO();
        tituloVO.setNosso_Numero("14"+getCrcrVO().getNossoNumero());
        tituloVO.setNumero_documento(getCrcrVO().getNrDocumento());
        tituloVO.setData_vencimento(Uteis.getData(getCrcrVO().getDataVencimento(), PATERN));
        tituloVO.setValor(gerarValorTitulo());       
      
        if(Uteis.isAtributoPreenchido(getCrcrVO().getEspecieTitulo())) {
        	 tituloVO.setTipo_especie(getCrcrVO().getEspecieTitulo());//21 MENSALIDADE ESCOLAR /  04 DUPLICATA SERVICO  99 OUTROS 
        }else {
        	 tituloVO.setTipo_especie("99");//21 MENSALIDADE ESCOLAR /  04 DUPLICATA SERVICO  99 OUTROS 
        }
        tituloVO.setFlag_aceite("N");
        if(!operacao.equals(operacaoIncluiBoleto)) {
        	tituloVO.setData_emissao(Uteis.getData(new Date(), PATERN));         	
        }
        tituloVO.setJuros_mora(gerarJurosMora()); 
        if(!operacao.equals(operacaoIncluiBoleto)) {
          tituloVO.setValor_abatimento(gerarValorAbatimento());
        }
        tituloVO.setPos_vencimento(gerarPosVencimentoBoletoOnlineCaixaEconomicaVO());      
        tituloVO.setCodigo_moeda("09");
        tituloVO.setPagador(gerarPagadorBoletoOnlineCaixaEconomicaVO());        
        tituloVO.setMulta(gerarMultaBoletoOnlineCaixaEconomicaVO());
        
        List<DescontosBoletoOnlineCaixaEconomicaVO> listaDescontos  = gerarDescontosBoletoOnlineCaixaEconomicaVO();
        if(Uteis.isAtributoPreenchido(listaDescontos)) {
        	 tituloVO.setDescontos(listaDescontos); 
        }  
        
        FichaCompensacaoBoletoOnlineCaixaEconomicaVO  fichaCompenssacao = gerarFichaCompensacaoBoletoOnlineCaixaEconomicaVO();
        if(Uteis.isAtributoPreenchido(fichaCompenssacao)) {
        	tituloVO.setFicha_compensacao(fichaCompenssacao);
        }
        
        ReciboPagadorBoletoOnlineCaixaEconomicaVO reciboPagador =  gerarReciboPagadorBoletoOnlineCaixaEconomicaVO();
        if(Uteis.isAtributoPreenchido(reciboPagador)) {        	
        	 tituloVO.setRecibo_pagador(reciboPagador);    
        }
       
        PagamentoBoletoOnlineCaixaEconomicaVO pagamento =  gerarPagamentoBoletoOnlineCaixaEconomicaVO();
        if(Uteis.isAtributoPreenchido(pagamento)) {        	
        	tituloVO.setPagamento(pagamento);       
        }
		return tituloVO;
	}

	private FichaCompensacaoBoletoOnlineCaixaEconomicaVO gerarFichaCompensacaoBoletoOnlineCaixaEconomicaVO() {
		FichaCompensacaoBoletoOnlineCaixaEconomicaVO  fichaCompenssacao = new FichaCompensacaoBoletoOnlineCaixaEconomicaVO();
		return fichaCompenssacao;
	}

	private ReciboPagadorBoletoOnlineCaixaEconomicaVO gerarReciboPagadorBoletoOnlineCaixaEconomicaVO() {
		ReciboPagadorBoletoOnlineCaixaEconomicaVO recibo = new ReciboPagadorBoletoOnlineCaixaEconomicaVO();
		return recibo;
	}

	private PagamentoBoletoOnlineCaixaEconomicaVO gerarPagamentoBoletoOnlineCaixaEconomicaVO() {
		    PagamentoBoletoOnlineCaixaEconomicaVO pagamento = new PagamentoBoletoOnlineCaixaEconomicaVO();
//		    pagamento.setQuantidade_permitida("");
//		    pagamento.setTipo("");
//		    pagamento.setValor_maximo("");
//		    pagamento.setVaLor_minimo("");
		
		return pagamento;
	}

	private List<DescontosBoletoOnlineCaixaEconomicaVO> gerarDescontosBoletoOnlineCaixaEconomicaVO() {
		    List<DescontosBoletoOnlineCaixaEconomicaVO> listaDescontos = new ArrayList<DescontosBoletoOnlineCaixaEconomicaVO>();
		    
		    DescontosBoletoOnlineCaixaEconomicaVO desc1 =   primeiraFaixaDesconto();
		    DescontosBoletoOnlineCaixaEconomicaVO desc2 =   segundaFaixaDesconto();
		    DescontosBoletoOnlineCaixaEconomicaVO desc3 =   terceiraFaixaDesconto();
            if(Uteis.isAtributoPreenchido(desc1)) {
        	   listaDescontos.add(desc1);		    	
		    }
            if(Uteis.isAtributoPreenchido(desc2)) {
        	   listaDescontos.add(desc2);		    	
		    }
            if(Uteis.isAtributoPreenchido(desc3)) {
        	   listaDescontos.add(desc3);		    	
		    }		   
		   
		return listaDescontos;
	}

	private MultaBoletoOnlineCaixaEconomicaVO gerarMultaBoletoOnlineCaixaEconomicaVO() {
		  MultaBoletoOnlineCaixaEconomicaVO  multa = new MultaBoletoOnlineCaixaEconomicaVO();
		  multa.setData(Uteis.getData(getCrcrVO().getDataVencimento(), PATERN));		 
		  multa.setPercentual("2.00");
		return multa;
	}

	private PagadorBoletoOnlineCaixaEconomicaVO gerarPagadorBoletoOnlineCaixaEconomicaVO() {
		 PagadorBoletoOnlineCaixaEconomicaVO pagador = new PagadorBoletoOnlineCaixaEconomicaVO();
		
		 if (getCrcrVO().getCodigoInscricao() == 0 || getCrcrVO().getCodigoInscricao() == 1) {				
			 pagador.setCpf(Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(getCrcrVO().getNumeroInscricao().toString()).trim()),11).replaceAll(" ", ""));
			 pagador.setNome(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getNomeSacado())).substring(0, Uteis.removeCaractersEspeciais(getCrcrVO().getNomeSacado()).length() > 40 ? 40 : Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getNomeSacado())).length()));	
		 } else {
				String nr = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(getCrcrVO().getNumeroInscricao().toString()).trim()).replaceAll(" ", "");
				if (nr.length() == 14) {
					pagador.setCnpj(Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(getCrcrVO().getNumeroInscricao().toString()).trim()), 14).replaceAll(" ", ""));
					pagador.setRazaoSocial(Uteis.removerAcentuacao(getCrcrVO().getUnidadeEnsino().getRazaoSocial()).substring(0, getCrcrVO().getUnidadeEnsino().getRazaoSocial().length() > 40 ? 40 : Uteis.removerAcentuacao(getCrcrVO().getUnidadeEnsino().getRazaoSocial()).length()));	
				} else {
					pagador.setNome(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getNomeSacado())).substring(0, Uteis.removeCaractersEspeciais(getCrcrVO().getNomeSacado()).length() > 40 ? 40 : Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getNomeSacado())).length()));
					pagador.setCpf(Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(getCrcrVO().getNumeroInscricao().toString()).trim()),11).replaceAll(" ", ""));
				}
			}		
		
		 if (getContareceberVO().getContaCorrenteVO().getHabilitarProtestoBoleto().equals(true)) {
		     EnderecoBoletoOnlineCaixaEconomicaVO endereco = new EnderecoBoletoOnlineCaixaEconomicaVO();	
		     endereco.setLogadouro(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getLogradouro())).substring(0, Uteis.removeCaractersEspeciais(getCrcrVO().getLogradouro()).length() > 40 ? 40 : Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(getCrcrVO().getLogradouro())).length()));
		     endereco.setBairro(Uteis.removerAcentuacao(getCrcrVO().getBairro()).substring(0, getCrcrVO().getBairro().length() > 30 ? 30 : Uteis.removerAcentuacao(getCrcrVO().getBairro()).length()));
		     endereco.setCidade(Uteis.removerAcentuacao(getCrcrVO().getCidade()).substring(0, getCrcrVO().getCidade().length() > 20 ? 20 : Uteis.removerAcentuacao(getCrcrVO().getCidade()).length()));
		     endereco.setUf(Uteis.removerAcentuacao(getCrcrVO().getEstado()));
		     endereco.setCep(Uteis.removerMascara(Uteis.removerAcentuacao(getCrcrVO().getCep())).substring(0, 5) + Uteis.removerMascara(Uteis.removerAcentuacao(getCrcrVO().getCep())).substring(5));
		     pagador.setEndereco(endereco);
		 }
		return pagador;
	}

	private PosVencimentoBoletoOnlineCaixaEconomicaVO gerarPosVencimentoBoletoOnlineCaixaEconomicaVO() {
		PosVencimentoBoletoOnlineCaixaEconomicaVO protesto = new PosVencimentoBoletoOnlineCaixaEconomicaVO();
		if (getContareceberVO().getContaCorrenteVO().getHabilitarProtestoBoleto().equals(true)) {
			protesto.setAcao("PROTESTAR");			
		} else {
			protesto.setAcao("DEVOLVER");
		}		
		if (getContareceberVO().getContaCorrenteVO().getHabilitarProtestoBoleto().equals(true)) {		
			protesto.setNumero_dias(getContareceberVO().getContaCorrenteVO().getQtdDiasProtestoBoleto_Str());
		} else {
			protesto.setNumero_dias("00");
		}
		return protesto;
	}

	private String gerarValorTitulo() {
	    String valorTitulo ="";
		//VALOR DO TITULO
        if (getContareceberVO().getContaCorrenteVO().getCarteiraRegistrada() && getContareceberVO().getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
        	valorTitulo = Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorBaseComAcrescimo());
        } else {
	        //VALOR DO TITULO
        	valorTitulo = Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorComAcrescimo());
        }	
		return valorTitulo;
	}

	private Juros_moraCaixaEconomicaVO gerarJurosMora() {	
		 //JUROS DE 1 DIA DE ATRASO
		Juros_moraCaixaEconomicaVO juros = new Juros_moraCaixaEconomicaVO();		
        Double juro = 0.01;
        Double valorTitulo = Uteis.arrendondarForcando2CadasDecimais(getConfigFin().getCobrarJuroMultaSobreValorCheioConta().equals(true) ? getCrcrVO().getValorComAcrescimo() : getCrcrVO().getValor()- getCrcrVO().getValorAbatimento());
        Double juroFinal = (valorTitulo * juro) / 30;
        String juroStr = "";
        if (juroFinal.toString().length() > 4) {
        	juroStr = (juroFinal.toString()).substring(0, 5);
        } else {
        	juroStr = juroFinal.toString();
        }
        Double juroDouble = new Double(juroStr);
        if (juroDouble > 0) {        	
        	juros.setTipo("TAXA_MENSAL");
        	juros.setData(Uteis.getData(Uteis.obterDataAvancada(getCrcrVO().getDataVencimento(), 1),PATERN));
        	juros.setPercentual(Uteis.arrendondarForcando2CadasDecimaisStr(getConfigFin().getPercentualJuroPadrao()));
      } else {    	 
    	  juros.setTipo("ISENTO");        
          juros.setPercentual("000000000000000");
      }        
		return juros;
	}

	private String gerarValorAbatimento() {
		 //VALOR ABATIMENTO 
		String valorAbatimento ="";
        if (getContareceberVO().getContaCorrenteVO().getCarteiraRegistrada() && getContareceberVO().getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
        	if (getCrcrVO().getValorBaseComAcrescimo() > 0 && getCrcrVO().getValorBaseComAcrescimo() > getCrcrVO().getValorComAcrescimo()) {
        		Double valorDescFinal = getCrcrVO().getValorBaseComAcrescimo() - getCrcrVO().getValorComAcrescimo();
        		valorAbatimento = Uteis.preencherComZerosPosicoesVagas(Uteis.arrendondarForcando2CadasDecimaisStr(valorDescFinal), 15);
        	} else {
        		if (getCrcrVO().getValorAbatimento() > 0) {
        			valorAbatimento = Uteis.preencherComZerosPosicoesVagas(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorAbatimento()), 15);
            	} else {
            		valorAbatimento = Uteis.preencherComZerosPosicoesVagas(Uteis.arrendondarForcando2CadasDecimaisStr(0), 15);
            	}
        	}
        } else {
        	if (getCrcrVO().getValorAbatimento() > 0) {
        		valorAbatimento = Uteis.preencherComZerosPosicoesVagas(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValorAbatimento()), 15);
        	} else {
        		valorAbatimento = Uteis.preencherComZerosPosicoesVagas(Uteis.arrendondarForcando2CadasDecimaisStr(0), 15);
        	}
        }  
		return valorAbatimento;
	}

	private SibarVO gerarSibar(String operacao ) {
		SibarVO sibar = new SibarVO();
		sibar.setVersao("1.0");
		sibar.setAutenticacao(Uteis.DO_HASHB64(getValoresAutenticar(operacao)));
		sibar.setUsuario_servico("SGCBS02P");
		sibar.setOperacao(operacao.toUpperCase());
		sibar.setSistema_origem("SIGCB");
		sibar.setUnidade(getContareceberVO().getContaCorrenteVO().getAgencia().getNumeroAgencia());	
		try {
			sibar.setIdentificadorOrigem(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		}
		sibar.setData_hora(Uteis.getData(new Date(),PATERN3));		
		return sibar;
	}
	
	private String getValoresAutenticar(String operacao) {
		String codigoBeneficiario = Uteis.preencherComZerosPosicoesVagas(Uteis.copiarDelimitandoTamanhoDoTexto(getContareceberVO().getContaCorrenteVO().getCodigoCedente(),7),7);
		String nossoNumero = Uteis.preencherComZerosPosicoesVagas(String.valueOf("14"+getCrcrVO().getNossoNumero()),17);		
		String dataVencimento = UteisData.getDataAplicandoFormatacao(getCrcrVO().getDataVencimento(),PATERN2);
		String valor = Uteis.preencherComZerosPosicoesVagas(Uteis.removerMascara(Uteis.arrendondarForcando2CadasDecimaisStr(getCrcrVO().getValor())),15);
	 	if(operacao.equals(operacaoConsultaBoleto)) {
	 	  dataVencimento = Uteis.preencherComZerosPosicoesVagas("", 8);
	 	  valor = Uteis.preencherComZerosPosicoesVagas("", 15);
	    }			
				
		
		String cpfCnpjBeneficiario = Uteis.preencherComZerosPosicoesVagas(Uteis.removerMascara(getCrcrVO().getCnpj()),14);
		StringBuilder str = new StringBuilder();		
	    str.append(codigoBeneficiario).append(nossoNumero).append(dataVencimento).append(valor).append(cpfCnpjBeneficiario);
	    return String.valueOf(str);
	}

	private XStream xstream() {
		XStream xstream2 = new XStream(new DomDriver());
		xstream2.setMode(XStream.NO_REFERENCES);
		xstream2.registerConverter(new EmptyConverter());		
		xstream2.autodetectAnnotations(true);
		return xstream2;
	}	
	
	public DescontosBoletoOnlineCaixaEconomicaVO primeiraFaixaDesconto() {
		    DescontosBoletoOnlineCaixaEconomicaVO desconto1 = new DescontosBoletoOnlineCaixaEconomicaVO();
		 
	        if (getCrcrVO().getValorDescontoDataLimite() != 0  && (getCrcrVO().getDataLimiteConcessaoDesconto() == null || (getCrcrVO().getDataLimiteConcessaoDesconto()!= null ))) {
	        	desconto1.setData(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto(), PATERN2)); 
			}
	        if (getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null ))) {
	        	desconto1.setData(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto2(), PATERN2)); 
			}
	        if (getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null ))) {										
	        	desconto1.setData(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3(), PATERN2)); 
			}		
	        if (getCrcrVO().getValorDescontoDataLimite() != 0  && (getCrcrVO().getDataLimiteConcessaoDesconto() == null || (getCrcrVO().getDataLimiteConcessaoDesconto()!= null  ))) {
				desconto1.setValor(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(getCrcrVO().getValorDescontoDataLimite(), 2)))), 13)); 
			}
	        if (getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null ))) {
	        	desconto1.setValor(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(getCrcrVO().getValorDescontoDataLimite2(), 2)))), 13));
			}
	        if (getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null ))) {										
				desconto1.setValor(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(getCrcrVO().getValorDescontoDataLimite3(), 3)))), 13)); 
			}		
		return desconto1;
	}

    public DescontosBoletoOnlineCaixaEconomicaVO segundaFaixaDesconto() {
    	   DescontosBoletoOnlineCaixaEconomicaVO desconto2 = new DescontosBoletoOnlineCaixaEconomicaVO();
    	
			if ((getCrcrVO().getValorDescontoDataLimite() != 0  && (getCrcrVO().getDataLimiteConcessaoDesconto() == null || (getCrcrVO().getDataLimiteConcessaoDesconto()!= null ))) && 
	        	(getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null )))) {
				desconto2.setData(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto2(), PATERN2)); 
			}
			if ((getCrcrVO().getValorDescontoDataLimite() == 0  || getCrcrVO().getDataLimiteConcessaoDesconto() == null) && 
	        	(getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null ))) && 
	        	(getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null )))) {										
				desconto2.setData(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3(), PATERN2)); 
			}		
			if ((getCrcrVO().getValorDescontoDataLimite() != 0  && (getCrcrVO().getDataLimiteConcessaoDesconto() == null || (getCrcrVO().getDataLimiteConcessaoDesconto()!= null ))) && 
	        	(getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null )))) {
				desconto2.setValor(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(getCrcrVO().getValorDescontoDataLimite2(), 2)))), 13)); 
			}
			if ((getCrcrVO().getValorDescontoDataLimite() == 0  || getCrcrVO().getDataLimiteConcessaoDesconto() == null) && 
	        	(getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null ))) && 
	        	(getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null )))) {										
				desconto2.setValor(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(getCrcrVO().getValorDescontoDataLimite3(), 2)))), 13)); 
			}
			return desconto2;
		}    

    public DescontosBoletoOnlineCaixaEconomicaVO terceiraFaixaDesconto() {
    	   DescontosBoletoOnlineCaixaEconomicaVO desconto3 = new DescontosBoletoOnlineCaixaEconomicaVO();
    	
	        if ((getCrcrVO().getValorDescontoDataLimite() != 0  && (getCrcrVO().getDataLimiteConcessaoDesconto() == null || (getCrcrVO().getDataLimiteConcessaoDesconto()!= null ))) && 
	        	(getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null ))) && 
	        	(getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null )))) {										
	        	desconto3.setData(Uteis.getData(getCrcrVO().getDataLimiteConcessaoDesconto3(), PATERN2)); 
			}	
	        if ((getCrcrVO().getValorDescontoDataLimite() != 0  && (getCrcrVO().getDataLimiteConcessaoDesconto() == null  )) && 
	        	(getCrcrVO().getValorDescontoDataLimite2() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto2() == null || (getCrcrVO().getDataLimiteConcessaoDesconto2() != null ))) && 
	        	(getCrcrVO().getValorDescontoDataLimite3() != 0 && (getCrcrVO().getDataLimiteConcessaoDesconto3() == null || (getCrcrVO().getDataLimiteConcessaoDesconto3() != null )))) {										
	        	desconto3.setValor(Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(getCrcrVO().getValorDescontoDataLimite3(), 2)))), 13)); 
			}		
		return desconto3;
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

}
