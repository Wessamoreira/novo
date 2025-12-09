/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.NumeroDocumentoContaReceberVO;

/**
 *
 * @author Otimize-04
 */
public interface NumeroDocumentoContaReceberInterfaceFacade {
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception;
    public NumeroDocumentoContaReceberVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public String gerarNumeroDocumentoTipoParceiro(Integer parceiro, UsuarioVO usuarioVO) throws Exception;
    public String gerarNumeroDocumentoTipoPessoa(Integer pessoa, UsuarioVO usuarioVO) throws Exception;


}
