/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.interfaces.academico;

import java.util.List;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.EnvelopeRelVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.protocolo.RequerimentoVO;

/**
 *
 * @author Carlos
 */

public interface EnvelopeRelInterfaceFacade {
    public void validarDadosConsultaAlunoUnidadeEnsino(Integer unidadeEnsino) throws Exception;
    public void validarDadosConsultaAluno(MatriculaVO matriculaVO) throws Exception;
    public List<EnvelopeRelVO> executarConsultaParametrizada(String matricula, Integer turma, Integer curso, String tipoRelatorio, Boolean apenasAlunoAtivo, UsuarioVO usuarioVO) throws Exception;
    public List<EnvelopeRelVO> montarDadosEnvelopeRequerimento(UnidadeEnsinoVO unidadeEnsinoVO, RequerimentoVO requerimentoVO) throws Exception;
}



