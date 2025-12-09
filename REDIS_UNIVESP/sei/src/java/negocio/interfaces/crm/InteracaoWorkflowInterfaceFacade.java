package negocio.interfaces.crm;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.EtapaWorkflowAntecedenteVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoOrigemInteracaoEnum;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface InteracaoWorkflowInterfaceFacade {
	

    public void persistir(InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception;
    public void persistirFollowUpContaReceber(BoletoBancarioRelVO boleto, String codigoUsuario, String origemRotina, String nomePDF) throws Exception;
    public List consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void validarDados(InteracaoWorkflowVO obj) throws Exception;
//    public InteracaoWorkflowVO consultarInteracaoWorkflowExistentePorCodigoFollowUp(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public InteracaoWorkflowVO executarPreenchimnetoNovaInteracaoWorkflowPorCompromissoPorEtapaAtual(Integer compromisso, Integer etapaAtual, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public InteracaoWorkflowVO consultarInteracaoWorkflowExistentePorCodigoCompromissoPorEtapaAtual(Integer compromisso, Integer etapaAtual, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public InteracaoWorkflowVO consultarInteracaoWorkflowAnterior(Integer codigoEtapa, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<InteracaoWorkflowVO> consultarInteracaoWorkflowAnteriorPorCodigoCompromissoPorEtapaAtual(Integer nivelApresentacao, Integer compromisso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;
    //public InteracaoWorkflowVO consultarInteracaoWorkflowNovo(int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public InteracaoWorkflowVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeProspects(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorDescricaoCampanha(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorNomeWorkflow(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorDescricaoCompromissoAgendaPessoaHorario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List<String> consultarObservacoesEtapasAnteriores(Integer nivelApresentacao, Integer compromisso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeSituacaoProspectPipeline(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorEtapaWorkflow(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List<InteracaoWorkflowVO> consultarInteracoesWorkflowExistentesPorCodigoCompromisso(InteracaoWorkflowVO interacaoWorkflow, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorMatriculaFuncionario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;     
    public void realizarGravacaoInteracaoWorkflow(List<InteracaoWorkflowVO> listaInteracaoWorkflowVO, Boolean validarDados, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public void incluirSemValidarDados(final InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception;
    public void preencherInteracaoWorkflowSemReferenciaMemoriaBasico(InteracaoWorkflowVO objDestino, InteracaoWorkflowVO objOrigem) throws Exception;
    public void preencherInteracaoWorkflowSemReferenciaMemoriaCompleto(InteracaoWorkflowVO objDestino, InteracaoWorkflowVO objOrigem) throws Exception;
    public void preencherInteracaoWorkflowSemReferenciaMemoriaDadosExclusivosInteracaoEtapa(InteracaoWorkflowVO objDestino, InteracaoWorkflowVO objOrigem, UsuarioVO usuario) throws Exception;
    public void preencherInteracaoWorkflowSemReferenciaMemoriaSemEtapaSemInteracao(InteracaoWorkflowVO objDestino, InteracaoWorkflowVO objOrigem) throws Exception;
    public List<InteracaoWorkflowVO> preencherListaTodasEtapasWorkflow(List<EtapaWorkflowVO> listaEtapas, UsuarioVO usuario) throws Exception;
    public void realizarNavegacaoProximaEtapa(List<InteracaoWorkflowVO> listaInteracoesPercorridasNovas, List<InteracaoWorkflowVO> listaInteracoesPercorridasGravadas, InteracaoWorkflowVO interacaoWorkflowVO, EtapaWorkflowAntecedenteVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public InteracaoWorkflowVO realizarNavegacaoEtapaAnterior(InteracaoWorkflowVO interacaoWorkflowVO, UsuarioVO usuarioLogado) throws Exception;
    public InteracaoWorkflowVO realizarPreenchimentoInteracaoNovoProspect(Integer campanha, UsuarioVO usuario, UnidadeEnsinoVO unidadeEnsinoLogado) throws Exception;
    public void realizarRemarcacaoCompromissoPorInteracaoWorkflow(List<InteracaoWorkflowVO> listaInteracaoWorkflowVO, InteracaoWorkflowVO interacaoWorkflowVO, Date dataCompromissoAdiado, String horaCompromissoAdiado, String horaFimCompromissoAdiado, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;
    public InteracaoWorkflowVO realizarPreenchimentoInteracaoLigacaoAtivaSemAgenda(ProspectsVO prospect, Integer campanha, UsuarioVO usuario, UnidadeEnsinoVO unidadeEnsinoLogado) throws Exception;
    public void preencherInteracaoWorkflowSemReferenciaMemoriaBasicoLigacaoAtivaSemAgenda(InteracaoWorkflowVO objDestino, InteracaoWorkflowVO objOrigem) throws Exception;
    public void alterarObservacao(final InteracaoWorkflowVO obj, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void alteratProspectInteracao(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuario) throws Exception;
    public void excluirInteracaoProspects(Integer codProspect, UsuarioVO usuario) throws Exception;
    public void persistirListaInteracaoWorkflow(List<InteracaoWorkflowVO> listaInteracaoWorkflowVO, Boolean validarDados, UsuarioVO usuarioLogado) throws Exception;
    public void excluirListaInteracaoWorkflowEntidadeOrigem(TipoOrigemInteracaoEnum tipoOrigemInteracao, String identificadorOrigem, Integer codigoEntidadeOrigem, UsuarioVO usuario) throws Exception;
    public InteracaoWorkflowVO consultarPorRenovacaoMatriculaPeloPortalAluno(String matricula, String semestreAno, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}