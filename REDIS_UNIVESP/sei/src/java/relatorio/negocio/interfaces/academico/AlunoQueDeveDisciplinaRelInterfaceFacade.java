/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.academico;

import negocio.comuns.utilitarias.ConsistirException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaFiltroRelVO;
import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaRelVO;

/**
 *
 * @author Alessandro
 */
public interface AlunoQueDeveDisciplinaRelInterfaceFacade {

    void consultarRelatorio(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) throws Exception;

    void montaDadosConsulta(SqlRowSet sqlRowSet, AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO);

    AlunoNaoCursouDisciplinaRelVO montarDados(SqlRowSet sqlRowSet);

    void validarDados(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) throws ConsistirException;

}
