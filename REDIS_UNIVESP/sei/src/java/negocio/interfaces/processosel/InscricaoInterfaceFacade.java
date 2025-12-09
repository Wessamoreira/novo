package negocio.interfaces.processosel;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.EmailVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoAtaProvaRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscritoSalaRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoRedacaoRelVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface InscricaoInterfaceFacade {

	public InscricaoVO novo() throws Exception;

	public void incluir(InscricaoVO obj, ArquivoVO arquivoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public void alterar(InscricaoVO obj, ArquivoVO arquivoVO, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception;

	public void excluir(InscricaoVO obj, UsuarioVO usuario) throws Exception;

	public void liberarPagamentoInscricao(InscricaoVO obj, UsuarioVO usuarioResponsavel) throws Exception;

	public void liberarInscricaoForaPrazo(InscricaoVO obj, UsuarioVO usuarioResponsavel) throws Exception;

	public InscricaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public InscricaoVO consultarPorCandidatoEProcessoSeletivo(Integer unidadeEnsino , Integer codigoCandidato, Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva,Integer codigoInscricao,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomePessoa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCPFPessoa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoInscricao(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDescricaoProcSeletivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoProcSeletivoCodigoAlunoComprovanteInscricao(Integer processoSeletivo, Integer aluno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void criarUsuarioVO(InscricaoVO obj, UsuarioVO usuario) throws Exception;

	public InscricaoVO consultarPorInscricaoCPF(Integer codigoPrm, String cpfPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void gravarNumeroContaReceber(InscricaoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void montarContaReceber(InscricaoVO inscricao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario, Boolean liberarPagamento) throws Exception;

	public ConfiguracaoFinanceiroVO consultarConfiguracaoFinanceiro(UsuarioVO usuario, Integer codigoUnidadeEnsino) throws Exception;

	public PerfilAcessoVO consultarPerfilAcessoPadrao(Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	public void alterarSituacaoFinanceira(Integer codigo, String situacao, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	public void incluir(final InscricaoVO obj, ArquivoVO arquivoVO, boolean verificarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public void alterar(final InscricaoVO obj, ArquivoVO arquivoVO, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception;

	public InscricaoVO consultarPorCPFPessoaUnico(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public InscricaoVO consultarPorInscricaoUnico(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultaCompletaPorCodigoInscricao(Integer inscricao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultaRapidaPorCodigoInscricao(Integer inscricao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultaRapidaPorCodigoInscricaoCpf(Integer inscricao, String cpf, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public InscricaoVO consultaRapidaInscricaoUnicaPorCodigoInscricaoCpf(Integer inscricao, String cpf, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<InscricaoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception;

	public List<InscricaoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception;

	public void carregarDados(InscricaoVO obj, UsuarioVO usuario) throws Exception;

	public void carregarDados(InscricaoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void validarDadosUnicidadeCandidatoCurso(Integer curso1, Integer curso2, Integer curso3, Integer pessoa ,Boolean permitirAlunosMatriculadosInscreverMesmoCurso) throws Exception;

	public List<InscricaoVO> consultarCanditadoPorCodigoProcSeletivo(ProcSeletivoVO processoSeletivo, Integer dataProva,Integer codigoCurso, Integer sala, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	Boolean consultarSeExisteInscricaoParaPessoa(Integer codigoPessoa) throws Exception;

	InscricaoVO consultarUltimaInscricaoPessoa(Integer codigoPessoa,Integer codigoInscricao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	InscricaoVO consultarDadosEnvioNotificacaoEmail(Integer inscricao);

	Integer consultarNumeroInscritosConfirmadosProcessoSeletivo(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva) throws Exception;

	Integer consultarNumeroInscritosConfirmadosSemSalaProcessoSeletivo(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva) throws Exception;

	Integer consultarNumeroInscritosConfirmadosNecessidadesEspeciaisProcessoSeletivo(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva) throws Exception;

	List<ProcessoSeletivoInscritoSalaRelVO> consultarQtdeInscritosPorSala(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Boolean trazerInscricaoSemSala) throws Exception;

	void realizarDistribuicaoSalaProcessoSeletivo(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Boolean agruparPorNecessidadesEspeciais, String formaDistribuicao, Boolean distribuirCandidatosSemSala, List<SalaLocalAulaVO> salaLocalAulaVOs, UsuarioVO usuarioVO) throws Exception;

	List<InscricaoVO> consultaRapidaPorSalaProcSeletivoData(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Integer sala) throws Exception;

	void alterarSalaInscricao(Integer inscricao, Integer sala, UsuarioVO usuario) throws Exception;

	List<InscricaoVO> consultaRapidaParaImpressaoEtiqueta(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Integer sala, Integer inscricao) throws Exception;

	String realizarImpressaoEtiquetaInscricaoProcessoSeletiva(LayoutEtiquetaVO layoutEtiqueta, List<InscricaoVO> inscricaoVOs, Integer numeroCopias, Integer linha, Integer coluna, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	InscricaoVO consultarDadosParaResultadoProcessoSeletivo(Integer inscricao, Integer procSeletivo, Integer itemProcSeletivoDataProva, Integer sala);

	public List<SalaLocalAulaVO> consultarSalaPorProcessoSeletivo(Integer procSeletivo, Integer unidadeEnsino, Integer unidadeEnsinoCurso, UsuarioVO usuarioVO);

	Integer consultarNumeroInscritosSalaEspecifica(Integer codigoProcessoSeletivo, Integer itemProcSeletivoDataProva, Integer salaLocalAula) throws Exception;

	List<SalaLocalAulaVO> consultarSalaPorProcessoSeletivoEDataAula(Integer procSeletivo, Integer itemProcSeletivoDataProva);

	List consultarPorCodigoUnidadeEnsino(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List consultarPorNomePessoaUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List consultarPorDescricaoProcSeletivoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List consultarPorNomeCursoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List consultarPorDataUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List consultarPorSituacaoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	int consultarChamadaProcSeletivo(ProcSeletivoVO procSeletivoVO, Integer curso, Integer unidadeEnsino) throws Exception;

	List<Integer> consultarNumeroChamada(ProcSeletivoVO procSeletivoVO, Integer curso, Integer unidadeEnsino) throws Exception;

	boolean verificarExisteInscricaoVinculadaProcSeletivoUnidadeEnsinoCurso(Integer procSeletivo, Integer unidadeEnsinoCurso, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<InscricaoVO> consultarCanditadoPorCodigo(PessoaVO candidato, Integer dataProva, Integer sala, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void alterarSituacaoInscricaoNaoCompareceu(final InscricaoVO inscricao, UsuarioVO usuarioVO) throws Exception;

	public void atualizarSituacaoInscricao(final InscricaoVO inscricao, final SituacaoInscricaoEnum situacao, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington - 28 de ago de 2015
	 * @param candidato
	 * @param situacaoInscricao
	 * @param usuarioVO
	 * @throws Exception
	 */
	void alterarSituacaoPorCandidato(String numeroInscricaoCandidato, String situacaoInscricao, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington - 8 de set de 2015
	 * @param aluno
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	List<InscricaoVO> executarMontagemInscricaoVOs(Integer aluno, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public InscricaoVO gravarInscricaoCandidato(InscricaoVO inscricaoVO, ArquivoVO arquivoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroPadraoSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	public Boolean consultarAlteracaoDadosJaGravadosEtapa1PorCodigoInscricao(Integer inscricao, Integer unidadeEnsino, Integer procSeletivo, Integer cursoOpcao1, Date dataProva, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 3 de dez de 2015 
	 * @param inscricaoVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void executarCriacaoVinculoInscricaoComGabarito(InscricaoVO inscricaoVO, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Wellington - 3 de dez de 2015 
	 * @param inscricaoVO
	 * @param usuarioVO
	 * @throws Exception 
	 */
	void executarCriacaoVinculoInscricaoComProvaProcessoSeletivo(InscricaoVO inscricaoVO, UsuarioVO usuarioVO) throws Exception;

	public List<ProcessoSeletivoAtaProvaRelVO> consultaInscricaoProcessoSeletivoAtaProvaVOs(Integer procSeletivo, Integer sala, ItemProcSeletivoDataProvaVO dataProva, Integer qtdLinhas, String ordenarPor, UsuarioVO usuarioVO) throws Exception;	
	
	public List<InscricaoVO> consultaInscricaoPendenteParaNotificacao() throws Exception;
	
	public void realizarRegistroNotificacaoVencimentoInscricao(final Integer inscricao, UsuarioVO usuarioVO) throws Exception;
	
	public List<ProcessoSeletivoRedacaoRelVO> consultaInscricaoProcessoSeletivoRedacaoVOs(Integer procSeletivo, Integer unidadeEnsino, Integer curso, Integer sala, ItemProcSeletivoDataProvaVO dataProva, Integer qtdeLinhas, String ordenarPor, Boolean ocultaLinha, String texto, Integer inscricao, UsuarioVO usuarioVO) throws Exception;	
		
	public List consultarPorNomePessoa(String valorConsulta, Integer unidadeEnsino, Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List consultarPorCPFPessoa(String valorConsulta, Integer unidadeEnsino, Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List consultarPorCodigo(Integer valorConsulta, Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
	public List<InscricaoVO> consultarCandidatoNotificacaoAlteracaoDataProva(ProcSeletivoVO processoSeletivo, ItemProcSeletivoDataProvaVO itemProcessoSeletivoDataProva,int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public Integer consultarQuantidadeIncricaoVinculadaItemProcessoSeletivoDataProva(Integer itemProcessoSeletivoDataProva) throws Exception;
	
	List<InscricaoVO> inicializarDadosInscricaoProcessoSeletivoFichaAluno(Integer aluno, UsuarioVO usuarioVO) throws Exception;
	
	List<InscricaoVO> consultarInscricaoNaoCompareceuProcessamentoResultadoProcessoSeletivo(List<Integer> listaInscricaoArquivoVOs, Integer procSeletivo, Integer itemProcSeletivoDataProva, Integer sala);

	void realizarAlteracaoInscricaoNaoCompareceu(List<InscricaoVO> listaInscricaoVOs, UsuarioVO usuarioVO) throws Exception;
	public Boolean existeResultadoProcessamentoRespostaPorGabarito(GabaritoVO gabaritoVO) throws Exception;
	
	public List<InscricaoVO> consultarInscricao(Integer processoSeletivo, Integer unidadeEnsino, String numeroInscricao, String nomeCandidato, Date dataInicioInscricao, Date dataFimInscricao, Date dataInicioProva, Date dataFimProva, Boolean filtroSituacaoInscricaoConsultaAtivo, Boolean filtroSituacaoInscricaoConsultaCancelouOutraInscricao, Boolean filtroSituacaoInscricaoConsultaNaoCompareceu, Boolean filtroSituacaoInscricaoConsultaCancelado, Integer limite, Integer pagina, boolean controlarAcesso, Integer nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public Integer totalConsultaInscricao(Integer processoSeletivo, Integer unidadeEnsino, String numeroInscricao, String nomeCandidato, Date dataInicioInscricao, Date dataFimInscricao, Date dataInicioProva, Date dataFimProva, Boolean filtroSituacaoInscricaoConsultaAtivo, Boolean filtroSituacaoInscricaoConsultaCancelouOutraInscricao, Boolean filtroSituacaoInscricaoConsultaNaoCompareceu, Boolean filtroSituacaoInscricaoConsultaCancelado, Integer limite, Integer pagina, boolean controlarAcesso, Integer nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	void validarArquivoInscricao(InscricaoVO inscricaoVO, ArquivoVO arquivoVO) throws Exception;

	public InscricaoVO consultarPorCodigoCertidaoNascimentoCPF(Integer codigoInscricao, String cpf,
			String certidaoNascimento, int nivelmontardadosDadosbasicos, UsuarioVO usuarioVO)throws Exception  ;
	
	public InscricaoVO consultarUltimaInscricaoPessoaProvaOnlinePorNumeroDocumento(String tipoDocumento, String numeroDocumento,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void enviarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(InscricaoVO inscricaoVO, Integer codigoAutenticacao, String meioAutenticacao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean controlarAcesso,  Boolean codigoAutenticacaoExpirada , Boolean apenasAlterar, UsuarioVO usuario) throws Exception;
	
	public void validarCodigoAutenticacaoLiberacaoProvaProcessoSeletivo(InscricaoVO inscricaoVO , Integer codigoAutenticacao, UsuarioVO usuario) throws Exception;
	
	public void alterarDataAutenticacaoLiberacaoProvaProcessoSeletivo(InscricaoVO inscricaoVO, Date data, UsuarioVO usuario) throws Exception;
	
	public void aceitarTermosAceiteProvaProcessoSeletivo(InscricaoVO inscricaoVO) throws Exception;
	
	public void alterarDataHoraInicioProvaProcessoSeletivo(InscricaoVO inscricaoVO,UsuarioVO usuario) throws Exception;
	
	public void alterarDataHoraTerminoProvaProcessoSeletivo(InscricaoVO inscricaoVO, UsuarioVO usuario) throws Exception;
	
	public void validarApresentacaoResultadoProcessoSeletivo(InscricaoVO inscricaoVO);
	
	public void validarPrazoRealizacaoProvaProcessoSeletivo(InscricaoVO inscricaoVO) throws Exception;
	
	public void validarPrazoRealizacaoProvaProcessoSeletivoOnline(InscricaoVO inscricaoVO, String tipoRequisicao ,String mensagemErro) throws Exception;
	
	public void validarDadosProvaProcessoSeletivo(InscricaoVO inscricaoVO, Boolean apresentarResultadoProcessoSeletivo) throws Exception;
	
	public Date consultarDataHoraTerminoInscricao(Integer codigoInscricao) throws Exception;
	
	public void consultarInscricaoProvaProcessoSeletivoEmRealizacao() throws Exception;
	
	public List<EmailVO> enviarCodigosAutenticacaoProvaOnlineDiaAtual(boolean controlarAcesso) throws Exception;

	public void alterarCodigoAutenticacaoNavegador(InscricaoVO inscricaoVO,UsuarioVO usuario) throws Exception;

	public InscricaoVO validarSessaoNavegadorProvaOnline(InscricaoVO inscricaoVO, String codigoautenticacaoNavegador, String codigoautenticacaoNavegadorAtualizar , Boolean validarSessao , UsuarioVO usuarioVO) throws Exception;
	
	
	public InscricaoVO validarSessaoNavegadorHomeCandidatoProcessoSeletivoRetornandoCodigoAutenticacaoNavegador(Integer codigoInscricao, String codigoautenticacaoNavegador, String navegador , Boolean validarSessao , String action , UsuarioVO usuarioVO) throws Exception;

	public void validarInscricaoAptoApresentarProvaProcessoSeletivoResultadoProcessoSeletivo(InscricaoVO inscricaoVO ,String tipoRequisicao) throws Exception;

	public InscricaoVO consultarUltimaInscricaoAtivaDentroPrazoDiferenteAtualSemResultadoPorPessoa(Integer codigoPessoa,
			 boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<InscricaoVO> montarDadosComprovanteConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception;

	InscricaoVO inicializarDadosInscricaoImportacaoCandidato(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, ProcSeletivoVO procSeletivoVO, ItemProcSeletivoDataProvaVO itemProcessoSeletivoDataProvaVO, PessoaVO candidatoVO, UsuarioVO usuarioVO) throws Exception;

	InscricaoVO consultarPorProcSeletivoECPF(Integer procSeletivo, String cpf, Integer unidadeEnsino, Integer curso, Integer turno, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public InscricaoVO consultarInscricaoAptaMatricula(String tipoDocumento, String numeroDocumento ,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
		
	public List<InscricaoVO> consultarInscricoesPessoa(Integer codigoPessoa, Integer codigoInscricao ,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<InscricaoVO> consultarInscricaoPessoaProvaOnlinePorNumeroDocumento(String tipoDocumento, String numeroDocumento,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void validarPrazoAlteracaoProcessoSeletivo(InscricaoVO inscricaoVO) throws Exception;

	void validarEmailTelefoneCandidato(InscricaoVO inscricaoVO, String meioAutenticacao, String valorValidarCandidato)	throws Exception;

	public void alterarNumeroChamadaInscricao(InscricaoVO inscricaoVO, UsuarioVO usuarioVO)throws Exception;
	
}
