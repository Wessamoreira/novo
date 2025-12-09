/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.academico;

import negocio.comuns.academico.TitulacaoProfessorCursoPendenteVO;
import negocio.comuns.academico.TitulacaoProfessorCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Philippe
 */
public interface TitulacaoProfessorCursoPendenteInterfaceFacade {

    public void incluirTitulacaoProfessorCursoPendente(final TitulacaoProfessorCursoVO obj, final Integer codigoTurma) throws Exception;
    public void alterarTitulacaoProfessorCursoPendente(final TitulacaoProfessorCursoPendenteVO obj) throws Exception;
    public TitulacaoProfessorCursoPendenteVO consultarTitulacaoProfessorCursoPendentePorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
