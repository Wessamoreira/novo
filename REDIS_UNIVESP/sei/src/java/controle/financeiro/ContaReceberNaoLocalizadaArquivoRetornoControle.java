/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.ContaReceberNaoLocalizadaArquivoRetornoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Philippe
 */
@Controller("ContaReceberNaoLocalizadaArquivoRetornoControle")
@Scope("viewScope")
@Lazy
public class ContaReceberNaoLocalizadaArquivoRetornoControle extends SuperControle {

    private ContaReceberNaoLocalizadaArquivoRetornoVO contaReceberNaoLocalizadaArquivoRetornoVO;
	private String campoConsultaContaReceber;
	private String valorConsultaContaReceber;
	private List listaConsultaContaReceber;
    

    public ContaReceberNaoLocalizadaArquivoRetornoControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("nossoNumero")) {
                objs = getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().consultarPorNossoNumero(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            } else if (getControleConsulta().getCampoConsulta().equals("dataVcto")) {
                objs = getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().consultarPorPeriodoDataVcto(getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            } else if (getControleConsulta().getCampoConsulta().equals("tratada")) {
                objs = getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().consultarPorTratada(Boolean.TRUE, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            } else if (getControleConsulta().getCampoConsulta().equals("naoTratada")) {
                objs = getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().consultarPorTratada(Boolean.FALSE, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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

    public void abrirModalObservacao() {
        try {
            ContaReceberNaoLocalizadaArquivoRetornoVO obj = (ContaReceberNaoLocalizadaArquivoRetornoVO) context().getExternalContext().getRequestMap().get("contaReceberNaoLocalizadaArquivoRetornoItens");
            setContaReceberNaoLocalizadaArquivoRetornoVO(obj);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void marcarDesmarcarComoTratada() {
        try {
            ContaReceberNaoLocalizadaArquivoRetornoVO obj = (ContaReceberNaoLocalizadaArquivoRetornoVO) context().getExternalContext().getRequestMap().get("contaReceberNaoLocalizadaArquivoRetornoItens");
            setContaReceberNaoLocalizadaArquivoRetornoVO(obj);
            if (!obj.getTratada()) {
                obj.setObservacao("");
                obj.setContaReceberVO(null);
                getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().alterar(obj, getUsuarioLogado());
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void gravar() {
        try {
            getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().alterar(getContaReceberNaoLocalizadaArquivoRetornoVO(), getUsuarioLogado());
            getContaReceberNaoLocalizadaArquivoRetornoVO().setObservacao("");
            this.consultar();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoComboConsulta() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("naoTratada", "Não tratadas"));
        objs.add(new SelectItem("tratada", "Tratadas"));
        objs.add(new SelectItem("nossoNumero", "Nosso Número"));
        objs.add(new SelectItem("dataVcto", "Data de Vencimento"));
        return objs;
    }

    public List getTipoConsultaComboContaReceber() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nossoNumero", "Nosso Número"));
		return itens;
	}

	public void consultarContaReceber() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getValorConsultaContaReceber().equals("") && !getValorConsultaContaReceber().contains("%")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaContaReceber().equals("nossoNumero")) {
				objs = getFacadeFactory().getContaReceberFacade().consultaRapidaPorNossoNumero(getValorConsultaContaReceber(), new ArrayList<String>(), 0, "", false, null, null, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), null);
			}
			setListaConsultaContaReceber(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaContaReceber(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarContaReceber() {
		try {
			ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("ContaReceberItens");
			getListaConsultaContaReceber().clear();
			getContaReceberNaoLocalizadaArquivoRetornoVO().setContaReceberVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparContaReceber() {
		try {
			getContaReceberNaoLocalizadaArquivoRetornoVO().setContaReceberVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
    public Boolean getIsConsultaDataVcto() {
        if (getControleConsulta().getCampoConsulta().equals("dataVcto")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public ContaReceberNaoLocalizadaArquivoRetornoVO getContaReceberNaoLocalizadaArquivoRetornoVO() {
        if (contaReceberNaoLocalizadaArquivoRetornoVO == null) {
            contaReceberNaoLocalizadaArquivoRetornoVO = new ContaReceberNaoLocalizadaArquivoRetornoVO();
        }
        return contaReceberNaoLocalizadaArquivoRetornoVO;
    }

    public void setContaReceberNaoLocalizadaArquivoRetornoVO(ContaReceberNaoLocalizadaArquivoRetornoVO contaReceberNaoLocalizadaArquivoRetornoVO) {
        this.contaReceberNaoLocalizadaArquivoRetornoVO = contaReceberNaoLocalizadaArquivoRetornoVO;
    }
    
	public String getCampoConsultaContaReceber() {
		if (campoConsultaContaReceber == null) {
			campoConsultaContaReceber = "";
		}
		return campoConsultaContaReceber;
	}

	public void setCampoConsultaContaReceber(String campoConsultaContaReceber) {
		this.campoConsultaContaReceber = campoConsultaContaReceber;
	}

	public String getValorConsultaContaReceber() {
		if (valorConsultaContaReceber == null) {
			valorConsultaContaReceber = "";
		}
		return valorConsultaContaReceber;
	}

	public void setValorConsultaContaReceber(String valorConsultaContaReceber) {
		this.valorConsultaContaReceber = valorConsultaContaReceber;
	}

	public List getListaConsultaContaReceber() {
		if (listaConsultaContaReceber == null) {
			listaConsultaContaReceber = new ArrayList(0);
		}
		return listaConsultaContaReceber;
	}

	public void setListaConsultaContaReceber(List listaConsultaContaReceber) {
		this.listaConsultaContaReceber = listaConsultaContaReceber;
	}    
}
