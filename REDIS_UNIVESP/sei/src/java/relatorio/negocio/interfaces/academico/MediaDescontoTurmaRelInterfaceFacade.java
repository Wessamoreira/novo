package relatorio.negocio.interfaces.academico;

import java.util.List;
import java.util.Vector;
import negocio.comuns.arquitetura.UsuarioVO;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import relatorio.negocio.comuns.academico.MediaDescontoTurmaRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

public interface MediaDescontoTurmaRelInterfaceFacade {

    public SqlRowSet executarConsultaParametrizada(MediaDescontoTurmaRelVO mediaDescontoTurmaRelVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<String> situacoes, String tipoOrdenacao) throws Exception;

    public List<MediaDescontoTurmaRelVO> criarObjeto(MediaDescontoTurmaRelVO mediaDescontoTurmaRelVO, UsuarioVO usuarioVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<String> situacoes, String tipoOrdenacao) throws Exception;

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();

    public Vector getOrdenacoesRelatorio();
}
