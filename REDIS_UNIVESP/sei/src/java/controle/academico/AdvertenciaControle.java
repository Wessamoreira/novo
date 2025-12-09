package controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.AdvertenciaVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TipoAdvertenciaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("AdvertenciaControle")
@Scope("viewScope")
@Lazy
public class AdvertenciaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private AdvertenciaVO advertenciaVO;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaConsultaAluno;
	private List<SelectItem> listaSelectItemTipoAdvertencia;
	private List<AdvertenciaVO> advertenciaVOs;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List<FuncionarioVO> listaConsultaProfessor;
	private TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO;
	private List<SelectItem> listaSelectItemTipoTextoPadrao;
	private Boolean visualizarRelatorio;

	public AdvertenciaControle() throws Exception {
		inicializarConsultar();
		montarListaSelectItemTipoAdvertencia();
	}

	public String novo() throws Exception {
		try {
			setAdvertenciaVO(new AdvertenciaVO());
			getAdvertenciaVO().setResponsavel(getUsuarioLogadoClone());
			montarListaSelectItemTipoAdvertencia();
			getAdvertenciaVO().setAno(Uteis.getAnoDataAtual());
			prencherDadosDescricaoAdvertencia();
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaForm.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaForm.xhtml");
		}
	}

	public String novoAdvertenciaVisaoCoordenador() throws Exception {
		try {
			setAdvertenciaVOs(new ArrayList<AdvertenciaVO>(0));
			setAdvertenciaVO(new AdvertenciaVO());
			getAdvertenciaVO().setResponsavel(getUsuarioLogadoClone());
			getAdvertenciaVO().setAno(Uteis.getAnoDataAtual());
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaFormCoordenador.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaFormCoordenador.xhtml");
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaCons.xhtml");
	}

	public String inicializarConsultarVisaoCoordenador() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaConsCoordenador.xhtml");
	}

	public String editar() throws Exception {
		try {
			AdvertenciaVO obj = (AdvertenciaVO) context().getExternalContext().getRequestMap().get("advertenciaItens");
			setAdvertenciaVO(obj);
			montarListaSelectItemTipoAdvertencia();
			getAdvertenciaVO().setNovoObj(false);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaForm.xhtml");
		}
	}

	public String gravar() throws Exception {
		try {
			executarValidacaoSimulacaoVisaoCoordenador();
			getFacadeFactory().getAdvertenciaFacade().persistir(getAdvertenciaVO(), true, getUsuarioLogado());
			getAdvertenciaVO().setResponsavel(getUsuarioLogadoClone());
			montarListaSelectItemTipoAdvertencia();
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaForm.xhtml");
		}
	}

	public String consultar() throws Exception {
		try {
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getAdvertenciaFacade().consultarAdvertenciaPorMatricula(getControleConsulta().getValorConsulta(), Constantes.EMPTY, false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			} else if (getControleConsulta().getCampoConsulta().equals("nome")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getAdvertenciaFacade().consultarAdvertenciaPorNomeAluno(getControleConsulta().getValorConsulta(), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			} else {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getAdvertenciaFacade().consultarPorDescricaoTipoAdvertencia(getControleConsulta().getValorConsulta(), false, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaCons.xhtml");
		}
	}

	public String excluir() throws Exception {
		try {
			executarValidacaoSimulacaoVisaoCoordenador();
			getFacadeFactory().getAdvertenciaFacade().excluir(getAdvertenciaVO(), true, getUsuarioLogado());
			setAdvertenciaVO(new AdvertenciaVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("advertenciaForm.xhtml");
		}
	}

	public void realizarPreparacaoImpressaoRelatorio() {
		try {
			setListaSelectItemTipoTextoPadrao(UtilSelectItem.getListaSelectItem(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("AD", getAdvertenciaVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), getAdvertenciaVO().getMatriculaVO().getCurso().getNivelEducacional(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), "codigo", "descricao", false));
			setMensagemID("msg_entre_prmrelatorio");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarImpressaoRelatorio() {
		try {
			ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
			impressaoContratoVO.setAdvertenciaVO(getFacadeFactory().getAdvertenciaFacade().consultarPorChavePrimaria(getAdvertenciaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			impressaoContratoVO.setProfessor(getAdvertenciaVO().getProfessor());
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getTextoPadraoDeclaracaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			String textoPadrao = getFacadeFactory().getImpressaoContratoFacade().montarDadosContratoTextoPadrao(getAdvertenciaVO().getMatriculaVO(), impressaoContratoVO, textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("textoRelatorio", textoPadrao);
			setVisualizarRelatorio(Boolean.TRUE);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setVisualizarRelatorio(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getRelatorioAdvertencia() {
    	if (getVisualizarRelatorio()) {
    		return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
    	}
    	return "";
    }

	public void consultarProfessor() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(), "PR", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaProfessor(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarProfessorPorMatricula() throws Exception {
		try {
			FuncionarioVO obj = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatriculaUnica(getAdvertenciaVO().getProfessor().getMatricula(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			if (obj.getMatricula().equals("")) {
				throw new Exception("Professor de matrícula " + getAdvertenciaVO().getProfessor().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getAdvertenciaVO().setProfessor(obj);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getAdvertenciaVO().setProfessor(null);
		}
	}

	public void limparCampoProfessor() {
		getAdvertenciaVO().setProfessor(null);
	}

	public void selecionarProfessor() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
		getAdvertenciaVO().setProfessor(obj);
		montarListaSelectItemTipoAdvertencia();
		setValorConsultaProfessor("");
		setCampoConsultaProfessor("");
		setListaConsultaProfessor(null);
	}

	public List<SelectItem> getTipoConsultaComboProfessor() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("nome", "Nome"));
		objs.add(new SelectItem("matricula", "Matrícula"));
		return objs;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("matricula", "Matrícula"));
		objs.add(new SelectItem("nome", "Nome"));
		objs.add(new SelectItem("tipoAdvertencia", "Tipo Advertência"));
		return objs;
	}

	public void consultarAlunoVisaoCoordenador() throws Exception {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!matriculaVO.getMatricula().equals("")) {
					objs.add(matriculaVO);
				} else {
					removerObjetoMemoria(matriculaVO);
				}
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

	public void consultarAlunoPorMatriculaVisaoCoordenador() throws Exception {
		try {
			MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getAdvertenciaVO().getMatriculaVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (matriculaVO.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getAdvertenciaVO().getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getAdvertenciaVO().setMatriculaVO(matriculaVO);
			montarListaSelectItemTipoAdvertencia();
		} catch (Exception e) {
			getAdvertenciaVO().setMatriculaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosAlunoVisaoCoordenador() throws Exception {
		getAdvertenciaVO().setMatriculaVO(new MatriculaVO());
		montarListaSelectItemTipoAdvertencia();
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		getAdvertenciaVO().setMatriculaVO(matriculaVO);
		setCampoConsultaAluno("");
		setValorConsultaAluno("");
		setListaConsultaAluno(null);
	}

	public void selecionarAlunoVisaoCoordenador() throws Exception {
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		getAdvertenciaVO().setMatriculaVO(matriculaVO);
		setListaConsultaAluno(null);
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTipoAdvertencia() throws Exception {
		List<TipoAdvertenciaVO> tipoAdvertenciaVOs = getFacadeFactory().getTipoAdvertenciaFacade().consultarPorDescricao("", SituacaoTipoAdvertenciaEnum.ATIVO, false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		for (TipoAdvertenciaVO tipoAdvertenciaVO : tipoAdvertenciaVOs) {
			objs.add(new SelectItem(tipoAdvertenciaVO.getCodigo(), tipoAdvertenciaVO.getDescricao()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort(objs, ordenador);
		setListaSelectItemTipoAdvertencia(objs);
	}

	public void prencherDadosDescricaoAdvertencia() throws Exception {
		TipoAdvertenciaVO obj = null;
		if (!getListaSelectItemTipoAdvertencia().isEmpty() && getAdvertenciaVO().getTipoAdvertenciaVO().getCodigo().equals(0)) {
			obj = getFacadeFactory().getTipoAdvertenciaFacade().consultarPorCodigo((Integer) getListaSelectItemTipoAdvertencia().get(0).getValue(), SituacaoTipoAdvertenciaEnum.ATIVO, false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		} else {
			obj = getFacadeFactory().getTipoAdvertenciaFacade().consultarPorCodigo(getAdvertenciaVO().getTipoAdvertenciaVO().getCodigo(), SituacaoTipoAdvertenciaEnum.ATIVO, false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}
		getAdvertenciaVO().setAdvertencia(obj.getDescricaoAdvertencia());
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		List<SelectItem> listaSelectItemSemestre = new ArrayList<SelectItem>(0);
		listaSelectItemSemestre = new ArrayList<SelectItem>(0);
		listaSelectItemSemestre.add(new SelectItem("", ""));
		listaSelectItemSemestre.add(new SelectItem("1", "1º"));
		listaSelectItemSemestre.add(new SelectItem("2", "2º"));
		return listaSelectItemSemestre;
	}

	public List<SelectItem> getListaSelectItemBimestre() {
		List<SelectItem> listaSelectItemSemestre = new ArrayList<SelectItem>(0);
		listaSelectItemSemestre.add(new SelectItem("", ""));
		listaSelectItemSemestre.add(new SelectItem(BimestreEnum.BIMESTRE_01, BimestreEnum.BIMESTRE_01.getValorApresentar()));
		listaSelectItemSemestre.add(new SelectItem(BimestreEnum.BIMESTRE_02, BimestreEnum.BIMESTRE_02.getValorApresentar()));
		listaSelectItemSemestre.add(new SelectItem(BimestreEnum.BIMESTRE_03, BimestreEnum.BIMESTRE_03.getValorApresentar()));
		listaSelectItemSemestre.add(new SelectItem(BimestreEnum.BIMESTRE_04, BimestreEnum.BIMESTRE_04.getValorApresentar()));		
		return listaSelectItemSemestre;
	}

	public boolean getIsExibirBimestre() {
		return getAdvertenciaVO().getMatriculaVO().getCurso().getPeriodicidade().equals("AN");
	}

	public List<SelectItem> getListaSelectItemTipoAdvertencia() {
		if (listaSelectItemTipoAdvertencia == null) {
			listaSelectItemTipoAdvertencia = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoAdvertencia;
	}

	public void setListaSelectItemTipoAdvertencia(List<SelectItem> listaSelectItemTipoAdvertencia) {
		this.listaSelectItemTipoAdvertencia = listaSelectItemTipoAdvertencia;
	}

	public void limparDadosAluno() {
		getAdvertenciaVO().setMatriculaVO(null);
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Nome Curso"));
		return itens;
	}

	public void consultarAluno() throws Exception {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!matriculaVO.getMatricula().equals("")) {
					objs.add(matriculaVO);
				} else {
					removerObjetoMemoria(matriculaVO);
				}
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
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getAdvertenciaVO().getMatriculaVO().getMatricula(), getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getAdvertenciaVO().getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			getAdvertenciaVO().setMatriculaVO(objAluno);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List consultarAdvertenciaVisaoCoordenador() throws Exception {
		getAdvertenciaVOs().clear();
		try {
			setAdvertenciaVOs(getFacadeFactory().getAdvertenciaFacade().consultarAdvertenciaVisaoCoordenador(getAdvertenciaVO().getMatriculaVO().getMatricula(), getAdvertenciaVO().getMatriculaVO().getAluno().getNome(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			if (getAdvertenciaVOs().isEmpty()) {
				throw new Exception("Não foi encontrado nenhuma advertência registrada para o aluno(a) " + getAdvertenciaVO().getMatriculaVO().getAluno().getNome() + " na matrícula "+getAdvertenciaVO().getMatriculaVO().getMatricula()+".");
			}
			setMensagemID("msg_dados_consultados");
			return getAdvertenciaVOs();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return advertenciaVOs;
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
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

	public AdvertenciaVO getAdvertenciaVO() {
		if (advertenciaVO == null) {
			advertenciaVO = new AdvertenciaVO();
		}
		return advertenciaVO;
	}

	public void setAdvertenciaVO(AdvertenciaVO advertenciaVO) {
		this.advertenciaVO = advertenciaVO;
	}

	public List<AdvertenciaVO> getAdvertenciaVOs() {
		if (advertenciaVOs == null) {
			advertenciaVOs = new ArrayList<AdvertenciaVO>(0);
		}
		return advertenciaVOs;
	}

	public void setAdvertenciaVOs(List<AdvertenciaVO> advertenciaVOs) {
		this.advertenciaVOs = advertenciaVOs;
	}

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	public List<FuncionarioVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public TextoPadraoDeclaracaoVO getTextoPadraoDeclaracaoVO() {
		if (textoPadraoDeclaracaoVO == null) {
			textoPadraoDeclaracaoVO = new TextoPadraoDeclaracaoVO();
		}
		return textoPadraoDeclaracaoVO;
	}

	public void setTextoPadraoDeclaracaoVO(TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO) {
		this.textoPadraoDeclaracaoVO = textoPadraoDeclaracaoVO;
	}

	public List<SelectItem> getListaSelectItemTipoTextoPadrao() {
		if (listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoTextoPadrao;
	}

	public void setListaSelectItemTipoTextoPadrao(List<SelectItem> listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}

	public Boolean getVisualizarRelatorio() {
		if (visualizarRelatorio == null) {
			visualizarRelatorio = Boolean.FALSE;
		}
		return visualizarRelatorio;
	}

	public void setVisualizarRelatorio(Boolean visualizarRelatorio) {
		this.visualizarRelatorio = visualizarRelatorio;
	}

}
