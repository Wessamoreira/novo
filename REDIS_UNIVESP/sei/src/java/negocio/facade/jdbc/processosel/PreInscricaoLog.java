package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.processosel.PreInscricaoLogVO;
import negocio.comuns.processosel.PreInscricaoVO;
import negocio.comuns.processosel.enumeradores.SituacaoLogPreInscricaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.PreInscricaoLogInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PreInscricaoLogVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>PreInscricaoLogVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see PreInscricaoLogVO
 */
@Repository
@Scope("singleton")
@Lazy
public class PreInscricaoLog extends ControleAcesso implements PreInscricaoLogInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3145433529399174086L;
	protected static String idEntidade;

	public PreInscricaoLog() throws Exception {
		super();
		setIdEntidade("PreInscricaoLog");
	}

	public void setIdEntidade(String idEntidade) {
		PreInscricaoLog.idEntidade = idEntidade;
	}
	
	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public StringBuilder getSqlPadraoConsulta(){
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append(" preinscricaolog.*, ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", ");
		sql.append(" curso.codigo as \"curso.codigo\", ");
		sql.append(" turno.codigo as \"turno.codigo\", ");
		sql.append(" cidpropect.codigo as \"cidpropect.codigo\", ");
		sql.append(" natpropect.codigo as \"natpropect.codigo\", ");
		sql.append(" prospects.codigo as \"prospect.codigo\" ");
		sql.append(" from preinscricaolog as preinscricaolog ");
		sql.append(" left join unidadeensino as unidadeensino on unidadeensino.codigo = preinscricaolog.unidadeensino ");
		sql.append(" left join curso as curso on curso.codigo = preinscricaolog.curso ");
		sql.append(" left join turno as turno on turno.codigo = preinscricaolog.turno ");
		sql.append(" left join cidade as cidpropect on cidpropect.codigo = preinscricaolog.cidadeprospect ");
		sql.append(" left join cidade as natpropect on natpropect.codigo = preinscricaolog.naturalidadeprospect ");
		sql.append(" left join prospects  as prospects on prospects.codigo = preinscricaolog.prospect ");
		return sql;
	}
	
	public StringBuilder getSqlPadraoConsultaTotalRegistro(){
		StringBuilder sql = new StringBuilder();
		sql.append(" select ");
		sql.append(" count (preinscricaolog.codigo) ");
		sql.append(" from preinscricaolog as preinscricaolog ");
		sql.append(" left join unidadeensino as unidadeensino on unidadeensino.codigo = preinscricaolog.unidadeensino ");
		sql.append(" left join curso as curso on curso.codigo = preinscricaolog.curso ");
		sql.append(" left join turno as turno on turno.codigo = preinscricaolog.turno ");
		sql.append(" left join cidade as cidpropect on cidpropect.codigo = preinscricaolog.cidadeprospect ");
		sql.append(" left join cidade as natpropect on natpropect.codigo = preinscricaolog.naturalidadeprospect ");
		sql.append(" left join prospects  as prospects on prospects.codigo = preinscricaolog.prospect ");
		return sql;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void incluir(final PreInscricaoLogVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO preinscricaolog( unidadeensino, curso, turno, dataCadastroPreMatricula, escolaridade, cursoGraduado, situacaoCursoGraduacao, nomeProspect, emailProspect, celularProspect, " 
					+ " cpfProspect, rgProspect, emissorrgProspect, estadoemissorgProspect, estadocivilProspect, dataexprgProspect, datanascProspect, sexoProspect, cepProspect, enderecoProspect, setorProspect, cidadeProspect, " 
					+ " naturalidadeProspect, telResidencialProspect, mensagemErro, prospect, situacaoLogPreInscricao ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) {
							sqlInserir.setInt(1, obj.getUnidadeEnsino());
						} else {
							sqlInserir.setNull(1, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getCurso())) {
							sqlInserir.setInt(2, obj.getCurso());
						} else if (Uteis.isAtributoPreenchido(obj.getCursoVO().getCodigo())) {
							sqlInserir.setInt(2, obj.getCursoVO().getCodigo());
						} else {
							sqlInserir.setNull(2, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getTurno())) {
							sqlInserir.setInt(3, obj.getTurno());
						} else {
							sqlInserir.setNull(3, 0);
						}
						sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataCadastroPreMatricula()));
						sqlInserir.setString(5, obj.getEscolaridade());
						sqlInserir.setString(6, obj.getCursoGraduado());
						sqlInserir.setString(7, obj.getSituacaoCursoGraduacao());
						sqlInserir.setString(8, obj.getNomeProspect());
						sqlInserir.setString(9, obj.getEmailProspect());
						sqlInserir.setString(10, obj.getCelularProspect());
						sqlInserir.setString(11, obj.getCpfProspect());
						sqlInserir.setString(12, obj.getRgProspect());
						sqlInserir.setString(13, obj.getEmissorrgProspect());
						sqlInserir.setString(14, obj.getEstadoemissorgProspect());
						sqlInserir.setString(15, obj.getEstadocivilProspect());
						sqlInserir.setDate(16, Uteis.getDataJDBC(obj.getDataexprgProspect()));
						sqlInserir.setDate(17, Uteis.getDataJDBC(obj.getDatanascProspect()));
						sqlInserir.setString(18, obj.getSexoProspect());
						sqlInserir.setString(19, obj.getCepProspect());
						sqlInserir.setString(20, obj.getEnderecoProspect());
						sqlInserir.setString(21, obj.getSetorProspect());
						if (Uteis.isAtributoPreenchido(obj.getCidadeProspect())) {
							sqlInserir.setInt(22, obj.getCidadeProspect());
						} else {
							sqlInserir.setNull(22, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getNaturalidadeProspect())) {
							sqlInserir.setInt(23, obj.getNaturalidadeProspect());
						} else {
							sqlInserir.setNull(23, 0);
						}
						sqlInserir.setString(24, obj.getTelResidencialProspect());
						sqlInserir.setString(25, obj.getMensagemErro());
						if (Uteis.isAtributoPreenchido(obj.getProspectsVO().getCodigo())) {
							sqlInserir.setInt(26, obj.getProspectsVO().getCodigo());
						}else {
							sqlInserir.setNull(26, 0);
						}
						sqlInserir.setString(27, obj.getSituacaoLogPreInscricao().toString());

					} catch (Exception x) {
						return null;
					}
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
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public List consultarPorPeriodo(Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsulta());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" ORDER BY codigo ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	@Override
	public List consultarPorUnidadeEnsino(Integer unidadeEnsino, Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsulta());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" and preinscricaolog.unidadeensino = ").append(unidadeEnsino);
		if (situacaoLogPreInscricao != null) {
			sql.append(" and preinscricaolog.situacaologpreinscricao = '").append(situacaoLogPreInscricao.toString()).append("' ");
		}
		sql.append(" ORDER BY codigo ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	@Override
	public List consultarPorCodigoCurso(Integer curso, Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsulta());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" and curso.codigo = ").append(curso);
		if (situacaoLogPreInscricao != null) {
			sql.append(" and preinscricaolog.situacaologpreinscricao = '").append(situacaoLogPreInscricao.toString()).append("' ");
		}
		sql.append(" ORDER BY codigo ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}
	
	@Override
	public List consultarPorNomeCurso(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsulta());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" and curso.nome ilike ('%").append(valorConsulta).append("%') ");
		if (situacaoLogPreInscricao != null) {
			sql.append(" and preinscricaolog.situacaologpreinscricao = '").append(situacaoLogPreInscricao.toString()).append("' ");
		}
		sql.append(" ORDER BY codigo ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	@Override
	public List consultarPorEmail(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsulta());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" and emailProspect ilike ('%").append(valorConsulta).append("%') ");
		if (situacaoLogPreInscricao != null) {
			sql.append(" and preinscricaolog.situacaologpreinscricao = '").append(situacaoLogPreInscricao.toString()).append("' ");
		}
		sql.append(" ORDER BY codigo ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	@Override
	public List consultarPorNomeProspect(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsulta());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" and nomeProspect ilike ('%").append(valorConsulta).append("%') ");
		if (situacaoLogPreInscricao != null) {
			sql.append(" and preinscricaolog.situacaologpreinscricao = '").append(situacaoLogPreInscricao.toString()).append("' ");
		}
		sql.append(" ORDER BY codigo ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}
	
	//------------
	// Bloco de metodos para Retorno de Totais
	//------------
	
	@Override
	public Integer consultarTotalRegistrosPorPeriodo(Date dataInicial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsultaTotalRegistro());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		} else {
			return 0;
		}
	}

	@Override
	public Integer consultarTotalRegistrosPorUnidadeEnsino(Integer unidadeEnsino, Date dataInicial, Date dataFinal, boolean controlarAcesso, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsultaTotalRegistro());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" and preinscricaolog.unidadeensino = ").append(unidadeEnsino);
		if (situacaoLogPreInscricao != null) {
			sql.append(" and preinscricaolog.situacaologpreinscricao = '").append(situacaoLogPreInscricao.toString()).append("' ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		} else {
			return 0;
		}
	}

	@Override
	public Integer consultarTotalRegistrosPorCodigoCurso(Integer curso, Date dataInicial, Date dataFinal, boolean controlarAcesso, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsultaTotalRegistro());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" and curso.codigo = ").append(curso);
		if (situacaoLogPreInscricao != null) {
			sql.append(" and preinscricaolog.situacaologpreinscricao = '").append(situacaoLogPreInscricao.toString()).append("' ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		} else {
			return 0;
		}
	}
	
	@Override
	public Integer consultarTotalRegistrosNomeCurso(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsultaTotalRegistro());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" and curso.nome ilike ('%").append(valorConsulta).append("%') ");
		if (situacaoLogPreInscricao != null) {
			sql.append(" and preinscricaolog.situacaologpreinscricao = '").append(situacaoLogPreInscricao.toString()).append("' ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		} else {
			return 0;
		}
	}

	@Override
	public Integer consultarTotalRegistrosPorEmail(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsultaTotalRegistro());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" and emailProspect ilike ('%").append(valorConsulta).append("%') ");
		if (situacaoLogPreInscricao != null) {
			sql.append(" and preinscricaolog.situacaologpreinscricao = '").append(situacaoLogPreInscricao.toString()).append("' ");
		}
		
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		} else {
			return 0;
		}
	}

	@Override
	public Integer consultarTotalRegistrosPorNomeProspect(String valorConsulta, Date dataInicial, Date dataFinal, boolean controlarAcesso, SituacaoLogPreInscricaoEnum situacaoLogPreInscricao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPadraoConsultaTotalRegistro());
		sql.append(" WHERE dataCadastroPreMatricula between '").append(Uteis.getDataJDBCTimestamp(dataInicial)).append("' and '").append(Uteis.getDataJDBCTimestamp(dataFinal)).append(" ' ");
		sql.append(" and nomeProspect ilike ('%").append(valorConsulta).append("%') ");
		if (situacaoLogPreInscricao != null) {
			sql.append(" and preinscricaolog.situacaologpreinscricao = '").append(situacaoLogPreInscricao.toString()).append("' ");
		}
		
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		} else {
			return 0;
		}
	}
	
	//-----------
	// Fim do Bloco
	//-----------

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return vetResultado;
	}

	public static PreInscricaoLogVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PreInscricaoLogVO obj = new PreInscricaoLogVO();

		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setUnidadeEnsino(dadosSQL.getInt("unidadeensino"));
		obj.setCurso(dadosSQL.getInt("curso"));
		obj.setTurno(dadosSQL.getInt("turno"));
		obj.setPropect(dadosSQL.getInt("prospect"));
		obj.setDataCadastroPreMatricula(dadosSQL.getDate("datacadastroprematricula"));
		obj.setEscolaridade(dadosSQL.getString("escolaridade"));
		obj.setCursoGraduado(dadosSQL.getString("cursoGraduado"));
		obj.setSituacaoCursoGraduacao(dadosSQL.getString("situacaocursograduacao"));
		obj.setNomeProspect(dadosSQL.getString("nomeprospect"));
		obj.setEmailProspect(dadosSQL.getString("emailprospect"));
		obj.setCelularProspect(dadosSQL.getString("celularprospect"));
		obj.setCpfProspect(dadosSQL.getString("cpfprospect"));
		obj.setRgProspect(dadosSQL.getString("rgprospect"));
		obj.setEmissorrgProspect(dadosSQL.getString("emissorrgprospect"));
		obj.setEstadoemissorgProspect(dadosSQL.getString("estadoemissorgprospect"));
		obj.setEstadocivilProspect(dadosSQL.getString("estadocivilprospect"));
		obj.setDataexprgProspect(dadosSQL.getDate("dataexprgprospect"));
		obj.setDatanascProspect(dadosSQL.getDate("datanascprospect"));
		obj.setSexoProspect(dadosSQL.getString("sexoprospect"));
		obj.setCepProspect(dadosSQL.getString("cepprospect"));
		obj.setEnderecoProspect(dadosSQL.getString("enderecoprospect"));
		obj.setSetorProspect(dadosSQL.getString("setorprospect"));
		obj.setCidadeProspect(dadosSQL.getInt("cidadeprospect"));
		obj.setNaturalidadeProspect(dadosSQL.getInt("naturalidadeprospect"));
		obj.setTelResidencialProspect(dadosSQL.getString("telresidencialprospect"));
		obj.setMensagemErro(dadosSQL.getString("mensagemerro"));
