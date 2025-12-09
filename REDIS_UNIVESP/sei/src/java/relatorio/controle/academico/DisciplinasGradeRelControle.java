package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DisciplinasGradeRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DisciplinasGradeRel;

@SuppressWarnings("unchecked")
@Controller("DisciplinasGradeRelControle")
@Scope("viewScope")
@Lazy
public class DisciplinasGradeRelControle extends SuperControleRelatorio {

	private List listaConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private List listaSelectItemGradeCurricular;
	private Integer codigoGradeCurricular;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List listaConsultaTurma;
	private String tipoRelatorio;
	private String tipoLayout;
	private List<SelectItem> listaSelectItemTipoLayout;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Boolean apresentarDisciplinaComposta;
	private Boolean apresentarListaAtividadeComplementar;
	private Boolean apresentarCreditoFinanceiro;

	public DisciplinasGradeRelControle() throws Exception {
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void imprimirPDF() throws Exception {
		String titulo = "Matriz Curricular";
		String design = DisciplinasGradeRel.getDesignIReportRelatorio(getTipoLayout());
		List listaRegistro = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DisciplinasGradeRelControle", "Inicializando Geração de Relatório Disciplinas Da Grade", "Emitindo Relatório");
			getFacadeFactory().getDisciplinaGradeRelFacade().validarDados(getTipoRelatorio(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getCodigoGradeCurricular());
			if (getTipoLayout().equals("DisciplinasGradeRel") || getTipoLayout().equals("DisciplinasGrade3Rel")) {
				listaRegistro = getFacadeFactory().getDisciplinaGradeRelFacade().criarObjeto(getCodigoGradeCurricular(), getApresentarDisciplinaComposta(), getTipoLayout());
			} else {
				setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeEnsino(getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				Map<String, Object> mapResultado = getFacadeFactory().getDisciplinaGradeRelFacade().criarObjetoLayout2(getCodigoGradeCurricular(), getUnidadeEnsinoCursoVO().getCodigo(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getApresentarDisciplinaComposta(), getUsuarioLogado());
				listaRegistro = (List<DisciplinasGradeRelVO>)mapResultado.get("LISTA");
				design = DisciplinasGradeRel.getDesignIReportRelatorio((String)mapResultado.get("LAYOUT"));
			}
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			
			getSuperParametroRelVO().adicionarParametro("endereco", unidadeEnsinoVO.getEndereco());
			getSuperParametroRelVO().adicionarParametro("numero", unidadeEnsinoVO.getNumero());
			getSuperParametroRelVO().adicionarParametro("bairro", unidadeEnsinoVO.getSetor());
			getSuperParametroRelVO().adicionarParametro("cidade", unidadeEnsinoVO.getCidade().getNome());
			getSuperParametroRelVO().adicionarParametro("estado", unidadeEnsinoVO.getCidade().getEstado().getSigla());
			getSuperParametroRelVO().adicionarParametro("cep", unidadeEnsinoVO.getCEP());
			getSuperParametroRelVO().adicionarParametro("fone", unidadeEnsinoVO.getTelComercial1());
			getSuperParametroRelVO().adicionarParametro("site", unidadeEnsinoVO.getSite());
			getSuperParametroRelVO().adicionarParametro("email", unidadeEnsinoVO.getEmail());
			getSuperParametroRelVO().adicionarParametro("nomeExpedicaoDiploma", unidadeEnsinoVO.getNomeExpedicaoDiploma());
			getSuperParametroRelVO().adicionarParametro("inscEstadual", unidadeEnsinoVO.getInscEstadual());
			getSuperParametroRelVO().adicionarParametro("inscMunicipal", unidadeEnsinoVO.getInscMunicipal());
			getSuperParametroRelVO().adicionarParametro("caixaPostal", unidadeEnsinoVO.getCaixaPostal());
			getSuperParametroRelVO().adicionarParametro("cnpj", unidadeEnsinoVO.getCNPJ());
			getSuperParametroRelVO().adicionarParametro("credenciamento", unidadeEnsinoVO.getCredenciamento());
			getSuperParametroRelVO().adicionarParametro("apresentarListaAtividadeComplementar", getApresentarListaAtividadeComplementar());
			getSuperParametroRelVO().adicionarParametro("apresentarCreditoFinanceiro", getApresentarCreditoFinanceiro());
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(DisciplinasGradeRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setListaObjetos(listaRegistro);
			getSuperParametroRelVO().setUnidadeEnsino(unidadeEnsinoVO.getNome());
			getSuperParametroRelVO().setNomeEmpresa(unidadeEnsinoVO.getNome());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(DisciplinasGradeRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setQuantidade(listaRegistro.size());
			if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
				setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
			}
			realizarImpressaoRelatorio();
			persistirLayoutPadrao(getTipoLayout());
			removerObjetoMemoria(this);
			registrarAtividadeUsuario(getUsuarioLogado(), "DisciplinasGradeRelControle", "Finalizando Geração de Relatório Disciplinas Da Grade", "Emitindo Relatório");
			setMensagemID("msg_relatorio_ok");
			montarListaSelectItemUnidadeEnsino();
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			titulo = null;
			design = null;
			Uteis.liberarListaMemoria(listaRegistro);
		}
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getUnidadeEnsinoVO().getCodigo().equals(0)) {
				throw new Exception("O Campo Unidade Ensino (DISCIPLINA MATRIZ CURRICULAR) deve ser Informado.");
			}
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(new Integer(valorInt), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			if (getUnidadeEnsinoVO().getCodigo().equals(0)) {
				throw new Exception("O Campo Unidade Ensino (DISCIPLINA MATRIZ CURRICULAR) deve ser Informado.");
			}
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getCursoVO().getCodigo(), 0, false, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDados() throws Exception {
		limparTurma();
		limparDadosCurso();
	}

	public void limparTurma() throws Exception {
		try {
			setTurmaVO(new TurmaVO());
			setListaSelectItemGradeCurricular(new ArrayList(0));
		} catch (Exception e) {
		}
	}

	public void selecionarCurso() throws Exception {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
		setCursoVO(obj);
		setCampoConsultaCurso("");
		setValorConsultaCurso("");
		setListaConsultaCurso(new ArrayList(0));
		montarListaSelectItemGradeCurricular();
	}

	public void limparDadosCurso() {
		setCursoVO(new CursoVO());
		setListaSelectItemGradeCurricular(new ArrayList(0));
	}

	private void montarListaSelectItemGradeCurricular() throws Exception {
		List<GradeCurricularVO> gradeCurricularVOs = new ArrayList<GradeCurricularVO>(0);
		if (getTipoRelatorio().equals("curso")) {
			gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurriculars(getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} else {
			gradeCurricularVOs.add(getFacadeFactory().getGradeCurricularFacade().consultarPorTurmaNivelComboBox(getTurmaVO().getCodigo(), getUsuarioLogado()));
		}
//		setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(gradeCurricularVOs, "codigo", "nome"));
		Collections.sort(gradeCurricularVOs, new Comparator<GradeCurricularVO>(){
		     public int compare(GradeCurricularVO o1, GradeCurricularVO o2){
		         if(o1.getSituacao() == o2.getSituacao()) {
		             return 0;
		         }
		         return o1.getSituacao().compareTo(o2.getSituacao());
		     }
		});
		getListaSelectItemGradeCurricular().clear();
		getListaSelectItemGradeCurricular().add(new SelectItem(0, ""));
		for (GradeCurricularVO item : gradeCurricularVOs) {
			getListaSelectItemGradeCurricular().add(new SelectItem(item.getCodigo(), item.getNome() + " - " + item.getSituacao_Apresentar()));
		}
		montarListaSelectItemTipoLayout();
		verificarLayoutPadrao();
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nome", "Nome Curso"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboTipoRelatorio() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		if (getTurmaVO().getSubturma()) {
			setCursoVO(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		} else {
			setCursoVO(getTurmaVO().getCurso());
		}
		montarListaSelectItemGradeCurricular();
		setValorConsultaTurma(null);
		setCampoConsultaTurma(null);
		Uteis.liberarListaMemoria(getListaConsultaTurma());
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
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

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
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

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public List getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public void setCodigoGradeCurricular(Integer codigoGradeCurricular) {
		this.codigoGradeCurricular = codigoGradeCurricular;
	}

	public Integer getCodigoGradeCurricular() {
		if (codigoGradeCurricular == null) {
			codigoGradeCurricular = 0;
		}
		return codigoGradeCurricular;
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

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "curso";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public boolean getApresentarDadosTurma() {
		return getTipoRelatorio().equals("turma");
	}

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public List<SelectItem> getListaSelectItemTipoLayout() {
		if (listaSelectItemTipoLayout == null) {
			listaSelectItemTipoLayout = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoLayout;
	}

	public void setListaSelectItemTipoLayout(List<SelectItem> listaSelectItemTipoLayout) {
		this.listaSelectItemTipoLayout = listaSelectItemTipoLayout;
	}

	public void montarListaSelectItemTipoLayout() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("DisciplinasGradeRel", "Layout 1"));
		objs.add(new SelectItem("DisciplinasGrade2Rel", "Layout 2"));
		objs.add(new SelectItem("DisciplinasGrade3Rel", "Layout 3"));
		setListaSelectItemTipoLayout(objs);
	}

	private void persistirLayoutPadrao(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "DisciplinasGradeRel", "designDisciplinasGrade", getUsuarioLogado());
	}

	private void verificarLayoutPadrao() throws Exception {
		LayoutPadraoVO layoutPadraoVO = new LayoutPadraoVO();
		layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("DisciplinasGradeRel", "designDisciplinasGrade", false, getUsuarioLogado());
		if (!layoutPadraoVO.getValor().equals("")) {
			setTipoLayout(layoutPadraoVO.getValor());
		}
		removerObjetoMemoria(layoutPadraoVO);
	}

	@PostConstruct
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

	public Boolean getApresentarDisciplinaComposta() {
		if (apresentarDisciplinaComposta == null) {
			apresentarDisciplinaComposta = false;
		}
		return apresentarDisciplinaComposta;
	}

	public void setApresentarDisciplinaComposta(Boolean apresentarDisciplinaComposta) {
		this.apresentarDisciplinaComposta = apresentarDisciplinaComposta;
	}

	public Boolean getApresentarListaAtividadeComplementar() {
		if (apresentarListaAtividadeComplementar == null) {
			apresentarListaAtividadeComplementar = false;
		}
		return apresentarListaAtividadeComplementar;
	}

	public void setApresentarListaAtividadeComplementar(Boolean apresentarListaAtividadeComplementar) {
		this.apresentarListaAtividadeComplementar = apresentarListaAtividadeComplementar;
	}

	public Boolean getApresentarCreditoFinanceiro() {
		if (apresentarCreditoFinanceiro == null) {
			apresentarCreditoFinanceiro = false;
		}
		return apresentarCreditoFinanceiro;
	}

	public void setApresentarCreditoFinanceiro(Boolean apresentarCreditoFinanceiro) {
		this.apresentarCreditoFinanceiro = apresentarCreditoFinanceiro;
	}
	
	

}
