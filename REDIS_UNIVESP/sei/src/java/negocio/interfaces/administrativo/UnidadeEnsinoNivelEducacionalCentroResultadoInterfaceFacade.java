package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoNivelEducacionalCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

public interface UnidadeEnsinoNivelEducacionalCentroResultadoInterfaceFacade {

	void validarDados(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj);

	void persistir(List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

	void persistir(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO);

	void excluir(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj, boolean verificarAcesso, UsuarioVO usuario);

	boolean consultarSeExisteUnidadeEnsinoNivelEducacionalCentroResultadoPorTipoNivelEducacionalPorUnidadeEnsino(TipoNivelEducacional tipoNivelEducacional, Integer unidadeEnsino);

	List<UnidadeEnsinoNivelEducacionalCentroResultadoVO> consultaRapidaPorUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	UnidadeEnsinoNivelEducacionalCentroResultadoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	void adicionarUnidadeEnsinoNivelEducacionalCentroResultadoVO(UnidadeEnsinoVO obj, UnidadeEnsinoNivelEducacionalCentroResultadoVO unidadeEnsinoCentroResultadoVO, UsuarioVO usuario);

	void removerUnidadeEnsinoNivelEducacionalCentroResultadoVO(UnidadeEnsinoVO obj, UnidadeEnsinoNivelEducacionalCentroResultadoVO unidadeEnsinoCentroResultadoVO, UsuarioVO usuario);

	void validarDadosAntesAdicionar(UnidadeEnsinoNivelEducacionalCentroResultadoVO obj);

	UnidadeEnsinoNivelEducacionalCentroResultadoVO consultaRapidaPorUnidadeEnsinoPorTipoNivelEducacional(TipoNivelEducacional tipoNivelEducacional, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

}
