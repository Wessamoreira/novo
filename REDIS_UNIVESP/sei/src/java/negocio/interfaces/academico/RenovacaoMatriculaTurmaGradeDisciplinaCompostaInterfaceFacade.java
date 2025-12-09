package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * @author Wellington - 2 de fev de 2016
 *
 */
public interface RenovacaoMatriculaTurmaGradeDisciplinaCompostaInterfaceFacade {

	/**
	 * @author Wellington - 2 de fev de 2016
	 * @param renovacaoMatriculaTurma
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO> consultarPorRenovacaoMatriculaTurma(Integer renovacaoMatriculaTurma, UsuarioVO usuario) throws Exception;
	/**
	 * @author Wellington - 2 de fev de 2016
	 */

	/**
	 * @author Wellington - 2 de fev de 2016
	 * @param obj
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	void incluir(RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington - 2 de fev de 2016
	 * @param renovacaoMatriculaTurmaVO
	 * @param gradeDisciplinaCompostaVOs
	 * @throws Exception
	 */
	void executarGeracaoRenovacaoMatriculaTurmaGradeDisciplinaComposta(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception;

	/**
	 * @author Wellington - 2 de fev de 2016
	 * @param renovacaoMatriculaTurmaVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistirPorRenovacaoMatriculaTurma(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
}
