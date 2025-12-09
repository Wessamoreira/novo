package relatorio.negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.processosel.InscricaoRespostaNaoProcessadaVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.ProcessarResultadoProcessoSeletivoRelVO;

public interface ProcessarResultadoProcessoSeletivoRelInterfaceFacade {

	public List<ProcessarResultadoProcessoSeletivoRelVO> criarObjeto(List<InscricaoRespostaNaoProcessadaVO> inscricaoRespostaNaoProcessadaVOs, List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs) throws Exception;
	public String getDesignIReportRelatorio();
	public String getCaminhoBaseRelatorio();
}
