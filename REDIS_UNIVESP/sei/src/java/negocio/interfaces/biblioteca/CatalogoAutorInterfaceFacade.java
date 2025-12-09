package negocio.interfaces.biblioteca;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.CatalogoAutorVO;
import negocio.comuns.biblioteca.CatalogoVO;

public interface CatalogoAutorInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>CatalogoAutorVO</code>.
     */
    public CatalogoAutorVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>CatalogoAutorVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>CatalogoAutorVO</code> que será gravado
     *            no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    public void incluir(final CatalogoAutorVO obj, UsuarioVO usuarioVO) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>CatalogoAutorVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>CatalogoAutorVO</code> que será
     *            alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    public void alterar(CatalogoAutorVO obj) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>CatalogoAutorVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>CatalogoAutorVO</code> que será
     *            removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(CatalogoAutorVO obj) throws Exception;

    public List consultarPorCatalogo(Integer catalogo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoAutor(Integer codigoAutor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>CatalogoAutorVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public CatalogoAutorVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public void alterarListaCatalogoAutorPorCodigoCatalogo(CatalogoVO catalogo, List<CatalogoAutorVO> objetos) throws Exception;

    public List<CatalogoAutorVO> consultaRapidaNivelComboBoxPorCodigoCatalogo(Integer catalogo, UsuarioVO usuario);

    public void excluirCatalogoAutorCatalogos(Integer catalogo) throws Exception;
    
    public CatalogoAutorVO consultarPorCodigoAutorRegistroUnico(Integer codigoAutor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
