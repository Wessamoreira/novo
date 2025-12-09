package controle.compras;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoComprasEnum;
import negocio.comuns.compras.EstoqueVO;
import negocio.comuns.compras.MapaRequisicaoVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("MapaRequisicaoControle")
@Scope("viewScope")
@Lazy
public class MapaRequisicaoControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3463835342637413361L;
	private List<MapaRequisicaoVO> listaMapaRequisicao;
	private MapaRequisicaoVO mapaRequisicao;
	private RequisicaoVO requisicaoFiltro;
	private RequisicaoItemVO requisicaoItemVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private Date dataIni;
	private Date dataFim;
	private List<EstoqueVO> listaEstoqueVOs;
	private Double saldoAtualMes;
	private String userNameLiberarFuncionalidade;
	private String senhaLiberarFuncionalidade;
	private Boolean saldoInsuficientePlanoOrcamentario;
	private Boolean requisicaoAutorizadaComSucesso;
	private boolean liberadoAlteracaoEstoqueRetidada = false;
	private TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum;

	@PostConstruct
	public void postConstruct() {
		inicializarDadosControlador();
		consultar();
	}

	public void inicializarDadosControlador() {
		removerObjetoMemoria(this);
		setListaMapaRequisicao(new ArrayList<>(0));
		setMapaRequisicao(new MapaRequisicaoVO());
		setRequisicaoFiltro(new RequisicaoVO());
		getRequisicaoFiltro().setSituacaoEntrega("");
		getRequisicaoFiltro().setCodigo(null);
		getRequisicaoFiltro().setDataRequisicao(null);
		getRequisicaoFiltro().setSituacaoTipoAutorizacaoRequisicaoEnum("");
		setDataIni(new Date());
		setLiberadoAlteracaoEstoqueRetidada(false);
		setDataFim(Uteis.getNewDateComUmMesAMais());
		montarListaSelectItemUnidadeEnsino();
	}

	public void limparCamposConsultar() {
		setListaMapaRequisicao(new ArrayList<>());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
	}

	@Override
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "MapaRequisicaoControle", "Inicializando Consultar Mapa Requisição", "Consultando");
			setListaMapaRequisicao(getFacadeFactory().getMapaRequisicaoFacade().consultar(getRequisicaoFiltro(), getDataIni(), getDataFim(), true, getUsuarioLogado()));
			registrarAtividadeUsuario(getUsuarioLogado(), "MapaRequisicaoControle", "Finalizando Consultar Mapa Requisição", "Consultando");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaMapaRequisicao(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaRequisicaoCons.xhtml");
	}

	public String realizarNavegacaoParaEntregaRequisicao() {
		try {
			MapaRequisicaoVO obj = (MapaRequisicaoVO) context().getExternalContext().getRequestMap().get("mapaItem");
			obj.setRequisicao(getFacadeFactory().getRequisicaoFacade().consultarPorChavePrimaria(obj.getRequisicao().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			Uteis.checkState(!obj.getRequisicao().getAutorizado(), "Não é possível realizar uma entrega para uma requisição que não esteja Autorizada.");
			//Uteis.checkState(!obj.getRequisicao().getTipoAutorizacaoRequisicaoEnum().isRetirada(), "Não é possível realizar uma entrega para uma requisição que não seja do tipo Autorizada Retirada.");
			context().getExternalContext().getSessionMap().put("requisicao", obj.getRequisicao());
			removerControleMemoriaFlash("EntregaRequisicaoControle");
			removerControleMemoriaTela("EntregaRequisicaoControle");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("entregaRequisicaoForm.xhtml");
	}

	public String autorizarTodas() {
		try {
			if (Uteis.isAtributoPreenchido(getListaMapaRequisicao())) {
				getFacadeFactory().getMapaRequisicaoFacade().autorizarTodas(getListaMapaRequisicao(), getTipoAutorizacaoRequisicaoEnum(), getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getListaMapaRequisicao().get(0).getRequisicao().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			}
			consultar();
			setSaldoInsuficientePlanoOrcamentario(getMapaRequisicao().getRequisicao().getSaldoPlanoOrcamentarioInsuficiente());
			setMensagemID("msg_mapaRequisicao_autorizadoTodos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaRequisicaoCons");
	}

	public void autorizar() {
		try {
			getFacadeFactory().getMapaRequisicaoFacade().autorizar(getMapaRequisicao(), this.getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMapaRequisicao().getRequisicao().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			consultar();
			setSaldoInsuficientePlanoOrcamentario(getMapaRequisicao().getRequisicao().getSaldoPlanoOrcamentarioInsuficiente());
			setRequisicaoAutorizadaComSucesso(Boolean.TRUE);
			setMensagemID("msg_mapaRequisicao_autorizado");
		} catch (Exception e) {
			//getMapaRequisicao().getRequisicao().setSituacaoAutorizacao("PE");
			setRequisicaoAutorizadaComSucesso(Boolean.FALSE);
			setSaldoInsuficientePlanoOrcamentario(getMapaRequisicao().getRequisicao().getSaldoPlanoOrcamentarioInsuficiente());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void indeferir() {
		try {
			MapaRequisicaoVO.validarDadosIndeferir(getMapaRequisicao());
			getFacadeFactory().getMapaRequisicaoFacade().indeferir(getMapaRequisicao(), this.getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMapaRequisicao().getRequisicao().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			consultar();
			setSaldoInsuficientePlanoOrcamentario(getMapaRequisicao().getRequisicao().getSaldoPlanoOrcamentarioInsuficiente());
			setRequisicaoAutorizadaComSucesso(Boolean.TRUE);
			setMensagemID("msg_mapaRequisicao_indeferido");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void desfazerAutorizacao() {
		try {
			getMapaRequisicao().getRequisicao().getRequisicaoItemVOs().forEach(this::validarItensRequisicoes);
			getFacadeFactory().getMapaRequisicaoFacade().desfazerAutorizar(getMapaRequisicao(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMapaRequisicao().getRequisicao().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			getMapaRequisicao().getRequisicao().setSituacaoAutorizacao("AU");
			
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	private void validarItensRequisicoes (RequisicaoItemVO requisicaoItemVO) throws StreamSeiException {
		try {
			RequisicaoItemVO.validarDados(requisicaoItemVO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public void cancelarAutorizacao() {
		try {
			getFacadeFactory().getRequisicaoFacade().verificarBloqueioRequisicao(Uteis.BLOQUEIO_GERAR, false, getMapaRequisicao().getRequisicao());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void abrirModalAprovacao() {
		try {
			MapaRequisicaoVO obj = (MapaRequisicaoVO) context().getExternalContext().getRequestMap().get("mapaItem");
			setSaldoInsuficientePlanoOrcamentario(false);
			setMapaRequisicao(obj);
			getFacadeFactory().getRequisicaoFacade().verificarBloqueioRequisicao(Uteis.BLOQUEIO_GERAR, true, obj.getRequisicao());
			getMapaRequisicao().setRequisicao(getFacadeFactory().getRequisicaoFacade().consultarPorChavePrimaria(getMapaRequisicao().getRequisicao().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			//if (!Uteis.isAtributoPreenchido(getMapaRequisicao().getRequisicao().getTipoAutorizacaoRequisicaoEnum())) {
			//	getMapaRequisicao().getRequisicao().setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum.COTACAO);
			//}
			/*if(getMapaRequisicao().getRequisicao().getRequisicaoItemVOs().stream().anyMatch(p-> p.getTipoAutorizacaoRequisicaoEnum().isNenhum())) {
				getMapaRequisicao().getRequisicao().getRequisicaoItemVOs().stream().forEach(p->p.setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum.RETIRADA));	
			}*/
			
			if(getMapaRequisicao().getRequisicao().isSituacaoAutorizacaoPendente()){
				getMapaRequisicao().setarQtdeAprovar();	
			}
			setSaldoAtualMes(getFacadeFactory().getPlanoOrcamentarioFacade().executarCalculoSaldoAtualMes(getMapaRequisicao().getRequisicao(), getMapaRequisicao().getRequisicao().getDataRequisicao(), getMapaRequisicao().getRequisicao().getDepartamento().getCodigo(), getMapaRequisicao().getRequisicao().getUnidadeEnsino().getCodigo()));
			setRequisicaoAutorizadaComSucesso(Boolean.FALSE);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void visualizarListaEstoque() {
		try {
			setRequisicaoItemVO((RequisicaoItemVO) context().getExternalContext().getRequestMap().get("requisicaoIten"));
			setListaEstoqueVOs(getFacadeFactory().getEstoqueFacade().consultarPorProdutoPorUnidadeEnsinoAgrupado(getRequisicaoItemVO().getProdutoServico().getCodigo(), 0, false, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void indeferirRequisicaoItem() {
		try {
			RequisicaoItemVO obj = (RequisicaoItemVO) context().getExternalContext().getRequestMap().get("requisicaoIten");
			obj.indeferirRequisicaoItem();
			inicializarMensagemVazia();
			setMensagem(UteisJSF.internacionalizar("msg_mapaRequisicao_indeferidoRequisicaoItem").replace("{0}", obj.getProdutoServico().getNome()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void autorizarRequisicaoItem() {
		try {
			RequisicaoItemVO obj = (RequisicaoItemVO) context().getExternalContext().getRequestMap().get("requisicaoIten");
			obj.autorizarRequisicaoItem();
			inicializarMensagemVazia();
			setMensagem(UteisJSF.internacionalizar("msg_mapaRequisicao_autorizadoRequisicaoItem").replace("{0}", obj.getProdutoServico().getNome()));
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarEstoque() {
		try {
			EstoqueVO obj = (EstoqueVO) context().getExternalContext().getRequestMap().get("estoqueItem");
			Uteis.checkState(getRequisicaoItemVO().getQuantidadeAutorizada() > obj.getQuantidade(), "Não é possível selecionar a unidade ensino, pois a mesma não tem a quantidade necessária em estoque.");
			getRequisicaoItemVO().setQuantidadeEstoque(obj.getQuantidade());
			getRequisicaoItemVO().setUnidadeEnsinoEstoqueRetirada(obj.getUnidadeEnsino());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
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

	public void realizarVerificacaoUusuarioLiberacaoValorAcimaPrevisto() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LiberarValorAcimaPrevistoPlanoOrcamentario", usuarioVerif);
			getMapaRequisicao().getRequisicao().setValorAcimaPrevistoAutorizado(Boolean.TRUE);
			getMapaRequisicao().getRequisicao().setSaldoPlanoOrcamentarioInsuficiente(Boolean.FALSE);
			setMensagemID("msg_LiberacaoValorAcimaPrevistoPlanoOrcamentario");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarVerificacaoUsuarioLiberacaoEstoqueRetirada() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_ESTOQUE_RETIRADA, usuarioVerif);
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.REQUISICAO, getMapaRequisicao().getRequisicao().getCodigo().toString(), OperacaoFuncionalidadeEnum.REQUISICAO_LIBERAR_ALTERACAO_ESTOQUE_RETIRADA, usuarioVerif, ""));
			setLiberadoAlteracaoEstoqueRetidada(true);
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setLiberadoAlteracaoEstoqueRetidada(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void realizarVerificacaoUsuarioLiberacaoCompraDireta() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarFuncionalidade(), this.getSenhaLiberarFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_LIBERAR_VALOR_MAXIMO_COMPRA_DIRETA, usuarioVerif);
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.REQUISICAO, getMapaRequisicao().getRequisicao().getCodigo().toString(), OperacaoFuncionalidadeEnum.REQUISICAO_LIBERAR_VALOR_MAXIMO_COMPRA_DIRETA, usuarioVerif, ""));
			getMapaRequisicao().getRequisicao().setLiberadoValorMaximoCompraDireta(true);
			getMapaRequisicao().getRequisicao().setHabilitarBotaoLiberacaoValorMaximoCompraDiretaRequisicao(false);
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			getMapaRequisicao().getRequisicao().setLiberadoValorMaximoCompraDireta(false);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			getListaSelectItemUnidadeEnsino().clear();
			if (isPermiteVisualizarTodasUnidade()) {
				List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			} else if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<>());
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				getRequisicaoFiltro().setUnidadeEnsino(obj);
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public boolean isPermiteVisualizarTodasUnidade() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_VISUALIZAR_TODAS_UNIDADES_ENSINO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isPermiteAlterarEstoqueRetirada() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_ESTOQUE_RETIRADA, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isPermiteDesfazerAutorizacaoRequisicao() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_DESFAZER_AUTORIZACAO_REQUISICAO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isPermiteAutorizarIndeferirRequisicao() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.AUTORIZAR_MAPA_REQUISICAO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<SelectItem> getListaSelectItemSituacaoAutorizacao() {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem("AU", "Autorizado"));
		objs.add(new SelectItem("PE", "Pendente"));
		objs.add(new SelectItem("IN", "Indeferido"));
		return objs;
	}

	public List<SelectItem> getListaSelectItemSituacaoEntrega() {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem("", "Todas"));
		objs.add(new SelectItem("PE", "Pendente"));
		objs.add(new SelectItem("PA", "Parcial"));
		objs.add(new SelectItem("PEPA", "Pendente/Parcial"));
		objs.add(new SelectItem("FI", "Entregue"));
		return objs;
	}
	
	public List<SelectItem> getListaSelectItemTipoAutorizacaoRequisicaoEnum() {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem(TipoAutorizacaoRequisicaoEnum.NENHUM, "Todas"));
		objs.add(new SelectItem(TipoAutorizacaoRequisicaoEnum.COTACAO, "Cotação"));
		objs.add(new SelectItem(TipoAutorizacaoRequisicaoEnum.COMPRA_DIRETA, "Compra Direta"));
		objs.add(new SelectItem(TipoAutorizacaoRequisicaoEnum.RETIRADA, "Retirada"));
		return objs;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoTipoAutorizacaoRequisicaoEnum() {
		List<SelectItem> objs = new ArrayList<>(0);
		if(Uteis.isAtributoPreenchido(getRequisicaoFiltro().getFiltroTipoAutorizacaoRequisicaoEnum()) && getRequisicaoFiltro().getFiltroTipoAutorizacaoRequisicaoEnum().equals(TipoAutorizacaoRequisicaoEnum.COTACAO.name())) {
			objs.add(new SelectItem("AC", "Aguardando Cotação"));			
			objs.add(new SelectItem("CO", "Cotado"));			
		}else if(Uteis.isAtributoPreenchido(getRequisicaoFiltro().getFiltroTipoAutorizacaoRequisicaoEnum()) && getRequisicaoFiltro().getFiltroTipoAutorizacaoRequisicaoEnum().equals(TipoAutorizacaoRequisicaoEnum.COMPRA_DIRETA.name())) {
			objs.add(new SelectItem("AD", "Aguardando Compra Direta"));			
			objs.add(new SelectItem("CD", "Comprado"));
		}
		return objs;
	}
	
	public void limparCamposSituacaoTipoAutorizacaoRequisicaoEnum() {
			getRequisicaoFiltro().setSituacaoTipoAutorizacaoRequisicaoEnum("");
	}

	public List<MapaRequisicaoVO> getListaMapaRequisicao() {
		return listaMapaRequisicao;
	}

	public void setListaMapaRequisicao(List<MapaRequisicaoVO> listaMapaRequisicao) {
		this.listaMapaRequisicao = listaMapaRequisicao;
	}

	public MapaRequisicaoVO getMapaRequisicao() {
		return mapaRequisicao;
	}

	public void setMapaRequisicao(MapaRequisicaoVO mapaRequisicao) {
		this.mapaRequisicao = mapaRequisicao;
	}

	public Double getSaldoAtualMes() {
		if (saldoAtualMes == null) {
			saldoAtualMes = 0.0;
		}
		return saldoAtualMes;
	}

	public void setSaldoAtualMes(Double saldoAtualMes) {
		this.saldoAtualMes = saldoAtualMes;
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

	public Boolean getApresentarBotaoLiberarValorAcimaPrevisto() {
		if (getSaldoInsuficientePlanoOrcamentario()) {
			return true;
		}
		return false;
	}

	public Boolean getSaldoInsuficientePlanoOrcamentario() {
		if (saldoInsuficientePlanoOrcamentario == null) {
			saldoInsuficientePlanoOrcamentario = Boolean.FALSE;
		}
		return saldoInsuficientePlanoOrcamentario;
	}

	public void setSaldoInsuficientePlanoOrcamentario(Boolean saldoInsuficientePlanoOrcamentario) {
		this.saldoInsuficientePlanoOrcamentario = saldoInsuficientePlanoOrcamentario;
	}

	public Boolean getRequisicaoAutorizadaComSucesso() {
		if (requisicaoAutorizadaComSucesso == null) {
			requisicaoAutorizadaComSucesso = Boolean.FALSE;
		}
		return requisicaoAutorizadaComSucesso;
	}

	public void setRequisicaoAutorizadaComSucesso(Boolean requisicaoAutorizadaComSucesso) {
		this.requisicaoAutorizadaComSucesso = requisicaoAutorizadaComSucesso;
	}

	public RequisicaoVO getRequisicaoFiltro() {
		requisicaoFiltro = Optional.ofNullable(requisicaoFiltro).orElse(new RequisicaoVO());
		return requisicaoFiltro;
	}

	public void setRequisicaoFiltro(RequisicaoVO requisicaoFiltro) {
		this.requisicaoFiltro = requisicaoFiltro;
	}

	public RequisicaoItemVO getRequisicaoItemVO() {
		requisicaoItemVO = Optional.ofNullable(requisicaoItemVO).orElse(new RequisicaoItemVO());
		return requisicaoItemVO;
	}

	public void setRequisicaoItemVO(RequisicaoItemVO requisicaoItemVO) {
		this.requisicaoItemVO = requisicaoItemVO;
	}

	public Date getDataIni() {
		return dataIni;
	}

	public void setDataIni(Date dataIni) {
		this.dataIni = dataIni;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
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
	

	public TipoAutorizacaoRequisicaoEnum getTipoAutorizacaoRequisicaoEnum() {
		tipoAutorizacaoRequisicaoEnum = Optional.ofNullable(tipoAutorizacaoRequisicaoEnum).orElse(TipoAutorizacaoRequisicaoEnum.COTACAO);
		return tipoAutorizacaoRequisicaoEnum;
	}

	public void setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum) {
		this.tipoAutorizacaoRequisicaoEnum = tipoAutorizacaoRequisicaoEnum;
	}

	public List<EstoqueVO> getListaEstoqueVOs() {
		listaEstoqueVOs = Optional.ofNullable(listaEstoqueVOs).orElse(new ArrayList<>());
		return listaEstoqueVOs;
	}

	public void setListaEstoqueVOs(List<EstoqueVO> listaEstoqueVOs) {
		this.listaEstoqueVOs = listaEstoqueVOs;
	}

	public boolean isLiberadoAlteracaoEstoqueRetidada() {
		return liberadoAlteracaoEstoqueRetidada;
	}

	public void setLiberadoAlteracaoEstoqueRetidada(boolean liberadoAlteracaoEstoqueRetidada) {
		this.liberadoAlteracaoEstoqueRetidada = liberadoAlteracaoEstoqueRetidada;
	}

	public boolean isHabilitarNavegacaoEntregaRequisicao() {
		try {
			ControleAcesso.incluir("EntregaRequisicao", true, getUsuarioLogado());
			return getRequisicaoFiltro().getAutorizado();
		} catch (Exception e) {
			return false;
		}
	}
	
	public void downloadArquivoRequisicao() throws Exception {
		try {
		
			String arquivo = (getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO + File.separator  + getMapaRequisicao().getRequisicao().getArquivoVO().getCpfAlunoDocumentacao() + File.separator + getMapaRequisicao().getRequisicao().getArquivoVO().getNome());
			InputStream fs = new FileInputStream(arquivo);
			
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", getMapaRequisicao().getRequisicao().getArquivoVO().getNome());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.substring(0, arquivo.lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

}
