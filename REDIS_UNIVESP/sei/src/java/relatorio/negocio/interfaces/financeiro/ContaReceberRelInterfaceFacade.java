package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import relatorio.negocio.comuns.financeiro.ContaReceberRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

public interface ContaReceberRelInterfaceFacade {

	/**
	 * 
	 * @param filtroRelatorioFinanceiroVO
	 * @param dataInicio
	 * @param dataFim
	 * @param tipoPessoa
	 * @param alunoMatriula
	 * @param alunoNome
	 * @param funcionarioMatricula
	 * @param funcionarioNome
	 * @param candidatoCpf
	 * @param candidatoNome
	 * @param parceiroCPF
	 * @param parceiroCNPJ
	 * @param parceiroNome
	 * @param situacao
	 * @param unidadeEnsinoVOs
	 * @param unidadeEnsinoCursoVO
	 * @param turmaVO
	 * @param contaCorrente
	 * @param pessoa
	 * @param parceiroCodigo
	 * @param opcaoOrdenacao
	 * @param usuarioLogado
	 * @param confFinanVO
	 * @param centroReceita
	 * @param condicaoPagamentoPlanoFinanceiroCurso
	 * @param fornecedor
	 * @param responsavelFinanceiroCodigo
	 * @param dataInicioCompetencia
	 * @param dataFimCompetencia
	 * @param consideraUnidadeEnsinoFinanceira
	 * @return
	 * @throws Exception
	 */
	public List<ContaReceberRelVO> criarObjeto(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Date dataInicio, Date dataFim, String tipoPessoa, String alunoMatriula, String alunoNome, String funcionarioMatricula, String funcionarioNome, String candidatoCpf, String candidatoNome, String parceiroCPF, String parceiroCNPJ, String parceiroNome, String situacao, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, ContaCorrenteVO contaCorrente, Integer pessoa, Integer parceiroCodigo, Integer opcaoOrdenacao, UsuarioVO usuarioLogado, ConfiguracaoFinanceiroVO confFinanVO, Integer centroReceita, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, Integer responsavelFinanceiroCodigo, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean consideraUnidadeEnsinoFinanceira) throws Exception;

	public void inicializarOrdenacoesRelatorio();

	public void setOrdenarPor(int intValue);

	public Vector getOrdenacoesRelatorio();

	public void setDescricaoFiltros(String string);

	public String designIReportRelatorio(String tipoLayout);

	String caminhoBaseRelatorio();

	public String designIReportRelatorioExcel(String tipoLayout);

	void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception;
}
