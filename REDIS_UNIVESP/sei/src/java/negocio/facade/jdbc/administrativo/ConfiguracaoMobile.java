package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

import negocio.comuns.administrativo.ConfiguracaoMobileVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.ead.AnotacaoDisciplina;
import negocio.interfaces.administrativo.ConfiguracaoMobileInterfaceFacade;

/**
 * @author Victor Hugo de Paula Costa - 3 de nov de 2016
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoMobile extends ControleAcesso implements ConfiguracaoMobileInterfaceFacade {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 3 de nov de 2016
	 */

	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ConfiguracaoMobile.idEntidade = idEntidade;
	}

	public ConfiguracaoMobile() throws Exception {
		super();
		setIdEntidade("ConfiguracaoMobile");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(configuracaoMobileVO);
			verificarUnicidadeDePadrao(configuracaoMobileVO);
			AnotacaoDisciplina.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO configuracaomobile (nome, situacao, IdRemetenteGoogle, CertificadoAPNSApple, senhaCertificadoApns, certificadoApnsDestribuicao, IdRemetenteGoogleProfessor, CertificadoAPNSAppleProfessor, senhaCertificadoApnsProfessor, certificadoApnsDestribuicaoProfessor, padrao) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);			configuracaoMobileVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, configuracaoMobileVO.getNome());
					sqlInserir.setString(2, configuracaoMobileVO.getSituacao().getName());
					sqlInserir.setString(3, configuracaoMobileVO.getIdRemetenteGoogle());
					sqlInserir.setBytes(4, configuracaoMobileVO.getCertificadoAPNSApple());
					sqlInserir.setString(5, configuracaoMobileVO.getSenhaCertificadoApns());
					sqlInserir.setBoolean(6, configuracaoMobileVO.getCertificadoDestribuicao());
					sqlInserir.setString(7, configuracaoMobileVO.getIdRemetenteGoogleProfessor());
					sqlInserir.setBytes(8, configuracaoMobileVO.getCertificadoAPNSAppleProfessor());
					sqlInserir.setString(9, configuracaoMobileVO.getSenhaCertificadoApnsProfessor());
					sqlInserir.setBoolean(10, configuracaoMobileVO.getCertificadoDestribuicaoProfessor());
					sqlInserir.setBoolean(11, configuracaoMobileVO.getPadrao());
					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						configuracaoMobileVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			configuracaoMobileVO.setNovoObj(Boolean.TRUE);
			configuracaoMobileVO.setCodigo(0);
			throw e;
		}
	}

	public void validarDados(ConfiguracaoMobileVO configuracaoMobileVO) throws Exception {
		if (configuracaoMobileVO.getNome().isEmpty()) {
			throw new Exception("O campo Nome é obrigatório.");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (configuracaoMobileVO.getCodigo().equals(0)) {
			validarDados(configuracaoMobileVO);
			incluir(configuracaoMobileVO, verificarAcesso, usuarioVO);
		} else {
			alterar(configuracaoMobileVO, verificarAcesso, usuarioVO);
		}

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(configuracaoMobileVO);
			ConfiguracaoMobile.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			verificarUnicidadeDePadrao(configuracaoMobileVO);
			final String sql = "UPDATE configuracaomobile set nome=?, situacao=?, IdRemetenteGoogle = ?, CertificadoAPNSApple = ?, senhaCertificadoApns = ?, certificadoApnsDestribuicao = ?, IdRemetenteGoogleProfessor = ?, CertificadoAPNSAppleProfessor = ?, senhaCertificadoApnsProfessor = ?, certificadoApnsDestribuicaoProfessor = ?, padrao = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, configuracaoMobileVO.getNome());
					sqlAlterar.setString(2, configuracaoMobileVO.getSituacao().getName());
					sqlAlterar.setString(3, configuracaoMobileVO.getIdRemetenteGoogle());
					sqlAlterar.setBytes(4, configuracaoMobileVO.getCertificadoAPNSApple());
					sqlAlterar.setString(5, configuracaoMobileVO.getSenhaCertificadoApns());
					sqlAlterar.setBoolean(6, configuracaoMobileVO.getCertificadoDestribuicao());
					sqlAlterar.setString(7, configuracaoMobileVO.getIdRemetenteGoogleProfessor());
					sqlAlterar.setBytes(8, configuracaoMobileVO.getCertificadoAPNSAppleProfessor());
					sqlAlterar.setString(9, configuracaoMobileVO.getSenhaCertificadoApnsProfessor());
					sqlAlterar.setBoolean(10, configuracaoMobileVO.getCertificadoDestribuicaoProfessor());
					sqlAlterar.setBoolean(11, configuracaoMobileVO.getPadrao());
					sqlAlterar.setInt(12, configuracaoMobileVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoMobile.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM configuracaomobile WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, configuracaoMobileVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	public ConfiguracaoMobileVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) {
		ConfiguracaoMobileVO obj = new ConfiguracaoMobileVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setNome(tabelaResultado.getString("nome"));
		obj.setSituacao(SituacaoEnum.valueOf(tabelaResultado.getString("situacao")));
		obj.setIdRemetenteGoogle(tabelaResultado.getString("IdRemetenteGoogle"));
		obj.setCertificadoAPNSApple((byte[])tabelaResultado.getObject("CertificadoAPNSApple"));
		obj.setSenhaCertificadoApns(tabelaResultado.getString("senhaCertificadoApns"));
		obj.setCertificadoDestribuicao(tabelaResultado.getBoolean("certificadoApnsDestribuicao"));
		obj.setIdRemetenteGoogleProfessor(tabelaResultado.getString("IdRemetenteGoogleProfessor"));
		obj.setCertificadoAPNSAppleProfessor((byte[])tabelaResultado.getObject("CertificadoAPNSAppleProfessor"));
		obj.setSenhaCertificadoApnsProfessor(tabelaResultado.getString("senhaCertificadoApnsProfessor"));
		obj.setCertificadoDestribuicaoProfessor(tabelaResultado.getBoolean("certificadoApnsDestribuicaoProfessor"));
		obj.setPadrao(tabelaResultado.getBoolean("padrao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			return obj;
		}
		return obj;
	}

	public List<ConfiguracaoMobileVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ConfiguracaoMobileVO> vetResultado = new ArrayList<ConfiguracaoMobileVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}

	@Override
	public List<ConfiguracaoMobileVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		Integer codigo;
		if (campoConsulta.equals("nome")) {
			if (valorConsulta.length() < 2) {
				throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
			} else {
				return consultarPorNome(valorConsulta, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			}
		} else {
			if (campoConsulta.equals("codigo")) {
				if (!Uteis.isAtributoPreenchido(valorConsulta)) {
					throw new Exception(UteisJSF.internacionalizar("msg_dados_parametroConsulta"));
				} else {
					try {
						codigo = Integer.parseInt(valorConsulta);
					} catch (Exception e) {
						throw new Exception(UteisJSF.internacionalizar("msg_validarSomenteNumeroString"));
					}
					return consultarPorChavePrimaria(codigo, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
				}
			}
		}
		return new ArrayList<>(0);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConfiguracaoMobileVO> consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM ConfiguracaoMobile WHERE codigo = ?";

		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql, codigo), nivelMontarDados, usuarioLogado));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConfiguracaoMobileVO> consultarPorNome(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sqlStr = "SELECT * FROM ConfiguracaoMobile WHERE upper(nome) like('" + valorConsulta.toUpperCase() + "%') ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr), nivelMontarDados, usuarioLogado));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoAtivo(final ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (configuracaoMobileVO.getCodigo().equals(0)) {
				throw new Exception("Essa Configuração Mobile não se encontra gravada.");
			}
			if (configuracaoMobileVO.getSituacao().equals(SituacaoEnum.ATIVO)) {
				throw new Exception("Essa Configuração Mobile já se encontra ativa.");
			}
			ConfiguracaoMobile.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
//			if (!verificarExistenciaConfiguracaoAtiva(usuarioVO)) {
				configuracaoMobileVO.setSituacao(SituacaoEnum.ATIVO);
				final String sql = "UPDATE ConfiguracaoMobile" + " SET " + "situacao=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement sqlAlterar = con.prepareStatement(sql);
						sqlAlterar.setString(1, configuracaoMobileVO.getSituacao().name());
						sqlAlterar.setInt(2, configuracaoMobileVO.getCodigo());
						return sqlAlterar;
					}
				});
