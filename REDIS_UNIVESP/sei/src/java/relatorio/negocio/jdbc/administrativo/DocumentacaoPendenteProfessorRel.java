package relatorio.negocio.jdbc.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import relatorio.negocio.comuns.administrativo.DocumentacaoPendenteProfessorRelVO;
import relatorio.negocio.comuns.administrativo.ProfessorRelVO;
import relatorio.negocio.interfaces.administrativo.DocumentacaoPendenteProfessorRelInterfaceFacade;

@Service
@Lazy
public class DocumentacaoPendenteProfessorRel extends SuperFacadeJDBC implements DocumentacaoPendenteProfessorRelInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -1208763821856859365L;

    @Override
    public List<ProfessorRelVO> consultarDadosGeracaoRelatorio(Integer unidadeEnsino, Integer professor, Integer turma, Integer curso, String escolaridade) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select  pessoa.codigo, pessoa.nome, formacaoacademica.escolaridade, tipoDocumento.nome as documento  from pessoa");
        sql.append(" inner join formacaoacademica on formacaoacademica.pessoa = pessoa.codigo and formacaoacademica.codigo = (select formacaoacademica.codigo from formacaoacademica where pessoa.codigo = formacaoacademica.pessoa order by case escolaridade when ('EF') then 1");
        sql.append(" when ('EM') then 2 when ('TE') then 3 when ('GR') then 4 when ('EP') then 5 when ('MS') then 6 when ('DR') then 7 when ('PD') then 8 end desc limit 1)");
        sql.append(" inner join documetacaopessoa on documetacaopessoa.pessoa = pessoa.codigo");
        sql.append(" inner join tipoDocumento on tipoDocumento.codigo = documetacaopessoa.tipoDocumento");
        sql.append(" where ativo  = true and professor = true and documetacaopessoa.entregue = false");
        sql.append(" and (tipoDocumento.escolaridade is null or tipoDocumento.escolaridade = '' or tipoDocumento.escolaridade = formacaoacademica.escolaridade)");
        if(escolaridade != null && !escolaridade.isEmpty()){
            sql.append(" and formacaoacademica.escolaridade = '").append(escolaridade).append("'");
        }
        if(professor != null && professor>0){
            sql.append(" and pessoa.codigo = ").append(professor);
        }
        if(unidadeEnsino != null &&  unidadeEnsino >0){
            sql.append(" and pessoa.codigo in (select distinct professor from horarioturmaprofessordisciplina inner join turma on turma.codigo = horarioturmaprofessordisciplina.turma");
            sql.append(" where  turma.unidadeEnsino = ").append(unidadeEnsino).append(") ");
        }
        if(turma != null &&  turma >0){
            sql.append(" and pessoa.codigo in (select distinct professor from horarioturmaprofessordisciplina where  turma = ").append(turma).append(") ");
        }  
        if(curso != null &&  curso >0){
            sql.append(" and pessoa.codigo in (select distinct professor from horarioturmaprofessordisciplina inner join turma on turma.codigo = horarioturmaprofessordisciplina.turma where  curso = ").append(curso).append(") ");
        } 
        sql.append(" order by pessoa.nome, tipoDocumento.nome");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
    }
    
    public List<ProfessorRelVO> montarDadosConsulta(SqlRowSet rs){
    	List<ProfessorRelVO> pessoaVOs = new ArrayList<ProfessorRelVO>(0);
    	ProfessorRelVO pessoaVO = null;
        while(rs.next()){
            if(pessoaVO == null || pessoaVO.getCodigo().intValue() != rs.getInt("codigo")){
                pessoaVO = new ProfessorRelVO();
                pessoaVO.setCodigo(rs.getInt("codigo"));
                pessoaVO.setNome(rs.getString("nome"));
                pessoaVO.setEscolaridade(NivelFormacaoAcademica.getDescricao(rs.getString("escolaridade")));
                pessoaVOs.add(pessoaVO);
            }
            pessoaVO.getDocumentacaoPendenteProfessorRelVOs().add(new DocumentacaoPendenteProfessorRelVO(rs.getString("documento")));
        }
        return pessoaVOs;
    }
    
    
    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getDesignIReportRelatorioExcel() {
    	return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + getIdEntidadeExcel() + ".jrxml");
    }
    
    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator);
    }

    public static String getIdEntidade() {        
            return "DocumentacaoPendenteProfessorRel";        
    }

    public static String getIdEntidadeExcel() {        
    	return "DocumentacaoPendenteProfessorRelExcel";        
    }
    
}
