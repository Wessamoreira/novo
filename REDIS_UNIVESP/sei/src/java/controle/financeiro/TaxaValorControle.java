package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.TaxaVO;
import negocio.comuns.financeiro.TaxaValorVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("TaxaValorControle")
@Scope("viewScope")
@Lazy
public class TaxaValorControle extends SuperControle implements Serializable {

	private TaxaVO taxaVO;
	private TaxaValorVO taxaValorVO;

	public TaxaValorControle() {
		inicializarObjetoTaxaVO();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_taxa_inicio");
		setMensagemDetalhada("");
	}

	public String adicionarItemTaxaValor() {
		try {
			getFacadeFactory().getTaxaValorFacade().validarDadosTaxaValor(getTaxaValorVO());
			if (!getTaxaVO().getCodigo().equals(0)) {
				taxaValorVO.setTaxaVO(getTaxaVO());
			}
			getFacadeFactory().getTaxaFacade().adicionarObjTaxaValorVOs(getTaxaVO(), getTaxaValorVO());
			setMensagemID("msg_taxaValor_adicionada");
			setTaxaValorVO(new TaxaValorVO());
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorForm.xhtml");
	}

	public String ativarTaxa() {
		try {
			if (getFacadeFactory().getTaxaValorFacade().consultarOpcoesTaxaValor(getTaxaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS).isEmpty() && getTaxaVO().getCodigo().intValue() != 0) {
				getFacadeFactory().getTaxaFacade().ativarTaxa(getTaxaVO().getCodigo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				getTaxaVO().setSituacao("AT");
				setMensagemID("msg_taxa_gravada");
			} else if (!getFacadeFactory().getTaxaValorFacade().consultarOpcoesTaxaValor(getTaxaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS).isEmpty() && getTaxaVO().getCodigo().intValue() == 0) {
				getFacadeFactory().getTaxaFacade().ativarTaxa(getTaxaVO().getCodigo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				setTaxaVO(getFacadeFactory().getTaxaFacade().consultarPorChavePrimaria(getTaxaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				setMensagemID("msg_taxa_gravada");
			} else if (!getFacadeFactory().getTaxaValorFacade().consultarOpcoesTaxaValor(getTaxaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS).isEmpty() && getTaxaVO().getCodigo().intValue() != 0) {
				getFacadeFactory().getTaxaFacade().ativarTaxa(getTaxaVO().getCodigo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				setTaxaVO(getFacadeFactory().getTaxaFacade().consultarPorChavePrimaria(getTaxaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				setMensagemID("msg_taxa_gravada");
			} else {
				getTaxaVO().setSituacao("AT");
			}

		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorForm.xhtml");
	}

	public String inativarTaxa() {
		try {
			if (getFacadeFactory().getTaxaValorFacade().consultarOpcoesTaxaValor(getTaxaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS).isEmpty() && getTaxaVO().getCodigo().intValue() != 0) {
				getFacadeFactory().getTaxaFacade().inativarTaxa(getTaxaVO().getCodigo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				getTaxaVO().setSituacao("IN");
				setMensagemID("msg_taxa_gravada");
			} else if (!getFacadeFactory().getTaxaValorFacade().consultarOpcoesTaxaValor(getTaxaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS).isEmpty() && getTaxaVO().getCodigo().intValue() == 0) {
				getFacadeFactory().getTaxaFacade().inativarTaxa(getTaxaVO().getCodigo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				setTaxaVO(getFacadeFactory().getTaxaFacade().consultarPorChavePrimaria(getTaxaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				setMensagemID("msg_taxa_gravada");
			} else if (!getFacadeFactory().getTaxaValorFacade().consultarOpcoesTaxaValor(getTaxaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS).isEmpty() && getTaxaVO().getCodigo().intValue() != 0) {
				getFacadeFactory().getTaxaFacade().inativarTaxa(getTaxaVO().getCodigo(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				setTaxaVO(getFacadeFactory().getTaxaFacade().consultarPorChavePrimaria(getTaxaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				setMensagemID("msg_taxa_gravada");
			} else {
				getTaxaVO().setSituacao("IN");
			}

		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorForm.xhtml");
	}

	public String persistir() {
		try {
			if (getTaxaVO().isNovoObj().booleanValue()) {
				getFacadeFactory().getTaxaFacade().incluir(getTaxaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			} else {
				getFacadeFactory().getTaxaFacade().alterar(getTaxaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			}
			setMensagemID("msg_taxa_gravada");
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorForm.xhtml");
	}

	public String novo() {
		setTaxaVO(new TaxaVO());
		setTaxaValorVO(new TaxaValorVO());
		setMensagemID("msg_taxa_inicio");
		setMensagemDetalhada("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorForm.xhtml");
	}

	public String consultarTaxa() {
		setMensagemID("msg_taxa_inicio");
		setMensagemDetalhada("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorCons.xhtml");
	}

	public String removerTaxaValorVOs() {

		try {
			TaxaValorVO obj = (TaxaValorVO) context().getExternalContext().getRequestMap().get("taxavalorItens");
			getFacadeFactory().getTaxaValorFacade().removerTaxaValorVOs(getTaxaVO(), obj);
			setTaxaValorVO(new TaxaValorVO());
			setMensagemID("msg_taxa_removida");
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorForm.xhtml");
	}

	public void inicializarObjetoTaxaVO() {
		TaxaVO taxa;
		taxa = (TaxaVO) context().getExternalContext().getSessionMap().get(TaxaVO.class.getSimpleName());
		if (taxa != null) {
			setTaxaVO(taxa);
		}
	}

	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "TaxaValorControle", "Inicializando Consultar Taxa", "Consultando");
			super.consultar();
			List objs = new ArrayList(0);
			objs = getFacadeFactory().getTaxaFacade().consultarDescricaoTaxa(getControleConsulta().getCampoConsulta(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setListaConsulta(objs);
			getTaxaVO().setNovoObj(Boolean.FALSE);
			registrarAtividadeUsuario(getUsuarioLogado(), "TaxaValorControle", "Finalizando Consultar Taxa", "Finalizando");
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorCons.xhtml");
		}
	}

	public String editarTaxa() throws Exception {
		TaxaVO obj = (TaxaVO) context().getExternalContext().getRequestMap().get("taxaItens");
		setTaxaVO(obj);
		getTaxaVO().setNovoObj(Boolean.FALSE);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorForm.xhtml");

	}

	public void editarTaxaValor() throws Exception {
		TaxaValorVO obj = (TaxaValorVO) context().getExternalContext().getRequestMap().get("taxavalorItens");
		setTaxaValorVO(obj);
	}

	public String excluirSegmentacaoProspectVO() {
		try {
			getFacadeFactory().getTaxaFacade().excluirTaxa(getTaxaVO(), getUsuarioLogado());
			setTaxaVO(new TaxaVO());
			this.consultar();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("taxaValorForm.xhtml");
		}
	}

	public List getSituacaoTaxa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("AT", "Ativa"));
		itens.add(new SelectItem("IN", "Inativa"));
		return itens;
	}

	public TaxaValorVO getTaxaValorVO() {

		if (taxaValorVO == null) {
			taxaValorVO = new TaxaValorVO();
		}

		return taxaValorVO;
	}

	public void setTaxaValorVO(TaxaValorVO taxaValorVO) {
		this.taxaValorVO = taxaValorVO;
	}

	public TaxaVO getTaxaVO() {
		if (taxaVO == null) {
			taxaVO = new TaxaVO();
		}

		return taxaVO;
	}

	public void setTaxaVO(TaxaVO taxaVO) {
		this.taxaVO = taxaVO;
	}


}