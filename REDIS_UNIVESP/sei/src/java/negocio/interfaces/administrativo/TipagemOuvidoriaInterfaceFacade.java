package negocio.interfaces.administrativo;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.administrativo.TipagemOuvidoriaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TipagemOuvidoriaInterfaceFacade {

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>TipagemOuvidoriaVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TipagemOuvidoriaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TipagemOuvidoriaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param TipagemOuvidoriaVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(TipagemOuvidoriaVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>TipagemOuvidoriaVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public void validarDados(TipagemOuvidoriaVO obj) throws Exception;

	/**
	 * Rotina responsavel por executar as consultas disponiveis na Tela
	 * TitulacaoCursoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public List<TipagemOuvidoriaVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>TitulacaoCursoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public TipagemOuvidoriaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}