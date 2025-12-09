package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.financeiro.ProcessamentoIntegracaoFinanceiraDetalheVO;

public interface IntegracaoFinanceiroInterfaceFacade {

	void executarIntegracaoFinanceiro(final Integer codigoProcessamentoIntegracaoFinanceiro);
	public List<ProcessamentoIntegracaoFinanceiraDetalheVO> consultaContaSerProcessada(Integer codigoProcessamentoIntegracaoFinanceiro, int limit, Integer offset) throws Exception;
	/**
	 * @author Rodrigo Wind - 23/11/2015
	 * @param codigoProcessamentoIntegracaoFinanceiro
	 * @return
	 * @throws Exception
	 */
	ProcessamentoIntegracaoFinanceiraDetalheVO consultaPorCodigoProcessamentoIntegracaoFinanceiro(Integer codigoProcessamentoIntegracaoFinanceiro) throws Exception;
	public  Boolean realizarVerificacaoProcessamentoIntegracaoFinanceira() ;
	List<PlanoFinanceiroAlunoDescricaoDescontosVO> realizarGeracaoPlanoDescontoDescricaoAluno(
			ProcessamentoIntegracaoFinanceiraDetalheVO processamentoIntegracaoFinanceiraDetalheVO,
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

}