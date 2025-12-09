package relatorio.negocio.interfaces.academico;

import java.util.List;
import negocio.comuns.academico.TurmaVO;
import relatorio.negocio.comuns.academico.RelacaoEnderecoAlunoRelVO;

public interface RelacaoEnderecoAlunoRelInterfaceFacade {

    public void validarDados(TurmaVO turma, String ano, String semestre) throws Exception;

    public List<RelacaoEnderecoAlunoRelVO> criarObjeto(TurmaVO turma, String ano, String semestre) throws Exception;

    public List<RelacaoEnderecoAlunoRelVO> criarObjetoLivroRegistro(TurmaVO turma, String ano, String semestre, Integer codigoUnidadeEnsino) throws Exception;
}
