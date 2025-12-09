package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.FormaIngresso;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.AlunosProUniRelListaVO;
import relatorio.negocio.comuns.academico.AlunosProUniRelVO;
import relatorio.negocio.interfaces.academico.AlunosProUniRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class AlunosProUniRel extends SuperRelatorio implements AlunosProUniRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public static String getIdEntidade() {
        return "AlunosProUniRel";
    }

    @Override
    public void validarDados(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, String ano, String semestre) throws ConsistirException {
        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == 0) {
            throw new ConsistirException("A Unidade De Ensino deve ser informada para a geração do relatório.");
        }

        if (curso == null || curso.getCodigo() == 0) {
            throw new ConsistirException("O Curso deve ser informado para a geração do relatório.");
        }
        if (!curso.getNivelEducacionalPosGraduacao()) {
            if (ano == null || ano.equals("")) {
                throw new ConsistirException("O Ano deve ser informado para a geração do relatório.");
            }

            if (semestre == null || semestre.equals("")) {
                throw new ConsistirException("O Semestre deve ser informado para a geração do relatório.");
            }
            
            if (ano != null && ano.length() > 0 && ano.length() < 4) {
            	throw new ConsistirException("O ANO deve possuir 4 digitos.");
            }
        }
    }

    @Override
    public List<AlunosProUniRelVO> consultarMatriculaPorFormaIngresso(Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurma, String ano, String semestre) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select matriculaPeriodo.ano ano, matriculaPeriodo.semestre semestre, ");
        sql.append(" periodoLetivo.descricao periodoLetivo, pessoa.nome nome, ");
        sql.append(" curso.descricao curso, cppc.variavel1 valorCheioMensalidade, turma.identificadorTurma identificadorTurma, ");
        sql.append(" turno.nome turno, unidadeEnsino.nome unidadeEnsino, matricula.matricula matricula ");
        sql.append(" from matricula matricula ");
        sql.append(" inner join matriculaPeriodo matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
        if ((ano == null || ano.equals("")) || (semestre == null || semestre.equals(""))) {
          sql.append(" and matriculaPeriodo.codigo = ");
          sql.append(" (select mp.codigo from matriculaPeriodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre) asc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo asc  limit 1) ");
        }
        sql.append("inner join pessoa pessoa on matricula.aluno = pessoa.codigo ");
        sql.append("inner join unidadeEnsino unidadeEnsino on matricula.unidadeEnsino = unidadeEnsino.codigo ");
        sql.append("inner join curso curso on matricula.curso = curso.codigo ");
        sql.append("inner join turma turma on matriculaPeriodo.turma = turma.codigo ");
        sql.append("inner join turno turno on matricula.turno = turno.codigo ");
        sql.append("inner join condicaoPagamentoPlanoFinanceiroCurso cppc on matriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso = cppc.codigo ");
        sql.append("inner join periodoLetivo periodoLetivo on matriculaPeriodo.periodoLetivoMatricula = periodoLetivo.codigo ");
        sql.append(" where matricula.formaIngresso = '").append(FormaIngresso.PROUNI.getValor()).append("' ");
        sql.append(" and matriculaPeriodo.situacaoMatriculaPeriodo in ('AT', 'FI', 'FO') ");
        if (codigoUnidadeEnsino != null && !codigoUnidadeEnsino.equals(0)) {
            sql.append(" and matricula.unidadeEnsino = ").append(codigoUnidadeEnsino);
        }
        if (codigoTurma != null && !codigoTurma.equals(0)) {
            sql.append(" and turma.codigo = ").append(codigoTurma);
        }
        if (codigoCurso != null && !codigoCurso.equals(0)) {
            sql.append(" and curso.codigo = ").append(codigoCurso);
        }
        if (ano != null && !ano.equals("")) {
            sql.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
        }
        if (semestre != null && !semestre.equals("")) {
            sql.append(" and matriculaPeriodo.semestre = '").append(semestre).append("' ");
        }
        sql.append(" ORDER BY turma.identificadorturma, pessoa.nome ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<AlunosProUniRelVO> alunosProUniRelVOs = new ArrayList<AlunosProUniRelVO>(0);
		while (resultado.next()) {
			AlunosProUniRelVO obj = new AlunosProUniRelVO();
			obj.setUnidadeEnsino(resultado.getString("unidadeEnsino"));
			obj.setAno(resultado.getString("ano"));
			obj.setCurso(resultado.getString("curso"));
			obj.setSemestre(resultado.getString("semestre"));
			obj.setTurno(resultado.getString("turno"));
			obj.setTurma(resultado.getString("identificadorTurma"));
			obj.setMatricula(resultado.getString("matricula"));
			obj.setNomeAluno(resultado.getString("nome"));
			obj.setPeriodoLetivo(resultado.getString("periodoLetivo"));
			obj.setValorCheioMensalidade(resultado.getString("valorCheioMensalidade"));
			alunosProUniRelVOs.add(obj);
		}
		return alunosProUniRelVOs;
    }

    @Override
    public String designIReportRelatorioExcel() {    
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "Excel" + ".jrxml");
    }
    
    @Override
    public String designIReportRelatorioPDF() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "PDF" + ".jrxml");
    }
    
    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
     * <code>AlunosProUniRelVO</code>.
     * 
     * @return O objeto da classe <code>AlunosProUniRelVO</code> com os dados devidamente montados.
     */
    public static AlunosProUniRelListaVO montarDadosMatricula(SqlRowSet dadosSQL) throws Exception {
        AlunosProUniRelListaVO obj = new AlunosProUniRelListaVO();
        obj.setMatricula(dadosSQL.getString("matricula"));
        obj.setNomeAluno(dadosSQL.getString("nome"));
        obj.setPeriodoLetivo(dadosSQL.getString("periodoLetivo"));
        obj.setValorCheioMensalidade(dadosSQL.getString("valorCheioMensalidade"));
        return obj;
    }

}
