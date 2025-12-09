package negocio.facade.jdbc.faturamento.nfe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesCursoItemVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesCursoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.IntegracaoGinfesCursoItemInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class IntegracaoGinfesCursoItem extends ControleAcesso implements IntegracaoGinfesCursoItemInterfaceFacade {

	private static final long serialVersionUID = -1L;
	protected static String idEntidade = "IntegracaoGinfesCurso";

	public IntegracaoGinfesCursoItem() {
		super();
	}

	public void validarDados(IntegracaoGinfesCursoItemVO obj) throws ConsistirException {

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirCursos(final IntegracaoGinfesCursoVO obj, List<IntegracaoGinfesCursoItemVO> cursos, UsuarioVO usuario) throws Exception {
		for (IntegracaoGinfesCursoItemVO curso : cursos) {
			curso.setIntegracao(obj);
			incluir(curso, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final IntegracaoGinfesCursoItemVO obj, UsuarioVO usuario) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO IntegracaoGinfesCursoItem (integracao, codigoUnidadeEnsino, descricaoCursoTurno, valorServico, itemListaServico, ativo, operacao) VALUES (?, ?, ?, ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getIntegracao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCodigoUnidadeEnsinoCurso(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescricaoCursoTurno(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorServico(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getItemListaServico(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAtivo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getOperacao(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCursos(IntegracaoGinfesCursoVO obj, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM IntegracaoGinfesCursoItem WHERE integracao = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoGinfesCursoItemVO> consultarPorIntegracao(Integer integracao, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select * from IntegracaoGinfesCursoItem where integracao = ").append(integracao);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoGinfesCursoItemVO> gerarCursos(Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder()
			.append(" with importados as (select distinct i.codigounidadeensino from integracaoginfescursoitem i ")
			.append(" inner join integracaoginfescurso g on g.codigo = i.integracao where g.importado) ")
			.append(" select distinct uec.codigo codcurso,  ")
			.append(" coalesce(unidadeensinocursovaloresginfes.codigocursoginfes, uec.codigoCursoUnidadeEnsinoGinfes) codigoCursoUnidadeEnsinoGinfes, ")
			.append(" unaccent(c.nome||' - '||t.nome)::varchar(100) descricaocurso, ")
			.append(" coalesce(unidadeensinocursovaloresginfes.valormensalidade, uec.valormensalidade)::numeric(15,2) valorservico, ")
			.append(" uec.codigoitemlistaservico itemlistaservico, ")
			.append(" case when uec.situacaocurso = 'AT' then 1 else 0 end icativo, ")
			.append(" case when im.codigounidadeensino is not null then 1 else 0 end operacao  ")
			.append(" from unidadeensinocurso uec ")
			.append(" left join  unidadeensinocursovaloresginfes on unidadeensinocursovaloresginfes.unidadeensinocurso = uec.codigo ")
			.append(" inner join curso c on c.codigo = uec.curso ")
			.append(" inner join unidadeensino ue on ue.codigo = uec.unidadeensino ")
			.append(" inner join turno t on t.codigo = uec.turno ")
			.append(" left join importados im on im.codigounidadeensino = uec.codigo ")
			.append(" where ue.codigo = ").append(unidadeEnsino);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<IntegracaoGinfesCursoItemVO> vetResultado = new ArrayList<IntegracaoGinfesCursoItemVO>(0);
		while (tabelaResultado.next()) {
			IntegracaoGinfesCursoItemVO obj = new IntegracaoGinfesCursoItemVO();
			if(Uteis.isAtributoPreenchido((tabelaResultado.getInt("codigoCursoUnidadeEnsinoGinfes")))) {
				obj.setCodigoUnidadeEnsinoCurso((tabelaResultado.getInt("codigoCursoUnidadeEnsinoGinfes")));				
			}else {
				obj.setCodigoUnidadeEnsinoCurso((tabelaResultado.getInt("codcurso")));
			}		
			obj.setDescricaoCursoTurno(tabelaResultado.getString("descricaocurso"));
			obj.setValorServico(tabelaResultado.getDouble("valorservico"));
			obj.setItemListaServico(tabelaResultado.getString("itemlistaservico"));
			obj.setAtivo(tabelaResultado.getInt("icativo"));
			obj.setOperacao(tabelaResultado.getInt("operacao"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private List<IntegracaoGinfesCursoItemVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<IntegracaoGinfesCursoItemVO> vetResultado = new ArrayList<IntegracaoGinfesCursoItemVO>(0);
		while (tabelaResultado.next()) {
			IntegracaoGinfesCursoItemVO obj = new IntegracaoGinfesCursoItemVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDados(IntegracaoGinfesCursoItemVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getIntegracao().setCodigo(dadosSQL.getInt("integracao"));
		obj.setCodigoUnidadeEnsinoCurso((dadosSQL.getInt("codigoUnidadeEnsino")));
		obj.setDescricaoCursoTurno(dadosSQL.getString("descricaoCursoTurno"));
		obj.setValorServico(dadosSQL.getDouble("valorServico"));
		obj.setItemListaServico(dadosSQL.getString("itemListaServico"));
		obj.setAtivo(dadosSQL.getInt("ativo"));
		obj.setOperacao(dadosSQL.getInt("operacao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	public static String getIdEntidade() {
		return IntegracaoGinfesCursoItem.idEntidade;
	}

}
