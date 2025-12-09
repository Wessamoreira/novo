package negocio.facade.jdbc.protocolo;

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
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.protocolo.AlterarResponsavelRequerimentoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.AlterarResponsavelRequerimentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>AlterarResponsavelRequerimentoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>AlterarResponsavelRequerimentoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see AlterarResponsavelRequerimentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class AlterarResponsavelRequerimento extends ControleAcesso implements AlterarResponsavelRequerimentoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public AlterarResponsavelRequerimento() throws Exception {
		super();
		setIdEntidade("AlterarResponsavelRequerimento");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AlterarResponsavelRequerimentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			AlterarResponsavelRequerimento.incluir(getIdEntidade(), true, usuario);
			AlterarResponsavelRequerimentoVO.validarDados(obj);
			
			List<RequerimentoVO> selecionados = new ArrayList<RequerimentoVO>(0);
			obj.setRequerimentos("");
			String aux = "";
			for (RequerimentoVO requerimento: obj.getRequerimentoVOs()) {
				if (requerimento.getSelecionado()) {
					selecionados.add(requerimento);
					getFacadeFactory().getRequerimentoFacade().realizarAlteracaoFuncionarioResponsavelRequerimento(requerimento, obj.getResponsavelRequerimento(), usuario);
					obj.setRequerimentos(obj.getRequerimentos() + aux + requerimento.getCodigo());
					aux = ",";
				}
			}
			
			final String sql = "INSERT INTO AlterarResponsavelRequerimento (responsavelRequerimento, motivoAlteracao, dataAlteracao, responsavelAlteracao, responsavelAnterior, requerimentos) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						sqlInserir.setInt(1, obj.getResponsavelRequerimento().getCodigo());
						sqlInserir.setString(2, obj.getMotivoAlteracao());
						sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataAlteracao()));
						sqlInserir.setInt(4, obj.getResponsavelAlteracao().getCodigo());
						sqlInserir.setInt(5, obj.getResponsavelAnterior().getCodigo());
						sqlInserir.setString(6, obj.getRequerimentos());
					} catch (Exception e) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setRequerimentoVOs(selecionados);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	public AlterarResponsavelRequerimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM AlterarResponsavelRequerimento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario, configuracaoFinanceiroVO));
	}

	@Override
	public List<AlterarResponsavelRequerimentoVO> consultarOtimizado(AlterarResponsavelRequerimentoVO alterarResponsavelRequerimentoVO, Date dataIni, Date dataFim, boolean todoPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, int limit, int offset, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer("SELECT distinct a.codigo, a.responsavelRequerimento, a.dataAlteracao, a.motivoAlteracao, a.responsavelAlteracao, a.responsavelAnterior, a.requerimentos, ");
		sqlStr.append("u.nome as nomeResponsavelAlteracao, pr.nome as nomeResponsavelRequerimento, pa.nome as nomeResponsavelAnterior FROM AlterarResponsavelRequerimento as a ");
		sqlStr.append("INNER JOIN Usuario as u ON u.codigo = a.responsavelAlteracao ");
		sqlStr.append("INNER JOIN Funcionario as fr ON fr.codigo = a.responsavelRequerimento ");
		sqlStr.append("INNER JOIN pessoa as pr ON pr.codigo = fr.pessoa ");
		sqlStr.append("INNER JOIN Funcionario as fa ON fa.codigo = a.responsavelAnterior ");
		sqlStr.append("INNER JOIN pessoa as pa ON pa.codigo = fa.pessoa ");
		sqlStr.append("WHERE 1 = 1 ");
		if (alterarResponsavelRequerimentoVO.getResponsavelAnterior().getCodigo() != 0) {
			sqlStr.append(" AND a.responsavelAnterior = ").append(alterarResponsavelRequerimentoVO.getResponsavelAnterior().getCodigo());
		}
		if (alterarResponsavelRequerimentoVO.getResponsavelAlteracao().getCodigo() != 0) {
			sqlStr.append(" AND a.responsavelAlteracao = ").append(alterarResponsavelRequerimentoVO.getResponsavelAlteracao().getCodigo());
		}
		if (alterarResponsavelRequerimentoVO.getResponsavelRequerimento().getCodigo() != 0) {
			sqlStr.append(" AND a.responsavelRequerimento = ").append(alterarResponsavelRequerimentoVO.getResponsavelRequerimento().getCodigo());
		}
		if (!todoPeriodo) {
			if (dataIni != null) {
				sqlStr.append(" AND a.dataAlteracao >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateSemHora(dataIni))).append("' ");
			}
			if (dataFim != null) {
				sqlStr.append(" AND a.dataAlteracao <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFim))).append("' ");
			}
		}
		sqlStr.append("ORDER BY a.dataAlteracao desc ");
		sqlStr.append(" limit ").append(limit);
		sqlStr.append(" offset  ").append(offset);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario, configuracaoFinanceiroVO);
	}
	
	@Override
	public Integer consultarTotalRegistros(AlterarResponsavelRequerimentoVO alterarResponsavelRequerimentoVO, Date dataIni, Date dataFim, boolean todoPeriodo, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" SELECT count(distinct a.codigo) as totalRegistro FROM AlterarResponsavelRequerimento as a WHERE 1 = 1 ");
		if (alterarResponsavelRequerimentoVO.getResponsavelAnterior().getCodigo() != 0) {
			sqlStr.append(" AND a.responsavelAnterior = ").append(alterarResponsavelRequerimentoVO.getResponsavelAnterior().getCodigo());
		}
		if (alterarResponsavelRequerimentoVO.getResponsavelAlteracao().getCodigo() != 0) {
			sqlStr.append(" AND a.responsavelAlteracao = ").append(alterarResponsavelRequerimentoVO.getResponsavelAlteracao().getCodigo());
		}
		if (alterarResponsavelRequerimentoVO.getResponsavelRequerimento().getCodigo() != 0) {
			sqlStr.append(" AND a.responsavelRequerimento = ").append(alterarResponsavelRequerimentoVO.getResponsavelRequerimento().getCodigo());
		}
		if (!todoPeriodo) {
			if (dataIni != null) {
				sqlStr.append(" AND a.dataAlteracao >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateSemHora(dataIni))).append("' ");
			}
			if (dataFim != null) {
				sqlStr.append(" AND a.dataAlteracao <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFim))).append("' ");
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("totalRegistro");
		} else {
			return 0;
		}
	}
	
	public static List<AlterarResponsavelRequerimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		List<AlterarResponsavelRequerimentoVO> vetResultado = new ArrayList<AlterarResponsavelRequerimentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario, configuracaoFinanceiroVO));
		}
		return vetResultado;
	}
	
	public static AlterarResponsavelRequerimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		AlterarResponsavelRequerimentoVO obj = new AlterarResponsavelRequerimentoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getResponsavelRequerimento().setCodigo(dadosSQL.getInt("responsavelRequerimento"));
		obj.getResponsavelRequerimento().getPessoa().setNome(dadosSQL.getString("nomeResponsavelRequerimento"));
		obj.setDataAlteracao(dadosSQL.getTimestamp("dataAlteracao"));
		obj.setMotivoAlteracao(dadosSQL.getString("motivoAlteracao"));
		obj.getResponsavelAlteracao().setCodigo(dadosSQL.getInt("responsavelAlteracao"));
		obj.getResponsavelAlteracao().setNome(dadosSQL.getString("nomeResponsavelAlteracao"));
		obj.getResponsavelAnterior().setCodigo(dadosSQL.getInt("responsavelAnterior"));
		obj.getResponsavelAnterior().getPessoa().setNome(dadosSQL.getString("nomeResponsavelAnterior"));
		obj.setRequerimentos(dadosSQL.getString("requerimentos"));
		montarRequisitos(obj, usuario, configuracaoFinanceiroVO);
		return obj;
	}
	
	public static void montarRequisitos(AlterarResponsavelRequerimentoVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		if (!obj.getRequerimentos().isEmpty()) {
			obj.setRequerimentoVOs(getFacadeFactory().getRequerimentoFacade().consultarPorCodigo(obj.getRequerimentos(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, configuracaoFinanceiroVO));
		}
	}
	
	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AlterarResponsavelRequerimento.idEntidade = idEntidade;
	}

}
