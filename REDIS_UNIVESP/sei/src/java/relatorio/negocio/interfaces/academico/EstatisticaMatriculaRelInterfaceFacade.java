package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import relatorio.negocio.comuns.academico.EstatisticaMatriculaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface EstatisticaMatriculaRelInterfaceFacade {

	public List<EstatisticaMatriculaRelVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<CursoVO> cursoVOs, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<TurnoVO> turnoVOs, Boolean exibirMatriculaCalouro, Boolean exibirMatriculaVeterano, Boolean exibirPreMatriculaCalouro, Boolean exibirPreMatriculaVeterano) throws Exception;

}