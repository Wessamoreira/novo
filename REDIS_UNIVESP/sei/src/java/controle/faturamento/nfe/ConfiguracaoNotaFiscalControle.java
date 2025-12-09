package controle.faturamento.nfe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.faturamento.nfe.enumeradores.CodigoRegimeTributarioEnum;
import negocio.comuns.faturamento.nfe.enumeradores.TipoIntegracaoNfeEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.AmbienteNfeEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("ConfiguracaoNotaFiscalControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoNotaFiscalControle extends SuperControle implements Serializable {

	private ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO;
	private Boolean permiteAlterarUltimoNumeroNota;
	
	public ConfiguracaoNotaFiscalControle() throws Exception {
		verificarPermissaoPermiteAlterarUltimoNumeroNota();
	}

	public String novo() {
		try {
			setConfiguracaoNotaFiscalVO(null);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoNotaFiscalForm");
	}

	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}

			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (!getControleConsulta().getValorConsulta().equals("")) {
					objs = getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorCodigo(Integer.parseInt(getControleConsulta().getValorConsulta()), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
				}
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String editar() {
		try {
			ConfiguracaoNotaFiscalVO obj = (ConfiguracaoNotaFiscalVO) context().getExternalContext().getRequestMap().get("configuracaoNotaFiscalItem");
			setConfiguracaoNotaFiscalVO(getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorChavePrimaria(obj.getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			limparMensagem();
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoNotaFiscalForm");
	}

	public void gravar() {
		try {
			if (getConfiguracaoNotaFiscalVO().isNovoObj()) {
				getFacadeFactory().getConfiguracaoNotaFiscalFacade().incluir(getConfiguracaoNotaFiscalVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			} else {
				getFacadeFactory().getConfiguracaoNotaFiscalFacade().alterar(getConfiguracaoNotaFiscalVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			}
			this.setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getConfiguracaoNotaFiscalFacade().excluir(getConfiguracaoNotaFiscalVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setConfiguracaoNotaFiscalVO(new ConfiguracaoNotaFiscalVO());
			this.setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			this.setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String inicializarConsultar() {
		try {
			setListaConsulta(null);
			setConfiguracaoNotaFiscalVO(null);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoNotaFiscalCons");
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getConfiguracaoNotaFiscalVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.CERTIFICADO_NFE, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public SelectItem[] getListaSelectItemTipoIntegracaoNfe() {
		SelectItem[] items = new SelectItem[TipoIntegracaoNfeEnum.values().length];
		int i = 0;
		for (TipoIntegracaoNfeEnum g : TipoIntegracaoNfeEnum.values()) {
			items[i++] = new SelectItem(g, g.getValue());
		}
		return items;
	}

	public SelectItem[] getListaSelectItemCodigoRegimeTributario() {
		SelectItem[] items = new SelectItem[CodigoRegimeTributarioEnum.values().length];
		int i = 0;
		for (CodigoRegimeTributarioEnum g : CodigoRegimeTributarioEnum.values()) {
			items[i++] = new SelectItem(g, g.getValue());
		}
		return items;
	}

	public SelectItem[] getListaSelectItemAmbienteNfe() {
		SelectItem[] items = new SelectItem[AmbienteNfeEnum.values().length];
		int i = 0;
		for (AmbienteNfeEnum g : AmbienteNfeEnum.values()) {
			items[i++] = new SelectItem(g, g.getValue());
		}
		return items;
	}

	public ConfiguracaoNotaFiscalVO getConfiguracaoNotaFiscalVO() {
		if (configuracaoNotaFiscalVO == null) {
			configuracaoNotaFiscalVO = new ConfiguracaoNotaFiscalVO();
		}
		return configuracaoNotaFiscalVO;
	}

	public void setConfiguracaoNotaFiscalVO(ConfiguracaoNotaFiscalVO configuracaoNotaFiscalVO) {
		this.configuracaoNotaFiscalVO = configuracaoNotaFiscalVO;
	}

	public void carregarEnderecoPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(configuracaoNotaFiscalVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCidade() {
		getConfiguracaoNotaFiscalVO().setCidade((CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItem"));
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setValorConsulta("");
		getControleConsultaOtimizado().setCampoConsulta("");
	}

	public List<SelectItem> getTipoConsultaCidade() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public void consultarCidade() {
		try {
			setMensagemID("");
			List<CidadeVO> objs = new ArrayList<CidadeVO>(0);
			if (getControleConsultaOtimizado().getCampoConsulta().equals("codigo")) {
				if (getControleConsultaOtimizado().getValorConsulta().equals("")) {
					setMensagemID("msg_entre_prmconsulta");
					getControleConsultaOtimizado().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsultaOtimizado().getValorConsulta());
				objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getControleConsultaOtimizado().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorNome(getControleConsultaOtimizado().getValorConsulta(), false, getUsuarioLogado());
			}
			getControleConsultaOtimizado().setListaConsulta(objs);
			if (getMensagemID().equals("")) {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void downloadCertificado() {
		try {
		String caminhoCertificado = UteisNfe.getCaminhoCertificado(getConfiguracaoNotaFiscalVO(), getConfiguracaoGeralPadraoSistema());
		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
		request.getSession().setAttribute("nomeArquivo", getConfiguracaoNotaFiscalVO().getArquivoVO().getNome());
		request.getSession().setAttribute("pastaBaseArquivo", caminhoCertificado.substring(0, caminhoCertificado.lastIndexOf(File.separator)));
		request.getSession().setAttribute("deletarArquivo", false);
		context().getExternalContext().dispatch("/DownloadSV");
		FacesContext.getCurrentInstance().responseComplete();
		} catch(Exception e) {
			if (e instanceof FileNotFoundException) {
				setMensagemDetalhada("msg_erro", "Certificado Não Localizado");
			}
			e.printStackTrace();
		}
	}
	
	public void verificarPermissaoPermiteAlterarUltimoNumeroNota() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteAlterarUltimoNumeroNota", getUsuarioLogado());
			setPermiteAlterarUltimoNumeroNota(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteAlterarUltimoNumeroNota(Boolean.FALSE);
		}
	}

	public Boolean getPermiteAlterarUltimoNumeroNota() {
		if (permiteAlterarUltimoNumeroNota == null) {
			permiteAlterarUltimoNumeroNota = false;
		}
		return permiteAlterarUltimoNumeroNota;
	}

	public void setPermiteAlterarUltimoNumeroNota(Boolean permiteAlterarUltimoNumeroNota) {
		this.permiteAlterarUltimoNumeroNota = permiteAlterarUltimoNumeroNota;
	}
	
	public boolean getIsAmbienteProducao() {
		return AmbienteNfeEnum.PRODUCAO.equals(getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum());
	}
	
    public String getHorarioAplicacao() {
    	return Uteis.getDataComHora(new Date());
    }
    
    public String getHorarioNotaFiscal() {
    	Calendar data = Calendar.getInstance();
    	data.setTime(new Date());
    	data.add(Calendar.HOUR, getConfiguracaoNotaFiscalVO().getFusoHorario());
    	return Uteis.getDataComHora(data.getTime());
    }
    
}
