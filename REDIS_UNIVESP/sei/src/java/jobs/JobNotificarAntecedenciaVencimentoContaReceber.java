package jobs;

import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;


@Service
@Lazy
public class JobNotificarAntecedenciaVencimentoContaReceber extends SuperFacadeJDBC implements Runnable {

    /**
     * 
     */
    private static final long serialVersionUID = -5982402502284567415L;

    @Override
    public void run() {
		RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
		Stopwatch tempoExcecucao = new Stopwatch();
    	try{
			registroExecucaoJobVO.setNome("JobNotificarAntecedenciaVencimentoContaReceber");
			registroExecucaoJobVO.setDataInicio(new Date());
			tempoExcecucao.start();        	
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemVencimentoContaReceber();
            registroExecucaoJobVO.setTempoExecucao(tempoExcecucao.getElapsedTicks());
            getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, null);
        }catch (Exception e) {
			registroExecucaoJobVO.setErro(e.getMessage());
			tempoExcecucao.stop();
			registroExecucaoJobVO.setTempoExecucao(tempoExcecucao.getElapsedTicks());
			try {
				getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
            e.printStackTrace();
        }
    }

}
