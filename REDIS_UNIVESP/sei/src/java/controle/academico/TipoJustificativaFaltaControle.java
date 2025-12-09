package controle.academico;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TipoJustificativaFaltaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

@Controller("TipoJustificativaFaltaControle")
@Scope("viewScope")
@Lazy

public class TipoJustificativaFaltaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;

	private TipoJustificativaFaltaVO tipoJustificativaFaltaVO;

	public TipoJustificativaFaltaControle() throws Exception {

		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {
		removerObjetoMemoria(this);
		setTipoJustificativaFaltaVO(new TipoJustificativaFaltaVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("tipoJustificativaFaltaForm.xhtml");
	}
	
	public String gravar() throws ConsistirException {
		try {
			validarDados(tipoJustificativaFaltaVO);
			if (tipoJustificativaFaltaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getTipoJustificativaFaltaFacade().incluir(tipoJustificativaFaltaVO, true, getUsuarioLogado());
			} else {
				getFacadeFactory().getTipoJustificativaFaltaFacade().alterar(tipoJustificativaFaltaVO, true, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoJustificativaFaltaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoJustificativaFaltaForm.xhtml");
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
				objs.add(getFacadeFactory().getTipoJustificativaFaltaFacade().consultarPorChavePrimeira(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				objs = getFacadeFactory().getTipoJustificativaFaltaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}	
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoJustificativaFaltaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoJustificativaFaltaCons.xhtml");
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getTipoJustificativaFaltaFacade().excluir(tipoJustificativaFaltaVO, true, getUsuarioLogado());
			setTipoJustificativaFaltaVO(new TipoJustificativaFaltaVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoJustificativaFaltaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoJustificativaFaltaForm.xhtml");
		}
	}

	public String editar() throws ConsistirException {
		TipoJustificativaFaltaVO obj = (TipoJustificativaFaltaVO) context().getExternalContext().getRequestMap().get("tipoJustItem");
		try {
			validarDados(obj);
			obj.setNovoObj(Boolean.FALSE);
			setTipoJustificativaFaltaVO(obj);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoJustificativaFaltaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("tipoJustificativaFaltaCons.xhtml");

		}
	}

	public TipoJustificativaFaltaVO getTipoJustificativaFaltaVO() {
		if(tipoJustificativaFaltaVO == null){
			tipoJustificativaFaltaVO = new  TipoJustificativaFaltaVO();
		}
		return tipoJustificativaFaltaVO;
	}

	public void setTipoJustificativaFaltaVO(TipoJustificativaFaltaVO tipoJustificativaFaltaVO) {
		this.tipoJustificativaFaltaVO = tipoJustificativaFaltaVO;
	}
	
	public void validarDados(TipoJustificativaFaltaVO obj) throws ConsistirException{
		if(obj.getDescricao() == null || obj.getDescricao().isEmpty()){
			throw new ConsistirException("O campo descri\u00e7\u00e3o deve ser informado.");
		}
	}
	
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
	public List getTipoSexo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("AB", "Ambos"));
        itens.add(new SelectItem("F", "Feminino"));
        itens.add(new SelectItem("M", "Masculino"));
        return itens;
    }
}
