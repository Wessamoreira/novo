package negocio.interfaces.planoorcamentario;

import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.planoorcamentario.UnidadesPlanoOrcamentarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface UnidadesPlanoOrcamentarioInterfaceFacade {

	public UnidadesPlanoOrcamentarioVO novo() throws Exception;

	public void incluir(final UnidadesPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(final UnidadesPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(UnidadesPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

        public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

        public void excluirUnidadesPlanoOrcamentarios(Integer planoOrcamentario, UsuarioVO usuario) throws Exception;

        public void alterarUnidadesPlanoOrcamentarios(Integer planoOrcamentario, List objetos, UsuarioVO usuario) throws Exception;

        public void incluirUnidadesPlanoOrcamentarios(Integer planoOrcamentarioPrm, List objetos, UsuarioVO usuario) throws Exception;

        public UnidadesPlanoOrcamentarioVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;
        
	public void setIdEntidade(String aIdEntidade);

        public List<UnidadesPlanoOrcamentarioVO> consultaRapidaPorPlanoOrcamentario(Integer planoOrcamentario, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

        public Boolean consultarPorUnidadeEnsinoPlanoOrcamentario(Integer planoOrcamentario, Integer unidadeEnsino);
}
