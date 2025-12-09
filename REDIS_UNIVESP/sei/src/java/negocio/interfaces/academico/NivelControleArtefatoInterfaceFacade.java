/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ArtefatoEntregaAlunoVO;
import negocio.comuns.academico.NivelControleArtefatoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Ana Claudia
 */
public interface NivelControleArtefatoInterfaceFacade {

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>NivelControleArtefatoVO</code>.
	 */
	public NivelControleArtefatoVO novo() throws Exception;

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>NivelControleArtefatoVO</code>. Verifica a conexão com o banco de dados
	 * e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>NivelControleArtefatoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void incluirNivelControleArtefatoUnidadeEnsino(NivelControleArtefatoVO obj) throws Exception;

	public void incluirNivelControleArtefatoCurso(NivelControleArtefatoVO obj) throws Exception;

	public void incluirNivelControleArtefatoDisciplina(NivelControleArtefatoVO obj) throws Exception;

	public void incluirNivelControleArtefatoFuncionario(NivelControleArtefatoVO obj) throws Exception;

	/**
	 * Operação responsável por excluir do banco de dados o objeto da classe
	 * <code>NivelControleArtefatoVO</code>,que não esteja mais na lista de
	 * NivelControeArtefatoVOs. E incluir no banco de dados um objeto da classe
	 * <code>NivelControleArtefatoVO</code>, que seja novo na lista de
	 * NivelControleArtefatoVOs. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ArtefatoEntregaAlunoVO</code> que tem as
	 *            listas de NivelControleArtefato.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	public void alterarNivelControleArtefato(ArtefatoEntregaAlunoVO artefatoEntregaAluno, UsuarioVO usuario)
			throws Exception;

	public List<NivelControleArtefatoVO> consultarNivelControleArtefato(Integer artefatoEntregaAluno,
			int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>NivelControleArtefatoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>NivelControleArtefatoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */

	public void excluir(NivelControleArtefatoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por excluir no BD todos objetos da classe
	 * <code>NivelControleArtefatoVO</code> que tenha o codigo do artefato
	 * informado. Sempre localiza o registro a ser excluído através do campo
	 * artefato. Primeiramente verifica a conexão com o banco de dados e a permissão
	 * do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ArtefatoEntregaAlunoVO</code>.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluirNivelControleArtefato(ArtefatoEntregaAlunoVO obj, UsuarioVO usuario) throws Exception;

	/**
	 * Operação responsável por incluir objetos da
	 * <code>NivelControleArtefatoVO</code> no BD. Garantindo o relacionamento com a
	 * entidade principal <code>basico.ArtefatoEntregaAlunoVO</code> através do
	 * atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public void incluirNivelControleArtefato(ArtefatoEntregaAlunoVO artefatoEntregaAluno) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

}
