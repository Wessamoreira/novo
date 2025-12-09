package negocio.facade.jdbc.moodle;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.moodle.OperacaoMoodleVO;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoOperacaoMoodleEnum;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.moodle.OperacaoMoodleInterfaceFacade;
import webservice.AlwaysListTypeAdapterFactory;
import webservice.moodle.MensagensItemRSVO;
import webservice.moodle.MensagensRSVO;
import webservice.moodle.NotasItemRSVO;
import webservice.moodle.NotasRSVO;

@SuppressWarnings("serial")
@Repository
@Scope("singleton")
@Lazy
public class OperacaoMoodle extends ControleAcesso implements OperacaoMoodleInterfaceFacade {

	private static String CONSULTAR_PROCESSAMENTO_PENDENTE = "PENDENTE";
	private static String CONSULTAR_PROCESSAMENTO_ERRO = "ERRO";

	@Override
	public void incluirOperacaoMoodleBaseMensagensRSVO(MensagensRSVO mensagensRSVO, UsuarioVO usuario) {
		OperacaoMoodleVO operacaoMoodle = new OperacaoMoodleVO();
		operacaoMoodle.setJsonMoodle(Uteis.converterObjetoParaJson(mensagensRSVO));
		operacaoMoodle.setProcessado(Boolean.FALSE);
		operacaoMoodle.setTipoOperacao(TipoOperacaoMoodleEnum.MENSAGENS);
		incluir(operacaoMoodle, "operacaoMoodle", new AtributoPersistencia().add("jsonMoodle", operacaoMoodle.getJsonMoodle()).add("processado", operacaoMoodle.getProcessado()).add("tipoOperacao", operacaoMoodle.getTipoOperacao().name()), usuario);
	}

	@Override
	public void incluirOperacaoMoodleBaseNotasRSVO(NotasRSVO notasRSVO, UsuarioVO usuario) {
		OperacaoMoodleVO operacaoMoodle = new OperacaoMoodleVO();
		operacaoMoodle.setJsonMoodle(Uteis.converterObjetoParaJson(notasRSVO));
		operacaoMoodle.setProcessado(Boolean.FALSE);
		operacaoMoodle.setTipoOperacao(TipoOperacaoMoodleEnum.NOTAS);
		incluir(operacaoMoodle, "operacaoMoodle", new AtributoPersistencia().add("jsonMoodle", operacaoMoodle.getJsonMoodle()).add("processado", operacaoMoodle.getProcessado()).add("tipoOperacao", operacaoMoodle.getTipoOperacao().name()), usuario);
	}

	private OperacaoMoodleVO montarDados(SqlRowSet tabelaResultado) {
		OperacaoMoodleVO operacaoMoodle = new OperacaoMoodleVO();
		operacaoMoodle.setCodigo(tabelaResultado.getInt("codigo"));
		operacaoMoodle.setProcessado(tabelaResultado.getBoolean("processado"));
		operacaoMoodle.setJsonMoodle(tabelaResultado.getString("jsonMoodle"));
		operacaoMoodle.setTipoOperacao(Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoOperacao")) ? TipoOperacaoMoodleEnum.valueOf(tabelaResultado.getString("tipoOperacao")) : null);
		operacaoMoodle.setErro(tabelaResultado.getString("erro"));
		operacaoMoodle.setCreated(tabelaResultado.getDate("created"));
		return operacaoMoodle;
	}

	private List<OperacaoMoodleVO> mondarDadosConsulta(SqlRowSet tabelaResultado) {
		List<OperacaoMoodleVO> operacaoMoodleVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			operacaoMoodleVOs.add(montarDados(tabelaResultado));
		}
		return operacaoMoodleVOs;
	}

	private StringBuilder getSelectConsultaPadrao() {
		return new StringBuilder("SELECT count(*) over() as qtde_total_registros, codigo, processado, jsonMoodle, tipoOperacao, erro, created FROM operacaoMoodle ");
	}

