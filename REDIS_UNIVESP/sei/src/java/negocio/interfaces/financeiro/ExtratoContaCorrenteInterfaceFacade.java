package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ExtratoContaCorrenteVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;


public interface ExtratoContaCorrenteInterfaceFacade {
    
    public void executarCriacaoExtratoContaCorrente(Double valor,    
    Date data,
    OrigemExtratoContaCorrenteEnum origemExtratoContaCorrente,
    TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira,
    Integer codigoOrigem,
    ChequeVO cheque,
    String nomeSacado,
    Integer codigoSacado,
    TipoSacadoExtratoContaCorrenteEnum tipoSacado,
    FormaPagamentoNegociacaoRecebimentoVO fpnrVO,
    FormaPagamentoVO formaPagamento,
    ContaCorrenteVO contaCorrente,
    UnidadeEnsinoVO unidadeEnsino,
    OperadoraCartaoVO operadoraCartaoVO,
    boolean desconsiderarConciliacaoBancaria, Double valorTaxaBancaria,
    Boolean bloqueioPorFechamentoMesLiberado,
    UsuarioVO responsavel) throws Exception;
    
    public void alterarPessoaExtratoContaCorrente(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception;
    
    public void excluirPorFormaPagamentoNegociacaoRecebimento(Integer codigo, OrigemExtratoContaCorrenteEnum origemExtratoContaCorrente, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	public List<ExtratoContaCorrenteVO> consultarExtratoContaCorrenteProcessamentoPorContaCorrentePorDataAndCodigoQueNaoEstaoNaLista(ConciliacaoContaCorrenteDiaVO obj, UsuarioVO usuarioVO) throws Exception;	

	public Double consultarSaldoAnterior(String numeroContaCorrente , String digitoContaCorrente, Date dataInicio, UsuarioVO usuario) throws Exception;
	
	public Double consultarSaldoPorConciliacaoContaCorrentePorDataMovimentacao(ConciliacaoContaCorrenteVO obj, Date data, UsuarioVO usuario) throws Exception;

	void atualizarConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaExtratoVO obj, UsuarioVO usuario) throws Exception;	

	void anularExtratoContaCorrentePorConciliacaoContaCorrente(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) throws Exception;

	List<ExtratoContaCorrenteVO> consultarExtratoContaCorrentePorListaCodigos(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoContaCorrenteDiaExtratoVO, UsuarioVO usuarioVO) throws Exception;
	
	List<ExtratoContaCorrenteVO> consultarExtratoContaCorrenteDesconsiderarConciliacaoBancaria(String listaCodigos, boolean desconsiderarconciliacaobancaria, UsuarioVO usuarioVO) throws Exception;

	List<ExtratoContaCorrenteVO> consultarExtratoContaCorrenteQueNaoExistemMaisPoremEstaNaConciliacao(String listaCodigos,  UsuarioVO usuarioVO) throws Exception;
	

	List<ExtratoContaCorrenteVO> consultarExtratoContaCorrentePorOrigemPorCodigoOrigem(TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira, OrigemExtratoContaCorrenteEnum origem, Integer codigoOrigem, UsuarioVO usuarioVO) throws Exception;

	ExtratoContaCorrenteVO consultarCodigoOrigemExtratoContaCorrente(Integer codigo, UsuarioVO usuario) throws Exception;

	public ExtratoContaCorrenteVO consultarExtratoContaCorrentePorValor(String contaCorrente, String digito, Date data, TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira, OrigemExtratoContaCorrenteEnum origemExtratoContaCorrenteEnum, TipoFormaPagamento tipoFormaPagamento, Double valor, String listaCodigoExtrato, UsuarioVO usuarioVO) throws Exception;
	
	public  List<ExtratoContaCorrenteVO> consultarExtratosContaCorrenteComValorMenores(String contaCorrente, String digito, Date data, TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira, OrigemExtratoContaCorrenteEnum origemExtratoContaCorrenteEnum, TipoFormaPagamento tipoFormaPagamento, Double valor, String listaCodigoExtrato, UsuarioVO usuarioVO) throws Exception;

	void validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(OrigemExtratoContaCorrenteEnum origem, Integer codigoOrigem, boolean desconsiderarConciliacaoBancaria, Integer cheque, boolean alterarSomenteUltimoRegistro, UsuarioVO usuario) throws Exception;

	void validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(ExtratoContaCorrenteVO extrato, UsuarioVO usuario) throws Exception;

	void anularConciliacaoContaCorrenteDiaExtratoPorConciliacaoExtratoDia(String listaCodigo, UsuarioVO usuario) throws Exception;
	
	public void anularExtratoContaCorrentePorConciliacaoContaCorrenteQueNaoFazemMaisParte(ConciliacaoContaCorrenteVO obj,  UsuarioVO usuario) throws Exception;
	
	public void anularExtratoContaCorrentePorConciliacaoContaCorrenteDiaQueNaoFazemMaisParte(ConciliacaoContaCorrenteDiaVO objDia ,  UsuarioVO usuario) throws Exception;
	
	void anularConciliacaoContaCorrenteDiaExtratoPorCodigoExtrato(String listaCodigo, UsuarioVO usuario) throws Exception;

	void incluir(ExtratoContaCorrenteVO obj, UsuarioVO usuarioVO) throws Exception;

	void alterar(ExtratoContaCorrenteVO obj, UsuarioVO usuarioVO)  throws Exception;

	void validarExclusaoExtratoContaCorrente(ExtratoContaCorrenteVO extratoContaCorrenteVO, UsuarioVO usuarioVO) throws ConsistirException;
}
