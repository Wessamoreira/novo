package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoAlunoEnum;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("DocumentoAssinadoControle")
@Scope("viewScope")
@Lazy
public class DocumentoAssinadoControle extends SuperControle implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8021215056897265499L;
	
	private String valorConsultarDiploma;
	private String campoConsultarDiploma;
	private List<SelectItem> listaSelectItemTipoDeDocumento;
	private List<ControleLivroFolhaReciboVO> listaControleLivroFolhaRecibo;	

	private Boolean arquivoAssinadoDigitalmente;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private List<DocumentoAssinadoVO> listaDocumentoAssinadoVO;
	private String senhaLiberarHistorico;
	private DocumentoAssinadoVO arquivoApresentar;
	private Boolean cpfValidado;
	private String senhaLiberarArquivo;
	private String campoConsultarHistorico;
	private List<SelectItem> tipoConsultaComboHistorico;
	private String valorConsultarHistorico;
	private String senhaLiberarXMLDiploma;
	private ControleLivroFolhaReciboVO diplomaVisualizar;
	private String senhaLiberarDiploma;
	private String valorConsultarCurriculo;
	private String campoConsultarCurriculo;
	private List<SelectItem> tipoConsultaComboCurriculo;
	private List<DocumentoAssinadoVO> listaDocumentoAssinadoCurriculoVO;
	private DocumentoAssinadoVO historicoVisualizar;
	private String senhaLiberarXMLHistorico;
	private String senhaLiberarArquivoHistorico;
	

	public DocumentoAssinadoControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		montarListaSelectItemTipoDeDocumentoDocumentacaoCurso();
	}
	
	@PostConstruct
	public void carregarDiplomaDocumentoAssinado() {
		if(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("tipoDoc") != null && ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("dados") != null) {
			String tipoDocumento = ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("tipoDoc");
			String dados = ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("dados");
			realizarConsultarPorTipoDocumentoPorDado(tipoDocumento, dados);
		}else if (getUsuarioLogado().getIsApresentarVisaoAlunoOuPais() && getVisaoAlunoControle() != null && !getVisaoAlunoControle().getMatricula().getMatricula().isEmpty()) {
			realizarInicializacaoDocumentoDigitaisAluno();
		}
	}

	private void realizarConsultarPorTipoDocumentoPorDado(String tipoDocumento, String dados) {
		if(Uteis.isAtributoPreenchido(tipoDocumento)) {
			if(tipoDocumento.equals("DIPLOMA") ) {// correcao para rotina de legado. Pedro Andrade
				tipoDocumento = TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA.name();						
			}
			TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumento = TipoOrigemDocumentoAssinadoEnum.valueOf(tipoDocumento);
			switch (tipoOrigemDocumento) {
			case EXPEDICAO_DIPLOMA:
				setCampoConsultarDiploma("CPF");
				setValorConsultarDiploma(dados);
				setAbaAtiva("richTabDiplomaCertificado");
				consultarDiplomasCertificados();
				break;
			case DIPLOMA_DIGITAL:
				setCampoConsultarDiploma("codigoValidacao");
				setValorConsultarDiploma(dados);
				setAbaAtiva("richTabDiplomaCertificado");
				consultarDiplomasCertificados();
				break;
			case HISTORICO_DIGITAL:
				setCampoConsultarHistorico("codigoValidacaoHistoricoDigital");
				setValorConsultarHistorico(dados);
				setAbaAtiva("richTabHistoricos");
				consultarHistoricos();
				break;		
			case CURRICULO_ESCOLAR_DIGITAL:
				setCampoConsultarCurriculo("codigoValidacaoCurriculo");
				setValorConsultarCurriculo(dados);
				setAbaAtiva("richTabCurriculoDigital");
				consultarCurriculos();
				break;
			default:
				getControleConsulta().setCampoConsulta("codigo");
				getControleConsulta().setValorConsulta(dados);
				setAbaAtiva("richTab");
				consultar();
				break;
			}
		}
	}
		

	public String consultar() {
		try {
			getControleConsulta().setCampoConsulta("codigo");
			try {
				Integer valorConsulta = Integer.valueOf(getControleConsulta().getValorConsulta());
				getControleConsulta().setValorConsulta(valorConsulta.toString());
			} catch (Exception e) {
				throw new Exception("Deve ser informado apenas valores numéricos");
			}
			consultarDocumentoAssinado();
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	private void consultarDocumentoAssinado() throws Exception {
		getControleConsultaOtimizado().setLimitePorPagina(10);
		executarValidacaoParametroConsultaVazio();
		getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getDocumentoAssinadoFacade().consultarArquivoAssinado(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_TODOS, getArquivoAssinadoDigitalmente(), null, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getApresentarDocumentoPortalTransparenciaComPendenciaAssinatura() ? null : SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, getUsuarioLogado(), false, true));
		getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getDocumentoAssinadoFacade().consultarTotalRegistroArquivoAssinados(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_TODOS, getArquivoAssinadoDigitalmente(), null, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getApresentarDocumentoPortalTransparenciaComPendenciaAssinatura() ? null : SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, getUsuarioLogado(), false, true));
		if (!Uteis.isAtributoPreenchido(getControleConsultaOtimizado().getListaConsulta())){
			 throw new Exception(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
	}
		
	public String consultarDiplomasCertificados() {
		try {
			executarValidacaoParametroConsultaDiploma();
			setListaControleLivroFolhaRecibo(getFacadeFactory().getControleLivroFolhaReciboFacade().consultarLivroFolhaReciboDiploma(getCampoConsultarDiploma(), getValorConsultarDiploma(), getUsuarioLogado()));
			if(!Uteis.isAtributoPreenchido(getListaControleLivroFolhaRecibo())){
				 throw new Exception(UteisJSF.internacionalizar("msg_erro_registronaocontrado"));
			}
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}
	
	
	public void executarValidacaoParametroConsultaDiploma() throws Exception {
		if ( getValorConsultarDiploma().length() < 1 ||
				getValorConsultarDiploma().length() < 2) {
			throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
		}
	}
		
	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultar();
	}
		
	public String getMascaraConsulta() {
		if (getCampoConsultarDiploma().equals("CPF")) {
			return "return mascara(this.form,'formCadastro:valorConsultaDiplomaCertificado','999.999.999-99',event)";
		}
		return Constantes.EMPTY;
	}
	
	public String getMascaraPainelControleCPF() {
			return "return mascara(this.form,'formVisualizarArquivoValidando:senhaLiberarArquivo','999.999.999-99',event)";
	}

	public List<SelectItem> getTipoConsultaComboDiplomaCertifiado(){
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("numeroProcesso", "Numero Processo"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("nomeAluno", "Nome Aluno"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("codigoValidacao", "Código Validação Diploma Digital"));
		return itens;
	}
	
	
	public void montarListaSelectItemTipoDeDocumentoDocumentacaoCurso() throws Exception {
		List<TipoDocumentoVO> resultadoConsulta = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorNome(Constantes.EMPTY, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		List<SelectItem> lista = UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome");
		setListaSelectItemTipoDeDocumento(lista);
	}
	
	
	public String getValorConsultarDiploma() {
		if(valorConsultarDiploma == null) {
			valorConsultarDiploma = Constantes.EMPTY;
		}
		return valorConsultarDiploma;
	}

	public String inicializarDadosArquivosInstitucionais() {
		try {
			setAbaAtiva("arquivosInstitucionais");
			setControleConsulta(new ControleConsulta());
			getControleConsulta().setCampoConsulta("documentoInstitucional");
			getControleConsulta().setValorConsulta("%%%");
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			setArquivoAssinadoDigitalmente(true);
			getControleConsultaOtimizado().setLimitePorPagina(10);
			executarValidacaoParametroConsultaVazio();
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getDocumentoAssinadoFacade().consultarArquivoAssinado(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_TODOS, getArquivoAssinadoDigitalmente(), null, null, getUsuarioLogado(), false, true));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getDocumentoAssinadoFacade().consultarTotalRegistroArquivoAssinados(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_TODOS, getArquivoAssinadoDigitalmente(), null, null, getUsuarioLogado(), false, true));
			getControleConsulta().setValorConsulta("");
			getControleConsulta().setCampoConsulta("");
		} catch (Exception e) {
			getControleConsulta().setValorConsulta("");
			getControleConsulta().setCampoConsulta("");
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "consultar";
	}
	
	public String realizarDownload() {
		DocumentoAssinadoVO obj = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("documentoAssinado1");
		try {
			if (Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema().getCodigo())) {
				setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			} else {
				if (!Uteis.isAtributoPreenchido(getConfiguracaoGeralSistemaVO().getCodigo())) {
					setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema());
				}
			}
			if (obj.getProvedorDeAssinaturaEnum().equals(ProvedorDeAssinaturaEnum.CERTISIGN)) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
			}
			if (obj.getProvedorDeAssinaturaEnum().equals(ProvedorDeAssinaturaEnum.TECHCERT)) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
			}
			realizarDownloadArquivo(obj.getArquivo(), configuracaoGeralSistemaVO);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Constantes.EMPTY;
	}
	
	public String realizarDownloadRepresentacaoVisual() {
		DocumentoAssinadoVO obj = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("documentoAssinado1");
		try {
			if (Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema().getCodigo())) {
				setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			} else {
				if (!Uteis.isAtributoPreenchido(getConfiguracaoGeralSistemaVO().getCodigo())) {
					setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema());
				}
			}
			realizarDownloadArquivo(obj.getArquivoVisual(), configuracaoGeralSistemaVO);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Constantes.EMPTY;
	}
	
	public String getUrlDownloadSV() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return "location.href='../../DownloadSV'";
		}else {
			return "location.href='../DownloadSV'";
		}
	}
	
	public void limparListaValidacaoDocumentosAssinados() {
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsulta().setCampoConsulta("codigo");
		getControleConsulta().setValorConsulta(Constantes.EMPTY);
	}
	
	public void scrollerListenerDocumentoDigitaisAluno(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		realizarConsultaDocumentoAssinadoVisaoAluno();
	}
	
	public String realizarInicializacaoDocumentoDigitaisAluno()  {
		try {
			if(getVisaoAlunoControle() != null) {
				getControleConsulta().setCampoConsulta("matricula"); 
				getControleConsulta().setValorConsulta(getVisaoAlunoControle().getMatricula().getMatricula());
				getControleConsultaOtimizado().setLimitePorPagina(10);
				realizarConsultaDocumentoAssinadoVisaoAluno();
			}
			setMensagemID(MSG_TELA.msg_dados_consultados.name());		
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoAluno/documentosDigitaisCons.xhtml");
	}

	private void realizarConsultaDocumentoAssinadoVisaoAluno() throws Exception {
		List<TipoOrigemDocumentoAssinadoEnum> listaTipoOrigemDocumentoAssinadoEnum = realizarVerificacaoPermissaoTipoOrigemDocumentoAluno();
		if(Uteis.isAtributoPreenchido(listaTipoOrigemDocumentoAssinadoEnum)) {			
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getDocumentoAssinadoFacade().consultarArquivoAssinado(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_TODOS, getArquivoAssinadoDigitalmente(), listaTipoOrigemDocumentoAssinadoEnum, SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, getUsuarioLogado(), true, false));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getDocumentoAssinadoFacade().consultarTotalRegistroArquivoAssinados(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getArquivoAssinadoDigitalmente(), listaTipoOrigemDocumentoAssinadoEnum, SituacaoDocumentoAssinadoPessoaEnum.ASSINADO,  getUsuarioLogado(), true, false));	
		}
	}
	
	public List<TipoOrigemDocumentoAssinadoEnum> realizarVerificacaoPermissaoTipoOrigemDocumentoAluno(){		
		return getFacadeFactory().getUsuarioFacade().realizarVerificacaoPermissaoTipoOrigemDocumentoAluno(getUsuarioLogado());
	}
	
	public boolean isPermitirDocumentoDigitaisBoletimAcademico()  {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_BOLETIM_ACADEMICO, getUsuarioLogado());		
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isPermitirDocumentoDigitaisDeclaracao()  {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_DECLARACAO, getUsuarioLogado());		
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isPermitirDocumentoDigitaisHistorico()  {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_HISTORICO, getUsuarioLogado());		
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isPermitirDocumentoDigitaisCertificado()  {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_CERTIFICADOS, getUsuarioLogado());		
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isPermitirDocumentoDigitaisDiploma()  {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_DIPLOMAS, getUsuarioLogado());		
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	
	
	

	public void setValorConsultarDiploma(String valorConsultarDiploma) {
		this.valorConsultarDiploma = valorConsultarDiploma;
	}

	public Boolean getArquivoAssinadoDigitalmente() {
		if (arquivoAssinadoDigitalmente == null) {
			arquivoAssinadoDigitalmente = true;
		}
		return arquivoAssinadoDigitalmente;
	}
	
	public String getCampoConsultarDiploma() {
		if(campoConsultarDiploma == null) {
			campoConsultarDiploma = Constantes.EMPTY;
		}
		return campoConsultarDiploma;
	}

	public void setCampoConsultarDiploma(String campoConsultarDiploma) {
		this.campoConsultarDiploma = campoConsultarDiploma;
	}

	public List<SelectItem> getListaSelectItemTipoDeDocumento() {
		if(listaSelectItemTipoDeDocumento == null) {
			listaSelectItemTipoDeDocumento = new ArrayList<SelectItem>();
		}
		return listaSelectItemTipoDeDocumento;
	}

	public void setListaSelectItemTipoDeDocumento(List<SelectItem> listaSelectItemTipoDeDocumento) {
		this.listaSelectItemTipoDeDocumento = listaSelectItemTipoDeDocumento;
	}
	
	public List<ControleLivroFolhaReciboVO> getListaControleLivroFolhaRecibo() {
		if(listaControleLivroFolhaRecibo == null) {
			listaControleLivroFolhaRecibo = new ArrayList<ControleLivroFolhaReciboVO>();
		}
		return listaControleLivroFolhaRecibo;
	}

	public void setListaControleLivroFolhaRecibo(List<ControleLivroFolhaReciboVO> listaControleLivroFolhaRecibo) {
		this.listaControleLivroFolhaRecibo = listaControleLivroFolhaRecibo;
	}

	public Integer getTotalElemento() {
		if(Uteis.isAtributoPreenchido(getListaControleLivroFolhaRecibo()) 
				&& getListaControleLivroFolhaRecibo().size() > 9) {
			return 9;
		}
		return getListaControleLivroFolhaRecibo().size();
	}
	
	public Integer getTotalColuna() {
		if(Uteis.isAtributoPreenchido(getListaControleLivroFolhaRecibo()) 
				&& getListaControleLivroFolhaRecibo().size() > 2) {
			return 2;
		}
		return getListaControleLivroFolhaRecibo().size();
	}

	public void setArquivoAssinadoDigitalmente(Boolean arquivoAssinadoDigitalmente) {
		this.arquivoAssinadoDigitalmente = arquivoAssinadoDigitalmente;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}
	
	public void limparControleConsulta() {
		if (!getControleConsulta().getCampoConsulta().equals("codigo")) {
			getControleConsulta().setValorConsulta(Constantes.EMPTY);
			anularDataModelo();
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
		}
	}
	
	public void realizarDownloadArquivoPDF() {
		DocumentoAssinadoVO obj = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("documentoAssinado1");
		try {
			if (Uteis.isAtributoPreenchido(getConfiguracaoGeralPadraoSistema().getCodigo())) {
				setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			} else {
				if (!Uteis.isAtributoPreenchido(getConfiguracaoGeralSistemaVO().getCodigo())) {
					setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema());
				}
			}
			realizarDownloadArquivo(obj.getArquivo(), configuracaoGeralSistemaVO);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<DocumentoAssinadoVO> getListaDocumentoAssinadoVO() {
		if (listaDocumentoAssinadoVO == null) {
			listaDocumentoAssinadoVO = new ArrayList<DocumentoAssinadoVO>();
		}
		return listaDocumentoAssinadoVO;
	}

	public void setListaDocumentoAssinadoVO(List<DocumentoAssinadoVO> listaDocumentoAssinadoVO) {
		this.listaDocumentoAssinadoVO = listaDocumentoAssinadoVO;
	}
	
	public String getSenhaLiberarHistorico() {
		if (senhaLiberarHistorico == null) {
			senhaLiberarHistorico = "";
		}
		return senhaLiberarHistorico;
	}

	public void setSenhaLiberarHistorico(String senhaLiberarHistorico) {
		this.senhaLiberarHistorico = senhaLiberarHistorico;
	}
	
	public void modalVisualizarArquivoAbaValidacao() {
		try {
			setOncompleteModal("");
			setSenhaLiberarArquivo("");
			setArquivoApresentar((DocumentoAssinadoVO) getRequestMap().get("documentoAssinado1"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void visualizarArquivoValidandoCpf() {
		try {
			setOncompleteModal("");
			if (!Uteis.retirarMascaraCPF(getSenhaLiberarArquivo()).equals(Uteis.retirarMascaraCPF(getArquivoApresentar().getMatricula().getAluno().getCPF()))) {
				cpfValidado = false;
				if(getArquivoApresentar().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO)) {
				throw new Exception("O CPF informado não é o mesmo vinculado ao Histórico selecionado.");
				} else if (getArquivoApresentar().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO)) {
					throw new Exception("O CPF informado não é o mesmo vinculado ao Boletim selecionado.");
				} else if (getArquivoApresentar().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DECLARACAO)) {
					throw new Exception("O CPF informado não é o mesmo vinculado à Declaração selecionada.");
				} else if (getArquivoApresentar().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO)) {
					throw new Exception("O CPF informado não é o mesmo vinculado ao Requerimento selecionado.");
				} else if (getArquivoApresentar().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO)) {
					throw new Exception("O CPF informado não é o mesmo vinculado ao Contrato selecionado.");
				} else if (getArquivoApresentar().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO)) {
					throw new Exception("O CPF informado não é o mesmo vinculado ao Certificado selecionado.");
				} else if (getArquivoApresentar().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA)) {
					throw new Exception("O CPF informado não é o mesmo vinculado à Expedição de Diploma selecionado.");
				} 
			}
			cpfValidado = true;
			context().getExternalContext().getSessionMap().put("urlTelaAtual", getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + getNomeTelaAtual());
				ArquivoVO arquivo = getFacadeFactory().getArquivoFacade().consultarPorCodigo(getArquivoApresentar().getArquivo().getCodigo(), 0, getUsuarioLogado());
				realizarVisualizacaoPreview(arquivo);				
				setOncompleteModal("RichFaces.$('panelPreview').show();RichFaces.$('panelVisualizarArquivoValidandoCpf').hide();");
				setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setSenhaLiberarArquivo("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void visualizarArquivo() {
		try {
			context().getExternalContext().getSessionMap().put("urlTelaAtual", getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + getNomeTelaAtual());
				ArquivoVO arquivo = getFacadeFactory().getArquivoFacade().consultarPorCodigo(getArquivoApresentar().getArquivo().getCodigo(), 0, getUsuarioLogado());
				realizarVisualizacaoPreview(arquivo);				
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setSenhaLiberarArquivo("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public DocumentoAssinadoVO getArquivoApresentar() {
		if(arquivoApresentar == null) {
			arquivoApresentar = new DocumentoAssinadoVO();
		}
		return arquivoApresentar;
	}

	public void setArquivoApresentar(DocumentoAssinadoVO arquivoApresentar) {
		this.arquivoApresentar = arquivoApresentar;
	}

	public Boolean getCpfValidado() {
		if(cpfValidado == null) {
			cpfValidado = Boolean.FALSE;
		}
		return cpfValidado;
	}

	public void setCpfValidado(Boolean cpfValidado) {
		this.cpfValidado = cpfValidado;
	}

	public String getSenhaLiberarArquivo() {
		if(senhaLiberarArquivo ==null) {
			senhaLiberarArquivo = "";
		}
		return senhaLiberarArquivo;
	}

	public void setSenhaLiberarArquivo(String senhaLiberarArquivo) {
		this.senhaLiberarArquivo = senhaLiberarArquivo;
	}

	public void visualizarArquivoInstituicao () {
		setOncompleteModal("");
		setSenhaLiberarArquivo("");
		setArquivoApresentar((DocumentoAssinadoVO) getRequestMap().get("documentoAssinado1"));
		setSenhaLiberarArquivo("");
		if (Uteis.isAtributoPreenchido(getArquivoApresentar())) {
			visualizarArquivoValidandoCpf();
		}
	}
	
	public String getCampoConsultarHistorico() {
		if (campoConsultarHistorico == null) {
			campoConsultarHistorico = "CPFHistoricoDigital";
		}
		return campoConsultarHistorico;
	}
	
	public void setCampoConsultarHistorico(String campoConsultarHistorico) {
		this.campoConsultarHistorico = campoConsultarHistorico;
	}
	
	public List<SelectItem> getTipoConsultaComboHistorico() {
		if (tipoConsultaComboHistorico == null) {
			tipoConsultaComboHistorico = new ArrayList<>();
			tipoConsultaComboHistorico.add(new SelectItem("CPFHistoricoDigital", "CPF"));
			tipoConsultaComboHistorico.add(new SelectItem("nomeAlunoHistoricoDigital", "Nome Aluno"));
			tipoConsultaComboHistorico.add(new SelectItem("codigoValidacaoHistoricoDigital", "Código Validação Histórico Digital"));
		}
		return tipoConsultaComboHistorico;
	}
	
	public void setTipoConsultaComboHistorico(List<SelectItem> tipoConsultaComboHistorico) {
		this.tipoConsultaComboHistorico = tipoConsultaComboHistorico;
	}
	
	public String getValorConsultarHistorico() {
		if (valorConsultarHistorico == null) {
			valorConsultarHistorico = Constantes.EMPTY;
		}
		return valorConsultarHistorico;
	}
	
	public void setValorConsultarHistorico(String valorConsultarHistorico) {
		this.valorConsultarHistorico = valorConsultarHistorico;
	}
	
	public String consultarHistoricos() {
		try {
			if (getValorConsultarHistorico().length() < 1 || getValorConsultarHistorico().length() < 2) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
			}
			List<TipoOrigemDocumentoAssinadoEnum> listaTipoOrigemDocumentoAssinadoEnum = new ArrayList<TipoOrigemDocumentoAssinadoEnum>();
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL);
			setListaDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarArquivoAssinado(getCampoConsultarHistorico(), getValorConsultarHistorico(), 0, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getArquivoAssinadoDigitalmente(), listaTipoOrigemDocumentoAssinadoEnum, null, getUsuarioLogado(), true, true));
			if (!Uteis.isAtributoPreenchido(getListaDocumentoAssinadoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_erro_registronaocontrado"));
			}
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}
	
	public String getMascaraConsultaHistorico() {
		if (getCampoConsultarHistorico().equals("CPFHistoricoDigital")) {
			return "return mascara(this.form,'formCadastro:valorConsultaHistorico','999.999.999-99',event)";
		}
		return Constantes.EMPTY;
	}
	
	public String getSenhaLiberarXMLDiploma() {
		if (senhaLiberarXMLDiploma == null) {
			senhaLiberarXMLDiploma = Constantes.EMPTY;
		}
		return senhaLiberarXMLDiploma;
	}
	
	public void setSenhaLiberarXMLDiploma(String senhaLiberarXMLDiploma) {
		this.senhaLiberarXMLDiploma = senhaLiberarXMLDiploma;
	}
	
	public String getMascaraPainelControleXMLDiploma() {
		return "return mascara(this.form,'formDownloadXML:senhaLiberarXMLDiploma','999.999.999-99',event)";
	}
	
	public ControleLivroFolhaReciboVO getDiplomaVisualizar() {
		if (diplomaVisualizar == null) {
			diplomaVisualizar = new ControleLivroFolhaReciboVO();
		}
		return diplomaVisualizar;
	}
	
	public void setDiplomaVisualizar(ControleLivroFolhaReciboVO diplomaVisualizar) {
		this.diplomaVisualizar = diplomaVisualizar;
	}
	
	public void realizarDownloadXMLDiploma() {
		try {
			setOncompleteModal("");
			if (!Uteis.retirarMascaraCPF(getSenhaLiberarXMLDiploma()).equals(Uteis.retirarMascaraCPF(getDiplomaVisualizar().getMatricula().getAluno().getCPF()))) {
				cpfValidado = false;
				throw new Exception("O CPF informado não é o mesmo vinculado ao Diploma selecionado.");
			}
			cpfValidado = true; 
			context().getExternalContext().getSessionMap().put("urlTelaAtual", getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + getNomeTelaAtual());
			if (getDiplomaVisualizar().getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL)) { 
				realizarDownloadArquivo(getDiplomaVisualizar().getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo());
			}
			setOncompleteModal("RichFaces.$('panelDownloadXMLDiploma').hide();");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setSenhaLiberarXMLDiploma("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String getSenhaLiberarDiploma() {
		if (senhaLiberarDiploma == null) {
			senhaLiberarDiploma = Constantes.EMPTY;
		}
		return senhaLiberarDiploma;
	}
	
	public void setSenhaLiberarDiploma(String senhaLiberarDiploma) {
		this.senhaLiberarDiploma = senhaLiberarDiploma;
	}
	
	public String getMascaraPainelControleArquivoDiploma() {
		return "return mascara(this.form,'formVisualizarArquivoDiploma:senhaLiberarDiploma','999.999.999-99',event)";
	}
	
	public void visualizarArquivoDiploma() {
		try {
			setOncompleteModal("");
			if (!Uteis.retirarMascaraCPF(getSenhaLiberarDiploma()).equals(Uteis.retirarMascaraCPF(getDiplomaVisualizar().getMatricula().getAluno().getCPF()))) {
				cpfValidado = false;
				throw new Exception("O CPF informado não é o mesmo vinculado ao Diploma selecionado.");
			}
			cpfValidado = true; 
			context().getExternalContext().getSessionMap().put("urlTelaAtual", getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + getNomeTelaAtual());
			if (getDiplomaVisualizar().getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL)) {
				ArquivoVO arquivo = getDiplomaVisualizar().getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual(); 
				realizarVisualizacaoPreview(arquivo);
			} else {
				realizarVisualizacaoPreview(getDiplomaVisualizar().getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo());
			}
			setOncompleteModal("RichFaces.$('panelPreview').show();RichFaces.$('panelVisualizarArquivoDiploma').hide();");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setSenhaLiberarDiploma("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarDownloadArquivoDiploma() {
		try {
			setOncompleteModal("");
			if (!Uteis.retirarMascaraCPF(getSenhaLiberarDiploma()).equals(Uteis.retirarMascaraCPF(getDiplomaVisualizar().getMatricula().getAluno().getCPF()))) {
				throw new Exception("O CPF informado não é o mesmo vinculado ao Diploma selecionado.");
			}
			context().getExternalContext().getSessionMap().put("urlTelaAtual", getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + getNomeTelaAtual());
			if (getDiplomaVisualizar().getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL)) {				
				realizarDownloadArquivo(getDiplomaVisualizar().getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual());
			} else {			
				realizarDownloadArquivo(getDiplomaVisualizar().getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo());
			}
			setOncompleteModal("RichFaces.$('panelVisualizarDiploma').hide();");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setSenhaLiberarDiploma("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String getMascaraPainelControleDiploma() {
		return "return mascara(this.form,'formVisualizarDiploma:senhaLiberarDiploma','999.999.999-99',event)";
	}
	
	public void modalVisualizarDiploma() {
		try {
			setOncompleteModal("");
			setSenhaLiberarDiploma("");
			setSenhaLiberarXMLDiploma("");
			setDiplomaVisualizar((ControleLivroFolhaReciboVO) getRequestMap().get("diplomaCertificado"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String getUrlDonloadSV() {
		if (Uteis.retirarMascaraCPF(getSenhaLiberarDiploma()).equals(Uteis.retirarMascaraCPF(getDiplomaVisualizar().getMatricula().getAluno().getCPF())) || Uteis.retirarMascaraCPF(getSenhaLiberarXMLDiploma()).equals(Uteis.retirarMascaraCPF(getDiplomaVisualizar().getMatricula().getAluno().getCPF()))) {
			if(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3.getValor())) {
				return "abrirPopup('"+getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao()+"/DownloadSV', 'DownloadSV_"+ new Date().getTime() +"', 950,595)";
			} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				if (getNomeTelaAtual().contains("diploma.xhtml") || getNomeTelaAtual().contains("historico.xhtml")) {
					return "location.href='../../../DownloadSV'";
				}
				return "location.href='../../DownloadSV'";
			} else {
				if (getNomeTelaAtual().contains("diploma.xhtml") || getNomeTelaAtual().contains("historico.xhtml")) {
					return "location.href='../../../DownloadSV'";
				} else {
					return "location.href='../DownloadSV'";
				}
			}
		}
		return "";
	}
	
	public String consultarCurriculos() {
		try {
			if (getValorConsultarCurriculo().length() < 1 || getValorConsultarCurriculo().length() < 2) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
			}
			List<TipoOrigemDocumentoAssinadoEnum> listaTipoOrigemDocumentoAssinadoEnum = new ArrayList<TipoOrigemDocumentoAssinadoEnum>();
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL);
			setListaDocumentoAssinadoCurriculoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarArquivoAssinado(getCampoConsultarCurriculo(), getValorConsultarCurriculo(), 0, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getArquivoAssinadoDigitalmente(), listaTipoOrigemDocumentoAssinadoEnum, null, getUsuarioLogado(), true, true));
			if (!Uteis.isAtributoPreenchido(getListaDocumentoAssinadoCurriculoVO())) {
				throw new Exception(UteisJSF.internacionalizar("msg_erro_registronaocontrado"));
			}
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}
	
	public String getValorConsultarCurriculo() {
		if (valorConsultarCurriculo == null) {
			valorConsultarCurriculo = Constantes.EMPTY;
		}
		return valorConsultarCurriculo;
	}

	public void setValorConsultarCurriculo(String valorConsultarCurriculo) {
		this.valorConsultarCurriculo = valorConsultarCurriculo;
	}

	public String getCampoConsultarCurriculo() {
		if (campoConsultarCurriculo == null) {
			campoConsultarCurriculo = Constantes.EMPTY;
		}
		return campoConsultarCurriculo;
	}

	public void setCampoConsultarCurriculo(String campoConsultarCurriculo) {
		this.campoConsultarCurriculo = campoConsultarCurriculo;
	}
	
	public List<SelectItem> getTipoConsultaComboCurriculo() {
		if (tipoConsultaComboCurriculo == null) {
			tipoConsultaComboCurriculo = new ArrayList<>(0);
			tipoConsultaComboCurriculo.add(new SelectItem("matrizCurricularCurriculo", "Matriz Curricular"));
			tipoConsultaComboCurriculo.add(new SelectItem("cursoCurriculo", "Curso"));
			tipoConsultaComboCurriculo.add(new SelectItem("unidadeEnsinoCurriculo", "Unidade de Ensino"));
			tipoConsultaComboCurriculo.add(new SelectItem("codigoValidacaoCurriculo", "Código Validação Curriculo Digital"));
		}
		return tipoConsultaComboCurriculo;
	}
	
	public void setTipoConsultaComboCurriculo(List<SelectItem> tipoConsultaComboCurriculo) {
		this.tipoConsultaComboCurriculo = tipoConsultaComboCurriculo;
	}
	
	public List<DocumentoAssinadoVO> getListaDocumentoAssinadoCurriculoVO() {
		if (listaDocumentoAssinadoCurriculoVO == null) {
			listaDocumentoAssinadoCurriculoVO = new ArrayList<>(0);
		}
		return listaDocumentoAssinadoCurriculoVO;
	}
	
	public void setListaDocumentoAssinadoCurriculoVO(List<DocumentoAssinadoVO> listaDocumentoAssinadoCurriculoVO) {
		this.listaDocumentoAssinadoCurriculoVO = listaDocumentoAssinadoCurriculoVO;
	}
	
	public void realizarDownloadArquivoXmlCurriculoEscolar() {
		try {
			DocumentoAssinadoVO obj = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("curriculoItesn");
			context().getExternalContext().getSessionMap().put("urlTelaAtual", getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + getNomeTelaAtual());
			realizarDownloadArquivo(obj.getArquivo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setSenhaLiberarDiploma("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarPreviewCurriculoEscolarDigital() {
		DocumentoAssinadoVO obj = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("curriculoItesn");
		try {
			setCaminhoPreview(getFacadeFactory().getDocumentoAssinadoFacade().realizarGeracaoPreviewRepresentacaoVisual(obj, getConfiguracaoGeralPadraoSistema()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String getMascaraPainelControleArquivoHistorico() {
		return "return mascara(this.form,'formVisualizarArquivoHistorico:senhaLiberarHistorico','999.999.999-99',event)";
	}
	
	public DocumentoAssinadoVO getHistoricoVisualizar() {
		if (historicoVisualizar == null) {
			historicoVisualizar = new DocumentoAssinadoVO();
		}
		return historicoVisualizar;
	}
	
	public void setHistoricoVisualizar(DocumentoAssinadoVO historicoVisualizar) {
		this.historicoVisualizar = historicoVisualizar;
	}
	
	public String getSenhaLiberarXMLHistorico() {
		if (senhaLiberarXMLHistorico == null) {
			senhaLiberarXMLHistorico = Constantes.EMPTY;
		}
		return senhaLiberarXMLHistorico;
	}
	
	public void setSenhaLiberarXMLHistorico(String senhaLiberarXMLHistorico) {
		this.senhaLiberarXMLHistorico = senhaLiberarXMLHistorico;
	}
	
	public String getSenhaLiberarArquivoHistorico() {
		if (senhaLiberarArquivoHistorico == null) {
			senhaLiberarArquivoHistorico = Constantes.EMPTY;
		}
		return senhaLiberarArquivoHistorico;
	}
	
	public void setSenhaLiberarArquivoHistorico(String senhaLiberarArquivoHistorico) {
		this.senhaLiberarArquivoHistorico = senhaLiberarArquivoHistorico;
	}
	
	public void modalVisualizarArquivoHistorico() {
		try {
			setOncompleteModal("");
			setSenhaLiberarArquivoHistorico("");
			setHistoricoVisualizar((DocumentoAssinadoVO) getRequestMap().get("historicoItens"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void modalVisualizarHistorico() {
		try {
			setOncompleteModal("");
			setSenhaLiberarHistorico("");
			setSenhaLiberarXMLHistorico("");
			setHistoricoVisualizar((DocumentoAssinadoVO) getRequestMap().get("historicoItens"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void visualizarArquivoHistorico() {
		try {
			setOncompleteModal("");
			if (!Uteis.retirarMascaraCPF(getSenhaLiberarHistorico()).equals(Uteis.retirarMascaraCPF(getHistoricoVisualizar().getMatricula().getAluno().getCPF()))) {
				cpfValidado = false;
				throw new Exception("O CPF informado não é o mesmo vinculado ao Histórico selecionado.");
			}
			cpfValidado = true; 			
			context().getExternalContext().getSessionMap().put("urlTelaAtual", getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + getNomeTelaAtual());
			if (getHistoricoVisualizar().getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL)) {
				realizarVisualizacaoPreview(getHistoricoVisualizar().getArquivoVisual());
			}
			setOncompleteModal("RichFaces.$('panelPreview').show();RichFaces.$('panelVisualizarArquivoHistorico').hide();");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setSenhaLiberarHistorico("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarDownloadArquivoHistorico() {
		try {
			setOncompleteModal("");
			if (!Uteis.retirarMascaraCPF(getSenhaLiberarHistorico()).equals(Uteis.retirarMascaraCPF(getHistoricoVisualizar().getMatricula().getAluno().getCPF()))) {
				throw new Exception("O CPF informado não é o mesmo vinculado ao Histórico selecionado.");
			}			
			context().getExternalContext().getSessionMap().put("urlTelaAtual", getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + getNomeTelaAtual());
			realizarDownloadArquivo(getHistoricoVisualizar().getArquivoVisual());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
			setOncompleteModal("RichFaces.$('panelVisualizarHistorico').hide()");
		} catch (Exception e) {
			setSenhaLiberarHistorico("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarDownloadXMLHistorico() {
		try {
			setOncompleteModal("");
			if (!Uteis.retirarMascaraCPF(getSenhaLiberarXMLHistorico()).equals(Uteis.retirarMascaraCPF(getHistoricoVisualizar().getMatricula().getAluno().getCPF()))) {
				throw new Exception("O CPF informado não é o mesmo vinculado ao Histórico selecionado.");
			}
			context().getExternalContext().getSessionMap().put("urlTelaAtual", getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + getNomeTelaAtual());
			realizarDownloadArquivo(getHistoricoVisualizar().getArquivo());
			setOncompleteModal("RichFaces.$('panelDownloadXMLHistorico').hide();");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setSenhaLiberarXMLHistorico("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String getUrlHistoricoDownloadSV() {
		if (Uteis.retirarMascaraCPF(getSenhaLiberarHistorico()).equals(Uteis.retirarMascaraCPF(getHistoricoVisualizar().getMatricula().getAluno().getCPF())) ||
			Uteis.retirarMascaraCPF(getSenhaLiberarXMLHistorico()).equals(Uteis.retirarMascaraCPF(getHistoricoVisualizar().getMatricula().getAluno().getCPF()))) {
			if(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3.getValor())) {
				return "abrirPopup('"+getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao()+"/DownloadSV', 'DownloadSV_"+ new Date().getTime() +"', 950,595)";
			} else if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				if (getNomeTelaAtual().contains("historico.xhtml")) {
					return "location.href='../../../DownloadSV'";
				}
				return "location.href='../../DownloadSV'";
			} else {
				if (getNomeTelaAtual().contains("historico.xhtml")) {
					return "location.href='../../../DownloadSV'";
				} else {
					return "location.href='../DownloadSV'";
				}
			}
		}
		return "";
	}
	
	public String getMascaraPainelControleHistorico() {
		return "return mascara(this.form,'formVisualizarHistorico:senhaLiberarHistorico','999.999.999-99',event)";
	}
}
