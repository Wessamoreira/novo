package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import controle.arquitetura.SuperControle;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Component
public class JobNotificacaoAlunoMensagemAtivacaoPreMatricula extends SuperControle {

    
    /**
     * 
     */
    private static final long serialVersionUID = -6424329078909514399L;

//  A: Segundos (0 - 59).
//  B: Minutos (0 - 59).
//  C: Horas (0 - 23).
//  D: Dia (1 - 31).
//  E: Mês (1 - 12).
//  F: Dia da semana (0 - 6).
	@Scheduled(cron = "0 0 1 * * ?")
    public void run() {
    	RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
		Stopwatch tempoExcecucao = new Stopwatch();		
		UsuarioVO usuarioOperacaoExterna = null ;
		StringBuilder logExecucaoJob = new StringBuilder();
		try {			
			System.out.println("JobNotificacaoAlunoMensagemAtivacaoPreMatricula inicio - " + Uteis.getDataComHora(new Date()));
			usuarioOperacaoExterna = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
			registroExecucaoJobVO.setNome(JobsEnum.JOB_NOTIFICAR_ALUNO_MENSAGEM_ATIVACAO_PREMATRICULA.getName());
			registroExecucaoJobVO.setDataInicio(new Date());
			tempoExcecucao.start(); 
			// consultando lista de personalizacao template pois existe vinculo com curso .
			List<PersonalizacaoMensagemAutomaticaVO> mensagemTemplates =  getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorParamentrosTemplate("",  TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ATIVACAO_PREMATRICULA_DOCUMENTO_ENTREGUE, null, "", false, usuarioOperacaoExterna);
			
			// verificado se a lista ta preenchido e pelo menos 1 template nao ta desabilitado envio de mensagens 
			if(Uteis.isAtributoPreenchido(mensagemTemplates) &&  mensagemTemplates.stream().anyMatch( template -> !template.getDesabilitarEnvioMensagemAutomatica())) {
				
				// filtro por um optional padrao que caso não tenha personalizacao com curso de acordo com a matricula sera enviado o padrao 
				Optional<PersonalizacaoMensagemAutomaticaVO> optMensagemTemplatePadrao = mensagemTemplates.stream().filter(template -> (!Uteis.isAtributoPreenchido(template.getCursoVO()))).findFirst();

				// consultando as matriculas aptas a realização .
				List<MatriculaVO> listaAlunos = getFacadeFactory().getMatriculaFacade().consultarMatriculasAtivasAptasRealizarNotificacaoMensagemAtivacaoPreMatriculaPorChamadaProcessoSeletivo();
				if(Uteis.isAtributoPreenchido(listaAlunos)) {
					registroExecucaoJobVO.setTotal(listaAlunos.size());
					List<MatriculaVO> listaAlunosEnviados = new ArrayList<MatriculaVO>(0);
					for (MatriculaVO matriculaAluno : listaAlunos) {	
						
						// busco na lista de template se existe algum template vinculado ao curso da matricula para ser usado 
						Optional<PersonalizacaoMensagemAutomaticaVO> optMensagemTemplateCurso = mensagemTemplates.stream().filter(template -> (Uteis.isAtributoPreenchido(template.getCursoVO()) && template.getCursoVO().getCodigo().equals(matriculaAluno.getCurso().getCodigo()) )).findFirst();
							PersonalizacaoMensagemAutomaticaVO templateUsar = null ;
							
							// caso exista para o curso sera  validado o  template que possui curso 
							if(optMensagemTemplateCurso != null && optMensagemTemplateCurso.isPresent()) {
								templateUsar = optMensagemTemplateCurso.get();
								
							// caso nao tenha vinculado ao curso da matricula sera usado o padrao 	
							}else if(optMensagemTemplatePadrao != null && optMensagemTemplatePadrao.isPresent()){
								templateUsar = optMensagemTemplatePadrao.get();
							}							
							// verificando se o template escolhido nao e nullo e se nao ta desabilitado o envio de mensagen
							if (templateUsar != null && !templateUsar.getDesabilitarEnvioMensagemAutomatica()) {								
								try {
						            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().enviarMensagemAtivacaoPreMatricula(matriculaAluno ,matriculaAluno.getMatriculaPeriodoVO().getAno(),matriculaAluno.getMatriculaPeriodoVO().getSemestre(), templateUsar,  usuarioOperacaoExterna);
						            registroExecucaoJobVO.setTotalSucesso(registroExecucaoJobVO.getTotalSucesso() + 1);
						            listaAlunosEnviados.add(matriculaAluno);
								}catch (Exception e) {
									logExecucaoJob.append("Falha ao enviar notificação de email (").append(matriculaAluno.getAluno().getEmail()).append(") para matrícula : ").append(matriculaAluno.getMatricula()).append(" erro  : ").append(e.getMessage()).append("\n");
									registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro() + 1);
									e.printStackTrace();							
								}
							
							}  
						}
						getFacadeFactory().getMatriculaFacade().atualizarDataEnvioNotificacaoMensagemAtivacaoPreMatricula(listaAlunosEnviados,usuarioOperacaoExterna);					
					}		
				
			
			
			
			
			}
			
			
			
			System.out.println("JobNotificacaoAlunoMensagemAtivacaoPreMatricula termino - " + Uteis.getDataComHora(new Date()));
        }catch (Exception e) {   
        	System.out.println("JobNotificacaoAlunoMensagemAtivacaoPreMatricula erro - " + Uteis.getDataComHora(new Date()));
        	if(e != null  && e.getMessage() != null ) {
        		logExecucaoJob.append("Falha ao enviar Notificação de Mensagens Ativação Pre-Matricula ): ").append(e.getMessage()).append("\n");
        	}else {
        		logExecucaoJob.append(" Ouve um erro na Job Notificar Aluno Mensagem Ativação Pre-Matricula , retornou nullo .");
        	}				
            e.printStackTrace();
        }finally {
        	tempoExcecucao.stop();
        	registroExecucaoJobVO.setErro(logExecucaoJob.toString());
        	registroExecucaoJobVO.setTempoExecucao(tempoExcecucao.getElapsedTicks());	
        	getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioOperacaoExterna);
        	
		}
    }

}
