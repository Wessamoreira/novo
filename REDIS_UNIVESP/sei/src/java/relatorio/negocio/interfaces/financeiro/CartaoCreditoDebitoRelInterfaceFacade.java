package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import relatorio.negocio.comuns.financeiro.CartaoCreditoDebitoRelVO;

public interface CartaoCreditoDebitoRelInterfaceFacade {

	String caminhoBaseRelatorio();

	String designIReportRelatorio();

	void validarDados(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataFim, String tipoRelatorio) throws Exception;
	
	public List<CartaoCreditoDebitoRelVO> gerarListaAnalitico(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<OperadoraCartaoVO> listaOperadoraCartao, List<CursoVO> listaCurso, Date dataInicio, Date dataFim, String filtrarPor, String situacaoBaixa, MatriculaVO matricula, PessoaVO responsavelFinanceiro, Integer fornecedor, Integer candidato, Integer parceiro, String tipoSacado) throws Exception ;
	
	public List<CartaoCreditoDebitoRelVO> gerarListaSintetico(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<OperadoraCartaoVO> listaOperadoraCartao, List<CursoVO> listaCurso, Date dataInicio, Date dataFim, String filtrarPor, String situacaoBaixa, MatriculaVO matricula, PessoaVO responsavelFinanceiro, Integer fornecedor, Integer candidato, Integer parceiro, String tipoSacado) throws Exception ;	
}
