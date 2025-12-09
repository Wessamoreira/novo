package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.InadimplenciaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface InadimplenciaRelInterfaceFacade {

	public List<InadimplenciaRelVO> gerarLista(List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, Date dataInicio, Date dataFim, String ordenacao, ConfiguracaoFinanceiroVO configuracao, PessoaVO responsavelFinanceiro,String situacaoRegristroCobranca) throws Exception ;

	public List<InadimplenciaRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, ConfiguracaoFinanceiroVO configuracao) throws Exception;

	public InadimplenciaRelVO montarDados(SqlRowSet dadosSQL, ConfiguracaoFinanceiroVO configuracao) throws Exception;

	public void calcularJuroMulta(InadimplenciaRelVO obj, SqlRowSet dadosSQL, ConfiguracaoFinanceiroVO configuracao) throws Exception;

	public Double getJurosPorcentagem();

	public void setJurosPorcentagem(Double jurosPorcentagem);

	public Double getMultaPorcentagem();

	public void setMultaPorcentagem(Double multaPorcentagem);

	public Integer gerarQuantidadeAlunosInadimplentes(List<CursoVO> cursoVOs,List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia, TurmaVO turmaVO, MatriculaVO matriculaVO,  Date dataInicio, Date dataFim, UsuarioVO usuario, PessoaVO responsavelFinanceiro, boolean filtrarAlunosSemEmail, FiltroRelatorioAcademicoVO filtroAcademicoVO, Boolean trazerMatriculaSerasa,  Boolean consideraUnidadeEnsinoFinanceira, Boolean imprimirApenasMatriculas, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,CentroReceitaVO centroReceitaVO, String situacaoRegristroCobranca) throws Exception;

	String caminhoBaseRelatorio();

	String designIReportRelatorio();
	
	String designIReportRelatorioExcel();

	void validarDados(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataFim, String tipoRelatorio) throws Exception;

	List<InadimplenciaRelVO> gerarListaComDesconto(List<CursoVO> cursoVOs,List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia, TurmaVO turmaVO, MatriculaVO matriculaVO, String ordenarPor,  Date dataInicio, Date dataFim,  UsuarioVO usuario, PessoaVO responsavelFinanceiro, boolean filtrarAlunosSemEmail, FiltroRelatorioAcademicoVO filtroAcademicoVO, Boolean trazerMatriculaSerasa, Boolean considerarUnidadeEnsinoFinanceira,  Boolean imprimirApenasMatriculas, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,CentroReceitaVO centroReceitaVO, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente,String situacaoRegistroCobranca) throws Exception;
    
	List<InadimplenciaRelVO> gerarListaInadimplenteEnvioNotificacao(List<CursoVO> cursoVOs,List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia, TurmaVO turmaVO, MatriculaVO matriculaVO, String ordenarPor,  Date dataInicio, Date dataFim,  UsuarioVO usuario, PessoaVO responsavelFinanceiro, boolean filtrarAlunosSemEmail, FiltroRelatorioAcademicoVO filtroAcademicoVO, Boolean trazerMatriculaSerasa, Boolean considerarUnidadeEnsinoFinanceira, Boolean imprimirApenasMatriculas, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,CentroReceitaVO centroReceitaVO) throws Exception;

}