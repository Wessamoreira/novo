package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.financeiro.CondicoesPagamentoRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.parametroRelatorio.academico.CondicoesPagamentoSuperParametroRelVO;

public interface CondicoesPagamentoRelInterfaceFacade {

    public void inicializarParametros();

    public List<CondicoesPagamentoRelVO> criarObjeto(Date dataInicio, Date dataFim, String ano, String semestre, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, UnidadeEnsinoCursoVO unidadeEnsinoCurso, MatriculaVO matricula, CondicoesPagamentoSuperParametroRelVO condicoesPagamentoSuperParametroRelVO, String tipoLayout, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception;

    public List<CondicoesPagamentoRelVO> criarListaPlanoDesconto(Date dataInicio, Date dataFim, String ano, String semestre, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, UnidadeEnsinoCursoVO unidadeEnsinoCurso, MatriculaVO matricula, CondicoesPagamentoSuperParametroRelVO condicoesPagamentoSuperParametroRelVO, String tipoLayout, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception;
    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();

    public String designIReportRelatorio(String tipoLayout);

    String caminhoBaseRelatorio();

    void validarDados(Date dataInicio, Date dataFim, UnidadeEnsinoVO unidadeEnsino, UnidadeEnsinoCursoVO unidadeEnsinoCurso, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String ano, String semestre) throws ConsistirException;
}
