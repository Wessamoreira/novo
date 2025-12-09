package controle.administrativo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;


import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.EventoAtividadeComplementarVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaPeriodoVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.RegistroAtividadeComplementarVO;
import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.academico.AcompanhamentoAtividadeComplementar;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("AcompanhamentoAtividadeComplementarControle")
@Scope("viewScope")
@Lazy
public class AcompanhamentoAtividadeComplementarControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigoTipoAtividadeComplementar;
	private TurmaVO turmaVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private MatriculaVO matriculaVO;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private List<SelectItem> listaSelectItemTipoAtividadeComplementar;
	private List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs;
	private List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaVOs;
	private List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs;
	private List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs;
	private RegistroAtividadeComplementarMatriculaVO atividadeComplementarMatricula;
	private RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatricula;
	private RegistroAtividadeComplementarVO registroAtividadeComplementarVO;
	private RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaIncluirVO;
	private Boolean permiteAlunoIncluirAtividade;
	private List<SelectItem> tipoConsultaComboTurma;
	private CursoVO cursoVO;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private List<SelectItem> tipoConsultaComboCurso;
	private String campoSemestre;
	private String campoAno;
	private String campoSituacao;
	private SituacaoAtividadeComplementarMatriculaEnum situacaoAtividadeComplementarMatricula;
	private List<SelectItem> listaSelectItemSituacaoAtividadeComplementarMatricula;
	private List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarObrigatoriaMatricula;
	private Boolean permiteEditarCHConsiderada;
	
	private String filterAnoSemestre;
	private Boolean consultaDataScroller;

	public AcompanhamentoAtividadeComplementarControle() throws Exception {
		
	}

	public void montarListaSelectItemTipoAtividadeComplementar() {
		try {
			List<TipoAtividadeComplementarVO> resultadoConsulta = getFacadeFactory().getTipoAtividadeComplementarFacade().consultar(false, getUsuarioLogado());
			List<SelectItem> lista = UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome");
			this.setListaSelectItemTipoAtividadeComplementar(lista);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@PostConstruct
	public void inicializarPorMatriculaVisaoAluno() {
		if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			try {
				setAtividadeComplementarMatricula(new RegistroAtividadeComplementarMatriculaVO());
				getAtividadeComplementarMatricula().getMatriculaVO().setMatricula(getVisaoAlunoControle().getMatricula().getMatricula());
				consultarRegistroAtividadeComplementarPorAnoSemestre();
				getAtividadeComplementarMatricula().getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(getVisaoAlunoControle().getMatricula().getMatricula(), false, getUsuarioLogadoClone()));
				setMensagemID("msg_dados_consultados");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		} else {
			montarListaSelectItemTipoAtividadeComplementar();
		}
	}
	
	public void consultarRegistroAtividadeComplementarPorAnoSemestre() {
		try {
			if (getFilterAnoSemestre().isEmpty()) {
				setCampoAno("");
				setCampoSemestre("");
			} else {
				setCampoSemestre(getFilterAnoSemestre().contains("/") ? getFilterAnoSemestre().substring(getFilterAnoSemestre().indexOf("/")+1, getFilterAnoSemestre().length()) : "");
				setCampoAno(getFilterAnoSemestre().contains("/") ? getFilterAnoSemestre().substring(0, getFilterAnoSemestre().indexOf("/")) : "");
			}

			this.setListaRegistroAtividadeComplementarMatriculaVOs(getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().consultar(0, 0, getCampoAno(), getCampoSemestre(), "", getVisaoAlunoControle().getMatricula().getMatricula(), 0, null, false,getVisaoAlunoControle().getMatricula().getGradeCurricularAtual().getCodigo() , getControleConsultaOtimizado() ,this.getUsuarioLogado()));
			if(!getListaRegistroAtividadeComplementarMatriculaVOs().isEmpty()){
				setAtividadeComplementarMatricula(getListaRegistroAtividadeComplementarMatriculaVOs().get(0));
				consultarPorMatricula(getAtividadeComplementarMatricula());
			} else {
				setAtividadeComplementarMatricula(new RegistroAtividadeComplementarMatriculaVO());
				setListaConsultaRegistroAtividadeComplementarMatriculaVOs(new ArrayList<>());
				setListaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs(new ArrayList<>());
				setListaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs(new ArrayList<>());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), this.getCursoVO().getCodigo(), 0, false, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() throws Exception {
		this.setTurmaVO(null);
		this.setCursoVO(null);
	}

	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(this.getMatriculaVO());
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		this.setListaRegistroAtividadeComplementarMatriculaVOs(new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("acompanhamentoAtividadeComplementarCons.xhtml");
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(this.getMatriculaVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + this.getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.setTurmaVO(null);
			this.setMatriculaVO(objAluno);
			this.setCursoVO(objAluno.getCurso());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatriculaVO(new MatriculaVO());
		}
	}

	public String consultar() {
		try {
			AcompanhamentoAtividadeComplementar.validarDadosFiltroConsulta(this.getCursoVO().getCodigo(), this.getTurmaVO().getCodigo(), this.getMatriculaVO().getMatricula(), this.getCursoVO().getPeriodicidade(), this.getCampoSemestre(), this.getCampoAno());
			//AcompanhamentoAtividadeComplementar.validarConsulta(this.getCursoVO().getCodigo());
			getControleConsultaOtimizado().getListaConsulta().clear();			
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().consultar(this.getCursoVO().getCodigo(), this.getTurmaVO().getCodigo(), this.getCampoAno(), this.getCampoSemestre(), this.getCampoSituacao(), this.getMatriculaVO().getMatricula(), this.getCodigoTipoAtividadeComplementar(), getSituacaoAtividadeComplementarMatricula(), false,this.getMatriculaVO().getGradeCurricularAtual().getCodigo(),getControleConsultaOtimizado(), this.getUsuarioLogado()));		
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("acompanhamentoAtividadeComplementarCons.xhtml");
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("acompanhamentoAtividadeComplementarCons.xhtml");
		}
	}

	public void consultarPorMatricula() {
		try {
			consultarPorMatricula((RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens"));			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarPorMatricula(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO) {
		try {
			setAtividadeComplementarMatricula(registroAtividadeComplementarMatriculaVO);
			this.setListaConsultaRegistroAtividadeComplementarMatriculaVOs(getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().consultarPorMatricula(getAtividadeComplementarMatricula().getMatriculaVO().getMatricula(), false, this.getUsuarioLogado()));
			setListaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarPorMatriculaSituacao(getAtividadeComplementarMatricula().getMatriculaVO().getMatricula(), SituacaoAtividadeComplementarMatriculaEnum.INDEFERIDO, false, getUsuario()));
			setListaConsultaRegistroAtividadeComplementarObrigatoriaMatricula(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarAtividadeComplementarObrigatoriaMatriz(getAtividadeComplementarMatricula().getMatriculaVO().getMatricula() , getAtividadeComplementarMatricula().getMatriculaVO().getGradeCurricularAtual().getCodigo()));
			Ordenacao.ordenarListaDecrescente(getListaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs(), "registroAtividadeComplementar.data");
			setListaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarPorMatriculaSituacao(getAtividadeComplementarMatricula().getMatriculaVO().getMatricula(), SituacaoAtividadeComplementarMatriculaEnum.AGUARDANDO_DEFERIMENTO, false, getUsuario()));
			Ordenacao.ordenarListaDecrescente(getListaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs(), "registroAtividadeComplementar.data");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(obj);
		setCursoVO(obj.getCurso());
		obj = null;
		setValorConsultaTurma(null);
		setCampoConsultaTurma(null);
		Uteis.liberarListaMemoria(getListaConsultaTurma());
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		this.setMatriculaVO(obj);
		this.setCursoVO(obj.getCurso());
		this.setValorConsultaAluno("");
		this.setCampoConsultaAluno("");
		this.setTurmaVO(null);
		getListaConsultaAluno().clear();
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public Integer getCodigoTipoAtividadeComplementar() {
		if (codigoTipoAtividadeComplementar == null) {
			codigoTipoAtividadeComplementar = 0;
		}
		return codigoTipoAtividadeComplementar;
	}

	public void setCodigoTipoAtividadeComplementar(Integer codigoTipoAtividadeComplementar) {
		this.codigoTipoAtividadeComplementar = codigoTipoAtividadeComplementar;
	}

	public TurmaVO getTurmaVO() {
		if (this.turmaVO == null) {
			this.turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (this.listaConsultaTurma == null) {
			this.listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public MatriculaVO getMatriculaVO() {
		if (this.matriculaVO == null) {
			this.matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public String getCampoConsultaAluno() {
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (this.listaConsultaAluno == null) {
			this.listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List<SelectItem> getListaSelectItemTipoAtividadeComplementar() {
		if (this.listaSelectItemTipoAtividadeComplementar == null) {
			this.listaSelectItemTipoAtividadeComplementar = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoAtividadeComplementar;
	}

	public void setListaSelectItemTipoAtividadeComplementar(List<SelectItem> listaSelectItemTipoAtividadeComplementar) {
		this.listaSelectItemTipoAtividadeComplementar = listaSelectItemTipoAtividadeComplementar;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> getListaRegistroAtividadeComplementarMatriculaVOs() {
		if (this.listaRegistroAtividadeComplementarMatriculaVOs == null) {
			this.listaRegistroAtividadeComplementarMatriculaVOs = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		}
		return listaRegistroAtividadeComplementarMatriculaVOs;
	}

	public void setListaRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs) {
		this.listaRegistroAtividadeComplementarMatriculaVOs = listaRegistroAtividadeComplementarMatriculaVOs;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> getListaConsultaRegistroAtividadeComplementarMatriculaVOs() {
		if (this.listaConsultaRegistroAtividadeComplementarMatriculaVOs == null) {
			this.listaConsultaRegistroAtividadeComplementarMatriculaVOs = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		}
		return listaConsultaRegistroAtividadeComplementarMatriculaVOs;
	}

	public void setListaConsultaRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaVOs) {
		this.listaConsultaRegistroAtividadeComplementarMatriculaVOs = listaConsultaRegistroAtividadeComplementarMatriculaVOs;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	public void setTipoConsultaComboTurma(List<SelectItem> tipoConsultaComboTurma) {
		this.tipoConsultaComboTurma = tipoConsultaComboTurma;
	}

	public String getCaminhoServidorDownload() {
		try {
			EventoAtividadeComplementarVO obj = (EventoAtividadeComplementarVO) context().getExternalContext().getRequestMap().get("eventosItens");
			if (obj.getCaminhoArquivoWeb() == null) {
				obj.setCaminhoArquivoWeb(getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoVO(), PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR, getConfiguracaoGeralPadraoSistema()));
			}
			return obj.getCaminhoArquivoWeb();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	
	public String selecionarArquivoParaDownload() {
		try {
			EventoAtividadeComplementarVO evento = (EventoAtividadeComplementarVO) context().getExternalContext().getRequestMap().get("eventosItens");
			selecionarArquivoParaDownloadGeral(evento.getArquivoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}	
	
	public void selecionarArquivoParaDownloadGeral(ArquivoVO arquivoVO) {
		try {
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.setAttribute("codigoArquivo", arquivoVO.getCodigo());
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public CursoVO getCursoVO() {
		if (this.cursoVO == null) {
			this.cursoVO = new CursoVO();
		}
		return this.cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public String getCampoConsultaCurso() {
		if (this.campoConsultaCurso == null) {
			this.campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (this.valorConsultaCurso == null) {
			this.valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (this.listaConsultaCurso == null) {
			this.listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			this.setCursoVO(obj);
			setTurmaVO(null);
			this.setCampoConsultaCurso("");
			this.setValorConsultaCurso("");
			this.getListaConsultaCurso().clear();
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public void setTipoConsultaComboCurso(List<SelectItem> tipoConsultaComboCurso) {
		this.tipoConsultaComboCurso = tipoConsultaComboCurso;
	}

	public Boolean getApresentarAnoMatricula() {
		return ((this.getCursoVO().getPeriodicidade().equals("AN") || this.getCursoVO().getPeriodicidade().equals("SE")) && !getTurmaVO().getCodigo().equals(0));
	}

	public Boolean getApresentarSemestreMatricula() {
		return (this.getCursoVO().getPeriodicidade().equals("SE") && !getTurmaVO().getCodigo().equals(0));
	}

	public void limparCurso() throws Exception {
		this.setCursoVO(new CursoVO());
	}

	public String getCampoSemestre() {
		return campoSemestre;
	}

	public void setCampoSemestre(String campoSemestre) {
		this.campoSemestre = campoSemestre;
	}

	public String getCampoAno() {
		return campoAno;
	}

	public void setCampoAno(String campoAno) {
		this.campoAno = campoAno;
	}

	public String getCampoSituacao() {
		return campoSituacao;
	}

	public void setCampoSituacao(String campoSituacao) {
		this.campoSituacao = campoSituacao;
	}

	public List<SelectItem> getListaSelectSituacao() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("AT", "Ativo/Concluído"));
		lista.add(new SelectItem("PF", "Ativo - Possível Formando"));
		lista.add(new SelectItem("CO", "Concluído - Possível Formando"));
		return lista;
	}

	public void consultarCursoCoordenador() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarListaCursoPorNomeCursoCodigoPessoaCoordenador(getValorConsultaCurso(), getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			} else if (getCampoConsultaCurso().equals("codigo")) {
				Integer codigoCurso = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultarListaCursoPorCodigoCursoCodigoPessoaCoordenador(codigoCurso, getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch(NumberFormatException numberFormatException) {
			setMensagemDetalhada("msg_erro", "Informe um número.");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaCoordenador() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCoordenador(getUsuarioLogado().getPessoa().getCodigo(), this.getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, true, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorCoordenador() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaPorCoordenador(getValorConsultaAluno(), getUsuarioLogado().getPessoa().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaPorCoordenador(getValorConsultaAluno(), getUsuarioLogado().getPessoa().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCursoPorCoordenador(getValorConsultaAluno(), getUsuarioLogado().getPessoa().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatriculaPorCoordenador() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaPorCoordenador(this.getMatriculaVO().getMatricula(), getUsuarioLogado().getPessoa().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + this.getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.setTurmaVO(null);
			this.setMatriculaVO(objAluno);
			this.setCursoVO(objAluno.getCurso());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatriculaVO(new MatriculaVO());
		}
	}

	public List<SelectItem> getListaSelectSemestre() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public RegistroAtividadeComplementarMatriculaVO getAtividadeComplementarMatricula() {
		if (atividadeComplementarMatricula == null) {
			atividadeComplementarMatricula = new RegistroAtividadeComplementarMatriculaVO();
		}
		return atividadeComplementarMatricula;
	}

	public void setAtividadeComplementarMatricula(RegistroAtividadeComplementarMatriculaVO atividadeComplementarMatricula) {
		this.atividadeComplementarMatricula = atividadeComplementarMatricula;
	}

	public RegistroAtividadeComplementarMatriculaVO getRegistroAtividadeComplementarMatricula() {
		if (registroAtividadeComplementarMatricula == null) {
			registroAtividadeComplementarMatricula = new RegistroAtividadeComplementarMatriculaVO();
		}
		return registroAtividadeComplementarMatricula;
	}

	public void setRegistroAtividadeComplementarMatricula(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatricula) {
		this.registroAtividadeComplementarMatricula = registroAtividadeComplementarMatricula;
	}

	public void consultarListaRegistroAtividadeComplementarMatrilculaVOsAluno() {
		try {
			setRegistroAtividadeComplementarMatricula((RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens"));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private EventoAtividadeComplementarVO eventoAtividadeComplementarVO;
	
	public EventoAtividadeComplementarVO getEventoAtividadeComplementarVO() {
		if(eventoAtividadeComplementarVO == null){
			eventoAtividadeComplementarVO = new EventoAtividadeComplementarVO();
		}
		return eventoAtividadeComplementarVO;
	}

	public void setEventoAtividadeComplementarVO(EventoAtividadeComplementarVO eventoAtividadeComplementarVO) {
		this.eventoAtividadeComplementarVO = eventoAtividadeComplementarVO;
	}
	
	public void selecionarEventoAtividadeComplementarVO() {
		try {			
			setRegistroAtividadeComplementarMatricula((RegistroAtividadeComplementarMatriculaVO) getRequestMap().get("registroAtividadeComplementarMatriculaVOItens"));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarObservacaoRegistroAtividadeComplementarMatricula() {
		try {					
			getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().alterarObservacao(getRegistroAtividadeComplementarMatricula(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<RegistroAtividadeComplementarMatriculaVO> getListaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs() {
		if (listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs == null) {
			listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		}
		return listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs;
	}

	public void setListaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs(List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs) {
		this.listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs = listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> getListaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs() {
		if (listaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs == null) {
			listaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		}
		return listaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs;
	}

	public void setListaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs(List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs) {
		this.listaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs = listaConsultaRegistroAtividadeComplementarMatriculaIndeferidoVOs;
	}

	public RegistroAtividadeComplementarVO getRegistroAtividadeComplementarVO() {
		if (registroAtividadeComplementarVO == null) {
			registroAtividadeComplementarVO = new RegistroAtividadeComplementarVO();
		}
		return registroAtividadeComplementarVO;
	}

	public void setRegistroAtividadeComplementarVO(RegistroAtividadeComplementarVO registroAtividadeComplementarVO) {
		this.registroAtividadeComplementarVO = registroAtividadeComplementarVO;
	}

	public RegistroAtividadeComplementarMatriculaVO getRegistroAtividadeComplementarMatriculaIncluirVO() {
		if (registroAtividadeComplementarMatriculaIncluirVO == null) {
			registroAtividadeComplementarMatriculaIncluirVO = new RegistroAtividadeComplementarMatriculaVO();
		}
		return registroAtividadeComplementarMatriculaIncluirVO;
	}

	public void setRegistroAtividadeComplementarMatriculaIncluirVO(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaIncluirVO) {
		this.registroAtividadeComplementarMatriculaIncluirVO = registroAtividadeComplementarMatriculaIncluirVO;
	}
	
	public void novo() {
		try {
			setOncompleteModal("");
			if(!getRegistroAtividadeComplementarVO().isNovoObj()) {				
				setRegistroAtividadeComplementarVO(new RegistroAtividadeComplementarVO());
				setRegistroAtividadeComplementarMatriculaIncluirVO(new RegistroAtividadeComplementarMatriculaVO());
				getRegistroAtividadeComplementarVO().getData();
			}
			getRegistroAtividadeComplementarMatriculaIncluirVO().setMatriculaVO(getVisaoAlunoControle().getMatricula());
			getRegistroAtividadeComplementarMatriculaIncluirVO().setSituacaoAtividadeComplementarMatricula(SituacaoAtividadeComplementarMatriculaEnum.AGUARDANDO_DEFERIMENTO);					
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void gravar() {
		try {
			setOncompleteModal("");
			getRegistroAtividadeComplementarVO().getListaRegistroAtividadeComplementarMatriculaVOs().clear();
			getRegistroAtividadeComplementarMatriculaIncluirVO().setRegistroAtividadeComplementarVO(getRegistroAtividadeComplementarVO());
			getRegistroAtividadeComplementarMatriculaIncluirVO().setDataCriacao(new Date());
			getRegistroAtividadeComplementarMatriculaIncluirVO().setTipoAtividadeComplementarVO(getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorChavePrimaria(getRegistroAtividadeComplementarMatriculaIncluirVO().getTipoAtividadeComplementarVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
			getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().validarDadosCargaHorariaRealizada(getRegistroAtividadeComplementarMatriculaIncluirVO(), getListaConsultaRegistroAtividadeComplementarMatriculaVOs(), getListaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs());
			getRegistroAtividadeComplementarVO().getListaRegistroAtividadeComplementarMatriculaVOs().add(getRegistroAtividadeComplementarMatriculaIncluirVO());
			getFacadeFactory().getRegistroAtividadeComplementarFacade().incluir(getRegistroAtividadeComplementarVO(), getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
			getListaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs().add(getRegistroAtividadeComplementarMatriculaIncluirVO());
			Ordenacao.ordenarListaDecrescente(getListaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs(), "registroAtividadeComplementar.data");
			novo();
			setOncompleteModal("PF('panelIncluirAtividade').hide();");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			if(!getRegistroAtividadeComplementarMatriculaIncluirVO().getArquivoVO().getNome().trim().isEmpty() && getRegistroAtividadeComplementarMatriculaIncluirVO().getArquivoVO().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR_TMP)) {
				getFacadeFactory().getArquivoHelper().removerArquivoDiretorio(false, getRegistroAtividadeComplementarMatriculaIncluirVO().getArquivoVO(), "atividadeComplementar", getConfiguracaoGeralPadraoSistema());
			}
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getRegistroAtividadeComplementarMatriculaIncluirVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR_TMP, getUsuarioLogado());
			getRegistroAtividadeComplementarMatriculaIncluirVO().setCaminhoArquivoWeb(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	
	public Boolean getPermiteAlunoIncluirAtividade() {
		if (permiteAlunoIncluirAtividade == null) {
			try {
				permiteAlunoIncluirAtividade = false;
				if(Uteis.isAtributoPreenchido(getVisaoAlunoControle()) && (getUsuarioLogado().getIsApresentarVisaoAluno()|| getUsuarioLogado().getIsApresentarVisaoPais())){
					getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVO("AtividadeComplementarAluno_permitirIncluirAtividade", getUsuarioLogado());				
					List<TipoAtividadeComplementarVO> resultadoConsulta = getFacadeFactory().getTipoAtividadeComplementarFacade()
						.consultarPorCursoTurmaMatricula(null, null, false, getVisaoAlunoControle().getMatricula().getMatricula(), 0, false, getUsuarioLogado());
					if(Uteis.isAtributoPreenchido(resultadoConsulta)) {
						setListaSelectItemTipoAtividadeComplementar(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
						permiteAlunoIncluirAtividade = true;
					}								
				}
			}catch (Exception e) {
				
			}
		}
		return permiteAlunoIncluirAtividade;
	}

	public void setPermiteAlunoIncluirAtividade(Boolean permiteAlunoIncluirAtividade) {
		this.permiteAlunoIncluirAtividade = permiteAlunoIncluirAtividade;
	}

	public SituacaoAtividadeComplementarMatriculaEnum getSituacaoAtividadeComplementarMatricula() {		
		return situacaoAtividadeComplementarMatricula;
	}

	public void setSituacaoAtividadeComplementarMatricula(SituacaoAtividadeComplementarMatriculaEnum situacaoAtividadeComplementarMatricula) {
		this.situacaoAtividadeComplementarMatricula = situacaoAtividadeComplementarMatricula;
	}

	public List<SelectItem> getListaSelectItemSituacaoAtividadeComplementarMatricula() {
		if (listaSelectItemSituacaoAtividadeComplementarMatricula == null) {
			listaSelectItemSituacaoAtividadeComplementarMatricula = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoAtividadeComplementarMatricula.add(new SelectItem(null, "Todas"));
			listaSelectItemSituacaoAtividadeComplementarMatricula.add(new SelectItem(SituacaoAtividadeComplementarMatriculaEnum.AGUARDANDO_DEFERIMENTO, SituacaoAtividadeComplementarMatriculaEnum.AGUARDANDO_DEFERIMENTO.getValorApresentar()));
			listaSelectItemSituacaoAtividadeComplementarMatricula.add(new SelectItem(SituacaoAtividadeComplementarMatriculaEnum.INDEFERIDO, SituacaoAtividadeComplementarMatriculaEnum.INDEFERIDO.getValorApresentar()));
		}
		return listaSelectItemSituacaoAtividadeComplementarMatricula;
	}

	public void setListaSelectItemSituacaoAtividadeComplementarMatricula(List<SelectItem> listaSelectItemSituacaoAtividadeComplementarMatricula) {
		this.listaSelectItemSituacaoAtividadeComplementarMatricula = listaSelectItemSituacaoAtividadeComplementarMatricula;
	}

	private RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaDeferirIndeferirVO;	

	public RegistroAtividadeComplementarMatriculaVO getRegistroAtividadeComplementarMatriculaDeferirIndeferirVO() {
		if (registroAtividadeComplementarMatriculaDeferirIndeferirVO == null) {
			registroAtividadeComplementarMatriculaDeferirIndeferirVO = new RegistroAtividadeComplementarMatriculaVO();
		}
		return registroAtividadeComplementarMatriculaDeferirIndeferirVO;
	}

	public void setRegistroAtividadeComplementarMatriculaDeferirIndeferirVO(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaDeferirIndeferirVO) {
		this.registroAtividadeComplementarMatriculaDeferirIndeferirVO = registroAtividadeComplementarMatriculaDeferirIndeferirVO;
	}
	
	public void selecionarRegistroAtividadeComplementarDeferirIndeferir() {
		setRegistroAtividadeComplementarMatriculaDeferirIndeferirVO((RegistroAtividadeComplementarMatriculaVO) getRequestMap().get("registroAtividadeComplementarMatriculaVOItens"));
		limparMensagem();
	}

	public void realizarDeferimentoAtividadeComplementarMatricula() {
		try {
			List<RegistroAtividadeComplementarMatriculaVO> lista = new ArrayList<RegistroAtividadeComplementarMatriculaVO>();
			lista.add(getRegistroAtividadeComplementarMatriculaDeferirIndeferirVO());
			getFacadeFactory().getRegistroAtividadeComplementarFacade().validarTipoAtividadeComplementar(lista, getUsuarioLogado());
			getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().realizarDeferimento(getRegistroAtividadeComplementarMatriculaDeferirIndeferirVO(), getUsuarioLogado());
			consultarPorMatricula(getAtividadeComplementarMatricula());
			calcularCargaHorariaTotal(getAtividadeComplementarMatricula());
			realizarAtualizacaoInformacaoListaConsulta();
			setMensagemID("msg_dados_deferido",  Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarIndeferimentoAtividadeComplementarMatricula() {
		try {
			setOncompleteModal("");
			getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().realizarIndeferimento(getRegistroAtividadeComplementarMatriculaDeferirIndeferirVO(), getUsuarioLogado());
			consultarPorMatricula(getAtividadeComplementarMatricula());
			calcularCargaHorariaTotal(getAtividadeComplementarMatricula());
			if (Uteis.isAtributoPreenchido(listaConsultaRegistroAtividadeComplementarMatriculaVOs)) {
				for( RegistroAtividadeComplementarMatriculaVO listaConsultaRegistroAtividadeComplementarMatriculaVOs: getListaConsultaRegistroAtividadeComplementarMatriculaVOs()) {
				for (RegistroAtividadeComplementarMatriculaPeriodoVO registroAtividadeComplementarMatriculaPeriodoVO : listaConsultaRegistroAtividadeComplementarMatriculaVOs.getRegistroAtividadeComplementarMatriculaPeriodoVOs()) {
					int x = 0;
					for (EventoAtividadeComplementarVO evento : registroAtividadeComplementarMatriculaPeriodoVO.getEventoAtividadeComplementarVOs()) {
						if (evento.getRegistroAtividadeComplementarMatriculaVO().getCodigo().equals(getRegistroAtividadeComplementarMatriculaDeferirIndeferirVO().getCodigo())) {
							registroAtividadeComplementarMatriculaPeriodoVO.getEventoAtividadeComplementarVOs().remove(x);
							break;
						}
						x++;
					}
				}
				}
			}
			realizarAtualizacaoInformacaoListaConsulta();
			setOncompleteModal("PF('panelIndeferir').hide();");
			setMensagemID("msg_dados_deferido",  Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarAtualizacaoInformacaoListaConsulta() throws Exception {
		for(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO: getListaRegistroAtividadeComplementarMatriculaVOs()) {
			if(registroAtividadeComplementarMatriculaVO.getMatriculaVO().getMatricula().equals(getAtividadeComplementarMatricula().getMatriculaVO().getMatricula())) {
				List<RegistroAtividadeComplementarMatriculaVO> registroAtividadeComplementarMatriculaVOs = getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().consultar(null, null, "", "", "", getAtividadeComplementarMatricula().getMatriculaVO().getMatricula(), null, null, false,getAtividadeComplementarMatricula().getMatriculaVO().getGradeCurricularAtual().getCodigo(), getControleConsultaOtimizado() ,getUsuarioLogado());
				if(registroAtividadeComplementarMatriculaVOs != null && !registroAtividadeComplementarMatriculaVOs.isEmpty()) {
					registroAtividadeComplementarMatriculaVO.setCargaHorariaAguardandoDeferimento(registroAtividadeComplementarMatriculaVOs.get(0).getCargaHorariaAguardandoDeferimento());
					registroAtividadeComplementarMatriculaVO.setCargaHorariaIndeferido(registroAtividadeComplementarMatriculaVOs.get(0).getCargaHorariaIndeferido());
					registroAtividadeComplementarMatriculaVO.setCargaHorariaConsiderada(registroAtividadeComplementarMatriculaVOs.get(0).getCargaHorariaConsiderada());
					registroAtividadeComplementarMatriculaVO.setCargaHorariaPendente(registroAtividadeComplementarMatriculaVOs.get(0).getCargaHorariaPendente());
					registroAtividadeComplementarMatriculaVO.setCargaHorariaRealizada(registroAtividadeComplementarMatriculaVOs.get(0).getCargaHorariaRealizada());
					registroAtividadeComplementarMatriculaVO.setAtividadeComplementarIntegraliazada(registroAtividadeComplementarMatriculaVOs.get(0).getAtividadeComplementarIntegraliazada());
				}
				break;
			}			
		}
	}

	public String selecionarArquivoRegistroAtividadeMatriculaParaDownload() {
		try {
			RegistroAtividadeComplementarMatriculaVO evento = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens");
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.setAttribute("codigoArquivo", evento.getArquivoVO().getCodigo());
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}
	
	public Boolean permitiDeferir;
	public Boolean permitiIndeferir;

	public Boolean getPermitiDeferir() {
		if (permitiDeferir == null) {
			try {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVO("RegistroAtividadeComplementarMatricula_permiteDeferir", getUsuarioLogado());
				permitiDeferir = true;
			}catch (Exception e) {
				permitiDeferir = false;
			}			
		}
		return permitiDeferir;
	}

	public void setPermitiDeferir(Boolean permitiDeferir) {
		this.permitiDeferir = permitiDeferir;
	}

	public Boolean getPermitiIndeferir() {
		if (permitiIndeferir == null) {
			try {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVO("RegistroAtividadeComplementarMatricula_permiteIndeferir", getUsuarioLogado());
				permitiIndeferir = true;
			}catch (Exception e) {
				permitiIndeferir = false;
			}
		}
		return permitiIndeferir;
	}

	public void setPermitiIndeferir(Boolean permitiIndeferir) {
		this.permitiIndeferir = permitiIndeferir;
	}
	
	public String getFilterAnoSemestre() {
		if (filterAnoSemestre == null) {
			filterAnoSemestre = "";
		}
		return filterAnoSemestre;
	}

	public void setFilterAnoSemestre(String filterAnoSemestre) {
		this.filterAnoSemestre = filterAnoSemestre;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> getListaConsultaRegistroAtividadeComplementarObrigatoriaMatricula() {
		if (listaConsultaRegistroAtividadeComplementarObrigatoriaMatricula == null) {
			listaConsultaRegistroAtividadeComplementarObrigatoriaMatricula = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		}
		return listaConsultaRegistroAtividadeComplementarObrigatoriaMatricula;
	}

	public void setListaConsultaRegistroAtividadeComplementarObrigatoriaMatricula(
			List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarObrigatoriaMatricula) {
		this.listaConsultaRegistroAtividadeComplementarObrigatoriaMatricula = listaConsultaRegistroAtividadeComplementarObrigatoriaMatricula;
	}
	
	public void realizarAlteracaoCHAtividadeComplementarMatricula() {
		try {
			if (!Uteis.isAtributoPreenchido(getRegistroAtividadeComplementarMatriculaDeferirIndeferirVO().getJustificativaAlteracaoCHConsiderada())) {
				throw new Exception("O campo JUSTIFICATIVA da alteração da carga horária considerada deve ser preenchido.");
			}
			getRegistroAtividadeComplementarMatriculaDeferirIndeferirVO().setResponsavelEditarCHConsiderada(getUsuarioLogado());
			getRegistroAtividadeComplementarMatriculaDeferirIndeferirVO().setDataEditarCHConsiderada(new Date());
			getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().alterarCargaHorariaConsiderada(getRegistroAtividadeComplementarMatriculaDeferirIndeferirVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			consultar();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			setOncompleteModal("PF('panelEditarChConsiderada').hide(); PF('panelDeferir').show(); ");
		}catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarRegistroAtividadeComplementarEditarChConsiderada() {
		setRegistroAtividadeComplementarMatriculaDeferirIndeferirVO((RegistroAtividadeComplementarMatriculaVO) getRequestMap().get("registroAtividadeComplementarMatriculaVOItens"));
		limparMensagem();
	}

	public Boolean getPermiteEditarCHConsiderada() {		
		if (permiteEditarCHConsiderada == null) {
			try {
				getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVO("RegistroAtividadeComplementarMatricula_permitirEditarCHConsiderada", getUsuarioLogado());
				permiteEditarCHConsiderada = true;
			}catch (Exception e) {
				permiteEditarCHConsiderada = false;
			}			
		}
		return permiteEditarCHConsiderada;
	}

	public void setPermiteEditarCHConsiderada(Boolean permiteEditarCHConsiderada) {
		this.permiteEditarCHConsiderada = permiteEditarCHConsiderada;
	}	
	
	
	public Boolean getConsultaDataScroller() {
		if (consultaDataScroller == null) {
			consultaDataScroller = false;
		}
		return consultaDataScroller;
	}

	public void setConsultaDataScroller(Boolean consultaDataScroller) {
		this.consultaDataScroller = consultaDataScroller;
	}
	
	public void scrollerListener() throws Exception {
	
		setConsultaDataScroller(true);
		consultar();
	}
	
	

	
	
	public Boolean getIsAnexoPDF() {
		return Uteis.isAtributoPreenchido(getCaminhoPreview()) && (getCaminhoPreview().endsWith(".pdf?embedded=true") || getCaminhoPreview().endsWith(".PDF?embedded=true")) ;	
	}
	
	public Boolean getIsAnexoImagem() {
		return Uteis.isAtributoPreenchido(getCaminhoPreview()) && (
				getCaminhoPreview().endsWith(".jpeg?embedded=true") || getCaminhoPreview().endsWith(".JPEG?embedded=true") || getCaminhoPreview().endsWith(".jpg?embedded=true")
		        || getCaminhoPreview().endsWith(".JPG?embedded=true") || getCaminhoPreview().endsWith(".png?embedded=true") || getCaminhoPreview().endsWith(".PNG?embedded=true")
		        || getCaminhoPreview().endsWith(".gif?embedded=true") || getCaminhoPreview().endsWith(".GIF?embedded=true") || getCaminhoPreview().endsWith(".bmp?embedded=true")
		        || getCaminhoPreview().endsWith(".BMP?embedded=true") || getCaminhoPreview().endsWith(".ico?embedded=true") || getCaminhoPreview().endsWith(".ICO?embedded=true"));	
	}
	
	public void removerRegistroAtividadeComplementarMatriculaVO() throws Exception {
		try {
			RegistroAtividadeComplementarMatriculaVO obj = (RegistroAtividadeComplementarMatriculaVO) context().getExternalContext().getRequestMap().get("registroAtividadeComplementarMatriculaVOItens");
			getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().removerRegistroAtividadeComplementarListaVOs(obj, getListaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs(), getUsuarioLogadoClone());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void calcularCargaHorariaTotal(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatricula) {
	 RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
		try {
			obj = getFacadeFactory().getAcompanhamentoAtividadeComplementarFacade().consultarCargaHorariaTotal(registroAtividadeComplementarMatricula.getMatriculaVO().getMatricula(), false, registroAtividadeComplementarMatricula.getMatriculaVO().getGradeCurricularAtual().getCodigo(), getUsuarioLogado());
			getAtividadeComplementarMatricula().setCargaHorariaExigida(obj.getCargaHorariaExigida());
			getAtividadeComplementarMatricula().setCargaHorariaConsiderada(obj.getCargaHorariaConsiderada());
			getAtividadeComplementarMatricula().setCargaHorariaIndeferido(obj.getCargaHorariaIndeferido());
			getAtividadeComplementarMatricula().setCargaHorariaAguardandoDeferimento(obj.getCargaHorariaAguardandoDeferimento());
			getAtividadeComplementarMatricula().setCargaHorariaRealizada(obj.getCargaHorariaRealizada());
			getAtividadeComplementarMatricula().setCargaHorariaPendente(obj.getCargaHorariaPendente());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
