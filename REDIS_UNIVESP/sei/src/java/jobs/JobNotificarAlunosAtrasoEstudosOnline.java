package jobs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificarAlunosAtrasoEstudosOnline extends SuperFacadeJDBC implements Runnable {

	@Override
	public void run() {

		try {
			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
			matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarAlunosConteudoAtrasadoEstudosOnline();
			for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
				if (matriculaPeriodoTurmaDisciplinaVO.getSituacaoNotificacaoAtrasoEstudosAluno().equals("NOT1")) {
					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacao1AtrasoEstudosAluno(matriculaPeriodoTurmaDisciplinaVO, null);
				} else if (matriculaPeriodoTurmaDisciplinaVO.getSituacaoNotificacaoAtrasoEstudosAluno().equals("NOT2")) {
					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacao2AtrasoEstudosAluno(matriculaPeriodoTurmaDisciplinaVO, null);
				} else if(matriculaPeriodoTurmaDisciplinaVO.getSituacaoNotificacaoAtrasoEstudosAluno().equals("NOT3")) {
					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacao3AtrasoEstudosAluno(matriculaPeriodoTurmaDisciplinaVO, null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
