/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoResultadoProcessoSeletivoEnum;
import relatorio.negocio.comuns.processosel.ResultadoProcessoSeletivoRelVO;
import negocio.comuns.processosel.InscricaoVO;
/**
 *
 * @author Philippe
 */
public interface ResultadoProcessoSeletivoRelInterfaceFacade {

	public List<ResultadoProcessoSeletivoRelVO> criarObjeto(List<ProcSeletivoVO> listaProcSeletivoVOs, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, List<CursoVO> listaCursoVOs, List<TurnoVO> listaTurnoVOs, Boolean apresentarNota, Boolean apresentarNotaPorDisciplina, String ordenacao, SituacaoResultadoProcessoSeletivoEnum situacaoResultadoProcessoSeletivoEnum, Date dataProvaInicio, Date dataProvaFim, String ano, String semestre, Boolean apresentarQuantidadeAcerto, Boolean apresentarSituacaoResultadoProcessoSeletivo, InscricaoVO inscricao, UsuarioVO usuarioVO) throws Exception;

    public String caminhoBaseRelatorio();

    public String designIReportRelatorio();

    public String designIReportRelatorioExcel();
    
    public String designIReportRelatorioFicha();    
	
}
