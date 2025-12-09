package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import negocio.comuns.administrativo.UnidadeEnsinoVO;

import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.ExtratoContaPagarRelVO;
import relatorio.negocio.interfaces.financeiro.ExtratoContaPagarRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ExtratoContaPagarRel extends SuperRelatorio implements ExtratoContaPagarRelInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1079942132388003993L;

	public void validarDados(Integer codigoFavorecido, Date dataInicio, Date dataFim) throws Exception {
        if (codigoFavorecido == null || codigoFavorecido.equals(0)) {
            throw new ConsistirException("O Favorecido deve ser informado para a geração do relatório.");
        }
        if (dataFim == null || dataInicio == null) {
            throw new ConsistirException("O período deve ser informado para a geração do relatório.");
        }
        if (dataFim.before(dataInicio)) {
            throw new ConsistirException("A Data Fim está menor que a Data Início.");
        }
    }

    public List<ExtratoContaPagarRelVO> criarObjeto(Integer codigoFavorecido, String nomeFavorecido, String filtroTipoFavorecido, String filtroTipoData, Date dataInicio, Date dataFim, UnidadeEnsinoVO unidadeEnsino) throws Exception {
        List<ExtratoContaPagarRelVO> listaExtratoContaPagarRelVO = new ArrayList<ExtratoContaPagarRelVO>(0);
        ExtratoContaPagarRelVO extratoContaPagarRelVO = new ExtratoContaPagarRelVO();

        SqlRowSet tabelaResultado = executarConsultaParametrizada(codigoFavorecido, filtroTipoFavorecido, filtroTipoData, dataInicio, dataFim, unidadeEnsino.getCodigo());
        while (tabelaResultado.next()) {
            extratoContaPagarRelVO.getListaContaPagarVOs().add(montarDados(tabelaResultado, extratoContaPagarRelVO));
        }
        if (!extratoContaPagarRelVO.getListaContaPagarVOs().isEmpty()) {
            extratoContaPagarRelVO.setFavorecido(nomeFavorecido);
            montarDadosBancoRecebimento(extratoContaPagarRelVO);
            listaExtratoContaPagarRelVO.add(extratoContaPagarRelVO);
        }
        return listaExtratoContaPagarRelVO;
    }

    private ContaPagarVO montarDados(SqlRowSet dadosSQL, ExtratoContaPagarRelVO extratoContaPagarRelVO) {
        ContaPagarVO contaPagar = new ContaPagarVO();
        contaPagar.getOperadoraCartao().setCodigo(dadosSQL.getInt("operadoracartaoCodigo"));
        contaPagar.getOperadoraCartao().setNome(dadosSQL.getString("operadoracartaoNome"));
        contaPagar.getFornecedor().setCodigo(dadosSQL.getInt("fornecedorCodigo"));
        contaPagar.getFornecedor().setNome(dadosSQL.getString("fornecedorNome"));
        contaPagar.getFornecedor().setNomeBanco(dadosSQL.getString("fornecedor.nomeBanco"));
        contaPagar.getFornecedor().setNumeroBancoRecebimento(dadosSQL.getString("fornecedor.numeroBancoRecebimento"));
        contaPagar.getFornecedor().setNumeroAgenciaRecebimento(dadosSQL.getString("fornecedor.numeroAgenciaRecebimento"));
        contaPagar.getFornecedor().setContaCorrenteRecebimento(dadosSQL.getString("fornecedor.contaCorrenteRecebimento"));
        contaPagar.getFuncionario().setCodigo(dadosSQL.getInt("funcionario.codigo"));
        contaPagar.getFuncionario().getPessoa().setCodigo(dadosSQL.getInt("pessoafuncionarioCodigo"));
        contaPagar.getFuncionario().getPessoa().setNome(dadosSQL.getString("pessoafuncionarioNome"));
        contaPagar.getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("responsavelFinanceiro.codigo"));
        contaPagar.getResponsavelFinanceiro().setNome(dadosSQL.getString("responsavelFinanceiro.nome"));
        contaPagar.getParceiro().setCodigo(dadosSQL.getInt("parceiro.codigo"));
        contaPagar.getParceiro().setNome(dadosSQL.getString("parceiro.nome"));
        contaPagar.getFuncionario().setNomeBanco(dadosSQL.getString("funcionario.nomeBanco"));
        contaPagar.getFuncionario().setNumeroBancoRecebimento(dadosSQL.getString("funcionario.numeroBancoRecebimento"));
        contaPagar.getFuncionario().setNumeroAgenciaRecebimento(dadosSQL.getString("funcionario.numeroAgenciaRecebimento"));
        contaPagar.getFuncionario().setContaCorrenteRecebimento(dadosSQL.getString("funcionario.contaCorrenteRecebimento"));
        contaPagar.getBanco().setCodigo(dadosSQL.getInt("bancoCodigo"));
        contaPagar.getBanco().setNome(dadosSQL.getString("bancoNome"));
        contaPagar.getPessoa().setCodigo(dadosSQL.getInt("pessoaCodigo"));
        contaPagar.getPessoa().setNome(dadosSQL.getString("pessoaNome"));
        contaPagar.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsinoCodigo"));
        contaPagar.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsinoNome"));
        contaPagar.setCodigo(new Integer(dadosSQL.getInt("contaPagarCodigo")));
        contaPagar.setDataVencimento(dadosSQL.getDate("dataVencimento"));
        contaPagar.setDataFatoGerador(dadosSQL.getDate("dataFatoGerador"));
        contaPagar.setSituacao(SituacaoFinanceira.getDescricao(dadosSQL.getString("contaPagarSituacao")));
        contaPagar.setValor(new Double(dadosSQL.getDouble("contaPagarValor")));
        contaPagar.setDescricao(dadosSQL.getString("contaPagarDescricao"));
        contaPagar.setNumeroNotaFiscalEntrada(dadosSQL.getString("contaPagarNumeroNotaFiscalEntrada"));
        contaPagar.setParcela(dadosSQL.getString("contaPagarParcela"));
        contaPagar.setTipoOrigem(dadosSQL.getString("contaPagarTipoOrigem"));
        if (contaPagar.getSituacao().equals(SituacaoFinanceira.A_PAGAR.getDescricao())) {
            extratoContaPagarRelVO.setTotalPagar(extratoContaPagarRelVO.getTotalPagar() + contaPagar.getValor());
        }
        if (contaPagar.getSituacao().equals(SituacaoFinanceira.PAGO.getDescricao())) {
            extratoContaPagarRelVO.setTotalPago(extratoContaPagarRelVO.getTotalPago() + contaPagar.getValor());
        }
        contaPagar.setListaCentroResultadoOrigemVOs(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(contaPagar.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, Uteis.NIVELMONTARDADOS_TODOS, null));
        return contaPagar;
    }

    public void calcularTotalPagarTotalPagoExtratoContaPagar(ExtratoContaPagarRelVO extratoContaPagarRelVO, List<ContaPagarVO> listaContaPagar) {
        for (ContaPagarVO contaPagar : listaContaPagar) {
            if (contaPagar.getSituacao().equals(SituacaoFinanceira.A_PAGAR.getDescricao()) || contaPagar.getSituacao().equals(SituacaoFinanceira.A_PAGAR.getValor())) {
                contaPagar.setSituacao(SituacaoFinanceira.getDescricao(contaPagar.getSituacao()));
                extratoContaPagarRelVO.setTotalPagar(extratoContaPagarRelVO.getTotalPagar() + contaPagar.getValor());
            }
            if (contaPagar.getSituacao().equals(SituacaoFinanceira.PAGO.getDescricao()) || contaPagar.getSituacao().equals(SituacaoFinanceira.PAGO.getValor())) {
                contaPagar.setSituacao(SituacaoFinanceira.getDescricao(contaPagar.getSituacao()));
                extratoContaPagarRelVO.setTotalPago(extratoContaPagarRelVO.getTotalPago() + contaPagar.getValor());
            }
        }
    }

    public void montarDadosBancoRecebimento(ExtratoContaPagarRelVO extratoContaPagarRelVO) {
        if (!extratoContaPagarRelVO.getListaContaPagarVOs().isEmpty()) {
            if (!extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFornecedor().getCodigo().equals(0)) {
                extratoContaPagarRelVO.setNomeBanco(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFornecedor().getNomeBanco());
                extratoContaPagarRelVO.setNumeroBancoRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFornecedor().getNumeroBancoRecebimento());
                extratoContaPagarRelVO.setNumeroAgenciaRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFornecedor().getNumeroAgenciaRecebimento());
                extratoContaPagarRelVO.setContaCorrenteRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFornecedor().getContaCorrenteRecebimento());
                extratoContaPagarRelVO.setTipoFavorecidoFornecedorFuncionario(true);
            } else if (!extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFuncionario().getCodigo().equals(0)) {
                extratoContaPagarRelVO.setNomeBanco(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFuncionario().getNomeBanco());
                extratoContaPagarRelVO.setNumeroBancoRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFuncionario().getNumeroBancoRecebimento());
                extratoContaPagarRelVO.setNumeroAgenciaRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFuncionario().getNumeroAgenciaRecebimento());
                extratoContaPagarRelVO.setContaCorrenteRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFuncionario().getContaCorrenteRecebimento());
                extratoContaPagarRelVO.setTipoFavorecidoFornecedorFuncionario(true);
            } else {
                extratoContaPagarRelVO.setTipoFavorecidoFornecedorFuncionario(false);
            }
        }
    }

    public SqlRowSet executarConsultaParametrizada(Integer codigoFavorecido, String filtroTipoFavorecido, String filtroTipoData, Date dataInicio, Date dataFim, Integer unidadeEnsino) throws Exception {
        StringBuilder sqlStr = new StringBuilder("select fornecedor.codigo as \"fornecedorCodigo\", fornecedor.nome as \"fornecedorNome\", ");
        sqlStr.append("fornecedor.nomeBanco as \"fornecedor.nomeBanco\", fornecedor.numeroBancoRecebimento as \"fornecedor.numeroBancoRecebimento\", ");
        sqlStr.append("fornecedor.numeroAgenciaRecebimento as \"fornecedor.numeroAgenciaRecebimento\", fornecedor.contaCorrenteRecebimento as \"fornecedor.contaCorrenteRecebimento\", ");
        sqlStr.append("pessoafuncionario.codigo as \"pessoafuncionarioCodigo\", pessoafuncionario.nome as \"pessoafuncionarioNome\", ");
        sqlStr.append("funcionario.codigo as \"funcionario.codigo\", ");
        sqlStr.append("funcionario.nomeBanco as \"funcionario.nomeBanco\", funcionario.numeroBancoRecebimento as \"funcionario.numeroBancoRecebimento\", ");
        sqlStr.append("funcionario.numeroAgenciaRecebimento as \"funcionario.numeroAgenciaRecebimento\", funcionario.contaCorrenteRecebimento as \"funcionario.contaCorrenteRecebimento\", ");
        sqlStr.append("banco.codigo as \"bancoCodigo\", banco.nome as \"bancoNome\", ");
        sqlStr.append("operadoracartao.codigo as \"operadoracartaoCodigo\", operadoracartao.nome as \"operadoracartaoNome\", ");
        sqlStr.append("pessoa.codigo as \"pessoaCodigo\", pessoa.nome as \"pessoaNome\", ");
        sqlStr.append("unidadeEnsino.codigo as \"unidadeEnsinoCodigo\", unidadeEnsino.nome as \"unidadeEnsinoNome\", ");
        sqlStr.append("responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\", parceiro.nome as \"parceiro.nome\", responsavelFinanceiro.codigo as \"responsavelFinanceiro.codigo\", parceiro.codigo as \"parceiro.codigo\", ");
        sqlStr.append("contaPagar.codigo as \"contaPagarCodigo\", contapagar.dataVencimento, contaPagar.dataFatoGerador, ");
        sqlStr.append("contaPagar.situacao as \"contaPagarSituacao\", contaPagar.valor as \"contaPagarValor\", contaPagar.descricao as \"contaPagarDescricao\", ");
        sqlStr.append("contaPagar.parcela as \"contaPagarParcela\", contaPagar.numeroNotaFiscalEntrada as \"contaPagarNumeroNotaFiscalEntrada\", contaPagar.tipoOrigem as \"contaPagarTipoOrigem\" ");
        sqlStr.append("from contapagar ");
        sqlStr.append("left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
        sqlStr.append("left join funcionario on funcionario.codigo = contapagar.funcionario ");
        sqlStr.append("left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
        sqlStr.append("left join pessoa on pessoa.codigo = contapagar.pessoa ");
        sqlStr.append("left join banco on banco.codigo = contapagar.banco ");
        sqlStr.append("left join unidadeEnsino on unidadeEnsino.codigo = contapagar.unidadeEnsino ");
        sqlStr.append("left join parceiro on parceiro.codigo = contapagar.parceiro ");
        sqlStr.append("left join operadoracartao on operadoracartao.codigo = contapagar.operadoracartao ");
        sqlStr.append("left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");
        sqlStr.append("where ");
        if (filtroTipoFavorecido.equals("AL")) {
            sqlStr.append("pessoa.codigo = ").append(codigoFavorecido).append(" ");
        } else if (filtroTipoFavorecido.equals("BA")) {
            sqlStr.append("banco.codigo = ").append(codigoFavorecido).append(" ");
        } else if (filtroTipoFavorecido.equals("FO")) {
            sqlStr.append("fornecedor.codigo = ").append(codigoFavorecido).append(" ");
        } else if (filtroTipoFavorecido.equals("PA")) {
            sqlStr.append("parceiro.codigo = ").append(codigoFavorecido).append(" ");
        } else if (filtroTipoFavorecido.equals("RF")) {
            sqlStr.append("responsavelFinanceiro.codigo = ").append(codigoFavorecido).append(" ");
        } else if (filtroTipoFavorecido.equals("OC")) {
        	sqlStr.append("operadoracartao.codigo = ").append(codigoFavorecido).append(" ");
        } else {
            sqlStr.append("pessoafuncionario.codigo = ").append(codigoFavorecido).append(" ");
        }
        if (unidadeEnsino != 0) {
            sqlStr.append("AND contapagar.unidadeensino = ").append(unidadeEnsino).append(" ");
        }
        if (filtroTipoData.equals("vencimento")) {
            sqlStr.append("and contapagar.datavencimento between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
            sqlStr.append("order by contapagar.datavencimento");
        } else {
            sqlStr.append("and contapagar.datafatogerador between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
            sqlStr.append("order by contapagar.datafatogerador");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return tabelaResultado;
    }

    public String getDesignIReportRelatorioPdf() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
    }

    public String getDesignIReportRelatorioExcel() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeExcel() + ".jrxml");
    }

    public String caminhoBaseIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }

    public static String getIdEntidade() {
        return ("ExtratoContaPagarRel");
    }

    public static String getIdEntidadeExcel() {
        return ("ExtratoContaPagarExcelRel");
    }
}
