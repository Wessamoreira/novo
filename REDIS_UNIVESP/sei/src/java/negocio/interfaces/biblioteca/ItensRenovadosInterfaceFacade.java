package negocio.interfaces.biblioteca;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.biblioteca.ItensRenovadosVO;

public interface ItensRenovadosInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ItensRenovadosVO</code>.
	 */
	public ItensRenovadosVO novo() throws Exception;

	/**
	 * Calcula o número de ocorrências de um determinado Exemplar para uma determinada Pessoa no banco de dados.
	 * 
	 * @param emprestimoVO
	 * @param itemEmprestimoVO
	 * @return
	 * @throws Exception
	 */
	public Integer calcularNrRenovacoesExemplar(EmprestimoVO emprestimoVO, ItemEmprestimoVO itemEmprestimoVO) throws Exception;

	/**
	 * Monta o Objeto de ItensRenovados para gravar no Banco de dados. Se for um emprestimo novo, seta o nrRenovacao
	 * para 0. Se for uma renovação, o método consulta no banco a existência de algum registro de um detemrinado
	 * exemplar para uma determinada pessoa, calcula o numero de ocorrências e seta o nrRenovacao de acordo com essa
	 * consulta.
	 * 
	 * @param emprestimoVO
	 * @param renovacao
	 * @throws Exception
	 */
	public void montarItensRenovacao(EmprestimoVO emprestimoVO, Boolean renovacao) throws Exception;

	public void atualizarNrRenovacoes(ItensRenovadosVO itensRenovadosVO) throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ItensRenovadosVO</code>.
	 * Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItensRenovadosVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(ItensRenovadosVO obj) throws Exception;

	public void excluirItensRenovados(EmprestimoVO emprestimoVO, ItemEmprestimoVO itemEmprestimoVO) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ItensRenovadosVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItensRenovadosVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ItensRenovadosVO obj) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe <code>ItensRenovadosVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ItensRenovadosVO consultarPorChavePrimaria(Integer exemplarPrm, Integer pessoaPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	/**
	 * @author Rodrigo Wind - 02/03/2016
	 * @param exemplar
	 * @throws Exception
	 */
	void excluirPorExemplar(Integer exemplar, UsuarioVO usuarioVO) throws Exception;

}