//		obj.setFalhaPreInscricao(dadosSQL.getBoolean("falhapreinscricao"));
		if (dadosSQL.getString("situacaoLogPreInscricao") != null) {
			obj.setSituacaoLogPreInscricao(SituacaoLogPreInscricaoEnum.valueOf(dadosSQL.getString("situacaoLogPreInscricao")));
		}

		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("unidadeensino.codigo"))) {
			obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino(), false,  Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("curso.codigo"))) {
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("turno.codigo"))) {
			obj.setTurnoVO(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno(),  Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("cidpropect.codigo"))) {
			obj.setCidadeProspectVO(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidadeProspect(), false, usuario));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("natpropect.codigo"))) {
			obj.setNaturalidadeProspectVO(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getNaturalidadeProspect(), false, usuario));
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("prospect.codigo"))){
			obj.setProspectsVO(getFacadeFactory().getProspectsFacade().consultarPorChavePrimaria(obj.getPropect(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}

		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarPreInscricaoComFalhaCadastro(PreInscricaoLogVO preInscricaoLogVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		PreInscricaoVO pre = new PreInscricaoVO();
		pre.setUnidadeEnsino(preInscricaoLogVO.getUnidadeEnsinoVO());
        pre.setCurso(preInscricaoLogVO.getCursoVO());
        pre.setData(new Date());
        pre.setProspect(preInscricaoLogVO.getProspectsVO());
//        pre.getProspect().getPessoa().setCodigo(preInscricaoLogVO.getPessoaVO().getCodigo());
        pre.getProspect().setTipoProspect(TipoProspectEnum.FISICO);
        pre.getProspect().setCEP(preInscricaoLogVO.getCepProspect());
        pre.getProspect().setUnidadeEnsino(preInscricaoLogVO.getUnidadeEnsinoVO());
        pre.getProspect().setCpf(preInscricaoLogVO.getCpfProspect());
        pre.getProspect().setCelular(preInscricaoLogVO.getCelularProspect());
        pre.getProspect().setTelefoneResidencial(preInscricaoLogVO.getTelResidencialProspect());
//        pre.getProspect().setTelefoneComercial(preInscricaoLogVO.getTelefoneComer());
        pre.getProspect().setNaturalidade(preInscricaoLogVO.getNaturalidadeProspectVO());
        pre.getProspect().setEstadoCivil(preInscricaoLogVO.getEstadocivilProspect());
        pre.getProspect().setNome(preInscricaoLogVO.getNomeProspect());
        pre.getProspect().setEmailPrincipal(preInscricaoLogVO.getEmailProspect());
        pre.getProspect().setSexo(preInscricaoLogVO.getSexoProspect());
        pre.getProspect().setRg(preInscricaoLogVO.getRgProspect());
        pre.getProspect().setDataExpedicao(preInscricaoLogVO.getDataexprgProspect());
        pre.getProspect().setOrgaoEmissor(preInscricaoLogVO.getEmissorrgProspect());
        pre.getProspect().setEstadoEmissor(preInscricaoLogVO.getEstadoemissorgProspect());
        pre.getProspect().setSetor(preInscricaoLogVO.getSetorProspect());
        pre.getProspect().setEndereco(preInscricaoLogVO.getEnderecoProspect());
        pre.getProspect().setDataNascimento(preInscricaoLogVO.getDatanascProspect());
        pre.getProspect().setCidade(preInscricaoLogVO.getCidadeProspectVO());
        pre.getProspect().setCursoInteresseVOs(getFacadeFactory().getCursoInteresseFacade().consultarCursosPorCodigoProspect(pre.getProspect().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        pre.getProspect().setFormacaoAcademicaVOs(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorCodigoProspect(pre.getProspect().getCodigo(), false, usuarioVO));
        pre.setReenvioPreInscricao(true);
        CursoInteresseVO cursoInt = new CursoInteresseVO();
        cursoInt.setCurso(preInscricaoLogVO.getCursoVO());
        cursoInt.setDataCadastro(new Date());
        preInscricaoLogVO.setSituacaoLogPreInscricao(SituacaoLogPreInscricaoEnum.REENVIADO);
        
        getFacadeFactory().getPreInscricaoFacade().incluirPreInscricaoAPartirSiteOuHomePreInscricao(pre, configuracaoGeralSistemaVO);
        alterarSituacaoLogPreInscricao(preInscricaoLogVO, usuarioVO);
             
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoLogPreInscricao(final PreInscricaoLogVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE preInscricaoLog set situacaoLogPreInscricao=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSituacaoLogPreInscricao().toString());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarPreInscricaoComFalhaSelecionadas(List listaPreInscricaoLogVOs, HashMap<Integer, PreInscricaoLogVO> mapLogPreInscricaoVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		for (PreInscricaoLogVO preInscricaoLogVO : (List<PreInscricaoLogVO>) listaPreInscricaoLogVOs) {
			if (mapLogPreInscricaoVOs.containsKey(preInscricaoLogVO.getCodigo())) {
				gravarPreInscricaoComFalhaCadastro(preInscricaoLogVO, configuracaoGeralSistemaVO, usuarioVO);
			}
		}
	}

	
	@Override
	public PreInscricaoLogVO montarDados(PreInscricaoVO preInscricaoVO) {
		
		if(preInscricaoVO == null)
			return new PreInscricaoLogVO();
		
		PreInscricaoLogVO preInscricaoLogVO = new PreInscricaoLogVO();
        preInscricaoLogVO.setUnidadeEnsino(preInscricaoVO.getUnidadeEnsino().getCodigo());
        preInscricaoLogVO.setNomeProspect(preInscricaoVO.getNome());
        preInscricaoLogVO.setEmailProspect(preInscricaoVO.getEmail());
        preInscricaoLogVO.setCelularProspect(preInscricaoVO.getCelular());
        preInscricaoLogVO.setCidadeProspect(preInscricaoVO.getUnidadeEnsino().getCidade().getCodigo());
        preInscricaoLogVO.setTelResidencialProspect(preInscricaoVO.getTelefoneResidencial());
        preInscricaoLogVO.setCursoVO(preInscricaoVO.getCurso());
        
        preInscricaoLogVO.setProspectsVO(preInscricaoLogVO.getProspectsVO());
        
        return preInscricaoLogVO;
	}
}