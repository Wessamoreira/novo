package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CentroResultadoRestricaoUsoVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
@Controller("CentroResultadoControle")
@Scope("viewScope")
@Lazy
public class CentroResultadoControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5367843101424894731L;
	private static final String TELA_FORM = "centroResultadoForm.xhtml";
	private static final String TELA_CONS = "centroResultadoCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "centroResultadoItens";

	private CentroResultadoVO centroResultadoVO;
	private UnidadeEnsinoVO unidadeEnsinoFiltro;
	private CentroResultadoRestricaoUsoVO centroResultadoRestricaoUsoVO;
	private SituacaoEnum situacaoEnumFiltro;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemPerfilAcesso;

	private List<CursoVO> listaConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaCurso;

	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;

	private List<DepartamentoVO> listaConsultaDepartamento;
	private String valorConsultaDepartamento;
	private String campoConsultaDepartamento;

	private DataModelo usuarioDataModelo;

	public CentroResultadoControle() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.IDENTIFICADOR_CENTRO_RESULTADO.name());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PlanoConta</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setCentroResultadoVO(new CentroResultadoVO());
		montarListaSelectItemPerfilAcesso();
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PlanoConta</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setCentroResultadoVO(getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimariaUnica(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			montarListaSelectItemPerfilAcesso();
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PlanoConta</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String addCentroResultadoFilho() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setCentroResultadoVO(new CentroResultadoVO());
			getCentroResultadoVO().setCentroResultadoPrincipal(obj);
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PlanoConta</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void persistir() {
		try {
			getFacadeFactory().getCentroResultadoFacade().persistir(getCentroResultadoVO(), true, getUsuarioLogado());
			getAplicacaoControle().removerCentroResultado(getCentroResultadoVO().getCodigo());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PlanoContaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	@Override
	public void consultarDados() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(getSituacaoEnumFiltro(),  false, null, null, null, getControleConsultaOtimizado());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>PlanoContaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getCentroResultadoFacade().excluir(getCentroResultadoVO(), true, true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void selecionarCentroResultadoSuperior() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			getFacadeFactory().getCentroResultadoFacade().validarNivelCentroResultadoSuperior(getCentroResultadoVO(), obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void limparCentroResultadoSuperior() {
		try {
			getCentroResultadoVO().setCentroResultadoPrincipal(new CentroResultadoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroResultadoSuperior() {
		try {
			getCentroResultadoVO().setArvoreCentroResultado(getFacadeFactory().getCentroResultadoFacade().consultarArvoreCentroResultadoSuperior(getCentroResultadoVO(), false, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCentroResultadoInferior() {
		try {
			getCentroResultadoVO().setArvoreCentroResultado(getFacadeFactory().getCentroResultadoFacade().consultarArvoreCentroResultadoInferior(getCentroResultadoVO(), getSituacaoEnumFiltro(), true,  getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCentroResultadoArvoreCompleta() {
		try {
			setCentroResultadoVO(new CentroResultadoVO());
			getCentroResultadoVO().setArvoreCentroResultado(getFacadeFactory().getCentroResultadoFacade().consultarArvoreCentroResultadoInferior(null, getSituacaoEnumFiltro(), true, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarCentroResultadoRestricaoUso() {
		try {
			getFacadeFactory().getCentroResultadoFacade().adicionarCentroResultadoRestricaoUso(getCentroResultadoVO(), getCentroResultadoRestricaoUsoVO(), getUsuarioLogado());
			setCentroResultadoRestricaoUsoVO(new CentroResultadoRestricaoUsoVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	

	public void removerCentroResultadoRestricaoUso() {
		try {
			setCentroResultadoRestricaoUsoVO((CentroResultadoRestricaoUsoVO) context().getExternalContext().getRequestMap().get("centroResultadoRestricaoUsoItens"));
			getFacadeFactory().getCentroResultadoFacade().removerCentroResultadoRestricaoUso(getCentroResultadoVO(), getCentroResultadoRestricaoUsoVO(), getUsuarioLogado());
			setCentroResultadoRestricaoUsoVO(new CentroResultadoRestricaoUsoVO());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerUsuario(DataScrollEvent dataScrollerEvent) {
		try {
			getUsuarioDataModelo().setPaginaAtual(dataScrollerEvent.getPage());
			getUsuarioDataModelo().setPage(dataScrollerEvent.getPage());
			consultarUsuario();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarUsuario() {
		try {
			getUsuarioDataModelo().getListaConsulta().clear();
			getUsuarioDataModelo().setLimitePorPagina(10);
			if (getUsuarioDataModelo().getCampoConsulta().equals("nome")) {
				Uteis.checkState(getUsuarioDataModelo().getValorConsulta().length() < 2, getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				getUsuarioDataModelo().setListaConsulta(getFacadeFactory().getUsuarioFacade().consultarPorNome(getUsuarioDataModelo().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioDataModelo().getLimitePorPagina(), getUsuarioDataModelo().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getUsuarioDataModelo().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarTotalDeGegistroPorNome(getUsuarioDataModelo().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getUsuarioDataModelo().getCampoConsulta().equals("username")) {
				Uteis.checkState(getUsuarioDataModelo().getValorConsulta().length() < 2, getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				getUsuarioDataModelo().setListaConsulta(getFacadeFactory().getUsuarioFacade().consultarPorUsername(getUsuarioDataModelo().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioDataModelo().getLimitePorPagina(), getUsuarioDataModelo().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				getUsuarioDataModelo().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarTotalDeRegistroPorUsername(getUsuarioDataModelo().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getUsuarioDataModelo().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarUsuario() {
		try {
			UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItens");
			getCentroResultadoRestricaoUsoVO().setUsuarioVO(obj);
			adicionarCentroResultadoRestricaoUso();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboUsuario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("username", "Username"));
		return itens;
	}
	
	public void limparDadosCentroResultadoRestricaoUso() {
		try {
			getCentroResultadoVO().getListaCentroResultadoRestricaoUsoVOs().clear();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public Integer getTamanhoListaUsuarioVOs() {
		if (getCentroResultadoVO().getListaCentroResultadoRestricaoUsoVOs().isEmpty()) {
			return 0;
		} else {
			return getCentroResultadoVO().getListaCentroResultadoRestricaoUsoVOs().size();
		}
	}

	public Integer getColunasListaUsuarioVOs() {
		if (getCentroResultadoVO().getListaCentroResultadoRestricaoUsoVOs().size() < 3) {
			return getCentroResultadoVO().getListaCentroResultadoRestricaoUsoVOs().size();
		}
		return 3;
	}

	public void montarListaSelectItemPerfilAcesso() {
		try {
			getListaSelectItemPerfilAcesso().clear();
			List<PerfilAcessoVO> resultadoConsulta = getFacadeFactory().getPerfilAcessoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemPerfilAcesso().add(new SelectItem(0, ""));
			resultadoConsulta.stream().forEach(p -> getListaSelectItemPerfilAcesso().add(new SelectItem(p.getCodigo(), p.getNome())));
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.IDENTIFICADOR_CENTRO_RESULTADO.name());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public CentroResultadoVO getCentroResultadoVO() {
		if (centroResultadoVO == null) {
			centroResultadoVO = new CentroResultadoVO();
		}
		return centroResultadoVO;
	}

	public void setCentroResultadoVO(CentroResultadoVO centroResultadoVO) {
		this.centroResultadoVO = centroResultadoVO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoFiltro() {
		if (unidadeEnsinoFiltro == null) {
			unidadeEnsinoFiltro = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoFiltro;
	}

	public void setUnidadeEnsinoFiltro(UnidadeEnsinoVO unidadeEnsinoFiltro) {
		this.unidadeEnsinoFiltro = unidadeEnsinoFiltro;
	}

	public SituacaoEnum getSituacaoEnumFiltro() {
		if (situacaoEnumFiltro == null) {
			situacaoEnumFiltro = SituacaoEnum.EM_CONSTRUCAO;
		}
		return situacaoEnumFiltro;
	}

	public void setSituacaoEnumFiltro(SituacaoEnum situacaoEnumFiltro) {
		this.situacaoEnumFiltro = situacaoEnumFiltro;
	}

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<>();
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List<DepartamentoVO> getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList<>();
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List<DepartamentoVO> listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}

	public String getValorConsultaDepartamento() {
		if (valorConsultaDepartamento == null) {
			valorConsultaDepartamento = "";
		}
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public String getCampoConsultaDepartamento() {
		if (campoConsultaDepartamento == null) {
			campoConsultaDepartamento = "";
		}
		return campoConsultaDepartamento;
	}

	public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
		this.campoConsultaDepartamento = campoConsultaDepartamento;
	}

	public List<SelectItem> getListaSelectItemPerfilAcesso() {
		listaSelectItemPerfilAcesso = Optional.ofNullable(listaSelectItemPerfilAcesso).orElse(new ArrayList<>());
		return listaSelectItemPerfilAcesso;
	}

	public void setListaSelectItemPerfilAcesso(List<SelectItem> listaSelectItemPerfilAcesso) {
		this.listaSelectItemPerfilAcesso = listaSelectItemPerfilAcesso;
	}

	public DataModelo getUsuarioDataModelo() {
		usuarioDataModelo = Optional.ofNullable(usuarioDataModelo).orElse(new DataModelo());
		return usuarioDataModelo;
	}

	public void setUsuarioDataModelo(DataModelo usuarioDataModelo) {
		this.usuarioDataModelo = usuarioDataModelo;
	}

	public CentroResultadoRestricaoUsoVO getCentroResultadoRestricaoUsoVO() {
		centroResultadoRestricaoUsoVO = Optional.ofNullable(centroResultadoRestricaoUsoVO).orElse(new CentroResultadoRestricaoUsoVO());
		return centroResultadoRestricaoUsoVO;
	}

	public void setCentroResultadoRestricaoUsoVO(CentroResultadoRestricaoUsoVO centroResultadoRestricaoUsoVO) {
		this.centroResultadoRestricaoUsoVO = centroResultadoRestricaoUsoVO;
	}

}
