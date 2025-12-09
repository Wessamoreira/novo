package relatorio.negocio.jdbc.processosel;

import java.io.File;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.interfaces.processosel.EmitirBoletoInscricaoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class EmitirBoletoInscricaoRel extends SuperRelatorio implements EmitirBoletoInscricaoRelInterfaceFacade {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.processosel.EmitirBoletoInscricaoRelInterfaceFacade#emitirRelatorio(java.lang.Integer)
	 */
	public String emitirRelatorio(Integer codigo) throws Exception {
		
		converterResultadoConsultaParaXML(executarConsultaParametrizada(codigo), EmitirBoletoInscricaoRel.getIdEntidade(), "registros");
		return getXmlRelatorio();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.processosel.EmitirBoletoInscricaoRelInterfaceFacade#executarConsultaParametrizada(java
	 * .lang.Integer)
	 */
	public SqlRowSet executarConsultaParametrizada(Integer codigo) throws Exception {
		String selectStr = "SELECT " + "contareceber.codigobarra as contareceber_codigobarra, " + "contareceber.data as contareceber_data, " + "contareceber.tipoorigem as contareceber_tipoorigem, "
				+ "contareceber.descricaopagamento as contareceber_descricaopagamento, " + "contareceber.datavencimento as contareceber_datavencimento, "
				+ "contareceber.valor as contareceber_valor, " + "contareceber.valordesconto as contareceber_valordesconto, " + "contareceber.juro as contareceber_juro, "
				+ "contareceber.juroporcentagem as contareceber_juroporcentagem, " + "contareceber.multa as contareceber_multa, " + "contareceber.multaporcentagem as contareceber_multaporcentagem, "
				+ "contareceber.nrdocumento as contareceber_nrdocumento, " + "contareceber.parcela as contareceber_parcela, "
				+ "contareceber.origemnegociacaoreceber as contareceber_origemnegociacaoreceber, " + "contareceber.contacorrente as contareceber_contacorrente, "
				+ "contacorrente.agencia as  contacorrente_agencia, " + "contacorrente.numero as  contacorrente_numero, " + "agencia.numeroagencia as agencia_numeroagencia, "
				+ "pessoa.nome as pessoa_nome, " + "pessoa.endereco as pessoa_endereco, " + "pessoa.setor as pessoa_setor, " + "pessoa.cep as pessoa_cep, " + "pessoa.cidade as pessoa_cidade, "
				+ "pessoa.cpf as pessoa_cpf ," + "cidade.nome as cidade_nome," + "cidade.estado as cidade_estado, " + "agencia.banco as agencia_banco, " + "banco.nome as banco_nome "
				+ "from contareceber left join pessoa on contareceber.pessoa = pessoa.codigo "
				+ "left join contacorrente on contareceber.contacorrente = contacorrente.codigo, agencia ,cidade , banco " + "where (contareceber.codigo = "
				+ codigo.intValue()
				+ ") and (agencia.codigo = contacorrente.agencia) and (cidade.codigo=pessoa.cidade) "
				+ "and (banco.codigo = agencia.banco)"
				+ " Group By "
				+ "pessoa_nome , "
				+ "contareceber_codigobarra, "
				+ "contareceber_data, "
				+ "contareceber_tipoorigem, "
				+ "contareceber_descricaopagamento, "
				+ "contareceber_datavencimento, "
				+ "contareceber_valor, "
				+ "contareceber_valordesconto, "
				+ "contareceber_juro, "
				+ "contareceber_juroporcentagem, "
				+ "contareceber_multa, "
				+ "contareceber_multaporcentagem, "
				+ "contareceber_nrdocumento, "
				+ "contareceber_parcela, "
				+ "contareceber_origemnegociacaoreceber, "
				+ "contacorrente_agencia, "
				+ "contareceber_contacorrente, "
				+ "agencia_numeroagencia, "
				+ "contacorrente_numero, "
				+ "pessoa_cpf, "
				+ "pessoa_setor, "
				+ "pessoa_endereco, "
				+ "pessoa_cep, "
				+ "pessoa_cidade, "
				+ "cidade_nome, "
				+ "agencia_banco, "
				+ "banco_nome, "
				+ "cidade_estado";
		return (getConexao().getJdbcTemplate().queryForRowSet(selectStr));
	}

	/**
	 * Operação reponsável por retornar o arquivo (caminho e nome) correspondente ao design do relatório criado pelo
	 * IReport.
	 */
	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getIdEntidade() {
		return ("EmitirBoletoInscricaoRel");
	}
}
