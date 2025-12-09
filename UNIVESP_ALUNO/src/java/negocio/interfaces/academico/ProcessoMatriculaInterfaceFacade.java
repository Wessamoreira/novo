package negocio.interfaces.academico;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.servicos.ProcessoMatriculaRSVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface ProcessoMatriculaInterfaceFacade {
	

    public ProcessoMatriculaVO novo() throws Exception;
    public void incluir(ProcessoMatriculaVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(ProcessoMatriculaVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(ProcessoMatriculaVO obj, UsuarioVO usuario) throws Exception;
    public ProcessoMatriculaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProcessoMatriculaVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProcessoMatriculaVO> consultarPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProcessoMatriculaVO> consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProcessoMatriculaVO> consultarSomentePelaDataFimProcessoMatricula(Date prmIni, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;
    public void setIdEntidade(String aIdEntidade);
	public List<ProcessoMatriculaVO> consultarPorNomeUnidadeEnsino_Ativo(String string, Integer codigo, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;
	public List<ProcessoMatriculaVO> consultaRapidaPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public List<ProcessoMatriculaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception;
	public SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codigoPrm, UsuarioVO usuario) throws Exception;
	public SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigoPrm, UsuarioVO usuario) throws Exception;
	public void carregarDados(ProcessoMatriculaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;
	public void carregarDados(ProcessoMatriculaVO obj, UsuarioVO usuario) throws Exception;
	public List<ProcessoMatriculaVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public List<ProcessoMatriculaVO> consultaRapidaPorDataInicio(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public List<ProcessoMatriculaVO> consultaRapidaPorDataFinal(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public List<ProcessoMatriculaVO> consultaRapidaPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
        public void adicionarProcessoMatriculaCalendario(ProcessoMatriculaVO obj, ProcessoMatriculaCalendarioVO processoMatriculaCalendario, CursoTurnoVO cursoTurnoVO, UsuarioVO usuario) throws Exception;
        public void adicionarObjProcessoMatriculaCalendarioVOs(ProcessoMatriculaVO obj, ProcessoMatriculaCalendarioVO processoMatriculaCalendario, boolean editarProcessoMatriculaCalendarioVO) throws Exception;

        public List<ProcessoMatriculaVO> consultaRapidaPorNomeUnidadeEnsinoESituacao(String valorConsulta, Integer unidadeEnsino, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception ;
        public void alterarSituacaoProcessoMatricula(ProcessoMatriculaVO processoMatricula, String situacao, UsuarioVO usuario) throws Exception;
        public List<ProcessoMatriculaVO> consultaRapidaPorUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(Integer turno, Integer curso, Integer unidadeEnsino, String situacaoPeriodoLetivoAtivoUnidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
        public void alterarSituacaoProcessoMatricula(ProcessoMatriculaVO processoMatriculaVO, Integer unidadeEnsino, String situacaoProcessoMatriculaBanco,  UsuarioVO usuario) throws Exception;
        public void ativarPreMatriculaPeriodo(ProcessoMatriculaVO processoMatriculaVO, Integer unidadeEnsino,  UsuarioVO usuario) throws Exception;
        public ProcessoMatriculaVO consultaRapidaPorMatriculaPeriodo(Integer matriculaPeriodo, UsuarioVO usuarioVO) throws Exception;
		List<ProcessoMatriculaVO> consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(Integer turno, Integer curso, Integer unidadeEnsino, String situacaoPeriodoLetivoAtivoUnidadeEnsinoCurso, String situacao, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO, TipoAlunoCalendarioMatriculaEnum tipoAluno) throws Exception;
		Boolean verificarPossibilidadeMatriculaOnline(Integer codigoBanner, Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoProcessoMatricula, Integer codigoGradeCurricular, Integer codigoTurma, Map<String, Integer> curso, UsuarioVO usuarioVO) throws Exception;
		List<ProcessoMatriculaVO> consultarProcessosMatriculasPorCodigoBanner(Integer codigoBanner, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
		public Integer consultaRapidaPorCodigoTurmaAtivoEntrePeriodo(Integer turma, UsuarioVO usuarioVO, TipoAlunoCalendarioMatriculaEnum tipoAluno) throws Exception;		
		public void alterarPeriodoProcessoMatricula(ProcessoMatriculaVO processoMatriculaVO, UsuarioVO usuario) throws Exception;
		String consultarAnoSemestrePorProcessoMatriculaUnidadeEnsinoCurso(Integer processoMatricula, Integer unidadeEnsinoCurso);
		public List<ProcessoMatriculaVO> consultarProcessosMatriculaOnline(Boolean apresentarVisaoAluno, Boolean matriculaOnline ,Integer unidadeEnsino,String ano,String semestre,Integer curso,Integer turno, UsuarioVO usuarioVO, Integer limit, Boolean trazerUltimoCalendarioAtivo,int nivelMontarDados ,Boolean validarTurmaMatriculaOnline, Boolean transferenciaInterna) throws Exception;
		List<ProcessoMatriculaRSVO> consultarProcessosMatriculaOnline(Integer unidadeEnsino, String ano,String semestre, Integer curso, Integer turno, Boolean validarTurmaMatriculaOnline,
				UsuarioVO usuarioVO);
	    public List<ProcessoMatriculaVO> consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(Integer turno, Integer curso, Integer unidadeEnsino, String situacaoPeriodoLetivoAtivoUnidadeEnsinoCurso, String situacao, boolean visaoAluno, boolean controlarAcesso, int nivelMontarDados, String matricula, UsuarioVO usuarioVO, TipoAlunoCalendarioMatriculaEnum tipoAluno) throws Exception;

		public List consultarProcessoMatriculaPorUnidadeEnsino(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, Boolean renovandoMatricula, Boolean existeRegraRenovacaoPorProcessoMatriculaTurma, UsuarioVO usuarioVO,  Boolean transferenciaInterna) throws Exception;

		public void realizarInclusaoProcessoMatriculaDeAcordoComMatriculaPeriodo(MatriculaPeriodoVO obj, List<ProcessoMatriculaVO> listaProcessoMatriculaVOs, UsuarioVO usuarioVO) throws Exception;

}