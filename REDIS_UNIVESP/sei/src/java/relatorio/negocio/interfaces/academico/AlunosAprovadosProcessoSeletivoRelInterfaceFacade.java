/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import relatorio.negocio.comuns.academico.AlunosAprovadosProcessoSeletivoRelVO;

/**
 *
 * @author Carlos
 */
public interface AlunosAprovadosProcessoSeletivoRelInterfaceFacade {
    public List realizarMontagemListaSelectItemProcessoSeletivo(Integer unidadeEnsinoLogado) throws Exception;
    public List realizarMontagemListaSelectItemUnidadeEnsino(Integer procSeletivo) throws Exception;
    public List realizarMontagemListaSelectItemCurso(Integer unidadeEnsino, Integer procSeletivo) throws Exception;
    public List<AlunosAprovadosProcessoSeletivoRelVO> realizarCriacaoObjeto(Integer procSeletivo, Integer unidadeEnsino, Integer curso, Date dataInicio, Date dataFim, int nivelMontarDados) throws Exception;
    public void inicializarDadosImpressaoPDF(AlunosAprovadosProcessoSeletivoRelVO obj, Integer procSeletivo, Integer unidadeEnsino, Integer curso) throws Exception;
}
