package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.ProfessorMinistrouAulaTurmaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ProfessorMinistrouAulaTurmaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ProfessorMinistrouAulaTurmaVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ProfessorMinistrouAulaTurmaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ProfessorMinistrouAulaTurmaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ProfessorMinistrouAulaTurma extends ControleAcesso implements ProfessorMinistrouAulaTurmaInterfaceFacade {

    protected static String idEntidade;
    //private Hashtable frequenciaAulas;

    public ProfessorMinistrouAulaTurma() throws Exception {
        super();
        setIdEntidade("ProfessorMinistrouAulaTurma");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ProfessorMinistrouAulaTurmaVO</code>.
     */
    public ProfessorMinistrouAulaTurmaVO novo() throws Exception {
        ProfessorMinistrouAulaTurma.incluir(getIdEntidade());
        ProfessorMinistrouAulaTurmaVO obj = new ProfessorMinistrouAulaTurmaVO();
        return obj;
    }

    public static void verificaPermissaoAlterarCargaHoraria() throws Exception {
//        verificarPermissaoUsuarioFuncionalidade("RegistroAulaAlterarCargaHoraria");
    }

//    public void verificarSeExisteProgramacaoAulaParaEssaTurma_e_Disciplina(ProfessorMinistrouAulaTurmaVO obj) throws Exception {
//        if (existeRegistroAula(obj)) {
//            throw new Exception("Já existe aula registrada nesta data, turma e horário.");
//        }
//    }
    public void validarConsultaDoUsuario(UsuarioVO usuario) throws Exception {
        ProfessorMinistrouAulaTurma.consultar(getIdEntidade(), true, usuario);
    }

    public void incluirProfessorMinistrouAulaTurmaEspecificaNaoAgrupada(TurmaVO turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataAula, UsuarioVO usuario) throws Exception {
        ProfessorMinistrouAulaTurmaVO p = new ProfessorMinistrouAulaTurmaVO();
        p.setTurma(turma);
        p.getDisciplina().setCodigo(disciplina);
        p.getProfessor().setCodigo(professor);
        p.setAno(ano);
        p.setSemestre(semestre);
        p.setData(dataAula);
        p.setTitular(true);
        p.getResponsavelRegistro().setCodigo(usuario.getCodigo());
        List l = this.consultarPorProfessorTurmaDisciplinaAnoSemestre(professor, turma.getCodigo(), disciplina, ano, semestre, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        if (l.isEmpty()) {
            this.incluir(p, usuario);
        } else {
            ProfessorMinistrouAulaTurmaVO prof = (ProfessorMinistrouAulaTurmaVO) l.get(0);
            p.setCodigo(prof.getCodigo());
            this.alterar(p, usuario);
        }
        p.setTitular(Boolean.FALSE);
        alterarTodosProfessores(p);
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ProfessorMinistrouAulaTurmaVO</code>. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ProfessorMinistrouAulaTurmaVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluirProfessorMinistrouAulaTurma(TurmaVO turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataAula, UsuarioVO usuario) throws Exception {
        if (turma.getTurmaAgrupada()) {
            getFacadeFactory().getTurmaFacade().carregarDados(turma, NivelMontarDados.TODOS, usuario);
            for (TurmaAgrupadaVO turmaAgrupada : turma.getTurmaAgrupadaVOs()) {
                incluirProfessorMinistrouAulaTurmaEspecificaNaoAgrupada(turmaAgrupada.getTurma(), disciplina, professor, ano, semestre, dataAula, usuario);
            }
        }
        incluirProfessorMinistrouAulaTurmaEspecificaNaoAgrupada(turma, disciplina, professor, ano, semestre, dataAula, usuario);
    }

    public void alterarProfessorMinistrouAulaTurma(TurmaVO turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataAula, UsuarioVO usuario) throws Exception {
        if (turma.getTurmaAgrupada()) {
            getFacadeFactory().getTurmaFacade().carregarDados(turma, NivelMontarDados.TODOS, usuario);
            for (TurmaAgrupadaVO turmaAgrupada : turma.getTurmaAgrupadaVOs()) {
                incluirProfessorMinistrouAulaTurmaEspecificaNaoAgrupada(turmaAgrupada.getTurma(), disciplina, professor, ano, semestre, dataAula, usuario);
            }
        }
        incluirProfessorMinistrouAulaTurmaEspecificaNaoAgrupada(turma, disciplina, professor, ano, semestre, dataAula, usuario);
//        ProfessorMinistrouAulaTurmaVO p = new ProfessorMinistrouAulaTurmaVO();
//        p.setTurma(turma);
//        p.getDisciplina().setCodigo(disciplina);
//        p.getProfessor().setCodigo(professor);
//        p.setAno(ano);
//        p.setSemestre(semestre);
//        p.setData(dataAula);
//        p.setTitular(true);
//        p.getResponsavelRegistro().setCodigo(getUsuarioLogado().getCodigo());
//        List l = this.consultarPorProfessorTurmaDisciplinaAnoSemestre(professor, turma.getCodigo(), disciplina, ano, semestre, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
//        if (l.isEmpty()) {
//            this.incluir(p);
//        } else {
//            ProfessorMinistrouAulaTurmaVO prof = (ProfessorMinistrouAulaTurmaVO)l.get(0);
//            p.setCodigo(prof.getCodigo());
//            this.alterar(p);
//        }
//        p.setTitular(Boolean.FALSE);
//        alterarTodosProfessores(p);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ProfessorMinistrouAulaTurmaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ProfessorMinistrouAulaTurmaVO.validarDados(obj);
            //verificarSeExisteProgramacaoAulaParaEssaTurma_e_Disciplina(obj);
            //ProfessorMinistrouAulaTurma.incluir(getIdEntidade());

            final String sql = "INSERT INTO ProfessorMinistrouAulaTurma( data, turma, responsavelRegistro, disciplina, professor, semestre, ano, titular ) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setInt(2, obj.getTurma().getCodigo().intValue());
                    sqlInserir.setInt(3, obj.getResponsavelRegistro().getCodigo().intValue());
                    sqlInserir.setInt(4, obj.getDisciplina().getCodigo().intValue());
                    sqlInserir.setInt(5, obj.getProfessor().getCodigo().intValue());
                    sqlInserir.setString(6, obj.getSemestre());
                    sqlInserir.setString(7, obj.getAno());
                    sqlInserir.setBoolean(8, obj.getTitular());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProfessorMinistrouAulaTurmaVO</code>. Sempre
     * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ProfessorMinistrouAulaTurmaVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ProfessorMinistrouAulaTurmaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ProfessorMinistrouAulaTurmaVO.validarDados(obj);
            //verificarSeExisteProgramacaoAulaParaEssaTurma_e_Disciplina(obj);
            //ProfessorMinistrouAulaTurma.alterar(getIdEntidade());

            final String sql = "UPDATE ProfessorMinistrouAulaTurma set data=?, turma=?, responsavelRegistro=?, disciplina=?, professor = ?, "
                    + "semestre=?, ano=?, titular=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setInt(2, obj.getTurma().getCodigo().intValue());
                    sqlAlterar.setInt(3, obj.getResponsavelRegistro().getCodigo().intValue());
                    sqlAlterar.setInt(4, obj.getDisciplina().getCodigo().intValue());
                    sqlAlterar.setInt(5, obj.getProfessor().getCodigo().intValue());
                    sqlAlterar.setString(6, obj.getSemestre());
                    sqlAlterar.setString(7, obj.getAno());
                    sqlAlterar.setBoolean(8, obj.getTitular());
                    sqlAlterar.setInt(9, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarListaProfessoresMinistrouAulaTurma(List professoresMinistrouAulaTurma) throws Exception {
        Iterator i = professoresMinistrouAulaTurma.iterator();
        validarSeExisteApenasUmProfessorComoTitular(professoresMinistrouAulaTurma);
        while (i.hasNext()) {
            ProfessorMinistrouAulaTurmaVO p = (ProfessorMinistrouAulaTurmaVO) i.next();
            alterarTodosProfessoresTitular(p);
        }
    }

    public void validarSeExisteApenasUmProfessorComoTitular(List professoresMinistrouAulaTurma) throws Exception {
        Iterator i = professoresMinistrouAulaTurma.iterator();
        int cont = 0;
        while (i.hasNext()) {
            ProfessorMinistrouAulaTurmaVO p = (ProfessorMinistrouAulaTurmaVO) i.next();
            if (p.getTitular().booleanValue()) {
                cont++;
            }
        }
        if (cont >= 2) {
            throw new Exception("Deve Existir Apenas um Professor Selecionado como Titular!");
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarTodosProfessores(final ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO) throws Exception {
        try {
            String sqlSTR = "UPDATE ProfessorMinistrouAulaTurma set titular=? WHERE professorMinistrouAulaTurma.turma = ? and professorMinistrouAulaTurma.disciplina = ? and professorMinistrouAulaTurma.professor != ?";
            if (!professorMinistrouAulaTurmaVO.getSemestre().isEmpty()) {
                sqlSTR += " and professorMinistrouAulaTurma.semestre = ? ";
            }
            if (!professorMinistrouAulaTurmaVO.getAno().isEmpty()) {
                sqlSTR += " and professorMinistrouAulaTurma.ano = ? ";
            }
            final String sql = sqlSTR;

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setBoolean(1, professorMinistrouAulaTurmaVO.getTitular());
                    sqlAlterar.setInt(2, professorMinistrouAulaTurmaVO.getTurma().getCodigo().intValue());
                    sqlAlterar.setInt(3, professorMinistrouAulaTurmaVO.getDisciplina().getCodigo().intValue());
                    sqlAlterar.setInt(4, professorMinistrouAulaTurmaVO.getProfessor().getCodigo().intValue());
                    if (!professorMinistrouAulaTurmaVO.getSemestre().isEmpty()) {
                        sqlAlterar.setString(5, professorMinistrouAulaTurmaVO.getSemestre());
                    }
                    if (!professorMinistrouAulaTurmaVO.getAno().isEmpty()) {
                        if (professorMinistrouAulaTurmaVO.getSemestre().isEmpty()) {
                            sqlAlterar.setString(5, professorMinistrouAulaTurmaVO.getAno());
                        } else {
                            sqlAlterar.setString(6, professorMinistrouAulaTurmaVO.getAno());
                        }
                    }
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarTodosProfessoresTitular(final ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO) throws Exception {
        try {
            String sqlSTR = "UPDATE ProfessorMinistrouAulaTurma set titular=? WHERE professorMinistrouAulaTurma.turma = ? and professorMinistrouAulaTurma.disciplina = ? and professorMinistrouAulaTurma.professor = ?";
            if (!professorMinistrouAulaTurmaVO.getSemestre().isEmpty()) {
                sqlSTR += " and professorMinistrouAulaTurma.semestre = ? ";
            }
            if (!professorMinistrouAulaTurmaVO.getAno().isEmpty()) {
                sqlSTR += " and professorMinistrouAulaTurma.ano = ? ";
            }

            final String sql = sqlSTR;

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setBoolean(1, professorMinistrouAulaTurmaVO.getTitular());
                    sqlAlterar.setInt(2, professorMinistrouAulaTurmaVO.getTurma().getCodigo().intValue());
                    sqlAlterar.setInt(3, professorMinistrouAulaTurmaVO.getDisciplina().getCodigo().intValue());
                    sqlAlterar.setInt(4, professorMinistrouAulaTurmaVO.getProfessor().getCodigo().intValue());
                    if (!professorMinistrouAulaTurmaVO.getSemestre().isEmpty()) {
                        sqlAlterar.setString(5, professorMinistrouAulaTurmaVO.getSemestre());
                    }
                    if (!professorMinistrouAulaTurmaVO.getAno().isEmpty()) {
                        if (professorMinistrouAulaTurmaVO.getSemestre().isEmpty()) {
                            sqlAlterar.setString(5, professorMinistrouAulaTurmaVO.getAno());
                        } else {
                            sqlAlterar.setString(6, professorMinistrouAulaTurmaVO.getAno());
                        }
                    }
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ProfessorMinistrouAulaTurmaVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ProfessorMinistrouAulaTurmaVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ProfessorMinistrouAulaTurmaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ProfessorMinistrouAulaTurma.excluir(getIdEntidade());
            String sql = "DELETE FROM ProfessorMinistrouAulaTurma WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getFrequenciaAulaFacade().excluirFrequenciaAulas(obj.getCodigo(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    public List montarProfessoresMinistrouAulaTurma(ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "select * from professorMinistrouAulaTurma where professorMinistrouAulaTurma.turma = " + professorMinistrouAulaTurmaVO.getTurma().getCodigo() + " and professorMinistrouAulaTurma.disciplina = " + professorMinistrouAulaTurmaVO.getDisciplina().getCodigo();
        if (!professorMinistrouAulaTurmaVO.getAno().isEmpty()) {
            sqlStr += " and professorMinistrouAulaTurma.semestre = '" + professorMinistrouAulaTurmaVO.getSemestre() + "' ";
        }
        if (!professorMinistrouAulaTurmaVO.getSemestre().isEmpty()) {
            sqlStr += " and professorMinistrouAulaTurma.ano = '" + professorMinistrouAulaTurmaVO.getAno() + "' ";
        }
        sqlStr += " ORDER BY professorMinistrouAulaTurma.data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public ProfessorMinistrouAulaTurmaVO montarProfessoresMinistrouAulaTurmaTitular(ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "select * from professorMinistrouAulaTurma where professorMinistrouAulaTurma.turma = ? and professorMinistrouAulaTurma.disciplina = ? ";
        sqlStr += " and professorMinistrouAulaTurma.semestre = ? ";
        sqlStr += " and professorMinistrouAulaTurma.ano = ? ";
        sqlStr += " and professorMinistrouAulaTurma.titular = true";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{professorMinistrouAulaTurmaVO.getTurma().getCodigo().intValue(), professorMinistrouAulaTurmaVO.getDisciplina().getCodigo().intValue(), professorMinistrouAulaTurmaVO.getSemestre(), professorMinistrouAulaTurmaVO.getAno()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Não foi possivel localizar o professor titular da turma!");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorProfessorTurmaDisciplinaAnoSemestre(Integer professor, Integer turma, Integer disciplina, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "select * from professorMinistrouAulaTurma where professorMinistrouAulaTurma.turma = " + turma + " and professorMinistrouAulaTurma.disciplina = " + disciplina + " and professorMinistrouAulaTurma.professor = " + professor;
        if (!ano.isEmpty()) {
            sqlStr += " and professorMinistrouAulaTurma.semestre = '" + semestre + "' ";
        }
        if (!semestre.isEmpty()) {
            sqlStr += " and professorMinistrouAulaTurma.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY professorMinistrouAulaTurma.data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List<PessoaVO> montarListaProfessorRegistroAula(HorarioTurmaVO obj, ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO, UsuarioVO usuario) {
        List<PessoaVO> listaProfessor = new ArrayList<PessoaVO>(0);
        try {
//            Integer professor = getFacadeFactory().getHorarioTurmaFacade().consultarProfessorLecionaDisciplina(obj,professorMinistrouAulaTurmaVO.getDisciplina().getCodigo(), TipoHorarioTurma.SEMANAL);
//            if (professor.intValue() != 0) {
//                adicionarProfessor(professor, listaProfessor);
//            }
            List<Integer> professores = getFacadeFactory().getHorarioTurmaFacade().consultarProfessorLecionaDisciplina(obj, professorMinistrouAulaTurmaVO.getDisciplina().getCodigo());
            for (Integer professor : professores) {
                adicionarProfessor(professor, listaProfessor, usuario);
            }

        } catch (Exception e) {
//            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
        return listaProfessor;
    }

    private void adicionarProfessor(Integer codigoProfessor, List<PessoaVO> listaProfessor, UsuarioVO usuario) throws Exception {
        Iterator i = listaProfessor.iterator();
        while (i.hasNext()) {
            PessoaVO item = (PessoaVO) i.next();
            if (item.getCodigo().equals(codigoProfessor)) {
                return;
            }
        }
        PessoaVO professor = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(codigoProfessor, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        listaProfessor.add(professor);
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroAula</code> através do valor do atributo
     * <code>codigo</code> da classe <code>ProgramacaoAula</code> Faz uso da operação <code>montarDadosConsulta</code>
     * que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>ProfessorMinistrouAulaTurmaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigoTurma(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = turma.codigo and turma.codigo >= " + valorConsulta.intValue() + " ";

        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY turma.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorCodigoDisciplina(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina WHERE RegistroAula.disciplina = disciplina.codigo and disciplina.codigo = " + valorConsulta.intValue() + " ";
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }

        sqlStr += " ORDER BY disciplina.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorCodigoDisciplinaCodigoTurma(Integer valorConsulta, Integer turma, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina, Turma WHERE RegistroAula.disciplina = disciplina.codigo " + " and disciplina.codigo = " + valorConsulta.intValue()
                + " and RegistroAula.turma = turma.codigo and turma.codigo = " + turma.intValue() + " ";
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }

        sqlStr += " ORDER BY disciplina.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorNomeDisciplina(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina WHERE RegistroAula.disciplina = disciplina.codigo and lower (disciplina.nome) like('" + valorConsulta.toLowerCase()
                + "%') ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina, Turma WHERE RegistroAula.turma = Turma.codigo and RegistroAula.disciplina = disciplina.codigo and lower (disciplina.nome) like('"
                    + valorConsulta.toLowerCase() + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY disciplina.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorNomeDisciplinaProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina, Pessoa WHERE RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue()
                + " and RegistroAula.disciplina = disciplina.codigo and lower (disciplina.nome) like('" + valorConsulta.toLowerCase() + "%') ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Disciplina, Pessoa, Turma WHERE RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue()
                    + " and RegistroAula.disciplina = disciplina.codigo and lower (disciplina.nome) like('" + valorConsulta.toLowerCase()
                    + "%') and RegistroAula.turma = turma.codigo and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY disciplina.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorNomeProfessor(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Pessoa WHERE RegistroAula.professor = pessoa.codigo and lower (pessoa.nome) like('" + valorConsulta.toLowerCase() + "%') ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Pessoa WHERE RegistroAula.professor = pessoa.codigo and lower (pessoa.nome) like('" + valorConsulta.toLowerCase()
                    + "%') and RegistroAula.turma = turma.codigo and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }

        sqlStr += " ORDER BY pessoa.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorNomeCurso(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Curso WHERE RegistroAula.turma = Turma.codigo and turma.curso = curso.codigo and lower (curso.nome) like('"
                + valorConsulta.toLowerCase() + "%') ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr += " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }

        sqlStr += " ORDER BY RegistroAula.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorNomeCursoProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Curso, Pessoa WHERE RegistroAula.turma = Turma.codigo and turma.curso = curso.codigo and lower (curso.nome) like('"
                + valorConsulta.toLowerCase() + "%') ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr += " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " and RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " ";

        sqlStr += " ORDER BY RegistroAula.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorIdentificadorTurma(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) like ('" + valorConsulta.toLowerCase() + "%') ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) like ('" + valorConsulta.toLowerCase()
                    + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY turma.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorIdentificadorTurmaProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Pessoa WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) like ('" + valorConsulta.toLowerCase()
                + "%') " + "  and RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Pessoa WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) like ('" + valorConsulta.toLowerCase()
                    + "%') and RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY turma.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorIdentificadorTurmaProfessorDisciplina(String valorConsulta, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino,
            boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and lower (Turma.identificadorTurma) = '" + valorConsulta.toLowerCase() + "' "
                + " and RegistroAula.disciplina = " + disciplina.intValue();
//        if ((professor != null) && (professor != 0)) {
//             sqlStr += " and RegistroAula.professor = " + professor.intValue();
//        }
        if (unidadeEnsino.intValue() != 0) {
            sqlStr += " and Turma.unidadeEnsino = " + unidadeEnsino.intValue();
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY registroaula.data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroAula</code> através do valor do atributo
     * <code>String conteudo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ProfessorMinistrouAulaTurmaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorConteudo(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroAula WHERE conteudo like('" + valorConsulta + "%') ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and conteudo like('" + valorConsulta + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue()
                    + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY conteudo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroAula</code> através do valor do atributo
     * <code>Integer cargaHoraria</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ProfessorMinistrouAulaTurmaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCargaHoraria(Integer valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroAula WHERE cargaHoraria >= " + valorConsulta.intValue() + " ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM RegistroAula, Turma WHERE RegistroAula.turma = Turma.codigo and cargaHoraria >= " + valorConsulta.intValue() + " and turma.unidadeEnsino = "
                    + unidadeEnsino.intValue() + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY cargaHoraria";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroAula</code> através do valor do atributo
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ProfessorMinistrouAulaTurmaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorData(Date prmIni, Date prmFim, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroAula WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM RegistroAula, Turma  WHERE RegistroAula.turma = Turma.codigo and ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim)
                    + "')) and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public ProfessorMinistrouAulaTurmaVO consultarUltimoRegistroAulaPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT registroaula.* ");
        sqlStr.append("FROM matricula ");
        sqlStr.append("INNER JOIN matriculaperiodo ON (matricula.matricula = matriculaperiodo.matricula) ");
        sqlStr.append("INNER JOIN registroaula ON (matriculaperiodo.turma = registroaula.turma AND matriculaperiodo.ano = registroaula.ano AND matriculaperiodo.semestre = registroaula.semestre) ");
        sqlStr.append("WHERE matricula.matricula like'" + matricula + "' ");
        sqlStr.append("Order BY registroaula.data ");
        sqlStr.append("limit 1 ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return (montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        throw new ConsistirException("Não existe nenhum registro de aula para esse aluno no curso escolhido.");
    }

    public List consultarPorDataProfessor(Date prmIni, Date prmFim, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroAula.* FROM RegistroAula , Pessoa WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) "
                + "  and RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue() + " ";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT RegistroAula.* FROM RegistroAula, Turma, Pessoa WHERE RegistroAula.turma = Turma.codigo and ((RegistroAula.data >= '" + Uteis.getDataJDBC(prmIni)
                    + "') and (RegistroAula.data <= '" + Uteis.getDataJDBC(prmFim) + "')) and RegistroAula.professor = Pessoa.codigo and Pessoa.codigo = " + professor.intValue()
                    + " and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
        }
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY Pessoa.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

//    public boolean existeRegistroAula(ProfessorMinistrouAulaTurmaVO ra) throws Exception {
//        String sql = "SELECT dataregistroaula, responsavelregistroaula, turma, conteudo, " + "cargahoraria, data, codigo, disciplina, diaSemana, tipoaula " + "FROM registroaula " + "WHERE data = ? "
//                + "AND turma = ? " + "AND disciplina = ? and diaSemana = ? " + "AND codigo != ?";
//        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql,
//                new Object[]{Uteis.getDataJDBC(ra.getData()), ra.getTurma().getCodigo().intValue(), ra.getDisciplina().getCodigo().intValue(), ra.getDiaSemana(), ra.getCodigo().intValue()});
//        if (rs.next()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    /**
     * Responsável por realizar uma consulta de <code>RegistroAula</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ProfessorMinistrouAulaTurmaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM RegistroAula WHERE codigo >= " + valorConsulta.intValue() + " ";
        if (!semestre.equals("")) {
            sqlStr += " and RegistroAula.semestre = '" + semestre + "' ";
        }

        if (!ano.equals("")) {
            sqlStr += " and RegistroAula.ano = '" + ano + "' ";
        }
        sqlStr += " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>ProfessorMinistrouAulaTurmaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>ProfessorMinistrouAulaTurmaVO</code>.
     *
     * @return O objeto da classe <code>ProfessorMinistrouAulaTurmaVO</code> com os dados devidamente montados.
     */
    public static ProfessorMinistrouAulaTurmaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ProfessorMinistrouAulaTurmaVO obj = new ProfessorMinistrouAulaTurmaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
        obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
        obj.getResponsavelRegistro().setCodigo(new Integer(dadosSQL.getInt("responsavelRegistro")));
        obj.getProfessor().getPessoa().setCodigo(new Integer(dadosSQL.getInt("professor")));
        obj.setSemestre(dadosSQL.getString("semestre"));
        obj.setAno(dadosSQL.getString("ano"));
        obj.setTitular(dadosSQL.getBoolean("titular"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            montarDadosProfessor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }
        montarDadosTurma(obj, nivelMontarDados, usuario);
        montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosProfessor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosResponsavelRegistroAula(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
     * <code>ProfessorMinistrouAulaTurmaVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelRegistroAula(ProfessorMinistrouAulaTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelRegistro().getCodigo().intValue() == 0) {
            obj.setResponsavelRegistro(new UsuarioVO());
            return;
        }
        obj.setResponsavelRegistro(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelRegistro().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ProgramacaoAulaVO</code> relacionado ao
     * objeto <code>ProfessorMinistrouAulaTurmaVO</code>. Faz uso da chave primária da classe <code>ProgramacaoAulaVO</code> para
     * realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosTurma(ProfessorMinistrouAulaTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            obj.setTurma(new TurmaVO());
            return;
        }
        obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosProfessor(ProfessorMinistrouAulaTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getProfessor().getPessoa().getCodigo().intValue() == 0) {
            obj.getProfessor().setPessoa(new PessoaVO());
            return;
        }
//        obj.getProfessor().setPessoa(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getProfessor().getPessoa().getCodigo(), false, nivelMontarDados));
        obj.setProfessor(getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getProfessor().getPessoa().getCodigo(), 0, false, nivelMontarDados, usuario));
    }

    public static void montarDadosDisciplina(ProfessorMinistrouAulaTurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDisciplina().getCodigo().intValue() == 0) {
            obj.setDisciplina(new DisciplinaVO());
            return;
        }
        obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ProfessorMinistrouAulaTurmaVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ProfessorMinistrouAulaTurmaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroAula WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public Integer consultarSomaCargaHorarioDisciplina(Integer turma, String semestre, String ano, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "select SUM(registroaula.cargahoraria) as soma from registroaula, turma, disciplina " + "where registroaula.turma = turma.codigo and turma.codigo = ? "
                + "and registroaula.disciplina = disciplina.codigo and disciplina.codigo = ? and registroAula.semestre = ? and registroAula.ano = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{turma.intValue(), disciplina.intValue(), semestre, ano});
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (new Integer(tabelaResultado.getInt("soma")));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ProfessorMinistrouAulaTurma.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ProfessorMinistrouAulaTurma.idEntidade = idEntidade;
    }
}
