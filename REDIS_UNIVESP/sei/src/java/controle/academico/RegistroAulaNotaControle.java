package controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CalendarioLancamentoNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoCoordenadorVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HistoricoNotaParcialVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaDisciplinaNotaParcialVO;
import negocio.comuns.academico.TurmaDisciplinaNotaTituloVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.administrativo.FuncionarioGrupoDestinatariosVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.secretaria.enumeradores.FormaReplicarNotaOutraDisciplinaEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("RegistroAulaNotaControle")
@Scope("viewScope")
@Lazy
public class RegistroAulaNotaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaNomeMatriculaAluno;
	private List<RegistroAulaVO> listaAulas;
	private List<HistoricoVO> listaNotas;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private HashMap<String, Integer> hashHistorico;
	private HashMap<String, Double> hashFrequencia;
	private List<HistoricoVO> listaAnterior;
	private List<HistoricoVO> alunosTurma;
	private Boolean listasIguais;
	// Atributos usados visão administrativa
	private List<SelectItem> listaSelectItemProfessores;
	private PessoaVO professor;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private Boolean permiteGravarVisaoCoordenador;
	private Integer qtdeNotasLancadas;
	private Integer qtdeNotasNaoLancadas;
	private Integer qtdeFaltasLancadas;
	private Boolean controlarMarcarDesmarcarTodos;
	private Boolean calcularMediaAoGravar;
	private Integer linha;
	private String ano;
	private String semestre;
	private List<SelectItem> listaSelectItemSemestre;
	private RegistroAulaVO registroAulaVO;
	private Boolean historicoAlterado;
	private TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO;
	private Boolean permiteInformarTituloNota;
	private FormaReplicarNotaOutraDisciplinaEnum formaReplicarNotaOutraDisciplina;	
	private List<SelectItem> listaSelectItemFormaReplicarNotaOutraDisciplina;
	private TipoNotaConceitoEnum tipoNotaReplicar;
	private TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcial;
	private List<HistoricoNotaParcialVO> historicoNotaParcialVOs;
	private HistoricoVO historicoTemporarioVO;
	private List<HistoricoNotaParcialVO> historicoNotaParcialGeralVOs;
	private String tipoNotaUsar;
	private String tituloNotaApresentar;

	public RegistroAulaNotaControle() {
		//verificarPermitirCalcularMediaAoGravar();
	}

	public void verificarPermitirCalcularMediaAoGravar() {
		try {
			this.setCalcularMediaAoGravar(getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getCalcularMediaAoGravar());
		} catch (Exception e) {
			this.setCalcularMediaAoGravar(null);
		}
	}
	
	@PostConstruct
	public void init() {
		inicializarDadosOrigemExterna();
	}
	
	

	private void inicializarDadosOrigemExterna() {
		try {
			TurmaVO turma = (TurmaVO) context().getExternalContext().getSessionMap().get("turmaVO");
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getSessionMap().get("disciplinaVO");
			if(Uteis.isAtributoPreenchido(turma) && Uteis.isAtributoPreenchido(disciplina)) {
				montarListaTurmas();
				setTurmaVO(turma);
				montarListaDisciplinas();
				setDisciplinaVO(disciplina); 
				montarComboboxConfiguracaoAcademico();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally {
			context().getExternalContext().getSessionMap().remove("turmaVO");
			context().getExternalContext().getSessionMap().remove("disciplinaVO");
		}
	}
		
	public void montarDadosConfiguracaoAcademicoVisaoProfessor() {
		try {
			if(getTurmaVO().getCurso().getPeriodicidade().equals("IN")) {
				boolean isTurmaDisciplinaEOnline = getFacadeFactory().getTurmaDisciplinaFacade().validarTurmaDisciplinaEOnline(getTurmaVO(), getDisciplinaVO(), getUsuarioLogado());
				if(isTurmaDisciplinaEOnline) {
					setOncompleteModal("RichFaces.$('panelAvisoLancamentoAulaNota').show()");
					return;
				}
			}
			montarComboboxConfiguracaoAcademico();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	

	public String redirecionarTelaRegistroDeAulaENota() {
		context().getExternalContext().getSessionMap().put("turmaVO", getTurmaVO());
		context().getExternalContext().getSessionMap().put("disciplinaVO", getDisciplinaVO());
		removerControleMemoriaFlash("RegistroAulaNotaControle");
		removerControleMemoriaTela("RegistroAulaNotaControle");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registrarNotaProfessor.xhtml");
	}
	
	
	public String novo() {
		//removerObjetoMemoria(this);
		try {
			setTurmaVO(null);
			setDisciplinaVO(null);
			setConfiguracaoAcademicoVO(null);
			getListaSelectItemDisciplina().clear();
			getListaNomeMatriculaAluno().clear();
			getListaAulas().clear();
			getListaNotas().clear();
			getHashFrequencia().clear();
			getHashHistorico().clear();
			verificarPermitirCalcularMediaAoGravar();
			montarListaTurmas();
			limparMensagem();
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String getUrlFotoAluno() throws Exception {
		FrequenciaAulaVO frequenciaAulaVO = (FrequenciaAulaVO) getRequestMap().get("frequenciaItens");
		if (frequenciaAulaVO.getMatricula().getAluno().getUrlFotoAluno() == null || frequenciaAulaVO.getMatricula().getAluno().getUrlFotoAluno().trim().isEmpty()) {
			if (frequenciaAulaVO.getMatricula().getAluno().getArquivoImagem().getCodigo() > 0) {
				frequenciaAulaVO.getMatricula().getAluno().setUrlFotoAluno(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM.getValue() + "/" + frequenciaAulaVO.getMatricula().getAluno().getArquivoImagem().getNome());
			} else {
				frequenciaAulaVO.getMatricula().getAluno().getArquivoImagem().setNome("foto_usuario.png");
				frequenciaAulaVO.getMatricula().getAluno().setUrlFotoAluno(UteisJSF.getCaminhoWeb() + "resources/imagens/visao/" + frequenciaAulaVO.getMatricula().getAluno().getArquivoImagem().getNome());
			}
		}
		return frequenciaAulaVO.getMatricula().getAluno().getUrlFotoAluno();
	}

	public void consultarAlunos() {
		try {
			if(getUsuarioLogado().getIsApresentarVisaoProfessor()){
				getProfessor().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
				getProfessor().setNome(getUsuarioLogado().getPessoa().getNome());
			}			
			getFacadeFactory().getRegistroAulaFacade().validarDadosRegistroAulaNotaTurma(getTurmaVO().getCodigo(), getProfessor().getCodigo(), getDisciplinaVO().getCodigo(), getConfiguracaoAcademicoVO(), getTurmaVO().getPeriodicidade(), getAno(), getSemestre());
			if(getPossuiDiversidadeConfiguracaoAcademico()){
				throw new Exception("Existem alunos vinculados a CONFIGURAÇÃO ACADÊMICA diferente uma das outras, para usar este recurso deve definida apenas uma configuração acadêmica para todos os aluno.");
			}
			getListaAulas().clear();
			getListaNotas().clear();
			getAlunosTurma().clear();
			montarListaAulas();
			ordenarListaFrequencia(getListaAulas());
			setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
			if (!getTurmaVO().getTurmaAgrupada()) {
				getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getTurmaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
			}	
			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
			montarListaNotas();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			getListaNomeMatriculaAluno().clear();
		}
	}

	public Boolean verificarExistenciaRegistroAula(String matricula, Integer turma, Integer disciplina, Integer professor, String semestre, String ano) throws Exception {
		try {
			return getFacadeFactory().getRegistroAulaFacade().consultarExistenciaRegistroAula(matricula, turma, disciplina, professor, semestre, ano);
		} catch (Exception e) {
			throw e;
		}
	}

	public void consultarAlunosVisaoCoordenador() {
		try {
			
			getListaAulas().clear();
			getListaNotas().clear();
			getAlunosTurma().clear();
			montarListaAulasVisaoCoordenador();
			ordenarListaFrequencia(getListaAulas());
			setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
			getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getTurmaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
			montarListaNotas();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			getListaNomeMatriculaAluno().clear();
		}
	}

	public void montarListaAulasVisaoCoordenador() throws Exception {
		if(getPossuiDiversidadeConfiguracaoAcademico()){
			throw new Exception("Existem alunos vinculados a CONFIGURAÇÃO ACADÊMICA diferente uma das outras, para usar este recurso deve definida apenas uma configuração acadêmica para todos os aluno.");
		}
		setListaAulas(getFacadeFactory().getRegistroAulaFacade().executarMontagemDadosRegistroAulaENota(0, getTurmaVO(), getDisciplinaVO(), "", "", getProfessor(), true, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), getTrazerAlunoPendenteFinanceiramente(), getConfiguracaoAcademicoVO(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados()));
	}

	public void aplicarPresencaTodasAulas() {
		FrequenciaAulaVO frequenciaAlterar = (FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens");
		for (RegistroAulaVO registros : this.listaAulas) {
			for (FrequenciaAulaVO freq : registros.getFrequenciaAulaVOs()) {
				if (freq.getMatriculaPeriodoTurmaDisciplina().equals(frequenciaAlterar.getMatriculaPeriodoTurmaDisciplina()) && !freq.getPresente()) {
					freq.setPresente(Boolean.TRUE);
					setHistoricoAlterado(true);
					break;
				}
			}
		}
	}

	public void aplicarFaltaTodasAulas() {
		FrequenciaAulaVO frequenciaAlterar = (FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens");
		for (RegistroAulaVO registros : this.listaAulas) {
			for (FrequenciaAulaVO freq : registros.getFrequenciaAulaVOs()) {
				if (freq.getMatriculaPeriodoTurmaDisciplina().equals(frequenciaAlterar.getMatriculaPeriodoTurmaDisciplina()) && freq.getPresente()) {
					freq.setPresente(Boolean.FALSE);
					setHistoricoAlterado(true);
					break;
				}
			}
		}
	}

	public void removerAbonoFrequenciaAula() {
		try {
			FrequenciaAulaVO frequenciaAlterar = (FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens");
			frequenciaAlterar.setAbonado(Boolean.FALSE);
			frequenciaAlterar.setJustificado(Boolean.FALSE);
			frequenciaAlterar.setPresente(Boolean.TRUE);
			frequenciaAlterar.setFrequenciaOculta(Boolean.FALSE);
			frequenciaAlterar.setDisciplinaAbonoVO(null);
			frequenciaAlterar.setEditavel(Boolean.TRUE);
			setHistoricoAlterado(true);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunosVisaoAdministrativa() {
		try {			
			setLinha(0);
			getListaAulas().clear();
			getListaNotas().clear();
			getAlunosTurma().clear();	
			getFacadeFactory().getRegistroAulaFacade().validarDadosRegistroAulaNotaTurma(getTurmaVO().getCodigo(), getProfessor().getCodigo(), getDisciplinaVO().getCodigo(), getConfiguracaoAcademicoVO(), getTurmaVO().getPeriodicidade(), getAno(), getSemestre());
			if(getPossuiDiversidadeConfiguracaoAcademico()){
				throw new Exception("Existem alunos vinculados a CONFIGURAÇÃO ACADÊMICA diferente uma das outras, para usar este recurso deve definida apenas uma configuração acadêmica para todos os aluno.");
			}
			setListaAulas(getFacadeFactory().getRegistroAulaFacade().executarMontagemDadosRegistroAulaENota(getUnidadeEnsinoLogado().getCodigo(), getTurmaVO(), getDisciplinaVO(), getSemestre(), getAno(), getProfessor(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), getTrazerAlunoPendenteFinanceiramente(), getConfiguracaoAcademicoVO(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados()));			
			ordenarListaFrequencia(getListaAulas());
			setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().realizarCriacaoTurmaDisciplinaNotaTituloComBaseTipoTurma(getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), this.getConfiguracaoAcademicoVO(), getUsuarioLogado());
			montarListaNotas();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			getListaNomeMatriculaAluno().clear();
		}
	}

	public void montarListaNotas() throws Exception {
		for (RegistroAulaVO registroAulaVO : getListaAulas()) {
			for (FrequenciaAulaVO frequenciaAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
				if (!getHashHistorico().containsKey(frequenciaAulaVO.getHistoricoVO().getMatricula().getMatricula())) {
					getListaNotas().add(frequenciaAulaVO.getHistoricoVO());
					getHashHistorico().put(frequenciaAulaVO.getHistoricoVO().getMatricula().getMatricula(), frequenciaAulaVO.getHistoricoVO().getCodigo());
				}
				frequenciaAulaVO.setCssInputTextRegistroAulaNota("camposReduzidosVisaoProfessor");
				frequenciaAulaVO.setCssSelectBooleanCheckboxRegistroAulaNota("camposReduzidosVisaoProfessor");
			}		
		}
		montarListaOpcoesNotas(getConfiguracaoAcademicoVO());
	}

	public void ordenarListaFrequencia(List<RegistroAulaVO> listaRegistroAula) throws Exception {
		for (RegistroAulaVO reg : listaRegistroAula) {
			Ordenacao.ordenarLista(reg.getFrequenciaAulaVOs(), "ordenacaoSemAcentuacaoNome");
		}
	}

	public void montarListaAulas() throws Exception {

		getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
		if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			setListaAulas(getFacadeFactory().getRegistroAulaFacade().executarMontagemDadosRegistroAulaENota(0, getTurmaVO(), getDisciplinaVO(), getSemestre(), getAno(), getProfessor(), true, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), getTrazerAlunoPendenteFinanceiramente(), getConfiguracaoAcademicoVO(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados()));
		} else {
			setListaAulas(getFacadeFactory().getRegistroAulaFacade().executarMontagemDadosRegistroAulaENota(0, getTurmaVO(), getDisciplinaVO(), getSemestre(), getAno(), getUsuarioLogado().getPessoa(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), getTrazerAlunoPendenteFinanceiramente(), getConfiguracaoAcademicoVO(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados()));
		}
		limparMensagem();			
	}

	public Boolean getPermiteLancamentoAulaFutura() {
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return getLoginControle().getPermissaoAcessoMenuVO().getPermiteLancamentoAulaFuturaProfessor();
		}
		if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			return getLoginControle().getPermissaoAcessoMenuVO().getPermiteLancamentoAulaFuturaCoordenador();
		}
		return getLoginControle().getPermissaoAcessoMenuVO().getPermiteLancamentoAulaFutura();
	}

	public void persistir() {
		try {
			realizarCloneListaHistorico();			
			Boolean erro = false;
			confirmarPersistir();
			getFacadeFactory().getRegistroAulaNotaFacade().persistir(getListaAulas(), getConfiguracaoAcademicoVO(), getPermiteLancamentoAulaFutura(), getUsuarioLogado());
//			validarDadosComparacaoListaHistorico();
			if (erro) {
				setMensagemID("msg_textoPadraoBancoCurriculum_inexistente");
			} else {
				setHistoricoAlterado(false);
				setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarUsuarioLogadoGrupoEmail(TurmaVO turmaVO) throws Exception {
		FuncionarioVO funcionarioVO = (getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		Boolean jaPossuiFuncionario = false;
		for (FuncionarioGrupoDestinatariosVO obj : turmaVO.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs()) {
			if (obj.getFuncionario().getCodigo().equals(funcionarioVO.getCodigo())) {
				jaPossuiFuncionario = true;
			}
		}
		if (!jaPossuiFuncionario) {
			FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO = new FuncionarioGrupoDestinatariosVO();
			funcionarioGrupoDestinatariosVO.setFuncionario(funcionarioVO);
			funcionarioGrupoDestinatariosVO.setGrupoDestinatarios(turmaVO.getGrupoDestinatarios().getCodigo());
			turmaVO.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs().add(funcionarioGrupoDestinatariosVO);
		}
	}

	@SuppressWarnings("unchecked")
	public void adicionarCoordenadoresCursoGrupoEmail(TurmaVO turmaVO) throws Exception {
		turmaVO.getCurso().setCursoCoordenadorVOs(getFacadeFactory().getCursoCoordenadorFacade().consultarPorCodigoCurso(turmaVO.getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		for (CursoCoordenadorVO obj : turmaVO.getCurso().getCursoCoordenadorVOs()) {
			if (obj.getTurma().getCodigo() == 0) {
				FuncionarioVO funcionarioVO = (getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getFuncionario().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO = new FuncionarioGrupoDestinatariosVO();
				funcionarioGrupoDestinatariosVO.setFuncionario(funcionarioVO);
				funcionarioGrupoDestinatariosVO.setGrupoDestinatarios(turmaVO.getGrupoDestinatarios().getCodigo());
				turmaVO.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs().add(funcionarioGrupoDestinatariosVO);
			} else {
				if (obj.getTurma().getCodigo().equals(turmaVO.getCodigo())) {
					FuncionarioVO funcionarioVO = (getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getFuncionario().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO = new FuncionarioGrupoDestinatariosVO();
					funcionarioGrupoDestinatariosVO.setFuncionario(funcionarioVO);
					funcionarioGrupoDestinatariosVO.setGrupoDestinatarios(turmaVO.getGrupoDestinatarios().getCodigo());
					turmaVO.getGrupoDestinatarios().getListaFuncionariosGrupoDestinatariosVOs().add(funcionarioGrupoDestinatariosVO);
				}
			}
		}
	}

	public String gerarMensagemRegistroAula2(String conteudoAula) throws Exception {
		StringBuilder sb = new StringBuilder();
		// sb.append("\r <br />");
		// sb.append("Este é um email automático enviado pelo SEI, informando que: \r <br />");
		// sb.append("O Usuário \"" + getUsuarioLogado().getNome() +
		// "\" \r <br />");
		// sb.append("Registrou aula no dia: " + Uteis.getData(new Date(),
		// "dd/MM/yyyy") + " <br />");
		// sb.append("Com o Conteúdo: \"" + conteudoAula + "\" <br />");
		// sb.append("O PDF contendo o Diário está em anexo. <br />");
		// sb.append("\r <br />");

		// sb.append("\r <br />");
		// sb.append("Este é um email automático enviado pelo SEI, informando que: \r <br />");
		// sb.append("O Usuário \"" +
		sb.append(UteisJSF.internacionalizar("msg_RegistroAula_envioMensagem1"));
		sb.append(getUsuarioLogado().getNome());
		// + "\" \r <br />");
		// sb.append("Registrou aula no dia: " +
		sb.append(UteisJSF.internacionalizar("msg_RegistroAula_envioMensagem2"));
		sb.append(Uteis.getData(new Date(), "dd/MM/yyyy"));
		// + " <br />");
		// sb.append("Com o Conteúdo: \""
		sb.append(UteisJSF.internacionalizar("msg_RegistroAula_envioMensagem3"));
		sb.append(conteudoAula);
		sb.append(UteisJSF.internacionalizar("msg_RegistroAula_envioMensagem4"));
		// + "\" <br />");
		// sb.append("O PDF contendo o Diário está em anexo. <br />");
		// sb.append("\r <br />");
		return sb.toString();
	}

	public String gerarMensagemRegistroAula(String conteudoAula) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("\r <br />");
		sb.append("Este é um email automático enviado pelo SEI, informando que: \r <br />");
		sb.append("O Usuário \"").append(getUsuarioLogado().getNome()).append("\" \r <br />");
		sb.append("Registrou aula no dia: ").append(Uteis.getData(new Date(), "dd/MM/yyyy")).append(" <br />");
		sb.append("Com o Conteúdo: \"").append(conteudoAula).append("\" <br />");
		sb.append("O PDF contendo o Diário está em anexo. <br />");
		sb.append("\r <br />");
		return getMensagemFormatada(sb.toString());
	}

	public String getMensagemFormatada(String mensagem) throws Exception {
		String temp = getConfiguracaoGeralPadraoSistema().getMensagemPadrao();
		if (temp.equals("")) {
			return mensagem;
		}
		String caminho = getCaminhoPastaWeb();
		temp = temp.replaceAll("http://localhost:8080/SEI/", caminho);
		temp = temp.replace("<TEXTO PADRAO>", mensagem);
		return temp;
	}

	public String novoVisaoProfessor() throws Exception {
		try {
//			removerObjetoMemoria(this);
			verificarPermitirCalcularMediaAoGravar();
			montarListaTurmas();			
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);			
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarAulaNotaProfessor.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public void montarListaTurmas() {
		List<TurmaVO> listaTurmas = null;
		String value = null;
		String nomeCurso = null;
		try {
			listaTurmas = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : listaTurmas) {
				if (turmaVO.getTurmaAgrupada()) {
					value = turmaVO.getIdentificadorTurma() + " - Turno " + turmaVO.getTurno().getNome();
					getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), value));
				} else {
					if (turmaVO.getCurso().getNivelEducacionalPosGraduacao()) {
						nomeCurso = turmaVO.getCurso().getNome();
						if (!nomeCurso.equals("")) {
							nomeCurso += " - ";
						}
						value = turmaVO.getIdentificadorTurma() + " : " + nomeCurso + turmaVO.getTurno().getNome();
						getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), value));
					}
				}
				removerObjetoMemoria(turmaVO);
			}
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		} finally {
			Uteis.liberarListaMemoria(listaTurmas);
			value = null;
			nomeCurso = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultarTurmaAgrupadaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, false, true, false, getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo());		
	}

	public void limparDadosVisaoCoordenador() {
		getListaNomeMatriculaAluno().clear();
		getListaAulas().clear();
		getListaNotas().clear();
		getHashFrequencia().clear();
		getHashHistorico().clear();
		montarComboboxConfiguracaoAcademico();
	}

	public String novoVisaoCoordenador() throws Exception {
		try {			
			setTurmaVO(null);
			setDisciplinaVO(null);
			setConfiguracaoAcademicoVO(null);
			getListaSelectItemDisciplina().clear();
			getListaNomeMatriculaAluno().clear();
			getListaSelectItemTurma().clear();
			getListaAulas().clear();
			getListaNotas().clear();
			getHashFrequencia().clear();
			getHashHistorico().clear();
			verificarPermitirCalcularMediaAoGravar();
			montarListaSelectItemTurmaCoordenador();
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("registrarAulaNotaCoordenador.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public void montarListaSelectItemTurmaCoordenador() {
		try {
			montarListaSelectItemTurmaCoordenador("");
		} catch (Exception e) {
			// System.out.println("Erro RegistroAulaNotaControle.montarListaSelectItemTurmaCoordenador: "
			// + e.getMessage());
		}
	}

	public void montarListaSelectItemTurmaCoordenador(String prm) throws Exception {
		try {
			if (getListaSelectItemTurma().isEmpty()) {
				List<TurmaVO> resultadoConsulta = consultarTurmaCoordenador();
				setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public List<TurmaVO> consultarTurmaCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenador(getUsuarioLogado().getPessoa().getCodigo(), true, false, true, getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		// return
		// getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorComMatriculaPeriodoAtiva(getUsuarioLogado().getPessoa().getCodigo(),
		// getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void montarListaSelectItemProfessoresTurmaCoordenador() {
		try {			
			montarListaSelectItemProfessoresTurmaCoordenador("");
		} catch (Exception e) {
			// System.out.println("Erro RegistroAulaNotaControle.montarListaSelectItemProfessoresTurmaCoordenador: "
			// + e.getMessage());
		}
	}

	public void montarListaSelectItemProfessoresTurmaCoordenador(String prm) throws Exception {
		try {
			limparDadosVisaoCoordenador();
			if (getTurmaVO().getCodigo() != 0) {
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
			}else {
				setTurmaVO(new TurmaVO());
			}
			getProfessor().setCodigo(0);
			getDisciplinaVO().setCodigo(0);
			getListaSelectItemDisciplina().clear();
			if (Uteis.isAtributoPreenchido(turmaVO)) {
				List<PessoaVO> resultadoConsulta = consultarProfessoresTurmaCoordenador();
				getListaSelectItemProfessores().clear();
				setListaSelectItemProfessores(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PessoaVO> consultarProfessoresTurmaCoordenador() throws Exception {
		return getFacadeFactory().getPessoaFacade().consultarProfessoresDaTurmaPorTurmaAgrupada(getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getSemestre(),  getAno(), false, getUsuarioLogado());				
	}

	public void montarListaDisciplinasVisaoCoordenador() throws Exception {
		getListaNotas().clear();
		getListaAulas().clear();
		setHistoricoAlterado(true);
		List<DisciplinaVO> listaDisciplinas = consultarDisciplinaProfessorTurmaVisaoCoordenador();
		setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(listaDisciplinas, "codigo", "nome"));
	}

	@SuppressWarnings("unchecked")
	public List<DisciplinaVO> consultarDisciplinaProfessorTurmaVisaoCoordenador() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaAgrupada(getProfessor().getCodigo(), getTurmaVO().getCodigo(),  getSemestre(), getAno(),  false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado(), false);				
	}

	public void montarListaDisciplinas() throws Exception {
		setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
		getListaNotas().clear();
		getListaAulas().clear();
		setHistoricoAlterado(true);
		setDisciplinaVO(null);
		List<DisciplinaVO> listaDisciplinas = consultarDisciplinaProfessorTurma();
		setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(listaDisciplinas, "codigo", "nome"));
		limparMensagem();	
		montarComboboxConfiguracaoAcademico();
	}

	@SuppressWarnings("unchecked")
	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaAgrupada(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getSemestre(), getAno(),  false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado(), false);				
	}

	public void calcularMedia() {
		try {
			getFacadeFactory().getRegistroAulaNotaFacade().calcularMedia(getListaAulas(), getConfiguracaoAcademicoVO(), getUsuarioLogado());	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// ------------------------ Métodos usados na visão administrativa
	// Carlos
	public void carregarTurma() {
		try {
			setProfessor(null);
			getListaAulas().clear();
			getListaSelectItemDisciplina().clear();
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurmaEspecifico(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), true, false, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setDisciplinaVO(null);
			setConfiguracaoAcademicoVO(null);
			montarListaProfessores();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurmaEspecifico(obj.getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			montarListaProfessores();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaProfessores() throws Exception {
		if (this.getTurmaVO().getCodigo().intValue() == 0) {
			getListaSelectItemProfessores().clear();
			return;
		}
		List<PessoaVO> listaProfessoresVOs = new ArrayList<PessoaVO>(0);
		Iterator i = null;
		try {
			listaProfessoresVOs = getFacadeFactory().getPessoaFacade().consultarPorTurmaTurmaAgrupada(getTurmaVO().getCodigo().intValue(), getAno() , getSemestre(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), false);
			i = listaProfessoresVOs.iterator();
			getListaSelectItemProfessores().clear();
			getListaSelectItemProfessores().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PessoaVO pessoa = (PessoaVO) i.next();
				getListaSelectItemProfessores().add(new SelectItem(pessoa.getCodigo().intValue(), pessoa.getNome()));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) getListaSelectItemProfessores(), ordenador);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(listaProfessoresVOs);
			i = null;
		}
	}

	public void montarListaDisciplinaVisaoAdministrativa() throws Exception {
		getListaNotas().clear();
		getListaAulas().clear();
		setHistoricoAlterado(true);
		setDisciplinaVO(null);
		List<DisciplinaVO> listaDisciplinas = consultarDisciplinaProfessorTurmaVisaoAdmnistrativa();
		setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(listaDisciplinas, "codigo", "nome"));
		montarComboboxConfiguracaoAcademico();
		setMensagemID("msg_dados_consultados");
	}

	@SuppressWarnings("unchecked")
	public List<DisciplinaVO> consultarDisciplinaProfessorTurmaVisaoAdmnistrativa() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaAgrupada(getProfessor().getCodigo(), getTurmaVO().getCodigo(), getSemestre(), getAno(), false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado(), false);
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), true, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), true, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), true, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), true, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public Boolean getIsApresentarComboDisciplina() {
		return Uteis.isAtributoPreenchido(getProfessor());
	}

	public Boolean getIsApresentarComboProfessor() {
		return Uteis.isAtributoPreenchido(getTurmaVO());
	}

	public void verificarPermissaoParaGravarRegistroAulaNotaVisaoCoordenador() {
		try {
			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				ControleAcesso.incluir("RegistroAulaNota", getUsuarioLogado());
				setPermiteGravarVisaoCoordenador(true);
			}
		} catch (Exception e) {
			setPermiteGravarVisaoCoordenador(false);
		}
	}

	public void calcularQtdeNotasLancadasNaoLancadasFaltas() throws Exception {
		try {
			setQtdeFaltasLancadas(null);
			setQtdeNotasLancadas(null);
			setQtdeNotasNaoLancadas(null);
			if (!getListaAulas().isEmpty()) {
				Map<String, MatriculaVO> matriculaJaContabilizadaNotaLancada = new HashMap<String, MatriculaVO>(0);
				Map<String, MatriculaVO> matriculaJaContabilizadaNotaNaoLancada = new HashMap<String, MatriculaVO>(0);
				for (RegistroAulaVO registroAulaVO : getListaAulas()) {
					boolean faltaContada = false;
					for (FrequenciaAulaVO frequenciaAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
						if (Uteis.isAtributoPreenchido(frequenciaAulaVO.getHistoricoVO().getMediaFinal()) && !matriculaJaContabilizadaNotaLancada.containsKey(frequenciaAulaVO.getMatricula().getMatricula())) {
							setQtdeNotasLancadas(getQtdeNotasLancadas() + 1);
							matriculaJaContabilizadaNotaLancada.put(frequenciaAulaVO.getMatricula().getMatricula(), frequenciaAulaVO.getMatricula());
						} else if (!Uteis.isAtributoPreenchido(frequenciaAulaVO.getHistoricoVO().getMediaFinal()) && !matriculaJaContabilizadaNotaNaoLancada.containsKey(frequenciaAulaVO.getMatricula().getMatricula())) {
							matriculaJaContabilizadaNotaNaoLancada.put(frequenciaAulaVO.getMatricula().getMatricula(), frequenciaAulaVO.getMatricula());
						}
						if (!faltaContada && !frequenciaAulaVO.getPresente() && !frequenciaAulaVO.getFrequenciaOculta()) {
							setQtdeFaltasLancadas(getQtdeFaltasLancadas() + 1);
						}
					}
				}
				/**
				 * Responsável por realizar a contagem das matrículas cuja nota
				 * ainda não foi lançada levando em consideração as matrículas
				 * cuja nota já foi lançada.
				 */
				for (MatriculaVO obj : matriculaJaContabilizadaNotaNaoLancada.values()) {
					if (!matriculaJaContabilizadaNotaLancada.containsKey(obj.getMatricula())) {
						setQtdeNotasNaoLancadas(getQtdeNotasNaoLancadas() + 1);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void confirmarPersistir() throws Exception {
		try {
			if (getCalcularMediaAoGravar()) {
				getFacadeFactory().getRegistroAulaNotaFacade().calcularMedia(getListaAulas(), getConfiguracaoAcademicoVO(), getUsuarioLogado());
			}
			calcularQtdeNotasLancadasNaoLancadasFaltas();
			setMensagemDetalhada("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaNomeMatriculaAluno(List<MatriculaPeriodoTurmaDisciplinaVO> listaNomeMatriculaAluno) {
		this.listaNomeMatriculaAluno = listaNomeMatriculaAluno;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaNomeMatriculaAluno() {
		if (listaNomeMatriculaAluno == null) {
			listaNomeMatriculaAluno = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaNomeMatriculaAluno;
	}

	public void setListaAulas(List<RegistroAulaVO> listaAulas) {
		this.listaAulas = listaAulas;
	}

	public List<RegistroAulaVO> getListaAulas() {
		if (listaAulas == null) {
			listaAulas = new ArrayList<RegistroAulaVO>(0);
		}
		return listaAulas;
	}

	public Integer getTamanhoListaAulas() {
		return getListaAulas().size();
	}

	public Integer getTamanhoListaFrequencia() {
		if (!getListaAulas().isEmpty()) {
			if (!getUltimoRegistroAula().getFrequenciaAulaVOs().isEmpty()) {
				return getUltimoRegistroAula().getFrequenciaAulaVOs().size();
			}
		}
		return 0;
	}

	public RegistroAulaVO getUltimoRegistroAula() {
		if(Uteis.isAtributoPreenchido(getListaAulas())) {
			return getListaAulas().get(getListaAulas().size()-1);
		}
		 return new RegistroAulaVO();
	}

	public Integer getLinha() {
		if (linha == null) {
			linha = 0;
		}
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	public String getFocusProximoCampo() {
		if ((getLinha() + 1) == (getTamanhoListaFrequencia() - 1)) {
			return "form:turma";
		}
		return "form:dtAulas:" + (getTamanhoListaAulas() - 1) + ":dtFrequencia:" + (getLinha() + 1) + ":nota";
	}

	public List<HistoricoVO> getListaNotas() {
		if (listaNotas == null) {
			listaNotas = new ArrayList<HistoricoVO>(0);
		}
		return listaNotas;
	}

	public void setListaNotas(List<HistoricoVO> listaNotas) {
		this.listaNotas = listaNotas;
	}

	public HashMap<String, Integer> getHashHistorico() {
		if (hashHistorico == null) {
			hashHistorico = new HashMap<String, Integer>(0);
		}
		return hashHistorico;
	}

	public void setHashHistorico(HashMap<String, Integer> hashHistorico) {
		this.hashHistorico = hashHistorico;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setHashFrequencia(HashMap<String, Double> hashFrequencia) {
		this.hashFrequencia = hashFrequencia;
	}

	public HashMap<String, Double> getHashFrequencia() {
		if (hashFrequencia == null) {
			hashFrequencia = new HashMap<String, Double>(0);
		}
		return hashFrequencia;
	}

	public List<HistoricoVO> getAlunosTurma() {
		if (alunosTurma == null) {
			alunosTurma = new ArrayList<HistoricoVO>(0);
		}
		return alunosTurma;
	}

	public void setAlunosTurma(List<HistoricoVO> alunosTurma) {
		this.alunosTurma = alunosTurma;
	}

	public void validarDadosComparacaoListaHistorico() {
		setListasIguais(getListaAnterior().equals(getListaNotas()));
	}

	public Boolean getApresentarAvisoGravadosComSucesso() {
		return (!getHistoricoAlterado() && !getListaAulas().isEmpty());
	}

	public Boolean getApresentarAvisoDadosNaoGravados() {
		return (getHistoricoAlterado() && !getListaAulas().isEmpty());
	}

	public Boolean getListasIguais() {
		if (listasIguais == null) {
			listasIguais = Boolean.FALSE;
		}
		return listasIguais;
	}

	/**
	 * @param listasIguais
	 *            the listasIguais to set
	 */
	public void setListasIguais(Boolean listasIguais) {
		this.listasIguais = listasIguais;
	}

	/**
	 * @return the listaAnterior
	 */
	public List<HistoricoVO> getListaAnterior() {
		if (listaAnterior == null) {
			listaAnterior = new ArrayList<HistoricoVO>(0);
		}
		return listaAnterior;
	}

	/**
	 * @param listaAnterior
	 *            the listaAnterior to set
	 */
	public void setListaAnterior(List<HistoricoVO> listaAnterior) {
		this.listaAnterior = listaAnterior;
	}

	public void realizarCloneListaHistorico() throws CloneNotSupportedException {
		getListaAnterior().clear();
		for (HistoricoVO obj : getListaNotas()) {
			getListaAnterior().add((HistoricoVO) obj.clone());
		}
	}

	public List<SelectItem> getListaSelectItemProfessores() {
		if (listaSelectItemProfessores == null) {
			listaSelectItemProfessores = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProfessores;
	}

	public void setListaSelectItemProfessores(List<SelectItem> listaSelectItemProfessores) {
		this.listaSelectItemProfessores = listaSelectItemProfessores;
	}

	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public Boolean getPermiteGravarVisaoCoordenador() {
		if (permiteGravarVisaoCoordenador == null) {
			verificarPermissaoParaGravarRegistroAulaNotaVisaoCoordenador();
		}
		return permiteGravarVisaoCoordenador;
	}

	public void setPermiteGravarVisaoCoordenador(Boolean permiteGravarVisaoCoordenador) {
		this.permiteGravarVisaoCoordenador = permiteGravarVisaoCoordenador;
	}

	public String getNomeColunas() {
		return "colunaMatricula,colunaNomeAluno,abono,colunaPresenca,colunaMediaFinal,colunaSituacao";
	}

	public Integer getQtdeFaltasLancadas() {
		if (qtdeFaltasLancadas == null) {
			qtdeFaltasLancadas = 0;
		}
		return qtdeFaltasLancadas;
	}

	public void setQtdeFaltasLancadas(Integer qtdeFaltasLancadas) {
		this.qtdeFaltasLancadas = qtdeFaltasLancadas;
	}

	public Integer getQtdeNotasLancadas() {
		if (qtdeNotasLancadas == null) {
			qtdeNotasLancadas = 0;
		}
		return qtdeNotasLancadas;
	}

	public void setQtdeNotasLancadas(Integer qtdeNotasLancadas) {
		this.qtdeNotasLancadas = qtdeNotasLancadas;
	}

	public Integer getQtdeNotasNaoLancadas() {
		if (qtdeNotasNaoLancadas == null) {
			qtdeNotasNaoLancadas = 0;
		}
		return qtdeNotasNaoLancadas;
	}

	public void setQtdeNotasNaoLancadas(Integer qtdeNotasNaoLancadas) {
		this.qtdeNotasNaoLancadas = qtdeNotasNaoLancadas;
	}

	public String getQtdeNotasLancadas_Apresentar() {
		return getQtdeNotasLancadas().toString();
	}

	public String getQtdeNotasNaoLancadas_Apresentar() {
		return getQtdeNotasNaoLancadas().toString();
	}

	public String getQtdeFaltasLancadas_Apresentar() {
		return getQtdeFaltasLancadas().toString();
	}

	public Boolean getControlarMarcarDesmarcarTodos() {
		if (controlarMarcarDesmarcarTodos == null) {
			controlarMarcarDesmarcarTodos = false;
		}
		return controlarMarcarDesmarcarTodos;
	}

	public void setControlarMarcarDesmarcarTodos(Boolean controlarMarcarDesmarcarTodos) {
		this.controlarMarcarDesmarcarTodos = controlarMarcarDesmarcarTodos;
	}

	public void marcarDesmarcarTodos() {
		try {
			for (RegistroAulaVO ra : getListaAulas()) {
				getFacadeFactory().getRegistroAulaNotaFacade().marcarDesmarcarAlunoPresenteAula(getControlarMarcarDesmarcarTodos(), ra, "registroAulaNotaForm", getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getCalcularMediaAoGravar() {
		if (calcularMediaAoGravar == null) {
			calcularMediaAoGravar = Boolean.FALSE;
		}
		return calcularMediaAoGravar;
	}

	public void setCalcularMediaAoGravar(Boolean calcularMediaAoGravar) {
		this.calcularMediaAoGravar = calcularMediaAoGravar;
	}

	public void replicarConteudoTodosDias() {
		
		try {
			if(getRegistroAulaVO().getReplicarConteudoTodosDias()){
				for (RegistroAulaVO obj1 : getListaAulas()) {
					obj1.setConteudo(getRegistroAulaVO().getConteudo());
				}							
			}
		} catch (Exception e) {
			
		}
	}

	public void limparTurma() {
		setTurmaVO(new TurmaVO());
		removerObjetoMemoria(this);
		setDisciplinaVO(null);
		setConfiguracaoAcademicoVO(null);
	}

	public boolean getIsApresentarCampos() {
		return Uteis.isAtributoPreenchido(getTurmaVO()) && !getTurmaVO().getTurmaAgrupada();
	}
	
	public boolean getTrazerAlunoPendenteFinanceiramente() throws Exception {
		return getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
	}
	
	private Date dataBase = new Date();

	public Date getDataBase() {
		if (dataBase == null) {
			dataBase = new Date();
		}
		return dataBase;
	}
	
	public Boolean getNota1AptoApresentar() {		
		if (getConfiguracaoAcademicoVO().getNota1TipoLancamento() && getTipoNota1() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota1() == null || getCalendarioLancamentoNotaVO().getDataInicioNota1().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota1().before(getDataBase());

		}
		return getConfiguracaoAcademicoVO().getUtilizarNota1() && getConfiguracaoAcademicoVO().getApresentarNota1() && getTipoNota1();
	}

	public Boolean getNota2AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota2TipoLancamento() && getTipoNota2() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota2() == null || getCalendarioLancamentoNotaVO().getDataInicioNota2().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota2().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota2() && getConfiguracaoAcademicoVO().getApresentarNota2() && getTipoNota2();
	}

	public Boolean getNota3AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota3TipoLancamento() && getTipoNota3() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota3() == null || getCalendarioLancamentoNotaVO().getDataInicioNota3().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota3().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota3() && getConfiguracaoAcademicoVO().getApresentarNota3() && getTipoNota3();
	}

	public Boolean getNota4AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota4TipoLancamento() && getTipoNota4() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota4() == null || getCalendarioLancamentoNotaVO().getDataInicioNota4().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota4().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota4() && getConfiguracaoAcademicoVO().getApresentarNota4() && getTipoNota4();
	}

	public Boolean getNota5AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota5TipoLancamento() && getTipoNota5() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota5() == null || getCalendarioLancamentoNotaVO().getDataInicioNota5().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota5().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota5() && getConfiguracaoAcademicoVO().getApresentarNota5() && getTipoNota5();
	}

	public Boolean getNota6AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota6TipoLancamento() && getTipoNota6() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota6() == null || getCalendarioLancamentoNotaVO().getDataInicioNota6().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota6().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota6() && getConfiguracaoAcademicoVO().getApresentarNota6() && getTipoNota6();
	}

	public Boolean getNota7AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota7TipoLancamento() && getTipoNota7() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota7() == null || getCalendarioLancamentoNotaVO().getDataInicioNota7().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota7().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota7() && getConfiguracaoAcademicoVO().getApresentarNota7() && getTipoNota7();
	}

	public Boolean getNota8AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota8TipoLancamento() && getTipoNota8() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota8() == null || getCalendarioLancamentoNotaVO().getDataInicioNota8().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota8().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota8() && getConfiguracaoAcademicoVO().getApresentarNota8() && getTipoNota8();
	}

	public Boolean getNota9AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota9TipoLancamento() && getTipoNota9() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota9() == null || getCalendarioLancamentoNotaVO().getDataInicioNota9().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota9().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota9() && getConfiguracaoAcademicoVO().getApresentarNota9() && getTipoNota9();
	}

	public Boolean getNota10AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota10TipoLancamento() && getTipoNota10() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota10() == null || getCalendarioLancamentoNotaVO().getDataInicioNota10().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota10().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota10() && getConfiguracaoAcademicoVO().getApresentarNota10() && getTipoNota10();
	}

	public Boolean getNota11AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota11TipoLancamento() && getTipoNota11() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota11() == null || getCalendarioLancamentoNotaVO().getDataInicioNota11().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota11().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota11() && getConfiguracaoAcademicoVO().getApresentarNota11() && getTipoNota11();
	}

	public Boolean getNota12AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota12TipoLancamento() && getTipoNota12() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota12() == null || getCalendarioLancamentoNotaVO().getDataInicioNota12().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota12().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota12() && getConfiguracaoAcademicoVO().getApresentarNota12() && getTipoNota12();
	}

	public Boolean getNota13AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota13TipoLancamento() && getTipoNota13() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota13() == null || getCalendarioLancamentoNotaVO().getDataInicioNota13().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota13().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota13() && getConfiguracaoAcademicoVO().getApresentarNota13() && getTipoNota13();
	}

	public Boolean getNota14AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota14TipoLancamento() && getTipoNota14() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota14() == null || getCalendarioLancamentoNotaVO().getDataInicioNota14().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota14().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota14() && getConfiguracaoAcademicoVO().getApresentarNota14() && getTipoNota14();
	}

	public Boolean getNota15AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota15TipoLancamento() && getTipoNota15() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota15() == null || getCalendarioLancamentoNotaVO().getDataInicioNota15().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota15().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota15() && getConfiguracaoAcademicoVO().getApresentarNota15() && getTipoNota15();
	}

	public Boolean getNota16AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota16TipoLancamento() && getTipoNota16() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota16() == null || getCalendarioLancamentoNotaVO().getDataInicioNota16().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota16().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota16() && getConfiguracaoAcademicoVO().getApresentarNota16() && getTipoNota16();
	}

	public Boolean getNota17AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota17TipoLancamento() && getTipoNota17() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota17() == null || getCalendarioLancamentoNotaVO().getDataInicioNota17().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota17().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota17() && getConfiguracaoAcademicoVO().getApresentarNota17() && getTipoNota17();
	}

	public Boolean getNota18AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota18TipoLancamento() && getTipoNota18() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota18() == null || getCalendarioLancamentoNotaVO().getDataInicioNota18().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota18().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota18() && getConfiguracaoAcademicoVO().getApresentarNota18() && getTipoNota18();
	}

	public Boolean getNota19AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota19TipoLancamento() && getTipoNota19() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota19() == null || getCalendarioLancamentoNotaVO().getDataInicioNota19().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota19().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota19() && getConfiguracaoAcademicoVO().getApresentarNota19() && getTipoNota19();
	}

	public Boolean getNota20AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota20TipoLancamento() && getTipoNota20() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota20() == null || getCalendarioLancamentoNotaVO().getDataInicioNota20().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota20().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota20() && getConfiguracaoAcademicoVO().getApresentarNota20() && getTipoNota20();
	}

	public Boolean getNota21AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota21TipoLancamento() && getTipoNota21() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota21() == null || getCalendarioLancamentoNotaVO().getDataInicioNota21().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota21().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota21() && getConfiguracaoAcademicoVO().getApresentarNota21() && getTipoNota21();
	}

	public Boolean getNota22AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota22TipoLancamento() && getTipoNota22() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota22() == null || getCalendarioLancamentoNotaVO().getDataInicioNota22().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota22().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota22() && getConfiguracaoAcademicoVO().getApresentarNota22() && getTipoNota22();
	}

	public Boolean getNota23AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota23TipoLancamento() && getTipoNota23() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota23() == null || getCalendarioLancamentoNotaVO().getDataInicioNota23().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota23().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota23() && getConfiguracaoAcademicoVO().getApresentarNota23() && getTipoNota23();
	}

	public Boolean getNota24AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota24TipoLancamento() && getTipoNota24() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota24() == null || getCalendarioLancamentoNotaVO().getDataInicioNota24().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota24().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota24() && getConfiguracaoAcademicoVO().getApresentarNota24() && getTipoNota24();
	}

	public Boolean getNota25AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota25TipoLancamento() && getTipoNota25() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota25() == null || getCalendarioLancamentoNotaVO().getDataInicioNota25().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota25().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota25() && getConfiguracaoAcademicoVO().getApresentarNota25() && getTipoNota25();
	}

	public Boolean getNota26AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota26TipoLancamento() && getTipoNota26() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota26() == null || getCalendarioLancamentoNotaVO().getDataInicioNota26().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota26().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota26() && getConfiguracaoAcademicoVO().getApresentarNota26() && getTipoNota26();
	}

	public Boolean getNota27AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota27TipoLancamento() && getTipoNota27() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota27() == null || getCalendarioLancamentoNotaVO().getDataInicioNota27().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota27().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota27() && getConfiguracaoAcademicoVO().getApresentarNota27() && getTipoNota27();
	}

	public Boolean getNota28AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota28TipoLancamento() && getTipoNota28() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota28() == null || getCalendarioLancamentoNotaVO().getDataInicioNota28().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota28().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota28() && getConfiguracaoAcademicoVO().getApresentarNota28() && getTipoNota28();
	}

	public Boolean getNota29AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota29TipoLancamento() && getTipoNota29() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota29() == null || getCalendarioLancamentoNotaVO().getDataInicioNota29().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota29().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota29() && getConfiguracaoAcademicoVO().getApresentarNota29() && getTipoNota29();
	}

	public Boolean getNota30AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota30TipoLancamento() && getTipoNota30() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota30() == null || getCalendarioLancamentoNotaVO().getDataInicioNota30().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota30().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota30() && getConfiguracaoAcademicoVO().getApresentarNota30() && getTipoNota30();
	}

	public Boolean getNota31AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota31TipoLancamento() && getTipoNota31() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota31() == null || getCalendarioLancamentoNotaVO().getDataInicioNota31().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota31().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota31() && getConfiguracaoAcademicoVO().getApresentarNota31() && getTipoNota31();
	}

	public Boolean getNota32AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota32TipoLancamento() && getTipoNota32() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota32() == null || getCalendarioLancamentoNotaVO().getDataInicioNota32().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota32().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota32() && getConfiguracaoAcademicoVO().getApresentarNota32() && getTipoNota32();
	}

	public Boolean getNota33AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota33TipoLancamento() && getTipoNota33() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota33() == null || getCalendarioLancamentoNotaVO().getDataInicioNota33().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota33().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota33() && getConfiguracaoAcademicoVO().getApresentarNota33() && getTipoNota33();
	}

	public Boolean getNota34AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota34TipoLancamento() && getTipoNota34() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota34() == null || getCalendarioLancamentoNotaVO().getDataInicioNota34().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota34().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota34() && getConfiguracaoAcademicoVO().getApresentarNota34() && getTipoNota34();
	}

	public Boolean getNota35AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota35TipoLancamento() && getTipoNota35() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota35() == null || getCalendarioLancamentoNotaVO().getDataInicioNota35().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota35().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota35() && getConfiguracaoAcademicoVO().getApresentarNota35() && getTipoNota35();
	}

	public Boolean getNota36AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota36TipoLancamento() && getTipoNota36() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota36() == null || getCalendarioLancamentoNotaVO().getDataInicioNota36().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota36().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota36() && getConfiguracaoAcademicoVO().getApresentarNota36() && getTipoNota36();
	}

	public Boolean getNota37AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota37TipoLancamento() && getTipoNota37() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota37() == null || getCalendarioLancamentoNotaVO().getDataInicioNota37().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota37().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota37() && getConfiguracaoAcademicoVO().getApresentarNota37() && getTipoNota37();
	}

	public Boolean getNota38AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota38TipoLancamento() && getTipoNota38() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota38() == null || getCalendarioLancamentoNotaVO().getDataInicioNota38().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota38().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota38() && getConfiguracaoAcademicoVO().getApresentarNota38() && getTipoNota38();
	}

	public Boolean getNota39AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota39TipoLancamento() && getTipoNota39() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota39() == null || getCalendarioLancamentoNotaVO().getDataInicioNota39().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota39().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota39() && getConfiguracaoAcademicoVO().getApresentarNota39() && getTipoNota39();
	}

	public Boolean getNota40AptoApresentar() {
		if (getConfiguracaoAcademicoVO().getNota40TipoLancamento() && getTipoNota40() && getCalendarioLancamentoNotaVO() != null && !getCalendarioLancamentoNotaVO().isNovoObj()) {
			return getCalendarioLancamentoNotaVO().getDataInicioNota40() == null || getCalendarioLancamentoNotaVO().getDataInicioNota40().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota40().before(getDataBase());
		}
		return getConfiguracaoAcademicoVO().getUtilizarNota40() && getConfiguracaoAcademicoVO().getApresentarNota40() && getTipoNota40();
	}
	
	private CalendarioLancamentoNotaVO calendarioLancamentoNotaVO;
	
	public CalendarioLancamentoNotaVO getCalendarioLancamentoNotaVO() {
		if (calendarioLancamentoNotaVO == null) {
			calendarioLancamentoNotaVO = new CalendarioLancamentoNotaVO();
		}
		return calendarioLancamentoNotaVO;
	}

	public void setCalendarioLancamentoNotaVO(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO) {
		this.calendarioLancamentoNotaVO = calendarioLancamentoNotaVO;
	}


	public boolean getTipoNota1() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota1())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota2() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota2())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota3() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota3())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota4() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota4())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota5() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota5())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota6() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota6())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota7() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota7())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota8() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota8())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota9() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota9())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota10() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota10())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota11() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota11())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota12() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota12())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota13() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota13())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota14() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota14())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota15() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota15())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota16() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota16())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota17() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota17())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota18() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota18())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota19() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota19())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota20() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota20())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota21() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota21())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota22() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota22())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota23() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota23())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota24() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota24())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota25() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota25())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota26() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota26())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota27() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota27())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota28() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota28())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota29() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota29())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota30() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota30())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota31() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota31())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota32() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota32())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota33() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota33())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota34() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota34())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota35() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota35())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota36() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota36())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota37() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota37())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota38() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota38())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota39() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota39())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean getTipoNota40() {
		if (getTipoNota() == null) {
			setTipoNota("");
		}
		if (getTipoNota().equals("")) {
			return true;
		} else {
			if (getTipoNota().equals(this.getConfiguracaoAcademicoVO().getTituloNota40())) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	private String tipoNota;
	
	public String getTipoNota() {
		if (tipoNota == null) {
			tipoNota = "";
		}
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota;
	}
	
	public void inicializarOpcaoNotaConceito() {
		setListaSelectItemNota1Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota1ConceitoVOs()));
		setListaSelectItemNota2Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota2ConceitoVOs()));
		setListaSelectItemNota3Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota3ConceitoVOs()));
		setListaSelectItemNota4Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota4ConceitoVOs()));
		setListaSelectItemNota5Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota5ConceitoVOs()));
		setListaSelectItemNota6Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota6ConceitoVOs()));
		setListaSelectItemNota7Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota7ConceitoVOs()));
		setListaSelectItemNota8Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota8ConceitoVOs()));
		setListaSelectItemNota9Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota9ConceitoVOs()));
		setListaSelectItemNota10Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota10ConceitoVOs()));
		setListaSelectItemNota11Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota11ConceitoVOs()));
		setListaSelectItemNota12Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota12ConceitoVOs()));
		setListaSelectItemNota13Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota13ConceitoVOs()));
		setListaSelectItemNota14Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota14ConceitoVOs()));
		setListaSelectItemNota15Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota15ConceitoVOs()));
		setListaSelectItemNota16Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota16ConceitoVOs()));
		setListaSelectItemNota17Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota17ConceitoVOs()));
		setListaSelectItemNota18Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota18ConceitoVOs()));
		setListaSelectItemNota19Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota19ConceitoVOs()));
		setListaSelectItemNota20Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota20ConceitoVOs()));
		setListaSelectItemNota21Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota21ConceitoVOs()));
		setListaSelectItemNota22Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota22ConceitoVOs()));
		setListaSelectItemNota23Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota23ConceitoVOs()));
		setListaSelectItemNota24Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota24ConceitoVOs()));
		setListaSelectItemNota25Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota25ConceitoVOs()));
		setListaSelectItemNota26Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota26ConceitoVOs()));
		setListaSelectItemNota27Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota27ConceitoVOs()));
		setListaSelectItemNota28Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota28ConceitoVOs()));
		setListaSelectItemNota29Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota29ConceitoVOs()));
		setListaSelectItemNota30Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota30ConceitoVOs()));
		setListaSelectItemNota31Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota31ConceitoVOs()));
		setListaSelectItemNota32Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota32ConceitoVOs()));
		setListaSelectItemNota33Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota33ConceitoVOs()));
		setListaSelectItemNota34Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota34ConceitoVOs()));
		setListaSelectItemNota35Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota35ConceitoVOs()));
		setListaSelectItemNota36Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota36ConceitoVOs()));
		setListaSelectItemNota37Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota37ConceitoVOs()));
		setListaSelectItemNota38Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota38ConceitoVOs()));
		setListaSelectItemNota39Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota39ConceitoVOs()));
		setListaSelectItemNota40Conceito(getListaSelectItemOpcaoNotaConceito(getConfiguracaoAcademicoVO().getConfiguracaoAcademicoNota10ConceitoVOs()));
	}
	
	private List<SelectItem> listaSelectItemNota1Conceito;
	private List<SelectItem> listaSelectItemNota2Conceito;
	private List<SelectItem> listaSelectItemNota3Conceito;
	private List<SelectItem> listaSelectItemNota4Conceito;
	private List<SelectItem> listaSelectItemNota5Conceito;
	private List<SelectItem> listaSelectItemNota6Conceito;
	private List<SelectItem> listaSelectItemNota7Conceito;
	private List<SelectItem> listaSelectItemNota8Conceito;
	private List<SelectItem> listaSelectItemNota9Conceito;
	private List<SelectItem> listaSelectItemNota10Conceito;
	private List<SelectItem> listaSelectItemNota11Conceito;
	private List<SelectItem> listaSelectItemNota12Conceito;
	private List<SelectItem> listaSelectItemNota13Conceito;
	private List<SelectItem> listaSelectItemNota14Conceito;
	private List<SelectItem> listaSelectItemNota15Conceito;
	private List<SelectItem> listaSelectItemNota16Conceito;
	private List<SelectItem> listaSelectItemNota17Conceito;
	private List<SelectItem> listaSelectItemNota18Conceito;
	private List<SelectItem> listaSelectItemNota19Conceito;
	private List<SelectItem> listaSelectItemNota20Conceito;
	private List<SelectItem> listaSelectItemNota21Conceito;
	private List<SelectItem> listaSelectItemNota22Conceito;
	private List<SelectItem> listaSelectItemNota23Conceito;
	private List<SelectItem> listaSelectItemNota24Conceito;
	private List<SelectItem> listaSelectItemNota25Conceito;
	private List<SelectItem> listaSelectItemNota26Conceito;
	private List<SelectItem> listaSelectItemNota27Conceito;
	private List<SelectItem> listaSelectItemNota28Conceito;
	private List<SelectItem> listaSelectItemNota29Conceito;
	private List<SelectItem> listaSelectItemNota30Conceito;
	private List<SelectItem> listaSelectItemNota31Conceito;
	private List<SelectItem> listaSelectItemNota32Conceito;
	private List<SelectItem> listaSelectItemNota33Conceito;
	private List<SelectItem> listaSelectItemNota34Conceito;
	private List<SelectItem> listaSelectItemNota35Conceito;
	private List<SelectItem> listaSelectItemNota36Conceito;
	private List<SelectItem> listaSelectItemNota37Conceito;
	private List<SelectItem> listaSelectItemNota38Conceito;
	private List<SelectItem> listaSelectItemNota39Conceito;
	private List<SelectItem> listaSelectItemNota40Conceito;
	
	public List<SelectItem> getListaSelectItemNota1Conceito() {
		if (listaSelectItemNota1Conceito == null) {
			listaSelectItemNota1Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota1Conceito;
	}

	public void setListaSelectItemNota1Conceito(List<SelectItem> listaSelectItemNota1Conceito) {
		this.listaSelectItemNota1Conceito = listaSelectItemNota1Conceito;
	}

	public List<SelectItem> getListaSelectItemNota2Conceito() {
		if (listaSelectItemNota2Conceito == null) {
			listaSelectItemNota2Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota2Conceito;
	}

	public void setListaSelectItemNota2Conceito(List<SelectItem> listaSelectItemNota2Conceito) {
		this.listaSelectItemNota2Conceito = listaSelectItemNota2Conceito;
	}

	public List<SelectItem> getListaSelectItemNota3Conceito() {
		if (listaSelectItemNota3Conceito == null) {
			listaSelectItemNota3Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota3Conceito;
	}

	public void setListaSelectItemNota3Conceito(List<SelectItem> listaSelectItemNota3Conceito) {
		this.listaSelectItemNota3Conceito = listaSelectItemNota3Conceito;
	}

	public List<SelectItem> getListaSelectItemNota4Conceito() {
		if (listaSelectItemNota4Conceito == null) {
			listaSelectItemNota4Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota4Conceito;
	}

	public void setListaSelectItemNota4Conceito(List<SelectItem> listaSelectItemNota4Conceito) {
		this.listaSelectItemNota4Conceito = listaSelectItemNota4Conceito;
	}

	public List<SelectItem> getListaSelectItemNota5Conceito() {
		if (listaSelectItemNota5Conceito == null) {
			listaSelectItemNota5Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota5Conceito;
	}

	public void setListaSelectItemNota5Conceito(List<SelectItem> listaSelectItemNota5Conceito) {
		this.listaSelectItemNota5Conceito = listaSelectItemNota5Conceito;
	}

	public List<SelectItem> getListaSelectItemNota6Conceito() {
		if (listaSelectItemNota6Conceito == null) {
			listaSelectItemNota6Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota6Conceito;
	}

	public void setListaSelectItemNota6Conceito(List<SelectItem> listaSelectItemNota6Conceito) {
		this.listaSelectItemNota6Conceito = listaSelectItemNota6Conceito;
	}

	public List<SelectItem> getListaSelectItemNota7Conceito() {
		if (listaSelectItemNota7Conceito == null) {
			listaSelectItemNota7Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota7Conceito;
	}

	public void setListaSelectItemNota7Conceito(List<SelectItem> listaSelectItemNota7Conceito) {
		this.listaSelectItemNota7Conceito = listaSelectItemNota7Conceito;
	}

	public List<SelectItem> getListaSelectItemNota8Conceito() {
		if (listaSelectItemNota8Conceito == null) {
			listaSelectItemNota8Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota8Conceito;
	}

	public void setListaSelectItemNota8Conceito(List<SelectItem> listaSelectItemNota8Conceito) {
		this.listaSelectItemNota8Conceito = listaSelectItemNota8Conceito;
	}

	public List<SelectItem> getListaSelectItemNota9Conceito() {
		if (listaSelectItemNota9Conceito == null) {
			listaSelectItemNota9Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota9Conceito;
	}

	public void setListaSelectItemNota9Conceito(List<SelectItem> listaSelectItemNota9Conceito) {
		this.listaSelectItemNota9Conceito = listaSelectItemNota9Conceito;
	}

	public List<SelectItem> getListaSelectItemNota10Conceito() {
		if (listaSelectItemNota10Conceito == null) {
			listaSelectItemNota10Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota10Conceito;
	}

	public void setListaSelectItemNota10Conceito(List<SelectItem> listaSelectItemNota10Conceito) {
		this.listaSelectItemNota10Conceito = listaSelectItemNota10Conceito;
	}

	public List<SelectItem> getListaSelectItemNota11Conceito() {
		if (listaSelectItemNota11Conceito == null) {
			listaSelectItemNota11Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota11Conceito;
	}

	public void setListaSelectItemNota11Conceito(List<SelectItem> listaSelectItemNota11Conceito) {
		this.listaSelectItemNota11Conceito = listaSelectItemNota11Conceito;
	}

	public List<SelectItem> getListaSelectItemNota12Conceito() {
		if (listaSelectItemNota12Conceito == null) {
			listaSelectItemNota12Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota12Conceito;
	}

	public void setListaSelectItemNota12Conceito(List<SelectItem> listaSelectItemNota12Conceito) {
		this.listaSelectItemNota12Conceito = listaSelectItemNota12Conceito;
	}

	public List<SelectItem> getListaSelectItemNota13Conceito() {
		if (listaSelectItemNota13Conceito == null) {
			listaSelectItemNota13Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota13Conceito;
	}

	public void setListaSelectItemNota13Conceito(List<SelectItem> listaSelectItemNota13Conceito) {
		this.listaSelectItemNota13Conceito = listaSelectItemNota13Conceito;
	}

	public List<SelectItem> getListaSelectItemNota14Conceito() {
		if (listaSelectItemNota14Conceito == null) {
			listaSelectItemNota14Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota14Conceito;
	}

	public void setListaSelectItemNota14Conceito(List<SelectItem> listaSelectItemNota14Conceito) {
		this.listaSelectItemNota14Conceito = listaSelectItemNota14Conceito;
	}

	public List<SelectItem> getListaSelectItemNota15Conceito() {
		if (listaSelectItemNota15Conceito == null) {
			listaSelectItemNota15Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota15Conceito;
	}

	public void setListaSelectItemNota15Conceito(List<SelectItem> listaSelectItemNota15Conceito) {
		this.listaSelectItemNota15Conceito = listaSelectItemNota15Conceito;
	}

	public List<SelectItem> getListaSelectItemNota16Conceito() {
		if (listaSelectItemNota16Conceito == null) {
			listaSelectItemNota16Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota16Conceito;
	}

	public void setListaSelectItemNota16Conceito(List<SelectItem> listaSelectItemNota16Conceito) {
		this.listaSelectItemNota16Conceito = listaSelectItemNota16Conceito;
	}

	public List<SelectItem> getListaSelectItemNota17Conceito() {
		if (listaSelectItemNota17Conceito == null) {
			listaSelectItemNota17Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota17Conceito;
	}

	public void setListaSelectItemNota17Conceito(List<SelectItem> listaSelectItemNota17Conceito) {
		this.listaSelectItemNota17Conceito = listaSelectItemNota17Conceito;
	}

	public List<SelectItem> getListaSelectItemNota18Conceito() {
		if (listaSelectItemNota18Conceito == null) {
			listaSelectItemNota18Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota18Conceito;
	}

	public void setListaSelectItemNota18Conceito(List<SelectItem> listaSelectItemNota18Conceito) {
		this.listaSelectItemNota18Conceito = listaSelectItemNota18Conceito;
	}

	public List<SelectItem> getListaSelectItemNota19Conceito() {
		if (listaSelectItemNota19Conceito == null) {
			listaSelectItemNota19Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota19Conceito;
	}

	public void setListaSelectItemNota19Conceito(List<SelectItem> listaSelectItemNota19Conceito) {
		this.listaSelectItemNota19Conceito = listaSelectItemNota19Conceito;
	}

	public List<SelectItem> getListaSelectItemNota20Conceito() {
		if (listaSelectItemNota20Conceito == null) {
			listaSelectItemNota20Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota20Conceito;
	}

	public void setListaSelectItemNota20Conceito(List<SelectItem> listaSelectItemNota20Conceito) {
		this.listaSelectItemNota20Conceito = listaSelectItemNota20Conceito;
	}

	public List<SelectItem> getListaSelectItemNota21Conceito() {
		if (listaSelectItemNota21Conceito == null) {
			listaSelectItemNota21Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota21Conceito;
	}

	public void setListaSelectItemNota21Conceito(List<SelectItem> listaSelectItemNota21Conceito) {
		this.listaSelectItemNota21Conceito = listaSelectItemNota21Conceito;
	}

	public List<SelectItem> getListaSelectItemNota22Conceito() {
		if (listaSelectItemNota22Conceito == null) {
			listaSelectItemNota22Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota22Conceito;
	}

	public void setListaSelectItemNota22Conceito(List<SelectItem> listaSelectItemNota22Conceito) {
		this.listaSelectItemNota22Conceito = listaSelectItemNota22Conceito;
	}

	public List<SelectItem> getListaSelectItemNota23Conceito() {
		if (listaSelectItemNota23Conceito == null) {
			listaSelectItemNota23Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota23Conceito;
	}

	public void setListaSelectItemNota23Conceito(List<SelectItem> listaSelectItemNota23Conceito) {
		this.listaSelectItemNota23Conceito = listaSelectItemNota23Conceito;
	}

	public List<SelectItem> getListaSelectItemNota24Conceito() {
		if (listaSelectItemNota24Conceito == null) {
			listaSelectItemNota24Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota24Conceito;
	}

	public void setListaSelectItemNota24Conceito(List<SelectItem> listaSelectItemNota24Conceito) {
		this.listaSelectItemNota24Conceito = listaSelectItemNota24Conceito;
	}

	public List<SelectItem> getListaSelectItemNota25Conceito() {
		if (listaSelectItemNota25Conceito == null) {
			listaSelectItemNota25Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota25Conceito;
	}

	public void setListaSelectItemNota25Conceito(List<SelectItem> listaSelectItemNota25Conceito) {
		this.listaSelectItemNota25Conceito = listaSelectItemNota25Conceito;
	}

	public List<SelectItem> getListaSelectItemNota26Conceito() {
		if (listaSelectItemNota26Conceito == null) {
			listaSelectItemNota26Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota26Conceito;
	}

	public void setListaSelectItemNota26Conceito(List<SelectItem> listaSelectItemNota26Conceito) {
		this.listaSelectItemNota26Conceito = listaSelectItemNota26Conceito;
	}

	public List<SelectItem> getListaSelectItemNota27Conceito() {
		if (listaSelectItemNota27Conceito == null) {
			listaSelectItemNota27Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota27Conceito;
	}

	public void setListaSelectItemNota27Conceito(List<SelectItem> listaSelectItemNota27Conceito) {
		this.listaSelectItemNota27Conceito = listaSelectItemNota27Conceito;
	}
	public List<SelectItem> getListaSelectItemNota28Conceito() {
		if (listaSelectItemNota28Conceito == null) {
			listaSelectItemNota28Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota28Conceito;
	}

	public void setListaSelectItemNota28Conceito(List<SelectItem> listaSelectItemNota28Conceito) {
		this.listaSelectItemNota28Conceito = listaSelectItemNota28Conceito;
	}

	public List<SelectItem> getListaSelectItemNota29Conceito() {
		if (listaSelectItemNota29Conceito == null) {
			listaSelectItemNota29Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota29Conceito;
	}

	public void setListaSelectItemNota29Conceito(List<SelectItem> listaSelectItemNota29Conceito) {
		this.listaSelectItemNota29Conceito = listaSelectItemNota29Conceito;
	}

	public List<SelectItem> getListaSelectItemNota30Conceito() {
		if (listaSelectItemNota30Conceito == null) {
			listaSelectItemNota30Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota30Conceito;
	}

	public void setListaSelectItemNota30Conceito(List<SelectItem> listaSelectItemNota30Conceito) {
		this.listaSelectItemNota30Conceito = listaSelectItemNota30Conceito;
	}

	public List<SelectItem> getListaSelectItemNota31Conceito() {
		if (listaSelectItemNota31Conceito == null) {
			listaSelectItemNota31Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota31Conceito;
	}

	public void setListaSelectItemNota31Conceito(List<SelectItem> listaSelectItemNota31Conceito) {
		this.listaSelectItemNota31Conceito = listaSelectItemNota31Conceito;
	}

	public List<SelectItem> getListaSelectItemNota32Conceito() {
		if (listaSelectItemNota32Conceito == null) {
			listaSelectItemNota32Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota32Conceito;
	}

	public void setListaSelectItemNota32Conceito(List<SelectItem> listaSelectItemNota32Conceito) {
		this.listaSelectItemNota32Conceito = listaSelectItemNota32Conceito;
	}

	public List<SelectItem> getListaSelectItemNota33Conceito() {
		if (listaSelectItemNota33Conceito == null) {
			listaSelectItemNota33Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota33Conceito;
	}

	public void setListaSelectItemNota33Conceito(List<SelectItem> listaSelectItemNota33Conceito) {
		this.listaSelectItemNota33Conceito = listaSelectItemNota33Conceito;
	}

	public List<SelectItem> getListaSelectItemNota34Conceito() {
		if (listaSelectItemNota34Conceito == null) {
			listaSelectItemNota34Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota34Conceito;
	}

	public void setListaSelectItemNota34Conceito(List<SelectItem> listaSelectItemNota34Conceito) {
		this.listaSelectItemNota34Conceito = listaSelectItemNota34Conceito;
	}

	public List<SelectItem> getListaSelectItemNota35Conceito() {
		if (listaSelectItemNota35Conceito == null) {
			listaSelectItemNota35Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota35Conceito;
	}

	public void setListaSelectItemNota35Conceito(List<SelectItem> listaSelectItemNota35Conceito) {
		this.listaSelectItemNota35Conceito = listaSelectItemNota35Conceito;
	}

	public List<SelectItem> getListaSelectItemNota36Conceito() {
		if (listaSelectItemNota36Conceito == null) {
			listaSelectItemNota36Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota36Conceito;
	}

	public void setListaSelectItemNota36Conceito(List<SelectItem> listaSelectItemNota36Conceito) {
		this.listaSelectItemNota36Conceito = listaSelectItemNota36Conceito;
	}

	public List<SelectItem> getListaSelectItemNota37Conceito() {
		if (listaSelectItemNota37Conceito == null) {
			listaSelectItemNota37Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota37Conceito;
	}

	public void setListaSelectItemNota37Conceito(List<SelectItem> listaSelectItemNota37Conceito) {
		this.listaSelectItemNota37Conceito = listaSelectItemNota37Conceito;
	}

	public List<SelectItem> getListaSelectItemNota38Conceito() {
		if (listaSelectItemNota38Conceito == null) {
			listaSelectItemNota38Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota38Conceito;
	}

	public void setListaSelectItemNota38Conceito(List<SelectItem> listaSelectItemNota38Conceito) {
		this.listaSelectItemNota38Conceito = listaSelectItemNota38Conceito;
	}

	public List<SelectItem> getListaSelectItemNota39Conceito() {
		if (listaSelectItemNota39Conceito == null) {
			listaSelectItemNota39Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota39Conceito;
	}

	public void setListaSelectItemNota39Conceito(List<SelectItem> listaSelectItemNota39Conceito) {
		this.listaSelectItemNota39Conceito = listaSelectItemNota39Conceito;
	}

	public List<SelectItem> getListaSelectItemNota40Conceito() {
		if (listaSelectItemNota40Conceito == null) {
			listaSelectItemNota40Conceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota40Conceito;
	}

	public void setListaSelectItemNota40Conceito(List<SelectItem> listaSelectItemNota40Conceito) {
		this.listaSelectItemNota40Conceito = listaSelectItemNota40Conceito;
	}
	
	private List<SelectItem> getListaSelectItemOpcaoNotaConceito(List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs) {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem(0, ""));
		for (ConfiguracaoAcademicoNotaConceitoVO obj : configuracaoAcademicoNotaConceitoVOs) {
			itens.add(new SelectItem(obj.getCodigo(), obj.getConceitoNota()));
		}
		return itens;

	}
	
	public void montarListaOpcoesNotas() {
		getListaAulas().clear();
		getListaNotas().clear();
		if (!getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
			try {
				setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getConfiguracaoAcademicoVO().getCodigo(), getUsuarioLogado()));
				montarListaOpcoesNotas(getConfiguracaoAcademicoVO());
				inicializarDadosDependentesConfiguracaoAcademico();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}
	
	public void montarListaOpcoesNotas(ConfiguracaoAcademicoVO ca) {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("", ""));
		if (ca == null) {
			setListaSelectItemTipoInformarNota(lista);
			return;
		}
		if (ca.getCodigo().intValue() != 0) {
			for (int i = 1; i <= 40; i++) {
				Boolean utilizarNota = (Boolean) UtilReflexao.invocarMetodoGet(ca, "utilizarNota" + i);
				Boolean apresentarNota = (Boolean) UtilReflexao.invocarMetodoGet(ca, "apresentarNota" + i);
				String tituloNotaApresentar = (String) UtilReflexao.invocarMetodoGet(ca, "tituloNotaApresentar" + i);
				String tituloNota = (String) UtilReflexao.invocarMetodoGet(ca, "tituloNota" + i);
				if (utilizarNota && apresentarNota) {
					TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO = (TurmaDisciplinaNotaTituloVO) UtilReflexao.invocarMetodoGet(ca, "turmaDisciplinaNotaTitulo"+ i);
					if(Uteis.isAtributoPreenchido(turmaDisciplinaNotaTituloVO)){
						lista.add(new SelectItem(tituloNota, turmaDisciplinaNotaTituloVO.getTituloNotaApresentar()));
					}else{
						lista.add(new SelectItem(tituloNota, tituloNotaApresentar));
					}
				}
			}
		}
		setListaSelectItemTipoInformarNota(lista);
	}
	
	public List<SelectItem> listaSelectItemTipoInformarNota;
	
	public List<SelectItem> getListaSelectItemTipoInformarNota() {
		if (listaSelectItemTipoInformarNota == null) {
			listaSelectItemTipoInformarNota = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoInformarNota;
	}

	public void setListaSelectItemTipoInformarNota(List<SelectItem> listaSelectItemTipoInformarNota) {
		this.listaSelectItemTipoInformarNota = listaSelectItemTipoInformarNota;
	}
	

	public Boolean getNota28Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota28() == null || getCalendarioLancamentoNotaVO().getDataInicioNota28().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota28().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota28() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota28().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota28().after(getDataBase()))))));
	}

	public Boolean getNota29Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota29() == null || getCalendarioLancamentoNotaVO().getDataInicioNota29().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota29().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota29() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota29().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota29().after(getDataBase()))))));
	}

	public Boolean getNota30Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota30() == null || getCalendarioLancamentoNotaVO().getDataInicioNota30().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota30().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota30() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota30().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota30().after(getDataBase()))))));
	}

	public Boolean getNota27Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota27() == null || getCalendarioLancamentoNotaVO().getDataInicioNota27().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota27().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota27() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota27().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota27().after(getDataBase()))))));
	}

	public Boolean getNota26Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota26() == null || getCalendarioLancamentoNotaVO().getDataInicioNota26().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota26().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota26() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota26().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota26().after(getDataBase()))))));
	}

	public Boolean getNota25Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota25() == null || getCalendarioLancamentoNotaVO().getDataInicioNota25().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota25().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota25() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota25().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota25().after(getDataBase()))))));
	}

	public Boolean getNota24Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota24() == null || getCalendarioLancamentoNotaVO().getDataInicioNota24().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota24().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota24() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota24().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota24().after(getDataBase()))))));
	}

	public Boolean getNota23Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota23() == null || getCalendarioLancamentoNotaVO().getDataInicioNota23().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota23().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota23() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota23().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota23().after(getDataBase()))))));
	}

	public Boolean getNota22Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota22() == null || getCalendarioLancamentoNotaVO().getDataInicioNota22().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota22().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota22() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota22().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota22().after(getDataBase()))))));
	}

	public Boolean getNota21Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota21() == null || getCalendarioLancamentoNotaVO().getDataInicioNota21().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota21().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota21() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota21().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota21().after(getDataBase()))))));
	}

	public Boolean getNota20Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota20() == null || getCalendarioLancamentoNotaVO().getDataInicioNota20().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota20().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota20() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota20().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota20().after(getDataBase()))))));
	}

	public Boolean getNota19Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota19() == null || getCalendarioLancamentoNotaVO().getDataInicioNota19().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota19().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota19() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota19().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota19().after(getDataBase()))))));
	}

	public Boolean getNota18Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota18() == null || getCalendarioLancamentoNotaVO().getDataInicioNota18().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota18().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota18() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota18().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota18().after(getDataBase()))))));
	}

	public Boolean getNota17Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota17() == null || getCalendarioLancamentoNotaVO().getDataInicioNota17().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota17().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota17() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota17().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota17().after(getDataBase()))))));
	}

	public Boolean getNota16Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota16() == null || getCalendarioLancamentoNotaVO().getDataInicioNota16().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota16().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota16() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota16().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota16().after(getDataBase()))))));
	}

	public Boolean getNota15Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota15() == null || getCalendarioLancamentoNotaVO().getDataInicioNota15().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota15().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota15() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota15().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota15().after(getDataBase()))))));
	}

	public Boolean getNota14Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota14() == null || getCalendarioLancamentoNotaVO().getDataInicioNota14().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota14().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota14() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota14().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota14().after(getDataBase()))))));
	}

	public Boolean getNota13Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota13() == null || getCalendarioLancamentoNotaVO().getDataInicioNota13().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota13().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota13() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota13().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota13().after(getDataBase()))))));

	}

	public Boolean getNota12Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota12() == null || getCalendarioLancamentoNotaVO().getDataInicioNota12().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota12().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota12() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota12().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota12().after(getDataBase()))))));
	}

	public Boolean getNota11Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota11() == null || getCalendarioLancamentoNotaVO().getDataInicioNota11().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota11().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota11() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota11().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota11().after(getDataBase()))))));
	}

	public Boolean getNota10Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota10() == null || getCalendarioLancamentoNotaVO().getDataInicioNota10().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota10().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota10() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota10().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota10().after(getDataBase()))))));
	}

	public Boolean getNota1Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota1() == null || getCalendarioLancamentoNotaVO().getDataInicioNota1().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota1().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota1() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota1().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota1().after(getDataBase()))))));
	}

	public Boolean getNota2Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota2() == null || getCalendarioLancamentoNotaVO().getDataInicioNota2().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota2().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota2() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota2().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota2().after(getDataBase()))))));
	}

	public Boolean getNota3Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota3() == null || getCalendarioLancamentoNotaVO().getDataInicioNota3().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota3().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota3() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota3().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota3().after(getDataBase()))))));
	}

	public Boolean getNota4Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota4() == null || getCalendarioLancamentoNotaVO().getDataInicioNota4().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota4().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota4() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota4().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota4().after(getDataBase()))))));
	}

	public Boolean getNota5Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota5() == null || getCalendarioLancamentoNotaVO().getDataInicioNota5().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota5().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota5() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota5().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota5().after(getDataBase()))))));
	}

	public Boolean getNota6Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota6() == null || getCalendarioLancamentoNotaVO().getDataInicioNota6().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota6().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota6() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota6().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota6().after(getDataBase()))))));
	}

	public Boolean getNota7Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota7() == null || getCalendarioLancamentoNotaVO().getDataInicioNota7().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota7().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota7() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota7().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota7().after(getDataBase()))))));
	}

	public Boolean getNota8Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota8() == null || getCalendarioLancamentoNotaVO().getDataInicioNota8().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota8().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota8() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota8().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota8().after(getDataBase()))))));
	}

	public Boolean getNota9Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota9() == null || getCalendarioLancamentoNotaVO().getDataInicioNota9().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota9().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota9() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota9().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota9().after(getDataBase()))))));
	}

	public Boolean getNota31Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota31() == null || getCalendarioLancamentoNotaVO().getDataInicioNota31().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota31().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota31() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota31().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota31().after(getDataBase()))))));
	}

	public Boolean getNota32Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota32() == null || getCalendarioLancamentoNotaVO().getDataInicioNota32().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota32().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota32() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota32().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota32().after(getDataBase()))))));
	}

	public Boolean getNota33Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota33() == null || getCalendarioLancamentoNotaVO().getDataInicioNota33().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota33().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota33() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota33().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota33().after(getDataBase()))))));
	}

	public Boolean getNota34Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota34() == null || getCalendarioLancamentoNotaVO().getDataInicioNota34().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota34().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota34() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota34().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota34().after(getDataBase()))))));
	}

	public Boolean getNota35Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota35() == null || getCalendarioLancamentoNotaVO().getDataInicioNota35().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota35().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota35() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota35().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota35().after(getDataBase()))))));
	}

	public Boolean getNota36Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota36() == null || getCalendarioLancamentoNotaVO().getDataInicioNota36().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota36().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota36() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota36().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota36().after(getDataBase()))))));
	}

	public Boolean getNota37Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota37() == null || getCalendarioLancamentoNotaVO().getDataInicioNota37().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota37().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota37() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota37().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota37().after(getDataBase()))))));
	}

	public Boolean getNota38Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota38() == null || getCalendarioLancamentoNotaVO().getDataInicioNota38().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota38().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota38() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota38().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota38().after(getDataBase()))))));
	}

	public Boolean getNota39Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota39() == null || getCalendarioLancamentoNotaVO().getDataInicioNota39().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota39().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota39() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota39().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota39().after(getDataBase()))))));
	}

	public Boolean getNota40Readonly() {
		return !((getCalendarioLancamentoNotaVO().isNovoObj() || (!getCalendarioLancamentoNotaVO().isNovoObj() && ((getCalendarioLancamentoNotaVO().getDataInicioNota40() == null || getCalendarioLancamentoNotaVO().getDataInicioNota40().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataInicioNota40().before(getDataBase())) && (getCalendarioLancamentoNotaVO().getDataTerminoNota40() == null || getCalendarioLancamentoNotaVO().getDataTerminoNota40().toString().equals(getDataBase().toString()) || getCalendarioLancamentoNotaVO().getDataTerminoNota40().after(getDataBase()))))));
	}

	public Boolean getIsNota1Media() {
		if (!getConfiguracaoAcademicoVO().getNota1TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota2Media() {
		if (!getConfiguracaoAcademicoVO().getNota2TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota3Media() {
		if (!getConfiguracaoAcademicoVO().getNota3TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota4Media() {
		if (!getConfiguracaoAcademicoVO().getNota4TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota5Media() {
		if (!getConfiguracaoAcademicoVO().getNota5TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota6Media() {
		if (!getConfiguracaoAcademicoVO().getNota6TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota7Media() {
		if (!getConfiguracaoAcademicoVO().getNota7TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota8Media() {
		if (!getConfiguracaoAcademicoVO().getNota8TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota9Media() {
		if (!getConfiguracaoAcademicoVO().getNota9TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota10Media() {
		if (!getConfiguracaoAcademicoVO().getNota10TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota11Media() {
		if (!getConfiguracaoAcademicoVO().getNota11TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota12Media() {
		if (!getConfiguracaoAcademicoVO().getNota12TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota13Media() {
		if (!getConfiguracaoAcademicoVO().getNota13TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota14Media() {
		if (!getConfiguracaoAcademicoVO().getNota14TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota15Media() {
		if (!getConfiguracaoAcademicoVO().getNota15TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota16Media() {
		if (!getConfiguracaoAcademicoVO().getNota16TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota17Media() {
		if (!getConfiguracaoAcademicoVO().getNota17TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota18Media() {
		if (!getConfiguracaoAcademicoVO().getNota18TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota19Media() {
		if (!getConfiguracaoAcademicoVO().getNota19TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota20Media() {
		if (!getConfiguracaoAcademicoVO().getNota20TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota21Media() {
		if (!getConfiguracaoAcademicoVO().getNota21TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota22Media() {
		if (!getConfiguracaoAcademicoVO().getNota22TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota23Media() {
		if (!getConfiguracaoAcademicoVO().getNota23TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota24Media() {
		if (!getConfiguracaoAcademicoVO().getNota24TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota25Media() {
		if (!getConfiguracaoAcademicoVO().getNota25TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota26Media() {
		if (!getConfiguracaoAcademicoVO().getNota26TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota27Media() {
		if (!getConfiguracaoAcademicoVO().getNota27TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota28Media() {
		if (!getConfiguracaoAcademicoVO().getNota28TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota29Media() {
		if (!getConfiguracaoAcademicoVO().getNota29TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota30Media() {
		if (!getConfiguracaoAcademicoVO().getNota30TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota31Media() {
		if (!getConfiguracaoAcademicoVO().getNota31TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota32Media() {
		if (!getConfiguracaoAcademicoVO().getNota32TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota33Media() {
		if (!getConfiguracaoAcademicoVO().getNota33TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota34Media() {
		if (!getConfiguracaoAcademicoVO().getNota34TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota35Media() {
		if (!getConfiguracaoAcademicoVO().getNota35TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota36Media() {
		if (!getConfiguracaoAcademicoVO().getNota36TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota37Media() {
		if (!getConfiguracaoAcademicoVO().getNota37TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota38Media() {
		if (!getConfiguracaoAcademicoVO().getNota38TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota39Media() {
		if (!getConfiguracaoAcademicoVO().getNota39TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsNota40Media() {
		if (!getConfiguracaoAcademicoVO().getNota40TipoLancamento()) {
			return true;
		} else {
			return false;
		}
	}
	
	private Boolean possuiDiversidadeConfiguracaoAcademico;
	private List<SelectItem> listaSelectItemConfiguracaoAcademico;
	private HashMap<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademicoVOs;
	
	public Boolean getPossuiDiversidadeConfiguracaoAcademico() {
		if (possuiDiversidadeConfiguracaoAcademico == null) {
			possuiDiversidadeConfiguracaoAcademico = Boolean.FALSE;
		}
		return possuiDiversidadeConfiguracaoAcademico;
	}

	public void setPossuiDiversidadeConfiguracaoAcademico(Boolean possuiDiversidadeConfiguracaoAcademico) {
		this.possuiDiversidadeConfiguracaoAcademico = possuiDiversidadeConfiguracaoAcademico;
	}

	public HashMap<Integer, ConfiguracaoAcademicoVO> getMapConfiguracaoAcademicoVOs() {
		if (mapConfiguracaoAcademicoVOs == null) {
			mapConfiguracaoAcademicoVOs = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		}
		return mapConfiguracaoAcademicoVOs;
	}

	public void setMapConfiguracaoAcademicoVOs(HashMap<Integer, ConfiguracaoAcademicoVO> mapConfiguracaoAcademicoVOs) {
		this.mapConfiguracaoAcademicoVOs = mapConfiguracaoAcademicoVOs;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoAcademico() {
		if (listaSelectItemConfiguracaoAcademico == null) {
			listaSelectItemConfiguracaoAcademico = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConfiguracaoAcademico;
	}

	public void setListaSelectItemConfiguracaoAcademico(List<SelectItem> listaSelectItemConfiguracaoAcademico) {
		this.listaSelectItemConfiguracaoAcademico = listaSelectItemConfiguracaoAcademico;
	}
	
	public void montarComboboxConfiguracaoAcademico() {
		try{
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			setConfiguracaoAcademicoVO(null);
			getListaAulas().clear();
			getListaNotas().clear();
			setTipoNota("");
			getListaSelectItemTipoInformarNota().clear();
			if(Uteis.isAtributoPreenchido(getDisciplinaVO()) && Uteis.isAtributoPreenchido(getTurmaVO())){				
				
			List<ConfiguracaoAcademicoVO> configuracaoAcademicoVOs =  null;		
			if (!getUsuarioLogado().getIsApresentarVisaoProfessor() && !getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(0, 0, getDisciplinaVO().getCodigo(), getTurmaVO(), getAno(), getSemestre(), getTrazerAlunoPendenteFinanceiramente(), "'AA', 'CC', 'CH', 'IS', 'TR'", false, false, false, null, false, false, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());						
			} else {
				configuracaoAcademicoVOs = getFacadeFactory().getConfiguracaoAcademicoFacade().consultaRapidaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNota(0, 0, getDisciplinaVO().getCodigo(), getTurmaVO(), getAno(), getSemestre(), getTrazerAlunoPendenteFinanceiramente(), "'AA', 'CC', 'CH', 'IS', 'TR'", false, getUsuarioLogado().getIsApresentarVisaoProfessor(), false, null, false, false, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getTurmaVO().getUnidadeEnsino().getCodigo()).getPermitirProfessorRealizarLancamentoAlunosPreMatriculados(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}				 
			for (ConfiguracaoAcademicoVO configuracao : configuracaoAcademicoVOs) {
				objs.add(new SelectItem(configuracao.getCodigo(), configuracao.getNome()));
			}
			setListaSelectItemConfiguracaoAcademico(objs);
			if(!configuracaoAcademicoVOs.isEmpty()){
				setConfiguracaoAcademicoVO(configuracaoAcademicoVOs.get(0));
				montarListaOpcoesNotas();
			}
			setPossuiDiversidadeConfiguracaoAcademico(configuracaoAcademicoVOs.size()>1);
			}
			limparMensagem();
		}catch(Exception e){
			
		}
	}		
	
	public void inicializarDadosDependentesConfiguracaoAcademico() throws Exception {
		inicializarOpcaoNotaConceito();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			setCalendarioLancamentoNotaVO(getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), getTurmaVO().getTurmaAgrupada(), getUsuarioLogado().getPessoa().getCodigo(), getDisciplinaVO().getCodigo(), getConfiguracaoAcademicoVO().getCodigo(), getTurmaVO().getPeriodicidade(), getAno(), getSemestre(), false, getUsuarioLogado()));
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			setCalendarioLancamentoNotaVO(getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(getTurmaVO().getUnidadeEnsino().getCodigo(), getTurmaVO().getCodigo(), getTurmaVO().getTurmaAgrupada(), 0, getDisciplinaVO().getCodigo(), getConfiguracaoAcademicoVO().getCodigo(), getTurmaVO().getPeriodicidade(), getAno(), getSemestre(), false, getUsuarioLogado()));
		} else {
			setCalendarioLancamentoNotaVO(null);
		}
	}

	public void checarFormatarValoresNota1() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota1() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 1);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}		
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota2() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		limparMensagem();
		if (historicoAux.getNota2() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 2);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota3() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota3() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 3);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota4() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota4() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 4);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota5() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota5() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 5);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota6() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota6() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 6);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota7() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota7() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 7);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota8() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota8() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 8);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota9() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota9() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 9);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota10() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota10() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 10);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota11() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota11() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 11);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota12() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota12() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 12);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota13() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota13() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 13);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota14() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota14() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 14);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota15() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota15() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 15);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota16() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota16() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 16);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota17() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota17() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 17);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota18() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota18() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 18);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota19() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota19() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 19);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota20() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota20() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 20);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota21() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota21() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 21);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota22() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota22() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 22);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota23() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota23() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 23);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota24() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota24() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 24);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota25() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota25() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 25);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota26() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota26() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 26);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota27() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota27() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 27);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota28() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota28() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 28);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota29() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota29() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 29);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota30() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota30() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 30);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota31() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota31() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 31);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota32() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota32() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 32);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota33() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota33() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 33);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota34() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota34() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 34);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota35() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota35() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 35);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota36() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota36() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 36);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota37() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota37() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 37);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota38() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota38() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 38);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota39() {
		HistoricoVO historicoAux =  ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota39() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 39);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public void checarFormatarValoresNota40() {
		HistoricoVO historicoAux = ((FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens")).getHistoricoVO();
		limparMensagem();
		setSucesso(false);
		historicoAux.setHistoricoAlterado(Boolean.TRUE);
		historicoAux.setHistoricoSalvo(Boolean.FALSE);
		historicoAux.setHistoricoCalculado(Boolean.FALSE);
		if (historicoAux.getNota40() != null) {
			try {
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoAux, historicoAux.getHistoricoDisciplinaFilhaComposicaoVOs(), getConfiguracaoAcademicoVO(), 40);
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		setHistoricoAlterado(true);
	}

	public String getAno() {
		if(ano == null){
			ano = Uteis.getAnoDataAtual4Digitos();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null){
			semestre = Uteis.getSemestreAtual();
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if(listaSelectItemSemestre == null){
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}
	
	public Boolean getIsApresentarAno(){
		return Uteis.isAtributoPreenchido(getTurmaVO()) && !getTurmaVO().getIntegral() && 
				((!getUsuarioLogado().getIsApresentarVisaoAdministrativa() && getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo())
						|| getUsuarioLogado().getIsApresentarVisaoAdministrativa());		
	}
	
	public Boolean getIsApresentarSemestre(){
		return Uteis.isAtributoPreenchido(getTurmaVO()) && getTurmaVO().getSemestral() && ((!getUsuarioLogado().getIsApresentarVisaoAdministrativa() && getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo())
				|| getUsuarioLogado().getIsApresentarVisaoAdministrativa());		
	}
	
     public void carregarDadosTurma(){
    	 if(Uteis.isAtributoPreenchido(getTurmaVO())){
    		 try {
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), getUsuarioLogado());
				setAno(null);
				setSemestre(null);
				if(getUsuarioLogado().getIsApresentarVisaoProfessor()){
					montarListaDisciplinas();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }else{
    		 setTurmaVO(null);
    	 }
    	 
     } 	

     public void marcarListaAlterada(){
    	 setHistoricoAlterado(true);
     }

	public RegistroAulaVO getRegistroAulaVO() {
		if(registroAulaVO == null){
			registroAulaVO = new RegistroAulaVO();
		}
		return registroAulaVO;
	}

	public void setRegistroAulaVO(RegistroAulaVO registroAulaVO) {
		this.registroAulaVO = registroAulaVO;
	}

	public Boolean getHistoricoAlterado() {
		if (historicoAlterado == null) {
			historicoAlterado = false;
		}
		return historicoAlterado;
	}

	public void setHistoricoAlterado(Boolean historicoAlterado) {
		this.historicoAlterado = historicoAlterado;
	}
	

	public TurmaDisciplinaNotaTituloVO getTurmaDisciplinaNotaTituloVO() {
		if(turmaDisciplinaNotaTituloVO == null){
			turmaDisciplinaNotaTituloVO =  new TurmaDisciplinaNotaTituloVO();
		}
		return turmaDisciplinaNotaTituloVO;
	}

	public void setTurmaDisciplinaNotaTituloVO(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTituloVO) {
		this.turmaDisciplinaNotaTituloVO = turmaDisciplinaNotaTituloVO;
	}

    public void gravarTituloNota(){
    	try {
    		
    		List<HistoricoVO> listaHistoricoVOs = new ArrayList<HistoricoVO>(0);
    		
    		if(Uteis.isAtributoPreenchido(getUltimoRegistroAula().getFrequenciaAulaVOs())) {
        		for (FrequenciaAulaVO frequenciaAulaVO : getUltimoRegistroAula().getFrequenciaAulaVOs()) {
        			listaHistoricoVOs.add(frequenciaAulaVO.getHistoricoVO());
        		}
    		}
    		
    		if(getTurmaDisciplinaNotaTituloVO().getPossuiFormula() && !Uteis.isAtributoPreenchido(getTurmaDisciplinaNotaTituloVO().getFormula())) {
    			setOncompleteModal("");
    			throw new Exception("O campo Fórmula Cálculo deve ser informado!");
    		}
    		if(Uteis.isAtributoPreenchido(getTurmaDisciplinaNotaTituloVO())){
    			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().alterar(getTurmaDisciplinaNotaTituloVO(), listaHistoricoVOs, getTurmaVO(), getUsuarioLogado(), null, false, false);
    		}else{
    			getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().incluir(getTurmaDisciplinaNotaTituloVO(), listaHistoricoVOs, getTurmaVO(), getUsuarioLogado(), null, false, false);
    		}
    		getTurmaDisciplinaNotaTituloVO().setTituloOriginal(getTurmaDisciplinaNotaTituloVO().getTitulo());
    		for(SelectItem item: getListaSelectItemTipoInformarNota()){
    			if(item.getValue().equals(getTurmaDisciplinaNotaTituloVO().getVariavelConfiguracao())){
    				item.setLabel(getTurmaDisciplinaNotaTituloVO().getTituloNotaApresentar());
    				break;
    			}
    		}
    		setOncompleteModal("RichFaces.$('panelTituloNota').hide();");	

    		setTurmaDisciplinaNotaTituloVO(new TurmaDisciplinaNotaTituloVO());
    			
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
    }
    
    public void cancelarTituloNota(){
    	getTurmaDisciplinaNotaTituloVO().setTitulo(getTurmaDisciplinaNotaTituloVO().getTituloOriginal());
    }

	public Boolean getPermiteInformarTituloNota() {
			try{
			return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("RegistrarAulaNotaPermiteInformarTituloNota", getUsuarioLogado());
			}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
			}
		}

	public FormaReplicarNotaOutraDisciplinaEnum getFormaReplicarNotaOutraDisciplina() {
		if(formaReplicarNotaOutraDisciplina == null){
			formaReplicarNotaOutraDisciplina = FormaReplicarNotaOutraDisciplinaEnum.TODAS;
		}
		return formaReplicarNotaOutraDisciplina;
	}

	public void setFormaReplicarNotaOutraDisciplina(FormaReplicarNotaOutraDisciplinaEnum formaReplicarNotaOutraDisciplina) {
		this.formaReplicarNotaOutraDisciplina = formaReplicarNotaOutraDisciplina;
	}

	public List<SelectItem> getListaSelectItemFormaReplicarNotaOutraDisciplina() {
		if(listaSelectItemFormaReplicarNotaOutraDisciplina == null){
			listaSelectItemFormaReplicarNotaOutraDisciplina =  new ArrayList<SelectItem>(0);
			listaSelectItemFormaReplicarNotaOutraDisciplina.add(new SelectItem(FormaReplicarNotaOutraDisciplinaEnum.TODAS, FormaReplicarNotaOutraDisciplinaEnum.TODAS.getValorApresentar()));
			listaSelectItemFormaReplicarNotaOutraDisciplina.add(new SelectItem(FormaReplicarNotaOutraDisciplinaEnum.NOTAS_LANCADAS, FormaReplicarNotaOutraDisciplinaEnum.NOTAS_LANCADAS.getValorApresentar()));
			listaSelectItemFormaReplicarNotaOutraDisciplina.add(new SelectItem(FormaReplicarNotaOutraDisciplinaEnum.NOTAS_NAO_LANCADAS, FormaReplicarNotaOutraDisciplinaEnum.NOTAS_NAO_LANCADAS.getValorApresentar()));
		}
		return listaSelectItemFormaReplicarNotaOutraDisciplina;
	}

	public void setListaSelectItemFormaReplicarNotaOutraDisciplina(
			List<SelectItem> listaSelectItemFormaReplicarNotaOutraDisciplina) {
		this.listaSelectItemFormaReplicarNotaOutraDisciplina = listaSelectItemFormaReplicarNotaOutraDisciplina;
	}

	public TipoNotaConceitoEnum getTipoNotaReplicar() {
		return tipoNotaReplicar;
	}

	public void setTipoNotaReplicar(TipoNotaConceitoEnum tipoNotaReplicar) {
		this.tipoNotaReplicar = tipoNotaReplicar;
	}

	public void gravarReplicacaoNotaOutraDisciplina(){
		try{	
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getHistoricoFacade().realizarReplicacaoNota(getListaNotas(), getConfiguracaoAcademicoVO(), getTipoNotaReplicar(), getFormaReplicarNotaOutraDisciplina(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public boolean renderizarDadosAluno(int row) {		
		return row == 0 || (getTam() > 0 && (row%getTam()) == 0); 		
	} 
	
	public TurmaDisciplinaNotaParcialVO getTurmaDisciplinaNotaParcial() {
		if(turmaDisciplinaNotaParcial == null) {
			turmaDisciplinaNotaParcial= new TurmaDisciplinaNotaParcialVO();
		}
		return turmaDisciplinaNotaParcial;
	}

	public void setTurmaDisciplinaNotaParcial(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcial) {
		this.turmaDisciplinaNotaParcial = turmaDisciplinaNotaParcial;
	}

	public void adicionarTurmaDisciplinaNotaParcialItem(){
		try {
			
			getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().adicionarTurmaDisciplinaNotaParcialItem(getTurmaDisciplinaNotaParcial(), getTurmaDisciplinaNotaTituloVO());
			
			setTurmaDisciplinaNotaParcial(new TurmaDisciplinaNotaParcialVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void removerTurmaDisciplinaNotaParcialItem() {
		TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO = (TurmaDisciplinaNotaParcialVO) context().getExternalContext().getRequestMap().get("turmaDisciplinaNotaParcialItens");
		try {
			getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().removerTurmaDisciplinaNotaParcialItem(turmaDisciplinaNotaParcialVO, getTurmaDisciplinaNotaTituloVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}	
		
	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialVOs() {
		if(historicoNotaParcialVOs == null) {
			historicoNotaParcialVOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialVOs;
	}

	public void setHistoricoNotaParcialVOs(List<HistoricoNotaParcialVO> historicoNotaParcialVOs) {
		this.historicoNotaParcialVOs = historicoNotaParcialVOs;
	}

	public void buscarHistoricoParcialNota(String tipoNota) throws Exception {
		FrequenciaAulaVO frequenciaAulaVO = new FrequenciaAulaVO();
		
		frequenciaAulaVO = (FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItens");
		setHistoricoTemporarioVO(frequenciaAulaVO.getHistoricoVO());		
		setHistoricoNotaParcialVOs(getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().consultarPorHistorico(getHistoricoTemporarioVO(), tipoNota, getUsuarioLogado(), getAno(), getSemestre(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));	
		setarTituloNotaApresentar(tipoNota);
	}
	
	public void alterarHistoricosNotaParcial() {
		try {
			getFacadeFactory().getHistoricoNotaParcialInterfaceFacade().alterarHistoricosNotaParcial(getHistoricoNotaParcialVOs(), getHistoricoTemporarioVO(), getUsuarioLogado());
			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				consultarAlunosVisaoCoordenador();
			}else if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
				consultarAlunos();
			} else {
//				consultarAlunosVisaoAdministrativa();
			}		
						
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
		
	}

	public HistoricoVO getHistoricoTemporarioVO() {
		if (historicoTemporarioVO == null) {
			historicoTemporarioVO = new HistoricoVO();
		}
		return historicoTemporarioVO;
	}

	public void setHistoricoTemporarioVO(HistoricoVO historicoTemporarioVO) {
		this.historicoTemporarioVO = historicoTemporarioVO;
	}
	
	public void buscarHistoricoParcialNotaGeral(String tipoNota) throws Exception {
		getHistoricoNotaParcialGeralVOs().clear();
	
		for (FrequenciaAulaVO obj : getUltimoRegistroAula().getFrequenciaAulaVOs()) {
			obj.getHistoricoVO().getObterListaNotaParcialPorTipoNota(tipoNota);
		}
		setTipoNotaUsar(tipoNota);
		setarTituloNotaApresentar(tipoNota);
	}

	public List<HistoricoNotaParcialVO> getHistoricoNotaParcialGeralVOs() {
		if (historicoNotaParcialGeralVOs == null) {
			historicoNotaParcialGeralVOs = new ArrayList<HistoricoNotaParcialVO>(0);
		}
		return historicoNotaParcialGeralVOs;
	}

	public void setHistoricoNotaParcialGeralVOs(List<HistoricoNotaParcialVO> historicoNotaParcialGeralVOs) {
		this.historicoNotaParcialGeralVOs = historicoNotaParcialGeralVOs;
	}
	
	public void alterarHistoricosNotaParcialGeral(){
		for (FrequenciaAulaVO obj : getUltimoRegistroAula().getFrequenciaAulaVOs()) {
			try {			
				if(Uteis.isAtributoPreenchido(obj.getHistoricoVO().getHistoricoNotaParcialNotaVOs())) {
					setHistoricoNotaParcialVOs(obj.getHistoricoVO().getHistoricoNotaParcialNotaVOs());
					setHistoricoTemporarioVO(obj.getHistoricoVO());
					alterarHistoricosNotaParcial();
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());	
			}	
		}
		
	}

	public String getTipoNotaUsar() {
		if (tipoNotaUsar == null) {
			tipoNotaUsar = "";
		}
		return tipoNotaUsar;
	}

	public void setTipoNotaUsar(String tipoNotaUsar) {
		this.tipoNotaUsar = tipoNotaUsar;
	}
	
	public Boolean buscarTurmaDisciplinaNotaParcial(String tipoNota) {
		List<TurmaDisciplinaNotaParcialVO> listTurmaDisciplinaNotaParcialVO = new ArrayList<TurmaDisciplinaNotaParcialVO>(0);
		try {
			listTurmaDisciplinaNotaParcialVO = getFacadeFactory().getTurmaDisciplinaNotaParcialInterfaceFacade().consultarPorTurmaDisciplinaTipoNota(getTurmaVO(), getDisciplinaVO(), tipoNota,  getAno(), getSemestre(), getConfiguracaoAcademicoVO().getCodigo(), getUsuarioLogado(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			if(Uteis.isAtributoPreenchido(listTurmaDisciplinaNotaParcialVO)) {				
				return true;
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());	
		}
		return false;
		
	}

	public String getTituloNotaApresentar() {
		if (tituloNotaApresentar == null) {
			tituloNotaApresentar = "";
		}
		return tituloNotaApresentar;
	}

	public void setTituloNotaApresentar(String tituloNotaApresentar) {
		this.tituloNotaApresentar = tituloNotaApresentar;
	}

	public void setarTituloNotaApresentar(String tipoNota) {
		setTituloNotaApresentar(getFacadeFactory().getTurmaDisciplinaNotaTituloFacade().setarTituloNotaApresentar(tipoNota, getConfiguracaoAcademicoVO().getTurmaDisciplinaNotaTitulo1().getTituloNotaApresentar()));
	}
	
	public Boolean getPermiteDefinirDetalhamentoNota() {
		try{
			return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("RegistrarAulaNota_PermiteDefinirDetalhamentoNota", getUsuarioLogado());
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
			return false;
		}		
	}

	public int getTamanhoPopupPanel() {
		if(getPermiteDefinirDetalhamentoNota()) {
			return 600;
		}
		else {
			return 200;
		}
	}

	@PostConstruct
	public void inicializarDados() {
		try {
		if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {				
			novoVisaoProfessor();				
		}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			novoVisaoCoordenador();
		}else {
			novo();
		}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public Boolean getNotaAptoApresentar(Integer nrNota) {
		return (Boolean) UtilReflexao.invocarMetodoGet(this, "nota"+nrNota+"AptoApresentar");
		
	}
	
	public Boolean getIsNotaMedia(Integer nrNota) {
		return (Boolean) UtilReflexao.invocarMetodoGet(this, "isNota"+nrNota+"Media");
	}
          
}
