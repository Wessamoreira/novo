package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoHistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

public interface ConfiguracaoHistoricoInterfaceFacade {

	void persistir(ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception;
	
	List<ConfiguracaoHistoricoVO> consultarConfiguracoesHistorico(UsuarioVO usuarioVO) throws Exception;
	
	ConfiguracaoHistoricoVO consultarConfiguracaoHistoricoPorNivelEducacional(TipoNivelEducacional tipoNivelEducacional, UsuarioVO usuarioVO) throws Exception;

	void realizarInclusaoLayoutPadraoHistorico(TipoNivelEducacional tipoNivelEducacional, String caminhoBase);
		
}
