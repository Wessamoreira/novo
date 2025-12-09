package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaCamposAlteradosVO;
import negocio.comuns.academico.enumeradores.OpcaoTabelaLogMatriculaEnum;
import negocio.comuns.administrativo.AuditVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;

public interface LogMatriculaInterfaceFacade {

	List<String> consultarAnoAudit(UsuarioVO usuarioVO);

	List<String> consultarMesAuditPorAno(String ano, UsuarioVO usuarioVO);

	List<AuditVO> consultar(String matricula, SituacaoVinculoMatricula situacao,
			OpcaoTabelaLogMatriculaEnum opcaoTabelaLogMatriculaEnum, String ano, String mes, String acao, String coluna,
			Date dataInicio, Date dataFim, UsuarioVO usuarioLogVO, boolean controlarAcesso,
			MatriculaCamposAlteradosVO matriculaCamposAlteradosVO, Integer turma, String anoMatriculaPeriodo,
			String semestreMatriculaPeriodo, Integer codigoTransacao, UsuarioVO usuarioVO) throws Exception;

	List<String> consultarColunasAuditMatricula(OpcaoTabelaLogMatriculaEnum opcaoTabelaLogMatriculaEnum);
	

}
