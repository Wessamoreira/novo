package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.PortadorDiplomaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.PortadorDiplomaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PortadorDiplomaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PortadorDiplomaVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see PortadorDiplomaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
public class PortadorDiploma extends ControleAcesso implements PortadorDiplomaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public PortadorDiploma() throws Exception {
		super();
		setIdEntidade("PortadorDiploma");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#novo()
	 */
	public PortadorDiplomaVO novo() throws Exception {
		PortadorDiploma.incluir(getIdEntidade());
		PortadorDiplomaVO obj = new PortadorDiplomaVO();
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#incluir(negocio.comuns.academico.PortadorDiplomaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PortadorDiplomaVO obj, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoGeralSistemaVO c = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, null);
			validarDados(obj, c);
			PortadorDiploma.incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO PortadorDiploma( data, descricao, matricula, codigoRequerimento, curso, instituicaoEnsino, responsavelAutorizacao, tipoMidiaCaptacao, aluno, cidade ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(2, obj.getDescricao());
					if (obj.getMatricula().getMatricula().equals("")) {
						sqlInserir.setNull(3, 0);
					} else {
						sqlInserir.setString(3, obj.getMatricula().getMatricula());
					}
					if (obj.getCodigoRequerimento().getCodigo().equals(0)) {
						sqlInserir.setNull(4, 0);
					} else {
						sqlInserir.setInt(4, obj.getCodigoRequerimento().getCodigo());
					}
					sqlInserir.setString(5, obj.getCurso());
					sqlInserir.setString(6, obj.getInstituicaoEnsino());
					if (obj.getResponsavelAutorizacao().getCodigo().equals(0)) {
						sqlInserir.setNull(7, 0);
					} else {
						sqlInserir.setInt(7, obj.getResponsavelAutorizacao().getCodigo());
					}
					sqlInserir.setInt(8, obj.getTipoMidiaCaptacao().getCodigo());
					sqlInserir.setInt(9, obj.getMatricula().getAluno().getCodigo());
					sqlInserir.setInt(10, obj.getCidade().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#alterar(negocio.comuns.academico.PortadorDiplomaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PortadorDiplomaVO obj, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoGeralSistemaVO c = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, null);
			validarDados(obj, c);
			PortadorDiploma.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE PortadorDiploma set data=?, descricao=?, matricula=?, codigoRequerimento=?, curso=?, instituicaoEnsino=?, responsavelAutorizacao=?, tipoMidiaCaptacao=?, aluno=?, cidade=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setString(2, obj.getDescricao());
					if (obj.getMatricula().getMatricula().equals("")) {
						sqlAlterar.setNull(3, 0);
					} else {
						sqlAlterar.setString(3, obj.getMatricula().getMatricula());
					}
					if (obj.getCodigoRequerimento().getCodigo().equals(0)) {
						sqlAlterar.setNull(4, 0);
					} else {
						sqlAlterar.setInt(4, obj.getCodigoRequerimento().getCodigo());
					}
					sqlAlterar.setString(5, obj.getCurso());
					sqlAlterar.setString(6, obj.getInstituicaoEnsino());
					if (obj.getResponsavelAutorizacao().getCodigo().equals(0)) {
						sqlAlterar.setNull(7, 0);
					} else {
						sqlAlterar.setInt(7, obj.getResponsavelAutorizacao().getCodigo());
					}
					sqlAlterar.setInt(8, obj.getTipoMidiaCaptacao().getCodigo());
					sqlAlterar.setInt(9, obj.getMatricula().getAluno().getCodigo());
					sqlAlterar.setInt(10, obj.getCidade().getCodigo());
					sqlAlterar.setInt(11, obj.getCodigo());
					return sqlAlterar;
				}
			});
			if (!obj.getMatricula().getMatricula().equals("")) {
				getFacadeFactory().getMatriculaFacade().alterarFormaIngressoAluno(obj.getMatricula().getMatricula(), FormaIngresso.PORTADOR_DE_DIPLOMA.getValor());			
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#excluir(negocio.comuns.academico.PortadorDiplomaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PortadorDiplomaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			PortadorDiploma.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM PortadorDiploma WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatricula(Integer codigo, String matricula) throws Exception {
		try {
			// PortadorDiploma.excluir(getIdEntidade());
			String sql = "UPDATE PortadorDiploma set matricula = ? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { matricula, codigo });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#consultarPorNomePessoa(java.lang.String, boolean, int)
	 */
	public List<PortadorDiplomaVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT PortadorDiploma.* FROM PortadorDiploma, Pessoa WHERE PortadorDiploma.responsavelAutorizacao = Pessoa.codigo and lower(Pessoa.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#consultarPorCodigoRequerimento(java.lang.Integer, boolean, int)
	 */
	public List<PortadorDiplomaVO> consultarPorCodigoRequerimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PortadorDiploma WHERE codigoRequerimento >= " + valorConsulta.intValue() + " ORDER BY codigoRequerimento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#consultarPorMatriculaMatricula(java.lang.String, boolean, int)
	 */
	public List<PortadorDiplomaVO> consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT PortadorDiploma.* FROM PortadorDiploma, Matricula WHERE PortadorDiploma.matricula = Matricula.matricula and Matricula.matricula like('" + valorConsulta + "%') ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<PortadorDiplomaVO> consultarPorNomeMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT PortadorDiploma.* FROM PortadorDiploma, Matricula, Pessoa WHERE PortadorDiploma.matricula = Matricula.matricula  and matricula.aluno = pessoa.codigo and Pessoa.nome ilike('" + valorConsulta + "%') ORDER BY pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#consultarPorSituacao(java.lang.String, boolean, int)
	 */
	public List<PortadorDiplomaVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PortadorDiploma WHERE situacao like('" + valorConsulta + "%') ORDER BY situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#consultarPorDescricao(java.lang.String, boolean, int)
	 */
	public List<PortadorDiplomaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PortadorDiploma WHERE lower(descricao) like('" + valorConsulta.toLowerCase() + "%') ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#consultarPorData(java.util.Date, java.util.Date, boolean, int)
	 */
	public List<PortadorDiplomaVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PortadorDiploma WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean, int)
	 */
	public List<PortadorDiplomaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PortadorDiploma WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List<PortadorDiplomaVO> Contendo vários objetos da classe <code>PortadorDiplomaVO</code> resultantes da consulta.
	 */
	public static List<PortadorDiplomaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<PortadorDiplomaVO> vetResultado = new ArrayList<PortadorDiplomaVO>(0);
		while (tabelaResultado.next()) {
			PortadorDiplomaVO obj = new PortadorDiplomaVO();
			obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>PortadorDiplomaVO</code>.
	 * 
	 * @return O objeto da classe <code>PortadorDiplomaVO</code> com os dados devidamente montados.
	 */
	public static PortadorDiplomaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PortadorDiplomaVO obj = new PortadorDiplomaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("aluno"));
		obj.getCodigoRequerimento().setCodigo(new Integer(dadosSQL.getInt("codigoRequerimento")));
		obj.getResponsavelAutorizacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAutorizacao")));
		obj.getCidade().setCodigo(new Integer(dadosSQL.getInt("cidade")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosAluno(obj, nivelMontarDados, usuario);
			return obj;
		}
		obj.setCurso(dadosSQL.getString("curso"));
		obj.setInstituicaoEnsino(dadosSQL.getString("instituicaoEnsino"));
		obj.getTipoMidiaCaptacao().setCodigo(new Integer(dadosSQL.getInt("tipoMidiaCaptacao")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosAluno(obj, nivelMontarDados, usuario);
			return obj;
		}
		if (obj.getMatricula().getMatricula().equals("")) {
			montarDadosAluno(obj, nivelMontarDados, usuario);
		} else {
			montarDadosMatricula(obj, nivelMontarDados, usuario);
		}
		montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCidade(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto <code>PortadorDiplomaVO</code>. Faz
	 * uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelAutorizacao(PortadorDiplomaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>MatriculaVO</code> relacionado ao objeto <code>PortadorDiplomaVO</code>.
	 * Faz uso da chave primária da classe <code>MatriculaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosMatricula(PortadorDiplomaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
	}

	public static void montarDadosAluno(PortadorDiplomaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getAluno() == null) || (obj.getMatricula().getAluno().getCodigo().equals(0))) {
			return;
		}
		obj.getMatricula().setAluno(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getMatricula().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int)
	 */
	public PortadorDiplomaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM PortadorDiploma WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return PortadorDiploma.idEntidade;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.PortadorDiplomaInterfaceFacade#setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		PortadorDiploma.idEntidade = idEntidade;
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>PortadorDiplomaVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os
	 * atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public void validarDados(PortadorDiplomaVO obj, ConfiguracaoGeralSistemaVO c) throws ConsistirException {
		if (obj.getData() == null) {
			throw new ConsistirException("O campo DATA (Portador Diploma) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getMatricula().getAluno())) {
			throw new ConsistirException("O campo ALUNO (Portador Diploma) deve ser informado.");
		}
		// if (!c.getPermiteCancelamentoSemRequerimento().booleanValue()) {
		// if ((obj.getCodigoRequerimento() == null) || (obj.getCodigoRequerimento().getCodigo().intValue() == 0)) {
		// throw new ConsistirException("O campo CÓDIGO REQUERIMENTO (Portador Diploma) deve ser informado.");
		// }
		// }
		if (obj.getCurso().equals("")) {
			throw new ConsistirException("O campo CURSO ORIGEM (Portador Diploma) deve ser informado.");
		}
		if (obj.getInstituicaoEnsino().equals("")) {
			throw new ConsistirException("O campo INSTITUIÇÃO ENSINO ORIGEM (Portador Diploma) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getResponsavelAutorizacao())) {
			throw new ConsistirException("O campo RESPONSAVEL AUTORIZACAO (Portador Diploma) deve ser informado.");
		}
	}

	@Override
	public void validarSituacaoRequerimento(RequerimentoVO obj) throws ConsistirException {
		if (obj.getSituacao().equals("AP")) {
			throw new ConsistirException("Requerimento especificado está aguardando pagamento.");
		}
		if (!obj.getMatricula().getSituacao().equals("AT")) {
			throw new ConsistirException("Matrícula especificada não está ativa.");
		}
		if (obj.getSituacao().equals("FI") || obj.getSituacao().equals("FD")) {
			throw new ConsistirException("Requerimento especificado já está finalizado.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final PortadorDiplomaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getNovoObj()) {
			incluir(obj, usuario);
		} else {
			alterar(obj, usuario);
		}
	}
	
	@Override
	public PortadorDiplomaVO consultarInstitiucaoEnsinoIngressoPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception{
		if (Uteis.isAtributoPreenchido(matricula)) {
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select codigo, curso, instituicaoEnsino, cidade, data from portadordiploma where matricula = ? ", matricula);
			if (rs.next()) {
				PortadorDiplomaVO obj = new PortadorDiplomaVO();
				obj.setInstituicaoEnsino(rs.getString("instituicaoEnsino"));
				obj.setCurso(rs.getString("curso"));
				obj.setCodigo(rs.getInt("codigo"));
				obj.getCidade().setCodigo(rs.getInt("cidade"));
				obj.setData(rs.getDate("data"));
				montarDadosCidade(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
				obj.setNovoObj(false);
				return obj;
			}
		}
		return null;
	}
	
	public static void montarDadosCidade(PortadorDiplomaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getCidade() == null) || (obj.getCidade().getCodigo() == null) || obj.getCidade().getCodigo() == 0) {
			return;
		}
		obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
	}
}


