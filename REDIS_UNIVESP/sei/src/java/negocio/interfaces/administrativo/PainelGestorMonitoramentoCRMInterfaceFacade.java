package negocio.interfaces.administrativo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoCRMVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoDetalheCRMVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.PainelGestorTipoMonitoramentoCRMEnum;
import negocio.comuns.crm.ConsultorPorMatriculaRelVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoFiltroMonitamentoCrmProspectEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface PainelGestorMonitoramentoCRMInterfaceFacade {
    
    void consultarDadosIniciaisPainelGestorMonitoramentoCRM(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino,  PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, String situacaoProspect) throws Exception;
    
    void consultarDadosDetalhePainelGestorMonitoramentoCRMPorConsultor(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO) throws Exception;
    
    void consultarDadosDetalhePainelGestorMonitoramentoCRMPorCurso(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO) throws Exception;
    
    void consultarDadosProspectDetalhePainelGestorMonitoramentoCRM(List<UnidadeEnsinoVO> unidadeEnsinoVOs, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO) throws Exception;

    void realizarAtualizacaorDadosDetalhePainelGestorMonitoramentoCRM(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO) throws Exception;

	void consultarMonitoramentoProspectPorConsultor(Date dateInicio, Date dataTermino,  Integer unidadeEspecifica, Integer consultor, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception;

	void consultarMonitoramentoProspectPorUnidade(Date dateInicio, Date dataTermino,  Integer unidadeEspecifica, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception;

	void consultarDadosIniciaisPainelGestorMonitoramentoProspectCRM(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino,  PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO) throws Exception;

	List<ProspectsVO> consultarDadosProspectComoFicouSabendoDaInstituicao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, String situacaoProspect, Integer tipoMidiaCaptacao, Integer limit, Integer offset) throws Exception;

	void realizarGeracaoGraficoMonitoramentoProspectComoFicouSabendoInstituicao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, PainelGestorMonitoramentoCRMVO painelGestorMonitoramentoCRMVO, String situacaoProspect) throws Exception;

	Integer consultarTotalRegistroProspectComoFicouSabendoDaInstituicao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dateInicio, Date dataTermino, String situacaoProspect, Integer tipoMidiaCaptacao) throws Exception;
    
	public Map<String, Object> consultarDadosPainelGestorMonitoramentoConsultorPorMatricula(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs,Date dataInicio, Date dataTermino, List<TurmaVO> turmaVOs, FuncionarioVO consultor, 
			boolean matRecebida, boolean matAReceber, boolean matVencida, boolean matAVencer, boolean excluida, boolean transferidaDe, boolean transferidaPara,
			String situacaoProspect, boolean considerarConsultorVinculadoProspect, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String situacaoAlunoCurso, List<ConsultorPorMatriculaRelVO> listaRelatorioAnalitico, Date dataInicioPagamentoMatricula, Date dataFimPagamentoMatricula, Boolean considerarApenasTurmaComAlunosMatriculados, Boolean trazerAlunosSemTutor, boolean matNaoGerada, Boolean trazerAlunosBolsistas) throws Exception;

	List<ProspectsVO> consultarDadosProspectMonitoramento(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer curso, Integer consultor, Integer unidadeEspecifica, TipoFiltroMonitamentoCrmProspectEnum tipoFiltroMonitamentoCrmProspectEnum, MesAnoEnum mesAno, Integer ano, Integer periodoUltimoContato1, Integer periodoUltimoContato2, Integer periodoUltimoContato3, PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum, Boolean matriculado, Integer limit, Integer offset, Integer codigoSegmentacaoOpcao) throws Exception;
	
	
	/**
	 * Consulta os dados dos Prospects que nao estao sincronizados com o RD Station
	 * (ProspectVO.sincronizadoRDStation = false)
	 * 
	 * @param unidadeEnsinoVOs
	 * @param dataInicio
	 * @param dataTermino
	 * @param curso
	 * @param consultor
	 * @param unidadeEspecifica
	 * @param tipoFiltroMonitamentoCrmProspectEnum
	 * @param mesAno
	 * @param ano
	 * @param periodoUltimoContato1
	 * @param periodoUltimoContato2
	 * @param periodoUltimoContato3
	 * @param painelGestorTipoMonitoramentoCRMEnum
	 * @param matriculado
	 * @param limit
	 * @param offset
	 * @param codigoSegmentacaoOpcao
	 * @return
	 * @throws Exception
	 */
	List<ProspectsVO> consultarDadosProspectNaoSincronizadosComRdStation(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer curso, Integer consultor, Integer unidadeEspecifica, TipoFiltroMonitamentoCrmProspectEnum tipoFiltroMonitamentoCrmProspectEnum, MesAnoEnum mesAno, Integer ano, Integer periodoUltimoContato1, Integer periodoUltimoContato2, Integer periodoUltimoContato3, PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum, Boolean matriculado, Integer limit, Integer offset, Integer codigoSegmentacaoOpcao) throws Exception;

	Integer consultarTotalDadosProspectMonitoramento(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer curso, Integer consultor, Integer unidadeEspecifica, TipoFiltroMonitamentoCrmProspectEnum tipoFiltroMonitamentoCrmProspectEnum, MesAnoEnum mesAno, Integer ano, Integer periodoUltimoContato1, Integer periodoUltimoContato2, Integer periodoUltimoContato3, PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum, Boolean matriculado, Integer codigoSegmentacaoOpcao) throws Exception;
	
	String designRelatorio();

	String designRelatorioAnalitico();

	String caminhoSubReport();

	String designRelatorioExcel();

	String designRelatorioExcelAnalitico();
}
