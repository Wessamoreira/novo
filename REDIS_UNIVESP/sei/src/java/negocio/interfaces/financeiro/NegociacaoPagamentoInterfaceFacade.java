package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarAdiantamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface NegociacaoPagamentoInterfaceFacade {

    public NegociacaoPagamentoVO novo() throws Exception;

    public void incluir(NegociacaoPagamentoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    public void incluir(NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;
    
    public void alterar(final NegociacaoPagamentoVO obj, Boolean verificarAcesso, UsuarioVO usuario) throws Exception;    

    public void excluir(NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;

    public NegociacaoPagamentoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    //public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados) throws Exception;

    public List consultarPorNumeroContaCorrente(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeFornecedor(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeBanco(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeFuncionario(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public Integer consultarCodigoUnidadeEnsinoPelaNegociacaoPagamento(Integer codigoNegociacaoPagamento) throws Exception;

    public List<NegociacaoPagamentoVO> consultaRapidaPorNomeAluno(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<NegociacaoPagamentoVO> consultaRapidaPorNomeBanco(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<NegociacaoPagamentoVO> consultaRapidaPorNomeFuncionario(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<NegociacaoPagamentoVO> consultaRapidaPorNomeFornecedor(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Integer consultarTotalPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Integer consultaRapidaTotalRegistrosPorNomeAluno(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Integer consultarPorNumeroContaCorrenteTotalRegistros(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Integer consultaRapidaPorNomeFornecedorTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Integer consultaRapidaPorNomeBancoTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Integer consultaRapidaPorNomeFuncionarioTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    Integer consultaRapidaPorNomeParceiroTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<NegociacaoPagamentoVO> consultaRapidaPorNomeParceiro(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<NegociacaoPagamentoVO> consultaRapidaPorNomeResponsavelFinanceiro(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    Integer consultaRapidaPorNomeResponsavelFinanceiroTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<NegociacaoPagamentoVO> consultaRapidaPorCodigoContaPagar(Integer valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Integer consultaRapidaPorCodigoContaPagarTotalRegistros(Integer valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Date consultarDataPagamentoNegociacaoPagamentoPorContaPagar(Integer contaPagar, UsuarioVO usuario) throws Exception;
    
    public boolean validarNegociacaoPagamentoExistente(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public NegociacaoPagamentoVO consultarPorCodigoContaPagar(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<NegociacaoPagamentoVO> consultaRapidaPorNomeSacado(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Integer consultaRapidaPorNomeSacadoTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<NegociacaoPagamentoVO> consultaRapidaPorNomeOperadoraCartao(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Integer consultaRapidaPorNomeOperadoraCartaoTotalRegistros(String valorConsulta, String campoConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void realizarDistribuicaoAutomaticaAdiantamentosDisponiveisParaAbaterContasPagar(NegociacaoPagamentoVO negociacaoPagamento) throws Exception;
	
	public void limparDistribuicaoAdiantamentosDisponiveisParaAbaterContasPagar(NegociacaoPagamentoVO negociacaoPagamento, Boolean manterListaAdiantamentosParaNovoProcessamento) throws Exception;
	
	public void removerAdiantamentosDisponiveisParaAbaterContasPagar(NegociacaoPagamentoVO negociacaoPagamento, ContaPagarAdiantamentoVO cpa,UsuarioVO usuario);
	
	public void atualizarEEstornarAdiantamentosUtilizadosComoDescontoNegociacaoPagamento(NegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception;
	
	public Boolean consultarSeExisteNegociacaoPagamentoPorContaPagar(Integer contaPagar, UsuarioVO usuario) throws Exception;
	
	NegociacaoPagamentoVO gerarNegociacaoContaPagarPorContaPagarPorFormaPagamentoNegociacaoPagamentoVO(ContaPagarVO contaPagar, FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO, Date dataPagamento, boolean isControlaAcesso, UsuarioVO usuario) throws Exception;

	
	
}
