package relatorio.negocio.jdbc.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.administrativo.SenhaAlunoProfessorVO;
import relatorio.negocio.interfaces.administrativo.SenhaAlunoProfessorRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class SenhaAlunoProfessorRel extends SuperRelatorio implements SenhaAlunoProfessorRelInterfaceFacade {

    public SenhaAlunoProfessorRel() throws Exception {
    }

    public List criarObjetos(Integer campoConsultaFiltro, SenhaAlunoProfessorVO senhaAlunoProfessorVO, List listaSenhaAlunoProfessorVO, String tipoPessoa, String caminhoImagem, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Date dataInicio, Date dataFim, String ano, String semestre, String periodicidadeCurso, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
        boolean msgErro = false;
        listaSenhaAlunoProfessorVO.clear();
        if (tipoPessoa.equals("aluno")) {
        	validarDadosPeriodicidade(periodicidadeCurso, ano, semestre, dataInicio, dataFim, unidadeEnsino);
        }
        switch (campoConsultaFiltro) {
            case 1:
                if (unidadeEnsino == 0 || unidadeEnsino == null) {
                    msgErro = true;
                }
                break;
            case 2:
                if ((unidadeEnsino == 0 || unidadeEnsino == null) || (curso == 0 || curso == null)) {
                    msgErro = true;
                }
                break;
            case 3:
                if ((unidadeEnsino == 0 || unidadeEnsino == null) || (curso == 0 || curso == null) || (turno == 0 || turno == null)) {
                    msgErro = true;
                }
                break;
            case 4:
                if ((unidadeEnsino == 0 || unidadeEnsino == null) || (turma == 0 || turma == null)) {
                    msgErro = true;
                }
                break;
            default:
                if (senhaAlunoProfessorVO.getMatricula() != null && senhaAlunoProfessorVO.getMatricula().equals("")) {
                    msgErro = true;
                } else {
                    listaSenhaAlunoProfessorVO.add(senhaAlunoProfessorVO);
                }
                break;
        }
        if (msgErro) {
            throw new ConsistirException("Preencha todos os dados para gerar o relatório.");
        }
        if (listaSenhaAlunoProfessorVO.isEmpty()) {
            CursoVO cursoVO = new CursoVO();
            if (curso != null && curso != 0) {
                cursoVO = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(curso, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, null);
            }
            if (cursoVO.getNivelEducacionalPosGraduacao()) {
                listaSenhaAlunoProfessorVO = consultarPorTipoPessoaUnidadeEnsinoCursoTurnoTurma(tipoPessoa, caminhoImagem, unidadeEnsino, curso, turno, turma, "", "", dataInicio, dataFim, periodicidadeCurso, filtroRelatorioAcademicoVO);
            } else {
                listaSenhaAlunoProfessorVO = consultarPorTipoPessoaUnidadeEnsinoCursoTurnoTurma(tipoPessoa, caminhoImagem, unidadeEnsino, curso, turno, turma, ano, semestre, dataInicio, dataFim, periodicidadeCurso, filtroRelatorioAcademicoVO);
            }
        }
        return listaSenhaAlunoProfessorVO;
    }

    public List consultarPorTipoPessoaUnidadeEnsinoCursoTurnoTurma(String tipoPessoa, String caminhoImagem, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, String ano,
            String semestre, Date dataInicio, Date dataFim, String periodicidadeCurso, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        if (tipoPessoa.equals("aluno")) {
            sqlStr.append(" SELECT DISTINCT pessoa.nome AS nomepessoa, pessoa.cpf, matricula.matricula, unidadeensino.nome AS \"unidadeensino.nome\", unidadeensino.site AS site, usuario.username");
            sqlStr.append(" FROM Pessoa");
            sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo");
            sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino");
            sqlStr.append(" INNER JOIN curso on matricula.curso = curso.codigo");
            sqlStr.append(" INNER JOIN usuario ON usuario.pessoa = pessoa.codigo");
            sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaperiodo.matricula = matricula.matricula");
            
            if (turma != 0) {
                sqlStr.append(" WHERE matriculaperiodo.turma = " + turma + " AND pessoa.aluno = true");
	        } else {
	        	sqlStr.append(" WHERE pessoa.aluno = true");
	        }
            if (!ano.equals("")) {
            	sqlStr.append(" AND matriculaperiodo.ano = '" + ano + "' ");
            }
			if (!semestre.equals("")) {
				sqlStr.append(" AND matriculaperiodo.semestre = '" + semestre + "'");
			}
            if (turno != 0) {
                sqlStr.append(" AND matricula.turno = " + turno);
            }
            if (curso != 0) {
                sqlStr.append(" AND matricula.curso = " + curso);
            }
            if (unidadeEnsino != 0) {
                sqlStr.append(" AND matricula.unidadeensino = " + unidadeEnsino);
            }
            sqlStr.append(" AND ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "matriculaperiodo.data", false));
            sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
            if (curso.equals(0) && turma.equals(0) && !periodicidadeCurso.equals("")) {
            	sqlStr.append(" AND curso.periodicidade = '").append(periodicidadeCurso).append("' ");
            }
        }
        if (tipoPessoa.equals("professor")) {
            sqlStr.append(" SELECT pessoa.nome AS nomepessoa, pessoa.cpf, funcionario.matricula, unidadeensino.nome AS \"unidadeensino.nome\", unidadeensino.site AS site, usuario.username");
            sqlStr.append(" FROM Pessoa");
            sqlStr.append(" INNER JOIN usuario ON usuario.pessoa = pessoa.codigo");
            sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo");
            sqlStr.append(" INNER JOIN funcionariocargo ON funcionariocargo.funcionario = funcionario.codigo");
            sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = funcionariocargo.unidadeensino");
            sqlStr.append(" WHERE pessoa.professor = true AND pessoa.ativo = true");
            if (unidadeEnsino != 0) {
                sqlStr.append(" AND unidadeensino.codigo = " + unidadeEnsino);
            }
        }
        sqlStr.append(" ORDER BY nomepessoa");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, caminhoImagem, tipoPessoa));
    }

    public SenhaAlunoProfessorVO consultarPorNomeCpf(PessoaVO pessoaVO, String tipoPessoa, String caminhoImagem, Integer unidadeEnsino) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        if (tipoPessoa.equals("aluno")) {
            sqlStr.append(" SELECT pessoa.nome AS nomepessoa, pessoa.cpf, matricula.matricula AS matricula, unidadeensino.nome AS \"unidadeensino.nome\", unidadeensino.site AS site, usuario.username");
            sqlStr.append(" FROM Pessoa");
            sqlStr.append(" INNER JOIN matricula ON matricula.aluno = pessoa.codigo");
            sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino");
            sqlStr.append(" INNER JOIN usuario ON usuario.pessoa = pessoa.codigo");
            sqlStr.append(" WHERE pessoa.cpf = '" + pessoaVO.getCPF() + "' AND pessoa.nome = '" + pessoaVO.getNome() + "'");
            sqlStr.append(" AND pessoa.aluno = true");
        }
        if (tipoPessoa.equals("professor")) {
            sqlStr.append(" SELECT pessoa.nome AS nomepessoa, pessoa.cpf, funcionario.matricula, unidadeensino.nome AS \"unidadeensino.nome\", unidadeensino.site AS site, usuario.username");
            sqlStr.append(" FROM Pessoa");
            sqlStr.append(" INNER JOIN usuario ON usuario.pessoa = pessoa.codigo");
            sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo");
            sqlStr.append(" LEFT JOIN funcionariocargo ON funcionariocargo.funcionario = funcionario.codigo");
            sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensino.codigo = funcionariocargo.unidadeensino");
            sqlStr.append(" WHERE pessoa.cpf = '" + pessoaVO.getCPF() + "' AND pessoa.nome = '" + pessoaVO.getNome() + "'");
            sqlStr.append(" AND pessoa.professor = true");
        }
        if (unidadeEnsino != 0) {
            sqlStr.append(" AND unidadeEnsino.codigo = " + unidadeEnsino + " ;");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, caminhoImagem, tipoPessoa));
    }

    public List montarDadosConsulta(SqlRowSet tabelaResultado, String caminhoImagem, String tipoPessoa) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, caminhoImagem, tipoPessoa));
        }
        return vetResultado;
    }

    public static SenhaAlunoProfessorVO montarDados(SqlRowSet dadosSQL, String caminhoImagem, String tipoPessoa) throws Exception {
        SenhaAlunoProfessorVO obj = new SenhaAlunoProfessorVO();
        if (tipoPessoa.equals("aluno")) {
            obj.setNome("Caro(a) Aluno(a),\n" + dadosSQL.getString("nomepessoa"));
        } else {
            obj.setNome("Caro(a) Professor(a),\n" + dadosSQL.getString("nomepessoa"));
        }
        obj.setData(Uteis.getData(new Date(), "dd 'de' MMMM 'de' yyyy"));
        obj.setCpf(dadosSQL.getString("CPF"));
        obj.setMatricula(dadosSQL.getString("matricula"));
        obj.setUsername(dadosSQL.getString("username"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
        obj.getUnidadeEnsino().setSite(dadosSQL.getString("site"));
        obj.setCaminhoImagem(caminhoImagem);
        return obj;
    }

    public String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + getIdEntidade() + ".jrxml");
    }
    
    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo");
    }

    public static String getIdEntidade() {
        return ("SenhaAlunoProfessorRel");
    }
    
    private void validarDadosPeriodicidade(String periodicidade, String ano, String semestre, Date dataInicio, Date dataFim, Integer unidadeEnsino) throws Exception{
    	if (periodicidade.equals("") && unidadeEnsino != 0) {
    		throw new ConsistirException("O campo (PERIODICIDADE) é obrigatório.");
    	}
    	if ((periodicidade.equals(PeriodicidadeEnum.ANUAL.getValor()) || periodicidade.equals(PeriodicidadeEnum.SEMESTRAL.getValor())) && ano.equals("")) {
    		throw new ConsistirException("O campo (ANO) é obrigatório.");
        } else if (periodicidade.equals(PeriodicidadeEnum.SEMESTRAL.getValor()) && semestre.equals("")) {
        	throw new ConsistirException("O campo (SEMESTRE) é obrigatório.");
        } else if (periodicidade.equals(PeriodicidadeEnum.INTEGRAL.getValor())) {
        	if (dataInicio == null) {
        		throw new ConsistirException("O campo (DATA INÍCIO MATRÍCULA) é obrigatório.");
        	}
        	if (dataFim == null) {
        		throw new ConsistirException("O campo (DATA FIM MATRÍCULA) é obrigatório.");
        	}
        }
    }
}
