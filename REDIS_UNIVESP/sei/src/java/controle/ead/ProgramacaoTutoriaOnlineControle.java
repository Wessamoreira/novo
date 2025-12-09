package controle.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEADEnum;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoTutoriaEnum;
import negocio.comuns.ead.enumeradores.SituacaoProgramacaoTutoriaOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoNivelProgramacaoTutoriaEnum;
import negocio.comuns.gsuite.ClassroomGoogleVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 * @author Victor Hugo 11/11/2014
 */
@Controller("ProgramacaoTutoriaOnlineControle")
@Scope("viewScope")
public class ProgramacaoTutoriaOnlineControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private String valorFiltroTelaForm;
	private String teste;
	private ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO;
	private ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemDisciplinas;
	private List<SelectItem> listaSelectItemNivelProgramacaoTutoria;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List listaConsultaProfessor;
	private Integer ordemPrioridade;
	private Integer qtdeTutores;
	private List listaNivelEducacional;
	private Boolean selecionarMatricula;
	private List<SelectItem> listaSelectItemProfessoresAtivos;
	private Integer codigoTutor;
	private String filtrarNomeAlunoAtivo;
	private String filtrarMatriculaAlunoAtivo;
	private String filtrarTurmaAlunoAtivo;
	private String filtrarCursoAlunoAtivo;
	private String filtrarUnidadeEnsinoAlunoAtivo;
	private String filtrarAnoSemestreAlunoAtivo;
	private Boolean apresentarDefinirPeriodoAulaOnline;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaAlunosSemTutor;
	private SalaAulaBlackboardVO salaAulaBlackboardVO;
	private boolean permitirProgramacaoTutoriaOnlineComSalaAulaBlackboard= false;
	private ClassroomGoogleVO classroomGoogleVO;
	private boolean permitirProgramacaoTutoriaOnlineComClassroom= false;
	private String ano;
	private String semestre;
	 

	@PostConstruct
	public void init() {
		setValorFiltroTelaForm("");
		programacaoTutoriaOnlineVO = new ProgramacaoTutoriaOnlineVO();
		limparMensagem();
		limparDadosCurso();
		limparDadosDisciplina();
		limparDadosTurma();
		setOrdemPrioridade(0);
		setApresentarDefinirPeriodoAulaOnline(false);
		getListaConsulta().clear();
		montarListaSelectItemNivelProgramacaoTutoria();
		montarListaSelectItemUnidadeEnsino();
		isValidarPermissaoParaProgramacaoAulaComClassroom();
		isValidarPermissaoParaProgramacaoAulaComSalaAulaBlackboard();
		setListaAlunosSemTutor(new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>());
		setMensagemID("msg_dados_parametroConsulta", Uteis.ALERTA);
	}

	public String persistir() {
		try {
			getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().persistir(getProgramacaoTutoriaOnlineVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoTutoriaOnlineForm.xhtml");
	}

	public void ativar() {
		try {
			getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().ativarProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getUsuarioLogado());
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativar() {
		try {			
			getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().desativarProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getUsuarioLogado());
			setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativarTutor() {
		try {
			ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO = (ProgramacaoTutoriaOnlineProfessorVO) context().getExternalContext().getRequestMap().get("tutor");
			programacaoTutoriaOnlineProfessorVO.setSituacaoProgramacaoTutoriaOnline(SituacaoProgramacaoTutoriaOnlineEnum.ATIVO);
			getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().persistir(programacaoTutoriaOnlineProfessorVO, false, getUsuarioLogado());
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativarTutor() {
		try {
			ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO = (ProgramacaoTutoriaOnlineProfessorVO) context().getExternalContext().getRequestMap().get("tutor");
//			if (programacaoTutoriaOnlineProfessorVO.getQtdeAlunosAtivos() > 0) {
//				throw new Exception(UteisJSF.internacionalizar("msg_ProgramacaoTutoriaOnline_validacaoDesativacaoTutor"));
//			}
			programacaoTutoriaOnlineProfessorVO.setSituacaoProgramacaoTutoriaOnline(SituacaoProgramacaoTutoriaOnlineEnum.INATIVO);
			getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().persistir(programacaoTutoriaOnlineProfessorVO, false, getUsuarioLogado());
			setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public String voltar() {
		limparMensagem();
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsulta().setValorConsulta("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoTutoriaOnlineCons");
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public String novo() {
		this.init();		
		setMensagemID("msg_entre_dados", Uteis.ALERTA);

		return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoTutoriaOnlineForm.xhtml");
	}

	public String editar() {
		try {
			ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO = (ProgramacaoTutoriaOnlineVO) context().getExternalContext().getRequestMap().get("programacao");
			setProgramacaoTutoriaOnlineVO(getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().consultarPorChavePrimaria(programacaoTutoriaOnlineVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));			
			montarListaSelectItemUnidadeEnsino();
			if (getIsApresentarCampoTurma()) {
				montarListaDisciplinasTurma();
			} else if (getIsApresentarCampoCurso()) {
				montarListaDisciplinasGradeCurso();
			}
			setQtdeTutores(getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs().size());
			verificarProgramacaoAulaEmodalidade();
//			consultarAlunosSemTutor();
			isValidarPermissaoParaProgramacaoAulaComClassroom();
			isValidarPermissaoParaProgramacaoAulaComSalaAulaBlackboard();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoTutoriaOnlineForm");
	}

	public void montarObjetosPorValorFiltroCampoSelecionado() {
		limparDadosCurso();
		limparDadosDisciplina();
		limparDadosTurma();
		getListaNivelEducacional().clear();
		getListaSelectItemDisciplinas().clear();
		limparDadosUnidadeEnsino();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getProgramacaoTutoriaOnlineVO().getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
			setMensagemID("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));

			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		return lista;
	}

	public void consultarTurma() {
		try {
			if (getControleConsulta().getCampoConsulta().equals("identificadorTurma")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getControleConsulta().getValorConsulta(), getProgramacaoTutoriaOnlineVO().getUnidadeEnsinoVO().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeTurno")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getControleConsulta().getValorConsulta(), getProgramacaoTutoriaOnlineVO().getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getControleConsulta().getValorConsulta(), getProgramacaoTutoriaOnlineVO().getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getControleConsulta().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarTurma() throws Exception {
		try {
			getProgramacaoTutoriaOnlineVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getProgramacaoTutoriaOnlineVO().getTurmaVO().getIdentificadorTurma(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getProgramacaoTutoriaOnlineVO().setCursoVO((CursoVO)getProgramacaoTutoriaOnlineVO().getTurmaVO().getCurso().clone());
			getProgramacaoTutoriaOnlineVO().setNivelEducacional(getProgramacaoTutoriaOnlineVO().getTurmaVO().getCurso().getNivelEducacional());
			getProgramacaoTutoriaOnlineVO().setUnidadeEnsinoVO((UnidadeEnsinoVO)getProgramacaoTutoriaOnlineVO().getTurmaVO().getUnidadeEnsino().clone());
			montarListaDisciplinasTurma();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			removerObjetoMemoria(getProgramacaoTutoriaOnlineVO().getTurmaVO());
			getProgramacaoTutoriaOnlineVO().setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} 
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), 0, getUsuarioLogado());
			getProgramacaoTutoriaOnlineVO().setTurmaVO(obj);
			getProgramacaoTutoriaOnlineVO().setCursoVO((CursoVO)obj.getCurso().clone());
			getProgramacaoTutoriaOnlineVO().setNivelEducacional(obj.getCurso().getNivelEducacional());
			getProgramacaoTutoriaOnlineVO().setUnidadeEnsinoVO((UnidadeEnsinoVO)obj.getUnidadeEnsino().clone());
			montarListaDisciplinasTurma();
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDadosTurma() {
		getProgramacaoTutoriaOnlineVO().setTurmaVO(new TurmaVO());
		getListaSelectItemDisciplinas().clear();
	}

	public void limparDadosCurso() {
		getProgramacaoTutoriaOnlineVO().setCursoVO(new CursoVO());
		getListaSelectItemDisciplinas().clear();
	}
	
	public void limparDadosTurmaIntegracao() {
		try {
			getSalaAulaBlackboardVO().setTurmaVO(new TurmaVO());	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
		
	}
	
	public void limparDadosCursoIntegracao() {
		try {
			getSalaAulaBlackboardVO().setCursoVO(new CursoVO());	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}

	public void limparDadosUnidadeEnsino() {
		getProgramacaoTutoriaOnlineVO().setUnidadeEnsinoVO(new UnidadeEnsinoVO());
		limparDadosDisciplina();
	}

	public void montarListaDisciplinasTurma() {
		try {
			List resultado = consultarDisciplina(null, getProgramacaoTutoriaOnlineVO().getTurmaVO(), null, null);
			setListaSelectItemDisciplinas(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome", true));
		} catch (Exception e) {
			setListaSelectItemDisciplinas(new ArrayList(0));
		}
	}

	public List consultarDisciplina(CursoVO cursoVO, TurmaVO turmaVO, UnidadeEnsinoVO unidadeEnsinoVO, String nivelEducacional) throws Exception {
		if (cursoVO != null) {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorCurso(getProgramacaoTutoriaOnlineVO().getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} else if (turmaVO != null) {
			return getFacadeFactory().getDisciplinaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(turmaVO.getCodigo(),false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		}
		return new ArrayList<List>();
	}

	public void consultarDadosCurso() {
		try {
			super.consultar();
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, Boolean.TRUE, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nrRegistroInterno")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNrRegistroInterno(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeAreaConhecimento")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAreaConhecimento(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nivelEducacional")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNrNivelEducacional(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				getControleConsulta().setListaConsulta(getFacadeFactory().getCursoFacade().consultaRapidaPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), 0, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			getProgramacaoTutoriaOnlineVO().setCursoVO(obj);
			montarListaDisciplinasGradeCurso();
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaDisciplinasGradeCurso() {
		try {
			List resultado = consultarDisciplina(getProgramacaoTutoriaOnlineVO().getCursoVO(), null, null, "");
			setListaSelectItemDisciplinas(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome", true));
		} catch (Exception e) {
			setListaSelectItemDisciplinas(new ArrayList(0));
		}
	}

	public void selecionarProfessor() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professor");
			setProgramacaoTutoriaOnlineProfessorVO(new ProgramacaoTutoriaOnlineProfessorVO());
			Integer ordem = 0;
			for (ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO : getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs()) {
				if (programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo().equals(obj.getCodigo())) {
					throw new Exception(UteisJSF.internacionalizar("msg_TutorJaIncluido"));
				}
				ordem++;
			}
//			if (getProgramacaoTutoriaOnlineVO().getQtdeAlunos() == 0) {
//				throw new Exception(UteisJSF.internacionalizar("msg_DigiteAQuantidadeDeAlunosPorTutor"));
//			}
			setOrdemPrioridade(ordem + 1);
			getProgramacaoTutoriaOnlineProfessorVO().setProfessor(obj);			
			getProgramacaoTutoriaOnlineProfessorVO().setOrdemPrioridade(getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs().size() + 1);
//			getProgramacaoTutoriaOnlineProfessorVO().setQtdeAlunosTutoria(getProgramacaoTutoriaOnlineVO().getQtdeAlunos());
//			getProgramacaoTutoriaOnlineProfessorVO().setQtdeAlunosAtivos(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarQtdAlunosAtivoPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getProgramacaoTutoriaOnlineProfessorVO()));
			if(Uteis.isAtributoPreenchido(getProgramacaoTutoriaOnlineVO().getCodigo())){
				getProgramacaoTutoriaOnlineProfessorVO().setProgramacaoTutoriaOnlineVO(getProgramacaoTutoriaOnlineVO());
				getProgramacaoTutoriaOnlineProfessorVO().setSituacaoProgramacaoTutoriaOnline(SituacaoProgramacaoTutoriaOnlineEnum.ATIVO);
				getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().incluir(getProgramacaoTutoriaOnlineProfessorVO(), false, getUsuarioLogado());
			}
			getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs().add(getProgramacaoTutoriaOnlineProfessorVO());
			setQtdeTutores(getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs().size());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarProfessor() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
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
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarDisciplina() {
		try {
			List objs = new ArrayList(0);
			if (getControleConsulta().getValorConsulta().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem");
			getProgramacaoTutoriaOnlineVO().setDisciplinaVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosDisciplina() {
		getProgramacaoTutoriaOnlineVO().setDisciplinaVO(new DisciplinaVO());
	}

	public void subirTutor() {
		try {
			ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO = (ProgramacaoTutoriaOnlineProfessorVO) context().getExternalContext().getRequestMap().get("tutor");
			if (programacaoTutoriaOnlineProfessorVO.getOrdemPrioridade() > 1) {
				ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO2 = getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs().get(programacaoTutoriaOnlineProfessorVO.getOrdemPrioridade() - 2);
				getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().alterarOrdemPrioridadeTutor(getProgramacaoTutoriaOnlineVO(), programacaoTutoriaOnlineProfessorVO, programacaoTutoriaOnlineProfessorVO2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerTutor() {
		try {
			ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO = (ProgramacaoTutoriaOnlineProfessorVO) context().getExternalContext().getRequestMap().get("tutor");
			if (getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs().size() >= programacaoTutoriaOnlineProfessorVO.getOrdemPrioridade()) {
				ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO2 = getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs().get(programacaoTutoriaOnlineProfessorVO.getOrdemPrioridade());
				getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().alterarOrdemPrioridadeTutor(getProgramacaoTutoriaOnlineVO(), programacaoTutoriaOnlineProfessorVO, programacaoTutoriaOnlineProfessorVO2);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerTutor() {
		try {
			ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO = (ProgramacaoTutoriaOnlineProfessorVO) context().getExternalContext().getRequestMap().get("tutor");
//			if (programacaoTutoriaOnlineProfessorVO.getQtdeAlunosAtivos() > 0 || programacaoTutoriaOnlineProfessorVO.getQtdeAlunosInativos() > 0) {
//				throw new Exception("Não Foi Possível Excluir o Tutor, É Necessário Desvincular os Alunos Ativos/Inativos do Tutor Antes de Prosseguir.");
//			}
			getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().removerTutor(getProgramacaoTutoriaOnlineVO(), programacaoTutoriaOnlineProfessorVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemNivelEducacional() {
		List<SelectItem> opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
		listaNivelEducacional =  new ArrayList<SelectItem>(0);
		for (SelectItem item : opcoes) {
			if (!item.getValue().equals("SE")) {
				listaNivelEducacional.add(item);
			}
		}
	}

	public void montarListaSelectItemNivelProgramacaoTutoria() {
		setListaSelectItemNivelProgramacaoTutoria(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelProgramacaoTutoriaEnum.class, "name", "valorApresentar", false));
	}

	public void consultarAlunosProfessor() {
		try {
			ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO = (ProgramacaoTutoriaOnlineProfessorVO) context().getExternalContext().getRequestMap().get("tutor");
			setProgramacaoTutoriaOnlineProfessorVO(programacaoTutoriaOnlineProfessorVO);
			getProgramacaoTutoriaOnlineProfessorVO().setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getProgramacaoTutoriaOnlineProfessorVO(), 0, getUsuarioLogado(), false, false));
			setSelecionarMatricula(false);
			setMensagemID("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void consultarAlunosInativosProfessor() {
		try {
			ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO = (ProgramacaoTutoriaOnlineProfessorVO) context().getExternalContext().getRequestMap().get("tutor");
			setProgramacaoTutoriaOnlineProfessorVO(programacaoTutoriaOnlineProfessorVO);
			getProgramacaoTutoriaOnlineProfessorVO().setListaAlunosInativos(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getProgramacaoTutoriaOnlineProfessorVO(), 0, getUsuarioLogado(), true, false));
			setSelecionarMatricula(false);
			setMensagemID("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTodasMatriculas() {
		ForMat:
		for (MatriculaPeriodoTurmaDisciplinaVO object : getProgramacaoTutoriaOnlineProfessorVO().getMatriculaPeriodoTurmaDisciplinaVOs()) {			
			if((Uteis.isAtributoPreenchido(getFiltrarAnoSemestreAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getApresentarAnoSemestre())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarAnoSemestreAlunoAtivo().trim().toUpperCase())))) ||
			   (Uteis.isAtributoPreenchido(getFiltrarCursoAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getCurso().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarCursoAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarMatriculaAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getMatricula())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarMatriculaAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarNomeAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getAluno().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarNomeAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarTurmaAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getTurma().getIdentificadorTurma())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarTurmaAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarUnidadeEnsinoAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getUnidadeEnsino().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarUnidadeEnsinoAlunoAtivo()).trim().toUpperCase())))){
				continue ForMat;
			}
			object.setSelecionarMatricula(getSelecionarMatricula());
		}
	}
	
	public void selecionarTodasMatriculasInativas() {
		ForMat:
		for (MatriculaPeriodoTurmaDisciplinaVO object : getProgramacaoTutoriaOnlineProfessorVO().getListaAlunosInativos()) {			
			if((Uteis.isAtributoPreenchido(getFiltrarAnoSemestreAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getApresentarAnoSemestre())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarAnoSemestreAlunoAtivo().trim().toUpperCase())))) ||
			   (Uteis.isAtributoPreenchido(getFiltrarCursoAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getCurso().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarCursoAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarMatriculaAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getMatricula())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarMatriculaAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarNomeAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getAluno().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarNomeAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarTurmaAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getTurma().getIdentificadorTurma())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarTurmaAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarUnidadeEnsinoAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getUnidadeEnsino().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarUnidadeEnsinoAlunoAtivo()).trim().toUpperCase())))){
				continue ForMat;
			}
			object.setSelecionarMatricula(getSelecionarMatricula());
		}
	}
	
	public void selecionarTodasMatriculasSemTutor() {
		ForMat:
		for (MatriculaPeriodoTurmaDisciplinaVO object : getListaAlunosSemTutor()) {			
			if((Uteis.isAtributoPreenchido(getFiltrarAnoSemestreAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getApresentarAnoSemestre())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarAnoSemestreAlunoAtivo().trim().toUpperCase())))) ||
			   (Uteis.isAtributoPreenchido(getFiltrarCursoAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getCurso().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarCursoAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarMatriculaAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getMatricula())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarMatriculaAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarNomeAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getAluno().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarNomeAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarTurmaAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getTurma().getIdentificadorTurma())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarTurmaAlunoAtivo().trim().toUpperCase())))) || 
			   (Uteis.isAtributoPreenchido(getFiltrarUnidadeEnsinoAlunoAtivo()) && !Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(object.getMatriculaObjetoVO().getUnidadeEnsino().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarUnidadeEnsinoAlunoAtivo()).trim().toUpperCase())))){
				continue ForMat;
			}
			object.setSelecionarMatricula(getSelecionarMatricula());
		}
	}

	public void montarListaProfessoresAtivos(Boolean origemPanelAlunosSemTutor) {
		try {
			getListaSelectItemProfessoresAtivos().clear();
			for (ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO : getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs()) {
				if ((!programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo().equals(getProgramacaoTutoriaOnlineProfessorVO().getProfessor().getCodigo()) || origemPanelAlunosSemTutor) && programacaoTutoriaOnlineProfessorVO.isQtdAlunoTutoriaMaiorQueAlunoAtivos() && programacaoTutoriaOnlineProfessorVO.getSituacaoProgramacaoTutoriaOnline().isSituacaoAtivo()){
					getListaSelectItemProfessoresAtivos().add(new SelectItem(programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo(), programacaoTutoriaOnlineProfessorVO.getProfessor().getNome()));
				}	
			}
			setMensagemID("", "");
			setMensagem("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarParaTutor(Boolean origemPanelAlunosSemTutor) {
		montarListaProfessoresAtivos(origemPanelAlunosSemTutor);
	}

	public void atualizarProgramacaoTutoriaOnlineProfessor() {
		try {
			ProgramacaoTutoriaOnlineProfessorVO obj = (ProgramacaoTutoriaOnlineProfessorVO) context().getExternalContext().getRequestMap().get("tutor");
			if(Uteis.isAtributoPreenchido(obj.getCodigo())){
				getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().atutalizarQtdAlunosTutoriaProgramacaoTutoriaOnlineProfessor(obj, false, getUsuarioLogado());
				setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gravarAlteracaoTutor() {
		try {
			List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaAlterarTutor = getProgramacaoTutoriaOnlineProfessorVO().getMatriculaPeriodoTurmaDisciplinaVOs().stream().filter(MatriculaPeriodoTurmaDisciplinaVO::getSelecionarMatricula).collect(Collectors.toList());
			if (Uteis.isAtributoPreenchido(listaMatriculaPeriodoTurmaDisciplinaAlterarTutor)) {
				getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().realizarAlteracaoTutor(getProgramacaoTutoriaOnlineVO(), getCodigoTutor(), listaMatriculaPeriodoTurmaDisciplinaAlterarTutor, getUsuarioLogado());
				getProgramacaoTutoriaOnlineVO().setProgramacaoTutoriaOnlineProfessorVOs(getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getProgramacaoTutoriaOnlineProfessorVO().setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getProgramacaoTutoriaOnlineProfessorVO(), 0, getUsuarioLogado(), false, false));
				consultarAlunosSemTutor();
				setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			} else {
				setMensagemDetalhada("msg_erro", "Nenhuma matrícula selecionada.", Uteis.ERRO);
			}
		} catch (Exception e) {
			//Atualizar Lista de alunos professor
			try {
				getProgramacaoTutoriaOnlineVO().setProgramacaoTutoriaOnlineProfessorVOs(getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getProgramacaoTutoriaOnlineProfessorVO().setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getProgramacaoTutoriaOnlineProfessorVO(), 0, getUsuarioLogado(), false, false));
			} catch (Exception e2) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void gravarAlteracaoTutorAlunosSemTutor() {
		try {
			List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaAlterarTutor = getListaAlunosSemTutor().stream().filter(MatriculaPeriodoTurmaDisciplinaVO::getSelecionarMatricula).collect(Collectors.toList());
			if (Uteis.isAtributoPreenchido(listaMatriculaPeriodoTurmaDisciplinaAlterarTutor)) {
				getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().realizarAlteracaoTutor(getProgramacaoTutoriaOnlineVO(), getCodigoTutor(), listaMatriculaPeriodoTurmaDisciplinaAlterarTutor, getUsuarioLogado());
				getProgramacaoTutoriaOnlineVO().setProgramacaoTutoriaOnlineProfessorVOs(getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getProgramacaoTutoriaOnlineProfessorVO().setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getProgramacaoTutoriaOnlineProfessorVO(),  0, getUsuarioLogado(), false, false));
				consultarAlunosSemTutor();
				setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			} else {
				setMensagemDetalhada("msg_erro", "Nenhuma matrícula selecionada.", Uteis.ERRO);
			}
		} catch (Exception e) {
			//Atualizar Lista de alunos professor
			try {
				getProgramacaoTutoriaOnlineVO().setProgramacaoTutoriaOnlineProfessorVOs(getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getProgramacaoTutoriaOnlineProfessorVO().setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getProgramacaoTutoriaOnlineProfessorVO(), 0, getUsuarioLogado(), false, false));
			} catch (Exception e2) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void redistruibuirAlunos() {
		try {
			getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().realizarRedistruibuicaoAlunosEntreTutoresAtivos(getProgramacaoTutoriaOnlineVO(), getProgramacaoTutoriaOnlineProfessorVO(), getUsuarioLogado());
			getProgramacaoTutoriaOnlineVO().setProgramacaoTutoriaOnlineProfessorVOs(getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			//Atualizar Lista de alunos professor
			try {
				getProgramacaoTutoriaOnlineVO().setProgramacaoTutoriaOnlineProfessorVOs(getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getProgramacaoTutoriaOnlineProfessorVO().setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getProgramacaoTutoriaOnlineProfessorVO(), 0, getUsuarioLogado(), false, false));
			} catch (Exception e2) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public boolean filtroNomeAlunoAtivo(Object obj){
		if (Uteis.isAtributoPreenchido(getFiltrarNomeAlunoAtivo())) {
			if (obj instanceof MatriculaPeriodoTurmaDisciplinaVO) {
				MatriculaPeriodoTurmaDisciplinaVO mat = ((MatriculaPeriodoTurmaDisciplinaVO) obj);
				if((Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(mat.getMatriculaObjetoVO().getAluno().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarNomeAlunoAtivo().trim().toUpperCase()))))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public boolean filtroAnoSemestreAlunoAtivo(Object obj){
		if (Uteis.isAtributoPreenchido(getFiltrarAnoSemestreAlunoAtivo())) {
			if (obj instanceof MatriculaPeriodoTurmaDisciplinaVO) {
				MatriculaPeriodoTurmaDisciplinaVO mat = ((MatriculaPeriodoTurmaDisciplinaVO) obj);
				if((Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(mat.getApresentarAnoSemestre())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarAnoSemestreAlunoAtivo().trim().toUpperCase()))))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public boolean filtroCursoAlunoAtivo(Object obj){
		if (Uteis.isAtributoPreenchido(getFiltrarCursoAlunoAtivo())) {
			if (obj instanceof MatriculaPeriodoTurmaDisciplinaVO) {
				MatriculaPeriodoTurmaDisciplinaVO mat = ((MatriculaPeriodoTurmaDisciplinaVO) obj);
				if((Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(mat.getMatriculaObjetoVO().getCurso().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarCursoAlunoAtivo().trim().toUpperCase()))))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public boolean filtroMatriculaAlunoAtivo(Object obj){
		if (Uteis.isAtributoPreenchido(getFiltrarMatriculaAlunoAtivo())) {
			if (obj instanceof MatriculaPeriodoTurmaDisciplinaVO) {
				MatriculaPeriodoTurmaDisciplinaVO mat = ((MatriculaPeriodoTurmaDisciplinaVO) obj);
				if((Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(mat.getMatriculaObjetoVO().getMatricula())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarMatriculaAlunoAtivo().trim().toUpperCase()))))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public boolean filtroTurmaAlunoAtivo(Object obj){
		if (Uteis.isAtributoPreenchido(getFiltrarTurmaAlunoAtivo())) {
			if (obj instanceof MatriculaPeriodoTurmaDisciplinaVO) {
				MatriculaPeriodoTurmaDisciplinaVO mat = ((MatriculaPeriodoTurmaDisciplinaVO) obj);
				if((Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(mat.getTurma().getIdentificadorTurma())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarTurmaAlunoAtivo().trim().toUpperCase()))))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public boolean filtroUnidadeEnsinoAlunoAtivo(Object obj){
		if (Uteis.isAtributoPreenchido(getFiltrarUnidadeEnsinoAlunoAtivo())) {
			if (obj instanceof MatriculaPeriodoTurmaDisciplinaVO) {
				MatriculaPeriodoTurmaDisciplinaVO mat = ((MatriculaPeriodoTurmaDisciplinaVO) obj);
				if((Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(mat.getMatriculaObjetoVO().getUnidadeEnsino().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltrarUnidadeEnsinoAlunoAtivo().trim().toUpperCase()))))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public void realizarInicializacaoSalaAulaBlackboard() {
		try {
			getProgramacaoTutoriaOnlineVO().setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getProgramacaoTutoriaOnlineVO().getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone()));
			getProgramacaoTutoriaOnlineVO().setListaSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardEad(TipoSalaAulaBlackboardEnum.DISCIPLINA, getProgramacaoTutoriaOnlineVO().getUnidadeEnsinoVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getCursoVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getNivelEducacional(), getProgramacaoTutoriaOnlineVO().getTurmaVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getDisciplinaVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getAno(), getProgramacaoTutoriaOnlineVO().getSemestre(), null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
			setSalaAulaBlackboardVO(new SalaAulaBlackboardVO());
			getSalaAulaBlackboardVO().setDisciplinaVO(getProgramacaoTutoriaOnlineVO().getDisciplinaVO());
			if(getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().isCurso()) {
				getSalaAulaBlackboardVO().setCursoVO(getProgramacaoTutoriaOnlineVO().getCursoVO());
				getSalaAulaBlackboardVO().setAno(getProgramacaoTutoriaOnlineVO().getAno());
				getSalaAulaBlackboardVO().setSemestre(getProgramacaoTutoriaOnlineVO().getSemestre());
			}else if(getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().isTurma()) {
				getSalaAulaBlackboardVO().setTurmaVO(getProgramacaoTutoriaOnlineVO().getTurmaVO());
				getSalaAulaBlackboardVO().setAno(getProgramacaoTutoriaOnlineVO().getAno());
				getSalaAulaBlackboardVO().setSemestre(getProgramacaoTutoriaOnlineVO().getSemestre());
			}else {
				getSalaAulaBlackboardVO().setAno(Uteis.getAnoDataAtual());
				getSalaAulaBlackboardVO().setSemestre(Uteis.getSemestreAtual());
			}
			inicializarMensagemVazia();
			montarListaSelectItemBimestre();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarGeracaoSalaAulaBlackboard() {
		try {
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboardPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getSalaAulaBlackboardVO(), getUsuarioLogadoClone());
			getProgramacaoTutoriaOnlineVO().setListaSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardEad(TipoSalaAulaBlackboardEnum.DISCIPLINA, getProgramacaoTutoriaOnlineVO().getUnidadeEnsinoVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getCursoVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getNivelEducacional(), getProgramacaoTutoriaOnlineVO().getTurmaVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getDisciplinaVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getAno(), getProgramacaoTutoriaOnlineVO().getSemestre(), null,  getProgramacaoTutoriaOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
			realizarLimpezaCamposSalaAulaBlackboard();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarBuscaAlunoBlackboard() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap().get("blackboardItem");
			setSalaAulaBlackboardVO((SalaAulaBlackboardVO) Uteis.clonar(obj));
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarBuscaAlunoSalaAulaBlackboard(getSalaAulaBlackboardVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarEnvioConviteAlunoBlackboard() {
		try {
			SalaAulaBlackboardPessoaVO obj = (SalaAulaBlackboardPessoaVO) context().getExternalContext().getRequestMap().get("salaAulaBlackboardPessoaItens");
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarEnvioConviteAlunoSalaAulaBlackboard(getSalaAulaBlackboardVO(), obj, getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void excluirSalaAulaBlackboard() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap().get("blackboardItem");
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarExclusaoSalaAulaBlackboard(obj, getUsuarioLogadoClone());
			getProgramacaoTutoriaOnlineVO().getListaSalaAulaBlackboardVO().removeIf(p-> p.getCodigo().equals(obj.getCodigo()));
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarCargaProgramacaoTutoriaOnlineBlackboardAutomatico()  {
		try {
			List<Integer> bimestres = getFacadeFactory().getDisciplinaFacade().consultarBimestresPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO());
			if(!bimestres.isEmpty()) {
				for(Integer bimestre: bimestres) {
					getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().executarCargaProgramacaoTutoriaOnlineSalaAulaBlackboardAutomatico(getProgramacaoTutoriaOnlineVO(), bimestre, getUsuarioLogadoClone());				
				}
			}else {
				getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().executarCargaProgramacaoTutoriaOnlineSalaAulaBlackboardAutomatico(getProgramacaoTutoriaOnlineVO(), 0, getUsuarioLogadoClone());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarLimpezaCamposSalaAulaBlackboard() {
		try {
			getSalaAulaBlackboardVO().setCodigo(0);
			getSalaAulaBlackboardVO().setTurmaVO(new TurmaVO());
			getSalaAulaBlackboardVO().setAno("");
			getSalaAulaBlackboardVO().setSemestre("");
			if(getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().isTurma()) {
				getSalaAulaBlackboardVO().setTurmaVO(getProgramacaoTutoriaOnlineVO().getTurmaVO());
				getSalaAulaBlackboardVO().setAno(getProgramacaoTutoriaOnlineVO().getAno());
				getSalaAulaBlackboardVO().setSemestre(getProgramacaoTutoriaOnlineVO().getSemestre());
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void visualizarClassroom() {
		try {
			ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO = (ProgramacaoTutoriaOnlineProfessorVO) context().getExternalContext().getRequestMap().get("tutor");
			setProgramacaoTutoriaOnlineProfessorVO(programacaoTutoriaOnlineProfessorVO);
			getProgramacaoTutoriaOnlineProfessorVO().setListaClassroomGoogleVO(getFacadeFactory().getClassroomGoogleFacade().consultarClassroomEad(getProgramacaoTutoriaOnlineVO().getUnidadeEnsinoVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getCursoVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getNivelEducacional(), getProgramacaoTutoriaOnlineVO().getTurmaVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getDisciplinaVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getAno(), getProgramacaoTutoriaOnlineVO().getSemestre(), programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS));
			setClassroomGoogleVO(new ClassroomGoogleVO());
			getClassroomGoogleVO().setProfessorEad(programacaoTutoriaOnlineProfessorVO.getProfessor());
			getClassroomGoogleVO().setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getProgramacaoTutoriaOnlineVO().getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone()));
			if(getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().isTurma()) {
				getClassroomGoogleVO().setTurmaVO(getProgramacaoTutoriaOnlineVO().getTurmaVO());
				getClassroomGoogleVO().setAno(getProgramacaoTutoriaOnlineVO().getAno());
				getClassroomGoogleVO().setSemestre(getProgramacaoTutoriaOnlineVO().getSemestre());
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarClassroom() {
		try {
			ClassroomGoogleVO obj = (ClassroomGoogleVO) context().getExternalContext().getRequestMap().get("classroomItem");
			setClassroomGoogleVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarGeracaoClassroom() {
		try {
			ClassroomGoogleVO obj = getFacadeFactory().getClassroomGoogleFacade().realizarGeracaoClassroomGooglePorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getClassroomGoogleVO(), getUsuarioLogadoClone());
			if(getProgramacaoTutoriaOnlineProfessorVO().getListaClassroomGoogleVO().stream().noneMatch(p-> p.getCodigo().equals(obj.getCodigo()))) {
				getProgramacaoTutoriaOnlineProfessorVO().getListaClassroomGoogleVO().add((ClassroomGoogleVO) Uteis.clonar(obj));	
			}
			realizarLimpezaCamposClassroom();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void excluirClassroom() {
		try {
			getFacadeFactory().getClassroomGoogleFacade().realizarExclusaoClassroomGoogle(getClassroomGoogleVO(), getUsuarioLogadoClone());
			getProgramacaoTutoriaOnlineProfessorVO().getListaClassroomGoogleVO().removeIf(p-> p.getCodigo().equals(getClassroomGoogleVO().getCodigo()));
			realizarLimpezaCamposClassroom();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarLimpezaCamposClassroom() {
		try {
			getClassroomGoogleVO().setCodigo(0);
			getClassroomGoogleVO().setTurmaVO(new TurmaVO());
			getClassroomGoogleVO().setAno("");
			getClassroomGoogleVO().setSemestre("");
			if(getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().isTurma()) {
				getClassroomGoogleVO().setTurmaVO(getProgramacaoTutoriaOnlineVO().getTurmaVO());
				getClassroomGoogleVO().setAno(getProgramacaoTutoriaOnlineVO().getAno());
				getClassroomGoogleVO().setSemestre(getProgramacaoTutoriaOnlineVO().getSemestre());
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarBuscaAlunoClassroom() {
		try {
			ClassroomGoogleVO obj = (ClassroomGoogleVO) context().getExternalContext().getRequestMap().get("classroomItem");
			setClassroomGoogleVO((ClassroomGoogleVO) Uteis.clonar(obj));
			getFacadeFactory().getClassroomGoogleFacade().realizarBuscaAlunoClassroom(getClassroomGoogleVO(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(), getUsuarioLogadoClone());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void realizarEnvioConviteAlunoClassroom() {
		try {
			PessoaGsuiteVO obj = (PessoaGsuiteVO) context().getExternalContext().getRequestMap().get("classroomStudentVOItens");
			getFacadeFactory().getClassroomGoogleFacade().realizarEnvioConviteAlunoClassroom(getClassroomGoogleVO(), obj, getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}	
	
	public void selecionarTurmaIntegracao()  {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), 0, getUsuarioLogado());
			if(Uteis.isAtributoPreenchido(getClassroomGoogleVO().getDisciplinaVO())) {
				getClassroomGoogleVO().setTurmaVO(obj);	
			}
			if(Uteis.isAtributoPreenchido(getSalaAulaBlackboardVO().getDisciplinaVO())) {
				getSalaAulaBlackboardVO().setTurmaVO(obj);	
			}
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarCursoIntegracao()  {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			getSalaAulaBlackboardVO().setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()));
			getControleConsulta().getListaConsulta().clear();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarCargaProgramacaoTutoriaOnlineClassroomAutomatico()  {
		try {
			getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().executarCargaProgramacaoTutoriaOnlineClassroomAutomatico(getProgramacaoTutoriaOnlineVO(), getUsuarioLogadoClone());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	

	// Getters and Setters
	public String getValorFiltroTelaForm() {
		if (valorFiltroTelaForm == null) {
			valorFiltroTelaForm = "";
		}
		return valorFiltroTelaForm;
	}

	public void setValorFiltroTelaForm(String valorFiltroTelaForm) {
		this.valorFiltroTelaForm = valorFiltroTelaForm;
	}

	public String getTeste() {
		if (teste == null) {
			teste = "";
		}
		return teste;
	}

	public void setTeste(String teste) {
		this.teste = teste;
	}

	public ProgramacaoTutoriaOnlineVO getProgramacaoTutoriaOnlineVO() {
		if (programacaoTutoriaOnlineVO == null) {
			programacaoTutoriaOnlineVO = new ProgramacaoTutoriaOnlineVO();
		}
		return programacaoTutoriaOnlineVO;
	}

	public void setProgramacaoTutoriaOnlineVO(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO) {
		this.programacaoTutoriaOnlineVO = programacaoTutoriaOnlineVO;
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getProgramacaoTutoriaOnlineVO().getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());				
				getProgramacaoTutoriaOnlineVO().getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemDisciplinas() {
		if (listaSelectItemDisciplinas == null) {
			listaSelectItemDisciplinas = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinas;
	}

	public void setListaSelectItemDisciplinas(List<SelectItem> listaSelectItemDisciplinas) {
		this.listaSelectItemDisciplinas = listaSelectItemDisciplinas;
	}

	public boolean getIsApresentarCampoTurma() {
		return getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.TURMA);
	}

	public boolean getIsApresentarCampoCurso() {
		return getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.CURSO);
	}

	public boolean getIsApresentarCampoNivelEducacional() {
		getListaNivelEducacional().clear();
		montarListaSelectItemNivelEducacional();
		return getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.NIVEL_EDUCACIONAL);
	}

	public boolean getIsApresentarCampoUnidadeEnsino() {
		return !getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.DISCIPLINA);
	}

	public boolean getIsApresentarCampoDisciplina() {
		return getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.CURSO) || getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.TURMA);
	}

	public boolean getIsApresentarCampoDisciplinaUnidadeEnsino() {
		return getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.UNIDADE_ENSINO) || getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.DISCIPLINA) || getProgramacaoTutoriaOnlineVO().getTipoNivelProgramacaoTutoria().equals(TipoNivelProgramacaoTutoriaEnum.NIVEL_EDUCACIONAL);
	}

	public List<SelectItem> listaSelectItemComboBoxConsultaProgramacaoTutoriaOnline;
	public List<SelectItem> getListaSelectItemComboBoxConsultaProgramacaoTutoriaOnline() {
		if(listaSelectItemComboBoxConsultaProgramacaoTutoriaOnline == null) {
			listaSelectItemComboBoxConsultaProgramacaoTutoriaOnline = new ArrayList<SelectItem>(0);
			listaSelectItemComboBoxConsultaProgramacaoTutoriaOnline.add(new SelectItem("nomeDisciplina", "Disciplina"));
			listaSelectItemComboBoxConsultaProgramacaoTutoriaOnline.add(new SelectItem("identificadorTurma", "Identificador Turma"));
			listaSelectItemComboBoxConsultaProgramacaoTutoriaOnline.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
			listaSelectItemComboBoxConsultaProgramacaoTutoriaOnline.add(new SelectItem("nivelEducacional", "Nível Educacional"));
			listaSelectItemComboBoxConsultaProgramacaoTutoriaOnline.add(new SelectItem("nomeCurso", "Curso"));		
		}

		return listaSelectItemComboBoxConsultaProgramacaoTutoriaOnline;
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

	public List<SelectItem> tipoConsultaComboCurso;
	public List<SelectItem> getTipoConsultaComboCurso() {
		if(tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
	}
		return tipoConsultaComboCurso;
	}

	public List<SelectItem> getListaSelectItemRegraDistribuicaoTutoriaEnum() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(RegraDistribuicaoTutoriaEnum.class, "name", "valorApresentar", false);
	}

	public ProgramacaoTutoriaOnlineProfessorVO getProgramacaoTutoriaOnlineProfessorVO() {
		if (programacaoTutoriaOnlineProfessorVO == null) {
			programacaoTutoriaOnlineProfessorVO = new ProgramacaoTutoriaOnlineProfessorVO();
		}
		return programacaoTutoriaOnlineProfessorVO;
	}

	public void setProgramacaoTutoriaOnlineProfessorVO(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO) {
		this.programacaoTutoriaOnlineProfessorVO = programacaoTutoriaOnlineProfessorVO;
	}

	public List<SelectItem> tipoConsultaComboProfessor;
	public List<SelectItem> getTipoConsultaComboProfessor() {
		if(tipoConsultaComboProfessor == null) {
		tipoConsultaComboProfessor = new ArrayList<SelectItem>(0);
		tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
		tipoConsultaComboProfessor.add(new SelectItem("cpf", "CPF"));
	}
		return tipoConsultaComboProfessor;
	}

	public Boolean getApresentarCampoCpf() {
		if (getCampoConsultaProfessor().equals("cpf")) {
			return true;
		}
		return false;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public List getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public Integer getOrdemPrioridade() {
		if (ordemPrioridade == null) {
			ordemPrioridade = 0;
		}
		return ordemPrioridade;
	}

	public void setOrdemPrioridade(Integer ordemPrioridade) {
		this.ordemPrioridade = ordemPrioridade;
	}

	public List<SelectItem> tipoConsultaCombo;
	public List<SelectItem> getTipoConsultaCombo() {
		if(tipoConsultaCombo == null) {
		tipoConsultaCombo = new ArrayList<SelectItem>(0);
		tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
		tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
	}
		return tipoConsultaCombo;
	}

	public Integer getQtdeTutores() {
		if (qtdeTutores == null) {
			qtdeTutores = 0;
		}
		return qtdeTutores;
	}

	public void setQtdeTutores(Integer qtdeTutores) {
		this.qtdeTutores = qtdeTutores;
	}

	public List<SelectItem> getListaSelectItemNivelProgramacaoTutoria() {
		if (listaSelectItemNivelProgramacaoTutoria == null) {
			listaSelectItemNivelProgramacaoTutoria = new ArrayList<SelectItem>();
		}
		return listaSelectItemNivelProgramacaoTutoria;
	}

	public void setListaSelectItemNivelProgramacaoTutoria(List<SelectItem> listaSelectItemNivelProgramacaoTutoria) {
		this.listaSelectItemNivelProgramacaoTutoria = listaSelectItemNivelProgramacaoTutoria;
	}

	public List<SelectItem> getListaNivelEducacional() {
		if (listaNivelEducacional == null) {
			montarListaSelectItemNivelEducacional();
		}
		return listaNivelEducacional;
	}

	public void setListaNivelEducacional(List<SelectItem> listaNivelEducacional) {
		this.listaNivelEducacional = listaNivelEducacional;
	}

	public Boolean getSelecionarMatricula() {
		if (selecionarMatricula == null) {
			selecionarMatricula = false;
		}
		return selecionarMatricula;
	}

	public void setSelecionarMatricula(Boolean selecionarMatricula) {
		this.selecionarMatricula = selecionarMatricula;
	}

	public List<SelectItem> getListaSelectItemProfessoresAtivos() {
		if (listaSelectItemProfessoresAtivos == null) {
			listaSelectItemProfessoresAtivos = new ArrayList(0);
		}
		return listaSelectItemProfessoresAtivos;
	}

	public void setListaSelectItemProfessoresAtivos(List<SelectItem> listaSelectItemProfessoresAtivos) {
		this.listaSelectItemProfessoresAtivos = listaSelectItemProfessoresAtivos;
	}
	

	public Integer getCodigoTutor() {
		return codigoTutor;
	}

	public void setCodigoTutor(Integer codigoTutor) {
		this.codigoTutor = codigoTutor;
	}
	
	public boolean getIsDesativarBotoesECampos() {
		boolean existeAlunoAtivo =false;
		for (ProgramacaoTutoriaOnlineProfessorVO obj : getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs()) {
			if (obj.getQtdeAlunosAtivos() > 0){
				existeAlunoAtivo = true;
				break;
			}
		}
		return existeAlunoAtivo;
	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultar();
	}
	
	public String getFiltrarNomeAlunoAtivo() {
		if(filtrarNomeAlunoAtivo == null){
			filtrarNomeAlunoAtivo = "";
		}
		return filtrarNomeAlunoAtivo;
	}

	public void setFiltrarNomeAlunoAtivo(String filtrarNomeAlunoAtivo) {
		this.filtrarNomeAlunoAtivo = filtrarNomeAlunoAtivo;
	}
	
	public void addFiltrarNomeAlunoAtivo(String filtrarNomeAlunoAtivo) {
		this.filtrarNomeAlunoAtivo = filtrarNomeAlunoAtivo;
	}

	public String getFiltrarMatriculaAlunoAtivo() {
		if(filtrarMatriculaAlunoAtivo == null){
			filtrarMatriculaAlunoAtivo = "";
		}
		return filtrarMatriculaAlunoAtivo;
	}

	public void setFiltrarMatriculaAlunoAtivo(String filtrarMatriculaAlunoAtivo) {
		this.filtrarMatriculaAlunoAtivo = filtrarMatriculaAlunoAtivo;
	}
	
	public void addFiltrarMatriculaAlunoAtivo(String filtrarMatriculaAlunoAtivo) {
		this.filtrarMatriculaAlunoAtivo = filtrarMatriculaAlunoAtivo;
	}

	public String getFiltrarTurmaAlunoAtivo() {
		if(filtrarTurmaAlunoAtivo == null){
			filtrarTurmaAlunoAtivo = "";
		}
		return filtrarTurmaAlunoAtivo;
	}

	public void setFiltrarTurmaAlunoAtivo(String filtrarTurmaAlunoAtivo) {
		this.filtrarTurmaAlunoAtivo = filtrarTurmaAlunoAtivo;
	}
	
	public void addFiltrarTurmaAlunoAtivo(String filtrarTurmaAlunoAtivo) {
		this.filtrarTurmaAlunoAtivo = filtrarTurmaAlunoAtivo;
	}

	public String getFiltrarCursoAlunoAtivo() {
		if(filtrarCursoAlunoAtivo == null){
			filtrarCursoAlunoAtivo = "";
		}
		return filtrarCursoAlunoAtivo;
	}

	public void setFiltrarCursoAlunoAtivo(String filtrarCursoAlunoAtivo) {
		this.filtrarCursoAlunoAtivo = filtrarCursoAlunoAtivo;
	}
	
	public void addFiltrarCursoAlunoAtivo(String filtrarCursoAlunoAtivo) {
		this.filtrarCursoAlunoAtivo = filtrarCursoAlunoAtivo;
	}

	public String getFiltrarUnidadeEnsinoAlunoAtivo() {
		if(filtrarUnidadeEnsinoAlunoAtivo == null){
			filtrarUnidadeEnsinoAlunoAtivo = "";
		}
		return filtrarUnidadeEnsinoAlunoAtivo;
	}

	public void setFiltrarUnidadeEnsinoAlunoAtivo(String filtrarUnidadeEnsinoAlunoAtivo) {
		this.filtrarUnidadeEnsinoAlunoAtivo = filtrarUnidadeEnsinoAlunoAtivo;
	}
	
	public void addFiltrarUnidadeEnsinoAlunoAtivo(String filtrarUnidadeEnsinoAlunoAtivo) {
		this.filtrarUnidadeEnsinoAlunoAtivo = filtrarUnidadeEnsinoAlunoAtivo;
	}

	public String getFiltrarAnoSemestreAlunoAtivo() {
		if(filtrarAnoSemestreAlunoAtivo == null){
			filtrarAnoSemestreAlunoAtivo = "";
		}
		return filtrarAnoSemestreAlunoAtivo;
	}

	public void setFiltrarAnoSemestreAlunoAtivo(String filtrarAnoSemestreAlunoAtivo) {
		this.filtrarAnoSemestreAlunoAtivo = filtrarAnoSemestreAlunoAtivo;
	}
	
	public void addFiltrarAnoSemestreAlunoAtivo(String filtrarAnoSemestreAlunoAtivo) {
		this.filtrarAnoSemestreAlunoAtivo = filtrarAnoSemestreAlunoAtivo;
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
    
    public void verificarProgramacaoAulaEmodalidade() throws Exception {
    	try {
    	TurmaDisciplinaVO turmaDisciplinaVO = new TurmaDisciplinaVO();
    	turmaDisciplinaVO = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurmaCodigoDisciplina(getProgramacaoTutoriaOnlineVO().getTurmaVO().getCodigo(), getProgramacaoTutoriaOnlineVO().getDisciplinaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    	if(turmaDisciplinaVO.getDefinicoesTutoriaOnline().equals(DefinicoesTutoriaOnlineEnum.DINAMICA) && turmaDisciplinaVO.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
    		setApresentarDefinirPeriodoAulaOnline(true);
    	}else {
    		setApresentarDefinirPeriodoAulaOnline(false);
    	}
    	setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
    }

	public Boolean getApresentarDefinirPeriodoAulaOnline() {
		if(apresentarDefinirPeriodoAulaOnline == null) {
			apresentarDefinirPeriodoAulaOnline = false;
		}
		return apresentarDefinirPeriodoAulaOnline;
	}

	public void setApresentarDefinirPeriodoAulaOnline(Boolean apresentarDefinirPeriodoAulaOnline) {
		this.apresentarDefinirPeriodoAulaOnline = apresentarDefinirPeriodoAulaOnline;
	}
    
    @PostConstruct
	public void cadastrarProgramacaoTutoriaOnlineVindoDaTelaDeProgramacaoAula() throws Exception {
    	try {
			if (context().getExternalContext().getSessionMap().get("turma") != null) {
				getProgramacaoTutoriaOnlineVO().setTipoNivelProgramacaoTutoria(TipoNivelProgramacaoTutoriaEnum.TURMA);
				getProgramacaoTutoriaOnlineVO().setTurmaVO((TurmaVO) context().getExternalContext().getSessionMap().get("turma"));
				montarListaDisciplinasTurma();
				getProgramacaoTutoriaOnlineVO().getDisciplinaVO().setCodigo((Integer) context().getExternalContext().getSessionMap().get("codigoDisciplina"));
				getProgramacaoTutoriaOnlineVO().getDisciplinaVO().setNome((String) context().getExternalContext().getSessionMap().get("disciplina"));
				
				Integer codigoProgramacaoTutoriaOnline = getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().programacaoTutoriaOnlinePersistidoNoBancoComParametrosPassados(getProgramacaoTutoriaOnlineVO(), getUsuarioLogado());
				if( codigoProgramacaoTutoriaOnline > 0) {
					setProgramacaoTutoriaOnlineVO(getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().consultarPorChavePrimaria(codigoProgramacaoTutoriaOnline, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					montarListaSelectItemUnidadeEnsino();
					if (getIsApresentarCampoTurma()) {
						montarListaDisciplinasTurma();
					} else if (getIsApresentarCampoCurso()) {
						montarListaDisciplinasGradeCurso();
					}
					setQtdeTutores(getProgramacaoTutoriaOnlineVO().getProgramacaoTutoriaOnlineProfessorVOs().size());
				}
				verificarProgramacaoAulaEmodalidade();
				context().getExternalContext().getSessionMap().remove("turma");
				context().getExternalContext().getSessionMap().remove("codigoDisciplina");
				context().getExternalContext().getSessionMap().remove("disciplina");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}			
	}
    
    public void limparDadosPeriodoAula() {
    	if(!getProgramacaoTutoriaOnlineVO().getDefinirPeriodoAulaOnline()) {
    		getProgramacaoTutoriaOnlineVO().setDataInicioAula(null);
    		getProgramacaoTutoriaOnlineVO().setDataTerminoAula(null);
    		getProgramacaoTutoriaOnlineVO().setAno(null);
    		getProgramacaoTutoriaOnlineVO().setSemestre(null);
    	}
    }
    
    public void desvincularAlunosInativos() {
    	try {
    	Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = getProgramacaoTutoriaOnlineProfessorVO().getListaAlunosInativos().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO object = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
    		if(object.getSelecionarMatricula()) {
    			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().desvincularProgramacaoTutoriaOnlineProfessor(object, getUsuarioLogado());
    		}
    	}
		getProgramacaoTutoriaOnlineProfessorVO().setListaAlunosInativos(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(),  getProgramacaoTutoriaOnlineProfessorVO(), 0, getUsuarioLogado(), true, false));
		getProgramacaoTutoriaOnlineVO().setProgramacaoTutoriaOnlineProfessorVOs(getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
    	} catch(Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
    public void consultarAlunosSemTutor() {
    	try {
		setListaAlunosSemTutor(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO(), getProgramacaoTutoriaOnlineProfessorVO(), 0, getUsuarioLogado(), false, true));
    	} catch(Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaAlunosSemTutor() {
		if (listaAlunosSemTutor == null) {
			listaAlunosSemTutor = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
		}
		return listaAlunosSemTutor;
	}

	public void setListaAlunosSemTutor(List<MatriculaPeriodoTurmaDisciplinaVO> listaAlunosSemTutor) {
		this.listaAlunosSemTutor = listaAlunosSemTutor;
	}
	
	
	public void isValidarPermissaoParaProgramacaoAulaComClassroom() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoEADEnum.PERMITE_PROGRAMACAO_TUTORIA_ONLINE_COM_CLASSROOM,getUsuarioLogadoClone());
			setPermitirProgramacaoTutoriaOnlineComClassroom(true);
		} catch (Exception e) {
			setPermitirProgramacaoTutoriaOnlineComClassroom(false);
		}
	}

	public boolean isPermitirProgramacaoTutoriaOnlineComClassroom() {
		return permitirProgramacaoTutoriaOnlineComClassroom;
	}

	public void setPermitirProgramacaoTutoriaOnlineComClassroom(boolean permitirProgramacaoTutoriaOnlineComClassroom) {
		this.permitirProgramacaoTutoriaOnlineComClassroom = permitirProgramacaoTutoriaOnlineComClassroom;
	}

	public ClassroomGoogleVO getClassroomGoogleVO() {
		if (classroomGoogleVO == null) {
			classroomGoogleVO = new ClassroomGoogleVO();
		}
		return classroomGoogleVO;
	}

	public void setClassroomGoogleVO(ClassroomGoogleVO classroomGoogleVO) {
		this.classroomGoogleVO = classroomGoogleVO;
	}
	
	public void isValidarPermissaoParaProgramacaoAulaComSalaAulaBlackboard() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoEADEnum.PERMITE_PROGRAMACAO_TUTORIA_ONLINE_COM_BLACKBOARD,getUsuarioLogadoClone());
			setPermitirProgramacaoTutoriaOnlineComSalaAulaBlackboard(true);
		} catch (Exception e) {
			setPermitirProgramacaoTutoriaOnlineComSalaAulaBlackboard(false);
		}
	}

	public SalaAulaBlackboardVO getSalaAulaBlackboardVO() {
		if (salaAulaBlackboardVO == null) {
			salaAulaBlackboardVO = new SalaAulaBlackboardVO();
		}
		return salaAulaBlackboardVO;
	}

	public void setSalaAulaBlackboardVO(SalaAulaBlackboardVO salaAulaBlackboardVO) {
		this.salaAulaBlackboardVO = salaAulaBlackboardVO;
	}

	public boolean isPermitirProgramacaoTutoriaOnlineComSalaAulaBlackboard() {
		return permitirProgramacaoTutoriaOnlineComSalaAulaBlackboard;
	}

	public void setPermitirProgramacaoTutoriaOnlineComSalaAulaBlackboard(boolean permitirProgramacaoTutoriaOnlineComSalaAulaBlackboard) {
		this.permitirProgramacaoTutoriaOnlineComSalaAulaBlackboard = permitirProgramacaoTutoriaOnlineComSalaAulaBlackboard;
	}
	
	public void montarListaSelectItemBimestre() {
		try {
			List<Integer> bimestres = getFacadeFactory().getDisciplinaFacade().consultarBimestresPorProgramacaoTutoriaOnline(getProgramacaoTutoriaOnlineVO());
			getListaSelectItemBimestre().clear();
			if(!bimestres.isEmpty()) {
				for(Integer bim: bimestres) {			
					getListaSelectItemBimestre().add(new SelectItem(bim, bim.toString()));
				}			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private List<SelectItem> listaSelectItemBimestre;


	public List<SelectItem> getListaSelectItemBimestre() {
		if(listaSelectItemBimestre == null) {
			listaSelectItemBimestre = new ArrayList<SelectItem>(0);
			
		}
		return listaSelectItemBimestre;
	}

	public void setListaSelectItemBimestre(List<SelectItem> listaSelectItemBimestre) {
		this.listaSelectItemBimestre = listaSelectItemBimestre;
	}
    
}
