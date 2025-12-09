package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
public class JobNotificacaoProcessoSeletivoResultadoAprovacao extends SuperFacadeJDBC implements Runnable {

	private static final long serialVersionUID = 1L;

	@Override
	public void run() {
		try {
			List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs = new ArrayList<ResultadoProcessoSeletivoVO>(0);
			resultadoProcessoSeletivoVOs = getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarResultadoProcessoSeletivoEnvioMensagemAprovacaoResultado();
			UsuarioVO usuario = new UsuarioVO();
			for (ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO : resultadoProcessoSeletivoVOs) {
				if (!resultadoProcessoSeletivoVO.getEnviaMensagemResultadoProcessoSeletivo() && Uteis.compararDatasSemConsiderarHoraMinutoSegundo(resultadoProcessoSeletivoVO.getInscricao().getItemProcessoSeletivoDataProva().getDataLiberacaoResultado(), new Date())) {
					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemResultadoProcessoSeletivo(resultadoProcessoSeletivoVO, 0);
					getFacadeFactory().getResultadoProcessoSeletivoFacade().alterarEnviaMensagemResultadoProcessoSeletivo(resultadoProcessoSeletivoVO.getCodigo());
				}
				if (!resultadoProcessoSeletivoVO.getEnviaMensagemAprovacaoProcessoseletivo() && Uteis.compararDatasSemConsiderarHoraMinutoSegundo(resultadoProcessoSeletivoVO.getInscricao().getItemProcessoSeletivoDataProva().getDataLiberacaoResultado(), new Date())) {
					if(resultadoProcessoSeletivoVO.getResultadoPrimeiraOpcao().equals("AP") || resultadoProcessoSeletivoVO.getResultadoSegundaOpcao().equals("AP") || resultadoProcessoSeletivoVO.getResultadoTerceiraOpcao().equals("AP")) {
						getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAprovacaoProcessoSeletivo(resultadoProcessoSeletivoVO, usuario);
						getFacadeFactory().getResultadoProcessoSeletivoFacade().alterarEnviaMensagemAprovacaoProcessoSeletivo(resultadoProcessoSeletivoVO.getCodigo());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
