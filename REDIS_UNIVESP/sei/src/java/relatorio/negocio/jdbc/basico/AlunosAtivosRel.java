package relatorio.negocio.jdbc.basico;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.basico.AlunosAtivosRelVO;
import relatorio.negocio.interfaces.basico.AlunosAtivosRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * @author Danilo
 */
@Repository
@Scope("singleton")
@Lazy
public class AlunosAtivosRel extends SuperRelatorio implements AlunosAtivosRelInterfaceFacade {

    private Date data;

    public AlunosAtivosRel() {
    }

    @Override
    public void validarDados(Integer codigoUnidadeEnsino) throws ConsistirException {
        if (codigoUnidadeEnsino == null || codigoUnidadeEnsino == 0) {
            throw new ConsistirException("A Unidade de Ensino deve ser informada para a geração do relatório.");
        }
    }

    /**
     * Método responsável por criar objetos, ou seja, a listagem dos alunos ativos
     * 
     * @param data
     * @return List<AlunosAtivosRelVO>
     * @author Danilo
     * @since 28.01.2011
     */
    public List<AlunosAtivosRelVO> criarObjeto(Date data, String tipoRelatorio, Integer unidadeEnsino) throws Exception {
        AlunosAtivosRel.emitirRelatorio(getIdEntidade(), false, null); // valida permissao, e obtem conexao
        List<AlunosAtivosRelVO> listaRelatorio = new ArrayList<AlunosAtivosRelVO>(0);
        setData(data);
        if (tipoRelatorio.equals("sintetico")) {
            listaRelatorio = executarConsultaAlunosAtivosSintetico(unidadeEnsino);
        } else {
            // Executando as consultas e preenchendo os VOs
            listaRelatorio = executarConsultaAlunosAtivos(unidadeEnsino);
        }
        return listaRelatorio;
    }

    /**
     * Consulta responsável por trazer os alunos ativos
     * 
     * @return List<AlunosAtivosRelVO> Lista de Alunos que estão ativos.
     * @author Danilo
     * @since 28.01.2011
     */
    private List<AlunosAtivosRelVO> executarConsultaAlunosAtivos(Integer unidadeEnsino) throws SQLException, Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select matricula.matricula, pessoa.nome as aluno, curso.nome as curso, unidadeensino.nome as unidadeensino, ");
        sql.append("turma.identificadorturma as turma, periodoletivo.descricao as periodoletivo ");
        sql.append("from matricula ");
        sql.append("inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
        sql.append("inner join pessoa on pessoa.codigo = matricula.aluno ");
        sql.append("inner join curso on curso.codigo = matricula.curso ");
        sql.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
        sql.append("inner join turma on turma.codigo = matriculaperiodo.turma ");
        sql.append("inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
        sql.append("where matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
        sql.append("and (matricula.situacao = 'AT' or matricula.situacao = 'CO') ");
        sql.append("and (matriculaperiodo.situacao = 'AT' or matriculaperiodo.situacao = 'CO') ");
        sql.append("and matricula.unidadeensino = ");
        sql.append(unidadeEnsino);
        sql.append(" order by curso.nome, turma.identificadorturma ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosAlunosAtivos(tabelaResultado);
    }

    private List<AlunosAtivosRelVO> executarConsultaAlunosAtivosSintetico(Integer unidadeEnsino) throws SQLException, Exception {
        List<AlunosAtivosRelVO> lista = new ArrayList<AlunosAtivosRelVO>(0);
        StringBuilder sql = new StringBuilder();
        sql.append("select count(matricula.matricula) as quantidade from matricula ");
        sql.append("inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
        sql.append("inner join pessoa on pessoa.codigo = matricula.aluno ");
        sql.append("inner join curso on curso.codigo = matricula.curso ");
        sql.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
        sql.append("inner join turma on turma.codigo = matriculaperiodo.turma ");
        sql.append("where matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
        sql.append("and (matricula.situacao = 'AT' or matricula.situacao = 'CO') ");
        sql.append("and (matriculaperiodo.situacao = 'AT' or matriculaperiodo.situacao = 'CO') ");
        sql.append("and matricula.unidadeensino = ");
        sql.append(unidadeEnsino);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        tabelaResultado.next();
        if (tabelaResultado.getRow() > 0) {
            AlunosAtivosRelVO obj = new AlunosAtivosRelVO();
            obj.setQuantidadeAlunosUnidade(tabelaResultado.getInt("quantidade"));
            obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, null)
                .getNome());
            lista.add(obj);
        }
        return lista;
    }

    private List<AlunosAtivosRelVO> montarDadosAlunosAtivos(SqlRowSet tabelaResultado) throws Exception {
        List<AlunosAtivosRelVO> listaConsulta = new ArrayList<AlunosAtivosRelVO>(0);
        while (tabelaResultado.next()) {
            listaConsulta.add(montarDados(tabelaResultado));
        }
        return listaConsulta;
    }

    /**
     * Método responsável por montar um objeto aluno ativo
     * 
     * @param tabelaResultado consulta sql
     * @return List lista de objetos para relatório
     * @author Danilo
     * @since 28.01.2011
     */
    private AlunosAtivosRelVO montarDados(SqlRowSet tabelaResultado) {
        AlunosAtivosRelVO obj = new AlunosAtivosRelVO();
        obj.setMatriculaAluno(tabelaResultado.getString("matricula"));
        obj.setNomeAluno(tabelaResultado.getString("aluno"));
        obj.setCurso(tabelaResultado.getString("curso"));
        obj.setUnidadeEnsino(tabelaResultado.getString("unidadeEnsino"));
        obj.setTurma(tabelaResultado.getString("turma"));
        obj.setPeriodoLetivo(tabelaResultado.getString("periodoLetivo").length() > 1 ? tabelaResultado.getString("periodoLetivo").substring(0, 2) : tabelaResultado.getString("periodoLetivo"));
        return obj;
    }

    /**
     * Método responsável por retornar a quantidade de alunos que estão ativos no sistema
     * 
     * @param data data no qual se pretende saber a quantidade de alunos ativos
     * @return int quantidade
     * @author Danilo
     * @since 28.01.2011
     */
    public int executarConsultaQuantidadeAlunosAtivos(Date data) throws Exception {
        AlunosAtivosRel.emitirRelatorio(getIdEntidade(), false, null);
        setData(data);
        return consultarQuantidadeAlunosAtivos(data);
    }

    /**
     * Método responsável por retornar a quantidade de alunos que estão ativos no sistema
     * 
     * @param data data no qual se pretende saber a quantidade de alunos ativos
     * @return int quantidade
     * @author Danilo
     * @since 28.01.2011
     */
    private int consultarQuantidadeAlunosAtivos(Date data) {

        int semestre;
        if (Uteis.getMesData(getData()) > 7) {
            semestre = 2;
        } else {
            semestre = 1;
        }

        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" select count( distinct matricula.matricula) as quantidade from matricula ");
        sqlStr.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
        sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
        sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
        sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
        sqlStr.append(" where matriculaperiodo.situacaomatriculaperiodo = 'AT' and matriculaperiodo.ano = '").append(Uteis.getAnoData(getData())).append("' ");
        sqlStr.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return (int) tabelaResultado.getInt("quantidade");
        } else {
            return 0;
        }

    }

    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "basico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public String designIReportRelatorioSintetico() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "basico" + File.separator + getIdEntidade() + "Sintetico.jrxml");
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "basico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("AlunosAtivosRel");
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
