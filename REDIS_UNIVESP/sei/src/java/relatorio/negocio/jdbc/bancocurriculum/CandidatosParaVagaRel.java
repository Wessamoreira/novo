/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.bancocurriculum;

import java.io.File;
import java.util.Date;
import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import relatorio.negocio.comuns.bancocurriculum.CandidatosParaVagaRelVO;
import relatorio.negocio.comuns.bancocurriculum.CandidatosParaVagaRelVOSub;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author PEDRO
 */
@Repository
@Scope("singleton")
@Lazy
public class CandidatosParaVagaRel extends SuperRelatorio implements CandidatosParaVagaRelInterfaceFacade {

    @Override
    public List criarObjeto(List<CandidatosParaVagaRelVO> listaObjetos, VagasVO vagas, ParceiroVO parceiro, Date dataInicio, Date dataFim, String situacaoVaga, UsuarioVO usuarioVO) throws Exception {
        listaObjetos.clear();
        SqlRowSet dadosSQL = executarConsultaParametrizada(parceiro, vagas, dataInicio, dataFim, situacaoVaga, usuarioVO);
        while (dadosSQL.next()) {
            montarDados(listaObjetos, dadosSQL);
        }
        return listaObjetos;
    }

    private void montarDados(List<CandidatosParaVagaRelVO> listaObjetos, SqlRowSet dadosSQL) {
        CandidatosParaVagaRelVO candidatosParaVagaRelVO = consultarCandidatosParaVagaRelVO(listaObjetos, dadosSQL.getInt("codigo_Empresa"), dadosSQL.getInt("codigoVaga"));
        candidatosParaVagaRelVO.setCodigoEmpresa(dadosSQL.getInt("codigo_empresa"));
        candidatosParaVagaRelVO.setEmpresa(dadosSQL.getString("empresa"));
        candidatosParaVagaRelVO.setSituacaoVaga(dadosSQL.getString("situacaoVaga"));
        candidatosParaVagaRelVO.setCodigoVaga(dadosSQL.getInt("codigoVaga"));
        candidatosParaVagaRelVO.setVaga(dadosSQL.getString("vaga"));
        candidatosParaVagaRelVO.setNumeroVagas(dadosSQL.getInt("numerovagas"));
        CandidatosParaVagaRelVOSub candidatosParaVagaRelVOSub = new CandidatosParaVagaRelVOSub();
        candidatosParaVagaRelVOSub.setCodigoCandidato(dadosSQL.getInt("codigoCandidato"));
        candidatosParaVagaRelVOSub.setCandidato(dadosSQL.getString("nome"));
        candidatosParaVagaRelVOSub.setEmail(dadosSQL.getString("email"));
        candidatosParaVagaRelVOSub.setEstado(dadosSQL.getString("estado"));
        candidatosParaVagaRelVOSub.setSituacaoReferenteVaga(dadosSQL.getString("cv_situacaoreferentevaga"));
        candidatosParaVagaRelVOSub.setTelefone(dadosSQL.getString("telefone"));
        adicionarCandidatosParaVagaRelVOSub(candidatosParaVagaRelVO.getListaCandidatosParaVagaRelVOSub(), candidatosParaVagaRelVOSub);
        adicionarCandidatosParaVagaRelVO(listaObjetos, candidatosParaVagaRelVO);
    }

    public CandidatosParaVagaRelVO consultarCandidatosParaVagaRelVO(List<CandidatosParaVagaRelVO> listaObjetos, Integer codigoEmpresa, Integer codigoVaga) {
        for (CandidatosParaVagaRelVO objExistente : listaObjetos) {
            if (codigoEmpresa.equals(objExistente.getCodigoEmpresa()) && codigoVaga.equals(objExistente.getCodigoVaga())) {
                return objExistente;
            }
        }
        return new CandidatosParaVagaRelVO();
    }

