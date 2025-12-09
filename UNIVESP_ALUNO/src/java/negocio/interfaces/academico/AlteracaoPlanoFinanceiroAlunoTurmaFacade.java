package negocio.interfaces.academico;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

//import negocio.comuns.academico.AlteracaoPlanoFinanceiroAlunoTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.utilitarias.ProgressBarVO;

/**
 * Interface de contrato das classes AlteracaoPlanoFinanceiroAlunoTurma e AlteracaoPlanoFinanceiroAlunoTurmaVO 
 */
public interface AlteracaoPlanoFinanceiroAlunoTurmaFacade {

	public void setIdEntidade(String aIdEntidade);

	
	/**
	 * Consulta a lista de alunos da turma que ja tem parcelas geradas (SituacaoAlteracaoPlanoFinanceiroEnum.COM_PARCELA_GERADA)
	 * 
	 * @return
	 */
//	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosDaTurmaComParcelasGeradas(Integer turma, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	
	/**
	 * Monta lista dos dados
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
//	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception;


//	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosComCondicaoPagamentoNaoEncontrado(Integer turma, Integer codigoNovoPlanoFinanceiroCurso, String categoria,
//			int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception;
//
//
//	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosQueSeraoAlterados(Integer turma, Integer codigoNovoPlanoFinanceiroCurso, String categoria,
//			int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception;
//
//
//	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosComValorInferiorAoNovoPlano(Integer turma, Integer codigoNovoPlanoFinanceiroCurso, String categoria, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception;


//	public void alterarPlanoFinanceiroAlunoConformeDadosDaTurma(final List<AlteracaoPlanoFinanceiroAlunoTurmaVO> listaDeAlunosQueSeraoAlterados, final UsuarioVO usuarioVO, TurmaVO turmaVO, final ProgressBarVO progressBarVO) throws Exception;
	
	
	/**
	 * 
	 * Consulta os alunos que estao com a condicao de pagamento em conformidade com o da turma
	 * 
	 *  
	 * @param turma
	 * @param codigoNovoPlanoFinanceiroCurso
	 * @param categoria
	 * @param nivelmontardadosDadosbasicos
	 * @param usuarioLogado
	 * @return
	 * @throws Exception
	 */
//	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosComCondicaoPagamentoEmConformidade(Integer turma, Integer codigoNovoPlanoFinanceiroCurso, String categoria, 
//			int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception;
//
//
//	/**
//	 * Consulta listagem dos alunos (matricula) que sofreram alteracao da sua condicao de pagamento - LOG
//	 * 
//	 * @param turmaVO
//	 * @return
//	 */
//	public List<AlteracaoPlanoFinanceiroAlunoTurmaVO> consultarAlunosQueSofreramAlteracaoNaCondicaoPagamento(TurmaVO turmaVO, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado);
}