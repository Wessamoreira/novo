package negocio.facade.jdbc.financeiro;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jobs.RegistrarSerasaApiGeo;
import jobs.RemoverSerasaApiGeo;
import jobs.enumeradores.JobsEnum;
import kong.unirest.HttpResponse;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.enumerador.IntegracaoNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.SituacaoParcelaRemoverSerasaApiGeo;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoContratoAgenteNegativacaoCobrancaEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemanaJob;
import negocio.comuns.utilitarias.dominios.Horas;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.AgenteNegativacaoCobrancaContaReceberInterfaceFacade;
import webservice.nfse.generic.AmbienteEnum;
import webservice.servicos.objetos.IntegracaoSerasaApiGeoRSVO;

@Repository
@Scope("singleton")
@Lazy
public class AgenteNegativacaoCobrancaContaReceber extends ControleAcesso implements AgenteNegativacaoCobrancaContaReceberInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public AgenteNegativacaoCobrancaContaReceber() throws Exception {
		super();
		setIdEntidade("AgenteNegativacaoCobrancaContaReceber");
	}

	public AgenteNegativacaoCobrancaContaReceberVO novo() throws Exception {
		AgenteNegativacaoCobrancaContaReceber.incluir(getIdEntidade());
		AgenteNegativacaoCobrancaContaReceberVO obj = new AgenteNegativacaoCobrancaContaReceberVO();
		return obj;
	}
	
	public void validarDados(AgenteNegativacaoCobrancaContaReceberVO agente) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(agente.getNome())) {
			throw new ConsistirException("O campo Nome (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(agente.getTipo())) {
			throw new ConsistirException("O campo Tipo (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
		}
		
		if (agente.getPossuiIntegracao() && !Uteis.isAtributoPreenchido(agente.getIntegracao())) {
			throw new ConsistirException("O campo Integração (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
		}
		Uteis.checkState(agente.getIntegracaoSerasaApiGeo() && !Uteis.isAtributoPreenchido(agente.getApiKeySerasaApiGeo()), "O campo Chave Integração  (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
		Uteis.checkState(agente.getIntegracaoSerasaApiGeo() && !Uteis.isAtributoPreenchido(agente.getSenhaApiSerasaApiGeo()), "O campo Senha Integração  (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
		Uteis.checkState(agente.getIntegracaoSerasaApiGeo() && !Uteis.isAtributoPreenchido(agente.getCredorUnidadeEnsinoVO()), "O campo Credor Unidade Ensino  (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
		Uteis.checkState(agente.getIntegracaoSerasaApiGeo() && !Uteis.isAtributoPreenchido(agente.getAgenteNegativacaoCobrancaUnidadeEnsinoVOs()), "O campo Unidade Ensino  (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
		Uteis.checkState(agente.getIntegracaoSerasaApiGeo() && agente.getRegistrarAutomaticamenteContasNegativacao()
				&& agente.getValidarDocumentosEntregue() && !Uteis.isAtributoPreenchido(agente.getTipoDocumentoPendenciaAgenteCobrancaVOs()), "O campo Tipo de Documentos  (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
		Uteis.checkState(agente.getIntegracaoSerasaApiGeo() && agente.getRegistrarAutomaticamenteContasNegativacao()
				&& agente.getDesconsiderarAlunoDisciplinaReprovadas() && !Uteis.isAtributoPreenchido(agente.getQuantidadeMinimaDisciplinaReprovada()), "O campo Quantidade de Disciplinas Reprovadas  (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
		Uteis.checkState(agente.getIntegracaoSerasaApiGeo() && agente.getRegistrarAutomaticamenteContasNegativacao() && !Uteis.isAtributoPreenchido(agente.getQuantidadeDiasConsiderarParcelaVencidaInicial()), "O campo Quantidade de dias retroativo Parcelas Vencidas Inicial  (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
		Uteis.checkState(agente.getIntegracaoSerasaApiGeo() && agente.getRegistrarAutomaticamenteContasNegativacao() && Uteis.isAtributoPreenchido(agente.getQuantidadeDiasConsiderarParcelaVencidaInicial()) && agente.getQuantidadeDiasConsiderarParcelaVencidaInicial() > 365, "O campo Quantidade de dias retroativo Parcelas Vencidas Inicial  (Agente Negativação/Cobrança Conta Receber) não pode ser mais que 365 dias.");
		Uteis.checkState(agente.getIntegracaoSerasaApiGeo() && agente.getRegistrarAutomaticamenteContasNegativacao() && !Uteis.isAtributoPreenchido(agente.getQuantidadeDiasConsiderarParcelaVencida()), "O campo Quantidade de dias retroativo Parcelas Vencidas Final  (Agente Negativação/Cobrança Conta Receber) deve ser informado.");		
		Uteis.checkState(agente.getIntegracaoSerasaApiGeo() && agente.getRemoverAutomaticamenteContasNegativacao()
				&& !Uteis.isAtributoPreenchido(agente.getSituacaoParcelaRemoverSerasaApiGeo()), "O campo Situação de Parcela  (Agente Negativação/Cobrança Conta Receber) deve ser informado.");
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AgenteNegativacaoCobrancaContaReceberVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			AgenteNegativacaoCobrancaContaReceber.incluir(getIdEntidade(), true, usuarioVO);
			incluir(obj, "AgenteNegativacaoCobrancaContaReceber", new AtributoPersistencia()
					.add("nome", obj.getNome()) 
					.add("tipo", obj.getTipo()) 
					.add("possuiintegracao", obj.getPossuiIntegracao())
					.add("integracao", obj.getIntegracao())
					.add("enviarValorTitulosUnificadosSerasa", obj.getEnviarValorTitulosUnificadosSerasa())
					.add("apiKeySerasaApiGeo", obj.getApiKeySerasaApiGeo())
					.add("senhaApiSerasaApiGeo", obj.getSenhaApiSerasaApiGeo())
					.add("negativarNegociacoesVencidaSemParcelaRecebida", obj.isNegativarNegociacoesVencidaSemParcelaRecebida())
					.add("registrarAutomaticamenteContasNegativacao", obj.getRegistrarAutomaticamenteContasNegativacao())
					.add("credorunidadeensino", obj.getCredorUnidadeEnsinoVO())
					.add("validarDocumentosEntregue", obj.getValidarDocumentosEntregue())
					.add("desconsiderarAlunoDisciplinaReprovadas", obj.getDesconsiderarAlunoDisciplinaReprovadas())
					.add("quantidadeMinimaDisciplinaReprovada", obj.getQuantidadeMinimaDisciplinaReprovada())
					.add("quantidadeDiasConsiderarParcelaVencida", obj.getQuantidadeDiasConsiderarParcelaVencida())
					.add("quantidadeDiasConsiderarParcelaVencidaInicial", obj.getQuantidadeDiasConsiderarParcelaVencidaInicial())
					.add("tipoOrigemMatricula", obj.getTipoOrigemMatricula())
					.add("tipoOrigemMensalidade", obj.getTipoOrigemMensalidade())
					.add("tipoOrigemBiblioteca", obj.getTipoOrigemBiblioteca())
					.add("tipoOrigemDevolucaoCheque", obj.getTipoOrigemDevolucaoCheque())
					.add("tipoOrigemNegociacao", obj.getTipoOrigemNegociacao())
					.add("tipoOrigemBolsaCusteadaConvenio", obj.getTipoOrigemBolsaCusteadaConvenio())
					.add("tipoOrigemContratoReceita", obj.getTipoOrigemContratoReceita())
					.add("tipoOrigemOutros", obj.getTipoOrigemOutros())
					.add("tipoOrigemInclusaoReposicao", obj.getTipoOrigemInclusaoReposicao())
					.add("tipoOrigemInscricaoProcessoSeletivo", obj.getTipoOrigemInscricaoProcessoSeletivo())
					.add("tipoOrigemRequerimento", obj.getTipoOrigemRequerimento())
					.add("tipoOrigemMaterialDidatico", obj.getTipoOrigemMaterialDidatico())
					.add("negativarTodasParcelas", obj.getNegativarTodasParcelas())
					.add("tipoContratoAgenteNegativacaoCobrancaEnum", obj.getTipoContratoAgenteNegativacaoCobrancaEnum())
					.add("diaSemanaBaseRegistrarContasNegativacao", obj.getDiaSemanaBaseRegistrarContasNegativacao())
					.add("horaExecucaoRotinaRegistrarContasNegativacao",  Integer.parseInt(obj.getHoraExecucaoRotinaRegistrarContasNegativacao().getValor()))
					.add("removerAutomaticamenteContasNegativacao", obj.getRemoverAutomaticamenteContasNegativacao())
					.add("diaSemanaBaseRemoverContasNegativacao", obj.getDiaSemanaBaseRemoverContasNegativacao())
					.add("horaExecucaoRotinaRemoverContasNegativacao", Integer.parseInt(obj.getHoraExecucaoRotinaRemoverContasNegativacao().getValor()))
					.add("situacaoParcelaRemoverSerasaApiGeo",obj.getSituacaoParcelaRemoverSerasaApiGeo())
					.add("situacaoParcelaRemoverNegociada",obj.getSituacaoParcelaRemoverNegociada())
					.add("ambienteAgenteNegativacaoCobranca", obj.getAmbienteAgenteNegativacaoCobranca()), usuarioVO);
			getFacadeFactory().getAgenteNegativacaoCobrancaUnidadeEnsinoFacade().persistir(obj.getAgenteNegativacaoCobrancaUnidadeEnsinoVOs(), obj, false, usuarioVO);
			getFacadeFactory().getTipoDocumentoPendenciaAgenteCobrancaFacade().persistir(obj.getTipoDocumentoPendenciaAgenteCobrancaVOs(), obj, false, usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AgenteNegativacaoCobrancaContaReceberVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			AgenteNegativacaoCobrancaContaReceber.alterar(getIdEntidade(), true, usuarioVO);
			alterar(obj, "AgenteNegativacaoCobrancaContaReceber", new AtributoPersistencia()
					.add("nome", obj.getNome()) 
					.add("tipo", obj.getTipo()) 
					.add("possuiintegracao", obj.getPossuiIntegracao())
					.add("integracao", obj.getIntegracao())
					.add("enviarValorTitulosUnificadosSerasa", obj.getEnviarValorTitulosUnificadosSerasa())
					.add("apiKeySerasaApiGeo", obj.getApiKeySerasaApiGeo())
					.add("senhaApiSerasaApiGeo", obj.getSenhaApiSerasaApiGeo())
					.add("negativarNegociacoesVencidaSemParcelaRecebida", obj.isNegativarNegociacoesVencidaSemParcelaRecebida())
					.add("registrarAutomaticamenteContasNegativacao", obj.getRegistrarAutomaticamenteContasNegativacao())
					.add("credorunidadeensino", obj.getCredorUnidadeEnsinoVO())
					.add("validarDocumentosEntregue", obj.getValidarDocumentosEntregue())
					.add("desconsiderarAlunoDisciplinaReprovadas", obj.getDesconsiderarAlunoDisciplinaReprovadas())
					.add("quantidadeMinimaDisciplinaReprovada", obj.getQuantidadeMinimaDisciplinaReprovada())
					.add("quantidadeDiasConsiderarParcelaVencida", obj.getQuantidadeDiasConsiderarParcelaVencida())
					.add("tipoOrigemMatricula", obj.getTipoOrigemMatricula())
					.add("tipoOrigemMensalidade", obj.getTipoOrigemMensalidade())
					.add("tipoOrigemBiblioteca", obj.getTipoOrigemBiblioteca())
					.add("tipoOrigemDevolucaoCheque", obj.getTipoOrigemDevolucaoCheque())
					.add("tipoOrigemNegociacao", obj.getTipoOrigemNegociacao())
					.add("tipoOrigemBolsaCusteadaConvenio", obj.getTipoOrigemBolsaCusteadaConvenio())
					.add("tipoOrigemContratoReceita", obj.getTipoOrigemContratoReceita())
					.add("tipoOrigemOutros", obj.getTipoOrigemOutros())
					.add("tipoOrigemInclusaoReposicao", obj.getTipoOrigemInclusaoReposicao())
					.add("tipoOrigemInscricaoProcessoSeletivo", obj.getTipoOrigemInscricaoProcessoSeletivo())
					.add("tipoOrigemRequerimento", obj.getTipoOrigemRequerimento())
					.add("tipoOrigemMaterialDidatico", obj.getTipoOrigemMaterialDidatico())
					.add("negativarTodasParcelas", obj.getNegativarTodasParcelas())
					.add("tipoContratoAgenteNegativacaoCobrancaEnum", obj.getTipoContratoAgenteNegativacaoCobrancaEnum())
					.add("diaSemanaBaseRegistrarContasNegativacao", obj.getDiaSemanaBaseRegistrarContasNegativacao())
					.add("horaExecucaoRotinaRegistrarContasNegativacao",  Integer.parseInt(obj.getHoraExecucaoRotinaRegistrarContasNegativacao().getValor()))
					.add("removerAutomaticamenteContasNegativacao", obj.getRemoverAutomaticamenteContasNegativacao())
					.add("diaSemanaBaseRemoverContasNegativacao", obj.getDiaSemanaBaseRemoverContasNegativacao())
					.add("horaExecucaoRotinaRemoverContasNegativacao", Integer.parseInt(obj.getHoraExecucaoRotinaRemoverContasNegativacao().getValor()))
					.add("situacaoParcelaRemoverSerasaApiGeo",obj.getSituacaoParcelaRemoverSerasaApiGeo())
					.add("situacaoParcelaRemoverNegociada",obj.getSituacaoParcelaRemoverNegociada())
					.add("ambienteAgenteNegativacaoCobranca", obj.getAmbienteAgenteNegativacaoCobranca()),
					new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getAgenteNegativacaoCobrancaUnidadeEnsinoVOs(), "agenteNegativacaoCobrancaUnidadeEnsino", idEntidade, obj.getCodigo(), usuarioVO);
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getTipoDocumentoPendenciaAgenteCobrancaVOs(), "tipoDocumentoPendenciaAgenteCobranca", idEntidade, obj.getCodigo(), usuarioVO);
			getFacadeFactory().getAgenteNegativacaoCobrancaUnidadeEnsinoFacade().persistir(obj.getAgenteNegativacaoCobrancaUnidadeEnsinoVOs(), obj, false, usuarioVO);
			getFacadeFactory().getTipoDocumentoPendenciaAgenteCobrancaFacade().persistir(obj.getTipoDocumentoPendenciaAgenteCobrancaVOs(), obj, false, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AgenteNegativacaoCobrancaContaReceberVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			AgenteNegativacaoCobrancaContaReceber.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM AgenteNegativacaoCobrancaContaReceber WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List<AgenteNegativacaoCobrancaContaReceberVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM AgenteNegativacaoCobrancaContaReceber WHERE lower (sem_acentos(nome)) ilike(sem_acentos('" + valorConsulta.toLowerCase() + "%')) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public List<AgenteNegativacaoCobrancaContaReceberVO> consultarPorTipo(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM AgenteNegativacaoCobrancaContaReceber "; 
		if (!valorConsulta.equals("NENHUM")) {
			sqlStr += "WHERE tipo = '" + valorConsulta + "'";
		}
		sqlStr += " ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public List<AgenteNegativacaoCobrancaContaReceberVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM AgenteNegativacaoCobrancaContaReceber WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public static List<AgenteNegativacaoCobrancaContaReceberVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<AgenteNegativacaoCobrancaContaReceberVO> vetResultado = new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static AgenteNegativacaoCobrancaContaReceberVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		AgenteNegativacaoCobrancaContaReceberVO obj = new AgenteNegativacaoCobrancaContaReceberVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setPossuiIntegracao(dadosSQL.getBoolean("possuiintegracao"));
		obj.setIntegracao((IntegracaoNegativacaoCobrancaContaReceberEnum) IntegracaoNegativacaoCobrancaContaReceberEnum.valueOf(dadosSQL.getString("integracao")));
		obj.setTipo((TipoAgenteNegativacaoCobrancaContaReceberEnum) TipoAgenteNegativacaoCobrancaContaReceberEnum.valueOf(dadosSQL.getString("tipo")));
		obj.setEnviarValorTitulosUnificadosSerasa(dadosSQL.getBoolean("enviarvalortitulosunificadosserasa"));				
		obj.setApiKeySerasaApiGeo(dadosSQL.getString("apiKeySerasaApiGeo"));
		obj.setSenhaApiSerasaApiGeo(dadosSQL.getString("senhaApiSerasaApiGeo"));
		obj.getCredorUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("credorunidadeensino"));
		obj.setNegativarNegociacoesVencidaSemParcelaRecebida(dadosSQL.getBoolean("negativarNegociacoesVencidaSemParcelaRecebida"));
		obj.setRegistrarAutomaticamenteContasNegativacao(dadosSQL.getBoolean("registrarAutomaticamenteContasNegativacao"));
		obj.setValidarDocumentosEntregue(dadosSQL.getBoolean("validarDocumentosEntregue"));
		obj.setDesconsiderarAlunoDisciplinaReprovadas(dadosSQL.getBoolean("desconsiderarAlunoDisciplinaReprovadas"));
		obj.setQuantidadeMinimaDisciplinaReprovada(dadosSQL.getInt("quantidadeMinimaDisciplinaReprovada"));
		obj.setQuantidadeDiasConsiderarParcelaVencida(dadosSQL.getInt("quantidadeDiasConsiderarParcelaVencida"));
		obj.setTipoOrigemMatricula(dadosSQL.getBoolean("tipoOrigemMatricula"));
		obj.setTipoOrigemMensalidade(dadosSQL.getBoolean("tipoOrigemMensalidade"));
		obj.setTipoOrigemBiblioteca(dadosSQL.getBoolean("tipoOrigemBiblioteca"));
		obj.setTipoOrigemDevolucaoCheque(dadosSQL.getBoolean("tipoOrigemDevolucaoCheque"));
		obj.setTipoOrigemNegociacao(dadosSQL.getBoolean("tipoOrigemNegociacao"));
		obj.setTipoOrigemBolsaCusteadaConvenio(dadosSQL.getBoolean("tipoOrigemBolsaCusteadaConvenio"));
		obj.setTipoOrigemContratoReceita(dadosSQL.getBoolean("tipoOrigemContratoReceita"));
		obj.setTipoOrigemOutros(dadosSQL.getBoolean("tipoOrigemOutros"));
		obj.setTipoOrigemInclusaoReposicao(dadosSQL.getBoolean("tipoOrigemInclusaoReposicao"));
		obj.setTipoOrigemInscricaoProcessoSeletivo(dadosSQL.getBoolean("tipoOrigemInscricaoProcessoSeletivo"));
		obj.setTipoOrigemRequerimento(dadosSQL.getBoolean("tipoOrigemRequerimento"));
		obj.setTipoOrigemMaterialDidatico(dadosSQL.getBoolean("tipoOrigemMaterialDidatico"));
		obj.setNegativarTodasParcelas(dadosSQL.getBoolean("negativarTodasParcelas"));
		obj.setTipoContratoAgenteNegativacaoCobrancaEnum(TipoContratoAgenteNegativacaoCobrancaEnum.valueOf(dadosSQL.getString("tipoContratoAgenteNegativacaoCobrancaEnum")));
		obj.setDiaSemanaBaseRegistrarContasNegativacao(dadosSQL.getString("diaSemanaBaseRegistrarContasNegativacao") == null ? null : DiaSemanaJob.valueOf(dadosSQL.getString("diaSemanaBaseRegistrarContasNegativacao")));
		obj.setHoraExecucaoRotinaRegistrarContasNegativacao(dadosSQL.getString("horaExecucaoRotinaRegistrarContasNegativacao") == null ? null : Horas.getEnum(dadosSQL.getString("horaExecucaoRotinaRegistrarContasNegativacao")));
		obj.setRemoverAutomaticamenteContasNegativacao(dadosSQL.getBoolean("removerAutomaticamenteContasNegativacao"));
		obj.setDiaSemanaBaseRemoverContasNegativacao(dadosSQL.getString("diaSemanaBaseRemoverContasNegativacao") == null ? null :  DiaSemanaJob.valueOf(dadosSQL.getString("diaSemanaBaseRemoverContasNegativacao")));
		obj.setHoraExecucaoRotinaRemoverContasNegativacao(dadosSQL.getString("horaExecucaoRotinaRemoverContasNegativacao")== null ? null : Horas.getEnum(dadosSQL.getString("horaExecucaoRotinaRemoverContasNegativacao")));
		obj.setSituacaoParcelaRemoverSerasaApiGeo(SituacaoParcelaRemoverSerasaApiGeo.valueOf(dadosSQL.getString("situacaoParcelaRemoverSerasaApiGeo")));
		obj.setSituacaoParcelaRemoverNegociada(dadosSQL.getString("situacaoParcelaRemoverNegociada"));
		obj.setAmbienteAgenteNegativacaoCobranca(dadosSQL.getString("ambienteAgenteNegativacaoCobranca") == null ? null : AmbienteEnum.valueOf(dadosSQL.getString("ambienteAgenteNegativacaoCobranca")));
		obj.setMarcarTodosTipoOrigem(dadosSQL.getBoolean("marcarTodosTipoOrigem"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			obj.setCredorUnidadeEnsinoVO(Uteis.montarDadosVO(dadosSQL.getInt("credorunidadeensino"), UnidadeEnsinoVO.class, p -> getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(p, false, nivelMontarDados, usuario)));
			return obj;
		}
		obj.setCredorUnidadeEnsinoVO(Uteis.montarDadosVO(dadosSQL.getInt("credorunidadeensino"), UnidadeEnsinoVO.class, p -> getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(p, false, nivelMontarDados, usuario)));
		obj.setAgenteNegativacaoCobrancaUnidadeEnsinoVOs(getFacadeFactory().getAgenteNegativacaoCobrancaUnidadeEnsinoFacade().consultarPorAgenteNegativacaoCobrancaContaReceberVO(obj, false, usuario));
		obj.setTipoDocumentoPendenciaAgenteCobrancaVOs(getFacadeFactory().getTipoDocumentoPendenciaAgenteCobrancaFacade().consultarPorAgenteNegativacaoCobrancaContaReceberVO(obj, false, usuario));
		return obj;
	}

	public AgenteNegativacaoCobrancaContaReceberVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados ,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM AgenteNegativacaoCobrancaContaReceber WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return new AgenteNegativacaoCobrancaContaReceberVO();
	}

	public static String getIdEntidade() {
		return AgenteNegativacaoCobrancaContaReceber.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		AgenteNegativacaoCobrancaContaReceber.idEntidade = idEntidade;
	}
	
	public List<AgenteNegativacaoCobrancaContaReceberVO> consultarAgenteNegativacaoSerasaApiGeo(IntegracaoNegativacaoCobrancaContaReceberEnum integracaoNegativacaoCobrancaContaReceberEnum, UsuarioVO usaurio) throws Exception {
		String sqlStr = "SELECT * FROM AgenteNegativacaoCobrancaContaReceber where possuiIntegracao = true and integracao = '" + integracaoNegativacaoCobrancaContaReceberEnum.name() +"'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usaurio));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarCronogramaExecucaoJobSerasaApiGeo(JobsEnum jobsEnum, String horaExecutarJob, int codigoAgenteNegativacaoCobrancaContaReceberVO) {
		try {
			Calendar cal = Calendar.getInstance();
	    	cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(horaExecutarJob));
	    	cal.set(Calendar.MINUTE, 0);
	    	cal.set(Calendar.SECOND, 0);
			Date dataInicioRegistro = cal.getTime();
			JobDetail job = null;
			Trigger trigger = null;
			if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR)) {
				job = JobBuilder.newJob(RegistrarSerasaApiGeo.class).build();
			}else if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REMOVER)) {
				job = JobBuilder.newJob(RemoverSerasaApiGeo.class).build();
			} 	
			if(dataInicioRegistro.before(new Date())) {
				executarCriacaoRegistroExecucaoJobSerasa(jobsEnum, codigoAgenteNegativacaoCobrancaContaReceberVO, new Date());	
				trigger = TriggerBuilder.newTrigger().withIdentity(jobsEnum.getName()).startNow().build();
			}else {
				executarCriacaoRegistroExecucaoJobSerasa(jobsEnum, codigoAgenteNegativacaoCobrancaContaReceberVO, dataInicioRegistro);
				trigger = TriggerBuilder.newTrigger().withIdentity(jobsEnum.getName()).withSchedule(CronScheduleBuilder.cronSchedule("0 0 " + horaExecutarJob + " " +UteisData.getDiaMesData(new Date())+ " * ? ")).build();	
			}
			Scheduler sc = StdSchedulerFactory.getDefaultScheduler();
			sc.start();
			sc.scheduleJob(job, trigger);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoRegistroExecucaoJobSerasa(JobsEnum jobsEnum, int codigoAgenteNegativacaoCobrancaContaReceberVO, Date dataInicioRegistro) throws Exception {
		if (getFacadeFactory().getRegistroExecucaoJobFacade().consultarSeExisterRegistroExecucaoJobPorCodigoOrigem(jobsEnum, codigoAgenteNegativacaoCobrancaContaReceberVO)) {
			getFacadeFactory().getRegistroExecucaoJobFacade().atualizarAgendamentoExecucaoRegistroExecucaoJob(jobsEnum, codigoAgenteNegativacaoCobrancaContaReceberVO, dataInicioRegistro);
		}else {
			RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO(); 
			registroExecucaoJobVO.setNome(jobsEnum.getName());
			registroExecucaoJobVO.setDataInicio(dataInicioRegistro);
			registroExecucaoJobVO.setDataTermino(null);
			registroExecucaoJobVO.setCodigoOrigem(codigoAgenteNegativacaoCobrancaContaReceberVO);
			getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJobVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioIntegracaoWebService(AgenteNegativacaoCobrancaContaReceberVO anccr, RegistroNegativacaoCobrancaContaReceberItemVO rnccri, JobsEnum jobsEnum, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		
		Uteis.checkState((!Uteis.isAmbienteProducao() || Uteis.isVersaoDev()) && anccr.getAmbienteAgenteNegativacaoCobranca().isProducao(), "O SEI está em um ambiente diferente de Produção e por isso não é possível executar  a Integração do Agente de Negativação para ambiente de Produção.");
		String url = anccr.getAmbienteAgenteNegativacaoCobranca().isHomologacao() ? "https://geotreinamento.wctecnologia.com.br/negativacaoserasa/" : "https://geo.wctecnologia.com.br/negativacaoserasa/";		
		try {
			//Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			Gson gson = new GsonBuilder().create();
			IntegracaoSerasaApiGeoRSVO isag = montarDadosIntegracaoSerasaApiGeoRSVO(anccr, rnccri, jobsEnum, config, usuario);
			String json = gson.toJson(isag);
			if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR)) {
				rnccri.setJsonIntegracao(json);	
			}else {
				rnccri.setJsonIntegracaoExclusao(json);
			}
			HttpResponse<String> response = unirest().post(url).header("Content-Type", "application/json").body(json).asString();
			if(response.getBody().contains("\"codigo\":200") || (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR) && response.getBody().contains("Esta d\\u00edvida j\\u00e1 foi registrada"))) {
				for (String bodyresposta : response.getBody().split(",")) {
					if(bodyresposta.contains("id_transacao")) {
						int tamanhoInicial = bodyresposta.indexOf(":");
						rnccri.setCodigoIntegracaoApi(bodyresposta.substring(tamanhoInicial+2, bodyresposta.length()-1));
						break;
					}	
				}
			} else if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REMOVER) && response.getBody().contains("O registro n\\u00e3o foi encontrado para exclus\\u00e3o de negativa\\u00e7\\u00e3o.\"")){
				rnccri.setCodigoIntegracaoApi("Alerta - " + new Date().getTime());
				rnccri.setMotivo(rnccri.getMotivo() + " - API O Registro não foi encontrado para exclusão.");
			} else {
				rnccri.setCodigoIntegracaoApi("Erro - " + new Date().getTime());
				rnccri.setMotivo("Erro - "+jobsEnum.getName()+" - "+response.getStatus()+ "-" + response.getBody());
				if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR)) {
					rnccri.setDataExclusao(new Date());	
				}else {
					rnccri.setDataExclusao(null);					
				}
				rnccri.setCodigoUsuarioExclusao(null);
				rnccri.setNomeUsuarioExclusao(null);
			}
		} catch (HttpClientErrorException hcee) {
			rnccri.setCodigoIntegracaoApi("Erro - " + new Date().getTime());
			rnccri.setMotivo("HttpClientErrorException - "+jobsEnum.getName()+" - "+hcee.getStatusCode()+ "-" + hcee.getResponseBodyAsString());
			if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR)) {
				rnccri.setDataExclusao(new Date());	
			}else {
				rnccri.setDataExclusao(null);					
			}
			rnccri.setCodigoUsuarioExclusao(null);
			rnccri.setNomeUsuarioExclusao(null);
		} catch (Exception e) {
			rnccri.setCodigoIntegracaoApi("Erro - " + new Date().getTime());
			rnccri.setMotivo("Exception - "+jobsEnum.getName()+" - " + e.getMessage());
			if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR)) {
				rnccri.setDataExclusao(new Date());	
			}else {
				rnccri.setDataExclusao(null);					
			}
			rnccri.setCodigoUsuarioExclusao(null);
			rnccri.setNomeUsuarioExclusao(null);
		} finally {
			url = null;
			//restTemplate = null;
		}
	}

	private IntegracaoSerasaApiGeoRSVO montarDadosIntegracaoSerasaApiGeoRSVO(AgenteNegativacaoCobrancaContaReceberVO anccr, RegistroNegativacaoCobrancaContaReceberItemVO rnccri, JobsEnum jobsEnum, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) {
		IntegracaoSerasaApiGeoRSVO isag = new IntegracaoSerasaApiGeoRSVO();
		Uteis.checkState(!Uteis.isAtributoPreenchido(anccr.getAmbienteAgenteNegativacaoCobranca()), "O Ambiente do Agente negativação deve ser informado");
		isag.setHomologacao(anccr.getAmbienteAgenteNegativacaoCobranca().isHomologacao() ? "S" : "N");
		Uteis.checkState(!Uteis.isAtributoPreenchido(anccr.getApiKeySerasaApiGeo()), "O campo key API do Agente negativação deve ser informado");
		isag.setApi_key(anccr.getApiKeySerasaApiGeo());
		Uteis.checkState(!Uteis.isAtributoPreenchido(anccr.getSenhaApiSerasaApiGeo()), "O campo Senha API do Agente negativação deve ser informado");
		isag.setSenha_api(Base64.getEncoder().encodeToString(anccr.getSenhaApiSerasaApiGeo().getBytes()));
		isag.setPostback("https://endlfb64urxc9.x.pipedream.net/");
		isag.setTipo_acao("inclusao/exclusao");		
		Uteis.checkState(!Uteis.isAtributoPreenchido(anccr.getCredorUnidadeEnsinoVO()), "O campo Credor Unidade Ensino do Agente negativação deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(anccr.getCredorUnidadeEnsinoVO().getCNPJ()), "O campo Cnpj do Credor Unidade Ensino do Agente negativação deve ser informado.");
		isag.setCnpj_credor(Uteis.removeCaractersEspeciais2(anccr.getCredorUnidadeEnsinoVO().getCNPJ()));
		if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR)) {
			switch (anccr.getTipoContratoAgenteNegativacaoCobrancaEnum()) {
			case NOSSO_NUMERO:
				Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getNossoNumero()), "O campo Nosso Número da Conta Receber de código "+ rnccri.getContaReceber()+ " não foi informado.");
				isag.setContrato(rnccri.getNossoNumero());
				break;
			case MATRICULA:
				Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getMatricula()), "O campo Matrícula da Conta Receber de código "+ rnccri.getContaReceber()+ " não foi informado.");
				isag.setContrato(rnccri.getMatricula());
				break;
			case MATRICULA_PERIODO:
				Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getMatriculaPeriodo()), "O campo Matrícula Período da Conta Receber de código "+ rnccri.getContaReceber()+ " não foi informado.");
				isag.setContrato(rnccri.getMatriculaPeriodo().toString());
				break;
			}	
			rnccri.setTipoContratoAgenteNegativacaoCobrancaEnum(anccr.getTipoContratoAgenteNegativacaoCobrancaEnum());
			isag.setOperacao("I");			
			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAluno()), "O campo Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
			isag.setNome_devedor(rnccri.getAluno().length() > 69 ? rnccri.getAluno().substring(0, 69) : rnccri.getAluno());
			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoDataNascimento()), "O campo Data de Nascimento do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
			isag.setData_nascimento(Uteis.getData(rnccri.getAlunoDataNascimento(), "yyyy-MM-dd"));
			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoCep()), "O campo Cep do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
			isag.setCep(Uteis.removeCaractersEspeciais2(rnccri.getAlunoCep()));
			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoEndereco()), "O campo Endereço do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
			isag.setEndereco(rnccri.getAlunoEndereco().length() > 36 ? rnccri.getAlunoEndereco().substring(0, 36) : rnccri.getAlunoEndereco());
			if(Uteis.isAtributoPreenchido(rnccri.getAlunoNumero())) {
				isag.setNumero(rnccri.getAlunoNumero().length() > 6 ? rnccri.getAlunoNumero().substring(0, 6) : rnccri.getAlunoNumero());	
			}else {
				isag.setNumero("0");
			}
			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoBairro()), "O campo Bairro do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
			isag.setBairro(rnccri.getAlunoBairro().length() > 19 ? rnccri.getAlunoBairro().substring(0, 19) : rnccri.getAlunoBairro());
			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoMunicipio()), "O campo Municipio do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
			isag.setMunicipio(rnccri.getAlunoMunicipio().length() > 49 ? rnccri.getAlunoMunicipio().substring(0, 49) : rnccri.getAlunoMunicipio());
			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoUf()), "O campo UF do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
			isag.setUf(rnccri.getAlunoUf());
			isag.setNatureza_operacao("AD");
			isag.setTem_aval("N");
			Uteis.checkState(!Uteis.isAtributoPreenchido(usuario.getNome()) , "Por favor verificar o Usuário de Operações externas na configuracao geral do sistema.");
			isag.setExtra(usuario.getNome());
		} else if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REMOVER)) {
			switch (rnccri.getTipoContratoAgenteNegativacaoCobrancaEnum()) {
			case NOSSO_NUMERO:
				Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getNossoNumero()), "O campo Nosso Número da Conta Receber de código "+ rnccri.getContaReceber()+ " não foi informado.");
				isag.setContrato(rnccri.getNossoNumero());
				break;
			case MATRICULA:
				Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getMatricula()), "O campo Matrícula da Conta Receber de código "+ rnccri.getContaReceber()+ " não foi informado.");
				isag.setContrato(rnccri.getMatricula());
				break;
			case MATRICULA_PERIODO:
				Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getMatriculaPeriodo()), "O campo Matrícula Período da Conta Receber de código "+ rnccri.getContaReceber()+ " não foi informado.");
				isag.setContrato(rnccri.getMatriculaPeriodo().toString());
				break;
			default:
				break;
			}
			isag.setOperacao("E");
			isag.setMotivo_baixa("89");
		}
		isag.setTipo_pessoa("F");
		Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoCpf()), "O campo CPF do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
		isag.setCpf_cnpj_devedor(Uteis.removeCaractersEspeciais2(rnccri.getAlunoCpf()));
		Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getDataVencimento()), "O campo Data Vencimento da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
		isag.setData_vencimento(Uteis.getData(rnccri.getDataVencimento(), "yyyy-MM-dd"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getValor()), "O campo Valor da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
		isag.setValor_divida(Uteis.arrendondarForcando2CadasDecimaisStr(rnccri.getValor()));
		return isag;
	}
	
	
