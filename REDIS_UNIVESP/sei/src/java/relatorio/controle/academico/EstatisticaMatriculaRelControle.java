package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.EstatisticaMatriculaRelVO;
import relatorio.negocio.comuns.academico.enumeradores.TipoFiltroPeriodoAcademicoEnum;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.EstatisticaMatriculaRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Controller("EstatisticaMatriculaRelControle")
@Scope("viewScope")
public class EstatisticaMatriculaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -2699862212261963961L;
	private Date dataInicio;
	private Date dataFim;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademico;
	private List<SelectItem> listaSelectItemAno;
	private Boolean exibirMatriculaCalouro;
	private Boolean exibirMatriculaVeterano;
	private Boolean exibirPreMatriculaCalouro;
	private Boolean exibirPreMatriculaVeterano;
	private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	private String turnoApresentar;

	public void imprimirExcel() {
		getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
		imprimir();
	}

	public void imprimirPDF() {
		getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
		imprimir();
	}

	public void imprimir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EstatisticaMatriculaRelControle", "Inicializando Geração de Relatório Estatística Matrícula", "Emitindo Relatório");
			List<EstatisticaMatriculaRelVO> estatisticaMatriculaRelVOs = getFacadeFactory().getEstatisticaMatriculaRelFacade().criarObjeto(getFiltroRelatorioAcademico(), getCursoVOs(), getUnidadeEnsinoVOs(), getTurnoVOs(), exibirMatriculaCalouro, exibirMatriculaVeterano, exibirPreMatriculaCalouro, exibirPreMatriculaVeterano);
			if (!estatisticaMatriculaRelVOs.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(EstatisticaMatriculaRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setSubReport_Dir(EstatisticaMatriculaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Estatística de Matrículas");
				getSuperParametroRelVO().setListaObjetos(estatisticaMatriculaRelVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(EstatisticaMatriculaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(estatisticaMatriculaRelVOs.size());
				realizarImpressaoRelatorio();
				getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademico(), "estatisticaMatricula", getUsuarioLogado());
				removerObjetoMemoria(this);
				consultarUnidadeEnsino();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "EstatisticaMatriculaRelControle", "Finalizando Geração de Relatório Estatística Matrícula", "Emitindo Relatório");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<SelectItem> getListaSelectItemTipoFiltroPeriodoAcademico() {
		return TipoFiltroPeriodoAcademicoEnum.getListaSelectItem();
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

	public Boolean getExibirMatriculaVeterano() {
		if (exibirMatriculaVeterano == null) {
			exibirMatriculaVeterano = Boolean.FALSE;
		}
		return exibirMatriculaVeterano;
	}

	public void setExibirMatriculaVeterano(Boolean exibirMatriculaVeterano) {
		this.exibirMatriculaVeterano = exibirMatriculaVeterano;
	}

	public Boolean getExibirPreMatriculaCalouro() {
		if (exibirPreMatriculaCalouro == null) {
			exibirPreMatriculaCalouro = Boolean.FALSE;
		}
		return exibirPreMatriculaCalouro;
	}

	public void setExibirPreMatriculaCalouro(Boolean exibirPreMatriculaCalouro) {
		this.exibirPreMatriculaCalouro = exibirPreMatriculaCalouro;
	}

	public Boolean getExibirPreMatriculaVeterano() {
		if (exibirPreMatriculaVeterano == null) {
			exibirPreMatriculaVeterano = Boolean.FALSE;
		}
		return exibirPreMatriculaVeterano;
	}

	public void setExibirPreMatriculaVeterano(Boolean exibirPreMatriculaVeterano) {
		this.exibirPreMatriculaVeterano = exibirPreMatriculaVeterano;
	}

	public Boolean getExibirMatriculaCalouro() {
		if (exibirMatriculaCalouro == null) {
			exibirMatriculaCalouro = Boolean.TRUE;
		}
		return exibirMatriculaCalouro;
	}

	public void setExibirMatriculaCalouro(Boolean exibirMatriculaCalouro) {
		this.exibirMatriculaCalouro = exibirMatriculaCalouro;
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademico(), "estatisticaMatricula", getUsuarioLogado());
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

}
