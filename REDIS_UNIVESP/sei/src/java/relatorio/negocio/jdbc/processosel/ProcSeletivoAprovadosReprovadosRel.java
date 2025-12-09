package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.processosel.ProcSeletivoAprovadoReprovadoRelVO;
import relatorio.negocio.interfaces.processosel.ProcSeletivoAprovadosReprovadosInterfaceFacade;

import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ProcSeletivoAprovadosReprovadosRel extends SuperRelatorio implements ProcSeletivoAprovadosReprovadosInterfaceFacade {

    protected static String idEntidade;
    private static String sqlFixo = "select pessoa.nome, pessoa.celular, pessoa.telefoneres, pessoa.email, "
            + "resultadoProcessoSeletivo.resultadoprimeiraopcao, "
            + "curso1.nome as nomecursoopcao1, "
            + "resultadoProcessoSeletivo.resultadosegundaopcao, "
            + "curso2.nome as nomecursoopcao2, "
            + "resultadoProcessoSeletivo.resultadoterceiraopcao, "
            + "curso3.nome as nomecursoopcao3 , "
            + "itemProcessoSeletivoDataProva.dataProva, "
            + "itemProcessoSeletivoDataProva.hora, "
            + "unidadeensino.nome as unidadeensinonome "
            + "from resultadoProcessoSeletivo "
            + "inner join inscricao on inscricao.codigo = resultadoProcessoSeletivo.inscricao "
            + "inner join unidadeensino on inscricao.unidadeensino = unidadeensino.codigo "
            + "inner join pessoa on inscricao.candidato = pessoa.codigo "
            + "left join unidadeensinocurso as unidadeensinocurso1 on unidadeensinocurso1.codigo = inscricao.cursoopcao1 "
            + "left join curso as curso1 on curso1.codigo = unidadeensinocurso1.curso "
            + "left join unidadeensinocurso as unidadeensinocurso2 on unidadeensinocurso2.codigo = inscricao.cursoopcao2 "
            + "left join curso as curso2 on curso2.codigo = unidadeensinocurso2.curso "
            + "left join unidadeensinocurso as unidadeensinocurso3 on unidadeensinocurso3.codigo = inscricao.cursoopcao3 "
            + "left join curso as curso3 on curso3.codigo = unidadeensinocurso3.curso"
            + "left join itemProcSeletivoDataProva as itemProcessoSeletivoDataProva on itemProcessoSeletivoDataProva.codigo = inscricao.itemProcessoSeletivoDataProva ";

    public ProcSeletivoAprovadosReprovadosRel() {
        setIdEntidade("ProcessoSeletivoAprovadoReprovadoRel");
    }

    public List<ProcSeletivoAprovadoReprovadoRelVO> executarGeracaoListaRelatorio(String situacao, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, Date dataProvaInicio, Date dataProvaFim,  UsuarioVO usuarioVO) throws Exception {
        validarDados(situacao, unidadeEnsinoVO, unidadeEnsinoCursoVO);
        ProcSeletivoAprovadosReprovadosRel.emitirRelatorio(getIdEntidade(), false, null); // valida permissao, e obtem conexao

        List<ProcSeletivoAprovadoReprovadoRelVO> lista = executarBuscarMontarListaGeracaoRelatorio(situacao, unidadeEnsinoVO, unidadeEnsinoCursoVO, dataProvaInicio, dataProvaFim);

        for (ProcSeletivoAprovadoReprovadoRelVO procSeletivoAprovadoReprovadoRel : lista) {
            if (!unidadeEnsinoCursoVO.getCodigo().equals(0)) {
                UnidadeEnsinoCursoVO unidadeEnsinoCurso = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(unidadeEnsinoCursoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuarioVO);
                procSeletivoAprovadoReprovadoRel.setTurno(unidadeEnsinoCurso.getTurno().getNome());
                procSeletivoAprovadoReprovadoRel.setNomeCursoFiltro(unidadeEnsinoCurso.getCurso().getNome());
                unidadeEnsinoCurso = null;
            }
        }

        return lista;
    }

    private void validarDados(String situacao, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) throws ConsistirException {
        if (situacao.isEmpty()) {
            throw new ConsistirException("O campo Situação deve ser informado.");
        }
        if (unidadeEnsinoVO.getCodigo() == null || unidadeEnsinoVO.getCodigo().equals(0)) {
            throw new ConsistirException("O campo Unidade Ensino deve ser informado.");
        }
    }

    public static String getIdEntidade() {
        return ProcSeletivoInscricoesRel.idEntidade;
    }

    /*
     * (non-Javadoc)
     * 
     * @see relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRelInterfaceFacade#setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        ProcSeletivoInscricoesRel.idEntidade = idEntidade;
    }

    private List<ProcSeletivoAprovadoReprovadoRelVO> executarBuscarMontarListaGeracaoRelatorio(String situacao, UnidadeEnsinoVO unidadeEnsinoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, Date dataInicio, Date dataFim) throws Exception {
        StringBuilder sql = new StringBuilder(sqlFixo);
        if (situacao.equals("ap")) {
            sql.append(" where (resultadoProcessoSeletivo.resultadoprimeiraopcao = 'AP' or resultadoProcessoSeletivo.resultadosegundaopcao = 'AP' or resultadoProcessoSeletivo.resultadoterceiraopcao = 'AP') ");
        } else if (situacao.equals("rp")) {
            sql.append(" where (resultadoProcessoSeletivo.resultadoprimeiraopcao = 'RE' and resultadoProcessoSeletivo.resultadosegundaopcao = 'RE' and resultadoProcessoSeletivo.resultadoterceiraopcao = 'RE') ");
        } else if (situacao.equals("td")) {
            sql.append(" where ((resultadoProcessoSeletivo.resultadoprimeiraopcao = 'RE' or resultadoProcessoSeletivo.resultadoprimeiraopcao = 'AP') and (resultadoProcessoSeletivo.resultadosegundaopcao = 'RE' or resultadoProcessoSeletivo.resultadosegundaopcao = 'AP' ) and (resultadoProcessoSeletivo.resultadoterceiraopcao = 'RE' or resultadoProcessoSeletivo.resultadoterceiraopcao = 'AP')) ");
        }
        if (unidadeEnsinoVO.getCodigo() != null && !unidadeEnsinoVO.getCodigo().equals(0)) {
            StringBuilder sb = new StringBuilder(" and ( ");
            sb.append(" unidadeensinocurso1.unidadeensino =").append(unidadeEnsinoVO.getCodigo());
            sb.append(" or unidadeensinocurso2.unidadeensino=").append(unidadeEnsinoVO.getCodigo());
            sb.append(" or unidadeensinocurso3.unidadeensino=").append(unidadeEnsinoVO.getCodigo());
            sb.append(") ");
            sql.append(sb.toString());
        }
        if (unidadeEnsinoCursoVO.getCodigo() != null && !unidadeEnsinoCursoVO.getCodigo().equals(0)) {
            StringBuilder sb = new StringBuilder(" and ( ");
            sb.append(" unidadeensinocurso1.codigo =").append(unidadeEnsinoCursoVO.getCodigo());
            sb.append(" or unidadeensinocurso2.codigo=").append(unidadeEnsinoCursoVO.getCodigo());
            sb.append(" or unidadeensinocurso3.codigo=").append(unidadeEnsinoCursoVO.getCodigo());
            sb.append(") ");
            sql.append(sb.toString());
        }
        if (dataInicio != null && dataFim != null) {
            StringBuilder sb = new StringBuilder(" and  ( ");
            sb.append("itemProcessoSeletivoDataProva.dataProva between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' )");
            sql.append(sb.toString());
        } else if (dataInicio != null) {
            StringBuilder sb = new StringBuilder(" and ( ");
            sb.append("itemProcessoSeletivoDataProva.dataProva >= '").append(Uteis.getDataJDBC(dataInicio)).append("' )");
            sql.append(sb.toString());
        } else if (dataFim != null) {
            StringBuilder sb = new StringBuilder(" and ( ");
            sb.append("itemProcessoSeletivoDataProva.dataProva <= '").append(Uteis.getDataJDBC(dataFim)).append("' )");
            sql.append(sb.toString());
        }
//"WHERE (dataPrevisao between '" + Uteis.getDataJDBC(prmIni) + "' and '" + Uteis.getDataJDBC(prmFim) + "')";
        sql.append(" order by itemProcessoSeletivoDataProva.dataProva, itemProcessoSeletivoDataProva.hora");

        return executarBuscarMontarListaGeracaoRelatorio(sql);
    }

    private List<ProcSeletivoAprovadoReprovadoRelVO> executarBuscarMontarListaGeracaoRelatorio(StringBuilder sql) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, null);

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (tabelaResultado.wasNull()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDadosLista(tabelaResultado));
    }

    private List<ProcSeletivoAprovadoReprovadoRelVO> montarDadosLista(SqlRowSet tabelaResultado) {
        List<ProcSeletivoAprovadoReprovadoRelVO> vetResultado = new ArrayList<ProcSeletivoAprovadoReprovadoRelVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    public ProcSeletivoAprovadoReprovadoRelVO montarDados(SqlRowSet tabelaResultado) {
        ProcSeletivoAprovadoReprovadoRelVO procSeletivoAprovadoReprovadoRel = new ProcSeletivoAprovadoReprovadoRelVO();
        procSeletivoAprovadoReprovadoRel.getPessoa().setNome(tabelaResultado.getString("nome"));
        procSeletivoAprovadoReprovadoRel.getPessoa().setCelular(tabelaResultado.getString("celular"));
        procSeletivoAprovadoReprovadoRel.getPessoa().setTelefoneRes(tabelaResultado.getString("telefoneres"));
        procSeletivoAprovadoReprovadoRel.getPessoa().setEmail(tabelaResultado.getString("email"));
        procSeletivoAprovadoReprovadoRel.getResultadoProcessoSeletivo().setResultadoPrimeiraOpcao(tabelaResultado.getString("resultadoprimeiraopcao"));
        procSeletivoAprovadoReprovadoRel.setNomeCurso1(tabelaResultado.getString("nomecursoopcao1"));

        procSeletivoAprovadoReprovadoRel.getResultadoProcessoSeletivo().setResultadoSegundaOpcao(tabelaResultado.getString("resultadosegundaopcao"));
        procSeletivoAprovadoReprovadoRel.setNomeCurso2(tabelaResultado.getString("nomecursoopcao2"));

        procSeletivoAprovadoReprovadoRel.getResultadoProcessoSeletivo().setResultadoTerceiraOpcao(tabelaResultado.getString("resultadoterceiraopcao"));
        procSeletivoAprovadoReprovadoRel.setNomeCurso3(tabelaResultado.getString("nomecursoopcao3"));

        procSeletivoAprovadoReprovadoRel.getInscricao().getItemProcessoSeletivoDataProva().setDataProva(tabelaResultado.getDate("dataprova"));
        procSeletivoAprovadoReprovadoRel.getInscricao().getItemProcessoSeletivoDataProva().setHora(tabelaResultado.getString("hora"));

        procSeletivoAprovadoReprovadoRel.getInscricao().getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeensinonome"));

        return procSeletivoAprovadoReprovadoRel;
    }

    public String getDesignIReportRelatorio() throws Exception {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ProcessoSeletivoAprovadosReprovadosRel.jrxml");
    }

    public String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
    }
}
