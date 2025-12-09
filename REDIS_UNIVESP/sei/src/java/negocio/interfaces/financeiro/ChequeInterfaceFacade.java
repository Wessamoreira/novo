package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ChequeInterfaceFacade {

    public ChequeVO novo() throws Exception;

    public void incluir(ChequeVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(ChequeVO obj) throws Exception;

    public void excluir(ChequeVO obj) throws Exception;

    public ChequeVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoRecebimento(Integer rtRLog, Integer localizacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoSituacao(Integer valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoSituacao(Integer valorConsulta, String situacao, List listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomePessoa(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorNomePessoaSituacao(String valorConsulta, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorNomePessoaSituacao(String valorConsulta, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoPessoa(Integer valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorSacado(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorSacadoSituacao(String valorConsulta, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorSacadoSituacao(String valorConsulta, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeBanco(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorNomeBancoSituacao(String valorConsulta, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorNomeBancoSituacao(String valorConsulta, List<String> situacoes, List<ContaCorrenteVO> lista, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNumeroAgenciaAgencia(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorNumeroAgenciaAgenciaSituacao(String valorConsulta, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNumero(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorNumeroSituacao(String valorConsulta, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ChequeVO consultarPorNumeroUnico(String valorConsulta, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataEmissao(Date prmIni, Date prmFim, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorDataEmissaoSituacao(Date prmIni, Date prmFim, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataPrevisao(Date prmIni, Date prmFim, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorDataPrevisaoSituacao(Date prmIni, Date prmFim, List<String> situacoes, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoPagamento(Integer ptRLog, Integer localizacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void desvincularChequePagamento(Integer codigo, String situacaoCheque, Integer localizacao) throws Exception;

    public void desvincularChequeRecebimento(Integer codigo, String situacaoCheque, Integer localizacao) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void alterarChequeBaixa(ChequeVO cheque) throws Exception;

    public Integer incluirChequePagamento(Integer codigo, Integer unidadeEnsino, ChequeVO cheque, UsuarioVO usuario) throws Exception;

    public Integer incluirChequeRecebimento(Integer codigo, Integer codigo2, Integer codigo3, ChequeVO cheque, UsuarioVO usuario) throws Exception;

    public void inutilizarCheque(Integer codigoCheque) throws Exception;

    public List consultarPorNomePessoaSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(String valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorSacadoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(String valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeBancoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(String valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNumeroAgenciaAgenciaSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(String valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNumeroSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(String valorConsulta, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataEmissaoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(Date prmIni, Date prmFim, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataPrevisaoSituacaoSemMovimentacaoFinanceiraPendenteFinalizada(Date prmIni, Date prmFim, String situacao, Integer localizacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorDataPrevisaoSituacao(Date prmIni, Date prmFim, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorDataEmissaoSituacao(Date prmIni, Date prmFim, List<String> situacao, List<ContaCorrenteVO> lista, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorNumeroAgenciaAgenciaSituacao(String valorConsulta, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarPorNumeroSituacao(String valorConsulta, List<String> situacoes, List<ContaCorrenteVO> listaCaixas, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarChequePendenteCompesacaoChequeProprioPagamentoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ChequeVO> consultarChequePendenteCompesacaoRecebimentoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void alterarChequeReapresentacao(final ChequeVO obj) throws Exception;

    public List consultarPorLocalizacaoCheque(Integer localizacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List consultarPorLocalizacaoSituacaoCheque(Integer localizacao, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Double consultarTotalChequePendenteCompesacaoChequeProprioPagamentoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Double consultarTotalChequePendenteCompesacaoRecebimentoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List<ChequeVO> consultarChequeDevolvidoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Double consultarTotalChequeDevolvidoPorContaCorrente(Integer contaCorrente, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	ChequeVO consultarUltimoChequePorSacadoTipoSacado(String tipoSacado, Integer sacado, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void validarUnicidadeCheque(ChequeVO chequeVO, UsuarioVO usuario) throws Exception;
	
	public List<ChequeVO> consultarPorCodigoSacadoTipoPessoaESituacao(Integer sacado, String tipoSacado, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void alterarPessoaCheque(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 21/05/2015
	 * @param pessoaAntigo
	 * @param pessoaNova
	 * @throws Exception
	 */
	void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception;

	public ChequeVO consultarChequeDevolvidoPorContaReceberCodOrigemTipoOrigem(String codOrigem, String tipoOrigem) throws Exception;
}

