package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.ChequeRelVO;
import relatorio.negocio.interfaces.financeiro.ChequesRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ChequesRel extends SuperRelatorio implements ChequesRelInterfaceFacade {

   

    public ChequesRel() {
        
    }

    @Override
    public List<ChequeRelVO> criarObjeto(UnidadeEnsinoVO unidadeEnsino, String tipoSituacao, String tipoFiltro, String ordenarPor, Date dataInicio, Date dataFim, Date dataInicioPrevisao, Date dataFimPrevisao, UsuarioVO usuarioVO) throws Exception {
        List<ChequeRelVO> chequeRelVOs = new ArrayList<ChequeRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizada(tipoSituacao, tipoFiltro, ordenarPor, dataInicio, dataFim, dataInicioPrevisao, dataFimPrevisao, unidadeEnsino);
        while (dadosSQL.next()) {
            chequeRelVOs.add(montarDados(dadosSQL));
        }
        return chequeRelVOs;
    }

    private ChequeRelVO montarDados(SqlRowSet dadosSQL) {
        ChequeRelVO chequeRelVO = new ChequeRelVO();
        chequeRelVO.setNumero(dadosSQL.getString("cheque_numero"));
        chequeRelVO.setAgencia(dadosSQL.getString("cheque_agencia"));
        chequeRelVO.setNumeroContaCorrente(dadosSQL.getString("cheque_numerocontacorrente"));
        chequeRelVO.setValor(dadosSQL.getDouble("cheque_valor"));
        chequeRelVO.setDataEmissao(Uteis.getDataJDBC(dadosSQL.getDate("cheque_dataemissao")));
        chequeRelVO.setDataPrevisao(Uteis.getData(dadosSQL.getDate("cheque_dataprevisao")));
        chequeRelVO.setSituacao(SituacaoCheque.getDescricao(dadosSQL.getString("cheque_situacao")));
        chequeRelVO.setMatriculaAluno(dadosSQL.getString("matriculaaluno"));
        chequeRelVO.setNomeAluno(dadosSQL.getString("nomealuno"));
        chequeRelVO.setSacado(dadosSQL.getString("sacado"));
        return chequeRelVO;
    }

    public SqlRowSet executarConsultaParametrizada(String tipoSituacao, String tipoFiltro, String ordenarPor, Date dataInicio, Date dataFim, Date dataInicioPrevisao, Date dataFimPrevisao, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append("SELECT ");
        selectStr.append(" m.matricula as matriculaaluno, case when p.codigo > 0 then p.nome else case when parceiro.codigo > 0 then parceiro.nome else fornecedor.nome end end  as nomealuno, cheque.sacado as sacado, ");
        selectStr.append(" cheque.codigo AS cheque_codigo,");
        selectStr.append(" cheque.numero AS cheque_numero,");
        selectStr.append(" cheque.agencia AS cheque_agencia,");
        selectStr.append(" cheque.numerocontacorrente AS cheque_numerocontacorrente,");
        selectStr.append(" cheque.valor AS cheque_valor,");
        selectStr.append(" cheque.dataemissao AS cheque_dataemissao,");
        selectStr.append(" cheque.dataprevisao AS cheque_dataprevisao,");
        selectStr.append(" case when ( (cheque.situacao = 'EC' or cheque.situacao = 'DE') and (select count(DevolucaoCheque.codigo) from DevolucaoCheque where DevolucaoCheque.cheque = cheque.codigo limit 1)>0) then 'DE' else cheque.situacao end AS cheque_situacao");
        selectStr.append(" FROM cheque ");
        selectStr.append(" LEFT JOIN pessoa p on cheque.pessoa = p.codigo ");
        selectStr.append(" LEFT JOIN parceiro on cheque.parceiro = parceiro.codigo ");
        selectStr.append(" LEFT JOIN fornecedor on cheque.fornecedor = fornecedor.codigo ");
        selectStr.append(" LEFT JOIN negociacaorecebimento on negociacaorecebimento.codigo = cheque.recebimento ");
        selectStr.append(" LEFT JOIN matricula m on m.matricula = negociacaorecebimento.matricula ");
        if (tipoSituacao.equals("Movimentado")) {
            selectStr.append(" INNER JOIN movimentacaofinanceiraitem ON movimentacaofinanceiraitem.cheque = cheque.codigo ");
        }
        selectStr.append(montarFiltrosRelatorio(tipoSituacao, tipoFiltro, dataInicio, dataFim, dataInicioPrevisao, dataFimPrevisao, unidadeEnsinoVO));
        selectStr.append(" GROUP BY ");
        selectStr.append(" cheque_dataemissao,");
        selectStr.append(" cheque_numero, cheque.codigo, ");
        selectStr.append(" cheque_agencia,");
        selectStr.append(" cheque_numerocontacorrente,");
        selectStr.append(" cheque_valor,");
        selectStr.append(" cheque_dataprevisao,");
        selectStr.append(" cheque_situacao, p.codigo , parceiro.codigo,   ");
        selectStr.append(" m.matricula, p.nome, parceiro.nome, fornecedor.nome,  cheque.sacado ");
        selectStr.append(" ORDER BY ");
        if (ordenarPor.equals("EM")) {
            selectStr.append(" cheque_dataemissao, cheque_numero;");
        } else {
            selectStr.append(" cheque_dataprevisao, cheque_numero;");
        }
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }

    private String montarFiltrosRelatorio(String tipoSituacao, String tipoFiltro, Date dataInicio, Date dataFim, Date dataInicioPrevisao, Date dataFimPrevisao, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
        String condicao = "";
        String where = " WHERE ";
        if ((tipoFiltro != null) && (tipoFiltro.equals("Emitidos"))) {
            condicao += (where + "( cheque.chequeproprio = true )");
            where = " AND ";
        }
        if ((tipoFiltro != null) && (tipoFiltro.equals("Recebidos"))) {
            condicao += (where + "( cheque.chequeproprio = false ) AND (cheque.pago = false)");
            where = " AND ";
        }
        if ((tipoSituacao != null) && (!tipoSituacao.equals("0")  && !tipoSituacao.equals("") )) {
            if(tipoSituacao.equals("DE")){
               condicao += (where + "( (cheque.situacao = 'EC' or cheque.situacao = 'DE') AND (select count(DevolucaoCheque.codigo) from DevolucaoCheque where DevolucaoCheque.cheque = cheque.codigo limit 1) > 0)");
            }else if(tipoSituacao.equals("EC")){
               condicao += (where + "( cheque.situacao = 'EC' and (select count(DevolucaoCheque.codigo) from DevolucaoCheque where DevolucaoCheque.cheque = cheque.codigo limit 1) = 0)");
            }else{
               condicao += (where + "( cheque.situacao = '" + tipoSituacao + "')");
            }
            where = " AND ";
        }
        if (dataInicio != null) {
            condicao += where + "( cheque.dataemissao >= '" + Uteis.getDataJDBC(dataInicio) + "' )";
            where = " AND ";
        }
        if (dataFim != null) {
            condicao += where + "( cheque.dataemissao <= '" + Uteis.getDataJDBC(dataFim) + "' )";
            where = " AND ";
        }
        if (dataInicioPrevisao != null) {
            condicao += where + "( cheque.dataprevisao >= '" + Uteis.getDataJDBC(dataInicioPrevisao) + "' )";
            where = " AND ";
        }
        if (dataFimPrevisao != null) {
            condicao += where + "( cheque.dataprevisao <= '" + Uteis.getDataJDBC(dataFimPrevisao) + "' )";
            where = " AND ";
        }
        if (unidadeEnsinoVO != null && unidadeEnsinoVO.getCodigo() != null && unidadeEnsinoVO.getCodigo().intValue() > 0) {
            condicao += where + "(cheque.unidadeEnsino = " + unidadeEnsinoVO.getCodigo().intValue() + " )";
            where = " AND ";
        }
        return condicao;
    }

    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }

    public static String getIdEntidade() {
        return "ChequesRel";
    }


}
