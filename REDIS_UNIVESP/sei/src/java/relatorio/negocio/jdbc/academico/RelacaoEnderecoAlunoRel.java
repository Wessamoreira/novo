package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.academico.RelacaoEnderecoAlunoRelVO;

import relatorio.negocio.interfaces.academico.RelacaoEnderecoAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class RelacaoEnderecoAlunoRel extends SuperRelatorio implements RelacaoEnderecoAlunoRelInterfaceFacade {

    public RelacaoEnderecoAlunoRel() {
    }

    public void validarDados(TurmaVO turma, String ano, String semestre) throws Exception {
        if (turma == null || turma.getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo TURMA deve ser informado.");
        }
        if (turma.getSemestral()) {
            if (semestre == null || semestre.equals("")) {
                throw new ConsistirException("O campo SEMESTRE deve ser informado");
            }
            if (ano == null || ano.equals("")) {
                throw new ConsistirException("O campo ANO deve ser informado");
            }
            
            if (ano.length() < 4) {
                throw new ConsistirException("O campo ANO deve ter 4 dígitos");
            }
        }
        if (turma.getAnual()) {
            if (ano.equals("")) {
                throw new ConsistirException("O campo ANO deve ser informado");
            }
        }
    }

    public List<RelacaoEnderecoAlunoRelVO> criarObjeto(TurmaVO turma, String ano, String semestre) throws Exception {
        validarDados(turma, ano, semestre);
        List<RelacaoEnderecoAlunoRelVO> relacaoEnderecoAlunoRelVOs = new ArrayList<RelacaoEnderecoAlunoRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizada(turma, ano, semestre);
        while (dadosSQL.next()) {
            relacaoEnderecoAlunoRelVOs.add(montarDados(dadosSQL));
        }
        return relacaoEnderecoAlunoRelVOs;
    }

    public List<RelacaoEnderecoAlunoRelVO> criarObjetoLivroRegistro(TurmaVO turma, String ano, String semestre, Integer codigoUnidadeEnsino) throws Exception {
        validarDados(turma, ano, semestre);
        List<RelacaoEnderecoAlunoRelVO> relacaoEnderecoAlunoRelVOs = new ArrayList<RelacaoEnderecoAlunoRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizadaLivroRegistro(turma, ano, semestre, codigoUnidadeEnsino);
        Integer quant = 0;
        while (dadosSQL.next()) {
            quant++;
            relacaoEnderecoAlunoRelVOs.add(montarDadosLivroRegistro(dadosSQL, quant));
        }
        return relacaoEnderecoAlunoRelVOs;
    }

    private RelacaoEnderecoAlunoRelVO montarDados(SqlRowSet dadosSQL) {
        RelacaoEnderecoAlunoRelVO relacaoEnderecoAlunoRelVO = new RelacaoEnderecoAlunoRelVO();
        relacaoEnderecoAlunoRelVO.setTurma(dadosSQL.getString("identificadorturma"));
        relacaoEnderecoAlunoRelVO.getMatricula().setMatricula(dadosSQL.getString("matricula"));
        relacaoEnderecoAlunoRelVO.getMatricula().getAluno().setNome(dadosSQL.getString("nome"));
        relacaoEnderecoAlunoRelVO.getMatricula().getAluno().setEndereco(dadosSQL.getString("endereco"));
        relacaoEnderecoAlunoRelVO.getMatricula().getAluno().setNumero(dadosSQL.getString("numero"));
        relacaoEnderecoAlunoRelVO.getMatricula().getAluno().setSetor(dadosSQL.getString("setor"));
        relacaoEnderecoAlunoRelVO.getMatricula().getAluno().getCidade().setNome(dadosSQL.getString("endCidade"));
        relacaoEnderecoAlunoRelVO.getMatricula().getAluno().getCidade().getEstado().setSigla(dadosSQL.getString("endEstado"));
        if(dadosSQL.getString("cep") != null && !dadosSQL.getString("cep").equals("")){
            relacaoEnderecoAlunoRelVO.getMatricula().getAluno().setCEP("CEP: ");
        }
        relacaoEnderecoAlunoRelVO.getMatricula().getAluno().setCEP(relacaoEnderecoAlunoRelVO.getMatricula().getAluno().getCEP()+dadosSQL.getString("cep"));
        relacaoEnderecoAlunoRelVO.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidEnsino"));

        return relacaoEnderecoAlunoRelVO;
    }

    private RelacaoEnderecoAlunoRelVO montarDadosLivroRegistro(SqlRowSet dadosSQL, Integer quant) {
        RelacaoEnderecoAlunoRelVO relacaoEnderecoAlunoRelVO = new RelacaoEnderecoAlunoRelVO();
        relacaoEnderecoAlunoRelVO.setTurma(dadosSQL.getString("identificadorturma"));
        relacaoEnderecoAlunoRelVO.getMatricula().setMatricula(dadosSQL.getString("matricula"));
        relacaoEnderecoAlunoRelVO.getMatricula().getAluno().setNome(dadosSQL.getString("nome"));
        relacaoEnderecoAlunoRelVO.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidEnsino"));
        relacaoEnderecoAlunoRelVO.getMatricula().getCurso().setNome(dadosSQL.getString("curso"));
        relacaoEnderecoAlunoRelVO.setNumero(quant);
        return relacaoEnderecoAlunoRelVO;
    }

    public SqlRowSet executarConsultaParametrizada(TurmaVO turma, String ano, String semestre) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append(" SELECT DISTINCT turma.identificadorturma, matricula.matricula, pessoa.nome, pessoa.endereco, pessoa.numero, ");
        selectStr.append(" pessoa.setor, cidade.nome AS endCidade, estado.sigla AS endEstado, pessoa.cep, unidadeEnsino.nome AS unidEnsino ");
        selectStr.append(" from matricula ");
        selectStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula");
        selectStr.append(" INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turma ");
        selectStr.append(" INNER JOIN unidadeEnsino ON unidadeensino.codigo = matricula.unidadeensino ");
        selectStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
        selectStr.append(" INNER JOIN cidade ON cidade.codigo = pessoa.cidade ");
        selectStr.append(" INNER JOIN estado ON estado.codigo = cidade.estado ");
        selectStr.append(" WHERE matriculaperiodoturmadisciplina.turma = ").append(turma.getCodigo());
        selectStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
        selectStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
        selectStr.append(" ORDER BY pessoa.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }

    public SqlRowSet executarConsultaParametrizadaLivroRegistro(TurmaVO turma, String ano, String semestre, Integer codigoUnidadeEnsino) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append(" SELECT DISTINCT turma.identificadorturma, matricula.matricula, pessoa.nome, ");
        selectStr.append(" unidadeEnsino.nome AS unidEnsino, curso.nome as curso ");
        selectStr.append(" from matricula ");
        selectStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula");
        selectStr.append(" INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turma ");
        selectStr.append(" INNER JOIN unidadeEnsino ON unidadeensino.codigo = matricula.unidadeensino ");
        selectStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
        selectStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
        selectStr.append(" WHERE matriculaperiodoturmadisciplina.turma = ").append(turma.getCodigo());
        if (!ano.equals("")) {
            selectStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
        }
        if (!semestre.equals("")) {
            selectStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
        }
        if (Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
        	selectStr.append(" AND unidadeEnsino.codigo = ").append(codigoUnidadeEnsino);
        }
        selectStr.append(" AND (matricula.situacao = 'AT' or matricula.situacao = 'FO')");
        selectStr.append(" ORDER BY pessoa.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("RelacaoEnderecoAlunoRel");
    }

    public static String getDesignIReportRelatorioLivroRegistro() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeLivroRegistro() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorioLivroRegistro() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidadeLivroRegistro() {
        return ("LivroRegistroRel");
    }
}
