package relatorio.negocio.interfaces.compras;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;

import relatorio.negocio.comuns.compras.ProdutoServicoRelVO;

public interface ProdutoServicoRelInterfaceFacade {


	public List<ProdutoServicoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception;

	public ProdutoServicoRelVO montarDados(SqlRowSet dadosSQL) throws Exception;

	String caminhoBaseRelatorio();

	String designIReportRelatorio();
	
	String designIReportRelatorioExcel();

	List<ProdutoServicoRelVO> criarObjeto(Integer categoriaProduto, String tipoProduto, String situacaoProduto, UsuarioVO usuario) throws Exception;
	
}