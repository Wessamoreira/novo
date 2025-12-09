package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.ChancelaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("ChancelaControle")
@Scope("viewScope")
@Lazy
public class ChancelaControle extends SuperControle implements Serializable {

	private ChancelaVO chancelaVO;
	private Boolean campoValorPorAluno;

	public ChancelaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {         removerObjetoMemoria(this);
		setChancelaVO(null);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("chancelaForm");
	}

	public String editar() throws Exception {
		ChancelaVO obj = (ChancelaVO) context().getExternalContext().getRequestMap().get("chancelaItem");
		obj.setNovoObj(Boolean.FALSE);
		setChancelaVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("chancelaForm");
	}

	public String gravar() {
		try {
			if (getChancelaVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getChancelaFacade().incluir(getChancelaVO());
			} else {
				getFacadeFactory().getChancelaFacade().alterar(getChancelaVO());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("chancelaForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("chancelaForm");
		}
	}

	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getChancelaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("instituicaoChanceladora")) {
				objs = getFacadeFactory().getChancelaFacade().consultarPorInstituicaoChanceladora(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("chancelaCons");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("chancelaCons");
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("instituicaoChanceladora", "Instituição Chanceladora"));
                itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public String excluir() {
		try {
			getFacadeFactory().getChancelaFacade().excluir(getChancelaVO());
			setChancelaVO(null);
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("chancelaForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("chancelaForm");
		}

	}

	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("chancelaCons");
	}

	public List getListaSelectItemTipoChancela() throws Exception {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("PC", "Paga Chancela"));
		itens.add(new SelectItem("RC", "Receber Chancela"));
		return itens;

	}

	public ChancelaVO getChancelaVO() {
		if (chancelaVO == null) {
			chancelaVO = new ChancelaVO();
		}
		return chancelaVO;
	}

	public void setChancelaVO(ChancelaVO chancelaVO) {
		this.chancelaVO = chancelaVO;
	}

}