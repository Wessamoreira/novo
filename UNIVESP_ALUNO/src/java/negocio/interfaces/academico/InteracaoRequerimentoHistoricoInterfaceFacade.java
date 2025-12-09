package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.InteracaoRequerimentoHistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface InteracaoRequerimentoHistoricoInterfaceFacade {

	void persistir(InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistoricoVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	 List<InteracaoRequerimentoHistoricoVO> consultarPorRequerimentoHistorico(Integer requerimentoHistorico, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Integer consultarTotalRegistroPorRequerimentoHistorico(Integer requerimentoHistorico) throws Exception;
	
	public void alterarVisualizacaoInteracaoHistrico(RequerimentoVO requerimento, UsuarioVO usuarioVO) throws Exception;
    
}
