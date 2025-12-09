package jobs;

import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobExcluirNegociacaoRecebimentoVencida extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 298401750769957865L;

	@Override
	public void run() {
		RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
		try {
			registroExecucaoJobVO.setDataInicio(new Date());
			registroExecucaoJobVO.setNome("Job Excluir Negociacao Conta Receber Vencida");
			getFacadeFactory().getNegociacaoContaReceberFacade().excluirNegociacaoRecebimentoVencida(registroExecucaoJobVO);
			if(!registroExecucaoJobVO.getErro().trim().isEmpty()) {
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
			}
		} catch (Exception e) {
			registroExecucaoJobVO.setErro(registroExecucaoJobVO.getErro()+"\n"+e.getMessage());
			registroExecucaoJobVO.setDataTermino(new Date());
			try {
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
			} catch (Exception e1) {			
				e1.printStackTrace();
			}
		}
		
	}

}
