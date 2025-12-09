package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoGradeAnteriorAlteradaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface HistoricoGradeAnteriorAlteradaInterfaceFacade {

	public void persistir(List<HistoricoGradeAnteriorAlteradaVO> historicoGradeAnteriorAlteradaVOs, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) throws Exception;

	public List<HistoricoGradeAnteriorAlteradaVO> consultarHistoricoGradePorMatriculaGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO);

	public void adicionarHistoricoGradeAnterior(List<HistoricoGradeAnteriorAlteradaVO> listaHistoricoGradeAnteriorVOs, HistoricoGradeAnteriorAlteradaVO obj, UsuarioVO usuarioVO) throws Exception;

	public void removerHistoricoGradeAnterior(List<HistoricoGradeAnteriorAlteradaVO> listaHistoricoGradeAnteriorVOs, HistoricoGradeAnteriorAlteradaVO obj) throws Exception;
}
