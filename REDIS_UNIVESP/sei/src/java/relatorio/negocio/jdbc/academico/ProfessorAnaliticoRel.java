package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.DisciplinasInteresse;
import negocio.facade.jdbc.administrativo.FormacaoAcademica;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO;
import relatorio.negocio.comuns.academico.ProfessoresAnaliticoPorUnidadeCursoTurmaRelVOs;
import relatorio.negocio.interfaces.academico.ProfessorAnaliticoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ProfessorAnaliticoRel extends SuperRelatorio implements ProfessorAnaliticoRelInterfaceFacade {

    public ProfessorAnaliticoRel() {
    }

    public static void validarDados(UnidadeEnsinoVO unidadeEnsino, UnidadeEnsinoCursoVO unidadeEnsinoCurso, DisciplinaVO disciplina) throws ConsistirException {
    }

    /*
     * (non-Javadoc)
     *
     * @seerelatorio.negocio.jdbc.academico.ProfessorRelInterfaceFacade#criarObjeto(negocio.comuns.
     * administrativo.UnidadeEnsinoVO, negocio.comuns.academico.CursoVO, negocio.comuns.academico.TurmaVO,
     * java.lang.String, java.lang.String)
     */
    public List<ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO> criarObjeto(
            AreaConhecimentoVO areaConhecimentoVO, DisciplinaVO disciplinaInteresseVO, String situacaoProfessor,
            String escolaridade, PessoaVO professorVO, UsuarioVO usuarioVO, String ordemRelatorio, Boolean imprimirDadosComplementares) throws Exception {
        List<ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO> listaRelatorio = new ArrayList<ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO>(0);
        ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO professoresAnaliticoPorUnidadeCursoTurmaRelVO = new ProfessoresAnaliticoPorUnidadeCursoTurmaRelVO();
        professoresAnaliticoPorUnidadeCursoTurmaRelVO.setProfessoresVOs(consultarProfessores(
                areaConhecimentoVO, disciplinaInteresseVO, situacaoProfessor,
                escolaridade, professorVO, usuarioVO, ordemRelatorio, imprimirDadosComplementares));
        if (!areaConhecimentoVO.getCodigo().equals(0)) {
            professoresAnaliticoPorUnidadeCursoTurmaRelVO.setAreaConhecimento(areaConhecimentoVO.getNome());
        } else {
            professoresAnaliticoPorUnidadeCursoTurmaRelVO.setAreaConhecimento("Não Informada!");
        }
        professoresAnaliticoPorUnidadeCursoTurmaRelVO.setOrdemRelatorio(ordemRelatorio);
        professoresAnaliticoPorUnidadeCursoTurmaRelVO.setImprimirDadosComplementares(imprimirDadosComplementares);
        professoresAnaliticoPorUnidadeCursoTurmaRelVO.setDisciplina(disciplinaInteresseVO.getNome());
        professoresAnaliticoPorUnidadeCursoTurmaRelVO.setFiltroSituacao(situacaoProfessor);
        if (!professorVO.getCodigo().equals(0)) {
            professoresAnaliticoPorUnidadeCursoTurmaRelVO.setProfessor(professorVO.getCodigo() + " - " + professorVO.getNome());
        } else {
            professoresAnaliticoPorUnidadeCursoTurmaRelVO.setProfessor("Professor não Informado!");
        }
        professoresAnaliticoPorUnidadeCursoTurmaRelVO.setQtdeProfessores(verificarQtdeProfessoresLista(professoresAnaliticoPorUnidadeCursoTurmaRelVO.getProfessoresVOs()));
        listaRelatorio.add(professoresAnaliticoPorUnidadeCursoTurmaRelVO);
        return listaRelatorio;
    }

    private Integer verificarQtdeProfessoresLista(List<ProfessoresAnaliticoPorUnidadeCursoTurmaRelVOs> professoresVOs) {
        String matricula = "";
        Integer qtdeAlunoTemp = 0;
        for (ProfessoresAnaliticoPorUnidadeCursoTurmaRelVOs professor : professoresVOs) {
            if (!matricula.equals(professor.getMatricula())) {
                matricula = professor.getMatricula();
                qtdeAlunoTemp = qtdeAlunoTemp + 1;
            }
        }
        return qtdeAlunoTemp;
    }

    private String consultarNomeUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) throws Exception {
        if (unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() != 0) {
            unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
            return unidadeEnsino.getNome();
        } else {
            return "Unidade de Ensino não Informada!";
        }
    }

    private String consultarNomeCurso(CursoVO curso) throws Exception {
        curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(curso.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, null);
        return curso.getNome();
    }

    private List<ProfessoresAnaliticoPorUnidadeCursoTurmaRelVOs> consultarProfessores(
            AreaConhecimentoVO areaConhecimentoVO, DisciplinaVO disciplinaInteresseVO,
            String situacaoProfessor,
            String escolaridade, PessoaVO professorVO, UsuarioVO usuarioVO,
            String ordemRelatorio, Boolean imprimirDadosComplementares) throws Exception {
        List lista = new ArrayList(0);
        lista = consultarPorCursoTurmaDisciplinaAnoSemestreSituacao(areaConhecimentoVO.getCodigo(),
                disciplinaInteresseVO.getCodigo(), situacaoProfessor, escolaridade,
                professorVO.getCodigo(), usuarioVO, ordemRelatorio);
        if (lista.isEmpty()) {
            throw new Exception("Não há professores cadastrados nas condições propostas acima.");
        }
        return lista;
    }

    public List consultarPorCursoTurmaDisciplinaAnoSemestreSituacao(Integer areaConhecimento,
            Integer disciplinaInteresse, String situacaoProfessor, String escolaridade,
            Integer professor, UsuarioVO usuario, String ordemRelatorio) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT pessoa.codigo AS \"pessoa.codigo\", funcionario.matricula AS \"funcionario.matricula\", ");
        sqlStr.append("pessoa.nome AS \"pessoa.nome\", CASE WHEN pessoa.ativo = true THEN 'Ativo' ELSE 'Inativo' END AS situacao, ");
        sqlStr.append("pessoa.telefoneRes AS \"pessoa.telefoneRes\", ");
        sqlStr.append("pessoa.celular AS \"pessoa.celular\", pessoa.email AS \"pessoa.email\", pessoa.email2 AS \"pessoa.email2\", ");
        sqlStr.append("pessoa.funcionario AS \"pessoa.funcionario\", pessoa.professor AS \"pessoa.professor\", pessoa.sexo AS \"pessoa.sexo\", ");
        sqlStr.append("pessoa.rg AS \"pessoa.rg\", pessoa.estadoemissaorg AS \"pessoa.estadoemissaorg\", pessoa.orgaoemissor AS \"pessoa.orgaoemissor\",   ");
        sqlStr.append("pessoa.cidade AS \"pessoa.cidade\", pessoa.complemento AS \"pessoa.complemento\", pessoa.cep AS \"pessoa.cep\",   ");
        sqlStr.append("pessoa.cpf AS \"pessoa.cpf\", pessoa.numero AS \"pessoa.numero\", pessoa.setor AS \"pessoa.setor\", pessoa.endereco AS \"pessoa.endereco\", pessoa.datanasc AS \"pessoa.datanasc\" ");
        sqlStr.append("FROM pessoa ");
        sqlStr.append("INNER JOIN funcionario ON funcionario.pessoa = pessoa.codigo ");
        sqlStr.append("LEFT JOIN professortitulardisciplinaturma ON professortitulardisciplinaturma.professor = pessoa.codigo ");
        sqlStr.append("LEFT JOIN disciplinasinteresse on pessoa.codigo = disciplinasinteresse.professor ");
        sqlStr.append("WHERE pessoa.professor = true ");
        if (situacaoProfessor.equals("AT")) {
            sqlStr.append(" AND pessoa.ativo = true");
        } else if (situacaoProfessor.equals("IN")) {
            sqlStr.append(" AND pessoa.ativo = false");
        }
        if (!professor.equals(0)) {
            sqlStr.append(" AND pessoa.codigo = ").append(professor);
        }
        if (!escolaridade.equals("")) {
            sqlStr.append(" AND pessoa.codigo IN (select distinct pessoa from formacaoacademica  where escolaridade = '").append(escolaridade).append("'").append(")");
        }
        if (!areaConhecimento.equals(0)) {
            sqlStr.append(" AND pessoa.codigo IN (select distinct pessoa from formacaoacademica  where areaConhecimento = '").append(areaConhecimento).append("'").append(")");
        }
        if (!disciplinaInteresse.equals(0)) {
            sqlStr.append(" AND ((disciplinasinteresse.disciplina = ").append(disciplinaInteresse).
                    append(") OR (professortitulardisciplinaturma.disciplina = ").append(disciplinaInteresse).append("))");
        }
        //if (ordemRelatorio.equals("Área Conhecimento")) {
        sqlStr.append(" ORDER BY Pessoa.nome");
        //} else if (ordemRelatorio.equals("Disciplina Interesse")) {
        //    sqlStr.append(" ORDER BY Pessoa.nome");
        //} else {
        //    sqlStr.append(" ORDER BY Pessoa.nome");
        //}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public static List<ProfessoresAnaliticoPorUnidadeCursoTurmaRelVOs> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList<MatriculaVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    public static ProfessoresAnaliticoPorUnidadeCursoTurmaRelVOs montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        ProfessoresAnaliticoPorUnidadeCursoTurmaRelVOs obj = new ProfessoresAnaliticoPorUnidadeCursoTurmaRelVOs();
        obj.setCodigo(dadosSQL.getInt("pessoa.codigo"));
        obj.setMatricula(dadosSQL.getString("funcionario.matricula"));
        obj.setNomeProfessor(dadosSQL.getString("pessoa.nome"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setTelefoneRes(dadosSQL.getString("pessoa.telefoneRes"));
        obj.setCelular(dadosSQL.getString("pessoa.celular"));
        obj.setEmail(dadosSQL.getString("pessoa.email"));
        obj.setEmail2(dadosSQL.getString("pessoa.email2"));
        obj.setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
        obj.setProfessor(dadosSQL.getBoolean("pessoa.professor"));
        obj.setSexo(dadosSQL.getString("pessoa.sexo"));
        obj.setRg(dadosSQL.getString("pessoa.rg"));
        obj.setCpf(dadosSQL.getString("pessoa.cpf"));
        obj.setEstadoEmissorRg(dadosSQL.getString("pessoa.estadoemissaorg"));
        obj.setOrgaoEmissorRg(dadosSQL.getString("pessoa.orgaoemissor"));
        try {
            CidadeVO cidadeVO = new CidadeVO();
            cidadeVO.setCodigo(dadosSQL.getInt("pessoa.cidade"));
            cidadeVO = getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(cidadeVO.getCodigo(), false, usuario);
            obj.setCidade(cidadeVO.getNome());
            obj.setEstado(cidadeVO.getEstado().getSigla());
        } catch (Exception e) {
        }
        obj.setComplemento(dadosSQL.getString("pessoa.complemento"));
        obj.setCep(dadosSQL.getString("pessoa.cep"));
        obj.setNumero(dadosSQL.getString("pessoa.numero"));
        obj.setBairro(dadosSQL.getString("pessoa.setor"));
        obj.setEndereco(dadosSQL.getString("pessoa.endereco"));
        obj.setDataNascimento(dadosSQL.getString("pessoa.datanasc"));


        obj.setFormacaoAcademicaVOs(FormacaoAcademica.consultarFormacaoAcademicas(obj.getCodigo(), false, obj.getFuncionario(), usuario));
        obj.setDisciplinasInteresseVOs(DisciplinasInteresse.consultarDisciplinasInteresses(obj.getCodigo(), false, usuario));
        obj.setTitulacao(obj.consultarMaiorNivelEscolaridade());
        obj.setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarFiliacaos(obj.getCodigo(), false, usuario));
        Iterator i = obj.getFiliacaoVOs().iterator();
        while (i.hasNext()) {
            try {
                FiliacaoVO filiacao = (FiliacaoVO) i.next();
                String tipo = filiacao.getTipo();
                if (tipo.equals("PA")) { // pai
                    Integer codigoPrm = filiacao.getPais().getCodigo();
                    PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(codigoPrm, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario);
                    obj.setNomePai(pessoa.getNome());
                }
                if (tipo.equals("MA")) { // mae
                    Integer codigoPrm = filiacao.getPais().getCodigo();
                    PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(codigoPrm, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario);
                    obj.setNomeMae(pessoa.getNome());
                }
            } catch (Exception e) {
            }
        }
        return obj;
    }

    public static String getDesignIReportRelatorioModelo3() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "Modelo3.jrxml");
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("ProfessorAnaliticoRel");
    }
}
