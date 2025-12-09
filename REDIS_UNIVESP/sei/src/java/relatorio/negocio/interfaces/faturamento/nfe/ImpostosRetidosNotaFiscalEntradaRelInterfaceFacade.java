package relatorio.negocio.interfaces.faturamento.nfe;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import relatorio.negocio.comuns.faturamento.nfe.ImpostosRetidosNotaFiscalEntradaRelVO;

public interface ImpostosRetidosNotaFiscalEntradaRelInterfaceFacade {


	public List<ImpostosRetidosNotaFiscalEntradaRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, String layout) throws Exception;

	public ImpostosRetidosNotaFiscalEntradaRelVO montarDados(SqlRowSet dadosSQL, String layout) throws Exception;

	String caminhoBaseRelatorio();

	String designIReportRelatorio(String layout);
	
	String designIReportRelatorioExcel(String layout);

	void validarDados(Date dataInicio, Date dataFim) throws Exception;
	
	void validarData(Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim) throws Exception;

	List<ImpostosRetidosNotaFiscalEntradaRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, FornecedorVO fornecedor, EstadoVO estado,
			CidadeVO cidade, List<ImpostoVO> listaImpostos, Date dataInicio, Date dataFim,Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim, String layout, UsuarioVO usuario) throws Exception;
	
	List<ImpostoVO> criarListaImposto(List<UnidadeEnsinoVO> listaUnidadeEnsino, FornecedorVO fornecedor, EstadoVO estado,
			CidadeVO cidade, List<ImpostoVO> listaImpostos, Date dataInicio, Date dataFim, Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim) throws Exception;
	
	List<ImpostosRetidosNotaFiscalEntradaRelVO> criarListaImpostoPorUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino, FornecedorVO fornecedor, EstadoVO estado,
			CidadeVO cidade, List<ImpostoVO> listaImpostos, Date dataInicio, Date dataFim,Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim) throws Exception;
	
	List<ImpostosRetidosNotaFiscalEntradaRelVO> criarListaImpostoPorFornecedor(List<UnidadeEnsinoVO> listaUnidadeEnsino, FornecedorVO fornecedor, EstadoVO estado,
			CidadeVO cidade, List<ImpostoVO> listaImpostos, Date dataInicio, Date dataFim,Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim) throws Exception;
	
}