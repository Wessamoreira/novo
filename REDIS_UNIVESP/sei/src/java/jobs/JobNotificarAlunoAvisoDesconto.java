package jobs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificarAlunoAvisoDesconto extends SuperFacadeJDBC implements Runnable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = -3311408265691102591L;

	@Override
	    public void run() {
	        try{
	        	List<UnidadeEnsinoVO> listaUnidade = new ArrayList<>(0);
	    		listaUnidade.addAll(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoConfiguracoesPorGestaoEnvioMensagemAutomatica());
	    		for (UnidadeEnsinoVO obj : listaUnidade) {
	    			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAvisoDesconto(obj.getCodigo(), obj.getConfiguracoes().getCodigo());	
	    		}
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
