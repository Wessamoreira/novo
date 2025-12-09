/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.crm;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.enumerador.TipoCompromissoEnum;
import negocio.comuns.crm.enumerador.TipoVisaoAgendaEnum;
import negocio.comuns.utilitarias.ConsistirException;

/**
 *
 * @author edigarjr
 */
public interface AgendaPessoaHorarioInterfaceFacade {

    public void validarDados(AgendaPessoaHorarioVO obj) throws ConsistirException;

    public AgendaPessoaHorarioVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorCodigoAgendaPessoa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorDia(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorMes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public List consultarPorAno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public void incluirAgendaPessoaHorarios(Integer agendaPessoaPrm, List objetos, UsuarioVO usuarioLogado) throws Exception;

    public void alterarAgendaPessoaHorarios(Integer agendaPessoa, List objetos, UsuarioVO usuarioLogado) throws Exception;

    public void excluirAgendaPessoaHorarios(Integer agendaPessoa, UsuarioVO usuarioLogado) throws Exception;
    
    public List<AgendaPessoaHorarioVO> consultarPorMesPorAnoPorAgendaPessoaEspecificaDoMes(Integer agendaPessoa, Integer codigoCampanha, Integer unidadeEnsino, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public AgendaPessoaHorarioVO consultarPorDiaPorMesPorAnoPorAgendaPessoaEspecificaDoMes(Integer agendaPessoa, Integer codigoCampanha, Integer unidadeEnsino, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
    //    public List<AgendaPessoaHorarioVO> consultarPorMesPorAnoPorAgendaPessoaAdministrador(Integer codigoCampanha, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public AgendaPessoaHorarioVO consultarPorDiaMesAnoPorAgendaPessoa(Integer unidadeEnsino, Integer responsavel, Integer codigoCampanha, Date dataFiltro, Boolean prospectsInativo, int nivelMontarDados, TipoCompromissoEnum tipoCompromisso, UsuarioVO usuarioLogado) throws Exception;

    public AgendaPessoaHorarioVO consultarPorDiaMesAnoPorAgendaPessoa(AgendaPessoaHorarioVO agendaPessoaHorario, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

    public AgendaPessoaHorarioVO consultarPorDiaMesAnoPorAgendaPessoaSemCampanha(Integer agendaPessoa, Date dataFiltro, int nivelMontarDados, boolean validarProspects, UsuarioVO usuarioLogado) throws Exception;

    public AgendaPessoaHorarioVO consultarPorDiaMesAnoAdministrador(Integer codigoCampanha, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
    
    public AgendaPessoaHorarioVO consultarPorDiaMesAnoAdministrador(Integer codigoCampanha, Integer unidade, Date dataFiltro, int nivelMontarDados, TipoCompromissoEnum tipoCompromisso, UsuarioVO usuarioLogado) throws Exception;
    
    public void incluir(AgendaPessoaHorarioVO obj, UsuarioVO usuarioLogado) throws Exception ;
    
    public void alterar(AgendaPessoaHorarioVO obj, UsuarioVO usuarioLogado) throws Exception;
    
    public List consultarPorDiaMesAnoAdministradorObterNomeResponsavel(Integer unidadeEnsino, Date dataFiltro, TipoVisaoAgendaEnum tipoVisaoAgendaEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
    
    public List consultarPorDiaMesAnoObterNomeCampanha(Integer codigoPedro, Date dataFiltro, TipoVisaoAgendaEnum tipoVisaoAgendaEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

//    public AgendaPessoaHorarioVO consultarPorDiaMesAnoPorAgendaPessoaRetornandoCodigo(Integer agendaPessoa,Integer codigoCampanha, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
    
    public AgendaPessoaHorarioVO consultarPorDiaMesAnoPorCodigoAgendaPessoa(Integer unidadeEnsino, Integer codigoAgendaPessoa, Integer codigoCampanha, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
    
    public AgendaPessoaHorarioVO consultarAPartirDiaMesAnoPorCodigoProspect(Integer prospect, Integer unidadeEnsino, Integer responsavel, Integer codigoCampanha, Date dataFiltro, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AgendaPessoaHorarioVO> consultarAgendaPessoaJobNotificacao();

	public void atualizarEstatisticasCompromissosAgendaPessoaHorario(AgendaPessoaHorarioVO agendaPessoaHorarioVO);
	
}
