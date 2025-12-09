package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface ContaCorrenteInterfaceFacade {

	public ContaCorrenteVO novo() throws Exception;

	public void incluir(ContaCorrenteVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void alterar(ContaCorrenteVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void excluir(ContaCorrenteVO obj, UsuarioVO usuario) throws Exception;

	public ContaCorrenteVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public String consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(String nrBanco, String numero, String digito, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaCorrenteVO> consultarPorNumero(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ContaCorrenteVO> consultarPorNumero(String valorConsulta, String situacao, TipoContaCorrenteEnum tipoContaCorrente, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception; 

	public List consultarPorDataAbertura(Date prmIni, Date prmFim, String situacao, TipoContaCorrenteEnum tipoContaCorrente, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNumeroAgenciaAgencia(String valorConsulta, String situacao, TipoContaCorrenteEnum tipoContaCorrente, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;	

	public List<ContaCorrenteVO> consultarPorContaCaixa(Boolean valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorContaCaixa(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ContaCorrenteVO> consultarContaCorrenteHabilitadoPix(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void alterarSaldoContaCorrente(Integer codigo, Double saldoFinal, UsuarioVO usuario) throws Exception;

	public void movimentarSaldoContaCorrente(Integer codigo, String tipoMovimentacao, Double valor, UsuarioVO usuario) throws Exception;

	public ContaCorrenteVO consultarPorNumeroAgenciaNumeroConta(String cedenteNumeroConta, String cedenteNumeroAgencia, Integer unidadeEnsino, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

	public Boolean consultarSeUsuarioTemContaCaixaVinculadoAEle(Integer codigoPessoa);

	public List<ContaCorrenteVO> consultarPorFuncionarioResponsavel(Integer codigoPessoa, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaCorrenteVO> consultarRapidaContaCorrentePorTipo(Boolean contaCaixa, Boolean renegociacao, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	public Double consultarSaldoContaCorrente(Integer codigoContaCorrente) throws Exception;

	public List<ContaCorrenteVO> consultarPorFuncionarioResponsavelDataAberturaFluxoCaixaSituacao(boolean contaCaixa, Integer codigoPessoa, Integer unidadeEnsino, Date dataAberturaFluxoCaixa, String situacaoFluxoCaixa, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaCorrenteVO> consultarPorContaCaixaDataAberturaFluxoCaixaSituacao(Boolean valorConsulta, Integer unidadeEnsino, Date dataAberturaFluxoCaixa, String situacaoFluxoCaixa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaCorrenteVO> consultarPorCodigoSomenteContasCorrente(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaCorrenteVO> consultarContaCorrenteCaixaPorNumero(String valorConsulta, Integer unidadeEnsino, boolean permissaoMovimentacaoContaCaixaContaCorrente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ContaCorrenteVO> consultarContaCorrenteCaixaPorNumeroPorUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List<ContaCorrenteVO> consultarContaCorrenteComboBox(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<ContaCorrenteVO> consultaRapidaPorBancoControleRemessaNivelComboBox(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public boolean consultarExistenciaContaGeradaPorContaCorrente(Integer contaCorrente, UsuarioVO usuarioVO);

	public boolean apreasentarMensagemAlteracaoContaCorrenteMudancaCarteira(ContaCorrenteVO contaCorrenteAlteradaVO, StringBuilder mensagemAviso, UsuarioVO usuarioVO) throws Exception;

	public ContaCorrenteVO consultarContaCorrentePadraoUnidadeEnsino(Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO);

	public ContaCorrenteVO consultarContaCorrentePadraoPorContaReceber(Integer contaReceber, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO);

	public List<ContaCorrenteVO> consultarContaCorrenteComboBoxPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void inativarConta(final ContaCorrenteVO obj, UsuarioVO usuario) throws Exception;

	public void inicializarDadosBancoContaCorrente(ContaCorrenteVO contaCorrenteVO, UsuarioVO usuarioVO) throws Exception;

	public Boolean consultarFuncionarioResponsavelPorCaixa(Integer pessoa, Integer contaCorrente, UsuarioVO usuarioVO);

	public ContaCorrenteVO consultarContaCorrentePadraoPorContaReceberAgrupada(Integer contaReceberAgrupada, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO);

	List<ContaCorrenteVO> consultarContaCorrente(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ContaCorrenteVO> consultarContaCaixa(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
	public List<ContaCorrenteVO> consultaRapidaPorBancoControleRemessaNivelComboBox(String nrBanco, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public Boolean consultarExistenciaContaGeradaPorContaCaixaExistente(Integer contaCorrenteCaixa, Integer unidadeEnsino) throws Exception;
	
	public List<ContaCorrenteVO> consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(String nrBanco , String numero, String digito, String convenio, Integer unidadeEnsino, boolean isSituacaoAtiva, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List<ContaCorrenteVO> consultarPorNomeBanco(String valorConsulta, String situacao, TipoContaCorrenteEnum tipoContaCorrente, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<ContaCorrenteVO> consultarPorNumeroAgenciaNumeroContaDigitoConta(String conta, String digitoConta, String agencia, Integer unidadeEnsino, String convenio, String cnab, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception ;	
	
	public List<ContaCorrenteVO> consultarPorCodigoFuncionarioResponsavel(Integer codigoPessoa, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<ContaCorrenteVO> consultarPorNossoNumero(List<ControleRemessaContaReceberVO> listaContaReceber, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ContaCorrenteVO> consultarContaCaixaUnidadesEnsino( List<UnidadeEnsinoVO> listaUnidadesEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	
	ContaCorrenteVO consultarContaCorrenteUsadaNaNegociacaoPagamentoPorContaPagar(Integer contaPagar, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	ContaCorrenteVO consultarContaCorrenteUsadaNaNegociacaoRecebimentoPorContaReceber(Integer contaReceber, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	List<ContaCorrenteVO> consultarRapidaContaCorrentePorTipoSituacao(Boolean contaCaixa, Boolean renegociacao, Integer unidadeEnsino, String situacao, UsuarioVO usuarioVO) throws Exception;	
	
	ContaCorrenteVO consultarPorCondicaoRenegociacaoEUnidadeEnsino(Integer condicaoRenegociacao, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	public List<ContaCorrenteVO> consultarRapidaContaCorrentePorTipo(Boolean contaCaixa, Boolean renegociacao, Boolean verificarUtilizarNegociacao, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
	
	public List<ContaCorrenteVO> consultarContaCorrenteCaixaPorNumeroPorUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsino, String ordenarPor, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public ContaCorrenteVO consultarContaCorrentePorTurma(Integer turma,  int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	ContaCorrenteVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;
	
	public List<ContaCorrenteVO> consultarPorNomeApresentacao(String valorConsulta, String situacao, TipoContaCorrenteEnum tipoContaCorrente, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public Double atualizarDadosSaldoContaCaixa(Integer contaCaixa, UsuarioVO usuario) throws Exception;
	
	List<ContaCorrenteVO> consultarPorContaCaixa(Boolean valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, boolean situacaoAtiva, UsuarioVO usuario) throws Exception;

	public void alterarChaveTransacaoRemessaOnlineContaCorrente(Integer codigo,
			String chaveTransacaoClienteRegistroRemessaOnline,
			Date dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline, UsuarioVO usuarioVO) throws Exception;

	public  ContaCorrenteVO consultarChaveTransacaoClienteRemessaOnlineAndDataExpiracaoPorChavePrimariaUnica(Integer codigoPrm,	 UsuarioVO usuario) throws Exception;

	Optional<ContaCorrenteVO> consultarPorCodigoFluxoCaixa(Integer fluxoCaixa, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

}
