package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.DebitoDocumentosAlunoRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface DebitoDocumentosAlunoRelInterfaceFacade {

	public List<DebitoDocumentosAlunoRelVO> criarObjeto(Boolean preMatricula, DebitoDocumentosAlunoRelVO debitoDocumentoAlunoRelVO, MatriculaPeriodoVO matriculaperiodoVO, Date dataInicio, Date dataFim, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, String ano, String semestre, Boolean filtrarCursoAnual, Boolean filtrarCursoSemestral, Boolean filtrarCursoIntegral,
            Boolean documentoEntregue, Boolean documentoPendente, Boolean documentoEntregaIndeferida, Boolean documentoPendenteAprovacao, List<UnidadeEnsinoVO> lista, String tipoConsulta, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, TurmaVO turmaVO, Boolean controlaAprovacaoDocEntregue) throws Exception;
    public void setOrdenarPor(int intValue);

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();

    public Vector getOrdenacoesRelatorio();

    Map<String, List<DebitoDocumentosAlunoRelVO>> realizarGeracaoListaAlunoComDocumentacaoPendenteEnvioEmail() throws Exception;
}
