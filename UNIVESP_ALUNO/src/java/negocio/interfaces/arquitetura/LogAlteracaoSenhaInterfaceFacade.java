/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.arquitetura;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.LogAlteracaoSenhaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Kennedy Souza
 */
public interface LogAlteracaoSenhaInterfaceFacade {
    

    public void incluir(LogAlteracaoSenhaVO obj, UsuarioVO usuario) throws Exception;
    void consultarSenhaJaUtilizada(String valorConsulta, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;
 }
