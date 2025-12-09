package relatorio.negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import relatorio.negocio.comuns.processosel.CasosEspeciaisRelVO;

public interface CasosEspeciaisRelInterfaceFacade {
	
	public String getDesignIReportRelatorio();
	public String getCaminhoBaseRelatorio();
	public List<CasosEspeciaisRelVO> emitirRelatorio(ProcSeletivoVO procSeletivoVO, Integer unidadeEnsino, Integer unidadeEnsinoCurso, Boolean canhoto, Boolean gravida, Boolean necessidadeEspecial, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, Integer sala) throws Exception;
	
}
