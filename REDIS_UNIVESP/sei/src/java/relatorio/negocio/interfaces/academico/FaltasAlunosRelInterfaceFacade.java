/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.FaltasAlunosRelVO;

/**
 *
 * @author Rogerio
 */
public interface FaltasAlunosRelInterfaceFacade {
	public List<FaltasAlunosRelVO> consultaFaltasAlunosRelatorio(Integer unidadeEnsino, Integer turma, Integer curso, Integer disciplina, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

}
