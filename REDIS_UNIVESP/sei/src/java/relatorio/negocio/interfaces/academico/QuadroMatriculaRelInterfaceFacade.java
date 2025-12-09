package relatorio.negocio.interfaces.academico;


import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.academico.QuadroMatriculaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface QuadroMatriculaRelInterfaceFacade {

    public void inicializarParametros();

    public List<QuadroMatriculaRelVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UnidadeEnsinoVO unidadeEnsino, List<QuadroMatriculaRelVO> quadroMatriculaRelVOs) throws Exception;

    public List<SelectItem> consultaAnoSemestrePorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception;

    public List<SelectItem> montarDadosConsultaAnoSemestre(SqlRowSet tabelaResultado) throws Exception;

    public List<QuadroMatriculaRelVO> consultaQuadroMatriculaPeriodoPorUnidadeEnsinoAnoSemestre(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer codigoUnidadeEnsino) throws Exception;

    public List<QuadroMatriculaRelVO> montarDadosConsultaQuadroMatriculaPeriodoPorUnidadeEnsinoAnoSemestre(String ano, String semestre, SqlRowSet tabelaResultado) throws Exception;

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();
}
