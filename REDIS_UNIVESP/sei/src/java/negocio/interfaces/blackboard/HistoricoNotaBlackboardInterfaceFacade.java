package negocio.interfaces.blackboard;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.HistoricoNotaBlackboardVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.SituacaoHistoricoNotaBlackboardEnum;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface HistoricoNotaBlackboardInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {	
	
	

    

    void excluir(HistoricoNotaBlackboardVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

    void deferirIndeferirNota(HistoricoNotaBlackboardVO obj, SituacaoHistoricoNotaBlackboardEnum situacaoHistoricoNotaBlackboardEnum, boolean realizarCalculoMediaApuracaoNotas,  UsuarioVO usuarioVO) throws Exception;

    HistoricoNotaBlackboardVO consultarPorHistoricoESituacaoHistoricoNotaBlackboardEnum(HistoricoVO historicoVO);
    
    HistoricoNotaBlackboardVO consultarPorSalaAulaBlacboardNotaNaoLocalizada(Integer salaAulaBlackboardVO, String emailNaoLocalizado); 

    HistoricoNotaBlackboardVO consultarPorCodigo(Integer codigo);

	List<HistoricoNotaBlackboardVO> consultarHistoricoNotaBlackboardPorSalaAulaBlackboard(SalaAulaBlackboardVO obj, UsuarioVO usuario);

	void consultar(DataModelo controleConsulta, HistoricoNotaBlackboardVO obj, Boolean usarLimiteConsulta, UsuarioVO usuarioVO) throws Exception;

}
