package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ItemPrestacaoContaCategoriaDespesaVO;
import negocio.comuns.financeiro.ItemPrestacaoContaPagarVO;
import negocio.comuns.financeiro.PrestacaoContaVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoPrestacaoContaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.ItemPrestacaoContaPagarInterfaceFacade;

@Service
@Lazy
public class ItemPrestacaoContaPagar extends ControleAcesso implements ItemPrestacaoContaPagarInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = 8429547121392945521L;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ItemPrestacaoContaPagarVO obj) throws Exception {
        final String sql = "INSERT INTO ItemPrestacaoContaPagar( contaPagar, itemPrestacaoContaCategoriaDespesa) VALUES ( ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
                final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getContaPagar().getCodigo());
                sqlInserir.setInt(2, obj.getItemPrestacaoContaCategoriaDespesa().getCodigo());
                return sqlInserir;
            }
        }, new ResultSetExtractor() {

            public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
                if (arg0.next()) {
                    obj.setNovoObj(Boolean.FALSE);
                    return arg0.getInt("codigo");
                }
                return null;
            }
        }));

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ItemPrestacaoContaPagarVO obj) throws Exception {
        try {
            final String sql = "UPDATE ItemPrestacaoContaPagar set contaPagar=?, itemPrestacaoContaCategoriaDespesa=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setInt(1, obj.getContaPagar().getCodigo());
                    sqlAlterar.setInt(2, obj.getItemPrestacaoContaCategoriaDespesa().getCodigo());
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void incluirItemPrestacaoContaPagarVOs(List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO) throws Exception {
        for (ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO : itemPrestacaoContaPagarVOs) {
            itemPrestacaoContaPagarVO.setItemPrestacaoContaCategoriaDespesa(itemPrestacaoContaCategoriaDespesaVO);
            incluir(itemPrestacaoContaPagarVO);
        }

    }

    @Override
    public void alterarItemPrestacaoContaPagarVOs(List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO) throws Exception {
        excluirItemPrestacaoContaPagarVOs(itemPrestacaoContaPagarVOs, itemPrestacaoContaCategoriaDespesaVO);
        for (ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO : itemPrestacaoContaPagarVOs) {
            itemPrestacaoContaPagarVO.setItemPrestacaoContaCategoriaDespesa(itemPrestacaoContaCategoriaDespesaVO);
            if (itemPrestacaoContaPagarVO.getCodigo() == null || itemPrestacaoContaPagarVO.getCodigo() == 0) {
                incluir(itemPrestacaoContaPagarVO);
            } else {
                alterar(itemPrestacaoContaPagarVO);
            }
        }
    }

    @Override
    public void excluirItemPrestacaoContaPagarVOs(List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs, ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO) throws Exception {
        StringBuilder sql = new StringBuilder("");
        try {
            sql.append("DELETE FROM ItemPrestacaoContaPagar where itemPrestacaoContaCategoriaDespesa = ").append(itemPrestacaoContaCategoriaDespesaVO.getCodigo());
            int x = 0;
            for (ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO : itemPrestacaoContaPagarVOs) {
                if (x == 0) {
                    sql.append(" and codigo not in (").append(itemPrestacaoContaPagarVO.getCodigo());
                    x++;
                } else {
                    sql.append(", ").append(itemPrestacaoContaPagarVO.getCodigo());
                }
            }
            if (x > 0) {
                sql.append(") ");
            }
            getConexao().getJdbcTemplate().execute(sql.toString());
        } catch (Exception e) {
            throw e;
        } finally {

            sql = null;
        }

    }

    public StringBuilder getSelectSqlDadosCompletos() {
        StringBuilder sql = new StringBuilder("SELECT distinct ItemPrestacaoContaPagar.*,   ");
        sql.append(" contapagar.codigo as \"contapagar.codigo\", contapagar.dataVencimento, contaPagar.dataFatoGerador, contaPagar.valor, contaPagar.tipoSacado, ");
        sql.append(" contaPagar.tipoOrigem, contaPagar.valorPago,");
        sql.append(" fornecedor.nome as \"fornecedor.nome\", pessoafuncionario.nome as \"pessoafuncionario.nome\", ");
        sql.append(" banco.nome as \"banco.nome\", pessoa.nome as \"pessoa.nome\",  ");
        sql.append(" responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\" ");
        sql.append(" from ContaPagar");
        sql.append(" inner join centroresultadoorigem on centroresultadoorigem.codorigem  = contapagar.codigo::VARCHAR and centroresultadoorigem.tipocentroresultadoorigem = 'CONTA_PAGAR' ");
        sql.append(" left join ItemPrestacaoContaPagar on ContaPagar.codigo = ItemPrestacaoContaPagar.contaPagar ");
        sql.append(" left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
        sql.append(" left join funcionario on funcionario.codigo = contapagar.funcionario ");
        sql.append(" left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
        sql.append(" left join pessoa on pessoa.codigo = contapagar.pessoa ");
        sql.append(" left join banco on banco.codigo = contapagar.banco ");
        sql.append(" LEFT JOIN Pessoa as responsavelFinanceiro ON contapagar.responsavelFinanceiro = responsavelFinanceiro.codigo ");
        
        return sql;
    }

    private ItemPrestacaoContaPagarVO montarDadosCompleta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) {
        ItemPrestacaoContaPagarVO obj = new ItemPrestacaoContaPagarVO();
        obj.setNovoObj(false);
        obj.setCodigo(rs.getInt("codigo"));
        obj.getItemPrestacaoContaCategoriaDespesa().setCodigo(rs.getInt("itemPrestacaoContaCategoriaDespesa"));
        obj.getContaPagar().setCodigo(rs.getInt("contapagar.codigo"));
        obj.getContaPagar().setDataVencimento(rs.getDate("dataVencimento"));
        obj.getContaPagar().setDataFatoGerador(rs.getDate("dataFatoGerador"));
        obj.getContaPagar().setValor(new Double(rs.getDouble("valor")));
        obj.getContaPagar().setTipoSacado(rs.getString("tipoSacado"));
        obj.getContaPagar().setTipoOrigem(rs.getString("tipoOrigem"));
        obj.getContaPagar().setValorPago(new Double(rs.getDouble("valorPago")));
        obj.getContaPagar().getFornecedor().setNome(rs.getString("fornecedor.nome"));
        obj.getContaPagar().getFuncionario().getPessoa().setNome(rs.getString("pessoafuncionario.nome"));
        obj.getContaPagar().getResponsavelFinanceiro().setNome(rs.getString("responsavelFinanceiro.nome"));
        obj.getContaPagar().getPessoa().setNome(rs.getString("pessoa.nome"));
        obj.getContaPagar().getBanco().setNome(rs.getString("banco.nome"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
        	obj.getContaPagar().setListaCentroResultadoOrigemVOs(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(obj.getContaPagar().getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			return obj;
		}
        return obj;
    }

    @Override
    public List<ItemPrestacaoContaPagarVO> consultarItemPrestacaoContaPagarPorItemPrestacaoContaCategoriaDespesa(ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesa, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sql = new StringBuilder(getSelectSqlDadosCompletos() + " WHERE  itemPrestacaoContaCategoriaDespesa = ").append(itemPrestacaoContaCategoriaDespesa.getCodigo());
        int x = 0;
        for (ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO : itemPrestacaoContaCategoriaDespesa.getItemPrestacaoContaPagarVOs()) {
            if (x == 0) {
                sql.append(" and itemPrestacaoContaPagar.codigo not in (").append(itemPrestacaoContaPagarVO.getCodigo());
                x++;
            } else {
                sql.append(", ").append(itemPrestacaoContaPagarVO.getContaPagar().getCodigo());
            }
        }

        if (x > 0) {
            sql.append(") ");
        }
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaPorItemPrestacaoContaCategoriaDespesas(itemPrestacaoContaCategoriaDespesa, usuarioVO, rs);
    }

	private List<ItemPrestacaoContaPagarVO> montarDadosConsultaPorItemPrestacaoContaCategoriaDespesas(ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesa, UsuarioVO usuarioVO, SqlRowSet rs) {
		List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs = new ArrayList<>(0);
        while (rs.next()) {
        	ItemPrestacaoContaPagarVO novo = montarDadosCompleta(rs, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
        	novo.setItemPrestacaoContaCategoriaDespesa(itemPrestacaoContaCategoriaDespesa);
            itemPrestacaoContaPagarVOs.add(novo);
        }
        return itemPrestacaoContaPagarVOs;
	}

    private List<ItemPrestacaoContaPagarVO> montarDadosConsultaCompleta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) {
        List<ItemPrestacaoContaPagarVO> itemPrestacaoContaPagarVOs = new ArrayList<ItemPrestacaoContaPagarVO>(0);
        while (rs.next()) {
            itemPrestacaoContaPagarVOs.add(montarDadosCompleta(rs, nivelMontarDados, usuarioVO));
        }
        return itemPrestacaoContaPagarVOs;
    }

    @Override
    public List<ItemPrestacaoContaPagarVO> consultarContaPagarDisponivelPrestacaoConta(String favorecido, String tipoFiltroData, Date dataInicio, Date dataTermino, PrestacaoContaVO prestacaoContaVO) {
        StringBuilder sql = new StringBuilder(getSelectSqlDadosCompletos());
        if (tipoFiltroData != null && tipoFiltroData.equals("DATA_PAGAMENTO")) {
            sql.append(" inner join ContaPagarNegociacaoPagamento on ContaPagarNegociacaoPagamento.contaPagar = contaPagar.codigo");
            sql.append(" inner join negociacaoPagamento on negociacaoPagamento.codigo = ContaPagarNegociacaoPagamento.negociacaoContaPagar");
        }
        sql.append(" WHERE (sem_acentos(UPPER(pessoafuncionario.nome)) LIKE sem_acentos(UPPER(?)) OR ");
        sql.append(" sem_acentos(UPPER(banco.nome)) LIKE sem_acentos(UPPER(?)) OR ");
        sql.append(" sem_acentos(UPPER(fornecedor.nome)) LIKE sem_acentos(UPPER(?)) OR ");
        sql.append(" sem_acentos(UPPER(responsavelFinanceiro.nome)) LIKE sem_acentos(UPPER(?)) OR ");
        sql.append(" sem_acentos(UPPER(pessoa.nome)) LIKE sem_acentos(UPPER(?)))  ");
        if (prestacaoContaVO.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)
                && prestacaoContaVO.getTurma().getCodigo() != null
                && prestacaoContaVO.getTurma().getCodigo() > 0) {
            sql.append(" and centroresultadoorigem.turma = ").append(prestacaoContaVO.getTurma().getCodigo());
        }else if (prestacaoContaVO.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.TURMA)) {
            sql.append(" and centroresultadoorigem.turma is not null ");
        }
        
        if (prestacaoContaVO.getTipoPrestacaoConta().equals(TipoPrestacaoContaEnum.UNIDADE_ENSINO)
                && prestacaoContaVO.getUnidadeEnsino().getCodigo() != null
                && prestacaoContaVO.getUnidadeEnsino().getCodigo() > 0) {
            sql.append(" and contaPagar.unidadeEnsino = ").append(prestacaoContaVO.getUnidadeEnsino().getCodigo());
        }
        if (tipoFiltroData != null && tipoFiltroData.equals("DATA_VENCIMENTO")) {
            sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "contaPagar.dataVencimento", false));
        }
        if (tipoFiltroData != null && tipoFiltroData.equals("DATA_FATO_GERADOR")) {
            sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "contaPagar.dataFatoGerador", false));
        }
        if (tipoFiltroData != null && tipoFiltroData.equals("DATA_PAGAMENTO")) {
            sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "negociacaoPagamento.data", false));
        }
        sql.append(" and contaPagar.situacao = 'PA' ");
        sql.append(" and contaPagar.codigo not in ( select ItemPrestacaoContaPagar.contaPagar from ItemPrestacaoContaPagar ) ");
        int x = 0;
        for (ItemPrestacaoContaCategoriaDespesaVO itemPrestacaoContaCategoriaDespesaVO : prestacaoContaVO.getItemPrestacaoContaCategoriaDespesaVOs()) {
            for (ItemPrestacaoContaPagarVO itemPrestacaoContaPagarVO : itemPrestacaoContaCategoriaDespesaVO.getItemPrestacaoContaPagarVOs()) {
                if (x == 0) {
                    sql.append(" and contaPagar.codigo not in (").append(itemPrestacaoContaPagarVO.getContaPagar().getCodigo());
                    x++;
                } else {
                    sql.append(", ").append(itemPrestacaoContaPagarVO.getContaPagar().getCodigo());
                }
            }
        }
        if (x > 0) {
            sql.append(") ");
        }

        return montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), favorecido + "%", favorecido + "%", favorecido + "%", favorecido + "%", favorecido + "%"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
    }

}
