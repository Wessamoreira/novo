/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.arquitetura;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.arquitetura.EmailVO;
import negocio.comuns.arquitetura.SMSVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ProgressBarVO;

/**
 *
 * @author PEDRO
 */
public interface EmailFacadeInterface {
    
    
    public void realizarGravacaoEmailPorSuspensaoMatricuala(ComunicacaoInternaVO obj, ConfiguracaoGeralSistemaVO confEmail, String emailDest, String nomeDest, Boolean anexarImagensPadrao) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>Cidade</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>CidadeVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    EmailVO consultarPorOrdemDeCadastro(boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CidadeVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>CidadeVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    void excluir(EmailVO obj) throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CidadeVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>CidadeVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    void incluir(final EmailVO obj) throws Exception;
    public void realizarGravacaoEmail(String emailDest, String nomeDest, String emailRemet, String nomeRemet, String assunto, String mensagem, List<File> listaAnexos) throws Exception;
    
    public void realizarGravacaoEmail(ComunicacaoInternaVO obj, List<File> listaAnexos, Boolean anexarImagensPadrao, UsuarioVO usuarioLogado, ProgressBarVO progressBarVO) throws Exception;
    
    public void realizarGravacaoEmail(TurmaVO turma, ComunicacaoInternaVO obj, List<File> listaAnexos, ConfiguracaoGeralSistemaVO config, Boolean anexarImagensPadrao, UsuarioVO usuarioLogado) throws Exception;
    public Boolean consultarQtdEmailMaiorQueConfiguracao(Integer qtdConfiguracao, UsuarioVO usuario) throws Exception;
    
    public List<EmailVO> consultarEmailsRedefinirSenha(ConfiguracaoGeralSistemaVO config, boolean redefinirSenha);
    public List<SMSVO> consultarSMSs(ConfiguracaoGeralSistemaVO config, boolean redefinirSenha);
    void realizarExclusaoEmail(EmailVO email) throws Exception;
    public Boolean consultarEmailRedefinirSenhaUsuario(PessoaVO pessoa) throws Exception;
	String capturarCaminhosAnexosEnvioEmail(ComunicacaoInternaVO comunicacaoInternaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO);
	List<EmailVO> consultarEmails(DataModelo dataModelo, String assunto,  Date dataEnvio, String destinatario, String emailDestinatario, String  remetente, String emailRemetente) throws Exception;
	Integer consultarTotalEmails(DataModelo dataModelo, String assunto,  Date dataEnvio, String destinatario, String emailDestinatario, String  remetente, String emailRemetente) throws Exception;
	public void excluirPorEmailDestinarioEassunto(String emailDestinatario, String assunto, UsuarioVO usuario);

	void executarReagendamentoTodosEmails(UsuarioVO usuarioLogado, String assunto,  Date dataEnvio, String destinatario, String emailDestinatario, String  remetente, String emailRemetente) throws Exception;

	void atualizarCampoErroEmail(Integer codigo, String mensagemErro) throws Exception;
	
	public void executarTodosEmails(String assunto,  Date dataEnvio, String destinatario, String emailDestinatario, String  remetente, String emailRemetente) throws Exception;
	
	void realizarGravacaoEmailMultiplosDestinatarios(ComunicacaoInternaVO obj, List<File> listaAnexos, Boolean anexarImagensPadrao, UsuarioVO usuarioLogado, ProgressBarVO progressBarVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
}
