package relatorio.negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import relatorio.negocio.comuns.processosel.AlunosMatriculadosPorProcessoSeletivoRelVO;

public interface AlunosMatriculadosPorProcessoSeletivoRelInterfaceFacade {

    public void validarDados(UnidadeEnsinoCursoVO unidadeEnsinoCurso, String tipoRelatorio) throws Exception;

    String designIReportRelatorio();

    String caminhoBaseIReportRelatorio();

    List<AlunosMatriculadosPorProcessoSeletivoRelVO> criarObjeto(String tipoRelatorio, Integer codigoProcessoSeletivo, Integer codigoCurso) throws Exception;

}