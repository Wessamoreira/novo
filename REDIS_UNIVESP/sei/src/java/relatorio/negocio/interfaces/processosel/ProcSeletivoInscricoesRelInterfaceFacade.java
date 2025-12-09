package relatorio.negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.academico.ItemDisciplinaAntigaDisciplinaNovaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscricoesRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO;

public interface ProcSeletivoInscricoesRelInterfaceFacade {

	public void montarDadosProcessoSeletivo(ProcessoSeletivoInscricoesRelVO processoSeletivoInscricoesRelVO, Integer codigoProcessoSeletivo) throws Exception;

	/**
	 * Metodo responsável por emitir o relatório
	 * @return
	 * @throws java.lang.Exception
	 */
	public List<ProcessoSeletivoInscricoesRelVO> emitirRelatorio(ProcSeletivoVO procSeletivoVO, Integer unidadeEnsino, Integer curso, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, Integer sala, String situacao, SituacaoInscricaoEnum situacaoInscricao, Boolean filtrarSomenteInscricoesIsentas, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) throws Exception;

	public void validarDados(ProcSeletivoVO obj) throws ConsistirException;

	public String getDesignIReportRelatorio();
        
        public String getDesignIReportRelatorioSintetico();

	public String getCaminhoBaseRelatorio();

	public void setIdEntidade(String idEntidade);
        
        public List consultarCurso(String campoConsultaCurso, String valorConsultaCurso, Integer unidadeEnsino, Integer procSeletivo, UsuarioVO usuarioVO) throws Exception;

        public String getDesignIReportRelatorioQuantitativo();

        public List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> emitirRelatorioSintetico(ProcSeletivoVO procSeletivoVO, Integer unidadeEnsino, Integer sala, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO) throws Exception;
		
        public List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> emitirRelatorioSintetico(ProcSeletivoVO procSeletivoVO, Integer unidadeEnsino, Integer sala, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, String situacao, SituacaoInscricaoEnum situacaoInscricao, Boolean filtrarSomenteInscricoesIsentas, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) throws Exception;
		
        public List<ProcessoSeletivoInscricoesRelVO> emitirRelatorioQuantitativo(ProcSeletivoVO procSeletivoVO, Integer unidadeEnsino, Integer unidadeEnsinoCurso, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, Integer sala, String situacao, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO, Boolean filtrarSomenteInscricoesIsentas) throws Exception;
        
}