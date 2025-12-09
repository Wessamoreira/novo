package relatorio.negocio.jdbc.compras;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.compras.CompraRelVO;
import relatorio.negocio.interfaces.compras.CompraRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings({"serial","deprecation"})
@Scope("singleton")
@Repository
@Lazy
public class CompraRel extends SuperRelatorio implements CompraRelInterfaceFacade {

	private static String idEntidade;

	public CompraRel() {
		setIdEntidade("CompraRel");
	}

	@Override
	public List<CompraRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, FornecedorVO fornecedor,  Integer numeroCompra, Double valorCompraInicio, Double valorCompraFim, String situacaoRecebimento, String situacaoFinanceira, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT compra.codigo as numeroCompra, ");
		sqlStr.append("compra.data as dataCompra, ");	  
		sqlStr.append("fornecedor.nome as nomeFornecedor, ");
		sqlStr.append("fornecedor.cnpj as cnpjFornecedor, ");
		sqlStr.append("fornecedor.cpf as cpfFornecedor, ");
		sqlStr.append("fornecedor.tipoempresa, ");
		sqlStr.append("fornecedor.endereco as enderecoFornecedor, ");
		sqlStr.append("fornecedor.contato as contatoFornecedor, ");
		sqlStr.append("fornecedor.telcomercial1 as telefone1Fornecedor, ");
		sqlStr.append("fornecedor.telcomercial2 as telefone2Fornecedor, ");
		sqlStr.append("fornecedor.telcomercial3 as telefone3Fornecedor, ");
		sqlStr.append("produtoservico.nome as nomeProduto, ");
		sqlStr.append("compraitem.quantidade,");
		sqlStr.append("compraitem.quantidadeadicional,");		
		sqlStr.append("compraitem.precounitario, ");
		sqlStr.append("condicaopagamento.nome as condicaoPagamento, ");
		sqlStr.append("formapagamento.nome as formaPagamento, ");
		sqlStr.append("unidademedida.nome as unidadeMedida ");
		
