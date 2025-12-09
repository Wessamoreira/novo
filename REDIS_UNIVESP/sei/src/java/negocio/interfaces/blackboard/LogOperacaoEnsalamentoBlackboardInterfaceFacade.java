package negocio.interfaces.blackboard;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.LogOperacaoEnsalamentoBlackboardVO;

public interface LogOperacaoEnsalamentoBlackboardInterfaceFacade {
	
//	void incluir(LogOperacaoEnsalamentoBlackboardVO logOperacaoEnsalamentoBlackboardVO, UsuarioVO usuarioVO) throws Exception;
	
	void consultar(DataModelo dataModelo, LogOperacaoEnsalamentoBlackboardVO filtros, UsuarioVO usuarioVO) throws Exception;
	
//	LogOperacaoEnsalamentoBlackboardVO novo(SalaAulaBlackboardVO salaAulaBlackboardVO, SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, OperacaoEnsalacaoBlackboardEnum operacaoEnsalacaoBlackboardEnum, String observacao, UsuarioVO usuarioVO) throws Exception;
}
