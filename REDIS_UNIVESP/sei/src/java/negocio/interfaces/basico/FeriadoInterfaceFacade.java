package negocio.interfaces.basico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import webservice.servicos.objetos.DataEventosRSVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface FeriadoInterfaceFacade {
	

    public FeriadoVO novo() throws Exception;
    public void incluir(FeriadoVO obj, UsuarioVO usuarioVO, boolean excluirAulaProgramada) throws Exception;
    public void alterar(FeriadoVO obj, UsuarioVO usuarioVO, boolean excluirAulaProgramada) throws Exception;
    public void excluir(FeriadoVO obj, UsuarioVO usuarioVO) throws Exception;
    public FeriadoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorAno(String valorConsulta, String considerarFeriado, String cidade, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorData(Date data, String considerarFeriado, Integer cidade, boolean controlarAcesso, int nivelMontarDado, UsuarioVO usuarios) throws Exception;
    public List consultarPorPeriodo(Date dataini, Date datafim, String considerarFeriado, String cidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public Integer consultarNrDiasUteisProgredir(Date dataInicial, Integer diasPrevisto, Integer cidade, boolean considerarDiaInicialContagemDiasUteis, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception ;
    public Integer consultarNrDiasUteis(Date dataInicial, Date dataFinal, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception ;
    public Integer consultarNrDiasUteisConsiderandoSabado(Date dataInicial, Date dataFinal, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception ;
    public Integer consultaNumeroFeriadoNoPeriodoDesconsiderandoFimSemana(Date dataInicio,Date dataFim, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception ;
    public List<DataEventosRSVO> consultaDiasFeriadoNoPeriodoPorDataEventos(Date dataInicio, Date dataFim, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum, int nivelMontaDados, UsuarioVO usuario) throws Exception;
    public List<FeriadoVO> consultaDiasFeriadoNoPeriodo(Date dataInicio, Date dataFim, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum, int nivelMontaDados, UsuarioVO usuario) throws Exception ;
    public void setIdEntidade(String aIdEntidade);
    public List consultarTodosFeriados(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<TurmaVO> executarValidarNaoPossuiAulaProgramada(FeriadoVO feriad, UsuarioVO usuarioo) throws Exception;public List consultarPorDescricao(String valorConsulta, String considerarFeriado, String cidade, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	FeriadoVO executarValidacaoDataFeriado(List<FeriadoVO> feriadoVOs, Date data);
        
    public Boolean verificarFeriadoNesteDia(Date data, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum, Boolean controlarAcesso, UsuarioVO usuario) throws Exception; 
    
	
    public Date obterDataFuturaProximoDiaUtil(Date dataInicial, Integer cidade, Boolean considerarSabado, Boolean considerarFeriado, ConsiderarFeriadoEnum considerarFeriadoEnum, UsuarioVO usuarioVO) throws Exception;
	Date obterDataFuturaOuRetroativaApenasDiasUteis(Date dataInicial, int nrDiasProgredir, Integer cidade, Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil,  ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception;
	boolean validarDataSeVesperaFimDeSemana(Date data, Integer cidade, Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception;
	Integer calcularNrDiasUteis(Date dataInicio, Date dataFim, Integer cidade, Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception;
	
	public boolean isDiaUtil(final Date date, Integer cidade,  Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception; 
	
}