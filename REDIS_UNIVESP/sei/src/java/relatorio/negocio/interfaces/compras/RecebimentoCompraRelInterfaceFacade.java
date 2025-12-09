/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.compras;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import relatorio.negocio.comuns.compras.RecebimentoCompraRelVO;
import relatorio.negocio.comuns.compras.RequisicaoRelVO;

/**
 * 
 * @author Philippe
 */
public interface RecebimentoCompraRelInterfaceFacade {

	public List<RecebimentoCompraRelVO> criarObjetoLayoutRecebimentoCompra(UnidadeEnsinoVO unidadeEnsino, Optional<Date> dataInicial, Optional<Date> dataFinal, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, FornecedorVO fornecedor, String situacaoRecebimento, String ordenacaoRelatorio) throws Exception;

	public Double montarValorTotalRecebimento(UnidadeEnsinoVO unidadeEnsino, Date dataInicial, Date dataFinal, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, FornecedorVO fornecedor, String situacaoRecebimento) throws Exception;
	
	public Double[] montarTotaisRecebimento(UnidadeEnsinoVO unidadeEnsino, Date dataInicial, Date dataFinal, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, FornecedorVO fornecedor, String situacaoRecebimento) throws Exception;

	public String designIReportRelatorio(String layout);

	public String designIReportRelatorioExcel(String layout);

	public String caminhoBaseRelatorio();

}