//	private void montarDadosIntegracaoSerasaApiGeoRSVO(MultiValueMap<String, String> map, AgenteNegativacaoCobrancaContaReceberVO anccr, RegistroNegativacaoCobrancaContaReceberItemVO rnccri, JobsEnum jobsEnum, ConfiguracaoGeralSistemaVO config) {
//		Uteis.checkState(!Uteis.isAtributoPreenchido(anccr.getAmbienteAgenteNegativacaoCobranca()), "O Ambiente do Agente negativação deve ser informado");
//		map.add("homologacao", anccr.getAmbienteAgenteNegativacaoCobranca().isHomologacao() ? "S" : "N");
//		Uteis.checkState(!Uteis.isAtributoPreenchido(anccr.getApiKeySerasaApiGeo()), "O campo key API do Agente negativação deve ser informado");
//		map.add("api_key", anccr.getApiKeySerasaApiGeo());	
//		Uteis.checkState(!Uteis.isAtributoPreenchido(anccr.getSenhaApiSerasaApiGeo()), "O campo Senha API do Agente negativação deve ser informado");
//		map.add("senha_api", Base64.getEncoder().encodeToString(anccr.getSenhaApiSerasaApiGeo().getBytes()));	
//		map.add("postback", config.getUrlAcessoExternoAplicacao() + "/webservice/integracaoSerasaApiGeoRS/status");	
//		map.add("tipo_acao", "inclusao/exclusao");	
//		Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getNossoNumero()), "O campo Nosso Número da Conta Receber de código "+ rnccri.getContaReceber()+ " não foi informado.");
//		map.add("contrato", rnccri.getNossoNumero());
//		Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getUnidadeEnsinoCnpj()), "O campo Unidade Ensino da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//		map.add("cnpj_credor", Uteis.removeCaractersEspeciais2(rnccri.getUnidadeEnsinoCnpj()));
//		if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR)) {
//			map.add("operacao", "I");
//			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAluno()), "O campo Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//			map.add("nome_devedor", rnccri.getAluno());
//			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoDataNascimento()), "O campo Data de Nascimento do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//			map.add("data_nascimento", Uteis.getData(rnccri.getAlunoDataNascimento(), "yyyy-MM-dd"));
//			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoCep()), "O campo Cep do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//			map.add("cep", Uteis.removeCaractersEspeciais2(rnccri.getAlunoCep()));
//			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoEndereco()), "O campo Endereço do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//			map.add("endereco", rnccri.getAlunoEndereco().length() > 37 ? rnccri.getAlunoEndereco().substring(0, 37) : rnccri.getAlunoEndereco());
//			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoNumero()), "O campo Número Endereço do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//			map.add("numero", rnccri.getAlunoNumero());
//			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoBairro()), "O campo Bairro do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//			map.add("bairro", rnccri.getAlunoBairro().length() > 20 ? rnccri.getAlunoBairro().substring(0, 20) : rnccri.getAlunoBairro());
//			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoMunicipio()), "O campo Municipio do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//			map.add("municipio", rnccri.getAlunoMunicipio().length() > 50 ? rnccri.getAlunoMunicipio().substring(0, 50) : rnccri.getAlunoMunicipio());
//			Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoUf()), "O campo UF do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//			map.add("uf", rnccri.getAlunoUf());
//			map.add("natureza_operacao", "AD");
//			map.add("tem_aval", "N");
//		} else if (jobsEnum.equals(JobsEnum.JOB_SERASA_API_GEO_REMOVER)) {
//			map.add("operacao", "E");
//			map.add("motivo_baixa", "89");
//		}
//		map.add("tipo_pessoa", "F");
//		Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getAlunoCpf()), "O campo CPF do Aluno da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");map.add("tipo_pessoa", "F");
//		map.add("cpf_cnpj_devedor", Uteis.removeCaractersEspeciais2(rnccri.getAlunoCpf()));
//		Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getDataVencimento()), "O campo Data Vencimento da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//		map.add("data_vencimento", Uteis.getData(rnccri.getDataVencimento(), "yyyy-MM-dd"));
//		Uteis.checkState(!Uteis.isAtributoPreenchido(rnccri.getValor()), "O campo Valor da Conta Receber de nosso número "+ rnccri.getNossoNumero()+ " não foi informado.");
//		map.add("valor_divida", Uteis.arrendondarForcando2CadasDecimaisStr(rnccri.getValor()));
//	}
		
	

}