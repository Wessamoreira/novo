package relatorio.negocio.jdbc.bancocurriculum;

import java.io.File;
import java.util.Date;
import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.bancocurriculum.ContatoEmpresaBancoTalentoRelVO;
import relatorio.negocio.comuns.bancocurriculum.EmpresaBancoTalentoRelVO;
import relatorio.negocio.comuns.bancocurriculum.VagasBancoTalentoRelVO;

import relatorio.negocio.interfaces.bancocurriculum.VagasBancoTalentoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class EmpresaVagaBancoTalentoRel extends SuperRelatorio implements VagasBancoTalentoRelInterfaceFacade {

    public EmpresaVagaBancoTalentoRel() {
    }

    public List<EmpresaBancoTalentoRelVO> criarObjetoPDF(List<EmpresaBancoTalentoRelVO> listaObjetos, VagasVO vagas, ParceiroVO parceiro, Date dataInicio, Date dataFim, String situacaoVaga, UsuarioVO usuarioVO, String ordenacao) throws Exception {
        listaObjetos.clear();
        SqlRowSet dadosSQL = executarConsultaParametrizadaPDF(parceiro, vagas, dataInicio, dataFim, situacaoVaga, usuarioVO, ordenacao);
        while (dadosSQL.next()) {
            montarDadosPDF(listaObjetos, dadosSQL);
        }
        return listaObjetos;
    }

    public List<EmpresaBancoTalentoRelVO> criarObjetoExcel(List<EmpresaBancoTalentoRelVO> listaObjetos, VagasVO vagas, ParceiroVO parceiro, Date dataInicio, Date dataFim, String situacaoVaga, UsuarioVO usuarioVO, String ordenacao) throws Exception {
        listaObjetos.clear();
        SqlRowSet dadosSQL = executarConsultaParametrizadaExcel(parceiro, vagas, dataInicio, dataFim, situacaoVaga, usuarioVO, ordenacao);
        while (dadosSQL.next()) {
            montarDadosExcel(listaObjetos, dadosSQL);
        }
        return listaObjetos;
    }

    private void montarDadosPDF(List<EmpresaBancoTalentoRelVO> listaObjetos, SqlRowSet dadosSQL) throws Exception  {
        EmpresaBancoTalentoRelVO empresaBancoTalentoRelVO = consultarEmpresaBancoTalentoRelVO(listaObjetos, dadosSQL.getInt("codigoParceiro"));
        if (empresaBancoTalentoRelVO.getCodigoempresa().equals(0)) {
            empresaBancoTalentoRelVO.setCodigoempresa(dadosSQL.getInt("codigoParceiro"));
            if (dadosSQL.getString("nomeContato") != null && dadosSQL.getString("telefoneContato") != null && dadosSQL.getString("emailContato") != null) {
                empresaBancoTalentoRelVO.setContato(dadosSQL.getString("nomeContato") + " - " + dadosSQL.getString("telefoneContato") + " - " + dadosSQL.getString("emailContato"));
            } else {
                empresaBancoTalentoRelVO.setContato("");
            }
            empresaBancoTalentoRelVO.setEstado(dadosSQL.getString("nomeEstado"));
            empresaBancoTalentoRelVO.setCidade(dadosSQL.getString("nomeCidade"));
            empresaBancoTalentoRelVO.setEmpresa(dadosSQL.getString("nomeParceiro"));
        }
        ContatoEmpresaBancoTalentoRelVO contatoEmpresaBancoTalentoRelVO = new ContatoEmpresaBancoTalentoRelVO();
        contatoEmpresaBancoTalentoRelVO.setCodigoContato(dadosSQL.getInt("codigoContato"));
        contatoEmpresaBancoTalentoRelVO.setNomeContato(dadosSQL.getString("nomeContato"));
        contatoEmpresaBancoTalentoRelVO.setTelefoneContato(dadosSQL.getString("telefoneContato"));
        contatoEmpresaBancoTalentoRelVO.setEmailContato(dadosSQL.getString("emailContato"));
        adicionarContatoEmpresaBancoTalentoRelVO(empresaBancoTalentoRelVO.getListaContatos(), contatoEmpresaBancoTalentoRelVO);
        VagasBancoTalentoRelVO vagasBancoTalentoRelVO = new VagasBancoTalentoRelVO();
        vagasBancoTalentoRelVO.setCodigoVaga(dadosSQL.getInt("codigoVaga"));
        vagasBancoTalentoRelVO.setVaga(dadosSQL.getString("vaga"));
        vagasBancoTalentoRelVO.setNumeroVagas(dadosSQL.getInt("numeroVagas"));
        vagasBancoTalentoRelVO.setListaVagaContatoVOs(getFacadeFactory().getVagaContatoFacade().consultarPorVaga(vagasBancoTalentoRelVO.getCodigoVaga()));
        vagasBancoTalentoRelVO.setAreaProfissional(dadosSQL.getString("areaprofissional_descricao"));
  //      vagasBancoTalentoRelVO.setEstado(dadosSQL.getString("nomeEstadoVaga"));
//        VagasVO vaga = new VagasVO();
//        vaga.setCodigo(vagasBancoTalentoRelVO.getCodigoVaga());
//        vaga.setVagaEstadoVOs(getFacadeFactory().getVagaEstadoFacade().consultarPorVaga(vagasBancoTalentoRelVO.getCodigoVaga()));
//        vaga.setVagaContatoVOs(getFacadeFactory().getVagaContatoFacade().consultarPorVaga(vagasBancoTalentoRelVO.getCodigoVaga()));
//        vagasBancoTalentoRelVO.setEstado(vaga.getEstadosVaga());
//        vaga.setVagaAreaVOs(getFacadeFactory().getVagaAreaFacade().consultarPorVaga(vagasBancoTalentoRelVO.getCodigoVaga()));
//        vagasBancoTalentoRelVO.setAreaProfissional(vaga.getAreasVaga());        
        vagasBancoTalentoRelVO.setDataInicio(dadosSQL.getDate("dataInicio"));
        vagasBancoTalentoRelVO.setDataFim(dadosSQL.getDate("dataFim"));
        vagasBancoTalentoRelVO.setTempoVeiculada(new Double(dadosSQL.getDouble("tempoVeiculada")).intValue());
        vagasBancoTalentoRelVO.setQtdCandidatos(dadosSQL.getInt("qtdCandidatos"));
        adicionarVagaEmpresaBancoTalentoRelVO(empresaBancoTalentoRelVO.getListaVagas(), vagasBancoTalentoRelVO);
        adicionarEmpresaBancoTalentoRelVO(listaObjetos, empresaBancoTalentoRelVO);
    }

    private void montarDadosExcel(List<EmpresaBancoTalentoRelVO> listaObjetos, SqlRowSet dadosSQL) throws Exception {
        EmpresaBancoTalentoRelVO empresaBancoTalentoRelVO = new EmpresaBancoTalentoRelVO();
        empresaBancoTalentoRelVO.setCodigoempresa(dadosSQL.getInt("codigoParceiro"));
        empresaBancoTalentoRelVO.setEstado(dadosSQL.getString("nomeEstado"));
        empresaBancoTalentoRelVO.setCidade(dadosSQL.getString("nomeCidade"));
        empresaBancoTalentoRelVO.setEmpresa(dadosSQL.getString("nomeParceiro"));
        SqlRowSet dadosContato = executarConsultaContatoParceiro(empresaBancoTalentoRelVO.getCodigoempresa());
        if (dadosContato.next()) {
            ContatoEmpresaBancoTalentoRelVO contatoEmpresaBancoTalentoRelVO = new ContatoEmpresaBancoTalentoRelVO();
            contatoEmpresaBancoTalentoRelVO.setCodigoContato(dadosContato.getInt("codigoContato"));
            contatoEmpresaBancoTalentoRelVO.setNomeContato(dadosContato.getString("nomeContato"));
            contatoEmpresaBancoTalentoRelVO.setTelefoneContato(dadosContato.getString("telefoneContato"));
            contatoEmpresaBancoTalentoRelVO.setEmailContato(dadosContato.getString("emailContato"));
            empresaBancoTalentoRelVO.getListaContatos().add(contatoEmpresaBancoTalentoRelVO);
        }
        VagasBancoTalentoRelVO vagasBancoTalentoRelVO = new VagasBancoTalentoRelVO();
        vagasBancoTalentoRelVO.setCodigoVaga(dadosSQL.getInt("codigoVaga"));
        vagasBancoTalentoRelVO.setVaga(dadosSQL.getString("vaga"));
        vagasBancoTalentoRelVO.setNumeroVagas(dadosSQL.getInt("numerovagas"));
        vagasBancoTalentoRelVO.setAreaProfissional(dadosSQL.getString("areaprofissional_descricao"));
        //vagasBancoTalentoRelVO.setEstado(dadosSQL.getString("nomeEstadoVaga"));
        vagasBancoTalentoRelVO.setCidade(dadosSQL.getString("nomeCidadeVaga"));
        vagasBancoTalentoRelVO.setDataInicio(dadosSQL.getDate("dataInicio"));
        vagasBancoTalentoRelVO.setDataFim(dadosSQL.getDate("dataFim"));
        vagasBancoTalentoRelVO.setTempoVeiculada(new Double(dadosSQL.getDouble("tempoVeiculada")).intValue());
        vagasBancoTalentoRelVO.setQtdCandidatos(dadosSQL.getInt("qtdCandidatos"));
        empresaBancoTalentoRelVO.getListaVagas().add(vagasBancoTalentoRelVO);
        listaObjetos.add(empresaBancoTalentoRelVO);
    }

    public EmpresaBancoTalentoRelVO consultarEmpresaBancoTalentoRelVO(List<EmpresaBancoTalentoRelVO> listaObjetos, Integer codigoParceiro) {
        for (EmpresaBancoTalentoRelVO objExistente : listaObjetos) {
            if (codigoParceiro.equals(objExistente.getCodigoempresa())) {
                return objExistente;
            }
        }
        return new EmpresaBancoTalentoRelVO();
    }

    public void adicionarEmpresaBancoTalentoRelVO(List<EmpresaBancoTalentoRelVO> listaObjetos, EmpresaBancoTalentoRelVO empresaBancoTalentoRelVO) {
        int index = 0;
        for (EmpresaBancoTalentoRelVO objExistente : listaObjetos) {
            if (empresaBancoTalentoRelVO.getCodigoempresa().equals(objExistente.getCodigoempresa())) {
                return;
            }
            index++;
        }
        listaObjetos.add(empresaBancoTalentoRelVO);
    }

    public void adicionarVagaEmpresaBancoTalentoRelVO(List<VagasBancoTalentoRelVO> listaObjetos, VagasBancoTalentoRelVO vagasBancoTalentoRelVO) {
        int index = 0;
        for (VagasBancoTalentoRelVO objExistente : listaObjetos) {
            if (vagasBancoTalentoRelVO.getCodigoVaga().equals(objExistente.getCodigoVaga())) {
                listaObjetos.set(index, vagasBancoTalentoRelVO);
                return;
            }
            index++;
        }
        listaObjetos.add(vagasBancoTalentoRelVO);
    }

    public void adicionarContatoEmpresaBancoTalentoRelVO(List<ContatoEmpresaBancoTalentoRelVO> listaObjetos, ContatoEmpresaBancoTalentoRelVO contatoEmpresaBancoTalentoRelVO) {
        int index = 0;
        for (ContatoEmpresaBancoTalentoRelVO objExistente : listaObjetos) {
            if (contatoEmpresaBancoTalentoRelVO.getCodigoContato().equals(objExistente.getCodigoContato())) {
                listaObjetos.set(index, contatoEmpresaBancoTalentoRelVO);
                return;
            }
            index++;
        }
        listaObjetos.add(contatoEmpresaBancoTalentoRelVO);
    }

    public SqlRowSet executarConsultaParametrizadaPDF(ParceiroVO parceiro, VagasVO vagas, Date dataInicio, Date dataFim, String situacaoVaga, UsuarioVO usuario, String ordenacao) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append(" select parceiro.nome AS nomeParceiro, estado.sigla AS nomeEstado, cidade.nome AS nomeCidade,parceiro.codigo AS codigoParceiro,");
        selectStr.append(" contatoparceiro.nome AS nomeContato, contatoparceiro.email AS emailContato, contatoparceiro.codigo AS codigoContato ,contatoparceiro.telefone AS telefoneContato, ");
        selectStr.append(" vagas.codigo AS codigoVaga,vagas.numerovagas, vagas.cargo AS vaga, vagas.dataAtivacao AS dataInicio, vagas.dataEncerramento as dataFim, extract(day from (now() - vagas.dataAtivacao ))  AS  tempoVeiculada, ");
        selectStr.append("(select count (cv.codigo) from candidatosvagas as cv where cv.vaga = vagas.codigo ) AS qtdCandidatos ");
        selectStr.append(" ,areaprofissional.descricaoareaprofissional AS areaprofissional_descricao, cidadeVaga.nome AS nomeCidadeVaga ");
        selectStr.append(" from parceiro ");
        selectStr.append(" left join contatoparceiro ON contatoparceiro.parceiro = parceiro.codigo ");
        selectStr.append(" left join cidade on cidade.codigo = parceiro.cidade ");
        selectStr.append(" left join estado on estado.codigo = cidade.estado ");
        selectStr.append(" left join vagas on vagas.parceiro = parceiro.codigo ");

        selectStr.append(" left join cidade AS cidadeVaga on cidadeVaga.codigo = vagas.cidade   ");
        selectStr.append(" left join vagaestado AS vagaestado on vagaestado.vaga = vagas.codigo  ");
        selectStr.append(" left join estado as estadovaga on estado.codigo = vagaestado.estado ");
        selectStr.append(" left join vagaarea on vagaarea.vaga = vagas.codigo ");
        selectStr.append(" left join areaprofissional on areaprofissional.codigo = vagaarea.areaprofissional   ");

        selectStr.append(" where parceiro.participabancocurriculum  = true ");
        selectStr.append(" AND vagas.codigo is not null ");
        if (parceiro.getCodigo() != 0) {
            selectStr.append(" AND parceiro.codigo = ");
            selectStr.append(parceiro.getCodigo());
        }
        if (vagas.getCodigo() != 0) {
            selectStr.append(" AND vagas.codigo = ");
            selectStr.append(vagas.getCodigo());
        }
        if (!situacaoVaga.equals("") && situacaoVaga.equals("EX") && (!Uteis.getDataBD0000(dataInicio).equals(Uteis.getDataBD0000(new Date())) && !Uteis.getDataBD0000(dataFim).equals(Uteis.getDataBD0000(new Date())))) {
            selectStr.append(" AND vagas.situacao = '");
            selectStr.append(situacaoVaga);
            selectStr.append("'");
            selectStr.append(" AND vagas.dataExpiracao >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
            selectStr.append(" AND vagas.dataExpiracao <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
        }
        if (!situacaoVaga.equals("") && situacaoVaga.equals("EN") && (!Uteis.getDataBD0000(dataInicio).equals(Uteis.getDataBD0000(new Date())) && !Uteis.getDataBD0000(dataFim).equals(Uteis.getDataBD0000(new Date())))) {
            selectStr.append(" AND vagas.situacao = '");
            selectStr.append(situacaoVaga);
            selectStr.append("'");
            selectStr.append(" AND vagas.dataEncerramento >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
            selectStr.append(" AND vagas.dataEncerramento <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
        }
        if (!situacaoVaga.equals("") && situacaoVaga.equals("AT") && (!Uteis.getDataBD0000(dataInicio).equals(Uteis.getDataBD0000(new Date())) && !Uteis.getDataBD0000(dataFim).equals(Uteis.getDataBD0000(new Date())))) {
            selectStr.append(" AND vagas.situacao = '");
            selectStr.append(situacaoVaga);
            selectStr.append("'");
            selectStr.append(" AND vagas.dataAtivacao >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
            selectStr.append(" AND vagas.dataAtivacao <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
        }
//        if (!situacaoVaga.equals("") && !situacaoVaga.equals("EX")) {
//            selectStr.append(" AND vagas.situacao = '");
//            selectStr.append(situacaoVaga);
//            selectStr.append("'");
//        }
        if (situacaoVaga.equals("") || (!situacaoVaga.equals("AT") && !situacaoVaga.equals("EX") && !situacaoVaga.equals("EN")  ) ) {
            if (!Uteis.getDataBD0000(dataInicio).equals(Uteis.getDataBD0000(new Date())) && !Uteis.getDataBD0000(dataFim).equals(Uteis.getDataBD0000(new Date()))) {
                selectStr.append(" AND vagas.dataCadastro >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
                selectStr.append(" AND vagas.dataCadastro <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
            }
        }
        if (vagas.getAreaProfissional().getCodigo() != 0) {
            selectStr.append(" AND areaProfissional.codigo = ");
            selectStr.append(vagas.getAreaProfissional().getCodigo());
        }
//        if (!vagas.getCidade().getCodigo().equals(0)) {
//            selectStr.append(" AND vagas.cidade = ");
//            selectStr.append(vagas.getCidade().getCodigo());
//        }
        if (!vagas.getEstado().getCodigo().equals(0)) {
            selectStr.append(" AND estado.codigo = ");
            selectStr.append(vagas.getEstado().getCodigo());
        }
        if (ordenacao.equals("data")) {
            selectStr.append(" ORDER BY vagas.dataativacao desc ");
        } else if (ordenacao.equals("empresa")) {
            selectStr.append(" ORDER BY parceiro.nome ");
        } else if (ordenacao.equals("areaProfissional")) {
            selectStr.append(" ORDER BY areaProfissional.descricaoAreaProfissional ");
        } else if (ordenacao.equals("estado")) {
            selectStr.append(" ORDER BY estado.nome ");
        } else if (ordenacao.equals("situacao")) {
            selectStr.append(" ORDER BY vagas.situacao ");
        } else {
            selectStr.append(" ORDER BY vagas.codigo ");
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }

    public SqlRowSet executarConsultaParametrizadaExcel(ParceiroVO parceiro, VagasVO vagas, Date dataInicio, Date dataFim, String situacaoVaga, UsuarioVO usuario, String ordenacao) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append(" select distinct parceiro.nome AS nomeParceiro, estado.sigla AS nomeEstado, cidade.nome AS nomeCidade,parceiro.codigo AS codigoParceiro,");
        selectStr.append(" vagas.codigo AS codigoVaga, vagas.numerovagas, vagas.cargo AS vaga, vagas.dataAtivacao AS dataInicio, vagas.dataEncerramento as dataFim, extract(day from (now() - vagas.dataAtivacao ))  AS  tempoVeiculada, ");
        selectStr.append("(select count (cv.codigo) from candidatosvagas as cv where cv.vaga = vagas.codigo ) AS qtdCandidatos ");
        //selectStr.append(" ,areaprofissional.descricaoareaprofissional AS areaprofissional_descricao, cidadeVaga.nome AS nomeCidadeVaga, estadoVaga.sigla AS nomeEstadoVaga ");
        selectStr.append(" ,areaprofissional.descricaoareaprofissional AS areaprofissional_descricao, cidadeVaga.nome AS nomeCidadeVaga ");
        //selectStr.append(" ,areaprofissional.descricaoareaprofissional AS areaprofissional_descricao");
        selectStr.append(" from parceiro ");
        selectStr.append(" left join cidade on cidade.codigo = parceiro.cidade ");
        selectStr.append(" left join estado on estado.codigo = cidade.estado ");
        selectStr.append(" left join vagas on vagas.parceiro = parceiro.codigo ");

//
//        selectStr.append(" left join cidade AS cidadeVaga on cidadeVaga.codigo = vagas.cidade ");
//        selectStr.append(" left join estado AS estadoVaga on estadoVaga.codigo = cidadeVaga.estado ");
//        selectStr.append(" left join areaprofissional on areaprofissional.codigo = vagas.areaprofissional ");

        selectStr.append(" left join cidade AS cidadeVaga on cidadeVaga.codigo = vagas.cidade   ");
        selectStr.append(" left join vagaestado AS vagaestado on vagaestado.vaga = vagas.codigo  ");
        selectStr.append(" left join estado as estadovaga on estado.codigo = vagaestado.estado ");
        selectStr.append(" left join vagaarea on vagaarea.vaga = vagas.codigo ");
        selectStr.append(" left join areaprofissional on areaprofissional.codigo = vagaarea.areaprofissional   ");

        selectStr.append(" where parceiro.participabancocurriculum  = true ");
        selectStr.append(" AND vagas.codigo is not null ");
        if (parceiro.getCodigo() != 0) {
            selectStr.append(" AND parceiro.codigo = ");
            selectStr.append(parceiro.getCodigo());
        }
        if (vagas.getCodigo() != 0) {
            selectStr.append(" AND vagas.codigo = ");
            selectStr.append(vagas.getCodigo());
        }
        if (vagas.getAreaProfissional().getCodigo() != 0) {
            selectStr.append(" AND areaProfissional.codigo = ");
            selectStr.append(vagas.getAreaProfissional().getCodigo());
        }
        if (!situacaoVaga.equals("") && situacaoVaga.equals("EX") && (!Uteis.getDataBD0000(dataInicio).equals(Uteis.getDataBD0000(new Date())) && !Uteis.getDataBD0000(dataFim).equals(Uteis.getDataBD0000(new Date())))) {
            selectStr.append(" AND vagas.situacao = '");
            selectStr.append(situacaoVaga);
            selectStr.append("'");
            selectStr.append(" AND vagas.dataExpiracao >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
            selectStr.append(" AND vagas.dataExpiracao <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
        }
        if (!situacaoVaga.equals("") && situacaoVaga.equals("EN") && (!Uteis.getDataBD0000(dataInicio).equals(Uteis.getDataBD0000(new Date())) && !Uteis.getDataBD0000(dataFim).equals(Uteis.getDataBD0000(new Date())))) {
            selectStr.append(" AND vagas.situacao = '");
            selectStr.append(situacaoVaga);
            selectStr.append("'");
            selectStr.append(" AND vagas.dataEncerramento >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
            selectStr.append(" AND vagas.dataEncerramento <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
        }
        if (!situacaoVaga.equals("") && situacaoVaga.equals("AT") && (!Uteis.getDataBD0000(dataInicio).equals(Uteis.getDataBD0000(new Date())) && !Uteis.getDataBD0000(dataFim).equals(Uteis.getDataBD0000(new Date())))) {
            selectStr.append(" AND vagas.situacao = '");
            selectStr.append(situacaoVaga);
            selectStr.append("'");
            selectStr.append(" AND vagas.dataAtivacao >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
            selectStr.append(" AND vagas.dataAtivacao <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
        }
//        if (!situacaoVaga.equals("") && !situacaoVaga.equals("EX")) {
//            selectStr.append(" AND vagas.situacao = '");
//            selectStr.append(situacaoVaga);
//            selectStr.append("'");
//        }
        if (situacaoVaga.equals("") || (!situacaoVaga.equals("AT") && !situacaoVaga.equals("EX") && !situacaoVaga.equals("EN")  ) ) {
            if (!Uteis.getDataBD0000(dataInicio).equals(Uteis.getDataBD0000(new Date())) && !Uteis.getDataBD0000(dataFim).equals(Uteis.getDataBD0000(new Date()))) {
                selectStr.append(" AND vagas.dataCadastro >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
                selectStr.append(" AND vagas.dataCadastro <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
            }
        }
//        if (!vagas.getCidade().getCodigo().equals(0)) {
//            selectStr.append(" AND vagas.cidade = ");
//            selectStr.append(vagas.getCidade().getCodigo());
//        }
        if (!vagas.getEstado().getCodigo().equals(0)) {
            selectStr.append(" AND estado.codigo = ");
            selectStr.append(vagas.getEstado().getCodigo());
        }
        if (ordenacao.equals("data")) {
            selectStr.append(" ORDER BY vagas.dataativacao desc ");
        } else if (ordenacao.equals("empresa")) {
            selectStr.append(" ORDER BY parceiro.nome ");
        } else if (ordenacao.equals("areaProfissional")) {
            selectStr.append(" ORDER BY areaProfissional.descricaoAreaProfissional ");
        } else if (ordenacao.equals("estado")) {
            selectStr.append(" ORDER BY estado.nome ");
        } else if (ordenacao.equals("situacao")) {
            selectStr.append(" ORDER BY vagas.situacao ");
        } else {
            selectStr.append(" ORDER BY vagas.codigo ");
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }

    public SqlRowSet executarConsultaContatoParceiro(Integer parceiro) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append(" select contatoparceiro.nome AS nomeContato, contatoparceiro.email AS emailContato, contatoparceiro.codigo AS codigoContato ,contatoparceiro.telefone AS telefoneContato ");
        selectStr.append(" from contatoparceiro ");
        selectStr.append(" inner join parceiro ON contatoparceiro.parceiro = parceiro.codigo ");
        //selectStr.append(" left join cidade on cidade.codigo = parceiro.cidade ");
        //selectStr.append(" left join estado on estado.codigo = cidade.estado ");
        //selectStr.append(" left join vagas on vagas.parceiro = parceiro.codigo ");
        //selectStr.append(" left join cidade AS cidadeVaga on cidadeVaga.codigo = vagas.cidade ");
        //selectStr.append(" left join estado AS estadoVaga on estadoVaga.codigo = cidadeVaga.estado ");
        //selectStr.append(" left join areaprofissional on areaprofissional.codigo = vagas.areaprofissional ");
        selectStr.append(" where parceiro.participabancocurriculum  = true  AND parceiro.codigo = ").append(parceiro);
        selectStr.append(" limit 1 ");


        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
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

    public static String getIdEntidade() {
        return "EmpresaVagaBancoTalentoRel";
    }

    public static String getIdEntidadeExcel() {
        return "EmpresaVagaBancoTalentoRelExcel";
    }
}
