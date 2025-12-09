package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.RecebimentoCentroReceitaRelVO;
import relatorio.negocio.comuns.financeiro.RecebimentoPorCursoCentroReceitaRelVO;
import relatorio.negocio.comuns.financeiro.RecebimentoRelVO;

public interface RecebimentoRelInterfaceFacade {

	public List<RecebimentoRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer curso, Integer turma, Integer turno, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String tipoPessoa, Integer codigoPessoa, Integer codigoParceiro, Date dataInicio, Date dataFim, Integer codigoContaCorrente, String tipoOrdenacao, String parcela, UsuarioVO usuarioLogado, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, String layout, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean apresentarQuadroResumoCalouroVeterano, boolean considerarUnidadeEnsinoFinanceira, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, String filtrarPeriodoPor, MatriculaVO matriculaVO) throws Exception;
	
	List<RecebimentoCentroReceitaRelVO> realizarGeracaoListaRecebimentoCentroReceita(List<RecebimentoRelVO> recebimentoRelVOs);
	
	public List<FormaPagamentoNegociacaoRecebimentoVO> criarRelatorioSinteticoPorFormaRecebimento(List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer curso, Integer turma, Integer turno, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String tipoPessoa, Integer codigoPessoa, Integer codigoParceiro, Date dataInicio, Date dataFim, Integer codigoContaCorrente, String tipoOrdenacao, String parcela, UsuarioVO usuarioLogado, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, String layout, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean apresentarQuadroResumoCalouroVeterano, boolean considerarUnidadeEnsinoFinanceira, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, String filtrarPeriodoPor, MatriculaVO matriculaVO) throws Exception;
        
        public List<RecebimentoPorCursoCentroReceitaRelVO> criarRelatorioFaturamentoSinteticoPorCursoTipoOrigem(
                List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer curso, Integer turma, Integer turno, 
                FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String tipoPessoa, Integer codigoPessoa, 
                Integer codigoParceiro, Date dataInicio, Date dataFim, Integer codigoContaCorrente, String tipoOrdenacao, 
                String parcela, UsuarioVO usuarioLogado, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso, 
                Integer fornecedor, String layout, Boolean controlarDataBaseFaturamento, Date dataBaseFaturamento, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean apresentarQuadroResumoCalouroVeterano, boolean considerarUnidadeEnsinoFinanceira, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, String filtrarPeriodoPor, MatriculaVO matriculaVO) throws Exception;    
}
