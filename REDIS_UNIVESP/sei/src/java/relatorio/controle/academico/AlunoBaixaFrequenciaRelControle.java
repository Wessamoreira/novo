package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import controle.academico.VisaoCoordenadorControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AlunoBaixaFrequenciaRelVO;
import relatorio.negocio.comuns.academico.enumeradores.TipoFiltroPeriodoAcademicoEnum;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AlunoBaixaFrequenciaRel;
import relatorio.negocio.jdbc.academico.FiltroAlunoBaixaFrequenciaVO;
import relatorio.negocio.jdbc.academico.RequerimentoRel;

@Controller("AlunoBaixaFrequenciaRelControle")
@Scope("viewScope")
public class AlunoBaixaFrequenciaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -1145125765869657140L;

	private String unidadeEnsinoApresentar;

	private TurmaVO turma;
	private CursoVO curso;

	private List<CursoVO> cursoVOs;
	private TipoFiltroPeriodoAcademicoEnum tipoFiltroPeriodo;
	private List<TurmaVO> listaConsultaTurma;
	private List<CursoVO> listaConsultaCurso;

	private String cursoApresentar;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private Integer percentualBaixaFrequencia;

	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemAno;
	private List<SelectItem> tipoConsultaComboCurso;

	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private FiltroAlunoBaixaFrequenciaVO filtroAlunoBaixaFrequencia;

	public AlunoBaixaFrequenciaRelControle() {
		verificarTodasUnidadesSelecionadas();
		setPercentualBaixaFrequencia(getConfiguracaoGeralPadraoSistema().getPercentualBaixaFrequencia());
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())) {
				setUnidadeEnsinoVOs(
						getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuario(getUsuarioLogado()));
			} else {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(0, false,
						getUsuarioLogado()));
			}
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				obj.setFiltrarUnidadeEnsino(true);
			}
			verificarTodasUnidadesSelecionadas();
			if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				getFiltroAlunoBaixaFrequencia().setAno(getVisaoCoordenadorControle().getAno());
				getFiltroAlunoBaixaFrequencia().setSemestre(getVisaoCoordenadorControle().getSemestre());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void impressaoPDF() {
		try {
			List<AlunoBaixaFrequenciaRelVO> listaAlunoBaixaFrequencia = null;
			VisaoCoordenadorControle visaoCoordenador = (VisaoCoordenadorControle) context().getExternalContext()
					.getSessionMap().get("VisaoCoordenadorControle");
			if (visaoCoordenador != null) {
				getUnidadeEnsinoVOs().clear();
				getCursoVOs().clear();
				getCursoVOs().add(getCurso());
				getUsuarioLogado().getUnidadeEnsinoLogado().setFiltrarUnidadeEnsino(Boolean.TRUE);
				getUnidadeEnsinoVOs().add(getUsuarioLogado().getUnidadeEnsinoLogado());
				listaAlunoBaixaFrequencia = getFacadeFactory().getAlunoBaixaFrequenciaRelInterfaceFacade().criarObjeto(
						getUnidadeEnsinoVOs(), getCursoVOs(), getTurma(), getPercentualBaixaFrequencia(),
						getFiltroAlunoBaixaFrequencia(), getUsuarioLogado().getPessoa().getCodigo(), true);
			} else {
				listaAlunoBaixaFrequencia = getFacadeFactory().getAlunoBaixaFrequenciaRelInterfaceFacade().criarObjeto(
						getUnidadeEnsinoVOs(), getCursoVOs(), getTurma(), getPercentualBaixaFrequencia(),
						getFiltroAlunoBaixaFrequencia(), 0, false);
			}

			if (!listaAlunoBaixaFrequencia.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio("Alunos com Baixa Frequência");
				getSuperParametroRelVO().setNomeDesignIreport(AlunoBaixaFrequenciaRel.getDesignIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(AlunoBaixaFrequenciaRel.caminhoBaseRelatorio());
				getSuperParametroRelVO().setListaObjetos(listaAlunoBaixaFrequencia);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(RequerimentoRel.caminhoBaseRelatorio());

				realizarImpressaoRelatorio();
				// removerObjetoMemoria(this);
				consultarUnidadeEnsino();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(
						getValorConsultaTurma(), 0, getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurma(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			this.setCurso(obj);
			setTurma(null);
			this.setCampoConsultaCurso("");
			this.setValorConsultaCurso("");
			this.getListaConsultaCurso().clear();
		} catch (Exception e) {
		}
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

	/* Início dos Métodos da visão do coordenador */

	public void consultarCursoCoordenador() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarListaCursoPorNomeCursoCodigoPessoaCoordenador(
						this.getValorConsultaCurso(), this.getUsuarioLogado().getPessoa().getCodigo(),
						getUnidadeEnsinoLogado().getCodigo(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurmaCoordenador() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCoordenador(
						getUsuarioLogado().getPessoa().getCodigo(), this.getValorConsultaTurma(),
						getUnidadeEnsinoLogado().getCodigo(), false, false, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setTurma(new TurmaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public void limparCurso() throws Exception {
		setCursoApresentar("");
		this.setCurso(new CursoVO());
	}

	public List<SelectItem> getListaSelectItemTipoFiltroPeriodo() {
		List<SelectItem> listaSelectItem = new ArrayList<SelectItem>(0);
		listaSelectItem.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO, "Ano"));
		listaSelectItem.add(new SelectItem(TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE, "Ano/Semestre"));
		return listaSelectItem;
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public void setTipoConsultaComboCurso(List<SelectItem> tipoConsultaComboCurso) {
		this.tipoConsultaComboCurso = tipoConsultaComboCurso;
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

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
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

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public List<CursoVO> getCursoVOs() {
		if (cursoVOs == null) {
			cursoVOs = new ArrayList<CursoVO>(0);
		}
		return cursoVOs;
	}

	public void setCursoVOs(List<CursoVO> cursoVOs) {
		this.cursoVOs = cursoVOs;
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

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<CursoVO> getListaConsultaCurso() {
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public Integer getPercentualBaixaFrequencia() {
		return percentualBaixaFrequencia;
	}

	public void setPercentualBaixaFrequencia(Integer percentualBaixaFrequencia) {
		this.percentualBaixaFrequencia = percentualBaixaFrequencia;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public TipoFiltroPeriodoAcademicoEnum getTipoFiltroPeriodo() {
		if (tipoFiltroPeriodo == null) {
			tipoFiltroPeriodo = TipoFiltroPeriodoAcademicoEnum.ANO_SEMESTRE;
		}
		return tipoFiltroPeriodo;
	}

	public void setTipoFiltroPeriodo(TipoFiltroPeriodoAcademicoEnum tipoFiltroPeriodo) {
		this.tipoFiltroPeriodo = tipoFiltroPeriodo;
	}

	public FiltroAlunoBaixaFrequenciaVO getFiltroAlunoBaixaFrequencia() {
		if (filtroAlunoBaixaFrequencia == null) {
			filtroAlunoBaixaFrequencia = new FiltroAlunoBaixaFrequenciaVO();
		}
		return filtroAlunoBaixaFrequencia;
	}

	public void setFiltroAlunoBaixaFrequencia(FiltroAlunoBaixaFrequenciaVO filtroAlunoBaixaFrequencia) {
		this.filtroAlunoBaixaFrequencia = filtroAlunoBaixaFrequencia;
	}

}
