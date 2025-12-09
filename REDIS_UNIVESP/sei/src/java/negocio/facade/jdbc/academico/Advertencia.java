package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import negocio.comuns.academico.AdvertenciaVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.VariaveisNotaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.AdvertenciaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Advertencia extends ControleAcesso implements AdvertenciaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public Advertencia() throws Exception {
		super();
		setIdEntidade("Advertencia");
	}

	public static void validarDados(AdvertenciaVO obj) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getMatriculaVO().getMatricula())) {
			throw new Exception("O campo MATRICULA (Advertência) deve ser informado.");
		}
//		if (!Uteis.isAtributoPreenchido(obj.getProfessor())) {
//			throw new Exception("O campo PROFESSOR (Advertência) deve ser informado.");
//		}
		if (!Uteis.isAtributoPreenchido(obj.getDataAdvertencia())) {
			throw new Exception("O campo DATA ADVERTÊNCIA (Advertência) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTipoAdvertenciaVO().getCodigo())) {
			throw new Exception("O campo TIPO ADVERTÊNCIA (Advertência) deve ser informado.");
		}
		if (obj.getMatriculaVO().getCurso().getPeriodicidade().equals("AN")) {
			if (!Uteis.isAtributoPreenchido(obj.getBimestre())) {
				throw new Exception("O campo BIMESTRE (Advertência) deve ser informado.");
			}
		} else {
			if (!Uteis.isAtributoPreenchido(obj.getSemestre())) {
				throw new Exception("O campo SEMESTRE (Advertência) deve ser informado.");
			}
		}
		if (!Uteis.isAtributoPreenchido(obj.getAdvertencia())) {
			throw new Exception("O campo DESCRIÇÃO (Advertência) deve ser informado.");
		}
		if (obj.getAno().length() < 4) {
			throw new Exception("O campo ANO (Advertência) deve possuir 4 dígitos.");
		}
	}
	
	public AdvertenciaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" SELECT * FROM advertencia WHERE ((codigo = ").append(codigoPrm).append(")) ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (rs.next()) {
				return montarDados(rs, usuarioVO);
			}
			return new AdvertenciaVO(); 
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final AdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getNovoObj()) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			TipoAdvertencia.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO Advertencia (matricula, dataAdvertencia, tipoAdvertencia, bimestre, semestre, advertencia, responsavel, dataCadastro, ano, professor, observacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						sqlInserir.setString(1, obj.getMatriculaVO().getMatricula());
						sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataAdvertencia()));
						sqlInserir.setInt(3, obj.getTipoAdvertenciaVO().getCodigo());
						if(Uteis.isAtributoPreenchido(obj.getBimestre())){
							sqlInserir.setString(4, obj.getBimestre().getValor());
						}else{
							sqlInserir.setNull(4, 0);
						}
						if(Uteis.isAtributoPreenchido(obj.getSemestre())){
							sqlInserir.setString(5, obj.getSemestre());
						}else{
							sqlInserir.setNull(5, 0);
						}
						sqlInserir.setString(6, obj.getAdvertencia());
						sqlInserir.setInt(7, obj.getResponsavel().getCodigo());
						sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataCadastro()));
						sqlInserir.setString(9, obj.getAno());
						if(Uteis.isAtributoPreenchido(obj.getProfessor())){
							sqlInserir.setInt(10, obj.getProfessor().getCodigo());
						}else{
							sqlInserir.setNull(10, 0);
						}
						sqlInserir.setString(11, obj.getObservacao());
					} catch (final Exception x) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			executarEnvioMensagenAdvertencia(obj, usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			TipoAdvertencia.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE Advertencia set matricula = ?, dataAdvertencia = ?, tipoAdvertencia = ?, bimestre = ?, semestre = ?, advertencia = ?, responsavel = ?, dataCadastro = ?, ano = ?, professor = ?, observacao = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getMatriculaVO().getMatricula());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataAdvertencia()));
					sqlAlterar.setInt(3, obj.getTipoAdvertenciaVO().getCodigo());
					if(Uteis.isAtributoPreenchido(obj.getBimestre())){
						sqlAlterar.setString(4, obj.getBimestre().getValor());
					}else{
						sqlAlterar.setNull(4, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getSemestre())){
						sqlAlterar.setString(5, obj.getSemestre());
					}else{
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setString(6, obj.getAdvertencia());
					sqlAlterar.setInt(7, obj.getResponsavel().getCodigo());
					sqlAlterar.setDate(8, Uteis.getDataJDBC(obj.getDataCadastro()));
					sqlAlterar.setString(9, obj.getAno());
					if(Uteis.isAtributoPreenchido(obj.getProfessor())){
						sqlAlterar.setInt(10, obj.getProfessor().getCodigo());
					}else{
						sqlAlterar.setNull(10, 0);
					}
					sqlAlterar.setString(11, obj.getObservacao());
					sqlAlterar.setInt(12, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			executarEnvioMensagenAdvertencia(obj, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AdvertenciaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TipoAdvertencia.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM Advertencia WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<AdvertenciaVO> consultarAdvertenciaPorMatricula(String matricula, String ano, Boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM Advertencia where matricula ilike '").append(matricula).append("' ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append("AND EXTRACT (YEAR FROM dataadvertencia) = '").append(ano).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosBasicos(tabelaResultado, unidadeEnsino, usuarioVO);
	}

	public List<AdvertenciaVO> consultarAdvertenciaVisaoAluno() throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return null;// montarDadosBasicos(tabelaResultado, unidadeEnsino,
					// usuarioVO);
	}

	@Override
	public Map<String, Integer> consultarPorMatriculaNrAdvertenciasPeriodo(String matricula, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		Map<String, Integer> listaComNrAdvertenciasPorBimestre = new HashMap<String, Integer>(0);
		CursoVO curso = getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(matricula, false, usuarioVO);
		String nivelEducacional = curso.getNivelEducacional();
		String periodicidade = curso.getPeriodicidade();

		// inicializado todas as variaveis com zero
		listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.ADVERB1.getValor(), 0);
		listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.ADVERB2.getValor(), 0);
		listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.ADVERB3.getValor(), 0);
		listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.ADVERB4.getValor(), 0);
		listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.ADVERPER.getValor(), 0);

		listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.SUSPENB1.getValor(), 0);
		listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.SUSPENB2.getValor(), 0);
		listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.SUSPENB3.getValor(), 0);
		listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.SUSPENB4.getValor(), 0);
		listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.SUSPENPER.getValor(), 0);

		StringBuilder sqlStr = new StringBuilder();

		if ((nivelEducacional.equals("IN")) || (nivelEducacional.equals("BA")) || (nivelEducacional.equals("ME"))) {
			// TRATANTO ADVERTENCIAS
			// montando advertencias (que nao seja somente orais e que nao sejam
			// de suspensao)
			// com base no bimestre, pois
			// sao cursos bimestrais. Neste caso somente o parametro
			// ano e matricula serao utilizados para filtro.
			sqlStr.append(" SELECT matricula, bimestre, count(*) as qtdAdvertencia FROM Advertencia ");
			sqlStr.append(" INNER JOIN TipoAdvertencia  ON (TipoAdvertencia.codigo = Advertencia.tipoAdvertencia) ");
			sqlStr.append(" WHERE (TipoAdvertencia.gerarSuspensao != true) AND (TipoAdvertencia.advertenciaVerbal != true) ");
			sqlStr.append(" and (matricula = '").append(matricula).append("') ");
			sqlStr.append(" and (extract(year from dataAdvertencia) = ").append(ano).append(")");
			sqlStr.append(" group by matricula, bimestre ");
			sqlStr.append(" order by matricula, bimestre ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			int totalAdvPeriodo = 0;
			while (tabelaResultado.next()) {
				int bimestre = tabelaResultado.getInt("bimetre");
				VariaveisNotaEnum identificadorBismetre = VariaveisNotaEnum.getEnum("ADVERB" + bimestre);
				int qtdAdv = tabelaResultado.getInt("qtdAdvertencia");
				listaComNrAdvertenciasPorBimestre.put(identificadorBismetre.getValor(), qtdAdv);
				totalAdvPeriodo = totalAdvPeriodo + qtdAdv;
			}
			listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.ADVERPER.getValor(), totalAdvPeriodo);

			// TRATANTO SUSPENSOES
			// montando advertencias do tipo suspensão com base no bimestre,
			// pois
			// sao cursos bimestrais. Neste caso somente o parametro
			// ano e matricula serao utilizados para filtro.
			sqlStr = new StringBuilder();
			sqlStr.append(" SELECT matricula, bimestre, count(*) as qtdAdvertencia FROM Advertencia ");
			sqlStr.append(" INNER JOIN TipoAdvertencia ON (TipoAdvertencia.codigo = Advertencia.tipoAdvertencia) ");
			sqlStr.append(" WHERE (TipoAdvertencia.gerarSuspensao = true) ");
			sqlStr.append(" and (matricula = '").append(matricula).append("') ");
			sqlStr.append(" and (extract(year from dataAdvertencia) = ").append(ano).append(")");
			sqlStr.append(" group by matricula, bimestre ");
			sqlStr.append(" order by matricula, bimestre ");
			SqlRowSet tabelaResultadoSuspen = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			int totalSuspensoesPeriodo = 0;
			while (tabelaResultadoSuspen.next()) {
				int bimestre = tabelaResultadoSuspen.getInt("bimetre");
				VariaveisNotaEnum identificadorBismetre = VariaveisNotaEnum.getEnum("SUSPENB" + bimestre);
				int qtdAdv = tabelaResultadoSuspen.getInt("qtdAdvertencia");
				listaComNrAdvertenciasPorBimestre.put(identificadorBismetre.getValor(), qtdAdv);
				totalSuspensoesPeriodo = totalSuspensoesPeriodo + qtdAdv;
			}
			listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.SUSPENPER.getValor(), totalSuspensoesPeriodo);

			return listaComNrAdvertenciasPorBimestre;
		} else {
			// para todos os demais cursos, vamos montar semestre caso a
			// periodicidade
			// seja semestral.
			if (periodicidade.equals("SE")) {
				// TRATANDO ADVERTENCIAS (que nao seja somente orais e que nao
				// sejam de suspensao)
				// montando advertencias com base no semestre, pois
				// sao cursos semestrais (graduacao e graduacao tecnologia.
				// Neste caso somente o parametro
				// ano e matricula serao utilizados para filtro.
				sqlStr.append(" SELECT matricula, semestre, count(*) as qtdAdvertencia FROM Advertencia ");
				sqlStr.append(" INNER JOIN TipoAdvertencia  ON (TipoAdvertencia.codigo = Advertencia.tipoAdvertencia) ");
				sqlStr.append(" WHERE (TipoAdvertencia.gerarSuspensao != true) AND (TipoAdvertencia.advertenciaVerbal != true) ");
				sqlStr.append(" and (matricula = '").append(matricula).append("') ");
				sqlStr.append(" and (extract(year from dataAdvertencia) = ").append(ano).append(")");
				sqlStr.append(" and (semestre = '").append(semestre).append("')");
				sqlStr.append(" group by matricula, semestre ");
				sqlStr.append(" order by matricula, semestre ");
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				int totalAdvPeriodo = 0;
				if (tabelaResultado.next()) {
					totalAdvPeriodo = tabelaResultado.getInt("qtdAdvertencia");
				}
				listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.ADVERPER.getValor(), totalAdvPeriodo);

				// TRATANDO SUSPENSOES (que seja, do tipo suspensão).
				// montando advertencias com base no semestre, pois
				// sao cursos semestrais (graduacao e graduacao tecnologia.
				// Neste caso somente o parametro
				// ano e matricula serao utilizados para filtro.
				sqlStr = new StringBuilder();
				sqlStr.append(" SELECT matricula, semestre, count(*) as qtdAdvertencia FROM Advertencia ");
				sqlStr.append(" INNER JOIN TipoAdvertencia  ON (TipoAdvertencia.codigo = Advertencia.tipoAdvertencia) ");
				sqlStr.append(" WHERE (TipoAdvertencia.gerarSuspensao = true) ");
				sqlStr.append(" and (matricula = '").append(matricula).append("') ");
				sqlStr.append(" and (extract(year from dataAdvertencia) = ").append(ano).append(")");
				sqlStr.append(" and (semestre = '").append(semestre).append("')");
				sqlStr.append(" group by matricula, semestre ");
				sqlStr.append(" order by matricula, semestre ");
				SqlRowSet tabelaResultadoSuspen = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				int totalSuspenPeriodo = 0;
				if (tabelaResultadoSuspen.next()) {
					totalSuspenPeriodo = tabelaResultadoSuspen.getInt("qtdAdvertencia");
				}
				listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.SUSPENPER.getValor(), totalSuspenPeriodo);

				return listaComNrAdvertenciasPorBimestre;
			}

			// caso a periodicidade seja anual, iremos montar por ano
			if (periodicidade.equals("AN")) {
				// TRATANDO ADVERTENCIAS (que nao seja somente orais e que nao
				// sejam de suspensao)
				// montando advertencias com base no ano, pois
				// sao cursos anuais
				sqlStr.append(" SELECT matricula, extract(year from dataAdvertencia) as ano, count(*) as qtdAdvertencia FROM Advertencia ");
				sqlStr.append(" INNER JOIN TipoAdvertencia  ON (TipoAdvertencia.codigo = Advertencia.tipoAdvertencia) ");
				sqlStr.append(" WHERE (TipoAdvertencia.gerarSuspensao != true) AND (TipoAdvertencia.advertenciaVerbal != true) ");
				sqlStr.append(" and (matricula = '").append(matricula).append("') ");
				sqlStr.append(" and (extract(year from dataAdvertencia) = ").append(ano).append(")");
				sqlStr.append(" group by matricula, extract(year from dataAdvertencia) ");
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				int totalAdvPeriodo = 0;
				if (tabelaResultado.next()) {
					totalAdvPeriodo = tabelaResultado.getInt("qtdAdvertencia");
				}
				listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.ADVERPER.getValor(), totalAdvPeriodo);

				// TRATANDO SUSPENSOES (que advertencias de suspensao)
				// montando advertencias com base no ano, pois
				// sao cursos anuais
				sqlStr = new StringBuilder();
				sqlStr.append(" SELECT matricula, extract(year from dataAdvertencia) as ano, count(*) as qtdAdvertencia FROM Advertencia ");
				sqlStr.append(" INNER JOIN TipoAdvertencia  ON (TipoAdvertencia.codigo = Advertencia.tipoAdvertencia) ");
				sqlStr.append(" WHERE (TipoAdvertencia.gerarSuspensao = true) ");
				sqlStr.append(" and (matricula = '").append(matricula).append("') ");
				sqlStr.append(" and (extract(year from dataAdvertencia) = ").append(ano).append(")");
				sqlStr.append(" group by matricula, extract(year from dataAdvertencia) ");
				SqlRowSet tabelaResultadoSuspen = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				int totalSuspenPeriodo = 0;
				if (tabelaResultadoSuspen.next()) {
					totalSuspenPeriodo = tabelaResultadoSuspen.getInt("qtdAdvertencia");
				}
				listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.SUSPENPER.getValor(), totalSuspenPeriodo);

				return listaComNrAdvertenciasPorBimestre;
			}

			// caso a periodicidade seja integral, iremos montar todas as
			// advertencia do
			// aluno no curso
			if (periodicidade.equals("IN")) {
				// TRATANDO ADVERTENCIAS (que nao seja somente orais e que nao
				// sejam de suspensao)
				// montando advertencias com base somente na matricula
				// pois tratam-se de cursos integrais
				sqlStr.append(" SELECT matricula, count(*) as qtdAdvertencia FROM Advertencia ");
				sqlStr.append(" INNER JOIN TipoAdvertencia  ON (TipoAdvertencia.codigo = Advertencia.tipoAdvertencia) ");
				sqlStr.append(" WHERE (TipoAdvertencia.gerarSuspensao != true) AND (TipoAdvertencia.advertenciaVerbal != true) ");
				sqlStr.append(" and (matricula = '").append(matricula).append("') ");
				sqlStr.append(" group by matricula ");
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				int totalAdvPeriodo = 0;
				if (tabelaResultado.next()) {
					totalAdvPeriodo = tabelaResultado.getInt("qtdAdvertencia");
				}
				listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.ADVERPER.getValor(), totalAdvPeriodo);

				// TRATANDO SUSPENSOES (que seja somente de suspensao)
				// montando advertencias com base somente na matricula
				// pois tratam-se de cursos integrais
				sqlStr = new StringBuilder();
				sqlStr.append(" SELECT matricula, count(*) as qtdAdvertencia FROM Advertencia ");
				sqlStr.append(" INNER JOIN TipoAdvertencia  ON (TipoAdvertencia.codigo = Advertencia.tipoAdvertencia) ");
				sqlStr.append(" WHERE (TipoAdvertencia.gerarSuspensao != true) AND (TipoAdvertencia.advertenciaVerbal != true) ");
				sqlStr.append(" and (matricula = '").append(matricula).append("') ");
				sqlStr.append(" group by matricula ");
				SqlRowSet tabelaResultadoSuspen = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				int totalAdvSuspenPeriodo = 0;
				if (tabelaResultadoSuspen.next()) {
					totalAdvSuspenPeriodo = tabelaResultadoSuspen.getInt("qtdAdvertencia");
				}
				listaComNrAdvertenciasPorBimestre.put(VariaveisNotaEnum.SUSPENPER.getValor(), totalAdvSuspenPeriodo);

				return listaComNrAdvertenciasPorBimestre;
			}
		}
		return null;
	}

	@Override
	public List<AdvertenciaVO> consultarAdvertenciaPorNomeAluno(String nome, Boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT Advertencia.* FROM Advertencia ");
		sqlStr.append(" INNER JOIN Matricula on Matricula.matricula = Advertencia.matricula ");
		sqlStr.append(" INNER JOIN Pessoa on Pessoa.codigo = Matricula.aluno ");
		sqlStr.append(" WHERE Pessoa.nome ilike ('%").append(nome).append("%')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosBasicos(tabelaResultado, unidadeEnsino, usuarioVO);
	}

	@Override
	public List<AdvertenciaVO> consultarPorDescricaoTipoAdvertencia(String descricao, Boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT Advertencia.* FROM Advertencia ");
		sqlStr.append(" INNER JOIN TipoAdvertencia on TipoAdvertencia.codigo = Advertencia.tipoAdvertencia ");
		sqlStr.append(" WHERE TipoAdvertencia.descricao ilike ('%").append(descricao).append("%')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosBasicos(tabelaResultado, unidadeEnsino, usuarioVO);
	}

	public List<AdvertenciaVO> montarDadosBasicos(SqlRowSet tabelaResultado, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List<AdvertenciaVO> advertenciaVOs = new ArrayList<AdvertenciaVO>(0);
		while (tabelaResultado.next()) {
			advertenciaVOs.add(montarDados(tabelaResultado, usuarioVO));
		}
		return advertenciaVOs;
	}
	
	public AdvertenciaVO montarDados(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		AdvertenciaVO obj = new AdvertenciaVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
		obj.setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatriculaVO().getMatricula(), null, NivelMontarDados.BASICO, usuarioVO));
		obj.setDataAdvertencia(tabelaResultado.getDate("dataAdvertencia"));
		obj.getTipoAdvertenciaVO().setCodigo(tabelaResultado.getInt("tipoAdvertencia"));
		obj.setTipoAdvertenciaVO(getFacadeFactory().getTipoAdvertenciaFacade().consultarPorCodigo(obj.getTipoAdvertenciaVO().getCodigo(), null, false, usuarioVO, Uteis.NIVELMONTARDADOS_TODOS));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("bimestre"))) {
			obj.setBimestre(BimestreEnum.valueOf(tabelaResultado.getString("bimestre")));
		}
		obj.setSemestre(tabelaResultado.getString("semestre"));
		obj.setAno(tabelaResultado.getString("ano"));
		obj.setAdvertencia(tabelaResultado.getString("advertencia"));
		obj.getProfessor().setCodigo(tabelaResultado.getInt("professor"));
		if (!obj.getProfessor().getCodigo().equals(0)) {
			obj.setProfessor(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getProfessor().getCodigo(), null, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		obj.setObservacao(tabelaResultado.getString("observacao"));
		obj.getResponsavel().setCodigo(tabelaResultado.getInt("responsavel"));
		if (!obj.getResponsavel().getCodigo().equals(0)) {
			obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		obj.setDataCadastro(tabelaResultado.getDate("dataCadastro"));
		return obj;
	}
	
	@Override
	public void executarEnvioMensagenAdvertencia(AdvertenciaVO advertenciaVO, UsuarioVO usuarioVO) throws Exception {
		advertenciaVO.setTipoAdvertenciaVO(getFacadeFactory().getTipoAdvertenciaFacade().consultarNotificacaoEmail(advertenciaVO.getTipoAdvertenciaVO()));
		if (advertenciaVO.getTipoAdvertenciaVO().getEnviarEmail()) {
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagenAdvertenciaResponsavelLegalAluno(advertenciaVO, usuarioVO);
		}
		if (advertenciaVO.getTipoAdvertenciaVO().getVisaoAluno()) {
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagenAdvertenciaAluno(advertenciaVO, usuarioVO);
		}
	}

	@Override
	public List<AdvertenciaVO> consultaAdvertenciaVisaoAluno(String matricula, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT advertencia.*, tipoadvertencia from advertencia");
		sqlStr.append(" INNER JOIN tipoadvertencia ON (tipoadvertencia.codigo = advertencia.tipoadvertencia)");
		sqlStr.append(" WHERE 1 = 1 "); 
		if(usuarioVO != null && usuarioVO.getIsApresentarVisaoAluno()) {
			sqlStr.append(" and tipoadvertencia.visaoaluno = true ");
		}else if(usuarioVO != null && usuarioVO.getIsApresentarVisaoPais()) {
			sqlStr.append(" and tipoadvertencia.visaopais = true ");
		}
		sqlStr.append(" and advertencia.matricula = '").append(matricula).append("' order by advertencia.dataadvertencia desc");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosBasicos(tabelaResultado, unidadeEnsino, usuarioVO);
	}

	@Override
	public List<AdvertenciaVO> consultarAdvertenciaPorTurmaVisaoProfessor(String matricula, String nomeAluno, Integer professorLogado, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct pessoa.nome as aluno, advertencia.* from advertencia");
		sqlStr.append(" inner join matricula on matricula.matricula = advertencia.matricula");
		sqlStr.append(" inner join pessoa on matricula.aluno = pessoa.codigo");
		sqlStr.append(" inner join curso on matricula.curso = curso.codigo");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" and case curso.periodicidade when 'IN' then true");
		sqlStr.append(" when 'AN' then matriculaperiodoturmadisciplina.ano = advertencia.ano");
		sqlStr.append(" when 'SE' then matriculaperiodoturmadisciplina.ano = advertencia.ano");
		sqlStr.append(" and matriculaperiodoturmadisciplina.semestre = advertencia.semestre end");
		sqlStr.append(" inner join horarioturma on horarioturma.turma = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" and case curso.periodicidade when 'IN' then true");
		sqlStr.append(" when 'AN' then horarioturma.anovigente = advertencia.ano");
		sqlStr.append(" when 'SE' then horarioturma.anovigente = advertencia.ano");
		sqlStr.append(" and horarioturma.semestrevigente = advertencia.semestre end");			
		sqlStr.append(" where ");
		sqlStr.append(" matricula.matricula = ? ");
		sqlStr.append(" and  exists (select horarioturmadiaitem.codigo from horarioturmadia inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia where horarioturma.codigo = horarioturmadia.horarioturma and  horarioturmadiaitem.professor = ").append(professorLogado).append(" limit 1) ");
		sqlStr.append(" and  ");
		sqlStr.append(" sem_acentos(pessoa.nome) ilike sem_acentos(?)  order by advertencia.dataadvertencia desc");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula,  "%"+nomeAluno+"%");
		return montarDadosBasicos(tabelaResultado, unidadeEnsino, usuarioVO);
	}
	
	@Override
	public List<AdvertenciaVO> consultarAdvertenciaVisaoCoordenador(String matricula, String nomeAluno, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		if(matricula == null || matricula.trim().isEmpty()){
			throw new Exception("O campo MATRÍCULA deve ser informado.");
		}
		StringBuilder sqlStr = new StringBuilder();		
		sqlStr.append("SELECT distinct pessoa.nome as aluno, advertencia.* from advertencia");
		sqlStr.append(" inner join matricula on matricula.matricula = advertencia.matricula");
		sqlStr.append(" inner join pessoa on matricula.aluno = pessoa.codigo");
		sqlStr.append(" inner join curso on matricula.curso = curso.codigo");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" and case curso.periodicidade when 'IN' then true");
		sqlStr.append(" when 'AN' then matriculaperiodoturmadisciplina.ano = advertencia.ano");
		sqlStr.append(" when 'SE' then matriculaperiodoturmadisciplina.ano = advertencia.ano");
		sqlStr.append(" and matriculaperiodoturmadisciplina.semestre = advertencia.semestre end");
		sqlStr.append(" inner join horarioturma on horarioturma.turma = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" and case curso.periodicidade when 'IN' then true");
		sqlStr.append(" when 'AN' then horarioturma.anovigente = advertencia.ano");
		sqlStr.append(" when 'SE' then horarioturma.anovigente = advertencia.ano");
		sqlStr.append(" and horarioturma.semestrevigente = advertencia.semestre end");
		sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append(" where ");
		sqlStr.append(" matricula.matricula = '" + matricula + "'");
		sqlStr.append(" and  ");
		sqlStr.append(" sem_acentos(pessoa.nome) ilike sem_acentos('%" + nomeAluno + "%')  order by advertencia.dataadvertencia desc");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosBasicos(tabelaResultado, unidadeEnsino, usuarioVO);
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		Advertencia.idEntidade = idEntidade;
	}

}
