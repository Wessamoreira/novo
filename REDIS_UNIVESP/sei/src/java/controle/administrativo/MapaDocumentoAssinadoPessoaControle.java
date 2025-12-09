package controle.administrativo;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import com.google.common.util.concurrent.RateLimiter;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.OperacaoDeVinculoEstagioEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.lacuna.CertificadoLacunaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.academico.DocumentoAssinado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import webservice.AlwaysListTypeAdapterFactory;
import webservice.aws.s3.ServidorArquivoOnlineS3RS;

@Controller("MapaDocumentoAssinadoPessoaControle")
@Scope("viewScope")
@Lazy
public class MapaDocumentoAssinadoPessoaControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8096486754061277836L;
	private DataModelo listaDocumentosPendentes;
	private DataModelo listaDocumentosRejeitados;
	private DataModelo listaDocumentosAssinados;
	private String ano;
	private String semestre;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private MatriculaVO matriculaVO;
	private TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum;
	private Date dataInicio;
	private Date dataFinal;
	private PessoaVO pessoaAssinaturaVO;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<TurmaVO> turmaVOs;
	private String valorConsultaDisciplina;
	private String campoConsultaDisciplina;
	private List<DisciplinaVO> disciplinaVOs;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<CursoVO> cursoVOs;
	private String valorConsultaMatricula;
	private String campoConsultaMatricula;
	private List<MatriculaVO> matriculaVOs;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemTipoOrigemDocumentoAssinadoEnum;
	private DocumentoAssinadoVO documentoAssinadoVO;
	private SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum;
	private DocumentoAssinadoPessoaVO documentoAssinadoPessoaExcluir;
	private DocumentoAssinadoVO documentoAssinadoExcluir;
	private String autocompleteValorMatricula;
	private List<DocumentoAssinadoVO> listaDocumentoAssinadosParaExclusao;
	private ProgressBarVO progressBarVO;
	private boolean existeVinculoDocumentoAssinado = false;
	private OperacaoDeVinculoEstagioEnum operacaoDeVinculoEstagioEnum;
	private String motivo;
	private String caminhoPreview;
	private ZipFile arquivoZipado;
	private List<String> listaMensagemImportaXmlDiploma;
	private List<String> listaMensagemErroImportaXmlDiploma;
	private DocumentoAssinadoVO documentoAssinadoUploadXMlDiploma;
	private DocumentoAssinadoVO documentoAssinadoRegrarRepresentacaoVisual;
	private List<String> listaErroXml;
	private String onCompleteRejeicaoDocumento;
	private String urlDownloadSeiSignature;
	private String motivoRejeicaoDocumentoAssinadoProvedorAssinatura;
	private Boolean existeDiplomaAssinado;
	private ProgressBarVO progressBarCertisign;
	private List<DocumentoAssinadoVO> listaDocumentoAssinadoErro;
	private Boolean extensaoLacunaInstalada;
	private String navegadorLogado;
	private List<SelectItem> listaSelectItemCertificadoInstalado;
	private List<CertificadoLacunaVO> listaCertificadoInstalado;
	private String jsonListaCertificadoInstalado;
	private String jsonListaDocumentoAssinadoAssinar;
	private String thumbprintCertificado;
	private Integer ordemAssinaturaXml;
	private Integer codigoPessoaLogadaAssinarXml;
	private List<SelectItem> listaSelectItemTipoAssinanteDiplomaDigital;
	private List<SelectItem> listaSelectItemTipoAssinanteHistoricoDigital;
	private List<SelectItem> listaSelectItemTipoAssinanteDocumentacaoAcademica;
	private List<SelectItem> listaSelectItemTipoAssinanteCurriculoEscolar;
	private String urlAcessoExternoAplicacao;
	private Boolean realizarAssinaturaXmlLote;
	private Boolean apresentarModalProgressBar;
	private ProgressBarVO progressBarTechCert;
	private DocumentoAssinadoVO documentoAssinadoTrocaResponsavel;
	private DocumentoAssinadoPessoaVO documentoAssinadoPessoaTrocaResponsavel;
	public Boolean permitirAlterarResponsavelAssinatura;
	private List<SelectItem> listaSelectItemPessoaRetirarDocumentoVO;
	private List<DocumentoAssinadoVO> documentoAssinadoTrocaResponsavelLote;
	private PessoaVO pessoaNovaAssinaturaVO;
	private PessoaVO pessoaRetirarDocumentoVO;
		
	public MapaDocumentoAssinadoPessoaControle() {
		super();	
	}

	@PostConstruct
	private void inicializarDados() {
		getPessoaAssinaturaVO().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
		getPessoaAssinaturaVO().setNome(getUsuarioLogado().getPessoa().getNome());
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa() && Uteis.isAtributoPreenchido(getUnidadeEnsinoLogadoClone())) {
			getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
		}
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemTurma();
		setCaminhoPreview("");
		consultarDocumentos();
		if ((Objects.isNull(getAplicacaoControle().getMapDocumentoAssinadoAssinarXml()) || getAplicacaoControle().getMapDocumentoAssinadoAssinarXml().isEmpty()) 
				|| ((getAplicacaoControle().getProgressBarAssinarXmlMec().getAtivado() || getAplicacaoControle().getProgressBarAssinarXmlMec().getPollAtivado()) && getAplicacaoControle().getProgressBarAssinarXmlMec().getForcarEncerramento())) {
			getAplicacaoControle().limparAplicacaoMapDocumentoAssinarXml();
			getAplicacaoControle().getProgressBarAssinarXmlMec().setForcarEncerramento(Boolean.TRUE);
			getAplicacaoControle().getProgressBarAssinarXmlMec().resetar();
		}
	}
		
	
	public void consultarDocumentos() {
		getListaDocumentosAssinados().setOffset(0);
		getListaDocumentosPendentes().setOffset(0);
		getListaDocumentosRejeitados().setOffset(0);
		
		getListaDocumentosAssinados().setLimitePorPagina(10);
		getListaDocumentosAssinados().setPage(0);
		getListaDocumentosAssinados().setPaginaAtual(0);
		
		getListaDocumentosPendentes().setLimitePorPagina(10);
		getListaDocumentosPendentes().setPage(0);
		getListaDocumentosPendentes().setPaginaAtual(0);
		
		getListaDocumentosRejeitados().setLimitePorPagina(10);
		getListaDocumentosRejeitados().setPage(0);
		getListaDocumentosRejeitados().setPaginaAtual(0);
		
		
		consultarDocumentosPendentes();
		consultarDocumentosRejeitados();
		consultarDocumentosAssinados();
	}
	
	public void consultarDocumentosPendentes() {
		try {		
			getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentos(getListaDocumentosPendentes(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), getMatriculaVO(), getTipoOrigemDocumentoAssinadoEnum(), getDataInicio(), getDataFinal(), SituacaoDocumentoAssinadoPessoaEnum.PENDENTE, getPessoaAssinaturaVO(), getUsuarioLogado(), getListaDocumentosPendentes().getLimitePorPagina(), getListaDocumentosPendentes().getOffset(), Uteis.isAtributoPreenchido(getTipoOrigemDocumentoAssinadoEnum()) && getTipoOrigemDocumentoAssinadoEnum().isXmlMec() ? getOrdemAssinaturaXml() : null);
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	
	public void paginarDocumentosPendentes(DataScrollEvent DataScrollEvent) {
		getListaDocumentosPendentes().setPaginaAtual(DataScrollEvent.getPage());
		getListaDocumentosPendentes().setPage(DataScrollEvent.getPage());
		consultarDocumentosPendentes();
	}
	
	public void consultarDocumentosRejeitados() {
		try {
			getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentos(getListaDocumentosRejeitados(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), getMatriculaVO(), getTipoOrigemDocumentoAssinadoEnum(), getDataInicio(), getDataFinal(), SituacaoDocumentoAssinadoPessoaEnum.REJEITADO, getPessoaAssinaturaVO(), getUsuarioLogado(), getListaDocumentosRejeitados().getLimitePorPagina(), getListaDocumentosRejeitados().getOffset());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void paginarDocumentosRejeitados(DataScrollEvent DataScrollEvent) {
		getListaDocumentosRejeitados().setPaginaAtual(DataScrollEvent.getPage());
		getListaDocumentosRejeitados().setPage(DataScrollEvent.getPage());
		consultarDocumentosRejeitados();
	}

	
	public void consultarDocumentosAssinados() {
		try {
			getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentos(getListaDocumentosAssinados(), getUnidadeEnsinoVO(), getCursoVO(), getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), getMatriculaVO(), getTipoOrigemDocumentoAssinadoEnum(), getDataInicio(), getDataFinal(), SituacaoDocumentoAssinadoPessoaEnum.ASSINADO, getPessoaAssinaturaVO(), getUsuarioLogado(), getListaDocumentosAssinados().getLimitePorPagina(), getListaDocumentosAssinados().getOffset());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void paginarDocumentosAssinados(DataScrollEvent DataScrollEvent) {
		getListaDocumentosAssinados().setPaginaAtual(DataScrollEvent.getPage());
		getListaDocumentosAssinados().setPage(DataScrollEvent.getPage());
		consultarDocumentosAssinados();
	}
	
	public DataModelo getListaDocumentosPendentes() {
		if(listaDocumentosPendentes == null) {
			listaDocumentosPendentes =  new DataModelo();
		}
		return listaDocumentosPendentes;
	}

	public void setListaDocumentosPendentes(DataModelo listaDocumentosPendentes) {
		this.listaDocumentosPendentes = listaDocumentosPendentes;
	}

	public DataModelo getListaDocumentosRejeitados() {
		if(listaDocumentosRejeitados == null) {
			listaDocumentosRejeitados =  new DataModelo();
		}
		return listaDocumentosRejeitados;
	}

	public void setListaDocumentosRejeitados(DataModelo listaDocumentosRejeitados) {
		this.listaDocumentosRejeitados = listaDocumentosRejeitados;
	}

	public DataModelo getListaDocumentosAssinados() {
		if(listaDocumentosAssinados == null) {
			listaDocumentosAssinados =  new DataModelo();
		}
		return listaDocumentosAssinados;
	}

	public void setListaDocumentosAssinados(DataModelo listaDocumentosAssinados) {
		this.listaDocumentosAssinados = listaDocumentosAssinados;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public TurmaVO getTurmaVO() {
		if(turmaVO == null) {
			turmaVO =  new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public DisciplinaVO getDisciplinaVO() {
		if(disciplinaVO == null) {
			disciplinaVO =  new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null) {
			unidadeEnsinoVO =  new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public CursoVO getCursoVO() {
		if(cursoVO == null) {
			cursoVO =  new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public Date getDataInicio() {		
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public PessoaVO getPessoaAssinaturaVO() {
		if(pessoaAssinaturaVO == null) {
			pessoaAssinaturaVO =  new PessoaVO();
		}
		return pessoaAssinaturaVO;
	}

	public void setPessoaAssinaturaVO(PessoaVO pessoaAssinaturaVO) {
		this.pessoaAssinaturaVO = pessoaAssinaturaVO;
	}

	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null) {
			matriculaVO =  new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public TipoOrigemDocumentoAssinadoEnum getTipoOrigemDocumentoAssinadoEnum() {
		return tipoOrigemDocumentoAssinadoEnum;
	}

	public void setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum) {
		this.tipoOrigemDocumentoAssinadoEnum = tipoOrigemDocumentoAssinadoEnum;
	}

	public String getValorConsultaTurma() {
		if(valorConsultaTurma == null) {
			valorConsultaTurma =  "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public String getCampoConsultaTurma() {
		if(campoConsultaTurma == null) {
			campoConsultaTurma =  "identificadorTurma";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List<TurmaVO> getTurmaVOs() {
		if(turmaVOs == null) {
			turmaVOs =  new ArrayList<TurmaVO>();
		}
		return turmaVOs;
	}

	public void setTurmaVOs(List<TurmaVO> turmaVOs) {
		this.turmaVOs = turmaVOs;
	}

	public String getValorConsultaDisciplina() {
		if(valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public String getCampoConsultaDisciplina() {
		if(campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "nome";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public List<DisciplinaVO> getDisciplinaVOs() {
		if(disciplinaVOs == null) {
			disciplinaVOs =  new ArrayList<DisciplinaVO>(0);
		}
		return disciplinaVOs;
	}

	public void setDisciplinaVOs(List<DisciplinaVO> disciplinaVOs) {
		this.disciplinaVOs = disciplinaVOs;
	}
	
	public List<SelectItem> tipoConsultaComboDisciplina;
	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if(tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboDisciplina;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt,  false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				if (getValorConsultaDisciplina().trim().isEmpty() || getValorConsultaDisciplina().trim().contains("%%") || getValorConsultaDisciplina().trim().length() < 3) {
					throw new Exception("Informe 3 caracteres válidos para realizar a consulta.");
				}
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setDisciplinaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setDisciplinaVOs(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarDisciplina() {
		limparDisciplina();
		setDisciplinaVO((DisciplinaVO)getRequestMap().get("disciplinaItem"));
	}
	
	public void limparDisciplina() {
		setDisciplinaVO(new DisciplinaVO());
	}
	
	public List<SelectItem> tipoConsultaComboCurso;
	public List<SelectItem> getTipoConsultaComboCurso() {
		if(tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public String getValorConsultaCurso() {
		if(valorConsultaCurso == null) {
			valorConsultaCurso =  "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public String getCampoConsultaCurso() {
		if(campoConsultaCurso == null) {
			campoConsultaCurso =  "nome";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public List<CursoVO> getCursoVOs() {
		if(cursoVOs == null) {
			cursoVOs =  new ArrayList<CursoVO>(0);
		}
		return cursoVOs;
	}

	public void setCursoVOs(List<CursoVO> cursoVOs) {
		this.cursoVOs = cursoVOs;
	}
	
	@SuppressWarnings("unchecked")
	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(valorInt, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				if (getValorConsultaCurso().trim().isEmpty() || getValorConsultaCurso().trim().contains("%%") || getValorConsultaCurso().trim().length() < 3) {
					throw new Exception("Informe 3 caracteres válidos para realizar a consulta.");
				}
				objs = getFacadeFactory().getCursoFacade().consultarPorNomeCurso_UnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setCursoVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setCursoVOs(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarCurso() {
		setCursoVO((CursoVO)getRequestMap().get("cursoItem"));
	}
	
	public void limparCurso() {
		setCursoVO(null);		
	}
	
	public void limparTurma() {
		setTurmaVO(null);		
		limparDisciplina();
	}
	
	public void limparMatricula() {
		setMatriculaVO(null);
	}
	
	public void limparPessoa() {
		setPessoaAssinaturaVO(null);
	}
	

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			
				if (getCampoConsultaTurma().equals("identificadorTurma")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), 0,  getUnidadeEnsinoVO().getCodigo().intValue(), false, false, true, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
				if (getCampoConsultaTurma().equals("nomeCurso")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, false, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
				setTurmaVOs(objs);
				setMensagemID("msg_dados_consultados");			
		} catch (Exception e) {
			setTurmaVOs(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
		setTurmaVO(obj);
		limparDisciplina();
		if(obj.getIntegral()) {
			setAno("");
			setSemestre("");
		}else if(obj.getSemestral()) {
			setAno(null);
			setSemestre(null);
		}else {
			setAno(null);
		}
		valorConsultaTurma = "";
		campoConsultaTurma = "";
		turmaVOs.clear();
		montarListaSelectItemDisciplinaTurma();
	}

	public List<SelectItem> tipoConsultaComboTurma;
	public List<SelectItem> getTipoConsultaComboTurma() {
		if(tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public List<SelectItem> tipoConsultaComboSemestre;
	public List<SelectItem> getTipoConsultaComboSemestre() {
		if(tipoConsultaComboSemestre == null) {
			tipoConsultaComboSemestre = new ArrayList<SelectItem>(0);
			tipoConsultaComboSemestre.add(new SelectItem("", ""));
			tipoConsultaComboSemestre.add(new SelectItem("1", "1º"));
			tipoConsultaComboSemestre.add(new SelectItem("2", "2º"));
		}
		return tipoConsultaComboSemestre;
	}
	
	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}		
			setMatriculaVO(objAluno);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}
	
	public List<SelectItem> tipoConsultaComboAluno;
	public List<SelectItem> getTipoConsultaComboAluno() {
		if(tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAluno;
	}
	

	public String getValorConsultaMatricula() {
		if(valorConsultaMatricula == null) {
			valorConsultaMatricula =  "";
		}
		return valorConsultaMatricula;
	}

	public void setValorConsultaMatricula(String valorConsultaMatricula) {
		this.valorConsultaMatricula = valorConsultaMatricula;
	}

	public String getCampoConsultaMatricula() {
		if(campoConsultaMatricula == null) {
			campoConsultaMatricula =  "nomePessoa";
		}
		return campoConsultaMatricula;
	}

	public void setCampoConsultaMatricula(String campoConsultaMatricula) {
		this.campoConsultaMatricula = campoConsultaMatricula;
	}

	public List<MatriculaVO> getMatriculaVOs() {
		if(matriculaVOs == null) {
			matriculaVOs =  new ArrayList<MatriculaVO>(0);
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	}
	
	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaMatricula().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaMatricula().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaMatricula(),
						getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaMatricula().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaMatricula(),
						getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaMatricula().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaMatricula(),
						getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaMatricula().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaMatricula(),
						getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			}
			setMatriculaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMatriculaVOs(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarAluno() {
		MatriculaVO obj = (MatriculaVO)getRequestMap().get("matriculaItem");
		setMatriculaVO(obj);
		getMatriculaVO().setMatricula(obj.getMatricula());
		selecionarMatriculaPorMatricula();
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if(listaSelectItemTurma == null) {
			listaSelectItemTurma =  new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void montarListaSelectItemTurma() {
		if(getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
		List listaResultado = null;
		Iterator i = null;
		Map<Integer, String> hashTurmasAdicionadas = new HashMap<Integer, String>(0);
		try {
			if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				listaResultado = consultarTurmaPorProfessor();
			}else {
				listaResultado = consultarTurmaPorCoordenador();
			}
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();
				if (!getUsuarioLogado().getVisaoLogar().equals("professor")) {
					if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
						if (turma.getTurmaAgrupada()) {
							value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
						} else {
							value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
						}
						getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
						hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
					}
				} else {
					if (turma.getTurmaAgrupada()) {
						List<TurmaVO> listaTurmasAgrupadas = getFacadeFactory().getTurmaFacade().consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamento(turma.getCodigo(), false, getUsuarioLogado());
						for (TurmaVO turmaParticipaAgrupamento : listaTurmasAgrupadas) {
							if (!hashTurmasAdicionadas.containsKey(turmaParticipaAgrupamento.getCodigo())) {
								value = turmaParticipaAgrupamento.getIdentificadorTurma() + " - Curso " + turmaParticipaAgrupamento.getCurso().getNome() + " - Turno " + turmaParticipaAgrupamento.getTurno().getNome();
								getListaSelectItemTurma().add(new SelectItem(turmaParticipaAgrupamento.getCodigo(), value));
								hashTurmasAdicionadas.put(turmaParticipaAgrupamento.getCodigo(), turmaParticipaAgrupamento.getIdentificadorTurma());
							}
						}
						if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
							value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
							getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
							hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
						}
					} else {
						if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
							if (turma.getTurmaAgrupada()) {
								value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
							} else {
								value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
							}							
							getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
							hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
						}
					}
				}
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List<SelectItem>) getListaSelectItemTurma(), ordenador);
		} catch (Exception e) {
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			hashTurmasAdicionadas = null;
			i = null;
		}
		}
	}
	
	public List<TurmaVO> consultarTurmaPorCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false);
		} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), true, false);
		} else {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true);
		}		
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if(listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina =  new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}	

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}
	
	public void carregarDadosTurma() {
		try {
			if(Uteis.isAtributoPreenchido(getTurmaVO())) {
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
				montarListaSelectItemDisciplinaTurma();
			}else {
				setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
				setTurmaVO(null);
			}
		} catch (Exception e) {
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		}
	}
	
	public void montarListaSelectItemDisciplinaTurma() {	
		setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		try {			
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				@SuppressWarnings("unchecked")
				List<DisciplinaVO> resultado = getFacadeFactory().getDisciplinaFacade().consultaRapidaPorDisciplinaProfessorTurmaAgrupada(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getSemestre(), getAno(), false, false, getUsuarioLogado());
				setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome", true));
			} else {
				List<HorarioTurmaDisciplinaProgramadaVO> resultado = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurmaVO().getCodigo(), false, false, 0);
				setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigoDisciplina", "nomeDisciplina", true));
			}
		} catch (Exception e) {
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		}
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino =  new ArrayList<SelectItem>(0);
			montarListaSelectItemUnidadeEnsino();
		}
		return listaSelectItemUnidadeEnsino;
	}
	
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			boolean campoEmBranco = false;
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				campoEmBranco = true;
			}
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", campoEmBranco));			
		} catch (Exception e) {
			
		}
	}
	
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {		
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemTipoOrigemDocumentoAssinadoEnum() {
		if(listaSelectItemTipoOrigemDocumentoAssinadoEnum == null) {
			listaSelectItemTipoOrigemDocumentoAssinadoEnum =  new ArrayList<SelectItem>();
			listaSelectItemTipoOrigemDocumentoAssinadoEnum.add(new SelectItem(null, ""));
			List<TipoOrigemDocumentoAssinadoEnum> tipoOrigemDocumentoAssinadoEnums = new ArrayList<>(Arrays.asList(TipoOrigemDocumentoAssinadoEnum.values()));
			for(TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinadoEnum: tipoOrigemDocumentoAssinadoEnums) {
				if(!tipoOrigemDocumentoAssinadoEnum.equals(TipoOrigemDocumentoAssinadoEnum.NENHUM)) {
					if(getUsuarioLogado().getIsApresentarVisaoProfessor() && tipoOrigemDocumentoAssinadoEnum.isApresentarVisaoProfessor()) {
						listaSelectItemTipoOrigemDocumentoAssinadoEnum.add(new SelectItem(tipoOrigemDocumentoAssinadoEnum, tipoOrigemDocumentoAssinadoEnum.isXmlMec() ? "XML - " + tipoOrigemDocumentoAssinadoEnum.getDescricao() : tipoOrigemDocumentoAssinadoEnum.getDescricao()));
					}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador() && tipoOrigemDocumentoAssinadoEnum.isApresentarVisaoCoordenador()) {
						listaSelectItemTipoOrigemDocumentoAssinadoEnum.add(new SelectItem(tipoOrigemDocumentoAssinadoEnum, tipoOrigemDocumentoAssinadoEnum.isXmlMec() ? "XML - " + tipoOrigemDocumentoAssinadoEnum.getDescricao() : tipoOrigemDocumentoAssinadoEnum.getDescricao()));
					}else if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
						listaSelectItemTipoOrigemDocumentoAssinadoEnum.add(new SelectItem(tipoOrigemDocumentoAssinadoEnum, tipoOrigemDocumentoAssinadoEnum.isXmlMec() ? "XML - " + tipoOrigemDocumentoAssinadoEnum.getDescricao() : tipoOrigemDocumentoAssinadoEnum.getDescricao()));
					}
				}
			}			
			listaSelectItemTipoOrigemDocumentoAssinadoEnum.sort(Comparator.comparing(SelectItem::getLabel));
		}
		return listaSelectItemTipoOrigemDocumentoAssinadoEnum;
	}

	public void setListaSelectItemTipoOrigemDocumentoAssinadoEnum(
			List<SelectItem> listaSelectItemTipoOrigemDocumentoAssinadoEnum) {
		this.listaSelectItemTipoOrigemDocumentoAssinadoEnum = listaSelectItemTipoOrigemDocumentoAssinadoEnum;
	}
	
	public void realizarRegistroAssinaturaDocumento(Boolean retornarThrows) throws Exception {
		realizarRegistroAssinaturaDocumento(getDocumentoAssinadoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), retornarThrows);
		consultarDocumentosPendentes();
		consultarDocumentosAssinados();
		getLoginControle().setQtdeDocumentoAssinarPendenteUsuario(null);
	}
	
	public void realizarRegistroAssinaturaDocumento(DocumentoAssinadoVO documentoAssinadoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Boolean retornarThrows) throws Exception {
		try {
			File arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+documentoAssinadoVO.getArquivo().getPastaBaseArquivo()+File.separator+documentoAssinadoVO.getArquivo().getNome());
			if(arquivo.exists()) {
				getFacadeFactory().getDocumentoAssinadoFacade().excutarVerificacaoPessoasParaAssinarDocumento(documentoAssinadoVO, arquivo, configuracaoGeralSistemaVO, getPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(), true, usuarioVO);
			}else {
				throw new Exception("O arquivo "+documentoAssinadoVO.getArquivo().getNome()+" não existe mais no diretório ("+(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator+documentoAssinadoVO.getArquivo().getPastaBaseArquivo())+").");
			}
		} catch (Exception e) {
			if (retornarThrows) {
				throw e;
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarDocumentoAssinado() {
		try {
			setRealizarAssinaturaXmlLote(Boolean.FALSE);
			setOncompleteModal(Constantes.EMPTY);
			if (Uteis.isAtributoPreenchido(getTipoOrigemDocumentoAssinadoEnum()) && getTipoOrigemDocumentoAssinadoEnum().isXmlMec()) {
				validarDadosIniciarConsultaCertificadosIntalados();
			}
			setDocumentoAssinadoVO((DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItem"));
			//setDocumentoAssinadoVO(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorChavePrimaria(getDocumentoAssinadoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setSituacaoDocumentoAssinadoPessoaEnum(SituacaoDocumentoAssinadoPessoaEnum.ASSINADO);
			for(DocumentoAssinadoPessoaVO documentoAssinadoPessoaVO: getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa()) {
				if(documentoAssinadoPessoaVO.getPessoaVO().getCodigo().equals(getUsuarioLogado().getPessoa().getCodigo())) {
					setSituacaoDocumentoAssinadoPessoaEnum(documentoAssinadoPessoaVO.getSituacaoDocumentoAssinadoPessoaEnum());
				}
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO, e, getUsuarioLogadoClone(), "", "", true);
		}
	}
	
	public void excluirDocumentoAssinado() {
		try {
			setOnCompleteRejeicaoDocumento(Constantes.EMPTY);
			Boolean possuiProvededorAssinatura = getDocumentoAssinadoExcluir().getListaDocumentoAssinadoPessoa().stream().anyMatch(n -> n.getProvedorAssinatura().equals("LACUNAS") || n.getProvedorAssinatura().equals("BRY"));
			if (possuiProvededorAssinatura) {
				setOnCompleteRejeicaoDocumento("RichFaces.$('panelAvisoRejeicaoDocumentoAssinadoProvedorAssinatura').show()");
				return;
			}
			ConfiguracaoEstagioObrigatorioVO configEstagio = getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadrao(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
			getFacadeFactory().getDocumentoAssinadoFacade().executarExclusaoTodosDocumentoAssinadoSelecionados(getDocumentoAssinadoExcluir(), getOperacaoDeVinculoEstagioEnum(), getMotivo(), getUsuarioLogadoClone(), configEstagio);			
			consultarDocumentosPendentes();
			consultarDocumentosAssinados();
			consultarDocumentosRejeitados();
			if(getDocumentoAssinadoExcluir().getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio()) {
				setMensagemDetalhada(MSG_TELA.msg_dados_operacao.name(), "");	
			}else {
//				setMensagemDetalhada(MSG_TELA.msg_dados_excluidos.name(), UteisJSF.internacionalizar("msg_Documento_Assinado_Excluido"));
				setMensagemID("msg_Documento_Assinado_Excluido", Uteis.SUCESSO, Boolean.TRUE);
			}
			setOnCompleteRejeicaoDocumento("RichFaces.$('panelAlertaExclusao').hide();");
			setDocumentoAssinadoExcluir(new DocumentoAssinadoVO());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarDownloadArquivoProvedorCertisin()  {
		try {
			DocumentoAssinadoVO obj = (DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItem");
			getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
			realizarDownloadArquivo(obj.getArquivo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarDownloadArquivoProvedorTechCert()  {
		try {
			DocumentoAssinadoVO obj = (DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItem");
			getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
			realizarDownloadArquivo(obj.getArquivo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if(documentoAssinadoVO == null) {
			documentoAssinadoVO =  new DocumentoAssinadoVO();
		}
		return documentoAssinadoVO;
	}

	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}

	public SituacaoDocumentoAssinadoPessoaEnum getSituacaoDocumentoAssinadoPessoaEnum() {
		if(situacaoDocumentoAssinadoPessoaEnum == null) {
			situacaoDocumentoAssinadoPessoaEnum = SituacaoDocumentoAssinadoPessoaEnum.ASSINADO;
		}
		return situacaoDocumentoAssinadoPessoaEnum;
	}

	public void setSituacaoDocumentoAssinadoPessoaEnum(
			SituacaoDocumentoAssinadoPessoaEnum situacaoDocumentoAssinadoPessoaEnum) {
		this.situacaoDocumentoAssinadoPessoaEnum = situacaoDocumentoAssinadoPessoaEnum;
	}
	

	public boolean isApresentarComboUnidadeEnsino() {
		return getUsuarioLogado().getIsApresentarVisaoAdministrativa() && !Uteis.isAtributoPreenchido(getUnidadeEnsinoLogadoClone());
	}
	
	public boolean isApresentarComboTurma() {
		return !getUsuarioLogado().getIsApresentarVisaoAdministrativa();
	}
	
	public boolean isApresentarComboDisciplina() {
		return Uteis.isAtributoPreenchido(getTurmaVO());
	}
	
	public boolean isApresentarConsultaDisciplina() {
		return getUsuarioLogado().getIsApresentarVisaoAdministrativa() && !Uteis.isAtributoPreenchido(getTurmaVO());
	}
	
	public boolean isApresentarConsultaCurso() {
		return getUsuarioLogado().getIsApresentarVisaoAdministrativa();
	}
	
	public boolean isApresentarConsultaTurma() {
		return getUsuarioLogado().getIsApresentarVisaoAdministrativa();
	}
	
	public boolean isApresentarConsultaMatricula() {
		return getUsuarioLogado().getIsApresentarVisaoAdministrativa();
	}
	
	public boolean isApresentarConsultaPessoa() {
		return getUsuarioLogado().getIsApresentarVisaoAdministrativa();
	}
	
	public Boolean permitirConsultarPessoa;
	public Boolean getPermitirConsultarPessoa() {
		if(permitirConsultarPessoa == null) {
			try {
				if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("PemitirVisualizarDocumentoAssinadoOutrasPessoas", getUsuarioLogado())) {
					permitirConsultarPessoa = true;
				}else {
					permitirConsultarPessoa = false;
				}
			}catch (Exception e) {
				permitirConsultarPessoa = false;
			}
		}
		return permitirConsultarPessoa;
	}
	
	
	
	public String getValorConsultaFuncionario() {
		if(valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if(campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "nome";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if(listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>();
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(), 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "", 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "", 0, null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionarioPrincipal() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		setPessoaAssinaturaVO(obj.getPessoa());		
	}
	
	public List<SelectItem> tipoConsultaComboFuncionario;
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if(tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFuncionario.add(new SelectItem("cargo", "Cargo"));
			tipoConsultaComboFuncionario.add(new SelectItem("departamento", "Departamento"));
		}
		return tipoConsultaComboFuncionario;
	}

	public String getUrlDonloadSV() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return "location.href='../../DownloadSV'";
		}else {
			return "location.href='../DownloadSV'";
		}
	}
	
	public String getMensagemSeiSignature() {
		StringBuilder sb = new StringBuilder();
		sb.append("<p style=\"text-align: center;\" center;\\\"=\"\"><strong>Requisitos para Utilizar o Sei Signature</strong></p> <p style=\"\\&quot;text-align:\" left;\\\"=\"\"><strong><br></strong></p> <p style=\"\\&quot;text-align:\" left;\\\"=\"\"><strong>Para garantir o bom funcionamento do Sei Signature, é fundamental que seu computador atenda aos seguintes requisitos:</strong></p> <p>1 - Java: Instale a versão \"1.8.0 (JDK)\" do Java em https://www.java.com/pt-BR/download/. O Java é essencial para a execução de diversas funcionalidades do software.</p> <p>2 - Navegador: Os navegadores recomendáveis para a utilização do Sei Signature são os: <strong>(Google, FifeFox, Microsoft Edge)</strong>, pois com esses navegadores garantimos uma ótima experiencia de usabilidade e também esses navegadores são os navegadores disponibilizam a extensão que será utilizada para as assinaturas de xml.</p> <p>3 - Certificado Digital A3 (E-CPF ou E-CNPJ): Adquira um certificado digital A3 válido e instale-o corretamente em seu sistema operacional (Windows ou Mac). O certificado A3, armazenado em um cartão ou token, é utilizado para garantir a autenticidade das assinaturas eletrônicas.</p> <p><br></p> <p><strong>Como Funciona:</strong></p> <p>1 - Instalação: Certifique-se de que o Java e o navegador que será utilizado estão instalados e atualizados.</p> <p>2 - Certificado Digital: Instale o certificado A3 seguindo as instruções do fornecedor</p> <p>3 - Acesso ao Sei Signature: Após a instalação do certificado, você poderá acessar o Sei Signature e iniciar o processo de assinatura eletrônica.</p> <p><br></p> <p><strong>Observação:&nbsp;</strong>A validade do certificado digital A3 varia entre 1 e 5 anos. É importante verificar a data de validade do seu certificado e renová-lo antes da expiração para evitar interrupções no processo de assinatura.<br></p>");
		return sb.toString();					
	}
	

	private Boolean permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso;
	
	public Boolean getPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso() {
		if(permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso == null){
			try {
				DocumentoAssinado.verificarPermissaoUsuarioFuncionalidade("PermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso", getUsuarioLogado());
				permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso = true;
			} catch (Exception e) {
				permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso = false;
			}
			
		}
		return permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso;
	}

	public void setPermitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso(Boolean permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso) {
		this.permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso = permitirAssinaturaDiarioPeloProfessorAposAssinaturaCoordenadorCurso;
	}
	
	
	public DocumentoAssinadoPessoaVO getDocumentoAssinadoPessoaExcluir() {
		if (documentoAssinadoPessoaExcluir == null) {
			documentoAssinadoPessoaExcluir = new DocumentoAssinadoPessoaVO();
		}
		return documentoAssinadoPessoaExcluir;
	}

	public void setDocumentoAssinadoPessoaExcluir(DocumentoAssinadoPessoaVO documentoAssinadoPessoaExcluir) {
		this.documentoAssinadoPessoaExcluir = documentoAssinadoPessoaExcluir;
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

	public void excluirDocumentoAssinadoPessoa() {
		try {
			if (getDocumentoAssinadoPessoaExcluir().getProvedorAssinatura().equals("BRY") || getDocumentoAssinadoPessoaExcluir().getProvedorAssinatura().equals("LACUNAS")) {
				throw new Exception("Essa assinatura foi realizada com um provedor de assinatura, portanto não pode ser excluída.");
			}
			realizarVerificacaoDocumentoPendenteAssinaturaPorProvedorTechCert();
			ConfiguracaoGEDVO configGEDVO = getAplicacaoControle()
					.getConfiguracaoGEDPorUnidadeEnsino(getDocumentoAssinadoVO().getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
			getFacadeFactory().getDocumentoAssinadoFacade().realizarExclusaoParticipantDiscardTechCert(getDocumentoAssinadoPessoaExcluir(), configGEDVO, getDocumentoAssinadoVO());
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().excluirPendente(getDocumentoAssinadoPessoaExcluir(), getUsuarioLogado());
			//se for removido o ultimo assinante nao é atualizada a ordem pois não alterou a fila
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().reordenarAssinatesDocumentoAssinado(getDocumentoAssinadoVO(), getUsuarioLogado(), configGEDVO);
//			getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumet(configGEDVO, getDocumentoAssinadoVO().getChaveProvedorDeAssinatura())
			consultarDocumentosPendentes();
			consultarDocumentosAssinados();
			consultarDocumentosRejeitados();
//			setMensagemDetalhada(MSG_TELA.msg_dados_excluidos.name(), UteisJSF.internacionalizar("msg_Documento_Assinado_Excluido"));
			setMensagemID("msg_Documento_Assinado_Excluido", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
		}
	}
	
	public void modalExcluirDocumentoAssinadoPessoa() {
		try {
			setDocumentoAssinadoPessoaExcluir((DocumentoAssinadoPessoaVO)getRequestMap().get("pessoaItem"));
			selecionarDocumentoAssinado();
			setDocumentoAssinadoVO((DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItem"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO, e, getUsuarioLogadoClone(), "", "", true);
		}
	}

	public void modalTrocarResponsavelDocumentoAssinadoPessoa() {
		try {
			setDocumentoAssinadoPessoaTrocaResponsavel((DocumentoAssinadoPessoaVO)getRequestMap().get("pessoaItem"));
			setDocumentoAssinadoTrocaResponsavel((DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItem"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO, e, getUsuarioLogadoClone(), "", "", true);
		}
	}

	public void modalTrocarResponsavelDocumentoAssinadoPessoaEmLote() {
		try {
			getListaSelectItemPessoaRetirarDocumentoVO().clear();
			getDocumentoAssinadoTrocaResponsavelLote().clear();
			List<DocumentoAssinadoVO> listaDocumentosPendentesSelecionados = getListaDocumentosPendentes().getListaConsulta().stream().filter(p -> ((DocumentoAssinadoVO) p).getSelecionado()).map(pendente -> (DocumentoAssinadoVO) pendente).collect(Collectors.toList());
			if (listaDocumentosPendentesSelecionados.isEmpty()) {
				throw new Exception("Nenhum documento selecionado!");
			}

			listaDocumentosPendentesSelecionados.stream().filter(docAssinado -> docAssinado.getListaDocumentoAssinadoPessoa().stream()
							.anyMatch(d -> d.getSituacaoDocumentoAssinadoPessoaEnum() == SituacaoDocumentoAssinadoPessoaEnum.PENDENTE))
					.forEach(resultado -> getDocumentoAssinadoTrocaResponsavelLote().add(resultado));

			List<PessoaVO> pessoasDisponiveisParaAlteracao = getDocumentoAssinadoTrocaResponsavelLote().stream()
					.flatMap(docAssinado -> docAssinado.getListaDocumentoAssinadoPessoa().stream())
					.filter(d -> d.getSituacaoDocumentoAssinadoPessoaEnum() == SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)
					.filter(d -> d.getTipoPessoa() == TipoPessoa.FUNCIONARIO)
					.map(DocumentoAssinadoPessoaVO::getPessoaVO)
					.distinct().collect(Collectors.toList());

			pessoasDisponiveisParaAlteracao.forEach(p -> getListaSelectItemPessoaRetirarDocumentoVO().add(new SelectItem(p.getCodigo(), p.getNome())));
			setOnCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote("RichFaces.$('panelTrocaFuncionario').show()");
			limparMensagem();
		} catch (Exception e) {
			setOnCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote("RichFaces.$('panelTrocaFuncionario').hide()");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO, e, getUsuarioLogadoClone(), "", "", true);
		}
	}

	public void selecionarNovoFuncionarioAssinatura() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		setPessoaNovaAssinaturaVO(obj.getPessoa());
		setCampoConsultaFuncionario("nome");
		setValorConsultaFuncionario(Constantes.EMPTY);
		getListaConsultaFuncionario().clear();
	}

	public void realizarTrocaResponsavelAssinaturaDocumento() {
		try {
			getFacadeFactory().getDocumentoAssinadoPessoaFacade().realizarTrocaResponsavelAssinaturaDocumento(getDocumentoAssinadoTrocaResponsavel(), getPessoaNovaAssinaturaVO(), getDocumentoAssinadoPessoaTrocaResponsavel().getCodigo(), getConfiguracaoGeralSistemaVO(), getUsuarioLogado());
			consultarDocumentosPendentes();
			consultarDocumentosRejeitados();
			consultarDocumentosAssinados();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO, e, getUsuarioLogadoClone(), "", "", true);
		} finally {
			setDocumentoAssinadoTrocaResponsavel(new DocumentoAssinadoVO());
			setDocumentoAssinadoPessoaTrocaResponsavel(new DocumentoAssinadoPessoaVO());
			setPessoaNovaAssinaturaVO(new PessoaVO());
		}

	}

	public void realizarTrocaResponsavelAssinaturaDocumentoEmLote() {
		try {
			getListaDocumentoAssinadoErro().clear();
			setOncompleteModal(Constantes.EMPTY);
			try {
				if (!getDocumentoAssinadoTrocaResponsavelLote().isEmpty()) {
					double permitsPerSecond = 1_000.0 / Uteis.delayMillisTechCert;
					RateLimiter limiter = RateLimiter.create(permitsPerSecond);
					for (DocumentoAssinadoVO documentoAssinadoVO : getDocumentoAssinadoTrocaResponsavelLote()) {
						documentoAssinadoVO.setConsistirException(new ConsistirException());
						for (DocumentoAssinadoPessoaVO dap : documentoAssinadoVO.getListaDocumentoAssinadoPessoa()) {
							if (dap.getPessoaVO().getCodigo().equals(getPessoaRetirarDocumentoVO().getCodigo()) && dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()) {
								limiter.acquire();
								getProgressBarVO().setStatus("Processando documento de código " + dap.getCodigo());
								getFacadeFactory().getDocumentoAssinadoPessoaFacade().processarTrocaResponsavelAssinaturaDocumentoAssinado(dap, documentoAssinadoVO.getProvedorDeAssinaturaEnum(), getPessoaNovaAssinaturaVO(), documentoAssinadoVO.getUnidadeEnsinoVO().getCodigo(), documentoAssinadoVO, getUsuarioLogado());
							}
						}
						if (getProgressBarVO() != null) {
							getProgressBarVO().incrementar();
						}
					}
				}
			} catch (ConsistirException ce) {
				if (Uteis.isAtributoPreenchido(ce.getListaMensagemErro())) {
					documentoAssinadoVO.getConsistirException().getListaMensagemErro().addAll(ce.getListaMensagemErro());
				} else {
					documentoAssinadoVO.getConsistirException().getListaMensagemErro().add(Objects.nonNull(ce.getMessage()) ? ce.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
				}
				getListaDocumentoAssinadoErro().add(documentoAssinadoVO);
			} catch (Exception e) {
				documentoAssinadoVO.getConsistirException().getListaMensagemErro().add(Objects.nonNull(e.getMessage()) ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
				getListaDocumentoAssinadoErro().add(documentoAssinadoVO);
			}
			if (Uteis.isAtributoPreenchido(getListaDocumentoAssinadoErro())) {
				setOncompleteModal("RichFaces.$('panelListaErroDocumentoAssinado').show();");
			}
			consultarDocumentosPendentes();
			consultarDocumentosRejeitados();
			consultarDocumentosAssinados();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), e, getUsuarioLogadoClone(), "", "", true);
		} finally {
			setDocumentoAssinadoTrocaResponsavel(new DocumentoAssinadoVO());
			setDocumentoAssinadoPessoaTrocaResponsavel(new DocumentoAssinadoPessoaVO());
			setPessoaNovaAssinaturaVO(new PessoaVO());
			getDocumentoAssinadoTrocaResponsavelLote().clear();
			getProgressBarVO().setForcarEncerramento(true);
			getProgressBarVO().encerrar();
		}
	}

	public void realizarInicializacaoProgressBarAlteracaoResponsavelAssinaturaEmLote() {
		try {
			getProgressBarVO().resetar();
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().iniciar(0L, getDocumentoAssinadoTrocaResponsavelLote().size(), "Realizando Alteração dos Responsáveis", true, this, "realizarTrocaResponsavelAssinaturaDocumentoEmLote");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), e, getUsuarioLogadoClone(), "", "", true);
		}
	}

	public void limparFuncionarioNovaAssinatura() {
		setPessoaNovaAssinaturaVO(new PessoaVO());
	}
	
	public void modalExcluirDocumentoAssinado() {
		try {
			DocumentoAssinado.excluir("MapaDocumentoAssinadoPessoa", true, getUsuarioLogado());
			setDocumentoAssinadoExcluir((DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItem"));
			setMotivo("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
		}
	}
	
	public void selecionarTodosDocumentosPendentes() {		
		((List<DocumentoAssinadoVO>)  getListaDocumentosPendentes().getListaConsulta()).stream().forEach(p -> p.setSelecionado(Boolean.TRUE));		
	}
	
	public void desmarcarTodosDocumentosPendentes() {
		((List<DocumentoAssinadoVO>)  getListaDocumentosPendentes().getListaConsulta()).stream().forEach(p -> p.setSelecionado(Boolean.FALSE));	
	}
	
	public void selecionarTodosDocumentosAssinados() {		
		((List<DocumentoAssinadoVO>)  getListaDocumentosAssinados().getListaConsulta()).stream().forEach(p -> p.setSelecionado(Boolean.TRUE));		
	}
	
	public void desmarcarTodosDocumentosAssinados() {
		((List<DocumentoAssinadoVO>)  getListaDocumentosAssinados().getListaConsulta()).stream().forEach(p -> p.setSelecionado(Boolean.FALSE));	
	}
	
	public void realizarVerificacaoDocumentoPendenteAssinaturaPorProvedorCertiSign() {
		DocumentoAssinadoVO doc = (DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItem");
		try {	
			getListaDocumentoAssinadoErro().clear();
			List<DocumentoAssinadoVO> lista = new ArrayList<DocumentoAssinadoVO>(0);
			lista.add(doc);			
			getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorCertiSign(lista, getUsuarioLogado());
			consultarDocumentosPendentes();
			consultarDocumentosRejeitados();
			consultarDocumentosAssinados();
			setMensagemID("msg_dados_validados", Uteis.SUCESSO);
			
		}catch (ConsistirException e) {
			setMensagemDetalhada("msg_erro", e.getToStringMensagemErro(), Uteis.ERRO);	
	    } catch (Exception e) {
	    	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
		
		}		
	}

	public void realizarVerificacaoDocumentoPendenteAssinaturaPorProvedorTechCert() {
		DocumentoAssinadoVO doc = (DocumentoAssinadoVO)getRequestMap().get("documentoAssinadoItem");
		try {
			getListaDocumentoAssinadoErro().clear();
			List<DocumentoAssinadoVO> lista = new ArrayList<DocumentoAssinadoVO>(0);
			lista.add(doc);
			getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCert(lista, getUsuarioLogado());
			consultarDocumentosPendentes();
			consultarDocumentosRejeitados();
			consultarDocumentosAssinados();
			setMensagemID("msg_dados_validados", Uteis.SUCESSO);

		}catch (ConsistirException e) {
			setMensagemDetalhada("msg_erro", e.getToStringMensagemErro(), Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	@SuppressWarnings("unchecked")
	public void realizarVerificacaoDocumentosPendenteAssinaturaPorProvedorCertiSignTodosDocumentosSelecionados() {		
		try {	
			getListaDocumentoAssinadoErro().clear();
			setOncompleteModal(Constantes.EMPTY);
			List<DocumentoAssinadoVO> lista = ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().filter(documentoAssinado -> documentoAssinado.getSelecionado() && documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorCertisign()).collect(Collectors.toList());
			if (Uteis.isAtributoPreenchido(lista)) {
				for (DocumentoAssinadoVO documentoAssinadoVO : lista) {
					String mensagemDocumentoAssinado = "Documento assinado: " + documentoAssinadoVO.getCodigo() + ", tipo origem: " + documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().getDescricao();
					getProgressBarCertisign().setProgresso(getProgressBarCertisign().getProgresso() + 1);
					getProgressBarCertisign().setStatus("(" + getProgressBarCertisign().getProgresso() + " de " + getProgressBarCertisign().getMaxValue() + ") " + mensagemDocumentoAssinado);
					try {
						documentoAssinadoVO.setConsistirException(new ConsistirException());
						getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorCertiSign(documentoAssinadoVO, getProgressBarCertisign().getUsuarioVO());				
					} catch (ConsistirException ce) {
						if (Uteis.isAtributoPreenchido(ce.getListaMensagemErro())) {
							documentoAssinadoVO.getConsistirException().getListaMensagemErro().addAll(ce.getListaMensagemErro());
						} else {
							documentoAssinadoVO.getConsistirException().getListaMensagemErro().add(Objects.nonNull(ce.getMessage()) ? ce.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
						}
						getListaDocumentoAssinadoErro().add(documentoAssinadoVO);
					} catch (Exception e) {
						documentoAssinadoVO.getConsistirException().getListaMensagemErro().add(Objects.nonNull(e.getMessage()) ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
						getListaDocumentoAssinadoErro().add(documentoAssinadoVO);
					}
				}
			}
			if (Uteis.isAtributoPreenchido(getListaDocumentoAssinadoErro())) {
				setOncompleteModal("RichFaces.$('panelListaErroDocumentoAssinado').show();");
			}
		  	consultarDocumentosPendentes();
			consultarDocumentosRejeitados();
			consultarDocumentosAssinados();
			setMensagemID("msg_dados_validados", Uteis.SUCESSO);
		} catch (Exception e) {
	    	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
		} finally {
			getProgressBarCertisign().setForcarEncerramento(Boolean.TRUE);
		}
	}

	public void realizarVerificacaoDocumentosPendenteAssinaturaPorProvedorTechCertTodosDocumentosSelecionados() {
		try {
			getListaDocumentoAssinadoErro().clear();
			setOncompleteModal(Constantes.EMPTY);
			List<DocumentoAssinadoVO> lista = ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().filter(documentoAssinado -> documentoAssinado.getSelecionado() && documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorTechCert()).collect(Collectors.toList());
			double permitsPerSecond = 1_000.0 / Uteis.delayMillisTechCert;
			RateLimiter limiter = RateLimiter.create(permitsPerSecond);
			if (Uteis.isAtributoPreenchido(lista)) {
				for (DocumentoAssinadoVO documentoAssinadoVO : lista) {
					limiter.acquire();
					String mensagemDocumentoAssinado = "Documento assinado: " + documentoAssinadoVO.getCodigo() + ", tipo origem: " + documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().getDescricao();
					getProgressBarTechCert().setProgresso(getProgressBarTechCert().getProgresso() + 1);
					getProgressBarTechCert().setStatus("(" + getProgressBarTechCert().getProgresso() + " de " + getProgressBarTechCert().getMaxValue() + ") " + mensagemDocumentoAssinado);
					try {
						documentoAssinadoVO.setConsistirException(new ConsistirException());
						getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCert(documentoAssinadoVO, getProgressBarTechCert().getUsuarioVO());
					} catch (ConsistirException ce) {
						if (Uteis.isAtributoPreenchido(ce.getListaMensagemErro())) {
							documentoAssinadoVO.getConsistirException().getListaMensagemErro().addAll(ce.getListaMensagemErro());
						} else {
							documentoAssinadoVO.getConsistirException().getListaMensagemErro().add(Objects.nonNull(ce.getMessage()) ? ce.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
						}
						getListaDocumentoAssinadoErro().add(documentoAssinadoVO);
					} catch (Exception e) {
						documentoAssinadoVO.getConsistirException().getListaMensagemErro().add(Objects.nonNull(e.getMessage()) ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
						getListaDocumentoAssinadoErro().add(documentoAssinadoVO);
					}
				}
			}
			if (Uteis.isAtributoPreenchido(getListaDocumentoAssinadoErro())) {
				setOncompleteModal("RichFaces.$('panelListaErroDocumentoAssinado').show();");
			}
			consultarDocumentosPendentes();
			consultarDocumentosRejeitados();
			consultarDocumentosAssinados();
			setMensagemID("msg_dados_validados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			getProgressBarTechCert().setForcarEncerramento(Boolean.TRUE);
		}
	}
	
	public void realizarAssinaturaDocumentosPendentePorProvedorSeiTodosDocumentosSelecionados() {
		try {
			getListaDocumentoAssinadoErro().clear();
			setOncompleteModal(Constantes.EMPTY);
			List<DocumentoAssinadoVO> lista = ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().filter(documentoAssinado -> documentoAssinado.getSelecionado() && !documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().isXmlMec() && documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorSei()).collect(Collectors.toList());
			double permitsPerSecond = 1_000.0 / Uteis.delayMillisTechCert;
			RateLimiter limiter = RateLimiter.create(permitsPerSecond);
			if (Uteis.isAtributoPreenchido(lista)) {
				for (DocumentoAssinadoVO documentoAssinadoVO : lista) {
					limiter.acquire();
					String mensagemDocumentoAssinado = "Documento assinado: " + documentoAssinadoVO.getCodigo() + ", tipo origem: " + documentoAssinadoVO.getTipoOrigemDocumentoAssinadoEnum().getDescricao();
					getProgressBarVO().setProgresso(getProgressBarVO().getProgresso() + 1);
					getProgressBarVO().setStatus("(" + getProgressBarVO().getProgresso() + " de " + getProgressBarVO().getMaxValue() + ") " + mensagemDocumentoAssinado);
					try {
						documentoAssinadoVO.setConsistirException(new ConsistirException());
						realizarRegistroAssinaturaDocumento(documentoAssinadoVO, getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO().getUsuarioVO(), Boolean.TRUE);
					} catch (ConsistirException ce) {
						if (Uteis.isAtributoPreenchido(ce.getListaMensagemErro())) {
							documentoAssinadoVO.getConsistirException().getListaMensagemErro().addAll(ce.getListaMensagemErro());
						} else {
							documentoAssinadoVO.getConsistirException().getListaMensagemErro().add(Objects.nonNull(ce.getMessage()) ? ce.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
						}
						getListaDocumentoAssinadoErro().add(documentoAssinadoVO);
					} catch (Exception e) {
						documentoAssinadoVO.getConsistirException().getListaMensagemErro().add(Objects.nonNull(e.getMessage()) ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
						getListaDocumentoAssinadoErro().add(documentoAssinadoVO);
					}
				}
			}
			if (Uteis.isAtributoPreenchido(getListaDocumentoAssinadoErro())) {
				setOncompleteModal("RichFaces.$('panelListaErroDocumentoAssinado').show();");
			}
			consultarDocumentosPendentes();
			consultarDocumentosRejeitados();
			consultarDocumentosAssinados();
			setMensagemID("msg_dados_validados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			getProgressBarVO().setForcarEncerramento(Boolean.TRUE);
		}
	}
	
	public void inicializarExclusaoDocumentoPendentesEmLote() {
		try {	
			setMotivo("");
			setOperacaoDeVinculoEstagioEnum(OperacaoDeVinculoEstagioEnum.INDEFERIR);
			setListaDocumentoAssinadosParaExclusao( ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().filter(p-> p.getSelecionado()).collect(Collectors.toList()));
			Uteis.checkState(getListaDocumentoAssinadosParaExclusao().size() == 0, "Não foi selecionado nenhum Documento Assinado para ser processado");
			setOncompleteModal("RichFaces.$('panelExclusaoDocumentoAssinadoVinculo').show();");
			setExisteVinculoDocumentoAssinado((getListaDocumentoAssinadosParaExclusao().stream().anyMatch(p-> p.getSelecionado() && p.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio())));
			setExisteDiplomaAssinado(getListaDocumentoAssinadosParaExclusao().stream().filter(d -> d.getTipoOrigemDocumentoAssinadoEnum().isXmlMec()).map(DocumentoAssinadoVO::getListaDocumentoAssinadoPessoa).flatMap(Collection::stream).anyMatch(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().isAssinado()));
			inicializarMensagemVazia();
		} catch (Exception e) {
			setOncompleteModal("");
			setListaDocumentoAssinadosParaExclusao(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarExclusaoDocumentoAssinadosEmLote() {
		try {	
			setMotivo("");
			setOperacaoDeVinculoEstagioEnum(OperacaoDeVinculoEstagioEnum.INDEFERIR);
			setListaDocumentoAssinadosParaExclusao( ((List<DocumentoAssinadoVO>) getListaDocumentosAssinados().getListaConsulta()).stream().filter(p-> p.getSelecionado()).collect(Collectors.toList()));
			Uteis.checkState(getListaDocumentoAssinadosParaExclusao().size() == 0, "Não foi selecionado nenhum Documento Assinado para ser processado");
			setOncompleteModal("RichFaces.$('panelExclusaoDocumentoAssinadoVinculo').show();");
			setExisteVinculoDocumentoAssinado((getListaDocumentoAssinadosParaExclusao().stream().anyMatch(p-> p.getSelecionado() && p.getTipoOrigemDocumentoAssinadoEnum().isTermoEstagioObrigatorio())));
			setExisteDiplomaAssinado(getListaDocumentoAssinadosParaExclusao().stream().filter(d -> d.getTipoOrigemDocumentoAssinadoEnum().isXmlMec()).map(DocumentoAssinadoVO::getListaDocumentoAssinadoPessoa).flatMap(Collection::stream).anyMatch(d -> d.getSituacaoDocumentoAssinadoPessoaEnum().isAssinado()));
			inicializarMensagemVazia();
		} catch (Exception e) {
			setOncompleteModal("");
			setListaDocumentoAssinadosParaExclusao(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarInicioProgressBarExclusaoDocumentoAssinado() {
		try {
			setOncompleteModal("RichFaces.$('panelExclusaoDocumentoAssinadoVinculo').hide();");
			setProgressBarVO(new ProgressBarVO());
			Uteis.checkState(isExisteVinculoDocumentoAssinado() && getOperacaoDeVinculoEstagioEnum().isIndeferir() && !Uteis.isAtributoPreenchido(getMotivo()), "O campo Motivo deve ser informado.");
			Integer qtdContasSelecionadas = getListaDocumentoAssinadosParaExclusao().size();
			Uteis.checkState(qtdContasSelecionadas == 0, "Não foi selecionado nenhum Documento Assinado para ser processado.");
			getProgressBarVO().resetar();
			getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().iniciar(0l, (qtdContasSelecionadas.intValue()), "Iniciando Processamento da(s) operações.", true, this, "executarExclusaoTodosDocumentoAssinadoSelecionados");
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());	
		} 
	}
	
	public void executarExclusaoTodosDocumentoAssinadoSelecionados() {
		try {
			ConsistirException consistirException = new ConsistirException();
			ConfiguracaoEstagioObrigatorioVO configEstagio = getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadrao(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, progressBarVO.getUsuarioVO());
			for (DocumentoAssinadoVO obj : getListaDocumentoAssinadosParaExclusao()) {
				try {
					getProgressBarVO().setStatus(getProgressBarVO().getPreencherStatusProgressBarVO(getProgressBarVO(), "Documento", obj.getCodigo().toString()));
					getFacadeFactory().getDocumentoAssinadoFacade().executarExclusaoTodosDocumentoAssinadoSelecionados(obj, getOperacaoDeVinculoEstagioEnum(), getMotivo(), getProgressBarVO().getUsuarioVO(), configEstagio);
				} catch (Exception e) {
					consistirException.adicionarListaMensagemErro("Log Documento Assinado de código:"+obj.getCodigo()+" descrição -"+ e.getMessage());
				} finally {
					getProgressBarVO().incrementar();
				}
			}
			consultarDocumentosPendentes();
			consultarDocumentosAssinados();
			consultarDocumentosRejeitados();
			if (consistirException.existeErroListaMensagemErro()) {
				setMensagemDetalhada("msg_erro", Uteis.ERRO);
				setListaMensagemErro(consistirException.getListaMensagemErro());
				setOncompleteModal("RichFaces.$('panelOperacoesDocumentoAssinadoEmLoteLogs').show();");
			} else {
//				setMensagemID(MSG_TELA.msg_dados_excluidos.name());
				setMensagemID("msg_Documento_Assinado_Excluido", Uteis.SUCESSO, Boolean.TRUE);
				setOncompleteModal("RichFaces.$('panelExclusaoDocumentoAssinadoVinculo').hide();");
			}
		} catch (Exception e) {
			setOncompleteModal("RichFaces.$('panelOperacoesDocumentoAssinadoEmLoteLogs').show();");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public Boolean getExisteDocumentoPendenteSelecionado() {		
		return	((List<DocumentoAssinadoVO>)  getListaDocumentosPendentes().getListaConsulta()).stream().anyMatch( p-> p.getSelecionado());		
	}
	
	public Boolean getExisteDocumentoAssinadosSelecionado() {		
		return	((List<DocumentoAssinadoVO>)  getListaDocumentosAssinados().getListaConsulta()).stream().anyMatch( p-> p.getSelecionado());		
	}

	public List<MatriculaVO> autocompleteMatricula(Object suggest) {
		try {
			return getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaAutoComplete((String) suggest, getMatriculaVO().getUnidadeEnsino().getCodigo(), 10, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()) ;
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<MatriculaVO>();
		}
	}
	
	public String getAutocompleteValorMatricula() {
		if (autocompleteValorMatricula == null) {
			autocompleteValorMatricula = "";
		}
		return autocompleteValorMatricula;
	}
	
	public void setAutocompleteValorMatricula(String autocompleteValorMatricula) {
		this.autocompleteValorMatricula = autocompleteValorMatricula;
	}
	
	public List<DocumentoAssinadoVO> getListaDocumentoAssinadosParaExclusao() {
		if(listaDocumentoAssinadosParaExclusao == null) {
			listaDocumentoAssinadosParaExclusao =new ArrayList<>();
		}
		return listaDocumentoAssinadosParaExclusao;
	}

	public void setListaDocumentoAssinadosParaExclusao(List<DocumentoAssinadoVO> listaDocumentoAssinadosParaExclusao) {
		this.listaDocumentoAssinadosParaExclusao = listaDocumentoAssinadosParaExclusao;
	}

	public OperacaoDeVinculoEstagioEnum getOperacaoDeVinculoEstagioEnum() {
		if(operacaoDeVinculoEstagioEnum == null) {
			operacaoDeVinculoEstagioEnum = OperacaoDeVinculoEstagioEnum.INDEFERIR;
		}
		return operacaoDeVinculoEstagioEnum;
	}

	public void setOperacaoDeVinculoEstagioEnum(OperacaoDeVinculoEstagioEnum operacaoDeVinculoEstagioEnum) {
		this.operacaoDeVinculoEstagioEnum = operacaoDeVinculoEstagioEnum;
	}

	public String getMotivo() {
		if(motivo == null) {
			motivo ="";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	public boolean isExisteVinculoDocumentoAssinado() {
		return existeVinculoDocumentoAssinado;
	}

	public void setExisteVinculoDocumentoAssinado(boolean existeVinculoDocumentoAssinado) {
		this.existeVinculoDocumentoAssinado = existeVinculoDocumentoAssinado;
	}

	public void blurListenterZeraConsultaMatricula() {
		if(!getMatriculaVO().getMatricula().contains(getMatriculaVO().getMatricula()) ||  !getMatriculaVO().getMatricula().contains(")") || !getMatriculaVO().getMatricula().contains("(")) {
			setCursoVO(new CursoVO());
		}
	}
	
	private String getValorAutoComplete(String valor) {
		if (valor != null) {
			return valor;
		}
		return "";
	}
	
	public void selecionarMatriculaPorMatricula() {
		consultarMatriculaPorMatricula(getValorAutoComplete(getMatriculaVO().getMatricula()));
	}

	public void consultarMatriculaPorMatricula(String valor) {
		try {
			List<MatriculaVO> matriculas = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(valor, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setMatriculaVO(matriculas.get(0));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getCaminhoPreview() {
		if (caminhoPreview == null) {
			caminhoPreview = "";
		}
		return caminhoPreview;
	}
	
	public void setCaminhoPreview(String caminhoPreview) {
		this.caminhoPreview = caminhoPreview;
	}
	
	public void realizarVisualizacaoPdf() {
		try {
			DocumentoAssinadoVO obj = (DocumentoAssinadoVO) getRequestMap().get("documentoAssinadoItem");
			setCaminhoPreview("");
			if (obj.getProvedorDeAssinaturaEnum().isProvedorSei()) {
				if(obj.getArquivo().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
					if (obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) || obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL)) {
						ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
						ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
						String nomeArquivoUsar = obj.getArquivoVisual().getDescricao().contains(".") ? obj.getArquivoVisual().getDescricao() : obj.getArquivoVisual().getDescricao() + (obj.getArquivoVisual().getNome().substring(obj.getArquivoVisual().getNome().lastIndexOf("."), obj.getArquivoVisual().getNome().length()));
						if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(obj.getArquivoVisual().recuperarNomeArquivoServidorExterno(obj.getArquivoVisual().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))){
							setCaminhoPreview(servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(obj.getArquivoVisual().recuperarNomeArquivoServidorExterno(obj.getArquivoVisual().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));					
						}else {
							nomeArquivoUsar =  obj.getArquivoVisual().getDescricao();
							if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(obj.getArquivoVisual().recuperarNomeArquivoServidorExterno(obj.getArquivoVisual().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))){
								setCaminhoPreview(servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(obj.getArquivoVisual().recuperarNomeArquivoServidorExterno(obj.getArquivoVisual().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));
							}else {
								setMensagemDetalhada(MSG_TELA.msg_erro.name(), "Não foi encontrado no repositório da AMAZON o aquivo no caminho "+obj.getArquivoVisual().recuperarNomeArquivoServidorExterno(obj.getArquivoVisual().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)+".");							
							}
						}
						realizarDownloadArquivo(obj.getArquivoVisual());
					} else {
						ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
						ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
						String nomeArquivoUsar = obj.getArquivo().getDescricao().contains(".") ? obj.getArquivo().getDescricao() : obj.getArquivo().getDescricao() + (obj.getArquivo().getNome().substring(obj.getArquivo().getNome().lastIndexOf("."), obj.getArquivo().getNome().length()));
						if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(obj.getArquivo().recuperarNomeArquivoServidorExterno(obj.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))){
							setCaminhoPreview(servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(obj.getArquivo().recuperarNomeArquivoServidorExterno(obj.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));					
						}else {
							nomeArquivoUsar =  obj.getArquivo().getDescricao();
							if(servidorArquivoOnlineS3RS.consultarSeObjetoExiste(obj.getArquivo().recuperarNomeArquivoServidorExterno(obj.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))){
								setCaminhoPreview(servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(obj.getArquivo().recuperarNomeArquivoServidorExterno(obj.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));
							}else {
								setMensagemDetalhada(MSG_TELA.msg_erro.name(), "Não foi encontrado no repositório da AMAZON o aquivo no caminho "+obj.getArquivo().recuperarNomeArquivoServidorExterno(obj.getArquivo().getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)+".");							
							}
						}
						realizarDownloadArquivo(obj.getArquivo());
					}
				}else {
					if (obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL) || obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL) || obj.getTipoOrigemDocumentoAssinadoEnum().equals(TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL)) {
						setCaminhoPreview(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + obj.getArquivoVisual().getPastaBaseArquivo() +"/" + obj.getArquivoVisual().getNome()+"?embedded=true");
					} else {
						setCaminhoPreview(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + obj.getArquivo().getPastaBaseArquivo() +"/" + obj.getArquivo().getNome()+"?embedded=true");
					}
				}
			} else if (obj.getProvedorDeAssinaturaEnum().isProvedorCertisign() || obj.getProvedorDeAssinaturaEnum().isProvedorImprensaOficial()) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarVisualizacaoArquivoProvedorCertisign(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getMatricula().getUnidadeEnsino().getCodigo()), false, true, false, getUsuarioLogado());
				setCaminhoPreview(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + obj.getArquivo().getNome());
			}
			else if (obj.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarVisualizacaoArquivoProvedorTechCert(obj, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(obj.getMatricula().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				setCaminhoPreview(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao() + "/relatorio/" + obj.getArquivo().getNome());
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public Boolean validarPeriodoApresentarNotificacaoExpiracaoAssinaturaAtaColacaoGrauAssinado(DocumentoAssinadoVO documentoAssinado) {
		if (Uteis.isAtributoPreenchido(documentoAssinado.getProgramacaoFormaturaVO())) {
			return (!getFacadeFactory().getProgramacaoFormaturaFacade().validarDataLimitePodeAssinarAta(documentoAssinado.getProgramacaoFormaturaVO()) && documentoAssinado.getListaDocumentoAssinadoPessoa().stream().filter(d->d.getSituacaoDocumentoAssinadoPessoaEnum().equals(SituacaoDocumentoAssinadoPessoaEnum.PENDENTE)).findAny().isPresent());
		}
		return false;
	}
	
	public ProgressBarVO getProgressBarVO() {
		if (progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}
	
	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}
	
	public ZipFile getArquivoZipado() {
		return arquivoZipado;
	}
	
	public void setArquivoZipado(ZipFile arquivoZipado) {
		this.arquivoZipado = arquivoZipado;
	}
	
	public void uploadDiplomaLote(FileUploadEvent event) {
		try {
			File file = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + event.getUploadedFile().getName());
			if (file.exists()) {
				file.delete();
			}
			byte[] buffer = null;
			buffer = new byte[event.getUploadedFile().getInputStream().available()];
			event.getUploadedFile().getInputStream().read(buffer);
			Files.write(buffer, file);
			setArquivoZipado(new ZipFile(file.getPath()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void executarInicioProgressBarImportacaoXmlLote() {
		try {
			limparListasMensagens();
			if (getArquivoZipado() != null) {
				setProgressBarVO(new ProgressBarVO());
				getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
				getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWeb());
				getProgressBarVO().resetar();
				getProgressBarVO().iniciar(0l, 100, "Processando arquivo zipado", true, this, "realizarImportacaoXmlDiplomaLote");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarImportacaoXmlDiplomaLote() {
		try {
			Map<List<String>, List<String>> map = getFacadeFactory().getDocumentoAssinadoFacade().realizarImportacaoXmlDiplomaRegistradoraLote(getArquivoZipado(), getProgressBarVO());
			if (!map.isEmpty()) {
				for (Entry<List<String>, List<String>> entry : map.entrySet()) {
					if (entry != null) {
						getListaMensagemImportaXmlDiploma().addAll(entry.getKey());
						getListaMensagemErroImportaXmlDiploma().addAll(entry.getValue());
					}
					break;
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setArquivoZipado(null);
			getProgressBarVO().setForcarEncerramento(true);
		}
	}
	
	public List<String> getListaMensagemImportaXmlDiploma() {
		if (listaMensagemImportaXmlDiploma == null) {
			listaMensagemImportaXmlDiploma = new ArrayList<>(0);
		}
		return listaMensagemImportaXmlDiploma;
	}
	
	public void setListaMensagemImportaXmlDiploma(List<String> listaMensagemImportaXmlDiploma) {
		this.listaMensagemImportaXmlDiploma = listaMensagemImportaXmlDiploma;
	}
	
	public List<String> getListaMensagemErroImportaXmlDiploma() {
		if (listaMensagemErroImportaXmlDiploma == null) {
			listaMensagemErroImportaXmlDiploma = new ArrayList<>(0);
		}
		return listaMensagemErroImportaXmlDiploma;
	}
	
	public void setListaMensagemErroImportaXmlDiploma(List<String> listaMensagemErroImportaXmlDiploma) {
		this.listaMensagemErroImportaXmlDiploma = listaMensagemErroImportaXmlDiploma;
	}
	
	public void limparListasMensagens() {
		setListaMensagemImportaXmlDiploma(new ArrayList<>(0));
		setListaMensagemErroImportaXmlDiploma(new ArrayList<>(0));
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoUploadXMlDiploma() {
		if (documentoAssinadoUploadXMlDiploma == null) {
			documentoAssinadoUploadXMlDiploma = new DocumentoAssinadoVO();
		}
		return documentoAssinadoUploadXMlDiploma;
	}

	public void setDocumentoAssinadoUploadXMlDiploma(DocumentoAssinadoVO documentoAssinadoUploadXMlDiploma) {
		this.documentoAssinadoUploadXMlDiploma = documentoAssinadoUploadXMlDiploma;
	}
	
	public DocumentoAssinadoVO getDocumentoAssinadoRegrarRepresentacaoVisual() {
		if (documentoAssinadoRegrarRepresentacaoVisual == null) {
			documentoAssinadoRegrarRepresentacaoVisual = new DocumentoAssinadoVO();
		}
		return documentoAssinadoRegrarRepresentacaoVisual;
	}

	public void setDocumentoAssinadoRegrarRepresentacaoVisual(DocumentoAssinadoVO documentoAssinadoRegrarRepresentacaoVisual) {
		this.documentoAssinadoRegrarRepresentacaoVisual = documentoAssinadoRegrarRepresentacaoVisual;
	}
	
	public List<String> getListaErroXml() {
		if (listaErroXml == null) {
			listaErroXml = new ArrayList<>(0);
		}
		return listaErroXml;
	}
	
	public void setListaErroXml(List<String> listaErroXml) {
		this.listaErroXml = listaErroXml;
	}
	
	public void selecionarDocumentoAssinadoUploadXML(DocumentoAssinadoVO documentoAssinadoVO) {
		setDocumentoAssinadoUploadXMlDiploma(documentoAssinadoVO);
	}
	
	public void selecionarDocumentoAssinadoRegerarPDF(DocumentoAssinadoVO documentoAssinadoVO) {
		setDocumentoAssinadoRegrarRepresentacaoVisual(documentoAssinadoVO);
	}
	
	public void validarConformidadeXML(DocumentoAssinadoVO documentoAssinadoVO) {
		setListaErroXml(new ArrayList<>());
		setOncompleteModal(Constantes.EMPTY);
		if (Uteis.isAtributoPreenchido(documentoAssinadoVO) && documentoAssinadoVO.getVersaoDiploma() != null) {
			try {
				List<String> listaErro = new ArrayList<>(0);
				if (documentoAssinadoVO.getVersaoDiploma().equals(VersaoDiplomaDigitalEnum.VERSAO_1_05)) {
					listaErro = getFacadeFactory().getExpedicaoDiplomaDigital_1_05_interfaceFacade().validarConformidadeXML(documentoAssinadoVO, getConfiguracaoGeralPadraoSistema());
				}
				if (Uteis.isAtributoPreenchido(listaErro)) {
					setListaErroXml(listaErro);
					setOncompleteModal("RichFaces.$('panelConformidadeXml').show()");
				} else {
					setMensagemID("A Estrutura do Diploma Digital está válida.", Uteis.SUCESSO, Boolean.TRUE);
				}
			} catch (Exception e) {
				setOncompleteModal(Constantes.EMPTY);
				setListaErroXml(new ArrayList<>());
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void uploadXmlDiploma(FileUploadEvent event) {
		try {
			getFacadeFactory().getDocumentoAssinadoFacade().realizarLeituraDiplomaDigital(getDocumentoAssinadoUploadXMlDiploma(), getConfiguracaoGeralPadraoSistema(), event.getUploadedFile().getInputStream(), getUsuarioLogado());
			consultarDocumentos();
			if (!getDocumentoAssinadoUploadXMlDiploma().getListaErroImportaAssinatura().isEmpty()) {
				getListaMensagemErro().addAll(getDocumentoAssinadoUploadXMlDiploma().getListaErroImportaAssinatura());
			} else {
				setMensagemID("msg_relatorio_ok");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarGeracaoRepresentacaoVisualDiplomaDigital() {
		try {
			Integer codigoExpedicaoDiploma = getFacadeFactory().getExpedicaoDiplomaFacade().consultarExpedicaoDiplomaUtilizarHistoricoDigital(getDocumentoAssinadoRegrarRepresentacaoVisual().getMatricula().getMatricula());
			if (!Uteis.isAtributoPreenchido(codigoExpedicaoDiploma)) {
				throw new Exception("Não foi localizado expedição de diploma para este aluno. Deve ser realizado a criação da expedição para realizar a regeração da representação visual.");
			}
			ExpedicaoDiplomaVO expedicaoDiplomaVO = getFacadeFactory().getExpedicaoDiplomaFacade().carregarDadosCompletoExpedicaoDiploma(codigoExpedicaoDiploma, getUsuarioLogado());
			getFacadeFactory().getExpedicaoDiplomaFacade().realizarGeracaoRepresentacaoVisualDiplomaDigital(expedicaoDiplomaVO);
			if (Uteis.isAtributoPreenchido(expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual().getCodigo())) {
				getDocumentoAssinadoRegrarRepresentacaoVisual().setArquivoVisual(expedicaoDiplomaVO.getDocumentoAssinadoVO().getArquivoVisual());
				getFacadeFactory().getDocumentoAssinadoFacade().alterarArquivoVisualDocumentoAssinado(getDocumentoAssinadoRegrarRepresentacaoVisual(),false, getUsuario());
			}
//			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());			
		} finally {
			setDocumentoAssinadoRegrarRepresentacaoVisual(null);
		}
	}

	public String getOnCompleteRejeicaoDocumento() {
		if (onCompleteRejeicaoDocumento == null) {
			onCompleteRejeicaoDocumento = "";
		}
		return onCompleteRejeicaoDocumento;
	}

	public void setOnCompleteRejeicaoDocumento(String onCompleteRejeicaoDocumento) {
		this.onCompleteRejeicaoDocumento = onCompleteRejeicaoDocumento;
	}
	
	public void limparDadosMemoria() {
		setDocumentoAssinadoRegrarRepresentacaoVisual(null);
	}
	
	public String getUrlDownloadSeiSignature() {
		if (urlDownloadSeiSignature == null) {
			urlDownloadSeiSignature = "location.href='https://cloud.otimize-ti.com.br/public/SeiSignature_" + getConfiguracaoGeralPadraoSistema().getVersaoSeiSignature() + ".jar'";
		}
		return urlDownloadSeiSignature;
	}

	public String getMotivoRejeicaoDocumentoAssinadoProvedorAssinatura() {
		if (motivoRejeicaoDocumentoAssinadoProvedorAssinatura == null) {
			motivoRejeicaoDocumentoAssinadoProvedorAssinatura = "";
		}
		return motivoRejeicaoDocumentoAssinadoProvedorAssinatura;
	}

	public void setMotivoRejeicaoDocumentoAssinadoProvedorAssinatura(String motivoRejeicaoDocumentoAssinadoProvedorAssinatura) {
		this.motivoRejeicaoDocumentoAssinadoProvedorAssinatura = motivoRejeicaoDocumentoAssinadoProvedorAssinatura;
	}
	
	public void rejeitarDocumentoAssinadoProvedorAssinatura() {
		try {
			getFacadeFactory().getDocumentoAssinadoFacade().rejeitarDocumentoAssinadoProvedorAssinatura(getDocumentoAssinadoExcluir(), getMotivoRejeicaoDocumentoAssinadoProvedorAssinatura(), getUsuarioLogadoClone());
			consultarDocumentosAssinados();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setDocumentoAssinadoExcluir(null);
			setOnCompleteRejeicaoDocumento("");
		}
	}
	
	public Boolean getExisteDiplomaAssinado() {
		if (existeDiplomaAssinado == null) {
			existeDiplomaAssinado = Boolean.FALSE;
		}
		return existeDiplomaAssinado;
	}
	
	public void setExisteDiplomaAssinado(Boolean existeDiplomaAssinado) {
		this.existeDiplomaAssinado = existeDiplomaAssinado;
	}
	
	public ProgressBarVO getProgressBarCertisign() {
		if (progressBarCertisign == null) {
			progressBarCertisign = new ProgressBarVO();
		}
		return progressBarCertisign;
	}
	
	public void setProgressBarCertisign(ProgressBarVO progressBarCertisign) {
		this.progressBarCertisign = progressBarCertisign;
	}
	
	public List<DocumentoAssinadoVO> getListaDocumentoAssinadoErro() {
		if (listaDocumentoAssinadoErro == null) {
			listaDocumentoAssinadoErro = new ArrayList<>(0);
		}
		return listaDocumentoAssinadoErro;
	}
	
	public void setListaDocumentoAssinadoErro(List<DocumentoAssinadoVO> listaDocumentoAssinadoErro) {
		this.listaDocumentoAssinadoErro = listaDocumentoAssinadoErro;
	}
	
	@SuppressWarnings("unchecked")
	public void executarInicioProgressBarVerificacaoDocumentosPendenteAssinaturaPorProvedorCertiSign() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			setListaDocumentoAssinadoErro(new ArrayList<>(0));
			List<DocumentoAssinadoVO> listaConvertida = (List<DocumentoAssinadoVO>)  getListaDocumentosPendentes().getListaConsulta();
			Uteis.checkState(!(Uteis.isAtributoPreenchido(listaConvertida) && listaConvertida.stream().anyMatch(documentoAssinado -> documentoAssinado.getSelecionado() && documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorCertisign())), "Nenhum documento apto para a validação foi selecionado");
			setProgressBarCertisign(new ProgressBarVO());			
			getProgressBarCertisign().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarCertisign().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarCertisign().setCaminhoWebRelatorio(getCaminhoPastaWeb());			
			getProgressBarCertisign().resetar();
			getProgressBarCertisign().iniciar(0l, listaConvertida.stream().filter(documentoAssinado -> documentoAssinado.getSelecionado() && documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorCertisign()).collect(Collectors.toList()).size(), "Iniciando Verificação Documentos", true, this, "realizarVerificacaoDocumentosPendenteAssinaturaPorProvedorCertiSignTodosDocumentosSelecionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	public void executarInicioProgressBarVerificacaoDocumentosPendenteAssinaturaPorProvedorTechCert() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			setListaDocumentoAssinadoErro(new ArrayList<>(0));
			List<DocumentoAssinadoVO> listaConvertida = (List<DocumentoAssinadoVO>)  getListaDocumentosPendentes().getListaConsulta();
			Uteis.checkState(!(Uteis.isAtributoPreenchido(listaConvertida) && listaConvertida.stream().anyMatch(documentoAssinado -> documentoAssinado.getSelecionado() && documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorTechCert())), "Nenhum documento apto para a validação foi selecionado");
			setProgressBarTechCert(new ProgressBarVO());
			getProgressBarTechCert().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarTechCert().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarTechCert().setCaminhoWebRelatorio(getCaminhoPastaWeb());
			getProgressBarTechCert().resetar();
			getProgressBarTechCert().iniciar(0l, listaConvertida.stream().filter(documentoAssinado -> documentoAssinado.getSelecionado() && documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorTechCert()).collect(Collectors.toList()).size(), "Iniciando Verificação Documentos", true, this, "realizarVerificacaoDocumentosPendenteAssinaturaPorProvedorTechCertTodosDocumentosSelecionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void executarInicioProgressBarVerificacaoDocumentosPendenteAssinaturaPorProvedorSei() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			setListaDocumentoAssinadoErro(new ArrayList<>(0));
			List<DocumentoAssinadoVO> listaConvertida = (List<DocumentoAssinadoVO>)  getListaDocumentosPendentes().getListaConsulta();
			Uteis.checkState(!(Uteis.isAtributoPreenchido(listaConvertida) && listaConvertida.stream().anyMatch(documentoAssinado -> documentoAssinado.getSelecionado() && !documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().isXmlMec() && documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorSei())), "Nenhum documento apto para a validação foi selecionado");
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWeb());
			getProgressBarVO().resetar();
			getProgressBarVO().iniciar(0l, listaConvertida.stream().filter(documentoAssinado -> documentoAssinado.getSelecionado() && !documentoAssinado.getTipoOrigemDocumentoAssinadoEnum().isXmlMec() && documentoAssinado.getProvedorDeAssinaturaEnum().isProvedorSei()).collect(Collectors.toList()).size(), "Iniciando Verificação Documentos", true, this, "realizarAssinaturaDocumentosPendentePorProvedorSeiTodosDocumentosSelecionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	
	public boolean isTipoDocumentoAssinadoXmlMec() {
		return Uteis.isAtributoPreenchido(getTipoOrigemDocumentoAssinadoEnum()) && getTipoOrigemDocumentoAssinadoEnum().isXmlMec();
	}
	
	public boolean isAssinarXmlMecDigitalmente() {
		return (!getRealizarAssinaturaXmlLote() && Uteis.isAtributoPreenchido(getDocumentoAssinadoVO()) && getDocumentoAssinadoVO().getTipoOrigemDocumentoAssinadoEnum().isXmlMec()) || (getRealizarAssinaturaXmlLote() && getTipoOrigemDocumentoAssinadoEnum().isXmlMec());
	}
	
	public Boolean getExtensaoLacunaInstalada() {
		if (extensaoLacunaInstalada == null) {
			extensaoLacunaInstalada = Boolean.FALSE;
		}
		return extensaoLacunaInstalada;
	}
	
	public void setExtensaoLacunaInstalada(Boolean extensaoLacunaInstalada) {
		this.extensaoLacunaInstalada = extensaoLacunaInstalada;
	}
	
	public String getNavegadorLogado() {
		if (navegadorLogado == null) {
			navegadorLogado = Constantes.EMPTY;
		}
		return navegadorLogado;
	}
	
	public void setNavegadorLogado(String navegadorLogado) {
		this.navegadorLogado = navegadorLogado;
	}
	
	public List<SelectItem> getListaSelectItemCertificadoInstalado() {
		if (listaSelectItemCertificadoInstalado == null) {
			listaSelectItemCertificadoInstalado = new ArrayList<>(0);
		}
		return listaSelectItemCertificadoInstalado;
	}
	
	public void setListaSelectItemCertificadoInstalado(List<SelectItem> listaSelectItemCertificadoInstalado) {
		this.listaSelectItemCertificadoInstalado = listaSelectItemCertificadoInstalado;
	}
	
	public List<CertificadoLacunaVO> getListaCertificadoInstalado() {
		if (listaCertificadoInstalado == null) {
			listaCertificadoInstalado = new ArrayList<>(0);
		}
		return listaCertificadoInstalado;
	}
	
	public void setListaCertificadoInstalado(List<CertificadoLacunaVO> listaCertificadoInstalado) {
		this.listaCertificadoInstalado = listaCertificadoInstalado;
	}
	
	public String getJsonListaCertificadoInstalado() {
		if (jsonListaCertificadoInstalado == null) {
			jsonListaCertificadoInstalado = Constantes.EMPTY;
		}
		return jsonListaCertificadoInstalado;
	}
	
	public void setJsonListaCertificadoInstalado(String jsonListaCertificadoInstalado) {
		this.jsonListaCertificadoInstalado = jsonListaCertificadoInstalado;
	}
	
	public String getJsonListaDocumentoAssinadoAssinar() {
		if (jsonListaDocumentoAssinadoAssinar == null) {
			jsonListaDocumentoAssinadoAssinar = Constantes.EMPTY;
		}
		return jsonListaDocumentoAssinadoAssinar;
	}
	
	public void setJsonListaDocumentoAssinadoAssinar(String jsonListaDocumentoAssinadoAssinar) {
		this.jsonListaDocumentoAssinadoAssinar = jsonListaDocumentoAssinadoAssinar;
	}
	
	public String getThumbprintCertificado() {
		if (thumbprintCertificado == null) {
			thumbprintCertificado = Constantes.EMPTY;
		}
		return thumbprintCertificado;
	}
	
	public void setThumbprintCertificado(String thumbprintCertificado) {
		this.thumbprintCertificado = thumbprintCertificado;
	}
	
	public Integer getOrdemAssinaturaXml() {
		if (ordemAssinaturaXml == null) {
			ordemAssinaturaXml = 1;
		}
		return ordemAssinaturaXml;
	}
	
	public void setOrdemAssinaturaXml(Integer ordemAssinaturaXml) {
		this.ordemAssinaturaXml = ordemAssinaturaXml;
	}
	
	public Integer getCodigoPessoaLogadaAssinarXml() {
		if (codigoPessoaLogadaAssinarXml == null) {
			codigoPessoaLogadaAssinarXml = 0;
		}
		return codigoPessoaLogadaAssinarXml;
	}
	
	public void setCodigoPessoaLogadaAssinarXml(Integer codigoPessoaLogadaAssinarXml) {
		this.codigoPessoaLogadaAssinarXml = codigoPessoaLogadaAssinarXml;
	}
	
	public List<SelectItem> getListaSelectItemTipoAssinanteDiplomaDigital() {
		if (listaSelectItemTipoAssinanteDiplomaDigital == null) {
			listaSelectItemTipoAssinanteDiplomaDigital = new ArrayList<SelectItem>(Arrays.asList(
					new SelectItem(1, "IES Representante 1 (E-CPF)"),
					new SelectItem(2, "IES Representante 2 (E-CPF)"),
					new SelectItem(3, "IES Emissora (E-CNPJ)"),
					new SelectItem(4, "Registradora (E-CPF)"),
					new SelectItem(5, "Registradora (E-CNPJ)")));
		}
		return listaSelectItemTipoAssinanteDiplomaDigital;
	}
	
	public void setListaSelectItemTipoAssinanteDiplomaDigital(List<SelectItem> listaSelectItemTipoAssinanteDiplomaDigital) {
		this.listaSelectItemTipoAssinanteDiplomaDigital = listaSelectItemTipoAssinanteDiplomaDigital;
	}
	
	public List<SelectItem> getListaSelectItemTipoAssinanteHistoricoDigital() {
		if (listaSelectItemTipoAssinanteHistoricoDigital == null) {
			listaSelectItemTipoAssinanteHistoricoDigital = new ArrayList<SelectItem>(Arrays.asList(
					new SelectItem(1, "IES Representante (E-CPF)"),
					new SelectItem(3, "IES Emissora Final (E-CNPJ)")));
		}
		return listaSelectItemTipoAssinanteHistoricoDigital;
	}
	
	public void setListaSelectItemTipoAssinanteHistoricoDigital(List<SelectItem> listaSelectItemTipoAssinanteHistoricoDigital) {
		this.listaSelectItemTipoAssinanteHistoricoDigital = listaSelectItemTipoAssinanteHistoricoDigital;
	}
	
	public List<SelectItem> getListaSelectItemTipoAssinanteDocumentacaoAcademica() {
		if (listaSelectItemTipoAssinanteDocumentacaoAcademica == null) {
			listaSelectItemTipoAssinanteDocumentacaoAcademica = new ArrayList<SelectItem>(Arrays.asList(
					new SelectItem(1, "IES Representante (E-CPF)"),
					new SelectItem(3, "IES Emissora (E-CNPJ)"),
					new SelectItem(5, "IES Emissora Final (E-CNPJ)")));
		}
		return listaSelectItemTipoAssinanteDocumentacaoAcademica;
	}
	
	public void setListaSelectItemTipoAssinanteDocumentacaoAcademica(List<SelectItem> listaSelectItemTipoAssinanteDocumentacaoAcademica) {
		this.listaSelectItemTipoAssinanteDocumentacaoAcademica = listaSelectItemTipoAssinanteDocumentacaoAcademica;
	}
	
	public List<SelectItem> getListaSelectItemTipoAssinanteCurriculoEscolar() {
		if (listaSelectItemTipoAssinanteCurriculoEscolar == null) {
			listaSelectItemTipoAssinanteCurriculoEscolar = new ArrayList<SelectItem>(Arrays.asList(
					new SelectItem(1, "IES Representante do Curso (E-CPF)"),
					new SelectItem(3, "IES Emissora Final (E-CNPJ)")));
		}
		return listaSelectItemTipoAssinanteCurriculoEscolar;
	}
	
	public void setListaSelectItemTipoAssinanteCurriculoEscolar(List<SelectItem> listaSelectItemTipoAssinanteCurriculoEscolar) {
		this.listaSelectItemTipoAssinanteCurriculoEscolar = listaSelectItemTipoAssinanteCurriculoEscolar;
	}
	
	public String getUrlAcessoExternoAplicacao() {
		if (urlAcessoExternoAplicacao == null) {
			urlAcessoExternoAplicacao = Constantes.EMPTY;
		}
		return urlAcessoExternoAplicacao;
	}
	
	public void setUrlAcessoExternoAplicacao(String urlAcessoExternoAplicacao) {
		this.urlAcessoExternoAplicacao = urlAcessoExternoAplicacao;
	}
	
	public Boolean getRealizarAssinaturaXmlLote() {
		if (realizarAssinaturaXmlLote == null) {
			realizarAssinaturaXmlLote = Boolean.FALSE;
		}
		return realizarAssinaturaXmlLote;
	}
	
	public void setRealizarAssinaturaXmlLote(Boolean realizarAssinaturaXmlLote) {
		this.realizarAssinaturaXmlLote = realizarAssinaturaXmlLote;
	}
	
	public void validarTipoOrigemDocumentoAssinadoEnum() {
		if (Uteis.isAtributoPreenchido(getTipoOrigemDocumentoAssinadoEnum()) && getTipoOrigemDocumentoAssinadoEnum().isXmlMec()) {
			setOrdemAssinaturaXml(null);
			consultarDocumentos();
		}
	}
	
	@SuppressWarnings({ "serial", "unchecked" })
	public void realizarMontagemListaCertificadosLacunaInstalados() {
		try {
			setListaSelectItemCertificadoInstalado(new ArrayList<>());
			if (Uteis.isAtributoPreenchido(getJsonListaCertificadoInstalado())) {
				Gson gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>()).create();
				Type type = new TypeToken<List<CertificadoLacunaVO>>() {
				}.getType();
				List<CertificadoLacunaVO> listaCertificadoInstalados = gson.fromJson(getJsonListaCertificadoInstalado(), type);
				if (Uteis.isAtributoPreenchido(listaCertificadoInstalados)) {
					if (getRealizarAssinaturaXmlLote()) {
						Predicate<CertificadoLacunaVO> filtroListaCertificado = c -> c.isApresentarCertificadoListagem(getUsuarioLogado(), Uteis.isAtributoPreenchido(getUnidadeEnsinoVO()) ? getUnidadeEnsinoVO() : ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().filter(DocumentoAssinadoVO::getSelecionado).map(DocumentoAssinadoVO::getUnidadeEnsinoVO).findAny().get(), getOrdemAssinaturaXml().equals(3) || getOrdemAssinaturaXml().equals(5), getOrdemAssinaturaXml());
						setListaCertificadoInstalado(listaCertificadoInstalados.stream().filter(filtroListaCertificado).collect(Collectors.toList()));
						listaCertificadoInstalados.stream().filter(filtroListaCertificado).map(c -> new SelectItem(c.getThumbprint(), c.getSubjectName() + " - " + c.getValidityEnd_apresentar())).forEach(getListaSelectItemCertificadoInstalado()::add);
					} else {
						Predicate<CertificadoLacunaVO> filtroListaCertificado = c -> c.isApresentarCertificadoListagem(getUsuarioLogado(), getDocumentoAssinadoVO().getUnidadeEnsinoVO(), getDocumentoAssinadoVO().getDocumentoAssinadoPessoaResposanvelAssinatura().getAssinarPorCNPJ(), getDocumentoAssinadoVO().getDocumentoAssinadoPessoaResposanvelAssinatura().getOrdemAssinatura());
						setListaCertificadoInstalado(listaCertificadoInstalados.stream().filter(filtroListaCertificado).collect(Collectors.toList()));
						listaCertificadoInstalados.stream().filter(filtroListaCertificado).map(c -> new SelectItem(c.getThumbprint(), c.getSubjectName() + " - " + c.getValidityEnd_apresentar())).forEach(getListaSelectItemCertificadoInstalado()::add);
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String getMensagemDownloadExtensaoLacuna() {
		return "<html> <head></head> <body> <h3 style=\"box-sizing: border-box; font-size: 1.75rem; font-family: &quot;Segoe UI&quot;, Arial, sans-serif; font-weight: 400; margin: 0px 0px 0.5rem; line-height: 1.2; color: rgba(0, 0, 0, 0.87); font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;\">Detectamos que a Extensão para Assinatura Digital não está instalada.</h3> <p style=\"box-sizing: border-box; margin-top: 0px; margin-bottom: 1rem; color: rgba(0, 0, 0, 0.87); font-family: Nunito, -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-size: 15px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;\">Segue abaixo um passo a passo para instalação da extensão</p> <p style=\"box-sizing: border-box; margin-top: 0px; margin-bottom: 1rem; color: rgba(0, 0, 0, 0.87); font-family: Nunito, -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-size: 15px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;\"><strong style=\"box-sizing: border-box; font-weight: bolder;\">1º Passo -</strong><span>&nbsp;</span>Clique no botão abaixo para acessar o site de instalação da Extensão no Chrome </p> <p><a href=\"https://get.webpkiplugin.com/\" target=\"_blank\" class=\"btn btn-primary btn-extension-install\" style=\"margin-bottom: 5px;\"> Instalar Extensão </a></p> <p><strong style=\"box-sizing: border-box; font-weight: bolder; color: rgba(0, 0, 0, 0.87); font-family: Nunito, -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-size: 15px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;\">2º Passo -</strong><span style=\"color: rgba(0, 0, 0, 0.87); font-family: Nunito, -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-size: 15px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial; display: inline !important; float: none;\"><span>&nbsp;</span>Você deve atualizar a página do SeiSignature clicando em<span>&nbsp;</span></span><strong style=\"box-sizing: border-box; font-weight: bolder; color: rgba(0, 0, 0, 0.87); font-family: Nunito, -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-size: 15px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial;\">(CTRL + F5)</strong><span style=\"color: rgba(0, 0, 0, 0.87); font-family: Nunito, -apple-system, BlinkMacSystemFont, &quot;Segoe UI&quot;, Roboto, &quot;Helvetica Neue&quot;, Arial, sans-serif, &quot;Apple Color Emoji&quot;, &quot;Segoe UI Emoji&quot;, &quot;Segoe UI Symbol&quot;, &quot;Noto Color Emoji&quot;; font-size: 15px; font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: left; text-indent: 0px; text-transform: none; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; white-space: normal; background-color: rgb(255, 255, 255); text-decoration-thickness: initial; text-decoration-style: initial; text-decoration-color: initial; display: inline !important; float: none;\">, depois basta apenas logar novamente e realizar a assinatura</span><br> </p> </body> </html>";
	}
	
	public boolean isApresentarOpcaoAssinarXml() {
		return isAssinarXmlMecDigitalmente() && getExtensaoLacunaInstalada() && Uteis.isAtributoPreenchido(getListaSelectItemCertificadoInstalado());
	}
	
	@SuppressWarnings("unchecked")
	public void validarDadosAssinaturaXmlLote() throws Exception {
		TipoOrigemDocumentoAssinadoEnum tipoOrigemDocumentoAssinado = getTipoOrigemDocumentoAssinadoEnum();
		List<DocumentoAssinadoVO> listaDocumentoAssinadoPendente = ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().filter(DocumentoAssinadoVO::getSelecionado).collect(Collectors.toList());
		Map<TipoOrigemDocumentoAssinadoEnum, List<DocumentoAssinadoVO>> tipoXml = listaDocumentoAssinadoPendente.stream().collect(Collectors.groupingBy(DocumentoAssinadoVO::getTipoOrigemDocumentoAssinadoEnum));
		Map<Integer, List<DocumentoAssinadoVO>> ordemAssinatura = listaDocumentoAssinadoPendente.stream().collect(Collectors.groupingBy(DocumentoAssinadoVO::getOrdemAssinatura));
		Map<Integer, List<UnidadeEnsinoVO>> unidadeEnsino = listaDocumentoAssinadoPendente.stream().map(documentoAssinado -> documentoAssinado.getUnidadeEnsinoVO()).collect(Collectors.groupingBy(UnidadeEnsinoVO::getCodigo));
		Uteis.checkState(!Uteis.isAtributoPreenchido(getTipoOrigemDocumentoAssinadoEnum()), "No campo TIPO DOCUMENTO deve ser informado qual tipo de xml assinar");
		Uteis.checkState(!Uteis.isAtributoPreenchido(getOrdemAssinaturaXml()), "O TIPO ASSINANTE XML deve ser informado");
		Uteis.checkState(!Uteis.isAtributoPreenchido(listaDocumentoAssinadoPendente) || listaDocumentoAssinadoPendente.stream().noneMatch(DocumentoAssinadoVO::getSelecionado), "Deve ser selecionado ao menos 1 XML para realizar assinatura em lote");
		Uteis.checkState(Objects.nonNull(tipoXml) && !tipoXml.keySet().isEmpty() && tipoXml.keySet().size() > 1, "Não é possível assinar múltiplos tipos de xmls ao mesmo tempo, favor selecionar apenas 1 tipo de XML para realizar a assinatura.");
		Uteis.checkState(Objects.nonNull(ordemAssinatura) && !ordemAssinatura.keySet().isEmpty() && ordemAssinatura.keySet().size() > 1, "Não é possível assinar múltiplos xmls de ordem de assinaturas diferentes ao mesmo tempo, favor selecionar apenas 1 ordem de assinatura para realizar a assinatura.");
		Uteis.checkState(Objects.nonNull(unidadeEnsino) && !unidadeEnsino.keySet().isEmpty() && unidadeEnsino.keySet().size() > 1, "Não é possível assinar múltiplos xmls com unidades de ensino de expedição de diploma diferentes ao mesmo tempo, favor selecionar para a assinatura dos xmls apenas lote com a mesma unidade de ensino de expedição.");
		Uteis.checkState(listaDocumentoAssinadoPendente.stream().anyMatch(d -> !d.getUsuarioPodeAssinarDocumento(getUsuarioLogado())), "Não é possível assinar documentos na qual não o responsável pela assinatura não seja você mesmo");
		Uteis.checkState(listaDocumentoAssinadoPendente.stream().anyMatch(d -> !d.getTipoOrigemDocumentoAssinadoEnum().equals(tipoOrigemDocumentoAssinado)), "Não é possivel assinar xml diferente do tipo documento selecionado, tipo documento: " + tipoOrigemDocumentoAssinado.getDescricao());
		Uteis.checkState(!Uteis.isAtributoPreenchido(getUsuarioLogado().getPessoa()), "O usuário logado deve ter uma pessoa vinculada para realizar a assinatura");
	}
	
	@SuppressWarnings("unchecked")
	public void validarDadosCertificadoSelecionado() throws Exception {
		Boolean assinarPorCnpj = (getOrdemAssinaturaXml().equals(3) || getOrdemAssinaturaXml().equals(5));
		List<DocumentoAssinadoVO> listaDocumentoAssinadoPendente = ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().filter(DocumentoAssinadoVO::getSelecionado).collect(Collectors.toList());
		Uteis.checkState(!Uteis.isAtributoPreenchido(getListaCertificadoInstalado()), "Nâo existe certificados aptos instalados em sua maquina");
		Uteis.checkState(!Uteis.isAtributoPreenchido(getThumbprintCertificado()), "O certificado A3 para realizar a assinatura deve ser informado");
		Optional<CertificadoLacunaVO> optional = getListaCertificadoInstalado().stream().filter(certificado -> Uteis.isAtributoPreenchido(certificado.getThumbprint()) && certificado.getThumbprint().equals(getThumbprintCertificado())).findAny();
		Uteis.checkState(!(Objects.nonNull(optional) && optional.isPresent()), "O certificado selecionado não está na listagem de certificados instalados em sua maquina");
		CertificadoLacunaVO certificado = optional.get();
		certificado.validarDados();
		if (assinarPorCnpj) {
			Uteis.checkState(!certificado.getPkiBrazil().getIsPessoaJuridica(), "O certificado selecionado não é do tipo E-CNPJ para realizar a assinatura do xml");
			Uteis.checkState(listaDocumentoAssinadoPendente.stream().anyMatch(d -> !d.getDocumentoAssinadoPessoaResposanvelAssinatura().getAssinarPorCNPJ()), "Não é possivel realizar assinatura de um documento do tipo E-CPF sendo que o TIPO ASSINANTE XML selecionado é do tipo E-CNPJ");
			Uteis.checkState(listaDocumentoAssinadoPendente.stream().anyMatch(d -> !d.isCnpjAssinanteValido(getOrdemAssinaturaXml(), certificado)), getOrdemAssinaturaXml().equals(3) ? "O CNPJ da unidade de ensino vinculada ao documento não é igual ao CNPJ do certificado selecionado" : "O CNPJ da unidade de ensino registradora vinculada ao documento não é igual ao CNPJ do certificado selecionado");
		} else {
			Uteis.checkState(!certificado.getPkiBrazil().getIsPessoaFisica(), "O certificado selecionado não é do tipo E-CPF para realizar a assinatura do xml");
			Uteis.checkState(listaDocumentoAssinadoPendente.stream().anyMatch(d -> d.getDocumentoAssinadoPessoaResposanvelAssinatura().getAssinarPorCNPJ()), "Não é possivel realizar assinatura de um documento do tipo E-CNPJ sendo que o TIPO ASSINANTE XML selecionado é do tipo E-CPF");
		}
	}
	
	public void realizarValidacaoLoteAssinaturaXml() {
		setOncompleteModal(Constantes.EMPTY);
		setRealizarAssinaturaXmlLote(Boolean.TRUE);
		try {
			validarDadosAssinaturaXmlLote();
			validarDadosIniciarConsultaCertificadosIntalados();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void executarInicioProgressBarAssinaturaXml() {
		setOncompleteModal(Constantes.EMPTY);
		try {
			getAplicacaoControle().setMapDocumentoAssinadoAssinarXml(null);
			if (!Uteis.isAtributoPreenchido(getAplicacaoControle().getMapDocumentoAssinadoAssinarXml()) && (getProgressBarAssinarXmlMec().getAtivado() || getProgressBarAssinarXmlMec().getPollAtivado())) {
				getAplicacaoControle().setDataUltimaAtualizacaoAssinaturaXml(null);
				getProgressBarAssinarXmlMec().setForcarEncerramento(Boolean.TRUE);
				getProgressBarAssinarXmlMec().resetar();
			} else if (getAplicacaoControle().isPeriodoAssinaturaXmlExcedido() && (getProgressBarAssinarXmlMec().getAtivado() || getProgressBarAssinarXmlMec().getPollAtivado())) {
				getAplicacaoControle().setDataUltimaAtualizacaoAssinaturaXml(null);
				getProgressBarAssinarXmlMec().setForcarEncerramento(Boolean.TRUE);
				getProgressBarAssinarXmlMec().resetar();
			}
			if (getProgressBarAssinarXmlMec().getAtivado() || getProgressBarAssinarXmlMec().getPollAtivado()) {
				throw new Exception("Já estão sendo realizadas assinaturas de xml em aplicação");
			}
			Uteis.checkState(!Uteis.isAtributoPreenchido(getDocumentoAssinadoVO()), "O Documento que será assinado deve ser selecionado");
			((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().forEach(d -> d.setSelecionado(Boolean.FALSE));
			((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().filter(d -> d.getCodigo().equals(getDocumentoAssinadoVO().getCodigo())).forEach(d -> d.setSelecionado(Boolean.TRUE));
			iniciarProgressBar();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void executarInicioProgressBarAssinaturaXmlLote() {
		setOncompleteModal(Constantes.EMPTY);
		try {
			if (!Uteis.isAtributoPreenchido(getAplicacaoControle().getMapDocumentoAssinadoAssinarXml()) && (getProgressBarAssinarXmlMec().getAtivado() || getProgressBarAssinarXmlMec().getPollAtivado())) {
				getAplicacaoControle().setDataUltimaAtualizacaoAssinaturaXml(null);
				getProgressBarAssinarXmlMec().setForcarEncerramento(Boolean.TRUE);
				getProgressBarAssinarXmlMec().resetar();
			} else if (getAplicacaoControle().isPeriodoAssinaturaXmlExcedido() && (getProgressBarAssinarXmlMec().getAtivado() || getProgressBarAssinarXmlMec().getPollAtivado())) {
				getAplicacaoControle().setDataUltimaAtualizacaoAssinaturaXml(null);
				getProgressBarAssinarXmlMec().setForcarEncerramento(Boolean.TRUE);
				getProgressBarAssinarXmlMec().resetar();
			}
			if (getProgressBarAssinarXmlMec().getAtivado() || getProgressBarAssinarXmlMec().getPollAtivado()) {
				throw new Exception("Já estão sendo realizadas assinaturas de xml em aplicação");
			}
			iniciarProgressBar();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void iniciarProgressBar() throws Exception {
		validarDadosAssinaturaXmlLote();
		validarDadosCertificadoSelecionado();
		List<DocumentoAssinadoVO> listaDocumentoAssinarLacuna = ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().filter(DocumentoAssinadoVO::getSelecionado).collect(Collectors.toList());
		setJsonListaDocumentoAssinadoAssinar(Uteis.converterObjetoParaJson(listaDocumentoAssinarLacuna.stream().map(DocumentoAssinadoVO::getCodigo).collect(Collectors.toList())));
		setUrlAcessoExternoAplicacao(getConfiguracaoGeralPadraoSistema().getUrlAcessoExternoAplicacao());
		setOncompleteModal("RichFaces.$('panelConfirmarAssinaturaXml').hide(); iniciarAssinaturaLoteXmlLacuna();");
		setCodigoPessoaLogadaAssinarXml(getUsuarioLogado().getPessoa().getCodigo());
		getAplicacaoControle().limparAplicacaoMapDocumentoAssinarXml();
		getAplicacaoControle().adicionarListaDocumentoAssinadoAssinarXmlEmAplicacao(listaDocumentoAssinarLacuna);
		getAplicacaoControle().setProgressBarAssinarXmlMec(new ProgressBarVO());
		getProgressBarAssinarXmlMec().setUsuarioVO(getUsuarioLogadoClone());
		getProgressBarAssinarXmlMec().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
		getProgressBarAssinarXmlMec().setCaminhoWebRelatorio(getCaminhoPastaWeb());			
		getProgressBarAssinarXmlMec().resetar();
		getProgressBarAssinarXmlMec().iniciar(0l, listaDocumentoAssinarLacuna.size(), "Iniciando Processo de Assinatura com Certificado A3", Boolean.TRUE, this, "inicioAssinaturaXmlLacuna");
	}
	
	public void inicioAssinaturaXmlLacuna() {
		while (getProgressBarAssinarXmlMec().getAtivado() 
				&& getProgressBarAssinarXmlMec().getPollAtivado() 
				&& !getProgressBarAssinarXmlMec().getForcarEncerramento() 
				&& Objects.nonNull(getAplicacaoControle().getMapDocumentoAssinadoAssinarXml()) 
				&& !getAplicacaoControle().getMapDocumentoAssinadoAssinarXml().isEmpty()) {
			
		}
		setApresentarModalProgressBar(Boolean.FALSE);
	}

	public void cancelarProgressBarAssinaturaXml() {
		setApresentarModalProgressBar(Boolean.FALSE);
		getAplicacaoControle().setMapDocumentoAssinadoAssinarXml(null);
		getProgressBarAssinarXmlMec().setForcarEncerramento(Boolean.TRUE);
		getProgressBarAssinarXmlMec().forcarEncerramento();
		setOncompleteModal(Constantes.EMPTY);
		consultarDocumentos();
	}
	
	public ProgressBarVO getProgressBarAssinarXmlMec() {
		return getAplicacaoControle().getProgressBarAssinarXmlMec();
	}
	
	/**
	 * Método para realizar a inicialização para realizar assinaturas do tipo A3 em
	 * XMLS do MEC
	 * 
	 * @author Felipi Alves
	 * @chamado 42281
	 */
	public void validarDadosIniciarConsultaCertificadosIntalados() throws Exception {
		setApresentarModalProgressBar(Boolean.FALSE);
		if ((Objects.isNull(getAplicacaoControle().getMapDocumentoAssinadoAssinarXml()) || getAplicacaoControle().getMapDocumentoAssinadoAssinarXml().isEmpty()) && (getProgressBarAssinarXmlMec().getAtivado() || getProgressBarAssinarXmlMec().getPollAtivado())) {
			getAplicacaoControle().setDataUltimaAtualizacaoAssinaturaXml(null);
			getProgressBarAssinarXmlMec().setForcarEncerramento(Boolean.TRUE);
			getProgressBarAssinarXmlMec().resetar();
		} else if (getAplicacaoControle().isPeriodoAssinaturaXmlExcedido() && (getProgressBarAssinarXmlMec().getAtivado() || getProgressBarAssinarXmlMec().getPollAtivado())) {
			getAplicacaoControle().setDataUltimaAtualizacaoAssinaturaXml(null);
			getProgressBarAssinarXmlMec().setForcarEncerramento(Boolean.TRUE);
			getProgressBarAssinarXmlMec().resetar();
		}
		if (getAplicacaoControle().getProgressBarAssinarXmlMec().getAtivado() || getAplicacaoControle().getProgressBarAssinarXmlMec().getPollAtivado()) {
			throw new Exception("Já estão sendo realizadas assinaturas de xml em aplicação");
		}
		setListaSelectItemCertificadoInstalado(null);
		setListaCertificadoInstalado(null);
		setThumbprintCertificado(null);
		setCodigoPessoaLogadaAssinarXml(null);
		setJsonListaCertificadoInstalado(null);
		setJsonListaDocumentoAssinadoAssinar(null);
		setNavegadorLogado(null);
		setExtensaoLacunaInstalada(null);
		setOncompleteModal("RichFaces.$('panelConfirmarAssinaturaXml').show();");
		setApresentarModalProgressBar(Boolean.TRUE);
	}
	
	public String getAvisoAssinaturaXmlLacuna() {
		return "<p style=\"text-align: center;\"><strong style=\"font-size: 16px;\">AVISO</strong></p> <p style=\"text-align: center;\"><strong style=\"font-size: 16px;\"><br></strong></p> <p><strong style=\"font-size: 14px;\"> Para realizar a assinatura de um documento XML, é necessário observar os seguintes requisitos:</strong></p> <p><strong style=\"font-size: 14px;\"><br></strong> </p> <p><span style=\"font-size: 14px;\"><span style=\"font-size: 14px;\">  1 - </span><strong>Seleção do Certificado</strong><span style=\"font-size: 14px;\">: Escolha o certificado digital adequado instalado em sua máquina, de acordo com a ordem de assinatura exigida. Se a ordem de assinatura for&nbsp;E-CPF, o usuário deve selecionar um certificado do tipo&nbsp;E-CPF. Da mesma forma, se a ordem for&nbsp;E-CNPJ, deve-se optar por um certificado&nbsp;E-CNPJ.</span></span></p> <p><br></p> <p><span style=\"font-size: 14px;\"><span style=\"font-size: 14px;\">  2 - </span><strong>Certificado Físico</strong><span style=\"font-size: 14px;\">: O certificado digital físico deve estar devidamente conectado ao computador para a execução da assinatura.</span></span></p> <p><br></p> <p><span style=\"font-size: 14px;\"><span style=\"font-size: 14px;\">  3 - </span><strong>Processo de Assinatura</strong><span style=\"font-size: 14px;\">: Durante o processo de assinatura, evite atualizar a página ou interromper a operação. Em caso de falha ou interrupção do processo, entre em contato com a equipe técnica para suporte.</span></span></p> <p><br></p> <p><span style=\"font-size: 14px;\"><span style=\"font-size: 14px;\">4 - </span><strong>Execução Sequencial</strong><span style=\"font-size: 14px;\">: Não será possível iniciar um novo processo de assinatura enquanto o anterior não for finalizado completamente. Certifique-se de concluir cada etapa antes de prosseguir com outra assinatura.</span></span></p>";
	}
	
	public Boolean getApresentarModalProgressBar() {
		if (apresentarModalProgressBar == null) {
			apresentarModalProgressBar = Boolean.FALSE;
		}
		return apresentarModalProgressBar;
	}
	
	public void setApresentarModalProgressBar(Boolean apresentarModalProgressBar) {
		this.apresentarModalProgressBar = apresentarModalProgressBar;
	}

	public ProgressBarVO getProgressBarTechCert() {
		if (progressBarTechCert == null) {
			progressBarTechCert = new ProgressBarVO();
		}
		return progressBarTechCert;
	}

	public void setProgressBarTechCert(ProgressBarVO progressBarTechCert) {
		this.progressBarTechCert = progressBarTechCert;
	}

	public DocumentoAssinadoVO getDocumentoAssinadoTrocaResponsavel() {
		if (documentoAssinadoTrocaResponsavel == null) {
			documentoAssinadoTrocaResponsavel = new DocumentoAssinadoVO();
		}
		return documentoAssinadoTrocaResponsavel;
	}

	public void setDocumentoAssinadoTrocaResponsavel(DocumentoAssinadoVO documentoAssinadoTrocaResponsavel) {
		this.documentoAssinadoTrocaResponsavel = documentoAssinadoTrocaResponsavel;
	}

	public DocumentoAssinadoPessoaVO getDocumentoAssinadoPessoaTrocaResponsavel() {
		if (documentoAssinadoPessoaTrocaResponsavel == null) {
			documentoAssinadoPessoaTrocaResponsavel = new DocumentoAssinadoPessoaVO();
		}
		return documentoAssinadoPessoaTrocaResponsavel;
	}

	public void setDocumentoAssinadoPessoaTrocaResponsavel(DocumentoAssinadoPessoaVO documentoAssinadoPessoaTrocaResponsavel) {
		this.documentoAssinadoPessoaTrocaResponsavel = documentoAssinadoPessoaTrocaResponsavel;
	}

	public Boolean getPermitirAlterarResponsavelAssinatura() {
		if(permitirAlterarResponsavelAssinatura == null) {
			try {
				if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("PermitirAlterarAssinante", getUsuarioLogado())) {
					permitirAlterarResponsavelAssinatura = true;
				}else {
					permitirAlterarResponsavelAssinatura = false;
				}
			}catch (Exception e) {
				permitirAlterarResponsavelAssinatura = false;
			}
		}
		return permitirAlterarResponsavelAssinatura;
	}

	private String onCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote;

	public String getOnCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote() {
		if (onCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote == null) {
			onCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote = "";
		}
		return onCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote;
	}

	public void setOnCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote(String onCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote) {
		this.onCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote = onCompletaModalTrocarResponsavelDocumentoAssinadoPessoaEmLote;
	}

	public List<SelectItem> getListaSelectItemPessoaRetirarDocumentoVO() {
		if (listaSelectItemPessoaRetirarDocumentoVO == null) {
			listaSelectItemPessoaRetirarDocumentoVO = new ArrayList<>();
			listaSelectItemPessoaRetirarDocumentoVO.add(new SelectItem("", ""));
		}
		return listaSelectItemPessoaRetirarDocumentoVO;
	}

	public void setListaSelectItemPessoaRetirarDocumentoVO(List<SelectItem> listaSelectItemPessoaRetirarDocumentoVO) {
		this.listaSelectItemPessoaRetirarDocumentoVO = listaSelectItemPessoaRetirarDocumentoVO;
	}

	public List<DocumentoAssinadoVO> getDocumentoAssinadoTrocaResponsavelLote() {
		if (documentoAssinadoTrocaResponsavelLote == null) {
			documentoAssinadoTrocaResponsavelLote = new ArrayList<>();
		}
		return documentoAssinadoTrocaResponsavelLote;
	}

	public void setDocumentoAssinadoTrocaResponsavelLote(List<DocumentoAssinadoVO> documentoAssinadoTrocaResponsavelLote) {
		this.documentoAssinadoTrocaResponsavelLote = documentoAssinadoTrocaResponsavelLote;
	}

	public PessoaVO getPessoaNovaAssinaturaVO() {
		if (pessoaNovaAssinaturaVO == null) {
			pessoaNovaAssinaturaVO = new PessoaVO();
		}
		return pessoaNovaAssinaturaVO;
	}

	public void setPessoaNovaAssinaturaVO(PessoaVO pessoaNovaAssinaturaVO) {
		this.pessoaNovaAssinaturaVO = pessoaNovaAssinaturaVO;
	}

	public PessoaVO getPessoaRetirarDocumentoVO() {
		if (pessoaRetirarDocumentoVO == null) {
			pessoaRetirarDocumentoVO = new PessoaVO();
		}
		return pessoaRetirarDocumentoVO;
	}

	public void setPessoaRetirarDocumentoVO(PessoaVO pessoaRetirarDocumentoVO) {
		this.pessoaRetirarDocumentoVO = pessoaRetirarDocumentoVO;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isExisteDocumentoPendenteTechesertSelecionado() {
		return ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().anyMatch(d -> d.getSelecionado() && d.isDocumentoAssinadoTechsert());
	}
	
	@SuppressWarnings("unchecked")
	public boolean isExisteDocumentoPendenteCertisgnSelecionado() {
		return ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().anyMatch(d -> d.getSelecionado() && d.isDocumentoAssinadoCertisign());
	}
	
	@SuppressWarnings("unchecked")
	public boolean isExisteDocumentoPendenteSeiSelecionado() {
		return ((List<DocumentoAssinadoVO>) getListaDocumentosPendentes().getListaConsulta()).stream().anyMatch(d -> d.getSelecionado() && d.isDocumentoAssinadoSei());
	}
}
