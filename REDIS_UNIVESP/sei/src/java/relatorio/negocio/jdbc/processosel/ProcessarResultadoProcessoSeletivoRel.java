package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.processosel.InscricaoRespostaNaoProcessadaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.ProcessarResultadoProcessoSeletivoRelVO;
import relatorio.negocio.interfaces.processosel.ProcessarResultadoProcessoSeletivoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ProcessarResultadoProcessoSeletivoRel extends SuperRelatorio implements ProcessarResultadoProcessoSeletivoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	public List<ProcessarResultadoProcessoSeletivoRelVO> criarObjeto(List<InscricaoRespostaNaoProcessadaVO> inscricaoRespostaNaoProcessadaVOs, List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs) throws Exception {
		List<ProcessarResultadoProcessoSeletivoRelVO> processarResultadoProcessoSeletivoRelVOs = new ArrayList<ProcessarResultadoProcessoSeletivoRelVO>();
		ProcessarResultadoProcessoSeletivoRelVO processarResultadoProcessoSeletivoRelVO = new ProcessarResultadoProcessoSeletivoRelVO();
		processarResultadoProcessoSeletivoRelVO.setInscricaoRespostaNaoProcessadaVOs(inscricaoRespostaNaoProcessadaVOs);
		processarResultadoProcessoSeletivoRelVO.setResultadoProcessoSeletivoVOs(resultadoProcessoSeletivoVOs);
		processarResultadoProcessoSeletivoRelVOs.add(processarResultadoProcessoSeletivoRelVO);
		return processarResultadoProcessoSeletivoRelVOs;
	}
	
	public String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + getIdEntidade() + ".jrxml");
	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
	}

	public static String getIdEntidade() {
		return ("ProcessarResultadoProcessoSeletivoRel");
	}
	
}
