package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AlunosMatriculadosGeralFormaIngressoRelVO;
import relatorio.negocio.comuns.academico.AlunosMatriculadosGeralRelVO;
import relatorio.negocio.comuns.academico.AlunosMatriculadosGeralSituacaoMatriculaRelVO;
import relatorio.negocio.comuns.academico.enumeradores.TipoFiltroPeriodoAcademicoEnum;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AlunosMatriculadosGeralRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Controller("AlunosMatriculadosGeralRelControle")
@Scope("viewScope")
public class AlunosMatriculadosGeralRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 608030581145169664L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private ProcSeletivoVO procSeletivoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private TurmaVO turmaVO;
	// Carouro, Veterano, Geral
	private String tipoRelatorioCalVetGer;
	private String tipoRelatorio;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemCurso;
	private List<SelectItem> listaSelectItemProcSeletivo;
	private List<SelectItem> listaSelectItemTurma;

	private Boolean trazerSomenteAlunosAtivos;
	private List<AlunosMatriculadosGeralRelVO> listaAlunosMatriculadosGeralRelVO;
	private List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs;
	private Boolean tipoRelatorioImprimirExcell;
	private Boolean tipoRelatorioImprimirPDF;
	private ProcessoMatriculaVO processoMatriculaVO;
	private List<SelectItem> listaSelectItemProcessoMatricula;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademico;
	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemAno;
	private String formaRenovacao;
	private Boolean calouro;
	private Boolean veterano;
	private Boolean transferenciaInterna;
	private Boolean transferenciaExterna;
	private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	private String turnoApresentar;
	private Boolean considerarCursosAnuaisSemestrais;
	
	private List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs;

	public AlunosMatriculadosGeralRelControle() throws Exception {
		// obterUsuarioLogado();
		getFiltroRelatorioAcademico().setPendenteFinanceiro(Boolean.TRUE);
		incializarDados();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void incializarDados() {
		montarListaSelectItemUnidadeEnsino();
		setTipoRelatorioCalVetGer("");
		setTipoRelatorio("AN");
		setCalouro(true);
		setVeterano(true);
		getFiltroRelatorioAcademico().setDataInicio(null);
		getFiltroRelatorioAcademico().setDataTermino(null);
		getFiltroRelatorioAcademico().setPendenteFinanceiro(Boolean.TRUE);
		try{
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademico(), AlunosMatriculadosGeralRel.getIdEntidade(), getUsuarioLogado());			
		}catch(Exception e){
			
		}

	}

	public void limparListasConsultas() {
		setTurmaVO(null);
		setUnidadeEnsinoCursoVO(null);
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
	}

	public void executarGeracaoRelatorio() {
		try {
			getFacadeFactory().getAlunosMatriculadosGeralRelFacade().validarDados(getFiltroRelatorioAcademico(), getUnidadeEnsinoVOs(), getCursoVOs(), getTurmaVO(), getProcessoMatriculaVO(), getTipoRelatorioCalVetGer(), getCalouro(), getVeterano(), getTransferenciaInterna(), getTransferenciaExterna(), getConsiderarCursosAnuaisSemestrais());
			imprimirPDFGeralAnalitico();
		} catch (Exception e) {
			setUsarTargetBlank("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(getListaAlunosMatriculadosGeralRelVO());
		}
	}

	public void imprimirPDFGeralAnalitico() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "AlunosMatriculadosGeralRelControle", "Inicializando Geração de Relatório Alunos Matriculados Geral (Geral Analítico)", "Emitindo Relatório");
		if (getTipoRelatorio().equals("SI")) {
			listaAlunosMatriculadosGeralRelVO = getFacadeFactory().getAlunosMatriculadosGeralRelFacade().criarObjetoSintetico(getFiltroRelatorioAcademico(), getUnidadeEnsinoVOs(), getProcessoMatriculaVO(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getTipoRelatorioCalVetGer(), getFormaRenovacao(), getCalouro(), getVeterano(), getTransferenciaInterna(), getTransferenciaExterna(), getConsiderarCursosAnuaisSemestrais());
		} else if (getTipoRelatorio().equals("SI3")) {
			listaAlunosMatriculadosGeralRelVO = getFacadeFactory().getAlunosMatriculadosGeralRelFacade().criarObjetoSinteticoPorCursoPeriodoLetivo(getFiltroRelatorioAcademico(), getUnidadeEnsinoVOs(), getProcessoMatriculaVO(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getTipoRelatorioCalVetGer(), getFormaRenovacao(), getCalouro(), getVeterano(), getTransferenciaInterna(), getTransferenciaExterna(), getConsiderarCursosAnuaisSemestrais());
		} else if (getTipoRelatorio().equals("AN3")) {
			listaAlunosMatriculadosGeralRelVO = getFacadeFactory().getAlunosMatriculadosGeralRelFacade().criarObjetoAnaliticoPorCursoPeriodoLetivo(getFiltroRelatorioAcademico(), getUnidadeEnsinoVOs(), getProcessoMatriculaVO(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getTipoRelatorioCalVetGer(), getFormaRenovacao(), getCalouro(), getVeterano(), getTransferenciaInterna(), getTransferenciaExterna(), getConsiderarCursosAnuaisSemestrais(), getListaAlunosMatriculadosGeralFormaIngressoRelVOs(), getListaAlunosMatriculadosGeralSituacaoMatriculaRelVOs());
		} else {
			listaAlunosMatriculadosGeralRelVO = getFacadeFactory().getAlunosMatriculadosGeralRelFacade().criarObjeto(getFiltroRelatorioAcademico(), getUnidadeEnsinoVOs(), getProcessoMatriculaVO(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getTipoRelatorioCalVetGer(), getFormaRenovacao(), getCalouro(), getVeterano(), getTransferenciaInterna(), getTransferenciaExterna(), getConsiderarCursosAnuaisSemestrais(), getListaAlunosMatriculadosGeralFormaIngressoRelVOs(), getListaAlunosMatriculadosGeralSituacaoMatriculaRelVOs());
		}
		getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Matriculados");
		if (!getListaAlunosMatriculadosGeralRelVO().isEmpty()) {
			if (getTipoRelatorio().equals("AN")) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorio());
			} else if (getTipoRelatorio().equals("SI")) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioSintetico());
			} else if (getTipoRelatorio().equals("AN3")) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioAnaliticoPorCursoPeriodoLetivo());
			} else if (getTipoRelatorio().equals("SI3")) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioSinteticoPorCursoPeriodoLetivo());
			} else {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioAnaliticoFormaRenovacao());
			}
			if (getTipoRelatorioImprimirExcell()) {
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				if (getTipoRelatorio().equals("AN")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioExcel());
				} else if (getTipoRelatorio().equals("AN2")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioExcelAnaliticoFormaRenovacao());
				} else if (getTipoRelatorio().equals("AN3")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioExcelAnaliticoPorCursoPeriodoLetivo());
				} else if (getTipoRelatorio().equals("SI3")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioExcelSinteticoPorCursoPeriodoLetivo());
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioExcelSintetico());
				}
			} else if (getTipoRelatorioimprimirPDF()) {
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			}

			getSuperParametroRelVO().setSubReport_Dir(AlunosMatriculadosGeralRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());

			getSuperParametroRelVO().setListaObjetos(getListaAlunosMatriculadosGeralRelVO());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosMatriculadosGeralRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			if(getFiltroRelatorioAcademico().getApresentarSubtotalFormaIngresso()){
				getSuperParametroRelVO().adicionarParametro("listaAlunosMatriculadosGeralFormaIngressoRelVOs", Ordenacao.ordenarLista(getListaAlunosMatriculadosGeralFormaIngressoRelVOs(), "descricao"));
				
			}
			getSuperParametroRelVO().adicionarParametro("listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs", Ordenacao.ordenarLista(getListaAlunosMatriculadosGeralSituacaoMatriculaRelVOs(), "descricao"));

			if (getUnidadeEnsinoCursoVO().getCodigo() > 0) {
				getSuperParametroRelVO().setCurso((getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
			} else {
				getSuperParametroRelVO().setCurso("TODOS");
			}

			if (getTurmaVO().getCodigo() > 0) {
				getSuperParametroRelVO().setTurma((getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getIdentificadorTurma());
			} else {
				getSuperParametroRelVO().setTurma("TODAS");
			}

			if ((getFiltroRelatorioAcademico().getFiltrarPorAnoSemestre() && (Uteis.isAtributoPreenchido(getTurmaVO()) || Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getCurso()))) || (getFiltroRelatorioAcademico().getFiltrarCursoSemestral() && (!Uteis.isAtributoPreenchido(getTurmaVO()) && !Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getCurso())))) {
				getSuperParametroRelVO().setSemestre(getFiltroRelatorioAcademico().getSemestre() + "º");
				getSuperParametroRelVO().setAno(getFiltroRelatorioAcademico().getAno());
			} else if ((getFiltroRelatorioAcademico().getFiltrarPorAno() && (Uteis.isAtributoPreenchido(getTurmaVO()) || Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getCurso()))) || (getFiltroRelatorioAcademico().getFiltrarCursoAnual() && (!Uteis.isAtributoPreenchido(getTurmaVO()) && !Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getCurso())))) {
				getSuperParametroRelVO().setSemestre("");
				getSuperParametroRelVO().setAno(getFiltroRelatorioAcademico().getAno());
			} else {
				getSuperParametroRelVO().setSemestre("");
				getSuperParametroRelVO().setAno("");
			}
			String retornoTipoRelatorio = "";
			if (getTipoRelatorioCalVetGer().equals("c")) {
				retornoTipoRelatorio = "Calouro";
			} else if (getTipoRelatorioCalVetGer().equals("v")) {
				retornoTipoRelatorio = "Veterano";
			} else if (getTipoRelatorioCalVetGer().equals("g")) {
				retornoTipoRelatorio = "Geral";
			}
			getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);
			if (getFormaRenovacao().equals("renovacaoOnline")) {
				getSuperParametroRelVO().adicionarParametro("formaRenovacao", "Renovação Online");
			} else if (getFormaRenovacao().equals("renovacaoSecretaria")) {
				getSuperParametroRelVO().adicionarParametro("formaRenovacao", "Renovação Secretaria");
			} else {
				getSuperParametroRelVO().adicionarParametro("formaRenovacao", "Renovação Online/Secretaria");
			}
			getSuperParametroRelVO().setDataInicio("");
			getSuperParametroRelVO().setDataFim("");
				if (Uteis.isAtributoPreenchido(getFiltroRelatorioAcademico().getDataInicio())) {
					getSuperParametroRelVO().setDataInicio(Uteis.getData(getFiltroRelatorioAcademico().getDataInicio()));
				}
				if (Uteis.isAtributoPreenchido(getFiltroRelatorioAcademico().getDataTermino())) {
					getSuperParametroRelVO().setDataFim(Uteis.getData(getFiltroRelatorioAcademico().getDataTermino()));
				}
				getSuperParametroRelVO().adicionarParametro("considerarCursosAnuaisSemestrais", getConsiderarCursosAnuaisSemestrais());
			adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
			getFiltroRelatorioAcademico().setPendenteFinanceiro(Boolean.TRUE);
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");

			removerObjetoMemoria(this);
			incializarDados();
			consultarUnidadeEnsino();

		} else {
			setMensagemID("msg_relatorio_sem_dados");
		}
		registrarAtividadeUsuario(getUsuarioLogado(), "AlunosMatriculadosGeralRelControle", "Finalizando Geração de Relatório Alunos Matriculados Geral (Geral Analítico)", "Emitindo Relatório");
	}

	public void imprimirPDFCalouroAnalitico() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosMatriculadosGeralRelControle", "Inicializando Geração de Relatório Alunos Matriculados Geral (Calouro Analítico)", "Emitindo Relatório");
			if (getTipoRelatorio().equals("AN")) {
				// listaAlunosMatriculadosGeralRelVO =
				// getFacadeFactory().getAlunosMatriculadosGeralRelFacade().criarObjeto(getFiltroRelatorioAcademico(),
				// getUnidadeEnsinoVO(), getProcessoMatriculaVO(),
				// getUnidadeEnsinoCursoVO(), getTurmaVO(),
				// getTipoRelatorioCalVetGer(), getFormaRenovacao());
			} else if (getTipoRelatorio().equals("SI")) {
				// listaAlunosMatriculadosGeralRelVO =
				// getFacadeFactory().getAlunosMatriculadosGeralRelFacade().criarObjetoSintetico(getFiltroRelatorioAcademico(),
				// getUnidadeEnsinoVO(), getProcessoMatriculaVO(),
				// getUnidadeEnsinoCursoVO(), getTurmaVO(),
				// getTipoRelatorioCalVetGer(), getFormaRenovacao());
			} else {
				// listaAlunosMatriculadosGeralRelVO =
				// getFacadeFactory().getAlunosMatriculadosGeralRelFacade().criarObjeto(getFiltroRelatorioAcademico(),
				// getUnidadeEnsinoVO(), getProcessoMatriculaVO(),
				// getUnidadeEnsinoCursoVO(), getTurmaVO(),
				// getTipoRelatorioCalVetGer(), getFormaRenovacao());
			}
			if (!getListaAlunosMatriculadosGeralRelVO().isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Matriculados Calouros");
				if (getTipoRelatorio().equals("AN")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorio());
				} else if (getTipoRelatorio().equals("SI")) {
					getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Matriculados Calouros Sintético");
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioSintetico());
				} else {
					getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Matriculados Calouros Analítico Forma Renovação");
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioAnaliticoFormaRenovacao());
				}
				if (getTipoRelatorioImprimirExcell()) {
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
					if (getTipoRelatorio().equals("AN")) {
						getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioExcel());
					} else {
						getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioExcelSintetico());
					}
				} else if (getTipoRelatorioimprimirPDF()) {
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				}
				getSuperParametroRelVO().setSubReport_Dir(AlunosMatriculadosGeralRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(getListaAlunosMatriculadosGeralRelVO());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosMatriculadosGeralRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

				getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());

				if (getUnidadeEnsinoCursoVO().getCodigo() > 0) {
					getSuperParametroRelVO().setCurso((getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
				} else {
					getSuperParametroRelVO().setCurso("TODOS");
				}

				if (getTurmaVO().getCodigo() > 0) {
					getSuperParametroRelVO().setTurma((getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getIdentificadorTurma());
				} else {
					getSuperParametroRelVO().setTurma("TODAS");
				}

				if (getFiltroRelatorioAcademico().getFiltrarPorAnoSemestre()) {
					getSuperParametroRelVO().setSemestre(getFiltroRelatorioAcademico().getSemestre() + "º");
					getSuperParametroRelVO().setAno(getFiltroRelatorioAcademico().getAno());
				} else {
					getSuperParametroRelVO().setSemestre("");
					getSuperParametroRelVO().setAno("");
				}

				String retornoTipoRelatorio = "";
				if (getTipoRelatorioCalVetGer().equals("c")) {
					retornoTipoRelatorio = "Calouro";
				} else if (getTipoRelatorioCalVetGer().equals("v")) {
					retornoTipoRelatorio = "Veterano";
				} else if (getTipoRelatorioCalVetGer().equals("g")) {
					retornoTipoRelatorio = "Geral";
				}
				getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);
				if (getFormaRenovacao().equals("renovacaoOnline")) {
					getSuperParametroRelVO().adicionarParametro("formaRenovacao", "Renovação Online");
				} else if (getFormaRenovacao().equals("renovacaoSecretaria")) {
					getSuperParametroRelVO().adicionarParametro("formaRenovacao", "Renovação Secretaria");
				} else {
					getSuperParametroRelVO().adicionarParametro("formaRenovacao", "Todos");
				}
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");

				removerObjetoMemoria(this);
				incializarDados();
				consultarUnidadeEnsino();

			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosMatriculadosGeralRelControle", "Finalizando Geração de Relatório Alunos Matriculados Geral (Calouro Analítico)", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDFVeteranoAnalitico() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "AlunosMatriculadosGeralRelControle", "Inicializando Geração de Relatório Alunos Matriculados Geral (Veterano Analítico)", "Emitindo Relatório");
		if (getTipoRelatorio().equals("AN")) {
			// listaAlunosMatriculadosGeralRelVO =
			// getFacadeFactory().getAlunosMatriculadosGeralRelFacade().criarObjeto(getFiltroRelatorioAcademico(),
			// getUnidadeEnsinoVO(), getProcessoMatriculaVO(),
			// getUnidadeEnsinoCursoVO(), getTurmaVO(),
			// getTipoRelatorioCalVetGer(), getFormaRenovacao());
		} else if (getTipoRelatorio().equals("SI")) {
			// listaAlunosMatriculadosGeralRelVO =
			// getFacadeFactory().getAlunosMatriculadosGeralRelFacade().criarObjetoSintetico(getFiltroRelatorioAcademico(),
			// getUnidadeEnsinoVO(), getProcessoMatriculaVO(),
			// getUnidadeEnsinoCursoVO(), getTurmaVO(),
			// getTipoRelatorioCalVetGer(), getFormaRenovacao());
		} else {
			// listaAlunosMatriculadosGeralRelVO =
			// getFacadeFactory().getAlunosMatriculadosGeralRelFacade().criarObjeto(getFiltroRelatorioAcademico(),
			// getUnidadeEnsinoVO(), getProcessoMatriculaVO(),
			// getUnidadeEnsinoCursoVO(), getTurmaVO(),
			// getTipoRelatorioCalVetGer(), getFormaRenovacao());
		}

		if (!getListaAlunosMatriculadosGeralRelVO().isEmpty()) {
			getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Matriculados Veteranos");
			if (getTipoRelatorio().equals("AN")) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorio());
			} else if (getTipoRelatorio().equals("SI")) {
				getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Matriculados Veteranos Sintético");
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioSintetico());
			} else {
				getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Matriculados Veteranos Analítico Forma Renovação");
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioAnaliticoFormaRenovacao());
			}

			if (getTipoRelatorioImprimirExcell()) {
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				if (getTipoRelatorio().equals("AN")) {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioExcel());
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosMatriculadosGeralRelFacade().getDesignIReportRelatorioExcelSintetico());
				}
			} else if (getTipoRelatorioimprimirPDF()) {
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			}

			getSuperParametroRelVO().setSubReport_Dir(AlunosMatriculadosGeralRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setListaObjetos(listaAlunosMatriculadosGeralRelVO);
			getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosMatriculadosGeralRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

			getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());

			if (getUnidadeEnsinoCursoVO().getCodigo() > 0) {
				getSuperParametroRelVO().setCurso((getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
			} else {
				getSuperParametroRelVO().setCurso("TODOS");
			}

			if (getTurmaVO().getCodigo() > 0) {
				getSuperParametroRelVO().setTurma((getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getIdentificadorTurma());
			} else {
				getSuperParametroRelVO().setTurma("TODAS");
			}

			if (getFiltroRelatorioAcademico().getFiltrarPorAnoSemestre()) {
				getSuperParametroRelVO().setSemestre(getFiltroRelatorioAcademico().getSemestre() + "º");
				getSuperParametroRelVO().setAno(getFiltroRelatorioAcademico().getAno());
			} else {
				getSuperParametroRelVO().setSemestre("");
				getSuperParametroRelVO().setAno("");
			}

			String retornoTipoRelatorio = "";
			if (getTipoRelatorioCalVetGer().equals("c")) {
				retornoTipoRelatorio = "Calouro";
			} else if (getTipoRelatorioCalVetGer().equals("v")) {
				retornoTipoRelatorio = "Veterano";
			} else if (getTipoRelatorioCalVetGer().equals("g")) {
				retornoTipoRelatorio = "Geral";
			}
			getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);
			if (getFormaRenovacao().equals("renovacaoOnline")) {
				getSuperParametroRelVO().adicionarParametro("formaRenovacao", "Renovação Online");
			} else if (getFormaRenovacao().equals("renovacaoSecretaria")) {
				getSuperParametroRelVO().adicionarParametro("formaRenovacao", "Renovação Secretaria");
			} else {
				getSuperParametroRelVO().adicionarParametro("formaRenovacao", "Todos");
			}

			adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");

			removerObjetoMemoria(this);
			incializarDados();
			consultarUnidadeEnsino();

		} else {
			setMensagemID("msg_relatorio_sem_dados");
		}
		registrarAtividadeUsuario(getUsuarioLogado(), "AlunosMatriculadosGeralRelControle", "Finalizando Geração de Relatório Alunos Matriculados Geral (Veterano Analítico)", "Emitindo Relatório");
	}

	public void montarListaSelectItemProcessoMatricula() {
		try {
			limparListasConsultas();
			setListaSelectItemProcessoMatricula(UtilSelectItem.getListaSelectItem(getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorNomeUnidadeEnsino("", getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), "codigo", "descricao"));
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
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCurso() throws Exception {
		List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			if (getUnidadeEnsinoVO().getCodigo() == 0) {
				setListaSelectItemTurma(new ArrayList(0));
				setListaSelectItemCurso(new ArrayList(0));
			}
			resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
			setListaSelectItemTurma(new ArrayList(0));
			setListaSelectItemCurso(new ArrayList(0));
			i = resultadoConsulta.iterator();
			getListaSelectItemCurso().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
				getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getCurso().getNome() + " - " + unidadeEnsinoCurso.getTurno().getNome()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemProcSeletivo() throws Exception {
		List<ProcSeletivoVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCursoPorProcSeletivo(getUnidadeEnsinoVO().getCodigo());
			setListaSelectItemTurma(new ArrayList(0));
			setListaSelectItemCurso(new ArrayList(0));
			i = resultadoConsulta.iterator();
			getListaSelectItemProcSeletivo().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ProcSeletivoVO procSeletivo = (ProcSeletivoVO) i.next();
				getListaSelectItemProcSeletivo().add(new SelectItem(procSeletivo.getCodigo(), procSeletivo.getDescricao()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	private List<ProcSeletivoVO> consultarCursoPorProcSeletivo(Integer codigoUnidadeEnsino) throws Exception {
		List<ProcSeletivoVO> lista = getFacadeFactory().getProcSeletivoFacade().consultarPorUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemTurma() throws Exception {
		try {
			if (getUnidadeEnsinoCursoVO().getCodigo().intValue() == 0) {
				setListaSelectItemTurma(new ArrayList(0));
			}
			setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			List<TurmaVO> resultadoConsulta = consultarTurmasPorCurso(getUnidadeEnsinoCursoVO().getCurso().getNome());
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "identificadorTurma", "identificadorTurma"));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_entre_prmconsulta");
		}
	}

	private List<TurmaVO> consultarTurmasPorCurso(String nomeCurso) throws Exception {
		List<TurmaVO> lista = getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoCursoTurno(getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoCursoVO().getTurno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	// public List getListaSelectItemTipoRelatorio() {
	// List lista = new ArrayList(0);
	// lista.add(new SelectItem("", ""));
	// // lista.add(new SelectItem("s", "Relatório Sintético"));
	// lista.add(new SelectItem("a", "Relatório Analítico"));
	// return lista;
	// }
	//

	public List<SelectItem> getListaSelectItemTipoRelatorioCalVetGer() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("c", "Calouro"));
		lista.add(new SelectItem("v", "Veterano"));
		lista.add(new SelectItem("i", "Transferência Interna"));
		lista.add(new SelectItem("e", "Transferência Externa"));
		lista.add(new SelectItem("g", "Todos"));
		return lista;
	}

	public List<SelectItem> getListaSelectItemTipoRelatorio() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("AN", "Analítico"));
		lista.add(new SelectItem("SI", "Sintético"));
		lista.add(new SelectItem("AN2", "Analítico 2"));
		lista.add(new SelectItem("AN3", "Analítico por Curso/Período Letivo"));
		lista.add(new SelectItem("SI3", "Sintético por Curso/Período Letivo"));
		return lista;
	}

	public void consultarTurma() {
		try {
			// if (getUnidadeEnsinoVO().getCodigo() == 0) {
			// throw new Exception("Informe a Unidade de Ensino.");
			// }
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			// if (getProcessoMatriculaVO().getCodigo() == 0) {
			// throw new Exception("Informe o Processo de Matrícula.");
			// }
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
			if (getTurmaVO().getCodigo() == 0) {
				setTurmaVO(null);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			if (obj.getAnual()) {
				getFiltroRelatorioAcademico().setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.ANO);
			} else if (obj.getSemestral()) {
				getFiltroRelatorioAcademico().setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE);
			} else {
				getFiltroRelatorioAcademico().setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA);
			}
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
			setMarcarTodosTurnos(false);
			marcarTodosTurnosAction();
			setCursoApresentar(getTurmaVO().getCurso().getNome());
			setTurnoApresentar(getTurmaVO().getTurno().getNome());
			realizarDefinicaoFiltroPeriodicidade();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setTurmaVO(null);
			realizarDefinicaoFiltroPeriodicidade();
			setCursoApresentar("");
			setTurnoApresentar("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			setUnidadeEnsinoCursoVO(obj);
			limparTurma();
			getListaConsultaTurma().clear();
			if (obj.getCurso().getAnual()) {
				getFiltroRelatorioAcademico().setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.ANO);
			} else if (obj.getCurso().getSemestral()) {
				getFiltroRelatorioAcademico().setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE);
			} else {
				getFiltroRelatorioAcademico().setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA);
			}
			realizarDefinicaoFiltroPeriodicidade();
		} catch (Exception e) {
		}
	}

	// public void limparCurso() throws Exception {
	// try {
	// setUnidadeEnsinoCursoVO(null);
	// setTurmaVO(null);
	// realizarDefinicaoFiltroPeriodicidade();
	// } catch (Exception e) {
	// }
	// }

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
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

	public void setTrazerSomenteAlunosAtivos(Boolean trazerSomenteAlunosAtivos) {
		this.trazerSomenteAlunosAtivos = trazerSomenteAlunosAtivos;
	}

	public Boolean getTrazerSomenteAlunosAtivos() {
		if (trazerSomenteAlunosAtivos == null) {
			trazerSomenteAlunosAtivos = true;
		}
		return trazerSomenteAlunosAtivos;
	}

	/**
	 * @return the procSeletivoVO
	 */
	public ProcSeletivoVO getProcSeletivoVO() {
		return procSeletivoVO;
	}

	/**
	 * @param procSeletivoVO
	 *            the procSeletivoVO to set
	 */
	public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
		this.procSeletivoVO = procSeletivoVO;
	}

	/**
	 * @return the listaSelectItemProcSeletivo
	 */
	public List<SelectItem> getListaSelectItemProcSeletivo() {
		return listaSelectItemProcSeletivo;
	}

	/**
	 * @param listaSelectItemProcSeletivo
	 *            the listaSelectItemProcSeletivo to set
	 */
	public void setListaSelectItemProcSeletivo(List listaSelectItemProcSeletivo) {
		this.listaSelectItemProcSeletivo = listaSelectItemProcSeletivo;
	}

	/**
	 * @return the tipoRelatorioCalVetGer
	 */
	public String getTipoRelatorioCalVetGer() {
		if (tipoRelatorioCalVetGer == null) {
			tipoRelatorioCalVetGer = "";
		}
		return tipoRelatorioCalVetGer;
	}

	/**
	 * @param tipoRelatorioCalVetGer
	 *            the tipoRelatorioCalVetGer to set
	 */
	public void setTipoRelatorioCalVetGer(String tipoRelatorioCalVetGer) {
		this.tipoRelatorioCalVetGer = tipoRelatorioCalVetGer;
	}

	public List<AlunosMatriculadosGeralRelVO> getListaAlunosMatriculadosGeralRelVO() {
		return listaAlunosMatriculadosGeralRelVO;
	}

	public void setListaAlunosMatriculadosGeralRelVO(List<AlunosMatriculadosGeralRelVO> listaAlunosMatriculadosGeralRelVO) {
		this.listaAlunosMatriculadosGeralRelVO = listaAlunosMatriculadosGeralRelVO;
	}

	public Boolean getTipoRelatorioImprimirExcell() {
		return tipoRelatorioImprimirExcell;
	}

	public void setTipoRelatorioImprimirExcell(Boolean tipoRelatorioImprimirExcell) {
		this.tipoRelatorioImprimirExcell = tipoRelatorioImprimirExcell;
	}

	public Boolean getTipoRelatorioimprimirPDF() {
		return tipoRelatorioImprimirPDF;
	}

	public void setTipoRelatorioImprimirPDF(Boolean tipoRelatorioImprimirPDF) {
		this.tipoRelatorioImprimirPDF = tipoRelatorioImprimirPDF;
	}

	public void setProcessoMatriculaVO(ProcessoMatriculaVO processoMatriculaVO) {
		this.processoMatriculaVO = processoMatriculaVO;
	}

	public ProcessoMatriculaVO getProcessoMatriculaVO() {
		if (processoMatriculaVO == null) {
			processoMatriculaVO = new ProcessoMatriculaVO();
		}
		return processoMatriculaVO;
	}

	public void setListaSelectItemProcessoMatricula(List<SelectItem> listaSelectItemProcessoMatricula) {
		this.listaSelectItemProcessoMatricula = listaSelectItemProcessoMatricula;
	}

	public List<SelectItem> getListaSelectItemProcessoMatricula() {
		if (listaSelectItemProcessoMatricula == null) {
			listaSelectItemProcessoMatricula = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProcessoMatricula;
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	/**
	 * @return the tipoRelatorio
	 */
	public String getTipoRelatorio() {
		return tipoRelatorio;
	}

	/**
	 * @param tipoRelatorio
	 *            the tipoRelatorio to set
	 */
	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public List<SelectItem> listaSelectItemTipoFiltroPeriodoAcademico;

	public List<SelectItem> getListaSelectItemTipoFiltroPeriodoAcademico() {
		if (listaSelectItemTipoFiltroPeriodoAcademico == null) {
			listaSelectItemTipoFiltroPeriodoAcademico = new ArrayList<SelectItem>(0);
			if (getFiltroRelatorioAcademico().getFiltrarCursoAnual() && !getFiltroRelatorioAcademico().getFiltrarCursoSemestral() && !getFiltroRelatorioAcademico().getFiltrarCursoIntegral()) {
				listaSelectItemTipoFiltroPeriodoAcademico.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO, "Ano"));
				listaSelectItemTipoFiltroPeriodoAcademico.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO_PERIODO_DATA, "Ano e Período Matrícula"));
				listaSelectItemTipoFiltroPeriodoAcademico.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA, "Período Matrícula"));
			} else if (!getFiltroRelatorioAcademico().getFiltrarCursoAnual() && getFiltroRelatorioAcademico().getFiltrarCursoSemestral() && !getFiltroRelatorioAcademico().getFiltrarCursoIntegral()) {
				listaSelectItemTipoFiltroPeriodoAcademico.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE, "Ano e Semestre"));
				listaSelectItemTipoFiltroPeriodoAcademico.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA, "Período Matrícula"));
				listaSelectItemTipoFiltroPeriodoAcademico.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.AMBOS, "Ano, Semestre e Período Matrícula"));
			} else if (!getFiltroRelatorioAcademico().getFiltrarCursoAnual() && !getFiltroRelatorioAcademico().getFiltrarCursoSemestral() && getFiltroRelatorioAcademico().getFiltrarCursoIntegral()) {
				listaSelectItemTipoFiltroPeriodoAcademico.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA, "Período Matrícula"));
				// }else
				// if(!getFiltroRelatorioAcademico().getFiltrarCursoAnual() &&
				// getSemestral() && getIntegral()){
				// listaSelectItemTipoFiltroPeriodoAcademico.add(new
				// SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE, "Ano
				// e Semestre"));
				// listaSelectItemTipoFiltroPeriodoAcademico.add(new
				// SelectItem(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA,
				// "Período Matrícula"));
				// listaSelectItemTipoFiltroPeriodoAcademico.add(new
				// SelectItem(TipoFiltroPeriodoAcademicoEnum.AMBOS, "Ano,
				// Semestre e Período Matrícula"));
				// }else if(getFiltroRelatorioAcademico().getFiltrarCursoAnual()
				// && !getSemestral() && getIntegral()){
				// listaSelectItemTipoFiltroPeriodoAcademico.add(new
				// SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO, "Ano"));
				// listaSelectItemTipoFiltroPeriodoAcademico.add(new
				// SelectItem(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA,
				// "Período Matrícula"));
				// listaSelectItemTipoFiltroPeriodoAcademico.add(new
				// SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO_PERIODO_DATA,
				// "Ano e Período Matrícula"));
				// }else if(getFiltroRelatorioAcademico().getFiltrarCursoAnual()
				// && getSemestral() && !getIntegral()){
				// listaSelectItemTipoFiltroPeriodoAcademico.add(new
				// SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE, "Ano
				// e Semestre"));
				// listaSelectItemTipoFiltroPeriodoAcademico.add(new
				// SelectItem(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA,
				// "Período Matrícula"));
				// listaSelectItemTipoFiltroPeriodoAcademico.add(new
				// SelectItem(TipoFiltroPeriodoAcademicoEnum.AMBOS, "Ano,
				// Semestre e Período Matrícula"));
			} else {
				listaSelectItemTipoFiltroPeriodoAcademico = TipoFiltroPeriodoAcademicoEnum.getListaSelectItem();
			}
		}
		return listaSelectItemTipoFiltroPeriodoAcademico;
	}

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademico() {
		if (filtroRelatorioAcademico == null) {
			filtroRelatorioAcademico = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademico;
	}

	public void setFiltroRelatorioAcademico(FiltroRelatorioAcademicoVO filtroRelatorioAcademico) {
		this.filtroRelatorioAcademico = filtroRelatorioAcademico;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}

	public List<SelectItem> getListaSelectItemAno() {
		try {
			if (listaSelectItemAno == null) {
				listaSelectItemAno = new ArrayList<SelectItem>(0);
				List<String> anos = getFacadeFactory().getMatriculaPeriodoFacade().consultarAnosMatriculaPeriodo();			
				for (String ano : anos) {
					listaSelectItemAno.add(new SelectItem(ano, ano));
				}
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return listaSelectItemAno;
	}

	public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
		this.listaSelectItemAno = listaSelectItemAno;
	}

	public List<SelectItem> getListaSelectItemFormaRenovacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("ambas", "Renovação Online/Secretaria"));
		itens.add(new SelectItem("renovacaoOnline", "Renovação Online"));
		itens.add(new SelectItem("renovacaoSecretaria", "Renovação Secretaria"));
		return itens;
	}

	public String getFormaRenovacao() {
		if (formaRenovacao == null) {
			formaRenovacao = "";
		}
		return formaRenovacao;
	}

	public void setFormaRenovacao(String formaRenovacao) {
		this.formaRenovacao = formaRenovacao;
	}

	public boolean getIsApresentarFormaRenovacao() {
		return getTipoRelatorio().equals("AN2");
	}

	public void adicionarFiltroSituacaoAcademica(SuperParametroRelVO superParametroRelVO) {
		superParametroRelVO.adicionarParametro("filtroAcademicoAtivo", getFiltroRelatorioAcademico().getAtivo());
		superParametroRelVO.adicionarParametro("filtroAcademicoTrancado", getFiltroRelatorioAcademico().getTrancado());
		superParametroRelVO.adicionarParametro("filtroAcademicoCancelado", getFiltroRelatorioAcademico().getCancelado());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatricula", getFiltroRelatorioAcademico().getPreMatricula());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatriculaCancelada", getFiltroRelatorioAcademico().getPreMatriculaCancelada());
		superParametroRelVO.adicionarParametro("filtroAcademicoConcluido", getFiltroRelatorioAcademico().getConcluido());
		superParametroRelVO.adicionarParametro("filtroAcademicoPendenteFinanceiro", getFiltroRelatorioAcademico().getPendenteFinanceiro());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaExterna", getFiltroRelatorioAcademico().getTransferenciaExterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaInterna", getFiltroRelatorioAcademico().getTransferenciaInterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoAbandonado", getFiltroRelatorioAcademico().getAbandonado());
		superParametroRelVO.adicionarParametro("filtroAcademicoFormado", getFiltroRelatorioAcademico().getFormado());
		superParametroRelVO.adicionarParametro("filtroAcademicoMatriculaAReceber", getFiltroRelatorioAcademico().getPendenteFinanceiro());
		superParametroRelVO.adicionarParametro("filtroAcademicoMatriculaRecebida", getFiltroRelatorioAcademico().getConfirmado());
		superParametroRelVO.adicionarParametro("filtroAcademicoJubilado", getFiltroRelatorioAcademico().getJubilado());

		superParametroRelVO.adicionarParametro("filtroCalouro", getCalouro());
		superParametroRelVO.adicionarParametro("filtroVeterano", getVeterano());
		superParametroRelVO.adicionarParametro("filtroTransInterna", getTransferenciaInterna());
		superParametroRelVO.adicionarParametro("filtroTransExterna", getTransferenciaExterna());

		superParametroRelVO.adicionarParametro("filtrarCursoAnual", getFiltroRelatorioAcademico().getFiltrarCursoAnual());
		superParametroRelVO.adicionarParametro("filtrarCursoSemestral", getFiltroRelatorioAcademico().getFiltrarCursoSemestral());
		superParametroRelVO.adicionarParametro("filtrarCursoIntegral", getFiltroRelatorioAcademico().getFiltrarCursoIntegral());
		superParametroRelVO.adicionarParametro("apresentarSubtotalFormaIngresso", getFiltroRelatorioAcademico().getApresentarSubtotalFormaIngresso());
		try{
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademico(), AlunosMatriculadosGeralRel.getIdEntidade(), getUsuarioLogado());
		}catch(Exception e){
			
		}

	}

	public Boolean getCalouro() {
		if (calouro == null) {
			calouro = Boolean.FALSE;
		}
		return calouro;
	}

	public void setCalouro(Boolean calouro) {
		this.calouro = calouro;
	}

	public Boolean getVeterano() {
		if (veterano == null) {
			veterano = Boolean.FALSE;
		}
		return veterano;
	}

	public void setVeterano(Boolean veterano) {
		this.veterano = veterano;
	}

	public Boolean getTransferenciaInterna() {
		if (transferenciaInterna == null) {
			transferenciaInterna = Boolean.FALSE;
		}
		return transferenciaInterna;
	}

	public void setTransferenciaInterna(Boolean transferenciaInterna) {
		this.transferenciaInterna = transferenciaInterna;
	}

	public Boolean getTransferenciaExterna() {
		if (transferenciaExterna == null) {
			transferenciaExterna = Boolean.FALSE;
		}
		return transferenciaExterna;
	}

	public void setTransferenciaExterna(Boolean transferenciaExterna) {
		this.transferenciaExterna = transferenciaExterna;
	}

	public boolean getIsApresentarFiltroPeriodoAnoSemestre() {
		return Uteis.isAtributoPreenchido(getTurmaVO()) || Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getCurso());
	}

	public void realizarDefinicaoFiltroPeriodicidade() {
		if (Uteis.isAtributoPreenchido(getTurmaVO())) {
			if (getTurmaVO().getAnual()) {
				getFiltroRelatorioAcademico().setFiltrarCursoAnual(true);
				getFiltroRelatorioAcademico().setFiltrarCursoSemestral(false);
				getFiltroRelatorioAcademico().setFiltrarCursoIntegral(false);
			} else if (getTurmaVO().getSemestral()) {
				getFiltroRelatorioAcademico().setFiltrarCursoAnual(false);
				getFiltroRelatorioAcademico().setFiltrarCursoSemestral(true);
				getFiltroRelatorioAcademico().setFiltrarCursoIntegral(false);
			} else {
				getFiltroRelatorioAcademico().setFiltrarCursoAnual(false);
				getFiltroRelatorioAcademico().setFiltrarCursoSemestral(false);
				getFiltroRelatorioAcademico().setFiltrarCursoIntegral(true);
			}
		} else if (Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getCurso())) {
			if (getUnidadeEnsinoCursoVO().getCurso().getAnual()) {
				getFiltroRelatorioAcademico().setFiltrarCursoAnual(true);
				getFiltroRelatorioAcademico().setFiltrarCursoSemestral(false);
				getFiltroRelatorioAcademico().setFiltrarCursoIntegral(false);
			} else if (getUnidadeEnsinoCursoVO().getCurso().getSemestral()) {
				getFiltroRelatorioAcademico().setFiltrarCursoAnual(false);
				getFiltroRelatorioAcademico().setFiltrarCursoSemestral(true);
				getFiltroRelatorioAcademico().setFiltrarCursoIntegral(false);
			} else {
				getFiltroRelatorioAcademico().setFiltrarCursoAnual(false);
				getFiltroRelatorioAcademico().setFiltrarCursoSemestral(false);
				getFiltroRelatorioAcademico().setFiltrarCursoIntegral(true);
			}
		} else {
			getFiltroRelatorioAcademico().setFiltrarCursoAnual(true);
			getFiltroRelatorioAcademico().setFiltrarCursoSemestral(true);
			getFiltroRelatorioAcademico().setFiltrarCursoIntegral(true);
		}
		listaSelectItemTipoFiltroPeriodoAcademico = null;
	}

	public boolean getApresentarFiltroAno() {
		return (!getIsApresentarFiltroPeriodoAnoSemestre() || (getIsApresentarFiltroPeriodoAnoSemestre() && (!getFiltroRelatorioAcademico().getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA)))) && (getFiltroRelatorioAcademico().getFiltrarCursoAnual() || getFiltroRelatorioAcademico().getFiltrarCursoSemestral());
	}

	public boolean getApresentarFiltroSemestre() {
		return (!getIsApresentarFiltroPeriodoAnoSemestre() || (getIsApresentarFiltroPeriodoAnoSemestre() && (!getFiltroRelatorioAcademico().getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA)) && (!getFiltroRelatorioAcademico().getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.ANO)) && (!getFiltroRelatorioAcademico().getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.ANO_PERIODO_DATA)))) && getFiltroRelatorioAcademico().getFiltrarCursoSemestral();
	}

	public boolean getApresentarFiltroPeriodo() {
		return ((getFiltroRelatorioAcademico().getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA)) || (getFiltroRelatorioAcademico().getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.ANO_PERIODO_DATA)) || (getFiltroRelatorioAcademico().getTipoFiltroPeriodoAcademico().equals(TipoFiltroPeriodoAcademicoEnum.AMBOS))) || !Uteis.isAtributoPreenchido(getTurmaVO());
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
		consultarCursoFiltroRelatorio("");
		consultarTurnoFiltroRelatorio();
	}

	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
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
			setCursoApresentar(curso.toString());
		} else {
			if (!getCursoVOs().isEmpty()) {
				if (getCursoVOs().get(0).getFiltrarCursoVO()) {
					setCursoApresentar(getCursoVOs().get(0).getNome());
				}
			}
		}
	}

	public void marcarTodosTurnosAction() throws Exception {
		for (TurnoVO turnoVO : getTurnoVOs()) {
			turnoVO.setFiltrarTurnoVO(getMarcarTodosTurnos());
		}
		verificarTodosTurnosSelecionados();
	}

	public void verificarTodosTurnosSelecionados() {
		StringBuilder turno = new StringBuilder();
		if (getTurnoVOs().size() > 1) {
			for (TurnoVO obj : getTurnoVOs()) {
				if (obj.getFiltrarTurnoVO()) {
					turno.append(obj.getNome()).append("; ");
				}
			}
			setTurnoApresentar(turno.toString());
		} else {
			if (!getTurnoVOs().isEmpty()) {
				if (getTurnoVOs().get(0).getFiltrarTurnoVO()) {
					setTurnoApresentar(getTurnoVOs().get(0).getNome());
				}
			} else {
				setTurnoApresentar(turno.toString());
			}
		}
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

	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public String getTurnoApresentar() {
		if (turnoApresentar == null) {
			turnoApresentar = "";
		}
		return turnoApresentar;
	}

	public void setTurnoApresentar(String turnoApresentar) {
		this.turnoApresentar = turnoApresentar;
	}

	public void limparCurso() {
		try {
			setCursoApresentar(null);
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurno() {
		try {
			setTurnoApresentar(null);
			setMarcarTodosTurnos(false);
			marcarTodosTurnosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getConsiderarCursosAnuaisSemestrais() {
		if (considerarCursosAnuaisSemestrais == null) {
			considerarCursosAnuaisSemestrais = Boolean.FALSE;
		}
		return considerarCursosAnuaisSemestrais;
	}

	public void setConsiderarCursosAnuaisSemestrais(Boolean considerarCursosAnuaisSemestrais) {
		this.considerarCursosAnuaisSemestrais = considerarCursosAnuaisSemestrais;
	}
	
	public List<AlunosMatriculadosGeralFormaIngressoRelVO> getListaAlunosMatriculadosGeralFormaIngressoRelVOs() {
		if(listaAlunosMatriculadosGeralFormaIngressoRelVOs ==  null){
			listaAlunosMatriculadosGeralFormaIngressoRelVOs = new ArrayList<AlunosMatriculadosGeralFormaIngressoRelVO>();
		}
		return listaAlunosMatriculadosGeralFormaIngressoRelVOs;
	}

	public void setListaAlunosMatriculadosGeralFormaIngressoRelVOs(List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs) {
		this.listaAlunosMatriculadosGeralFormaIngressoRelVOs = listaAlunosMatriculadosGeralFormaIngressoRelVOs;
	}

	public List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> getListaAlunosMatriculadosGeralSituacaoMatriculaRelVOs() {
		if(listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs ==  null){
			listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs = new ArrayList<AlunosMatriculadosGeralSituacaoMatriculaRelVO>();
		}
		return listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs;
	}

	public void setListaAlunosMatriculadosGeralSituacaoMatriculaRelVOs(
			List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs) {
		this.listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs = listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs;
	}
	
	

}