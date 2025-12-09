package negocio.interfaces;

import java.util.List;

import negocio.comuns.academico.TrabalhoConclusaoCursoInteracaoVO;
import negocio.comuns.academico.enumeradores.EtapaTCCEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface TrabalhoConclusaoCursoInteracaoInterfaceFacade {

	void incluir(final TrabalhoConclusaoCursoInteracaoVO trabalhoConclusaoCursoInteracaoVO, UsuarioVO usuarioVO) throws Exception;
	
	void excluir(TrabalhoConclusaoCursoInteracaoVO trabalhoConclusaoCursoInteracaoVO, UsuarioVO usuarioVO) throws Exception;
	
	void validarDados(TrabalhoConclusaoCursoInteracaoVO trabalhoConclusaoCursoInteracaoVO) throws ConsistirException;
	
	List<TrabalhoConclusaoCursoInteracaoVO> consultarPorTCCEtapa(int tcc, EtapaTCCEnum etapaTCCEnum, Integer limite, Integer offset) throws Exception;
	
	Integer consultarTotalRegistroPorTCCEtapa(int tcc, EtapaTCCEnum etapaTCCEnum) throws Exception;
	
}
