package negocio.facade.jdbc.financeiro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CondicaoDescontoRenegociacaoVO;
import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CondicaoDescontoRenegociacaoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CondicaoDescontoRenegociacao extends ControleAcesso implements CondicaoDescontoRenegociacaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5376903361683057025L;
	protected static String idEntidade;

	public CondicaoDescontoRenegociacao() {
		super();
		setIdEntidade("CondicaoDescontoRenegociacao");
	}

	public void validarDados(CondicaoDescontoRenegociacaoVO obj)  {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDescricao()), "O Campo Descrição ( CondicaoDescontoRenegociacao ) deve ser informado.");
		Uteis.checkState(obj.getItemCondicaoDescontoRenegociacaoVOs().isEmpty(), "Deve existir pelo menos uma Item para as regra de condições desconto renegociação. ");
		for (ItemCondicaoDescontoRenegociacaoVO icdr : obj.getItemCondicaoDescontoRenegociacaoVOs()) {
			getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().validarDados(icdr);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CondicaoDescontoRenegociacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioLogado) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioLogado);
		} else {
			alterar(obj, verificarAcesso, usuarioLogado);
		}
		getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().persistir(obj, obj.getItemCondicaoDescontoRenegociacaoVOs(), false, usuarioLogado);
	}

	private void incluir(final CondicaoDescontoRenegociacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioLogado) throws Exception {
		try {
			CondicaoDescontoRenegociacao.incluir(getIdEntidade(), verificarAcesso, usuarioLogado);
			final String sql = "INSERT INTO CondicaoDescontoRenegociacao( descricao ) VALUES ( ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException {
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
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}

	}

	private void alterar(final CondicaoDescontoRenegociacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioLogado) throws Exception {
		try {
			CondicaoDescontoRenegociacao.alterar(getIdEntidade(), verificarAcesso, usuarioLogado);
			final String sql = "UPDATE CondicaoDescontoRenegociacao set descricao=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CondicaoDescontoRenegociacaoVO obj, boolean verificarAcesso, UsuarioVO usuarioLogado) throws Exception {
		CondicaoDescontoRenegociacao.alterar(getIdEntidade(), verificarAcesso, usuarioLogado);
		String sql = "DELETE FROM CondicaoDescontoRenegociacao WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT *  FROM condicaodescontorenegociacao ");
		sql.append(" ");
		return sql;
	}

	private StringBuilder getSQLPadraoConsultaTotalBasica() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT count(condicaodescontorenegociacao.codigo) as qtde FROM condicaodescontorenegociacao ");
		sql.append(" ");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<CondicaoDescontoRenegociacaoVO> consultar(String valorConsulta, String campoConsulta, DataModelo dataModelo) throws Exception {
		List<CondicaoDescontoRenegociacaoVO> objs = new ArrayList<>();
		if (campoConsulta.equals("codigo")) {
			Uteis.checkState(valorConsulta.trim().equals(""), UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
			int valorInt = Uteis.getValorInteiro(valorConsulta);
			objs = consultarPorCodigo(valorInt, dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(consultarTotalPorCodigo(valorInt, dataModelo));
		}
		if (campoConsulta.equals("descricao")) {
			Uteis.checkState(valorConsulta.trim().equals(""), UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
			objs = consultarPorDescricao(valorConsulta, dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(consultarTotalPorDescricao(valorConsulta, dataModelo));
		}
		return objs;
	}

	public List<CondicaoDescontoRenegociacaoVO> consultarPorCodigo(Integer valorConsulta, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE codigo = ?");
		dataModelo.getListaFiltros().add(valorConsulta);
		sqlStr.append(" ORDER BY codigo ");
		UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosConsulta(rs, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
	}

	public Integer consultarTotalPorCodigo(Integer valorConsulta, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE codigo = ?");
			dataModelo.getListaFiltros().add(valorConsulta);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public List<CondicaoDescontoRenegociacaoVO> consultarPorDescricao(String valorConsulta, DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lower(descricao) like (?)");
		dataModelo.getListaFiltros().add(valorConsulta.toLowerCase() + PERCENT);
		sqlStr.append(" ORDER BY descricao ");
		UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosConsulta(rs, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
	}

	public Integer consultarTotalPorDescricao(String valorConsulta, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE lower(descricao) like (?)");
			dataModelo.getListaFiltros().add(valorConsulta.toLowerCase() + PERCENT);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public List<CondicaoDescontoRenegociacaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<CondicaoDescontoRenegociacaoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static CondicaoDescontoRenegociacaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		CondicaoDescontoRenegociacaoVO obj = new CondicaoDescontoRenegociacaoVO();
		obj.setNovoObj(false);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setItemCondicaoDescontoRenegociacaoVOs(getFacadeFactory().getItemCondicaoDescontoRenegociacaoFacade().consultarItemCondicaoDescontoRenegociacaoPorCondicaoDescontoRenegociacao(obj.getCodigo(), nivelMontarDados, usuarioVO));
		return obj;
	}

	public CondicaoDescontoRenegociacaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CondicaoDescontoRenegociacao.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM CondicaoDescontoRenegociacao WHERE codigo = " + codigoPrm;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( CondicaoDescontoRenegociacao ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return CondicaoDescontoRenegociacao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		CondicaoDescontoRenegociacao.idEntidade = idEntidade;
	}

	@Override
	public void adicionarObjItemCondicaoDescontoRenegociacaoVOs(CondicaoDescontoRenegociacaoVO obj, ItemCondicaoDescontoRenegociacaoVO icdr) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(icdr.getQuantidadeDiasAtrasoParcela()), "O Campo Quantidade Dias Atraso Parcela ( ItemCondicaoDescontoRenegociacao ) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(icdr.getQuantidadeDiasAtrasoParcelaFinal()), "O Campo Quantidade Dias Atraso Parcela Final ( ItemCondicaoDescontoRenegociacao ) deve ser informado.");
		if(!icdr.getConsiderarDescontoProgressivoPerdidoDevidoDataAntecipacao() && Uteis.isAtributoPreenchido(icdr.getPercentualDescontoProgressivo().doubleValue())){
			icdr.setPercentualDescontoProgressivo(BigDecimal.ZERO);
		}
		if(!icdr.getConsiderarDescontoAlunoPerdidoDevidoDataAntecipacao() && Uteis.isAtributoPreenchido(icdr.getPercentualDescontoAluno().doubleValue())){
			icdr.setPercentualDescontoAluno(BigDecimal.ZERO);
		}
		if(!icdr.getConsiderarPlanoDescontoPerdidoDevidoDataAntecipacao() && Uteis.isAtributoPreenchido(icdr.getPercentualPlanoDesconto().doubleValue())){
			icdr.setPercentualPlanoDesconto(BigDecimal.ZERO);
		}
		/*obj.getItemCondicaoDescontoRenegociacaoVOs().stream().filter(p->
		   ((icdr.getQuantidadeDiasAtrasoParcela() >= p.getQuantidadeDiasAtrasoParcela() && icdr.getQuantidadeDiasAtrasoParcela() <= p.getQuantidadeDiasAtrasoParcelaFinal()) || (icdr.getQuantidadeDiasAtrasoParcelaFinal() >= p.getQuantidadeDiasAtrasoParcela() && icdr.getQuantidadeDiasAtrasoParcelaFinal() <= p.getQuantidadeDiasAtrasoParcelaFinal()))
		   
		);*/
		
		icdr.setCondicaoDescontoRenegociacaoVO(obj);
		int index = 0;
		for (ItemCondicaoDescontoRenegociacaoVO icdrExistente : obj.getItemCondicaoDescontoRenegociacaoVOs()) {
			if (icdrExistente.equalsItemCondicaoDescontoRenegociacaoVO(icdr)) {
				Uteis.checkState(icdrExistente.getSituacao().isAtivo() && !icdr.getSituacao().isAtivo(), "Já existe uma Condições Regra de Renegociação de Conta Receber Vencida para o período de dias de atraso parcela  " + icdr.getQuantidadeDiasAtrasoParcela() + " até " + icdr.getQuantidadeDiasAtrasoParcelaFinal() +" com a situação Ativada.");
				obj.getItemCondicaoDescontoRenegociacaoVOs().set(index, icdr);
				return;
			}
			index++;
		}
		obj.getItemCondicaoDescontoRenegociacaoVOs().add(icdr);
		Ordenacao.ordenarLista(obj.getItemCondicaoDescontoRenegociacaoVOs(), "quantidadeDiasAtrasoParcela");

	}

	@Override
	public void removerObjItemCondicaoDescontoRenegociacaoVOs(CondicaoDescontoRenegociacaoVO obj, ItemCondicaoDescontoRenegociacaoVO icdr) throws Exception {
		Iterator<ItemCondicaoDescontoRenegociacaoVO> i = obj.getItemCondicaoDescontoRenegociacaoVOs().iterator();
		while (i.hasNext()) {
			ItemCondicaoDescontoRenegociacaoVO icdrExistente = i.next();
			if (icdrExistente.equalsItemCondicaoDescontoRenegociacaoVO(icdr)) {
				i.remove();
			}
		}
	}

}