package controle.academico;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.FilterFactory;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.OperacaoImportacaoSalaBlackboardEnum;
import negocio.comuns.academico.enumeradores.SalaBlackboardSituacaoNotaEnum;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.BlackboardFechamentoNotaOperacaoVO;
import negocio.comuns.blackboard.BlackboardGestaoFechamentoNotaVO;
import negocio.comuns.blackboard.HistoricoNotaBlackboardVO;
import negocio.comuns.blackboard.LogOperacaoEnsalamentoBlackboardVO;
import negocio.comuns.blackboard.SalaAulaBlackboardGrupoVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.SugestaoFacilitadorBlackboardVO;
import negocio.comuns.blackboard.enumeradores.SituacaoHistoricoNotaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.ead.SalaAulaBlackboardOperacaoVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.moodle.OperacaoMoodleVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoOperacaoMoodleEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.blackboard.SalaAulaBlackboardOperacao;
import negocio.facade.jdbc.moodle.OperacaoMoodle;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Controller("GestaoSalasNotasBlackboardControle")
@Scope("viewScope")
@Lazy
public class GestaoSalasNotasBlackboardControle extends SuperControleRelatorio implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7082278143907165510L;

	private List<SelectItem> listaSelectItemPeriodicidade;

	private List<SelectItem> tipoConsultaComboDisciplina;

	private List<SelectItem> tipoConsultaComboAluno;

	private TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum;

	private String periodicidade;

	private String idSalaAulaBlackboard;
	
	private String ano;

	private String semestre;

	private CursoVO curso;

	private PessoaVO pessoa;

	private DisciplinaVO disciplina;

	private MatriculaVO matricula;

	private SalaBlackboardSituacaoNotaEnum situacaoNota;

	private List<SelectItem> listaDisciplinaClassificada;	
	private List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVO;
	private List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVOFechamento;
	private ClassificacaoDisciplinaEnum tipoFechamentoClassificacaoDisciplinaEnum;
	private SalaAulaBlackboardGrupoVO totalizadoSalaAulaBlackboardGrupoVO;

	private Integer qtdLinhasExportacaoPorSalaAulaBlackboard;
	private Boolean exportarSala;

	private Boolean exportarAlunos;

	private Boolean exportarNotas;

	private Boolean exportarProfessores;

	private Boolean exportarFacilitadores;

	private Boolean exportarSupervisores;

	private OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoProfessor;

	private OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoFacilitador;

	private OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoSupervisor;

	private OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoNota;

	private UploadedFile arquivo;
	
	private InputStream inputStreamSalaImporta;

	private DataModelo dadosConsultaSalaAula;

	private String tipoAdicionarFacilitador;

	private List<SelectItem> tipoAdicionarComboFacilitador;

	private List<SelectItem> tipoConsultaComboFacilitador;

	private SalaAulaBlackboardVO salaAulaBlackboardSelecionada;

	private SalaAulaBlackboardPessoaVO facilitadorSelecionado;

	private List<SugestaoFacilitadorBlackboardVO> listaSugestaoFacilitadores;

	private Integer quantidadeFacilitadoresPorSala;

	private List<SalaAulaBlackboardPessoaVO> listaSalaAulaBlackboardPessoaImportar;

	private List<SalaAulaBlackboardVO> listaGeracaoSalaAulaBlackboard;
	private Integer bimestreGeracaoSala;
	private Integer situacaoAlunoGeracaoSala;
	private List<SelectItem> listaSelectItemSituacaoAlunoGeracaoSala;
	private List<SelectItem> listaSelectItemBimestre;
	private List<MatriculaPeriodoVO> listaMatriculaPeriodoVO;
	private DisciplinaVO disciplinaGeracaoSala;
	private ProgressBarVO progressBarVO;
	private SalaAulaBlackboardVO totalizadoSalaAulaBlackboardVO;

	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<CursoVO> listaConsultaCurso;
	private List<MatriculaVO> listaConsultaAluno;	
	private CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO;
	private boolean permiteExcluirSalaAulaBlackboard = false;
	private boolean permiteCriarAtualizarSalaAulaBlackboard = false;
	private boolean permiteOperacoesGrupoTcc = false;
	private boolean permiteOperacoesGrupoProjetoIntegrador = false;
	private List<SelectItem> listaSalaAulaBlackboardGrupoDisponivel;
	private String idSalaAulaBlackboardGrupoDisponivel;
	private DataModelo controleConsultaLogProcessamento;
	private String nomeArquivoUploadSalaAulaBlackboard;

	private SalaAulaBlackboardPessoaVO alunoSalaAulaBlackboardSelecionado;

	private HistoricoNotaBlackboardVO historicoNotaBlackboardSelecionado;
	private DataModelo controleConsultaHistoricoNotaBlackboard;
	private List<SelectItem> listaSelectConteudoMasterBlackboard;
	private boolean marcarTodasSalaConteudoMaster = false;	
	private boolean realizarBuscarNotaBlackboard = true;
	private boolean realizarCalculoMediaApuracaoNotas = false;
	
	private List<SelectItem> listaSelectItemCalendarioAgrupamento;	
	
	private DataModelo controleConsultaLogEnsalamento;
	private LogOperacaoEnsalamentoBlackboardVO logOperacaoEnsalamentoBlackboardFiltroVO;
	
	private BlackboardGestaoFechamentoNotaVO blackboardGestaoFechamentoNotaVO;
	private Boolean operacaoFechamentoNotaPendente;
	private String oncompleteFechamentoNota;
	private Boolean apresentarProgressBarFechamentoNota;
	private List<BlackboardFechamentoNotaOperacaoVO> listaFechamentoNotaErro;
	
	private DataModelo dataModeloOperacaoMoodleProcessamentoPendente;
	private String nivelEducacionalApresentar;
	private List<PessoaVO> listaConsultaPessoa;
	private DataModelo dataModeloOperacaoMoodleProcessamentoErro;
	private DataModelo dataModeloRegistroExecucaoJob;
	private OperacaoMoodleVO operacaoMoodle;
	private PessoaVO supervisor;
	private String campoConsultaPessoa;
	private String valorConsultaPessoa;

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			setAno(Uteis.getAnoDataAtual());
			setSemestre(Uteis.getSemestreAtual());
			getFiltroRelatorioAcademicoVO().setJubilado(false);
			isVerificarPermissaoOperacoesGrupoTcc();
			isVerificarPermissaoOperacoesGrupoProjetoIntegrador();
			isVerificarPermissaoExcluirSalaAulaBlackboard();
			isVerificarPermissaoCriarAtualizarSalaAulaBlackboard();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultarPorUnidadeEnsino(
					getControleConsulta().getCampoConsulta().toLowerCase(), getControleConsulta().getValorConsulta(),
					getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			setCurso((CursoVO) context().getExternalContext().getRequestMap().get("cursoItem"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}


	public void limparCampoCurso() {
		setCurso(new CursoVO());
	}

	public void limparConsultaRichModalDisciplina() {
		setDisciplina(new DisciplinaVO());
		limparConsultaRichModal();
	}
	
	public void limparConsultaRichModalCurso() {
		setCurso(new CursoVO());
	}
	
	public void limparConsultaRichModal() {
		getListaConsulta().clear();
		setControleConsulta(null);
	}

	public void consultarSupervisor() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaPessoa().equals("nome")) {
				if (getValorConsultaPessoa().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getSalaAulaBlackboardFacade().consultarFacilitadorProfessorSupervisorPorNome(getValorConsultaPessoa(), getAno(), getSemestre());
			}
			if (getCampoConsultaPessoa().equals("cpf")) {
				if (getValorConsultaPessoa().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getSalaAulaBlackboardFacade().consultarFacilitadorProfessorSupervisorPorCPF(getValorConsultaPessoa(), getAno(), getSemestre());
			}
			setListaConsultaPessoa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarSupervisor() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("supervisorItem");
			setSupervisor(obj);
			getListaConsultaPessoa().clear();;
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void limparSupervisor() {
		try {
			setSupervisor(new PessoaVO());
		} catch (Exception e) {
		    setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public Boolean getApresentarCampoCpf() {
		return getCampoConsultaPessoa().equals("cpf") ? true : false;
	}
	
	public List<SelectItem> getTipoConsultaComboPessoa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}
	
	public List<PessoaVO> getListaConsultaPessoa() {
		if (listaConsultaPessoa == null) {
			listaConsultaPessoa = new ArrayList<PessoaVO>();
		}
		return listaConsultaPessoa;
	}

	public void setListaConsultaPessoa(List<PessoaVO> listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = "";
		}
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = "";
		}
		return valorConsultaPessoa;
	}

	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	public void consultarDisciplina() {
		try {
			setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorUnidadeEnsinoCursoTurma(
					getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(),
					getUnidadeEnsinoVOs(), getCurso().getCodigo(), 0,
					Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsultaDisciplina().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarDisciplina() {
		try {
			setDisciplina((DisciplinaVO) context().getExternalContext().getRequestMap().get(Constantes.disciplinaItens));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDisciplina() {
		setDisciplina(null);
	}

	public void consultarAluno() {
		try {
			setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultarPorUnidadeEnsinoCursoTurmaDisciplina(
					getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(),
					getUnidadeEnsinoVOs(), getCurso().getCodigo(), 0, getDisciplina().getCodigo(),
					getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultaAluno().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarAluno() {
		try {
			setMatricula((MatriculaVO) context().getExternalContext().getRequestMap().get(Constantes.matriculaItens));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparAluno() {
		setMatricula(null);
	}

	public void realizarUpload(FileUploadEvent uploadEvent) {
		try {
			setNomeArquivoUploadSalaAulaBlackboard(uploadEvent.getUploadedFile().getName());
			setInputStreamSalaImporta(uploadEvent.getUploadedFile().getInputStream());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} 
	}
	
	public void realizarProcessamentoUploadFileImportaSalas() {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().upLoadArquivoImportado(getInputStreamSalaImporta(),getListaSalaAulaBlackboardPessoaImportar(), getUsuarioLogadoClone());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} 
	}

	public void realizarImportacaoSalas() {
		try {			
			Uteis.checkState(getListaSalaAulaBlackboardPessoaImportar().isEmpty(), "Não foi realizado o upload do arquivo para importação.");
			Uteis.checkState(!getListaSalaAulaBlackboardPessoaImportar().stream().anyMatch(p->Uteis.isAtributoPreenchido(p.getPessoaEmailInstitucionalVO())), "Não foi Localizado Nenhum Email a ser Importado.");
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarImportacao(
					getListaSalaAulaBlackboardPessoaImportar(),
					getOperacaoImportacaoProfessor(),
					getOperacaoImportacaoFacilitador(),
					getOperacaoImportacaoSupervisor(),
					getOperacaoImportacaoNota(), getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarSalaAulaBlackboardOperacao(getUsuarioLogadoClone());
			realizarValidacaoProcessamentoEmAndamento();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarValidacaoProcessamentoEmAndamento() {
		try {
			limparMensagem();
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setSuperControle(this);
			getProgressBarVO().setAssincrono(false);
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarEnsalamentoPendenteProcessamento(getProgressBarVO());
			if((getProgressBarVO().getForcarEncerramento() || !getProgressBarVO().getAtivado())) {
				consultarSalasBlackboard();
			}
		}catch(Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void scrollerListenerSalaAula(DataScrollEvent dataScrollerEvent) throws Exception {
		getDadosConsultaSalaAula().setPage(dataScrollerEvent.getPage());
		getDadosConsultaSalaAula().setPaginaAtual(dataScrollerEvent.getPage());
		consultarSalasBlackboard();
	}

	public void consultarSalasBlackboard() {
		try {
			Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()) && !Uteis.isAtributoPreenchido(getIdSalaAulaBlackboard()), "O campo ANO deve ser informado.");
			Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()) && !Uteis.isAtributoPreenchido(getIdSalaAulaBlackboard()), "O campo SEMESTRE deve ser informado.");
			getFacadeFactory().getSalaAulaBlackboardFacade().consultar(getIdSalaAulaBlackboard(), getUnidadeEnsinoVOs(), getTipoSalaAulaBlackboardEnum(), getAno(), getSemestre(), getBimestreGeracaoSala(),
					getCurso().getCodigo(), getSupervisor().getCodigo(), getDisciplina().getCodigo(),
					getMatricula().getMatricula(), getFiltroRelatorioAcademicoVO(), getUsuarioLogadoClone(), getDadosConsultaSalaAula(), getNivelEducacionalApresentar());
			setHistoricoNotaBlackboardSelecionado(new HistoricoNotaBlackboardVO());
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void scrollerListenerAlunos(DataScrollEvent dataScrollerEvent) throws Exception {
		SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap()
				.get(Constantes.salaAulaItem);
		obj.getDadosConsultaAlunos().setPage(dataScrollerEvent.getPage());
		obj.getDadosConsultaAlunos().setPaginaAtual(dataScrollerEvent.getPage());
		getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosPorSalaAulaBlackboardOtimizado(obj, getFiltroRelatorioAcademicoVO(),
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone());
	}

	public void scrollerListenerFacilitadores(DataScrollEvent dataScrollerEvent) throws Exception {
		SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap()
				.get(Constantes.salaAulaItem);
		obj.getDadosConsultaFacilitadores().setPage(dataScrollerEvent.getPage());
		obj.getDadosConsultaFacilitadores().setPaginaAtual(dataScrollerEvent.getPage());
		getFacadeFactory().getSalaAulaBlackboardPessoaFacade()
				.consultarProfessoresFacilitadoresESupervisoresPorSalaAulaBlackboardOtimizado(obj,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone());
	}

	public void consultarAlunosPorNomeEmail() throws Exception {
		FiltroRelatorioAcademicoVO fra = new FiltroRelatorioAcademicoVO();
		fra.realizarDesmarcarTodasSituacoes();
		SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap()
				.get(Constantes.salaAulaItem);
		obj.getDadosConsultaAlunos().setPage(0);
		obj.getDadosConsultaAlunos().setPaginaAtual(0);
		getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosPorSalaAulaBlackboardOtimizado(obj, fra,
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone());
	}

	public void consultarFacilitadoresPorNomeEmail() throws Exception {
		SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap()
				.get(Constantes.salaAulaItem);
		obj.getDadosConsultaFacilitadores().setPage(0);
		obj.getDadosConsultaFacilitadores().setPaginaAtual(0);
		getFacadeFactory().getSalaAulaBlackboardPessoaFacade()
				.consultarProfessoresFacilitadoresESupervisoresPorSalaAulaBlackboardOtimizado(obj,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone());
	}

	public void selecionarSalaAulaAluno() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap().get(Constantes.salaAulaItem);
			setSalaAulaBlackboardSelecionada(obj);
			setTipoAdicionarFacilitador(Constantes.ALUNO);			
			getTipoAdicionarComboFacilitador().clear();
			getTipoAdicionarComboFacilitador().add(new SelectItem(Constantes.ALUNO, Constantes.Aluno));
			setControleConsulta( new ControleConsulta());
			getListaConsulta().clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}

	}
	
	public void selecionarSalaAula() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap().get(Constantes.salaAulaItem);
			Uteis.checkState(obj.getTipoSalaAulaBlackboardEnum().isTccGrupo(), "Não é possível adicionar um Professores/Facilitadores/Supervisores para um Grupo da Blackboard. Deve ser adicionado para Sala Principal.");
			setSalaAulaBlackboardSelecionada(obj);
			setTipoAdicionarFacilitador(Constantes.EMPTY);			
			tipoAdicionarComboFacilitador = null;
			setOncompleteModal("RichFaces.$('panelAdicionarPessoa').show()");
			setControleConsulta( new ControleConsulta());
			getListaConsulta().clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setOncompleteModal(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
		
	}
	
	
	
	public void selecionarSalaAulaExclusao() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap().get(Constantes.salaAulaItem);
			setSalaAulaBlackboardSelecionada(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarSalaAulaCopiaConteudoMaster() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap().get(Constantes.salaAulaItem);
			setSalaAulaBlackboardSelecionada(obj);
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			Uteis.checkState(!Uteis.isAtributoPreenchido(configSeiBlackboardVO.getFonteDeDadosConteudoMasterBlackboard()), "Não esta parametrizado a Fonte de Dados do Conteúdo Master da Configuração Sei Blackboard.");
			montarListaSelectConteudoMasterBlackboard();
			if(getSalaAulaBlackboardSelecionada().getTipoSalaAulaBlackboardEnum().isEstagio() 
					&& Uteis.isAtributoPreenchido(getSalaAulaBlackboardSelecionada().getCursoVO().getIdConteudoMasterBlackboardEstagio())) {
				if(getListaSelectConteudoMasterBlackboard().stream().noneMatch(p-> p.getValue().equals(getSalaAulaBlackboardSelecionada().getCursoVO().getIdConteudoMasterBlackboardEstagio()))) {
					SalaAulaBlackboardVO resultadoConsulta = getFacadeFactory().getSalaAulaBlackboardFacade().realizarConsultaSalaAulaBlackboardConteudoMaster(getSalaAulaBlackboardSelecionada().getCursoVO().getIdConteudoMasterBlackboardEstagio(), getUsuarioLogado());
					getListaSelectConteudoMasterBlackboard().add(new SelectItem(resultadoConsulta.getIdSalaAulaBlackboard(), resultadoConsulta.getNome()));
				}
				getSalaAulaBlackboardSelecionada().setIdSalaAulaBlackboardConteudoMaster(getSalaAulaBlackboardSelecionada().getCursoVO().getIdConteudoMasterBlackboardEstagio());
			}else if((getSalaAulaBlackboardSelecionada().getTipoSalaAulaBlackboardEnum().isDisciplina()
					||getSalaAulaBlackboardSelecionada().getTipoSalaAulaBlackboardEnum().isTcc()
					||getSalaAulaBlackboardSelecionada().getTipoSalaAulaBlackboardEnum().isTccAmbientacao()
					||getSalaAulaBlackboardSelecionada().getTipoSalaAulaBlackboardEnum().isProjetoIntegrador()
					||getSalaAulaBlackboardSelecionada().getTipoSalaAulaBlackboardEnum().isProjetoIntegradorAmbientacao()) 
					&& Uteis.isAtributoPreenchido(getSalaAulaBlackboardSelecionada().getDisciplinaVO().getIdConteudoMasterBlackboard())) {
				if(getListaSelectConteudoMasterBlackboard().stream().noneMatch(p-> p.getValue().equals(getSalaAulaBlackboardSelecionada().getDisciplinaVO().getIdConteudoMasterBlackboard()))) {
					SalaAulaBlackboardVO resultadoConsulta = getFacadeFactory().getSalaAulaBlackboardFacade().realizarConsultaSalaAulaBlackboardConteudoMaster(getSalaAulaBlackboardSelecionada().getDisciplinaVO().getIdConteudoMasterBlackboard(), getUsuarioLogado());
					getListaSelectConteudoMasterBlackboard().add(new SelectItem(resultadoConsulta.getIdSalaAulaBlackboard(), resultadoConsulta.getNome()));
				}
				getSalaAulaBlackboardSelecionada().setIdSalaAulaBlackboardConteudoMaster(getSalaAulaBlackboardSelecionada().getDisciplinaVO().getIdConteudoMasterBlackboard());
			}
			setOncompleteModal("RichFaces.$('panelCopiarConteudoBlackboard').show()");
			setMensagemID(MSG_TELA.msg_dados_selecionados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void montarListaSelectConteudoMasterBlackboard() {
		try {
			List<SalaAulaBlackboardVO> resultadoConsulta = getFacadeFactory().getSalaAulaBlackboardFacade().realizarConsultaSalaAulaBlackboardConteudoMaster(getUsuarioLogado());
			setListaSelectConteudoMasterBlackboard(UtilSelectItem.getListaSelectItem(resultadoConsulta, "idSalaAulaBlackboard", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarCopiaSalaAulaConteudoIndividual() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getSalaAulaBlackboardSelecionada()), "Para realizar essa operação dever ser informado uma sala aula blackboard.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(getSalaAulaBlackboardSelecionada().getIdSalaAulaBlackboardConteudoMaster()), "o campo Sala do Conteudo Mestre deve ser informado.");
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirCopiaConteudoSalaBlack(getSalaAulaBlackboardSelecionada(), SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD_COPIA_CONTEUDO,getUsuarioLogadoClone());
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			realizarValidacaoProcessamentoEmAndamento();
			setSalaAulaBlackboardSelecionada(new SalaAulaBlackboardVO());
			setOncompleteModal("RichFaces.$('panelCopiarConteudoBlackboard').hide()");
			setMensagemID(MSG_TELA.msg_dados_gravados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarRemocaoSalaAulaConteudoIndividual() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getSalaAulaBlackboardSelecionada()), "Para realizar essa operação dever ser informado uma sala aula blackboard.");
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirCopiaConteudoSalaBlack(getSalaAulaBlackboardSelecionada(), SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD_DELETAR_CONTEUDO, getUsuarioLogadoClone());
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			realizarValidacaoProcessamentoEmAndamento();
			setSalaAulaBlackboardSelecionada(new SalaAulaBlackboardVO());
			setOncompleteModal("RichFaces.$('panelCopiarConteudoBlackboard').hide()");
			setMensagemID(MSG_TELA.msg_dados_gravados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarVisualizacaoCopiaConteudo() {
		List<SalaAulaBlackboardVO> listaTemp = new ArrayList<>();
		List<SalaAulaBlackboardGrupoVO> listaGrupoTemp = new ArrayList<>();
		try {
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			Uteis.checkState(!Uteis.isAtributoPreenchido(configSeiBlackboardVO.getFonteDeDadosConteudoMasterBlackboard()), "Não esta parametrizado a Fonte de Dados do Conteúdo Master da Configuração Sei Blackboard.");
			setOncompleteModal(Constantes.EMPTY);
			setListaSalaAulaBlackboardGrupoVO(new ArrayList<>());
			setTotalizadoSalaAulaBlackboardGrupoVO(new SalaAulaBlackboardGrupoVO());
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setSuperControle(this);
			getProgressBarVO().setAssincrono(false);
			getProgressBarVO().setOncomplete("RichFaces.$('panelConfirmarCopiaConteudoLote').show()");
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarEnsalamentoPendenteProcessamento(getProgressBarVO());
			listaTemp =  getFacadeFactory().getSalaAulaBlackboardFacade().consultaPadraoSalaAulaBlackBoardCopiaConteudo(getIdSalaAulaBlackboard(), getUnidadeEnsinoVOs(), getTipoSalaAulaBlackboardEnum(), getAno(), getSemestre(), getBimestreGeracaoSala(),
					getCurso().getCodigo(), getSupervisor().getCodigo(), getDisciplina().getCodigo(),
					getMatricula().getMatricula(), getFiltroRelatorioAcademicoVO(), getUsuarioLogadoClone(), "");
			if(!listaTemp.isEmpty()) {
				montarListaSelectConteudoMasterBlackboard();
				Map<String, List<SalaAulaBlackboardVO>> mapa = listaTemp.stream()						
						.collect(Collectors.groupingBy(p -> p.getIdTipoSalaAulaBlackboardEnumPorDisciplina()));
				
				for (Map.Entry<String, List<SalaAulaBlackboardVO>> map : mapa.entrySet()) {
					SalaAulaBlackboardGrupoVO sabGrupo = new SalaAulaBlackboardGrupoVO();
					sabGrupo.setAgrupamentoUnidadeEnsinoVO(map.getValue().get(0).getAgrupamentoUnidadeEnsinoVO());
					sabGrupo.setDisciplinaVO(map.getValue().get(0).getDisciplinaVO());
					sabGrupo.setCursoVO(map.getValue().get(0).getCursoVO());
					sabGrupo.setAno(map.getValue().get(0).getAno());
					sabGrupo.setSemestre(map.getValue().get(0).getSemestre());
					sabGrupo.setTipoSalaAulaBlackboardEnum(map.getValue().get(0).getTipoSalaAulaBlackboardEnum());
					sabGrupo.setNrSala(map.getValue().size());
					sabGrupo.getListaGrupoSalaAulaBlackboardVO().addAll(map.getValue());
					sabGrupo.getListaSelectConteudoMasterBlackboard().addAll(getListaSelectConteudoMasterBlackboard());
					if(sabGrupo.getTipoSalaAulaBlackboardEnum().isEstagio() && !sabGrupo.getCursoVO().getIdConteudoMasterBlackboardEstagio().isEmpty()) {
						sabGrupo.setIdSalaAulaBlackboardConteudoMaster(sabGrupo.getCursoVO().getIdConteudoMasterBlackboardEstagio());
					}else {
						sabGrupo.setIdSalaAulaBlackboardConteudoMaster(sabGrupo.getDisciplinaVO().getIdConteudoMasterBlackboard());
					}
					listaGrupoTemp.add(sabGrupo);
				}
				getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardExistentes(listaTemp.size());
				getListaSalaAulaBlackboardGrupoVO().addAll(listaGrupoTemp.stream()
						.sorted(Comparator.comparing(p -> ((SalaAulaBlackboardGrupoVO) p).getTipoSalaAulaBlackboardEnum()).thenComparing(p-> ((SalaAulaBlackboardGrupoVO) p).getDisciplinaVO().getAbreviatura()))
						.collect(Collectors.toList()));
			}
			if((getProgressBarVO().getForcarEncerramento() || !getProgressBarVO().getAtivado())) {
				setOncompleteModal("RichFaces.$('panelConfirmarCopiaConteudoLote').show()");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}finally {
			listaGrupoTemp = null;
			listaTemp = null;
		}
	}
	
	public void realizarCopiaSalaAulaConteudoPorSalaAulaBlackboardGrupoVOSelecionada() {
		try {
			SalaAulaBlackboardGrupoVO obj = (SalaAulaBlackboardGrupoVO) context().getExternalContext().getRequestMap().get(Constantes.sabGrupoItem);
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getListaGrupoSalaAulaBlackboardVO()), "Não foi localizado nenhum Sala de Aula Blackboard para ter o Conteúdo Mestre a ser copiado.");
			for (SalaAulaBlackboardVO sab : obj.getListaGrupoSalaAulaBlackboardVO()) {
				sab.setIdSalaAulaBlackboardConteudoMaster(obj.getIdSalaAulaBlackboardConteudoMaster());
				Uteis.checkState(!Uteis.isAtributoPreenchido(sab.getIdSalaAulaBlackboardConteudoMaster()), "o campo Sala do Conteúdo Mestre deve ser informado.");
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirCopiaConteudoSalaBlack(sab, SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD_COPIA_CONTEUDO,getUsuarioLogadoClone());	
			}
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setOncomplete("RichFaces.$('panelConfirmarCopiaConteudoLote').hide()");
			realizarAtualizacaoStatusSalasBlackboard("panelCopiarConteudoBlackboard");
			setMensagemID(MSG_TELA.msg_dados_gravados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarCopiaSalaAulaConteudoPorSalaAulaBlackboardGrupoVO() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getListaSalaAulaBlackboardGrupoVO()), "Não foi localizado nenhum Sala de Aula Blackboard para ter o Conteúdo Mestre a ser copiado.");
			for (SalaAulaBlackboardGrupoVO sabGrupo : getListaSalaAulaBlackboardGrupoVO()) {
				for (SalaAulaBlackboardVO sab : sabGrupo.getListaGrupoSalaAulaBlackboardVO()) {
					sab.setIdSalaAulaBlackboardConteudoMaster(sabGrupo.getIdSalaAulaBlackboardConteudoMaster());
					Uteis.checkState(!Uteis.isAtributoPreenchido(sab.getIdSalaAulaBlackboardConteudoMaster()), "o campo Sala do Conteúdo Mestre deve ser informado.");
					getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirCopiaConteudoSalaBlack(sab, SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD_COPIA_CONTEUDO,getUsuarioLogadoClone());	
				}	
			}
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setOncomplete("RichFaces.$('panelConfirmarCopiaConteudoLote').hide()");
			realizarAtualizacaoStatusSalasBlackboard("panelCopiarConteudoBlackboard");
			setMensagemID(MSG_TELA.msg_dados_gravados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	

	public void excluirSalaAulaBlackboard() {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarExclusaoSalaAulaBlackboard(getSalaAulaBlackboardSelecionada(), getUsuarioLogadoClone());
			setDadosConsultaSalaAula( new DataModelo() );
			getDadosConsultaSalaAula().setLimitePorPagina(10);
			getDadosConsultaSalaAula().setPaginaAtual(1);
			getDadosConsultaSalaAula().setPage(1);
			consultarSalasBlackboard();
			setSalaAulaBlackboardSelecionada(new SalaAulaBlackboardVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void criarAtualizarSalaAulaBlackboard() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap().get(Constantes.salaAulaItem);
			obj = getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoDadosSeiComDadosBlackboard(obj.getCodigo(), getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_atualizados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarPessoa() {
		try {
			setListaConsulta(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(),Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarPessoa() {
		try {
			PessoaEmailInstitucionalVO obj = (PessoaEmailInstitucionalVO) context().getExternalContext().getRequestMap().get(Constantes.pessoaItem);
			if(getTipoAdicionarFacilitador().equals(Constantes.ALUNO)) {
				MatriculaPeriodoTurmaDisciplinaVO mptd = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaVOPorTipoSalaAulaBlackboardEnumPorNomeAlunoPorCursoPorTurmaPorDisciplinaPorAnoPorSemestre(getSalaAulaBlackboardSelecionada().getTipoSalaAulaBlackboardEnum(),obj.getNome(), getSalaAulaBlackboardSelecionada().getCursoVO().getCodigo(), 0, getSalaAulaBlackboardSelecionada().getDisciplinaVO().getCodigo(), getSalaAulaBlackboardSelecionada().getAno(), getSalaAulaBlackboardSelecionada().getSemestre() ,getUsuarioLogadoClone());
				Uteis.checkState(!Uteis.isAtributoPreenchido(mptd.getMatricula()), "Não foi localizado nenhuma matrícula para o aluno - "+obj.getNome() +" com os dados informados pela Sala de Aula Escolhida.");
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirPessoaSalaBlack(getSalaAulaBlackboardSelecionada(),
						obj.getPessoaVO().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.valueOf(getTipoAdicionarFacilitador()),
						mptd.getMatricula(), mptd.getCodigo(), obj.getEmail(), getUsuarioLogadoClone());	
			}else {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirPessoaSalaBlack(null,
						obj.getPessoaVO().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.valueOf(getTipoAdicionarFacilitador()),
						null, null, getSalaAulaBlackboardSelecionada().getIdSalaAulaBlackboard(), null, getUsuarioLogadoClone());	
			}
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			realizarValidacaoProcessamentoEmAndamento();
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarAlunoAptoExclusao() {
		SalaAulaBlackboardPessoaVO obj = (SalaAulaBlackboardPessoaVO) context().getExternalContext().getRequestMap().get(Constantes.alunoItem);
		setFacilitadorSelecionado(obj);
	}
	
	public void selecionarFacilitador() {
		SalaAulaBlackboardPessoaVO obj = (SalaAulaBlackboardPessoaVO) context().getExternalContext().getRequestMap().get(Constantes.facilitadorItem);
		setFacilitadorSelecionado(obj);
	}

	public void excluirFacilitador() {
		try {
			if(getFacilitadorSelecionado().getTipoSalaAulaBlackboardPessoaEnum().isAluno()) {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(getFacilitadorSelecionado().getSalaAulaBlackboardVO().getCodigo(),
						getFacilitadorSelecionado().getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(),
						getFacilitadorSelecionado().getTipoSalaAulaBlackboardPessoaEnum(),
						getFacilitadorSelecionado().getPessoaEmailInstitucionalVO().getEmail(),
						getUsuarioLogadoClone());
			}else {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(null,
						getFacilitadorSelecionado().getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(),
						getFacilitadorSelecionado().getTipoSalaAulaBlackboardPessoaEnum(),
						getFacilitadorSelecionado().getSalaAulaBlackboardVO().getIdSalaAulaBlackboard(),
						getFacilitadorSelecionado().getPessoaEmailInstitucionalVO().getEmail(),
						getUsuarioLogadoClone());	
			}
			
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			realizarValidacaoProcessamentoEmAndamento();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarSugestaoFacilitadores() {
		try {
			setListaSugestaoFacilitadores(getFacadeFactory().getSalaAulaBlackboardFacade()
					.consultarSugestaoFacilitadores(getAno(), getSemestre(), getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void sugerirFacilitadores() {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().sugerirFacilitadores(getListaSugestaoFacilitadores(),
					getQuantidadeFacilitadoresPorSala(), getAno(), getSemestre(), getUsuarioLogadoClone());
			realizarValidacaoProcessamentoEmAndamento();
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarGeracaoExcelSalas() {
		try {
			File arquivo = getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoExcelSalas(
					getUnidadeEnsinoVOs(), getTipoSalaAulaBlackboardEnum(), getAno(), getSemestre(), getCurso().getCodigo(), 0,
					getDisciplina().getCodigo(), getMatricula().getMatricula(), getFiltroRelatorioAcademicoVO(), getExportarSala(),
					getExportarAlunos(), getExportarNotas(), getExportarProfessores(), getExportarFacilitadores(),
					getExportarSupervisores(), getQtdLinhasExportacaoPorSalaAulaBlackboard(), getUsuarioLogadoClone());

			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);

			setMensagemID(MSG_TELA.msg_relatorio_ok.name());
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	public void realizarAtualizacaoCalendarioAgrupamentoTCC(ClassificacaoDisciplinaEnum classificacaoDisciplinaEnum) {
		try {
			if(Uteis.isAtributoPreenchido(getAno()) && Uteis.isAtributoPreenchido(getSemestre())) {
				setCalendarioAgrupamentoTccVO(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorClassificacaoPorAnoPorSemestre(classificacaoDisciplinaEnum, getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));	
				consultarListaSelectItemCalendarioAgrupamento(classificacaoDisciplinaEnum);
				montarDisciplinasPorClassificacao(classificacaoDisciplinaEnum);
			}else {
				getListaSelectItemCalendarioAgrupamento().clear();
				setCalendarioAgrupamentoTccVO(new CalendarioAgrupamentoTccVO());
			}
			setTotalizadoSalaAulaBlackboardGrupoVO(new SalaAulaBlackboardGrupoVO());
			getListaSalaAulaBlackboardGrupoVO().clear();
			getListaSalaAulaBlackboardGrupoVOFechamento().clear();			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	

	public void realizarVisualizacaoSalaTcc() {
		try {
			Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()), "O campo ANO deve ser informado.");
			Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()), "O campo SEMESTRE deve ser informado.");
			if(getCalendarioAgrupamentoTccVO().getClassificacaoAgrupamento().isProjetoIntegrador()) {
				setCalendarioAgrupamentoTccVO(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorClassificacaoPorAnoPorSemestre(ClassificacaoDisciplinaEnum.TCC, getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
			}
			setTotalizadoSalaAulaBlackboardGrupoVO(new SalaAulaBlackboardGrupoVO());
			getListaSalaAulaBlackboardGrupoVO().clear();
			getListaSalaAulaBlackboardGrupoVOFechamento().clear();
			consultarListaSelectItemCalendarioAgrupamento(ClassificacaoDisciplinaEnum.TCC);
			montarDisciplinasPorClassificacao(ClassificacaoDisciplinaEnum.TCC);
			limparMensagem();
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setSuperControle(this);
			getProgressBarVO().setAssincrono(false);
			getProgressBarVO().setOncomplete("RichFaces.$('panelGrupoTcc').show()");
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarEnsalamentoPendenteProcessamento(getProgressBarVO());
			if((getProgressBarVO().getForcarEncerramento() || !getProgressBarVO().getAtivado())) {
				setOncompleteModal("RichFaces.$('panelGrupoTcc').show()");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarMontagemSalaTcc() {
		try {
			Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()), "O campo ANO deve ser informado.");
			Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()), "O campo SEMESTRE deve ser informado.");
			setListaSalaAulaBlackboardGrupoVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaMontagemSalaTccGrupo(getCalendarioAgrupamentoTccVO(), getDisciplina(), getAno(), getSemestre(), getNivelEducacionalApresentar(), getUsuarioLogadoClone()));			
			getTotalizadoSalaAulaBlackboardGrupoVO().setQtdAlunos(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getQtdAlunos()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setQtdAlunosEnsalados(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getQtdAlunosEnsalados()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setQtdAlunosNaoEnsalados(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getQtdAlunosNaoEnsalados()).reduce(0, Integer::sum));			
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardNecessario(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardNecessario()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardGrupoNecessario(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardGrupoNecessario()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardExistentes(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardExistentes()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardGrupoExistentes(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardGrupoExistentes()).reduce(0, Integer::sum));			
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardNovo(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardNovo()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardGrupoNovo(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardGrupoNovo()).reduce(0, Integer::sum));
			getListaSalaAulaBlackboardGrupoVOFechamento().clear();
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarGeracaoGrupoSalaAulaBlackboard() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(!Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
				throw new ConsistirException("Não foi encontrado uma CONFIGURAÇÃO BLACKBOARD parametrizada.");
			}
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarGeracaoListaGrupoSalaAulaBlackboardVO(getListaSalaAulaBlackboardGrupoVO(), ClassificacaoDisciplinaEnum.TCC, getAno(), getSemestre(), getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			setProgressBarVO(new ProgressBarVO());
			limparMensagem();
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setOncomplete("RichFaces.$('panelGrupoTcc').show()");
			realizarAtualizacaoStatusSalasBlackboard("panelGrupoTcc");
			setOncompleteModal("RichFaces.$('panelGrupoTcc').hide()");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void visualizarTelaFechamentoGrupoSalaAulaBlackboard() {
		try {
			setTipoFechamentoClassificacaoDisciplinaEnum(ClassificacaoDisciplinaEnum.TCC);
			getFacadeFactory().getSalaAulaBlackboardFacade().visualizarTelaFechamentoGrupoSalaAulaBlackboard(getCalendarioAgrupamentoTccVO(), getListaSalaAulaBlackboardGrupoVOFechamento(), getTipoFechamentoClassificacaoDisciplinaEnum(), getDisciplina(), getAno(), getSemestre(), getUsuarioLogadoClone());			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarMontagemGrupoDisponivelParaTrocarManual() {
		try {
			getListaSalaAulaBlackboardGrupoDisponivel().clear();
			setAlunoSalaAulaBlackboardSelecionado((SalaAulaBlackboardPessoaVO) context().getExternalContext().getRequestMap().get(Constantes.alunoItem));
			getListaSalaAulaBlackboardGrupoVOFechamento().stream()
			.flatMap(sala-> sala.getListaGrupoSalaAulaBlackboardVO().stream())
			.filter(grupo-> !grupo.getIdGrupo().equals(getAlunoSalaAulaBlackboardSelecionado().getSalaAulaBlackboardVO().getIdGrupo()) && grupo.getQtdeAlunos() < grupo.getDisciplinaVO().getNrMaximoAulosPorGrupo())
			.forEach(grupo -> getListaSalaAulaBlackboardGrupoDisponivel().add(new SelectItem(grupo.getIdGrupo(), grupo.getNomeGrupo())));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarTrocaManualGrupoDisponivel() {
		try {
			getListaSalaAulaBlackboardGrupoVOFechamento().stream()
			.filter(sala-> sala.getListaGrupoSalaAulaBlackboardVO().stream().anyMatch(grupo-> grupo.getIdGrupo().equals(getAlunoSalaAulaBlackboardSelecionado().getSalaAulaBlackboardVO().getIdGrupo())))
			.forEach(sala->{
				sala.getListaGrupoSalaAulaBlackboardVO().stream()
				.filter(grupo-> grupo.getIdGrupo().equals(getAlunoSalaAulaBlackboardSelecionado().getSalaAulaBlackboardVO().getIdGrupo()))
				.forEach(grupo -> {
					for (Iterator<SalaAulaBlackboardPessoaVO> iterator = grupo.getListaAlunos().iterator(); iterator.hasNext();) {
						SalaAulaBlackboardPessoaVO sabp =  iterator.next();
						if(sabp.getMatricula().equals(getAlunoSalaAulaBlackboardSelecionado().getMatricula())) {
							if(!Uteis.isAtributoPreenchido(sabp.getCodigoGrupoOrigem())) {
								sabp.setCodigoGrupoOrigem(getAlunoSalaAulaBlackboardSelecionado().getSalaAulaBlackboardVO().getCodigo());
								sabp.setNomeGrupoOrigem(getAlunoSalaAulaBlackboardSelecionado().getSalaAulaBlackboardVO().getNomeGrupo());
							}
							grupo.setQtdeAlunos(grupo.getQtdeAlunos() - 1);
							iterator.remove();	
							break;
						}
					}
				});
				sala.setQtdAlunosEnsalados(sala.getQtdAlunosEnsalados() - 1);
				sala.setVagasSalaAulaBlackboard(sala.getVagasSalaAulaBlackboard() + 1);
			});
			
			getListaSalaAulaBlackboardGrupoVOFechamento().stream()
			.filter(sala-> sala.getListaGrupoSalaAulaBlackboardVO().stream().anyMatch(grupo-> grupo.getIdGrupo().equals(getIdSalaAulaBlackboardGrupoDisponivel())))
			.forEach(sala->{
				sala.getListaGrupoSalaAulaBlackboardVO().stream()
				.filter(grupo-> grupo.getIdGrupo().equals(getIdSalaAulaBlackboardGrupoDisponivel()))
				.forEach(grupo -> {
					if(Uteis.isAtributoPreenchido(getAlunoSalaAulaBlackboardSelecionado().getCodigoGrupoOrigem()) && getAlunoSalaAulaBlackboardSelecionado().getCodigoGrupoOrigem().equals(grupo.getCodigo())) {
						getAlunoSalaAulaBlackboardSelecionado().setCodigoGrupoOrigem(0);
						getAlunoSalaAulaBlackboardSelecionado().setNomeGrupoOrigem(Constantes.EMPTY);
					}
					getAlunoSalaAulaBlackboardSelecionado().setSalaAulaBlackboardVO(grupo);
					grupo.getListaAlunos().add(getAlunoSalaAulaBlackboardSelecionado());
					grupo.setQtdeAlunos(grupo.getQtdeAlunos() + 1);
					Ordenacao.ordenarListaDecrescente(grupo.getListaAlunos(), Constantes.nomeGrupoOrigem);
				});
				sala.setQtdAlunosEnsalados(sala.getQtdAlunosEnsalados() + 1);
				sala.setVagasSalaAulaBlackboard(sala.getVagasSalaAulaBlackboard() - 1);
			});
			setAlunoSalaAulaBlackboardSelecionado(new SalaAulaBlackboardPessoaVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void persistirFechamentoGrupoSalaAulaBlackboard() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(!Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
				throw new ConsistirException("Não foi encontrado uma CONFIGURAÇÃO BLACKBOARD parametrizada.");
			}
			if(getTipoFechamentoClassificacaoDisciplinaEnum().isTcc()) {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarFechamentoGrupoSalaAulaBlackboardPorTCC(getListaSalaAulaBlackboardGrupoVOFechamento(), getUsuarioLogadoClone());	
			}else {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarFechamentoGrupoSalaAulaBlackboardPorProjetoIntegrador(getListaSalaAulaBlackboardGrupoVOFechamento(), getUsuarioLogadoClone());	
			}
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			setProgressBarVO(new ProgressBarVO());
			limparMensagem();
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			if(getTipoFechamentoClassificacaoDisciplinaEnum().isTcc()) {
				getProgressBarVO().setOncomplete("RichFaces.$('panelGrupoTcc').show()");
				realizarAtualizacaoStatusSalasBlackboard("panelGrupoTcc");
				setOncompleteModal("RichFaces.$('panelGrupoTccFechamento').hide();RichFaces.$('panelGrupoTcc').hide();");	
			}else {
				getProgressBarVO().setOncomplete("RichFaces.$('panelGrupoProjetoIntegrador').show()");
				realizarAtualizacaoStatusSalasBlackboard("panelGrupoProjetoIntegrador");
				setOncompleteModal("RichFaces.$('panelSalaAulaGrupoFechamento').hide();RichFaces.$('panelGrupoProjetoIntegrador').hide();");	
			}
			setTipoFechamentoClassificacaoDisciplinaEnum(ClassificacaoDisciplinaEnum.NENHUMA);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void persistirFechamentoGrupoSalaAulaBlackboardIndividual() {
		try {
			SalaAulaBlackboardGrupoVO sabGrupo = (SalaAulaBlackboardGrupoVO) context().getExternalContext().getRequestMap().get(Constantes.sabGrupoFechamentoItem);
			setOncompleteModal(Constantes.EMPTY);			
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(!Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
				throw new ConsistirException("Não foi encontrado uma CONFIGURAÇÃO BLACKBOARD parametrizada.");
			}
			Map<String, List<MatriculaPeriodoTurmaDisciplinaVO>> mapMptd = new HashMap<>();
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().executarFechamentoGrupoSalaAulaBlackboardIndividual(sabGrupo, mapMptd, getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			setProgressBarVO(new ProgressBarVO());
			limparMensagem();
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			if(getTipoFechamentoClassificacaoDisciplinaEnum().isTcc()) {
				getProgressBarVO().setOncomplete("RichFaces.$('panelGrupoTcc').show()");
				realizarAtualizacaoStatusSalasBlackboard("panelGrupoTcc");
				setOncompleteModal("RichFaces.$('panelGrupoTccFechamento').hide();RichFaces.$('panelGrupoTcc').hide();");	
			}else {
				getProgressBarVO().setOncomplete("RichFaces.$('panelGrupoProjetoIntegrador').show()");
				realizarAtualizacaoStatusSalasBlackboard("panelGrupoProjetoIntegrador");
				setOncompleteModal("RichFaces.$('panelSalaAulaGrupoFechamento').hide();RichFaces.$('panelGrupoProjetoIntegrador').hide();");	
			}
			setTipoFechamentoClassificacaoDisciplinaEnum(ClassificacaoDisciplinaEnum.NENHUMA);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarDisciplinasPorClassificacao(ClassificacaoDisciplinaEnum classificacaoDisciplinaEnum)  throws Exception {
		getDisciplina().setCodigo(0);
		List<DisciplinaVO> lista = new ArrayList<>();
		if(Uteis.isAtributoPreenchido(getCalendarioAgrupamentoTccVO()) 
				&& getCalendarioAgrupamentoTccVO().getClassificacaoAgrupamento().equals(classificacaoDisciplinaEnum)
				&& !getCalendarioAgrupamentoTccVO().getCalendarioAgrupamentoDisciplinaVOs().isEmpty()) {
			
			getListaDisciplinaClassificada().clear();
			getListaDisciplinaClassificada().add(new SelectItem(0, Constantes.EMPTY));
			getCalendarioAgrupamentoTccVO().getCalendarioAgrupamentoDisciplinaVOs().stream().forEach(p -> getListaDisciplinaClassificada().add(new SelectItem(p.getDisciplinaVO().getCodigo(), p.getDisciplinaVO().getDescricaoAbreviaturaNome())));
			if(!getCalendarioAgrupamentoTccVO().getCalendarioAgrupamentoDisciplinaVOs().isEmpty() && getCalendarioAgrupamentoTccVO().getCalendarioAgrupamentoDisciplinaVOs().size() == 1) {
				getDisciplina().setCodigo( getCalendarioAgrupamentoTccVO().getCalendarioAgrupamentoDisciplinaVOs().get(0).getDisciplinaVO().getCodigo());
			}
		}else {
			if(classificacaoDisciplinaEnum.isTcc()) {			
				lista = getFacadeFactory().getDisciplinaFacade().consultarPorGradeCurricularDisciplinaPadraoTcc(false, Uteis.NIVELMONTARDADOS_COMBOBOX,getUsuarioLogadoClone());
			}else if(classificacaoDisciplinaEnum.isProjetoIntegrador()) {
				lista = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorClassificacaoDisciplinaEnum(classificacaoDisciplinaEnum, false, Uteis.NIVELMONTARDADOS_COMBOBOX,getUsuarioLogadoClone());
			}
			getListaDisciplinaClassificada().clear();
			getListaDisciplinaClassificada().add(new SelectItem(0, Constantes.EMPTY));
			lista.stream().forEach(p -> getListaDisciplinaClassificada()
				.add(new SelectItem(p.getCodigo(), p.getAbreviatura() + Constantes.CARACTER_TRACO + p.getNome())));
		}
	}
	
	public void realizarVisualizacaoSalaProjetoIntegrador() {
		try {
			Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()), "O campo ANO deve ser informado.");
			Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()), "O campo SEMESTRE deve ser informado.");
			if(getCalendarioAgrupamentoTccVO().getClassificacaoAgrupamento().isTcc()) {
				setCalendarioAgrupamentoTccVO(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorClassificacaoPorAnoPorSemestre(ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR, getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
			}
			setTotalizadoSalaAulaBlackboardGrupoVO(new SalaAulaBlackboardGrupoVO());
			getListaSalaAulaBlackboardGrupoVO().clear();
			getListaSalaAulaBlackboardGrupoVOFechamento().clear();
			consultarListaSelectItemCalendarioAgrupamento(ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR);
			montarDisciplinasPorClassificacao(ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR);
			limparMensagem();
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setSuperControle(this);
			getProgressBarVO().setAssincrono(false);
			getProgressBarVO().setOncomplete("RichFaces.$('panelGrupoProjetoIntegrador').show()");
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarEnsalamentoPendenteProcessamento(getProgressBarVO());
			if((getProgressBarVO().getForcarEncerramento() || !getProgressBarVO().getAtivado())) {
				setOncompleteModal("RichFaces.$('panelGrupoProjetoIntegrador').show()");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarMontagemSalaProjetoIntegrador() {
		try {
			Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()), "O campo ANO deve ser informado.");
			Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()), "O campo SEMESTRE deve ser informado.");
			setListaSalaAulaBlackboardGrupoVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaMontagemSalaProjetoIntegrador(getCalendarioAgrupamentoTccVO(), getDisciplina().getCodigo(), getAno(), getSemestre(), 0, getNivelEducacionalApresentar()));
			getTotalizadoSalaAulaBlackboardGrupoVO().setQtdAlunos(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getQtdAlunos()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setQtdAlunosEnsalados(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getQtdAlunosEnsalados()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setQtdAlunosNaoEnsalados(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getQtdAlunosNaoEnsalados()).reduce(0, Integer::sum));			
			
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardNecessario(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardNecessario()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardGrupoNecessario(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardGrupoNecessario()).reduce(0, Integer::sum));
			
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardExistentes(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardExistentes()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardGrupoExistentes(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardGrupoExistentes()).reduce(0, Integer::sum));			
			
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardNovo(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardNovo()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardGrupoVO().setVagasSalaAulaBlackboardGrupoNovo(getListaSalaAulaBlackboardGrupoVO().stream().map(t -> t.getVagasSalaAulaBlackboardGrupoNovo()).reduce(0, Integer::sum));			
			
			getListaSalaAulaBlackboardGrupoVOFechamento().clear();
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarGeracaoListaGrupoSalaAulaBlackboardPI() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(!Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
				throw new ConsistirException("Não foi encontrado uma CONFIGURAÇÃO BLACKBOARD parametrizada.");
			}
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarGeracaoListaGrupoSalaAulaBlackboardVO(getListaSalaAulaBlackboardGrupoVO(), ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR, getAno(), getSemestre(), getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			setProgressBarVO(new ProgressBarVO());
			limparMensagem();
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setOncomplete("RichFaces.$('panelGrupoProjetoIntegrador').show()");
			realizarAtualizacaoStatusSalasBlackboard("panelGrupoProjetoIntegrador");
			setOncompleteModal("RichFaces.$('panelGrupoProjetoIntegrador').hide()");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarGeracaoGrupoSalaAulaBlackboardPI() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(!Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
				throw new ConsistirException("Não foi encontrado uma CONFIGURAÇÃO BLACKBOARD parametrizada.");
			}
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarSeExisteProcessamentoPendente();
			SalaAulaBlackboardGrupoVO sabGrupo = (SalaAulaBlackboardGrupoVO) context().getExternalContext().getRequestMap().get(Constantes.sabGrupoItem);
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarGeracaoGrupoSalaAulaBlackboardVO(sabGrupo, ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR, getAno(), getSemestre(), getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			setProgressBarVO(new ProgressBarVO());
			limparMensagem();
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setOncomplete("RichFaces.$('panelGrupoProjetoIntegrador').show()");
			realizarAtualizacaoStatusSalasBlackboard("panelGrupoProjetoIntegrador");
			setOncompleteModal("RichFaces.$('panelGrupoProjetoIntegrador').hide()");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void visualizarTelaFechamentoGrupoSalaAulaBlackboardPI() {
		try {			
			setTipoFechamentoClassificacaoDisciplinaEnum(ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR);
			getFacadeFactory().getSalaAulaBlackboardFacade().visualizarTelaFechamentoGrupoSalaAulaBlackboard(getCalendarioAgrupamentoTccVO(), getListaSalaAulaBlackboardGrupoVOFechamento(), getTipoFechamentoClassificacaoDisciplinaEnum(), getDisciplina(), getAno(), getSemestre(), getUsuarioLogadoClone());			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void consultarAlunosTccPorSalaAulaBlackboardGrupo(String acao) {
		try {
			SalaAulaBlackboardGrupoVO sabGrupo = (SalaAulaBlackboardGrupoVO) context().getExternalContext().getRequestMap().get(Constantes.sabGrupoItem);
			setSalaAulaBlackboardSelecionada(new SalaAulaBlackboardVO());
			getSalaAulaBlackboardSelecionada().setDisciplinaVO(sabGrupo.getDisciplinaVO());
			getSalaAulaBlackboardSelecionada().setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getSalaAulaBlackboardFacade().consultarAlunosTccPorSalaAulaBlackboardGrupo(sabGrupo, acao));
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarAlunosProjetoIntegradorPorSalaAulaBlackboardGrupo(String acao) {
		try {
			SalaAulaBlackboardGrupoVO sabGrupo = (SalaAulaBlackboardGrupoVO) context().getExternalContext().getRequestMap().get(Constantes.sabGrupoItem);
			setSalaAulaBlackboardSelecionada(new SalaAulaBlackboardVO());
			getSalaAulaBlackboardSelecionada().setDisciplinaVO(sabGrupo.getDisciplinaVO());
			getSalaAulaBlackboardSelecionada().setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getSalaAulaBlackboardFacade().consultarAlunosProjetoIntegradorPorSalaAulaBlackboardGrupo(sabGrupo, acao));
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarGeracaoSalaEstagio() {
		try {
			setListaGeracaoSalaAulaBlackboard(new ArrayList<>());
			setListaMatriculaPeriodoVO(new ArrayList<>());
			setCurso(new CursoVO());
			inicializarVersaoSistema();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	
	public void realizarMontagemSalaEstagio() {
		try {
			Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()), "O campo ANO deve ser informado.");
			Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()), "O campo SEMESTRE deve ser informado.");
			setListaGeracaoSalaAulaBlackboard(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaMontagemSalaEstagio(getCurso(), getAno(), getSemestre(), getNivelEducacionalApresentar(), getUsuarioLogadoClone()));
			setListaMatriculaPeriodoVO(new ArrayList<>());
			inicializarVersaoSistema();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	
	public void realizarEnsalamentoSalaEstagioSelecionado() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap().get(Constantes.sabItem);
			List<Integer> listaCurso= new ArrayList<>();
			listaCurso.add(obj.getCursoVO().getCodigo());
			realizarInicializacaoEnsalamentoSalaEstagioProgressBar(listaCurso);
			setOncompleteModal("RichFaces.$('panelGeracaoSalaEstagio').hide();");
		} catch (Exception e) {
			setOncompleteModal(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarEnsalamentoSalaEstagio() {
		try {
			List<Integer> listaCurso= new ArrayList<>();
			getListaGeracaoSalaAulaBlackboard().forEach(p-> listaCurso.add(p.getCursoVO().getCodigo()));
			realizarInicializacaoEnsalamentoSalaEstagioProgressBar(listaCurso);
			setOncompleteModal("RichFaces.$('panelGeracaoSalaEstagio').hide();");
		} catch (Exception e) {
			setOncompleteModal(Constantes.EMPTY);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarInicializacaoEnsalamentoSalaEstagioProgressBar(List<Integer> listaCurso) throws Exception {
		Uteis.checkState(listaCurso.size() == 0, "Não foi encontrado nenhum Aluno para realizar o ensalamento do estágio.");
		getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarSeExisteProcessamentoPendente();
		setListaMatriculaPeriodoVO(getFacadeFactory().getSalaAulaBlackboardFacade().realizarConsultaDeVerificacaoSeMatriculaAptaSalaAulaBlackboardEstagio(listaCurso, getAno(), getSemestre(), getUsuarioLogadoClone()));
		setProgressBarVO(new ProgressBarVO());
		Integer qtdContasSelecionadas = getListaMatriculaPeriodoVO().size();
		Uteis.checkState(qtdContasSelecionadas == 0, "Não foi encontrado nenhum Aluno para realizar o ensalamento do estágio.");
		getProgressBarVO().resetar();
		getProgressBarVO().setAplicacaoControle(getAplicacaoControle());
		getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
		getProgressBarVO().iniciar(0l, (qtdContasSelecionadas.intValue()), "Iniciando Processamento da(s) operações.", true, this, "realizarEnsalamentoPorGestaoSalaNotaBlackboard");
	}
	
	public void realizarEnsalamentoPorGestaoSalaNotaBlackboard() {
		ConfiguracaoEstagioObrigatorioVO configEstagio = getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadrao(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());
		try {
			ConsistirException consistirException = new ConsistirException();
			for (MatriculaPeriodoVO matriculaPeriodoVO : getListaMatriculaPeriodoVO()) {
				try {
					getProgressBarVO().setStatus(getProgressBarVO().getPreencherStatusProgressBarVO(getProgressBarVO(), "Ensalamento Estágio", matriculaPeriodoVO.getMatriculaVO().getMatricula()));
					getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSalaAulaBlackboardPorModuloEstagio(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, configEstagio, getProgressBarVO().getUsuarioVO());
					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioLiberado(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, getProgressBarVO().getUsuarioVO());
				} catch (Exception e) {
					consistirException.adicionarListaMensagemErro("Log Ensalamento Estágio:"+matriculaPeriodoVO.getMatriculaVO().getMatricula()+" descrição -"+ e.getMessage());
				} finally {
					getProgressBarVO().incrementar();
				}
			}
			if (consistirException.existeErroListaMensagemErro()) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), Uteis.ERRO);
				setListaMensagemErro(consistirException.getListaMensagemErro());
				setOncompleteModal("RichFaces.$('panelOperacoesEmLoteLogs').show();");
			} else {
				realizarMontagemSalaEstagio();
				setOncompleteModal("RichFaces.$('panelGeracaoSalaEstagio').show();");
				setMensagemID(MSG_TELA.msg_dados_operacao.name());
			}
		} catch (Exception e) {
			setOncompleteModal("RichFaces.$('panelOperacoesEmLoteLogs').show();");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarVisualizacaoApuracaoNotasAva() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			limparMensagem();
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setSuperControle(this);
			getProgressBarVO().setAssincrono(false);
			getProgressBarVO().setOncomplete("RichFaces.$('panelConfirmarApuracao').show()");
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarEnsalamentoPendenteProcessamento(getProgressBarVO());
			if((getProgressBarVO().getForcarEncerramento() || !getProgressBarVO().getAtivado())) {
				setOncompleteModal("RichFaces.$('panelConfirmarApuracao').show()");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void apurarNotas() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			
			Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()), "O campo ANO deve ser informado.");
			Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()), "O campo SEMESTRE deve ser informado.");
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(!Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
				throw new ConsistirException("Não foi encontrado uma CONFIGURAÇÃO BLACKBOARD parametrizada.");
			}
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarOperacaoDeApuracaoNotasAva(getIdSalaAulaBlackboard(), getUnidadeEnsinoVOs(), getTipoSalaAulaBlackboardEnum(), getAno(), getSemestre(),
					getBimestreGeracaoSala(), getCurso().getCodigo(), null, getDisciplina().getCodigo(), getMatricula().getMatricula(),
					getFiltroRelatorioAcademicoVO(), isRealizarCalculoMediaApuracaoNotas(), isRealizarBuscarNotaBlackboard(), getUsuarioLogadoClone(), getNivelEducacionalApresentar());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			limparMensagem();
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			getProgressBarVO().setOncomplete("RichFaces.$('panelConfirmarApuracao').show()");
			realizarAtualizacaoStatusSalasBlackboard("panelConfirmarApuracao");
			setOncompleteModal("RichFaces.$('panelConfirmarApuracao').hide()");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void visualizarHistoricoNotaBlackboard() {
		try {
			setHistoricoNotaBlackboardSelecionado(new HistoricoNotaBlackboardVO());
			setControleConsultaHistoricoNotaBlackboard(new DataModelo(20, 0, getUsuarioLogadoClone()));
			getControleConsultaHistoricoNotaBlackboard().setPage(1);
			getControleConsultaHistoricoNotaBlackboard().setPaginaAtual(1);
			consultarHistoricoNotaBlackboard();
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarHistoricoNotaBlackboardPorFiltro() {
		try {
			getControleConsultaHistoricoNotaBlackboard().setPage(1);
			getControleConsultaHistoricoNotaBlackboard().setPaginaAtual(1);
			consultarHistoricoNotaBlackboard();
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarHistoricoNotaBlackboard() {
		try {
			getFacadeFactory().getHistoricoNotaBlackboardFacade().consultar(getControleConsultaHistoricoNotaBlackboard(), getHistoricoNotaBlackboardSelecionado(), true, getUsuarioLogadoClone());
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void scrollerListenerHistoricoNotaBlackboard(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaHistoricoNotaBlackboard().setPage(dataScrollerEvent.getPage());
		getControleConsultaHistoricoNotaBlackboard().setPaginaAtual(dataScrollerEvent.getPage());
		consultarHistoricoNotaBlackboard();
	}
	
	public void selecionarSalaAulaBlackboardHistoricoNotaNaoLocalizada() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap().get(Constantes.salaAulaItem);
			setHistoricoNotaBlackboardSelecionado(new HistoricoNotaBlackboardVO());
			getHistoricoNotaBlackboardSelecionado().getSalaAulaBlackboardVO().setCodigo(obj.getCodigo());
			setControleConsultaHistoricoNotaBlackboard(new DataModelo(20, 0, getUsuarioLogadoClone()));
			getControleConsultaHistoricoNotaBlackboard().setPaginaAtual(1);
			consultarHistoricoNotaBlackboard();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarAlunoSalaAulaBlackboard() {
		SalaAulaBlackboardPessoaVO obj = (SalaAulaBlackboardPessoaVO) context().getExternalContext().getRequestMap().get(Constantes.alunoItem);
		setAlunoSalaAulaBlackboardSelecionado(obj);
	}

	public void deferirApuracaoNota() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getHistoricoNotaBlackboardSelecionado().getMotivoDeferimentoIndeferimento()), "O campo MOTIVO deve ser informado.");
			getHistoricoNotaBlackboardSelecionado().setUsuarioResponsavel(getUsuarioLogadoClone());
			setHistoricoNotaBlackboardSelecionado(new HistoricoNotaBlackboardVO());
			consultarSalasBlackboard();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void indeferirApuracaoNota() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getHistoricoNotaBlackboardSelecionado().getMotivoDeferimentoIndeferimento()), "O campo MOTIVO deve ser informado.");
			getFacadeFactory().getHistoricoNotaBlackboardFacade().deferirIndeferirNota(getHistoricoNotaBlackboardSelecionado(), SituacaoHistoricoNotaBlackboardEnum.INDEFERIDO, isRealizarCalculoMediaApuracaoNotas(), getUsuarioLogadoClone());
			setHistoricoNotaBlackboardSelecionado(new HistoricoNotaBlackboardVO());
			consultarSalasBlackboard();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void abrirModalDeferimentoIndeferimento() {
		try {
			SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO = (SalaAulaBlackboardPessoaVO) context().getExternalContext().getRequestMap().get(Constantes.alunoItem);
			setHistoricoNotaBlackboardSelecionado(getFacadeFactory().getHistoricoNotaBlackboardFacade().consultarPorCodigo(salaAulaBlackboardPessoaVO.getHistoricoNotaBlackboardVO().getCodigo()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparImportacaoPlanilha() {
		setNomeArquivoUploadSalaAulaBlackboard(Constantes.EMPTY);
		getListaSalaAulaBlackboardPessoaImportar().clear();
	}	

	public List<SalaAulaBlackboardGrupoVO> getListaSalaAulaBlackboardGrupoVO() {
		if (listaSalaAulaBlackboardGrupoVO == null) {
			listaSalaAulaBlackboardGrupoVO = new ArrayList<>();
		}
		return listaSalaAulaBlackboardGrupoVO;
	}

	public void setListaSalaAulaBlackboardGrupoVO(List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVO) {
		this.listaSalaAulaBlackboardGrupoVO = listaSalaAulaBlackboardGrupoVO;
	}
	
	

	public List<SalaAulaBlackboardGrupoVO> getListaSalaAulaBlackboardGrupoVOFechamento() {
		if (listaSalaAulaBlackboardGrupoVOFechamento == null) {
			listaSalaAulaBlackboardGrupoVOFechamento = new ArrayList<>();
		}
		return listaSalaAulaBlackboardGrupoVOFechamento;
	}

	public void setListaSalaAulaBlackboardGrupoVOFechamento(List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVOFechamento) {
		this.listaSalaAulaBlackboardGrupoVOFechamento = listaSalaAulaBlackboardGrupoVOFechamento;
	}
	
	
	
	public ClassificacaoDisciplinaEnum getTipoFechamentoClassificacaoDisciplinaEnum() {
		if(tipoFechamentoClassificacaoDisciplinaEnum == null) {
			tipoFechamentoClassificacaoDisciplinaEnum = ClassificacaoDisciplinaEnum.NENHUMA;
		}
		return tipoFechamentoClassificacaoDisciplinaEnum;
	}

	public void setTipoFechamentoClassificacaoDisciplinaEnum(ClassificacaoDisciplinaEnum tipoFechamentoClassificacaoDisciplinaEnum) {
		this.tipoFechamentoClassificacaoDisciplinaEnum = tipoFechamentoClassificacaoDisciplinaEnum;
	}

	public CalendarioAgrupamentoTccVO getCalendarioAgrupamentoTccVO() {
		if (calendarioAgrupamentoTccVO == null) {
			calendarioAgrupamentoTccVO = new CalendarioAgrupamentoTccVO();
		}
		return calendarioAgrupamentoTccVO;
	}

	public void setCalendarioAgrupamentoTccVO(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO) {
		this.calendarioAgrupamentoTccVO = calendarioAgrupamentoTccVO;
	}

	public List<SelectItem> getListaSalaAulaBlackboardGrupoDisponivel() {
		if (listaSalaAulaBlackboardGrupoDisponivel == null) {
			listaSalaAulaBlackboardGrupoDisponivel = new ArrayList<>();
		}
		return listaSalaAulaBlackboardGrupoDisponivel;
	}

	public void setListaSalaAulaBlackboardGrupoDisponivel(List<SelectItem> listaSalaAulaBlackboardGrupoDisponivel) {
		this.listaSalaAulaBlackboardGrupoDisponivel = listaSalaAulaBlackboardGrupoDisponivel;
	}

	public String getIdSalaAulaBlackboardGrupoDisponivel() {
		if (idSalaAulaBlackboardGrupoDisponivel == null) {
			idSalaAulaBlackboardGrupoDisponivel = Constantes.EMPTY;
		}
		return idSalaAulaBlackboardGrupoDisponivel;
	}

	public void setIdSalaAulaBlackboardGrupoDisponivel(String idSalaAulaBlackboardGrupoDisponivel) {
		this.idSalaAulaBlackboardGrupoDisponivel = idSalaAulaBlackboardGrupoDisponivel;
	}

	public List<SelectItem> getListaDisciplinaClassificada() {
		if (listaDisciplinaClassificada == null) {
			listaDisciplinaClassificada = new ArrayList<>();
		}
		return listaDisciplinaClassificada;
	}

	public void setListaDisciplinaClassificada(List<SelectItem> listaDisciplinaClassificadaTcc) {
		this.listaDisciplinaClassificada = listaDisciplinaClassificadaTcc;
	}

	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem(Constantes.SE, Constantes.Semestral));
			listaSelectItemPeriodicidade.add(new SelectItem(Constantes.AN, Constantes.Anual));
			listaSelectItemPeriodicidade.add(new SelectItem(Constantes.IN, Constantes.Integral));

		}
		return listaSelectItemPeriodicidade;
	}

	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}

	public void limparCamposPeriodicidade()  {
		try {
			if(!getApresentarAno() && !getApresentarSemestre()) {
				setAno(Constantes.EMPTY);
				setSemestre(Constantes.EMPTY);
			}
			if(getApresentarAno()) {
				setSemestre(Constantes.EMPTY);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public Boolean getApresentarAno() {
		return getPeriodicidade().equals(Constantes.SE) || getPeriodicidade().equals(Constantes.AN);
	}

	public Boolean getApresentarSemestre() {
		return getPeriodicidade().equals(Constantes.SE);
	}
	
	public Boolean getApresentarIntegral() {
		return getPeriodicidade().equals(Constantes.IN);
	}
	
	public List<SelectItem> getListaSelectConteudoMasterBlackboard() {
		if (listaSelectConteudoMasterBlackboard == null) {
			listaSelectConteudoMasterBlackboard = new ArrayList<SelectItem>(0);
		}
		return listaSelectConteudoMasterBlackboard;
	}

	public void setListaSelectConteudoMasterBlackboard(List<SelectItem> listaSelectConteudoMasterBlackboard) {
		this.listaSelectConteudoMasterBlackboard = listaSelectConteudoMasterBlackboard;
	}
	
	public void realizarMarcacaoTodasSalaConteudoMaster() {
		try {
			getListaGeracaoSalaAulaBlackboard().stream()
			.filter(p-> Uteis.isAtributoPreenchido(p.getDisciplinaVO().getIdConteudoMasterBlackboard()))
			.forEach(p-> p.setConteudoMasterSelecionado(isMarcarTodasSalaConteudoMaster()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public boolean isMarcarTodasSalaConteudoMaster() {
		return marcarTodasSalaConteudoMaster;
	}

	public void setMarcarTodasSalaConteudoMaster(boolean marcarTodasSalaConteudoMaster) {
		this.marcarTodasSalaConteudoMaster = marcarTodasSalaConteudoMaster;
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem(Constantes.nome, Constantes.Nome));
			tipoConsultaComboDisciplina.add(new SelectItem(Constantes.abreviatura, Constantes.Abreviatura));
			tipoConsultaComboDisciplina.add(new SelectItem(Constantes.codigo, Constantes.Codigo));
		}
		return tipoConsultaComboDisciplina;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>();
			tipoConsultaComboAluno.add(new SelectItem(Constantes.nome, Constantes.Nome));
			tipoConsultaComboAluno.add(new SelectItem(Constantes.matricula, Constantes.Matricula));
			tipoConsultaComboAluno.add(new SelectItem(Constantes.registroAcademico, Constantes.Registro_Academico));
			return tipoConsultaComboAluno;
		}
		return tipoConsultaComboAluno;
	}



	public TipoSalaAulaBlackboardEnum getTipoSalaAulaBlackboardEnum() {
		if (tipoSalaAulaBlackboardEnum == null) {
			tipoSalaAulaBlackboardEnum = TipoSalaAulaBlackboardEnum.NENHUM;
		}
		return tipoSalaAulaBlackboardEnum;
	}

	public void setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum) {
		this.tipoSalaAulaBlackboardEnum = tipoSalaAulaBlackboardEnum;
	}

	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = Constantes.SE;
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	

	public String getIdSalaAulaBlackboard() {
		if (idSalaAulaBlackboard == null) {
			idSalaAulaBlackboard = Constantes.EMPTY;
		}
		return idSalaAulaBlackboard;
	}

	public void setIdSalaAulaBlackboard(String idSalaAulaBlackboard) {
		this.idSalaAulaBlackboard = idSalaAulaBlackboard;
	}

	public String getAno() {
		if (ano == null) {
			ano = Constantes.EMPTY;
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = Constantes.EMPTY;
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
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

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
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

	public SalaBlackboardSituacaoNotaEnum getSituacaoNota() {
		if (situacaoNota == null) {
			situacaoNota = SalaBlackboardSituacaoNotaEnum.TODOS;
		}
		return situacaoNota;
	}

	public void setSituacaoNota(SalaBlackboardSituacaoNotaEnum situacaoNota) {
		this.situacaoNota = situacaoNota;
	}

	public Integer getQtdLinhasExportacaoPorSalaAulaBlackboard() {
		if (qtdLinhasExportacaoPorSalaAulaBlackboard == null) {
			qtdLinhasExportacaoPorSalaAulaBlackboard = 5;
		}
		return qtdLinhasExportacaoPorSalaAulaBlackboard;
	}

	public void setQtdLinhasExportacaoPorSalaAulaBlackboard(Integer qtdLinhasExportacaoPorSalaAulaBlackboard) {
		this.qtdLinhasExportacaoPorSalaAulaBlackboard = qtdLinhasExportacaoPorSalaAulaBlackboard;
	}

	public Boolean getExportarSala() {
		if (exportarSala == null) {
			exportarSala = Boolean.TRUE;
		}
		return exportarSala;
	}

	public void setExportarSala(Boolean exportarSala) {
		this.exportarSala = exportarSala;
	}

	public Boolean getExportarAlunos() {
		if (exportarAlunos == null) {
			exportarAlunos = Boolean.FALSE;
		}
		return exportarAlunos;
	}

	public void setExportarAlunos(Boolean exportarAlunos) {
		this.exportarAlunos = exportarAlunos;
	}

	public Boolean getExportarNotas() {
		if (exportarNotas == null) {
			exportarNotas = Boolean.FALSE;
		}
		return exportarNotas;
	}

	public void setExportarNotas(Boolean exportarNotas) {
		this.exportarNotas = exportarNotas;
	}

	public Boolean getExportarProfessores() {
		if (exportarProfessores == null) {
			exportarProfessores = Boolean.TRUE;
		}
		return exportarProfessores;
	}

	public void setExportarProfessores(Boolean exportarProfessores) {
		this.exportarProfessores = exportarProfessores;
	}

	public Boolean getExportarFacilitadores() {
		if (exportarFacilitadores == null) {
			exportarFacilitadores = Boolean.TRUE;
		}
		return exportarFacilitadores;
	}

	public void setExportarFacilitadores(Boolean exportarFacilitadores) {
		this.exportarFacilitadores = exportarFacilitadores;
	}

	public Boolean getExportarSupervisores() {
		if (exportarSupervisores == null) {
			exportarSupervisores = Boolean.TRUE;
		}
		return exportarSupervisores;
	}

	public void setExportarSupervisores(Boolean exportarSupervisores) {
		this.exportarSupervisores = exportarSupervisores;
	}

	public OperacaoImportacaoSalaBlackboardEnum getOperacaoImportacaoProfessor() {
		if (operacaoImportacaoProfessor == null) {
			operacaoImportacaoProfessor = OperacaoImportacaoSalaBlackboardEnum.INCLUIR;
		}
		return operacaoImportacaoProfessor;
	}

	public void setOperacaoImportacaoProfessor(OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoProfessor) {
		this.operacaoImportacaoProfessor = operacaoImportacaoProfessor;
	}

	public OperacaoImportacaoSalaBlackboardEnum getOperacaoImportacaoFacilitador() {
		if (operacaoImportacaoFacilitador == null) {
			operacaoImportacaoFacilitador = OperacaoImportacaoSalaBlackboardEnum.INCLUIR;
		}
		return operacaoImportacaoFacilitador;
	}

	public void setOperacaoImportacaoFacilitador(
			OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoFacilitador) {
		this.operacaoImportacaoFacilitador = operacaoImportacaoFacilitador;
	}

	public OperacaoImportacaoSalaBlackboardEnum getOperacaoImportacaoSupervisor() {
		if (operacaoImportacaoSupervisor == null) {
			operacaoImportacaoSupervisor = OperacaoImportacaoSalaBlackboardEnum.INCLUIR;
		}
		return operacaoImportacaoSupervisor;
	}

	public void setOperacaoImportacaoSupervisor(
			OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoSupervisor) {
		this.operacaoImportacaoSupervisor = operacaoImportacaoSupervisor;
	}

	public OperacaoImportacaoSalaBlackboardEnum getOperacaoImportacaoNota() {
		if (operacaoImportacaoNota == null) {
			operacaoImportacaoNota = OperacaoImportacaoSalaBlackboardEnum.INCLUIR;
		}
		return operacaoImportacaoNota;
	}

	public void setOperacaoImportacaoNota(OperacaoImportacaoSalaBlackboardEnum operacaoImportacaoNota) {
		this.operacaoImportacaoNota = operacaoImportacaoNota;
	}

	public UploadedFile getArquivo() {
		return arquivo;
	}

	public void setArquivo(UploadedFile arquivo) {
		this.arquivo = arquivo;
	}	

	public InputStream getInputStreamSalaImporta() {
		return inputStreamSalaImporta;
	}

	public void setInputStreamSalaImporta(InputStream inputStreamSalaImporta) {
		this.inputStreamSalaImporta = inputStreamSalaImporta;
	}

	public DataModelo getDadosConsultaSalaAula() {
		if (dadosConsultaSalaAula == null) {
			dadosConsultaSalaAula = new DataModelo();
			dadosConsultaSalaAula.setLimitePorPagina(10);
			dadosConsultaSalaAula.setPaginaAtual(0);
			dadosConsultaSalaAula.setPage(0);
		}
		return dadosConsultaSalaAula;
	}

	public void setDadosConsultaSalaAula(DataModelo dadosConsultaSalaAula) {
		this.dadosConsultaSalaAula = dadosConsultaSalaAula;
	}

	public void setTipoAdicionarFacilitador(String tipoAdicionarFacilitador) {
		this.tipoAdicionarFacilitador = tipoAdicionarFacilitador;
	}

	public String getTipoAdicionarFacilitador() {
		if (tipoAdicionarFacilitador == null) {
			tipoAdicionarFacilitador = Constantes.EMPTY;
		}
		return tipoAdicionarFacilitador;
	}

	public List<SelectItem> getTipoAdicionarComboFacilitador() {
		if (tipoAdicionarComboFacilitador == null) {
			tipoAdicionarComboFacilitador = new ArrayList<SelectItem>();
			tipoAdicionarComboFacilitador.add(new SelectItem(Constantes.PROFESSOR, Constantes.Professor));
			tipoAdicionarComboFacilitador.add(new SelectItem(Constantes.FACILITADOR, Constantes.Facilitador));
			tipoAdicionarComboFacilitador.add(new SelectItem(Constantes.ORIENTADOR, Constantes.Orientador));
		}
		return tipoAdicionarComboFacilitador;
	}

	public List<SelectItem> getTipoConsultaComboFacilitador() {
		if (tipoConsultaComboFacilitador == null) {
			tipoConsultaComboFacilitador = new ArrayList<SelectItem>();
			tipoConsultaComboFacilitador.add(new SelectItem(Constantes.nome, Constantes.Nome));
			tipoConsultaComboFacilitador.add(new SelectItem(Constantes.email, Constantes.Email_Institucional));
		}
		return tipoConsultaComboFacilitador;
	}

	public SalaAulaBlackboardVO getSalaAulaBlackboardSelecionada() {
		if (salaAulaBlackboardSelecionada == null) {
			salaAulaBlackboardSelecionada = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardSelecionada;
	}

	public void setSalaAulaBlackboardSelecionada(SalaAulaBlackboardVO salaAulaBlackboardSelecionada) {
		this.salaAulaBlackboardSelecionada = salaAulaBlackboardSelecionada;
	}

	public SalaAulaBlackboardPessoaVO getFacilitadorSelecionado() {
		if (facilitadorSelecionado == null) {
			facilitadorSelecionado = new SalaAulaBlackboardPessoaVO();
		}
		return facilitadorSelecionado;
	}

	public void setFacilitadorSelecionado(SalaAulaBlackboardPessoaVO facilitadorSelecionado) {
		this.facilitadorSelecionado = facilitadorSelecionado;
	}

	public List<SugestaoFacilitadorBlackboardVO> getListaSugestaoFacilitadores() {
		if (listaSugestaoFacilitadores == null) {
			listaSugestaoFacilitadores = new ArrayList<SugestaoFacilitadorBlackboardVO>(0);
		}
		return listaSugestaoFacilitadores;
	}

	public void setListaSugestaoFacilitadores(List<SugestaoFacilitadorBlackboardVO> listaSugestaoFacilitadores) {
		this.listaSugestaoFacilitadores = listaSugestaoFacilitadores;
	}

	public Integer getQuantidadeFacilitadoresPorSala() {
		if (quantidadeFacilitadoresPorSala == null) {
			quantidadeFacilitadoresPorSala = 3;
		}
		return quantidadeFacilitadoresPorSala;
	}

	public void setQuantidadeFacilitadoresPorSala(Integer quantidadeFacilitadoresPorSala) {
		this.quantidadeFacilitadoresPorSala = quantidadeFacilitadoresPorSala;
	}

	public List<SalaAulaBlackboardPessoaVO> getListaSalaAulaBlackboardPessoaImportar() {
		if (listaSalaAulaBlackboardPessoaImportar == null) {
			listaSalaAulaBlackboardPessoaImportar = new ArrayList<SalaAulaBlackboardPessoaVO>();
		}
		return listaSalaAulaBlackboardPessoaImportar;
	}

	public void setListaSalaAulaBlackboardPessoaImportar(
			List<SalaAulaBlackboardPessoaVO> listaSalaAulaBlackboardPessoaImportar) {
		this.listaSalaAulaBlackboardPessoaImportar = listaSalaAulaBlackboardPessoaImportar;
	}

	public List<SalaAulaBlackboardVO> getListaGeracaoSalaAulaBlackboard() {
		if (listaGeracaoSalaAulaBlackboard == null) {
			listaGeracaoSalaAulaBlackboard = new ArrayList<SalaAulaBlackboardVO>(0);
		}
		return listaGeracaoSalaAulaBlackboard;
	}

	public void setListaGeracaoSalaAulaBlackboard(List<SalaAulaBlackboardVO> listaGeracaoSalaAulaBlackboard) {
		this.listaGeracaoSalaAulaBlackboard = listaGeracaoSalaAulaBlackboard;
	}

	public Integer getBimestreGeracaoSala() {
		return bimestreGeracaoSala;
	}

	public void setBimestreGeracaoSala(Integer bimestreGeracaoSala) {
		this.bimestreGeracaoSala = bimestreGeracaoSala;
	}

	public Integer getSituacaoAlunoGeracaoSala() {
		if (situacaoAlunoGeracaoSala == null) {
			situacaoAlunoGeracaoSala = 0;
		}
		return situacaoAlunoGeracaoSala;
	}

	public void setSituacaoAlunoGeracaoSala(Integer situacaoAlunoGeracaoSala) {
		this.situacaoAlunoGeracaoSala = situacaoAlunoGeracaoSala;
	}

	public List<SelectItem> getListaSelectItemSituacaoAlunoGeracaoSala() {
		if (listaSelectItemSituacaoAlunoGeracaoSala == null) {
			listaSelectItemSituacaoAlunoGeracaoSala = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoAlunoGeracaoSala.add(new SelectItem(0, Constantes.EMPTY));
			listaSelectItemSituacaoAlunoGeracaoSala.add(new SelectItem(1, Constantes.Ensalados));
			listaSelectItemSituacaoAlunoGeracaoSala.add(new SelectItem(2, Constantes.Nao_Ensalados));
		}
		return listaSelectItemSituacaoAlunoGeracaoSala;
	}

	public void setListaSelectItemSituacaoAlunoGeracaoSala(List<SelectItem> listaSelectItemSituacaoAlunoGeracaoSala) {
		this.listaSelectItemSituacaoAlunoGeracaoSala = listaSelectItemSituacaoAlunoGeracaoSala;
	}

	public List<SelectItem> getListaSelectItemBimestre() {
		if (listaSelectItemBimestre == null) {
			listaSelectItemBimestre = new ArrayList<SelectItem>(0);
			listaSelectItemBimestre.add(new SelectItem(null, Constantes.EMPTY));
			listaSelectItemBimestre.add(new SelectItem(0, Constantes.Semestral));
			listaSelectItemBimestre.add(new SelectItem(1, Constantes._1_Bimestre));
			listaSelectItemBimestre.add(new SelectItem(2, Constantes._2_Bimestre));
		}
		return listaSelectItemBimestre;
	}

	public void setListaSelectItemBimestre(List<SelectItem> listaSelectItemBimestre) {
		this.listaSelectItemBimestre = listaSelectItemBimestre;
	}

	public DisciplinaVO getDisciplinaGeracaoSala() {
		if (disciplinaGeracaoSala == null) {
			disciplinaGeracaoSala = new DisciplinaVO();
		}
		return disciplinaGeracaoSala;
	}

	public void setDisciplinaGeracaoSala(DisciplinaVO disciplinaGeracaoSala) {
		this.disciplinaGeracaoSala = disciplinaGeracaoSala;
	}

	public void selecionarDisciplinaGeracaoSala() {
		try {
			setDisciplinaGeracaoSala((DisciplinaVO) context().getExternalContext().getRequestMap().get(Constantes.disciplinaItens));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparDisciplinaGeracaoSala() {
		setDisciplinaGeracaoSala(null);
	}



	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public void consultarGeracaoSalaAulaBlackboard() {
		try {
			Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()), "O campo ANO deve ser informado.");
			Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()), "O campo SEMESTRE deve ser informado.");
			setListaGeracaoSalaAulaBlackboard(getFacadeFactory().getSalaAulaBlackboardFacade()
					.consultarGeracaoSalaAulaBlackboard(getDisciplinaGeracaoSala().getCodigo(), getApresentarAno() ? getAno() : Constantes.EMPTY,
							getApresentarSemestre() ? getSemestre() : Constantes.EMPTY, getBimestreGeracaoSala(), getSituacaoAlunoGeracaoSala(), getNivelEducacionalApresentar()));
			setTotalizadoSalaAulaBlackboardVO(new SalaAulaBlackboardVO());
			getTotalizadoSalaAulaBlackboardVO().setQtdeAlunos(getListaGeracaoSalaAulaBlackboard().stream().map(t -> t.getQtdeAlunos()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardVO().setQtdAlunosEnsalados(getListaGeracaoSalaAulaBlackboard().stream().map(t -> t.getQtdAlunosEnsalados()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardVO().setQtdAlunosNaoEnsalados(getListaGeracaoSalaAulaBlackboard().stream().map(t -> t.getQtdAlunosNaoEnsalados()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardVO().setQtdeSalas(getListaGeracaoSalaAulaBlackboard().stream().map(t -> t.getQtdeSalas()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardVO().setQtdeSalasExistentes(getListaGeracaoSalaAulaBlackboard().stream().map(t -> t.getQtdeSalasExistentes()).reduce(0, Integer::sum));
			getTotalizadoSalaAulaBlackboardVO().setQtdeSalasNecessarias(getListaGeracaoSalaAulaBlackboard().stream().map(t -> t.getQtdeSalasNecessarias()).reduce(0, Integer::sum));
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarProcessamentoGeracaoSalaBlackboard() {
		try {
			setOncompleteModal(Constantes.EMPTY);
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(!Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
				throw new ConsistirException("Não foi encontrado uma CONFIGURAÇÃO BLACKBOARD parametrizada.");
			}
			if(getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirSalaBlack(getListaGeracaoSalaAulaBlackboard(), getProgressBarVO().getUsuarioVO())) {
				getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
				limparMensagem();
				getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
				getProgressBarVO().setOncomplete("RichFaces.$('panelGeracaoSala').show()");
				realizarAtualizacaoStatusSalasBlackboard("panelGeracaoSala");
//				getProgressBarVO().iniciar(0l, 1000, "Gerando fila de processamento....", false, this, "");
				setOncompleteModal("RichFaces.$('panelGeracaoSala').hide()");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarProcessamentoGeracaoSalaBlackboardEspecifica() {
		try {
			setProgressBarVO(new ProgressBarVO());
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			List<SalaAulaBlackboardVO> salaAulaBlackboardVOs =  new ArrayList<SalaAulaBlackboardVO>();
			salaAulaBlackboardVOs.add((SalaAulaBlackboardVO)getRequestMap().get(Constantes.salaItem));
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO =  getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarConfiguracaoSeiBlackboardPadrao(0, getUsuarioLogadoClone());
			if(!Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
				throw new ConsistirException("Não foi encontrado uma CONFIGURAÇÃO BLACKBOARD parametrizada.");
			}
			if(getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirSalaBlack(salaAulaBlackboardVOs, getProgressBarVO().getUsuarioVO())) {
				getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, getUsuarioLogadoClone());
			setOncompleteModal(Constantes.EMPTY);
			getProgressBarVO().setOncomplete("RichFaces.$('panelGeracaoSala').show()");
			getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
			realizarAtualizacaoStatusSalasBlackboard("panelGeracaoSala");
			//getProgressBarVO().iniciar(0l, 1000, "Carregando dados para processamento....", false, this, "");
			setOncompleteModal("RichFaces.$('panelGeracaoSala').hide()");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarAtualizacaoStatusCopiaConteudoPorSalaGrupo() {
		try {
			realizarAtualizacaoStatusSalasBlackboard("panelCopiarConteudoBlackboard");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAtualizacaoStatusApuracaoNotaAva() {
		try {
			realizarAtualizacaoStatusSalasBlackboard("panelConfirmarApuracao");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAtualizacaoStatusCriacaoSalasBlackboardGrupo() {
		try {
			realizarAtualizacaoStatusSalasBlackboard("panelGrupoTcc");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAtualizacaoStatusCriacaoSalasBlackboardGrupoProjetoIntegrador() {
		try {
			realizarAtualizacaoStatusSalasBlackboard("panelGrupoProjetoIntegrador");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAtualizacaoStatusFechamentoSalasBlackboardGrupo() {
		try {
			if(getListaSalaAulaBlackboardGrupoVOFechamento().stream().anyMatch(p-> p.getDisciplinaVO().getClassificacaoDisciplina().isTcc())) {
				realizarAtualizacaoStatusSalasBlackboard("panelGrupoTcc");
			}else {
				realizarAtualizacaoStatusSalasBlackboard("panelGrupoProjetoIntegrador");	
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarAtualizacaoStatusCriacaoSalasBlackboard() {
		try {
			realizarAtualizacaoStatusSalasBlackboard("panelGeracaoSala");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarAtualizacaoStatusSalasBlackboard(String popupPanel) {
		getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarEnsalamentoPendenteProcessamento(getProgressBarVO());
		if((getProgressBarVO().getForcarEncerramento() || !getProgressBarVO().getAtivado())) {
			switch (popupPanel) {
			case "panelGeracaoSala":
				if(!getListaGeracaoSalaAulaBlackboard().isEmpty()) {
					consultarGeracaoSalaAulaBlackboard();
				}
				break;
			case "panelGrupoTcc":
				if(!getListaSalaAulaBlackboardGrupoVO().isEmpty()) {
					realizarMontagemSalaTcc();
				}
				break;
			case "panelGrupoProjetoIntegrador":
				if(!getListaSalaAulaBlackboardGrupoVO().isEmpty()) {
					realizarMontagemSalaProjetoIntegrador();
				}
				break;
			case "panelConfirmarApuracao":
				consultarSalasBlackboard();
				break;
			default:
				break;
			}
			setOncompleteModal("RichFaces.$('"+popupPanel+"').show()");
		}
	}

	public void realizarValidacaoProcessamentoCriacaoSalasBlackboardEmAndamento() {
		try {
		Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()), "O campo ANO deve ser informado.");
		Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()), "O campo SEMESTRE deve ser informado.");
		limparMensagem();
		setProgressBarVO(new ProgressBarVO());
		getProgressBarVO().setSuperControle(this);
		getProgressBarVO().setAssincrono(false);
		//getProgressBarVO().setOncomplete("RichFaces.$('panelGeracaoSala').show()");
		getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
		getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarEnsalamentoPendenteProcessamento(getProgressBarVO());
		if((getProgressBarVO().getForcarEncerramento() || !getProgressBarVO().getAtivado())) {

			if(!getListaGeracaoSalaAulaBlackboard().isEmpty()) {
				consultarGeracaoSalaAulaBlackboard();
			}

			setOncompleteModal("RichFaces.$('panelGeracaoSala').show()");
		}

		}catch(Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public SalaAulaBlackboardVO getTotalizadoSalaAulaBlackboardVO() {
		if(totalizadoSalaAulaBlackboardVO == null) {
			totalizadoSalaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return totalizadoSalaAulaBlackboardVO;
	}

	public void setTotalizadoSalaAulaBlackboardVO(SalaAulaBlackboardVO totalizadoSalaAulaBlackboardVO) {
		this.totalizadoSalaAulaBlackboardVO = totalizadoSalaAulaBlackboardVO;
	}
	
	

	public SalaAulaBlackboardGrupoVO getTotalizadoSalaAulaBlackboardGrupoVO() {
		if(totalizadoSalaAulaBlackboardGrupoVO == null) {
			totalizadoSalaAulaBlackboardGrupoVO = new SalaAulaBlackboardGrupoVO();
		}
		return totalizadoSalaAulaBlackboardGrupoVO;
	}

	public void setTotalizadoSalaAulaBlackboardGrupoVO(SalaAulaBlackboardGrupoVO totalizadoSalaAulaBlackboardGrupoVO) {
		this.totalizadoSalaAulaBlackboardGrupoVO = totalizadoSalaAulaBlackboardGrupoVO;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if(listaConsultaDisciplina == null) {
			listaConsultaDisciplina =  new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
	}

	public List<CursoVO> getListaConsultaCurso() {
		if(listaConsultaCurso == null) {
			listaConsultaCurso =  new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if(listaConsultaAluno == null) {
			listaConsultaAluno =  new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List<MatriculaPeriodoVO> getListaMatriculaPeriodoVO() {
		return listaMatriculaPeriodoVO;
	}

	public void setListaMatriculaPeriodoVO(List<MatriculaPeriodoVO> listaMatriculaPeriodoVO) {
		this.listaMatriculaPeriodoVO = listaMatriculaPeriodoVO;
	}

	public boolean isPermiteOperacoesGrupoTcc() {
		return permiteOperacoesGrupoTcc;
	}

	public void setPermiteOperacoesGrupoTcc(boolean permiteOperacoesGrupoTcc) {
		this.permiteOperacoesGrupoTcc = permiteOperacoesGrupoTcc;
	}
	
	

	public boolean isPermiteOperacoesGrupoProjetoIntegrador() {
		return permiteOperacoesGrupoProjetoIntegrador;
	}

	public void setPermiteOperacoesGrupoProjetoIntegrador(boolean permiteOperacoesGrupoProjetoIntegrador) {
		this.permiteOperacoesGrupoProjetoIntegrador = permiteOperacoesGrupoProjetoIntegrador;
	}

	public void isVerificarPermissaoOperacoesGrupoTcc() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_OPERACOES_GRUPO_TCC, getUsuarioLogadoClone());
			setPermiteOperacoesGrupoTcc(true);
		} catch (Exception e) {
			setPermiteOperacoesGrupoTcc(false);
		}
	}
	
	public void isVerificarPermissaoOperacoesGrupoProjetoIntegrador() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_OPERACOES_GRUPO_PROJETO_INTEGRADOR, getUsuarioLogadoClone());
			setPermiteOperacoesGrupoProjetoIntegrador(true);
		} catch (Exception e) {
			setPermiteOperacoesGrupoProjetoIntegrador(false);
		}
	}

	public void isVerificarPermissaoExcluirSalaAulaBlackboard() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_EXCLUIR_SALA_AULA_BLACKBOARD, getUsuarioLogadoClone());
			setPermiteExcluirSalaAulaBlackboard(true);
		} catch (Exception e) {
			setPermiteExcluirSalaAulaBlackboard(false);
		}
	}

	public boolean isPermiteExcluirSalaAulaBlackboard() {
		return permiteExcluirSalaAulaBlackboard;
	}

	public void setPermiteExcluirSalaAulaBlackboard(boolean permiteExcluirSalaAulaBlackboard) {
		this.permiteExcluirSalaAulaBlackboard = permiteExcluirSalaAulaBlackboard;
	}
	
	public void isVerificarPermissaoCriarAtualizarSalaAulaBlackboard() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_CRIAR_SALA_BLACKBOARD, getUsuarioLogadoClone());
			setPermiteCriarAtualizarSalaAulaBlackboard(true);
		} catch (Exception e) {
			setPermiteCriarAtualizarSalaAulaBlackboard(false);
		}
	}
	
	public boolean isPermiteCriarAtualizarSalaAulaBlackboard() {
		return permiteCriarAtualizarSalaAulaBlackboard;
	}
	
	public void setPermiteCriarAtualizarSalaAulaBlackboard(boolean permiteCriarAtualizarSalaAulaBlackboard) {
		this.permiteCriarAtualizarSalaAulaBlackboard = permiteCriarAtualizarSalaAulaBlackboard;
	}
	
	

	public DataModelo getControleConsultaLogProcessamento() {
		if(controleConsultaLogProcessamento == null) {
			controleConsultaLogProcessamento =  new DataModelo();
			controleConsultaLogProcessamento.setLimitePorPagina(10);
			controleConsultaLogProcessamento.setDataIni(null);
		}
		return controleConsultaLogProcessamento;
	}

	public void setControleConsultaLogProcessamento(DataModelo controleConsultaLogProcessamento) {
		this.controleConsultaLogProcessamento = controleConsultaLogProcessamento;
	}
	
	

	public DataModelo getControleConsultaHistoricoNotaBlackboard() {
		if(controleConsultaHistoricoNotaBlackboard == null) {
			controleConsultaHistoricoNotaBlackboard =  new DataModelo();
		}
		return controleConsultaHistoricoNotaBlackboard;
	}

	public void setControleConsultaHistoricoNotaBlackboard(DataModelo controleConsultaHistoricoNotaBlackboard) {
		this.controleConsultaHistoricoNotaBlackboard = controleConsultaHistoricoNotaBlackboard;
	}



	private String disciplinaLogProcessamento;

	public String getDisciplinaLogProcessamento() {
		if(disciplinaLogProcessamento == null) {
			disciplinaLogProcessamento =  Constantes.EMPTY;
		}
		return disciplinaLogProcessamento;
	}

	public void setDisciplinaLogProcessamento(String disciplinaLogProcessamento) {
		this.disciplinaLogProcessamento = disciplinaLogProcessamento;
	}

	public void realizarExecucaoSalaAulaOperacao() {
		try {
			SalaAulaBlackboardOperacaoVO obj = (SalaAulaBlackboardOperacaoVO) context().getExternalContext().getRequestMap().get("log");
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getAplicacaoControle().getConfiguracaoSeiBlackboardVO();
			Uteis.checkState(!Uteis.isAtributoPreenchido(configSeiBlackboardVO), "Não foi encontrado uma CONFIGURAÇÃO BLACKBOARD parametrizada.");
			getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarExecucaoSalaAulaOperacao(obj, getUsuarioLogadoClone());
			getControleConsultaLogProcessamento().getListaConsulta().removeIf(p-> ((SalaAulaBlackboardOperacaoVO)p).getCodigo().equals(obj.getCodigo()));
			setMensagemID(MSG_TELA.msg_dados_operacao.name());
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	private SalaAulaBlackboardOperacaoVO salaAulaBlackboardOperacaoFiltroVO;
	
	public SalaAulaBlackboardOperacaoVO getSalaAulaBlackboardOperacaoFiltroVO() {
		if(salaAulaBlackboardOperacaoFiltroVO == null) {
			salaAulaBlackboardOperacaoFiltroVO =  new SalaAulaBlackboardOperacaoVO();
		}
		return salaAulaBlackboardOperacaoFiltroVO;
	}

	public void setSalaAulaBlackboardOperacaoFiltroVO(SalaAulaBlackboardOperacaoVO salaAulaBlackboardOperacaoFiltroVO) {
		this.salaAulaBlackboardOperacaoFiltroVO = salaAulaBlackboardOperacaoFiltroVO;
	}
	
	public void inicializarLogProcessamento(Integer pagina) {		
		getSalaAulaBlackboardOperacaoFiltroVO().setAno(getAno());
		getSalaAulaBlackboardOperacaoFiltroVO().setSemestre(getSemestre());
		getSalaAulaBlackboardOperacaoFiltroVO().setTipoSalaAulaBlackboardEnum(getTipoSalaAulaBlackboardEnum());
		getSalaAulaBlackboardOperacaoFiltroVO().getDisciplinaVO().setNome(getDisciplina().getNome());
		getSalaAulaBlackboardOperacaoFiltroVO().getDisciplinaVO().setAbreviatura(getDisciplina().getAbreviatura());		
		consultarLogProcessamento(pagina);
	}

	public void consultarLogProcessamento(Integer pagina) {
		try {			
			getControleConsultaLogProcessamento().setPage(pagina);
			getControleConsultaLogProcessamento().setPaginaAtual(pagina);
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().consultarLogErroProcessamento(getControleConsultaLogProcessamento(), getSalaAulaBlackboardOperacaoFiltroVO());
			if(!getControleConsultaLogProcessamento().getApresentarListaConsulta()) {
				setMensagemID("msg_nenhum_log_erro",  Uteis.ALERTA);
			}else {
				setMensagemID(MSG_TELA.msg_dados_consultados.name(),  Uteis.ALERTA);
			}
		}catch (Exception e) {			
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void scrollerListenerLogProcessamento(DataScrollEvent DataScrollEvent) {
		consultarLogProcessamento(DataScrollEvent.getPage());
	}
	

	public String getNomeArquivoUploadSalaAulaBlackboard() {
		if (nomeArquivoUploadSalaAulaBlackboard == null) {
			nomeArquivoUploadSalaAulaBlackboard = Constantes.EMPTY;
		}
		return nomeArquivoUploadSalaAulaBlackboard;
	}

	public void setNomeArquivoUploadSalaAulaBlackboard(String nomeArquivoUploadSalaAulaBlackboard) {
		this.nomeArquivoUploadSalaAulaBlackboard = nomeArquivoUploadSalaAulaBlackboard;
	}

	public SalaAulaBlackboardPessoaVO getAlunoSalaAulaBlackboardSelecionado() {
		if (alunoSalaAulaBlackboardSelecionado == null) {
			alunoSalaAulaBlackboardSelecionado = new SalaAulaBlackboardPessoaVO();
		}
		return alunoSalaAulaBlackboardSelecionado;
	}

	public void setAlunoSalaAulaBlackboardSelecionado(SalaAulaBlackboardPessoaVO alunoSalaAulaBlackboardSelecionado) {
		this.alunoSalaAulaBlackboardSelecionado = alunoSalaAulaBlackboardSelecionado;
	}	

	public HistoricoNotaBlackboardVO getHistoricoNotaBlackboardSelecionado() {
		if (historicoNotaBlackboardSelecionado == null) {
			historicoNotaBlackboardSelecionado = new HistoricoNotaBlackboardVO();
		}
		return historicoNotaBlackboardSelecionado;
	}

	public void setHistoricoNotaBlackboardSelecionado(HistoricoNotaBlackboardVO historicoNotaBlackboardSelecionado) {
		this.historicoNotaBlackboardSelecionado = historicoNotaBlackboardSelecionado;
	}	
	
	
	public boolean isRealizarCalculoMediaApuracaoNotas() {
		return realizarCalculoMediaApuracaoNotas;
	}

	public void setRealizarCalculoMediaApuracaoNotas(boolean realizarCalculoMediaApuracaoNotas) {
		this.realizarCalculoMediaApuracaoNotas = realizarCalculoMediaApuracaoNotas;
	}
	
	

	public boolean isRealizarBuscarNotaBlackboard() {
		return realizarBuscarNotaBlackboard;
	}

	public void setRealizarBuscarNotaBlackboard(boolean realizarBuscarNotaBlackboard) {
		this.realizarBuscarNotaBlackboard = realizarBuscarNotaBlackboard;
	}

	public void consultarMatriculaPeriodoTurmaDisciplinaParaEnsalamento(SalaAulaBlackboardVO salaAulaBlackboardVO, String listaAlunos){
		try {
			setSalaAulaBlackboardSelecionada(salaAulaBlackboardVO);
			if(Uteis.isAtributoPreenchido(listaAlunos) && listaAlunos.equals(Constantes.TODOS)) {
				listaAlunos = salaAulaBlackboardVO.getAlunosEnsalados()+ (salaAulaBlackboardVO.getAlunosEnsalados().isEmpty() || salaAulaBlackboardVO.getAlunosNaoEnsalados().isEmpty() ? Constantes.EMPTY : Constantes.VIRGULA) + salaAulaBlackboardVO.getAlunosNaoEnsalados();
			}
			if(Uteis.isAtributoPreenchido(listaAlunos)) {
				getSalaAulaBlackboardSelecionada().setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getSalaAulaBlackboardFacade().consultarMatriculaPeriodoTurmaDisciplinaParaEnsalamento(listaAlunos, salaAulaBlackboardVO.getDisciplinaVO(), salaAulaBlackboardVO.getAno(), salaAulaBlackboardVO.getSemestre()));
			}			
			setMensagemID(MSG_TELA.msg_dados_consultados.name(),  Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}		
	}
	
	private List<SalaAulaBlackboardVO> salaAulaBlackboardExistenteVOs;
	
	public List<SalaAulaBlackboardVO> getSalaAulaBlackboardExistenteVOs() {
		if(salaAulaBlackboardExistenteVOs == null) {
			salaAulaBlackboardExistenteVOs =  new ArrayList<SalaAulaBlackboardVO>(0);
		}
		return salaAulaBlackboardExistenteVOs;
	}

	public void setSalaAulaBlackboardExistenteVOs(List<SalaAulaBlackboardVO> salaAulaBlackboardExistenteVOs) {
		this.salaAulaBlackboardExistenteVOs = salaAulaBlackboardExistenteVOs;
	}

	public void consultarSalasGeradaParaEnsalamento(SalaAulaBlackboardVO salaAulaBlackboardVO){
		try {
			setSalaAulaBlackboardSelecionada(salaAulaBlackboardVO);
			getSalaAulaBlackboardExistenteVOs().clear();
			if(Uteis.isAtributoPreenchido(salaAulaBlackboardVO.getSalasExistentes())) {				
				setSalaAulaBlackboardExistenteVOs(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulasExistentes(salaAulaBlackboardVO.getSalasExistentes(), getUsuarioLogadoClone()));
			}			
			setMensagemID(MSG_TELA.msg_dados_consultados.name(),  Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void consultarListaSelectItemCalendarioAgrupamento(ClassificacaoDisciplinaEnum classificacaoDisciplinaEnum) {
		try {
			limparMensagem();
			setListaSelectItemCalendarioAgrupamento(UtilSelectItem.getListaSelectItem(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorClassificacaoAnoSemestre(classificacaoDisciplinaEnum, getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()), "codigo", "descricaoApresentar"));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemCalendarioAgrupamento() {
		if(listaSelectItemCalendarioAgrupamento == null) {
			listaSelectItemCalendarioAgrupamento =  new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCalendarioAgrupamento;
	}

	public void setListaSelectItemCalendarioAgrupamento(List<SelectItem> listaSelectItemCalendarioAgrupamento) {
		this.listaSelectItemCalendarioAgrupamento = listaSelectItemCalendarioAgrupamento;
	}
	

	public void selecionarCalendarioAgrupamento(ClassificacaoDisciplinaEnum classificacaoDisciplinaEnum) {
		try {
			limparMensagem();
			if(Uteis.isAtributoPreenchido(getCalendarioAgrupamentoTccVO())) {
				setCalendarioAgrupamentoTccVO(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorChavePrimaria(getCalendarioAgrupamentoTccVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}else {
				setCalendarioAgrupamentoTccVO(new CalendarioAgrupamentoTccVO());
			}
			setTotalizadoSalaAulaBlackboardGrupoVO(new SalaAulaBlackboardGrupoVO());
			getListaSalaAulaBlackboardGrupoVO().clear();
			getListaSalaAulaBlackboardGrupoVOFechamento().clear();
			montarDisciplinasPorClassificacao(classificacaoDisciplinaEnum);
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public DataModelo getControleConsultaLogEnsalamento() {
		if(controleConsultaLogEnsalamento == null) {
			controleConsultaLogEnsalamento =  new DataModelo();
			controleConsultaLogEnsalamento.setLimitePorPagina(10);
			controleConsultaLogEnsalamento.setPage(1);
			controleConsultaLogEnsalamento.setPaginaAtual(1);
			controleConsultaLogEnsalamento.setDataIni(null);
		}
		return controleConsultaLogEnsalamento;
	}

	public void setControleConsultaLogEnsalamento(DataModelo controleConsultaLogEnsalamento) {
		this.controleConsultaLogEnsalamento = controleConsultaLogEnsalamento;
	}

	public LogOperacaoEnsalamentoBlackboardVO getLogOperacaoEnsalamentoBlackboardFiltroVO() {
		if(logOperacaoEnsalamentoBlackboardFiltroVO == null) {
			logOperacaoEnsalamentoBlackboardFiltroVO =  new LogOperacaoEnsalamentoBlackboardVO();
		}
		return logOperacaoEnsalamentoBlackboardFiltroVO;
	}

	public void setLogOperacaoEnsalamentoBlackboardFiltroVO(
			LogOperacaoEnsalamentoBlackboardVO logOperacaoEnsalamentoBlackboardFiltroVO) {
		this.logOperacaoEnsalamentoBlackboardFiltroVO = logOperacaoEnsalamentoBlackboardFiltroVO;
	}
	
	public void inicializarConsultarLogEnsalamento(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, Integer codigoSala, Integer diciplina) {
		getControleConsultaLogEnsalamento().setLimitePorPagina(10);		
		setLogOperacaoEnsalamentoBlackboardFiltroVO(null);
		getLogOperacaoEnsalamentoBlackboardFiltroVO().setAno(getAno());
		getLogOperacaoEnsalamentoBlackboardFiltroVO().setSemestre(getSemestre());
		getLogOperacaoEnsalamentoBlackboardFiltroVO().setTipoSalaAulaBlackboard(tipoSalaAulaBlackboardEnum);
		getLogOperacaoEnsalamentoBlackboardFiltroVO().setCodigoSalaAulaBlackBoard(codigoSala);
		if(Uteis.isAtributoPreenchido(diciplina)) {			
			DisciplinaVO disciplinaVO;
			try {
				disciplinaVO = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(diciplina, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getLogOperacaoEnsalamentoBlackboardFiltroVO().setNomeDisciplina(disciplinaVO.getNome());
				getLogOperacaoEnsalamentoBlackboardFiltroVO().setAbreviaturaDisciplina(disciplinaVO.getAbreviatura());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		consultarLogEnsalamento(1);
	}
	
	public void scrollLogEnsalamento(DataScrollEvent scroll) {
		consultarLogEnsalamento(scroll.getPage());
	}
	public void consultarLogEnsalamento(Integer pagina) {
		try {
			getControleConsultaLogEnsalamento().setPage(pagina);
			getControleConsultaLogEnsalamento().setPaginaAtual(pagina);
			getFacadeFactory().getLogOperacaoEnsalamentoBlackboardFacade().consultar(getControleConsultaLogEnsalamento(), getLogOperacaoEnsalamentoBlackboardFiltroVO(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.ALERTA);
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}
	
	private List<SelectItem> listaSelectItemTipoOrigemOperacaoBlackboard;
	private List<SelectItem> listaSelectItemOperacaoBlackboard;
	
	public List<SelectItem> getListaSelectItemTipoOrigemOperacaoBlackboard() {
		if(listaSelectItemTipoOrigemOperacaoBlackboard == null) {
			listaSelectItemTipoOrigemOperacaoBlackboard =  new ArrayList<SelectItem>();
			listaSelectItemTipoOrigemOperacaoBlackboard.add(new SelectItem("", ""));
			if(getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().isEmpty() || !(getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().equals("INATIVAR_USUARIO")
					|| getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().equals("ATUALIZAR_USUARIO")
					|| getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().equals("ATIVAR_USUARIO"))) {
				listaSelectItemTipoOrigemOperacaoBlackboard.add(new SelectItem("SALA_AULA_BLACKBOARD", "Sala Aula"));			
				listaSelectItemTipoOrigemOperacaoBlackboard.add(new SelectItem("GRUPO_SALA_AULA_BLACKBOARD_EXISTENTE", "Grupo Sala Aula"));			
				listaSelectItemTipoOrigemOperacaoBlackboard.add(new SelectItem("SALA_AULA_BLACKBOARD_PESSOA", "Membro Sala Aula"));			
			}
			if(getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().isEmpty() ||  !(getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().equals("INATIVAR_USUARIO")
					|| getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().equals("ATUALIZAR_USUARIO")
					|| getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().equals("ATIVAR_USUARIO")
					|| getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().equals("DELETE") )) {
				listaSelectItemTipoOrigemOperacaoBlackboard.add(new SelectItem("CONSOLIDAR_NOTA_SALA_AULA_BLACBOARD", "Consolidar Nota Ava"));
				listaSelectItemTipoOrigemOperacaoBlackboard.add(new SelectItem("APURAR_NOTA_SALA_AULA_BLACBOARD", "Carregar Nota Ava"));
				listaSelectItemTipoOrigemOperacaoBlackboard.add(new SelectItem("CONSOLIDAR_NOTA_SOMENTE_NO_SEI", "Consolidar Nota SEI"));
			}
			if(getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().isEmpty() || getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().equals("ATIVAR_USUARIO")) {
				listaSelectItemTipoOrigemOperacaoBlackboard.add(new SelectItem("ATIVAR_USUARIO", "Ativar Membro"));			
			}
			if(getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().isEmpty() || getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().equals("INATIVAR_USUARIO")) {
				listaSelectItemTipoOrigemOperacaoBlackboard.add(new SelectItem("INATIVAR_USUARIO", "Inativar Membro"));			
			}
			if(getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().isEmpty() || getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().equals("ATUALIZAR_USUARIO")) {
				listaSelectItemTipoOrigemOperacaoBlackboard.add(new SelectItem("ATUALIZAR_USUARIO", "Atualizar Membro"));			
			}
			if(!getSalaAulaBlackboardOperacaoFiltroVO().getOperacao().isEmpty() && listaSelectItemTipoOrigemOperacaoBlackboard.stream().noneMatch(t -> ((String)t.getValue()).equals(getSalaAulaBlackboardOperacaoFiltroVO().getOperacao()))) {
				getSalaAulaBlackboardOperacaoFiltroVO().setOperacao("");
			}
		}
		return listaSelectItemTipoOrigemOperacaoBlackboard;
	}

	public void setListaSelectItemTipoOrigemOperacaoBlackboard(
			List<SelectItem> listaSelectItemTipoOrigemOperacaoBlackboard) {
		this.listaSelectItemTipoOrigemOperacaoBlackboard = listaSelectItemTipoOrigemOperacaoBlackboard;
	}

	public List<SelectItem> getListaSelectItemOperacaoBlackboard() {
		if(listaSelectItemOperacaoBlackboard == null) {
			listaSelectItemOperacaoBlackboard =  new ArrayList<SelectItem>();
			listaSelectItemOperacaoBlackboard.add(new SelectItem("", ""));
			if(getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().isEmpty() || !(getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("ATIVAR_USUARIO")
					|| getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("INATIVAR_USUARIO") 
					|| getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("ATUALIZAR_USUARIO"))) {
				listaSelectItemOperacaoBlackboard.add(new SelectItem("INSERT", "Inclusão"));
			}
			if(getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().isEmpty() || !(getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("ATIVAR_USUARIO")
					|| getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("INATIVAR_USUARIO") 
					|| getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("ATUALIZAR_USUARIO")
					|| getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("CONSOLIDAR_NOTA_SALA_AULA_BLACBOARD")
					|| getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("APURAR_NOTA_SALA_AULA_BLACBOARD")
					|| getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("CONSOLIDAR_NOTA_SOMENTE_NO_SEI"))) {				
				listaSelectItemOperacaoBlackboard.add(new SelectItem("DELETE", "Exclusão"));
			}
			if(getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().isEmpty() || getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("ATIVAR_USUARIO")) {
				listaSelectItemOperacaoBlackboard.add(new SelectItem("ATIVAR_USUARIO", "Ativar Membro"));	
			}
			if(getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().isEmpty() || getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("INATIVAR_USUARIO")) {
				listaSelectItemOperacaoBlackboard.add(new SelectItem("INATIVAR_USUARIO", "Inativar Membro"));	
			}
			if(getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().isEmpty() || getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().equals("ATUALIZAR_USUARIO")) {
				listaSelectItemOperacaoBlackboard.add(new SelectItem("ATUALIZAR_USUARIO", "Atualizar Membro"));	
			}
			if(!getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem().isEmpty() && listaSelectItemOperacaoBlackboard.stream().noneMatch(t -> ((String)t.getValue()).equals(getSalaAulaBlackboardOperacaoFiltroVO().getTipoOrigem()))) {
				getSalaAulaBlackboardOperacaoFiltroVO().setTipoOrigem("");
			}
		}
		return listaSelectItemOperacaoBlackboard;
	}

	public void setListaSelectItemOperacaoBlackboard(List<SelectItem> listaSelectItemOperacaoBlackboard) {
		this.listaSelectItemOperacaoBlackboard = listaSelectItemOperacaoBlackboard;
	}

	public void montarListaSelectItemOperacaoBlackboard() {
		setListaSelectItemOperacaoBlackboard(null);
		getListaSelectItemOperacaoBlackboard();
	}
	public void montarListaSelectItemTipoOrigemOperacaoBlackboard() {
		setListaSelectItemTipoOrigemOperacaoBlackboard(null);
		getListaSelectItemTipoOrigemOperacaoBlackboard();
	} 
	
	public void realizarGeracaoExcelLogNotas() {
		try {
			getFacadeFactory().getHistoricoNotaBlackboardFacade().consultar(getControleConsultaHistoricoNotaBlackboard(), getHistoricoNotaBlackboardSelecionado(), false, getUsuarioLogadoClone());
			File arquivo = (getFacadeFactory().getSalaAulaBlackboardFacade()).realizarGeracaoExcelLogNotas(getControleConsultaHistoricoNotaBlackboard() ,getUsuarioLogadoClone());
			setCaminhoRelatorio(arquivo.getName());
			setFazerDownload(true);
			setMensagemID(MSG_TELA.msg_relatorio_ok.name());
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public BlackboardGestaoFechamentoNotaVO getBlackboardGestaoFechamentoNotaVO() {
		if (blackboardGestaoFechamentoNotaVO == null) {
			blackboardGestaoFechamentoNotaVO = new BlackboardGestaoFechamentoNotaVO();
		}
		return blackboardGestaoFechamentoNotaVO;
	}
	
	public void setBlackboardGestaoFechamentoNotaVO(BlackboardGestaoFechamentoNotaVO salaAulaBlackboardGestaoFechamentoNotaVO) {
		this.blackboardGestaoFechamentoNotaVO = salaAulaBlackboardGestaoFechamentoNotaVO;
	}
	
	public Boolean getOperacaoFechamentoNotaPendente() {
		if (operacaoFechamentoNotaPendente == null) {
			operacaoFechamentoNotaPendente = Boolean.FALSE;
		}
		return operacaoFechamentoNotaPendente;
	}
	
	public void setOperacaoFechamentoNotaPendente(Boolean operacaoFechamentoNotaPendente) {
		this.operacaoFechamentoNotaPendente = operacaoFechamentoNotaPendente;
	}
	
	public String getOncompleteFechamentoNota() {
		if (oncompleteFechamentoNota == null) {
			oncompleteFechamentoNota = Constantes.EMPTY;
		}
		return oncompleteFechamentoNota;
	}
	
	public void setOncompleteFechamentoNota(String oncompleteFechamentoNota) {
		this.oncompleteFechamentoNota = oncompleteFechamentoNota;
	}
	
	public Boolean getApresentarProgressBarFechamentoNota() {
		if (apresentarProgressBarFechamentoNota == null) {
			apresentarProgressBarFechamentoNota = Boolean.FALSE;
		}
		return apresentarProgressBarFechamentoNota;
	}
	
	public void setApresentarProgressBarFechamentoNota(Boolean apresentarProgressBarFechamentoNota) {
		this.apresentarProgressBarFechamentoNota = apresentarProgressBarFechamentoNota;
	}
	
	public void verificarOperacaoExistente() throws Exception {
		if(!getFacadeFactory().getBlackboardFechamentoNotaOperacaoInterfaceFacade().validarExisteOperacaoFechamentoNotaPendente()) {
			setOncompleteFechamentoNota("RichFaces.$('panelFechamentoNota').show()");
			setBlackboardGestaoFechamentoNotaVO(getFacadeFactory().getBlackboardFechamentoNotaOperacaoInterfaceFacade().consultarConfguracaoAcademicaParaFechamentoNota(getIdSalaAulaBlackboard(), getUnidadeEnsinoVOs(), getTipoSalaAulaBlackboardEnum(), getAno(), getSemestre(), getBimestreGeracaoSala(), getCurso().getCodigo(), 0, getDisciplina().getCodigo(), getMatricula().getMatricula(), getFiltroRelatorioAcademicoVO(), getUsuarioLogadoClone(), getDadosConsultaSalaAula(), getNivelEducacionalApresentar()));
		} else {
			if (getAplicacaoControle().getProgressBarFechamentoNota().getAtivado() && !Uteis.isAtributoPreenchido(getBlackboardGestaoFechamentoNotaVO())) {
				setApresentarProgressBarFechamentoNota(Boolean.TRUE);
			} else {
				setApresentarProgressBarFechamentoNota(Boolean.FALSE);
				setOncompleteFechamentoNota("RichFaces.$('panelAvisoFechamentoNota').show(); RichFaces.$('panelFechamentoNota').hide();");
			}
		}
	}
	
	public void consultarNotasParaFechamentoNota() {
		try {
			setApresentarProgressBarFechamentoNota(Boolean.FALSE);
			setOncompleteFechamentoNota(Constantes.EMPTY);
			setBlackboardGestaoFechamentoNotaVO(new BlackboardGestaoFechamentoNotaVO());
			limparMensagem();
			Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()), "O campo ANO deve ser informado.");
			Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()), "O campo SEMESTRE deve ser informado.");
			verificarOperacaoExistente();
		} catch (Exception e) {
			setBlackboardGestaoFechamentoNotaVO(new BlackboardGestaoFechamentoNotaVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarFechamentoNota() {
		try {
			setApresentarProgressBarFechamentoNota(Boolean.FALSE);
			setOncompleteFechamentoNota(Constantes.EMPTY);
			Uteis.checkState(getApresentarAno() && !Uteis.isAtributoPreenchido(getAno()), "O campo ANO deve ser informado.");
			Uteis.checkState(getApresentarSemestre() && !Uteis.isAtributoPreenchido(getSemestre()), "O campo SEMESTRE deve ser informado.");
			getFacadeFactory().getBlackboardFechamentoNotaOperacaoInterfaceFacade().realizarOperacaoDeFechamentoNota(getBlackboardGestaoFechamentoNotaVO(), getIdSalaAulaBlackboard(), getUnidadeEnsinoVOs(), getTipoSalaAulaBlackboardEnum(), getAno(), getSemestre(), getBimestreGeracaoSala(), getCurso().getCodigo(), 0, getDisciplina().getCodigo(), getMatricula().getMatricula(), getFiltroRelatorioAcademicoVO(), getUsuarioLogadoClone());
			setOncompleteFechamentoNota("RichFaces.$('panelFechamentoNota').hide();");
			iniciarProgressBarFechamentoNota();
			setMensagemID("Fechamento de notas criado com sucesso, foram criadas operações para realizar os fechamentos de notas", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void iniciarProgressBarFechamentoNota() {
		try {
			if (!getAplicacaoControle().getProgressBarFechamentoNota().getAtivado()) {
				setApresentarProgressBarFechamentoNota(Boolean.TRUE);
				getAplicacaoControle().setProgressBarFechamentoNota(new ProgressBarVO());
				getAplicacaoControle().getProgressBarFechamentoNota().setUsuarioVO(getUsuarioLogado());
				getAplicacaoControle().getProgressBarFechamentoNota().setConfiguracaoGeralSistemaVO(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null));
				getAplicacaoControle().getProgressBarFechamentoNota().setCaminhoWebRelatorio(getCaminhoPastaWeb());
				getAplicacaoControle().getProgressBarFechamentoNota().resetar();
				getAplicacaoControle().getProgressBarFechamentoNota().iniciar(0l, 999, "Iniciando Operação", Boolean.TRUE, this, "executarOperacaoFechamentoNotaBlackboard");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void executarOperacaoFechamentoNotaBlackboard() {
		try {
			List<BlackboardFechamentoNotaOperacaoVO> operacoes = getFacadeFactory().getBlackboardFechamentoNotaOperacaoInterfaceFacade().consultarFechamentoNotaOperacaoNaoExecutado();
			if (Uteis.isAtributoPreenchido(operacoes)) {
				getAplicacaoControle().getProgressBarFechamentoNota().setMaxValue(operacoes.size());
				getFacadeFactory().getBlackboardFechamentoNotaOperacaoInterfaceFacade().executarOperacaoFechamentoNotaBlackboard(operacoes, getAplicacaoControle().getProgressBarFechamentoNota(), this);
			}
			setApresentarProgressBarFechamentoNota(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public List<BlackboardFechamentoNotaOperacaoVO> getListaFechamentoNotaErro() {
		if (listaFechamentoNotaErro == null) {
			listaFechamentoNotaErro = new ArrayList<>(0);
		}
		return listaFechamentoNotaErro;
	}
	
	public void setListaFechamentoNotaErro(List<BlackboardFechamentoNotaOperacaoVO> listaFechamentoNotaErro) {
		this.listaFechamentoNotaErro = listaFechamentoNotaErro;
	}
	
	public void consultarFechamentoNotaErro() {
		try {
			setListaFechamentoNotaErro(getFacadeFactory().getBlackboardFechamentoNotaOperacaoInterfaceFacade().consultarFechamentoNotaErro());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public DataModelo getDataModeloOperacaoMoodleProcessamentoPendente() {
		if (dataModeloOperacaoMoodleProcessamentoPendente == null) {
			dataModeloOperacaoMoodleProcessamentoPendente = new DataModelo();
		}
		return dataModeloOperacaoMoodleProcessamentoPendente;
	}

	public void setDataModeloOperacaoMoodleProcessamentoPendente(DataModelo dataModeloOperacaoMoodleProcessamentoPendente) {
		this.dataModeloOperacaoMoodleProcessamentoPendente = dataModeloOperacaoMoodleProcessamentoPendente;
	}

	public DataModelo getDataModeloOperacaoMoodleProcessamentoErro() {
		if (dataModeloOperacaoMoodleProcessamentoErro == null) {
			dataModeloOperacaoMoodleProcessamentoErro = new DataModelo();
		}
		return dataModeloOperacaoMoodleProcessamentoErro;
	}

	public void setDataModeloOperacaoMoodleProcessamentoErro(DataModelo dataModeloOperacaoMoodleProcessamentoErro) {
		this.dataModeloOperacaoMoodleProcessamentoErro = dataModeloOperacaoMoodleProcessamentoErro;
	}

	public DataModelo getDataModeloRegistroExecucaoJob() {
		if (dataModeloRegistroExecucaoJob == null) {
			dataModeloRegistroExecucaoJob = new DataModelo();
		}
		return dataModeloRegistroExecucaoJob;
	}

	public void setDataModeloRegistroExecucaoJob(DataModelo dataModeloRegistroExecucaoJob) {
		this.dataModeloRegistroExecucaoJob = dataModeloRegistroExecucaoJob;
	}

	public OperacaoMoodleVO getOperacaoMoodle() {
		if (operacaoMoodle == null) {
			operacaoMoodle = new OperacaoMoodleVO();
		}
		return operacaoMoodle;
	}

	public void setOperacaoMoodle(OperacaoMoodleVO operacaoMoodle) {
		this.operacaoMoodle = operacaoMoodle;
	}

	public void atualizarDadosProcessamentoNotasMoodle() {
		try {
			getDataModeloRegistroExecucaoJob().setValorConsulta(JobsEnum.JOB_OPERACAO_NOTA_MOODLE.getName());
			getFacadeFactory().getOperacaoMoodleInterfaceFacade().carregarDadosOperacoesMoodle(TipoOperacaoMoodleEnum.NOTAS, getDataModeloOperacaoMoodleProcessamentoPendente(), getDataModeloOperacaoMoodleProcessamentoErro());
			getFacadeFactory().getRegistroExecucaoJobFacade().consultarOtimizado(getDataModeloRegistroExecucaoJob());
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerDadosProcessamentoPendente(DataScrollEvent dataScrollEvent) throws Exception {
		try {
			getDataModeloOperacaoMoodleProcessamentoPendente().setPaginaAtual(dataScrollEvent.getPage());
			getDataModeloOperacaoMoodleProcessamentoPendente().setPage(dataScrollEvent.getPage());
			getFacadeFactory().getOperacaoMoodleInterfaceFacade().carregarDadosOperacoesMoodle(TipoOperacaoMoodleEnum.NOTAS, getDataModeloOperacaoMoodleProcessamentoPendente(), "PENDENTE");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerDadosProcessamentoErro(DataScrollEvent dataScrollEvent) throws Exception {
		try {
			getDataModeloOperacaoMoodleProcessamentoErro().setPaginaAtual(dataScrollEvent.getPage());
			getDataModeloOperacaoMoodleProcessamentoErro().setPage(dataScrollEvent.getPage());
			getFacadeFactory().getOperacaoMoodleInterfaceFacade().carregarDadosOperacoesMoodle(TipoOperacaoMoodleEnum.NOTAS, getDataModeloOperacaoMoodleProcessamentoErro(), "ERRO");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerRegistroExecucaoJob(DataScrollEvent dataScrollEvent) throws Exception {
		try {
			getDataModeloRegistroExecucaoJob().setValorConsulta(JobsEnum.JOB_OPERACAO_NOTA_MOODLE.getName());
			getDataModeloRegistroExecucaoJob().setPaginaAtual(dataScrollEvent.getPage());
			getDataModeloRegistroExecucaoJob().setPage(dataScrollEvent.getPage());
			getFacadeFactory().getRegistroExecucaoJobFacade().consultarOtimizado(getDataModeloRegistroExecucaoJob());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void visualizarOperacaoMoodleNotas(String var) {
		setOncompleteModal(Constantes.EMPTY);
		try {
			OperacaoMoodleVO obj = (OperacaoMoodleVO) context().getExternalContext().getRequestMap().get(var);
			if (Uteis.isAtributoPreenchido(obj)) {
				if (!Uteis.isAtributoPreenchido(obj.getNotasRSVO().getNotas())) {
					obj.setNotasRSVO(OperacaoMoodle.converterJsonParaObjetoNotasRSVO(obj.getJsonMoodle()));
				}
				setOperacaoMoodle(obj);
				limparFilterFactory();
				setOncompleteModal("RichFaces.$('panelVisualizarDadosOperacaoMoodle').show();");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparFilterFactory() {
		FilterFactory filter = (FilterFactory) getControlador("FilterFactory");
		try {
			if (Objects.nonNull(filter) && Uteis.isAtributoPreenchido(filter.getMapFilter()) && filter.getMapFilter().containsKey("email_notasRSVO")) {
				filter.getMapFilter().get("email_notasRSVO").setFiltro(Constantes.EMPTY);
			}
		} finally {
			if (Objects.nonNull(filter)) {
				filter = null;
			}
		}
	}

	public String getNivelEducacionalApresentar() {
		if(nivelEducacionalApresentar == null){
			nivelEducacionalApresentar = "";
		}
		return nivelEducacionalApresentar;
	}

	public void setNivelEducacionalApresentar(String nivelEducacionalApresentar) {
		this.nivelEducacionalApresentar = nivelEducacionalApresentar;
	}

	public PessoaVO getSupervisor() {
		if(supervisor == null){
			supervisor = new PessoaVO();
		}
		return supervisor;
	}

	public void setSupervisor(PessoaVO supervisor) {
		this.supervisor = supervisor;
	}
}