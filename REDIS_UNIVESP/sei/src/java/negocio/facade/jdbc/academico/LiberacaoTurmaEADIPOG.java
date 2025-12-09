package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.CensoVO;
import negocio.comuns.academico.LiberacaoTurmaEADIPOGVO;
import negocio.comuns.academico.TurmaEADIPOGVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LiberacaoTurmaEADIPOGInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>CensoVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>CensoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CensoVO
 * @see ControleAcesso
 */
@SuppressWarnings({ "unchecked" })
@Repository
@Scope("singleton")
@Lazy
public class LiberacaoTurmaEADIPOG extends ControleAcesso implements LiberacaoTurmaEADIPOGInterfaceFacade {

	protected static String idEntidade;

	public LiberacaoTurmaEADIPOG() throws Exception {
		super();
		setIdEntidade("LiberacaoTurmaEADIPOG");
	}



	public TurmaEADIPOGVO novo() throws Exception {
		LiberacaoTurmaEADIPOG.incluir(getIdEntidade());
		TurmaEADIPOGVO obj = new TurmaEADIPOGVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>CensoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param censo
	 *            Objeto da classe <code>CensoVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TurmaEADIPOGVO turmaEAD, UsuarioVO usuario) throws Exception {
		try {
			LiberacaoTurmaEADIPOG.incluir(getIdEntidade());
			final String sql = "INSERT INTO TurmaEADIPOG( codTurma, identificadorTurma,  dataAtivacao, usuarioResp ) VALUES ( ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			turmaEAD.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, turmaEAD.getCodTurma());
					sqlInserir.setString(2, turmaEAD.getIdentificadorTurma());
					sqlInserir.setDate(3, Uteis.getDataJDBC(new Date()));
					if (turmaEAD.getUsuario().intValue() != 0) {
						sqlInserir.setInt(4, turmaEAD.getUsuario().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						turmaEAD.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			turmaEAD.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			turmaEAD.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>CensoVO</code>. Sempre localiza o registro a ser excluído através
	 * da chave primária da entidade. Primeiramente verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param censo
	 *            Objeto da classe <code>CensoVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TurmaEADIPOGVO turmaEADVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			LiberacaoTurmaEADIPOG.excluir(getIdEntidade());
			String sql = "DELETE FROM TurmaEADIPOG WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { turmaEADVO.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Censo</code> através do
	 * valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CensoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TurmaEADIPOG WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>CensoVO</code>
	 *         resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		TurmaEADIPOGVO obj = null;
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>CensoVO</code>.
	 * 
	 * @return O objeto da classe <code>CensoVO</code> com os dados devidamente
	 *         montados.
	 */
	public static TurmaEADIPOGVO montarDados(SqlRowSet dadosSQL, TurmaEADIPOGVO turmaEADVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		turmaEADVO = new TurmaEADIPOGVO();
		turmaEADVO.setCodigo(dadosSQL.getInt("codigo"));
		turmaEADVO.setCodTurma(dadosSQL.getInt("codTurma"));
		turmaEADVO.setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
		turmaEADVO.setUsuario(dadosSQL.getInt("usuarioResp"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return turmaEADVO;
		}

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return turmaEADVO;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return turmaEADVO;
		}
		return turmaEADVO;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>CensoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public TurmaEADIPOGVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM TurmaEADIPOG WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( TURMAEADIPOG ).");
		}
		TurmaEADIPOGVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}

	public TurmaEADIPOGVO consultarPorTurma(String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {		
		String sql = "SELECT TurmaEADIPOG.* FROM TurmaEADIPOG inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.turma = turmaeadipog.codturma  WHERE matriculaperiodoturmadisciplina.matricula = ? order by matriculaperiodoturmadisciplina.codigo desc limit 1 ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { matricula });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( TURMAEADIPOG ).");
		}
		TurmaEADIPOGVO obj = null;
		return (montarDados(tabelaResultado, obj, nivelMontarDados, usuario));
	}
	
	public List<LiberacaoTurmaEADIPOGVO> consultarTurmas() throws Exception {
		List<LiberacaoTurmaEADIPOGVO> listaLiberacaoTurmaEADVO = new ArrayList<LiberacaoTurmaEADIPOGVO>(0);
		StringBuilder sqlStr = new StringBuilder(0);
		sqlStr.append(" select t.numeromodulo, count(t.turma) as qtdTurmaModulo, sum(qtdAlunoAtivo) as qtdAlunoAtivos, sum(t.qtdTurmaEAD) as qtdTurmaEAD from ( select   horario.datainicio as dataInicio, horario.datatermino as dataFim,  unidadeensino.codigo as unidadeensino,   ");
		sqlStr.append(" unidadeensino.nome as nomeUnidadeEnsino, turma.codigo as turma,  turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina,   ");
		sqlStr.append(" (select count(codigo) from turmaeadipog where turmaeadipog.codturma = turma.codigo) as qtdTurmaEAD, ");
		sqlStr.append(" (SELECT COUNT(distinct matriculaPeriodo.codigo) AS qtde    FROM matriculaPeriodoTurmaDisciplina as mptd   INNER JOIN matriculaPeriodo  ON mptd.matriculaPeriodo = matriculaPeriodo.codigo     ");
		sqlStr.append(" INNER JOIN matricula ON matricula.matricula = matriculaPeriodo.matricula       WHERE (matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'CO')   ");
		sqlStr.append(" AND matricula.unidadeEnsino = unidadeensino.codigo AND matricula.curso = turma.curso AND matriculaPeriodo.turma = turma.codigo   AND mptd.disciplina = horario.disciplina  ) as qtdAlunoAtivo,   horario.numeromodulo from horarioturmaresumido as horario    ");
		sqlStr.append(" inner join turma on turma.codigo = horario.turma   inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino   inner join disciplina on disciplina.codigo = horario.disciplina   inner join curso on curso.codigo = turma.curso   ");
    	sqlStr.append(" where horario.datainicio >= '").append(Uteis.getDataBD0000(Uteis.getDataPrimeiroDiaMes(new Date()))).append("' ");
    	sqlStr.append(" and horario.datainicio <= '").append(Uteis.getDataBD2359(Uteis.getDataUltimoDiaMes(new Date()))).append("' ");
		sqlStr.append(" and curso.niveleducacional = 'PO' group by  unidadeensino.codigo , unidadeensino.nome, turma.codigo , disciplina.codigo , disciplina.nome, turma.curso , horario.turma,   ");
		sqlStr.append(" horario.disciplina, curso.nome, horario.datainicio, horario.datatermino,  horario.numeromodulo    ORDER BY numeromodulo, dataInicio, nomeUnidadeEnsino, identificadorturma  ) as t group by numeromodulo ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			LiberacaoTurmaEADIPOGVO liberacaoTurmaEADVO = new LiberacaoTurmaEADIPOGVO();			
			liberacaoTurmaEADVO.setModulo(tabelaResultado.getInt("numeromodulo"));
			liberacaoTurmaEADVO.setQtdTurmaCursandoEAD(tabelaResultado.getInt("qtdTurmaEAD"));
			liberacaoTurmaEADVO.setQtdTurma(tabelaResultado.getInt("qtdTurmaModulo"));
			liberacaoTurmaEADVO.setQtdAlunosCursandoEAD(tabelaResultado.getInt("qtdAlunoAtivos"));
			listaLiberacaoTurmaEADVO.add(liberacaoTurmaEADVO);
		}
		return listaLiberacaoTurmaEADVO;
	}

	public List<TurmaEADIPOGVO> consultarTurmasPorModulo(Integer modulo) throws Exception {
		List<TurmaEADIPOGVO> listaTurmaEADVO = new ArrayList<TurmaEADIPOGVO>(0);
		StringBuilder sqlStr = new StringBuilder(0);
		sqlStr.append(" select   horario.datainicio as dataInicio, horario.datatermino as dataFim,  unidadeensino.codigo as unidadeensino,   ");
		sqlStr.append(" unidadeensino.nome as nomeUnidadeEnsino, turma.codigo as turma,  turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomeDisciplina,   ");
		sqlStr.append(" (select count(codigo) from turmaeadipog where turmaeadipog.codturma = turma.codigo) as qtdTurmaEAD, ");
		sqlStr.append(" (select dataativacao from turmaeadipog where turmaeadipog.codturma = turma.codigo order by codigo desc limit 1) as dataativacao, ");
		sqlStr.append(" (SELECT COUNT(distinct matriculaPeriodo.codigo) AS qtde    FROM matriculaPeriodoTurmaDisciplina as mptd   INNER JOIN matriculaPeriodo  ON mptd.matriculaPeriodo = matriculaPeriodo.codigo     ");
		sqlStr.append(" INNER JOIN matricula ON matricula.matricula = matriculaPeriodo.matricula       WHERE (matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' OR matriculaPeriodo.situacaoMatriculaPeriodo = 'CO')   ");
		sqlStr.append(" AND matricula.unidadeEnsino = unidadeensino.codigo AND matricula.curso = turma.curso AND matriculaPeriodo.turma = turma.codigo   AND mptd.disciplina = horario.disciplina  ) as qtdAlunoAtivo,   horario.numeromodulo from horarioturmaresumido as horario    ");
		sqlStr.append(" inner join turma on turma.codigo = horario.turma   inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino   inner join cidade on cidade.codigo = unidadeensino.cidade   inner join disciplina on disciplina.codigo = horario.disciplina   inner join curso on curso.codigo = turma.curso   ");
    	sqlStr.append(" where horario.datainicio >= '").append(Uteis.getDataBD0000(Uteis.getDataPrimeiroDiaMes(new Date()))).append("' ");
    	sqlStr.append(" and horario.datainicio <= '").append(Uteis.getDataBD2359(Uteis.getDataUltimoDiaMes(new Date()))).append("' ");
    	sqlStr.append(" and numeromodulo = ").append(modulo).append(" ");
		sqlStr.append(" and curso.niveleducacional = 'PO' group by  unidadeensino.codigo , unidadeensino.nome, turma.codigo , disciplina.codigo , disciplina.nome, turma.curso , horario.turma,   ");
		sqlStr.append(" horario.disciplina, curso.nome, horario.datainicio, horario.datatermino,  horario.numeromodulo    ORDER BY numeromodulo, identificadorturma  , dataInicio, nomeUnidadeEnsino ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			TurmaEADIPOGVO turmaEADVO = new TurmaEADIPOGVO();			
			turmaEADVO.setCodTurma(tabelaResultado.getInt("turma"));
			turmaEADVO.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			turmaEADVO.setQtdAlunos(tabelaResultado.getInt("qtdAlunoAtivo"));
			turmaEADVO.setQtdTurmaEAD(tabelaResultado.getInt("qtdTurmaEAD"));
			if (tabelaResultado.getDate("dataAtivacao") != null) {
				Long dias = UteisData.nrDiasEntreDatas(new Date(),tabelaResultado.getDate("dataAtivacao"));
				turmaEADVO.setQtdDiasAlunosCursandoEAD(dias.intValue());
			} else {
				turmaEADVO.setQtdDiasAlunosCursandoEAD(0);
			}
			listaTurmaEADVO.add(turmaEADVO);
		}
		return listaTurmaEADVO;
	}
	
	public static String getIdEntidade() {
		return LiberacaoTurmaEADIPOG.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		LiberacaoTurmaEADIPOG.idEntidade = idEntidade;
	}


}