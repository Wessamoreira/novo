package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
//import negocio.comuns.financeiro.CondicaoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import webservice.servicos.TurnoRSVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface TurnoInterfaceFacade {

    public TurnoVO novo() throws Exception;

    public void incluir(TurnoVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

    public void alterar(TurnoVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

    public void excluir(TurnoVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

    public TurnoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<TurnoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<TurnoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNome(String valorConsulta, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public TurnoVO consultarTurnoPorMatricula(String matricula, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;


    public String realizarConsultaHorarioInicioFim(TurnoVO obj, DiaSemana diaSemana, Integer nrAula) ;

    public void inicializarDadosListaTurnoHorarioVO(TurnoVO turnoVO, Integer numeroAula, DiaSemana diaSemana, Integer duracaoAula, Boolean permiteAlterar) throws Exception ;

    public void realizarMontagemHorarios(TurnoVO obj);

    public TurnoHorarioVO consultarObjTurnoHorarioVOs(TurnoVO obj,DiaSemana diaSemana, Integer nrAula) ;

    public Integer consultarNumeroAulaTurnoHorarioVOs(TurnoVO obj,DiaSemana diaSemana) ;
    
//    public List consultarPorUnidadeEnsinoCondicaoRenegociacao(List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoRenegociacaoUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<TurnoVO> consultarPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    List<TurnoVO> consultarPorCodigoCursos(List<CursoVO> cursos, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<TurnoVO> consultarPorProfessor(Integer codigoProfessor, int nivelMontarDados) throws Exception;
    
    public List<TurnoVO> consultarPorCodigoCursoUnidadeEnsino(Integer curso, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public TurnoVO consultarTurnoPorMatriculaPeriodoUnidadeEnsinoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Boolean consultarTransferenciaTurnoPorMatricula(String matricula, UsuarioVO usuarioVO);

	Integer realizarConsultaDuracaoHorarioInicioFim(TurnoVO obj, DiaSemana diaSemana, Integer nrAula);

	TurnoVO consultarTurnoPorCodigoTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public TurnoVO consultaRapidaPorMatricula(String matricula, UsuarioVO usuarioVO);

	void realizarCriacaoHorarioTurnoFixo() throws Exception;

	List<TurnoVO> consultarTurnoPorCodigoBanner(Integer codigoBanner, Integer codigoUnidadeEnsino, UsuarioVO usuarioVO) throws Exception;
	
	public List<TurnoVO> consultarTurnoPorProcSeletivoUnidadeEnsinosComboBox(List<ProcSeletivoVO> procSeletivoVOs,List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 14/03/2016
	 * @param unidadeEnsinoVOs
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 */
	List<TurnoVO> consultarTurnoUsadoMatricula(List<UnidadeEnsinoVO> unidadeEnsinoVOs, int nivelMontarDados) throws Exception;
	
	List consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	TurnoVO consultarTurnoPorNomeUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	 public TurnoVO consultarPorChavePrimaria(Integer chavePrimaria, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	TurnoVO consultarPorChavePrimariaUnico(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	TurnoRSVO consultarTurnoMatriculaOnlineProcessoSeletivoPorCodigo(Integer codigoTurno, UsuarioVO usuario);
}
