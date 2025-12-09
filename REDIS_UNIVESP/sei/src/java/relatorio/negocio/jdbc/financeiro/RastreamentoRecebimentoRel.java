package relatorio.negocio.jdbc.financeiro;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.RastreamentoRecebimentoRelVO;
import relatorio.negocio.interfaces.financeiro.RastreamentoRecebimentoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class RastreamentoRecebimentoRel extends SuperRelatorio implements RastreamentoRecebimentoRelInterfaceFacade {

    private RastreamentoRecebimentoRelVO rastreamentoRecebimentoRelVO;

    private List<RastreamentoRecebimentoRelVO> listaRastreamentoRecVO;

    private List<Integer> inteiros;

    private Integer numeroParcelas = 0;

    public RastreamentoRecebimentoRel() {

    }

    public static String getIdEntidade() {
        return ("RastreamentoRecebimentoRel");
    }

    @Override
    public List<RastreamentoRecebimentoRelVO> consultarRastreamentoRecebimento(Integer codUnidadeEnsino, Integer codCurso, Integer codTurno, String mes, String ano) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        String fim = Uteis.getMesReferencia(Integer.valueOf(mes), Integer.valueOf(ano));

        Integer qtdDiasMes = Uteis.obterNrDiasNoMes(Uteis.getDate("01/" + fim));

        Date dataLimiteInicial = montarData(1, Integer.valueOf(mes), Integer.valueOf(ano));
        Date dataLimiteFinal = montarData(qtdDiasMes.intValue(), Integer.valueOf(mes), Integer.valueOf(ano));

        int contLinha = 1;
        int contColuna = 1;
        while (contLinha < 7) {
            sql.append(" select (select SUM(cr1.valor) from contareceber  cr1 inner join matricula m1 on cr1.matriculaaluno = m1.matricula where cr1.situacao = 'RE' ");
            sql.append(" and m1.unidadeensino = ").append(codUnidadeEnsino);
            sql.append(" and cr1.datavencimento between '").append(Uteis.getDataJDBC(atualizarData(dataLimiteInicial, contLinha - 1))).append("' and '")
                .append(Uteis.getDataJDBC(atualizarDataUltimoDia(dataLimiteFinal, contLinha - 1))).append("'");
            if (codTurno != null && codTurno > 0) {
                sql.append(" and m1.turno = ").append(codTurno);
            }
            if (codCurso != null && codCurso > 0) {
                sql.append(" and m1.curso = ").append(codCurso);
            }
            sql.append(") as faturamento, ");

            sql.append(" (select COUNT(cr1.codigo) from contareceber  cr1 inner join matricula m1 on cr1.matriculaaluno = m1.matricula where cr1.situacao = 'RE' ");
            sql.append(" and m1.unidadeensino = ").append(codUnidadeEnsino);
            sql.append(" and cr1.datavencimento between '").append(Uteis.getDataJDBC(atualizarData(dataLimiteInicial, contLinha - 1))).append("' and '")
                .append(Uteis.getDataJDBC(atualizarDataUltimoDia(dataLimiteFinal, contLinha - 1))).append("'");
            if (codTurno != null && codTurno > 0) {
                sql.append(" and m1.turno = ").append(codTurno);
            }
            if (codCurso != null && codCurso > 0) {
                sql.append(" and m1.curso = ").append(codCurso);
            }
            sql.append(") as numeroParcelas, ");

            while (contColuna < 7) {
                sql.append(" (select sum(valorrecebimento) from (select case when (datarecebimento between '");
                sql.append(Uteis.getDataJDBC(atualizarData(dataLimiteInicial, contColuna - 1))).append("' and '")
                    .append(Uteis.getDataJDBC(atualizarDataUltimoDia(dataLimiteFinal, contColuna - 1)));
                sql.append("') then sum(valorrecebimento) end as valorrecebimento from contareceberrecebimento");
                sql.append(" where contareceber in (select cr2.codigo from contareceber  cr2 inner join matricula m2 on cr2.matriculaaluno = m2.matricula where cr2.situacao = 'RE'");
                sql.append(" and m2.unidadeensino = ").append(codUnidadeEnsino);
                sql.append(" and cr2.datavencimento between '").append(Uteis.getDataJDBC(atualizarData(dataLimiteInicial, contLinha - 1))).append("' and '");
                sql.append(Uteis.getDataJDBC(atualizarDataUltimoDia(dataLimiteFinal, contLinha  - 1))).append("'");
                if (codTurno != null && codTurno > 0) {
                    sql.append(" and m2.turno = ").append(codTurno);
                }
                if (codCurso != null && codCurso > 0) {
                    sql.append(" and m2.curso = ").append(codCurso);
                }
                if (contColuna < 6) {
                    sql.append(") and tiporecebimento != 'DE' and formapagamentonegociacaorecebimento is not null group by datarecebimento) as mes").append(contColuna).append(") as mes").append(contColuna).append(", ");
                } else {
                    sql.append(") and tiporecebimento != 'DE' and formapagamentonegociacaorecebimento is not null group by datarecebimento) as mes").append(contColuna).append(") as mes").append(contColuna);
                }
                contColuna++;
            }
            if (contLinha < 6) {
                sql.append(" UNION ALL");
            }
            contLinha++;
            contColuna = 1;
        }

        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        setListaRastreamentoRecVO(new ArrayList<RastreamentoRecebimentoRelVO>(0));

        while (resultado.next()) {
            setRastreamentoRecebimentoRelVO(montarDados(resultado));
            listaRastreamentoRecVO.add(getRastreamentoRecebimentoRelVO());

        }

        return getListaRastreamentoRecVO();
    }

    @Override
    public Date atualizarData(Date data, int indice) {
        Calendar dataRetorno = Calendar.getInstance();
        dataRetorno.setTime(data);
        dataRetorno.add(Calendar.MONTH, indice);
        return dataRetorno.getTime();
    }
    
    public Date atualizarDataUltimoDia(Date data, int indice) {
        Calendar dataRetorno = Calendar.getInstance();
        dataRetorno.setTime(data);
        dataRetorno.add(Calendar.MONTH, indice + 1);
        dataRetorno.set(Calendar.DAY_OF_MONTH, 1);
        dataRetorno.add(Calendar.DAY_OF_MONTH, -1);
        return dataRetorno.getTime();
    }

    @Override
    public String nomeMesExtenso(int indice) throws Exception {
        String retorno;
        switch (indice) {
            case 1:
                retorno = "JANEIRO";
                break;
            case 2:
                retorno = "FEVEREIRO";
                break;
            case 3:
                retorno = "MARÇO";
                break;
            case 4:
                retorno = "ABRIL";
                break;
            case 5:
                retorno = "MAIO";
                break;
            case 6:
                retorno = "JUNHO";
                break;
            case 7:
                retorno = "JULHO";
                break;
            case 8:
                retorno = "AGOSTO";
                break;
            case 9:
                retorno = "SETEMBRO";
                break;
            case 10:
                retorno = "OUTUBRO";
                break;
            case 11:
                retorno = "NOVEMBRO";
                break;
            case 12:
                retorno = "DEZEMBRO";
                break;
            default:
                retorno = "";
                break;
        }
        return retorno;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
     * <code>RastreamentoRecebimentoRelVO</code>.
     * 
     * @return O objeto da classe <code>RastreamentoRecebimentoRelVO</code> com os dados devidamente montados.
     */
    public static RastreamentoRecebimentoRelVO montarDados(SqlRowSet dadosSQL) throws Exception {

        RastreamentoRecebimentoRelVO obj = new RastreamentoRecebimentoRelVO();

        obj.setFaturamento(String.valueOf(dadosSQL.getDouble("faturamento")));
        obj.setNumeroParcela(String.valueOf(dadosSQL.getInt("numeroparcelas")));
        obj.setValor1(String.valueOf(dadosSQL.getDouble("mes1")));
        obj.setValor2(String.valueOf(dadosSQL.getDouble("mes2")));
        obj.setValor3(String.valueOf(dadosSQL.getDouble("mes3")));
        obj.setValor4(String.valueOf(dadosSQL.getDouble("mes4")));
        obj.setValor5(String.valueOf(dadosSQL.getDouble("mes5")));
        obj.setValor6(String.valueOf(dadosSQL.getDouble("mes6")));

        return obj;
    }

    @Override
    public void validarDados(Integer codigoUnidadeEnsino, String mes, String ano) throws ConsistirException {
        if (codigoUnidadeEnsino == null || codigoUnidadeEnsino == 0) {
            throw new ConsistirException("A Unidade De Ensino deve ser informada para a geração do relatório.");
        }

        if (mes == null || mes.equals("")) {
            throw new ConsistirException("O Mês deve ser informado para a geração do relatório.");
        }

        if (ano == null || ano.equals("")) {
            throw new ConsistirException("O Ano deve ser informado para a geração do relatório.");
        }

    }

    @Override
    public Date montarData(Integer qtdDias, int mes, int ano) throws Exception {
        Calendar data = Calendar.getInstance();
        data.set(Calendar.MONTH, (mes - 1));
        data.set(Calendar.YEAR, ano);
        data.set(Calendar.DAY_OF_MONTH, qtdDias);
        return data.getTime();
    }

    public int mesData(Date dataPrm) {
        Calendar dataCalendar = Calendar.getInstance();
        dataCalendar.setTime(dataPrm);
        int mes = dataCalendar.get(Calendar.MONTH) + 1;
        return mes;
    }

    public RastreamentoRecebimentoRelVO getRastreamentoRecebimentoRelVO() {
        return rastreamentoRecebimentoRelVO;
    }

    public void setRastreamentoRecebimentoRelVO(RastreamentoRecebimentoRelVO rastreamentoRecebimentoRelVO) {
        this.rastreamentoRecebimentoRelVO = rastreamentoRecebimentoRelVO;
    }

    public List<RastreamentoRecebimentoRelVO> getListaRastreamentoRecVO() {
        return listaRastreamentoRecVO;
    }

    public void setListaRastreamentoRecVO(List<RastreamentoRecebimentoRelVO> listaRastreamentoRecVO) {
        this.listaRastreamentoRecVO = listaRastreamentoRecVO;
    }

    public List<Integer> getInteiros() {
        return inteiros;
    }

    public void setInteiros(List<Integer> inteiros) {
        this.inteiros = inteiros;
    }

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

}
