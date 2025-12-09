package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.FichaAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FichaAtualizacaoCadastralRel;

@Controller("FichaAtualizacaoCadastralRelControle")
@Scope("viewScope")
public class FichaAtualizacaoCadastralRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private TurmaVO turmaVO;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private MatriculaVO matriculaVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemCurso;
	private List<SelectItem> listaSelectItemTurma;
	private String filtro;
	private Boolean mostrarPanelAluno;
	private Boolean mostrarPanelOutros;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private String anoConsulta ;
	private String semestreConsulta;
	private Boolean filtrarPorRegistroAcademico;

	public FichaAtualizacaoCadastralRelControle() throws Exception {
		incializarDados();
		setMensagemID("msg_entre_prmconsulta");
	}

	private void incializarDados() {
		montarListaSelectItemUnidadeEnsino();
	}

	public void imprimirPDF() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "FichaAtualizacaoCadastralRelControle", "Inicializando Geração de Relatório Ficha Atualização Cadastral", "Emitindo Relatório");
			List<FichaAlunoRelVO> fichaAlunoRelVOs = getFacadeFactory().getFichaAtualizacaoCadastralRelFacade().criarObjeto(getMatriculaVO(), getTurmaVO(), getUnidadeEnsinoCursoVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getFiltro(), getAnoConsulta(), getSemestreConsulta());
			if (!fichaAlunoRelVOs.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(FichaAtualizacaoCadastralRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(FichaAtualizacaoCadastralRel.getCaminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Ficha de Atualização Cadastral e Renovação de Matrícula");
				getSuperParametroRelVO().setListaObjetos(fichaAlunoRelVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(FichaAtualizacaoCadastralRel.getCaminhoBaseIReportRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(fichaAlunoRelVOs.size());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				incializarDados();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "FichaAtualizacaoCadastralRelControle", "Finalizando Geração de Relatório Ficha Atualização Cadastral", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void montarListaSelectItemCurso() throws Exception {
		try {
			List<UnidadeEnsinoCursoVO> resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
			setListaSelectItemCurso(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nomeCursoTurno"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "consultar";
	}

	public void selecionarTurma() {
		try {
			setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), getUsuarioLogado());
			getUnidadeEnsinoCursoVO().setCurso(getTurmaVO().getCurso());
			setCampoConsultaTurma("");
			setValorConsultaTurma("");
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
		} catch (Exception e) {
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
		getTurmaVO().setCodigo(0);
		getTurmaVO().setIdentificadorTurma("");
		getTurmaVO().setAnual(false);
		getTurmaVO().setSemestral(false);
		setMensagemID("msg_entre_prmrelatorio");
	}

	private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	}

	public void selecinarCurso() {
		try {
			setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			montarListaSelectItemTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTurma() throws Exception {
		try {
			List<TurmaVO> resultadoConsulta = consultarTurmasPorUnidadeEnsinoCurso(getUnidadeEnsinoCursoVO().getCodigo());
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
		} catch (Exception e) {
			throw e;
		}
	}

	private List<TurmaVO> consultarTurmasPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso) throws Exception {
		return getFacadeFactory().getTurmaFacade().consultarPorCodigoUnidadeEnsinoCurso(codigoUnidadeEnsinoCurso, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	}

	public void consultarAluno() {
		try {
			if (getCampoConsultaAluno().equals("nome")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		setMatriculaVO((MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens"));
		Uteis.liberarListaMemoria(getListaConsultaAluno());
		this.setValorConsultaAluno(null);
		this.setCampoConsultaAluno(null);
	}

	public void limparAluno() {
		setMatriculaVO(new MatriculaVO());
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matricula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		return itens;
	}

	public void selecionarFiltro() {
		setarFalseNosFiltros();
		setarNewNosObjetosDeConsulta();
		setMostrarPanelAluno(getFiltro().equals("aluno"));
		setMostrarPanelOutros(getFiltro().equals("outros"));
	}

	private void setarFalseNosFiltros() {
		setMostrarPanelAluno(false);
		setMostrarPanelOutros(false);
	}

	private void setarNewNosObjetosDeConsulta() {
		setUnidadeEnsinoVO(null);
		setUnidadeEnsinoCursoVO(null);
		setTurmaVO(null);
		setMatriculaVO(null);
	}

	public List<SelectItem> getListaFiltros() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("outros", "Turma"));
		return itens;
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

	public List<SelectItem> getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public String getFiltro() {
		if (filtro == null) {
			filtro = "";
		}
		return filtro;
	}

	public Boolean getMostrarPanelAluno() {
		if (mostrarPanelAluno == null) {
			mostrarPanelAluno = false;
		}
		return mostrarPanelAluno;
	}

	public void setMostrarPanelAluno(Boolean mostrarPanelAluno) {
		this.mostrarPanelAluno = mostrarPanelAluno;
	}

	public Boolean getMostrarPanelOutros() {
		if (mostrarPanelOutros == null) {
			mostrarPanelOutros = false;
		}
		return mostrarPanelOutros;
	}

	public void setMostrarPanelOutros(Boolean mostrarPanelOutros) {
		this.mostrarPanelOutros = mostrarPanelOutros;
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

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
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

	public void consultarMatriculaPorChavePrimaria() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
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
	
	
	
	public void consultarAlunoPorRegistroAcademico() {
		try {
			MatriculaVO  objAluno = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorRegistroAcademico(getMatriculaVO().getAluno().getRegistroAcademico(), this.getUnidadeEnsinoLogado().getCodigo(), 0,  Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(this.getUnidadeEnsinoLogado().getCodigo()), getUsuarioLogado());
			if (objAluno == null || objAluno.getMatricula().equals("") ) {
				throw new Exception("Aluno de registro Acadêmico " + getMatriculaVO().getAluno().getRegistroAcademico() + " não encontrado. Verifique se o número de matrícula está correto.");
			}		
				setMatriculaVO(objAluno);
				setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
}
	
	
	public List getListaSelectItemSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1°"));
		lista.add(new SelectItem("2", "2°"));
		return lista;
	}

	public String getAnoConsulta() {
		if(anoConsulta == null) {
			anoConsulta = Uteis.getAnoDataAtual() ;
		}		
		return anoConsulta;
	}

	public void setAnoConsulta(String anoConsulta) {
		this.anoConsulta = anoConsulta;
	}

	public String getSemestreConsulta() {
		if(semestreConsulta == null ) {
			semestreConsulta  = Uteis.getSemestreAtual();
		}
		return semestreConsulta;
	}

	public void setSemestreConsulta(String semestreConsulta) {
		this.semestreConsulta = semestreConsulta;
	}
	
	
	public Boolean getFiltrarPorRegistroAcademico() {
		if(filtrarPorRegistroAcademico == null) {
			filtrarPorRegistroAcademico = Boolean.FALSE;
		}
		return filtrarPorRegistroAcademico;
	}

	public void setFiltrarPorRegistroAcademico(Boolean filtrarPorRegistroAcademico) {
		this.filtrarPorRegistroAcademico = filtrarPorRegistroAcademico;
	}
	
	
	
	

}
