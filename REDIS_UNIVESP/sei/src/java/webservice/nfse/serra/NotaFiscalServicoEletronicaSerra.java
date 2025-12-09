package webservice.nfse.serra;

import java.security.MessageDigest;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.enumerador.TipoEmpresaEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.faturamento.nfe.enumeradores.SituacaoNotaFiscalSaidaEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.nfse.NotaFiscalServicoEletronicaInterfaceFacade;

@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class NotaFiscalServicoEletronicaSerra extends ControleAcesso implements NotaFiscalServicoEletronicaInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public NotaFiscalServicoEletronicaSerra() throws Exception {
		super();
		setIdEntidade("NotaFiscalServicoEletronicaSerra");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		NotaFiscalServicoEletronicaSerra.idEntidade = idEntidade;
	}

	@Override
	public void enviarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder xml = new StringBuilder();
			xml = montarXmlEnvio(notaFiscalSaidaVO, usuarioVO);
			Nfse builder =  new Nfse();	
			notaFiscalSaidaVO.setXmlEnvio(builder.asGerarNFSEXML(xml.toString(), notaFiscalSaidaVO, configuracaoRespositoriArquivo));
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarXmlEnvio(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getXmlEnvio(), usuarioVO);
			WSEntrada sc = new WSEntradaServiceLocator().getWSEntradaPort();
			MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaUsuarioIntegracaoNotaFiscalServico().getBytes());
            byte[] aux = md.digest();
            
			notaFiscalSaidaVO.setMensagemRetorno(sc.nfdEntrada(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getUsuarioIntegracaoNotaFiscalServico(), "", 3205002, notaFiscalSaidaVO.getXmlEnvio()));
			getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
			notaFiscalSaidaVO.setDataStuacao(new Date());
			notaFiscalSaidaVO.setMensagemRetorno(notaFiscalSaidaVO.getMensagemRetorno().replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
			if (notaFiscalSaidaVO.getMensagemRetorno().contains("<codrecibo>")) {				
				String numeroLote = notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().indexOf("<codrecibo>"), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</codrecibo>"));
				notaFiscalSaidaVO.setRecibo(numeroLote.replace("<codrecibo>", ""));
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarRecibo(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getRecibo(), usuarioVO);
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor(), "", notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioVO);
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
				notaFiscalSaidaVO.setLinkAcesso(notaFiscalSaidaVO.getWebServicesNFSEEnum().getValorApresentar().replace("{0}", notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ()).replace("{1}", notaFiscalSaidaVO.getNumeroNota()).replace("{2}", notaFiscalSaidaVO.getRecibo()));
			} else {
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor(), "", notaFiscalSaidaVO.getDataStuacao(), "", notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioVO);
				getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
			}
		} catch (Exception e) {
			progressBar.setStatus("NF-e n° " + notaFiscalSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
			autorizado = e.getMessage();
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
			notaFiscalSaidaVO.setDataEmissao(new Date());
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor(), notaFiscalSaidaVO.getProtocolo(), notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioVO);
			notaFiscalSaidaVO.setMensagemRetorno(autorizado);
			getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
			throw e;
		}
	}

	
	public StringBuilder montarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO, UsuarioVO usuarioVO) throws Exception{
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		xml.append("<tbnfd>");
		xml.append("<nfd>");
		xml.append("<numeronfd>"+notaFiscalSaidaVO.getNumeroNota()+"</numeronfd>");
		xml.append("<codseriedocumento>7</codseriedocumento>");
		xml.append("<codnaturezaoperacao>"+notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoNaturezaOperacao()+"</codnaturezaoperacao>");
		xml.append("<codigocidade>3</codigocidade>");
		xml.append("<inscricaomunicipalemissor>"+notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal()+"</inscricaomunicipalemissor>");
		xml.append("<dataemissao>"+Uteis.getData(new Date(), "dd/MM/yyyy")+"</dataemissao>");
		StringBuilder aux = new StringBuilder();
		if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
			aux = montarDadosSacado(notaFiscalSaidaVO, notaFiscalSaidaVO.getFornecedorVO(), aux, usuarioVO);
		} else if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			aux = montarDadosSacado(notaFiscalSaidaVO, notaFiscalSaidaVO.getParceiroVO(), aux, usuarioVO);
		} else {
			aux = montarDadosSacado(notaFiscalSaidaVO, notaFiscalSaidaVO.getPessoaVO(), aux, usuarioVO);
		}
		xml.append(aux);
		xml.append("<descdeducoesconstrucao />");
		xml.append("<totaldeducoesconstrucao />");
		xml.append("<tributadonomunicipio>true</tributadonomunicipio>");
		xml.append("<numerort />");
		xml.append("<codigoseriert />");
		xml.append("<dataemissaort />");
		xml.append("</nfd>");
		xml.append("</tbnfd>");
		return xml;
	}
	
	/**
	 * Nessa Sequencia
	 * NOME_ALUNO, NOME_CURSO, TIPO_PESSOA, COMPETENCIA, DESCRICAO_CONVENIO
	 * @param mensagem
	 * @param parametros
	 * @return
	 * @throws Exception
	 */
	public String montarDescrimicaoNotaFiscalServico(String mensagem, final Object... parametros) throws Exception {
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), parametros[0].toString().trim());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), parametros[1].toString().trim().toUpperCase());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.TIPO_PESSOA.name(), parametros[2].toString().trim());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.COMPETENCIA.name(), parametros[3].toString().trim());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.DESCRICAO_CONVENIO.name(), parametros[4].toString().trim());
		return mensagem;
	}
	
	public StringBuilder montarDadosSacado(NotaFiscalSaidaVO notaFiscalSaidaVO, Object tipoPessoa, StringBuilder xml, UsuarioVO usuarioVO) throws Exception {
		String nomeCurso = getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(notaFiscalSaidaVO.getMatricula(), false, usuarioVO).getNome().replace("&", "E");
		String datasCompetenciasContaReceberNotaFiscal = getFacadeFactory().getNotaFiscalSaidaFacade().concatenarDatasCompetenciasContaReceberNotaFiscal(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs());
		if(tipoPessoa.equals(notaFiscalSaidaVO.getPessoaVO())) {
			xml.append("<razaotomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getNome())+"</razaotomador>");
			xml.append("<nomefantasiatomador></nomefantasiatomador>");
			xml.append("<enderecotomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getEndereco())+"</enderecotomador>");
			xml.append("<cidadetomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getCidade().getNome())+"</cidadetomador>");
			xml.append("<estadotomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getCidade().getEstado().getSigla())+"</estadotomador>");
			xml.append("<paistomador>Brasil</paistomador>");
			xml.append("<fonetomador>"+notaFiscalSaidaVO.getPessoaVO().getCidade().getEstado().getSigla()+"</fonetomador>");
			xml.append("<faxtomador />");
			xml.append("<ceptomador>"+Uteis.removerMascara(notaFiscalSaidaVO.getPessoaVO().getCEP())+"</ceptomador>");
			xml.append("<bairrotomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getEndereco())+"</bairrotomador>");
			xml.append("<emailtomador>"+notaFiscalSaidaVO.getPessoaVO().getEmail()+"</emailtomador>");
			xml.append("<cpfcnpjtomador>"+Uteis.removerMascara(notaFiscalSaidaVO.getPessoaVO().getCPF())+"</cpfcnpjtomador>");
			xml.append("<inscricaoestadualtomador />");
			xml.append("<inscricaomunicipaltomador />");
			xml.append("<tbfatura />");
			xml.append("<tbservico>");
			xml.append("<servico>");
			xml.append("<quantidade>1</quantidade>");
			xml.append("<descricao>"+Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getPessoaVO().getNome(), nomeCurso, notaFiscalSaidaVO.getTipoPessoa(), Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()), notaFiscalSaidaVO.getNomesConvenios()}))+"</descricao>");
			xml.append("<codatividade>"+notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoItemListaServico()+"</codatividade>");
			xml.append("<valorunitario>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal(), 2)+"</valorunitario>");
			xml.append("<aliquota>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn(), 2)+"</aliquota>");
			xml.append("<impostoretido>False</impostoretido>");
			xml.append("</servico>");
			xml.append("</tbservico>");
			xml.append("<razaotransportadora />");
			xml.append("<cpfcnpjtransportadora />");
			xml.append("<enderecotransportadora />");
			xml.append("<tipofrete>0</tipofrete>");
			xml.append("<quantidade>0</quantidade>");
			xml.append("<especie />");
			xml.append("<pesoliquido>0</pesoliquido>");
			xml.append("<pesobruto>0</pesobruto>");
			xml.append("<pis />");
			xml.append("<cofins />");
			xml.append("<csll />");
			xml.append("<irrf />");
			xml.append("<inss />");
		} else if(tipoPessoa.equals(notaFiscalSaidaVO.getParceiroVO())) {
			xml.append("<razaotomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getNome())+"</razaotomador>");
			xml.append("<nomefantasiatomador></nomefantasiatomador>");
			xml.append("<enderecotomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getEndereco())+"</enderecotomador>");
			xml.append("<cidadetomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getCidade().getNome())+"</cidadetomador>");
			xml.append("<estadotomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getCidade().getEstado().getSigla())+"</estadotomador>");
			xml.append("<paistomador>Brasil</paistomador>");
			xml.append("<fonetomador>"+notaFiscalSaidaVO.getParceiroVO().getCidade().getEstado().getSigla()+"</fonetomador>");
			xml.append("<faxtomador />");
			xml.append("<ceptomador>"+Uteis.removerMascara(notaFiscalSaidaVO.getParceiroVO().getCEP())+"</ceptomador>");
			xml.append("<bairrotomador></bairrotomador>");
			xml.append("<emailtomador>"+notaFiscalSaidaVO.getParceiroVO().getEmail()+"</emailtomador>");
			if(notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				xml.append("<cpfcnpjtomador>"+Uteis.removerMascara(notaFiscalSaidaVO.getParceiroVO().getCNPJ())+"</cpfcnpjtomador>");				
			} else {
				xml.append("<cpfcnpjtomador>"+Uteis.removerMascara(notaFiscalSaidaVO.getParceiroVO().getCPF())+"</cpfcnpjtomador>");				
			}
			xml.append("<inscricaoestadualtomador />");
			xml.append("<inscricaomunicipaltomador />");
			xml.append("<tbfatura />");
			xml.append("<tbservico>");
			xml.append("<servico>");
			xml.append("<quantidade>1</quantidade>");
			xml.append("<descricao>"+Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getParceiroVO().getNome(), nomeCurso, notaFiscalSaidaVO.getTipoPessoa(), Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()), notaFiscalSaidaVO.getNomesConvenios()}))+"</descricao>");
			xml.append("<codatividade>"+notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio()+"</codatividade>");
			xml.append("<valorunitario>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal(), 2)+"</valorunitario>");
			xml.append("<aliquota>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn(), 2)+"</aliquota>");
			xml.append("<impostoretido>False</impostoretido>");
			xml.append("</servico>");
			xml.append("</tbservico>");
			xml.append("<razaotransportadora />");
			xml.append("<cpfcnpjtransportadora />");
			xml.append("<enderecotransportadora />");
			xml.append("<tipofrete>0</tipofrete>");
			xml.append("<quantidade>0</quantidade>");
			xml.append("<especie />");
			xml.append("<pesoliquido>0</pesoliquido>");
			xml.append("<pesobruto>0</pesobruto>");
			if(notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				if (notaFiscalSaidaVO.getParceiroVO().getPossuiAliquotaEmissaoNotaEspecifica()) {
					if(notaFiscalSaidaVO.getParceiroVO().getPis() > 0) {
						xml.append("<pis>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getPis() / 100), 2)+"</pis>");
					}
					if(notaFiscalSaidaVO.getParceiroVO().getCofins() > 0) {
						xml.append("<cofins>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCofins() / 100), 2)+"</cofins>");
					}
					if(notaFiscalSaidaVO.getParceiroVO().getCsll() > 0) {
						xml.append("<csll>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCsll() / 100), 2)+"</csll>");
					}
					xml.append("<irrf />");
					if(notaFiscalSaidaVO.getParceiroVO().getInss() > 0 ) {
						xml.append("<inss>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getInss() / 100), 2)+"</inss>");
					}
				} else {	
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() > 0) {
						xml.append("<pis>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100), 2)+"</pis>");
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() > 0) {
						xml.append("<cofins>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100), 2)+"</cofins>");
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() > 0) {
						xml.append("<csll>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100), 2)+"</csll>");
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR() > 0) {
						xml.append("<irrf>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR() / 100), 2)+"</irrf>");
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() > 0) {
						xml.append("<inss>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100), 2)+"</inss>");
					}
				}
			} 
		} else {
			xml.append("<razaotomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome())+"</razaotomador>");
			xml.append("<nomefantasiatomador></nomefantasiatomador>");
			xml.append("<enderecotomador>"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getEndereco())+"</enderecotomador>");
			xml.append("<cidadetomador>"+notaFiscalSaidaVO.getFornecedorVO().getCidade().getNome()+"</cidadetomador>");
			xml.append("<estadotomador>"+notaFiscalSaidaVO.getFornecedorVO().getCidade().getEstado().getSigla()+"</estadotomador>");
			xml.append("<paistomador>Brasil</paistomador>");
			xml.append("<fonetomador>"+notaFiscalSaidaVO.getFornecedorVO().getCidade().getEstado().getSigla()+"</fonetomador>");
			xml.append("<faxtomador />");
			xml.append("<ceptomador>"+Uteis.removerMascara(notaFiscalSaidaVO.getFornecedorVO().getCEP())+"</ceptomador>");
			xml.append("<bairrotomador></bairrotomador>");
			xml.append("<emailtomador>"+notaFiscalSaidaVO.getFornecedorVO().getEmail()+"</emailtomador>");
			if(notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				xml.append("<cpfcnpjtomador>"+Uteis.removerMascara(notaFiscalSaidaVO.getFornecedorVO().getCNPJ())+"</cpfcnpjtomador>");				
			} else {
				xml.append("<cpfcnpjtomador>"+Uteis.removerMascara(notaFiscalSaidaVO.getFornecedorVO().getCPF())+"</cpfcnpjtomador>");				
			}
			xml.append("<inscricaoestadualtomador />");
			xml.append("<inscricaomunicipaltomador />");
			xml.append("<tbfatura />");
			xml.append("<tbservico>");
			xml.append("<servico>");
			xml.append("<quantidade>1</quantidade>");
			xml.append("<descricao>"+Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getPessoaVO().getNome(), nomeCurso, notaFiscalSaidaVO.getTipoPessoa(), Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()), notaFiscalSaidaVO.getNomesConvenios()}))+"</descricao>");
			xml.append("<codatividade>"+notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio()+"</codatividade>");
			xml.append("<valorunitario>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal(), 2)+"</valorunitario>");
			xml.append("<aliquota>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn(), 2)+"</aliquota>");
			xml.append("<impostoretido>False</impostoretido>");
			xml.append("</servico>");
			xml.append("</tbservico>");
			xml.append("<razaotransportadora />");
			xml.append("<cpfcnpjtransportadora />");
			xml.append("<enderecotransportadora />");
			xml.append("<tipofrete>0</tipofrete>");
			xml.append("<quantidade>0</quantidade>");
			xml.append("<especie />");
			xml.append("<pesoliquido>0</pesoliquido>");
			xml.append("<pesobruto>0</pesobruto>");
			if(notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				xml.append("<pis>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100), 2)+"</pis>");
				xml.append("<cofins>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100), 2)+"</cofins>");
				xml.append("<csll>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100), 2)+"</csll>");
				xml.append("<irrf>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR() / 100), 2)+"</irrf>");
				xml.append("<inss>"+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100), 2)+"</inss>");
			} 
		}
		return xml;
	}
	
	@Override
	public void consultarLoteRps(NotaFiscalSaidaVO notaFiscalSaidaVO,
			ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado,
			UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelar(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo,
			UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void imprimirDanfe(NotaFiscalSaidaVO notaFiscalSaidaVO,
			ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
