package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoGradeAnteriorAlteradaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("HistoricoGradeAnteriorAlteradaControle")
@Scope("viewScope")
@Lazy
public class HistoricoGradeAnteriorAlteradaControle extends SuperControle implements Serializable {

	private MatriculaVO matriculaVO;
	private GradeCurricularVO gradeCurricularVO;
	private HistoricoGradeAnteriorAlteradaVO historicoGradeAnteriorAlteradaVO;
	private List<HistoricoGradeAnteriorAlteradaVO> historicoGradeAnteriorAlteradaVOs;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private String valorConsultaCidade;
	private String campoConsultaCidade;
	private List<CidadeVO> listaConsultaCidade;
	private String campoConsultarDisciplina;
	private String valorConsultarDisciplina;
	private List<DisciplinaVO> listaConsultarDisciplina;
	private HistoricoGradeAnteriorAlteradaVO historicoGradeAnteriorAlteradaTempVO;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private List<SelectItem> listaSelectItemMatriculaPeriodo;
	private static final long serialVersionUID = 1L;

	public HistoricoGradeAnteriorAlteradaControle() {
		super();
	}

	public void consultarHistoricoGrade() {
		try {
			setHistoricoGradeAnteriorAlteradaVOs(getFacadeFactory().getHistoricoGradeAnteriorAlteradaFacade().consultarHistoricoGradePorMatriculaGradeCurricular(getMatriculaVO().getMatricula(), getGradeCurricularVO().getCodigo(), getUsuarioLogado()));
			montarListaSelectItemMatriculaPeriodo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void novo() {
		setMatriculaVO(new MatriculaVO());
		setGradeCurricularVO(new GradeCurricularVO());
		setHistoricoGradeAnteriorAlteradaVO(new HistoricoGradeAnteriorAlteradaVO());
		getHistoricoGradeAnteriorAlteradaVOs().clear();
		setMensagemID("msg_entre_dados");
	}

	public void persistir() {
		try {
			getFacadeFactory().getHistoricoGradeAnteriorAlteradaFacade().persistir(getHistoricoGradeAnteriorAlteradaVOs(), getMatriculaVO(), getGradeCurricularVO(), getUsuarioLogado());
			consultarHistoricoGrade();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			montarListaSelectItemGradeCurricular();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatriculaVO(new MatriculaVO());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado()));
		montarListaSelectItemGradeCurricular();
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}

	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(getMatriculaVO());
		getHistoricoGradeAnteriorAlteradaVOs().clear();
	}

