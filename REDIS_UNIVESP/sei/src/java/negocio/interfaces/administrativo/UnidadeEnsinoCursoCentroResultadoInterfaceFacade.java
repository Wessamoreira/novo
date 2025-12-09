package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoCursoCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface UnidadeEnsinoCursoCentroResultadoInterfaceFacade {

	void persistir(List<UnidadeEnsinoCursoCentroResultadoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(UnidadeEnsinoCursoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario);

	List<UnidadeEnsinoCursoCentroResultadoVO> consultaRapidaPorUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	UnidadeEnsinoCursoCentroResultadoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	void validarDados(UnidadeEnsinoCursoCentroResultadoVO obj);

	void persistir(UnidadeEnsinoCursoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	boolean consultarSeExisteUnidadeEnsinoCursoCentroResultadoPorCursoPorUnidadeEnsino(Integer curso, Integer unidadeEnsino);

	void validarDadosAntesAdicionar(UnidadeEnsinoCursoCentroResultadoVO obj);

	

}
