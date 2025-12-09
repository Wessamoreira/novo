package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import relatorio.negocio.comuns.academico.AlunosMatriculadosGeralFormaIngressoRelVO;
import relatorio.negocio.comuns.academico.AlunosMatriculadosGeralRelVO;
import relatorio.negocio.comuns.academico.AlunosMatriculadosGeralSituacaoMatriculaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface AlunosMatriculadosGeralRelInterfaceFacade {

	List<AlunosMatriculadosGeralRelVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais, List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs, List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs) throws Exception;

	void validarDados(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, TurmaVO turmaVO, ProcessoMatriculaVO processoMatricula, String tipoRelatorioCalVetGer, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais) throws Exception;

	String getDesignIReportRelatorio();

	String getDesignIReportRelatorioExcel();

	String getDesignIReportRelatorioExcelSintetico();

	String getDesignIReportRelatorioSintetico();
	
	String getDesignIReportRelatorioAnaliticoPorCursoPeriodoLetivo();
	
	String getDesignIReportRelatorioSinteticoPorCursoPeriodoLetivo();

	List<AlunosMatriculadosGeralRelVO> criarObjetoSintetico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais) throws Exception;

	String getDesignIReportRelatorioAnaliticoFormaRenovacao();
	
	String getDesignIReportRelatorioExcelAnaliticoFormaRenovacao();
	
	String getDesignIReportRelatorioExcelAnaliticoPorCursoPeriodoLetivo();
	
	String getDesignIReportRelatorioExcelSinteticoPorCursoPeriodoLetivo();
	
	List<AlunosMatriculadosGeralRelVO> criarObjetoAnaliticoPorCursoPeriodoLetivo(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais, List<AlunosMatriculadosGeralFormaIngressoRelVO> listaAlunosMatriculadosGeralFormaIngressoRelVOs, List<AlunosMatriculadosGeralSituacaoMatriculaRelVO> listaAlunosMatriculadosGeralSituacaoMatriculaRelVOs) throws Exception;
	
	List<AlunosMatriculadosGeralRelVO> criarObjetoSinteticoPorCursoPeriodoLetivo(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, ProcessoMatriculaVO processoMatricula, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, String tipoRelatorioCalVetGer, String formaRenovacao, Boolean calouro, Boolean veterano, Boolean transInterna, Boolean transExterna, Boolean considerarCursosAnuaisSemestrais) throws Exception;

}