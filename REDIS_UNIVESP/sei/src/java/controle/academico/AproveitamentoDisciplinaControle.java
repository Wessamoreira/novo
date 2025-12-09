package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas transferenciaEntradaForm.jsp transferenciaEntradaCons.jsp) com as
 * funcionalidades da classe <code>TransferenciaEntrada</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see TransferenciaEntrada
 * @see aproveitamentoDisciplinaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.collect.Lists;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.AproveitamentoDisciplinaVO;
import negocio.comuns.academico.AproveitamentoDisciplinasEntreMatriculasVO;
import negocio.comuns.academico.ConcessaoCargaHorariaDisciplinaVO;
import negocio.comuns.academico.ConcessaoCreditoDisciplinaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisciplinasAproveitadasVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.crm.enumerador.SexoEnum;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("AproveitamentoDisciplinaControle")
@Scope("viewScope")
@Lazy
public class AproveitamentoDisciplinaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -8724355375944634524L;

	private PeriodoLetivoVO periodoLetivoPrevistoMatricula;
	private Boolean panelAproveitamentoDisciplinaAberto;
	private Boolean panelAproveitamentoDisciplinaAbertoGrupoOptativa;
	private Boolean panelAproveitamentoDisciplinaAbertoForaGrade;
	private String renderedPanelCidade;
	private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVisualizar;
	private AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO;
	private List<SelectItem> listaSelectItemPeriodoLetivo;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private List<SelectItem> listaSelectItemPeriodoLetivoMatricula;
	private String abaApresentar;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private List<SelectItem> listaSelectItemDisciplina;
	private List listaConsultaMatriculado;
	private DisciplinasAproveitadasVO disciplinasAproveitadasVO;
	private ConcessaoCreditoDisciplinaVO concessaoCreditoDisciplinaVO;
	private ConcessaoCargaHorariaDisciplinaVO concessaoCargaHorariaDisciplinaVO;
	private Boolean matriculado;
	private List listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List listaConsultaDisciplinaForaGrade;
	private List<DisciplinasAproveitadasVO> listaDisciplinasExcluidas;
	private String valorConsultaDisciplinaForaGrade;
	private String campoConsultaDisciplinaForaGrade;
	private Boolean mostrarPanelAviso;
	private Boolean mostrarPanelAvisoDisciplinaHistorico;
	private Boolean editandoDisciplinasAproveitadas;
	private GradeDisciplinaVO gradeDisciplinaVO;
	private MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO;
	private DisciplinasAproveitadasVO disciplinasAproveitadasTemporariaVO;
	private List<SelectItem> ListaSelectItemPeriodoLetivoComGrupoOptativas;
	private PeriodoLetivoVO periodoLetivoGrupoOptativaVO;
	private Integer codigoPeriodoLetivoGrupoOptativa;
	private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO;
	private String retornoRichPanel;
	private DisciplinasAproveitadasVO logDisciplinasAproveitadasVO;
	private List<MatriculaVO> matriculasAlunoValidasAproveitamento;
	private MatriculaVO matriculaAlunoAproveitarDisciplina;
	private List<SelectItem> listaSelectItemMapaEquivalenciaMatriz;
	private MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaAproveitarDisciplinasOutraMatricula;
	private AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO;
	private String anoPadraoAproveitamentoDisciplinasEntreMatriculas;
	private String semestrePadraoAproveitamentoDisciplinasEntreMatriculas;
	private Boolean utilizarAnoSemestreAtualDisciplinaAprovada;
	private String tipoAproveitamentoDisciplinasEntreMatriculas;
	private Boolean aproveitamentoPorIsencaoEntreMatriculas;
	private Boolean realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
	private String descricaoComplementacaoAproveitamentoEntreMatriculas;
	private String situacaoHistoricoAproveitamentoEntreMatriculas;
	private Boolean aproveitamentoEntreMatriculasJaExecutado; 
	
	private DataModelo dataModeloAluno;
	private List<SelectItem> comboboxSexoEnum;
	private Boolean permitirAproveitamentoDisciplinasOptativas; 
	
	public AproveitamentoDisciplinaControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		listaConsulta = new ArrayList<>(0);
		setTipoRequerimento("AD");
		setMensagemID("msg_entre_prmconsulta");
	}

	@PostConstruct
	public void iniciarAproveitamentoApartirOutraTela() {
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			if (request.getParameter("requerimento") == null || ((String) request.getParameter("requerimento")).equals("")) {
				return;
			}
			Integer codigoRequerimento = Integer.parseInt((String) request.getParameter("requerimento"));
			try {

				setAproveitamentoDisciplinaVO(getFacadeFactory().getAproveitamentoDisciplinaFacade().consultarPorCodigoRequerimento(codigoRequerimento, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
				if (getAproveitamentoDisciplinaVO().getCodigo().equals(0)) {					
					inicializarDadosAproveitamentoPrevisto(codigoRequerimento);
					realizarMontagemPainelMatrizCurricular();
				} else {
					realizarMontagemPainelMatrizCurricular();
					getAproveitamentoDisciplinaVO().setNovoObj(Boolean.FALSE);
				}
				montarListaSelectItemGradeCurricular();
				realizarVerificacaoPermitirAproveitamentoDisciplinasOptativas();
				setMensagemID("msg_dados_consultados");
			} catch (Exception e) {
				setMensagemID("msg_erro_dadosnaoencontrados");
				getAproveitamentoDisciplinaVO().getCodigoRequerimento().setCodigo(0);
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarDadosAproveitamentoPrevisto(Integer codigoRequerimento) throws Exception {
		getAproveitamentoDisciplinaVO().getCodigoRequerimento().setCodigo(codigoRequerimento);
		Integer campoConsulta = getAproveitamentoDisciplinaVO().getCodigoRequerimento().getCodigo();
		RequerimentoVO requerimento = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(campoConsulta, "", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
		getAproveitamentoDisciplinaVO().setCurso(requerimento.getMatricula().getCurso());
		getAproveitamentoDisciplinaVO().setAproveitamentoPrevisto(Boolean.TRUE);
		setEditandoDisciplinasAproveitadas(Boolean.FALSE);
		getAproveitamentoDisciplinaVO().setDisciplinaForaGrade(Boolean.FALSE);
		getAproveitamentoDisciplinaVO().getCodigoRequerimento().setCodigo(requerimento.getCodigo());
		getAproveitamentoDisciplinaVO().setPessoa(requerimento.getPessoa());
		getAproveitamentoDisciplinaVO().setUnidadeEnsino(requerimento.getUnidadeEnsino());
		getAproveitamentoDisciplinaVO().setCodigoRequerimento(requerimento);

		getFacadeFactory().getCursoFacade().carregarDados(getAproveitamentoDisciplinaVO().getCurso(), NivelMontarDados.BASICO, getUsuarioLogado());

		ConfiguracaoAcademicoVO cfgCurso = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(this.getAproveitamentoDisciplinaVO().getCurso().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado());
		this.getAproveitamentoDisciplinaVO().getCurso().setConfiguracaoAcademico(cfgCurso);
		getAproveitamentoDisciplinaVO().getMatricula().getCurso().setConfiguracaoAcademico(cfgCurso);

		getDisciplinasAproveitadasVO().setTipo("AP");
	}
	
	/**
	 * Realiza a paginação da consulta da marcacaoFeriasColetivasCons.xhtml
	 * 
	 * @param dataScrollerEvent
	 */
	public void scrollerListenerAluno(DataScrollEvent dataScrollerEvent) {
		try {
			getDataModeloAluno().setPaginaAtual(dataScrollerEvent.getPage());
			getDataModeloAluno().setPage(dataScrollerEvent.getPage());
			executarConsultaAluno();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>TransferenciaEntrada</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setMatriculado(Boolean.FALSE);
		setAproveitamentoDisciplinaVO(null);
		setAproveitamentoEntreMatriculasJaExecutado(Boolean.FALSE);
		setAproveitamentoDisciplinasEntreMatriculasVO(null);		
		setAbaApresentar("dadosGerais"); // "dadosAproveitamento";
		inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
		setDisciplinasAproveitadasVO(null);
		setListaSelectItemDisciplina(null);
		inicializarListasSelectItemTodosComboBox();
		verificarDisciplinaUsaNotaConceito();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
	}

	private String valorConsultaCidade;
	private String campoConsultaCidade;
	private List<CidadeVO> listaConsultaCidade;

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

	public void consultarCidade() {
		try {
			List objs = new ArrayList<>(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				if (getValorConsultaCidade().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("estado")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}

			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void limparCidade() {
		getAproveitamentoDisciplinaVO().setCidadeVO(null);
	}

	public void limparCidadeAproveitamentoDisciplinaForaGrade() {
		getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().setCidade(null);
	}

	public void limparCidadeAproveitamentoDisciplinaGrupoOptativa() {
		getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().setCidade(null);
	}

	public void limparCidadeAproveitamentoDisciplina() {
		getGradeDisciplinaVO().getDisciplinasAproveitadasVO().setCidade(null);
	}

	/**
	 * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
	 */
	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
		// panel é utilizado para quatro locais de busca de cidade. Por isto, o
		// if abaixo
		if (getPanelAproveitamentoDisciplinaAberto()) {
			getGradeDisciplinaVO().getDisciplinasAproveitadasVO().setCidade(obj);
		} else {
			if (getPanelAproveitamentoDisciplinaAbertoForaGrade()) {
				getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().setCidade(obj);
			} else {
				if (getPanelAproveitamentoDisciplinaAbertoGrupoOptativa()) {
					getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().setCidade(obj);
				} else {
					getAproveitamentoDisciplinaVO().setCidadeVO(obj);
				}
			}
		}
		listaConsultaCidade.clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de
	 * Cidade <code>Cidade/code>.
	 */
	public List getTipoConsultaCidade() {
		List itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("estado", "Estado"));
		return itens;
	}

	public String novoMatriculado() {
		setMatriculado(Boolean.FALSE);
		setAproveitamentoDisciplinaVO(null);
		getAproveitamentoDisciplinaVO().setMatriculado(true);
		inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
		setDisciplinasAproveitadasVO(null);
		setListaSelectItemDisciplina(null);
		inicializarListasSelectItemTodosComboBox();
		verificarDisciplinaUsaNotaConceito();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemDisciplinaPeriodo();
		montarListaSelectItemPeriodoLetivo();
	}

	public void inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado() {
		try {
			getAproveitamentoDisciplinaVO().getResponsavelAutorizacao().setCodigo(getUsuarioLogado().getCodigo());
			getAproveitamentoDisciplinaVO().getResponsavelAutorizacao().setNome(getUsuarioLogado().getNome());
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>TransferenciaEntrada</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		try {
			inicializarListasSelectItemTodosComboBox();
			AproveitamentoDisciplinaVO obj = (AproveitamentoDisciplinaVO) context().getExternalContext().getRequestMap().get("aproveitamentoDisciplinaItens");
			obj = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setAproveitamentoEntreMatriculasJaExecutado(Boolean.FALSE);
			setAproveitamentoDisciplinaVO(obj);
			if (!obj.getAproveitamentoPrevisto()) {
				getAproveitamentoDisciplinaVO().setCurso(getAproveitamentoDisciplinaVO().getMatricula().getCurso());
				getAproveitamentoDisciplinaVO().setTurno(getAproveitamentoDisciplinaVO().getMatricula().getTurno());
				getAproveitamentoDisciplinaVO().setPessoa(getAproveitamentoDisciplinaVO().getMatricula().getAluno());
				getAproveitamentoDisciplinaVO().setMatriculaPeriodo(getAproveitamentoDisciplinaVO().getMatriculaPeriodo());
				getAproveitamentoDisciplinaVO().setGradeCurricular(getAproveitamentoDisciplinaVO().getMatriculaPeriodo().getGradeCurricular());
				getAproveitamentoDisciplinaVO().setPeridoLetivo(getAproveitamentoDisciplinaVO().getMatriculaPeriodo().getPeridoLetivo());
				getAproveitamentoDisciplinaVO().setUnidadeEnsinoCurso(getAproveitamentoDisciplinaVO().getMatriculaPeriodo().getUnidadeEnsinoCurso());
				this.getAproveitamentoDisciplinaVO().setUnidadeEnsino(getAproveitamentoDisciplinaVO().getMatricula().getUnidadeEnsino());
				obj.setNovoObj(Boolean.FALSE);
				setDisciplinasAproveitadasVO(null);
				verificarDisciplinaUsaNotaConceito();
				realizarMontagemPainelMatrizCurricular();
				realizarVerificacaoPermitirAproveitamentoDisciplinasOptativas();
			} else {
				if (Uteis.isAtributoPreenchido(obj.getCodigoRequerimento().getCodigo())) {
					inicializarDadosAproveitamentoPrevisto(obj.getCodigoRequerimento().getCodigo());
				}
				realizarMontagemPainelMatrizCurricular();
				montarListaSelectItemGradeCurricular();
				realizarVerificacaoPermitirAproveitamentoDisciplinasOptativas();
			}
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void montarListaSelectItemPeriodoLetivoComGrupoOptativas() {
		try {
			List objs = new ArrayList<>(0);
			for (PeriodoLetivoVO periodoLetivoVO : getAproveitamentoDisciplinaVO().getGradeCurricular().getPeriodoLetivosVOs()) {
				if (periodoLetivoVO.getControleOptativaGrupo()) {
					objs.add(new SelectItem(periodoLetivoVO.getCodigo(), periodoLetivoVO.getDescricao()));
				}
			}
			setListaSelectItemPeriodoLetivoComGrupoOptativas(objs);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	public void prepararDisciplinasGrupoOptativaPeriodoLetivoAproveitamento() {
		try {
			PeriodoLetivoVO obj = (PeriodoLetivoVO) context().getExternalContext().getRequestMap().get("periodoLetivoItens");
			setCodigoPeriodoLetivoGrupoOptativa(obj.getCodigo());
			montarListaSelectItemPeriodoLetivoComGrupoOptativas();
			if (obj.getControleOptativaGrupo() && !obj.getGradeCurricularGrupoOptativa().getCodigo().equals(0) && obj.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().isEmpty()) {
				setPeriodoLetivoGrupoOptativaVO(new PeriodoLetivoVO());
				GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO = getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(obj.getGradeCurricularGrupoOptativa().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
				obj.setGradeCurricularGrupoOptativa(gradeCurricularGrupoOptativaVO);
				setPeriodoLetivoGrupoOptativaVO(obj);
			} else if (obj.getControleOptativaGrupo() && !obj.getGradeCurricularGrupoOptativa().getCodigo().equals(0) && !obj.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().isEmpty()) {
				setPeriodoLetivoGrupoOptativaVO(obj);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void prepararDisciplinasGrupoOptativa() {
		try {
			setPeriodoLetivoGrupoOptativaVO(getAproveitamentoDisciplinaVO().getGradeCurricular().obterPeriodoLetivoPorCodigoPeriodoLetivo(getCodigoPeriodoLetivoGrupoOptativa()));
			if (getPeriodoLetivoGrupoOptativaVO().getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().isEmpty() && !getPeriodoLetivoGrupoOptativaVO().getGradeCurricularGrupoOptativa().getCodigo().equals(0)) {
				GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO = getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorChavePrimaria(getPeriodoLetivoGrupoOptativaVO().getGradeCurricularGrupoOptativa().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
				getPeriodoLetivoGrupoOptativaVO().setGradeCurricularGrupoOptativa(gradeCurricularGrupoOptativaVO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>TransferenciaEntrada</code>. Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>.
	 * Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
			getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarMontagemListaDisciplinasAproveitadas(getAproveitamentoDisciplinaVO(), getAproveitamentoDisciplinaVO().getInstituicao(), getAproveitamentoDisciplinaVO().getCidadeVO(), getUsuarioLogado());
			if (getAproveitamentoDisciplinaVO().getCodigo().equals(0)) {
				getFacadeFactory().getAproveitamentoDisciplinaFacade().incluir(aproveitamentoDisciplinaVO, true, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			} else {
				getFacadeFactory().getAproveitamentoDisciplinaFacade().alterar(aproveitamentoDisciplinaVO, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarRegistroAproveitamentoDisciplinasRegistrado(getAproveitamentoDisciplinaVO());
			realizarMontagemPainelMatrizCurricular();
			realizarVerificacaoPermitirAproveitamentoDisciplinasOptativas();
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		}
	}

	public void gravarSemExcecao() throws Exception {
		inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
		if (aproveitamentoDisciplinaVO.isNovoObj().booleanValue()) {
			getFacadeFactory().getAproveitamentoDisciplinaFacade().incluir(aproveitamentoDisciplinaVO, true, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
		} else {
			getFacadeFactory().getAproveitamentoDisciplinaFacade().alterar(aproveitamentoDisciplinaVO, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
		}
		setMensagemID("msg_dados_gravados");
	}

	public String gravarAproveitamentoPrevisto() {
		try {
			inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
			getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarMontagemListaDisciplinasAproveitadas(getAproveitamentoDisciplinaVO(), getAproveitamentoDisciplinaVO().getInstituicao(), getAproveitamentoDisciplinaVO().getCidadeVO(), getUsuarioLogado());
			if (getAproveitamentoDisciplinaVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getAproveitamentoDisciplinaFacade().incluir(aproveitamentoDisciplinaVO, true, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			} else {
				getFacadeFactory().getAproveitamentoDisciplinaFacade().alterar(aproveitamentoDisciplinaVO, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		}
	}

	public String inicializarMatriculaTransferenciaEntrada() throws Exception {
		try {
			aproveitamentoDisciplinaVO.validarDadosParaMatricula(aproveitamentoDisciplinaVO, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null));
			gravarSemExcecao();
			adicionarMatriculaPeriodo();
			setAproveitamentoDisciplinaVO(getFacadeFactory().getAproveitamentoDisciplinaFacade().consultarPorChavePrimaria(getAproveitamentoDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			executarMetodoControle(MatriculaDiretaControle.class.getSimpleName(), "navegarAbaDadosBasicos", (Object[]) null);
			return navegarPara(MatriculaDiretaControle.class.getSimpleName(), "matriculaApartirTranferenciaEntrada", "matricula", getAproveitamentoDisciplinaVO());
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		}
	}

	public String consultarMatriculado() {
		try {
			super.consultar();
			List objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorSituacao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoRequerimento")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorCodigoRequerimento(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("instituicaoOrigem")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorInstituicaoOrigem(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoJustificativa")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorTipoJustificativa(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("curso")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorNomeCurso(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("pessoa")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorNomeAluno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorRegistroAcademicoAluno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("turma")) {
				if (getControleConsulta().getValorConsulta().length() < 3) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_informeTresParametro"));
				}
				objs = getFacadeFactory().getAproveitamentoDisciplinaFacade().consultaRapidaPorTurma(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaMatriculado(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaCons.xhtml");
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>aproveitamentoDisciplinaVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getAproveitamentoDisciplinaFacade().excluir(aproveitamentoDisciplinaVO, getUsuarioLogado());
			setAproveitamentoDisciplinaVO(null);
			setDisciplinasAproveitadasVO(null);
			setConcessaoCreditoDisciplinaVO(null);
			setAproveitamentoEntreMatriculasJaExecutado(Boolean.FALSE);
			setAproveitamentoDisciplinasEntreMatriculasVO(null);
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		}
	}

	public String excluirMatriculado() {
		try {
			getFacadeFactory().getAproveitamentoDisciplinaFacade().excluir(aproveitamentoDisciplinaVO, getUsuarioLogado());
			setAproveitamentoDisciplinaVO(null);
			setDisciplinasAproveitadasVO(null);
			setAproveitamentoEntreMatriculasJaExecutado(Boolean.FALSE);
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaForm.xhtml");
		}
	}

	public void atualizarListaDisciplinasAproveitadas() throws Exception {
		getAproveitamentoDisciplinaVO().setDisciplinasAproveitadasVOs(getFacadeFactory().getDisciplinaAproveitadasFacade().consultarDisciplinasAproveitadass(getAproveitamentoDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
	}

	public void adicionarDisciplinasAproveitadas() throws Exception {
		try {
			if (!getAproveitamentoDisciplinaVO().getCodigo().equals(0)) {
				getDisciplinasAproveitadasVO().setAproveitamentoDisciplina(getAproveitamentoDisciplinaVO().getCodigo());
			}
			if (!getDisciplinasAproveitadasVO().getDisciplina().getCodigo().equals(0)) {
				getDisciplinasAproveitadasVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinasAproveitadasVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getDisciplinasAproveitadasVO().getFrequencia() == null || getDisciplinasAproveitadasVO().getFrequencia().equals(0.0)) {
				getDisciplinasAproveitadasVO().setFrequencia(100.00);
			}

			// if (getDisciplinasAproveitadasVO().getUtilizaNotaConceito() &&
			// getDisciplinasAproveitadasVO().getMediaFinalConceito().getCodigo()
			// > 0) {
			// getDisciplinasAproveitadasVO().setMediaFinalConceito(getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(getDisciplinasAproveitadasVO().getMediaFinalConceito().getCodigo()));
			// }

			// for (DisciplinasAproveitadasVO disciplinasAproveitadasVO :
			// getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasVOs())
			// {
			// if
			// (!disciplinasAproveitadasVO.getAno().equals(getDisciplinasAproveitadasVO().getAno())
			// ||
			// !disciplinasAproveitadasVO.getSemestre().equals(getDisciplinasAproveitadasVO().getSemestre()))
			// {
			// setMostrarPanelAviso(Boolean.TRUE);
			// return "";
			// }
			// }

			getAproveitamentoDisciplinaVO().adicionarObjDisciplinasAproveitadasVOs(getDisciplinasAproveitadasVO());
			setEditandoDisciplinasAproveitadas(Boolean.FALSE);
			String instituicao = getDisciplinasAproveitadasVO().getInstituicao();
			CidadeVO cidade = new CidadeVO();
			cidade.setCodigo(getDisciplinasAproveitadasVO().getCidade().getCodigo());
			cidade.setNome(getDisciplinasAproveitadasVO().getCidade().getNome());
			cidade.getEstado().setNome(getDisciplinasAproveitadasVO().getCidade().getEstado().getNome());
			cidade.getEstado().setSigla(getDisciplinasAproveitadasVO().getCidade().getEstado().getSigla());
			this.setDisciplinasAproveitadasVO(new DisciplinasAproveitadasVO());
			getDisciplinasAproveitadasVO().setInstituicao(instituicao);
			getDisciplinasAproveitadasVO().setCidade(cidade);
			getListaSelectItemDisciplina().clear();
			verificarDisciplinaUsaNotaConceito();
			setMensagemID("msg_dados_adicionados");
			setMostrarPanelAviso(Boolean.FALSE);
			// return "";
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			// return "";
		}
	}

	public void validarNotaForaGrade() {
		// com a entrada do ensino médio, as notas podem ser superior a 10. logo
		// este método teria que ser
		// melhorado para olhar para configucacao academica do aproveitamento em
		// registro.
		// try {
		// if
		// (getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().getNota()
		// > 10) {
		// getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().setNota(0.0);
		// throw new
		// Exception("A nota deve ser maior que 0 e menor ou igual a 10.");
		// } else {
		// setMensagemID("");
		// }
		// } catch (Exception e) {
		// setMensagemDetalhada("msg_erro", e.getMessage());
		// }
	}

	public void validarNota() {
		try {
			if (!getGradeDisciplinaVO().getDisciplinasAproveitadasVO().getNota().equals(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(getGradeDisciplinaVO().getDisciplinasAproveitadasVO().getNota(), getAproveitamentoDisciplinaVO().getMatricula().getCurso().getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()))) {
				getGradeDisciplinaVO().getDisciplinasAproveitadasVO().setNota(0.0);
				throw new Exception("A Nota Deve Conter o Número de Casas Decimais Presente na Configuração Acadêmica.Quantidade de Casas Decimais:" + getAproveitamentoDisciplinaVO().getMatricula().getCurso().getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula());
			}  
			if(getGradeDisciplinaVO().getDisciplinasAproveitadasVO().getNota() > 10) {
				getGradeDisciplinaVO().getDisciplinasAproveitadasVO().setNota(0.0);
				throw new Exception("A nota não pode ser maior que 10.");
			}else {
				setMensagemID("");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarNotaGrupoOptativa() {
		try {
			if (!getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().getNota().equals(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().getNota(), getAproveitamentoDisciplinaVO().getMatricula().getCurso().getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()))) {
				getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().setNota(0.0);
				throw new Exception("A Nota Deve Conter o Número de Casas Decimais Presente na Configuração Acadêmica.Quantidade de Casas Decimais:" + getAproveitamentoDisciplinaVO().getMatricula().getCurso().getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula());
			} 
			if(getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().getNota() > 10) {
				getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().setNota(0.0);
				throw new Exception("A nota não pode ser maior que 10.");
			}
			else {
				setMensagemID("");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarConcessaoCreditoDisciplina() {
		try {
			getFacadeFactory().getAproveitamentoDisciplinaFacade().adicionarConcessaoCreditoDisciplina(getAproveitamentoDisciplinaVO(), getConcessaoCreditoDisciplinaVO(), getUsuarioLogado());
			this.setConcessaoCreditoDisciplinaVO(null);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void adicionarConcessaoCargaHorariaDisciplina() {
		try {
			getFacadeFactory().getAproveitamentoDisciplinaFacade().adicionarConcessaoCargaHorariaDisciplina(getAproveitamentoDisciplinaVO(), getConcessaoCargaHorariaDisciplinaVO(), getUsuarioLogado());
			this.setConcessaoCargaHorariaDisciplinaVO(null);
			setMensagemID("msg_dados_adicionados");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public String getVerificarMostrarPanelAviso() {
		if (getIsMostrarPanelAviso().equals(Boolean.TRUE)) {
			return "RichFaces.$('panelAviso').show()";
		} else {
			return "";
		}
	}

	public String adicionarDisciplinaAproveitadaValidandoExistenciaDisciplinaNoHistorico() {
		try {
			if (!getAproveitamentoDisciplinaVO().getAproveitamentoPrevisto()) {
				if (!getDisciplinasAproveitadasVO().getDisciplina().getCodigo().equals(0)) {
					setMostrarPanelAvisoDisciplinaHistorico(getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarVerificacaoExistenciaCursandoDisciplinaHistoricoAluno(getAproveitamentoDisciplinaVO().getMatricula().getMatricula(), getDisciplinasAproveitadasVO().getDisciplina(), getUsuarioLogado()));
					if (!getMostrarPanelAvisoDisciplinaHistorico()) {
						adicionarDisciplinasAproveitadas();
					}
					setMensagemID("msg_dados_adicionados");
				} else {
					getDisciplinasAproveitadasVO().validarDados(getDisciplinasAproveitadasVO(), getAproveitamentoDisciplinaVO().getCurso().getPeriodicidade());
				}
			} else {
				adicionarDisciplinasAproveitadas();
			}
			return "";
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}

	}

	// public String adicionarDisciplinaAproveitadaExcluindoHistoricoCursado() {
	// try {
	// getDisciplinasAproveitadasVO().setExcluirHistoricoDisciplinaCursada(Boolean.TRUE);
	// adicionarDisciplinasAproveitadas();
	// setMostrarPanelAvisoDisciplinaHistorico(Boolean.FALSE);
	// setMensagemID("msg_dados_adicionados");
	// return "";
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// return "";
	// }
	// }
	public void fecharModalDisciplinaHistorico() {
		setMostrarPanelAvisoDisciplinaHistorico(Boolean.FALSE);
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>TransferenciaEntradaDisciplinasAproveitadas</code> para edição pelo
	 * usuário.
	 */
	public String editarDisciplinasAproveitadas() throws Exception {
		DisciplinasAproveitadasVO obj = (DisciplinasAproveitadasVO) context().getExternalContext().getRequestMap().get("disciplinasAproveitadas");
		setEditandoDisciplinasAproveitadas(Boolean.TRUE);
		setDisciplinasAproveitadasVO(obj);
		// verificarDisciplinaUsaNotaConceito(obj.getDisciplina().getCodigo());
		montarListaSelectItemDisciplinaPeriodo();
		if (getListaSelectItemPeriodoLetivoMatricula().isEmpty()) {
			montarListaSelectItemPeriodoLetivo();
		}
		return "";
	}

	private List<SelectItem> listaSelectItemNotaConceito;

	public void verificarDisciplinaUsaNotaConceito() {
		// verificarDisciplinaUsaNotaConceito(getDisciplinasAproveitadasVO().getDisciplina().getCodigo());
	}

	public List<SelectItem> getListaSelectItemNotaConceito() {
		if (listaSelectItemNotaConceito == null) {
			listaSelectItemNotaConceito = new ArrayList<SelectItem>();
		}
		return listaSelectItemNotaConceito;
	}

	public void setListaSelectItemNotaConceito(List<SelectItem> listaSelectItemNotaConceito) {
		this.listaSelectItemNotaConceito = listaSelectItemNotaConceito;
	}

	public void editarConcessoaCreditoDisciplina() {
		ConcessaoCreditoDisciplinaVO obj = (ConcessaoCreditoDisciplinaVO) context().getExternalContext().getRequestMap().get("concessaoCredito");
		setConcessaoCreditoDisciplinaVO(obj);
		if (getListaSelectItemPeriodoLetivoMatricula().isEmpty()) {
			montarListaSelectItemPeriodoLetivo();
		}
	}

	public void editarConcessoaCargaHorariaDisciplina() {
		ConcessaoCargaHorariaDisciplinaVO obj = (ConcessaoCargaHorariaDisciplinaVO) context().getExternalContext().getRequestMap().get("concessaoCargaHoraria");
		setConcessaoCargaHorariaDisciplinaVO(obj);
		if (getListaSelectItemPeriodoLetivoMatricula().isEmpty()) {
			montarListaSelectItemPeriodoLetivo();
		}
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>TransferenciaEntradaDisciplinasAproveitadas</code> do objeto
	 * <code>aproveitamentoDisciplinaVO</code> da classe
	 * <code>TransferenciaEntrada</code>
	 */
	public String removerDisciplinasAproveitadas() throws Exception {
		DisciplinasAproveitadasVO obj = (DisciplinasAproveitadasVO) context().getExternalContext().getRequestMap().get("disciplinasAproveitadas");
		getAproveitamentoDisciplinaVO().excluirObjDisciplinasAproveitadasVOs(obj.getDisciplina().getCodigo());
		getListaDisciplinasExcluidas().add(obj);
		setMensagemID("msg_dados_excluidos");
		return "";
	}

	public void removerConcessaoCreditoDisciplina() {
		ConcessaoCreditoDisciplinaVO obj = (ConcessaoCreditoDisciplinaVO) context().getExternalContext().getRequestMap().get("concessaoCredito");
		try {
			getFacadeFactory().getAproveitamentoDisciplinaFacade().excluirObjConcessaoCreditoDisciplinaVOs(getAproveitamentoDisciplinaVO(), obj.getDisciplinaVO().getCodigo());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void removerConcessaoCargaHorariaDisciplina() {
		ConcessaoCargaHorariaDisciplinaVO obj = (ConcessaoCargaHorariaDisciplinaVO) context().getExternalContext().getRequestMap().get("concessaoCargaHoraria");
		try {
			getFacadeFactory().getAproveitamentoDisciplinaFacade().excluirObjConcessaoCargaHorariaDisciplinaVOs(getAproveitamentoDisciplinaVO(), obj.getDisciplinaVO().getCodigo());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTipoJustificativaTransferenciaEntrada() throws Exception {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoJustificativaAlteracaoMatriculas = (Hashtable) Dominios.getTipoJustificativaAlteracaoMatricula();
		Enumeration keys = tipoJustificativaAlteracaoMatriculas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoJustificativaAlteracaoMatriculas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Requerimento</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarRequerimentoPorChavePrimaria() {
		try {
			Integer campoConsulta = getAproveitamentoDisciplinaVO().getCodigoRequerimento().getCodigo();
			RequerimentoVO requerimento = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(campoConsulta, "AD", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			getAproveitamentoDisciplinaVO().setCurso(requerimento.getMatricula().getCurso());
			getAproveitamentoDisciplinaVO().setTurno(requerimento.getMatricula().getTurno());
			getAproveitamentoDisciplinaVO().getCodigoRequerimento().setCodigo(requerimento.getCodigo());
			getAproveitamentoDisciplinaVO().setPessoa(requerimento.getPessoa());
			getAproveitamentoDisciplinaVO().setMatricula(requerimento.getMatricula());
			getAproveitamentoDisciplinaVO().setUnidadeEnsino(requerimento.getMatricula().getUnidadeEnsino());
			getAproveitamentoDisciplinaVO().setCodigoRequerimento(requerimento);
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(requerimento.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			getAproveitamentoDisciplinaVO().setGradeCurricular(matriculaPeriodoVO.getGradeCurricular());
			getAproveitamentoDisciplinaVO().setPeridoLetivo(matriculaPeriodoVO.getPeridoLetivo());
			getAproveitamentoDisciplinaVO().setUnidadeEnsinoCurso(matriculaPeriodoVO.getUnidadeEnsinoCurso());
			montarListaSelectItemGradeCurricular();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			getAproveitamentoDisciplinaVO().getCodigoRequerimento().setCodigo(0);
		}
	}

	public List getTipoConsultaComboRequisitante() {
		List itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		return itens;
	}

	public void consultarAluno() {
		try {
			getDataModeloAluno().setListaConsulta(new ArrayList<>());
			getDataModeloAluno().setTotalRegistrosEncontrados(0);
			getDataModeloAluno().setPage(0);
			getDataModeloAluno().setPaginaAtual(1);
			executarConsultaAluno();
		} catch (Exception e) {
			setListaConsultaAluno(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		return itens;
	}

	public void selecionarAluno() {
		try {
			setMensagemDetalhada("");
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			obj = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			this.getAproveitamentoDisciplinaVO().setMatricula(obj);
			this.getAproveitamentoDisciplinaVO().setUnidadeEnsino(obj.getUnidadeEnsino());
			this.getAproveitamentoDisciplinaVO().setCurso(obj.getCurso());
			this.getAproveitamentoDisciplinaVO().setTurno(obj.getTurno());
			setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaAnoSemestre(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			this.getAproveitamentoDisciplinaVO().setMatriculaPeriodo(matriculaPeriodoVO);
			getAproveitamentoDisciplinaVO().setGradeCurricular(matriculaPeriodoVO.getGradeCurricular());
			getAproveitamentoDisciplinaVO().setPeridoLetivo(matriculaPeriodoVO.getPeridoLetivo());
			getAproveitamentoDisciplinaVO().setUnidadeEnsinoCurso(matriculaPeriodoVO.getUnidadeEnsinoCurso());
			realizarMontagemPainelMatrizCurricular();
			this.getAproveitamentoDisciplinaVO().setPessoa(obj.getAluno());
			realizarVerificacaoPermitirAproveitamentoDisciplinasOptativas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarDisciplinaForaGrade() {
		try {
			List objs = new ArrayList<>(0);
			if (getCampoConsultaDisciplinaForaGrade().equals("nomeDisciplina")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplinaForaGrade(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaDisciplinaForaGrade(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboDisciplinaForaGrade() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomeDisciplina", "Nome da Disciplina"));
		return itens;
	}

	public void selecionarDisciplinaForaGrade() {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaForaGradeItens");
			getDisciplinasAproveitadasVO().setDisciplina(obj);
			getDisciplinasAproveitadasVO().setDisciplinaForaGrade(Boolean.TRUE);
			// verificarDisciplinaUsaNotaConceito(getDisciplinasAproveitadasVO().getDisciplina().getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosDisciplinaForaGrade() {
		getConcessaoCreditoDisciplinaVO().setDisciplinaVO(null);
		getConcessaoCreditoDisciplinaVO().setGradeDisciplinaVO(null);
		getConcessaoCargaHorariaDisciplinaVO().setDisciplinaVO(null);
		getConcessaoCargaHorariaDisciplinaVO().setGradeDisciplinaVO(null);
		getDisciplinasAproveitadasVO().setDisciplina(null);
		getDisciplinasAproveitadasVO().setGradeDisciplinaVO(null);
		verificarDisciplinaUsaNotaConceito();
	}

	public void limparDadosPesssoa() {
		getAproveitamentoDisciplinaVO().setPessoa(null);
		verificarDisciplinaUsaNotaConceito();
	}

	public String getCadastrarNovoAluno() throws Exception {
		try {
			navegarPara(AlunoControle.class.getSimpleName(), "novo", "");
			executarMetodoControle(AlunoControle.class.getSimpleName(), "permitirIniciarInscricao");
			return "abrirPopup('alunoForm.xhtml', 'alunoForm', 780, 585);";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void selecionarRequerimento() {
		RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
		MatriculaPeriodoVO matriculaPeriodoVO = new MatriculaPeriodoVO();
		try {
			obj = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			getAproveitamentoDisciplinaVO().setCurso(obj.getMatricula().getCurso());
			getAproveitamentoDisciplinaVO().setTurno(obj.getMatricula().getTurno());
			getAproveitamentoDisciplinaVO().getCodigoRequerimento().setCodigo(obj.getCodigo());
			getAproveitamentoDisciplinaVO().setPessoa(obj.getPessoa());
			getAproveitamentoDisciplinaVO().setMatricula(obj.getMatricula());
			getAproveitamentoDisciplinaVO().setUnidadeEnsino(obj.getMatricula().getUnidadeEnsino());
			getAproveitamentoDisciplinaVO().setCodigoRequerimento(obj);
			matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(obj.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			getAproveitamentoDisciplinaVO().setGradeCurricular(matriculaPeriodoVO.getGradeCurricular());
			getAproveitamentoDisciplinaVO().setPeridoLetivo(matriculaPeriodoVO.getPeridoLetivo());
			getAproveitamentoDisciplinaVO().setUnidadeEnsinoCurso(matriculaPeriodoVO.getUnidadeEnsinoCurso());
			montarListaSelectItemPeriodoLetivo();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			getAproveitamentoDisciplinaVO().getCodigoRequerimento().setCodigo(0);
		} finally {
			obj = null;
			matriculaPeriodoVO = null;
		}
	}

	public void prepararMapaEquivalenciaParaVisualizacao(MapaEquivalenciaDisciplinaVO mapaVisualizar, GradeDisciplinaVO gradeDisciplina, Integer numeroAgrupamentoEquivalenciaDisciplina) throws Exception {
		this.getAproveitamentoDisciplinaVO().getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().atualizarSituacaoMapaEquivalenciaDisciplinaAluno(mapaVisualizar, numeroAgrupamentoEquivalenciaDisciplina, null);
		getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarMontagemPainelMapaEquivalenciaDisciplinasAproveitadas(getAproveitamentoDisciplinaVO(), mapaVisualizar, getUsuarioLogado());
		setMapaEquivalenciaDisciplinaVisualizar(mapaVisualizar);
	}

	public void selecionarMapaEquivalenciaParaVisualizacao() {
		try {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
			MapaEquivalenciaDisciplinaVO mapaVisualizar = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(obj.getHistoricoAtualAluno().getMapaEquivalenciaDisciplina().getCodigo(), NivelMontarDados.TODOS);
			prepararMapaEquivalenciaParaVisualizacao(mapaVisualizar, obj, obj.getHistoricoAtualAluno().getNumeroAgrupamentoEquivalenciaDisciplina());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void prepararMapaEquivalenciaParaVisualizacaoGrupoOptativa(MapaEquivalenciaDisciplinaVO mapaVisualizar, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativa, Integer numeroAgrupamentoEquivalenciaDisciplina) throws Exception {
		this.getAproveitamentoDisciplinaVO().getMatricula().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().atualizarSituacaoMapaEquivalenciaDisciplinaAluno(mapaVisualizar, numeroAgrupamentoEquivalenciaDisciplina, null);
		getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarMontagemPainelMapaEquivalenciaDisciplinasAproveitadas(getAproveitamentoDisciplinaVO(), mapaVisualizar, getUsuarioLogado());
		setMapaEquivalenciaDisciplinaVisualizar(mapaVisualizar);
	}
	
	public void selecionarMapaEquivalenciaParaVisualizacaoGrupoOptativa() {
		try {
			GradeCurricularGrupoOptativaDisciplinaVO obj = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("grupoOptativaDisciplinaItens");
			MapaEquivalenciaDisciplinaVO mapaVisualizar = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorChavePrimaria(obj.getHistoricoAtualAluno().getMapaEquivalenciaDisciplina().getCodigo(), NivelMontarDados.TODOS);
			prepararMapaEquivalenciaParaVisualizacaoGrupoOptativa(mapaVisualizar, obj, obj.getHistoricoAtualAluno().getNumeroAgrupamentoEquivalenciaDisciplina());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	
	public void inicializarMatriculaComHistoricoAluno(Boolean forcarNovoCarregamentoDados) throws Exception {
		if ((getAproveitamentoDisciplinaVO().getMatricula().getMatriculaComHistoricoAlunoVO().getIsInicializado()) && (!forcarNovoCarregamentoDados)) {
			return;
		}

		// CARREGANDO DADOS DA CFG DO CURSO PARA SER UTILIZADO POSTERIORMENTE
		if (!this.getAproveitamentoDisciplinaVO().getCurso().getConfiguracaoAcademico().getCodigo().equals(0)) {
			ConfiguracaoAcademicoVO cfgCurso = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(this.getAproveitamentoDisciplinaVO().getCurso().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado());
			this.getAproveitamentoDisciplinaVO().getCurso().setConfiguracaoAcademico(cfgCurso);
			getAproveitamentoDisciplinaVO().getMatricula().getCurso().setConfiguracaoAcademico(cfgCurso);
		}

		if (this.getAproveitamentoDisciplinaVO().getMatricula().getMatricula().equals("")) {
			if (getAproveitamentoDisciplinaVO().getGradeCurricular().getCodigo().equals(0)) {
				throw new Exception("Não é possível iniciar o Aproveitamento de Disciplinas sem que uma matriz curricular seja informada");
			}
			getAproveitamentoDisciplinaVO().getMatricula().setGradeCurricularAtual(getAproveitamentoDisciplinaVO().getGradeCurricular());
		}
		if (this.getAproveitamentoDisciplinaVO().getAproveitamentoPrevisto()) {
			List<HistoricoVO> historicosPrevitosAproveitamento = getFacadeFactory().getAproveitamentoDisciplinaFacade().gerarHistoricosPrevistosDisciplinasAproveitadas(getAproveitamentoDisciplinaVO(), getUsuarioLogado());
			getAproveitamentoDisciplinaVO().getMatricula().setHistoricosAproveitamentoDisciplinaPrevisto(historicosPrevitosAproveitamento);
		}
		MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(getAproveitamentoDisciplinaVO().getMatricula(), getAproveitamentoDisciplinaVO().getMatricula().getGradeCurricularAtual().getCodigo(), false, getAproveitamentoDisciplinaVO().getMatricula().getCurso().getConfiguracaoAcademico(), getUsuarioLogado());
		getAproveitamentoDisciplinaVO().getMatricula().setMatriculaComHistoricoAlunoVO(matriculaComHistoricoAlunoVO);
		getAproveitamentoDisciplinaVO().setGradeCurricular(matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Matricula</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() {
		String campoConsulta = "";
		MatriculaVO matriculaVO = new MatriculaVO();
		try {
			campoConsulta = getAproveitamentoDisciplinaVO().getMatricula().getMatricula();
			matriculaVO = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			if (!matriculaVO.getMatricula().trim().isEmpty()) {
				getAproveitamentoDisciplinaVO().setMatricula(matriculaVO);
				getFacadeFactory().getCursoFacade().carregarDados(matriculaVO.getCurso(), NivelMontarDados.BASICO, getUsuarioLogado());
				getAproveitamentoDisciplinaVO().setCurso(matriculaVO.getCurso());
				getAproveitamentoDisciplinaVO().setTurno(matriculaVO.getTurno());
				getAproveitamentoDisciplinaVO().setPessoa(matriculaVO.getAluno());
				setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaAnoSemestre(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
				getAproveitamentoDisciplinaVO().setGradeCurricular(matriculaPeriodoVO.getGradeCurricular());
				getAproveitamentoDisciplinaVO().setPeridoLetivo(matriculaPeriodoVO.getPeridoLetivo());
				getAproveitamentoDisciplinaVO().setUnidadeEnsinoCurso(matriculaPeriodoVO.getUnidadeEnsinoCurso());
				getAproveitamentoDisciplinaVO().setUnidadeEnsino(matriculaVO.getUnidadeEnsino());
				getAproveitamentoDisciplinaVO().setMatriculaPeriodo(matriculaPeriodoVO);
				realizarMontagemPainelMatrizCurricular();
				realizarVerificacaoPermitirAproveitamentoDisciplinasOptativas();
				setMensagemID("msg_dados_consultados");
			} else {
				novoMatriculado();
				setMensagemDetalhada("msg_erro", UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));				
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
			getAproveitamentoDisciplinaVO().getMatricula().setMatricula("");
			getAproveitamentoDisciplinaVO().getMatriculaPeriodo().setCodigo(0);
		} finally {
			campoConsulta = null;

		}
	}

	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(this);
		setMatriculado(Boolean.FALSE);
		setAproveitamentoDisciplinaVO(null);
		setAproveitamentoEntreMatriculasJaExecutado(Boolean.FALSE);
		setAproveitamentoDisciplinasEntreMatriculasVO(null);		
		setAbaApresentar("dadosGerais");
	}

	public void adicionarMatriculaPeriodo() throws Exception {
		String ano = "";
		String semestre = "";
		if (getAproveitamentoDisciplinaVO().getTipo().equals("AP")) {
			ano = getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasVOs().get(0).getAno();
			semestre = getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasVOs().get(0).getSemestre();
		} else if (getAproveitamentoDisciplinaVO().getTipo().equals("CO")) {
			ano = getAproveitamentoDisciplinaVO().getConcessaoCreditoDisciplinaVOs().get(0).getAno();
			semestre = getAproveitamentoDisciplinaVO().getConcessaoCreditoDisciplinaVOs().get(0).getSemestre();
		} else if (getAproveitamentoDisciplinaVO().getTipo().equals("CH")) {
			ano = getAproveitamentoDisciplinaVO().getConcessaoCargaHorariaDisciplinaVOs().get(0).getAno();
			semestre = getAproveitamentoDisciplinaVO().getConcessaoCargaHorariaDisciplinaVOs().get(0).getSemestre();
		}
		String matricula = getAproveitamentoDisciplinaVO().getMatricula().getMatricula();
		CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(matricula, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		if (cursoVO.getPeriodicidade().equals("IN")) {
			semestre = null;
			ano = null;
		}
		if (cursoVO.getPeriodicidade().equals("AN")) {
			semestre = null;			
		}
		MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatriculaSemestreAno(matricula, semestre, ano, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
		try {
			getAproveitamentoDisciplinaVO().setMatriculaPeriodo(matriculaPeriodoVO);
		} finally {
			ano = null;
			semestre = null;
			matricula = null;
			matriculaPeriodoVO = null;
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("pessoa", "Aluno"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("matriculaMatricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("codigoRequerimento", "Código Requerimento"));
		return itens;
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("aproveitamentoDisciplinaCons.xhtml");
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemDisciplinaPeriodo(Integer codigo) throws Exception {
		PeriodoLetivoVO periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		List<GradeDisciplinaVO> listGradeDisciplinaVOs = periodoLetivoVO.getGradeDisciplinaVOs();
		Iterator<GradeDisciplinaVO> i = listGradeDisciplinaVOs.iterator();
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) i.next();
			objs.add(new SelectItem(obj.getDisciplina().getCodigo(), obj.getDisciplina().getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List<SelectItem>) objs, ordenador);
		setListaSelectItemDisciplina(objs);
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemGradeCurricular(Integer codigo) throws Exception {
		List<GradeCurricularVO> listGradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurriculars(getAproveitamentoDisciplinaVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		Iterator<GradeCurricularVO> i = listGradeCurricularVOs.iterator();
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			GradeCurricularVO obj = (GradeCurricularVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List<SelectItem>) objs, ordenador);
		setListaSelectItemGradeCurricular(objs);
	}

	public void montarListaSelectItemDisciplinaPeriodo() {
		try {
			if (Uteis.isAtributoPreenchido(getDisciplinasAproveitadasVO().getPeriodoLetivoOrigemDisciplina())) {
				montarListaSelectItemDisciplinaPeriodo(getDisciplinasAproveitadasVO().getPeriodoLetivoOrigemDisciplina());
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemGradeCurricular() {
		try {
			montarListaSelectItemGradeCurricular(0);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosQtdeCreditoDisciplina() {
		try {
			if (getConcessaoCreditoDisciplinaVO().getDisciplinaVO().getCodigo() != 0) {
				getConcessaoCreditoDisciplinaVO().setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getConcessaoCreditoDisciplinaVO().getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
				getConcessaoCreditoDisciplinaVO().setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorMatriculaDisciplina(getAproveitamentoDisciplinaVO().getMatricula().getMatricula(), getConcessaoCreditoDisciplinaVO().getDisciplinaVO().getCodigo(), getUsuarioLogado()));
				getConcessaoCreditoDisciplinaVO().setQtdeCreditoConcedido(0);
				getConcessaoCreditoDisciplinaVO().setDescricaoComplementacaoCH("");
			} else {
				getConcessaoCreditoDisciplinaVO().getGradeDisciplinaVO().setNrCreditos(0);
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void inicializarDadosQtdeCargaHorariaDisciplina() {
		try {
			if (getConcessaoCargaHorariaDisciplinaVO().getDisciplinaVO().getCodigo() != 0) {
				getConcessaoCargaHorariaDisciplinaVO().setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getConcessaoCargaHorariaDisciplinaVO().getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
				getConcessaoCargaHorariaDisciplinaVO().setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorMatriculaDisciplina(getAproveitamentoDisciplinaVO().getMatricula().getMatricula(), getConcessaoCargaHorariaDisciplinaVO().getDisciplinaVO().getCodigo(), getUsuarioLogado()));
				getConcessaoCargaHorariaDisciplinaVO().setQtdeCargaHorariaConcedido(0);
				getConcessaoCargaHorariaDisciplinaVO().setDescricaoComplementacaoCH("");
			} else {
				getConcessaoCargaHorariaDisciplinaVO().getGradeDisciplinaVO().setCargaHoraria(0);
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarDisciplinaPorGradeCurricular() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorGradeCurricular(getAproveitamentoDisciplinaVO().getGradeCurricular().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	public DisciplinaVO consultarDisciplinaPorCodigo(Integer Prm) throws Exception {
		DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(Prm, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return disciplina;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PeriodoLetivoMatricula</code>.
	 */
	public void montarListaSelectItemPeriodoLetivo(Integer prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (getAproveitamentoDisciplinaVO().getGradeCurricular().getCodigo().equals(0)) {
				setListaSelectItemPeriodoLetivoMatricula(null);
				return;
			}
			resultadoConsulta = consultarPeriodoLetivoPorSigla(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
				if (obj.getPeriodoLetivo().intValue() == 1) {
					getAproveitamentoDisciplinaVO().setPeridoLetivo(obj);
				}
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemPeriodoLetivoMatricula(objs);
			montarListaSelectItemDisciplinaPeriodo();
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void realizarMontagemPainelMatrizCurricular() {
		try {
			inicializarMatriculaComHistoricoAluno(true);
			getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarMontagemPainelMatrizCurricularComDisciplinasAproveitadas(getAproveitamentoDisciplinaVO(), getUsuarioLogado());
			setAbaApresentar("dadosAproveitamento"); // "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PeriodoLetivoMatricula</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não
	 * recebe parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemPeriodoLetivo() {
		try {
			montarListaSelectItemPeriodoLetivo(getAproveitamentoDisciplinaVO().getGradeCurricular().getCodigo());
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void editarDisciplinaAproveitadaGradeDisciplina() {
		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
		setGradeDisciplinaVO(obj);
		setPanelAproveitamentoDisciplinaAberto(Boolean.TRUE);
	}

	public void editarDisciplinaAproveitadaGradeCurricularGrupoOptativa() {
		GradeCurricularGrupoOptativaDisciplinaVO obj = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("grupoOptativaDisciplinaItens");
		setGradeCurricularGrupoOptativaDisciplinaVO(obj);
		setPanelAproveitamentoDisciplinaAbertoGrupoOptativa(Boolean.TRUE);
	}

	public void editarDisciplinaAproveitadaForaGrade() {
		MapaEquivalenciaDisciplinaCursadaVO obj = (MapaEquivalenciaDisciplinaCursadaVO) context().getExternalContext().getRequestMap().get("disciplinaCursarMapaItens");
		setMapaEquivalenciaDisciplinaCursadaVO(obj);
		setPanelAproveitamentoDisciplinaAbertoForaGrade(Boolean.TRUE);
	}

	public void inicializarDadosDisciplinaAproveitadaTemporaria() {
		try {
			setRetornoRichPanel("");
			// é importante inicializar a carga horaria para que a validação
			// possa ser feita da forma adequada.
			getGradeDisciplinaVO().getDisciplinasAproveitadasVO().setCargaHoraria(getGradeDisciplinaVO().getCargaHoraria());
			DisciplinasAproveitadasVO.validarDados(getGradeDisciplinaVO().getDisciplinasAproveitadasVO(), this.getAproveitamentoDisciplinaVO().getCurso().getPeriodicidade(), this.getAproveitamentoDisciplinaVO().getCurso().getConfiguracaoAcademico().getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
			if (!this.getAproveitamentoDisciplinaVO().getMatricula().getCurso().getNivelEducacionalPosGraduacao()) {
				DisciplinasAproveitadasVO.validarAnoSemestreAproveitamentoDisciplina(getGradeDisciplinaVO().getDisciplinasAproveitadasVO(), getGradeDisciplinaVO().getHistoricoAtualAluno());
			}
			getGradeDisciplinaVO().setSelecionadoAproveitamento(Boolean.TRUE);
			setMensagemID("msg_dados_adicionados");
			setRetornoRichPanel("RichFaces.$('panelDadosAproveitamento').hide()");
			setPanelAproveitamentoDisciplinaAberto(Boolean.FALSE);
		} catch (Exception e) {
			setRetornoRichPanel("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inicializarDadosDisciplinaAproveitadaGrupoOptativa() {
		try {
			setRetornoRichPanel("");
			// é importante inicializar a carga horaria para que a validação
			// possa ser feita da forma adequada.
			getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().setCargaHoraria(getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria());
			DisciplinasAproveitadasVO.validarDados(getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO(), this.getAproveitamentoDisciplinaVO().getCurso().getPeriodicidade(), this.getAproveitamentoDisciplinaVO().getCurso().getConfiguracaoAcademico().getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
			DisciplinasAproveitadasVO.validarAnoSemestreAproveitamentoDisciplina(getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO(), getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno());
			getGradeCurricularGrupoOptativaDisciplinaVO().setSelecionadoAproveitamento(Boolean.TRUE);
			setMensagemID("msg_dados_adicionados");
			setRetornoRichPanel("RichFaces.$('panelDadosAproveitamentoGrupoOptativa').hide()");
			setPanelAproveitamentoDisciplinaAbertoGrupoOptativa(Boolean.FALSE);
		} catch (Exception e) {
			setRetornoRichPanel("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarMarcacaoAproveitamento() {
		try {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
			obj.getDisciplinasAproveitadasVO().setDisciplina(obj.getDisciplina());
			obj.getDisciplinasAproveitadasVO().setCargaHorariaCursada(obj.getCargaHoraria());
			obj.getDisciplinasAproveitadasVO().setQtdeCreditoConcedido(obj.getNrCreditos());
			obj.getDisciplinasAproveitadasVO().setInstituicao(getAproveitamentoDisciplinaVO().getInstituicao());
			obj.getDisciplinasAproveitadasVO().setCidade(getAproveitamentoDisciplinaVO().getCidadeVO());
			if (getDisciplinasAproveitadasTemporariaVO() != null) {
				obj.getDisciplinasAproveitadasVO().setTipo(getDisciplinasAproveitadasTemporariaVO().getTipo());
				obj.getDisciplinasAproveitadasVO().setAproveitamentoPorIsencao(getDisciplinasAproveitadasTemporariaVO().getAproveitamentoPorIsencao());
			}
			setGradeDisciplinaVO(obj);
			executarVerificacaoJaExisteHistoricoVinculadoDisciplina(obj.getHistoricoAtualAluno());
			setPanelAproveitamentoDisciplinaAberto(Boolean.TRUE);
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void verificarERemoverOutrosMapaEquivalenciaReferenciandoMesmaDisciplinaCursada(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) {
		MapaEquivalenciaDisciplinaVO mapaBaseJaRemovido = mapaEquivalenciaDisciplinaCursadaVO.getMapaEquivalenciaDisciplina();
		for (MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVerificar : this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasForaDaGradeMapaEquivalencia()) {
			if (mapaEquivalenciaDisciplinaCursadaVerificar.getMapaEquivalenciaDisciplina().getCodigo().equals(mapaBaseJaRemovido.getCodigo())) {
				// se entrarmos aqui é por que encontramos outro mapa diferente do mapaEquivalenciaDisciplinaCursadaVO que foi removido.
				// agora temos que verificar se este outro mapa contem a mesma disciplina removida pelo usuario
				if (mapaEquivalenciaDisciplinaCursadaVerificar.getDisciplinaVO().getCodigo().equals(mapaEquivalenciaDisciplinaCursadaVO.getDisciplinaVO().getCodigo())) {
					// se entrarmos aqui é por que encontramos a segunda referencia a mesma.
				}
			}
		}
	}
	
	public void realizarMarcacaoAproveitamentoForaGrade() {
		try {
			MapaEquivalenciaDisciplinaCursadaVO obj = (MapaEquivalenciaDisciplinaCursadaVO) context().getExternalContext().getRequestMap().get("disciplinaCursarMapaItens");
			obj.getDisciplinasAproveitadasVO().setDisciplina(obj.getDisciplinaVO());
			obj.getDisciplinasAproveitadasVO().setCargaHorariaCursada(obj.getCargaHoraria());
			obj.getDisciplinasAproveitadasVO().setQtdeCreditoConcedido(obj.getNumeroCreditos());
			obj.getDisciplinasAproveitadasVO().setInstituicao(getAproveitamentoDisciplinaVO().getInstituicao());
			obj.getDisciplinasAproveitadasVO().setCidade(getAproveitamentoDisciplinaVO().getCidadeVO());
			if (getDisciplinasAproveitadasTemporariaVO() != null) {
				obj.getDisciplinasAproveitadasVO().setTipo(getDisciplinasAproveitadasTemporariaVO().getTipo());
				obj.getDisciplinasAproveitadasVO().setAproveitamentoPorIsencao(getDisciplinasAproveitadasTemporariaVO().getAproveitamentoPorIsencao());
			}
			setMapaEquivalenciaDisciplinaCursadaVO(obj);
			executarVerificacaoJaExisteHistoricoVinculadoDisciplina(obj.getHistorico());
			setPanelAproveitamentoDisciplinaAbertoForaGrade(Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarDesmarcacaoAproveitamentoForaGrade() {
		MapaEquivalenciaDisciplinaCursadaVO obj = (MapaEquivalenciaDisciplinaCursadaVO) context().getExternalContext().getRequestMap().get("disciplinaCursarMapaItens");
		setMapaEquivalenciaDisciplinaCursadaVO(obj);
		removerMapaEquivalenciaCursadoPersistencia();
		removerDisciplinaAproveitaMapaEquivalenciaPersistencia(obj);
		//verificarERemoverOutrosMapaEquivalenciaReferenciandoMesmaDisciplinaCursada(obj);
		obj.setSelecionadoAproveitamento(Boolean.FALSE);
		obj.setDisciplinasAproveitadasVO(null);
		obj.getHistorico().setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
	}

	public void realizarDesmarcacaoAproveitamento() {
		GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
		obj.setSelecionadoAproveitamento(Boolean.FALSE);
		obj.setDisciplinasAproveitadasVO(null);
		obj.getHistoricoAtualAluno().setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
		setGradeDisciplinaVO(obj);
	}

	public Boolean getApresentarAproveitamentoGrupoOptativa() {
		if (getGradeCurricularGrupoOptativaDisciplinaVO().getSelecionadoAproveitamento()) {
			if (getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().getPeriodoletivoGrupoOptativaVO().getCodigo().equals(getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno().getPeriodoLetivoMatrizCurricular().getCodigo())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public void realizarMarcacaoAproveitamentoGrupoOptativa() {
		try {
			GradeCurricularGrupoOptativaDisciplinaVO obj = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("grupoOptativaDisciplinaItens");
			getFacadeFactory().getAproveitamentoDisciplinaFacade().veriricarJaExisteAproveitamentoDisciplinaGrupoOptativa(this.getAproveitamentoDisciplinaVO(), obj);
			setGradeCurricularGrupoOptativaDisciplinaVO(new GradeCurricularGrupoOptativaDisciplinaVO());
			obj.getDisciplinasAproveitadasVO().setDisciplina(obj.getDisciplina());
			obj.getDisciplinasAproveitadasVO().setCargaHorariaCursada(obj.getCargaHoraria());
			obj.getDisciplinasAproveitadasVO().setQtdeCreditoConcedido(obj.getNrCreditos());
			obj.getDisciplinasAproveitadasVO().setInstituicao(getAproveitamentoDisciplinaVO().getInstituicao());
			obj.getDisciplinasAproveitadasVO().setCidade(getAproveitamentoDisciplinaVO().getCidadeVO());
			PeriodoLetivoVO periodoAtivoGrupoOptativa = this.getAproveitamentoDisciplinaVO().getGradeCurricular().consultarObjPeriodoLetivoVOPorCodigo(this.getCodigoPeriodoLetivoGrupoOptativa());
			obj.getDisciplinasAproveitadasVO().setPeriodoletivoGrupoOptativaVO(periodoAtivoGrupoOptativa);
			if (getDisciplinasAproveitadasTemporariaVO() != null) {
				obj.getDisciplinasAproveitadasVO().setTipo(getDisciplinasAproveitadasTemporariaVO().getTipo());
				obj.getDisciplinasAproveitadasVO().setAproveitamentoPorIsencao(getDisciplinasAproveitadasTemporariaVO().getAproveitamentoPorIsencao());
			}
			setGradeCurricularGrupoOptativaDisciplinaVO(obj);
			setMensagemID("msg_dados_editar");
			executarVerificacaoJaExisteHistoricoVinculadoDisciplina(obj.getHistoricoAtualAluno());
			setPanelAproveitamentoDisciplinaAbertoGrupoOptativa(Boolean.TRUE);
		} catch (Exception e) {
			setGradeCurricularGrupoOptativaDisciplinaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void voltarEdicaoAproveitamento() {
		setMensagemID("msg_dados_editar");
	}

	public void realizarDesmarcacaoAproveitamentoGrupoOptativa() {
		GradeCurricularGrupoOptativaDisciplinaVO obj = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("grupoOptativaDisciplinaItens");
		obj.setSelecionadoAproveitamento(Boolean.FALSE);
		obj.setDisciplinasAproveitadasVO(null);
		obj.getHistoricoAtualAluno().setSituacao(SituacaoHistorico.NAO_CURSADA.getValor());
		setGradeCurricularGrupoOptativaDisciplinaVO(obj);
	}

	/**
	 * Como o usuário clicou para remover o aproveitamento, entao removemos o
	 * mesmo da lista de aproveitamentos a serem persistidos.
	 */
	public void removerMapaEquivalenciaCursadoPersistencia() {
		if (this.getMapaEquivalenciaDisciplinaCursadaVO().getSelecionadoAproveitamento()) {
			int i = 0;
			while (i < this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().size()) {
				MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaCursadaJaRegistrada = this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().get(i);
				Integer codDisciplinaAtualizada = this.getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinaVO().getCodigo();
				Integer cargaHorariaDisciplinaAtualizada = this.getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().getCargaHoraria();
				if ((mapaEquivalenciaCursadaJaRegistrada.getDisciplinaVO().getCodigo().equals(codDisciplinaAtualizada)) && (mapaEquivalenciaCursadaJaRegistrada.getDisciplinasAproveitadasVO().getCargaHoraria().equals(cargaHorariaDisciplinaAtualizada))) {
					// Se entrar aqui é por que trata-se de um aproveitamento da
					// mesma disciplina. logo temos que verificar se o
					// aproveitamento
					// é de mesmo código, se for não temos que fazer nada pois o
					// mesmo já foi atualizado diretamente no mapa.
					this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().remove(i);
					return;
				}
				i++;
			}
		}
	}

	public void removerDisciplinaAproveitaMapaEquivalenciaPersistencia(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVORemover) {
		int i = 0;
		while (i < this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasVOs().size()) {
			DisciplinasAproveitadasVO disciplinasAproveitadasVO = (DisciplinasAproveitadasVO)this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasVOs().get(i);
			if ((disciplinasAproveitadasVO.getDisciplina().getCodigo().equals(mapaEquivalenciaDisciplinaCursadaVORemover.getDisciplinaVO().getCodigo())) &&
			    (disciplinasAproveitadasVO.getCargaHoraria().equals(mapaEquivalenciaDisciplinaCursadaVORemover.getCargaHoraria()))) {
				this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasVOs().remove(i);
			}
			i++;
		}
		mapaEquivalenciaDisciplinaCursadaVORemover.setDisciplinasAproveitadasVO(null);
	}

	public void fecharPainelMapaEquivalencia() {
	}

	public void registrarMapaEquivalenciaCursadoPersistencia() {
		if (this.getMapaEquivalenciaDisciplinaCursadaVO().getSelecionadoAproveitamento()) {
			this.getMapaEquivalenciaDisciplinaCursadaVO().setMapaEquivalenciaDisciplina(mapaEquivalenciaDisciplinaVisualizar);
			int i = 0;
			while (i < this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().size()) {
				MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaCursadaJaRegistrada = this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().get(i);
				Integer codDisciplinaAtualizada = this.getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinaVO().getCodigo();
				Integer cargaHorariaDisciplinaAtualizada = this.getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().getCargaHoraria();
				if ((mapaEquivalenciaCursadaJaRegistrada.getDisciplinaVO().getCodigo().equals(codDisciplinaAtualizada)) && (mapaEquivalenciaCursadaJaRegistrada.getDisciplinasAproveitadasVO().getCargaHoraria().equals(cargaHorariaDisciplinaAtualizada))) {
					// Se entrar aqui é por que trata-se de um aproveitamento da
					// mesma disciplina. logo temos que verificar se o
					// aproveitamento
					// é de mesmo código, se for não temos que fazer nada pois o
					// mesmo já foi atualizado diretamente no mapa.
					this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().set(i, this.getMapaEquivalenciaDisciplinaCursadaVO());
					return;
				}
				i++;
			}
			// caso já nao exista na lista (o que seria identificado pelo codigo
			// acima, entao o mesmo é adicionado para ser posterior
			// persistido.
			this.getAproveitamentoDisciplinaVO().getDisciplinasAproveitadasForaDaGradeMapaEquivalencia().add(this.getMapaEquivalenciaDisciplinaCursadaVO());
		}
	}

	public void fecharPanelsAproveitamento() {
		setPanelAproveitamentoDisciplinaAbertoForaGrade(Boolean.FALSE);
		setPanelAproveitamentoDisciplinaAberto(Boolean.FALSE);
		setPanelAproveitamentoDisciplinaAbertoGrupoOptativa(Boolean.FALSE);
	}

	public void inicializarAproveitamentoPainelMapaEquivalencia() {
		try {
			setRetornoRichPanel("");
			// é importante inicializar a carga horaria para que a validação
			// possa ser feita da forma adequada.
			getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().setDisciplinaForaGrade(Boolean.TRUE);
			getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().setCargaHoraria(getMapaEquivalenciaDisciplinaCursadaVO().getCargaHoraria());
			DisciplinasAproveitadasVO.validarDados(getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO(), this.getAproveitamentoDisciplinaVO().getCurso().getPeriodicidade(), this.getAproveitamentoDisciplinaVO().getCurso().getConfiguracaoAcademico().getPercMinimoCargaHorariaDisciplinaParaAproveitamento());
			DisciplinasAproveitadasVO.validarAnoSemestreAproveitamentoDisciplina(getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO(), getMapaEquivalenciaDisciplinaCursadaVO().getHistorico());
			getMapaEquivalenciaDisciplinaCursadaVO().setSelecionadoAproveitamento(Boolean.TRUE);
			getAproveitamentoDisciplinaVO().adicionarDisciplinasAproveitadasForaDaGradeMapaEquivalencia(getMapaEquivalenciaDisciplinaCursadaVO());
			setMensagemID("msg_dados_adicionados");
			setRetornoRichPanel("RichFaces.$('panelDadosAproveitamentoForaGrade').hide()");
			setPanelAproveitamentoDisciplinaAbertoForaGrade(Boolean.FALSE);
		} catch (Exception e) {
			setRetornoRichPanel("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCssAproveitamentoPorIsencaoGrupoOptativa() {
		if (getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().getAproveitamentoPorIsencao()) {
			return "form-control campos";
		}
		return "form-control camposObrigatorios";
	}

	public String getCssAproveitamentoPorIsencao() {
		if (getGradeDisciplinaVO().getDisciplinasAproveitadasVO().getAproveitamentoPorIsencao()) {
			return "form-control campos";
		}
		return "form-control camposObrigatorios";
	}

	public String getCssAproveitamentoPorIsencaoForaGrade() {
		if (getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().getAproveitamentoPorIsencao()) {
			return "form-control campos";
		}
		return "form-control camposObrigatorios";
	}

	public String getAbrirModalAproveitamentoDisciplinaForaGrade() {
		if (Uteis.isAtributoPreenchido(getMapaEquivalenciaDisciplinaCursadaVO()) && !isExisteHistoricoVinculadoDisciplina(getMapaEquivalenciaDisciplinaCursadaVO().getHistorico()))
			return "RichFaces.$('panelDadosAproveitamentoForaGrade').show()";
		return "";
	}

	public String getAbrirModalAproveitamentoDisciplina() {
		if (!isExisteHistoricoVinculadoDisciplina(getGradeDisciplinaVO().getHistoricoAtualAluno()))
			return "RichFaces.$('panelDadosAproveitamento').show()";
		return "";
	}

	public String getAbrirModalAproveitamentoDisciplinaGrupoOptativa() {
		if (Uteis.isAtributoPreenchido(getGradeCurricularGrupoOptativaDisciplinaVO()) && !isExisteHistoricoVinculadoDisciplina(getGradeCurricularGrupoOptativaDisciplinaVO().getHistoricoAtualAluno()))
			return "RichFaces.$('panelDadosAproveitamentoGrupoOptativa').show()";
		return "";
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>sigla</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPeriodoLetivoPorSigla(Integer siglaPrm) throws Exception {
		return getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(siglaPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>descricao</code> Este atributo é
	 * uma lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarGradeCurricularPorDescricao(String descricaoPrm) throws Exception {
		return getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(getAproveitamentoDisciplinaVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>identificadorTurma</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os
	 * valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarTurmaPorIdentificadorTurma() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultarPorPeriodoLetivoPorIdentificadorPeriodoLetivoEUnidadeEnsinoCursoTurno(
				getAproveitamentoDisciplinaVO().getPeridoLetivo().getPeriodoLetivo(), getAproveitamentoDisciplinaVO().getUnidadeEnsino().getCodigo(), 
				getAproveitamentoDisciplinaVO().getCurso().getCodigo(), getAproveitamentoDisciplinaVO().getTurno().getCodigo(),
				false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAcao() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("AP", "Aproveitamento de Disciplina"));
		itens.add(new SelectItem("CO", "Concessão de Crédito"));
		itens.add(new SelectItem("CH", "Concessão de Carga Horária"));
		return itens;
	}

	public void consultarDisciplinaPeriodoLetivo() {
		try {
			List<GradeDisciplinaVO> listaDisciplina = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getAproveitamentoDisciplinaVO().getPeridoLetivo().getCodigo(), false, getUsuarioLogado(), getAproveitamentoDisciplinaVO().getMatricula().getCurso().getConfiguracaoAcademico());
			getAproveitamentoDisciplinaVO().getPeridoLetivo().setGradeDisciplinaVOs(listaDisciplina);
		} catch (Exception e) {
			getAproveitamentoDisciplinaVO().getPeridoLetivo().setGradeDisciplinaVOs(new ArrayList<>(0));
		}
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<>(0);
		}
		return listaSelectItemPeriodoLetivo;
	}

	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivoMatricula() {
		if (listaSelectItemPeriodoLetivoMatricula == null) {
			listaSelectItemPeriodoLetivoMatricula = new ArrayList<>(0);
		}
		return listaSelectItemPeriodoLetivoMatricula;
	}

	public void setListaSelectItemPeriodoLetivoMatricula(List<SelectItem> listaSelectItemPeriodoLetivoMatricula) {
		this.listaSelectItemPeriodoLetivoMatricula = listaSelectItemPeriodoLetivoMatricula;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public List getListaConsultaMatriculado() {
		return listaConsultaMatriculado;
	}

	public void setListaConsultaMatriculado(List listaConsultaMatriculado) {
		this.listaConsultaMatriculado = listaConsultaMatriculado;
	}

	public boolean getApresentarResultadoConsultaMatriculado() {
		if (this.getListaConsultaMatriculado() == null || this.getListaConsultaMatriculado().size() == 0) {
			return false;
		}
		return true;
	}

	public Boolean getMatriculado() {
		return matriculado;
	}

	public void setMatriculado(Boolean matriculado) {
		this.matriculado = matriculado;
	}

	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
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

	public List getListaConsultaDisciplinaForaGrade() {
		if (listaConsultaDisciplinaForaGrade == null) {
			listaConsultaDisciplinaForaGrade = new ArrayList<>(0);
		}
		return listaConsultaDisciplinaForaGrade;
	}

	public void setListaConsultaDisciplinaForaGrade(List listaConsultaDisciplinaForaGrade) {
		this.listaConsultaDisciplinaForaGrade = listaConsultaDisciplinaForaGrade;
	}

	public String getCampoConsultaDisciplinaForaGrade() {
		if (campoConsultaDisciplinaForaGrade == null) {
			campoConsultaDisciplinaForaGrade = "";
		}
		return campoConsultaDisciplinaForaGrade;
	}

	public void setCampoConsultaDisciplinaForaGrade(String campoConsultaDisciplinaForaGrade) {
		this.campoConsultaDisciplinaForaGrade = campoConsultaDisciplinaForaGrade;
	}

	public String getValorConsultaDisciplinaForaGrade() {
		if (valorConsultaDisciplinaForaGrade == null) {
			valorConsultaDisciplinaForaGrade = "";
		}
		return valorConsultaDisciplinaForaGrade;
	}

	public void setValorConsultaDisciplinaForaGrade(String valorConsultaDisciplinaForaGrade) {
		this.valorConsultaDisciplinaForaGrade = valorConsultaDisciplinaForaGrade;
	}

	public AproveitamentoDisciplinaVO getAproveitamentoDisciplinaVO() {
		if (aproveitamentoDisciplinaVO == null) {
			aproveitamentoDisciplinaVO = new AproveitamentoDisciplinaVO();
		}
		return aproveitamentoDisciplinaVO;
	}

	public void setAproveitamentoDisciplinaVO(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO) {
		this.aproveitamentoDisciplinaVO = aproveitamentoDisciplinaVO;
	}

	public DisciplinasAproveitadasVO getDisciplinasAproveitadasVO() {
		if (disciplinasAproveitadasVO == null) {
			disciplinasAproveitadasVO = new DisciplinasAproveitadasVO();
		}
		return disciplinasAproveitadasVO;
	}

	public void setDisciplinasAproveitadasVO(DisciplinasAproveitadasVO disciplinasAproveitadasVO) {
		this.disciplinasAproveitadasVO = disciplinasAproveitadasVO;
	}

	public void setMostrarPanelAviso(Boolean mostrarPanelAviso) {
		this.mostrarPanelAviso = mostrarPanelAviso;
	}

	public Boolean getIsMostrarPanelAviso() {
		if (mostrarPanelAviso == null) {
			mostrarPanelAviso = Boolean.FALSE;
		}
		return mostrarPanelAviso;
	}

	public boolean getApresentarDadosAproveitamentoDisciplina() {
		return getGradeDisciplinaVO().getDisciplinasAproveitadasVO().getTipo().equals("AP");
	}

	public boolean getApresentarDadosConcessaoCredito() {
		return getGradeDisciplinaVO().getDisciplinasAproveitadasVO().getTipo().equals("CO");
	}

	public boolean getApresentarDadosConcessaoCargaHoraria() {
		return getGradeDisciplinaVO().getDisciplinasAproveitadasVO().getTipo().equals("CH");
	}

	public boolean getApresentarDadosAproveitamentoDisciplinaGrupoOptativa() {
		return getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().getTipo().equals("AP");
	}

	public boolean getApresentarDadosConcessaoCreditoGrupoOptativa() {
		return getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().getTipo().equals("CO");
	}

	public boolean getApresentarDadosConcessaoCargaHorariaGrupoOptativa() {
		return getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplinasAproveitadasVO().getTipo().equals("CH");
	}

	public boolean getApresentarDadosAproveitamentoDisciplinaForaGrade() {
		return getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().getTipo().equals("AP");
	}

	public boolean getApresentarDadosConcessaoCreditoForaGrade() {
		return getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().getTipo().equals("CO");
	}

	public boolean getApresentarDadosConcessaoCargaHorariaForaGrade() {
		return getMapaEquivalenciaDisciplinaCursadaVO().getDisciplinasAproveitadasVO().getTipo().equals("CH");
	}

	public ConcessaoCreditoDisciplinaVO getConcessaoCreditoDisciplinaVO() {
		if (concessaoCreditoDisciplinaVO == null) {
			concessaoCreditoDisciplinaVO = new ConcessaoCreditoDisciplinaVO();
		}
		return concessaoCreditoDisciplinaVO;
	}

	public void setConcessaoCreditoDisciplinaVO(ConcessaoCreditoDisciplinaVO concessaoCreditoDisciplinaVO) {
		this.concessaoCreditoDisciplinaVO = concessaoCreditoDisciplinaVO;
	}

	// public boolean getApresentarAnoSemestre() {
	// return
	// !getAproveitamentoDisciplinaVO().getCurso().getNivelEducacionalPosGraduacao();
	// }
	public Boolean getApresentarAno() {
		return getAproveitamentoDisciplinaVO().getCurso().getPeriodicidade().equals("AN") || getAproveitamentoDisciplinaVO().getCurso().getPeriodicidade().equals("SE");
	}

	public Boolean getApresentarSemestre() {
		return getAproveitamentoDisciplinaVO().getCurso().getPeriodicidade().equals("SE");
	}

	public String getDesabilitaTipoAproveitamentoDisciplina() {
		if (getAproveitamentoDisciplinaVO().getNovoObj()) {
			return "false";
		}
		return "true";
	}

	public ConcessaoCargaHorariaDisciplinaVO getConcessaoCargaHorariaDisciplinaVO() {
		if (concessaoCargaHorariaDisciplinaVO == null) {
			concessaoCargaHorariaDisciplinaVO = new ConcessaoCargaHorariaDisciplinaVO();
		}
		return concessaoCargaHorariaDisciplinaVO;
	}

	public void setConcessaoCargaHorariaDisciplinaVO(ConcessaoCargaHorariaDisciplinaVO concessaoCargaHorariaDisciplinaVO) {
		this.concessaoCargaHorariaDisciplinaVO = concessaoCargaHorariaDisciplinaVO;
	}

	public Boolean getIsCursoEnsinoMedio() {
		if (getAproveitamentoDisciplinaVO().getCurso().getNivelEducacional().equals("ME")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public List<DisciplinasAproveitadasVO> getListaDisciplinasExcluidas() {
		if (listaDisciplinasExcluidas == null) {
			listaDisciplinasExcluidas = new ArrayList<DisciplinasAproveitadasVO>(0);
		}
		return listaDisciplinasExcluidas;
	}

	public void setListaDisciplinasExcluidas(List<DisciplinasAproveitadasVO> listaDisciplinasExcluidas) {
		this.listaDisciplinasExcluidas = listaDisciplinasExcluidas;
	}

	public Boolean getEditandoDisciplinasAproveitadas() {
		if (editandoDisciplinasAproveitadas == null) {
			editandoDisciplinasAproveitadas = Boolean.FALSE;
		}
		return editandoDisciplinasAproveitadas;
	}

	public void setEditandoDisciplinasAproveitadas(Boolean editandoDisciplinasAproveitadas) {
		this.editandoDisciplinasAproveitadas = editandoDisciplinasAproveitadas;
	}

	public Boolean getMostrarPanelAvisoDisciplinaHistorico() {
		if (mostrarPanelAvisoDisciplinaHistorico == null) {
			mostrarPanelAvisoDisciplinaHistorico = Boolean.FALSE;
		}
		return mostrarPanelAvisoDisciplinaHistorico;
	}

	public void setMostrarPanelAvisoDisciplinaHistorico(Boolean mostrarPanelAvisoDisciplinaHistorico) {
		this.mostrarPanelAvisoDisciplinaHistorico = mostrarPanelAvisoDisciplinaHistorico;
	}

	public List getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>();
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}

	public DisciplinasAproveitadasVO getDisciplinasAproveitadasTemporariaVO() {
		return disciplinasAproveitadasTemporariaVO;
	}

	public void setDisciplinasAproveitadasTemporariaVO(DisciplinasAproveitadasVO disciplinasAproveitadasTemporariaVO) {
		this.disciplinasAproveitadasTemporariaVO = disciplinasAproveitadasTemporariaVO;
	}

	public List getListaSelectItemPeriodoLetivoComGrupoOptativas() {
		if (ListaSelectItemPeriodoLetivoComGrupoOptativas == null) {
			ListaSelectItemPeriodoLetivoComGrupoOptativas = new ArrayList<>(0);
		}
		return ListaSelectItemPeriodoLetivoComGrupoOptativas;
	}

	public void setListaSelectItemPeriodoLetivoComGrupoOptativas(List listaSelectItemPeriodoLetivoComGrupoOptativas) {
		ListaSelectItemPeriodoLetivoComGrupoOptativas = listaSelectItemPeriodoLetivoComGrupoOptativas;
	}

	public PeriodoLetivoVO getPeriodoLetivoGrupoOptativaVO() {
		if (periodoLetivoGrupoOptativaVO == null) {
			periodoLetivoGrupoOptativaVO = new PeriodoLetivoVO();
		}
		return periodoLetivoGrupoOptativaVO;
	}

	public void setPeriodoLetivoGrupoOptativaVO(PeriodoLetivoVO periodoLetivoGrupoOptativaVO) {
		this.periodoLetivoGrupoOptativaVO = periodoLetivoGrupoOptativaVO;
	}

	public Integer getCodigoPeriodoLetivoGrupoOptativa() {
		if (codigoPeriodoLetivoGrupoOptativa == null) {
			codigoPeriodoLetivoGrupoOptativa = 0;
		}
		return codigoPeriodoLetivoGrupoOptativa;
	}

	public void setCodigoPeriodoLetivoGrupoOptativa(Integer codigoPeriodoLetivoGrupoOptativa) {
		this.codigoPeriodoLetivoGrupoOptativa = codigoPeriodoLetivoGrupoOptativa;
	}

	public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplinaVO() {
		if (gradeCurricularGrupoOptativaDisciplinaVO == null) {
			gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
		}
		return gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public void setGradeCurricularGrupoOptativaDisciplinaVO(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
		this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
	}

	/**
	 * @return the mapaEquivalenciaDisciplinaVisualizar
	 */
	public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplinaVisualizar() {
		if (mapaEquivalenciaDisciplinaVisualizar == null) {
			mapaEquivalenciaDisciplinaVisualizar = new MapaEquivalenciaDisciplinaVO();
		}
		return mapaEquivalenciaDisciplinaVisualizar;
	}

	/**
	 * @param mapaEquivalenciaDisciplinaVisualizar
	 *            the mapaEquivalenciaDisciplinaVisualizar to set
	 */
	public void setMapaEquivalenciaDisciplinaVisualizar(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVisualizar) {
		this.mapaEquivalenciaDisciplinaVisualizar = mapaEquivalenciaDisciplinaVisualizar;
	}

	/**
	 * @return the mapaEquivalenciaDisciplinaCursadaVO
	 */
	public MapaEquivalenciaDisciplinaCursadaVO getMapaEquivalenciaDisciplinaCursadaVO() {
		if (mapaEquivalenciaDisciplinaCursadaVO == null) {
			mapaEquivalenciaDisciplinaCursadaVO = new MapaEquivalenciaDisciplinaCursadaVO();
		}
		return mapaEquivalenciaDisciplinaCursadaVO;
	}

	/**
	 * @param mapaEquivalenciaDisciplinaCursadaVO
	 *            the mapaEquivalenciaDisciplinaCursadaVO to set
	 */
	public void setMapaEquivalenciaDisciplinaCursadaVO(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) {
		this.mapaEquivalenciaDisciplinaCursadaVO = mapaEquivalenciaDisciplinaCursadaVO;
	}

	/**
	 * @return the retornoRichPanel
	 */
	public String getRetornoRichPanel() {
		if (retornoRichPanel == null) {
			retornoRichPanel = "";
		}
		return retornoRichPanel;
	}

	/**
	 * @param retornoRichPanel
	 *            the retornoRichPanel to set
	 */
	public void setRetornoRichPanel(String retornoRichPanel) {
		this.retornoRichPanel = retornoRichPanel;
	}

	/**
	 * @return the abaApresentar
	 */
	public String getAbaApresentar() {
		if (abaApresentar == null) {
			if (this.getAproveitamentoDisciplinaVO().getAproveitamentoPrevisto()) {
				abaApresentar = "dadosGerais"; // dadosGerais
			} else {
				abaApresentar = "dadosGerais"; //
			}
		}
		return abaApresentar;
	}

	/**
	 * @param abaApresentar
	 *            the abaApresentar to set
	 */
	public void setAbaApresentar(String abaApresentar) {
		this.abaApresentar = abaApresentar;
	}

	/**
	 * @return the panelAproveitamentoDisciplinaAberto
	 */
	public Boolean getPanelAproveitamentoDisciplinaAberto() {
		if (panelAproveitamentoDisciplinaAberto == null) {
			panelAproveitamentoDisciplinaAberto = Boolean.FALSE;
		}
		return panelAproveitamentoDisciplinaAberto;
	}

	/**
	 * @param panelAproveitamentoDisciplinaAberto
	 *            the panelAproveitamentoDisciplinaAberto to set
	 */
	public void setPanelAproveitamentoDisciplinaAberto(Boolean panelAproveitamentoDisciplinaAberto) {
		this.panelAproveitamentoDisciplinaAberto = panelAproveitamentoDisciplinaAberto;
	}

	/**
	 * @return the panelAproveitamentoDisciplinaAbertoGrupoOptativa
	 */
	public Boolean getPanelAproveitamentoDisciplinaAbertoGrupoOptativa() {
		if (panelAproveitamentoDisciplinaAbertoGrupoOptativa == null) {
			panelAproveitamentoDisciplinaAbertoGrupoOptativa = Boolean.FALSE;
		}
		return panelAproveitamentoDisciplinaAbertoGrupoOptativa;
	}

	/**
	 * @param panelAproveitamentoDisciplinaAbertoGrupoOptativa
	 *            the panelAproveitamentoDisciplinaAbertoGrupoOptativa to set
	 */
	public void setPanelAproveitamentoDisciplinaAbertoGrupoOptativa(Boolean panelAproveitamentoDisciplinaAbertoGrupoOptativa) {
		this.panelAproveitamentoDisciplinaAbertoGrupoOptativa = panelAproveitamentoDisciplinaAbertoGrupoOptativa;
	}

	/**
	 * @return the panelAproveitamentoDisciplinaAbertoForaGrade
	 */
	public Boolean getPanelAproveitamentoDisciplinaAbertoForaGrade() {
		if (panelAproveitamentoDisciplinaAbertoForaGrade == null) {
			panelAproveitamentoDisciplinaAbertoForaGrade = Boolean.FALSE;
		}
		return panelAproveitamentoDisciplinaAbertoForaGrade;
	}

	/**
	 * @param panelAproveitamentoDisciplinaAbertoForaGrade
	 *            the panelAproveitamentoDisciplinaAbertoForaGrade to set
	 */
	public void setPanelAproveitamentoDisciplinaAbertoForaGrade(Boolean panelAproveitamentoDisciplinaAbertoForaGrade) {
		this.panelAproveitamentoDisciplinaAbertoForaGrade = panelAproveitamentoDisciplinaAbertoForaGrade;
	}

	/**
	 * @return the renderedPanelCidade
	 */
	public String getRenderedPanelCidade() {
		if (renderedPanelCidade == null) {
			renderedPanelCidade = "";
		}
		return renderedPanelCidade;
	}

	/**
	 * @param renderedPanelCidade
	 *            the renderedPanelCidade to set
	 */
	public void setRenderedPanelCidade(String renderedPanelCidade) {
		this.renderedPanelCidade = renderedPanelCidade;
	}

	public void atualizarPeriodoLetivoPrevistoMatricula() {
		try {
			getFacadeFactory().getAproveitamentoDisciplinaFacade().realizarMontagemListaDisciplinasAproveitadas(getAproveitamentoDisciplinaVO(), getAproveitamentoDisciplinaVO().getInstituicao(), getAproveitamentoDisciplinaVO().getCidadeVO(), getUsuarioLogado());
			List<HistoricoVO> historicosPrevitosAproveitamento = getFacadeFactory().getAproveitamentoDisciplinaFacade().gerarHistoricosPrevistosDisciplinasAproveitadas(getAproveitamentoDisciplinaVO(), getUsuarioLogado());
			getAproveitamentoDisciplinaVO().getMatricula().setHistoricosAproveitamentoDisciplinaPrevisto(historicosPrevitosAproveitamento);
			getFacadeFactory().getHistoricoFacade().atualizarDadosMatriculaComHistoricoAlunoVO(getAproveitamentoDisciplinaVO().getMatricula(), getAproveitamentoDisciplinaVO().getMatricula().getGradeCurricularAtual().getCodigo(), false, getAproveitamentoDisciplinaVO().getMatricula().getCurso().getConfiguracaoAcademico(), getUsuarioLogado());
			List<PeriodoLetivoVO> listaPeriodoValidosRenovacao = getFacadeFactory().getMatriculaPeriodoFacade().obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao(getAproveitamentoDisciplinaVO().getMatricula(), getAproveitamentoDisciplinaVO().getMatriculaPeriodo(), getAproveitamentoDisciplinaVO().getMatricula().getCurso().getConfiguracaoAcademico(), getAproveitamentoDisciplinaVO().getMatricula().getMatriculaComHistoricoAlunoVO(), getUsuarioLogado());
			if (listaPeriodoValidosRenovacao.isEmpty()) {
				// se vazia adota-se o primeiro periodo como indicado para
				// matricula do aluno
				this.setPeriodoLetivoPrevistoMatricula(getAproveitamentoDisciplinaVO().getMatricula().getGradeCurricularAtual().getPrimeiroPeriodoLetivoGrade());
			} else {
				// adota-se o ultimo periodo da lista como sendo o indicado /
				// previsto para renovacao do aluno.
				this.setPeriodoLetivoPrevistoMatricula(listaPeriodoValidosRenovacao.get(listaPeriodoValidosRenovacao.size() - 1));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the periodoLetivoPrevistoMatricula
	 */
	public PeriodoLetivoVO getPeriodoLetivoPrevistoMatricula() {
		if (periodoLetivoPrevistoMatricula == null) {
			periodoLetivoPrevistoMatricula = new PeriodoLetivoVO();
		}
		return periodoLetivoPrevistoMatricula;
	}

	/**
	 * @param periodoLetivoPrevistoMatricula
	 *            the periodoLetivoPrevistoMatricula to set
	 */
	public void setPeriodoLetivoPrevistoMatricula(PeriodoLetivoVO periodoLetivoPrevistoMatricula) {
		this.periodoLetivoPrevistoMatricula = periodoLetivoPrevistoMatricula;
	}

	public List<SelectItem> listaSelectItemSituacaoHistorico;

	public void setListaSelectItemSituacaoHistorico(List<SelectItem> listaSelectItemSituacaoHistorico) {
		this.listaSelectItemSituacaoHistorico = listaSelectItemSituacaoHistorico;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoHistorico() {
		if (listaSelectItemSituacaoHistorico == null) {
			listaSelectItemSituacaoHistorico = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.APROVADO_APROVEITAMENTO, SituacaoHistorico.APROVADO_APROVEITAMENTO.getDescricao()));
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.CONCESSAO_CREDITO, SituacaoHistorico.CONCESSAO_CREDITO.getDescricao()));
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.CONCESSAO_CARGA_HORARIA, SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getDescricao()));
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.APROVADO, SituacaoHistorico.APROVADO.getDescricao()));
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.APROVEITAMENTO_BANCA, SituacaoHistorico.APROVEITAMENTO_BANCA.getDescricao()));
			listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA, SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getDescricao()));
		}
		return listaSelectItemSituacaoHistorico;
	}

	private void executarVerificacaoJaExisteHistoricoVinculadoDisciplina(HistoricoVO historicoVO) throws Exception {
		if (isExisteHistoricoVinculadoDisciplina(historicoVO)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AproveitamentoDisciplina_historicoJaIncluido").replace("{0}", historicoVO.getDisciplina().getNome()));
		}
	}

	/**
	 * Responsável por verificar se existe histórico vinculado a disciplina,
	 * levando em consideração que o registro do aproveitamento só poderá ser
	 * realizado para disciplinas que não tenham vínculo com histórico ou que a
	 * matriz curricular do histórico seja diferente da matriz curricular do
	 * aproveitamento.
	 * 
	 * @author Wellington Rodrigues 02/03/2015
	 * @param historicoVO
	 * @return
	 */
	private boolean isExisteHistoricoVinculadoDisciplina(HistoricoVO historicoVO) {
		return Uteis.isAtributoPreenchido(historicoVO) 
				&& historicoVO.getMatrizCurricular().getCodigo().equals(getAproveitamentoDisciplinaVO().getGradeCurricular().getCodigo())
		        && (SituacaoHistorico.getEnum(historicoVO.getSituacao()).getHistoricoAprovado());
	}

	
	public void cancelarSelecacaoMatriculaAlunoAproveitamentoDisciplina() {
		setMatriculaAlunoAproveitarDisciplina(new MatriculaVO());
		inicializarMatriculasAlunoOutrosCursosAproveitamentoDisciplina();
	}
	
	public void selecionarMatriculaAlunoAproveitamentoDisciplina() {
		try {		
			MatriculaVO matriculaPessoa = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaAlunoOutrosCursoAproveitamento");
//			if (!getAproveitamentoDisciplinaVO().getMatricula().getCurso().getPeriodicidade().equals(matriculaPessoa.getCurso().getPeriodicidade())) {
//				throw new Exception("Só é possível realizar o aproveitamento entre cursos de mesma periodicidade. Periodicidade do outro curso selecionado: " + matriculaPessoa.getCurso().getPeriodicidade_Apresentar() + 
//						            "Periodicidade do curso do aproveitamento: " + getAproveitamentoDisciplinaVO().getMatricula().getCurso().getPeriodicidade_Apresentar());
//			}
//			TipoNivelEducacional tipoCursoAproveitamento = TipoNivelEducacional.getEnum(getAproveitamentoDisciplinaVO().getMatricula().getCurso().getNivelEducacional());
//			TipoNivelEducacional tipoOutroCursoAproveitarDisciplinas = TipoNivelEducacional.getEnum(matriculaPessoa.getCurso().getNivelEducacional());
//			if (!tipoCursoAproveitamento.getNivel().equals(tipoOutroCursoAproveitarDisciplinas.getNivel())) {
//				throw new Exception("Só é possível realizar o aproveitamento entre cursos de níveis educacionais incompatíveis. Nível Educacional do outro curso selecionado: " + tipoOutroCursoAproveitarDisciplinas.getNivel() + 
//						            "Nível Educacional do curso do aproveitamento: " + tipoCursoAproveitamento.getNivel());
//			}
			setMatriculaAlunoAproveitarDisciplina(matriculaPessoa);
			montarListaSelectMapaEquivalenciaMatrizCurricularCursoSelecionadoAproveitamento();
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}		
	}
	
	public void inicializarMatriculasAlunoOutrosCursosAproveitamentoDisciplina() {
		try {
			getMatriculasAlunoValidasAproveitamento().clear();
			setMapaEquivalenciaAproveitarDisciplinasOutraMatricula(null);
			setMatriculaAlunoAproveitarDisciplina(null);
			// traz todas as matriculas da pessoa, agora vamos filtrar as que são validas para aproveitamento.
			List<MatriculaVO> matriculasPessoa = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCodigoPessoaCurso(getAproveitamentoDisciplinaVO().getMatricula().getAluno().getCodigo(), null, null, false, getUsuarioLogado());
			for (MatriculaVO matriculaPessoa : matriculasPessoa) {
				if (!getAproveitamentoDisciplinaVO().getMatricula().getMatricula().equals(matriculaPessoa.getMatricula())) {
					// nao pode ser a propria matriz selecionada no aproveitamento
					// tem que ser a mesma periodicidade - anual com anual - semestral com semestral - integral com integral
					// tem qer ser do mesmo nível educional (atributo nivel do Enum TipoNivelEducacional)
					getMatriculasAlunoValidasAproveitamento().add(matriculaPessoa);
				}
			}
			if (getMatriculasAlunoValidasAproveitamento().isEmpty()) {
				throw new Exception("Este aluno(a) não possui outras matrículas no sistema para aproveitar disciplinas");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<MatriculaVO> getMatriculasAlunoValidasAproveitamento() {
		if (matriculasAlunoValidasAproveitamento == null) {
			matriculasAlunoValidasAproveitamento = new ArrayList<MatriculaVO>(0);
		}
		return matriculasAlunoValidasAproveitamento;
	}

	public void setMatriculasAlunoValidasAproveitamento(List<MatriculaVO> matriculasAlunoValidasAproveitamento) {
		this.matriculasAlunoValidasAproveitamento = matriculasAlunoValidasAproveitamento;
	}
	
	public String getMatriculaSelecionada() {
		if (getMatriculaAlunoAproveitarDisciplinaSelecionada()) {
			return getMatriculaAlunoAproveitarDisciplina().getMatricula() + " - " + getMatriculaAlunoAproveitarDisciplina().getCurso().getNome() + " - " +
					getMatriculaAlunoAproveitarDisciplina().getSituacao_Apresentar();	
		}
		return "";
	}
	
	public Boolean getMatriculaAlunoAproveitarDisciplinaSelecionada() {
		if (!getMatriculaAlunoAproveitarDisciplina().getMatricula().equals("")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public MatriculaVO getMatriculaAlunoAproveitarDisciplina() {
		if (matriculaAlunoAproveitarDisciplina == null) {
			matriculaAlunoAproveitarDisciplina = new MatriculaVO();
		}
		return matriculaAlunoAproveitarDisciplina;
	}

	public void setMatriculaAlunoAproveitarDisciplina(MatriculaVO matriculaAlunoAproveitarDisciplina) {
		this.matriculaAlunoAproveitarDisciplina = matriculaAlunoAproveitarDisciplina;
	}
	
	@SuppressWarnings("unchecked")
	public void montarListaSelectMapaEquivalenciaMatrizCurricularCursoSelecionadoAproveitamento() throws Exception {
		List<MapaEquivalenciaMatrizCurricularVO> listMapaEquivalenciaVOs = getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().consultarPorCodigoGradeCurricular(getAproveitamentoDisciplinaVO().getMatricula().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
		Iterator<MapaEquivalenciaMatrizCurricularVO> i = listMapaEquivalenciaVOs.iterator();
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			MapaEquivalenciaMatrizCurricularVO obj = (MapaEquivalenciaMatrizCurricularVO) i.next();
			if(obj.getSituacaoAtivo()) {
			objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List<SelectItem>) objs, ordenador);
		setListaSelectItemMapaEquivalenciaMatriz(objs);
	}

	public List<SelectItem> getListaSelectItemMapaEquivalenciaMatriz() {
		if (listaSelectItemMapaEquivalenciaMatriz == null) {
			listaSelectItemMapaEquivalenciaMatriz = new ArrayList<SelectItem>();
		}
		return listaSelectItemMapaEquivalenciaMatriz;
	}

	public void setListaSelectItemMapaEquivalenciaMatriz(List<SelectItem> listaSelectItemMapaEquivalenciaMatriz) {
		this.listaSelectItemMapaEquivalenciaMatriz = listaSelectItemMapaEquivalenciaMatriz;
	}

	public MapaEquivalenciaMatrizCurricularVO getMapaEquivalenciaAproveitarDisciplinasOutraMatricula() {
		if (mapaEquivalenciaAproveitarDisciplinasOutraMatricula == null) {
			mapaEquivalenciaAproveitarDisciplinasOutraMatricula = new MapaEquivalenciaMatrizCurricularVO();
		}
		return mapaEquivalenciaAproveitarDisciplinasOutraMatricula;
	}

	public void setMapaEquivalenciaAproveitarDisciplinasOutraMatricula(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaAproveitarDisciplinasOutraMatricula) {
		this.mapaEquivalenciaAproveitarDisciplinasOutraMatricula = mapaEquivalenciaAproveitarDisciplinasOutraMatricula;
	}

	/**
	 * Método responsável por verificar e mapear em uma lista todas as disciplinas que podem
	 * ser aproveitadas de uma segunda matricula do aluno. Ao final deste método é possível
	 * lista todas as disciplinas que podem ser aproveitadas, listadas por período letivo, e
	 * com informações adicionais como: aproveitamento de disciplinas de grupo de optativa (sim ou nao)
	 * aproveitamento sendo realizado por meio de mapa de equivalencia (sim ou nao).
	 * Com esta lista o usuário poderá remover algum item que não tenha interesse em aproveitar e depois
	 * clicar no botao confirmar. O que de fato irá gerar os históricos para a matrícula destino.
	 * @author Edigar A Diniz Junior - 21 de jun de 2016
	 */
	public void verificarEMapearDisciplinasPodemSerAproveitadasOutraMatricula() {
		try {
			// criando uma nova instancia do objeto que mantem todas as informacores sobre o aproveitamento
			// de disciplinas entre matriculas diferentes do mesmo aluno.
			setAproveitamentoDisciplinasEntreMatriculasVO(new AproveitamentoDisciplinasEntreMatriculasVO());
			
			// inicializando os dados do objeto AproveitamentoDisciplinasEntreMatriculasVO para que o processamento possa ser realizado
			getAproveitamentoDisciplinasEntreMatriculasVO().setAproveitamentoDisciplinaVO(aproveitamentoDisciplinaVO);
			getAproveitamentoDisciplinasEntreMatriculasVO().setMatriculaOrigemAproveitamentoVO(matriculaAlunoAproveitarDisciplina);
			getAproveitamentoDisciplinasEntreMatriculasVO().setMatriculaDestinoAproveitamentoVO(getAproveitamentoDisciplinaVO().getMatricula());
			getAproveitamentoDisciplinasEntreMatriculasVO().setMapaEquivalenciaUtilizadoAproveitamento(getMapaEquivalenciaAproveitarDisciplinasOutraMatricula());
			getAproveitamentoDisciplinasEntreMatriculasVO().setResponsavel(getUsuarioLogadoClone());
			getAproveitamentoDisciplinasEntreMatriculasVO().setAnoPadrao(getAnoPadraoAproveitamentoDisciplinasEntreMatriculas());
			getAproveitamentoDisciplinasEntreMatriculasVO().setSemestrePadrao(getSemestrePadraoAproveitamentoDisciplinasEntreMatriculas());
			getAproveitamentoDisciplinasEntreMatriculasVO().setRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento(getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento());
			getAproveitamentoDisciplinasEntreMatriculasVO().setAproveitamentoPorIsencao(getAproveitamentoPorIsencaoEntreMatriculas());
			getAproveitamentoDisciplinasEntreMatriculasVO().setUtilizarAnoSemestreAtualDisciplinaAprovada(getUtilizarAnoSemestreAtualDisciplinaAprovada());
			getAproveitamentoDisciplinasEntreMatriculasVO().setDescricaoComplementacao(getDescricaoComplementacaoAproveitamentoEntreMatriculas());
			getAproveitamentoDisciplinasEntreMatriculasVO().setSituacaoHistoricoConcessaoEntreMatriculas(getSituacaoHistoricoAproveitamentoEntreMatriculas());
			getAproveitamentoDisciplinasEntreMatriculasVO().setTipoAproveitamentoDisciplinasEntreMatriculas(getTipoAproveitamentoDisciplinasEntreMatriculas());
			
			// chamando metodo responsavel por fazer as inicializacoes necessaria e o processar os aproveitamentos
			getFacadeFactory().getAproveitamentoDisciplinaFacade().verificarEMapearDisciplinasPodemSerAproveitadasOutraMatricula(getAproveitamentoDisciplinasEntreMatriculasVO(), false, getUsuarioLogado());
			
			setAproveitamentoEntreMatriculasJaExecutado(Boolean.TRUE);
			
			setMensagemID("msg_dados_incluidos");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}		
	}
	
	public Boolean getApresentarMensagemDiscplinasAproveitadasEntreMatriculas() {
		if (getAproveitamentoDisciplinasEntreMatriculasVO().getSituacao().equals("RE")) { 
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public Boolean getApresentarBotaoDiscplinasNaoAproveitadasEntreMatriculas() {
		if ((getAproveitamentoDisciplinasEntreMatriculasVO().getSituacao().equals("RE")) &&
		    (getAproveitamentoDisciplinasEntreMatriculasVO().getHistoricoNaoAproveitados().size() > 0)) { 
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	
	public String getMensagemDisciplinasAproveitadasEntreMatriculas() {
		int aproveitadas = getAproveitamentoDisciplinasEntreMatriculasVO().getHistoricoAproveitados().size();
		int naoAproveitadas = getAproveitamentoDisciplinasEntreMatriculasVO().getHistoricoNaoAproveitados().size();

		String strMensagemAproveitada = "Nenhuma Disciplina Aproveitada.";
		if (aproveitadas == 1) {
			strMensagemAproveitada = "1 Disciplina Aproveitada.";
		} else {
			if (aproveitadas > 1) {
				strMensagemAproveitada = aproveitadas + " Disciplinas Aproveitadas.";
			}
		}
		
		String strMensagemNaoAproveitada = "Nenhuma disciplina pendente.";
		if (naoAproveitadas == 1) {
			strMensagemNaoAproveitada = "1 disciplina NÃO foi aproveitada";
		} else {
			if (naoAproveitadas > 1) {
				strMensagemNaoAproveitada = naoAproveitadas + " disciplinas NÃO foram aproveitadas";
			}
		}
		return strMensagemAproveitada + " " + strMensagemNaoAproveitada;
	}

	
	public void cancelarAproveitamentoOutraMatricula() {
		// limpar dados processados aproveitamento...
	}

	public AproveitamentoDisciplinasEntreMatriculasVO getAproveitamentoDisciplinasEntreMatriculasVO() {
		if (aproveitamentoDisciplinasEntreMatriculasVO == null) {
			aproveitamentoDisciplinasEntreMatriculasVO = new AproveitamentoDisciplinasEntreMatriculasVO();
		}
		return aproveitamentoDisciplinasEntreMatriculasVO;
	}

	public void setAproveitamentoDisciplinasEntreMatriculasVO(AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO) {
		this.aproveitamentoDisciplinasEntreMatriculasVO = aproveitamentoDisciplinasEntreMatriculasVO;
	}

	public String getAnoPadraoAproveitamentoDisciplinasEntreMatriculas() {
		if (anoPadraoAproveitamentoDisciplinasEntreMatriculas == null) {
			anoPadraoAproveitamentoDisciplinasEntreMatriculas = Uteis.getAnoDataAtual4Digitos();
		}
		return anoPadraoAproveitamentoDisciplinasEntreMatriculas;
	}

	public void setAnoPadraoAproveitamentoDisciplinasEntreMatriculas(String anoPadraoAproveitamentoDisciplinasEntreMatriculas) {
		this.anoPadraoAproveitamentoDisciplinasEntreMatriculas = anoPadraoAproveitamentoDisciplinasEntreMatriculas;
	}

	public String getSemestrePadraoAproveitamentoDisciplinasEntreMatriculas() {
		if (semestrePadraoAproveitamentoDisciplinasEntreMatriculas == null) {
			semestrePadraoAproveitamentoDisciplinasEntreMatriculas = Uteis.getSemestreAtual();;
		}
		return semestrePadraoAproveitamentoDisciplinasEntreMatriculas;
	}

	public void setSemestrePadraoAproveitamentoDisciplinasEntreMatriculas(String semestrePadraoAproveitamentoDisciplinasEntreMatriculas) {
		this.semestrePadraoAproveitamentoDisciplinasEntreMatriculas = semestrePadraoAproveitamentoDisciplinasEntreMatriculas;
	}

	public Boolean getUtilizarAnoSemestreAtualDisciplinaAprovada() {
		if (utilizarAnoSemestreAtualDisciplinaAprovada == null) {
			utilizarAnoSemestreAtualDisciplinaAprovada = Boolean.FALSE;
		}
		return utilizarAnoSemestreAtualDisciplinaAprovada;
	}

	public void setUtilizarAnoSemestreAtualDisciplinaAprovada(Boolean utilizarAnoSemestreAtualDisciplinaAprovada) {
		this.utilizarAnoSemestreAtualDisciplinaAprovada = utilizarAnoSemestreAtualDisciplinaAprovada;
	}

	public String getTipoAproveitamentoDisciplinasEntreMatriculas() {
		if (tipoAproveitamentoDisciplinasEntreMatriculas == null) {
			tipoAproveitamentoDisciplinasEntreMatriculas = "AP";
		}
		return tipoAproveitamentoDisciplinasEntreMatriculas;
	}

	public void setTipoAproveitamentoDisciplinasEntreMatriculas(String tipoAproveitamentoDisciplinasEntreMatriculas) {
		this.tipoAproveitamentoDisciplinasEntreMatriculas = tipoAproveitamentoDisciplinasEntreMatriculas;
	}
	
	public Boolean getTipoAproveitamentoDisciplinasEntreMatriculasConcesao() {
		if ((getTipoAproveitamentoDisciplinasEntreMatriculas().equals("CO")) ||
		    (getTipoAproveitamentoDisciplinasEntreMatriculas().equals("CH"))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getAproveitamentoPorIsencaoEntreMatriculas() {
		if (aproveitamentoPorIsencaoEntreMatriculas == null) {
			aproveitamentoPorIsencaoEntreMatriculas = Boolean.FALSE;
		}
		return aproveitamentoPorIsencaoEntreMatriculas;
	}

	public void setAproveitamentoPorIsencaoEntreMatriculas(Boolean aproveitamentoPorIsencaoEntreMatriculas) {
		this.aproveitamentoPorIsencaoEntreMatriculas = aproveitamentoPorIsencaoEntreMatriculas;
	}

	public Boolean getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento() {
		if (realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento == null) {
			realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento = Boolean.TRUE;
		}
		return realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
	}

	public void setRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento(Boolean realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento) {
		this.realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento = realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
	}

	public String getDescricaoComplementacaoAproveitamentoEntreMatriculas() {
		if (descricaoComplementacaoAproveitamentoEntreMatriculas == null) {
			descricaoComplementacaoAproveitamentoEntreMatriculas = "";
		}
		return descricaoComplementacaoAproveitamentoEntreMatriculas;
	}

	public void setDescricaoComplementacaoAproveitamentoEntreMatriculas(String descricaoComplementacaoAproveitamentoEntreMatriculas) {
		this.descricaoComplementacaoAproveitamentoEntreMatriculas = descricaoComplementacaoAproveitamentoEntreMatriculas;
	}

	public String getSituacaoHistoricoAproveitamentoEntreMatriculas() {
		if (situacaoHistoricoAproveitamentoEntreMatriculas == null) {
			situacaoHistoricoAproveitamentoEntreMatriculas = "";
		}
		return situacaoHistoricoAproveitamentoEntreMatriculas;
	}

	public void setSituacaoHistoricoAproveitamentoEntreMatriculas(String situacaoHistoricoAproveitamentoEntreMatriculas) {
		this.situacaoHistoricoAproveitamentoEntreMatriculas = situacaoHistoricoAproveitamentoEntreMatriculas;
	}
	
	public void inicializarDetalhesLogAproveitamentoEntreMatriculasMapaEquivalente() {
		try {
			MapaEquivalenciaDisciplinaCursadaVO obj = (MapaEquivalenciaDisciplinaCursadaVO) context().getExternalContext().getRequestMap().get("disciplinaCursarMapaItens");
			getFacadeFactory().getMatriculaFacade().carregarDados(obj.getDisciplinasAproveitadasVO().getMatriculaOrigemAproveitamentoEntreMatriculas(), getUsuarioLogado());
			obj.getDisciplinasAproveitadasVO().setMatriculaComNomeCursoMatriculaOrigemAproveitamento(obj.getDisciplinasAproveitadasVO().getMatriculaOrigemAproveitamentoEntreMatriculas().getMatricula() + " - " + obj.getDisciplinasAproveitadasVO().getMatriculaOrigemAproveitamentoEntreMatriculas().getCurso().getNome());
			if (obj.getDisciplinasAproveitadasVO().getResponsavelAproveitamentoEntreMatriculas().getNome_Apresentar().equals("")) {
				UsuarioVO usuarioCarregado = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getDisciplinasAproveitadasVO().getResponsavelAproveitamentoEntreMatriculas().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				obj.getDisciplinasAproveitadasVO().setResponsavelAproveitamentoEntreMatriculas(usuarioCarregado);
			}			
			setLogDisciplinasAproveitadasVO(obj.getDisciplinasAproveitadasVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	
	public void inicializarDetalhesLogAproveitamentoEntreMatriculasGrupoOptativa() {
		try {
			GradeCurricularGrupoOptativaDisciplinaVO obj = (GradeCurricularGrupoOptativaDisciplinaVO) context().getExternalContext().getRequestMap().get("grupoOptativaDisciplinaItens");
			getFacadeFactory().getMatriculaFacade().carregarDados(obj.getDisciplinasAproveitadasVO().getMatriculaOrigemAproveitamentoEntreMatriculas(), getUsuarioLogado());
			obj.getDisciplinasAproveitadasVO().setMatriculaComNomeCursoMatriculaOrigemAproveitamento(obj.getDisciplinasAproveitadasVO().getMatriculaOrigemAproveitamentoEntreMatriculas().getMatricula() + " - " + obj.getDisciplinasAproveitadasVO().getMatriculaOrigemAproveitamentoEntreMatriculas().getCurso().getNome());
			if (obj.getDisciplinasAproveitadasVO().getResponsavelAproveitamentoEntreMatriculas().getNome_Apresentar().equals("")) {
				UsuarioVO usuarioCarregado = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getDisciplinasAproveitadasVO().getResponsavelAproveitamentoEntreMatriculas().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				obj.getDisciplinasAproveitadasVO().setResponsavelAproveitamentoEntreMatriculas(usuarioCarregado);
			}			
			setLogDisciplinasAproveitadasVO(obj.getDisciplinasAproveitadasVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	
	public void inicializarDetalhesLogAproveitamentoEntreMatriculas() {
		try {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) context().getExternalContext().getRequestMap().get("gradeDisciplinaItens");
			getFacadeFactory().getMatriculaFacade().carregarDados(obj.getDisciplinasAproveitadasVO().getMatriculaOrigemAproveitamentoEntreMatriculas(), getUsuarioLogado());
			obj.getDisciplinasAproveitadasVO().setMatriculaComNomeCursoMatriculaOrigemAproveitamento(obj.getDisciplinasAproveitadasVO().getMatriculaOrigemAproveitamentoEntreMatriculas().getMatricula() + " - " + obj.getDisciplinasAproveitadasVO().getMatriculaOrigemAproveitamentoEntreMatriculas().getCurso().getNome());
			if (obj.getDisciplinasAproveitadasVO().getResponsavelAproveitamentoEntreMatriculas().getNome_Apresentar().equals("")) {
				UsuarioVO usuarioCarregado = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getDisciplinasAproveitadasVO().getResponsavelAproveitamentoEntreMatriculas().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				obj.getDisciplinasAproveitadasVO().setResponsavelAproveitamentoEntreMatriculas(usuarioCarregado);
			}
			setLogDisciplinasAproveitadasVO(obj.getDisciplinasAproveitadasVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public DisciplinasAproveitadasVO getLogDisciplinasAproveitadasVO() {
		if (logDisciplinasAproveitadasVO == null) {
			logDisciplinasAproveitadasVO = new DisciplinasAproveitadasVO();
		}
		return logDisciplinasAproveitadasVO;
	}

	public void setLogDisciplinasAproveitadasVO(DisciplinasAproveitadasVO logDisciplinasAproveitadasVO) {
		this.logDisciplinasAproveitadasVO = logDisciplinasAproveitadasVO;
	}

	public Boolean getAproveitamentoEntreMatriculasJaExecutado() {
		if (aproveitamentoEntreMatriculasJaExecutado == null) {
			aproveitamentoEntreMatriculasJaExecutado = Boolean.FALSE;
		}
		return aproveitamentoEntreMatriculasJaExecutado;
	}

	public void setAproveitamentoEntreMatriculasJaExecutado(Boolean aproveitamentoEntreMatriculasJaExecutado) {
		this.aproveitamentoEntreMatriculasJaExecutado = aproveitamentoEntreMatriculasJaExecutado;
	}
	
    public String getMascaraConsulta() {
        if (getControleConsulta().getCampoConsulta().equals("data")) {
            return "return mascara(this.form,'formCadastro:valorConsulta','99/99/9999',event);";
        }
        return "";
    }

	public DataModelo getDataModeloAluno() {
		if (dataModeloAluno == null) {
			dataModeloAluno = new DataModelo();
			dataModeloAluno.preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		return dataModeloAluno;
	}

	public void setDataModeloAluno(DataModelo dataModeloAluno) {
		this.dataModeloAluno = dataModeloAluno;
	}
	
	public void inicializarDadosConsultaAluno() {
		try {
			getDataModeloAluno().setValorConsulta("");
			getDataModeloAluno().setListaConsulta(new ArrayList<>());
			getDataModeloAluno().setTotalRegistrosEncontrados(0);
			getDataModeloAluno().setPage(0);
			getDataModeloAluno().setPaginaAtual(1);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void executarConsultaAluno() throws Exception {
		if (getDataModeloAluno().getValorConsulta().equals("")) {
			setMensagemID("msg_entre_prmconsulta");
			return;
		}
		if (getCampoConsultaAluno().equals("matricula")) {
			MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getDataModeloAluno().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (!obj.getMatricula().equals("")) {
				getDataModeloAluno().setListaConsulta(Lists.newArrayList(obj));
				getDataModeloAluno().setTotalRegistrosEncontrados(1);
			}
		} else if (getCampoConsultaAluno().equals("nomePessoa")) {
			getDataModeloAluno().setListaConsulta(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getDataModeloAluno(), false,"", "", null, getUsuarioLogado()));
			getDataModeloAluno().setTotalRegistrosEncontrados(getFacadeFactory().getMatriculaFacade().consultaRapidaTotalPorNomePessoa(getDataModeloAluno(), false,"", "", null, getUsuarioLogado()));
		} else if (getCampoConsultaAluno().equals("registroAcademico")) {
			 
			getDataModeloAluno().setListaConsulta(getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademicoAlunoPorDataModelo(getDataModeloAluno().getValorConsulta(), getDataModeloAluno(),false, "", "", 0, getUsuarioLogado()));		
		}
		setMensagemID("msg_dados_consultados");
	}
	
	public List<SelectItem> getComboboxSexoEnum() {
		if (comboboxSexoEnum == null) {
			comboboxSexoEnum = new ArrayList<SelectItem>(0);
			comboboxSexoEnum.add(new SelectItem("", ""));
			comboboxSexoEnum.add(new SelectItem(SexoEnum.F.name(), "Feminino"));
			comboboxSexoEnum.add(new SelectItem(SexoEnum.M.name(), "Masculino"));
		}
		return comboboxSexoEnum;
	}

	public void setComboboxSexoEnum(List<SelectItem> comboboxSexoEnum) {
		this.comboboxSexoEnum = comboboxSexoEnum;
	}
	
	private void realizarVerificacaoPermitirAproveitamentoDisciplinasOptativas() throws Exception {
		if(Uteis.isAtributoPreenchido(getAproveitamentoDisciplinaVO().getMatricula().getCurso().getConfiguracaoAcademico())){
			ConfiguracaoAcademicoVO cfgCurso = getAplicacaoControle().carregarDadosConfiguracaoAcademica(getAproveitamentoDisciplinaVO().getMatricula().getCurso().getConfiguracaoAcademico().getCodigo());
			setPermitirAproveitamentoDisciplinasOptativas(Uteis.isAtributoPreenchido(cfgCurso) ? cfgCurso.getPermitirAproveitamentoDisciplinasOptativas() : true);
		} else {
			CursoVO curso = getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(getAproveitamentoDisciplinaVO().getMatricula().getMatricula(), false, getUsuarioLogado());
			ConfiguracaoAcademicoVO cfgCurso = getAplicacaoControle().carregarDadosConfiguracaoAcademica(curso.getConfiguracaoAcademico().getCodigo());
			setPermitirAproveitamentoDisciplinasOptativas(Uteis.isAtributoPreenchido(cfgCurso) ? cfgCurso.getPermitirAproveitamentoDisciplinasOptativas() : true);
		}
	}
	
	public void setPermitirAproveitamentoDisciplinasOptativas(Boolean permitirAproveitamentoDisciplinasOptativas) {
		this.permitirAproveitamentoDisciplinasOptativas = permitirAproveitamentoDisciplinasOptativas;
	}

	public Boolean getPermitirAproveitamentoDisciplinasOptativas() {
		if (permitirAproveitamentoDisciplinasOptativas == null) {
			permitirAproveitamentoDisciplinasOptativas = true;
		}
		return permitirAproveitamentoDisciplinasOptativas;
	}
	
}
