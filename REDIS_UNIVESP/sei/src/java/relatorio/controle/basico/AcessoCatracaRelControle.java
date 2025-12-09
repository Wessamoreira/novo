/**
 * 
 */
package relatorio.controle.basico;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CatracaVO;
import negocio.comuns.basico.OrdenadorVO;
import negocio.comuns.processosel.enumeradores.SituacaoResultadoProcessoSeletivoEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.basico.AcessoCatracaRelVO;
import relatorio.negocio.jdbc.academico.AlunosPorUnidadeCursoTurmaRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.NotaNaoLancadaProfessorRel;
import relatorio.negocio.jdbc.basico.AcessoCatracaRel;

/**
 * @author Carlos Eugênio
 *
 */
@Controller("AcessoCatracaRelControle")
@Scope("request")
@Lazy
public class AcessoCatracaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;

	private MatriculaVO matriculaVO;
	private TurmaVO turma;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private Date periodoAcessoInicio;
	private Date periodoAcessoFinal;
	private String ano;
	private String semestre;

	private String unidadeEnsinoApresentar;
	private String campoFiltroPor;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private String periodicidade;
	private List listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private String tipoRelatorio;
	private Integer catraca;
	private List<OrdenadorVO> listaOrdenacaoVOs;
	private String ordenadorApresentar;
	private String layout;

	public AcessoCatracaRelControle() throws Exception {
		super();
		consultarUnidadeEnsinoFiltroRelatorio("");
		verificarTodasUnidadesSelecionadas();
		inicializarDadosOrdenacao();
		setMensagemID("msg_entre_prmrelatorio");

	}

	public void inicializarDadosOrdenacao() {
		if (getListaOrdenacaoVOs().isEmpty()) {
			for (int i = 1; i <= 5; i++) {
				OrdenadorVO objTurma = new OrdenadorVO();
				if (i == 1) {
					objTurma.setCampoOrdenar("turma_identificadorTurma");
					objTurma.setDescricao("Identificador Turma");
				}
				if (i == 2) {
					objTurma.setCampoOrdenar("pessoa_nome");
					objTurma.setDescricao("Nome");
				}
				if (i == 3) {
					objTurma.setCampoOrdenar("tipoRelatorio");
					objTurma.setDescricao("Tipo Relatório");
				}
				if (i == 4) {
					objTurma.setCampoOrdenar("dataHora");
					objTurma.setDescricao("Data");
				}
				if (i == 5) {
					objTurma.setCampoOrdenar("matricula");
					objTurma.setDescricao("Matrícula");
				}
				objTurma.setOrdem(i);
				objTurma.setUtilizar(true);
				getListaOrdenacaoVOs().add(objTurma);
			}
		}
		realizarAtualizacaoComboBoxOrdenacao();
	}

	public void ordenarListaOrdenacao() {
		Ordenacao.ordenarLista(getListaOrdenacaoVOs(), "ordem");
	}

	public void alterarOrdemOrdenadorDescerUmaPosicao() {
		OrdenadorVO obj = (OrdenadorVO) context().getExternalContext().getRequestMap().get("ordenador");
		for (OrdenadorVO objOrdenacao : getListaOrdenacaoVOs()) {
			if (obj.getOrdem() + 1 == objOrdenacao.getOrdem()) {
				obj.setOrdem(obj.getOrdem() + 1);
				objOrdenacao.setOrdem(objOrdenacao.getOrdem() - 1);
				break;
			}
		}
		ordenarListaOrdenacao();
	}

	public void alterarOrdemOrdenadorSubirUmaPosicao() {
		OrdenadorVO obj = (OrdenadorVO) context().getExternalContext().getRequestMap().get("ordenador");
		for (OrdenadorVO objOrdenacao : getListaOrdenacaoVOs()) {
			if (obj.getOrdem() - 1 == objOrdenacao.getOrdem()) {
				obj.setOrdem(obj.getOrdem() - 1);
				objOrdenacao.setOrdem(objOrdenacao.getOrdem() + 1);
			}
		}
		ordenarListaOrdenacao();
	}

	public void realizarAtualizacaoComboBoxOrdenacao() {
		ordenarListaOrdenacao();
		setOrdenadorApresentar("");
		for (OrdenadorVO obj : getListaOrdenacaoVOs()) {
			if (obj.getUtilizar()) {
				if (getOrdenadorApresentar().equals("")) {
					setOrdenadorApresentar(obj.getOrdem() + " - " + obj.getDescricao());
				} else {
					setOrdenadorApresentar(getOrdenadorApresentar() + ", " + obj.getOrdem() + " - " + obj.getDescricao());
				}
			}
		}
	}

	public void imprimirPDF() {
		List<AcessoCatracaRelVO> listaObjetos = null;
		String design = null;
		try {
			listaObjetos = getFacadeFactory().getAcessoCatracaRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getPeriodoAcessoInicio(), getPeriodoAcessoFinal(), getTurma().getCodigo(), getMatriculaVO().getMatricula(), getAno(), getSemestre(), getCampoFiltroPor(), getTipoRelatorio(), getCatraca(), getPeriodicidade(), getListaOrdenacaoVOs(), getCursosApresentar(), getTurnosApresentar(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(AcessoCatracaRel.getDesignIReportRelatorio(getLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(AcessoCatracaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setTituloRelatorio("Controle de Acesso Aluno na Catraca");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AcessoCatracaRel.getCaminhoBaseRelatorio());
				adicionarParamentrosComuns();
				realizarImpressaoRelatorio();
				persistirDadosPadroesGeracaoRelatorio();
				removerObjetoMemoria(this);
				consultarUnidadeEnsinoFiltroRelatorio("");
				verificarTodasUnidadesSelecionadas();
				inicializarDadosOrdenacao();
				inicializarDados();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "ControleAcessoAlunoCatracaRelControle", "Finalizando Geração de Relatório Controle de Acesso do Aluno na Catraca", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	public void persistirDadosPadroesGeracaoRelatorio() throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(Uteis.getData(getPeriodoAcessoInicio(), "dd/MM/yyyy"), "acessoCatracaRel", "periodoAcessoInicio", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(Uteis.getData(getPeriodoAcessoFinal(), "dd/MM/yyyy"), "acessoCatracaRel", "periodoAcessoFinal", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getPeriodicidade(), "acessoCatracaRel", "periodicidade", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(), "acessoCatracaRel", "ano", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(), "acessoCatracaRel", "semestre", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoRelatorio(), "acessoCatracaRel", "tipoRelatorio", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getLayout(), "acessoCatracaRel", "layout", getUsuarioLogado());
		
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getAtivo() ? "true" : "false", "acessoCatracaRel", "filtroAcademico_ativo", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getPreMatricula() ? "true" : "false", "acessoCatracaRel", "filtroAcademico_preMatricula", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada() ? "true" : "false", "acessoCatracaRel", "filtroAcademico_preMatriculaCancelada", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getTrancado() ? "true" : "false", "acessoCatracaRel", "filtroAcademico_trancado", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getCancelado() ? "true" : "false", "acessoCatracaRel", "filtroAcademico_cancelado", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getConcluido() ? "true" : "false", "acessoCatracaRel", "filtroAcademico_concluido", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getTransferenciaInterna() ? "true" : "false", "acessoCatracaRel", "filtroAcademico_transferenciaInterna", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getTransferenciaExterna() ? "true" : "false", "acessoCatracaRel", "filtroAcademico_transferenciaExterna", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getFormado() ? "true" : "false", "acessoCatracaRel", "filtroAcademico_formado", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getFiltroRelatorioAcademicoVO().getAbandonado() ? "true" : "false", "acessoCatracaRel", "filtroAcademico_abandonoCurso", getUsuarioLogado());

		getFacadeFactory().getLayoutPadraoFacade().persistirOrdenacao(getListaOrdenacaoVOs(), "acessoCatracaRel", getUsuarioLogado());
	}

	@PostConstruct
	public void inicializarDados() throws Exception {
		Map<String, String> campos = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(null, "acessoCatracaRel");
		if (campos != null && !campos.isEmpty()) {
			try {
				setPeriodoAcessoInicio(campos.containsKey("periodoAcessoInicio") ? Uteis.getData(campos.get("periodoAcessoInicio"), "dd/MM/yyyy") : new Date());
				setPeriodoAcessoFinal(campos.containsKey("periodoAcessoFinal") ? Uteis.getData(campos.get("periodoAcessoFinal"), "dd/MM/yyyy") : new Date());
				setPeriodicidade(campos.containsKey("periodicidade") ? campos.get("periodicidade") : "AN");
				setAno(campos.containsKey("ano") ? campos.get("ano") : "");
				setSemestre(campos.containsKey("semestre") ? campos.get("semestre") : "");
				setTipoRelatorio(campos.containsKey("tipoRelatorio") ? campos.get("tipoRelatorio") : "TODOS");
				setLayout(campos.containsKey("layout") ? campos.get("layout") : "ANALITICO_TURMA_DIA");
				
				getFiltroRelatorioAcademicoVO().setAtivo(campos.containsKey("filtroAcademico_ativo") && Uteis.isAtributoPreenchido(campos.get("filtroAcademico_ativo")) && campos.get("filtroAcademico_ativo").equals("true"));
				getFiltroRelatorioAcademicoVO().setPreMatricula(campos.containsKey("filtroAcademico_preMatricula") && Uteis.isAtributoPreenchido(campos.get("filtroAcademico_preMatricula")) && campos.get("filtroAcademico_preMatricula").equals("true"));
				getFiltroRelatorioAcademicoVO().setPreMatriculaCancelada(campos.containsKey("filtroAcademico_preMatriculaCancelada") && Uteis.isAtributoPreenchido(campos.get("filtroAcademico_preMatriculaCancelada")) && campos.get("filtroAcademico_preMatriculaCancelada").equals("true"));
				getFiltroRelatorioAcademicoVO().setTrancado(campos.containsKey("filtroAcademico_trancado") && Uteis.isAtributoPreenchido(campos.get("filtroAcademico_trancado")) && campos.get("filtroAcademico_trancado").equals("true"));
				getFiltroRelatorioAcademicoVO().setCancelado(campos.containsKey("filtroAcademico_cancelado") && Uteis.isAtributoPreenchido(campos.get("filtroAcademico_cancelado")) && campos.get("filtroAcademico_cancelado").equals("true"));
				getFiltroRelatorioAcademicoVO().setConcluido(campos.containsKey("filtroAcademico_concluido") && Uteis.isAtributoPreenchido(campos.get("filtroAcademico_concluido")) && campos.get("filtroAcademico_concluido").equals("true"));
				getFiltroRelatorioAcademicoVO().setTransferenciaInterna(campos.containsKey("filtroAcademico_transferenciaInterna") && Uteis.isAtributoPreenchido(campos.get("filtroAcademico_transferenciaInterna")) && campos.get("filtroAcademico_transferenciaInterna").equals("true"));
				getFiltroRelatorioAcademicoVO().setTransferenciaExterna(campos.containsKey("filtroAcademico_transferenciaExterna") && Uteis.isAtributoPreenchido(campos.get("filtroAcademico_transferenciaExterna")) && campos.get("filtroAcademico_transferenciaExterna").equals("true"));
				getFiltroRelatorioAcademicoVO().setFormado(campos.containsKey("filtroAcademico_formado") && Uteis.isAtributoPreenchido(campos.get("filtroAcademico_formado")) && campos.get("filtroAcademico_formado").equals("true"));
				getFiltroRelatorioAcademicoVO().setAbandonado(campos.containsKey("filtroAcademico_abandonoCurso") && Uteis.isAtributoPreenchido(campos.get("filtroAcademico_abandonoCurso")) && campos.get("filtroAcademico_abandonoCurso").equals("true"));
				

				getFacadeFactory().getLayoutPadraoFacade().consultarOrdenacaoPadrao(getListaOrdenacaoVOs(), "acessoCatracaRel", getUsuarioLogado());
				realizarAtualizacaoComboBoxOrdenacao();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public void adicionarParamentrosComuns() throws Exception {
		getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
		getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
		getSuperParametroRelVO().adicionarParametro("ano", getAno());
		getSuperParametroRelVO().adicionarParametro("semestre", getSemestre());
		getSuperParametroRelVO().adicionarParametro("periodoAcessoInicio", Uteis.getDataAno4Digitos(getPeriodoAcessoInicio()));
		getSuperParametroRelVO().adicionarParametro("periodoAcessoFinal", Uteis.getDataAno4Digitos(getPeriodoAcessoFinal()));
		getSuperParametroRelVO().adicionarParametro("ordenacao", getOrdenadorApresentar());
		if (getLayout().equals("ANALITICO_TURMA_DIA")) {
			getSuperParametroRelVO().adicionarParametro("layout", "Analítico por Turma ");
		} else {
			getSuperParametroRelVO().adicionarParametro("layout", "Analítico por Dia");
		}

		if (!getCatraca().equals(0)) {
			getSuperParametroRelVO().adicionarParametro("catraca", getFacadeFactory().getCatracaFacade().consultarPorChavePrimaria(getCatraca(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado()).getDescricao());
		} else {
			getSuperParametroRelVO().adicionarParametro("catraca", "Todas");
		}
		StringBuilder unidades = new StringBuilder();
		String aux = "";
		boolean possuiAlgumaUnidadeDesmarcada = false;
		for (UnidadeEnsinoVO ue : getUnidadeEnsinoVOs()) {
			if (ue.getFiltrarUnidadeEnsino()) {
				unidades.append(aux).append(ue.getNome());
				aux = ",";
			} else {
				possuiAlgumaUnidadeDesmarcada = true;
			}
		}
		if (!possuiAlgumaUnidadeDesmarcada) {
			getSuperParametroRelVO().adicionarParametro("unidadesEnsino", "Todas");
		} else {
			if (!aux.isEmpty()) {
				if (unidades.toString().length() > 110) {
					getSuperParametroRelVO().adicionarParametro("unidadesEnsino", unidades.toString().substring(0, 108) + "...");
				} else {
					getSuperParametroRelVO().adicionarParametro("unidadesEnsino", unidades.toString());
				}
			}
		}
		if (getCampoFiltroPor().equals("curso")) {
			getSuperParametroRelVO().adicionarParametro("campoFiltroPor", "Curso");
			StringBuilder cursos = new StringBuilder();
			String aux1 = "";
			boolean possuiAgumCursoDesmarcado = false;
			for (CursoVO c : getCursoVOs()) {
				if (c.getFiltrarCursoVO()) {
					cursos.append(aux1).append(c.getNome());
					aux1 = ",";
				} else {
					possuiAgumCursoDesmarcado = true;
				}
			}
			if (!possuiAgumCursoDesmarcado) {
				getSuperParametroRelVO().adicionarParametro("cursos", "Todos");
			} else {
				if (!aux1.isEmpty()) {
					if (cursos.toString().length() > 124) {
						getSuperParametroRelVO().adicionarParametro("cursos", cursos.toString().substring(0, 122) + "...");
					} else {
						getSuperParametroRelVO().adicionarParametro("cursos", cursos.toString());
					}
				}
			}
			StringBuilder turnos = new StringBuilder();
			String aux2 = "";
			boolean possuiAlgumTurnoDesmarcado = false;
			for (TurnoVO t : getTurnoVOs()) {
				if (t.getFiltrarTurnoVO()) {
					turnos.append(aux2).append(t.getNome());
					aux2 = ",";
				} else {
					possuiAlgumTurnoDesmarcado = true;
				}
			}
			if (!possuiAlgumTurnoDesmarcado || aux2.isEmpty()) {
				getSuperParametroRelVO().adicionarParametro("turnos", "Todos");
			} else {
				if (!aux2.isEmpty()) {
					getSuperParametroRelVO().adicionarParametro("turnos", turnos.toString());
				}
			}
		} else if (getCampoFiltroPor().equals("turma")) {
			getSuperParametroRelVO().adicionarParametro("campoFiltroPor", "Turma");
			if (getTurma().getIdentificadorTurma().isEmpty()) {
				getSuperParametroRelVO().adicionarParametro("turma", "Todas");
			} else {
				getSuperParametroRelVO().adicionarParametro("turma", getTurma().getIdentificadorTurma());
				getSuperParametroRelVO().adicionarParametro("cursos", getTurma().getCurso().getNome());
				getSuperParametroRelVO().adicionarParametro("turnos", getTurma().getTurno().getNome());
			}
		}
		
		StringBuilder situacaoAcademica = new StringBuilder();
		String auxSit = "";
		if (getFiltroRelatorioAcademicoVO().getAtivo()) {
			situacaoAcademica.append(auxSit).append("Ativo");
			auxSit = ",";
		}
		if (getFiltroRelatorioAcademicoVO().getPreMatricula()) {
			situacaoAcademica.append(auxSit).append("Pré Matrícula");
			auxSit = ",";
		}
		if (getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada()) {
			situacaoAcademica.append(auxSit).append("Pré Matrícula Cancelada");
			auxSit = ",";
		}
		if (getFiltroRelatorioAcademicoVO().getTrancado()) {
			situacaoAcademica.append(auxSit).append("Trancado");
			auxSit = ",";
		}
		if (getFiltroRelatorioAcademicoVO().getCancelado()) {
			situacaoAcademica.append(auxSit).append("Cancelado");
			auxSit = ",";
		}
		if (getFiltroRelatorioAcademicoVO().getConcluido()) {
			situacaoAcademica.append(auxSit).append("Concluido");
			auxSit = ",";
		}
		if (getFiltroRelatorioAcademicoVO().getTransferenciaInterna()) {
			situacaoAcademica.append(auxSit).append("Transferência Interna");
			auxSit = ",";
		}
		if (getFiltroRelatorioAcademicoVO().getTransferenciaExterna()) {
			situacaoAcademica.append(auxSit).append("Transferência Saida");
			auxSit = ",";
		}
		if (getFiltroRelatorioAcademicoVO().getFormado()) {
			situacaoAcademica.append(auxSit).append("Formado");
			auxSit = ",";
		}
		if (getFiltroRelatorioAcademicoVO().getAbandonado()) {
			situacaoAcademica.append(auxSit).append("Abandono de Curso");
			auxSit = ",";
		}
        getSuperParametroRelVO().adicionarParametro("situacaoAcademica", situacaoAcademica.toString());
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNivelComboboxPorListaUnidadeEnsinoIdentificadorTurma(getUnidadeEnsinoVOs(), getValorConsultaTurma(), getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNivelComboboxPorNomeCursoListaUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVOs(), getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turma");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			setTurma(obj);
			setPeriodicidade(obj.getCurso().getPeriodicidade());
			limparUnidadeEnsino();
			for (UnidadeEnsinoVO unidadeEnsinoVO : getUnidadeEnsinoVOs()) {
				if (obj.getUnidadeEnsino().getCodigo().equals(unidadeEnsinoVO.getCodigo())) {
					unidadeEnsinoVO.setFiltrarUnidadeEnsino(true);
				}
			}
			verificarTodasUnidadesSelecionadas();
			setValorConsultaTurma("");
			setCampoConsultaTurma("");
			getListaConsultaTurma().clear();
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (!getUnidadeEnsinoApresentar().equals("")) {
				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaListaUnidadeEnsino(getValorConsultaAluno(), getUnidadeEnsinoVOs(), false, "", getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCursoListaUnidadeEnsinoVOs(getValorConsultaAluno(), getUnidadeEnsinoVOs(), false, "", getUsuarioLogado());
				}
				setListaConsultaAluno(objs);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaListaUnidadeEnsinoVOs(getMatriculaVO().getMatricula(), getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (objAluno.getMatricula().equals("")) {
					throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
				}
				setMatriculaVO(objAluno);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
			MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatriculaVO(objCompleto);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarCurso() {
		try {
			consultarCursoFiltroRelatorio(getPeriodicidade());
			limparCurso();
			limparTurno();
			limparTurma();
		} catch (Exception e) {

		}
	}

	public List<SelectItem> getTipoConsultaComboSemestre() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("1", "1º"));
		itens.add(new SelectItem("2", "2º"));
		return itens;
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
				}
			}
			setUnidadeEnsinoApresentar(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setUnidadeEnsinoApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
		consultarCursoFiltroRelatorio(getPeriodicidade());
		consultarTurnoFiltroRelatorio();
	}

	public void verificarTodosCursosSelecionados() {
		StringBuilder curso = new StringBuilder();
		if (getCursoVOs().size() > 1) {
			for (CursoVO obj : getCursoVOs()) {
				if (obj.getFiltrarCursoVO()) {
					curso.append(obj.getCodigo()).append(" - ");
					curso.append(obj.getNome()).append("; ");
				}
			}
			setCursosApresentar(curso.toString());
		} else {
			if (!getCursoVOs().isEmpty()) {
				if (getCursoVOs().get(0).getFiltrarCursoVO()) {
					setCursosApresentar(getCursoVOs().get(0).getNome());
				}
			}
		}
	}

	public void verificarTodosTurnosSelecionados() {
		StringBuilder turno = new StringBuilder();
		if (getTurnoVOs().size() > 1) {
			for (TurnoVO obj : getTurnoVOs()) {
				if (obj.getFiltrarTurnoVO()) {
					turno.append(obj.getNome()).append("; ");
				}
			}
			setTurnosApresentar(turno.toString());
		} else {
			if (!getTurnoVOs().isEmpty()) {
				if (getTurnoVOs().get(0).getFiltrarTurnoVO()) {
					setTurnosApresentar(getTurnoVOs().get(0).getNome());
				}
			} else {
				setTurnosApresentar(turno.toString());
			}
		}
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>(0);
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

	public Date getPeriodoAcessoInicio() {
		if (periodoAcessoInicio == null) {
			periodoAcessoInicio = new Date();
		}
		return periodoAcessoInicio;
	}

	public void setPeriodoAcessoInicio(Date periodoAcessoInicio) {
		this.periodoAcessoInicio = periodoAcessoInicio;
	}

	public Date getPeriodoAcessoFinal() {
		if (periodoAcessoFinal == null) {
			periodoAcessoFinal = new Date();
		}
		return periodoAcessoFinal;
	}

	public void setPeriodoAcessoFinal(Date periodoAcessoFinal) {
		this.periodoAcessoFinal = periodoAcessoFinal;
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

	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
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

	public Boolean getIsFiltrarPorTurma() {
		if (getCampoFiltroPor().equals("turma")) {
			return true;
		}
		return false;
	}

	public Boolean getIsFiltrarPorCurso() {
		if (getCampoFiltroPor().equals("curso")) {
			return true;
		}
		return false;
	}

	public Boolean getIsFiltrarPorMatricula() {
		if (getCampoFiltroPor().equals("matricula")) {
			return true;
		}
		return false;
	}

	public Boolean getIsExibirFiltroAno() {
		return getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor());
	}

	public Boolean getIsExibirFiltroAnoSemestre() {
		return getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor());
	}

	public String getCampoFiltroPor() {
		if (campoFiltroPor == null) {
			campoFiltroPor = "curso";
		}
		return campoFiltroPor;
	}

	public void setCampoFiltroPor(String campoFiltroPor) {
		this.campoFiltroPor = campoFiltroPor;
	}

	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>();
			for (PeriodicidadeEnum periodicidadeEnum : PeriodicidadeEnum.values()) {
				listaSelectItemPeriodicidade.add(new SelectItem(periodicidadeEnum, periodicidadeEnum.getDescricao()));
			}
		}
		return listaSelectItemPeriodicidade;
	}

	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}

	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = "AN";
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}

	public List<SelectItem> tipoConsultaComboFiltroPor;

	public List<SelectItem> getTipoConsultaComboFiltroPor() {
		if (tipoConsultaComboFiltroPor == null) {
			tipoConsultaComboFiltroPor = new ArrayList<SelectItem>(0);
			tipoConsultaComboFiltroPor.add(new SelectItem("curso", "Curso"));
			tipoConsultaComboFiltroPor.add(new SelectItem("turma", "Turma"));
			tipoConsultaComboFiltroPor.add(new SelectItem("matricula", "Matrícula"));
		}
		return tipoConsultaComboFiltroPor;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoPeriodicidadeCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("AN", "Anual"));
		itens.add(new SelectItem("SE", "Semestral"));
		itens.add(new SelectItem("IN", "Integral"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTipoRelatorio() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("TODOS", "Todos"));
		itens.add(new SelectItem("ACESSARAM_CATRACA", "Acessaram Catraca"));
		itens.add(new SelectItem("NAO_ACESSARAM_CATRACA", "Não Acessaram Catraca"));
		return itens;
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("ANALITICO_TURMA_DIA", "Analítico por Turma e Dia"));
		itens.add(new SelectItem("ANALITICO_DIA", "Analítico por Dia"));
		return itens;
	}

	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
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

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(new MatriculaVO());
	}

	public void limparUnidadeEnsino() {
		try {
			setUnidadeEnsinoApresentar(null);
			setMarcarTodasUnidadeEnsino(false);
			marcarTodasUnidadesEnsinoAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() {
		try {
			setCursosApresentar(null);
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurno() {
		try {
			setTurnosApresentar(null);
			setMarcarTodosTurnos(false);
			marcarTodosTurnosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		setTurma(null);
	}

	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	public void marcarTodosTurnosAction() throws Exception {
		for (TurnoVO turnoVO : getTurnoVOs()) {
			turnoVO.setFiltrarTurnoVO(getMarcarTodosTurnos());
		}
		verificarTodosTurnosSelecionados();
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "TODOS";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public List<SelectItem> listaSelectItemCatraca;

	public List<SelectItem> getListaSelectItemCatraca() {
		if (listaSelectItemCatraca == null) {
			listaSelectItemCatraca = new ArrayList<SelectItem>(0);
			listaSelectItemCatraca.add(new SelectItem("", ""));
			List<CatracaVO> listaCatracaVOs = getFacadeFactory().getCatracaFacade().consultarCatracaComboBox("", getUsuarioLogado());
			for (CatracaVO catracaVO : listaCatracaVOs) {
				listaSelectItemCatraca.add(new SelectItem(catracaVO.getCodigo(), catracaVO.getDescricao()));
			}
		}
		return listaSelectItemCatraca;
	}

	public Integer getCatraca() {
		if (catraca == null) {
			catraca = 0;
		}
		return catraca;
	}

	public void setCatraca(Integer catraca) {
		this.catraca = catraca;
	}

	public List<OrdenadorVO> getListaOrdenacaoVOs() {
		if (listaOrdenacaoVOs == null) {
			listaOrdenacaoVOs = new ArrayList<OrdenadorVO>(0);
		}
		return listaOrdenacaoVOs;
	}

	public void setListaOrdenacaoVOs(List<OrdenadorVO> listaOrdenacaoVOs) {
		this.listaOrdenacaoVOs = listaOrdenacaoVOs;
	}

	public String getOrdenadorApresentar() {
		if (ordenadorApresentar == null) {
			ordenadorApresentar = "";
		}
		return ordenadorApresentar;
	}

	public void setOrdenadorApresentar(String ordenadorApresentar) {
		this.ordenadorApresentar = ordenadorApresentar;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "ANALITICO_TURMA_DIA";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public Boolean getApresentarBotaoSubirOrdenador() {
		return true;
	}

	public Boolean getApresentarBotaoDescerOrdenador() {
		return true;
	}

}
