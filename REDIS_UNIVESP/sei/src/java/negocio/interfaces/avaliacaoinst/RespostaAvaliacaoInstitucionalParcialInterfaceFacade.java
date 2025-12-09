package negocio.interfaces.avaliacaoinst;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.QuestionarioVO;

public interface RespostaAvaliacaoInstitucionalParcialInterfaceFacade {

	
	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * @param obj  Objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code> que será gravado no banco de dados.
	 * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void incluir(RespostaAvaliacaoInstitucionalDWVO obj,UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * @param obj    Objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code> que será alterada no banco de dados.
	 * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	public void alterar(RespostaAvaliacaoInstitucionalDWVO obj, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code>.
	 * Sempre localiza o registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * @param obj    Objeto da classe <code>RespostaAvaliacaoInstitucionalDWVO</code> que será removido no banco de dados.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(RespostaAvaliacaoInstitucionalDWVO obj, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * Responsável por realizar uma consulta de <code>RespostaAvaliacaoInstitucionalDW</code> através do valor do atributo 
	 * <code>Integer avaliacaoInstitucional</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return  List Contendo vários objetos da classe <code>RespostaAvaliacaoInstitucionalDWVO</code> resultantes da consulta.
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RespostaAvaliacaoInstitucionalDWVO> consultarPorAvaliacaoInstitucionalRespondente(Integer avaliacaoInstitucional, Integer respondente, String matriculaAluno) throws Exception;

	void excluirPorAvaliacaoRespondente(Integer avaliacao, Integer respondente, String matriculaAluno, UsuarioVO usuarioVO) throws Exception;

	void gravarRespostaAvaliacaoInstitucionalParcial(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UnidadeEnsinoVO unidadeEnsinoVO, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, QuestionarioVO questionarioVO, PerguntaQuestionarioVO perguntaQuestionarioVO, String matriculaFuncionario, UsuarioVO usuarioVO) throws Exception;

	void realizarRecuperacaoRespostaParcial(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, List<QuestionarioVO> questionarioVOs, MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception;

}