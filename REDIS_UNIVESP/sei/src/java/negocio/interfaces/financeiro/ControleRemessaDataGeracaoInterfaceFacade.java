/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ControleRemessaDataGeracaoVO;

/**
 *
 * @author Philippe
 */
public interface ControleRemessaDataGeracaoInterfaceFacade {

    public void incluir(final ControleRemessaDataGeracaoVO obj, UsuarioVO usuario) throws Exception;

	public void excluirPorNossoNumero(String nossoNumero, String tipoRemessa, UsuarioVO usuario) throws Exception;

}
