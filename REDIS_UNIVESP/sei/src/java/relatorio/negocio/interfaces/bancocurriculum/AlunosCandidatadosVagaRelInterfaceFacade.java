/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.bancocurriculum;

import java.util.List;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.basico.PessoaVO;
import relatorio.negocio.comuns.bancocurriculum.AlunosCandidatadosVagaRelVO;

/**
 *
 * @author Philippe
 */
public interface AlunosCandidatadosVagaRelInterfaceFacade {
    public List<AlunosCandidatadosVagaRelVO> criarObjeto(PessoaVO pessoa, CursoVO curso, TurmaVO turma, String situacaoVaga) throws Exception;

    public String caminhoBaseRelatorio();

    public String designIReportRelatorio();

    public String designIReportRelatorioExcel();
}
