package negocio.facade.jdbc.arquitetura;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.Modification;
import com.unboundid.ldap.sdk.ModificationType;
import com.unboundid.ldap.sdk.ModifyDNRequest;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.arquitetura.RegistroLdapVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.Ldap.SituacaoRetornoLdapEnum;
import negocio.interfaces.arquitetura.LdapInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Ldap extends ControleAcesso implements LdapInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2533435851856026474L;
	private static String idEntidade;

	public Ldap() throws Exception {
		super();
		setIdEntidade("RegistroLdap");
	}

	public static String getIdEntidade() {
		return Ldap.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		Ldap.idEntidade = idEntidade;
	}
	
	public LDAPConnection executarAutenticacaoLdap(ConfiguracaoLdapVO conf, UsuarioVO usuario) throws Exception {
		LDAPConnection con = null;
		if (Uteis.isAtributoPreenchido(conf)) {
			if (conf.getPortaLdap().equals("636")) {
				SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
				con = new LDAPConnection(sslUtil.createSSLSocketFactory());
			} else {
				con = new LDAPConnection();
			}
			con.connect(conf.getHostnameLdap(), Integer.valueOf(conf.getPortaLdap()));
			con.bind(conf.getDnLdap(), conf.getSenhaLdap());
		}
		return con;
	}
	
	@Override
	public boolean consultarSeUsuarioExisteLdap(ConfiguracaoLdapVO conf, UsuarioVO usuario) throws Exception {
		LDAPConnection con = executarAutenticacaoLdap(conf, usuario);
		String atributo = conf.getUrlLoginAD().contains("microsoft") ? "sAMAccountName": "uid";
		if (con.isConnected()) {
			SearchResult resultado = pesquisarPorAtributo(conf, con, "", atributo, usuario.getUsername());
			for (SearchResultEntry objeto : resultado.getSearchEntries()) {
				return objeto.getAttributeValue(atributo).equals(usuario.getUsername());
			}
		}
		return false;
	}

	@Override
	public void executarSincronismoComLdapAoIncluirUsuario(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha, MatriculaVO matricula  , PessoaEmailInstitucionalVO pessoaEmailInstitucional ,UsuarioVO usuarioLogado) {
		this.executarSincronismoComLdapAoIncluirUsuario(conf, usuario, senha, null, matricula, pessoaEmailInstitucional,usuarioLogado );
	}

	public void executarSincronismoComLdapAoIncluirUsuario(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha, Integer registroLdap, MatriculaVO matricula , PessoaEmailInstitucionalVO pessoaEmailInstitucional,UsuarioVO usuarioLogado ) {
		LDAPConnection con = null;
		MatriculaVO matriculaVO = Uteis.isAtributoPreenchido(matricula) ? matricula : new MatriculaVO();
		try {
			if (Uteis.isAtributoPreenchido(conf)) {
				con = executarAutenticacaoLdap(conf, usuario);				
				if (con.isConnected()) {
					if (!usuario.getPossuiCadastroLdap()) {							
						if(!getFacadeFactory().getLdapFacade().consultarSeUsuarioExisteLdap(conf, usuario)) {
							this.incluirUsuario(conf, con, usuario, senha);
						}						
						usuario.setAtivoLdap(getFacadeFactory().getPessoaFacade().consultarSePessoaAtiva(usuario.getPessoa().getCodigo()));
						usuario.setPossuiCadastroLdap(true);
						getFacadeFactory().getUsuarioFacade().alterarPossuiCadastroLdap(usuario ,usuario);
					}
					this.registrarSincronismoLdap("sincronizarComLdapAoLogar", true, "", usuario, conf, registroLdap, matriculaVO, usuario.getPessoa(), pessoaEmailInstitucional,usuarioLogado);
				} else {
					throw new Exception("Falha ao conectar a " + conf.getHostnameLdap());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			matriculaVO.setProcessouLdapSemFalhar(Boolean.FALSE);
			this.registrarSincronismoLdap("sincronizarComLdapAoLogar", false, e.getMessage(), usuario, conf, registroLdap, matriculaVO, usuario.getPessoa(), pessoaEmailInstitucional,usuarioLogado);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}
	
	@Override
	public void executarSincronismoComLdapAoAlterarSenha(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha) {
		this.executarSincronismoComLdapAoAlterarSenha(conf,usuario, senha, null );
	}

	@Override
	public void executarSincronismoComLdapAoAlterarSenha(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha ,Integer registroLdap) {
		LDAPConnection con = null;
		try {
			if(!Uteis.isAtributoPreenchido(conf)) {
				conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(usuario.getPessoa().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(conf) && usuario.getPossuiCadastroLdap()) {
				con = executarAutenticacaoLdap(conf, usuario);
				if (con.isConnected()) {
					this.alterarSenhaUsuario(conf, con, usuario, senha);
				} else {
					throw new Exception("Falha ao conectar a " + conf.getHostnameLdap());
				}
				this.registrarSincronismoLdap("sincronizarComLdapAoAlterarSenha", true, "", usuario, conf, registroLdap, null, usuario.getPessoa(), null,usuario);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.registrarSincronismoLdap("sincronizarComLdapAoAlterarSenha", false, e.getMessage(), usuario, conf, registroLdap, null, usuario.getPessoa(), null,usuario);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	@Override
	public void reexecutarSincronismoComLdap(RegistroLdapVO obj, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoLdapVO conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarPorChavePrimaria(obj.getConfiguracaoLdap().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);		
		String senha = obj.getUsuario().getSenha();
		if(Uteis.isAtributoPreenchido(conf.getPrefixoSenha()) && Uteis.isAtributoPreenchido(obj.getMatricula().getAluno().getCPF())) {
			senha = conf.getPrefixoSenha() + Uteis.removerMascara(obj.getMatricula().getAluno().getCPF());
		} else if(Uteis.isAtributoPreenchido(conf.getPrefixoSenha()) && Uteis.isAtributoPreenchido(obj.getPessoaVO().getCPF())) {
			senha = conf.getPrefixoSenha() + Uteis.removerMascara(obj.getPessoaVO().getCPF());
		}
		if(obj.getOperacao().equals("sincronizarComLdapAoLogar")) {
			this.executarSincronismoComLdapAoIncluirUsuario(conf, obj.getUsuario(), senha, obj.getCodigo(), obj.getMatricula() , obj.getPessoaEmailInstitucionalVO(),usuarioVO);
		}else if(obj.getOperacao().equals("sincronizarComLdapAoAlterarSenha")) {
			this.executarSincronismoComLdapAoAlterarSenha(conf, obj.getUsuario(), senha,obj.getCodigo());
		}else if(obj.getOperacao().equals("sincronizarComLdapAoCancelarTransferirMatricula")) {			
			this.executarSincronismoComLdapAoCancelarTransferirMatricula(conf, obj.getUsuario(),  obj.getMatricula(), obj.getCodigo(), false,usuarioVO);
		}else if(obj.getOperacao().equals("sincronizarComLdapAoCancelarTransferirMatriculaEstorno")) {			
			this.executarSincronismoComLdapAoCancelarTransferirMatricula(conf, obj.getUsuario(),  obj.getMatricula(),obj.getCodigo(), true,usuarioVO);
		}else if(obj.getOperacao().equals("sincronizarAlteracaoAcadastral")) {			
			realizarAlterarDadosCadastraisPessoaLDAP(obj.getPessoaVO(), obj.getPessoaEmailInstitucionalVO(),obj.getCodigo(), usuarioVO);		
		}else if(obj.getOperacao().equals("sincronizarComLdapAoInativarConta")) {				
			this.executarSincronismoComLdapAoInativarContaPorPessoaEmailInstitucional(conf, obj.getUsuario(),  obj.getMatricula(), obj.getMatricula().getAluno(), false ,obj.getCodigo() ,obj.getPessoaEmailInstitucionalVO(),usuarioVO);		
		}else if(obj.getOperacao().equals("sincronizarComLdapAoInativarContaEstorno")) {			
			this.executarSincronismoComLdapAoInativarContaPorPessoaEmailInstitucional(conf, obj.getUsuario(),  obj.getMatricula(), obj.getMatricula().getAluno(), true,obj.getCodigo() ,obj.getPessoaEmailInstitucionalVO(),usuarioVO);
		}
	}

	private void incluirUsuario(ConfiguracaoLdapVO conf, LDAPConnection con, UsuarioVO usuario, String senha) throws Exception {
	    List<Attribute> attribs = new ArrayList<Attribute>();
	    Entry entry = null;
	    attribs.add(new Attribute("objectClass", "organizationalPerson"));
	    attribs.add(new Attribute("objectClass", "person"));
	    attribs.add(new Attribute("objectClass", "top"));
	    attribs.add(new Attribute("cn", usuario.getPessoa().getNome().trim()));
	    attribs.add(new Attribute("sn", Arrays.stream(usuario.getPessoa().getNome().split(" ")).skip(1).collect(Collectors.joining(" "))));
	    attribs.add(new Attribute("givenName", Arrays.stream(usuario.getPessoa().getNome().split(" ")).findFirst().orElse("")));
	    attribs.add(new Attribute("mail", this.obterEmail(conf, usuario)));

	    if(conf.getUrlLoginAD().contains("microsoft")) {
	        attribs.add(new Attribute("objectClass", "user"));
	        attribs.add(new Attribute("displayName", usuario.getPessoa().getNome().trim()));
	        attribs.add(new Attribute("sAMAccountName", usuario.getUsername().trim()));
	        attribs.add(new Attribute("userPrincipalName", this.obterEmail(conf, usuario)));
	        attribs.add(new Attribute("userAccountControl", "512"));
	        attribs.add(new Attribute("unicodePwd", encriptarSenhaAD(senha, usuario, conf)));
	        if(conf.getGrupo().contains("365")) {
	            attribs.add(new Attribute("proxyAddresses", "smtp:"+usuario.getUsername().trim()+"@o365." +conf.getDominio()));
	        }
	        entry = new Entry(this.getDistinguishedName(conf, usuario, false), attribs);
	    } else {
	        attribs.add(new Attribute("objectClass", "inetOrgPerson"));
	        attribs.add(new Attribute("objectClass", "univespAccount"));
	        attribs.add(new Attribute("cpf", Uteis.removerMascara(usuario.getPessoa().getCPF())));
	        attribs.add(new Attribute("dateOfInclusion", new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
	        attribs.add(new Attribute("login", this.obterEmail(conf, usuario)));
	        attribs.add(new Attribute("personalMail", usuario.getPessoa().getEmail().trim()));
	        attribs.add(new Attribute("registeredAddress", "true"));
	        attribs.add(new Attribute("uid", usuario.getUsername().trim()));
	        
	        if (conf.getFormatoSenhaLdap().contentEquals("SHA")) {
	            // Usando Base64 nativo do Java 8+
	            byte[] hexDecoded = Hex.decode(usuario.getSenhaSHA().trim().getBytes());
	            String base64 = Base64.getEncoder().encodeToString(hexDecoded);
	            attribs.add(new Attribute("userPassword", "{SHA}" + base64));
	        } else {
	            attribs.add(new Attribute("userPassword", encriptarSenhaAD(senha, usuario, conf)));
	        }
	        entry = new Entry(this.getUidDnRA(conf, false, usuario), attribs);
	    }
	    con.add(entry);
	    
	    if(!conf.getGrupo().isEmpty()) {
	        for (String grupo : conf.getGrupo().split(",")) {
	            adicionarUsuarioAoGrupoCasoNaoExista(conf, con, usuario, grupo);
	        }
	    }
	}
	
	

	private byte[] encriptarSenhaAD(String senha, UsuarioVO usuario, ConfiguracaoLdapVO conf) throws Exception {
	    if (conf.getFormatoSenhaLdap().contentEquals("SHA256")) {
	        return usuario.getSenha().getBytes();
	    } else if (conf.getFormatoSenhaLdap().contentEquals("SHA")) {
	        // Usando Base64 nativo do Java 8+
	        return Base64.getEncoder().encode(usuario.getSenhaSHA().getBytes());
	    } else if (conf.getFormatoSenhaLdap().contentEquals("MSCHAPV2")) {
	        return usuario.getSenhaMSCHAPV2().getBytes();
	    } else {
	        return ("\"" + senha + "\"").getBytes(StandardCharsets.UTF_16LE);
	    }
	}

	
	private String getUidDnRA(ConfiguracaoLdapVO conf, Boolean estorno ,UsuarioVO usuario) {
		return String.format("uid=%s,%s,%s", Uteis.isAtributoPreenchido(usuario.getPessoa().getRegistroAcademico()) ? usuario.getPessoa().getRegistroAcademico() :usuario.getUsername(), ( estorno ? conf.getDiretorioInativacao() : conf.getDiretorio()	), conf.getDcLdap());
	}
	private String getDistinguishedName(ConfiguracaoLdapVO conf, UsuarioVO usuario ,Boolean estorno) {
		if(Uteis.isAtributoPreenchido(conf.getDiretorio())) {
			return String.format("CN=%s,%s,%s", usuario.getPessoa().getNome().trim(),  ( estorno ? conf.getDiretorioInativacao() : conf.getDiretorio()	), conf.getDcLdap());
		}
		return String.format("CN=%s,%s", usuario.getPessoa().getNome().trim(),  ( estorno ? conf.getDiretorioInativacao() : conf.getDiretorio()	));
	}

	private void alterarSenhaUsuario(ConfiguracaoLdapVO conf, LDAPConnection con, UsuarioVO usuario, String senha) throws Exception {
	    Modification mod = null;
	    if(conf.getUrlLoginAD().contains("microsoft")) {
	        mod = new Modification(ModificationType.REPLACE, "unicodePwd", encriptarSenhaAD(senha, usuario, conf));
	        con.modify(this.getDistinguishedName(conf, usuario, false), mod);
	    } else {
	        if (conf.getFormatoSenhaLdap().contentEquals("SHA")) {
	            
	            String base64 = Base64.getEncoder().encodeToString(Hex.decode(usuario.getSenhaSHA().getBytes()));
	            mod = new Modification(ModificationType.REPLACE, "userPassword", "{SHA}" + base64);
	        } else {
	            mod = new Modification(ModificationType.REPLACE, "userPassword", this.encriptarSenhaAD(senha, usuario, conf));
	        }
	        con.modify(this.getUidDnRA(conf, false, usuario), mod);
	    }
	}
	
	
	
	
	private void adicionarUsuarioAoGrupoCasoNaoExista(ConfiguracaoLdapVO conf, LDAPConnection con, UsuarioVO usuario, String grupo) throws Exception {
		boolean pertence = false;
		SearchResult resultado = pesquisarPorAtributo(conf, con, "", "sAMAccountName", usuario.getUsername());
		if (!resultado.getSearchEntries().isEmpty()) {
			SearchResultEntry objeto = resultado.getSearchEntries().get(0);
			try {
				for (String dn : objeto.getAttributeValues("memberOf")) {
					if (dn != null && dn.equals(consultarLdapPeloCN(conf, con, grupo).getDN())) {
						pertence = true;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!pertence) {
				adicionarCNAoGrupo(conf, con, objeto.getDN(), grupo);
			}
		}
	}
	
	private void adicionarCNAoGrupo(ConfiguracaoLdapVO conf, LDAPConnection con, String dn, String grupo) throws Exception {
		Modification mod = new Modification(ModificationType.ADD, "member", dn);
		con.modify(consultarLdapPeloCN(conf, con, grupo).getDN(), mod);
	}
	
	private SearchResultEntry consultarLdapPeloCN(ConfiguracaoLdapVO conf, LDAPConnection con, String grupo) throws Exception {
		SearchResult resultado = pesquisarPorCN(conf, con, montarAtributo("CN", grupo));
		return resultado.getSearchEntries().get(0);
	}
	
	private SearchResult pesquisarPorCN(ConfiguracaoLdapVO conf, LDAPConnection con, String cn) throws Exception {
		SearchRequest ldapRequest = new SearchRequest(conf.getDcLdap().trim(), SearchScope.SUB, adicionarParenteses(cn));
		return con.search(ldapRequest);
	}
	
	private SearchResult pesquisarPorAtributo(ConfiguracaoLdapVO conf, LDAPConnection con, String ou, String atributo, String valor) throws Exception {
		SearchRequest ldapRequest = new SearchRequest(adicionarDomainComponent(conf, ou), SearchScope.SUB, adicionarParenteses(montarAtributo(atributo, valor)));
		return con.search(ldapRequest);
	}
	
	private String adicionarDomainComponent(ConfiguracaoLdapVO conf, String ou) throws Exception {
		if (ou.trim().isEmpty()) {
			return conf.getDcLdap().trim();
		}
		return new StringBuilder(ou.trim()).append(",").append(conf.getDcLdap().trim()).toString();
	}	
	
	private String adicionarParenteses(String texto) throws Exception {
		return new StringBuilder("(").append(texto).append(")").toString();
	}
	
	private String montarAtributo(String atributo, String valor) throws Exception {
		return new StringBuilder(atributo.trim()).append("=").append(valor.trim()).toString();
	}
	
	@Override
	public void realizarAlterarDadosCadastraisPessoaLDAP(PessoaVO pessoaVO, PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO,Integer registroLdap, UsuarioVO usuarioVO) {
		Thread t = new Thread(new AlterarDadosCadastraisPessoaLDAP(pessoaVO, pessoaEmailInstitucionalVO, usuarioVO, registroLdap));
		t.start();
	}
	
	private class AlterarDadosCadastraisPessoaLDAP  implements Runnable {			
		private PessoaVO pessoaVO; 
		private UsuarioVO usuarioVO;
		private PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO;
		private Integer registroLdap;
		
		public AlterarDadosCadastraisPessoaLDAP(PessoaVO pessoaVO, PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO, UsuarioVO usuarioVO ,Integer registroLdap) {
			super();
			this.pessoaVO = pessoaVO;
			this.usuarioVO = usuarioVO;
			this.pessoaEmailInstitucionalVO = pessoaEmailInstitucionalVO;
			this.registroLdap = registroLdap;
		}


		public void run() {
			LDAPConnection con = null;
			ConfiguracaoLdapVO	conf = null;
			UsuarioVO usuario = null;
		try {
			Thread.sleep(6000);
			usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(pessoaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if((Uteis.isAtributoPreenchido(usuario) && usuario.getPossuiCadastroLdap())) {			
				conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(pessoaVO.getCodigo());
				if(Uteis.isAtributoPreenchido(conf)) {
					con = executarAutenticacaoLdap(conf, usuario);
					con.connect(conf.getHostnameLdap(), Integer.valueOf(conf.getPortaLdap()));
					con.bind(conf.getDnLdap(), conf.getSenhaLdap());
					if (con.isConnected()) {
						 List<Modification> mods = new ArrayList<Modification>(0);
						 Modification mod = new Modification(ModificationType.REPLACE, "cn", pessoaVO.getNome());						
						 mods.add(mod);
						 mod = new Modification(ModificationType.REPLACE, "sn", Arrays.stream(pessoaVO.getNome().split(" ")).skip(1).collect(Collectors.joining(" ")));
						 mods.add(mod);
						 mod = new Modification(ModificationType.REPLACE, "personalMail" , pessoaVO.getEmail());
						 mods.add(mod);
						 mod = new Modification(ModificationType.REPLACE, "givenName" ,Arrays.stream(pessoaVO.getNome().split(" ")).findFirst().orElse(""));
						 mods.add(mod);						 
						 con.modify(getUidDnRA(conf,false, usuario), mods);
						registrarSincronismoLdap("sincronizarAlteracaoAcadastral", true, "", usuario, conf, registroLdap, null, pessoaVO, pessoaEmailInstitucionalVO,usuarioVO);
					}else {
						throw new Exception("Falha ao conectar a " + conf.getHostnameLdap());
					}			
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			registrarSincronismoLdap("sincronizarAlteracaoAcadastral", false, e.getMessage(), usuarioVO, conf, registroLdap, null, pessoaVO, pessoaEmailInstitucionalVO,usuarioVO);
		} finally {
			if (con != null) {
				con.close();
			}
		}
		}
	}

	

	private String obterEmail(ConfiguracaoLdapVO conf, UsuarioVO usuario) {
		return usuario.getUsername().concat(conf.getDominio().startsWith("@") ? conf.getDominio() : "@".concat(conf.getDominio()));
	}

	private void registrarSincronismoLdap(String operacao, boolean sucesso, String excessao, UsuarioVO usuario, ConfiguracaoLdapVO conf, Integer registroLdap, MatriculaVO matricula, PessoaVO pessoaVO, PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO , UsuarioVO usuarioLogado) {
		try {
			RegistroLdapVO registro = new RegistroLdapVO();
			registro.setUsuario(usuario);
			registro.setOperacao(operacao);
			registro.setSucesso(sucesso);
			registro.setExcessao(excessao);
			registro.setConfiguracaoLdap(conf);
			if(Uteis.isAtributoPreenchido(pessoaVO)) {
				registro.setPessoaVO(pessoaVO);
			}
			if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
				registro.setPessoaEmailInstitucionalVO(pessoaEmailInstitucionalVO);
			}
			if (matricula != null && Uteis.isAtributoPreenchido(matricula.getMatricula())) {
				registro.setMatricula(matricula);
			}
			if (!Uteis.isAtributoPreenchido(registroLdap)) {
				this.incluirRegistroLdap(registro, usuarioLogado);
			} else {
				registro.setCodigo(registroLdap);
				this.alterarRegistroLdap(registro, usuarioLogado);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void incluirRegistroLdap(final RegistroLdapVO registro, UsuarioVO usuarioVO) throws Exception {
		try {
			if (!registro.getSucesso()) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.LDAP, "excessao: " + registro.getExcessao().replaceAll("\u0000", ""));
			}
			final String sql = "INSERT INTO registroldap(usuario, operacao, resumo, sucesso, excessao, configuracaoldap, matricula, pessoa, pessoaemailinstitucional) VALUES (?,?,?,?,?,?,?,?,?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			registro.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					Uteis.setValuePreparedStatement(registro.getUsuario().getCodigo(), 1, sqlInserir);
					Uteis.setValuePreparedStatement(registro.getOperacao(), 2, sqlInserir);
					Uteis.setValuePreparedStatement(registro.getResumo(), 3, sqlInserir);
					Uteis.setValuePreparedStatement(registro.getSucesso(), 4, sqlInserir);
					Uteis.setValuePreparedStatement(registro.getExcessao().replaceAll("\u0000", ""), 5, sqlInserir);
					Uteis.setValuePreparedStatement(registro.getConfiguracaoLdap(), 6, sqlInserir);
					if(Uteis.isAtributoPreenchido(registro.getMatricula().getMatricula())) {
						Uteis.setValuePreparedStatement(registro.getMatricula().getMatricula(), 7, sqlInserir);
					}else {
						Uteis.setValuePreparedStatement(null, 7, sqlInserir);
					}
					Uteis.setValuePreparedStatement(registro.getPessoaVO(), 8, sqlInserir);
					Uteis.setValuePreparedStatement(registro.getPessoaEmailInstitucionalVO(), 9, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void alterarRegistroLdap(final RegistroLdapVO registro, UsuarioVO usuarioVO) throws Exception {
		try {
			if (!registro.getSucesso()) {
				AplicacaoControle.realizarEscritaTextoDebug(AssuntoDebugEnum.LDAP, "excessao: " + registro.getExcessao().replaceAll("\u0000", ""));
			}
			final String sql = "UPDATE registroldap SET usuario=?, operacao=?, resumo=?, sucesso=?, excessao=?, configuracaoldap=?, matricula=?, pessoa = ?, pessoaemailinstitucional =? WHERE codigo=? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					Uteis.setValuePreparedStatement(registro.getUsuario().getCodigo(), 1, sqlAlterar);
					Uteis.setValuePreparedStatement(registro.getOperacao(), 2, sqlAlterar);
					Uteis.setValuePreparedStatement(registro.getResumo(), 3, sqlAlterar);
					Uteis.setValuePreparedStatement(registro.getSucesso(), 4, sqlAlterar);
					Uteis.setValuePreparedStatement(registro.getExcessao().replaceAll("\u0000", ""), 5, sqlAlterar);
					Uteis.setValuePreparedStatement(registro.getConfiguracaoLdap(), 6, sqlAlterar);
					Uteis.setValuePreparedStatement(registro.getMatricula().getMatricula(), 7, sqlAlterar);
					Uteis.setValuePreparedStatement(registro.getPessoaVO(), 8, sqlAlterar);
					Uteis.setValuePreparedStatement(registro.getPessoaEmailInstitucionalVO(), 9, sqlAlterar);
					Uteis.setValuePreparedStatement(registro.getCodigo(), 10, sqlAlterar);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void consultar(DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT count(*) over() as totalRegistroConsulta, registroldap.* FROM registroldap left JOIN usuario on registroldap.usuario = usuario.codigo left JOIN pessoa on registroldap.pessoa = pessoa.codigo left JOIN matricula on registroldap.matricula = matricula.matricula left JOIN pessoa as aluno on matricula.aluno = aluno.codigo ";
		Object valorConsulta = dataModelo.getValorConsulta();
		if (dataModelo.getCampoConsulta().equals("codigo") && Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			sqlStr += "WHERE registroldap.codigo = ? ";
			valorConsulta = Integer.valueOf(dataModelo.getValorConsulta());
			sqlStr += "ORDER BY registroldap.codigo desc ";
		} else if (dataModelo.getCampoConsulta().equals("username") && Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			sqlStr += "WHERE sem_acentos(usuario.username) ilike sem_acentos(?) ";
			sqlStr += "ORDER BY  usuario.username, registroldap.codigo desc   ";
		} else if (dataModelo.getCampoConsulta().equals("nome") && Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			sqlStr += "WHERE (sem_acentos(usuario.nome)  ilike sem_acentos(?) or sem_acentos(pessoa.nome)  ilike sem_acentos(?) or sem_acentos(aluno.nome)  ilike sem_acentos(?)) ";
			sqlStr += "ORDER BY pessoa.nome, usuario.nome, registroldap.codigo desc   ";
		} else if (dataModelo.getCampoConsulta().equals("matricula") && Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			sqlStr += "WHERE sem_acentos(registroldap.matricula) ilike sem_acentos(?) ";
			sqlStr += "ORDER BY  registroldap.matricula, registroldap.codigo desc   ";
		}
		
		
		sqlStr += " limit "+dataModelo.getLimitePorPagina();
		sqlStr += " offset "+dataModelo.getOffset();
		SqlRowSet tabelaResultado = null;
		
		if(Uteis.isAtributoPreenchido(valorConsulta) && dataModelo.getCampoConsulta().equals("nome") ) {
			valorConsulta = valorConsulta+"%";
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta, valorConsulta, valorConsulta);
		}else if(Uteis.isAtributoPreenchido(valorConsulta) && dataModelo.getCampoConsulta().equals("codigo") ) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
		}else if(Uteis.isAtributoPreenchido(valorConsulta)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		}		
		montarTotalizadorConsultaBasica(dataModelo, tabelaResultado);
		dataModelo.setListaConsulta(montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	private List<RegistroLdapVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<RegistroLdapVO> vetResultado = new ArrayList<RegistroLdapVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(this.montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private RegistroLdapVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RegistroLdapVO obj = new RegistroLdapVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(dadosSQL.getInt("usuario"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setConfiguracaoLdap(getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarPorChavePrimaria(dadosSQL.getInt("configuracaoldap"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(dadosSQL.getString("matricula")));
		obj.setPessoaVO(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(dadosSQL.getInt("pessoa"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorChavePrimaria(dadosSQL.getInt("pessoaEmailInstitucional"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setOperacao(dadosSQL.getString("operacao"));
		obj.setResumo(dadosSQL.getString("resumo"));
		obj.setSucesso(dadosSQL.getBoolean("sucesso"));
		obj.setExcessao(dadosSQL.getString("excessao"));
		return obj;
	}

	@Override
	public boolean testarConexaoLdap(ConfiguracaoLdapVO conf, UsuarioVO usuario) throws Exception {
		LDAPConnection con = null;
		try {
			con = executarAutenticacaoLdap(conf, usuario);
			if (con.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		} finally {
			if (con != null) {
				con.close();
			}
		}
		return false;
	}
	
	@Override
	public void executarCriacaoContaTeste(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha) throws Exception {
		LDAPConnection con = null;
		try {
			if (Uteis.isAtributoPreenchido(conf)) {
				con = executarAutenticacaoLdap(conf, usuario);
				if (con.isConnected()) {						
					this.incluirUsuario(conf, con, usuario, senha);
				} else {
					throw new Exception("Falha ao conectar a " + conf.getHostnameLdap());
				}
			}
		} catch (Exception e) {
			throw e;			
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}
	
	@Override
	public void executarExclusaoContaTeste(ConfiguracaoLdapVO conf, UsuarioVO usuario, String senha) throws Exception {
		LDAPConnection con = null;
		try {
			if (Uteis.isAtributoPreenchido(conf)) {
				con = executarAutenticacaoLdap(conf, usuario);
				if (con.isConnected()) {
					con.delete(this.getDistinguishedName(conf, usuario,false));					
				} else {
					throw new Exception("Falha ao conectar a " + conf.getHostnameLdap());
				}
			}
		} catch (Exception e) {
			throw e;			
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	@Override
	public void executarSincronismoComLdapAoCancelarTransferirMatricula(ConfiguracaoLdapVO conf, UsuarioVO usuario, MatriculaVO matricula, Integer registroLdap ,Boolean estorno,UsuarioVO usuarioLogado) {		
		PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  null;
		try {
			if(Uteis.isAtributoPreenchido(matricula.getMatricula()) && (!Uteis.isAtributoPreenchido(matricula.getAluno()) || !Uteis.isAtributoPreenchido(matricula.getCurso()))) {
				getFacadeFactory().getMatriculaFacade().carregarDados(matricula, usuarioLogado);
			}
			if(!Uteis.isAtributoPreenchido(matricula.getCurso().getConfiguracaoLdapVO()) && Uteis.isAtributoPreenchido(matricula.getCurso())) {
				matricula.getCurso().setConfiguracaoLdapVO(getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorCurso(matricula.getCurso().getCodigo()));
			}
			if (!estorno && getFacadeFactory().getMatriculaFacade().verificarExisteMultiplasMatriculasAtivasDominioLDAP(matricula, usuarioLogado)) {
				return;
			}
			if(!Uteis.isAtributoPreenchido(conf)) {
				if(Uteis.isAtributoPreenchido(matricula.getCurso().getConfiguracaoLdapVO())) {
					conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarPorChavePrimaria(matricula.getCurso().getConfiguracaoLdapVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
				}else if(Uteis.isAtributoPreenchido(matricula.getMatricula()) && Uteis.isAtributoPreenchido(matricula.getCurso().getCodigo())) {
					conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorCurso(matricula.getCurso().getCodigo());
				}else {
					conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(matricula.getAluno().getCodigo());
				}
				matricula.getCurso().setConfiguracaoLdapVO(conf);
			}			
			pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorMatricula(matricula.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
			SituacaoRetornoLdapEnum  retorno =  getFacadeFactory().getLdapFacade().verificarExistenciaContaLdapPorRegistroAcademicoEmailInstitucional(conf, usuario, pessoaEmailInstitucionalVO.getEmail(),matricula.getAluno().getRegistroAcademico()); 			
            if((estorno && retorno.isInativo()) || (!estorno && retorno.isAtivo())) {
            	executarSincronismoComLdapAoInativarContaPorPessoaEmailInstitucional(conf, usuario, matricula, matricula.getAluno(), estorno,registroLdap, pessoaEmailInstitucionalVO,usuarioLogado);			
            }
		} catch (Exception e) {
			e.printStackTrace();
			String operacao = estorno ? "sincronizarComLdapAoCancelarTransferirMatriculaEstorno" : "sincronizarComLdapAoCancelarTransferirMatricula";
			this.registrarSincronismoLdap(operacao, false, e.getMessage(), usuario, conf, registroLdap, matricula, matricula.getAluno(), pessoaEmailInstitucionalVO,usuarioLogado);
		}
	}
	
	
	
	@Override
	public void executarSincronismoComLdapAoInativarContaPorPessoaEmailInstitucional(ConfiguracaoLdapVO conf, UsuarioVO usuario, MatriculaVO matricula, PessoaVO pessoa, Boolean estorno ,Integer registroLdap, PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO,UsuarioVO usuarioLogado) {
		LDAPConnection con = null;		
		try {
			if(!Uteis.isAtributoPreenchido(conf)) {
				if(Uteis.isAtributoPreenchido(matricula) && Uteis.isAtributoPreenchido(matricula.getCurso()) && Uteis.isAtributoPreenchido(matricula.getCurso().getCodigo())) {
					conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorCurso(matricula.getCurso().getCodigo());
				}else {
					conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(pessoa.getCodigo());
				}
			}
			if (Uteis.isAtributoPreenchido(conf) && usuario.getPossuiCadastroLdap()) {
				con = executarAutenticacaoLdap(conf, usuario);				
				if (con.isConnected()) {					
					String diretorioOrigem = estorno ? conf.getDiretorioInativacao() : conf.getDiretorio();
					String diretorioDestino = estorno ? conf.getDiretorio() : conf.getDiretorioInativacao();
					
					if(conf.getUrlLoginAD().contains("microsoft")) {						
						ModifyDNRequest mod = new ModifyDNRequest(String.format("CN=%s,%s,%s",   usuario.getPessoa().getNome()  , diretorioOrigem, conf.getDcLdap()),
								String.format("CN=%s", usuario.getPessoa().getNome() ),	true,String.format("%s,%s", diretorioDestino, conf.getDcLdap()));
						con.modifyDN(mod);						
						Modification modf = null;
						String codigoAtivarInativar = estorno ? "512" : "514";
				    	modf = new Modification(ModificationType.REPLACE, "userAccountControl",codigoAtivarInativar);
				    	con.modify(this.getDistinguishedName(conf, usuario,!estorno), modf);
				    	if(estorno) {
				    		this.alterarSenhaUsuario(conf, con, usuario, "V1rtuAL$2025");
				    	}
				    		
					}else {						
 						ModifyDNRequest mod = new ModifyDNRequest(String.format("uid=%s,%s,%s", Uteis.isAtributoPreenchido(usuario.getPessoa().getRegistroAcademico()) ? usuario.getPessoa().getRegistroAcademico() :usuario.getUsername(), diretorioOrigem, conf.getDcLdap()),
						String.format("uid=%s", Uteis.isAtributoPreenchido(usuario.getPessoa().getRegistroAcademico()) ? usuario.getPessoa().getRegistroAcademico() :usuario.getUsername()),
						true,String.format("%s,%s", diretorioDestino, conf.getDcLdap()));
				        con.modifyDN(mod);						
					}				 
				} else {
					throw new Exception("Falha ao conectar a " + conf.getHostnameLdap());
				}
				getFacadeFactory().getUsuarioFacade().alterarBooleanoAtivoLdap(estorno, usuario, usuario);
				String operacao = estorno ? "sincronizarComLdapAoInativarContaEstorno" : "sincronizarComLdapAoInativarConta";
				this.registrarSincronismoLdap(operacao, true, "", usuario, conf, registroLdap, matricula, pessoa, pessoaEmailInstitucionalVO,usuarioLogado);				
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(matricula != null) {
				matricula.setProcessouLdapSemFalhar(Boolean.FALSE);
			}
			String operacao = estorno ? "sincronizarComLdapAoInativarContaEstorno" : "sincronizarComLdapAoInativarConta";
			this.registrarSincronismoLdap(operacao, false, e.getMessage(), usuario, conf, registroLdap, matricula, pessoa, pessoaEmailInstitucionalVO,usuarioLogado);
						
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}
	
		
	
    public enum SituacaoRetornoLdapEnum {

        ATIVO(1,"Ativo",""),
        INATIVO(2,"Inativo",""),
        USUARIONAOENCONTRADO(3,"Usuário não Encontrado.",""),
    	SITUACAONAOENCONTRADA(4,"Situação não Encontrado.",""),
    	LDAPNAOCONECTADO(5,"LDAP não Conectado.",""),
    	NAOPOSSUICONFLDAP(6,"Conf LDAP não Encontrado.","");

	    private Integer valor;
        private String descricao;
        private String uidValidar;
        private String emailValidar;

        SituacaoRetornoLdapEnum(Integer valor, String descricao,String uidValidar) {
            this.descricao = descricao;
            this.valor = valor;
            this.uidValidar =uidValidar;
        }

        public String getDescricao() {
            return descricao;
        }
        public Integer getValor() {
        	return valor;
        }
        
        public String getUIDValidar() {
        	return uidValidar;
        }
        public void setUIDValidar(String uidValidar) {
        	this.uidValidar =uidValidar;;
        }
        
        public String getEmailValidar() {
        	return emailValidar;
        }
        public void setEmailValidar(String emailValidar) {
        	this.emailValidar =emailValidar;;
        }
        
        
		public static SituacaoRetornoLdapEnum getEnumPorValor(Integer valor) {
			for (SituacaoRetornoLdapEnum situacaoRetornoLdapEnum : SituacaoRetornoLdapEnum.values()) {
				if (situacaoRetornoLdapEnum.getValor().equals( valor)) {
					return situacaoRetornoLdapEnum;
				}
			}
			return SITUACAONAOENCONTRADA;
		}      
		
		public boolean isAtivo(){
	    	return name().equals(SituacaoRetornoLdapEnum.ATIVO.name());
	    }
	    
	    public boolean isInativo(){
	    	return name().equals(SituacaoRetornoLdapEnum.INATIVO.name());
	    }
	    
	    public boolean isAtivoInativo(){
	    	return name().equals(SituacaoRetornoLdapEnum.ATIVO.name()) || 
	    			name().equals(SituacaoRetornoLdapEnum.INATIVO.name());
	    }
	    public boolean isUsuarioNaoEncontrado(){
	    	return name().equals(SituacaoRetornoLdapEnum.USUARIONAOENCONTRADO.name());
	    }
       
    }
	
	@Override
	public SituacaoRetornoLdapEnum  verificarExistenciaContaLdapPorRegistroAcademicoEmailInstitucional(ConfiguracaoLdapVO conf , UsuarioVO usuario , String emailInstitucional , String numeroRegistroAcademico) throws Exception {
	            
		LDAPConnection con = null;
		try {			
			    if (!Uteis.isAtributoPreenchido(conf)){	
				   return SituacaoRetornoLdapEnum.NAOPOSSUICONFLDAP;
			    }	
				try {
				   con = executarAutenticacaoLdap(conf, usuario);
				   if (!con.isConnected()){	
						return SituacaoRetornoLdapEnum.LDAPNAOCONECTADO;
				   }
				}catch (Exception e) {
					return SituacaoRetornoLdapEnum.LDAPNAOCONECTADO;
				}							
				// primeiro verificaremos se existe conta para usuario  com uid  pelo registro academico 
				String atributo = conf.getUrlLoginAD().contains("microsoft") ? "sAMAccountName": "uid";
				SearchResult resultado = pesquisarPorAtributo(conf, con, "", atributo, numeroRegistroAcademico);			
				// caso nao encontre pelo registro academico verificaremos se existe conta para usuario  com email institucional
				if (resultado == null ||  resultado.getSearchEntries() == null ||  resultado.getSearchEntries().isEmpty()) {
					 resultado = pesquisarPorAtributo(conf, con, "","mail" , emailInstitucional);											 
				}
				
				if (resultado != null && resultado.getSearchEntries() != null && !resultado.getSearchEntries().isEmpty()) {					
					SituacaoRetornoLdapEnum situacaoRetorno = getSituacaoRegistroLdap(resultado.getSearchEntries().get(0));
					validarPreenchimentoEmailUserNameLdap(situacaoRetorno, resultado.getSearchEntries().get(0));					
					return situacaoRetorno;											
				}	
				return SituacaoRetornoLdapEnum.USUARIONAOENCONTRADO ;
			}catch (Exception e) {
				throw e;
			}finally {
				if (con != null) {
					con.close();
				}
			}
	}

	
	


	public void validarPreenchimentoEmailUserNameLdap( SituacaoRetornoLdapEnum situacaoRetorno,SearchResultEntry objetoResultEntry) {		
			// caso esteja ativo ou inativo por identificador email institucional 
			// ou seja encontrou usuario por email institucional  
			// neste caso vamos  recuperar o uid da conta ldap para validar se o identificador registro academico  
			// que sera usado pelo aluno e o mesmo da informação uid da conta ldap .
			if(objetoResultEntry != null &&  objetoResultEntry.getAttribute("uid") != null &&  objetoResultEntry.getAttribute("uid").getValue() != null ) {				
				situacaoRetorno.setUIDValidar(objetoResultEntry.getAttribute("uid").getValue());
			}
			// caso esteja ativo ou inativo por identificador registro academico
			// ou seja encontrou conta pelo uid  usando registro academico   
			// neste caso vamos recuperar o email da conta ldap para validar se o email institucional 
			// que sera usado pelo aluno e o mesmo da conta ldap .
			if(objetoResultEntry != null &&   objetoResultEntry.getAttribute("mail") != null && objetoResultEntry.getAttribute("mail").getValue() != null ) {				
				 situacaoRetorno.setEmailValidar(objetoResultEntry.getAttribute("mail").getValue());
			}	
		
	}


	@Override
	public void realizarCriacaoUsuarioLDAP_AlterandoDadosUsuarioVeteranoParaPrimeiroAcesso(ConfiguracaoLdapVO configuracaoLdapPorCurso, 
			UsuarioVO usuarioAlteracao, MatriculaVO matriculaVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema ,Boolean criarNovoPessoaEmailInstitucional ,
			PessoaEmailInstitucionalVO pessoaEmailInstitucionalExistente , UsuarioVO usuarioLogado) throws Exception {
		
		    usuarioAlteracao.setSenha(matriculaVO.getAluno().getRegistroAcademico());
		    usuarioAlteracao.setUsername(matriculaVO.getAluno().getRegistroAcademico());
			if (configuracaoGeralSistema.getGerarSenhaCpfAluno()){
				usuarioAlteracao.setSenha(Uteis.removerMascara(matriculaVO.getAluno().getCPF()));
			}
			String senhaAntesCriptografia = usuarioAlteracao.getSenha();
			if(Uteis.isAtributoPreenchido(configuracaoLdapPorCurso.getPrefixoSenha())) {
			   senhaAntesCriptografia = configuracaoLdapPorCurso.getPrefixoSenha() + Uteis.removerMascara(matriculaVO.getAluno().getCPF());
			}
			if(!usuarioAlteracao.getResetouSenhaPrimeiroAcesso()) {
			  getFacadeFactory().getUsuarioFacade().alterarUserNameSenhaAlteracaoSenhaAluno( usuarioAlteracao, usuarioAlteracao.getUsername(), senhaAntesCriptografia, false, usuarioLogado );
			  getFacadeFactory().getUsuarioFacade().alterarBooleanoResetouSenhaPrimeiroAcesso(true, usuarioAlteracao, false, usuarioLogado);		
			}	
			PessoaEmailInstitucionalVO emailInstitucional =  getFacadeFactory().getPessoaEmailInstitucionalFacade().incluirPessoaEmailInstitucional(configuracaoLdapPorCurso, pessoaEmailInstitucionalExistente , criarNovoPessoaEmailInstitucional,  usuarioAlteracao,usuarioLogado);
			usuarioAlteracao.setPossuiCadastroLdap(false); 
			getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoIncluirUsuario(configuracaoLdapPorCurso, usuarioAlteracao, senhaAntesCriptografia, matriculaVO,emailInstitucional,usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarProcessamentoAtivacaoRegistroLdapBlackBoard(MatriculaVO matriculaVO,ConfiguracaoLdapVO configuracaoLdapPorCurso,UsuarioVO usuarioAlteracao,ConfiguracaoGeralSistemaVO configuracaoGeralSistema,UsuarioVO usuarioLogado) throws Exception {			
		PessoaEmailInstitucionalVO emailInstitucional = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoaPrivilegiandoRegistroAcademicoDominio(matriculaVO.getAluno().getCodigo(), configuracaoLdapPorCurso.getCodigo(),false ,Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		 if(Uteis.isAtributoPreenchido(emailInstitucional)) {
		
					SituacaoRetornoLdapEnum  situacaoRetorno =  getFacadeFactory().getLdapFacade().verificarExistenciaContaLdapPorRegistroAcademicoEmailInstitucional(configuracaoLdapPorCurso, usuarioAlteracao, emailInstitucional.getEmail(),matriculaVO.getAluno().getRegistroAcademico()); 			
					if(situacaoRetorno != null && situacaoRetorno.isInativo() &&  configuracaoLdapPorCurso.getUrlLoginAD().contains("microsoft")) {
						getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoInativarContaPorPessoaEmailInstitucional(configuracaoLdapPorCurso, usuarioAlteracao, matriculaVO, matriculaVO.getAluno(), true,null, emailInstitucional,usuarioLogado);
						
					}else if(situacaoRetorno != null && situacaoRetorno.isAtivoInativo()  && !configuracaoLdapPorCurso.getUrlLoginAD().contains("microsoft")) {
						validarUidEmailUsuarioLdap(emailInstitucional,configuracaoLdapPorCurso, usuarioAlteracao,matriculaVO, configuracaoGeralSistema,situacaoRetorno,usuarioLogado); 	 			  
						 				 
					}else if(situacaoRetorno != null && situacaoRetorno.isUsuarioNaoEncontrado()) {				  			    	 
						  getFacadeFactory().getLdapFacade().realizarCriacaoUsuarioLDAP_AlterandoDadosUsuarioVeteranoParaPrimeiroAcesso(configuracaoLdapPorCurso, usuarioAlteracao, matriculaVO, configuracaoGeralSistema,false, emailInstitucional, usuarioLogado);
		 			} 		
					
 			 reativarEmailInstitucionalEPessoaBlackBoard(matriculaVO, usuarioLogado, emailInstitucional);
 	  	 }else {
 		     getFacadeFactory().getLdapFacade().realizarCriacaoUsuarioLDAP_AlterandoDadosUsuarioVeteranoParaPrimeiroAcesso(configuracaoLdapPorCurso, usuarioAlteracao, matriculaVO, configuracaoGeralSistema,  true,emailInstitucional,usuarioLogado);
 		 }
	}
	
	public void validarUidEmailUsuarioLdap(PessoaEmailInstitucionalVO emailInstitucional,
			ConfiguracaoLdapVO configuracaoLdapPorCurso, UsuarioVO usuarioMatricula, MatriculaVO matriculaVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema, SituacaoRetornoLdapEnum retorno, UsuarioVO usuarioLogado) throws Exception {
		
		if(!usuarioMatricula.getAtivoLdap() || !usuarioMatricula.getPossuiCadastroLdap()) {
			   usuarioMatricula.setAtivoLdap(true);
			   usuarioMatricula.setPossuiCadastroLdap(true);		    		   
			   getFacadeFactory().getUsuarioFacade().alterarPossuiCadastroLdap(usuarioMatricula, usuarioLogado); 	 			    	 
		}		  
	    if(Uteis.isAtributoPreenchido(retorno.getUIDValidar()) && !matriculaVO.getAluno().getRegistroAcademico().equals(retorno.getUIDValidar())) { 						   
	    	alterarCampoRegistroAcademicoLdapAtivandoCasoInativo(configuracaoLdapPorCurso, emailInstitucional,usuarioMatricula, matriculaVO,retorno.getUIDValidar() , retorno.isInativo() ,null, usuarioLogado);
	    }else if(Uteis.isAtributoPreenchido(retorno.getEmailValidar()) && !emailInstitucional.getEmail().equals(retorno.getEmailValidar())) { 
	    	alterarEmailUsuarioLdap(configuracaoLdapPorCurso, usuarioMatricula, matriculaVO ,emailInstitucional ,retorno.isInativo() , null, usuarioLogado );  
	    	if(retorno.isInativo()){
	    		alterarCampoRegistroAcademicoLdapAtivandoCasoInativo(configuracaoLdapPorCurso, emailInstitucional,usuarioMatricula, matriculaVO,matriculaVO.getAluno().getRegistroAcademico() , retorno.isInativo() , null, usuarioLogado);
	    	}	    		
	    }else if(retorno.isInativo()){
	    	alterarCampoRegistroAcademicoLdapAtivandoCasoInativo(configuracaoLdapPorCurso, emailInstitucional,usuarioMatricula, matriculaVO,matriculaVO.getAluno().getRegistroAcademico() , retorno.isInativo() , null, usuarioLogado);
	    }     
	}
	
	public void reativarEmailInstitucionalEPessoaBlackBoard(MatriculaVO matriculaVO, UsuarioVO usuarioLogado,PessoaEmailInstitucionalVO emailInstitucional) throws Exception {
		if(matriculaVO.getProcessouLdapSemFalhar()){				
		    if(emailInstitucional.getStatusAtivoInativoEnum().isInativo()) {
			    emailInstitucional.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
				getFacadeFactory().getPessoaEmailInstitucionalFacade().alterarSituacaoStatusAtivoInativoEnum(emailInstitucional, usuarioLogado);
			}
		    getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarAtivacaoPessoaBlack(emailInstitucional.getEmail(), matriculaVO.getResponsavelAtualizacaoMatriculaFormada());					  
		}
	}
	
	
	
	
	
	private void alterarCampoRegistroAcademicoLdapAtivandoCasoInativo(ConfiguracaoLdapVO conf,PessoaEmailInstitucionalVO emailInstitucional,	 UsuarioVO usuarioMatricula, MatriculaVO matriculaVO,  String campoValidar , Boolean estorno ,Integer registroLdap, UsuarioVO usuarioLogado) throws Exception {
		LDAPConnection con = null;
		String operacao = "sincronizarComLdapAoAlterarCampoRegistroLdapAtivandoCasoInativo";
		try {			
			 if(Uteis.isAtributoPreenchido(conf)){			
				con = executarAutenticacaoLdap(conf, usuarioLogado);
				if(con.isConnected()){
					String diretorio = estorno ? conf.getDiretorioInativacao() : conf.getDiretorio();		    	
			    	ModifyDNRequest mod = new ModifyDNRequest(
			    			String.format("uid=%s,%s,%s",campoValidar, diretorio, conf.getDcLdap()),
							String.format("uid=%s", matriculaVO.getAluno().getRegistroAcademico()),
							true,
							String.format("%s,%s", conf.getDiretorio(), conf.getDcLdap()));
					con.modifyDN(mod);
					this.registrarSincronismoLdap(operacao, true, "", usuarioMatricula, conf, registroLdap, matriculaVO, matriculaVO.getAluno(), emailInstitucional, usuarioLogado);
				} else {
					throw new Exception("Falha ao conectar a " + conf.getHostnameLdap());
				}				
			 }
			}catch (Exception e) {				
				e.printStackTrace();
				matriculaVO.setProcessouLdapSemFalhar(Boolean.FALSE);
				this.registrarSincronismoLdap(operacao, false, e.getMessage(), usuarioMatricula, conf, registroLdap, matriculaVO, matriculaVO.getAluno(), emailInstitucional,usuarioLogado);
				
			}finally {
				if (con != null) {
					con.close();
				}
			}
	}
	
	
	
	private void alterarEmailUsuarioLdap(ConfiguracaoLdapVO conf,  UsuarioVO usuarioMatricula, MatriculaVO matriculaVO ,PessoaEmailInstitucionalVO emailInstitucional, Boolean estorno , Integer registroLdap ,UsuarioVO usuarioLogado) throws Exception {
		LDAPConnection con = null;
		try {			
			 if(Uteis.isAtributoPreenchido(conf)){			
				con = executarAutenticacaoLdap(conf, usuarioLogado);
				if(con.isConnected()){						
					Modification mod = null;
			    	mod = new Modification(ModificationType.REPLACE, "mail",emailInstitucional.getEmail());
			    	con.modify(this.getUidDnRA(conf,estorno, usuarioMatricula), mod);
					this.registrarSincronismoLdap("sincronizarComLdapAoalterarEmailUsuarioLdap", true, "", usuarioMatricula, conf, registroLdap, matriculaVO, matriculaVO.getAluno(), emailInstitucional, usuarioLogado);
				 }
			 }
			}catch (Exception e) {
				e.printStackTrace();
				matriculaVO.setProcessouLdapSemFalhar(Boolean.FALSE);
				this.registrarSincronismoLdap("sincronizarComLdapAoalterarEmailUsuarioLdap", false, e.getMessage(), usuarioMatricula, conf, registroLdap, matriculaVO, matriculaVO.getAluno(), emailInstitucional,usuarioLogado);
			}finally {
				if (con != null) {
					con.close();
				}
			}
	}
	
	
	public SituacaoRetornoLdapEnum getSituacaoRegistroLdap(SearchResultEntry objetoResultEntry){		
		if(objetoResultEntry != null &&
				objetoResultEntry.getDN() != null &&
				!objetoResultEntry.getDN().toString().isEmpty() && 
				(objetoResultEntry.getDN().contains("ou=ativo"))){			 
			return SituacaoRetornoLdapEnum.ATIVO; 				
		}else if(objetoResultEntry != null &&
				objetoResultEntry.getDN() != null &&
				!objetoResultEntry.getDN().toString().isEmpty() && 
				(objetoResultEntry.getDN().contains("ou=inativo"))) {
			return SituacaoRetornoLdapEnum.INATIVO; 		
		}else if(objetoResultEntry != null && 
				objetoResultEntry.getAttributeValues("userAccountControl") != null ) {			
			for(String codigoAtivoInativo: objetoResultEntry.getAttributeValues("userAccountControl")) {
				if(codigoAtivoInativo != null && !codigoAtivoInativo.isEmpty() && codigoAtivoInativo.equals("512")) {
					return SituacaoRetornoLdapEnum.ATIVO; 
				}
				if(codigoAtivoInativo != null && !codigoAtivoInativo.isEmpty() && codigoAtivoInativo.equals("514")) {
					return SituacaoRetornoLdapEnum.INATIVO; 
				}
			}
		} 	
		return SituacaoRetornoLdapEnum.USUARIONAOENCONTRADO; 
	}
			
}
