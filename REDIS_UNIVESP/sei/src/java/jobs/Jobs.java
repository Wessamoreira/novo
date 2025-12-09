package jobs;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class Jobs implements SchedulingConfigurer {
	
	 @Override
     public void configureTasks(ScheduledTaskRegistrar taskRegistrar) { 
		 taskRegistrar.setScheduler(taskExecutor());
     }
	 
	 @Bean(destroyMethod="shutdown")
     public Executor taskExecutor() {
         return Executors.newScheduledThreadPool(20);
     }

	@Bean
	public JobEnviarEmail executarJobEnviarEmail() {
		return new JobEnviarEmail();
	}
	
	@Bean
	public JobDocumentacaoGED executarJobDocumentacaoGED() {
		return new JobDocumentacaoGED();
	}

//	@Bean
//	public JobSituacaoFuncionarioCargoAfastado executarJobSituacaoFuncionarioCargoAfastamento() {
//		return new JobSituacaoFuncionarioCargoAfastado();
//	}

//	@Bean
//	public JobSituacaoFuncionarioCargoFerias executarJobSituacaoFuncionarioCargoFerias() {
//		return new JobSituacaoFuncionarioCargoFerias();
//	}
	
//	@Bean
//	public JobAlunosBaixaFrequencia executarJobAlunosBaixaFrequencia() {
//		return new JobAlunosBaixaFrequencia();
//	}
	@Bean
	public jobExcluirAlunosIntegracaoMinhaBiblioteca executarjobExcluirAlunosIntegracaoMinhaBiblioteca() {
		return new jobExcluirAlunosIntegracaoMinhaBiblioteca();
	}
	
	
	@Bean
	public JobEnvioBoletoAluno executarJobEnvioBoletoAluno() {
		return new JobEnvioBoletoAluno();
	}
	
//	@Bean
//	public JobCalcularValorTemporarioContaReceberServico executarJobCalculoValorTemporarioContaReceber() {
//		return new JobCalcularValorTemporarioContaReceberServico();
//	}

//	@Bean
//	public JobCalculoValorIndiceReajustePrecoProcessamentoInterrompido executarJobIndiceReajusteProcessamentoInterrompido() {
//		return new JobCalculoValorIndiceReajustePrecoProcessamentoInterrompido();
//	}
	
//	@Bean
//	public JobConsultarArquivoRetornoLocalizarContaReceber executarJobConsultarArquivoRetornoLocalizarContaReceber() {
//		return new JobConsultarArquivoRetornoLocalizarContaReceber();
//	}
	
//	@Bean
//	public JobSerasaApiGeoProcessamentoInterrompido executarJobSerasaApiGeoProcessamentoInterrompido() {
//		return new JobSerasaApiGeoProcessamentoInterrompido();
//	}
	
//	@Bean
//	public JobBaixarCartaoCreditoRecorrenciaDCCServico executarJobBaixarCartaoCreditoRecorrenciaDCC() {
//		return new JobBaixarCartaoCreditoRecorrenciaDCCServico();
//	}
	
	@Bean
	public JobRegistrarAulaAutomaticamente executarJobRegistrarAulaAutomaticamente() {
		return new JobRegistrarAulaAutomaticamente();
	}
	
//	@Bean
//	public JobWebhookPix executarJobBancoBrasilPix() {
//		return new JobWebhookPix();
//	}

	@Bean
	public JobEstagioObrigatorio executarJobEstagioObrigatorio() {
		return new JobEstagioObrigatorio();
	}
	
	@Bean
	public JobEstagioEnsalamento executarJobEstagioEnsalamento() {
		return new JobEstagioEnsalamento();
	}
	
	@Bean
	public JobTccEnsalamento executarJobTccEnsalamento() {
		return new JobTccEnsalamento();
	}
	
	@Bean
	public JobEstagioDesensalamento executarJobEstagioDesensalamento() {
		return new JobEstagioDesensalamento();
	}
	
	@Bean
	public JobTccDesensalamento executarJobTccDesensalamento() {
		return new JobTccDesensalamento();
	}
	
	@Bean
	public JobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign executarJobValidacaoDocumentoAssinadoEnviadosAte30DiasPorProvedorCertiSign() {
		return new JobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign();
	}
	
	@Bean
	public JobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign executarJobValidacaoDocumentoAssinadoEnviadosSuperiorA30DiasPorProvedorCertiSign() {
		return new JobValidarAssinaturasDocumentoAssinadoPorProvedorCertiSign();
	}

	@Bean
	public JobValidarAssinaturasDocumentoAssinadoPorProvedorTechCert executarJobValidacaoDocumentoAssinadoEnviadosAte30DiasPorProvedorTechCert() {
		return new JobValidarAssinaturasDocumentoAssinadoPorProvedorTechCert();
	}

	@Bean
	public JobValidarAssinaturasDocumentoAssinadoPorProvedorTechCert executarJobValidacaoDocumentoAssinadoEnviadosSuperiorA30DiasPorProvedorTechCert() {
		return new JobValidarAssinaturasDocumentoAssinadoPorProvedorTechCert();
	}
	
	@Bean
	public JobAlterarSituacaoMatriculaIntegralizada executarJobAlteracaoSituacaoMatriculaIntegralizadaParaFinalizada() {
		return new JobAlterarSituacaoMatriculaIntegralizada();
	}
	
	@Bean
	public JobAtualizarAjuda executarJobArtefatoAjuda() {
		return new JobAtualizarAjuda();
	}
	
	@Bean
	public JobBlackboardFechamentoNota executarJobFechamentoNota() {
		return new JobBlackboardFechamentoNota();
	}
	
	@Bean
	public JobNotificacaoAlunoMensagemAtivacaoPreMatricula executarJobNotificacaoAlunoMensagemAtivacaoPreMatricula() {
		return new JobNotificacaoAlunoMensagemAtivacaoPreMatricula();
	}
	
	@Bean
	public JobNotificacaoPeriodoEntregaRelatorioFacilitador executarJobNotificacaoPeriodoEntregaRelatorioFacilitador() {
		return new JobNotificacaoPeriodoEntregaRelatorioFacilitador();
	}
	
	@Bean
	public JobOperacaoMensagemMoodle executarJobOperacaoMensagemMoodle() {
		return new JobOperacaoMensagemMoodle();
	}
	
	@Bean
	public JobOperacaoNotaMoodle executarJobOperacaoNotaMoodle() {
		return new JobOperacaoNotaMoodle();
	}
	
	@Bean
	public JobSymplicty executarJobSymplicty() {
		return new JobSymplicty();
	}

	@Bean
	public JobOperacoesIntegracaoMestreGR executarJobOperacoesIntegracaoMestreGR() {
		return new JobOperacoesIntegracaoMestreGR();
	}
	
	@Bean
	public JobMarcacaoAusenciaAtaColacaoGrauNaoAssinada executarJobMarcacaoAusenciaAtaColacaoGrauNaoAssinada() {
		return new JobMarcacaoAusenciaAtaColacaoGrauNaoAssinada();
	}
}
