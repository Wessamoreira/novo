/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.interfaces.crm;

import java.util.List;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.crm.ExtratoComissaoRelVO;

/**
 *
 * @author Otimize-04
 */
public interface ExtratoComissaoSinteticoRelInterfaceFacade {

    public List<ExtratoComissaoRelVO> criarObjeto(Integer unidadeEnsino, Integer consultorPessoa, Integer turma, String tipoRelatorio, String valorConsultaMes, String valorOrdenarPor, UsuarioVO usuarioVO) throws Exception;
    public List<TurmaVO> consultarTurma(String campoConsulta, String valorConsulta, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
    public void validarDados(Integer unidadeEnsino) throws Exception;
}
