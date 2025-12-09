package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import negocio.comuns.administrativo.UnidadeEnsinoVO;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.financeiro.PagamentoRelVO;

public interface PagamentoRelInterfaceFacade {



    public Vector getOrdenacoesRelatorio();

    public String designIReportRelatorio(String tipoRelatorio, String layout);

    String caminhoBaseIReportRelatorio();


	List<PagamentoRelVO> criarObjeto(Date dataInicio, Date dataFim, Boolean filtrarDataFatoGerador, UnidadeEnsinoVO unidadeEnsino, Integer fornecedor, String fornecedorNome, String fornecedorCpfCnpj, String funcionarioNome, Integer funcionario, Integer banco, String bancoNome, Integer filtroFornecedor, Integer filtroFuncionario, Integer filtroBanco, Integer aluno, Integer filtroAluno, String ordenacao, String tipo, Integer filtroTipo, String tipoRelatorio, Integer filtroResponsavelFinanceiro, Integer responsavelFinanceiro, String nomeResponsavelFinanceiro, Integer filtroParceiro, Integer parceiro, String nomeParceiro, String tipoConta, Integer codigoContaCorrenteCaixa, Integer filtroOperadoraCartao, Integer operadoraCartao) throws Exception;

	SqlRowSet executarConsultaParametrizada(Date dataInicio, Date dataFim, Boolean filtrarDataFatoGerador, UnidadeEnsinoVO unidadeEnsino, Integer fornecedor, String fornecedorNome, String fornecedorCpfCnpj, String funcionarioNome, Integer funcionario, Integer banco, String bancoNome, Integer filtroFornecedor, Integer filtroFuncionario, Integer filtroBanco, Integer aluno, Integer filtroAluno, String ordenacao, String tipo, Integer filtroTipo, Integer filtroResponsavelFinanceiro, Integer responsavelFinanceiro, Integer filtroParceiro, Integer parceiro, String tipoConta, Integer codigoContaCorrenteCaixa, Integer filtroOperadoraCartao, Integer operadoraCartao) throws Exception;
}
