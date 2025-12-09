package negocio.facade.jdbc.arquitetura;

import static java.lang.Math.max;
import static negocio.comuns.utilitarias.Uteis.calcularSomaSemestres;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.google.gson.Gson;
import controle.arquitetura.DataModelo;
import jobs.JobEnvioEmail;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.LogAlteracaoSenhaVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAdministrativoEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoAlunoEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoAlunoEnum;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.ReturnResponseMinhaBibliotecaVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.estagio.DashboardEstagioVO;
import negocio.comuns.estagio.enumeradores.SituacaoEstagioEnum;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.protocolo.enumeradores.TipoPoliticaDistribuicaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PlataformaEnum;
import negocio.comuns.utilitarias.dominios.TipoUsuario;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.arquitetura.UsuarioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>UsuarioVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>UsuarioVO</code>. Encapsula toda a interação com o banco de dados.
 *
 * @see UsuarioVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Usuario extends ControleAcesso implements UsuarioInterfaceFacade {

	protected static String idEntidade;
	private final static String SUCESSO = "Usuários exluídos com sucesso";
	private final static String ERRO = "Usuários não excluídos";

	public Usuario() throws Exception {
		super();
		setIdEntidade("Usuario");
	}
	
	public Usuario(Conexao conexao) throws Exception {
		super();
		setConexao(conexao);
		setIdEntidade("Usuario");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>UsuarioVO</code>.
	 */
	public UsuarioVO novo() throws Exception {
		Usuario.incluir(getIdEntidade());
		UsuarioVO obj = new UsuarioVO();
		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioVO validarUsuarioExistente(PessoaVO pessoaVO, String matricula, UsuarioVO usuarioLogado) throws Exception {
		UsuarioVO usuarioExistente = getFacadeFactory().getUsuarioFacade().consultarPorUsernameUnico(pessoaVO.getCPF(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuarioLogado);
		if(usuarioExistente == null && matricula != null && !matricula.isEmpty()){
	        usuarioExistente = getFacadeFactory().getUsuarioFacade().consultarPorUsernameUnico(matricula, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuarioLogado);
	    }
		if(usuarioExistente == null){
	    	usuarioExistente = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(pessoaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	    }
		return usuarioExistente;
	}
	
//	@Override
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public void criarNovoUsuario(PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, PerfilAcessoVO perfilAcessoVO, String matricula, String tipoUsuario, ConfiguracaoLdapVO configuracaoLdapVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioLogado) throws Exception {
//		UsuarioVO usuarioIncluir = new UsuarioVO();
//		usuarioIncluir.criarUsuario(pessoaVO, unidadeEnsinoVO, perfilAcessoVO, tipoUsuario, matricula, configuracaoGeralSistema);
//		String senhaAntesCriptografia = usuarioIncluir.getSenha();
//		getFacadeFactory().getUsuarioFacade().incluir(usuarioIncluir, false, usuarioLogado);
//		ConfiguracaoLdapVO confLdap = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarPorCodigo(configuracaoLdapVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
//		if(Uteis.isAtributoPreenchido(confLdap) && Uteis.isAtributoPreenchido(confLdap.getPrefixoSenha())) {
//			senhaAntesCriptografia = confLdap.getPrefixoSenha() + Uteis.removerMascara(pessoaVO.getCPF());	
//		}
//		getFacadeFactory().getLdapFacade().executarSincronismoComLdap(confLdap, usuarioIncluir, senhaAntesCriptografia, null ,false);
//		
//		
//	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarRegistroUsuario2(PessoaVO pessoaVO, UnidadeEnsinoVO unidadeEnsinoVO, PerfilAcessoVO PerfilAcessoVO, String matricula, String tipoUsuario,  ConfiguracaoLdapVO configuracaoLdapVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioLogado) throws Exception {
		UsuarioVO usuarioExistente = null;
		if (configuracaoGeralSistema.getGerarSenhaCpfAluno()) {
			usuarioExistente = getFacadeFactory().getUsuarioFacade().consultarPorUsernameUnico(pessoaVO.getCPF(), false, Uteis.NIVELMONTARDADOS_TODOS,usuarioLogado);
		} else {
			usuarioExistente = getFacadeFactory().getUsuarioFacade().consultarPorUsernameUnico(matricula, false, Uteis.NIVELMONTARDADOS_TODOS,usuarioLogado);
		}
		if(usuarioExistente == null){
			usuarioExistente = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(pessoaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		if (usuarioExistente == null || usuarioExistente.getCodigo().intValue() == 0) {
			//criarNovoUsuario(pessoaVO, unidadeEnsinoVO, PerfilAcessoVO, matricula, tipoUsuario, null, configuracaoGeralSistema, usuarioLogado);
		} else if(usuarioExistente.getPessoa().getCodigo().equals(pessoaVO.getCodigo())){
			
		}
	}
	
	@Override
	public String executarVerificacaoDeUsernameValida(PessoaVO pessoa, Integer tentativa, Integer qtdeLimiteCaracteres) throws Exception {
		String tentativaUsername = null;
		if (tentativa.equals(1) || tentativa.equals(7)) {
			tentativaUsername = executarCriacaoEmailRegra(pessoa);
			if (tentativa.equals(7)) {
				tentativaUsername = tentativaUsername + Uteis.getData(pessoa.getDataNasc(), "ddMMyyyy");
			}
		}else if (tentativa.equals(2) || tentativa.equals(8)) {
			tentativaUsername = executarCriacaoEmailPrimeiraRegra(pessoa);
			if (tentativa.equals(8)) {
				tentativaUsername = tentativaUsername + Uteis.getData(pessoa.getDataNasc(), "ddMMyyyy");
			}
		} else if (tentativa.equals(3) || tentativa.equals(9)) {
			tentativaUsername = executarCriacaoEmailSegundaRegra(pessoa);
			if (tentativa.equals(9)) {
				tentativaUsername = tentativaUsername + Uteis.getData(pessoa.getDataNasc(), "ddMMyyyy");
			}
		} else if (tentativa.equals(4) || tentativa.equals(10)) {
			tentativaUsername = executarCriacaoEmailTerceiraRegra(pessoa);
			if (tentativa.equals(10)) {
				tentativaUsername = tentativaUsername + Uteis.getData(pessoa.getDataNasc(), "ddMMyyyy");
			}
		} else if (tentativa.equals(5) || tentativa.equals(11)) {
			tentativaUsername = executarCriacaoEmailQuartaRegra(pessoa);
			if (tentativa.equals(11)) {
				tentativaUsername = tentativaUsername + Uteis.getData(pessoa.getDataNasc(), "ddMMyyyy");
			}
		} else if (tentativa.equals(6) || tentativa.equals(12)) {
			tentativaUsername = executarCriacaoEmailQuintaRegra(pessoa);
			if (tentativa.equals(12)) {
				tentativaUsername = tentativaUsername + Uteis.getData(pessoa.getDataNasc(), "ddMMyyyy");
			}
		} else if (tentativa > 12) {
			throw new Exception("Todas as tentativas de criação para username foram esgotadas para a pessoa de código:"+ pessoa.getCodigo() +" - nome:"+pessoa.getNome());
		}
		if(qtdeLimiteCaracteres > 0  && tentativaUsername.length() > qtdeLimiteCaracteres) {
			tentativaUsername = tentativaUsername.substring(0, qtdeLimiteCaracteres);	
		}
		if (!tentativaUsername.isEmpty() && !validarUnicidadeUsername(tentativaUsername, null)) {
			return tentativaUsername.toLowerCase();
		}
		return executarVerificacaoDeUsernameValida(pessoa, tentativa + 1, qtdeLimiteCaracteres);

	}
	
	/**
	 * nome + ultimo sobrenome completo ex: rodrigo.wind
	 * @param pessoa
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String executarCriacaoEmailRegra(PessoaVO pessoa) {
		String nome = Uteis.removeCaractersEspeciais(pessoa.getNome().trim().toLowerCase());
		nome = Uteis.removeCaractersAspas(nome);
		String [] arrays = nome.split(" ");
		int tamanho = 1;
		StringBuilder sb = new StringBuilder();
		for (String array : arrays) {
			if((sb.toString().isEmpty() && !array.isEmpty()) || (tamanho == arrays.length)) {
				if(tamanho == arrays.length) {
					sb.append(".");
				}
				sb.append(array);
			}
			tamanho++;
		}
		return sb.toString();
	}
	
	/**
	 * nome + primeira letra dos primeiros sobrenomes + ultimo sobrenome completo ex: rodrigo.j.wind
	 * @param pessoa
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String executarCriacaoEmailPrimeiraRegra(PessoaVO pessoa) {
		String nome = Uteis.removeCaractersEspeciais(pessoa.getNome().trim().toLowerCase());
		nome = Uteis.removeCaractersAspas(nome);
		String [] arrays = nome.split(" ");
		int tamanho = 1;
		StringBuilder sb = new StringBuilder();
		for (String array : arrays) {
			if((sb.toString().isEmpty() && !array.isEmpty()) || (tamanho == arrays.length)) {
				if(tamanho == arrays.length) {
					sb.append(".");
				}
				sb.append(array);
			}else if (!array.isEmpty() && array.length() >=1 && Uteis.isDiferentePreposicao(array)) {
				sb.append(".").append(array.substring(0,1));
			}
			tamanho++;
		}
		return sb.toString();
	}
	
	/**
	 * nome + sobrenome completo + ultimo sobrenome completo ex: rodrigojaymewind
	 * @param pessoa
	 * @return
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String executarCriacaoEmailSegundaRegra(PessoaVO pessoa) {
		String nome = Uteis.removeCaractersEspeciais(pessoa.getNome().trim().toLowerCase());
		nome = Uteis.removeCaractersAspas(nome);
		String [] arrays = nome.split(" ");
		int tamanho = 1;
		StringBuilder sb = new StringBuilder();
		for (String array : arrays) {
			if((sb.toString().isEmpty() && !array.isEmpty()) || (Uteis.isDiferentePreposicao(array)) ||  (tamanho == arrays.length)) {
				sb.append(array);
			}
			tamanho++;
		}
		return sb.toString();
	}
	
	/**
	 * nome + sobrenome completo + primeiraletra do ultimo sobrenomes ex: rodrigojaymew
	 * @param pessoa
	 * @return
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String executarCriacaoEmailTerceiraRegra(PessoaVO pessoa) {
		String nome = Uteis.removeCaractersEspeciais(pessoa.getNome().trim().toLowerCase());
		nome = Uteis.removeCaractersAspas(nome);
		String [] arrays = nome.split(" ");
		int tamanho = 1;
		StringBuilder sb = new StringBuilder();
		for (String array : arrays) {
			if(tamanho != arrays.length && ((sb.toString().isEmpty() && !array.isEmpty()) || (Uteis.isDiferentePreposicao(array)))) {
				sb.append(array);
			}else if(!array.isEmpty() && tamanho == arrays.length){
				sb.append(".").append(array.substring(0,1));
			}
			tamanho++;
		}
		return sb.toString();
	}
	
	/**
	 * 	nome + primeiraletra do primeiro sobrenomes + primeiraletra do ultimo sobrenomes ex: rodrigojw
	 * @param pessoa
	 * @return
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String executarCriacaoEmailQuartaRegra(PessoaVO pessoa) {
		String nome = Uteis.removeCaractersEspeciais(pessoa.getNome().trim().toLowerCase());
		nome = Uteis.removeCaractersAspas(nome);
		String [] arrays = nome.split(" ");
		int tamanho = 1;
		StringBuilder sb = new StringBuilder();
		for (String array : arrays) {
			if((sb.toString().isEmpty() && !array.isEmpty())) {
				sb.append(array);
			}else if(!array.isEmpty() && array.length() >=1 && (Uteis.isDiferentePreposicao(array) || tamanho == arrays.length)){
				sb.append(".").append(array.substring(0,1));
			}
			tamanho++;
		}
		return sb.toString();
	}
	
	/**
	 * 	Inicial do e-mail principal adicionando o dominio da instituição ex: rodrigojawind@hotmail.com = rodrigojawind@<dominio da instituição>
	 * @param pessoa
	 * @return
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String executarCriacaoEmailQuintaRegra(PessoaVO pessoa) {
		if(Uteis.isAtributoPreenchido(pessoa.getEmail()) && pessoa.getEmail().contains("@")) {
			return pessoa.getEmail().substring(0, pessoa.getEmail().indexOf("@"));	
		}
		return "";
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>UsuarioVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados
	 * e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>UsuarioVO</code> que será gravado no banco
	 *            de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final UsuarioVO obj, UsuarioVO usuario) throws Exception {
		incluir(obj, true, usuario);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final UsuarioVO obj, final boolean verificarPermissao, UsuarioVO usuario) throws Exception {
		String senha =  obj.getSenha();
		try {			
			Usuario.incluir(getIdEntidade(), verificarPermissao, usuario);
			UsuarioVO.validarDados(obj);
			if (validarUnicidadeUsername(obj.getUsername(), obj.getPessoa().getCodigo())) {
				throw new Exception("Já existe um usuário cadastrado com este USERNAME");
			}
			validarUnicidadeTipoUsuario(obj);
			criptografarSenhaUsuario(obj, obj.getSenha());			
			final String sql = "INSERT INTO Usuario( nome, username, senha,  pessoa, tipoUsuario, perfilAdministrador, parceiro, dataultimaalteracao, senhaSHA, senhaMSCHAPV2) " + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getUsername());
					sqlInserir.setString(3, obj.getSenha());
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getTipoUsuario());
					sqlInserir.setBoolean(6, obj.getPerfilAdministrador().booleanValue());
					if (obj.getParceiro().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getParceiro().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setString(9, obj.getSenhaSHA());
					sqlInserir.setString(10, obj.getSenhaMSCHAPV2());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getUsuarioPerfilAcessoFacade().incluirUsuarioPerfilAcesso(obj.getCodigo(), obj.getUsuarioPerfilAcessoVOs(), usuario);
			realizarValidacaoSeDeverarSerAtualizadaTabelaPessoa(obj, verificarPermissao, usuario);
		} catch (Exception e) {
			obj.setSenha(senha);
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final UsuarioVO obj, UsuarioVO usuario, PerfilAcessoPermissaoEnumInterface... permissoes) throws Exception {
		alterar(obj, true, usuario, permissoes);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>UsuarioVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco
	 * de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>UsuarioVO</code> que será alterada no banco
	 *            de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final UsuarioVO obj, boolean verificarAcesso, UsuarioVO usuario, PerfilAcessoPermissaoEnumInterface... permissoes) throws Exception {
		String senha = "";
		if (obj.getAlterarSenha()) {
			senha =  obj.getSenha();
		}
		try {
			Usuario.alterar(getIdEntidade(), verificarAcesso, usuario);
			UsuarioVO.validarDados(obj);
			validarUnicidadeTipoUsuario(obj);
			validarPermissoes(permissoes, obj, usuario);
			if (obj.getTipoUsuario().equals("PA")) {
				if (validarUnicidadeUsernameParceiro(obj.getUsername(), obj.getParceiro().getCodigo())) {
					throw new Exception("Já existe um usuário cadastrado com este USERNAME");
				} else {
				}
				if (validarUnicidadeUsername(obj.getUsername(), obj.getPessoa().getCodigo())) {
					throw new Exception("Já existe um usuário cadastrado com este USERNAME");
				}
			}
			if (obj.getAlterarSenha()) {
				getFacadeFactory().getLogAlteracaoSenhaFacade().consultarSenhaJaUtilizada(senha, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null), obj);
				criptografarSenhaUsuario(obj, senha);				
			}				
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					String sql = "UPDATE Usuario set nome=?, username=?,  pessoa=? , tipousuario=?, perfilAdministrador=?, parceiro=?, dataultimaalteracao=? , qtdFalhaLogin=0, dataFalhaLogin=now(), usuarioBloqPorFalhaLogin=false ";
					if (obj.getAlterarSenha()) {
						sql += ", senha=?,  senhaSHA=?, senhaMSCHAPV2=? ";
					}
					sql += " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);			
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					int x = 1;					
						sqlAlterar.setString(x++, obj.getNome());
						sqlAlterar.setString(x++, obj.getUsername());												
						if (obj.getPessoa().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(x++, obj.getPessoa().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(x++, 0);
						}
						sqlAlterar.setString(x++, obj.getTipoUsuario());
						sqlAlterar.setBoolean(x++, obj.getPerfilAdministrador().booleanValue());
						if (obj.getParceiro().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(x++, obj.getParceiro().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(x++, 0);
						}
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
						if (obj.getAlterarSenha()) {
							
							sqlAlterar.setString(x++, obj.getSenha());
							sqlAlterar.setString(x++, obj.getSenhaSHA());
							sqlAlterar.setString(x++, obj.getSenhaMSCHAPV2());
						}
						sqlAlterar.setInt(x++, obj.getCodigo().intValue());


					return sqlAlterar;
				}
			});
			if (obj.getAlterarSenha()) {				
				gravarLogAlteracaoSenha(obj, obj.getSenha(), usuario);			
			}
			getFacadeFactory().getUsuarioPerfilAcessoFacade().alterarUsuarioPerfilAcesso(obj.getCodigo(), obj.getUsuarioPerfilAcessoVOs(), usuario);
			realizarValidacaoSeDeverarSerAtualizadaTabelaPessoa(obj, verificarAcesso, usuario);
		} catch (Exception e) {
			if (obj.getAlterarSenha()) {
				usuario.setSenha(senha);
			}
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSenha(Boolean lembrarDeSincronizarComLdapNoFinalDaTransacao, final UsuarioVO obj) throws Exception {
		String senha  = obj.getSenha();
		try {
			getFacadeFactory().getLogAlteracaoSenhaFacade().consultarSenhaJaUtilizada(senha, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null), obj);
			final String sql = "UPDATE Usuario set senha=?, senhaSHA=?, senhaMSCHAPV2=?, tokenRedefinirSenha=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(obj);
			criptografarSenhaUsuario(obj, senha);	
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSenha());
					sqlAlterar.setString(2, obj.getSenhaSHA());
					sqlAlterar.setString(3, obj.getSenhaMSCHAPV2());					
					sqlAlterar.setString(4, "");					
					sqlAlterar.setInt(5, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			gravarLogAlteracaoSenha(obj, obj.getSenha(), obj);			
		} catch (Exception e) {
			obj.setSenha(senha);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void registrarFalhaTentativaLogin(String username, int qtdTentativas) throws Exception {
		try {
			final String sql = "UPDATE Usuario set qtdFalhaLogin= case when datafalhalogin::date = current_date then (qtdFalhaLogin+1) else 0 end, datafalhalogin=now(), usuarioBloqPorFalhaLogin= case when (qtdFalhaLogin >= " + qtdTentativas + ") then true else false end WHERE ((username = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, username);
					return sqlAlterar;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSenhaPrimeiroAcesso(Boolean lembrarDeSincronizarComLdapNoFinalDaTransacao, final UsuarioVO obj, ConfiguracaoGeralSistemaVO config) throws Exception {
		String senha  = obj.getSenha();
		try {
			Uteis.validarSenha(config, senha);
			getFacadeFactory().getLogAlteracaoSenhaFacade().consultarSenhaJaUtilizada(senha, config, obj);
			final String sql = "UPDATE Usuario set senha=?, senhaSHA=?, senhaMSCHAPV2=?, resetouSenhaPrimeiroAcesso=true WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(obj);
			criptografarSenhaUsuario(obj, senha);	
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSenha());
					sqlAlterar.setString(2, obj.getSenhaSHA());
					sqlAlterar.setString(3, obj.getSenhaMSCHAPV2());					
					sqlAlterar.setInt(4, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			gravarLogAlteracaoSenha(obj, obj.getSenha(), obj);
		} catch (Exception e) {
			obj.setSenha(senha);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTokenRedefinirSenha(final UsuarioVO obj) throws Exception {
		try {
			final String sql = "UPDATE Usuario set tokenRedefinirSenha=? WHERE (codigo = ?) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(obj);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getTokenRedefinirSenha());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarNomeUsuario(final Integer codPessoa, final String nome, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Usuario set nome=? WHERE pessoa = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, nome);
					sqlAlterar.setInt(2, codPessoa);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTipoUsuario(final Integer pessoa, final String tipoUsuario, final UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Usuario set tipoUsuario=? WHERE ((pessoa = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, tipoUsuario);
					sqlAlterar.setInt(2, pessoa);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void realizarValidacaoSenhaPessoa(Integer pessoa, String senha) throws Exception {
		try {
			StringBuilder sb = new StringBuilder("select codigo from usuario where pessoa = ? and senha = ? ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), pessoa, Uteis.encriptar(senha));
			if (!rs.next()) {
				throw new Exception(UteisJSF.internacionalizar("msg_senha_invalida"));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void registrarUltimoAcesso(final UsuarioVO obj) throws Exception {
		try {
			final String sql = "UPDATE Usuario set ipUltimoAcesso = ?, dataUltimoAcesso = ?, qtdfalhalogin = 0, usuariobloqporfalhalogin = false WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(obj);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getIpUltimoAcesso());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void registrarPrimeiroAcesso(final UsuarioVO obj) throws Exception {
		try {
			final String sql = "UPDATE Usuario set dataPrimeiroAcesso=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(obj);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(new Date()));
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSenha(final UsuarioVO obj, String usernameAtual, String senhaAtual, final String usernameNovo, final String senhaNova, final boolean alterarSenhaContaGsuite) throws Exception {
		try {
			UsuarioVO.validarDadosAlterarSenha(obj, usernameAtual, senhaAtual, usernameNovo, senhaNova);
			getFacadeFactory().getLogAlteracaoSenhaFacade().consultarSenhaJaUtilizada(senhaNova, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null), obj);
			criptografarSenhaUsuario(obj, senhaNova);			
			final String sql = "UPDATE Usuario set username=?, senha=?, qtdFalhaLogin=0, dataFalhaLogin=now(), usuarioBloqPorFalhaLogin=false, senhaSHA=?, senhaMSCHAPV2=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(obj);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);					
					sqlAlterar.setString(1, usernameNovo);
					sqlAlterar.setString(2, obj.getSenha());
					sqlAlterar.setString(3, obj.getSenhaSHA());
					sqlAlterar.setString(4, obj.getSenhaMSCHAPV2());
					sqlAlterar.setInt(5, obj.getCodigo().intValue());					
					return sqlAlterar;
				}
			});
			if(alterarSenhaContaGsuite) {
				getFacadeFactory().getAdminSdkIntegracaoFacade().executarAlteracaoSenhaContaGsuite(obj.getPessoa(), senhaNova, senhaNova, obj);
			}
			gravarLogAlteracaoSenha(obj, obj.getSenha(), obj);
			obj.setUsername(usernameNovo);			
		} catch (Exception e) {
			throw e;
		}
	}

	public void validarDadosUserNameSenha(String userName, String senha) throws Exception {
		if (userName.equals("")) {
			throw new Exception("O campo UserName deve ser informado");
		}
		if (senha.equals("")) {
			throw new Exception("O campo Senha deve ser informado");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarUserNameSenhaAlteracaoSenhaAluno(final UsuarioVO obj, final String usernameNovo, final String senhaNova ,Boolean validarUnicidadeSenhaJaUtilizada , UsuarioVO usuarioLogado) throws Exception {
		try {
			validarDadosUserNameSenha(usernameNovo, senhaNova);
			if (validarUnicidadeUsername(usernameNovo, obj.getPessoa().getCodigo())) {
				throw new Exception("Já existe um usuário cadastrado com este USERNAME");
			}	
			if(validarUnicidadeSenhaJaUtilizada) {	
				getFacadeFactory().getLogAlteracaoSenhaFacade().consultarSenhaJaUtilizada(senhaNova, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null), obj);
			}
			criptografarSenhaUsuario(obj, senhaNova);
			if (Uteis.isAtributoPreenchido(obj.getPessoa().getCodigo()) && Uteis.isAtributoPreenchido(obj.getPessoa().getEmail().trim())) {
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
					String sql = "UPDATE pessoa set email=?, dataultimaalteracao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);

					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement sqlAlterar = con.prepareStatement(sql);
						sqlAlterar.setString(1, obj.getPessoa().getEmail());
						sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
						sqlAlterar.setInt(3, obj.getPessoa().getCodigo());
						return sqlAlterar;
					}
				});
			}				
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {				
				String sql = "UPDATE Usuario set username=?, senha=?, dataultimaalteracao=?, senhaSHA=?, senhaMSCHAPV2=? WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);					
						sqlAlterar.setString(1, usernameNovo);
						sqlAlterar.setString(2, obj.getSenha());
						sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(new Date()));
						sqlAlterar.setString(4, obj.getSenhaSHA());
						sqlAlterar.setString(5, obj.getSenhaMSCHAPV2());
						sqlAlterar.setInt(6, obj.getCodigo().intValue());					
					return sqlAlterar;
				}
			});
			gravarLogAlteracaoSenha(obj, obj.getSenha(), usuarioLogado);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>UsuarioVO</code>. Sempre localiza o registro a ser excluído através da
	 * chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>excluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>UsuarioVO</code> que será removido no banco
	 *            de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UsuarioVO obj, UsuarioVO usuario) throws Exception {
		try {
			Usuario.excluir(getIdEntidade());
			getFacadeFactory().getUsuarioPerfilAcessoFacade().excluirUsuarioPerfilAcesso(obj.getCodigo(), usuario);
			String sql = "DELETE FROM Usuario WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Usuario</code> através do
	 * valor do atributo <code>nome</code> da classe <code>PerfilAcesso</code> Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe <code>UsuarioVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomePerfilAcesso(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct Usuario.* FROM Usuario ");
		sb.append("inner join UsuarioPerfilAcesso on Usuario.codigo = UsuarioPerfilAcesso.Usuario ");
		sb.append("inner join PerfilAcesso on UsuarioPerfilAcesso.PerfilAcesso = PerfilAcesso.codigo ");
		sb.append("WHERE lower (sem_acentos(PerfilAcesso.nome)) like sem_acentos(?) ");
		if (codUnidadeEnsino != null) {
			if (codUnidadeEnsino != 0) {
				sb.append("and (UsuarioPerfilAcesso.unidadeensino = ").append(codUnidadeEnsino).append(" or usuario.tipousuario in('AL', 'RF', 'PR', 'RL')) ");
			}
		}
		sb.append(" ORDER BY usuario.nome");
		if (limite != null) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sb.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<UsuarioVO> consultarUsuarioPorUnidadeEnsino(String valorConsulta, int unidadeEnsino, int limit, int offset, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder stringJoiner = new StringBuilder(" ");
		consultarUsuarioPorUnidadeEnsinoConsulta(valorConsulta, unidadeEnsino, usuario, stringJoiner);
		stringJoiner.append(" order by usuario.codigo");
		UteisTexto.addLimitAndOffset(stringJoiner, limit, offset);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(stringJoiner.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	private void consultarUsuarioPorUnidadeEnsinoConsulta(String valorConsulta, int unidadeEnsino, UsuarioVO usuario, StringBuilder stringJoiner) {
		stringJoiner.append(" select distinct usuario.* from usuario");
		stringJoiner.append(" left join pessoa on usuario.pessoa = pessoa.codigo");
		stringJoiner.append(" left join funcionario on funcionario.pessoa = pessoa.codigo");
		stringJoiner.append(" left join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo");
		stringJoiner.append(" left join usuarioperfilacesso on usuario.codigo =  usuarioperfilacesso.usuario");
		stringJoiner.append(" where funcionario.codigo is not null");
		stringJoiner.append(String.format(" and lower(sem_acentos(usuario.nome)) ilike sem_acentos('%%%s%%')", valorConsulta));
		if (getFacadeFactory().getRequisicaoFacade().getPermiteCadastrarRequisicaoTodasUnidadesEnsino(usuario) && Uteis.isAtributoPreenchido(unidadeEnsino)) {
			stringJoiner.append(String.format(" and usuarioperfilacesso.unidadeensino = %d", unidadeEnsino));
		}
	}

	@Override
	public int consultarUsuarioPorUnidadeEnsinoContador(String valorConsulta, int unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder stringJoiner = new StringBuilder(" ");
		consultarUsuarioPorUnidadeEnsinoConsulta(valorConsulta, unidadeEnsino, usuario, stringJoiner);
		return getConexao().getJdbcTemplate().queryForObject(String.format("select count(*) from (%s) contador", stringJoiner.toString()), Integer.class);
	}

	public Integer consultarTotalDeRegistroPorNomePerfilAcesso(String valorConsulta, Integer codUnidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(distinct Usuario.codigo) FROM Usuario ");
		sb.append("inner join UsuarioPerfilAcesso on Usuario.codigo = UsuarioPerfilAcesso.Usuario ");
		sb.append("inner join PerfilAcesso on UsuarioPerfilAcesso.PerfilAcesso = PerfilAcesso.codigo ");
		sb.append("WHERE lower (sem_acentos(PerfilAcesso.nome)) like sem_acentos(?)");
		if (codUnidadeEnsino != null) {
			if (codUnidadeEnsino != 0) {
				sb.append("and (UsuarioPerfilAcesso.unidadeensino = ").append(codUnidadeEnsino).append(" or usuario.tipousuario in('AL', 'RF', 'PR', 'RL')) ");
			}
		}

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta.toLowerCase()+"%");
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	public List consultarPorTipoUsuario(String valorConsulta, String nome, Integer codUnidadeEnsino, Integer limite, Integer offset, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct Usuario.* FROM Usuario ");
		sb.append("left join UsuarioPerfilAcesso on Usuario.codigo = UsuarioPerfilAcesso.Usuario ");
		sb.append("WHERE usuario.tipoUsuario ilike '").append(valorConsulta).append("' ");
		sb.append("and usuario.nome ilike '").append(nome).append("%' ");
		if (codUnidadeEnsino != null) {
			if (codUnidadeEnsino != 0) {
				sb.append("and (UsuarioPerfilAcesso.unidadeensino = ").append(codUnidadeEnsino).append(" or usuario.tipousuario in('AL', 'RF', 'PR', 'RL')) ");
			}
		}
		sb.append(" ORDER BY usuario.nome");
		if (limite != null) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sb.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List consultarPorTipoUsuario(TipoUsuario tipoUsuario) throws Exception {
    	StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct Usuario.* FROM Usuario ");
        sb.append("left join UsuarioPerfilAcesso on Usuario.codigo = UsuarioPerfilAcesso.Usuario ");
        sb.append("WHERE usuario.tipoUsuario = '").append(tipoUsuario.getValor()).append("' limit 1");
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, null);
    }

	public Integer consultarTotalDeRegistroPorTipoUsuario(String valorConsulta, String nome, Integer codUnidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(distinct Usuario.codigo) FROM Usuario ");
		sb.append("left join UsuarioPerfilAcesso on Usuario.codigo = UsuarioPerfilAcesso.Usuario ");
		sb.append("WHERE usuario.tipoUsuario ilike '").append(valorConsulta).append("'");
		sb.append("and usuario.nome ilike '").append(nome).append("%'");
		if (codUnidadeEnsino != null) {
			if (codUnidadeEnsino != 0) {
				sb.append("and (UsuarioPerfilAcesso.unidadeensino = ").append(codUnidadeEnsino).append(" or usuario.tipousuario in('AL', 'RF', 'PR', 'RL')) ");
			}
		}

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	public List consultarPorUsername(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM Usuario WHERE lower (username) like'").append(valorConsulta.toLowerCase()).append("%'");
		sb.append(" ORDER BY username");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Usuario</code> através do
	 * valor do atributo <code>String username</code>. Retorna os objetos, com
	 * início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UsuarioVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorUsername(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT DISTINCT usuario.* FROM Usuario ");
		sb.append("left join usuarioperfilacesso up on up.usuario = usuario.codigo ");
		sb.append("WHERE username ilike '").append(valorConsulta.toLowerCase()).append("%' ");
		if (codUnidadeEnsino != 0) {
			sb.append("and (up.unidadeensino = ").append(codUnidadeEnsino).append(" or usuario.tipousuario in('AL', 'RF', 'PR', 'RL')) ");
		}
		sb.append(" ORDER BY nome");
		if (limite != null) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sb.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<UsuarioVO> consultarPorUsernameTipoEspecificoUsuarioAlunoProfessorCoordenador(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
		sb.append(ControleAcesso.getSQLConsultaCompletaUsuario());
		sb.append(" WHERE usuario.username ilike ? ");
		sb.append(" and usuario.tipousuario in ('AL','CO','PR','DM','FU')) AS t");
		sb.append(" ORDER BY \"usuario.nome\"");
		if (limite != null) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sb.append(" OFFSET ").append(offset);
			}
		}
		dataModelo.setTotalRegistrosEncontrados(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsultaUsuario(tabelaResultado, nivelMontarDados);
	}
	
	@Override
	public List<UsuarioVO> consultarPorMatriculaTipoEspecificoUsuarioAlunoProfessorCoordenador(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
		sb.append(ControleAcesso.getSQLConsultaCompletaUsuario());	
		sb.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sb.append(" WHERE Matricula.matricula ilike ? ");	
		sb.append(" and usuario.tipousuario in ('AL','CO','PR','DM','FU')) AS t");
		sb.append(" ORDER BY \"usuario.nome\"");
		if (limite != null) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sb.append(" OFFSET ").append(offset);
			}
		}
		dataModelo.setTotalRegistrosEncontrados(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsultaUsuario(tabelaResultado, nivelMontarDados);
	}
	
	@Override
	public List<UsuarioVO> consultarPorRegistroAcademicoTipoEspecificoUsuarioAlunoProfessorCoordenador(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
		sb.append(ControleAcesso.getSQLConsultaCompletaUsuario());	
		sb.append(" WHERE pessoa.registroAcademico ilike ? ");
		sb.append(" and   usuario.tipousuario in ('AL','CO','PR','DM','FU')) AS t");
		sb.append(" ORDER BY \"usuario.nome\"");
		if (limite != null) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sb.append(" OFFSET ").append(offset);
			}
		}
		dataModelo.setTotalRegistrosEncontrados(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsultaUsuario(tabelaResultado, nivelMontarDados);
	}

	public Integer consultarTotalDeRegistroPorUsername(String valorConsulta, Integer codUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT COUNT(DISTINCT usuario.codigo) FROM Usuario ");
		sb.append("left join usuarioperfilacesso up on up.usuario = usuario.codigo ");
		sb.append("WHERE username ilike '").append(valorConsulta.toLowerCase()).append("%' ");
		if (codUnidadeEnsino != 0) {
			sb.append("and (up.unidadeensino = ").append(codUnidadeEnsino).append(" or usuario.tipousuario in('AL', 'RF', 'PR', 'RL')) ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	public UsuarioVO consultarPorUsernameUnico(String username, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM Usuario ");
		if (username != null && !username.isEmpty()) {
			sqlBuilder.append(" WHERE username = ('" + username.trim() + "') ORDER BY username");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlBuilder.toString());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return null;

	}

	public UsuarioVO consultarPorUsernameCPFUnico(String username, String cpf, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM Usuario left join pessoa on usuario.pessoa = pessoa.codigo ");
		if (username != null && !username.isEmpty()) {
			sqlBuilder.append(" WHERE username = ('" + username.trim() + "') ORDER BY username");
		} else {
			sqlBuilder.append(" WHERE sem_pontocpf(pessoa.cpf) = ('" + Uteis.retirarMascaraCPF(cpf.trim()) + "') ORDER BY username");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlBuilder.toString());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return null;

	}

	public UsuarioVO consultarPorTokenRedefinirSenha(String token) throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT * FROM Usuario  ");
		sqlBuilder.append(" WHERE tokenRedefinirSenha = '" + token + "' ORDER BY username");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlBuilder.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados. (Usuário)");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSLOGIN, new UsuarioVO()));
	}

	public UsuarioVO consultarUsuarioUnicoDMParaMatriculaCRM(int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select * from usuario where tipousuario = 'DM' order by codigo limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlBuilder.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados. (Usuário)");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Usuario</code> através do
	 * valor do atributo <code>String nome</code>. Retorna os objetos, com início do
	 * valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UsuarioVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNome(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlsb = new StringBuilder();
		sqlsb.append("SELECT DISTINCT usuario.* FROM Usuario ");
		sqlsb.append("left join usuarioperfilacesso up on up.usuario = usuario.codigo ");
		sqlsb.append("WHERE sem_acentos(nome) ilike sem_acentos(?) ");
		if (codUnidadeEnsino != 0) {
			sqlsb.append("and (up.unidadeensino = ").append(codUnidadeEnsino).append(" or usuario.tipousuario in('AL', 'RF', 'PR', 'RL')) ");
		}
		sqlsb.append(" ORDER BY nome");
		if (limite != null) {
			sqlsb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlsb.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString(), valorConsulta.toLowerCase() +"%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<UsuarioVO> consultarPorNomeTipoEspecificoUsuarioAlunoProfessorCoordenador(String valorConsulta, Integer codUnidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlsb = new StringBuilder(" SELECT count(*) over() as qtde_total_registros,usuario.* FROM usuario ");
		sqlsb.append(" WHERE sem_acentos(lower(usuario.nome)) ilike sem_acentos(lower(?)) ");
		sqlsb.append(" and usuario.tipousuario in('AL','CO','PR','DM','FU')");
		sqlsb.append(" ORDER BY usuario.nome ");
		if (limite != null) {
			sqlsb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlsb.append(" OFFSET ").append(offset);
			}
		}
		dataModelo.setTotalRegistrosEncontrados(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString(), valorConsulta + PERCENT);
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlsb = new StringBuilder();
		sqlsb.append("SELECT DISTINCT * FROM Usuario WHERE lower (sem_acentos(nome)) ilike(sem_acentos(?))");
		sqlsb.append(" ORDER BY nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString(), valorConsulta.toLowerCase()+"%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public Integer consultarTotalDeGegistroPorNome(String valorConsulta, Integer codUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlsb = new StringBuilder();
		sqlsb.append("SELECT DISTINCT COUNT (DISTINCT usuario.codigo) FROM Usuario ");
		sqlsb.append("left join usuarioperfilacesso up on up.usuario = usuario.codigo ");
		sqlsb.append("WHERE sem_acentos(nome) ilike sem_acentos(?) ");
		if (codUnidadeEnsino != 0) {
			sqlsb.append("and (up.unidadeensino = ").append(codUnidadeEnsino).append(" or usuario.tipousuario in('AL', 'RF', 'PR', 'RL')) ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString(), valorConsulta.toLowerCase()+"%" );
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	public List consultarUsuariosFuncionarios(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Usuario Where tipoUsuario = '" + valorConsulta + "' ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public UsuarioVO consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Usuario WHERE lower (nome) like('" + valorConsulta.toLowerCase() + "') ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new UsuarioVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Usuario</code> através do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores
	 * iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UsuarioVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Usuario WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));

	}
	
	public List consultarPorCodigoUnico(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Usuario WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List consultarPorCodigoPessoa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Usuario WHERE pessoa = " + valorConsulta.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigoParceiro(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Usuario WHERE parceiro = " + valorConsulta.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public UsuarioVO consultarPorPessoa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Usuario WHERE pessoa = " + valorConsulta.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new UsuarioVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public UsuarioVO consultarPorDepartamentoCargoEPoliticaDistribuicao(DepartamentoVO departamento, CargoVO cargo, TipoPoliticaDistribuicaoEnum tipoPoliticaDistribuicao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		switch (tipoPoliticaDistribuicao) {
		case DISTRIBUICAO_CIRCULAR:
			return this.buscaUsuarioMetodoCircular(departamento, cargo, nivelMontarDados, usuario);
		case DISTRIBUICAO_QUANTITATIVA:
			return this.buscaUsuarioMetodoQuantitativo(departamento, cargo, nivelMontarDados, usuario);
		}
		return null;
	}

	private UsuarioVO buscaUsuarioMetodoCircular(DepartamentoVO departamento, CargoVO cargo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringJoiner sql = new StringJoiner(" ");
		sql.add("SELECT USUARIO.*, COTACAOHISTORICO.DATAINICIO AS DATAINICIO, COTACAOHISTORICO.DATAFIM AS DATAFIM  FROM FUNCIONARIOCARGO");
		sql.add("INNER JOIN FUNCIONARIO ON FUNCIONARIOCARGO.FUNCIONARIO = FUNCIONARIO.CODIGO");
		sql.add("INNER JOIN PESSOA ON FUNCIONARIO.PESSOA = PESSOA.CODIGO");
		sql.add("INNER JOIN CARGO ON FUNCIONARIOCARGO.CARGO = CARGO.CODIGO");
		sql.add("INNER JOIN DEPARTAMENTO ON DEPARTAMENTO.CODIGO = case when FUNCIONARIOCARGO.DEPARTAMENTO is not null "); 
		sql.add("   then funcionariocargo.departamento"); 
		sql.add("	else cargo.departamento end ");
		sql.add("INNER JOIN USUARIO ON USUARIO.PESSOA = PESSOA.CODIGO");
		sql.add("FULL OUTER JOIN COTACAOHISTORICO ON USUARIO.CODIGO = COTACAOHISTORICO.USUARIO");
		sql.add("WHERE FUNCIONARIOCARGO.ATIVO = TRUE");

		if (Objects.nonNull(departamento) && Objects.nonNull(departamento.getCodigo())) {
			sql.add(String.format("AND DEPARTAMENTO.CODIGO = %s", departamento.getCodigo()));
		}
		if (Objects.nonNull(cargo) && Objects.nonNull(cargo.getCodigo())) {
			sql.add(String.format("AND CARGO.CODIGO = %s", cargo.getCodigo()));
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		ArrayList<UsuarioAgregador> usuarios = new ArrayList<UsuarioAgregador>();
		while (tabelaResultado.next()) {
			usuarios.add(new UsuarioAgregador(montarDados(tabelaResultado, nivelMontarDados, usuario), tabelaResultado.getDate("datafim"), tabelaResultado.getDate("datainicio")));
		}
		Comparator<UsuarioAgregador> sortByDate = (o1, o2) -> {
			if (o1.dataInicio == null) {
				return (o2.dataInicio == null) ? 0 : -1;
			}
			if (o2.dataInicio == null) {
				return 1;
			}
			return o2.dataInicio.compareTo(o1.dataInicio);
		};
		Optional<UsuarioAgregador> findFirst = usuarios.stream()
		        .sorted(sortByDate.thenComparing(UsuarioAgregador::getNome))
		        .findFirst();
		
		if (findFirst.isPresent()) {
			return findFirst.get().usuarioVO;	
		} else {
			return new UsuarioVO();
		}
		

	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	private UsuarioVO buscaUsuarioMetodoQuantitativo(DepartamentoVO departamento, CargoVO cargo, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		StringJoiner sql = new StringJoiner(" ");
		sql.add(" SELECT USUARIO.codigo, pessoa.nome, count(COTACAOHISTORICO.USUARIO ) as qtd");
		sql.add(" FROM FUNCIONARIOCARGO ");
		sql.add(" INNER JOIN FUNCIONARIO ON FUNCIONARIOCARGO.FUNCIONARIO = FUNCIONARIO.CODIGO ");
		sql.add(" INNER JOIN PESSOA ON FUNCIONARIO.PESSOA = PESSOA.CODIGO ");
		sql.add(" INNER JOIN CARGO ON FUNCIONARIOCARGO.CARGO = CARGO.CODIGO ");
		sql.add("INNER JOIN DEPARTAMENTO ON DEPARTAMENTO.CODIGO = case when FUNCIONARIOCARGO.DEPARTAMENTO is not null "); 
		sql.add("   then funcionariocargo.departamento"); 
		sql.add("	else cargo.departamento end ");
		sql.add(" INNER JOIN USUARIO ON USUARIO.PESSOA = PESSOA.CODIGO ");
		sql.add(" LEFT JOIN COTACAOHISTORICO ON USUARIO.CODIGO = COTACAOHISTORICO.USUARIO and COTACAOHISTORICO.datafim is null ");
		sql.add(" WHERE FUNCIONARIOCARGO.ATIVO = TRUE");
		if (Objects.nonNull(departamento) && Objects.nonNull(departamento.getCodigo())) {
			sql.add(String.format("AND DEPARTAMENTO.CODIGO = %s", departamento.getCodigo()));
		}
		if (Objects.nonNull(cargo) && Objects.nonNull(cargo.getCodigo())) {
			sql.add(String.format("AND CARGO.CODIGO = %s", cargo.getCodigo()));
		}
		sql.add(" group by USUARIO.codigo, pessoa.nome  ");
		sql.add(" order by count(COTACAOHISTORICO.USUARIO), pessoa.nome   ");
		sql.add(" limit 1   ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return consultarPorChavePrimaria(tabelaResultado.getInt("codigo"), nivelMontarDados, usuario);
		}
		return null;
	}

	@Override
	public List<UsuarioVO> consultaUsuarioPorDepartamento(DepartamentoVO departamento, CargoVO cargo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringJoiner sql = new StringJoiner(" ");
		sql.add("SELECT distinct USUARIO.*");
		sql.add("FROM FUNCIONARIOCARGO ");
		sql.add("INNER JOIN FUNCIONARIO ON FUNCIONARIOCARGO.FUNCIONARIO = FUNCIONARIO.CODIGO ");
		sql.add("INNER JOIN CARGO ON FUNCIONARIOCARGO.CARGO = CARGO.CODIGO");
		sql.add("INNER JOIN DEPARTAMENTO ON DEPARTAMENTO.CODIGO = case when FUNCIONARIOCARGO.DEPARTAMENTO is not null "); 
		sql.add("   then funcionariocargo.departamento"); 
		sql.add("	else cargo.departamento end ");
		sql.add("INNER JOIN PESSOA ON FUNCIONARIO.PESSOA = PESSOA.CODIGO");
		sql.add("INNER JOIN USUARIO ON USUARIO.PESSOA = PESSOA.CODIGO");
		sql.add("FULL OUTER JOIN COTACAOHISTORICO ON USUARIO.CODIGO = COTACAOHISTORICO.USUARIO");
		sql.add("WHERE FUNCIONARIOCARGO.ATIVO = TRUE");

		if (Objects.nonNull(departamento) && Objects.nonNull(departamento.getCodigo())) {
			sql.add(String.format("AND DEPARTAMENTO.CODIGO = %s", departamento.getCodigo()));
		}
		if (Objects.nonNull(cargo) && Objects.nonNull(cargo.getCodigo())) {
			sql.add(String.format("AND CARGO.CODIGO = %s", cargo.getCodigo()));
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		List<UsuarioVO> montarDados = Usuario.montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);

		return montarDados;
	}
	
	@Override
	public List<UsuarioVO> consultaUsuarioCoordenadorPorDepartamento(DepartamentoVO departamento, CargoVO cargo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringJoiner sql = new StringJoiner(" ");
		sql.add("SELECT distinct USUARIO.* ");
		sql.add("FROM FUNCIONARIOCARGO ");
		sql.add("INNER JOIN CARGO ON FUNCIONARIOCARGO.CARGO = CARGO.CODIGO");
		sql.add("INNER JOIN DEPARTAMENTO ON DEPARTAMENTO.CODIGO = case when FUNCIONARIOCARGO.DEPARTAMENTO is not null "); 
		sql.add("   then funcionariocargo.departamento"); 
		sql.add("	else cargo.departamento end ");
		sql.add("INNER JOIN cursocoordenador ON FUNCIONARIOCARGO.FUNCIONARIO = cursocoordenador.funcionario");
		sql.add("INNER JOIN FUNCIONARIO ON cursocoordenador.funcionario = FUNCIONARIO.CODIGO ");
		sql.add("INNER JOIN PESSOA ON FUNCIONARIO.PESSOA = PESSOA.CODIGO");
		sql.add("INNER JOIN USUARIO ON USUARIO.PESSOA = PESSOA.CODIGO");
		sql.add("WHERE FUNCIONARIOCARGO.ATIVO = TRUE");
		
		if (Objects.nonNull(departamento) && Objects.nonNull(departamento.getCodigo())) {
			sql.add(String.format("AND DEPARTAMENTO.CODIGO = %s", departamento.getCodigo()));
		}
		if (Objects.nonNull(cargo) && Objects.nonNull(cargo.getCodigo())) {
			sql.add(String.format("AND CARGO.CODIGO = %s", cargo.getCodigo()));
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		
		List<UsuarioVO> montarDados = Usuario.montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		
		return montarDados;
	}
	
	@Override
	public List<UsuarioVO> consultaUsuarioGerentePorDepartamento(DepartamentoVO departamento, CargoVO cargo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringJoiner sql = new StringJoiner(" ");
		sql.add(" SELECT distinct USUARIO.* ");
		sql.add(" FROM FUNCIONARIOCARGO ");
		sql.add(" INNER JOIN CARGO ON FUNCIONARIOCARGO.CARGO = CARGO.CODIGO ");
		sql.add("INNER JOIN DEPARTAMENTO ON DEPARTAMENTO.CODIGO = case when FUNCIONARIOCARGO.DEPARTAMENTO is not null "); 
		sql.add("   then funcionariocargo.departamento"); 
		sql.add("	else cargo.departamento end ");
		sql.add(" INNER JOIN FUNCIONARIO ON FUNCIONARIOCARGO.FUNCIONARIO = FUNCIONARIO.CODIGO ");
		sql.add(" INNER JOIN PESSOA ON FUNCIONARIO.PESSOA = PESSOA.CODIGO ");
		sql.add(" INNER JOIN USUARIO ON USUARIO.PESSOA = PESSOA.CODIGO ");
		sql.add(" WHERE FUNCIONARIOCARGO.ATIVO = TRUE ");
		sql.add(" AND FUNCIONARIOCARGO.GERENTE = TRUE ");
		if (Objects.nonNull(departamento) && Objects.nonNull(departamento.getCodigo())) {
			sql.add(String.format("AND DEPARTAMENTO.CODIGO = %s", departamento.getCodigo()));
		}
		if (Objects.nonNull(cargo) && Objects.nonNull(cargo.getCodigo())) {
			sql.add(String.format("AND CARGO.CODIGO = %s", cargo.getCodigo()));
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<UsuarioVO> montarDados = Usuario.montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		return montarDados;
	}

	/**
	 * É realizar uma verificação se o usuario é do tipo responsável lega caso seja
	 * então deverár ser sempre atualizado o campo possuiAcessoVisaoPais na tabela
	 * pessoa para true, pois esse usuario agora deverá poder acessar a visão dos
	 * pais.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 * @Autor Pedro
	 */
	public void realizarValidacaoSeDeverarSerAtualizadaTabelaPessoa(UsuarioVO usuarioVO, Boolean verificarAcesso, UsuarioVO usuarioLogado) throws Exception {
		if (usuarioVO.getTipoUsuario().equals("RL")) {
			getFacadeFactory().getPessoaFacade().executarAtualizacaoDadosPessoaPossuiAcessoVisaoPais(usuarioVO.getPessoa(), verificarAcesso, usuarioLogado);
		}
	}

	/**
	 * Operação responsável por validar a pesistencia de um novo usuário do tipo
	 * parceiro
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public void realizarValidacaoPersitirUsuario(ParceiroVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioLogado) throws Exception {
		List<UsuarioVO> listaUsuario = consultarPorCodigoParceiro(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);

		if (obj.getParticipaBancoCurriculum() && listaUsuario.isEmpty()) {
			// caso esteja gravando um novo parceiro e o boolean participaBancoCurriculum
			// seja igual a true
			// será criado um novo usuário com o cnpj do parceiro
			executarInclusaoNovoUsuario(obj, configuracaoGeralSistema, usuarioLogado);
		} else if (!obj.getParticipaBancoCurriculum() && !listaUsuario.isEmpty()) {
			// caso esteja alterando um parceiro na visão administrativa que já tenha um
			// usuario cadastrado
			// e o boolean participaBancoCurriculum seja igual a false
			// será excluido o usuario do parceiro
			excluir(listaUsuario.get(0), usuarioLogado);
		} else if (!listaUsuario.isEmpty()) {
			executarAlteracaoUsuario(obj, listaUsuario.get(0), usuarioLogado);
		}
	}

	/**
	 * Operação responsável por incluir um novo usuário do tipo parceiro
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public void executarInclusaoNovoUsuario(ParceiroVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioLogado) throws Exception {
		UsuarioVO usuario = new UsuarioVO();
		usuario.setNome(obj.getNome());
		usuario.setUsername(Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(obj.getCNPJ()));
		usuario.setSenha(Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(obj.getCNPJ()));
		usuario.setPerfilAdministrador(false);
		usuario.setTipoUsuario("PA");
		adicionarUsuarioPerfilAcesso(usuario, usuarioLogado);
		usuario.setParceiro(obj);

		incluir(usuario, false, usuarioLogado);

	}

	/**
	 * Operação responsável por incluir um novo usuário do tipo parceiro
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public void executarAlteracaoUsuario(ParceiroVO obj, UsuarioVO usuario, UsuarioVO usuarioLogado) throws Exception {
		usuario.setNome(obj.getNome());
		usuario.setParceiro(obj);
		alterar(usuario, false, usuarioLogado);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe <code>UsuarioVO</code>
	 *         resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList();
		while (tabelaResultado.next()) {

			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe <code>UsuarioVO</code>.
	 *
	 * @return O objeto da classe <code>UsuarioVO</code> com os dados devidamente
	 *         montados.
	 */
	public static UsuarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		UsuarioVO obj = new UsuarioVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setUsername(dadosSQL.getString("username"));
		obj.setTipoUsuario(dadosSQL.getString("tipoUsuario"));
		obj.setDataUltimoAcesso(dadosSQL.getTimestamp("dataUltimoAcesso"));
		obj.setDataPrimeiroAcesso(dadosSQL.getTimestamp("dataPrimeiroAcesso"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.getParceiro().setCodigo(dadosSQL.getInt("parceiro"));
		obj.setPossuiCadastroLdap(dadosSQL.getBoolean("possuicadastroldap"));
		obj.setAtivoLdap(dadosSQL.getBoolean("ativoLdap"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosPessoa(obj, nivelMontarDados, usuario);
			montarDadosParceiro(obj, nivelMontarDados, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			montarDadosPessoa(obj, nivelMontarDados, usuario);
			return obj;
		}
		obj.setResetouSenhaPrimeiroAcesso(dadosSQL.getBoolean("resetouSenhaPrimeiroAcesso"));
		obj.setSenha(dadosSQL.getString("senha"));
		obj.setSenhaSHA(dadosSQL.getString("senhaSHA"));
		obj.setSenhaMSCHAPV2(dadosSQL.getString("senhaMSCHAPV2"));

		obj.setPerfilAdministrador(dadosSQL.getBoolean("perfilAdministrador"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSLOGIN) {
			obj.setUsuarioPerfilAcessoVOs(getFacadeFactory().getUsuarioPerfilAcessoFacade().consultarUsuarioPerfilAcesso(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
			montarDadosPessoa(obj, nivelMontarDados, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosPessoa(obj, nivelMontarDados, usuario);
			return obj;
		}
		// montarDadosPessoa(obj, nivelMontarDados, usuario);
		montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS) {
			return obj;
		}
		obj.setUsuarioPerfilAcessoVOs(getFacadeFactory().getUsuarioPerfilAcessoFacade().consultarUsuarioPerfilAcesso(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
		return obj;
	}

	public static void montarDadosPessoa(UsuarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>UsuarioVO</code>
	 * através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public UsuarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		if(Uteis.NIVELMONTARDADOS_DADOSMINIMOS == nivelMontarDados || Uteis.NIVELMONTARDADOS_COMBOBOX == nivelMontarDados ) {
			String sql = "SELECT Usuario.codigo, Usuario.nome, Usuario.username, Usuario.tipoUsuario, Usuario.dataUltimoAcesso, Usuario.dataPrimeiroAcesso, Usuario.pessoa, Usuario.parceiro, Usuario.possuicadastroldap, pessoa.nome as pessoa_nome , pessoa.email as pessoa_email, pessoa.celular as pessoa_celular, pessoa.cpf as pessoa_cpf  FROM Usuario left join pessoa on pessoa.codigo = usuario.pessoa WHERE usuario.codigo = ?";
			SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
			if (!dadosSQL.next()) {
				throw new ConsistirException("Dados Não Encontrados. (Usuário)");
			}
			UsuarioVO obj =  new UsuarioVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setNome(dadosSQL.getString("nome"));
			obj.setUsername(dadosSQL.getString("username"));
			obj.setTipoUsuario(dadosSQL.getString("tipoUsuario"));
			obj.setDataUltimoAcesso(dadosSQL.getTimestamp("dataUltimoAcesso"));
			obj.setDataPrimeiroAcesso(dadosSQL.getTimestamp("dataPrimeiroAcesso"));
			obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
			obj.getParceiro().setCodigo(dadosSQL.getInt("parceiro"));
			obj.setPossuiCadastroLdap(dadosSQL.getBoolean("possuicadastroldap"));
			obj.getPessoa().setNome(dadosSQL.getString("pessoa_nome"));
			obj.getPessoa().setEmail(dadosSQL.getString("pessoa_email"));
			obj.getPessoa().setCelular(dadosSQL.getString("pessoa_celular"));
			obj.getPessoa().setCPF(dadosSQL.getString("pessoa_cpf"));
			return obj;
		}else {
			String sql = "SELECT * FROM Usuario WHERE codigo = ?";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados. (Usuário)");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return Usuario.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Usuario.idEntidade = idEntidade;
	}

	public void executarAtualizarUsernameSenha(String tipoUsuario, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		// System.out.println(">> INICIOU PROCESSAMENTO...");
		String sqlStr = "SELECT * FROM Usuario where (tipoUsuario = '" + tipoUsuario + "') ORDER BY username";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados. (Usuário)");
		}
		List<UsuarioVO> usuariosProc = montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS, usuario);
		for (UsuarioVO obj : usuariosProc) {
			try {
				//// System.out.println("ALTERANDO SENHA USUARIO: " +
				//// obj.getPessoa().getNome());
				String primeiroNome = obj.getPessoa().getNome().substring(0, obj.getPessoa().getNome().indexOf(" "));
				String cpfInicio = obj.getPessoa().getCPF().substring(0, 3);
				String username = primeiroNome.toUpperCase() + cpfInicio;
				obj.setUsername(username);
				obj.setSenha(username);
				obj.setNome(obj.getPessoa().getNome());
				alterar(obj, usuario);
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public String definirVisaoLogarDiretamente(UsuarioVO usuarioVO) throws Exception {
		// Aqui definimos qual a visão que o usuário irá logar diretamente com base nos
		// seus dados.
		if (usuarioVO.getPerfilAdministrador()) {
			return "loginAdministrador";
		} else if (usuarioVO.getTipoUsuario().equals(TipoUsuario.ALUNO.getValor())) {
			return "aluno";
		} else if (usuarioVO.getTipoUsuario().equals(TipoUsuario.PROFESSOR.getValor())) {
			return "professor";
		} else if (usuarioVO.getTipoUsuario().equals(TipoUsuario.FUNCIONARIO.getValor())) {
			return "funcionario";
		} else if (usuarioVO.getTipoUsuario().equals(TipoUsuario.CANDIDATO.getValor())) {
			return "candidato";
		} else {
			return "erroLogin";
		}
	}

	public String definirVisaoLogarComEscolha(UsuarioVO usuarioVO) throws Exception {
		if (usuarioVO.getPerfilAdministrador()) {
			return "loginAdministrador";
		} else if (usuarioVO.getTipoUsuario().equals(TipoUsuario.ALUNO.getValor())) {
			return "aluno";
		} else if (usuarioVO.getTipoUsuario().equals(TipoUsuario.PROFESSOR.getValor())) {
			return "professor";
		} else if (usuarioVO.getTipoUsuario().equals(TipoUsuario.FUNCIONARIO.getValor())) {
			return "funcionario";
		} else if (usuarioVO.getTipoUsuario().equals(TipoUsuario.CANDIDATO.getValor())) {
			return "candidato";
		} else {
			return "erroLogin";
		}
	}

	public void definirOpcoesVisao(UsuarioVO usuarioVO) throws Exception {
		// Aqui definimos quais as visões esse usuário tem acesso, para podermos
		// montar o modal que aparecerá na tela.
		usuarioVO.setEscolhaAdministrador(false);
		usuarioVO.setEscolhaAluno(false);
		usuarioVO.setEscolhaFuncionario(false);
		usuarioVO.setEscolhaProfessor(false);
		if (usuarioVO.getPerfilAdministrador() && usuarioVO.getPessoa().getAtivo()) {
			usuarioVO.setEscolhaAdministrador(true);
		}
		if (usuarioVO.getPessoa().getAluno()) {
			usuarioVO.setEscolhaAluno(true);
		}
		if (usuarioVO.getPessoa().getProfessor() && usuarioVO.getPessoa().getAtivo()) {
			usuarioVO.setEscolhaProfessor(true);
		}
		if (usuarioVO.getPessoa().getFuncionario() && usuarioVO.getPessoa().getAtivo()) {
			usuarioVO.setEscolhaFuncionario(true);
		}
	}

	public Integer consultarQuantidadeDeUnidadesUsuarioEstaVinculado(UsuarioVO usuarioVO) {
		int contador = 0;
		Hashtable<Integer, UsuarioPerfilAcessoVO> hashtableUsuarioPerfilAcessoVO = new Hashtable<Integer, UsuarioPerfilAcessoVO>(0);
		for (UsuarioPerfilAcessoVO usuarioPerfilAcessoVO : usuarioVO.getUsuarioPerfilAcessoVOs()) {
			if (!hashtableUsuarioPerfilAcessoVO.containsKey(usuarioPerfilAcessoVO.getUnidadeEnsinoVO().getCodigo()) && usuarioPerfilAcessoVO.getUnidadeEnsinoVO().getCodigo() != 0) {
				contador = contador + 1;
			}
			hashtableUsuarioPerfilAcessoVO.put(usuarioPerfilAcessoVO.getUnidadeEnsinoVO().getCodigo(), usuarioPerfilAcessoVO);
		}
		return contador;
	}

	public Boolean validarUnicidadeUsernameParceiro(String username, Integer codigoParceiro) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT username FROM usuario where upper(username) like upper('");
			sql.append(username).append("') ");
			if (codigoParceiro != null && !codigoParceiro.equals(0)) {
				sql.append("AND parceiro != ").append(codigoParceiro);
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw e;
		}
	}

	public Boolean validarUnicidadeUsername(String username, Integer codigoPessoa) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT username FROM usuario where upper(username) like upper('");
			sql.append(username).append("') ");
			if (codigoPessoa != null && !codigoPessoa.equals(0)) {
				sql.append("AND pessoa != ").append(codigoPessoa);
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw e;
		}
	}

	public static void montarDadosParceiro(UsuarioVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getParceiro().getCodigo().intValue() == 0) {
			return;
		}
		obj.setParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(obj.getParceiro().getCodigo(), false, nivelMontarDados, usuario));
	}

	public void adicionarUsuarioPerfilAcesso(UsuarioVO obj, UsuarioVO usuarioLogado) throws Exception {
		UsuarioPerfilAcessoVO usuarioPerfilAcesso = new UsuarioPerfilAcessoVO();
		PerfilAcessoVO perfilAcesso = consultarPerfilAcessoPadrao(null, usuarioLogado);
		usuarioPerfilAcesso.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(perfilAcesso.getCodigo(), usuarioLogado));
		obj.getUsuarioPerfilAcessoVOs().add(usuarioPerfilAcesso);
	}

	public PerfilAcessoVO consultarPerfilAcessoPadrao(Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, unidadeEnsino);
		if (Uteis.isAtributoPreenchido(conf) && Uteis.isAtributoPreenchido(conf.getPerfilPadraoParceiro())) {
			return conf.getPerfilPadraoParceiro();
		}
		throw new Exception("Não Existe nenhum perfil de acesso padrão cadastrado para efetuar do cadastro do usuário.");
	}

	public List<UsuarioVO> consultaRapidaPorNomeAlunoAlteracaoSenha(String nomePessoa, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT usuario.codigo, usuario.username, pessoa.codigo as \"pessoa.codigo\", pessoa.nome, tipousuario, pessoa.email FROM usuario  ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" WHERE upper(sem_acentos(pessoa.nome)) like (upper(sem_acentos('%" + nomePessoa.toLowerCase() + "%'))) ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (matricula.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" AND pessoa.aluno = true AND usuario.tipoUsuario = 'AL' ");
		sqlStr.append(" ORDER BY Pessoa.nome ");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<UsuarioVO> vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			UsuarioVO obj = new UsuarioVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setEmail(tabelaResultado.getString("email"));
			obj.setUsername(tabelaResultado.getString("username"));
			obj.setTipoUsuario(tabelaResultado.getString("tipoUsuario"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<UsuarioVO> consultaRapidaPorMatriculaAlunoAlteracaoSenha(String matricula, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT usuario.codigo, usuario.username, pessoa.codigo as \"pessoa.codigo\", pessoa.nome, pessoa.email, tipousuario FROM usuario  ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" WHERE (Matricula.matricula ilike('").append(matricula).append("%')) ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (matricula.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" AND pessoa.aluno = true AND usuario.tipoUsuario = 'AL' ");
		sqlStr.append(" ORDER BY Pessoa.nome ");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<UsuarioVO> vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			UsuarioVO obj = new UsuarioVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setEmail(tabelaResultado.getString("email"));
			obj.setUsername(tabelaResultado.getString("username"));
			obj.setTipoUsuario(tabelaResultado.getString("tipoUsuario"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List<UsuarioVO> consultaRapidaPorResponsavelLegal(Integer codigoResponsavelLegal, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT usuario.codigo, usuario.username, pessoa.codigo as \"pessoa.codigo\", pessoa.nome, pessoa.dataUltimaAtualizacaoCadatral as \"dataUltimaAtualizacaoCadatral\", tipousuario FROM usuario  ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
		sqlStr.append(" INNER join filiacao on filiacao.aluno = pessoa.codigo ");
		sqlStr.append(" INNER JOIN Matricula ON (Matricula.aluno = pessoa.codigo) ");
		sqlStr.append(" WHERE pais =  ").append(codigoResponsavelLegal);
		sqlStr.append(" AND (matricula.situacao != 'CA') AND (matricula.matriculaSuspensa = false or matricula.matriculaSuspensa is null) ");
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<UsuarioVO> vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			UsuarioVO obj = new UsuarioVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getPessoa().setDataUltimaAtualizacaoCadatral(tabelaResultado.getDate("dataUltimaAtualizacaoCadatral"));
			obj.setUsername(tabelaResultado.getString("username"));
			obj.setTipoUsuario(tabelaResultado.getString("tipoUsuario"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public Integer consultarTotalDeGegistroPorNomeAlunoAlteracaoSenha(String valorConsulta, Integer codUnidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlsb = new StringBuilder();
		sqlsb.append("SELECT COUNT(codigo) FROM (");
		sqlsb.append("SELECT DISTINCT (Usuario.codigo), usuario.nome FROM Usuario ");
		sqlsb.append(" INNER JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
		sqlsb.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlsb.append(" WHERE upper(sem_acentos(pessoa.nome)) like (upper(sem_acentos('%" + valorConsulta.toLowerCase() + "%'))) ");
		if (codUnidadeEnsino != 0) {
			sqlsb.append("and matricula.unidadeensino = ").append(codUnidadeEnsino).append(" ");
		}
		sqlsb.append(" AND pessoa.aluno = true AND usuario.tipoUsuario = 'AL' ");
		sqlsb.append(") AS t");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	public Integer consultarTotalDeGegistroPorMatriculaAlunoAlteracaoSenha(String valorConsulta, Integer codUnidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlsb = new StringBuilder();
		sqlsb.append("SELECT COUNT(codigo) FROM (");
		sqlsb.append("SELECT DISTINCT (Usuario.codigo), usuario.nome FROM Usuario ");
		sqlsb.append(" INNER JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
		sqlsb.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlsb.append("WHERE matricula.matricula ilike'").append(valorConsulta.toLowerCase()).append("%' ");
		if (codUnidadeEnsino != 0) {
			sqlsb.append("and matricula.unidadeensino = ").append(codUnidadeEnsino).append(" ");
		}
		sqlsb.append(" AND pessoa.aluno = true AND usuario.tipoUsuario = 'AL' ");
		sqlsb.append(") AS t");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	@Override
	public void executarVerificacaoParaInclusaoNovoUsuarioPorFiliacao(PessoaVO pessoa, UsuarioVO usuarioLogado) throws Exception {
		if (pessoa.getPossuiAcessoVisaoPais()) {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("SELECT usuario.codigo FROM usuario where pessoa =  ").append(pessoa.getCodigo());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				return;
			}
			UsuarioVO usuario = new UsuarioVO();
			usuario.setPessoa(pessoa);
			usuario.setNome(pessoa.getNome());
			usuario.setUsername(Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(pessoa.getCPF()));
			usuario.setSenha(Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(pessoa.getCPF()));
			usuario.setPerfilAdministrador(false);
			usuario.setTipoUsuario("RL");
			incluir(usuario, false, usuarioLogado);
		}
	}

	@Override
	public Boolean consultarPorCodigoPessoaSeUsuarioExiste(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT codigo FROM Usuario WHERE pessoa = " + valorConsulta.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean consultarPorCpfPessoaSeUsuarioExiste(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append("select usuario.codigo from usuario ");
		sb.append(" inner join pessoa on pessoa.codigo = usuario.pessoa ");
		sb.append(" where cpf = '").append(valorConsulta).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	/**
	 * Método responsável por realizar a consulta de funcionario por nome vinculado
	 * ao usuariona classe <code>RequerimentoControle</code>
	 */
	@Override
	public List<UsuarioVO> consultaRapidaPorNomeUsuarioAutoComplete(String valorConsulta, int limit, Integer unidadeEnsino, Integer unidadeEnsinoLogado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		} else if (Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsinoLogado));
		}
		return consultaRapidaPorNomeUsuarioAutoComplete(valorConsulta, limit, unidadeEnsinoVOs, null, controlarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<UsuarioVO> consultaRapidaPorNomeUsuarioAutoComplete(String valorConsulta, int limit, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer departamento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getPadraoSqlConsultaFuncionario();
		sqlStr.append(" WHERE pessoa.nome ilike('%").append(valorConsulta.toLowerCase()).append("%') ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND (unidadeensino.codigo IS NULL OR unidadeensino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(")) ");
		}
		if (Uteis.isAtributoPreenchido(departamento)) {
			sqlStr.append(" AND departamento.codigo = ").append(departamento);
		}
		sqlStr.append(" ORDER BY pessoa.nome ");
		sqlStr.append(" limit ").append(limit);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosFuncionarioResposanvel(tabelaResultado);
	}

	/**
	 * Método responsável por realizar a consulta de funcionario por codigo
	 * vinculado ao usuario na classe <code>RequerimentoControle</code>
	 */
	@Override
	public List<UsuarioVO> consultarUsuarioPorCodigoDepartamentoESemDepartamento(Integer codigo, Integer unidadeEnsino, Integer unidadeEnsinoLogado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getPadraoSqlConsultaFuncionario();
		sqlStr.append(" WHERE departamento.codigo = ").append(codigo).append(" ");
		if (unidadeEnsino != null && unidadeEnsino != 0 || unidadeEnsinoLogado != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append(" ORDER BY departamento.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosFuncionarioResposanvel(tabelaResultado);
	}
	
	/**
	 * Método responsável por realizar a consulta de funcionario por nome
	 * funcionario vinculado ao usuario na classe <code>RequerimentoControle</code>
	 */
	@Override
	public List<UsuarioVO> consultaRapidaPorNomeFuncionario(String valorConsulta, Integer departamento, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPorNomeFuncionario(valorConsulta, departamento, tipoPessoa, unidadeEnsinoVOs, exerceCargoAdministrativo, ativo, limite, offset, controlarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<UsuarioVO> consultaRapidaPorNomeFuncionario(String valorConsulta, Integer departamento, String tipoPessoa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getPadraoSqlConsultaFuncionario();
		sqlStr.append(" WHERE pessoa.nome ilike(?) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND unidadeensino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		if (Uteis.isAtributoPreenchido(tipoPessoa)) {
			if (tipoPessoa.equals("FU")) {
				sqlStr.append(" and pessoa.funcionario");
			}
			if (tipoPessoa.equals("PR")) {
				sqlStr.append(" and pessoa.professor");
			}
			if (!tipoPessoa.equals("PR")) {
				sqlStr.append(" and pessoa.professor = false");
			}
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		if (Uteis.isAtributoPreenchido(departamento)) {
			sqlStr.append(" AND departamento.codigo = ").append(departamento);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%" + valorConsulta.toLowerCase() + "%");
		return montarDadosFuncionarioResposanvel(tabelaResultado);
	}

	/**
	 * Método responsável por realizar a consulta de funcionario por matricula
	 * vinculado ao usuario na classe <code>RequerimentoControle</code>
	 */
	@Override
	public List<UsuarioVO> consultaRapidaPorMatriculaFuncionario(String valorConsulta, Integer departamento, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPorMatriculaFuncionario(valorConsulta, departamento, unidadeEnsinoVOs, exerceCargoAdministrativo, ativo, nivelMontarDados, limite, offset, usuario);
	}
	
	@Override
	public List<UsuarioVO> consultaRapidaPorMatriculaFuncionario(String valorConsulta, Integer departamento, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Boolean exerceCargoAdministrativo, Boolean ativo, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getPadraoSqlConsultaFuncionario();
		sqlStr.append("WHERE lower(funcionario.matricula) like(?)");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND unidadeensino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		if (Uteis.isAtributoPreenchido(departamento)) {
			sqlStr.append(" AND departamento.codigo = ").append(departamento);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase()+"%");
		return montarDadosFuncionarioResposanvel(tabelaResultado);
	}

	/**
	 * Método responsável por realizar a consulta de funcionario por cpf vinculado
	 * ao usuario na classe <code>RequerimentoControle</code>
	 */
	@Override
	public List<UsuarioVO> consultaRapidaPorCPFFuncionario(String valorConsulta, Integer departamento, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPorCPFFuncionario(valorConsulta, departamento, tipoPessoa, unidadeEnsinoVOs, exerceCargoAdministrativo, ativo, limite, offset, controlarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<UsuarioVO> consultaRapidaPorCPFFuncionario(String valorConsulta, Integer departamento, String tipoPessoa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getPadraoSqlConsultaFuncionario();
		sqlStr.append("WHERE lower(replace(replace(pessoa.cpf,'.',''),'-','')) like(?)");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND UnidadeEnsino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		if (Uteis.isAtributoPreenchido(tipoPessoa)) {
			if (tipoPessoa.equals("FU")) {
				sqlStr.append(" and pessoa.funcionario ");
			}
			if (tipoPessoa.equals("PR")) {
				sqlStr.append(" and  pessoa.professor ");
			}
			if (!tipoPessoa.equals("PR")) {
				sqlStr.append(" and  pessoa.professor = false");
			}
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		if (Uteis.isAtributoPreenchido(departamento)) {
			sqlStr.append(" AND departamento.codigo = ").append(departamento);
		}
		sqlStr.append(" ORDER BY pessoa.cpf");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), Uteis.retirarMascaraCPF(valorConsulta.toLowerCase()) + "%");
		return montarDadosFuncionarioResposanvel(tabelaResultado);
	}

	/**
	 * Método responsável por realizar a consulta de funcionario por cargo vinculado
	 * ao usuario na classe <code>RequerimentoControle</code>
	 */
	@Override
	public List<UsuarioVO> consultaRapidaPorCargoFuncionario(String valorConsulta, Integer departamento, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPorCargoFuncionario(valorConsulta, departamento, unidadeEnsinoVOs, exerceCargoAdministrativo, ativo, limite, offset, controlarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<UsuarioVO> consultaRapidaPorCargoFuncionario(String valorConsulta, Integer departamento, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getPadraoSqlConsultaFuncionario();
		sqlStr.append("WHERE sem_acentos(lower(cargo.nome)) like(sem_acentos(?))");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND unidadeensino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		if (Uteis.isAtributoPreenchido(departamento)) {
			sqlStr.append(" AND departamento.codigo = ").append(departamento);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toLowerCase() + "%");
		return montarDadosFuncionarioResposanvel(tabelaResultado);
	}

	/**
	 * Método responsável por realizar a consulta de funcionario por unidade e
	 * ensino vinculado ao usuario na classe <code>RequerimentoControle</code>
	 */
	@Override
	public List<UsuarioVO> consultaRapidaFuncionarioPorUnidadeEnsino(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, Boolean exerceCargoAdministrativo, Boolean ativo, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getPadraoSqlConsultaFuncionario();
		sqlStr.append("WHERE sem_acentos(lower(unidadeEnsino.nome)) like(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND funcionarioCargo.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		if (tipoPessoa.equals("FU")) {
			sqlStr.append(" and pessoa.funcionario = 'true'");
		}
		if (tipoPessoa.equals("PR")) {
			sqlStr.append(" and pessoa.professor = 'true'");
		}
		if (tipoPessoa.equals("!PR")) {
			sqlStr.append(" and pessoa.professor = 'false'");
		}
		if (exerceCargoAdministrativo != null) {
			sqlStr.append(" and funcionario.exerceCargoAdministrativo = ").append(exerceCargoAdministrativo);
		}
		if (ativo != null) {
			sqlStr.append(" and pessoa.ativo = ").append(ativo);
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosFuncionarioResposanvel(tabelaResultado);
	}

	/**
	 * Metodo utilizado para montar os dados referente as consultas de funcionario
	 * 
	 * @param tabelaResultado
	 * @return
	 */
	public List<UsuarioVO> montarDadosFuncionarioResposanvel(SqlRowSet tabelaResultado) {
		List<UsuarioVO> listaUsuario = new ArrayList<UsuarioVO>();
		while (tabelaResultado.next()) {
			UsuarioVO obj = new UsuarioVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("usuario.codigo")));
			obj.getPessoa().setCodigo(new Integer(tabelaResultado.getInt("pessoa.codigo")));
			obj.getPessoa().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getPessoa().setCPF(tabelaResultado.getString("pessoa.cpf"));
			listaUsuario.add(obj);
		}
		return listaUsuario;
	}

	/**
	 * Padrao Sql para utilizacao nas consultas aos funcionarios
	 * 
	 * @return
	 */
	public StringBuilder getPadraoSqlConsultaFuncionario() {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT usuario.codigo as \"usuario.codigo\", pessoa.codigo as \"pessoa.codigo\", pessoa.cpf as \"pessoa.cpf\", pessoa.nome as \"pessoa.nome\" ");
		sqlStr.append(" FROM Usuario ");
		sqlStr.append(" LEFT JOIN Pessoa on pessoa.codigo = usuario.pessoa ");
		sqlStr.append(" LEFT JOIN Funcionario on funcionario.pessoa = pessoa.codigo ");
		sqlStr.append(" LEFT JOIN FuncionarioCargo on funcionariocargo.funcionario = funcionario.codigo ");
		sqlStr.append(" LEFT JOIN Cargo on cargo.codigo = funcionariocargo.cargo ");

		sqlStr.append("LEFT JOIN DEPARTAMENTO ON DEPARTAMENTO.CODIGO = case when FUNCIONARIOCARGO.DEPARTAMENTO is not null "); 
		sqlStr.append("  then funcionariocargo.departamento"); 
		sqlStr.append("	 else cargo.departamento end ");

		sqlStr.append(" LEFT JOIN UnidadeEnsino on unidadeensino.codigo = funcionariocargo.unidadeensino ");
		return sqlStr;
	}

	/**
	 * Metodo responsavel por executar a consulta do funcionario atravez do codigo
	 * do usuario logado e retorna um objeto do tipo UsuarioVO
	 */
	@Override
	public UsuarioVO consultarPorCodigoUsuario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getPadraoSqlConsultaFuncionario();
		sqlStr.append(" WHERE usuario.codigo = ").append(valorConsulta);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		UsuarioVO obj = new UsuarioVO();
		while (tabelaResultado.next()) {
			obj.setCodigo(new Integer(tabelaResultado.getInt("usuario.codigo")));
			obj.getPessoa().setCodigo(new Integer(tabelaResultado.getInt("pessoa.codigo")));
			obj.getPessoa().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getPessoa().setCPF(tabelaResultado.getString("pessoa.cpf"));
		}
		return obj;
	}

	@Override
	public void alterarBoleanoUsuarioPerfilAdministrador(final Integer pessoa, final boolean perfilAdministrador, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		final String sql = "UPDATE Usuario SET perfilAdministrador = ? WHERE ((pessoa = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setBoolean(1, perfilAdministrador);
				sqlAlterar.setInt(2, pessoa);
				return sqlAlterar;
			}
		});
	}

	/**
	 * @author Victor Hugo 10/12/2014
	 * 
	 *         Método responsável por verificar os alunos que estão sem logar no
	 *         sistema de acordo com as configuração feita na tela de
	 *         ConfiguraçãoEAD. Os dados são fornecidos para uma rotina diária que
	 *         verifica o mesmo toda meia noite.
	 */
	@Override
	public SqlRowSet consultarAlunosSemLogarSistema() throws Exception {

		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select distinct aluno.codigo as codigo, aluno.nome as nome, dataultimoacesso, aluno.email as email,");
		sqlStr.append(" matriculaperiodoturmadisciplina.codigo as matriculaperiodoturmadisciplina, disciplina.nome as nomedisciplina, configuracaoead.notUmDiasSemLogarSistema,");
		sqlStr.append(" configuracaoead.notDoisDiasSemLogarSistema, configuracaoead.notTresDiasSemLogarSistema,");
		sqlStr.append(" case when notificarAlunoDiasFicarSemLogarSistema then");
		sqlStr.append(" 	case when EXTRACT( DAYS FROM (current_timestamp-dataultimoacesso)) =  notUmDiasSemLogarSistema then 'MENSAGEM_NOTIFICACAO1_CONFIGURACAOEAD_ALUNOS_SEM_LOGAR_SISTEMA' else");
		sqlStr.append(" 	case when EXTRACT( DAYS FROM (current_timestamp-dataultimoacesso)) =  notDoisDiasSemLogarSistema then 'MENSAGEM_NOTIFICACAO2_CONFIGURACAOEAD_ALUNOS_SEM_LOGAR_SISTEMA' else");
		sqlStr.append(" 	case when EXTRACT( DAYS FROM (current_timestamp-dataultimoacesso)) =  notTresDiasSemLogarSistema then 'MENSAGEM_NOTIFICACAO3_CONFIGURACAOEAD_ALUNOS_SEM_LOGAR_SISTEMA'");
		sqlStr.append(" end end end end AS templateNotificacao");
		sqlStr.append(" from usuario");
		sqlStr.append(" inner join matricula on matricula.aluno = usuario.pessoa");
		sqlStr.append(" inner join pessoa as aluno on matricula.aluno = aluno.codigo");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join calendarioatividadematricula on calendarioatividadematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" inner join configuracaoead on turma.configuracaoead = configuracaoead.codigo");
		sqlStr.append(" left join PersonalizacaoMensagemAutomatica as mensagem1 on mensagem1.templateMensagemAutomaticaEnum = 'MENSAGEM_NOTIFICACAO1_CONFIGURACAOEAD_ALUNOS_SEM_LOGAR_SISTEMA'");
		sqlStr.append(" left join PersonalizacaoMensagemAutomatica as mensagem2 on mensagem2.templateMensagemAutomaticaEnum = 'MENSAGEM_NOTIFICACAO2_CONFIGURACAOEAD_ALUNOS_SEM_LOGAR_SISTEMA'");
		sqlStr.append(" left join PersonalizacaoMensagemAutomatica as mensagem3 on mensagem3.templateMensagemAutomaticaEnum = 'MENSAGEM_NOTIFICACAO3_CONFIGURACAOEAD_ALUNOS_SEM_LOGAR_SISTEMA'");
		sqlStr.append(" where calendarioatividadematricula.tipocalendarioatividade = 'ACESSO_CONTEUDO_ESTUDO'");
		sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AT'");
		sqlStr.append(" and notificarAlunoDiasFicarSemLogarSistema");
		sqlStr.append(" and (");
		sqlStr.append(" 	EXTRACT( DAYS FROM (current_timestamp-dataultimoacesso)) =  notUmDiasSemLogarSistema and mensagem1.desabilitarEnvioMensagemAutomatica = false or");
		sqlStr.append(" 	EXTRACT( DAYS FROM (current_timestamp-dataultimoacesso)) =  notDoisDiasSemLogarSistema and mensagem2.desabilitarEnvioMensagemAutomatica = false or");
		sqlStr.append(" 	EXTRACT( DAYS FROM (current_timestamp-dataultimoacesso)) =  notTresDiasSemLogarSistema and mensagem3.desabilitarEnvioMensagemAutomatica = false");
		sqlStr.append(" )");

		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova, UsuarioVO usuario) throws Exception {
		String sqlStr = "UPDATE Usuario set pessoa=? WHERE ((pessoa = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { pessoaNova, pessoaAntigo });
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarBooleanoSolicitarNovaSenha(Boolean solicitarNovaSenha, UsuarioVO usuarioAlterarSenha, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.alterar(getIdEntidade(), controlarAcesso, usuarioVO);
		final String sql = "UPDATE Usuario SET solicitarnovasenha = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setBoolean(1, solicitarNovaSenha);
				sqlAlterar.setInt(2, usuarioAlterarSenha.getCodigo());
				return sqlAlterar;
			}
		});
	}

	private void alterarDadosNovaSenhaUsuario(Boolean lembrarDeSincronizarComLdapNoFinalDaTransacao, final UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE Usuario SET solicitarnovasenha = ?, senha = ?, senhaSHA=?, senhaMSCHAPV2=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setBoolean(1, false);
					try {
						sqlAlterar.setString(2, Uteis.encriptar(usuarioVO.getSenha()));
						sqlAlterar.setString(3, Uteis.encriptarSHA(usuarioVO.getSenha()));
						sqlAlterar.setString(4, Uteis.encriptarMSCHAPV2(usuarioVO.getSenha()));
					} catch (UnsupportedEncodingException e) {
					}
					sqlAlterar.setInt(5, usuarioVO.getCodigo());
					return sqlAlterar;
				}
			});
			usuarioVO.setSenhaSHA(Uteis.encriptar(usuarioVO.getSenha()));
			usuarioVO.setSenhaSHA(Uteis.encriptarSHA(usuarioVO.getSenha()));
			usuarioVO.setSenhaMSCHAPV2(Uteis.encriptarMSCHAPV2(usuarioVO.getSenha()));
			getFacadeFactory().getLogAlteracaoSenhaFacade().consultarSenhaJaUtilizada(usuarioVO.getSenha(), getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, null), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarValidacaoSenhasCompativeis(UsuarioVO usuarioVO, String novaSenha, String novaSenhaDois) throws Exception {
		try {
			if (Uteis.isAtributoPreenchido(usuarioVO)) {
				if (novaSenha != null && !novaSenha.equals("") && novaSenhaDois != null && !novaSenhaDois.equals("")) {
					if (novaSenha.equals(novaSenhaDois)) {
						if (usuarioVO.getSenha().equals(Uteis.encriptar(novaSenha))) {
							throw new Exception(UteisJSF.internacionalizar("msg_LoginNovaSenha_SenhaAtualIgualAntiga"));
						} else {
							usuarioVO.setSenha(novaSenha);
							usuarioVO.setSolicitarNovaSenha(Boolean.FALSE);
							alterarDadosNovaSenhaUsuario(true, usuarioVO);
						}
					} else {
						throw new Exception(UteisJSF.internacionalizar("msg_LoginNovaSenha_SenhaIguais"));
					}
				} else {
					throw new Exception(UteisJSF.internacionalizar("msg_senha_invalida"));
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTokenEPlataformaAplicativo(final UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Usuario set tokenAplicativo=?, celular=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, usuario.getTokenAplicativo());
					sqlAlterar.setString(2, usuario.getCelular().name());
					sqlAlterar.setInt(3, usuario.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<UsuarioVO> consultaRapidaUsuariosEnvioPushAplicativo(List<ComunicadoInternoDestinatarioVO> comunicadoInternoDestinatarioVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT usuario.codigo, tokenAplicativo, usuario.celular, pessoa.professor, pessoa.aluno, pessoa.funcionario FROM usuario");
		sqlStr.append(" inner join pessoa on usuario.pessoa = pessoa.codigo");
		sqlStr.append(" WHERE pessoa in (0 ");
		for (ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO : comunicadoInternoDestinatarioVOs) {
			sqlStr.append(", " + comunicadoInternoDestinatarioVO.getDestinatario().getCodigo());
		}
		sqlStr.append(")");
		sqlStr.append(" AND usuario.celular != ''");
		sqlStr.append(" AND tokenAplicativo != ''");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<UsuarioVO> usuarioVOs = new ArrayList<>();
		UsuarioVO usuarioVO = null;
		while (tabelaResultado.next()) {
			usuarioVO = new UsuarioVO();
			usuarioVO.setCodigo(tabelaResultado.getInt("codigo"));
			usuarioVO.setTokenAplicativo(tabelaResultado.getString("tokenAplicativo"));
			usuarioVO.setCelular(PlataformaEnum.valueOf(tabelaResultado.getString("celular")));
			usuarioVO.getPessoa().setProfessor(tabelaResultado.getBoolean("professor"));
			usuarioVO.getPessoa().setFuncionario(tabelaResultado.getBoolean("funcionario"));
			usuarioVO.getPessoa().setAluno(tabelaResultado.getBoolean("aluno"));
			usuarioVOs.add(usuarioVO);
		}
		return usuarioVOs;
	}

	@Override
	public List<UsuarioVO> consultarResponsavelInteracaoWorkflowPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct usuario.codigo, pessoa.nome from interacaoworkflow ");
		sb.append(" INNER JOIN prospects ON prospects.codigo = interacaoworkflow.prospect ");
		sb.append(" inner join usuario on usuario.codigo = interacaoworkflow.responsavel ");
		sb.append(" inner join pessoa on pessoa.codigo = usuario.pessoa ");
		sb.append(" where prospects.pessoa = ").append(aluno);
		sb.append(" order by pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<UsuarioVO> listaResponsavelInteracaoWorlflowVOs = new ArrayList<UsuarioVO>(0);
		while (tabelaResultado.next()) {
			UsuarioVO obj = new UsuarioVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getPessoa().setNome(tabelaResultado.getString("nome"));
			listaResponsavelInteracaoWorlflowVOs.add(obj);
		}
		return listaResponsavelInteracaoWorlflowVOs;
	}

	public void gravarLogAlteracaoSenha(UsuarioVO usuario, String senha, UsuarioVO usuarioResposavelAlteracao) throws Exception {

		LogAlteracaoSenhaVO logAlteracaoSenha = new LogAlteracaoSenhaVO();
		logAlteracaoSenha.setData(new Date());
		logAlteracaoSenha.setUsuario(usuario);
		logAlteracaoSenha.setUsuarioResponsavelAlteracao(usuarioResposavelAlteracao);
		logAlteracaoSenha.setSenha(senha);

		getFacadeFactory().getLogAlteracaoSenhaFacade().incluir(logAlteracaoSenha, usuarioResposavelAlteracao);
	}
	
	@Override
	public Boolean validarTokenAplicativo(String token) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from usuario where tokenAplicativo = '").append(token).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			return true; 
		}
		return false;
	}

	class UsuarioAgregador {

		public UsuarioVO usuarioVO;
		public Date dataFim;
		public Date dataInicio;

		public UsuarioAgregador(UsuarioVO usuarioVO, Date dataFim, Date dataInicio) {
			super();
			this.usuarioVO = usuarioVO;
			this.dataFim = dataFim;
			this.dataInicio = dataInicio;
		}

		public String getNome() {
			return this.usuarioVO.getNome();
		}

		@Override
		public String toString() {
			return "UsuarioAgregador [usuarioVO=" + usuarioVO.getNome() + ", dataFim=" + dataFim + ", dataInicio=" + dataInicio + "]";
		}

	}
	
	private void removerUsuarioMinhaBiblioteca(UsuarioVO usuarioLogado, ConfiguracaoBibliotecaVO configuracaoBibliotecaVO, ConsistirException consistirException, Map<String, Integer> resultado) throws Exception {
		if (!usuarioLogado.getPessoa().getEmail().trim().isEmpty()) {
			Boolean removido = false;
			String msg = "";
			try {
				//String url = "https://digitallibrary.zbra.com.br/DigitalLibraryIntegrationService/RemovePreRegisterUser";
				String url = "https://integracao.dli.minhabiblioteca.com.br/DigitalLibraryIntegrationService/RemovePreRegisterUser";
				StringBuilder body = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><RemovePreRegisterUserRequest xmlns=\"http://dli.zbra.com.br\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><UserName>")
						.append(usuarioLogado.getPessoa().getEmail().trim()).append("</UserName></RemovePreRegisterUserRequest>");
				HttpResponse<String> response = Unirest.post(url)
				.header("X-DigitalLibraryIntegration-API-Key", configuracaoBibliotecaVO.getChaveAutenticacaoMinhaBiblioteca())
				.header("Content-Type", "application/xml")
				.body(body.toString()).asString();
				
				if(response != null && response.getHeaders() != null && response.getHeaders().get("Content-Type") != null &&  response.getBody() != null && response.getHeaders().getFirst("Content-Type").equals(MediaType.APPLICATION_JSON.getType())) {					
					ReturnResponseMinhaBibliotecaVO  retornoResponse  = null ;
					try { retornoResponse = new Gson().fromJson(response.getBody(), ReturnResponseMinhaBibliotecaVO.class);	}catch(Exception ex) {}
					
					
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(Uteis.removerAcentuacao(response.getBody()).getBytes()));
					Element root = doc.getDocumentElement();
					NodeList nodes = root.getChildNodes();
					for (int i = 0; i < nodes.getLength(); i++) {
						Node node = nodes.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							if ("Success".equals(element.getNodeName())) {
								removido = Boolean.parseBoolean(element.getTextContent());
							} else if ("Message".equals(element.getNodeName())) {
								msg = "não possui cadastro Minha Biblioteca.";
							}
						}
					}
				}
					
				}catch(Exception e) {    		
		    		msg = e.getMessage();	
		    		e.printStackTrace();
			    }
			if (!removido) {
				consistirException.adicionarListaMensagemErro(usuarioLogado.getPessoa().getNome() +"->> Usuário "+ msg);
				usuarioLogado.setMensagemExclusaoMinhaBiblioteca("Usuário não excluído. Motivo: " + msg);
				resultado.put(ERRO, resultado.get(ERRO) + 1);
			} else {
				usuarioLogado.setMensagemExclusaoMinhaBiblioteca("Usuário excluído com sucesso.");
				resultado.put(SUCESSO, resultado.get(ERRO) + 1);
			}
		} else {
			consistirException.adicionarListaMensagemErro("Usuário não possui email cadastrado.");
			usuarioLogado.setMensagemExclusaoMinhaBiblioteca("Usuário não excluído. Motivo: Não possui email cadastrado.");
			resultado.put(ERRO, resultado.get(ERRO) + 1);
		}
	}
	
	private String realizarCadastroNaMinhaBiblioteca(ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuarioLogado, PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO) throws Exception {
		Boolean cadastrado = false;
		String msg = "";
		try {
			//String url = "https://digitallibrary.zbra.com.br/DigitalLibraryIntegrationService/CreatePreRegisterUser";
			String url = "https://integracao.dli.minhabiblioteca.com.br/DigitalLibraryIntegrationService/CreatePreRegisterUser";
			StringBuilder body = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><CreatePreRegisterUserRequest xmlns=\"http://dli.zbra.com.br\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><FirstName>")
				.append(Uteis.removerAcentuacaoLimitarTamanho(usuarioLogado.getPessoa().getPrimeiroNome(), 40)).append("</FirstName><LastName>")
				.append(Uteis.removerAcentuacaoLimitarTamanho(usuarioLogado.getPessoa().getUltimoNome(), 40)).append("</LastName><UserName>")
				.append(pessoaEmailInstitucionalVO.getEmail().trim()).append("</UserName>");
			if (Uteis.isAtributoPreenchido(usuarioLogado.getCodigoCursoLogado())) {
				body.append("<CourseId xsi:nil=\"true\">").append(usuarioLogado.getCodigoCursoLogado()).append("</CourseId>");
			} else {
				body.append("<CourseId xsi:nil=\"true\"/>");
			}
			body.append("<UserGroupId xsi:nil=\"true\"/></CreatePreRegisterUserRequest>");
	    	
			HttpResponse<String> response = Unirest.post(url)
					.header("X-DigitalLibraryIntegration-API-Key", configuracaoBiblioteca.getChaveAutenticacaoMinhaBiblioteca())
					.header("Content-Type", "application/xml")
					.body(body.toString()).asString();
    	
			if(response != null && response.getHeaders() != null && response.getHeaders().get("Content-Type") != null &&  response.getBody() != null && response.getHeaders().getFirst("Content-Type").equals(MediaType.APPLICATION_JSON.getType())) {					
				ReturnResponseMinhaBibliotecaVO  retornoResponse  = null ;
				try { retornoResponse = new Gson().fromJson(response.getBody(), ReturnResponseMinhaBibliotecaVO.class);	}catch(Exception ex) {}
				
	    		if(retornoResponse != null && retornoResponse.getSuccess() != null && retornoResponse.getSuccess()) {    			
	    			cadastrado =  retornoResponse.getSuccess();
	    		}else if(retornoResponse != null  && (retornoResponse.getSuccess() == null ||  !retornoResponse.getSuccess() ) &&  Uteis.isAtributoPreenchido(retornoResponse.getMessage()) ){
	    			msg = retornoResponse.getMessage();
	    		}else if(response.getBody() != null ) {
	    			msg =  response.getBody();    			
	    		}		    		
			}else {	
	    	
		
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(Uteis.removerAcentuacao(response.getBody()).getBytes()));
	    	Element root = doc.getDocumentElement();
	    	NodeList nodes = root.getChildNodes();
	    	for (int i = 0; i < nodes.getLength(); i++) {
	    		Node node = nodes.item(i);
	    		if (node.getNodeType() == Node.ELEMENT_NODE) {
	               Element element = (Element) node;
	               if ("Success".equals(element.getNodeName())) {
	            	   cadastrado = Boolean.parseBoolean(element.getTextContent());
	               } else if ("Message".equals(element.getNodeName())) {
	            	   msg = element.getTextContent();
	               }
	            }
	    	}
		  }
		}catch(Exception e) {    		
			msg = e.getMessage();	
			e.printStackTrace();
	    }
    	if (cadastrado) {
    		return "ok";
    	} else {
    		return msg;
    	}
	}
	
	private String realizarGeracaoLinkParaMinhaBiblioteca(ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuarioLogado, PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO) throws Exception {
		boolean autorizado = false;
		String urlAutorizada = "";
		String msg = "";
		try {
			//String url = "https://digitallibrary.zbra.com.br/DigitalLibraryIntegrationService/AuthenticatedUrl";
			String url = "https://integracao.dli.minhabiblioteca.com.br/DigitalLibraryIntegrationService/AuthenticatedUrl";
			StringBuilder body = new StringBuilder("<CreateAuthenticatedUrlRequest xmlns=\"http://dli.zbra.com.br\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><FirstName>")
				.append(Uteis.removerAcentuacaoLimitarTamanho(usuarioLogado.getPessoa().getPrimeiroNome(), 40)).append("</FirstName><LastName>")
				.append(Uteis.removerAcentuacaoLimitarTamanho(usuarioLogado.getPessoa().getUltimoNome(), 40)).append("</LastName><Email>")
				.append(pessoaEmailInstitucionalVO.getEmail().trim()).append("</Email>");
			if (Uteis.isAtributoPreenchido(usuarioLogado.getCodigoCursoLogado())) {
				body.append("<CourseId xsi:nil=\"true\">").append(usuarioLogado.getCodigoCursoLogado()).append("</CourseId>");
			} else {
				body.append("<CourseId xsi:nil=\"true\"/>");
			}
			body.append("<Tag xsi:nil=\"true\"/><Isbn xsi:nil=\"true\"/></CreateAuthenticatedUrlRequest>");
		
			HttpResponse<String> response = Unirest.post(url)
					  .header("X-DigitalLibraryIntegration-API-Key", configuracaoBiblioteca.getChaveAutenticacaoMinhaBiblioteca())
					  .header("Content-Type", "application/xml")
					  .body(body.toString())
					  .asString();
			if(response != null && response.getHeaders() != null && response.getHeaders().get("Content-Type") != null &&  response.getBody() != null && response.getHeaders().getFirst("Content-Type").equals(MediaType.APPLICATION_JSON.getType())) {					
					ReturnResponseMinhaBibliotecaVO  retornoResponse  = null ;
					try { retornoResponse = new Gson().fromJson(response.getBody(), ReturnResponseMinhaBibliotecaVO.class);	}catch(Exception ex) {}
					
		    		if(retornoResponse != null && retornoResponse.getSuccess() != null && retornoResponse.getSuccess() && retornoResponse.getAuthenticatedUrl() != null  && Uteis.isAtributoPreenchido(retornoResponse.getAuthenticatedUrl())) {
		    			urlAutorizada =  retornoResponse.getAuthenticatedUrl();
		    			autorizado =  retornoResponse.getSuccess();
		    		}else if(retornoResponse != null  && (retornoResponse.getSuccess() == null ||  !retornoResponse.getSuccess() ) &&  (retornoResponse.getAuthenticatedUrl() == null  || !Uteis.isAtributoPreenchido(retornoResponse.getAuthenticatedUrl()) ) && Uteis.isAtributoPreenchido(retornoResponse.getMessage()) ){
		    			msg = retornoResponse.getMessage();
		    		}else if(response.getBody().contains("does not have access to digital library")) {
		    			msg =  response.getBody();    			
		    		}		    		
				}else {				
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			    	Document doc = factory.newDocumentBuilder().parse(new ByteArrayInputStream(Uteis.removerAcentuacao(response.getBody()).getBytes()));
			    	Element root = doc.getDocumentElement();
			    	NodeList nodes = root.getChildNodes();
			    	for (int i = 0; i < nodes.getLength(); i++) {
			    		Node node = nodes.item(i);
			    		if (node.getNodeType() == Node.ELEMENT_NODE) {
			               Element element = (Element) node;
			               if ("Success".equals(element.getNodeName())) {
			            	   autorizado = Boolean.parseBoolean(element.getTextContent());
			               } else if ("AuthenticatedUrl".equals(element.getNodeName())) {
			            	   urlAutorizada = element.getTextContent();
			               } else if ("Message".equals(element.getNodeName())) {
			            	   msg = element.getTextContent();
			               }
			            }
			    	}
			    		
				}    	
    	}catch(Exception e) {    		
    		msg = e.getMessage();	
    		e.printStackTrace();
	    }
		if (autorizado) {
    		return urlAutorizada;
    	} else {
    		return msg;
    	}
    	
    	
	}
	
	public void realizarNavegacaoParaMinhaBiblioteca(ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuarioLogado) throws Exception {
		PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  null;
		if(usuarioLogado.getPessoa().getListaPessoaEmailInstitucionalVO().isEmpty()) {
			pessoaEmailInstitucionalVO = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(usuarioLogado.getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO) && pessoaEmailInstitucionalVO.getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.ATIVO)) {
				usuarioLogado.getPessoa().getListaPessoaEmailInstitucionalVO().add(pessoaEmailInstitucionalVO);
			}
		}else {
			pessoaEmailInstitucionalVO = usuarioLogado.getPessoa().getListaPessoaEmailInstitucionalVO().stream().filter(e -> e.getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.ATIVO)).findFirst().get();
		}
		if (!Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
			throw new Exception("Usuário não possui email cadastrado.");
		}
    	String link = realizarGeracaoLinkParaMinhaBiblioteca(configuracaoBiblioteca, usuarioLogado, pessoaEmailInstitucionalVO);
		if (link.startsWith("http")) {
    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    	    externalContext.redirect(link);
    	} else {
    		if (link.contains("does not have access to digital library")) {
        		String cadastro = realizarCadastroNaMinhaBiblioteca(configuracaoBiblioteca, usuarioLogado, pessoaEmailInstitucionalVO);
        		if ("ok".equals(cadastro)) {
        			link = realizarGeracaoLinkParaMinhaBiblioteca(configuracaoBiblioteca, usuarioLogado, pessoaEmailInstitucionalVO);
        			if (link.startsWith("http")) {
        	    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        	    	    externalContext.redirect(link);
        	    	} else {
        	    		throw new Exception(link);
        	    	}
        		} else {
        			throw new Exception(cadastro);
        		}
        	} else {
        		throw new Exception(link);
        	}
    	}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPossuiCadastroLdap(UsuarioVO usuarioVO , UsuarioVO usuarioLogado ) throws Exception {
		final String sql = "UPDATE usuario SET possuiCadastroLdap = ?, ativoLdap = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setBoolean(1, usuarioVO.getPossuiCadastroLdap());
				sqlAlterar.setBoolean(2, usuarioVO.getAtivoLdap());
				sqlAlterar.setInt(3, usuarioVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	public List<UsuarioVO> consultarUsuarioPossuiIntegracaoMinhaBiblioteca(Date dataEvasao,String valorConsulta, String campoConsulta) throws Exception{
		List<UsuarioVO> usuarios = new ArrayList<>();
		List<Object> parametros = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder("select distinct emailinstitucional.email as email , pessoa.nome  as nome from matricula ");
		sqlStr.append(" inner join pessoa on matricula.aluno = pessoa.codigo  ");
		sqlStr.append(" inner join pessoaemailinstitucional  emailinstitucional on emailinstitucional.pessoa = pessoa.codigo  ");
		sqlStr.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");	
		sqlStr.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by mp.ano desc, mp.semestre desc, mp.codigo desc limit 1) ");	
		sqlStr.append(" where matriculaperiodo.datafechamentomatriculaperiodo::date >= '"+UteisData.getDateHoraInicialDia(dataEvasao)+"' and  matriculaperiodo.datafechamentomatriculaperiodo::date <= current_date  ");	
		sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo in ('TR', 'AC', 'JU', 'CA', 'FO', 'TS', 'FI', 'PC') ");	
		sqlStr.append(" and exists (select configuracaobiblioteca.codigo from configuracaobiblioteca where possuiIntegracaoMinhaBiblioteca and padrao) ");	
		sqlStr.append(" and not exists ( select funcionario.pessoa from funcionario ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sqlStr.append(" where funcionario.pessoa = matricula.aluno and  pessoa.ativo ");				
		sqlStr.append(" union ");
		sqlStr.append(" select m.aluno from matricula as m where m.aluno = matricula.aluno and  m.situacao = 'AT' ) ");
		sqlStr.append(" and emailinstitucional.email is not null and emailinstitucional.email != '' ");
		filtroBuscaPorNomeEmail(valorConsulta,campoConsulta, parametros, sqlStr);
		sqlStr.append(" union ");
		sqlStr.append(" select distinct emailinstitucional.email , pessoa.nome from funcionario ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sqlStr.append("  inner join pessoaemailinstitucional  emailinstitucional on emailinstitucional.pessoa = pessoa.codigo ");
		sqlStr.append(" where pessoa.ativo  = false and pessoa.dataUltimaAlteracao::date >= '"+Uteis.getDateHoraInicialDia(dataEvasao)+"' and  pessoa.dataUltimaAlteracao::date <= current_date ");
		sqlStr.append(" and not exists ( ");		
		sqlStr.append(" select m.aluno from matricula as m ");
		sqlStr.append(" where m.aluno = pessoa.codigo and  m.situacao = 'AT' ) ");
		sqlStr.append(" and emailinstitucional.email is not null and emailinstitucional.email != '' ");
		filtroBuscaPorNomeEmail(valorConsulta,campoConsulta, parametros, sqlStr);
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametros.toArray());
		while (dadosSQL.next()) {
			UsuarioVO usuario = new UsuarioVO();	
			usuario.getPessoa().setNome(dadosSQL.getString("nome"));		
			usuario.getPessoa().setEmail(dadosSQL.getString("email"));				
			usuarios.add(usuario);
		}
		return usuarios;
	}

	public void filtroBuscaPorNomeEmail(String valorConsulta, String campoConsulta, List<Object> parametros, StringBuilder str) {
		if (Uteis.isAtributoPreenchido(valorConsulta)) {
			if (campoConsulta.equals("nome")) {
				str.append(" and  pessoa.nome ilike ?");
			} else {
				str.append(" and emailinstitucional.email ilike ?");
			}
			parametros.add(PERCENT + valorConsulta + PERCENT);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSenhaShaSenhaMSCHAPV2(String senha, final UsuarioVO obj) throws Exception {
		try {

			final String sql = "UPDATE Usuario set senhaSHA=?, senhaMSCHAPV2=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(obj);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					try {
						sqlAlterar.setString(1, Uteis.encriptarSHA(senha));
						sqlAlterar.setString(2, Uteis.encriptarMSCHAPV2(senha));
					} catch (UnsupportedEncodingException e) {
						throw new SQLException(e.getMessage());
					}
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			obj.setSenhaSHA(Uteis.encriptarSHA(senha));
			obj.setSenhaMSCHAPV2(Uteis.encriptarMSCHAPV2(senha));
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Override
	public StringBuilder consultarUsuarioAlunoPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception {
		List<UsuarioVO> usuarios = new ArrayList<UsuarioVO>();
		StringBuilder sqlStr = new StringBuilder("select usuario.codigo as \"usuario.codigo\", usuario.username as \"usuario.username\", pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", pessoa.email as \"pessoa.email\" from usuario "); 
			sqlStr.append(" inner join pessoa on pessoa.codigo =  usuario.pessoa "); 
			sqlStr.append(" inner join matricula on matricula.aluno = pessoa.codigo where pessoa.aluno and usuario.solicitarnovasenha = ").append(solicitarNovaSenha);		
			if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
				sqlStr.append(" and matricula.unidadeensino = ").append(codigoUnidadeEnsino);
			}
			if(Uteis.isAtributoPreenchido(codigoCurso)) {
				sqlStr.append(" and matricula.curso = ").append(codigoCurso);
			}
		sqlStr.append(" order by usuario.codigo asc limit 1000");
		

		return sqlStr;
	}
	
	
	@Override
	public StringBuilder consultarUsuarioProfessorPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception {
		List<UsuarioVO> usuarios = new ArrayList<UsuarioVO>();
		StringBuilder sqlStr = new StringBuilder("select usuario.codigo as \"usuario.codigo\", usuario.username as \"usuario.username\", pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", pessoa.email as \"pessoa.email\" from usuario ");
			sqlStr.append(" inner join pessoa on pessoa.codigo =  usuario.pessoa "); 
			sqlStr.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo where pessoa.professor and usuario.solicitarnovasenha = ").append(solicitarNovaSenha);
			if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
				sqlStr.append(" and exists ");
				sqlStr.append(" ( ");
					sqlStr.append(" select funcionariocargo.codigo from funcionariocargo where funcionariocargo.funcionario = funcionario.codigo ");
					
						sqlStr.append(" and unidadeensino = ").append(codigoUnidadeEnsino);
					
				sqlStr.append(" ) ");
			}
			sqlStr.append(" and exists ");
			sqlStr.append(" ( ");
				sqlStr.append(" select horarioturmadiaitem.codigo from horarioturmadiaitem ");
				sqlStr.append(" inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
				sqlStr.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
				sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma ");
				sqlStr.append(" where horarioturmadiaitem.professor = pessoa.codigo ");
				if(Uteis.isAtributoPreenchido(codigoCurso)) {
					sqlStr.append(" and case when turma.turmaagrupada then exists ");
					sqlStr.append(" ( ");
							sqlStr.append(" select ta.codigo from turmaagrupada ta ");
							sqlStr.append(" inner join turma t2 ON t2.codigo = ta.turma ");
							sqlStr.append(" inner join curso on curso.codigo = t2.curso ");
							sqlStr.append(" where ta.turmaorigem = turma.codigo ");
							sqlStr.append(" and curso.codigo = ").append(codigoCurso);
					sqlStr.append(" ) else ");
					sqlStr.append(" turma.curso = ").append(codigoCurso);
					sqlStr.append(" end ");
					sqlStr.append(" and turma.curso = ").append(codigoCurso);
			
				}
				sqlStr.append(" )");
		sqlStr.append(" order by usuario.codigo asc limit 1000");		

		return sqlStr;
	}
	
	@Override
	public StringBuilder consultarUsuarioCoordenadorPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception {
		List<UsuarioVO> usuarios = new ArrayList<UsuarioVO>();
		StringBuilder sqlStr = new StringBuilder("select usuario.codigo as \"usuario.codigo\", usuario.username as \"usuario.username\", pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", pessoa.email as \"pessoa.email\" from usuario "); 
		sqlStr.append(" inner join pessoa on pessoa.codigo =  usuario.pessoa "); 
		sqlStr.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo where pessoa.coordenador and usuario.solicitarnovasenha = ").append(solicitarNovaSenha);	
		sqlStr.append(" and exists ");		
		sqlStr.append(" ( ");
			sqlStr.append(" select cursocoordenador.codigo from cursocoordenador where cursocoordenador.funcionario = funcionario.codigo ");
			if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
				sqlStr.append(" and cursocoordenador.unidadeensino = ").append(codigoUnidadeEnsino);
			}
			if(Uteis.isAtributoPreenchido(codigoCurso)) {
				sqlStr.append(" and cursocoordenador.curso = ").append(codigoCurso);
			}
		sqlStr.append(" ) ");
		sqlStr.append(" order by usuario.codigo asc limit 1000");

		return sqlStr;
	}
	
	@Override
	public StringBuilder consultarUsuarioFuncionarioPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoDepartamento, Boolean solicitarNovaSenha) throws Exception {
		List<UsuarioVO> usuarios = new ArrayList<UsuarioVO>();
		StringBuilder sqlStr = new StringBuilder("select usuario.codigo as \"usuario.codigo\", usuario.username as \"usuario.username\", pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\", pessoa.email as \"pessoa.email\" from usuario "); 
		sqlStr.append(" inner join pessoa on pessoa.codigo =  usuario.pessoa "); 
		sqlStr.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");	
		sqlStr.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");	
		sqlStr.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");	
		sqlStr.append(" where pessoa.funcionario and usuario.solicitarnovasenha = ").append(solicitarNovaSenha);
			if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
				sqlStr.append(" and funcionariocargo.unidadeensino = ").append(codigoUnidadeEnsino);
			}
			if(Uteis.isAtributoPreenchido(codigoDepartamento)) {
				sqlStr.append(" and cargo.departamento = ").append(codigoDepartamento);
			}
		sqlStr.append(" order by usuario.codigo asc limit 1000");

		return sqlStr;
	}
	
	@Override
	public Integer consultarTotalUsuarioAlunoPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception {
		List<UsuarioVO> usuarios = new ArrayList<UsuarioVO>();
		StringBuilder sqlStr = new StringBuilder("select count(usuario.codigo) from usuario "); 
			sqlStr.append(" inner join pessoa on pessoa.codigo =  usuario.pessoa "); 
			sqlStr.append(" inner join matricula on matricula.aluno = pessoa.codigo where pessoa.aluno and usuario.solicitarnovasenha = ").append(solicitarNovaSenha);		
			if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
				sqlStr.append(" and matricula.unidadeensino = ").append(codigoUnidadeEnsino);
			}
			if(Uteis.isAtributoPreenchido(codigoCurso)) {
				sqlStr.append(" and matricula.curso = ").append(codigoCurso);
			}
			SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (resultado.next()) {
				return resultado.getInt("count");
			}
			return 0;
	}
	
	@Override
	public Integer consultarTotalUsuarioProfessorPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception {
		List<UsuarioVO> usuarios = new ArrayList<UsuarioVO>();
		StringBuilder sqlStr = new StringBuilder("select count(usuario.codigo) from usuario ");
			sqlStr.append(" inner join pessoa on pessoa.codigo =  usuario.pessoa "); 
			sqlStr.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo where pessoa.professor and usuario.solicitarnovasenha = ").append(solicitarNovaSenha);
			if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
				sqlStr.append(" and exists ");
				sqlStr.append(" ( ");
					sqlStr.append(" select funcionariocargo.codigo from funcionariocargo where funcionariocargo.funcionario = funcionario.codigo ");
					
						sqlStr.append(" and unidadeensino = ").append(codigoUnidadeEnsino);
					
				sqlStr.append(" ) ");
			}
			sqlStr.append(" and exists ");
			sqlStr.append(" ( ");
				sqlStr.append(" select horarioturmadiaitem.codigo from horarioturmadiaitem ");
				sqlStr.append(" inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
				sqlStr.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
				sqlStr.append(" inner join turma on turma.codigo = horarioturma.turma ");
				sqlStr.append(" where horarioturmadiaitem.professor = pessoa.codigo ");
				if(Uteis.isAtributoPreenchido(codigoCurso)) {
					sqlStr.append(" and case when turma.turmaagrupada then exists ");
					sqlStr.append(" ( ");
							sqlStr.append(" select ta.codigo from turmaagrupada ta ");
							sqlStr.append(" inner join turma t2 ON t2.codigo = ta.turma ");
							sqlStr.append(" inner join curso on curso.codigo = t2.curso ");
							sqlStr.append(" where ta.turmaorigem = turma.codigo ");
							sqlStr.append(" and curso.codigo = ").append(codigoCurso);
					sqlStr.append(" ) else ");
					sqlStr.append(" turma.curso = ").append(codigoCurso);
					sqlStr.append(" end ");
					sqlStr.append(" and turma.curso = ").append(codigoCurso);
			
				}
				sqlStr.append(" )");
				SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				if (resultado.next()) {
					return resultado.getInt("count");
				}
				return 0;
	}
	
	@Override
	public Integer consultarTotalUsuarioCoordenadorPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoCurso, Boolean solicitarNovaSenha) throws Exception {
		List<UsuarioVO> usuarios = new ArrayList<UsuarioVO>();
		StringBuilder sqlStr = new StringBuilder("select count(usuario.codigo) from usuario "); 
		sqlStr.append(" inner join pessoa on pessoa.codigo =  usuario.pessoa "); 
		sqlStr.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo where pessoa.coordenador and usuario.solicitarnovasenha = ").append(solicitarNovaSenha);	
		sqlStr.append(" and exists ");		
		sqlStr.append(" ( ");
			sqlStr.append(" select cursocoordenador.codigo from cursocoordenador where cursocoordenador.funcionario = funcionario.codigo ");
			if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
				sqlStr.append(" and cursocoordenador.unidadeensino = ").append(codigoUnidadeEnsino);
			}
			if(Uteis.isAtributoPreenchido(codigoCurso)) {
				sqlStr.append(" and cursocoordenador.curso = ").append(codigoCurso);
			}
		sqlStr.append(" ) ");

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}
	
	@Override
	public Integer consultarTotalUsuarioFuncionarioPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino,  Integer codigoDepartamento, Boolean solicitarNovaSenha) throws Exception {
		List<UsuarioVO> usuarios = new ArrayList<UsuarioVO>();
		StringBuilder sqlStr = new StringBuilder("select count(usuario.codigo) from usuario "); 
		sqlStr.append(" inner join pessoa on pessoa.codigo =  usuario.pessoa "); 
		sqlStr.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");	
		sqlStr.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");	
		sqlStr.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");	
		sqlStr.append(" where pessoa.funcionario and usuario.solicitarnovasenha = ").append(solicitarNovaSenha);
			if(Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
				sqlStr.append(" and funcionariocargo.unidadeensino = ").append(codigoUnidadeEnsino);
			}
			if(Uteis.isAtributoPreenchido(codigoDepartamento)) {
				sqlStr.append(" and cargo.departamento = ").append(codigoDepartamento);
			}
			SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (resultado.next()) {
				return resultado.getInt("count");
			}
			return 0;
	}
	
	@Override
	public UsuarioVO obterUsuarioResponsavelOperacoesExternas() throws Exception  {		 
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, null, null);
		try {
			if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getUsuarioOperacaoExternas())) {
				return configuracaoGeralSistemaVO.getUsuarioOperacaoExternas();
			}
			List<UsuarioVO> usuarioVOs = new ArrayList<UsuarioVO>();
			usuarioVOs.addAll(getFacadeFactory().getUsuarioFacade().consultarPorCodigoPessoa(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null));
			if (!usuarioVOs.isEmpty()) {
				UsuarioVO usuarioVO = usuarioVOs.get(0);
				usuarioVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPerfilParaFuncionarioAdministrador(usuarioVO.getUnidadeEnsinoLogado().getCodigo(), usuarioVO));
				configuracaoGeralSistemaVO.setUsuarioOperacaoExternas(usuarioVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configuracaoGeralSistemaVO.getUsuarioOperacaoExternas();
	}
	
	private void validarPermissoes(PerfilAcessoPermissaoEnumInterface[] permissoes, UsuarioVO usuarioPersistido, UsuarioVO usuarioResponsavel) throws Exception {
		for (PerfilAcessoPermissaoEnumInterface permissao : permissoes) {
			validarPermissaoUsuarioPermitirAlterarPessoaUsuario(permissao, usuarioPersistido, usuarioResponsavel);
		}
	}
	
	private void validarPermissaoUsuarioPermitirAlterarPessoaUsuario(PerfilAcessoPermissaoEnumInterface permissao, UsuarioVO usuarioPersistido, UsuarioVO usuarioResponsavel) throws Exception {
		if (PerfilAcessoPermissaoAdministrativoEnum.USUARIO_PERMITIR_ALTERAR_PESSOA_USUARIO.equals(permissao) && !verificarPermissaoFuncionalidadeUsuario(PerfilAcessoPermissaoAdministrativoEnum.USUARIO_PERMITIR_ALTERAR_PESSOA_USUARIO.getValor(), usuarioResponsavel)) {
			UsuarioVO usuarioEstadoOriginal = consultarPorChavePrimaria(usuarioPersistido.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioResponsavel);
			if (!usuarioEstadoOriginal.getPessoa().getCodigo().equals(usuarioPersistido.getPessoa().getCodigo())) {
				throw new ConsistirException("USUÁRIO " + usuarioResponsavel.getNome().toUpperCase() + " não possui permissão para alterar a pessoa vinculada ao usuário.");
			}
		}
	}
	
	@Override
	public List<UsuarioVO> consultarUsuarioUnificacao(UsuarioVO usuarioManterVO, String campoUnificar, String valorUnificar, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(usuarioManterVO.getCodigo())) {
			throw new Exception("O campo USUÁRIO PERMANECER deve ser informado!");
		}
		validarDadosUsuarioUnificar(campoUnificar, valorUnificar, usuarioVO);
	
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT usuario.codigo, usuario.nome, usuario.username, usuario.tipoUsuario, usuario.perfilAdministrador, usuario.dataUltimaAlteracao ");
		sb.append(" from usuario ");
		sb.append(" inner join pessoa on pessoa.codigo = usuario.pessoa ");
		
		sb.append(" where trim(usuario.username) <> '").append(usuarioManterVO.getUsername().trim()).append("' ");

		if (campoUnificar.equals("NOME")) {
			sb.append(" and upper(sem_acentos(usuario.nome)) like upper(sem_acentos('"+valorUnificar +"%')) ");
		}
		if (campoUnificar.equals("USER_NAME")) {
			sb.append(" and usuario.username like '").append(valorUnificar).append("%' ");
		}
		if (campoUnificar.equals("CPF")) {
			sb.append(" and pessoa.cpf like '").append(valorUnificar).append("%' ");
		}
		sb.append("order by usuario.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		List<UsuarioVO> listaUsuarioUnificarVOs = new ArrayList<UsuarioVO>(0);
		while (tabelaResultado.next()) {
			UsuarioVO obj = new UsuarioVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			obj.setUsername(tabelaResultado.getString("username"));
			obj.setTipoUsuario(tabelaResultado.getString("tipoUsuario"));
			obj.setPerfilAdministrador(tabelaResultado.getBoolean("perfilAdministrador"));
			obj.setDataUltimaAlteracao(tabelaResultado.getTimestamp("dataUltimaAlteracao"));
			listaUsuarioUnificarVOs.add(obj);
		}
		return listaUsuarioUnificarVOs;

	}
	
	
	
	
	
	public void validarDadosUsuarioUnificar(String campoUnificar, String valorUnificar, UsuarioVO usuarioVO) throws Exception {
		if (campoUnificar.equals("NOME")) {
			if (valorUnificar.equals("")) {
				throw new Exception("O campo NOME deve ser informado!");
			}
		}
		if (campoUnificar.equals("USER_NAME")) {
			if (valorUnificar.equals("")) {
				throw new Exception("O campo USERNAME deve ser informado!");
			}
		}
		if (campoUnificar.equals("CPF")) {
			if (valorUnificar.equals("")) {
				throw new Exception("O campo CPF deve ser informado!");
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarUnificacaoUsuario(List<UsuarioVO> listaUsuarioUnificarVOs, UsuarioVO usuarioManterVO, UsuarioVO usuarioLogadoVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(usuarioManterVO.getCodigo())) {
			throw new Exception("O campo USUÁRIO PERMANECER deve ser informado!");
		}
		if (listaUsuarioUnificarVOs.isEmpty()) {
			throw new Exception("Não foi encontrado Usuário para Unificação, realize a consulta novamente.");
		}
		removerForumInteracaoGostouUsuarioUnificar(usuarioManterVO, listaUsuarioUnificarVOs, usuarioLogadoVO);
		for (UsuarioVO usuarioUnificarVO : listaUsuarioUnificarVOs) {
			if (usuarioUnificarVO.getSelecionado()) {
				
				StringBuilder sb = new StringBuilder();
				sb.append("select fn_removerduplicidadeusuario('").append(usuarioUnificarVO.getUsername()).append("','").append(usuarioManterVO.getUsername()).append("' )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogadoVO));
				getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void removerForumInteracaoGostouUsuarioUnificar(UsuarioVO usuarioManterVO, List<UsuarioVO> listaUsuarioUnificarVOs, UsuarioVO usuarioLogadoVO) throws Exception {
		Boolean existeRegistroUsuarioManter = getFacadeFactory().getForumInteracaoGostadoFacade().consultarForumIntegracaoGostadoPorUsuario(usuarioManterVO, usuarioLogadoVO);
		if (existeRegistroUsuarioManter) {
			for (UsuarioVO usuarioUnificarVO : listaUsuarioUnificarVOs) {
				if (usuarioUnificarVO.getSelecionado()) {
					getFacadeFactory().getForumInteracaoGostadoFacade().excluirPorUsuario(usuarioUnificarVO);
				}
			}
		}
	}
	
	public Map<String, Integer> removerUsuarioMinhaBiblioteca(List<UsuarioVO> usuarioVOs, ConsistirException consistirException) throws Exception {
		Map<String, Integer> resultado = new HashMap<>();
		resultado.put(SUCESSO, 0);
		resultado.put(ERRO, 0);
		ConfiguracaoBibliotecaVO configuracaoBibliotecaVO = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoMinhaBiblioteca();
		if (Uteis.isAtributoPreenchido(usuarioVOs)) {
			if (configuracaoBibliotecaVO.getPossuiIntegracaoMinhaBiblioteca()) {
				for (UsuarioVO usuarioVO : usuarioVOs) {
					removerUsuarioMinhaBiblioteca(usuarioVO, configuracaoBibliotecaVO, consistirException, resultado);
				}
			} else {
				throw new Exception("Integração com Minha Biblioteca está desabilitada na Configuração Biblioteca.");
			}
		}
		return resultado;
	}
	
	public static List<UsuarioVO> montarDadosConsultaUsuario(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<UsuarioVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(ControleAcesso.montarDadosUsuarioLogado(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPoliticaPrivacidade(final UsuarioVO obj) throws Exception {
		try {
			final String sql = "UPDATE Usuario set aceitouPoliticaPrivacidadeAluno = ?, aceitouPoliticaPrivacidadeProfessor = ? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, obj.getAceitouPoliticaPrivacidadeAluno());
					sqlAlterar.setBoolean(2, obj.getAceitouPoliticaPrivacidadeProfessor());
					sqlAlterar.setInt(3, obj.getCodigo());
					return sqlAlterar;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	private void criptografarSenhaUsuario(UsuarioVO usuario, String senha) throws Exception {
		usuario.setSenha(Uteis.encriptar(senha));
		usuario.setSenhaSHA(Uteis.encriptarSHA(senha));
		usuario.setSenhaMSCHAPV2(Uteis.encriptarMSCHAPV2(senha));
	}
	
	
	
	@Override
	public List consultarPorMatricula(String valorConsulta,  Integer codUnidadeEnsino, int nivelMontarDados, Integer limite, Integer offset ,DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sb = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
		sb.append("SELECT distinct Usuario.* FROM Usuario ");
		sb.append("left join UsuarioPerfilAcesso on Usuario.codigo = UsuarioPerfilAcesso.Usuario ");
		sb.append(" left join pessoa on pessoa.codigo = usuario.pessoa ");	
		sb.append(" left join matricula on matricula.aluno = pessoa.codigo ");		
		sb.append("WHERE matricula.matricula ilike ?  ");
	
		if (codUnidadeEnsino != null) {
			if (codUnidadeEnsino != 0) {
				sb.append("and (UsuarioPerfilAcesso.unidadeensino = ").append(codUnidadeEnsino).append(" or usuario.tipousuario in('AL', 'RF', 'PR', 'RL')) ");
			}
		}
		sb.append(" ) AS t ");
		if (limite != null) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sb.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString() ,valorConsulta+ PERCENT);
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	
	@Override
	public List consultarPorRegistroAcademico(String valorConsulta,  Integer codUnidadeEnsino, int nivelMontarDados,  Integer limite, Integer offset ,DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sb = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
		sb.append("SELECT distinct Usuario.* FROM Usuario ");	
		sb.append("left join UsuarioPerfilAcesso on Usuario.codigo = UsuarioPerfilAcesso.Usuario ");
		sb.append(" left join pessoa on pessoa.codigo = usuario.pessoa ");		
		
		sb.append("where  pessoa.registroAcademico ilike ? ");
		if (codUnidadeEnsino != null) {
			if (codUnidadeEnsino != 0) {
				sb.append("and (UsuarioPerfilAcesso.unidadeensino = ").append(codUnidadeEnsino).append(" or usuario.tipousuario in('AL', 'RF', 'PR', 'RL')) ");
			}
		}
		sb.append(" ) AS t ");
		if (limite != null) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sb.append(" OFFSET ").append(offset);
			}
		}
		
		dataModelo.setTotalRegistrosEncontrados(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<UsuarioVO> consultarPorCPF(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, DataModelo dataModelo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("SELECT count(*) over() as qtde_total_registro, usuario.* FROM usuario");
		sb.append(" INNER JOIN pessoa ON usuario.pessoa = pessoa.codigo ");
		sb.append(" WHERE translate(pessoa.cpf, '-.', '') ilike translate(?, '-.', '') order by codigo ");
		dataModelo.setTotalRegistrosEncontrados(0);
		if (limite != null) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sb.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registro"));
		}
		tabelaResultado.beforeFirst();
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<UsuarioVO> consultarPorEmailInstitucional(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso,  DataModelo dataModelo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT count(*) over() as qtde_total_registro, usuario.* FROM Usuario ");
		sb.append(" INNER JOIN pessoa ON usuario.pessoa = pessoa.codigo ");
		sb.append(" WHERE EXISTS (SELECT 1 FROM pessoaemailinstitucional WHERE pessoaemailinstitucional.pessoa = pessoa.codigo AND sem_acentos(pessoaemailinstitucional.email) ilike sem_acentos(?)) ");
		dataModelo.setTotalRegistrosEncontrados(0);
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		if (limite != null) {
			sb.append(" LIMIT ").append(limite);
			if (offset != null) {
				sb.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registro"));
		}
		tabelaResultado.beforeFirst();
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario)); 
	}
	
	public UsuarioVO consultarPorCPF(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT usuario.codigo AS usuario_codigo, pessoa.nome as pessoa_nome, pessoa.CPF as pessoa_cpf, pessoa.email as pessoa_email FROM Usuario ");
		sb.append(" INNER JOIN pessoa ON usuario.pessoa = pessoa.codigo ");
		sb.append(" WHERE translate(pessoa.cpf, '-.', '') ilike translate(?, '-.', '') order by usuario.codigo desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		UsuarioVO usuarioVO = new UsuarioVO();
		if(tabelaResultado.next()) {
			usuarioVO.setCodigo(tabelaResultado.getInt("usuario_codigo"));
			usuarioVO.getPessoa().setNome(tabelaResultado.getString("pessoa_nome"));
			usuarioVO.getPessoa().setCPF(tabelaResultado.getString("pessoa_cpf"));
			usuarioVO.getPessoa().setEmail(tabelaResultado.getString("pessoa_email"));
			return usuarioVO; 
			
		}
		return usuarioVO; 
	}
	
	
	public UsuarioVO consultarPorEmailInstitucional(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT usuario.codigo AS usuario_codigo, pessoa.nome as pessoa_nome, pessoa.CPF as pessoa_cpf, pessoa.email as pessoa_email FROM Usuario ");
		sb.append(" INNER JOIN pessoa ON usuario.pessoa = pessoa.codigo ");
		sb.append(" WHERE EXISTS (SELECT  FROM pessoaemailinstitucional WHERE pessoaemailinstitucional.pessoa = pessoa.codigo AND sem_acentos(pessoaemailinstitucional.email) ilike sem_acentos(?)) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		UsuarioVO usuarioVO = new UsuarioVO();
		if(tabelaResultado.next()) {
			usuarioVO.setCodigo(tabelaResultado.getInt("usuario_codigo"));
			usuarioVO.getPessoa().setNome(tabelaResultado.getString("pessoa_nome"));
			usuarioVO.getPessoa().setCPF(tabelaResultado.getString("pessoa_cpf"));
			usuarioVO.getPessoa().setEmail(tabelaResultado.getString("pessoa_email"));
			return usuarioVO; 
		}
		return usuarioVO; 
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarBooleanoResetouSenhaPrimeiroAcesso(Boolean resetouSenhaPrimeiroAcesso, UsuarioVO usuarioResetouSenha, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.alterar(getIdEntidade(), controlarAcesso, usuarioVO);
		final String sql = "UPDATE Usuario SET resetouSenhaPrimeiroAcesso = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setBoolean(1, resetouSenhaPrimeiroAcesso);
				sqlAlterar.setInt(2, usuarioResetouSenha.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	public UsuarioVO consultaRapidaPorMatriculaAluno(String matricula, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT usuario.* FROM usuario ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
		sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo ");
		sqlStr.append(" WHERE Matricula.matricula = ? ");
		sqlStr.append(" AND matricula.unidadeEnsino = ? ");
		sqlStr.append(" AND pessoa.aluno = true AND usuario.tipoUsuario = 'AL' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { matricula, unidadeEnsino });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados. (Usuário)");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}


	@Override
	public List<TipoOrigemDocumentoAssinadoEnum> realizarVerificacaoPermissaoTipoOrigemDocumentoAluno(UsuarioVO usuarioVO){
		List<TipoOrigemDocumentoAssinadoEnum> listaTipoOrigemDocumentoAssinadoEnum = new ArrayList<TipoOrigemDocumentoAssinadoEnum>();
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_BOLETIM_ACADEMICO, usuarioVO);		
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.BOLETIM_ACADEMICO);
		} catch (Exception e) {
			
		}
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_CERTIFICADOS, usuarioVO);		
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO);
		} catch (Exception e) {

		}
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_DECLARACAO, usuarioVO);		
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.DECLARACAO);
		} catch (Exception e) {

		}
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_DIPLOMAS, usuarioVO);		
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.DIPLOMA_DIGITAL);
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.DOCUMENTACAO_ACADEMICA_REGISTRO_DIPLOMA_DIGITAL);
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.EXPEDICAO_DIPLOMA);
		} catch (Exception e) {
		}
		
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_HISTORICO, usuarioVO);		
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.HISTORICO);
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.HISTORICO_DIGITAL);
		} catch (Exception e) {

		}

//		try {
//			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_REVALIDACAO_MATRICULA, usuarioVO);		
//			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.REVALIDACAO_MATRICULA);
//		} catch (Exception e) {
//
//		}
		
//		try {
//			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoVisaoAlunoEnum.PERMITIR_DOCUMENTOS_DIGITAIS_REQUERIMENTO, usuarioVO);		
			listaTipoOrigemDocumentoAssinadoEnum.add(TipoOrigemDocumentoAssinadoEnum.REQUERIMENTO);
//		} catch (Exception e) {
//
//		}
		
		return listaTipoOrigemDocumentoAssinadoEnum;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarBooleanoAtivoLdap(Boolean ativoLdap, UsuarioVO usuarioResetouSenha, UsuarioVO usuarioVO) throws Exception {		
		final String sql = "UPDATE Usuario SET ativoldap = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setBoolean(1, ativoLdap);
				sqlAlterar.setInt(2, usuarioResetouSenha.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	public Boolean consultarExisteUsuarioPorPessoaTipoUsuario(Integer codigoPessoa, Integer codigoUsuario, String tipoUsuario) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM usuario ");
		sql.append("WHERE pessoa = ? AND tipousuario = ? ");
		if (Uteis.isAtributoPreenchido(codigoUsuario)) {
			sql.append("AND codigo != ").append(codigoUsuario).append(" ");
		}
		sql.append("LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPessoa, tipoUsuario);
		return tabelaResultado.next();
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<UsuarioVO> consultarUsuariosPorPessoa(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Usuario WHERE pessoa = " + valorConsulta.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public void validarUnicidadeTipoUsuario(UsuarioVO usuario) throws Exception {
		if (usuario != null && consultarExisteUsuarioPorPessoaTipoUsuario(usuario.getPessoa().getCodigo(), usuario.getCodigo(), usuario.getTipoUsuario())) {
			throw new Exception("Não é possível cadastrar mais de 1 usuário com o mesmo tipo para o " + usuario.getNome() + ", pois o mesmo já tem um usuário do tipo \"" + usuario.getTipoUsuario_Apresentar() + "\"");
		}
	}
	
	public UsuarioVO consultarUsuarioNaoVinculadoFuncionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM usuario u ");
		sqlStr.append(" WHERE u.pessoa NOT IN (");
		sqlStr.append(" SELECT f.pessoa FROM funcionario f");
		sqlStr.append(" INNER JOIN pessoa p ON ");
		sqlStr.append(" p.codigo = f.pessoa ");
		sqlStr.append(" WHERE ");
		sqlStr.append(" f.pessoa = u.pessoa AND p.ativo) ");
		sqlStr.append(" AND u.pessoa = ").append(valorConsulta.intValue());
		sqlStr.append(" ORDER BY (CASE WHEN u.tipousuario = 'AL' THEN 0 ELSE 1 END) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new UsuarioVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public String realizarCarregamentoListaUsuarioAlunoAtivos() {
		StringBuilder sql  =  new StringBuilder("");
		sql.append(" 	select array_to_string(array_agg(distinct usuario.codigo), ',') as usuarios from matricula");
		sql.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula");
		sql.append(" 	and matriculaperiodo.codigo = (");
		sql.append(" 		select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula");
		sql.append(" 		order by mp.ano desc, mp.semestre desc limit 1");
		sql.append(" 	)");
		sql.append(" 	inner join usuario on usuario.pessoa = matricula.aluno");
		sql.append(" 	where matricula.situacao = 'AT'");
		sql.append(" 	and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");	
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()) {
			return rs.getString("usuarios");
		}
		return "";
	}
	
	@Override
	public void realizarCarregamentoDadosUsuarioAlunoAtivo(Integer usuario, ProgressBarVO progressBarVO)  throws Exception {
		try {
			UsuarioVO usuarioVO =  consultarPorChavePrimaria(usuario, Uteis.NIVELMONTARDADOS_DADOSBASICOS, progressBarVO.getUsuarioVO());
			if(getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().containsKey((usuarioVO.getUsername()+usuarioVO.getSenha()))){				
				getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().remove((usuarioVO.getUsername()+usuarioVO.getSenha()));
			}			
			usuarioVO.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(usuarioVO.getPessoa().getCodigo(), false, true, false, progressBarVO.getUsuarioVO()));
			usuarioVO.getPessoa().setNivelMontarDados(NivelMontarDados.TODOS);
			usuarioVO.setUsuarioFacilitador(getFacadeFactory().getEstagioFacade().realizarVerificacaoUsuarioFacilitador(usuarioVO.getPessoa().getCodigo()));
			getAplicacaoControle().getUsuarioAlunoPorUsernameSenhaCache().put((usuarioVO.getUsername()+usuarioVO.getSenha()), usuarioVO);			
//			List<PessoaEmailInstitucionalVO> pessoaEmailInstitucionalVOs = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(usuarioVO.getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, progressBarVO.getUsuarioVO());
			for(PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO: usuarioVO.getPessoa().getListaPessoaEmailInstitucionalVO().stream().filter(e -> e.getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.ATIVO)).collect(Collectors.toList())) {
				if(getAplicacaoControle().getUsuarioAlunoPorEmailCache().containsKey(pessoaEmailInstitucionalVO.getEmail())){
					getAplicacaoControle().getUsuarioAlunoPorEmailCache().remove(pessoaEmailInstitucionalVO.getEmail());
				}
				getAplicacaoControle().getUsuarioAlunoPorEmailCache().put(pessoaEmailInstitucionalVO.getEmail(), usuarioVO);
			}
			List<MatriculaVO> matriculaVOs = getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoPessoaNaoCancelada(usuarioVO.getPessoa().getCodigo(),false, false, true, true, usuarioVO, progressBarVO.getConfiguracaoGeralSistemaVO());
			if(getAplicacaoControle().getMatriculasAlunoCache().containsKey(usuarioVO.getCodigo())){
				getAplicacaoControle().getMatriculasAlunoCache().remove(usuarioVO.getCodigo());
			}
			if(matriculaVOs.isEmpty()) {
				return;
			}
			getAplicacaoControle().getMatriculasAlunoCache().put(usuarioVO.getCodigo(), matriculaVOs);			
			if(getAplicacaoControle().getMatriculaAvaliacaoInstitucionalCache().containsKey(matriculaVOs.get(0).getMatricula())) {
				getAplicacaoControle().getMatriculaAvaliacaoInstitucionalCache().remove(matriculaVOs.get(0).getMatricula());
			}
			getAplicacaoControle().getMatriculaAvaliacaoInstitucionalCache().put(matriculaVOs.get(0).getMatricula(), getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarAvaliacaoInstitucionalUsuarioLogado(usuarioVO, matriculaVOs.get(0)));
			if(getAplicacaoControle().getMatriculaAlunoCache().containsKey(matriculaVOs.get(0).getMatricula())) {
				getAplicacaoControle().getMatriculaAlunoCache().remove(matriculaVOs.get(0).getMatricula());
			}
			MatriculaVO matriculaVO =  getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(matriculaVOs.get(0).getMatricula(), 0, false, usuarioVO);
			getAplicacaoControle().getUnidadeEnsinoVO(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO);			
			matriculaVO.setMatriculaComHistoricoAlunoVO(getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(matriculaVO, matriculaVO.getGradeCurricularAtual().getCodigo(), false, matriculaVO.getCurso().getConfiguracaoAcademico(), usuarioVO));
			matriculaVO.setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(matriculaVO.getMatricula(), TipoSalaAulaBlackboardEnum.ESTAGIO, usuarioVO));
			matriculaVO.setSalaAulaBlackboardTcc(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(matriculaVO.getMatricula(), TipoSalaAulaBlackboardEnum.TCC_AMBIENTACAO, usuarioVO));
			matriculaVO.getGradeCurricularAtual().setPrazojubilamento(getFacadeFactory().getGradeCurricularFacade().consultarPrazoJubilamento(matriculaVO.getMatricula()));
			gerarPrazoJubilamentoMatricula(matriculaVO, usuarioVO);
			getAplicacaoControle().getMatriculaAlunoCache().put(matriculaVO.getMatricula(), matriculaVO);
			if(getAplicacaoControle().getMatriculaPeriodoAlunoCache().containsKey(matriculaVO.getMatricula())) {
				getAplicacaoControle().getMatriculaPeriodoAlunoCache().remove(matriculaVO.getMatricula());
			}
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			getAplicacaoControle().getMatriculaPeriodoAlunoCache().put(matriculaVO.getMatricula(), matriculaPeriodoVO);			
			if(getAplicacaoControle().getMatriculaIntegralizacaoCurricularCache().containsKey(matriculaVO.getMatricula())) {
				getAplicacaoControle().getMatriculaIntegralizacaoCurricularCache().remove(matriculaVO.getMatricula());
			}
			getAplicacaoControle().getMatriculaIntegralizacaoCurricularCache().put(matriculaVO.getMatricula(), getFacadeFactory().getMatriculaFacade().consultarPercentuaisIntegralizacaoCurricularMatricula(matriculaVO.getMatricula()));
							
			if(getAplicacaoControle().getAnoSemestreMatriculaCache().containsKey(matriculaVO.getMatricula())) {
				getAplicacaoControle().getAnoSemestreMatriculaCache().remove(matriculaVO.getMatricula());
			}
			getAplicacaoControle().getAnoSemestreMatriculaCache().put(matriculaVO.getMatricula(), getFacadeFactory().getHistoricoFacade().consultarAnoSemestreHistoricoPorMatricula(matriculaVO.getMatricula(), "{p}", null, null));
			
			if(getAplicacaoControle().getMatriculaPeriodoTurmaDisciplinasCache().containsKey(matriculaVO.getMatricula())) {
				getAplicacaoControle().getMatriculaPeriodoTurmaDisciplinasCache().remove(matriculaVO.getMatricula());
			}
			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs =  getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarDisciplinaDoAlunoPorMatricula(matriculaVO.getMatricula(), matriculaPeriodoVO.getAnoSemestreOriginal(), null, null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			getAplicacaoControle().getMatriculaPeriodoTurmaDisciplinasCache().put(matriculaVO.getMatricula(), matriculaPeriodoTurmaDisciplinaVOs);
			
			if(getAplicacaoControle().getCalendarioProjetoIntegradorCache().containsKey(matriculaVO.getMatricula())) {
				getAplicacaoControle().getCalendarioProjetoIntegradorCache().remove(matriculaVO.getMatricula());
			}
			getAplicacaoControle().getCalendarioProjetoIntegradorCache().put(matriculaVO.getMatricula(), getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorClassificacaoPorAnoPorSemestrePorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVOs, ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR, matriculaPeriodoVO.getAno(), matriculaPeriodoVO.getSemestre(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			if(getAplicacaoControle().getMatriculaEstagioDeferidoCache().containsKey(matriculaVO.getMatricula())) {
				getAplicacaoControle().getMatriculaEstagioDeferidoCache().remove(matriculaVO.getMatricula());
			}
			getAplicacaoControle().getMatriculaEstagioDeferidoCache().put(matriculaVO.getMatricula(), getFacadeFactory().getGradeCurricularEstagioFacade().consultarCargahorariaDeferidaEstagio(matriculaVO.getMatricula()));		
			if(getAplicacaoControle().getPreferenciaSistemaUsuarioCache().containsKey(usuarioVO.getCodigo())) {
				getAplicacaoControle().getPreferenciaSistemaUsuarioCache().remove(usuarioVO.getCodigo());
			}
			getAplicacaoControle().getPreferenciaSistemaUsuarioCache().put(usuarioVO.getCodigo(), getFacadeFactory().getPreferenciaSistemaUsuarioFacade().consultarPorUsuario(usuarioVO));		
			
			if(getAplicacaoControle().getLinksUteisUsuarioCache().containsKey(usuarioVO.getCodigo())) {
				getAplicacaoControle().getLinksUteisUsuarioCache().remove(usuarioVO.getCodigo());
			}
			getAplicacaoControle().getLinksUteisUsuarioCache().put(usuarioVO.getCodigo(), getFacadeFactory().getLinksUteisFacade().consultarLinksUteisUsuario(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));		
			
			if(usuarioVO.isUsuarioFacilitador()) {
				DashboardEstagioVO dash = new DashboardEstagioVO();
				dash.setSituacaoEstagioEnum(SituacaoEstagioEnum.EXIGIDO);
				getFacadeFactory().getEstagioFacade().executarInicializacaoTotalizadoresDashboardEstagio(dash, new EstagioVO(), matriculaPeriodoVO.getAno(), matriculaPeriodoVO.getSemestre(), usuarioVO);
				if(getAplicacaoControle().getDashboardEstagioFacilitadorCache().containsKey(matriculaVO.getMatricula())) {
					getAplicacaoControle().getDashboardEstagioFacilitadorCache().remove(matriculaVO.getMatricula());
				}
				getAplicacaoControle().getDashboardEstagioFacilitadorCache().put(matriculaVO.getMatricula(), dash);
			}
			DashboardEstagioVO dash = new DashboardEstagioVO();
			dash.setSituacaoEstagioEnum(SituacaoEstagioEnum.EXIGIDO);
			getFacadeFactory().getEstagioFacade().executarInicializacaoTotalizadoresDashboardEstagioPorMatricula(dash, matriculaVO.getMatricula(), usuarioVO);
			if(getAplicacaoControle().getDashboardEstagioAlunoCache().containsKey(matriculaVO.getMatricula())) {
				getAplicacaoControle().getDashboardEstagioAlunoCache().remove(matriculaVO.getMatricula());
			}
			getAplicacaoControle().getDashboardEstagioAlunoCache().put(matriculaVO.getMatricula(), dash);
			
			
			List<DashboardVO> dashboards = getFacadeFactory().getDashboardInterfaceFacade().consultarDashboardPorUsuarioAmbiente(usuarioVO, TipoVisaoEnum.ALUNO);
			if(getAplicacaoControle().getDashboardsAlunoCache().containsKey(usuarioVO.getCodigo())) {
				getAplicacaoControle().getDashboardsAlunoCache().remove(usuarioVO.getCodigo());
			}
			getAplicacaoControle().getDashboardsAlunoCache().put(usuarioVO.getCodigo(), dashboards);
		}catch (Exception e) {
			throw e;
		}
		
		
	}
	
	private void gerarPrazoJubilamentoMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(matriculaVO.getAnoIngresso()) && Uteis.isAtributoPreenchido(matriculaVO.getSemestreIngresso())) {
			try {				
			int anoInicial = Integer.parseInt(matriculaVO.getAnoIngresso());
			int semestreInicial = Integer.parseInt(matriculaVO.getSemestreIngresso());
			
			int periodoLetivoTotal = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPeriodoLetivosVOs().stream()
					.mapToInt(PeriodoLetivoVO::getPeriodoLetivo)
					.max()
					.orElseThrow(() -> new IllegalStateException("Lista de períodos vazia"));
			List<MatriculaPeriodoVO> matriculaPeriodoVOs = (getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatricula(matriculaVO.getMatricula(), false, usuarioVO));
			if(matriculaPeriodoVOs.isEmpty()) {
				return;
			}
			PeriodoLetivoVO periodoLetivoVO = (matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getUltimoPeriodoLetivoGrade());
//			matriculaVO.getGradeCurricularAtual().setPrazojubilamento(getFacadeFactory().getGradeCurricularFacade().consultarPrazoJubilamento(matriculaVO.getMatricula()));
			String anoSemestreJubilamento = matriculaVO.getGradeCurricularAtual().getPrazojubilamento();
			String anoSemestreSomaPeriodosInicioIngresso = calcularSomaSemestres(anoInicial,semestreInicial, periodoLetivoTotal);
			Optional<MatriculaPeriodoVO> UltimoMatriculaPeriodo = matriculaPeriodoVOs.stream().max(Comparator.comparing(m -> m.getData()));
			if (matriculaVO.getGradeCurricularAtual().getPrazojubilamento().isEmpty()) {
				return;
			}
			int periodoLetivoFinal = periodoLetivoVO.getPeriodoLetivo();
			int periodoLetivoAtual = matriculaPeriodoVOs.stream()
					.max(Comparator.comparing(MatriculaPeriodoVO::getData))
					.map(m -> m.getPeriodoLetivo().getPeriodoLetivo())
					.orElse(0);
			long totalMatriculas = matriculaPeriodoVOs.size();
			String cor = definirCorJubilamento(
					UltimoMatriculaPeriodo.get().getAno()+ "/" + UltimoMatriculaPeriodo.get().getSemestre(),
					anoSemestreSomaPeriodosInicioIngresso,
					anoSemestreJubilamento,
					periodoLetivoFinal,
					max(periodoLetivoAtual,totalMatriculas)
					);
			matriculaVO.getGradeCurricularAtual().setCorDoTextoJubilamento(cor);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String definirCorJubilamento(String periodoAtual,
											   String periodoLimiteRegular,
											   String periodoLimiteJubilamento,
											   int periodoLetivoFinal,
											   long periodoLetivoAtual) {

		if (periodoLetivoAtual <= periodoLetivoFinal) {
			return TEXTO_VERDE;
		}

		if (compararPeriodos(periodoAtual, periodoLimiteJubilamento) >= 0) {
			return TEXTO_VERMELHO;
		}

		if (compararPeriodos(periodoAtual, periodoLimiteRegular) > 0) {
			return TEXTO_AMARELO;
		}

		return TEXTO_VERMELHO;
	}
	
	public static final String TEXTO_VERDE = "texto-green";
	public static final String TEXTO_AMARELO = "texto-yellow";
	public static final String TEXTO_VERMELHO = "texto-red";

	private static int compararPeriodos(String periodo1, String periodo2) {
		String[] parts1 = periodo1.split("/");
		String[] parts2 = periodo2.split("/");

		int total1 = Integer.parseInt(parts1[0]) * 2 + Integer.parseInt(parts1[1]);
		int total2 = Integer.parseInt(parts2[0]) * 2 + Integer.parseInt(parts2[1]);

		return Integer.compare(total1, total2);
	}

}
