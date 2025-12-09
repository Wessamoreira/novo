package controle.compras;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoComprasEnum;
import negocio.comuns.compras.EntregaRequisicaoItemVO;
import negocio.comuns.compras.EntregaRequisicaoVO;
import negocio.comuns.compras.EstoqueVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("EntregaRequisicaoControle")
@Scope("viewScope")
@Lazy
public class EntregaRequisicaoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private EntregaRequisicaoVO entregaRequisicaoVO;
	private EntregaRequisicaoItemVO entregaRequisicaoItemVO;
	private List<EstoqueVO> listaEstoqueVOs;
	private String campoConsultaRequisicao;
	private String valorConsultaRequisicao;
	private List<SelectItem> listaConsultaRequisicao;
	private String userNameLiberarFuncionalidade;
	private String senhaLiberarFuncionalidade;
	private boolean liberadoAlteracaoEstoqueRetidada = false;
	private boolean navegacaoDeMapaRequisicao = false;
	private DataModelo centroResultadoDataModelo;

	public EntregaRequisicaoControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	@PostConstruct
	public void postConstruct() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(new Date());
		getControleConsultaOtimizado().setDataFim(Uteis.getNewDateComUmMesAMais());
		realizarCarregamentoPorTelaMapaRequisicao();
	}

	public void realizarCarregamentoPorTelaMapaRequisicao() {
		RequisicaoVO requisicaoVO = (RequisicaoVO) context().getExternalContext().getSessionMap().get("requisicao");
		try {
			if (Uteis.isAtributoPreenchido(requisicaoVO)) {
				setEntregaRequisicaoVO(new EntregaRequisicaoVO());
				setEntregaRequisicaoItemVO(new EntregaRequisicaoItemVO());
				getEntregaRequisicaoVO().setResponsavel(getUsuarioLogadoClone());
				getEntregaRequisicaoVO().setRequisicao(requisicaoVO);
				montarItensEntrega();
				setNavegacaoDeMapaRequisicao(true);
				setMensagemID("msg_entre_dados");
			}else {
				setNavegacaoDeMapaRequisicao(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("requisicao");
		}
	}

	public String realizarNavegacaoParaMapaRequisicao() {
		try {
			removerControleMemoriaFlash("MapaRequisicaoControle");
			removerControleMemoriaTela("MapaRequisicaoControle");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaRequisicaoCons.xhtml");
	}

	public String novo() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EntregaRequisicaoControle", "Novo Entrega Requisição", "Novo");
			removerObjetoMemoria(this);
			setEntregaRequisicaoVO(new EntregaRequisicaoVO());
			setEntregaRequisicaoItemVO(new EntregaRequisicaoItemVO());
			getEntregaRequisicaoVO().setResponsavel(getUsuarioLogadoClone());
			setNavegacaoDeMapaRequisicao(false);
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("entregaRequisicaoForm");
	}

	public String editar() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "EntregaRequisicaoControle", "Inicializando Editar Entrega Requisição", "Editando");
		EntregaRequisicaoVO obj = (EntregaRequisicaoVO) context().getExternalContext().getRequestMap().get("entregaRequisicaoIten");
		setEntregaRequisicaoVO(getFacadeFactory().getEntregaRequisicaoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		setEntregaRequisicaoItemVO(new EntregaRequisicaoItemVO());
		registrarAtividadeUsuario(getUsuarioLogado(), "EntregaRequisicaoControle", "Finalizando Editar Entrega Requisição", "Editando");
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("entregaRequisicaoForm");
	}

	public String gravar() {
		try {
			if (!Uteis.isAtributoPreenchido(getEntregaRequisicaoVO())) {
				if (!getFacadeFactory().getRequisicaoFacade().consultarSituacaoAutorizacaoPorCodigo(getEntregaRequisicaoVO().getRequisicao().getCodigo(), false, getUsuarioLogado()).equals("AU")) {
					getEntregaRequisicaoVO().getEntregaRequisicaoItemVOs().clear();
					throw new ConsistirException("Esta Requisição não está Autorizada.");
				}
				registrarAtividadeUsuario(getUsuarioLogado(), "EntregaRequisicaoControle", "Inicializando Incluir Entrega Requisição", "Incluindo");
				getFacadeFactory().getEntregaRequisicaoFacade().incluir(entregaRequisicaoVO, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "EntregaRequisicaoControle", "Finalizando Incluir Entrega Requisição", "Incluindo");
			} else {
				throw new Exception("Uma Entrega de Requisição NÃO pode ser alterada.");
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			try {
				getFacadeFactory().getEntregaRequisicaoFacade().atualizarRequisicaoItem(entregaRequisicaoVO, getUsuarioLogado());
			} catch (Exception e2) {
				setMensagemDetalhada("msg_erro", e2.getMessage());
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("entregaRequisicaoForm");
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EntregaRequisicaoControle", "Inicializando Consultar Entrega Requisição", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getEntregaRequisicaoFacade().consultar(getUnidadeEnsinoLogado().getCodigo(), getControleConsultaOtimizado());
			registrarAtividadeUsuario(getUsuarioLogado(), "EntregaRequisicaoControle", "Finalizando Consultar Entrega Requisição", "Consultando");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("entregaRequisicaoCons");
	}

	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EntregaRequisicaoControle", "Inicializando Excluir Entrega Requisição", "Excluindo");
			if (Uteis.isAtributoPreenchido(getEntregaRequisicaoVO())) {
				getFacadeFactory().getEntregaRequisicaoFacade().excluir(entregaRequisicaoVO, getUsuarioLogado());
			}
			setEntregaRequisicaoVO(new EntregaRequisicaoVO());
			setEntregaRequisicaoItemVO(new EntregaRequisicaoItemVO());
			getEntregaRequisicaoVO().setResponsavel(getUsuarioLogadoClone());
			registrarAtividadeUsuario(getUsuarioLogado(), "EntregaRequisicaoControle", "Finalizando Excluir Entrega Requisição", "Excluindo");
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			try {
				getFacadeFactory().getEntregaRequisicaoFacade().atualizarRequisicaoItem(entregaRequisicaoVO, getUsuarioLogado());
			} catch (Exception e2) {
				setMensagemDetalhada("msg_erro", e2.getMessage());
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("entregaRequisicaoForm");
	}

	public void visualizarListaEstoque() {
		try {
			setEntregaRequisicaoItemVO((EntregaRequisicaoItemVO) context().getExternalContext().getRequestMap().get("entregaRequisicaoIten"));
			setListaEstoqueVOs(getFacadeFactory().getEstoqueFacade().consultarPorProdutoPorUnidadeEnsinoAgrupado(getEntregaRequisicaoItemVO().getRequisicaoItem().getProdutoServico().getCodigo(), 0, false, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarEstoque() {
		try {
			EstoqueVO obj = (EstoqueVO) context().getExternalContext().getRequestMap().get("estoqueItem");
			Uteis.checkState(getEntregaRequisicaoItemVO().getRequisicaoItem().getQuantidadeAutorizada() > obj.getQuantidade(), "Não é possível selecionar a unidade ensino, pois a mesma não tem a quantidade necessária em estoque.");
			getEntregaRequisicaoItemVO().getRequisicaoItem().setQuantidadeEstoque(obj.getQuantidade());
			getEntregaRequisicaoItemVO().getRequisicaoItem().setUnidadeEnsinoEstoqueRetirada(obj.getUnidadeEnsino());
			adicionarEntregaRequisicaoItem();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarVerificacaoUsuarioLiberacaoEstoqueRetirada() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_ESTOQUE_RETIRADA, usuarioVerif);
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.REQUISICAO, getEntregaRequisicaoVO().getRequisicao().getCodigo().toString(), OperacaoFuncionalidadeEnum.REQUISICAO_LIBERAR_ALTERACAO_ESTOQUE_RETIRADA, usuarioVerif, ""));
			setLiberadoAlteracaoEstoqueRetidada(true);
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setLiberadoAlteracaoEstoqueRetidada(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void carregarDadosLiberarFuncionalidade() {
		try {
			setUserNameLiberarFuncionalidade("");
			setSenhaLiberarFuncionalidade("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void montarItensEntrega() throws Exception {
		getEntregaRequisicaoVO().setEntregaRequisicaoItemVOs(new ArrayList<>(0));
		Iterator<RequisicaoItemVO> i = getEntregaRequisicaoVO().getRequisicao().getRequisicaoItemVOs().iterator();
		while (i.hasNext()) {
			RequisicaoItemVO obj = i.next();

			if (((Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoRequisicaoEnum()) && obj.getTipoAutorizacaoRequisicaoEnum().isRetirada())
					|| Uteis.isAtributoPreenchido(obj.getCompraItemVO()))
					&& obj.getQuantidadeEntregue().doubleValue() < obj.getQuantidadeAutorizada()) {
				getEntregaRequisicaoItemVO().setRequisicaoItem(obj);
				DepartamentoVO dep = getFacadeFactory().getDepartamentoFacade().consultarDepartamentoControlaEstoquePorUnidadeEnsino(getEntregaRequisicaoItemVO().getRequisicaoItem().getUnidadeEnsinoEstoqueRetirada().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getEntregaRequisicaoItemVO().setCentroResultadoEstoque(dep.getCentroResultadoVO());
				adicionarEntregaRequisicaoItem();
			}
		}
		if(getEntregaRequisicaoVO().getEntregaRequisicaoItemVOs().isEmpty()) {
			Iterator<RequisicaoItemVO> iterator = getEntregaRequisicaoVO().getRequisicao().getRequisicaoItemVOs().iterator();
			while (iterator.hasNext()) {				
				RequisicaoItemVO obj = iterator.next();
				if((Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoRequisicaoEnum()) && !obj.getTipoAutorizacaoRequisicaoEnum().isRetirada())
					&& !Uteis.isAtributoPreenchido(obj.getCompraItemVO())) {
					throw new Exception("Os item da Requisição que foram Autorizados para Cotação/Compra Direta ainda não foram realizados!");
				}
				else if(obj.getQuantidadeAutorizada() == 0.0) {
					throw new Exception("A quantidade autorizada para a requisição deve ser maior que ZERO!");
				}
				else if(obj.getQuantidadeEntregue().doubleValue() == obj.getQuantidadeAutorizada()) {
					throw new Exception("Já foi entregue a quantidade autorizada para essa requisição!");
				}
			}
		}
		//Uteis.checkState(getEntregaRequisicaoVO().getEntregaRequisicaoItemVOs().isEmpty(), "Não foi encontrado nenhum item da requisição para ser gerado um item de entrega da Requisição");
	}

	public void adicionarEntregaRequisicaoItem() {
		try {
			if (!getEntregaRequisicaoVO().getCodigo().equals(0)) {
				getEntregaRequisicaoItemVO().setEntregaRequisicaoVO(getEntregaRequisicaoVO());
			}
			getEntregaRequisicaoVO().adicionarObjEntregaRequisicaoItemVOs(getEntregaRequisicaoItemVO());
			this.setEntregaRequisicaoItemVO(new EntregaRequisicaoItemVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerEntregaRequisicaoItem() {
		try {
			EntregaRequisicaoItemVO obj = (EntregaRequisicaoItemVO) context().getExternalContext().getRequestMap().get("entregaRequisicaoIten");
			getEntregaRequisicaoVO().excluirObjEntregaRequisicaoItemVOs(obj.getRequisicaoItem().getCodigo());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarRequisicaoPorChavePrimaria() {
		try {
			if (Uteis.isAtributoPreenchido(getEntregaRequisicaoVO().getRequisicao())) {
				RequisicaoVO obj = getFacadeFactory().getRequisicaoFacade().consultarPorChavePrimaria(getEntregaRequisicaoVO().getRequisicao().getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
				RequisicaoVO.validarDadosParaNaEntregaRequisicao(obj);
				this.getEntregaRequisicaoVO().setRequisicao(obj);
				montarItensEntrega();
				setMensagemID("msg_dados_selecionados");
			} else {
				getEntregaRequisicaoVO().setRequisicao(new RequisicaoVO());
				getEntregaRequisicaoVO().getEntregaRequisicaoItemVOs().clear();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarRequisicao() {
		try {
			RequisicaoVO obj = (RequisicaoVO) context().getExternalContext().getRequestMap().get("requisicaoIten");
			this.getEntregaRequisicaoVO().setRequisicao(getFacadeFactory().getRequisicaoFacade().consultarPorChavePrimaria(obj.getCodigo(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			RequisicaoVO.validarDadosParaNaEntregaRequisicao(getEntregaRequisicaoVO().getRequisicao());
			montarItensEntrega();
			this.listaConsultaRequisicao.clear();
			this.valorConsultaRequisicao = null;
			this.campoConsultaRequisicao = null;
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboRequisicao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Nr.Requisição"));
		itens.add(new SelectItem("nomeUsuario", "Requisitante"));
		itens.add(new SelectItem("nomeDepartamento", "Departamento"));
		return itens;
	}

	public void consultarRequisicao() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaRequisicao().equals("codigo")) {
				if (getValorConsultaRequisicao().equals("")) {
					setValorConsultaRequisicao("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaRequisicao());
				objs = getFacadeFactory().getRequisicaoFacade().consultarPorCodigo(new Integer(valorInt), null, null, "PE", "PA", "", "AU", this.getUnidadeEnsinoLogado().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisicao().equals("nomeUsuario")) {
				objs = getFacadeFactory().getRequisicaoFacade().consultarPorNomeUsuario(getValorConsultaRequisicao(), null, null, "PE", "PA", "", "AU", this.getUnidadeEnsinoLogado().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaRequisicao().equals("nomeDepartamento")) {
				objs = getFacadeFactory().getRequisicaoFacade().consultarPorNomeDepartamento(getValorConsultaRequisicao(), null, null, "PE", "PA", "", "AU", this.getUnidadeEnsinoLogado().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaRequisicao(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaRequisicao(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarDadoConsultaCentroResultadoAdministrativo() {
		try {
			setEntregaRequisicaoItemVO((EntregaRequisicaoItemVO) context().getExternalContext().getRequestMap().get("entregaRequisicaoIten"));
			inicializarDadosComunsCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}	

	private void inicializarDadosComunsCentroResultado() {
		setCentroResultadoDataModelo(new DataModelo());
		getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			getEntregaRequisicaoItemVO().setCentroResultadoEstoque(obj);
			adicionarEntregaRequisicaoItem();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerCentroResultado(DataScrollEvent dataScrollerEvent) {
		try {
			getCentroResultadoDataModelo().setPaginaAtual(dataScrollerEvent.getPage());
			getCentroResultadoDataModelo().setPage(dataScrollerEvent.getPage());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroResultado() {
		try {
			super.consultar();
			getCentroResultadoDataModelo().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, true, getEntregaRequisicaoVO().getRequisicao().getDepartamento(), getEntregaRequisicaoVO().getRequisicao().getCurso(), getEntregaRequisicaoVO().getRequisicao().getTurma(), getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getCampoConsultaRequisicao() {
		return campoConsultaRequisicao;
	}

	public void setCampoConsultaRequisicao(String campoConsultaRequisicao) {
		this.campoConsultaRequisicao = campoConsultaRequisicao;
	}

	public List<SelectItem> getListaConsultaRequisicao() {
		return listaConsultaRequisicao;
	}

	public void setListaConsultaRequisicao(List<SelectItem> listaConsultaRequisicao) {
		this.listaConsultaRequisicao = listaConsultaRequisicao;
	}

	public String getValorConsultaRequisicao() {
		return valorConsultaRequisicao;
	}

	public void setValorConsultaRequisicao(String valorConsultaRequisicao) {
		this.valorConsultaRequisicao = valorConsultaRequisicao;
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
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigoRequisicao", "Nr. Requisição"));
		itens.add(new SelectItem("codigo", "Código Entrega"));
		itens.add(new SelectItem("nomeUsuario", "Responsável"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<SelectItem>(0));
		consultar();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("entregaRequisicaoCons");
	}

	public EntregaRequisicaoItemVO getEntregaRequisicaoItemVO() {
		return entregaRequisicaoItemVO;
	}

	public void setEntregaRequisicaoItemVO(EntregaRequisicaoItemVO entregaRequisicaoItemVO) {
		this.entregaRequisicaoItemVO = entregaRequisicaoItemVO;
	}

	public EntregaRequisicaoVO getEntregaRequisicaoVO() {
		return entregaRequisicaoVO;
	}

	public void setEntregaRequisicaoVO(EntregaRequisicaoVO entregaRequisicaoVO) {
		this.entregaRequisicaoVO = entregaRequisicaoVO;
	}

	public boolean isNavegacaoDeMapaRequisicao() {
		return navegacaoDeMapaRequisicao;
	}

	public void setNavegacaoDeMapaRequisicao(boolean navegacaoDeMapaRequisicao) {
		this.navegacaoDeMapaRequisicao = navegacaoDeMapaRequisicao;
	}

	public List<EstoqueVO> getListaEstoqueVOs() {
		listaEstoqueVOs = Optional.ofNullable(listaEstoqueVOs).orElse(new ArrayList<>());
		return listaEstoqueVOs;
	}

	public void setListaEstoqueVOs(List<EstoqueVO> listaEstoqueVOs) {
		this.listaEstoqueVOs = listaEstoqueVOs;
	}

	public String getUserNameLiberarFuncionalidade() {
		if (userNameLiberarFuncionalidade == null) {
			userNameLiberarFuncionalidade = "";
		}
		return userNameLiberarFuncionalidade;
	}

	public void setUserNameLiberarFuncionalidade(String userNameLiberarValorAcimaPrevisto) {
		this.userNameLiberarFuncionalidade = userNameLiberarValorAcimaPrevisto;
	}

	public String getSenhaLiberarFuncionalidade() {
		if (senhaLiberarFuncionalidade == null) {
			senhaLiberarFuncionalidade = "";
		}
		return senhaLiberarFuncionalidade;
	}

	public void setSenhaLiberarFuncionalidade(String senhaLiberarValorAcimaPrevisto) {
		this.senhaLiberarFuncionalidade = senhaLiberarValorAcimaPrevisto;
	}

	public boolean isLiberadoAlteracaoEstoqueRetidada() {
		return liberadoAlteracaoEstoqueRetidada;
	}

	public void setLiberadoAlteracaoEstoqueRetidada(boolean liberadoAlteracaoEstoqueRetidada) {
		this.liberadoAlteracaoEstoqueRetidada = liberadoAlteracaoEstoqueRetidada;
	}
	
	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public boolean isPermiteAlterarEstoqueRetirada() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_ESTOQUE_RETIRADA, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		entregaRequisicaoVO = null;
		entregaRequisicaoItemVO = null;
		campoConsultaRequisicao = null;
		valorConsultaRequisicao = null;
		Uteis.liberarListaMemoria(listaConsultaRequisicao);
	}
}
