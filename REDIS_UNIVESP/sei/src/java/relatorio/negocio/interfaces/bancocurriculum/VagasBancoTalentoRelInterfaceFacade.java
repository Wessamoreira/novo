package relatorio.negocio.interfaces.bancocurriculum;

import java.util.Date;
import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.financeiro.ParceiroVO;
import relatorio.negocio.comuns.bancocurriculum.EmpresaBancoTalentoRelVO;

public interface VagasBancoTalentoRelInterfaceFacade {

    public List<EmpresaBancoTalentoRelVO> criarObjetoPDF(List<EmpresaBancoTalentoRelVO> listaObjetos, VagasVO vagas, ParceiroVO parceiro, Date dataInicio, Date dataFim, String situacaoVaga, UsuarioVO usuarioVO, String ordenacao) throws Exception;

    public List<EmpresaBancoTalentoRelVO> criarObjetoExcel(List<EmpresaBancoTalentoRelVO> listaObjetos, VagasVO vagas, ParceiroVO parceiro, Date dataInicio, Date dataFim, String situacaoVaga, UsuarioVO usuarioVO, String ordenacao) throws Exception;

    public String designIReportRelatorio();

    public String designIReportRelatorioExcel();

    public String caminhoBaseRelatorio();

}
