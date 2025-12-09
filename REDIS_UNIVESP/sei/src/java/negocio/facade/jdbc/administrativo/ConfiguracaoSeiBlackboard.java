package negocio.facade.jdbc.administrativo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import controle.arquitetura.DataModelo;
import jobs.enumeradores.JobsEnum;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardDominioVO;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.FonteDeDadosBlackboardVO;
import negocio.comuns.blackboard.PermissaoBlackboardVO;
import negocio.comuns.blackboard.enumeradores.CourseRoleIdEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ConfiguracaoSeiBlackboardInterfaceFacade;
import webservice.boletoonline.itau.comuns.TokenVO;

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoSeiBlackboard extends ControleAcesso implements ConfiguracaoSeiBlackboardInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5932799722068589985L;
	private static String idEntidade = "ConfiguracaoSeiBlackboard";
	
	public static String getIdEntidade() {
		return ConfiguracaoSeiBlackboard.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ConfiguracaoSeiBlackboard.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDados(ConfiguracaoSeiBlackboardVO obj) {
		validarDadosSeiBlackboard(obj);
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getClientBlackboard()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_clientGsuite"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSecretBlackboard()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_secretBlackboard"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUrlExternaBlackboard()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_urlExternaBlackboard"));
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDadosSeiBlackboard(ConfiguracaoSeiBlackboardVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUsernameSeiBlackboard()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_usernameSeiGsuite"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSenhaSeiBlackboard()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_senhaSeiGsuite"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getClienteSeiBlackboard()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_clienteSeiGsuite"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTokenSeiBlackboard()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_tokenSeiGsuite"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUrlExternaSeiBlackboard()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_urlExternaSeiGsuite"));		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ConfiguracaoSeiBlackboardVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			ConfiguracaoSeiBlackboard.incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "ConfiguracaoSeiBlackboard", new AtributoPersistencia()
					.add("usernameSeiBlackboard", obj.getUsernameSeiBlackboard())
					.add("senhaSeiBlackboard", obj.getSenhaSeiBlackboard())
					.add("clienteSeiBlackboard", obj.getClienteSeiBlackboard())
					.add("tokenSeiBlackboard", obj.getTokenSeiBlackboard())
					.add("urlExternaSeiBlackboard", obj.getUrlExternaSeiBlackboard())
					.add("idBlackboard", obj.getIdBlackboard())
					.add("clientBlackboard", obj.getClientBlackboard())
					.add("secretBlackboard", obj.getSecretBlackboard())
					.add("fonteDeDadosConteudoMasterBlackboard", obj.getFonteDeDadosConteudoMasterBlackboard())
					.add("urlExternaBlackboard", obj.getUrlExternaBlackboard())
					.add("ativarOperacoesDoBlackboard", obj.isAtivarOperacoesDoBlackboard())
					.add("ativarOperacaoEnsalamentoEstagio", obj.isAtivarOperacaoEnsalamentoEstagio())
					.add("ativarOperacaoEnsalamentoTCC", obj.isAtivarOperacaoEnsalamentoTCC()),
					usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ConfiguracaoSeiBlackboardVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			ConfiguracaoSeiBlackboard.alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "ConfiguracaoSeiBlackboard", new AtributoPersistencia()
					.add("usernameSeiBlackboard", obj.getUsernameSeiBlackboard())
					.add("senhaSeiBlackboard", obj.getSenhaSeiBlackboard())
					.add("clienteSeiBlackboard", obj.getClienteSeiBlackboard())
					.add("tokenSeiBlackboard", obj.getTokenSeiBlackboard())
					.add("urlExternaSeiBlackboard", obj.getUrlExternaSeiBlackboard())
					.add("idBlackboard", obj.getIdBlackboard())
					.add("clientBlackboard", obj.getClientBlackboard())
					.add("secretBlackboard", obj.getSecretBlackboard())
					.add("urlExternaBlackboard", obj.getUrlExternaBlackboard())
					.add("fonteDeDadosConteudoMasterBlackboard", obj.getFonteDeDadosConteudoMasterBlackboard())
					.add("ativarOperacoesDoBlackboard", obj.isAtivarOperacoesDoBlackboard())
					.add("ativarOperacaoEnsalamentoEstagio", obj.isAtivarOperacaoEnsalamentoEstagio())
					.add("ativarOperacaoEnsalamentoTCC", obj.isAtivarOperacaoEnsalamentoTCC()),
					new AtributoPersistencia()
					.add("codigo", obj.getCodigo()), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoSeiBlackboardVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM ConfiguracaoSeiBlackboard WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
			getAplicacaoControle().setConfiguracaoSeiBlackboardVO(null);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persitir(ConfiguracaoSeiBlackboardVO obj, boolean verificarAcesso,  UsuarioVO usuario) throws Exception {
		validarDados(obj);		
		if (!Uteis.isAtributoPreenchido(obj)) {
			incluir(obj, verificarAcesso, usuario);
		} else {
			alterar(obj, verificarAcesso, usuario);
		}
		if(Uteis.isAtributoPreenchido(obj.getFonteDeDadosConteudoMasterBlackboard())){
			realizarVerificacaoFonteDadosConteudoMaster(obj, usuario);
		}
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaConfiguracaoSeiBlackboardDominioVO(), "configuracaoSeiBlackboardDominio", idEntidade, obj.getCodigo(), usuario);
		getFacadeFactory().getConfiguracaoSeiBlackboardDominioFacade().persistir(obj.getListaConfiguracaoSeiBlackboardDominioVO(), verificarAcesso, usuario);
		getAplicacaoControle().setConfiguracaoSeiBlackboardVO(null);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarConfiguracaoSeiBlackboardDominioVO(ConfiguracaoSeiBlackboardVO obj, ConfiguracaoSeiBlackboardDominioVO csbd, UsuarioVO usuario) {
		try {
			int index = 0;
			csbd.setConfiguracaoSeiBlackboardVO(obj);
			getFacadeFactory().getConfiguracaoSeiBlackboardDominioFacade().validarDados(csbd);
			for (ConfiguracaoSeiBlackboardDominioVO objExistente : obj.getListaConfiguracaoSeiBlackboardDominioVO()) {
				if (objExistente.equalsCampoSelecaoLista(csbd)) {
					obj.getListaConfiguracaoSeiBlackboardDominioVO().set(index, csbd);
					return;
				}
				index++;
			}
			obj.getListaConfiguracaoSeiBlackboardDominioVO().add(csbd);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)	
	public void removerConfiguracaoSeiBlackboardDominioVO(ConfiguracaoSeiBlackboardVO obj, ConfiguracaoSeiBlackboardDominioVO csbd, UsuarioVO usuario) {
		Iterator<ConfiguracaoSeiBlackboardDominioVO> i = obj.getListaConfiguracaoSeiBlackboardDominioVO().iterator();
		while (i.hasNext()) {
			ConfiguracaoSeiBlackboardDominioVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(csbd)) {
				i.remove();
				return;
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarVerificacaoFonteDadosConteudoMaster(ConfiguracaoSeiBlackboardVO obj, UsuarioVO usuario) {
		for(String fonteDados : obj.getFonteDeDadosConteudoMasterBlackboard().split(";") ) {
			 List<FonteDeDadosBlackboardVO> lista = realizarConsultaFonteDeDadosBlackboardPorDescricao(obj, fonteDados.trim(), usuario);
			 Uteis.checkState(!Uteis.isAtributoPreenchido(lista), "Não foi encontrado nenhuma Fonte de Dados com a seguinte Descrição "+fonteDados);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public TokenVO consultarTokenVO(ConfiguracaoSeiBlackboardVO obj) {
		try {			
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj), "Não foi localizado uma configuração Sei Blackboard para realizar essa operação.");
			validarDadosSeiBlackboard(obj);
			TokenVO token =  TokenVO.autenticarBasicaTokenPorUnirest(obj.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_TOKEN_SEI, 
					obj.getClienteSeiBlackboard(),
					obj.getTokenSeiBlackboard(),
					obj.getParametroBody());
			Uteis.checkState(token == null || token.getAccess_token().isEmpty(), "Token não Autenticado (Sei Blackboard)");
			return token;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<String> consultarStatusSeiBlackboard(ConfiguracaoSeiBlackboardVO obj, UsuarioVO usuario) {
		try {
			TokenVO token = consultarTokenVO(obj);			
			return unirest().get(obj.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_SERVICO_STATUS_SEI)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
					.asString();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<String> consultarStatusBlackboard(ConfiguracaoSeiBlackboardVO obj, UsuarioVO usuario) {
		try {
			TokenVO token = consultarTokenVO(obj);			
			return unirest().get(obj.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_SERVICO_STATUS_SEI+ UteisWebServiceUrl.URL_BLACKBOARD_SERVICO_STATUS_BLACKBOARD)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
					.asString();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<FonteDeDadosBlackboardVO> realizarConsultaFonteDeDadosBlackboardVO(UsuarioVO usuario) {
		try {
			ConfiguracaoSeiBlackboardVO obj = consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuario);
			TokenVO token = consultarTokenVO(obj);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(UteisWebServiceUrl.descricao, Constantes.EMPTY);
			HttpResponse<JsonNode> jsonResponse  = unirestHeaders(token, obj.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_ADMIN_SDK + UteisWebServiceUrl.URL_BLACKBOARD_ADMIN_SDK_DATA_SOURCE, RequestMethod.GET, headers,  usuario);
			Gson gson = inicializaGson();
			Type listaType = new TypeToken<ArrayList<FonteDeDadosBlackboardVO>>(){}.getType();
			return gson.fromJson(jsonResponse.getBody().toString(), listaType);
		} catch (Exception e) {
			if(!e.getMessage().contains("Connection refused")) {
				throw new StreamSeiException(e);	
			}
			return new ArrayList<>();
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<FonteDeDadosBlackboardVO> realizarConsultaFonteDeDadosBlackboardPorDescricao(ConfiguracaoSeiBlackboardVO obj, String descricao, UsuarioVO usuario) {
		try {
			TokenVO token = consultarTokenVO(obj);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(UteisWebServiceUrl.descricao,descricao);
			HttpResponse<JsonNode> jsonResponse  = unirestHeaders(token, obj.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_ADMIN_SDK + UteisWebServiceUrl.URL_BLACKBOARD_ADMIN_SDK_DATA_SOURCE, RequestMethod.GET, headers,  usuario);
			Gson gson = inicializaGson();
			Type listaType = new TypeToken<ArrayList<FonteDeDadosBlackboardVO>>(){}.getType();
			return gson.fromJson(jsonResponse.getBody().toString(), listaType);
		} catch (Exception e) {
			if(!e.getMessage().contains("Connection refused")) {
				throw new StreamSeiException(e);	
			}
			return new ArrayList<>();
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<PermissaoBlackboardVO> realizarConsultaPermissaoBlackboard(ConfiguracaoSeiBlackboardVO obj, ConfiguracaoSeiBlackboardDominioVO csbd, UsuarioVO usuario) {
		try {
			TokenVO token = consultarTokenVO(obj);	
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(UteisWebServiceUrl.operacao, csbd.isTipoPermissaoSistema() ? "PERMISSAO_SISTEMA": "PERMISSAO_INSTITUCIONAL");
			HttpResponse<JsonNode> jsonResponse  = unirestHeaders(token, obj.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_ADMIN_SDK + UteisWebServiceUrl.URL_BLACKBOARD_ADMIN_SDK_PERMISAO, RequestMethod.GET, headers,  usuario);
			Gson gson = inicializaGson();
			Type listaType = new TypeToken<ArrayList<PermissaoBlackboardVO>>(){}.getType();
			List<PermissaoBlackboardVO> listaPermissaoBlackboard =  gson.fromJson(jsonResponse.getBody().toString(), listaType);			
			for (PermissaoBlackboardVO permissaoBlackboardVO : csbd.getListaTempPermissaoBlackboardVO()) {
				listaPermissaoBlackboard.stream().filter(p-> p.getName().equals(permissaoBlackboardVO.getName())).forEach(p-> p.setSelecionado(true));
			}
			return listaPermissaoBlackboard;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarGeracaoDaContaBlackboardPorPessoa(ConfiguracaoSeiBlackboardVO obj, PessoaVO pessoa, UsuarioVO usuario) {
		try {
			TokenVO token = consultarTokenVO(obj);		
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(UteisWebServiceUrl.pessoa, pessoa.getCodigo().toString());
			HttpResponse<JsonNode> jsonResponse  = unirestHeaders(token, obj.getUrlExternaSeiBlackboard() + UteisWebServiceUrl.URL_BLACKBOARD_ADMIN_SDK + UteisWebServiceUrl.URL_BLACKBOARD_ADMIN_SDK_CONTA_INDIVIDUAL, RequestMethod.POST, headers,  usuario);
			return jsonResponse;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarSalaAulaOperacaoPorMatricula(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) {
		try {			
			ConfiguracaoSeiBlackboardVO obj = consultarConfiguracaoSeiBlackboardPadrao(Uteis.NIVELMONTARDADOS_TODOS, usuario);			
			if(Uteis.isAtributoPreenchido(obj) ) {
				executarVerificacaoIncluirSalaAulaBlackboardOperacao(matricula, matriculaPeriodoVO, usuario);
			}		
		} catch (Exception e) {
			/// metodo nao pode levantar excecão pois nao pode parar o processo de matricula
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_BLACKBOARD_OPERACAO.getName(), "SEI Criar Matricula:"+matricula.getMatricula()+" - pessoa:" + matricula.getAluno().getCodigo()+ " - ue:" + matricula.getUnidadeEnsino().getCodigo() + " msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarVerificacaoIncluirSalaAulaBlackboardOperacao(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" INSERT INTO salaaulablackboardoperacao (curso, turma, turmapratica, turmateorica, disciplina, ano, semestre, email, operacao, executada, codigoorigem, tipoorigem)");
		sql.append(" (SELECT   ");
		sql.append("	matricula.curso as curso, ");
		sql.append("	matriculaperiodoturmadisciplina.turma as turma, ");
		sql.append("	matriculaperiodoturmadisciplina.turmapratica as turmapratica, ");
		sql.append("	matriculaperiodoturmadisciplina.turmateorica as turmateorica, ");
		sql.append("	matriculaperiodoturmadisciplina.disciplina, ");
		sql.append("	matriculaperiodoturmadisciplina.ano, ");
		sql.append("	matriculaperiodoturmadisciplina.semestre, ");
		sql.append("	pessoaemailinstitucional.email as email, ");
		sql.append("	'INSERT' as operacao, ");
		sql.append("	false as executada, ");
		sql.append("	matriculaperiodoturmadisciplina.codigo as codigoorigem, ");
		sql.append("    'MATRICULA_PERIODO_TURMA_DISCIPLINA' as tipoorigem");
		sql.append(" from pessoaemailinstitucional");
		sql.append(" inner join pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa");
		sql.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo and  matriculaperiodo.matricula =  matriculaperiodoturmadisciplina.matricula");
		sql.append(" inner join disciplina on matriculaperiodoturmadisciplina.disciplina = disciplina.codigo and (disciplina.dividirSalaEmGrupo is false and disciplina.classificacaoDisciplina != 'TCC' ) ");
		sql.append(" where matricula.matricula = '").append(matricula.getMatricula()).append("' ");
		if(Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
			sql.append(" and matriculaperiodo.codigo = ").append(matriculaPeriodoVO.getCodigo()).append(" ");
		}
		sql.append(" and not exists ( ");
		sql.append("  select salaaulablackboardpessoa.codigo ");
		sql.append("  from salaaulablackboardpessoa ");
		sql.append("  where salaaulablackboardpessoa.matriculaperiodoturmadisciplina is not null and salaaulablackboardpessoa.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" ) ");
		sql.append(" and not exists ( ");
		sql.append("  select salaaulablackboardoperacao.codigo ");
		sql.append("  from salaaulablackboardoperacao ");
		sql.append("  where salaaulablackboardoperacao.tipoorigem = 'MATRICULA_PERIODO_TURMA_DISCIPLINA' ");
		sql.append("  and salaaulablackboardoperacao.codigoorigem = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" ))");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().execute(sql.toString());
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, ConfiguracaoSeiBlackboardVO obj) {
		dataModelo.getListaConsulta().clear();
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultaRapidaPorFiltros(obj, dataModelo));
	}

	private List<ConfiguracaoSeiBlackboardVO> consultaRapidaPorFiltros(ConfiguracaoSeiBlackboardVO obj, DataModelo dataModelo) {
		try {
			ConfiguracaoSeiBlackboard.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1 = 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY csb.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			montarTotalizadorConsultaBasica(dataModelo, tabelaResultado);
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}	

	private void montarFiltrosParaConsulta(ConfiguracaoSeiBlackboardVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (dataModelo.getCampoConsulta().equals(ConfiguracaoSeiBlackboardVO.enumCampoConsultaConfiguracaoSeiBlackboard.CODIGO.name())
				&& Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			sqlStr.append(" and csb.codigo = ? ");
			dataModelo.getListaFiltros().add(Integer.parseInt(dataModelo.getValorConsulta()));
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder(getSelectTotalizadorConsultaBasica());
		sql.append(" csb.codigo as \"csb.codigo\",  ");
		sql.append(" csb.usernameSeiBlackboard as \"csb.usernameSeiBlackboard\", csb.senhaSeiBlackboard as \"csb.senhaSeiBlackboard\", ");
		sql.append(" csb.clienteSeiBlackboard as \"csb.clienteSeiBlackboard\", csb.tokenSeiBlackboard as \"csb.tokenSeiBlackboard\", ");
		sql.append(" csb.urlExternaSeiBlackboard as \"csb.urlExternaSeiBlackboard\", ");
		sql.append(" csb.idBlackboard as \"csb.idBlackboard\", ");
		sql.append(" csb.urlExternaBlackboard as \"csb.urlExternaBlackboard\", ");
		sql.append(" csb.clientBlackboard as \"csb.clientBlackboard\", ");		
		sql.append(" csb.secretBlackboard as \"csb.secretBlackboard\", ");
		sql.append(" csb.ativarOperacoesDoBlackboard as \"csb.ativarOperacoesDoBlackboard\", ");
		sql.append(" csb.ativarOperacaoEnsalamentoEstagio as \"csb.ativarOperacaoEnsalamentoEstagio\", ");
		sql.append(" csb.ativarOperacaoEnsalamentoTCC as \"csb.ativarOperacaoEnsalamentoTCC\", ");
		sql.append(" csb.fonteDeDadosConteudoMasterBlackboard as \"csb.fonteDeDadosConteudoMasterBlackboard\", ");
		sql.append(" csb.importacaoEmRealizacao as \"csb.importacaoEmRealizacao\" ");
		sql.append(" FROM ConfiguracaoSeiBlackboard as csb");
		return sql;
	}
	
	private StringBuilder getSQLPadraoConsultaCompleta() {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" csb.codigo as \"csb.codigo\",  ");
		sql.append(" csb.usernameSeiBlackboard as \"csb.usernameSeiBlackboard\", csb.senhaSeiBlackboard as \"csb.senhaSeiBlackboard\", ");
		sql.append(" csb.clienteSeiBlackboard as \"csb.clienteSeiBlackboard\", csb.tokenSeiBlackboard as \"csb.tokenSeiBlackboard\", ");
		sql.append(" csb.urlExternaSeiBlackboard as \"csb.urlExternaSeiBlackboard\", ");
		sql.append(" csb.idBlackboard as \"csb.idBlackboard\", ");
		sql.append(" csb.urlExternaBlackboard as \"csb.urlExternaBlackboard\", ");
		sql.append(" csb.clientBlackboard as \"csb.clientBlackboard\", ");		
		sql.append(" csb.secretBlackboard as \"csb.secretBlackboard\", ");
		sql.append(" csb.ativarOperacoesDoBlackboard as \"csb.ativarOperacoesDoBlackboard\", ");
		sql.append(" csb.ativarOperacaoEnsalamentoEstagio as \"csb.ativarOperacaoEnsalamentoEstagio\", ");
		sql.append(" csb.ativarOperacaoEnsalamentoTCC as \"csb.ativarOperacaoEnsalamentoTCC\", ");
		sql.append(" csb.fonteDeDadosConteudoMasterBlackboard as \"csb.fonteDeDadosConteudoMasterBlackboard\", ");
		sql.append(" csb.importacaoEmRealizacao as \"csb.importacaoEmRealizacao\", ");
		sql.append(" csbd.codigo as \"csbd.codigo\",  ");		
		sql.append(" csbd.permissaosistema as \"csbd.permissaosistema\", ");
		sql.append(" csbd.permissaoinstitucional as \"csbd.permissaoinstitucional\", ");
		sql.append(" csbd.roleIdSistema as \"csbd.roleIdSistema\", ");
		sql.append(" csbd.roleIdInstitucional as \"csbd.roleIdInstitucional\", ");
		sql.append(" csbd.tipoUsuarioBlackboard as \"csbd.tipoUsuarioBlackboard\" ");
		
		sql.append(" FROM configuracaoSeiBlackboard as csb");
		sql.append(" left join  configuracaoSeiBlackboardDominio as csbd on csbd.configuracaoSeiBlackboard = csb.codigo ");
		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean consultarSeExisteConfiguracaoSeiBlackboardPadrao(UsuarioVO usuario) {
		StringBuilder sqlStr = new StringBuilder(" Select count(codigo) QTDE from ConfiguracaoSeiBlackboard ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoSeiBlackboardVO consultarConfiguracaoSeiBlackboardPadrao(int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" order by csb.codigo desc limit 1 ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				return new ConfiguracaoSeiBlackboardVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoSeiBlackboardVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
			sqlStr.append(" WHERE csb.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( ConfiguracaoSeiBlackboardVO ).");
			}
			tabelaResultado.beforeFirst();
			return (montarDadosCompleto(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private List<ConfiguracaoSeiBlackboardVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<ConfiguracaoSeiBlackboardVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private ConfiguracaoSeiBlackboardVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		ConfiguracaoSeiBlackboardVO obj = new ConfiguracaoSeiBlackboardVO();
		obj.setCodigo((dadosSQL.getInt("csb.codigo")));
		obj.setUsernameSeiBlackboard(dadosSQL.getString("csb.usernameSeiBlackboard"));
		obj.setSenhaSeiBlackboard(dadosSQL.getString("csb.senhaSeiBlackboard"));
		obj.setClienteSeiBlackboard(dadosSQL.getString("csb.clienteSeiBlackboard"));
		obj.setTokenSeiBlackboard(dadosSQL.getString("csb.tokenSeiBlackboard"));
		obj.setUrlExternaSeiBlackboard(dadosSQL.getString("csb.urlExternaSeiBlackboard"));
		obj.setClientBlackboard(dadosSQL.getString("csb.clientBlackboard"));
		obj.setSecretBlackboard(dadosSQL.getString("csb.secretBlackboard"));
		obj.setUrlExternaBlackboard(dadosSQL.getString("csb.urlExternaBlackboard"));
		obj.setIdBlackboard(dadosSQL.getString("csb.idBlackboard"));
		obj.setFonteDeDadosConteudoMasterBlackboard(dadosSQL.getString("csb.fonteDeDadosConteudoMasterBlackboard"));
		obj.setAtivarOperacoesDoBlackboard(dadosSQL.getBoolean("csb.ativarOperacoesDoBlackboard"));
		obj.setAtivarOperacaoEnsalamentoEstagio(dadosSQL.getBoolean("csb.ativarOperacaoEnsalamentoEstagio"));
		obj.setAtivarOperacaoEnsalamentoTCC(dadosSQL.getBoolean("csb.ativarOperacaoEnsalamentoTCC"));
		obj.setImportacaoEmRealizacao(dadosSQL.getBoolean("csb.importacaoEmRealizacao"));
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		
		return obj;
	}
	
	private ConfiguracaoSeiBlackboardVO montarDadosCompleto(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) {
		ConfiguracaoSeiBlackboardVO obj = new ConfiguracaoSeiBlackboardVO();
		while (rs.next()) {
			if(!Uteis.isAtributoPreenchido(obj.getCodigo())) {
				obj.setNovoObj(false);
				obj.setCodigo((rs.getInt("csb.codigo")));
				obj.setUsernameSeiBlackboard(rs.getString("csb.usernameSeiBlackboard"));
				obj.setSenhaSeiBlackboard(rs.getString("csb.senhaSeiBlackboard"));
				obj.setClienteSeiBlackboard(rs.getString("csb.clienteSeiBlackboard"));
				obj.setTokenSeiBlackboard(rs.getString("csb.tokenSeiBlackboard"));
				obj.setUrlExternaSeiBlackboard(rs.getString("csb.urlExternaSeiBlackboard"));
				obj.setClientBlackboard(rs.getString("csb.clientBlackboard"));
				obj.setSecretBlackboard(rs.getString("csb.secretBlackboard"));
				obj.setUrlExternaBlackboard(rs.getString("csb.urlExternaBlackboard"));
				obj.setIdBlackboard(rs.getString("csb.idBlackboard"));
				obj.setFonteDeDadosConteudoMasterBlackboard(rs.getString("csb.fonteDeDadosConteudoMasterBlackboard"));
				obj.setAtivarOperacoesDoBlackboard(rs.getBoolean("csb.ativarOperacoesDoBlackboard"));
				obj.setAtivarOperacaoEnsalamentoEstagio(rs.getBoolean("csb.ativarOperacaoEnsalamentoEstagio"));
				obj.setAtivarOperacaoEnsalamentoTCC(rs.getBoolean("csb.ativarOperacaoEnsalamentoTCC"));
				obj.setImportacaoEmRealizacao(rs.getBoolean("csb.importacaoEmRealizacao"));
			}
			if(Uteis.isAtributoPreenchido(rs.getInt("csbd.codigo"))) {
				ConfiguracaoSeiBlackboardDominioVO csbd = new ConfiguracaoSeiBlackboardDominioVO();
				csbd.setNovoObj(false);
				csbd.setCodigo((rs.getInt("csbd.codigo")));
				csbd.setConfiguracaoSeiBlackboardVO(obj);
				csbd.setTipoUsuarioBlackboard(CourseRoleIdEnum.valueOf(rs.getString("csbd.tipoUsuarioBlackboard")));				
				csbd.setPermissaoSistema(rs.getString("csbd.permissaoSistema"));
				csbd.setRoleIdSistema(rs.getString("csbd.roleIdSistema"));
				csbd.setPermissaoInstitucional(rs.getString("csbd.permissaoInstitucional"));
				csbd.setRolerIdInstitucional(rs.getString("csbd.roleIdInstitucional"));
				obj.getListaConfiguracaoSeiBlackboardDominioVO().add(csbd);	
			}
		}
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirImportacaoEmRealizacao(Integer conf, Boolean emRealizacao) {
		getConexao().getJdbcTemplate().update("update ConfiguracaoSeiBlackboard set importacaoEmRealizacao = ? where codigo = ? ", emRealizacao, conf);
	}
	
	

	

}