//			} else {
//				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoMobile_voceJaPossuiUmaConfiguracaoAtiva"));
//			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoInativo(final ConfiguracaoMobileVO configuracaoMobileVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (configuracaoMobileVO.getSituacao().equals(SituacaoEnum.INATIVO)) {
				throw new Exception("Essa Configuração Mobile já se encontra inativa.");
			}
			configuracaoMobileVO.setSituacao(SituacaoEnum.INATIVO);
			ConfiguracaoMobile.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE ConfiguracaoMobile" + " SET " + "situacao=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, configuracaoMobileVO.getSituacao().name());
					sqlAlterar.setInt(2, configuracaoMobileVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoMobileVO consultarConfiguracaoASerUsada(int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sqlStr = "SELECT * FROM ConfiguracaoMobile WHERE situacao = 'ATIVO'";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new ConfiguracaoMobileVO();
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Boolean verificarExistenciaConfiguracaoAtiva(UsuarioVO usuarioLogado) throws Exception {
		String sqlStr = "SELECT codigo FROM ConfiguracaoMobile WHERE situacao = 'ATIVO'";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (rs.next()) {
			return true;
		}
		return false;
	}
	
    public void verificarUnicidadeDePadrao(ConfiguracaoMobileVO configuracaoMobileVO) throws Exception {
        boolean valorSugeridoPeloBD;
        String sqlStr = "select count(*) = 0 from configuracaomobile where padrao = true";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        tabelaResultado.next();
        valorSugeridoPeloBD = tabelaResultado.getBoolean(1);
        if (valorSugeridoPeloBD && !configuracaoMobileVO.getPadrao()) {
            throw new Exception("Não há configuração padrão salva no sistema, selecione a opção (PADRÃO)");
        }
        if (!valorSugeridoPeloBD && configuracaoMobileVO.getPadrao()) {
            sqlStr += " and codigo = " + configuracaoMobileVO.getCodigo().intValue();
            SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);

            resultado.next();
            valorSugeridoPeloBD = resultado.getBoolean(1);
            if (valorSugeridoPeloBD) {
                throw new Exception("O sistema pode ter apenas uma configuração mobile padrão, desmarque a opção (PADRAO)");
            }
        }
    }
    
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoMobileVO consultarConfiguracaoPadrao(int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sqlStr = "SELECT * FROM ConfiguracaoMobile WHERE padrao = true";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new ConfiguracaoMobileVO();
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConfiguracaoMobileVO> consultarPorNomeAtivo(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sqlStr = "SELECT * FROM ConfiguracaoMobile WHERE upper(nome) like('" + valorConsulta.toUpperCase() + "%') AND situacao = 'ATIVO' ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr), nivelMontarDados, usuarioLogado));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConfiguracaoMobileVO consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select configuracaomobile.* from configuracaomobile ");
		sqlStr.append("inner join unidadeensino on unidadeensino.configuracaomobile = configuracaomobile.codigo ");
		sqlStr.append("where unidadeensino.codigo = ").append(codigoUnidadeEnsino);
		sqlStr.append(" and situacao = 'ATIVO'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new ConfiguracaoMobileVO();
	}
}
