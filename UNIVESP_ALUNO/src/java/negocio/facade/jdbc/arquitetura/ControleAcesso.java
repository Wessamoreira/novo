package negocio.facade.jdbc.arquitetura;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.DataModelo;
import kong.unirest.*;
import org.springframework.stereotype.Service;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.arquitetura.*;
import negocio.comuns.arquitetura.annotation.ColunaDaoGenerico;
import negocio.comuns.arquitetura.annotation.EntidadeDaoGenerico;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
//import negocio.comuns.contabil.FechamentoMesVO;
//import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.interfaces.arquitetura.ControleAcessoInterfaceFacade;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import webservice.arquitetura.InfoWSVO;
import webservice.boletoonline.itau.comuns.TokenVO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

/**
 * SuperClasse padrão para a classe <code>ControleAcesso</code>. Responsável por implementar características comuns relativas ao controle de acesso e a conexão com o banco de dados.
 */
@Service("controleAcesso")
@Primary
public  class ControleAcesso extends SuperFacadeJDBC implements ControleAcessoInterfaceFacade {
	
	private static final long serialVersionUID = 1L;

	/** PerfilAcesso objeto que contém as regras de permissão de acesso do usuário, para funcionalidade da aplicação. */
	// protected PerfilAcessoVO perfilAcesso = null;
	public final int qtdCaracteresValidacao = 2;
	public Monitoramento monitoramento;	
	protected final static String PERCENT = "%";
	
	private static UnirestInstance unirest = null;

	public String getDiretorioPastaWeb() {
		return diretorioPastaWeb;
	}

	public void setDiretorioPastaWeb(String diretorioPastaWeb) {
		this.diretorioPastaWeb = diretorioPastaWeb;
	}

	/** Creates a new instance of Funcionalidade */
	public ControleAcesso() {
	}

	public static void imprimir() throws Exception {
		Monitoramento.imprimirObjeto();
	}
	
	

