package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.IntegracaoFinanceiroVO;
import negocio.comuns.utilitarias.ControleConsulta;
@Controller("IntegracaoFinanceiroControle")
@Scope("request")
@Lazy
public class IntegracaoFinanceiroControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private IntegracaoFinanceiroVO integracaoFinanceiroVO;

	public IntegracaoFinanceiroControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}
	
	public String executarProcessamentoArquivoIntegracaoFinanceiro() {
		try {
			
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	public String editar() {
		try {
			IntegracaoFinanceiroVO obj = (IntegracaoFinanceiroVO) context().getExternalContext().getRequestMap().get("integracao");
			obj.setNovoObj(Boolean.FALSE);
			setIntegracaoFinanceiroVO(obj);
			setMensagemID("msg_dados_editar");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	@SuppressWarnings("rawtypes")
	public String consultar() {
		try {
			super.consultar();
			
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}
	
    public String getCaminhoServidorDownloadArquivo() {
        try {
            return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getIntegracaoFinanceiroVO().getArquivo(), getIntegracaoFinanceiroVO().getArquivo().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
        return "";
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
//        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("dataProcessamento", "Data Processamento"));
		return itens;
	}

	public String excluir() {
		try {
			
			setIntegracaoFinanceiroVO(null);
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}

	}

	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	public IntegracaoFinanceiroVO getIntegracaoFinanceiroVO() {
		if (integracaoFinanceiroVO == null) {
			integracaoFinanceiroVO = new IntegracaoFinanceiroVO();
		}
		return integracaoFinanceiroVO;
	}

	public void setIntegracaoFinanceiroVO(IntegracaoFinanceiroVO integracaoFinanceiroVO) {
		this.integracaoFinanceiroVO = integracaoFinanceiroVO;
	}
	
    public boolean getIsFiltrarPorData() {
        return getControleConsulta().getCampoConsulta().equals("dataProcessamento");
    }

}