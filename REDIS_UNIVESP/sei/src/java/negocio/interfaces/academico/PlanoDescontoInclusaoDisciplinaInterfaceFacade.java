/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.PlanoDescontoInclusaoDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Carlos
 */
public interface PlanoDescontoInclusaoDisciplinaInterfaceFacade {
    public void persistir(PlanoDescontoInclusaoDisciplinaVO obj, UsuarioVO usuario) throws Exception;
    public void incluir(final PlanoDescontoInclusaoDisciplinaVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(final PlanoDescontoInclusaoDisciplinaVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(PlanoDescontoInclusaoDisciplinaVO obj, UsuarioVO usuario) throws Exception;
    public List<PlanoDescontoInclusaoDisciplinaVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuarioLogado) throws Exception;
    public void realizarAtivacaoPlanoDescontoInclusaoDisciplina(PlanoDescontoInclusaoDisciplinaVO planoDescontoInclusaoDisciplinaVO, UsuarioVO usuario) throws Exception;
    public void realizarInativacaoPlanoDescontoInclusaoDisciplina(PlanoDescontoInclusaoDisciplinaVO planoDescontoInclusaoDisciplinaVO, UsuarioVO usuario) throws Exception;
    public List<PlanoDescontoInclusaoDisciplinaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public PlanoDescontoInclusaoDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<PlanoDescontoInclusaoDisciplinaVO> consultarPorSituacao(String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
