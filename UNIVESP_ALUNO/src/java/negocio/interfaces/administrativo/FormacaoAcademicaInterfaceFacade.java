package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

public interface FormacaoAcademicaInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>FormacaoAcademicaVO</code>.
     */
    public FormacaoAcademicaVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FormacaoAcademicaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FormacaoAcademicaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(FormacaoAcademicaVO obj, UsuarioVO usuario) throws Exception;

    public void incluir(FormacaoAcademicaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    public void alterar(FormacaoAcademicaVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FormacaoAcademicaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FormacaoAcademicaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(FormacaoAcademicaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FormacaoAcademicaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FormacaoAcademicaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(FormacaoAcademicaVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>FormacaoAcademica</code> através do valor do atributo
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>FormacaoAcademicaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>FormacaoAcademica</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FormacaoAcademicaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>FormacaoAcademicaVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>FormacaoAcademica</code>.
     * @param <code>pessoa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirFormacaoAcademicas(Integer pessoa, List objetos, UsuarioVO usuario) throws Exception;

    public void excluirFormacaoAcademicas(Integer pessoa, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>FormacaoAcademicaVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirFormacaoAcademicas</code> e <code>incluirFormacaoAcademicas</code> disponíveis na classe <code>FormacaoAcademica</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarFormacaoAcademicas(Integer pessoa, List objetos, UsuarioVO usuario) throws Exception;

    public void alterarFormacaoAcademicas(Integer pessoa, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>FormacaoAcademicaVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>basico.Pessoa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirFormacaoAcademicas(Integer pessoaPrm, List objetos, UsuarioVO usuario) throws Exception;

    public void incluirFormacaoAcademicas(Integer pessoaPrm, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>FormacaoAcademicaVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FormacaoAcademicaVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public List consultarPorCodigoPessoaOrdemNovaAntiga(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void incluirFormacaoAcademicasProspects(Integer prospects, List objetos, UsuarioVO usuario) throws Exception;

    public void alterarFormacaoAcademicasProspects(Integer prospects, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    FormacaoAcademicaVO consultarFormacaoAcademicaoMaisAtual(Integer pessoa, UsuarioVO usuario) throws Exception;
    
    public List<FormacaoAcademicaVO> consultarFormacaoAcademicaoMaisAtual(Integer pessoa, UsuarioVO usuario, Integer limit) throws Exception;

    FormacaoAcademicaVO consultarPorPessoaEEscolaridade(Integer pessoa, NivelFormacaoAcademica nivelFormacaoAcademica, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoProspect(Integer codProspect, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	FormacaoAcademicaVO consultarPorCodigoPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Boolean validarRemoverFormacaoAcademica(Integer codigoPessoa, FormacaoAcademicaVO formacao) throws Exception;
	
	public void incluirFormacaoAcademicaMatricula(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception;
	
	FormacaoAcademicaVO consultarPorNomeCursoPessoa(String nomeCurso, Integer codigoPessoa,boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	FormacaoAcademicaVO consultarPorPessoaEEscolaridadeOrdenandoUltimaDataConclusao(Integer pessoa, NivelFormacaoAcademica nivelFormacaoAcademica, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public void validarVinculoFormacaoAcademica(Integer areaConhecimento, UsuarioVO usuario) throws Exception;

}
