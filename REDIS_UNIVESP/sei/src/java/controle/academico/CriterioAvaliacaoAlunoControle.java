package controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CriterioAvaliacaoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoNotaConceitoVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.academico.CriterioAvaliacaoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoMatriculaPeriodo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("CriterioAvaliacaoAlunoControle")
@Lazy
@Scope("viewScope")
public class CriterioAvaliacaoAlunoControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2415700398026000257L;
	private CriterioAvaliacaoVO criterioAvaliacaoVO;
	private List<CriterioAvaliacaoVO> criterioAvaliacaoVOs;
	private MatriculaVO matriculaVO;
	private TurmaVO turmaVO;
	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemOpcaoConsulta;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemSituacao;

	private List<SelectItem> listaSelectItemNota;
	private Integer unidadeEnsino;
	private Integer gradeCurricular;
	private String situacao;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private List<SelectItem> tipoConsultaComboAluno;
	private List<SelectItem> tipoConsultaComboTurma;

	private Integer criterioAvaliacaoDisciplina;
	private Integer criterioAvaliacaoNotaConceito;
	private Integer criterioAvaliacaoEixoDisciplina;
	private Integer criterioAvaliacaoIndicador;
	private String origemCriterioAvaliacaoIndicador;
	private Integer nota;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemTurmaVisaoCoordenador;
	private List<SelectItem> listaSelectItemDisciplinasTurma;
	private DisciplinaVO disciplinaVO;
	private Boolean buscarTurmasAnteriores;
	private String ano;
	private String semestre;
	private PessoaVO professor;
	private List<SelectItem> listaSelectItemProfessores;
	private List<SelectItem> listaSelectItemCriterioAvaliacaoNotaConceito;
	private CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO;
	private String notaBimestreSelecionado;
	private Boolean isPermissaoAcessarAlteracoesCriterioAvaliacoesGerais;

	public CriterioAvaliacaoAlunoControle() {
		getControleConsulta().setCampoConsulta("matricula");
	}

	public void persistir() {
		try {
			getFacadeFactory().getCriterioAvaliacaoAlunoFacade().incluirCriterioAvaliacaoAlunoVO(getCriterioAvaliacaoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			getCriterioAvaliacaoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getCriterioAvaliacaoAlunoFacade().excluirCriterioAvaliacaoAlunoVO(getCriterioAvaliacaoVO(), new ArrayList<CriterioAvaliacaoPeriodoLetivoVO>(0), true, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoAlunoCons.xhtml");
		} catch (Exception e) {
			getCriterioAvaliacaoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoAlunoForm.xhtml");
		}
	}

	public String editar() {
		try {
			CriterioAvaliacaoVO criterioAvaliacaoVO = (CriterioAvaliacaoVO) getRequestMap().get("criterioAvaliacaoItens");
			List<CriterioAvaliacaoVO> criterioAvaliacaoVOs = null;
			if (criterioAvaliacaoVO.getCriterioAvaliacaoAlunoRespondido()) {
				criterioAvaliacaoVOs = getFacadeFactory().getCriterioAvaliacaoAlunoFacade().consultarCriterioAvaliacaoAlunoRespondido("matriculaPeriodo", criterioAvaliacaoVO.getMatriculaPeriodoVO().getCodigo(), "", 0, 0, criterioAvaliacaoVO.getMatriculaPeriodoVO().getAno(), criterioAvaliacaoVO.getMatriculaPeriodoVO().getSemestre(), criterioAvaliacaoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), criterioAvaliacaoVO.getGradeCurricularVO().getCodigo(), false, false, NivelMontarDados.TODOS, true, getUsuarioLogado());
			} else {
				criterioAvaliacaoVOs = getFacadeFactory().getCriterioAvaliacaoAlunoFacade().consultarCriterioAvaliacaoAlunoNaoRespondido("matriculaPeriodo", criterioAvaliacaoVO.getMatriculaPeriodoVO().getCodigo(), "", 0, 0, criterioAvaliacaoVO.getMatriculaPeriodoVO().getAno(), criterioAvaliacaoVO.getMatriculaPeriodoVO().getSemestre(), criterioAvaliacaoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), criterioAvaliacaoVO.getGradeCurricularVO().getCodigo(), false, false, NivelMontarDados.TODOS, true, getUsuarioLogado());
			}
			if (!criterioAvaliacaoVOs.isEmpty()) {
				setCriterioAvaliacaoVO(criterioAvaliacaoVOs.get(0));
				montarListaSelectItemNota();
				getFacadeFactory().getCriterioAvaliacaoAlunoFacade().realizacaoCriacaoOpcaoNotaConceito(getCriterioAvaliacaoVO().getCriterioAvaliacaoPeriodoLetivoVOs().get(0), getConfiguracaoGeralPadraoSistema());
				setMensagemID("msg_entre_dados", Uteis.ALERTA);
				if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoVisaoCoordenadorForm.xhtml");
				}
			} else {
				setMensagemID("msg_dados_nao_encontrados", Uteis.ALERTA);
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoAlunoCons.xhtml");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoAlunoForm.xhtml");
	}

	public String irPaginaConsultar() {
		limparMensagem();
		getCriterioAvaliacaoVOs().clear();
		return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoAlunoCons.xhtml");
	}

	public String consultar() {
		try {
			if (!getApresentarSemestre()) {
				setSemestre("");
			}
			if (!getApresentarAno()) {
				setAno("");
			}
			setCriterioAvaliacaoVOs(getFacadeFactory().getCriterioAvaliacaoAlunoFacade().consultarCriterioAvaliacaoAlunoResponder(getControleConsulta().getCampoConsulta(), 0, getMatriculaVO(), 0, getSituacao(), getTurmaVO(), getAno(), getSemestre(), getUnidadeEnsino(), getGradeCurricular(), false, false, NivelMontarDados.BASICO, true, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getCriterioAvaliacaoVOs().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoAlunoCons.xhtml");
	}
	
	public Boolean getApresentarMatricula() {
		if (getControleConsulta().getCampoConsulta().equals("matricula")) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getApresentarAno() {
		if (getControleConsulta().getCampoConsulta().equals("matricula") && !getMatriculaVO().getMatricula().trim().isEmpty() && !getMatriculaVO().getCurso().getPeriodicidade().equals("IN")) {
			return true;
		}
		if (getControleConsulta().getCampoConsulta().equals("turma") && getTurmaVO().getCodigo() > 0 && !getTurmaVO().getCurso().getPeriodicidade().equals("IN")) {
			return true;
		}

		return false;
	}

	public Boolean getApresentarSemestre() {
		if (getControleConsulta().getCampoConsulta().equals("matricula") && !getMatriculaVO().getMatricula().trim().isEmpty() && getMatriculaVO().getCurso().getPeriodicidade().equals("SE")) {
			return true;
		}
		if (getControleConsulta().getCampoConsulta().equals("turma") && getTurmaVO().getCodigo() > 0 && getTurmaVO().getCurso().getPeriodicidade().equals("SE")) {
			return true;
		}
		return false;
	}

	public void consultarMatriculaPorMatricula() {
		try {
			inicializarDadosMatriculaVO(getMatriculaVO());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMatriculaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public CriterioAvaliacaoVO getCriterioAvaliacaoVO() {
		if (criterioAvaliacaoVO == null) {
			criterioAvaliacaoVO = new CriterioAvaliacaoVO();
		}
		return criterioAvaliacaoVO;
	}

	public void setCriterioAvaliacaoVO(CriterioAvaliacaoVO criterioAvaliacaoVO) {
		this.criterioAvaliacaoVO = criterioAvaliacaoVO;
	}

	public List<CriterioAvaliacaoVO> getCriterioAvaliacaoVOs() {
		if (criterioAvaliacaoVOs == null) {
			criterioAvaliacaoVOs = new ArrayList<CriterioAvaliacaoVO>(0);
		}
		return criterioAvaliacaoVOs;
	}

	public void setCriterioAvaliacaoVOs(List<CriterioAvaliacaoVO> criterioAvaliacaoVOs) {
		this.criterioAvaliacaoVOs = criterioAvaliacaoVOs;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		if (listaSelectItemSemestre == null) {
			listaSelectItemSemestre = new ArrayList<SelectItem>(0);
			listaSelectItemSemestre.add(new SelectItem("", ""));
			listaSelectItemSemestre.add(new SelectItem("1", "1º"));
			listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectItemSemestre;
	}

	public void setListaSelectItemSemestre(List<SelectItem> listaSelectItemSemestre) {
		this.listaSelectItemSemestre = listaSelectItemSemestre;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsulta() {
		if (listaSelectItemOpcaoConsulta == null) {
			listaSelectItemOpcaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsulta.add(new SelectItem("matricula", "Matrícula"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("turma", "Turma"));
		}
		return listaSelectItemOpcaoConsulta;
	}

	public void setListaSelectItemOpcaoConsulta(List<SelectItem> listaSelectItemOpcaoConsulta) {
		this.listaSelectItemOpcaoConsulta = listaSelectItemOpcaoConsulta;
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

	public Integer getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Integer getGradeCurricular() {
		if (gradeCurricular == null) {
			gradeCurricular = 0;
		}
		return gradeCurricular;
	}

	public void setGradeCurricular(Integer gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				if (unidadeEnsinoVOs.size() > 1) {
					listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
				}
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}

		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null) {
			listaSelectItemSituacao = new ArrayList<SelectItem>(0);
			listaSelectItemSituacao.add(new SelectItem("", ""));
			listaSelectItemSituacao.add(new SelectItem("respondido", "Respondido"));
			listaSelectItemSituacao.add(new SelectItem("naoRespondido", "Não Respondido"));
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@SuppressWarnings("unchecked")
	public void imprimir() {
		try {
			getCriterioAvaliacaoVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getCriterioAvaliacaoVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado()));
			getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "CriterioAvaliacaoAlunoRel.jrxml");
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Avaliação Individual");
			getSuperParametroRelVO().getListaObjetos().clear();
			getSuperParametroRelVO().getListaObjetos().add(getCriterioAvaliacaoVO());
			getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoLogado().getNome());
			getSuperParametroRelVO().adicionarParametro("caminhoArquivoFixo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo());
			getSuperParametroRelVO().adicionarParametro("unidadeEnsinoNome", getCriterioAvaliacaoVO().getUnidadeEnsino().getNome());
			getSuperParametroRelVO().adicionarParametro("unidadeEnsinoEndereco", getCriterioAvaliacaoVO().getUnidadeEnsino().getEndereco());
			getSuperParametroRelVO().adicionarParametro("unidadeEnsinoNumero", getCriterioAvaliacaoVO().getUnidadeEnsino().getNumero());
			getSuperParametroRelVO().adicionarParametro("unidadeEnsinoSetor", getCriterioAvaliacaoVO().getUnidadeEnsino().getSetor());
			getSuperParametroRelVO().adicionarParametro("unidadeEnsinoCidadeNome", getCriterioAvaliacaoVO().getUnidadeEnsino().getCidade().getNome());
			getSuperParametroRelVO().adicionarParametro("unidadeEnsinoCidadeEstadoSigla", getCriterioAvaliacaoVO().getUnidadeEnsino().getCidade().getEstado().getSigla());
			getSuperParametroRelVO().adicionarParametro("unidadeEnsinoCep", getCriterioAvaliacaoVO().getUnidadeEnsino().getCEP());
			getSuperParametroRelVO().adicionarParametro("unidadeEnsinoSite", getCriterioAvaliacaoVO().getUnidadeEnsino().getSite());
			getSuperParametroRelVO().adicionarParametro("unidadeEnsinoEmail", getCriterioAvaliacaoVO().getUnidadeEnsino().getEmail());
			getSuperParametroRelVO().adicionarParametro("unidadeEnsinoTelComercial1", getCriterioAvaliacaoVO().getUnidadeEnsino().getTelComercial1());
			getSuperParametroRelVO().adicionarParametro("nota", 5);
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
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

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
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

	public void consultarTurmaPorIdentificador() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaEspecifico(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getCodigo());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarTurma() {
		try {
			getListaConsultaTurma().clear();
			if (getUnidadeEnsino().intValue() != 0) {
				if (getCampoConsultaTurma().equals("identificadorTurma")) {
					setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoIdentificadorTurma(getUnidadeEnsino(), getValorConsultaTurma(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				}
				if (getCampoConsultaTurma().equals("nomeCurso")) {
					setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaTurma(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				}
				setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		setTurmaVO(obj);
		setUnidadeEnsino(getTurmaVO().getUnidadeEnsino().getCodigo());
		setAno("");
		setSemestre("");
	}

	public void limparTurma() throws Exception {
		setTurmaVO(null);
		setAno("");
		setSemestre("");
	}

	public void limparDadosAluno() throws Exception {
		setMatriculaVO(null);
		setAno("");
		setSemestre("");
	}

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			inicializarDadosMatriculaVO(obj);
		} catch (Exception e) {
			setMatriculaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarAluno() {
		try {
			getListaConsultaAluno().clear();
			if (this.getUnidadeEnsino() != 0) {
				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsino(), false, getUsuarioLogado()));
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsino(), false, getUsuarioLogado()));
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsino(), false, getUsuarioLogado()));
				}

			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultaAluno().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarNotaConceito() {
		try {
			getFacadeFactory().getCriterioAvaliacaoAlunoFacade().selecionarNotaConceito(getCriterioAvaliacaoVO().getCriterioAvaliacaoPeriodoLetivoVOs().get(0), getCriterioAvaliacaoDisciplina(), getCriterioAvaliacaoEixoDisciplina(), getCriterioAvaliacaoIndicador(), getOrigemCriterioAvaliacaoIndicador(), getNota(), getCriterioAvaliacaoNotaConceito());
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		}
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAluno;
	}

	public void setTipoConsultaComboAluno(List<SelectItem> tipoConsultaComboAluno) {
		this.tipoConsultaComboAluno = tipoConsultaComboAluno;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador Turma"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public void setTipoConsultaComboTurma(List<SelectItem> tipoConsultaComboTurma) {
		this.tipoConsultaComboTurma = tipoConsultaComboTurma;
	}

	public List<SelectItem> getListaSelectItemNota() {
		if (listaSelectItemNota == null) {
			listaSelectItemNota = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota;
	}

	public void setListaSelectItemNota(List<SelectItem> listaSelectItemNota) {
		this.listaSelectItemNota = listaSelectItemNota;
	}

	public void montarListaSelectItemNota() {
		try {
			if (!getCriterioAvaliacaoVO().getCriterioAvaliacaoPeriodoLetivoVOs().isEmpty()) {
				CriterioAvaliacaoNotaConceitoVO canc = new CriterioAvaliacaoNotaConceitoVO();
				canc.getNotaConceitoIndicadorAvaliacao().setUrlImagem(UteisJSF.getUrlAplicacaoExterna() + "/resources/imagens/caixainput.png");
				canc.setOrdem(0);
				getCriterioAvaliacaoVO().getCriterioAvaliacaoPeriodoLetivoVOs().get(0).getCriterioAvaliacaoNotaConceitoVOs().add(canc);
				Ordenacao.ordenarLista(getCriterioAvaliacaoVO().getCriterioAvaliacaoPeriodoLetivoVOs().get(0).getCriterioAvaliacaoNotaConceitoVOs(), "ordem");
				for (CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO : getCriterioAvaliacaoVO().getCriterioAvaliacaoPeriodoLetivoVOs().get(0).getCriterioAvaliacaoNotaConceitoVOs()) {
					if (!criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getNomeArquivo().trim().isEmpty()) {
						criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().setUrlImagem(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getPastaBaseArquivo().getValue() + "/" + criterioAvaliacaoNotaConceitoVO.getNotaConceitoIndicadorAvaliacao().getNomeArquivo());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selecionarCriterioAvaliacaoIndicadorDisciplina() {
		try {
			setCriterioAvaliacaoIndicadorVO((CriterioAvaliacaoIndicadorVO) context().getExternalContext().getRequestMap().get("criterioAvaliacaoIndicadorItens"));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		}
	}

	public void selecionarCriterioAvaliacaoIndicadorGeral() {
		try {
			setCriterioAvaliacaoIndicadorVO((CriterioAvaliacaoIndicadorVO) context().getExternalContext().getRequestMap().get("criterioAvaliacaoIndicadorPeriodoLetivo"));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		}
	}

	public void registrarCriterioAvaliacaoNotaConceito() {
		try {
			CriterioAvaliacaoNotaConceitoVO obj = (CriterioAvaliacaoNotaConceitoVO) context().getExternalContext().getRequestMap().get("notaLegenda");
			if (isNotaBimestre1Selecionado()) {
				getCriterioAvaliacaoIndicadorVO().getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito1Bimestre(obj);
			} else if (isNotaBimestre2Selecionado()) {
				getCriterioAvaliacaoIndicadorVO().getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito2Bimestre(obj);
			} else if (isNotaBimestre3Selecionado()) {
				getCriterioAvaliacaoIndicadorVO().getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito3Bimestre(obj);
			} else if (isNotaBimestre4Selecionado()) {
				getCriterioAvaliacaoIndicadorVO().getCriterioAvaliacaoAlunoVO().setCriterioAvaliacaoNotaConceito4Bimestre(obj);
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.SUCESSO);
		}
	}

	public Integer getCriterioAvaliacaoDisciplina() {
		if (criterioAvaliacaoDisciplina == null) {
			criterioAvaliacaoDisciplina = 0;
		}
		return criterioAvaliacaoDisciplina;
	}

	public void setCriterioAvaliacaoDisciplina(Integer criterioAvaliacaoDisciplina) {
		this.criterioAvaliacaoDisciplina = criterioAvaliacaoDisciplina;
	}

	public Integer getCriterioAvaliacaoEixoDisciplina() {
		if (criterioAvaliacaoEixoDisciplina == null) {
			criterioAvaliacaoEixoDisciplina = 0;
		}
		return criterioAvaliacaoEixoDisciplina;
	}

	public void setCriterioAvaliacaoEixoDisciplina(Integer criterioAvaliacaoEixoDisciplina) {
		this.criterioAvaliacaoEixoDisciplina = criterioAvaliacaoEixoDisciplina;
	}

	public Integer getCriterioAvaliacaoIndicador() {
		if (criterioAvaliacaoIndicador == null) {
			criterioAvaliacaoIndicador = 0;
		}
		return criterioAvaliacaoIndicador;
	}

	public void setCriterioAvaliacaoIndicador(Integer criterioAvaliacaoIndicador) {
		this.criterioAvaliacaoIndicador = criterioAvaliacaoIndicador;
	}

	public String getOrigemCriterioAvaliacaoIndicador() {
		if (origemCriterioAvaliacaoIndicador == null) {
			origemCriterioAvaliacaoIndicador = "";
		}
		return origemCriterioAvaliacaoIndicador;
	}

	public void setOrigemCriterioAvaliacaoIndicador(String origemCriterioAvaliacaoIndicador) {
		this.origemCriterioAvaliacaoIndicador = origemCriterioAvaliacaoIndicador;
	}

	public Integer getCriterioAvaliacaoNotaConceito() {
		if (criterioAvaliacaoNotaConceito == null) {
			criterioAvaliacaoNotaConceito = 0;
		}
		return criterioAvaliacaoNotaConceito;
	}

	public void setCriterioAvaliacaoNotaConceito(Integer criterioAvaliacaoNotaConceito) {
		this.criterioAvaliacaoNotaConceito = criterioAvaliacaoNotaConceito;
	}

	public Integer getNota() {
		if (nota == null) {
			nota = 0;
		}
		return nota;
	}

	public void setNota(Integer nota) {
		this.nota = nota;
	}

	
	public void inicializarMenuCriterioAvaliacaoAluno() throws Exception {
		if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
			registrarAtividadeUsuario(getUsuarioLogado(), "CriterioAvaliacaoAlunoControle", "Inicializando Menu Criteario Avaliacao Aluno", "Inicializando");
			consultarCritearioAvaliacaoAluno(getVisaoAlunoControle().getMatricula());
		}
//		return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoAlunoVisaoPais.xhtml");
	}

	public void consultarCritearioAvaliacaoAluno(MatriculaVO matriculaVO) throws Exception {
		try {
			setCriterioAvaliacaoVOs(getFacadeFactory().getCriterioAvaliacaoAlunoFacade().consultarCriterioAvaliacaoAlunoResponder(getControleConsulta().getCampoConsulta(), 0, matriculaVO, 0, getSituacao(), getTurmaVO(), getAno(), getSemestre(), getUnidadeEnsino(), getGradeCurricular(), true, false, NivelMontarDados.TODOS, false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirRelatorioVisaoPais() {
		try {
			CriterioAvaliacaoVO criterioAvaliacaoVO = (CriterioAvaliacaoVO) getRequestMap().get("criterioAvaliacaoItens");
			setCriterioAvaliacaoVO(criterioAvaliacaoVO);
			montarListaSelectItemNota();
			getFacadeFactory().getCriterioAvaliacaoAlunoFacade().realizacaoCriacaoOpcaoNotaConceito(getCriterioAvaliacaoVO().getCriterioAvaliacaoPeriodoLetivoVOs().get(0), getConfiguracaoGeralPadraoSistema());
			imprimir();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String inicializarMenuCriterioAvaliacaoVisaoProfessor() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "CriterioAvaliacaoAlunoControle", "Inicializando Menu Criteario Avaliacao Aluno Visao Professor", "Inicializando");
		setTurmaVO(new TurmaVO());
		setDisciplinaVO(new DisciplinaVO());
		setAno(Uteis.getAnoDataAtual4Digitos());
		setSemestre(Uteis.getSemestreAtual());
		setSituacao("");
		setControleConsulta(new ControleConsulta());
		setCriterioAvaliacaoVOs(new ArrayList<CriterioAvaliacaoVO>(0));
		setListaSelectItemDisciplinasTurma(new ArrayList<SelectItem>(0));
		montarListaSelectItemTurmaProfessor();
		return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoVisaoProfessorCons.xhtml");
		// return "criterioAvaliacaoVisaoProfessor";
	}

	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			if (!getTurmaVO().getCodigo().equals(0)) {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado()));
			}
			if (getTurmaVO().getCodigo() != 0) {
				setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(consultarDisciplinaProfessorTurma(), "codigo", "nome", false));
			}
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	public void montarListaSelectItemTurmaProfessor() {
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		List<TurmaVO> listaTurmas = null;
		String value = null;
		String nomeCurso = null;
		try {
			listaTurmas = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : listaTurmas) {
				if (!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())) {
					getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
					mapAuxiliarSelectItem.add(turmaVO.getCodigo());
				}
				// }
				removerObjetoMemoria(turmaVO);
			}
			setListaSelectItemDisciplinasTurma(new ArrayList<SelectItem>(0));
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		} finally {
			Uteis.liberarListaMemoria(listaTurmas);
			value = null;
			nomeCurso = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() {
		try {
			if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true);
			} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), true, false);
			} else {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return null;
	}

	public void montarListaSelectItemDisciplinasProfessor() {
		try {
			setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, true, false, getUsuarioLogado()), "codigo", "nome", false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() {
		List listaConsultas = new ArrayList<SelectItem>(0);
		try {
			if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaAgrupada(getProfessor().getCodigo(), getTurmaVO().getCodigo(), getSemestre(), getAno(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), true);
			} else {
				listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaAgrupada(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getSemestre(), getAno(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (List<DisciplinaVO>) listaConsultas;
	}

	public void consultarCriterioAvaliacaoVisaoProfessor() {
		try {
			getControleConsulta().setListaConsulta(getFacadeFactory().getCriterioAvaliacaoAlunoFacade().consultarCriterioAvaliacaoAlunoResponder("turma", 0, getMatriculaVO(), getDisciplinaVO().getCodigo(), getSituacao(), getTurmaVO(), getAno(), getSemestre(), getTurmaVO().getUnidadeEnsino().getCodigo(), getGradeCurricular(), false, true, NivelMontarDados.TODOS, true, getUsuarioLogado()));
			if (getControleConsulta().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCriterioAvaliacaoVisaoCoordenador() {
		try {
			getControleConsulta().setListaConsulta(getFacadeFactory().getCriterioAvaliacaoAlunoFacade().consultarCriterioAvaliacaoAlunoResponder("turma", 0, getMatriculaVO(), 0, getSituacao(), getTurmaVO(), getAno(), getSemestre(), getTurmaVO().getUnidadeEnsino().getCodigo(), getGradeCurricular(), false, false, NivelMontarDados.BASICO, true, getUsuarioLogado()));
			if (getControleConsulta().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
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

	public List<SelectItem> getListaSelectItemDisciplinasTurma() {
		if (listaSelectItemDisciplinasTurma == null) {
			listaSelectItemDisciplinasTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinasTurma;
	}

	public void setListaSelectItemDisciplinasTurma(List<SelectItem> listaSelectItemDisciplinasTurma) {
		this.listaSelectItemDisciplinasTurma = listaSelectItemDisciplinasTurma;
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

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (!getTurmaVO().getCodigo().equals(0)) {
				if (getTurmaVO().getSemestral()) {
					if (Uteis.isAtributoPreenchido(getAno())) {
						setAno(Uteis.getAnoDataAtual());
					}
					return true;
				} else if (getTurmaVO().getAnual()) {
					if (Uteis.isAtributoPreenchido(getAno())) {
						setAno(Uteis.getAnoDataAtual());
					}
					return true;
				} else {
					setAno("");
					return false;
				}
			}
			return true;
		}
		return true;
	}

	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (!getTurmaVO().getCodigo().equals(0)) {
				if (getTurmaVO().getSemestral()) {
					if (Uteis.isAtributoPreenchido(getSemestre())) {
						setSemestre(Uteis.getSemestreAtual());
					}
					return true;
				} else {
					setSemestre("");
					return false;
				}
			}
			return true;
		}
		return true;
	}

	public String editarVisaoProfessor() {
		try {
			CriterioAvaliacaoVO criterioAvaliacaoVO = (CriterioAvaliacaoVO) getRequestMap().get("criterioAvaliacaoItens");
			List<CriterioAvaliacaoVO> criterioAvaliacaoVOs = new ArrayList<>();
			if (criterioAvaliacaoVO != null) {
				criterioAvaliacaoVOs.addAll(getFacadeFactory().getCriterioAvaliacaoAlunoFacade().consultarCriterioAvaliacaoAlunoRespondido("matriculaPeriodo", criterioAvaliacaoVO.getMatriculaPeriodoVO().getCodigo(), "", getDisciplinaVO().getCodigo(), 0, criterioAvaliacaoVO.getMatriculaPeriodoVO().getAno(), criterioAvaliacaoVO.getMatriculaPeriodoVO().getSemestre(), criterioAvaliacaoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), criterioAvaliacaoVO.getGradeCurricularVO().getCodigo(), false, true, NivelMontarDados.TODOS, true, getUsuarioLogado()));
				criterioAvaliacaoVOs.addAll(getFacadeFactory().getCriterioAvaliacaoAlunoFacade().consultarCriterioAvaliacaoAlunoNaoRespondido("matriculaPeriodo", criterioAvaliacaoVO.getMatriculaPeriodoVO().getCodigo(), "", getDisciplinaVO().getCodigo(), 0, criterioAvaliacaoVO.getMatriculaPeriodoVO().getAno(), criterioAvaliacaoVO.getMatriculaPeriodoVO().getSemestre(), criterioAvaliacaoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), criterioAvaliacaoVO.getGradeCurricularVO().getCodigo(), false, true, NivelMontarDados.TODOS, true, getUsuarioLogado()));
				if (!criterioAvaliacaoVOs.isEmpty()) {
					setCriterioAvaliacaoVO(criterioAvaliacaoVOs.get(0));
					montarListaSelectItemNota();
					getFacadeFactory().getCriterioAvaliacaoAlunoFacade().realizacaoCriacaoOpcaoNotaConceito(getCriterioAvaliacaoVO().getCriterioAvaliacaoPeriodoLetivoVOs().get(0), getConfiguracaoGeralPadraoSistema());
					setMensagemID("msg_entre_dados", Uteis.ALERTA);
					verificarAlteracoesCriterioAvaliacoesGerais();
					return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoVisaoProfessorForm.xhtml");
				} else {
					setMensagemID("msg_dados_nao_encontrados", Uteis.ALERTA);
				}
				verificarAlteracoesCriterioAvaliacoesGerais();
				return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoVisaoProfessorForm.xhtml");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoVisaoProfessorCons.xhtml");
	}

	public String irPaginaConsultarVisaoProfessor() {
		// setTurmaVO(new TurmaVO());
		// setDisciplinaVO(new DisciplinaVO());
		// setAno("");
		// setSemestre("");
		// setControleConsulta(new ControleConsulta());
		// setCriterioAvaliacaoVOs(new ArrayList<CriterioAvaliacaoVO>(0));
		// setListaSelectItemDisciplinasTurma(new ArrayList<SelectItem>(0));
		// setSituacao("");
		// montarListaSelectItemTurmaProfessor();
		if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoVisaoCoordenadorCons.xhtml");
		} else {
			return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoVisaoProfessorCons.xhtml");
		}
	}

	public List<SelectItem> getCampoSemestreTurma() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);

		objs.add(new SelectItem("1", "1º"));
		objs.add(new SelectItem("2", "2º"));

		return objs;
	}

	public Boolean getBuscarTurmasAnteriores() {
		if (buscarTurmasAnteriores == null) {
			buscarTurmasAnteriores = false;
		}
		return buscarTurmasAnteriores;
	}

	public void setBuscarTurmasAnteriores(Boolean buscarTurmasAnteriores) {
		this.buscarTurmasAnteriores = buscarTurmasAnteriores;
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

	public String inicializarMenuCriterioAvaliacaoVisaoCoordenador() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "CriterioAvaliacaoAlunoControle", "Inicializando Menu Criteario Avaliacao Aluno Visao Coordenador", "Inicializando");
		setTurmaVO(new TurmaVO());
		setSemestre(Uteis.getSemestreAtual());
		setControleConsulta(new ControleConsulta());
		setCriterioAvaliacaoVOs(new ArrayList<CriterioAvaliacaoVO>(0));
		setListaSelectItemTurmaVisaoCoordenador(new ArrayList<SelectItem>(0));
		setListaSelectItemDisciplinasTurma(new ArrayList<SelectItem>(0));
		setDisciplinaVO(new DisciplinaVO());
		setAno(Uteis.getAnoDataAtual4Digitos());
		montarListaSelectItemTurmaCoordenador();
		return Uteis.getCaminhoRedirecionamentoNavegacao("criterioAvaliacaoVisaoCoordenadorCons.xhtml");
		// return "criterioAvaliacaoVisaoCoordenador";
	}

	public void montarListaSelectItemTurmaCoordenador() {
		List<TurmaVO> listaTurmas = null;
		String value = null;
		String nomeCurso = null;
		try {
			listaTurmas = consultarTurmaPorCoordenador();
			getListaSelectItemTurmaVisaoCoordenador().clear();
			getListaSelectItemTurmaVisaoCoordenador().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : listaTurmas) {
				nomeCurso = turmaVO.getCurso().getNome();
				if (!nomeCurso.equals("")) {
					nomeCurso += " - ";
				}
				value = turmaVO.getIdentificadorTurma() + " : " + nomeCurso + turmaVO.getTurno().getNome();
				getListaSelectItemTurmaVisaoCoordenador().add(new SelectItem(turmaVO.getCodigo(), value));
				// }
				removerObjetoMemoria(turmaVO);
			}
			setListaSelectItemDisciplinasTurma(new ArrayList<SelectItem>(0));
		} catch (Exception e) {
			getListaSelectItemTurmaVisaoCoordenador().clear();
		} finally {
			Uteis.liberarListaMemoria(listaTurmas);
			value = null;
			nomeCurso = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorCoordenador() {
		try {
			return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return null;
	}

	public void montarListaSelectItemProfessoresTurmaCoordenador() {
		try {
			if (getTurmaVO().getCodigo() != 0) {
				getFacadeFactory().getTurmaFacade().carregarDados(getTurmaVO(), NivelMontarDados.BASICO, getUsuarioLogado());
				List<PessoaVO> resultadoConsulta = consultarProfessoresTurmaCoordenador();
				getListaSelectItemProfessores().clear();
				setListaSelectItemProfessores(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			}
			getProfessor().setCodigo(0);
			getDisciplinaVO().setCodigo(0);
			getListaSelectItemDisciplinasTurma().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@SuppressWarnings("unchecked")
	public List<PessoaVO> consultarProfessoresTurmaCoordenador() throws Exception {
		return getFacadeFactory().getPessoaFacade().consultarProfessoresDaTurmaPorTurmaAgrupada(getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getSemestre(), getAno(), false, getUsuarioLogado());
	}

	public void montarListaDisciplinaTurmaVisaoCoordenador() {
		try {
			if (!getTurmaVO().getCodigo().equals(0)) {
				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado()));
			}
			if (getTurmaVO().getCodigo() != 0) {
				setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(consultarDisciplinaProfessorTurma(), "codigo", "nome", false));
			}
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	public List<SelectItem> getListaSelectItemTurmaVisaoCoordenador() {
		if (listaSelectItemTurmaVisaoCoordenador == null) {
			listaSelectItemTurmaVisaoCoordenador = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTurmaVisaoCoordenador;
	}

	public void setListaSelectItemTurmaVisaoCoordenador(List<SelectItem> listaSelectItemTurmaVisaoCoordenador) {
		this.listaSelectItemTurmaVisaoCoordenador = listaSelectItemTurmaVisaoCoordenador;
	}

	public List<SelectItem> getListaSelectItemProfessores() {
		if (listaSelectItemProfessores == null) {
			listaSelectItemProfessores = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProfessores;
	}

	public void setListaSelectItemProfessores(List<SelectItem> listaSelectItemProfessores) {
		this.listaSelectItemProfessores = listaSelectItemProfessores;
	}

	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public List<SelectItem> getListaSelectItemCriterioAvaliacaoNotaConceito() {
		if (listaSelectItemCriterioAvaliacaoNotaConceito == null) {
			listaSelectItemCriterioAvaliacaoNotaConceito = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCriterioAvaliacaoNotaConceito;
	}

	public void setListaSelectItemCriterioAvaliacaoNotaConceito(List<SelectItem> listaSelectItemCriterioAvaliacaoNotaConceito) {
		this.listaSelectItemCriterioAvaliacaoNotaConceito = listaSelectItemCriterioAvaliacaoNotaConceito;
	}

	public CriterioAvaliacaoIndicadorVO getCriterioAvaliacaoIndicadorVO() {
		if (criterioAvaliacaoIndicadorVO == null) {
			criterioAvaliacaoIndicadorVO = new CriterioAvaliacaoIndicadorVO();
		}
		return criterioAvaliacaoIndicadorVO;
	}

	public void setCriterioAvaliacaoIndicadorVO(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO) {
		this.criterioAvaliacaoIndicadorVO = criterioAvaliacaoIndicadorVO;
	}

	public String getNotaBimestreSelecionado() {
		if (notaBimestreSelecionado == null) {
			notaBimestreSelecionado = "";
		}
		return notaBimestreSelecionado;
	}

	public void setNotaBimestreSelecionado(String notaBimestreSelecionado) {
		this.notaBimestreSelecionado = notaBimestreSelecionado;
	}

	public boolean isNotaBimestre1Selecionado() {
		return getNotaBimestreSelecionado().contains("AvaliacaoIndicador1");
	}

	public boolean isNotaBimestre2Selecionado() {
		return getNotaBimestreSelecionado().contains("AvaliacaoIndicador2");
	}

	public boolean isNotaBimestre3Selecionado() {
		return getNotaBimestreSelecionado().contains("AvaliacaoIndicador3");
	}

	public boolean isNotaBimestre4Selecionado() {
		return getNotaBimestreSelecionado().contains("AvaliacaoIndicador4");
	}

	public String getApresentarNomeBismestreSelecionado() {
		if (isNotaBimestre1Selecionado()) {
			return " 1ºB";
		} else if (isNotaBimestre2Selecionado()) {
			return " 2ºB";
		} else if (isNotaBimestre3Selecionado()) {
			return " 3ºB";
		} else if (isNotaBimestre4Selecionado()) {
			return " 4ºB";
		}
		return "";
	}
	
	@PostConstruct
	public void inicializarDados() {
		try {
			if(getUsuarioLogado().getIsApresentarVisaoProfessor()) {				
				inicializarMenuCriterioAvaliacaoVisaoProfessor();				
			}else if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				inicializarMenuCriterioAvaliacaoVisaoCoordenador();
			}else if(getUsuarioLogado().getIsApresentarVisaoAlunoOuPais()) {
				inicializarMenuCriterioAvaliacaoAluno();
			}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
	}
	
	private void inicializarDadosMatriculaVO(MatriculaVO matriculaVO) throws Exception {
		if (matriculaVO != null) {
			setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(matriculaVO.getMatricula(), getUnidadeEnsino(), false, getUsuarioLogado()));
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade()
					.consultaRapidaPorMatriculaSemestreAno(getMatriculaVO().getMatricula(), "", "", false, false, Optional.empty(), Optional.empty(), getUsuario());
			if (!Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			if (!matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodo.PREMATRICULA_CANCELADA.getValor())) {
				setUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo());
				setAno(matriculaPeriodoVO.getAno());
				setSemestre(matriculaPeriodoVO.getSemestre());
			}
		}
	}
	
	public void verificarAlteracoesCriterioAvaliacoesGerais() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAlteracoesCriterioAvaliacoesGerais",
					getUsuarioLogado());
			setIsPermissaoAcessarAlteracoesCriterioAvaliacoesGerais(Boolean.TRUE);
		} catch (Exception e) {
			setIsPermissaoAcessarAlteracoesCriterioAvaliacoesGerais(Boolean.FALSE);
		}
	}

	public Boolean getIsPermissaoAcessarAlteracoesCriterioAvaliacoesGerais() {
		return isPermissaoAcessarAlteracoesCriterioAvaliacoesGerais;
	}

	public void setIsPermissaoAcessarAlteracoesCriterioAvaliacoesGerais(
			Boolean isPermissaoAcessarAlteracoesCriterioAvaliacoesGerais) {
		this.isPermissaoAcessarAlteracoesCriterioAvaliacoesGerais = isPermissaoAcessarAlteracoesCriterioAvaliacoesGerais;
	}
}
