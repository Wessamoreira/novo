/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.MotivoInsucessoVO;

/**
 *
 * @author Philippe
 */
public interface MotivoInsucessoInterfaceFacade {
    public void incluir(final MotivoInsucessoVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(final MotivoInsucessoVO obj, UsuarioVO usuario) throws Exception;
    public List consultar(String campoConsulta, String valorConsulta, UsuarioVO usuario) throws Exception;
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void excluir(MotivoInsucessoVO obj, UsuarioVO usuario) throws Exception;
}
