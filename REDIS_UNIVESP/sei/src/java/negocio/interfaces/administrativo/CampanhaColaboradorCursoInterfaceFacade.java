/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.administrativo;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.CampanhaColaboradorCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;

/**
 *
 * @author PEDRO
 */
public interface CampanhaColaboradorCursoInterfaceFacade {

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>CampanhaColaboradorCursoVO</code>. Sempre utiliza a chave primária
     * da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação
     * <code>alterar</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>CampanhaColaboradorCursoVO</code> que será alterada no banco de
     * dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso
     * ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    void alterar(final CampanhaColaboradorCursoVO obj, boolean verificarAcesso) throws Exception;

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    void alterarCampanhaColaboradorCurso(Integer campanhaColaborador, List objetos, boolean verificarAcesso) throws Exception;

    /**
     * Responsável por realizar uma consulta de
     * <code>CampanhaColaboradorCurso</code> através do valor do atributo
     * <code>nome</code> da classe
     * <code>Pessoa</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @return List Contendo vários objetos da classe
     * <code>CampanhaColaboradorCursoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    CampanhaColaboradorCursoVO consultarCampanhaAndResponsavel(Integer codCurso, Integer codUnidadeEnsino, String situacao, TipoCampanhaEnum tipoCampanhaEnum, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>CampanhaColaboradorCursoVO</code> através de sua chave primária.
     *
     * @exception Exception Caso haja problemas de conexão ou localização do
     * objeto procurado.
     */
    CampanhaColaboradorCursoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de
     * <code>CampanhaColaboradorCurso</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
     * superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
     * List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o
     * usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>CampanhaColaboradorCursoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List consultarPorCodigoPessoa(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>CampanhaColaboradorCursoVO</code>. Sempre localiza o registro a ser
     * excluído através da chave primária da entidade. Primeiramente verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta
     * operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>CampanhaColaboradorCursoVO</code> que será removido no banco de
     * dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de
     * acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    void excluir(CampanhaColaboradorCursoVO obj) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da
     * <code>CampanhaColaboradorCursoVO</code> no BD. Faz uso da operação
     * <code>excluir</code> disponível na classe
     * <code>CampanhaColaboradorCurso</code>.
     *
     * @param <code>campanhaColaborador</code> campo chave para exclusão dos
     * objetos no BD.
     * @exception Exception Erro de conexão com o BD ou restrição de acesso a
     * esta operação.
     */
    void excluirCampanhaColaboradorCursos(Integer campanhaColaborador, List objetos) throws Exception;

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    void excluirCampanhaColaboradorCursos(Integer campanhaColaborador, List objetos, boolean verificarAcesso) throws Exception;

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    void incluir(final CampanhaColaboradorCursoVO obj, boolean verificarAcesso) throws Exception;

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    void incluirCampanhaColaboradorCursos(Integer campanhaColaboradorPrm, List objetos, boolean verificarAcesso) throws Exception;
        
    public List consultarCampanhaColaboradorCursos(Integer campanhaColaborador, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}
