package jobs;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.IndiceReajustePeriodoVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobCalculoValorIndiceReajustePreco extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 953893850950791441L;
	private List<ContaReceberVO> contaReceberVOs;
	
	@Override
    public void run() {
        try{
        	
        	List<IndiceReajustePeriodoVO> listaRejustePeriodoVOs = getFacadeFactory().getIndiceReajustePeriodoFacade().consultarIndiceReajustePeriodoAguardandoProcessamento(null);
        	for (IndiceReajustePeriodoVO indiceReajustePeriodoVO : listaRejustePeriodoVOs) {
        		indiceReajustePeriodoVO.setProcessamentoAutomatico(true);
        		ProgressBarVO progressBarVO =  new ProgressBarVO();
    			progressBarVO.resetar();
    			progressBarVO.iniciar(0l, 10000, "Consultando contas.....", false, null, "");
        		getFacadeFactory().getIndiceReajustePeriodoFacade().executarReajustePrecoContaReceber(progressBarVO, indiceReajustePeriodoVO.getIndiceReajusteVO(), indiceReajustePeriodoVO, null);
			}
        }catch (Exception e) {
        	System.out.println("erro JobCalculoValorIndiceReajustePreco");
            e.printStackTrace();
        }
    }

	public List<ContaReceberVO> getContaReceberVOs() {		
		return contaReceberVOs;
	}

	public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
		this.contaReceberVOs = contaReceberVOs;
	}
	
	

}
