package relatorio.negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FornecedorVO;
import relatorio.negocio.comuns.compras.CompraRelVO;

public interface CompraRelInterfaceFacade {


	public List<CompraRelVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception;

	public CompraRelVO montarDados(SqlRowSet dadosSQL) throws Exception;

	String caminhoBaseRelatorio();

	String designIReportRelatorio();
	
	String designIReportRelatorioExcel();

	void validarDados(FornecedorVO fornecedor, Date dataInicio, Date dataFim, Double valorCompraInicio, Double valorCompraFim) throws Exception;

	List<CompraRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, FornecedorVO fornecedor, Integer numeroCompra, Double valorCompraInicio, Double valorCompraFim, String situacaoRecebimento, String situacaoFinanceira, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;
	
}