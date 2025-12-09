package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.financeiro.PrevisaoFaturamentoRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioContaReceberVO;

public interface PrevisaoFaturamentoRelInterfaceFacade {

	public List<PrevisaoFaturamentoRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia, Integer curso, Integer turno, Integer turma, Date dataInicio, Date dataFim, String ordenador, FiltroRelatorioAcademicoVO filtroAcademicoVO, FiltroRelatorioContaReceberVO filtroRelatorioContaReceberVO, boolean considerarUnidadeEnsinoFinanceira, UsuarioVO usuarioLogado) throws Exception;
    public void validarDados(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataFim) throws Exception;
    
}