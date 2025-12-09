package relatorio.negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.crm.BuscaProspectVO;
import relatorio.negocio.comuns.crm.InteracaoFollowUpRelVO;

public interface InteracaoFollowUpRelInterfaceFacade {

	public List<InteracaoFollowUpRelVO> criarObjeto(List<BuscaProspectVO> listaProspects);
	
	public String designRelatorio();
	
	public String caminhoBaseRelatorio();

}
