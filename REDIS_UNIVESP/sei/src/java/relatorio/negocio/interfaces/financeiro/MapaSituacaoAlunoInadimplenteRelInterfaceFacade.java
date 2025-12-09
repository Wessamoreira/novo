/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.interfaces.financeiro;

import java.util.List;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.academico.MapaSituacaoAlunoRelVO;

/**
 *
 * @author Carlos
 */
public interface MapaSituacaoAlunoInadimplenteRelInterfaceFacade {

    public void validarDados(MatriculaVO matricula, TurmaVO turma, CursoVO curso, Integer unidadeEnsino, String ano, String semestre) throws ConsistirException;

    public List<MapaSituacaoAlunoRelVO> executarGeracaoRelatorio(Integer unidadeEnsino, Integer curso, Integer turma, MatriculaVO matriculaVO, String ano, String semestre) throws Exception;
}