	/**
	 * consulta que retorna a lista de operações que não foram processadas no
	 * momento, passando como parâmetro qual a operação específica
	 * 
	 * @author Felipi Alves
	 */
	private List<OperacaoMoodleVO> consultarOperacoesMoodleProcessar(TipoOperacaoMoodleEnum tipoOperacaoMoodle) {
		StringBuilder sql = new StringBuilder(getSelectConsultaPadrao());
		sql.append("WHERE processado = FALSE ");
		if (Uteis.isAtributoPreenchido(tipoOperacaoMoodle)) {
			sql.append("AND tipoOperacao = '").append(tipoOperacaoMoodle.name()).append("' ");
		}
		sql.append("ORDER BY codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return mondarDadosConsulta(tabelaResultado);
	}

	private void consultarOperacoesMoodle(DataModelo dataModelo, TipoOperacaoMoodleEnum tipoOperacaoMoodle, String tipoConsulta) {
		StringBuilder sql = new StringBuilder(getSelectConsultaPadrao());
		sql.append("WHERE 1 = 1 ");
		if (Uteis.isAtributoPreenchido(tipoOperacaoMoodle)) {
			sql.append("AND tipoOperacao = '").append(tipoOperacaoMoodle.name()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(tipoConsulta)) {
			sql.append("AND " + (Objects.equals(tipoConsulta, CONSULTAR_PROCESSAMENTO_PENDENTE) ? "processado = FALSE" : "processado = TRUE")).append(" ");
		}
		sql.append("ORDER BY codigo DESC ");
		sql.append("LIMIT ").append(dataModelo.getLimitePorPagina()).append(" OFFSET ").append(dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (dataModelo != null && tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		dataModelo.setListaConsulta(mondarDadosConsulta(tabelaResultado));
	}

	@Override
	public void carregarDadosOperacoesMoodle(TipoOperacaoMoodleEnum tipoOperacaoMoodle, DataModelo dataModeloOperacaoMoodleProcessamentoPendente, DataModelo dataModeloOperacaoMoodleProcessamentoErro) {
		try {
			carregarDadosOperacoesMoodle(tipoOperacaoMoodle, dataModeloOperacaoMoodleProcessamentoPendente, CONSULTAR_PROCESSAMENTO_PENDENTE);
			carregarDadosOperacoesMoodle(tipoOperacaoMoodle, dataModeloOperacaoMoodleProcessamentoErro, CONSULTAR_PROCESSAMENTO_ERRO);
		} finally {
			if (Objects.nonNull(tipoOperacaoMoodle)) {
				tipoOperacaoMoodle = null;
			}
			if (Objects.nonNull(dataModeloOperacaoMoodleProcessamentoPendente)) {
				dataModeloOperacaoMoodleProcessamentoPendente = null;
			}
			if (Objects.nonNull(dataModeloOperacaoMoodleProcessamentoErro)) {
				dataModeloOperacaoMoodleProcessamentoErro = null;
			}
		}
	}

	@Override
	public void carregarDadosOperacoesMoodle(TipoOperacaoMoodleEnum tipoOperacaoMoodle, DataModelo dataModelo, String tipoConsulta) {
		try {
			if (Uteis.isAtributoPreenchido(tipoOperacaoMoodle) && Objects.nonNull(dataModelo)) {
				dataModelo.getListaConsulta().clear();
				dataModelo.setTotalRegistrosEncontrados(0);
				dataModelo.setLimitePorPagina(10);
				consultarOperacoesMoodle(dataModelo, tipoOperacaoMoodle, tipoConsulta);
			}
		} finally {
			if (Objects.nonNull(tipoOperacaoMoodle)) {
				tipoOperacaoMoodle = null;
			}
			if (Objects.nonNull(dataModelo)) {
				dataModelo = null;
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarErroOperacaoMoodle(OperacaoMoodleVO operacaoMoodle, UsuarioVO usuario) {
		if (Uteis.isAtributoPreenchido(operacaoMoodle)) {
			alterar(operacaoMoodle, "operacaoMoodle", new AtributoPersistencia().add("erro", operacaoMoodle.getErro()).add("processado", Boolean.TRUE), new AtributoPersistencia().add("codigo", operacaoMoodle.getCodigo()), usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void excluirOperacaoMoodle(OperacaoMoodleVO operacaoMoodle, UsuarioVO usuario) {
		if (Uteis.isAtributoPreenchido(operacaoMoodle)) {
			excluir("operacaomoodle", new AtributoPersistencia().add("codigo", operacaoMoodle.getCodigo()), usuario);
		}
	}

	private void validarDadosMensagemMoodle(MensagensItemRSVO mensagensItemRSVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(mensagensItemRSVO.getAssunto()), "Deve ser informado o campo Assunto.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(mensagensItemRSVO.getMensagem()), "Deve ser informado o campo Mensagem.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(mensagensItemRSVO.getEmailRemetente()), "Deve ser informado o campo Email Remetente.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(mensagensItemRSVO.getEmailDestinatarios()), "Deve ser informado o campo Email Destinatários.");
		Uteis.checkState(mensagensItemRSVO.getEnviarSms() && !Uteis.isAtributoPreenchido(mensagensItemRSVO.getSms()), "Deve ser informado o campo Mensagem SMS.");
	}

	/**
	 * método que retorna o {@link ComunicacaoInternaVO} com a configuração
	 * utilizada para as mensagens recebidas do moodle
	 * 
	 * @author Felipi Alves
	 */
	private ComunicacaoInternaVO inicializarDadosComunicacaoInternaPadrao(ComunicacaoInternaVO comunicacaoEnviar) {
		comunicacaoEnviar.setEnviarEmail(Boolean.TRUE);
		comunicacaoEnviar.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		comunicacaoEnviar.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
		comunicacaoEnviar.setTipoMarketing(Boolean.FALSE);
		comunicacaoEnviar.setTipoLeituraObrigatoria(Boolean.FALSE);
		comunicacaoEnviar.setDigitarMensagem(Boolean.TRUE);
		return comunicacaoEnviar;
	}

	/**
	 * método que retorna os destinatários encontrados de acordo com a lista de
	 * e-mails recebidos do moodle e do tipo de pessoa informado
	 *
	 * @author Felipi Alves
	 */
	private List<PessoaVO> obterListaPessoaDestinatariosComunicacaoInterna(List<String> emailsLocalizados, String tipoPessoa, UsuarioVO usuario) throws Exception {
		List<PessoaVO> listDestinatario = new ArrayList<>();
		if (Uteis.isAtributoPreenchido(emailsLocalizados)) {
			List<PessoaVO> pessoaVOs = getFacadeFactory().getPessoaFacade().consultarDestinatariosMensagemMoodlePorEmail(emailsLocalizados, tipoPessoa);
			if (Uteis.isAtributoPreenchido(pessoaVOs)) {
				listDestinatario.addAll(pessoaVOs);
			}
		}
		return listDestinatario;
	}

	/**
	 * método que retorna a lista de {@link ComunicadoInternoDestinatarioVO} com a
	 * configuração padrão de mensagem recebida pelo moodle
	 *
	 * @author Felipi Alves
	 */
	private List<ComunicadoInternoDestinatarioVO> obterListaDestinatariosComunicacaoInterna(List<PessoaVO> pessoaVOs, UsuarioVO usuario) {
		List<ComunicadoInternoDestinatarioVO> listDestinatario = new ArrayList<>();
		if (Uteis.isAtributoPreenchido(pessoaVOs)) {
			for (PessoaVO pessoaVO : pessoaVOs) {
				ComunicadoInternoDestinatarioVO destinatario = new ComunicadoInternoDestinatarioVO();
				destinatario.setCiJaLida(Boolean.FALSE);
				destinatario.setDestinatario(pessoaVO);
				destinatario.setEmail(pessoaVO.getEmail());
				destinatario.setNome(pessoaVO.getNome());
				destinatario.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
				listDestinatario.add(destinatario);
			}
		}
		return listDestinatario;
	}

	/**
	 * método que valida e retorna o remetente responsável pelo comunicado interno
	 * recebido do moodle
	 *
	 * @author Felipi Alves
	 */
	private PessoaVO obterPessoaPorEmailComunicacaoInterna(String email, RegistroExecucaoJobVO registroExecucaoJob, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(registroExecucaoJob.getMapResponsavelComunicadoInterno()) && registroExecucaoJob.getMapResponsavelComunicadoInterno().containsKey(email.trim())) {
			return registroExecucaoJob.getMapResponsavelComunicadoInterno().get(email.trim());
		} else {
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorEmail(email, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			Uteis.checkState(!Uteis.isAtributoPreenchido(pessoaVO), "Não foi possível localizar o remetente pelo email: " + email);
			registroExecucaoJob.getMapResponsavelComunicadoInterno().put(email.trim(), pessoaVO);
			return pessoaVO;
		}
	}

	/**
	 * método responsável pela criação de um comunicado interno a partir de um
	 * determinado tipo de pessoa, o sistema vai consultar os destinatários de
	 * acordo com o tipo de pessoa e caso encontre será criado o comunicado interno
	 * com o tipo de pessoa descrito
	 * 
	 * @author Felipi Alves
	 */
	private void realizarEnvioMensagemMoodle(List<String> emailsLocalizados, MensagensItemRSVO mensagensItemRSVO, MensagensRSVO mensagensRSVO, String tipoPessoa, RegistroExecucaoJobVO registroExecucaoJob, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(emailsLocalizados) && Objects.nonNull(mensagensRSVO)) {
			ComunicacaoInternaVO comunicacaoEnviar = null;
			List<PessoaVO> pessoaVOs = null;
			try {
				comunicacaoEnviar = inicializarDadosComunicacaoInternaPadrao(new ComunicacaoInternaVO());
				comunicacaoEnviar.setTipoDestinatario(tipoPessoa);
				pessoaVOs = obterListaPessoaDestinatariosComunicacaoInterna(emailsLocalizados, tipoPessoa, usuario);
				if (Uteis.isAtributoPreenchido(pessoaVOs)) {
					comunicacaoEnviar.setComunicadoInternoDestinatarioVOs(obterListaDestinatariosComunicacaoInterna(pessoaVOs, usuario));
					comunicacaoEnviar.setAssunto(mensagensItemRSVO.getAssunto());
					comunicacaoEnviar.setMensagem(mensagensItemRSVO.getMensagem());
					comunicacaoEnviar.setEnviarEmail(mensagensItemRSVO.getEnviarPorEmail());
					comunicacaoEnviar.setEnviarSMS(mensagensItemRSVO.getEnviarSms());
					comunicacaoEnviar.setMensagemSMS(mensagensItemRSVO.getSms());
					comunicacaoEnviar.setResponsavel(obterPessoaPorEmailComunicacaoInterna(mensagensItemRSVO.getEmailRemetente(), registroExecucaoJob, usuario));
					comunicacaoEnviar.setProfessor(comunicacaoEnviar.getResponsavel());
					comunicacaoEnviar.setData(new Date());
					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoEnviar, false, usuario, config, null);
					RegistroExecucaoJobVO.incrementarTotalSucesso(registroExecucaoJob, pessoaVOs.size());
				}
			} catch (Exception e) {
				if (Uteis.isAtributoPreenchido(pessoaVOs)) {
					RegistroExecucaoJobVO.incrementarTotalErro(registroExecucaoJob, pessoaVOs.size());
				}
				throw e;
			} finally {
				if (Objects.nonNull(comunicacaoEnviar)) {
					removerObjetoMemoria(comunicacaoEnviar);
					comunicacaoEnviar = null;
				}
				if (Objects.nonNull(pessoaVOs)) {
					pessoaVOs.clear();
					pessoaVOs = null;
				}
				if (Objects.nonNull(emailsLocalizados)) {
					emailsLocalizados = null;
				}
				if (Objects.nonNull(mensagensItemRSVO)) {
					mensagensItemRSVO = null;
				}
				if (Objects.nonNull(mensagensRSVO)) {
					mensagensRSVO = null;
				}
				if (Objects.nonNull(tipoPessoa)) {
					tipoPessoa = null;
				}
				if (Objects.nonNull(config)) {
					config = null;
				}
				if (Objects.nonNull(usuario)) {
					usuario = null;
				}
			}
		}
	}

	private boolean executarLancamentoNotaFrequenciaHistorico(NotasItemRSVO notasItemRSVO, HistoricoVO historico) throws Exception {
		boolean notaLancada = Uteis.isAtributoPreenchido(notasItemRSVO.getVariavelNota());
		if (notaLancada) {
			if (notasItemRSVO.getNota() == null) {
				notasItemRSVO.setNota(0.0);
			}
			TipoNotaConceitoEnum tipoNotaAlterar = historico.getConfiguracaoAcademico().getNumeroNotaPorVariavel(notasItemRSVO.getVariavelNota());
			notaLancada = getFacadeFactory().getHistoricoFacade().realizarLancamentoNotaHistoricoAutomaticamente(tipoNotaAlterar, historico, notasItemRSVO.getNota(), false, null, historico.getMatriculaPeriodoTurmaDisciplina(), false, false, null);
			if (!notaLancada) {
				throw new Exception("A variável " + notasItemRSVO.getVariavelNota() + " não foi encontrada na Configuração Acadêmico: " + historico.getConfiguracaoAcademico().getNome() + " do histórico do aluno da matrícula " + historico.getMatricula().getMatricula() + ".");
			}
		} else {
			historico.setNota1(notasItemRSVO.getNota());
			notaLancada = true;
		}
		return notaLancada;
	}

	public static MensagensRSVO converterJsonParaObjetoMensagensRSVO(String json) {
		if (!Uteis.isAtributoPreenchido(json)) {
			return new MensagensRSVO();
		}
		Gson gson = null;
		Type type = null;
		try {
			gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>()).create();
			type = new TypeToken<MensagensRSVO>() {
			}.getType();
			return gson.fromJson(json, type);
		} finally {
			if (Objects.nonNull(gson)) {
				gson = null;
			}
			if (Objects.nonNull(type)) {
				type = null;
			}
		}
	}

	public static NotasRSVO converterJsonParaObjetoNotasRSVO(String json) {
		if (!Uteis.isAtributoPreenchido(json)) {
			return new NotasRSVO();
		}
		Gson gson = null;
		Type type = null;
		try {
			gson = new GsonBuilder().setDateFormat("MM-dd-yyyy HH:mm:ss").registerTypeAdapterFactory(new AlwaysListTypeAdapterFactory<>()).create();
			type = new TypeToken<NotasRSVO>() {
			}.getType();
			return gson.fromJson(json, type);
		} finally {
			if (Objects.nonNull(gson)) {
				gson = null;
			}
			if (Objects.nonNull(type)) {
				type = null;
			}
		}
	}

	/**
	 * metodo responsavel por realizar o processamento de uma mensagem recebida do
	 * moodle, pegando o json recebido do moodle e realizando a consulta dos
	 * destinatários para o comunicado interno e criando o comunicado interno para
	 * eles, ao finalizar o processamento e caso não ocorra nenhum erro vai ser
	 * alterado no banco de dados que foi processado com sucesso
	 * 
	 * @author Felipi Alves
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarProcessamentoOperacaoEnvioMensagemMoodle(OperacaoMoodleVO operacaoMoodle, RegistroExecucaoJobVO registroExecucaoJob, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(operacaoMoodle) && operacaoMoodle.isTipoOperacaoEnvioMensagens()) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(operacaoMoodle.getJsonMoodle()), "O json recebido do moodle de envio de mensagem deve ser informado");
			MensagensRSVO mensagensRSVO = null;
			try {
				mensagensRSVO = converterJsonParaObjetoMensagensRSVO(operacaoMoodle.getJsonMoodle());
				mensagensRSVO.inicializarDadosRetornoEnvioMensagens();
				for (MensagensItemRSVO mensagensItemRSVO : mensagensRSVO.getMensagens()) {
					validarDadosMensagemMoodle(mensagensItemRSVO);
					List<String> emailsLocalizados = new ArrayList<>(Arrays.asList(mensagensItemRSVO.getEmailDestinatarios().replace(" ", "").split(",")));
					if (Uteis.isAtributoPreenchido(emailsLocalizados)) {
						RegistroExecucaoJobVO.incrementarTotal(registroExecucaoJob, emailsLocalizados.size());
						realizarEnvioMensagemMoodle(emailsLocalizados, mensagensItemRSVO, mensagensRSVO, "AL", registroExecucaoJob, config, usuario);
//						realizarEnvioMensagemMoodle(emailsLocalizados, mensagensItemRSVO, mensagensRSVO, "PR", registroExecucaoJob, config, usuario);
//						realizarEnvioMensagemMoodle(emailsLocalizados, mensagensItemRSVO, mensagensRSVO, "FU", registroExecucaoJob, config, usuario);
						if (Uteis.isAtributoPreenchido(emailsLocalizados)) {
							RegistroExecucaoJobVO.incrementarErro(registroExecucaoJob, emailsLocalizados.stream().map(email -> "e-mail não encontrado " + email).collect(Collectors.joining(", ")));
							RegistroExecucaoJobVO.incrementarTotalErro(registroExecucaoJob, emailsLocalizados.size());
						}
					}
				}
				operacaoMoodle.setProcessado(Boolean.TRUE);
				excluirOperacaoMoodle(operacaoMoodle, usuario);
			} finally {
				if (Objects.nonNull(mensagensRSVO)) {
					removerObjetoMemoria(mensagensRSVO);
					mensagensRSVO = null;
				}
				if (Objects.nonNull(config)) {
					config = null;
				}
				if (Objects.nonNull(usuario)) {
					usuario = null;
				}
				if (Objects.nonNull(operacaoMoodle)) {
					operacaoMoodle = null;
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarProcessamentoOperacaoInserirNotas(OperacaoMoodleVO operacaoMoodle, RegistroExecucaoJobVO registroExecucaoJob, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(operacaoMoodle) && operacaoMoodle.isTipoOperacaoEnvioNotas()) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(operacaoMoodle.getJsonMoodle()), "O json recebido do moodle de inserir notas deve ser informado");
			NotasRSVO notasRSVO = null;
			List<NotasItemRSVO> notasInseridas = new ArrayList<>(0);
			List<HistoricoVO> listaHistorico = new ArrayList<>();
			List<String> processado = new ArrayList<String>(0);
			List<String> listaErro = new ArrayList<>(0);
			try {
				notasRSVO = converterJsonParaObjetoNotasRSVO(operacaoMoodle.getJsonMoodle());
				if (Uteis.isAtributoPreenchido(notasRSVO.getNotas())) {
					RegistroExecucaoJobVO.incrementarTotal(registroExecucaoJob, notasRSVO.getNotas().size());
					for (NotasItemRSVO nota : notasRSVO.getNotas()) {
						List<HistoricoVO> historicos = null;
						String hash = null;
						try {
							historicos = getFacadeFactory().getHistoricoFacade().consultarCodigoNotaFrequenciaPorParametros(nota.getCpf(), nota.getEmail(), nota.getCursoCodigo(), nota.getTurmaCodigo(), nota.getModuloCodigo(), nota.getAno(), nota.getSemestre(), nota.getBimestre());
							if (!Uteis.isAtributoPreenchido(historicos)) {
								listaErro.add("Histórico não encontrado" + ", disciplina: " + nota.getModuloCodigo() + ", e-mail: " + nota.getEmail());
								continue;
							}
							for (HistoricoVO historico : historicos) {
								if (Uteis.isAtributoPreenchido(historico) && executarLancamentoNotaFrequenciaHistorico(nota, historico)) {
									listaHistorico.add(historico);
									notasInseridas.add(nota);
									if (nota.getCalcularMedia()) {
										hash = historico.getMatricula().getMatricula() + "-A" + historico.getAnoHistorico() + "-S" + historico.getSemestreHistorico() + "-D" + historico.getDisciplina().getCodigo();
										if (!processado.contains(hash)) {
											processado.add(hash);
											getFacadeFactory().getHistoricoFacade().calcularMedia(historico, null, historico.getConfiguracaoAcademico(), 0, TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS, true, usuario);
										}
									}
								}
							}
						} catch (Exception e) {
							listaErro.add(e.getMessage());
						} finally {
							if (Objects.nonNull(historicos)) {
								Uteis.liberarListaMemoria(historicos);
								historicos = null;
							}
							if (Objects.nonNull(hash)) {
								hash = null;
							}
						}
					}
				}
				if (!Uteis.isAtributoPreenchido(notasInseridas) && Uteis.isAtributoPreenchido(listaErro)) {
					String mensagemErro = listaErro.stream().collect(Collectors.joining("\n"));
					registroExecucaoJob.setErro((Uteis.isAtributoPreenchido(registroExecucaoJob.getErro()) ? registroExecucaoJob.getErro() + "\n" : Constantes.EMPTY) + (mensagemErro));
					RegistroExecucaoJobVO.incrementarTotalErro(registroExecucaoJob, listaErro.size());
					throw new Exception(mensagemErro);
				}
				if (Uteis.isAtributoPreenchido(notasInseridas)) {
					getFacadeFactory().getHistoricoFacade().incluirListaHistorico(listaHistorico, null, usuario, "WS REST Moodle", true, TipoAlteracaoSituacaoHistoricoEnum.TODOS_HISTORICOS);
					RegistroExecucaoJobVO.incrementarTotalSucesso(registroExecucaoJob, notasInseridas.size());
				}
				if (Uteis.isAtributoPreenchido(listaErro)) {
					String mensagemErro = listaErro.stream().collect(Collectors.joining("\n"));
					registroExecucaoJob.setErro((Uteis.isAtributoPreenchido(registroExecucaoJob.getErro()) ? registroExecucaoJob.getErro() + "\n" : Constantes.EMPTY) + (mensagemErro));
					RegistroExecucaoJobVO.incrementarTotalErro(registroExecucaoJob, listaErro.size());
					operacaoMoodle.setErro(mensagemErro);
					alterarErroOperacaoMoodle(operacaoMoodle, usuario);
					return;
				}
				operacaoMoodle.setProcessado(Boolean.TRUE);
				excluirOperacaoMoodle(operacaoMoodle, usuario);
			} finally {
				if (Objects.nonNull(notasRSVO)) {
					notasRSVO = null;
				}
				if (Objects.nonNull(notasInseridas)) {
					Uteis.liberarListaMemoria(notasInseridas);
					notasInseridas = null;
				}
				if (Objects.nonNull(listaHistorico)) {
					Uteis.liberarListaMemoria(listaHistorico);
					listaHistorico = null;
				}
				if (Objects.nonNull(processado)) {
					Uteis.liberarListaMemoria(processado);
					processado = null;
				}
				if (Objects.nonNull(listaErro)) {
					Uteis.liberarListaMemoria(listaErro);
					listaErro = null;
				}
				if (Objects.nonNull(config)) {
					config = null;
				}
				if (Objects.nonNull(usuario)) {
					usuario = null;
				}
				if (Objects.nonNull(operacaoMoodle)) {
					operacaoMoodle = null;
				}
			}
		}
	}

	/**
	 * metodo responsavel por fazer a consulta das operações do moodle que não foram
	 * executadas ainda, sendo assim ao finalizar será marcado que foi processado e
	 * se ocorrer algum erro salvará no banco de dados o erro, a tabela no banco de
	 * dados utilizada para realizar as operações e o controle é a "operacaomoodle"
	 * 
	 * @author Felipi Alves
	 */
	@Override
	public void realizarOperacaoMoodle(TipoOperacaoMoodleEnum tipoOperacaoMoodle, RegistroExecucaoJobVO registroExecucao) throws Exception {
		List<OperacaoMoodleVO> operacaoMoodleVOs = consultarOperacoesMoodleProcessar(tipoOperacaoMoodle);
		while (!operacaoMoodleVOs.isEmpty()) {
			realizarOperacaoMoodle(operacaoMoodleVOs, registroExecucao);
			Uteis.liberarListaMemoria(operacaoMoodleVOs);
			operacaoMoodleVOs = consultarOperacoesMoodleProcessar(tipoOperacaoMoodle);
		}
	}

	public void realizarOperacaoMoodle(List<OperacaoMoodleVO> operacaoMoodleVOs, RegistroExecucaoJobVO registroExecucaoJob) throws Exception {
		if (Uteis.isAtributoPreenchido(operacaoMoodleVOs)) {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(config.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
			ProcessarParalelismo.executar(0, operacaoMoodleVOs.size(), 50, new ConsistirException(), new ProcessarParalelismo.Processo() {
				@Override
				public void run(int i) {
					OperacaoMoodleVO operacaoMoodle = operacaoMoodleVOs.get(i);
					if (Uteis.isAtributoPreenchido(operacaoMoodle)) {
						try {
							if (operacaoMoodle.isTipoOperacaoEnvioMensagens()) {
								getFacadeFactory().getOperacaoMoodleInterfaceFacade().realizarProcessamentoOperacaoEnvioMensagemMoodle(operacaoMoodle, registroExecucaoJob, config, usuario);
							} else if (operacaoMoodle.isTipoOperacaoEnvioNotas()) {
								getFacadeFactory().getOperacaoMoodleInterfaceFacade().realizarProcessamentoOperacaoInserirNotas(operacaoMoodle, registroExecucaoJob, config, usuario);
							}
						} catch (Exception e) {
							RegistroExecucaoJobVO.incrementarErro(registroExecucaoJob, Objects.isNull(e.getMessage()) ? "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado." : e.getMessage());
							RegistroExecucaoJobVO.incrementarTotalErro(registroExecucaoJob, 1);
							operacaoMoodle.setErro(Objects.isNull(e.getMessage()) ? "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado." : e.getMessage());
							alterarErroOperacaoMoodle(operacaoMoodle, usuario);
							registrarExceptionSentry(e, null, Boolean.TRUE, usuario);
						} finally {
							if (Objects.nonNull(operacaoMoodle)) {
								removerObjetoMemoria(operacaoMoodle);
								operacaoMoodle = null;
							}
						}
					}
				}
			});
		}
	}
}