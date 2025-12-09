/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.bancocurriculum;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.bancocurriculum.enumeradores.SituacaoReferenteVagaEnum;
import negocio.comuns.financeiro.ContatoParceiroVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.interfaces.bancocurriculum.EmpresaPorVagasRelInterfaceFacade;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import relatorio.negocio.comuns.bancocurriculum.EmpresaPorVagasRelVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author rogerio.gomes
 */
@Repository
@Scope("singleton")
@Lazy
public class EmpresaPorVagasRel extends SuperRelatorio implements EmpresaPorVagasRelInterfaceFacade {

    public EmpresaPorVagasRel() {
    }

    public static String getIdEntidade() {
        return "EmpresaPorVagasRel";
    }

    public static String getIdEntidadeExcel() {
        return "EmpresaPorVagasRelExcel";
    }

    public List<EmpresaPorVagasRelVO> criarObjeto(VagasVO vagas, ParceiroVO parceiro, Date dataInicio, Date dataFim, String situacaoVaga, Boolean contratados) throws Exception {
        List<EmpresaPorVagasRelVO> empresaPorVagasRelVOs = new ArrayList<EmpresaPorVagasRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizada(vagas, parceiro, dataInicio, dataFim, situacaoVaga, contratados);
        while (dadosSQL.next()) {
            montarDados(empresaPorVagasRelVOs, dadosSQL);
        }
        return empresaPorVagasRelVOs;


    }

