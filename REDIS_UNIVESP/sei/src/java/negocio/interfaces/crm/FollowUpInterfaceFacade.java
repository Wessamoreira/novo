package negocio.interfaces.crm;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.FamiliaresVO;
import negocio.comuns.crm.FollowUpVO;
import negocio.comuns.crm.HistoricoFollowUpVO;
import negocio.comuns.crm.InteracaoWorkflowHistoricoVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.enumerador.TipoOrigemInteracaoEnum;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface FollowUpInterfaceFacade {
	

//    public void persistir(FollowUpVO obj) throws Exception;
//    public void excluir(FollowUpVO obj) throws Exception;
//    public void validarDados(FollowUpVO obj) throws ConsistirException;
    public void setIdEntidade(String aIdEntidade);
    public List consultaMontarComboboxWorkflow(Integer codigoProspect);
    public List consultaMontarComboboxCampanha(Integer codigoProspect);
    public List consultaMontarComboboxResponsavel(Integer codigoProspect);
    public List consultaMontarComboboxCurso(Integer codigoProspect);
    public FollowUpVO consultarFollowUpPorCodigoProspect(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public Integer consultarTotalDeRegistroInteracoes(Integer codigoWorkflow, Integer codigoCampanha, Integer codigoResponsavel, Integer codigoCurso, Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarInteracoes(Integer codigoWorkflow, Integer codigoCampanha, Integer codigoResponsavel, Integer codigoCurso, Integer limite, Integer offset, FollowUpVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<InteracaoWorkflowVO> consultarInteracoes(Integer codigoWorkflow, Integer codigoCampanha, Integer codigoResponsavel, Integer codigoCurso, TipoOrigemInteracaoEnum tipoOrigem, String identificadorOrigem, Integer codigoEntidadeOrigem, Integer limite, Integer offset, FollowUpVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void adicionarObjHistoricoFollowUpVOs(FollowUpVO objFollowUpVO, HistoricoFollowUpVO obj) throws Exception;    
    public HistoricoFollowUpVO consultarObjHistoricoFollowUpVO(FollowUpVO objFollowUpVO, String observacao) throws Exception;
    public AgendaPessoaVO realizarValidacaoAgendaFollowUp(PessoaVO pessoaVO, UsuarioVO usuario) throws Exception;
    public AgendaPessoaHorarioVO realizarValidacaoAgendaPessoaExiste(AgendaPessoaVO agenda, AgendaPessoaHorarioVO agendaPessoaHorario, CompromissoAgendaPessoaHorarioVO compromisso, UsuarioVO usuario) throws Exception;
    public FollowUpVO consultarFollowUpPorCodigoPessoa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public Integer consultarCodigoProspectPorCodigoPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	public List<FamiliaresVO> consultarFamilires(FollowUpVO followUpVO) throws Exception;
	List<InteracaoWorkflowHistoricoVO> consultarInteracaoWorkflowPorAlunoFichaAluno(Integer pessoa, Integer responsavel, String mesAno, UsuarioVO usuarioVO);
	List<InteracaoWorkflowHistoricoVO> consultarHistoricoFollowUpPorAlunoFichaAluno(Integer pessoa, Integer responsavel, String mesAno, UsuarioVO usuarioVO);
	List<SelectItem> consultarMesAnoInteracaoWorkflowPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO);
	void excluirObjHistoricoFollowUpVOs(FollowUpVO objFollowUpVO, HistoricoFollowUpVO historicoFollowUpVO, UsuarioVO usuarioVO) throws Exception;
	
	boolean verificarExistenciaInteracaoWorkflowPorProspectsMatricula(Integer workflow, Integer campanha, Integer responsavel, Integer curso, TipoOrigemInteracaoEnum tipoOrigem, String identificadorOrigem, Integer codigoEntidadeOrigem, Integer prospects, String matricula) throws Exception;

}