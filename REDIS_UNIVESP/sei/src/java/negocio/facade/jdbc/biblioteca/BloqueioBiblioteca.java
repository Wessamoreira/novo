package negocio.facade.jdbc.biblioteca;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.BloqueioBibliotecaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.BloqueioBibliotecaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>BloqueioBibliotecaVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>BloqueioBibliotecaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see BloqueioBibliotecaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class BloqueioBiblioteca extends ControleAcesso implements BloqueioBibliotecaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7233319858465077185L;
	protected static String idEntidade;

	public BloqueioBiblioteca() throws Exception {
		super();
		setIdEntidade("BloqueioBiblioteca");
	}

	public void inicializarDadosBloqueioBibliotecaNovo(BloqueioBibliotecaVO bloqueioBibliotecaVO, UsuarioVO usuario) throws Exception {
		bloqueioBibliotecaVO.setData(new Date());
		bloqueioBibliotecaVO.setAtendente(usuario);
	}

	public BloqueioBibliotecaVO novo() throws Exception {
		BloqueioBiblioteca.incluir(getIdEntidade());
		BloqueioBibliotecaVO obj = new BloqueioBibliotecaVO();
		return obj;
	}

	/**
	 * Verifica se a pessoa tem um bloqueio na biblioteca, o que impossibilita de realizar algumas ações.
	 * 
	 * @param codPessoa
	 * @throws Exception
	 */
        @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarBloqueioBiblioteca(Integer codPessoa, Integer biblioteca, String tipoPessoa, UsuarioVO usuarioVO) throws Exception {		
		String sqlStr = "SELECT pessoa.nome as pessoa FROM bloqueioBiblioteca inner join Pessoa on pessoa.codigo = bloqueioBiblioteca.pessoa WHERE pessoa.codigo = " + codPessoa + " and biblioteca = " + biblioteca+"  and (dataLimiteBloqueio is null or dataLimiteBloqueio >= current_date) limit 1 ";
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);		
			if (tabelaResultado.next()) {
				if(usuarioVO.getIsApresentarVisaoAluno() || usuarioVO.getIsApresentarVisaoCoordenador() || usuarioVO.getIsApresentarVisaoProfessor()){
					throw new ConsistirException("Você possui bloqueio registrado nesta biblioteca, por isto não é possível realizar empréstimos/reserva.");
				}else{
					if(Uteis.isAtributoPreenchido(tipoPessoa) && TipoPessoa.getEnum(tipoPessoa) != null){
						TipoPessoa tipoPessoa2 = TipoPessoa.getEnum(tipoPessoa);
						throw new ConsistirException("O(a)  "+tipoPessoa2.getDescricao()+"(a) possui bloqueio(s) registrado(s) nesta biblioteca, por isto não é possível realizar empréstimos/reserva.");
					}else{
						throw new ConsistirException("A pessoa "+tabelaResultado.getString("pessoa")+" possui bloqueio(s) registrados nesta biblioteca, por isto não é possível realizar empréstimos/reserva.");
					}
				}
			}
		} finally {			
			sqlStr = null;
		}
	}

	/**
	 * Verifica se a pessoa já tem um bloqueio na biblioteca.
	 * 
	 * @param bloqueioBibliotecaVO
	 * @throws Exception
	 */
     public Boolean verificarBloqueioExistente(Integer pessoa) throws Exception {
		String sqlStr = "SELECT COUNT (bloqueioBiblioteca.codigo) as qteBloqueios FROM bloqueioBiblioteca WHERE pessoa = " + pessoa + " and (dataLimiteBloqueio is null or dataLimiteBloqueio >= current_date) ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		
		if(tabelaResultado.next() && tabelaResultado.getInt("qteBloqueios") > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final BloqueioBibliotecaVO obj, final UsuarioVO usuarioVO) throws Exception {
		if(obj.getNovoObj()){
			incluir(obj, usuarioVO);
		}else{
			alterar(obj, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final BloqueioBibliotecaVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			BloqueioBiblioteca.incluir(getIdEntidade(), true, usuarioVO);
			BloqueioBibliotecaVO.validarDados(obj);
			final String sql = "INSERT INTO BloqueioBiblioteca( data, atendente, biblioteca, pessoa, tipoPessoa, motivoBloqueio, dataLimiteBloqueio, matricula ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
					if (obj.getAtendente().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getAtendente().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getBiblioteca().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getBiblioteca().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getPessoa().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getTipoPessoa());
					sqlInserir.setString(6, obj.getMotivoBloqueio());
					sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataLimiteBloqueio()));
					if(obj.getMatricula().getMatricula() != null && !obj.getMatricula().getMatricula().trim().isEmpty()){
						sqlInserir.setString(8, obj.getMatricula().getMatricula());
					}else{
						sqlInserir.setNull(8, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if(arg0.next()){
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final BloqueioBibliotecaVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			BloqueioBiblioteca.alterar(getIdEntidade(), true, usuarioVO);
			BloqueioBibliotecaVO.validarDados(obj);
			final String sql = "update bloqueiobiblioteca  set motivoBloqueio = ? , dataLimiteBloqueio = ?  where codigo =  ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getMotivoBloqueio());
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataLimiteBloqueio()));
					sqlInserir.setInt(3, obj.getCodigo());
					return sqlInserir;
				}
			});
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {			
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(BloqueioBibliotecaVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			BloqueioBiblioteca.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM BloqueioBiblioteca WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List<BloqueioBibliotecaVO> consultarPorNomeBiblioteca(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());
		sqlStr.append(" where ").append(" upper(sem_acentos( Biblioteca.nome )) like(sem_acentos('").append(valorConsulta.toUpperCase()).append("%')) ORDER BY Biblioteca.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public List<BloqueioBibliotecaVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());
		sqlStr.append(" where ").append(realizarGeracaoWherePeriodo(prmIni, prmFim, "BloqueioBiblioteca.data", false));		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}
	
	public StringBuilder getSqlConsultaCompleta(){
		StringBuilder sqlStr = new StringBuilder("SELECT distinct BloqueioBiblioteca.*, pessoa.nome as pessoa_nome, atendente.nome as atendente_nome, biblioteca.nome as biblioteca_nome, ");
		sqlStr.append(" curso.nome as curso_nome, turno.nome as turno_nome ");
		sqlStr.append(" FROM BloqueioBiblioteca  ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = BloqueioBiblioteca.pessoa ");
		sqlStr.append(" inner join usuario as atendente on atendente.codigo = BloqueioBiblioteca.atendente ");
		sqlStr.append(" inner join biblioteca on biblioteca.codigo = BloqueioBiblioteca.biblioteca ");
		sqlStr.append(" left join matricula on matricula.matricula = BloqueioBiblioteca.matricula ");
		sqlStr.append(" left join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" left join turno on matricula.turno = turno.codigo ");
		return sqlStr;
	}

	public List<BloqueioBibliotecaVO> consultarPorNomePessoa(String pessoa, Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso,  int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());		
		sqlStr.append(" WHERE sem_acentos(pessoa.nome) ilike sem_acentos('").append(pessoa).append("%') ");
		sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(prmIni, prmFim, "BloqueioBiblioteca.data", false));
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sqlStr.append(" and exists (select unidadeensino from unidadeensinobiblioteca where unidadeensinobiblioteca.biblioteca =  	BloqueioBiblioteca.biblioteca ");
			sqlStr.append(" and unidadeensinobiblioteca.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		sqlStr.append(" order by pessoa_nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}
	
	@Override
	public List<BloqueioBibliotecaVO> consultarPorCodigoPessoa(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso,  int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());		
		sqlStr.append(" WHERE pessoa.codigo = ").append(pessoa);		
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sqlStr.append(" and exists (select unidadeensino from unidadeensinobiblioteca where unidadeensinobiblioteca.biblioteca =  	BloqueioBiblioteca.biblioteca ");
			sqlStr.append(" and unidadeensinobiblioteca.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		sqlStr.append(" order by data desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}
	
	public List<BloqueioBibliotecaVO> consultarPorMatricula(String matricula, Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());		
		sqlStr.append(" WHERE matricula.matricula = '").append(matricula).append("' ");
		sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(prmIni, prmFim, "BloqueioBiblioteca.data", false));
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sqlStr.append(" and exists (select unidadeensino from unidadeensinobiblioteca where unidadeensinobiblioteca.biblioteca =  	BloqueioBiblioteca.biblioteca ");
			sqlStr.append(" and unidadeensinobiblioteca.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		sqlStr.append(" order by data ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public static List<BloqueioBibliotecaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<BloqueioBibliotecaVO> vetResultado = new ArrayList<BloqueioBibliotecaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return vetResultado;
	}

	public static BloqueioBibliotecaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		BloqueioBibliotecaVO obj = new BloqueioBibliotecaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setMotivoBloqueio(dadosSQL.getString("motivoBloqueio"));
		obj.setDataLimiteBloqueio(dadosSQL.getDate("dataLimiteBloqueio"));
		obj.setData(dadosSQL.getDate("data"));
		obj.getAtendente().setCodigo(dadosSQL.getInt("atendente"));
		obj.getBiblioteca().setCodigo(dadosSQL.getInt("biblioteca"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		obj.getPessoa().setNome(dadosSQL.getString("pessoa_nome"));
		obj.getBiblioteca().setNome(dadosSQL.getString("biblioteca_nome"));
		obj.getAtendente().setNome(dadosSQL.getString("atendente_nome"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatricula().getCurso().setNome(dadosSQL.getString("curso_nome"));
		obj.getMatricula().getTurno().setNome(dadosSQL.getString("turno_nome"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		
		return obj;
	}

	public static void montarDadosPessoa(BloqueioBibliotecaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosBiblioteca(BloqueioBibliotecaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBiblioteca().getCodigo().intValue() == 0) {
			obj.setBiblioteca(new BibliotecaVO());
			return;
		}
		obj.setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(obj.getBiblioteca().getCodigo(),
				nivelMontarDados, usuario));
	}

	public static void montarDadosAtendente(BloqueioBibliotecaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getAtendente().getCodigo().intValue() == 0) {
			obj.setAtendente(new UsuarioVO());
			return;
		}
		obj.setAtendente(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getAtendente().getCodigo(), nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return BloqueioBiblioteca.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		BloqueioBiblioteca.idEntidade = idEntidade;
	}
	
	public List<BloqueioBibliotecaVO> consultar(Integer funcionarioSolicitante, Integer biblioteca, Date dataInicioDataBloqueio, Date dataFimDataBloqueio, 
				Date dataInicioDataLimiteBloqueio, Date dataFimDataLimiteBloqueio, int nivelMontarDados, Integer limit, Integer offset, UsuarioVO usuario) throws Exception{
		
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());		
		sqlStr.append(" left join pessoa pessoa_atendente on pessoa_atendente.codigo = atendente.pessoa ");	
		sqlStr.append(" where 1 = 1 ");
		
		if(funcionarioSolicitante != null && funcionarioSolicitante > 0) {
			sqlStr.append(" and pessoa_atendente.codigo=" + funcionarioSolicitante);
		}
		
		if(dataInicioDataBloqueio != null && dataFimDataBloqueio != null) {
			sqlStr.append(" and BloqueioBiblioteca.data between '" +  Uteis.getDataJDBC(dataInicioDataBloqueio) + "' and '" + Uteis.getDataJDBC(dataFimDataBloqueio) + "'");
		}
		
		if(dataInicioDataLimiteBloqueio != null && dataFimDataLimiteBloqueio != null) {
			sqlStr.append(" and BloqueioBiblioteca.datalimitebloqueio between '" +  Uteis.getDataJDBC(dataInicioDataLimiteBloqueio) + "' and '" + Uteis.getDataJDBC(dataFimDataLimiteBloqueio) + "'");
		}
		
		if(biblioteca != null && biblioteca > 0) {
			sqlStr.append(" and  BloqueioBiblioteca.biblioteca = ").append(biblioteca);
		}
		
		if(limit != null && limit > 0) {
			sqlStr.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
		
	}
	
	public Integer consultarQuantidadeRegistros(Integer pessoaSolicitante, Integer biblioteca, Date dataInicioDataBloqueio, Date dataFimDataBloqueio, Date dataInicioDataLimiteBloqueio, Date dataFimDataLimiteBloqueio, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		StringBuilder sqlStr = new StringBuilder("select count(*) as qtde from (");
		sqlStr.append(getSqlConsultaCompleta());
		sqlStr.append(" left join pessoa pessoa_atendente on pessoa_atendente.codigo = atendente.pessoa ");	
		sqlStr.append(" where 1 = 1 ");
		
		if(pessoaSolicitante != null && pessoaSolicitante > 0) {
			sqlStr.append(" and pessoa_atendente.codigo=" + pessoaSolicitante);
		}
		
		if(dataInicioDataBloqueio != null && dataFimDataBloqueio != null) {
			sqlStr.append(" and BloqueioBiblioteca.data between '" +  Uteis.getDataJDBC(dataInicioDataBloqueio) + "' and '" + Uteis.getDataJDBC(dataFimDataBloqueio) + "'");
		}
		
		if(dataInicioDataLimiteBloqueio != null && dataFimDataLimiteBloqueio != null) {
			sqlStr.append(" and BloqueioBiblioteca.datalimitebloqueio between '" +  Uteis.getDataJDBC(dataInicioDataLimiteBloqueio) + "' and '" + Uteis.getDataJDBC(dataFimDataLimiteBloqueio) + "'");
		}
		
		if(biblioteca != null && biblioteca > 0) {
			sqlStr.append(" and  BloqueioBiblioteca.biblioteca = ").append(biblioteca);
		}
		
		sqlStr.append(") as t ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		if(tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		
		return 0;
	}
	
	
}
