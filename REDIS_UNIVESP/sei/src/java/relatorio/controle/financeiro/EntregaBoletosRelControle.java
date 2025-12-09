package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.EntregaBoletosRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.financeiro.EntregaBoletosRel;

@Controller("EntregaBoletosRelControle")
@Scope("viewScope")
@Lazy
public class EntregaBoletosRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademico;
	private MatriculaVO matriculaVO;
	private TurmaVO turmaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<MatriculaVO> listaConsultaAluno;
	private List<TurmaVO> listaConsultaTurma;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemPeriodoLetivo;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private String campoFiltroPor;
	private String ano;
	private String semestre;
	private String tipoAluno;
	private String tipoRelatorio;
	private String tipoDocumento;
	private boolean ataEntrega;
	private boolean apresentarData = true;
	private Boolean periodoLetivoControle;
	private Integer periodoLetivo;
	private DisciplinaVO disciplinaVO;
	private List<SelectItem> listaSelectItemDisciplina;
	private Date dataAula;
	
	public EntregaBoletosRelControle() {
		incializarDados();
	}

	public void incializarDados() {
		setCampoFiltroPor("turma");
		setPeriodoLetivoControle(false);
		montarListaSelectItemUnidadeEnsino();
		try {
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), EntregaBoletosRel.getIdEntidade(), getUsuarioLogado());
		} catch (Exception e) {

		}
	}

	public void imprimir() {
		try {
			if (!getTipoDocumento().equals("boleto")) {
				setTipoRelatorio("listagem");
			}
			if (getTipoRelatorio().equals("declaracao")) {
				setAtaEntrega(true);
			}
			if (getTipoRelatorio().equals("listagem")) {
				setAtaEntrega(false);
			}
			imprimirPDF(getAtaEntrega());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDF(boolean ata) throws Exception {
		List<EntregaBoletosRelVO> entregaBoletosRelVOs;
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EntregaBoletosRelControle", "Inicializando Geração de Relatório Entrega Boletos", "Emitindo Relatório");
			boolean modeloAntigo = false;
			if (modeloAntigo) {
				EntregaBoletosRel.validarDados(getMatriculaVO(), getTurmaVO(), getIsFiltrarPorAluno(), getAno(), getSemestre());
				if (getIsFiltrarPorAluno()) {
					matriculaVOs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getMatriculaVO().getMatricula(), 0, true, getUsuarioLogado());
				} else {
					if (getTurmaVO().getTurmaAgrupada() && !getTurmaVO().getSubturma()) {
						matriculaVOs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaAnoSemestreTipoAlunoEntregaDocumento(getFiltroRelatorioAcademicoVO(), getTurmaVO().getTurmaAgrupadaVOs(), getAno(), getSemestre(), getUnidadeEnsinoVO().getCodigo(), getConfiguracaoFinanceiroPadraoSistema(), getTipoAluno(), getUsuarioLogado());
					} else {
						matriculaVOs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaCursoGradeCurricularAnoSemestreTipoAluno(getFiltroRelatorioAcademicoVO(), getTurmaVO(), getTurmaVO().getCurso().getCodigo(), getAno(), getSemestre(), 0, getUnidadeEnsinoVO().getCodigo(), getTipoAluno(), getUsuarioLogado());
					}
				}
				entregaBoletosRelVOs = getFacadeFactory().getEntregaBoletosRelFacade().criarObjeto(matriculaVOs, getIsFiltrarPorturma(), getApresentarData(), getTurmaVO(), getPeriodoLetivo(), getPeriodoLetivoControle(), getTipoDocumento(), getAno(), getSemestre());
			} else {
				entregaBoletosRelVOs = getFacadeFactory().getEntregaBoletosRelFacade().realizarGeracaoRelatorio(getTipoDocumento(), getPeriodoLetivoControle(), getIsFiltrarPorturma(), getTurmaVO(), getMatriculaVO(), getPeriodoLetivo(), getAno(), getSemestre(), getTipoAluno(), getFiltroRelatorioAcademicoVO(), getDisciplinaVO(), getCarregarFotoAluno(), getConfiguracaoGeralPadraoSistema());
			}
			if (!entregaBoletosRelVOs.isEmpty()) {
				if (ata) {
					getSuperParametroRelVO().setNomeDesignIreport(EntregaBoletosRel.getDesignIReportRelatorioAta());
				} if(getCarregarFotoAluno()){
					getSuperParametroRelVO().setNomeDesignIreport(EntregaBoletosRel.getLayoutListaPresencaComFoto());
				} else {
					getSuperParametroRelVO().setNomeDesignIreport(EntregaBoletosRel.getDesignIReportRelatorio());
				}
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio(getComboTipoRelatorioLayout_Apresentar());
				getSuperParametroRelVO().setListaObjetos(entregaBoletosRelVOs);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(EntregaBoletosRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setQuantidade(entregaBoletosRelVOs.size());
				getSuperParametroRelVO().adicionarParametro("apresentarCampoData", getApresentarData());
				getSuperParametroRelVO().adicionarParametro("apresentarCampoDataAula", getIsApresentarCampoDataAula());
				getSuperParametroRelVO().adicionarParametro("dataAula", Uteis.getDataAno4Digitos(getDataAula()));
				if (getDisciplinaVO().getCodigo().intValue() > 0) {
					setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarParametro("disciplina", getDisciplinaVO().getNome());
				} else {
					getSuperParametroRelVO().adicionarParametro("disciplina", "Todas");
				}
				getSuperParametroRelVO().adicionarParametro("apresentarCampoAssinatura", !getTipoDocumento().equals("listaPresencaSemAssinatura"));
				getSuperParametroRelVO().adicionarParametro("apresentarCampoRg", !getApresentarData());
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
				}
				if (getIsFiltrarPorturma()) {
					getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
					getSuperParametroRelVO().setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getNome());
					getSuperParametroRelVO().setCurso(getTurmaVO().getCurso().getNome());
					if (!getTurmaVO().getIntegral()) {
						getSuperParametroRelVO().setAno(getAno());
						if (getTurmaVO().getSemestral()) {
							getSuperParametroRelVO().setSemestre(getSemestre());
						}
					}
				} else {
					getSuperParametroRelVO().setUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getNome());
					getSuperParametroRelVO().setCurso(getMatriculaVO().getCurso().getNome());
					getSuperParametroRelVO().setMatricula(getMatriculaVO().getMatricula());
				}
				try {
					getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), EntregaBoletosRel.getIdEntidade(), getUsuarioLogado());
				} catch (Exception e) {

				}
				realizarImpressaoRelatorio();
				// removerObjetoMemoria(this);
				// setCampoFiltroPor("turma");
				// montarListaSelectItemUnidadeEnsino();
				// setPeriodoLetivoControle(false);
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "EntregaBoletosRelControle", "Finalizando Geração de Relatório Entrega Boletos", "Emitindo Relatório");
		} catch (Exception e) {
			throw e;
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
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
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatriculaVO(obj);
		if (obj.getCurso() != null && !obj.getCurso().getNivelEducacionalPosGraduacao()) {
			setPeriodoLetivoControle(true);
		} else {
			setPeriodoLetivoControle(false);
		}
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
			if (objAluno.getCurso() != null && !objAluno.getCurso().getNivelEducacionalPosGraduacao()) {
				setPeriodoLetivoControle(true);
			} else {
				setPeriodoLetivoControle(false);
			}
			montarListaPeriodoLetivo();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void montarListaPeriodoLetivo() {
		List<PeriodoLetivoVO> periodoLetivoVOs = null;
		Iterator<PeriodoLetivoVO> i = null;
		Integer codigoUltimoPeriodoLetivo = null;
		try {
			periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemPeriodoLetivo().clear();
			int index = 0;
			boolean repetido = false;
			i = periodoLetivoVOs.iterator();
			codigoUltimoPeriodoLetivo = 0;
			while (i.hasNext()) {
				PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
				index = 0;
				while (index < getListaSelectItemPeriodoLetivo().size()) {
					if (getListaSelectItemPeriodoLetivo().get(index).getValue().equals(obj.getPeriodoLetivo())) {
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
			setListaSelectItemPeriodoLetivo(new ArrayList<SelectItem>(0));
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

	public List<SelectItem> tipoConsultaComboAluno;

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAluno;
	}

	public List<SelectItem> tipoConsultaComboFiltroPor;

	public List<SelectItem> getTipoConsultaComboFiltroPor() {
		if (tipoConsultaComboFiltroPor == null) {
			tipoConsultaComboFiltroPor = new ArrayList<SelectItem>(0);
			tipoConsultaComboFiltroPor.add(new SelectItem("aluno", "Aluno"));
			tipoConsultaComboFiltroPor.add(new SelectItem("turma", "Turma"));
		}
		return tipoConsultaComboFiltroPor;
	}

	@SuppressWarnings("unchecked")
	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
				if (getCampoConsultaTurma().equals("identificadorTurma")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaTurma().equals("nomeCurso")) {
					objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				setListaConsultaTurma(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		getFacadeFactory().getTurmaFacade().carregarDados(obj, NivelMontarDados.TODOS, getUsuarioLogado());
		setTurmaVO(obj);
		setPeriodoLetivo(0);
		setAno("");
		setSemestre("");
		valorConsultaTurma = "";
		campoConsultaTurma = "";
		listaConsultaTurma.clear();
		setListaSelectItemDisciplina(null);
		montarListaDisciplina();
	}

	public List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public List<SelectItem> tipoConsultaComboSemestre;

	public List<SelectItem> getTipoConsultaComboSemestre() {
		if (tipoConsultaComboSemestre == null) {
			tipoConsultaComboSemestre = new ArrayList<SelectItem>(0);
			tipoConsultaComboSemestre.add(new SelectItem("", ""));
			tipoConsultaComboSemestre.add(new SelectItem("1", "1º"));
			tipoConsultaComboSemestre.add(new SelectItem("2", "2º"));
		}
		return tipoConsultaComboSemestre;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			limparDadosAluno();
			limparIdentificador();
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
			setMensagemID("");
		} catch (Exception e) {
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			} else {
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	public List<SelectItem> comboTipoRelatorio;

	public List<SelectItem> getComboTipoRelatorio() {
		if (comboTipoRelatorio == null) {
			comboTipoRelatorio = new ArrayList<SelectItem>(0);
			comboTipoRelatorio.add(new SelectItem("declaracao", "Declaração"));
			comboTipoRelatorio.add(new SelectItem("listagem", "Listagem"));
		}
		return comboTipoRelatorio;
	}

	public boolean getApresentarTipoRelatorio() {
		return getTipoDocumento().equals("boleto");
	}

	public List<SelectItem> comboTipoRelatorioLayout;

	public List<SelectItem> getComboTipoRelatorioLayout() {
		if (comboTipoRelatorioLayout == null) {
			comboTipoRelatorioLayout = new ArrayList<SelectItem>(0);
			comboTipoRelatorioLayout.add(new SelectItem("avaliacao", "Avaliação"));
			comboTipoRelatorioLayout.add(new SelectItem("carterinha", "Carterinha Estudantil"));
			comboTipoRelatorioLayout.add(new SelectItem("bertificado", "Certificado"));
			comboTipoRelatorioLayout.add(new SelectItem("boleto", "Entrega de Boleto"));
			comboTipoRelatorioLayout.add(new SelectItem("entregaContrato", "Entrega de Contrato"));
			comboTipoRelatorioLayout.add(new SelectItem("livroRegistro", "Livro de Registro"));
			comboTipoRelatorioLayout.add(new SelectItem("didatico", "Material Didático"));
			comboTipoRelatorioLayout.add(new SelectItem("curso", "Material do Curso"));
			comboTipoRelatorioLayout.add(new SelectItem("listaPresenca", "Lista de Presença"));
			comboTipoRelatorioLayout.add(new SelectItem("listaPresencaComFoto", "Lista de Presença c/ Foto"));
			comboTipoRelatorioLayout.add(new SelectItem("listaPresencaSemAssinatura", "Lista de Presença s/ Assinatura"));
		}
		return comboTipoRelatorioLayout;
	}

	public String getComboTipoRelatorioLayout_Apresentar() {
		if (getTipoDocumento().equals("carterinha")) {
			return "Carterinha Estudantil";
		} else if (getTipoDocumento().equals("bertificado")) {
			return "Certificado";
		} else if (getTipoDocumento().equals("didatico")) {
			return "Material Didático";
		} else if (getTipoDocumento().equals("curso")) {
			return "Material do Curso";
		} else if (getTipoDocumento().equals("avaliacao")) {
			return "Avaliação";
		} else if (getTipoDocumento().equals("entregaContrato")) {
			return "Entrega de Contrato";
		} else if (getTipoDocumento().equals("listaPresenca") || getTipoDocumento().equals("listaPresencaComFoto")) {
			return "Lista de Presença";
		} else if (getTipoDocumento().equals("listaPresencaSemAssinatura")) {
			return "Relação de Alunos";
		} else if (getTipoDocumento().equals("livroRegistro")) {
			return "Livro de Registro";
		} else {
			return "Entrega de Boleto";
		}
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "listagem";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public boolean getIsTipoRelatorioDeclaracao() {
		return getTipoRelatorio().equals("declaracao");
	}

	public boolean getIsTipoRelatorioListagem() {
		return getTipoRelatorio().equals("listagem");
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean isApresentarCampos() {
		return ((getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0 && getIsFiltrarPorAluno()) || (getTurmaVO() != null && getTurmaVO().getCodigo() != 0 && getIsFiltrarPorturma()));
	}

	public void limparIdentificador() {
		setMatriculaVO(new MatriculaVO());
		setTurmaVO(new TurmaVO());
		setAno("");
		setSemestre("");
		setListaSelectItemDisciplina(null);
		setListaConsultaTurma(new ArrayList<TurmaVO>(0));
	}

	public boolean isApresentarCamposAluno() {
		return (getMatriculaVO().getAluno() != null && getMatriculaVO().getAluno().getCodigo() != 0 && getIsFiltrarPorAluno());
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

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
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

	public String getCampoFiltroPor() {
		if (campoFiltroPor == null) {
			campoFiltroPor = "";
		}
		return campoFiltroPor;
	}

	public void setCampoFiltroPor(String campoFiltroPor) {
		this.campoFiltroPor = campoFiltroPor;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<SelectItem> listaSelectItemSemestre;

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("", ""));
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
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

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
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

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
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

	public boolean getAtaEntrega() {
		return ataEntrega;
	}

	public void setAtaEntrega(boolean ataEntrega) {
		this.ataEntrega = ataEntrega;
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodoLetivo;
	}

	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
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

	public Boolean getIsFiltrarPorturma() {
		return getCampoFiltroPor().equals("turma");
	}

	public Boolean getIsFiltrarPorAluno() {
		return getCampoFiltroPor().equals("aluno");
	}

	public Boolean getIsFiltrarPorAno() {
		return getIsFiltrarPorturma() && Uteis.isAtributoPreenchido(getTurmaVO()) && !getTurmaVO().getIntegral();
	}

	public Boolean getIsFiltrarPorSemestre() {
		return getIsFiltrarPorturma() && Uteis.isAtributoPreenchido(getTurmaVO()) && getTurmaVO().getSemestral();
	}

	public Boolean getPeriodoLetivoControle() {
		if (periodoLetivoControle == null) {
			periodoLetivoControle = false;
		}
		return periodoLetivoControle;
	}

	public void setPeriodoLetivoControle(Boolean periodoLetivoControle) {
		this.periodoLetivoControle = periodoLetivoControle;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		if (tipoDocumento == null) {
			tipoDocumento = "";
		}
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the apresentarData
	 */
	public boolean getApresentarData() {
		return apresentarData;
	}

	/**
	 * @param apresentarData
	 *            the apresentarData to set
	 */
	public void setApresentarData(boolean apresentarData) {
		this.apresentarData = apresentarData;
	}

	public List<SelectItem> listaAlunoRel;

	public List<SelectItem> getListaAlunoRel() {
		if (listaAlunoRel == null) {
			listaAlunoRel = new ArrayList<SelectItem>(0);
			listaAlunoRel.add(new SelectItem("TODOS", "Todos"));
			listaAlunoRel.add(new SelectItem("NORMAL", "Normal"));
			listaAlunoRel.add(new SelectItem("REPOSICAO", "Reposição/Inclusão"));
		}
		return listaAlunoRel;
	}

	public String getTipoAluno() {
		if (tipoAluno == null) {
			tipoAluno = "TODOS";
		}
		return tipoAluno;
	}

	public void setTipoAluno(String tipoAluno) {
		this.tipoAluno = tipoAluno;
	}
	
	public boolean getIsApresentarCampoData(){
		if((getTipoDocumento().equals("listaPresencaSemAssinatura") || getTipoDocumento().equals("listaPresencaComFoto")) && getCampoFiltroPor().equals("aluno")){			
			return false;
		}else{			
			return true;
		}
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

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}
   
	public static String getIdEntidade() {
		return ("EntregaBoletosRel");
	}
	
	public void montarListaDisciplina() throws Exception {
		List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurmaVO().getCodigo(), false, true, 0);
		getListaSelectItemDisciplina().add(new SelectItem(0, ""));
		for (HorarioTurmaDisciplinaProgramadaVO obj : horarioTurmaDisciplinaProgramadaVOs) {
			getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigoDisciplina(), obj.getNomeDisciplina()));
		}
	}

	public Boolean getIsApresentarCampoDataAula() {
		if (getTipoDocumento().equals("avaliacao") || getTipoDocumento().equals("listaPresenca") || getTipoDocumento().equals("listaPresencaSemAssinatura") || getTipoDocumento().equals("listaPresencaComFoto")) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public Date getDataAula() {
		if (dataAula == null) {
			dataAula = new Date();
		}
		return dataAula;
	}

	public void setDataAula(Date dataAula) {
		this.dataAula = dataAula;
	}
	
	public Boolean getCarregarFotoAluno(){
		return  getTipoDocumento().equals("listaPresencaComFoto");
	}
}
