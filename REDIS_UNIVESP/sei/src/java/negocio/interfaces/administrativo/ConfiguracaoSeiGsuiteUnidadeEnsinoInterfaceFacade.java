package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteUnidadeEnsinoVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ConfiguracaoSeiGsuiteUnidadeEnsinoInterfaceFacade {

	void persistir(List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO);

	ConfiguracaoSeiGsuiteUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario);

	List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> consultarPorConfiguracaoSeiGsuiteVO(ConfiguracaoSeiGsuiteVO obj, int nivelMontarDados, UsuarioVO usuario);

	void validarDados(ConfiguracaoSeiGsuiteUnidadeEnsinoVO obj);

	List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> consultarPorMatriculaMaiorNivelEducacionalMaiorDataPorCodigoPessoa(Integer pessoa, UsuarioVO usuario) throws Exception;

}
