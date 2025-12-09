package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.academico.FichaAlunoRelVO;

public interface FichaAtualizacaoCadastralRelInterfaceFacade {

	public List<FichaAlunoRelVO> criarObjeto(MatriculaVO matriculaVO, TurmaVO turmaVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, String filtro ,String anoConsulta , String semestreConsulta) throws Exception;

}