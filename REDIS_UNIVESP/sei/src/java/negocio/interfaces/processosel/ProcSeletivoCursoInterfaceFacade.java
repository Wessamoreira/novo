package negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoEixoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;

public interface ProcSeletivoCursoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ProcSeletivoCursoVO</code>.
	 */
	public ProcSeletivoCursoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcSeletivoCursoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>ProcSeletivoCursoVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(ProcSeletivoCursoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcSeletivoCursoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>ProcSeletivoCursoVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(ProcSeletivoCursoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ProcSeletivoCursoVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>ProcSeletivoCursoVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ProcSeletivoCursoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ProcSeletivoCurso</code> através do valor do atributo 
	 * <code>nome</code> da classe <code>Curso</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ProcSeletivoCursoVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ProcSeletivoCurso</code> através do valor do atributo 
	 * <code>descricao</code> da classe <code>ProcSeletivo</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ProcSeletivoCursoVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricaoProcSeletivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>ProcSeletivoCursoVO</code> no BD.
	 * Faz uso da operação <code>excluir</code> disponível na classe <code>ProcSeletivoCurso</code>.
	 * @param <code>procSeletivo</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirProcSeletivoCursos(Integer procSeletivoUnidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>ProcSeletivoCursoVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirProcSeletivoCursos</code> e <code>incluirProcSeletivoCursos</code> disponíveis na classe <code>ProcSeletivoCurso</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarProcSeletivoCursos(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * Operação responsável por incluir objetos da <code>ProcSeletivoCursoVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>processosel.ProcSeletivo</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirProcSeletivoCursos(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;
	
	

	/**
	 * Operação responsável por localizar um objeto da classe <code>ProcSeletivoCursoVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ProcSeletivoCursoVO consultarPorChavePrimaria(Integer cursoPrm, Integer procSeletivoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);

        public List consultarPorCodigoProcSeletivo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        public List consultarPorCodigoProcSeletivoUnidadeEnsinoOpcaoInscicao(Integer processoSele,Integer unidadeEnsino, Date data ,int nivelMontarDados , UsuarioVO usuario) throws Exception ;
        
        public List consultarPorCodigoProcSeletivoUnidadeEnsinoInscicao(String valorConsultaCurso, Integer processoSele, Integer unidadeEnsino) throws Exception;

		public ProcSeletivoCursoVO consultarPorProcSeletivoUnidadeEnsinoCurso(Integer cursoPrm, Integer procSeletivoPrm, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		public List<ProcSeletivoCursoVO> consultarPorCodigoProcSeletivoUnidadeEnsino(Integer codigoProcessoSeletivo, Integer unidadeEnsino,
				UsuarioVO usuario) throws Exception;

		ProcSeletivoCursoVO inicializarDadosImportarCandidatoInscricaoProcSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, UsuarioVO usuario) throws Exception;

		List consultarPorCodigoProcSeletivoUnidadeEnsinoInscricao(String valorConsultaCurso, Integer processoSele,
				Integer unidadeEnsino) throws Exception;

}