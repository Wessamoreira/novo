package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobEstagioObrigatorio extends SuperControle {

	private static final long serialVersionUID = 8743259907143464792L;
	
//  A: Segundos (0 - 59).
//  B: Minutos (0 - 59).
//  C: Horas (0 - 23).
//  D: Dia (1 - 31).
//  E: Mês (1 - 12).
//  F: Dia da semana (0 - 6).
	@Scheduled(cron = "0 30 04 * * ?")
	public void run() {
		try {
			UsuarioVO usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			ConfiguracaoEstagioObrigatorioVO config = getFacadeFactory().getConfiguracaoEstagioObrigatorioFacade().consultarPorConfiguracaoEstagioPadrao(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogadoClone());

//			// TEMPO ANÁLISE DE RELATÓRIO FINAL ESTÁGIO OBRIGATÓRIO EXCEDIDO
			this.executarTempoAnaliseRelatorioFinalEstagioObrigatorioExcedido(config);
//			// PERIODO DE ANALISE DE DO APROVEITAMENTO DE ESTÁGIO ENCERRADO
			this.executarPeriodoAnaliseAproveitamentoEstagioEncerrado(config);
//			// PERIODO DE ANALISE DE DO EQUIVALÊNCIA DE ESTÁGIO ENCERRADO
			this.executarPeriodoAnaliseEquivalenciaEstagioEncerrado(config);
//			// PERIODO DE ENTREGA CORREÇÃO DO EQUIVALÊNCIA DE ESTÁGIO ENCERRADO
			this.executarPeriodoEntregaCorrecaoEquivalenciaEstagioEncerrado(config);
//			// PERIODO DE ENTREGA CORREÇÃO DO APROVEITAMENTO DE ESTÁGIO ENCERRADO
			this.executarPeriodoEntregaCorrecaoAproveitamentoEstagioEncerrado(config);
//			// TEMPO ENVIO NOVO DE RELATÓRIO FINAL ESTÁGIO OBRIGATORIO EXCEDIDO
			this.executarTempoNovoRelatorioFinalEstagioObrigatorioExedido(config);
			// Notificacao Periodica por falta de assinatura do estagio
			this.executarPeriodicidadeNotificacaoAssinaturaEstagio(config, usuarioOperacaoExterna);
			// Notificacao de cancelamento do estagio por falta de assinatura
			this.executarNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio(config, usuarioOperacaoExterna);
		} catch (Exception e) {
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "JobEstagioObrigatorio msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void executarTempoAnaliseRelatorioFinalEstagioObrigatorioExcedido(ConfiguracaoEstagioObrigatorioVO config) {
		try {
			// Consultar estagios que seram notificados, que tempo de analise foi execido
			// pelo responsável
			// notifica o responsavel
			// salva no estagio data notificacao dessa mensagem, pois de tempos em tempos
			// deverá notificar
			// o respomsavel novamente, caso o tempo de analise continue vencido
			List<EstagioVO> lista = getFacadeFactory().getEstagioFacade().consultaEstagioPeriodoAnaliseRelatorioFinalEncerrado(config.getQtdDiasMaximoParaRespostaAnaliseAproveitamento());
			for (EstagioVO estagio : lista) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAnaliseEstagioObrigatorioRelatorioFinal(estagio, config, getUsuarioLogadoClone());
				getFacadeFactory().getEstagioFacade().alterarDataLimiteAnaliseNotificacao(estagio, getUsuarioLogadoClone());
			}
		} catch (Exception e) {
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarTempoAnaliseRelatorioFinalEstagioObrigatorioExcedido msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
			e.printStackTrace();
		}
	}

	public void executarPeriodoAnaliseAproveitamentoEstagioEncerrado(ConfiguracaoEstagioObrigatorioVO config) {
		try {
			//// verifica os estagios do tipo aproveitamenteo que estão em analise e que o
			//// periodo de analise está encerrado
			/// apos encontrar, irá notificar o responsavel avisando do encerramento do
			//// prazo
			// será enviado de forma periodica de acordo com campo na configuração do
			//// estagio.
			List<EstagioVO> lista = getFacadeFactory().getEstagioFacade().consultaEstagioPeriodoAnaliseAproveitamentoEncerrado(config.getQtdDiasMaximoParaRespostaAnaliseAproveitamento());
			for (EstagioVO estagio : lista) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemPeriodoEntregaAnaliseAproveitamentoEncerrado(estagio, config, getUsuarioLogadoClone());
				getFacadeFactory().getEstagioFacade().alterarDataLimiteAnaliseNotificacao(estagio, getUsuarioLogadoClone());
			}
		} catch (Exception e) {
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarPeriodoAnaliseAproveitamentoEstagioEncerrado msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
			e.printStackTrace();
		}
	}

	public void executarPeriodoAnaliseEquivalenciaEstagioEncerrado(ConfiguracaoEstagioObrigatorioVO config) {
		try {
			//// verifica os estagios do tipo aproveitamenteo que estão em analise e que o
			//// periodo de analise está encerrado
			/// apos encontrar, irá notificar o responsavel avisando do encerramento do
			//// prazo
			// será enviado de forma periodica de acordo com campo na configuração do
			//// estagio.
			List<EstagioVO> lista = getFacadeFactory().getEstagioFacade().consultaEstagioPeriodoAnaliseEquivalenciaEncerrado(config.getQtdDiasMaximoParaRespostaAnaliseEquivalencia());
			for (EstagioVO estagio : lista) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemPeriodoEntregaAnaliseEquivalenciaEncerrado(estagio, config, getUsuarioLogadoClone());
				getFacadeFactory().getEstagioFacade().alterarDataLimiteAnaliseNotificacao(estagio, getUsuarioLogadoClone());
			}
		} catch (Exception e) {
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarPeriodoAnaliseEquivalenciaEstagioEncerrado msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
			e.printStackTrace();
		}
	}

	public void executarTempoNovoRelatorioFinalEstagioObrigatorioExedido(ConfiguracaoEstagioObrigatorioVO config) {
		try {
			List<EstagioVO> lista = getFacadeFactory().getEstagioFacade().consultaEstagioCorrecaoRelatorioFinalEncerrado(config.getQtdDiasMaximoParaRespostaAnaliseEquivalencia());
			for (EstagioVO estagio : lista) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioSolicitacaoCorrecaoAluno(estagio, config, getUsuarioLogadoClone());
				getFacadeFactory().getEstagioFacade().alterarDataLimiteCorrecaoNotificacao(estagio, getUsuarioLogadoClone());
			}
		} catch (Exception e) {
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarTempoNovoRelatorioFinalEstagioObrigatorioExedido msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
			e.printStackTrace();
		}
	}

	public void executarPeriodoEntregaCorrecaoAproveitamentoEstagioEncerrado(ConfiguracaoEstagioObrigatorioVO config) {
		try {
			List<EstagioVO> lista = getFacadeFactory().getEstagioFacade().consultaEstagioCorrecaoAproveitamentoEncerrado(config.getQtdDiasMaximoParaRespostaAnaliseEquivalencia());
			for (EstagioVO estagio : lista) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioAproveitamentoSolicitacaoCorrecaoAluno(estagio, config, getUsuarioLogadoClone());
				getFacadeFactory().getEstagioFacade().alterarDataLimiteCorrecaoNotificacao(estagio, getUsuarioLogadoClone());
			}
		} catch (Exception e) {
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarPeriodoEntregaCorrecaoAproveitamentoEstagioEncerrado msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
			e.printStackTrace();
		}
	}

	public void executarPeriodoEntregaCorrecaoEquivalenciaEstagioEncerrado(ConfiguracaoEstagioObrigatorioVO config) {
		try {
			List<EstagioVO> lista = getFacadeFactory().getEstagioFacade().consultaEstagioCorrecaoEquivalenciaEncerrado(config.getQtdDiasMaximoParaRespostaAnaliseEquivalencia());
			for (EstagioVO estagio : lista) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioEquivalenciaSolicitacaoCorrecaoAluno(estagio, config, getUsuarioLogadoClone());
				getFacadeFactory().getEstagioFacade().alterarDataLimiteCorrecaoNotificacao(estagio, getUsuarioLogadoClone());
			}
		} catch (Exception e) {
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarPeriodoEntregaCorrecaoEquivalenciaEstagioEncerrado msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
			e.printStackTrace();
		}
	}
	
	public void executarPeriodicidadeNotificacaoAssinaturaEstagio(ConfiguracaoEstagioObrigatorioVO config, UsuarioVO usuarioOperacaoExterna) {
		try {
			if(config.getQtdDiasNotificacaoAssinaturaEstagio() > 0) {
				List<EstagioVO> lista = getFacadeFactory().getEstagioFacade().consultaEstagioAguardandoAssinatura(config.getQtdDiasNotificacaoAssinaturaEstagio());
				for (EstagioVO estagio : lista) {
					realizarProcessamentoPeriodicidadeNotificacaoAssinaturaEstagio(config, usuarioOperacaoExterna, estagio);
				}	
			}
		} catch (Exception e) {
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarPeriodicidadeNotificacaoAssinaturaEstagio msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
			e.printStackTrace();
		}
	}

	private void realizarProcessamentoPeriodicidadeNotificacaoAssinaturaEstagio(ConfiguracaoEstagioObrigatorioVO config, UsuarioVO usuarioOperacaoExterna, EstagioVO estagio) throws Exception {
		try {
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEstagioObrigatorioNotificacaoAguardandoAssinatura(estagio, config, usuarioOperacaoExterna);	
		} catch (Exception e) {
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarPeriodicidadeNotificacaoAssinaturaEstagio msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
		}
	}
	
	public void executarNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio(ConfiguracaoEstagioObrigatorioVO config, UsuarioVO usuarioOperacaoExterna) {
		try {
			if(config.getQtdDiasMaximoParaAssinaturaEstagio() > 0) {
				List<EstagioVO> lista = getFacadeFactory().getEstagioFacade().consultaEstagioParaCancelamentoPorFaltaDeAssinatura(config.getQtdDiasMaximoParaAssinaturaEstagio());
				for (EstagioVO estagio : lista) {
					try {
						if (executarValidacaoNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio(estagio, usuarioOperacaoExterna, config.getQtdDiasMaximoParaAssinaturaEstagio())) {
							getFacadeFactory().getEstagioFacade().realizarProcessamentoNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio(config, usuarioOperacaoExterna, estagio);
						}
					} catch (Exception e) {
						try {
							RegistroExecucaoJobVO registroExecucaoJob = new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio msg:" + e.getMessage());
							registroExecucaoJob.setDataTermino(new Date());
							getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}	
			}
		} catch (Exception e) {
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio1 msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public boolean executarValidacaoNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio(EstagioVO estagio, UsuarioVO usuarioOperacaoExterna, Integer qtdDiasMaximoParaAssinaturaEstagio) throws Exception {
		List<DocumentoAssinadoVO> lista = new ArrayList<DocumentoAssinadoVO>(0);
		lista.add(estagio.getDocumentoAssinadoVO());			
		try {
			getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorCertiSign(lista, usuarioOperacaoExterna);
			return getFacadeFactory().getEstagioFacade().consultaSeExisteEstagioParaCancelamentoPorFaltaDeAssinatura(qtdDiasMaximoParaAssinaturaEstagio, estagio.getDocumentoAssinadoVO().getCodigo());
		} catch (ConsistirException ce) {
			try {
				getFacadeFactory().getEstagioFacade().atualizarCampoSqlMensagem(estagio, "executarValidacaoNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio-"+ ce.getToStringMensagemErro(), usuarioOperacaoExterna);
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarValidacaoNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio - estagio"+estagio.getCodigo()+" matricula "+estagio.getMatriculaVO().getMatricula()+" msg:" + ce.getToStringMensagemErro());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			try {
				getFacadeFactory().getEstagioFacade().atualizarCampoSqlMensagem(estagio, "executarValidacaoNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio-"+ e.getMessage(), usuarioOperacaoExterna);
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_ESTAGIO_OBRIGATORIO.getName(), "executarValidacaoNotificacaoDeCancelamentoPorFaltaDeAssinaturaEstagio - estagio"+estagio.getCodigo()+" matricula "+estagio.getMatriculaVO().getMatricula()+" msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	

}
