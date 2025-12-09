package negocio.interfaces.processosel;

import java.util.List;
import java.util.Map;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoEixoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;

public interface ProcSeletivoUnidadeEnsinoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
	 */
	public ProcSeletivoUnidadeEnsinoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(ProcSeletivoUnidadeEnsinoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(ProcSeletivoUnidadeEnsinoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ProcSeletivoUnidadeEnsinoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ProcSeletivoUnidadeEnsino</code> através do valor do atributo 
	 * <code>nome</code> da classe <code>UnidadeEnsino</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ProcSeletivoUnidadeEnsinoVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 *
	 * @param codigo UnidadeEnsino.codigo
	 * @param codigo0 processoSeletivo.codigo
	 * @param nivelMontarDados
	 * @return
	 * @throws java.lang.Exception
	 */
	public ProcSeletivoUnidadeEnsinoVO consultarPorCodigoUnidadeEnsino(Integer codigo, Integer codigo0, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir todos os objetos da <code>ProcSeletivoUnidadeEnsinoVO</code> no BD.
	 * Faz uso da operação <code>excluir</code> disponível na classe <code>ProcSeletivoUnidadeEnsino</code>.
	 * @param <code>procSeletivo</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void excluirProcSeletivoUnidadeEnsinos(Integer procSeletivo, UsuarioVO usuarioVO) throws Exception;

	//    public void excluirProcSeletivoUnidadeEnsinosCurso(Integer procSeletivo, List objetos) throws Exception {
	//        Iterator i = objetos.iterator();
	//        while (i.hasNext()) {
	//            ProcSeletivoUnidadeEnsinoVO obj = (ProcSeletivoUnidadeEnsinoVO) i.next();
	//            obj.setProcSeletivo(procSeletivo);
	//            if (!obj.getCodigo().equals(0)) {
	//                excluir(obj);
	//            }
	//        }
	//    }
	/**
	 * Operação responsável por alterar todos os objetos da <code>ProcSeletivoUnidadeEnsinoVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirProcSeletivoUnidadeEnsinos</code> e <code>incluirProcSeletivoUnidadeEnsinos</code> disponíveis na classe <code>ProcSeletivoUnidadeEnsino</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarProcSeletivoUnidadeEnsinos(ProcSeletivoVO procSeletivoVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>ProcSeletivoUnidadeEnsinoVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>processosel.ProcSeletivo</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirProcSeletivoUnidadeEnsinos(ProcSeletivoVO procSeletivoVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>ProcSeletivoUnidadeEnsinoVO</code>
	 * através de sua chave primária. 
	 * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ProcSeletivoUnidadeEnsinoVO consultarPorChavePrimaria(Integer unidadeEnsinoPrm, Integer procSeletivoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe.
	 * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
	 * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
	 */
	public void setIdEntidade(String idEntidade);

	ProcSeletivoUnidadeEnsinoVO consultarPorProcSeletivoUnidadeEnsino(Integer procSeletivo, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	ProcSeletivoUnidadeEnsinoVO inicializarDadosImportarCandidatoInscricaoProcSeletivo(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, ProcSeletivoVO procSeletivoVO, Map<String, ProcSeletivoUnidadeEnsinoVO> mapProcSeletivoUnidadeEnsinoVOs, UsuarioVO usuario) throws Exception;

	void adicionarProcSeletivoUnidadeEnsinoEixoCursoVO(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO,
			ProcSeletivoUnidadeEnsinoEixoCursoVO procSeletivoUnidadeEnsinoEixoCursoVO) throws Exception;


	void removerProcSeletivoUnidadeEnsinoEixoCursoVO(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, ProcSeletivoUnidadeEnsinoEixoCursoVO procSeletivoUnidadeEnsinoEixoCursoVO) throws Exception;

	void realizarProcessamentoPlanilha(FileUploadEvent uploadEvent, ProcSeletivoVO procSeletivoVO, UsuarioVO usuario)
			throws Exception;

}