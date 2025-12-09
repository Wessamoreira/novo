package controle.secretaria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.base.Splitter;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularMatriculaVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.academico.Historico;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas historicoForm.jsp historicoCons.jsp) com as funcionalidades da classe
 * <code>Historico</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Historico
 * @see HistoricoVO
 */
@Controller("TransferenciaMatrizCurricularControle")
@Scope("viewScope")
@Lazy
public class TransferenciaMatrizCurricularControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO;
	private TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatriculaVO;
	private MatriculaVO matricula;
	private UnidadeEnsinoVO unidadeEnsino;
	private PeriodoLetivoVO periodoLetivoVO;
	private GradeCurricularVO gradeCurricularOrigem;
	private GradeCurricularVO gradeCurricularMigrar;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private TurmaVO turmaVO;
	private List listaSelectItemTurma;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;	
	private List<MatriculaVO> listaConsultaAluno;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private List<SelectItem> listaSelectItemPeriodosLetivos;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemMapaEquivalenciaMatriz;
	private List<SelectItem> listaSelectItemPeriodoLetivo;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasPorCorrespondencia;
	private List<SelectItem> listaTurnosMigrarDisciplinas;	
	private Date valorConsultaData;
	private String responsavel_Erro;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private String situacaoMatricula;
	private String cursoTurno;
	private String turnoMigrarDisciplinas;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasPorEquivalenciaVOs;

	public TransferenciaMatrizCurricularControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {
		try {
			setSituacaoMatricula("AT");
			setTransferenciaMatrizCurricularVO(null);
			getTransferenciaMatrizCurricularVO();
			setMatricula(new MatriculaVO());
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaMatrizCurricularForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaMatrizCurricularCons.xhtml");
		}
	}

	public String editar() throws Exception {
		try {
			TransferenciaMatrizCurricularVO obj = (TransferenciaMatrizCurricularVO) context().getExternalContext().getRequestMap().get("transferenciaMatrizCurricularItens");
			obj = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultarPorChavePrimaria(obj.getCodigo(), false, NivelMontarDados.TODOS, getUsuarioLogado());
			setTransferenciaMatrizCurricularVO(obj);
			getTransferenciaMatrizCurricularVO().atualizarEstatisticasTransferenciaMatrizCurricular();
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaMatrizCurricularForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaMatrizCurricularCons.xhtml");
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Historico</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (getTransferenciaMatrizCurricularVO().getCodigo().equals(0)) {
				getFacadeFactory().getTransferenciaMatrizCurricularFacade().incluir(getTransferenciaMatrizCurricularVO(), getUsuarioLogado());
			} else {
				getFacadeFactory().getTransferenciaMatrizCurricularFacade().alterar(getTransferenciaMatrizCurricularVO(), getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaMatrizCurricularForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaMatrizCurricularForm.xhtml");
		}
	}

	public void limparDadosAluno() {
		setMatricula(new MatriculaVO());
		setMatriculaPeriodoVO(null);
		limparMensagem();
	}

	public List<SelectItem> getListaSelectSemestre() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getTransferenciaMatrizCurricularVO().getUnidadeEnsino().getCodigo(), getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getTransferenciaMatrizCurricularVO().getUnidadeEnsino().getCodigo(), getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getCurso().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getTransferenciaMatrizCurricularVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}


    public void consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getCurso().getCodigo(), getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
	
    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            setTurmaVO(obj);
            getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().setCurso(obj.getCurso());
			montarListaSelectItemGradeCurricular();
			this.getTransferenciaMatrizCurricularVO().setGradeMigrar(null);
			this.getTransferenciaMatrizCurricularVO().setGradeOrigem(null);
			this.getTransferenciaMatrizCurricularVO().setListaTransferenciaMatrizCurricularMatricula(null);
			this.getTransferenciaMatrizCurricularVO().atualizarEstatisticasTransferenciaMatrizCurricular();
			this.setListaConsultaCurso(null);
            setListaConsultaTurma(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparTurma() {
        try {
            setTurmaVO(null);            
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        return itens;
    }
    
	public void montarListaSelectItemUnidadeEnsino() throws Exception {
		montarListaSelectItemUnidadeEnsino("");
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		try {
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(consultarUnidadeEnsinoPorNome(prm), "codigo", "nome", true));
		} catch (Exception e) {
			throw e;
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		 itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		// itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaMatrizCurricularCons.xhtml");
	}

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemGradeCurricular();
		montarListaSelectItemMapaEquivalenciaMatrizCurricular();
	}

	public String getResponsavel_Erro() {
		if (responsavel_Erro == null) {
			responsavel_Erro = "";
		}
		return responsavel_Erro;
	}

	public void setResponsavel_Erro(String responsavel_Erro) {
		this.responsavel_Erro = responsavel_Erro;
	}

	public List<SelectItem> getListaSelectItemPeriodosLetivos() {
		if (listaSelectItemPeriodosLetivos == null) {
			listaSelectItemPeriodosLetivos = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodosLetivos;
	}

	public void setListaSelectItemPeriodosLetivos(List<SelectItem> listaSelectItemPeriodosLetivos) {
		this.listaSelectItemPeriodosLetivos = listaSelectItemPeriodosLetivos;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		Uteis.liberarListaMemoria(getTransferenciaMatrizCurricularVO().getListaTransferenciaMatrizCurricularMatricula());
		setTransferenciaMatrizCurricularVO(null);
		responsavel_Erro = null;
		Uteis.liberarListaMemoria(listaSelectItemPeriodosLetivos);
	}

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
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
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
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
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
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
	 * @return the listaConsultaCurso
	 */
	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	/**
	 * @param listaConsultaCurso
	 *            the listaConsultaCurso to set
	 */
	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	/**
	 * @return the campoConsultaCurso
	 */
	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
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
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
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
	 * @return the unidadeEnsino
	 */
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	/**
	 * @param unidadeEnsino
	 *            the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	/**
	 * @return the listaSelectItemGradeCurricularMigrar
	 */
	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>();
		}
		return listaSelectItemGradeCurricular;
	}

	/**
	 * @param listaSelectItemGradeCurricularMigrar
	 *            the listaSelectItemGradeCurricularMigrar to set
	 */
	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	/**
	 * @return the gradeCurricularOrigem
	 */
	public GradeCurricularVO getGradeCurricularOrigem() {
		if (gradeCurricularOrigem == null) {
			gradeCurricularOrigem = new GradeCurricularVO();
		}
		return gradeCurricularOrigem;
	}

	/**
	 * @param gradeCurricularOrigem
	 *            the gradeCurricularOrigem to set
	 */
	public void setGradeCurricularOrigem(GradeCurricularVO gradeCurricularOrigem) {
		this.gradeCurricularOrigem = gradeCurricularOrigem;
	}

	/**
	 * @return the gradeCurricularMigrar
	 */
	public GradeCurricularVO getGradeCurricularMigrar() {
		if (gradeCurricularMigrar == null) {
			gradeCurricularMigrar = new GradeCurricularVO();
		}
		return gradeCurricularMigrar;
	}

	/**
	 * @param gradeCurricularMigrar
	 *            the gradeCurricularMigrar to set
	 */
	public void setGradeCurricularMigrar(GradeCurricularVO gradeCurricularMigrar) {
		this.gradeCurricularMigrar = gradeCurricularMigrar;
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemPeriodoLetivo);
	}

	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
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
	 * @return the periodoLetivoVO
	 */
	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if (periodoLetivoVO == null) {
			periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}

	/**
	 * @param periodoLetivoVO
	 *            the periodoLetivoVO to set
	 */
	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	/**
	 * @param matricula
	 *            the matricula to set
	 */
	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public TransferenciaMatrizCurricularVO getTransferenciaMatrizCurricularVO() {
		if (transferenciaMatrizCurricularVO == null) {
			transferenciaMatrizCurricularVO = new TransferenciaMatrizCurricularVO();
		}
		return transferenciaMatrizCurricularVO;
	}

	public void setTransferenciaMatrizCurricularVO(TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO) {
		this.transferenciaMatrizCurricularVO = transferenciaMatrizCurricularVO;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public void limparCurso() throws Exception {
		try {
			this.getTransferenciaMatrizCurricularVO().setUnidadeEnsinoCurso(null);
			this.getTransferenciaMatrizCurricularVO().setGradeOrigem(null);
			this.getTransferenciaMatrizCurricularVO().setGradeMigrar(null);
			this.getTransferenciaMatrizCurricularVO().setPeriodoLetivoInicial(null);
			this.getTransferenciaMatrizCurricularVO().setPeriodoLetivoFinal(null);
			this.getTransferenciaMatrizCurricularVO().setListaTransferenciaMatrizCurricularMatricula(null);
			this.getTransferenciaMatrizCurricularVO().atualizarEstatisticasTransferenciaMatrizCurricular();
		} catch (Exception e) {
		}
	}

	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getTransferenciaMatrizCurricularVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getGradeMigrarInformada() {
		return !getTransferenciaMatrizCurricularVO().getGradeMigrar().getCodigo().equals(0);
	}

	public boolean getCursoInformado() {
		return !getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getCodigo().equals(0);
	}

	public boolean getGradeOrigemInformada() {
		return !getTransferenciaMatrizCurricularVO().getGradeOrigem().getCodigo().equals(0);
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			getTransferenciaMatrizCurricularVO().setUnidadeEnsinoCurso(obj);
			montarListaSelectItemGradeCurricular();
			this.getTransferenciaMatrizCurricularVO().setGradeMigrar(null);
			this.getTransferenciaMatrizCurricularVO().setGradeOrigem(null);
			this.getTransferenciaMatrizCurricularVO().setListaTransferenciaMatrizCurricularMatricula(null);
			this.getTransferenciaMatrizCurricularVO().atualizarEstatisticasTransferenciaMatrizCurricular();
			this.setListaConsultaCurso(null);
			this.limparTurma();
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<GradeCurricularVO> consultarGradeCurricularCurso() throws Exception {
		Integer cursoBuscar = getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getCurso().getCodigo();
		List<GradeCurricularVO> lista = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(cursoBuscar, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemGradeCurricular() throws Exception {
		SelectItemOrdemValor ordenador = null;
		List<GradeCurricularVO> resultadoConsulta = null;
		Iterator<GradeCurricularVO> i = null;
		try {
			resultadoConsulta = consultarGradeCurricularCurso();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);

			i = resultadoConsulta.iterator();
			objs.add(new SelectItem(0, ""));

			while (i.hasNext()) {
				GradeCurricularVO obj = (GradeCurricularVO) i.next();
				if (!obj.getSituacao().equals("CO")) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getCodigo() + " - " + obj.getNome() + " (" + obj.getSituacao_Apresentar().toUpperCase() + ")"));
				}
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List<SelectItem>) objs, ordenador);

			setListaSelectItemGradeCurricular(objs);
		} catch (Exception e) {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemPeriodoLetivo(Integer grade) throws Exception {
		List<PeriodoLetivoVO> resultadoConsulta = null;
		setListaSelectItemPeriodoLetivo(new ArrayList<SelectItem>(0));
		try {
			resultadoConsulta = getFacadeFactory().getPeriodoLetivoFacade().consultarPorGradeCurricular(grade, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemPeriodoLetivo().add(new SelectItem("", ""));
			for (PeriodoLetivoVO obj : resultadoConsulta) {
				getListaSelectItemPeriodoLetivo().add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void selecionarUnidadeEnsino() {
		try {
			this.getTransferenciaMatrizCurricularVO().setGradeMigrar(null);
			this.getTransferenciaMatrizCurricularVO().setGradeOrigem(null);
			this.getTransferenciaMatrizCurricularVO().setUnidadeEnsinoCurso(null);
			this.getTransferenciaMatrizCurricularVO().setListaTransferenciaMatrizCurricularMatricula(null);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarGradeCurricularMigrar() {
		try {
			montarListaSelectItemMapaEquivalenciaMatrizCurricular();
		} catch (Exception e) {
			setListaSelectItemMapaEquivalenciaMatriz(new ArrayList<SelectItem>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarGradeCurricularOrigem() {
		try {
			montarListaSelectItemPeriodoLetivo(getTransferenciaMatrizCurricularVO().getGradeOrigem().getCodigo());
			this.getTransferenciaMatrizCurricularVO().getGradeMigrar().setCodigo(0);
			this.getTransferenciaMatrizCurricularVO().setListaTransferenciaMatrizCurricularMatricula(null);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the situacaoMatricula
	 */
	public String getSituacaoMatricula() {
		if (situacaoMatricula == null) {
			situacaoMatricula = "AT";
		}
		return situacaoMatricula;
	}

	/**
	 * @param situacaoMatricula
	 *            the situacaoMatricula to set
	 */
	public void setSituacaoMatricula(String situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
	}

	public void validarGradeCurricularDestino() throws Exception {
		if (this.getTransferenciaMatrizCurricularVO().getGradeMigrar().getCodigo().equals(this.getTransferenciaMatrizCurricularVO().getGradeOrigem().getCodigo())) {
			this.getTransferenciaMatrizCurricularVO().getGradeMigrar().setCodigo(0);
			throw new Exception("Matriz Currilar Destino (Migrar) deve ser diferente da Matriz Curricular Origem");
		}
	}

	public void validarPeriodosLetivosBuscarMatricula() throws Exception {
		if (this.getTransferenciaMatrizCurricularVO().getPeriodoLetivoInicial().getPeriodoLetivo().compareTo(this.getTransferenciaMatrizCurricularVO().getPeriodoLetivoFinal().getPeriodoLetivo()) > 0) {
			this.getTransferenciaMatrizCurricularVO().getPeriodoLetivoFinal().setCodigo(0);
			throw new Exception("Período Letivo Final deve ser maior que o período Letivo Inicial");
		}
	}

	public void adicionarGrupoMatriculaTransferencia() {
		try {
			getTransferenciaMatrizCurricularVO().setListaTransferenciaMatrizCurricularMatricula(new ArrayList<TransferenciaMatrizCurricularMatriculaVO>(0));
			validarGradeCurricularDestino();
			validarPeriodosLetivosBuscarMatricula();
			if (!this.getTransferenciaMatrizCurricularVO().getPeriodoLetivoInicial().getCodigo().equals(0)) {
				PeriodoLetivoVO periodoLetivoCarregadoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(this.getTransferenciaMatrizCurricularVO().getPeriodoLetivoInicial().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				this.getTransferenciaMatrizCurricularVO().setPeriodoLetivoInicial(periodoLetivoCarregadoVO);
			} else {
				getTransferenciaMatrizCurricularVO().setPeriodoLetivoInicial(null);
			}
			if (!this.getTransferenciaMatrizCurricularVO().getPeriodoLetivoFinal().getCodigo().equals(0)) {
				PeriodoLetivoVO periodoLetivoCarregadoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(this.getTransferenciaMatrizCurricularVO().getPeriodoLetivoFinal().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				this.getTransferenciaMatrizCurricularVO().setPeriodoLetivoFinal(periodoLetivoCarregadoVO);
			} else {
				getTransferenciaMatrizCurricularVO().setPeriodoLetivoFinal(null);
			}
			List<MatriculaPeriodoVO> matriculasAdicionar = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorGradeCurricularAtualTransferenciaMatrizCurricular(getTransferenciaMatrizCurricularVO().getGradeOrigem().getCodigo(), getTransferenciaMatrizCurricularVO().getUnidadeEnsino().getCodigo(), getTransferenciaMatrizCurricularVO().getPeriodoLetivoInicial().getPeriodoLetivo(), getTransferenciaMatrizCurricularVO().getPeriodoLetivoFinal().getPeriodoLetivo(), getSituacaoMatricula(),  getTurmaVO().getCodigo(), getTransferenciaMatrizCurricularVO().getAnoDisciplinaAprovada(), getTransferenciaMatrizCurricularVO().getSemestreDisciplinaAprovada(), false, getUsuarioLogado());
			if (matriculasAdicionar.isEmpty()) {
				throw new Exception("Não foram encontrados alunos para este parâmetros informados, nesta Unidade e Matriz Curricular de Origem.");
			}
			for (MatriculaPeriodoVO matriculaPeriodoAdicionar : matriculasAdicionar) {
				if (matriculaPeriodoAdicionar.getMatriculaVO().getTurno().getCodigo().equals(this.getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getTurno().getCodigo())) {
					// somente as matriculas do turno selecionado serão
					// adicionadas.
					this.getTransferenciaMatrizCurricularVO().adicionarMatriculaParaTransferenciaMatrizCurricular(matriculaPeriodoAdicionar.getMatriculaVO(), matriculaPeriodoAdicionar, false);
				}
			}
			this.getTransferenciaMatrizCurricularVO().atualizarEstatisticasTransferenciaMatrizCurricular();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO, true);
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarMatriculaTransferencia() {
		try {
			validarGradeCurricularDestino();
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatricula(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (!getMatricula().getTurno().getCodigo().equals(this.getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getTurno().getCodigo())) {
				throw new Exception("Esta matrícula não pertence ao Turno selecionado para esta Transferência de Matriz. Turno Matrícula: " + getMatricula().getTurno().getNome());
			}
			if (!getMatricula().getCurso().getCodigo().equals(this.getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getCurso().getCodigo())) {
				throw new Exception("Esta matrícula não pertence ao Curso selecionado para esta Transferência de Matriz. Curso da Matrícula: " + getMatricula().getCurso().getNome());
			}
			this.getTransferenciaMatrizCurricularVO().adicionarMatriculaParaTransferenciaMatrizCurricular(this.getMatricula(), this.getMatriculaPeriodoVO(), true);
			this.getTransferenciaMatrizCurricularVO().atualizarEstatisticasTransferenciaMatrizCurricular();
			this.setMatricula(null);
			this.setMatriculaPeriodoVO(null);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO objAluno = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setMatricula(objAluno);
		getFacadeFactory().getMatriculaFacade().carregarDados(getMatricula(), NivelMontarDados.BASICO, getUsuarioLogado());
		setValorConsultaAluno("");
		setCampoConsultaAluno("");
		getListaConsultaAluno().clear();
	}

	public void consultarAlunoPorMatricula() {
		try {
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatricula(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatricula(new MatriculaVO());
		}
	}

	// public void inicializarDadosMatricula() throws Exception {
	// if (getMatricula().getMatricula().equals("")) {
	// setMatricula(null);
	// setMatriculaPeriodoVO(null);
	// setCurso(null);
	// setUnidadeEnsino(null);
	// setGradeCurricularOrigem(null);
	// return;
	// }
	// getFacadeFactory().getMatriculaFacade().carregarDados(getMatricula(),
	// NivelMontarDados.TODOS, getUsuarioLogado());
	// setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(getMatricula().getMatricula(),
	// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
	// if (getMatriculaPeriodoVO().getCodigo() == 0) {
	// throw new Exception("Aluno de matrícula " + getMatricula().getMatricula()
	// + " não encontrado. Verifique se o número de matrícula está correto.");
	// }
	// getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(getMatriculaPeriodoVO(),
	// NivelMontarDados.TODOS, getConfiguracaoFinanceiroPadraoSistema(),
	// getUsuarioLogado());
	// getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(this.getMatricula(),
	// this.getMatriculaPeriodoVO(), getUsuarioLogado());
	// getMatricula().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(),
	// false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
	// getMatriculaPeriodoVO().setMatriculaVO(getMatricula());
	// this.setUnidadeEnsino(getMatricula().getUnidadeEnsino());
	// this.setCurso(getMatricula().getCurso());
	// this.setTurno(getMatricula().getTurno());
	// this.setGradeCurricularOrigem(getMatriculaPeriodoVO().getGradeCurricular());
	// montarListaSelectItemGradeCurricular();
	// }

	public List<SelectItem> getTipoSituacaoMatricula() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AT", "Ativa"));
		itens.add(new SelectItem("TR", "Trancada"));
		itens.add(new SelectItem("PR", "Pré-matrículado"));
		itens.add(new SelectItem("TO", "Todas Anteriores"));
		return itens;
	}

	public void selecionarMatriculaListaTransferenciaMatrizCurricular() {
		try {
			TransferenciaMatrizCurricularMatriculaVO matriculaSelecionar = (TransferenciaMatrizCurricularMatriculaVO) context().getExternalContext().getRequestMap().get("matriculaTransferenciaItens");
			setTransferenciaMatrizCurricularMatriculaVO(matriculaSelecionar);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerMatriculaListaTransferenciaMatrizCurricular() {
		try {
			TransferenciaMatrizCurricularMatriculaVO matriculaRemover = (TransferenciaMatrizCurricularMatriculaVO) context().getExternalContext().getRequestMap().get("matriculaTransferenciaItens");
			if (!matriculaRemover.getPodeSerRemovida()) {
				throw new Exception("Esta matrícula não pode mais ser removida da lista desta Transferência de Matriz Curricular");
			}
			this.getTransferenciaMatrizCurricularVO().getListaTransferenciaMatrizCurricularMatricula().remove(matriculaRemover);
			this.getTransferenciaMatrizCurricularVO().atualizarEstatisticasTransferenciaMatrizCurricular();
			setMensagemID("msg_dados_removidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void cancelarTransferenciaMatriculaEspecifica() {
		ConsistirException consistirException =  new ConsistirException();
		StringBuilder matriculas = new StringBuilder("");
		TransferenciaMatrizCurricularMatriculaVO matriculaCancelar = (TransferenciaMatrizCurricularMatriculaVO) context().getExternalContext().getRequestMap().get("matriculaTransferenciaItens");
		try {
			matriculaCancelar.setResultadoProcessamento("");
			matriculaCancelar.setNrAlertasCriticos(0);
			getFacadeFactory().getTransferenciaMatrizCurricularFacade().cancelarMigracaoMatrizCurricular(this.getTransferenciaMatrizCurricularVO(), matriculaCancelar, getUsuarioLogado());
			// se cancelou entao nao vamos mais ter disciplinas cursando por correspodencia.
			// Entao temos que atualizar o atributo, para que o usuario possa perceber estas
			// alteracoes
			TransferenciaMatrizCurricularMatriculaVO matriculaAtualizada = getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().consultarPorChavePrimaria(matriculaCancelar.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			matriculaCancelar.setNrDisciplinasCursandoPorCorrespondencia(matriculaAtualizada.getNrDisciplinasCursandoPorCorrespondencia());
			matriculaCancelar.setNrDisciplinasCursandoPorEquivalencia(matriculaAtualizada.getNrDisciplinasCursandoPorEquivalencia());
			if(matriculas.length() > 0) {
				matriculas.append(", ");
			}
			if(matriculaAtualizada.getSituacao().equals("CA")) {
				matriculas.append(matriculaCancelar.getMatriculaVO().getMatricula());
			}else {
				consistirException.getListaMensagemErro().add("Não foi possível realizar o cancelamento da transferência da matrícula "+matriculaCancelar.getMatriculaVO().getMatricula()+", vejo a mensagem específica na propria linha da matrícula.");
			}
			setMensagemID("msg_ProcessamentoRealizado");
		} catch (Exception e) {
			consistirException.getListaMensagemErro().add("Não foi possível realizar o cancelamento da transferência da matrícula "+matriculaCancelar.getMatriculaVO().getMatricula()+", vejo a mensagem específica na propria linha da matrícula.");
			matriculaCancelar.setResultadoProcessamento(e.getMessage());
			matriculaCancelar.setNrAlertasCriticos(1);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		if(matriculas.length() > 0) {
			setMensagemID("A(s) MATRÍCULA(S) "+matriculas+" foi(ram) CANCELAMENTA(S) com sucesso.", Uteis.SUCESSO, true);
		}
		if(!consistirException.getListaMensagemErro().isEmpty()) {
			setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
		}else if(matriculas.length() ==0) {
			setMensagemID("Não foram encontradas MATRÍCULAS com a situação Realizada Com Sucesso aptas para o CANCELAMENTO.", Uteis.ALERTA, true);
		}
	}

	public void processarTransferenciaTodasMatriculas() {
		try {
			getFacadeFactory().getTransferenciaMatrizCurricularFacade().realizarMigracaoMatrizCurricular(this.getTransferenciaMatrizCurricularVO(), getUsuarioLogado());
			boolean processarParalelismo = true;
			int contador = 0;
			if (processarParalelismo) {
				contador = getFacadeFactory().getTransferenciaMatrizCurricularFacade().processarTransferenciaTodasMatriculasParalelismo(getTransferenciaMatrizCurricularVO(), getTransferenciaMatrizCurricularVO().getListaTransferenciaMatrizCurricularMatricula(), getUsuarioLogado());
			} else {
				for (TransferenciaMatrizCurricularMatriculaVO matriculaProcessar : getTransferenciaMatrizCurricularVO().getListaTransferenciaMatrizCurricularMatricula()) {
					if (matriculaProcessar.getPodeSerProcessada()) {
						try {
							getFacadeFactory().getTransferenciaMatrizCurricularFacade().realizarMigracaoMatrizCurricularMatricula(getTransferenciaMatrizCurricularVO(), matriculaProcessar, getUsuarioLogado());
							contador++;
						} catch (Exception e) {
							getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().persistir(matriculaProcessar, getUsuarioLogado());
							getFacadeFactory().getTransferenciaMatrizCurricularFacade().inicializarDadosMatriculaProcessar(matriculaProcessar);
						}
					}
				}
			}
			// gravando o data/hora que foi finalizado o processamento da
			// tranferencia e quantas matrículas foram processadas.
			getTransferenciaMatrizCurricularVO().adicionarHistoricoResultadoProcessamento(new Date(), getUsuarioLogado().getNome_Apresentar(), "   -> Finalizado processamento de transferência de matriz curricular. " + contador + " matrícula(s) transferida(s).");
			getFacadeFactory().getTransferenciaMatrizCurricularFacade().gravar(getTransferenciaMatrizCurricularVO(), getUsuarioLogado());
			TransferenciaMatrizCurricularVO obj = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultarPorChavePrimaria(getTransferenciaMatrizCurricularVO().getCodigo(), false, NivelMontarDados.TODOS, getUsuarioLogado());
			setTransferenciaMatrizCurricularVO(obj);
			getTransferenciaMatrizCurricularVO().atualizarEstatisticasTransferenciaMatrizCurricular();
			setMensagemID("");
			setMensagem(UteisJSF.internacionalizar("msg_TransferenciaMatrizCurricular_ProcessamentoRealizado").replace("{0}", getTransferenciaMatrizCurricularVO().getNrMatriculasMigradas().toString()).replace("{1}", getTransferenciaMatrizCurricularVO().getNrMatriculasComErros().toString()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void processarTransferenciaMatriculaEspecifica() throws Exception {
		TransferenciaMatrizCurricularMatriculaVO matriculaProcessar = (TransferenciaMatrizCurricularMatriculaVO) context().getExternalContext().getRequestMap().get("matriculaTransferenciaItens");
		try {
			if (!matriculaProcessar.getPodeSerProcessada()) {
				throw new Exception("Esta transferência de matrícula não pode mais ser processada");
			}			
			getFacadeFactory().getTransferenciaMatrizCurricularFacade().realizarMigracaoMatrizCurricular(this.getTransferenciaMatrizCurricularVO(), getUsuarioLogado());
			getFacadeFactory().getTransferenciaMatrizCurricularFacade().realizarMigracaoMatrizCurricularMatricula(getTransferenciaMatrizCurricularVO(), matriculaProcessar, getUsuarioLogado());
			getTransferenciaMatrizCurricularVO().adicionarHistoricoResultadoProcessamento(new Date(), getUsuarioLogado().getNome_Apresentar(), "   -> Finalizado processamento de transferência de matriz curricular.  1 matrícula transferida.");
			getFacadeFactory().getTransferenciaMatrizCurricularFacade().gravar(getTransferenciaMatrizCurricularVO(), getUsuarioLogado());
			getTransferenciaMatrizCurricularVO().atualizarEstatisticasTransferenciaMatrizCurricular();
			// atualizando o total de disciplinas cursadas por correspodencia.
			TransferenciaMatrizCurricularMatriculaVO matriculaAtualizada = getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().consultarPorChavePrimaria(matriculaProcessar.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			matriculaProcessar.setNrDisciplinasCursandoPorCorrespondencia(matriculaAtualizada.getNrDisciplinasCursandoPorCorrespondencia());
			matriculaProcessar.setNrDisciplinasCursandoPorEquivalencia(matriculaAtualizada.getNrDisciplinasCursandoPorEquivalencia());
			setMensagemID("");
			setMensagem(UteisJSF.internacionalizar("msg_TransferenciaMatrizCurricular_ProcessamentoRealizado").replace("{0}", getTransferenciaMatrizCurricularVO().getNrMatriculasMigradas().toString()).replace("{1}", getTransferenciaMatrizCurricularVO().getNrMatriculasComErros().toString()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().persistir(matriculaProcessar, getUsuarioLogado());
			getFacadeFactory().getTransferenciaMatrizCurricularFacade().inicializarDadosMatriculaProcessar(matriculaProcessar);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultaRapidaPorCodigoTransferencia(new Integer(valorInt), true, NivelMontarDados.BASICO, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoCurso")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultaRapidaPorCodigoCurso(new Integer(valorInt), true, NivelMontarDados.BASICO, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				objs = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultaRapidaPorNomeCurso(getControleConsulta().getValorConsulta(), true, NivelMontarDados.BASICO, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeAluno")) {
				objs = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultaRapidaPorNomeAluno(getControleConsulta().getValorConsulta(), true, NivelMontarDados.BASICO, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
				objs = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultaRapidaPorRegistroAcademicoAluno(getControleConsulta().getValorConsulta(), true, NivelMontarDados.BASICO, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				objs = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), true, NivelMontarDados.BASICO, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date dataValida = Uteis.getDateTime(getValorConsultaData(), 0, 0, 0);
				objs = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultaRapidaPorDataTransferencia(dataValida, true, NivelMontarDados.BASICO, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaMatrizCurricularCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaMatrizCurricularCons.xhtml");
		}
	}

	public Boolean getApresentarCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		} else {
			return false;
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomeCurso", "Nome Curso"));
		itens.add(new SelectItem("codigoCurso", "Código Curso"));
		itens.add(new SelectItem("data", "Data Transferência"));
		itens.add(new SelectItem("codigo", "Código Transferência"));
		itens.add(new SelectItem("nomeAluno", "Nome Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		return itens;
	}

	/**
	 * @return the valorConsultaData
	 */
	public Date getValorConsultaData() {
		if (valorConsultaData == null) {
			valorConsultaData = new Date();
		}
		return valorConsultaData;
	}

	/**
	 * @param valorConsultaData
	 *            the valorConsultaData to set
	 */
	public void setValorConsultaData(Date valorConsultaData) {
		this.valorConsultaData = valorConsultaData;
	}

	/**
	 * @return the listaSelectItemMapaEquivalenciaMatriz
	 */
	public List<SelectItem> getListaSelectItemMapaEquivalenciaMatriz() {
		if (listaSelectItemMapaEquivalenciaMatriz == null) {
			listaSelectItemMapaEquivalenciaMatriz = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemMapaEquivalenciaMatriz;
	}

	/**
	 * @param listaSelectItemMapaEquivalenciaMatriz
	 *            the listaSelectItemMapaEquivalenciaMatriz to set
	 */
	public void setListaSelectItemMapaEquivalenciaMatriz(List<SelectItem> listaSelectItemMapaEquivalenciaMatriz) {
		this.listaSelectItemMapaEquivalenciaMatriz = listaSelectItemMapaEquivalenciaMatriz;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void montarListaSelectItemMapaEquivalenciaMatrizCurricular() throws Exception {
		SelectItemOrdemValor ordenador = null;
		List<MapaEquivalenciaMatrizCurricularVO> resultadoConsulta = null;
		Iterator<MapaEquivalenciaMatrizCurricularVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().consultarPorCodigoGradeCurricular(this.getTransferenciaMatrizCurricularVO().getGradeMigrar().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				MapaEquivalenciaMatrizCurricularVO obj = (MapaEquivalenciaMatrizCurricularVO) i.next();
				if (!obj.getSituacaoEmConstrucao()) {
					objs.add(new SelectItem(obj.getCodigo(), obj.getCodigo() + " - " + obj.getDescricao() + " (" + obj.getSituacao_Apresentar().toUpperCase() + ")"));
				}
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemMapaEquivalenciaMatriz(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * @return the transferenciaMatrizCurricularMatriculaVO
	 */
	public TransferenciaMatrizCurricularMatriculaVO getTransferenciaMatrizCurricularMatriculaVO() {
		if (transferenciaMatrizCurricularMatriculaVO == null) {
			transferenciaMatrizCurricularMatriculaVO = new TransferenciaMatrizCurricularMatriculaVO();
		}
		return transferenciaMatrizCurricularMatriculaVO;
	}

	/**
	 * @param transferenciaMatrizCurricularMatriculaVO
	 *            the transferenciaMatrizCurricularMatriculaVO to set
	 */
	public void setTransferenciaMatrizCurricularMatriculaVO(TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatriculaVO) {
		this.transferenciaMatrizCurricularMatriculaVO = transferenciaMatrizCurricularMatriculaVO;
	}

	public boolean getIsApresentarCampoUtilizarAnoSemestreAtualDisciplinaAprovada() {
		return getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getCurso().getPeriodicidade().equals("SE");
	}

	/**
	 * @return the cursoTurno
	 */
	public String getCursoTurno() {
		cursoTurno = this.getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getCurso().getNome();
		if (!this.getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getTurno().getNome().equals("")) {
			cursoTurno = cursoTurno + " - " + this.getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getTurno().getNome();
		}
		return cursoTurno;
	}

	/**
	 * @param cursoTurno
	 *            the cursoTurno to set
	 */
	public void setCursoTurno(String cursoTurno) {
		this.cursoTurno = cursoTurno;
	}


    public List getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList(0);
        }
        return listaSelectItemTurma;
    }

    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
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
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaDisciplinasPorCorrespondencia() {
		if (listaDisciplinasPorCorrespondencia == null) {
			listaDisciplinasPorCorrespondencia = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaDisciplinasPorCorrespondencia;
	}

	public List<SelectItem> getListaTurnosMigrarDisciplinas() {
		if (listaTurnosMigrarDisciplinas == null) {
			listaTurnosMigrarDisciplinas = new ArrayList<SelectItem>(0);
		}
		return listaTurnosMigrarDisciplinas;
	}

	public void setListaTurnosMigrarDisciplinas(List<SelectItem> listaTurnosMigrarDisciplinas) {
		this.listaTurnosMigrarDisciplinas = listaTurnosMigrarDisciplinas;
	}	

	public void setListaDisciplinasPorCorrespondencia(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasPorCorrespondencia) {
		this.listaDisciplinasPorCorrespondencia = listaDisciplinasPorCorrespondencia;
	}

	public String getTurnoMigrarDisciplinas() {
		if (turnoMigrarDisciplinas == null) {
			turnoMigrarDisciplinas = "";
		}
		return turnoMigrarDisciplinas;
	}

	public void setTurnoMigrarDisciplinas(String turnoMigrarDisciplinas) {
		this.turnoMigrarDisciplinas = turnoMigrarDisciplinas;
	}
	
	public void consultarTurnosValidosParaCursoAluno() {
		try {
			List<UnidadeEnsinoCursoVO> objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurnoLista(getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getCurso().getCodigo(), getTransferenciaMatrizCurricularVO().getUnidadeEnsino().getCodigo(), 0, getUsuarioLogado());
			getListaTurnosMigrarDisciplinas().clear();
			for (UnidadeEnsinoCursoVO obj : objs) { 
				if (obj.getSituacaoCurso().equals("AT")) {
					getListaTurnosMigrarDisciplinas().add(new SelectItem(obj.getTurno().getCodigo(), obj.getTurno().getNome()));
				}
			}
			// ja inicializando com o turno atual. assim o usuario irá alterar somente se desejar.
			setTurnoMigrarDisciplinas(getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getTurno().getCodigo().toString()); 
		} catch (Exception e) {
			setListaTurnosMigrarDisciplinas(null);
		}
	}
	
	public void prepararMigrarDisciplinasNavegandoTelaMatricula() {
		prepararMigrarDisciplinasCursandoPorCorrespondencia();
	}
	
	public void prepararMigrarDisciplinasCursandoPorCorrespondencia() {
		selecionarMatriculaListaTransferenciaMatrizCurricular();
		consultarTurnosValidosParaCursoAluno();
		try {
			setListaDisciplinasPorCorrespondencia(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodoDisciplinasCursandoPorCorrespodencia(getTransferenciaMatrizCurricularMatriculaVO().getMatriculaPeriodoUltimoPeriodoVO().getCodigo(), false, getUsuarioLogado()));
			setListaDisciplinasPorEquivalenciaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodoDisciplinasCursandoPorEquivalencia(getTransferenciaMatrizCurricularMatriculaVO().getMatriculaPeriodoUltimoPeriodoVO().getCodigo(), false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaDisciplinasPorCorrespondencia(null); 
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void iniciarProcessoMigracaoDisciplinasNavegandoTelaMatricula() {		
		iniciarProcessoMigracaoDisciplinasPorCorrespodencia();
		iniciarProcessoMigracaoDisciplinasPorEquivalencia();
		
		getTransferenciaMatrizCurricularMatriculaVO().getMatriculaPeriodoUltimoPeriodoVO().setMatricula(getTransferenciaMatrizCurricularMatriculaVO().getMatriculaVO().getMatricula());
		getTransferenciaMatrizCurricularMatriculaVO().getMatriculaPeriodoUltimoPeriodoVO().setMatriculaVO(getTransferenciaMatrizCurricularMatriculaVO().getMatriculaVO());
		TurnoVO turnoMatricula = null;
		if (!getTurnoMigrarDisciplinas().equals(getTransferenciaMatrizCurricularVO().getUnidadeEnsinoCurso().getTurno().getCodigo().toString())) {
			// se entramos aqui é por que o usuário alterou o turno da matriculaPeriodo. Logo,
			// esta alteracao terá que ser refletida no momento de editar a MatriculaPeriodo.
			turnoMatricula = new TurnoVO();
			turnoMatricula.setCodigo(!Uteis.isAtributoPreenchido(getTurnoMigrarDisciplinas()) ? 0 : new Integer(getTurnoMigrarDisciplinas()));
		}
		getTransferenciaMatrizCurricularMatriculaVO().setTurnoMigrarMatricula(turnoMatricula);
		getTransferenciaMatrizCurricularMatriculaVO().adicionarHistoricoResultadoProcessamento(new Date(), getUsuarioLogado().getNome_Apresentar(), "Iniciando Migração das Disciplinas Cursando por Correspondência para NOVA MATRIZ / NOVA TURMA");
		context().getExternalContext().getSessionMap().put("transferenciaMatrizCurricularMatricula", getTransferenciaMatrizCurricularMatriculaVO());
		removerControleMemoriaFlash("RenovarMatriculaControle");
	}
	
	public void iniciarProcessoMigracaoDisciplinasPorCorrespodencia() {
		// Se a lista estiver vazia o que será resolvido será as equivalencias e não as correspondências 
		if (getListaDisciplinasPorCorrespondencia().isEmpty()) {
			return;
		}
		getTransferenciaMatrizCurricularMatriculaVO().setEliminandoDisciplinasCursandoPorCorrespodencia(Boolean.TRUE);
		getTransferenciaMatrizCurricularMatriculaVO().setListaDisciplinasPorCorrespondencia(getListaDisciplinasPorCorrespondencia());
	}
	
	public void iniciarProcessoMigracaoDisciplinasPorEquivalencia() {
		// Se o número de disciplinas cursando por equivalencia estiver vazia o que será resolvido será as correspondências e não as equivalências
		if (getListaDisciplinasPorEquivalenciaVOs().isEmpty()) {
			return;
		}
		getTransferenciaMatrizCurricularMatriculaVO().setEliminandoDisciplinasCursandoPorEquivalencia(Boolean.TRUE);
		getTransferenciaMatrizCurricularMatriculaVO().setListaDisciplinasPorEquivalenciaVOs(getListaDisciplinasPorEquivalenciaVOs());
	}

	/**
	 * Método responsavel 
	 * @author Otimize - 30 de set de 2016
	 */
	@PostConstruct
	public void retornarEdicaoMatriculaPeriodoEliminarCorrespodencia() {
		if (context().getExternalContext().getSessionMap().get("retornarTransferenciaMatricula") != null) {
			TransferenciaMatrizCurricularMatriculaVO transferenciaMatrizCurricularMatriculaVO = (TransferenciaMatrizCurricularMatriculaVO)context().getExternalContext().getSessionMap().get("retornarTransferenciaMatricula");
			context().getExternalContext().getSessionMap().put("retornarTransferenciaMatricula", null);
			try {
				TransferenciaMatrizCurricularVO obj = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultarPorChavePrimaria(transferenciaMatrizCurricularMatriculaVO.getTransferenciaMatrizCurricularVO().getCodigo(), false, NivelMontarDados.TODOS, getUsuarioLogado());
				setTransferenciaMatrizCurricularVO(obj);
				getTransferenciaMatrizCurricularVO().atualizarEstatisticasTransferenciaMatrizCurricular();
				inicializarListasSelectItemTodosComboBox();
				setMensagemID("msg_dados_editar");
			} catch (Exception e) {
				novo();
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		} 
	}
    
	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaDisciplinasPorEquivalenciaVOs() {
		if (listaDisciplinasPorEquivalenciaVOs == null) {
			listaDisciplinasPorEquivalenciaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return listaDisciplinasPorEquivalenciaVOs;
	}

	public void setListaDisciplinasPorEquivalenciaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasPorEquivalenciaVOs) {
		this.listaDisciplinasPorEquivalenciaVOs = listaDisciplinasPorEquivalenciaVOs;
	}
	
	public void cancelarTodasTransferenciaMatricula() throws Exception {
		limparMensagem();
		ConsistirException consistirException =  new ConsistirException();
		StringBuilder matriculas = new StringBuilder("");
		for (TransferenciaMatrizCurricularMatriculaVO matriculaCancelar : getTransferenciaMatrizCurricularVO().getListaTransferenciaMatrizCurricularMatricula()) {
			if (matriculaCancelar.getPodeSerCancelada()) {
				try {
					matriculaCancelar.setResultadoProcessamento("");
					matriculaCancelar.setNrAlertasCriticos(0);
					getFacadeFactory().getTransferenciaMatrizCurricularFacade().cancelarMigracaoMatrizCurricular(this.getTransferenciaMatrizCurricularVO(), matriculaCancelar, getUsuarioLogado());
					// se cancelou entao nao vamos mais ter disciplinas cursando por correspodencia.
					// Entao temos que atualizar o atributo, para que o usuario possa perceber estas
					// alteracoes
					TransferenciaMatrizCurricularMatriculaVO matriculaAtualizada = getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().consultarPorChavePrimaria(matriculaCancelar.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					matriculaCancelar.setNrDisciplinasCursandoPorCorrespondencia(matriculaAtualizada.getNrDisciplinasCursandoPorCorrespondencia());					
					matriculaCancelar.setNrDisciplinasCursandoPorEquivalencia(matriculaAtualizada.getNrDisciplinasCursandoPorEquivalencia());
					if(matriculas.length() > 0) {
						matriculas.append(", ");
					}
					if(matriculaAtualizada.getSituacao().equals("CA")) {
						matriculas.append(matriculaCancelar.getMatriculaVO().getMatricula());
					}else {
						consistirException.getListaMensagemErro().add("Não foi possível realizar o cancelamento da transferência da matrícula "+matriculaCancelar.getMatriculaVO().getMatricula()+", vejo a mensagem específica na propria linha da matrícula.");
					}
				} catch (Exception e) {
					consistirException.getListaMensagemErro().add("Não foi possível realizar o cancelamento da transferência da matrícula "+matriculaCancelar.getMatriculaVO().getMatricula()+", vejo a mensagem específica na propria linha da matrícula.");
					matriculaCancelar.setResultadoProcessamento(e.getMessage());
					matriculaCancelar.setNrAlertasCriticos(1);
				}
			}
		}
		if(matriculas.length() > 0) {
			setMensagemID("A(s) MATRÍCULA(S) "+matriculas+" foi(ram) CANCELAMENTA(S) com sucesso.", Uteis.SUCESSO, true);
		}
		if(!consistirException.getListaMensagemErro().isEmpty()) {
			setConsistirExceptionMensagemDetalhada("msg_erro", consistirException, Uteis.ERRO);
		}else if(matriculas.length() ==0) {
			setMensagemID("Não foram encontradas MATRÍCULAS com a situação Realizada Com Sucesso aptas para o CANCELAMENTO.", Uteis.ALERTA, true);
		}
		
	}
	
	public void montarListaResultadoProcessamento() {
		try {
			if (Uteis.isAtributoPreenchido(getTransferenciaMatrizCurricularVO().getResultadoProcessamento())) {
				getTransferenciaMatrizCurricularVO().getListaResultadoProcessamento().clear();
				getTransferenciaMatrizCurricularVO().getListaResultadoProcessamento().addAll(Splitter.fixedLength(1200).omitEmptyStrings().trimResults().splitToList(getTransferenciaMatrizCurricularVO().getResultadoProcessamento()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
}
