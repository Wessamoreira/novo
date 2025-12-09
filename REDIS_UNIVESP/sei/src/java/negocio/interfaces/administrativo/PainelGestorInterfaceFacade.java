/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.administrativo;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.PainelGestorContaReceberMesAnoVO;
import negocio.comuns.administrativo.PainelGestorVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.planoorcamentario.DetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

/**
 *
 * @author Otimize-Not
 */
public interface PainelGestorInterfaceFacade {

    /**Este método tem a função de incializar os dados da pagina inicial do painel gestor financeiro
     * Os dados inicializado são:
     *  1 - Gráfico de RECEITAS X DESPESAS
     *  2 - Mapa de Receitas
     * @param painelGestorVO - Objeto que irá arrmazenar as informações gerais do painel gestor
     * @param unidadeEnsinoVOs - Lista com as unidades de ensino que será utilizado no filtro de busca
     * @param dataInicio - Data inicial da busca das informações
     * @param dataTermino - Data final da busca das informações
     * @throws Exception
     */
    public void executarInicializacaoDadoFinanceiroPainelGestor(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, String filtrarPeriodoPor) throws Exception;

    public List<TipoNivelEducacional> consultarNivelEducacionalPorUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception;

    public void executarCriacaoGraficoLinhaRecebimentoPagamento(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Boolean mesmoMesAno) throws Exception;

    public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicao(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception;

    /**Este método tem a função de incializar os dados do painel gestor financeiro por nivel educacional
     *
     * @param painelGestorVO - Objeto que irá arrmazenar as informações gerais do painel gestor
     * @param unidadeEnsinoVOs - Lista com as unidades de ensino que será utilizado no filtro de busca
     * @param dataInicio - Data inicial da busca das informações
     * @param dataTermino - Data final da busca das informações
     * @param tipoNivelEducacional - Filtro Por Nivel Educacional
     * @param mesAco - Define também o período da busca de vazio irá utilizar a dataInicio e dataTermino;
     * @throws Exception
     */
    public void executarInicializacaoDadosMapaReceitaPorNivelEducacional(PainelGestorVO painelGestorVO, String tipoMapaReceita, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, String filtrarPeriodoPor) throws Exception;

    public void consultarDadosGraficoPainelGestorFinanceiroAcademico(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception;
    
    public void consultarDadosGraficoPainelGestorFinanceiroAcademicoNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, UsuarioVO usuarioVO) throws Exception;

    /**Realiza a consulta dos dados necessário para a geração do Painel Gestor Financeiro Acadêmico trazendo separado por mês e nivel educacional os seguintes dados:
     * Mês e Ano;
     * Nível Educacional;
     * Total de Alunos Ativos de cada Nivel/Mês;
     * Total de Alunos Novos de cada Nivel/Mês;
     * Total de Alunos Renovados de cada Nivel/Mês;
     * Total de Alunos Cancelados de cada Nivel/Mês;
     * Total de Alunos Trancados de cada Nivel/Mês;
     * Total de Alunos Trânsferidos de cada Nivel/Mês;
     * Total de Receita em cada Nivel/Mês;
     * Total de Despesa em cada Nivel/Mês;
     * Media de Receita em cada Nivel/Mês;
     * Media de Despesa em cada Nivel/Mês;
     *
     * @param painelGestorVO
     * @param unidadeEnsinoVOs
     * @param dataInicio
     * @param dataTermino
     * @throws Exception
     */
    public void consultarDadosPainelGestorFinanceiroAcademico(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception;

    public void consultarDadosPainelGestorMonitoramentoDesconto(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception;

    /**
     * Responsável em consultar os descontos dados aos curso de um determinado Nivel Educacional
     * @param painelGestorVO
     * @param unidadeEnsinoVOs
     * @param dataInicio
     * @param dataTermino
     * @param tipoNivelEducacional
     * @param mesAno
     * @throws Exception
     */
    public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception;

    public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenio(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception;

    public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivo(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception;

    public void executarInicializacaoDadosMapaReceitaPorTurma(PainelGestorVO painelGestorVO, String tipoMapaReceita, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer codigoCurso, String filtrarPeriodoPor) throws Exception;

    public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicaoTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TurmaVO turmaVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception;

    public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivoTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TurmaVO turmaVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception;

    public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenioTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TurmaVO turmaVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception;

    public SqlRowSet consultarRecebimentoPagamentoMesmoMesAno(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception;

    

    public void executarInicializacaoDadosPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentarioVO, UsuarioVO usuario) throws Exception;

    public void executarMontagemGraficoDetalhamentoPorMes(DetalhamentoPlanoOrcamentarioVO detalhamentoPlanoOrcamentarioVO);

    public void executarInicializacaoDadosMapaPainelGestorAcademicoXFinanceiroPorNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;	

	void executarInicializacaoDadosMapaPainelGestorAcademicoXFinanceiroPorTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, Integer codigoCurso, String mesAno) throws Exception;

	void executarInicializacaoDadosPainelGestorMonitoramentoDescontoPorTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, Integer codigoCurso) throws Exception;

	void executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivoPorTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codigoTurma, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception;

    void executarCriacaoGraficoCategoriaDespesa(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String identificadorCategoriaPrincipal, Integer departamento, Boolean telaInicial, Integer limite, Integer offset) throws Exception;

	Integer consultarQtdeAlunoAtivoAtualmente(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception;

	String consultarMatriculasSeremDesconsideradasMesAtual(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception;
    
	public void consultarDadosPainelGestorMonitoramentoConsultor(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codCurso, Integer codTurma) throws Exception;    

	public void consultarDadosPainelGestorMonitoramentoConsultorMatRec(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception;
	
	public void consultarDadosPainelGestorMonitoramentoConsultorMatARec(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception;
	
	public void consultarDadosPainelGestorMonitoramentoConsultorMatVencida(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception;
	
	public void consultarDadosPainelGestorMonitoramentoConsultorMatAVencer(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception;
	
	public void consultarDadosPainelGestorMonitoramentoConsultorPreMat(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception;
	
	public void consultarDadosPainelGestorMonitoramentoConsultorMatAT(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception;
	
	public void consultarDadosPainelGestorMonitoramentoConsultorMatCanc(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception;
	
	public void consultarDadosPainelGestorMonitoramentoConsultorMatExt(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception;
	
	public void consultarDadosPainelGestorMonitoramentoConsultorMatPendDoc(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception;
	
	public void executarDetalhamentoReceitaDoMesPorMesAnoETipoOrigemSituacaoTipoMapareceitaTipoDesconto(PainelGestorVO painelGestorVO, PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO, String tipoOrigem, String situacao, String tipoMapaReceita, String tipoDesconto, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer codigoCurso, Integer codigoTurma, String filtarPeriodoPor) throws Exception;

	void executarCriacaoGraficoConsumo(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino,Boolean telaInicial) throws Exception;

	SqlRowSet consultarGraficoCategoriaDespesaPainelGestorFinanceiroPorDepartamento(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Boolean trazerContasPagas, Boolean trazerContasAPagar, Integer departamento, Date dataCompetenciaInicial, Date dataCompetenciaFinal) throws Exception;

	void executarCriacaoGraficoCategoriaDespesaPorDepartamento(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Boolean telaInicial, Integer departamento, Date dataCompetenciaInicial, Date dataCompetenciaFinal) throws Exception;

	void consultarMapaReceitaPorCompetenciaPainelGestorFinanceiroPorPeriodo(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String filtrarPeriodoPor) throws Exception;

}
