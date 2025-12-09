package negocio.interfaces.financeiro;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface MovimentacaoFinanceiraInterfaceFacade {
	

    public MovimentacaoFinanceiraVO novo() throws Exception;
    public void incluir(MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception;
    public MovimentacaoFinanceiraVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception;           
    public List consultarPorNumeroContaCorrente(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception;
    public List consultarPorNomeUsuario(String valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception ;
    public void setIdEntidade(String aIdEntidade);
    public List consultarPorNumeroContaCorrenteDestino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception;
    public String consultarDescricaoPorCodigo(Integer codigoPrm) throws Exception;
    public List<MovimentacaoFinanceiraVO> consultarMapaPendenciaPorDataEmissaoResponsavelContaCorrenteSituacao(String situacao, Date dataEmissaoInicial, Date dataEmissaoFinal, String numeroCheque, String nomeSacadoCheque, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void alterarSituacao(final MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, final String situacao, UsuarioVO usuario) throws Exception;
    public void alterarSituacaoMotivoRecusa(final Integer codigo, final String situacao, final String motivoRecusa, UsuarioVO usuario) throws Exception;
    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception;
    void estornar(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, boolean possuiPermissaoMovimentarCaixaFechado, UsuarioVO usuario) throws Exception;
    public void estornarContabilidadeConciliacao(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, UsuarioVO usuario) throws Exception;
    public String imprimirTextoPadrao(MovimentacaoFinanceiraVO mov, ConfiguracaoFinanceiroVO configFinanceiro, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioLogado) throws Exception;
	void persistir(MovimentacaoFinanceiraVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	public boolean validarMovimentacaoFinanceiraExistente(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	void persistirLancamentoContabilVO(MovimentacaoFinanceiraVO mov, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	void addLancamentoContabilVO(MovimentacaoFinanceiraVO mov, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception;
	void removeLancamentoContabilVO(MovimentacaoFinanceiraVO mov, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception;
	void preencherListaLancamentoContabilVO(List<LancamentoContabilVO> lista, LancamentoContabilVO lancamento) throws Exception;
	
	Integer consultarTotalRegistrosPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	Integer consultarTotalRegistrosPorNomeUsuario(String valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	Integer consultarTotalRegistrosPorNumeroContaCorrente(String valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	Integer consultarTotalRegistrosPorNumeroContaCorrenteDestino(String valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	Integer consultarTotalRegistrosPorSituacao(String valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	Integer consultarTotalRegistrosPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public void persistir(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, UsuarioVO usuarioLogado, boolean permitiRealizarMovimentacaoCaixaFechado) throws Exception;  
	
	MovimentacaoFinanceiraVO consultarPorCodigoCheque(Integer cheque, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<MovimentacaoFinanceiraVO> consultarPorNumeroNomeSacadoCheque(String numeroCheque, String nomeSacadoCheque, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	Integer consultarTotalRegistrosPorNumeroNomeSacadoCheque(String numeroCheque, String nomeSacadoCheque, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	FluxoCaixaVO atualizarFluxoCaixa(ContaCorrenteVO contaCorrenteVO, UsuarioVO usuario) throws Exception;
}