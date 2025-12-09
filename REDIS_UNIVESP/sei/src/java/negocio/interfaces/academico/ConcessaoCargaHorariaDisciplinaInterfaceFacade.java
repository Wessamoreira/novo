/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConcessaoCargaHorariaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Otimize-04
 */
public interface ConcessaoCargaHorariaDisciplinaInterfaceFacade {
    public void validarDados(ConcessaoCargaHorariaDisciplinaVO obj) throws Exception;
    public void validarDadosAdicionarAproveitamento(ConcessaoCargaHorariaDisciplinaVO obj) throws Exception;
    public void incluir(final ConcessaoCargaHorariaDisciplinaVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(final ConcessaoCargaHorariaDisciplinaVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(ConcessaoCargaHorariaDisciplinaVO obj, UsuarioVO usuario) throws Exception;
    public List<ConcessaoCargaHorariaDisciplinaVO> consultarPorDescricaoAproveitamentoDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void incluirConcessaoCargaHorariaDisciplina(Integer aproveitamentoDisciplina, List objetos, UsuarioVO usuario) throws Exception;
    public void alterarConcessaoCargaHorariaDisciplina(Integer aproveitamentoDisciplina, List objetos, UsuarioVO usuario) throws Exception;
    public List<ConcessaoCargaHorariaDisciplinaVO> consultarConcessaoCargaHorariaDisciplinaPorAproveitamento(Integer aproveitamentoDisciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void excluirPorAproveitamentoCargaHorariaDisciplina(Integer aproveitamentoDisciplina, UsuarioVO usuario) throws Exception;

}
