package controle.basico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.BiometriaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.EstadoBiometriaEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("BiometriaControle")
@Scope("viewScope")
@Lazy
public class BiometriaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private BiometriaVO biometriaVO;
	private String campoConsultaPessoa;
	private String valorConsultaPessoa;
	private List<PessoaVO> listaConsultaPessoa;
	private String ipServidorBiometria;
	private Boolean excluirDigital;

	public BiometriaControle() {
		setControleConsulta(new ControleConsulta());
		setIpServidorBiometria(getConfiguracaoGeralPadraoSistema().getIpServidorBiometria());
		setMensagemID("msg_entre_prmconsulta");
	}

	@PostConstruct
	public void init() {
		try {
			String codigoPessoa = ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("codigoPessoa");
			if (Uteis.isAtributoPreenchido(codigoPessoa)) {
				PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(Integer.parseInt(codigoPessoa), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				setBiometriaVO(getFacadeFactory().getBiometriaFacade().realizarPreenchimentoBiometria(pessoaVO, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void persistir() {
		try {
			getFacadeFactory().getBiometriaFacade().persistir(getBiometriaVO(), true, getUsuarioLogado());
			limparMensagem();
			setMensagemID("msg_dados_gravados");
//			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
//			return "";
		}
	}

	public String novo() {
		try {
			setBiometriaVO(null);
			getControleConsulta().setListaConsulta(null);
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("biometriaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String consultar() {
		try {
			List objs = new ArrayList(0);
			getControleConsulta().setLimitePorPagina(10);
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getBiometriaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado(), getControleConsulta().getLimitePorPagina(), getControleConsulta().getOffset());
				getControleConsulta().setTotalRegistrosEncontrados(getFacadeFactory().getBiometriaFacade().consultarTotalPorNomePessoa(getControleConsulta().getValorConsulta()));
			}
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				Integer valorConsulta = Integer.parseInt(getControleConsulta().getValorConsulta());
				getControleConsulta().setListaConsulta(getFacadeFactory().getBiometriaFacade().consultarPorCodigo(valorConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				getControleConsulta().setTotalRegistrosEncontrados(0);
				if(!getControleConsulta().getListaConsulta().isEmpty()){				
					getControleConsulta().setTotalRegistrosEncontrados(1);
				}
			}
			setListaConsulta(objs);
			return Uteis.getCaminhoRedirecionamentoNavegacao("biometriaCons.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("biometriaCons.xhtml");
		}
	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
        getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());        
        consultar();
    }

	public String editar() {
		try {
			BiometriaVO obj = (BiometriaVO) context().getExternalContext().getRequestMap().get("biometriaItens");
			setBiometriaVO(obj);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("biometriaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("biometriaForm.xhtml");
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getBiometriaFacade().excluir(getBiometriaVO(), false, getUsuarioLogado());
			limparMensagem();
			setExcluirDigital(Boolean.TRUE);
			setMensagemID("msg_dados_excluidos");
//			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
//			return "editar";
		}
	}

	public String inicializarConsulta() {
		getControleConsulta().setListaConsulta(null);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("biometriaCons.xhtml");
	}

	public void consultarPessoa() {
		try {
			if (getCampoConsultaPessoa().equals("nome")) {
				setListaConsultaPessoa(getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaPessoa(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			if (getCampoConsultaPessoa().equals("cpf")) {
				setListaConsultaPessoa(getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaPessoa(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarPessoaPorCPF() {
		try {
			if (!getBiometriaVO().getPessoaVO().getCPF().equals("")) {
				PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(getBiometriaVO().getPessoaVO().getCPF(), 0, "", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				setBiometriaVO(getFacadeFactory().getBiometriaFacade().realizarPreenchimentoBiometria(pessoaVO, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPessoa() {
		try {
			PessoaVO pessoaVO = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItem");
			setBiometriaVO(getFacadeFactory().getBiometriaFacade().realizarPreenchimentoBiometria(pessoaVO, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCampoPessoa() {
		try {
			setCampoConsultaPessoa(null);
			setValorConsultaPessoa(null);
			getBiometriaVO().setPessoaVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getExibirPanelCadastroBiometrico() {
		if (getExcluirDigital()) {
			setExcluirDigital(Boolean.FALSE);
			return "RichFaces.$('panelCadastroBiometrico').hide();";
		} else {
			return "";
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboPessoa() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public Integer getIdProximoDedo() {
		if (getBiometriaVO().getEstadoBio().equals(EstadoBiometriaEnum.NaoUniu.toString()) || getBiometriaVO().getEstadoBio().equals(EstadoBiometriaEnum.Cadastrando.toString())) {
			return getBiometriaVO().getIdDedo();
		}
		switch (getBiometriaVO().getIdDedo()) {
		case 1:
			return 2;
		case 2:
			return 3;
		case 3:
			return 4;
		case 4:
			return 5;
		case 5:
			return 0;
		case 6:
			return 7;
		case 7:
			return 8;
		case 8:
			return 9;
		case 9:
			return 10;
		case 10:
			return 1;
		default:
			return 0;
		}
	}

	public String getFecharModalPanelCadastroBiometrico() {
		if (getIdProximoDedo().equals(0)) {
			return "RichFaces.$('panelCadastroBiometrico').hide()";
		}
		return "";
	}

	public BiometriaVO getBiometriaVO() {
		if (biometriaVO == null) {
			biometriaVO = new BiometriaVO();
		}
		return biometriaVO;
	}

	public void setBiometriaVO(BiometriaVO biometriaVO) {
		this.biometriaVO = biometriaVO;
	}

	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = "";
		}
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public List<PessoaVO> getListaConsultaPessoa() {
		if (listaConsultaPessoa == null) {
			listaConsultaPessoa = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaPessoa;
	}

	public void setListaConsultaPessoa(List<PessoaVO> listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = "";
		}
		return valorConsultaPessoa;
	}

	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	public String getIpServidorBiometria() {
		if (ipServidorBiometria == null) {
			ipServidorBiometria = "";
		}
		return ipServidorBiometria;
	}

	public void setIpServidorBiometria(String ipServidorBiometria) {
		this.ipServidorBiometria = ipServidorBiometria;
	}

	public Boolean getExcluirDigital() {
		if (excluirDigital == null) {
			excluirDigital = Boolean.FALSE;
		}
		return excluirDigital;
	}

	public void setExcluirDigital(Boolean excluirDigital) {
		this.excluirDigital = excluirDigital;
	}

}
