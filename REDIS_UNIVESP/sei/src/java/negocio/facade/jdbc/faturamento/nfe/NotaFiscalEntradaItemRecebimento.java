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
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemRecebimentoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.NotaFiscalEntradaItemRecebimentoInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class NotaFiscalEntradaItemRecebimento extends ControleAcesso implements NotaFiscalEntradaItemRecebimentoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8229434979024767558L;
	protected static String idEntidade = "NotaFiscalEntradaItemRecebimento";

	public NotaFiscalEntradaItemRecebimento() {
		super();
	}

	public void validarDados(NotaFiscalEntradaItemRecebimentoVO obj) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaCentroResultadoInterfaceFacade#persistir(java.util.List, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<NotaFiscalEntradaItemRecebimentoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		for (NotaFiscalEntradaItemRecebimentoVO obj : lista) {
			validarDados(obj);
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final NotaFiscalEntradaItemRecebimentoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaItemRecebimento.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO NotaFiscalEntradaItemRecebimento (requisicaoItem, compraItem, notaFiscalEntradaItem, quantidadeNotaFiscalEntrada, valorUnitario) ");
			sql.append("    VALUES (?,?,?,?,?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getRequisicaoItemVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCompraItemVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNotaFiscalEntradaItemVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQuantidadeNotaFiscalEntrada(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorUnitario(), ++i, sqlInserir);
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
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final NotaFiscalEntradaItemRecebimentoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaItemRecebimento.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE NotaFiscalEntradaItemRecebimento ");
			sql.append("   SET requisicaoItem= ?, compraItem= ?, notaFiscalEntradaItem= ?, quantidadeNotaFiscalEntrada= ?, valorUnitario= ? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getRequisicaoItemVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCompraItemVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNotaFiscalEntradaItemVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getQuantidadeNotaFiscalEntrada(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorUnitario(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Double consultarQuantidadeNotaFiscalEntradaTotal(NotaFiscalEntradaItemRecebimentoVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT sum (quantidadeNotaFiscalEntrada) as quantidadeNotaFiscalEntrada from NotaFiscalEntradaItemRecebimento ");
		sql.append(" where notaFiscalEntradaItem !=  ").append(obj.getNotaFiscalEntradaItemVO().getCodigo());
		if (Uteis.isAtributoPreenchido(obj.getRequisicaoItemVO())) {
			sql.append(" and requisicaoitem =  ").append(obj.getRequisicaoItemVO().getCodigo());
		} else {
			sql.append(" and compraitem =  ").append(obj.getCompraItemVO().getCodigo());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return tabelaResultado.next() ? tabelaResultado.getDouble("quantidadeNotaFiscalEntrada") : 0.0;
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT NotaFiscalEntradaItemRecebimento.codigo as \"NotaFiscalEntradaItemRecebimento.codigo\",  ");
		sql.append(" NotaFiscalEntradaItemRecebimento.quantidadeNotaFiscalEntrada as \"NotaFiscalEntradaItemRecebimento.quantidadeNotaFiscalEntrada\",  ");
		sql.append(" NotaFiscalEntradaItemRecebimento.valorUnitario as \"NotaFiscalEntradaItemRecebimento.valorUnitario\",  ");

		sql.append(" notaFiscalEntradaItem.codigo as \"notaFiscalEntradaItem.codigo\",  ");

		sql.append(" requisicaoitem.codigo as \"requisicaoitem.codigo\",  ");
		sql.append(" requisicaoitem.quantidadeAutorizada as \"requisicaoitem.quantidadeAutorizada\", ");

		sql.append(" requisicao.codigo as \"requisicao.codigo\",   ");
		sql.append(" requisicao.tipoNivelCentroResultadoEnum as \"requisicao.tipoNivelCentroResultadoEnum\",   ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", ");

		sql.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", categoriaDespesa.descricao as \"categoriaDespesa.descricao\",   ");

		sql.append(" funcionariocargo.codigo as \"funcionariocargo.codigo\", ");
		sql.append(" departamento.codigo as \"departamento.codigo\", departamento.nome as \"departamento.nome\",  ");
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");
		sql.append(" turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\",  ");
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\",  ");
		
		sql.append(" centroresultadoadministrativo.codigo as \"centroresultadoadministrativo.codigo\", centroresultadoadministrativo.descricao as \"centroresultadoadministrativo.descricao\", centroresultadoadministrativo.identificadorCentroResultado as \"centroresultadoadministrativo.identificadorCentroResultado\",   ");		

		sql.append(" compraitem.codigo as \"compraitem.codigo\",  ");
		sql.append(" compraitem.quantidadeAdicional as \"compraitem.quantidadeAdicional\",  ");
		sql.append(" compraitem.quantidadeRequisicao as \"compraitem.quantidadeRequisicao\",  ");
		sql.append(" compraitem.tipoNivelCentroResultadoEnum as \"compraitem.tipoNivelCentroResultadoEnum\",  ");
		sql.append(" unidadeensinoCompraItem.codigo as \"unidadeensinoCompraItem.codigo\", unidadeensinoCompraItem.nome as \"unidadeensinoCompraItem.nome\", ");
		sql.append(" categoriaDespesaCompraItem.codigo as \"categoriaDespesaCompraItem.codigo\", categoriaDespesaCompraItem.descricao as \"categoriaDespesaCompraItem.descricao\",   ");
		sql.append(" departamentoCompraItem.codigo as \"departamentoCompraItem.codigo\", departamentoCompraItem.nome as \"departamentoCompraItem.nome\",  ");
		sql.append(" cursoCompraItem.codigo as \"cursoCompraItem.codigo\", cursoCompraItem.nome as \"cursoCompraItem.nome\",  ");
		sql.append(" turnoCompraItem.codigo as \"turnoCompraItem.codigo\", turnoCompraItem.nome as \"turnoCompraItem.nome\",  ");
		sql.append(" turmaCompraItem.codigo as \"turmaCompraItem.codigo\", turmaCompraItem.identificadorturma as \"turmaCompraItem.identificadorturma\",  ");
		
		sql.append(" crad.codigo as \"crad.codigo\", crad.descricao as \"crad.descricao\", crad.identificadorCentroResultado as \"crad.identificadorCentroResultado\"  ");		

		sql.append(" FROM NotaFiscalEntradaItemRecebimento ");
		sql.append(" inner join notaFiscalEntradaItem on notaFiscalEntradaItem.codigo = NotaFiscalEntradaItemRecebimento.notaFiscalEntradaItem");
		sql.append(" left join requisicaoitem on requisicaoitem.codigo = NotaFiscalEntradaItemRecebimento.requisicaoitem");
		sql.append(" left join requisicao ON requisicao.codigo = requisicaoitem.requisicao ");
		sql.append(" left join unidadeensino ON requisicao.unidadeensino = unidadeensino.codigo ");
		sql.append(" left join categoriaDespesa ON requisicao.categoriaDespesa = categoriaDespesa.codigo ");
		sql.append(" LEFT JOIN funcionariocargo on requisicao.funcionariocargo = funcionariocargo.codigo ");
		sql.append(" LEFT JOIN departamento on requisicao.departamento = departamento.codigo ");
		sql.append(" LEFT JOIN curso on requisicao.curso = curso.codigo ");
		sql.append(" LEFT JOIN turma on requisicao.turma = turma.codigo ");
		sql.append(" LEFT JOIN turno on requisicao.turno = turno.codigo ");
		sql.append(" left join centroresultado AS centroresultadoadministrativo ON requisicao.centroresultadoadministrativo = centroresultadoadministrativo.codigo ");
		sql.append(" left join compraitem on compraitem.codigo = NotaFiscalEntradaItemRecebimento.compraitem");
		sql.append(" left join compra ON compra.codigo = compraitem.compra ");
		sql.append(" left join unidadeensino as unidadeensinoCompraItem ON compra.unidadeensino = unidadeensinoCompraItem.codigo ");
		sql.append(" left join categoriaDespesa AS categoriaDespesaCompraItem  ON compraitem.categoriaDespesa = categoriaDespesaCompraItem.codigo ");
		sql.append(" LEFT JOIN departamento as departamentoCompraItem  on compraitem.departamento = departamentoCompraItem.codigo ");
		sql.append(" LEFT JOIN curso as cursoCompraItem  on compraitem.curso = cursoCompraItem.codigo ");
		sql.append(" LEFT JOIN turma as turmaCompraItem  on compraitem.turma = turmaCompraItem.codigo ");
		sql.append(" LEFT JOIN turno as turnoCompraItem  on compraitem.turno = turnoCompraItem.codigo ");
		sql.append(" left join centroresultado AS crad ON compraitem.centroresultadoadministrativo = crad.codigo ");
		
		return sql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaCentroResultadoInterfaceFacade#consultaRapidaPorNotaFiscalEntrada(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaItemRecebimentoVO> consultaRapidaPorNotaFiscalEntrada(NotaFiscalEntradaItemVO notaFiscalEntrada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaItemRecebimento.notaFiscalEntradaItem = ").append(notaFiscalEntrada.getCodigo()).append(" ");
			sqlStr.append(" ORDER BY NotaFiscalEntradaItemRecebimento.codigo ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			List<NotaFiscalEntradaItemRecebimentoVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				NotaFiscalEntradaItemRecebimentoVO obj = new NotaFiscalEntradaItemRecebimentoVO();
				montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
				obj.setNotaFiscalEntradaItemVO(notaFiscalEntrada);
				vetResultado.add(obj);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaCentroResultadoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaFiscalEntradaItemRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaItemRecebimento.codigo = ").append(codigoPrm).append(" ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( NotaFiscalEntradaItemRecebimentoVO ).");
			}
			NotaFiscalEntradaItemRecebimentoVO obj = new NotaFiscalEntradaItemRecebimentoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaItemRecebimentoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<NotaFiscalEntradaItemRecebimentoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			NotaFiscalEntradaItemRecebimentoVO obj = new NotaFiscalEntradaItemRecebimentoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(NotaFiscalEntradaItemRecebimentoVO obj, SqlRowSet dadosSQL, int nivelMontarDados) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("NotaFiscalEntradaItemRecebimento.codigo"));
		obj.setQuantidadeNotaFiscalEntrada(dadosSQL.getDouble("NotaFiscalEntradaItemRecebimento.quantidadeNotaFiscalEntrada"));
		obj.setValorUnitario(dadosSQL.getDouble("NotaFiscalEntradaItemRecebimento.valorUnitario"));
		obj.getNotaFiscalEntradaItemVO().setCodigo(dadosSQL.getInt("notaFiscalEntradaItem.codigo"));
		
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("requisicaoitem.codigo"))){
			obj.getRequisicaoItemVO().setCodigo((dadosSQL.getInt("requisicaoitem.codigo")));
			obj.getRequisicaoItemVO().setQuantidadeAutorizada((dadosSQL.getDouble("requisicaoitem.quantidadeAutorizada")));

			obj.getRequisicaoItemVO().getRequisicaoVO().setCodigo((dadosSQL.getInt("requisicao.codigo")));
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("requisicao.tipoNivelCentroResultadoEnum"))){
				obj.getRequisicaoItemVO().getRequisicaoVO().setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(dadosSQL.getString("requisicao.tipoNivelCentroResultadoEnum")));	
			}
			obj.getRequisicaoItemVO().getRequisicaoVO().getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesa.codigo")));
			obj.getRequisicaoItemVO().getRequisicaoVO().getCategoriaDespesa().setDescricao((dadosSQL.getString("categoriaDespesa.descricao")));

			obj.getRequisicaoItemVO().getRequisicaoVO().getFuncionarioCargoVO().setCodigo((dadosSQL.getInt("funcionariocargo.codigo")));

			obj.getRequisicaoItemVO().getRequisicaoVO().getDepartamento().setCodigo((dadosSQL.getInt("departamento.codigo")));
			obj.getRequisicaoItemVO().getRequisicaoVO().getDepartamento().setNome((dadosSQL.getString("departamento.nome")));

			obj.getRequisicaoItemVO().getRequisicaoVO().getCurso().setCodigo((dadosSQL.getInt("curso.codigo")));
			obj.getRequisicaoItemVO().getRequisicaoVO().getCurso().setNome((dadosSQL.getString("curso.nome")));

			obj.getRequisicaoItemVO().getRequisicaoVO().getTurno().setCodigo((dadosSQL.getInt("turno.codigo")));
			obj.getRequisicaoItemVO().getRequisicaoVO().getTurno().setNome((dadosSQL.getString("turno.nome")));

			obj.getRequisicaoItemVO().getRequisicaoVO().getTurma().setCodigo((dadosSQL.getInt("turma.codigo")));
			obj.getRequisicaoItemVO().getRequisicaoVO().getTurma().setIdentificadorTurma((dadosSQL.getString("turma.identificadorturma")));

			obj.getRequisicaoItemVO().getRequisicaoVO().getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino.codigo")));
			obj.getRequisicaoItemVO().getRequisicaoVO().getUnidadeEnsino().setNome((dadosSQL.getString("unidadeEnsino.nome")));

			obj.getRequisicaoItemVO().getRequisicaoVO().getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("centroResultadoAdministrativo.codigo")));
			obj.getRequisicaoItemVO().getRequisicaoVO().getCentroResultadoAdministrativo().setDescricao((dadosSQL.getString("centroResultadoAdministrativo.descricao")));
			obj.getRequisicaoItemVO().getRequisicaoVO().getCentroResultadoAdministrativo().setIdentificadorCentroResultado((dadosSQL.getString("centroResultadoAdministrativo.identificadorCentroResultado")));			

		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("compraitem.codigo"))){
			obj.getCompraItemVO().setCodigo((dadosSQL.getInt("compraitem.codigo")));
			obj.getCompraItemVO().setQuantidadeAdicional((dadosSQL.getDouble("compraitem.quantidadeadicional")));
			obj.getCompraItemVO().setQuantidadeRequisicao((dadosSQL.getDouble("compraitem.quantidaderequisicao")));
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("compraitem.tipoNivelCentroResultadoEnum"))){
				obj.getCompraItemVO().setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(dadosSQL.getString("compraitem.tipoNivelCentroResultadoEnum")));	
			}

			obj.getCompraItemVO().getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesaCompraItem.codigo")));
			obj.getCompraItemVO().getCategoriaDespesa().setDescricao((dadosSQL.getString("categoriaDespesaCompraItem.descricao")));
			
			obj.getCompraItemVO().getDepartamentoVO().setCodigo((dadosSQL.getInt("departamentoCompraItem.codigo")));
			obj.getCompraItemVO().getDepartamentoVO().setNome((dadosSQL.getString("departamentoCompraItem.nome")));

			obj.getCompraItemVO().getCursoVO().setCodigo((dadosSQL.getInt("cursoCompraItem.codigo")));
			obj.getCompraItemVO().getCursoVO().setNome((dadosSQL.getString("cursoCompraItem.nome")));

			obj.getCompraItemVO().getTurnoVO().setCodigo((dadosSQL.getInt("turnoCompraItem.codigo")));
			obj.getCompraItemVO().getTurnoVO().setNome((dadosSQL.getString("turnoCompraItem.nome")));

			obj.getCompraItemVO().getTurma().setCodigo((dadosSQL.getInt("turmaCompraItem.codigo")));
			obj.getCompraItemVO().getTurma().setIdentificadorTurma((dadosSQL.getString("turmaCompraItem.identificadorturma")));

			obj.getCompraItemVO().getCompra().getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsinoCompraItem.codigo")));
			obj.getCompraItemVO().getCompra().getUnidadeEnsino().setNome((dadosSQL.getString("unidadeEnsinoCompraItem.nome")));


			obj.getCompraItemVO().getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("crad.codigo")));
			obj.getCompraItemVO().getCentroResultadoAdministrativo().setDescricao((dadosSQL.getString("crad.descricao")));
			obj.getCompraItemVO().getCentroResultadoAdministrativo().setIdentificadorCentroResultado((dadosSQL.getString("crad.identificadorCentroResultado")));
		}
		

		
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return NotaFiscalEntradaItemRecebimento.idEntidade;
	}

}
