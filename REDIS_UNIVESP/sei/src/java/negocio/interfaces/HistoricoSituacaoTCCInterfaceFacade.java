package negocio.interfaces;

import java.util.List;

import negocio.comuns.academico.HistoricoSituacaoTCCVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface HistoricoSituacaoTCCInterfaceFacade {

	void incluir(final HistoricoSituacaoTCCVO historicoSituacaoTCCVO) throws Exception;
	
	void realizarCriacaoHistoricoSituacaoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, String tipoUsuario, UsuarioVO usuarioVO) throws Exception;
	
	List<HistoricoSituacaoTCCVO> consultarPorTCC(Integer tcc, Integer limit, Integer offset) throws Exception;
	
	Integer consultarTotalRegistroPorTCC(Integer tcc) throws Exception;
	
	
}
