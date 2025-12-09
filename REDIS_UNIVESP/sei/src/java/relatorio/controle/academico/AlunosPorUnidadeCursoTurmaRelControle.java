package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AlunosPorUnidadeCursoTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AlunosPorUnidadeCursoTurmaRel;

@SuppressWarnings("unchecked")
@Controller("AlunosPorUnidadeCursoTurmaRelControle")
@Scope("viewScope")
@Lazy
public class AlunosPorUnidadeCursoTurmaRelControle extends SuperControleRelatorio {

	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private String ano;
	private String semestre;
	private List listaSelectItemUnidadeEnsino;
	private List listaSelectItemCurso;
	private List listaSelectItemTurma;
	private Boolean trazerSomenteAlunosAtivos;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List listaConsultaDisciplina;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private String tipoMatricula;
	private String tipoAluno;
	private String filtroTipoCursoAluno;
	private String tipoRelatorio;
	private Boolean trazerFiliacao;	
	private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	private String turnoApresentar;
	private String situacaoAlunoCurso;
	private List<SelectItem> listaSelectItemDisciplina;
	private Boolean realizarQuebraPaginaTurma;
	private Boolean trazerAlunoTransferencia;	
	private PeriodicidadeEnum periodicidade;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private String campoFiltroPor;
	

	public AlunosPorUnidadeCursoTurmaRelControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		incializarDados();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void incializarDados() {
        getFiltroRelatorioAcademicoVO().setDataInicio(null);
        getFiltroRelatorioAcademicoVO().setDataTermino(null);
        inicializarDadosPadroes();
    }

	public void limparListasConsultas() {
		setTurmaVO(null);
		setUnidadeEnsinoCursoVO(null);
		setDisciplinaVO(null);
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
		getListaConsultaDisciplina().clear();
	}

	public void adicionarFiltroSituacaoAcademica(SuperParametroRelVO superParametroRelVO) {
		superParametroRelVO.adicionarParametro("filtroAcademicoAtivo", getFiltroRelatorioAcademicoVO().getAtivo());
		superParametroRelVO.adicionarParametro("filtroAcademicoTrancado", getFiltroRelatorioAcademicoVO().getTrancado());
		superParametroRelVO.adicionarParametro("filtroAcademicoCancelado", getFiltroRelatorioAcademicoVO().getCancelado());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatricula", getFiltroRelatorioAcademicoVO().getPreMatricula());
		superParametroRelVO.adicionarParametro("filtroAcademicoPreMatriculaCancelada", getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada());
		superParametroRelVO.adicionarParametro("filtroAcademicoConcluido", getFiltroRelatorioAcademicoVO().getConcluido());
		superParametroRelVO.adicionarParametro("filtroAcademicoPendenteFinanceiro", getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaExterna", getFiltroRelatorioAcademicoVO().getTransferenciaExterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoTransferenciaInterna", getFiltroRelatorioAcademicoVO().getTransferenciaInterna());
		superParametroRelVO.adicionarParametro("filtroAcademicoAbandonado", getFiltroRelatorioAcademicoVO().getAbandonado());
		superParametroRelVO.adicionarParametro("filtroAcademicoFormado", getFiltroRelatorioAcademicoVO().getFormado());
		superParametroRelVO.adicionarParametro("filtroAcademicoMatriculaAReceber", getFiltroRelatorioAcademicoVO().getPendenteFinanceiro());
		superParametroRelVO.adicionarParametro("filtroAcademicoMatriculaRecebida", getFiltroRelatorioAcademicoVO().getConfirmado());
	}

