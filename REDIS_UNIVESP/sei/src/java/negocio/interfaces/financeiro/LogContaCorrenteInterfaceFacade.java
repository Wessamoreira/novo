/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.LogContaCorrenteVO;

/**
 *
 * @author Philippe
 */
public interface LogContaCorrenteInterfaceFacade {

    public void incluir(final LogContaCorrenteVO obj, final UsuarioVO usuario) throws Exception;

}
