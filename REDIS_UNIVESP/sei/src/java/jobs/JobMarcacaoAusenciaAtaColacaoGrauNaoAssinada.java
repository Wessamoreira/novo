package jobs;

import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * Job com finalidade de executar uma automação da marcação de ausência em ATA de Colação de Grau não assinada.
 * Esta Job deve rodar as 1h da manhã e deverá pegar todas as Atas de Colção de Grau que a data limite de assinatura passou
 * e que ainda esteja pendente de assinatura do aluno, esta rotina irá rejeitar o documento do lado do SEI e marcar a assinatura 
 * como Rejeita com a mensagem "O aluno não Assinou a ata dentro do prazo estipulado <Colocar a data limite da assinatura>", 
 * também será alterado a situação da programação de formatura aluno para "NO", porém so deve ser executada para 
 * as matriculas com a situacao diferente de Formado.
 * 
 * @author Jeff
 * @chamado 45158
 *
 */

@Component
public class JobMarcacaoAusenciaAtaColacaoGrauNaoAssinada extends SuperControle {
	
	private static final long serialVersionUID = -5982402502284567415L;
	public static final String TIME_ZONE = "America/Sao_Paulo";
	
	@Scheduled(cron = "00 00 01 * * ?", zone=TIME_ZONE) // Job programada para se executar todos os dias da semana as 01:00h da manha
	public void executarJobMarcacaoAusenciaAtaColacaoGrauNaoAssinada() {
		RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
		Stopwatch tempoExecucao = new Stopwatch();
		registroExecucaoJobVO.setNome(JobsEnum.JOB_MARCACAO_AUSENCIA_ATA_COLACAO_GRAU_NAO_ASSINADA.getName());
		try {
			tempoExecucao.start();
			registroExecucaoJobVO.setDataInicio(new Date());
			System.out.println("JobMarcacaoAusenciaAtaColacaoGrauNaoAssinada Inicio - " + Uteis.getDataComHora(new Date()));
			List<DocumentoAssinadoVO> documentoAssinadoVOs = getFacadeFactory().getDocumentoAssinadoFacade().consultarAtaColacaoGrauDataLimiteAssinaturaVencido();
			for (DocumentoAssinadoVO documentoAssinadoVO : documentoAssinadoVOs) {
				try {
					getFacadeFactory().getDocumentoAssinadoPessoaFacade().rejeitarAtaColacaoGrauDataLimiteAssinaturaVencido(documentoAssinadoVO.getCodigoRejeitarAtaColacaoGrau(), documentoAssinadoVO.getProgramacaoFormaturaVO().getDataLimiteAssinaturaAta(), getUsuarioLogado());
					if (!documentoAssinadoVO.getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVO().getColouGrau().equals("NO")) {
						getFacadeFactory().getProgramacaoFormaturaAlunoFacade().alterarSituacaoColacaoGrauAluno(documentoAssinadoVO.getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVO().getCodigo(), getUsuarioLogado());
					}
					registroExecucaoJobVO.setTotalSucesso(registroExecucaoJobVO.getTotalSucesso() + 1);
				} catch (Exception e) {
					registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro() + 1);
					registroExecucaoJobVO.setErro(e.getMessage());
				} finally {
					registroExecucaoJobVO.setTotal(registroExecucaoJobVO.getTotal() + 1);
				}
			}
			registroExecucaoJobVO.setDataTermino(new Date());
			System.out.println("JobMarcacaoAusenciaAtaColacaoGrauNaoAssinada Termino - " + Uteis.getDataComHora(new Date()));
		} catch (Exception e) {
			System.out.println("JobMarcacaoAusenciaAtaColacaoGrauNaoAssinada Erro - " + Uteis.getDataComHora(new Date()));
			e.printStackTrace();
		} finally {
			tempoExecucao.stop();
			registroExecucaoJobVO.setTempoExecucao(tempoExecucao.getElapsedTicks());
			try {
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
