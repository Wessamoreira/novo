/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.academico;

import java.util.List;
import relatorio.negocio.comuns.academico.UploadProfessorRelVO;

/**
 *
 * @author Philippe
 */
public interface UploadProfessorRelInterfaceFacade {

    public List<UploadProfessorRelVO> criarObjeto(Integer unidadeEnsino, Integer curso, Integer turma, Integer professor) throws Exception;

    public String designIReportRelatorio();

    public String caminhoBaseRelatorio();

}
