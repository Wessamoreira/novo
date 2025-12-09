package relatorio.negocio.interfaces.academico;


import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoLayoutHistoricoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoDisciplinaRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface HistoricoAlunoRelInterfaceFacade {

    public HistoricoAlunoRelVO criarObjeto(HistoricoAlunoRelVO historicoAlunoRelVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO,
            int ordem, String observacaoComplementar, String observacaoComplementarIntegralizado, String campoFiltroPor, String tipoLayout, Boolean utilizarUnidadeEnsinoMatriz, Date dataExpedicaoDiploma, Boolean apresentarInstituicaoDisciplinaAproveitada,
            Boolean apresentarDisciplinaAnoSemestreTransferenciaGrade, Boolean apresentarDisciplinaForaGrade, UsuarioVO usuarioVO, Boolean filtroDisciplinasACursar, Boolean desconsiderarSituacaoMatriculaPeriodo, Boolean apresentarObservacaoTransferenciaMatrizCurricular, String observacaoTransferenciaMatrizCurricular, boolean apresentarApenasUltimoHistoricoDisciplina, boolean considerarCargaHorariaCursadaIgualCargaHorariaPrevista, boolean apresentarApenasProfessorTitulacaoTurmaOrigem, String regraApresentacaoProfessorDisciplinaAproveitamento, Boolean dataInicioTerminoAlteracoesCadastrais, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO) throws Exception;

    public CargoVO montarNomeCargoFuncionario(CargoVO cargoVO, UsuarioVO usuarioVO) throws Exception;

    public void validarDados(MatriculaVO matriculaVO, TurmaVO turmaVO, String campoFiltrarPor, String tipoDesconto, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception;

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();
    
    public String realizarCriacaoLegendaSituacaoDisciplinaHistorico(HistoricoAlunoRelVO historicoAlunoRel , String tipoLayout);

    public Double calcularMediaNotasSemestreAluno(List<HistoricoAlunoDisciplinaRelVO> historicoAlunoDisciplinaRelVOs, MatriculaPeriodoVO matriculaPeriodoVO);

	public boolean validarTipoLayoutGraduacao(String tipoLayout, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO);

	public String realizarCriacaoLegendaTitulacaoProfessorDisciplinaHistorico(HistoricoAlunoRelVO historicoTemp,String tipoLayout);

	public HistoricoAlunoRelVO criarObjeto(HistoricoAlunoRelVO historicoAlunoRelVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int ordem, String observacaoComplementar, String observacaoComplementarIntegralizado, String tipoConsulta, String tipoLayout, Boolean utilizarCidadeUnidadeMatriz, Date dataExpedicaoDiploma, Boolean apresentarInstituicaoDisciplinaAproveitada, Boolean apresentarDisciplinaAnoSemestreTransferenciaGrade, Boolean apresentarDisciplinaForaGrade, UsuarioVO usuarioVO, Boolean filtroDisciplinasACursar, Boolean desconsiderarSituacaoMatriculaPeriodo, Boolean apresentarObservacaoTransferenciaMatrizCurricular, String observacaoTransferenciaMatrizCurricular, boolean apresentarApenasUltimoHistoricoDisciplina, boolean considerarCargaHorariaCursadaIgualCargaHorariaPrevista, boolean apresentarApenasProfessorTitulacaoTurmaOrigem, String regraApresentacaoProfessorDisciplinaAproveitamento,
			Boolean dataInicioTerminoAlteracoesCadastrais, ConfiguracaoLayoutHistoricoVO configuracaoLayoutHistoricoVO, Boolean xmlDiploma) throws Exception;
}
