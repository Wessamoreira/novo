package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import negocio.comuns.academico.CursoVO;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.ReposicaoRelVO;

public interface ReposicaoRelInterfaceFacade {

    public List<ReposicaoRelVO> criarObjeto(UnidadeEnsinoVO unidadeEnsinoVO, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, Date dataInicio, Date dataFim, String tipo, Integer responsavel, Date dataAulaInicio, Date dataAulaFim,  Date dataInclusaoInicio, Date dataInclusaoFim,  Date dataPgtoInicio, Date dataPgtoFim, UsuarioVO usuarioLogado) throws Exception;

    public ReposicaoRelVO getReposicaoRelVO();

    public void setReposicaoRelVO(ReposicaoRelVO reposicaoRelVO);

    public MatriculaPeriodoVO getMatriculaPeriodoVO();

    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO);

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();
}
