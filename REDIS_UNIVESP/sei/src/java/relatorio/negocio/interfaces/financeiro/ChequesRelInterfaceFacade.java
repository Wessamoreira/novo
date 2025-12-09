package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

import relatorio.negocio.comuns.financeiro.ChequeRelVO;

public interface ChequesRelInterfaceFacade {

    List<ChequeRelVO> criarObjeto(UnidadeEnsinoVO unidadeEnsino, String tipoSituacao, String tipoFiltro, String ordenarPor, Date dataInicio, Date dataFim, Date dataInicioPrevisao, Date dataFimPrevisao, UsuarioVO usuarioVO) throws Exception;

    public String designIReportRelatorio();

    public String caminhoBaseRelatorio();

    public Vector getOrdenacoesRelatorio();
}
