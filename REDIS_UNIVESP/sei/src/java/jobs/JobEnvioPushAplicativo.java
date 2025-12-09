package jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoMobileVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.PushNotificacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

/**
 * @author Victor Hugo de Paula Costa - 7 de nov de 2016
 *
 */
public class JobEnvioPushAplicativo extends SuperFacadeJDBC implements Runnable {

    private ComunicacaoInternaVO comunicacaoInternaVO;
    private UsuarioVO usuarioVO;
    private List<ComunicadoInternoDestinatarioVO> comunicadoInternoDestinatarioVOs; 

    /**
     * @author Victor Hugo de Paula Costa - 7 de nov de 2016
     */
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(JobEnvioPushAplicativo.class.getName());

    /**
     * @param comunicacaoInternaVO
     */
    public JobEnvioPushAplicativo(ComunicacaoInternaVO comunicacaoInternaVO, UsuarioVO usuarioVO) {
		super();
		comunicadoInternoDestinatarioVOs = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
		comunicadoInternoDestinatarioVOs.addAll(comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs());
		this.comunicacaoInternaVO = comunicacaoInternaVO;
		this.usuarioVO = usuarioVO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
	try {
		comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().clear();
		comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().addAll(comunicadoInternoDestinatarioVOs);
	    ConfiguracaoMobileVO configuracaoMobileVO = getFacadeFactory().getConfiguracaoMobileFacade().consultarPorUnidadeEnsino(comunicacaoInternaVO.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
	    if (!Uteis.isAtributoPreenchido(configuracaoMobileVO.getCodigo())) {
	    		configuracaoMobileVO = getFacadeFactory().getConfiguracaoMobileFacade().consultarConfiguracaoPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);	
	    }
	    if (!configuracaoMobileVO.getCodigo().equals(0)) {
		List<UsuarioVO> usuarioVOs = getFacadeFactory().getUsuarioFacade().consultaRapidaUsuariosEnvioPushAplicativo(comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		for (UsuarioVO usuarioVO : usuarioVOs) {
			if (usuarioVO.getPessoa().getProfessor()) {
				PushNotificacao.send(usuarioVO.getCelular(), usuarioVO.getTokenAplicativo(), comunicacaoInternaVO.getResponsavel().getNome(), comunicacaoInternaVO.getAssunto(), configuracaoMobileVO.getIdRemetenteGoogleProfessor(), configuracaoMobileVO.getCertificadoAPNSAppleProfessor(), configuracaoMobileVO.getSenhaCertificadoApnsProfessor(), configuracaoMobileVO.getCertificadoDestribuicaoProfessor());	
			}
		    PushNotificacao.send(usuarioVO.getCelular(), usuarioVO.getTokenAplicativo(), comunicacaoInternaVO.getResponsavel().getNome(), comunicacaoInternaVO.getAssunto(), configuracaoMobileVO.getIdRemetenteGoogle(), configuracaoMobileVO.getCertificadoAPNSApple(), configuracaoMobileVO.getSenhaCertificadoApns(), configuracaoMobileVO.getCertificadoDestribuicao());
		}
	    }
	} catch (Exception e) {
	    LOGGER.log(Level.SEVERE, e.toString(), e);
	}
    }
}
