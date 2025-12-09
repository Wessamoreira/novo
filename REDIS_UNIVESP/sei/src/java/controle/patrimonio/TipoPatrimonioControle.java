package controle.patrimonio;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("TipoPatrimonioControle")
@Scope("viewScope")
@Lazy
public class TipoPatrimonioControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private TipoPatrimonioVO tipoPatrimonioVO;

	public TipoPatrimonioControle() throws Exception {
		setTipoPatrimonioVO(new TipoPatrimonioVO());
	}

	public String inicializarConsultar() {
		setTipoPatrimonioVO(null);
		getControleConsulta().setListaConsulta(null);
		getControleConsulta().setValorConsulta("");
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoPatrimonioCons");
	}

	public String novo() {
		setTipoPatrimonioVO(new TipoPatrimonioVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoPatrimonioForm");
	}

	public void gravar() {
		try {
			getFacadeFactory().getTipoPatrimonioFacede().persistir(getTipoPatrimonioVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getTipoPatrimonioFacede().excluir(getTipoPatrimonioVO(), false, getUsuarioLogado());
			setTipoPatrimonioVO(new TipoPatrimonioVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String consultar() {
		try {			
			getControleConsulta().setListaConsulta(getFacadeFactory().getTipoPatrimonioFacede().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public String editar() {
		try {
			setTipoPatrimonioVO((TipoPatrimonioVO) context().getExternalContext().getRequestMap().get("tipoPatrimonioItem"));
			getTipoPatrimonioVO().setNovoObj(false);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoPatrimonioForm");
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	// Getters e Setters
	public TipoPatrimonioVO getTipoPatrimonioVO() {
		if (tipoPatrimonioVO == null) {
			tipoPatrimonioVO = new TipoPatrimonioVO();
		}
		return tipoPatrimonioVO;
	}

	public void setTipoPatrimonioVO(TipoPatrimonioVO tipoPatrimonioVO) {
		this.tipoPatrimonioVO = tipoPatrimonioVO;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		objs.add(new SelectItem("descricao", "Descrição"));
		objs.add(new SelectItem("codigo", "Código"));
		return objs;
	}

}
