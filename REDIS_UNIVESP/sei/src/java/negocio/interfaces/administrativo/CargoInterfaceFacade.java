package negocio.interfaces.administrativo;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CargoInterfaceFacade {
	

    public CargoVO novo() throws Exception;
    public void incluir(CargoVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(CargoVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(CargoVO obj, UsuarioVO usuarioVO) throws Exception;
    public CargoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<CargoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<CargoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<CargoVO> consultarPorCbo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<CargoVO> consultarPorNomeDuploPercent(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<CargoVO> consultarPorDepartamento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public List<CargoVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<CargoVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CargoVO> consultarPorDepartamentoENome(String nome, DepartamentoVO departamento, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
	
    public void consultarPorEnumCampoConsulta(DataModelo controleConsultaOtimizado) throws Exception;
	public List<CargoVO> consultarCargoPorFuncionario(Integer codigoFuncionario, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}