	/**
	 * Operação padrão para realizar o INCLUIR de dados de uma entidade no BD. Verifica e inicializa (se necessário) a conexão com o BD. Verifica se o usuário logado possui permissão de acesso a operação INCLUIR.
	 * 
	 * @param idEntidade
	 *            Nome da entidade para a qual se deseja realizar a operação.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean isFuncionalidadePerfilAcessoDoUsuario(String idEntidade, UsuarioVO usuario) throws Exception {
		return verificarPermissaoUsuarioComUsuarioEspecifico(idEntidade,  Uteis.INCLUIR, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public  void incluir(String idEntidade, UsuarioVO usuario) throws Exception {
		incluir(idEntidade, true, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public  void incluir(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		Monitoramento.criarMonitoramentoVO(idEntidade, "INCLUIR", usuario);
		if (!verificarAcesso) {
			return;
		}
		/**
		 * @author Leonardo Riciolle Comentado 29/10/2014
		 */
		// SuperFacadeJDBC.incluir(idEntidade);
		if (!verificarPermissaoUsuario(idEntidade, Uteis.INCLUIR, usuario)) {
			String nomeUsuario = "";
			if (usuario != null) {
				nomeUsuario = " " + usuario.getNome().toUpperCase();
			}
			String msgErro = "USUÁRIO " + nomeUsuario + " não possui permissão para INCLUIR em " + tratarTituloPermissao(idEntidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}

	/**
	 * Operação padrão para realizar o ALTERAR de dados de uma entidade no BD. Verifica e inicializa (se necessário) a conexão com o BD. Verifica se o usuário logado possui permissão de acesso a operação ALTERAR.
	 * 
	 * @param idEntidade
	 *            Nome da entidade para a qual se deseja realizar a operação.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	public  void alterar(String idEntidade, UsuarioVO usuario) throws Exception {
		alterar(idEntidade, true, usuario);
	}

	public  void alterar(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		Monitoramento.criarMonitoramentoVO(idEntidade, "ALTERAR", usuario);
		if (!verificarAcesso) {
			return;
		}
		/**
		 * @author Leonardo Riciolle Comentado 29/10/2014
		 */
		// SuperFacadeJDBC.alterar(idEntidade);
		if (!verificarPermissaoUsuario(idEntidade, Uteis.ALTERAR, usuario)) {
			String nomeUsuario = "";
			if (usuario != null) {
				nomeUsuario = " " + usuario.getNome().toUpperCase();
			}
			// if (obterUsuarioLogado() != null) {
			// nomeUsuario = " " + obterUsuarioLogado().getNome().toUpperCase();
			// }
			String msgErro = "USUÁRIO " + nomeUsuario + " não possui permissão para ALTERAR em " + tratarTituloPermissao(idEntidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}

	/**
	 * Operação padrão para realizar o EXCLUIR de dados de uma entidade no BD. Verifica e inicializa (se necessário) a conexão com o BD. Verifica se o usuário logado possui permissão de acesso a operação EXCLUIR.
	 * 
	 * @param idEntidade
	 *            Nome da entidade para a qual se deseja realizar a operação.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	public  void excluir(String idEntidade, UsuarioVO usuario) throws Exception {
		excluir(idEntidade, true, usuario);
	}

	public  void excluir(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		Monitoramento.criarMonitoramentoVO(idEntidade, "EXCLUIR", usuario);
		if (!verificarAcesso) {
			return;
		}
		/**
		 * @author Leonardo Riciolle Comentado 29/10/2014
		 */
		// SuperFacadeJDBC.excluir(idEntidade);
		if (!verificarPermissaoUsuario(idEntidade, Uteis.EXCLUIR, usuario)) {
			String nomeUsuario = "";
			if (usuario != null) {
				nomeUsuario = " " + usuario.getNome().toUpperCase();
			}
			// if (obterUsuarioLogado() != null) {
			// nomeUsuario = " " + obterUsuarioLogado().getNome().toUpperCase();
			// }
			String msgErro = "USUÁRIO " + nomeUsuario + " não possui permissão para EXCLUIR em " + tratarTituloPermissao(idEntidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}

	/**
	 * Operação padrão para realizar o CONSULTAR de dados de uma entidade no BD. Verifica e inicializa (se necessário) a conexão com o BD. Verifica se o usuário logado possui permissão de acesso a operação CONSULTAR.
	 * 
	 * @param idEntidade
	 *            Nome da entidade para a qual se deseja realizar a operação.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	public  void consultar(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		// Monitoramento.criarMonitoramentoVO("CONSULTAR", usuario);
		SuperFacadeJDBC.consultar(idEntidade);
		if (!verificarAcesso) {
			return;
		}
		if (!verificarPermissaoUsuario(idEntidade, Uteis.CONSULTAR, usuario)) {
			String nomeUsuario = "";
			if (usuario != null) {
				nomeUsuario = " " + usuario.getNome().toUpperCase();
			}
			String msgErro = "USUÁRIO " + nomeUsuario + " não possui permissão para CONSULTAR em " + tratarTituloPermissao(idEntidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}

	public  Boolean verificarAcessoMenu(String idEntidade, UsuarioVO usuario) throws Exception {
		return verificarPermissaoOperacao(usuario.getPerfilAcesso().getCodigo(), idEntidade, null);
	}

	public  Boolean verificarAcessoMenuUnificaoPerfil(String idEntidade, UsuarioVO usuario) throws Exception {
		return verificarPermissaoOperacaoUnificacaoPerfil(usuario.getCodigo(), idEntidade, null);
	}

	/**
	 * Operação padrão para realizar o EMITIR UM RELATÓRIO de dados de uma entidade no BD. Verifica e inicializa (se necessário) a conexão com o BD. Verifica se o usuário logado possui permissão de acesso a operação EMITIRRELATORIO.
	 * 
	 * @param idEntidade
	 *            Nome da entidade para a qual se deseja realizar a operação.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	public  void emitirRelatorio(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		Monitoramento.criarMonitoramentoVO(idEntidade, "EMITIRRELATORIO", usuario);
		SuperFacadeJDBC.emitirRelatorio(idEntidade);
		if (!verificarAcesso) {
			return;
		}
		if (!verificarPermissaoUsuario(idEntidade, Uteis.EMITIRRELATORIO, usuario)) {
			String nomeUsuario = "";
			if (usuario != null) {
				nomeUsuario = " " + usuario.getNome().toUpperCase();
			}
			// if (obterUsuarioLogado() != null) {
			// nomeUsuario = " " + obterUsuarioLogado().getNome().toUpperCase();
			// }
			String msgErro = "USUÁRIO " + nomeUsuario + " não possui permissão para EMITIR RELATÓRIO em " + tratarTituloPermissao(idEntidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}

	/**
	 * Operação padrão para verificar acesso do usuário a determinada funcionalidade registrada no Perfil de Acesso do Usuário. Pode ser, por exemplo, alterar um valor de um campo. Neste caso deverá existir no PerfilAcesso do usuário o key (apelido) identificando esta funcionalidade. Esta funcinalidade deverá estar gravada com a opção TOTAL ou ALTERAR para garantir que o usuário tenha acesso à mesma.
	 * 
	 * @param funcionalidade
	 *            Nome da funcionalidade para a qual se deseja realizar a verificação de permissão do usuário.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	@Override
	public void verificarPermissaoUsuarioFuncionalidade(UsuarioVO usuario, String funcionalidade) throws Exception {
		// SuperFacadeJDBC.verificarPermissaoUsuarioFuncionalidade(funcionalidade);
		if (!verificarPermissaoUsuario(funcionalidade, Uteis.ALTERAR, usuario)) {
			String nomeUsuario = "";
			if (usuario != null) {
				nomeUsuario = " " + usuario.getNome().toUpperCase();
			}
			String msgErro = "USUÁRIO " + nomeUsuario + " não possui permissão para a funcionalidade " + tratarTituloPermissao(funcionalidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}
	

	@Override
	public Boolean verificarPermissaoUsuarioFuncionalidade(String funcionalidade, UsuarioVO usuario) throws Exception {
		return verificarPermissaoFuncionalidadeComUsuarioEspecifico(funcionalidade, Uteis.ALTERAR, usuario);
	}

	public  void verificarPermissaoUsuarioFuncionalidadeComUsuarioVO(String funcionalidade, UsuarioVO usuario) throws Exception {
		// SuperFacadeJDBC.verificarPermissaoUsuarioFuncionalidade(funcionalidade);
		if (!verificarPermissaoUsuarioComUsuario(funcionalidade, Uteis.ALTERAR, usuario)) {
			String nomeUsuario = "";
			nomeUsuario = " " + usuario.getNome().toUpperCase();
			String msgErro = "USUÁRIO " + nomeUsuario + " não possui permissão para a funcionalidade " + tratarTituloPermissao(funcionalidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}

	public  void verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(String funcionalidade, UsuarioVO usuario) throws Exception {
		// SuperFacadeJDBC.verificarPermissaoUsuarioFuncionalidade(funcionalidade);
		if (!verificarPermissaoUsuarioComUsuarioEspecifico(funcionalidade, Uteis.ALTERAR, usuario)) {
			String nomeUsuario = "";
			nomeUsuario = " " + usuario.getNome().toUpperCase();
			String msgErro = "USUÁRIO " + nomeUsuario + " não possui permissão para a funcionalidade " + tratarTituloPermissao(funcionalidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}

	public  void verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(String funcionalidade, UsuarioVO usuario) throws Exception {
		// SuperFacadeJDBC.verificarPermissaoUsuarioFuncionalidade(funcionalidade);
		if (!verificarPermissaoFuncionalidadeComUsuarioEspecifico(funcionalidade, Uteis.ALTERAR, usuario)) {
			String nomeUsuario = "";
			nomeUsuario = " " + usuario.getNome().toUpperCase();
			String msgErro = "USUÁRIO " + nomeUsuario + " não possui permissão para a funcionalidade " + tratarTituloPermissao(funcionalidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}
	
	public  void verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoEnumInterface perfilAcessoEnum, UsuarioVO usuario) throws AcessoException {
		try {
			if (!verificarPermissaoFuncionalidadeComUsuarioEspecifico(perfilAcessoEnum.getValor(), Uteis.ALTERAR, usuario)) {
				String msgErro = "USUÁRIO " + usuario.getNome().toUpperCase() + " não possui permissão para a funcionalidade " + perfilAcessoEnum.getDescricaoVisao(usuario.getTipoVisaoAcesso()).toUpperCase();
				throw (new AcessoException(msgErro));
			}
		} catch (Exception e) {
			throw new AcessoException(e.getMessage());
		}
	}

	public  Boolean verificarPermissaoFuncionalidadeUsuario(String funcionalidade, UsuarioVO usuario) throws Exception {
		return verificarPermissaoFuncionalidadeComUsuarioEspecifico(funcionalidade, Uteis.ALTERAR, usuario);
	}

	/**
	 * Operação de responsável por verificar se uma operação está definida dentro de um objeto de <code>PermissaoVO</code>. Caso o codigo da operação (parâmetro <code>int operacao</code>) exista no atributo <code>permissoes</code> de <code>PermissaoVO</code> significa que existe acesso liberado para esta operação. O atributo permissoes assume valores do tipo "(1)(2)(4)", ou seja, os códigos da operações pertinentes cercados por parênteses.
	 * 
	 * @param permissaoVO
	 *            objeto contendo as permissões para uma determinada entidade, de um perfil de acesso específico.
	 * @param operacao
	 *            código de uma operação para qual se deseja realizar esta verificação.
	 * @return boolean true caso exista liberação para uso da operação neste perfil de acesso.
	 */
	// private static boolean verificarPermissaoOperacao(PermissaoVO permissaoVO, int operacao) {
	// String operStr = "(" + operacao + ")";
	// if (permissaoVO == null) {
	// return false;
	// }
	// int resultado = permissaoVO.getPermissoes().indexOf(operStr);
	// if (resultado == -1) {
	// return false;
	// } else {
	// return true;
	// }
	// }
	/**
	 * Operação responsável por verificar se uma operação está definida dentro de um conjunto de <code>PermissaoVO</code>, mantidos em um List. Isto para uma determinada entidade. Com base no nome da entidade (key do List) é possível obter o objeto da classe <code>PermissaoVO</code> pertinente a esta entidade. Para que posteriormente seja verificado o acesso a operação através da operação </code>verificarPermissaoOperacao(PermissaoVO permissao, int operacao)</code>.
	 * 
	 * @param permissoes
	 *            Todos os objetos PermissaoVO do perfil de acesso em questão.
	 * @param nomeEntidade
	 *            Código de uma operação para qual se deseja realizar esta verificação.
	 * @param operacao
	 *            Código de uma operação para qual se deseja realizar esta verificação.
	 * @return boolean true caso exista liberação para uso da operação neste perfil de acesso.
	 */
	public  boolean verificarPermissaoOperacao(Integer perfilAcesso, String nomeEntidade, Integer operacao)  {
		AplicacaoControle aplicacaoControle = null;
    	if(getSpringUtil().getApplicationContext() == null) {
    			aplicacaoControle = new AplicacaoControle();
    			aplicacaoControle.setFacadeFactory(getFacadeFactory());
    	}else {
    		aplicacaoControle = (AplicacaoControle) getSpringUtil().getApplicationContext().getBean(AplicacaoControle.class);
    	}
		if(aplicacaoControle != null) {
			PerfilAcessoVO perfilAcessoVO;
			try {
				perfilAcessoVO = aplicacaoControle.getPerfilAcessoVO(perfilAcesso, null);
				if(perfilAcesso != null) {
					return perfilAcessoVO.getPermissaoVOs().stream().anyMatch(t -> t.getNomeEntidade().equalsIgnoreCase(nomeEntidade) && t.getPermissoes().contains(operacao == null ? "" : getOperacaoStr(operacao)));
				}else {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}else {
			String operStr = null;
			if (operacao == null) {
				operStr = "";
			} else {
				operStr = getOperacaoStr(operacao);
			}
			StringBuilder sb = new StringBuilder("select nomeEntidade from permissao where nomeEntidade = ? and codperfilAcesso = ? and permissoes like ('%" + operStr + "%')");
			try {
				return getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), nomeEntidade, perfilAcesso).next();
			} finally {
				operStr = null;
				sb = null;
			}
		}
		// for (PermissaoVO obj : permissoes) {
		// if (obj.getNomeEntidade().equals(nomeEntidade)) {
		// return (verificarPermissaoOperacao(obj, operacao));
		// }
		// }
		// return false;
	}

	public  boolean verificarPermissaoOperacao(String nomeEntidade, Integer operacao, UsuarioVO usuarioVO) {
		try {
			return verificarPermissaoFuncionalidadeComUsuarioEspecifico(nomeEntidade, operacao, usuarioVO);
		} catch (Exception e) {
			return false;
		}
	}

	public  boolean verificarPermissaoOperacaoUnificacaoPerfil(Integer usuario, String nomeEntidade, Integer operacao) {
		String operStr = null;
		if (operacao == null) {
			operStr = "";
		} else {
			operStr = getOperacaoStr(operacao);
		}
		StringBuilder sb = new StringBuilder("select nomeEntidade from permissao where nomeEntidade = ? and codperfilAcesso in(select perfilacesso from usuarioperfilacesso where usuario = ?) and permissoes like ('%" + operStr + "%')");
		try {
			return getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), nomeEntidade, usuario).next();
		} finally {
			operStr = null;
			sb = null;
		}
	}

	private static String getOperacaoStr(int codOper) {
		return "(" + codOper + ")";
	}

	/**
	 * Operação responsável por verificar se um usuário existe (através de ser Username e Senha) e retornar os dados deste usuário para registro na sessão da aplicação. Caso o usuário não exista é retornado um Exception. da operação </code>verificarPermissaoOperacao(PermissaoVO permissao, int operacao)</code>.
	 * 
	 * @param username
	 *            Username do usuário.
	 * @param senha
	 *            Senha do usuário.
	 * @return UsuarioVO Dados do usuário localizado.
	 * @exception Exception
	 *                Erro alertando que o usuário não existe.
	 */	
	public  UsuarioVO verificarLoginUsuario(String username, String senha, boolean encriptar, int nivelMontarDados) throws Exception {

		StringBuilder sqlStr = getSQLConsultaCompletaUsuario().append(" WHERE ((username = ?) AND (senha = ?))");
		SqlRowSet tabelaResultado = null;
		UsuarioVO user = null;
		String senhaDescriptografada = senha;
		try {
			if (encriptar) {
				senha = Uteis.encriptar(senha);
			}
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { username, senha });
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Usuário e/ou senha incorretos!");
			}

			user = montarDadosUsuarioLogado(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
			// if (user.getPessoa().getCodigo() > 0 && user.getPessoa().getAtivo() == false) {
			// throw new ConsistirException("Usuário Inativo");
			// }
			
			if(!Uteis.isAtributoPreenchido(user.getSenhaSHA()) || !Uteis.isAtributoPreenchido(user.getSenhaMSCHAPV2())) {
				getFacadeFactory().getUsuarioFacade().alterarSenhaShaSenhaMSCHAPV2(senhaDescriptografada, user);
			}
			
			return user;
		} catch (Exception e) {
			Uteis.removerObjetoMemoria(user);
			throw e;
		} finally {
			tabelaResultado = null;
			sqlStr = null;
		}
	}