		sqlStr.append("FROM compra ");
		sqlStr.append("INNER JOIN fornecedor on (compra.fornecedor=fornecedor.codigo) ");
		sqlStr.append("INNER JOIN condicaopagamento on (compra.condicaopagamento=condicaopagamento.codigo) ");
		sqlStr.append("INNER JOIN formapagamento on (compra.formapagamento=formapagamento.codigo) ");
		sqlStr.append("INNER JOIN compraitem on (compra.codigo=compraitem.compra) ");
		sqlStr.append("INNER JOIN produtoservico on (compraitem.produto=produtoservico.codigo) ");
		sqlStr.append("INNER JOIN unidademedida on (produtoservico.unidademedida=unidademedida.codigo) ");
		//sqlStr.append("WHERE contareceber.tipoorigem = 'BCC' ");
		if (dataInicio != null) {
			sqlStr.append("WHERE compra.data BETWEEN '").append(Uteis.getDataBD0000(dataInicio)).append("' AND '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		} else {
			sqlStr.append("WHERE compra.data < '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}

		if (!listaUnidadeEnsino.isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND compra.unidadeensino IN(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (!virgula) {
						sqlStr.append(unidadeEnsinoVO.getCodigo());
					} else {
						sqlStr.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
					virgula = true;
				}
			}
			sqlStr.append(") ");
		}

		if(fornecedor.getCodigo() != null && fornecedor.getCodigo() != 0) {
			sqlStr.append(" AND fornecedor.codigo = ").append(fornecedor.getCodigo());

		}
		
		if(!situacaoRecebimento.equals("")) {
			sqlStr.append(" AND compra.situacaorecebimento = '").append(situacaoRecebimento).append("'");
		}
		
		if(!situacaoFinanceira.equals("")) {

			sqlStr.append(" AND compra.situacaofinanceira = '").append(situacaoFinanceira).append("'");
		}
				
		if(numeroCompra != null) {
			sqlStr.append(" AND compra.codigo = ").append(numeroCompra);
		}
		
		if((valorCompraInicio > 0) && (valorCompraFim > 0)) {
			sqlStr.append(" AND (select sum(precounitario  * (quantidade + quantidadeadicional)) from compraitem  where compraitem.compra = compra.codigo) >= " ).append(valorCompraInicio);
			sqlStr.append(" AND (select sum(precounitario  * (quantidade + quantidadeadicional)) from compraitem  where compraitem.compra = compra.codigo) <= " ).append(valorCompraFim);
		}
		
		
		sqlStr.append(" ORDER BY fornecedor.nome, compra.codigo, produtoservico.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado);

	}

	@Override
	public List<CompraRelVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<CompraRelVO> vetResultado = new ArrayList<CompraRelVO>(0);
		while (tabelaResultado.next()) {
			CompraRelVO compraRelVO = montarDados(tabelaResultado);
			vetResultado.add(compraRelVO);
		}
		return vetResultado;
	}

	@Override
	public CompraRelVO montarDados(SqlRowSet dadosSQL) throws Exception {
		CompraRelVO obj = new CompraRelVO();
		//dadosSQL.getDouble("quantidaderecebida");
		//dadosSQL.getDouble("quantidadeadicional");
		obj.setNumeroCompra(dadosSQL.getInt("numeroCompra"));
		obj.setDataCompra(dadosSQL.getDate("dataCompra"));
		obj.setNomeFornecedor(dadosSQL.getString("nomeFornecedor"));
		obj.setCnpjFornecedor(dadosSQL.getString("cnpjFornecedor"));
		obj.setCpfFornecedor(dadosSQL.getString("cpfFornecedor"));
		obj.setTipoEmpresa(dadosSQL.getString("tipoEmpresa"));
		obj.setEnderecoFornecedor(dadosSQL.getString("enderecoFornecedor"));
		obj.setNomeProduto(dadosSQL.getString("nomeProduto"));
		obj.setQuantidadeProduto(dadosSQL.getDouble("quantidade"));
		obj.setPrecoUnitario(dadosSQL.getDouble("precoUnitario"));
		obj.setValorTotal((dadosSQL.getDouble("quantidade")) * dadosSQL.getDouble("precoUnitario"));
		obj.setCondicaoPagamento(dadosSQL.getString("condicaoPagamento"));
		obj.setFormaPagamento(dadosSQL.getString("formaPagamento"));
		obj.setUnidadeMedida(dadosSQL.getString("unidadeMedida"));
		obj.setContatoFornecedor(dadosSQL.getString("contatoFornecedor"));
		obj.setTelefone1Fornecedor(dadosSQL.getString("telefone1Fornecedor"));
		obj.setTelefone2Fornecedor(dadosSQL.getString("telefone2Fornecedor"));
		obj.setTelefone3Fornecedor(dadosSQL.getString("telefone3Fornecedor"));
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

	@Override
	public void validarDados(FornecedorVO fornecedor, Date dataInicio, Date dataFim, Double valorCompraInicio, Double valorCompraFim) throws Exception {

		if(valorCompraInicio > 0 && valorCompraFim == 0) {
			throw new Exception("O valor final da compra deve ser informado para a geração do relatório.");
		}
		
		if(dataInicio == null) {
			throw new Exception("A Data Inicial deve ser informada para a geração do relatório.");
		}
		else if (dataFim == null) {
			throw new Exception("A Data Final deve ser informada para a geração do relatório.");
		}else { 

			ZoneId defaultZoneId = ZoneId.systemDefault();				

			LocalDate ldDataInicio = dataInicio.toInstant().atZone(defaultZoneId).toLocalDate();
			LocalDate ldDataFim = dataFim.toInstant().atZone(defaultZoneId).toLocalDate();

			Period periodo = Period.between(ldDataInicio, ldDataFim);
			
			if(fornecedor.getCodigo() == null || fornecedor.getCodigo() == 0) {	
				if((periodo.getYears() > 1) || (periodo.getYears() == 1 && (periodo.getMonths() > 0 || periodo.getDays() > 0))) {
					throw new Exception("O intervalo deve ser menor que um ano para a geração do relatório.");
				}
			}

			if(ldDataInicio.isAfter(LocalDate.now())){
				throw new Exception("A Data Inicial não pode ser maior que a data atual.");
			}

			if (ldDataFim.isAfter(LocalDate.now())) {
				throw new Exception("A Data Final não pode ser maior que a data atual.");
			}
			if (ldDataInicio.isAfter(ldDataFim)) {
				throw new Exception("A Data Inicial não pode ser maior que a data Final.");
			}
		}
	}

	public static void setIdEntidade(String idEntidade) {
		CompraRel.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

}
