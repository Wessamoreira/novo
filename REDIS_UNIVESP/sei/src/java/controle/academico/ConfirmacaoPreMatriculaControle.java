package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas historicoForm.jsp
 * historicoCons.jsp) com as funcionalidades da classe <code>Historico</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Historico
 * @see HistoricoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import jobs.JobExecutarSincronismoComLdapAoCancelarTransferirMatricula;
import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("ConfirmacaoPreMatriculaControle")
@Scope("viewScope")
@Lazy
public class ConfirmacaoPreMatriculaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ProcessoMatriculaVO processoMatriculaVO;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private TurnoVO turnoVO;
	private MatriculaVO matriculaVO;
	private String situacao;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemProcessoMatricula;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> listaSelectItemSituacao;
	private List<MatriculaPeriodoVO> listaAlunosPreMatriculados;
	private List<MatriculaPeriodoVO> listaMatriculaPeriodoNaoPodemSeremEfetivadas;
	private List<MatriculaPeriodoVO> listaMatriculaPeriodoNaoPodemSeremCanceladas;
	private List<MatriculaPeriodoVO> listaMatriculaPeriodoNaoPodemSeremExcluidas;
	private MatriculaPeriodoVO matriculaPeridoVO;
	private Boolean selecionarTodos;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private Boolean ocorreuPrimeiraAula;
	private String preMatricula;
	private Boolean trazerPreMatriculasCanceladas;
	private String onCompletePanelConfirmarCancelarMatricula;
	private String onCompletePanelConfirmarExcluirMatricula;

	public ConfirmacaoPreMatriculaControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
		novo();
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe para
	 * edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setUnidadeEnsinoVO(new UnidadeEnsinoVO());
		setProcessoMatriculaVO(new ProcessoMatriculaVO());
		setCursoVO(new CursoVO());
		setTurmaVO(new TurmaVO());
		setTurnoVO(new TurnoVO());
		setMatriculaVO(new MatriculaVO());
		setListaConsultaCurso(new ArrayList<CursoVO>(0));
		setListaSelectItemTurma(new ArrayList<SelectItem>(0));
		setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
		setListaSelectItemTurno(new ArrayList<SelectItem>(0));
		setListaAlunosPreMatriculados(new ArrayList<MatriculaPeriodoVO>(0));
		setListaSelectItemProcessoMatricula(new ArrayList<SelectItem>(0));
		setCampoConsultaCurso("");
		setValorConsultaCurso("");
		setSituacao("todos");
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("confirmacaoPreMatriculaForm.xhtml");
	}

	/**
	 * Método responsável por confirmar todas as pré matrículas listadas na
	 * tela. Utiliza método
	 * "executarValidacaoParaEfetivarMatriculasPeriodoPreMatriculadas" de
	 * MatriculaPeriodoFacade.
	 */
	public void executarValidacaoParaEfetivarTodasPreMatriculadas() {
		try {
			getFacadeFactory().getMatriculaPeriodoFacade().executarValidacaoParaEfetivarMatriculasPeriodoPreMatriculadas(getListaAlunosPreMatriculados(), getConfiguracaoFinanceiroPadraoSistema(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_ConfirmacaoPreMatricula_sucessoAtivacao");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getApresentarMatriculasNaoPodemSeremEfetvadas() {
		return "";
	}

	public boolean getApresentarListaMatriculasNaoPodemSeremEfetivadas() {
		return false;
	}

	public void executarEfetivacaoSomentePossiveis() {
		try {
			setListaAlunosPreMatriculados(getFacadeFactory().getMatriculaPeriodoFacade().executarEfetivacaoMatriculasPeriodoPreMatriculadas(getListaAlunosPreMatriculados(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			setMensagemID("msg_ConfirmacaoPreMatricula_sucessoAtivacao");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void ativarPreMatriculaPeriodo() {
		try {
			MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			// boolean existeOutraMatriculaPeriodoAtiva =
			// getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMatriculaPeriodoPorSituacao(obj.getMatricula(),
			// "AT");
			// if (existeOutraMatriculaPeriodoAtiva) {
			// throw new
			// Exception(UteisJSF.internacionalizar("msg_ConfirmacaoPreMatricula_outraAtiva"));
			// }
			obj = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			MatriculaPeriodoVO m = getFacadeFactory().getMatriculaPeriodoFacade().efetivarMatriculaPeriodoPreMatriculada(obj, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado(), false);
			adicionarObjMatriculaPeriodoListaAlunosMatriculados(m);
			setMensagemID("msg_ConfirmacaoPreMatricula_sucessoAtivacao");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarObjMatriculaPeriodoListaAlunosMatriculados(MatriculaPeriodoVO obj) throws Exception {
		int index = 0;
		Iterator<MatriculaPeriodoVO> i = getListaAlunosPreMatriculados().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoVO objExistente = (MatriculaPeriodoVO) i.next();
			if (objExistente.getCodigo().intValue() == obj.getCodigo().intValue()) {
				getListaAlunosPreMatriculados().set(index, obj);
				return;
			}
			index++;
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarAlunosPreMatricula() {
		try {
			List<MatriculaPeriodoVO> listaResultado = new ArrayList<MatriculaPeriodoVO>(0);
//			if (getUnidadeEnsinoVO().getCodigo() == null || getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
//				throw new Exception(getMensagemInternalizacao("msg_ConfirmacaoPreMatricula_unidadeEnsino"));
//			}
			// if (getProcessoMatriculaVO().getCodigo() == null ||
			// getProcessoMatriculaVO().getCodigo().intValue() == 0) {
			// throw new
			// Exception(getMensagemInternalizacao("msg_ConfirmacaoPreMatricula_processoMatricula"));
			// }
			if (getSituacao() == null || getSituacao().equals("")) {
				throw new Exception(getMensagemInternalizacao("msg_ConfirmacaoPreMatricula_situacao"));
			}
			listaResultado = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorUndiadeEnsinoProcessoMatriculaCursoTurnoTurmaSituacaoMatricula(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), getProcessoMatriculaVO().getCodigo(), getCursoVO().getCodigo(), getTurnoVO().getCodigo(), getTurmaVO().getCodigo(), getSituacao(), true, getPreMatricula(), getOcorreuPrimeiraAula(), getTrazerPreMatriculasCanceladas(), true, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setListaAlunosPreMatriculados(listaResultado);
			ConsistirException consistirException = new ConsistirException();
			ProcessarParalelismo.executar(0, getListaAlunosPreMatriculados().size(), consistirException, new ProcessarParalelismo.Processo() {				
				@Override
				public void run(int i) {					
					MatriculaPeriodoVO matriculaPeriodoVO = getListaAlunosPreMatriculados().get(i);
					try {
						matriculaPeriodoVO.setCalouro(!getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMaisDeUmaMatriculaPeriodo(matriculaPeriodoVO.getMatricula()));
						matriculaPeriodoVO.setSelecionarMatriculaPeriodoRenovar(false);
					} catch (Exception e) {
						matriculaPeriodoVO.setCalouro(false);
					}						
				}
			});
			setSelecionarTodos(Boolean.FALSE);
			selecionarTodosDesmarcarTodos();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaAlunosPreMatriculados(new ArrayList<MatriculaPeriodoVO>(0));
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
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false,  getUsuarioLogado());
			}

			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());

			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			if(objAluno.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			this.setMatriculaVO(objAluno);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatriculaVO(new MatriculaVO());
		}
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(new MatriculaVO());
	}

	public void selecionarAluno()  {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			MatriculaVO objCompleto;
			objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if(objCompleto.getBloqueioPorSolicitacaoLiberacaoMatricula()) {
				throw new Exception(getMensagemInternalizacao("msg_RenovarMatricula_avisoMatriculaSuspensa"));
			}
			this.setMatriculaVO(objCompleto);
			obj = null;
			objCompleto = null;
			setValorConsultaAluno("");
			setCampoConsultaAluno("");
			getListaConsultaAluno().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				montarListaProcessoMatricula();
				return;
			}

			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	@SuppressWarnings("unchecked")
	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			// if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), getCursoVO().getCodigo().intValue(), getTurnoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,"", getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), getCursoVO().getCodigo().intValue(), getTurnoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			}

			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
			// } else {
			// throw new Exception("Por Favor Informe a Unidade de Ensino.");
			// }
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getFacadeFactory().getTurmaFacade().carregarDados(obj, getUsuarioLogado());
			setUnidadeEnsinoVO(obj.getUnidadeEnsino());
			montarListaProcessoMatricula();
			setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
			montarListaSelectItemTurno();
			setTurnoVO(obj.getTurno());
			setTurmaVO(obj);
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
		} catch (Exception e) {
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
			setTurmaVO(null);
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	// public void montarListaTurma() {
	// try {
	// if (getTurnoVO().getCodigo() == null ||
	// getTurnoVO().getCodigo().intValue() == 0) {
	// setListaSelectItemTurma(new ArrayList(0));
	// return;
	// }
	// List resultadoConsulta = consultarTurmaPorCurso_Turno();
	// Iterator i = resultadoConsulta.iterator();
	// List objs = new ArrayList(0);
	// objs.add(new SelectItem(0, ""));
	// while (i.hasNext()) {
	// TurmaVO obj = (TurmaVO) i.next();
	// objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma()));
	// }
	// SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
	// Collections.sort((List) objs, ordenador);
	// setListaSelectItemTurma(objs);
	// } catch (Exception e) {
	// //System.out.println("MENSAGEM => " + e.getMessage());
	// }
	// }

	// public List consultarTurmaPorCurso_Turno() throws Exception {
	// List lista =
	// getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoCursoTurno(getUnidadeEnsinoVO().getCodigo(),
	// getCursoVO().getCodigo(), getTurnoVO().getCodigo(), false,
	// Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	// return lista;
	// }

	public void montarCurso() {
		if (getProcessoMatriculaVO().getCodigo() == null || getProcessoMatriculaVO().getCodigo().intValue() == 0) {
			setListaSelectItemTurma(new ArrayList<SelectItem>(0));
			setListaSelectItemTurno(new ArrayList<SelectItem>(0));
			setCursoVO(new CursoVO());
			return;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaProcessoMatricula() {
		try {
//			if (getUnidadeEnsinoVO().getCodigo() == null || getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
//				setListaSelectItemProcessoMatricula(new ArrayList(0));
//				setCursoVO(new CursoVO());
//				setListaSelectItemTurno(new ArrayList(0));
//				setListaSelectItemTurma(new ArrayList(0));
//				return;
//			}
			List resultadoConsulta = consultarProcessoMatriculaPorUnidadeEnsino();
			Iterator i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ProcessoMatriculaVO obj = (ProcessoMatriculaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemProcessoMatricula(objs);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void montarListaSelectItemTurno() {
		try {
			if (getCursoVO().getCodigo() == null || getCursoVO().getCodigo().intValue() == 0) {
				setListaSelectItemTurno(new ArrayList(0));
				return;
			}
			List resultadoConsulta = getCursoVO().getCursoTurnoVOs();
			Iterator i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CursoTurnoVO obj = (CursoTurnoVO) i.next();
				objs.add(new SelectItem(obj.getTurno().getCodigo(), obj.getTurno().getNome()));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemTurno(objs);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<ProcessoMatriculaVO> consultarProcessoMatriculaPorUnidadeEnsino() throws Exception {
		return getFacadeFactory().getProcessoMatriculaFacade().consultarPorNomeUnidadeEnsino_Ativo("", getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	@SuppressWarnings("unchecked")
	public List<GradeDisciplinaVO> consultarDisciplinasPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getTurmaVO().getPeridoLetivo().getCodigo(), false, getUsuarioLogado(), null);
	}

	@SuppressWarnings("unchecked")
	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					throw new Exception("Informe um código para realização da consulta!");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				if (getProcessoMatriculaVO().getCodigo().intValue() == 0) {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoProcessoMatricula(valorInt, getProcessoMatriculaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
			}
			if (getCampoConsultaCurso().equals("nome")) {
				if (getProcessoMatriculaVO().getCodigo() == null) {
					throw new Exception("Informe um Processo Matrícula!");
				}
				if (getProcessoMatriculaVO().getCodigo().intValue() == 0) {
					if (getValorConsultaCurso().length() < 2) {
						throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
					}
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, Boolean.FALSE, getUsuarioLogado());
				} else {
					if (getValorConsultaCurso().length() < 2) {
						throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
					}
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoProcessoMatricula(getValorConsultaCurso(), getProcessoMatriculaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
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
			CursoVO curso = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(curso.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
			montarListaSelectItemTurno();
			setMensagemDetalhada("");
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setValorConsultaCurso("");
			setCampoConsultaCurso("");
			getListaConsultaCurso().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosCurso() {
		setListaSelectItemTurno(new ArrayList<SelectItem>(0));
		setCursoVO(new CursoVO());
		setTurmaVO(new TurmaVO());
	}

	public void limparDadosTurma() {
		setProcessoMatriculaVO(new ProcessoMatriculaVO());
		setTurmaVO(new TurmaVO());
		setTurnoVO(new TurnoVO());
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));		
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("confirmacaoPreMatriculaForm.xhtml");
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaProcessoMatricula();
		montarListaSelectItemTurno();
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemPreMatricula() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("pago", "Pago"));
		itens.add(new SelectItem("receber", "A Receber"));
		return itens;
	}

	public void cancelarPreMatricula() {
		try {
			boolean existeContaReceberRecebidaOuNegociada = getFacadeFactory().getContaReceberFacade().consultarExistenciaContaReceberRecebidaOuNegociadaPorMatriculaPeriodo(getMatriculaPeridoVO().getCodigo());
			if (existeContaReceberRecebidaOuNegociada) {
				setOnCompletePanelConfirmarCancelarMatricula("");
				throw new Exception(UteisJSF.internacionalizar("msg_ConfirmacaoPreMatricula_preMatriculaComParcelaPaga"));
			}
//			if(getMatriculaPeridoVO().getCalouro()){
//				setOnCompletePanelConfirmarCancelarMatricula("");
//				throw new Exception(UteisJSF.internacionalizar("msg_CancelamentoPreMatricula_preMatriculaCalouro"));
//			}
			getFacadeFactory().getMatriculaPeriodoFacade().executarCancelamentoPreMatricula(getMatriculaPeridoVO(), getUsuarioLogado().getCodigo(), getListaAlunosPreMatriculados(), getUsuarioLogado());
			if (getMatriculaPeridoVO().getCalouro()) {
				MatriculaVO matNrMatriculaCancelada = getFacadeFactory().getMatriculaFacade().consultarMatriculaNrMatriculaCancelada(getMatriculaPeridoVO().getMatricula(), false, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(matNrMatriculaCancelada.getNrMatriculaCancelada())) {
					String[] matriculas = matNrMatriculaCancelada.getNrMatriculaCancelada().split(";");
					MatriculaPeriodoVO matPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarListaMatriculasCanceladas(matriculas);
					CancelamentoVO matCancelada = getFacadeFactory().getCancelamentoFacade().consultarPorMatricula(matPeriodo.getMatricula(), Uteis.NIVELMONTARDADOS_COMBOBOX, null, getUsuarioLogado());
					if (Uteis.isAtributoPreenchido(matCancelada)) {
						getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(matPeriodo.getMatricula(), "AT", getUsuarioLogado());
						getFacadeFactory().getMatriculaFacade().alterarMatriculaCanceladoFinanceiro(matPeriodo.getMatricula(), false);
						getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodo(matPeriodo.getCodigo(), "AT", getUsuarioLogado());
					}
				}
			}
			adicionarObjMatriculaPeriodoListaAlunosMatriculados(getMatriculaPeridoVO());
			setMatriculaPeridoVO(null);
			setOnCompletePanelConfirmarCancelarMatricula("RichFaces.$('confirmarCancelarMatricula').hide()");
			setMensagemID("msg_ConfirmacaoPreMatricula_preMatriculaCancelada");
		} catch (Exception e) {
			getMatriculaPeridoVO().setErro(true);
			getMatriculaPeridoVO().setMensagemErro(e.getMessage());
			getMatriculaPeridoVO().setApresentarSituacao(true);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarMatriculaPerido() {
		try {
			MatriculaPeriodoVO matPer = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(matPer, null, getUsuarioLogado());
			setMatriculaPeridoVO(matPer);
		} catch (Exception e) {
			setMatriculaPeridoVO(null);
		}
	}

	public void selecionarTodosDesmarcarTodos() {
		Iterator<MatriculaPeriodoVO> i = getListaAlunosPreMatriculados().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoVO m = (MatriculaPeriodoVO) i.next();
			m.setSelecionarMatriculaPeriodoRenovar(getSelecionarTodos());
		}
	}

	public TurmaVO getTurmaVO() {
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		Uteis.liberarListaMemoria(listaConsultaCurso);
	}

	/**
	 * @return the processoMatriculaVO
	 */
	public ProcessoMatriculaVO getProcessoMatriculaVO() {
		return processoMatriculaVO;
	}

	/**
	 * @param processoMatriculaVO
	 *            the processoMatriculaVO to set
	 */
	public void setProcessoMatriculaVO(ProcessoMatriculaVO processoMatriculaVO) {
		this.processoMatriculaVO = processoMatriculaVO;
	}

	/**
	 * @return the cursoVO
	 */
	public CursoVO getCursoVO() {
		return cursoVO;
	}

	/**
	 * @param cursoVO
	 *            the cursoVO to set
	 */
	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	/**
	 * @return the turnoVO
	 */
	public TurnoVO getTurnoVO() {
		return turnoVO;
	}

	/**
	 * @param turnoVO
	 *            the turnoVO to set
	 */
	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	/**
	 * @return the matriculaVO
	 */
	public MatriculaVO getMatriculaVO() {
		return matriculaVO;
	}

	/**
	 * @param matriculaVO
	 *            the matriculaVO to set
	 */
	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	/**
	 * @return the campoConsultaCurso
	 */
	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}

	/**
	 * @param campoConsultaCurso
	 *            the campoConsultaCurso to set
	 */
	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	/**
	 * @return the valorConsultaCurso
	 */
	public String getValorConsultaCurso() {
		return valorConsultaCurso;
	}

	/**
	 * @param valorConsultaCurso
	 *            the valorConsultaCurso to set
	 */
	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	/**
	 * @return the listaConsultaCurso
	 */
	public List<CursoVO> getListaConsultaCurso() {
		return listaConsultaCurso;
	}

	/**
	 * @param listaConsultaCurso
	 *            the listaConsultaCurso to set
	 */
	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	/**
	 * @param listaSelectItemUnidadeEnsino
	 *            the listaSelectItemUnidadeEnsino to set
	 */
	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	/**
	 * @return the unidadeEnsinoVO
	 */
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		return unidadeEnsinoVO;
	}

	/**
	 * @param unidadeEnsinoVO
	 *            the unidadeEnsinoVO to set
	 */
	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	/**
	 * @return the listaSelectItemProcessoMatricula
	 */
	public List<SelectItem> getListaSelectItemProcessoMatricula() {
		return listaSelectItemProcessoMatricula;
	}

	/**
	 * @param listaSelectItemProcessoMatricula
	 *            the listaSelectItemProcessoMatricula to set
	 */
	public void setListaSelectItemProcessoMatricula(List<SelectItem> listaSelectItemProcessoMatricula) {
		this.listaSelectItemProcessoMatricula = listaSelectItemProcessoMatricula;
	}

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List<MatriculaVO> getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	/**
	 * @return the listaSelectItemTurma
	 */
	public List<SelectItem> getListaSelectItemTurma() {
		return listaSelectItemTurma;
	}

	/**
	 * @param listaSelectItemTurma
	 *            the listaSelectItemTurma to set
	 */
	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	/**
	 * @return the listaSelectItemTurno
	 */
	public List<SelectItem> getListaSelectItemTurno() {
		return listaSelectItemTurno;
	}

	/**
	 * @param listaSelectItemTurno
	 *            the listaSelectItemTurno to set
	 */
	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	/**
	 * @return the listaSelectItemSituacao
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> getListaSelectItemSituacao() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		Hashtable situacaoGradeCurriculars = (Hashtable) Dominios.getSituacaoMatriculaPeriodoPreMatriculada();
		Enumeration keys = situacaoGradeCurriculars.keys();
		objs.add(new SelectItem("todos", "TODOS"));
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoGradeCurriculars.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * @param listaSelectItemSituacao
	 *            the listaSelectItemSituacao to set
	 */
	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	/**
	 * @return the situacao
	 */
	public String getSituacao() {
		return situacao;
	}

	/**
	 * @param situacao
	 *            the situacao to set
	 */
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	/**
	 * @return the listaAlunosPreMatriculados
	 */
	public List<MatriculaPeriodoVO> getListaAlunosPreMatriculados() {
		return listaAlunosPreMatriculados;
	}

	/**
	 * @param listaAlunosPreMatriculados
	 *            the listaAlunosPreMatriculados to set
	 */
	public void setListaAlunosPreMatriculados(List<MatriculaPeriodoVO> listaAlunosPreMatriculados) {
		this.listaAlunosPreMatriculados = listaAlunosPreMatriculados;
	}

	public MatriculaPeriodoVO getMatriculaPeridoVO() {
		if (matriculaPeridoVO == null) {
			matriculaPeridoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeridoVO;
	}

	public void setMatriculaPeridoVO(MatriculaPeriodoVO matriculaPeridoVO) {
		this.matriculaPeridoVO = matriculaPeridoVO;
	}

	public List<MatriculaPeriodoVO> getListaMatriculaPeriodoNaoPodemSeremEfetivadas() {
		if (listaMatriculaPeriodoNaoPodemSeremEfetivadas == null) {
			listaMatriculaPeriodoNaoPodemSeremEfetivadas = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return listaMatriculaPeriodoNaoPodemSeremEfetivadas;
	}

	public void setListaMatriculaPeriodoNaoPodemSeremEfetivadas(List<MatriculaPeriodoVO> listaMatriculaPeriodoNaoPodemSeremEfetivadas) {
		this.listaMatriculaPeriodoNaoPodemSeremEfetivadas = listaMatriculaPeriodoNaoPodemSeremEfetivadas;
	}

	public List<MatriculaPeriodoVO> getListaMatriculaPeriodoNaoPodemSeremCanceladas() {
		if (listaMatriculaPeriodoNaoPodemSeremCanceladas == null) {
			listaMatriculaPeriodoNaoPodemSeremCanceladas = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return listaMatriculaPeriodoNaoPodemSeremCanceladas;
	}

	public void setListaMatriculaPeriodoNaoPodemSeremCanceladas(List<MatriculaPeriodoVO> listaMatriculaPeriodoNaoPodemSeremCanceladas) {
		this.listaMatriculaPeriodoNaoPodemSeremCanceladas = listaMatriculaPeriodoNaoPodemSeremCanceladas;
	}

	/**
	 * Método responsável por cancelar todas as pré matrículas listadas na tela.
	 * Utiliza método
	 * "executarValidacaoParaEfetivarMatriculasPeriodoPreMatriculadas" de
	 * MatriculaPeriodoFacade.
	 */
	@SuppressWarnings("unchecked")
	public void executarValidacaoParaCancelarTodasPreMatriculadas() {
		try {
			setListaMatriculaPeriodoNaoPodemSeremCanceladas(getFacadeFactory().getMatriculaPeriodoFacade().executarValidacaoParaCancelarMatriculasPeriodoPreMatriculadas(getListaAlunosPreMatriculados(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			setMensagemID("msg_ConfirmacaoPreMatricula_preMatriculaCancelada");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void executarCancelamentoSomentePossiveis() {
		try {
			setListaAlunosPreMatriculados(getFacadeFactory().getMatriculaPeriodoFacade().executarCancelamentoMatriculasPeriodoPreMatriculadas(getListaAlunosPreMatriculados(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			setMensagemID("msg_ConfirmacaoPreMatricula_sucessoAtivacao");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getApresentarMatriculasNaoPodemSeremCanceladas() {
		if (!getListaMatriculaPeriodoNaoPodemSeremCanceladas().isEmpty()) {
			return "RichFaces.$('panelPreMatriculasNaoPodemSeremCanceladas').show()";
		}
		return "";
	}

	public boolean getApresentarListaMatriculasNaoPodemSeremCanceladas() {
		return !getListaMatriculaPeriodoNaoPodemSeremCanceladas().isEmpty();
	}

	/**
	 * @return the selecionarTodos
	 */
	public Boolean getSelecionarTodos() {
		if (selecionarTodos == null) {
			selecionarTodos = Boolean.FALSE;
		}
		return selecionarTodos;
	}

	/**
	 * @param selecionarTodos
	 *            the selecionarTodos to set
	 */
	public void setSelecionarTodos(Boolean selecionarTodos) {
		this.selecionarTodos = selecionarTodos;
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public Boolean getOcorreuPrimeiraAula() {
		if (ocorreuPrimeiraAula == null) {
			ocorreuPrimeiraAula = Boolean.FALSE;
		}
		return ocorreuPrimeiraAula;
	}

	public void setOcorreuPrimeiraAula(Boolean ocorreuPrimeiraAula) {
		this.ocorreuPrimeiraAula = ocorreuPrimeiraAula;
	}

	public String getPreMatricula() {
		if (preMatricula == null) {
			preMatricula = "";
		}
		return preMatricula;
	}

	public void setPreMatricula(String preMatricula) {
		this.preMatricula = preMatricula;
	}

	public void executarValidacaoParaExcluirTodasPreMatriculadas() {
		try {
			if (getPermitirExcluirPreMatricula() || getPermitirExcluirPreMatriculaCancelada()) {
				setListaMatriculaPeriodoNaoPodemSeremExcluidas(getFacadeFactory().getMatriculaPeriodoFacade().executarValidacaoParaExcluirMatriculasPeriodoPreMatriculadas(getListaAlunosPreMatriculados(), getConfiguracaoFinanceiroPadraoSistema(), getConfiguracaoGeralPadraoSistema(),  getUsuarioLogado()));
				setMensagemID("msg_ConfirmacaoPreMatricula_preMatriculaExcluida");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarExclusaoSomentePossiveis() {
		try {
			if (getPermitirExcluirPreMatricula() || getPermitirExcluirPreMatriculaCancelada()) {
				setListaAlunosPreMatriculados(getFacadeFactory().getMatriculaPeriodoFacade().executarExclusaoMatriculasPeriodoPreMatriculadas(getListaAlunosPreMatriculados(), getConfiguracaoFinanceiroPadraoSistema(), getConfiguracaoGeralPadraoSistema(),  getUsuarioLogado()));
				setMensagemID("msg_ConfirmacaoPreMatricula_preMatriculaExcluida");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void excluirPreMatricula() {
		try {
			if (getPermitirExcluirPreMatricula() || getPermitirExcluirPreMatriculaCancelada()) {
				getFacadeFactory().getMatriculaPeriodoFacade().executarExclusaoPreMatricula(getMatriculaPeridoVO(), getConfiguracaoFinanceiroPadraoSistema(), getConfiguracaoGeralPadraoSistema(),  getListaAlunosPreMatriculados(), getUsuarioLogado());
				if (getMatriculaPeridoVO().getErro()) {
					throw new Exception(getMatriculaPeridoVO().getMensagemErro());
				}
				setMatriculaPeridoVO(null);
				setOnCompletePanelConfirmarExcluirMatricula("RichFaces.$('confirmarExcluirMatricula').hide()");
				setMensagemID("msg_ConfirmacaoPreMatricula_preMatriculaExcluida");
			}
		} catch (Exception e) {
			setOnCompletePanelConfirmarExcluirMatricula("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getApresentarMatriculasNaoPodemSeremExcluidas() {
		if (!getListaMatriculaPeriodoNaoPodemSeremExcluidas().isEmpty()) {
			return "RichFaces.$('panelPreMatriculasNaoPodemSeremExcluidas').show()";
		}
		return "";
	}

	public boolean getApresentarListaMatriculasNaoPodemSeremExcluidas() {
		return !getListaMatriculaPeriodoNaoPodemSeremCanceladas().isEmpty();
	}

	public List<MatriculaPeriodoVO> getListaMatriculaPeriodoNaoPodemSeremExcluidas() {
		if (listaMatriculaPeriodoNaoPodemSeremExcluidas == null) {
			listaMatriculaPeriodoNaoPodemSeremExcluidas = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return listaMatriculaPeriodoNaoPodemSeremExcluidas;
	}

	public void setListaMatriculaPeriodoNaoPodemSeremExcluidas(List<MatriculaPeriodoVO> listaMatriculaPeriodoNaoPodemSeremExcluidas) {
		this.listaMatriculaPeriodoNaoPodemSeremExcluidas = listaMatriculaPeriodoNaoPodemSeremExcluidas;
	}

	public boolean getPermitirExcluirPreMatricula() {
		return getLoginControle().getPermissaoAcessoMenuVO().getPermitirExcluirPreMatricula();
	}

	public boolean getPermitirExcluirPreMatriculaCancelada() {
		return getLoginControle().getPermissaoAcessoMenuVO().getPermitirExcluirPreMatriculaCancelada();
	}

	public Boolean getTrazerPreMatriculasCanceladas() {
		if (trazerPreMatriculasCanceladas == null) {
			trazerPreMatriculasCanceladas = false;
		}
		return trazerPreMatriculasCanceladas;
	}

	public void setTrazerPreMatriculasCanceladas(Boolean trazerPreMatriculasCanceladas) {
		this.trazerPreMatriculasCanceladas = trazerPreMatriculasCanceladas;
	}

	public Boolean getIsApresentarSituacaoMatriculaPeriodo() {
		return getMatriculaPeridoVO().getSituacaoMatriculaPeriodo().equals("AT");
	}

	public String getOnCompletePanelConfirmarExcluirMatricula() {
		if (onCompletePanelConfirmarExcluirMatricula == null) {
			onCompletePanelConfirmarExcluirMatricula = "";
		}
		return onCompletePanelConfirmarExcluirMatricula;
	}

	public void setOnCompletePanelConfirmarExcluirMatricula(String onCompletePanelConfirmarExcluirMatricula) {
		this.onCompletePanelConfirmarExcluirMatricula = onCompletePanelConfirmarExcluirMatricula;
	}

	public String getOnCompletePanelConfirmarCancelarMatricula() {
		if (onCompletePanelConfirmarCancelarMatricula == null) {
			onCompletePanelConfirmarCancelarMatricula = "";
		}
		return onCompletePanelConfirmarCancelarMatricula;
	}

	public void setOnCompletePanelConfirmarCancelarMatricula(String onCompletePanelConfirmarCancelarMatricula) {
		this.onCompletePanelConfirmarCancelarMatricula = onCompletePanelConfirmarCancelarMatricula;
	}

	public void limparMensagem() {
		setMensagemID("msg_entre_dados");
	}

}
