package relatorio.negocio.jdbc.compras;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.compras.ProdutoServicoRelVO;
import relatorio.negocio.interfaces.compras.ProdutoServicoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings({"serial","deprecation"})
@Scope("singleton")
@Repository
@Lazy
public class ProdutoServicoRel extends SuperRelatorio implements ProdutoServicoRelInterfaceFacade {

	private static String idEntidade;

	public ProdutoServicoRel() {
		setIdEntidade("ProdutoServicoRel");
	}

	@Override
	public List<ProdutoServicoRelVO> criarObjeto(Integer categoriaProduto, String tipoProduto, String situacaoProduto, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT produtoservico.nome as produto, ");
		sqlStr.append("produtoservico.tipoprodutoservico as tipoProduto, ");	  
		sqlStr.append("produtoservico.situacao as situacaoProduto, ");
		sqlStr.append("categoriaproduto.nome  as categoriaProduto ");		
		sqlStr.append("FROM produtoservico ");
		sqlStr.append("INNER JOIN categoriaproduto on (produtoservico.categoriaproduto=categoriaproduto.codigo) ");
		sqlStr.append(" WHERE 1=1 ");
		if(categoriaProduto > 0) {
			sqlStr.append("AND categoriaproduto.codigo = ").append(categoriaProduto);
		}
		
		if(!tipoProduto.equals("")) {
			sqlStr.append("AND produtoservico.tipoprodutoservico = '").append(tipoProduto).append("'");
		}
		
		if(!situacaoProduto.equals("")) {
			sqlStr.append("AND produtoservico.situacao = '").append(situacaoProduto).append("'");
		}
		
		sqlStr.append(" ORDER BY categoriaproduto.nome, produtoservico.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado);

	}

	@Override
	public List<ProdutoServicoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<ProdutoServicoRelVO> vetResultado = new ArrayList<ProdutoServicoRelVO>(0);
		while (tabelaResultado.next()) {
			ProdutoServicoRelVO produtoServicoRelVO = montarDados(tabelaResultado);
			vetResultado.add(produtoServicoRelVO);
		}
		return vetResultado;
	}

	@Override
	public ProdutoServicoRelVO montarDados(SqlRowSet dadosSQL) throws Exception {
		ProdutoServicoRelVO obj = new ProdutoServicoRelVO();
		obj.setProduto(dadosSQL.getString("produto"));
		obj.setCategoriaProduto(dadosSQL.getString("categoriaProduto"));
		obj.setTipoProduto(dadosSQL.getString("tipoProduto"));
		obj.setSituacaoProduto(dadosSQL.getString("situacaoProduto"));
		return obj;
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras");
	}

	@Override
	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator + getIdEntidade() + ".jrxml");
	}

	@Override
	public String designIReportRelatorioExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator + getIdEntidade() + ".jrxml");
	}


	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator);
	}

	public static void setIdEntidade(String idEntidade) {
		ProdutoServicoRel.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

}
