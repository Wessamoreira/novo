package jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobSituacaoFuncionarioCargoFerias extends SuperControle {

	private static final long serialVersionUID = 5561906177399709027L;

	private static final long SEGUNDO = 1000;
	private static final long MINUTO = SEGUNDO * 60;
	private static final long HORA = MINUTO * 60;
	private static final long DIA = HORA * 24;

	@Scheduled(fixedRate = DIA)
	public void run() {
		try {
			if (Uteis.isAplicacaoPrincipal()) {
				getFacadeFactory().getMarcacaoFeriasInterfaceFacade().processaSituacaoFuncionarioFerias();
			}
		} catch (Exception e) {
			System.out.println("Erro ao alterar a situacao do funcionario de Ferias para Ativo");
			e.printStackTrace();
		}
	}

	public JobSituacaoFuncionarioCargoFerias getJobSituacaoFuncionarioCargoFerias() {
		return SpringUtil.getApplicationContext().getBean(JobSituacaoFuncionarioCargoFerias.class);
	}

}
