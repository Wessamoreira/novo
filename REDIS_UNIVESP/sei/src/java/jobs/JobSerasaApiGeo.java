package jobs;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import controle.arquitetura.SuperControle;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.enumerador.IntegracaoNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemanaJob;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@Service
@Lazy
public class JobSerasaApiGeo extends SuperControle {

	private static final long serialVersionUID = 881879427907355116L;

	public void executeJobSerasaApiGeo() {
		try {
			List<AgenteNegativacaoCobrancaContaReceberVO> agenteNegativacaoCobrancaContaReceberVOs  = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarAgenteNegativacaoSerasaApiGeo(IntegracaoNegativacaoCobrancaContaReceberEnum.SERASA_API_GEO, new UsuarioVO());
			agenteNegativacaoCobrancaContaReceberVOs.stream().forEach(agente -> {
				if (agente.getRegistrarAutomaticamenteContasNegativacao() && (agente.getDiaSemanaBaseRegistrarContasNegativacao().equals(DiaSemanaJob.TODOS_DIAS) || agente.getDiaSemanaBaseRegistrarContasNegativacao().getDescricao().equals(UteisData.diaSemana()))) {
					getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().criarCronogramaExecucaoJobSerasaApiGeo(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR, agente.getHoraExecucaoRotinaRegistrarContasNegativacao().getValor() , agente.getCodigo());
				}
				if (agente.getRemoverAutomaticamenteContasNegativacao() && (agente.getDiaSemanaBaseRegistrarContasNegativacao().equals(DiaSemanaJob.TODOS_DIAS) || agente.getDiaSemanaBaseRemoverContasNegativacao().getDescricao().equals(UteisData.diaSemana()))) {
					getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().criarCronogramaExecucaoJobSerasaApiGeo(JobsEnum.JOB_SERASA_API_GEO_REMOVER, agente.getHoraExecucaoRotinaRemoverContasNegativacao().getValor() , agente.getCodigo());
				}
			});
		} catch (Exception e) {
			System.out.println("Erro - executeJobSerasaApiGeo" + e.getMessage() +"- dia"+ Uteis.getDataComHora(new Date()));
			e.printStackTrace();
		}
		
	}
	
	//Rotina criada para ser executar Pela tela de Correcao Banco de Dados no JobEnuns
	public void executeRegistarSerasaApiGeo() {
		try {
			List<AgenteNegativacaoCobrancaContaReceberVO> agenteNegativacaoCobrancaContaReceberVOs  = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarAgenteNegativacaoSerasaApiGeo(IntegracaoNegativacaoCobrancaContaReceberEnum.SERASA_API_GEO, new UsuarioVO());
			for (AgenteNegativacaoCobrancaContaReceberVO agente : agenteNegativacaoCobrancaContaReceberVOs) {
				if (agente.getRegistrarAutomaticamenteContasNegativacao()) {
					getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().criarCronogramaExecucaoJobSerasaApiGeo(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR, String.valueOf(Uteis.getHoraMinutoSegundo(new Date(), "hora")) , agente.getCodigo());
				}
			}
		} catch (Exception e) {
			System.out.println("Erro - executeJobSerasaApiGeo - executeRegistarSerasaApiGeo" + e.getMessage() +"- dia"+ Uteis.getDataComHora(new Date()));
			e.printStackTrace();
		}
		
	}

	//Rotina criada para ser executar Pela tela de Correcao Banco de Dados no JobEnuns
	public void executeRemocaoSerasaApiGeo() {
		try {
			List<AgenteNegativacaoCobrancaContaReceberVO> agenteNegativacaoCobrancaContaReceberVOs  = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarAgenteNegativacaoSerasaApiGeo(IntegracaoNegativacaoCobrancaContaReceberEnum.SERASA_API_GEO, new UsuarioVO());
			for (AgenteNegativacaoCobrancaContaReceberVO agente : agenteNegativacaoCobrancaContaReceberVOs) {
				if (agente.getRemoverAutomaticamenteContasNegativacao()) {
					getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().criarCronogramaExecucaoJobSerasaApiGeo(JobsEnum.JOB_SERASA_API_GEO_REMOVER, String.valueOf(Uteis.getHoraMinutoSegundo(new Date(), "hora")) , agente.getCodigo());
				}
			}	
		} catch (Exception e) {
			System.out.println("Erro - executeJobSerasaApiGeo - executeRemocaoSerasaApiGeo" + e.getMessage()+"- dia"+ Uteis.getDataComHora(new Date()));
			e.printStackTrace();
		}
	}

}
