/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.administrativo;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.AdvertenciaVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.InclusaoDisciplinasHistoricoForaPrazoVO;
import negocio.comuns.academico.InteracaoRequerimentoHistoricoVO;
//import negocio.comuns.academico.MapaLocalAulaTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PendenciaLiberacaoMatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.RelatorioFinalFacilitadorVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.AtendimentoInteracaoDepartamentoVO;
import negocio.comuns.administrativo.AtendimentoInteracaoSolicitanteVO;
import negocio.comuns.administrativo.AtendimentoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.biblioteca.ReservaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.ead.AtividadeDiscursivaInteracaoVO;
import negocio.comuns.ead.AtividadeDiscursivaRespostaAlunoVO;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.SalaAulaBlackboardOperacaoVO;
import negocio.comuns.ead.enumeradores.TipoPessoaInteracaoDuvidaProfessorEnum;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;

import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelVO;

/**
 *
 * @author murillo Classe Criada para enviar mensagens automaticas, usando os
 *         templas cadastrados.
 */
public interface GestaoEnvioMensagemAutomaticaInterfaceFacade {

	/**
	 * Este Medoto gerar uma ComunicacaoInternaVO e envia um email ao aluno que
	 * efetuou o emprestimo.
	 * 
	 * @param Emprestimo
	 *            - Objeto do Emprestimo, deve estar com os
	 *            ItensEmprestimo/Exmplares e Pessoa carregados;
	 * @param usuario
	 *            - Usuario Logado
	 * @throws Exception
	 *             - Podera Lançar um Exception para a Camada Chamadora.
	 */
	void executarEnvioMensagemEmprestimoRealizado(final EmprestimoVO emprestimo, UsuarioVO usuario) throws Exception;

