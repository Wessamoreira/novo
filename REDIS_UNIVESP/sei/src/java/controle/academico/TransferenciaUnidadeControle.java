package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * matriculaForm.jsp matriculaCons.jsp) com as funcionalidades da classe <code>Matricula</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Matricula
 * @see MatriculaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TransferenciaUnidadeVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.academico.MatriculaPeriodo;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("TransferenciaUnidadeControle")
@Scope("viewScope")
@Lazy
public class TransferenciaUnidadeControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private MatriculaPeriodoVO matriculaPeriodoVoOrigem;
	private MatriculaPeriodoVO matriculaPeriodoVoDestino;
	private MatriculaVO matriculaVoNova;
	private UnidadeEnsinoVO unidadeEnsinoOrigem;
	private UnidadeEnsinoVO unidadeEnsinoDestino;
	private MotivoCancelamentoTrancamentoVO motivo;
	private TransferenciaUnidadeVO transferenciaUnidadeVO;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private List<MatriculaVO> listaConsultaAluno;
	private List<SelectItem> listaSelectItemUnidadeEnsinoOrigem;
	private List<SelectItem> listaSelectItemUnidadeEnsinoDestino;
	private List<SelectItem> listaSelectItemProcessoMatricula;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private List<SelectItem> listaSelectItemPeriodoLetivoMatricula;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemPlanoFinanceiroCurso;
	private List<SelectItem> listaSelectItemMotivo;
	private List<SelectItem> listaSelectItemTextoPadrao;
	private List<SelectItem> listaSelectItemTextoPadraoDeclaracao;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private String turma_Erro;
	private Boolean apresentarPlanoFinanceiroCurso;
	private Boolean transferirDescontos;
	private Boolean textoPadraoDeclaracao;
	private Boolean imprimirContrato;
	private Date valorConsultaData;
	private Date valorConsultaDataFinal;
	private Integer textoPadrao;
	private Boolean apresentarCodigoFinanceiro;	

	public TransferenciaUnidadeControle() throws Exception {
		novo();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public String persistir() {
		try {
			gerarDataMatriculaDestino();
			setTransferenciaUnidadeVO(getFacadeFactory().getTransferenciaUnidadeFacade().persistir(getMatriculaPeriodoVoOrigem(), getMatriculaPeriodoVoDestino(), getMatriculaVoNova(), getTransferirDescontos(), getListaSelectItemReconhecimentoCurso(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVoOrigem().getMatriculaVO().getUnidadeEnsino().getCodigo()), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getMatriculaPeriodoVoOrigem().getMatriculaVO().getUnidadeEnsino().getCodigo()), getMotivo(), getUsuarioLogado()));
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaUnidadeForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVoOrigem().getMatriculaVO().getMatricula(), this.getUnidadeEnsinoOrigem().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaPeriodoVoOrigem().getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaPeriodoVoOrigem(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getMatriculaPeriodoVoOrigem().getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getMatriculaPeriodoVoOrigem().setMatriculaVO(objAluno);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getMatriculaPeriodoVoOrigem().setMatriculaVO(new MatriculaVO());
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (this.getUnidadeEnsinoOrigem().getCodigo() != 0) {
				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoOrigem().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoOrigem().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoOrigem().getCodigo(), false, getUsuarioLogado());
				}
				setListaConsultaAluno(objs);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
			obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (obj.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaPeriodoVoOrigem().getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}

			if (getFacadeFactory().getTransferenciaUnidadeFacade().verificarExistenciaTransferenciaInterna(obj.getMatricula())) {
				throw new Exception("Já existe um registro de Transferência Interna para o aluno de matrícula " + getMatriculaPeriodoVoOrigem().getMatriculaVO().getMatricula() + ". Verifique se o número de matrícula está correto.");
			}
			setMatriculaPeriodoVoOrigem(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getMatriculaPeriodoVoOrigem().setMatriculaVO(obj);

			valorConsultaAluno = "";
			campoConsultaAluno = "";
			getListaConsultaAluno().clear();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getMatriculaPeriodoVoOrigem().setMatriculaVO(new MatriculaVO());
		}
	}

	public void limparDadosMatriculaOrigem() throws Exception {
		removerObjetoMemoria(getMatriculaPeriodoVoOrigem());
		setMatriculaPeriodoVoOrigem(new MatriculaPeriodoVO());
		Uteis.liberarListaMemoria(getListaConsultaAluno());
	}

	public void limparDadosMatriculaDestino() throws Exception {
		removerObjetoMemoria(getMatriculaPeriodoVoDestino());
		removerObjetoMemoria(getMatriculaVoNova());
		setMatriculaPeriodoVoDestino(new MatriculaPeriodoVO());
		setMatriculaVoNova(new MatriculaVO());
		getMatriculaVoNova().getTransferenciaEntrada().setTipoTransferenciaEntrada("IN");
		Uteis.liberarListaMemoria(getListaConsultaCurso());
		Uteis.liberarListaMemoria(getListaSelectItemProcessoMatricula());
		Uteis.liberarListaMemoria(getListaSelectItemGradeCurricular());
		Uteis.liberarListaMemoria(getListaSelectItemPeriodoLetivoMatricula());
		Uteis.liberarListaMemoria(getListaSelectItemTurma());
		Uteis.liberarListaMemoria(getListaSelectItemPlanoFinanceiroCurso());
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getUnidadeEnsinoOrigem().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoOrigem().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public void montarListaSelectItemUnidadeEnsinoDestino() {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsinoDestino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			limparDadosMatriculaDestino();
			setUnidadeEnsinoOrigem(new UnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoOrigem().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
			setMensagemID("");
		} catch (Exception e) {
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				setListaSelectItemUnidadeEnsinoOrigem(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			} else {
				setListaSelectItemUnidadeEnsinoOrigem(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, getUnidadeEnsinoDestino().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoDestino().getCodigo(), false, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void limparListaConsultaCurso() {
		Uteis.liberarListaMemoria(getListaConsultaCurso());
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(unidadeEnsinoCurso.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			getMatriculaVoNova().setCurso(cursoVO);
			getMatriculaVoNova().setTurno(unidadeEnsinoCurso.getTurno());
			if (getUnidadeEnsinoDestino().getCodigo().intValue() == 0) {
				throw new Exception("Selecione a unidade de ensino, para dar continuidade ao procedimento!");
			}
			setUnidadeEnsinoDestino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoDestino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getMatriculaVoNova().setUnidadeEnsino(getUnidadeEnsinoDestino());
			getFacadeFactory().getMatriculaFacade().verificaAlunoJaMatriculado(getMatriculaVoNova(), false, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(),false, false);
			setMensagemDetalhada("");
			montarListaSelectItemGradeCurricular();
			montarListaProcessoMatricula();
			montarListaSelectItemPeriodoLetivo();
			getMatriculaPeriodoVoDestino().setUnidadeEnsinoCurso(unidadeEnsinoCurso.getCodigo());
			getMatriculaPeriodoVoDestino().getMatriculaPeriodoTumaDisciplinaVOs().clear();
		} catch (Exception e) {
			getMatriculaVoNova().setCurso(new CursoVO());
			getMatriculaVoNova().setTurno(new TurnoVO());
			getListaSelectItemProcessoMatricula().clear();
			getListaSelectItemGradeCurricular().clear();
			getListaSelectItemPeriodoLetivoMatricula().clear();
			getListaSelectItemTurma().clear();
			getListaSelectItemPlanoFinanceiroCurso().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void processarDadosPermitinentesTurmaSelecionada() {
		try {
			getListaSelectItemPlanoFinanceiroCurso().clear();
			if (getMatriculaPeriodoVoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() != 0) {
				getMatriculaPeriodoVoDestino().setCondicaoPagamentoPlanoFinanceiroCurso(new CondicaoPagamentoPlanoFinanceiroCursoVO());
			}
			atualizarSituacaoMatriculaPeriodo();
			getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(getMatriculaVoNova(), getMatriculaPeriodoVoDestino(), getUsuarioLogado(), null, false, false);
			getMatriculaPeriodoVoDestino().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVoDestino().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(getMatriculaVoNova(), getMatriculaPeriodoVoDestino(), getUsuarioLogado());
			inicializarListaSelectItemPlanoFinanceiroCursoParaTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gerarDataMatriculaDestino() {
		try {
			if (getMatriculaPeriodoVoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() != 0) {
				getMatriculaPeriodoVoDestino().setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				if (getMatriculaVoNova().getCurso().getNivelEducacionalPosGraduacao()) {
					getFacadeFactory().getTransferenciaUnidadeFacade().gerarDataMatriculaDestino(getMatriculaVoNova(), getMatriculaPeriodoVoOrigem(), getMatriculaPeriodoVoDestino());
				} else {
					getFacadeFactory().getTransferenciaUnidadeFacade().gerarDataMatriculaDestinoDeAcordoProcessoMatricula(getMatriculaVoNova(), getMatriculaPeriodoVoOrigem(), getMatriculaPeriodoVoDestino());
				}
			} else {
				throw new ConsistirException("É necessário informar o plano financeiro do curso.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarSituacaoMatriculaPeriodo() {
		try {
			if ((getMatriculaPeriodoVoDestino().getTurma() == null) || (getMatriculaPeriodoVoDestino().getTurma().getCodigo().intValue() == 0)) {
				return;
			}
			if ((getMatriculaPeriodoVoDestino().getProcessoMatricula() == null) || (getMatriculaPeriodoVoDestino().getProcessoMatricula().equals(0))) {
				return;
			}
			getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(getMatriculaVoNova(), getMatriculaPeriodoVoDestino(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getFacadeFactory().getMatriculaPeriodoFacade().definirSituacaoMatriculaPeriodoComBaseProcesso(getMatriculaVoNova(),  getMatriculaPeriodoVoDestino(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVoNova().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarListaSelectItemPlanoFinanceiroCursoParaTurma() {
		if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVoDestino().getTurma())) {
			setApresentarPlanoFinanceiroCurso(true);
		}
		montarListaSelectItemPlanoFinanceiroCurso();
	}

	public void montarListaSelectItemPlanoFinanceiroCurso() {
		try {
			montarListaSelectItemPlanoFinanceiroCurso("");
		} catch (Exception e) {
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaSelectItemPlanoFinanceiroCurso(String prm) throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		List resultadoConsulta = new ArrayList(0);
		List<CondicaoPagamentoPlanoFinanceiroCursoVO> listaTodasCondicaoPagamentoVOs = new ArrayList<CondicaoPagamentoPlanoFinanceiroCursoVO>(0);
		if (getMatriculaPeriodoVoDestino().getUnidadeEnsinoCurso().equals(0)) {
			setListaSelectItemPlanoFinanceiroCurso(objs);
			return;
		}
		getMatriculaPeriodoVoDestino().setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVoDestino().getUnidadeEnsinoCurso(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		if (getMatriculaPeriodoVoDestino().getUnidadeEnsinoCursoVO().getPlanoFinanceiroCurso().getCodigo().intValue() != getMatriculaPeriodoVoDestino().getPlanoFinanceiroCurso().getCodigo().intValue()) {
			getFacadeFactory().getTurmaFacade().carregarDados(getMatriculaPeriodoVoDestino().getTurma(), getUsuarioLogado());
			if (getMatriculaPeriodoVoDestino().getTurma().getPlanoFinanceiroCurso().getCodigo() != 0) {
				resultadoConsulta = getFacadeFactory().getMatriculaPeriodoFacade().executarMontagemComboCondicaoPagamentoPlanoFinanceiroCurso(getMatriculaPeriodoVoDestino().getTurma().getPlanoFinanceiroCurso().getCodigo(), getMatriculaPeriodoVoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), getMatriculaVoNova(), getMatriculaPeriodoVoDestino(), getUsuarioLogado());
				getMatriculaPeriodoVoDestino().setPlanoFinanceiroCurso(getMatriculaPeriodoVoDestino().getTurma().getPlanoFinanceiroCurso());
			} else {
				resultadoConsulta = getFacadeFactory().getMatriculaPeriodoFacade().executarMontagemComboCondicaoPagamentoPlanoFinanceiroCurso(getMatriculaPeriodoVoDestino().getUnidadeEnsinoCursoVO().getPlanoFinanceiroCurso().getCodigo(), getMatriculaPeriodoVoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), getMatriculaVoNova(), getMatriculaPeriodoVoDestino(), getUsuarioLogado());
				getMatriculaPeriodoVoDestino().setPlanoFinanceiroCurso(getMatriculaPeriodoVoDestino().getUnidadeEnsinoCursoVO().getPlanoFinanceiroCurso());
			}
			MatriculaPeriodo.montarDadosPlanoFinanceiroCurso(getMatriculaPeriodoVoDestino(), NivelMontarDados.TODOS, getUsuarioLogado());
		} else {
			MatriculaPeriodo.montarDadosPlanoFinanceiroCurso(getMatriculaPeriodoVoDestino(), NivelMontarDados.TODOS, getUsuarioLogado());
			listaTodasCondicaoPagamentoVOs = this.getMatriculaPeriodoVoDestino().getPlanoFinanceiroCurso().getCondicaoPagamentoPlanoFinanceiroCursoVOs();
			// Pega a condiçãoPagamentoPlanoFinanceiroCurso atual do aluno
			// independente da situacao em que se encontra, e as
			// condiçõesPagamentoPlanoFinanceiroCurso
			// ativas que ele não está utilizando.
			// Carlos
			for (CondicaoPagamentoPlanoFinanceiroCursoVO obj : listaTodasCondicaoPagamentoVOs) {
				if (obj.getCodigo().equals(getMatriculaPeriodoVoDestino().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo())) {
					resultadoConsulta.add(obj);
				} else {
					if (obj.getSituacao().equals("AT")) {
						resultadoConsulta.add(obj);
					}
				}
			}
		}
		Iterator i = resultadoConsulta.iterator();
		String planoFinanceiroDesc = this.getMatriculaPeriodoVoDestino().getPlanoFinanceiroCurso().getDescricao();
		if (!planoFinanceiroDesc.equals("")) {
			if (planoFinanceiroDesc.length() > 20) {
				planoFinanceiroDesc = planoFinanceiroDesc.substring(0, 20);
			}
			planoFinanceiroDesc = planoFinanceiroDesc + " - ";
		}
		while (i.hasNext()) {
			CondicaoPagamentoPlanoFinanceiroCursoVO obj = (CondicaoPagamentoPlanoFinanceiroCursoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), planoFinanceiroDesc + obj.getDescricao()));
		}
		setListaSelectItemPlanoFinanceiroCurso(objs);
	}

	public void atualizarSituacaoMatriculaPeriodoPartindoProceMatricula() {
		try {
			executarDefinicaoPeriodoLetivoNovaMatriculaAluno();
			if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVoDestino().getTurma())) {
				setMensagemID("msg_entre_dados");
				return;
			}
			if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVoDestino().getProcessoMatricula())) {
				setMensagemID("msg_entre_dados");
				return;
			}
			getMatriculaPeriodoVoDestino().getProcessoMatriculaVO().setCodigo(getMatriculaPeriodoVoDestino().getProcessoMatricula());
			getFacadeFactory().getProcessoMatriculaFacade().carregarDados(getMatriculaPeriodoVoDestino().getProcessoMatriculaVO(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
			getMatriculaPeriodoVoDestino().setProcessoMatriculaCalendarioVO(new ProcessoMatriculaCalendarioVO());
			getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(getMatriculaVoNova(), getMatriculaPeriodoVoDestino(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
			getMatriculaPeriodoVoDestino().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVoDestino().getTurma().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado()));
			getFacadeFactory().getMatriculaPeriodoFacade().definirSituacaoMatriculaPeriodoComBaseProcesso(getMatriculaVoNova(), getMatriculaPeriodoVoDestino(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVoNova().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarDefinicaoPeriodoLetivoNovaMatriculaAluno() throws Exception {
		getMatriculaPeriodoVoDestino().getMatriculaPeriodoTumaDisciplinaVOs().clear();
		getMatriculaPeriodoVoDestino().setPeridoLetivo(new PeriodoLetivoVO());
		getFacadeFactory().getMatriculaPeriodoFacade().definirPeriodoLetivoNovaMatriculaAluno(getMatriculaVoNova(), getMatriculaPeriodoVoDestino(), getUsuarioLogado());
		montarListaSelectItemPeriodoLetivo();
		prepararDadosTurma();
	}

	public void prepararDadosTurma() {
		try {
			MatriculaPeriodo.montarDadosPeriodoLetivoMatricula(getMatriculaPeriodoVoDestino(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
			montarListaSelectItemTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTurma() {
		try {
			montarListaSelectItemTurma("");
		} catch (Exception e) {
		}
	}

	public void montarListaSelectItemTurma(String prm) throws Exception {
		List<TurmaVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarTurmaPorIdentificadorTurma();
			if (resultadoConsulta.isEmpty()) {
				this.setTurma_Erro("Não existe turma cadastrada para o curso: " + getMatriculaVoNova().getCurso().getNome().toUpperCase() + " no período: " + getMatriculaVoNova().getTurno().getNome().toUpperCase());
			} else {
				this.setTurma_Erro("");
			}
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public List<TurmaVO> consultarTurmaPorIdentificadorTurma() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(getMatriculaPeriodoVoDestino().getPeridoLetivo().getPeriodoLetivo(), getMatriculaVoNova().getUnidadeEnsino().getCodigo(), getMatriculaVoNova().getCurso().getCodigo(), getMatriculaVoNova().getTurno().getCodigo(), getMatriculaPeriodoVoDestino().getGradeCurricular().getCodigo(), true, false, false, getMatriculaVoNova().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), getMatriculaPeriodoVoOrigem().getAno(),  getMatriculaPeriodoVoOrigem().getSemestre(),   false, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), false);
	}

	public void montarListaSelectItemGradeCurricular() {
		try {
			montarListaSelectItemGradeCurricular("");
		} catch (Exception e) {
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaSelectItemGradeCurricular(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List<GradeCurricularVO> resultadoConsulta = null;
		Iterator<GradeCurricularVO> i = null;
		try {
			if (getMatriculaVoNova().getUnidadeEnsino().getCodigo().equals(0)) {
				setListaSelectItemGradeCurricular(new ArrayList<SelectItem>(0));
				return;
			}
			if (getMatriculaVoNova().getCurso().getCodigo().equals(0)) {
				setListaSelectItemGradeCurricular(new ArrayList<SelectItem>(0));
				return;
			}
			resultadoConsulta = consultarGradeCurricularPorDescricao(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				GradeCurricularVO obj = (GradeCurricularVO) i.next();
				if (obj.getSituacao().equals("AT")) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}

				if ((obj.getSituacao().equals("AT")) && (getMatriculaPeriodoVoDestino().getGradeCurricular().getCodigo() == 0)) {
					getMatriculaPeriodoVoDestino().setGradeCurricular(obj);
				}
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemGradeCurricular(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<GradeCurricularVO> consultarGradeCurricularPorDescricao(String descricaoPrm) throws Exception {
		return getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(getMatriculaVoNova().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaProcessoMatricula() {
		try {
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			if (getMatriculaVoNova().getUnidadeEnsino().getCodigo() == null || getMatriculaVoNova().getUnidadeEnsino().getCodigo() == 0) {
				setListaSelectItemProcessoMatricula(objs);
				return;
			}
			List<ProcessoMatriculaVO> resultadoConsulta = consultarProcessoMatriculaPorUnidadeEnsino(getMatriculaVoNova());
			Iterator<ProcessoMatriculaVO> i = resultadoConsulta.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ProcessoMatriculaVO obj = (ProcessoMatriculaVO) i.next();
				if (obj.ativoParaMatriculaEPreMatricula(new Date())) {
					/**
					 * se obj é novo, so add processoMatricula q ñ estaja
					 * Finalizado
					 */
					if (getMatriculaPeriodoVoDestino().isNovoObj() && !obj.getSituacao().equals("FI")) {
						objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
						/**
						 * se nao for novo, add todos processoMatricula
						 */
					} else if (!getMatriculaPeriodoVoDestino().isNovoObj()) {
						objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
					}
				}
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemProcessoMatricula(objs);
		} catch (Exception e) {
		}
	}

	public List<ProcessoMatriculaVO> consultarProcessoMatriculaPorUnidadeEnsino(MatriculaVO matriculaVO) throws Exception {
		boolean existeOutraMatriculaPeriodoAtiva = getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMatriculaPeriodoPorSituacao(matriculaVO.getMatricula(), "AT");
		if (existeOutraMatriculaPeriodoAtiva) {
			return getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(matriculaVO.getTurno().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), "AT", "PR", false, false, Uteis.NIVELMONTARDADOS_TODOS, matriculaVO.getMatricula(), getUsuarioLogado(), TipoAlunoCalendarioMatriculaEnum.AMBOS);
		}
		return getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(matriculaVO.getTurno().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	public void montarListaSelectItemPeriodoLetivo() {
		try {
			montarListaSelectItemPeriodoLetivo(getMatriculaPeriodoVoDestino().getGradeCurricular().getCodigo());
		} catch (Exception e) {
		}
	}

	public void montarListaSelectItemPeriodoLetivo(Integer prm) {
		List<PeriodoLetivoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarPeriodoLetivoPorGradeCurricular(getMatriculaPeriodoVoDestino().getGradeCurricular().getCodigo());
			setListaSelectItemPeriodoLetivoMatricula(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
		} catch (Exception e) {
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	@SuppressWarnings("unchecked")
	public List<PeriodoLetivoVO> consultarPeriodoLetivoPorGradeCurricular(Integer gradeCurricular) throws Exception {
		return getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(gradeCurricular, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public String novo() {
		removerObjetoMemoria(this);
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemUnidadeEnsinoDestino();
		setMatriculaPeriodoVoDestino(new MatriculaPeriodoVO());
		setMatriculaPeriodoVoOrigem(new MatriculaPeriodoVO());
		setMatriculaVoNova(new MatriculaVO());
		getMatriculaVoNova().getTransferenciaEntrada().setTipoTransferenciaEntrada("IN");
		setListaSelectItemMotivo(null);
		setMensagemID("msg_entre_dados");		
		try {
			setApresentarCodigoFinanceiro(getConfiguracaoFinanceiroPadraoSistema().getUtilizarIntegracaoFinanceira());
		} catch (Exception e) {
			setApresentarCodigoFinanceiro(null);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaUnidadeForm.xhtml");
	}

	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaUnidadeCons.xhtml");
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matriculaOrigem", "Matrícula Origem"));
		itens.add(new SelectItem("matriculaDestino", "Matrícula Destino"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("data", "Data"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public boolean getApresentarReconhecimentoCurso() throws Exception {
		return getListaSelectItemReconhecimentoCurso().size() > 2;
	}

	@SuppressWarnings("rawtypes")
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "TransferenciaUnidadeControle", "Iniciando Consultar Transferência Unidade", "Consultando");
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("matriculaOrigem")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTransferenciaUnidadeFacade().consultaRapidaPorMatriculaOrigem(getControleConsulta().getValorConsulta(), 0, true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matriculaDestino")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTransferenciaUnidadeFacade().consultaRapidaPorMatriculaDestino(getControleConsulta().getValorConsulta(), 0, true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTransferenciaUnidadeFacade().consultaRapidaPorNomeAluno(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTransferenciaUnidadeFacade().consultaRapidaPorNomeCurso(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				objs = getFacadeFactory().getTransferenciaUnidadeFacade().consultaRapidaPorData(getValorConsultaData(), getValorConsultaDataFinal(), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			registrarAtividadeUsuario(getUsuarioLogado(), "TransferenciaUnidadeControle", "Finalizando Consultar Transferência Unidade", "Consultando");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaUnidadeCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaUnidadeCons.xhtml");
		}
	}

	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','99/99/9999',event);";
		}
		if (getControleConsulta().getCampoConsulta().equals("cpf")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','999.999.999-99',event);";
		}
		return "";
	}

	public boolean getApresentarCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVoDestino() {
		if (matriculaPeriodoVoDestino == null) {
			matriculaPeriodoVoDestino = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVoDestino;
	}

	public void setMatriculaPeriodoVoDestino(MatriculaPeriodoVO matriculaPeriodoVoDestino) {
		this.matriculaPeriodoVoDestino = matriculaPeriodoVoDestino;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVoOrigem() {
		if (matriculaPeriodoVoOrigem == null) {
			matriculaPeriodoVoOrigem = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVoOrigem;
	}

	public void setMatriculaPeriodoVoOrigem(MatriculaPeriodoVO matriculaPeriodoVoOrigem) {
		this.matriculaPeriodoVoOrigem = matriculaPeriodoVoOrigem;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoDestino() {
		if (unidadeEnsinoDestino == null) {
			unidadeEnsinoDestino = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoDestino;
	}

	public void setUnidadeEnsinoDestino(UnidadeEnsinoVO unidadeEnsinoDestino) {
		this.unidadeEnsinoDestino = unidadeEnsinoDestino;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoOrigem() {
		if (unidadeEnsinoOrigem == null) {
			unidadeEnsinoOrigem = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoOrigem;
	}

	public void setUnidadeEnsinoOrigem(UnidadeEnsinoVO unidadeEnsinoOrigem) {
		this.unidadeEnsinoOrigem = unidadeEnsinoOrigem;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsinoOrigem() {
		if (listaSelectItemUnidadeEnsinoOrigem == null) {
			listaSelectItemUnidadeEnsinoOrigem = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsinoOrigem;
	}

	public void setListaSelectItemUnidadeEnsinoOrigem(List<SelectItem> listaSelectItemUnidadeEnsinoOrigem) {
		this.listaSelectItemUnidadeEnsinoOrigem = listaSelectItemUnidadeEnsinoOrigem;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsinoDestino() {
		if (listaSelectItemUnidadeEnsinoDestino == null) {
			listaSelectItemUnidadeEnsinoDestino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsinoDestino;
	}

	public void setListaSelectItemUnidadeEnsinoDestino(List<SelectItem> listaSelectItemUnidadeEnsinoDestino) {
		this.listaSelectItemUnidadeEnsinoDestino = listaSelectItemUnidadeEnsinoDestino;
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivoMatricula() {
		if (listaSelectItemPeriodoLetivoMatricula == null) {
			listaSelectItemPeriodoLetivoMatricula = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodoLetivoMatricula;
	}

	public void setListaSelectItemPeriodoLetivoMatricula(List<SelectItem> listaSelectItemPeriodoLetivoMatricula) {
		this.listaSelectItemPeriodoLetivoMatricula = listaSelectItemPeriodoLetivoMatricula;
	}

	public List<SelectItem> getListaSelectItemPlanoFinanceiroCurso() {
		if (listaSelectItemPlanoFinanceiroCurso == null) {
			listaSelectItemPlanoFinanceiroCurso = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPlanoFinanceiroCurso;
	}

	public void setListaSelectItemPlanoFinanceiroCurso(List<SelectItem> listaSelectItemPlanoFinanceiroCurso) {
		this.listaSelectItemPlanoFinanceiroCurso = listaSelectItemPlanoFinanceiroCurso;
	}

	public List<SelectItem> getListaSelectItemProcessoMatricula() {
		if (listaSelectItemProcessoMatricula == null) {
			listaSelectItemProcessoMatricula = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProcessoMatricula;
	}

	public void setListaSelectItemProcessoMatricula(List<SelectItem> listaSelectItemProcessoMatricula) {
		this.listaSelectItemProcessoMatricula = listaSelectItemProcessoMatricula;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
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

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
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

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
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

	public MatriculaVO getMatriculaVoNova() {
		if (matriculaVoNova == null) {
			matriculaVoNova = new MatriculaVO();
		}
		return matriculaVoNova;
	}

	public void setMatriculaVoNova(MatriculaVO matriculaVoNova) {
		this.matriculaVoNova = matriculaVoNova;
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

	public Boolean getApresentarPlanoFinanceiroCurso() {
		if (apresentarPlanoFinanceiroCurso == null) {
			apresentarPlanoFinanceiroCurso = false;
		}
		return apresentarPlanoFinanceiroCurso;
	}

	public void setApresentarPlanoFinanceiroCurso(Boolean apresentarPlanoFinanceiroCurso) {
		this.apresentarPlanoFinanceiroCurso = apresentarPlanoFinanceiroCurso;
	}

	public Date getValorConsultaData() {
		if (valorConsultaData == null) {
			valorConsultaData = new Date();
		}
		return valorConsultaData;
	}

	public void setValorConsultaData(Date valorConsultaData) {
		this.valorConsultaData = valorConsultaData;
	}

	public Date getValorConsultaDataFinal() {
		if (valorConsultaDataFinal == null) {
			valorConsultaDataFinal = new Date();
		}
		return valorConsultaDataFinal;
	}

	public void setValorConsultaDataFinal(Date valorConsultaDataFinal) {
		this.valorConsultaDataFinal = valorConsultaDataFinal;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemReconhecimentoCurso() throws Exception {
		if (Uteis.isAtributoPreenchido(getMatriculaVoNova().getCurso())) {
			List<AutorizacaoCursoVO> listaCurso = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCurso(getMatriculaVoNova().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return UtilSelectItem.getListaSelectItem(listaCurso, "codigo", "nome");
		}
		return new ArrayList<SelectItem>(0);
	}

	public boolean getIsNovaMatriculaGerada() {
		return !getMatriculaVoNova().getMatricula().equals("");
	}

	public Boolean getTransferirDescontos() {
		if (transferirDescontos == null) {
			transferirDescontos = true;
		}
		return transferirDescontos;
	}

	public void setTransferirDescontos(Boolean transferirDescontos) {
		this.transferirDescontos = transferirDescontos;
	}

	public boolean getIsTurmaSelecionada() {
		return !getMatriculaPeriodoVoDestino().getTurma().getCodigo().equals(0);
	}

	public List<SelectItem> getListaSelectItemMotivo() {
		if (listaSelectItemMotivo == null) {
			listaSelectItemMotivo = new ArrayList<SelectItem>(0);
			try {
				List<MotivoCancelamentoTrancamentoVO> motivoCancelamentoTrancamentoVOs = new ArrayList<MotivoCancelamentoTrancamentoVO>(0);
				motivoCancelamentoTrancamentoVOs.addAll(getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorTipoJustificativaSituacao("TI", "AT"));
				motivoCancelamentoTrancamentoVOs.addAll(getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorTipoJustificativaSituacao("OU", "AT"));
				for (MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO : motivoCancelamentoTrancamentoVOs) {
					listaSelectItemMotivo.add(new SelectItem(motivoCancelamentoTrancamentoVO.getCodigo(), motivoCancelamentoTrancamentoVO.getNome()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return listaSelectItemMotivo;
	}

	public void setListaSelectItemMotivo(List<SelectItem> listaSelectItemMotivo) {
		this.listaSelectItemMotivo = listaSelectItemMotivo;
	}

	public MotivoCancelamentoTrancamentoVO getMotivo() {
		if (motivo == null) {
			motivo = new MotivoCancelamentoTrancamentoVO();
		}
		return motivo;
	}

	public void setMotivo(MotivoCancelamentoTrancamentoVO motivo) {
		this.motivo = motivo;
	}

	public void imprimirDeclaracao() throws Exception {
		try {
			setImprimirContrato(getFacadeFactory().getTransferenciaUnidadeFacade().imprimirDeclaracaoTransferenciaUnidade(getTransferenciaUnidadeVO().getCodigo(), getTextoPadrao(), getUsuarioLogado()));
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirContrato() throws Exception {
		try {
			setImprimirContrato(getFacadeFactory().getTransferenciaUnidadeFacade().imprimirContratoTransferenciaUnidade(getTransferenciaUnidadeVO().getCodigo(), getTextoPadrao(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getContrato() {
		if (getImprimirContrato()) {
			return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545); RichFaces.$('panelTextoPadraoDeclaracao').hide()";
		}
		return "";
	}

	public void editar() throws Exception {
		setTransferenciaUnidadeVO((TransferenciaUnidadeVO) context().getExternalContext().getRequestMap().get("transferenciaUnidadeItens"));
		montarListaSelectItemTextosPadroes();
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

	public TransferenciaUnidadeVO getTransferenciaUnidadeVO() {
		if (transferenciaUnidadeVO == null) {
			transferenciaUnidadeVO = new TransferenciaUnidadeVO();
		}
		return transferenciaUnidadeVO;
	}

	public void setTransferenciaUnidadeVO(TransferenciaUnidadeVO transferenciaUnidadeVO) {
		this.transferenciaUnidadeVO = transferenciaUnidadeVO;
	}

	/**
	 * Método responsável por executar a montagem da lista de SelectItems de
	 * TextoPadraoDeclaracao do tipo Transferencia de Unidade (TU).
	 */
	public void montarListaSelectItemTextoPadraoDeclaracao() {
		try {
			List<TextoPadraoDeclaracaoVO> textoPadraoDeclaracaoVOs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("TU", getUnidadeEnsinoLogado().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemTextoPadraoDeclaracao(UtilSelectItem.getListaSelectItem(textoPadraoDeclaracaoVOs, "codigo", "descricao", false));
		} catch (Exception e) {
		}
	}

	/**
	 * Método responsável por executar a montagem da lista de SelectItems de
	 * TextoPadrao do tipo Transferencia de Unidade (TU).
	 */
	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTextoPadrao() {
		try {
			List<TextoPadraoVO> textoPadraoVOs = getFacadeFactory().getTextoPadraoFacade().consultarPorTipoEUnidadeEnsino("TU", getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemTextoPadrao(UtilSelectItem.getListaSelectItem(textoPadraoVOs, "codigo", "descricao", false));
		} catch (Exception e) {
		}
	}

	/**
	 * Método responsável por executar a montagem da lista de SelectItems de
	 * TextoPadrao ou TextoPadraoDeclaracao de acordo com o tipo de TextoPadrao
	 * selecionado
	 */
	public void montarListaSelectItemTextosPadroes() {
		if (getTextoPadraoDeclaracao()) {
			montarListaSelectItemTextoPadraoDeclaracao();
		} else {
			montarListaSelectItemTextoPadrao();
		}
	}

	public List<SelectItem> getListaSelectItemTextoPadrao() {
		if (listaSelectItemTextoPadrao == null) {
			listaSelectItemTextoPadrao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTextoPadrao;
	}

	public void setListaSelectItemTextoPadrao(List<SelectItem> listaSelectItemTextoPadrao) {
		this.listaSelectItemTextoPadrao = listaSelectItemTextoPadrao;
	}

	public List<SelectItem> getListaSelectItemTextoPadraoDeclaracao() {
		if (listaSelectItemTextoPadraoDeclaracao == null) {
			listaSelectItemTextoPadraoDeclaracao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTextoPadraoDeclaracao;
	}

	public void setListaSelectItemTextoPadraoDeclaracao(List<SelectItem> listaSelectItemTextoPadraoDeclaracao) {
		this.listaSelectItemTextoPadraoDeclaracao = listaSelectItemTextoPadraoDeclaracao;
	}

	public Integer getTextoPadrao() {
		if (textoPadrao == null) {
			textoPadrao = 0;
		}
		return textoPadrao;
	}

	public void setTextoPadrao(Integer textoPadrao) {
		this.textoPadrao = textoPadrao;
	}

	public Boolean getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = false;
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(Boolean textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}

	public Boolean getApresentarCodigoFinanceiro() {
		if (apresentarCodigoFinanceiro == null) {
			apresentarCodigoFinanceiro = Boolean.FALSE;
		}
		return apresentarCodigoFinanceiro;
	}

	public void setApresentarCodigoFinanceiro(Boolean apresentarCodigoFinanceiro) {
		this.apresentarCodigoFinanceiro = apresentarCodigoFinanceiro;
	}

}
