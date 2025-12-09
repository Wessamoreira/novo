package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.TurmaAberturaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TurmaAberturaInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>TurmaAberturaVO</code>.
     */
    public TurmaAberturaVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>TurmaAberturaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>TurmaAberturaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(TurmaAberturaVO obj) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>TurmaAberturaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>TurmaAberturaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(TurmaAberturaVO obj) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>TurmaAberturaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>TurmaAberturaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(TurmaAberturaVO obj) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>TurmaAbertura</code> através do valor do atributo
     * <code>Integer gradeDisciplina</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TurmaAberturaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>TurmaAbertura</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TurmaAberturaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirTurmaAberturas(Integer turma) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>TurmaAberturaVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirTurmaAberturas</code> e <code>incluirTurmaAberturas</code> disponíveis na classe <code>TurmaAbertura</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarTurmaAberturas(TurmaVO turma, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>TurmaAberturaVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>academico.Turma</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirTurmaAberturas(TurmaVO turma, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>TurmaAberturaVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public TurmaAberturaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    //public void executarBuscaPorDisciplinasComunsEntreTurmasAgrupadas(TurmaVO TurmaAbertura, UsuarioVO usuario) throws Exception;
    public void carregarDadosTurmaAbertura(TurmaVO turmaVO, UsuarioVO usuario) throws Exception;

    public void carregarDados(TurmaVO obj, UsuarioVO usuario) throws Exception;

    public TurmaAberturaVO inicializarDadosTurmaAbertura(Integer turma, UsuarioVO usuario);

    public List<Integer> consultarPorSituacaoDataJob(String situacao, Date data) throws Exception;

    public void alterarSituacao(Integer codigo, String situacao) throws Exception;

	TurmaAberturaVO consultarUltimaTurmaAberturaPorTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void alterarDataAberturaTurmaBaseAgrupada(List<TurmaAgrupadaVO> listaTurmaAgrupada, TurmaAberturaVO turmaAberturaVO, UsuarioVO usuario) throws Exception;

	public void carregarDadosTurmaAberturaTurmaBase(TurmaVO turmaVO, UsuarioVO usuarioLogado) throws Exception;
}
