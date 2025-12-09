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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.biblioteca.ReservaVO;
import negocio.comuns.biblioteca.enumeradores.FormatoImpressaoEnum;
import negocio.comuns.biblioteca.enumeradores.SituacaoReservaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.ReservaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ReservaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ReservaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ReservaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Reserva extends ControleAcesso implements ReservaInterfaceFacade {

	protected static String idEntidade;

	public Reserva() throws Exception {
		super();
		setIdEntidade("Reserva");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ReservaVO</code>.
	 */
	public ReservaVO novo() throws Exception {
		Reserva.incluir(getIdEntidade());
		ReservaVO obj = new ReservaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirReservas(List<ReservaVO> reservaVOs, UsuarioVO usuario) throws Exception {
		try {
			for (ReservaVO reservaVO : reservaVOs) {
				incluir(reservaVO, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ReservaVO obj, UsuarioVO usuario) throws Exception {
		incluir(obj, true, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ReservaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			Reserva.incluir(getIdEntidade(), verificarAcesso, usuario);

			final String sql = "INSERT INTO Reserva( pessoa, catalogo, dataReserva, dataTerminoReserva, emprestimo, tipoPessoa, situacao, matricula, biblioteca ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo "  + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario) ;
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getPessoa().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getPessoa().getCodigo());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getCatalogo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getCatalogo().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataReserva()));
					if (obj.getDataTerminoReserva() != null) {
						sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataTerminoReserva()));
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (obj.getEmprestimo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getEmprestimo());
					} else {
						sqlInserir.setNull(5, 0);
					}
                    sqlInserir.setString(6, obj.getTipoPessoa());
                    sqlInserir.setString(7, obj.getSituacao());
                    sqlInserir.setString(8, obj.getMatricula());
					if (obj.getBibliotecaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(9, obj.getBibliotecaVO().getCodigo());
					} else {
						sqlInserir.setNull(9, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

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
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Monta um objeto de reserva de acordo com o catálogo passado como parâmetro, a pessoa, a data corrente e o prazo
	 * de validade da reserva para calcular a data de término. Depois de montado, inclui no banco.
	 * 
	 * @param catalogo
	 * @param pessoa
	 * @param prazoValidadeReserva
	 * @return reserva
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	public ReservaVO montarReserva(CatalogoVO catalogo, PessoaVO pessoa, Date prazoValidadeReserva, String tipoPessoa, BibliotecaVO biblioteca, String matricula, UsuarioVO usuario) throws Exception {
		ReservaVO reserva = new ReservaVO();
		reserva.setCatalogo(catalogo);
		reserva.setPessoa(pessoa);
		reserva.setDataReserva(new Date());
		reserva.setBibliotecaVO(biblioteca);
		reserva.setMatricula(matricula);
		reserva.setDataTerminoReserva(prazoValidadeReserva);
		reserva.setSituacao(SituacaoReservaEnum.EM_EXECUCAO.getKey());
        if(tipoPessoa.equals("")){
            tipoPessoa = usuario.getTipoUsuario();
        }
        reserva.setTipoPessoa(tipoPessoa);
		try {
			incluir(reserva, false, usuario);
		} finally {
			//reserva = null;
		}
		return reserva;
	}

	/**
	 * Método que verifica se já existe uma reserva de um determinado catálogo para uma pessoa no período de reserva
	 * corrente.
	 * 
	 * @param codigoCatalogo
	 * @param codigoPessoa
	 * @author Murillo Parreira
	 */
	public Boolean verificarExistenciaReservaCatalogoParaDeterminadaPessoa(Integer codigoCatalogo, Integer codigoPessoa, Integer biblioteca) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT (COUNT(codigo) > 0) AS resultado FROM reserva WHERE catalogo = " + codigoCatalogo + " ");
		sqlStr.append("AND pessoa = " + codigoPessoa + " AND emprestimo IS NULL AND (dataterminoreserva is null or dataterminoreserva > current_timestamp) AND situacao not in ('CA', 'FI', 'EM') ");
		if(Uteis.isAtributoPreenchido(biblioteca)){
			sqlStr.append(" and reserva.biblioteca = ").append(biblioteca);
		}
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				return false;
			}
			return tabelaResultado.getBoolean("resultado");
		} finally {
			sqlStr = null;
		}
	}

	/**
	 * Método que pega o número de reservas de uma determinada pessoa no período de reserva corrente.
	 * 
	 * @param codigoPessoa
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	public Integer consultarNumeroDeCatalogosReservadosParaDeterminadaPessoa(Integer codigoPessoa) throws Exception {
		String sqlStr = "SELECT COUNT(codigo) FROM reserva WHERE pessoa = " + codigoPessoa + " AND (dataterminoreserva is null or dataterminoreserva >= current_timestamp) AND emprestimo IS NULL AND situacao not in ('CA', 'FI', 'EM') ";
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			if (!tabelaResultado.next()) {
				return 0;
			}
			return tabelaResultado.getInt("count");
		} finally {
			sqlStr = null;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ReservaVO</code>. Sempre localiza o registro a
	 * ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ReservaVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ReservaVO obj) throws Exception {
		try {
			Reserva.excluir(getIdEntidade());
			String sql = "DELETE FROM Reserva WHERE codigo=?";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ReservaVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}
	
	public static List montarDadosConsultaEmprestimo(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, true, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static ReservaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return montarDados(dadosSQL, nivelMontarDados, false, usuario);
	}
	
	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>ReservaVO</code>.
	 * 
	 * @return O objeto da classe <code>ReservaVO</code> com os dados devidamente montados.
	 */
	public static ReservaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, Boolean montarDadosTelaEmprestimo, UsuarioVO usuario) throws Exception {
		ReservaVO obj = new ReservaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.getCatalogo().setCodigo(dadosSQL.getInt("catalogo"));
		obj.setDataReserva(dadosSQL.getTimestamp("dataReserva"));
		obj.setDataTerminoReserva(dadosSQL.getTimestamp("dataTerminoReserva"));
		obj.setEmprestimo(dadosSQL.getInt("emprestimo"));
		obj.setTipoPessoa(dadosSQL.getString("tipopessoa"));
		obj.getBibliotecaVO().setCodigo(dadosSQL.getInt("biblioteca"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		if (montarDadosTelaEmprestimo && dadosSQL.getInt("nrexemplaresdisponiveis") >= 0) {
			obj.setNrExemplaresDisponiveisDesseCatalogo(dadosSQL.getInt("nrexemplaresdisponiveis"));
		}
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		montarDadosCatalogo(obj, nivelMontarDados, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		montarDadosBiblioteca(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosPessoa(obj, nivelMontarDados, usuario);
		return obj;
	}
	
	public static void montarDadosBiblioteca(ReservaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBibliotecaVO().getCodigo() == 0) {
			obj.setBibliotecaVO(new BibliotecaVO());
			return;
		}
		obj.setBibliotecaVO(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(obj.getBibliotecaVO().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosPessoa(ReservaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosCatalogo(ReservaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCatalogo().getCodigo().intValue() == 0) {
			obj.setCatalogo(new CatalogoVO());
			return;
		}
		obj.setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCatalogo().getCodigo(), nivelMontarDados, 0, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ReservaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ReservaVO consultarPorChavePrimaria(Integer codigoReserva, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Reserva WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoReserva });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Reserva ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return Reserva.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Reserva.idEntidade = idEntidade;
	}

	/**
	 * Método que consulta na base todas as reservas de uma pessoa pelo código da mesma.
	 * 
	 * @param codigoPessoa
	 * @param nivelMontarDados
	 * @return List<ReservaVO>
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	public List<ReservaVO> consultarReservasPorCodigoPessoa(Integer codigoPessoa, BibliotecaVO bibliotecaVO, ConfiguracaoBibliotecaVO configuracaoBibliotecaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT *, (SELECT COUNT(e.codigo) FROM exemplar e INNER JOIN catalogo c ON e.catalogo = c.codigo ");
		sqlStr.append("WHERE e.situacaoAtual = 'DI' and desconsiderarReserva = 'f' and e.catalogo = r.catalogo and e.biblioteca = r.biblioteca ");
		if(Uteis.isAtributoPreenchido(bibliotecaVO)){
			sqlStr.append(" and e.biblioteca = ").append(bibliotecaVO.getCodigo());			
		}
		Integer diaDaSemana = Uteis.getDiaSemana(new Date());
		if (diaDaSemana != 6 && diaDaSemana != 7 && diaDaSemana != 1 && !getFacadeFactory().getFeriadoFacade().validarDataSeVesperaFimDeSemana(new Date(), Uteis.isAtributoPreenchido(bibliotecaVO) ? bibliotecaVO.getCidade().getCodigo() : 0, Uteis.isAtributoPreenchido(configuracaoBibliotecaVO) ? configuracaoBibliotecaVO.getConsiderarSabadoDiaUtil() : false, Uteis.isAtributoPreenchido(configuracaoBibliotecaVO) ?  configuracaoBibliotecaVO.getConsiderarDomingoDiaUtil() : false, ConsiderarFeriadoEnum.BIBLIOTECA)) {
			sqlStr.append(" and ((paraConsulta = false and emprestarSomenteFinalDeSemana = false) or (emprestarSomenteFinalDeSemana = true and EXTRACT(DOW FROM current_date) in (0,5,6)))");			
		}else{
			sqlStr.append(" and ((paraConsulta = false and emprestarSomenteFinalDeSemana = false) or (emprestarSomenteFinalDeSemana = true))");
		}		
		sqlStr.append(" ) ");
		if(!usuario.getIsApresentarVisaoAdministrativa()){
			sqlStr.append(" - (select count(reserva.codigo) from reserva where reserva.catalogo = r.catalogo and reserva.biblioteca = r.biblioteca ");
			sqlStr.append(" and reserva.codigo < r.codigo  AND reserva.emprestimo IS NULL AND (reserva.situacao not in ('CA', 'FI', 'EM') or reserva.situacao is null) ");
			sqlStr.append(" and (reserva.dataTerminoReserva is null or reserva.dataTerminoReserva >= current_timestamp ) ) ");
		}
		sqlStr.append(" AS nrExemplaresDisponiveis ");
		sqlStr.append(" FROM reserva r WHERE pessoa = ? ");
		if(Uteis.isAtributoPreenchido(bibliotecaVO)){
			sqlStr.append(" and r.biblioteca = ").append(bibliotecaVO.getCodigo());
		}
		sqlStr.append(" AND emprestimo IS NULL AND (situacao not in ('CA', 'FI', 'EM') or situacao is null) ");
		sqlStr.append(" and (r.dataTerminoReserva is null or r.dataTerminoReserva >= current_timestamp ) ");
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoPessoa });
			return montarDadosConsultaEmprestimo(tabelaResultado, nivelMontarDados, usuario);
		} finally {
			sqlStr = null;
		}
	}

	/**
	 * Neste método verificamos se existe alguma reserva para determinado catálogo. Se o número de reservas for maior ou igual
	 *  ao número de exemplares do mesmo catálogo, e uma dessas reservas não é do estudante em questão, então o sistema avisa 
	 *  ao usuário que os exemplares desse catálogo estão reservados. Por outro lado, se o número de reservas for menor que o 
	 *  número de exemplares do catálogo ou se essa reserva estiver no nome do estudante, então terminamos o método e continuamos
	 *  com o resto da operação no controlador.
	 *  @param codigoCatalogo
	 *  @param codigoPessoa
	 *  @throws Exception
	 *  @author Murillo Parreira
	 */
	public void verificarPossibilidadeEmprestimoCatalogoComBaseNoNumeroDeReservas(Integer codigoCatalogo, Integer codigoPessoa, Integer biblioteca)
			throws Exception {
		Integer nrReservasCatalogo = 0;
		Integer nrExemplaresDisponiveisDeUmCatalogo = 0;
		Boolean existeReservaDaPessoaParaUmCatalogo = false;
		try {
			nrReservasCatalogo = consultarReservasPorCodigoCatalogo(codigoCatalogo);
			nrExemplaresDisponiveisDeUmCatalogo = getFacadeFactory().getCatalogoFacade().consultarNumeroDeExemplaresDisponiveisPorCatalogo(codigoCatalogo);
			if (nrReservasCatalogo >= nrExemplaresDisponiveisDeUmCatalogo) {
				existeReservaDaPessoaParaUmCatalogo = verificarExistenciaReservaCatalogoParaDeterminadaPessoa(codigoCatalogo, codigoPessoa, biblioteca);
				if (!existeReservaDaPessoaParaUmCatalogo) {
					throw new Exception(
							"Esse exemplar deste catálogo não pode ser emprestado pois existem reservas ativas para o mesmo!");
				}
			}
		} finally {
			nrExemplaresDisponiveisDeUmCatalogo = null;
			nrExemplaresDisponiveisDeUmCatalogo = null;
			existeReservaDaPessoaParaUmCatalogo = null;
		}
	}

	public Integer consultarReservasPorCodigoCatalogo(Integer codigoCatalogo) throws Exception {
		String sqlStrNrReservas = "SELECT COUNT(codigo) FROM reserva WHERE catalogo = ? AND dataterminoreserva > current_timestamp AND emprestimo IS NULL";
		try {
			SqlRowSet tabelaResultadoNrReservas = getConexao().getJdbcTemplate().queryForRowSet(sqlStrNrReservas,
					new Object[] { codigoCatalogo });
			tabelaResultadoNrReservas.next();
			return tabelaResultadoNrReservas.getInt("count");
		} finally {
			sqlStrNrReservas = null;
		}
	}
	
	/**
	 * Método que atualiza o registro de uma reserva. Ele vincula um código de empréstimo a ela, quando a reserva que a pessoa
	 * fez foi efetivada pela tela de empréstimo. Esse código de empréstimo na reserva, serve para mostrar que agora aquela reserva
	 * não está mais ativa.
	 * @param codigoEmprestimo
	 * @param codigoPessoa
	 * @param codigoCatalogo
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	public void executarVinculoDaReservaParaUmEmprestimo(Integer codigoEmprestimo, Integer codigoPessoa, Integer codigoCatalogo) throws Exception {
		String sqlStr= "UPDATE reserva SET emprestimo = ?, situacao = 'EM' WHERE pessoa = ? AND catalogo = ?";
		try {
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { codigoEmprestimo, codigoPessoa, codigoCatalogo });
		} finally {
			sqlStr = null;
		}
	}
	
	public Integer consultarNumeroDeExemplaresDisponiveisPorCatalogo(CatalogoVO catalogoVO, BibliotecaVO bibliotecaVO, ConfiguracaoBibliotecaVO confBibVO, Boolean validarFeriado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select count(exemplar.codigo) from exemplar ");
		sqlStr.append("inner join catalogo on catalogo.codigo = exemplar.catalogo ");
		sqlStr.append("where exemplar.situacaoatual = 'DI' and catalogo.codigo = ").append(catalogoVO.getCodigo());
		if(Uteis.isAtributoPreenchido(bibliotecaVO)){
			sqlStr.append(" and exemplar.biblioteca = ").append(bibliotecaVO.getCodigo());
		}
		
		if(validarFeriado != null && validarFeriado) {
			Integer diaDaSemana = Uteis.getDiaSemana(new Date());
			if (diaDaSemana != 6 && diaDaSemana != 7 && diaDaSemana != 1 && !getFacadeFactory().getFeriadoFacade().validarDataSeVesperaFimDeSemana(new Date(), Uteis.isAtributoPreenchido(bibliotecaVO) ? bibliotecaVO.getCidade().getCodigo() : 0, confBibVO.getConsiderarSabadoDiaUtil(), confBibVO.getConsiderarDomingoDiaUtil(), ConsiderarFeriadoEnum.BIBLIOTECA)) {				
				sqlStr.append(" and ((paraConsulta = false and emprestarSomenteFinalDeSemana = false) or (emprestarSomenteFinalDeSemana = true and EXTRACT(DOW FROM current_date) in (0, 5, 6)))");				
			}else {
				sqlStr.append(" and ((paraConsulta = false and emprestarSomenteFinalDeSemana = false) or (emprestarSomenteFinalDeSemana = true))");
			}
		}else {
			sqlStr.append(" and ((paraConsulta = false and emprestarSomenteFinalDeSemana = false) or (emprestarSomenteFinalDeSemana = true and EXTRACT(DOW FROM current_date) in (0, 5, 6)))");			
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}
	
	public Integer consultarQuantidadeReservaEmAbertoPorCatalogoPessoa(Integer catalogo, Integer pessoa, Integer reserva) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT count(codigo) FROM reserva WHERE emprestimo IS NULL ");
		sqlStr.append(" AND catalogo = ").append(catalogo);
		sqlStr.append(" AND (situacao not in ('CA', 'FI', 'EM') or situacao is null) ");
		sqlStr.append(" AND pessoa not in (").append(pessoa).append(")");
		sqlStr.append(" AND codigo < ").append(reserva).append("");
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				return 0;
			}
			return tabelaResultado.getInt("count");
		} finally {
			sqlStr = null;
		}
	}
	/*
	 * Método que consulta a quantidade de reservas validas com base nas anteriores realizadas.
	 * 
	 * Para uma reserva ser valida a data de termino tem que ser maior ou igual a data atual ou então nula e não pode está vinculada a um emprestimo.
	 */
	public Integer consultarQuantidadeDeReservasValidasPorCatalogo(CatalogoVO catalogoVO, BibliotecaVO bibliotecaVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select count(reserva.codigo) from reserva");
		sqlStr.append(" inner join catalogo on catalogo.codigo = reserva.catalogo ");
		sqlStr.append(" where reserva.emprestimo is null ");
		sqlStr.append(" and (reserva.dataterminoreserva >= current_timestamp or reserva.dataterminoreserva is null) and situacao not in ('CA', 'FI', 'EM') ");
		sqlStr.append(" and catalogo.codigo = ").append(catalogoVO.getCodigo());
		if(Uteis.isAtributoPreenchido(bibliotecaVO)){
			sqlStr.append(" and reserva.biblioteca = ").append(bibliotecaVO.getCodigo());
		}
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				return 0;
			}
			return tabelaResultado.getInt("count");
		} finally {
			sqlStr = null;
		}
	}
	
	public List<ReservaVO> consultarReservasPorCatalogoPessoa(CatalogoVO catalogoVO, BibliotecaVO bibliotecaVO, PessoaVO pessoaVO, UsuarioVO usuario, Integer limit) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select * from reserva ");
		sqlStr.append(" where emprestimo is null and (dataterminoreserva is null ");
		sqlStr.append(" or dataterminoreserva >= current_timestamp) and situacao not in ('CA', 'FI', 'EM') ");
		if (catalogoVO != null && catalogoVO.getCodigo() > 0) {
			sqlStr.append(" and catalogo = ").append(catalogoVO.getCodigo());
		}
		if (pessoaVO != null && pessoaVO.getCodigo() > 0) {
			sqlStr.append(" and pessoa = ").append(pessoaVO.getCodigo());
		}
		if (bibliotecaVO != null && bibliotecaVO.getCodigo() > 0) {
			sqlStr.append(" and biblioteca = ").append(bibliotecaVO.getCodigo());
		}
		sqlStr.append(" order by datareserva asc ");
		if(Uteis.isAtributoPreenchido(limit)){
			sqlStr.append(" limit ").append(limit);
		}
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		List<ReservaVO> reservaVOs = new ArrayList<ReservaVO>(0);
		ReservaVO obj = null;
		while (dadosSQL.next()) {
			obj = new ReservaVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
			obj.getBibliotecaVO().setCodigo(dadosSQL.getInt("biblioteca"));
			obj.getCatalogo().setCodigo(dadosSQL.getInt("catalogo"));
			obj.setDataReserva(dadosSQL.getTimestamp("dataReserva"));
			obj.setDataTerminoReserva(dadosSQL.getTimestamp("dataTerminoReserva"));
			obj.setEmprestimo(dadosSQL.getInt("emprestimo"));
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			reservaVOs.add(obj);
			obj = null;
		}
		return reservaVOs;
	}
	
	public List<ReservaVO> consultarReservasVencidasPorCatalogoPessoa(CatalogoVO catalogoVO, PessoaVO pessoaVO, ConfiguracaoBibliotecaVO conBibliotecaVO, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM ( ");
		sqlStr.append(" 	SELECT *, ( ");
		sqlStr.append(" 		SELECT configuracaobiblioteca.prazovalidadereservacatalogosindisponiveis FROM biblioteca ");
		sqlStr.append(" 		INNER JOIN ( ");
		sqlStr.append(" 			SELECT 1 AS ordem, configuracaobibliotecaniveleducacional.configuracaobiblioteca, configuracaobibliotecaniveleducacional.biblioteca FROM matricula ");
		sqlStr.append(" 			INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" 			INNER JOIN configuracaobibliotecaniveleducacional ON configuracaobibliotecaniveleducacional.biblioteca = reserva.biblioteca ");
		sqlStr.append(" 			AND configuracaobibliotecaniveleducacional.unidadeensino = matricula.unidadeensino ");
		sqlStr.append("				AND configuracaobibliotecaniveleducacional.niveleducacional = curso.niveleducacional  ");
		sqlStr.append("				WHERE matricula.matricula = reserva.matricula  ");
		sqlStr.append("				UNION  ");
		sqlStr.append("				SELECT 2, configuracaobibliotecaniveleducacional.configuracaobiblioteca, configuracaobibliotecaniveleducacional.biblioteca FROM matricula  ");
		sqlStr.append("				INNER JOIN curso ON curso.codigo = matricula.curso  ");
		sqlStr.append("				INNER JOIN configuracaobibliotecaniveleducacional ON configuracaobibliotecaniveleducacional.biblioteca = reserva.biblioteca  ");
		sqlStr.append("				AND configuracaobibliotecaniveleducacional.unidadeensino = matricula.unidadeensino  ");
		sqlStr.append("				AND (configuracaobibliotecaniveleducacional.niveleducacional IS NULL OR configuracaobibliotecaniveleducacional.niveleducacional = '')  ");
		sqlStr.append("				WHERE matricula.matricula = reserva.matricula  ");
		sqlStr.append("				UNION  ");
		sqlStr.append("				SELECT 3, biblioteca.configuracaobiblioteca, biblioteca.codigo FROM biblioteca WHERE codigo = reserva.biblioteca  ");
		sqlStr.append("				ORDER BY ordem LIMIT 1  ");
		sqlStr.append("			) AS configuracaobibliotecautilizar ON configuracaobibliotecautilizar.biblioteca = biblioteca.codigo  ");
		sqlStr.append("			INNER JOIN configuracaobiblioteca ON configuracaobiblioteca.codigo = configuracaobibliotecautilizar.configuracaobiblioteca  ");
		sqlStr.append("			WHERE biblioteca.codigo = reserva.biblioteca  ");
		sqlStr.append("		) AS prazovalidadereservacatalogosindisponiveis FROM reserva ");
		sqlStr.append("		WHERE emprestimo IS NULL ");
		sqlStr.append(" 	AND dataterminoreserva IS NOT NULL AND situacao NOT IN ('CA', 'FI', 'EM') ");
		sqlStr.append(" ) AS t ");
		sqlStr.append(" WHERE dataterminoreserva >= current_date - prazovalidadereservacatalogosindisponiveis ");
		sqlStr.append(" AND dataterminoreserva <= current_timestamp  ");
		
		if (catalogoVO != null && catalogoVO.getCodigo() > 0){
			sqlStr.append(" AND catalogo = ").append(catalogoVO.getCodigo());
		}
		if (pessoaVO != null && pessoaVO.getCodigo() > 0) {
			sqlStr.append(" AND pessoa = ").append(pessoaVO.getCodigo()); 
		}
		sqlStr.append(" ORDER BY datareserva ASC ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		List<ReservaVO> reservaVOs = new ArrayList<ReservaVO>(0);
		ReservaVO obj = null;
		while (dadosSQL.next()) {
			obj = new ReservaVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
			obj.getCatalogo().setCodigo(dadosSQL.getInt("catalogo"));
			obj.setTipoPessoa(dadosSQL.getString("tipopessoa"));
			obj.setDataReserva(dadosSQL.getTimestamp("dataReserva"));
			obj.setDataTerminoReserva(dadosSQL.getTimestamp("dataTerminoReserva"));
			obj.getBibliotecaVO().setCodigo(dadosSQL.getInt("biblioteca"));
			obj.setMatricula(dadosSQL.getString("matricula"));
			obj.setEmprestimo(dadosSQL.getInt("emprestimo"));
			montarDadosPessoa(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			reservaVOs.add(obj);
			obj = null;
		}
		return reservaVOs;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAlterarDataTerminoReservaDataReservaMaisAntigaPorCatalogoEEnviaMensagemReservaDisponivel(final CatalogoVO catalogoVO, final ReservaVO reservaAnterior, ConfiguracaoBibliotecaVO confPadraoBib, UsuarioVO usuario) throws Exception {
		try {
			final StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" UPDATE reserva SET dataterminoreserva = ( ");
			sqlStr.append(" 	SELECT ( ");
			sqlStr.append("    SELECT now() + (prazovalidadereservacatalogosdisponiveis::text || ' hours')::interval FROM biblioteca ");
			sqlStr.append(" 		INNER JOIN ( ");
			sqlStr.append(" 			SELECT 1 AS ordem, configuracaobibliotecaniveleducacional.configuracaobiblioteca, configuracaobibliotecaniveleducacional.biblioteca FROM matricula ");
			sqlStr.append(" 			INNER JOIN curso ON curso.codigo = matricula.curso ");
			sqlStr.append(" 			INNER JOIN configuracaobibliotecaniveleducacional ON configuracaobibliotecaniveleducacional.biblioteca = reserva.biblioteca ");
			sqlStr.append(" 			AND configuracaobibliotecaniveleducacional.unidadeensino = matricula.unidadeensino ");
			sqlStr.append(" 			AND configuracaobibliotecaniveleducacional.niveleducacional = curso.niveleducacional ");
			sqlStr.append(" 			WHERE matricula.matricula = reserva.matricula ");
			sqlStr.append(" 			UNION ");
			sqlStr.append(" 			SELECT 2, configuracaobibliotecaniveleducacional.configuracaobiblioteca, configuracaobibliotecaniveleducacional.biblioteca FROM matricula ");
			sqlStr.append(" 			INNER JOIN curso ON curso.codigo = matricula.curso ");
			sqlStr.append(" 			INNER JOIN configuracaobibliotecaniveleducacional ON configuracaobibliotecaniveleducacional.biblioteca = reserva.biblioteca ");
			sqlStr.append("				AND configuracaobibliotecaniveleducacional.unidadeensino = matricula.unidadeensino  ");
			sqlStr.append(" 			AND (configuracaobibliotecaniveleducacional.niveleducacional IS NULL OR configuracaobibliotecaniveleducacional.niveleducacional = '') ");
			sqlStr.append(" 			WHERE matricula.matricula = reserva.matricula ");
			sqlStr.append(" 			UNION ");
			sqlStr.append(" 			SELECT 3, biblioteca.configuracaobiblioteca, biblioteca.codigo FROM biblioteca WHERE codigo = reserva.biblioteca ");
			sqlStr.append("				ORDER BY ordem LIMIT 1  ");
			sqlStr.append(" 		) AS configuracaobibliotecautilizar ON configuracaobibliotecautilizar.biblioteca = biblioteca.codigo ");
			sqlStr.append(" 		INNER JOIN configuracaobiblioteca ON configuracaobiblioteca.codigo = configuracaobibliotecautilizar.configuracaobiblioteca ");
			sqlStr.append(" 		WHERE biblioteca.codigo = reserva.biblioteca ");
			sqlStr.append(" 	) AS prazovalidadereservacatalogosindisponiveis ");
			sqlStr.append(" ), situacao = 'DI' ");
			sqlStr.append(" WHERE codigo = ( ");
			sqlStr.append(" 	SELECT codigo FROM reserva ");
			sqlStr.append(" 	WHERE (dataterminoreserva IS NULL or dataterminoreserva::date > current_date) AND emprestimo IS NULL AND situacao NOT IN ('CA', 'FI', 'EM', 'DI') ");
			sqlStr.append(" 	and not exists (select codigo from bloqueiobiblioteca where bloqueiobiblioteca.biblioteca = reserva.biblioteca and bloqueiobiblioteca.pessoa = reserva.pessoa and (bloqueiobiblioteca.datalimitebloqueio is null or bloqueiobiblioteca.datalimitebloqueio >= current_date) limit 1  ) ");
			sqlStr.append("     AND catalogo = ? and biblioteca = ?  ");
			sqlStr.append("     and ((select count(ex.codigo) "); 
			sqlStr.append("           from exemplar ex where ex.catalogo = ? and ex.biblioteca = ? ");
			sqlStr.append("           and ex.situacaoatual = 'DI'  ");
			sqlStr.append("           and ((paraConsulta = false and emprestarSomenteFinalDeSemana = false) or (emprestarSomenteFinalDeSemana = true and EXTRACT(DOW FROM current_date) in (0, 5, 6))) ");
			sqlStr.append("           ) - (  ");
			sqlStr.append("           select count(codigo) from reserva r where r.situacao = 'DI'   and (dataterminoreserva IS NULL or dataterminoreserva::date >= current_date)  "); 
			sqlStr.append("           and r.catalogo = ?  and r.biblioteca = ?   ");
			sqlStr.append("           )) > 0  ");
			sqlStr.append(" 	ORDER BY datareserva ASC LIMIT 1 ");
			sqlStr.append(" ) ");
			sqlStr.append(" RETURNING codigo ");

			Integer codigoReserva = getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
					sqlAlterar.setInt(1, catalogoVO.getCodigo());
					sqlAlterar.setInt(2, reservaAnterior.getBibliotecaVO().getCodigo());
					sqlAlterar.setInt(3, catalogoVO.getCodigo());
					sqlAlterar.setInt(4, reservaAnterior.getBibliotecaVO().getCodigo());
					sqlAlterar.setInt(5, catalogoVO.getCodigo());
					sqlAlterar.setInt(6, reservaAnterior.getBibliotecaVO().getCodigo());
					return sqlAlterar;
				}
			}, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			});

			if (codigoReserva != null && codigoReserva > 0) {
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
				ReservaVO novaReserva = getFacadeFactory().getReservaFacade().consultarPorChavePrimaria(codigoReserva, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
				PessoaVO pessoaNovaReserva = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(novaReserva.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				BibliotecaVO bibliotecaVO = getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(reservaAnterior.getBibliotecaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemReservaDisponivel(catalogoVO, reservaAnterior.getBibliotecaVO(), novaReserva, pessoaNovaReserva, novaReserva.getTipoPessoa(), confPadraoBib.getFuncionarioPadraoEnvioMensagem().getPessoa(), configuracaoGeralSistemaVO, usuario);
				PessoaVO pessoaReservaAnterior = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(reservaAnterior.getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemReservaCancelada(catalogoVO, reservaAnterior.getBibliotecaVO(), reservaAnterior, pessoaReservaAnterior, reservaAnterior.getTipoPessoa(), confPadraoBib.getFuncionarioPadraoEnvioMensagem().getPessoa(), configuracaoGeralSistemaVO, usuario);
			}

		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoReserva(ReservaVO reservaVO, String situacao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("UPDATE reserva SET situacao = ? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		try {
			reservaVO.setSituacao(situacao);
			getConexao().getJdbcTemplate().update(sqlStr.toString(), new Object[] {reservaVO.getSituacao(), reservaVO.getCodigo()});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ReservaVO consultarReservaPorEmprestimoCatalogoPessoa(Integer emprestimo, Integer catalogo, Integer pessoa, UsuarioVO usuarioVO) throws Exception {
		try {
			final StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" select * from reserva where emprestimo = ").append(emprestimo).append(" and catalogo = ").append(catalogo).append(" and pessoa = ").append(pessoa);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO); 
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception {
		String sqlStr = "UPDATE Reserva set pessoa=? WHERE ((pessoa = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { pessoaNova, pessoaAntigo });
	}
	
	
	@Override
	public String gerarStringParaTicket(List<ReservaVO> reservas,  BibliotecaVO bibliotecaVO, ImpressoraVO impressoraVO, ConfiguracaoBibliotecaVO confBibVO ,UsuarioVO usuario) throws Exception{
		String textoComprovante = "";
		Integer index = 1;
		boolean cabecalhoCriado = false;
		boolean rodapeCriado = false;

		for (ReservaVO reserva : reservas) {
			if (!cabecalhoCriado) {
				textoComprovante += "&&&&&&&&&&&RECIBO RESERVA" + ">";
				if (!reserva.getBibliotecaVO().getNome().equals("")) {
					textoComprovante += Uteis.removeCaractersEspeciais(reserva.getBibliotecaVO().getNome().toUpperCase()) + ">";
				}
				if (!reserva.getPessoa().getNome().equals("")) {
					textoComprovante += "SOLICITANTE: " + Uteis.removeCaractersEspeciais(reserva.getPessoa().getNome().toUpperCase()) + ">";
					textoComprovante += "MATRICULA: " + Uteis.removeCaractersEspeciais(reserva.getMatricula()) + ">";
				}
				textoComprovante += "DATA: " + Uteis.getDataAtual() + ">";
				textoComprovante += "---------------------------------------->";

				cabecalhoCriado = true;
			}

			if (reserva.getCatalogo().getTitulo().toUpperCase().length() > 30) {
				String aux = reserva.getCatalogo().getTitulo().toUpperCase().substring(0, 30) + ">";
				aux += reserva.getCatalogo().getTitulo().toUpperCase().substring(30, reserva.getCatalogo().getTitulo().length());
				textoComprovante += +index + "-" + "TITULO: " + aux + ">";
			} else {
				textoComprovante += +index + "-" + "TITULO: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(reserva.getCatalogo().getTitulo().toUpperCase())) + ">";
			}
			textoComprovante += "AUTOR: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(reserva.getCatalogo().getNomeAutores())) + ">";
			textoComprovante += "DATA RESERVA: " + Uteis.getDataComHora(reserva.getDataReserva()) + ">";
			index++;
			textoComprovante += "---------------------------------------->";
			textoComprovante += "                                               >";

		}
		for (ReservaVO reserva : reservas) {
			if (!rodapeCriado) {
				if (!confBibVO.getCodigo().equals(0)) {
					if (!confBibVO.getTextoPadraoReservaCatalogo().equals("")) {
					textoComprovante += Uteis.removerAcentos(confBibVO.getTextoPadraoReservaCatalogo().toUpperCase() + ">");
					}
					textoComprovante += "                                               >";
				}
				
				textoComprovante += "&&&&&_______________________________&&&&&>";
				String nomePessoa = Uteis.removeCaractersEspeciais(reserva.getPessoa().getNome().toUpperCase());
				if (nomePessoa.length() < 42) {
					int espaco = (42 - nomePessoa.length()) / 2;
					for (int x = 1; x <= espaco; x++) {
						nomePessoa = "&" + nomePessoa + "&";
					}
				}
				textoComprovante += nomePessoa + ">";
				textoComprovante += "                                               >";
				textoComprovante += "&&&&&_______________________________&&&&&>";

				String nomeUsuario = Uteis.removeCaractersEspeciais(usuario.getNome_Apresentar());
				if (nomeUsuario.length() < 42) {
					int espaco = (42 - nomeUsuario.length()) / 2;
					for (int x = 1; x <= espaco; x++) {
						nomeUsuario = "&" + nomeUsuario + "&";
					}
				}
				textoComprovante += nomeUsuario + ">";
			}
			rodapeCriado = true;
		}
		if(Uteis.isAtributoPreenchido(bibliotecaVO) && bibliotecaVO.getIsImpressaoPorPool() && Uteis.isAtributoPreenchido(impressoraVO)){
			getFacadeFactory().getPoolImpressaoFacade().incluirPoolImpressao(impressoraVO, FormatoImpressaoEnum.TEXTO, textoComprovante, usuario);
		}
		return textoComprovante;
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarExclusaoReservaPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception{
		getConexao().getJdbcTemplate().update("DELETE FROM reserva where matricula = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), matricula);
	}

}