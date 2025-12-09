package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * @author Wellington - 4 de jan de 2016
 */
public interface AlteracaoPlanoFinanceiroAlunoInterfaceFacade {

	/**
	 * @author Wellington - 4 de jan de 2016
	 * @param campoConsulta
	 * @param valorConsulta
	 * @param ano
	 * @param semestre
	 * @param turno
	 * @param situacaoMatricula
	 * @param unidadeEnsinoVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<MatriculaPeriodoVO> consultar(String campoConsulta, String valorConsulta, String ano, String semestre, String situacaoMatricula, UnidadeEnsinoVO unidadeEnsinoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
}
