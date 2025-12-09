package negocio.interfaces.protocolo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.protocolo.AlterarResponsavelRequerimentoVO;
import negocio.comuns.protocolo.EstatisticaRequerimentoVO;
import negocio.comuns.protocolo.RequerimentoDisciplinaVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.RequerimentoResumoOperacaoLoteVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface RequerimentoInterfaceFacade {

	public RequerimentoVO novo() throws Exception;

	public void incluir(RequerimentoVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public void alterar(RequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public void excluir(RequerimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean validarPermissao) throws Exception;

	public void alterarSituacao(Integer requerimento, String situacao, String motivoIndeferimento, String motivoDeferimento, RequerimentoVO requerimentoVO,UsuarioVO usuarioVO) throws Exception;

	public void alterarOrdemExecucaoTramiteDepartamento(final Integer requerimento, final Integer ordem, final Integer codigoDpto) throws Exception;

	public void iniciarRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception;

	public void enviarRequerimentoProximoDepartamento(RequerimentoVO requerimentoVO, RequerimentoHistoricoVO requerimentoHistoricoAtualVO, Boolean usarFuncionarioProximoTramite, Boolean usarCoordenadorEspecificoTramite,   FuncionarioVO funcionarioProximoTramite, UsuarioVO usuarioVO) throws Exception;

	public void deferirRequerimento(RequerimentoVO requerimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, RequerimentoHistoricoVO requerimentoHistoricoAtualVO, UsuarioVO usuarioVO) throws Exception;
	
	public void indeferirRequerimento(RequerimentoVO requerimentoVO, boolean validarAcesso, boolean indeferimentoAutomatico, UsuarioVO usuarioVO) throws Exception;

	public void iniciarExecucaoRequerimentoNoDepartamento(RequerimentoVO requerimentoVO, Boolean realizandoRecebimento, UsuarioVO usuarioVO) throws Exception;
	
	void validarDadosBloqueioRequerimentoAbertoSimultaneo(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception;

	public RequerimentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public RequerimentoVO consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(Integer codigoPrm, String filtro, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public void receberBoleto(RequerimentoVO requerimento, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public List<RequerimentoVO> consultarPorNomeCPFPessoa(String valorConsulta, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorCodigo(Integer valorConsulta, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;
	
	List<RequerimentoVO> consultarPorCodigo(String listaCodigos, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorData(Date prmIni, Date prmFim, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorNomeTipoRequerimento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorValor(Double valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorDataPrevistaFinalizacao(Date prmIni, Date prmFim, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorDataFinalizacao(Date prmIni, Date prmFim, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorSituacao(String valorConsulta, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorSituacaoFinanceira(String valorConsulta, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorNomePessoa(String valorConsulta, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorMatriculaMatricula(String valorConsulta, String tipo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorCodigoCentroReceita(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorMatriculaMatriculaVisaoAluno(String valorConsulta, Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorNomeTipoRequerimento(String valorConsulta, Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorCodigo(Integer valorConsulta, Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void registrarRecebimentoDoc(RequerimentoVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public void alterarSituacaoFinanceiraESituacaoExecucao(Integer codigo, Boolean realizandoRecebimento, String situacao, String situacaoFinanceira, boolean realizandoBaixaAutomatica, UsuarioVO usuario) throws Exception;

	public List<RequerimentoVO> consultarPorMatriculaSituacaoDiferenteDe(String matricula, String string, String string2, Integer codigo, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorCodigoSemProgramacaoFormatura(Integer valorInt, String string, List<ProgramacaoFormaturaUnidadeEnsinoVO> proFormaturaUnidadeEnsinoVOs, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorDataSemProgramacaoFormatura(Date dateTime, Date dateTime2, String string, List<ProgramacaoFormaturaUnidadeEnsinoVO> proFormaturaUnidadeEnsinoVOs, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorSituacaoSemProgramacaoFormatura(String valorConsultarRequerimento, String string, List<ProgramacaoFormaturaUnidadeEnsinoVO> proFormaturaUnidadeEnsinoVOs, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultarPorResponsavelSemProgramacaoFormatura(String valorConsultarRequerimento, String string, List<ProgramacaoFormaturaUnidadeEnsinoVO> proFormaturaUnidadeEnsinoVOs, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public Integer consultaRapidaRequerimentoPendenteUsuario(Integer codUsuarioLogado) throws Exception;

	public List<RequerimentoVO> consultarPorMatriculaMatriculaSemProgramacaoFormatura(String valorConsultarRequerimento, String string, List<ProgramacaoFormaturaUnidadeEnsinoVO> proFormaturaUnidadeEnsinoVOs, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<EstatisticaRequerimentoVO> consultaRapidaRequerimentosPendentes(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentosPorCodigoTipoRequerimento(Integer unidadeEnsino, Integer codTipoRequerimento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void verificarExisteSolicitacaoRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorCodigo(Integer requerimento, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorData(Date dataInicio, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorNomeTipoRequerimento(String nomeTipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorSituacaoRequerimento(String situacao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorSituacaoFinanceiraRequerimento(String situacao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorNomePessoa(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorCpfPessoa(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorMatricula(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPendenteUsuarioMenu(Integer codUsuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void alterarCodigoArquivo(RequerimentoVO obj, Integer codArquivo, UsuarioVO usuario) throws Exception;

	public void gravarNovoArquivo(RequerimentoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorCodigo(Integer requerimento, Integer unidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorData(Date dataInicio, Date dataFim, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorNomeTipoRequerimento(String nomeTipoRequerimento, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorNomePessoa(String nomePessoa, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorCpfPessoa(String cpfPessoa, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorMatricula(String matricula, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorSituacaoFinanceiraRequerimento(String situacaoFinanceira, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public List<RequerimentoVO> consultaRapidaRequerimentoPorCodigoAluno(Integer requerimento, Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	public void carregarDados(RequerimentoVO obj, UsuarioVO usuario) throws Exception;

	public void carregarDados(RequerimentoVO obj, NivelMontarDados nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	List<RequerimentoVO> consultaRapidaRequerimentoPorCodigoPai(Integer codigoPai, String matriculaAluno, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	public List<RequerimentoVO> consultarRequerimentoAtrasadosParaExclusao(UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceira) throws Exception;

	public List<RequerimentoVO> consultarRequerimentosMapaReposicao(RequerimentoVO obj, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataFim, Integer unidadeEnsino, String ordenarPor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	SqlRowSet consultarDadosParaNotificacaoAtraso();

	void executarRegistroNotificacaoRequerimentoEmAtraso(List<Integer> requerimentos);

	public Boolean consultaRapidaRequerimentoReposicaoAluno(String matricula, Integer disciplina) throws Exception;

	String consultarDadosGraficoRequerimento(RequerimentoVO obj, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<SelectItem> departamentoVOs, CursoVO curso, TurmaVO turma, String situacao, String situacaoFinanceira, Date dataIni, Date dataFim, boolean todoPeriodo, String sigla, Integer situacaoRequerimentoDepartamento, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirConsultarTodasUnidades, Boolean permiteConsultarRequerimentoOutrosResponsaveis, Boolean permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartamento,  Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios) throws Exception;

	String consultarDadosGraficoPainelGestorRequerimento(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String situacao, Boolean permissaoConsultarOutrosDepartamentosConsultoresGrafico, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void alterarFuncionarioResponsavel(Integer requerimento, Integer funcionario, UsuarioVO usuarioLogado) throws Exception;

	void alterarFuncionarioResponsavelEDepartamento(Integer requerimento, Integer funcionario, Integer departamento, UsuarioVO usuarioLogado) throws Exception;

	public String montaEnderecoRelatorioRequerimento(Integer codRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void realizarValidacaoRegrasCriacaoRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarValidacaoCobrancaRequerimento(RequerimentoVO requerimentoVO) throws Exception;

	RequerimentoHistoricoVO realizarVerificacaoRequerimentoHistoricoAtualPossueQuestionarioResponder(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception;

	void enviarRequerimentoDepartamentoAnterior(RequerimentoVO requerimentoVO, Integer tipoRequerimentoDepartamentoVoltar, String motivoRetorno, UsuarioVO usuarioVO) throws Exception;

	List<SelectItem> consultarDepartamentoAnterioresPermiteRetornar(RequerimentoVO requerimentoVO) throws Exception;


	void persistirRequerimento(RequerimentoVO requerimento, RequerimentoHistoricoVO requerimentoHistoricoVO, Boolean visaoAluno, Boolean exigePagamento, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, ConfiguracaoFinanceiroVO configuracaoFinanceira, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception;

	Boolean validarApresentarBotaoImprimirVisaoAluno(RequerimentoVO requerimento) throws Exception;

	void alterarDataUltimaImpressao(Integer codigoRequerimento) throws Exception;

	Integer consultarTotalRegistros(RequerimentoVO obj, List<UnidadeEnsinoVO> listaUnidadeEnsino, List<SelectItem> listaSelectItemDepartamento, CursoVO curso, TurmaVO turma, String situacao, String situacaoFinanceira, Date dataIni, Date dataFim, boolean todoPeriodo, String sigla, Integer situacaoRequerimentoDepartamento, String ordenarPor, boolean ordemCrescente, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirConsultarTodasUnidades, Boolean permiteConsultarRequerimentoOutrosResponsaveis, Boolean permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartamento,  Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios) throws Exception;

	List<RequerimentoVO> consultarOtimizado(RequerimentoVO obj, List<UnidadeEnsinoVO> listaUnidadeEnsino, List<SelectItem> listaSelectItemDepartamento, CursoVO curso, TurmaVO turma, String situacao, String situacaoFinanceira, Date dataIni, Date dataFim, boolean todoPeriodo, String sigla, Integer situacaoRequerimentoDepartamento, String ordenarPor, boolean ordemCrescente, boolean controlarAcesso, 
			UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, int limit, int offset, Boolean permitirConsultarTodasUnidades, Boolean permiteConsultarRequerimentoOutrosResponsaveis, Boolean permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartamento,  Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios, DataModelo dataModelo) throws Exception;

	/**
	 * @author Wellington Rodrigues - 21/05/2015
	 * @param pessoaAntigo
	 * @param pessoaNova
	 * @throws Exception
	 */
	void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception;

	/**
	 * @author Rodrigo Wind - 09/10/2015
	 * @param requerimentoVO
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	Boolean realizarVerificarProximoTramiteExigeInformarFuncionarioResponsavel(RequerimentoVO requerimentoVO, FuncionarioVO funcionarioProximoTramite, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 09/10/2015
	 * @param unidadeEnsino
	 * @param controlarAcesso
	 * @param usuario
	 * @param configuracaoGeralSistemaVO
	 * @return
	 * @throws Exception
	 */
	List<RequerimentoVO> consultaRapidaRequerimentoVisaoProfessorCoordenador(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	/**
	 * @author Rodrigo Wind - 09/10/2015
	 * @param usuario
	 * @return
	 */
	Integer consultarQtdeRequerimentoAlunoVisaoProfessorCoordenador(UsuarioVO usuario);

	/**
	 * @author Rodrigo Wind - 15/10/2015
	 * @param requerimentoVO
	 * @param funcionarioVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void realizarAlteracaoFuncionarioResponsavelRequerimento(RequerimentoVO requerimentoVO, FuncionarioVO funcionarioVO, UsuarioVO usuarioVO) throws Exception;

	public RequerimentoVO consultaRapidaUltimoRequerimentoPorMatriculaTipoDocumentoDiploma(String matricula);

	/**
	 * @author Rodrigo Wind - 11/11/2015
	 * @param requerimentoVO
	 * @throws Exception
	 */
	void realizarValidacaoRequisitantePermiteAbrirRequerimento(RequerimentoVO requerimentoVO) throws Exception;
	
	List<RequerimentoVO> consultarOtimizadoParaAlterarResponsavel(AlterarResponsavelRequerimentoVO alterarResponsavelRequerimento,
				List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO curso, TurmaVO turma, DepartamentoVO departamento, TipoRequerimentoVO tipoRequerimento, Date dataIni, Date dataFim, Boolean todoPeriodo, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	/** 
	 * @author Wellington - 11 de abr de 2016 
	 * @param matricula
	 * @param disciplina
	 * @return
	 * @throws Exception 
	 */
	String executarVerificacaoProfessorMinistrouAula(String matricula, Integer disciplina, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 19 de abr de 2016 
	 * @param requerimentoVO
	 * @param valorDescontoSelecionado
	 * @param tipoDescontoTemp
	 * @param valorAcrescimoDescontoTemp
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void executarVerificacaoUsuarioPossuiPermissaoInformarDescontoAcrescimo(RequerimentoVO requerimentoVO, boolean valorDescontoSelecionado, String tipoDescontoTemp, Double valorAcrescimoDescontoTemp, String usuarioLiberarOperacaoFuncionalidade, String senhaLiberarOperacaoFuncionalidade, List<OperacaoFuncionalidadeVO> operacaoFuncionalidadeVOs) throws Exception;
	
	/**
	 * @author Rodrigo Wind - 01/06/2016
	 * @param requerimento
	 * @param situacao
	 * @throws Exception
	 */
	void alterarSituacaoRequerimento(Integer requerimento, String situacao, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 23/06/2016
	 * @param codigoPrm
	 * @param tipoRequerimento
	 * @param unidadeEnsino
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	RequerimentoVO consultarPorChavePrimariaTipoRequerimentoInclusaoEReposicaoDisciplinaDadosMinimos(Integer codigoPrm, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public void alterarDisciplina(final Integer requerimento, final Integer disciplina, UsuarioVO usuarioVO) throws Exception;

	void excluirRequerimentoPorMatricula(String matricula, Integer unidadeEnsino,
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			UsuarioVO usuarioVO) throws Exception;

	Boolean realizarVerificarProximoTramiteExigeInformarCoordenadorCursoResponsavel(RequerimentoVO requerimentoVO,
			FuncionarioVO funcionarioProximoTramite, UsuarioVO usuario) throws Exception;
	
	List<RequerimentoVO> consultarPorMatriculaFichaAluno(String matricula, Integer tipoRequerimento, String situacao, String mesAno, UsuarioVO usuarioVO) throws Exception;

	List<SelectItem> consultarMesAnoRequerimentoPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO);
	
	public Date atualizarDataPrevistaFinalizacao(RequerimentoVO requerimentoVO);
	
	public RequerimentoVO inicializarRequerimentoConformeTipoRequerimento(TipoRequerimentoVO tipoRequerimentoVO, RequerimentoVO requerimento, Boolean forcarDefinicaoData) throws Exception;

	void imprimirComprovanteRequerimentoBemateck(RequerimentoVO requerimentoVO, ImpressoraVO impressoraVO,			
			UsuarioVO usuarioVO, String professorMinistrouAula) throws Exception;

	Boolean consultarTipoRequerimentoVinculadoRequerimento(TipoRequerimentoVO tipoRequerimentoVO) throws Exception;

	void realizarAutorizacaoPagamentoRequerimento(RequerimentoVO requerimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	void realizarDefinicaoVencimentoContaReceberRequerimento(RequerimentoVO requerimentoVO, boolean forcarDefinicaoData) throws Exception;	
	void alterarSituacaoDepartamento(Integer requerimento, Integer situacao, UsuarioVO usuarioVO) throws Exception;		

	void incluirSolicitacaoIsencaoTaxa(RequerimentoVO requerimentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception;

	void realizarRegistroDeferimentoIndeferimentoSolicitacaoIsencaoTaxa(RequerimentoVO requerimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	void validarDadosSolicitacaoIsencaoTaxa(RequerimentoVO requerimentoVO) throws ConsistirException;

	void realizarInicializacaoDepartamentoEResponsavelRequerimento(Integer requerimento, Boolean realizandoRecebimento, String situacao, UsuarioVO usuarioVO) throws Exception;

	void realizarValidacaoChoqueHorarioEVagaTurmaRequerimentoReposicao(RequerimentoVO requerimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	StringBuilder getSqlQtdeRequerimentoReposicaoNaoFinalizadosNaoVencidosParaContablizacaoVaga(String sqlCampoTurma, Integer turmaEspecifica, Integer disciplina, String ano, String semestre);	
	
	public void excluirContaReceber(RequerimentoVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception;
	
	void reativarContaReceberRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception;
	
	public void alterarDataUltimaAlteracao(final Integer requerimento) throws Exception;
	
	public Integer consultarTotalInteracaoNaoLida(Integer idPessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public void alterarFuncionarioRequerimentoUnificacaoFuncionario(Integer funcionarioAntigo, Integer funcionarioNovo) throws Exception;

	public void alterarDataPrevistaFinalizacaoRequerimento(RequerimentoVO requerimentoVO,UsuarioVO usuarioVO) throws Exception;
	
	public boolean verificarCobrancaComBaseUltimaAula(RequerimentoVO requerimentoVO) throws Exception;
	
	public Long qtdDiasExedidosDoPrazoComBaseUltimaAula(Integer codigoMatricula) throws Exception;
	
	void executarIndeferirRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
	
	public void atualizarValorRequerimento(RequerimentoVO requerimentoVO, Boolean forcarDefinicaoData, UsuarioVO usuarioVO) throws Exception;
	
	public String realizarImpressaoComprovanteRequerimento(RequerimentoVO requerimentoVO, SuperParametroRelVO superParametroRelVO, UsuarioVO usuarioVO, String tipoLayout) throws Exception;
	
	public String realizarImpressaoDeclaracaoRequerimento(RequerimentoVO requerimentoVO, ImpressaoContratoVO impressaoContratoFiltro, ImpressaoContratoVO contratoGravar, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, TextoPadraoDeclaracaoVO texto, Boolean persistirDocumentoAssinado, UsuarioVO usuarioVO) throws Exception;

	public List<RequerimentoVO> consultarPorTipoRequerimentoAberto(RequerimentoVO requerimentoVO, UsuarioVO usuarioLogado) throws Exception;
	
	public void alterarUnidadeEnsinoTurmaBaseMatriculaPeriodo (TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void gravarAtualizacaoMatricula(RequerimentoVO requerimentoVO,UsuarioVO usuarioVO) throws Exception;
	
	public void montarListaSelectItemTurmaAdicionar(RequerimentoVO requerimentoVO, List<TurmaVO> listaTurmaIncluir, UsuarioVO usuarioVO, Map<Integer, List<TurmaVO>> mapTurmas);
	
	public void definirTurmaBaseRequerimentosTurmaAgrupada(RequerimentoVO requerimentoVO, List<TurmaVO> listaTurmaIncluir, UsuarioVO usuarioVO, Map<Integer, List<TurmaVO>> mapTurmas, TurmaVO turmaVO) throws Exception;

	void excluirRequerimentoPorMatriculaPeriodo(Integer matriculaPeriodo, Integer unidadeEnsino,
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			UsuarioVO usuarioVO) throws Exception;

	void adicionarRequerimentoDisciplina(RequerimentoVO requerimentoVO, RequerimentoDisciplinaVO requerimentoDisciplinaVO, UsuarioVO usuarioVO) throws Exception;

	void removerRequerimentoDisciplina(RequerimentoVO requerimentoVO, RequerimentoDisciplinaVO requerimentoDisciplinaVO,
			UsuarioVO usuarioVO) throws Exception;

	void carregarDadoCampoServidorOnlineArquivo(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception;

	void persistirSicronizadoRequerimento(RequerimentoVO requerimento, RequerimentoHistoricoVO requerimentoHistoricoVO,
			Boolean visaoAluno, Boolean exigePagamento, ConfiguracaoGeralSistemaVO configuracaoGeralSistema,
			ConfiguracaoFinanceiroVO configuracaoFinanceira, UnidadeEnsinoVO unidadeEnsinoLogado,
			UsuarioVO usuarioLogado) throws Exception;

	List<RequerimentoVO> consultarRequerimentoOperacaoLote(RequerimentoVO obj, List<UnidadeEnsinoVO> listaUnidadeEnsino,
			CursoVO curso, Date dataIni, Date dataFim, boolean todoPeriodo, String ordenarPor, boolean ordemCrescente,
			boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,
			int limit, int offset, Boolean permitirConsultarTodasUnidades,
			Boolean permiteConsultarRequerimentoOutrosResponsaveis,
			Boolean permiteConsultarRequerimentoOutrosResponsaveisMesmoDepartanento,
			Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios) throws Exception;

	void realizarGeracaoResumoOperacaoLote(List<RequerimentoVO> listaConsulta, TipoRequerimentoVO tipoRequerimentoVO,
			List<RequerimentoResumoOperacaoLoteVO> resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsino,
			List<RequerimentoResumoOperacaoLoteVO> resumoOperacaoLoteTransferenciaInternaPorUnidadeEnsinoCurso);
	
	public Integer consultarContagemRequerimentoDeferidoUnidadeEnsinoCurso(Integer unidadeEnsinoTransferenciaInterna,Integer cursoTransferenciaInterna,  Integer tipoRequerimento) ;

	
}
