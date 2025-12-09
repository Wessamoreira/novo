package relatorio.negocio.jdbc.crm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.crm.BuscaProspectVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.crm.InteracaoFollowUpRelVO;
import relatorio.negocio.interfaces.crm.InteracaoFollowUpRelInterfaceFacade;
import relatorio.negocio.interfaces.crm.InteracaoFollowUpRelInterfaceFacade;

import relatorio.negocio.interfaces.crm.InteracaoFollowUpRelInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class InteracaoFollowUpRel extends ControleAcesso implements InteracaoFollowUpRelInterfaceFacade {

    public List<InteracaoFollowUpRelVO> criarObjeto(List<BuscaProspectVO> listaProspects) {
        List<InteracaoFollowUpRelVO> listaInteracaoFollowUpRelVO = new ArrayList<InteracaoFollowUpRelVO>(0);
        listaInteracaoFollowUpRelVO.addAll(executarConsultaParametrizada(listaProspects));
        Ordenacao.ordenarLista(listaInteracaoFollowUpRelVO, "nomeProspect");
        return listaInteracaoFollowUpRelVO;
    }

    private List<InteracaoFollowUpRelVO> executarConsultaParametrizada(List<BuscaProspectVO> buscaProspectVOs) {
        StringBuilder sql = new StringBuilder();
        List<InteracaoFollowUpRelVO> listaInteracaoFollowUpRelVO = new ArrayList<InteracaoFollowUpRelVO>(0);
        sql.append("SELECT DISTINCT interacaoworkflow.datainicio AS dataContato, interacaoworkflow.horaInicio AS horaInicio, pessoaResponsavel.nome AS nomeResponsavel,interacaoworkflow.observacao AS observacao, prospects.emailprincipal AS email, prospects.codigo AS codigoProspect, ");
        sql.append("prospects.nome AS nomeProspect, interacaoworkflow.tipoInteracao AS tipoInteracao, prospects.emailPrincipal AS email, prospects.telefoneresidencial AS telefoneresidencial, prospects.celular AS celular, curso.nome AS nomeCurso, curso.codigo AS codigoCurso, consultorPadrao.nome AS nomeConsultorPadrao ");
        sql.append("FROM prospects ");
        sql.append("INNER JOIN interacaoworkflow ON prospects.codigo = interacaoworkflow.prospect ");
        sql.append("LEFT JOIN usuario ON interacaoworkflow.responsavel = usuario.codigo ");
        sql.append("LEFT JOIN funcionario  ON prospects.consultorPadrao = funcionario.codigo ");
        sql.append("LEFT JOIN pessoa AS consultorPadrao  ON consultorPadrao.codigo = funcionario.pessoa ");
        sql.append("LEFT JOIN pessoa AS pessoaResponsavel ON usuario.pessoa = pessoaResponsavel.codigo ");
        sql.append("LEFT JOIN curso ON interacaoworkflow.curso = curso.codigo ");

        int i = 1;
        for (BuscaProspectVO buscaProspectVO : buscaProspectVOs) {
            if (i == 1) {
                sql.append("WHERE prospects.codigo in ( ");
            }
            if (i != buscaProspectVOs.size()) {
                sql.append(buscaProspectVO.getProspectNovaInteracao().getCodigo()).append(",");
            } else {
                sql.append(buscaProspectVO.getProspectNovaInteracao().getCodigo()).append(")");
            }
            i++;
        }
        sql.append(" ORDER BY prospects.nome,interacaoworkflow.datainicio desc ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        while (tabelaResultado.next()) {
            listaInteracaoFollowUpRelVO.add(montarDados(tabelaResultado));
        }
        return listaInteracaoFollowUpRelVO;
    }

    private InteracaoFollowUpRelVO montarDados(SqlRowSet dadosSQL) {
        InteracaoFollowUpRelVO obj = new InteracaoFollowUpRelVO();
        obj.setDataContato(Uteis.obterDataFormatoTextoddMMyyyy(dadosSQL.getTimestamp("dataContato")) + " - " + dadosSQL.getString("horaInicio"));
        if (dadosSQL.getString("nomeResponsavel") == null || dadosSQL.getString("nomeResponsavel").equals("")) {
            obj.setNomeResponsavel(dadosSQL.getString("nomeConsultorPadrao"));
        } else {
            obj.setNomeResponsavel(dadosSQL.getString("nomeResponsavel"));
        }
        obj.setNomeProspect(dadosSQL.getString("nomeProspect"));
        obj.setCodigoProspect(dadosSQL.getInt("codigoProspect"));
        obj.setTelefoneResidencial(dadosSQL.getString("telefoneResidencial"));
        obj.setCelular(dadosSQL.getString("celular"));
        obj.setObservacao(dadosSQL.getString("observacao"));
        obj.setEmailProspect(dadosSQL.getString("email"));
        obj.setCurso(dadosSQL.getString("nomeCurso"));
        if (dadosSQL.getString("tipoInteracao") != null) {
            obj.setTipo(dadosSQL.getString("tipoInteracao").substring(0, 1) + dadosSQL.getString("tipoInteracao").substring(1).toLowerCase());
        }
        return obj;
    }

    public String designRelatorio() {
        return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator);
    }

    public static String getIdEntidade() {
        return "InteracaoFollowUpRel";
    }
}
