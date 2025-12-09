package controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TipoAdvertenciaVO;
import negocio.comuns.academico.enumeradores.SituacaoTipoAdvertenciaEnum;
import negocio.comuns.utilitarias.Uteis;

@Controller("TipoAdvertenciaControle")
@Scope("viewScope")
@Lazy
public class TipoAdvertenciaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private TipoAdvertenciaVO tipoAdvertenciaVO;
	private Boolean exibirListaSelectItemSituacao;

	public TipoAdvertenciaControle() throws Exception {
		setTipoAdvertenciaVO(new TipoAdvertenciaVO());
		inicializarConsultar();
	}

	public String novo() throws Exception {
		try {
			setTipoAdvertenciaVO(new TipoAdvertenciaVO());
			removerObjetoMemoria(this);
			getTipoAdvertenciaVO().setUsuarioVO(getUsuarioLogadoClone());
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaForm.xhtml");
		} catch (Exception e) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaForm.xhtml");
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaCons.xhtml");
	}

	public String editar() throws Exception {
		try {
			TipoAdvertenciaVO tipoAdvertenciaVO = (TipoAdvertenciaVO) context().getExternalContext().getRequestMap().get("tipoAdvertenciaItens");
			setTipoAdvertenciaVO(tipoAdvertenciaVO);
			getTipoAdvertenciaVO().setNovoObj(false);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaForm.xhtml");
		}
	}

	public String gravar() throws Exception {
		try {
			getFacadeFactory().getTipoAdvertenciaFacade().persistir(getTipoAdvertenciaVO(), true, getUsuarioLogado());
			getTipoAdvertenciaVO().setUsuarioVO(getUsuarioLogadoClone());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaForm.xhtml");
		}
	}

	public String consultar() throws Exception {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getTipoAdvertenciaFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), getTipoAdvertenciaVO().getSituacao(), false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS));
			} else {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getTipoAdvertenciaFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), null, false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS));
			}
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaCons.xhtml");
		}
	}

	public String excluir() throws Exception {
		try {
			getFacadeFactory().getTipoAdvertenciaFacade().excluir(getTipoAdvertenciaVO(), true, getUsuarioLogado());
			setTipoAdvertenciaVO(new TipoAdvertenciaVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoAdvertenciaForm.xhtml");
		}
	}

	public List<SelectItem> getListaSelectItemSituacaoTipoAdvertencia() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		for (SituacaoTipoAdvertenciaEnum situacaoTipoAdvertenciaEnum : SituacaoTipoAdvertenciaEnum.values()) {
			objs.add(new SelectItem(situacaoTipoAdvertenciaEnum.name(), situacaoTipoAdvertenciaEnum.getValorApresentar()));
		}
		return objs;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> objs = new ArrayList<SelectItem>();
		objs.add(new SelectItem("descricao", "Descrição"));
		objs.add(new SelectItem("situacao", "Situação"));
		return objs;
	}

	public void verificarTipoConsultaCombo() {
		if (getControleConsulta().getCampoConsulta().equals("situacao")) {
			setExibirListaSelectItemSituacao(true);
		} else {
			setExibirListaSelectItemSituacao(false);
		}
	}

	public TipoAdvertenciaVO getTipoAdvertenciaVO() {
		if (tipoAdvertenciaVO == null) {
			tipoAdvertenciaVO = new TipoAdvertenciaVO();
		}
		return tipoAdvertenciaVO;
	}

	public void setTipoAdvertenciaVO(TipoAdvertenciaVO tipoAdvertenciaVO) {
		this.tipoAdvertenciaVO = tipoAdvertenciaVO;
	}

	public Boolean getExibirListaSelectItemSituacao() {
		if (exibirListaSelectItemSituacao == null) {
			exibirListaSelectItemSituacao = false;
		}
		return exibirListaSelectItemSituacao;
	}

	public Boolean getIsExibirListaSelectItemSituacao() {
		return exibirListaSelectItemSituacao;
	}

	public void setExibirListaSelectItemSituacao(Boolean exibirListaSelectItemSituacao) {
		this.exibirListaSelectItemSituacao = exibirListaSelectItemSituacao;
	}

}
