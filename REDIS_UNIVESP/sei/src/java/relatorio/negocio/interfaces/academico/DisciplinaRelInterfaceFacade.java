package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.academico.DisciplinaRelVO;

public interface DisciplinaRelInterfaceFacade {

    List<DisciplinaRelVO> criarObjeto(String tipoRelatorio, CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception;

    SqlRowSet executarConsultaParametrizadaDisciplinaPlanejamento(CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception;

    SqlRowSet executarConsultaParametrizadaDisciplinaReferencia(CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception;

    SqlRowSet executarConsultaParametrizadaPlanejamento(CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception;

    SqlRowSet executarConsultaParametrizadaReferencia(CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception;

    void inicializarOrdenacoesRelatorio();

    String designIReportRelatorio(String tipoRelatorio);

    String designIReportRelatorioSintetico();

    String caminhoBaseRelatorio();

    void ValidarDados(String tipoRelatorio, CursoVO curso, GradeCurricularVO gradeCurricular) throws ConsistirException;
    
    public GradeCurricularVO criarObjetoRelatorioSintetico(String tipoRelatorio, CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO, UsuarioVO usuario) throws Exception;

}