    public void adicionarCandidatosParaVagaRelVO(List<CandidatosParaVagaRelVO> listaObjetos, CandidatosParaVagaRelVO candidatosParaVagaRelVO) {
        int index = 0;
        for (CandidatosParaVagaRelVO objExistente : listaObjetos) {
            if (candidatosParaVagaRelVO.getCodigoEmpresa().equals(objExistente.getCodigoEmpresa()) && candidatosParaVagaRelVO.getCodigoVaga().equals(objExistente.getCodigoVaga())) {
                listaObjetos.set(index, candidatosParaVagaRelVO);
                return;
            }
            index++;
        }
        listaObjetos.add(candidatosParaVagaRelVO);
    }

    public void adicionarCandidatosParaVagaRelVOSub(List<CandidatosParaVagaRelVOSub> listaObjetos, CandidatosParaVagaRelVOSub candidatosParaVagaRelVOSub) {
        int index = 0;
        for (CandidatosParaVagaRelVOSub objExistente : listaObjetos) {
            if (candidatosParaVagaRelVOSub.getCodigoCandidato().equals(objExistente.getCodigoCandidato())) {
                listaObjetos.set(index, candidatosParaVagaRelVOSub);
                return;
            }
            index++;
        }
        listaObjetos.add(candidatosParaVagaRelVOSub);
    }

    @Override
    public SqlRowSet executarConsultaParametrizada(ParceiroVO parceiro, VagasVO vagas, Date dataInicio, Date dataFim, String situacaoVaga, UsuarioVO usuario) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append("SELECT parceiro.codigo as codigo_empresa, parceiro.nome as empresa, vagas.cargo AS vaga, vagas.codigo AS codigoVaga, vagas.situacao AS situacaoVaga, ");
        selectStr.append("pessoa.nome AS nome, pessoa.email AS email, pessoa.codigo AS codigoCandidato, ");
        selectStr.append("(CASE WHEN pessoa.celular <> '' THEN pessoa.celular ");
        selectStr.append("WHEN  pessoa.telefonerecado <> '' THEN pessoa.telefonerecado ");
        selectStr.append("WHEN  pessoa.telefoneres <> '' THEN pessoa.telefoneres ELSE pessoa.telefonecomer END) as telefone, ");
        selectStr.append("cv.situacaoreferentevaga AS cv_situacaoreferentevaga, ");
        selectStr.append("estado.sigla as estado, vagas.numerovagas ");
        selectStr.append("FROM candidatosvagas as cv ");
        selectStr.append("INNER JOIN vagas on vagas.codigo = cv.vaga ");
        selectStr.append("INNER JOIN parceiro  on vagas.parceiro = parceiro.codigo ");
        selectStr.append("INNER JOIN pessoa on pessoa.codigo = cv.pessoa ");
        selectStr.append("LEFT JOIN cidade on pessoa.cidade = cidade.codigo ");
        selectStr.append("LEFT JOIN estado on cidade.estado = estado.codigo ");
        selectStr.append("where parceiro.participabancocurriculum  = true ");
        if (parceiro.getCodigo() != 0) {
            selectStr.append(" AND parceiro.codigo = ");
            selectStr.append(parceiro.getCodigo());
        }
        if (vagas.getCodigo() != 0) {
            selectStr.append(" AND vagas.codigo = ");
            selectStr.append(vagas.getCodigo());
        }
        if (!situacaoVaga.isEmpty()) {
            selectStr.append(" AND vagas.situacao = '").append(situacaoVaga).append("'");
        }
        if (!Uteis.getDataBD0000(dataInicio).equals(Uteis.getDataBD0000(new Date())) && !Uteis.getDataBD0000(dataFim).equals(Uteis.getDataBD0000(new Date()))) {
            selectStr.append(" AND vagas.dataCadastro >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
            selectStr.append(" AND vagas.dataCadastro <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
        }
        selectStr.append(" ORDER BY parceiro.nome, pessoa.nome, vagas.dataCadastro desc");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }

    @Override
    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator + getIdEntidade() + ".jrxml");
    }

    public String designIReportRelatorioExcel() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator + getIdEntidadeExcel() + ".jrxml");
    }

    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator);
    }

    public static String getIdEntidade() {
        return "CandidatosParaVagaRel";
    }

    public static String getIdEntidadeExcel() {
        return "CandidatosParaVagaRelExcel";
    }
}
