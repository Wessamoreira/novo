package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import relatorio.negocio.comuns.academico.AlunoBaixaFrequenciaRelVO;
import relatorio.negocio.jdbc.academico.FiltroAlunoBaixaFrequenciaVO;

public interface AlunoBaixaFrequenciaRelInterfaceFacade{
	
	
	public List<AlunoBaixaFrequenciaRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<CursoVO> listaCurso, TurmaVO turmaVO, Integer percentualFrequencia, FiltroAlunoBaixaFrequenciaVO filtroAlunoBaixaFrequenciaVO,Integer idPessoa, boolean isVisaoCoordenador) throws Exception ;

}
