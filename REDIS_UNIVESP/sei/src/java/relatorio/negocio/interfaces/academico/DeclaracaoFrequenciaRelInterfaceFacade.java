package relatorio.negocio.interfaces.academico;

import java.util.Date;
import negocio.comuns.academico.DisciplinaVO;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.DeclaracaoFrequenciaVO;

public interface DeclaracaoFrequenciaRelInterfaceFacade {

	public DeclaracaoFrequenciaVO consultarPorCodigoAluno(DeclaracaoFrequenciaVO obj, String ano, String semestre, Date dataDeclaracao, int disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public DeclaracaoFrequenciaVO montarDados(MatriculaPeriodoVO matPeriodo, Date dataDeclaracao, DeclaracaoFrequenciaVO obj, int disciplina) throws Exception;

}