	public  UsuarioVO verificarLoginUsuarioEmailInstitucional(String emailInstitucional, int nivelMontarDados) throws Exception {
		StringBuilder sqlStr = getSQLConsultaCompletaUsuario().append(" WHERE pessoaemailinstitucional.email = ? ").append(" AND pessoaemailinstitucional.statusAtivoInativoEnum = 'ATIVO' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{emailInstitucional});
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Usuário e/ou senha incorretos!");
		}
		return montarDadosUsuarioLogado(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
	}
	
	public  UsuarioVO verificarLoginAdministrador(String username, String senha, boolean encriptar) throws Exception {
		String sql = "select codigo, nome, username from usuario where administrador and username = ? AND senha = ?";
		SqlRowSet tabelaResultado = null;
		UsuarioVO user = null;
		try {
			if (encriptar) {
				senha = Uteis.encriptar(senha);
			}
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { username, senha });
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Usuário e/ou senha incorretos!");
			} else {
				user = new UsuarioVO();
				user.setCodigo(tabelaResultado.getInt("codigo"));
				user.setNome(tabelaResultado.getString("nome"));
				user.setUsername(tabelaResultado.getString("username"));
				return user;
			}
		} catch (Exception e) {
			Uteis.removerObjetoMemoria(user);
			throw e;
		} finally {
			tabelaResultado = null;
			sql = null;
		}
	}

	public  UsuarioVO verificarLoginUsuarioSimulacaoSuporte(String username, String senha, boolean encriptar, int nivelMontarDados) throws Exception {

		StringBuilder sqlStr = getSQLConsultaCompletaUsuario().append(" WHERE ((username = ?) AND (senha = ?))");
		SqlRowSet tabelaResultado = null;
		UsuarioVO user = null;
		try {
			if (!senha.equals("") && senha.length() < 64) {
				senha = Uteis.encriptar(senha);
			}
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { username, senha });
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Usuário e/ou senha incorretos!");
			}

			user = montarDadosUsuarioLogado(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
			// if (user.getPessoa().getCodigo() > 0 && user.getPessoa().getAtivo() == false) {
			// throw new ConsistirException("Usuário Inativo");
			// }
			return user;
		} catch (Exception e) {
			Uteis.removerObjetoMemoria(user);
			throw e;
		} finally {
			tabelaResultado = null;
			sqlStr = null;
		}
	}

	public  UsuarioVO verificarLoginUsuarioSimulacaoVisaoAluno(Integer codigoPessoa, Boolean exceptionUsuarioNaoExistente) throws Exception {

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select usuario.username as \"usuario.username\" , usuario.senha as \"usuario.senha\" ");
		sqlStr.append(" from usuario ");
		sqlStr.append(" WHERE (usuario.pessoa = ? )");
		SqlRowSet tabelaResultado = null;
		try {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoPessoa });
			if (!tabelaResultado.next()) {
				throw new ConsistirException(exceptionUsuarioNaoExistente ? "Não existe Usuário criado/vinculado para essa pessoa!" : "Usuário e/ou senha incorretos!");
			}
			UsuarioVO obj = new UsuarioVO();
			obj.setUsername(tabelaResultado.getString("usuario.username"));
			obj.setSenha(tabelaResultado.getString("usuario.senha"));
			return obj;
		} catch (Exception e) {
			throw e;
		} finally {
			tabelaResultado = null;
			sqlStr = null;
		}
	}

	public  UsuarioVO montarDadosUsuarioLogado(SqlRowSet dadosSQL, int nivelMontarDados) {
		UsuarioVO obj = new UsuarioVO();
		obj.setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.setNome(dadosSQL.getString("usuario.nome"));
		obj.setUsername(dadosSQL.getString("usuario.username"));
		obj.setTipoUsuario(dadosSQL.getString("usuario.tipoUsuario"));
		obj.setDataUltimoAcesso(dadosSQL.getTimestamp("usuario.dataUltimoAcesso"));
		obj.setResetouSenhaPrimeiroAcesso(dadosSQL.getBoolean("usuario.resetouSenhaPrimeiroAcesso"));
		obj.setDataPrimeiroAcesso(dadosSQL.getDate("usuario.dataPrimeiroAcesso"));
		obj.setSenha(dadosSQL.getString("usuario.senha"));
		obj.setSenhaSHA(dadosSQL.getString("usuario.senhaSHA"));
		obj.setSenhaMSCHAPV2(dadosSQL.getString("usuario.senhaMSCHAPV2"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("usuario.pessoa"));
		obj.setPerfilAdministrador(dadosSQL.getBoolean("usuario.perfilAdministrador"));
		obj.setSolicitarNovaSenha(dadosSQL.getBoolean("usuario.solicitarnovasenha"));
		obj.setPossuiCadastroLdap(dadosSQL.getBoolean("usuario.possuiCadastroLdap"));
		obj.setQtdFalhaLogin(dadosSQL.getInt("usuario.qtdFalhaLogin"));
		obj.setDataFalhaLogin(dadosSQL.getTimestamp("usuario.dataFalhaLogin"));
		obj.setUsuarioBloqPorFalhaLogin(dadosSQL.getBoolean("usuario.usuarioBloqPorFalhaLogin"));
		obj.setNovoObj(Boolean.FALSE);
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getPessoa().setCurriculoAtualizado(dadosSQL.getBoolean("pessoa.curriculoAtualizado"));
		obj.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getPessoa().setCPF(dadosSQL.getString("pessoa.CPF"));
		obj.getPessoa().setRG(dadosSQL.getString("pessoa.RG"));
		obj.getPessoa().setValorCssTopoLogo(dadosSQL.getString("pessoa.valorCssTopoLogo"));
		obj.getPessoa().setValorCssBackground(dadosSQL.getString("pessoa.valorCssBackground"));
		obj.getPessoa().setValorCssMenu(dadosSQL.getString("pessoa.valorCssMenu"));
		obj.getPessoa().setSexo(dadosSQL.getString("pessoa.sexo"));
		obj.getPessoa().setEstadoCivil(dadosSQL.getString("pessoa.estadoCivil"));
		obj.getPessoa().setTelefoneRes(dadosSQL.getString("pessoa.telefoneRes"));
		obj.getPessoa().setTelefoneRecado(dadosSQL.getString("pessoa.telefoneRecado"));
		obj.getPessoa().setCelular(dadosSQL.getString("pessoa.celular"));
		obj.getPessoa().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getPessoa().setEmail2(dadosSQL.getString("pessoa.email2"));
		obj.getPessoa().setDataNasc(dadosSQL.getDate("pessoa.dataNasc"));
		obj.getPessoa().setCorRaca(dadosSQL.getString("pessoa.corraca"));
		obj.getPessoa().setPaginaPessoal(dadosSQL.getString("pessoa.paginaPessoal"));
		obj.getPessoa().setEndereco(dadosSQL.getString("pessoa.endereco"));
		obj.getPessoa().setSetor(dadosSQL.getString("pessoa.setor"));
		obj.getPessoa().setNumero(dadosSQL.getString("pessoa.numero"));
		obj.getPessoa().setCEP(dadosSQL.getString("pessoa.CEP"));
		obj.getPessoa().setComplemento(dadosSQL.getString("pessoa.complemento"));
		obj.getPessoa().setCertificadoMilitar(dadosSQL.getString("pessoa.certificadoMilitar"));
		obj.getPessoa().setDataEmissaoRG(dadosSQL.getDate("pessoa.dataEmissaoRG"));
		obj.getPessoa().setEstadoEmissaoRG(dadosSQL.getString("pessoa.estadoEmissaoRG"));
		obj.getPessoa().setOrgaoEmissor(dadosSQL.getString("pessoa.orgaoEmissor"));
		obj.getPessoa().setTituloEleitoral(dadosSQL.getString("pessoa.tituloEleitoral"));
		obj.getPessoa().setNecessidadesEspeciais(dadosSQL.getString("pessoa.necessidadesEspeciais"));
		obj.getPessoa().setTipoNecessidadesEspeciais(dadosSQL.getString("pessoa.tipoNecessidadesEspeciais"));
		obj.getPessoa().setProfessor(dadosSQL.getBoolean("pessoa.professor"));
		obj.getPessoa().setAluno(dadosSQL.getBoolean("pessoa.aluno"));
		obj.getPessoa().setPossuiAcessoVisaoPais(dadosSQL.getBoolean("pessoa.possuiAcessoVisaoPais"));
		obj.getPessoa().setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
		obj.getPessoa().setCandidato(dadosSQL.getBoolean("pessoa.candidato"));
		obj.getPessoa().setMembroComunidade(dadosSQL.getBoolean("pessoa.membroComunidade"));
		obj.getPessoa().setAtuaComoDocente(dadosSQL.getString("pessoa.atuaComoDocente"));
		obj.getPessoa().setAtivo(dadosSQL.getBoolean("pessoa.ativo"));
		obj.getPessoa().setIdAlunoInep(dadosSQL.getString("pessoa.idalunoinep"));
		obj.getPessoa().setPassaporte(dadosSQL.getString("pessoa.passaporte"));
		obj.getPessoa().setDeficiencia(dadosSQL.getString("pessoa.deficiencia"));
		obj.getPessoa().setNomeFiador(dadosSQL.getString("pessoa.nomeFiador"));
		obj.getPessoa().setCpfFiador(dadosSQL.getString("pessoa.cpfFiador"));
		obj.getPessoa().setEnderecoFiador(dadosSQL.getString("pessoa.enderecoFiador"));
		obj.getPessoa().setTelefoneFiador(dadosSQL.getString("pessoa.telefoneFiador"));
		obj.getPessoa().setCelularFiador(dadosSQL.getString("pessoa.celularFiador"));
		obj.getPessoa().setCoordenador(dadosSQL.getBoolean("pessoa.coordenador"));
		obj.getPessoa().setOcultarDadosCRM(dadosSQL.getBoolean("pessoa.ocultarDadosCRM"));
		obj.getPessoa().setParticipaBancoCurriculum(dadosSQL.getBoolean("pessoa.participaBancoCurriculum"));
		obj.getPessoa().setDataUltimaAtualizacaoCadatral(dadosSQL.getTimestamp("pessoa.dataUltimaAtualizacaoCadatral"));
		obj.getPessoa().setRegistroAcademico(dadosSQL.getString("pessoa.registroAcademico"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("pessoa.tipoAssinaturaDocumentoEnum"))) {
			obj.getPessoa().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("pessoa.tipoAssinaturaDocumentoEnum")));	
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		// obj.getPessoa().getCidadeEmpresa().setCodigo(dadosSQL.getInt("pessoa.cidadeEmpresa"));
		//
		// // Dados Cidade Empresa
		// obj.getPessoa().getCidadeEmpresa().setCodigo(dadosSQL.getInt("codigoCidadeEmpresa"));
		// obj.getPessoa().getCidadeEmpresa().setNome(dadosSQL.getString("nomeCidadeEmpresa"));
		obj.getPessoa().setNovoObj(Boolean.FALSE);
		obj.getPessoa().getCidade().setNovoObj(Boolean.FALSE);
		// obj.getPessoa().getCidadeEmpresa().setNovoObj(Boolean.FALSE);
		obj.getPessoa().getNacionalidade().setNovoObj(Boolean.FALSE);
		obj.getPessoa().getNaturalidade().setNovoObj(Boolean.FALSE);
		obj.getPessoa().getArquivoImagem().setNovoObj(Boolean.FALSE);

		obj.getPessoa().setNovoObj(false);
		// DADOS CIDADE
		obj.getPessoa().getCidade().setCodigo(dadosSQL.getInt("cidade.codigo"));
		obj.getPessoa().getCidade().setNome(dadosSQL.getString("cidade.nome"));
		// DADOS NATURALIDADE
		obj.getPessoa().getNaturalidade().setCodigo(dadosSQL.getInt("naturalidade.codigo"));
		obj.getPessoa().getNaturalidade().setNome(dadosSQL.getString("naturalidade.nome"));
		// DADOS NACIONALIDADE
		obj.getPessoa().getNacionalidade().setCodigo(dadosSQL.getInt("nacionalidade.codigo"));
		obj.getPessoa().getNacionalidade().setNacionalidade(dadosSQL.getString("nacionalidade.nome"));
		// DADOS PERFIL ECONÔMICO
//		obj.getPessoa().getPerfilEconomico().setCodigo(dadosSQL.getInt("perfilEconomico.codigo"));
//		obj.getPessoa().getPerfilEconomico().setNome(dadosSQL.getString("perfilEconomico.nome"));
		obj.getPessoa().getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("arquivo.pastaBaseArquivo"));
		obj.getPessoa().getArquivoImagem().setCodigo(dadosSQL.getInt("arquivo.codigo"));
		obj.getPessoa().getArquivoImagem().setNome(dadosSQL.getString("arquivo.nome"));
		obj.getPessoa().getArquivoImagem().setCpfRequerimento(dadosSQL.getString("arquivo.cpfrequerimento"));
		// DADOS PERFIL PARCEIRO
