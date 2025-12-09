package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.enumeradores.TipoFiltroPeriodoAcademicoEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@SuppressWarnings("unchecked")
@Controller("EtiquetaAlunoRelControle")
@Scope("viewScope")
@Lazy
public class EtiquetaAlunoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private MatriculaVO matriculaVO;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private List listaConsultaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaSelectItemUnidadeEnsino;
	private String layoutImpressao;
	private LayoutEtiquetaVO layoutEtiquetaVO;
	private Integer numeroCopias;
	private Integer coluna;
	private Integer linha;
	private String ano;
	private String semestre;
	private List listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List listaSelectItemPeriodoLetivo;
	private Integer periodoLetivo;
	private String tipoRelatorio;
	private List<SelectItem> listaSelectItemlayoutEtiqueta;
	private List<SelectItem> listaSelectItemColuna;
	private List<SelectItem> listaSelectItemLinha;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;

	public EtiquetaAlunoRelControle() {
		setAno("");
		setSemestre("");
		getFiltroRelatorioAcademicoVO().setDataInicio(null);
		getFiltroRelatorioAcademicoVO().setDataTermino(null);
		setUnidadeEnsinoVOs(new ArrayList<UnidadeEnsinoVO>(0));
		inicializarDadosListaSelectItemUnidadelEnsino();
	}

	public void realizarGeracaoEtiqueta() {
		setFazerDownload(false);

		try {
			validarDadosAnoSemestre();
			getFiltroRelatorioAcademicoVO().setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.PERIODO_DATA);
			setCaminhoRelatorio(getFacadeFactory().getEtiquetaAlunoRelFacade().realizarImpressaoEtiquetaMatricula(getLayoutEtiquetaVO(), getNumeroCopias(), getLinha(), getColuna(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getMatriculaVO().getMatricula(), getAno(), getSemestre(), getPeriodoLetivo(), getTipoRelatorio(), getCursoVO().getNivelEducacional(), getConfiguracaoGeralPadraoSistema(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado(),""));
			super.setFazerDownload(true);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosListaSelectItemUnidadelEnsino() {
		try {
			List<UnidadeEnsinoVO> listaResultado = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(listaResultado, "codigo", "nome"));
			setMensagem("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
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
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(obj);
			setTurmaVO(null);
		} catch (Exception e) {
		}
	}

	public void limparCurso() throws Exception {
		try {
			setCursoVO(new CursoVO());
			setListaConsultaCurso(new ArrayList<SelectItem>(0));
		} catch (Exception e) {
		}
	}

	List<SelectItem> tipoConsultaComboCurso;

	public List getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public List<SelectItem> getListaSelectItemlayoutEtiqueta() {
		if (listaSelectItemlayoutEtiqueta == null) {
			listaSelectItemlayoutEtiqueta = new ArrayList<SelectItem>(0);
			try {
				List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.MATRICULA, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				listaSelectItemlayoutEtiqueta.add(new SelectItem(0, ""));
				for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
					listaSelectItemlayoutEtiqueta.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));

				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaSelectItemlayoutEtiqueta;
	}

	public void consultarLayoutEtiquetaPorModulo() {
		try {
			getListaSelectItemlayoutEtiqueta().clear();
			List<LayoutEtiquetaVO> layoutEtiquetaVOs = getFacadeFactory().getLayoutEtiquetaFacade().consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum.MATRICULA, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			listaSelectItemlayoutEtiqueta.add(new SelectItem(0, ""));
			for (LayoutEtiquetaVO layoutEtiquetaVO : layoutEtiquetaVOs) {
				listaSelectItemlayoutEtiqueta.add(new SelectItem(layoutEtiquetaVO.getCodigo(), layoutEtiquetaVO.getDescricao()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void inicializarDadosLayoutEtiqueta() {
		try {
			getListaSelectItemColuna().clear();
			getListaSelectItemLinha().clear();
			if (getLayoutEtiquetaVO().getCodigo() > 0) {
				setLayoutEtiquetaVO(getFacadeFactory().getLayoutEtiquetaFacade().consultarPorChavePrimaria(getLayoutEtiquetaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
				for (int x = 1; x <= getLayoutEtiquetaVO().getNumeroLinhasEtiqueta(); x++) {
					getListaSelectItemLinha().add(new SelectItem(x, String.valueOf(x)));
				}
				for (int y = 1; y <= getLayoutEtiquetaVO().getNumeroColunasEtiqueta(); y++) {
					getListaSelectItemColuna().add(new SelectItem(y, String.valueOf(y)));
				}
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemModuloLayoutEtiqueta() throws Exception {
		return ModuloLayoutEtiquetaEnum.getModuloLayoutEtiqueta();
	}

	public void consultarTurma() {
		try {
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

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setTurmaVO(obj);
		if (getTurmaVO().getSubturma()) {
			getTurmaVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		}
		setCursoVO(getTurmaVO().getCurso());
		if(getTurmaVO().getIntegral()){
			setAno("");
			setSemestre("");
		}
		if(getTurmaVO().getAnual()){
			setSemestre("");
		}
		obj = null;
		setValorConsultaTurma(null);
		setCampoConsultaTurma(null);
		Uteis.liberarListaMemoria(getListaConsultaTurma());
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			// if (this.getUnidadeEnsinoVO().getCodigo() != 0) {
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			// } else {
			// throw new Exception("Por Favor Informe a Unidade de Ensino.");
			// }
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatriculaVO(obj);
		montarListaPeriodoLetivo();
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}

	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			montarListaPeriodoLetivo();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void limparTurma() throws Exception {
		try {
			setTurmaVO(new TurmaVO());
			setListaConsultaTurma(new ArrayList<SelectItem>(0));
		} catch (Exception e) {
		}
	}

	public void montarListaPeriodoLetivo() {
		List periodoLetivoVOs = null;
		Iterator i = null;
		Integer codigoUltimoPeriodoLetivo = null;
		try {
			periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPorMatricula(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemPeriodoLetivo().clear();
			int index = 0;
			boolean repetido = false;
			i = periodoLetivoVOs.iterator();
			codigoUltimoPeriodoLetivo = 0;
			while (i.hasNext()) {
				PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
				index = 0;
				while (index < getListaSelectItemPeriodoLetivo().size()) {
					if (((SelectItem) getListaSelectItemPeriodoLetivo().get(index)).getValue().equals(obj.getPeriodoLetivo())) {
						repetido = true;
					}
					index++;
				}
				if (!repetido) {
					getListaSelectItemPeriodoLetivo().add(new SelectItem(obj.getPeriodoLetivo(), obj.getPeriodoLetivo() + "°"));
					codigoUltimoPeriodoLetivo = obj.getPeriodoLetivo();
				}
				obj = null;
				repetido = false;
			}
			setPeriodoLetivo(codigoUltimoPeriodoLetivo);
		} catch (Exception e) {
			getListaSelectItemPeriodoLetivo().clear();
			setListaSelectItemPeriodoLetivo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			periodoLetivoVOs.clear();
			periodoLetivoVOs = null;
			i = null;
			codigoUltimoPeriodoLetivo = null;
		}
	}

	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(getMatriculaVO());
		setMatriculaVO(new MatriculaVO());
		setMensagemID("msg_entre_dados");
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List getTipoConsultaComboFiltroPor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("curso", "Curso/Turma"));
		return itens;
	}

	public List getListaSelectItemSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return tipoConsultaComboTurma;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getLayoutImpressao() {
		if (layoutImpressao == null) {
			layoutImpressao = "";
		}
		return layoutImpressao;
	}

	public void setLayoutImpressao(String layoutImpressao) {
		this.layoutImpressao = layoutImpressao;
	}

	public LayoutEtiquetaVO getLayoutEtiquetaVO() {
		if (layoutEtiquetaVO == null) {
			layoutEtiquetaVO = new LayoutEtiquetaVO();
		}
		return layoutEtiquetaVO;
	}

	public void setLayoutEtiquetaVO(LayoutEtiquetaVO layoutEtiquetaVO) {
		this.layoutEtiquetaVO = layoutEtiquetaVO;
	}

	public Integer getNumeroCopias() {
		if (numeroCopias == null) {
			numeroCopias = 1;
		}
		return numeroCopias;
	}

	public void setNumeroCopias(Integer numeroCopias) {
		this.numeroCopias = numeroCopias;
	}

	public Integer getColuna() {
		if (coluna == null) {
			coluna = 1;
		}
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}

	public Integer getLinha() {
		if (linha == null) {
			linha = 1;
		}
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
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

	public List getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList(0);
		}
		return listaSelectItemPeriodoLetivo;
	}

	public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}

	public Integer getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = 0;
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public boolean getApresentarPeriodoLetivo() {
		return !getMatriculaVO().getMatricula().equals("") && getTipoRelatorio().equals("aluno");
	}

	public boolean getApresentarDadosAluno() {
		return getTipoRelatorio().equals("aluno");
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

	public List<SelectItem> getListaSelectItemColuna() {
		if (listaSelectItemColuna == null) {
			listaSelectItemColuna = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemColuna;
	}

	public void setListaSelectItemColuna(List<SelectItem> listaSelectItemColuna) {
		this.listaSelectItemColuna = listaSelectItemColuna;
	}

	public List<SelectItem> getListaSelectItemLinha() {
		if (listaSelectItemLinha == null) {
			listaSelectItemLinha = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemLinha;
	}

	public void setListaSelectItemLinha(List<SelectItem> listaSelectItemLinha) {
		this.listaSelectItemLinha = listaSelectItemLinha;
	}

	public Boolean getIsApresentarAno() {
		return ((this.getCursoVO().getPeriodicidade().equals("AN") || this.getCursoVO().getPeriodicidade().equals("SE")) && !this.getCursoVO().getCodigo().equals(0) && !getTipoRelatorio().equals("aluno"));
	}

	public Boolean getIsApresentarSemestre() {
		return getCursoVO().getPeriodicidade().equals("SE") && !getTipoRelatorio().equals("aluno");
	}
	
	public void validarDadosAnoSemestre() throws Exception {
		if (getIsApresentarAno().equals(true)) {
			if (getAno().equals(null) || getAno().equals("")) {
				throw new Exception("O campo ANO deve ser informado.");
			}
		}
		if (getIsApresentarSemestre().equals(true)) {
			if (getSemestre().equals(null) || getSemestre().equals("")) {
				throw new Exception("O campo SEMESTRE deve ser informado.");
			}
		}
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
