/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.utilitarias.ConsistirException;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaFiltroRelVO;
import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 * 
 * @author Alessandro
 */
public interface AlunoNaoCursouDisciplinaRelInterfaceFacade {

    AlunoNaoCursouDisciplinaRelVO montarDados(SqlRowSet sqlRowSet);

    List<AlunoNaoCursouDisciplinaRelVO> consultarRelatorio(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;

    void validarDados(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) throws ConsistirException;

    String designIReportRelatorio(TipoRelatorioEnum tipoRelatorioEnum);

    String caminhoIReportRelatorio();

}
