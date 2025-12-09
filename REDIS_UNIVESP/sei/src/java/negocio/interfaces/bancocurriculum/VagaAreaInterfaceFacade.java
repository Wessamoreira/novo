package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.bancocurriculum.VagaAreaVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface VagaAreaInterfaceFacade {

	void incluirVagaArea(VagasVO vagasVO) throws Exception;
	void alterarVagaArea(VagasVO vagasVO) throws Exception;
	List<VagaAreaVO> consultarPorVaga(Integer vaga) throws Exception;
	void validarDados(VagaAreaVO vagaAreaVO) throws ConsistirException;
	
}