	public void imprimirPDF() {
		String design = null;
		List listaObjetos = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorUnidadeCursoTurmaRelControle", "Inicializando Geração de Relatório Alunos Por Unidade Curso Turma", "Emitindo Relatório");
			AlunosPorUnidadeCursoTurmaRel.validarDados(getUnidadeEnsinoVOs());
			getFacadeFactory().getAlunosPorUnidadeCursoTurmaRelFacade().validarDadosAnoSemestre(getPeriodicidade(), getAno(), getSemestre(), getCampoFiltroPor());
			design = AlunosPorUnidadeCursoTurmaRel.getDesignIReportRelatorio();
			listaObjetos = getFacadeFactory().getAlunosPorUnidadeCursoTurmaRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), getTipoMatricula(), false, getTrazerFiliacao(), getTipoAluno(), getConfiguracaoFinanceiroPadraoSistema(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado(), getSituacaoAlunoCurso(), getTrazerAlunoTransferencia(), getPeriodicidade().getValor());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(AlunosPorUnidadeCursoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setQuantidade(inicializarDadosQtdeAlunos(listaObjetos));
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosPorUnidadeCursoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().adicionarParametro("realizarQuebraPaginaTurma", getRealizarQuebraPaginaTurma());
				adicionarParamentrosComuns();
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
				
				realizarImpressaoRelatorio();
				persistirDadosPadroes();
				removerObjetoMemoria(this);
				incializarDados();
				consultarUnidadeEnsino();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorUnidadeCursoTurmaRelControle", "Finalizando Geração de Relatório Alunos Por Unidade Curso Turma", "Emitindo Relatório");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			design = null;
		}
	}

	public void adicionarParamentrosComuns() throws Exception {
		getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
		getSuperParametroRelVO().setNomeEmpresa("");
		getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
		if (getTipoAluno().equals("todos")) {
			getSuperParametroRelVO().adicionarParametro("tipoAluno", "Todos");
		} else if (getTipoAluno().equals("normal")) {
			getSuperParametroRelVO().adicionarParametro("tipoAluno", "Normal");
		} else if (getTipoAluno().equals("reposicao")) {
			getSuperParametroRelVO().adicionarParametro("tipoAluno", "Reposição/Inclusão");
		}
		if (getTipoMatricula().equals("NO")) {
			getSuperParametroRelVO().adicionarParametro("tipoMatricula", "CONTRATO PÓS");
		} else if (getTipoMatricula().equals("EX")) {
			getSuperParametroRelVO().adicionarParametro("tipoMatricula", "CONTRATO EXTENSÃƒO");
		} else if (getTipoMatricula().equals("MO")) {
			getSuperParametroRelVO().adicionarParametro("tipoMatricula", "CONTRATO ESPECIAL");
		}
		getSuperParametroRelVO().adicionarParametro("ano", getAno());
		getSuperParametroRelVO().adicionarParametro("semestre", getSemestre());
		if (getSituacaoAlunoCurso().equals("calouro")) {
			getSuperParametroRelVO().adicionarParametro("situacaoAlunoCurso", "Calouro");
		} else if (getSituacaoAlunoCurso().equals("veterano")) {
			getSuperParametroRelVO().adicionarParametro("situacaoAlunoCurso", "Veterano");
		} else {
			getSuperParametroRelVO().adicionarParametro("situacaoAlunoCurso", "Ambos");
		}
		getSuperParametroRelVO().adicionarParametro("dataInicio", getFiltroRelatorioAcademicoVO().getDataInicio());
		getSuperParametroRelVO().adicionarParametro("dataTermino", getFiltroRelatorioAcademicoVO().getDataTermino());
		getSuperParametroRelVO().adicionarParametro("disciplina", getDisciplinaVO().getNome());
		getSuperParametroRelVO().adicionarParametro("unidadeEnsinoFiltro", getUnidadeEnsinoApresentar());
		getSuperParametroRelVO().adicionarParametro("cursoFiltro", getCursoApresentar());
		getSuperParametroRelVO().adicionarParametro("turnoFiltro",getTurnoApresentar());
		if(Uteis.isAtributoPreenchido(turmaVO.getCodigo()) && Uteis.isAtributoPreenchido(getDisciplinaVO().getCodigo())){
			DisciplinaVO disc = (DisciplinaVO) getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(), 0, getUsuarioLogado());
			getSuperParametroRelVO().adicionarParametro("disciplinaFiltro",disc.getNome());
		}else{
			getSuperParametroRelVO().adicionarParametro("disciplinaFiltro",getDisciplinaVO().getNome());
		}
		getSuperParametroRelVO().adicionarParametro("turmaFiltro", getTurmaVO().getIdentificadorTurma());
		
		
		// StringBuilder situacoes = new StringBuilder("");
		// if(getFiltroRelatorioAcademicoVO().getAtivo()){
		// if(!situacoes.toString().isEmpty()){
		// situacoes.append(", ");
		// }
		// situacoes.append(SituacaoMatriculaPeriodoEnum.ATIVA.getDescricao());
		// }
		// if(getFiltroRelatorioAcademicoVO().getPreMatricula()){
		// if(!situacoes.toString().isEmpty()){
		// situacoes.append(", ");
		// }
		// situacoes.append(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getDescricao());
		// }
		// if(getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada()){
		// if(!situacoes.toString().isEmpty()){
		// situacoes.append(", ");
		// }
		// situacoes.append(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA_CANCELADA.getDescricao());
		// }
		// if(getFiltroRelatorioAcademicoVO().getTrancado()){
		// if(!situacoes.toString().isEmpty()){
		// situacoes.append(", ");
		// }
		// situacoes.append(SituacaoMatriculaPeriodoEnum.TRANCADA.getDescricao());
		// }
		// if(getFiltroRelatorioAcademicoVO().getCancelado()){
		// if(!situacoes.toString().isEmpty()){
		// situacoes.append(", ");
		// }
		// situacoes.append(SituacaoMatriculaPeriodoEnum.CANCELADA.getDescricao());
		// }
		// if(getFiltroRelatorioAcademicoVO().getConcluido()){
		// if(!situacoes.toString().isEmpty()){
		// situacoes.append(", ");
		// }
		// situacoes.append(SituacaoMatriculaPeriodoEnum.FINALIZADA.getDescricao());
		// }
		// if(getFiltroRelatorioAcademicoVO().getTransferenciaInterna()){
		// if(!situacoes.toString().isEmpty()){
		// situacoes.append(", ");
		// }
		// situacoes.append(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA.getDescricao());
		// }
		// if(getFiltroRelatorioAcademicoVO().getTransferenciaExterna()){
		// if(!situacoes.toString().isEmpty()){
		// situacoes.append(", ");
		// }
		// situacoes.append(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getDescricao());
		// }
		// if(getFiltroRelatorioAcademicoVO().getFormado()){
		// if(!situacoes.toString().isEmpty()){
		// situacoes.append(", ");
		// }
		// situacoes.append(SituacaoMatriculaPeriodoEnum.FORMADO.getDescricao());
		// }
		// if(getFiltroRelatorioAcademicoVO().getAbandonado()){
		// if(!situacoes.toString().isEmpty()){
		// situacoes.append(", ");
		// }
		// situacoes.append(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO.getDescricao());
		// }
		// getSuperParametroRelVO().adicionarParametro("situacoes",
		// situacoes.toString());
		// if((getFiltroRelatorioAcademicoVO().getMatriculaAReceber() &&
		// getFiltroRelatorioAcademicoVO().getMatriculaRecebida())
		// || (!getFiltroRelatorioAcademicoVO().getMatriculaAReceber() &&
		// !getFiltroRelatorioAcademicoVO().getMatriculaRecebida())){
		// getSuperParametroRelVO().adicionarParametro("situacaoParcelaMatricula",
		// "A Receber, Recebida");
		// }else if(getFiltroRelatorioAcademicoVO().getMatriculaAReceber() &&
		// !getFiltroRelatorioAcademicoVO().getMatriculaRecebida()){
		// getSuperParametroRelVO().adicionarParametro("situacaoParcelaMatricula",
		// "A Receber");
		// }else if(!getFiltroRelatorioAcademicoVO().getMatriculaAReceber() &&
		// getFiltroRelatorioAcademicoVO().getMatriculaRecebida()){
		// getSuperParametroRelVO().adicionarParametro("situacaoParcelaMatricula",
		// "Recebida");
		// }

		// getSuperParametroRelVO().adicionarParametro("alunoAtivo",getTrazerSomenteAlunosAtivos());
	}

	public void imprimirExcel() {
		String design = null;
		List listaObjetos = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorUnidadeCursoTurmaRelControle", "Inicializando Geração de Relatório Alunos Por Unidade Curso Turma", "Emitindo Relatório");
			AlunosPorUnidadeCursoTurmaRel.validarDados(getUnidadeEnsinoVOs());
			getFacadeFactory().getAlunosPorUnidadeCursoTurmaRelFacade().validarDadosAnoSemestre(getPeriodicidade(), getAno(), getSemestre(), getCampoFiltroPor());
			design = AlunosPorUnidadeCursoTurmaRel.getDesignIReportRelatorioExcel();
			listaObjetos = getFacadeFactory().getAlunosPorUnidadeCursoTurmaRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), getTipoMatricula(), false, getTrazerFiliacao(), getTipoAluno(), getConfiguracaoFinanceiroPadraoSistema(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado(), getSituacaoAlunoCurso(), getTrazerAlunoTransferencia(), getPeriodicidade().getValor());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(AlunosPorUnidadeCursoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosPorUnidadeCursoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setQuantidade(inicializarDadosQtdeAlunos(listaObjetos));
				adicionarParamentrosComuns();
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
				realizarImpressaoRelatorio();
				persistirDadosPadroes();
				removerObjetoMemoria(this);
				incializarDados();
				consultarUnidadeEnsino();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorUnidadeCursoTurmaRelControle", "Finalizando Geração de Relatório Alunos Por Unidade Curso Turma", "Emitindo Relatório");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			design = null;
		}
	}

	public void imprimirExcelSintetico() {
		String design = null;
		List listaObjetos = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorUnidadeCursoTurmaRelControle", "Inicializando Geração de Relatório Alunos Por Unidade Curso Turma", "Emitindo Relatório");
			AlunosPorUnidadeCursoTurmaRel.validarDados(getUnidadeEnsinoVOs());
			getFacadeFactory().getAlunosPorUnidadeCursoTurmaRelFacade().validarDadosAnoSemestre(getPeriodicidade(), getAno(), getSemestre(), getCampoFiltroPor());
			design = AlunosPorUnidadeCursoTurmaRel.getDesignIReportRelatorioSinteticoExcel();
			listaObjetos = getFacadeFactory().getAlunosPorUnidadeCursoTurmaRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), getTipoMatricula(), getTrazerSomenteAlunosAtivos(), getTrazerFiliacao(), getTipoAluno(), getConfiguracaoFinanceiroPadraoSistema(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado(), getSituacaoAlunoCurso(), getTrazerAlunoTransferencia(), getPeriodicidade().getValor());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
				getSuperParametroRelVO().setSubReport_Dir(AlunosPorUnidadeCursoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosPorUnidadeCursoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setQuantidade(inicializarDadosQtdeAlunos(listaObjetos));
				adicionarParamentrosComuns();
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
				realizarImpressaoRelatorio();
				persistirDadosPadroes();
				removerObjetoMemoria(this);
				incializarDados();
				consultarUnidadeEnsino();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "AlunosPorUnidadeCursoTurmaRelControle", "Finalizando Geração de Relatório Alunos Por Unidade Curso Turma", "Emitindo Relatório");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			design = null;
		}
	}

	public Integer inicializarDadosQtdeAlunos(List<AlunosPorUnidadeCursoTurmaRelVO> listaObjetos) {
		for (AlunosPorUnidadeCursoTurmaRelVO obj : listaObjetos) {
			// return obj.getQtdeAlunos();
		}
		return 0;
	}

	public void imprimirPDFSintetico() {
		String design = null;
		List listaObjetos = null;
		try {
			AlunosPorUnidadeCursoTurmaRel.validarDados(getUnidadeEnsinoVOs());
			getFacadeFactory().getAlunosPorUnidadeCursoTurmaRelFacade().validarDadosAnoSemestre(getPeriodicidade(), getAno(), getSemestre(), getCampoFiltroPor());
			design = AlunosPorUnidadeCursoTurmaRel.getDesignIReportRelatorioSintetico();
			listaObjetos = getFacadeFactory().getAlunosPorUnidadeCursoTurmaRelFacade().criarObjeto(getUnidadeEnsinoVOs(), getCursoVOs(), getTurnoVOs(), getTurmaVO(), getDisciplinaVO(), getAno(), getSemestre(), getTipoMatricula(), getTrazerSomenteAlunosAtivos(), getTrazerFiliacao(), getTipoAluno(), getConfiguracaoFinanceiroPadraoSistema(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado(), getSituacaoAlunoCurso(), getTrazerAlunoTransferencia(), getPeriodicidade().getValor());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().limparParametros();
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(AlunosPorUnidadeCursoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setTituloRelatorio("Relação de Alunos Sintético");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(AlunosPorUnidadeCursoTurmaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setQuantidade(inicializarDadosQtdeAlunos(listaObjetos));
				getSuperParametroRelVO().adicionarParametro("realizarQuebraPaginaTurma", getRealizarQuebraPaginaTurma());
				adicionarParamentrosComuns();
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO());
				realizarImpressaoRelatorio();
				persistirDadosPadroes();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
				setFazerDownload(false);	
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setFazerDownload(false);
		} finally {
			design = null;
			removerObjetoMemoria(getSuperParametroRelVO());
			design = "";
			listaObjetos.clear();
		}
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
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemCurso() throws Exception {
		List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
			setListaSelectItemTurma(new ArrayList(0));
			setListaSelectItemCurso(new ArrayList(0));
			i = resultadoConsulta.iterator();
			getListaSelectItemCurso().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
				getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getNomeCursoTurno()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public void selecinarCurso() {
		try {
			UnidadeEnsinoCursoVO unidade = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setUnidadeEnsinoCursoVO(unidade);
			montarListaSelectItemTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTurma() throws Exception {
		getFacadeFactory().getUnidadeEnsinoCursoFacade().carregarDados(getUnidadeEnsinoCursoVO(), getUsuarioLogado());
		List<TurmaVO> resultadoConsulta = consultarTurmasPorUnidadeEnsinoCurso(getUnidadeEnsinoCursoVO().getCodigo());
		setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
	}

	private List<TurmaVO> consultarTurmasPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso) throws Exception {
		List<TurmaVO> lista = getFacadeFactory().getTurmaFacade().consultarPorCodigoUnidadeEnsinoCurso(codigoUnidadeEnsinoCurso, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public List getListaSelectItemSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1°"));
		lista.add(new SelectItem("2", "2°"));
		return lista;
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaPorChavePrimaria() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			if (getTurmaVO().getCodigo() == 0) {
				setTurmaVO(null);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarDadosCurso() {
		if (getTipoAluno().equals("reposicao")) {
			if (!getTurmaVO().getCodigo().equals(0)) {
				getUnidadeEnsinoCursoVO().getCurso().setCodigo(0);
				getUnidadeEnsinoCursoVO().getCurso().setNome("");
			} else {
				getUnidadeEnsinoCursoVO().setCurso(null);
			}
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			if (getTipoAluno().equals("reposicao")) {
				getUnidadeEnsinoCursoVO().getCurso().setCodigo(0);
				getUnidadeEnsinoCursoVO().getCurso().setNome("");
				getUnidadeEnsinoCursoVO().getCurso().setNivelEducacional(obj.getCurso().getNivelEducacional());
			} else {
				getUnidadeEnsinoCursoVO().setCurso(obj.getCurso());
			}
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
			setMarcarTodosTurnos(false);
			marcarTodosTurnosAction();
			if (Uteis.isAtributoPreenchido(getTurmaVO().getCurso()) && !getCursoVOs().isEmpty()) {
				getCursoVOs().stream().filter(c -> c.getCodigo().equals(getTurmaVO().getCurso().getCodigo())).forEach(c -> c.setFiltrarCursoVO(Boolean.TRUE));
				verificarTodosCursosSelecionados();
			}
			if (Uteis.isAtributoPreenchido(getTurmaVO().getTurno()) && !getTurnoVOs().isEmpty()) {
				getTurnoVOs().stream().filter(t -> t.getCodigo().equals(getTurmaVO().getTurno().getCodigo())).forEach(t -> t.setFiltrarTurnoVO(Boolean.TRUE));
				verificarTodosTurnosSelecionados();
			}
			limparDisciplina();
			montarListaSelectItemDisciplinaTurma();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setTurmaVO(null);
			setCursoApresentar("");
			setTurnoApresentar("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public List getTipoRelatorioCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("SI", "Sintético"));
		itens.add(new SelectItem("AN", "Analítico"));
		return itens;
	}

	  public void consultarCurso() {
	        try {       	
	        	List<CursoVO> resultadoConsulta = getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs("", getPeriodicidade().getValor(), null, getUnidadeEnsinoVOs(), getUsuarioLogado());
	        	for(CursoVO cursoVO: getCursoVOs()){
	        		for(CursoVO c: resultadoConsulta){
	        			if(c.getCodigo().equals(cursoVO.getCodigo())){
	        				c.setCursoSelecionado(cursoVO.getCursoSelecionado());
	        			}
	        		}        	
	        	}
	        	setCursoVOs(resultadoConsulta);
	        } catch (Exception e) {            
	            setMensagemDetalhada("msg_erro", e.getMessage());
	        }
	    }   
	    

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocurso");
			setUnidadeEnsinoCursoVO(obj);
			limparTurma();
			limparDisciplina();
		} catch (Exception e) {
		}
	}

	// public void limparCurso() throws Exception {
	// try {
	// setUnidadeEnsinoCursoVO(null);
	// } catch (Exception e) {
	// }
	// }

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void consultarDisciplina() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				if (getValorConsultaDisciplina().trim() != null || !getValorConsultaDisciplina().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaDisciplina().trim());
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				// objs =
				// getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt,
				// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
				// getUsuarioLogado());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoCursoTurma(valorInt, getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				// objs =
				// getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(),
				// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
				// getUsuarioLogado());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeCursoTurma(getValorConsultaDisciplina(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
		setDisciplinaVO(obj);
		obj = null;
		setValorConsultaDisciplina("");
		setCampoConsultaDisciplina("");
		getListaConsultaDisciplina().clear();
	}

	public List getTipoConsultaComboDisciplina() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void limparDisciplina() throws Exception {
		try {
			setDisciplinaVO(null);
		} catch (Exception e) {
		}
	}

	public List getComboBoxTipoMatricula() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("NO", "CONTRATO PÓS"));
		itens.add(new SelectItem("EX", "CONTRATO EXTENSÃO"));
		// itens.add(new SelectItem("MO", "CONTRATO ESPECIAL"));
		return itens;
	}

	public List getListaTipoRelPos() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("normal", "Normal"));
		itens.add(new SelectItem("reposicao", "Reposição/Inclusão"));
		return itens;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList(0);
		}
		return listaSelectItemCurso;
	}

	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
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

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
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

	public void setTrazerSomenteAlunosAtivos(Boolean trazerSomenteAlunosAtivos) {
		this.trazerSomenteAlunosAtivos = trazerSomenteAlunosAtivos;
	}

	public Boolean getTrazerSomenteAlunosAtivos() {
		if (trazerSomenteAlunosAtivos == null) {
			trazerSomenteAlunosAtivos = true;
		}
		return trazerSomenteAlunosAtivos;
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

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	/**
	 * @return the tipoMatricula
	 */
	public String getTipoMatricula() {
		if (tipoMatricula == null) {
			tipoMatricula = "";
		}
		return tipoMatricula;
	}

	/**
	 * @param tipoMatricula
	 *            the tipoMatricula to set
	 */
	public void setTipoMatricula(String tipoMatricula) {
		this.tipoMatricula = tipoMatricula;
	}

	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "normal";
		}
		return tipoAluno;
	}

	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}

	public String getFiltroTipoCursoAluno() {
		if (filtroTipoCursoAluno == null) {
			filtroTipoCursoAluno = "";
		}
		return filtroTipoCursoAluno;
	}

	public void setFiltroTipoCursoAluno(String filtroTipoCursoAluno) {
		this.filtroTipoCursoAluno = filtroTipoCursoAluno;
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

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public List getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
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

	public boolean getIsTipoALunoNormal() {
		return (!getTipoAluno().equals("reposicao"));
	}

	public boolean getIsSintetico() {
		return (getTipoRelatorio().equals("SI"));
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "SI";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Boolean getTrazerFiliacao() {
		if (trazerFiliacao == null) {
			trazerFiliacao = false;
		}
		return trazerFiliacao;
	}

	public void setTrazerFiliacao(Boolean trazerFiliacao) {
		this.trazerFiliacao = trazerFiliacao;
	}

	public boolean getIsApresentarAno() {
		return !getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL);
	    }

	public boolean getIsApresentarSemestre() {
		return getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL);
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

	public List<SelectItem> getListaSelectItemSituacaoAlunoCurso() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("ambos", "Ambos"));
		lista.add(new SelectItem("calouro", "Calouro"));
		lista.add(new SelectItem("veterano", "Veterano"));
		return lista;
	}

	public String getSituacaoAlunoCurso() {
		if (situacaoAlunoCurso == null) {
			situacaoAlunoCurso = "ambos";
		}
		return situacaoAlunoCurso;
	}

	public void setSituacaoAlunoCurso(String situacaoAlunoCurso) {
		this.situacaoAlunoCurso = situacaoAlunoCurso;
	}
	
	public void montarListaSelectItemDisciplinaTurma() throws Exception {
		List<DisciplinaVO> disciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaGradeEOptativaPorTurmaFazParteComposicao(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		setListaSelectItemDisciplina(UtilSelectItem.getListaSelectItem(disciplinaVOs, "codigo", "nome"));
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
	

	@PostConstruct
	private void inicializarDadosPadroes(){
		try {
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "RelatorioListagemAlunos", getUsuarioLogado());
			Map<String, String> camposPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[]{"tipoAluno", "ano", "semestre", "situacaoAlunoCurso", "tipoMatricula", "tipoRelatorio", "trazerFiliacao", "realizarQuebraPaginaTurma", "trazerAlunoTransferencia"}, "RelatorioListagemAlunos");
			for(String key: camposPadroes.keySet()){
				if(key.equals("tipoAluno")){
					setTipoAluno(camposPadroes.get(key));
				}else if(key.equals("ano")){
					setAno(camposPadroes.get(key));
				}else if(key.equals("semestre")){
					setSemestre(camposPadroes.get(key));
				}else if(key.equals("situacaoAlunoCurso")){
					setSituacaoAlunoCurso(camposPadroes.get(key));
				}else if(key.equals("tipoMatricula")){
					setTipoMatricula(camposPadroes.get(key));
				}else if(key.equals("tipoRelatorio")){
					setTipoRelatorio(camposPadroes.get(key));
				}else if(key.equals("trazerFiliacao")){
					setTrazerFiliacao(Boolean.valueOf(camposPadroes.get(key)));
				}else if(key.equals("realizarQuebraPaginaTurma")) {
					setRealizarQuebraPaginaTurma(Boolean.valueOf(camposPadroes.get(key)));					
				}else if(key.equals("trazerAlunoTransferencia")) {
					setTrazerAlunoTransferencia(Boolean.valueOf(camposPadroes.get(key)));
				}
			}
			
		} catch (Exception e) {			
		}
	}
	
	
	private void persistirDadosPadroes(){
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "RelatorioListagemAlunos", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoAluno(), "RelatorioListagemAlunos", "tipoAluno", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(), "RelatorioListagemAlunos", "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(), "RelatorioListagemAlunos", "semestre", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSituacaoAlunoCurso(), "RelatorioListagemAlunos", "situacaoAlunoCurso", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoMatricula(), "RelatorioListagemAlunos", "tipoMatricula", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTipoRelatorio(), "RelatorioListagemAlunos", "tipoRelatorio", getUsuarioLogado());						
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTrazerFiliacao().toString(), "RelatorioListagemAlunos", "trazerFiliacao", getUsuarioLogado());	
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getRealizarQuebraPaginaTurma().toString(), "RelatorioListagemAlunos", "realizarQuebraPaginaTurma", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTrazerAlunoTransferencia().toString(), "RelatorioListagemAlunos", "trazerAlunoTransferencia", getUsuarioLogado());
		} catch (Exception e) {
			
		}				
		
	}
	
	public Boolean getRealizarQuebraPaginaTurma() {
		if(realizarQuebraPaginaTurma == null) {
			realizarQuebraPaginaTurma = Boolean.FALSE;
		}
		return realizarQuebraPaginaTurma;
	}

	public void setRealizarQuebraPaginaTurma(Boolean realizarQuebraPaginaTurma) {
		this.realizarQuebraPaginaTurma = realizarQuebraPaginaTurma;
	}
	public Boolean getTrazerAlunoTransferencia() {
		if (trazerAlunoTransferencia == null) {
			trazerAlunoTransferencia = Boolean.FALSE;
		}
		return trazerAlunoTransferencia;
	}

	public void setTrazerAlunoTransferencia(Boolean trazerAlunoTransferencia) {
		this.trazerAlunoTransferencia = trazerAlunoTransferencia;
	}
	/**
	 * @return the periodicidade
	 */
	public PeriodicidadeEnum getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = PeriodicidadeEnum.SEMESTRAL;
		}
		return periodicidade;
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
		this.periodicidade = periodicidade;
	}
	
	/**
	 * @return the listaSelectItemPeriodicidade
	 */
	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>();
			for(PeriodicidadeEnum periodicidadeEnum: PeriodicidadeEnum.values()){
				listaSelectItemPeriodicidade.add(new SelectItem(periodicidadeEnum, periodicidadeEnum.getDescricao()));
			}			 
		}
		return listaSelectItemPeriodicidade;
	}

	/**
	 * @param listaSelectItemPeriodicidade the listaSelectItemPeriodicidade to set
	 */
	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
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
    
}
