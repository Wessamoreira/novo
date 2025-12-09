package jobs;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jobs.enumeradores.JobsEnum;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelVO;

@Service
@Lazy
public class JobNotificarRespondenteAvaliacaoInstitucional extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AvaliacaoInstitucionalVO avaliacaoInstitucionalVO;
	private List<AvaliacaoInstitucionalAnaliticoRelVO> avaliacaoInstitucionalAnaliticoRelVOs;
	private UsuarioVO usuarioVO;
	private PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO;

	public JobNotificarRespondenteAvaliacaoInstitucional() {
		super();		
	}
	
	public JobNotificarRespondenteAvaliacaoInstitucional(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, List<AvaliacaoInstitucionalAnaliticoRelVO> avaliacaoInstitucionalAnaliticoRelVOs, PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO, UsuarioVO usuarioVO) {
		super();		
		this.avaliacaoInstitucionalVO = avaliacaoInstitucionalVO;
		this.personalizacaoMensagemAutomaticaVO = personalizacaoMensagemAutomaticaVO;
		this.avaliacaoInstitucionalAnaliticoRelVOs = avaliacaoInstitucionalAnaliticoRelVOs;
		this.usuarioVO = usuarioVO;
	}

	@Override
	public void run() {
		RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
		registroExecucaoJobVO.setNome(JobsEnum.JOB_NOTIFICAR_RESPONDENTE_AVALIACAO_INSTITUCIONAL.getName());
		List<AvaliacaoInstitucionalVO> avaliacaoInstitucionalVOs =  null;
		try {			
			registroExecucaoJobVO.setDataInicio(new Date());
			if(Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO) && personalizacaoMensagemAutomaticaVO != null){
				realizarNotificacao(avaliacaoInstitucionalVO, avaliacaoInstitucionalAnaliticoRelVOs, personalizacaoMensagemAutomaticaVO, usuarioVO);
			}else{
//				this.personalizacaoMensagemAutomaticaVO = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_RESPONDENTE_AVALIACAO_INSTITUCIONAL, false, avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo(), null);
				if(personalizacaoMensagemAutomaticaVO != null && !personalizacaoMensagemAutomaticaVO.getDesabilitarEnvioMensagemAutomatica()){
					avaliacaoInstitucionalVOs =  getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarAvaliacaoInstitucionalNotificar();
					for(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO: avaliacaoInstitucionalVOs){
						if(avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
							avaliacaoInstitucionalVO.setDataInicio(new Date());
							avaliacaoInstitucionalVO.setDataFinal(new Date());
						}else {
						
						}
						realizarNotificacao(avaliacaoInstitucionalVO, avaliacaoInstitucionalAnaliticoRelVOs, personalizacaoMensagemAutomaticaVO, usuarioVO);
					}
				}
			}
		} catch (Exception e) {
			registroExecucaoJobVO.setErro(e.getMessage());			
//			try {
//				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
//			} catch (Exception ex) {				
//				
//			}
		}finally {
			Uteis.liberarListaMemoria(avaliacaoInstitucionalVOs);
			avaliacaoInstitucionalVOs = null;
			avaliacaoInstitucionalVO =  null;
			personalizacaoMensagemAutomaticaVO =  null;
			registroExecucaoJobVO =  null;
		}
	}
	
	
	public void realizarNotificacao(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, List<AvaliacaoInstitucionalAnaliticoRelVO> avaliacaoInstitucionalAnaliticoRelVOs, PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO, UsuarioVO usuarioVO){
//		try {
//			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoRespondenteAvaliacaoInstitucionalEspecifica(avaliacaoInstitucionalVO, avaliacaoInstitucionalAnaliticoRelVOs, personalizacaoMensagemAutomaticaVO, usuarioVO);
//		}catch(Exception e){
//			RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//			registroExecucaoJobVO.setDataInicio(new Date());
//			registroExecucaoJobVO.setNome(JobsEnum.JOB_NOTIFICAR_RESPONDENTE_AVALIACAO_INSTITUCIONAL.getName());
//			registroExecucaoJobVO.setErro("Erro ao notificar a avaliação institucional: "+avaliacaoInstitucionalVO.getNome()+":\n"+e.getMessage());			
//			try {
//				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
//			} catch (Exception ex) {				
//				
//			}finally {				
//				registroExecucaoJobVO = null;
//			}
//		}finally {
//		}
	}
}
