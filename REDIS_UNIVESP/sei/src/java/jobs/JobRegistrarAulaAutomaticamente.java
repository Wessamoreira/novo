package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.SuperControle;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

@Component
public class JobRegistrarAulaAutomaticamente extends SuperControle {
	

    /**
	 * 
	 */
	private static final long SEGUNDO = 1000;
	private static final long MINUTO = SEGUNDO * 60;
	private static final long HORA = MINUTO * 60;
	private static final long EXECUTAR_MINUTO = MINUTO * 3;
	public static final String TIME_ZONE = "America/Sao_Paulo";
	private static final long serialVersionUID = -1465638695992931795L;

	@Scheduled(cron="00 00 03 * * ?", zone=TIME_ZONE) // initialDelay 10 minuto esperando para rodar e depois fixedDelay roda de 1 em 1 horas caso ja tenha terminado de executar a primeira vez
    public void executarJobRegistrarAulaAutomaticamente() throws Exception {
		RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
		Stopwatch tempoExcecucao = new Stopwatch();
		registroExecucaoJobVO.setNome(JobsEnum.JOB_REGISTRAR_AULA_AUTOMATICAMENTE.getName());
		List<RegistroAulaVO> listaRegistrosAula = new ArrayList<RegistroAulaVO>();
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		try {
			tempoExcecucao.start();
			registroExecucaoJobVO.setDataInicio(new Date());
			List<HorarioProfessorDiaVO> horarioProfessorDiaVOs = getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorarioProfessorDiaRegistroAulaAutomaticamente(new UsuarioVO());
			for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorDiaVOs) {
				for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
					listaRegistrosAula = getFacadeFactory().getRegistroAulaFacade().realizarGeracaoRegistroAulaPeloHorarioProfessorDia(horarioProfessorDiaVO, new UsuarioVO(), horarioProfessorDiaItemVO.getTurmaVO(), horarioProfessorDiaItemVO.getDisciplinaVO(), horarioProfessorDiaVO.getHorarioProfessor().getProfessor(), horarioProfessorDiaVO.getAnoVigente(), horarioProfessorDiaVO.getSemestreVigente(), false);
					for (RegistroAulaVO registroAulaVO : listaRegistrosAula) {
						List<RegistroAulaVO> registroAulaVOs = new ArrayList<RegistroAulaVO>();
						registroAulaVO.setConteudo(horarioProfessorDiaItemVO.getConteudoRegistroAulaAutomatico());
						registroAulaVO.setNivelEducacional((horarioProfessorDiaItemVO.getNivelEducacional()));
						for (FrequenciaAulaVO frequenciaAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
							frequenciaAulaVO.setPresente(true);
						}
						registroAulaVOs.add(registroAulaVO);
						try {
							if (!Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getCodigo())) {
								configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_COMBOBOX, new UsuarioVO(), null);
							}
							UsuarioVO usuarioProfessor = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(registroAulaVO.getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
							if (!Uteis.isAtributoPreenchido(usuarioProfessor)) {
								usuarioProfessor = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
							}
							usuarioProfessor.setTipoUsuario(TipoPessoa.PROFESSOR.getValor());
							usuarioProfessor.setVisaoLogar("professor");
							usuarioProfessor.setPerfilAcesso(configuracaoGeralSistemaVO.getPerfilAcessoProfessor(TipoNivelEducacional.getEnum(registroAulaVO.getNivelEducacional())));
							registroAulaVO.setResponsavelRegistroAula(usuarioProfessor);
							getFacadeFactory().getRegistroAulaFacade().persistir(registroAulaVOs, registroAulaVOs.get(0).getConteudo(), registroAulaVOs.get(0).getTipoAula(), false, "RegistroAula", "Inserção pela Job Registro Aula Automatico", usuarioProfessor, false);
							horarioProfessorDiaItemVO.setRegistroAulaAutomaticoSucesso(true);
							horarioProfessorDiaItemVO.setMotivoErroRegistroAulaAutomatico("");
							registroExecucaoJobVO.setTotalSucesso(registroExecucaoJobVO.getTotalSucesso() + 1);
				        } catch (Exception e) {
				        	registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro() + 1);
				        	registroExecucaoJobVO.setErro(e.getMessage());
				        	horarioProfessorDiaItemVO.setRegistroAulaAutomaticoSucesso(false);
				        	horarioProfessorDiaItemVO.setMotivoErroRegistroAulaAutomatico(e.getMessage());
				        } finally {
				        	registroExecucaoJobVO.setTotal(registroExecucaoJobVO.getTotal() + 1);
							getFacadeFactory().getHorarioProfessorDiaItemFacade().atualizarSituacaoRegistroAulaAutomatico(horarioProfessorDiaItemVO);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tempoExcecucao.stop();
        	registroExecucaoJobVO.setTempoExecucao(tempoExcecucao.getElapsedTicks());
			getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);			
		}
	}	
}
