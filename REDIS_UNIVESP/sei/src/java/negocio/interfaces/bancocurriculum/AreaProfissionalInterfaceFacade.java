package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;


public interface AreaProfissionalInterfaceFacade {

    public AreaProfissionalVO novo() throws Exception;

    public void incluir(AreaProfissionalVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(AreaProfissionalVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(AreaProfissionalVO obj, UsuarioVO usuario) throws Exception;

    public AreaProfissionalVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDescricaoAreaProfissional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<AreaProfissionalVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List consultarPorSituacaoAreaProfissional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void inativar(final AreaProfissionalVO obj, UsuarioVO usuario) throws Exception;

    public List consultarPorDescricaoAreaProfissionalAtivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    /**
	 * @author Carlos Eugênio - 01/06/2016
	 * @param parceiro
	 * @param estagio
	 * @param usuarioVO
	 * @return
	 */
	List<AreaProfissionalVO> consultarPorCodigoEmpresaNivelComboBox(Integer parceiro, Integer estagio, UsuarioVO usuarioVO);
}
