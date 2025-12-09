package negocio.facade.jdbc.ead;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.MonitoramentoAlunosEADInterfaceFacade;

/**
 * 
 * @author Victor Hugo de Paula Costa 15/04/2015
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class MonitoramentoAlunosEAD extends ControleAcesso implements MonitoramentoAlunosEADInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void realizarEnvioComunicadoInternoComEnvioEmail(ComunicacaoInternaVO comunicacaoInternaVO, List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		List<ComunicadoInternoDestinatarioVO> comunicadoInternoDestinatarioVOs = new ArrayList<ComunicadoInternoDestinatarioVO>();
		ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = null;
		for (CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO : calendarioAtividadeMatriculaVOs) {
			if (calendarioAtividadeMatriculaVO.getSelecionarAtividade()) {
				comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
				calendarioAtividadeMatriculaVO.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
				comunicadoInternoDestinatarioVO.setDestinatario(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno());
				comunicadoInternoDestinatarioVO.setEmail(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getEmail());
				comunicadoInternoDestinatarioVO.setNome(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getNome());
				comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
				comunicadoInternoDestinatarioVOs.add(comunicadoInternoDestinatarioVO);
			}
		}
		comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().addAll(comunicadoInternoDestinatarioVOs);
		validarDadosEnvioComunicadoInterno(comunicacaoInternaVO);
		comunicacaoInternaVO.setEnviarEmail(Boolean.TRUE);
		comunicacaoInternaVO.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		comunicacaoInternaVO.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
		comunicacaoInternaVO.setTipoMarketing(Boolean.FALSE);
		comunicacaoInternaVO.setTipoLeituraObrigatoria(Boolean.FALSE);
		comunicacaoInternaVO.setDigitarMensagem(Boolean.TRUE);
		if(usuarioVO.getIsApresentarVisaoCoordenador() || usuarioVO.getIsApresentarVisaoProfessor()) {
			comunicacaoInternaVO.setResponsavel(usuarioVO.getPessoa());			
		} else {
			comunicacaoInternaVO.setResponsavel(configuracaoGeralSistemaVO.getResponsavelPadraoComunicadoInterno());				
		}
		comunicacaoInternaVO.setTipoDestinatario("AL");
		comunicacaoInternaVO.setTipoRemetente("FU");
		comunicacaoInternaVO.getUnidadeEnsino().setCodigo(configuracaoGeralSistemaVO.getUnidadeEnsino());
		getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, usuarioVO, configuracaoGeralSistemaVO,null);
	}

	private void validarDadosEnvioComunicadoInterno(ComunicacaoInternaVO comunicacaoInternaVO) throws Exception {
		if (comunicacaoInternaVO.getAssunto().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_envioEmail_assunto"));
		}
		if (comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_envioEmail_semDestinatario"));
		}
	}
}
