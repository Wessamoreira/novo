/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import relatorio.negocio.comuns.compras.RequisicaoRelVO;

/**
 *
 * @author Philippe
 */
public interface RequisicaoRelInterfaceFacade {

    public List<RequisicaoRelVO> criarObjetoLayout1(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, Date dataInicio, Date dataFim, RequisicaoVO requisicao) throws Exception;
    
    public List<RequisicaoRelVO> criarObjetoLayout2(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, Date dataInicio, Date dataFim, RequisicaoVO requisicao) throws Exception;
    
    public List<RequisicaoRelVO> criarObjetoLayout3(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, Date dataInicio, Date dataFim, RequisicaoVO requisicao) throws Exception;
    
    public List<RequisicaoRelVO> criarObjetoLayout4(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, RequisicaoVO requisicao, List<CentroResultadoVO> listaCentroResultadoEstoque, Date dataInicioPeriodoConsumo, Date dataFimPeriodoConsumo) throws Exception;
    
    public void validarDados(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataFim) throws Exception;
    
    public String designIReportRelatorio(String layout);
    
    public String designIReportRelatorioExcel(String layout);
    
    public String caminhoBaseRelatorio();

    public String caminhoBaseQuestionarioRelatorio();

}
