package relatorio.negocio.jdbc.bancocurriculum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.basico.PessoaVO;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.bancocurriculum.AlunosCandidatadosVagaDadosVagaRelVO;
import relatorio.negocio.comuns.bancocurriculum.AlunosCandidatadosVagaRelVO;
import relatorio.negocio.interfaces.bancocurriculum.AlunosCandidatadosVagaRelInterfaceFacade;

import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class AlunosCandidatadosVagaRel extends SuperRelatorio implements AlunosCandidatadosVagaRelInterfaceFacade {

    public AlunosCandidatadosVagaRel() {
    }

    public List<AlunosCandidatadosVagaRelVO> criarObjeto(PessoaVO pessoa, CursoVO curso, TurmaVO turma, String situacaoVaga) throws Exception {
        List<AlunosCandidatadosVagaRelVO> listaObjetos = new ArrayList(0);
        listaObjetos.clear();
        SqlRowSet dadosSQL = executarConsultaParametrizada(pessoa, curso, turma, situacaoVaga);
        while (dadosSQL.next()) {
            montarDados(listaObjetos, dadosSQL);
        }
        return listaObjetos;
    }

    private void montarDados(List<AlunosCandidatadosVagaRelVO> listaObjetos, SqlRowSet dadosSQL) {
        AlunosCandidatadosVagaRelVO alunosCadidatadosVagaRelVO = consultarAlunosCadidatadosVagaRelVO(listaObjetos, dadosSQL.getInt("codigoPessoa"));
        if (alunosCadidatadosVagaRelVO.getCodigoPessoa().equals(0)) {
            alunosCadidatadosVagaRelVO.setCodigoPessoa(dadosSQL.getInt("codigoPessoa"));
            alunosCadidatadosVagaRelVO.setAluno(dadosSQL.getString("aluno"));
            alunosCadidatadosVagaRelVO.setCurso(dadosSQL.getString("curso"));
            alunosCadidatadosVagaRelVO.setCidadeAluno(dadosSQL.getString("cidadeAluno"));
            alunosCadidatadosVagaRelVO.setIdentificadorTurma(dadosSQL.getString("turma"));
        }
        AlunosCandidatadosVagaDadosVagaRelVO alunosCadidatadosVagaDadosVagaRelVO = new AlunosCandidatadosVagaDadosVagaRelVO();
        alunosCadidatadosVagaDadosVagaRelVO.setAreaProfissional(dadosSQL.getString("areaProfissional"));
        alunosCadidatadosVagaDadosVagaRelVO.setCidadeEmpresa(dadosSQL.getString("cidadeEmpresa"));
        alunosCadidatadosVagaDadosVagaRelVO.setEmpresa(dadosSQL.getString("empresa"));
        alunosCadidatadosVagaDadosVagaRelVO.setCargo(dadosSQL.getString("cargo"));
        alunosCadidatadosVagaDadosVagaRelVO.setSituacao(dadosSQL.getString("situacao"));
        alunosCadidatadosVagaRelVO.getListaAlunosCadidatadosVagaDadosVagaRelVO().add(alunosCadidatadosVagaDadosVagaRelVO);
        adicionarAlunosCadidatadosVagaRelVO(listaObjetos, alunosCadidatadosVagaRelVO);
    }

    public AlunosCandidatadosVagaRelVO consultarAlunosCadidatadosVagaRelVO(List<AlunosCandidatadosVagaRelVO> listaObjetos, Integer codigoPessoa) {
        for (AlunosCandidatadosVagaRelVO objExistente : listaObjetos) {
            if (codigoPessoa.equals(objExistente.getCodigoPessoa())) {
                return objExistente;
            }
        }
        return new AlunosCandidatadosVagaRelVO();
    }

    public void adicionarAlunosCadidatadosVagaRelVO(List<AlunosCandidatadosVagaRelVO> listaObjetos, AlunosCandidatadosVagaRelVO alunosCadidatadosVagaRelVO) {
        int index = 0;
        for (AlunosCandidatadosVagaRelVO objExistente : listaObjetos) {
            if (alunosCadidatadosVagaRelVO.getCodigoPessoa().equals(objExistente.getCodigoPessoa())) {
                listaObjetos.set(index, alunosCadidatadosVagaRelVO);
                return;
            }
            index++;
        }
        listaObjetos.add(alunosCadidatadosVagaRelVO);
    }

    public SqlRowSet executarConsultaParametrizada(PessoaVO pessoa, CursoVO curso, TurmaVO turma, String situacaoVaga) throws Exception {
        StringBuilder selectStr = new StringBuilder();
        selectStr.append(" SELECT distinct pessoa.codigo AS codigoPessoa, pessoa.nome AS aluno, curso.nome AS curso, turma.identificadorturma AS turma, areaprofissional.descricaoareaprofissional AS areaProfissional, ");
        selectStr.append(" cidadeAluno.nome AS cidadeAluno, cidadeEmpresa.nome AS cidadeEmpresa, vagas.cargo AS cargo, candidatosvagas.situacaoreferentevaga AS situacao, parceiro.nome AS empresa  ");
        selectStr.append(" from pessoa ");
        selectStr.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
        selectStr.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
        selectStr.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
        selectStr.append(" inner join curso on matricula.curso = curso.codigo ");
        selectStr.append(" left join cidade AS cidadeAluno on pessoa.cidade = cidadeAluno.codigo ");
        selectStr.append(" inner join candidatosvagas on candidatosvagas.pessoa = pessoa.codigo ");
        selectStr.append(" left join vagas on  candidatosvagas.vaga = vagas.codigo ");
        selectStr.append(" left join areaprofissional on vagas.areaprofissional = areaprofissional.codigo ");
        selectStr.append(" left join parceiro on vagas.parceiro = parceiro.codigo ");
        selectStr.append(" left join cidade AS cidadeEmpresa on parceiro.cidade = cidadeempresa.codigo ");
        selectStr.append(" where 1 = 1 ");
        if (pessoa.getCodigo() != 0) {
            selectStr.append(" AND pessoa.codigo = ");
            selectStr.append(pessoa.getCodigo());
        }
        if (curso.getCodigo() != 0) {
            selectStr.append(" AND curso.codigo = ");
            selectStr.append(curso.getCodigo());
        }
        if (turma.getCodigo() != 0) {
            selectStr.append(" AND turma.codigo = ");
            selectStr.append(turma.getCodigo());
        }
        if (!situacaoVaga.equals("")) {
            selectStr.append(" AND vagas.situacao = '");
            selectStr.append(situacaoVaga);
            selectStr.append("'");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        return tabelaResultado;
    }

    public String designIReportRelatorioExcel() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator + getIdEntidadeExcel() + ".jrxml");
    }

    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator + getIdEntidade() + ".jrxml");
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator);
    }

    public static String getIdEntidade() {
        return "AlunosCandidatadosVagaRel";
    }

    public static String getIdEntidadeExcel() {
        return "AlunosCandidatadosVagaRelExcel";
    }
}
