package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.NumeroMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface NumeroMatriculaInterfaceFacade {

	public NumeroMatriculaVO consultarPorTodosParametros(NumeroMatriculaVO numeroMatriculaVO, String mascaraPadrao) throws Exception;
	
	public void alterarIncremental(NumeroMatriculaVO obj, Integer incremental, UsuarioVO usuario);

	/**
	 * Responsável por realizar uma consulta de <code>NumeroMatricula</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>NumeroMatriculaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados) throws Exception;
	
	String gerarNumeroMatricula(MatriculaVO matriculaVO, int anoData) throws Exception;

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>NumeroMatriculaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public NumeroMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade);

}