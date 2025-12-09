package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.CampanhaMarketingMidiaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface CampanhaMarketingMidiaInterfaceFacade {

	public CampanhaMarketingMidiaVO novo() throws Exception;

	public void incluir(CampanhaMarketingMidiaVO obj) throws Exception;

	public void alterar(CampanhaMarketingMidiaVO obj) throws Exception;

	public void excluirMidia(Integer campanhaMarketing) throws Exception;

	/**
	 * Operação responsável por alterar todos os objetos da <code>UnidadeEnsinoCursoVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirUnidadeEnsinoCursos</code> e <code>incluirUnidadeEnsinoCursos</code> disponíveis na classe <code>UnidadeEnsinoCurso</code>.
	 * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarMidia(Integer campanhaMarketing, List objetos) throws Exception;

	/**
	 * Operação responsável por incluir objetos da <code>UnidadeEnsinoCursoVO</code> no BD.
	 * Garantindo o relacionamento com a entidade principal <code>administrativo.UnidadeEnsino</code> através do atributo de vínculo.
	 * @param objetos List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void incluirMidia(Integer campanhaMarketing, List objetos) throws Exception;

	public CampanhaMarketingMidiaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;

	public List consultarMidias(Integer codigo) throws Exception;

}