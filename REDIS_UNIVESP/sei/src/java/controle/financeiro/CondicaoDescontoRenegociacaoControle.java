package controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.CondicaoDescontoRenegociacaoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("CondicaoDescontoRenegociacaoControle")
@Scope("viewScope")
public class CondicaoDescontoRenegociacaoControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -305537181471979485L;
	private CondicaoDescontoRenegociacaoVO condicaoDescontoRenegociacaoVO;
	private ItemCondicaoDescontoRenegociacaoVO itemCondicaoDescontoRenegociacaoVO;
	private ItemCondicaoDescontoRenegociacaoVO itemCondicaoDescontoRenegociacaoTempVO;

	public CondicaoDescontoRenegociacaoControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}

	public String novo() {
		try {
			setCondicaoDescontoRenegociacaoVO(new CondicaoDescontoRenegociacaoVO());
			setItemCondicaoDescontoRenegociacaoVO(getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().inicializarDadosCondicaoRenegociacaoNovo(getUsuarioLogado()));
			getUnidadeEnsinoVOs().clear();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoDescontoRenegociacaoForm.xhtml");
	}

	public String editar() {
		try {
			CondicaoDescontoRenegociacaoVO obj = (CondicaoDescontoRenegociacaoVO) context().getExternalContext().getRequestMap().get("condicaoDescontoRenegociacaoItens");
			setCondicaoDescontoRenegociacaoVO(getFacadeFactory().getCondicaoDescontoRenegociacaoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setItemCondicaoDescontoRenegociacaoVO(getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().inicializarDadosCondicaoRenegociacaoNovo(getUsuarioLogado()));
			getUnidadeEnsinoVOs().clear();
			setMensagemID("msg_dados_editar", Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoDescontoRenegociacaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_dados_erro", e.getMessage(), Uteis.ALERTA);
			return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoDescontoRenegociacaoCons.xhtml");
		}
	}

	public String persistir() {
		try {
			getFacadeFactory().getCondicaoDescontoRenegociacaoFacade().persistir(getCondicaoDescontoRenegociacaoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoDescontoRenegociacaoForm");
		}
	}

	public void ativarItemCondicaoDescontoRenegociacao() {
		try {
			ItemCondicaoDescontoRenegociacaoVO obj = (ItemCondicaoDescontoRenegociacaoVO) context().getExternalContext().getRequestMap().get("itemCondicaoDescontoRenegociacaoItens");
			getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().ativarItemCondicaoDescontoRenegociacaoVO(obj, true, getUsuarioLogado());
			if(Uteis.isAtributoPreenchido(obj)) {
				setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativarItemCondicaoDescontoRenegociacao() {
		try {
			ItemCondicaoDescontoRenegociacaoVO obj = (ItemCondicaoDescontoRenegociacaoVO) context().getExternalContext().getRequestMap().get("itemCondicaoDescontoRenegociacaoItens");
			getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().inativarItemCondicaoDescontoRenegociacaoVO(obj, true, getUsuarioLogado());
			if(Uteis.isAtributoPreenchido(obj)) {
				setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String excluir() {
		try {
			getFacadeFactory().getCondicaoDescontoRenegociacaoFacade().excluir(getCondicaoDescontoRenegociacaoVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return "";
	}

	public void adicionarItemCondicaoDescontoRenegociacao() {
		try {
			getFacadeFactory().getCondicaoDescontoRenegociacaoFacade().adicionarObjItemCondicaoDescontoRenegociacaoVOs(getCondicaoDescontoRenegociacaoVO(), getItemCondicaoDescontoRenegociacaoVO());
			setItemCondicaoDescontoRenegociacaoVO(getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().inicializarDadosCondicaoRenegociacaoNovo(getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_adicionados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarItemCondicaoDescontoRenegociacao() {
		try {
			ItemCondicaoDescontoRenegociacaoVO obj = (ItemCondicaoDescontoRenegociacaoVO) context().getExternalContext().getRequestMap().get("itemCondicaoDescontoRenegociacaoItens");
			setItemCondicaoDescontoRenegociacaoVO(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerItemCondicaoDescontoRenegociacao() {
		try {
			ItemCondicaoDescontoRenegociacaoVO obj = (ItemCondicaoDescontoRenegociacaoVO) context().getExternalContext().getRequestMap().get("itemCondicaoDescontoRenegociacaoItens");
			getFacadeFactory().getCondicaoDescontoRenegociacaoFacade().removerObjItemCondicaoDescontoRenegociacaoVOs(getCondicaoDescontoRenegociacaoVO(), obj);
			setMensagemID(MSG_TELA.msg_dados_excluidos.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@Override
	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getCondicaoDescontoRenegociacaoFacade().consultar(getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	public String inicializarConsultar() {
		setCondicaoDescontoRenegociacaoVO(new CondicaoDescontoRenegociacaoVO());
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao("condicaoDescontoRenegociacaoCons.xhtml");
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

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public ItemCondicaoDescontoRenegociacaoVO getItemCondicaoDescontoRenegociacaoVO() {
		if (itemCondicaoDescontoRenegociacaoVO == null) {
			itemCondicaoDescontoRenegociacaoVO = new ItemCondicaoDescontoRenegociacaoVO();
		}
		return itemCondicaoDescontoRenegociacaoVO;
	}

	public void setItemCondicaoDescontoRenegociacaoVO(ItemCondicaoDescontoRenegociacaoVO itemCondicaoDescontoRenegociacaoVO) {
		this.itemCondicaoDescontoRenegociacaoVO = itemCondicaoDescontoRenegociacaoVO;
	}

	public CondicaoDescontoRenegociacaoVO getCondicaoDescontoRenegociacaoVO() {
		if (condicaoDescontoRenegociacaoVO == null) {
			condicaoDescontoRenegociacaoVO = new CondicaoDescontoRenegociacaoVO();
		}
		return condicaoDescontoRenegociacaoVO;
	}

	public void setCondicaoDescontoRenegociacaoVO(CondicaoDescontoRenegociacaoVO condicaoDescontoRenegociacaoVO) {
		this.condicaoDescontoRenegociacaoVO = condicaoDescontoRenegociacaoVO;
	}

	public void consultarUnidadeEnsino() {
		try {
			ItemCondicaoDescontoRenegociacaoVO obj = (ItemCondicaoDescontoRenegociacaoVO) context().getExternalContext().getRequestMap().get("itemCondicaoDescontoRenegociacaoItens");
			setItemCondicaoDescontoRenegociacaoVO(obj);
			if (Uteis.isAtributoPreenchido(obj)) {
				consultarUnidadeEnsinoFiltroRelatorio("CondicaoDescontoRenegociacaoControle");
				getUnidadeEnsinoVOs().stream().forEach(p -> {
					boolean existeUnidade = false;
					for (ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO icdrue : obj.getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs()) {
						if (icdrue.getUnidadeEnsinoVO().getCodigo().equals(p.getCodigo())) {
							existeUnidade = true;
							break;
						}
					}
					if(!existeUnidade){
						p.setFiltrarUnidadeEnsino(false);	
					}
				});
			} else if (getUnidadeEnsinoVOs().isEmpty()) {
				consultarUnidadeEnsinoFiltroRelatorio("CondicaoDescontoRenegociacaoControle");
				setMarcarTodasUnidadeEnsino(false);
				marcarTodasUnidadesEnsinoAction();
				verificarTodasUnidadesSelecionadas();
			}else if(!getUnidadeEnsinoVOs().isEmpty() && getItemCondicaoDescontoRenegociacaoVO().getItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs().isEmpty()){
				verificarTodasUnidadesSelecionadas();	
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@Override
	public void marcarTodasUnidadesEnsinoAction() {
		try {
			for (UnidadeEnsinoVO unidadeEnsino : getUnidadeEnsinoVOs()) {
				if (getMarcarTodasUnidadeEnsino()) {
					unidadeEnsino.setFiltrarUnidadeEnsino(Boolean.TRUE);
				} else {
					unidadeEnsino.setFiltrarUnidadeEnsino(Boolean.FALSE);
				}
			}
			verificarTodasUnidadesSelecionadas();
			setMensagemID(MSG_TELA.msg_dados_adicionados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public void verificarTodasUnidadesSelecionadas() {
		try {
			for (UnidadeEnsinoVO unidadeEnsino : getUnidadeEnsinoVOs()) {
				ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO icdrue = new ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO();
				icdrue.setUnidadeEnsinoVO(unidadeEnsino);
				if (unidadeEnsino.getFiltrarUnidadeEnsino()) {
					getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().adicionarItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(getCondicaoDescontoRenegociacaoVO(), getItemCondicaoDescontoRenegociacaoVO(), icdrue);
				} else {
					getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().removerItemCondicaoDescontoRenegociacaoUnidadeEnsinoVOs(getItemCondicaoDescontoRenegociacaoVO(), icdrue);
				}
			}
			getItemCondicaoDescontoRenegociacaoVO().preencherNomeUnidadeEnsisnoSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public ItemCondicaoDescontoRenegociacaoVO getItemCondicaoDescontoRenegociacaoTempVO() {
		if (itemCondicaoDescontoRenegociacaoTempVO == null) {
			itemCondicaoDescontoRenegociacaoTempVO = new ItemCondicaoDescontoRenegociacaoVO();
		}
		return itemCondicaoDescontoRenegociacaoTempVO;
	}

	public void setItemCondicaoDescontoRenegociacaoTempVO(ItemCondicaoDescontoRenegociacaoVO itemCondicaoDescontoRenegociacaoTempVO) {
		this.itemCondicaoDescontoRenegociacaoTempVO = itemCondicaoDescontoRenegociacaoTempVO;
	}

}
