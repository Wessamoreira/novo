package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoAtaResultadosFinaisVO;
import negocio.comuns.academico.ConfiguracaoHistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

public interface ConfiguracaoAtaResultadosFinaisInterfaceFacade {

	void persistir(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception;
	
	List<ConfiguracaoAtaResultadosFinaisVO> consultarConfiguracoesAtaResultadosFinais(UsuarioVO usuarioVO) throws Exception;
	
	ConfiguracaoAtaResultadosFinaisVO consultarConfiguracaoAtaResultadosFinais(UsuarioVO usuarioVO) throws Exception;
	
	void realizarInclusaoLayoutPadraoAtaResultadosFinais(String caminhoBase);
		
}
