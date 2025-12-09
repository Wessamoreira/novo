package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.financeiro.DemonstrativoResultadoFinanceiroTurmaRelVO;

public interface DemonstrativoResultadoFinanceiroRelInterfaceFacade {
	
	public String realizarGeracaoRelatorio(List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO cursoVO, TurmaVO turmaVO, Integer mesInicio, Integer anoInicio, Integer mesTermino, Integer anoTermino, String nivelEducacional, UsuarioVO usuarioVO, boolean filtrarDataFatoGerador) throws Exception;
	
	public void criarDemonstrativoResultado(List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO cursoVO, TurmaVO turmaVO, String nivelEducacional, Date dataInicio, Date dataFinal, Boolean filtrarDataFatoGerador, DemonstrativoResultadoFinanceiroTurmaRelVO demonstrativoResultadoFinanceiroTurmaRelVO) throws Exception;

}
