package negocio.interfaces.crm;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.HistoricoFollowUpVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface HistoricoFollowUpInterfaceFacade {
	
    public void incluir(final HistoricoFollowUpVO obj, UsuarioVO usuarioVO) throws Exception ;
    public void excluir(final HistoricoFollowUpVO obj, UsuarioVO usuarioVO) throws Exception ;
    public void validarDados(HistoricoFollowUpVO obj) throws ConsistirException;
    public HistoricoFollowUpVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List<HistoricoFollowUpVO> consultarTodosHistoricosFollowUps(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigoFollowUp(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorObservacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorDataregistro(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorMatriculaFuncionario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;    
    public void excluirTodosHistoricosFollowUps(Integer followup, UsuarioVO usuario) throws Exception;    
    public void alterarProspectHistoricoFollowUp(final Integer codProspectManter, final Integer codProspectRemover, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void incluirHistoricoFollowUpPorComunicadoInterno(PessoaVO pessoaVO, ComunicacaoInternaVO comunicacaoInternaVO, UsuarioVO usuarioVO) throws Exception;
    public void excluirHistoricoFollowUps(Integer followup, UsuarioVO usuario) throws Exception;	
}