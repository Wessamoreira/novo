package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.EmailTurmaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Controller("EmailTurmaRelControle")
@Scope("viewScope")
public class EmailTurmaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private TurmaVO turma;
	private DisciplinaVO disciplina;
	private String ano;
	private String semestre;
	private String emails;
	private boolean apresentarEmail;
	private boolean alunoReposicao = false;
	private String campoConsultaDisciplinaTurma;
	private String valorConsultaDisciplinaTurma;
	private List<DisciplinaVO> listaConsultaDisciplinaTurma;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;

	public EmailTurmaRelControle() throws Exception {
		inicializarListasSelectItemTodosComboBox();
		apresentarEmail = Boolean.FALSE;
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void imprimirPDF() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EmailTurmaRelControle", "Inicializando Geração de Relatório Email Turma", "Emitindo Relatório");
			setEmails("");
			getFacadeFactory().getEmailTurmaRelFacade().validarDados(getUnidadeEnsinoVO().getCodigo(), getTurma(), getAno(), getSemestre());
			EmailTurmaRelVO emailTurmaRelVO = getFacadeFactory().getEmailTurmaRelFacade().criarObjeto(0, turma.getCodigo(), getAno(), getSemestre(), getAlunoReposicao(), getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getFiltroRelatorioAcademicoVO());
			setEmails(emailTurmaRelVO.getEmail());
			if (!getEmails().equals("")) {
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			setApresentarEmail(Boolean.TRUE);
			registrarAtividadeUsuario(getUsuarioLogado(), "EmailTurmaRelControle", "Finalizando Geração de Relatório Email Turma", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			inicializarListasSelectItemTodosComboBox();
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox
	 * denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultarTurma() {
		try {
			super.consultar();
			if (getUnidadeEnsinoVO().getCodigo() == 0) {
				throw new Exception("Selecione a Unidade de Ensino.");
			}
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}

	}

	public void selecionarTurma() throws Exception {
		setTurma((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
		setCampoConsultaTurma("");
		setValorConsultaTurma("");
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));

	}

	public void selecionarDisciplinaTurma() throws Exception {
		setDisciplina((DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaTurmaItens"));
		setListaConsultaDisciplinaTurma(new ArrayList<DisciplinaVO>(0));
		setValorConsultaDisciplinaTurma("");
		setCampoConsultaDisciplinaTurma("");
		getListaConsultaDisciplinaTurma().clear();
	}

	public void limparDadosDisciplinaTurma() throws Exception {
		setDisciplina(new DisciplinaVO());
		setMensagemID("msg_entre_prmconsulta");
	}

	public void consultarDisciplinaTurmaRich() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplinaTurma().equals("codigo")) {
				if (getValorConsultaDisciplinaTurma().equals("")) {
					setValorConsultaDisciplinaTurma("0");
				}
				if (getValorConsultaDisciplinaTurma().trim() != null || !getValorConsultaDisciplinaTurma().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplinaTurma().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplinaTurma());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), getTurma().getCurso().getCodigo(), getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplinaTurma().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(getValorConsultaDisciplinaTurma(), getUnidadeEnsinoLogado().getCodigo(), getTurma().getCurso().getCodigo(), getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDisciplinaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplinaTurma(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void limparDadosTurma() {
		getTurma().setCodigo(0);
		getTurma().setIdentificadorTurma("");
		setApresentarEmail(Boolean.FALSE);
		setEmails("");
		setMensagemID("msg_entre_prmrelatorio");
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void inicializarListasSelectItemTodosComboBox() {
		// montarListaSelectItemOrdenacao();
		montarListaSelectItemUnidadeEnsino();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			getListaSelectItemUnidadeEnsino();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
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
	 * @return the listaConsultaTurma
	 */
	public List<TurmaVO> getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	/**
	 * @param listaConsultaTurma
	 *            the listaConsultaTurma to set
	 */
	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	/**
	 * @return the valorConsultaTurma
	 */
	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	/**
	 * @param valorConsultaTurma
	 *            the valorConsultaTurma to set
	 */
	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	/**
	 * @return the campoConsultaTurma
	 */
	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	/**
	 * @param campoConsultaTurma
	 *            the campoConsultaTurma to set
	 */
	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	/**
	 * @return the unidadeEnsinoVO
	 */
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
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
	 * @return the ano
	 */
	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	/**
	 * @param ano
	 *            the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	/**
	 * @return the semestre
	 */
	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	/**
	 * @param semestre
	 *            the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	/**
	 * @return the emails
	 */
	public String getEmails() {
		return emails;
	}

	/**
	 * @param emails
	 *            the emails to set
	 */
	public void setEmails(String emails) {
		this.emails = emails;
	}

	/**
	 * @return the apresentarEmail
	 */
	public boolean getApresentarEmail() {
		return apresentarEmail;
	}

	/**
	 * @param apresentarEmail
	 *            the apresentarEmail to set
	 */
	public void setApresentarEmail(boolean apresentarEmail) {
		this.apresentarEmail = apresentarEmail;
	}

	/**
	 * @return the campoConsultaDisciplinaTurma
	 */
	public String getCampoConsultaDisciplinaTurma() {
		return campoConsultaDisciplinaTurma;
	}

	/**
	 * @param campoConsultaDisciplinaTurma
	 *            the campoConsultaDisciplinaTurma to set
	 */
	public void setCampoConsultaDisciplinaTurma(String campoConsultaDisciplinaTurma) {
		this.campoConsultaDisciplinaTurma = campoConsultaDisciplinaTurma;
	}

	/**
	 * @return the valorConsultaDisciplinaTurma
	 */
	public String getValorConsultaDisciplinaTurma() {
		return valorConsultaDisciplinaTurma;
	}

	/**
	 * @param valorConsultaDisciplinaTurma
	 *            the valorConsultaDisciplinaTurma to set
	 */
	public void setValorConsultaDisciplinaTurma(String valorConsultaDisciplinaTurma) {
		this.valorConsultaDisciplinaTurma = valorConsultaDisciplinaTurma;
	}

	/**
	 * @return the listaConsultaDisciplinaTurma
	 */
	public List<DisciplinaVO> getListaConsultaDisciplinaTurma() {
		return listaConsultaDisciplinaTurma;
	}

	/**
	 * @param listaConsultaDisciplinaTurma
	 *            the listaConsultaDisciplinaTurma to set
	 */
	public void setListaConsultaDisciplinaTurma(List<DisciplinaVO> listaConsultaDisciplinaTurma) {
		this.listaConsultaDisciplinaTurma = listaConsultaDisciplinaTurma;
	}

	/**
	 * @return the disciplina
	 */
	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	/**
	 * @param disciplina
	 *            the disciplina to set
	 */
	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	/**
	 * @return the alunoReposicao
	 */
	public boolean getAlunoReposicao() {
		return alunoReposicao;
	}

	/**
	 * @param alunoReposicao
	 *            the alunoReposicao to set
	 */
	public void setAlunoReposicao(boolean alunoReposicao) {
		this.alunoReposicao = alunoReposicao;
	}

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}

}