	/**
	 * Este Medoto gerar uma ComunicacaoInternaVO e envia um email ao aluno que
	 * efetuou o a Devolucao.
	 * 
	 * @param livros
	 *            - <code> List<ItemEmprestimoVO> </code> que foram Devolvidos
	 * @param pessoa
	 *            - Pessoa que Devolvel os livros. Garantir que o nome e o email
	 *            estao carregados.
	 * @param valorTipoPessoa
	 *            - Tipo Pessoa, podera ser Aluno, Professor, Funcionario. Passa
	 *            valor de 2 digitos de Enum
	 * @param biblioteca
	 *            - Nome da Biblioteca em que os livros foram devolvidos
	 * @param usuario
	 *            - Usuario logado
	 * @throws Exception
	 *             - Podera Lançar um Exception para a Camada Chamadora.
	 */
	void executarEnvioMensagemEmprestimoDevolucao(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String nomeBiblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	/**
	 * Este Medoto gerar uma ComunicacaoInternaVO e envia um email ao aluno que
	 * efetuou o emprestimo.
	 * 
	 * @param livros
	 *            - <code> List<ItemEmprestimoVO> </code> que foram Emprestados
	 *            a Pessoa
	 * @param pessoa
	 *            - Pessoa que pegou os livros emprestado. Garantir que o nome e
	 *            o email estao carregados.
	 * @param valorTipoPessoa
	 *            - Tipo Pessoa, podera ser Aluno, Professor, Funcionario. Passa
	 *            valor de 2 digitos de Enum
	 * @param biblioteca
	 *            - Nome da Biblioteca em que os livros foram emprestados
	 * @param usuario
	 *            - Usuario logado
	 * @throws Exception
	 *             - Podera Lançar um Exception para a Camada Chamadora.
	 */
	void executarEnvioMensagemEmprestimoRealizado(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String nomeBiblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	/**
	 * Este Medoto gerar uma ComunicacaoInternaVO e envia um email ao aluno que
	 * possue livros atrasados
	 * 
	 * @param livros
	 *            - <code> List<ItemEmprestimoVO> </code> que estao em atraso.
	 * @param pessoa
	 *            - Pessoa que pegou os livros emprestado. Garantir que o nome e
	 *            o email estao carregados.
	 * @param valorTipoPessoa
	 *            - Tipo Pessoa, podera ser Aluno, Professor, Funcionario. Passa
	 *            valor de 2 digitos.
	 * @param biblioteca
	 *            - Nome da Biblioteca em que os livros foram emprestados.
	 * @param usuario
	 *            - Usuario logado
	 * @throws Exception
	 *             - Podera Lançar um Exception para a Camada Chamadora.
	 */
	void executaEnvioMensagemLivroAtrasado(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String nomeBiblioteca, UsuarioVO usuario) throws Exception;

	/**
	 * Este Medoto gerar uma ComunicacaoInternaVO e envia um email ao aluno
	 * renovou o emprestimo de livros
	 * 
	 * @param livros
	 *            - <code> List<ItemEmprestimoVO> </code> que foram renovados.
	 * @param pessoa
	 *            - Pessoa que pegou os livros emprestado. Garantir que o nome e
	 *            o email estao carregados.
	 * @param valorTipoPessoa
	 *            - Tipo Pessoa, podera ser Aluno, Professor, Funcionario. Passa
	 *            valor de 2 digitos.
	 * @param biblioteca
	 *            - Nome da Biblioteca em que os livros foram emprestados.
	 * @param usuario
	 *            - Usuario logado
	 * @throws Exception
	 *             - Podera Lançar um Exception para a Camada Chamadora.
	 */
	void executaEnvioMensagemRenovacaoEmprestimo(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String nomeBiblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	/**
	 * Metodo Usado pela JobNotificaçãoAtrasoDevolucao ver
	 * executaEnvioMensagemRenovacaoEmprestimo
	 * 
	 * @param livros
	 * @param pessoa
	 * @param valorTipoPessoa
	 * @param biblioteca
	 * @param usuario
	 * @param config
	 * @param responsavel
	 * @throws Exception
	 */
	void executaEnvioMensagemLivroAtrasadoJob(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String biblioteca, Integer unidadeEnsino, UsuarioVO usuario, ConfiguracaoGeralSistemaVO config, PessoaVO responsavel) throws Exception;

	void executarEnvioMensagemRenegociacaoContaReceberAluno( UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemRenegociacaoContaReceberGrupoDestinatario( GrupoDestinatariosVO grupoDestinatariosVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemRenovacaoMatricula(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemDownloadAntecedenciaMaterial() throws Exception;

//	void executarEnvioMensagemVencimentoContaReceber() throws Exception;

	public void executarEnvioMensagemConsultorMatriculaNaoPaga() throws Exception;

	void executarEnvioMensagemOuvidoriaAbertura(AtendimentoVO atedimento, UsuarioVO usuarioLogado) throws Exception;

	void executarEnvioMensagemOuvidoriaInteracaoSolicitante(AtendimentoVO atendimento, AtendimentoInteracaoSolicitanteVO atendimentoSolicitante, Boolean questionamentoOuvidor, UsuarioVO usuarioLogado) throws Exception;

	void executarEnvioMensagemOuvidoriaInteracaoDepartamento(AtendimentoVO atendimento, AtendimentoInteracaoDepartamentoVO atendimentoDepartamento, Boolean questionamentoOuvidor, UsuarioVO usuarioLogado) throws Exception;

//	void executarEnvioMensagemCobrancaAlunoInadimplenteContaReceberEspecifica(ContaReceberVO contaReceber) throws Exception;
	
	public void executarEnvioMensagemAvisoDesconto(Integer codigoUnidade, Integer codigoConfiguracoes) throws Exception;

	void executarEnvioMensagemFinalizacaoOuvidoria(AtendimentoVO atendimento, AtendimentoInteracaoSolicitanteVO atendimentoSolicitante, UsuarioVO usuarioLogado) throws Exception;

	void executarEnvioMensagemOuvidoriaAtendimentoForaPrazo(AtendimentoVO atendimento, GrupoDestinatariosVO grupo, ConfiguracaoAtendimentoVO configuracao, UsuarioVO usuarioLogado) throws Exception;

	void executarEnvioMensagemOuvidoriaAtendimentoSituacaoAvaliada(String avaliacao, Integer codAtendimento, String motivoAvaliacaoRuim) throws Exception;

	void executarEnvioMensagemAtendimentoDepartamentoForaPrazo(AtendimentoInteracaoDepartamentoVO atendimentoDepartamento, ConfiguracaoAtendimentoVO configuracao, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception;

	void executarEnvioMensagemCobrancaAlunoDocumentacaoPendente() throws Exception;

	void executarEnvioMensagemAlunoFrequenciaBaixa() throws Exception;

	void executarEnvioMensagemInscricaoProcessoSeletivo(InscricaoVO inscricaoVO) throws Exception;

	void executarEnvioMensagemConfirmacaoPagamentoInscricaoProcessoSeletivo(Integer inscricao, Integer unidadeEnsino) throws Exception;

//	void executarEnvioMensagemResultadoProcessoSeletivo(Integer unidadeEnsino) throws Exception;

	void executarEnvioMensagemNovaVagaBancoCurriculum(Boolean enviarSomenteParaCidadeInformada, UsuarioVO usuarioVO, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, List<PessoaVO> pessoaVOs) throws Exception;

//	void executarEnvioMensagemProfessorPostagemMaterial(List<NotificacaoProfessorPostarMaterialVO> notificacaoProfessorPostarMaterialVOs) throws Exception;

	void executarEnvioMensagemNotificacaoNaoLacamentoNota() throws Exception;

	void executarEnvioMensagemAlunoComBoletoAnexo() throws Exception;

	void executarEnvioMensagemAlunoSolicitouAvisoAulaFuturo() throws Exception;

	void executarEnvioMensagemNotificacaoLocalAulaTurma( Integer codGrupoDestinatario, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemRequerimentoEmAtraso() throws Exception;

	void executarEnvioMensagemReposicaoAulaDisponivel(Integer horarioTurma, UsuarioVO usuarioVO) throws Exception;

	void executarEnvioMensagemBuscaProspect( PersonalizacaoMensagemAutomaticaVO mensagemTemplate, ComunicacaoInternaVO comunicacaoEnviar) throws Exception;

	ComunicacaoInternaVO inicializarDadosPadrao(ComunicacaoInternaVO comunicacaoEnviar);

	public void executarEnvioMensagemAniversarianteDia() throws Exception;

	public void executarEnvioMensagemSemTemplate(String assunto, String mensagem, UsuarioVO usuarioVO, List<PessoaVO> pessoaVOs) throws Exception;

	public void executarEnvioMensagemFuncionarioDocumentoIndeferido(DocumetacaoMatriculaVO documentacao, UsuarioVO usuario) throws Exception;

	public void executarEnvioMensagemAlunoDocumentoIndeferido(DocumetacaoMatriculaVO documentacaoMatricula, UsuarioVO usuario) throws Exception;

	public void executarEnvioMensagemAlunoDocumentoPostado(MatriculaVO matricula, List<DocumetacaoMatriculaVO> documentos, UsuarioVO usuario) throws Exception;

	public void executarEnvioMensagemAlunoDocumentoDeferido(MatriculaVO matricula, List<DocumetacaoMatriculaVO> documentos, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemAguardandoAprovacaoOrientador(TrabalhoConclusaoCursoVO tcc, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemAprovacaoOrientador(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemAguardandoAvaliacaoBanca(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemTrabalhoConclusaoCursoAprovado(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemTrabalhoConclusaoCursoReprovado(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemPrimeiroAvisoTCCEmAtraso(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, String tipoDestinatario) throws Exception;

	void executarEnvioMensagemSegundoAvisoTCCEmAtraso(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, String tipoDestinatario) throws Exception;

	void executarEnvioMensagemReprovacaoAutomaticaPorAtrasoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception;

	void executarEnvioMensagemTramiteRequerimento(RequerimentoVO obj, RequerimentoHistoricoVO historico, UsuarioVO usuarioLogadoVO) throws Exception;
	
	void executarEnvioMensagemTramiteInteracaoTramiteRequerimento(RequerimentoVO obj, RequerimentoHistoricoVO historico, InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistoricoVO,UsuarioVO usuarioLogadoVO) throws Exception;
	
	void executarEnvioMensagemTramiteRequerimentoAtendente(RequerimentoVO obj, RequerimentoHistoricoVO historico, UsuarioVO usuarioLogadoVO) throws Exception;

	void executarEnvioMensagemNotificacaoProcessoSeletivo(Integer processoSeletivo, Integer unidadeEnsino, Integer itemProcSeletivoDataProva, Integer sala,   UsuarioVO usuarioVO) throws Exception;

	void executarEnvioMensagemNotificacaoProcessoSeletivoLembreteDataProva() throws Exception;

	public void executarEnvioMensagemCobrancaAlunoInadimplenteSegundoConfiguracaoFinanceira() throws Exception;

	void executarEnvioMensagemConfirmacaoMatricula(MatriculaVO matriculaVO,  PersonalizacaoMensagemAutomaticaVO mensagemTemplate ,UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemAprovacaoProcessoSeletivo(  UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoAbandonoCurso();

	void executarEnvioMensagemRegistroAbandonoCurso(MatriculaPeriodoVO matriculaPeriodoVO);

	public void executarEnvioMensagemReservaDisponivel(CatalogoVO catalogo, BibliotecaVO biblioteca, ReservaVO reserva, PessoaVO pessoa, String valorTipoPessoa, PessoaVO responsavel, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemAtualizacaoDisciplinaTurmaRealizado(TurmaVO turma, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemCancelamentoMatricula(PessoaVO pessoa, MatriculaVO matricula, UsuarioVO usuario) throws Exception;

//	void executarEnvioMensagemCoordenadorInicioTurma(List<NotificacaoProfessorPostarMaterialVO> notificacaoProfessorPostarMaterialVOs) throws Exception;

	void executarEnvioMensagemCobrancaAlunoInadimplentePeriodico(Integer codigoUnidade, Integer codigoConfiguracoes) throws Exception;

	void executarEnvioMensagemCobrancaAlunoInadimplente(Integer codigoUnidade, Integer codigoConfiguracoes) throws Exception;

	public void executaEnvioMensagemNotificacaoPrazoDevolucao(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String biblioteca, UsuarioVO usuario, ConfiguracaoGeralSistemaVO config, PessoaVO responsavel, Integer unidadeEnsino) throws Exception;

	public void executarEnvioMensagemReservaCancelada(CatalogoVO catalogo, BibliotecaVO biblioteca, ReservaVO reserva, PessoaVO pessoa, String valorTipoPessoa, PessoaVO responsavel, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemPostagemMaterial(ConfiguracaoGeralSistemaVO config, ArquivoVO arquivo) throws Exception;

	void executarEnvioConsultorContatosDia() throws Exception;

	public void executarEnvioMensagemTrabalhoConclusaoCursoEncaminhadoOrientador(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception;

	public void executarEnvioMensagemTrabalhoConclusaoCursoEncaminhadoOrientadorFormatacao(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception;

	public void executarEnvioMensagemTrabalhoConclusaoCursoEncaminhadoOrientadorConteudo(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception;

	public void executarEnvioMensagenAdvertenciaResponsavelLegalAluno(AdvertenciaVO advertenciaVO, UsuarioVO usuarioVO) throws Exception;

	void executarEnvioMensagemAcessoConteudoEstudo(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemAcessoAvaliacaoOnline(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemPeriodoMaximoConclusaoCurso(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemPeriodoMaximoConclusaoDisciplinas(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacao1AtrasoEstudosAluno(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacao2AtrasoEstudosAluno(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacao3AtrasoEstudosAluno(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoDiasAntesConclusaoAtividadeDiscursiva(SqlRowSet resultSet, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoDiasSemLogar(SqlRowSet resultSet, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoNovaInteraçaoAtividadeDiscursiva(AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoAlunoAvaliadoAtividadeDiscursiva(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoSolicitarAvaliacaoProfessorAtividadeDiscursiva(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoSolicitarNovaRespostaAlunoAtividadeDiscursiva(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoDuvidasNaoRespondidasNoPrazoProfessor(SqlRowSet notificacoesDuvidasNaoRespondidas, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoGrupoDestinatarioDuvidasNaoRespondidasNoPrazoProfessor(SqlRowSet notificacoesDuvidasNaoRespondidas, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoNovaDuvidaProfessorCriada(Integer codigoDuvidaProfessorVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoSolicitacaoRespostaAlunoDuvidaProfessor(Integer codigoDuvidaProfessorVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoSolicitacaoRespostaProfessorDuvidaProfessor(Integer codigoDuvidaProfessorVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoFinalizacaoDuvidaProfessor(Integer codigoDuvidaProfessorVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNotificacaoNovaInteracaoDuvidaProfessor(Integer codigoDuvidaProfessorVO, TipoPessoaInteracaoDuvidaProfessorEnum tipoPessoaInteracaoDuvidaProfessorEnum, UsuarioVO usuario) throws Exception;

//	public void executarEnvioMensagemConvocacaoEnade( List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception;
	
	public void executarEnvioMensagemAlunosBaixaFrequencia() throws Exception;

	/**
	 * @author Wellington - 31 de ago de 2015
	 * @param pessoa
	 * @param enviarCopiaPais
	 * @return
	 * @throws Exception
	 */
	List<ComunicadoInternoDestinatarioVO> obterListaDestinatarios(PessoaVO pessoa, boolean enviarCopiaPais) throws Exception;

	/** 
	 * @author Wellington - 11 de fev de 2016 
	 * @param matriculaPeriodoVO
	 * @param usuario
	 * @throws Exception 
	 */
	void executarEnvioMensagemNotificacaoInclusaoDisciplina(MatriculaVO matriculaVO, List<InclusaoDisciplinasHistoricoForaPrazoVO> inclusaoDisciplinasHistoricoForaPrazoVOs, UsuarioVO usuario) throws Exception;
	
	public void executarEnvioMensagemNotificacaoVencimentoInscricaoCandidato();

	void executarEnvioMensagemPeriodoMaximoAtividadeDiscursiva(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemNovaAtividadeDiscursiva(AtividadeDiscursivaVO atividdadeDiscursiva, UsuarioVO usuario)
			throws Exception;

	void executarEnvioMensagemNotificacaoRespondenteAvaliacaoInstitucionalEspecifica(
			AvaliacaoInstitucionalVO avaliacaoInstitucionalVO,
			List<AvaliacaoInstitucionalAnaliticoRelVO> avaliacaoInstitucionalAnaliticoRelVOs,
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate, UsuarioVO usuarioVO) throws Exception;
	
	void executarEnvioMensagemDownloadMaterialAlunosPeriodoAula(SqlRowSet rs, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemRequerimentoSolicitacaoIsencaoTaxaDeferido(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, RequerimentoVO requerimentoVO, UsuarioVO usuario) throws Exception;
	
	public void executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoAprovador(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, MatriculaPeriodoVO matriculaPeriodovo, Boolean solicitarLiberacaoFinanceira, Boolean solicitarLiberacaoMatriculaAposInicioXModulos, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	public void executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoAluno(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, MatriculaPeriodoVO matriculaPeriodovo, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	public void executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoDeferido(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, MatriculaPeriodoVO matriculaPeriodovo, PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	public void executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoIndeferido(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, MatriculaPeriodoVO matriculaPeriodovo, PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemGsuiteCriacaoConta(PessoaGsuiteVO pessoaGsuite, UsuarioVO usuario) throws Exception;
	
//	void enviarMensagemAvisoErroPagamentoCartaoCredito(ContaReceberVO contaReceberVO, UsuarioVO usuarioVO) throws Exception;
//	void enviarMensagemAvisoSucessoPagamentoCartaoCredito(ContaReceberVO contaReceberVO, UsuarioVO usuarioVO) throws Exception;
	void executarEnvioMensagemNotificacaoSalaAulaBlackboardOperacao(SalaAulaBlackboardOperacaoVO operacao, TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioLiberado(MatriculaVO matricula, MatriculaPeriodoVO matPer, UsuarioVO usuario) throws Exception;

	String obterMensagemFormatadaMensagemAlunosEstagioLiberado(MatriculaVO matricula, String mensagemTemplate);

	void executarEnvioMensagemTccLiberado(MatriculaVO matricula, MatriculaPeriodoVO matPer, UsuarioVO usuario) throws Exception;

    String obterMensagemFormatadaMensagemAlunosTccLiberado(MatriculaVO matricula, String mensagemTemplate);

    void executarEnvioMensagemEstagioObrigatorioDeferimento(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioAproveitamentoDeferimento(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioEquivalenciaDeferimento(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioIndeferimento(EstagioVO obj, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioAproveitamentoIndeferimento(EstagioVO obj, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioEquivalenciaIndeferimento(EstagioVO obj, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioAproveitamentoSolicitacaoCorrecaoAluno(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioEquivalenciaSolicitacaoCorrecaoAluno(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioSolicitacaoCorrecaoAluno(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void enviarMensagemAtivacaoPreMatricula(MatriculaVO mat, String ano , String semestre ,PersonalizacaoMensagemAutomaticaVO mensagemTemplate, UsuarioVO usuario) throws Exception;
	void enviarMensagemCancelamentoPreMatricula(MatriculaVO mat, MatriculaPeriodoVO matPer, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioNotificacaoAguardandoAssinatura(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioNotificacaoDeCancelamentoPorFaltaDeAssinatura(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemEstagioObrigatorioAvisoPrazoAnaliseAvaliador(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemAnaliseEstagioObrigatorioRelatorioFinal(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemPeriodoEntregaAnaliseAproveitamentoEncerrado(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	void executarEnvioMensagemPeriodoEntregaAnaliseEquivalenciaEncerrado(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception;
	
	public void executarEnvioMensagenAdvertenciaAluno(AdvertenciaVO advertenciaVO, UsuarioVO usuarioVO) throws Exception;

	void realizarNotificacaoAlunoEscolhaGrupoProjetoIntegracao(
			TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, SalaAulaBlackboardVO salaAulaBlackboardVO,
			SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, UsuarioVO usuarioVO) throws Exception;

	void enviarMensagemNotificacaoRegistroFormatura(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO,UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemSituacaoRelatorioFacilitador(List<RelatorioFinalFacilitadorVO> listaRelatorioFacilitadorVOs, String situacao, UsuarioVO usuarioVO) throws Exception;
	
	String obterMensagemFormatadaMensagemRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, String situacao, String mensagemTemplate);

	void executarEnvioMensagemNotificacaoSupervisorRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception;

	void executarEnvioMensagemTrancamentoMatricula(PessoaVO pessoa, MatriculaVO matricula, UsuarioVO usuario) throws Exception;

	void executarEnvioMensagemJubilamentoMatricula(PessoaVO pessoa, MatriculaVO matricula, UsuarioVO usuario) throws Exception;
}
