package jobs;

import java.util.Date;
import java.util.Objects;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoOperacaoMoodleEnum;
import webservice.moodle.IntegracaoMoodleRS;

/**
 * job com a finalidade de executar a operação de atualização de notas de alunos
 * do moodle em {@link IntegracaoMoodleRS} no POST /vinculacoes/notas
 * 
 * @author Felipi Alves
 * @chamado 42530
 */
@SuppressWarnings("serial")
@Component
public class JobOperacaoNotaMoodle extends SuperControle implements Runnable {
	
	private static final long SEGUNDO = 1000;
	private static final long MINUTO = SEGUNDO * 60;
	private static final long EXECUTAR_MINUTO = MINUTO * 2;
	private static final long EXECUTAR_HORA = MINUTO * 5;
	static Boolean emExecucao = false;

	@Scheduled(fixedDelay = EXECUTAR_HORA, initialDelay = EXECUTAR_MINUTO)
	public void run() {
		if (!JobOperacaoNotaMoodle.emExecucao) {
			JobOperacaoNotaMoodle.emExecucao = true;
			Stopwatch stopwatch = new Stopwatch();
			RegistroExecucaoJobVO registroExecucaoJob = new RegistroExecucaoJobVO();
			try {
				stopwatch.start();
				registroExecucaoJob.setNome(JobsEnum.JOB_OPERACAO_NOTA_MOODLE.getName());
				registroExecucaoJob.setDataInicio(new Date());
				registroExecucaoJob.setTotal(0);
				registroExecucaoJob.setTotalSucesso(0);
				registroExecucaoJob.setTotalErro(0);
				getFacadeFactory().getOperacaoMoodleInterfaceFacade().realizarOperacaoMoodle(TipoOperacaoMoodleEnum.NOTAS, registroExecucaoJob);
			} catch (Exception e) {
				registroExecucaoJob.setErro((Uteis.isAtributoPreenchido(registroExecucaoJob.getErro()) ? registroExecucaoJob.getErro() + ", " : Constantes.EMPTY) + e.getMessage());
			} finally {
				stopwatch.stop();
				registroExecucaoJob.setTempoExecucao(stopwatch.getElapsedTicks());
				registroExecucaoJob.setDataTermino(new Date());
				try {
					getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (Objects.nonNull(stopwatch)) {
					removerObjetoMemoria(stopwatch);
					stopwatch = null;
				}
				if (Objects.nonNull(registroExecucaoJob)) {
					removerObjetoMemoria(registroExecucaoJob);
					registroExecucaoJob = null;
				}
			}
			JobOperacaoNotaMoodle.emExecucao = false;
		}
	}
}