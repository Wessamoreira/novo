/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.academico;

import java.util.List;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.academico.AlunoDescontoDesempenhoRelVO;

/**
 *
 * @author Carlos
 */
public interface AlunoDescontoDesempenhoRelInterfaceFacade {

    public List realizarMontagemListaSelectItemCurso(Integer unidadeEnsino) throws Exception;
    public List realizarMontagemListaSelectItemTurma(Integer unidadeEnsino) throws Exception;
    public void validarDadosGeracaoRelatorio(String tipoRelatorio) throws ConsistirException;
    public String getCaminhoBaseRelatorio();
    public String getDesignIReportRelatorio();
    public List executarCriacaoObjetos(AlunoDescontoDesempenhoRelVO alunoDescontoDesempenhoRelVO, String tipoConsulta, Integer unidadeEnsino, Integer curso, Integer turma) throws Exception;
}
