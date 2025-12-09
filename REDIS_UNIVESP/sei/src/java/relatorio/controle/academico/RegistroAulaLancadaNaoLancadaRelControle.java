package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.RegistroAulaLancadaNaoLancadaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.RegistroAulaLancadaNaoLancadaRel;

@Controller("RegistroAulaLancadaNaoLancadaRelControle")
@Scope("viewScope")
public class RegistroAulaLancadaNaoLancadaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsino;
	private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private TurmaVO turma;
	private DisciplinaVO disciplina;
	private FuncionarioVO professor;
	private String tipoCurso;
	private String situacaoRegistroAula;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemCurso;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemDisciplina;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List<PessoaVO> listaConsultaProfessor;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private String ano;
	private String semestre;
	private Date dataIni;
	private Date dataFim;
	private List<SelectItem> listaSelectItemSituacaoRegistroAula;
	private List<SelectItem> listaSelectItemDisciplinasProgramacaoAula;

	public RegistroAulaLancadaNaoLancadaRelControle() throws Exception {
		incializarDados();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void limparListasConsultas() {
		setTurma(null);
		setDisciplina(null);
		setUnidadeEnsinoCurso(null);
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
		getListaSelectItemDisciplina().clear();
	}

	private void incializarDados() {
		montarListaSelectItemUnidadeEnsino();
	}

	public void imprimirPDF() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				setUnidadeEnsino(getTurma().getUnidadeEnsino());
				if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					getProfessor().setPessoa(getUsuarioLogadoClone().getPessoa());
				}
				setTipoCurso(getTurma().getPeriodicidade());
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "RegistroAulaLancadaNaoLancadaRelControle", "Inicializando Geração de Relatório Faltas de Alunos", "Emitindo Relatório");
			List<RegistroAulaLancadaNaoLancadaRelVO> aulaLancadaNaoLancadaRelVOs = getFacadeFactory().getRegistroAulaLancadaNaoLancadaRelFacade().consultaRegistroAulaLancadaNaoLancadaRelatorio(getUnidadeEnsino().getCodigo(), getUnidadeEnsinoCurso().getCurso().getCodigo(), getUnidadeEnsinoCurso().getTurno().getCodigo(), getTurma().getCodigo(), getDisciplina().getCodigo(), getProfessor().getPessoa().getCodigo(), getAno(), getSemestre(), getDataIni(), getDataFim(), getSituacaoRegistroAula(), getTipoCurso(), false, getUsuarioLogado());
			if (aulaLancadaNaoLancadaRelVOs.isEmpty()) {
				setMensagemID("msg_relatorio_sem_dados");
			} else {
				getSuperParametroRelVO().setNomeDesignIreport(RegistroAulaLancadaNaoLancadaRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Registro Aula Lançada/Não Lançada");
				getSuperParametroRelVO().setListaObjetos(aulaLancadaNaoLancadaRelVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(RegistroAulaLancadaNaoLancadaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				if (!getUnidadeEnsino().getCodigo().equals(0)) {
					setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsino());
				}
				executarMontagemParametrosRelatorio();
				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);
				incializarDados();
				setMensagemID("msg_relatorio_ok");
				if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					novoVisaoProfessor();
				} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					novoVisaoCoordenador();
				}
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "RegistroAulaLancadaNaoLancadaRelControle", "Finalizando Geração de Relatório Faltas de Alunos", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemSituacaoRegistroAula() {
		if (listaSelectItemSituacaoRegistroAula == null) {
			listaSelectItemSituacaoRegistroAula = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoRegistroAula.add(new SelectItem("RE", "Registrada"));
			listaSelectItemSituacaoRegistroAula.add(new SelectItem("NA", "Não Registrada"));
			listaSelectItemSituacaoRegistroAula.add(new SelectItem("RN", "Registrada/Não Registrada"));
		}
		return listaSelectItemSituacaoRegistroAula;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		try {
			return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList<>(0);
		}
	}

	public void montarListaSelectItemCurso() throws Exception {
		List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
		try {
			if (getUnidadeEnsino().getCodigo() == 0) {
				setListaSelectItemTurma(new ArrayList<SelectItem>(0));
				setListaSelectItemCurso(new ArrayList<SelectItem>(0));
			}
			resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsino().getCodigo());
			getListaSelectItemTurma().clear();
			getListaSelectItemCurso().clear();
			getListaSelectItemCurso().add(new SelectItem(0, ""));
			for (UnidadeEnsinoCursoVO unidadeEnsinoCursoVO : resultadoConsulta) {
				getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCursoVO.getCodigo(), unidadeEnsinoCursoVO.getCurso().getNome() + " - " + unidadeEnsinoCursoVO.getTurno().getNome()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	}

	private List<TurmaVO> consultarTurmasPorCurso(String nomeCurso) throws Exception {
		return getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoCursoTurno(getUnidadeEnsino().getCodigo(), getUnidadeEnsinoCurso().getCurso().getCodigo(), getUnidadeEnsinoCurso().getTurno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
	}

	public void montarListaSelectItemTurma() throws Exception {
		List<TurmaVO> resultadoConsulta = null;
		try {
			if (getUnidadeEnsinoCurso().getCodigo().intValue() == 0) {
				setListaSelectItemTurma(new ArrayList<SelectItem>(0));
			}
			setUnidadeEnsinoCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			resultadoConsulta = consultarTurmasPorCurso(getUnidadeEnsinoCurso().getCurso().getNome());
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public void consultarTurma() {
		try {
			if (getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCursoPeriodicidade(getValorConsultaTurma(), getUnidadeEnsinoCurso().getCurso().getCodigo(), getUnidadeEnsino().getCodigo(), getTipoCurso(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				if (getValorConsultaDisciplina().trim() != null || !getValorConsultaDisciplina().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplina().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoCursoTurma(valorInt, getUnidadeEnsinoCurso().getCurso().getCodigo(), getTurma().getCodigo(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeCursoTurma(getValorConsultaDisciplina(), getUnidadeEnsinoCurso().getCurso().getCodigo(), getTurma().getCodigo(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		setDisciplina((DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItem"));
		setValorConsultaDisciplina("");
		setCampoConsultaDisciplina("");
		getListaConsultaDisciplina().clear();
	}

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void consultarProfessor() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessor() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
			this.getProfessor().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			FuncionarioVO fun = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			this.setProfessor(fun);
		} catch (Exception e) {
		}
	}

	public String getMascaraConsultaProfessor() {
		if (getCampoConsultaProfessor().equals("cpf")) {
			return "return mascara(this.form,'formProfessor:valorConsultarProfessor','999.999.999-99',event);";
		}
		return "";
	}

	public String getTamanhoMaximoCPF() {
		if (getCampoConsultaProfessor().equals("cpf")) {
			return "14";
		}
		return "150";
	}

	private List<SelectItem> tipoConsultaComboProfessorBusca;

	public List<SelectItem> getTipoConsultaComboProfessorBusca() {
		if (tipoConsultaComboProfessorBusca == null) {
			tipoConsultaComboProfessorBusca = new ArrayList<SelectItem>(0);
			tipoConsultaComboProfessorBusca.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboProfessorBusca.add(new SelectItem("cpf", "CPF"));
		}
		return tipoConsultaComboProfessorBusca;
	}

	public void limparProfessor() {
		try {
			setProfessor(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			if (getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurma(), getTurma().getIdentificadorTurma(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getTurma().getCodigo() == 0) {
				setTurma(null);
			} else {
				montarListaSelectItemDisciplina();
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurma(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			setTurma((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			if (!getTurma().getTurmaAgrupada()) {
				setUnidadeEnsinoCurso(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(getTurma().getCurso().getCodigo(), getUnidadeEnsino().getCodigo(), getTurma().getTurno().getCodigo(), getUsuarioLogado()));
			}
			montarListaSelectItemDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setDisciplina(null);
			setTurma(null);
			setListaSelectItemDisciplina(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public void montarListaSelectItemDisciplina() {
		try {
			List<DisciplinaVO> resultado = consultarDisciplinaProfessorTurmaVisaoSecretaria();
			setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome"));
		} catch (Exception e) {
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		}
	}

	public List<DisciplinaVO> consultarDisciplinaProfessorTurmaVisaoSecretaria() throws Exception {
		// return
		// getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDia(getProfessor().getPessoa().getCodigo(),
		// getTurma().getCodigo(), getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaGradeEOptativaPorTurmaFazParteComposicao(getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void consultarCurso() {
		try {
			if (getUnidadeEnsino().getCodigo() == 0) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsinoPeriodicidade(new Integer(valorInt), getUnidadeEnsino().getCodigo(), getTipoCurso(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsinoPeriodicidade(getValorConsultaCurso(), getUnidadeEnsino().getCodigo(), getTipoCurso(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarCurso() throws Exception {
		try {
			setUnidadeEnsinoCurso((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens"));
			limparTurma();
			getListaConsultaTurma().clear();
			getListaSelectItemDisciplina().clear();
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			setUnidadeEnsinoCurso(null);
		} catch (Exception e) {
		}
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplina(null);
		} catch (Exception e) {
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
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

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
		if (unidadeEnsinoCurso == null) {
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
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

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
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

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
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

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public boolean getIsApresentarAno() {
		if ((getTipoCurso().equals("AN") || getTipoCurso().equals("SE")) || ((getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) && getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && (getTurma().getAnual() || getTurma().getSemestral()) || !Uteis.isAtributoPreenchido(getTurma().getCodigo()))) {
			if (!Uteis.isAtributoPreenchido(getAno())) {
				setAno(Uteis.getAnoDataAtual4Digitos());
			}
			return true;
		} else {
			if(getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()){
				if (!Uteis.isAtributoPreenchido(getAno())) {
					setAno(Uteis.getAnoDataAtual4Digitos());
				}				
			} else {
				setAno("");
				setSemestre("");
			}
			return false;
		}
	}
	
	public Boolean getPermitirGerarRelatorioRetroativo() throws Exception {
		return ControleAcesso.verificarPermissaoFuncionalidadeUsuario("PermitirGerarRelatorioRegistroAulaNaoLancadaRetroativo", getUsuarioLogado());
	}

	public boolean getIsApresentarSemestre() {
		if ((getTipoCurso().equals("SE")) || ((getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()) && getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getTurma().getSemestral()  || !Uteis.isAtributoPreenchido(getTurma().getCodigo()))) {
			setSemestre(Uteis.getSemestreAtual());
			return true;
		} else {
			if(getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador()){
				if (!Uteis.isAtributoPreenchido(getSemestre())) {
					setSemestre(Uteis.getSemestreAtual());
				}
			}else {
				setSemestre("");
			}
			return false;
		}
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
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

	public List<PessoaVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<PessoaVO>();
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<PessoaVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public Date getDataIni() {
		return dataIni;
	}

	public void setDataIni(Date dataIni) {
		this.dataIni = dataIni;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getSituacaoRegistroAula() {
		if (situacaoRegistroAula == null) {
			situacaoRegistroAula = "RE";
		}
		return situacaoRegistroAula;
	}

	public void setSituacaoRegistroAula(String situacaoRegistroAula) {
		this.situacaoRegistroAula = situacaoRegistroAula;
	}

	public FuncionarioVO getProfessor() {
		if (professor == null) {
			professor = new FuncionarioVO();
		}
		return professor;
	}

	public void setProfessor(FuncionarioVO professor) {
		this.professor = professor;
	}

	public String getTipoCurso() {
		if (tipoCurso == null && !getUsuarioLogado().getIsApresentarVisaoProfessor() && !getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			tipoCurso = "SE";
		} else if (tipoCurso == null && (getUsuarioLogado().getIsApresentarVisaoProfessor() || getUsuarioLogado().getIsApresentarVisaoCoordenador())) {
			tipoCurso = "";
		}
			
		return tipoCurso;
	}

	public void setTipoCurso(String tipoCurso) {
		this.tipoCurso = tipoCurso;
	}

	private void executarMontagemParametrosRelatorio() throws Exception {
		getSuperParametroRelVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
		getSuperParametroRelVO().setAno(getAno());
		getSuperParametroRelVO().setSemestre(getSemestre());
		getSuperParametroRelVO().setDataInicio(Uteis.getDataAplicandoFormatacao(getDataIni(), "dd/MM/yyyy"));
		getSuperParametroRelVO().setDataFim(Uteis.getDataAplicandoFormatacao(getDataFim(), "dd/MM/yyyy"));
		getSuperParametroRelVO().adicionarParametro("periodicidade", PeriodicidadeEnum.getEnumPorValor(getTipoCurso()).getDescricao());
		String situacaoRegistroAula = "";
		if (getSituacaoRegistroAula().equals("RE")) {
			situacaoRegistroAula = "Registrada";
		} else if (getSituacaoRegistroAula().equals("NA")) {
			situacaoRegistroAula = "Não Registrada";
		} else {
			situacaoRegistroAula = "Registrada/ Não Registrada";
		}
		getSuperParametroRelVO().adicionarParametro("situacaoRegistroAula", situacaoRegistroAula);
		getSuperParametroRelVO().adicionarParametro("situacaoRegistroAulaKey", getSituacaoRegistroAula());
	}
	
	public void montarAlunosVisaoProfessor() throws Exception {
		try {
			if (getTurma().getCodigo() > 0) {
				TurmaVO turmas = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
				setTurma(turmas);
				if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					montarListaDisciplinaTurmaVisaoProfessor();
				} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					montarListaDisciplinaTurmaVisaoCoordenador();
				}
				setMensagemID("msg_dados_consultados");
			} else {
				limparMensagem();
			}
			// montarComboDisciplinaPorAnoSemestre();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			List objs = new ArrayList(0);
			List resultado = consultarDisciplinaProfessorTurma();
			Iterator i = resultado.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DisciplinaVO obj = (DisciplinaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemDisciplinasProgramacaoAula(objs);

		} catch (Exception e) {
			setListaSelectItemDisciplinasProgramacaoAula(new ArrayList(0));
		}
	}
	
	public List consultarDisciplinaProfessorTurma() throws Exception {
		if (getTurma().getCodigo() > 0 && getTurma().getTurmaAgrupada()) {
			getTurma().getCurso().setLiberarRegistroAulaEntrePeriodo(getFacadeFactory().getTurmaFacade().consultarLiberarRegistroAulaEnterPeriodoTurmaAgrupada(getTurma().getCodigo(), getUsuarioLogado()));
		}
		if (getTurma().getCurso().getNivelEducacionalPosGraduacao() || (getTurma().getCurso().getLiberarRegistroAulaEntrePeriodo())) {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDia(getUsuarioLogado().getPessoa().getCodigo(), getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} else {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDiaSemestreAtual(getUsuarioLogado().getPessoa().getCodigo(), getTurma().getCodigo(), getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
	}

	public List<SelectItem> getListaSelectItemDisciplinasProgramacaoAula() {
		if (listaSelectItemDisciplinasProgramacaoAula == null) {
			listaSelectItemDisciplinasProgramacaoAula = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinasProgramacaoAula;
	}

	public void setListaSelectItemDisciplinasProgramacaoAula(List<SelectItem> listaSelectItemDisciplinasProgramacaoAula) {
		this.listaSelectItemDisciplinasProgramacaoAula = listaSelectItemDisciplinasProgramacaoAula;
	}
	
	public void montarListaSelectItemTurmaVisao() {
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			montarListaSelectItemTurmaVisaoProfessor();
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			montarListaSelectItemTurmaCoordenador();
		}
	}
	
	public void montarListaSelectItemTurmaVisaoProfessor() {
		getListaSelectItemTurma().clear();
		setDisciplina(null);
		setTurma(null);
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		List listaResultado = null;
		Iterator i = null;
		try {
			List obj = new ArrayList(0);
			listaResultado = consultarTurmaPorProfessor();
			obj.add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();
				if(!mapAuxiliarSelectItem.contains(turma.getCodigo())){
					obj.add(new SelectItem(turma.getCodigo(), turma.aplicarRegraNomeCursoApresentarCombobox()));
            		mapAuxiliarSelectItem.add(turma.getCodigo());
				}
			}
			setListaSelectItemTurma(obj);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList(0));
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
		}
	}
	
	public List consultarTurmaPorProfessor() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getVisaoLogar().equals("professor"), true, true);
	}
	
	public String novoVisaoProfessor() throws Exception {
		try {
			setTurma(null);
			setDisciplina(null);
			getIsApresentarAno();
			getIsApresentarSemestre();
//			getFacadeFactory().getRegistroAulaFacade().validarConsultaDoUsuario(getUsuarioLogadoClone());
			montarListaSelectItemTurmaVisaoProfessor();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroAulaLancadaNaoLancadaRelProfessor.xhtml");
	}
	
	public String novoVisaoCoordenador() throws Exception {
		try {
			setTurma(null);
			setDisciplina(null);
			getIsApresentarAno();
			getIsApresentarSemestre();
			getFacadeFactory().getRegistroAulaFacade().validarConsultaDoUsuario(getUsuarioLogadoClone());
			montarListaSelectItemTurmaCoordenador();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroAulaLancadaNaoLancadaRelCoordenador.xhtml");
	}
	
	@PostConstruct
	private void incializarDadosVisaoProfessorCoordenador() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				novoVisaoProfessor();
			} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				novoVisaoCoordenador();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		montarListaSelectItemUnidadeEnsino();
	}
	
	public void montarListaSelectItemTurmaCoordenador() {
		try {
			getListaSelectItemTurma().clear();
			setTurma(null);
			setDisciplina(null);
			List<TurmaVO> resultadoConsulta = null;
			Iterator<TurmaVO> i = null;
			resultadoConsulta = consultarTurmaCoordenador();
			i = resultadoConsulta.iterator();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TurmaVO obj = (TurmaVO) i.next();
				getListaSelectItemTurma().add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
				removerObjetoMemoria(obj);
			}
			resultadoConsulta.clear();
			resultadoConsulta = null;
			i = null;					
		} catch (Exception e) {
			System.out.println("MENSAGEM => " + e.getMessage());
		}
	}
	
	public List<TurmaVO> consultarTurmaCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, true, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}
	
	public void montarListaDisciplinaTurmaVisaoCoordenador() {
		try {
			setDisciplina(new DisciplinaVO());
			setMensagemDetalhada("");
//			if ((getTurma().getAnual() || getTurma().getSemestral()) && getAno().equals("") && !getProfessor().getCodigo().equals(0)) {
//				getProfessor().setCodigo(0);
//				throw new ConsistirException("Por favor informe o ano para buscar as disciplinas do professor.");
//			}
			getDisciplina().setCodigo(0);
			List objs = new ArrayList(0);
			List resultado = consultarDisciplinaProfessorTurmaVisaoSecretaria();
			Iterator i = resultado.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DisciplinaVO obj = (DisciplinaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemDisciplinasProgramacaoAula(objs);
		} catch (Exception e) {
			setListaSelectItemDisciplinasProgramacaoAula(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

}
