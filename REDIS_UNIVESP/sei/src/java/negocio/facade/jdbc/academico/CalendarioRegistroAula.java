package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CalendarioRegistroAulaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CalendarioRegistroAulaInterfaceFacade;

@Repository
@Lazy
public class CalendarioRegistroAula extends ControleAcesso implements CalendarioRegistroAulaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7006155065140841092L;
	protected static String idEntidade;

	public CalendarioRegistroAula() throws Exception {
		super();
		setIdEntidade("CalendarioRegistroAula");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CalendarioRegistroAulaVO calendarioRegistroAulaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(calendarioRegistroAulaVO);
		validarUnicidade(calendarioRegistroAulaVO, usuarioVO);
		if (calendarioRegistroAulaVO.getNovoObj()) {
			incluir(calendarioRegistroAulaVO, validarAcesso, usuarioVO);
		} else {
			alterar(calendarioRegistroAulaVO, validarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CalendarioRegistroAulaVO calendarioRegistroAulaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluir(getIdEntidade(), validarAcesso, usuarioVO);
		getConexao().getJdbcTemplate().update("DELETE FROM CalendarioRegistroAula where codigo = " + calendarioRegistroAulaVO.getCodigo()+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
	}

	@Override
	public void realizarClonagemCalendarioRegistroAula(CalendarioRegistroAulaVO calendarioRegistroAulaVO, UsuarioVO usuarioVO) throws Exception {
		CalendarioRegistroAulaVO obj = (CalendarioRegistroAulaVO) calendarioRegistroAulaVO.clone();
		calendarioRegistroAulaVO.setCodigo(0);
		calendarioRegistroAulaVO.setNovoObj(true);
		calendarioRegistroAulaVO = obj;
	}

	private void validarUnicidade(CalendarioRegistroAulaVO calendarioRegistroAulaVO, UsuarioVO usuarioVO) throws Exception {
		CalendarioRegistroAulaVO obj = consultarPorCalendarioRegistroUnico(calendarioRegistroAulaVO.getUnidadeEnsino().getCodigo(), calendarioRegistroAulaVO.getUnidadeEnsinoCurso().getCodigo(), calendarioRegistroAulaVO.getTurma().getCodigo(), calendarioRegistroAulaVO.getProfessor().getCodigo(),calendarioRegistroAulaVO.getAno(),  false, usuarioVO);
		if (obj != null && obj.getCodigo() > 0 && !obj.getCodigo().equals(calendarioRegistroAulaVO.getCodigo())) {
			throw new Exception("Já existe um CALENDÁRIO DE REGISTRO DE AULA já cadastrado com estas configurações, favor consultar e alterar o mesmo.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final CalendarioRegistroAulaVO calendarioRegistroAulaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		incluir(idEntidade, validarAcesso, usuarioVO);

		final StringBuilder sql = new StringBuilder(" INSERT INTO CalendarioRegistroAula( ano, unidadeEnsino, ");
		sql.append(" dataJaneiro, dataFevereiro, ");
		sql.append(" dataMarco, dataAbril, ");
		sql.append(" dataMaio, dataJunho, ");
		sql.append(" dataJulho, dataAgosto, ");
		sql.append(" dataSetembro, dataOutubro, ");
		sql.append(" dataNovembro, dataDezembro, ");
		sql.append(" unidadeEnsinoCurso, turma, professor ");
		sql.append(" ) VALUES ( ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,? ) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

		calendarioRegistroAulaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setString(x++, calendarioRegistroAulaVO.getAno());
				ps.setInt(x++, calendarioRegistroAulaVO.getUnidadeEnsino().getCodigo());
				if (calendarioRegistroAulaVO.getDataJaneiro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataJaneiro()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataFevereiro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataFevereiro()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataMarco() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataMarco()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataAbril() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataAbril()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataMaio() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataMaio()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataJunho() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataJunho()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataJulho() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataJulho()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataAgosto() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataAgosto()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataSetembro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataSetembro()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataOutubro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataOutubro()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataNovembro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataNovembro()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataDezembro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataDezembro()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioRegistroAulaVO.getUnidadeEnsinoCurso().getCodigo() > 0) {
					ps.setInt(x++, calendarioRegistroAulaVO.getUnidadeEnsinoCurso().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getTurma().getCodigo() > 0) {
					ps.setInt(x++, calendarioRegistroAulaVO.getTurma().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getProfessor().getCodigo() > 0) {
					ps.setInt(x++, calendarioRegistroAulaVO.getProfessor().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}

				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		calendarioRegistroAulaVO.setNovoObj(Boolean.FALSE);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final CalendarioRegistroAulaVO calendarioRegistroAulaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		alterar(idEntidade, validarAcesso, usuarioVO);

		final StringBuilder sql = new StringBuilder(" UPDATE CalendarioRegistroAula SET ano = ?, unidadeEnsino = ?, ");
		sql.append(" dataJaneiro=?, dataFevereiro=?, ");
		sql.append(" dataMarco=?, dataAbril=?, ");
		sql.append(" dataMaio=?, dataJunho=?, ");
		sql.append(" dataJulho=?, dataAgosto=?, ");
		sql.append(" dataSetembro=?, dataOutubro=?, ");
		sql.append(" dataNovembro=?, dataDezembro=?, ");
		sql.append("  unidadeEnsinoCurso=?, turma = ?, professor = ? ");
		sql.append(" where  codigo = ?").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setString(x++, calendarioRegistroAulaVO.getAno());
				ps.setInt(x++, calendarioRegistroAulaVO.getUnidadeEnsino().getCodigo());
				if (calendarioRegistroAulaVO.getDataJaneiro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataJaneiro()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataFevereiro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataFevereiro()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataMarco() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataMarco()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataAbril() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataAbril()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataMaio() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataMaio()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataJunho() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataJunho()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataJulho() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataJulho()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataAgosto() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataAgosto()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataSetembro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataSetembro()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataOutubro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataOutubro()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataNovembro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataNovembro()));
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getDataDezembro() != null) {
					ps.setDate(x++, Uteis.getDataJDBC(calendarioRegistroAulaVO.getDataDezembro()));
				} else {
					ps.setNull(x++, 0);
				}

				if (calendarioRegistroAulaVO.getUnidadeEnsinoCurso().getCodigo() > 0) {
					ps.setInt(x++, calendarioRegistroAulaVO.getUnidadeEnsinoCurso().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getTurma().getCodigo() > 0) {
					ps.setInt(x++, calendarioRegistroAulaVO.getTurma().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				if (calendarioRegistroAulaVO.getProfessor().getCodigo() > 0) {
					ps.setInt(x++, calendarioRegistroAulaVO.getProfessor().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}

				ps.setInt(x++, calendarioRegistroAulaVO.getCodigo());
				return ps;
			}
		});
		calendarioRegistroAulaVO.setNovoObj(Boolean.FALSE);
	}

	@Override
	public void validarDados(CalendarioRegistroAulaVO obj) throws ConsistirException {
		ConsistirException ex = new ConsistirException();
		if (obj.getUnidadeEnsino().getCodigo() == null || obj.getUnidadeEnsino().getCodigo() == 0) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_unidadeEnsino"));
		}
		if (obj.getAno() == null || obj.getAno().isEmpty()) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_ano"));
		}
		if (obj.getDataJaneiro() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataJaneiro"));
		}
		if (obj.getDataFevereiro() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataFevereiro"));
		}
		if (obj.getDataMarco() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataMarco"));
		}
		if (obj.getDataAbril() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataAbril"));
		}
		if (obj.getDataMaio() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataMaio"));
		}
		if (obj.getDataJunho() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataJunho"));
		}
		if (obj.getDataJulho() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataJulho"));
		}
		if (obj.getDataAgosto() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataAgosto"));
		}
		if (obj.getDataSetembro() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataSetembro"));
		}
		if (obj.getDataOutubro() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataOutubro"));
		}
		if (obj.getDataNovembro() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataNovembro"));
		}
		if (obj.getDataDezembro() == null) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_dataDezembro"));
		}
		
		if (!ex.getListaMensagemErro().isEmpty()) {
			throw ex;
		} else {
			ex = null;
		}
	}

	public String getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("SELECT CalendarioRegistroAula.*, curso.nome as \"curso.nome\", turno.nome as \"turno.nome\", turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sql.append(" pessoa.nome as \"pessoa.nome\", unidadeEnsino.nome as \"unidadeEnsino.nome\"  ");
		sql.append(" FROM CalendarioRegistroAula  ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = CalendarioRegistroAula.unidadeEnsino ");
		sql.append(" left join unidadeEnsinocurso on unidadeEnsinocurso.codigo = CalendarioRegistroAula.unidadeEnsinocurso ");
		sql.append(" left join curso on curso.codigo = unidadeEnsinocurso.curso ");
		sql.append(" left join turno on turno.codigo = unidadeEnsinocurso.turno ");
		sql.append(" left join turma on turma.codigo = CalendarioRegistroAula.turma ");
		sql.append(" left join pessoa on pessoa.codigo = CalendarioRegistroAula.professor ");
		return sql.toString();
	}

	@Override
	public CalendarioRegistroAulaVO consultarPorCalendarioRegistroUnico(Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor, String ano,  Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), validarAcesso, usuarioVO);
		if (unidadeEnsino == null || unidadeEnsino == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_unidadeEnsino"));
		}
		
		if (ano == null || ano.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_CalendarioRegistroAula_ano"));
		}
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE unidadeEnsino.codigo = ").append(unidadeEnsino);		
		sql.append(" and CalendarioRegistroAula.ano = '").append(ano).append("' ");		
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0) {
			sql.append(" and unidadeEnsinoCurso.codigo = ").append(unidadeEnsinoCurso);
		} else {
			sql.append(" and unidadeEnsinoCurso.codigo is null ");
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		} else {
			sql.append(" and turma.codigo is null ");
		}
		if (professor != null && professor > 0) {
			sql.append(" and pessoa.codigo = ").append(professor);
		} else {
			sql.append(" and pessoa.codigo is null ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		return null;
	}

	@Override
	public CalendarioRegistroAulaVO consultarPorCalendarioRegistroAulaUtilizar(Integer unidadeEnsino, Integer turma, Boolean turmaAgrupada, Integer professor,  String ano, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), validarAcesso, usuarioVO);
		// Busca Especifico do Professor na Turma
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" where CalendarioRegistroAula.codigo in ");
		sql.append(" (select codigo from ( SELECT 1 as ordem, CalendarioRegistroAula.codigo ");
		sql.append(" FROM CalendarioRegistroAula  ");
		sql.append(" WHERE CalendarioRegistroAula.unidadeEnsino = ").append(unidadeEnsino);
		
		sql.append(" and CalendarioRegistroAula.ano = '").append(ano).append("' ");
		
		sql.append(" and turma = ").append(turma);
		sql.append(" and professor = ").append(professor);

		// Busca Especifico da Turma
		sql.append(" union all ");

		sql.append(" SELECT 2 as ordem, CalendarioRegistroAula.codigo ");
		sql.append(" FROM CalendarioRegistroAula  ");
		sql.append(" inner join turma on turma.codigo = CalendarioRegistroAula.turma ");
		sql.append(" WHERE CalendarioRegistroAula.unidadeEnsino = ").append(unidadeEnsino);
		
		sql.append(" and CalendarioRegistroAula.ano = '").append(ano).append("' ");
		sql.append(" and turma = ").append(turma);
		sql.append(" and professor is null ");

		if (turmaAgrupada == null || !turmaAgrupada) {
			// Busca Especifico do Curso/Turno
			sql.append(" union all ");
			sql.append(" SELECT 3 as ordem, CalendarioRegistroAula.codigo ");
			sql.append(" FROM CalendarioRegistroAula  ");
			sql.append(" inner join turma on turma.codigo = ").append(turma);
			sql.append(" inner join unidadeEnsinocurso on unidadeEnsinocurso.codigo = CalendarioRegistroAula.unidadeEnsinocurso ");
			sql.append(" and turma.curso = unidadeEnsinocurso.curso ");
			sql.append(" and turma.turno = unidadeEnsinocurso.turno ");
			sql.append(" WHERE CalendarioRegistroAula.unidadeEnsino = ").append(unidadeEnsino);
			sql.append(" and CalendarioRegistroAula.ano = '").append(ano).append("' ");
			sql.append(" and turma is null ");
			sql.append(" and professor is null ");
		}
		// Busca Especifico da Unidade Ensino
		sql.append(" union all ");
		sql.append(" SELECT 4 as ordem, CalendarioRegistroAula.codigo ");
		sql.append(" FROM CalendarioRegistroAula  ");
		sql.append(" WHERE CalendarioRegistroAula.unidadeEnsino = ").append(unidadeEnsino);
		sql.append(" and CalendarioRegistroAula.ano = '").append(ano).append("' ");
		sql.append(" and unidadeEnsinoCurso is null ");
		sql.append(" and turma is null ");
		sql.append(" and professor is null ");

		sql.append(" order by ordem limit 1 ) as calendario) ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		return null;
	}

	@Override
	public List<CalendarioRegistroAulaVO> consultar(Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor,  String ano,  Boolean validarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception {
		consultar(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE 1=1  ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		
		if (ano != null && !ano.trim().isEmpty()) {
			sql.append(" and CalendarioRegistroAula.ano = '").append(ano).append("' ");
		}
		
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0) {
			sql.append(" and unidadeEnsinoCurso.codigo = ").append(unidadeEnsinoCurso);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if (professor != null && professor > 0) {
			sql.append(" and pessoa.codigo = ").append(professor);
		}
		sql.append(" order by CalendarioRegistroAula.codigo desc ");
		if (limit != null && limit > 0) {
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, usuarioVO);
	}

	@Override
	public Integer consultarTotalRegistro(Integer unidadeEnsino, Integer unidadeEnsinoCurso, Integer turma, Integer professor,String ano) throws Exception {

		StringBuilder sql = new StringBuilder(" select count(CalendarioRegistroAula.codigo) as qtde from CalendarioRegistroAula ");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = CalendarioRegistroAula.unidadeEnsino ");
		sql.append(" left join unidadeEnsinocurso on unidadeEnsinocurso.codigo = CalendarioRegistroAula.unidadeEnsinocurso ");
		sql.append(" left join curso on curso.codigo = unidadeEnsinocurso.curso ");
		sql.append(" left join turno on turno.codigo = unidadeEnsinocurso.turno ");
		sql.append(" left join turma on turma.codigo = CalendarioRegistroAula.turma ");
		sql.append(" left join pessoa on pessoa.codigo = CalendarioRegistroAula.professor ");
		sql.append(" WHERE 1=1  ");
		if (unidadeEnsino != null && unidadeEnsino > 0) {
			sql.append(" and unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (ano != null && !ano.trim().isEmpty()) {
			sql.append(" and CalendarioRegistroAula.ano = '").append(ano).append("' ");
		}
		if (unidadeEnsinoCurso != null && unidadeEnsinoCurso > 0) {
			sql.append(" and unidadeEnsinoCurso.codigo = ").append(unidadeEnsinoCurso);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if (professor != null && professor > 0) {
			sql.append(" and pessoa.codigo = ").append(professor);
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	private List<CalendarioRegistroAulaVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		List<CalendarioRegistroAulaVO> calendarioRegistroAulaVOs = new ArrayList<CalendarioRegistroAulaVO>(0);
		while (rs.next()) {
			calendarioRegistroAulaVOs.add(montarDados(rs, usuario));
		}
		return calendarioRegistroAulaVOs;
	}

	private CalendarioRegistroAulaVO montarDados(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		CalendarioRegistroAulaVO obj = new CalendarioRegistroAulaVO();
		obj.setCodigo(rs.getInt("codigo"));		
		obj.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
		obj.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino.nome"));
		obj.getUnidadeEnsinoCurso().setCodigo(rs.getInt("unidadeEnsinoCurso"));
		if (obj.getUnidadeEnsinoCurso().getCodigo() > 0) {
			obj.getUnidadeEnsinoCurso().getCurso().setNome(rs.getString("curso.nome"));
			obj.getUnidadeEnsinoCurso().getTurno().setNome(rs.getString("turno.nome"));
		}

		obj.getTurma().setCodigo(rs.getInt("turma"));
		if (obj.getTurma().getCodigo() > 0) {
			obj.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
		}
		obj.getProfessor().setCodigo(rs.getInt("professor"));
		if (obj.getProfessor().getCodigo() > 0) {
			obj.getProfessor().setNome(rs.getString("pessoa.nome"));
		}
		obj.setAno(rs.getString("ano"));
		
		obj.setDataJaneiro(rs.getDate("dataJaneiro"));
		obj.setDataFevereiro(rs.getDate("dataFevereiro"));
		obj.setDataMarco(rs.getDate("dataMarco"));
		obj.setDataAbril(rs.getDate("dataAbril"));
		obj.setDataMaio(rs.getDate("dataMaio"));
		obj.setDataJunho(rs.getDate("dataJunho"));
		obj.setDataJulho(rs.getDate("dataJulho"));
		obj.setDataAgosto(rs.getDate("dataAgosto"));
		obj.setDataSetembro(rs.getDate("dataSetembro"));
		obj.setDataOutubro(rs.getDate("dataOutubro"));
		obj.setDataNovembro(rs.getDate("dataNovembro"));
		obj.setDataDezembro(rs.getDate("dataDezembro"));
		
		
		obj.setNovoObj(false);
		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return CalendarioRegistroAula.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CalendarioRegistroAula.idEntidade = idEntidade;
	}

}
