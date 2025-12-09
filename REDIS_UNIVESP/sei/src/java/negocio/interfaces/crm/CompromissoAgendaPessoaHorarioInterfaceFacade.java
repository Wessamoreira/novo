/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.crm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.administrativo.CampanhaColaboradorVO;
import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.CompromissoCampanhaPublicoAlvoProspectVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.ReagendamentoCompromissoVO;
import negocio.comuns.crm.enumerador.PoliticaGerarAgendaEnum;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.processosel.InscricaoVO;

/**
 *
 * @author edigarjr
 */
@Repository
@Scope("singleton")
@Lazy
public interface CompromissoAgendaPessoaHorarioInterfaceFacade {

    public void persistir(CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuarioVO) throws Exception;

    public void excluir(CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuarioVO) throws Exception;

    public List consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

    public void gravarCompromissoRealizado(final Integer codigoCompromisso, UsuarioVO usuarioVO) throws Exception;

    public void incluirCompromissoPorAgendaHorarioPessoa(CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuario) throws Exception;

    public void alterarSemValidarDados(final CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuarioVO) throws Exception;

    public void incluirSemValidarDados(final CompromissoAgendaPessoaHorarioVO obj, UsuarioVO usuarioVO) throws Exception;
    
    public void criarReagendamento(CompromissoAgendaPessoaHorarioVO compromissoExistente , UsuarioVO usuarioVO);

    public CompromissoAgendaPessoaHorarioVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorAgendaPessoaHorario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorHora(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorNomeProspects(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorDescricaoCampanha(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public CompromissoAgendaPessoaHorarioVO executarGeracaoCompromisso(AgendaPessoaVO agenda, AgendaPessoaHorarioVO agendaPessoaHorario, CampanhaVO campanha, UsuarioVO usuario, ProspectsVO prospect, Integer prospectsAgendados, String ultimaHoraRegistrada, Boolean considerarSabado, Boolean considerarFeriados, String horaFinalGerarAgenda) throws Exception;

    public Integer realizarSomaTotalProspects(List<CampanhaPublicoAlvoVO> lista);

    public void validarDiaMesAnoCompromisso(CompromissoAgendaPessoaHorarioVO obj) throws Exception;

    public CompromissoAgendaPessoaHorarioVO consultarCompromissoPorCodigoProspect(Integer valorConsulta, UsuarioVO usuario) throws Exception;

    public void executarAtualizacaoEtapaAtualCompromisso(final Integer codigoEtapa, final Integer codigoCompromisso, UsuarioVO usuarioVO) throws Exception;

    public List<CompromissoAgendaPessoaHorarioVO> consultarCompromissoPorCodigoPessoaContatosPendentes(Integer codigoPessoa, Integer unidadeEnsino, Boolean contatosNaoIniciados, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

    public void realizarRemarcacoesCompromissos(List<CompromissoAgendaPessoaHorarioVO> lista, Date dataCompromissoAdiado, String horaAdiado, Boolean considerarSabado, Boolean considerarFeriados ,String horaFimCompromissoAdiado, String horaIntevaloInicioCompromissoAdiado,String horaIntevaloFimCompromissoAdiado,Integer intervaloAgendaCompromisso, UsuarioVO usuarioLogado) throws Exception;

    public void gravarCompromissoRealizadoComEtapa(final Integer codigoCompromisso, final Integer codigoEtapa, UsuarioVO usuarioVO) throws Exception;

    public void realizarRemarcacaoCompromisso(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorario, Date dataCompromissoAdiado, String horaAdiado, String horaFimAdiado, Boolean considerarSabado, Boolean considerarFeriados,String horaFimCompromissoAdiado, String horaIntevaloInicioCompromissoAdiado,String horaIntevaloFimCompromissoAdiado,Integer intervaloAgendaCompromisso,  UsuarioVO usuarioLogado) throws Exception;

    public void consultarQuantidadeCompromissoPorColaborador(CampanhaColaboradorVO campanhaColaboradorVO, Date dataIncial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

    public List<CompromissoAgendaPessoaHorarioVO> consultarCompromissoPorColaborador(CampanhaColaboradorVO campanhaColaboradorVO, Date dataIncial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

    public Boolean consultarSeCompromissoRealizadoDiaAtual(Integer valorConsulta, Integer ano, Integer mes, Integer dia) throws Exception;
//    public List consultaMontarComboboxCampanha(Integer codigoPessoa) throws Exception;

    public List consultarCompromissoFuturoProspect(Integer codigoProspect, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void removerCompromissoPreInscricaoPessoaAnteriormenteResponsavelCompromisso(
            Integer codPessoaAgendaManter,
            Integer codProspectAgenda,
            Integer codCampanha,
            boolean verificarAcesso,
            UsuarioVO usuario) throws Exception;

    public void alteratProspectCompromisso(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuarioVO) throws Exception;
    
    List<CompromissoAgendaPessoaHorarioVO> consultarTodosCompromissoPorCodigoProspect(Integer valorConsulta) throws Exception;

    public CompromissoAgendaPessoaHorarioVO executarGeracaoCompromissoRotacionamento(
        AgendaPessoaVO agenda, AgendaPessoaHorarioVO agendaPessoaHorario, CampanhaVO campanha, UsuarioVO usuario, 
        ProspectsVO prospect, String horaGeracaoProximaAgenda) throws Exception;   
    
    public void removerCompromissosNaoIniciacaoCampanha(
            Integer codCampanha, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

    public void excluirCompromissoAgendaPessoaHorarioNaoInicializadoProspect(Integer prospect, UsuarioVO usuarioVO) throws Exception;
    
    public boolean verificarExisteAgendaNaoConcluidaProspectDentroPeriodoParaCaptacao(
            Integer codigoProspect, Date dataInicio, Date dataFinal) throws Exception;

	void alterarDataCompromissoRealizado(Integer codigoCompromisso, final Date dataCompromisso, final String horaCompromisso, final Integer agendapessoahorario, UsuarioVO usuarioVO) throws Exception;

	void realizarExclusaoCompromissos(List<CompromissoAgendaPessoaHorarioVO> lista, UsuarioVO usuarioLogado) throws Exception;

	void realizarAlteracaoCompromissoAoAlterarConsultor(ProspectsVO prospectsVO, UsuarioVO usuarioLogado) throws Exception;
	
	public void executarAtualizacaoTipoSituacaoCompromissoEnum(final TipoSituacaoCompromissoEnum tipoSituacaoCompromissoEnum, final Integer codigoCompromisso, UsuarioVO usuarioVO) throws Exception;

	boolean consultarExisteCompromissoCampanhaTipoCobranca(Integer codigoProspect) throws Exception;

	public boolean verificarExisteAgendaNaoConcluidaProspectDentroPeriodoParaCobranca(Integer codigoProspect, Date dataInicio, Date dataFinal) throws Exception;
	
    public void gerarAgendaCampanhaCRMInscricaoProcessoSeletivo(Integer codigoProcessoSeletivo,
                InscricaoVO inscricaoVO, PoliticaGerarAgendaEnum politica, boolean verificarAcesso, UsuarioVO usuario) throws Exception;
        
    public boolean verificarExisteAgendaNaoConcluidaProspectCampanhaEspecifica(Integer codigoProspect, 
                    Integer codigoCampanha) throws Exception;

	/** 
	 * @author Wellington - 7 de out de 2015 
	 * @param compromissoAgendaPessoaHorario
	 * @param preInscricao
	 * @throws Exception 
	 */
	void gravarPreInscricaoCompromissoAgendaPessoaHorario(Integer compromissoAgendaPessoaHorario, Integer preInscricao, UsuarioVO usuarioVO) throws Exception;

	public void excluirCompromissoAgendaPessoaHorario(Integer prospect, UsuarioVO usuarioVO) throws Exception;
	
	/**
	 * @author Carlos Eugênio - 10/11/2016
	 * @param novoCompromisso
	 * @param agendaPessoaHorario
	 * @param campanha
	 * @param prospect
	 * @param usuario
	 * @throws Exception
	 */
	CompromissoAgendaPessoaHorarioVO inicializarDadosCompromissoAgendaPorCompromissoCamapnhaPublicoAlvo(CompromissoCampanhaPublicoAlvoProspectVO novoCompromisso, AgendaPessoaHorarioVO agendaPessoaHorario, CampanhaVO campanha, ProspectsVO prospect, UsuarioVO usuario) throws Exception;

	/**
	 * @author Carlos Eugênio - 25/11/2016
	 * @param campanha
	 * @param prospect
	 * @param dataCompromisso
	 * @param usuarioVO
	 * @return
	 */
	TipoSituacaoCompromissoEnum consultarSituacaoAtualCompromissoPorProspectCampanha(Integer campanha, Integer prospect, Date dataCompromisso, UsuarioVO usuarioVO);

	/**
	 * @author Carlos Eugênio - 25/11/2016
	 * @param campanha
	 * @param usuarioVO
	 * @return
	 */
	public HashMap<Integer, List<CompromissoAgendaPessoaHorarioVO>> consultarQuantidadeCompromissoIniciouAgendaPorCampanhaProspect(Integer campanha, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs, List<CampanhaColaboradorVO> campanhaColaboradorVOs, UsuarioVO usuarioVO);

	/**
	 * @author Carlos Eugênio - 29/11/2016
	 * @param campanha
	 * @param listaCampanhaPublicoAlvoProspectVOs
	 * @param usuarioVO
	 * @throws Exception
	 */
	void excluirCompromissoAgendaPessoaHorarioPorCampanhaProspect(Integer campanha, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs, UsuarioVO usuarioVO) throws Exception;
	
	CompromissoAgendaPessoaHorarioVO realizarCriacaoCompromissoCampanhaLigacaoReceptivaRS(final ProspectsVO prospectVO, CampanhaVO campanhaVO, TipoCompromissoEnum tipoCompromisso, Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, String duvida, UsuarioVO usuario) throws Exception;
	
	void excluirCompromissoAgendaPessoaHorarioPorCampanhaProspectNaoInicializado(Integer campanha, Integer prospect, UsuarioVO usuarioVO) throws Exception;
	
	public void removerVinculoInteracaoWorkFlowCompromissoAgendaPessoaHorario(Integer campanha, List<CampanhaPublicoAlvoProspectVO> listaCampanhaPublicoAlvoProspectVOs, UsuarioVO usuarioVO) throws Exception;

	public void incluirReagendamento(ReagendamentoCompromissoVO obj, UsuarioVO usuarioVO) throws Exception;
	
	public void executarCancelamentoCompromissosNaoIniciacaoCampanha(Integer codCampanha, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	
}