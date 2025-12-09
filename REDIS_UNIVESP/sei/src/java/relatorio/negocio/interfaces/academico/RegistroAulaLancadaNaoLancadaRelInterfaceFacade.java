/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.RegistroAulaLancadaNaoLancadaRelVO;

/**
 *
 * @author Rogerio
 */
public interface RegistroAulaLancadaNaoLancadaRelInterfaceFacade {

	/**
	 * @author Wellington - 19 de out de 2015
	 * @param unidadeEnsino
	 * @param curso
	 * @param turma
	 * @param disciplina
	 * @param professor
	 * @param ano
	 * @param semestre
	 * @param dataInicio
	 * @param dataFim
	 * @param situacaoRegistroAula
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<RegistroAulaLancadaNaoLancadaRelVO> consultaRegistroAulaLancadaNaoLancadaRelatorio(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, String situacaoRegistroAula, String periodicidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}
