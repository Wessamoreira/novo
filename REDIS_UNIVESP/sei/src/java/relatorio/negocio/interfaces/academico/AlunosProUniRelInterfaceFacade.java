package relatorio.negocio.interfaces.academico;

import java.util.List;
import negocio.comuns.academico.CursoVO;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.academico.AlunosProUniRelVO;

public interface AlunosProUniRelInterfaceFacade {

    List<AlunosProUniRelVO> consultarMatriculaPorFormaIngresso(Integer codigoUnidadeEnsino, Integer codigoUnidadeEnsinoCurso, Integer codigoTurma, String ano, String semestre) throws Exception;

    String designIReportRelatorioExcel();

    public void validarDados(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, String ano, String semestre) throws ConsistirException;

    String caminhoBaseRelatorio();

    String designIReportRelatorioPDF();

}
