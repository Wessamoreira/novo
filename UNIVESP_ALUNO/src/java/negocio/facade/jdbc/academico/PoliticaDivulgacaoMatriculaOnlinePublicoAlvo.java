package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO;
import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlineVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.TipoPeriodoLetivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Escolaridade;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class PoliticaDivulgacaoMatriculaOnlinePublicoAlvo extends ControleAcesso implements PoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public PoliticaDivulgacaoMatriculaOnlinePublicoAlvo() throws Exception {
		super();
		setIdEntidade("PoliticaDivulgacaoMatriculaOnlinePublicoAlvo");
	}

	public static void validarDados(PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO obj) throws Exception {
		if (obj.getPublicoAlvo().equals("ALUNO")) {
			if (obj.getTipoPeriodoLetivoEnum().equals(TipoPeriodoLetivoEnum.FAIXA_PERIODO)) {
				if (obj.getPeriodoLetivoDe() >= obj.getPeriodoLetivoAte()) {
					throw new ConsistirException("O Campo Período Letivo De deve ser menor que o Até !");
				}
				if (obj.getPeriodoLetivoDe().equals(0)) {
					throw new ConsistirException("O Campo Período Letivo De deve ser maior que 0 !");
				}
				if (obj.getPeriodoLetivoAte().equals(0)) {
					throw new ConsistirException("O Campo Período Letivo Até deve ser maior que 0 !");
				}
				if (obj.getPeriodoLetivoAte() <= obj.getPeriodoLetivoDe()) {
					throw new ConsistirException("O Campo Período Letivo Até deve ser maior que o Campo Período Letivo De !");
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(PoliticaDivulgacaoMatriculaOnlineVO politica, List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> lista, UsuarioVO usuarioVO) throws Exception {
		Iterator<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> e = lista.iterator();
		while (e.hasNext()) {
			PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO obj = (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO) e.next();
			obj.setPoliticaDivulgacaoMatriculaOnline(politica.getCodigo());
			TipoNivelEducacional tipoNivelEducacinal = TipoNivelEducacional.getEnum(obj.getNivelEducacional());
			obj.setNivelEducacional(tipoNivelEducacinal == null ? null : tipoNivelEducacinal.getValor());
			Escolaridade escolaridade = Escolaridade.getEnum(obj.getEscolaridade());
			obj.setEscolaridade(escolaridade == null ? null : escolaridade.getValor());
			incluir(obj, usuarioVO);
			obj.setNivelEducacional(tipoNivelEducacinal == null ? null : tipoNivelEducacinal.getDescricao());
			obj.setEscolaridade(escolaridade == null ? null : escolaridade.getDescricao());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 28/10/2014
			  *  Classe Subordinada
			  */ 
			 // PoliticaDivulgacaoMatriculaOnlinePublicoAlvo.incluir(getIdEntidade());
			final String sql = " INSERT INTO politicadivulgacaomatriculaonlinepublicoalvo (politicadivulgacaomatriculaonline, publicoalvo, unidadeensino, alunoativo, alunoformado, niveleducacional, curso, turno, turma, periodo, periodoletivode, periodoletivoate, departamento, cargo, escolaridade, datacadastro, usuario) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						sqlInserir.setInt(1, obj.getPoliticaDivulgacaoMatriculaOnline());
						sqlInserir.setString(2, obj.getPublicoAlvo());
						sqlInserir.setInt(3, obj.getUnidadeEnsino().getCodigo());
						if (obj.getPublicoAlvo().equals("ALUNO")) {
							sqlInserir.setBoolean(4, obj.getAlunoAtivo());
							sqlInserir.setBoolean(5, obj.getAlunoFormado());
						} else {
							sqlInserir.setNull(4, Types.NULL);
							sqlInserir.setNull(5, Types.NULL);
						}
						if (obj.getNivelEducacional() == null) {
							sqlInserir.setNull(6, Types.NULL);
						} else {
							sqlInserir.setString(6, obj.getNivelEducacional());
						}
						if (obj.getCurso().getCodigo().intValue() == 0) {
							sqlInserir.setNull(7, Types.NULL);
						} else {
							sqlInserir.setInt(7, obj.getCurso().getCodigo());
						}
						if (obj.getTurno().getCodigo().intValue() == 0) {
							sqlInserir.setNull(8, Types.NULL);
						} else {
							sqlInserir.setInt(8, obj.getTurno().getCodigo());
						}
						if (obj.getTurma().getCodigo().intValue() == 0) {
							sqlInserir.setNull(9, Types.NULL);
						} else {
							sqlInserir.setInt(9, obj.getTurma().getCodigo());
						}
						if (obj.getPublicoAlvo().equals("ALUNO")) {
							sqlInserir.setString(10, obj.getTipoPeriodoLetivoEnum().name());
						} else {
							sqlInserir.setNull(10, Types.NULL);
						}
						if (obj.getPeriodoLetivoDe().intValue() == 0) {
							sqlInserir.setNull(11, Types.NULL);
						} else {
							sqlInserir.setInt(11, obj.getPeriodoLetivoDe());
						}
						if (obj.getPeriodoLetivoAte().intValue() == 0) {
							sqlInserir.setNull(12, Types.NULL);
						} else {
							sqlInserir.setInt(12, obj.getPeriodoLetivoAte());
						}
						if (obj.getDepartamento().getCodigo().intValue() == 0) {
							sqlInserir.setNull(13, Types.NULL);
						} else {
							sqlInserir.setInt(13, obj.getDepartamento().getCodigo());
						}
						if (obj.getCargo().getCodigo().intValue() == 0) {
							sqlInserir.setNull(14, Types.NULL);
						} else {
							sqlInserir.setInt(14, obj.getCargo().getCodigo());
						}
						if (obj.getEscolaridade() != null) {
							sqlInserir.setString(15, obj.getEscolaridade());
						} else {
							sqlInserir.setNull(15, Types.NULL);
						}
						sqlInserir.setTimestamp(16, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
						sqlInserir.setInt(17, usuarioVO.getCodigo());
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
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			PoliticaDivulgacaoMatriculaOnlinePublicoAlvo.alterar(getIdEntidade());
			final String sql = " UPDATE politicadivulgacaomatriculaonlinepublicoalvo SET politicadivulgacaomatriculaonline=?, publicoalvo=?, unidadeensino=?, alunoativo=?, alunoformado=?, niveleducacional=?, curso=?, turno=?, turma=?, periodo=?, periodoletivode=?, periodoletivoate=?, departamento=?, cargo=?, escolaridade=?, datacadastro=?, usuario=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getPoliticaDivulgacaoMatriculaOnline());
					sqlAlterar.setString(2, obj.getPublicoAlvo());
					sqlAlterar.setInt(3, obj.getUnidadeEnsino().getCodigo());
					if (obj.getPublicoAlvo().equals("ALUNO")) {
						sqlAlterar.setBoolean(4, obj.getAlunoAtivo());
						sqlAlterar.setBoolean(5, obj.getAlunoFormado());
					} else {
						sqlAlterar.setNull(4, Types.NULL);
						sqlAlterar.setNull(5, Types.NULL);
					}
					if (obj.getNivelEducacional() == null) {
						sqlAlterar.setNull(6, Types.NULL);
					} else {
						sqlAlterar.setString(6, obj.getNivelEducacional());
					}
					if (obj.getCurso().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(7, Types.NULL);
					} else {
						sqlAlterar.setInt(7, obj.getCurso().getCodigo());
					}
					if (obj.getTurno().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(8, Types.NULL);
					} else {
						sqlAlterar.setInt(8, obj.getTurno().getCodigo());
					}
					if (obj.getTurma().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(9, Types.NULL);
					} else {
						sqlAlterar.setInt(9, obj.getTurma().getCodigo());
					}
					if (obj.getPublicoAlvo().equals("ALUNO")) {
						sqlAlterar.setString(10, obj.getTipoPeriodoLetivoEnum().name());
					} else {
						sqlAlterar.setNull(10, Types.NULL);
					}
					if (obj.getPeriodoLetivoDe().intValue() == 0) {
						sqlAlterar.setNull(11, Types.NULL);
					} else {
						sqlAlterar.setInt(11, obj.getPeriodoLetivoDe());
					}
					if (obj.getPeriodoLetivoAte().intValue() == 0) {
						sqlAlterar.setNull(12, Types.NULL);
					} else {
						sqlAlterar.setInt(12, obj.getPeriodoLetivoAte());
					}
					if (obj.getDepartamento().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(13, Types.NULL);
					} else {
						sqlAlterar.setInt(13, obj.getDepartamento().getCodigo());
					}
					if (obj.getCargo().getCodigo().intValue() == 0) {
						sqlAlterar.setNull(14, Types.NULL);
					} else {
						sqlAlterar.setInt(14, obj.getCargo().getCodigo());
					}
					if (obj.getEscolaridade() != null) {
						sqlAlterar.setString(15, obj.getEscolaridade());
					} else {
						sqlAlterar.setNull(15, Types.NULL);
					}
					sqlAlterar.setTimestamp(16, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
					sqlAlterar.setInt(17, usuarioVO.getCodigo());
					sqlAlterar.setInt(18, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			PoliticaDivulgacaoMatriculaOnlinePublicoAlvo.excluir(getIdEntidade());
			String sql = "DELETE FROM politicaDivulgacaoMatriculaOnlinePublicoAlvo WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(PoliticaDivulgacaoMatriculaOnlineVO obj, List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> lista, String publicoAlvo) throws Exception {
		StringBuilder sql = new StringBuilder(" delete from politicaDivulgacaoMatriculaOnlinePublicoAlvo where politicaDivulgacaoMatriculaOnline = ").append(obj.getCodigo()).append(" and publicoalvo = '").append(publicoAlvo + "'");
		sql.append(" and codigo not in(0");

		for (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politica : lista) {

			sql.append(",").append(politica.getCodigo());
		}
		sql.append(" ) ");
		try {

			getConexao().getJdbcTemplate().update(sql.toString());

		} catch (Exception e) {
			throw e;
		} finally {

		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPoliticaDivulgacaoMatriculaOnlinePublicoAlvo(PoliticaDivulgacaoMatriculaOnlineVO obj, List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> lista, UsuarioVO usuarioVO, String publicoAlvo) throws Exception {
		this.excluirPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(obj, lista, publicoAlvo);
		for (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politica : lista) {
			politica.setPoliticaDivulgacaoMatriculaOnline(obj.getCodigo());
			try {
				if (politica.isNovoObj()) {
					TipoNivelEducacional tipoNivelEducacinal = TipoNivelEducacional.getEnum(politica.getNivelEducacional());
					politica.setNivelEducacional(tipoNivelEducacinal == null ? null : tipoNivelEducacinal.getValor());
					Escolaridade escolaridade = Escolaridade.getEnum(politica.getEscolaridade());
					politica.setEscolaridade(escolaridade == null ? null : escolaridade.getValor());
					incluir(politica, usuarioVO);
					politica.setNivelEducacional(tipoNivelEducacinal == null ? null : tipoNivelEducacinal.getDescricao());
					politica.setEscolaridade(escolaridade == null ? null : escolaridade.getDescricao());
				}
				TipoNivelEducacional tipoNivelEducacinal = TipoNivelEducacional.getEnum(politica.getNivelEducacional());
				politica.setNivelEducacional(tipoNivelEducacinal == null ? null : tipoNivelEducacinal.getValor());
				Escolaridade escolaridade = Escolaridade.getEnum(politica.getEscolaridade());
				politica.setEscolaridade(escolaridade == null ? null : escolaridade.getValor());
				alterar(politica, usuarioVO);
				politica.setNivelEducacional(tipoNivelEducacinal == null ? null : tipoNivelEducacinal.getDescricao());
				politica.setEscolaridade(escolaridade == null ? null : escolaridade.getDescricao());
			} catch (Exception e) {
				throw e;
			} finally {

			}
		}
	}

	@Override
	public List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> consultarPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(Integer politicaDivulgacaoMatriculaOnline, int nivelMontarDados, String publicoAlvo, List<UnidadeEnsinoVO> listaUnidadeEnsino, UsuarioVO usuario) throws Exception {
		PoliticaDivulgacaoMatriculaOnlinePublicoAlvo.consultar(getIdEntidade());
		List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> objetos = new ArrayList(0);
		String sql = "SELECT * FROM PoliticaDivulgacaoMatriculaOnlinePublicoAlvo WHERE politicaDivulgacaoMatriculaOnline = ? and publicoalvo = ? ";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { politicaDivulgacaoMatriculaOnline, publicoAlvo });
		while (resultado.next()) {
			PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO novoObj = new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO();
			novoObj = montarDados(resultado, nivelMontarDados, listaUnidadeEnsino, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	public void verificaDuplicacaoObjetoPoliticaMatriculaOnlineNovoObjeto(PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO, PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaPublicoAlvo, List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> listaPublicoAlvo, UsuarioVO usuario, Integer codigoCurso) throws Exception {
		
			adicionarPoliticaDivulgacaoMatriculaOnlineVOs(politicaDivulgacaoMatriculaOnlineVO, politicaPublicoAlvo.getPublicoAlvo(), politicaPublicoAlvo, usuario, codigoCurso);
			List<CursoVO> cursoVOs = new ArrayList<>();
			cursoVOs = getFacadeFactory().getCursoFacade().consultarCursoPorCodigoUnidadeEnsino(codigoCurso, politicaPublicoAlvo.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			
	}

	public static void montaNomeUnidade(PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaPublicoAlvo, List<UnidadeEnsinoVO> listaUnidadeEnsino) {
		Iterator i = listaUnidadeEnsino.iterator();
		while (i.hasNext()) {
			UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
			if (politicaPublicoAlvo.getUnidadeEnsino().getCodigo().intValue() == obj.getCodigo().intValue()) {
				politicaPublicoAlvo.setUnidadeEnsino(obj);
				break;
			}
		}
	}

	public int montaNumeroPeriodo(String intervaloPeriodo) {
		if (intervaloPeriodo.equals("primeiros")) {
			return 5;
		} else if (intervaloPeriodo.equals("ultimos")) {
			return 10;
		} else {
			return 0;
		}

	}

	@Override
	public void adicionarObjPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaPublicoAlvo, PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO, String publicoAlvo, String nivelEducacional, List<UnidadeEnsinoVO> listaUnidadeEnsino, UsuarioVO usuario, Integer codigoCurso) throws Exception {
		if (publicoAlvo.equals("ALUNO") || publicoAlvo.equals("COORDENADOR") || publicoAlvo.equals("PROFESSOR")) {
			TipoNivelEducacional tipoNivelEducacinal = TipoNivelEducacional.getEnum(nivelEducacional);
			politicaPublicoAlvo.setNivelEducacional(tipoNivelEducacinal == null ? null : tipoNivelEducacinal.getDescricao());
		} else {
			Escolaridade escolaridade = Escolaridade.getEnum(nivelEducacional);
			politicaPublicoAlvo.setEscolaridade(escolaridade == null ? null : escolaridade.getDescricao());
		}
		politicaPublicoAlvo.setPublicoAlvo(publicoAlvo);
		this.montaNomeUnidade(politicaPublicoAlvo, listaUnidadeEnsino);
		if (politicaPublicoAlvo.getNovoObj()) {
			politicaPublicoAlvo.setDataCadastro(new Date());
		} else {
			politicaPublicoAlvo.setDataCadastro(politicaPublicoAlvo.getDataCadastro());
		}
		politicaPublicoAlvo.setUsuario(usuario);
		this.validarDados(politicaPublicoAlvo);
		if ((publicoAlvo.equals("ALUNO") || publicoAlvo.equals("COORDENADOR") || publicoAlvo.equals("FUNCIONARIO") || publicoAlvo.equals("PROFESSOR"))) {
			this.verificaDuplicacaoObjetoPoliticaMatriculaOnlineNovoObjeto(politicaDivulgacaoMatriculaOnlineVO, politicaPublicoAlvo, politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs(), usuario, codigoCurso);
		} 
	}

	public  PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, List<UnidadeEnsinoVO> listaUnidadeEnsino, UsuarioVO usuario) throws Exception {
		PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO obj = new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setPoliticaDivulgacaoMatriculaOnline(new Integer(dadosSQL.getInt("politicaDivulgacaoMatriculaOnline")));
		obj.setPublicoAlvo(dadosSQL.getString("publicoAlvo"));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		montaNomeUnidade(obj, listaUnidadeEnsino);
		obj.setAlunoAtivo(dadosSQL.getBoolean("alunoAtivo"));
		obj.setAlunoFormado(dadosSQL.getBoolean("alunoFormado"));
		obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
		if (!obj.getCurso().getCodigo().equals(0)) {
			obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
		}
		obj.getTurno().setCodigo(new Integer(dadosSQL.getInt("turno")));
		if (!obj.getTurno().getCodigo().equals(0)) {
			obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		if (!obj.getTurma().getCodigo().equals(0)) {
			obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("periodo"))) {
			obj.setTipoPeriodoLetivoEnum(TipoPeriodoLetivoEnum.valueOf(dadosSQL.getString("periodo")));
		}
		obj.setPeriodoLetivoDe(dadosSQL.getInt("periodoletivode"));
		obj.setPeriodoLetivoAte(dadosSQL.getInt("periodoletivoate"));
		obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento")));
		if (!obj.getDepartamento().getCodigo().equals(0)) {
			obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		obj.getCargo().setCodigo(new Integer(dadosSQL.getInt("cargo")));
		if (!obj.getCargo().getCodigo().equals(0)) {
			obj.setCargo(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getCargo().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		obj.setEscolaridade(dadosSQL.getString("escolaridade"));
		if (obj.getPublicoAlvo().equals("ALUNO") || obj.getPublicoAlvo().equals("COORDENADOR") || obj.getPublicoAlvo().equals("PROFESSOR")) {
			TipoNivelEducacional tipoNivelEducacinal = TipoNivelEducacional.getEnum(obj.getNivelEducacional());
			obj.setNivelEducacional(tipoNivelEducacinal == null ? null : tipoNivelEducacinal.getDescricao());
		} else {
			Escolaridade escolaridade = Escolaridade.getEnum(obj.getEscolaridade());
			obj.setEscolaridade(escolaridade == null ? null : escolaridade.getDescricao());
		}
		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		obj.getUsuario().setCodigo(new Integer(dadosSQL.getInt("usuario")));
		obj.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuario().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	@Override
	public void removerPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacao, PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaPublicoAlvo, String publicoAlvo) {
		if (publicoAlvo.equals("ALUNO")) {
			for (Iterator<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> iterator = politicaDivulgacao.getPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs().iterator(); iterator.hasNext();) {
				PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politica = (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO) iterator.next();
				if (politica.getCodigo().equals(politicaPublicoAlvo.getCodigo()) && politica.getUnidadeEnsino().getCodigo().equals(politicaPublicoAlvo.getUnidadeEnsino().getCodigo()) && politica.getAlunoAtivo().equals(politicaPublicoAlvo.getAlunoAtivo()) && politica.getAlunoFormado().equals(politicaPublicoAlvo.getAlunoFormado() && politica.getNivelEducacional().equals(politicaPublicoAlvo.getNivelEducacional())) && politica.getCurso().getCodigo().equals(politicaPublicoAlvo.getCurso().getCodigo()) && politica.getTurno().getCodigo().equals(politicaPublicoAlvo.getTurno().getCodigo()) && politica.getTurma().getCodigo().equals(politicaPublicoAlvo.getTurma().getCodigo()) && politica.getPeriodoLetivoDe().equals(politicaPublicoAlvo.getPeriodoLetivoDe()) && politica.getPeriodoLetivoAte().equals(politicaPublicoAlvo.getPeriodoLetivoAte())) {
					iterator.remove();
				}
			}
		}
		if (publicoAlvo.equals("COORDENADOR")) {
			for (Iterator<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> iterator = politicaDivulgacao.getPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs().iterator(); iterator.hasNext();) {
				PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politica = (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO) iterator.next();
				if (politica.getCodigo().equals(politicaPublicoAlvo.getCodigo()) && politica.getUnidadeEnsino().getCodigo().equals(politicaPublicoAlvo.getUnidadeEnsino().getCodigo()) && politica.getAlunoAtivo().equals(politicaPublicoAlvo.getAlunoAtivo()) && politica.getAlunoFormado().equals(politicaPublicoAlvo.getAlunoFormado() && politica.getNivelEducacional().equals(politicaPublicoAlvo.getNivelEducacional())) && politica.getCurso().getCodigo().equals(politicaPublicoAlvo.getCurso().getCodigo()) && politica.getTurno().getCodigo().equals(politicaPublicoAlvo.getTurno().getCodigo()) && politica.getTurma().getCodigo().equals(politicaPublicoAlvo.getTurma().getCodigo()) && politica.getPeriodoLetivoDe().equals(politicaPublicoAlvo.getPeriodoLetivoDe()) && politica.getPeriodoLetivoAte().equals(politicaPublicoAlvo.getPeriodoLetivoAte())) {
					iterator.remove();
				}
			}
		}
		if (publicoAlvo.equals("FUNCIONARIO")) {
			for (Iterator<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> iterator = politicaDivulgacao.getPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs().iterator(); iterator.hasNext();) {
				PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politica = (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO) iterator.next();
				if (politica.getCodigo().equals(politicaPublicoAlvo.getCodigo()) && politica.getUnidadeEnsino().getCodigo().equals(politicaPublicoAlvo.getUnidadeEnsino().getCodigo()) && politica.getAlunoAtivo().equals(politicaPublicoAlvo.getAlunoAtivo()) && politica.getAlunoFormado().equals(politicaPublicoAlvo.getAlunoFormado() && politica.getNivelEducacional().equals(politicaPublicoAlvo.getNivelEducacional())) && politica.getCurso().getCodigo().equals(politicaPublicoAlvo.getCurso().getCodigo()) && politica.getTurno().getCodigo().equals(politicaPublicoAlvo.getTurno().getCodigo()) && politica.getTurma().getCodigo().equals(politicaPublicoAlvo.getTurma().getCodigo()) && politica.getPeriodoLetivoDe().equals(politicaPublicoAlvo.getPeriodoLetivoDe()) && politica.getPeriodoLetivoAte().equals(politicaPublicoAlvo.getPeriodoLetivoAte())) {
					iterator.remove();
				}
			}
		}
		if (publicoAlvo.equals("PROFESSOR")) {
			for (Iterator<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> iterator = politicaDivulgacao.getPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs().iterator(); iterator.hasNext();) {
				PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politica = (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO) iterator.next();
				if (politica.getCodigo().equals(politicaPublicoAlvo.getCodigo()) && politica.getUnidadeEnsino().getCodigo().equals(politicaPublicoAlvo.getUnidadeEnsino().getCodigo()) && politica.getAlunoAtivo().equals(politicaPublicoAlvo.getAlunoAtivo()) && politica.getAlunoFormado().equals(politicaPublicoAlvo.getAlunoFormado() && politica.getNivelEducacional().equals(politicaPublicoAlvo.getNivelEducacional())) && politica.getCurso().getCodigo().equals(politicaPublicoAlvo.getCurso().getCodigo()) && politica.getTurno().getCodigo().equals(politicaPublicoAlvo.getTurno().getCodigo()) && politica.getTurma().getCodigo().equals(politicaPublicoAlvo.getTurma().getCodigo()) && politica.getPeriodoLetivoDe().equals(politicaPublicoAlvo.getPeriodoLetivoDe()) && politica.getPeriodoLetivoAte().equals(politicaPublicoAlvo.getPeriodoLetivoAte())) {
					iterator.remove();
				}
			}
		}

	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		PoliticaDivulgacaoMatriculaOnlinePublicoAlvo.idEntidade = idEntidade;
	}
	
	public void adicionarPoliticaDivulgacaoMatriculaOnlineVOs(PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO, String tipoPublicoAlvo, PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaDivulgacaoMatriculaOnlinePublicoAlvoVO, UsuarioVO usuarioVO, Integer codigoCurso) throws Exception {
		
		if (politicaDivulgacaoMatriculaOnlinePublicoAlvoVO.getUnidadeEnsino().getCodigo().equals(0)) {
			List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		
		List<CursoVO> cursoVOs = new ArrayList<>();
		q:
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (codigoCurso != 0) {
				cursoVOs = getFacadeFactory().getCursoFacade().consultarCursoPorCodigoUnidadeEnsino(codigoCurso, unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
			}
			if (!cursoVOs.isEmpty() || codigoCurso == 0) {
				PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2 = new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO();
				politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2 = (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO) politicaDivulgacaoMatriculaOnlinePublicoAlvoVO.clone();
				politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.setUnidadeEnsino(new UnidadeEnsinoVO());
				politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.setUnidadeEnsino(unidadeEnsinoVO);
				List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> listaPublicoAlvo = null;
				if (tipoPublicoAlvo.equals("ALUNO")) {
					listaPublicoAlvo = politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs();
				} else if (tipoPublicoAlvo.equals("COORDENADOR")) {
					listaPublicoAlvo = politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs();
				} else if (tipoPublicoAlvo.equals("FUNCIONARIO")) {
					listaPublicoAlvo = politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs();
				} else if (tipoPublicoAlvo.equals("PROFESSOR")) {
					listaPublicoAlvo = politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs();
				}
				int index = 0;
				Iterator i = listaPublicoAlvo.iterator();
				while (i.hasNext()) {
					PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO objExistente = (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO) i.next();
					if (objExistente.getUnidadeEnsino().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getUnidadeEnsino().getCodigo()) 
							&& objExistente.getAlunoAtivo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getAlunoAtivo()) 
							&& objExistente.getAlunoFormado().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getAlunoFormado() 
							&& objExistente.getNivelEducacional().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getNivelEducacional())) 
							&& objExistente.getCurso().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getCurso().getCodigo()) 
							&& objExistente.getTurno().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getTurno().getCodigo()) 
							&& objExistente.getTurma().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getTurma().getCodigo()) 
							&& objExistente.getPeriodoLetivoDe().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getPeriodoLetivoDe()) 
							&& objExistente.getPeriodoLetivoAte().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getPeriodoLetivoAte()) 
							&& (objExistente.getDepartamento().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getDepartamento().getCodigo()))
							&& (objExistente.getCargo().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getCargo().getCodigo())) 
							&& (objExistente.getEscolaridade().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getEscolaridade()))) {
						listaPublicoAlvo.set(index, politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2);
						continue q;
					}
					index++;
				}
				
				
				if (tipoPublicoAlvo.equals("ALUNO")) {
					politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs().add(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2);
				} else if (tipoPublicoAlvo.equals("COORDENADOR")) {
					politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs().add(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2);
				} else if (tipoPublicoAlvo.equals("FUNCIONARIO")) {
					politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs().add(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2);
				} else if (tipoPublicoAlvo.equals("PROFESSOR")) {
					politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs().add(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2);
				}
			}
		}
	}else{
		
		UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO.getUnidadeEnsino().getCodigo(), false, usuarioVO);
		
		List<CursoVO> cursoVOs = new ArrayList<>();
		
			if (codigoCurso != 0) {
				cursoVOs = getFacadeFactory().getCursoFacade().consultarCursoPorCodigoUnidadeEnsino(codigoCurso, unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
			}
			if (!cursoVOs.isEmpty() || codigoCurso == 0) {
				PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2 = new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO();
				politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2 = (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO) politicaDivulgacaoMatriculaOnlinePublicoAlvoVO.clone();
				politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.setUnidadeEnsino(new UnidadeEnsinoVO());
				politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.setUnidadeEnsino(unidadeEnsinoVO);
				List<PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO> listaPublicoAlvo = null;
				if (tipoPublicoAlvo.equals("ALUNO")) {
					listaPublicoAlvo = politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs();
				} else if (tipoPublicoAlvo.equals("COORDENADOR")) {
					listaPublicoAlvo = politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs();
				} else if (tipoPublicoAlvo.equals("FUNCIONARIO")) {
					listaPublicoAlvo = politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs();
				} else if (tipoPublicoAlvo.equals("PROFESSOR")) {
					listaPublicoAlvo = politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs();
				}
				int index = 0;
				Iterator i = listaPublicoAlvo.iterator();
				while (i.hasNext()) {
					PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO objExistente = (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO) i.next();
					if (objExistente.getUnidadeEnsino().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getUnidadeEnsino().getCodigo()) 
							&& objExistente.getAlunoAtivo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getAlunoAtivo()) 
							&& objExistente.getAlunoFormado().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getAlunoFormado() 
							&& objExistente.getNivelEducacional().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getNivelEducacional())) 
							&& objExistente.getCurso().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getCurso().getCodigo()) 
							&& objExistente.getTurno().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getTurno().getCodigo()) 
							&& objExistente.getTurma().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getTurma().getCodigo()) 
							&& objExistente.getPeriodoLetivoDe().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getPeriodoLetivoDe()) 
							&& objExistente.getPeriodoLetivoAte().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getPeriodoLetivoAte()) 
							&& (objExistente.getDepartamento().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getDepartamento().getCodigo()))
							&& (objExistente.getCargo().getCodigo().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getCargo().getCodigo())) 
							&& (objExistente.getEscolaridade().equals(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2.getEscolaridade()))) {
						listaPublicoAlvo.set(index, politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2);
					}
					index++;
				}
				
				
				if (tipoPublicoAlvo.equals("ALUNO")) {
					politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineAlunoPublicoAlvoVOs().add(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2);
				} else if (tipoPublicoAlvo.equals("COORDENADOR")) {
					politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineCoordenadorPublicoAlvoVOs().add(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2);
				} else if (tipoPublicoAlvo.equals("FUNCIONARIO")) {
					politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineFuncionarioPublicoAlvoVOs().add(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2);
				} else if (tipoPublicoAlvo.equals("PROFESSOR")) {
					politicaDivulgacaoMatriculaOnlineVO.getPoliticaDivulgacaoMatriculaOnlineProfessorPublicoAlvoVOs().add(politicaDivulgacaoMatriculaOnlinePublicoAlvoVO2);
				}
			}
		
	}

	}
}
