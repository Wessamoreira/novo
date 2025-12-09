package negocio.interfaces.arquitetura;

import java.util.List;

import negocio.comuns.arquitetura.Cliente;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.PermissaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoSubModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;

public interface PermissaoInterfaceFacade {

    public PermissaoVO novo() throws Exception;

    public void incluir(PermissaoVO obj, UsuarioVO usuarioVO) throws Exception;

    public void alterar(PermissaoVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(PermissaoVO obj) throws Exception;

    public List<PermissaoVO> consultarPermissaos(Integer codPerfilAcesso) throws Exception;

    public List<PermissaoVO> consultarPorNomeEntidade(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<PermissaoVO> consultarPorNomePerfilAcesso(String valorConsulta, UsuarioVO usuario) throws Exception;

    public void excluirPermissaos(Integer codPerfilAcesso, List<PermissaoVO> objetos) throws Exception;

    public void alterarPermissaos(Integer codPerfilAcesso, List<PermissaoVO> objetos, UsuarioVO usuarioVO) throws Exception;

    public void incluirPermissaos(Integer codPerfilAcessoPrm, List<PermissaoVO> objetos, UsuarioVO usuarioVO) throws Exception;

    public PermissaoVO consultarPorChavePrimaria(Integer codPerfilAcessoPrm, String nomeEntidadePrm, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String idEntidade);

    public List<PermissaoVO> consultarPermissaosComMaisDeUmPerfil(Integer usuario, Integer unidadeEnsino, Integer codPerfilAcesso) throws Exception;

	/** 
	 * @author Otimize - 12 de fev de 2016 
	 * @param perfilAcessoVO
	 * @param tipoVisaoEnum 
	 */
	void realizarGeracaoListaPermissaoPorAmbiente(PerfilAcessoVO perfilAcessoVO, TipoVisaoEnum tipoVisaoEnum, Boolean forcarAlteracaoAmbiente, Cliente cliente);

	boolean validarPermissaoModuloHabilitadoCliente(Cliente cliente, PerfilAcessoModuloEnum permissao);

	boolean validarPermissaoModuloHabilitadoCliente(Cliente cliente, PerfilAcessoPermissaoEnumInterface permissao);

	boolean validarPermissaoEstaNoModulo(Cliente cliente, PerfilAcessoPermissaoEnumInterface permissao,
			PerfilAcessoModuloEnum perfilAcessoModuloEnum);

	boolean validarPermissaoEstaNoSubModulo(Cliente cliente, PerfilAcessoPermissaoEnumInterface permissao,
			PerfilAcessoSubModuloEnum perfilAcessoSubModuloEnums);

	public List<PermissaoVO> validarPermissaoMaterial(Integer codigo) throws Exception;
}
