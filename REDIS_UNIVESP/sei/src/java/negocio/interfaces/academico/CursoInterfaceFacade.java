package negocio.interfaces.academico;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.academico.Disciplina;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.servicos.CursoObject;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface CursoInterfaceFacade {

    public CursoVO novo() throws Exception;

    public void incluir(CursoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void alterar(CursoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public void excluir(CursoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

    public CursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, boolean montarListaGradeCurricularNivelDadosBasicos, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorNrNivelEducacional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorNome(String valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorNrRegistroInterno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List<CursoVO> consultaRapidaPorCodigoAreaConhecimento(Integer codigo, PeriodicidadeEnum periodicidadeEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorNomeCurso_UnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPeriodoLetivoPorGradeCurricular(String prm, Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorNomeCursoUnidadeEnsino(String valorConsulta, Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorCodigoProfessor(Integer professor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public CursoVO consultarCursoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void excluirObjGradeCurricularVOs(GradeCurricularVO obj, CursoVO cursoVO, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Boolean validarUnidadeLogada, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNrRegistroInterno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNrNivelEducacional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void carregarDados(CursoVO obj, UsuarioVO usuario) throws Exception;

    public void carregarDados(CursoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorCodigoNivelEducacionalUnidadeEnsino(Integer valorConsulta, String nivelEducacional, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorNomeUnidadeEnsinoNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public CursoVO consultarPorCodigoUnidadeEnsinoCurso(Integer codigo, boolean b, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception;

    public void excluirPeriodosLetivosGradeCurricular(GradeCurricularVO obj, CursoVO cursoVO, UsuarioVO usuario) throws Exception;

    public CursoVO consultarCursoPorTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorCodigoUnidadeEnsino(Integer codigoUnidadeEnsino, String periodicidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorCodigoUnidadeEnsinoProcSeletivo(Integer codigoUnidadeEnsino, Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorCodigoUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorCodigoUnidadeEnsinoTurno(Integer unidadeEnsino, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultar(String campoConsultaCurso, String valorConsultaCurso, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorCodigoEUnidadeEnsino(Integer valorConsulta, Integer unidadeDeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs(String nome, String periodicidade, String nivelEducacional, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNomeEUnidadeDeEnsino(String valorConsultaCurso, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List<CursoVO> consultaRapidaPorCodigoEUnidadeDeEnsino(Integer valorConsultaCurso, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<CursoVO> consultarPorNomeAreaConhecimentoNivelEducacional(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, String nivelEducacional, UsuarioVO usuario) throws Exception;

    void validarReconhecimentoCurso(AutorizacaoCursoVO autorizacaoCursoVO, CursoVO cursoVO) throws Exception;

    public List<CursoVO> consultaRapidaPorCodigoCursoUnidadeEnsino(Integer codigoCurso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNomeCursoUnidadeEnsino(String nomeCurso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public CursoVO consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public String consultarNivelEducacionalPorTurma(Integer turma);

    public List<TipoNivelEducacional> consultarNivelEducacionalProfessor(UsuarioVO usuarioVO) throws Exception;

    public String consultarNivelEducacionalPorCodigoUsuario(Integer usuario);

    public List<String> consultarListaNiveisEducacionaisPorCodigoUsuario(Integer usuario);

    public CursoVO consultarCursoPorMatriculaParaInicializarNotaRapida(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorProfessor(String nome, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarPorCodigoCursoProfessor(Integer codigoCurso, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, Integer codigoUnidadeEnsino, int limit, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<CursoVO> consultarPorCodigoEspecifico(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Boolean apresentarCursoBiblioteca) throws Exception;

    List<CursoVO> consultarPorNomeEpecifico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Boolean apresentarCursoBiblioteca) throws Exception;

    List<CursoVO> consultarCursoPorCodigoUnidadeEnsino(Integer codigoCurso, Integer unidadeEnsinoCodigo, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    List<CursoVO> consultarPorNomeCursoUnidadeEnsinoBasica(String valorConsulta, Integer unidadeEnsinoCodigo, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorCodigoPorCodigoUnidadeEnsinoCondicaoRenegociacao(Integer codigo, List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNomePorCodigoUnidadeEnsinoCondicaoRenegociacao(String nome, List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoUnidadeEnsinoVOs, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<String> consultarListaNivelEducacionalCursoPorCodigoPessoaCoordenador(Integer pessoa);

    public List<CursoVO> consultaCursoDoProfessor(Integer professor, Integer unidadeEnsino, boolean consultarTurmasEAD, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarTodosCursos();

    public List<CursoVO> consultaRapidaPorCodigoCursoProcessoMatricula(Integer valorConsulta, Integer processoMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNomeCursoProcessoMatricula(String valorConsulta, Integer processoMatricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public List<String> consultarNivelEducacionalPorTurmaAgrupada(Integer turmaAgrupada, UsuarioVO usuarioVO);
    
    public String consultarNivelEducacionalPorTurmaAgrupadaUnico(Integer turmaAgrupada, UsuarioVO usuarioVO);
    
    public Boolean consultarLiberarRegistroAulaEntrePeriodoPorTurma(Integer turma, UsuarioVO usuarioVO);

    public List<CursoVO> consultarListaCursoPorCodigoPessoaCoordenador(Integer pessoa, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
    
    public List<CursoVO> consultarCursoPorProfessorNivelDadosCombobox(Integer codigoPessoa, String semestre, String ano, String situacao, Integer areaConhecimento, Integer unidadeEnsino, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, PeriodicidadeEnum periodicidadeEnum, UsuarioVO usuario) throws Exception;
    public List<CursoVO> consultarNotasPorCurso(Integer codigoCurso)  throws Exception;

	List<CursoVO> consultarPorNomeCursoProfessor(String nomeCurso, Integer professor, Integer unidadeEnsino, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<CursoVO> consultarListaCursoPorNomeCursoCodigoPessoaCoordenador(String nomeCurso, Integer pessoa, Integer unidadeEnsino, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	CursoVO consultarCursoPorTurmaPrincipalSubturma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<CursoVO> consultaRapidaPorNomeEUnidadeDeEnsinoENivelEducacional(String valorConsultaCurso, List<UnidadeEnsinoVO> lista,Boolean infantil,Boolean basico,Boolean medio,Boolean extensao,Boolean sequencial,Boolean graduacaoTecnologica,Boolean superior,Boolean posGraduacao,Boolean profissionalizante, Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	CursoObject consultarPorChavePrimariaMatriculaExterna(Integer codigoCurso) throws Exception;

	/**
	 * Responsável por realizar a consulta de cursos das turmas que fazem parte do agrupamento com base na turma origem.
	 * 
	 * @author Wellington - 9 de dez de 2015
	 * @param turmaOrigem
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<CursoVO> consultarCursoTurmasAgrupadasPorTurmaOrigem(Integer turmaOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 11/04/2016
	 * @param unidadeEnsinoVOs
	 * @return
	 */
	List<CursoVO> consultarTodosCursosPorUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs);
	
	String consultarPeriodicidadePorMatricula(String matricula, UsuarioVO usuario) throws Exception;
	
	List<CursoVO> consultarCursoApresentarProcessoSeletivoPorNome(String valorConsulta, Integer unidadeEnsinoCodigo,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	
	/**
	 * 
	 * Consulta CursoVO pelo nome do curso e o id da Unidade de Ensino no qual o curso tem algum vínculo
	 * 
	 * @author gilberto.nery
	 * @param nomeDoCurso
	 * @param idUnidadeEnsino
	 * @param nivelMontarDados
	 * @param montarListaGradeCurricularNivelDadosBasicos
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public CursoVO consultarPorNomeDoCursoEUnidadeDeEnsino(String nomeDoCurso, Integer idUnidadeEnsino , int nivelMontarDados, boolean montarListaGradeCurricularNivelDadosBasicos, UsuarioVO usuario) throws Exception;

	List<CursoVO> consultaRapidaPorUnidadeEnsinoNivelEducacional(String nomeCurso, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception; 
	
    public List<CursoVO> consultarCursoApresentarBiblioteca(String campoConsultaCurso, String valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

	List<CursoVO> consultaRapidaPorNomePorUnidadeEnsinoPorNivelEducacional(String valorConsulta, Integer unidadeEnsino, TipoNivelEducacional tipoNivelEducacional, DataModelo dataModelo) throws Exception;

    List<CursoVO> consultaRapidaPorNomePorListaUnidadeEnsinoPorNivelEducacional(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinos, TipoNivelEducacional tipoNivelEducacional, DataModelo dataModelo) throws Exception;

    List<CursoVO> consultaRapidaPorCodigoPorUnidadeEnsinoPorNivelEducacional(Integer valorConsulta, Integer unidadeEnsino, TipoNivelEducacional tipoNivelEducacional, DataModelo dataModelo) throws Exception;

	List<CursoVO> consultarCursosPossuemGradeCurricularComTipoAtividadeComplementar();

	List<CursoVO> consultarListaCursoPorCodigoCursoCodigoPessoaCoordenador(Integer codigoCurso, Integer pessoa, Integer unidadeEnsino, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CursoVO> consultarPorArtefatoEntregaAluno(Integer artefato, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultaRapidaPorNomeEPeriodicidade(String periodicidadeArtefato, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Boolean validarUnidadeLogada, UsuarioVO usuario) throws Exception;
	
    List<CursoVO> consultaRapidaPorNomePorCodigoUnidadeEnsinoTurno(String nome, Integer unidadeEnsino, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<CursoVO> consultaRapidaPorCodigoPorCodigoUnidadeEnsinoTurno(Integer codigo, Integer unidadeEnsino, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<CursoVO> consultarCursoPorNivelEducacionalEUnidadeEnsinoVOs(String nivelEducacional, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception;
	
	public void alterarPeriodicidadeCurso(final CursoVO obj, UsuarioVO usuario) throws Exception;
	
	public List<CursoVO> consultaRapidaPorNomeCursoUnidadeEnsinoNivelEducacional(String nomeCurso, Integer unidadeEnsino,  String nivelEducacional,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;
		
	public List<CursoVO> consultarPorCodigoCoordenador(Integer valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer codigoCoordenador) throws Exception;

	public List<CursoVO> consultarPorNomeCoordenador(String valorConsulta, Integer unidadeEnsinoCodigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer codigoCoordenador) throws Exception;

	void consultarCursoDataModelo(DataModelo dataModelo, UsuarioVO usuarioVO, PeriodicidadeEnum periodicidadeEnum);
	List<CursoVO> consultarCursoAtividadeExtraClasseProfessor(int codigoFuncionarioCargo);

	CursoVO consultarPorNome(String nomeDoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	List<CursoVO> consultarCursoPorCoordenador(int codigoPessoaCoordenador);

    public List<CursoVO> consultarPorUnidadeEnsino(String campoConsulta, String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public List<CursoVO> consultaRapidaPorNomeEUnidadeDeEnsino(String valorConsultaCurso, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorUnidadesEnsinos(List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	
	List<CursoVO> consultarCursoPorNomePeriodicidadeNivelEducacionalEUnidadeEnsinoVOs(String nome, String periodicidade, String nivelEducacional ,List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception;

	List<CursoVO> consultarCursoPorCodigoPeriodicidadeNivelEducacionalEUnidadeEnsinoVO(Integer codigo, String periodicidade, String nivelEducacional ,Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	public List<CursoVO> consultarCursoPorNomePeriodicidadeNivelEducacionalEUnidadeEnsinoVO(String nome, String periodicidade, String nivelEducacional ,Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	public List<CursoVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, int limit, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CursoVO> consultarPorCodigo(Integer valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CursoVO> consultarPorNome(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CursoVO> consultarCursoPorUnidadeEnsinoNivelEducacional(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<TipoNivelEducacional> tipoNivelEducacional, UsuarioVO usuario) throws Exception;

    public List<CursoVO> consultarCursoPorDisciplina(Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void consultaControleConsultaOtimizadoCurso(DataModelo dataModelo, Integer unidadeEnsino, TipoNivelEducacional tipoNivelEducacional) throws Exception;
}
