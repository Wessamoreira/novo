package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.OrdemDescontoVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

public interface MatriculaPeriodoVencimentoInterfaceFacade {

    /**
     * Operação responsável por retornar um novo objeto da classe <code>MatriculaPeriodoVencimentoVO</code>.
     */
    public MatriculaPeriodoVencimentoVO novo() throws Exception;

    public void incluir(MatriculaPeriodoVencimentoVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(MatriculaPeriodoVencimentoVO obj, boolean modificarSituacaoContaReceber, UsuarioVO usuario) throws Exception;

    public void excluirContaReceber(Integer contaReceber, UsuarioVO usuario) throws Exception;

    public void excluirSemCommitVctoNaoPagos(MatriculaPeriodoVO matriculaPeriodoVO, Boolean possuiValorRateio,  UsuarioVO usuario) throws Exception;
    
    public void excluirSemCommitVctoMatricula(MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuario) throws Exception;
    
    public void excluirMatriculaPeriodoVencimentoSemCommit(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario, MatriculaPeriodoVencimentoVO vctoExcluir) throws Exception;

    public void excluirTodos(Integer codigoMatriculaPeriodo, UsuarioVO usuario) throws Exception;

    public void excluir(Integer codigoExcluir, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVencimentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String idEntidade);

    public MatriculaPeriodoVencimentoVO consultarPorContaReceber(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void excluirMatriculaPeriodoVencimentoVOsNaoUtilizadosMais(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

    public void gravarMatriculaPeriodoVencimentoVOs(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception;

    public void atualizarMatriculaPeriodoVencimentoPelaContaReceberVO(ContaReceberVO contaReceberBase, MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVo, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVencimentoVO preencherNovaMatriculaPeriodoVencimentoVo(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVo, MatriculaPeriodoVO matriculaPeriodoVo, UsuarioVO usuario);

    public void gravarMatriculaPeriodoVencimento(MatriculaPeriodoVO matriculaPeriodoVo, MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVo, boolean novaMatriculaPeriodoVencimento, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

    public List consultarMatriculaPeriodoVencimentoVOsSituacao(Integer codMatriculaPeriodo, String string, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVencimentoVO> consultarPorMesReferenciaSituacaoUnidade(
            String mesReferencia,
            String anoReferencia,
            String situacao,
            Integer unidadeEnsino,
            Integer curso,
            TurmaVO turma,
            Boolean permitirGerarParcelaPreMatricula,
            boolean trazerMatriculasComCanceladoFinanceiro,
            int nivelMontarDados,             
            ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, 
            UsuarioVO usuario, 
            Integer limit,
            Integer geracaoManulaParcela,
            Boolean utilizarDataCompetencia
            ) throws Exception;

    public List<MatriculaPeriodoVencimentoVO> consultarPorMesReferenciaSituacaoUnidadeAluno(
            String mesReferencia,
            String anoReferencia,
            String situacao,
            String matriculaAluno,
            Integer unidadeEnsino,
            Boolean permitirGerarParcelaPreMatricula,
            boolean trazerMatriculasComCanceladoFinanceiro,
            int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void atualizarDataVencimentoMatriculaPeriodoVencimentoAposRegerarBoleto(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVencimentoVO> consultarPorMatricula(String matricula, String parcela, String ano, String semestre, Boolean contaVencida, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Optional<FiltroRelatorioFinanceiroVO> filtroRelatorioFinanceiroVO, UsuarioVO usuario) throws Exception;

    public Integer consultarDiasVariacaoDataVencimentoPeloCodigoContaReceber(Integer codigoContaReceber, UsuarioVO usuario) throws Exception;

    List<MatriculaPeriodoVencimentoVO> consultarPorMatriculaPeriodo(Integer codigoMatriculaPeriodo, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVencimentoVO consultarMatriculaVencimentoPorContaReceber(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public Integer consultarPorMatriculaPeriodoParcelaExtraSituacao(Integer codigoMatriculaPeriodo, String situacaoPeriodoVencimento, UsuarioVO usuario) throws Exception;

    public void alterarSituacaoMatriculaPeriodoVencimento(Integer codigoContaReceber, String situacao, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVencimentoVO> consultarPorMatriculaPeriodoESituacaoParcelaExtra(Integer codigoMatriculaPeriodo, String situacaoPeriodoVencimento, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<MatriculaPeriodoVencimentoVO> consultarMatriculaPeriodoVencimentoNaoPagaPorMatricula(String matricula, Date dataInicial, Date dataFinal, Boolean consultarContaVencida, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void realizarAlteracaoDataVencimentoPorDataBaseGeracaoTurma(final TurmaVO turma, Date dataBaseGeracaoParcelas, List<MatriculaVO> listaMatriculaComControleGeracaoParcelaDataBase, UsuarioVO usuario) throws Exception;
    
    public void realizarAlteracaoDiaVencimento(List<MatriculaPeriodoVencimentoVO> listaMatriculaPeriodoVencimentoVOs, Integer novoDia, UsuarioVO usuario) throws Exception;

    public void alterarDataVencimento(MatriculaPeriodoVencimentoVO obj) throws Exception;

    public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

    public void excluirComBaseNaContaReceberDataVencimentoSituacaoMatricula(String matricula, String situacao, UsuarioVO usuario) throws Exception;

    public Boolean consultarExisteMatriculaPeriodoVencimentoPorMatriculaPeriodoSituacao(Integer matriculaPeriodo, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void alterarSituacaoValorDescontosMatriculaPeriodoVencimento(MatriculaPeriodoVencimentoVO obj, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoVencimentoVO consultarPorMatriculaPeriodoParcela(Integer codigoMatriculaPeriodo, String parcela, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public void excluirPorMatriculaPeriodoContaReceberNaoPagaNaoNegociada(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception;

    public void excluirComBaseNaMatriculaPeriodo(Integer codMatriculaPeriodo, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;
    
    public Boolean consultarPorMatriculaPeriodoParcelaSituacaoExistente(Integer matriculaPeriodo, String parcela, String situacao, UsuarioVO usuarioVO);

	Map<String, Object> consultarPorMesReferenciaSituacaoUnidadeTotalRegistroTotalValor(String mesReferencia, String anoReferencia, String situacao, Integer unidadeEnsino, Integer curso, TurmaVO turma, Boolean permitirGerarParcelaPreMatricula, Boolean utilizarDataCompetencia) throws Exception;

	void realizarRegistroErroGeracaoContaReceber(Integer matriculaPeriodoVencimento, Integer geracaoManualParcela, String erro) throws Exception;

	List<MatriculaPeriodoVencimentoVO> consultarMatriculaPeriodoVencimentoPorGeracaoManualParcela(Integer geracaoManualParcela, String matricula, String aluno, String curso, Date dataVencimento, String parcela,  Boolean processadaComErro, Integer limite, Integer offset) throws Exception;

	Integer consultarMatriculaPeriodoVencimentoPorGeracaoManualParcelaTotalRegistro(Integer geracaoManualParcela, String matricula, String aluno, String curso, Date dataVencimento, String parcela, Boolean processadaComErro) throws Exception;

	List<MatriculaPeriodoVencimentoVO> consultarMatriculaPeriodoVencimentoParcelaReajusteAReceberAptasParaAplicacaoReajuste(Integer indiceReajuste, Integer mes, String ano, Integer parcelaInicialReajuste, Integer matriculaPeriodo, Boolean limit1 ,Boolean considerarDescontoSemValidadeCalculoIndiceReajuste, UsuarioVO usuarioVO) throws Exception;

//	List<MatriculaPeriodoVencimentoVO> consultarMatriculaPeriodoVencimentoParcelaReajusteARecebidaAptasParaAplicacaoReajuste(Integer indiceReajuste, Integer mes, String ano, Integer parcelaInicialReajuste, Integer matriculaPeriodo, Boolean limit1,Boolean considerarDescontoSemValidadeCalculoIndiceReajuste, UsuarioVO usuarioVO) throws Exception;

	void realizarGeracaoDescricaoDescontoMatriculaPeriodoVencimento(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<OrdemDescontoVO> ordemDesconto, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
	
	public Boolean consultarExisteMatriculaPeriodoVencimentoGeradaPagaOuNegociada(Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public void atualizarDataVencimentoEDataCompetencia(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO, UsuarioVO usuario) throws Exception;

	void realizarGeracaoDescricaoParcelaMensalidade(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO,
			MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<OrdemDescontoVO> ordemDesconto,
			ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception;

	void realizarGeracaoDescricaoParcelaMaterialDidatico(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO,
			MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List<OrdemDescontoVO> ordemDesconto,
			ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception;
	
	public void consultarValorMatriculaMensalidadeMatriculaPeriodoVencimento(MatriculaPeriodoVO matriculaPeriodoVO, boolean controlarAcesso, int nivelMontarDados, String tipoBoleto, UsuarioVO usuario) throws Exception;

}
