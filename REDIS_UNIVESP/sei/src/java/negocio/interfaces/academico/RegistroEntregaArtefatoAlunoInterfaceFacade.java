package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArtefatoEntregaAlunoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.RegistroEntregaArtefatoAlunoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface RegistroEntregaArtefatoAlunoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>RegistroEntregaArtefatoAlunoVO</code>.
	 */
	public RegistroEntregaArtefatoAlunoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>RegistroEntregaArtefatoAlunoVO</code>. Verifica a conexão com o banco de dados
	 * e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroEntregaArtefatoAlunoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluir(ArtefatoEntregaAlunoVO artefartoEntregaAluno, RegistroEntregaArtefatoAlunoVO registroEntregaArtefatoAluno, DisciplinaVO disciplina, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>RegistroEntregaArtefatoAlunoVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado. Verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>RegistroEntregaArtefatoAlunoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterar(ArtefatoEntregaAlunoVO artefartoEntregaAluno, RegistroEntregaArtefatoAlunoVO obj, DisciplinaVO disciplina, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por realizar uma consulta de
	 * <code>registroEntregaArtefatoAluno</code> através do código da pessoa
	 * <code>RegistroEntregaArtefatoAlunoVO</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroEntregaArtefatoAlunoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */

	public List consultarPorAluno(PessoaVO pessoa, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;
	
	/**
	 * Responsável por realizar uma consulta de <code>RegistroEntregaArtefatoAluno</code>
	 * através do artefato. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroEntregaArtefatoAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<RegistroEntregaArtefatoAlunoVO> consultarRegistroEntregaArtefatoAluno(ArtefatoEntregaAlunoVO artefatoEntregaAluno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;

	public void validarDados(ArtefatoEntregaAlunoVO obj, String ano, String semestre, DisciplinaVO disciplina) throws Exception;
	
	public List<RegistroEntregaArtefatoAlunoVO> consultarAlunos(ArtefatoEntregaAlunoVO artefatoEntregaAlunoVO, Date dataInicio, Date dataFim, String ano, String semestre, UnidadeEnsinoVO unidadeEnsinoVO, CursoVO curso, TurmaVO turma, DisciplinaVO disciplinaVO, MatriculaVO matricula, String situacaoEntrega,
					UsuarioVO usuario)throws Exception;
	
	/**
	 * Responsável por realizar uma consulta de <code>RegistroEntregaArtefatoAluno</code>
	 * através do artefato e da matriculaperiodo. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>RegistroEntregaArtefatoAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public RegistroEntregaArtefatoAlunoVO verificarArtefatoEntregueAluno(ArtefatoEntregaAlunoVO artefatoEntregaAluno, RegistroEntregaArtefatoAlunoVO registroEntregaArtefatoAluon, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;
	
	public String caminhoBaseRelatorio();

	String designIReportRelatorio(String layout);

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String aIdEntidade);

}