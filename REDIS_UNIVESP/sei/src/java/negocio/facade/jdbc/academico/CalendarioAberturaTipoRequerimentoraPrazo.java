package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.CalendarioAberturaRequerimentoVO;
import negocio.comuns.academico.CalendarioAberturaTipoRequerimentoraPrazoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CalendarioAberturaTipoRequerimentoraPrazoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>CalendarioAberturaTipoRequerimentoraPrazoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>ArquivoVO</code>. Encapsula toda a
 * interação com o banco de dados.
 *
 * @see CalendarioAberturaTipoRequerimentoraPrazoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class CalendarioAberturaTipoRequerimentoraPrazo extends ControleAcesso implements CalendarioAberturaTipoRequerimentoraPrazoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public CalendarioAberturaTipoRequerimentoraPrazo() throws Exception {
		super();
		setIdEntidade("CalendarioAberturaTipoRequerimentoraPrazo");
	}

	public static void setIdEntidade(String idEntidade) {
		CalendarioAberturaTipoRequerimentoraPrazo.idEntidade = idEntidade;
	}

	@Override
	public void incluir(Integer calendarioAbertura, List<CalendarioAberturaTipoRequerimentoraPrazoVO> calendarioAberturaTipoRequerimentoraPrazoVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			for (CalendarioAberturaTipoRequerimentoraPrazoVO obj : calendarioAberturaTipoRequerimentoraPrazoVOs) {
				if (obj.getNovoObj()) {
					incluirCalendarioAberturaTipoRequerimentoPrazo(obj, calendarioAbertura, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirCalendarioAberturaTipoRequerimentoPrazo(final CalendarioAberturaTipoRequerimentoraPrazoVO obj, final Integer calendarioAbertura, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO calendarioAberturaTipoRequerimentoraPrazo(tiporequerimento, calendarioAberturaRequerimento ,datainicio, datafim ) values (?,?,?,?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setInt(1, obj.getTipoRequerimentoVO().getCodigo());
					sqlInserir.setInt(2, calendarioAbertura);
					sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataInicio()));
					sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataFim()));
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
			throw e;
		}
	}

	@Override
	public void alterar(Integer calendarioAbertura, List<CalendarioAberturaTipoRequerimentoraPrazoVO> calendarioAberturaTipoRequerimentoraPrazoVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			for (CalendarioAberturaTipoRequerimentoraPrazoVO obj : calendarioAberturaTipoRequerimentoraPrazoVOs) {
				if (!obj.getNovoObj()) {
					alterarCalendarioAberturaTipoRequerimentoPrazo(obj, usuarioVO);
				}else if(obj.getNovoObj()){
					incluirCalendarioAberturaTipoRequerimentoPrazo(obj, calendarioAbertura, usuarioVO);;
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCalendarioAberturaTipoRequerimentoPrazo(final CalendarioAberturaTipoRequerimentoraPrazoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			final String sql = "UPDATE calendarioAberturaTipoRequerimentoraPrazo set datainicio=?, datafim=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataInicio()));
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataFim()));
					sqlAlterar.setInt(3, obj.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void consultarPorCalendarioAberturaRequerimento(CalendarioAberturaRequerimentoVO calendarioAberturaRequerimentoVO, UsuarioVO usuarioVO) throws Exception {
		final StringBuilder sql = new StringBuilder();
		sql.append("select * from calendarioAberturaTipoRequerimentoraPrazo ");
		sql.append(" where calendarioaberturarequerimento =  ? ORDER BY codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), calendarioAberturaRequerimentoVO.getCodigo());

		CalendarioAberturaTipoRequerimentoraPrazoVO objs = new CalendarioAberturaTipoRequerimentoraPrazoVO();
		calendarioAberturaRequerimentoVO.getCalendarioAberturaTipoRequerimentoraPrazoVOs().addAll(montarDadosConsulta(tabelaResultado, usuarioVO));
	}

	public static List<CalendarioAberturaTipoRequerimentoraPrazoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<CalendarioAberturaTipoRequerimentoraPrazoVO> vetResultado = new ArrayList<CalendarioAberturaTipoRequerimentoraPrazoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static CalendarioAberturaTipoRequerimentoraPrazoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		CalendarioAberturaTipoRequerimentoraPrazoVO obj = new CalendarioAberturaTipoRequerimentoraPrazoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDataInicio(dadosSQL.getDate("datainicio"));
		obj.setDataFim(dadosSQL.getDate("datafim"));
		obj.setTipoRequerimentoVO(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(dadosSQL.getInt("tiporequerimento"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public void validarDados(CalendarioAberturaTipoRequerimentoraPrazoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getDataInicio()) && Uteis.isAtributoPreenchido(obj.getDataFim())) {
			throw new ConsistirException("O campo Data Inicio deve ser informado para o Tipo Requerimento :" + obj.getTipoRequerimentoVO().getNome());
		}
		
		if (Uteis.isAtributoPreenchido(obj.getDataInicio()) && !Uteis.isAtributoPreenchido(obj.getDataFim())) {
			throw new ConsistirException("O campo Data Fim deve ser informado para o Tipo Requerimento :" + obj.getTipoRequerimentoVO().getNome());
		}
	}
}
