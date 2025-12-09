package controle.basico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CatracaVO;
import negocio.comuns.basico.enumeradores.DirecaoEnum;
import negocio.comuns.basico.enumeradores.TagsCatracaEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("CatracaControle")
@Scope("viewScope")
@Lazy
public class CatracaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private CatracaVO catracaVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemDirecao;
	private List<SelectItem> listaSelectItemSituacao;

	public CatracaControle() {
		setMensagemID("msg_entre_prmconsulta");
	}

	public void persistir() {
		try {
			getFacadeFactory().getCatracaFacade().persistir(getCatracaVO(), true, getUsuarioLogado());
			limparMensagem();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String novo() {
		try {
			montarListaSelectItemUnidadeEnsino();
			montarListaSelectItemDirecao();
			montarListaSelectItemSituacao();
			setCatracaVO(null);
			getControleConsulta().setListaConsulta(null);
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("catracaForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String consultar() {
		try {
			List<CatracaVO> listaConsulta = new ArrayList<CatracaVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				listaConsulta.addAll(getFacadeFactory().getCatracaFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				Integer valorConsulta = Integer.parseInt(getControleConsulta().getValorConsulta());
				listaConsulta.addAll(getFacadeFactory().getCatracaFacade().consultarPorCodigo(valorConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("serie")) {
				listaConsulta.add(getFacadeFactory().getCatracaFacade().consultarPorNumeroSerie(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("ip")) {
				listaConsulta.add(getFacadeFactory().getCatracaFacade().consultarPorEnderecoIP(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			getControleConsulta().setListaConsulta(listaConsulta);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String editar() {
		try {
			CatracaVO obj = (CatracaVO) context().getExternalContext().getRequestMap().get("catracaItem");
			setCatracaVO(getFacadeFactory().getCatracaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("catracaForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("catracaForm");
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getCatracaFacade().excluir(getCatracaVO(), false, getUsuarioLogado());
			limparMensagem();
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String inicializarConsulta() {
		getControleConsulta().setListaConsulta(null);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("catracaCons");
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>(0));
		}
	}

	public void montarListaSelectItemDirecao() {
		try {
			setListaSelectItemDirecao(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(DirecaoEnum.class, "name", "valorApresentar", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setListaSelectItemDirecao(new ArrayList<SelectItem>(0));
		}
	}

	public void montarListaSelectItemSituacao() {
		try {
			setListaSelectItemSituacao(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoEnum.class, "name", "valorApresentar", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setListaSelectItemSituacao(new ArrayList<SelectItem>(0));
		}
	}

	public String getTags() {
		StringBuilder tags = new StringBuilder();
		Integer i = 1;
		for (TagsCatracaEnum tag : TagsCatracaEnum.values()) {
			if (i.equals(TagsCatracaEnum.values().length)) {
				tags.append(tag.getName());
			} else {
				tags.append(tag.getName()).append(", ");
			}
			i++;
		}
		return tags.toString();
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("ip", "Endereço IP"));
		itens.add(new SelectItem("serie", "Número de Série"));
		return itens;
	}
	
	 public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
	        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
	        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
	        consultar();
	    }

	public CatracaVO getCatracaVO() {
		if (catracaVO == null) {
			catracaVO = new CatracaVO();
		}
		return catracaVO;
	}

	public void setCatracaVO(CatracaVO catracaVO) {
		this.catracaVO = catracaVO;
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

	public List<SelectItem> getListaSelectItemDirecao() {
		if (listaSelectItemDirecao == null) {
			listaSelectItemDirecao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDirecao;
	}

	public void setListaSelectItemDirecao(List<SelectItem> listaSelectItemDirecao) {
		this.listaSelectItemDirecao = listaSelectItemDirecao;
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null) {
			listaSelectItemSituacao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

}
