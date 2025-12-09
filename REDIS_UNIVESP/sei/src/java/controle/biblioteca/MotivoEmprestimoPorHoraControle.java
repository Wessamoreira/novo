package controle.biblioteca;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.biblioteca.MotivoEmprestimoPorHoraVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("MotivoEmprestimoPorHoraControle")
@Scope("viewScope")
@Lazy
public class MotivoEmprestimoPorHoraControle extends SuperControle implements Serializable {

	private MotivoEmprestimoPorHoraVO motivoEmprestimoPorHoraVO;

	public String novo() {
		removerObjetoMemoria(this);
		setMotivoEmprestimoPorHoraVO(new MotivoEmprestimoPorHoraVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("motivoEmprestimoPorHoraForm.xhtml");
	}

	public String editar() {
		MotivoEmprestimoPorHoraVO obj = (MotivoEmprestimoPorHoraVO) context().getExternalContext().getRequestMap().get("motivoVO");
		obj.setNovoObj(Boolean.FALSE);
		setMotivoEmprestimoPorHoraVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("motivoEmprestimoPorHoraForm.xhtml");
	}

	public String gravar() {
		try {
			validarDados(motivoEmprestimoPorHoraVO);
			if (motivoEmprestimoPorHoraVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getMotivoEmprestimoPorHoraInterfaceFacade().incluir(motivoEmprestimoPorHoraVO, false, getUsuarioLogado());
			} else {
				getFacadeFactory().getMotivoEmprestimoPorHoraInterfaceFacade().alterar(motivoEmprestimoPorHoraVO, false, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("motivoEmprestimoPorHoraForm.xhtml");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("motivoEmprestimoPorHoraForm.xhtml");

		}

	}

	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("motivoEmprestimoPorHoraCons");

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
				objs = getFacadeFactory().getMotivoEmprestimoPorHoraInterfaceFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				objs = getFacadeFactory().getMotivoEmprestimoPorHoraInterfaceFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
				setListaConsulta(objs);
				setMensagemID("msg_dados_consultados");
				return Uteis.getCaminhoRedirecionamentoNavegacao("motivoEmprestimoPorHoraCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("motivoEmprestimoPorHoraCons.xhtml");
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getMotivoEmprestimoPorHoraInterfaceFacade().excluir(motivoEmprestimoPorHoraVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("motivoEmprestimoPorHoraForm.xhtml");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("motivoEmprestimoPorHoraForm.xhtml");

		}
	}
	
	public void validarDados(MotivoEmprestimoPorHoraVO obj) throws ConsistirException{
		if(obj.getDescricao() == null || obj.getDescricao().isEmpty()){
			throw new ConsistirException("O campo descri\u00e7\u00e3o deve ser informado.");
		}
	}
	
	public MotivoEmprestimoPorHoraControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public MotivoEmprestimoPorHoraVO getMotivoEmprestimoPorHoraVO() {
		if (motivoEmprestimoPorHoraVO == null) {
			motivoEmprestimoPorHoraVO = new MotivoEmprestimoPorHoraVO();
		}
		return motivoEmprestimoPorHoraVO;
	}

	public void setMotivoEmprestimoPorHoraVO(MotivoEmprestimoPorHoraVO motivoEmprestimoPorHoraVO) {
		this.motivoEmprestimoPorHoraVO = motivoEmprestimoPorHoraVO;
	}

}