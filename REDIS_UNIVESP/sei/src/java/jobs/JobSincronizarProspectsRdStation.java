package jobs;

import java.util.List;

import org.springframework.stereotype.Service;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;


/**
 * @author Victor Hugo 06/04/2016 5.0.4.0
 */
@Service
public class JobSincronizarProspectsRdStation extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ProspectsVO> prospectsVos;
	private ConfiguracaoGeralSistemaVO config;

	
	public JobSincronizarProspectsRdStation(List<ProspectsVO>prospectsVos, ConfiguracaoGeralSistemaVO config) {
		super();
		this.prospectsVos = prospectsVos;
		this.config = config;
	}
	
	@Override
	public synchronized void run() {
		try {
			int status;
			
			// Contador que ira controlar a quantidade maxima de requisicoes aceitas por minuto pelo WS do RD Station
			int cont = 0;
			for(ProspectsVO prospectVO : getProspectsVos()) {
				
				
				//https://developers.rdstation.com/pt-BR/best-practices
				if(cont >= 100) {
					this.wait(60000);
					cont = 0;
				}
				
				status = getFacadeFactory().getLeadInterfaceFacade().incluirLeadNoRdStation(prospectVO, config);
				
				//status definidos do RD Station https://github.com/ResultadosDigitais/rdocs/blob/master/rdstation_js_integration.md
				if(status == 200 || status == 302) {
					prospectVO.setSincronizadoRDStation(true);
					getFacadeFactory().getProspectsFacade().alterarFlagProspectSincronizadoComRDStation(prospectVO, null);
				} else {
					prospectVO.setLogSincronizacaoRD("Status CODE: " + String.valueOf(status) + " - " + UteisData.getDataComHoraAtual().toString());
					prospectVO.setSincronizadoRDStation(false);
					getFacadeFactory().getProspectsFacade().alterarFlagProspectSincronizadoComRDStation(prospectVO, null);
				}
				
				cont++;
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public ConfiguracaoGeralSistemaVO getConfig() {
		return config;
	}

	public void setConfig(ConfiguracaoGeralSistemaVO config) {
		this.config = config;
	}

	public List<ProspectsVO> getProspectsVos() {
		return prospectsVos;
	}

	public void setProspectsVos(List<ProspectsVO> prospectsVos) {
		this.prospectsVos = prospectsVos;
	}
}