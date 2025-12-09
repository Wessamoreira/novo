/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.arquitetura;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.arquitetura.SMSVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author PEDRO
 */
public interface SMSFacadeInterface {
    

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CidadeVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CidadeVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    void incluir(final SMSVO obj) throws Exception;
    
    public void realizarGravacaoSMS(ComunicacaoInternaVO obj, UsuarioVO usuarioLogado) throws Exception;
    
    public List<SMSVO> consultarSms(String nome, String numero) throws Exception;
 }
