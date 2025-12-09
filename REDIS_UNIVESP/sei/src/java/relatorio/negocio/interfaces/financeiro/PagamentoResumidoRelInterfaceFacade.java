package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import negocio.comuns.administrativo.UnidadeEnsinoVO;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.financeiro.PagamentoRelVO;

public interface PagamentoResumidoRelInterfaceFacade {

    List<PagamentoRelVO> criarObjeto(Date dataInicio, Date dataFim, UnidadeEnsinoVO unidadeEnsino, Integer fornecedor, String fornecedorNome, String fornecedorCpfCnpj,
                String funcionarioNome, Integer funcionario, Integer banco, String bancoNome, Integer filtroFornecedor, Integer filtroFuncionario, Integer filtroBanco, String formaPagamento, Integer filtroTipo) throws Exception;

    public SqlRowSet executarConsultaParametrizada(Date dataInicio, Date dataFim, UnidadeEnsinoVO unidadeEnsino, Integer fornecedor, String fornecedorNome, String fornecedorCpfCnpj,
            String funcionarioNome, Integer funcionario, Integer banco, String bancoNome, Integer filtroFornecedor, Integer filtroFuncionario, Integer filtroBanco, String formaPagamento, Integer filtroTipo) throws Exception;

    public Vector getOrdenacoesRelatorio();

    String designIReportRelatorio();

    String caminhoBaseRelatorio();
}
