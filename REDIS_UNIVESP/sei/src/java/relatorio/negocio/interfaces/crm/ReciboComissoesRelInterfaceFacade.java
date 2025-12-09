package relatorio.negocio.interfaces.crm;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.crm.ReciboComissoesRelVO;

public interface ReciboComissoesRelInterfaceFacade {

    public List<ReciboComissoesRelVO> criarObjeto(String valorConsultaMes, Integer funcionario, Integer cargo, UsuarioVO usuarioLogado) throws Exception;

    String designIReportRelatorio();

    String caminhoBaseRelatorio();

    public String designIReportRelatorioExcel();
}
