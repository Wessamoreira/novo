package negocio.interfaces.biblioteca;

import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

public interface ItemEmprestimoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ItemEmprestimoVO</code>.
	 */
	public ItemEmprestimoVO novo() throws Exception;

	public Integer calcularNrExemplaresEmprestadosParaUmaCatalogo(ItemEmprestimoVO itemEmprestimoVO) throws Exception;

//	public String inicializarDadosNovoEmprestimoDosItensEmprestimo(EmprestimoVO emprestimoVO, List<ExemplarVO> listaExemplares,
//			ConfiguracaoBibliotecaVO configuracaoBibliotecaVO,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
	public void inicializarDadosNovoEmprestimoDosItensEmprestimo(EmprestimoVO emprestimoVO, List<ItemEmprestimoVO> listaItemEmprestimo,
			ConfiguracaoBibliotecaVO configuracaoBibliotecaVO,ConfiguracaoFinanceiroVO configuracaoFinanceiro, List<String> listaMensagem, UsuarioVO usuario) throws Exception;
	
//	public String inicializarDadosDevolucaoRenovacaoDosItensEmprestimo(EmprestimoVO emprestimoVO, List<ItemEmprestimoVO> listaItensEmprestimo,
//			ConfiguracaoBibliotecaVO confPadraoBib,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
        public void inicializarDadosDevolucaoRenovacaoDosItensEmprestimo(EmprestimoVO emprestimoParaRenovacao,
            List<ItemEmprestimoVO> listaItensEmprestimo, ConfiguracaoBibliotecaVO confPadraoBib, ConfiguracaoFinanceiroVO configuracaoFinanceiro, List<String> listaMensagem, UsuarioVO usuario) throws Exception;
	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ItemEmprestimoVO</code>.
	 * Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemEmprestimoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
        public void incluir(final ItemEmprestimoVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ItemEmprestimoVO</code>. Sempre
	 * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemEmprestimoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(ItemEmprestimoVO obj) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ItemEmprestimoVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemEmprestimoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(ItemEmprestimoVO obj) throws Exception;


 
	public void alterarSituacaoItensEmprestimoParaEmExecucao(List<ItemEmprestimoVO> itemEmprestimoVOs) throws Exception;

	/**
	 * Operação que, ao realizar uma <b>Devolução</b> altera a situação de todos os <code>ItemEmprestimoVO</code> para
	 * <b>DEVOLVIDO</b>.
	 * 
	 * @param itemEmprestimoVOs
	 * @param emprestimoVO
	 * @param devolucao
	 * @throws Exception
	 */
	public void alterarSituacaoItensEmprestimoParaDevolvido(ItemEmprestimoVO itemEmprestimoVO) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>ItemEmprestimoVO</code> no BD. Garantindo o relacionamento com
	 * a entidade principal <code>biblioteca.Emprestimo</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirItemEmprestimos(Integer emprestimoPrm, List objetos, UsuarioVO usuarioVO) throws Exception;

	

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

	public List<ItemEmprestimoVO> consultarItemEmprestimos(Integer codigo, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ItemEmprestimoVO> consultarItensEmprestadosPorCodigoPessoa(Integer codigoPessoa, Integer biblioteca, String tipoPessoa, int nivelMontarDados, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception;

	public void realizarCalculoDataPrevisaoDevolucaoExemplar(ItemEmprestimoVO itemEmprestimoVO, TipoPessoa tipoPessoa, BibliotecaVO bibliotecaVO, ConfiguracaoBibliotecaVO confPadraoBib, Boolean utilizarDataDevolucaoTemp, CidadeVO cidadeBibliotecaVO, UsuarioVO usuarioVO) throws Exception;

	public void realizarCalculoDataPrevisaoDevolucaoExemplarHora(ItemEmprestimoVO itemEmprestimoVO, TipoPessoa tipoPessoa, BibliotecaVO bibliotecaVO, ConfiguracaoBibliotecaVO confPadraoBib, Boolean utilizarDataDevolucaoTemp, CidadeVO cidadeBibliotecaVO, UsuarioVO usuarioVO) throws Exception;	
	/**
     * Consulta Carrega os dados minimos necessarios para a JobNotificacaoAtrasoEmprestimo
     * @param codigoEmprestimo - codigo do emprestimo
     * @return - lista da ItemEmprestimoVO
     * @throws Exception
     */
    public List<ItemEmprestimoVO> consultarItensEmprestimoPorCodigoEmprestimoJob(Integer codigoEmprestimo) throws Exception;

    void registarRenovacaoEmprestimo(String tipoPessoa, ItemEmprestimoVO itemEmprestimoVO, ConfiguracaoBibliotecaVO configuracaoBibliotecaVO, Boolean liberarValidacaoBloqueioBiblioteca, UsuarioVO usuario) throws Exception;

	List<ItemEmprestimoVO> consultarItemEmprestimoVisaoAlunoProfessor(String valorConsulta, boolean apenasEmprestimosEmAberto, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Boolean consultarPessoaPossuePendenciaBiblioteca(Integer pessoa, String matricula, Boolean somenteEmAtraso) throws Exception;
	
	public String consultarPorSituacaoExecucaoAtrasadoRenovadoEPorPessoa(Integer pessoa, UsuarioVO usuarioVO);
	
	public List<ItemEmprestimoVO> consultarItensEmprestadosOutraBibliotecaPorCodigoPessoa(List<ItemEmprestimoVO> itemEmprestimoVOs, Integer codigoPessoa, Integer biblioteca, int nivelMontarDados, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception;
	
	/**
	 * @author Wendel Rodrigues
	 * @version 5.0.3.1
	 * @since 20/03/2015
	 * Cria o vínculo com chave estrangeira de ContaReceber para ItemEmprestimo.
	 */
	public void executarVinculoDaContaReceberParaItemEmprestimo(List<ItemEmprestimoVO> listaItensEmprestimo, Integer contaReceber, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * @author Wendel Rodrigues
	 * @version 5.0.3.1
	 * @since 20/03/2015
	 * Caso exclua a ContaReceber será removido a chave estrangeira e concedido a insenção da multa do ItemEmprestimo.
	 * Caso cancele a ContaReceber o vínculo com ItemEmprestimo não é removido e será concedido a insenção da multa.
	 * Caso reativar ContaReceber que foi cancelada o vínculo com ItemEmprestimo não é removido e será cobrado a multa do ItemEmprestimo.
	 */
	public void executarIsencaoEOuRemoverVinculoDaContaReceberParaItemEmprestimo(Integer contaReceber, Boolean isentarCobrancaMulta, Boolean removerVinculoContaReceber, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 7 de dez de 2015 
	 * @param codigoPessoa
	 * @param biblioteca
	 * @param tipoPessoa
	 * @param nivelMontarDados
	 * @param configuracaoBiblioteca
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<ItemEmprestimoVO> consultarItensEmprestadosPorCodigoPessoaValidandoEmprestimosOutrasBibliotecas(Integer codigoPessoa, Integer biblioteca, String tipoPessoa, int nivelMontarDados, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception;
	
	List<ItemEmprestimoVO> consultarPorMatriculaFichaAluno(String matricula, String situacao, String mesAno, UsuarioVO usuarioVO);

	List<SelectItem> consultarMesAnoItemEmprestimoPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO);

}