/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import negocio.comuns.utilitarias.ConsistirException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import relatorio.negocio.comuns.academico.UploadProfessorRelVO;
import relatorio.negocio.interfaces.academico.UploadProfessorRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class UploadProfessorRel extends SuperRelatorio implements UploadProfessorRelInterfaceFacade {

    public List<UploadProfessorRelVO> criarObjeto(Integer unidadeEnsino, Integer curso, Integer turma, Integer professor) throws Exception {
        List<UploadProfessorRelVO> tipoDescontoRelVOs = new ArrayList<UploadProfessorRelVO>(0);
        validarDados(unidadeEnsino);
        return executarConsultaParametrizada(unidadeEnsino, curso, turma, professor);
    }

    private void validarDados(Integer unidadeEnsino) throws Exception {
        if (unidadeEnsino == 0 || unidadeEnsino == null) {
            throw new ConsistirException("O campo UNIDADE ENSINO DEVE SER INFORMADO");
        }
    }

    public List<UploadProfessorRelVO> executarConsultaParametrizada(Integer unidadeEnsino, Integer curso, Integer turma, Integer professor) throws Exception {
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("select distinct arquivo.nome as arquivo, pessoa.nome as professor, arquivo.dataupload as dataupload ");
        sqlsb.append("from arquivo   ");
        sqlsb.append("inner join pessoa on pessoa.codigo = arquivo.professor ");
        sqlsb.append("inner join horarioturmaprofessordisciplina on horarioturmaprofessordisciplina.professor = arquivo.professor ");
        sqlsb.append("inner join turma on horarioturmaprofessordisciplina.turma = turma.codigo ");
        sqlsb.append("inner join curso on turma.curso = curso.codigo ");
        sqlsb.append("inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
        sqlsb.append("where unidadeensino.codigo = ").append(unidadeEnsino).append(" AND origem = 'PR' ");
        if (curso != 0) {
            sqlsb.append(" AND curso.codigo = ").append(curso);
        }
        if (turma != 0) {
            sqlsb.append(" AND turma.codigo = ").append(turma);
        }
        if (professor != 0) {
            sqlsb.append(" AND pessoa.codigo = ");
            sqlsb.append(professor);
        }
        sqlsb.append(" order by pessoa.nome, arquivo.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado);
        }
        return new ArrayList<UploadProfessorRelVO>();
    }

    public List<UploadProfessorRelVO> montarDados(SqlRowSet dadosSQL) throws Exception {
        List<UploadProfessorRelVO> listaObjs = new ArrayList<UploadProfessorRelVO>(0);
        do {
            UploadProfessorRelVO obj = new UploadProfessorRelVO();
            obj.setNomeProfessor(dadosSQL.getString("professor"));
            obj.setNomeArquivo(dadosSQL.getString("arquivo"));
            obj.setDataUpload(dadosSQL.getDate("dataupload"));
            listaObjs.add(obj);
        } while (dadosSQL.next());
        return listaObjs;
    }

    public String designIReportRelatorio() {
        return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return "UploadProfessorRel";
    }
}
