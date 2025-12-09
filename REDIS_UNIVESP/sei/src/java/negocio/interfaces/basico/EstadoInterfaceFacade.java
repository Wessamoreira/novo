package negocio.interfaces.basico;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.EstadoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface EstadoInterfaceFacade {

    public EstadoVO novo() throws Exception;
    public void incluir(EstadoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void alterar(EstadoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(EstadoVO obj, UsuarioVO usuarioVO) throws Exception;
    public EstadoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List<EstadoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List<EstadoVO> consultarPorSigla(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List<EstadoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List<EstadoVO> consultarPorNomePaiz(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario ) throws Exception;
    public List<EstadoVO> consultarPorCodigoPaiz(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public EstadoVO consultarPorCodigoCidade(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
	void persistir(EstadoVO estadoVO, UsuarioVO usuarioVO) throws Exception;
	public EstadoVO consultarPorSigla(String valorConsulta, UsuarioVO usuario) throws Exception ;
	public List<EstadoVO> consultarEstadosMinimo() throws Exception;
	EstadoVO consultarPorChavePrimariaUnico(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;
}