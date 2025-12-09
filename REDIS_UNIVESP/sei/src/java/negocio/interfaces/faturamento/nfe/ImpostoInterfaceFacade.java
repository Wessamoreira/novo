package negocio.interfaces.faturamento.nfe;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;

/**
 *
 * @author Pedro
 */
public interface ImpostoInterfaceFacade {

	void persistir(ImpostoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(ImpostoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	List<ImpostoVO> consultaRapidaPorCodigo(Integer valorConsulta, DataModelo dataModelo);

	List<ImpostoVO> consultaRapidaPorNome(String valorConsulta, DataModelo dataModelo);

	ImpostoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	void consultar(DataModelo dataModelo);

	Integer consultaTotalPorNome(String valorConsulta, DataModelo dataModelo);

	Integer consultaTotalPorCodigo(Integer valorConsulta, DataModelo dataModelo);

	List<ImpostoVO> consultarImpostoComboBox(boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}
