package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaDisciplinaCompostaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * @author Wellington - 19 de jan de 2016
 *
 */
public interface TurmaDisciplinaCompostaInterfaceFacade {

	/**
	 * @author Wellington - 19 de jan de 2016
	 * @param turmaDisciplinaVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistirTurmaDisciplinaVOs(TurmaDisciplinaVO turmaDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington - 19 de jan de 2016
	 * @param turmaDisciplinaComposta
	 * @param nivelMontarDados
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	TurmaDisciplinaCompostaVO consultarPorChavePrimaria(Integer turmaDisciplinaComposta, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington - 19 de jan de 2016
	 * @param turmaDisciplina
	 * @param nivelMontarDados
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<TurmaDisciplinaCompostaVO> consultarPorTurmaDisciplina(Integer turmaDisciplina, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 25 de jan de 2016 
	 * @param turma
	 * @param nivelMontarDados
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<TurmaDisciplinaCompostaVO> consultarPorTurma(Integer turma, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	TurmaDisciplinaCompostaVO consultarPorTurmaGradeDisciplinaComposta(Integer turma, Integer gradeDisciplinaComposta,
			int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
}
