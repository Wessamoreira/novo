package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoTipoRequerimentoCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;

public interface UnidadeEnsinoTipoRequerimentoCentroResultadoInterfaceFacade {
	
	void validarDados(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj);

	void persistir(List<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

	void persistir(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario);

	boolean consultarSeExisteUnidadeEnsinoTipoRequerimentoCentroResultadoPorTipoRequerimentoPorUnidadeEnsino(TipoRequerimentoVO tipoRequerimentoVO, Integer unidadeEnsino);

	List<UnidadeEnsinoTipoRequerimentoCentroResultadoVO> consultaRapidaPorUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	UnidadeEnsinoTipoRequerimentoCentroResultadoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	void adicionarUnidadeEnsinoTipoRequerimentoCentroResultadoVO(UnidadeEnsinoVO obj, UnidadeEnsinoTipoRequerimentoCentroResultadoVO unidadeEnsinoCentroResultadoVO, UsuarioVO usuario);

	void removerUnidadeEnsinoTipoRequerimentoCentroResultadoVO(UnidadeEnsinoVO obj, UnidadeEnsinoTipoRequerimentoCentroResultadoVO unidadeEnsinoCentroResultadoVO, UsuarioVO usuario);

	void validarDadosAntesAdicionar(UnidadeEnsinoTipoRequerimentoCentroResultadoVO obj);

}
