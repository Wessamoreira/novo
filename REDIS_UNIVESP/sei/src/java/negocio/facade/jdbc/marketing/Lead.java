package negocio.facade.jdbc.marketing;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import jobs.JobSincronizarProspectsRdStation;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.marketing.LeadInterfaceFacade;
import webservice.servicos.IntegracaoRdStationRS;
import webservice.servicos.objetos.LeadRSVO;

/**
 * Classe que encapsula as as operações de manipulação dos
 * dados da classe
 * <code>LeadRSVO</code>. 
 */
@Repository
@Scope("singleton")
@Lazy
public class Lead extends ControleAcesso implements LeadInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

    public Lead() throws Exception {
        super();
        setIdEntidade("Lead");
    }

    public LeadRSVO novo() throws Exception {
        Lead.incluir(getIdEntidade());
        LeadRSVO obj = new LeadRSVO();
        return obj;
    }

    public void setIdEntidade(String idEntidade) {
        Lead.idEntidade = idEntidade;
    }

	@Override
	public int incluirLeadNoRdStation(ProspectsVO prospectsVO, ConfiguracaoGeralSistemaVO config) throws Exception {
		IntegracaoRdStationRS integracao = new IntegracaoRdStationRS();
		return Integer.valueOf(integracao.incluirLeadRDStation(prospectsVO, config));
	}
	
	@Override
	public void incluirListaDeLeadsNoRdStation(List<ProspectsVO> prospectsVos, ConfiguracaoGeralSistemaVO config) {
		
		JobSincronizarProspectsRdStation jobSincronizarProspetsRdStation = new JobSincronizarProspectsRdStation(prospectsVos, config);
    	Thread thread = new Thread(jobSincronizarProspetsRdStation);
    	thread.start();
    	
	}

}