package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoLogVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface PlanoFinanceiroAlunoLogInterfaceFacade {

	void realizarCriacaoLogPlanoFinanceiroAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;
	
	List<PlanoFinanceiroAlunoLogVO> consultarPorMatriculaPeriodo(Integer matriculaPeriodo) throws Exception;

	PlanoFinanceiroAlunoLogVO realizarCriacaoLogPlanoFinanceiroAlunoAtual(PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO)  throws Exception;	

}
