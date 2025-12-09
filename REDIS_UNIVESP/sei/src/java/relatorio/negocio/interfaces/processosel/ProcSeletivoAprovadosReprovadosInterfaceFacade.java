package relatorio.negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.processosel.ProcSeletivoAprovadoReprovadoRelVO;

public interface ProcSeletivoAprovadosReprovadosInterfaceFacade {

    public List<ProcSeletivoAprovadoReprovadoRelVO> executarGeracaoListaRelatorio(String situacao, 
            UnidadeEnsinoVO unidadeEnsinoVO, 
            UnidadeEnsinoCursoVO unidadeEnsinoCursoVO,
            Date dataProvaInicio,
            Date dataProvaFim,UsuarioVO usuarioVO) throws Exception;

    public String getDesignIReportRelatorio() throws Exception;

    public String getCaminhoBaseRelatorio() throws Exception;
}
