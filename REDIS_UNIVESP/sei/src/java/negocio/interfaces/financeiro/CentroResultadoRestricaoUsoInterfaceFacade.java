package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroResultadoRestricaoUsoVO;
import negocio.comuns.financeiro.CentroResultadoVO;

public interface CentroResultadoRestricaoUsoInterfaceFacade {

	void persistir(List<CentroResultadoRestricaoUsoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

	List<CentroResultadoRestricaoUsoVO> consultaRapidaPorCentroResultado(CentroResultadoVO obj, int nivelMontarDados, UsuarioVO usuario);

	CentroResultadoRestricaoUsoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	Boolean consultarSeExisteUsuarioEspecificoPorCentroResultado(Integer codigoCentroResultado, Integer codigoUsuarioEspecifico);

	Boolean consultarSeExistePerfilAcessoEspecificoPorCentroResultado(Integer codigoCentroResultado, Integer codigoPerfilAcessoEspecifico);

}
