package relatorio.negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.processosel.AlunosProcessoSeletivoRelVO;

/**
 * 
 * @author Carlos
 */
public interface AlunosProcessoSeletivoRelInterfaceFacade {

    public List realizarMontagemListaSelectItemProcessoSeletivo(Integer unidadeEnsinoLogado) throws Exception;

    public List realizarMontagemListaSelectItemUnidadeEnsino(Integer procSeletivo) throws Exception;

    public List realizarMontagemListaSelectItemCurso(Integer unidadeEnsino, Integer procSeletivo) throws Exception;

    public void inicializarDadosImpressaoPDF(AlunosProcessoSeletivoRelVO obj, Integer procSeletivo, Integer unidadeEnsino, Integer curso) throws Exception;

    List<AlunosProcessoSeletivoRelVO> realizarCriacaoObjeto(Integer procSeletivo, Integer unidadeEnsino, Integer curso, Date dataInicio, Date dataFim, String situacaoAluno, int nivelMontarDados) throws Exception;

    String designIReportRelatorio();

    String caminhoBaseRelatorio();

    void validarDados(UnidadeEnsinoVO unidadeEnsino, ProcSeletivoVO procSeletivoVO, AlunosProcessoSeletivoRelVO alunosProcessoSeletivoRelVO, Date dataInicio, Date dataFim) throws ConsistirException;
}