    public SqlRowSet executarConsultaParametrizada(VagasVO vagas, ParceiroVO parceiro, Date dataInicio, Date dataFim, String situacaoVaga, Boolean contratados) throws Exception {
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT parceiro.codigo AS codigoParceiro, parceiro.nome AS nomeParceiro, estado.nome AS estado, cidade.nome AS cidade, parceiro.telcomercial1 AS telefone, ");
        sqlsb.append("(Select COUNT (vagas.codigo) from vagas where vagas.parceiro = parceiro.codigo and  vagas.situacao = 'AT') as vagasAberta,");
        sqlsb.append("(Select COUNT (vagas.codigo) from vagas where vagas.parceiro = parceiro.codigo and  vagas.situacao = 'EX') as vagasExpirada,");
        sqlsb.append("(Select COUNT (vagas.codigo) from vagas where vagas.parceiro = parceiro.codigo and  vagas.situacao = 'EN') as vagasEncerrada,");
        sqlsb.append("(Select COUNT (vagas.codigo) from vagas inner join candidatosvagas on  candidatosvagas.vaga = vagas.codigo where vagas.parceiro = parceiro.codigo and  candidatosvagas.situacaoreferentevaga = 'SELECIONADO') as contratados, ");
        sqlsb.append(" contatoParceiro.nome, contatoParceiro.email, contatoParceiro.telefone");

        sqlsb.append(" FROM parceiro ");
        sqlsb.append(" LEFT JOIN cidade on parceiro.cidade = cidade.codigo ");
        sqlsb.append(" LEFT JOIN estado on estado.codigo = cidade.estado ");
        sqlsb.append(" LEFT JOIN vagas on vagas.parceiro = parceiro.codigo ");
        sqlsb.append(" LEFT JOIN contatoParceiro on contatoParceiro.parceiro = parceiro.codigo ");
        if (contratados) {
            sqlsb.append(" INNER JOIN candidatosvagas on  candidatosvagas.vaga = vagas.codigo and  candidatosvagas.situacaoreferentevaga = '").append(SituacaoReferenteVagaEnum.SELECIONADO.toString()).append("'");
        }
        sqlsb.append(" where participabancocurriculum = true ");
        if (!Uteis.getDataBD0000(dataInicio).equals(Uteis.getDataBD0000(new Date())) && !Uteis.getDataBD0000(dataFim).equals(Uteis.getDataBD0000(new Date()))) {
            sqlsb.append(" AND vagas.dataCadastro >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
            sqlsb.append(" AND vagas.dataCadastro <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
        }
        if (parceiro.getCodigo() != 0) {
            sqlsb.append(" AND parceiro.codigo = ");
            sqlsb.append(parceiro.getCodigo());
        }
        if (vagas.getCodigo() != 0) {
            sqlsb.append(" AND vagas.codigo = ");
            sqlsb.append(vagas.getCodigo());
        }
        if (!situacaoVaga.equals("")) {
            sqlsb.append(" AND vagas.situacao = '").append(situacaoVaga).append("' ");
        }
        sqlsb.append(" group by codigoparceiro, nomeparceiro, estado.nome,vagasaberta, vagasexpirada, vagasencerrada, contratados, cidade.nome, parceiro.telcomercial1,  ");
        sqlsb.append(" contatoparceiro.nome, contatoparceiro.email, contatoparceiro.telefone ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());

        return tabelaResultado;
    }

    private void montarDados(List<EmpresaPorVagasRelVO> empresaPorVagasRelVOs, SqlRowSet dadosSQL) throws Exception {
        EmpresaPorVagasRelVO empresaPorVagasRelVO = consultarEmpresaPorVagasRelVO(empresaPorVagasRelVOs, dadosSQL.getInt("codigoParceiro"));
        if (empresaPorVagasRelVO.getCodigoParceiro() == null || empresaPorVagasRelVO.getCodigoParceiro().intValue() == 0) {
            empresaPorVagasRelVO.setCodigoParceiro(dadosSQL.getInt("codigoParceiro"));
            empresaPorVagasRelVO.setEstado(dadosSQL.getString("estado"));
            empresaPorVagasRelVO.setNomeParceiro(dadosSQL.getString("nomeParceiro"));
            empresaPorVagasRelVO.setContratados(dadosSQL.getInt("contratados"));
            empresaPorVagasRelVO.setVagasAbertas(dadosSQL.getInt("vagasAberta"));
            empresaPorVagasRelVO.setVagasEncerradas(dadosSQL.getInt("vagasEncerrada"));
            empresaPorVagasRelVO.setVagasExpiradas(dadosSQL.getInt("vagasExpirada"));
            empresaPorVagasRelVO.setTelefoneEmpresa(dadosSQL.getString("telefone"));
            empresaPorVagasRelVO.setCidade(dadosSQL.getString("cidade"));
        }
        ContatoParceiroVO contato = new ContatoParceiroVO();
        if (dadosSQL.getString("nome") != null &&  !dadosSQL.getString("nome").isEmpty()) {
            contato.setNome(dadosSQL.getString("nome"));
            contato.setEmail(dadosSQL.getString("email"));
            contato.setTelefone(dadosSQL.getString("telefone"));
            empresaPorVagasRelVO.setApresentarContatos(Boolean.TRUE);
            empresaPorVagasRelVO.getContatoParceiroVOs().add(contato);
        }
        adicionarEmpresaPorVagasRelVO(empresaPorVagasRelVOs, empresaPorVagasRelVO);
    }

    public EmpresaPorVagasRelVO consultarEmpresaPorVagasRelVO(List<EmpresaPorVagasRelVO> listaObjetos, Integer codigoParceiro) {
        for (EmpresaPorVagasRelVO objExistente : listaObjetos) {
            if (codigoParceiro.equals(objExistente.getCodigoParceiro())) {
                return objExistente;
            }
        }
        return new EmpresaPorVagasRelVO();
    }

    public void adicionarEmpresaPorVagasRelVO(List<EmpresaPorVagasRelVO> listaObjetos, EmpresaPorVagasRelVO empresaPorVagasRelVO) {
        int index = 0;
        for (EmpresaPorVagasRelVO objExistente : listaObjetos) {
            if (empresaPorVagasRelVO.getCodigoParceiro().equals(objExistente.getCodigoParceiro())) {
                listaObjetos.set(index, empresaPorVagasRelVO);
                return;
            }
            index++;
        }
        listaObjetos.add(empresaPorVagasRelVO);
    }

    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator + getIdEntidade() + ".jrxml");
    }

    public String designIReportRelatorioExcel() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator + getIdEntidadeExcel() + ".jrxml");
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator);
    }
}
