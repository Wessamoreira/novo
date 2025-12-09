package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberRecebimentoVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

public interface ContaReceberRecebimentoInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContaReceberRecebimentoVO</code>.
     */
    public ContaReceberRecebimentoVO novo() throws Exception;

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaReceberRecebimentoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ContaReceberRecebimentoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(ContaReceberRecebimentoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaReceberRecebimentoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberRecebimentoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(ContaReceberRecebimentoVO obj, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContaReceberRecebimentoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ContaReceberRecebimentoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(ContaReceberRecebimentoVO obj, UsuarioVO usuario) throws Exception;

    public List consultarPorRecebimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberRecebimento</code> através do valor do atributo
     * <code>Integer FormaPagamentoNegociacaoRecebimento</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ContaReceberRecebimentoVO> consultarPorFormaPagamentoNegociacaoRecebimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoContaReceberCodigoNegociacaoTipoPagamentoPago(Integer valorConsulta, Integer negociacao, String tipoPagamento, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberRecebimento</code> através do valor do atributo
     * <code>codigo</code> da classe <code>ContaReceber</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ContaReceberRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoContaReceber(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Responsável por realizar uma consulta de <code>ContaReceberRecebimento</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ContaReceberRecebimentoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por excluir todos os objetos da <code>ContaReceberRecebimentoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ContaReceberRecebimento</code>.
     * @param <code>ContaReceber</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirContaReceberRecebimentos(Integer ContaReceber, UsuarioVO usuario) throws Exception;

    public void excluirContaReceberRecebimentos(Integer ContaReceber, List objeto, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por alterar todos os objetos da <code>ContaReceberRecebimentoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirContaReceberRecebimentos</code> e <code>incluirContaReceberRecebimentos</code> disponíveis na classe <code>ContaReceberRecebimento</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarContaReceberRecebimentos(Integer ContaReceber, Integer notaFiscalSaidaServico, List objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por incluir objetos da <code>ContaReceberRecebimentoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.ContaReceber</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirContaReceberRecebimentos(Integer ContaReceberPrm, Integer notaFiscalSaidaServico, List<ContaReceberRecebimentoVO> objetos, UsuarioVO usuario) throws Exception;

    /**
     * Operação responsável por localizar um objeto da classe <code>ContaReceberRecebimentoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ContaReceberRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade);

    public List<ContaReceberRecebimentoVO> consultarContaReceberPorRecebimentos(Integer contaReceber, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public ContaReceberRecebimentoVO consultarPorCodigoContaReceber(Integer codigoContaReceber) throws Exception;

	List<ContaReceberRecebimentoVO> consultarContasRecebidasNotaFiscalSaida(MatriculaVO matriculaVO, Date dataInicio, Date dataFim, CursoVO cursoVO, TurmaVO turmaVO, String tipoValorConsultaContasRecebidas, String tipoDataConsiderar, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String situacaoContaReceber, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	void alterarCodigoNotaFiscalSaidaServico(Integer notaFiscalSaida, Integer contaReceberRecebimento, UsuarioVO usuarioVO) throws Exception;
	
	public void realizarMontagemDadosNegociacaoRecebimentoDeRecebimentoTerceirizado(List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	Integer verificarNotaFiscalSaidaServicoEmitidaContaReceberRecebimento(Integer contaReceberRecebimento) throws Exception;

	/**
	 * Responsável por remover o vínculo de Nota Fiscal Saída com Conta Receber
	 * Recebimento.
	 * 
	 * @author Wellington Rodrigues - 13/05/2015
	 * @param notaFiscalSaida
	 * @param usuarioVO
	 * @throws Exception
	 */
	void removerVinculoNotaFiscalSaidaServicoContaReceberRecebimento(Integer notaFiscalSaida, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 23 de mai de 2016 
	 * @param codigoFormaPagamentoNegociacaoRecebimento
	 * @return
	 * @throws Exception 
	 */
	String consultarContaReceberRecebimentoTransacaoCartao(Integer codigoFormaPagamentoNegociacaoRecebimento) throws Exception;
	
	public List<ContaReceberRecebimentoVO> consultarPorContaReceberParaGeracaoLancamentoContabil(Integer contaRereber, boolean controlarAcesso, UsuarioVO usuario) throws Exception; 

	List<ContaReceberRecebimentoVO> consultarContasRecebidasComFormaPagamento(Integer contaReceber, UsuarioVO usuarioVO) throws Exception;
}