//		obj.getParceiro().setCodigo(dadosSQL.getInt("parceiro.codigo"));
//		if (!obj.getParceiro().getCodigo().equals(0)) {
//			obj.getPessoa().getCidade().setCodigo(dadosSQL.getInt("parceiro.cidade"));
//		}
		do {
			if (dadosSQL.getInt("usuarioPerfilAcesso.codigo") > 0) {
				UsuarioPerfilAcessoVO usuarioPerfilAcessoVO = new UsuarioPerfilAcessoVO();
				usuarioPerfilAcessoVO.setUsuario(dadosSQL.getInt("usuario.codigo"));
				usuarioPerfilAcessoVO.setCodigo(dadosSQL.getInt("usuarioPerfilAcesso.codigo"));
				usuarioPerfilAcessoVO.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("usuarioPerfilAcesso.unidadeEnsino"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setCodigo(dadosSQL.getInt("usuarioPerfilAcesso.perfilAcesso"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoAcademico(dadosSQL.getBoolean("perfilAcesso.possuiAcessoAcademico"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoAdministrativo(dadosSQL.getBoolean("perfilAcesso.possuiAcessoAdministrativo"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoFinanceiro(dadosSQL.getBoolean("perfilAcesso.possuiAcessoFinanceiro"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoCompras(dadosSQL.getBoolean("perfilAcesso.possuiAcessoCompras"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoSeiDecidir(dadosSQL.getBoolean("perfilAcesso.possuiAcessoSeiDecidir"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoProcessoSeletivo(dadosSQL.getBoolean("perfilAcesso.possuiAcessoProcessoSeletivo"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoEAD(dadosSQL.getBoolean("perfilAcesso.possuiAcessoEAD"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoBiblioteca(dadosSQL.getBoolean("perfilAcesso.possuiAcessoBiblioteca"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoCRM(dadosSQL.getBoolean("perfilAcesso.possuiAcessoCRM"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoAvaliacaoInsitucional(dadosSQL.getBoolean("perfilAcesso.possuiAcessoAvaliacaoInsitucional"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoBancoDeCurriculos(dadosSQL.getBoolean("perfilAcesso.possuiAcessoBancoDeCurriculos"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoPlanoOrcamentario(dadosSQL.getBoolean("perfilAcesso.possuiAcessoPlanoOrcamentario"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoNotaFiscal(dadosSQL.getBoolean("perfilAcesso.possuiAcessoNotaFiscal"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoPatrimonio(dadosSQL.getBoolean("perfilAcesso.possuiAcessoPatrimonio"));
				usuarioPerfilAcessoVO.getPerfilAcesso().setPossuiAcessoEstagio(dadosSQL.getBoolean("perfilAcesso.possuiAcessoEstagio"));
				usuarioPerfilAcessoVO.setNovoObj(Boolean.FALSE);
				obj.getUsuarioPerfilAcessoVOs().add(usuarioPerfilAcessoVO);
			}
		} while (dadosSQL.next());

		return obj;
	}

	public  StringBuilder getSQLConsultaCompletaUsuario() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\"  , usuario.username as \"usuario.username\"  ");
		sb.append(" , usuario.tipoUsuario as \"usuario.tipoUsuario\", usuario.dataUltimoAcesso  as \"usuario.dataUltimoAcesso\", usuario.dataPrimeiroAcesso  as \"usuario.dataPrimeiroAcesso\" , usuario.resetouSenhaPrimeiroAcesso  as \"usuario.resetouSenhaPrimeiroAcesso\"");
		sb.append(" , usuario.senha as \"usuario.senha\", usuario.pessoa  as \"usuario.pessoa\", usuario.possuiCadastroLdap as \"usuario.possuiCadastroLdap\"");
		sb.append(" , usuario.perfilAdministrador as \"usuario.perfilAdministrador\", usuario.solicitarnovasenha as \"usuario.solicitarnovasenha\", usuario.resetouSenhaPrimeiroAcesso as \"usuario.resetouSenhaPrimeiroAcesso\", usuario.senhaSHA as \"usuario.senhaSHA\", usuario.senhaMSCHAPV2 as \"usuario.senhaMSCHAPV2\", ");
		sb.append(" usuario.qtdFalhaLogin as \"usuario.qtdFalhaLogin\", usuario.dataFalhaLogin as \"usuario.dataFalhaLogin\", usuario.usuarioBloqPorFalhaLogin as \"usuario.usuarioBloqPorFalhaLogin\", ");
		sb.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.curriculoAtualizado as \"pessoa.curriculoAtualizado\",pessoa.nome as \"pessoa.nome\", pessoa.endereco as \"pessoa.endereco\", pessoa.setor as \"pessoa.setor\", pessoa.CEP as \"pessoa.CEP\"");
		sb.append(" ,pessoa.complemento as \"pessoa.complemento\", pessoa.numero as \"pessoa.numero\", pessoa.cidade as \"pessoa.cidade\", pessoa.sexo as \"pessoa.sexo\"");
		sb.append(" ,pessoa.estadoCivil as \"pessoa.estadoCivil\", pessoa.telefoneRes as \"pessoa.telefoneRes\", pessoa.telefoneRecado as \"pessoa.telefoneRecado\"");
		sb.append(" ,pessoa.celular as \"pessoa.celular\", pessoa.email as \"pessoa.email\", pessoa.dataNasc as \"pessoa.dataNasc\", pessoa.naturalidade as \"pessoa.naturalidade\"");
		sb.append(" ,pessoa.nacionalidade as \"pessoa.nacionalidade\", pessoa.CPF as \"pessoa.CPF\", pessoa.certificadoMilitar as \"pessoa.certificadoMilitar\", pessoa.RG as \"pessoa.RG\" , pessoa.registroAcademico as \"pessoa.registroAcademico\"");
		sb.append(" ,pessoa.dataEmissaoRG as \"pessoa.dataEmissaoRG\", pessoa.estadoEmissaoRG as \"pessoa.estadoEmissaoRG\", pessoa.orgaoEmissor as \"pessoa.orgaoEmissor\", pessoa.tituloEleitoral as \"pessoa.tituloEleitoral\"");
		sb.append(" ,pessoa.necessidadesEspeciais as \"pessoa.necessidadesEspeciais\", pessoa.professor as \"pessoa.professor\", pessoa.aluno as \"pessoa.aluno\", pessoa.candidato as \"pessoa.candidato\" , pessoa.possuiAcessoVisaoPais as \"pessoa.possuiAcessoVisaoPais\" ");
		sb.append(" ,pessoa.membroComunidade as \"pessoa.membroComunidade\", pessoa.funcionario as \"pessoa.funcionario\", pessoa.paginaPessoal as \"pessoa.paginaPessoal\", pessoa.valorCssTopoLogo as \"pessoa.valorCssTopoLogo\"");
		sb.append(" ,pessoa.valorCssBackground as \"pessoa.valorCssBackground\", pessoa.valorCssMenu as \"pessoa.valorCssMenu\", pessoa.perfilEconomico as \"pessoa.perfilEconomico\", pessoa.atuaComoDocente as \"pessoa.atuaComoDocente\"");
		sb.append(" ,pessoa.ativo as \"pessoa.ativo\", pessoa.idalunoinep as \"pessoa.idalunoinep\", pessoa.passaporte as \"pessoa.passaporte\", pessoa.corraca as \"pessoa.corraca\", pessoa.ocultarDadosCRM as \"pessoa.ocultarDadosCRM\" ");
		sb.append(" ,pessoa.deficiencia as \"pessoa.deficiencia\", pessoa.nomeFiador as \"pessoa.nomeFiador\", pessoa.enderecoFiador as \"pessoa.enderecoFiador\", pessoa.telefoneFiador as \"pessoa.telefoneFiador\"");
		sb.append(" ,pessoa.cpfFiador as \"pessoa.cpfFiador\", pessoa.tipoNecessidadesEspeciais as \"pessoa.tipoNecessidadesEspeciais\", pessoa.celularFiador as \"pessoa.celularFiador\", pessoa.arquivoImagem as \"pessoa.arquivoImagem\", pessoa.participaBancoCurriculum as \"pessoa.participaBancoCurriculum\" ");
		sb.append(" ,pessoa.tipoAssinaturaDocumentoEnum as \"pessoa.tipoAssinaturaDocumentoEnum\" ");
		// sb.append(" , pessoa.enderecoEmpresa as \"pessoa.enderecoEmpresa\", pessoa.cargoPessoaEmpresa as \"pessoa.cargoPessoaEmpresa\", pessoa.cepEmpresa as \"pessoa.cepEmpresa\"");
		// sb.append(" ,pessoa.complementoEmpresa as \"pessoa.complementoEmpresa\", pessoa.cidadeEmpresa as \"pessoa.cidadeEmpresa\", pessoa.setorEmpresa as \"pessoa.setorEmpresa\", pessoa.email2 as \"pessoa.email2\", pessoa.coordenador as \"pessoa.coordenador\", ");
		sb.append(" , pessoa.email2 as \"pessoa.email2\", exists(select cursocoordenador.codigo from cursocoordenador inner join funcionario on funcionario.codigo = cursocoordenador.funcionario  where funcionario.pessoa = pessoa.codigo) as \"pessoa.coordenador\", ");
		sb.append(" pessoa.dataUltimaAtualizacaoCadatral as \"pessoa.dataUltimaAtualizacaoCadatral\", ");
		sb.append(" cidade.codigo AS \"cidade.codigo\", cidade.nome AS \"cidade.nome\",  ");
		sb.append(" paiz.codigo AS \"nacionalidade.codigo\", paiz.nacionalidade AS \"nacionalidade.nome\", ");
		// sb.append(" cidadeEmpresa.codigo AS \"codigoCidadeEmpresa\", cidadeEmpresa.nome AS \"nomeCidadeEmpresa\", ");
		sb.append(" perfilEconomico.codigo AS \"perfilEconomico.codigo\", perfilEconomico.nome AS \"perfilEconomico.nome\", ");
		sb.append(" naturalidade.codigo AS \"naturalidade.codigo\", naturalidade.nome AS \"naturalidade.nome\",");
		sb.append(" arquivo.pastaBaseArquivo as \"arquivo.pastaBaseArquivo\", arquivo.codigo AS \"arquivo.codigo\", arquivo.nome AS \"arquivo.nome\", arquivo.cpfrequerimento as \"arquivo.cpfrequerimento\" ");
		sb.append(" , usuarioPerfilAcesso.codigo as \"usuarioPerfilAcesso.codigo\" ,usuarioPerfilAcesso.unidadeEnsino as \"usuarioPerfilAcesso.unidadeEnsino\", usuarioPerfilAcesso.perfilAcesso as \"usuarioPerfilAcesso.perfilAcesso\", ");
		sb.append(" parceiro.codigo as \"parceiro.codigo\", parceiro.cidade as \"parceiro.cidade\",  ");
		sb.append(
				" perfilAcesso.possuiAcessoAdministrativo as \"perfilAcesso.possuiAcessoAdministrativo\", perfilAcesso.possuiAcessoAcademico as \"perfilAcesso.possuiAcessoAcademico\", perfilAcesso.possuiAcessoFinanceiro as \"perfilAcesso.possuiAcessoFinanceiro\", perfilAcesso.possuiAcessoCompras as \"perfilAcesso.possuiAcessoCompras\", perfilAcesso.possuiAcessoSeiDecidir as \"perfilAcesso.possuiAcessoSeiDecidir\", perfilAcesso.possuiAcessoProcessoSeletivo as \"perfilAcesso.possuiAcessoProcessoSeletivo\", perfilAcesso.possuiAcessoEAD as \"perfilAcesso.possuiAcessoEAD\", perfilAcesso.possuiAcessoBiblioteca as \"perfilAcesso.possuiAcessoBiblioteca\", perfilAcesso.possuiAcessoCRM as \"perfilAcesso.possuiAcessoCRM\", perfilAcesso.possuiAcessoAvaliacaoInsitucional as \"perfilAcesso.possuiAcessoAvaliacaoInsitucional\", perfilAcesso.possuiAcessoBancoDeCurriculos as \"perfilAcesso.possuiAcessoBancoDeCurriculos\", perfilAcesso.possuiAcessoPlanoOrcamentario as \"perfilAcesso.possuiAcessoPlanoOrcamentario\", perfilAcesso.possuiAcessoNotaFiscal as \"perfilAcesso.possuiAcessoNotaFiscal\", perfilAcesso.possuiAcessoEstagio as \"perfilAcesso.possuiAcessoEstagio\", perfilAcesso.possuiAcessoPatrimonio as \"perfilAcesso.possuiAcessoPatrimonio\"");
		sb.append(" FROM usuario ");
		sb.append(" LEFT JOIN pessoa 					ON pessoa.codigo 					= usuario.pessoa ");
		sb.append(" LEFT JOIN pessoaemailinstitucional  ON pessoaemailinstitucional.pessoa  = pessoa.codigo ");
		sb.append(" LEFT JOIN usuarioPerfilAcesso 		ON usuarioPerfilAcesso.usuario 		= usuario.codigo ");
		sb.append(" LEFT JOIN perfilAcesso 				ON usuarioPerfilAcesso.perfilAcesso = perfilAcesso.codigo ");
		sb.append(" LEFT JOIN perfilEconomico 			ON pessoa.perfilEconomico 			= perfilEconomico.codigo ");
		sb.append(" LEFT JOIN paiz 						ON pessoa.nacionalidade 			= paiz.codigo ");
		sb.append(" LEFT JOIN cidade 					ON pessoa.cidade 					= cidade.codigo ");
		sb.append(" LEFT JOIN cidade naturalidade 		ON pessoa.naturalidade 				= naturalidade.codigo ");
		// sb.append(" LEFT JOIN cidade cidadeEmpresa ON pessoa.cidadeEmpresa = cidadeEmpresa.codigo ");
		sb.append(" LEFT JOIN arquivo 					ON pessoa.arquivoImagem 			= arquivo.codigo");
		sb.append(" LEFT JOIN parceiro 					ON parceiro.codigo 					= usuario.parceiro");
		return sb;
	}

	// private static UsuarioVO obterUsuarioLogado() throws Exception {
	// LoginControle loginControle = (LoginControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LoginControle");
	// UsuarioVO usuario = loginControle.getUsuario();
	// return usuario;
	// }
	/**
	 * Operação padrão para realizar o CONSULTAR de dados de uma entidade no BD. Verifica e inicializa (se necessário) a conexão com o BD. Verifica se o usuário logado possui permissão de acesso a operação CONSULTAR.
	 * 
	 * @param idEntidade
	 *            Nome da entidade para a qual se deseja realizar a operação.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso a esta operação.
	 */
	protected void verificarPermissaoConsultar(String idEntidade, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		if (!verificarAcesso) {
			return;
		}
		if (!verificarPermissaoUsuario(idEntidade, Uteis.CONSULTAR, usuario)) {
			String nomeUsuario = "";
			if (usuario != null) {
				nomeUsuario = " " + usuario.getNome().toUpperCase();
			}
			// if (obterUsuarioLogado() != null) {
			// nomeUsuario = " " + obterUsuarioLogado().getNome().toUpperCase();
			// }
			String msgErro = "USUÁRIO" + nomeUsuario + " não possui permissão para CONSULTAR em " + tratarTituloPermissao(idEntidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}

	/**
	 * Operação responsável por verificar se um usuário possui acesso a uma determinada operação, de uma entidade específica. Faz uso da operação </code>verificarPermissaoOperacao(getPerfilAcesso().getPermissaoVOs(), nomeEntidade, operacao)</code> para realizar a verificação para um perfil de acesso específico. Libera a consulta para as entidades PerfilAcesso e Permissoes, para que seja possível consultar se o usuário possui ou não acesso a uma determinada funcionalidade.
	 * 
	 * @param nomeEntidade
	 *            Código de uma operação para qual se deseja realizar esta verificação.
	 * @param operacao
	 *            Código de uma operação para qual se deseja realizar esta verificação.
	 * @return boolean true caso exista liberação para uso da operação neste perfil de acesso.
	 */
//	private static boolean verificarPermissaoUsuario(String nomeEntidade, int operacao, UsuarioVO usuario) throws Exception {
//		if (nomeEntidade.equals("")) {
//			return false;
//		}
//		if ((usuario == null) || (usuario.getUsername().equalsIgnoreCase(""))) {
//			return false;
//		}
//		if (usuario.getPerfilAcesso() == null || usuario.getPerfilAcesso().getCodigo() == 0) {
//			return false;
//		}
//		// return verificarPermissaoOperacao(usuario.getPerfilAcesso().getPermissaoVOs(), nomeEntidade, operacao);
//		if (usuario.getTipoPessoa().equals("FU")) {
//			return verificarPermissaoOperacao(nomeEntidade, operacao, usuario);
//		} else {
//			return verificarPermissaoOperacao(usuario.getPerfilAcesso().getCodigo(), nomeEntidade, operacao);
//		}
//	}
	
	private  boolean verificarPermissaoUsuario(String nomeEntidade, int operacao, UsuarioVO usuario) throws Exception {
		if (nomeEntidade.equals("")) {
//			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.PERMISSAO, "(  => NOMEINDETIDADE VAZIO )");
			return false;
		}
		if ((usuario == null) || (usuario.getUsername().equalsIgnoreCase(""))) {
//			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.PERMISSAO, "( => USUARIO NULO OU VAZIO  )");
			return false;
		}
		if (usuario.getPerfilAcesso() == null || usuario.getPerfilAcesso().getCodigo() == 0) {
//			AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.PERMISSAO, " ( => USUARIO.GETPERFILACESSO NULLO OU USUARIO.GETPERFILACESSO SEM CODIGO )");
			return false;
		}
		if (usuario.getTipoPessoa().equals("FU")) {
			boolean verificacao = verificarPermissaoOperacao(nomeEntidade, operacao, usuario);
//			if(!verificacao) {
//				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.PERMISSAO, "( => USUARIO SEM PERMISSAO   | CODIGOUSUARIOPERFILACESSO: => "+usuario.getPerfilAcesso().getCodigo() +" | NOME IDENTIDADE: => "+nomeEntidade+ "  | OPERACAO : => "+operacao);
//			}
			return verificacao;
		} else {			
			boolean verificacao = verificarPermissaoOperacao(usuario.getPerfilAcesso().getCodigo(), nomeEntidade, operacao);
//			if(!verificacao) {
//				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.PERMISSAO, "( => USUARIOPERFILACESSO  SEM PERMISSAO    | CODIGOUSUARIOPERFILACESSO: => "+usuario.getPerfilAcesso().getCodigo() +" | NOME IDENTIDADE: => "+nomeEntidade+ "  | OPERCAO : => "+operacao);
//			}
			return verificacao ; 
		}
	}

	private  boolean verificarPermissaoUsuarioComUsuario(String nomeEntidade, int operacao, UsuarioVO usuario) throws Exception {
        PerfilAcessoVO perfil = null;
        if(!usuario.getIsApresentarVisaoAdministrativa()) {
        	perfil = usuario.getPerfilAcesso();
        } else if (usuario.getUsuarioPerfilAcessoVOs().isEmpty() && usuario.getPerfilAcesso() != null) {
			perfil = usuario.getPerfilAcesso();
        }else {
	        Iterator<UsuarioPerfilAcessoVO> i = usuario.getUsuarioPerfilAcessoVOs().iterator();
	        while (i.hasNext()) {
	            UsuarioPerfilAcessoVO usuarioPerfilAcessoVO = (UsuarioPerfilAcessoVO) i.next();
	            if (usuarioPerfilAcessoVO.getUnidadeEnsinoVO().getCodigo().equals(usuario.getUnidadeEnsinoLogado().getCodigo())) {
	                perfil = usuarioPerfilAcessoVO.getPerfilAcesso(); //getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(usuarioPerfilAcessoVO.getPerfilAcesso().getCodigo(), usuario);
	                break;
	            }
	        }
        }
		if (nomeEntidade.equals("")) {
			return false;
		}
		if ((usuario == null) || (usuario.getUsername().equalsIgnoreCase(""))) {
			return false;
		}
		if (perfil == null || perfil.getCodigo().intValue() == 0) {
			return false;
		}
		// return verificarPermissaoOperacao(perfil.getPermissaoVOs(), nomeEntidade, operacao);
		return verificarPermissaoOperacao(perfil.getCodigo(), nomeEntidade, operacao);
	}
	private  boolean verificarPermissaoUsuarioComUsuarioEspecifico(String nomeEntidade, int operacao, UsuarioVO usuario) throws Exception {
		PerfilAcessoVO perfil = null;
		if (!usuario.getIsApresentarVisaoAdministrativa()) {
			perfil = usuario.getPerfilAcesso();
		} else if (usuario.getUsuarioPerfilAcessoVOs().isEmpty() && usuario.getPerfilAcesso() != null) {
			perfil = usuario.getPerfilAcesso();
		} else {
			Iterator<UsuarioPerfilAcessoVO> i = usuario.getUsuarioPerfilAcessoVOs().iterator();
			while (i.hasNext()) {
				UsuarioPerfilAcessoVO usuarioPerfilAcessoVO = (UsuarioPerfilAcessoVO) i.next();
				if (usuarioPerfilAcessoVO.getUnidadeEnsinoVO().getCodigo().equals(usuario.getUnidadeEnsinoLogado().getCodigo())) {
					perfil = usuarioPerfilAcessoVO.getPerfilAcesso();
					break;					
				}				
				perfil = usuarioPerfilAcessoVO.getPerfilAcesso();
			}
		}
		if (nomeEntidade.equals("")) {
			return false;
		}
		if ((usuario == null) || (usuario.getUsername().equalsIgnoreCase(""))) {
			return false;
		}
		if (perfil == null || perfil.getCodigo().intValue() == 0) {
			return false;
		}		
		// return verificarPermissaoOperacao(perfil.getPermissaoVOs(), nomeEntidade, operacao);
		return verificarPermissaoOperacao(perfil.getCodigo(), nomeEntidade, operacao);
	}

	private  boolean verificarPermissaoFuncionalidadeComUsuarioEspecifico(String nomeEntidade, int operacao, UsuarioVO usuario) throws Exception {
		PerfilAcessoVO perfil = null;
		if(Uteis.isAtributoPreenchido(usuario.getPerfilAcesso())){
			perfil = usuario.getPerfilAcesso();
		} else if(Uteis.isAtributoPreenchido(usuario.getUsuarioPerfilAcessoVOs())) {
			perfil = usuario.getUsuarioPerfilAcessoVOs().get(0).getPerfilAcesso();
		}
		if (nomeEntidade.equals("")) {
			return false;
		}
		if ((usuario == null) || (usuario.getUsername().equalsIgnoreCase(""))) {
			return false;
		}
		if (perfil == null || perfil.getCodigo().intValue() == 0) {
			return false;
		}
		return verificarPermissaoOperacao(perfil.getCodigo(), nomeEntidade, operacao);
	}

	public static String getIdEntidade() {
		return "";
	}

	// public PerfilAcessoVO getPerfilAcesso() {
	// return perfilAcesso;
	// }
	//
	// public void setPerfilAcesso(PerfilAcessoVO aPerfilAcesso) {
	// perfilAcesso = aPerfilAcesso;
	// }
	public Monitoramento getMonitoramento() {
		return monitoramento;
	}

	public void setMonitoramento(Monitoramento monitoramento) {
		this.monitoramento = monitoramento;
	}

	public static String adicionarUsuarioLogadoComoComentarioParaLogTrigger(UsuarioVO usuarioLogado) {
		if (usuarioLogado != null && usuarioLogado.getCodigo() != null) {
			return " --ul:" + usuarioLogado.getCodigo();
		} else {
			return " --ul:0";
		}
	}

	public  boolean verificarPermissaoFuncionalidadePorUsernameSenhaUnidadeEnsino(String funcionalidade, String username, String senha, Integer unidadeEnsino, Boolean retornarExcexao) throws Exception {
		if (retornarExcexao && username.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_Usuario_username"));
		}
		if (retornarExcexao && senha.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_Usuario_senha"));
		}
		StringBuilder sql = new StringBuilder("SELECT DISTINCT p.nomeEntidade FROM permissao p ");
		sql.append(" INNER JOIN perfilacesso pa ON pa.codigo = p.codperfilacesso ");
		sql.append(" WHERE p.nomeEntidade = ? AND pa.codigo IN ( ");
		sql.append(" SELECT pa2.codigo FROM perfilacesso pa2 ");
		sql.append(" INNER JOIN usuarioperfilacesso upa2 ON upa2.perfilacesso = pa2.codigo ");
		sql.append(" INNER JOIN usuario ON usuario.codigo = upa2.usuario ");
		sql.append(" WHERE usuario.username = ? and usuario.senha = ? ");
		sql.append(" AND ( (upa2.unidadeensino is null) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" or (upa2.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		sql.append(" )) ");
		sql.append("LIMIT 1");
		Boolean possuiAcesso = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionalidade, username, Uteis.encriptar(senha)).next();
		if (retornarExcexao && !possuiAcesso) {
			throw new Exception(UteisJSF.internacionalizar("msg_Usuario_funcionalidadeNaoPermitida"));
		}
		return possuiAcesso;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarSeRegistroForamExcluidoDasListaSubordinadas(List<? extends SuperVO> lista, String tabelaSubordinada, String nomeEntidade, Integer codigoEntidade, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(lista)) {
			StringBuilder sb = new StringBuilder("DELETE FROM ").append(tabelaSubordinada).append(" where ").append(nomeEntidade).append(" =  ").append(codigoEntidade);
			sb.append(" and codigo not in ( 0  ");
			for (Object obj : lista) {
				Integer codigo = (Integer) UtilReflexao.invocarMetodoGet(obj, "codigo");
				if (Uteis.isAtributoPreenchido(codigo)) {
					sb.append(", ").append(codigo);
				}

			}
			sb.append(") ");
			sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().execute(sb.toString());
		} else {
			StringBuilder sb = new StringBuilder("DELETE FROM ").append(tabelaSubordinada).append(" where ").append(nomeEntidade).append(" =  ").append(codigoEntidade).append(" ");
			sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().execute(sb.toString());
		}
	}

	public void executarRegistroFalhaPesquisaElastic(String indice, String mensagem) {
		getConexao().getJdbcTemplate().update("insert into elastic.search_exception (origin, message_text) values (?, ?)", indice, mensagem);
	}

	public Integer executarConsultaRegistrosIndiceVersao(String url) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet(url);
			get.setHeader("Content-Type", "application/json; charset=UTF-8");
			HttpResponse response = client.execute(get);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			StringBuilder sb = new StringBuilder();
			sb.append("select ('").append(result).append("'::json -> 'count')::text::int registros");
			Integer r = 0;
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (tabelaResultado.next()) {
				r = tabelaResultado.getInt("registros");
			}
			return r;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public  void verificarCompetenciaBloqueadaParaRegistrosEntidade(
			SuperVO obj, String operacao, Date novaDataVerificar, Date dataVerificarAnterior, Integer codigoUnidadeEnsino, Integer codigoCaixa,  UsuarioVO usuario) throws Exception {
		verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, operacao, novaDataVerificar, dataVerificarAnterior, null, null, codigoUnidadeEnsino, codigoCaixa,  usuario);		
	}
	
	public  void verificarCompetenciaBloqueadaParaRegistrosEntidade(
			SuperVO obj, String operacao, Date novaDataVerificar,  List<Integer> listaCodigosUnidadesEnsino, Integer codigoCaixa,  UsuarioVO usuario) throws Exception {
		verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, operacao, novaDataVerificar, null, null, null, listaCodigosUnidadesEnsino, codigoCaixa,  usuario);		
	}	
	
	public  void verificarCompetenciaBloqueadaParaRegistrosEntidade(
			SuperVO obj, String operacao, Date novaDataVerificar, Integer codigoUnidadeEnsino,  UsuarioVO usuario) throws Exception {
		verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, operacao, novaDataVerificar, null, null, null, codigoUnidadeEnsino, null,  usuario);		
	}	

	public  void verificarCompetenciaBloqueadaParaRegistrosEntidade(
			SuperVO obj, String operacao, Date novaDataVerificar, Date novaDataCompVerificar, Integer codigoUnidadeEnsino,  UsuarioVO usuario) throws Exception {
		verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, operacao, novaDataVerificar, null, novaDataCompVerificar, null, codigoUnidadeEnsino, null,  usuario);		
	}

	public  void verificarCompetenciaBloqueadaParaRegistrosEntidade(
			SuperVO obj, String operacao, Date novaDataVerificar, Date dataVerificarAnterior, Date novaDataCompVerificar, Date novaDataCompVerificarAnterior, Integer codigoUnidadeEnsino, Integer codigoCaixa,  UsuarioVO usuario) throws Exception {
		List<Integer> listaCodigosUnidadesEnsino = new ArrayList<Integer>();
		listaCodigosUnidadesEnsino.add(codigoUnidadeEnsino);
		verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, operacao, novaDataVerificar, dataVerificarAnterior, novaDataCompVerificar, novaDataCompVerificarAnterior, listaCodigosUnidadesEnsino, codigoCaixa,  usuario);		
	}
	
	public  void verificarCompetenciaBloqueadaParaRegistrosEntidade(
			SuperVO obj, String operacao, Date novaDataVerificar, Date dataVerificarAnterior, List<Integer> listaCodigosUnidadesEnsino, Integer codigoCaixa,  UsuarioVO usuario) throws Exception {
		verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, operacao, novaDataVerificar, dataVerificarAnterior, null, null, listaCodigosUnidadesEnsino, codigoCaixa,  usuario);		
	}	
	
	public  void verificarCompetenciaBloqueadaParaRegistrosEntidade(
			SuperVO obj, String operacao, Date novaDataVerificar, Date dataVerificarAnterior, Date novaDataCompVerificar, Date novaDataCompVerificarAnterior, List<Integer> listaCodigosUnidadesEnsino, Integer codigoCaixa,  UsuarioVO usuario) throws Exception {
//		if (!obj.getVerificouBloqueioPorFechamentoMes()) {
//			String strOperacao = "IGUAL";
//			FechamentoMesVO fechamentoMesVO = getFacadeFactory().getFechamentoMesFacade().verificarCompetenciaFechada(novaDataVerificar, novaDataCompVerificar,  listaCodigosUnidadesEnsino, codigoCaixa, usuario); 
//			if (fechamentoMesVO == null) {
//				// Caso a novaData que esta sendo levada a registro seja de um mes bloqueado, isto é tratado acima. 
//				// Contudo, se a data anterior tb era do mes fechado, temos que impedir a modificao.
//				if ((dataVerificarAnterior != null) && (!dataVerificarAnterior.equals(novaDataVerificar))) {
//					fechamentoMesVO = getFacadeFactory().getFechamentoMesFacade().verificarCompetenciaFechada(dataVerificarAnterior, novaDataCompVerificar,  listaCodigosUnidadesEnsino, codigoCaixa, usuario); 
//					if (fechamentoMesVO != null) { 
//						obj.setDataBloqueioVerificada(fechamentoMesVO.getDataBloqueioVerificada());
//						strOperacao = "ANTERIOR";
//					}
//				}
//			} else {
//				obj.setDataBloqueioVerificada(fechamentoMesVO.getDataBloqueioVerificada());
//			}
//			obj.setVerificouBloqueioPorFechamentoMes(Boolean.TRUE);
//			if (fechamentoMesVO != null) {
//				obj.setBloqueioPorFechamentoMesLiberado(Boolean.FALSE);
//				//FechamentoMesVO fechamentoMes = new FechamentoMesVO();
//				//fechamentoMes.setCodigo(codigoFechamento);
//				obj.setFechamentoMesVOBloqueio(fechamentoMesVO);
//				String descricaoBloquioStr = "(Competência Fechada) Não é possível "+ operacao + " " + tipoOrigemHistoricoBloqueio.getValorApresentar() + " com a Data " + fechamentoMesVO.getCompletoDescricaoDataUtilizadaVerificacao(tipoOrigemHistoricoBloqueio) + " " + strOperacao + ": " + Uteis.getData(obj.getDataBloqueioVerificada()) + ". Este mês/ano já está com sua competência fechada (Fechamento Mês)"; 
//				obj.setDescricaoBloqueio("Realizada Operação " + operacao + " em " + tipoOrigemHistoricoBloqueio.getValorApresentar() + " nesta competência. Data " + fechamentoMesVO.getCompletoDescricaoDataUtilizadaVerificacao(tipoOrigemHistoricoBloqueio) + " " + strOperacao + ": " + Uteis.getData(obj.getDataBloqueioVerificada()));
//				throw new ConsistirException(descricaoBloquioStr, obj);
//			} else {
//				obj.setBloqueioPorFechamentoMesLiberado(Boolean.TRUE);
//			}
//		} else {
//			if (!obj.getBloqueioPorFechamentoMesLiberado()) { 
//				throw new ConsistirException("Não é possível "+ operacao + " uma " + tipoOrigemHistoricoBloqueio.getValorApresentar() + " com a Data " + obj.getFechamentoMesVOBloqueio().getCompletoDescricaoDataUtilizadaVerificacao(tipoOrigemHistoricoBloqueio) + " " + Uteis.getData(obj.getDataBloqueioVerificada()) + ". Este mês/ano já está com sua competência fechada (Fechamento Mês)");
//			}
//		}
	}
	
	public <T extends SuperVO> void inserir(T entidade, UsuarioVO usuarioVO) throws SQLException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		if (entidade.getClass().isAnnotationPresent(EntidadeDaoGenerico.class)) {
			EntidadeDaoGenerico edg = entidade.getClass().getAnnotation(EntidadeDaoGenerico.class);
			StringBuilder sbValues = new StringBuilder("");
			StringBuilder sbCampos = new StringBuilder("");
			List<HashMap<Integer, Object>> valores = new ArrayList<HashMap<Integer, Object>>();
			HashMap<Integer, Object>map = null;
			boolean existeVirgula = false;
			for (Field fied : entidade.getClass().getDeclaredFields()) {				
				if (fied.isAnnotationPresent(ColunaDaoGenerico.class)) {
					fied.setAccessible(true);
					ColunaDaoGenerico cdg = fied.getAnnotation(ColunaDaoGenerico.class);
					String coluna = cdg.nome() == null ? fied.getName() : cdg.nome();
					if (!existeVirgula) {
						sbCampos.append(coluna).append(" ");
						sbValues.append(" ? ");
						map = new HashMap<Integer, Object>();
						map.put(cdg.tipo(), fied.get(entidade));
						valores.add(map);
						existeVirgula = true;
					} else {
						sbCampos.append(", ").append(coluna).append(" ");
						sbValues.append(", ? ");						
						map = new HashMap<Integer, Object>();
						map.put(cdg.tipo(), fied.get(entidade));
						valores.add(map);
					}
				}
			}
			
			StringBuilder sql = new StringBuilder("INSERT INTO ").append(edg.tabela()).append(" ( ").append(sbCampos).append(" ) VALUES ( ").append(sbValues).append(") returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));			
			Field campoCodigo = entidade.getClass().getDeclaredField("codigo");
			campoCodigo.setAccessible(true);
			campoCodigo.set(entidade, (Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					for (HashMap<Integer, Object> map : valores) {
						for (Entry<Integer, Object> entry: map.entrySet()) {
							Uteis.setValuePreparedStatement(entry.getValue(), entry.getKey(), ++i, sqlInserir);
						}
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						entidade.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			entidade.setNovoObj(Boolean.FALSE);
		}

	}	
	
	protected boolean verificarPermissaoVisualizarAlunoTR_CA(UsuarioVO usuario) {
		try {
			verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("LancamentoNotaa_VisualizarMatriculaTR_CA", usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	protected boolean verificarPermiteLancarNotaDisciplinaComposta(UsuarioVO usuario) {
		try {
			return verificarPermissaoFuncionalidadeUsuario("PermiteLancarNotaDisciplinaComposta",usuario);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String tratarTituloPermissao(String key, UsuarioVO usuario) {
		Enum<? extends PerfilAcessoPermissaoEnumInterface> permissao = PerfilAcessoModuloEnum.getEnumsPorValor(key);
		if (permissao != null) {
			return ((PerfilAcessoPermissaoEnumInterface) permissao).getDescricaoVisao(usuario.getTipoVisaoAcesso()).toUpperCase();
		} else {
			return key.toUpperCase();
		}
	}
	
	public  UsuarioVO verificarLoginUsuarioParaControleFalhaLogin(String username) throws Exception {
		try {
			StringBuilder sqlStr = getSQLConsultaCompletaUsuario().append(" WHERE username = ?");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), username);
			return tabelaResultado.next() ? montarDadosUsuarioLogado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS) : null;
		} catch (Exception e) {
			throw e;
		}
	}
	public  void realizarMovimentacaoContasCaixaFechada(String funcionalidade, UsuarioVO usuario) throws Exception {
		if (!verificarPermissaoUsuarioComUsuarioEspecifico(funcionalidade, Uteis.ALTERAR, usuario)) {
			String nomeUsuario = "";
			nomeUsuario = " " + usuario.getNome().toUpperCase();
			String msgErro = "USUÁRIO " + nomeUsuario + " não possui permissão para a funcionalidade " + tratarTituloPermissao(funcionalidade, usuario);
			throw (new AcessoException(msgErro));
		}
	}
	
	public String getSelectTotalizadorConsultaBasica() {
		return " select COUNT(*) OVER() as totalRegistroConsulta, ";
	}

	public void montarTotalizadorConsultaBasica(DataModelo dataModelo, SqlRowSet tabelaResultado) {
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados((tabelaResultado.getInt("totalRegistroConsulta")));
		}else {
			dataModelo.setTotalRegistrosEncontrados(0);
		}
		tabelaResultado.beforeFirst();
	}
	
	
	protected static synchronized  UnirestInstance unirest() {
		if(unirest == null) {
			unirest = Unirest.primaryInstance();
			if(unirest.isRunning()) {
				unirest.config().reset();
			}
			unirest.config().connectTimeout(120000);
			unirest.config().socketTimeout(0);
		}
		return unirest;
	}
	
	protected static synchronized  void resetUnirest() {
		if(unirest != null ) {
		   unirest = null;		
		}		
	}
	
	
	protected static synchronized void configurarUnirestImpressaoContratoMatriculaExterna () {
		 unirest = null;
		 unirest = Unirest.primaryInstance();
		 if(unirest.isRunning()) {
				unirest.config().reset();
		 }			
		 unirest.config().connectTimeout(30000);
		 unirest.config().socketTimeout(30000);		
	}
	
	public Gson inicializaGson() {
		return inicializaGson("yyyy-MM-dd HH:mm:ss");
	}
	
	public Gson inicializaGson(String dateFormat) {
		return new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat(dateFormat).create();
	}
	
	protected void tratarMensagemErroWebService(InfoWSVO resp, String status,  String mensagemRetorno) throws StreamSeiException {
		String msg = null;
		if(resp != null && resp.getCodigo() != 0) {
			msg = resp.getMensagem() + " - " + resp.getMensagemCampos();
		}else if(resp != null && resp.getStatus() != 0) {
			msg = resp.getMessage();
		}else {
			msg = mensagemRetorno;
		}
		if (status.contains("400")) {
			throw new StreamSeiException("A requisição é inválida, em geral conteúdo malformado. " + msg);
		}
		if (status.contains("401")) {
			throw new StreamSeiException("O usuário e senha ou token de acesso são inválidos. " + msg);
		}
		if (status.contains("403")) {
			throw new StreamSeiException("O acesso à API está bloqueado ou o usuário está bloqueado. " + msg);
		}
		if (status.contains("404")) {
			throw new StreamSeiException("O endereço acessado não existe. " + msg);
		}
		if (status.contains("406")) {
			throw new StreamSeiException("Acesso não autorizado não autorizado para o recurso. " + msg);
		}
		if (status.contains("413")) {
			throw new StreamSeiException("A solicitação é muito grande A solicitação está excedendo o limite permitido para o seu perfil de token de acesso. " + msg);
		}
		if (status.contains("422")) {
			throw new StreamSeiException("A Requisição é válida, mas os dados passados não são válidos. " + msg);
		}
		if (status.contains("429")) {
			throw new StreamSeiException("O usuário atingiu o limite de requisições. " + msg);
		}
		if (status.contains("500")) {
			throw new StreamSeiException("Houve um erro interno do servidor ao processar a requisição. " + msg);
		}
		throw new StreamSeiException("Erro Não tratato Web Service : " + msg);
	}

	
	/**
	 * Metodo que realiza as requisicoes do para o seigsuite.
	 * 
	 * @param configuracaoSeiGsuiteVO
	 * @param url - Url complementar para concatenação da url base da {@link ConfiguracaoSeiGsuiteVO}
	 * @param json - String Json
	 * @param requestMethod - Exemplo: RequestMethod.POST
	 * @param usuarioVO - Usuário logado.
	 * @Param queryStrings Map<String, Object> para requisições GET
	 * @return HttpResponse<JsonNode>
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public kong.unirest.HttpResponse<JsonNode> unirestBody(ConfiguracaoSeiGsuiteVO configuracaoSeiGsuiteVO, String url, String json, RequestMethod requestMethod,  Map<String, Object> queryStrings, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(configuracaoSeiGsuiteVO), "Não foi localizado uma configuração Sei Gsuite para realizar operação do evento Google Meet.");
//		TokenVO token = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarTokenVO(configuracaoSeiGsuiteVO);
		Uteis.checkState(requestMethod == null, "O tipo de requisição não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(json), "O json não esta informado.");
//		return unirestBody(token, configuracaoSeiGsuiteVO.getUrlExternaSeiGsuite() + url, json, requestMethod, queryStrings, usuarioVO);
		return null;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public kong.unirest.HttpResponse<JsonNode> unirestBody(TokenVO token, String url, String json, RequestMethod requestMethod,  Map<String, Object> queryStrings, UsuarioVO usuarioVO) throws Exception {
		kong.unirest.HttpResponse<JsonNode> jsonResponse = null;
		RequestBodyEntity requestBodyEntity = null;
		Uteis.checkState(requestMethod == null, "O tipo de requisição não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(json), "O json não esta informado.");
		switch (requestMethod) {
		case POST:
			requestBodyEntity = unirest().post(url)
			.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(UteisWebServiceUrl.ul, usuarioVO.getCodigo().toString())
			.body(json);
			break;
		case PUT:
			requestBodyEntity = unirest().put(url)
			.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(UteisWebServiceUrl.ul, usuarioVO.getCodigo().toString())
			.body(json);
			break;
		case DELETE:
			requestBodyEntity = unirest().delete(url)
			.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(UteisWebServiceUrl.ul, usuarioVO.getCodigo().toString())
			.body(json);
			break;
		case GET:
			GetRequest getRequest = unirest().get(url)
			.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(UteisWebServiceUrl.ul, usuarioVO.getCodigo().toString());
			if(queryStrings != null && queryStrings.size() > 0) {
				getRequest.queryString(queryStrings);
			}
			jsonResponse =  getRequest.asJson();
			break;
		default:
			throw new StreamSeiException("Opção de RequestMethod não implementada "+requestMethod.name());
		}
		
		if(!requestMethod.equals(RequestMethod.GET)) {
			if(queryStrings != null && queryStrings.size() > 0) {
				requestBodyEntity.queryString(queryStrings);	
			}
			jsonResponse = requestBodyEntity.asJson();
		}
		if (jsonResponse != null && !jsonResponse.isSuccess()) {
			throw new StreamSeiException((String) jsonResponse.getBody().getObject().get("message"));
		}
		
		return jsonResponse;
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public kong.unirest.HttpResponse<JsonNode> unirestHeaders(ConfiguracaoSeiGsuiteVO configuracaoSeiGsuiteVO, String url, RequestMethod requestMethod, Map<String, String> headers,  UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(configuracaoSeiGsuiteVO), "Não foi localizado uma configuração Sei Gsuite para realizar operação do evento Google Meet.");
//		TokenVO token = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarTokenVO(configuracaoSeiGsuiteVO);
		Uteis.checkState(requestMethod == null, "O tipo de requisição não foi informado.");
//		return unirestHeaders(token, configuracaoSeiGsuiteVO.getUrlExternaSeiGsuite() + url, requestMethod, headers, usuarioVO);
		return null;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public kong.unirest.HttpResponse<JsonNode> unirestHeaders(TokenVO token, String url, RequestMethod requestMethod, Map<String, String> headers,  UsuarioVO usuarioVO) throws Exception {
		kong.unirest.HttpResponse<JsonNode> jsonResponse = null;
		HttpRequestWithBody requestBodyEntity = null;
		Uteis.checkState(requestMethod == null, "O tipo de requisição não foi informado.");
		switch (requestMethod) {
		case POST:
			requestBodyEntity = unirest().post(url)
			.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(UteisWebServiceUrl.ul, usuarioVO.getCodigo().toString());
			break;
		case PUT:
			requestBodyEntity = unirest().put(url)
			.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(UteisWebServiceUrl.ul, usuarioVO.getCodigo().toString());
			break;
		case DELETE:
			requestBodyEntity = unirest().delete(url)
			.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(UteisWebServiceUrl.ul, usuarioVO.getCodigo().toString());
			break;
		case GET:
			GetRequest getRequest = unirest().get(url)
			.header(UteisWebServiceUrl.Authorization, token.getToken_type() + " " + token.getAccess_token())
			.header(UteisWebServiceUrl.Content_Type, UteisWebServiceUrl.Application_JSON)
			.header(UteisWebServiceUrl.ul, usuarioVO.getCodigo().toString());
			if(headers != null && headers.size() > 0) {
				getRequest.headers(headers);
			}
			jsonResponse =  getRequest.asJson();
			break;
		default:
			throw new StreamSeiException("Opção de RequestMethod não implementada "+requestMethod.name());
		}
		if(!requestMethod.equals(RequestMethod.GET)) {
			if(headers != null && headers.size() > 0) {
				requestBodyEntity.headers(headers);	
			}
			jsonResponse = requestBodyEntity.asJson();
		}
		
		if (jsonResponse != null && !jsonResponse.isSuccess()) {
			throw new StreamSeiException((String) jsonResponse.getBody().getObject().get("message"));
		}
		return jsonResponse;
	}
	
	public <T> List<T> executarVerificacaoRetornoLista(kong.unirest.HttpResponse<JsonNode> jsonResponse, Class<T> type, String key) throws StreamSeiException {
		if(jsonResponse.getBody().toString().contains(key)) {
			List<T> lista = new ArrayList<>();
			Gson gson = new Gson();
			Type collectionType = TypeToken.getParameterized(List.class, type).getType();
			if (jsonResponse.getStatus() == 200 && !jsonResponse.getBody().isArray()) {
				String json = jsonResponse.getBody().getObject().get(key).toString();
				return gson.fromJson(json, collectionType);
			} else if (jsonResponse.getStatus() == 200  &&  jsonResponse.getBody().isArray()) {
				JSONArray jsonArray = jsonResponse.getBody().getArray();
				jsonArray.forEach(issueNode -> {
					JSONObject issue = (JSONObject) issueNode;
					String json = issue.get(key).toString();
					lista.addAll((gson.fromJson(json, collectionType)));
				});
				return lista;
			} 
			throw new StreamSeiException("Erro ao tentar verificar o Retorno do Serviço Sei de Lista.");
		}
		throw new StreamSeiException("Erro ao tentar verificar o Retorno do Serviço Sei de Lista. Não foi localizado a Key-"+key+" na resposta do serviço.");
	}

	public <T> T executarVerificacaoRetornoObjeto(kong.unirest.HttpResponse<JsonNode> jsonResponse, Class<T> type, String key) throws StreamSeiException {
		Gson gson = new Gson();
		Type collectionType = TypeToken.getParameterized(type, type).getType();
		if (jsonResponse.getStatus() == 200 && !jsonResponse.getBody().isArray() && Uteis.isAtributoPreenchido(key)) {
			String json = jsonResponse.getBody().getObject().get(key).toString();
			return gson.fromJson(json, collectionType);
		}else if (jsonResponse.getStatus() == 200 && !jsonResponse.getBody().isArray() && !Uteis.isAtributoPreenchido(key)) {
			return gson.fromJson(jsonResponse.getBody().toString(), collectionType);
		} else if (jsonResponse.getStatus() == 200  &&  jsonResponse.getBody().isArray()) {
			throw new StreamSeiException("Não foi possível realizar a operação de Integração pois o retorno é uma Lista");
		}else if (!jsonResponse.isSuccess() && jsonResponse.getStatus() != 200 ) {
			throw new StreamSeiException("Não foi possível realizar a operação -"+jsonResponse.getBody().toString());
		} 
		throw new StreamSeiException("Erro ao tentar verificar o Retorno do Serviço Sei de Objeto.");
	}
}
