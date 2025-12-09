package relatorio.controle.crm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.crm.BuscaProspectVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.crm.InteracaoFollowUpRelVO;

@Controller("InteracaoFollowUpRelControle")
@Scope("request")
@Lazy
public class InteracaoFollowUpRelControle extends SuperControleRelatorio {

	private List<BuscaProspectVO> buscaProspectVOs;
	
	public String imprimirPDF() {
		List<InteracaoFollowUpRelVO> interacaoFollowUpVOs = new ArrayList<InteracaoFollowUpRelVO>(0);
        try {
        	
            interacaoFollowUpVOs = getFacadeFactory().getInteracaoFollowUpRelFacade().criarObjeto(getBuscaProspectVOs());
            if (!interacaoFollowUpVOs.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getInteracaoFollowUpRelFacade().designRelatorio());
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getInteracaoFollowUpRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Interações");
                getSuperParametroRelVO().setListaObjetos(interacaoFollowUpVOs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getInteracaoFollowUpRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                realizarImpressaoRelatorio();
                return "msg_relatorio_ok";
            } else {
                setUsarTargetBlank("");
                return "msg_relatorio_sem_dados";
            }
        } catch (Exception ex) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", ex.getMessage());
            return ex.getMessage();
        }
    }

	public List<BuscaProspectVO> getBuscaProspectVOs() {
		if (buscaProspectVOs == null) {
			buscaProspectVOs = new ArrayList<BuscaProspectVO>(0);
		}
		return buscaProspectVOs;
	}

	public void setBuscaProspectVOs(List<BuscaProspectVO> buscaProspectVOs) {
		this.buscaProspectVOs = buscaProspectVOs;
	}

	
}
