package negocio.facade.jdbc.compras;

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
import negocio.comuns.compras.EntregaRequisicaoItemVO;
import negocio.comuns.compras.EntregaRequisicaoVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.enumeradores.OperacaoEstoqueEnum;
import negocio.comuns.compras.enumeradores.TipoOperacaoEstoqueOrigemEnum;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.EntregaRequisicaoItemInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class EntregaRequisicaoItem extends ControleAcesso implements EntregaRequisicaoItemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7953384012491787670L;
	protected static String idEntidade;

	public EntregaRequisicaoItem() {
		super();
		setIdEntidade("EntregaRequisicao");
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirEntregaRequisicaoItems(EntregaRequisicaoVO obj, UsuarioVO usuario) throws Exception {
		for (EntregaRequisicaoItemVO eri : obj.getEntregaRequisicaoItemVOs()) {
			eri.setEntregaRequisicaoVO(obj);			
			incluir(eri, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final EntregaRequisicaoItemVO obj, UsuarioVO usuario) throws Exception {
		EntregaRequisicaoItemVO.validarDados(obj);
		final String sql = "INSERT INTO EntregaRequisicaoItem( requisicaoItem, entregaRequisicao, quantidade, centroResultadoEstoque ) VALUES ( ?, ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				int i = 1;
				Uteis.setValuePreparedStatement(obj.getRequisicaoItem(), i++, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getEntregaRequisicaoVO(), i++, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getQuantidade(), i++, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getCentroResultadoEstoque(), i++, sqlInserir);
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
		
		
		if (obj.getRequisicaoItem().getProdutoServico().getControlarEstoque()) {
			Integer unidadeEnsino = Uteis.isAtributoPreenchido(obj.getRequisicaoItem().getUnidadeEnsinoEstoqueRetirada()) ? obj.getRequisicaoItem().getUnidadeEnsinoEstoqueRetirada().getCodigo() : obj.getEntregaRequisicaoVO().getRequisicao().getUnidadeEnsino().getCodigo(); 
			obj.setListaOperacaoEstoqueVOs(Estoque.manipularEstoque(obj.getCodigo().toString(), TipoOperacaoEstoqueOrigemEnum.ENTREGA_REQUISICAO_ITEM, obj.getRequisicaoItem().getProdutoServico().getCodigo(), obj.getQuantidade(), 0.0, null, unidadeEnsino, OperacaoEstoqueEnum.EXCLUIR, usuario));
			obj.getRequisicaoItem().getUnidadeEnsinoEstoqueRetirada().setCodigo(unidadeEnsino);
		}		
		obj.getRequisicaoItem().setQuantidadeEntregue(obj.getRequisicaoItem().getQuantidadeEntregue() + obj.getQuantidade());
		getFacadeFactory().getRequisicaoItemFacade().atualizarRequisitoItemPorEntregaRequisicaoItem(obj.getRequisicaoItem(), usuario);

	}	

	
	public EntregaRequisicaoItemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		EntregaRequisicaoItemVO obj = new EntregaRequisicaoItemVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getRequisicaoItem().setCodigo((dadosSQL.getInt("requisicaoItem")));
		obj.getEntregaRequisicaoVO().setCodigo((dadosSQL.getInt("entregaRequisicao")));
		obj.setQuantidade((dadosSQL.getDouble("quantidade")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosRequisicaoItem(obj, nivelMontarDados, usuario);
		obj.setCentroResultadoEstoque(Uteis.montarDadosVO(dadosSQL.getInt("centroResultadoEstoque"), CentroResultadoVO.class, p -> getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		Integer unidadeEnsino = Uteis.isAtributoPreenchido(obj.getRequisicaoItem().getUnidadeEnsinoEstoqueRetirada()) ? obj.getRequisicaoItem().getUnidadeEnsinoEstoqueRetirada().getCodigo() : obj.getRequisicaoItem().getRequisicaoVO().getUnidadeEnsino().getCodigo();
		obj.setListaOperacaoEstoqueVOs(getFacadeFactory().getOperacaoEstoqueFacade().consultaRapidaPorCodOrigemPorTipoOperacaoEstoqueOrigemEnum(obj.getCodigo().toString(), TipoOperacaoEstoqueOrigemEnum.ENTREGA_REQUISICAO_ITEM, unidadeEnsino, nivelMontarDados, usuario));
		return obj;
	}

	
	public static void montarDadosRequisicaoItem(EntregaRequisicaoItemVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getRequisicaoItem().getCodigo().intValue() == 0) {
			obj.setRequisicaoItem(new RequisicaoItemVO());
			return;
		}
		obj.setRequisicaoItem(getFacadeFactory().getRequisicaoItemFacade().consultarPorChavePrimaria(obj.getRequisicaoItem().getCodigo(), nivelMontarDados, usuario));
	}

	public List<EntregaRequisicaoItemVO> consultarEntregaRequisicaoItems(Integer entregaRequisicao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		EntregaRequisicaoItem.consultar(getIdEntidade());
		List<EntregaRequisicaoItemVO> objetos = new ArrayList<>(0);
		String sql = "SELECT * FROM EntregaRequisicaoItem WHERE entregaRequisicao = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, entregaRequisicao);
		while (resultado.next()) {
			EntregaRequisicaoItemVO novoObj = montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	public EntregaRequisicaoItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM EntregaRequisicaoItem WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( EntregaRequisicaoItem ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return EntregaRequisicaoItem.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		EntregaRequisicaoItem.idEntidade = idEntidade;
	}
}