package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.HistoricoTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.HistoricoTurmaRel;
import controle.arquitetura.SelectItemOrdemValor;

@Controller("HistoricoTurmaRelControle")
@Scope("viewScope")
@Lazy
public class HistoricoTurmaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private Boolean existeUnidadeEnsino;
	private List<SelectItem> listaSelectItemTurma;
	private TurmaVO turmaVO;
	private String semestre;
	private String ano;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Integer disciplina;
	private Boolean trazerDisciplinaAproveitadas;	
	private List<SelectItem> listaSelectItemTipoAluno;
	private String tipoAluno;
	private List<SelectItem> listaSelectItemCurso;
	private Boolean apresentarAlunosTurmaOrigem;

	public HistoricoTurmaRelControle() throws Exception {
		inicializarListasSelectItemTodosComboBox();
		inicializarUnidadeEnsino();
		setMensagemID("msg_entre_prmrelatorio");
		setSemestre(Uteis.getSemestreAtual());
		inicializarFiltroRelatorioAcademicoVO();
	}

	public void inicializarUnidadeEnsino() {
		try {
			getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
			setExisteUnidadeEnsino(Uteis.isAtributoPreenchido(getUnidadeEnsinoVO()));
		} catch (Exception e) {
			setExisteUnidadeEnsino(false);
		}
	}

	public void imprimirPDF() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoTurmaRelControle", "Iniciando Impressao Relatorio PDF", "Emitindo Relatorio");
			setUnidadeEnsinoVO(getTurmaVO().getUnidadeEnsino());
			HistoricoTurmaRel.validarDados(getTurmaVO(), getSemestre(), getAno(), getUnidadeEnsinoVO().getCodigo(), getDisciplina());
			if (!getIsApresentarCampoAno()) {
				setAno("");
			}
			if (!getIsApresentarCampoSemestre()) {
				setSemestre("");
			}
			getFacadeFactory().getHistoricoTurmaRelFacade().setDescricaoFiltros("");
			List<HistoricoTurmaRelVO> listaObjetos = getFacadeFactory().getHistoricoTurmaRelFacade().criarObjeto(getTurmaVO(), getSemestre(), getAno(), getUnidadeEnsinoVO().getCodigo(), getDisciplina(), true, getTrazerAlunoPendenteFinanceiramente(), getTrazerDisciplinaAproveitadas(), getUsuarioLogado(), null, "",getApresentarAlunosTurmaOrigem(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
			if (listaObjetos.isEmpty()) {
				throw new Exception("Não há dados a serem exibidos no relatório.");
			}
			getSuperParametroRelVO().setNomeDesignIreport(HistoricoTurmaRel.getDesignIReportRelatorio());
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(HistoricoTurmaRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Relatório do Histórico da Turma");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(HistoricoTurmaRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setQuantidade(listaObjetos.size());
			getSuperParametroRelVO().adicionarParametro("apresentarAlunosTurmaOrigem", getApresentarAlunosTurmaOrigem());
			getSuperParametroRelVO().adicionarParametro("turma_apresentar_cabecalho", getTurmaVO().getIdentificadorTurma());
			realizarImpressaoRelatorio();
			//persistirDadosPadroes();
			if (Uteis.isAtributoPreenchido(listaObjetos)) {
				setMensagemID("msg_relatorio_ok");
//				removerObjetoMemoria(this);
//				inicializarListasSelectItemTodosComboBox();
//				inicializarUnidadeEnsino();
//				setSemestre(Uteis.getSemestreAtual());
			}
//			inicializarDadosPadroes();
			registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoTurmaRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
//			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
//				montarListaSelectItemTurmaVisaoCoordenador();
//			} else {
//				montarListaSelectItemTurmaVisaoProfessor();
//			}
		}
	}

	public void imprimirPDFVisaoFuncionario() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoTurmaRelControle", "Iniciando Impressao Relatorio PDF - Visao Funcionario", "Emitindo Relatorio");
			HistoricoTurmaRel.validarDados(getTurmaVO(), getSemestre(), getAno(), getUnidadeEnsinoVO().getCodigo(), getDisciplina());
			getFacadeFactory().getHistoricoTurmaRelFacade().setDescricaoFiltros("");
			if (!getIsApresentarCampoAno()) {
				setAno("");
			}
			if (!getIsApresentarCampoSemestre()) {
				setSemestre("");
			}
			List<HistoricoTurmaRelVO> listaObjetos = getFacadeFactory().getHistoricoTurmaRelFacade().criarObjeto(getTurmaVO(), getSemestre(), getAno(), getUnidadeEnsinoVO().getCodigo(), getDisciplina(), false, getTrazerAlunoPendenteFinanceiramente(), getTrazerDisciplinaAproveitadas(), getUsuarioLogado(), getFiltroRelatorioAcademicoVO(), getTipoAluno(),getApresentarAlunosTurmaOrigem(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
			if (listaObjetos.isEmpty()) {
				throw new Exception("Não há dados a serem exibidos no relatório.");
			}
			getSuperParametroRelVO().setNomeDesignIreport(HistoricoTurmaRel.getDesignIReportRelatorio());
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(HistoricoTurmaRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Relatório do Histórico da Turma");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(HistoricoTurmaRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setQuantidade(listaObjetos.size());
			getSuperParametroRelVO().adicionarParametro("apresentarAlunosTurmaOrigem", getApresentarAlunosTurmaOrigem());
			getSuperParametroRelVO().adicionarParametro("turma_apresentar_cabecalho", getTurmaVO().getIdentificadorTurma());
			if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
				setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
			}
			realizarImpressaoRelatorio();
			persistirDadosPadroes();
			if (Uteis.isAtributoPreenchido(listaObjetos)) {
				setMensagemID("msg_relatorio_ok");
				removerObjetoMemoria(this);
				inicializarListasSelectItemTodosComboBox();
				inicializarUnidadeEnsino();
				setSemestre(Uteis.getSemestreAtual());
				inicializarFiltroRelatorioAcademicoVO();
			}
			inicializarDadosPadroes();
			registrarAtividadeUsuario(getUsuarioLogado(), "HistoricoTurmaRelControle", "Finalizando Impressao Relatorio PDF", "Emitindo Relatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				throw new ConsistirException("O campo UNIDADE DE ENSINO (Histórico Turma) deve ser informado.");
			}
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		getFacadeFactory().getTurmaFacade().carregarDados(obj, getUsuarioLogado());
		setTurmaVO(obj);
		if (getTurmaVO().getSubturma()) {
			getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		}
		setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		montarListaDisciplina();
		obj = null;
		valorConsultaTurma = "";
		campoConsultaTurma = "";
		listaConsultaTurma.clear();
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void montarTurma() throws Exception {
		try {
			if (!Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				throw new ConsistirException("O campo UNIDADE DE ENSINO (Histórico Turma) deve ser informado.");
			}
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(),getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			if (getTurmaVO().getSubturma()) {
				getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			montarListaDisciplina();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			removerObjetoMemoria(getTurmaVO());
			setTurmaVO(new TurmaVO());
			getListaSelectItemDisciplina().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void montarListaSelectItemTurmaVisaoCoordenador() {
		try {
			List<Integer> mapAuxiliarSelectItem = new ArrayList();
			List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
			selectItems.add(new SelectItem(0, ""));
			List<TurmaVO> listaResultado = consultarTurmaPorCoordenador();
			for (TurmaVO obj : listaResultado) {
				if(!mapAuxiliarSelectItem.contains(obj.getCodigo())){
					selectItems.add(new SelectItem(obj.getCodigo(), obj.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(obj.getCodigo());
				}
			}
			Collections.sort(getListaSelectItemTurma(), new SelectItemOrdemValor());
			setListaSelectItemTurma(selectItems);
		} catch (Exception e) {
		}
	}

	public List<TurmaVO> consultarTurmaPorCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void montarListaDisciplinaTurmaVisaoCoordenador() {
		try {
			setDisciplina(0);
			if (getTurmaVO().getCodigo() != 0) {
				getTurmaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.TODOS, getUsuarioLogado());
				montarListaSelectItemCursoTurmaAgupadaVisaoProfessorCoordenador();
				montarListaDisciplina();
				//List<DisciplinaVO> resultado = consultarDisciplinaTurmaVisaoCoordenador();
				//setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome"));
			}
		} catch (Exception e) {
			setListaSelectItemDisciplina(null);
		}
	}

	@SuppressWarnings("unchecked")
	public List<DisciplinaVO> consultarDisciplinaTurmaVisaoCoordenador() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaCoordenadorPorTurma(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void montarListaSelectItemTurma() {
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		List<TurmaVO> listaResultado = null;
		Iterator<TurmaVO> i = null;
		String value = "";
		try {
			List<SelectItem> obj = new ArrayList<SelectItem>(0);
			listaResultado = consultarTurmaPorProfessor();
			obj.add(new SelectItem(0, ""));
			i = listaResultado.iterator();

			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();

				if(!mapAuxiliarSelectItem.contains(turma.getCodigo())){
					obj.add(new SelectItem(turma.getCodigo(), turma.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(turma.getCodigo());
				}
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) obj, ordenador);
			setListaSelectItemTurma(obj);
		} catch (Exception e) {
			Uteis.liberarListaMemoria(getListaSelectItemTurma());
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
			value = "";
		}
	}

	@SuppressWarnings("unchecked")
	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
			if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true);
			} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), true, false);
			} else {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true);
			}
		} else {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		}
	}

	public void consultarTurmaProfessor() {
		try {
			if (getTurmaVO().getCodigo() != 0) {
				getTurmaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
				montarListaSelectItemCursoTurmaAgupadaVisaoProfessorCoordenador();
				montarListaSelectItemDisciplinaTurma();
				if (!getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
					if (getTurmaVO().getSemestral()) {
						setSemestre(Uteis.getSemestreAtual());
						setAno(Uteis.getAnoDataAtual4Digitos());
					} else if (getTurmaVO().getAnual()) {
						setSemestre("");
						setAno(Uteis.getAnoDataAtual4Digitos());
					} else if (getTurmaVO().getIntegral()) {
						setAno("");
						setSemestre("");
					}
				}
			}
			setMensagemID("msg_entre_prmrelatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void montarListaSelectItemDisciplinaTurma() {
		List<DisciplinaVO> resultado = null;
		try {
			resultado = consultarDisciplinaProfessorTurma();
			setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome"));
		} catch (Exception e) {
			Uteis.liberarListaMemoria(getListaSelectItemDisciplina());
		} finally {
			Uteis.liberarListaMemoria(resultado);
		}
	}

	public void montarTurmaProfessor() throws Exception {
		try {
			if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
				Uteis.liberarListaMemoria(getListaSelectItemDisciplina());
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorCodigoUnidadeEnsinoIdentificadorTurma(getUnidadeEnsinoVO().getCodigo().intValue(), getTurmaVO().getIdentificadorTurma(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				montarListaDisciplinaProfessor();
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			removerObjetoMemoria(getTurmaVO());
			setTurmaVO(new TurmaVO());
			Uteis.liberarListaMemoria(getListaSelectItemDisciplina());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaDisciplinaProfessor() throws Exception {
		List<DisciplinaVO> listaConsultas = consultarDisciplinaProfessorTurma();
		setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(listaConsultas, "codigo", "nome"));
	}

	@SuppressWarnings("unchecked")
	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultaRapidaPorProfessorTurma(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), false, getUsuarioLogado());
	}

	public void limparDados() {
		setTurmaVO(null);
		setSemestre(null);
		setAno(null);
		setDisciplina(null);
		setSemestre(Uteis.getSemestreAtual());
		setApresentarAlunosTurmaOrigem(Boolean.FALSE);
	}

	public void montarListaDisciplina() {
		try {
			List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurmaVO().getCodigo(), true, true, 0);
			getListaSelectItemDisciplina().clear();
			getListaSelectItemDisciplina().add(new SelectItem(0, ""));
			for (HorarioTurmaDisciplinaProgramadaVO obj : horarioTurmaDisciplinaProgramadaVOs) {
				getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigoDisciplina(), obj.getCodigoDisciplina()+" - "+obj.getNomeDisciplina() + " (CH: " + obj.getChDisciplina()+") "));
			}
		} catch (Exception e) {
			setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
		}

	}

	public void montarListaDisciplinaAgrupada() throws Exception {
		List<DisciplinaVO> resultadoConsulta = consultarDisciplinaTurmaAgrupada();
		setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
	}

	public List<DisciplinaVO> consultarDisciplinaTurmaAgrupada() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaTurmaAgrupada(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public void montarListaDisciplinaNaoAgrupada() throws Exception {
		Iterator<TurmaDisciplinaVO> i = getTurmaVO().getTurmaDisciplinaVOs().iterator();
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			TurmaDisciplinaVO gradeDisciplina = (TurmaDisciplinaVO) i.next();
			objs.add(new SelectItem(gradeDisciplina.getDisciplina().getCodigo(), gradeDisciplina.getDisciplina().getNome()));
		}
		setListaSelectItemDisciplina(objs);

	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			boolean emBranco = false;
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				emBranco = true;
			}
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", emBranco));
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
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
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// //System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
	}

	public void limparIdentificador() {
		setTurmaVO(null);
		getListaSelectItemDisciplina().clear();
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
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
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public Boolean getExisteUnidadeEnsino() {
		return existeUnidadeEnsino;
	}

	public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
		this.existeUnidadeEnsino = existeUnidadeEnsino;
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

	public String getAno() {
		if (ano == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				ano = getVisaoProfessorControle().getAno();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				ano = getVisaoCoordenadorControle().getAno();
			}else {
				ano = Uteis.getAnoDataAtual4Digitos();
			}
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
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

	public String getSemestre() {
		if (semestre == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				semestre = getVisaoProfessorControle().getSemestre();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				semestre = getVisaoCoordenadorControle().getSemestre();
			}else {
				semestre = Uteis.getSemestreAtual();
			}
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public Boolean getTrazerDisciplinaAproveitadas() {
		if (trazerDisciplinaAproveitadas == null) {
			trazerDisciplinaAproveitadas = false;
		}
		return trazerDisciplinaAproveitadas;
	}

	public void setTrazerDisciplinaAproveitadas(Boolean trazerDisciplinaAproveitadas) {
		this.trazerDisciplinaAproveitadas = trazerDisciplinaAproveitadas;
	}

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral() || getTurmaVO().getAnual()) {
						setAno(getAno());
						return true;
					} else {
						setAno("");
						setSemestre("");
						return false;
					}
				}
				return true;
			} else {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral() || getTurmaVO().getAnual()) {
						setAno(getAno());
					} else {
						setAno("");
						setSemestre("");
					}
				}
				return false;
			}
		}
		return true;
	}

	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral()) {
						setSemestre(getSemestre());
						return true;
					} else {
						setSemestre("");
						return false;
					}
				}
				return true;
			} else {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral()) {
						setSemestre(getSemestre());
					} else {
						setSemestre("");
					}
				}
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTurmaVisaoProfessor() {
		try {
			List<Integer> mapAuxiliarSelectItem = new ArrayList();
			List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
			selectItems.add(new SelectItem(0, ""));
			List<TurmaVO> listaResultado = consultarTurmaPorProfessor();
			for (TurmaVO obj : listaResultado) {
				if(!mapAuxiliarSelectItem.contains(obj.getCodigo())){
					selectItems.add(new SelectItem(obj.getCodigo(), obj.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(obj.getCodigo());
				}
			}
			Collections.sort(getListaSelectItemTurma(), new SelectItemOrdemValor());
			setListaSelectItemTurma(selectItems);
		} catch (Exception e) {
		}
	}

	public boolean getIsApresentarCampos() {
		return Uteis.isAtributoPreenchido(getTurmaVO());
	}

	public boolean getIsApresentarCampoAno() {
		if (getTurmaVO().getTurmaAgrupada()) {
			return getTurmaVO().getAnual() || getTurmaVO().getSemestral();
		}
		return getTurmaVO().getCurso().getAnual() || getTurmaVO().getCurso().getSemestral();
	}

	public boolean getIsApresentarCampoSemestre() {
		if (getTurmaVO().getTurmaAgrupada()) {
			return getTurmaVO().getSemestral();
		}
		return getTurmaVO().getCurso().getSemestral();
	}
	
	public boolean getTrazerAlunoPendenteFinanceiramente() throws Exception {
		return getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
	}
	

	public List<SelectItem> getListaSelectItemTipoAluno() {
		if(listaSelectItemTipoAluno == null){
			listaSelectItemTipoAluno = new ArrayList<SelectItem>(0);
			listaSelectItemTipoAluno.add(new SelectItem("todos", "Todos"));
			listaSelectItemTipoAluno.add(new SelectItem("normal", "Alunos (Turma Origem)"));
			listaSelectItemTipoAluno.add(new SelectItem("reposicao", "Alunos (Reposição/Inclusão)"));
		}
		return listaSelectItemTipoAluno;
	}
	
	public void inicializarFiltroRelatorioAcademicoVO(){
		getFiltroRelatorioAcademicoVO().setAtivo(true);
		getFiltroRelatorioAcademicoVO().setConcluido(true);
		getFiltroRelatorioAcademicoVO().setFormado(true);
		getFiltroRelatorioAcademicoVO().setPreMatricula(true);
		getFiltroRelatorioAcademicoVO().setPendenteFinanceiro(true);
		getFiltroRelatorioAcademicoVO().setConfirmado(true);
		getFiltroRelatorioAcademicoVO().setTrazerAlunosComTransferenciaMatriz(true);
	}	

	/**
	 * @return the tipoAluno
	 */
	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "todos";
		}
		return tipoAluno;
	}

	/**
	 * @param tipoAluno the tipoAluno to set
	 */
	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}
	
	public void montarListaSelectItemCursoTurmaAgupadaVisaoProfessorCoordenador() throws Exception {
		if (getTurmaVO().getTurmaAgrupada()) {
			List<CursoVO> cursoVOs = getFacadeFactory().getCursoFacade().consultarCursoTurmasAgrupadasPorTurmaOrigem(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemCurso(UtilSelectItem.getListaSelectItem(cursoVOs, "codigo", "nome"));
		}
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
	
	public Boolean getApresentarAlunosTurmaOrigem() {
		if (apresentarAlunosTurmaOrigem == null) {
			apresentarAlunosTurmaOrigem = Boolean.FALSE;
		}
		return apresentarAlunosTurmaOrigem;
	}

	public void setApresentarAlunosTurmaOrigem(Boolean apresentarAlunosTurmaOrigem) {
		this.apresentarAlunosTurmaOrigem = apresentarAlunosTurmaOrigem;
	}

	public boolean getIsApresentarCheckBoxAlunoTurmaOrigem() {
		return getTurmaVO().getTurmaAgrupada();
	}
 

	@PostConstruct
	private void inicializarDadosPadroes(){
		try {
			if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
				getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "HistoricoTurmaRel", getUsuarioLogado());
				Map<String, String> camposPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"tipoAluno", "ano", "semestre", "trazerAlunosComTransferenciaMatriz", "trazerDisciplinaAproveitadas"}, "HistoricoTurmaRel");
				for(String key: camposPadroes.keySet()){
					if(key.equals("tipoAluno")){
						setTipoAluno(camposPadroes.get(key));
					}else if(key.equals("ano")){
						setAno(camposPadroes.get(key));
					}else if(key.equals("semestre")){
						setSemestre(camposPadroes.get(key));
					}else if(key.equals("trazerAlunosComTransferenciaMatriz")){
						getFiltroRelatorioAcademicoVO().setTrazerAlunosComTransferenciaMatriz(Boolean.valueOf(camposPadroes.get(key)));
					}else if(key.equals("trazerDisciplinaAproveitadas")){
						setTrazerDisciplinaAproveitadas(Boolean.valueOf(camposPadroes.get(key)));
					}
				}
			}else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				montarListaSelectItemTurmaVisaoProfessor();
			}else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				montarListaSelectItemTurmaVisaoCoordenador();
			}
		} catch (Exception e) {			
		}
	}
	
	
	private void persistirDadosPadroes(){
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "HistoricoTurmaRel", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoAluno(), "HistoricoTurmaRel", "tipoAluno", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(), "HistoricoTurmaRel", "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(), "HistoricoTurmaRel", "semestre", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getTrazerAlunosComTransferenciaMatriz().toString(), "HistoricoTurmaRel", "trazerAlunosComTransferenciaMatriz", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTrazerDisciplinaAproveitadas().toString(), "HistoricoTurmaRel", "trazerDisciplinaAproveitadas", getUsuarioLogado());										
		} catch (Exception e) {
			
		}				
		
	}
	

}
