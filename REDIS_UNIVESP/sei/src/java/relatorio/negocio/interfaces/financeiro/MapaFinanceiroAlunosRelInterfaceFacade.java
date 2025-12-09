package relatorio.negocio.interfaces.financeiro;

import java.util.List;

import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.financeiro.MapaFinanceiroAlunosRelVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface MapaFinanceiroAlunosRelInterfaceFacade {

    void validarDados(Integer unidadeEnsino, Integer curso, String tipoRelatorio, String ano, String semestre, CursoVO cursoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) throws ConsistirException;

    List<MapaFinanceiroAlunosRelVO> criarLista(Boolean trazerApenasAlunosAtivos, Integer unidadeEnsino, Integer curso, Integer turma, String ano, String semestre) throws Exception;

    String designIReportRelatorio();

    String caminhoBaseRelatorio();
    
    void calcularSomatoriosParcelas (List<MapaFinanceiroAlunosRelVO> listaMapaFinanceiroAlunosRelVOs, SuperParametroRelVO superParametroRelVO) throws Exception;

}
