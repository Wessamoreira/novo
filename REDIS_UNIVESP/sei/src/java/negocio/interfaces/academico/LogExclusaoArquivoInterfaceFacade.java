/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.academico;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.LogExclusaoArquivoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Carlos
 */
public interface LogExclusaoArquivoInterfaceFacade {
    public void incluir(final LogExclusaoArquivoVO obj) throws Exception;
    public void realizarRegistroLogExclusaoArquivo(ArquivoVO arquivoVO, String tipoVisao, UsuarioVO usuarioVO) throws Exception ;
    public void realizarRegistroLogExclusaoArquivoBackup(ArquivoVO arquivoVO, String tipoVisao, UsuarioVO usuarioVO) throws Exception ;
    
}
