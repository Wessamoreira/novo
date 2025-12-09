package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle.MSG_TELA;
import jobs.JobExecutarSincronismoComLdapAoCancelarTransferirMatricula;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.InclusaoDisciplinasHistoricoForaPrazoVO;
import negocio.comuns.academico.InclusaoHistoricoForaPrazoVO;
import negocio.comuns.academico.LogImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.TurmaContratoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoTextoPadrao;
import negocio.facade.jdbc.academico.DocumentoAssinado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * 
 * @author Carlos
 */
@Controller("ImpressaoContratoControle")
@Scope("viewScope")
@Lazy
public class ImpressaoContratoControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = -4821188639498349890L;
	private ImpressaoContratoVO impressaoContratoVO;
	private List<ImpressaoContratoVO> impressaoContratoVOs;
	private String tipoContrato;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private Boolean imprimirContrato;
	private Boolean adicionarAssinatura;
	private TurmaVO turma;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private String ano;
	private String semestre;
	private String campoFiltroPor;
	private MatriculaVO matricula;
	private List<InclusaoHistoricoForaPrazoVO> inclusaoHistoricoForaPrazoVOs;
	private Boolean abrirPanelInclusaoHistoricoForaPrazo;
	private List listaSelectItemTextoPadrao;
	private InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO;
	private Date dataInicio;
	private Date dataFim;
	private String oncompleteModal;
	private Boolean habilitadoAssinarEletronicamente;
	private DocumentoAssinadoVO documentoAssinadoExcluir;
	private Integer quantidadeDocumentoAssinadoMatriculaPeriodo;
	private Boolean rejeitarContratosPendentesEmitidos;
	private List<DocumentoAssinadoVO> documentoAssinadoMatriculaPeriodo;
	private CursoVO curso;
	private List<CursoVO> listaConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private ProgressBarVO progressBarVO;
	private Boolean realizandoImpressaoLote;
	private String tipoAluno;
	private List<ImpressaoContratoVO> listaErroEnvioLote;
	private Boolean apresentarModalErroEnvioLote;
	private ProvedorDeAssinaturaEnum provedorDeAssinaturaLoteEnum;
	private String situacaoContratoDigital;
	
	public void consultarDadosGeracaoRelatorio() {
		try {
			setImpressaoContratoVOs(getFacadeFactory().getImpressaoContratoFacade().consultarDadosGeracaoContrato(getMatricula(), getCampoFiltroPor(), getTurma(), getSemestre(), getAno(), getUnidadeEnsinoLogado().getCodigo(), Optional.ofNullable(getDataInicio()), Optional.ofNullable(getDataFim()), getUsuarioLogado(), getCurso(), getTipoAluno(),getSituacaoContratoDigital()));
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setImpressaoContratoVO(new ImpressaoContratoVO());
		}
	}
	

	@PostConstruct
	public void realizarCarregamentoMatriculaVindoTelaFichaAluno() {
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaFichaAluno");
		if (matriculaVO != null && !matriculaVO.getMatricula().equals("")) {
			try {
				getImpressaoContratoVO().setMatriculaVO(matriculaVO);
				consultarAlunoPorMatricula();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaFichaAluno");
			}
			
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			setRealizandoImpressaoLote(false);
			setImpressaoContratoVOs(getFacadeFactory().getImpressaoContratoFacade().consultarDadosGeracaoContrato(getImpressaoContratoVO().getMatriculaVO(), getCampoFiltroPor(), getTurma(), getSemestre(), getAno(), getUnidadeEnsinoLogado().getCodigo(), Optional.ofNullable(null), Optional.ofNullable(null), getUsuarioLogado(), getCurso(), getTipoAluno() ,Constantes.EMPTY));
			if(Uteis.isAtributoPreenchido(getImpressaoContratoVOs()) && getImpressaoContratoVOs().get(0).getMatriculaVO().getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			if(!getImpressaoContratoVOs().isEmpty()){
				getImpressaoContratoVO().setListaLogImpressaoContratoVO(getImpressaoContratoVOs().get(0).getListaLogImpressaoContratoVO());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getImpressaoContratoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setImpressaoContratoVO(new ImpressaoContratoVO());
		}
	}
	
	
	public void consultarAlunoPorRegistroAcademico() {		
		try {
			MatriculaVO  objAluno = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorRegistroAcademico(getImpressaoContratoVO().getMatriculaVO().getAluno().getRegistroAcademico(), this.getUnidadeEnsinoLogado().getCodigo(), 0,  Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(this.getUnidadeEnsinoLogado().getCodigo()), getUsuarioLogado());
			if(objAluno == null) {
				throw new Exception("Dados não encontrado matrícula");
			}
			getImpressaoContratoVO().setMatriculaVO(objAluno);
			setImpressaoContratoVOs(getFacadeFactory().getImpressaoContratoFacade().consultarDadosGeracaoContrato(getImpressaoContratoVO().getMatriculaVO(), getCampoFiltroPor(), getTurma(), getSemestre(), getAno(), getUnidadeEnsinoLogado().getCodigo(), Optional.ofNullable(null), Optional.ofNullable(null), getUsuarioLogado(), getCurso(), getTipoAluno(),Constantes.EMPTY));
			if(getImpressaoContratoVOs().get(0).getMatriculaVO().getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			if(!getImpressaoContratoVOs().isEmpty()){
				getImpressaoContratoVO().setListaLogImpressaoContratoVO(getImpressaoContratoVOs().get(0).getListaLogImpressaoContratoVO());
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getImpressaoContratoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setImpressaoContratoVO(new ImpressaoContratoVO());
		}
	}

	public void novo() {
	}

	public void consultarTurmaPorIdentificador() {
		try {
			setTurma(getTurma().getIdentificadorTurma().trim().equals("") ? null : getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurma(), getTurma().getIdentificadorTurma().trim(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setAno(getApresentarAno() ? Uteis.getAnoDataAtual4Digitos() : "");
			setSemestre(getApresentarSemestre() ? Uteis.getSemestreAtual() : "");
			setDataInicio(null);
			setDataFim(null);
		} catch (Exception e) {
			setTurma(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarTurma() {
		try {
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
//				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoIdentificadorTurma(getUnidadeEnsinoLogado().getCodigo(), getValorConsultaTurma(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				getListaConsultaTurma().stream().map(TurmaVO::getCurso).filter(Uteis::isAtributoPreenchido).forEach(this::setNomeCurso);
				getListaConsultaTurma().sort(Comparator.comparing(TurmaVO::getIdentificadorTurma));
			} else if (getCampoConsultaTurma().equals("nomeCurso")) {				
				List listaUnidadeEnsino = new ArrayList();
				if(Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado())) {
					listaUnidadeEnsino.add(getUnidadeEnsinoLogado());					
				}
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaTurma(), listaUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			if (Uteis.isAtributoPreenchido(obj)) {
				obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				setTurma(obj);
				setAno(getApresentarAno() ? Uteis.getAnoDataAtual4Digitos() : "");
				setSemestre(getApresentarSemestre() ? Uteis.getSemestreAtual() : "");
				setValorConsultaTurma("");
				setCampoConsultaTurma("");
				setDataInicio(null);
				setDataFim(null);
				getListaConsultaTurma().clear();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> tipoConsultaComboAluno;

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("registroAcademico", "Registro Acadêmico"));		
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAluno;
	}

	public List<SelectItem> tipoConsultaFiltroPor;

	public List<SelectItem> getTipoConsultaFiltroPor() {
		if (tipoConsultaFiltroPor == null) {
			tipoConsultaFiltroPor = new ArrayList<SelectItem>(0);
			tipoConsultaFiltroPor.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaFiltroPor.add(new SelectItem("registroAcademico", "Registro Acadêmico"));		
			tipoConsultaFiltroPor.add(new SelectItem("turma", "Turma"));
			tipoConsultaFiltroPor.add(new SelectItem("curso", "Curso"));
		}
		return tipoConsultaFiltroPor;
	}

	public void consultarAluno() {
		try {

			setListaConsultaAluno(getFacadeFactory().getImpressaoContratoFacade().consultarAluno(getValorConsultaAluno(), getCampoConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() {
		try {
			setRealizandoImpressaoLote(false);
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			getImpressaoContratoVO().setMatriculaVO(obj);
			setMatricula(obj);
			setSituacaoContratoDigital("");
			consultarDadosGeracaoRelatorio();
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarLogImpressaoContrato() {
		try {

			getFacadeFactory().getLogImpressaoContratoFacade().alterarPorImpressaoContrato(getImpressaoContratoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosAluno() {
		setImpressaoContratoVO(null);
		setMatricula(null);
		setImpressaoContratoVOs(null);
		setImprimirContrato(false);
	}

	public void limparDadosTurma() {
		setImpressaoContratoVO(null);
		setTurma(null);
		setImpressaoContratoVOs(null);
		setImprimirContrato(false);
	}

	public List<SelectItem> getListaSelectItemTipoTextoPadrao() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoTextoPadrao.class, false);
	}

	public void inicializarDados() {
		limparDadosAluno();
		limparDadosTurma();
		limparDadosCurso();
	}

	public void visualizarContratoImpresso() {
		if(getCampoFiltroPor().equals("turma")){
			setImpressaoContratoVO(new ImpressaoContratoVO());
			setImpressaoContratoVO((ImpressaoContratoVO) getRequestMap().get("impressaoContratoItens"));
		}
		if(getCampoFiltroPor().equals("curso") || getCampoFiltroPor().equals("turma")) {
			try {
				getImpressaoContratoVO().setListaLogImpressaoContratoVO(getFacadeFactory().getLogImpressaoContratoFacade().consultarPorMatricula(getImpressaoContratoVO().getMatriculaVO().getMatricula(), getUsuarioLogado()));
			} catch (Exception e) {			
				e.printStackTrace();
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}		
	}
	
	public List<SelectItem> getComboboxProvedorAssinaturaPadrao(){
		Integer codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()) ?
				getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo() : 0;
		if(!Uteis.isAtributoPreenchido(codigoUnidadeEnsino)){
			setProvedorDeAssinaturaEnum(null);
			return new ArrayList<SelectItem>();
		}
		return this.getComboboxProvedorAssinaturaPadrao(codigoUnidadeEnsino, TipoOrigemDocumentoAssinadoEnum.CONTRATO);
	}

	public void imprimirContrato() {
		try {
			ImpressaoContratoVO impressaoContratoVO = ((ImpressaoContratoVO) getRequestMap().get("impressaoContratoItens"));
			setImprimirContrato(false);
			setFazerDownload(false);
			setCaminhoRelatorio("");
			getDocumentoAssinadoMatriculaPeriodo().clear();
			setQuantidadeDocumentoAssinadoMatriculaPeriodo(null);
			impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO);
			impressaoContratoVO.setGerarNovoArquivoAssinado(true);
			setImpressaoContratoVO(impressaoContratoVO);
			setAbrirPanelInclusaoHistoricoForaPrazo(false);
			if (getTipoContrato().equals("IR")) {
				setMensagemID("");
				setAbrirPanelInclusaoHistoricoForaPrazo(true);
				setInclusaoHistoricoForaPrazoVOs(getFacadeFactory().getInclusaoHistoricoForaPrazoFacade().consultaRapidaPorMatricula(getImpressaoContratoVO().getMatriculaVO().getMatricula(), false, null, null, getUsuarioLogado()));
				return;
			}
			if(getTipoContrato().equals("MA") || getTipoContrato().equals("EX") || getTipoContrato().equals("FI")) {
				setTipoContratoMatricula(TipoContratoMatriculaEnum.getEnumPorValor(getTipoContrato()));
				realizarCarregamentoPossiveisContratos();
				setHabilitadoAssinarEletronicamente(validarAssinaturaDigitalHabilitada(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoMatricula().getCodigo()));
				getComboboxProvedorAssinaturaPadrao();
			}else {
				setAbrirModalSelecaoContrato(false);
			}
			if(!getAbrirModalSelecaoContrato() && !getHabilitadoAssinarEletronicamente()) {
				realizarImpressaoContrato();		
			}
			if (Uteis.isAtributoPreenchido(impressaoContratoVO.getMatriculaPeriodoVO().getListaDocumentoAssinadoVO())) {
				setQuantidadeDocumentoAssinadoMatriculaPeriodo(impressaoContratoVO.getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().size());
				getDocumentoAssinadoMatriculaPeriodo().addAll(impressaoContratoVO.getMatriculaPeriodoVO().getListaDocumentoAssinadoVO());
			}
		} catch (Exception ex) {
			setImprimirContrato(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	
	public void realizarImpressaoContrato() throws Exception {
		try {
		setCaminhoRelatorio(getFacadeFactory().getImpressaoContratoFacade().imprimirContrato(getTipoContrato(), getImpressaoContratoVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado().getCodigo() ==  0 ? getProgressBarVO().getUsuarioVO() : getUsuarioLogado()));
		if(Uteis.isAtributoPreenchido(getCaminhoRelatorio()) && !getImpressaoContratoVO().isImpressaoPdf()){
			setImprimirContrato(true);
			setFazerDownload(false);
		}
		if(Uteis.isAtributoPreenchido(getCaminhoRelatorio()) && getImpressaoContratoVO().isImpressaoPdf()){
			setFazerDownload(true);	
			setImprimirContrato(false);
		}		
		getMatricula().setMatricula(impressaoContratoVO.getMatriculaVO().getMatricula());
//		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
//		LogImpressaoContratoVO obj = new LogImpressaoContratoVO();
//		obj.setDataGeracao(new Date());
//		obj.setAssinado(Boolean.FALSE);
//		obj.setMatricula(impressaoContratoVO.getMatriculaVO());
//		obj.setTipoContrato(getTipoContrato());
//		obj.setTextoPadrao((TextoPadraoVO) request.getSession().getAttribute("textoPadrao"));
//		obj.setImpressaoContrato(impressaoContratoVO);
//		obj.setUsuarioRespImpressao(getUsuarioLogadoClone());
//		getFacadeFactory().getLogImpressaoContratoFacade().incluir(obj, getUsuarioLogado());
//		impressaoContratoVO.getListaLogImpressaoContratoVO().add(obj);
		setAbrirModalSelecaoContrato(false);
		setPermitirAlterarContratoMatricula(false);
		impressaoContratoVO.setListaPlanoEnsino(new ArrayList<PlanoEnsinoVO>());
		setMensagemID("msg_impressaoContrato_contratoImpresso");
		}catch (Exception e) {
			setImprimirContrato(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
			throw e;
		}
	}
	
	public void impressaoContratoJaGerada(){
		try {
			LogImpressaoContratoVO obj = ((LogImpressaoContratoVO) getRequestMap().get("logImpressaoContratoItens"));
			getFacadeFactory().getArquivoHelper().disponibilizarArquivoAssinadoParaDowload(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + obj.getImpressaoContrato().getDocumentoAssinado().getArquivo().getPastaBaseArquivo() + File.separator + obj.getImpressaoContrato().getDocumentoAssinado().getArquivo().getNome(), obj.getImpressaoContrato().getDocumentoAssinado().getArquivo().getNome());
			setCaminhoRelatorio(obj.getImpressaoContrato().getDocumentoAssinado().getArquivo().getNome());
			setFazerDownload(true);	
			setImprimirContrato(false);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getContrato() {
		if(getAbrirModalSelecaoContrato()) {			
			return "RichFaces.$('panelSelecionarContrato').show()";
		}else if (getTipoContrato().equals("IR")) {
			return "RichFaces.$('panelInclusaoReposicao').show()";
		}else if (getImprimirContrato()) {
			return "abrirPopup('../../VisualizarContrato', '" + getMatricula().getMatricula() + "', 730, 545)";
		}else if (getRealizandoImpressaoLote()) {
			return "RichFaces.$('panelImpressaoLote').show()";
		}else if (getHabilitadoAssinarEletronicamente() && !getRealizandoImpressaoLote()) {
			return "RichFaces.$('panelAssinarDocumento').show()";
		}else if(getFazerDownload()){
			return getDownload();
		}
		return "";
	}

	public String getContratoInclusaoReposicao() {
		if (getImprimirContrato() && getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato().getTipoDesigneTextoEnum().isHtml() && !getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato().getCodigo().equals(0)) {
			return "abrirPopup('../../VisualizarContrato', '" + getMatricula().getMatricula() + "', 730, 545)";
		}
		if (getImprimirContrato() && getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato().getTipoDesigneTextoEnum().isPdf() && !getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato().getCodigo().equals(0)) {
			setFazerDownload(true);
			return getDownload();
		}
		if (!getImprimirContrato() && getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato().getCodigo().equals(0)) {
			return "RichFaces.$('panelAvisoTextoPadrao').show()";
		}
		return "";
	}


	public ImpressaoContratoVO getImpressaoContratoVO() {
		if (impressaoContratoVO == null) {
			impressaoContratoVO = new ImpressaoContratoVO();
		}
		return impressaoContratoVO;
	}

	public void setImpressaoContratoVO(ImpressaoContratoVO impressaoContratoVO) {
		this.impressaoContratoVO = impressaoContratoVO;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public String getTipoContrato() {
		if (tipoContrato == null) {
			tipoContrato = "";
		}
		return tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = Boolean.FALSE;
		}
		return imprimirContrato;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	/**
	 * @return the adicionarAssinatura
	 */
	public Boolean getAdicionarAssinatura() {
		if (adicionarAssinatura == null) {
			adicionarAssinatura = Boolean.FALSE;
		}
		return adicionarAssinatura;
	}

	/**
	 * @param adicionarAssinatura
	 *            the adicionarAssinatura to set
	 */
	public void setAdicionarAssinatura(Boolean adicionarAssinatura) {
		this.adicionarAssinatura = adicionarAssinatura;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getCampoFiltroPor() {
		if (campoFiltroPor == null) {
			campoFiltroPor = "matricula";
		}
		return campoFiltroPor;
	}

	public void setCampoFiltroPor(String campoFiltroPor) {
		this.campoFiltroPor = campoFiltroPor;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turmaVO) {
		this.turma = turmaVO;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public List<ImpressaoContratoVO> getImpressaoContratoVOs() {
		if (impressaoContratoVOs == null) {
			impressaoContratoVOs = new ArrayList<ImpressaoContratoVO>(0);
		}
		return impressaoContratoVOs;
	}

	public void setImpressaoContratoVOs(List<ImpressaoContratoVO> impressaoContratoVOs) {
		this.impressaoContratoVOs = impressaoContratoVOs;
	}

	public List<InclusaoHistoricoForaPrazoVO> getInclusaoHistoricoForaPrazoVOs() {
		if (inclusaoHistoricoForaPrazoVOs == null) {
			inclusaoHistoricoForaPrazoVOs = new ArrayList<InclusaoHistoricoForaPrazoVO>(0);
		}
		return inclusaoHistoricoForaPrazoVOs;
	}

	public void setInclusaoHistoricoForaPrazoVOs(List<InclusaoHistoricoForaPrazoVO> inclusaoHistoricoForaPrazoVOs) {
		this.inclusaoHistoricoForaPrazoVOs = inclusaoHistoricoForaPrazoVOs;
	}

	public Boolean getAbrirPanelInclusaoHistoricoForaPrazo() {
		if (abrirPanelInclusaoHistoricoForaPrazo == null) {
			abrirPanelInclusaoHistoricoForaPrazo = false;
		}
		return abrirPanelInclusaoHistoricoForaPrazo;
	}

	public void setAbrirPanelInclusaoHistoricoForaPrazo(Boolean abrirPanelInclusaoHistoricoForaPrazo) {
		this.abrirPanelInclusaoHistoricoForaPrazo = abrirPanelInclusaoHistoricoForaPrazo;
	}

	public void imprimirContratoInclusaoReposicao() {
		try {
			InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO = (InclusaoHistoricoForaPrazoVO) getRequestMap().get("inclusaoHistoricoForaPrazoItens");
			if (Uteis.isAtributoPreenchido(inclusaoHistoricoForaPrazoVO.getTextoPadraoContrato().getCodigo()))  {
				inclusaoHistoricoForaPrazoVO.setTextoPadraoContrato(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(inclusaoHistoricoForaPrazoVO.getTextoPadraoContrato().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setInclusaoHistoricoForaPrazoVO(null);
			setImprimirContrato(Boolean.FALSE);
			executarMontagemDadosMatriculaPeriodoImpressaoContrato(inclusaoHistoricoForaPrazoVO);
			if (!getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato().getCodigo().equals(0)) {
				setCaminhoRelatorio(getFacadeFactory().getImpressaoContratoFacade().imprimirContratoInclusaoReposicao(getImpressaoContratoVO(), getConfiguracaoFinanceiroPadraoSistema(), inclusaoHistoricoForaPrazoVO.getTextoPadraoContrato(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema()));
				if (getImpressaoContratoVO().getMatriculaPeriodoVO().getReposicao()) {
					if (getImpressaoContratoVO().getMatriculaPeriodoVO().getPlanoFinanceiroReposicaoVO().getTextoPadraoContratoVO().getCodigo().intValue() == 0) {
						setImprimirContrato(false);
						throw new Exception("Não existe um Contrato Padrão definido no Plano Financeiro do Curso!");
					} else {
						setImprimirContrato(true);
					}
				} else {
					setImprimirContrato(true);
				}
				getMatricula().setMatricula(getImpressaoContratoVO().getMatriculaVO().getMatricula());
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				LogImpressaoContratoVO obj = new LogImpressaoContratoVO();
				obj.setAssinado(false);
				obj.setMatricula(getImpressaoContratoVO().getMatriculaVO());
				obj.setTipoContrato(getTipoContrato());
				obj.setTextoPadrao((TextoPadraoVO) request.getSession().getAttribute("textoPadrao"));
				obj.setUsuarioRespImpressao(getUsuarioLogadoClone());
				getFacadeFactory().getLogImpressaoContratoFacade().incluir(obj, getUsuarioLogado());
				getImpressaoContratoVO().getListaLogImpressaoContratoVO().add(obj);
				setMensagemID("msg_impressaoContrato_contratoImpresso");
			}
		} catch (Exception ex) {
			setImprimirContrato(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void imprimirContratoInclusaoReposicaoSelecionandoTextoPadrao() {
		try {
			if (Uteis.isAtributoPreenchido(getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato().getCodigo()))  {
				getInclusaoHistoricoForaPrazoVO().setTextoPadraoContrato(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setCaminhoRelatorio(getFacadeFactory().getImpressaoContratoFacade().imprimirContratoInclusaoReposicao(getImpressaoContratoVO(), getConfiguracaoFinanceiroPadraoSistema(), getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema()));
			
			if (getImpressaoContratoVO().getMatriculaPeriodoVO().getReposicao()) {
				if (getImpressaoContratoVO().getMatriculaPeriodoVO().getPlanoFinanceiroReposicaoVO().getTextoPadraoContratoVO().getCodigo().intValue() == 0) {
					setImprimirContrato(false);
					throw new Exception("Não existe um Contrato Padrão definido no Plano Financeiro do Curso!");
				} else {
					setImprimirContrato(true);
				}
			} else {
				setImprimirContrato(true);
			}
			getMatricula().setMatricula(getImpressaoContratoVO().getMatriculaVO().getMatricula());
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			LogImpressaoContratoVO obj = new LogImpressaoContratoVO();
			obj.setAssinado(false);
			obj.setMatricula(getImpressaoContratoVO().getMatriculaVO());
			obj.setTipoContrato(getTipoContrato());
			obj.setTextoPadrao((TextoPadraoVO) request.getSession().getAttribute("textoPadrao"));
			obj.setUsuarioRespImpressao(getUsuarioLogadoClone());
			getFacadeFactory().getLogImpressaoContratoFacade().incluir(obj, getUsuarioLogado());
			getImpressaoContratoVO().getListaLogImpressaoContratoVO().add(obj);
			setMensagemID("msg_impressaoContrato_contratoImpresso");
		} catch (Exception ex) {
			setImprimirContrato(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}



	private void executarMontagemDadosMatriculaPeriodoImpressaoContrato(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO) throws Exception {
		inclusaoHistoricoForaPrazoVO.getMatriculaPeriodoVO().setReposicao(inclusaoHistoricoForaPrazoVO.getReposicao());
		if (!inclusaoHistoricoForaPrazoVO.getReposicao()) {
			inclusaoHistoricoForaPrazoVO.getMatriculaPeriodoVO().setInclusaoForaPrazo(true);
		}
		inclusaoHistoricoForaPrazoVO.getMatriculaPeriodoVO().setPlanoFinanceiroReposicaoVO(inclusaoHistoricoForaPrazoVO.getPlanoFinanceiroReposicaoVO());
		inclusaoHistoricoForaPrazoVO.getMatriculaPeriodoVO().setNumParcelasInclusaoForaPrazo(inclusaoHistoricoForaPrazoVO.getNrParcelas());
		inclusaoHistoricoForaPrazoVO.getMatriculaPeriodoVO().setValorTotalParcelaInclusaoForaPrazo(inclusaoHistoricoForaPrazoVO.getValorTotalParcela());
		inclusaoHistoricoForaPrazoVO.getMatriculaPeriodoVO().setDescontoReposicao(inclusaoHistoricoForaPrazoVO.getDesconto());
		inclusaoHistoricoForaPrazoVO.getMatriculaPeriodoVO().setDiaVencimentoInclusaoForaPrazo(inclusaoHistoricoForaPrazoVO.getDataVencimento());		

		inclusaoHistoricoForaPrazoVO.setListaInclusaoDisciplinasHistoricoForaPrazoVO(getFacadeFactory().getInclusaoDisciplinasHistoricoForaPrazoFacade().consultarPorInclusaoHistoricoForaPrazo(inclusaoHistoricoForaPrazoVO.getCodigo(), false, null));

		getImpressaoContratoVO().setMatriculaPeriodoVO(inclusaoHistoricoForaPrazoVO.getMatriculaPeriodoVO());
		getImpressaoContratoVO().getMatriculaPeriodoVO().getMatriculaPeriodoTumaDisciplinaVOs().clear();
		List listaFinal = new ArrayList();
		Iterator i = inclusaoHistoricoForaPrazoVO.getListaInclusaoDisciplinasHistoricoForaPrazoVO().iterator();
		while (i.hasNext()) {
			InclusaoDisciplinasHistoricoForaPrazoVO disc = (InclusaoDisciplinasHistoricoForaPrazoVO)i.next();
			MatriculaPeriodoTurmaDisciplinaVO mptd = new MatriculaPeriodoTurmaDisciplinaVO();
			mptd.setDisciplina(disc.getDisciplina());
			mptd.setAno(disc.getAno());
			mptd.setSemestre(disc.getSemestre());
			listaFinal.add(mptd);			
		}
		getImpressaoContratoVO().getMatriculaPeriodoVO().setMatriculaPeriodoTumaDisciplinaVOs(listaFinal);
		setInclusaoHistoricoForaPrazoVO(inclusaoHistoricoForaPrazoVO);
		if (getInclusaoHistoricoForaPrazoVO().getTextoPadraoContrato().getCodigo().equals(0)) {
			montarListaSelectItemTextoPadrao();
		}
	}

	public String getAbrirModalAvisoTextoPadrao() {
		if (getImpressaoContratoVO().getTextoPadraoDeclaracao().equals(0)) {
			return "RichFaces.$('panelAvisoTextoPadrao').show()";
		}
		return "";
	}
	
	public void montarListaSelectItemTextoPadrao() throws Exception {
		List<TextoPadraoVO> listaTextoPadrao = getFacadeFactory().getTextoPadraoFacade().consultarPorTipoNivelComboBox("IR", null, "", false, getUsuarioLogado());
		setListaSelectItemTextoPadrao(UtilSelectItem.getListaSelectItem(listaTextoPadrao, "codigo", "descricao", true));

	}
	
	public void limparDadosAoFecharModalAvisoTextoPadrao() {
		setInclusaoHistoricoForaPrazoVO(null);
	}

	public List getListaSelectItemTextoPadrao() {
		if (listaSelectItemTextoPadrao == null) {
			listaSelectItemTextoPadrao = new ArrayList(0);
		}
		return listaSelectItemTextoPadrao;
	}

	public void setListaSelectItemTextoPadrao(List listaSelectItemTextoPadrao) {
		this.listaSelectItemTextoPadrao = listaSelectItemTextoPadrao;
	}

	public InclusaoHistoricoForaPrazoVO getInclusaoHistoricoForaPrazoVO() {
		if (inclusaoHistoricoForaPrazoVO == null) {
			inclusaoHistoricoForaPrazoVO = new InclusaoHistoricoForaPrazoVO();
		}
		return inclusaoHistoricoForaPrazoVO;
	}

	public void setInclusaoHistoricoForaPrazoVO(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO) {
		this.inclusaoHistoricoForaPrazoVO = inclusaoHistoricoForaPrazoVO;
	}

	
    public boolean getApresentarAnoLista(){
    	return (getCampoFiltroPor().equals("matricula") && !getImpressaoContratoVO().getMatriculaVO().getCurso().getPeriodicidade().equals("IN")) || (getCampoFiltroPor().equals("turma") && !getTurma().getIntegral());
    }
    
    public boolean getApresentarSemestreLista(){
    	return (getCampoFiltroPor().equals("matricula") && getImpressaoContratoVO().getMatriculaVO().getCurso().getPeriodicidade().equals("SE")) || (getCampoFiltroPor().equals("turma") && getTurma().getSemestral());
    }
    
    private Boolean abrirModalSelecaoContrato;
	private List<SelectItem> listaSelectItemContrato;
	private TipoContratoMatriculaEnum tipoContratoMatricula;
	private Boolean permitirAlterarContratoMatricula;

	public Boolean getAbrirModalSelecaoContrato() {
		if(abrirModalSelecaoContrato == null){
			abrirModalSelecaoContrato = false;
		}
		return abrirModalSelecaoContrato;
	}

	public void setAbrirModalSelecaoContrato(Boolean abrirModalSelecaoContrato) {
		this.abrirModalSelecaoContrato = abrirModalSelecaoContrato;
	}

	public List<SelectItem> getListaSelectItemContrato() {
		if(listaSelectItemContrato == null){
			listaSelectItemContrato = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemContrato;
	}

	public void setListaSelectItemContrato(List<SelectItem> listaSelectItemContrato) {
		this.listaSelectItemContrato = listaSelectItemContrato;
	}

	public TipoContratoMatriculaEnum getTipoContratoMatricula() {
		if(tipoContratoMatricula == null){
			tipoContratoMatricula = TipoContratoMatriculaEnum.NORMAL;
		}
		return tipoContratoMatricula;
	}

	public void setTipoContratoMatricula(TipoContratoMatriculaEnum tipoContratoMatricula) {
		this.tipoContratoMatricula = tipoContratoMatricula;
	}

	private void realizarCarregamentoPossiveisContratos() throws Exception {
		getListaSelectItemContrato().clear();
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			List<TurmaContratoVO> turmaContratoVOs = getFacadeFactory().getTurmaContratoFacade()
					.consultarTurmaTipoContratoMatricula(getImpressaoContratoVO().getMatriculaPeriodoVO().getTurma().getCodigo(),
							getTipoContratoMatricula(), false, getUsuarioLogado());
			boolean existeContratoMP = false;
			for (TurmaContratoVO turmaContratoVO : turmaContratoVOs) {
				getListaSelectItemContrato().add(new SelectItem(turmaContratoVO.getTextoPadraoVO().getCodigo(),
						turmaContratoVO.getTextoPadraoVO().getDescricao()));
				if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.NORMAL)
						&& Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoMatricula())
						&& getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoMatricula().getCodigo()
								.equals(turmaContratoVO.getTextoPadraoVO().getCodigo())) {
					existeContratoMP = true;
				} else if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.EXTENSAO)
						&& Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoExtensao())
						&& getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoExtensao().getCodigo()
								.equals(turmaContratoVO.getTextoPadraoVO().getCodigo())) {
					existeContratoMP = true;
				} else if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.FIADOR)
						&& Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoFiador())
						&& getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoFiador().getCodigo()
								.equals(turmaContratoVO.getTextoPadraoVO().getCodigo())) {
					existeContratoMP = true;
				}
			}
			if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.NORMAL)
					&& Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoMatricula())) {
				getListaSelectItemContrato().add(new SelectItem(getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoMatricula().getCodigo(),
						getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoMatricula().getDescricao()));
			} else if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.EXTENSAO)
					&& Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoExtensao())) {
				getListaSelectItemContrato().add(new SelectItem(getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoExtensao().getCodigo(),
						getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoExtensao().getDescricao()));
			} else if (!existeContratoMP && getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.FIADOR)
					&& Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoFiador())) {
				getListaSelectItemContrato().add(new SelectItem(getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoFiador().getCodigo(),
						getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoFiador().getDescricao()));
			}
			setAbrirModalSelecaoContrato(getListaSelectItemContrato().size() > 1);
		}
	}
	
	

	public Boolean getPermitirAlterarContratoMatricula() {
		if(permitirAlterarContratoMatricula == null){
			permitirAlterarContratoMatricula = false;
		}
		return permitirAlterarContratoMatricula;
	}

	public void setPermitirAlterarContratoMatricula(Boolean permitirAlterarContratoMatricula) {
		this.permitirAlterarContratoMatricula = permitirAlterarContratoMatricula;
	}

	public void selecionarContratoParaImpressao() {		
		try {	
			if(getPermitirAlterarContratoMatricula()) {
				if(getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.NORMAL)) {
					getImpressaoContratoVO().getMatriculaPeriodoVO().setGravarContratoMatricula(true);
				}else if(getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.EXTENSAO)) {
					getImpressaoContratoVO().getMatriculaPeriodoVO().setGravarContratoExtensao(true);
				}else if(getTipoContratoMatricula().equals(TipoContratoMatriculaEnum.FIADOR)) {
					getImpressaoContratoVO().getMatriculaPeriodoVO().setGravarContratoFiador(true);
				}				
			}			
			realizarImpressaoContrato();
			setAbrirModalSelecaoContrato(false);
		} catch (Exception e) {
			setImprimirContrato(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void validarLiberarOperacaoFuncionalidade() {
		try {
			
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarOperacaoFuncionalidade(), this.getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			if (getTipoOperacaoFuncionalidadeLiberar().equals(UteisJSF.internacionalizar("per_ImpressaoContrato_PermitirAlterarContratoMatricula_titulo"))){
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ImpressaoContrato_PermitirAlterarContratoMatricula", usuarioVerif);
				setPermitirAlterarContratoMatricula(true);
				OperacaoFuncionalidadeVO operacaoFuncionalidadeVO = getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA, getImpressaoContratoVO().getMatriculaVO().getMatricula(), OperacaoFuncionalidadeEnum.MATRICULA_LIBERARALTERACAOCONTRATO, usuarioVerif, "");
				getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacaoFuncionalidadeVO);
				setOncompleteOperacaoFuncionalidade("RichFaces.$('panelSelecionarContrato').show()");
			}
			if(getOncompleteOperacaoFuncionalidade().trim().isEmpty()) {
				setOncompleteOperacaoFuncionalidade("RichFaces.$('liberarOperacaoFuncionalidade').hide()");
			}else {
				setOncompleteOperacaoFuncionalidade("RichFaces.$('liberarOperacaoFuncionalidade').hide();"+getOncompleteOperacaoFuncionalidade());
			}
			setMensagemID("msg_funcionalidadeLiberadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private String oncompleteOperacaoFuncionalidade;
	private String tipoOperacaoFuncionalidadeLiberar;
	private String usernameLiberarOperacaoFuncionalidade;
	private String senhaLiberarOperacaoFuncionalidade;
	/**
	 * @return the oncompleteOperacaoFuncionalidade
	 */
	public String getOncompleteOperacaoFuncionalidade() {
		if (oncompleteOperacaoFuncionalidade == null) {
			oncompleteOperacaoFuncionalidade = "";
		}
		return oncompleteOperacaoFuncionalidade;
	}
	
	/**
	 * @param oncompleteOperacaoFuncionalidade
	 *            the oncompleteOperacaoFuncionalidade to set
	 */
	public void setOncompleteOperacaoFuncionalidade(String oncompleteOperacaoFuncionalidade) {
		this.oncompleteOperacaoFuncionalidade = oncompleteOperacaoFuncionalidade;
	}
	
	
	public String getTipoOperacaoFuncionalidadeLiberar() {
		if (tipoOperacaoFuncionalidadeLiberar == null) {
			tipoOperacaoFuncionalidadeLiberar = "";
		}
		return tipoOperacaoFuncionalidadeLiberar;
	}
	
	public void setTipoOperacaoFuncionalidadeLiberar(String valor) {
		this.tipoOperacaoFuncionalidadeLiberar = valor;
	}
	
	public String getUsernameLiberarOperacaoFuncionalidade() {
		if (usernameLiberarOperacaoFuncionalidade == null) {
			usernameLiberarOperacaoFuncionalidade = "";
		}
		return usernameLiberarOperacaoFuncionalidade;
	}
	
	public void setUsernameLiberarOperacaoFuncionalidade(String valor) {
		this.usernameLiberarOperacaoFuncionalidade = valor;
	}
	
	public String getSenhaLiberarOperacaoFuncionalidade() {
		if (senhaLiberarOperacaoFuncionalidade == null) {
			senhaLiberarOperacaoFuncionalidade = "";
		}
		return senhaLiberarOperacaoFuncionalidade;
	}
	
	public void setSenhaLiberarOperacaoFuncionalidade(String valor) {
		this.senhaLiberarOperacaoFuncionalidade = valor;
	}
	
	public Boolean getApresentarAno() {
		if (Uteis.isAtributoPreenchido(getTurma()) && (getTurma().getSemestral() || getTurma().getAnual()) && !getTurma().getCurso().getPeriodicidade().equals("IN")) {
			return true;
		} else if (Uteis.isAtributoPreenchido(getCurso()) && (getCurso().getSemestral() || getCurso().getAnual() && getCurso().getPeriodicidade().equals("IN")) ) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getApresentarSemestre() {
		if (Uteis.isAtributoPreenchido(getTurma()) && getTurma().getSemestral() && !getTurma().getCurso().getPeriodicidade().equals("IN")) {
			return true;
		} else if (Uteis.isAtributoPreenchido(getCurso()) && getCurso().getSemestral() && !getCurso().getPeriodicidade().equals("IN")) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public Boolean getApresentarIntervaloDatasParaTurmaIntegral() {
		return Uteis.isAtributoPreenchido(getTurma()) && getTurma().getCurso().getPeriodicidade().equals("IN");
	}
	
	private HashMap<Integer, String> mapCursoCodigoNome;
	
	public HashMap<Integer, String> getMapCursoCodigoNome() {
		if (mapCursoCodigoNome == null) {
			mapCursoCodigoNome = new HashMap<Integer, String>(0);
		}
		return mapCursoCodigoNome;
	}

	private void setNomeCurso(CursoVO cursoVO) throws StreamSeiException {
		try {
			if (getMapCursoCodigoNome().containsKey(cursoVO.getCodigo())) {
				cursoVO.setNome(getMapCursoCodigoNome().get(cursoVO.getCodigo()));
			} else {
				CursoVO c = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(cursoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
				cursoVO.setNome(c.getNome());
				getMapCursoCodigoNome().put(cursoVO.getCodigo(), cursoVO.getNome());
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public String getOncompleteModal() {
		if (oncompleteModal == null) {
			oncompleteModal = "";
		}
		return oncompleteModal;
	}

	public void setOncompleteModal(String oncompleteModal) {
		this.oncompleteModal = oncompleteModal;
	}
	
	public Boolean getHabilitadoAssinarEletronicamente() {
		if (habilitadoAssinarEletronicamente == null) {
			habilitadoAssinarEletronicamente = Boolean.FALSE;
		}
		return habilitadoAssinarEletronicamente;
	}

	public void setHabilitadoAssinarEletronicamente(Boolean habilitadoAssinarEletronicamente) {
		this.habilitadoAssinarEletronicamente = habilitadoAssinarEletronicamente;
	}
	

	public DocumentoAssinadoVO getDocumentoAssinadoExcluir() {
		if (documentoAssinadoExcluir == null) {
			documentoAssinadoExcluir = new DocumentoAssinadoVO();
		}
		return documentoAssinadoExcluir;
	}

	public void setDocumentoAssinadoExcluir(DocumentoAssinadoVO documentoAssinadoExcluir) {
		this.documentoAssinadoExcluir = documentoAssinadoExcluir;
	}

	public void visualizarContratoAssinado() throws UnknownHostException {
		setImpressaoContratoVO(new ImpressaoContratoVO());
		setImpressaoContratoVO((ImpressaoContratoVO) getRequestMap().get("impressaoContratoItens"));
		getImpressaoContratoVO();
	}
	
	public void realizarDownloadContrato() throws CloneNotSupportedException {
		try {
		DocumentoAssinadoVO documentoAssinado = (DocumentoAssinadoVO) context().getExternalContext().getRequestMap().get("documentoAssinadoItens");
		ArquivoVO arquivoVO;
			arquivoVO = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(documentoAssinado.getArquivo().getCodigo(), 0, getUsuario());
		if(!arquivoVO.getPastaBaseArquivo().startsWith(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo())) {
			ArquivoVO cloneArquivo = (ArquivoVO) arquivoVO.clone();
			if (cloneArquivo.getPastaBaseArquivo().endsWith("TMP")) {
				cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+cloneArquivo.getPastaBaseArquivo());
					
			}else {
			cloneArquivo.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo()+File.separator+cloneArquivo.getPastaBaseArquivo()+File.separator);
			} context().getExternalContext().getSessionMap().put("arquivoVO", cloneArquivo);		
		}else {
			context().getExternalContext().getSessionMap().put("arquivoVO", arquivoVO);
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void excluirDocumentoAssinado() {
		try {
			DocumentoAssinado.excluir("MapaDocumentoAssinadoPessoa", true, getUsuarioLogado());
			getFacadeFactory().getDocumentoAssinadoFacade().excluir(getDocumentoAssinadoExcluir(), false, getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getDocumentoAssinadoExcluir().getUnidadeEnsinoVO().getCodigo()));
			getImpressaoContratoVO().getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().clear();
			getImpressaoContratoVO().getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().addAll(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorMatriculaPeriodo(getImpressaoContratoVO().getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getImpressaoContratoVO().getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().size();
			if (getImpressaoContratoVO().getMatriculaPeriodoVO().getListaDocumentoAssinadoVO().size() == 0) {
				setOncompleteModal("RichFaces.$('panelDocumentoAssinado').hide();");
				consultarAlunoPorMatricula();
			}
			setMensagemID("msg_Documento_Assinado_Excluido");
			setMensagemDetalhada(MSG_TELA.msg_dados_excluidos.name(), UteisJSF.internacionalizar("msg_Documento_Assinado_Excluido"), true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
		}
	}
	
	public void documentoAssinadoExcluir() {
		try {
			DocumentoAssinadoVO obj = (DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItens");
			setDocumentoAssinadoExcluir(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
		}
	}
	
	 
	 public void notificacaoPendenciaContratoAssinado() {
			try {
				DocumentoAssinadoPessoaVO obj = (DocumentoAssinadoPessoaVO)getRequestMap().get("listaDocumentoPessoa");
				PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.NOTIFICACAO_PENDENCIA_ASSINATURA_CONTRATO, false, null,getUsuarioLogado());
				getFacadeFactory().getImpressaoContratoFacade().realizarNotificacaoPendenciaAssinaturaContrato(obj, mensagemTemplate, getUsuarioLogado(), getConfiguracaoGeralSistemaVO(), false);
				setMensagemID("msg_Notificacao_Pendencia_Enviada");
				setMensagemDetalhada(MSG_TELA.msg_dados_Enviados.name(), UteisJSF.internacionalizar("msg_Notificacao_Pendencia_Enviada"), true);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
			}
		}
	 
	 public void imprimirContratoAssinaturaDigital(Boolean gerarNovoArquivoAssinado) throws Exception {
		 	getImpressaoContratoVO().setGerarNovoArquivoAssinado(gerarNovoArquivoAssinado);
		 	getImpressaoContratoVO().setProvedorDeAssinaturaEnum(gerarNovoArquivoAssinado == Boolean.TRUE ? getProvedorDeAssinaturaEnum() : null);
		 	if (getQuantidadeDocumentoAssinadoMatriculaPeriodo() > 0 && gerarNovoArquivoAssinado) {
			 	setOncompleteOperacaoFuncionalidade("RichFaces.$('panelAlertaDocumentoAssinado').show();RichFaces.$('panelAssinarDocumento').hide();");
			}else {
				realizarImpressaoContrato();
				if (!getRealizandoImpressaoLote()) {
					setOncompleteOperacaoFuncionalidade("RichFaces.$('panelAssinarDocumento').hide();");
					consultarAlunoPorMatricula();					
				}

			}
		 	setHabilitadoAssinarEletronicamente(false);
	 }
	 
	 
	 public void imprimirContratoAssinaturaDigitalPorLote() throws Exception {
		 	getImpressaoContratoVO().setGerarNovoArquivoAssinado( Boolean.TRUE);
		 	getImpressaoContratoVO().setProvedorDeAssinaturaEnum(getProvedorDeAssinaturaEnum());
		 	realizarImpressaoContrato();
			if (!getRealizandoImpressaoLote()) {
				setOncompleteOperacaoFuncionalidade("RichFaces.$('panelAssinarDocumento').hide();");
				consultarAlunoPorMatricula();					
			}
		 	setHabilitadoAssinarEletronicamente(false);
	 }

	public Integer getQuantidadeDocumentoAssinadoMatriculaPeriodo() {
		if (quantidadeDocumentoAssinadoMatriculaPeriodo == null) {
			quantidadeDocumentoAssinadoMatriculaPeriodo = 0;
		}
		return quantidadeDocumentoAssinadoMatriculaPeriodo;
	}

	public void setQuantidadeDocumentoAssinadoMatriculaPeriodo(Integer quantidadeDocumentoAssinadoMatriculaPeriodo) {
		this.quantidadeDocumentoAssinadoMatriculaPeriodo = quantidadeDocumentoAssinadoMatriculaPeriodo;
	}

	public Boolean getRejeitarContratosPendentesEmitidos() {
		if (rejeitarContratosPendentesEmitidos == null) {
			rejeitarContratosPendentesEmitidos = Boolean.FALSE;
		}
		return rejeitarContratosPendentesEmitidos;
	}

	public void setRejeitarContratosPendentesEmitidos(Boolean rejeitarContratosPendentesEmitidos) {
		this.rejeitarContratosPendentesEmitidos = rejeitarContratosPendentesEmitidos;
	}
	
	 public void rejeitarContratosAssinaturaDigital(Boolean gerarNovoArquivoAssinado) {
		 try {
			 if (gerarNovoArquivoAssinado) {
				getDocumentoAssinadoMatriculaPeriodo().stream().map(DocumentoAssinadoVO::getListaDocumentoAssinadoPessoa).flatMap(x -> x.stream())
					.filter(documentoPessoaPendendePessoa -> documentoPessoaPendendePessoa.getSituacaoDocumentoAssinadoPessoaEnum().isPendente())
					.forEach(documentoPessoaPendenteRejeitar -> getFacadeFactory().getDocumentoAssinadoPessoaFacade().rejeitarContratoAssinadoPendenteAutomaticamente(documentoPessoaPendenteRejeitar, getUsuarioLogado()));
				setMensagemID("msg_Documento_Assinado_Rejeitado", Uteis.SUCESSO, true);
			 }
			 realizarImpressaoContrato();
			 consultarAlunoPorMatricula();
			 setMensagemID("msg_impressaoContrato_contratoImpresso", Uteis.SUCESSO, true);
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	 }

	public List<DocumentoAssinadoVO> getDocumentoAssinadoMatriculaPeriodo() {
		if (documentoAssinadoMatriculaPeriodo == null) {
			documentoAssinadoMatriculaPeriodo = new ArrayList<DocumentoAssinadoVO>();
		}
		return documentoAssinadoMatriculaPeriodo;
	}

	public void setDocumentoAssinadoMatriculaPeriodo(List<DocumentoAssinadoVO> documentoAssinadoMatriculaPeriodo) {
		this.documentoAssinadoMatriculaPeriodo = documentoAssinadoMatriculaPeriodo;
	}
	 
	public void consultarCursoPorNome() {
		try {
			setCurso(getCurso().getNome().trim().equals("") ? null : getFacadeFactory().getCursoFacade().consultarPorNome(getCurso().getNome(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setAno(getApresentarAno() ? Uteis.getAnoDataAtual4Digitos() : "");
			setSemestre(getApresentarSemestre() ? Uteis.getSemestreAtual() : "");
			setDataInicio(null);
			setDataFim(null);
		} catch (Exception e) {
			setTurma(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarCurso() {
		try {
			if (getCampoConsultaCurso().equals("nome")) {
				setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//				getListaConsultaCurso().stream().map(TurmaVO::getCurso).filter(Uteis::isAtributoPreenchido).forEach(this::setNomeCurso);
				getListaConsultaCurso().sort(Comparator.comparing(CursoVO::getNome));
			} 
//			else if (getCampoConsultaCurso().equals("nomeCurso")) {
//				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
//			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			if (Uteis.isAtributoPreenchido(obj)) {
				obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, getUsuarioLogado());
				setCurso(obj);
				setAno(getApresentarAno() ? Uteis.getAnoDataAtual4Digitos() : "");
				setSemestre(getApresentarSemestre() ? Uteis.getSemestreAtual() : "");
				setValorConsultaCurso("");
				setCampoConsultaCurso("");
				setDataInicio(null);
				setDataFim(null);
				getListaConsultaCurso().clear();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}


	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}


	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}


	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}


	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}


	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}


	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}


	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}


	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}
	 

	public void limparDadosCurso() {
		setImpressaoContratoVO(null);
		setCurso(null);
		setImpressaoContratoVOs(null);
		setImprimirContrato(false);
	}
	
	public void prepararImpressaoLote() {
		try {
			if (!getImpressaoContratoVOs().isEmpty()) {
				setImpressaoContratoVO(getImpressaoContratoVOs().get(0));
				realizarCarregamentoPossiveisContratos();
				getImpressaoContratoVO().setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(getImpressaoContratoVO().getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
				setHabilitadoAssinarEletronicamente(validarAssinaturaDigitalHabilitada(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), getImpressaoContratoVO().getMatriculaPeriodoVO().getContratoMatricula().getCodigo()));
				getComboboxProvedorAssinaturaPadrao();
			}
			setRealizandoImpressaoLote(true);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());	
		}
	}
	
	public void realizarInicioProgressBarImpressaoLote() {
		try {
			getListaErroEnvioLote().clear();
			setApresentarModalErroEnvioLote(false);
			setProgressBarVO(new ProgressBarVO());
			Uteis.checkState(getImpressaoContratoVOs().size() == 0L, "Não foi encontrado nenhum contrato pra ser impresso.");
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().iniciar(0l, (getImpressaoContratoVOs().size()), "Iniciando Processamento da(s) operações.", true, this, "imprimirTodosContratos");
			setOncompleteModal("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());	
		} 
	}
	
	public void imprimirTodosContratos() throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();
		for (ImpressaoContratoVO impressaoContratoVO : impressaoContratoVOs) {
			try {
				setProvedorDeAssinaturaEnum(getProvedorDeAssinaturaLoteEnum());
				impressaoContratoVO.setGerarNovoArquivoAssinado(true);
				getProgressBarVO().setStatus(" Imprimindo contrato ( " + (getProgressBarVO().getProgresso()) + " de " + getProgressBarVO().getMaxValue() + " ) ");
				setImpressaoContratoVO(impressaoContratoVO);
				imprimirContratoAssinaturaDigitalPorLote();
			} catch (Exception e) {
				getImpressaoContratoVO().setMotivoRejeicao(e.getMessage());
				consistirException.adicionarListaMensagemErro(e.getMessage());
				setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
				getListaErroEnvioLote().add(getImpressaoContratoVO());
			} finally {
				getProgressBarVO().incrementar();
			}
		}
		setRealizandoImpressaoLote(false);
		if (!getListaErroEnvioLote().isEmpty()) {
			setOncompleteModal("RichFaces.$('panelErroEnvioLote').show()");
		}
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
	}

	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO =  new ProgressBarVO();
		}
		return progressBarVO;
	}


	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}


	public Boolean getRealizandoImpressaoLote() {
		if (realizandoImpressaoLote == null) {
			realizandoImpressaoLote = false;
		}
		return realizandoImpressaoLote;
	}


	public void setRealizandoImpressaoLote(Boolean realizandoImpressaoLote) {
		this.realizandoImpressaoLote = realizandoImpressaoLote;
	}


	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "";
		}
		return tipoAluno;
	}


	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}
	
	public List<SelectItem> tipoAlunoComboBox;

	public List<SelectItem> getTipoAlunoComboBox() {
		if (tipoAlunoComboBox == null) {
			tipoAlunoComboBox = new ArrayList<SelectItem>(0);
			tipoAlunoComboBox.add(new SelectItem("AMBOS", "Calouro/Veterano"));
			tipoAlunoComboBox.add(new SelectItem("CALOURO", "Calouro"));
			tipoAlunoComboBox.add(new SelectItem("VETERANO", "Veterano"));		
		}
		return tipoAlunoComboBox;
	}


	public List<ImpressaoContratoVO> getListaErroEnvioLote() {
		if (listaErroEnvioLote == null) {
			listaErroEnvioLote = new ArrayList<ImpressaoContratoVO>();
		}
		return listaErroEnvioLote;
	}


	public void setListaErroEnvioLote(List<ImpressaoContratoVO> listaErroEnvioLote) {
		this.listaErroEnvioLote = listaErroEnvioLote;
	}


	public Boolean getApresentarModalErroEnvioLote() {
		if (apresentarModalErroEnvioLote == null) {
			apresentarModalErroEnvioLote = false;
		}
		return apresentarModalErroEnvioLote;
	}


	public void setApresentarModalErroEnvioLote(Boolean apresentarModalErroEnvioLote) {
		this.apresentarModalErroEnvioLote = apresentarModalErroEnvioLote;
	}
	
	public void fecharModalErroEnvioLote() {
		setApresentarModalErroEnvioLote(false);
	}


	public ProvedorDeAssinaturaEnum getProvedorDeAssinaturaLoteEnum() {
		return provedorDeAssinaturaLoteEnum;
	}


	public void setProvedorDeAssinaturaLoteEnum(ProvedorDeAssinaturaEnum provedorDeAssinaturaLoteEnum) {
		this.provedorDeAssinaturaLoteEnum = provedorDeAssinaturaLoteEnum;
	}
	
	
	public String getSituacaoContratoDigital() {
		if (situacaoContratoDigital == null) {
			situacaoContratoDigital = "";
		}
		return situacaoContratoDigital;
	}


	public void setSituacaoContratoDigital(String situacaoContratoDigital) {
		this.situacaoContratoDigital = situacaoContratoDigital;
	}
	
	public List<SelectItem> situacaoContratoDigitalComboBox;

	public List<SelectItem> getSituacaoContratoDigitalComboBox() {
		if (situacaoContratoDigitalComboBox == null) {
			situacaoContratoDigitalComboBox = new ArrayList<SelectItem>(0);
			situacaoContratoDigitalComboBox.add(new SelectItem("CONTRATONAOGERADO", "Sem Contrato Digital"));
			situacaoContratoDigitalComboBox.add(new SelectItem("CONTRATOPENDENTE", "Pendente Assinatura"));
			situacaoContratoDigitalComboBox.add(new SelectItem("CONTRATOASSINADO", "Assinado"));		
		}
		return situacaoContratoDigitalComboBox;
	}
	 
}