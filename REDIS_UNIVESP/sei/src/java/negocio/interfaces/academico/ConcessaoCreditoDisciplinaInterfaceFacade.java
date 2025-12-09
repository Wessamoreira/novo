/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConcessaoCreditoDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Otimize-04
 */
public interface ConcessaoCreditoDisciplinaInterfaceFacade {
    public void validarDados(ConcessaoCreditoDisciplinaVO obj) throws Exception;
    public void validarDadosAdicionarAproveitamento(ConcessaoCreditoDisciplinaVO obj) throws Exception;
    public void incluir(final ConcessaoCreditoDisciplinaVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(final ConcessaoCreditoDisciplinaVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(ConcessaoCreditoDisciplinaVO obj, UsuarioVO usuario) throws Exception;
    public List<ConcessaoCreditoDisciplinaVO> consultarPorDescricaoAproveitamentoDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void incluirConcessaoCreditoDisciplina(Integer aproveitamentoDisciplina, List objetos, UsuarioVO usuario) throws Exception;
    public void alterarConcessaoCreditoDisciplina(Integer aproveitamentoDisciplina, List objetos, UsuarioVO usuario) throws Exception;
    public List<ConcessaoCreditoDisciplinaVO> consultarConcessaoCreditoDisciplinaPorAproveitamento(Integer aproveitamentoDisciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void excluirPorAproveitamentoCreditoDisciplina(Integer aproveitamentoDisciplina, UsuarioVO usuario) throws Exception;
    public boolean consultaExistenciaCreditoConcedidoDisciplinaPreRequisito(String matricula, Integer disciplina, Integer gradeCurricular) throws Exception;

}
