package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.AuditVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

public interface LogAuditHistoricoInterfaceFacade {

	List<String> consultarAnoAudit(UsuarioVO usuarioVO);

	List<String> consultarMesAuditPorAno(String ano, UsuarioVO usuarioVO);

	List<String> consultarColunasAuditHistorico();

	List<AuditVO> consultar(String matricula, SituacaoHistorico situacao, Integer codigo, Integer disciplina,
			String ano, String mes, String acao, String coluna, Date dataInicio, Date dataFim, UsuarioVO usuarioLogVO,
			boolean controlarAcesso,  UsuarioVO usuarioVO)
			throws Exception;

}