	public void validarNota() {
		try {
			HistoricoGradeAnteriorAlteradaVO obj = (HistoricoGradeAnteriorAlteradaVO) context().getExternalContext().getRequestMap().get("historicoGradeItens");
			if (!obj.getIsentarMediaFinal()) {
				if (obj.getMediaFinal() > 10) {
					obj.setMediaFinal(0.0);
					throw new Exception("A nota deve ser maior que 0 e menor ou igual a 10.");
				} else {
					setMensagemID("");
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarNotaInclusao() {
		try {
			if (!getHistoricoGradeAnteriorAlteradaVO().getIsentarMediaFinal()) {
				if (getHistoricoGradeAnteriorAlteradaVO().getMediaFinal() > 10) {
					getHistoricoGradeAnteriorAlteradaVO().setMediaFinal(0.0);
					throw new Exception("A nota deve ser maior que 0 e menor ou igual a 10.");
				} else {
					setMensagemID("");
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemGradeCurricular() throws Exception {
		try {
			List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularAnteriorPorMatricula(getMatriculaVO().getMatricula(), getUsuarioLogado());
			setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(gradeCurricularVOs, "codigo", "nome", false));
		} catch (Exception e) {
			throw e;
		}
	}

	public void montarListaSelectItemMatriculaPeriodo() throws Exception {
		List<MatriculaPeriodoVO> resultadoConsulta = null;
		Iterator<MatriculaPeriodoVO> i = null;
		try {
			resultadoConsulta = consultarAnoSemstrePeriodoLetivoMatriculaPeriodoPorMatriucla();
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			while (i.hasNext()) {
				MatriculaPeriodoVO obj = (MatriculaPeriodoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getAno() + "/" + obj.getSemestre() + " - " + obj.getPeridoLetivo().getPeriodoLetivo() + "º Período"));
			}
			setListaSelectItemMatriculaPeriodo(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List<MatriculaPeriodoVO> consultarAnoSemstrePeriodoLetivoMatriculaPeriodoPorMatriucla() throws Exception{
		return getFacadeFactory().getMatriculaPeriodoFacade().consultarAnoSemstrePeriodoLetivoMatriculaPeriodoPorMatriucla(getMatriculaVO().getMatricula(), null, getUsuarioLogado());
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSemestre() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", "1º Semestre"));
		itens.add(new SelectItem("2", "2º Semestre"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemSituacaoHistorico() throws Exception {
		List<SelectItem> listaSelectItemSituacaoHistorico = new ArrayList<SelectItem>(0);
		// listaSelectItemSituacaoHistorico.add(new
		// SelectItem(SituacaoHistorico.getSituacaoHistoricoEnum()));
		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.APROVADO.getValor(), SituacaoHistorico.APROVADO.getDescricao()));
		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), SituacaoHistorico.APROVADO_APROVEITAMENTO.getDescricao()));
		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.REPROVADO.getValor(), SituacaoHistorico.REPROVADO.getDescricao()));
		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.REPROVADO_FALTA.getValor(), SituacaoHistorico.REPROVADO_FALTA.getDescricao()));
		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.CONCESSAO_CREDITO.getValor(), SituacaoHistorico.CONCESSAO_CREDITO.getDescricao()));
		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getValor(), SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getDescricao()));
		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.TRANCAMENTO.getValor(), SituacaoHistorico.TRANCAMENTO.getDescricao()));
		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.ABANDONO_CURSO.getValor(), SituacaoHistorico.ABANDONO_CURSO.getDescricao()));
		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.CANCELADO.getValor(), SituacaoHistorico.CANCELADO.getDescricao()));
		return listaSelectItemSituacaoHistorico;
	}

	public void alterarAcaoAlteracao() {
		HistoricoGradeAnteriorAlteradaVO obj = (HistoricoGradeAnteriorAlteradaVO) context().getExternalContext().getRequestMap().get("historicoGradeItens");
		if (obj.getAcao().equals("ALTERACAO")) {
			obj.setCssAcao("");
		} else if (obj.getAcao().equals("EXCLUSAO")) {
			obj.setCssAcao("background-color: #FF6464;");
		} else {
			obj.setCssAcao("");
		}
	}

	public void adicionarHistoricoGradeAnterior() {
		try {
			getFacadeFactory().getHistoricoGradeAnteriorAlteradaFacade().adicionarHistoricoGradeAnterior(getHistoricoGradeAnteriorAlteradaVOs(), getHistoricoGradeAnteriorAlteradaVO(), getUsuarioLogado());
			setHistoricoGradeAnteriorAlteradaVO(new HistoricoGradeAnteriorAlteradaVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosInclusaoHistoricoGrade() {
		try {
			setHistoricoGradeAnteriorAlteradaVO(new HistoricoGradeAnteriorAlteradaVO());
			montarListaSelectItemMatriculaPeriodo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerHistoricoGradeAnterior() {
		try {
			HistoricoGradeAnteriorAlteradaVO obj = (HistoricoGradeAnteriorAlteradaVO) context().getExternalContext().getRequestMap().get("historicoGradeItens");
			getFacadeFactory().getHistoricoGradeAnteriorAlteradaFacade().removerHistoricoGradeAnterior(getHistoricoGradeAnteriorAlteradaVOs(), obj);
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosInclusaHistoricoGrade() {
		setHistoricoGradeAnteriorAlteradaVO(new HistoricoGradeAnteriorAlteradaVO());
	}

	public void consultarCidade() {
		try {
			if (getCampoConsultaCidade().equals("nome")) {
				if (getValorConsultaCidade().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				setListaConsultaCidade(getFacadeFactory().getCidadeFacade().consultaRapidaPorNome(getValorConsultaCidade(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList<CidadeVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCidadeAproveitamento() {
		setHistoricoGradeAnteriorAlteradaTempVO((HistoricoGradeAnteriorAlteradaVO) context().getExternalContext().getRequestMap().get("historicoGradeItens"));
	}

	public void limparDadosCidade() {
		HistoricoGradeAnteriorAlteradaVO obj = (HistoricoGradeAnteriorAlteradaVO) context().getExternalContext().getRequestMap().get("historicoGradeItens");
		obj.setCidadeVO(new CidadeVO());
	}

	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
		getHistoricoGradeAnteriorAlteradaTempVO().setCidadeVO(obj);
		getHistoricoGradeAnteriorAlteradaVO().setCidadeVO(obj);
		getListaConsultaCidade().clear();
		setValorConsultaCidade("");
		setCampoConsultaCidade("");
		setHistoricoGradeAnteriorAlteradaTempVO(new HistoricoGradeAnteriorAlteradaVO());
	}

	public void limparDadosCidadeInclusao() {
		getHistoricoGradeAnteriorAlteradaVO().setCidadeVO(new CidadeVO());
	}

	public List<SelectItem> getTipoConsultaCidade() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	@SuppressWarnings("unchecked")
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultarDisciplina().equals("codigo")) {
				if (getValorConsultarDisciplina().equals("")) {
					setValorConsultarDisciplina("0");
				}
				if (getValorConsultarDisciplina().trim() != null || !getValorConsultarDisciplina().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultarDisciplina().trim());
				}
				Integer valorInt = Integer.parseInt(getValorConsultarDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultarDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultarDisciplina(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultarDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
		getHistoricoGradeAnteriorAlteradaVO().setDisciplinaVO(obj);
		Uteis.liberarListaMemoria(this.getListaConsultarDisciplina());
		this.setValorConsultarDisciplina(null);
		this.setCampoConsultarDisciplina(null);
	}

	public void limparCampoDisciplina() {
		getHistoricoGradeAnteriorAlteradaVO().setDisciplinaVO(null);
	}

	public List<SelectItem> getTipoConsultarComboDisciplina() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public HistoricoGradeAnteriorAlteradaVO getHistoricoGradeAnteriorAlteradaVO() {
		if (historicoGradeAnteriorAlteradaVO == null) {
			historicoGradeAnteriorAlteradaVO = new HistoricoGradeAnteriorAlteradaVO();
		}
		return historicoGradeAnteriorAlteradaVO;
	}

	public void setHistoricoGradeAnteriorAlteradaVO(HistoricoGradeAnteriorAlteradaVO historicoGradeAnteriorAlteradaVO) {
		this.historicoGradeAnteriorAlteradaVO = historicoGradeAnteriorAlteradaVO;
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

	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}

	public List<HistoricoGradeAnteriorAlteradaVO> getHistoricoGradeAnteriorAlteradaVOs() {
		if (historicoGradeAnteriorAlteradaVOs == null) {
			historicoGradeAnteriorAlteradaVOs = new ArrayList<HistoricoGradeAnteriorAlteradaVO>(0);
		}
		return historicoGradeAnteriorAlteradaVOs;
	}

	public void setHistoricoGradeAnteriorAlteradaVOs(List<HistoricoGradeAnteriorAlteradaVO> historicoGradeAnteriorAlteradaVOs) {
		this.historicoGradeAnteriorAlteradaVOs = historicoGradeAnteriorAlteradaVOs;
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

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public String getValorConsultaCidade() {
		if (valorConsultaCidade == null) {
			valorConsultaCidade = "";
		}
		return valorConsultaCidade;
	}

	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	public String getCampoConsultaCidade() {
		if (campoConsultaCidade == null) {
			campoConsultaCidade = "";
		}
		return campoConsultaCidade;
	}

	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	public List<CidadeVO> getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<CidadeVO>(0);
		}
		return listaConsultaCidade;
	}

	public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}

	public HistoricoGradeAnteriorAlteradaVO getHistoricoGradeAnteriorAlteradaTempVO() {
		if (historicoGradeAnteriorAlteradaTempVO == null) {
			historicoGradeAnteriorAlteradaTempVO = new HistoricoGradeAnteriorAlteradaVO();
		}
		return historicoGradeAnteriorAlteradaTempVO;
	}

	public void setHistoricoGradeAnteriorAlteradaTempVO(HistoricoGradeAnteriorAlteradaVO historicoGradeAnteriorAlteradaTempVO) {
		this.historicoGradeAnteriorAlteradaTempVO = historicoGradeAnteriorAlteradaTempVO;
	}

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>();
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public Boolean getApresentarGradeCurricular() {
		return !getMatriculaVO().getMatricula().equals("");
	}

	public List<SelectItem> getListaSelectItemMatriculaPeriodo() {
		if (listaSelectItemMatriculaPeriodo == null) {
			listaSelectItemMatriculaPeriodo = new ArrayList<SelectItem>();
		}
		return listaSelectItemMatriculaPeriodo;
	}

	public void setListaSelectItemMatriculaPeriodo(List<SelectItem> listaSelectItemMatriculaPeriodo) {
		this.listaSelectItemMatriculaPeriodo = listaSelectItemMatriculaPeriodo;
	}

	public String getCampoConsultarDisciplina() {
		if (campoConsultarDisciplina == null) {
			campoConsultarDisciplina = "";
		}
		return campoConsultarDisciplina;
	}

	public void setCampoConsultarDisciplina(String campoConsultarDisciplina) {
		this.campoConsultarDisciplina = campoConsultarDisciplina;
	}

	public String getValorConsultarDisciplina() {
		if (valorConsultarDisciplina == null) {
			valorConsultarDisciplina = "";
		}
		return valorConsultarDisciplina;
	}

	public void setValorConsultarDisciplina(String valorConsultarDisciplina) {
		this.valorConsultarDisciplina = valorConsultarDisciplina;
	}

	public List<DisciplinaVO> getListaConsultarDisciplina() {
		if (listaConsultarDisciplina == null) {
			listaConsultarDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultarDisciplina;
	}

	public void setListaConsultarDisciplina(List<DisciplinaVO> listaConsultarDisciplina) {
		this.listaConsultarDisciplina = listaConsultarDisciplina;
	}

	public Boolean getApresentarBotaoGravar() {
		return !getHistoricoGradeAnteriorAlteradaVOs().isEmpty();
	}

	public Boolean getApresentarBotaoAposSelecaoMatricula() {
		return !getMatriculaVO().getMatricula().equals("");
	}

}
