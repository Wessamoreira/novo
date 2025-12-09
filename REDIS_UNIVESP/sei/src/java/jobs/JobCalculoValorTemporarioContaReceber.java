package jobs;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobCalculoValorTemporarioContaReceber extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 953893850950791441L;
	private List<ContaReceberVO> contaReceberVOs;
	
	@Override
    public void run() {
        try{        	
        	getFacadeFactory().getContaReceberFacade().realizarProcessamentoValorFinalContaReceberAtualizadoComAcrescimosEDescontos(null, getContaReceberVOs(), true, "", true, new UsuarioVO(), false , false , null , null, false);        	                     
        }catch (Exception e) {
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
