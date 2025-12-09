package jobs;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jobs.enumeradores.JobsEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.administrativo.ConfiguracaoGeralSistemaInterfaceFacade;
import negocio.interfaces.arquitetura.UsuarioInterfaceFacade;
import negocio.interfaces.financeiro.ConfiguracaoFinanceiroInterfaceFacade;
import negocio.interfaces.job.RegistroExecucaoJobInterfaceFacade;
import negocio.interfaces.protocolo.RequerimentoInterfaceFacade;

@Service
@Lazy
public class JobRemoverRequerimentoAntigosBaseDados implements Runnable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9096742290608469959L;
	@Autowired
	private ConfiguracaoGeralSistemaInterfaceFacade configuracaoGeralSistemaFacade;
	@Autowired
	private ConfiguracaoFinanceiroInterfaceFacade configuracaoFinanceiroFacade;
	@Autowired
	private UsuarioInterfaceFacade usuarioFacade;
	@Autowired
	private RequerimentoInterfaceFacade requerimentoFacade;
	@Autowired
	private RegistroExecucaoJobInterfaceFacade registroExecucaoJobFacade;

    public void run() {
    	List<RequerimentoVO> listaRequerimentos = null;
    	ConsistirException ex = null;
    	UsuarioVO usuResp = null;
    	ConfiguracaoGeralSistemaVO conf = null;
    	ConfiguracaoFinanceiroVO confFinan = null;
        try {     		
            conf = getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
            confFinan = getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
            if(Uteis.isAtributoPreenchido(conf.getUsuarioResponsavelOperacoesExternas().getCodigo())) {
            	usuResp = getUsuarioFacade().consultarPorPessoa(conf.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
            }
            if(usuResp == null) {
            	usuResp = getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
            }
            listaRequerimentos = getRequerimentoFacade().consultarRequerimentoAtrasadosParaExclusao(usuResp, confFinan);
            ex = new ConsistirException();
            for (Iterator<RequerimentoVO> iterator = listaRequerimentos.iterator(); iterator.hasNext();) {
            	try{
            		RequerimentoVO req = (RequerimentoVO) iterator.next();                        	
            		req.setMotivoIndeferimento("Requerimento Indeferido Automaticamente Por Falta de Pagamento");
            		getRequerimentoFacade().indeferirRequerimento(req, false, true, usuResp);
            		ex.adicionarListaMensagemErro("Requerimento  "+req.getCodigo()+" Indeferido Automaticamente Por Falta de Pagamento");
            	}catch(Exception e){
            		ex.adicionarListaMensagemErro(e.getMessage());
            	}
			}
            RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
			registroExecucaoJobVO.setDataInicio(new Date());
			registroExecucaoJobVO.setNome(JobsEnum.JOB_EXCLUIR_REQUERIMENTO_DAFASADO.getName());
			registroExecucaoJobVO.setErro(ex.getToStringMensagemErro().toString());
            getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
        } catch (Exception e) {
        	if(e.getMessage() == null) {
        		RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
    			registroExecucaoJobVO.setDataInicio(new Date());
    			registroExecucaoJobVO.setNome(JobsEnum.JOB_EXCLUIR_REQUERIMENTO_DAFASADO.getName());
    			registroExecucaoJobVO.setErro("Nullpointer");
    			try {
					getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}else {
        		RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
    			registroExecucaoJobVO.setDataInicio(new Date());
    			registroExecucaoJobVO.setNome(JobsEnum.JOB_EXCLUIR_REQUERIMENTO_DAFASADO.getName());
    			registroExecucaoJobVO.setErro(e.getMessage().toString());
        		try {
					getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        } finally {
        	listaRequerimentos = null;
        	ex = null;
        	usuResp = null;
        	conf = null;
        	confFinan = null;
		}
    }

	public ConfiguracaoGeralSistemaInterfaceFacade getConfiguracaoGeralSistemaFacade() {
		
		return configuracaoGeralSistemaFacade;
	}

	public void setConfiguracaoGeralSistemaFacade(ConfiguracaoGeralSistemaInterfaceFacade configuracaoGeralSistemaFacade) {
		this.configuracaoGeralSistemaFacade = configuracaoGeralSistemaFacade;
	}

	public ConfiguracaoFinanceiroInterfaceFacade getConfiguracaoFinanceiroFacade() {
		
		return configuracaoFinanceiroFacade;
	}

	public void setConfiguracaoFinanceiroFacade(ConfiguracaoFinanceiroInterfaceFacade configuracaoFinanceiroFacade) {
		this.configuracaoFinanceiroFacade = configuracaoFinanceiroFacade;
	}

	public UsuarioInterfaceFacade getUsuarioFacade() {
		
		return usuarioFacade;
	}

	public void setUsuarioFacade(UsuarioInterfaceFacade usuarioFacade) {
		this.usuarioFacade = usuarioFacade;
	}

	public RequerimentoInterfaceFacade getRequerimentoFacade() {
		
		return requerimentoFacade;
	}

	public void setRequerimentoFacade(RequerimentoInterfaceFacade requerimentoFacade) {
		this.requerimentoFacade = requerimentoFacade;
	}

	public RegistroExecucaoJobInterfaceFacade getRegistroExecucaoJobFacade() {		
		return registroExecucaoJobFacade;
	}

	public void setRegistroExecucaoJobFacade(RegistroExecucaoJobInterfaceFacade registroExecucaoJobFacade) {
		this.registroExecucaoJobFacade = registroExecucaoJobFacade;
	}


}
