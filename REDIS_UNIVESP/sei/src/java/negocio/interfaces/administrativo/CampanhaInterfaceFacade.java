package negocio.interfaces.administrativo;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.CampanhaColaboradorVO;
import negocio.comuns.administrativo.CampanhaMidiaVO;
import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;

public interface CampanhaInterfaceFacade {

    public CampanhaVO novo() throws Exception;
    public void incluir(CampanhaVO obj, List<CampanhaColaboradorVO> objetosColaborador, List<CampanhaMidiaVO> objetosMidia, List<CampanhaPublicoAlvoVO> objetosPublicoAlvo, UsuarioVO usuario) throws Exception;
    public void gravarSituacao(CampanhaVO obj, UsuarioVO usuario) throws Exception;
    public void gravarAgenda(final CampanhaVO obj) throws Exception;
    public void alterar(CampanhaVO obj, List<CampanhaColaboradorVO> objetosColaborador, List<CampanhaMidiaVO> objetosMidia, List<CampanhaPublicoAlvoVO> objetosPublicoAlvo, UsuarioVO usuario) throws Exception;
    public void excluir(CampanhaVO obj, UsuarioVO usuario) throws Exception;
    public CampanhaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultarPorUnidadeEnsino(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultarPorListaUnidadeEnsinoPorListaCurso(List<UnidadeEnsinoVO> listaUnidadeensino, List<CursoVO> listaCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultarPorCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultarPorCurso(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    //public List<CampanhaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultarPorDescricao(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuario) throws Exception;
    public List consultar(String campoConsulta, String valorConsulta, Integer unidadeEnsino, String situacao, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultarPorUnidadeEnsinoPorSituacaoPorTipoCampanha(Integer codigoUnidadeEnsino, String situacao, TipoCampanhaEnum tipoCampanhaEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void validarCampanhaGerarAgenda(CampanhaVO obj) throws ConsistirException;
    public Date persistirAlteracaoCompromissoPorCampanha(CampanhaVO campanha, CampanhaColaboradorVO campanhaColaboradorVO, List<CampanhaColaboradorVO> listaCampanhaColaboradorAlterarCompromisso, Date dataIncial, Date dataFinal, String tipoAlteracaoColaborador, Date dataNovoCompromisso, String horaNovoCompromisso, Boolean considerarSabado, Boolean considerarFeriados, UsuarioVO usuarioLogado) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public List<SelectItem> consultarListaSelectItemCampanha(Integer unidadeEnsino, String situacao, Obrigatorio obrigatorio, Integer vendedor) throws Exception;
    public List<CampanhaVO> consultarPorCodigoCampanhaColaboradoFuncionarioPorTipoCampanha(Integer valorConsulta, Integer unidadeEnsino, String situacao, TipoCampanhaEnum tipoCampanhaEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public AgendaPessoaHorarioVO realizarValidacaoAgendaPessoaHorarioExiste(AgendaPessoaVO agenda,  Date dataCompromisso, Boolean considerarSabado, Boolean considerarFeriados, UsuarioVO usuarioLogado) throws Exception;
    public List<CampanhaVO> consultarPorDescricao(String valorConsulta, String situacao, Integer unidadeensino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultarPorUnidadeEnsino(String valorConsulta, String situacao, Integer unidadeensino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<CampanhaVO> consultarPorCurso(String valorConsulta, String situacao, Integer unidadeensino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public Date gerarAgendaDistribuindoProspectsPorConsultor(CampanhaVO campanha, ProgressBarVO progressBar, UsuarioVO usuario) throws Exception;
    public void cancelarCampanha(final CampanhaVO obj, UsuarioVO usuario) throws Exception;
    public void finalizarCampanha(final CampanhaVO obj, UsuarioVO usuario) throws Exception;
	void executarCriacaoCampanhaRecorrenteCriacaoAgenda();
	void finalizarAgendaCompromissoContaReceber(NegociacaoRecebimentoVO negociacao) throws Exception;
	CampanhaVO consultarCampanhaPorCodigoCompromisso(Integer codigoCompromisso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
    public List<CampanhaVO> consultarPorDescricao(String valorConsulta, String tipoCampanha, String situacao, Integer unidadeensino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    /**
	 * @author Carlos Eugênio - 04/10/2016
	 * @param campanha
	 * @param usuario
	 * @throws Exception
	 */
	void carregarDadosCompletosConsultoresCampanha(CampanhaVO campanha, UsuarioVO usuario) throws Exception;

	/**
	 * @author Carlos Eugênio - 08/11/2016
	 * @param campanhaPublicoAlvoVO
	 * @param usuarioVO
	 */
	void realizarMontagemListaConsultorProspect(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Carlos Eugênio - 08/11/2016
	 * @param campanhaPublicoAlvoVO
	 * @param consultorSelecionadoVO
	 * @param usuarioVO
	 * @return 
	 */
	List<CampanhaPublicoAlvoProspectVO> realizarVisualizacaoProspectsPorConsultor(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, FuncionarioVO consultorSelecionadoVO, Boolean carregarSomenteCompromissoUltrapassouDataCampanha, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 10/11/2016
	 * @param campanhaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarGeracaoAgendaDeAcordoPublicoAlvo(CampanhaVO campanhaVO, ProgressBarVO progressBar, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Carlos Eugênio - 17/11/2016
	 * @param campanhaVO
	 * @param usuarioVO
	 * @return
	 */
	List<FuncionarioVO> realizarMontagemListaConsultorProspectTotalizador(CampanhaVO campanhaVO, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 18/11/2016
	 * @param campanha
	 * @param campanhaPublicoAlvoVO
	 * @param progressBar
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	Date realizarCarregamentoAgendaDistribuindoProspectsPorConsultor(CampanhaVO campanha, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, ProgressBarVO progressBar, Boolean alterarConsultorPadraoProspect,  UsuarioVO usuario, Boolean alterarConsultorCompromissoProspectJaIniciado) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 24/11/2016
	 * @param campanhaVO
	 * @param consultorVO
	 * @param usuarioVO
	 * @return
	 */
	List<CampanhaPublicoAlvoProspectVO> realizarVisualizacaoProspectsPorConsultorGeral(CampanhaVO campanhaVO, FuncionarioVO consultorVO, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 24/11/2016
	 * @param campanhaVO
	 * @param usuarioVO
	 */
	void inicializarDadosCompromissoUltrapassouDataLimite(CampanhaVO campanhaVO, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 24/11/2016
	 * @param campanhaVO
	 * @param progressBar
	 * @param usuarioVO
	 * @throws Exception
	 */
	void realizarRedistribuicaoConsultorPublicoAlvo(CampanhaVO campanhaVO, ProgressBarVO progressBar, Boolean alterarConsultorPadraoProspect, UsuarioVO usuarioVO, Boolean alterarConsultorCompromissoProspectJaIniciado) throws Exception;
	/**
	 * @author Carlos Eugênio - 29/11/2016
	 * @param campanhaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void regerarAgendaCampanha(CampanhaVO campanhaVO, ProgressBarVO progressBar, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Carlos Eugênio - 29/11/2016
	 * @param campanha
	 * @param usuarioVO
	 * @return
	 */
	Boolean consultarExistenciaCampanhaPossuiAgenda(Integer campanha, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 01/12/2016
	 * @param campanhaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void inicializarDadosConsultorIncluidoCampanha(CampanhaVO campanhaVO, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Carlos Eugênio - 01/12/2016
	 * @param campanhaVO
	 * @param campanhaPublicoAlvoVO
	 * @param progressBar
	 * @param usuarioVO
	 * @throws Exception
	 */
	void realizarRedistribuicaoConsultorPublicoAlvoEspecifico(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, ProgressBarVO progressBar, Boolean alterarConsultorPadraoProspect, UsuarioVO usuarioVO, Boolean alterarConsultorCompromissoProspectJaIniciado) throws Exception;
	/**
	 * @author Carlos Eugênio - 01/12/2016
	 * @param campanhaVO
	 * @param campanhaPublicoAlvoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void regerarAgendaCampanhaPublicoAlvoEspecifico(CampanhaVO campanhaVO, ProgressBarVO progressBar, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Carlos Eugênio - 02/12/2016
	 * @param campanhaPublicoAlvoVO
	 * @param usuarioVO
	 * @return
	 */
	List<CampanhaPublicoAlvoProspectVO> realizarVisualizacaoProspectsSemConsultor(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 02/12/2016
	 * @param campanhaVO
	 * @param campanhaPublicoAlvoVO
	 * @param progressBar
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarGeracaoAgendaDeAcordoPublicoAlvoEspecifico(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, ProgressBarVO progressBar, UsuarioVO usuarioVO) throws Exception;
	/**
	 * @author Carlos Eugênio - 14/12/2016
	 * @param campanhaVO
	 * @param campanhaPublicoAlvoVO
	 * @param usuarioVO
	 */
	void realizarVerificacaoPoliticaRedistribuicaoProspectSeEncontraAtualmenteCampanhaPublicoAlvoEspecifico(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 14/12/2016
	 * @param campanhaPublicoAlvoVO
	 * @param listaCampanhaColaboradorVOs
	 * @param usuarioVO
	 */
	void inicializarDadosQuantidadeProspectPorSituacaoAtualCampanhaPorCampanhaPublicoAlvoEspecifico(CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, List<CampanhaColaboradorVO> listaCampanhaColaboradorVOs, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 14/12/2016
	 * @param campanhaVO
	 * @param listaCampanhaColaboradorVOs
	 * @param usuarioVO
	 */
	void inicializarDadosQuantidadeProspectPorSituacaoAtualCampanha(CampanhaVO campanhaVO, List<CampanhaColaboradorVO> listaCampanhaColaboradorVOs, UsuarioVO usuarioVO);
	/**
	 * @author Carlos Eugênio - 16/12/2016
	 * @param campanhaVO
	 * @param campanhaPublicoAlvoVO
	 * @param usuarioVO
	 */
	void inicializarDadosMediaProspectPorColaborador(CampanhaVO campanhaVO, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, UsuarioVO usuarioVO);
	
	CampanhaVO consultarCampanhaPorTipoCampanhaUnidadeEnsino(Integer unidadeEnsino, String situacao, TipoCampanhaEnum tipoCampanhaEnum, int nivelMontarDados, UsuarioVO usuario) throws Exception;
//	boolean verificarDeveSerGeradaAgendaProspectControlandoDuplicidadeDeCompromisso(CampanhaVO campanha, ProspectsVO prospectsVO, UsuarioVO usuario);
//	CampanhaColaboradorVO obterConsultorDistribuicaoRotacionadaParaGerarAgendaLigacaoReceptiva(CampanhaVO campanha, Integer consultorDistribuir) throws Exception;
	void realizarVerificacaoPoliticaRedistribuicaoProspectSeEncontraAtualmenteCampanha(CampanhaVO campanhaVO,
			UsuarioVO usuarioVO);
	boolean verificarDeveSerGeradaAgendaProspectControlandoDuplicidadeDeCompromisso(CampanhaVO campanha,
			ProspectsVO prospectsVO, UsuarioVO usuario);
	CampanhaColaboradorVO obterConsultorDistribuicaoRotacionadaParaGerarAgendaLigacaoReceptiva(CampanhaVO campanha,
			Integer consultorDistribuir) throws Exception;
	void realizarCriacaoCampanhaPublicoAlvoProspectAdicionadoDinamicamente(CampanhaVO campanhaVO, UsuarioVO usuarioVO)
			throws Exception;
}
