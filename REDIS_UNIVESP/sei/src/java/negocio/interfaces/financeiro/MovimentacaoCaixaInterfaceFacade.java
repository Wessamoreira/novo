package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.MovimentacaoCaixaVO;

public interface MovimentacaoCaixaInterfaceFacade {

    public MovimentacaoCaixaVO novo() throws Exception;

    
    public void persistir(List<MovimentacaoCaixaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
    
    public void incluir(MovimentacaoCaixaVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(MovimentacaoCaixaVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(MovimentacaoCaixaVO obj, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoFluxoCaixa(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeFormaPagamento(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTipoOrigem(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<MovimentacaoCaixaVO> consultarPorCodigoTipoOrigem(Integer codigoOrigem, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTipoMovimentacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorResponsavel(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirMovimentacaoCaixas(Integer fluxoCaixa, UsuarioVO usuario) throws Exception;

    public void alterarMovimentacaoCaixas(Integer fluxoCaixa, List objetos, UsuarioVO usuario) throws Exception;

    public void incluirMovimentacaoCaixas(Integer fluxoCaixaPrm, List objetos, UsuarioVO usuario) throws Exception;

    public MovimentacaoCaixaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarMovimentacaoCaixas(Integer fluxoCaixa, int nivelMontarDados , UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String idEntidade);

	List<MovimentacaoCaixaVO> consultarChequeFluxoCaixaPelaMovimentacaoCaixa(Integer contaCaixa, Date dataBase) throws Exception;
	
	public void alterarPessoaMovimentacaoCaixa(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 17 de mar de 2016 
	 * @param formaPagamentoNegociacaoRecebimentoVO
	 * @param fluxoCaixaVO
	 * @param pessoa
	 * @param parceiro
	 * @param fornecedor
	 * @param responsavel
	 * @param codigoOrigem
	 * @param formaPagamento
	 * @param valor
	 * @param tipoOrigem
	 * @param tipoMovimentacao
	 * @return
	 * @throws Exception 
	 */
	MovimentacaoCaixaVO executarGeracaoMovimentacaoCaixa(ChequeVO chequeVO, FluxoCaixaVO fluxoCaixaVO, Integer pessoa, Integer parceiro, Integer fornecedor, Integer responsavel, Integer operadoraCartao, Integer banco, Integer codigoOrigem, Integer formaPagamento, Double valor, String tipoOrigem, String tipoMovimentacao) throws Exception;

	/** 
	 * @author Wellington - 18 de mar de 2016 
	 * @param fluxoCaixa
	 * @param nivelMontarDados
	 * @param verificarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<MovimentacaoCaixaVO> consultarPorFluxoCaixaFormaPagamentoDinheiroECheque(Integer fluxoCaixa, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
	
	public Integer excluirMovimentacaoCaixasPorAlteracaoMapaPendenciaCartaoCredito(Integer negociacaorecebimento, Integer formaPagamento, Integer operadoraCartao, UsuarioVO usuario) throws Exception;
}
