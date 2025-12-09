package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TipoDocumentoEquivalenteVO;

public interface TipoDocumentoEquivalenteInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>TipoDocumentoEquivalenteVO</code>.
     */
    public TipoDocumentoEquivalenteVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>TipoDocumentoEquivalenteVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>TipoDocumentoEquivalenteVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(TipoDocumentoEquivalenteVO obj) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>TipoDocumentoEquivalenteVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>TipoDocumentoEquivalenteVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(TipoDocumentoEquivalenteVO obj) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>TipoDocumentoEquivalenteVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>TipoDocumentoEquivalenteVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(TipoDocumentoEquivalenteVO obj) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>tipoDocumentoTipoDocumentoEquivalente</code> através do valor do atributo
     * <code>nome</code> da classe <code>TipoDocumentoEquivalente</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>TipoDocumentoEquivalenteVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeTipoDocumentoEquivalente(String valorConsulta, boolean controleAcesso) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>TipoDocumentoEquivalenteVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>tipoDocumentoTipoDocumentoEquivalente</code>.
     * @param <code>tipoDocumento</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirTipoDocumentoEquivalentes(Integer tipoDocumento) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>TipoDocumentoEquivalenteVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirtipoDocumentoTipoDocumentoEquivalentes</code> e <code>incluirtipoDocumentoTipoDocumentoEquivalentes</code> disponíveis na classe <code>tipoDocumentoTipoDocumentoEquivalente</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarTipoDocumentoEquivalentes(Integer tipoDocumento, List objetos) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>TipoDocumentoEquivalenteVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>academico.tipoDocumento</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirTipoDocumentoEquivalentes(Integer tipoDocumentoPrm, List objetos) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>TipoDocumentoEquivalenteVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public TipoDocumentoEquivalenteVO consultarPorChavePrimaria(Integer tipoDocumentoPrm, Integer TipoDocumentoEquivalentePrm, boolean controleAcesso) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public List consultarTipoDocumentoEquivalentes(Integer tipoDocumento, boolean controleAcesso) throws Exception;

}
