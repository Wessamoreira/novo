/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package negocio.facade.jdbc.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.AdvertenciaVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.InclusaoDisciplinasHistoricoForaPrazoVO;
import negocio.comuns.academico.InteracaoRequerimentoHistoricoVO;
import negocio.comuns.academico.MapaLocalAulaTurmaVO;
import negocio.comuns.academico.MaterialAlunoVO;
import negocio.comuns.academico.MaterialProfessorVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PendenciaLiberacaoMatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.RelatorioFinalFacilitadorVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRelatorioFacilitadorEnum;
import negocio.comuns.administrativo.AtendimentoInteracaoDepartamentoVO;
import negocio.comuns.administrativo.AtendimentoInteracaoSolicitanteVO;
import negocio.comuns.administrativo.AtendimentoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoAtendimentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioGrupoDestinatariosVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TipoDestinatarioComunicadoInternaEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.biblioteca.ReservaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.crm.AgendaPessoaHorarioVO;
import negocio.comuns.crm.BuscaProspectVO;
import negocio.comuns.ead.AtividadeDiscursivaInteracaoVO;
import negocio.comuns.ead.AtividadeDiscursivaRespostaAlunoVO;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.ead.SalaAulaBlackboardOperacaoVO;
import negocio.comuns.ead.enumeradores.InteragidoPorEnum;
import negocio.comuns.ead.enumeradores.TipoPessoaInteracaoDuvidaProfessorEnum;
import negocio.comuns.estagio.ConfiguracaoEstagioObrigatorioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.job.NotificacaoProfessorPostarMaterialVO;
import negocio.comuns.job.NotificacaoRegistroAulaNaoLancadaVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.protocolo.RequerimentoDisciplinaVO;
import negocio.comuns.protocolo.RequerimentoDisciplinasAproveitadasVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeMatriculaVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.GestaoEnvioMensagemAutomaticaInterfaceFacade;
import relatorio.negocio.comuns.academico.AlunoBaixaFrequenciaRelVO;
import relatorio.negocio.comuns.academico.AlunoComFrequenciaBaixaRelVO;
import relatorio.negocio.comuns.academico.DebitoDocumentosAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelVO;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;
import relatorio.negocio.comuns.financeiro.ContaReceberCobrancaRelVO;
import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;
import relatorio.negocio.jdbc.academico.FiltroAlunoBaixaFrequenciaVO;
import relatorio.negocio.jdbc.financeiro.BoletoBancarioRel;

/**
 * 
 * @author murillo Classe Criada para enviar mensagens automaticas, usando os templas cadastrados.
 */
@Repository
@Scope("singleton")
@Lazy
public class GestaoEnvioMensagemAutomatica extends ControleAcesso implements GestaoEnvioMensagemAutomaticaInterfaceFacade {

	/**
	 *
	 */
	private static final long serialVersionUID = 4955877389142927168L;

