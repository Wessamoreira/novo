package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.academico.ConfiguracaoTCCArtefatoVO;
import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface ConfiguracaoTCCArtefatoInterfaceFacade {
	
	void incluirConfiguracaoTCCArtefatoVOs(ConfiguracaoTCCVO configuracaoTCCVO) throws Exception;
	
	void alterarConfiguracaoTCCArtefatoVOs(ConfiguracaoTCCVO configuracaoTCCVO) throws Exception;
	
	void excluirConfiguracaoTCCArtefatoVOs(ConfiguracaoTCCVO configuracaoTCCVO) throws Exception;
	
	List<ConfiguracaoTCCArtefatoVO> consultarPorConfiguracaoTCC(Integer configuracaoTCC) throws Exception;
	
	void validarDados(ConfiguracaoTCCArtefatoVO configuracaoTCCArtefatoVO) throws ConsistirException;

}
