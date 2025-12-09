package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.ControleConcorrencia;
import controle.arquitetura.SelectItemOrdemChave;
import controle.arquitetura.SuperControle;
import jobs.JobNotificacaoAulaReposicaoDisponivel;
import negocio.comuns.academico.ChoqueHorarioAlunoVO;
import negocio.comuns.academico.ChoqueHorarioVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisponibilidadeHorarioTurmaProfessorVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.ProgramacaoAulaResumoSemanaVO;
import negocio.comuns.academico.ProgramacaoAulaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoValidacaoChoqueHorarioEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.TipoHorarioTurma;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.academico.ProgramacaoAula;
import negocio.facade.jdbc.academico.TurmaAgrupada;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas programacaoAulaForm.jsp programacaoAulaCons.jsp) com as
 * funcionalidades da classe <code>ProgramacaoAula</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ProgramacaoAula
 * @see ProgramacaoAulaVO
 */

@Controller("ProgramacaoAulaControle")
@Scope("viewScope")
@Lazy
public class ProgramacaoAulaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -6418404760262894285L;

	private HorarioTurmaVO horarioTurmaVO;
	private String turma_Erro;
	private String disciplina_Erro;
	private String professor_Erro;
	private String matriculaProfessor;
	private List listaProfessoresInteressadosDisciplina;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<SelectItem> listaSelectItemDisciplinaProfessor;
	private List<SelectItem> listaSelectItemTurma;
	private HorarioProfessorVO horarioProfessorVO;
	private DisponibilidadeHorarioTurmaProfessorVO disponibilidadeSubstituta;
	private HorarioTurmaDiaVO horarioTurmaPorDiaVO;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaProfessor;
	private List listaConsultaTurma;
	private Boolean programarAulaPeriodo;
	private List<ChoqueHorarioVO> listaMensagemProgramarAulaPeriodo;
	private List<ChoqueHorarioVO> listaChoqueHorariaAulaAcimaLimite;
	private List<ChoqueHorarioVO> listaChoqueHorariaSala;
	private List<ChoqueHorarioVO> listaChoqueHorariaAulaRegistrada;
	private List<ChoqueHorarioVO> listaChoqueHorariaAula;
	private List<ChoqueHorarioVO> listaMensagemProgramarAulaSemChoqueHorario;
	private List<SelectItem> listaSelectItemProfessor;
	private List<HorarioTurmaDiaVO> listaHorarioTurmaDiaVOs;
	private Integer professor;
	private Integer disciplina;
	private Boolean consultarPorProfessorSubstituto;
	private Integer index;
	private Integer numeroAula;
	private DiaSemana diaSemana;
	private Boolean alterarTodasAulas;
	private Boolean imprimirHorarioTurma;
	private String anoVigente;
	private String semestreVigente;
	private Boolean isApresentarAnoSemestre;
	private Boolean isApresentarDisciplinaEProfessor;
	private Boolean horarioAlterado;
	private Boolean erroExclusao;
	private Boolean alteracaoComAulaRegistrada;
	private Boolean retornarExcecaoAlteracaoComAulaRegistrada;
	private Boolean enviarComunicadoPorEmail;
	private Boolean enviarTurmaBase;
	private Boolean enviarTurmaReposicao;
	private PersonalizacaoMensagemAutomaticaVO msgNotificacaoAlunoProgramacaoAula;
	private PersonalizacaoMensagemAutomaticaVO msgNotificacaoAlunoReposicaoAula;
	private PersonalizacaoMensagemAutomaticaVO msgNotificacaoAlunoReposicaoAulaDisponivel;
	private Map<Integer, String> hashDisciplinasAlteradas;
	private List<Integer> listaDisciplinasAlteradasCodigo;
	private TitulacaoCursoVO titulacaoCursoVO;
	private List<PessoaVO> listaProfessoresSelecionados;
	private HashMap<Integer, Integer> hashMapQtdeNivelEscolaridade;
	private HashMap<String, String> hashMapProfessorDisciplina;
	private Boolean panelGravarTitulacao;
	private List<SelectItem> listaSelectItemLocal;
	private List<SelectItem> listaSelectItemSala;
	private SalaLocalAulaVO sala;
	private Boolean liberarProgramacaoAulaProfessorAcimaPermitido;
	private boolean liberarProgramacaoAulaComChoqueHorario=false;
	private String username;
	private String senha;
	private String userNameLiberarFuncionalidade;
	private String senhaLiberarFuncionalidade;
	private String mensagemLiberacaoAulaProfessorAcimaPermitido;
	private List<ChoqueHorarioAlunoVO> choqueHorarioAlunoVOs;
	private ChoqueHorarioAlunoVO choqueHorarioAlunoVO;
	private List<ProgramacaoAulaResumoSemanaVO> programacaoAulaResumoSemanaVOs;
	private Boolean calendarioResumido;
	private Integer disciplinaFilter;
	private Integer professorFilter;
	private String situacaoTurma;
	private String situacaoTipoTurma;
	private List<SelectItem> listaSelectItemSituacaoTurma;
	private List<SelectItem> listaSelectItemSituacaoTipoTurma;
	private Boolean ocultarHorarioSemAula;
	private String manterRichModalLiberarComChoqueHorario;
	
	private String acaoModalEnviarEmailAlunosTurmaPadrao;
	private List<SelectItem> listaSelectItemFuncionarioCargo;

	private FuncionarioCargoVO funcionarioCargoVO;
	private HorarioTurmaDiaItemVO horarioTurmaDiaItemSelecionadoVO;
	private boolean operacaoExcluirHorarioTurmaDoProfessor = false;
	private String modalRegistrarAulaAutomaticamente;
	private Boolean enviarProfessor;
	private Boolean enviarCoordenador;
	private Boolean enviarReponsavelUnidade;
	private Boolean enviarUsuarioEspecifico;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionariosAptoReceberNotificacao;
	private List<FuncionarioVO> listaConsultaFuncionariosAptoReceberNotificacaoSelecionados;

	
	public ProgramacaoAulaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		if (!getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			montarListaTurmaAluno();
			setAnoVigente("");
			setSemestreVigente("");
		}
		setMensagemID("msg_entre_prmconsulta");
	}

	public void consultarProfessor() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				if (getValorConsultaProfessor().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				if (getValorConsultaProfessor().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getTituloScrollFirst() {
		if (getIndex() - 1 < 1) {
			return "";
		}
		if (getHorarioTurmaVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getHorarioTurmaVO().getCalendarioHorarioAulaVOs().get(getIndex() - 2).getTituloCalendarioAbreviado();
		}
		return "";
	}

	public String getTituloScroll() {
		if (getIndex() - 1 < 0) {
			setIndex(1);
		}
		if (getHorarioTurmaVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getHorarioTurmaVO().getCalendarioHorarioAulaVOs().get(getIndex() - 1).getTituloCalendarioAbreviado();
		}
		return "";
	}

	public String getTituloScrollNext() {
		if (getIndex() == getHorarioTurmaVO().getCalendarioHorarioAulaVOs().size()) {
			return "";
		}
		if (getHorarioTurmaVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getHorarioTurmaVO().getCalendarioHorarioAulaVOs().get(getIndex()).getTituloCalendarioAbreviado();
		}
		return "";
	}

	public Integer getIndex() {
		if (index == null) {
			index = 0;
		}
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public void consultarTurma() {
		try {
			super.consultar();
//			List objs = new ArrayList<>(0);
//			if (getCampoConsultaTurma().equals("identificadorTurma")) {
//				if (getValorConsultaTurma().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//			}
//			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
//				if (getValorConsultaTurma().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//			}
//			if (getCampoConsultaTurma().equals("nomeTurno")) {
//				if (getValorConsultaTurma().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//			}
//			if (getCampoConsultaTurma().equals("nomeCurso")) {
//				if (getValorConsultaTurma().length() < 2) {
//					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
//				}
//				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//			}
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarTurmaProgramacaoAula(getCampoConsultaTurma(), getValorConsultaTurma(), getSituacaoTurma(), "", getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboProfessor() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));	
		itens.add(new SelectItem("matricula", "matricula"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void selecionarProfessor2() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
			if (getConsultarPorProfessorSubstituto()) {
				getHorarioTurmaVO().setProfessorSubstituto(obj);
			} else {
				getHorarioTurmaVO().setProfessor(obj);
			}
			setMatriculaProfessor(getFacadeFactory().getFuncionarioFacade().consultarMatriculaFuncionarioPorCodigoPessoa(obj.getCodigo(), 0));
			if (verificarPermissaoUsuarioIncluirFuncionarioCargo()) {
				FuncionarioVO fun = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				montarListaSelectItemFuncionarioCargo(fun);
			}
			if (getApresentarProgramarAulaDiaDiaAvulso()) {
				inicializarDadosDisponibilidadeHorarioDiaDiaAvulso();
			} else {
				inicializarDadosDisponibilidadeHorarioDiaDiaPeriodo();
			}
		} catch (Exception e) {
		}

	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>ProgramacaoAula</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		setUsername("");
		setSenha("");
		setMensagemLiberacaoAulaProfessorAcimaPermitido("");
		setLiberarProgramacaoAulaProfessorAcimaPermitido(false);
		removerObjetoMemoria(this);
		setHorarioTurmaVO(new HorarioTurmaVO());
		setMatriculaProfessor("");
		inicializarListasSelectItemTodosComboBox();
		setListaProfessoresInteressadosDisciplina(new ArrayList<>(0));
		setHorarioProfessorVO(null);
		setListaConsultaProfessor(new ArrayList<>(0));
		setHorarioTurmaPorDiaVO(new HorarioTurmaDiaVO());
		setHorarioAlterado(null);
		setEnviarComunicadoPorEmail(null);
		isValidarPermissaoParaProgramacaoAulaComGoogleMeet();
		isValidarPermissaoParaProgramacaoAulaComClassroom();
		isValidarPermissaoParaProgramacaoAulaComBlackboard();
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>ProgramacaoAula</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {			
			setUsername("");
			setSenha("");
			setMensagemLiberacaoAulaProfessorAcimaPermitido("");
			setLiberarProgramacaoAulaProfessorAcimaPermitido(false);
			HorarioTurmaVO obj = (HorarioTurmaVO) context().getExternalContext().getRequestMap().get("programacaoAulaItens");
			carregarDadosEdicao(obj);
			return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaForm.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemID("msg_erro_dadosnaoencontrados");
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaCons.xhtml");
		}
	}
	
	public void carregarDadosEdicao(HorarioTurmaVO horarioTurmaVO) throws Exception{
		setUsername("");
		setSenha("");
		setMensagemLiberacaoAulaProfessorAcimaPermitido("");
		setLiberarProgramacaoAulaProfessorAcimaPermitido(false);
		horarioTurmaVO.setNovoObj(false);
		horarioTurmaVO.getListaTemporariaHorarioTurmaDiaItemVOLog().clear();
		setHorarioTurmaVO(horarioTurmaVO);
		setHorarioTurmaPorDiaVO(new HorarioTurmaDiaVO());			
		getFacadeFactory().getHorarioTurmaFacade().inicializarDadosHorarioTurmaPorTurma(getHorarioTurmaVO(), true, getUnidadeEnsinoLogado(), getUsuarioLogado());
		setMatriculaProfessor("");
		setListaConsultaProfessor(new ArrayList<>(0));
		inicializarListasSelectItemTodosComboBox();
		setTitulacaoCursoVO(getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(getHorarioTurmaVO().getTurma().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
		if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
			preencherListaProfessoresParaTitulacaoProfessorCursoPendente(null, null);
			getFacadeFactory().getItemTitulacaoCursoFacade().calcularQtdeProfessorNivelEscolaridade(getHashMapQtdeNivelEscolaridade(), getHashMapProfessorDisciplina().size(), getTitulacaoCursoVO());
		}
		isValidarPermissaoParaProgramacaoAulaComGoogleMeet();
		isValidarPermissaoParaProgramacaoAulaComClassroom();
		isValidarPermissaoParaProgramacaoAulaComBlackboard();
		inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
		setMensagemID("msg_dados_editar");
		setDisciplina_Erro("");
		setProfessor_Erro("");
		setTurma_Erro("");
		montarListaSelectItemLocal();
		Ordenacao.ordenarLista(getHorarioTurmaVO().getCalendarioHorarioAulaVOs(), "campoOrdenacao");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>ProgramacaoAula</code>. Caso o objeto seja novo (ainda
	 * não gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			setHorarioAlterado(false);
			getHorarioTurmaVO().montarDadosHorarioTurma();
			if (!getTitulacaoCursoVO().getCodigo().equals(0) && !getPanelGravarTitulacao()) {
				if (getTitulacaoCursoVO().getItemTitulacaoCursoVOsPreenchidasParcialmente()) {
					throw new ConsistirException("Titulação Preenchida Parcialmente");
				}
			}
			if (getHorarioTurmaVO().getCodigo().intValue() == 0) {
				getFacadeFactory().getHorarioTurmaFacade().incluir(getHorarioTurmaVO(), getUsuarioLogado());
			} else {
				getFacadeFactory().getHorarioTurmaFacade().alterar(getHorarioTurmaVO(), new ArrayList<HorarioProfessorVO>(0), true, getUsuarioLogado());
				setHorarioAlterado(true);
				setEnviarComunicadoPorEmail(true);
			}
			if (getHorarioTurmaVO().getHorarioTurmaDiaVOs().size() > 0) {
				setHorarioTurmaPorDiaVO(getHorarioTurmaVO().getHorarioTurmaDiaVOs().get(0));
			}
			setMensagemID("msg_dados_gravados");
			setPanelGravarTitulacao(false);
			if (getHorarioAlterado()) {
				return "";
			} else {
				return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaForm.xhtml");
			}
		} catch (Exception e) {
			if (e.getMessage().contains("Titulação Preenchida Parcialmente")) {
				setPanelGravarTitulacao(true);
				return "";
			}
			setPanelGravarTitulacao(false);
			setHorarioAlterado(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaForm.xhtml");
		}
	}

	public String gravarObservacao() {
		try {
			getFacadeFactory().getHorarioTurmaFacade().alterarObservacao(getHorarioTurmaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaForm.xhtml");
		}
	}

	private void montarListaTurmaAluno() {
		try {
			setHorarioTurmaVO(new HorarioTurmaVO());
			List<SelectItem> objs = new ArrayList<>(0);
			List<TurmaVO> lista = consultarListaTurmaAluno();
			Iterator<TurmaVO> i = lista.iterator();
			objs.add(new SelectItem("", ""));
			while (i.hasNext()) {
				TurmaVO obj = (TurmaVO) i.next();
				objs.add(new SelectItem(obj.getIdentificadorTurma(), obj.getCurso().getNome() + " - " + obj.getTurno().getNome()));
			}
			setListaSelectItemTurma(objs);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList<>(0));
		}
	}

	// public void montarHorarioTurma() {
	// if (!getHorarioTurmaVO().getTurma().getIdentificadorTurma().equals("")) {
	// consultarTurmaPorChavePrimaria();
	// } else {
	// setHorarioTurmaVO(new HorarioTurmaVO());
	// }
	// }
	@SuppressWarnings("unchecked")
	public List<TurmaVO> consultarListaTurmaAluno() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultarTurmaPorPessoa(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		List<SelectItem> lista = new ArrayList<>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ProgramacaoAulaCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList<>(0);
			// if (getControleConsulta().getCampoConsulta().equals("codigo")) {
			// if (getControleConsulta().getValorConsulta().equals("")) {
			// getControleConsulta().setValorConsulta("0");
			// }
			// int valorInt =
			// Integer.parseInt(getControleConsulta().getValorConsulta());
			// objs =
			// getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigo(new
			// Integer(valorInt), true,
			// Uteis.NIVELMONTARDADOS_TODOS);
			// }

			if(getControleConsulta().getValorConsulta().trim().length() < 3){
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio3")+" (Campo Turma)");
			}
			objs = getFacadeFactory().getHorarioTurmaFacade().consultaRapidaPorIdentificadorTurmaTurma(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), getSemestreVigente().trim(), getAnoVigente().trim(), getSituacaoTurma(), getSituacaoTipoTurma(), true, getUsuarioLogado());
			validarPreenchimentoCampoCursoTurmasAgrupadas(objs);
			// if
			// (getControleConsulta().getCampoConsulta().equals("codigoTurma"))
			// {
			// if (getControleConsulta().getValorConsulta().equals("")) {
			// getControleConsulta().setValorConsulta("0");
			// }
			// int valorInt =
			// Integer.parseInt(getControleConsulta().getValorConsulta());
			// objs =
			// getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurma(valorInt,
			// Uteis.NIVELMONTARDADOS_TODOS);
			// }
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaCons.xhtml");
		}
	}

	public  void validarPreenchimentoCampoCursoTurmasAgrupadas(List<HorarioTurmaVO> objs) throws Exception {
		for(HorarioTurmaVO horarioTurmaVO :  objs) {
			if(!Uteis.isAtributoPreenchido(horarioTurmaVO.getTurma().getCurso()) && horarioTurmaVO.getTurma().getTurmaAgrupada()) {
		    	CursoVO primeiroCursoTurmaAgrupada;
				try {
					primeiroCursoTurmaAgrupada = TurmaAgrupada.consultarTurmaAgrupadas(horarioTurmaVO.getTurma().getCodigo(), false, 0, getUsuarioLogado()).get(0).getTurma().getCurso();
					if(Uteis.isAtributoPreenchido(primeiroCursoTurmaAgrupada)) {
			    		horarioTurmaVO.getTurma().setCurso(primeiroCursoTurmaAgrupada);
			    	} 
				} catch (Exception e) {					
					throw e;
				}
		    	  	
		    }	
		}
	
		
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ProgramacaoAulaVO</code> Após a exclusão ela automaticamente aciona
	 * a rotina para uma nova inclusão.
	 */
	public String excluir() {
		boolean bloqueioComSucesso = false;
		try {
			executarBloqueioTurmaProgramacaoAula(true, true);
			bloqueioComSucesso = true;
			setHorarioAlterado(false);
			getFacadeFactory().getHorarioTurmaFacade().excluir(getHorarioTurmaVO(), getUsuarioLogado());
			ControleConcorrencia.liberarHorarioProfessor(horarioTurmaVO);
			if(isPermitirProgramacaoAulaComClassroom()) {
				getFacadeFactory().getClassroomGoogleFacade().realizarRevisaoClassroomPorTurma(getHorarioTurmaVO().getTurma(), getUsuarioLogadoClone());	
			}
			if(isPermitirProgramacaoAulaComBlackboard()) {
				getFacadeFactory().getSalaAulaBlackboardFacade().realizarRevisaoSalaAulaBlackboardPorTurma(getHorarioTurmaVO().getTurma(), getUsuarioLogadoClone());	
			}
			setMensagemID("msg_dados_excluidos");
			setHorarioAlterado(true);
			return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaForm.xhtml");
		} catch (Exception e) {
			ControleConcorrencia.liberarHorarioProfessor(horarioTurmaVO);
			setHorarioAlterado(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaForm.xhtml");
		} finally {
			try {
				if(bloqueioComSucesso){
					executarBloqueioTurmaProgramacaoAula(false, false);
					if (getHorarioAlterado()) {
						novo();
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>diaSemana</code>
	 */
	public List<SelectItem> getListaSelectItemDiaSemanaProgramacaoAula() throws Exception {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem("", ""));
		Hashtable diaSemanas = (Hashtable) Dominios.getDiaSemanaDisponibilidadeHorario();
		Enumeration keys = diaSemanas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) diaSemanas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemChave ordenador = new SelectItemOrdemChave();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public void montarListaSelectItemDisciplina(String prm) throws Exception {
		// if (getHorarioTurmaVO().getTurma().getTurmaAgrupada()) {
		// montarListaDisciplinaAgrupada();
		// } else {
		montarListaDisciplina();
		// }
	}

	public void montarListaDisciplinaAgrupada() throws Exception {
		montarListaDisciplina();
		// List resultadoConsulta = consultarDisciplinaTurmaAgrupada();
		// Iterator i = resultadoConsulta.iterator();
		// List objs = new ArrayList<>(0);
		// objs.add(new SelectItem(0, ""));
		// while (i.hasNext()) {
		// DisciplinaVO obj = (DisciplinaVO) i.next();
		// objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		// }
		// SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		// Collections.sort((List) objs, ordenador);
		// setListaSelectItemDisciplina(objs);
	}

	// public List<DisciplinaVO> consultarDisciplinaTurmaAgrupada() throws
	// Exception {
	// List<DisciplinaVO> objs =
	// getFacadeFactory().getDisciplinaFacade().consultarDisciplinaTurmaAgrupada(getHorarioTurmaVO().getTurma().getCodigo(),
	// Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	// return objs;
	// }

	public void montarListaDisciplina() throws Exception {
		if (getHorarioTurmaVO().getTurma().getCodigo().equals(0)) {
			getListaSelectItemDisciplina().clear();
			return;
		}
		getListaSelectItemDisciplina().clear();
		Boolean permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica = getLoginControle().getPermissaoAcessoMenuVO().getPermiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica();
		List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getHorarioTurmaVO().getTurma().getCodigo(), false, permiteProgramarAulaDisciplinaOnlineTipoTutoriaDinamica, getHorarioTurmaVO().getCodigo());
		getListaSelectItemDisciplina().add(new SelectItem(0, ""));
		for (HorarioTurmaDisciplinaProgramadaVO obj : horarioTurmaDisciplinaProgramadaVOs) {			
			getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigoDisciplina(), obj.getNomeDisciplina() + " - CH: " + obj.getChDisciplina()));
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Turno</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemDisciplina() {
		try {
			montarListaSelectItemDisciplina("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarDisciplinaPorCodigoPeriodoLetivo(Integer prm) throws Exception {
		List lista = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(prm, false, getUsuarioLogado(), null);
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemDisciplina();
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Pessoa</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			setDisciplina_Erro("");
			setProfessor_Erro("");
			PessoaVO pessoa = new PessoaVO();
			pessoa = getFacadeFactory().getPessoaFacade().consultarFuncionarioPorMatricula(getMatriculaProfessor(), "PR", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (!pessoa.getProfessor().equals(Boolean.TRUE)) {
				setMensagemID("msg_erro_dadosnaoencontrados");
				setMatriculaProfessor("");
				horarioTurmaVO.getProfessor().setNome("");
				horarioTurmaVO.getProfessor().setCodigo(0);
				this.setProfessor_Erro(getMensagemInternalizacao("msg_erro_professornaoencontrados"));
			} else {
				horarioTurmaVO.setProfessor(pessoa);
				this.setProfessor_Erro("");
				setMensagemID("msg_dados_consultados");
			}

			if (verificarPermissaoUsuarioIncluirFuncionarioCargo()) {
				FuncionarioVO fun = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(pessoa.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				montarListaSelectItemFuncionarioCargo(fun);
			}

		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			horarioTurmaVO.getProfessor().setNome("");
			horarioTurmaVO.getProfessor().setCodigo(0);
			this.setProfessor_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public void consultarPessoaSubstitutaPorChavePrimaria() {
		try {
			setDisciplina_Erro("");
			setProfessor_Erro("");
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarFuncionarioPorMatricula(getMatriculaProfessor(), "PR", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (!pessoa.getProfessor().equals(Boolean.TRUE)) {
				setMensagemID("msg_erro_dadosnaoencontrados");
				horarioTurmaVO.getProfessorSubstituto().setNome("");
				horarioTurmaVO.getProfessorSubstituto().setCodigo(0);
				this.setProfessor_Erro(getMensagemInternalizacao("msg_erro_professornaoencontrados"));
			} else {
				horarioTurmaVO.setProfessorSubstituto(pessoa);
				this.setProfessor_Erro("");
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			horarioTurmaVO.getProfessorSubstituto().setNome("");
			horarioTurmaVO.getProfessorSubstituto().setCodigo(0);
			this.setProfessor_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public void executarAdicionarDadosHorarioTurmaProfessorPorDiaPeriodoSemValidarDisponibilidade() {
		boolean bloqueioAdicionado = false;
		try {
			setAcaoModalChoqueHorario("");
			executarValidarAulaProgramadaParaDisciplina();			
			executarBloqueioTurmaProgramacaoAula(true, true);
			bloqueioAdicionado = true;
			setMensagemLiberacaoAulaProfessorAcimaPermitido("");
			setUsername("");
			setSenha("");
			int qtdeAulasProgramadas = getHorarioTurmaVO().getHorarioTurmaDiaVOs().size();
			getListaMensagemProgramarAulaPeriodo().clear();
			getListaChoqueHorariaAulaRegistrada().clear();
			getListaChoqueHorariaAula().clear();
			getListaChoqueHorariaAulaAcimaLimite().clear();
			getListaChoqueHorariaAulaRegistrada().clear();
			getListaChoqueHorariaSala().clear();
			ConfiguracaoGeralSistemaVO conf = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo());
			Integer disciplina = getHorarioTurmaVO().getDisciplina().getCodigo();
			Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> validacoes = getFacadeFactory().getHorarioTurmaFacade().executarAdicionarDadosHorarioTurmaProfessor(getHorarioTurmaVO(), getHorarioTurmaPorDiaVO(), !getProgramarAulaPeriodo(), getHorarioProfessorVO(), false, getUsuarioLogado(), conf.getControlarNumeroAulaProgramadoProfessorPorDia(), conf.getQuantidadeAulaMaximaProgramarProfessorDia(), getLiberarProgramacaoAulaProfessorAcimaPermitido());
			if (validacoes != null && !validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).isEmpty()) {
				setListaMensagemProgramarAulaPeriodo(validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO));
				realizarSeparacaoChoqueHorarioPorTipo();
				setListaMensagemProgramarAulaSemChoqueHorario(validacoes.get(TipoValidacaoChoqueHorarioEnum.SUCESSO));
			} else {
				verificarDisciplinaJaIncluida(getHorarioTurmaVO().getDisciplina());
				setTitulacaoCursoVO(getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(getHorarioTurmaVO().getTurma().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
					preencherListaProfessoresParaTitulacaoProfessorCursoPendente(getHorarioTurmaVO().getProfessor(), getHorarioTurmaVO().getDisciplinaAtual());
					getFacadeFactory().getItemTitulacaoCursoFacade().calcularQtdeProfessorNivelEscolaridade(getHashMapQtdeNivelEscolaridade(), getHashMapProfessorDisciplina().size(), getTitulacaoCursoVO());
				}
				if(isPermitirProgramacaoAulaComClassroom()) {
					getFacadeFactory().getClassroomGoogleFacade().realizarRevisaoClassroomPorTurma(getHorarioTurmaVO().getTurma(), getUsuarioLogadoClone());	
				}
				if(isPermitirProgramacaoAulaComBlackboard()) {
					getFacadeFactory().getSalaAulaBlackboardFacade().realizarRevisaoSalaAulaBlackboardPorTurma(getHorarioTurmaVO().getTurma(), getUsuarioLogadoClone());	
				}
				inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());	
				executarValidacaoGeracaoDadosSalaAulaIntegracao();
				getHorarioTurmaVO().setGerarEventoAulaOnLineGoogleMeet(false);
			}
			if (qtdeAulasProgramadas == getHorarioTurmaVO().getHorarioTurmaDiaVOs().size()) {
				removerDisciplinaAlterada();
			}
			setChoqueHorarioAlunoVOs(getFacadeFactory().getHorarioAlunoFacade().consultarChoqueHorarioAlunoPorTurmaAnoSemestre(getHorarioTurmaVO().getTurma(), getHorarioTurmaVO().getAnoVigente(), getHorarioTurmaVO().getSemestreVigente(), disciplina));
			if(!getChoqueHorarioAlunoVOs().isEmpty()){
				setAcaoModalChoqueHorario("RichFaces.$('panelChoqueHorarioAluno').show();");
			}
			setEnviarUsuarioEspecifico(Boolean.FALSE);			
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			ControleConcorrencia.liberarHorarioProfessor(horarioTurmaVO);
			try {
				if (bloqueioAdicionado) {
					executarBloqueioTurmaProgramacaoAula(false, false);
				}
				if(Uteis.isAtributoPreenchido(getHorarioTurmaPorDiaVO().getCodigo())){
					setHorarioTurmaPorDiaVO(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPorChavePrimaria(getHorarioTurmaPorDiaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				}
			} catch (Exception e) {
			}
		}
	}



	/**
	 * Método responsável por adicionar no Horario da Turma os Horários
	 * Selecionados do Professor.
	 * TODO
	 */
	public void executarAdicionarDadosHorarioTurmaProfessor() {
		boolean bloqueioAdicionado = false;
		Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> validacoes = null;
		boolean ocorreuExcecao = false;
		try {
			setEnviarUsuarioEspecifico(Boolean.FALSE);
			inicializarListaFuncionariosAptoReceberNotificacao();
			setAcaoModalChoqueHorario("");
			executarValidarAulaProgramadaParaDisciplina();
			executarBloqueioTurmaProgramacaoAula(true, true);	
			bloqueioAdicionado = true;
			setHorarioAlterado(false);
			setMensagemLiberacaoAulaProfessorAcimaPermitido("");
			setUsername("");
			setSenha("");
			getListaMensagemProgramarAulaPeriodo().clear();
			getListaChoqueHorariaAulaRegistrada().clear();
			getListaChoqueHorariaAula().clear();
			getListaChoqueHorariaAulaAcimaLimite().clear();
			getListaChoqueHorariaAulaRegistrada().clear();
			getListaChoqueHorariaSala().clear();
			ConfiguracaoGeralSistemaVO conf = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo());
			Integer disciplina = getHorarioTurmaVO().getDisciplina().getCodigo();
			getHorarioTurmaPorDiaVO().setFuncionarioCargoVO(getFuncionarioCargoVO());
			validacoes = getFacadeFactory().getHorarioTurmaFacade().executarAdicionarDadosHorarioTurmaProfessor(getHorarioTurmaVO(), getHorarioTurmaPorDiaVO(), !getProgramarAulaPeriodo(), getHorarioProfessorVO(), true, getUsuarioLogado(), conf.getControlarNumeroAulaProgramadoProfessorPorDia(), conf.getQuantidadeAulaMaximaProgramarProfessorDia(), getLiberarProgramacaoAulaProfessorAcimaPermitido());
			getHorarioProfessorVO().getHorarioProfessorDiaVOs();
			if (validacoes != null && !validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).isEmpty()) {
				setListaMensagemProgramarAulaPeriodo(validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO));
				realizarSeparacaoChoqueHorarioPorTipo();
				setListaMensagemProgramarAulaSemChoqueHorario(validacoes.get(TipoValidacaoChoqueHorarioEnum.SUCESSO));
				return;
			}			
			verificarDisciplinaJaIncluida(getHorarioTurmaVO().getDisciplina());
			setTitulacaoCursoVO(getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(getHorarioTurmaVO().getTurma().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
				preencherListaProfessoresParaTitulacaoProfessorCursoPendente(getHorarioTurmaVO().getProfessor(), getHorarioTurmaVO().getDisciplinaAtual());
				getFacadeFactory().getItemTitulacaoCursoFacade().calcularQtdeProfessorNivelEscolaridade(getHashMapQtdeNivelEscolaridade(), getHashMapProfessorDisciplina().size(), getTitulacaoCursoVO());
			}
			if(isPermitirProgramacaoAulaComClassroom()) {
				getFacadeFactory().getClassroomGoogleFacade().realizarRevisaoClassroomPorTurma(getHorarioTurmaVO().getTurma(), getUsuarioLogadoClone());	
			}
			if(isPermitirProgramacaoAulaComBlackboard()) {
				getFacadeFactory().getSalaAulaBlackboardFacade().realizarRevisaoSalaAulaBlackboardPorTurma(getHorarioTurmaVO().getTurma(), getUsuarioLogadoClone());	
			}
			inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
			executarValidacaoGeracaoDadosSalaAulaIntegracao();
			getHorarioTurmaVO().setGerarEventoAulaOnLineGoogleMeet(false);
			if(Uteis.isAtributoPreenchido(getHorarioTurmaPorDiaVO().getCodigo())){
				setHorarioTurmaPorDiaVO(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPorChavePrimaria(getHorarioTurmaPorDiaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				getHorarioTurmaPorDiaVO().setFuncionarioCargoVO(getFuncionarioCargoVO());
			}
			setHorarioAlterado(true);
			setChoqueHorarioAlunoVOs(getFacadeFactory().getHorarioAlunoFacade().consultarChoqueHorarioAlunoPorTurmaAnoSemestre(getHorarioTurmaVO().getTurma(), getHorarioTurmaVO().getAnoVigente(), getHorarioTurmaVO().getSemestreVigente(), disciplina));
			if(!getChoqueHorarioAlunoVOs().isEmpty()){
				setAcaoModalChoqueHorario("RichFaces.$('panelChoqueHorarioAluno').show();");
				setAcaoModalEnviarEmailAlunosTurmaPadrao("");
			}			
			else {
				setAcaoModalEnviarEmailAlunosTurmaPadrao("RichFaces.$('panelEnviarEmailAlunosTurmaPadrao').show();");
			}
			
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			e.printStackTrace();
			try{
				ocorreuExcecao = true;				
				getFacadeFactory().getHorarioTurmaFacade().carregarDadosHorarioTurma(getHorarioTurmaVO(), null, null, getUsuarioLogado());
				setAcaoModalEnviarEmailAlunosTurmaPadrao("");
			} catch (Exception ex) {
				
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally{
			if (bloqueioAdicionado) {
				try {
					executarBloqueioTurmaProgramacaoAula(false, false);
					if(Uteis.isAtributoPreenchido(getHorarioTurmaPorDiaVO().getCodigo())){
						setHorarioTurmaPorDiaVO(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPorChavePrimaria(getHorarioTurmaPorDiaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
						getHorarioTurmaPorDiaVO().setFuncionarioCargoVO(getFuncionarioCargoVO());
					}
				} catch (Exception e) {					
				}
			}
			ControleConcorrencia.liberarHorarioProfessor(horarioTurmaVO);
			if ((validacoes == null && !ocorreuExcecao) || (validacoes != null && validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).isEmpty())) {			
				limparDadosDisciplina();
				limparDadosProfessor();
			}
		}
	}

	private void executarValidacaoGeracaoDadosSalaAulaIntegracao() throws Exception {		
		if(getHorarioTurmaVO().getGerarEventoAulaOnLineGoogleMeet()) {
			getFacadeFactory().getGoogleMeetInterfaceFacade().realizarGeracaoEventoGoogleMeetIncluirVisaoAdministrativa(getHorarioTurmaVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo()), getUsuarioLogado());	
		}else if(getHorarioTurmaVO().isGerarClassroomGoogle()) {
			getFacadeFactory().getClassroomGoogleFacade().realizarGeracaoClassroomGooglePorHorarioTurma(getHorarioTurmaVO(), getUsuarioLogadoClone());			
		}
		if(getHorarioTurmaVO().isGerarSalaBlackboard()) {
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboardPorHorarioTurma(getHorarioTurmaVO(), getUsuarioLogadoClone());
		}
		if(getHorarioTurmaVO().isGerarSalaBlackboard() || getHorarioTurmaVO().isGerarClassroomGoogle()) {
			inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
		}
		
	}	

	

	public void adicionarProfessorListaProfessoresSelecionados(PessoaVO professor, DisciplinaVO disciplinaVO) throws Exception {
		if (professor != null && !professor.getCodigo().equals(0) && disciplinaVO != null && !disciplinaVO.getCodigo().equals(0)) {
			if (!getHashMapProfessorDisciplina().containsKey("P" + professor.getCodigo().toString() + "D" + disciplinaVO.getCodigo().toString())) {
				if (!professor.getFormacaoAcademicaVOs().isEmpty()) {
					getFacadeFactory().getItemTitulacaoCursoFacade().adicionarQtdeNivelEscolaridade(getHashMapQtdeNivelEscolaridade(), professor);
				}
				getHashMapProfessorDisciplina().put("P" + professor.getCodigo().toString() + "D" + disciplinaVO.getCodigo().toString(), professor.getNome() + " - " + disciplinaVO.getNome());
			}
		}
	}

	public void removerDisciplinaAlterada() {
		if (!getHorarioTurmaVO().getDisciplina().getCodigo().equals(0)) {
			getHashDisciplinasAlteradas().remove(getHorarioTurmaVO().getDisciplina().getCodigo());
			getListaDisciplinasAlteradasCodigo().remove(getHorarioTurmaVO().getDisciplina().getCodigo());
		}
	}

	public String getFecharModalHorarioProfessor() {
		if (!getListaMensagemProgramarAulaPeriodo().isEmpty()) {
			setAcaoModalEnviarEmailAlunosTurmaPadrao("");
			return "RichFaces.$('panelConfirmarHorario').show();";
		}		
		return "RichFaces.$('panelProfessorHorario').hide();";				
	}

	public boolean getApresentarHorarioProfessor() {
		if (getHorarioTurmaVO().getDia() != null) {
			return true;
		} else if (getProgramarAulaPeriodo()) {
			return true;
		}
		return false;
	}

	public void inicializarDadosDisponibilidadeHorarioDiaDiaAvulso() {
		try {
			setHorarioProfessorVO(null);
			getHorarioTurmaVO().setDia(null);
			getListaChoqueHorariaAulaRegistrada().clear();
			getListaChoqueHorariaAula().clear();
			getListaChoqueHorariaAulaAcimaLimite().clear();
			getListaChoqueHorariaAulaRegistrada().clear();
			getListaChoqueHorariaSala().clear();
			getFacadeFactory().getHorarioTurmaFacade().inicializarDadosProgramarAulaDiaDiaAvulso(getHorarioTurmaVO());
			setProgramarAulaPeriodo(Boolean.FALSE);
			inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
			setMensagemDetalhada("", "");
			setMensagem("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosDisponibilidadeHorarioDiaDiaPeriodo() {
		try {
			setHorarioProfessorVO(null);
			getListaChoqueHorariaAulaRegistrada().clear();
			getListaChoqueHorariaAula().clear();
			getListaChoqueHorariaAulaAcimaLimite().clear();
			getListaChoqueHorariaAulaRegistrada().clear();
			getListaChoqueHorariaSala().clear();
			setHorarioProfessorVO(getFacadeFactory().getHorarioTurmaFacade().inicializarDadosProgramarAulaDiaDiaPeriodo(getHorarioTurmaVO(), getUsuarioLogado()));
			setProgramarAulaPeriodo(Boolean.TRUE);
			inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
			setMensagemDetalhada("", "");
			setMensagem("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarAtualizacaoDisponibilidadeHorarioProfessorProgramacaoAulaDiaADiaAvulso() {
		try {
			setMensagemDetalhada("", "");
			setMensagem("");
			setProgramarAulaPeriodo(Boolean.FALSE);
			setHorarioProfessorVO(getFacadeFactory().getHorarioTurmaFacade().executarAtualizacaoDisponibilidadeHorarioProfessorProgramacaoAulaDiaADiaAvulso(getHorarioTurmaVO(), getUsuarioLogado()));

		} catch (Exception e) {
			setHorarioProfessorVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarMigrarDadosBD() {
		try {
			setMensagemDetalhada("", "");
			setMensagem("");
			getFacadeFactory().getHorarioTurmaFacade().executarMigracaoDados(getUsuarioLogadoClone());
			// getFacadeFactory().getHorarioProfessorFacade().executarMigracaoDados();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getApresentarDomingo() {
		if ((getProgramarAulaPeriodo() || Uteis.getDiaSemana(getHorarioTurmaVO().getDia()) == 1) && getHorarioProfessorVO().getIsExisteHorarioDomingo()) {
			return true;
		}
		return false;
	}

	public boolean getApresentarSegunda() {
		if ((getProgramarAulaPeriodo() || Uteis.getDiaSemana(getHorarioTurmaVO().getDia()) == 2) && getHorarioProfessorVO().getIsExisteHorarioSegunda()) {
			return true;
		}
		return false;
	}

	public boolean getApresentarTerca() {
		if ((getProgramarAulaPeriodo() || Uteis.getDiaSemana(getHorarioTurmaVO().getDia()) == 3) && getHorarioProfessorVO().getIsExisteHorarioTerca()) {
			return true;
		}
		return false;
	}

	public boolean getApresentarQuarta() {
		if ((getProgramarAulaPeriodo() || Uteis.getDiaSemana(getHorarioTurmaVO().getDia()) == 4) && getHorarioProfessorVO().getIsExisteHorarioQuarta()) {
			return true;
		}
		return false;
	}

	public boolean getApresentarQuinta() {
		if ((getProgramarAulaPeriodo() || Uteis.getDiaSemana(getHorarioTurmaVO().getDia()) == 5) && getHorarioProfessorVO().getIsExisteHorarioQuinta()) {
			return true;
		}
		return false;
	}

	public boolean getApresentarSexta() {
		if ((getProgramarAulaPeriodo() || Uteis.getDiaSemana(getHorarioTurmaVO().getDia()) == 6) && getHorarioProfessorVO().getIsExisteHorarioSexta()) {
			return true;
		}
		return false;
	}

	public boolean getApresentarSabado() {
		if ((getProgramarAulaPeriodo() || Uteis.getDiaSemana(getHorarioTurmaVO().getDia()) == 7) && getHorarioProfessorVO().getIsExisteHorarioSabado()) {
			return true;
		}
		return false;
	}

	//
	// public void
	// executarAtualizacaoDisponibilidadeHorarioProfessorProgramacaoAulaSemanal()
	// {
	// try {
	// setMensagemDetalhada("", "");
	// setMensagem("");
	// setHorarioProfessorVO(getFacadeFactory().getHorarioTurmaFacade().executarAtualizacaoDisponibilidadeHorarioProfessorProgramacaoAulaSemanal(getHorarioTurmaVO()));
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }

	public Boolean getApresentarProgramarAulaDiaDiaAvulso() {
		// if (!getHorarioTurmaVO().getSemanal() && !getProgramarAulaPeriodo())
		// {sele
		if (!getProgramarAulaPeriodo()) {
			return true;
		}
		return false;
	}

	public Boolean getApresentarProgramarAulaDiaDiaPeriodo() {
		// if (!getHorarioTurmaVO().getSemanal() && getProgramarAulaPeriodo()) {
		if (getProgramarAulaPeriodo()) {
			return true;
		}
		return false;
	}

	public String getAbriModalHorarioProfessor() {
		if (getMensagemDetalhada() == null || getMensagemDetalhada().equals("")) {
			return "RichFaces.$('panelProfessorHorario').show()";
		}
		return "";
	}

	/**
	 * Operação responsável por processar a consulta pelo parâmetro informado
	 * pelo usuário.<code>ProfessorVO</code> Após a consulta ela automaticamente
	 * adciona o código e o nome da cidade na tela.
	 */
	public List consultarPessoaSuggestionbox(Object event) {
		try {
			String valor = event.toString();
			List lista = getFacadeFactory().getPessoaFacade().consultarPorNome(valor, "PR", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return lista;
		} catch (Exception e) {
			return new ArrayList<>(0);
		}
	}

	public void selecionarProfessor() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			FuncionarioVO fun = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getHorarioTurmaVO().setProfessor(obj);
			getHorarioTurmaVO().getProfessor().getTitulacaoNivelEscolaridade();
			setMatriculaProfessor(fun.getMatricula());
			
			montarListaSelectItemFuncionarioCargo(fun);
		} catch (Exception e) {
			setMensagemDetalhada("mag_erro", e.getMessage());
		}

	}

	/**
	 * Monta Lista de {@link SelectItem} do {@link FuncionarioCargoVO} pelo {@link FuncionarioVO} informado.
	 * 
	 * @param funcionario
	 * @throws Exception
	 */
	private void montarListaSelectItemFuncionarioCargo(FuncionarioVO funcionario) throws Exception {
		if (verificarPermissaoUsuarioIncluirFuncionarioCargo()) {
			listaSelectItemFuncionarioCargo.clear();
			@SuppressWarnings("unchecked")
			List<FuncionarioCargoVO> lista = getFacadeFactory().getFuncionarioCargoFacade().consultarPorFuncionario(funcionario.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			listaSelectItemFuncionarioCargo.add(new SelectItem("", ""));
			for (FuncionarioCargoVO funcionarioCargoVO : lista) {
				if (funcionarioCargoVO.getSituacaoFuncionario().equals(SituacaoFuncionarioEnum.ATIVO.name())) {					
					listaSelectItemFuncionarioCargo.add(new SelectItem(funcionarioCargoVO.getCodigo(), funcionarioCargoVO.getMatriculaCargo() + " - " + funcionarioCargoVO.getCargo().getNome()));
				}
			}
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getHorarioTurmaVO().setTurma(obj);
			getHorarioTurmaVO().setTurno(getHorarioTurmaVO().getTurma().getTurno());			
			consultarTurmaPorChavePrimaria();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void preencherListaProfessoresParaTitulacaoProfessorCursoPendente(PessoaVO pessoaVO, DisciplinaVO disciplinaVO) throws Exception {
		setHashMapProfessorDisciplina(null);
		setHashMapQtdeNivelEscolaridade(null);
		setListaProfessoresSelecionados(null);
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : getHorarioTurmaVO().getHorarioTurmaDiaVOs()) {
			for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
				if (!getHashMapProfessorDisciplina().containsKey("P" + horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo().toString() + "D" + horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo().toString())) {
					adicionarProfessorListaProfessoresSelecionados(horarioTurmaDiaItemVO.getFuncionarioVO(), horarioTurmaDiaItemVO.getDisciplinaVO());
				}
			}
		}
		if (pessoaVO != null) {
			adicionarProfessorListaProfessoresSelecionados(pessoaVO, disciplinaVO);
		}
	}

	// public void
	// preencherListaProfessoresParaTitulacaoProfessorCursoPendente(PessoaVO
	// pessoaVO, DisciplinaVO disciplinaVO) throws Exception {
	// setHashMapProfessorDisciplina(null);
	// setHashMapQtdeNivelEscolaridade(null);
	// setListaProfessoresSelecionados(null);
	// for (HorarioTurmaDiaVO horarioTurmaDiaVO :
	// getHorarioTurmaVO().getHorarioTurmaDiaVOs()) {
	// for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO :
	// horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
	// if
	// (!getListaProfessoresSelecionados().contains(horarioTurmaDiaItemVO.getFuncionarioVO()))
	// {
	// adicionarProfessorListaProfessoresSelecionados(horarioTurmaDiaItemVO.getFuncionarioVO(),
	// horarioTurmaDiaItemVO.getDisciplinaVO());
	// }
	// }
	// }
	// if (pessoaVO != null) {
	// adicionarProfessorListaProfessoresSelecionados(pessoaVO, disciplinaVO);
	// }
	// }

	public boolean getIsApresentarAnoSemestre() {
		if (getHorarioTurmaVO().getTurma().getSemestral() || getHorarioTurmaVO().getTurma().getAnual()) {
			isApresentarAnoSemestre = true;
		} else {
			isApresentarAnoSemestre = false;
		}
		return isApresentarAnoSemestre;
	}

	public void setIsApresentarAnoSemestre(Boolean isApresentarAnoSemestre) {
		this.isApresentarAnoSemestre = isApresentarAnoSemestre;
	}

	public void setIsApresentarDisciplinaEProfessor(Boolean isApresentarDisciplinaEProfessor) {
		this.isApresentarDisciplinaEProfessor = isApresentarDisciplinaEProfessor;
	}

	public boolean getIsApresentarDisciplinaEProfessor() {
		if (!getIsApresentarAnoSemestre() || (getIsApresentarAnoSemestre() && !getHorarioTurmaVO().getAnoVigente().equals("") && !getHorarioTurmaVO().getSemestreVigente().equals("")) || (getIsApresentarSomenteAno() && !getHorarioTurmaVO().getAnoVigente().equals(""))) {
			this.isApresentarDisciplinaEProfessor = true;
		} else {
			this.isApresentarDisciplinaEProfessor = false;
		}
		return isApresentarDisciplinaEProfessor;
	}

	public void selecionarProfessorSubstituto() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			horarioTurmaVO.setProfessorSubstituto(obj);
			FuncionarioVO fun = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setMatriculaProfessor(fun.getMatricula());
			
			montarListaSelectItemFuncionarioCargo(fun);
		} catch (Exception e) {
		}

	}

	public void carregarProfessoresInteressadosDisciplina() {
		try {
			if (!getHorarioTurmaVO().getDisciplina().getCodigo().equals(0)) {
				getHorarioTurmaVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getHorarioTurmaVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone()));
				setListaProfessoresInteressadosDisciplina(getFacadeFactory().getPessoaFacade().consultarProfessoresInteressadosDisciplina(getHorarioTurmaVO().getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			} else {
				getListaProfessoresInteressadosDisciplina().clear();
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void carregarProfessoresInteressadosDisciplinaSubstituta() {
		try {
			if (!horarioTurmaVO.getDisciplinaSubstituta().getCodigo().equals(0)) {
				getFacadeFactory().getHorarioTurmaFacade().realizarVerificacaoExisteAulaProgramadaParaDisciplinaEEquivalenteParaOutraTurmaOuTurmaAgrupada(getHorarioTurmaVO().getTurma(), getHorarioTurmaVO().getDisciplinaSubstituta().getCodigo(), getHorarioTurmaVO().getAnoVigente(), getHorarioTurmaVO().getSemestreVigente(), true);							
				// getHorarioTurmaVO().setDisciplinaSubstituta(consultarDisciplinaPorChavePrimaria(getHorarioTurmaVO().getDisciplinaSubstituta().getCodigo()));
				setListaProfessoresInteressadosDisciplina(getFacadeFactory().getPessoaFacade().consultarProfessoresInteressadosDisciplina(horarioTurmaVO.getDisciplinaSubstituta().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));

			} else {
				getListaProfessoresInteressadosDisciplina().clear();
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// public DisciplinaVO consultarDisciplinaPorChavePrimaria(Integer
	// codigoDisciplina) throws Exception {
	// DisciplinaVO obj =
	// getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(codigoDisciplina,
	// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	// return obj;
	// }
	public void editarDiaExcluir() {
		HorarioTurmaDiaItemVO obj = (HorarioTurmaDiaItemVO) context().getExternalContext().getRequestMap().get("horarioProgramacaoDiaItemExcluir");
		executarEditarDia(obj);
	}

	public void executarEditarDia(HorarioTurmaDiaItemVO obj) {
		try {
			setDisponibilidadeSubstituta(new DisponibilidadeHorarioTurmaProfessorVO());
			setListaProfessoresInteressadosDisciplina(new ArrayList<>(0));
			HorarioTurmaDiaItemVO novo = getFacadeFactory().getHorarioTurmaDiaItemFacade().consultarFuncionarioCargoPorCodigo(obj.getCodigo(), getUsuarioLogado()); 
			obj.setFuncionarioCargoVO(novo.getFuncionarioCargoVO());
			getHorarioTurmaVO().getDisciplinaAtual().setCodigo(obj.getDisciplinaVO().getCodigo());
			getHorarioTurmaVO().getDisciplinaAtual().setNome(obj.getDisciplinaVO().getNome());
			getHorarioTurmaVO().getDisciplinaSubstituta().setCodigo(obj.getDisciplinaVO().getCodigo());
			getHorarioTurmaVO().getDisciplinaSubstituta().setNome(obj.getDisciplinaVO().getNome());
			getHorarioTurmaVO().getProfessorAtual().setCodigo(obj.getFuncionarioVO().getCodigo());
			getHorarioTurmaVO().getProfessorAtual().setNome(obj.getFuncionarioVO().getNome());
			getHorarioTurmaVO().getProfessorSubstituto().setCodigo(obj.getFuncionarioVO().getCodigo());
			getHorarioTurmaVO().getProfessorSubstituto().setNome(obj.getFuncionarioVO().getNome());
			getHorarioTurmaVO().getSalaAtual().setCodigo(obj.getSala().getCodigo());
			getHorarioTurmaVO().getSalaAtual().setSala(obj.getSala().getSala());
			getHorarioTurmaVO().getSalaAtual().getLocalAula().setLocal(obj.getSala().getLocalAula().getLocal());
			getHorarioTurmaVO().getSalaAtual().getLocalAula().setCodigo(obj.getSala().getLocalAula().getCodigo());
			getHorarioTurmaVO().getSalaSubstituta().setCodigo(obj.getSala().getCodigo());
			getHorarioTurmaVO().getSalaSubstituta().setSala(obj.getSala().getSala());
			getHorarioTurmaVO().getSalaSubstituta().getLocalAula().setLocal(obj.getSala().getLocalAula().getLocal());
			getHorarioTurmaVO().getSalaSubstituta().getLocalAula().setCodigo(obj.getSala().getLocalAula().getCodigo());
			getHorarioTurmaPorDiaVO().setAulaReposicao(obj.getAulaReposicao());

			FuncionarioVO fun = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getFuncionarioVO().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			montarListaSelectItemFuncionarioCargo(fun);
			setFuncionarioCargoVO(obj.getFuncionarioCargoVO());

			montarListaSelectItemSalaSubstituta();
			getDisponibilidadeSubstituta().setData(getHorarioTurmaPorDiaVO().getData());
			getDisponibilidadeSubstituta().setNrAula(obj.getNrAula());
			getDisponibilidadeSubstituta().setProfessor(obj.getFuncionarioVO());
			getDisponibilidadeSubstituta().setDisciplina(obj.getDisciplinaVO());		
			setProfessor(obj.getFuncionarioVO().getCodigo());
			setDisciplina(obj.getDisciplinaVO().getCodigo());
			setNumeroAula(obj.getNrAula());
			setMatriculaProfessor("");
			setAlterarTodasAulas(false);
			setRetornarExcecaoAlteracaoComAulaRegistrada(true);
			setAlteracaoComAulaRegistrada(false);
			limparMensagem();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void editarDia() {
		try {
			HorarioTurmaDiaItemVO obj = (HorarioTurmaDiaItemVO) context().getExternalContext().getRequestMap().get("horarioProgramacaoDiaItemItens");
			setHorarioTurmaDiaItemSelecionadoVO(obj);
			getHorarioTurmaDiaItemSelecionadoVO().setGoogleMeetVO(getFacadeFactory().getGoogleMeetInterfaceFacade().consultarPorHorarioTurmaDiaItem(obj, Uteis.NIVELMONTARDADOS_TODOS));
			executarEditarDia(obj);
			// getFacadeFactory().getRegistroAulaFacade().verificarVinculoRegistroAula(horarioTurmaVO.getTurma().getCodigo(),
			// horarioTurmaVO.getDisciplinaAtual().getCodigo(),
			// horarioTurmaVO.getProfessorAtual().getCodigo(),
			// getHorarioTurmaPorDiaVO().getData());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void editarAulaResposicao() {
		try {
			HorarioTurmaDiaItemVO obj = (HorarioTurmaDiaItemVO) context().getExternalContext().getRequestMap().get("horarioProgramacaoDiaItemItens");
			
			getFacadeFactory().getHorarioTurmaDiaItemFacade().alterarAulaResposicao(obj.getAulaReposicao(), obj.getCodigo(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	// public void editarSegunda() {
	// setDisponibilidadeSubstituta(new
	// DisponibilidadeHorarioTurmaProfessorVO());
	// setListaProfessoresInteressadosDisciplina(new ArrayList<>(0));
	// DisponibilidadeHorarioTurmaProfessorVO obj =
	// (DisponibilidadeHorarioTurmaProfessorVO)
	// context().getExternalContext().getRequestMap().get("horarioProgramacaoSeg");
	// getHorarioTurmaVO().getDisciplinaAtual().setCodigo(obj.getDisciplina().getCodigo());
	// getHorarioTurmaVO().getDisciplinaAtual().setNome(obj.getDisciplina().getNome());
	// getHorarioTurmaVO().getProfessorAtual().setCodigo(obj.getProfessor().getCodigo());
	// getHorarioTurmaVO().getProfessorAtual().setNome(obj.getProfessor().getNome());
	// setDisponibilidadeSubstituta(obj);
	// getDisponibilidadeSubstituta().setDiaSemana(DiaSemana.SEGUNGA);
	// setMatriculaProfessor("");
	// setNumeroAula(obj.getNrAula());
	// setDiaSemana(DiaSemana.SEGUNGA);
	// getHorarioTurmaVO().setTipoHorarioTurma(TipoHorarioTurma.SEMANAL);
	// }
	//
	// public void editarTerca() {
	// setDisponibilidadeSubstituta(new
	// DisponibilidadeHorarioTurmaProfessorVO());
	// setListaProfessoresInteressadosDisciplina(new ArrayList<>(0));
	// DisponibilidadeHorarioTurmaProfessorVO obj =
	// (DisponibilidadeHorarioTurmaProfessorVO)
	// context().getExternalContext().getRequestMap().get("horarioProgramacaoTer");
	// getHorarioTurmaVO().getDisciplinaAtual().setCodigo(obj.getDisciplina().getCodigo());
	// getHorarioTurmaVO().getDisciplinaAtual().setNome(obj.getDisciplina().getNome());
	// getHorarioTurmaVO().getProfessorAtual().setCodigo(obj.getProfessor().getCodigo());
	// getHorarioTurmaVO().getProfessorAtual().setNome(obj.getProfessor().getNome());
	// setDisponibilidadeSubstituta(obj);
	// getDisponibilidadeSubstituta().setDiaSemana(DiaSemana.TERCA);
	// setMatriculaProfessor("");
	// setNumeroAula(obj.getNrAula());
	// setDiaSemana(DiaSemana.TERCA);
	// getHorarioTurmaVO().setTipoHorarioTurma(TipoHorarioTurma.SEMANAL);
	// }
	//
	// public void editarQuarta() {
	// setDisponibilidadeSubstituta(new
	// DisponibilidadeHorarioTurmaProfessorVO());
	// setListaProfessoresInteressadosDisciplina(new ArrayList<>(0));
	// DisponibilidadeHorarioTurmaProfessorVO obj =
	// (DisponibilidadeHorarioTurmaProfessorVO)
	// context().getExternalContext().getRequestMap().get("horarioProgramacaoQua");
	// getHorarioTurmaVO().getDisciplinaAtual().setCodigo(obj.getDisciplina().getCodigo());
	// getHorarioTurmaVO().getDisciplinaAtual().setNome(obj.getDisciplina().getNome());
	// getHorarioTurmaVO().getProfessorAtual().setCodigo(obj.getProfessor().getCodigo());
	// getHorarioTurmaVO().getProfessorAtual().setNome(obj.getProfessor().getNome());
	// setDisponibilidadeSubstituta(obj);
	// getDisponibilidadeSubstituta().setDiaSemana(DiaSemana.QUARTA);
	// setMatriculaProfessor("");
	// setNumeroAula(obj.getNrAula());
	// setDiaSemana(DiaSemana.QUARTA);
	// getHorarioTurmaVO().setTipoHorarioTurma(TipoHorarioTurma.SEMANAL);
	// }
	//
	// public void editarQuinta() {
	// setDisponibilidadeSubstituta(new
	// DisponibilidadeHorarioTurmaProfessorVO());
	// setListaProfessoresInteressadosDisciplina(new ArrayList<>(0));
	// DisponibilidadeHorarioTurmaProfessorVO obj =
	// (DisponibilidadeHorarioTurmaProfessorVO)
	// context().getExternalContext().getRequestMap().get("horarioProgramacaoQui");
	// getHorarioTurmaVO().getDisciplinaAtual().setCodigo(obj.getDisciplina().getCodigo());
	// getHorarioTurmaVO().getDisciplinaAtual().setNome(obj.getDisciplina().getNome());
	// getHorarioTurmaVO().getProfessorAtual().setCodigo(obj.getProfessor().getCodigo());
	// getHorarioTurmaVO().getProfessorAtual().setNome(obj.getProfessor().getNome());
	// setDisponibilidadeSubstituta(obj);
	// getDisponibilidadeSubstituta().setDiaSemana(DiaSemana.QUINTA);
	// setMatriculaProfessor("");
	// setNumeroAula(obj.getNrAula());
	// setDiaSemana(DiaSemana.QUINTA);
	// getHorarioTurmaVO().setTipoHorarioTurma(TipoHorarioTurma.SEMANAL);
	// }
	//
	// public void editarSexta() {
	// setDisponibilidadeSubstituta(new
	// DisponibilidadeHorarioTurmaProfessorVO());
	// setListaProfessoresInteressadosDisciplina(new ArrayList<>(0));
	// DisponibilidadeHorarioTurmaProfessorVO obj =
	// (DisponibilidadeHorarioTurmaProfessorVO)
	// context().getExternalContext().getRequestMap().get("horarioProgramacaoSex");
	// getHorarioTurmaVO().getDisciplinaAtual().setCodigo(obj.getDisciplina().getCodigo());
	// getHorarioTurmaVO().getDisciplinaAtual().setNome(obj.getDisciplina().getNome());
	// getHorarioTurmaVO().getProfessorAtual().setCodigo(obj.getProfessor().getCodigo());
	// getHorarioTurmaVO().getProfessorAtual().setNome(obj.getProfessor().getNome());
	// setDisponibilidadeSubstituta(obj);
	// getDisponibilidadeSubstituta().setDiaSemana(DiaSemana.SEXTA);
	// setMatriculaProfessor("");
	// setNumeroAula(obj.getNrAula());
	// setDiaSemana(DiaSemana.SEXTA);
	// getHorarioTurmaVO().setTipoHorarioTurma(TipoHorarioTurma.SEMANAL);
	// }
	//
	// public void editarSabado() {
	// setDisponibilidadeSubstituta(new
	// DisponibilidadeHorarioTurmaProfessorVO());
	// setListaProfessoresInteressadosDisciplina(new ArrayList<>(0));
	// DisponibilidadeHorarioTurmaProfessorVO obj =
	// (DisponibilidadeHorarioTurmaProfessorVO)
	// context().getExternalContext().getRequestMap().get("horarioProgramacaoSab");
	// getHorarioTurmaVO().getDisciplinaAtual().setCodigo(obj.getDisciplina().getCodigo());
	// getHorarioTurmaVO().getDisciplinaAtual().setNome(obj.getDisciplina().getNome());
	// getHorarioTurmaVO().getProfessorAtual().setCodigo(obj.getProfessor().getCodigo());
	// getHorarioTurmaVO().getProfessorAtual().setNome(obj.getProfessor().getNome());
	// setDisponibilidadeSubstituta(obj);
	// getDisponibilidadeSubstituta().setDiaSemana(DiaSemana.SABADO);
	// setNumeroAula(obj.getNrAula());
	// setDiaSemana(DiaSemana.SABADO);
	// getHorarioTurmaVO().setTipoHorarioTurma(TipoHorarioTurma.SEMANAL);
	// }
	//
	// public void editarDomingo() {
	// setDisponibilidadeSubstituta(new
	// DisponibilidadeHorarioTurmaProfessorVO());
	// setListaProfessoresInteressadosDisciplina(new ArrayList<>(0));
	// DisponibilidadeHorarioTurmaProfessorVO obj =
	// (DisponibilidadeHorarioTurmaProfessorVO)
	// context().getExternalContext().getRequestMap().get("horarioProgramacaoDom");
	// getHorarioTurmaVO().getDisciplinaAtual().setCodigo(obj.getDisciplina().getCodigo());
	// getHorarioTurmaVO().getDisciplinaAtual().setNome(obj.getDisciplina().getNome());
	// getHorarioTurmaVO().getProfessorAtual().setCodigo(obj.getProfessor().getCodigo());
	// getHorarioTurmaVO().getProfessorAtual().setNome(obj.getProfessor().getNome());
	// setDisponibilidadeSubstituta(obj);
	// getDisponibilidadeSubstituta().setDiaSemana(DiaSemana.DOMINGO);
	// setMatriculaProfessor("");
	// setNumeroAula(obj.getNrAula());
	// setDiaSemana(DiaSemana.DOMINGO);
	// getHorarioTurmaVO().setTipoHorarioTurma(TipoHorarioTurma.SEMANAL);
	// }

	// public void gravarEdicaoTodosHorarios() {
	// try {
	// getFacadeFactory().getHorarioTurmaFacade().executarAlteracaoAulaHorarioTurmaProfessorDisciplina(getHorarioTurmaVO(),
	// getHorarioTurmaPorDiaVO(), getNumeroAula(), getAlterarTodasAulas(),
	// getUsuarioLogado(), getRetornarExcecaoAlteracaoComAulaRegistrada());
	// //
	// getFacadeFactory().getHorarioTurmaFacade().executarSubstituicaoProfessorDisciplina(getHorarioTurmaVO(),
	// null, 0, null, false, getUsuarioLogado());
	// setListaProfessoresInteressadosDisciplina(new ArrayList<>(0));
	// // if
	// //
	// (getHorarioTurmaVO().getTipoHorarioTurma().equals(TipoHorarioTurma.DIARIO)
	// // && getProfessor() > 0) {
	// if (getProfessor() > 0) {
	// montarListaSelectItemProfessorComAulaProgramadaTurma();
	// executarMontagemListaAulaProfessor();
	// }
	// setMatriculaProfessor("");
	// setMensagemID("msg_dados_gravados");
	// } catch (Exception e) {
	//
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }

	public Boolean getAlterarTodasAulas() {
		if (alterarTodasAulas == null) {
			alterarTodasAulas = false;
		}
		return alterarTodasAulas;
	}

	public void setAlterarTodasAulas(Boolean alterarTodasAulas) {
		this.alterarTodasAulas = alterarTodasAulas;
	}

	//TODO
	public void gravarEdicaoHorario() {
		boolean bloqueioAdicionado = false;
		DisciplinaVO disciplinaSubstituta = null;
		getHorarioTurmaPorDiaVO().setFuncionarioCargoVO(getFuncionarioCargoVO());
		try {
			getFacadeFactory().getHorarioTurmaFacade().realizarVerificacaoExisteAulaProgramadaParaDisciplinaEEquivalenteParaOutraTurmaOuTurmaAgrupada(getHorarioTurmaVO().getTurma(), getHorarioTurmaVO().getDisciplinaSubstituta().getCodigo(), getHorarioTurmaVO().getAnoVigente(), getHorarioTurmaVO().getSemestreVigente(), true);							
			executarBloqueioTurmaProgramacaoAula(true, true);
			bloqueioAdicionado = true;
			setErroExclusao(Boolean.FALSE);
			if (verificarPermissaoUsuarioExcluirHorarioTurmaDia()) {
				if (getHorarioTurmaPorDiaVO().getUsuarioResp().getCodigo().intValue() != getUsuarioLogado().getCodigo().intValue()) {
					setErroExclusao(Boolean.TRUE);
					throw new Exception("Não é possível realizar a alteração da aula selecionada! Somente o usuário " + getHorarioTurmaPorDiaVO().getUsuarioResp().getNome() + " tem permissão para alterar este registro,ele é o responsável pelo cadastro! ");
				}
			}
			setAlteracaoComAulaRegistrada(false);
			setMensagemLiberacaoAulaProfessorAcimaPermitido("");
			setSenha("");
			setUsername("");
			setHorarioAlterado(false);
			disciplinaSubstituta = new DisciplinaVO();
			disciplinaSubstituta.setCodigo(getHorarioTurmaVO().getDisciplinaSubstituta().getCodigo());
			disciplinaSubstituta.setNome(getHorarioTurmaVO().getDisciplinaSubstituta().getNome());
			getListaMensagemProgramarAulaPeriodo().clear();
			getListaMensagemProgramarAulaSemChoqueHorario().clear();
			getHorarioTurmaVO().getHorarioTurmaDiaItemGoogleMeet().clear();
			ConfiguracaoGeralSistemaVO conf = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo());
			Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> validacoes = getFacadeFactory().getHorarioTurmaFacade().executarAlteracaoAulaHorarioTurmaProfessorDisciplina(getHorarioTurmaVO(), getHorarioTurmaPorDiaVO(), getNumeroAula(), getAlterarTodasAulas(), getUsuarioLogado(), getRetornarExcecaoAlteracaoComAulaRegistrada(), conf.getControlarNumeroAulaProgramadoProfessorPorDia(), conf.getQuantidadeAulaMaximaProgramarProfessorDia(), getLiberarProgramacaoAulaProfessorAcimaPermitido(), getFuncionarioCargoVO());
			if (validacoes != null && validacoes.containsKey(TipoValidacaoChoqueHorarioEnum.ERRO) && !validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).isEmpty()) {				
				setListaMensagemProgramarAulaPeriodo(validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO));
				realizarSeparacaoChoqueHorarioPorTipo();
				if(validacoes.containsKey(TipoValidacaoChoqueHorarioEnum.SUCESSO)){
					setListaMensagemProgramarAulaSemChoqueHorario(validacoes.get(TipoValidacaoChoqueHorarioEnum.SUCESSO));
				}
				setAlteracaoComAulaRegistrada(true);
				setAcaoModalEnviarEmailAlunosTurmaPadrao("");
				return;
			}
			
			setTitulacaoCursoVO(getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(getHorarioTurmaVO().getTurma().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			PessoaVO objProfessorSubstituto = getHorarioTurmaVO().getProfessorAtual();
			if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
				preencherListaProfessoresParaTitulacaoProfessorCursoPendente(objProfessorSubstituto, null);
			}
			if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
				getFacadeFactory().getItemTitulacaoCursoFacade().calcularQtdeProfessorNivelEscolaridade(getHashMapQtdeNivelEscolaridade(), getHashMapProfessorDisciplina().size(), getTitulacaoCursoVO());
			}
			getListaProfessoresInteressadosDisciplina().clear();
			if(isPermitirProgramacaoAulaComClassroom()) {
				getFacadeFactory().getClassroomGoogleFacade().realizarRevisaoClassroomPorTurma(getHorarioTurmaVO().getTurma(), getUsuarioLogadoClone());	
			}
			if(isPermitirProgramacaoAulaComBlackboard()) {
				getFacadeFactory().getSalaAulaBlackboardFacade().realizarRevisaoSalaAulaBlackboardPorTurma(getHorarioTurmaVO().getTurma(), getUsuarioLogadoClone());	
			}
			if(Uteis.isAtributoPreenchido(getHorarioTurmaVO().getHorarioTurmaDiaItemGoogleMeet())) {
				getFacadeFactory().getGoogleMeetInterfaceFacade().realizarOperacaoGoogleMeetPorListaHorarioTurmaDiaItem(getHorarioTurmaVO().getHorarioTurmaDiaItemGoogleMeet(), false, getUsuarioLogadoClone());				
			}
			
			if (getProfessor() > 0 && getDisciplina() > 0 && !getListaHorarioTurmaDiaVOs().isEmpty()) {				
				montarListaSelectItemProfessorComAulaProgramadaTurma();
				executarMontagemListaAulaProfessor();				
			}else if(Uteis.isAtributoPreenchido(getHorarioTurmaPorDiaVO().getCodigo())){				
				setHorarioTurmaPorDiaVO(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPorChavePrimaria(getHorarioTurmaPorDiaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				getHorarioTurmaPorDiaVO().setFuncionarioCargoVO(getFuncionarioCargoVO());
				executarAtualizacaoHorarioTurmaGoogleMeet(false);
			}			
			if (disciplinaSubstituta != null && !disciplinaSubstituta.getCodigo().equals(0)) {
				verificarDisciplinaJaIncluida(disciplinaSubstituta);
			}
			inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
			setMatriculaProfessor("");
			setMensagemID("msg_dados_gravados");
			setHorarioAlterado(true);			
			setEnviarComunicadoPorEmail(true);
			disciplinaSubstituta = null;	
			setAcaoModalEnviarEmailAlunosTurmaPadrao("RichFaces.$('panelEnviarEmailAlunosTurmaPadrao').show();");
		} catch (Exception e) {
			try{
 				getFacadeFactory().getHorarioTurmaFacade().carregarDadosHorarioTurma(getHorarioTurmaVO(), null, null, getUsuarioLogado());
			} catch (Exception ex) {
			}
			e.printStackTrace();
			setHorarioAlterado(false);	
			setAcaoModalEnviarEmailAlunosTurmaPadrao("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			ControleConcorrencia.liberarHorarioProfessor(horarioTurmaVO);
			try {
				if (bloqueioAdicionado) {
					executarBloqueioTurmaProgramacaoAula(false, false);
				}
			} catch (Exception e) {
			}
		}
	}

	public void realizarGeracaoGoogleMeetTodosDia() {
		try {
			getFacadeFactory().getGoogleMeetInterfaceFacade().realizarGeracaoEventoGoogleMeetIncluirVisaoAdministrativa(getHorarioTurmaVO(), null, getProfessor(), getDisciplina(), false, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());			
			setMensagemID("msg_googleMeet_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	public void realizarGeracaoGoogleMeetDia() {
		try {
			getFacadeFactory().getGoogleMeetInterfaceFacade().realizarGeracaoEventoGoogleMeetIncluirVisaoAdministrativa(getHorarioTurmaVO(), getHorarioTurmaPorDiaVO(), getProfessor(), getDisciplina(), false, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());			
			setMensagemID("msg_googleMeet_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	public void realizarExclusaoGoogleMeetTodosDia() {
		try {
			getFacadeFactory().getGoogleMeetInterfaceFacade().realizarGeracaoEventoGoogleMeetIncluirVisaoAdministrativa(getHorarioTurmaVO(), null, getProfessor(), getDisciplina(), true, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());		
			setMensagemID("msg_googleMeet_excluir");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	public void realizarExclusaoGoogleMeetDia() {
		try {
			getFacadeFactory().getGoogleMeetInterfaceFacade().realizarGeracaoEventoGoogleMeetIncluirVisaoAdministrativa(getHorarioTurmaVO(), getHorarioTurmaPorDiaVO(), getProfessor(), getDisciplina(), true, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			setMensagemID("msg_googleMeet_excluir");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarGeracaoGoogleMeet() {
		try {
			getFacadeFactory().getGoogleMeetInterfaceFacade().realizarOperacaoGoogleMeetVisaoAdministrativa(getHorarioTurmaDiaItemSelecionadoVO(), getHorarioTurmaVO(), getHorarioTurmaPorDiaVO(), false, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			setMensagemID("msg_googleMeet_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void excluirGoogleMeet() {
		try {
			getFacadeFactory().getGoogleMeetInterfaceFacade().realizarOperacaoGoogleMeetVisaoAdministrativa(getHorarioTurmaDiaItemSelecionadoVO(), getHorarioTurmaVO(), getHorarioTurmaPorDiaVO(), true, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogado());
			getHorarioTurmaDiaItemSelecionadoVO().setGoogleMeetVO(new GoogleMeetVO());
			setMensagemID("msg_googleMeet_excluir");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	
	public void editarHorarioTurmaDisciplinaProgramadaVO() {
		try {
			HorarioTurmaDisciplinaProgramadaVO obj = (HorarioTurmaDisciplinaProgramadaVO) context().getExternalContext().getRequestMap().get("horarioTurmaDisciplinaProgramadaItens");
			setHorarioTurmaDisciplinaProgramadaVO(obj);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void realizarGeracaoClassroom() {
		try {
			getHorarioTurmaDisciplinaProgramadaVO().setClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().realizarGeracaoClassroomGoogle(getHorarioTurmaDisciplinaProgramadaVO().getClassroomGoogleVO(), getUsuarioLogadoClone()));
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAtualizacaoClassroom() {
		try {
			getHorarioTurmaDisciplinaProgramadaVO().setClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().realizarRevisaoClassroom(getHorarioTurmaDisciplinaProgramadaVO().getClassroomGoogleVO(), getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_atualizados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarProcessamentoLoteClassroomPorHorarioTurma() {
		try {
			getFacadeFactory().getClassroomGoogleFacade().realizarProcessamentoLoteClassroomPorHorarioTurma(getHorarioTurmaVO(), getUsuarioLogadoClone());
			inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	

	public void excluirClassroom() {
		try {
			getFacadeFactory().getClassroomGoogleFacade().realizarExclusaoClassroomGooglePorHorarioTurmaDisciplinaProgramada(getHorarioTurmaDisciplinaProgramadaVO(), getHorarioTurmaVO(), getHorarioTurmaPorDiaVO(), getUsuarioLogadoClone());
			getHorarioTurmaDisciplinaProgramadaVO().getClassroomGoogleVO().setCodigo(0);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarBuscaAlunoClassroom() {
		try {
			getHorarioTurmaDisciplinaProgramadaVO().getClassroomGoogleVO().setTurmaVO(getHorarioTurmaVO().getTurma());
			getFacadeFactory().getClassroomGoogleFacade().realizarBuscaAlunoClassroom(getHorarioTurmaDisciplinaProgramadaVO().getClassroomGoogleVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarEnvioConviteAlunoClassroom() {
		try {
			PessoaGsuiteVO obj = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("classroomStudentVOItens");
			getFacadeFactory().getClassroomGoogleFacade().realizarEnvioConviteAlunoClassroom(getHorarioTurmaDisciplinaProgramadaVO().getClassroomGoogleVO(), obj, getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarAtualizacaoAlunoClassroom() {
		try {
			inicializarMensagemVazia();
			setMensagemResponseJson(getFacadeFactory().getClassroomGoogleFacade().realizarAtualizacaoAlunoClassroom(getHorarioTurmaDisciplinaProgramadaVO().getClassroomGoogleVO(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarInicializacaoSalaAulaBlackboard() {
		try {
			HorarioTurmaDisciplinaProgramadaVO obj = (HorarioTurmaDisciplinaProgramadaVO) context().getExternalContext().getRequestMap().get("horarioTurmaDisciplinaProgramadaItens");
			setHorarioTurmaDisciplinaProgramadaVO(obj);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void realizarGeracaoSalaAulaBlackboard() {
		try {
			getHorarioTurmaDisciplinaProgramadaVO().setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO(), getUsuarioLogadoClone()));
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarAtualizacaoBlackboard() {
		try {
			getHorarioTurmaDisciplinaProgramadaVO().setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().realizarRevisaoSalaAulaBlackboard(getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO(), getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_atualizados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarProcessamentoLoteBlackboardPorHorarioTurma() {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarProcessamentoLoteSalaAulaBlackboardPorHorarioTurma(getHorarioTurmaVO(), getUsuarioLogadoClone());
			inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	

	public void excluirBlackboard() {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarExclusaoSalaAulaBlackboard(getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO(), getUsuarioLogadoClone());
			getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO().setCodigo(0);
			getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO().setLinkSalaAulaBlackboard("");
			getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO().setId("");
			getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO().setIdSalaAulaBlackboard("");
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarBuscaAlunoBlackboard() {
		try {
			getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO().setTurmaVO(getHorarioTurmaVO().getTurma());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarBuscaAlunoSalaAulaBlackboard(getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarEnvioConviteAlunoBlackboard() {
		try {
			SalaAulaBlackboardPessoaVO obj = (SalaAulaBlackboardPessoaVO) context().getExternalContext().getRequestMap().get("salaAulaBlackboardPessoaItens");
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarEnvioConviteAlunoSalaAulaBlackboard(getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO(), obj, getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarAtualizacaoAlunoBlackboard() {
		try {
			inicializarMensagemVazia();
			setMensagemResponseJson(getFacadeFactory().getSalaAulaBlackboardFacade().realizarAtualizacaoAlunoSalaAulaBlackboard(getHorarioTurmaDisciplinaProgramadaVO().getSalaAulaBlackboardVO(), getUsuarioLogadoClone()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public Boolean verificarProfessorAulaUnica(PessoaVO professor) {
		int i = 0;
		for (HorarioTurmaDiaVO horarioTurmaDiaVO : getHorarioTurmaVO().getHorarioTurmaDiaVOs()) {
			for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
				if (professor.getCodigo().equals(horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo())) {
					i++;
				}
			}
		}
		if (i > 1) {
			return false;
		} else {
			return true;
		}
	}
	public void removerHorarioTurmaDoProfessor () {
		try {
			setOperacaoExcluirHorarioTurmaDoProfessor(true);
			montarListaSelectItemProfessorComAulaProgramadaTurma();	
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}
	
	public void googleMeetHorarioTurmaDoProfessor () {
		try {
			setOperacaoExcluirHorarioTurmaDoProfessor(false);
			montarListaSelectItemProfessorComAulaProgramadaTurma();	
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}

	public void montarListaSelectItemProfessorComAulaProgramadaTurma() {
		setProfessor(0);
		getListaSelectItemDisciplinaProfessor().clear();
		List<PessoaVO> professores = getFacadeFactory().getHorarioTurmaFacade().executarMontagemListaProfessorComAulaProgramadaTurma(horarioTurmaVO);
		setHorarioTurmaPorDiaVO(new HorarioTurmaDiaVO());
		setListaHorarioTurmaDiaVOs(new ArrayList<HorarioTurmaDiaVO>(0));
		setListaSelectItemProfessor(new ArrayList<>(0));
		getListaSelectItemProfessor().add(new SelectItem(0, ""));
		for (PessoaVO pessoaVO : professores) {
			if (pessoaVO.getCodigo().intValue() != 0) {
				getListaSelectItemProfessor().add(new SelectItem(pessoaVO.getCodigo(), pessoaVO.getNome()));
			}
		}
	}

	public void fecharModalExcluirHorarioProfessor() {
		setProfessor(0);
		setDisciplina(0);
		getListaSelectItemDisciplinaProfessor().clear();
		setHorarioTurmaPorDiaVO(new HorarioTurmaDiaVO());
		getListaHorarioTurmaDiaVOs().clear();

	}

	public void montarListaSelectItemDisciplinaProfessorComAulaProgramadaTurma() {
		setListaHorarioTurmaDiaVOs(new ArrayList<HorarioTurmaDiaVO>(0));
		getListaSelectItemDisciplinaProfessor().clear();
		List<DisciplinaVO> disciplinaVOs = getFacadeFactory().getHorarioTurmaFacade().realizarObtencaoDisciplinaLecionadaProfessor(horarioTurmaVO, getProfessor());
		for (DisciplinaVO disciplinaVO : disciplinaVOs) {
			getListaSelectItemDisciplinaProfessor().add(new SelectItem(disciplinaVO.getCodigo(), disciplinaVO.getNome()));
		}
		if (!disciplinaVOs.isEmpty()) {
			setDisciplina(disciplinaVOs.get(0).getCodigo());
			executarMontagemListaAulaProfessor();
		} else {
			setHorarioTurmaPorDiaVO(new HorarioTurmaDiaVO());
			setDisciplina(0);
		}
	}

	public void executarMontagemListaAulaProfessor() {
		try {
			Date dataBase = getHorarioTurmaPorDiaVO().getData();
			setHorarioTurmaPorDiaVO(new HorarioTurmaDiaVO());
			if (getProfessor() != null && getProfessor() > 0 && getDisciplina() != null && getDisciplina() > 0) {
				setListaHorarioTurmaDiaVOs(getFacadeFactory().getHorarioTurmaFacade().inicializarDadosHorarioTurmaDiaVOPorProfessor(getProfessor(), getDisciplina(), getHorarioTurmaVO()));
				for (HorarioTurmaDiaVO horarioTurmaDiaVO : getListaHorarioTurmaDiaVOs()) {
					if (horarioTurmaDiaVO.getData_Apresentar().equals(Uteis.getData(dataBase))) {
						setHorarioTurmaPorDiaVO(horarioTurmaDiaVO);
						break;
					}
				}
			} else {
				setListaHorarioTurmaDiaVOs(new ArrayList<HorarioTurmaDiaVO>(0));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<HorarioTurmaDiaVO> getListaHorarioTurmaDiaVOs() {
		if (listaHorarioTurmaDiaVOs == null) {
			listaHorarioTurmaDiaVOs = new ArrayList<HorarioTurmaDiaVO>(0);
		}
		return listaHorarioTurmaDiaVOs;
	}

	public void setListaHorarioTurmaDiaVOs(List<HorarioTurmaDiaVO> listaHorarioTurmaDiaVOs) {
		this.listaHorarioTurmaDiaVOs = listaHorarioTurmaDiaVOs;
	}

	public boolean getIsApresentarDia() {
		if (getHorarioTurmaPorDiaVO().getData_Apresentar().equals("") || getHorarioTurmaPorDiaVO().getHorarioTurmaDiaItemVOs().isEmpty()) {
			return false;
		}
		return true;
	}

	public Integer getProfessor() {
		if (professor == null) {
			professor = 0;
		}
		return professor;
	}

	public void setProfessor(Integer professor) {
		this.professor = professor;
	}

	public List<SelectItem> getListaSelectItemProfessor() {
		if (listaSelectItemProfessor == null) {
			listaSelectItemProfessor = new ArrayList<>(0);
		}
		return listaSelectItemProfessor;
	}

	public void setListaSelectItemProfessor(List<SelectItem> listaSelectItemProfessor) {
		this.listaSelectItemProfessor = listaSelectItemProfessor;
	}

	/**
	 * Operação responsável por processar a consulta pelo parâmetro informado
	 * pelo usuário.<code>DisciplinaVO</code> Após a consulta ela
	 * automaticamente adciona o código e o nome da cidade na tela.
	 */
	public List consultarDisciplinaSuggestionbox(Object event) {
		try {
			String valor = event.toString();
			List lista = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinasTurma(valor, getHorarioTurmaVO().getTurma(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return lista;
		} catch (Exception e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Turma</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarTurmaPorChavePrimaria() {
		try {
			setHorarioTurmaPorDiaVO(new HorarioTurmaDiaVO());
			String campoConsulta = getHorarioTurmaVO().getTurma().getIdentificadorTurma();
			if (campoConsulta != null && !campoConsulta.trim().equals("")) {
				getHorarioTurmaVO().getCalendarioHorarioAulaVOs().clear();
				getHorarioTurmaVO().getHorarioTurmaDiaVOs().clear();			
				getHashMapProfessorDisciplina().clear();
				getFacadeFactory().getHorarioTurmaFacade().inicializarDadosHorarioTurmaPorTurma(getHorarioTurmaVO(), true, getUnidadeEnsinoLogado(), getUsuarioLogado());
				inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
				montarListaSelectItemDisciplina();
				setTitulacaoCursoVO(getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(getHorarioTurmaVO().getTurma().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
					preencherListaProfessoresParaTitulacaoProfessorCursoPendente(null, null);
					getFacadeFactory().getItemTitulacaoCursoFacade().calcularQtdeProfessorNivelEscolaridade(getHashMapQtdeNivelEscolaridade(), getHashMapProfessorDisciplina().size(), getTitulacaoCursoVO());
				}
			} else {
				setHorarioTurmaVO(new HorarioTurmaVO());
			}
			montarListaSelectItemLocal();
			this.setTurma_Erro("");
			setMensagemID("msg_dados_consultados");
			setHashDisciplinasAlteradas(null);
			setListaDisciplinasAlteradasCodigo(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Turma</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarTurmaPorChavePrimariaAnoSemestre() {
		consultarTurmaPorChavePrimaria();
		executarValidarProcessoMatricula();
		setIndex(0);
		limparDadosDisciplina();
	}

	public void executarValidarProcessoMatricula() {
		try {
			if (((getIsApresentarAnoSemestre() && getHorarioTurmaVO().getSemestreVigente().trim().isEmpty()) || getIsApresentarSomenteAno()) && !getHorarioTurmaVO().getAnoVigente().trim().isEmpty() && getHorarioTurmaVO().getAnoVigente().trim().length() != 4) {
				PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCurso = getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorUnidadeEnsinoTurnoCursoAnoSemestre(getHorarioTurmaVO().getAnoVigente(), getHorarioTurmaVO().getSemestreVigente(), getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo(), getHorarioTurmaVO().getTurno().getCodigo(), getHorarioTurmaVO().getTurma().getCurso().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (periodoLetivoAtivoUnidadeEnsinoCurso == null || periodoLetivoAtivoUnidadeEnsinoCurso.getCodigo().intValue() == 0) {
					throw new Exception("msg_erro_naoEncontradoProcessoMatricula");
				}
			}
		} catch (Exception e) {
			setMensagemID(e.getMessage());
			getHorarioTurmaVO().setAnoVigente("");
			getHorarioTurmaVO().setSemestreVigente("");
			this.setTurma_Erro(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemTipoHorarioTurma() {
		return TipoHorarioTurma.getComboDiaSemana();
	}

	public void selecionarHorarioTurmaDia() {
		try {
			setHorarioTurmaPorDiaVO(new HorarioTurmaDiaVO());
			setHorarioTurmaPorDiaVO((HorarioTurmaDiaVO) context().getExternalContext().getRequestMap().get("horarioProgramacaoDiaItens"));
			getHorarioTurmaVO().setDia(getHorarioTurmaPorDiaVO().getData());
			getFacadeFactory().getHorarioTurmaDiaFacade().montarDadosHorarioTurmaDiaItemVOs(getHorarioTurmaVO(), getHorarioTurmaPorDiaVO());
			setAlterarTodasAulas(false);
			setRetornarExcecaoAlteracaoComAulaRegistrada(true);
			setNumeroAula(0);
			setAlteracaoComAulaRegistrada(false);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public Boolean verificarPermissaoUsuarioExcluirHorarioTurmaDia() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteAlterarExcluirAulasCadastradasApenasPeloProprioUsuario", getUsuarioLogado());
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;			
		}
	}

	public Boolean verificarPermissaoUsuarioIncluirFuncionarioCargo() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteInformarFuncionarioCargo", getUsuarioLogado());
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;			
		}
	}
	
	public void excluirHorarioTurmaDiaItem() throws Exception {
		boolean bloqueioAdicionado = false;
		try {
			executarBloqueioTurmaProgramacaoAula(true, true);
			bloqueioAdicionado = true;
			setErroExclusao(Boolean.FALSE);
//			if (verificarPermissaoUsuarioExcluirHorarioTurmaDia()) {
//				if (getHorarioTurmaPorDiaVO().getUsuarioResp().getCodigo().intValue() != getUsuarioLogado().getCodigo().intValue()) {
//					setErroExclusao(Boolean.TRUE);
//					throw new Exception("Usuário não pode realizar exclusão da aula selecionada! É somente permitido realizar exclusão de aulas pelo usuário responsável pelo cadastro! ");
//				}
//			}
			setHorarioAlterado(false);
			setAlteracaoComAulaRegistrada(false);
			getHorarioTurmaVO().getHorarioTurmaDiaItemGoogleMeet().clear();
			Map<TipoValidacaoChoqueHorarioEnum, List<ChoqueHorarioVO>> validacoes = getFacadeFactory().getHorarioTurmaFacade().realizarExclusaoHorarioTurmaPorProfessorDisciplina(getHorarioTurmaVO(), getHorarioTurmaPorDiaVO(), getProfessor(), getDisciplina(), getNumeroAula(), getAlterarTodasAulas(), getUsuarioLogado(), getRetornarExcecaoAlteracaoComAulaRegistrada());
			if (validacoes.containsKey(TipoValidacaoChoqueHorarioEnum.ERRO) && !validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO).isEmpty()) {
				setListaMensagemProgramarAulaPeriodo(validacoes.get(TipoValidacaoChoqueHorarioEnum.ERRO));
				setListaMensagemProgramarAulaSemChoqueHorario(validacoes.get(TipoValidacaoChoqueHorarioEnum.SUCESSO));
				setAlteracaoComAulaRegistrada(true);
				return;
			}
			if (getProfessor() > 0 && getDisciplina() > 0 && !getListaHorarioTurmaDiaVOs().isEmpty()) {
				executarMontagemListaAulaProfessor();
			}
			setTitulacaoCursoVO(getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(getHorarioTurmaVO().getTurma().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
				preencherListaProfessoresParaTitulacaoProfessorCursoPendente(null, null);
				getFacadeFactory().getItemTitulacaoCursoFacade().calcularQtdeProfessorNivelEscolaridade(getHashMapQtdeNivelEscolaridade(), getHashMapProfessorDisciplina().size(), getTitulacaoCursoVO());
			}
			if(isPermitirProgramacaoAulaComClassroom()) {
				DisciplinaVO disciplina = new DisciplinaVO();
				disciplina.setCodigo(getDisciplina());
				getFacadeFactory().getClassroomGoogleFacade().realizarRevisaoClassroomPorTurmaPorDisciplinaPorAnoPorSemestrePorProfessor(getHorarioTurmaVO().getTurma(), disciplina, getHorarioTurmaVO().getAnoVigente(), getHorarioTurmaVO().getSemestreVigente(), null, getUsuarioLogadoClone());
			}
			inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
			if(!getHorarioTurmaVO().getHorarioTurmaDiaItemGoogleMeet().isEmpty()) {
				getHorarioTurmaVO().getHorarioTurmaDiaItemGoogleMeet().stream().forEach(p->p.setCodigo(0));	//se fazer necessario zerar o codigo do HTDI pois os mesmo ja nao existe mais na base de dados pq foi excluido nas rotinas a cima			
				getFacadeFactory().getGoogleMeetInterfaceFacade().realizarOperacaoGoogleMeetPorListaHorarioTurmaDiaItem(getHorarioTurmaVO().getHorarioTurmaDiaItemGoogleMeet(), true, getUsuarioLogadoClone());	
			}
			if(Uteis.isAtributoPreenchido(getHorarioTurmaPorDiaVO().getCodigo())){
				setHorarioTurmaPorDiaVO(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPorChavePrimaria(getHorarioTurmaPorDiaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				executarAtualizacaoHorarioTurmaGoogleMeet(true);
				if(Uteis.isAtributoPreenchido(getHorarioTurmaPorDiaVO().getCodigo()) && getHorarioTurmaPorDiaVO().getIsAulaProgramada()){
					getFacadeFactory().getHorarioTurmaDiaFacade().montarDadosHorarioTurmaDiaItemVOs(horarioTurmaVO, getHorarioTurmaPorDiaVO());
				}else{
					setHorarioTurmaPorDiaVO(null);
				}
			}
			setMensagemID("msg_dados_excluidos");
			setHorarioAlterado(true);
			setEnviarComunicadoPorEmail(false);
			//TODO verificar pedro
//			setAlterarTodasAulas(false);
//			setRetornarExcecaoAlteracaoComAulaRegistrada(true);
//			setNumeroAula(0);
//			setAlteracaoComAulaRegistrada(false);
		} catch (Exception e) {
			setHorarioAlterado(false);
			setErroExclusao(Boolean.TRUE);
			// getHorarioTurmaVO().adicinarHorarioTurmaPorDiaPorDia(getHorarioTurmaPorDiaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			ControleConcorrencia.liberarHorarioProfessor(horarioTurmaVO);
			try {
				if (bloqueioAdicionado) {
					executarBloqueioTurmaProgramacaoAula(false, false);
				}
			} catch (Exception e) {
			}
		}
	}

	private void executarAtualizacaoHorarioTurmaGoogleMeet(boolean isExclusao) {
		for (HorarioTurmaDiaVO htd : getHorarioTurmaVO().getHorarioTurmaDiaVOs()) {
			if(htd.getCodigo().equals(getHorarioTurmaPorDiaVO().getCodigo())) {
				for (HorarioTurmaDiaItemVO htdiAtualizado : getHorarioTurmaPorDiaVO().getHorarioTurmaDiaItemVOs()) {
					htd.getHorarioTurmaDiaItemVOs().stream().filter(p-> p.getNrAula().equals(htdiAtualizado.getNrAula())).forEach(p->{
							p.setGoogleMeetVO(htdiAtualizado.getGoogleMeetVO());
							p.setGerarEventoAulaOnLineGoogleMeet(false);
					});	
				}
				if(isExclusao) {
					for (HorarioTurmaDiaItemVO htdiAtualizado : getHorarioTurmaVO().getHorarioTurmaDiaItemGoogleMeet()) {
						htd.getHorarioTurmaDiaItemVOs().stream().filter(p-> p.getNrAula().equals(htdiAtualizado.getNrAula())).forEach(p->{
								p.setGoogleMeetVO(new GoogleMeetVO());
								p.setGerarEventoAulaOnLineGoogleMeet(false);
						});	
					}
				}
			}
		}
	}

	

	// public void excluirEdicaoHorario() {
	// try {
	// setHorarioAlterado(false);
	// getFacadeFactory().getHorarioTurmaFacade().realizarExclusaoHorarioTurmaPorProfessorDisciplina(getHorarioTurmaVO(),
	// getHorarioTurmaPorDiaVO(),
	// getHorarioTurmaVO().getProfessorAtual().getCodigo(),
	// getHorarioTurmaVO().getDisciplinaAtual().getCodigo(), getNumeroAula(),
	// false, getUsuarioLogado());
	// verificarDisciplinaJaIncluida(getHorarioTurmaVO().getDisciplinaAtual());
	// setTitulacaoCursoVO(getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(getHorarioTurmaVO().getTurma().getCurso().getCodigo(),
	// false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,
	// getUsuarioLogado()));
	// if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
	// preencherListaProfessoresParaTitulacaoProfessorCursoPendente(null, null);
	// getFacadeFactory().getItemTitulacaoCursoFacade().calcularQtdeProfessorNivelEscolaridade(getHashMapQtdeNivelEscolaridade(),
	// getHashMapProfessorDisciplina().size(), getTitulacaoCursoVO());
	// }
	// inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
	// setMensagemID("msg_dados_excluidos");
	// setHorarioAlterado(true);
	// setEnviarComunicadoPorEmail(true);
	// } catch (Exception e) {
	// setHorarioAlterado(false);
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }
	//
	// public void removerTodosHorarioProfessor() {
	// try {
	// setAlterarTodasAulas(false);
	// setHorarioTurmaPorDiaVO(new HorarioTurmaDiaVO());
	// setRetornarExcecaoAlteracaoComAulaRegistrada(true);
	// setNumeroAula(0);
	// getFacadeFactory().getHorarioTurmaFacade().realizarExclusaoHorarioTurmaPorProfessorDisciplina(getHorarioTurmaVO(),
	// getHorarioTurmaPorDiaVO(), getProfessor(), 0, getNumeroAula(),
	// getAlterarTodasAulas(), getUsuarioLogado(),
	// getRetornarExcecaoAlteracaoComAulaRegistrada());
	// setTitulacaoCursoVO(getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(getHorarioTurmaVO().getTurma().getCurso().getCodigo(),
	// false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,
	// getUsuarioLogado()));
	// if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
	// preencherListaProfessoresParaTitulacaoProfessorCursoPendente(null, null);
	// getFacadeFactory().getItemTitulacaoCursoFacade().calcularQtdeProfessorNivelEscolaridade(getHashMapQtdeNivelEscolaridade(),
	// getHashMapProfessorDisciplina().size(), getTitulacaoCursoVO());
	// }
	// setListaHorarioTurmaDiaVOs(new ArrayList<HorarioTurmaDiaVO>(0));
	// montarListaSelectItemProfessorComAulaProgramadaTurma();
	// inicializarListaHorarioTurmaDisciplinaProgramada(getHorarioTurmaVO());
	// setMensagemID("msg_dados_excluidos");
	// setHorarioAlterado(true);
	// setEnviarComunicadoPorEmail(true);
	// } catch (Exception e) {
	// setHorarioAlterado(false);
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }

	public void imprimirHorario() {
		try {
			HttpServletRequest request = this.request();
			request.getSession().setAttribute("nomeRelatorio", "Horario Turma");
			request.getSession().setAttribute("nomeEmpresa", getHorarioTurmaVO().getTurma().getUnidadeEnsino().getNome());
			request.getSession().setAttribute("usuario", getUsuarioLogado().getNome());
			request.getSession().setAttribute("nomeDesignIReport", "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "HorarioAula_Turma.jrxml");
			request.getSession().setAttribute("caminhoBaseAplicacao", "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
			
            UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
			if (ue.getExisteLogoRelatorio()) {
				String urlLogoUnidadeEnsinoRelatorio = ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoRelatorio();
				String urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
				request.getSession().setAttribute("logoPadraoRelatorio", urlLogo);
			} else {
				request.getSession().setAttribute("logoPadraoRelatorio", "");
			}

			List<HorarioTurmaVO> lista = new ArrayList<HorarioTurmaVO>(0);
			lista.add(getHorarioTurmaVO());
			request.getSession().setAttribute("listaObjetos", lista);
			setImprimirHorarioTurma(true);
		} catch (Exception e) {
			setImprimirHorarioTurma(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
			//System.out.print("ERRO THYAGO => " + e.getMessage());
		}
	}
	
	public void limparDadosDisciplina() {
		getHorarioTurmaVO().setDisciplina(new DisciplinaVO());
	}

	public void limparDadosProfessor() {
		getHorarioTurmaVO().setProfessor(new PessoaVO());
		setMatriculaProfessor("");
		setFuncionarioCargoVO(new FuncionarioCargoVO());
		setListaSelectItemFuncionarioCargo(new ArrayList<>(0));
	}

	public void executarValidarAulaProgramadaParaDisciplina() {
		try {
			limparMensagem();
			if(Uteis.isAtributoPreenchido(getHorarioTurmaVO().getDisciplina().getCodigo())){
				
				getFacadeFactory().getHorarioTurmaFacade().realizarVerificacaoExisteAulaProgramadaParaDisciplinaEEquivalenteParaOutraTurmaOuTurmaAgrupada(getHorarioTurmaVO().getTurma(), getHorarioTurmaVO().getDisciplina().getCodigo(), getHorarioTurmaVO().getAnoVigente(), getHorarioTurmaVO().getSemestreVigente(), true);			
			}
		} catch (Exception e) {
//			limparDadosProfessor();
//			setHorarioTurmaVO(new HorarioTurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getAbrirPopupRelatorioImprimirHorario() {
		if (getImprimirHorarioTurma()) {
			return "abrirPopup('../../VisualizadorHorarioTurma', 'ImprimirHorario', 730, 545)";
		}
		return "";
	}

	public Boolean getImprimirHorarioTurma() {
		if (imprimirHorarioTurma == null) {
			imprimirHorarioTurma = false;
		}
		return imprimirHorarioTurma;
	}

	public void setImprimirHorarioTurma(Boolean imprimirHorarioTurma) {
		this.imprimirHorarioTurma = imprimirHorarioTurma;
	}

	/**
	 * Operação responsável por processar a consulta pelo parâmetro informado
	 * pelo usuário.<code>TurmaVO</code> Após a consulta ela automaticamente
	 * adciona o código e o nome da cidade na tela.
	 */
	public List consultarTurmaSuggestionbox(Object event) {
		try {
			String valor = event.toString();
			return getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(valor, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			return new ArrayList<>(0);
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurmaTurma", "Turma"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoAulaCons.xhtml");
	}

	public DisponibilidadeHorarioTurmaProfessorVO getDisponibilidadeSubstituta() {
		if (disponibilidadeSubstituta == null) {
			disponibilidadeSubstituta = new DisponibilidadeHorarioTurmaProfessorVO();
		}
		return disponibilidadeSubstituta;
	}

	public void setDisponibilidadeSubstituta(DisponibilidadeHorarioTurmaProfessorVO disponibilidadeSubstituta) {
		this.disponibilidadeSubstituta = disponibilidadeSubstituta;
	}

	public HorarioTurmaVO getHorarioTurmaVO() {
		if (horarioTurmaVO == null) {
			horarioTurmaVO = new HorarioTurmaVO();
		}
		return horarioTurmaVO;
	}

	public void setHorarioTurmaVO(HorarioTurmaVO horarioTurmaVO) {
		this.horarioTurmaVO = horarioTurmaVO;
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public String getProfessor_Erro() {
		if (professor_Erro == null) {
			professor_Erro = "";
		}
		return professor_Erro;
	}

	public void setProfessor_Erro(String professor_Erro) {
		this.professor_Erro = professor_Erro;
	}

	public String getDisciplina_Erro() {
		if (disciplina_Erro == null) {
			disciplina_Erro = "";
		}
		return disciplina_Erro;
	}

	public void setDisciplina_Erro(String disciplina_Erro) {
		this.disciplina_Erro = disciplina_Erro;
	}

	public String getTurma_Erro() {
		if (turma_Erro == null) {
			turma_Erro = "";
		}
		return turma_Erro;
	}

	public void setTurma_Erro(String turma_Erro) {
		this.turma_Erro = turma_Erro;
	}

	public List getListaProfessoresInteressadosDisciplina() {
		if (listaProfessoresInteressadosDisciplina == null) {
			listaProfessoresInteressadosDisciplina = new ArrayList<>(0);
		}
		return listaProfessoresInteressadosDisciplina;
	}

	public void setListaProfessoresInteressadosDisciplina(List listaProfessoresInteressadosDisciplina) {
		this.listaProfessoresInteressadosDisciplina = listaProfessoresInteressadosDisciplina;
	}

	public HorarioProfessorVO getHorarioProfessorVO() {
		if (horarioProfessorVO == null) {
			horarioProfessorVO = new HorarioProfessorVO();
		}
		return horarioProfessorVO;
	}

	public void setHorarioProfessorVO(HorarioProfessorVO horarioProfessorVO) {
		this.horarioProfessorVO = horarioProfessorVO;
	}

	public String getMatriculaProfessor() {
		if (matriculaProfessor == null) {
			matriculaProfessor = "";
		}
		return matriculaProfessor;
	}

	public void setMatriculaProfessor(String matriculaProfessor) {
		this.matriculaProfessor = matriculaProfessor;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		turma_Erro = null;
		disciplina_Erro = null;
		professor_Erro = null;
		Uteis.liberarListaMemoria(listaProfessoresInteressadosDisciplina);

	}

	/**
	 * @return the campoConsultaProfessor
	 */
	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	/**
	 * @param campoConsultaProfessor
	 *            the campoConsultaProfessor to set
	 */
	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	/**
	 * @return the valorConsultaProfessor
	 */
	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	/**
	 * @param valorConsultaProfessor
	 *            the valorConsultaProfessor to set
	 */
	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	/**
	 * @param listaConsultaProfessor
	 *            the listaConsultaProfessor to set
	 */
	public void setListaConsultaProfessor(List listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	/**
	 * @return the listaConsultaProfessor
	 */
	public List getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<>(0);
		}
		return listaConsultaProfessor;
	}

	public HorarioTurmaDiaVO getHorarioTurmaPorDiaVO() {
		if (horarioTurmaPorDiaVO == null) {
			horarioTurmaPorDiaVO = new HorarioTurmaDiaVO();
		}
		return horarioTurmaPorDiaVO;
	}

	public void setHorarioTurmaPorDiaVO(HorarioTurmaDiaVO horarioTurmaPorDiaVO) {
		this.horarioTurmaPorDiaVO = horarioTurmaPorDiaVO;
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

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public Boolean getProgramarAulaPeriodo() {
		if (programarAulaPeriodo == null) {
			programarAulaPeriodo = Boolean.FALSE;
		}
		return programarAulaPeriodo;
	}

	public void setProgramarAulaPeriodo(Boolean programarAulaPeriodo) {
		this.programarAulaPeriodo = programarAulaPeriodo;
	}

	public List<ChoqueHorarioVO> getListaMensagemProgramarAulaPeriodo() {
		if (listaMensagemProgramarAulaPeriodo == null) {
			listaMensagemProgramarAulaPeriodo = new ArrayList<ChoqueHorarioVO>(0);
		}
		return listaMensagemProgramarAulaPeriodo;
	}

	public void setListaMensagemProgramarAulaPeriodo(List<ChoqueHorarioVO> listaMensagemProgramarAulaPeriodo) {
		this.listaMensagemProgramarAulaPeriodo = listaMensagemProgramarAulaPeriodo;
	}

	public Boolean getConsultarPorProfessorSubstituto() {
		if (consultarPorProfessorSubstituto == null) {
			consultarPorProfessorSubstituto = false;
		}
		return consultarPorProfessorSubstituto;
	}

	public void setConsultarPorProfessorSubstituto(Boolean consultarPorProfessorSubstituto) {
		this.consultarPorProfessorSubstituto = consultarPorProfessorSubstituto;
	}

	public Integer getNumeroAula() {
		if (numeroAula == null) {
			numeroAula = 0;
		}
		return numeroAula;
	}

	public void setNumeroAula(Integer numeroAula) {
		this.numeroAula = numeroAula;
	}

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Boolean getApresentarCampoCpf() {
		if (getCampoConsultaProfessor().equals("cpf") || getCampoConsultaFuncionario().equals("cpf")) {
			return true;
		}
		return false;
	}

	public String getAnoVigente() {
		if (anoVigente == null) {
			anoVigente = "";
		}
		return anoVigente;
	}

	public void setAnoVigente(String anoVigente) {
		this.anoVigente = anoVigente;
	}

	public String getSemestreVigente() {
		if (semestreVigente == null) {
			semestreVigente = "";
		}
		return semestreVigente;
	}

	public void setSemestreVigente(String semestreVigente) {
		this.semestreVigente = semestreVigente;
	}

	public Boolean getHorarioAlterado() {
		if (horarioAlterado == null) {
			horarioAlterado = false;
		}
		return horarioAlterado;
	}

	public void setHorarioAlterado(Boolean horarioAlterado) {
		this.horarioAlterado = horarioAlterado;
	}

	// public ComunicadoInternoDestinatarioVO
	// getComunicadoInternoDestinatarioVO() {
	// if (comunicadoInternoDestinatarioVO == null) {
	// comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
	// }
	// return comunicadoInternoDestinatarioVO;
	// }
	//
	// public void
	// setComunicadoInternoDestinatarioVO(ComunicadoInternoDestinatarioVO
	// comunicadoInternoDestinatarioVO) {
	// this.comunicadoInternoDestinatarioVO = comunicadoInternoDestinatarioVO;
	// }
	//
	// public ComunicacaoInternaVO getComunicacaoInternaVO() {
	// if (comunicacaoInternaVO == null) {
	// comunicacaoInternaVO = new ComunicacaoInternaVO();
	// }
	// return comunicacaoInternaVO;
	// }
	//
	// public void setComunicacaoInternaVO(ComunicacaoInternaVO
	// comunicacaoInternaVO) {
	// this.comunicacaoInternaVO = comunicacaoInternaVO;
	// }

	public Boolean getEnviarComunicadoPorEmail() {
		if (enviarComunicadoPorEmail == null) {
			enviarComunicadoPorEmail = true;
		}
		return enviarComunicadoPorEmail;
	}

	public void setEnviarComunicadoPorEmail(Boolean enviarComunicadoPorEmail) {
		this.enviarComunicadoPorEmail = enviarComunicadoPorEmail;
	}
	
	

	// public String getMensagemPadraoAlteracaoHorario() {
	// if (mensagemPadraoAlteracaoHorario == null) {
	// mensagemPadraoAlteracaoHorario = "";
	// }
	// return mensagemPadraoAlteracaoHorario;
	// }
	//
	// public void setMensagemPadraoAlteracaoHorario(String
	// mensagemPadraoAlteracaoHorario) {
	// this.mensagemPadraoAlteracaoHorario = mensagemPadraoAlteracaoHorario;
	// }
	//
	// public String getMensagemPadraoAlteracaoHorarioInclusao() {
	// if (mensagemPadraoAlteracaoHorarioInclusao == null) {
	// mensagemPadraoAlteracaoHorarioInclusao = "";
	// }
	// return mensagemPadraoAlteracaoHorarioInclusao;
	// }

	// public void setMensagemPadraoAlteracaoHorarioInclusao(String
	// mensagemPadraoAlteracaoHorarioInclusao) {
	// this.mensagemPadraoAlteracaoHorarioInclusao =
	// mensagemPadraoAlteracaoHorarioInclusao;
	// }

	public Boolean getEnviarTurmaBase() {
		if (enviarTurmaBase == null) {
			enviarTurmaBase = true;
		}
		return enviarTurmaBase;
	}

	public void setEnviarTurmaBase(Boolean enviarTurmaBase) {
		this.enviarTurmaBase = enviarTurmaBase;
	}

	public Boolean getEnviarTurmaReposicao() {
		if (enviarTurmaReposicao == null) {
			enviarTurmaReposicao = true;
		}
		return enviarTurmaReposicao;
	}

	public void setEnviarTurmaReposicao(Boolean enviarTurmaReposicao) {
		this.enviarTurmaReposicao = enviarTurmaReposicao;
	}

	public List<Integer> getListaDisciplinasAlteradasCodigo() {
		if (listaDisciplinasAlteradasCodigo == null) {
			listaDisciplinasAlteradasCodigo = new ArrayList<Integer>(0);
		}
		return listaDisciplinasAlteradasCodigo;
	}

	public void setListaDisciplinasAlteradasCodigo(List<Integer> listaDisciplinasAlteradasCodigo) {
		this.listaDisciplinasAlteradasCodigo = listaDisciplinasAlteradasCodigo;
	}

	public Map<Integer, String> getHashDisciplinasAlteradas() {
		if (hashDisciplinasAlteradas == null) {
			hashDisciplinasAlteradas = new HashMap<Integer, String>(0);
		}
		return hashDisciplinasAlteradas;
	}

	public void setHashDisciplinasAlteradas(Map<Integer, String> hashDisciplinasAlteradas) {
		this.hashDisciplinasAlteradas = hashDisciplinasAlteradas;
	}

	public String getExcluiuEdicaoHorario() {		
		if (getErroExclusao()) {
			return "RichFaces.$('panelExcluir').hide(); RichFaces.$('panelRemoverHorarioProfessor').hide(); RichFaces.$('panelEditarHorario').hide(); RichFaces.$('panelEnviarEmailAlunosTurmaPadrao').hide(); RichFaces.$('panelAulaRegistrada').hide();";
		}
		if (getAlteracaoComAulaRegistrada()) {
			return "RichFaces.$('panelExcluir').hide(); RichFaces.$('panelEditarHorario').hide(); RichFaces.$('panelAulaRegistrada').show();";
		}
		if (getHorarioAlterado() && !getHorarioTurmaVO().getCodigo().equals(0)) {
			// setMsgEnviarTurmaPadrao(msgTurmaPadrao.replace("#TURMA",
			// getHorarioTurmaVO().getTurma().getIdentificadorTurma()));
			// setMensagemPadraoAlteracaoHorario(getMensagemPadraoAlteracaoHorario().replace("#TURMA",
			// getHorarioTurmaVO().getTurma().getIdentificadorTurma()));
			// setMsgEnviarTurmaInclusao(msgTurmaInclusao);
			// setMensagemPadraoAlteracaoHorarioInclusao(msgTurmaInclusao);
			if (getMsgNotificacaoAlunoProgramacaoAula().isNovoObj() || getMsgNotificacaoAlunoProgramacaoAula().getDesabilitarEnvioMensagemAutomatica()) {
				return "RichFaces.$('panelExcluir').hide(); RichFaces.$('panelAulaRegistrada').hide(); RichFaces.$('panelEnviarEmailAlunosTurmaPadrao').show()";
			}
			return "RichFaces.$('panelExcluir').hide(); RichFaces.$('panelAulaRegistrada').hide(); RichFaces.$('panelEditarHorario').hide(); RichFaces.$('panelEnviarEmailAlunosTurmaPadrao').show()";
		}
		return "RichFaces.$('panelExcluir').hide(); RichFaces.$('panelAulaRegistrada').hide(); RichFaces.$('panelEditarHorario').hide()";
	}

	public String getExcluiuHorarioProfessor() {
		if (getHorarioAlterado() && !getHorarioTurmaVO().getCodigo().equals(0)) {
			// setMsgEnviarTurmaPadrao(msgTurmaPadrao.replace("#TURMA",
			// getHorarioTurmaVO().getTurma().getIdentificadorTurma()));
			// setMensagemPadraoAlteracaoHorario(getMensagemPadraoAlteracaoHorario().replace("#TURMA",
			// getHorarioTurmaVO().getTurma().getIdentificadorTurma()));
			// setMsgEnviarTurmaInclusao(msgTurmaInclusao);
			// setMensagemPadraoAlteracaoHorarioInclusao(msgTurmaInclusao);
			if (getMsgNotificacaoAlunoProgramacaoAula().isNovoObj() || getMsgNotificacaoAlunoProgramacaoAula().getDesabilitarEnvioMensagemAutomatica()) {
				return "RichFaces.$('panelExcluirDiaTudo').hide(); RichFaces.$('panelRemoverHorarioProfessor').hide();";
			}
			return "RichFaces.$('panelExcluirDiaTudo').hide(); RichFaces.$('panelRemoverHorarioProfessor').hide();";
		}
		return "RichFaces.$('panelExcluirDiaTudo').hide(); RichFaces.$('panelRemoverHorarioProfessor').hide()";
	}

	public String getExcluiuHorario() {
		try {
			if (getAlteracaoComAulaRegistrada()) {
				return "RichFaces.$('panelGravar').hide(); RichFaces.$('panelAulaRegistradaSubstituicao').show();";
			}			
			
			return "RichFaces.$('panelGravar').hide(); RichFaces.$('panelAulaRegistradaSubstituicao').hide(); RichFaces.$('panelAulaRegistrada').hide(); RichFaces.$('panelEditarHorario').hide();";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "RichFaces.$('panelGravar').hide(); RichFaces.$('panelGravar').hide();";
		}
	}

	public String getExcluiuHorarioTurmaDisciplinaDia() {
		if (getHorarioAlterado() && !getHorarioTurmaVO().getCodigo().equals(0)) {
			if (getMsgNotificacaoAlunoProgramacaoAula().isNovoObj() || getMsgNotificacaoAlunoProgramacaoAula().getDesabilitarEnvioMensagemAutomatica()) {
				return "RichFaces.$('panelExcluirDia').hide();";
			}
			return "RichFaces.$('panelExcluirDia').hide();";
		}
		return "RichFaces.$('panelExcluirDia').hide();";
	}

	public String getAlterouHorario() {
		if (getPanelGravarTitulacao()) {
			return "RichFaces.$('panelGravarTitulacao').show()";
		} else {
			if (getHorarioAlterado() && !getHorarioTurmaVO().getCodigo().equals(0)) {
				// setMsgEnviarTurmaPadrao(msgTurmaPadrao.replace("#TURMA",
				// getHorarioTurmaVO().getTurma().getIdentificadorTurma()));
				// setMensagemPadraoAlteracaoHorario(getMensagemPadraoAlteracaoHorario().replace("#TURMA",
				// getHorarioTurmaVO().getTurma().getIdentificadorTurma()));
				// setMsgEnviarTurmaInclusao(msgTurmaInclusao);
				// setMensagemPadraoAlteracaoHorarioInclusao(msgTurmaInclusao);
				if (getMsgNotificacaoAlunoProgramacaoAula().isNovoObj() || getMsgNotificacaoAlunoProgramacaoAula().getDesabilitarEnvioMensagemAutomatica()) {
					return "";
				}
				return "RichFaces.$('panelEnviarEmailAlunosTurmaPadrao').show()";
			}
			return "";
		}
	}

	/*public String getModalAbrirGravacao() {
		if (getMsgNotificacaoAlunoReposicaoAulaDisponivel().isNovoObj() || getMsgNotificacaoAlunoReposicaoAulaDisponivel().getDesabilitarEnvioMensagemAutomatica() || (!getHorarioTurmaVO().getTurma().getCurso().getNivelEducacional().equals("PO") && !getHorarioTurmaVO().getTurma().getCurso().getNivelEducacional().equals("EX"))) {
			return getAlterouHorario();
		}
		return "RichFaces.$('panelEnviarEmailAlunosReposicaoDisponivel').show()";
	}*/
	
	public void enviarEmailAlunosTurma() {
		try {
			if(getEnviarTurmaBase()) {
				getFacadeFactory().getHorarioTurmaFacade().realizarEnvioEmailAlunosTurmaPadrao(getHorarioTurmaVO(), getMsgNotificacaoAlunoProgramacaoAula(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getEnviarComunicadoPorEmail());				
			}
			if(getEnviarTurmaReposicao()) {
				if (getHashDisciplinasAlteradas().isEmpty() && getDisciplina().intValue() > 0) {
					getHashDisciplinasAlteradas().put(getDisciplina(), getDisciplina().toString());
				}		
				getFacadeFactory().getHorarioTurmaFacade().realizarEnvioEmailAlunosInclusaoDisciplina(getHorarioTurmaVO(), getMsgNotificacaoAlunoReposicaoAula(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getEnviarComunicadoPorEmail(), getHashDisciplinasAlteradas());
				notificarAlunosAguardandoDisponibilidadeReposicaoDisciplina();
			}		
			if(getEnviarProfessor() ) {			
				getFacadeFactory().getHorarioTurmaFacade().realizarEnvioEmailNotificacaoAlteracaoCronogramaAula(getHorarioTurmaVO(),TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROFESSOR_PROGRAMACAO_AULA,  getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getEnviarComunicadoPorEmail(), getHashDisciplinasAlteradas());
			}
			if(getEnviarCoordenador()) {				
				getFacadeFactory().getHorarioTurmaFacade().realizarEnvioEmailNotificacaoAlteracaoCronogramaAula(getHorarioTurmaVO(), TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_COORDENADOR_PROGRAMACAO_AULA,  getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getEnviarComunicadoPorEmail(), getHashDisciplinasAlteradas());
			}
			if(getEnviarReponsavelUnidade()) {				
				getFacadeFactory().getHorarioTurmaFacade().realizarEnvioEmailNotificacaoAlteracaoCronogramaAula(getHorarioTurmaVO(), TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_RESPONSAVEL_UNIDADE_PROGRAMACAO_AULA ,  getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getEnviarComunicadoPorEmail(), getHashDisciplinasAlteradas());
			}
			if(getEnviarUsuarioEspecifico()) {
				getHorarioTurmaVO().setResponsaveisInternoAptoReceberNotificacaoCronogramaAula(getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados());
				getFacadeFactory().getHorarioTurmaFacade().realizarEnvioEmailNotificacaoAlteracaoCronogramaAula(getHorarioTurmaVO(), TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_USUARIO_PROGRAMACAO_AULA ,  getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getEnviarComunicadoPorEmail(), getHashDisciplinasAlteradas());
			}
	
			
			if(getEnviarTurmaBase() || getEnviarTurmaReposicao()) {
				//getHorarioTurmaVO().getListaTemporariaHorarioTurmaDiaItemVOLog().clear();
			}
			setHashDisciplinasAlteradas(null);
			setListaDisciplinasAlteradasCodigo(null);

			getHorarioTurmaVO().getResponsaveisInternoAptoReceberNotificacaoCronogramaAula().clear();
			setEnviarUsuarioEspecifico(Boolean.FALSE);
			setMensagemID("msg_msg_enviados");
		} catch (Exception e) {
			setHashDisciplinasAlteradas(null);
			setListaDisciplinasAlteradasCodigo(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void enviarEmailAlunosTurmaPadrao() {
		try {
			getFacadeFactory().getHorarioTurmaFacade().realizarEnvioEmailAlunosTurmaPadrao(getHorarioTurmaVO(), getMsgNotificacaoAlunoProgramacaoAula(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getEnviarComunicadoPorEmail());
			// if (getUsuarioLogado().getPessoa() == null ||
			// getUsuarioLogado().getPessoa().getCodigo().equals(0)) {
			// throw new
			// Exception("Este usuáro não pode enviar Comunicação Interna, pois não possue nenhuma pessoa vinculada a ele.");
			// }
			// if(getMsgNotificacaoAlunoProgramacaoAula().isNovoObj() ||
			// getMsgNotificacaoAlunoProgramacaoAula().getDesabilitarEnvioMensagemAutomatica()){
			// throw new Exception("Não existe .");
			// }
			// getComunicacaoInternaVO().setResponsavel(getUsuarioLogado().getPessoa());
			// getComunicacaoInternaVO().setTurma(getHorarioTurmaVO().getTurma());
			// getComunicacaoInternaVO().setTurmaNome(getHorarioTurmaVO().getTurma().getIdentificadorTurma());
			//
			// getComunicacaoInternaVO().setEnviarEmail(getEnviarComunicadoPorEmail());
			// getComunicacaoInternaVO().setTipoDestinatario("TU");
			// getComunicacaoInternaVO().setTipoMarketing(false);
			// getComunicacaoInternaVO().setTipoLeituraObrigatoria(false);
			// getComunicacaoInternaVO().setDigitarMensagem(true);
			// getComunicacaoInternaVO().setRemoverCaixaSaida(false);
			// if
			// (getMsgEnviarTurmaPadrao().equals(getMensagemPadraoAlteracaoHorario()))
			// {
			// getComunicacaoInternaVO().setAssunto("Seu Cronograma de aulas foi Atualizado");
			// getComunicacaoInternaVO().setMensagem(getComunicacaoInternaVO().getMensagemComLayout(getMsgEnviarTurmaPadrao()));
			// } else {
			// getComunicacaoInternaVO().setAssunto("Seu Cronograma de aulas foi Atualizado");
			// getComunicacaoInternaVO().setMensagem(getComunicacaoInternaVO().getMensagemComLayout(getMensagemPadraoAlteracaoHorario()));
			// }
			// adicionarDestinatarios(true);
			// getFacadeFactory().getComunicacaoInternaFacade().incluir(getComunicacaoInternaVO(),
			// true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			// // setMensagemPadraoAlteracaoHorarioInclusao(msgTurmaInclusao);
			// setComunicacaoInternaVO(null);
			// setMsgEnviarTurmaPadrao(null);
			// preencherDadosEmailAlunosInclusaoDisciplina();
			setMensagemID("msg_msg_enviados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void enviarEmailAlunosInclusaoDisciplina() {
		try {
			if (getHashDisciplinasAlteradas().isEmpty() && getDisciplina().intValue() > 0) {
				getHashDisciplinasAlteradas().put(getDisciplina(), getDisciplina().toString());
			}		
			getFacadeFactory().getHorarioTurmaFacade().realizarEnvioEmailAlunosInclusaoDisciplina(getHorarioTurmaVO(), getMsgNotificacaoAlunoReposicaoAula(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getEnviarComunicadoPorEmail(), getHashDisciplinasAlteradas());
			// if (getUsuarioLogado().getPessoa() == null ||
			// getUsuarioLogado().getPessoa().getCodigo().equals(0)) {
			// throw new
			// Exception("Este usuáro não pode enviar Comunicação Interna, pois não possue nenhuma pessoa vinculada a ele.");
			// }
			// getComunicacaoInternaVO().setResponsavel(getUsuarioLogado().getPessoa());
			// getComunicacaoInternaVO().setTurma(getHorarioTurmaVO().getTurma());
			// getComunicacaoInternaVO().setTurmaNome(getHorarioTurmaVO().getTurma().getIdentificadorTurma());
			// //getComunicacaoInternaVO().setAssunto("Seu Cronograma de aulas foi Atualizado");
			// getComunicacaoInternaVO().setEnviarEmail(getEnviarComunicadoPorEmail());
			// getComunicacaoInternaVO().setTipoDestinatario("TU");
			// getComunicacaoInternaVO().setTipoMarketing(false);
			// getComunicacaoInternaVO().setTipoLeituraObrigatoria(false);
			// getComunicacaoInternaVO().setDigitarMensagem(true);
			// getComunicacaoInternaVO().setRemoverCaixaSaida(false);
			// if
			// (getMsgEnviarTurmaInclusao().equals(getMensagemPadraoAlteracaoHorarioInclusao()))
			// {
			// getComunicacaoInternaVO().setAssunto("Sua Reposição de Aula foi Reagendada.");
			// getComunicacaoInternaVO().setMensagem(getComunicacaoInternaVO().getMensagemComLayout(getMsgEnviarTurmaInclusao()));
			// } else {
			// getComunicacaoInternaVO().setAssunto("Sua Reposição de Aula foi Reagendada.");
			// getComunicacaoInternaVO().setMensagem(getComunicacaoInternaVO().getMensagemComLayout(getMensagemPadraoAlteracaoHorarioInclusao()));
			// }
			// adicionarDestinatarios(false);
			// getFacadeFactory().getComunicacaoInternaFacade().incluir(getComunicacaoInternaVO(),
			// true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			// setMensagemPadraoAlteracaoHorario(null);
			setHashDisciplinasAlteradas(null);
			setListaDisciplinasAlteradasCodigo(null);
			setMensagemID("msg_msg_enviados");
		} catch (Exception e) {
			// setMensagemPadraoAlteracaoHorario(null);
			setHashDisciplinasAlteradas(null);
			setListaDisciplinasAlteradasCodigo(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			// preencherDadosEmailAlunosTurma();
			// setComunicacaoInternaVO(null);
		}
	}

	// public void adicionarDestinatarios(boolean turmaPadrao) throws Exception
	// {
	// try {
	// if (getComunicacaoInternaVO().getTurma().getCodigo().intValue() != 0) {
	// List objs = new ArrayList<>(0);
	// if (turmaPadrao) {
	// objs =
	// getFacadeFactory().getPessoaFacade().consultaRapidaAlunosPorCodigoTurmaPadraoSituacao(getComunicacaoInternaVO().getTurma().getCodigo(),
	// "AT", Uteis.getAnoDataAtual4Digitos(), Uteis.getSemestreAtual(), false,
	// getUsuarioLogado());
	// if (!objs.isEmpty()) {
	// objs.add(getUsuarioLogado().getPessoa());
	// }
	// } else {
	// objs =
	// getFacadeFactory().getPessoaFacade().consultaRapidaAlunosPorCodigoTurmaInclusaoSituacaoDisciplinas(getComunicacaoInternaVO().getTurma().getCodigo(),
	// "AT", Uteis.getAnoDataAtual4Digitos(), Uteis.getSemestreAtual(),
	// getListaDisciplinasAlteradasCodigo(), false, getUsuarioLogado());
	// if (!objs.isEmpty()) {
	// objs.add(getUsuarioLogado().getPessoa());
	// }
	// }
	// Iterator i = objs.iterator();
	// while (i.hasNext()) {
	// PessoaVO obj = (PessoaVO) i.next();
	// getComunicadoInternoDestinatarioVO().setDestinatario(obj);
	// getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno("LE");
	// getComunicadoInternoDestinatarioVO().setDataLeitura(null);
	// getComunicadoInternoDestinatarioVO().setCiJaRespondida(false);
	// getComunicadoInternoDestinatarioVO().setCiJaLida(false);
	// getComunicadoInternoDestinatarioVO().setRemoverCaixaEntrada(false);
	// getComunicadoInternoDestinatarioVO().setMensagemMarketingLida(false);
	// getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
	// setComunicadoInternoDestinatarioVO(new
	// ComunicadoInternoDestinatarioVO());
	// }
	// }
	// setMensagemID("msg_dados_adicionados");
	// } catch (Exception e) {
	// setComunicadoInternoDestinatarioVO(new
	// ComunicadoInternoDestinatarioVO());
	// getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().clear();
	// throw e;
	// } finally {
	// setComunicadoInternoDestinatarioVO(null);
	// }
	// }

	// public void preencherDadosEmailAlunosInclusaoDisciplina() {
	// if (!getHashDisciplinasAlteradas().isEmpty()) {
	// String disciplinasAlteradas = "";
	// for (Integer i : getListaDisciplinasAlteradasCodigo()) {
	// if (!disciplinasAlteradas.equals("")) {
	// disciplinasAlteradas += ", " + getHashDisciplinasAlteradas().get(i);
	// } else {
	// disciplinasAlteradas = getHashDisciplinasAlteradas().get(i);
	// }
	// }
	// setMsgEnviarTurmaInclusao(msgTurmaInclusao.replace("#DISCIPLINA",
	// disciplinasAlteradas));
	// }
	// setMsgEnviarTurmaInclusao(getMsgEnviarTurmaInclusao().replace("#UNIDADEENSINO",
	// getHorarioTurmaVO().getTurma().getUnidadeEnsino().getNome()));
	// setMensagemPadraoAlteracaoHorarioInclusao(getMsgEnviarTurmaInclusao());
	// }

	public void verificarDisciplinaJaIncluida(DisciplinaVO disciplina) throws Exception {
		try {
			if (disciplina != null && !disciplina.getCodigo().equals(0) && !getHashDisciplinasAlteradas().containsKey(disciplina.getCodigo())) {
				// if (disciplina.getNome().equals("")) {
				disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				// }
				getHashDisciplinasAlteradas().put(disciplina.getCodigo(), disciplina.getNome());
				getListaDisciplinasAlteradasCodigo().add(disciplina.getCodigo());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public Boolean getMostrarListaTitulacoes() {
		if (getHorarioTurmaVO().getTurma().getCodigo() != 0) {
			return true;
		}
		return false;
	}

	// public void preencherDadosEmailAlunosTurma() {
	// setMensagemPadraoAlteracaoHorario(msgTurmaPadrao);
	// }

	public void preencherDadosEmailAlunosTurmaInclusao() {
		// setMensagemPadraoAlteracaoHorario(msgTurmaPadrao);
		// setMensagemPadraoAlteracaoHorario(null);
		setHashDisciplinasAlteradas(null);
		setListaDisciplinasAlteradasCodigo(null);
		// setComunicacaoInternaVO(null);
		// setMsgEnviarTurmaInclusao(msgTurmaInclusao);
	}

	public void notificarAlunosAguardandoDisponibilidadeReposicaoDisciplina() {
		try {
			JobNotificacaoAulaReposicaoDisponivel jobNotificacaoAulaReposicaoDisponivel = new JobNotificacaoAulaReposicaoDisponivel(getHorarioTurmaVO().getCodigo(), getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade(), getUsuarioLogado());
			Thread thread = new Thread(jobNotificacaoAulaReposicaoDisponivel, "jobNotificacaoAulaReposicaoDisponivel");
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public String getMsgEnviarTurmaInclusao() {
	// if (msgEnviarTurmaInclusao == null) {
	// msgEnviarTurmaInclusao = msgTurmaInclusao;
	// }
	// return msgEnviarTurmaInclusao;
	// }

	// public void setMsgEnviarTurmaInclusao(String msgEnviarTurmaInclusao) {
	// this.msgEnviarTurmaInclusao = msgEnviarTurmaInclusao;
	// }

	// public String getMsgEnviarTurmaPadrao() {
	// if (msgEnviarTurmaPadrao == null) {
	// msgEnviarTurmaPadrao = msgTurmaPadrao;
	// }
	// return msgEnviarTurmaPadrao;
	// }

	// public void setMsgEnviarTurmaPadrao(String msgEnviarTurmaPadrao) {
	// this.msgEnviarTurmaPadrao = msgEnviarTurmaPadrao;
	// }

	// public TitulacaoProfessorCursoVO getTitulacaoProfessorCursoVO() {
	// if (titulacaoProfessorCursoVO == null) {
	// titulacaoProfessorCursoVO = new TitulacaoProfessorCursoVO();
	// }
	// return titulacaoProfessorCursoVO;
	// }
	//
	// public void setTitulacaoProfessorCursoVO(TitulacaoProfessorCursoVO
	// titulacaoProfessorCursoVO) {
	// this.titulacaoProfessorCursoVO = titulacaoProfessorCursoVO;
	// }
	public List<PessoaVO> getListaProfessoresSelecionados() {
		if (listaProfessoresSelecionados == null) {
			listaProfessoresSelecionados = new ArrayList<>(0);
		}
		return listaProfessoresSelecionados;
	}

	public void setListaProfessoresSelecionados(List<PessoaVO> listaProfessoresSelecionados) {
		this.listaProfessoresSelecionados = listaProfessoresSelecionados;
	}

	public boolean getIsApresentarSemestre() {
		if (getIsApresentarAnoSemestre()) {
			if (getHorarioTurmaVO().getTurma().getAnual()) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean getIsApresentarSomenteAno() {
		if (getIsApresentarAnoSemestre()) {
			if (getHorarioTurmaVO().getTurma().getAnual()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public TitulacaoCursoVO getTitulacaoCursoVO() {
		if (titulacaoCursoVO == null) {
			titulacaoCursoVO = new TitulacaoCursoVO();
		}
		return titulacaoCursoVO;
	}

	public void setTitulacaoCursoVO(TitulacaoCursoVO titulacaoCursoVO) {
		this.titulacaoCursoVO = titulacaoCursoVO;
	}

	public HashMap<Integer, Integer> getHashMapQtdeNivelEscolaridade() {
		if (hashMapQtdeNivelEscolaridade == null) {
			hashMapQtdeNivelEscolaridade = new HashMap<Integer, Integer>(0);
		}
		return hashMapQtdeNivelEscolaridade;
	}

	public void setHashMapQtdeNivelEscolaridade(HashMap<Integer, Integer> hashMapQtdeNivelEscolaridade) {
		this.hashMapQtdeNivelEscolaridade = hashMapQtdeNivelEscolaridade;
	}

	public HashMap<String, String> getHashMapProfessorDisciplina() {
		if (hashMapProfessorDisciplina == null) {
			hashMapProfessorDisciplina = new HashMap<String, String>(0);
		}
		return hashMapProfessorDisciplina;
	}

	public void setHashMapProfessorDisciplina(HashMap<String, String> hashMapProfessorDisciplina) {
		this.hashMapProfessorDisciplina = hashMapProfessorDisciplina;
	}

	public Boolean getPanelGravarTitulacao() {
		if (panelGravarTitulacao == null) {
			panelGravarTitulacao = false;
		}
		return panelGravarTitulacao;
	}

	public void setPanelGravarTitulacao(Boolean panelGravarTitulacao) {
		this.panelGravarTitulacao = panelGravarTitulacao;
	}

	public PersonalizacaoMensagemAutomaticaVO getMsgNotificacaoAlunoProgramacaoAula() {
		if (msgNotificacaoAlunoProgramacaoAula == null) {
			try {
				msgNotificacaoAlunoProgramacaoAula = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ALUNO_PROGRAMACAO_AULA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo(), getUsuarioLogado(), null);
				if (msgNotificacaoAlunoProgramacaoAula == null) {
					msgNotificacaoAlunoProgramacaoAula = new PersonalizacaoMensagemAutomaticaVO();
				}
			} catch (Exception e) {
				msgNotificacaoAlunoProgramacaoAula = new PersonalizacaoMensagemAutomaticaVO();
				e.printStackTrace();
			}
		}
		return msgNotificacaoAlunoProgramacaoAula;
	}

	public void setMsgNotificacaoAlunoProgramacaoAula(PersonalizacaoMensagemAutomaticaVO msgNotificacaoAlunoProgramacaoAula) {
		this.msgNotificacaoAlunoProgramacaoAula = msgNotificacaoAlunoProgramacaoAula;
	}

	public PersonalizacaoMensagemAutomaticaVO getMsgNotificacaoAlunoReposicaoAula() {
		if (msgNotificacaoAlunoReposicaoAula == null) {
			try {
				msgNotificacaoAlunoReposicaoAula = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ALUNO_REPOSICAO_AULA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo(), getUsuarioLogado(), null);
				if (msgNotificacaoAlunoReposicaoAula == null) {
					msgNotificacaoAlunoReposicaoAula = new PersonalizacaoMensagemAutomaticaVO();
				}
			} catch (Exception e) {
				msgNotificacaoAlunoReposicaoAula = new PersonalizacaoMensagemAutomaticaVO();
				e.printStackTrace();
			}
		}
		return msgNotificacaoAlunoReposicaoAula;
	}

	public void setMsgNotificacaoAlunoReposicaoAula(PersonalizacaoMensagemAutomaticaVO msgNotificacaoAlunoReposicaoAula) {
		this.msgNotificacaoAlunoReposicaoAula = msgNotificacaoAlunoReposicaoAula;
	}

	public PersonalizacaoMensagemAutomaticaVO getMsgNotificacaoAlunoReposicaoAulaDisponivel() {
		if (msgNotificacaoAlunoReposicaoAulaDisponivel == null) {
			try {
				msgNotificacaoAlunoReposicaoAulaDisponivel = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ALUNO_REPOSICAO_AULA_DISPONIVEL, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo(), getUsuarioLogado(), null);
				if (msgNotificacaoAlunoReposicaoAulaDisponivel == null) {
					msgNotificacaoAlunoReposicaoAulaDisponivel = new PersonalizacaoMensagemAutomaticaVO();
				}
			} catch (Exception e) {
				msgNotificacaoAlunoReposicaoAulaDisponivel = new PersonalizacaoMensagemAutomaticaVO();
				e.printStackTrace();
			}
		}
		return msgNotificacaoAlunoReposicaoAulaDisponivel;
	}

	public void setMsgNotificacaoAlunoReposicaoAulaDisponivel(PersonalizacaoMensagemAutomaticaVO msgNotificacaoAlunoReposicaoAulaDisponivel) {
		this.msgNotificacaoAlunoReposicaoAulaDisponivel = msgNotificacaoAlunoReposicaoAulaDisponivel;
	}

	public void inicializarListaHorarioTurmaDisciplinaProgramada(HorarioTurmaVO horarioTurmaVO) throws Exception {
		getFacadeFactory().getHorarioTurmaFacade().inicializarListaHorarioTurmaDisciplinaProgramada(horarioTurmaVO, isPermitirProgramacaoAulaComClassroom(), isPermitirProgramacaoAulaComBlackboard(), getUsuarioLogadoClone());
	}

	public List<ChoqueHorarioVO> getListaMensagemProgramarAulaSemChoqueHorario() {
		if (listaMensagemProgramarAulaSemChoqueHorario == null) {
			listaMensagemProgramarAulaSemChoqueHorario = new ArrayList<ChoqueHorarioVO>(0);
		}
		return listaMensagemProgramarAulaSemChoqueHorario;
	}

	public void setListaMensagemProgramarAulaSemChoqueHorario(List<ChoqueHorarioVO> listaMensagemProgramarAulaSemChoqueHorario) {
		this.listaMensagemProgramarAulaSemChoqueHorario = listaMensagemProgramarAulaSemChoqueHorario;
	}

	public Boolean getAlteracaoComAulaRegistrada() {
		if (alteracaoComAulaRegistrada == null) {
			alteracaoComAulaRegistrada = false;
		}
		return alteracaoComAulaRegistrada;
	}

	public void setAlteracaoComAulaRegistrada(Boolean alteracaoComAulaRegistrada) {
		this.alteracaoComAulaRegistrada = alteracaoComAulaRegistrada;
	}

	public Boolean getRetornarExcecaoAlteracaoComAulaRegistrada() {
		if (retornarExcecaoAlteracaoComAulaRegistrada == null) {
			retornarExcecaoAlteracaoComAulaRegistrada = true;
		}
		return retornarExcecaoAlteracaoComAulaRegistrada;
	}

	public void setRetornarExcecaoAlteracaoComAulaRegistrada(Boolean retornarExcecaoAlteracaoComAulaRegistrada) {
		this.retornarExcecaoAlteracaoComAulaRegistrada = retornarExcecaoAlteracaoComAulaRegistrada;
	}

	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	public List<SelectItem> getListaSelectItemDisciplinaProfessor() {
		if (listaSelectItemDisciplinaProfessor == null) {
			listaSelectItemDisciplinaProfessor = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinaProfessor;
	}

	public void setListaSelectItemDisciplinaProfessor(List<SelectItem> listaSelectItemDisciplinaProfessor) {
		this.listaSelectItemDisciplinaProfessor = listaSelectItemDisciplinaProfessor;
	}

	public void selecionarOcultarDataAula() {
		try {
			if (this.getHorarioTurmaPorDiaVO().getCodigo().intValue() > 0) {
				getFacadeFactory().getHorarioTurmaDiaFacade().alterar(this.getHorarioTurmaPorDiaVO(), getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the listaSelectItemSala
	 */
	public List<SelectItem> getListaSelectItemSala() {
		if (listaSelectItemSala == null) {
			listaSelectItemSala = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSala;
	}

	/**
	 * @param listaSelectItemSala the listaSelectItemSala to set
	 */
	public void setListaSelectItemSala(List<SelectItem> listaSelectItemSala) {
		this.listaSelectItemSala = listaSelectItemSala;
	}

	/**
	 * @return the sala
	 */
	public SalaLocalAulaVO getSala() {
		if (sala == null) {
			sala = new SalaLocalAulaVO();
		}
		return sala;
	}

	/**
	 * @param sala the sala to set
	 */
	public void setSala(SalaLocalAulaVO sala) {
		this.sala = sala;
	}
	
	public void montarListaSelectItemLocal(){
		getListaSelectItemLocal().clear();
		try{
			List<LocalAulaVO> localAulaVOs = getFacadeFactory().getLocalAulaFacade().consultaLocalSalaAulaPorSituacao(StatusAtivoInativoEnum.ATIVO, getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemLocal().add(new SelectItem(0, ""));
			for(LocalAulaVO local: localAulaVOs){
				getListaSelectItemLocal().add(new SelectItem(local.getCodigo(), local.getLocal()));
			}
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaSelectItemSala(){
		getListaSelectItemSala().clear();
		try{
			List<SalaLocalAulaVO> salaLocalAulaVOs = getFacadeFactory().getSalaLocalAulaFacade().consultarComboboxPorLocalAula(getHorarioTurmaVO().getSala().getLocalAula().getCodigo(), true);
			getListaSelectItemSala().add(new SelectItem(0, ""));
			for(SalaLocalAulaVO sala: salaLocalAulaVOs){
				getListaSelectItemSala().add(new SelectItem(sala.getCodigo(), sala.getSala()));
			}
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaSelectItemSalaSubstituta(){
		getListaSelectItemSala().clear();
		try{
			List<SalaLocalAulaVO> salaLocalAulaVOs = getFacadeFactory().getSalaLocalAulaFacade().consultarComboboxPorLocalAula(getHorarioTurmaVO().getSalaSubstituta().getLocalAula().getCodigo(), true);
			getListaSelectItemSala().add(new SelectItem(0, ""));
			for(SalaLocalAulaVO sala: salaLocalAulaVOs){
				getListaSelectItemSala().add(new SelectItem(sala.getCodigo(), sala.getSala()));
			}
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the listaChoqueHorariaAulaAcimaLimite
	 */
	public List<ChoqueHorarioVO> getListaChoqueHorariaAulaAcimaLimite() {
		if (listaChoqueHorariaAulaAcimaLimite == null) {
			listaChoqueHorariaAulaAcimaLimite = new ArrayList<ChoqueHorarioVO>(0);
		}
		return listaChoqueHorariaAulaAcimaLimite;
	}

	/**
	 * @param listaChoqueHorariaAulaAcimaLimite the listaChoqueHorariaAulaAcimaLimite to set
	 */
	public void setListaChoqueHorariaAulaAcimaLimite(List<ChoqueHorarioVO> listaChoqueHorariaAulaAcimaLimite) {
		this.listaChoqueHorariaAulaAcimaLimite = listaChoqueHorariaAulaAcimaLimite;
	}

	/**
	 * @return the listaChoqueHorariaSala
	 */
	public List<ChoqueHorarioVO> getListaChoqueHorariaSala() {
		if (listaChoqueHorariaSala == null) {
			listaChoqueHorariaSala = new ArrayList<ChoqueHorarioVO>(0);
		}
		return listaChoqueHorariaSala;
	}

	/**
	 * @param listaChoqueHorariaSala the listaChoqueHorariaSala to set
	 */
	public void setListaChoqueHorariaSala(List<ChoqueHorarioVO> listaChoqueHorariaSala) {
		this.listaChoqueHorariaSala = listaChoqueHorariaSala;
	}

	/**
	 * @return the listaChoqueHorariaAulaRegistrada
	 */
	public List<ChoqueHorarioVO> getListaChoqueHorariaAulaRegistrada() {
		if (listaChoqueHorariaAulaRegistrada == null) {
			listaChoqueHorariaAulaRegistrada = new ArrayList<ChoqueHorarioVO>(0);
		}
		return listaChoqueHorariaAulaRegistrada;
	}

	/**
	 * @param listaChoqueHorariaAulaRegistrada the listaChoqueHorariaAulaRegistrada to set
	 */
	public void setListaChoqueHorariaAulaRegistrada(List<ChoqueHorarioVO> listaChoqueHorariaAulaRegistrada) {
		this.listaChoqueHorariaAulaRegistrada = listaChoqueHorariaAulaRegistrada;
	}

	/**
	 * @return the listaChoqueHorariaAula
	 */
	public List<ChoqueHorarioVO> getListaChoqueHorariaAula() {
		if (listaChoqueHorariaAula == null) {
			listaChoqueHorariaAula = new ArrayList<ChoqueHorarioVO>(0);
		}
		return listaChoqueHorariaAula;
	}

	/**
	 * @param listaChoqueHorariaAula the listaChoqueHorariaAula to set
	 */
	public void setListaChoqueHorariaAula(List<ChoqueHorarioVO> listaChoqueHorariaAula) {
		this.listaChoqueHorariaAula = listaChoqueHorariaAula;
	}

	/**
	 * @return the permiteLiberarProgramacaoAulaProfessorAcimaPermitido
	 */
	public Boolean getLiberarProgramacaoAulaProfessorAcimaPermitido() {
		if (liberarProgramacaoAulaProfessorAcimaPermitido == null) {
			liberarProgramacaoAulaProfessorAcimaPermitido = false;
		}
		return liberarProgramacaoAulaProfessorAcimaPermitido;
	}

	/**
	 * @param permiteLiberarProgramacaoAulaProfessorAcimaPermitido the permiteLiberarProgramacaoAulaProfessorAcimaPermitido to set
	 */
	public void setLiberarProgramacaoAulaProfessorAcimaPermitido(Boolean liberarProgramacaoAulaProfessorAcimaPermitido) {
		this.liberarProgramacaoAulaProfessorAcimaPermitido = liberarProgramacaoAulaProfessorAcimaPermitido;
	}
	
	public void realizarSeparacaoChoqueHorarioPorTipo(){
		getListaChoqueHorariaAula().clear();
		getListaChoqueHorariaAulaAcimaLimite().clear();
		getListaChoqueHorariaAulaRegistrada().clear();
		getListaChoqueHorariaSala().clear();
		for(ChoqueHorarioVO choqueHorarioVO: getListaMensagemProgramarAulaPeriodo()){
			if(choqueHorarioVO.getAulaRegistrada()){
				getListaChoqueHorariaAulaRegistrada().add(choqueHorarioVO);
				Ordenacao.ordenarLista(getListaChoqueHorariaAulaRegistrada(), "ordenacao");
			}else if(choqueHorarioVO.getChoqueHorarioSala()){
				getListaChoqueHorariaSala().add(choqueHorarioVO);
				Ordenacao.ordenarLista(getListaChoqueHorariaSala(), "ordenacao");
			}else if(choqueHorarioVO.getChoqueHorarioAulaExcesso()){
				getListaChoqueHorariaAulaAcimaLimite().add(choqueHorarioVO);
				Ordenacao.ordenarLista(getListaChoqueHorariaAulaAcimaLimite(), "ordenacao");
			}else{
				getListaChoqueHorariaAula().add(choqueHorarioVO);
				Ordenacao.ordenarLista(getListaChoqueHorariaAula(), "ordenacao");
			}
		}
	}
	
	public void realizarLiberarProgramacaoAulaProfessorAcimaLimitePermitido(){
		try{	
			setMensagemLiberacaoAulaProfessorAcimaPermitido("");
			setLiberarProgramacaoAulaProfessorAcimaPermitido(ControleAcesso.verificarPermissaoFuncionalidadePorUsernameSenhaUnidadeEnsino("PermiteLiberarProgramacaoAulaProfessorAcimaPermitido", getUsername(), getSenha(), getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo(), true));
			setMensagemLiberacaoAulaProfessorAcimaPermitido(UteisJSF.internacionalizar("msg_ProgramacaoAula_alulaAcimaLimiteLiberada"));
		}catch(Exception e){
			setMensagemLiberacaoAulaProfessorAcimaPermitido(e.getMessage());
			setLiberarProgramacaoAulaProfessorAcimaPermitido(false);
		}
	}
	
	public void carregarDadosLiberarFuncionalidade() {
		try {
			setUserNameLiberarFuncionalidade("");
			setSenhaLiberarFuncionalidade("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarVerificacaoUsuarioLiberacaoChoqueHorario() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_LIBERAR_PROGRAMACAO_AULA_COM_CHOQUE_HORARIO, usuarioVerif);
			getHorarioProfessorVO().setOperacaoFuncionalidadeVO(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.PROGRAMACAO_AULA, "", OperacaoFuncionalidadeEnum.PROGRAMACAO_AULA_LIBERAR_COM_CHOQUE_HORARIO, usuarioVerif, ""));
			Date dataInicio = null;
			Date dataFim = null;
			Date diaBase = null;
			if (!getProgramarAulaPeriodo()) {
				dataInicio = getHorarioTurmaVO().getDia();
				dataFim = getHorarioTurmaVO().getDia();
			} else {
				dataInicio = getHorarioTurmaVO().getDiaInicio();
				dataFim = getHorarioTurmaVO().getDiaFim();
			}
			diaBase = Uteis.getDateHoraFinalDia(dataInicio);
			List<DisponibilidadeHorarioTurmaProfessorVO> disponibilidadeHorarioTurmaProfessorVOs = null;
			while (Uteis.getDateHoraFinalDia(diaBase).compareTo(Uteis.getDateHoraFinalDia(dataFim)) <= 0) {
				disponibilidadeHorarioTurmaProfessorVOs = getHorarioProfessorVO().consultarListaHorarioDisponivelTurmaProfessorVO(Uteis.getDiaSemanaEnum(diaBase));
				for (DisponibilidadeHorarioTurmaProfessorVO disponibilidadeHorarioProfessorVO : disponibilidadeHorarioTurmaProfessorVOs) {
					diaBase = Uteis.getDateHoraFinalDia(diaBase);
					if (disponibilidadeHorarioProfessorVO.getProgramarAula() 
							&& disponibilidadeHorarioProfessorVO.getHorarioLivre()
							&& (!disponibilidadeHorarioProfessorVO.getNaoPossueChoqueHorario() || disponibilidadeHorarioProfessorVO.getPossuiChoqueSala())) {
						disponibilidadeHorarioProfessorVO.setNaoPossueChoqueHorario(true);
						disponibilidadeHorarioProfessorVO.setPossuiChoqueSala(false);
					}
				}
				diaBase = Uteis.getDateHoraFinalDia(Uteis.obterDataFutura(diaBase, 1));
			}
			executarAdicionarDadosHorarioTurmaProfessorPorDiaPeriodoSemValidarDisponibilidade();
			setManterRichModalLiberarComChoqueHorario("RichFaces.$('panelLiberarComChoqueHorario').hide();RichFaces.$('panelConfirmarHorario').hide();RichFaces.$('panelProfessorHorario').hide();RichFaces.$('panelEnviarEmailAlunosTurmaPadrao').show();"+getAcaoModalChoqueHorario());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setManterRichModalLiberarComChoqueHorario("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	
	public String getManterRichModalLiberarComChoqueHorario() {
		if (manterRichModalLiberarComChoqueHorario == null) {
			manterRichModalLiberarComChoqueHorario = "";
		}
		return manterRichModalLiberarComChoqueHorario;
	}

	public void setManterRichModalLiberarComChoqueHorario(String manterRichModalLiberarComChoqueHorario) {
		this.manterRichModalLiberarComChoqueHorario = manterRichModalLiberarComChoqueHorario;
	}

	public String getUserNameLiberarFuncionalidade() {
		if (userNameLiberarFuncionalidade == null) {
			userNameLiberarFuncionalidade = "";
		}
		return userNameLiberarFuncionalidade;
	}

	public void setUserNameLiberarFuncionalidade(String userNameLiberarValorAcimaPrevisto) {
		this.userNameLiberarFuncionalidade = userNameLiberarValorAcimaPrevisto;
	}

	public String getSenhaLiberarFuncionalidade() {
		if (senhaLiberarFuncionalidade == null) {
			senhaLiberarFuncionalidade = "";
		}
		return senhaLiberarFuncionalidade;
	}

	public void setSenhaLiberarFuncionalidade(String senhaLiberarValorAcimaPrevisto) {
		this.senhaLiberarFuncionalidade = senhaLiberarValorAcimaPrevisto;
	}
	

	public Boolean isLiberarProgramacaoAulaComChoqueHorario() {
		return liberarProgramacaoAulaComChoqueHorario;
	}

	public void setLiberarProgramacaoAulaComChoqueHorario(Boolean liberarProgramacaoAulaComChoqueHorario) {
		this.liberarProgramacaoAulaComChoqueHorario = liberarProgramacaoAulaComChoqueHorario;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		if (username == null) {
			username = "";
		}
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the senha
	 */
	public String getSenha() {
		if (senha == null) {
			senha = "";
		}
		return senha;
	}

	/**
	 * @param senha the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Boolean getApresentarOpcaoLiberacaoAulaAcimaLimite(){
		return !getListaChoqueHorariaAulaAcimaLimite().isEmpty();
	}
	
	public Integer getQtdeChoqueHorarioSala(){
		return getListaChoqueHorariaSala().size();
	}
	public Integer getQtdeChoqueHorarioAulaRegistrada(){
		return getListaChoqueHorariaAulaRegistrada().size();
	}
	
	public Integer getQtdeChoqueHorarioAulaAcimaLimite(){
		return getListaChoqueHorariaAulaAcimaLimite().size();
	}
	public Integer getQtdeChoqueHorarioAula(){
		return getListaChoqueHorariaAula().size();
	}

	/**
	 * @return the mensagemLiberacaoAulaProfessorAcimaPermitido
	 */
	public String getMensagemLiberacaoAulaProfessorAcimaPermitido() {
		if (mensagemLiberacaoAulaProfessorAcimaPermitido == null) {
			mensagemLiberacaoAulaProfessorAcimaPermitido = "";
		}
		return mensagemLiberacaoAulaProfessorAcimaPermitido;
	}

	/**
	 * @param mensagemLiberacaoAulaProfessorAcimaPermitido the mensagemLiberacaoAulaProfessorAcimaPermitido to set
	 */
	public void setMensagemLiberacaoAulaProfessorAcimaPermitido(String mensagemLiberacaoAulaProfessorAcimaPermitido) {
		this.mensagemLiberacaoAulaProfessorAcimaPermitido = mensagemLiberacaoAulaProfessorAcimaPermitido;
	}

	/**
	 * @return the choqueHorarioAlunoVOs
	 */
	public List<ChoqueHorarioAlunoVO> getChoqueHorarioAlunoVOs() {
		if (choqueHorarioAlunoVOs == null) {
			choqueHorarioAlunoVOs = new ArrayList<ChoqueHorarioAlunoVO>(0);
		}
		return choqueHorarioAlunoVOs;
	}

	/**
	 * @param choqueHorarioAlunoVOs the choqueHorarioAlunoVOs to set
	 */
	public void setChoqueHorarioAlunoVOs(List<ChoqueHorarioAlunoVO> choqueHorarioAlunoVOs) {
		this.choqueHorarioAlunoVOs = choqueHorarioAlunoVOs;
	}
	
	public void consultarChoqueHorarioAluno(){
		try{
			getChoqueHorarioAlunoVOs().clear();
			setChoqueHorarioAlunoVOs(getFacadeFactory().getHorarioAlunoFacade().consultarChoqueHorarioAlunoPorTurmaAnoSemestre(getHorarioTurmaVO().getTurma(), getHorarioTurmaVO().getAnoVigente(), getHorarioTurmaVO().getSemestreVigente(), 0));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);			
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarChoqueHorarioAlunoDetalhe(){
		try{
			setChoqueHorarioAlunoVO((ChoqueHorarioAlunoVO) getRequestMap().get("choqueHorarioAluno"));
			if(getChoqueHorarioAlunoVO().getChoqueHorarioAlunoDetalheVOs().isEmpty()){
				getChoqueHorarioAlunoVO().setChoqueHorarioAlunoDetalheVOs(getFacadeFactory().getHorarioAlunoFacade().consultarChoqueHorarioAlunoDetalhePorTurmaAnoSemestre(getHorarioTurmaVO().getTurma(), getChoqueHorarioAlunoVO().getMatricula().getMatricula(), getHorarioTurmaVO().getAnoVigente(), getHorarioTurmaVO().getSemestreVigente(), 0));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * @return the choqueHorarioAlunoVO
	 */
	public ChoqueHorarioAlunoVO getChoqueHorarioAlunoVO() {
		if (choqueHorarioAlunoVO == null) {
			choqueHorarioAlunoVO = new ChoqueHorarioAlunoVO();
		}
		return choqueHorarioAlunoVO;
	}

	/**
	 * @param choqueHorarioAlunoVO the choqueHorarioAlunoVO to set
	 */
	public void setChoqueHorarioAlunoVO(ChoqueHorarioAlunoVO choqueHorarioAlunoVO) {
		this.choqueHorarioAlunoVO = choqueHorarioAlunoVO;
	}
	
	public Integer getQtdeChoqueHorarioAlunoVOs(){
		return getChoqueHorarioAlunoVOs().size();
	}
	
	
	
	/**
	 * @return the programacaoAulaResumoSemanaVOs
	 */
	public List<ProgramacaoAulaResumoSemanaVO> getProgramacaoAulaResumoSemanaVOs() {
		if (programacaoAulaResumoSemanaVOs == null) {
			programacaoAulaResumoSemanaVOs = new ArrayList<ProgramacaoAulaResumoSemanaVO>(0);
		}
		return programacaoAulaResumoSemanaVOs;
	}

	/**
	 * @param programacaoAulaResumoSemanaVOs the programacaoAulaResumoSemanaVOs to set
	 */
	public void setProgramacaoAulaResumoSemanaVOs(List<ProgramacaoAulaResumoSemanaVO> programacaoAulaResumoSemanaVOs) {
		this.programacaoAulaResumoSemanaVOs = programacaoAulaResumoSemanaVOs;
	}

	public void realizarMontagemListaProgramacaoAulaResumoSemanaVO(){
		setProgramacaoAulaResumoSemanaVOs(getFacadeFactory().getHorarioTurmaFacade().realizarMontagemListaProgramacaoAulaResumoSemanaVO(getHorarioTurmaVO()));
	}

	/**
	 * @return the calendarioResumido
	 */
	public Boolean getCalendarioResumido() {
		if (calendarioResumido == null) {
			calendarioResumido = true;
		}
		return calendarioResumido;
	}

	/**
	 * @param calendarioResumido the calendarioResumido to set
	 */
	public void setCalendarioResumido(Boolean calendarioResumido) {
		this.calendarioResumido = calendarioResumido;
	}

	/**
	 * @return the disciplinaFilter
	 */
	public Integer getDisciplinaFilter() {
		if (disciplinaFilter == null) {
			disciplinaFilter = 0;
		}
		return disciplinaFilter;
	}

	/**
	 * @param disciplinaFilter the disciplinaFilter to set
	 */
	public void setDisciplinaFilter(Integer disciplinaFilter) {
		this.disciplinaFilter = disciplinaFilter;
	}

	/**
	 * @return the professorFilter
	 */
	public Integer getProfessorFilter() {
		if (professorFilter == null) {
			professorFilter = 0;
		}
		return professorFilter;
	}

	/**
	 * @param professorFilter the professorFilter to set
	 */
	public void setProfessorFilter(Integer professorFilter) {
		this.professorFilter = professorFilter;
	}
		

	/**
	 * @return the situacaoTurma
	 */
	public String getSituacaoTurma() {
		if (situacaoTurma == null) {
			situacaoTurma = "AB";
		}
		return situacaoTurma;
	}

	/**
	 * @param situacaoTurma the situacaoTurma to set
	 */
	public void setSituacaoTurma(String situacaoTurma) {
		this.situacaoTurma = situacaoTurma;
	}

	/**
	 * @return the situacaoTipoTurma
	 */
	public String getSituacaoTipoTurma() {
		if (situacaoTipoTurma == null) {
			situacaoTipoTurma = "";
		}
		return situacaoTipoTurma;
	}

	/**
	 * @param situacaoTipoTurma the situacaoTipoTurma to set
	 */
	public void setSituacaoTipoTurma(String situacaoTipoTurma) {
		this.situacaoTipoTurma = situacaoTipoTurma;
	}

	/**
	 * @return the listaSelectItemSituacaoTurma
	 */
	public List<SelectItem> getListaSelectItemSituacaoTurma() {
		if (listaSelectItemSituacaoTurma == null) {
			listaSelectItemSituacaoTurma = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoTurma.add(new SelectItem("", ""));
			listaSelectItemSituacaoTurma.add(new SelectItem("AB", "Aberta"));
			listaSelectItemSituacaoTurma.add(new SelectItem("FE", "Fechada"));					
		}
		return listaSelectItemSituacaoTurma;
	}

	/**
	 * @param listaSelectItemSituacaoTurma the listaSelectItemSituacaoTurma to set
	 */
	public void setListaSelectItemSituacaoTurma(List<SelectItem> listaSelectItemSituacaoTurma) {
		this.listaSelectItemSituacaoTurma = listaSelectItemSituacaoTurma;
	}

	/**
	 * @return the listaSelectItemSituacaoTipoTurma
	 */
	public List<SelectItem> getListaSelectItemSituacaoTipoTurma() {
		if (listaSelectItemSituacaoTipoTurma == null) {
			listaSelectItemSituacaoTipoTurma = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoTipoTurma.add(new SelectItem("", ""));
			listaSelectItemSituacaoTipoTurma.add(new SelectItem("normal", "Normal"));
			listaSelectItemSituacaoTipoTurma.add(new SelectItem("agrupada", "Agrupada"));
			listaSelectItemSituacaoTipoTurma.add(new SelectItem("subturma", "Subturma"));			
		}
		return listaSelectItemSituacaoTipoTurma;
	}

	/**
	 * @param listaSelectItemSituacaoTipoTurma the listaSelectItemSituacaoTipoTurma to set
	 */
	public void setListaSelectItemSituacaoTipoTurma(List<SelectItem> listaSelectItemSituacaoTipoTurma) {
		this.listaSelectItemSituacaoTipoTurma = listaSelectItemSituacaoTipoTurma;
	}

	/**
	 * @return the listaSelectItemLocal
	 */
	public List<SelectItem> getListaSelectItemLocal() {
		if (listaSelectItemLocal == null) {
			listaSelectItemLocal = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemLocal;
	}

	/**
	 * @param listaSelectItemLocal the listaSelectItemLocal to set
	 */
	public void setListaSelectItemLocal(List<SelectItem> listaSelectItemLocal) {
		this.listaSelectItemLocal = listaSelectItemLocal;
	}
	
	public void realizarNavegacaoVagaTurma(){
		removerControleMemoriaFlashTela("VagaTurmaControle");
		context().getExternalContext().getSessionMap().put("vagaTurma.turma", getHorarioTurmaVO().getTurma().getCodigo());
		context().getExternalContext().getSessionMap().put("vagaTurma.ano", getHorarioTurmaVO().getAnoVigente());
		context().getExternalContext().getSessionMap().put("vagaTurma.semestre", getHorarioTurmaVO().getSemestreVigente());
	}
	
	@PostConstruct
	public void inicializarDadosNavegacaoPagina(){
		if (context().getExternalContext().getSessionMap().containsKey("programacaoAula.turma")) {
			novo();
			getHorarioTurmaVO().getTurma().setCodigo((Integer) context().getExternalContext().getSessionMap().get("programacaoAula.turma"));
			context().getExternalContext().getSessionMap().remove("programacaoAula.turma");
			getHorarioTurmaVO().setAnoVigente((String) context().getExternalContext().getSessionMap().get("programacaoAula.ano"));
			context().getExternalContext().getSessionMap().remove("programacaoAula.ano");
			getHorarioTurmaVO().setSemestreVigente((String) context().getExternalContext().getSessionMap().get("programacaoAula.semestre"));
			context().getExternalContext().getSessionMap().remove("programacaoAula.semestre");			
			try {
				getFacadeFactory().getTurmaFacade().carregarDados(getHorarioTurmaVO().getTurma(), getUsuarioLogado());
				consultarTurmaPorChavePrimaria();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
	}

	public Boolean getErroExclusao() {
		if (erroExclusao == null) {
			erroExclusao = Boolean.FALSE;
		}
		return erroExclusao;
	}

	public void setErroExclusao(Boolean erroExclusao) {
		this.erroExclusao = erroExclusao;
	}
	
	private void executarBloqueioTurmaProgramacaoAula(boolean adicionar, boolean retornarExcecao) throws AcessoException {		
		AplicacaoControle.executarBloqueioTurmaProgramacaoAula(getHorarioTurmaVO().getTurma(), getHorarioTurmaVO().getUpdated(), getHorarioTurmaVO().getAnoVigente(), getHorarioTurmaVO().getSemestreVigente(), adicionar, retornarExcecao, getFacadeFactory().getHorarioTurmaFacade(), getUsuarioLogado());
	}

	
	private HorarioTurmaDisciplinaProgramadaVO horarioTurmaDisciplinaProgramadaVO;

	/**
	 * @return the horarioTurmaDisciplinaProgramadaVO
	 */
	public HorarioTurmaDisciplinaProgramadaVO getHorarioTurmaDisciplinaProgramadaVO() {
		if (horarioTurmaDisciplinaProgramadaVO == null) {
			horarioTurmaDisciplinaProgramadaVO = new HorarioTurmaDisciplinaProgramadaVO();
		}
		return horarioTurmaDisciplinaProgramadaVO;
	}

	/**
	 * @param horarioTurmaDisciplinaProgramadaVO the horarioTurmaDisciplinaProgramadaVO to set
	 */
	public void setHorarioTurmaDisciplinaProgramadaVO(HorarioTurmaDisciplinaProgramadaVO horarioTurmaDisciplinaProgramadaVO) {
		this.horarioTurmaDisciplinaProgramadaVO = horarioTurmaDisciplinaProgramadaVO;
	}
	
	public void editarHorarioTurma(){
		try {			
			HorarioTurmaDisciplinaProgramadaVO obj = (HorarioTurmaDisciplinaProgramadaVO) context().getExternalContext().getRequestMap().get("horarioTurmaDisciplinaProgramadaOutraTurma");
			obj.getHorarioTurmaVO().setAnoVigente(getHorarioTurmaVO().getAnoVigente());
			obj.getHorarioTurmaVO().setSemestreVigente(getHorarioTurmaVO().getSemestreVigente());
			obj.getHorarioTurmaVO().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaEspecifico(obj.getHorarioTurmaVO().getTurma(), obj.getHorarioTurmaVO().getTurma().getIdentificadorTurma(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			carregarDadosEdicao(obj.getHorarioTurmaVO());			
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			setMensagemDetalhada("msg_erro", e.getMessage());			
		}
	}

	private String acaoModalChoqueHorario;

	/**
	 * @return the acaoModalChoqueHorario
	 */
	public String getAcaoModalChoqueHorario() {
		if (acaoModalChoqueHorario == null) {
			acaoModalChoqueHorario = "";
		}
		return acaoModalChoqueHorario;
	}

	/**
	 * @param acaoModalChoqueHorario the acaoModalChoqueHorario to set
	 */
	public void setAcaoModalChoqueHorario(String acaoModalChoqueHorario) {
		this.acaoModalChoqueHorario = acaoModalChoqueHorario;
	}

	public boolean isApresentarBotaoLiberacaoChoqueHorario() {
		return getListaChoqueHorariaAula().stream().anyMatch(p-> p.getChoqueHorarioProfessor()) ||
				getListaChoqueHorariaSala().size() > 0;
	}

	public Boolean getOcultarHorarioSemAula() {
		if(ocultarHorarioSemAula == null){
			ocultarHorarioSemAula = true;
		}
		return ocultarHorarioSemAula;
	}

	public void setOcultarHorarioSemAula(Boolean ocultarHorarioSemAula) {
		this.ocultarHorarioSemAula = ocultarHorarioSemAula;
	}

	public void prepararDadosImprimirHorario() {
		if (Uteis.isAtributoPreenchido(getHorarioTurmaVO().getTurma())) {
			removerControleMemoriaFlashTela("HorarioDaTurmaRelControle");
			context().getExternalContext().getSessionMap().put("turmaHorarioTurmaTelaProgramacao", getHorarioTurmaVO().getTurma());
		}
	}

	public String navegarCadastrarTutoriaOnline() {
		try {
			removerControleMemoriaFlashTela("ProgramacaoTutoriaOnlineControle");
			
			HorarioTurmaDisciplinaProgramadaVO obj = (HorarioTurmaDisciplinaProgramadaVO) context().getExternalContext().getRequestMap().get("horarioTurmaDisciplinaProgramadaItens");
			
			context().getExternalContext().getSessionMap().put("turma", getHorarioTurmaVO().getTurma());
			context().getExternalContext().getSessionMap().put("codigoDisciplina", obj.getCodigoDisciplina());
			context().getExternalContext().getSessionMap().put("disciplina", obj.getNomeDisciplina());
			
			return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoAdministrativo/ead/programacaoTutoriaOnlineForm.xhtml");
		} catch (Exception e) {
			return "";
		}
	}

	public boolean getApresentarTabelaAulaRegistradaSubstituicao() {
		return !getListaChoqueHorariaAulaAcimaLimite().isEmpty() 
				|| !getListaChoqueHorariaSala().isEmpty()
				|| !getListaChoqueHorariaAulaRegistrada().isEmpty()
				|| !getListaMensagemProgramarAulaSemChoqueHorario().isEmpty();
	}

	public List<SelectItem> getListaSelectItemFuncionarioCargo() {
		if (listaSelectItemFuncionarioCargo == null) {
			listaSelectItemFuncionarioCargo = new ArrayList<>();
		}
		return listaSelectItemFuncionarioCargo;
	}

	public void setListaSelectItemFuncionarioCargo(List<SelectItem> listaSelectItemFuncionarioCargo) {
		this.listaSelectItemFuncionarioCargo = listaSelectItemFuncionarioCargo;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}
	
	private boolean permitirProgramacaoAulaComGoogleMeet = false;
	private boolean permitirProgramacaoAulaComClassroom= false;
	private boolean permitirProgramacaoAulaComBlackboard= false;
	
	public void isValidarPermissaoParaProgramacaoAulaComGoogleMeet() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_PROGRAMACAO_AULA_COM_GOOGLE_MEET,getUsuarioLogadoClone());
			setPermitirProgramacaoAulaComGoogleMeet(true);
		} catch (Exception e) {
			setPermitirProgramacaoAulaComGoogleMeet(false);
		}
	}

	public boolean isPermitirProgramacaoAulaComGoogleMeet() {
		return permitirProgramacaoAulaComGoogleMeet;
	}

	public void setPermitirProgramacaoAulaComGoogleMeet(boolean permitirProgramacaoAulaComGoogleMeet) {
		this.permitirProgramacaoAulaComGoogleMeet = permitirProgramacaoAulaComGoogleMeet;
	}
	
	public void isValidarPermissaoParaProgramacaoAulaComClassroom() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_PROGRAMACAO_AULA_COM_CLASSROOM,getUsuarioLogadoClone());
			setPermitirProgramacaoAulaComClassroom(true);
		} catch (Exception e) {
			setPermitirProgramacaoAulaComClassroom(false);
		}
	}

	public boolean isPermitirProgramacaoAulaComClassroom() {
		return permitirProgramacaoAulaComClassroom;
	}

	public void setPermitirProgramacaoAulaComClassroom(boolean permitirProgramacaoAulaComClassroom) {
		this.permitirProgramacaoAulaComClassroom = permitirProgramacaoAulaComClassroom;
	}
	
	public void isValidarPermissaoParaProgramacaoAulaComBlackboard() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.PERMITE_PROGRAMACAO_AULA_COM_BLACKBOARD,getUsuarioLogadoClone());
			setPermitirProgramacaoAulaComBlackboard(true);
		} catch (Exception e) {
			setPermitirProgramacaoAulaComBlackboard(false);
		}
	}

	

	public boolean isPermitirProgramacaoAulaComBlackboard() {
		return permitirProgramacaoAulaComBlackboard;
	}

	public void setPermitirProgramacaoAulaComBlackboard(boolean permitirProgramacaoAulaComBlackboard) {
		this.permitirProgramacaoAulaComBlackboard = permitirProgramacaoAulaComBlackboard;
	}

	public HorarioTurmaDiaItemVO getHorarioTurmaDiaItemSelecionadoVO() {
		if (horarioTurmaDiaItemSelecionadoVO == null) {
			horarioTurmaDiaItemSelecionadoVO = new HorarioTurmaDiaItemVO();
		}
		return horarioTurmaDiaItemSelecionadoVO;
	}

	public void setHorarioTurmaDiaItemSelecionadoVO(HorarioTurmaDiaItemVO horarioTurmaDiaItemSelecionadoVO) {
		this.horarioTurmaDiaItemSelecionadoVO = horarioTurmaDiaItemSelecionadoVO;
	}

	public boolean isOperacaoExcluirHorarioTurmaDoProfessor() {		
		return operacaoExcluirHorarioTurmaDoProfessor;
	}

	public void setOperacaoExcluirHorarioTurmaDoProfessor(boolean operacaoExcluirHorarioTurmaDoProfessor) {
		this.operacaoExcluirHorarioTurmaDoProfessor = operacaoExcluirHorarioTurmaDoProfessor;
	}
	
	
	public String getModalRegistrarAulaAutomaticamente() {
		if (modalRegistrarAulaAutomaticamente == null) {
			modalRegistrarAulaAutomaticamente = "";
		}
		return modalRegistrarAulaAutomaticamente;
	}

	public void setModalRegistrarAulaAutomaticamente(String modalRegistrarAulaAutomaticamente) {
		this.modalRegistrarAulaAutomaticamente = modalRegistrarAulaAutomaticamente;
	}

	public void selecionarHorarioTurmaDisciplinaProgramadaRegistroAutomatico() {
		try {
			editarHorarioTurmaDisciplinaProgramadaVO();
			if (getHorarioTurmaVO() == null || (getHorarioTurmaVO() != null && !Uteis.isAtributoPreenchido(getHorarioTurmaVO()))) {
				throw new Exception("Não foi encontrado um Horário do professor");
			}
			getHorarioTurmaDisciplinaProgramadaVO().setHorarioTurmaVO(getHorarioTurmaVO());
			if (getHorarioTurmaDisciplinaProgramadaVO().getRegistrarAulaAutomaticamente()) {
				setModalRegistrarAulaAutomaticamente("RichFaces.$('panelRegistrarAulaAutomaticamente').show();");
			} else {
				setModalRegistrarAulaAutomaticamente("");
				gravarRegistroAulaAutomatico();
			}
		} catch (Exception e) {
			setModalRegistrarAulaAutomaticamente("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}
	
	public void gravarRegistroAulaAutomatico() {
		try {
			getFacadeFactory().getHorarioTurmaDisciplinaProgramadaFacade().persistir(getHorarioTurmaDisciplinaProgramadaVO(), getUsuarioLogado());
		} catch (Exception e) {
			getHorarioTurmaDisciplinaProgramadaVO().setRegistrarAulaAutomaticamente(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public String getAcaoModalEnviarEmailAlunosTurmaPadrao() {
 		if(acaoModalEnviarEmailAlunosTurmaPadrao == null) {
			acaoModalEnviarEmailAlunosTurmaPadrao = "";
		}
		return acaoModalEnviarEmailAlunosTurmaPadrao;
	}

	public void setAcaoModalEnviarEmailAlunosTurmaPadrao(String acaoModalEnviarEmailAlunosTurmaPadrao) {
		this.acaoModalEnviarEmailAlunosTurmaPadrao = acaoModalEnviarEmailAlunosTurmaPadrao;
	}

	public Boolean getEnviarProfessor() {
		if(enviarProfessor == null) {
			enviarProfessor = true ;
		}
		return enviarProfessor;
	}

	public void setEnviarProfessor(Boolean enviarProfessor) {
		this.enviarProfessor = enviarProfessor;
	}

	public Boolean getEnviarCoordenador() {
		if(enviarCoordenador == null ) {
			enviarCoordenador = true ;
		}
		return enviarCoordenador;
	}

	public void setEnviarCoordenador(Boolean enviarCoordenador) {
		this.enviarCoordenador = enviarCoordenador;
	}

	public Boolean getEnviarReponsavelUnidade() {
		if(enviarReponsavelUnidade == null ) {
			enviarReponsavelUnidade = true ;
		}
		return enviarReponsavelUnidade;
	}

	public void setEnviarReponsavelUnidade(Boolean enviarReponsavelUnidade) {
		this.enviarReponsavelUnidade = enviarReponsavelUnidade;
	}

	public Boolean getEnviarUsuarioEspecifico() {
		if(enviarUsuarioEspecifico == null ) {
			enviarUsuarioEspecifico = false ;
		}
		return enviarUsuarioEspecifico;
	}

	public void setEnviarUsuarioEspecifico(Boolean enviarUsuarioEspecifico) {
		this.enviarUsuarioEspecifico = enviarUsuarioEspecifico;
	}
	
	public void inicializarListaFuncionariosAptoReceberNotificacao() {
		try {
			getListaConsultaFuncionariosAptoReceberNotificacao().clear();
			
			if(getEnviarUsuarioEspecifico().equals(Boolean.TRUE)) {			
				
			}else {
				getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados().clear();
			}
			
		} catch (Exception ex) {			
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public String getCampoConsultaFuncionario() {
		if(campoConsultaFuncionario == null) {
			campoConsultaFuncionario =""; 
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if(valorConsultaFuncionario == null ) {
			valorConsultaFuncionario ="";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionariosAptoReceberNotificacao() {
		if(listaConsultaFuncionariosAptoReceberNotificacao == null) {
			listaConsultaFuncionariosAptoReceberNotificacao = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionariosAptoReceberNotificacao;
	}

	public void setListaConsultaFuncionariosAptoReceberNotificacao(
			List<FuncionarioVO> listaConsultaFuncionariosAptoReceberNotificacao) {
		this.listaConsultaFuncionariosAptoReceberNotificacao = listaConsultaFuncionariosAptoReceberNotificacao;
	}
	
	public void  limparCamposPainelNotificarAlunos() {		
		try {
			setEnviarCoordenador(null);
			setEnviarProfessor(null);
			setEnviarReponsavelUnidade(null);
			setEnviarUsuarioEspecifico(null);
			inicializarListaFuncionariosAptoReceberNotificacao();
		} catch (Exception ex) {			
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarFuncionarioAptoReceberNotificacao() {	
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaFuncionario().equals("nome")) {
				 objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor() ,null, getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo(), true,
	                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), getHorarioTurmaVO().getTurma().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionariosAptoReceberNotificacao(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {			
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
		
	}
	
	
	public void adicionarTodosFuncionariosNotificacaoCronogramasAula() {
		try {			 
			  Iterator<FuncionarioVO> i = getListaConsultaFuncionariosAptoReceberNotificacao().iterator();
			  while (i.hasNext()) {
				  FuncionarioVO objExistente1 = (FuncionarioVO) i.next();
				  int index = 0;
				  Iterator<FuncionarioVO> i2 = getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados().iterator();
				  while (i2.hasNext()) {
					  FuncionarioVO objExistente2 = (FuncionarioVO) i2.next();
			           if (objExistente2.getCodigo().equals(objExistente1.getCodigo())) {
			               getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados().set(index, objExistente1);
			                return;
			           }
			           index++;
		      }
			  getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados().add(objExistente1);
			}
			
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	
	public void adicionarFuncionarioNotificacaoCronogramasAula() {
		
	  FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");	
	  int index = 0;
	  Iterator<FuncionarioVO> i = getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados().iterator();
	  while (i.hasNext()) {
		  FuncionarioVO objExistente = (FuncionarioVO) i.next();
            if (objExistente.getCodigo().equals(obj.getCodigo())) {
            	getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados().set(index, obj);
                return;
            }
            index++;
      }
	  getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados().add(obj);
	}
		
	
	
	public void removerFuncionariosAptoReceberNotificacaoSelecionado() {		
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");		
		int index = 0;
        Iterator<FuncionarioVO> i = getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados().iterator();
        while (i.hasNext()) {
        	FuncionarioVO objExistente = (FuncionarioVO) i.next();
            if (objExistente.getCodigo().equals(obj.getCodigo())) {
            	getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados().remove(index);
                return;
            }
            index++;
        }
	}
	
	public void confirmarFuncionariosAptoReceberNotificacaoSelecionados() {
		    getListaConsultaFuncionariosAptoReceberNotificacao().clear();
	}

	public List<FuncionarioVO> getListaConsultaFuncionariosAptoReceberNotificacaoSelecionados() {
		if(listaConsultaFuncionariosAptoReceberNotificacaoSelecionados == null) {
			listaConsultaFuncionariosAptoReceberNotificacaoSelecionados = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionariosAptoReceberNotificacaoSelecionados;
	}

	public void setListaConsultaFuncionariosAptoReceberNotificacaoSelecionados(List<FuncionarioVO> listaConsultaFuncionariosAptoReceberNotificacaoSelecionados) {
		this.listaConsultaFuncionariosAptoReceberNotificacaoSelecionados = listaConsultaFuncionariosAptoReceberNotificacaoSelecionados;
	}	
	
	
	
	public void executarMontagemHorarioTurmaDisciplina() {
		HorarioTurmaDisciplinaProgramadaVO obj = (HorarioTurmaDisciplinaProgramadaVO) context().getExternalContext().getRequestMap().get("horarioTurmaDisciplinaProgramadaItens");
		setHorarioTurmaDisciplinaProgramadaVO(obj);
	}
	
}
