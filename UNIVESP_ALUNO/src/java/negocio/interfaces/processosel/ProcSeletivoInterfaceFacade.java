package negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.processosel.ProcSeletivoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ProcSeletivoInterfaceFacade {

	public ProcSeletivoVO novo() throws Exception;

	public void incluir(ProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterar(ProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(ProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception;

	public ProcSeletivoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ProcSeletivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;	

	public List<ProcSeletivoVO> consultarPorUnidadeEnsinoUnidadeEnsinoCurso(Integer unidadeEnsino, Integer unidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public ProcSeletivoVO consultarCodigo(Integer intValue, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

	public void inicializarProcSeletivoSemAgendamento(ProcSeletivoVO procSeletivoVO);

        public List<ProcSeletivoVO> consultarPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        public List<ProcSeletivoVO> consultarPorUnidadeEnsinoLogado(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        public String consultarDisciplinasProcSeletivoString(ProcSeletivoVO procSeletivoVO) ;
        
        public void validarDadosMediaMinimaAprovacao(ProcSeletivoVO procSeletivoVO) throws Exception;

        List<ProcSeletivoVO> consultarProcessoSeletivoAntesDataProvaPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        List<ProcSeletivoVO> consultarProcessoSeletivoAposDataProvaPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		List<ProcSeletivoVO> consultarPorUnidadeEnsinoAptoInscricao(Integer unidadeEnsino, boolean visaoCandidato, boolean controlarAcesso, Integer codigoCurso, Integer codigoTurno , String tipoProcSeletivo ,int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public List<ProcSeletivoVO> consultarPorDescricaoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public List<ProcSeletivoVO> consultarPorDataInicioUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public List<ProcSeletivoVO> consultarPorDataFimUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		List<ProcSeletivoVO> consultarPorDataInicioInternetUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		List<ProcSeletivoVO> consultarPorDataFimInternetUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public ProcSeletivoVO consultarUltimoProcessoSeletivo(int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public List<ProcSeletivoVO> consultarUltimosProcessosSeletivos(int quantidade, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public StringBuilder SQLrealizarClassificacaoCandidatoProcessoSeletivo(Integer processoSeletivo, Integer unidadeEnsinoCurso);
		
		public List<ProcSeletivoVO> consultaRapidaComboBoxPorDescricaoUnidadeEnsino(String descricao, Integer unidadeEnsino, UsuarioVO usuarioVO);
		
		public List<ProcSeletivoVO> consultaRapidaComboBoxProcessoSeletivoFaltandoLista(List<ProcSeletivoVO> procSeletivoVOs, UsuarioVO usuario) throws Exception;
		
		/**
		 * @author Carlos Eugênio - 16/12/2016
		 * @param unidadeEnsino
		 * @param quantidade
		 * @param controlarAcesso
		 * @param nivelMontarDados
		 * @param usuario
		 * @return
		 * @throws Exception
		 */
		List<ProcSeletivoVO> consultarPorUnidadeEnsinoUltimosProcessosSeletivos(Integer unidadeEnsino, Integer quantidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
		public List<ProcSeletivoVO> consultarPorDataProvaUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		

		public List<ProcSeletivoVO> consultarPorUnidadeEnsinoCursoTurnoTipoProcessoSeletivo(Integer unidadeEnsino,
				Integer curso, Integer turno, String tipoProcessoSeletivo, boolean controlarAcesso,
				int nivelMontarDados, UsuarioVO usuario) throws Exception;

		ProcSeletivoVO consultarPorDescricaoExata( boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		ProcSeletivoVO inicializarDadosProcSeletivoImportacaoCandidatoInscricao( UsuarioVO usuario) throws Exception;

		Integer verificarQuantidadeAlunosMatriculadosPorProcessoSeletivoUnidadeEnsino(Integer procSeletivo,	Integer unidadeEnsino,Integer eixoCurso,  UsuarioVO usuario) throws Exception;

		void removerProcSeletivoUnidadeEnsinoTodas(ProcSeletivoVO procSeletivoVO, UsuarioVO usuarioVO) throws Exception;

		void adicionarCursoTurnoGeral(ProcSeletivoVO procSeletivoVO, CursoTurnoVO cursoTurnoVO, UsuarioVO usuarioVO)
				throws Exception;

}