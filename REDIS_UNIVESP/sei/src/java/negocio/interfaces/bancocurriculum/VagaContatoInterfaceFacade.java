package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.bancocurriculum.VagaContatoVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface VagaContatoInterfaceFacade {

	void incluirVagaContato(VagasVO vagasVO) throws Exception;
	void alterarVagaContato(VagasVO vagasVO) throws Exception;
	List<VagaContatoVO> consultarPorVaga(Integer vaga) throws Exception;
	void validarDados(VagaContatoVO vagaContatoVO) throws ConsistirException;
	
}
