package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.academico.ControleVagaRelVO;

public interface ControleVagaRelInterfaceFacade {

    public List<ControleVagaRelVO> criarObjeto(ControleVagaRelVO controleVagaRelVO, TurmaVO turmaVO, List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs, Boolean unificarTurmas, Boolean detalharCalouroVeterano, String filtrarPor, String tipoRelatorio, String ano, String semestre) throws Exception;

    public ControleVagaRelVO montarDados(SqlRowSet dadosSQL, String tipoRelatorio) throws Exception;

    public void validarDados(TurmaVO turmaVO, List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs, ControleVagaRelVO ControleVagaRelVO, String campoFiltroPor, String periodicidade, String ano, String semestre) throws Exception;

    public String getDescricaoFiltros();

}