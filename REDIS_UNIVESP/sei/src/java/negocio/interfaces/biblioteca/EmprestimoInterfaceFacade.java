package negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.negocio.comuns.biblioteca.TicketRelVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface EmprestimoInterfaceFacade {

	public EmprestimoVO novo() throws Exception;
	
	public Boolean isNumeroMaximoRenovacoesPessoa(List<ItemEmprestimoVO> itemEmprestimoVOs, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception;

	public void incluir(EmprestimoVO obj, Boolean renovacao, ConfiguracaoBibliotecaVO confPadraoBib,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public EmprestimoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeUsuario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeBiblioteca(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void verificarMatriculaAtivaEmprestimoBiblioteca(EmprestimoVO emprestimoVO,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public List<EmprestimoVO> consultarPorCodigoSituacaoEmExecucao(Integer integer, String valor, boolean b, int nivelmontardadosTodos, UsuarioVO usuario)
			throws Exception;

	public List<EmprestimoVO> consultarPorCodigoBarraExemplar(String valorConsultaEmprestimo, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

	public List<EmprestimoVO> consultarPorTipoPessoa(String valorConsultaEmprestimo,  String valor2, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

	public void inicializarDadosEmprestimoNovo(EmprestimoVO emprestimoVO, UsuarioVO usuario) throws Exception;

	public void verificarNrExemplaresPorPessoa(EmprestimoVO obj, ConfiguracaoBibliotecaVO confPadraoBib, UsuarioVO usuario) throws Exception;

	public List<String> finalizarEmprestimoDevolucao(EmprestimoVO emprestimoVO, String  matricula, List<ItemEmprestimoVO> listaItensEmprestimo, ConfiguracaoBibliotecaVO confPadraoBib, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuario) throws Exception;
//	public String finalizarEmprestimoDevolucao(EmprestimoVO emprestimoVO, List<ExemplarVO> listaExemplaresParaEmprestimo,
//			List<ItemEmprestimoVO> listaItensEmprestimoParaDevolucaoRenovacao, ConfiguracaoBibliotecaVO configuracaoBibliotecaVO,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario)
//			throws Exception;

	public void verificarSeTodosItensEmprestimoForamDevolvidosRenovados(Integer codigoEmprestimo) throws Exception;
	
	public Boolean verificarExisteEmprestimoParaDeterminadoCatalogo(Integer codigoCatalogo, Integer codigoPessoa, Integer codigobiblioteca) throws Exception;
	
//	public List executarConsultaTelaBuscaEmprestimoEmAberto(String valorConsulta, UsuarioVO usuarioLogado, boolean apenasEmprestimosEmAberto) throws Exception;

    public List<EmprestimoVO> consultarAtivosEAtrasados(Integer unidadeEnsino) throws Exception;

    public List<EmprestimoVO> consultarAtrasadosPorBibliotecaSituacaoEmExecucao(Integer codigobiblioteca, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
        public List<EmprestimoVO> consultarAtivosEAtrasadosPorUnidadeEnsinoENivelEducacional(Integer unidadeEnsino, String nivelEducacional) throws Exception;

        public List<EmprestimoVO> consultarAtrasadosPorCursoSituacaoEmExecucao(String nomeCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

        public void realizarCalculoMultaDevolucaoItemEmprestimo(ItemEmprestimoVO itemEmprestimoVO, TipoPessoa tipoPessoa, ConfiguracaoBibliotecaVO configuracaoBibliotecaVO, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception;

        public Double realizarCalculoMultaDevolucaoEmprestimo(List<ItemEmprestimoVO> itemEmprestimoVOs, Integer pessoa, TipoPessoa tipoPessoa, Boolean criacaoContaReceber, ConfiguracaoBibliotecaVO confBibVO, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception;
        
        public List<EmprestimoVO> consultarAtrasadosParaNotificacao(Integer numeroDias,String notificacao ,int nivelMontarDados) throws Exception;
        
        public String realizarCriacaoComprovanteEmprestimo(List<TicketRelVO> listaTicketRelVOs, String unidadeEnsino, EmprestimoVO emprestimoVO, UsuarioVO usuarioVO) throws Exception;

        public Double realizarCalculoIsencaoDevolucaoEmprestimo(List<ItemEmprestimoVO> itemEmprestimoVOs, Integer pessoa, TipoPessoa tipoPessoa, ConfiguracaoBibliotecaVO confBibVO, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception;

		String consultarMatriculaAlunoPorCodigoExemplar(Integer codigoBarra, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

		String consultarMatriculaFuncionarioProfessorPorCodigoExemplar(Integer codigoBarra, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

		String consultarMatriculaFuncionarioProfessorPorCodigoPessoa(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
		
		public EmprestimoVO consultarResumoEmprestimoPorCodigoExemplar(Integer exemplar, UsuarioVO usuario) throws Exception;
		public List<EmprestimoVO> consultarEmprestimoNotificacaoPrazoDevolucao() throws Exception;
		
		public Integer realizarCalculoQtdeEmprestimosAtrasados(List<ItemEmprestimoVO> itemEmprestimoVOs, Integer pessoa, ConfiguracaoBibliotecaVO confBibVO, UsuarioVO usuarioVO) throws Exception;

		/**
		 * @author Wellington Rodrigues - 21/05/2015
		 * @param pessoaAntigo
		 * @param pessoaNova
		 * @throws Exception
		 */
		void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception;

		boolean consultaExistenciaEmprestimosEmAbertoPorMatricula(String matricula, Integer aluno);

		void excluirEmprestimosFinalizadosPorMatricula(String matricula, Integer aluno, UsuarioVO usuarioVO);

		boolean consultaExistenciaVinculoPessoComUnidadeEnsinoBiblioteca(PessoaVO pessoa, String tipoPessoa,
				List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso, UsuarioVO usuario)
				throws Exception;

		Boolean consultarPessoaPossuiExemplarEmprestadoPorCatalogoBiblioteca(CatalogoVO catalogoVO,
				BibliotecaVO bibliotecaVO, PessoaVO pessoaVO) throws Exception;

}