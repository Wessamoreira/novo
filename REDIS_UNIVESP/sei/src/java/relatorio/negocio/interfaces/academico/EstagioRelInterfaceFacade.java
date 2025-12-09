package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.financeiro.ParceiroVO;
import relatorio.negocio.comuns.academico.EstagioRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface EstagioRelInterfaceFacade {

	List<EstagioRelVO> consultarListaEstagioSintetico(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, List<DisciplinaVO> disciplinaVOs, ParceiroVO empresaVO, List<AreaProfissionalVO> areaProfissionalVOs, MatriculaVO matricula, String situacao, TipoEstagioEnum tipoEstagio, String periodicidade, String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean trazerTodosEstagiosAluno, String tipoLayout, Integer periodoLetivoDe, Integer periodoLetivoAte, UsuarioVO usuarioLogado,Date dataInicio,Date dataFim) throws Exception;
	List<EstagioRelVO> consultarListaEstagioAnalitico(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, List<DisciplinaVO> disciplinaVOs, ParceiroVO empresaVO, List<AreaProfissionalVO> areaProfissionalVOs, MatriculaVO matricula, String situacao, TipoEstagioEnum tipoEstagio, String periodicidade, String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean apresentarNotaDisciplina, Boolean trazerTodosEstagiosAluno, String tipoLayout, Integer periodoLetivoDe, Integer periodoLetivoAte, String estagioRegistradoSeguradora, Boolean filtrarEstagioRegitradoSeguradora, Date dataInicioEnvioSeguradora, Date dataTerminoEnvioSeguradora, UsuarioVO usuarioLogado,Date dataInicio,Date dataFim) throws Exception;

}