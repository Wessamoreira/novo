package jobs;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.IndiceReajustePeriodoVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;

@Component
public class JobCalculoValorIndiceReajustePrecoProcessamentoInterrompido extends SuperControle implements InitializingBean {

	
	
	public JobCalculoValorIndiceReajustePrecoProcessamentoInterrompido() {
		super();	
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4962629527318163633L;

	
    public void run() {
        try{
        	
        	List<IndiceReajustePeriodoVO> listaRejustePeriodoVOs = getFacadeFactory().getIndiceReajustePeriodoFacade().consultarIndiceReajustePeriodoProcessando(null);
        	q:
        	for (IndiceReajustePeriodoVO indiceReajustePeriodoVO : listaRejustePeriodoVOs) {
        		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
				for(Thread thread : threadSet) {
					if(thread.getName().equals("IndiceReajustePeriodo"+indiceReajustePeriodoVO.getCodigo())) {
						continue q;
					}
				}
        		indiceReajustePeriodoVO.setProcessamentoAutomatico(true);
        		ProgressBarVO progressBarVO =  new ProgressBarVO();
    			progressBarVO.resetar();
    			progressBarVO.iniciar(0l, 10000, "Consultando contas.....", false, null, "");
    			indiceReajustePeriodoVO.setSituacaoExecucao(SituacaoExecucaoEnum.AGUARDANDO_PROCESSAMENTO);			
				getFacadeFactory().getIndiceReajustePeriodoFacade().alterarSituacaoIndiceReajustePeriodo(indiceReajustePeriodoVO.getCodigo(), SituacaoExecucaoEnum.AGUARDANDO_PROCESSAMENTO, "", false, null);
        		getFacadeFactory().getIndiceReajustePeriodoFacade().executarReajustePrecoContaReceber(progressBarVO, indiceReajustePeriodoVO.getIndiceReajusteVO(), indiceReajustePeriodoVO, null);
        		
        		
			}
        }catch (Exception e) {
        	System.out.println("erro JobCalculoValorIndiceReajustePrecoProcessamentoInterrompido");
            e.printStackTrace();
        }
    }


	@Override
	public void afterPropertiesSet() throws Exception {
		run();		
	}

	
	

}
