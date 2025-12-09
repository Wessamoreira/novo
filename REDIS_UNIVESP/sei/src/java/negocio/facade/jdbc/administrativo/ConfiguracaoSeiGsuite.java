
package negocio.facade.jdbc.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle.MSG_TELA;
import jobs.enumeradores.JobsEnum;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteUnidadeEnsinoVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.gsuite.AdminSdkIntegracaoVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntrada;
import negocio.interfaces.administrativo.ConfiguracaoSeiGsuiteInterfaceFacade;
import webservice.arquitetura.InfoWSVO;
import webservice.boletoonline.itau.comuns.TokenVO;

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoSeiGsuite extends ControleAcesso implements ConfiguracaoSeiGsuiteInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4062768825367318538L;
	private static String idEntidade = "ConfiguracaoSeiGsuite";

	public static String getIdEntidade() {
		return ConfiguracaoSeiGsuite.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ConfiguracaoSeiGsuite.idEntidade = idEntidade;
	}
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDados(ConfiguracaoSeiGsuiteVO obj) {
		validarDadosSeiGSuite(obj);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDadosSeiGSuite(ConfiguracaoSeiGsuiteVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getClienteSeiGsuite()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_clienteSeiGsuite"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTokenSeiGsuite()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_tokenSeiGsuite"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUsuarioSeiGsuite()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_usuarioSeiGsuite"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSenhaSeiGsuite()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_senhaSeiGsuite"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUrlExternaSeiGsuite()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_urlExternaSeiGsuite"));
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDadosApiGoogle(ConfiguracaoSeiGsuiteVO obj)  {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getContaEmailGoogle()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_contaEmailGoogle"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getClienteGoogle()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_clienteGoogle"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTokenGoogle()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_tokenGoogle"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getRedirectUrlAplicacao()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_redirectUrlAplicacao"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDiretorioCredencialGoogle()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_diretorioCredencialGoogle"));
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ConfiguracaoSeiGsuiteVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) {
		try {
			ConfiguracaoSeiGsuite.incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "ConfiguracaoSeiGsuite", new AtributoPersistencia()
					.add("clienteSeiGsuite", obj.getClienteSeiGsuite())
					.add("tokenSeiGsuite", obj.getTokenSeiGsuite())
					.add("usuarioSeiGsuite", obj.getUsuarioSeiGsuite())
					.add("senhaSeiGsuite", obj.getSenhaSeiGsuite())
					.add("arquivoCredencialGoogle", obj.getArquivoCredencialGoogle())
					.add("contaEmailGoogle", obj.getContaEmailGoogle())
					.add("clienteGoogle", obj.getClienteGoogle())
					.add("tokenGoogle", obj.getTokenGoogle())
					.add("redirectUrlAplicacao", obj.getRedirectUrlAplicacao())
					.add("diretorioCredencialGoogle", obj.getDiretorioCredencialGoogle())
					.add("urlExternaSeiGsuite", obj.getUrlExternaSeiGsuite())
					.add("ativarOperacoesDoClassroom", obj.isAtivarOperacoesDoClassroom())
					.add("habilitarVerificacaoCalendarioClassroom", obj.isHabilitarVerificacaoCalendarioClassroom()),
					usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ConfiguracaoSeiGsuiteVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) {
		try {
			ConfiguracaoSeiGsuite.alterar(getIdEntidade(), verificarAcesso, usuario);
			if(!Uteis.isAtributoPreenchido(obj.getArquivoCredencialGoogle())) {
				validarArquivoGeradoCredencial(obj);
			}
			alterar(obj, "ConfiguracaoSeiGsuite", new AtributoPersistencia()
					.add("clienteSeiGsuite", obj.getClienteSeiGsuite())
					.add("tokenSeiGsuite", obj.getTokenSeiGsuite())
					.add("usuarioSeiGsuite", obj.getUsuarioSeiGsuite())
					.add("senhaSeiGsuite", obj.getSenhaSeiGsuite())
					.add("arquivoCredencialGoogle", obj.getArquivoCredencialGoogle())
					.add("contaEmailGoogle", obj.getContaEmailGoogle())
					.add("clienteGoogle", obj.getClienteGoogle())
					.add("tokenGoogle", obj.getTokenGoogle())
					.add("redirectUrlAplicacao", obj.getRedirectUrlAplicacao())
					.add("diretorioCredencialGoogle", obj.getDiretorioCredencialGoogle())
					.add("urlExternaSeiGsuite", obj.getUrlExternaSeiGsuite())
					.add("ativarOperacoesDoClassroom", obj.isAtivarOperacoesDoClassroom())
					.add("habilitarVerificacaoCalendarioClassroom", obj.isHabilitarVerificacaoCalendarioClassroom()),
					new AtributoPersistencia()
					.add("codigo", obj.getCodigo()), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoSeiGsuiteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntrada.excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM ConfiguracaoSeiGsuite WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persitir(ConfiguracaoSeiGsuiteVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		validarDados(obj);		
		if (!Uteis.isAtributoPreenchido(obj)) {
			incluir(obj, verificarAcesso, configuracaoGeralSistemaVO, usuario);
		} else {
			alterar(obj, verificarAcesso, configuracaoGeralSistemaVO, usuario);
		}
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO(), "configuracaoSeiGsuiteUnidadeEnsino", idEntidade, obj.getCodigo(), usuario);
		getFacadeFactory().getConfiguracaoSeiGsuiteUnidadeEnsinoFacade().persistir(obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO(), verificarAcesso, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void validarEnvioComunicadoInternoCriacaoContaGsuite(HttpResponse<JsonNode> response, UsuarioVO usuario) throws Exception {
		InfoWSVO rep = new Gson().fromJson(response.getBody().toString(), InfoWSVO.class);
		if(!rep.getCampos().isEmpty() && rep.getCampos().get(0).getCampo().equals(UteisWebServiceUrl.pessoagsuite)) {
			PessoaGsuiteVO pessoaGsuite = getFacadeFactory().getPessoaGsuiteFacade().consultarPorChavePrimaria(Integer.parseInt(rep.getCampos().get(0).getValor()), Uteis.NIVELMONTARDADOS_TODOS, usuario);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemGsuiteCriacaoConta(pessoaGsuite, usuario);	
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public TokenVO consultarTokenVO(ConfiguracaoSeiGsuiteVO obj) {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosSeiGSuite(obj);
			return TokenVO.autenticarBasicaTokenPorUnirest(obj.getUrlExternaSeiGsuite() + UteisWebServiceUrl.URL_GSUITE_TOKEN, 
					obj.getClienteSeiGsuite(),
					obj.getTokenSeiGsuite(),
					obj.getParametroBody());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<String> consultarStatusSeiGsuite(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario) {
		try {
			TokenVO token = consultarTokenVO(obj);			
			return Unirest.get(obj.getUrlExternaSeiGsuite() + UteisWebServiceUrl.URL_GSUITE_SERVICO_STATUS)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
					.asString();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<String> realizarRequestUrlCodeGoogle(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario) {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
			TokenVO token = consultarTokenVO(obj);			
			return Unirest.get(obj.getUrlExternaSeiGsuite() + UteisWebServiceUrl.URL_GSUITE_CREDENCIAL)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
					.asString();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<String> realizarProcessoUrlCodeGoogle(String code, UsuarioVO usuarioVO) {
		try {
			ConfiguracaoSeiGsuiteVO obj = consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
			TokenVO token = consultarTokenVO(obj);
			return Unirest.post(obj.getUrlExternaSeiGsuite() + UteisWebServiceUrl.URL_GSUITE_CREDENCIAL)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
					.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_X_WWW_FORM)
					.header(UteisWebServiceUrl.ul, usuarioVO.getCodigo().toString())
					.field("code", code)
					.asString();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<String> realizarExclusaoCredencialGoogle(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario) {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
			TokenVO token = consultarTokenVO(obj);			
			HttpResponse<String> response = Unirest.delete(obj.getUrlExternaSeiGsuite() + UteisWebServiceUrl.URL_GSUITE_CREDENCIAL)
					.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
					.header(UteisWebServiceUrl.ul, usuario.getCodigo().toString())
					.asString();
			obj.setArquivoCredencialGoogle(new ArquivoVO());
			return response;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarGeracaoDaContaGsuitePorMatricula(MatriculaVO matricula, UsuarioVO usuario) {
		try {			
			ConfiguracaoSeiGsuiteVO obj = consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuario);			
			if(Uteis.isAtributoPreenchido(obj)) {
				getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
				Map<String, String> headers = new HashMap<String, String>();
				headers.put(UteisWebServiceUrl.matricula, matricula.getMatricula());
				headers.put(UteisWebServiceUrl.unidadeEnsino, matricula.getUnidadeEnsino().getCodigo().toString());
				headers.put(UteisWebServiceUrl.pessoa, matricula.getAluno().getCodigo().toString());
				HttpResponse<JsonNode> jsonResponse  = unirestHeaders(obj, UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK + UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK_CONTA_MATRICULA, RequestMethod.POST, headers, usuario);
				validarEnvioComunicadoInternoCriacaoContaGsuite(jsonResponse, usuario);
				executarVerificacaoIncluirClassroomOperacao(matricula, usuario);
			}		
		} catch (Exception e) {
			/// metodo nao pode levantar excecão pois nao pode parar o processo de matricula
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_CRIAR_CONTAS_GSUITE.getName(), "Criar Matricula :"+matricula.getMatricula()+" - pessoa:" + matricula.getAluno().getCodigo()+ " - ue:" + matricula.getUnidadeEnsino().getCodigo() + " msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarExclusaoDaContaGsuitePorMatricula(MatriculaVO matricula, UsuarioVO usuario) {
		try {
			boolean existeOutraMatricula = getFacadeFactory().getMatriculaFacade().consultarExistenciaMatriculaPorCodigoPessoaPorUnidadeEnsinoDiferenteMatricula(matricula.getAluno().getCodigo(), matricula.getUnidadeEnsino().getCodigo(), matricula.getMatricula(), usuario);
			if(!existeOutraMatricula) {
				PessoaGsuiteVO pessoaGsuite = getFacadeFactory().getPessoaGsuiteFacade().consultarPorPessoaPorUnidadeEnsino(matricula.getAluno().getCodigo(), matricula.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
				if(Uteis.isAtributoPreenchido(pessoaGsuite) ) {
					ConfiguracaoSeiGsuiteVO obj = consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuario);
					if(Uteis.isAtributoPreenchido(obj)) {
						getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
						//realizarExclusaoPorPessoaGsuite(obj, pessoaGsuite, usuario);
						pessoaGsuite.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.INATIVO);
						realizarAlteracaoPorPessoaGsuite(obj, pessoaGsuite, usuario);
					}
				}	
			}
		} catch (Exception e) {
			/// metodo nao pode levantar excecão pois nao pode parar o processo de matricula
			try {
				RegistroExecucaoJobVO registroExecucaoJob =  new RegistroExecucaoJobVO(JobsEnum.JOB_CRIAR_CONTAS_GSUITE.getName(), "Deletar Matricula :"+matricula.getMatricula()+" - pessoa:" + matricula.getAluno().getCodigo()+ " - ue:" + matricula.getUnidadeEnsino().getCodigo() + " msg:" + e.getMessage());
				registroExecucaoJob.setDataTermino(new Date());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluir(registroExecucaoJob);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarVerificacaoIncluirClassroomOperacao(MatriculaVO matricula, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" INSERT INTO classroomoperacao (turma, turmapratica, turmateorica, disciplina, ano, semestre, emailgsuite, operacao, executada, codigoorigem, tipoorigem)");
		sql.append(" (SELECT   ");
		sql.append("	matriculaperiodoturmadisciplina.turma, ");
		sql.append("	matriculaperiodoturmadisciplina.turmapratica, ");
		sql.append("	matriculaperiodoturmadisciplina.turmateorica, ");
		sql.append("	matriculaperiodoturmadisciplina.disciplina, ");
		sql.append("	matriculaperiodoturmadisciplina.ano, ");
		sql.append("	matriculaperiodoturmadisciplina.semestre, ");
		sql.append("	pessoagsuite.email as emailgsuite, ");
		sql.append("	'INSERT' as operacao, ");
		sql.append("	false as executada, ");
		sql.append("	matriculaperiodoturmadisciplina.codigo as codigoorigem, ");
		sql.append("    'MATRICULA_PERIODO_TURMA_DISCIPLINA' as tipoorigem");
		sql.append(" from pessoagsuite");
		sql.append(" inner join pessoa on pessoa.codigo = pessoagsuite.pessoa");
		sql.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo and  matriculaperiodo.matricula =  matriculaperiodoturmadisciplina.matricula");
		sql.append(" inner join configuracaoseigsuiteunidadeensino on configuracaoseigsuiteunidadeensino.unidadeensino =  matricula.unidadeensino and  (( pessoa.aluno and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemail ||'%' )  or ( pessoa.funcionario and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemailfuncionario ||'%' ))");
		sql.append(" where matricula.matricula = '").append(matricula.getMatricula()).append("' ");
		sql.append(" and not exists ( ");
		sql.append("  select classroomoperacao.codigo ");
		sql.append("  from classroomoperacao ");
		sql.append("  where classroomoperacao.turma = matriculaperiodoturmadisciplina.turma ");
		sql.append("  and (classroomoperacao.turmapratica = matriculaperiodoturmadisciplina.turmapratica or (matriculaperiodoturmadisciplina.turmapratica is null and classroomoperacao.turmapratica is null)) ");
		sql.append("  and (classroomoperacao.turmateorica = matriculaperiodoturmadisciplina.turmateorica or (matriculaperiodoturmadisciplina.turmateorica is null and classroomoperacao.turmateorica is null)) ");
		sql.append("  and classroomoperacao.disciplina = matriculaperiodoturmadisciplina.disciplina ");
		sql.append("  and classroomoperacao.ano = matriculaperiodoturmadisciplina.ano ");
		sql.append("  and classroomoperacao.semestre = matriculaperiodoturmadisciplina.semestre ");
		sql.append("  and classroomoperacao.tipoorigem = 'MATRICULA_PERIODO_TURMA_DISCIPLINA' ");
		sql.append("  and classroomoperacao.codigoorigem = matriculaperiodoturmadisciplina.codigo ");
		sql.append("  and classroomoperacao.professor is null ");
		sql.append("  and classroomoperacao.operacao = 'INSERT' ");
		sql.append(" ))");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().execute(sql.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarGeracaoDaContaGsuitePorPessoa(ConfiguracaoSeiGsuiteVO obj, PessoaVO pessoa, UsuarioVO usuario) {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(UteisWebServiceUrl.pessoa, pessoa.getCodigo().toString());
			HttpResponse<JsonNode> jsonResponse  = unirestHeaders(obj, UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK + UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK_CONTA_INDIVIDUAL, RequestMethod.POST, headers,  usuario);
			validarEnvioComunicadoInternoCriacaoContaGsuite(jsonResponse, usuario);
			return jsonResponse;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarExclusaoPorPessoaGsuite(ConfiguracaoSeiGsuiteVO obj, PessoaGsuiteVO pessoaGsuite, UsuarioVO usuario) {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(UteisWebServiceUrl.pessoagsuite, pessoaGsuite.getCodigo().toString());
			return unirestHeaders(obj, UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK + UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK_CONTA_INDIVIDUAL, RequestMethod.DELETE, headers,  usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public HttpResponse<JsonNode> realizarAlteracaoPorPessoaGsuite(ConfiguracaoSeiGsuiteVO obj, PessoaGsuiteVO pessoaGsuite, UsuarioVO usuario) {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(UteisWebServiceUrl.pessoagsuite, pessoaGsuite.getCodigo().toString());
			headers.put(UteisWebServiceUrl.statuspessoagsuite, pessoaGsuite.getStatusAtivoInativoEnum().toString());
			return unirestHeaders(obj, UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK + UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK_CONTA_INDIVIDUAL, RequestMethod.PUT, headers,  usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarGeracaoUsuarioGsuiteLote(ConfiguracaoSeiGsuiteVO obj, AdminSdkIntegracaoVO adminSdkVO, UsuarioVO usuario) {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
			Uteis.checkState(!adminSdkVO.isCriarContaAluno() && !adminSdkVO.isCriarContaFuncionario(), "Uma das opções para criação de conta Aluno/Funcionário deve estar marcada.");
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(UteisWebServiceUrl.criarContaFuncionario, String.valueOf(adminSdkVO.isCriarContaFuncionario()));
			headers.put(UteisWebServiceUrl.criarContaAluno, String.valueOf(adminSdkVO.isCriarContaAluno()));
			return unirestHeaders(obj, UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK + UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK_CONTA_LOTE, RequestMethod.POST, headers,  adminSdkVO.getUsuarioVO());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarImportacaoUsuarioGsuiteLote(ConfiguracaoSeiGsuiteVO obj, AdminSdkIntegracaoVO adminSdkVO, UsuarioVO usuario) {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(UteisWebServiceUrl.criarContaFuncionario, String.valueOf(adminSdkVO.isCriarContaFuncionario()));
			headers.put(UteisWebServiceUrl.criarContaAluno, String.valueOf(adminSdkVO.isCriarContaAluno()));
			return unirestHeaders(obj, UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK + UteisWebServiceUrl.URL_GSUITE_ADMIN_SDK_IMPORTA_LOTE, RequestMethod.POST, headers,  adminSdkVO.getUsuarioVO());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public HttpResponse<JsonNode> realizarProcessamentoGoogleMeetLote(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario) {
		try {
			getFacadeFactory().getConfiguracaoSeiGsuiteFacade().validarDadosApiGoogle(obj);
			return unirestHeaders(obj, UteisWebServiceUrl.URL_GOOGLE_MEET + UteisWebServiceUrl.URL_GOOGLE_MEET_LOTE_NAO_PROCESSADO, RequestMethod.POST, null,  usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarTodasUnidadeEnsinoParaConfiguracaoSeiGsuiteUnidadeEnsino(ConfiguracaoSeiGsuiteVO obj, ConfiguracaoSeiGsuiteUnidadeEnsinoVO csgue, List<SelectItem> listaSelectItemUnidadeEnsino, UsuarioVO usuario) {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(csgue.getUnidadeEnsinoVO()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_unidadeEnsino"));
			for (SelectItem selectItem : listaSelectItemUnidadeEnsino) {
				if(selectItem.getValue() != null && !((Integer)selectItem.getValue()).equals(0) ) {
					ConfiguracaoSeiGsuiteUnidadeEnsinoVO novoCsgue = (ConfiguracaoSeiGsuiteUnidadeEnsinoVO) Uteis.clonar(csgue);
					novoCsgue.setCodigo(0);
					novoCsgue.getUnidadeEnsinoVO().setCodigo((Integer)selectItem.getValue());
					novoCsgue.getUnidadeEnsinoVO().setNome((String)selectItem.getLabel());
					adicionarConfiguracaoSeiGsuiteUnidadeEnsinoVO(obj, novoCsgue, usuario);
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarConfiguracaoSeiGsuiteUnidadeEnsinoVO(ConfiguracaoSeiGsuiteVO obj, ConfiguracaoSeiGsuiteUnidadeEnsinoVO csgue, UsuarioVO usuario) {
		try {
			csgue.setConfiguracaoSeiGsuiteVO(obj);
			getFacadeFactory().getConfiguracaoSeiGsuiteUnidadeEnsinoFacade().validarDados(csgue);
			if(Uteis.isAtributoPreenchido(csgue.getUnidadeEnsinoVO().getCodigo()) && !Uteis.isAtributoPreenchido(csgue.getUnidadeEnsinoVO().getNome())) {
				csgue.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(csgue.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));	
			}
			for (ConfiguracaoSeiGsuiteUnidadeEnsinoVO objExistente : obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO()) {
				if (objExistente.equalsCampoSelecaoLista(csgue)) {
					objExistente.setDominioEmail(csgue.getDominioEmail());
					objExistente.setDominioEmailFuncionario(csgue.getDominioEmailFuncionario());
					objExistente.setUnidadeOrganizacionalAluno(csgue.getUnidadeOrganizacionalAluno());
					objExistente.setUnidadeOrganizacionalFuncionario(csgue.getUnidadeOrganizacionalFuncionario());
					objExistente.setAlterarSenhaProximoLogin(csgue.isAlterarSenhaProximoLogin());
					objExistente.setFormaGeracaoEventoAulaOnLineGoogleMeet(csgue.getFormaGeracaoEventoAulaOnLineGoogleMeet());
					objExistente.setTipoGeracaoEmailGsuiteEnum(csgue.getTipoGeracaoEmailGsuiteEnum());
					objExistente.setEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada(csgue.getEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada());
					objExistente.setNotificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada(csgue.getNotificarAlunoEventoAulaOnLineGoogleMeetDiasAntesAulaProgramada());
					return;
				}
			}
			obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().add(csgue);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)	
	public void removerConfiguracaoSeiGsuiteUnidadeEnsinoVO(ConfiguracaoSeiGsuiteVO obj, ConfiguracaoSeiGsuiteUnidadeEnsinoVO csgue, UsuarioVO usuario) {
		Iterator<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> i = obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().iterator();
		while (i.hasNext()) {
			ConfiguracaoSeiGsuiteUnidadeEnsinoVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(csgue)) {
				i.remove();
				return;
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarAlteracaoDominioEmailPorLista(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario) {		
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getValorAlterarDominioEmail()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_dominioEmailAlterar"));
		Uteis.checkState(!obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream().anyMatch(ConfiguracaoSeiGsuiteUnidadeEnsinoVO::isSelecionado), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_nenhumaUnidadeOrganizacionalSelecionada"));
		obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream().filter(ConfiguracaoSeiGsuiteUnidadeEnsinoVO::isSelecionado).forEach(p-> p.setDominioEmail(obj.getValorAlterarDominioEmail()));
	}
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarAlteracaoDominioEmailFuncionarioPorLista(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario) {		
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getValorAlterarDominioEmailFuncionario()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_dominioEmailFuncionarioAlterar"));
		Uteis.checkState(!obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream().anyMatch(ConfiguracaoSeiGsuiteUnidadeEnsinoVO::isSelecionado), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_nenhumaUnidadeOrganizacionalSelecionada"));
		obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream().filter(ConfiguracaoSeiGsuiteUnidadeEnsinoVO::isSelecionado).forEach(p-> p.setDominioEmailFuncionario(obj.getValorAlterarDominioEmailFuncionario()));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarAlteracaoUnidadeOrganizacionalAlunoPorLista(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getValorAlterarUnidadeOrganizacionalAluno()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_unidadeOrganizacionalAlunoAlterar"));
		Uteis.checkState(!obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream().anyMatch(ConfiguracaoSeiGsuiteUnidadeEnsinoVO::isSelecionado), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_nenhumaUnidadeOrganizacionalSelecionada"));
		obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream().filter(ConfiguracaoSeiGsuiteUnidadeEnsinoVO::isSelecionado).forEach(p-> p.setUnidadeOrganizacionalAluno(obj.getValorAlterarUnidadeOrganizacionalAluno()));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarAlteracaoUnidadeOrganizacionalFuncionarioPorLista(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getValorAlterarUnidadeOrganizacionalFuncionario()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_unidadeOrganizacionalFuncionarioAlterar"));
		Uteis.checkState(!obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream().anyMatch(ConfiguracaoSeiGsuiteUnidadeEnsinoVO::isSelecionado), UteisJSF.internacionalizar("msg_ConfiguracaoSeiGsuite_nenhumaUnidadeOrganizacionalSelecionada"));
		obj.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream().filter(ConfiguracaoSeiGsuiteUnidadeEnsinoVO::isSelecionado).forEach(p-> p.setUnidadeOrganizacionalFuncionario(obj.getValorAlterarUnidadeOrganizacionalFuncionario()));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarDesvinculacaoPessoaGsuite(PessoaGsuiteVO pessoaGsuite, ConfiguracaoSeiGsuiteVO confSeiGsuiteVO, UsuarioVO usuario) throws Exception {
		pessoaGsuite.setPessoaVO(null);		
		pessoaGsuite.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.INATIVO);		
		getFacadeFactory().getPessoaGsuiteFacade().alterarAtualizacaoPessoaGsuiteImportada(pessoaGsuite, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarVinculoPessoaComPessoaGsuiteImportada(PessoaGsuiteVO pessoaGsuite, ConfiguracaoSeiGsuiteVO confSeiGsuiteVO, UsuarioVO usuario) throws Exception {
		if (pessoaGsuite.getPessoaVO().getFuncionario() || pessoaGsuite.getPessoaVO().getProfessor() || pessoaGsuite.getPessoaVO().getCoordenador()) {
			FuncionarioVO funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(pessoaGsuite.getPessoaVO().getCodigo(), false, usuario);
			Uteis.checkState(!Uteis.isAtributoPreenchido(funcionarioVO), "Não foi localizado uma Funcionário para a pessoa de código " + pessoaGsuite.getPessoaVO().getCodigo() + " - nome:" + pessoaGsuite.getPessoaVO().getNome() + ". Sendo assim não é possível gerar conta Gsuite. ");
			UnidadeEnsinoVO unidadeEnisnoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultaBasicaPorFuncionario(funcionarioVO, usuario);
			Uteis.checkState(!Uteis.isAtributoPreenchido(unidadeEnisnoVO), "Não foi localizado a Unidade Ensino do Funcionário para a pessoa de código " + pessoaGsuite.getPessoaVO().getCodigo() + " - nome:" + pessoaGsuite.getPessoaVO().getNome() + ". Sendo assim não é possível gerar conta Gsuite. ");
			boolean unidadeEnsinoConfigurada = confSeiGsuiteVO.getListaConfiguracaoSeiGsuiteUnidadeEnsinoVO().stream().anyMatch(p -> p.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnisnoVO.getCodigo()));
			Uteis.checkState(!unidadeEnsinoConfigurada, "A unidade de ensino informada no Funcionário da pessoa de código " + pessoaGsuite.getPessoaVO().getCodigo() + " - nome:" + pessoaGsuite.getPessoaVO().getNome() + " não esta configurada para gerar a conta do Gsuite.");
			
		} else if (pessoaGsuite.getPessoaVO().getAluno()) {
			List<ConfiguracaoSeiGsuiteUnidadeEnsinoVO> lista = getFacadeFactory().getConfiguracaoSeiGsuiteUnidadeEnsinoFacade().consultarPorMatriculaMaiorNivelEducacionalMaiorDataPorCodigoPessoa(pessoaGsuite.getPessoaVO().getCodigo(), usuario);
			Uteis.checkState(lista.isEmpty(), "A unidade de ensino informada na matrícula da pessoa de código "+ pessoaGsuite.getPessoaVO().getCodigo() +" - nome:"+pessoaGsuite.getPessoaVO().getNome() + " não esta configurada para gerar a conta do Gsuite.");
			Uteis.checkState(lista.stream().noneMatch(p-> pessoaGsuite.getEmail().contains(p.getDominioEmail())), "A unidade de ensino informada no Matricula da pessoa de código " + pessoaGsuite.getPessoaVO().getCodigo() + " - nome:" + pessoaGsuite.getPessoaVO().getNome() + " não esta configurada para gerar a conta do Gsuite.");
		}
		pessoaGsuite.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
		getFacadeFactory().getPessoaGsuiteFacade().alterarAtualizacaoPessoaGsuiteImportada(pessoaGsuite, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarProcessamentoLoteGoogleMeet(ConfiguracaoSeiGsuiteVO obj, UsuarioVO usuario) throws Exception {
		Map<String, String> headers = new HashMap<String, String>();
		unirestHeaders(obj, UteisWebServiceUrl.URL_GOOGLE_MEET + UteisWebServiceUrl.URL_GOOGLE_MEET_LOTE_NAO_PROCESSADO, RequestMethod.POST, headers,  usuario);
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, ConfiguracaoSeiGsuiteVO obj) {
		List<ConfiguracaoSeiGsuiteVO> objs = consultaRapidaPorFiltros(obj, dataModelo);
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(obj, dataModelo));
		dataModelo.setListaConsulta(objs);
	}

	private List<ConfiguracaoSeiGsuiteVO> consultaRapidaPorFiltros(ConfiguracaoSeiGsuiteVO obj, DataModelo dataModelo) {
		try {
			ConfiguracaoSeiGsuite.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1 = 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY configuracaoSeiGsuite.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private Integer consultarTotalPorFiltros(ConfiguracaoSeiGsuiteVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosParaConsulta(ConfiguracaoSeiGsuiteVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (dataModelo.getCampoConsulta().equals(ConfiguracaoSeiGsuiteVO.enumCampoConsultaConfiguracaoSeiGsuite.CODIGO.name())
				&& Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			sqlStr.append(" and configuracaoSeiGsuite.codigo = ? ");
			dataModelo.getListaFiltros().add(Integer.parseInt(dataModelo.getValorConsulta()));
		}else if (dataModelo.getCampoConsulta().equals(ConfiguracaoSeiGsuiteVO.enumCampoConsultaConfiguracaoSeiGsuite.CLIENTE_SEI_GSUITE.name())
				&& Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			sqlStr.append(" and configuracaoSeiGsuite.clienteSeiGsuite ilike (?) ");
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
		}else if (dataModelo.getCampoConsulta().equals(ConfiguracaoSeiGsuiteVO.enumCampoConsultaConfiguracaoSeiGsuite.CONTA_EMAIL_GOOGLE.name())
			&& Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			sqlStr.append(" and configuracaoSeiGsuite.contaEmailGoogle ilike (?) ");
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" configuracaoSeiGsuite.codigo as \"configuracaoSeiGsuite.codigo\",  ");
		sql.append(" configuracaoSeiGsuite.clienteSeiGsuite as \"configuracaoSeiGsuite.clienteSeiGsuite\", configuracaoSeiGsuite.tokenSeiGsuite as \"configuracaoSeiGsuite.tokenSeiGsuite\", ");
		sql.append(" configuracaoSeiGsuite.usuarioSeiGsuite as \"configuracaoSeiGsuite.usuarioSeiGsuite\", configuracaoSeiGsuite.senhaSeiGsuite as \"configuracaoSeiGsuite.senhaSeiGsuite\", ");
		sql.append(" configuracaoSeiGsuite.contaEmailGoogle as \"configuracaoSeiGsuite.contaEmailGoogle\", ");
		sql.append(" configuracaoSeiGsuite.clienteGoogle as \"configuracaoSeiGsuite.clienteGoogle\", configuracaoSeiGsuite.tokenGoogle as \"configuracaoSeiGsuite.tokenGoogle\", ");
		sql.append(" configuracaoSeiGsuite.urlExternaSeiGsuite as \"configuracaoSeiGsuite.urlExternaSeiGsuite\", ");		
		sql.append(" configuracaoSeiGsuite.redirectUrlAplicacao as \"configuracaoSeiGsuite.redirectUrlAplicacao\", ");		
		sql.append(" configuracaoSeiGsuite.diretorioCredencialGoogle as \"configuracaoSeiGsuite.diretorioCredencialGoogle\", ");
		sql.append(" configuracaoSeiGsuite.ativarOperacoesDoClassroom as \"configuracaoSeiGsuite.ativarOperacoesDoClassroom\", ");
		sql.append(" configuracaoSeiGsuite.habilitarVerificacaoCalendarioClassroom as \"configuracaoSeiGsuite.habilitarVerificacaoCalendarioClassroom\", ");
		sql.append(" arquivo.codigo as \"arquivo.codigo\" , arquivo.nome as \"arquivo.nome\", ");
		sql.append(" arquivo.descricao as \"arquivo.descricao\", arquivo.dataupload as \"arquivo.dataupload\", arquivo.dataDisponibilizacao as \"arquivo.dataDisponibilizacao\", ");
		sql.append(" arquivo.situacao as \"arquivo.situacao\", arquivo.extensao as \"arquivo.extensao\", ");
		sql.append(" arquivo.pastabasearquivo as \"arquivo.pastabasearquivo\", arquivo.descricaoArquivo as \"arquivo.descricaoArquivo\" ");
		sql.append(" FROM ConfiguracaoSeiGsuite ");
		sql.append(" left join arquivo on arquivo.codigo = configuracaoSeiGsuite.arquivoCredencialGoogle ");

		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean consultarSeExisteConfiguracaoSeiGsuitePadrao(UsuarioVO usuario) {
		StringBuilder sqlStr = new StringBuilder(" Select count(codigo) QTDE from configuracaoSeiGsuite ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoSeiGsuiteVO consultarConfiguracaoSeiGsuitePadrao(int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" order by configuracaoSeiGsuite.codigo desc limit 1 ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				return new ConfiguracaoSeiGsuiteVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoSeiGsuiteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE configuracaoSeiGsuite.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( ConfiguracaoSeiGsuiteVO ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	public ConfiguracaoSeiGsuiteVO consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);

		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" left join configuracaoseigsuiteunidadeensino on configuracaoseigsuiteunidadeensino.configuracaoseigsuite = configuracaoseigsuite.codigo");
		sqlStr.append(" WHERE configuracaoseigsuiteunidadeensino.unidadeensino = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoUnidadeEnsino);
		if (!tabelaResultado.next()) {
			return new ConfiguracaoSeiGsuiteVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
	}
	
	private void validarArquivoGeradoCredencial(ConfiguracaoSeiGsuiteVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" arquivo.codigo as \"arquivo.codigo\" , arquivo.nome as \"arquivo.nome\", ");
		sql.append(" arquivo.descricao as \"arquivo.descricao\", arquivo.dataupload as \"arquivo.dataupload\", arquivo.dataDisponibilizacao as \"arquivo.dataDisponibilizacao\", ");
		sql.append(" arquivo.situacao as \"arquivo.situacao\", arquivo.extensao as \"arquivo.extensao\", ");
		sql.append(" arquivo.pastabasearquivo as \"arquivo.pastabasearquivo\", arquivo.descricaoArquivo as \"arquivo.descricaoArquivo\" ");
		sql.append(" FROM ConfiguracaoSeiGsuite ");
		sql.append(" left join arquivo on arquivo.codigo = configuracaoSeiGsuite.arquivoCredencialGoogle ");
		sql.append(" where ConfiguracaoSeiGsuite.codigo = ? ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getCodigo());
		if (dadosSQL.next()) {
			obj.getArquivoCredencialGoogle().setCodigo(dadosSQL.getInt("arquivo.codigo"));
			obj.getArquivoCredencialGoogle().setNome(dadosSQL.getString("arquivo.nome"));
			obj.getArquivoCredencialGoogle().setDescricao(dadosSQL.getString("arquivo.descricao"));
			obj.getArquivoCredencialGoogle().setDescricaoArquivo(dadosSQL.getString("arquivo.descricaoArquivo"));
			obj.getArquivoCredencialGoogle().setExtensao(dadosSQL.getString("arquivo.extensao"));
			obj.getArquivoCredencialGoogle().setDataUpload(dadosSQL.getDate("arquivo.dataUpload"));
			obj.getArquivoCredencialGoogle().setDataDisponibilizacao(dadosSQL.getDate("arquivo.dataDisponibilizacao"));
			obj.getArquivoCredencialGoogle().setSituacao(dadosSQL.getString("arquivo.situacao"));
			obj.getArquivoCredencialGoogle().setPastaBaseArquivo(dadosSQL.getString("arquivo.pastabasearquivo"));
			obj.getArquivoCredencialGoogle().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.toString(dadosSQL.getString("arquivo.pastabasearquivo")));
		}
	}

	private StringBuilder getSQLPadraoConsultaTotalBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(configuracaoSeiGsuite.codigo) as QTDE FROM configuracaoSeiGsuite  ");
		sql.append(" left join arquivo on arquivo.codigo = configuracaoSeiGsuite.arquivoCredencialGoogle ");
		return sql;
	}

	private List<ConfiguracaoSeiGsuiteVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<ConfiguracaoSeiGsuiteVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private ConfiguracaoSeiGsuiteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		ConfiguracaoSeiGsuiteVO obj = new ConfiguracaoSeiGsuiteVO();
		obj.setCodigo((dadosSQL.getInt("configuracaoSeiGsuite.codigo")));
		obj.setClienteSeiGsuite(dadosSQL.getString("configuracaoSeiGsuite.clienteSeiGsuite"));
		obj.setTokenSeiGsuite(dadosSQL.getString("configuracaoSeiGsuite.tokenSeiGsuite"));
		obj.setUsuarioSeiGsuite(dadosSQL.getString("configuracaoSeiGsuite.usuarioSeiGsuite"));
		obj.setSenhaSeiGsuite(dadosSQL.getString("configuracaoSeiGsuite.senhaSeiGsuite"));
		obj.setContaEmailGoogle(dadosSQL.getString("configuracaoSeiGsuite.contaEmailGoogle"));
		obj.setClienteGoogle(dadosSQL.getString("configuracaoSeiGsuite.clienteGoogle"));
		obj.setTokenGoogle(dadosSQL.getString("configuracaoSeiGsuite.tokenGoogle"));
		obj.setDiretorioCredencialGoogle(dadosSQL.getString("configuracaoSeiGsuite.diretorioCredencialGoogle"));
		obj.setAtivarOperacoesDoClassroom(dadosSQL.getBoolean("configuracaoSeiGsuite.ativarOperacoesDoClassroom"));
		obj.setHabilitarVerificacaoCalendarioClassroom(dadosSQL.getBoolean("configuracaoSeiGsuite.habilitarVerificacaoCalendarioClassroom"));
		obj.setRedirectUrlAplicacao(dadosSQL.getString("configuracaoSeiGsuite.redirectUrlAplicacao"));
		obj.setUrlExternaSeiGsuite(dadosSQL.getString("configuracaoSeiGsuite.urlExternaSeiGsuite"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("arquivo.codigo"))) {
			obj.getArquivoCredencialGoogle().setCodigo(dadosSQL.getInt("arquivo.codigo"));
			obj.getArquivoCredencialGoogle().setNome(dadosSQL.getString("arquivo.nome"));
			obj.getArquivoCredencialGoogle().setDescricao(dadosSQL.getString("arquivo.descricao"));
			obj.getArquivoCredencialGoogle().setDescricaoArquivo(dadosSQL.getString("arquivo.descricaoArquivo"));
			obj.getArquivoCredencialGoogle().setExtensao(dadosSQL.getString("arquivo.extensao"));
			obj.getArquivoCredencialGoogle().setDataUpload(dadosSQL.getDate("arquivo.dataUpload"));
			obj.getArquivoCredencialGoogle().setDataDisponibilizacao(dadosSQL.getDate("arquivo.dataDisponibilizacao"));
			obj.getArquivoCredencialGoogle().setSituacao(dadosSQL.getString("arquivo.situacao"));
			obj.getArquivoCredencialGoogle().setPastaBaseArquivo(dadosSQL.getString("arquivo.pastabasearquivo"));
			obj.getArquivoCredencialGoogle().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.toString(dadosSQL.getString("arquivo.pastabasearquivo")));	
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setListaConfiguracaoSeiGsuiteUnidadeEnsinoVO(getFacadeFactory().getConfiguracaoSeiGsuiteUnidadeEnsinoFacade().consultarPorConfiguracaoSeiGsuiteVO(obj, nivelMontarDados, usuario));
		return obj;
	}
	
}