	/**
	 * Este Medoto gera uma ComunicacaoInternaVO e envia um email ao aluno que efetuou o emprestimo.
	 * 
	 * @param Emprestimo
	 *            - Objeto do Emprestimo, deve estar com os ItensEmprestimo/Exmplares e Pessoa carregados;
	 * @param usuario
	 *            - Usuario Logado
	 * @throws Exception
	 *             - Podera Lançar um Exception para a Camada Chamadora.
	 */
	@Override
	public void executarEnvioMensagemConvocacaoEnade(final List<MapaConvocacaoEnadeMatriculaVO> listaConvocadosIngressantes, List<MapaConvocacaoEnadeMatriculaVO> listaConvocadosConcluintes, List<UnidadeEnsinoVO> unidadeEnsinoVOs, MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO,  UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONVOCACAO_ENADE, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuario, unidadeEnsinoVOs);
		if(mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			throw new Exception("Para que a notificação seja enviada para os alunos convocados, é necessário que a mensagem NOTIFICAÇÃO CONVOCAÇÃO ENADE, seja habilitada na tela PERSONALIZAÇÃO MENSAGEM AUTOMÁTICA.");
		}
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(mapaConvocacaoEnadeVO.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			PessoaVO resposavel = usuarioVO.getPessoa();
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			for (MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO : listaConvocadosIngressantes) {
				// Para obter a mensagem do email formatado Usamos um metodo a
				// parte.
				String mensagemEditada = obterMensagemFormatadaMensagemConvocacaoEnade(mapaConvocacaoEnadeMatriculaVO, mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaMensagemConvocacaoEnade(mapaConvocacaoEnadeMatriculaVO, mensagemTemplate.getMensagemSMS());
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setResponsavel(resposavel);
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getAluno(), mensagemTemplate.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("AL");
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
			}
			for (MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO : listaConvocadosConcluintes) {
				// Para obter a mensagem do email formatado Usamos um metodo a
				// parte.
				String mensagemEditada = obterMensagemFormatadaMensagemConvocacaoEnade(mapaConvocacaoEnadeMatriculaVO, mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaMensagemConvocacaoEnade(mapaConvocacaoEnadeMatriculaVO, mensagemTemplate.getMensagemSMS());
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setMensagem(mensagemEditada);
				comunicacaoEnviar.setResponsavel(resposavel);
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getAluno(), mensagemTemplate.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("AL");
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
			}
		}

	}

	@Override
	public void executarEnvioMensagemEmprestimoRealizado(final EmprestimoVO emprestimoEnviar, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_EMPRESTIMO_REALIZADO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, emprestimoEnviar.getUnidadeEnsinoVO().getCodigo(), usuario, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			PessoaVO resposavel = (getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarFuncionarioPadraoEnvioNotificacaoEmail(usuario)).getPessoa();
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			// Para obter a mensagem do email formatado Usamos um metodo a
			// parte.
			String mensagemEditada = obterMensagemFormatadaMensagemEmprestimoRealizado(emprestimoEnviar, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemEmprestimoRealizado(emprestimoEnviar, mensagemTemplate.getMensagemSMS());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(resposavel);
			comunicacaoEnviar.setData(emprestimoEnviar.getData());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(emprestimoEnviar.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(emprestimoEnviar.getTipoPessoa());
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}

	}

	@Override
	public void executarEnvioMensagemCancelamentoMatricula(final PessoaVO pessoa, MatriculaVO matricula, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_CANCELAMENTO_MATRICULA, false, matricula.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemCancelamentoMatricula(pessoa, matricula, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemCancelamentoMatricula(pessoa, matricula, mensagemTemplate.getMensagemSMS());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(pessoa.getTipoPessoa());
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}

	}

	@Override
	public void executarEnvioMensagemAtualizacaoDisciplinaTurmaRealizado(TurmaVO turma, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ATUALIZACAO_DISCIPLINA_REALIZADO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, turma.getUnidadeEnsino().getCodigo(), usuario, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());

			PessoaVO responsavel = new PessoaVO();
			turma.setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(turma.getUnidadeEnsino().getCodigo(), usuario));			
			responsavel.setEmail(turma.getUnidadeEnsino().getEmail());
			responsavel.setNome(turma.getUnidadeEnsino().getNome());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			// Para obter a mensagem do email formatado Usamos um metodo a
			// parte.
			String mensagemEditada = obterMensagemFormatadaMensagemAtualizacaoDisciplinaTurmaRealizado(turma, responsavel, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemAtualizacaoDisciplinaTurmaRealizado(turma, responsavel, mensagemTemplate.getMensagemSMS());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(responsavel, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("FU");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	/**
	 * Este Medoto gerar uma ComunicacaoInternaVO e envia um email ao aluno que efetuou o renegociacao conta receber.
	 * 
	 * @param Emprestimo
	 *            - Objeto do Emprestimo, deve estar com os ItensEmprestimo/Exmplares e Pessoa carregados;
	 * @param usuario
	 *            - Usuario Logado
	 * @throws Exception
	 *             - Podera Lançar um Exception para a Camada Chamadora.
	 */
	@Override
	public void executarEnvioMensagemRenegociacaoContaReceberAluno(NegociacaoContaReceberVO negociacaoContaReceberVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RENEGOCIACAO_CONTA_RECEBER_ALUNO, false, negociacaoContaReceberVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			// Para obter a mensagem do email formatado Usamos um metodo a
			// parte.
			String mensagemEditada = obterMensagemFormatadaMensagemRenegociacaoContaReceberAluno(negociacaoContaReceberVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemRenegociacaoContaReceberAluno(negociacaoContaReceberVO, mensagemTemplate.getMensagemSMS());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(usuario.getPessoa());
			comunicacaoEnviar.setData(negociacaoContaReceberVO.getData());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(negociacaoContaReceberVO.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(negociacaoContaReceberVO.getTipoPessoa());
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}

	}

	@Override
	public void executarEnvioMensagemRenegociacaoContaReceberGrupoDestinatario(NegociacaoContaReceberVO negociacaoContaReceberVO, GrupoDestinatariosVO grupoDestinatariosVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RENEGOCIACAO_CONTA_RECEBER_GRUPO_DESTINATARIO, false, negociacaoContaReceberVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && grupoDestinatariosVO != null && !grupoDestinatariosVO.getListaFuncionariosGrupoDestinatariosVOs().isEmpty()) {
			for (FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO : grupoDestinatariosVO.getListaFuncionariosGrupoDestinatariosVOs()) {
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				// Para obter a mensagem do email formatado Usamos um metodo a
				// parte.
				String mensagemEditada = obterMensagemFormatadaMensagemRenegociacaoContaReceberGrupoDestinatario(negociacaoContaReceberVO, funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa(), mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaMensagemRenegociacaoContaReceberGrupoDestinatario(negociacaoContaReceberVO, funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa(), mensagemTemplate.getMensagemSMS());
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setMensagem(mensagemEditada);
				comunicacaoEnviar.setResponsavel(usuario.getPessoa());
				comunicacaoEnviar.setData(negociacaoContaReceberVO.getData());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario(negociacaoContaReceberVO.getTipoPessoa());
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
			}
		}

	}

	@Override
	public void executarEnvioMensagemRenovacaoMatricula(MatriculaVO matriculaVO, UsuarioVO usuario) {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate;
		try {
			mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplateCurso(TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getCurso().getCodigo(), usuario);
			if (mensagemTemplate == null) {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplateCurso(TemplateMensagemAutomaticaEnum.MENSAGEM_RENOVACAO_MATRICULA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, matriculaVO.getUnidadeEnsino().getCodigo(), 0, usuario);
			}
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				// Para obter a mensagem do email formatado Usamos um metodo a
				// parte.
				String mensagemEditada = obterMensagemFormatadaMensagemRenovacaoMatricula(matriculaVO, mensagemTemplate.getMensagem(),usuario);
				String mensagemSMSEditada = obterMensagemFormatadaMensagemRenovacaoMatricula(matriculaVO, mensagemTemplate.getMensagemSMS(),usuario);
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!Uteis.isAtributoPreenchido(usuario.getPessoa().getCodigo()) && Uteis.isAtributoPreenchido(config.getResponsavelPadraoComunicadoInterno().getCodigo())) {
					comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());	
				} else {
					comunicacaoEnviar.setResponsavel(usuario.getPessoa());
				}
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("AL");
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * Este Medoto gerar uma ComunicacaoInternaVO e envia um email ao aluno que efetuou o emprestimo.
	 * 
	 * @param livros
	 *            - <code> List<ItemEmprestimoVO> </code> que foram Emprestados a Pessoa
	 * @param pessoa
	 *            - Pessoa que pegou os livros emprestado. Garantir que o nome e o email estao carregados.
	 * @param valorTipoPessoa
	 *            - Tipo Pessoa, podera ser Aluno, Professor, Funcionario. Passa valor de 2 digitos de Enum
	 * @param biblioteca
	 *            - Nome da Biblioteca em que os livros foram emprestados
	 * @param usuario
	 *            - Usuario logado
	 * @throws Exception
	 *             - Podera Lançar um Exception para a Camada Chamadora.
	 */
	@Override
	public void executarEnvioMensagemEmprestimoRealizado(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String biblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_EMPRESTIMO_REALIZADO, false, unidadeEnsino, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			PessoaVO resposavel = (getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarFuncionarioPadraoEnvioNotificacaoEmail(usuario)).getPessoa();
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			// Para obter a mensagem do email formatado Usamos um metodo a
			// parte.
			String mensagemEditada = obterMensagemFormatadaMensagemEmprestimoRealizado(mensagemTemplate.getMensagem(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataPrevisaoDevolucao());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemEmprestimoRealizado(mensagemTemplate.getMensagemSMS(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataPrevisaoDevolucao());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(resposavel);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(valorTipoPessoa);
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}

	}

	/**
	 * Este Medoto gerar uma ComunicacaoInternaVO e envia um email ao aluno que efetuou o a Devolucao.
	 * 
	 * @param livros
	 *            - <code> List<ItemEmprestimoVO> </code> que foram Devolvidos
	 * @param pessoa
	 *            - Pessoa que Devolvel os livros. Garantir que o nome e o email estao carregados.
	 * @param valorTipoPessoa
	 *            - Tipo Pessoa, podera ser Aluno, Professor, Funcionario. Passa valor de 2 digitos de Enum
	 * @param biblioteca
	 *            - Nome da Biblioteca em que os livros foram devolvidos
	 * @param usuario
	 *            - Usuario logado
	 * @throws Exception
	 *             - Podera Lançar um Exception para a Camada Chamadora.
	 */
	@Override
	public void executarEnvioMensagemEmprestimoDevolucao(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String biblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_EMPRESTIMO_DEVOLUCAO, false, unidadeEnsino, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && livros != null && !livros.isEmpty()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			PessoaVO resposavel = (getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarFuncionarioPadraoEnvioNotificacaoEmail(usuario)).getPessoa();
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaDevolucaoAtrasoRealizada(mensagemTemplate.getMensagem(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataDevolucao());
			String mensagemSMSEditada = obterMensagemFormatadaDevolucaoAtrasoRealizada(mensagemTemplate.getMensagemSMS(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataDevolucao());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(resposavel);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(valorTipoPessoa);
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);

		}

	}

	@Override
	public void executaEnvioMensagemLivroAtrasado(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String biblioteca, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_EMPRESTIMO_ATRASADO, false, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && livros != null && !livros.isEmpty()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			PessoaVO resposavel = (getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarFuncionarioPadraoEnvioNotificacaoEmail(usuario)).getPessoa();
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaDevolucaoAtrasoRealizada(mensagemTemplate.getMensagem(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataPrevisaoDevolucao());
			String mensagemSMSEditada = obterMensagemFormatadaDevolucaoAtrasoRealizada(mensagemTemplate.getMensagemSMS(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataPrevisaoDevolucao());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(resposavel);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(valorTipoPessoa);
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);

		}

	}

	public void executaEnvioMensagemLivroAtrasadoJob(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String biblioteca, Integer unidadeEnsino, UsuarioVO usuario, ConfiguracaoGeralSistemaVO config, PessoaVO responsavel) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_EMPRESTIMO_ATRASADO, false, unidadeEnsino, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && livros != null && !livros.isEmpty()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaDevolucaoAtrasoRealizada(mensagemTemplate.getMensagem(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataPrevisaoDevolucao());
			String mensagemSMSEditada = obterMensagemFormatadaDevolucaoAtrasoRealizada(mensagemTemplate.getMensagemSMS(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataPrevisaoDevolucao());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(responsavel);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(valorTipoPessoa);
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);

		}
	}

	public void executarEnvioMensagemReservaDisponivel(CatalogoVO catalogo, BibliotecaVO biblioteca, ReservaVO reserva, PessoaVO pessoa, String valorTipoPessoa, PessoaVO responsavel, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RESERVA_DISPONIVEL, false, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaReservaDisponivel(mensagemTemplate.getMensagem(), catalogo.getTitulo(), pessoa.getNome(), biblioteca.getNome(), reserva.getDataReserva(), reserva.getDataTerminoReserva());
			String mensagemSMSEditada = obterMensagemFormatadaReservaDisponivel(mensagemTemplate.getMensagemSMS(), catalogo.getTitulo(), pessoa.getNome(), biblioteca.getNome(), reserva.getDataReserva(), reserva.getDataTerminoReserva());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(responsavel);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(valorTipoPessoa);
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executaEnvioMensagemRenovacaoEmprestimo(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String biblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_EMPRESTIMO_RENOVACAO, false, unidadeEnsino, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			PessoaVO resposavel = (getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarFuncionarioPadraoEnvioNotificacaoEmail(usuario)).getPessoa();
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaRenovacaoRealizada(mensagemTemplate.getMensagem(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataPrevistaDevolucaoTemp());
			String mensagemSMSEditada = obterMensagemFormatadaRenovacaoRealizada(mensagemTemplate.getMensagemSMS(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataPrevistaDevolucaoTemp());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(resposavel);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(valorTipoPessoa);
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaDevolucaoAtrasoRealizada(final String mensagemTemplate, String livros, String nomePessoa, String biblioteca, Date DataPrevistaDevolucao) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), nomePessoa);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DOS_LIVROS.name(), livros);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_BIBLIOTECA.name(), biblioteca);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_DEVOLUCAO.name(), Uteis.getData(DataPrevistaDevolucao));
		return mensagemTexto;

	}

	public String obterMensagemFormatadaRenovacaoRealizada(final String mensagemTemplate, String livros, String nomePessoa, String biblioteca, Date devolucao) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), nomePessoa);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DOS_LIVROS.name(), livros);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_BIBLIOTECA.name(), biblioteca);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_RENOVACAO.name(), Uteis.getData(new Date(), "dd/MM/yyyy"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_DEVOLUCAO.name(), Uteis.getData(devolucao , "dd/MM/yyyy"));
		return mensagemTexto;

	}

	public String obterMensagemFormatadaReservaDisponivel(final String mensagemTemplate, String tituloCatalogo, String nomePessoa, String biblioteca, Date DataReserva, Date DataLimite) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), nomePessoa);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TITULO_CATALOGO.name(), tituloCatalogo);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_RESERVA.name(), Uteis.getData(DataReserva, "dd/MM/yyyy"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE.name(), Uteis.getData(DataLimite, "dd/MM/yyyy HH:mm"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_BIBLIOTECA.name(), biblioteca);
		return mensagemTexto;
	}

	public String obterNomeLivrosFormatado(List<ItemEmprestimoVO> lista) {

		String livros = "";
		String separador = "";
		if (lista != null && !lista.isEmpty()) {
			for (int i = 0; i < lista.size(); i++) {
				if (i != (lista.size() - 1) || lista.size() == 1) {
					if (!lista.get(i).getExemplar().getTituloExemplar().equals("")) {
						livros += separador + lista.get(i).getExemplar().getTituloExemplar();
					}else{
					    livros += separador + lista.get(i).getExemplar().getCatalogo().getTitulo();
					}
					separador = ", ";
				} else {
					livros += " e " + lista.get(i).getExemplar().getTituloExemplar();
				}
			}
		}
		return livros;

	}

	public String obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(TrabalhoConclusaoCursoVO tcc, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), tcc.getMatriculaPeriodoTurmaDisciplina().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), tcc.getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ORIENTADOR_TCC.name(), tcc.getOrientador().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_COORDENADOR_TCC.name(), tcc.getCoordenador().getNome());
		String dataAula = Uteis.getData(new Date(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_ATUALIZACAO.name(), dataAula);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ETAPA_TCC.name(), tcc.getEtapaTCC().getValorApresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SITUACAO_TCC.name(), tcc.getSituacaoTCC().getValorApresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), tcc.getMatriculaPeriodoTurmaDisciplina().getDisciplina().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), tcc.getMatriculaPeriodoTurmaDisciplina().getTurma().getIdentificadorTurma());
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemAtrasoTCC(TrabalhoConclusaoCursoVO tcc, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ETAPA_TCC.name(), tcc.getEtapaTCC().getValorApresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SITUACAO_TCC.name(), tcc.getSituacaoTCC().getValorApresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), tcc.getMatriculaPeriodoTurmaDisciplina().getDisciplina().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), tcc.getMatriculaPeriodoTurmaDisciplina().getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_TERMINO_ETAPA_TCC.name(), tcc.getDataTerminoEtapaAtualApresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_RESPONSAVEL_TCC.name(), tcc.getNomeResponsavelSituacaoAtual());
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemReprovacaoAutomaticaPorAtrasoTCC(TrabalhoConclusaoCursoVO tcc, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), tcc.getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ETAPA_TCC.name(), tcc.getEtapaTCC().getValorApresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), tcc.getMatriculaPeriodoTurmaDisciplina().getDisciplina().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), tcc.getMatriculaPeriodoTurmaDisciplina().getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_TERMINO_ETAPA_TCC.name(), tcc.getDataTerminoEtapaAtualApresentar());
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemRenegociacaoContaReceberAluno(NegociacaoContaReceberVO negociacaoContaReceberVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), negociacaoContaReceberVO.getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), negociacaoContaReceberVO.getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), negociacaoContaReceberVO.getMatriculaAluno().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_TURNO.name(), negociacaoContaReceberVO.getMatriculaAluno().getTurno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), negociacaoContaReceberVO.getMatriculaAluno().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_RENEGOCIACAO.name(), negociacaoContaReceberVO.getData_Apresentar());
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemRenegociacaoContaReceberGrupoDestinatario(NegociacaoContaReceberVO negociacaoContaReceberVO, PessoaVO destinatario, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), negociacaoContaReceberVO.getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), destinatario.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), negociacaoContaReceberVO.getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), negociacaoContaReceberVO.getMatriculaAluno().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_TURNO.name(), negociacaoContaReceberVO.getMatriculaAluno().getTurno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), negociacaoContaReceberVO.getMatriculaAluno().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_RENEGOCIACAO.name(), negociacaoContaReceberVO.getData_Apresentar());
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemRenovacaoMatricula(MatriculaVO matriculaVO, final String mensagemTemplate , UsuarioVO usuarioVO) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaVO.getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), matriculaVO.getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), matriculaVO.getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_TURNO.name(), matriculaVO.getTurno().getNome());
		String identificadorTurma = matriculaVO.getUltimoMatriculaPeriodoVO().getTurma().getIdentificadorTurma();
		if(identificadorTurma.isEmpty()) {
			try {
				TurmaVO	turma = getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(matriculaVO.getMatricula(),usuarioVO);
				if(Uteis.isAtributoPreenchido(turma) && !turma.getIdentificadorTurma().isEmpty()) {
					identificadorTurma = turma.getIdentificadorTurma();
				}
			   } catch (Exception e) {}			
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), identificadorTurma);	
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matriculaVO.getMatricula());
		if(mensagemTexto.contains(TagsMensagemAutomaticaEnum.EMAIL_INSTITUCIONAL.name())) {
			PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(matriculaVO.getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null);
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.EMAIL_INSTITUCIONAL.name(), pessoaEmailInstitucionalVO.getEmail());
		}
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemConvocacaoEnade(MapaConvocacaoEnadeMatriculaVO mapaConvocacaoEnadeMatriculaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), mapaConvocacaoEnadeMatriculaVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TITULO_ENADE.name(), mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getTituloEnade());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_PROVA_ENADE.name(), mapaConvocacaoEnadeMatriculaVO.getMapaConvocacaoEnadeVO().getEnadeCursoVO().getEnadeVO().getDataProva_Apresentar());
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemEmprestimoRealizado(EmprestimoVO emprestimoEnviar, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;

		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), emprestimoEnviar.getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DOS_LIVROS.name(), obterNomeLivrosFormatado(emprestimoEnviar.getItemEmprestimoVOs()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_BIBLIOTECA.name(), emprestimoEnviar.getBiblioteca().getNome());
		String dataDevolucao = Uteis.getData(emprestimoEnviar.getItemEmprestimoVOs().get(0).getDataDevolucao(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_DEVOLUCAO.name(), dataDevolucao);
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemCancelamentoMatricula(PessoaVO pessoa, MatriculaVO matricula, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), pessoa.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matricula.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), matricula.getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CPF_ALUNO.name(), pessoa.getCPF());
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemAtualizacaoDisciplinaTurmaRealizado(TurmaVO turma, PessoaVO pessoa, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), pessoa.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), pessoa.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_ATUALIZACAO.name(), Uteis.getData(new Date(), "dd/MM/yyyy"));
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemDownloadAntecedenciaMaterial(SqlRowSet rs, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), rs.getString("nome_aluno"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), rs.getString("matricula"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), rs.getString("nome_curso"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), rs.getString("nome_disciplina"));
		String dataAula = Uteis.getData(rs.getDate("data_aula"), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_AULA.name(), dataAula);
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemConsultorContatosDia(AgendaPessoaHorarioVO obj, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), obj.getAgendaPessoa().getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.QUANTIDADE_CONTATOS.name(), obj.getQuantidadeContatos().toString());
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemBuscaProspect(BuscaProspectVO bp, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), bp.getProspect().getNome());
		// mensagemTexto =
		// mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(),
		// rs.getString("nome_curso"));
		// String dataAula = Uteis.getData(rs.getDate("data_aula"),
		// "dd/MM/yyyy");
		// mensagemTexto =
		// mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_AULA.name(),
		// dataAula);
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemBoletoAluno(ContaReceberVO cr, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), cr.getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), cr.getMatriculaAluno().getMatricula());
		if (cr.getResponsavelFinanceiro().getCodigo().intValue() > 0) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SACADO.name(), cr.getResponsavelFinanceiro().getNome());
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SACADO.name(), cr.getPessoa().getNome());
		}
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemVencimentoContaReceber(ContaReceberVO contaReceberVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), contaReceberVO.getNomePessoa());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_VENCIMENTO.name(), Uteis.getData(contaReceberVO.getDataVencimento()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TIPO_ORIGEM.name(), contaReceberVO.getTipoOrigemPorConfiguracaoFinanceira_Apresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_BARRA.name(), contaReceberVO.getLinhaDigitavelCodigoBarras());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.EMAIL_RESPONSAVEL_COBRANCA.name(), contaReceberVO.getUnidadeEnsino().getResponsavelCobrancaUnidade().getEmail());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_PARCELA.name(), contaReceberVO.getParcelaPorConfiguracaoFinanceira_Apresentar());

		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemConsultorMatriculaNaoPaga(ContaReceberVO contaReceberVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), contaReceberVO.getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CONSULTOR.name(), contaReceberVO.getMatriculaAluno().getConsultor().getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), contaReceberVO.getMatriculaAluno().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_RENEGOCIACAO.name(), Uteis.getData(new Date()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), contaReceberVO.getTurma().getIdentificadorTurma());
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemEmprestimoRealizado(final String mensagemTemplate, String livros, String nomePessoa, String biblioteca, Date devolucao) {
		String mensagemTexto = mensagemTemplate;

		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), nomePessoa);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DOS_LIVROS.name(), livros);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_BIBLIOTECA.name(), biblioteca);
		String dataDevolucao = Uteis.getData(devolucao, "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_DEVOLUCAO.name(), dataDevolucao);
		return mensagemTexto;

	}

	@Override
	public ComunicacaoInternaVO inicializarDadosPadrao(ComunicacaoInternaVO comunicacaoEnviar) {
		// Caso o valor seja True, um email sera envida quando a comunicacao for
		// persistida.
		comunicacaoEnviar.setEnviarEmail(Boolean.TRUE);
		// Para obter a mensagem do email formatado Usamos um metodo a parte.
		comunicacaoEnviar.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		comunicacaoEnviar.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
		comunicacaoEnviar.setTipoMarketing(Boolean.FALSE);
		comunicacaoEnviar.setTipoLeituraObrigatoria(Boolean.FALSE);
		comunicacaoEnviar.setDigitarMensagem(Boolean.TRUE);
//		comunicacaoEnviar.setEnviarEmailInstitucional(Boolean.TRUE);
		return comunicacaoEnviar;

	}

	@Override
	public List<ComunicadoInternoDestinatarioVO> obterListaDestinatarios(PessoaVO pessoa, boolean enviarCopiaPais) throws Exception {
		List<ComunicadoInternoDestinatarioVO> listDestinatario = new ArrayList<ComunicadoInternoDestinatarioVO>();
		ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
		destinatario.setCiJaLida(Boolean.FALSE);
		destinatario.setDestinatario(pessoa);
		destinatario.setEmail(pessoa.getEmail());
		destinatario.setNome(pessoa.getNome());
		destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		listDestinatario.add(destinatario);
		if (enviarCopiaPais) {
			List<FiliacaoVO> pais = getFacadeFactory().getFiliacaoFacade().consultarFiliacaos(pessoa.getCodigo(), false, new UsuarioVO());
			for (FiliacaoVO pai : pais) {
				destinatario = new ComunicadoInternoDestinatarioVO();
				destinatario.setCiJaLida(Boolean.FALSE);
				destinatario.setDestinatario(pai.getPais());
				destinatario.setEmail(pai.getPais().getEmail());
				destinatario.setNome(pai.getPais().getNome());
				destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
				listDestinatario.add(destinatario);
			}
		}
		return listDestinatario;
	}

	/**
	 * Este Medoto gerar uma ComunicacaoInternaVO e envia um email ao aluno alertando sobre o download do material.
	 * 
	 * 
	 */
	@Override
	public void executarEnvioMensagemAlunoComBoletoAnexo() throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ENVIO_BOLETO_ALUNO, false, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
			List<ContaReceberVO> lista = new ArrayList<>(0);
			do {
				lista = getFacadeFactory().getContaReceberFacade().consultarAlunoNotificarBoleto(mensagemTemplate);
				Map<Integer, List<ContaReceberVO>> collect = lista.stream().collect(Collectors.groupingBy(p->p.getUnidadeEnsinoFinanceira().getCodigo())); 
				Map<Integer, PersonalizacaoMensagemAutomaticaVO> consultarPorUnidadeEnsino = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorUnidadeEnsino(TemplateMensagemAutomaticaEnum.MENSAGEM_ENVIO_BOLETO_ALUNO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						collect.keySet().stream().distinct().collect(Collectors.toList()), null);
				for (Map.Entry<Integer, List<ContaReceberVO>> mapa : collect.entrySet()) {
					ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getAplicacaoControle().getConfiguracaoFinanceiroVO(mapa.getKey());
					UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUnidadeEnsinoDadosLogoRelatorio(mapa.getKey(), null);
					PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = null;
					if (Uteis.isAtributoPreenchido(consultarPorUnidadeEnsino) && consultarPorUnidadeEnsino.containsKey(mapa.getKey())) {
						mensagemTemplateUtilizar = consultarPorUnidadeEnsino.get(mapa.getKey());
					} else {
						mensagemTemplateUtilizar = mensagemTemplate;
					}
					if (mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica()) {
						preencherComunicaoInternoBoletoAluno(mensagemTemplateUtilizar, config, mapa, configuracaoFinanceiroVO,unidadeEnsinoVO, usuario);
					}
				}
			}while(!lista.isEmpty());
		}
	}	

	private void preencherComunicaoInternoBoletoAluno(PersonalizacaoMensagemAutomaticaVO mensagemTemplate,ConfiguracaoGeralSistemaVO config, Map.Entry<Integer, List<ContaReceberVO>> mapa,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
		for (ContaReceberVO cr : mapa.getValue()) {
			try {
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				PessoaVO aluno = new PessoaVO();
				if (cr.getResponsavelFinanceiro().getCodigo().intValue() > 0) {
					aluno.setCodigo(cr.getResponsavelFinanceiro().getCodigo());
					aluno.setNome(cr.getResponsavelFinanceiro().getNome());
					aluno.setEmail(cr.getResponsavelFinanceiro().getEmail());
				} else {
					aluno.setCodigo(cr.getPessoa().getCodigo());
					aluno.setNome(cr.getPessoa().getNome());
					aluno.setEmail(cr.getPessoa().getEmail());
				}
				// Para obter a mensagem do email formatado Usamos um metodo a parte.
				String mensagemEditada = obterMensagemFormatadaMensagemBoletoAluno(cr, mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaMensagemBoletoAluno(cr, mensagemTemplate.getMensagemSMS());
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setUnidadeEnsino(cr.getUnidadeEnsinoFinanceira());
				comunicacaoEnviar.setMensagem(mensagemEditada);
				comunicacaoEnviar.setAluno(aluno);
				if (!config.getResponsavelPadraoComunicadoInterno().getCodigo().equals(0)) {
				  comunicacaoEnviar.setResponsavel(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(config.getResponsavelPadraoComunicadoInterno().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
				}else{
				  comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				}
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(aluno, mensagemTemplate.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("AL");
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				
				List<BoletoBancarioRelVO> listaBoleto = getFacadeFactory().getBoletoBancarioRelFacade().emitirRelatorioLista(false, cr.getCodigo(), null, null, null, null, null, null, null, null, null, "", 0, null, configuracaoFinanceiroVO, null, false);
				BoletoBancarioRelVO boletoBancarioRelVO = new BoletoBancarioRelVO();
				if(!listaBoleto.isEmpty()) {
					boletoBancarioRelVO = listaBoleto.get(0);
				}
				String logoBanco = "";
				
		        if (configuracaoFinanceiroVO != null && configuracaoFinanceiroVO.getImprimirBoletoComLogoBanco()) {
		           	logoBanco = boletoBancarioRelVO.getObterLogoBanco(getCaminhoPastaWeb());
		        }
				SuperParametroRelVO superParametroRelVO = new SuperParametroRelVO();
				superParametroRelVO.setNomeDesignIreport(getFacadeFactory().getBoletoBancarioRelFacade().getObterDesign("boleto", boletoBancarioRelVO));
				superParametroRelVO.setCaminhoBaseRelatorio(BoletoBancarioRel.getCaminhoBaseRelatorio());
				superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				superParametroRelVO.setNomeEmpresa(boletoBancarioRelVO.getContareceber_razaoSocialMantenedora());
				superParametroRelVO.setNomeUsuario(boletoBancarioRelVO.getPessoa_nome());
				superParametroRelVO.setTituloRelatorio("Recibo do Sacado");
				superParametroRelVO.setFiltros("");
				superParametroRelVO.setListaObjetos(listaBoleto);
				superParametroRelVO.getParametros().put("parametro1", logoBanco);				        
				if (unidadeEnsinoVO != null) {
					if (unidadeEnsinoVO.getExisteLogoRelatorio()) {
						superParametroRelVO.getParametros().put("logoPadraoRelatorio",unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogoRelatorio());
					}else {
						superParametroRelVO.getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");	
					}
					if(unidadeEnsinoVO.getExisteLogo()){
						superParametroRelVO.getParametros().put("logoCliente", unidadeEnsinoVO.getCaminhoBaseLogo().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogo());	
					}else {
						superParametroRelVO.getParametros().put("logoCliente", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logo.png");	
					}
				}else {
					superParametroRelVO.getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
					superParametroRelVO.getParametros().put("logoCliente", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logo.png");
				}    
				persistirGeracaoBoletoAluno(config, cr, comunicacaoEnviar, superParametroRelVO, usuario);
			} catch (Exception e) {
				getFacadeFactory().getContaReceberFacade().registrarAlunoRecebeuNotificacaoBoleto(cr.getCodigo(), usuario);
				RegistroExecucaoJobVO  registroExecucaoJob =  new RegistroExecucaoJobVO();
				registroExecucaoJob.setErro("Erro Job Notificacao Boleto Aluno conta receber :"+cr.getCodigo()+ " msg:"+e.getMessage());
				registroExecucaoJob.setNome(JobsEnum.JOB_ENVIO_BOLETO_ALUNO.getName());
				registroExecucaoJob.setTempoExecucao(0);
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);						
			}
		}
	}
	
	/**
	 * Metodo separada da rotina principal pois aqui sera aberto a transacao com banco de dados para que ao persistir um comunicado internto e conta receber estejam na mesma transacao.
	 * e caso seja lancado a excecao as proximas contas receber continuem a serem gravadas
	 * @param config
	 * @param cr
	 * @param comunicacaoEnviar
	 * @param superParametroRelVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirGeracaoBoletoAluno(ConfiguracaoGeralSistemaVO config, ContaReceberVO cr, ComunicacaoInternaVO comunicacaoEnviar, SuperParametroRelVO superParametroRelVO, UsuarioVO usuario) throws Exception {
		File file = UteisJSF.realizarGeracaoArquivoPDF(superParametroRelVO);
		ArquivoVO arquivo = new ArquivoVO();
		arquivo.setResponsavelUpload(usuario);
		upLoad(file, arquivo, config.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.ARQUIVO_TMP.getValue(), arquivo.getResponsavelUpload());
		getFacadeFactory().getComunicacaoInternaFacade().criarFileCorpoMensagemEmail(comunicacaoEnviar);
		List<ArquivoVO> listaAnexos = new ArrayList<ArquivoVO>();
		if (file != null) {
			listaAnexos.add(arquivo);
		}
		comunicacaoEnviar.setListaArquivosAnexo(listaAnexos);
		getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, arquivo.getResponsavelUpload(), config,null);
		getFacadeFactory().getContaReceberFacade().registrarAlunoRecebeuNotificacaoBoleto(cr.getCodigo(), arquivo.getResponsavelUpload());
	}
	

	public String criarNomeArquivo(String nomeArquivo, UsuarioVO usuarioVO) {
		return usuarioVO.getCodigo() + "_" + new Date().getTime();
	}

	public void upLoad(File file, ArquivoVO arquivoVO, String caminhaArquivo, UsuarioVO usuarioVO) throws Exception {
		File fileDiretorio = null;
		File fileArquivo = null;
		String nomeArquivoSemAcento = "";
		try {
			fileDiretorio = new File(caminhaArquivo);
			if (!fileDiretorio.exists()) {
				fileDiretorio.mkdirs();
			}
			nomeArquivoSemAcento = criarNomeArquivo(file.getName().substring(0, file.getName().lastIndexOf(".")), usuarioVO);
			arquivoVO.setNome(nomeArquivoSemAcento + "." + file.getName().substring(file.getName().lastIndexOf(".") + 1));
			arquivoVO.setDescricao("Boleto Conta Receber");
			fileArquivo = new File(caminhaArquivo + File.separator + arquivoVO.getNome());
			if (!fileArquivo.exists()) {
				fileArquivo.createNewFile();
			}
			FileUtils.copyFile(file, fileArquivo);
			arquivoVO.setPastaBaseArquivo(caminhaArquivo);
			arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVO_TMP);
		} catch (Exception e) {
			throw e;
		} finally {
			fileArquivo = null;
			nomeArquivoSemAcento = null;
		}
	}

	@Override
	public void executarEnvioMensagemDownloadAntecedenciaMaterial() throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_DOWNLOAD_ANTECEDENCIA_MATERIAL, false, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			List<UnidadeEnsinoVO> consultarTodasUnidades = getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
			Map<Integer, PersonalizacaoMensagemAutomaticaVO> consultarPorUnidadeEnsino = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorUnidadeEnsino(TemplateMensagemAutomaticaEnum.MENSAGEM_DOWNLOAD_ANTECEDENCIA_MATERIAL, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
					consultarTodasUnidades.stream().map(UnidadeEnsinoVO::getCodigo).distinct().collect(Collectors.toList()), null);
			SqlRowSet rs = getFacadeFactory().getArquivoFacade().consultarAlunoNotificarDownloadMaterial();
			while (rs.next()) {
				if (Uteis.isAtributoPreenchido(rs.getInt("unidadeensino")) && consultarPorUnidadeEnsino.containsKey(rs.getInt("unidadeensino"))) {
					PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO = consultarPorUnidadeEnsino.get(rs.getInt("unidadeensino"));
					if (personalizacaoMensagemAutomaticaVO != null && !personalizacaoMensagemAutomaticaVO.getDesabilitarEnvioMensagemAutomatica()) {
						executarEnvioMensagemDownloadAntecedenciaMaterial(rs, personalizacaoMensagemAutomaticaVO, config);
						continue;
					}
				}
				executarEnvioMensagemDownloadAntecedenciaMaterial(rs, mensagemTemplate, config);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemBuscaProspect(List<BuscaProspectVO> lista, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, ComunicacaoInternaVO comunicacaoEnviar) throws Exception {
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			Iterator<BuscaProspectVO> i = lista.iterator();
			while (i.hasNext()) {
				BuscaProspectVO bp = (BuscaProspectVO) i.next();
				try {
					// ComunicacaoInternaVO comunicacaoEnviar =
					// inicializarDadosPadrao(new ComunicacaoInternaVO());
					// comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
					comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
					comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
					PessoaVO aluno = new PessoaVO();
					// aluno.setCodigo(rs.getInt("codigo"));
					aluno.setNome(bp.getNomeProspect());
					aluno.setEmail(bp.getEmailProspect());
					// Para obter a mensagem do email formatado Usamos um
					// metodo a parte.
					String mensagemEditada = obterMensagemFormatadaMensagemBuscaProspect(bp, mensagemTemplate.getMensagem());
					String mensagemSMSEditada = obterMensagemFormatadaMensagemBuscaProspect(bp, mensagemTemplate.getMensagemSMS());
					if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
						comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
						comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
					}
					comunicacaoEnviar.setMensagem(mensagemEditada);
					comunicacaoEnviar.setAluno(aluno);
					comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacaoEnviar.setData(new Date());
					comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(aluno, mensagemTemplate.getEnviarCopiaPais()));
					comunicacaoEnviar.setTipoDestinatario("AL");
					comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					getFacadeFactory().getComunicacaoInternaFacade().enviarEmailComunicacaoInterna(comunicacaoEnviar, new UsuarioVO(), config, mensagemTemplate, null, false);
				} catch (Exception e) {
					e.getMessage();
				}

			}
		}

	}

	@Override
	public void executarEnvioMensagemVencimentoContaReceber() throws Exception {
		List<ContaReceberVO> contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberNotificarVencimentoConta();
		if (contaReceberVOs != null && !contaReceberVOs.isEmpty()) {
			List<Integer> unidadeEnsinos = contaReceberVOs.stream().map(ContaReceberVO::getUnidadeEnsinoFinanceira).map(UnidadeEnsinoVO::getCodigo).distinct().collect(Collectors.toList());
			Map<Integer, PersonalizacaoMensagemAutomaticaVO> consultarPorUnidadeEnsino = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorUnidadeEnsino(TemplateMensagemAutomaticaEnum.MENSAGEM_VENCIMENTO_CONTA_RECEBER, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, unidadeEnsinos, null);
			Date dataEnvio = new Date();
			for (ContaReceberVO contaReceberVO : contaReceberVOs) {
				try {
					ConfiguracaoGeralSistemaVO config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(contaReceberVO.getUnidadeEnsino().getCodigo(), null);						
					ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
					PersonalizacaoMensagemAutomaticaVO mensagemTemplate = null;
					if (Uteis.isAtributoPreenchido(consultarPorUnidadeEnsino) && consultarPorUnidadeEnsino.containsKey(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo())) {
						mensagemTemplate = consultarPorUnidadeEnsino.get(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo());
					}
					if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
						comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
						comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
						comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
						comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
						
						// Para obter a mensagem do email formatado Usamos um metodo
						// a parte.
						contaReceberVO.setConfiguracaoFinanceiro(getAplicacaoControle().getConfiguracaoFinanceiroVO(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo()));
						String mensagemEditada = obterMensagemFormatadaMensagemVencimentoContaReceber(contaReceberVO, mensagemTemplate.getMensagem());
						String mensagemSMSEditada = obterMensagemFormatadaMensagemVencimentoContaReceber(contaReceberVO, mensagemTemplate.getMensagemSMS());
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
							comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
						}
						comunicacaoEnviar.setMensagem(mensagemEditada);
						PessoaVO responsavel = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(config.getResponsavelPadraoComunicadoInterno().getCodigo());
						
						if (contaReceberVO.getTipoAluno() || contaReceberVO.getTipoRequerente()) {
							comunicacaoEnviar.setTipoDestinatario("AL");
							comunicacaoEnviar.setAluno(contaReceberVO.getPessoa());
							comunicacaoEnviar.setResponsavel(responsavel);
							comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceberVO.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
						}
						if (contaReceberVO.getTipoResponsavelFinanceiro()) {
							comunicacaoEnviar.setTipoDestinatario("AL");
							comunicacaoEnviar.setPessoa(contaReceberVO.getResponsavelFinanceiro());
							comunicacaoEnviar.setResponsavel(responsavel);
							comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceberVO.getResponsavelFinanceiro(), mensagemTemplate.getEnviarCopiaPais()));
						}
						if (contaReceberVO.getTipoFuncionario()) {
							comunicacaoEnviar.setTipoDestinatario("FU");
							comunicacaoEnviar.setFuncionario(contaReceberVO.getFuncionario());
							comunicacaoEnviar.setResponsavel(responsavel);
							comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceberVO.getFuncionario().getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
						}
						comunicacaoEnviar.setData(dataEnvio);
						comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
						comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
						getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
					}
				} catch (Exception e) {
					e.getMessage();
				}
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemConsultorMatriculaNaoPaga() throws Exception {
		List<UnidadeEnsinoVO> listaUnidade = new ArrayList<UnidadeEnsinoVO>(0);
		listaUnidade.addAll(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoConfiguracoesPorGestaoEnvioMensagemAutomatica());
		

		for (UnidadeEnsinoVO obj : listaUnidade) {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_CONSULTOR_MATRICULA_NAO_PAGA, false, obj.getCodigo(), null);
			ConfiguracaoGeralSistemaVO config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(obj.getCodigo(), null);

			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				List<ContaReceberVO> contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberMatriculaNaoPaga(obj.getCodigo());
				if (contaReceberVOs != null && !contaReceberVOs.isEmpty()) {
					Date dataEnvio = new Date();
					for (ContaReceberVO contaReceberVO : contaReceberVOs) {
						if (Uteis.getIntervaloEntreDatas(contaReceberVO.getDataVencimento(), new Date()) < contaReceberVO.getConfiguracaoFinanceiro().getNumeroDiasEnviarNotificacaoMatriculaNaoPagaConsultor()) {
							continue;
						}
						if (contaReceberVO.getMatriculaAluno().getConsultor().getCodigo() == 0) {
							contaReceberVO.getMatriculaAluno().setConsultor(getFacadeFactory().getFuncionarioFacade().consultaRapidaConsultorPorMatricula(contaReceberVO.getMatriculaAluno().getMatricula(), false, null));
							if (contaReceberVO.getMatriculaAluno().getConsultor().getCodigo() == 0) {
								continue;
							}
						}
						ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
						comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
						comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
						comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
						comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
						// Para obter a mensagem do email formatado Usamos um
						// metodo
						// a parte.
						String mensagemEditada = obterMensagemFormatadaMensagemConsultorMatriculaNaoPaga(contaReceberVO, mensagemTemplate.getMensagem());
						String mensagemSMSEditada = obterMensagemFormatadaMensagemConsultorMatriculaNaoPaga(contaReceberVO, mensagemTemplate.getMensagemSMS());
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
							comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
						}
						comunicacaoEnviar.setMensagem(mensagemEditada);
						comunicacaoEnviar.setTipoDestinatario("FU");
						comunicacaoEnviar.setFuncionario(contaReceberVO.getMatriculaAluno().getConsultor());
						comunicacaoEnviar.setAluno(contaReceberVO.getPessoa());
						PessoaVO responsavel = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarResponsavelPadraoComunicadoInternoPorCodigoConfiguracoes(config.getResponsavelPadraoComunicadoInterno().getCodigo());
						comunicacaoEnviar.setResponsavel(responsavel);
						comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceberVO.getMatriculaAluno().getConsultor().getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
						comunicacaoEnviar.setData(dataEnvio);
						comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
						comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
						getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
					}
				}
			}
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemOuvidoriaAbertura(AtendimentoVO atendimento, UsuarioVO usuarioLogado) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_OUVIDORIA_ABERTURA, false, atendimento.getUnidadeEnsino(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmailMaisResponsavelPadrao();
			ConfiguracaoAtendimentoVO configAtendimento = getFacadeFactory().getConfiguracaoAtendimentoFacade().consultarPorCodigoUnidadeEnsino(atendimento.getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			String mensagemRes = obterMensagemFormatadaMensagemOuvidoriaAbertura(atendimento, configAtendimento, mensagemTemplate.getMensagem(), false);
			String mensagemSMSRes = obterMensagemFormatadaMensagemOuvidoriaAbertura(atendimento, configAtendimento, mensagemTemplate.getMensagemSMS(), false);
			String mensagemSol = obterMensagemFormatadaMensagemOuvidoriaAbertura(atendimento, configAtendimento, mensagemTemplate.getMensagem(), true);
			String mensagemSMSSol = obterMensagemFormatadaMensagemOuvidoriaAbertura(atendimento, configAtendimento, mensagemTemplate.getMensagemSMS(), true);
			executarEnvioMensagemOuvidoriaPreenchimentoDadosResponsavel(mensagemTemplate, atendimento, atendimento.getResponsavelAtendimento(), config, mensagemRes, mensagemSMSRes);
			executarEnvioMensagemOuvidoriaPreenchimentoDadosSolicitante(mensagemTemplate, atendimento, config, mensagemSol, mensagemSMSSol, false, usuarioLogado);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemFinalizacaoOuvidoria(AtendimentoVO atendimento, AtendimentoInteracaoSolicitanteVO atendimentoSolicitante, UsuarioVO usuarioLogado) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_OUVIDORIA_FINALIZACAO, false, atendimento.getUnidadeEnsino(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmailMaisResponsavelPadrao();
			String mensagemSol = obterMensagemFormatadaMensagemOuvidoriaFinalizacao(atendimento, mensagemTemplate.getMensagem());
			String mensagemSMSSol = obterMensagemFormatadaMensagemOuvidoriaFinalizacao(atendimento, mensagemTemplate.getMensagemSMS());
			executarEnvioMensagemOuvidoriaPreenchimentoDadosSolicitante(mensagemTemplate, atendimento, config, mensagemSol, mensagemSMSSol, true, usuarioLogado);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemOuvidoriaInteracaoDepartamento(AtendimentoVO atendimento, AtendimentoInteracaoDepartamentoVO atendimentoDepartamento, Boolean questionamentoOuvidor, UsuarioVO usuarioLogado) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_OUVIDORIA_INTERACAO, false, atendimento.getUnidadeEnsino(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmailMaisResponsavelPadrao();
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			if (questionamentoOuvidor) {
				mensagemEditada = mensagemTemplate.getMensagem().replaceAll(TagsMensagemAutomaticaEnum.INTERACAO_ATENDIMENTO.name(), atendimentoDepartamento.getQuestionamento());
				mensagemSMSEditada = mensagemTemplate.getMensagemSMS().replaceAll(TagsMensagemAutomaticaEnum.INTERACAO_ATENDIMENTO.name(), atendimentoDepartamento.getQuestionamento());
			} else {
				mensagemEditada = mensagemTemplate.getMensagem().replaceAll(TagsMensagemAutomaticaEnum.INTERACAO_ATENDIMENTO.name(), atendimentoDepartamento.getResposta());
				mensagemSMSEditada = mensagemTemplate.getMensagemSMS().replaceAll(TagsMensagemAutomaticaEnum.INTERACAO_ATENDIMENTO.name(), atendimentoDepartamento.getResposta());
			}
			executarEnvioMensagemOuvidoriaPreenchimentoDadosResponsavel(mensagemTemplate, atendimento, atendimento.getResponsavelAtendimento(), config, mensagemEditada, mensagemSMSEditada);
			executarEnvioMensagemOuvidoriaPreenchimentoDadosResponsavel(mensagemTemplate, atendimento, atendimentoDepartamento.getFuncionarioVO(), config, mensagemEditada, mensagemSMSEditada);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemOuvidoriaInteracaoSolicitante(AtendimentoVO atendimento, AtendimentoInteracaoSolicitanteVO atendimentoSolicitante, Boolean questionamentoOuvidor, UsuarioVO usuarioLogado) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_OUVIDORIA_INTERACAO, false, atendimento.getUnidadeEnsino(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmailMaisResponsavelPadrao();
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			if (questionamentoOuvidor == null || questionamentoOuvidor) {
				mensagemEditada = mensagemTemplate.getMensagem().replaceAll(TagsMensagemAutomaticaEnum.INTERACAO_ATENDIMENTO.name(), atendimentoSolicitante.getQuestionamentoOuvidor());
				mensagemSMSEditada = mensagemTemplate.getMensagemSMS().replaceAll(TagsMensagemAutomaticaEnum.INTERACAO_ATENDIMENTO.name(), atendimentoSolicitante.getQuestionamentoOuvidor());
			} else {
				mensagemEditada = mensagemTemplate.getMensagem().replaceAll(TagsMensagemAutomaticaEnum.INTERACAO_ATENDIMENTO.name(), atendimentoSolicitante.getQuestionamentoSolicitante());
				mensagemSMSEditada = mensagemTemplate.getMensagemSMS().replaceAll(TagsMensagemAutomaticaEnum.INTERACAO_ATENDIMENTO.name(), atendimentoSolicitante.getQuestionamentoSolicitante());
			}
			executarEnvioMensagemOuvidoriaPreenchimentoDadosResponsavel(mensagemTemplate, atendimento, atendimento.getResponsavelAtendimento(), config, mensagemEditada, mensagemSMSEditada);
			executarEnvioMensagemOuvidoriaPreenchimentoDadosSolicitante(mensagemTemplate, atendimento, config, mensagemEditada, mensagemSMSEditada, false, usuarioLogado);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemOuvidoriaPreenchimentoDadosResponsavel(PersonalizacaoMensagemAutomaticaVO mensagemTemplate, AtendimentoVO atendimento, FuncionarioVO funcionario, ConfiguracaoGeralSistemaVO config, String mensagemEditada, String mensagemSMSEditada) throws Exception {
		ComunicacaoInternaVO comunicacaoResponsavelAtendimento = inicializarDadosPadrao(new ComunicacaoInternaVO());
		comunicacaoResponsavelAtendimento.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.OUVIDORIA);
		comunicacaoResponsavelAtendimento.setCodigoTipoOrigemComunicacaoInterna(atendimento.getCodigo());
		comunicacaoResponsavelAtendimento.setAssunto(mensagemTemplate.getAssunto());
		comunicacaoResponsavelAtendimento.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
		comunicacaoResponsavelAtendimento.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
		comunicacaoResponsavelAtendimento.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
		comunicacaoResponsavelAtendimento.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
		comunicacaoResponsavelAtendimento.setFuncionario(funcionario);
		comunicacaoResponsavelAtendimento.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
		comunicacaoResponsavelAtendimento.setData(new Date());
		comunicacaoResponsavelAtendimento.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(funcionario.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
		comunicacaoResponsavelAtendimento.setTipoDestinatario("FU");
		comunicacaoResponsavelAtendimento.setMensagem(mensagemEditada);
		if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
			comunicacaoResponsavelAtendimento.setMensagemSMS(mensagemSMSEditada);
			comunicacaoResponsavelAtendimento.setEnviarSMS(Boolean.TRUE);
		}
		comunicacaoResponsavelAtendimento.setEnviarEmail(mensagemTemplate.getEnviarEmail());
		comunicacaoResponsavelAtendimento.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
		getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoResponsavelAtendimento, false, new UsuarioVO(), config,null);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemOuvidoriaPreenchimentoDadosSolicitante(PersonalizacaoMensagemAutomaticaVO mensagemTemplate, AtendimentoVO atendimento, ConfiguracaoGeralSistemaVO config, String mensagemEditada, String mensagemSMSEditada, Boolean utilizaAvaliacao, UsuarioVO usuarioLogado) throws Exception {
		ComunicacaoInternaVO comunicacaoSolicitante = inicializarDadosPadrao(new ComunicacaoInternaVO());
		comunicacaoSolicitante.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.OUVIDORIA);
		comunicacaoSolicitante.setCodigoTipoOrigemComunicacaoInterna(atendimento.getCodigo());
		comunicacaoSolicitante.setAssunto(mensagemTemplate.getAssunto());
		comunicacaoSolicitante.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
		comunicacaoSolicitante.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
		comunicacaoSolicitante.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
		comunicacaoSolicitante.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
		comunicacaoSolicitante.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
		comunicacaoSolicitante.setData(new Date());
		comunicacaoSolicitante.setMensagem(mensagemEditada);
		comunicacaoSolicitante.setEnviarEmail(mensagemTemplate.getEnviarEmail());
		comunicacaoSolicitante.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
		if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
			comunicacaoSolicitante.setMensagemSMS(mensagemSMSEditada);
			comunicacaoSolicitante.setEnviarSMS(Boolean.TRUE);
		}

		if (utilizaAvaliacao) {
			comunicacaoSolicitante.setImgAvaliacao_1("star1");
			comunicacaoSolicitante.setImgAvaliacao_2("star2");
			comunicacaoSolicitante.setImgAvaliacao_3("star3");
			comunicacaoSolicitante.setImgAvaliacao_4("star4");
		}
		if (atendimento.getExistePessoa()) {
			if (atendimento.getPessoaVO().getProfessor()) {
				comunicacaoSolicitante.setProfessor(atendimento.getPessoaVO());
				comunicacaoSolicitante.setTipoDestinatario("PR");
			} else if (atendimento.getPessoaVO().getCoordenador()) {
				comunicacaoSolicitante.setProfessor(atendimento.getPessoaVO());
				comunicacaoSolicitante.setTipoDestinatario("CO");

			} else {
				comunicacaoSolicitante.setAluno(atendimento.getPessoaVO());
				comunicacaoSolicitante.setTipoDestinatario("AL");
			}
			comunicacaoSolicitante.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(atendimento.getPessoaVO(), mensagemTemplate.getEnviarCopiaPais()));
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoSolicitante, false, new UsuarioVO(), config,null);
		} else {
			ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
			destinatario.setCiJaLida(Boolean.FALSE);
			destinatario.getDestinatario().setNome(atendimento.getNome());
			destinatario.setEmail(atendimento.getEmail());
			destinatario.setNome(atendimento.getNome());
			destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			List<ComunicadoInternoDestinatarioVO> listDestinatario = new ArrayList<ComunicadoInternoDestinatarioVO>();
			listDestinatario.add(destinatario);
			comunicacaoSolicitante.setComunicadoInternoDestinatarioVOs(listDestinatario);
			getFacadeFactory().getEmailFacade().realizarGravacaoEmail(comunicacaoSolicitante, null, true, usuarioLogado, null);
		}
	}

	public String obterMensagemFormatadaMensagemOuvidoriaAbertura(AtendimentoVO atendimento, ConfiguracaoAtendimentoVO configAtendimento, final String mensagemTemplate, Boolean validarAcessoAplicao) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), atendimento.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimento.getCodigo().toString());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TEMPO_RESPOSTA_ATENDIMENTO.name(), configAtendimento.getTempoMaximoParaRespostaOuvidoriaPeloOuvidor().toString());
		if (validarAcessoAplicao && !atendimento.getUsername().isEmpty() && !atendimento.getSenha().isEmpty() && !atendimento.getUrlAplicacao().isEmpty()) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ACESSO_APLICACAO.name(), atendimento.executarGeracaoMensagemAcessoUrlAplicacao());
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ACESSO_APLICACAO.name(), "");
		}
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemOuvidoriaFinalizacao(AtendimentoVO atendimento, final String mensagemTemplate) {

		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), atendimento.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimento.getCodigo().toString() + " - " + atendimento.getTitulo_Apresentar_Atendimento());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_ABERTURA_ATENDIMENTO.name(), atendimento.getDataRegistroApresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ASSUNTO_ATENDIMENTO.name(), atendimento.getAssunto());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.AVALIACAO_1ESTRELA_ATENDIMENTO.name(), atendimento.getAvaliacao_1_Estrela_Apresentacao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.AVALIACAO_2ESTRELA_ATENDIMENTO.name(), atendimento.getAvaliacao_2_Estrela_Apresentacao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.AVALIACAO_3ESTRELA_ATENDIMENTO.name(), atendimento.getAvaliacao_3_Estrela_Apresentacao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.AVALIACAO_4ESTRELA_ATENDIMENTO.name(), atendimento.getAvaliacao_4_Estrela_Apresentacao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ACESSO_APLICACAO.name(), atendimento.getAcesso_Url_Apresentacao());
		return mensagemTexto;

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemOuvidoriaAtendimentoForaPrazo(AtendimentoVO atendimento, GrupoDestinatariosVO grupo, ConfiguracaoAtendimentoVO configuracao, UsuarioVO usuarioLogado) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_OUVIDORIA_ATENDIMENTO_FORA_PRAZO, false, atendimento.getUnidadeEnsino(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmailMaisResponsavelPadrao();
			for (FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO : grupo.getListaFuncionariosGrupoDestinatariosVOs()) {
				String mensagemEditada = mensagemTemplate.getMensagem();
				String mensagemSMSEditada = mensagemTemplate.getMensagemSMS();
				mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa().getNome());
				mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa().getNome());
				mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimento.getCodigo().toString());
				mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimento.getCodigo().toString());
				mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), atendimento.getResponsavelAtendimento().getPessoa().getNome());
				mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), atendimento.getResponsavelAtendimento().getPessoa().getNome());
				mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.TEMPO_RESPOSTA_ATENDIMENTO.name(), configuracao.getTempoMaximoParaRespostaOuvidoriaPeloOuvidor().toString());
				mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.TEMPO_RESPOSTA_ATENDIMENTO.name(), configuracao.getTempoMaximoParaRespostaOuvidoriaPeloOuvidor().toString());
				executarEnvioMensagemOuvidoriaPreenchimentoDadosResponsavel(mensagemTemplate, atendimento, funcionarioGrupoDestinatariosVO.getFuncionario(), config, mensagemEditada, mensagemSMSEditada);
			}
			String mensagemEditada = mensagemTemplate.getMensagem();
			String mensagemSMSEditada = mensagemTemplate.getMensagemSMS();
			mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), atendimento.getResponsavelAtendimento().getPessoa().getNome());
			mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), atendimento.getResponsavelAtendimento().getPessoa().getNome());
			mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimento.getCodigo().toString());
			mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimento.getCodigo().toString());
			mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), atendimento.getResponsavelAtendimento().getPessoa().getNome());
			mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), atendimento.getResponsavelAtendimento().getPessoa().getNome());
			mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.TEMPO_RESPOSTA_ATENDIMENTO.name(), configuracao.getTempoMaximoParaRespostaOuvidoriaPeloOuvidor().toString());
			mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.TEMPO_RESPOSTA_ATENDIMENTO.name(), configuracao.getTempoMaximoParaRespostaOuvidoriaPeloOuvidor().toString());
			executarEnvioMensagemOuvidoriaPreenchimentoDadosResponsavel(mensagemTemplate, atendimento, atendimento.getResponsavelAtendimento(), config, mensagemEditada, mensagemSMSEditada);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemAtendimentoDepartamentoForaPrazo(AtendimentoInteracaoDepartamentoVO atendimentoDepartamento, ConfiguracaoAtendimentoVO configuracao, Integer unidadeEnsino, UsuarioVO usuarioLogado) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_OUVIDORIA_ATENDIMENTODEPARTAMENTO_FORA_PRAZO, false, unidadeEnsino, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmailMaisResponsavelPadrao();
			String mensagemResponsavelAtendimento = mensagemTemplate.getMensagem();
			String mensagemSMSResponsavelAtendimento = mensagemTemplate.getMensagemSMS();
			mensagemResponsavelAtendimento = mensagemResponsavelAtendimento.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), atendimentoDepartamento.getAtendimentoVO().getResponsavelAtendimento().getPessoa().getNome());
			mensagemSMSResponsavelAtendimento = mensagemSMSResponsavelAtendimento.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), atendimentoDepartamento.getAtendimentoVO().getResponsavelAtendimento().getPessoa().getNome());
			mensagemResponsavelAtendimento = mensagemResponsavelAtendimento.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimentoDepartamento.getAtendimentoVO().getCodigo().toString());
			mensagemSMSResponsavelAtendimento = mensagemSMSResponsavelAtendimento.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimentoDepartamento.getAtendimentoVO().getCodigo().toString());
			mensagemResponsavelAtendimento = mensagemResponsavelAtendimento.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), atendimentoDepartamento.getFuncionarioVO().getPessoa().getNome());
			mensagemSMSResponsavelAtendimento = mensagemSMSResponsavelAtendimento.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), atendimentoDepartamento.getFuncionarioVO().getPessoa().getNome());
			mensagemResponsavelAtendimento = mensagemResponsavelAtendimento.replaceAll(TagsMensagemAutomaticaEnum.TEMPO_RESPOSTA_ATENDIMENTO.name(), configuracao.getTempoMaximoParaResponderCadaInteracaoEntreDepartamentos().toString());
			mensagemSMSResponsavelAtendimento = mensagemSMSResponsavelAtendimento.replaceAll(TagsMensagemAutomaticaEnum.TEMPO_RESPOSTA_ATENDIMENTO.name(), configuracao.getTempoMaximoParaResponderCadaInteracaoEntreDepartamentos().toString());
			executarEnvioMensagemOuvidoriaPreenchimentoDadosResponsavel(mensagemTemplate, atendimentoDepartamento.getAtendimentoVO(), atendimentoDepartamento.getAtendimentoVO().getResponsavelAtendimento(), config, mensagemResponsavelAtendimento, mensagemSMSResponsavelAtendimento);
			String mensagemFuncionario = mensagemTemplate.getMensagem();
			String mensagemSMSFuncionario = mensagemTemplate.getMensagemSMS();
			mensagemFuncionario = mensagemFuncionario.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), atendimentoDepartamento.getFuncionarioVO().getPessoa().getNome());
			mensagemFuncionario = mensagemFuncionario.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimentoDepartamento.getAtendimentoVO().getCodigo().toString());
			mensagemFuncionario = mensagemFuncionario.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), atendimentoDepartamento.getFuncionarioVO().getPessoa().getNome());
			mensagemFuncionario = mensagemFuncionario.replaceAll(TagsMensagemAutomaticaEnum.TEMPO_RESPOSTA_ATENDIMENTO.name(), configuracao.getTempoMaximoParaResponderCadaInteracaoEntreDepartamentos().toString());

			mensagemSMSFuncionario = mensagemSMSFuncionario.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), atendimentoDepartamento.getFuncionarioVO().getPessoa().getNome());
			mensagemSMSFuncionario = mensagemSMSFuncionario.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimentoDepartamento.getAtendimentoVO().getCodigo().toString());
			mensagemSMSFuncionario = mensagemSMSFuncionario.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), atendimentoDepartamento.getFuncionarioVO().getPessoa().getNome());
			mensagemSMSFuncionario = mensagemSMSFuncionario.replaceAll(TagsMensagemAutomaticaEnum.TEMPO_RESPOSTA_ATENDIMENTO.name(), configuracao.getTempoMaximoParaResponderCadaInteracaoEntreDepartamentos().toString());
			executarEnvioMensagemOuvidoriaPreenchimentoDadosResponsavel(mensagemTemplate, atendimentoDepartamento.getAtendimentoVO(), atendimentoDepartamento.getFuncionarioVO(), config, mensagemFuncionario, mensagemSMSFuncionario);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemOuvidoriaAtendimentoSituacaoAvaliada(String avaliacao, Integer codAtendimento, String motivoAvaliacaoRuim) throws Exception {
		AtendimentoVO atendimento = getFacadeFactory().getAtendimentoFacade().consultarPorChavePrimaria(codAtendimento, Uteis.NIVELMONTARDADOS_DADOSBASICOS, "", null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_OUVIDORIA_SITUACAO_AVALIADA, false, atendimento.getUnidadeEnsino(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			GrupoDestinatariosVO grupo = getFacadeFactory().getGrupoDestinatariosFacade().consultarGrupoDestinatarioQuandoOuvidoriaForMalAvaliadaPorUnidadeEnsino(atendimento.getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmailMaisResponsavelPadrao();
			for (FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO : grupo.getListaFuncionariosGrupoDestinatariosVOs()) {
				String mensagemEditada = mensagemTemplate.getMensagem();
				mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa().getNome());
				mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimento.getCodigo().toString());
				mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.AVALIACAO_ATENDIMENTO.name(), avaliacao);
				String mensagemSMSEditada = mensagemTemplate.getMensagemSMS();
				mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa().getNome());
				mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_ATENDIMENTO.name(), atendimento.getCodigo().toString());
				mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.AVALIACAO_ATENDIMENTO.name(), avaliacao);
				if (motivoAvaliacaoRuim.isEmpty()) {
					mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.AVALIACAO_MOTIVO_ATENDIMENTO.name(), " Não foi informado o motivo, pois a mesma foi avaliada por e-mail. ");
					mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.AVALIACAO_MOTIVO_ATENDIMENTO.name(), " Não foi informado o motivo, pois a mesma foi avaliada por e-mail. ");
				} else {
					mensagemEditada = mensagemEditada.replaceAll(TagsMensagemAutomaticaEnum.AVALIACAO_MOTIVO_ATENDIMENTO.name(), motivoAvaliacaoRuim);
					mensagemSMSEditada = mensagemSMSEditada.replaceAll(TagsMensagemAutomaticaEnum.AVALIACAO_MOTIVO_ATENDIMENTO.name(), motivoAvaliacaoRuim);
				}
				executarEnvioMensagemOuvidoriaPreenchimentoDadosResponsavel(mensagemTemplate, atendimento, funcionarioGrupoDestinatariosVO.getFuncionario(), config, mensagemEditada, mensagemSMSEditada);
			}
		}
	}

	
	@Override
	public void executarEnvioMensagemCobrancaAlunoInadimplenteSegundoConfiguracaoFinanceira() throws Exception {

		List<UnidadeEnsinoVO> listaUnidade = new ArrayList<UnidadeEnsinoVO>(0);
		listaUnidade.addAll(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoConfiguracoesPorGestaoEnvioMensagemAutomatica());

		for (UnidadeEnsinoVO obj : listaUnidade) {
			try {
				executarEnvioMensagemCobrancaAlunoInadimplentePeriodico(obj.getCodigo(), obj.getConfiguracoes().getCodigo());
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				realizarBloqueioMatriculaAlunoInadimplente(obj.getCodigo());
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				executarEnvioMensagemCobrancaAlunoInadimplente(obj.getCodigo(), obj.getConfiguracoes().getCodigo());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	
	
	@Override
	public void executarEnvioMensagemCobrancaAlunoInadimplentePeriodico(Integer codigoUnidade, Integer codigoConfiguracoes) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_COBRANCA_ALUNO_PRIMEIRA_MSG_INADIMPLENCIA, false, codigoUnidade, null);
		ConfiguracaoGeralSistemaVO config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(codigoUnidade, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			RegistroExecucaoJobVO registroExecucaoJobVO =  new RegistroExecucaoJobVO();
			registroExecucaoJobVO.setDataInicio(new Date());
			registroExecucaoJobVO.setNome("Mensagem Notificação Aluno Inadimplente Primeira Notificação.");		
			// envio primeira msg baseado na configuração
			
			List<ContaReceberCobrancaRelVO> cobrancaMatricula = getFacadeFactory().getContaReceberFacade().consultarContaReceberAlunosInadimentesPrimeiraMsg(codigoUnidade);
			if (!cobrancaMatricula.isEmpty()) {

				for (ContaReceberCobrancaRelVO obj : cobrancaMatricula) {
					List<ContaReceberCobrancaRelVO> contaReceberCobrancaRels = getFacadeFactory().getContaReceberFacade().consultarContaReceberAlunosInadimentesPrimeiraMsg(obj.getMatricula(), getAplicacaoControle().getConfiguracaoFinanceiroVO(codigoUnidade));

					ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacao.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.FINANCEIRO_COBRANCA);
					comunicacao.setAssunto(mensagemTemplate.getAssunto());
					comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
					comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
					comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
					comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacao.setData(new Date());
					comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					
					for (ContaReceberCobrancaRelVO contaReceberCobrancaRelVO : contaReceberCobrancaRels) {
						
						comunicacao.setCodigo(0);
						comunicacao.getComunicadoInternoDestinatarioVOs().clear();
						comunicacao.setCodigoTipoOrigemComunicacaoInterna(contaReceberCobrancaRelVO.getPessoaVO().getCodigo());
						comunicacao.setMensagem(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(contaReceberCobrancaRelVO, mensagemTemplate.getMensagem()));
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(contaReceberCobrancaRelVO, mensagemTemplate.getMensagemSMS()));
							comunicacao.setEnviarSMS(Boolean.TRUE);
						}

						comunicacao.setTipoDestinatario(contaReceberCobrancaRelVO.getTipoDestinatario());
						if (contaReceberCobrancaRelVO.getTipoDestinatario().equals("AL")) {
							comunicacao.setAluno(contaReceberCobrancaRelVO.getPessoaVO());
						} else {
							comunicacao.setPessoa(contaReceberCobrancaRelVO.getPessoaVO());
						}
						comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceberCobrancaRelVO.getPessoaVO(), mensagemTemplate.getEnviarCopiaPais()));
						if (!contaReceberCobrancaRelVO.getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs().isEmpty()) {
							for (FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO : contaReceberCobrancaRelVO.getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs()) {
								comunicacao.getComunicadoInternoDestinatarioVOs().addAll(obterListaDestinatarios(funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
							}
						}
						registroExecucaoJobVO.setTotal(registroExecucaoJobVO.getTotal()+1);
						try {
							getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
							getFacadeFactory().getMatriculaFacade().alterarDataPrimeiraNotificacaoAlunoInadimplente(contaReceberCobrancaRelVO.getMatricula());						
							registroExecucaoJobVO.setTotalSucesso(registroExecucaoJobVO.getTotalSucesso()+1);

						}catch (Exception e) {
							if(!registroExecucaoJobVO.getErro().isEmpty()) {								
								registroExecucaoJobVO.setErro(registroExecucaoJobVO.getErro()+"/n");
							}
							registroExecucaoJobVO.setErro("Erro ao notificar a matrícula: "+contaReceberCobrancaRelVO.getMatricula()+" - "+e.getMessage());
							registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro()+1);}						
						}
				}
			}
			try {
				registroExecucaoJobVO.setDataTermino(new Date());				
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);			
			}catch (Exception e) {

			}
		}

		PersonalizacaoMensagemAutomaticaVO mensagemTemplate2 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_COBRANCA_ALUNO_SEGUNDA_MSG_INADIMPLENCIA, false, codigoUnidade, null);
		if (mensagemTemplate2 != null && !mensagemTemplate2.getDesabilitarEnvioMensagemAutomatica()) {
			RegistroExecucaoJobVO registroExecucaoJobVO =  new RegistroExecucaoJobVO();
			registroExecucaoJobVO.setDataInicio(new Date());
			registroExecucaoJobVO.setNome("Mensagem Notificação Aluno Inadimplente Segunda Notificação.");			
			List<ContaReceberCobrancaRelVO> cobrancaMatricula = getFacadeFactory().getContaReceberFacade().consultarContaReceberAlunosInadimentesSegundaMsg(codigoUnidade);
			if (!cobrancaMatricula.isEmpty()) {

				for (ContaReceberCobrancaRelVO obj : cobrancaMatricula) {

					List<ContaReceberCobrancaRelVO> contaReceberCobrancaRelVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberAlunosInadimentesSegundaMsg(obj.getMatricula(), getAplicacaoControle().getConfiguracaoFinanceiroVO(codigoUnidade));
					ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacao.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.FINANCEIRO_COBRANCA);
					comunicacao.setAssunto(mensagemTemplate2.getAssunto());
					comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate2.getCaminhoImagemPadraoCima());
					comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate2.getCaminhoImagemPadraoBaixo());
					comunicacao.setCopiaFollowUp(mensagemTemplate2.getCopiaFollowUp());
					comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacao.setData(new Date());
					comunicacao.setEnviarEmail(true);
					for (ContaReceberCobrancaRelVO contaReceberCobrancaRelVO : contaReceberCobrancaRelVOs) {
						comunicacao.setCodigo(0);
						comunicacao.getComunicadoInternoDestinatarioVOs().clear();
						comunicacao.setCodigoTipoOrigemComunicacaoInterna(contaReceberCobrancaRelVO.getPessoaVO().getCodigo());
						comunicacao.setMensagem(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(contaReceberCobrancaRelVO, mensagemTemplate2.getMensagem()));
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(contaReceberCobrancaRelVO, mensagemTemplate2.getMensagemSMS()));
							comunicacao.setEnviarSMS(Boolean.TRUE);
						}
						comunicacao.setTipoDestinatario(contaReceberCobrancaRelVO.getTipoDestinatario());
						if (contaReceberCobrancaRelVO.getTipoDestinatario().equals("AL")) {
							comunicacao.setAluno(contaReceberCobrancaRelVO.getPessoaVO());
						} else {
							comunicacao.setPessoa(contaReceberCobrancaRelVO.getPessoaVO());
						}
						comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceberCobrancaRelVO.getPessoaVO(), mensagemTemplate.getEnviarCopiaPais()));
						if (!contaReceberCobrancaRelVO.getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs().isEmpty()) {
							for (FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO : contaReceberCobrancaRelVO.getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs()) {
								comunicacao.getComunicadoInternoDestinatarioVOs().addAll(obterListaDestinatarios(funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
							}
						}
						registroExecucaoJobVO.setTotal(registroExecucaoJobVO.getTotal()+1);
						try {
							getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
							getFacadeFactory().getMatriculaFacade().alterarDataSegundaNotificacaoAlunoInadimplente(contaReceberCobrancaRelVO.getMatricula());
							registroExecucaoJobVO.setTotalSucesso(registroExecucaoJobVO.getTotalSucesso()+1);
						}catch (Exception e) {
							if(!registroExecucaoJobVO.getErro().isEmpty()) {								
								registroExecucaoJobVO.setErro(registroExecucaoJobVO.getErro()+"/n");
							}
							registroExecucaoJobVO.setErro("Erro ao notificar a matrícula: "+contaReceberCobrancaRelVO.getMatricula()+" - "+e.getMessage());
							registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro()+1);						
						}
					}
				}

			}
			try {
				registroExecucaoJobVO.setDataTermino(new Date());				
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);			
			}catch (Exception e) {

			}
		}

		PersonalizacaoMensagemAutomaticaVO mensagemTemplate3 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_COBRANCA_ALUNO_TERCEIRA_MSG_INADIMPLENCIA, false, codigoUnidade, null);
		if (mensagemTemplate3 != null && !mensagemTemplate3.getDesabilitarEnvioMensagemAutomatica()) {
			// envio terceira msg baseado na configuração
			RegistroExecucaoJobVO registroExecucaoJobVO =  new RegistroExecucaoJobVO();
			registroExecucaoJobVO.setDataInicio(new Date());
			registroExecucaoJobVO.setNome("Mensagem Notificação Aluno Inadimplente Terceira Notificação.");		
			List<ContaReceberCobrancaRelVO> cobrancaMatricula = getFacadeFactory().getContaReceberFacade().consultarContaReceberAlunosInadimentesTerceiraMsg(codigoUnidade);
			if (!cobrancaMatricula.isEmpty()) {

				for (ContaReceberCobrancaRelVO obj : cobrancaMatricula) {

					List<ContaReceberCobrancaRelVO> contaReceberCobrancaRelVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberAlunosInadimentesTerceiraMsg(obj.getMatricula(), getAplicacaoControle().getConfiguracaoFinanceiroVO(codigoUnidade));

					ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacao.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.FINANCEIRO_COBRANCA);
					comunicacao.setAssunto(mensagemTemplate3.getAssunto());
					comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate3.getCaminhoImagemPadraoCima());
					comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate3.getCaminhoImagemPadraoBaixo());
					comunicacao.setCopiaFollowUp(mensagemTemplate3.getCopiaFollowUp());
					comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacao.setData(new Date());
					comunicacao.setEnviarEmail(true);
					for (ContaReceberCobrancaRelVO contaReceberCobrancaRelVO : contaReceberCobrancaRelVOs) {
						comunicacao.setCodigo(0);
						comunicacao.getComunicadoInternoDestinatarioVOs().clear();
						comunicacao.setCodigoTipoOrigemComunicacaoInterna(contaReceberCobrancaRelVO.getPessoaVO().getCodigo());
						comunicacao.setMensagem(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(contaReceberCobrancaRelVO, mensagemTemplate3.getMensagem()));
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(contaReceberCobrancaRelVO, mensagemTemplate3.getMensagemSMS()));
							comunicacao.setEnviarSMS(Boolean.TRUE);
						}
						comunicacao.setTipoDestinatario(contaReceberCobrancaRelVO.getTipoDestinatario());
						if (contaReceberCobrancaRelVO.getTipoDestinatario().equals("AL")) {
							comunicacao.setAluno(contaReceberCobrancaRelVO.getPessoaVO());
						} else {
							comunicacao.setPessoa(contaReceberCobrancaRelVO.getPessoaVO());
						}
						comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceberCobrancaRelVO.getPessoaVO(), mensagemTemplate.getEnviarCopiaPais()));
						if (!contaReceberCobrancaRelVO.getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs().isEmpty()) {
							for (FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO : contaReceberCobrancaRelVO.getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs()) {
								comunicacao.getComunicadoInternoDestinatarioVOs().addAll(obterListaDestinatarios(funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
							}
						}
						registroExecucaoJobVO.setTotal(registroExecucaoJobVO.getTotal()+1);
						try {
							getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
							getFacadeFactory().getMatriculaFacade().alterarDataTerceiraNotificacaoAlunoInadimplente(contaReceberCobrancaRelVO.getMatricula());
							registroExecucaoJobVO.setTotalSucesso(registroExecucaoJobVO.getTotalSucesso()+1);
						}catch (Exception e) {
							if(!registroExecucaoJobVO.getErro().isEmpty()) {								
								registroExecucaoJobVO.setErro(registroExecucaoJobVO.getErro()+"/n");
							}
							registroExecucaoJobVO.setErro("Erro ao notificar a matrícula: "+contaReceberCobrancaRelVO.getMatricula()+" - "+e.getMessage());
							registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro()+1);						
						}
					}
				}
			}
			try {
				registroExecucaoJobVO.setDataTermino(new Date());				
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);			
			}catch (Exception e) {

			}
		}
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate4 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_COBRANCA_ALUNO_QUARTA_MSG_INADIMPLENCIA, false, codigoUnidade, null);
		if (mensagemTemplate4 != null && !mensagemTemplate4.getDesabilitarEnvioMensagemAutomatica()) {
			// MENSAGEM QUARTA COBRANÇA
			RegistroExecucaoJobVO registroExecucaoJobVO =  new RegistroExecucaoJobVO();
			registroExecucaoJobVO.setDataInicio(new Date());
			registroExecucaoJobVO.setNome("Mensagem Notificação Aluno Inadimplente Quarta Notificação.");		
			List<ContaReceberCobrancaRelVO> contaReceberCobrancaRelVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberAlunosInadimentesQuartaMsgPrimeiraConsulta(codigoUnidade);
			if (!contaReceberCobrancaRelVOs.isEmpty()) {

				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.FINANCEIRO_COBRANCA);
				comunicacao.setAssunto(mensagemTemplate4.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate4.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate4.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate4.getCopiaFollowUp());
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setEnviarEmail(true);
				for (ContaReceberCobrancaRelVO contaReceberCobrancaRelVO : contaReceberCobrancaRelVOs) {

					List<ContaReceberCobrancaRelVO> contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberAlunosInadimentesQuartaMensagem(contaReceberCobrancaRelVO.getMatricula(), getAplicacaoControle().getConfiguracaoFinanceiroVO(codigoUnidade));
					for (ContaReceberCobrancaRelVO obj : contaReceberVOs) {

						comunicacao.setCodigo(0);
						comunicacao.getComunicadoInternoDestinatarioVOs().clear();
						comunicacao.setCodigoTipoOrigemComunicacaoInterna(obj.getPessoaVO().getCodigo());
						comunicacao.setMensagem(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(obj, mensagemTemplate4.getMensagem()));
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(obj, mensagemTemplate4.getMensagemSMS()));
							comunicacao.setEnviarSMS(Boolean.TRUE);
						}
						comunicacao.setTipoDestinatario(obj.getTipoDestinatario());
						if (obj.getTipoDestinatario().equals("AL")) {
							comunicacao.setAluno(obj.getPessoaVO());
						} else {
							comunicacao.setPessoa(obj.getPessoaVO());
						}
						comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(obj.getPessoaVO(), mensagemTemplate.getEnviarCopiaPais()));
						if (!obj.getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs().isEmpty()) {
							for (FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO : obj.getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs()) {
								comunicacao.getComunicadoInternoDestinatarioVOs().addAll(obterListaDestinatarios(funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
							}
						}
						registroExecucaoJobVO.setTotal(registroExecucaoJobVO.getTotal()+1);
						try {

						getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
						getFacadeFactory().getMatriculaFacade().alterarDataQuartaNotificacaoAlunoInadimplente(obj.getMatricula());
						registroExecucaoJobVO.setTotalSucesso(registroExecucaoJobVO.getTotalSucesso()+1);
					}catch (Exception e) {
						if(!registroExecucaoJobVO.getErro().isEmpty()) {								
							registroExecucaoJobVO.setErro(registroExecucaoJobVO.getErro()+"/n");
						}
						registroExecucaoJobVO.setErro("Erro ao notificar a matrícula: "+contaReceberCobrancaRelVO.getMatricula()+" - "+e.getMessage());
						registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro()+1);						
					}
					}
				}
			}
			try {
				registroExecucaoJobVO.setDataTermino(new Date());				
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);			
			}catch (Exception e) {

			}
		}

	}

	@Override
	public void executarEnvioMensagemCobrancaAlunoInadimplente(Integer codigoUnidade, Integer codigoConfiguracoes) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_COBRANCA_ALUNO_INADIMPLENCIA, false, codigoUnidade, null);
		ConfiguracaoGeralSistemaVO config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(codigoUnidade, null);		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			RegistroExecucaoJobVO registroExecucaoJobVO =  new RegistroExecucaoJobVO();
			registroExecucaoJobVO.setDataInicio(new Date());
			registroExecucaoJobVO.setNome("Mensagem Notificação Aluno Inadimplente.");
			List<ContaReceberCobrancaRelVO> contas = getFacadeFactory().getContaReceberFacade().consultarContaReceberAlunosInadimentes(codigoUnidade);
			if (!contas.isEmpty()) {

				for (ContaReceberCobrancaRelVO obj : contas) {

					List<ContaReceberCobrancaRelVO> contaReceberCobrancaRelVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberAlunosInadimentes(obj.getMatricula(), getAplicacaoControle().getConfiguracaoFinanceiroVO(codigoUnidade));

					ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacao.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.FINANCEIRO_COBRANCA);
					comunicacao.setAssunto(mensagemTemplate.getAssunto());
					comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
					comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
					comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
					comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacao.setData(new Date());
					comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					for (ContaReceberCobrancaRelVO contaReceberCobrancaRelVO : contaReceberCobrancaRelVOs) {
						comunicacao.setCodigo(0);
						comunicacao.getComunicadoInternoDestinatarioVOs().clear();
						comunicacao.setCodigoTipoOrigemComunicacaoInterna(contaReceberCobrancaRelVO.getPessoaVO().getCodigo());
						comunicacao.setMensagem(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(contaReceberCobrancaRelVO, mensagemTemplate.getMensagem()));
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(contaReceberCobrancaRelVO, mensagemTemplate.getMensagemSMS()));
							comunicacao.setEnviarSMS(Boolean.TRUE);
						}
						comunicacao.setTipoDestinatario(contaReceberCobrancaRelVO.getTipoDestinatario());
						if (contaReceberCobrancaRelVO.getTipoDestinatario().equals("AL")) {
							comunicacao.setAluno(contaReceberCobrancaRelVO.getPessoaVO());
						} else {
							comunicacao.setPessoa(contaReceberCobrancaRelVO.getPessoaVO());
						}
						comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceberCobrancaRelVO.getPessoaVO(), mensagemTemplate.getEnviarCopiaPais()));
						if (!contaReceberCobrancaRelVO.getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs().isEmpty()) {
							for (FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO : contaReceberCobrancaRelVO.getGrupoDestinatariosVO().getListaFuncionariosGrupoDestinatariosVOs()) {
								comunicacao.getComunicadoInternoDestinatarioVOs().addAll(obterListaDestinatarios(funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
							}
						}
						registroExecucaoJobVO.setTotal(registroExecucaoJobVO.getTotal()+1);
						try {
							getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
							getFacadeFactory().getMatriculaFacade().alterarDataNotificacaoAlunoInadimplente(contaReceberCobrancaRelVO.getMatricula());	
							registroExecucaoJobVO.setTotalSucesso(registroExecucaoJobVO.getTotalSucesso()+1);
						}catch (Exception e) {
							if(!registroExecucaoJobVO.getErro().isEmpty()) {								
								registroExecucaoJobVO.setErro(registroExecucaoJobVO.getErro()+"/n");
							}
							registroExecucaoJobVO.setErro("Erro ao notificar a matrícula: "+contaReceberCobrancaRelVO.getMatricula()+" - "+e.getMessage());
							registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro()+1);						
						}
					}
				}

			}
			try {
				registroExecucaoJobVO.setDataTermino(new Date());				
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);			
			}catch (Exception e) {

			}
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
	public void realizarBloqueioMatriculaAlunoInadimplente(Integer codigoUnidade) {
		RegistroExecucaoJobVO registroExecucaoJobVO =  new RegistroExecucaoJobVO();
		registroExecucaoJobVO.setDataInicio(new Date());
		registroExecucaoJobVO.setNome("Job Suspensão Matrícula Aluno Inadimplente.");
		int x = 0;
		try {
		StringBuilder sql = new StringBuilder(" UPDATE matricula SET matriculasuspensa = true, databasesuspensao =  current_date WHERE matricula.matricula IN ( ");
		sql.append(" SELECT DISTINCT matriculaaluno FROM contareceber ");
		sql.append(" INNER JOIN matricula ON matricula.matricula = matriculaaluno ");
		sql.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
		// sql.append(" INNER JOIN configuracoes ON unidadeensino.configuracoes
		// = configuracoes.codigo ");
		// sql.append(" INNER JOIN configuracaofinanceiro ON
		// configuracaofinanceiro.configuracoes = configuracoes.codigo ");
		sql.append(" inner join configuracaoFinanceiro on configuracaoFinanceiro.configuracoes = (case when unidadeensino.configuracoes is not null then unidadeensino.configuracoes else (select c.codigo from configuracoes c where c.padrao = true ) end)");
		sql.append(" WHERE contareceber.situacao = 'AR'  ");
		sql.append(" and (");
		sql.append("   (configuracaoFinanceiro.tipoOrigemMatriculaRotinaInadimplencia         and contareceber.tipoorigem = 'MAT') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemBibliotecaRotinaInadimplencia        and contareceber.tipoorigem = 'BIB') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemMensalidadeRotinaInadimplencia       and contareceber.tipoorigem = 'MEN') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemDevolucaoChequeRotinaInadimplencia   and contareceber.tipoorigem = 'DCH') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemNegociacaoRotinaInadimplencia        and contareceber.tipoorigem = 'NCR') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemContratoReceitaRotinaInadimplencia   and contareceber.tipoorigem = 'CTR') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemOutrosRotinaInadimplencia            and contareceber.tipoorigem = 'OUT') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemMaterialDidaticoRotinaInadimplencia  and contareceber.tipoorigem = 'MDI') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemInclusaoReposicaoRotinaInadimplencia and contareceber.tipoorigem = 'IRE') ");
		sql.append(") ");
		sql.append(" AND configuracaofinanceiro.numerodiasbloquearacessoalunoinadimplente IS NOT NULL AND numerodiasbloquearacessoalunoinadimplente > 0 ");
		sql.append(" AND (matricula.liberarbloqueioalunoinadimplente IS NULL OR matricula.liberarbloqueioalunoinadimplente = false) ");
		sql.append(" AND contareceber.datavencimento < current_date AND DATE_PART('day', current_date - datavencimento) >= numerodiasbloquearacessoalunoinadimplente ");
		sql.append(" AND (matricula.matriculasuspensa IS NULL OR matricula.matriculasuspensa = false) ");
		sql.append(" AND matricula.situacao IN ('AT', 'CO') ");
		sql.append(" AND unidadeensino.codigo =").append(codigoUnidade).append(" )");								
		x = getConexao().getJdbcTemplate().update(sql.toString());
		}catch (Exception e) {
			registroExecucaoJobVO.setDataTermino(new Date());
			registroExecucaoJobVO.setErro(e.getMessage());
		}finally {
			if(x>0 || !registroExecucaoJobVO.getErro().isEmpty()) {
				registroExecucaoJobVO.setTotalSucesso(x);
				try {
					getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
		}
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemCobrancaAlunoInadimplenteContaReceberEspecifica(ContaReceberVO contaReceber) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_COBRANCA_ALUNO_INADIMPLENCIA, false, contaReceber.getUnidadeEnsino().getCodigo(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			if (!Uteis.isAtributoPreenchido(config.getResponsavelPadraoComunicadoInterno())) {
				throw new Exception(UteisJSF.internacionalizar("msg_ContaReceber_responsavelPadraoComunicadoInternoNaoInformado"));
			}
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.FINANCEIRO_COBRANCA);
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setCodigoTipoOrigemComunicacaoInterna(contaReceber.getPessoa().getCodigo());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(contaReceber, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(contaReceber, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}
			comunicacao.setTipoDestinatario(contaReceber.getTipoPessoa());
			if (contaReceber.getTipoAluno()) {
				comunicacao.setAluno(contaReceber.getPessoa());
			} else if (contaReceber.getTipoResponsavelFinanceiro()) {
				comunicacao.setPessoa(contaReceber.getResponsavelFinanceiro());
			} else {
				comunicacao.setPessoa(contaReceber.getPessoa());
			}
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceber.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);

		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_ContaReceber_cobrancaSemMensagemPadrao"));
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemAvisoDesconto(Integer codigoUnidade, Integer codigoConfiguracoes) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_AVISO_DESCONTO_ALUNO, false, codigoUnidade, null);
		ConfiguracaoGeralSistemaVO config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(codigoUnidade, null);
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, codigoUnidade, null);		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			List<ContaReceberCobrancaRelVO> contas = getFacadeFactory().getContaReceberFacade().consultarContaReceberAvisoDesconto(codigoUnidade, configuracaoFinanceiroVO, new UsuarioVO());
			if (!contas.isEmpty()) {
				for (ContaReceberCobrancaRelVO contaReceberCobrancaRelVO : contas) {
					ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacao.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.SUPORTE_ALUNO);
					comunicacao.setAssunto(mensagemTemplate.getAssunto());
					comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
					comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
					comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
					comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacao.setData(new Date());
					comunicacao.setCodigo(0);
					comunicacao.getComunicadoInternoDestinatarioVOs().clear();
					comunicacao.setCodigoTipoOrigemComunicacaoInterna(contaReceberCobrancaRelVO.getPessoaVO().getCodigo());
					comunicacao.setMensagem(obterMensagemFormatadaMensagemAvisoDesconto(contaReceberCobrancaRelVO, mensagemTemplate.getMensagem()));
					if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
						comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemAvisoDesconto(contaReceberCobrancaRelVO, mensagemTemplate.getMensagemSMS()));
						comunicacao.setEnviarSMS(Boolean.TRUE);
					}
					comunicacao.setTipoDestinatario(contaReceberCobrancaRelVO.getTipoDestinatario());
					if (contaReceberCobrancaRelVO.getTipoDestinatario().equals("AL")) {
						comunicacao.setAluno(contaReceberCobrancaRelVO.getPessoaVO());
					} else {
						comunicacao.setPessoa(contaReceberCobrancaRelVO.getPessoaVO());
					}
					comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(contaReceberCobrancaRelVO.getPessoaVO(), mensagemTemplate.getEnviarCopiaPais()));
					comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
				}
			}
		}
	}
	
	public String obterMensagemFormatadaMensagemAvisoDesconto(ContaReceberCobrancaRelVO contaReceberCobrancaRelVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), contaReceberCobrancaRelVO.getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), contaReceberCobrancaRelVO.getMatricula());
		StringBuilder listaContaReceber = new StringBuilder("<ul>");
		for (ContaReceberVO contaReceberVO : contaReceberCobrancaRelVO.getContaReceberVOs()) {
			
			listaContaReceber.append("<li>");
			listaContaReceber.append(" Parcela: <strong>").append(contaReceberVO.getParcelaPorConfiguracaoFinanceira_Apresentar()).append("</strong> ");
			listaContaReceber.append(" Valor: <strong>").append(Uteis.getDoubleFormatado(contaReceberVO.getValor())).append("</strong> ");			
			listaContaReceber.append(" Data Vencimento: <strong>").append(Uteis.getData(contaReceberVO.getDataVencimento())).append("</strong> ");
			listaContaReceber.append("<p>Descrição dos Descontos abaixo:</p>");
			for (PlanoFinanceiroAlunoDescricaoDescontosVO plano : contaReceberVO.getListaDescontosAplicavesContaReceber()) {
				listaContaReceber.append("<p>");
				plano.setAcrescimo(contaReceberVO.getAcrescimo());
				listaContaReceber.append(plano.getDescricaoDetalhadaComDataLimites());
				listaContaReceber.append("</p>");
			}
			listaContaReceber.append("</li>");
		}
		listaContaReceber.append("</ul>");
		mensagemTexto = mensagemTexto.replace(TagsMensagemAutomaticaEnum.LISTA_PARCELAS.name(), listaContaReceber.toString());
		return mensagemTexto;

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemCobrancaAlunoDocumentacaoPendente() throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_DOCUMENTACAO_PENDENTE, false, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			Map<String, List<DebitoDocumentosAlunoRelVO>> listaAlunos = getFacadeFactory().getDebitoDocumentosAlunoRelFacade().realizarGeracaoListaAlunoComDocumentacaoPendenteEnvioEmail();

			for (String key : listaAlunos.keySet()) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(key, 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
				PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_DOCUMENTACAO_PENDENTE, false, 
						matriculaVO.getUnidadeEnsino().getCodigo(), null);
				mensagemTemplateUtilizar = mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica() ? mensagemTemplateUtilizar : mensagemTemplate;
				List<DebitoDocumentosAlunoRelVO> debitoDocumentosAlunoRelVOs = listaAlunos.get(key);
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setAssunto(mensagemTemplateUtilizar.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplateUtilizar.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplateUtilizar.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplateUtilizar.getCopiaFollowUp());
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunoDocumentacaoPendente(debitoDocumentosAlunoRelVOs, mensagemTemplateUtilizar.getMensagem()));
				if (!mensagemTemplateUtilizar.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemAlunoDocumentacaoPendente(debitoDocumentosAlunoRelVOs, mensagemTemplateUtilizar.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("AL");
				comunicacao.setAluno(debitoDocumentosAlunoRelVOs.get(0).getMatriculaVO().getAluno());
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(debitoDocumentosAlunoRelVOs.get(0).getMatriculaVO().getAluno(), mensagemTemplateUtilizar.getEnviarCopiaPais()));
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
			}
			getFacadeFactory().getMatriculaFacade().atualizarDataEnvioNotificacaoPendenciaDocumento(listaAlunos.keySet());

		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemAlunoFrequenciaBaixa() throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_FREQUENCIA_BAIXA, false, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			Map<String, List<AlunoComFrequenciaBaixaRelVO>> listaAlunos = getFacadeFactory().getRegistroAulaFacade().consultarMatriculaComFrequenciaBaixaEmailAutomatico();
			List<Integer> matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<Integer>(0);
			for (String key : listaAlunos.keySet()) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(key, 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
				PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_FREQUENCIA_BAIXA, false, 
						matriculaVO.getUnidadeEnsino().getCodigo(), null);
				mensagemTemplateUtilizar = mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica() ? mensagemTemplateUtilizar : mensagemTemplate;
				List<AlunoComFrequenciaBaixaRelVO> alunoComFrequenciaBaixaRelVOs = listaAlunos.get(key);
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setAssunto(mensagemTemplateUtilizar.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplateUtilizar.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplateUtilizar.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplateUtilizar.getCopiaFollowUp());
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunoFrequenciaBaixa(alunoComFrequenciaBaixaRelVOs, mensagemTemplateUtilizar.getMensagem()));
				if (!mensagemTemplateUtilizar.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemAlunoFrequenciaBaixa(alunoComFrequenciaBaixaRelVOs, mensagemTemplateUtilizar.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("AL");
				comunicacao.setAluno(alunoComFrequenciaBaixaRelVOs.get(0).getAluno());
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(alunoComFrequenciaBaixaRelVOs.get(0).getAluno(), mensagemTemplateUtilizar.getEnviarCopiaPais()));
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
				for (AlunoComFrequenciaBaixaRelVO alunoComFrequenciaBaixaRelVO : alunoComFrequenciaBaixaRelVOs) {
					matriculaPeriodoTurmaDisciplinaVOs.add(alunoComFrequenciaBaixaRelVO.getMatriculaPeriodoDisciplina());
				}
			}
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().atualizarDataNotificacaoFrequenciaBaixa(matriculaPeriodoTurmaDisciplinaVOs);

		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemFuncionarioDocumentoIndeferido(DocumetacaoMatriculaVO documentacaoMatricula, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(documentacaoMatricula.getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_FUNCIONARIO_DOCUMENTO_INDEFERIDO, false, matriculaVO.getUnidadeEnsino().getCodigo(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			MatriculaVO mat = new MatriculaVO();
			mat.setMatricula(documentacaoMatricula.getMatricula());
			getFacadeFactory().getMatriculaFacade().carregarDados(mat, usuario);

			documentacaoMatricula.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(documentacaoMatricula.getUsuario().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));

			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunoDocumentoIndeferido(documentacaoMatricula, mat, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemAlunoDocumentoIndeferido(documentacaoMatricula, mat, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}
			comunicacao.setTipoDestinatario("FU");
			comunicacao.setPessoa(usuario.getPessoa());
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(documentacaoMatricula.getUsuario().getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemAlunoDocumentoIndeferido(DocumetacaoMatriculaVO documentacaoMatricula, UsuarioVO usuario) throws Exception {
		if(documentacaoMatricula.getLocalUpload().equals("Aluno")) {
			enviarMensagemIndeferimentoAluno(documentacaoMatricula, usuario);
		}else {
			enviarMensagemIndeferimentoAluno(documentacaoMatricula, usuario);
			enviarMensagemIndeferimentoResponsavelUpload(documentacaoMatricula, usuario);
		}
	}
	
	
	public void enviarMensagemIndeferimentoResponsavelUpload(DocumetacaoMatriculaVO documentacaoMatricula, UsuarioVO usuario) throws Exception {
		MatriculaVO mat = new MatriculaVO();
		mat.setMatricula(documentacaoMatricula.getMatricula());
		getFacadeFactory().getMatriculaFacade().carregarDados(mat, usuario);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_INDEFERIMENTO_DOCUMENTACAO_ALUNO_RESPONSAVEL_UPLOAD, false, mat.getUnidadeEnsino().getCodigo(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();

			documentacaoMatricula.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(documentacaoMatricula.getUsuario().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));

			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunoDocumentoIndeferido(documentacaoMatricula, mat, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemAlunoDocumentoIndeferido(documentacaoMatricula, mat, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}
			comunicacao.setTipoDestinatario("FU");
			comunicacao.setPessoa(usuario.getPessoa());
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(documentacaoMatricula.getUsuario().getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
		}
	}

	private void enviarMensagemIndeferimentoAluno(DocumetacaoMatriculaVO documentacaoMatricula, UsuarioVO usuario) throws Exception {
		MatriculaVO mat = new MatriculaVO();
		mat.setMatricula(documentacaoMatricula.getMatricula());
		getFacadeFactory().getMatriculaFacade().carregarDados(mat, usuario);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_DOCUMENTO_INDEFERIDO, false, mat.getUnidadeEnsino().getCodigo(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();

			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunoDocumentoIndeferido(documentacaoMatricula, mat, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemAlunoDocumentoIndeferido(documentacaoMatricula, mat, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setPessoa(usuario.getPessoa());
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(mat.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemAlunoDocumentoPostado(MatriculaVO matricula, List<DocumetacaoMatriculaVO> documentos, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_DOCUMENTO_POSTADO, false, matricula.getUnidadeEnsino().getCodigo(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && Uteis.isAtributoPreenchido(documentos)) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunoDocumentos(matricula, documentos, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemAlunoDocumentos(matricula, documentos, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setAluno(matricula.getAluno());
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matricula.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
			Iterator<DocumetacaoMatriculaVO> i = documentos.iterator();
			while (i.hasNext()) {
				DocumetacaoMatriculaVO doc = (DocumetacaoMatriculaVO) i.next();
				getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentoMatriculaAlunoPostagemNotificado(doc, usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemAlunoDocumentoDeferido(MatriculaVO matricula, List<DocumetacaoMatriculaVO> documentos, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_DOCUMENTO_DEFERIDO, false, matricula.getUnidadeEnsino().getCodigo(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			getFacadeFactory().getPessoaFacade().carregarDados(matricula.getAluno(), usuario);
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunoDocumentos(matricula, documentos, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemAlunoDocumentos(matricula, documentos, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setAluno(matricula.getAluno());
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matricula.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
			Iterator<DocumetacaoMatriculaVO> i = documentos.iterator();
			while (i.hasNext()) {
				DocumetacaoMatriculaVO doc = (DocumetacaoMatriculaVO) i.next();
				getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentoMatriculaNotificado(doc, usuario);
			}
		}else {
			throw new Exception("Não foi encontrado um modelo de mensagem habilitado no sistema.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemAlunoSolicitouAvisoAulaFuturo() throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_SOLICITOU_AVISO_AULA_FUTURA, false, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			List<HistoricoVO> listaAlunos = getFacadeFactory().getHistoricoFacade().consultarAlunoSolicitouAvisoAulaFutura();
			Map<Integer, PersonalizacaoMensagemAutomaticaVO> consultarPorUnidadeEnsino = new HashMap<>();
			if (Uteis.isAtributoPreenchido(listaAlunos)) {
				List<Integer> unidadeEnsinos = listaAlunos.stream().map(h -> h.getMatricula().getUnidadeEnsino().getCodigo()).distinct().collect(Collectors.toList());
				consultarPorUnidadeEnsino = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorUnidadeEnsino(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_SOLICITOU_AVISO_AULA_FUTURA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, unidadeEnsinos, null);
			}
			Iterator<HistoricoVO> i = listaAlunos.iterator();
			while (i.hasNext()) {
				HistoricoVO historicoVO = (HistoricoVO) i.next();
				PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = null;
				if (Uteis.isAtributoPreenchido(consultarPorUnidadeEnsino) && consultarPorUnidadeEnsino.containsKey(historicoVO.getMatricula().getUnidadeEnsino().getCodigo())) {
					mensagemTemplateUtilizar = consultarPorUnidadeEnsino.get(historicoVO.getMatricula().getUnidadeEnsino().getCodigo());
					mensagemTemplateUtilizar = mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica() ? mensagemTemplateUtilizar : mensagemTemplate;
				} else {
					mensagemTemplateUtilizar = mensagemTemplate;
				}
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setAssunto(mensagemTemplateUtilizar.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplateUtilizar.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplateUtilizar.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplateUtilizar.getCopiaFollowUp());
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunoSolicitouAvisoAulaFuturo(historicoVO, mensagemTemplateUtilizar.getMensagem()));
				if (!mensagemTemplateUtilizar.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemAlunoSolicitouAvisoAulaFuturo(historicoVO, mensagemTemplateUtilizar.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("AL");
				comunicacao.setAluno(historicoVO.getMatricula().getAluno());
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(historicoVO.getMatricula().getAluno(), mensagemTemplateUtilizar.getEnviarCopiaPais()));
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, new UsuarioVO(), config,null);
				getFacadeFactory().getHistoricoFacade().alterarNotificarSolicitacaoReposicao(historicoVO.getCodigo(), false);
			}

		}

	}

	public String obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(ContaReceberVO contaReceberVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), contaReceberVO.getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SACADO.name(), contaReceberVO.getResponsavelFinanceiro().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), contaReceberVO.getMatriculaAluno().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), contaReceberVO.getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.QUANTIDADE_PARCELAS.name(), "1");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.VALOR_TOTAL_PARCELAS.name(), Uteis.getDoubleFormatado(contaReceberVO.getValor()));
		StringBuilder listaContaReceber = new StringBuilder("<ul>");
		listaContaReceber.append("<li>");
		listaContaReceber.append("Parcela: <strong>").append(contaReceberVO.getParcelaPorConfiguracaoFinanceira_Apresentar()).append("</strong>");
		listaContaReceber.append("    Valor: <strong>").append(Uteis.getDoubleFormatado(contaReceberVO.getValorBaseContaReceber() - contaReceberVO.getListaDescontosAplicavesContaReceber().get(0).getValorDescontoSemValidade())).append("</strong>");
		listaContaReceber.append("    Data Vencimento: <strong>").append(Uteis.getData(contaReceberVO.getDataVencimento())).append("</strong>");
		listaContaReceber.append("</li>");
		listaContaReceber.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_PARCELAS.name(), listaContaReceber.toString());
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemCobrancaAlunoInadimplente(ContaReceberCobrancaRelVO contaReceberCobrancaRelVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), contaReceberCobrancaRelVO.getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), contaReceberCobrancaRelVO.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), contaReceberCobrancaRelVO.getUnidadeEnsino());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.QUANTIDADE_PARCELAS.name(), contaReceberCobrancaRelVO.getQuantidadeParcela().toString());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.VALOR_TOTAL_PARCELAS.name(), Uteis.getDoubleFormatado(contaReceberCobrancaRelVO.getValorTotalParcela()));
		if (contaReceberCobrancaRelVO.getEmailResponsavelCobranca() != null || !contaReceberCobrancaRelVO.getEmailResponsavelCobranca().trim().equals("")) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.EMAIL_RESPONSAVEL_COBRANCA.name(), contaReceberCobrancaRelVO.getEmailResponsavelCobranca());
		}
		StringBuilder listaContaReceber = new StringBuilder("<ul>");
		for (ContaReceberVO contaReceberVO : contaReceberCobrancaRelVO.getContaReceberVOs()) {
			listaContaReceber.append("<li>");
			listaContaReceber.append("Parcela: <strong>").append(contaReceberVO.getParcelaPorConfiguracaoFinanceira_Apresentar()).append("</strong>");
			listaContaReceber.append("    Valor: <strong>").append(Uteis.getDoubleFormatado(contaReceberVO.getValorReceberCalculado())).append("</strong>");
			;
			listaContaReceber.append("    Data Vencimento: <strong>").append(Uteis.getData(contaReceberVO.getDataVencimento())).append("</strong>");
			;
			listaContaReceber.append("</li>");
		}
		listaContaReceber.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_PARCELAS.name(), listaContaReceber.toString());
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemAlunoDocumentacaoPendente(List<DebitoDocumentosAlunoRelVO> debitoDocumentosAlunoRelVOs, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), debitoDocumentosAlunoRelVOs.get(0).getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), debitoDocumentosAlunoRelVOs.get(0).getMatriculaVO().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), debitoDocumentosAlunoRelVOs.get(0).getMatriculaVO().getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), debitoDocumentosAlunoRelVOs.get(0).getMatriculaVO().getCurso().getNome());
		StringBuilder listaDocs = new StringBuilder("<ul>");
		for (DebitoDocumentosAlunoRelVO debitoDocumentosAlunoRelVO : debitoDocumentosAlunoRelVOs) {
			listaDocs.append("<li>");
			listaDocs.append(" <strong>").append(debitoDocumentosAlunoRelVO.getTipoDocumento()).append("</strong>");
			listaDocs.append("</li>");
		}
		listaDocs.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_DOCUMENTOS.name(), listaDocs.toString());
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemAlunoFrequenciaBaixa(List<AlunoComFrequenciaBaixaRelVO> alunoComFrequenciaBaixaRelVOs, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), alunoComFrequenciaBaixaRelVOs.get(0).getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), alunoComFrequenciaBaixaRelVOs.get(0).getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), alunoComFrequenciaBaixaRelVOs.get(0).getUnidadeEnsino());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), alunoComFrequenciaBaixaRelVOs.get(0).getCurso());
		StringBuilder listaDocs = new StringBuilder("<ul>");
		for (AlunoComFrequenciaBaixaRelVO alunoComFrequenciaBaixaRelVO : alunoComFrequenciaBaixaRelVOs) {
			listaDocs.append("<li>");
			listaDocs.append(" <strong>").append(alunoComFrequenciaBaixaRelVO.getDisciplina()).append("</strong>");
			listaDocs.append("</li>");
		}
		listaDocs.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_DISCIPLINAS.name(), listaDocs.toString());
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemAlunoDocumentos(MatriculaVO mat, List<DocumetacaoMatriculaVO> documentos, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), mat.getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), mat.getMatricula());
		StringBuilder listaDocs = new StringBuilder("<ul>");
		for (DocumetacaoMatriculaVO docVO : documentos) {
			listaDocs.append("<li>");
			listaDocs.append(" <strong>").append(docVO.getTipoDeDocumentoVO().getNome()).append("</strong>");
			listaDocs.append("</li>");
		}
		listaDocs.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DOCUMENTOS.name(), listaDocs.toString());
		return mensagemTexto;

	}

	public String obterMensagemFormatadaMensagemAlunoDocumentoIndeferido(DocumetacaoMatriculaVO doc, MatriculaVO mat, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), mat.getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), doc.getUsuario().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), mat.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_TIPO_DOCUMENTO.name(), doc.getTipoDeDocumentoVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OBSERVACAO_PEDAGOGICO.name(), doc.getJustificativaNegacao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MOTIVO_INDEFERIMENTO.name(), doc.getMotivoIndeferimentoDocumentoAlunoVO().getNome());
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemAlunoSolicitouAvisoAulaFuturo(HistoricoVO historico, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), historico.getMatricula().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), historico.getMatricula().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), historico.getMatricula().getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), historico.getMatricula().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), historico.getDisciplina().getNome());
		return mensagemTexto;

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemInscricaoProcessoSeletivo(InscricaoVO inscricaoVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_INSCRICAO_PROCESSO_SELETIVO, false, inscricaoVO.getUnidadeEnsino().getCodigo(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			if (inscricaoVO.getItemProcessoSeletivoDataProva().getCodigo().intValue() > 0) {
				inscricaoVO.setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(inscricaoVO.getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, inscricaoVO.getResponsavel()));
			}
			comunicacao.setMensagem(obterMensagemFormatadaMensagemInscicaoProcessoSeletivo(inscricaoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemInscicaoProcessoSeletivo(inscricaoVO, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}
			comunicacao.setTipoDestinatario("Al");
			comunicacao.setAluno(inscricaoVO.getCandidato());
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(inscricaoVO.getCandidato(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().enviarEmailComunicacaoInterna(comunicacao, new UsuarioVO(), config, mensagemTemplate, null, false);

		}
	}

	public String obterMensagemFormatadaMensagemInscicaoProcessoSeletivo(InscricaoVO inscricaoVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CANDIDATO.name(), inscricaoVO.getCandidato().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_INSCRICAO.name(), inscricaoVO.getCodigo().toString());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.PROCESSO_SELETIVO.name(), inscricaoVO.getProcSeletivo().getDescricao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_PROVA.name(), Uteis.getData(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva(), "dd/MM/yyyy"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.HORA_PROVA.name(), inscricaoVO.getItemProcessoSeletivoDataProva().getHora());
		if (inscricaoVO.getOpcaoLinguaEstrangeira().getCodigo() > 0) {
			if (inscricaoVO.getOpcaoLinguaEstrangeira().getNome().trim().isEmpty()) {
				try {
					inscricaoVO.setOpcaoLinguaEstrangeira(getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(inscricaoVO.getOpcaoLinguaEstrangeira().getCodigo(), new UsuarioVO()));
					mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), inscricaoVO.getOpcaoLinguaEstrangeira().getNome());
				} catch (Exception e) {
					mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), "Não informado.");
				}
			} else {
				mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), inscricaoVO.getOpcaoLinguaEstrangeira().getNome());
			}
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), "Não informado.");
		}
		StringBuilder cursosInscritos = new StringBuilder("<ul>");
		if (inscricaoVO.getCursoOpcao1().getCodigo() > 0) {

			if (inscricaoVO.getCursoOpcao1().getCurso().getNome().trim().isEmpty() || inscricaoVO.getCursoOpcao1().getTurno().getNome().trim().isEmpty()) {
				try {
					inscricaoVO.setCursoOpcao1(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(inscricaoVO.getCursoOpcao1().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>1ª Opção: ").append(inscricaoVO.getCursoOpcao1().getCurso().getNome()).append(" - ").append(inscricaoVO.getCursoOpcao1().getTurno().getNome()).append("</strong>");
			cursosInscritos.append("</li>");
		}
		if (inscricaoVO.getCursoOpcao2().getCodigo() > 0) {

			if (inscricaoVO.getCursoOpcao2().getCurso().getNome().trim().isEmpty() || inscricaoVO.getCursoOpcao2().getTurno().getNome().trim().isEmpty()) {
				try {
					inscricaoVO.setCursoOpcao2(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(inscricaoVO.getCursoOpcao2().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>2ª Opção: ").append(inscricaoVO.getCursoOpcao2().getCurso().getNome()).append(" - ").append(inscricaoVO.getCursoOpcao2().getTurno().getNome()).append("</strong>");
			cursosInscritos.append("</li>");
		}
		if (inscricaoVO.getCursoOpcao3().getCodigo() > 0) {
			if (inscricaoVO.getCursoOpcao3().getCurso().getNome().trim().isEmpty() || inscricaoVO.getCursoOpcao3().getTurno().getNome().trim().isEmpty()) {
				try {
					inscricaoVO.setCursoOpcao3(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(inscricaoVO.getCursoOpcao3().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>3ª Opção: ").append(inscricaoVO.getCursoOpcao3().getCurso().getNome()).append(" - ").append(inscricaoVO.getCursoOpcao3().getTurno().getNome()).append("</strong>");
			cursosInscritos.append("</li>");
		}

		cursosInscritos.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CURSOS_INSCRITOS.name(), cursosInscritos.toString());
		return mensagemTexto;

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemConfirmacaoPagamentoInscricaoProcessoSeletivo(Integer inscricao, Integer unidadeEnsino) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_CONFIRMACAO_INSCRICAO_PROCESSO_SELETIVO, false, unidadeEnsino, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && inscricao != null && inscricao > 0) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarDadosEnvioNotificacaoEmail(inscricao);
			if (!inscricaoVO.isNovoObj()) {
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setAssunto(mensagemTemplate.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setMensagem(obterMensagemFormatadaMensagemConfirmacaoInscicaoProcessoSeletivo(inscricaoVO, mensagemTemplate.getMensagem()));
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemConfirmacaoInscicaoProcessoSeletivo(inscricaoVO, mensagemTemplate.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("AL");
				comunicacao.setAluno(inscricaoVO.getCandidato());
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(inscricaoVO.getCandidato(), mensagemTemplate.getEnviarCopiaPais()));
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().enviarEmailComunicacaoInterna(comunicacao, new UsuarioVO(), config, mensagemTemplate, null, false);
			}

		}
	}

	public String obterMensagemFormatadaMensagemConfirmacaoInscicaoProcessoSeletivo(InscricaoVO inscricaoVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CANDIDATO.name(), inscricaoVO.getCandidato().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_INSCRICAO.name(), inscricaoVO.getCodigo().toString());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.PROCESSO_SELETIVO.name(), inscricaoVO.getProcSeletivo().getDescricao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_PROVA.name(), inscricaoVO.getProcSeletivo().getDataProva_Apresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.HORA_PROVA.name(), inscricaoVO.getProcSeletivo().getHorarioProva());
		if (inscricaoVO.getOpcaoLinguaEstrangeira().getCodigo() > 0) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), inscricaoVO.getOpcaoLinguaEstrangeira().getNome());
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), "Não informado.");
		}
		StringBuilder cursosInscritos = new StringBuilder("<ul>");
		if (inscricaoVO.getCursoOpcao1().getCurso().getCodigo() > 0) {
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>1ª Opção: ").append(inscricaoVO.getCursoOpcao1().getCurso().getNome()).append(" - ").append(inscricaoVO.getCursoOpcao1().getTurno().getNome()).append("</strong>");
			cursosInscritos.append("</li>");
		}
		if (inscricaoVO.getCursoOpcao2().getCurso().getCodigo() > 0) {
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>2ª Opção: ").append(inscricaoVO.getCursoOpcao2().getCurso().getNome()).append(" - ").append(inscricaoVO.getCursoOpcao2().getTurno().getNome()).append("</strong>");
			cursosInscritos.append("</li>");
		}
		if (inscricaoVO.getCursoOpcao3().getCurso().getCodigo() > 0) {
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>3ª Opção: ").append(inscricaoVO.getCursoOpcao3().getCurso().getNome()).append(" - ").append(inscricaoVO.getCursoOpcao3().getTurno().getNome()).append("</strong>");
			cursosInscritos.append("</li>");
		}

		cursosInscritos.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CURSOS_INSCRITOS.name(), cursosInscritos.toString());
		return mensagemTexto;

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemResultadoProcessoSeletivo(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, Integer unidadeEnsino) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = null;
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RESULTADO_PROCESSO_SELETIVO, false, 
					unidadeEnsino, null);
		} else {
			mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RESULTADO_PROCESSO_SELETIVO, false, 
					resultadoProcessoSeletivoVO.getInscricao().getUnidadeEnsino().getCodigo(), null);
		}
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			if (!resultadoProcessoSeletivoVO.isNovoObj()) {
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setAssunto(mensagemTemplate.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setMensagem(obterMensagemFormatadaMensagemResultadoProcessoSeletivo(resultadoProcessoSeletivoVO, mensagemTemplate.getMensagem()));
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemResultadoProcessoSeletivo(resultadoProcessoSeletivoVO, mensagemTemplate.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("AL");
				comunicacao.setAluno(resultadoProcessoSeletivoVO.getInscricao().getCandidato());
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(resultadoProcessoSeletivoVO.getInscricao().getCandidato(), mensagemTemplate.getEnviarCopiaPais()));
				getFacadeFactory().getComunicacaoInternaFacade().enviarEmailComunicacaoInterna(comunicacao, new UsuarioVO(), config, mensagemTemplate, null, false);
			}

		}
	}

	public String obterMensagemFormatadaMensagemResultadoProcessoSeletivo(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CANDIDATO.name(), resultadoProcessoSeletivoVO.getInscricao().getCandidato().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_INSCRICAO.name(), resultadoProcessoSeletivoVO.getInscricao().getCodigo().toString());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.PROCESSO_SELETIVO.name(), resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getDescricao());
		if (resultadoProcessoSeletivoVO.getInscricao().getOpcaoLinguaEstrangeira().getCodigo() > 0) {
			if (resultadoProcessoSeletivoVO.getInscricao().getOpcaoLinguaEstrangeira().getNome().trim().isEmpty()) {
				try {
					resultadoProcessoSeletivoVO.getInscricao().setOpcaoLinguaEstrangeira(getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(resultadoProcessoSeletivoVO.getInscricao().getOpcaoLinguaEstrangeira().getCodigo(), new UsuarioVO()));
					mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), resultadoProcessoSeletivoVO.getInscricao().getOpcaoLinguaEstrangeira().getNome());
				} catch (Exception e) {
					mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), "Não informado.");
				}
			} else {
				mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), "Não informado.");
			}
		}
		StringBuilder cursosInscritos = new StringBuilder("<ul>");
		if (resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao1().getCodigo() > 0) {

			if (resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao1().getCurso().getNome().trim().isEmpty() || resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao1().getTurno().getNome().trim().isEmpty()) {
				try {
					resultadoProcessoSeletivoVO.getInscricao().setCursoOpcao1(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao1().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>1ª Opção: ").append(resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao1().getCurso().getNome()).append(" - ").append(resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao1().getTurno().getNome()).append(":</strong> ").append(resultadoProcessoSeletivoVO.getResultadoPrimeiraOpcao_Apresentar());
			cursosInscritos.append("</li>");
		}
		if (resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao2().getCodigo() > 0) {
			if (resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao2().getCurso().getNome().trim().isEmpty() || resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao2().getTurno().getNome().trim().isEmpty()) {
				try {
					resultadoProcessoSeletivoVO.getInscricao().setCursoOpcao2(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao2().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>2ª Opção: ").append(resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao2().getCurso().getNome()).append(" - ").append(resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao2().getTurno().getNome()).append(":</strong> ").append(resultadoProcessoSeletivoVO.getResultadoSegundaOpcao_Apresentar());
			cursosInscritos.append("</li>");
		}
		if (resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao3().getCodigo() > 0) {
			if (resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao3().getCurso().getNome().trim().isEmpty() || resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao3().getTurno().getNome().trim().isEmpty()) {
				try {
					resultadoProcessoSeletivoVO.getInscricao().setCursoOpcao3(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao3().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>3ª Opção: ").append(resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao3().getCurso().getNome()).append(" - ").append(resultadoProcessoSeletivoVO.getInscricao().getCursoOpcao3().getTurno().getNome()).append(":</strong> ").append(resultadoProcessoSeletivoVO.getResultadoTerceiraOpcao_Apresentar());
			cursosInscritos.append("</li>");
		}

		cursosInscritos.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CURSOS_INSCRITOS.name(), cursosInscritos.toString());
		return mensagemTexto;

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemNovaVagaBancoCurriculum(VagasVO vagasVO, Boolean enviarSomenteParaCidadeInformada, UsuarioVO usuarioVO, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, List<PessoaVO> pessoaVOs) throws Exception {
		// PersonalizacaoMensagemAutomaticaVO mensagemTemplate =
		// getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOVA_VAGA_BANCO_CURRICULUM,
		// false, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			if (!vagasVO.isNovoObj() && vagasVO.getSituacao().equals("AT")) {
				// List<PessoaVO> pessoaVOs =
				// getFacadeFactory().getPessoaFacade().consultarPessoaInteresseAreaProfissinal(vagasVO.getAreaProfissional(),
				// enviarSomenteParaCidadeInformada ? vagasVO.getCidade() :
				// null);
				for (PessoaVO pessoaVO : pessoaVOs) {
					ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
					comunicacao.setAssunto(mensagemTemplate.getAssunto());
					comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
					comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
					comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
					comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacao.setData(new Date());
					comunicacao.setCodigo(0);
					comunicacao.getComunicadoInternoDestinatarioVOs().clear();
					comunicacao.setMensagem(obterMensagemFormatadaMensagemNovaVagaBancoCurriculum(vagasVO, pessoaVO, mensagemTemplate.getMensagem()));
					if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
						comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNovaVagaBancoCurriculum(vagasVO, pessoaVO, mensagemTemplate.getMensagemSMS()));
						comunicacao.setEnviarSMS(Boolean.TRUE);
					}
					comunicacao.setTipoDestinatario("AL");
					comunicacao.setAluno(pessoaVO);
					comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
					comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);

					// getFacadeFactory().getComunicacaoInternaFacade().enviarEmailComunicacaoInterna(comunicacao,
					// usuarioVO, config);
				}
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemSemTemplate(String assunto, String mensagem, UsuarioVO usuarioVO, List<PessoaVO> pessoaVOs) throws Exception {
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		for (PessoaVO pessoaVO : pessoaVOs) {
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setAssunto(assunto);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setEnviarEmail(true);
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setMensagem(comunicacao.getMensagemComLayout(mensagem));
			comunicacao.setTipoDestinatario("FU");
			comunicacao.setPessoa(pessoaVO);
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, false));
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNovaVagaBancoCurriculum(VagasVO vagasVO, PessoaVO pessoaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), pessoaVO.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CIDADE.name(), pessoaVO.getCidade().getNome().toString());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ESTADO.name(), pessoaVO.getCidade().getEstado().getSigla().toString());
		if (vagasVO.getCargo().trim().equals("")) {
			vagasVO.setCargo("Dados não informados pela empresa");
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_VAGA.name(), vagasVO.getCargo());

		if (vagasVO.getEmpresaSigilosaParaVaga()) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PARCEIRO.name(), "<Não Disponível>");
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PARCEIRO.name(), vagasVO.getParceiro().getNome());
		}
		if (vagasVO.getNumeroVagas().toString().trim().equals("")) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_VAGA.name(), "Dados não informados pela empresa");
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_VAGA.name(), vagasVO.getNumeroVagas().toString());
		}
		if (vagasVO.getEscolaridadeRequerida().toString().trim().equals("")) {
			vagasVO.setEscolaridadeRequerida("Dados não informados pela empresa");
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ESCOLARIDADE_REQUERIDA.name(), vagasVO.getEscolaridadeRequerida());
		if (vagasVO.getConhecimentoEspecifico().trim().equals("")) {
			vagasVO.setConhecimentoEspecifico("Dados não informados pela empresa");
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONHECIMENTOS_ESPECIFICOS.name(), vagasVO.getConhecimentoEspecifico().toString());
		if (vagasVO.getPrincipalAtividadeCargo().trim().equals("")) {
			vagasVO.setPrincipalAtividadeCargo("Dados não informados pela empresa");
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.PRINCIPAIS_ATIVIDADES.name(), vagasVO.getPrincipalAtividadeCargo().toString());
		if (vagasVO.getCaracteristicaPessoalImprescindivel().trim().equals("")) {
			vagasVO.setConhecimentoEspecifico("Dados não informados pela empresa");
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CARAC_IMPORTANTES.name(), vagasVO.getCaracteristicaPessoalImprescindivel().toString());
		String disponibilidade = "";
		if (vagasVO.getViagem().booleanValue()) {
			disponibilidade += "Viagens";
		}
		if (vagasVO.getMudanca().booleanValue()) {
			if (vagasVO.getViagem().booleanValue()) {
				disponibilidade += "| Mudanças";
			} else {
				disponibilidade += "Mudanças";
			}
		}
		if (disponibilidade.equals("")) {
			disponibilidade = "Dados não informados pela empresa";
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DISPONIBILIDADE.name(), disponibilidade);
		String software = "";
		if (vagasVO.getWindows().booleanValue()) {
			software += "Windows";
		}
		if (vagasVO.getWord().booleanValue()) {
			if (software.equals("")) {
				software += "Word";
			} else {
				software += "| Word";
			}
		}
		if (vagasVO.getAccess().booleanValue()) {
			if (software.equals("")) {
				software += "Access";
			} else {
				software += "| Access";
			}
		}
		if (vagasVO.getPowerPoint().booleanValue()) {
			if (software.equals("")) {
				software += "PowerPoint";
			} else {
				software += "| PowerPoint";
			}
		}
		if (vagasVO.getInternet().booleanValue()) {
			if (software.equals("")) {
				software += "Internet";
			} else {
				software += "| Internet";
			}
		}
		if (vagasVO.getSap().booleanValue()) {
			if (software.equals("")) {
				software += "SAP";
			} else {
				software += "| SAP";
			}
		}
		if (vagasVO.getCorelDraw().booleanValue()) {
			if (software.equals("")) {
				software += "CorelDraw";
			} else {
				software += "| CorelDraw";
			}
		}
		if (vagasVO.getAutoCad().booleanValue()) {
			if (software.equals("")) {
				software += "AutoCad";
			} else {
				software += "| AutoCad";
			}
		}
		if (vagasVO.getPhotoshop().booleanValue()) {
			if (software.equals("")) {
				software += "Photoshop";
			} else {
				software += "| Photoshop";
			}
		}
		if (vagasVO.getMicrosiga().booleanValue()) {
			if (software.equals("")) {
				software += "Microsiga";
			} else {
				software += "| Microsiga";
			}
		}
		if (!vagasVO.getOutrosSoftwares().equals("")) {
			if (software.equals("")) {
				software += "Outros Softwares: " + vagasVO.getOutrosSoftwares();
			} else {
				software += "| Outros Softwares: " + vagasVO.getOutrosSoftwares();
			}
		}
		if (software.equals("")) {
			software = "Dados não informados pela empresa";
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SOFTWARE.name(), software);
		String idioma = "";
		if (vagasVO.getIngles().booleanValue()) {
			idioma += "Inglês, nível: " + vagasVO.getNivel_Apresentar(vagasVO.getInglesNivel());
		}
		if (vagasVO.getEspanhol().booleanValue()) {
			if (idioma.equals("")) {
				idioma += "Espanhol, nível: " + vagasVO.getNivel_Apresentar(vagasVO.getEspanholNivel());
			} else {
				idioma += "| Espanhol, nível: " + vagasVO.getNivel_Apresentar(vagasVO.getEspanholNivel());
			}
		}
		if (vagasVO.getFrances().booleanValue()) {
			if (idioma.equals("")) {
				idioma += "Francês, nível: " + vagasVO.getNivel_Apresentar(vagasVO.getFrancesNivel());
			} else {
				idioma += "| Francês, nível: " + vagasVO.getNivel_Apresentar(vagasVO.getFrancesNivel());
			}
		}
		if (idioma.equals("")) {
			idioma = "Dados não informados pela empresa";
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.IDIOMA.name(), idioma);
		if (vagasVO.getBeneficios().equals("")) {
			vagasVO.setBeneficios("Dados não informados pela empresa");
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.BENEFICIOS.name(), vagasVO.getBeneficios().toString());
		if (vagasVO.getHorarioTrabalho().equals("")) {
			vagasVO.setHorarioTrabalho("Dados não informados pela empresa");
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.HORARIO_TRABALHO.name(), vagasVO.getHorarioTrabalho().toString());
		if (vagasVO.getSalario_Apresentar().equals("")) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SALARIO.name(), "Dados não informados pela empresa");
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SALARIO.name(), vagasVO.getSalario_Apresentar());
		}
		if (vagasVO.getNumeroVagas().toString().trim().equals("")) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NR_VAGA.name(), "Dados não informados pela empresa");
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NR_VAGA.name(), vagasVO.getNumeroVagas().toString());
		}
		return mensagemTexto;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemProfessorPostagemMaterial(List<NotificacaoProfessorPostarMaterialVO> notificacaoProfessorPostarMaterialVOs) throws Exception {

		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		UsuarioVO usuarioVO = new UsuarioVO();
		for (NotificacaoProfessorPostarMaterialVO notificacaoRegistroAulaNaoLancadaVO : notificacaoProfessorPostarMaterialVOs) {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate1 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_PROFESSOR_POSTAGEM_MATERIAL, false, notificacaoRegistroAulaNaoLancadaVO.getUnidadeEnsino(), null);
			if ((mensagemTemplate1 != null && !mensagemTemplate1.getDesabilitarEnvioMensagemAutomatica())) {
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setAssunto(mensagemTemplate1.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate1.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate1.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate1.getCopiaFollowUp());
				comunicacao.setMensagem(obterMensagemFormatadaMensagemProfessorPostagemMaterial(notificacaoRegistroAulaNaoLancadaVO, mensagemTemplate1.getMensagem()));
				if (!mensagemTemplate1.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemProfessorPostagemMaterial(notificacaoRegistroAulaNaoLancadaVO, mensagemTemplate1.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("PR");
				comunicacao.setProfessor(notificacaoRegistroAulaNaoLancadaVO.getProfessor());
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(notificacaoRegistroAulaNaoLancadaVO.getProfessor(), mensagemTemplate1.getEnviarCopiaPais()));
				comunicacao.setEnviarEmail(mensagemTemplate1.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate1.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemCoordenadorInicioTurma(List<NotificacaoProfessorPostarMaterialVO> notificacaoProfessorPostarMaterialVOs) throws Exception {

		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		UsuarioVO usuarioVO = new UsuarioVO();
		for (NotificacaoProfessorPostarMaterialVO notificacaoRegistroAulaNaoLancadaVO : notificacaoProfessorPostarMaterialVOs) {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate1 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_COORDENADOR_INICIO_TURMA, false, notificacaoRegistroAulaNaoLancadaVO.getUnidadeEnsino(), null);
			if ((mensagemTemplate1 != null && !mensagemTemplate1.getDesabilitarEnvioMensagemAutomatica())) {
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setAssunto(mensagemTemplate1.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate1.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate1.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate1.getCopiaFollowUp());
				comunicacao.setMensagem(obterMensagemFormatadaMensagemCoordenadorInicioTurma(notificacaoRegistroAulaNaoLancadaVO, mensagemTemplate1.getMensagem()));
				if (!mensagemTemplate1.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemCoordenadorInicioTurma(notificacaoRegistroAulaNaoLancadaVO, mensagemTemplate1.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("PR");
				comunicacao.setProfessor(notificacaoRegistroAulaNaoLancadaVO.getProfessor());
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(notificacaoRegistroAulaNaoLancadaVO.getProfessor(), mensagemTemplate1.getEnviarCopiaPais()));
				comunicacao.setEnviarEmail(mensagemTemplate1.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate1.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
			}
		}
	}

	public String obterMensagemFormatadaMensagemProfessorPostagemMaterial(NotificacaoProfessorPostarMaterialVO notificacaoProfessorPostarMaterialVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), notificacaoProfessorPostarMaterialVO.getProfessor().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), notificacaoProfessorPostarMaterialVO.getTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), notificacaoProfessorPostarMaterialVO.getDisciplina());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_AULA.name(), Uteis.getData(notificacaoProfessorPostarMaterialVO.getDataAula(), "dd/MM/yyyy"));
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemCoordenadorInicioTurma(NotificacaoProfessorPostarMaterialVO notificacaoProfessorPostarMaterialVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_COORDENADOR.name(), notificacaoProfessorPostarMaterialVO.getProfessor().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), notificacaoProfessorPostarMaterialVO.getTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), notificacaoProfessorPostarMaterialVO.getDisciplina());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_AULA.name(), Uteis.getData(notificacaoProfessorPostarMaterialVO.getDataAula(), "dd/MM/yyyy"));
		return mensagemTexto;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemNotificacaoNaoLacamentoNota() throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate1 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_PRIMEIRA_NOTIFICACAO_NAO_LANCAMENTO_AULA, false, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate2 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_SEGUNDA_NOTIFICACAO_NAO_LANCAMENTO_AULA, false, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate3 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TERCEIRA_NOTIFICACAO_NAO_LANCAMENTO_AULA, false, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate4 = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_QUARTA_NOTIFICACAO_NAO_LANCAMENTO_AULA, false, null);
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		UsuarioVO usuarioVO = new UsuarioVO();
		if ((mensagemTemplate1 != null && !mensagemTemplate1.getDesabilitarEnvioMensagemAutomatica()) || (mensagemTemplate2 != null && !mensagemTemplate2.getDesabilitarEnvioMensagemAutomatica())) {
			List<NotificacaoRegistroAulaNaoLancadaVO> noAulaNaoLancadaVOs = getFacadeFactory().getRegistroAulaFacade().consultarDadosNotificacaoNaoLancamentoRegistroAula();
			for (NotificacaoRegistroAulaNaoLancadaVO notificacaoRegistroAulaNaoLancadaVO : noAulaNaoLancadaVOs) {
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				if (notificacaoRegistroAulaNaoLancadaVO.getTipoNotificacao().equals("MENSAGEM_PRIMEIRA_NOTIFICACAO_NAO_LANCAMENTO_AULA")) {
					preencherMensagemNotificacaoNaoLacamentoNota(mensagemTemplate1, notificacaoRegistroAulaNaoLancadaVO, comunicacao, config, usuarioVO);
					comunicacao.setEnviarEmail(mensagemTemplate1.getEnviarEmail());
					comunicacao.setEnviarEmailInstitucional(mensagemTemplate1.getEnviarEmailInstitucional());
				} else if (notificacaoRegistroAulaNaoLancadaVO.getTipoNotificacao().equals("MENSAGEM_SEGUNDA_NOTIFICACAO_NAO_LANCAMENTO_AULA")) {
					preencherMensagemNotificacaoNaoLacamentoNota(mensagemTemplate2, notificacaoRegistroAulaNaoLancadaVO, comunicacao, config, usuarioVO);
					comunicacao.setEnviarEmail(mensagemTemplate2.getEnviarEmail());
					comunicacao.setEnviarEmailInstitucional(mensagemTemplate2.getEnviarEmailInstitucional());
				} else if (notificacaoRegistroAulaNaoLancadaVO.getTipoNotificacao().equals("MENSAGEM_TERCEIRA_NOTIFICACAO_NAO_LANCAMENTO_AULA")) {
					preencherMensagemNotificacaoNaoLacamentoNota(mensagemTemplate3, notificacaoRegistroAulaNaoLancadaVO, comunicacao, config, usuarioVO);
					comunicacao.setEnviarEmail(mensagemTemplate3.getEnviarEmail());
					comunicacao.setEnviarEmailInstitucional(mensagemTemplate3.getEnviarEmailInstitucional());
				} else if (notificacaoRegistroAulaNaoLancadaVO.getTipoNotificacao().equals("MENSAGEM_QUARTA_NOTIFICACAO_NAO_LANCAMENTO_AULA")) {
					preencherMensagemNotificacaoNaoLacamentoNota(mensagemTemplate4, notificacaoRegistroAulaNaoLancadaVO, comunicacao, config, usuarioVO);
					comunicacao.setEnviarEmail(mensagemTemplate4.getEnviarEmail());
					comunicacao.setEnviarEmailInstitucional(mensagemTemplate4.getEnviarEmailInstitucional());
				}

			}
		}
	}

	private void preencherMensagemNotificacaoNaoLacamentoNota(PersonalizacaoMensagemAutomaticaVO mensagemTemplate, NotificacaoRegistroAulaNaoLancadaVO notificacaoRegistroAulaNaoLancadaVO, ComunicacaoInternaVO comunicacao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		comunicacao.setAssunto(mensagemTemplate.getAssunto());
		comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
		comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
		comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
		comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoNaoLacamentoNota(notificacaoRegistroAulaNaoLancadaVO, mensagemTemplate.getMensagem()));
		if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
			comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoNaoLacamentoNota(notificacaoRegistroAulaNaoLancadaVO, mensagemTemplate.getMensagemSMS()));
			comunicacao.setEnviarSMS(Boolean.TRUE);
		}
		comunicacao.setTipoDestinatario("PR");
		comunicacao.setProfessor(notificacaoRegistroAulaNaoLancadaVO.getProfessor());
		comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(notificacaoRegistroAulaNaoLancadaVO.getProfessor(), mensagemTemplate.getEnviarCopiaPais()));
		getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
	}

	public String obterMensagemFormatadaMensagemNotificacaoNaoLacamentoNota(NotificacaoRegistroAulaNaoLancadaVO notificacaoRegistroAulaNaoLancadaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), notificacaoRegistroAulaNaoLancadaVO.getProfessor().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), notificacaoRegistroAulaNaoLancadaVO.getTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), notificacaoRegistroAulaNaoLancadaVO.getDisciplina());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_AULA.name(), Uteis.getData(notificacaoRegistroAulaNaoLancadaVO.getDataAula(), "dd/MM/yyyy"));
		return mensagemTexto;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemNotificacaoLocalAulaTurma(MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO, Integer codGrupoDestinatario, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getTurmaDisciplinaFacade().alterarLocalSala(mapaLocalAulaTurmaVO.getTurmaDisciplina(), usuario);
		mapaLocalAulaTurmaVO.getTurmaDisciplina().setLocalAula(getFacadeFactory().getLocalAulaFacade().consultarPorChavePrimaria(mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		getFacadeFactory().getTurmaFacade().carregarDados(mapaLocalAulaTurmaVO.getTurma(), usuario);		
		mapaLocalAulaTurmaVO.getTurma().setUnidadeEnsino(getAplicacaoControle().getUnidadeEnsinoVO(mapaLocalAulaTurmaVO.getTurma().getUnidadeEnsino().getCodigo(), usuario));
		List<ArquivoVO> listaAluno = new ArrayList<ArquivoVO>();
		Iterator<MaterialAlunoVO> i = mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getMaterialAlunoVOs().iterator();
		while (i.hasNext()) {
			MaterialAlunoVO mat = (MaterialAlunoVO) i.next();
			listaAluno.add(mat.getArquivoVO());
		}
		List<ArquivoVO> listaProfessor = new ArrayList<ArquivoVO>();
		Iterator<MaterialProfessorVO> j = mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getMaterialProfessorVOs().iterator();
		while (j.hasNext()) {
			MaterialProfessorVO mat = (MaterialProfessorVO) j.next();
			listaProfessor.add(mat.getArquivoVO());
		}
		List<PessoaVO> funcionarioVOs = getFacadeFactory().getPessoaFacade().consultaRapidaPessoaGrupoDestinatario(codGrupoDestinatario, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		mapaLocalAulaTurmaVO.getTurmaDisciplina().setSalaLocalAula(getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(mapaLocalAulaTurmaVO.getTurmaDisciplina().getSalaLocalAula().getCodigo()));
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_LOCAL_AULA_TURMA_ALUNO, false, mapaLocalAulaTurmaVO.getTurma().getUnidadeEnsino().getCodigo(), null);
		if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			List<PessoaVO> pessoaVOs = getFacadeFactory().getPessoaFacade().consultaRapidaAlunosPorCodigoTurmaSituacaoEDisciplina(mapaLocalAulaTurmaVO.getTurma(), "AT", mapaLocalAulaTurmaVO.getTurmaDisciplina().getDisciplina().getCodigo(), false, usuarioVO);
			for (PessoaVO pessoaVO : pessoaVOs) {
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setAssunto(mensagemTemplate.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoLocalAulaTurmaAluno(mapaLocalAulaTurmaVO, pessoaVO, mensagemTemplate.getMensagem()));
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoLocalAulaTurmaAluno(mapaLocalAulaTurmaVO, pessoaVO, mensagemTemplate.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("AL");
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
				if (!listaAluno.isEmpty()) {
					comunicacao.setListaArquivosAnexo(listaAluno);
				}
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
			}
			for (PessoaVO pessoaVO : funcionarioVOs) {
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setAssunto(mensagemTemplate.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoLocalAulaTurmaAluno(mapaLocalAulaTurmaVO, pessoaVO, mensagemTemplate.getMensagem()));
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoLocalAulaTurmaAluno(mapaLocalAulaTurmaVO, pessoaVO, mensagemTemplate.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("FU");
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
				if (!listaAluno.isEmpty()) {
					comunicacao.setListaArquivosAnexo(listaAluno);
				}
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
			}

		}

		mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_LOCAL_AULA_TURMA_PROFESSOR, false, mapaLocalAulaTurmaVO.getTurma().getUnidadeEnsino().getCodigo(), null);
		if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoLocalAulaTurmaProfessor(mapaLocalAulaTurmaVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoLocalAulaTurmaProfessor(mapaLocalAulaTurmaVO, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}
			comunicacao.setTipoDestinatario("PR");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(mapaLocalAulaTurmaVO.getProfessor(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);

			// for (PessoaVO pessoaVO : funcionarioVOs) {
			// comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			// comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			// comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			// comunicacao.setData(new Date());
			// comunicacao.setEnviarEmail(true);
			// comunicacao.setCodigo(0);
			// comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			// comunicacao.setAssunto(mensagemTemplate.getAssunto());
			// comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoLocalAulaTurmaProfessor(mapaLocalAulaTurmaVO,
			// mensagemTemplate.getMensagem()));
			// comunicacao.setTipoDestinatario("FU");
			// comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO));
			// if (!listaAluno.isEmpty()) {
			// comunicacao.setListaArquivosAnexo(listaAluno);
			// }
			// getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao,
			// false, usuarioVO, config,null);
			// }
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoLocalAulaTurmaAluno(MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO, PessoaVO aluno, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), aluno.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), mapaLocalAulaTurmaVO.getProfessor().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), mapaLocalAulaTurmaVO.getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), mapaLocalAulaTurmaVO.getTurma().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), mapaLocalAulaTurmaVO.getTurma().getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.EMAIL_UNIDADE_ENSINO.name(), mapaLocalAulaTurmaVO.getTurma().getUnidadeEnsino().getEmail());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.HORARIO_AULA.name(), mapaLocalAulaTurmaVO.getDatasAulaStr());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getDisciplina().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LOCAL_AULA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getLocal());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ENDERECO_AULA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getEndereco());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OBSERVACAO_AULA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getObservacao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TELEFONE_AULA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getTelefone());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SALA_AULA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getSalaLocalAula().getSala());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DESCRICAO_TURNO.name(), mapaLocalAulaTurmaVO.getTurma().getTurno().getDescricaoTurnoContrato());
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemNotificacaoLocalAulaTurmaProfessor(MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), mapaLocalAulaTurmaVO.getProfessor().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), mapaLocalAulaTurmaVO.getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), mapaLocalAulaTurmaVO.getTurma().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), mapaLocalAulaTurmaVO.getTurma().getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.EMAIL_UNIDADE_ENSINO.name(), mapaLocalAulaTurmaVO.getTurma().getUnidadeEnsino().getEmail());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.HORARIO_AULA.name(), mapaLocalAulaTurmaVO.getDatasAulaStr());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NR_MODULOS.name(), String.valueOf(mapaLocalAulaTurmaVO.getNrModulo()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.QTD_ALUNOS.name(), String.valueOf(mapaLocalAulaTurmaVO.getQtdeAluno() + mapaLocalAulaTurmaVO.getQtdeAlunoExtRep()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getDisciplina().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LOCAL_AULA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getLocal());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ENDERECO_AULA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getEndereco());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OBSERVACAO_AULA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getObservacao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TELEFONE_AULA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getTelefone());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SALA_AULA.name(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getSalaLocalAula().getSala());
		return mensagemTexto;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemRequerimentoEmAtraso() throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_EM_ATRASO, false, null);
		if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			PessoaVO pessoaVO = null;
			List<Integer> requerimentos = new ArrayList<Integer>(0);
			SqlRowSet rs = getFacadeFactory().getRequerimentoFacade().consultarDadosParaNotificacaoAtraso();
			while (rs.next()) {
				requerimentos.add(rs.getInt("codigo"));
				pessoaVO = new PessoaVO();
				pessoaVO.setCodigo(rs.getInt("Pessoa.codigo"));
				pessoaVO.setNome(rs.getString("Pessoa.nome"));
				pessoaVO.setEmail(rs.getString("Pessoa.email"));
				
				PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_DUVIDA_PROFESSOR_FINALIZADA, false, Uteis.isAtributoPreenchido(rs.getInt("unidadeensino")) ? rs.getInt("unidadeensino") : 0, null);
				mensagemTemplateUtilizar = mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica() ? mensagemTemplateUtilizar : mensagemTemplate;
				if (!pessoaVO.getEmail().equals("")) {
					ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
					comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacao.setData(new Date());
					comunicacao.setCodigo(0);
					comunicacao.getComunicadoInternoDestinatarioVOs().clear();
					comunicacao.setAssunto(mensagemTemplateUtilizar.getAssunto());
					comunicacao.setCaminhoImagemPadraoCima(mensagemTemplateUtilizar.getCaminhoImagemPadraoCima());
					comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplateUtilizar.getCaminhoImagemPadraoBaixo());
					comunicacao.setCopiaFollowUp(mensagemTemplateUtilizar.getCopiaFollowUp());
					comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoRequerimentoEmAtraso(rs, mensagemTemplateUtilizar.getMensagem()));
					if (!mensagemTemplateUtilizar.getDesabilitarEnvioMensagemSMSAutomatica()) {
						comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoRequerimentoEmAtraso(rs, mensagemTemplateUtilizar.getMensagemSMS()));
						comunicacao.setEnviarSMS(Boolean.TRUE);
					}
					comunicacao.setTipoDestinatario("AL");
					comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplateUtilizar.getEnviarCopiaPais()));
					comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
				}
				getFacadeFactory().getRequerimentoFacade().executarRegistroNotificacaoRequerimentoEmAtraso(requerimentos);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemAniversarianteDia() throws Exception {
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		// / PROFESSOR
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = null;
		mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_PROFESSOR_ANIVERSARIANTE_DIA, false, null);
		if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
			UsuarioVO usuarioVO = new UsuarioVO();
			PessoaVO pessoaVO = null;
			SqlRowSet rs = getFacadeFactory().getPessoaFacade().consultarAniversariantesDia(true, false, false, false);
			while (rs.next()) {
				pessoaVO = new PessoaVO();
				pessoaVO.setCodigo(rs.getInt("Pessoa.codigo"));
				pessoaVO.setNome(rs.getString("Pessoa.nome"));
				pessoaVO.setEmail(rs.getString("Pessoa.email"));
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setAssunto("Feliz Aniversário");
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoAniversarianteDia(rs, mensagemTemplate.getMensagem()));
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoAniversarianteDia(rs, mensagemTemplate.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("PR");
				// comunicacao.setProfessor(pessoaVO);
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
			}
		}
		// / FUNCIONARIO
		mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_FUNCIONARIO_ANIVERSARIANTE_DIA, false, null);
		if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
			UsuarioVO usuarioVO = new UsuarioVO();
			PessoaVO pessoaVO = null;
			SqlRowSet rs = getFacadeFactory().getPessoaFacade().consultarAniversariantesDia(false, true, false, false);
			while (rs.next()) {
				pessoaVO = new PessoaVO();
				pessoaVO.setCodigo(rs.getInt("Pessoa.codigo"));
				pessoaVO.setNome(rs.getString("Pessoa.nome"));
				pessoaVO.setEmail(rs.getString("Pessoa.email"));
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setEnviarEmail(true);
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setAssunto("Feliz Aniversário");
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoAniversarianteDia(rs, mensagemTemplate.getMensagem()));
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoAniversarianteDia(rs, mensagemTemplate.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("FU");
				// comunicacao.getFuncionario().setCodigo(rs.getInt("Funcionario.codFunc"));
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
			}
		}
		// / ALUNO
		mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALUNO_ANIVERSARIANTE_DIA, false, null);
		if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
			UsuarioVO usuarioVO = new UsuarioVO();
			PessoaVO pessoaVO = null;
			SqlRowSet rs = getFacadeFactory().getPessoaFacade().consultarAniversariantesDia(false, false, true, false);
			while (rs.next()) {
				pessoaVO = new PessoaVO();
				pessoaVO.setCodigo(rs.getInt("Pessoa.codigo"));
				pessoaVO.setNome(rs.getString("Pessoa.nome"));
				pessoaVO.setEmail(rs.getString("Pessoa.email"));
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setEnviarEmail(true);
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setAssunto("Feliz Aniversário");
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoAniversarianteDia(rs, mensagemTemplate.getMensagem()));
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoAniversarianteDia(rs, mensagemTemplate.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("AL");
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
			}
		}
		// / EX-ALUNO
		mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_EXALUNO_ANIVERSARIANTE_DIA, false, null);
		if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
			UsuarioVO usuarioVO = new UsuarioVO();
			PessoaVO pessoaVO = null;
			SqlRowSet rs = getFacadeFactory().getPessoaFacade().consultarAniversariantesDia(false, false, false, true);
			while (rs.next()) {
				pessoaVO = new PessoaVO();
				pessoaVO.setCodigo(rs.getInt("Pessoa.codigo"));
				pessoaVO.setNome(rs.getString("Pessoa.nome"));
				pessoaVO.setEmail(rs.getString("Pessoa.email"));
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setAssunto("Feliz Aniversário");
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoAniversarianteDia(rs, mensagemTemplate.getMensagem()));
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoAniversarianteDia(rs, mensagemTemplate.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setTipoDestinatario("AL");
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
			}
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoRequerimentoEmAtraso(SqlRowSet rs, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.RESPONSAVEL_DEPARTAMENTO.name(), rs.getString("Pessoa.nome"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_REQUERIMENTO.name(), Uteis.getData(rs.getDate("data")));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TIPO_REQUERIMENTO.name(), rs.getString("tipoRequerimento"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_REQUERENTE.name(), rs.getString("requerente"));
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemNotificacaoAniversarianteDia(SqlRowSet rs, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), rs.getString("Pessoa.nome"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), rs.getString("Pessoa.nome"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), rs.getString("Pessoa.nome"));
		return mensagemTexto;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemReposicaoAulaDisponivel(Integer horarioTurma, UsuarioVO usuarioVO) throws Exception {
		if (horarioTurma != null && horarioTurma > 0) {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ALUNO_REPOSICAO_AULA_DISPONIVEL, false, null);
			if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
//				UsuarioVO usuarioVO = new UsuarioVO();
				PessoaVO pessoaVO = null;
				SqlRowSet rs = consultarAlunoNotificarReposicaoAulaDisponivel(horarioTurma);
				while (rs.next()) {
					PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = mensagemTemplate;
					pessoaVO = new PessoaVO();
					pessoaVO.setCodigo(rs.getInt("aluno_codigo"));
					pessoaVO.setNome(rs.getString("aluno_nome"));
					pessoaVO.setEmail(rs.getString("aluno_email"));
					String matricula = rs.getString("matricula");
					if (Uteis.isAtributoPreenchido(matricula)) {
						MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
						mensagemTemplateUtilizar = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ALUNO_REPOSICAO_AULA_DISPONIVEL, false, matriculaVO.getUnidadeEnsino().getCodigo(), null);
						mensagemTemplateUtilizar = mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica() ? mensagemTemplateUtilizar : mensagemTemplate;
					}
					ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
					comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacao.setData(new Date());
					comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					comunicacao.setCodigo(0);
					comunicacao.getComunicadoInternoDestinatarioVOs().clear();
					comunicacao.setAssunto(mensagemTemplateUtilizar.getAssunto());
					comunicacao.setCaminhoImagemPadraoCima(mensagemTemplateUtilizar.getCaminhoImagemPadraoCima());
					comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplateUtilizar.getCaminhoImagemPadraoBaixo());
					comunicacao.setCopiaFollowUp(mensagemTemplateUtilizar.getCopiaFollowUp());
					comunicacao.setMensagem(obterMensagemFormatadaMensagemReposicaoAulaDisponivel(rs, mensagemTemplateUtilizar.getMensagem()));
					if (!mensagemTemplateUtilizar.getDesabilitarEnvioMensagemSMSAutomatica()) {
						comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemReposicaoAulaDisponivel(rs, mensagemTemplateUtilizar.getMensagemSMS()));
						comunicacao.setEnviarSMS(Boolean.TRUE);
					}
					comunicacao.setTipoDestinatario("AL");
					comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplateUtilizar.getEnviarCopiaPais()));
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);

				}

			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private SqlRowSet consultarAlunoNotificarReposicaoAulaDisponivel(Integer horarioTurma) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct matricula.matricula, aluno.codigo as aluno_codigo, aluno.nome aluno_nome, aluno.email as aluno_email, ");
		sql.append(" horarioturma.disciplinaProgramada as disciplina, horarioturma.turmaProgramada as turma, horarioturma.unidadeEnsino,  ");
		sql.append(" horarioturma.dataAula, disciplina.nome as disciplinaRepor ");
		sql.append(" from matriculaperiodo   ");
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sql.append(" inner join curso on matricula.curso = curso.codigo ");
		sql.append(" inner join pessoa as aluno on matricula.aluno = aluno.codigo ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sql.append(" inner join disciplina on matriculaperiodoturmadisciplina.disciplina = disciplina.codigo ");
		sql.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" inner join (select horario.horarioturma, unidadeEnsino.nome as unidadeEnsino, turma.codigo as turma, turma.identificadorTurma as turmaProgramada, turma.codigo as turma, ");
		sql.append(" disciplina.codigo as disciplina, disciplina.nome as disciplinaProgramada, min(data) as dataAula from horarioturmadetalhado( ");
		sql.append(" (select turma from horarioturma where codigo = " + horarioTurma + "), null, null, null) as horario ");
		sql.append(" inner join turma on turma.codigo = horario.turma ");
		sql.append(" inner join disciplina on disciplina.codigo = horario.disciplina ");
		sql.append(" inner join unidadeEnsino on turma.unidadeEnsino = unidadeEnsino.codigo ");
		sql.append(" group by horario.horarioturma, unidadeEnsino.nome, turma.identificadorTurma, disciplina.nome, turma.codigo, disciplina.codigo ");
		sql.append(" having min(data) > current_date) as horarioturma on  ");
		sql.append(" (disciplina.codigo = horarioturma.disciplina or disciplina.codigo  in (select equivalente from disciplinaequivalente  where disciplina = horarioturma.disciplina ) ");
		sql.append(" or disciplina.codigo  in (select disciplina from disciplinaequivalente  where equivalente = horarioturma.disciplina )) ");
		sql.append(" where historico.situacao in ('RE', 'RF') and situacaomatriculaperiodo  = 'AT' and curso.niveleducacional in ('PO', 'EX')  ");
		sql.append(" and (select count(distinct codigo) from historico hist where matricula.matricula = hist.matricula ");
		sql.append(" and (hist.disciplina = horarioturma.disciplina or hist.disciplina in (select equivalente from disciplinaequivalente  where disciplina = horarioturma.disciplina ) ");
		sql.append(" or hist.disciplina  in (select disciplina from disciplinaequivalente  where equivalente = horarioturma.disciplina )) ");
		sql.append(" and hist.matriculaperiodoturmadisciplina <> matriculaperiodoturmadisciplina.codigo ");
		sql.append(" and hist.situacao not in ('RE', 'RF') limit 1) = 0 ");
		sql.append(" order by aluno.nome");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	public String obterMensagemFormatadaMensagemReposicaoAulaDisponivel(SqlRowSet rs, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), rs.getString("matricula"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), rs.getString("aluno_nome"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_AULA.name(), Uteis.getData(rs.getDate("dataAula")));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), rs.getString("turma"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DISCIPLINA_DISPONIVEL_REPOSICAO.name(), rs.getString("disciplina"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DISCIPLINA_REPOR.name(), rs.getString("disciplinaRepor"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), rs.getString("unidadeEnsino"));
		return mensagemTexto;
	}

	@Override
	public void executarEnvioMensagemAguardandoAprovacaoOrientador(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_AGUARDANDO_APROVACAO_ORIENTADOR, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario("PR");
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getOrientador(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemAprovacaoOrientador(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_APROVACAO_ORIENTADOR, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemAguardandoAvaliacaoBanca(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_AGUARDANDO_AVALIACAO_BANCA, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && trabalhoConclusaoCursoVO.getCoordenador().getCodigo().intValue() != 0) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setEnviarEmail(true);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario("FU");
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getCoordenador(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemTrabalhoConclusaoCursoAprovado(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_APROVADO, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemTrabalhoConclusaoCursoEncaminhadoOrientadorFormatacao(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_ENCAMINHADO_ORIENTADOR_FORMATACAO, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemTrabalhoConclusaoCursoEncaminhadoOrientadorConteudo(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_ENCAMINHADO_ORIENTADOR_CONTEUDO, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setEnviarEmail(true);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemTrabalhoConclusaoCursoEncaminhadoOrientador(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_ENCAMINHADO_ORIENTADO, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario("PR");
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getOrientador(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemTrabalhoConclusaoCursoReprovado(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_REPROVADO, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemAlteracaoSituacaoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemPrimeiroAvisoTCCEmAtraso(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, String tipoDestinatario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		UsuarioVO usuarioVO = new UsuarioVO();
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_PRIMEIRO_AVISO_ATRASO, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemAtrasoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemAtrasoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario(tipoDestinatario);
			if (tipoDestinatario.equals("AL")) {
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			} else if (tipoDestinatario.equals("PR")) {
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getOrientador(), mensagemTemplate.getEnviarCopiaPais()));
			} else if (tipoDestinatario.equals("FU")) {
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getCoordenador(), mensagemTemplate.getEnviarCopiaPais()));
			}
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuarioVO, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemSegundoAvisoTCCEmAtraso(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO, String tipoDestinatario) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		UsuarioVO usuarioVO = new UsuarioVO();
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_SEGUNDO_AVISO_ATRASO, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemAtrasoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemAtrasoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario(tipoDestinatario);
			if (tipoDestinatario.equals("AL")) {
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			} else if (tipoDestinatario.equals("PR")) {
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getOrientador(), mensagemTemplate.getEnviarCopiaPais()));
			} else if (tipoDestinatario.equals("FU")) {
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getCoordenador(), mensagemTemplate.getEnviarCopiaPais()));
			}
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuarioVO, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemReprovacaoAutomaticaPorAtrasoTCC(TrabalhoConclusaoCursoVO trabalhoConclusaoCursoVO) throws Exception {
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(trabalhoConclusaoCursoVO.getMatriculaPeriodoTurmaDisciplina().getMatricula(), 0, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null);
		UsuarioVO usuarioVO = new UsuarioVO();
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_REPROVADO_ATRASO, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaMensagemReprovacaoAutomaticaPorAtrasoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaMensagemReprovacaoAutomaticaPorAtrasoTCC(trabalhoConclusaoCursoVO, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setEnviarEmail(true);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(trabalhoConclusaoCursoVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuarioVO, config,null);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemTramiteRequerimento(RequerimentoVO obj, RequerimentoHistoricoVO historico, UsuarioVO usuarioLogadoVO) throws Exception {
		if(obj.getTipoRequerimento().getEnviarNotificacaoRequerente()) {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate;
			if (obj.isUtilizarMensagemDeferimentoTipoRequerimento()) {
				if (Uteis.isAtributoPreenchido(obj.getTipoRequerimento().getPersonalizacaoMensagemAutomaticaDeferimento()) && (obj.getTipoRequerimento().getPersonalizacaoMensagemAutomaticaDeferimento().isNivelMontarDadosBasicos() || obj.getTipoRequerimento().getPersonalizacaoMensagemAutomaticaDeferimento().isNivelMontarDadosTodos())) {
					mensagemTemplate = obj.getTipoRequerimento().getPersonalizacaoMensagemAutomaticaDeferimento();
				} else {
					mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplatePorTipoRequerimento(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_DEFERIDO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, obj.getUnidadeEnsino().getCodigo(), obj.getTipoRequerimento().getCodigo(), usuarioLogadoVO);
				}
			} else if (obj.isUtilizarMensagemIndeferimentoTipoRequerimento()) {
				if (Uteis.isAtributoPreenchido(obj.getTipoRequerimento().getPersonalizacaoMensagemAutomaticaIndeferimento()) && (obj.getTipoRequerimento().getPersonalizacaoMensagemAutomaticaIndeferimento().isNivelMontarDadosBasicos() || obj.getTipoRequerimento().getPersonalizacaoMensagemAutomaticaIndeferimento().isNivelMontarDadosTodos())) {
					mensagemTemplate = obj.getTipoRequerimento().getPersonalizacaoMensagemAutomaticaIndeferimento();
				} else {
					mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplatePorTipoRequerimento(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_INDEFERIDO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, obj.getUnidadeEnsino().getCodigo(), obj.getTipoRequerimento().getCodigo(), usuarioLogadoVO);
				}
			}else if (obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor()) && obj.getTipoRequerimento().getTipo().equals("TC")) {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_APROVADO, false, obj.getUnidadeEnsino().getCodigo(), null);
			} else if (obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_DEFERIDO, false, obj.getUnidadeEnsino().getCodigo(), null);
			} else if (obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor()) && obj.getTipoRequerimento().getTipo().equals("TC")) {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TCC_REPROVADO, false, obj.getUnidadeEnsino().getCodigo(), null);
			}else if (obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor())) {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_INDEFERIDO, false, obj.getUnidadeEnsino().getCodigo(), null);
			}else {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_TRAMITE_REQUERENTE, false, obj.getUnidadeEnsino().getCodigo(), null);
			}
			List<Integer> requerimentos = new ArrayList<Integer>(0);
			if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
				if (usuarioLogadoVO.getPessoa() == null || usuarioLogadoVO.getPessoa().getCodigo().equals(0)) {
					return;
				}
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				UsuarioVO usuarioVO = new UsuarioVO();
				PessoaVO pessoaVO = null;
				pessoaVO = new PessoaVO();			
				requerimentos.add(obj.getCodigo());
				pessoaVO.setCodigo(obj.getPessoa().getCodigo());
				pessoaVO.setNome(obj.getPessoa().getNome());
				pessoaVO.setEmail(obj.getPessoa().getEmail());
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				if (!config.getResponsavelPadraoComunicadoInterno().getCodigo().equals(0)) {
					comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				} else {
					comunicacao.setResponsavel(usuarioLogadoVO.getPessoa());
				}
				comunicacao.setData(new Date());
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setAssunto(mensagemTemplate.getAssunto());
				comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				comunicacao.setMensagem(obterMensagemFormatadaMensagemTramitacaoRequerimento(obj, historico, mensagemTemplate.getMensagem()));
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemTramitacaoRequerimento(obj, historico, mensagemTemplate.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				
				
				if(obj.getTipoPessoa().equals(TipoPessoa.ALUNO)) {
					comunicacao.setTipoDestinatario("AL");
				}
				else if(obj.getTipoPessoa().equals(TipoPessoa.REQUERENTE) && (obj.getPessoa().getFuncionario() || obj.getPessoa().getProfessor() || obj.getPessoa().getCoordenador())) {
					comunicacao.setTipoDestinatario("FU");
				}
				
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
			}
			getFacadeFactory().getRequerimentoFacade().executarRegistroNotificacaoRequerimentoEmAtraso(requerimentos);
		}
	}

	public String obterMensagemFormatadaMensagemTramitacaoRequerimento(RequerimentoVO obj, RequerimentoHistoricoVO historico, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.RESPONSAVEL_DEPARTAMENTO.name(), obj.getFuncionarioVO().getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_REQUERIMENTO.name(), Uteis.getData(obj.getData()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TIPO_REQUERIMENTO.name(), obj.getTipoRequerimento().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_REQUERENTE.name(), obj.getNomeRequerente());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SITUACAO_REQUERIMENTO.name(), obj.getSituacao_Apresentar());
		if (historico.getCodigo() != 0) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_TRAMITE.name(), historico.getDataEntradaDepartamento_Apresentar());
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DEPARTAMENTO.name(), obj.getDepartamentoResponsavel().getNome());
		if(obj.getIsDeferido()) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MOTIVO_DEFERIMENTO.name(), obj.getMotivoDeferimento());
		} else if(obj.getIsIndeferido()) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MOTIVO_INDEFERIMENTO.name(), obj.getMotivoIndeferimento());
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TITULO_MONOGRAFIA.name(), obj.getTituloMonografia());
		if(Uteis.isAtributoPreenchido(obj.getNotaMonografia())) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOTA.name(), obj.getNotaMonografia().toString());
		}
		if (Uteis.isAtributoPreenchido(obj.getTurma().getIdentificadorTurma())) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), obj.getTurma().getIdentificadorTurma());
		
		}
		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatricula().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), obj.getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.REGISTRO_ACADEMICO.name(), obj.getPessoa().getRegistroAcademico());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_INICIO_AFASTAMENTO.name(), Uteis.getData(obj.getDataAfastamentoInicio()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_TERMINO_AFASTAMENTO.name(), Uteis.getData(obj.getDataAfastamentoFim()));
		if (obj.getTipoRequerimento().getIsTipoAproveitamentoDisciplina()) {
			String disciplinasDeferidas = Uteis.isAtributoPreenchido(obj.getListaRequerimentoDisciplinasAproveitadasVOs()) ? obj.getListaRequerimentoDisciplinasAproveitadasVOs().stream().filter(RequerimentoDisciplinasAproveitadasVO::isSituacaoRequerimentoDisciplinaDeferido).map(requerimentoDisciplina -> "<li> <strong>" + requerimentoDisciplina.getDisciplina().getNome() + "</strong> </li>").collect(Collectors.joining(Constantes.EMPTY)) : Constantes.EMPTY;
			String disciplinasIndeferidas = Uteis.isAtributoPreenchido(obj.getListaRequerimentoDisciplinasAproveitadasVOs()) ? obj.getListaRequerimentoDisciplinasAproveitadasVOs().stream().filter(RequerimentoDisciplinasAproveitadasVO::isSituacaoRequerimentoDisciplinaIndeferido).map(requerimentoDisciplina -> "<li> <strong>" + requerimentoDisciplina.getDisciplina().getNome() + "</strong> </li>").collect(Collectors.joining(Constantes.EMPTY)) : Constantes.EMPTY;
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_DISCIPLINAS_DEFERIDAS.name(), Uteis.isAtributoPreenchido(disciplinasDeferidas) ? "<ul> " + disciplinasDeferidas + " </ul>" : Constantes.EMPTY);
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_DISCIPLINAS_INDEFERIDAS.name(), Uteis.isAtributoPreenchido(disciplinasIndeferidas) ? "<ul> " + disciplinasIndeferidas + " </ul>" : Constantes.EMPTY);
		}
		if (obj.getTipoRequerimento().getIsTipoSegundaChamada()) {
			String disciplinasDeferidas = Uteis.isAtributoPreenchido(obj.getRequerimentoDisciplinaVOs()) ? obj.getRequerimentoDisciplinaVOs().stream().filter(RequerimentoDisciplinaVO::isSituacaoRequerimentoDisciplinaDeferido).map(requerimentoDisciplina -> "<li> <strong>" + requerimentoDisciplina.getDisciplina().getNome() + "</strong> </li>").collect(Collectors.joining(Constantes.EMPTY)) : Constantes.EMPTY;
			String disciplinasIndeferidas = Uteis.isAtributoPreenchido(obj.getRequerimentoDisciplinaVOs()) ? obj.getRequerimentoDisciplinaVOs().stream().filter(RequerimentoDisciplinaVO::isSituacaoRequerimentoDisciplinaIndeferido).map(requerimentoDisciplina -> "<li> <strong>" + requerimentoDisciplina.getDisciplina().getNome() + "</strong> </li>").collect(Collectors.joining(Constantes.EMPTY)) : Constantes.EMPTY;
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_DISCIPLINAS_DEFERIDAS.name(), Uteis.isAtributoPreenchido(disciplinasDeferidas) ? "<ul> " + disciplinasDeferidas + " </ul>" : Constantes.EMPTY);
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_DISCIPLINAS_INDEFERIDAS.name(), Uteis.isAtributoPreenchido(disciplinasIndeferidas) ? "<ul> " + disciplinasIndeferidas + " </ul>" : Constantes.EMPTY);
		}
		return mensagemTexto;
	}
	

	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemAlunosBaixaFrequencia() throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_COORDENADOR_ALUNO_BAIXA_FREQUENCIA, false, null);
		
		List<AlunoBaixaFrequenciaRelVO> listaAlunoBaixaFrequencia = getFacadeFactory().getAlunoBaixaFrequenciaRelInterfaceFacade().criarObjeto(new ArrayList<UnidadeEnsinoVO>(), new ArrayList<CursoVO>(), new TurmaVO(), 0, new FiltroAlunoBaixaFrequenciaVO(), 0, false);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && !listaAlunoBaixaFrequencia.isEmpty()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			for(AlunoBaixaFrequenciaRelVO alnBaixaFrequencia : listaAlunoBaixaFrequencia) {
				if(!listaPessoas.contains(alnBaixaFrequencia.getCoordenador())) {
					listaPessoas.add(alnBaixaFrequencia.getCoordenador());
				}
			}
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosBaixaFrequencia(listaAlunoBaixaFrequencia, mensagemTemplate.getMensagem()));
		
			comunicacao.setTipoDestinatario("CO");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemTramiteRequerimentoAtendente(RequerimentoVO obj, RequerimentoHistoricoVO historico, UsuarioVO usuarioLogadoVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate;
	
		mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_REQUERIMENTO_TRAMITE_ATENDENTE, false, obj.getUnidadeEnsino().getCodigo(), null);

		List<Integer> requerimentos = new ArrayList<Integer>(0);
		if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			PessoaVO pessoaVO = null;
			pessoaVO = new PessoaVO();			
			requerimentos.add(obj.getCodigo());
			pessoaVO.setCodigo(obj.getFuncionarioVO().getPessoa().getCodigo());
			pessoaVO.setNome(obj.getFuncionarioVO().getPessoa().getNome());
			pessoaVO.setEmail(obj.getFuncionarioVO().getPessoa().getEmail());
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			if (!config.getResponsavelPadraoComunicadoInterno().getCodigo().equals(0)) {
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			} else {
				comunicacao.setResponsavel(usuarioLogadoVO.getPessoa());
			}
			comunicacao.setData(new Date());
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemTramitacaoRequerimentoAtendente(obj, historico, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemTramitacaoRequerimentoAtendente(obj, historico, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}	
			
			comunicacao.setTipoDestinatario("FU");		

			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
		getFacadeFactory().getRequerimentoFacade().executarRegistroNotificacaoRequerimentoEmAtraso(requerimentos);
	}

	public String obterMensagemFormatadaMensagemTramitacaoRequerimentoAtendente(RequerimentoVO obj, RequerimentoHistoricoVO historico, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.RESPONSAVEL_DEPARTAMENTO.name(), obj.getFuncionarioVO().getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_REQUERIMENTO.name(), Uteis.getData(obj.getData()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TIPO_REQUERIMENTO.name(), obj.getTipoRequerimento().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_REQUERENTE.name(), obj.getNomeRequerente());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SITUACAO_REQUERIMENTO.name(), obj.getSituacao_Apresentar());
		if (historico.getCodigo() != 0) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_TRAMITE.name(), historico.getDataEntradaDepartamento_Apresentar());
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DEPARTAMENTO.name(), obj.getDepartamentoResponsavel().getNome());
		if(obj.getIsDeferido()) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MOTIVO_DEFERIMENTO.name(), obj.getMotivoDeferimento());
		}
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemAlunosBaixaFrequencia(List<AlunoBaixaFrequenciaRelVO> listaAlunosBaixaFrequencia, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		
		StringBuilder listaAlunos = new StringBuilder();
		listaAlunos.append("<table style=width: 100%> ");
			listaAlunos.append("<tr> ");
				listaAlunos.append(" <th>Nome Aluno</th>");
				listaAlunos.append(" <th>Curso</th>");
				listaAlunos.append(" <th>Turma</th>");
				listaAlunos.append(" <th>Disciplina</th>");
				listaAlunos.append(" <th>Unidade Ensino</th>");
				listaAlunos.append(" <th>Percentual Faltas</th>");
				listaAlunos.append(" <th>Qtd Faltas</th>");
				listaAlunos.append(" <th>Coordenador</th>");
			listaAlunos.append("</tr> ");
		for (AlunoBaixaFrequenciaRelVO alunoBaixaFrequenciaRelVO : listaAlunosBaixaFrequencia) {
			listaAlunos.append("<tr>");
				listaAlunos.append(" <td>").append(alunoBaixaFrequenciaRelVO.getNomeAluno()).append("</td> ");
				listaAlunos.append(" <td>").append(alunoBaixaFrequenciaRelVO.getCurso()).append("</td> ");
				listaAlunos.append(" <td>").append(alunoBaixaFrequenciaRelVO.getTurma()).append("</td> ");
				listaAlunos.append(" <td>").append(alunoBaixaFrequenciaRelVO.getDisciplina()).append("</td> ");
				listaAlunos.append(" <td>").append(alunoBaixaFrequenciaRelVO.getNomeUnidadeEnsino()).append("</td> ");
				listaAlunos.append(" <td>").append(alunoBaixaFrequenciaRelVO.getPercentualFaltas()).append("</td> ");
				listaAlunos.append(" <td>").append(alunoBaixaFrequenciaRelVO.getTotalFaltas()).append("</td> ");
				listaAlunos.append(" <td>").append(alunoBaixaFrequenciaRelVO.getCoordenador().getNome()).append("</td> ");
			listaAlunos.append("</tr>");
		}
		listaAlunos.append("</table>");
		mensagemTexto = mensagemTexto.replace(TagsMensagemAutomaticaEnum.LISTA_ALUNOS_BAIXA_FREQUENCIA.name(), listaAlunos.toString());
		return mensagemTexto;

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemNotificacaoProcessoSeletivo(Integer processoSeletivo, Integer unidadeEnsino, Integer itemProcSeletivoDataProva, Integer sala, TipoRelatorioEstatisticoProcessoSeletivoEnum tipoNotificacao, UsuarioVO usuarioVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = null;
		try {
			if (tipoNotificacao.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_NAO_MATRICULADOS)) {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROCESSO_SELETIVO_APROVADO_NAO_MATRICULADO, false, unidadeEnsino, null);
			} else if (tipoNotificacao.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_AUSENTES)) {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROCESSO_SELETIVO_AUSENTE_VESTIBULAR, false, unidadeEnsino, null);
			} else if (tipoNotificacao.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LEMBRETE_DATA_PROVA)) {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROCESSO_SELETIVO_LEMBRETE_DATA_PROVA, false, unidadeEnsino, null);
			} else if (tipoNotificacao.equals(TipoRelatorioEstatisticoProcessoSeletivoEnum.LISTAGEM_PRESENTES)) {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROCESSO_SELETIVO_PRESENTE_VESTIBULAR, false, unidadeEnsino, null);
			}
			if (mensagemTemplate == null) {
				throw new Exception("Mensagem Personalizada para Notificação de Processo Seletivo não foi gerada!");
			}
			if (mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				throw new Exception(mensagemTemplate.getAssunto() + " não está habilitada!");
			} else {
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				@SuppressWarnings("unchecked")
				List<InscricaoVO> inscricaoVOs = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().consultarDadosGeracaoEstatistica(tipoNotificacao, processoSeletivo, itemProcSeletivoDataProva, sala, unidadeEnsino, "", "", new FiltroRelatorioProcessoSeletivoVO(), "", 0, 0, true, config.getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo(), usuarioVO, 0);
				if (inscricaoVOs.isEmpty()) {
					throw new Exception("Nenhum dado encontrado.");
				} else {
					for (InscricaoVO inscricaoVO : inscricaoVOs) {
						if (!inscricaoVO.getCandidato().getEmail().equals("")) {
							PessoaVO pessoaVO = null;
							pessoaVO = new PessoaVO();
							pessoaVO.setCodigo(inscricaoVO.getCandidato().getCodigo());
							pessoaVO.setNome(inscricaoVO.getCandidato().getNome());
							pessoaVO.setEmail(inscricaoVO.getCandidato().getEmail());
							ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
							comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
							comunicacao.setAssunto(mensagemTemplate.getAssunto());
							comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
							comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
							comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
							comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
							comunicacao.setData(new Date());
							comunicacao.setCodigo(0);
							comunicacao.getComunicadoInternoDestinatarioVOs().clear();
							comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoProcessoSeletivo(inscricaoVO, mensagemTemplate.getMensagem()));
							if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
								comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoProcessoSeletivo(inscricaoVO, mensagemTemplate.getMensagemSMS()));
								comunicacao.setEnviarSMS(Boolean.TRUE);
							}
							comunicacao.setTipoDestinatario("AL");
							comunicacao.setAluno(pessoaVO);
							comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
							comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
							comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
							getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
							comunicacao = null;
							inscricaoVO = null;
						}
					}
					inscricaoVOs.clear();
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemNotificacaoProcessoSeletivoLembreteDataProva() throws Exception {
		UsuarioVO usuarioVO = new UsuarioVO();
		ConfiguracaoGeralSistemaVO configGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesQtdeDiasNotificacaoDataProcessoSeletivo();
		if (configGeralSistema.getQtdeDiasNotificacaoDataProva() > 0) {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = null;
			try {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROCESSO_SELETIVO_LEMBRETE_DATA_PROVA, false, null);
				if (mensagemTemplate == null) {
					throw new Exception("Mensagem Personalizada para Notificação de Processo Seletivo não foi gerada!");
				}
				if (mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
					throw new Exception(mensagemTemplate.getAssunto() + " não está habilitada!");
				} else {
					ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
					@SuppressWarnings("unchecked")
					List<InscricaoVO> inscricaoVOs = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().consultarDadosGeracaoEstatistica(TipoRelatorioEstatisticoProcessoSeletivoEnum.LEMBRETE_DATA_PROVA, 0, 0, 0, 0, "", "", new FiltroRelatorioProcessoSeletivoVO(), "", configGeralSistema.getQtdeDiasNotificacaoDataProva(), 0, true, config.getQuantidadeCasaDecimalConsiderarNotaProcessoSeletivo(), usuarioVO, 0);
					if (inscricaoVOs.isEmpty()) {
						throw new Exception("Nenhum dado encontrado.");
					} else {
						for (InscricaoVO inscricaoVO : inscricaoVOs) {
							if (!inscricaoVO.getCandidato().getEmail().equals("")) {
								PessoaVO pessoaVO = null;
								pessoaVO = new PessoaVO();
								pessoaVO.setCodigo(inscricaoVO.getCandidato().getCodigo());
								pessoaVO.setNome(inscricaoVO.getCandidato().getNome());
								pessoaVO.setEmail(inscricaoVO.getCandidato().getEmail());
								ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
								comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
								comunicacao.setAssunto(mensagemTemplate.getAssunto());
								comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
								comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
								comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
								comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
								comunicacao.setData(new Date());
								comunicacao.setCodigo(0);
								comunicacao.getComunicadoInternoDestinatarioVOs().clear();
								comunicacao.setMensagem(obterMensagemFormatadaMensagemNotificacaoProcessoSeletivo(inscricaoVO, mensagemTemplate.getMensagem()));
								if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
									comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemNotificacaoProcessoSeletivo(inscricaoVO, mensagemTemplate.getMensagemSMS()));
									comunicacao.setEnviarSMS(Boolean.TRUE);
								}
								comunicacao.setTipoDestinatario("AL");
								comunicacao.setAluno(pessoaVO);
								comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
								comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
								comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
								getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
								comunicacao = null;
								inscricaoVO = null;
							}
						}
						inscricaoVOs.clear();
					}
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoProcessoSeletivo(InscricaoVO inscricaoVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CANDIDATO.name(), inscricaoVO.getCandidato().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_INSCRICAO.name(), inscricaoVO.getCodigo().toString());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.PROCESSO_SELETIVO.name(), inscricaoVO.getProcSeletivo().getDescricao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_PROVA.name(), Uteis.getDataBD(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva(), "dd/MM/yyyy"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.HORA_PROVA.name(), Uteis.getHoraMinutoComMascara(inscricaoVO.getItemProcessoSeletivoDataProva().getDataProva()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), inscricaoVO.getCursoOpcao1().getNomeUnidadeEnsino());
		if (inscricaoVO.getOpcaoLinguaEstrangeira().getCodigo() > 0) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), inscricaoVO.getOpcaoLinguaEstrangeira().getNome());
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OPCAO_LINGUA_ESTRANGEIRA.name(), "Não informado.");
		}
		StringBuilder cursosInscritos = new StringBuilder("<ul>");
		if (inscricaoVO.getCursoOpcao1().getCurso().getCodigo() > 0) {
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>1ª Opção: ").append(inscricaoVO.getCursoOpcao1().getCurso().getNome()).append(" - ").append(inscricaoVO.getCursoOpcao1().getTurno().getNome()).append("</strong>");
			cursosInscritos.append("</li>");
		}
		if (inscricaoVO.getCursoOpcao2().getCurso().getCodigo() > 0) {
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>2ª Opção: ").append(inscricaoVO.getCursoOpcao1().getCurso().getNome()).append(" - ").append(inscricaoVO.getCursoOpcao2().getTurno().getNome()).append("</strong>");
			cursosInscritos.append("</li>");
		}
		if (inscricaoVO.getCursoOpcao3().getCurso().getCodigo() > 0) {
			cursosInscritos.append("<li>");
			cursosInscritos.append(" <strong>3ª Opção: ").append(inscricaoVO.getCursoOpcao1().getCurso().getNome()).append(" - ").append(inscricaoVO.getCursoOpcao3().getTurno().getNome()).append("</strong>");
			cursosInscritos.append("</li>");
		}
		cursosInscritos.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CURSOS_INSCRITOS.name(), cursosInscritos.toString());
		return mensagemTexto;
	}

	@Override
	public void executarEnvioMensagemConfirmacaoMatricula(MatriculaVO matriculaVO,PersonalizacaoMensagemAutomaticaVO mensagemTemplate , UsuarioVO usuario) throws Exception {
		
		try {
		
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				String mensagemEditada = obterMensagemFormatadaMensagemRenovacaoMatricula(matriculaVO, mensagemTemplate.getMensagem(),usuario);
				String mensagemSMSEditada = obterMensagemFormatadaMensagemRenovacaoMatricula(matriculaVO, mensagemTemplate.getMensagemSMS(),usuario);
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				if (usuario.getPessoa().getCodigo() == 0) {
					comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				} else {
					comunicacaoEnviar.setResponsavel(usuario.getPessoa());
				}
				if(matriculaVO.getAluno().getEmail().isEmpty()) {
					matriculaVO.getAluno().setEmail(getFacadeFactory().getPessoaFacade().consultarEmailCodigo(matriculaVO.getAluno().getCodigo()));
				}
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("AL");
				comunicacaoEnviar.setPersonalizacaoMensagemAutomaticaVO(mensagemTemplate);
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				if (comunicacaoEnviar.getResponsavel().getCodigo() != 0) {
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
				}
			}
		} catch (Exception e) {

			throw e;
		}
	}

	@Override
	public void executarEnvioMensagemAprovacaoProcessoSeletivo(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate;
		mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROCESSO_SELETIVO_APROVADO_PROCESSO_SELETIVO, false,
				resultadoProcessoSeletivoVO.getInscricao().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemNotificacaoProcessoSeletivo(resultadoProcessoSeletivoVO.getInscricao(), mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoProcessoSeletivo(resultadoProcessoSeletivoVO.getInscricao(), mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(resultadoProcessoSeletivoVO.getInscricao().getCandidato(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemNotificacaoAbandonoCurso() {
		try {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = null;
			List<MatriculaPeriodoVO> matriculaPeriodoVOs = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoNotificacaoAbandonoCurso();
			Map<Integer, PersonalizacaoMensagemAutomaticaVO> consultarPorUnidadeEnsino = new HashMap<>();
			if (Uteis.isAtributoPreenchido(matriculaPeriodoVOs)) {
				List<Integer> unidadeEnsinos = matriculaPeriodoVOs.stream().map(mpvo -> mpvo.getMatriculaVO().getUnidadeEnsino().getCodigo()).distinct().collect(Collectors.toList());
				consultarPorUnidadeEnsino = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorUnidadeEnsino(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ABANDONO_CURSO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, unidadeEnsinos, null);
			}
			for (MatriculaPeriodoVO matriculaPeriodoVO : matriculaPeriodoVOs) {
				try {
					getFacadeFactory().getMatriculaPeriodoFacade().realizarRegistroNotificacaoAbandonoCurso(matriculaPeriodoVO.getCodigo());
					ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
					ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
					if (Uteis.isAtributoPreenchido(consultarPorUnidadeEnsino) && consultarPorUnidadeEnsino.containsKey(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo())) {
						mensagemTemplate = consultarPorUnidadeEnsino.get(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo());
					}
					if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
						comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
						comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
						comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
						comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
						String mensagemEditada = obterMensagemFormatadaAbandonoCurso(matriculaPeriodoVO, mensagemTemplate.getMensagem());
						String mensagemSMSEditada = obterMensagemFormatadaAbandonoCurso(matriculaPeriodoVO, mensagemTemplate.getMensagemSMS());
						comunicacaoEnviar.setMensagem(mensagemEditada);
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
							comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
						}
						comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
						comunicacaoEnviar.setData(new Date());
						comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaPeriodoVO.getMatriculaVO().getAluno(), mensagemTemplate.getEnviarCopiaPais()));
						comunicacaoEnviar.setTipoDestinatario("AL");
						comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
						comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
						getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, null, config,null);
						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void executarEnvioMensagemRegistroAbandonoCurso(MatriculaPeriodoVO matriculaPeriodoVO) {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate;
		try {
			mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_REGISTRO_ABANDONO_CURSO, false, matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), null);
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {

				if (matriculaPeriodoVO != null && matriculaPeriodoVO.getCodigo() != null && matriculaPeriodoVO.getCodigo() > 0) {
					ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
					ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
					comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
					comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
					comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
					comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
					String mensagemEditada = obterMensagemFormatadaAbandonoCurso(matriculaPeriodoVO, mensagemTemplate.getMensagem());
					String mensagemSMSEditada = obterMensagemFormatadaAbandonoCurso(matriculaPeriodoVO, mensagemTemplate.getMensagemSMS());
					comunicacaoEnviar.setMensagem(mensagemEditada);
					if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
						comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
						comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
					}
					comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacaoEnviar.setData(new Date());
					comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaPeriodoVO.getMatriculaVO().getAluno(), mensagemTemplate.getEnviarCopiaPais()));
					comunicacaoEnviar.setTipoDestinatario("AL");
					comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, null, config,null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String obterMensagemFormatadaAbandonoCurso(MatriculaPeriodoVO matriculaPeriodoVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaPeriodoVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matriculaPeriodoVO.getMatriculaVO().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), matriculaPeriodoVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_TURNO.name(), matriculaPeriodoVO.getMatriculaVO().getTurno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getNome());
		return mensagemTexto;
	}

	public void executaEnvioMensagemNotificacaoPrazoDevolucao(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String biblioteca, UsuarioVO usuario, ConfiguracaoGeralSistemaVO config, PessoaVO responsavel, Integer unidadeEnsino) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_EMPRESTIMO_PRAZO_DE_VOLUCAO, false, unidadeEnsino, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && livros != null && !livros.isEmpty()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaNotificacaoPrazoDevolucao(mensagemTemplate.getMensagem(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataPrevisaoDevolucao());
			String mensagemSMSEditada = obterMensagemFormatadaNotificacaoPrazoDevolucao(mensagemTemplate.getMensagemSMS(), obterNomeLivrosFormatado(livros), pessoa.getNome(), biblioteca, livros.get(0).getDataPrevisaoDevolucao());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(responsavel);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(valorTipoPessoa);
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	@Override
	public void executarEnvioMensagemNotificacaoVencimentoInscricaoCandidato() {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate;
		try {
			mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_VENCIMENTO_INSCRICAO_CANDIDATO, false, null);
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				List<InscricaoVO> inscricaoVOs = getFacadeFactory().getInscricaoFacade().consultaInscricaoPendenteParaNotificacao();
				for (InscricaoVO inscricaoVO : inscricaoVOs) {
					try {
						InscricaoVO insc = getFacadeFactory().getInscricaoFacade().consultarDadosEnvioNotificacaoEmail(inscricaoVO.getCodigo());
						getFacadeFactory().getInscricaoFacade().realizarRegistroNotificacaoVencimentoInscricao(insc.getCodigo(), null);

						ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
						ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
						comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
						comunicacao.setAssunto(mensagemTemplate.getAssunto());
						comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
						comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
						comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
						comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
						comunicacao.setData(new Date());
						comunicacao.setCodigo(0);
						comunicacao.getComunicadoInternoDestinatarioVOs().clear();
						if (insc.getItemProcessoSeletivoDataProva().getCodigo().intValue() > 0) {
							insc.setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(insc.getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, inscricaoVO.getResponsavel()));
						}
						comunicacao.setMensagem(obterMensagemFormatadaMensagemInscicaoProcessoSeletivo(insc, mensagemTemplate.getMensagem()));
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemInscicaoProcessoSeletivo(insc, mensagemTemplate.getMensagemSMS()));
							comunicacao.setEnviarSMS(Boolean.TRUE);
						}
						comunicacao.setTipoDestinatario("Al");
						comunicacao.setAluno(insc.getCandidato());
						comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(insc.getCandidato(), mensagemTemplate.getEnviarCopiaPais()));
						comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
						comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
						getFacadeFactory().getComunicacaoInternaFacade().enviarEmailComunicacaoInterna(comunicacao, new UsuarioVO(), config, mensagemTemplate, null, false);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String obterMensagemFormatadaNotificacaoPrazoDevolucao(final String mensagemTemplate, String livros, String nomePessoa, String biblioteca, Date DataPrevistaDevolucao) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), nomePessoa);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DOS_LIVROS.name(), livros);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_BIBLIOTECA.name(), biblioteca);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_DEVOLUCAO.name(), Uteis.getData(DataPrevistaDevolucao));
		return mensagemTexto;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemPostagemMaterial(ConfiguracaoGeralSistemaVO config, ArquivoVO arquivo) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_POSTAGEM_MATERIAL_PROFESSOR, false, arquivo.getUnidadeEnsinoVO().getCodigo(), null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			try {
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				String mensagemEditada = obterMensagemFormatadaMensagemUploadMaterial(arquivo, mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaMensagemUploadMaterial(arquivo, mensagemTemplate.getMensagemSMS());
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setProfessor(arquivo.getProfessor());
				comunicacaoEnviar.setResponsavel(arquivo.getProfessor());
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(arquivo.getProfessor(), mensagemTemplate.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("PR");
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
			} catch (Exception e) {
				e.getMessage();
			}
		}
	}

	public String obterMensagemFormatadaMensagemUploadMaterial(ArquivoVO arquivo, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), arquivo.getProfessor().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), arquivo.getDisciplina().getNome());
		if (arquivo.getTurma().getCodigo() != null && arquivo.getTurma().getCodigo() != 0) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), arquivo.getTurma().getIdentificadorTurma());
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), "Todas as Turmas");
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_POSTAGEM.name(), Uteis.getData(new Date(), "dd/MM/yyyy"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ARQUIVO.name(), arquivo.getDescricao());
		return mensagemTexto;

	}

	public void executarEnvioMensagemReservaCancelada(CatalogoVO catalogo, BibliotecaVO biblioteca, ReservaVO reserva, PessoaVO pessoa, String valorTipoPessoa, PessoaVO responsavel, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RESERVA_CANCELADA, false, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaReservaCancelada(mensagemTemplate.getMensagem(), catalogo.getTitulo(), pessoa.getNome(), biblioteca.getNome(), reserva.getDataReserva(), reserva.getDataTerminoReserva());
			String mensagemSMSEditada = obterMensagemFormatadaReservaCancelada(mensagemTemplate.getMensagemSMS(), catalogo.getTitulo(), pessoa.getNome(), biblioteca.getNome(), reserva.getDataReserva(), reserva.getDataTerminoReserva());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(responsavel);
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(valorTipoPessoa);
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaReservaCancelada(final String mensagemTemplate, String tituloCatalogo, String nomePessoa, String biblioteca, Date DataReserva, Date DataLimite) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), nomePessoa);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TITULO_CATALOGO.name(), tituloCatalogo);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_RESERVA.name(), Uteis.getData(DataReserva, "dd/MM/yyyy"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE.name(), Uteis.getData(DataLimite, "dd/MM/yyyy"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_BIBLIOTECA.name(), biblioteca);
		return mensagemTexto;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioConsultorContatosDia() throws Exception {
		ConfiguracaoGeralSistemaVO config = new ConfiguracaoGeralSistemaVO();
		config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		List<AgendaPessoaHorarioVO> listaEmail = getFacadeFactory().getAgendaPessoaHorarioFacade().consultarAgendaPessoaJobNotificacao();
		Map<Integer, PersonalizacaoMensagemAutomaticaVO> consultarPorUnidadeEnsino = new HashMap<>();
		if (Uteis.isAtributoPreenchido(listaEmail)) {
			List<Integer> unidadeEnsinos = listaEmail.stream().map(p -> p.getUnidadeEnsinoVO().getCodigo()).filter(Uteis::isAtributoPreenchido).distinct().collect(Collectors.toList());
			consultarPorUnidadeEnsino = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorUnidadeEnsino(TemplateMensagemAutomaticaEnum.MENSAGEM_CONSULTOR_CONTATOS_DIA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, unidadeEnsinos, null);
		}
		for (AgendaPessoaHorarioVO obj : listaEmail) {
			try {
				PersonalizacaoMensagemAutomaticaVO mensagemTemplate = null;
				if (Uteis.isAtributoPreenchido(consultarPorUnidadeEnsino) && consultarPorUnidadeEnsino.containsKey(obj.getUnidadeEnsinoVO().getCodigo())) {
					mensagemTemplate = consultarPorUnidadeEnsino.get(obj.getUnidadeEnsinoVO().getCodigo());
				}
				if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
					ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
					comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
					comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
					comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
					PessoaVO consultor = new PessoaVO();
					consultor.setCodigo(obj.getAgendaPessoa().getPessoa().getCodigo());
					consultor.setEmail(obj.getAgendaPessoa().getPessoa().getEmail());
					consultor.setNome(obj.getAgendaPessoa().getPessoa().getNome());
					
					String mensagemEditada = obterMensagemFormatadaMensagemConsultorContatosDia(obj, mensagemTemplate.getMensagem());
					String mensagemSMSEditada = obterMensagemFormatadaMensagemConsultorContatosDia(obj, mensagemTemplate.getMensagemSMS());
					comunicacaoEnviar.setMensagem(mensagemEditada);
					if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
						comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
						comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
					}
					comunicacaoEnviar.setPessoa(consultor);
					comunicacaoEnviar.setResponsavel(consultor);
					comunicacaoEnviar.setData(new Date());
					comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(consultor, mensagemTemplate.getEnviarCopiaPais()));
					comunicacaoEnviar.setTipoDestinatario("FU");
					comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
				}
			} catch (Exception e) {
				e.getMessage();
			}
		}
	}

	@Override
	public void executarEnvioMensagenAdvertenciaResponsavelLegalAluno(AdvertenciaVO advertenciaVO, UsuarioVO usuarioVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ADVERTENCIA_ALUNO, false, advertenciaVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), null);
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();

		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			List<PessoaVO> pessoaVOs = getFacadeFactory().getPessoaFacade().consultarResponsavelLegalAluno(advertenciaVO.getMatriculaVO().getAluno().getCodigo(), usuarioVO);

			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());

			String mensagemEditada = obterMensagemFormatadaMensagemAdvertenciaResponsavelAlunoEmail(advertenciaVO.getMatriculaVO().getAluno(), advertenciaVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemAdvertenciaResponsavelAlunoEmail(advertenciaVO.getMatriculaVO().getAluno(), advertenciaVO, mensagemTemplate.getMensagemSMS());

			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(pessoaVOs));
			comunicacaoEnviar.setTipoDestinatario("RL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);

		}
	}

	public String obterMensagemFormatadaMensagemAdvertenciaResponsavelAlunoEmail(PessoaVO aluno, AdvertenciaVO advertenciaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), aluno.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TIPO_ADVERTENCIA.name(), advertenciaVO.getTipoAdvertenciaVO().getDescricao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.OBSERVACAO_ADVERTENCIA.name(), advertenciaVO.getAdvertencia());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_ADVERTENCIA.name(), advertenciaVO.getDataAdvertencia_Apresentar());
		return mensagemTexto;

	}

	public List<ComunicadoInternoDestinatarioVO> obterListaDestinatariosPessoa(List<PessoaVO> pessoaVOs) {
		List<ComunicadoInternoDestinatarioVO> comunicadoInternoDestinatarioVOs = new ArrayList<ComunicadoInternoDestinatarioVO>();
		ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = null;
		for (PessoaVO pessoaVO : pessoaVOs) {
			comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
			comunicadoInternoDestinatarioVO.setCiJaLida(Boolean.FALSE);
			comunicadoInternoDestinatarioVO.setDestinatario(pessoaVO);
			comunicadoInternoDestinatarioVO.setEmail(pessoaVO.getEmail());
			comunicadoInternoDestinatarioVO.setNome(pessoaVO.getNome());
			comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			comunicadoInternoDestinatarioVOs.add(comunicadoInternoDestinatarioVO);
			comunicadoInternoDestinatarioVO = null;
		}
		return comunicadoInternoDestinatarioVOs;
	}

	/**
	 * @author Victor Hugo 03/12/2014
	 * @param calendarioAtividadeMatriculaVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemAcessoConteudoEstudo(final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ACESSO_CONTEUDO_ESTUDO, false, 
				calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemAcessoConteudoEstudo(calendarioAtividadeMatriculaVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemAcessoConteudoEstudo(calendarioAtividadeMatriculaVO, mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(usuario.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemAcessoConteudoEstudo(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome());
		String dataInicio = Uteis.getData(calendarioAtividadeMatriculaVO.getDataInicio(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_INICIO.name(), dataInicio);
		String dataFim = Uteis.getData(calendarioAtividadeMatriculaVO.getDataFim(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE.name(), dataFim);
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 03/12/2014
	 * @param calendarioAtividadeMatriculaVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemAcessoAvaliacaoOnline(final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ACESSO_AVALIACAO_ONLINE, false, 
				calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemAcessoConteudoEstudo(calendarioAtividadeMatriculaVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemAcessoConteudoEstudo(calendarioAtividadeMatriculaVO, mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(usuario.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemAcessoAvaliacaoOnline(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome());
		String dataInicio = Uteis.getData(calendarioAtividadeMatriculaVO.getDataInicio(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_INICIO.name(), dataInicio);
		String dataFim = Uteis.getData(calendarioAtividadeMatriculaVO.getDataFim(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE.name(), dataFim);
		return mensagemTexto;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemNovaAtividadeDiscursiva(final AtividadeDiscursivaVO atividdadeDiscursiva, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_NOVA_ATIVIDADE_DISCURSIVA, false, atividdadeDiscursiva.getTurmaVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			List<MatriculaPeriodoTurmaDisciplinaVO> lista = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
			if (atividdadeDiscursiva.getPublicoAlvo().getIsTipoAluno()) {
				lista.add(atividdadeDiscursiva.getMatriculaPeriodoTurmaDisciplinaVO());
			} else {
				lista = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaPorAtividadeDiscursivas(atividdadeDiscursiva, usuario);
			}
			for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : lista) {
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				String mensagemEditada = obterMensagemFormatadaMensagemNovaAtividadeDiscursiva(atividdadeDiscursiva, matriculaPeriodoTurmaDisciplinaVO, mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaMensagemNovaAtividadeDiscursiva(atividdadeDiscursiva, matriculaPeriodoTurmaDisciplinaVO, mensagemTemplate.getMensagemSMS());
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getAluno(), mensagemTemplate.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("AL");
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
			}
		}
	}

	public String obterMensagemFormatadaMensagemNovaAtividadeDiscursiva(AtividadeDiscursivaVO atividadeDiscursiva, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), atividadeDiscursiva.getResponsavelCadastro().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), atividadeDiscursiva.getDisciplinaVO().getNome());
		if (atividadeDiscursiva.getTurmaDisciplinaDefinicoesTutoriaOnlineEnum().isProgramacaoAula()) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.PERIODO_ATIVIDADE_DISCURSIVA.name(), Uteis.getData(atividadeDiscursiva.getDataLiberacao(), "dd/MM/yyyy HH:mm") + " até " + Uteis.getData(atividadeDiscursiva.getDataLimiteEntrega(), "dd/MM/yyyy HH:mm") + ".");
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.PERIODO_ATIVIDADE_DISCURSIVA.name(), atividadeDiscursiva.getQtdDiasParaConclusao() + " dia(s) após o iníco do estudo Online. ");
		}

		return mensagemTexto;
	}

	@Override
	public void executarEnvioMensagemPeriodoMaximoAtividadeDiscursiva(final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA, false, 
				calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemPeriodoMaximoAtividadeDiscursiva(calendarioAtividadeMatriculaVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemPeriodoMaximoAtividadeDiscursiva(calendarioAtividadeMatriculaVO, mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(usuario.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemPeriodoMaximoAtividadeDiscursiva(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ATIVIDADE_DISCURSIVA.name(), calendarioAtividadeMatriculaVO.getAtividadeDiscursivaVO().getCodigo().toString());
		String dataFim = Uteis.getData(calendarioAtividadeMatriculaVO.getDataFim(), "dd/MM/yyyy HH:mm");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE.name(), dataFim);
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 03/12/2014
	 * @param calendarioAtividadeMatriculaVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemPeriodoMaximoConclusaoCurso(final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_PERIODO_MAXIMO_CONCLUSAO_CURSO, false, 
				calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemPeriodoMaximoConclusaoCurso(calendarioAtividadeMatriculaVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemPeriodoMaximoConclusaoCurso(calendarioAtividadeMatriculaVO, mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(usuario.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemPeriodoMaximoConclusaoCurso(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getNome());
		String dataInicio = Uteis.getData(calendarioAtividadeMatriculaVO.getDataInicio(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_INICIO.name(), dataInicio);
		String dataFim = Uteis.getData(calendarioAtividadeMatriculaVO.getDataFim(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE.name(), dataFim);
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 03/12/2014
	 * @param calendarioAtividadeMatriculaVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemPeriodoMaximoConclusaoDisciplinas(final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS, false,
				calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemPeriodoMaximoConclusaoDisciplinas(calendarioAtividadeMatriculaVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemPeriodoMaximoConclusaoDisciplinas(calendarioAtividadeMatriculaVO, mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(usuario.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemPeriodoMaximoConclusaoDisciplinas(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getCurso().getNome());
		String dataFim = Uteis.getData(calendarioAtividadeMatriculaVO.getDataFim(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE.name(), dataFim);
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 09/12/2014
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacao1AtrasoEstudosAluno(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO1_CONFIGURACAOEAD_ALUNOS_ESTUDOS_ATRASADOS, false,
				matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatricula());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemNotificacao1AtrasoEstudosAluno(matriculaPeriodoTurmaDisciplinaVO, pessoaVO.getNome(), mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacao1AtrasoEstudosAluno(matriculaPeriodoTurmaDisciplinaVO, pessoaVO.getNome(), mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNotificacao1AtrasoEstudosAluno(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, String nomeAluno, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getCurso().getNome());
		Map<String, Object> auxiliar = new HashMap<String, Object>();
		getFacadeFactory().getConteudoFacade().gerarCalculosDesempenhoAlunoEstudosOnline(auxiliar, matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getMatricula(), matriculaPeriodoTurmaDisciplinaVO.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getModalidadeDisciplina(), null);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONTEUDO_ESTUDADO.name(), auxiliar.get("percentEstudado").toString());
		String dataFim = Uteis.getData(new Date(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_AULA.name(), dataFim);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONTEUDO_ESPERADO.name(), auxiliar.get("percentEsperadoAtingir").toString());
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 09/12/2014
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacao2AtrasoEstudosAluno(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO2_CONFIGURACAOEAD_ALUNOS_ESTUDOS_ATRASADOS, false, 
				matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatricula());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemNotificacao2AtrasoEstudosAluno(matriculaPeriodoTurmaDisciplinaVO, pessoaVO.getNome(), mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacao2AtrasoEstudosAluno(matriculaPeriodoTurmaDisciplinaVO, pessoaVO.getNome(), mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNotificacao2AtrasoEstudosAluno(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, String nomeAluno, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getCurso().getNome());
		Map<String, Object> auxiliar = new HashMap<String, Object>();
		getFacadeFactory().getConteudoFacade().gerarCalculosDesempenhoAlunoEstudosOnline(auxiliar, matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getMatricula(), matriculaPeriodoTurmaDisciplinaVO.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getModalidadeDisciplina(), null);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONTEUDO_ESTUDADO.name(), auxiliar.get("percentEstudado").toString());
		String dataFim = Uteis.getData(new Date(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_AULA.name(), dataFim);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONTEUDO_ESPERADO.name(), auxiliar.get("percentEsperadoAtingir").toString());
		ConfiguracaoEADVO configuracaoEADVO = getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.PRAZO_CONCLUSAO_ESTUDOS.name(), configuracaoEADVO.getNotDoisPrazoConclusaoEstudos().toString());
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 09/12/2014
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacao3AtrasoEstudosAluno(final MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO3_CONFIGURACAOEAD_ALUNOS_ESTUDOS_ATRASADOS, false, 
				matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatricula());
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemNotificacao3AtrasoEstudosAluno(matriculaPeriodoTurmaDisciplinaVO, pessoaVO.getNome(), mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacao3AtrasoEstudosAluno(matriculaPeriodoTurmaDisciplinaVO, pessoaVO.getNome(), mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNotificacao3AtrasoEstudosAluno(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, String nomeAluno, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nomeAluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getCurso().getNome());
		Map<String, Object> auxiliar = new HashMap<String, Object>();
		getFacadeFactory().getConteudoFacade().gerarCalculosDesempenhoAlunoEstudosOnline(auxiliar, matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getMatricula(), matriculaPeriodoTurmaDisciplinaVO.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getModalidadeDisciplina(), null);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONTEUDO_ESTUDADO.name(), auxiliar.get("percentEstudado").toString());
		String dataFim = Uteis.getData(new Date(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_AULA.name(), dataFim);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONTEUDO_ESPERADO.name(), auxiliar.get("percentEsperadoAtingir").toString());
		ConfiguracaoEADVO configuracaoEADVO = getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.PRAZO_CONCLUSAO_ESTUDOS.name(), configuracaoEADVO.getNotTresPrazoConclusaoEstudos().toString());
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 10/12/2014
	 * @param SqlRowSet
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoDiasSemLogar(final SqlRowSet resultSet, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.valueOf(resultSet.getString("templateNotificacao")), false, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			PessoaVO pessoaVO = new PessoaVO();
			pessoaVO.setCodigo(resultSet.getInt("codigo"));
			pessoaVO.setNome(resultSet.getString("nome"));
			pessoaVO.setEmail(resultSet.getString("email"));
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			if (TemplateMensagemAutomaticaEnum.valueOf(resultSet.getString("templateNotificacao")).equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO1_CONFIGURACAOEAD_ALUNOS_SEM_LOGAR_SISTEMA)) {
				mensagemEditada = obterMensagemFormatadaMensagemNNotificacao1DiasSemLogar(resultSet, mensagemTemplate.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNNotificacao1DiasSemLogar(resultSet, mensagemTemplate.getMensagemSMS());
			} else if (TemplateMensagemAutomaticaEnum.valueOf(resultSet.getString("templateNotificacao")).equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO1_CONFIGURACAOEAD_ALUNOS_SEM_LOGAR_SISTEMA)) {
				mensagemEditada = obterMensagemFormatadaMensagemNNotificacao2DiasSemLogar(resultSet, mensagemTemplate.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNNotificacao2DiasSemLogar(resultSet, mensagemTemplate.getMensagemSMS());
			} else if (TemplateMensagemAutomaticaEnum.valueOf(resultSet.getString("templateNotificacao")).equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO1_CONFIGURACAOEAD_ALUNOS_SEM_LOGAR_SISTEMA)) {
				mensagemEditada = obterMensagemFormatadaMensagemNNotificacao3DiasSemLogar(resultSet, mensagemTemplate.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNNotificacao3DiasSemLogar(resultSet, mensagemTemplate.getMensagemSMS());
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNNotificacao1DiasSemLogar(SqlRowSet resultSet, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), resultSet.getString("nome"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), resultSet.getString("nomedisciplina"));
		Integer diasSemLogar = resultSet.getInt("notUmDiasSemLogarSistema");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DIAS_SEM_LOGAR.name(), diasSemLogar.toString());
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemNNotificacao2DiasSemLogar(SqlRowSet resultSet, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), resultSet.getString("nome"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), resultSet.getString("nomedisciplina"));
		Integer diasSemLogar = resultSet.getInt("notUmDiasSemLogarSistema");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DIAS_SEM_LOGAR.name(), diasSemLogar.toString());
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemNNotificacao3DiasSemLogar(SqlRowSet resultSet, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), resultSet.getString("nome"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), resultSet.getString("nomedisciplina"));
		Integer diasSemLogar = resultSet.getInt("notUmDiasSemLogarSistema");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DIAS_SEM_LOGAR.name(), diasSemLogar.toString());
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 23/12/2014
	 * @param SqlRowSet
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoDiasAntesConclusaoAtividadeDiscursiva(final SqlRowSet resultSet, UsuarioVO usuario) throws Exception {
		int unidadeEnsino = 0;
		if (Uteis.isAtributoPreenchido(resultSet.getInt("unidadeensino"))) {
			unidadeEnsino = resultSet.getInt("unidadeensino");
		}
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.valueOf(resultSet.getString("templateNotificacao")), false, unidadeEnsino, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			PessoaVO pessoaVO = null;
			pessoaVO = new PessoaVO();
			pessoaVO.setCodigo(resultSet.getInt("codigo"));
			pessoaVO.setNome(resultSet.getString("nome"));
			pessoaVO.setEmail(resultSet.getString("email"));
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			String campoDataNotificacao = "";
			if (TemplateMensagemAutomaticaEnum.valueOf(resultSet.getString("templateNotificacao")).equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO1_CONFIGURACAOEAD_ALUNOS_ATIVIDADE_DISCURSIVA_PRAZO_CONCLUSAO)) {
				mensagemEditada = obterMensagemFormatadaMensagemNotificacao1DiasAntesConclusaoAtividadeDiscursiva(resultSet, mensagemTemplate.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacao1DiasAntesConclusaoAtividadeDiscursiva(resultSet, mensagemTemplate.getMensagemSMS());
				campoDataNotificacao = "dataprimeiranotificacao";
			} else if (TemplateMensagemAutomaticaEnum.valueOf(resultSet.getString("templateNotificacao")).equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO2_CONFIGURACAOEAD_ALUNOS_ATIVIDADE_DISCURSIVA_PRAZO_CONCLUSAO)) {
				mensagemEditada = obterMensagemFormatadaMensagemNotificacao2DiasAntesConclusaoAtividadeDiscursiva(resultSet, mensagemTemplate.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacao2DiasAntesConclusaoAtividadeDiscursiva(resultSet, mensagemTemplate.getMensagemSMS());
				campoDataNotificacao = "datasegundanotificao";
			} else if (TemplateMensagemAutomaticaEnum.valueOf(resultSet.getString("templateNotificacao")).equals(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO3_CONFIGURACAOEAD_ALUNOS_ATIVIDADE_DISCURSIVA_PRAZO_CONCLUSAO)) {
				mensagemEditada = obterMensagemFormatadaMensagemNotificacao3DiasAntesConclusaoAtividadeDiscursiva(resultSet, mensagemTemplate.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacao3DiasAntesConclusaoAtividadeDiscursiva(resultSet, mensagemTemplate.getMensagemSMS());
				campoDataNotificacao = "dataterceiranotificacao";
			}
			getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().incluirDataNotificacaoAtividadeDiscursivaRespostaAluno(resultSet.getInt("atividadediscursivarespostaaluno"), new Date(), campoDataNotificacao);
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			if (Uteis.isAtributoPreenchido(resultSet.getBoolean("aluno")) && resultSet.getBoolean("aluno")) {
				comunicacaoEnviar.setTipoDestinatario("AL");
			} else if (Uteis.isAtributoPreenchido(resultSet.getBoolean("professor")) && resultSet.getBoolean("professor")) {
				comunicacaoEnviar.setTipoDestinatario("PR");
			}
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNotificacao1DiasAntesConclusaoAtividadeDiscursiva(SqlRowSet resultSet, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), resultSet.getString("nome"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DIAS_CONCLUSAO_ATIVIDADE.name(), Uteis.getDataAplicandoFormatacao(resultSet.getDate("datalimiteentrega"), "dd/MM/yyyy HH:mm"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), resultSet.getString("nomedisciplina"));
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemNotificacao2DiasAntesConclusaoAtividadeDiscursiva(SqlRowSet resultSet, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), resultSet.getString("nome"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DIAS_CONCLUSAO_ATIVIDADE.name(), Uteis.getDataAplicandoFormatacao(resultSet.getDate("datalimiteentrega"), "dd/MM/yyyy HH:mm"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), resultSet.getString("nomedisciplina"));
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemNotificacao3DiasAntesConclusaoAtividadeDiscursiva(SqlRowSet resultSet, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), resultSet.getString("nome"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), resultSet.getString("nomedisciplina"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DIAS_CONCLUSAO_ATIVIDADE.name(), UteisData.getCompareData(resultSet.getDate("datalimiteentrega"), new Date()).toString());
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 23/12/2014
	 * @param atividadeDiscursivaInteracaoVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoNovaInteraçaoAtividadeDiscursiva(AtividadeDiscursivaInteracaoVO atividadeDiscursivaInteracaoVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_INTERACAO_ATIVIDADE_DISCURSIVA, false, 
				atividadeDiscursivaInteracaoVO.getAtividadeDiscursivaRepostaAlunoVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			PessoaVO pessoaVO = null;
			PessoaVO pessoaVO2 = null;
			atividadeDiscursivaInteracaoVO = getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().consultarPorChavePrimaria(atividadeDiscursivaInteracaoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
			atividadeDiscursivaInteracaoVO.setAtividadeDiscursivaRepostaAlunoVO(getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarPorChavePrimaria(atividadeDiscursivaInteracaoVO.getAtividadeDiscursivaRepostaAlunoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
			if (atividadeDiscursivaInteracaoVO.getInteragidoPorEnum().equals(InteragidoPorEnum.PROFESSOR)) {
				pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorMatricula(atividadeDiscursivaInteracaoVO.getAtividadeDiscursivaRepostaAlunoVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatricula());
				pessoaVO2 = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(atividadeDiscursivaInteracaoVO.getUsuarioVO().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				comunicacaoEnviar.setTipoDestinatario("AL");
			} else if (atividadeDiscursivaInteracaoVO.getInteragidoPorEnum().equals(InteragidoPorEnum.ALUNO)) {
				pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(atividadeDiscursivaInteracaoVO.getAtividadeDiscursivaRepostaAlunoVO().getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
				pessoaVO2 = getFacadeFactory().getPessoaFacade().consultarPorMatricula(atividadeDiscursivaInteracaoVO.getAtividadeDiscursivaRepostaAlunoVO().getMatriculaPeriodoTurmaDisciplinaVO().getMatricula());
				comunicacaoEnviar.setTipoDestinatario("PR");
			}
			if (Uteis.isAtributoPreenchido(pessoaVO2)) {
				mensagemEditada = obterMensagemFormatadaMensagemNotificacaoNovaInteraçaoAtividadeDiscursiva(pessoaVO2.getNome(), atividadeDiscursivaInteracaoVO.getDataInteracao(), mensagemTemplate.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoNovaInteraçaoAtividadeDiscursiva(pessoaVO2.getNome(), atividadeDiscursivaInteracaoVO.getDataInteracao(), mensagemTemplate.getMensagemSMS());
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			if (Uteis.isAtributoPreenchido(pessoaVO2)) {
				comunicacaoEnviar.setResponsavel(pessoaVO2);
			}else {
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			}
			comunicacaoEnviar.setData(new Date());
			if (Uteis.isAtributoPreenchido(pessoaVO)) {
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			}
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoNovaInteraçaoAtividadeDiscursiva(String nome, Date dataInteracao, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), nome);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_TRAMITE.name(), Uteis.getDataAplicandoFormatacao(dataInteracao, "dd/MM/yyyy HH:mm"));
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 23/12/2014
	 * @param atividadeDiscursivaRespostaAlunoVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoAlunoAvaliadoAtividadeDiscursiva(final AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ALUNO_AVALIADO_ATIVIDADE_DISCURSIVA, false, 
				atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			PessoaVO pessoaVO = null;
			pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorMatricula(atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatricula());
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			mensagemEditada = obterMensagemFormatadaMensagemNotificacaoAlunoAvaliadoAtividadeDiscursiva(pessoaVO.getNome(), atividadeDiscursivaRespostaAlunoVO.getAtividadeDiscursivaVO().getDisciplinaVO().getNome(), atividadeDiscursivaRespostaAlunoVO.getNota(), mensagemTemplate.getMensagem());
			;
			mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoAlunoAvaliadoAtividadeDiscursiva(pessoaVO.getNome(), atividadeDiscursivaRespostaAlunoVO.getAtividadeDiscursivaVO().getDisciplinaVO().getNome(), atividadeDiscursivaRespostaAlunoVO.getNota(), mensagemTemplate.getMensagemSMS());
			;
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoAlunoAvaliadoAtividadeDiscursiva(String nome, String nomeDisciplina, Double nota, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nome);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), nomeDisciplina);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOTA.name(), Uteis.formatarDecimalDuasCasas(nota));

		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 24/12/2014
	 * @param atividadeDiscursivaRespostaAlunoVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoSolicitarAvaliacaoProfessorAtividadeDiscursiva(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROFESSOR_SOLICITAR_AVALIACAO_ATIVIDADE_DISCURSIVA, false, 
				atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			PessoaVO pessoaVO = null;
			PessoaVO pessoaVO2 = null;
			pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
			pessoaVO2 = getFacadeFactory().getPessoaFacade().consultarPorMatricula(atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatricula());
			mensagemEditada = obterMensagemFormatadaMensagemNotificacaoSolicitarAvaliacaoProfessorAtividadeDiscursiva(pessoaVO.getNome(), pessoaVO2.getNome(), atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome(), atividadeDiscursivaRespostaAlunoVO.getDataLimiteEntrega(), mensagemTemplate.getMensagem());
			mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoSolicitarAvaliacaoProfessorAtividadeDiscursiva(pessoaVO.getNome(), pessoaVO2.getNome(), atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome(), atividadeDiscursivaRespostaAlunoVO.getDataLimiteEntrega(), mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("PR");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoSolicitarAvaliacaoProfessorAtividadeDiscursiva(String nomeProfessor, String nomeAluno, String nomeDisciplina, Date dataLimiteEntrega, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), nomeProfessor);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nomeAluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), nomeDisciplina);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_TRAMITE.name(), Uteis.getDataAplicandoFormatacao(dataLimiteEntrega, "dd/MM/yyyy HH:mm"));
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 24/12/2014
	 * @param atividadeDiscursivaRespostaAlunoVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoSolicitarNovaRespostaAlunoAtividadeDiscursiva(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ALUNO_SOLICITAR_NOVA_RESPOSTA_ATIVIDADE_DISCURSIVA, false, 
				atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			PessoaVO pessoaVO = null;
			pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorMatricula(atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatricula());
			mensagemEditada = obterMensagemFormatadaMensagemNotificacaoSolicitarNovaRespostaAlunoAtividadeDiscursiva(pessoaVO.getNome(), atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome(), atividadeDiscursivaRespostaAlunoVO.getDataLimiteEntrega(), mensagemTemplate.getMensagem());
			mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoSolicitarNovaRespostaAlunoAtividadeDiscursiva(pessoaVO.getNome(), atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome(), atividadeDiscursivaRespostaAlunoVO.getDataLimiteEntrega(), mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoSolicitarNovaRespostaAlunoAtividadeDiscursiva(String nomeAluno, String nomeDisciplina, Date dataLimiteEntrega, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nomeAluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), nomeDisciplina);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_TRAMITE.name(), Uteis.getDataAplicandoFormatacao(dataLimiteEntrega, "dd/MM/yyyy HH:mm"));
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 06/01/2014
	 * @param notificacoesDuvidasNaoRespondidas
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoDuvidasNaoRespondidasNoPrazoProfessor(final SqlRowSet notificacoesDuvidasNaoRespondidas, UsuarioVO usuario) throws Exception {
		int unidadeEnsino = 0;
		if (Uteis.isAtributoPreenchido(notificacoesDuvidasNaoRespondidas.getInt("unidadeensino"))) {
			unidadeEnsino = notificacoesDuvidasNaoRespondidas.getInt("unidadeensino");
		}
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROFESSOR_DUVIDAS_NAO_RESPONDIDAS, false, unidadeEnsino, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			PessoaVO pessoaVO = new PessoaVO();
			pessoaVO.setCodigo(notificacoesDuvidasNaoRespondidas.getInt("professor"));
			pessoaVO.setNome(notificacoesDuvidasNaoRespondidas.getString("nomeprofessor"));
			pessoaVO.setEmail(notificacoesDuvidasNaoRespondidas.getString("email"));
			mensagemEditada = obterMensagemFormatadaMensagemNotificacaoDuvidasNaoRespondidasProfessor(notificacoesDuvidasNaoRespondidas.getString("nomeprofessor"), notificacoesDuvidasNaoRespondidas.getString("nomealuno"), notificacoesDuvidasNaoRespondidas.getDate("dataalteracao"), notificacoesDuvidasNaoRespondidas.getInt("notificacaoprofessordiasduvidasnaorespondidas"), mensagemTemplate.getMensagem());
			mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoDuvidasNaoRespondidasProfessor(notificacoesDuvidasNaoRespondidas.getString("nomeprofessor"), notificacoesDuvidasNaoRespondidas.getString("nomealuno"), notificacoesDuvidasNaoRespondidas.getDate("dataalteracao"), notificacoesDuvidasNaoRespondidas.getInt("notificacaoprofessordiasduvidasnaorespondidas"), mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("PR");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoDuvidasNaoRespondidasProfessor(String nomeProfessor, String nomeAluno, Date dataUltimaInteracao, Integer numeroDiasSemResposta, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), nomeProfessor);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nomeAluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_ULTIMA_INTERACAO.name(), Uteis.getDataAplicandoFormatacao(dataUltimaInteracao, "dd/MM/yyyy HH:mm"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_DIAS_SEM_RESPOSTA.name(), numeroDiasSemResposta.toString());

		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 07/01/2014
	 * @param notificacoesDuvidasNaoRespondidas
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoGrupoDestinatarioDuvidasNaoRespondidasNoPrazoProfessor(final SqlRowSet notificacoesDuvidasNaoRespondidas, UsuarioVO usuario) throws Exception {
		int unidadeEnsino = 0;
		if (Uteis.isAtributoPreenchido(notificacoesDuvidasNaoRespondidas.getInt("unidadeensino"))) {
			unidadeEnsino = notificacoesDuvidasNaoRespondidas.getInt("unidadeensino");
		}
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_GRUPO_DESTINATARIO_COODENADOR_DUVIDAS_NAO_RESPONDIDAS_PROFESSOR, false, unidadeEnsino, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			PessoaVO pessoaVO = new PessoaVO();
			pessoaVO.setCodigo(notificacoesDuvidasNaoRespondidas.getInt("destinatario"));
			pessoaVO.setNome(notificacoesDuvidasNaoRespondidas.getString("nomedestinatario"));
			pessoaVO.setEmail(notificacoesDuvidasNaoRespondidas.getString("emaildestinatario"));
			mensagemEditada = obterMensagemFormatadaMensagemNotificacaoGrupoDestinatarioDuvidasNaoRespondidasNoPrazoProfessor(notificacoesDuvidasNaoRespondidas.getString("nomedestinatario"), notificacoesDuvidasNaoRespondidas.getString("nomealuno"), notificacoesDuvidasNaoRespondidas.getDate("dataalteracao"), notificacoesDuvidasNaoRespondidas.getString("nomeprofessor"), notificacoesDuvidasNaoRespondidas.getInt("notificacaoprofessordiasduvidasnaorespondidas"), mensagemTemplate.getMensagem());
			mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoGrupoDestinatarioDuvidasNaoRespondidasNoPrazoProfessor(notificacoesDuvidasNaoRespondidas.getString("nomedestinatario"), notificacoesDuvidasNaoRespondidas.getString("nomealuno"), notificacoesDuvidasNaoRespondidas.getDate("dataalteracao"), notificacoesDuvidasNaoRespondidas.getString("nomeprofessor"), notificacoesDuvidasNaoRespondidas.getInt("notificacaoprofessordiasduvidasnaorespondidas"), mensagemTemplate.getMensagemSMS());
			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("FU");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoGrupoDestinatarioDuvidasNaoRespondidasNoPrazoProfessor(String nomeDestinatario, String nomeAluno, Date dataUltimaInteracao, String nomeProfessor, Integer numeroDiasSemResposta, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DESTINATARIO.name(), nomeDestinatario);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nomeAluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_ULTIMA_INTERACAO.name(), Uteis.getDataAplicandoFormatacao(dataUltimaInteracao, "dd/MM/yyyy HH:mm"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), nomeProfessor);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_DIAS_SEM_RESPOSTA.name(), numeroDiasSemResposta.toString());

		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 07/01/2014
	 * @param codigoDuvidaProfessorVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoNovaDuvidaProfessorCriada(Integer codigoDuvidaProfessorVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_NOVA_DUVIDA_PROFESSOR_CRIADA, false, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			PessoaVO pessoaVO = null;
			SqlRowSet rs = getFacadeFactory().getDuvidaProfessorFacade().consultarDadosEnvioNotificacaoDuvidaProfessor(codigoDuvidaProfessorVO);
			if (rs.next()) {
				pessoaVO = new PessoaVO();
				pessoaVO.setCodigo(rs.getInt("professor"));
				pessoaVO.setNome(rs.getString("nomeprofessor"));
				pessoaVO.setEmail(rs.getString("emailprofessor"));
				PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_DUVIDA_PROFESSOR_FINALIZADA, false, Uteis.isAtributoPreenchido(rs.getInt("unidadeensino")) ? rs.getInt("unidadeensino") : 0, usuario);
				mensagemTemplateUtilizar = mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica() ? mensagemTemplateUtilizar : mensagemTemplate;
				comunicacaoEnviar.setAssunto(mensagemTemplateUtilizar.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplateUtilizar.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplateUtilizar.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplateUtilizar.getCopiaFollowUp());
				mensagemEditada = obterMensagemFormatadaMensagemNotificacaoNovaDuvidaProfessorCriada(pessoaVO.getNome(), rs.getString("nomealuno"), rs.getString("assunto"), mensagemTemplateUtilizar.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoNovaDuvidaProfessorCriada(pessoaVO.getNome(), rs.getString("nomealuno"), rs.getString("assunto"), mensagemTemplateUtilizar.getMensagemSMS());
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!mensagemTemplateUtilizar.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplateUtilizar.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("PR");
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
			}
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoNovaDuvidaProfessorCriada(String nomeProfessor, String nomeAluno, String assuntoDuvida, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), nomeProfessor);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nomeAluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ASSUNTO_DUVIDA.name(), assuntoDuvida);
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 07/01/2014
	 * @param codigoDuvidaProfessorVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoSolicitacaoRespostaAlunoDuvidaProfessor(Integer codigoDuvidaProfessorVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_SOLICITACAO_RESPOSTA_ALUNO_DUVIDA_PROFESSOR, false, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			PessoaVO pessoaVO = null;
			SqlRowSet rs = getFacadeFactory().getDuvidaProfessorFacade().consultarDadosEnvioNotificacaoDuvidaProfessor(codigoDuvidaProfessorVO);
			if (rs.next()) {
				pessoaVO = new PessoaVO();
				pessoaVO.setCodigo(rs.getInt("aluno"));
				pessoaVO.setNome(rs.getString("nomealuno"));
				pessoaVO.setEmail(rs.getString("emailaluno"));
				PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_DUVIDA_PROFESSOR_FINALIZADA, false, Uteis.isAtributoPreenchido(rs.getInt("unidadeensino")) ? rs.getInt("unidadeensino") : 0, usuario);
				mensagemTemplateUtilizar = mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica() ? mensagemTemplateUtilizar : mensagemTemplate;
				comunicacaoEnviar.setAssunto(mensagemTemplateUtilizar.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplateUtilizar.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplateUtilizar.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplateUtilizar.getCopiaFollowUp());
				mensagemEditada = obterMensagemFormatadaMensagemNotificacaoSolicitacaoRespostaAlunoDuvidaProfessor(pessoaVO.getNome(), rs.getString("nomeprofessor"), rs.getString("assunto"), mensagemTemplateUtilizar.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoSolicitacaoRespostaAlunoDuvidaProfessor(pessoaVO.getNome(), rs.getString("nomeprofessor"), rs.getString("assunto"), mensagemTemplateUtilizar.getMensagemSMS());
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!mensagemTemplateUtilizar.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplateUtilizar.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("AL");
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
			}
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoSolicitacaoRespostaAlunoDuvidaProfessor(String nomeAluno, String nomeProfessor, String assuntoDuvida, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nomeAluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), nomeProfessor);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ASSUNTO_DUVIDA.name(), assuntoDuvida);
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 07/01/2014
	 * @param codigoDuvidaProfessorVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoSolicitacaoRespostaProfessorDuvidaProfessor(Integer codigoDuvidaProfessorVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_SOLICITACAO_RESPOSTA_PROFESSOR_DUVIDA_PROFESSOR, false, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			PessoaVO pessoaVO = null;
			SqlRowSet rs = getFacadeFactory().getDuvidaProfessorFacade().consultarDadosEnvioNotificacaoDuvidaProfessor(codigoDuvidaProfessorVO);
			if (rs.next()) {
				pessoaVO = new PessoaVO();
				pessoaVO.setCodigo(rs.getInt("professor"));
				pessoaVO.setNome(rs.getString("nomeprofessor"));
				pessoaVO.setEmail(rs.getString("emailprofessor"));
				PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_DUVIDA_PROFESSOR_FINALIZADA, false, Uteis.isAtributoPreenchido(rs.getInt("unidadeensino")) ? rs.getInt("unidadeensino") : 0, usuario);
				mensagemTemplateUtilizar = mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica() ? mensagemTemplateUtilizar : mensagemTemplate;
				comunicacaoEnviar.setAssunto(mensagemTemplateUtilizar.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplateUtilizar.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplateUtilizar.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplateUtilizar.getCopiaFollowUp());
				mensagemEditada = obterMensagemFormatadaMensagemNotificacaoSolicitacaoRespostaProfessorDuvidaProfessor(pessoaVO.getNome(), rs.getString("nomealuno"), rs.getString("assunto"), mensagemTemplateUtilizar.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoSolicitacaoRespostaProfessorDuvidaProfessor(pessoaVO.getNome(), rs.getString("nomealuno"), rs.getString("assunto"), mensagemTemplateUtilizar.getMensagemSMS());
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!mensagemTemplateUtilizar.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplateUtilizar.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("PR");
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
			}
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoSolicitacaoRespostaProfessorDuvidaProfessor(String nomeProfessor, String nomeAluno, String assuntoDuvida, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PROFESSOR.name(), nomeProfessor);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nomeAluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ASSUNTO_DUVIDA.name(), assuntoDuvida);
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 07/01/2014
	 * @param codigoDuvidaProfessorVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoFinalizacaoDuvidaProfessor(Integer codigoDuvidaProfessorVO, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_DUVIDA_PROFESSOR_FINALIZADA, false, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			PessoaVO pessoaVO = null;
			SqlRowSet rs = getFacadeFactory().getDuvidaProfessorFacade().consultarDadosEnvioNotificacaoDuvidaProfessor(codigoDuvidaProfessorVO);
			if (rs.next()) {
				pessoaVO = new PessoaVO();
				pessoaVO.setCodigo(rs.getInt("aluno"));
				pessoaVO.setNome(rs.getString("nomealuno"));
				pessoaVO.setEmail(rs.getString("emailaluno"));
				PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_DUVIDA_PROFESSOR_FINALIZADA, false, Uteis.isAtributoPreenchido(rs.getInt("unidadeensino")) ? rs.getInt("unidadeensino") : 0, usuario);
				mensagemTemplateUtilizar = mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica() ? mensagemTemplateUtilizar : mensagemTemplate;
				comunicacaoEnviar.setAssunto(mensagemTemplateUtilizar.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplateUtilizar.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplateUtilizar.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplateUtilizar.getCopiaFollowUp());
				mensagemEditada = obterMensagemFormatadaMensagemNotificacaoFinalizacaoDuvidaProfessor(pessoaVO.getNome(), rs.getString("assunto"), mensagemTemplateUtilizar.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoFinalizacaoDuvidaProfessor(pessoaVO.getNome(), rs.getString("assunto"), mensagemTemplateUtilizar.getMensagemSMS());
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!mensagemTemplateUtilizar.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplateUtilizar.getEnviarCopiaPais()));
				comunicacaoEnviar.setTipoDestinatario("AL");
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
			}
		}
	}
	
	@Override
	public void executarEnvioMensagemTramiteInteracaoTramiteRequerimento(RequerimentoVO obj, RequerimentoHistoricoVO historico, InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistoricoVO, UsuarioVO usuarioLogadoVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_INTERACAO_TRAMITE_REQUERIMENTO, false, obj.getUnidadeEnsino().getCodigo(), null);
		List<Integer> requerimentos = new ArrayList<Integer>(0);
		if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			UsuarioVO usuarioResponsavel = null;
			if (Uteis.isAtributoPreenchido(historico.getResponsavelRequerimentoDepartamento().getCodigo())) {
				usuarioResponsavel = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(historico.getResponsavelRequerimentoDepartamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogadoVO);
			} else {
				usuarioResponsavel = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(obj.getFuncionarioVO().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogadoVO);
			}
			PessoaVO pessoaVO = null;
			String nomeUsuarioInteracao = "";
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			
			if(!obj.getPessoa().getCodigo().equals(usuarioLogadoVO.getPessoa().getCodigo())) {
				pessoaVO = new PessoaVO();			
				requerimentos.add(obj.getCodigo());
				pessoaVO.setCodigo(obj.getPessoa().getCodigo());
				pessoaVO.setNome(obj.getPessoa().getNome());
				pessoaVO.setEmail(obj.getPessoa().getEmail());
				nomeUsuarioInteracao = obj.getPessoa().getNome();
				comunicacao.setTipoDestinatario("AL");
				
			}else {
				pessoaVO = new PessoaVO();			
				requerimentos.add(obj.getCodigo());
				pessoaVO.setCodigo(usuarioResponsavel.getPessoa().getCodigo());
				pessoaVO.setNome(usuarioResponsavel.getPessoa().getNome());
				pessoaVO.setEmail(usuarioResponsavel.getPessoa().getEmail());
				nomeUsuarioInteracao = usuarioResponsavel.getPessoa().getNome();
				comunicacao.setTipoDestinatario(usuarioResponsavel.getTipoUsuario());
			}
			
			comunicacao.setResponsavel(usuarioLogadoVO.getPessoa());
			comunicacao.setData(new Date());
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCodigoTipoOrigemComunicacaoInterna(obj.getCodigo());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.ITEM_REQUERIMENTO_INTERACAO);
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemInteracaoTramiteRequerimento(interacaoRequerimentoHistoricoVO, historico, mensagemTemplate.getMensagem(), nomeUsuarioInteracao));
			
			
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	public String obterMensagemFormatadaMensagemInteracaoTramiteRequerimento(InteracaoRequerimentoHistoricoVO obj, RequerimentoHistoricoVO historico, final String mensagemTemplate, String nomeUsuarioInteracao) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_USUARIO_INTERACAO.name(), nomeUsuarioInteracao);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA.name(),Uteis.getDataAplicandoFormatacao(new Date(), "dd/MM/yyyy HH:mm"));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.INTERACAO.name(), obj.getInteracao());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DEPARTAMENTO.name(), historico.getDepartamento().getNome());
		
		return mensagemTexto;
	}

	public String obterMensagemFormatadaMensagemNotificacaoFinalizacaoDuvidaProfessor(String nomeAluno, String assuntoDuvida, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nomeAluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ASSUNTO_DUVIDA.name(), assuntoDuvida);
		return mensagemTexto;
	}

	/**
	 * @author Victor Hugo 07/01/2014
	 * @param codigoDuvidaProfessorVO
	 * @param tipoPessoaInteracaoDuvidaProfessorEnum
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void executarEnvioMensagemNotificacaoNovaInteracaoDuvidaProfessor(Integer codigoDuvidaProfessorVO, TipoPessoaInteracaoDuvidaProfessorEnum tipoPessoaInteracaoDuvidaProfessorEnum, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_NOVA_INTERACAO_DUVIDA_PROFESSOR, false, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			String mensagemEditada = "";
			String mensagemSMSEditada = "";
			PessoaVO pessoaVO = null;
			SqlRowSet rs = getFacadeFactory().getDuvidaProfessorFacade().consultarDadosEnvioNotificacaoDuvidaProfessor(codigoDuvidaProfessorVO);
			if (rs.next()) {
				PersonalizacaoMensagemAutomaticaVO mensagemTemplateUtilizar = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_DUVIDA_PROFESSOR_FINALIZADA, false, Uteis.isAtributoPreenchido(rs.getInt("unidadeensino")) ? rs.getInt("unidadeensino") : 0, usuario);
				mensagemTemplateUtilizar = mensagemTemplateUtilizar != null && !mensagemTemplateUtilizar.getDesabilitarEnvioMensagemAutomatica() ? mensagemTemplateUtilizar : mensagemTemplate;
				comunicacaoEnviar.setAssunto(mensagemTemplateUtilizar.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplateUtilizar.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplateUtilizar.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplateUtilizar.getCopiaFollowUp());
				pessoaVO = new PessoaVO();
				if (tipoPessoaInteracaoDuvidaProfessorEnum.equals(TipoPessoaInteracaoDuvidaProfessorEnum.ALUNO)) {
					pessoaVO.setCodigo(rs.getInt("professor"));
					pessoaVO.setNome(rs.getString("nomeprofessor"));
					pessoaVO.setEmail(rs.getString("emailprofessor"));
					comunicacaoEnviar.setTipoDestinatario("PR");
				} else if (tipoPessoaInteracaoDuvidaProfessorEnum.equals(TipoPessoaInteracaoDuvidaProfessorEnum.PROFESSOR)) {
					pessoaVO.setCodigo(rs.getInt("aluno"));
					pessoaVO.setNome(rs.getString("nomealuno"));
					pessoaVO.setEmail(rs.getString("emailaluno"));
					comunicacaoEnviar.setTipoDestinatario("AL");
				}
				mensagemEditada = obterMensagemFormatadaMensagemNotificacaoNovaInteracaoDuvidaProfessor(pessoaVO.getNome(), rs.getString("assunto"), mensagemTemplateUtilizar.getMensagem());
				mensagemSMSEditada = obterMensagemFormatadaMensagemNotificacaoNovaInteracaoDuvidaProfessor(pessoaVO.getNome(), rs.getString("assunto"), mensagemTemplateUtilizar.getMensagemSMS());
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!mensagemTemplateUtilizar.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplateUtilizar.getEnviarCopiaPais()));
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
			}
		}
	}

	public String obterMensagemFormatadaMensagemNotificacaoNovaInteracaoDuvidaProfessor(String nomePessoaInteracao, String assuntoDuvida, final String mensagemTemplate) throws Exception {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), nomePessoaInteracao);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ASSUNTO_DUVIDA.name(), assuntoDuvida);
		return mensagemTexto;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarEnvioMensagemDownloadAntecedenciaMaterial(SqlRowSet rs, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, ConfiguracaoGeralSistemaVO config) throws Exception {
		try {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			PessoaVO aluno = new PessoaVO();
			aluno.setCodigo(rs.getInt("codigo"));
			aluno.setNome(rs.getString("nome_aluno"));
			aluno.setEmail(rs.getString("email"));
			// Para obter a mensagem do email formatado Usamos um
			// metodo a parte.
			String mensagemEditada = obterMensagemFormatadaMensagemDownloadAntecedenciaMaterial(rs, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemDownloadAntecedenciaMaterial(rs, mensagemTemplate.getMensagemSMS());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setAluno(aluno);
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(aluno, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().registrarAlunoRecebeuNotificacaoDownloadMaterial(rs.getInt("codigoMatriculaPeriodo"), rs.getInt("codigoDisciplina"), rs.getInt("codigoTurma"));
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	public void executarEnvioMensagemNotificacaoInclusaoDisciplina(MatriculaVO matriculaVO, List<InclusaoDisciplinasHistoricoForaPrazoVO> inclusaoDisciplinasHistoricoForaPrazoVOs, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_INCLUSAO_DISCIPLINA, false, matriculaVO.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setAluno(matriculaVO.getAluno());
			comunicacaoEnviar.setAlunoNome(matriculaVO.getAluno().getNome());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaNotificacaoInclusaoDisciplina(matriculaVO, inclusaoDisciplinasHistoricoForaPrazoVOs, mensagemTemplate.getMensagemSMS()));
				comunicacaoEnviar.setEnviarSMS(true);
			}
			comunicacaoEnviar.setMensagem(obterMensagemFormatadaNotificacaoInclusaoDisciplina(matriculaVO, inclusaoDisciplinasHistoricoForaPrazoVOs, mensagemTemplate.getMensagem()));
			comunicacaoEnviar.setResponsavel(usuario.getPessoa());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaVO.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(TipoPessoa.ALUNO.getValor());
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}
	}

	private String obterMensagemFormatadaNotificacaoInclusaoDisciplina(MatriculaVO matriculaVO, List<InclusaoDisciplinasHistoricoForaPrazoVO> inclusaoDisciplinasHistoricoForaPrazoVOs, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaVO.getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matriculaVO.getMatricula());
		StringBuilder disciplinas = new StringBuilder("<ul>");
		for (InclusaoDisciplinasHistoricoForaPrazoVO idhfpVO : inclusaoDisciplinasHistoricoForaPrazoVOs) {
			disciplinas.append("<li>");
			disciplinas.append(" <strong>").append(idhfpVO.getDisciplina().getNome()).append("</strong>");
			disciplinas.append("</li>");
		}
		disciplinas.append("</ul>");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_DISCIPLINAS.name(), disciplinas.toString());
		return mensagemTexto;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemNotificacaoRespondenteAvaliacaoInstitucionalEspecifica(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, List<AvaliacaoInstitucionalAnaliticoRelVO> avaliacaoInstitucionalAnaliticoRelVOs, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, UsuarioVO usuarioVO) throws Exception {
		Boolean utilizarListagemRespondente = false;
		
		if(avaliacaoInstitucionalVO.getSituacao().equals("AT") || avaliacaoInstitucionalVO.getSituacao().equals("FI")) {
			utilizarListagemRespondente = true;
		}
		else {
			utilizarListagemRespondente = false;
		}
		
		if (avaliacaoInstitucionalAnaliticoRelVOs == null || avaliacaoInstitucionalAnaliticoRelVOs.isEmpty()) {
			avaliacaoInstitucionalAnaliticoRelVOs = getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().realizarGeracaoRelatorioAnalitico(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo(), avaliacaoInstitucionalVO, avaliacaoInstitucionalVO.getCurso().getCodigo(), 0, avaliacaoInstitucionalVO.getTurma().getCodigo(), "NAO_RESPONDIDO", "pessoa.nome", avaliacaoInstitucionalVO.getDataInicio(), avaliacaoInstitucionalVO.getDataFinal(), utilizarListagemRespondente, null, false);
		}
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		for (AvaliacaoInstitucionalAnaliticoRelVO avaliacaoInstitucionalAnaliticoRelVO : avaliacaoInstitucionalAnaliticoRelVOs) {
			if (!avaliacaoInstitucionalAnaliticoRelVO.getJaRespondeu() && !avaliacaoInstitucionalAnaliticoRelVO.getEmail().trim().isEmpty() && avaliacaoInstitucionalAnaliticoRelVO.getEmail().contains("@")) {
				try {
					ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacaoEnviar.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.AVALIACAO_INSTITUCIONAL);
					comunicacaoEnviar.setCodigoTipoOrigemComunicacaoInterna(avaliacaoInstitucionalVO.getCodigo());
					comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
					comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
					comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
					comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
					if (!avaliacaoInstitucionalAnaliticoRelVO.getMatricula().trim().isEmpty()) {
						comunicacaoEnviar.getAluno().setCodigo(avaliacaoInstitucionalAnaliticoRelVO.getAluno());
						comunicacaoEnviar.getAluno().setNome(avaliacaoInstitucionalAnaliticoRelVO.getNome());
						comunicacaoEnviar.getAluno().setEmail(avaliacaoInstitucionalAnaliticoRelVO.getEmail());
						comunicacaoEnviar.getPessoa().setCodigo(avaliacaoInstitucionalAnaliticoRelVO.getAluno());
						comunicacaoEnviar.getPessoa().setNome(avaliacaoInstitucionalAnaliticoRelVO.getNome());
						comunicacaoEnviar.getPessoa().setEmail(avaliacaoInstitucionalAnaliticoRelVO.getEmail());
						comunicacaoEnviar.setAlunoNome(avaliacaoInstitucionalAnaliticoRelVO.getNome());
						comunicacaoEnviar.setTipoDestinatario(TipoDestinatarioComunicadoInternaEnum.AL.name());
					} else {
						comunicacaoEnviar.getPessoa().setCodigo(avaliacaoInstitucionalAnaliticoRelVO.getAluno());
						comunicacaoEnviar.getPessoa().setNome(avaliacaoInstitucionalAnaliticoRelVO.getNome());
						comunicacaoEnviar.getPessoa().setEmail(avaliacaoInstitucionalAnaliticoRelVO.getEmail());
						if (avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_CURSO)
								|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA)
								|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES)
								|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.PROFESSORES_COORDENADORES)) {
							comunicacaoEnviar.getProfessor().setCodigo(avaliacaoInstitucionalAnaliticoRelVO.getAluno());
							comunicacaoEnviar.getProfessor().setNome(avaliacaoInstitucionalAnaliticoRelVO.getNome());
							comunicacaoEnviar.getProfessor().setEmail(avaliacaoInstitucionalAnaliticoRelVO.getEmail());
							comunicacaoEnviar.setTipoDestinatario(TipoDestinatarioComunicadoInternaEnum.PR.name());
						} else if (avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES)
								|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CARGO)
								|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_DEPARTAMENTO)
								|| avaliacaoInstitucionalVO.getPublicarAlvoEnum().equals(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_PROFESSOR)) {
							comunicacaoEnviar.getCoordenador().setCodigo(avaliacaoInstitucionalAnaliticoRelVO.getAluno());
							comunicacaoEnviar.getCoordenador().setNome(avaliacaoInstitucionalAnaliticoRelVO.getNome());
							comunicacaoEnviar.getCoordenador().setEmail(avaliacaoInstitucionalAnaliticoRelVO.getEmail());
							comunicacaoEnviar.setTipoDestinatario(TipoDestinatarioComunicadoInternaEnum.CO.name());
						} else {
							comunicacaoEnviar.setTipoDestinatario(TipoDestinatarioComunicadoInternaEnum.FU.name());
						}
					}
					if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica() && !mensagemTemplate.getMensagemSMS().trim().isEmpty()) {
						comunicacaoEnviar.setMensagemSMS(obterMensagemFormatadaNotificacaoRespondenteAvaliacaoInstitucional(avaliacaoInstitucionalAnaliticoRelVO, avaliacaoInstitucionalVO, mensagemTemplate.getMensagemSMS()));
						comunicacaoEnviar.setEnviarSMS(true);
					}
					comunicacaoEnviar.setMensagem(obterMensagemFormatadaNotificacaoRespondenteAvaliacaoInstitucional(avaliacaoInstitucionalAnaliticoRelVO, avaliacaoInstitucionalVO, mensagemTemplate.getMensagem()));
					comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacaoEnviar.setData(new Date());
					comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(comunicacaoEnviar.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
					comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuarioVO, config,null);
					comunicacaoEnviar = null;
				} catch (Exception e) {
					throw e;
				}
			}
		}
		getFacadeFactory().getAvaliacaoInstitucionalFacade().gravarDataUltimaNotificacao(avaliacaoInstitucionalVO.getCodigo(), usuarioVO);
	}

	private String obterMensagemFormatadaNotificacaoRespondenteAvaliacaoInstitucional(AvaliacaoInstitucionalAnaliticoRelVO avaliacaoInstitucionalAnaliticoRelVO, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), avaliacaoInstitucionalAnaliticoRelVO.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_AVALIACAO_INSTITUCIONAL.name(), avaliacaoInstitucionalVO.getNome());
		if(avaliacaoInstitucionalVO.getAvaliacaoUltimoModulo()) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_INICIO_AVALIACAO_INSTITUCIONAL.name(), avaliacaoInstitucionalVO.getDataInicio_Apresentar()+" às "+avaliacaoInstitucionalVO.getHoraInicio()+"h ");
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_TERMINO_AVALIACAO_INSTITUCIONAL.name(), Uteis.getData(Uteis.obterDataAvancada(new Date(), avaliacaoInstitucionalVO.getDiasDisponivel())) +" às "+avaliacaoInstitucionalVO.getHoraFim()+"h ");
		}else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_INICIO_AVALIACAO_INSTITUCIONAL.name(), avaliacaoInstitucionalVO.getDataInicio_Apresentar());
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_TERMINO_AVALIACAO_INSTITUCIONAL.name(), avaliacaoInstitucionalVO.getDataFinal_Apresentar());
		}
		return mensagemTexto;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemDownloadMaterialAlunosPeriodoAula(SqlRowSet rs, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		try {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			PessoaVO aluno = new PessoaVO();
			aluno.setCodigo(rs.getInt("codigo"));
			aluno.setNome(rs.getString("nome_aluno"));
			aluno.setEmail(rs.getString("email"));
			// Para obter a mensagem do email formatado Usamos um
			// metodo a parte.
			String mensagemEditada = obterMensagemFormatadaMensagemDownloadAntecedenciaMaterial(rs, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemDownloadAntecedenciaMaterial(rs, mensagemTemplate.getMensagemSMS());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setAluno(aluno);
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(aluno, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario("AL");
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().registrarAlunoRecebeuNotificacaoDownloadMaterial(rs.getInt("codigoMatriculaPeriodo"), rs.getInt("codigoDisciplina"), rs.getInt("codigoTurma"));
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemRequerimentoSolicitacaoIsencaoTaxaDeferido(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, RequerimentoVO requerimentoVO, UsuarioVO usuario) throws Exception {
		try {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(templateMensagemAutomaticaEnum, false, requerimentoVO.getUnidadeEnsino().getCodigo(), usuario);
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				// Para obter a mensagem do email formatado Usamos um
				// metodo a parte.
				String mensagemEditada = obterMensagemFormatadaRequerimentoSolicitacaoIsencaoTaxaDeferido(requerimentoVO, mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaRequerimentoSolicitacaoIsencaoTaxaDeferido(requerimentoVO, mensagemTemplate.getMensagemSMS());
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (Uteis.isAtributoPreenchido(requerimentoVO.getMatricula().getMatricula())) {
					comunicacaoEnviar.setTipoDestinatario("AL");
					comunicacaoEnviar.setAluno(requerimentoVO.getPessoa());
				} else if (Uteis.isAtributoPreenchido(requerimentoVO.getFuncionarioVO())) {
					comunicacaoEnviar.setTipoDestinatario("FU");
					comunicacaoEnviar.setAluno(requerimentoVO.getPessoa());
				} else {
					comunicacaoEnviar.setTipoDestinatario("FU");
					comunicacaoEnviar.setAluno(requerimentoVO.getPessoa());
				}
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(requerimentoVO.getPessoa(), mensagemTemplate.getEnviarCopiaPais()));
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private String obterMensagemFormatadaRequerimentoSolicitacaoIsencaoTaxaDeferido(RequerimentoVO requerimentoVO, String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_REQUERENTE.name(), requerimentoVO.getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CODIGO_REQUERIMENTO.name(), requerimentoVO.getCodigo().toString());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_REQUERIMENTO.name(), requerimentoVO.getData_Apresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TIPO_REQUERIMENTO.name(), requerimentoVO.getTipoRequerimento().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), requerimentoVO.getDisciplina().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), requerimentoVO.getTurmaReposicao().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MOTIVO_INDEFERIMENTO_TAXA_ISENCAO.name(), requerimentoVO.getMotivoDeferimentoIndeferimentoIsencaoTaxa());
		return mensagemTexto;
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemGsuiteCriacaoConta(PessoaGsuiteVO pessoaGsuite, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_GSUITE_CRIACAO_CONTA, false, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			config.setGerarSenhaCpfAluno(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoGeralSeGerarSenhaCpfAluno());
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setMensagem(obterMensagemFormatadaMensagemGsuiteCriacaoConta(pessoaGsuite, config, mensagemTemplate.getMensagem()));
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemGsuiteCriacaoConta(pessoaGsuite, config, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setAluno(pessoaGsuite.getPessoaVO());
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaGsuite.getPessoaVO(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuario, config,null);
		}
	}
	
	public String obterMensagemFormatadaMensagemGsuiteCriacaoConta(PessoaGsuiteVO pessoaGsuite, ConfiguracaoGeralSistemaVO config, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), pessoaGsuite.getPessoaVO().getNome());		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.EMAIL_GSUITE.name(), pessoaGsuite.getEmail());
		if(config.getGerarSenhaCpfAluno() || pessoaGsuite.getPessoaVO().getFuncionario()) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SENHA_GSUITE.name(), "Sua senha temporária é os números do seu cpf.");	
		}else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SENHA_GSUITE.name(), "Sua senha temporária é os números da sua matrícula.");
		}
		return mensagemTexto;
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoAprovador(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, MatriculaPeriodoVO matriculaPeriodoVO, Boolean solicitarLiberacaoFinanceira, Boolean solicitarLiberacaoMatriculaAposInicioXModulos, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		try {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(templateMensagemAutomaticaEnum, false, unidadeEnsino, usuario);
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				List<PessoaVO> lista = getFacadeFactory().getPessoaFacade().consultarAprovadorLiberacaoMatricula(matriculaPeriodoVO, solicitarLiberacaoFinanceira, solicitarLiberacaoMatriculaAposInicioXModulos, usuario);
				for (PessoaVO pessoaVO : lista) {
					ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
					comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
					comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
					comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
					comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
					// Para obter a mensagem do email formatado Usamos um
					// metodo a parte.
					String mensagemEditada = obterMensagemFormatadaNotificacaoPendenteAprovacaoAprovador(matriculaPeriodoVO, pessoaVO, mensagemTemplate.getMensagem());
					String mensagemSMSEditada = obterMensagemFormatadaNotificacaoPendenteAprovacaoAprovador(matriculaPeriodoVO, pessoaVO,  mensagemTemplate.getMensagemSMS());
					if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
						comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
						comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
					}
					comunicacaoEnviar.setMensagem(mensagemEditada);
					comunicacaoEnviar.setTipoDestinatario("FU");				
					comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
					comunicacaoEnviar.setData(new Date());
					comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, false));
					comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
					comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);	
				}
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private String obterMensagemFormatadaNotificacaoPendenteAprovacaoAprovador(MatriculaPeriodoVO matriculaPeriodoVO, PessoaVO aprovador, String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.APROVADOR.name(), aprovador.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TIPO_SOLICITACAO_PENDENCIA.name(), matriculaPeriodoVO.getMatriculaVO().getMotivoMatriculaBloqueada());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matriculaPeriodoVO.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaPeriodoVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), matriculaPeriodoVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), matriculaPeriodoVO.getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getNome());
		return mensagemTexto;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoAluno(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, MatriculaPeriodoVO matriculaPeriodoVO, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		try {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(templateMensagemAutomaticaEnum, false, unidadeEnsino, usuario);
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				// Para obter a mensagem do email formatado Usamos um
				// metodo a parte.
				String mensagemEditada = obterMensagemFormatadaNotificacaoPendenteAprovacaoAluno(matriculaPeriodoVO, mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaNotificacaoPendenteAprovacaoAluno(matriculaPeriodoVO, mensagemTemplate.getMensagemSMS());
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}

				comunicacaoEnviar.setMensagem(mensagemEditada);

				comunicacaoEnviar.setTipoDestinatario("AL");
				comunicacaoEnviar.setAluno(matriculaPeriodoVO.getMatriculaVO().getAluno());

				
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matriculaPeriodoVO.getMatriculaVO().getAluno(), mensagemTemplate.getEnviarCopiaPais()));
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private String obterMensagemFormatadaNotificacaoPendenteAprovacaoAluno(MatriculaPeriodoVO matriculaPeriodoVO, String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TIPO_SOLICITACAO_PENDENCIA.name(), matriculaPeriodoVO.getMatriculaVO().getMotivoMatriculaBloqueada());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matriculaPeriodoVO.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaPeriodoVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), matriculaPeriodoVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), matriculaPeriodoVO.getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getNome());
		return mensagemTexto;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoDeferido(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, MatriculaPeriodoVO matriculaPeriodoVO, PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		try {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(templateMensagemAutomaticaEnum, false, unidadeEnsino, usuario);
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
				// Para obter a mensagem do email formatado Usamos um
				// metodo a parte.
				String mensagemEditada = obterMensagemFormatadaNotificacaoPendenteAprovacaoDeferido(matriculaPeriodoVO, pendenciaLiberacaoMatriculaVO, mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaNotificacaoPendenteAprovacaoDeferido(matriculaPeriodoVO, pendenciaLiberacaoMatriculaVO, mensagemTemplate.getMensagemSMS());
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setMensagem(mensagemEditada);

				comunicacaoEnviar.setTipoDestinatario("FU");
				comunicacaoEnviar.setAluno(matriculaPeriodoVO.getMatriculaVO().getAluno());


				
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());
				
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().getPessoa(), false));
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private String obterMensagemFormatadaNotificacaoPendenteAprovacaoDeferido(MatriculaPeriodoVO matriculaPeriodoVO, PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO, String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;

		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SOLICITANTE.name(), pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matriculaPeriodoVO.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaPeriodoVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), matriculaPeriodoVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), matriculaPeriodoVO.getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getNome());
		return mensagemTexto;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoIndeferido(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, MatriculaPeriodoVO matriculaPeriodoVO, PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO, UsuarioVO usuario) throws Exception {
		try {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(templateMensagemAutomaticaEnum, false, matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), usuario);
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
				comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
				comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
				pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
				// Para obter a mensagem do email formatado Usamos um
				// metodo a parte.
				String mensagemEditada = obterMensagemFormatadaNotificacaoPendenteAprovacaoIndeferido(matriculaPeriodoVO, pendenciaLiberacaoMatriculaVO, mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaNotificacaoPendenteAprovacaoIndeferido(matriculaPeriodoVO, pendenciaLiberacaoMatriculaVO, mensagemTemplate.getMensagemSMS());
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setMensagem(mensagemEditada);

				comunicacaoEnviar.setTipoDestinatario("FU");
				comunicacaoEnviar.setAluno(matriculaPeriodoVO.getMatriculaVO().getAluno());

				
				comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacaoEnviar.setData(new Date());				
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().getPessoa(), false));
				comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, new UsuarioVO(), config,null);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private String obterMensagemFormatadaNotificacaoPendenteAprovacaoIndeferido(MatriculaPeriodoVO matriculaPeriodoVO, PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO, String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;

		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SOLICITANTE.name(), pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().getPessoa().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TIPO_SOLICITACAO_PENDENCIA.name(), matriculaPeriodoVO.getMatriculaVO().getMotivoMatriculaBloqueada());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matriculaPeriodoVO.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matriculaPeriodoVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), matriculaPeriodoVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), matriculaPeriodoVO.getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MOTIVO_INDEFERIMENTO.name(), pendenciaLiberacaoMatriculaVO.getMotivoIndeferimento());
		return mensagemTexto;
	}
	
	@Override
	public void enviarMensagemAvisoErroPagamentoCartaoCredito(ContaReceberVO contaReceberVO, UsuarioVO usuarioVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PAGAMENTO_RECORRENCIA_DCC_NAO_REALIZADO, false, usuarioVO);
		Boolean contaBaixada = getFacadeFactory().getContaReceberFacade().consultarContaReceberRecebidaPorCodigo(contaReceberVO.getCodigo());
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && !contaBaixada) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			PessoaVO resposavel = usuarioVO.getPessoa();
			PessoaVO pessoaVO = contaReceberVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor()) ?  contaReceberVO.getPessoa() : contaReceberVO.getResponsavelFinanceiro();
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			// Para obter a mensagem do email formatado Usamos um metodo a
			// parte.
			String mensagemEditada = obterMensagemFormatadaMensagemErroCartaoCredito(contaReceberVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemErroCartaoCredito(contaReceberVO, mensagemTemplate.getMensagemSMS());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(resposavel);
			comunicacaoEnviar.setData(contaReceberVO.getDataVencimento());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(contaReceberVO.getTipoPessoa());
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuarioVO, config,null);
		}
	}
	
	@Override
	public void enviarMensagemAvisoSucessoPagamentoCartaoCredito(ContaReceberVO contaReceberVO, UsuarioVO usuarioVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PAGAMENTO_RECORRENCIA_DCC_REALIZADO_COM_SUCESSO, false, usuarioVO);
		Boolean contaBaixada = getFacadeFactory().getContaReceberFacade().consultarContaReceberRecebidaPorCodigo(contaReceberVO.getCodigo());
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && contaBaixada) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			PessoaVO resposavel = usuarioVO.getPessoa();
			PessoaVO pessoaVO = contaReceberVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor()) ?  contaReceberVO.getPessoa() : contaReceberVO.getResponsavelFinanceiro();
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			// Para obter a mensagem do email formatado Usamos um metodo a
			// parte.
			String mensagemEditada = obterMensagemFormatadaMensagemErroCartaoCredito(contaReceberVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemErroCartaoCredito(contaReceberVO, mensagemTemplate.getMensagemSMS());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(resposavel);
			comunicacaoEnviar.setData(contaReceberVO.getDataVencimento());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(contaReceberVO.getTipoPessoa());
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuarioVO, config,null);
		}
	}
	
	public String obterMensagemFormatadaMensagemErroCartaoCredito(ContaReceberVO contaReceberVO, String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		if (contaReceberVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), contaReceberVO.getPessoa().getNome());
		} else {
			mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_PESSOA.name(), contaReceberVO.getResponsavelFinanceiro().getNome());
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), contaReceberVO.getMatriculaAluno().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TIPO_ORIGEM.name(), TipoOrigemContaReceber.getDescricao(contaReceberVO.getTipoOrigem()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NUMERO_PARCELA.name(), contaReceberVO.getParcela());
		String dataVencimento = Uteis.getData(contaReceberVO.getDataVencimento(), "dd/MM/yyyy");
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_VENCIMENTO.name(), dataVencimento);
		return mensagemTexto;

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemNotificacaoSalaAulaBlackboardOperacao(SalaAulaBlackboardOperacaoVO operacao, TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(templateMensagemAutomaticaEnum, false, usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(operacao.getPessoaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(operacao.getMsgNotificacao());			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuario, config,null);
		}
	}
	
	public String obterMensagemFormatadaMensagemNotificacaoAlteracaoSalaAulaBlackboardGrupoTcc(SalaAulaBlackboardVO sabAtual,  SalaAulaBlackboardPessoaVO aluno, String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), aluno.getPessoaEmailInstitucionalVO().getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), aluno.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SALA_BLACKBOARD_GRUPO_ANTERIOR.name(), aluno.getNomeGrupoOrigem());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SALA_BLACKBOARD_GRUPO_ATUAL.name(), sabAtual.getNome());
		return mensagemTexto;
		
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioLiberado(MatriculaVO matricula, MatriculaPeriodoVO matPer, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_LIBERADO, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && !matricula.getMatricula().isEmpty()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(matricula.getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosEstagioLiberado(matricula, mensagemTemplate.getMensagem()));
		
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}

	@Override
	public String obterMensagemFormatadaMensagemAlunosEstagioLiberado(MatriculaVO matricula, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matricula.getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), matricula.getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matricula.getMatricula());
		return mensagemTexto;

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemTccLiberado(MatriculaVO matricula, MatriculaPeriodoVO matPer, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_TCC_LIBERADO, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() && !matricula.getMatricula().isEmpty()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(matricula.getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosTccLiberado(matricula, mensagemTemplate.getMensagem()));
			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}

	@Override
	public String obterMensagemFormatadaMensagemAlunosTccLiberado(MatriculaVO matricula, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), matricula.getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), matricula.getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), matricula.getMatricula());
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioDeferimento(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_DEFERIR_RELATORIO_FINAL, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosEstagioDeferimento(obj, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemAlunosEstagioDeferimento(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioAproveitamentoDeferimento(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_DEFERIR_APROVEITAMENTO, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosEstagioAproveitamentoDeferimento(obj, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemAlunosEstagioAproveitamentoDeferimento(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioEquivalenciaDeferimento(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_DEFERIR_APROVEITAMENTO, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosEstagioEquivalenciaDeferimento(obj, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemAlunosEstagioEquivalenciaDeferimento(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioIndeferimento(EstagioVO obj, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_INDEFERIR_RELATORIO_FINAL, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosEstagioIndeferimento(obj, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemAlunosEstagioIndeferimento(EstagioVO obj, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioAproveitamentoIndeferimento(EstagioVO obj, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_INDEFERIR_APROVEITAMENTO, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosEstagioAproveitamentoIndeferimento(obj, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemAlunosEstagioAproveitamentoIndeferimento(EstagioVO obj, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioEquivalenciaIndeferimento(EstagioVO obj, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_INDEFERIR_APROVEITAMENTO, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosEstagioEquivalenciaIndeferimento(obj, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemAlunosEstagioEquivalenciaIndeferimento(EstagioVO obj, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioEquivalenciaSolicitacaoCorrecaoAluno(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_SOLICITAR_CORRECAO_EQUIVALENCIA, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosEstagioEquivalenciaSolicitacaoCorrecaoAluno(obj, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemAlunosEstagioEquivalenciaSolicitacaoCorrecaoAluno(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE_CORRECAO.name(), Uteis.getData(obj.getDataLimiteCorrecao()));		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioAproveitamentoSolicitacaoCorrecaoAluno(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_SOLICITAR_CORRECAO_APROVEITAMENTO, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosEstagioAproveitamentoSolicitacaoCorrecaoAluno(obj, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemAlunosEstagioAproveitamentoSolicitacaoCorrecaoAluno(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE_CORRECAO.name(), Uteis.getData(obj.getDataLimiteCorrecao()));		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioSolicitacaoCorrecaoAluno(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_SOLICITAR_CORRECAO_RELATORIO_FINAL, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAlunosEstagioSolicitacaoCorrecaoAluno(obj, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemAlunosEstagioSolicitacaoCorrecaoAluno(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE_CORRECAO.name(), Uteis.getData(obj.getDataLimiteCorrecao()));		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void executarEnvioMensagemEstagioObrigatorioAvisoPrazoAnaliseAvaliador(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = null;
		
		
		mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(templateMensagemAutomaticaEnum, false, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getGrupoPessoaItemVO().getPessoaVO());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemEstagioObrigatorioAvisoAnaliseAvaliador(obj, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemEstagioObrigatorioAvisoAnaliseAvaliador(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_AVALIADOR.name(), obj.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE_ANALISE.name(), Uteis.getData(obj.getDataLimiteAnalise()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemEstagioObrigatorioNotificacaoAguardandoAssinatura(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_AVISO_PRAZO_AGUARDANDO_ASSINATURA, false, null);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			 String linkAssinatura  = null;
			for (DocumentoAssinadoPessoaVO dap : obj.getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa()) {
				if(dap.getTipoPessoa().isMembroComunidade() && dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()) {
					PessoaVO pessoa = new PessoaVO();
					pessoa.setNome(dap.getNomePessoa());
					pessoa.setEmail(dap.getEmailPessoa());
					pessoa.setMembroComunidade(true);
					listaPessoas.add(pessoa);
					listaPessoas.add(obj.getMatriculaVO().getAluno());
					linkAssinatura = dap.getUrlAssinatura();
					break;
				}else if(dap.getTipoPessoa().isAluno() && dap.getSituacaoDocumentoAssinadoPessoaEnum().isPendente()) {
					listaPessoas.add(obj.getMatriculaVO().getAluno());
					linkAssinatura = dap.getUrlAssinatura();
					break;
				}
			}
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemEstagioObrigatorioNotificacaoAguardandoAssinatura(obj, linkAssinatura, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemEstagioObrigatorioNotificacaoAguardandoAssinatura(EstagioVO obj, String linkAssinatura, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LINK_ASSINATURA.name(), linkAssinatura);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CARGA_HORARIA.name(), obj.getCargaHorariaDeferida().toString());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.COMPONENTE_ESTAGIO.name(), obj.getGradeCurricularEstagioVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONCEDENTE.name(), obj.getConcedente());
		return mensagemTexto;
		
	}
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemEstagioObrigatorioNotificacaoDeCancelamentoPorFaltaDeAssinatura(EstagioVO obj, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_CANCELAMENTO_AGUARDANDO_ASSINATURA, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(obj.getMatriculaVO().getAluno());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemEstagioObrigatorioNotificacaoDeCancelamentoPorFaltaDeAssinatura(obj, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemEstagioObrigatorioNotificacaoDeCancelamentoPorFaltaDeAssinatura(EstagioVO obj, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CARGA_HORARIA.name(), obj.getCargaHorariaDeferida().toString());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.COMPONENTE_ESTAGIO.name(), obj.getGradeCurricularEstagioVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.CONCEDENTE.name(), obj.getConcedente());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), obj.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), obj.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), obj.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void enviarMensagemAtivacaoPreMatricula(MatriculaVO matricula, String ano , String semestre, PersonalizacaoMensagemAutomaticaVO mensagemTemplate,UsuarioVO usuario) throws Exception {
		
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.NENHUM);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAtivacaoPreMatricula(matricula.getAluno().getNome(),matricula.getCurso().getNome(), ano, semestre, mensagemTemplate.getMensagem()));			
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemAtivacaoPreMatricula(matricula.getAluno().getNome(),matricula.getCurso().getNome(), ano , semestre, mensagemTemplate.getMensagemSMS()));
				comunicacao.setEnviarSMS(Boolean.TRUE);
			}
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(matricula.getAluno(), mensagemTemplate.getEnviarCopiaPais()));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuario, config,null);		
	}
	
	
	public String obterMensagemFormatadaMensagemAtivacaoPreMatricula(String nomeAluno, String nomeCurso, String ano , String semestre , final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), nomeAluno);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), nomeCurso);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.ANO_MATRICULA_PERIODO.name(), ano);
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SEMESTRE_MATRICULA_PERIODO.name(),semestre);
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	@Override
	public void enviarMensagemCancelamentoPreMatricula(MatriculaVO mat, MatriculaPeriodoVO matPer, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CANCELAMENTO_PREMATRICULA, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(mat.getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemCancelamentoPreMatricula(mat, matPer, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemCancelamentoPreMatricula(MatriculaVO mat, MatriculaPeriodoVO matPer, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replace(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), mat.getAluno().getNome());
		mensagemTexto = mensagemTexto.replace(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), mat.getCurso().getNome());
		mensagemTexto = mensagemTexto.replace(TagsMensagemAutomaticaEnum.ANO_MATRICULA_PERIODO.name(), matPer.getAno());
		mensagemTexto = mensagemTexto.replace(TagsMensagemAutomaticaEnum.SEMESTRE_MATRICULA_PERIODO.name(), matPer.getSemestre());
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemAnaliseEstagioObrigatorioRelatorioFinal(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_AVISO_PRAZO_ANALISE_RELATORIO_FINAL, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(estagioVO.getGrupoPessoaItemVO().getPessoaVO());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemAnaliseEstagioObrigatorioRelatorioFinal(estagioVO, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemAnaliseEstagioObrigatorioRelatorioFinal(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_AVALIADOR.name(), estagioVO.getGrupoPessoaItemVO().getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE_ANALISE.name(), estagioVO.getDataLimiteCorrecao_Apresentar());		
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), estagioVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), estagioVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), estagioVO.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemPeriodoEntregaCorrecaoEquivalenciaEncerrado(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_SOLICITAR_CORRECAO_EQUIVALENCIA, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(estagioVO.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemPeriodoEntregaCorrecaoEquivalenciaEncerrado(estagioVO, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemPeriodoEntregaCorrecaoEquivalenciaEncerrado(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_AVALIADOR.name(), estagioVO.getGrupoPessoaItemVO().getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE_ANALISE.name(), estagioVO.getDataLimiteCorrecao_Apresentar());
//		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), estagioVO.getMatriculaVO().getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), estagioVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), estagioVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), estagioVO.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemPeriodoEntregaCorrecaoAproveitamentoEncerrado(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_SOLICITAR_CORRECAO_APROVEITAMENTO, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(estagioVO.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemPeriodoEntregaCorrecaoAproveitamentoEncerrado(estagioVO, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemPeriodoEntregaCorrecaoAproveitamentoEncerrado(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_AVALIADOR.name(), estagioVO.getGrupoPessoaItemVO().getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE_ANALISE.name(), estagioVO.getDataLimiteCorrecao_Apresentar());
//		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), estagioVO.getMatriculaVO().getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), estagioVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), estagioVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), estagioVO.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemPeriodoEntregaAnaliseAproveitamentoEncerrado(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_AVISO_PRAZO_ANALISE_APROVEITAMENTO, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(estagioVO.getGrupoPessoaItemVO().getPessoaVO());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemPeriodoAnaliseCorrecaoAproveitamentoEncerrado(estagioVO, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemPeriodoAnaliseCorrecaoAproveitamentoEncerrado(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_AVALIADOR.name(), estagioVO.getGrupoPessoaItemVO().getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE_ANALISE.name(), estagioVO.getDataLimiteCorrecao_Apresentar());
//		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), estagioVO.getMatriculaVO().getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), estagioVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), estagioVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), estagioVO.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemPeriodoEntregaAnaliseEquivalenciaEncerrado(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_AVISO_PRAZO_ANALISE_EQUIVALENCIA, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(estagioVO.getGrupoPessoaItemVO().getPessoaVO());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemPeriodoAnaliseCorrecaoAproveitamentoEncerrado(estagioVO, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
	
	
	public String obterMensagemFormatadaMensagemPeriodoAnaliseCorrecaoEquivalenciaEncerrado(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_AVALIADOR.name(), estagioVO.getGrupoPessoaItemVO().getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE_ANALISE.name(), estagioVO.getDataLimiteCorrecao_Apresentar());
//		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.TURMA.name(), estagioVO.getMatriculaVO().getTurma().getIdentificadorTurma());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), estagioVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), estagioVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), estagioVO.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemTempoAnaliseRelatorioFinalExcedido(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ESTAGIOOBRIGATORIO_AVISO_PRAZO_ANALISE_RELATORIO_FINAL, false, null);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(estagioVO.getMatriculaVO().getAluno());
			
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuarioVO = new UsuarioVO();
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemTempoAnaliseRelatorioFinalExcedido(estagioVO, configEstagio, mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}
		
	public String obterMensagemFormatadaMensagemTempoAnaliseRelatorioFinalExcedido(EstagioVO estagioVO, ConfiguracaoEstagioObrigatorioVO configEstagio, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_AVALIADOR.name(), estagioVO.getGrupoPessoaItemVO().getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_LIMITE_ANALISE.name(), estagioVO.getDataLimiteCorrecao_Apresentar());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), estagioVO.getMatriculaVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), estagioVO.getMatriculaVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), estagioVO.getMatriculaVO().getMatricula());
		return mensagemTexto;
		
	}
	public void executarEnvioMensagenAdvertenciaAluno(AdvertenciaVO advertenciaVO, UsuarioVO usuarioVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ADVERTENCIA_ALUNO, false, advertenciaVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), null);
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();

		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>();
			pessoaVOs.add(advertenciaVO.getMatriculaVO().getAluno());
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			String mensagemEditada = obterMensagemFormatadaMensagemAdvertenciaResponsavelAlunoEmail(advertenciaVO.getMatriculaVO().getAluno(), advertenciaVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemAdvertenciaResponsavelAlunoEmail(advertenciaVO.getMatriculaVO().getAluno(), advertenciaVO, mensagemTemplate.getMensagemSMS());

			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(pessoaVOs));
			comunicacaoEnviar.setTipoDestinatario("AL");
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuarioVO, config,null);

		}
	}
	
	@Override
	public void realizarNotificacaoAlunoEscolhaGrupoProjetoIntegracao(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, SalaAulaBlackboardVO salaAulaBlackboardVO, SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, UsuarioVO usuarioVO) throws Exception{
		Thread notificar =  new Thread(new RealizarNotificacaoAlunoEscolhaGrupoProjetoIntegracao(templateMensagemAutomaticaEnum, salaAulaBlackboardVO, salaAulaBlackboardPessoaVO, usuarioVO));
		notificar.start();
	}
	
	class RealizarNotificacaoAlunoEscolhaGrupoProjetoIntegracao implements Runnable{
	
		private TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum; 
		private SalaAulaBlackboardVO salaAulaBlackboardVO;
		private SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO; 
		private UsuarioVO usuarioVO;
			
		
		public RealizarNotificacaoAlunoEscolhaGrupoProjetoIntegracao(
				TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum,
				SalaAulaBlackboardVO salaAulaBlackboardVO, SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO,
				UsuarioVO usuarioVO) {
			super();
			this.templateMensagemAutomaticaEnum = templateMensagemAutomaticaEnum;
			this.salaAulaBlackboardVO = salaAulaBlackboardVO;
			this.salaAulaBlackboardPessoaVO = salaAulaBlackboardPessoaVO;
			this.usuarioVO = usuarioVO;
		}

		public void run(){
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate;
		try {
			mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(templateMensagemAutomaticaEnum, false, null, null);
		
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			List<PessoaVO> pessoaVOs = new ArrayList<PessoaVO>();
			pessoaVOs.add(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getPessoaVO());
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacaoEnviar.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.valueOf(templateMensagemAutomaticaEnum.name()));
			comunicacaoEnviar.setCodigoTipoOrigemComunicacaoInterna(salaAulaBlackboardVO.getCodigo());
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());

			String mensagemEditada = obterMensagemFormatadaMensagemEscolhaSaidaGrupoBlackboard(salaAulaBlackboardVO, salaAulaBlackboardPessoaVO, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemEscolhaSaidaGrupoBlackboard(salaAulaBlackboardVO, salaAulaBlackboardPessoaVO, mensagemTemplate.getMensagemSMS());

			comunicacaoEnviar.setMensagem(mensagemEditada);
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(pessoaVOs));
			comunicacaoEnviar.setTipoDestinatario("AL");
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuarioVO, config,null);

		}
		} catch (Exception e) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.NOTIFICACAO_ENSALAMENTO_BLACKBOARD, e);
		}
	}
	
	public String obterMensagemFormatadaMensagemEscolhaSaidaGrupoBlackboard(SalaAulaBlackboardVO salaAulaBlackboardVO, SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), salaAulaBlackboardVO.getDisciplinaVO().getAbreviatura()+" - "+salaAulaBlackboardVO.getDisciplinaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SALA_BLACKBOARD_GRUPO_ATUAL.name(), salaAulaBlackboardVO.getIdSalaAulaBlackboard() +" - "+  salaAulaBlackboardVO.getNomeGrupo());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), salaAulaBlackboardPessoaVO.getMatricula());
		return mensagemTexto;

	}

	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void enviarMensagemNotificacaoRegistroFormatura(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO,  UsuarioVO usuarioVO) throws Exception {
		
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_REGISTRO_FORMATURA, false, usuarioVO);
		
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			
			List<PessoaVO> listaPessoas = new ArrayList<PessoaVO>();
			listaPessoas.add(programacaoFormaturaAlunoVO.getMatricula().getAluno());		
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		
			
			ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacao.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.NENHUM);
			comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacao.setData(new Date());
			comunicacao.setCodigo(0);
			comunicacao.getComunicadoInternoDestinatarioVOs().clear();
			comunicacao.setAssunto(mensagemTemplate.getAssunto());
			comunicacao.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacao.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacao.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			comunicacao.setMensagem(obterMensagemFormatadaMensagemFormaturaMatricula(programacaoFormaturaAlunoVO.getMatricula(),  mensagemTemplate.getMensagem()));			
			comunicacao.setTipoDestinatario("AL");
			comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosPessoa(listaPessoas));
			comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
		}
	}

	public String obterMensagemFormatadaMensagemFormaturaMatricula(MatriculaVO mat ,final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), mat.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), mat.getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), mat.getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_TURNO.name(),  mat.getTurno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), mat.getUnidadeEnsino().getNome());
		
		return mensagemTexto;
		
	}
	
	@Override
	public void executarEnvioMensagemNotificacaoSupervisorRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_ENTREGA_RELATORIO_FACILITADOR, false, null);
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		ArrayList<PessoaVO> listaDestinatarios = new ArrayList<PessoaVO>();
		if ((mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica())) {
			relatorioFinalFacilitadorVO.setSupervisor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(relatorioFinalFacilitadorVO.getSupervisor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			listaDestinatarios.add(relatorioFinalFacilitadorVO.getSupervisor());
			listaDestinatarios.add(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno());
			for(PessoaVO pessoaVO : listaDestinatarios) {
				ComunicacaoInternaVO comunicacao = inicializarDadosPadrao(new ComunicacaoInternaVO());
				comunicacao.setTipoOrigemComunicacaoInternaEnum(null);
				comunicacao.setCodigo(0);
				comunicacao.getComunicadoInternoDestinatarioVOs().clear();
				comunicacao.setAssunto(mensagemTemplate.getAssunto());
				comunicacao.setPersonalizacaoMensagemAutomaticaVO(mensagemTemplate);
				comunicacao.setEnviarEmail(mensagemTemplate.getEnviarEmail());
				comunicacao.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
				comunicacao.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
				comunicacao.setData(new Date());
				comunicacao.setTipoDestinatario("AL");
				comunicacao.setCodigo(0);
				comunicacao.setMensagem(obterMensagemFormatadaMensagemRelatorioFacilitador(relatorioFinalFacilitadorVO, "", mensagemTemplate.getMensagem()));
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacao.setMensagemSMS(obterMensagemFormatadaMensagemRelatorioFacilitador(relatorioFinalFacilitadorVO, "", mensagemTemplate.getMensagemSMS()));
					comunicacao.setEnviarSMS(Boolean.TRUE);
				}
				comunicacao.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoaVO, mensagemTemplate.getEnviarCopiaPais()));
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacao, false, usuarioVO, config,null);
			}
		}
	}
	
	@Override
	public void executarEnvioMensagemSituacaoRelatorioFacilitador(List<RelatorioFinalFacilitadorVO> listaRelatorioFacilitadorVOs, String situacao, UsuarioVO usuarioVO) throws Exception {
		TemplateMensagemAutomaticaEnum template ;
		 switch (situacao) {
	        case "DE":
	        	template = TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_DEFERIMENTO_RELATORIO_FACILITADOR;
	            break;
	        case "IN":
	        	template = TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_INDEFERIMENTO_RELATORIO_FACILITADOR;
	            break;
	        case "SB":
	        	template = TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_SUSPENSAO_BOLSA_RELATORIO_FACILITADOR;
	            break;
	        default:
	            template = TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CORRECAO_RELATORIO_FACILITADOR;
	            break;
	    }
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(template, false, usuarioVO);
		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			comunicacaoEnviar.setTipoOrigemComunicacaoInternaEnum(null);
			comunicacaoEnviar.setCodigo(0);
			comunicacaoEnviar.getComunicadoInternoDestinatarioVOs().clear();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setPersonalizacaoMensagemAutomaticaVO(mensagemTemplate);
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setTipoDestinatario("AL");
			for (RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO : listaRelatorioFacilitadorVOs) {		
				String mensagemEditada = obterMensagemFormatadaMensagemRelatorioFacilitador(relatorioFinalFacilitadorVO, situacao, mensagemTemplate.getMensagem());
				String mensagemSMSEditada = obterMensagemFormatadaMensagemRelatorioFacilitador(relatorioFinalFacilitadorVO, situacao, mensagemTemplate.getMensagemSMS());
				comunicacaoEnviar.setMensagem(mensagemEditada);
				if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
					comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
					comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
				}
				comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno(), mensagemTemplate.getEnviarCopiaPais()));
				getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuarioVO, config,null);
			}
		}
	}
	
	@Override
	public void executarEnvioMensagemTrancamentoMatricula(final PessoaVO pessoa, MatriculaVO matricula, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_TRANCAMENTO_MATRICULA, false, matricula.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemCancelamentoMatricula(pessoa, matricula, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemCancelamentoMatricula(pessoa, matricula, mensagemTemplate.getMensagemSMS());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(pessoa.getTipoPessoa());
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}

	}
	
	@Override
	public void executarEnvioMensagemJubilamentoMatricula(final PessoaVO pessoa, MatriculaVO matricula, UsuarioVO usuario) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_JUBILAMENTO_MATRICULA, false, matricula.getUnidadeEnsino().getCodigo(), usuario);
		if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
			ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
			comunicacaoEnviar.setCaminhoImagemPadraoCima(mensagemTemplate.getCaminhoImagemPadraoCima());
			comunicacaoEnviar.setCaminhoImagemPadraoBaixo(mensagemTemplate.getCaminhoImagemPadraoBaixo());
			comunicacaoEnviar.setCopiaFollowUp(mensagemTemplate.getCopiaFollowUp());
			String mensagemEditada = obterMensagemFormatadaMensagemCancelamentoMatricula(pessoa, matricula, mensagemTemplate.getMensagem());
			String mensagemSMSEditada = obterMensagemFormatadaMensagemCancelamentoMatricula(pessoa, matricula, mensagemTemplate.getMensagemSMS());
			if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
				comunicacaoEnviar.setMensagemSMS(mensagemSMSEditada);
				comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
			}
			comunicacaoEnviar.setMensagem(mensagemEditada);
			comunicacaoEnviar.setResponsavel(config.getResponsavelPadraoComunicadoInterno());
			comunicacaoEnviar.setData(new Date());
			comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatarios(pessoa, mensagemTemplate.getEnviarCopiaPais()));
			comunicacaoEnviar.setTipoDestinatario(pessoa.getTipoPessoa());
			comunicacaoEnviar.setEnviarEmail(mensagemTemplate.getEnviarEmail());
			comunicacaoEnviar.setEnviarEmailInstitucional(mensagemTemplate.getEnviarEmailInstitucional());
			getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config,null);
		}

	}

	@Override
	public String obterMensagemFormatadaMensagemRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, String situacao, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_DISCIPLINA.name(), relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getDisciplina().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.REGISTRO_ACADEMICO.name(), relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getRegistroAcademico());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getCurso().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().getNome());
		if(Uteis.isAtributoPreenchido(situacao)) {
			if(situacao.equals(SituacaoRelatorioFacilitadorEnum.DEFERIDO_SUPERVISOR.getKey())) {
				mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOTA_INFORMADA.name(), relatorioFinalFacilitadorVO.getNotaApresentar());
			} 
			if(situacao.equals(SituacaoRelatorioFacilitadorEnum.CORRECAO_ALUNO.getKey())) {
				mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MOTIVO_CORRECAO.name(), relatorioFinalFacilitadorVO.getMotivo());
			}
			if(situacao.equals(SituacaoRelatorioFacilitadorEnum.INDEFERIDO_SUPERVISOR.getKey())) {
				mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MOTIVO_INDEFERIMENTO.name(), relatorioFinalFacilitadorVO.getMotivo());
			}
			if(situacao.equals(SituacaoRelatorioFacilitadorEnum.SUSPENSAO_BOLSA.getKey())) {
				mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MOTIVO_SUSPENSAO.name(), relatorioFinalFacilitadorVO.getMotivo());
			}
		}
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SUPERVISOR.name(), relatorioFinalFacilitadorVO.getSupervisor().getNome());
		return mensagemTexto;
	}
	

}
