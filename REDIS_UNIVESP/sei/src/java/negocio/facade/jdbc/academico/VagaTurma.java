package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.VagaTurmaDisciplinaVO;
import negocio.comuns.academico.VagaTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.VagaTurmaInterfaceFacade;

/**
 * Classe de persistï¿½ncia que encapsula todas as operaçãoes de manipulaçãoo dos dados da classe <code>VagaTurmaVO</code>. responsável por
 * implementar operaçãoes como incluir, alterar, excluir e consultar pertinentes a classe <code>VagaTurmaVO</code>. Encapsula toda a interaçãoo
 * com o banco de dados.
 * 
 * @see VagaTurmaVO
 * @see ControleAcesso
 */
@Repository
public class VagaTurma extends ControleAcesso implements VagaTurmaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public VagaTurma() throws Exception {
		super();
		setIdEntidade("VagaTurma");
	}

	public void validarUnicidadeIdentificadorTurma(VagaTurmaVO obj) throws Exception {
		VagaTurmaVO turma = this.consultaRapidaUnicidadeTurmaPorIdentificador(obj.getTurmaVO().getCodigo(), obj.getAno(), obj.getSemestre(), obj.getCodigo());
		if (turma.getCodigo() != 0) {
			throw new ConsistirException("J� existe um Controle de Vaga(s) cadastrado para essa Turma.");
		}
	}

	/**
	 * Operaçãoo responsável por incluir no banco de dados um objeto da classe <code>VagaTurmaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexï¿½o com o banco de dados e a permissï¿½o do usuário para realizar esta
	 * operacï¿½o na entidade. Isto, atravï¿½s da operaçãoo <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>VagaTurmaVO</code> que serï¿½ gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o, restriçãoo de acesso ou validaçãoo de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final VagaTurmaVO obj, UsuarioVO usuario) throws Exception {
		try {
			VagaTurmaVO.validarDados(obj);
			VagaTurma.incluir(getIdEntidade(), true, usuario);
			validarUnicidadeIdentificadorTurma(obj);
			final String sql = "INSERT INTO VagaTurma( turma, ano, semestre, dataCadastro, usuarioResponsavel) VALUES ( ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getTurmaVO().getCodigo());
					sqlInserir.setString(2, obj.getAno().trim());
					sqlInserir.setString(3, obj.getSemestre().trim());
					sqlInserir.setDate(4, Uteis.getDataJDBC(new Date()));
					if (obj.getUsuarioResponsavel().getCodigo() != 0) {
						sqlInserir.setInt(5, obj.getUsuarioResponsavel().getCodigo());
					} else {
						sqlInserir.setNull(5, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getVagaTurmaDisciplinaFacade().incluirTurmaDisciplinas(obj, obj.getVagaTurmaDisciplinaVOs());
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirVagaTurmaBase(final VagaTurmaVO obj, UsuarioVO usuario) throws Exception{
		
		for(TurmaAgrupadaVO agrupadaVO : obj.getTurmaVO().getTurmaAgrupadaVOs()) {
			VagaTurmaVO vagaTurma = getFacadeFactory().getVagaTurmaFacade().consultaRapidaUnicidadeTurmaPorIdentificador(agrupadaVO.getTurma().getCodigo(), "", "", 0);
			if(Uteis.isAtributoPreenchido(vagaTurma.getCodigo())) {
				vagaTurma = getFacadeFactory().getVagaTurmaFacade().consultarPorChavePrimaria(vagaTurma.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
				vincularVagaTurmaDisciplina(obj.getVagaTurmaDisciplinaVOs(),vagaTurma.getVagaTurmaDisciplinaVOs());
				getFacadeFactory().getVagaTurmaDisciplinaFacade().alterarTurmaDisciplinas(vagaTurma, vagaTurma.getVagaTurmaDisciplinaVOs());
			}else {
				VagaTurmaVO vagaTurmaNova = new VagaTurmaVO();
				vagaTurmaNova.setAno(obj.getAno());
				if(agrupadaVO.getTurma().getSemestral()) {
					vagaTurmaNova.setSemestre(obj.getSemestre());
				}
				vagaTurmaNova.setNovoObj(true);
				vagaTurmaNova.setTurmaVO(agrupadaVO.getTurma());
				List<VagaTurmaDisciplinaVO> vagaTurmaDisciplinaVOs = getFacadeFactory().getVagaTurmaDisciplinaFacade().consultaRapidaPorTurma(agrupadaVO.getTurma().getCodigo(), agrupadaVO.getTurma().getCodigo(), usuario);
				vincularVagaTurmaDisciplina(obj.getVagaTurmaDisciplinaVOs(), vagaTurmaDisciplinaVOs);
				vagaTurmaNova.getVagaTurmaDisciplinaVOs().addAll(vagaTurmaDisciplinaVOs);
				vagaTurmaNova.setTurmaVO(agrupadaVO.getTurma());
				getFacadeFactory().getVagaTurmaFacade().persistir(vagaTurmaNova, usuario);
			}
		}
		persistir(obj, usuario);
		
	}

	private void vincularVagaTurmaDisciplina(List<VagaTurmaDisciplinaVO> vagaTurmaDisciplinaVOs,List<VagaTurmaDisciplinaVO> vagaTurmaDisciplinaVOsTurmaBase) {
		for(VagaTurmaDisciplinaVO obj : vagaTurmaDisciplinaVOs) {
			for(VagaTurmaDisciplinaVO vagaTurmaDisciplinaTurmaBase : vagaTurmaDisciplinaVOsTurmaBase) {
				if((obj.getDisciplina().getCodigo().equals(vagaTurmaDisciplinaTurmaBase.getDisciplina().getCodigo())) || (vagaTurmaDisciplinaTurmaBase.getDisciplinaEquivalente())){
					vagaTurmaDisciplinaTurmaBase.setNrVagasMatricula(obj.getNrVagasMatricula());
					vagaTurmaDisciplinaTurmaBase.setNrMaximoMatricula(obj.getNrMaximoMatricula());
					vagaTurmaDisciplinaTurmaBase.setNrVagasMatriculaReposicao(obj.getNrVagasMatriculaReposicao());
					if (vagaTurmaDisciplinaTurmaBase.getDisciplinaEquivalente()) {
						continue;
					} else {
						break;
					}
				}
			}
			
		}
		
	}

	/**
	 * Operaçãoo responsável por alterar no BD os dados de um objeto da classe <code>VagaTurmaVO</code>. Sempre utiliza a chave primï¿½ria da
	 * classe como atributo para localizaçãoo do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexï¿½o com o banco de dados e a permissï¿½o do usuário para realizar esta operacï¿½o na entidade. Isto, atravï¿½s da
	 * operaçãoo <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>VagaTurmaVO</code> que serï¿½ alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexï¿½o, restriçãoo de acesso ou validaçãoo de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final VagaTurmaVO obj, UsuarioVO usuario) throws Exception {
		try {
			VagaTurmaVO.validarDados(obj);
			VagaTurma.alterar(getIdEntidade(), true, usuario);
			validarUnicidadeIdentificadorTurma(obj);
			final String sql = "UPDATE VagaTurma set turma=?, ano=?, semestre=?, dataCadastro=?, usuarioResponsavel=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getTurmaVO().getCodigo());
					sqlAlterar.setString(2, obj.getAno().trim());
					sqlAlterar.setString(3, obj.getSemestre().trim());
					sqlAlterar.setDate(4, Uteis.getDataJDBC(new Date()));
					if (obj.getUsuarioResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getUsuarioResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getVagaTurmaDisciplinaFacade().alterarTurmaDisciplinas(obj, obj.getVagaTurmaDisciplinaVOs());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operaçãoo responsável por excluir no BD um objeto da classe <code>VagaTurmaVO</code>. Sempre localiza o registro a ser excluï¿½do
	 * atravï¿½s da chave primï¿½ria da entidade. Primeiramente verifica a conexï¿½o com o banco de dados e a permissï¿½o do usuário para
	 * realizar esta operacï¿½o na entidade. Isto, atravï¿½s da operaçãoo <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>VagaTurmaVO</code> que serï¿½ removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexï¿½o ou restriçãoo de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(VagaTurmaVO obj, UsuarioVO usuarioVO) throws Exception {
		VagaTurma.excluir(getIdEntidade(), true, usuarioVO);
		String sql = "DELETE FROM VagaTurma WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	/**
	 * responsável por realizar uma consulta de <code>Turma</code> atravï¿½s do valor do atributo <code>Integer codigo</code>. Retorna os objetos
	 * com valores iguais ou superiores ao parï¿½metro fornecido. Faz uso da operaçãoo <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicaçãoo deverï¿½ verificar se o usuário possui permissï¿½o para esta consulta ou não.
	 * @return List Contendo vï¿½rios objetos da classe <code>VagaTurmaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou restriçãoo de acesso.
	 */
	public List<VagaTurmaVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (turma.codigo >= ").append(valorConsulta).append(")");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append("  WHERE (turma.codigo >= ").append(valorConsulta).append(")");
			sqlStr.append("  and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY vagaTurma.codigo ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * responsável por montar os dados de vï¿½rios objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da
	 * operaçãoo <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vï¿½rios objetos da classe <code>VagaTurmaVO</code> resultantes da consulta.
	 */
	public List<VagaTurmaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<VagaTurmaVO> vetResultado = new ArrayList<VagaTurmaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>VagaTurmaVO</code>.
	 * 
	 * @return O objeto da classe <code>VagaTurmaVO</code> com os dados devidamente montados.
	 */
	public VagaTurmaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		VagaTurmaVO obj = new VagaTurmaVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma"));
		obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("turma.identificadorTurma"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setAno(tabelaResultado.getString("ano"));
		obj.setSemestre(tabelaResultado.getString("semestre"));

		obj.setDataCadastro(tabelaResultado.getDate("dataCadastro"));
		obj.getUsuarioResponsavel().setCodigo(tabelaResultado.getInt("usuarioResponsavel"));
		obj.setVagaTurmaDisciplinaVOs(getFacadeFactory().getVagaTurmaDisciplinaFacade().consultarTurmaDisciplinas(obj.getCodigo(), false, nivelMontarDados, usuario));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		return obj;
	}

	/**
	 * Operaçãoo responsável por localizar um objeto da classe <code>VagaTurmaVO</code> atravï¿½s de sua chave primï¿½ria.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou localizaçãoo do objeto procurado.
	 */
	public VagaTurmaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE vagaturma.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados N�o Encontrados (Turma).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public VagaTurmaVO consultarPorChavePrimaria(Integer codTurma, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		VagaTurmaVO obj = new VagaTurmaVO();
		obj.setCodigo(codTurma);
		carregarDados(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operaçãoo reponsï¿½vel por retornar o identificador desta classe. Este identificar ï¿½ utilizado para verificar as permissï¿½es de
	 * acesso as operaçãoes desta classe.
	 */
	public static String getIdEntidade() {
		return VagaTurma.idEntidade;
	}

	/**
	 * Operaçãoo reponsï¿½vel por definir um novo valor para o identificador desta classe. Esta alteraçãoo deve ser possï¿½vel, pois, uma
	 * mesma classe de negï¿½cio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso ï¿½ realizado
	 * com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		VagaTurma.idEntidade = idEntidade;
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT VagaTurma.codigo, VagaTurma.turma, VagaTurma.ano, VagaTurma.semestre, VagaTurma.dataCadastro, VagaTurma.usuarioResponsavel, ");
		sql.append("Turma.codigo as \"Turma.codigo\", Turma.identificadorturma as \"Turma.identificadorturma\", ");
		sql.append("usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\" ");
		sql.append("from vagaturma ");
		sql.append("inner join turma on turma.codigo = vagaTurma.turma ");
		sql.append("left join usuario on usuario.codigo = vagaturma.usuarioResponsavel ");
		return sql;
	}

	private void montarDadosBasico(VagaTurmaVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNivelMontarDados(NivelMontarDados.TODOS);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));

		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		obj.getUsuarioResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getUsuarioResponsavel().setNome(dadosSQL.getString("usuario.nome"));
	}

	private void montarDadosCompleto(VagaTurmaVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		obj.setNivelMontarDados(NivelMontarDados.TODOS);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
		obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));

		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		obj.getUsuarioResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getUsuarioResponsavel().setNome(dadosSQL.getString("usuario.nome"));
		obj.setVagaTurmaDisciplinaVOs(getFacadeFactory().getVagaTurmaDisciplinaFacade().consultarTurmaDisciplinas(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));

	}

	public List<VagaTurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer unidade, String ano, String semestre, String situacaoTurma, String situacaoTipoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE upper( identificadorturma )like('").append(identificador.toUpperCase()).append("%'").append(")");
		if (unidade.intValue() != 0) {
			sqlStr.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND (vagaTurma.ano = '").append(ano).append("') ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND (vagaTurma.semestre = '").append(semestre).append("') ");
		}
		if (!situacaoTurma.isEmpty()) {
			sqlStr.append(" and turma.situacao = '").append(situacaoTurma).append("'");
		}
		if (!situacaoTipoTurma.isEmpty()) {
			if (situacaoTipoTurma.equals("agrupada")) {
				sqlStr.append(" and turma.turmaagrupada = true ");
			}
			if (situacaoTipoTurma.equals("subturma")) {
				sqlStr.append(" and turma.subturma = true ");
			}
			if (situacaoTipoTurma.equals("normal")) {
				sqlStr.append(" and turma.subturma = false and turma.turmaagrupada = false ");
			}
		}
		sqlStr.append(" ORDER BY identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<VagaTurmaVO> consultaRapidaPorCodigoTurma(String valorConsulta, Integer unidade, String ano, String semestre, String situacaoTurma, String situacaoTipoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE vagaturma.codigo = ");
		if (valorConsulta.isEmpty()) {
			valorConsulta = "0";
		}
		sqlStr.append(valorConsulta);
		sqlStr.append("");
		if (unidade.intValue() != 0) {
			sqlStr.append(" AND (unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (!situacaoTurma.isEmpty()) {
			sqlStr.append(" and turma.situacao = '").append(situacaoTurma).append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND (vagaTurma.ano = '").append(ano).append("') ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND (vagaTurma.semestre = '").append(semestre).append("') ");
		}
		if (!situacaoTipoTurma.isEmpty()) {
			if (situacaoTipoTurma.equals("agrupada")) {
				sqlStr.append(" and turma.turmaagrupada = true ");
			}
			if (situacaoTipoTurma.equals("subturma")) {
				sqlStr.append(" and turma.subturma = true ");
			}
			if (situacaoTipoTurma.equals("normal")) {
				sqlStr.append(" and turma.subturma = false and turma.turmaagrupada = false ");
			}
		}
		sqlStr.append(" ORDER BY identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<VagaTurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoECurso(String identificador, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( identificadorturma )like('");
		sql.append(identificador.toUpperCase());
		sql.append("%'");
		sql.append(" )");
		if (curso.intValue() != 0) {
			sql.append(" AND (curso.codigo = ").append(curso.intValue()).append(") ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND (unidadeensino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sql.append(" ORDER BY identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<VagaTurmaVO> consultaRapidaPorIdentificadorTurmaNivelEducacional(String identificador, Integer unidade, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( identificadorturma )like('");
		sql.append(identificador.toUpperCase());
		sql.append("%'");
		sql.append(" )");
		if (unidade.intValue() != 0) {
			sql.append(" AND (unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (!nivelEducacional.equals("")) {
			sql.append(" AND curso.nivelEducacional in ('").append(nivelEducacional.toUpperCase()).append("') ");
		}
		sql.append(" ORDER BY identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public VagaTurmaVO consultaRapidaPorMatriculaPeriodoDadosContaCorrente(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT turma.codigo, turma.identificadorTurma, turma.databasegeracaoparcelas, contaCorrente.codigo AS \"contaCorrente\" ");
		sql.append(" FROM turma ");
		sql.append(" LEFT JOIN contaCorrente ON contaCorrente.codigo = turma.contaCorrente ");
		sql.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.turma = turma.codigo ");
		sql.append(" WHERE matriculaPeriodo.codigo = ").append(matriculaPeriodo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		VagaTurmaVO obj = new VagaTurmaVO();
		if (!tabelaResultado.next()) {
			return new VagaTurmaVO();
		}
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		// obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
		// obj.setDataBaseGeracaoParcelas(tabelaResultado.getDate("databasegeracaoparcelas"));
		// obj.getContaCorrente().setCodigo(tabelaResultado.getInt("contaCorrente"));
		return obj;
	}

	public TurmaDisciplinaVO obterLocalSalaTurmaDisciplinaLog(TurmaDisciplinaVO turmaDisc, Integer turma) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from logturmadisciplina  where turma is not null and localaula <> '' and salalocalAula <> '' ");
		sql.append(" and turma = ").append(turma);
		sql.append(" and disciplina ilike '").append(turmaDisc.getDisciplina().getCodigo()).append("%'");
		sql.append(" order by codigo desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return turmaDisc;
		}
		String local = tabelaResultado.getString("localAula").replaceAll(" ", "");
		String salalocal = tabelaResultado.getString("salalocalAula").replaceAll(" ", "");
		Double avaliacao = tabelaResultado.getDouble("avaliacao");
		turmaDisc.getLocalAula().setCodigo(Integer.parseInt(local.substring(0, local.indexOf("-"))));
		turmaDisc.getSalaLocalAula().setCodigo(Integer.parseInt(salalocal.substring(0, salalocal.indexOf("-"))));
		turmaDisc.setAvaliacao(avaliacao);
		return turmaDisc;
	}

	public VagaTurmaVO consultaRapidaPorIdentificadorTurma(VagaTurmaVO obj, String identificador, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( identificadorturma )like('");
		sql.append(identificador.toUpperCase());
		sql.append("%'");
		sql.append(" )");
		if (unidade.intValue() != 0) {
			sql.append(" AND (unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		sql.append(" ORDER BY identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			throw new Exception("Dados n�o encontrados (Turma)");
		}
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	public VagaTurmaVO consultaRapidaPorIdentificadorTurmaEspecifico(VagaTurmaVO obj, String identificador, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper(identificadorturma) = upper('").append(identificador).append("') ");
		if (unidade.intValue() != 0) {
			sql.append(" AND (unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		sql.append(" ORDER BY identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			throw new Exception("Dados não encontrados (Turma)");
		}
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	@Override
	public VagaTurmaVO consultaRapidaUnicidadeTurmaPorIdentificador(Integer turma, String ano, String semestre, Integer codigo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT VagaTurma.codigo ");
		sql.append("from vagaturma ");
		sql.append(" WHERE turma = ").append(turma).append(" ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and ano = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and semestre = '").append(semestre).append("'");
		}
		sql.append(" and codigo != ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		VagaTurmaVO obj = new VagaTurmaVO();
		if (!tabelaResultado.next()) {
			return obj;
		}
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		// obj.setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
		return obj;
	}

	public List<VagaTurmaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<VagaTurmaVO> vetResultado = new ArrayList<VagaTurmaVO>(0);
		while (tabelaResultado.next()) {
			VagaTurmaVO obj = new VagaTurmaVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<VagaTurmaVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<VagaTurmaVO> vetResultado = new ArrayList<VagaTurmaVO>(0);
		while (tabelaResultado.next()) {
			VagaTurmaVO obj = new VagaTurmaVO();
			montarDadosCompleto(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void carregarDados(VagaTurmaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((VagaTurmaVO) obj, NivelMontarDados.TODOS, usuario);
	}

	public VagaTurmaVO carregarDadosTurmaAgrupada(VagaTurmaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((VagaTurmaVO) obj, NivelMontarDados.BASICO, usuario);
		return obj;
	}

	public void carregarDados(VagaTurmaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico(obj, resultado);
		}
		if (((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) || (nivelMontarDados.equals(NivelMontarDados.FORCAR_RECARGATODOSOSDADOS))) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto(obj, resultado, usuario);
		}
	}

	public SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer turma, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (turma.codigo= '").append(turma).append("')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados n�o Encontrados. (Turma)");
		}
		return tabelaResultado;
	}

	public VagaTurmaVO consultaRapidaPorTurma(Integer turma, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (turma.codigo= '").append(turma).append("')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet((sqlStr.toString()));
		VagaTurmaVO obj = new VagaTurmaVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(obj, tabelaResultado);	
		}
		return obj;

	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer turma, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (turma.codigo= ").append(turma).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados n�o Encontrados (Turma).");
		}
		return tabelaResultado;
	}

	public VagaTurmaVO consultaRapidaPorIdentificadorTurma(VagaTurmaVO obj, String identificador, Integer curso, Integer unidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( identificadorturma )like('");
		sql.append(identificador.toUpperCase());
		sql.append("%'");
		sql.append(" )");
		if (unidade.intValue() != 0) {
			sql.append(" AND (unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (curso.intValue() != 0) {
			sql.append(" AND (curso = ").append(curso.intValue()).append(") ");
		}
		sql.append(" ORDER BY identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			throw new Exception("Dados n�o encontrados (Turma)");
		}
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	@Override
	public void persistir(VagaTurmaVO vagaTurmaVO, UsuarioVO usuarioVO) throws Exception {
		if (vagaTurmaVO.isNovoObj()) {
			incluir(vagaTurmaVO, usuarioVO);
		} else {
			alterar(vagaTurmaVO, usuarioVO);
		}
	}

	@Override
	public List<VagaTurmaVO> consultaRapidaPorCodigoDisciplina(String valorConsulta, Integer unidade, String ano, String semestre, String situacaoTurma, String situacaoTipoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("inner join VagaTurmaDisciplina on VagaTurmaDisciplina.vagaTurma = VagaTurma.codigo ");
		sqlStr.append(" WHERE VagaTurmaDisciplina.disciplina = ");
		if (valorConsulta.isEmpty()) {
			valorConsulta = "0";
		}
		sqlStr.append(valorConsulta);
		if (unidade.intValue() != 0) {
			sqlStr.append(" AND (unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (!situacaoTurma.isEmpty()) {
			sqlStr.append(" and turma.situacao = '").append(situacaoTurma).append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND (vagaTurma.ano = '").append(ano).append("') ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND (vagaTurma.semestre = '").append(semestre).append("') ");
		}
		if (!situacaoTipoTurma.isEmpty()) {
			if (situacaoTipoTurma.equals("agrupada")) {
				sqlStr.append(" and turma.turmaagrupada = true ");
			}
			if (situacaoTipoTurma.equals("subturma")) {
				sqlStr.append(" and turma.subturma = true ");
			}
			if (situacaoTipoTurma.equals("normal")) {
				sqlStr.append(" and turma.subturma = false and turma.turmaagrupada = false ");
			}
		}
		sqlStr.append(" ORDER BY identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<VagaTurmaVO> consultaRapidaPorNomeDisciplina(String valorConsulta, Integer unidade, String ano, String semestre, String situacaoTurma, String situacaoTipoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("inner join VagaTurmaDisciplina on VagaTurmaDisciplina.vagaTurma = VagaTurma.codigo ");
		sqlStr.append("inner join Disciplina on Disciplina.codigo = VagaTurmaDisciplina.disciplina ");
		sqlStr.append("WHERE upper(Disciplina.nome) like('").append(valorConsulta.toUpperCase()).append("%'").append(")");
		if (unidade.intValue() != 0) {
			sqlStr.append(" AND (unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (!situacaoTurma.isEmpty()) {
			sqlStr.append(" and turma.situacao = '").append(situacaoTurma).append("'");
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND (vagaTurma.ano = '").append(ano).append("') ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND (vagaTurma.semestre = '").append(semestre).append("') ");
		}
		if (!situacaoTipoTurma.isEmpty()) {
			if (situacaoTipoTurma.equals("agrupada")) {
				sqlStr.append(" and turma.turmaagrupada = true ");
			}
			if (situacaoTipoTurma.equals("subturma")) {
				sqlStr.append(" and turma.subturma = true ");
			}
			if (situacaoTipoTurma.equals("normal")) {
				sqlStr.append(" and turma.subturma = false and turma.turmaagrupada = false ");
			}
		}
		sqlStr.append(" ORDER BY identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

}
