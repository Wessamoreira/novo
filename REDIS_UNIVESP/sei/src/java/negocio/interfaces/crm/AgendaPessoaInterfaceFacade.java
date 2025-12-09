/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.crm;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CalendarioHorarioAulaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.AgendaPessoaVO;
import negocio.comuns.crm.CalendarioAgendaPessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import webservice.servicos.objetos.DataEventosRSVO;

/**
 *
 * @author edigarjr
 */
public interface AgendaPessoaInterfaceFacade {

        public void persistir(AgendaPessoaVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(AgendaPessoaVO obj, UsuarioVO usuarioVO) throws Exception;
    public List consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso,UsuarioVO usuarioLogado) throws Exception;
    public void validarDados(AgendaPessoaVO obj) throws ConsistirException;
    public AgendaPessoaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuarioLogado ) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado ) throws Exception;
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado ) throws Exception;
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuarioLogado ) throws Exception;
    public AgendaPessoaVO consultarPorCodigoPessoa(Integer codigoPessoa , boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void realizarGeracaoCalendarioMes(Boolean administrador, AgendaPessoaVO agendaPessoa, Integer codigoResponsavel, Integer codigoCampanha, Integer unidadeEnsino, Date filtroMes, UsuarioVO usuarioVO) throws Exception;
    public void adicionarObjAgendaPessoaHorarioVOs(AgendaPessoaVO objAgendaPessoaVO, AgendaPessoaHorarioVO obj) throws Exception;
    public void excluirObjAgendaPessoaHorarioVOs(AgendaPessoaVO objAgendaPessoaVO, Integer dia) throws Exception;
    public AgendaPessoaHorarioVO consultarObjAgendaPessoaHorarioVO(AgendaPessoaVO objAgendaPessoaVO, Integer dia) throws Exception;    
    public AgendaPessoaVO realizarValidacaoSeExisteAgendaPessoaParaUsuarioLogado(PessoaVO pessoa, UsuarioVO usuarioLogado) throws Exception;
    public void executarCriacaoAgendaPessoaHorarioDoDia(AgendaPessoaVO agendaPessoa, Date data, UsuarioVO  usuarioVO) throws Exception ;    
    public void realizarBuscaAgendaPessoaHorarioParaAdicionarOrRemoverCompromissoAgendaPessoaHorario(CompromissoAgendaPessoaHorarioVO compromisso, AgendaPessoaVO agendaPessoa, Boolean isAdicionar, UsuarioVO usuarioVO) throws Exception;
    public void executarAtualizacaoContatosPendentes(AgendaPessoaVO agendaPessoa, Integer codigoResponsavel, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;
    public void adicionarCompromissoAgendaPessoaHorarioRealizandoValidacaoSeExisteAgendaHorario(CompromissoAgendaPessoaHorarioVO compromisso, AgendaPessoaVO agendaPessoa, UsuarioVO usuarioVO) throws Exception;
    CalendarioHorarioAulaVO<CalendarioAgendaPessoaVO> realizarGeracaoCalendarioAgendaPessoa(Integer pessoa, Integer unidadeEnsino, Boolean visualizarAgendaGeral, MesAnoEnum mesAno, Integer ano, TipoSituacaoCompromissoEnum tipoSituacaoCompromissoEnum) throws Exception;
    public void atualizarEstatisticasCompromissosAgendaPessoa(AgendaPessoaVO agendaPessoa);
    public void atualizarEstatisticasCompromissosAgendaPessoa(AgendaPessoaVO agendaPessoa, boolean forcarAtualizarAgendaPessoaHorario);
	
	
	
}
