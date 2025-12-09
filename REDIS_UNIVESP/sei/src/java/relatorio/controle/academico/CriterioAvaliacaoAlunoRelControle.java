package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CriterioAvaliacaoVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("CriterioAvaliacaoAlunoRelControle")
@Lazy
@Scope("viewScope")
public class CriterioAvaliacaoAlunoRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5774875312094676308L;
	private List<CriterioAvaliacaoVO> criterioAvaliacaoVOs;
	private CriterioAvaliacaoVO criterioAvaliacaoVO;
	private MatriculaVO matriculaVO;
	private TurmaVO turmaVO;
	private String ano;
	private String semestre;
	private List<SelectItem> listaSelectItemSemestre;
	private List<SelectItem> listaSelectItemOpcaoConsulta;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemSituacao;
	private List<SelectItem> listaSelectItemBimestre;
	private Integer unidadeEnsino;
	private String situacao;
	private Integer bimestre;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private List<SelectItem> tipoConsultaComboAluno;
	private List<SelectItem> tipoConsultaComboTurma;
	private String tipoLayout;
	private String observacao;
	private List<SelectItem> listaSelectItemTipoLayout;
	
	public CriterioAvaliacaoAlunoRelControle(){
		try {
			getControleConsulta().setCampoConsulta("matricula");
			montarListaSelectItemTipoLayout();	
			verificarLayoutPadrao();
		} catch (Exception e) {

		}		
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

			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarPorUnidadeEnsinoIdentificadorTurma(getUnidadeEnsino(), getValorConsultaTurma(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);

		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
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

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatriculaVO(obj);
		setUnidadeEnsino(obj.getUnidadeEnsino().getCodigo());
		MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuario());
		if (matriculaPeriodo == null) {
			throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
		}
		setAno(matriculaPeriodo.getAno());
		setSemestre(matriculaPeriodo.getSemestre());
	}

	public void consultarAluno() {
		try {
			getListaConsultaAluno().clear();

			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsino(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsino(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsino(), false, getUsuarioLogado()));
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				setListaConsultaAluno(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsino(), false, getUsuarioLogado()));
			}

			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getListaConsultaAluno().clear();
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

	@SuppressWarnings("unchecked")
	public void imprimir() {
		try {
			//getTipoLayout();
			setCriterioAvaliacaoVOs(getFacadeFactory().getCriterioAvaliacaoAlunoFacade().consultarCriterioAvaliacaoAlunoResponder(getControleConsulta().getCampoConsulta(), 0, getMatriculaVO(), 0, getSituacao(), getTurmaVO(), getAno(), getSemestre(), getUnidadeEnsino(), 0, false, false, NivelMontarDados.TODOS, false, getUsuarioLogado()));
			if (!getCriterioAvaliacaoVOs().isEmpty()) {
				UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsino(), false, getUsuarioLogado());
				for (CriterioAvaliacaoVO criterioAvaliacaoVO : getCriterioAvaliacaoVOs()) {
					criterioAvaliacaoVO.setUnidadeEnsino(unidadeEnsinoVO);
				}
				getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getTipoLayout() + ".jrxml");
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				if (getTipoLayout().equals("CriterioAvaliacaoAlunoRel")) {
					getSuperParametroRelVO().setTituloRelatorio("Avaliação Individual");
				} else {
					getSuperParametroRelVO().setTituloRelatorio("Avaliação do Desenvolvimento da Criança");
					if (!getCriterioAvaliacaoVOs().isEmpty()) {
						Iterator<CriterioAvaliacaoVO> i = getCriterioAvaliacaoVOs().iterator();
						while (i.hasNext()) {
							CriterioAvaliacaoVO obj = i.next();
							obj.distribuirCriterioAvaliacao(getObservacao());
						}
					}
				}
				getSuperParametroRelVO().getListaObjetos().clear();
				getSuperParametroRelVO().setListaObjetos(getCriterioAvaliacaoVOs());
				getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoLogado().getNome());
				getSuperParametroRelVO().adicionarParametro("caminhoArquivoFixo", getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoNome", unidadeEnsinoVO.getNome());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoEndereco", unidadeEnsinoVO.getEndereco());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoNumero", unidadeEnsinoVO.getNumero());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoSetor", unidadeEnsinoVO.getSetor());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoCidadeNome", unidadeEnsinoVO.getCidade().getNome());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoCidadeEstadoSigla", unidadeEnsinoVO.getCidade().getEstado().getSigla());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoCep", unidadeEnsinoVO.getCEP());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoSite", unidadeEnsinoVO.getSite());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoEmail", unidadeEnsinoVO.getEmail());
				getSuperParametroRelVO().adicionarParametro("unidadeEnsinoTelComercial1", unidadeEnsinoVO.getTelComercial1());
				getSuperParametroRelVO().adicionarParametro("nota", getBimestre());
				if (!unidadeEnsinoVO.getCodigo().equals(0)) {
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(unidadeEnsinoVO);
				}
				realizarImpressaoRelatorio();
				persistirLayoutPadrao(getTipoLayout());				
				setMensagemID("msg_relatorio_ok", Uteis.ALERTA);
			} else {
				setMensagemID("msg_relatorio_sem_dados", Uteis.ALERTA);
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private void persistirLayoutPadrao(String valor) throws Exception {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "CriterioAvaliacaoAlunoRel", "designCriterioAvaliacaoAluno", getUsuarioLogado());
	}
	
	private void verificarLayoutPadrao() throws Exception {
		LayoutPadraoVO layoutPadraoVO = new LayoutPadraoVO();
		layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("DisciplinasGradeRel", "designDisciplinasGrade", false, getUsuarioLogado());
		if (!layoutPadraoVO.getValor().equals("")) {
			setTipoLayout(layoutPadraoVO.getValor());
		}
		removerObjetoMemoria(layoutPadraoVO);
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
			setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getMatriculaVO().getMatricula(), getUnidadeEnsino(), false, getUsuarioLogado()));
			setUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo());
			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatricula(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuario());
			if (matriculaPeriodo == null) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setAno(matriculaPeriodo.getAno());
			setSemestre(matriculaPeriodo.getSemestre());
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMatriculaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	
	public void consultarAlunoPorRegistroAcademico() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorRegistroAcademico(getMatriculaVO().getAluno().getRegistroAcademico(), this.getUnidadeEnsinoLogado().getCodigo(), 0,  Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(this.getUnidadeEnsinoLogado().getCodigo()), getUsuarioLogado()); 
			if (objAluno == null || objAluno.getMatricula().equals("") ) {
				throw new Exception("Aluno de registro Acadêmico " + getMatriculaVO().getAluno().getRegistroAcademico() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
						
				setUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo());
				MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatricula(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuario());
				if (matriculaPeriodo == null) {
					throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
				}
				setAno(matriculaPeriodo.getAno());
				setSemestre(matriculaPeriodo.getSemestre());
				setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMatriculaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
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
		objs.add(new SelectItem("CriterioAvaliacaoAlunoRel", "Layout 1"));
		objs.add(new SelectItem("CriterioAvaliacaoAlunoRel2", "Layout 2"));
		setListaSelectItemTipoLayout(objs);
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
			listaSelectItemOpcaoConsulta.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("turma", "Turma"));
		}
		return listaSelectItemOpcaoConsulta;
	}

	public void setListaSelectItemOpcaoConsulta(List<SelectItem> listaSelectItemOpcaoConsulta) {
		this.listaSelectItemOpcaoConsulta = listaSelectItemOpcaoConsulta;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				if(unidadeEnsinoVOs.size() > 1){
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

	public List<SelectItem> getListaSelectItemBimestre() {
		if (listaSelectItemBimestre == null) {
			listaSelectItemBimestre = new ArrayList<SelectItem>(0);
			listaSelectItemBimestre.add(new SelectItem(5, ""));
			listaSelectItemBimestre.add(new SelectItem(1, "1º"));
			listaSelectItemBimestre.add(new SelectItem(2, "2º"));
			listaSelectItemBimestre.add(new SelectItem(3, "3º"));
			listaSelectItemBimestre.add(new SelectItem(4, "4º"));
		}
		return listaSelectItemBimestre;
	}

	public void setListaSelectItemBimestre(List<SelectItem> listaSelectItemBimestre) {
		this.listaSelectItemBimestre = listaSelectItemBimestre;
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

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getBimestre() {
		if (bimestre == null) {
			bimestre = 5;
		}
		return bimestre;
	}

	public void setBimestre(Integer bimestre) {
		this.bimestre = bimestre;
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

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
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

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
