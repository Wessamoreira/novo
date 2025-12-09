package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.HistoricoNotaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;

public interface HistoricoNotaInterfaceFacade {
	
	public void alterar(final HistoricoNotaVO historicoNotaVO) throws Exception;
	
	public void incluirHistoricoNotaVOs(HistoricoVO historicoVO) throws Exception;				
	
	public List<HistoricoNotaVO> consultarHistoricoNotaVOs(Integer historico) throws Exception;
	
	public HistoricoNotaVO consultarHistoricoNotaPorHistoricoTipoNota(Integer historico, TipoNotaConceitoEnum tipoNota) throws Exception;
	
	public HistoricoNotaVO consultarPorChavePrimaria(Integer historicoNota) throws Exception;

	Integer consultarQtdeDisciplinaEmRecuperacaoPorMatriculaAnoSemestreTipoNota(String matricula, String ano, String semestre, TipoNotaConceitoEnum tipoNota);
	
}
