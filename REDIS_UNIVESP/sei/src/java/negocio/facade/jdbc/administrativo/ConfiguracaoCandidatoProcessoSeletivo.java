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

import negocio.comuns.administrativo.ConfiguracaoAtualizacaoCadastralVO;
import negocio.comuns.administrativo.ConfiguracaoCandidatoProcessoSeletivoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ConfiguracaoCandidatoProcessoSeletivoInterfaceFacade;

/**
 * 
 * Classe de persistência e manipulação dos dados da classe
 * <code>ConfiguracaoCandidatoProcessoSeletivoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ConfiguracaoCandidatoProcessoSeletivoVO</code>.
 * 
 * @see ConfiguracaoAtualizacaoCadastralVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoCandidatoProcessoSeletivo extends ControleAcesso implements ConfiguracaoCandidatoProcessoSeletivoInterfaceFacade {

	private static final long serialVersionUID = 2669139070835145310L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		return ConfiguracaoCandidatoProcessoSeletivo.idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ConfiguracaoCandidatoProcessoSeletivo.idEntidade = idEntidade;
	}

	public ConfiguracaoCandidatoProcessoSeletivo() throws Exception {
		super();
		setIdEntidade("ConfiguracaoCandidatoProcessoSeletivo");
	}

	public ConfiguracaoCandidatoProcessoSeletivoVO novo() throws Exception {
		ConfiguracaoCandidatoProcessoSeletivoVO obj = new ConfiguracaoCandidatoProcessoSeletivoVO();
		return obj;
	}

	/**
	 * Implementação do método incluir, presente na InterfaceFacade referente. O
	 * método insere um Value Object
	 * <code>ConfiguracaoCandidatoProcessoSeletivoVO</code> no banco de dados
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoCandidatoProcessoSeletivoVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		try {
			final String sqlInserir = "INSERT INTO ConfiguracaoCandidatoProcessoSeletivo (configuracaoGeralSistema, apresentarCampoEndereco, enderecoObrigatorio, apresentarCamposTelefonicos, telefoneResidencialObrigatorio, "
					+ "telefoneComercialObrigatorio, telefoneRecadoObrigatorio, telefoneCelularObrigatorio, apresentarCampoEmail, emailObrigatorio, apresentarCampoDataNascimento, "
					+ "dataNascimentoObrigatorio, apresentarCampoNaturalidade, naturalidadeObrigatorio, apresentarCampoNacionalidade, nacionalidadeObrigatorio, apresentarCampoSexo, "
					+ "sexoObrigatorio, apresentarCampoCorRaca, corRacaObrigatorio, apresentarCampoEstadoCivil, estadoCivilObrigatorio, apresentarCampoCasosEspeciais, apresentarRg, "
					+ "rgObrigatorio, apresentarCampoRegistroMilitar, apresentarCampoTituloEleitoral, apresentarCampoFormacaoEnsinoMedio, formacaoEnsinoMedioObrigatorio, apresentarCampoFiliacao, maeFiliacaoObrigatorio,"
					+ "apresentarCampoCertidaoNascimento,certidaoNascimentoObrigatorio, apresentarCampoPaiFiliacao ,apresentarTelefoneResidencial,apresentarTelefoneComercial ,apresentarTelefoneRecado,apresentarTelefoneCelular,apresentarNomeBatismo ,"
					+ " nomeBatismoObrigatorio, apresentarCampoEnderecoSetor, apresentarCampoEnderecoNumero,apresentarCampoEnderecoComplemento,apresentarCampoEnderecoCidade) "
					+ "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement psInserir = con.prepareStatement(sqlInserir);
					int i = 1;
					psInserir.setInt(i++, obj.getConfiguracaoGeralSistema());
					psInserir.setBoolean(i++, obj.getApresentarCampoEndereco());
					psInserir.setBoolean(i++, obj.getEnderecoObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCamposTelefonicos());
					psInserir.setBoolean(i++, obj.getTelefoneResidencialObrigatorio());
					psInserir.setBoolean(i++, obj.getTelefoneComercialObrigatorio());
					psInserir.setBoolean(i++, obj.getTelefoneRecadoObrigatorio());
					psInserir.setBoolean(i++, obj.getTelefoneCelularObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoEmail());
					psInserir.setBoolean(i++, obj.getEmailObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoDataNascimento());
					psInserir.setBoolean(i++, obj.getDataNascimentoObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoNaturalidade());
					psInserir.setBoolean(i++, obj.getNaturalidadeObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoNacionalidade());
					psInserir.setBoolean(i++, obj.getNacionalidadeObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoSexo());
					psInserir.setBoolean(i++, obj.getSexoObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoCorRaca());
					psInserir.setBoolean(i++, obj.getCorRacaObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoEstadoCivil());
					psInserir.setBoolean(i++, obj.getEstadoCivilObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoCasosEspeciais());
					psInserir.setBoolean(i++, obj.getApresentarRg());
					psInserir.setBoolean(i++, obj.getRgObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoRegistroMilitar());
					psInserir.setBoolean(i++, obj.getApresentarCampoTituloEleitoral());
					psInserir.setBoolean(i++, obj.getApresentarCampoFormacaoEnsinoMedio());
					psInserir.setBoolean(i++, obj.getFormacaoEnsinoMedioObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoFiliacao());
					psInserir.setBoolean(i++, obj.getMaeFiliacaoObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoCertidaoNascimento());
					psInserir.setBoolean(i++, obj.getCertidaoNascimentoObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoPaiFiliacao());
					psInserir.setBoolean(i++, obj.getApresentarTelefoneResidencial());
					psInserir.setBoolean(i++, obj.getApresentarTelefoneComercial());
					psInserir.setBoolean(i++, obj.getApresentarTelefoneRecado());
					psInserir.setBoolean(i++, obj.getApresentarTelefoneCelular());
					psInserir.setBoolean(i++, obj.getApresentarNomeBatismo());
					psInserir.setBoolean(i++, obj.getNomeBatismoObrigatorio());
					psInserir.setBoolean(i++, obj.getApresentarCampoEnderecoSetor());
					psInserir.setBoolean(i++, obj.getApresentarCampoEnderecoNumero());
					psInserir.setBoolean(i++, obj.getApresentarCampoEnderecoComplemento());
					psInserir.setBoolean(i++, obj.getApresentarCampoEnderecoCidade());
					return psInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * Implementação do método alterar, presente na InterfaceFacade referente. O
	 * método edita um Value Object
	 * <code>ConfiguracaoCandidatoProcessoSeletivoVO</code> no banco de dados
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoCandidatoProcessoSeletivoVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		try {
			final String sqlAlterar = "UPDATE ConfiguracaoCandidatoProcessoSeletivo set configuracaoGeralSistema=?, apresentarCampoEndereco=?, enderecoObrigatorio=?, apresentarCamposTelefonicos=?, telefoneResidencialObrigatorio=?, "
					+ " telefoneComercialObrigatorio=?, telefoneRecadoObrigatorio=?, telefoneCelularObrigatorio=?, apresentarCampoEmail=?, emailObrigatorio=?, apresentarCampoDataNascimento=?, "
					+ " dataNascimentoObrigatorio=?, apresentarCampoNaturalidade=?, naturalidadeObrigatorio=?, apresentarCampoNacionalidade=?, nacionalidadeObrigatorio=?, apresentarCampoSexo=?, "
					+ " sexoObrigatorio=?, apresentarCampoCorRaca=?, corRacaObrigatorio=?, apresentarCampoEstadoCivil=?, estadoCivilObrigatorio=?, apresentarCampoCasosEspeciais=?, apresentarRg=?, "
					+ " rgObrigatorio=?, apresentarCampoRegistroMilitar=?, apresentarCampoTituloEleitoral=?, apresentarCampoFormacaoEnsinoMedio=?, formacaoEnsinoMedioObrigatorio=?, apresentarCampoFiliacao=?, maeFiliacaoObrigatorio=? ,"
					+  "apresentarCampoCertidaoNascimento= ? ,certidaoNascimentoObrigatorio=? , apresentarCampoPaiFiliacao=?  ,apresentarTelefoneResidencial=? ,apresentarTelefoneComercial=? ,apresentarTelefoneRecado=? ,apresentarTelefoneCelular=? ,apresentarNomeBatismo=? ,"
					+ " nomeBatismoObrigatorio=? , apresentarCampoEnderecoSetor=? , apresentarCampoEnderecoNumero=? ,apresentarCampoEnderecoComplemento=? ,apresentarCampoEnderecoCidade= ?  "
					+ " WHERE ((codigo = ?))";
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement psAlterar = con.prepareStatement(sqlAlterar);
					int i = 1;
					psAlterar.setInt(i++, obj.getConfiguracaoGeralSistema());
					psAlterar.setBoolean(i++, obj.getApresentarCampoEndereco());
					psAlterar.setBoolean(i++, obj.getEnderecoObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCamposTelefonicos());
					psAlterar.setBoolean(i++, obj.getTelefoneResidencialObrigatorio());
					psAlterar.setBoolean(i++, obj.getTelefoneComercialObrigatorio());
					psAlterar.setBoolean(i++, obj.getTelefoneRecadoObrigatorio());
					psAlterar.setBoolean(i++, obj.getTelefoneCelularObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoEmail());
					psAlterar.setBoolean(i++, obj.getEmailObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoDataNascimento());
					psAlterar.setBoolean(i++, obj.getDataNascimentoObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoNaturalidade());
					psAlterar.setBoolean(i++, obj.getNaturalidadeObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoNacionalidade());
					psAlterar.setBoolean(i++, obj.getNacionalidadeObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoSexo());
					psAlterar.setBoolean(i++, obj.getSexoObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoCorRaca());
					psAlterar.setBoolean(i++, obj.getCorRacaObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoEstadoCivil());
					psAlterar.setBoolean(i++, obj.getEstadoCivilObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoCasosEspeciais());
					psAlterar.setBoolean(i++, obj.getApresentarRg());
					psAlterar.setBoolean(i++, obj.getRgObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoRegistroMilitar());
					psAlterar.setBoolean(i++, obj.getApresentarCampoTituloEleitoral());
					psAlterar.setBoolean(i++, obj.getApresentarCampoFormacaoEnsinoMedio());
					psAlterar.setBoolean(i++, obj.getFormacaoEnsinoMedioObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoFiliacao());
					psAlterar.setBoolean(i++, obj.getMaeFiliacaoObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoCertidaoNascimento());
					psAlterar.setBoolean(i++, obj.getCertidaoNascimentoObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoPaiFiliacao());
					psAlterar.setBoolean(i++, obj.getApresentarTelefoneResidencial());
					psAlterar.setBoolean(i++, obj.getApresentarTelefoneComercial());
					psAlterar.setBoolean(i++, obj.getApresentarTelefoneRecado());
					psAlterar.setBoolean(i++, obj.getApresentarTelefoneCelular());
					psAlterar.setBoolean(i++, obj.getApresentarNomeBatismo());
					psAlterar.setBoolean(i++, obj.getNomeBatismoObrigatorio());
					psAlterar.setBoolean(i++, obj.getApresentarCampoEnderecoSetor());
					psAlterar.setBoolean(i++, obj.getApresentarCampoEnderecoNumero());
					psAlterar.setBoolean(i++, obj.getApresentarCampoEnderecoComplemento());
					psAlterar.setBoolean(i++, obj.getApresentarCampoEnderecoCidade());
					psAlterar.setInt(i++, obj.getCodigo().intValue());
					return psAlterar;
				}
			}) == 0) {
				incluir(obj, usuario);
				return;
			}
			;
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * Implementação do método excluir, presente na InterfaceFacade referente. O
	 * método excluir um Value Object
	 * <code>ConfiguracaoCandidatoProcessoSeletivoVO</code> no banco de dados
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ConfiguracaoCandidatoProcessoSeletivoVO obj, UsuarioVO usuario) throws Exception {
		try {
			String sqlExcluir = "DELETE FROM ConfiguracaoCandidatoProcessoSeletivo WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sqlExcluir, new Object[] { obj.getCodigo() });
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List resultados = new ArrayList();
		while (tabelaResultado.next()) {
			resultados.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return resultados;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe
	 * <code>ConfiguracaoCandidatoProcessoSeletivoVO</code>.
	 * 
	 * @return O objeto da classe
	 *         <code>ConfiguracaoCandidatoProcessoSeletivoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ConfiguracaoCandidatoProcessoSeletivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoCandidatoProcessoSeletivoVO obj = new ConfiguracaoCandidatoProcessoSeletivoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setConfiguracaoGeralSistema(dadosSQL.getInt("configuracaoGeralSistema"));
		obj.setApresentarCampoCasosEspeciais(dadosSQL.getBoolean("apresentarCampoCasosEspeciais"));
		obj.setApresentarCampoCorRaca(dadosSQL.getBoolean("apresentarCampoCorRaca"));
		obj.setApresentarCampoDataNascimento(dadosSQL.getBoolean("apresentarCampoDataNascimento"));
		obj.setApresentarCampoEmail(dadosSQL.getBoolean("apresentarCampoEmail"));
		obj.setApresentarCampoEndereco(dadosSQL.getBoolean("apresentarCampoEndereco"));
		obj.setApresentarCampoEstadoCivil(dadosSQL.getBoolean("apresentarCampoEstadoCivil"));
		obj.setApresentarCampoFiliacao(dadosSQL.getBoolean("apresentarCampoFiliacao"));
		obj.setApresentarCampoFormacaoEnsinoMedio(dadosSQL.getBoolean("apresentarCampoFormacaoEnsinoMedio"));
		obj.setApresentarCampoNacionalidade(dadosSQL.getBoolean("apresentarCampoNacionalidade"));
		obj.setApresentarCampoNaturalidade(dadosSQL.getBoolean("apresentarCampoNaturalidade"));
		obj.setApresentarCampoRegistroMilitar(dadosSQL.getBoolean("apresentarCampoRegistroMilitar"));
		obj.setApresentarCampoSexo(dadosSQL.getBoolean("apresentarCampoSexo"));
		obj.setApresentarCamposTelefonicos(dadosSQL.getBoolean("apresentarCamposTelefonicos"));
		obj.setApresentarCampoTituloEleitoral(dadosSQL.getBoolean("apresentarCampoTituloEleitoral"));
		obj.setApresentarRg(dadosSQL.getBoolean("apresentarRg"));
		obj.setCorRacaObrigatorio(dadosSQL.getBoolean("corRacaObrigatorio"));
		obj.setDataNascimentoObrigatorio(dadosSQL.getBoolean("dataNascimentoObrigatorio"));
		obj.setEmailObrigatorio(dadosSQL.getBoolean("emailObrigatorio"));
		obj.setEnderecoObrigatorio(dadosSQL.getBoolean("enderecoObrigatorio"));
		obj.setEstadoCivilObrigatorio(dadosSQL.getBoolean("estadoCivilObrigatorio"));
		obj.setFormacaoEnsinoMedioObrigatorio(dadosSQL.getBoolean("formacaoEnsinoMedioObrigatorio"));
		obj.setMaeFiliacaoObrigatorio(dadosSQL.getBoolean("maeFiliacaoObrigatorio"));
		obj.setNacionalidadeObrigatorio(dadosSQL.getBoolean("nacionalidadeObrigatorio"));
		obj.setNaturalidadeObrigatorio(dadosSQL.getBoolean("naturalidadeObrigatorio"));
		obj.setRgObrigatorio(dadosSQL.getBoolean("rgObrigatorio"));
		obj.setSexoObrigatorio(dadosSQL.getBoolean("sexoObrigatorio"));
		obj.setTelefoneCelularObrigatorio(dadosSQL.getBoolean("telefoneCelularObrigatorio"));
		obj.setTelefoneComercialObrigatorio(dadosSQL.getBoolean("telefoneComercialObrigatorio"));
		obj.setTelefoneRecadoObrigatorio(dadosSQL.getBoolean("telefoneRecadoObrigatorio"));
		obj.setTelefoneResidencialObrigatorio(dadosSQL.getBoolean("telefoneResidencialObrigatorio"));
		obj.setApresentarCampoCertidaoNascimento(dadosSQL.getBoolean("apresentarCampoCertidaoNascimento"));
		obj.setCertidaoNascimentoObrigatorio(dadosSQL.getBoolean("certidaoNascimentoObrigatorio"));
		obj.setApresentarCampoPaiFiliacao(dadosSQL.getBoolean("apresentarCampoPaiFiliacao"));	
		obj.setApresentarTelefoneResidencial(dadosSQL.getBoolean("apresentarTelefoneResidencial"));
		obj.setApresentarTelefoneComercial(dadosSQL.getBoolean("apresentarTelefoneComercial"));
		obj.setApresentarTelefoneRecado(dadosSQL.getBoolean("apresentarTelefoneRecado"));
		obj.setApresentarTelefoneCelular(dadosSQL.getBoolean("apresentarTelefoneCelular"));
		obj.setApresentarNomeBatismo(dadosSQL.getBoolean("apresentarNomeBatismo"));
		obj.setNomeBatismoObrigatorio(dadosSQL.getBoolean("nomeBatismoObrigatorio"));
		obj.setApresentarCampoEnderecoSetor(dadosSQL.getBoolean("apresentarCampoEnderecoSetor"));
		obj.setApresentarCampoEnderecoNumero(dadosSQL.getBoolean("apresentarCampoEnderecoNumero"));
		obj.setApresentarCampoEnderecoComplemento(dadosSQL.getBoolean("apresentarCampoEnderecoComplemento"));
		obj.setApresentarCampoEnderecoCidade(dadosSQL.getBoolean("apresentarCampoEnderecoCidade"));
		return obj;
	}

	public static void validarDados(ConfiguracaoCandidatoProcessoSeletivoVO obj) throws ConsistirException {
		if (!obj.getApresentarCampoEndereco()) {
			obj.setEnderecoObrigatorio(false);
		}
		if (!obj.getApresentarCamposTelefonicos()) {
			obj.setTelefoneCelularObrigatorio(false);
			obj.setTelefoneComercialObrigatorio(false);
			obj.setTelefoneRecadoObrigatorio(false);
			obj.setTelefoneResidencialObrigatorio(false);
		}
		if(!obj.getApresentarTelefoneRecado()) {
			obj.setTelefoneRecadoObrigatorio(false);
		}
		if(!obj.getApresentarTelefoneComercial()) {
			obj.setTelefoneComercialObrigatorio(false);
		}
		if(!obj.getApresentarTelefoneCelular()) {
			obj.setTelefoneCelularObrigatorio(false);
		}
		if(!obj.getApresentarTelefoneResidencial() ) {
			obj.setTelefoneResidencialObrigatorio(false);
		}
		if (!obj.getApresentarCampoEmail()) {
			obj.setEmailObrigatorio(false);
		}
		if (!obj.getApresentarCampoDataNascimento()) {
			obj.setDataNascimentoObrigatorio(false);
		}
		if (!obj.getApresentarCampoNaturalidade()) {
			obj.setNaturalidadeObrigatorio(false);
		}
		if (!obj.getApresentarCampoNacionalidade()) {
			obj.setNacionalidadeObrigatorio(false);
		}
		if (!obj.getApresentarCampoSexo()) {
			obj.setSexoObrigatorio(false);
		}
		if (!obj.getApresentarCampoCorRaca()) {
			obj.setCorRacaObrigatorio(false);
		}
		if (!obj.getApresentarCampoEstadoCivil()) {
			obj.setEstadoCivilObrigatorio(false);
		}
		if (!obj.getApresentarRg()) {
			obj.setRgObrigatorio(false);
		}
		if (!obj.getApresentarCampoFormacaoEnsinoMedio()) {
			obj.setFormacaoEnsinoMedioObrigatorio(false);
		}
		if (!obj.getApresentarCampoFiliacao()) {
			obj.setMaeFiliacaoObrigatorio(false);
		}
		if (!obj.getApresentarCampoCertidaoNascimento()) {
			obj.setCertidaoNascimentoObrigatorio(false);
		}
		if (!obj.getApresentarNomeBatismo()) {
			obj.setNomeBatismoObrigatorio(false);
		}
	}

	public ConfiguracaoCandidatoProcessoSeletivoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlConsulta = "SELECT * FROM  ConfiguracaoCandidatoProcessoSeletivo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlConsulta,
				new Object[] { codigo.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados (ConfiguracaoCandidatoProcessoSeletivoVO) não encontrados");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlConsulta = "SELECT * FROM 	ConfiguracaoCandidatoProcessoSeletivo WHERE codigo >= "
				+ valorConsulta.intValue() + "ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlConsulta);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ConfiguracaoCandidatoProcessoSeletivoVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
			throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
		}
		return new ArrayList(0);
	}

	public ConfiguracaoCandidatoProcessoSeletivoVO consultarPorConfiguracaoGeralSistema(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlConsulta = "SELECT * FROM ConfiguracaoCandidatoProcessoSeletivo WHERE configuracaoGeralSistema = ?";
		SqlRowSet tabelaConsulta = getConexao().getJdbcTemplate().queryForRowSet(sqlConsulta,
				new Object[] { codigoPrm.intValue() });
		if (!tabelaConsulta.next()) {
			return new ConfiguracaoCandidatoProcessoSeletivoVO();
		}
		return (montarDados(tabelaConsulta, nivelMontarDados, usuario));
	}

}
