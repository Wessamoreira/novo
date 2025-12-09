package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.FrequenciaAlunoRelVO;

/**
 * @author Wellington - 28 de out de 2015
 *
 */
public interface FrequenciaAlunoRelInterfaceFacade {

	/**
	 * @author Wellington - 28 de out de 2015
	 * @param matriculaVO
	 * @param disciplinaVO
	 * @param ano
	 * @param semestre
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 * @throws Exception
	 */
	List<FrequenciaAlunoRelVO> executarCriacaoObjeto(MatriculaVO matriculaVO, DisciplinaVO disciplinaVO, String ano, String semestre, Date dataInicio, Date dataFim,
			boolean trazerAlunoTransferencia, UsuarioVO usuarioVO, CursoVO cursoVO, TurmaVO turmaVO, Integer quantidadeMinimaFaltas,
			Integer percentualMinimaFaltas, boolean considerarFaltaAulasNaoRegistradas) throws Exception;

	/**
	 * @author Wellington - 29 de out de 2015
	 * @param matriculaVO
	 * @param ano
	 * @param semestre
	 * @throws Exception
	 */
	void validarDados(UnidadeEnsinoVO unidadeEnsinoVO, MatriculaVO matriculaVO, String ano, String semestre, CursoVO cursoVO, TurmaVO turmaVO) throws Exception;

}
