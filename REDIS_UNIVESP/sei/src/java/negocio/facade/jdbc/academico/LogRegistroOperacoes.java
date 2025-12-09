package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.postgresql.util.PGobject;
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

import com.google.gson.JsonArray;

import negocio.comuns.academico.AproveitamentoDisciplinaVO;
import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.LogRegistroOperacoesVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.enumeradores.OperacaoLogRegistroOperacoesEnum;
import negocio.comuns.academico.enumeradores.TabelaLogRegistroOperacoesEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.DisciplinasAproveitadasInterfaceFacade;
import negocio.interfaces.academico.LogRegistroOperacoesInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe
 * <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code>. Responsável
 * por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe
 * <code>AproveitamentoDisciplinaDisciplinasAproveitadasVO</code>. Encapsula
 * toda a interação com o banco de dados.
 * 
 * @see AproveitamentoDisciplinaDisciplinasAproveitadasVO
 * @see ControleAcesso
 * @see AproveitamentoDisciplina
 */
@Repository
@Scope("singleton")
@Lazy
public class LogRegistroOperacoes extends ControleAcesso implements LogRegistroOperacoesInterfaceFacade {

	private static final long serialVersionUID = -1125108885136825593L;
	protected static String idEntidade;

	public LogRegistroOperacoes() throws Exception {
		super();
		setIdEntidade("LogRegistroOperacoes");
	}

	public LogRegistroOperacoesVO novo() throws Exception {
		LogRegistroOperacoes.incluir(getIdEntidade());
		LogRegistroOperacoesVO obj = new LogRegistroOperacoesVO();
		return obj;
	}

	public void validarDados(LogRegistroOperacoesVO logRegistroOperacoesVO, UsuarioVO usuarioVO) throws Exception {
		if (logRegistroOperacoesVO.getRow_data().isEmpty()) {
			throw new Exception("Dados do Log Registro Operações não foi informado (Row Data).");
		}
		if (logRegistroOperacoesVO.getChanged_fields().isEmpty()) {
			throw new Exception("Alterações do Log Registro Operações não foi informado (Changed Fields).");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LogRegistroOperacoesVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj, usuario);
			final String sql = "INSERT INTO LogRegistroOperacoes( tabelaLogRegistroOperacoes, operacaoLogRegistroOperacoes, responsavel, dataOperacao, row_data, changed_fields, observacao ) " + "VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					PGobject jsonObjectRowData = new PGobject();
					PGobject jsonObjectChangedFields = new PGobject();
					sqlInserir.setString(1, obj.getTabelaLogRegistroOperacoes().name());
					sqlInserir.setString(2, obj.getOperacaoLogRegistroOperacoes().name());
					sqlInserir.setInt(3, obj.getResponsavel().getCodigo());
					sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataOperacao()));
					
					jsonObjectRowData.setType("json");
					jsonObjectRowData.setValue(obj.getRow_data().toJSONString());
					sqlInserir.setObject(5, jsonObjectRowData);
					
					jsonObjectChangedFields.setType("json");
					jsonObjectChangedFields.setValue(obj.getChanged_fields().toJSONString());
					sqlInserir.setObject(6, jsonObjectChangedFields);
					
					sqlInserir.setString(7, obj.getObservacao());
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

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public LogRegistroOperacoesVO inicializarDadosLogRegistroOperacoes(TabelaLogRegistroOperacoesEnum tabelaLogRegistroOperacoes, OperacaoLogRegistroOperacoesEnum operacaoLogRegistroOperacoes, JSONObject jsonObjectRowData, JSONObject jsonObjectChangedFields, String observacao, UsuarioVO responsavelVO) {
		LogRegistroOperacoesVO logRegistroOperacoesVO = new LogRegistroOperacoesVO(jsonObjectRowData, jsonObjectChangedFields);
		logRegistroOperacoesVO.setTabelaLogRegistroOperacoes(tabelaLogRegistroOperacoes);
		logRegistroOperacoesVO.setOperacaoLogRegistroOperacoes(operacaoLogRegistroOperacoes);
		logRegistroOperacoesVO.setObservacao(observacao);
		logRegistroOperacoesVO.setDataOperacao(new Date());
		logRegistroOperacoesVO.setResponsavel(responsavelVO);
		return logRegistroOperacoesVO;
	}

	public static String getIdEntidade() {
		return LogRegistroOperacoes.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		LogRegistroOperacoes.idEntidade = idEntidade;
	}

}