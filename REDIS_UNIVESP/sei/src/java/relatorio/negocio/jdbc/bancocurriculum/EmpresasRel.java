package relatorio.negocio.jdbc.bancocurriculum;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.bancocurriculum.ContatoEmpresasRelVO;
import relatorio.negocio.comuns.bancocurriculum.EmpresasRelVO;
import relatorio.negocio.interfaces.bancocurriculum.EmpresasRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class EmpresasRel extends SuperRelatorio implements EmpresasRelInterfaceFacade {

    public EmpresasRel() {
    }

    public List<EmpresasRelVO> criarObjeto(ParceiroVO empresa, CidadeVO cidade, EstadoVO estado, String ordenarPor, String tipoConsulta, Date dataInicio, Date dataFim) throws Exception {
        List<EmpresasRelVO> listaObjetos = new ArrayList(0);
        listaObjetos.clear();
        SqlRowSet dadosSQL = executarConsultaParametrizada(empresa, cidade, estado, ordenarPor, tipoConsulta, dataInicio, dataFim);
        while (dadosSQL.next()) {
            montarDados(listaObjetos, dadosSQL);
        }
        return listaObjetos;
    }

    private void montarDados(List<EmpresasRelVO> listaObjetos, SqlRowSet dadosSQL) {
        EmpresasRelVO empresasRelVO = consultarEmpresaBancoTalentoRelVO(listaObjetos, dadosSQL.getInt("codigoEmpresa"));
        empresasRelVO.setCodigoEmpresa(dadosSQL.getInt("codigoEmpresa"));
        empresasRelVO.setNomeEmpresa(dadosSQL.getString("nomeEmpresa"));
        empresasRelVO.setTelefoneEmpresa(dadosSQL.getString("telefoneEmpresa"));
        empresasRelVO.setCnpj(dadosSQL.getString("cnpj"));
        empresasRelVO.setCidade(dadosSQL.getString("cidade"));
        empresasRelVO.setDataCadastro(dadosSQL.getDate("dataCadastro"));
        empresasRelVO.setEstado(dadosSQL.getString("estado"));
        empresasRelVO.setDataUltimoAcesso(dadosSQL.getDate("dataUltimoAcesso"));
        ContatoEmpresasRelVO contatoEmpresasRelVO = new ContatoEmpresasRelVO();
        contatoEmpresasRelVO.setNomeContato(dadosSQL.getString("contatoparceiro_nome"));
        contatoEmpresasRelVO.setTelefone(dadosSQL.getString("contatoparceiro_telefone"));
        contatoEmpresasRelVO.setEmail(dadosSQL.getString("contatoparceiro_email"));
        
        empresasRelVO.getListaContatoEmpresasRelVO().add(contatoEmpresasRelVO);
        adicionarEmpresaBancoTalentoRelVO(listaObjetos, empresasRelVO);
    }

    public EmpresasRelVO consultarEmpresaBancoTalentoRelVO(List<EmpresasRelVO> listaObjetos, Integer codigoEmpresa) {
        for (EmpresasRelVO objExistente : listaObjetos) {
            if (codigoEmpresa.equals(objExistente.getCodigoEmpresa())) {
                return objExistente;
            }
        }
        return new EmpresasRelVO();
    }

    public void adicionarEmpresaBancoTalentoRelVO(List<EmpresasRelVO> listaObjetos, EmpresasRelVO empresasRelVO) {
        int index = 0;
        for (EmpresasRelVO objExistente : listaObjetos) {
            if (empresasRelVO.getCodigoEmpresa().equals(objExistente.getCodigoEmpresa())) {
                listaObjetos.set(index, empresasRelVO);
                return;
            }
            index++;
        }
        listaObjetos.add(empresasRelVO);
    }

    public SqlRowSet executarConsultaParametrizada(ParceiroVO empresa, CidadeVO cidade, EstadoVO estado, String ordenarPor, String tipoConsulta, Date dataInicio, Date dataFim) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append(" SELECT distinct parceiro.nome AS nomeEmpresa, parceiro.dataCadastro AS dataCadastro, parceiro.telComercial1 AS telefoneEmpresa, estado.nome AS estado, parceiro.codigo AS codigoEmpresa, cidade.nome AS cidade, ");
        selectStr.append(" parceiro.telcomercial1 AS telefone, parceiro.email AS email, parceiro.cnpj AS cnpj, ");
        selectStr.append(" contatoparceiro.nome AS contatoparceiro_nome, contatoparceiro.telefone AS contatoparceiro_telefone, contatoparceiro.email AS contatoparceiro_email, usuario.dataUltimoAcesso ");
        selectStr.append(" from parceiro ");
        selectStr.append(" left join cidade on cidade.codigo = parceiro.cidade ");
        selectStr.append(" left join contatoparceiro ON contatoparceiro.parceiro = parceiro.codigo ");
        selectStr.append(" left join estado on estado.codigo = cidade.estado ");
        selectStr.append(" left join vagas on vagas.parceiro = parceiro.codigo ");
        selectStr.append(" left join usuario on parceiro.codigo = usuario.parceiro ");
        selectStr.append(" where parceiro.participabancocurriculum = true ");
        if (empresa.getCodigo() != 0) {
            selectStr.append(" AND parceiro.codigo = ");
            selectStr.append(empresa.getCodigo());
        }
        if (cidade.getCodigo() != 0) {
            selectStr.append(" AND cidade.codigo = ");
            selectStr.append(cidade.getCodigo());
        }
        if (estado.getCodigo() != 0) {
            selectStr.append(" AND estado.codigo = ");
            selectStr.append(estado.getCodigo());
        }
        if(tipoConsulta.equals("CADASTRADOS_NO_PERIODO")){
        	selectStr.append(" AND parceiro.datacadastro >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
        	selectStr.append(" AND parceiro.datacadastro <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        }
        if(tipoConsulta.equals("ACESSARAM_NO_PERIODO")){
        	selectStr.append(" AND usuario.dataultimoacesso >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
        	selectStr.append(" AND usuario.dataultimoacesso <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        }
        if(tipoConsulta.equals("CADASTRARAM_VAGAS_NO_PERIODO")){
        	selectStr.append(" AND vagas.datacadastro >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
        	selectStr.append(" AND vagas.datacadastro <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        }
        
        if (ordenarPor.equals("dataUltimoAcesso")) {
            selectStr.append(" ORDER BY usuario.dataultimoacesso");
        } else {
            selectStr.append(" ORDER BY parceiro.nome");
        }
        
        
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
        return "EmpresasRel";
    }

    public static String getIdEntidadeExcel() {
        return "EmpresasRelExcel";
    }
}
