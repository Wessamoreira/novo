package negocio.facade.jdbc.avaliacaoinst;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import controle.arquitetura.DataModelo;
import controle.avaliacaoinst.AvaliacaoInstitucionalControle;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoCoordenadorCursoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalCursoVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPessoaAvaliadaVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalUnidadeEnsinoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.avaliacaoinst.AvaliacaoInstitucionalInterfaceFacade;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelVO;
import relatorio.negocio.comuns.avaliacaoInst.enumeradores.NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>AvaliacaoInstitucionalVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>AvaliacaoInstitucionalVO</code>. Encapsula toda a interação
 * com o banco de dados.
 *
 * @see AvaliacaoInstitucionalVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoInstitucional extends ControleAcesso implements AvaliacaoInstitucionalInterfaceFacade {

    /**
     *
     */
    private static final long serialVersionUID = 5166097655404363002L;
    protected static String idEntidade;

    public AvaliacaoInstitucional() throws Exception {
        super();
        setIdEntidade("AvaliacaoInstitucional");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>AvaliacaoInstitucionalVO</code>.
     */
    public AvaliacaoInstitucionalVO novo() throws Exception {
        AvaliacaoInstitucional.incluir(getIdEntidade());
        AvaliacaoInstitucionalVO obj = new AvaliacaoInstitucionalVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>AvaliacaoInstitucionalVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados
     * e a permissão do usuário para realizar esta operacão na entidade. Isto,
     * através da operação <code>incluir</code> da superclasse.
     *
     * @param obj Objeto da classe <code>AvaliacaoInstitucionalVO</code> que será
     *            gravado no banco de dados.
     * @throws Exception Caso haja problemas de conexão, restrição de acesso ou
     *                   validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AvaliacaoInstitucionalVO obj, final UsuarioVO usuarioVO) throws Exception {
        try {
            AvaliacaoInstitucionalVO.validarDados(obj);
            if (obj.getQuestionarioVO().getCodigo().intValue() != 0) {
                QuestionarioVO questionario = getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(
                        obj.getQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
                if (obj.getPublicoAlvo().equals("PR")) {
                    if (!questionario.getEscopo().equals(obj.getPublicoAlvo()) && !questionario.getEscopo().equals("GE")
                            && !questionario.getEscopo().equals("UM")) {
                        throw new ConsistirException(
                                "O campo QUESTIONÁRIO selecionado deve ter como Escopo o tipo: Professores ou Geral.");
                    }
                } else if (obj.getPublicoAlvo().equals("FG")) {
                    if (!questionario.getEscopo().equals(obj.getPublicoAlvo())
                            && !questionario.getEscopo().equals("GE")) {
                        throw new ConsistirException(
                                "O campo QUESTIONÁRIO selecionado deve ter como Escopo o tipo: Funcionário/Gestor ou Geral.");
                    }
                } else if (obj.getPublicoAlvo().equals("CO")) {
                    if (!questionario.getEscopo().equals(obj.getPublicoAlvo())
                            && !questionario.getEscopo().equals("GE")) {
                        throw new ConsistirException(
                                "O campo QUESTIONÁRIO selecionado deve ter como Escopo o tipo: Coordenadores ou Geral.");
                    }
                }
            }

            if (obj.getPublicoAlvo().equals("TC")) {
                if (!obj.getAvaliacaoInstitucionalUnidadeEnsinoVOs().isEmpty()) {
                    obj.setCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNomePorListaUnidadeEnsinoPorNivelEducacional("",
                            obj.getAvaliacaoInstitucionalUnidadeEnsinoVOs()
                                    .stream()
                                    .map(AvaliacaoInstitucionalUnidadeEnsinoVO::getUnidadeEnsinoVO)
                                    .collect(Collectors.toList())
                            ,
                            TipoNivelEducacional.getEnum(obj.getNivelEducacional()),
                            new DataModelo()));
                }
                for (CursoVO item : obj.getCursoVOs()) {
                    adicionarAvaliacaoInstitucionalCurso(obj, item);
                }
            }

            incluir(getIdEntidade(), true, usuarioVO);
            incluir(obj, "AvaliacaoInstitucional", new AtributoPersistencia()
                            .add("data", Uteis.getDataJDBC(obj.getData()))
                            .add("nome", obj.getNome())
                            .add("tipoFiltroProfessor", obj.getTipoFiltroProfessor())
                            .add("descricao", obj.getDescricao())
                            .add("publicoAlvo", obj.getPublicoAlvo())
                            .add("dataInicio", Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataInicio(), 0, 0, 0)))
                            .add("dataFinal", Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataFinal(), 23, 59, 59)))
                            .add("avaliacaoObrigatoria", obj.getAvaliacaoObrigatoria())
                            //.add("unidadeEnsino", obj.getUnidadeEnsino())
                            .add("questionario", obj.getQuestionarioVO())
                            .add("responsavel", obj.getResponsavel())
                            .add("informarImportanciaPergunta", obj.getInformarImportanciaPergunta())
                            .add("situacao", obj.getSituacao())
                            .add("avaliacaoPresencial", obj.getAvaliacaoPresencial())
                            .add("curso", obj.getCurso())
                            .add("turma", obj.getTurma())
                            .add("disciplina", obj.getDisciplina())
                            .add("nivelEducacional", obj.getNivelEducacional())
                            .add("ano", obj.getAno())
                            .add("semestre", obj.getSemestre())
                            .add("avaliacaoUltimoModulo", obj.getAvaliacaoUltimoModulo())
                            .add("horaInicio", obj.getHoraInicio())
                            .add("diasDisponivel", obj.getDiasDisponivel())
                            .add("horaFim", obj.getHoraFim())
                            .add("mensagem", obj.getMensagem())
                            .add("cargo", obj.getCargo())
                            .add("departamento", obj.getDepartamento())
                            .add("cargoAvaliado", obj.getCargoAvaliado())
                            .add("departamentoAvaliado", obj.getDepartamentoAvaliado())
                            .add("datainicioaula", obj.getIsApresentarPeriodoAula() ? Uteis.getDataJDBC(obj.getDataInicioAula()) : null)
                            .add("dataterminoaula", obj.getIsApresentarPeriodoAula() ? Uteis.getDataJDBC(obj.getDataTerminoAula()) : null)
                            .add("publicarResultadoRespondente", obj.getPublicarResultadoRespondente())
                            .add("dataInicioPublicarResultado",
                                    obj.getPublicarResultadoRespondente() || obj.getPublicarResultadoAluno()
                                            || obj.getPublicarResultadoProfessor() || obj.getPublicarResultadoCoordenador()
                                            ? Uteis.getDataJDBC(obj.getDataInicioPublicarResultado())
                                            : null)
                            .add("dataTerminoPublicarResultado",
                                    obj.getPublicarResultadoRespondente() || obj.getPublicarResultadoAluno()
                                            || obj.getPublicarResultadoProfessor() || obj.getPublicarResultadoCoordenador()
                                            ? Uteis.getDataJDBC(obj.getDataTerminoPublicarResultado())
                                            : null)
                            .add("notificarRespondentes", obj.getNotificarRespondentes())
                            .add("recorrenciaDiaNotificar", obj.getRecorrenciaDiaNotificar())
                            .add("publicarResultadoAluno", obj.getPublicarResultadoAluno())
                            .add("publicarResultadoProfessor", obj.getPublicarResultadoProfessor())
                            .add("publicarResultadoCoordenador", obj.getPublicarResultadoCoordenador())
                            .add("nivelDetalhamentoPublicarResultado", obj.getNivelDetalhamentoPublicarResultado().name())
                            .add("tipoCoordenadorCurso", obj.getTipoCoordenadorCurso().name())
                            .add("avaliardisciplinasreposicao", obj.getAvaliarDisciplinasReposicao()),
                    usuarioVO);
            getFacadeFactory().getAvaliacaoInstitucionalPessoaAvaliadaFacade().persistir(obj.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs(), false, usuarioVO);
            getFacadeFactory().getAvaliacaoInstitucionalCurso().incluirAvaliacaoInstitucionalCurso(obj, usuarioVO);
            getFacadeFactory().getAvaliacaoInstitucionalUnidadeEnsino().incluirAvaliacaoInstitucionalUnidadeEnsino(obj, usuarioVO);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>AvaliacaoInstitucionalVO</code>. Sempre utiliza a chave primária da
     * classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica
     * a conexão com o banco de dados e a permissão do usuário para realizar esta
     * operacão na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj Objeto da classe <code>AvaliacaoInstitucionalVO</code> que será
     *            alterada no banco de dados.
     * @throws Exception Caso haja problemas de conexão, restrição de acesso ou
     *                   validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AvaliacaoInstitucionalVO obj, UsuarioVO usuario) throws Exception {
        try {

            AvaliacaoInstitucionalVO.validarDados(obj);
            alterar(getIdEntidade(), true, usuario);

            alterar(obj, "AvaliacaoInstitucional", new AtributoPersistencia()
                            .add("data", Uteis.getDataJDBC(obj.getData()))
                            .add("nome", obj.getNome())
                            .add("tipoFiltroProfessor", obj.getTipoFiltroProfessor())
                            .add("descricao", obj.getDescricao())
                            .add("publicoAlvo", obj.getPublicoAlvo())
                            .add("dataInicio", Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataInicio(), 0, 0, 0)))
                            .add("dataFinal", Uteis.getDataJDBCTimestamp(Uteis.getDateTime(obj.getDataFinal(), 23, 59, 59)))
                            .add("avaliacaoObrigatoria", obj.getAvaliacaoObrigatoria())
                            //.add("unidadeEnsino", obj.getUnidadeEnsino())
                            .add("questionario", obj.getQuestionarioVO())
                            .add("responsavel", obj.getResponsavel())
                            .add("informarImportanciaPergunta", obj.getInformarImportanciaPergunta())
                            .add("situacao", obj.getSituacao())
                            .add("avaliacaoPresencial", obj.getAvaliacaoPresencial())
                            .add("curso", obj.getCurso())
                            .add("turma", obj.getTurma())
                            .add("disciplina", obj.getDisciplina())
                            .add("nivelEducacional", obj.getNivelEducacional())
                            .add("ano", obj.getAno())
                            .add("semestre", obj.getSemestre())
                            .add("avaliacaoUltimoModulo", obj.getAvaliacaoUltimoModulo())
                            .add("horaInicio", obj.getHoraInicio())
                            .add("diasDisponivel", obj.getDiasDisponivel())
                            .add("horaFim", obj.getHoraFim())
                            .add("mensagem", obj.getMensagem())
                            .add("cargo", obj.getCargo())
                            .add("departamento", obj.getDepartamento())
                            .add("cargoAvaliado", obj.getCargoAvaliado())
                            .add("departamentoAvaliado", obj.getDepartamentoAvaliado())
                            .add("datainicioaula", obj.getIsApresentarPeriodoAula() ? Uteis.getDataJDBC(obj.getDataInicioAula()) : null)
                            .add("dataterminoaula", obj.getIsApresentarPeriodoAula() ? Uteis.getDataJDBC(obj.getDataTerminoAula()) : null)
                            .add("publicarResultadoRespondente", obj.getPublicarResultadoRespondente())
                            .add("dataInicioPublicarResultado",
                                    obj.getPublicarResultadoRespondente() || obj.getPublicarResultadoAluno()
                                            || obj.getPublicarResultadoProfessor() || obj.getPublicarResultadoCoordenador()
                                            ? Uteis.getDataJDBC(obj.getDataInicioPublicarResultado())
                                            : null)
                            .add("dataTerminoPublicarResultado",
                                    obj.getPublicarResultadoRespondente() || obj.getPublicarResultadoAluno()
                                            || obj.getPublicarResultadoProfessor() || obj.getPublicarResultadoCoordenador()
                                            ? Uteis.getDataJDBC(obj.getDataTerminoPublicarResultado())
                                            : null)
                            .add("notificarRespondentes", obj.getNotificarRespondentes())
                            .add("recorrenciaDiaNotificar", obj.getRecorrenciaDiaNotificar())
                            .add("publicarResultadoAluno", obj.getPublicarResultadoAluno())
                            .add("publicarResultadoProfessor", obj.getPublicarResultadoProfessor())
                            .add("publicarResultadoCoordenador", obj.getPublicarResultadoCoordenador())
                            .add("nivelDetalhamentoPublicarResultado", obj.getNivelDetalhamentoPublicarResultado().name())
                            .add("tipoCoordenadorCurso", obj.getTipoCoordenadorCurso().name())
                            .add("avaliardisciplinasreposicao", obj.getAvaliarDisciplinasReposicao()),
                    new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);

            validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs(),
                    "avaliacaoInstitucionalPessoaAvaliada", "avaliacaoInstitucional", obj.getCodigo(), usuario);
            getFacadeFactory().getAvaliacaoInstitucionalPessoaAvaliadaFacade()
                    .persistir(obj.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs(), false, usuario);
            getFacadeFactory().getAvaliacaoInstitucionalCurso().alterarAvaliacaoInstitucionalCurso(obj, usuario);
            getFacadeFactory().getAvaliacaoInstitucionalUnidadeEnsino().alterarAvaliacaoInstitucionalUnidadeEnsino(obj, usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoAvaliacao(final Integer codigo, final String situacao, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario)
            throws Exception {
        try {
            if (situacao.equals("AT") && !existeAvaliacaoInstitucional(avaliacaoInstitucionalVO, usuario) && avaliacaoInstitucionalVO != null) {
                List<AvaliacaoInstitucionalAnaliticoRelVO> lista = getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().realizarGeracaoRelatorioAnalitico(avaliacaoInstitucionalVO.getUnidadeEnsino().getCodigo(), avaliacaoInstitucionalVO, avaliacaoInstitucionalVO.getCurso().getCodigo(), 0, avaliacaoInstitucionalVO.getTurma().getCodigo(), "TODAS", "pessoa.nome", avaliacaoInstitucionalVO.getDataInicio(), avaliacaoInstitucionalVO.getDataFinal(), false, usuario, true);
                if (lista.isEmpty()) {
                    throw new ConsistirException("Nenhum respondente encontrado para esta avaliação institucional.");
                }
                getFacadeFactory().getAvaliacaoInstitucionalRespondenteInterfaceFacade().persistir(lista, codigo, usuario);
            }

            final String sql = "UPDATE AvaliacaoInstitucional set situacao=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

                    sqlAlterar.setString(1, situacao);
                    sqlAlterar.setInt(2, codigo);
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    private boolean existeAvaliacaoInstitucional(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UsuarioVO usuario) throws Exception {
        return getFacadeFactory().getAvaliacaoInstitucionalRespondenteInterfaceFacade().consultarAvaliacaoInstitucionalRespondenteExistentePorAvaliacaoInstitucional(avaliacaoInstitucionalVO, 0, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDataInicioDataFimAvaliacao(final AvaliacaoInstitucionalVO avaliacaoInstitucionalVO,
                                                  UsuarioVO usuario) throws Exception {
        try {
            alterar(avaliacaoInstitucionalVO, "AvaliacaoInstitucional",
                    new AtributoPersistencia()
                            .add("nome", avaliacaoInstitucionalVO.getNome())
                            .add("dataInicio", Uteis.getDataJDBCTimestamp(Uteis.getDateTime(avaliacaoInstitucionalVO.getDataInicio(), 0, 0, 0)))
                            .add("dataFinal", Uteis.getDataJDBCTimestamp(Uteis.getDateTime(avaliacaoInstitucionalVO.getDataFinal(), 23, 59, 59)))
                            .add("avaliacaoObrigatoria", avaliacaoInstitucionalVO.getAvaliacaoObrigatoria())
                            .add("avaliacaoPresencial", avaliacaoInstitucionalVO.getAvaliacaoPresencial())
                            .add("informarImportanciaPergunta", avaliacaoInstitucionalVO.getInformarImportanciaPergunta())
                            .add("publicarResultadoRespondente", avaliacaoInstitucionalVO.getPublicarResultadoRespondente())
                            .add("dataInicioPublicarResultado", avaliacaoInstitucionalVO.getPublicarResultadoRespondente()
                                    || avaliacaoInstitucionalVO.getPublicarResultadoAluno()
                                    || avaliacaoInstitucionalVO.getPublicarResultadoProfessor()
                                    || avaliacaoInstitucionalVO.getPublicarResultadoCoordenador() ? Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataInicioPublicarResultado()) : null)
                            .add("dataTerminoPublicarResultado", avaliacaoInstitucionalVO.getPublicarResultadoRespondente()
                                    || avaliacaoInstitucionalVO.getPublicarResultadoAluno()
                                    || avaliacaoInstitucionalVO.getPublicarResultadoProfessor()
                                    || avaliacaoInstitucionalVO.getPublicarResultadoCoordenador() ? Uteis.getDataJDBC(avaliacaoInstitucionalVO.getDataTerminoPublicarResultado()) : null)
                            .add("notificarRespondentes", avaliacaoInstitucionalVO.getNotificarRespondentes())
                            .add("recorrenciaDiaNotificar", avaliacaoInstitucionalVO.getRecorrenciaDiaNotificar())
                            .add("publicarResultadoAluno", avaliacaoInstitucionalVO.getPublicarResultadoAluno())
                            .add("publicarResultadoProfessor", avaliacaoInstitucionalVO.getPublicarResultadoProfessor())
                            .add("publicarResultadoCoordenador", avaliacaoInstitucionalVO.getPublicarResultadoCoordenador())
                            .add("horaInicio", avaliacaoInstitucionalVO.getHoraInicio())
                            .add("horaFim", avaliacaoInstitucionalVO.getHoraFim())
                            .add("diasDisponivel", avaliacaoInstitucionalVO.getDiasDisponivel())
                            .add("nivelDetalhamentoPublicarResultado", avaliacaoInstitucionalVO.getNivelDetalhamentoPublicarResultado().name())
                            .add("avaliardisciplinasreposicao", avaliacaoInstitucionalVO.getAvaliarDisciplinasReposicao()),
                    new AtributoPersistencia().add("codigo", avaliacaoInstitucionalVO.getCodigo()), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>AvaliacaoInstitucionalVO</code>. Sempre localiza o registro a ser
     * excluído através da chave primária da entidade. Primeiramente verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta
     * operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj Objeto da classe <code>AvaliacaoInstitucionalVO</code> que será
     *            removido no banco de dados.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AvaliacaoInstitucionalVO obj, UsuarioVO usuario) throws Exception {
        try {
            excluir(getIdEntidade(), true, usuario);
            Boolean avaliacaoRespondida = verificarPermissaoAlteracaoExclusao(obj.getCodigo(), usuario);
            if (avaliacaoRespondida) {
                throw new Exception(
                        "Não é possível realizar a exclusão desta avaliação. Pois a mesma já foi respondida pelo público alvo.");
            }
            String sql = "DELETE FROM AvaliacaoInstitucional WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public Boolean verificarPermissaoAlteracaoExclusao(Integer codigo, UsuarioVO usuario) throws Exception {
        Boolean respondida = false;
        if (codigo.intValue() != 0) {
            respondida = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade()
                    .consultarPorAvaliacaoInstitucionalRespondida(codigo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                            usuario);
        }
        return respondida;
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code>
     * através do valor do atributo <code>descricao</code> da classe
     * <code>Questionario</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @return List Contendo vários objetos da classe
     * <code>AvaliacaoInstitucionalVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<AvaliacaoInstitucionalVO> consultarPorDescricaoQuestionario(String valorConsulta,
                                                                            boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT AvaliacaoInstitucional.* FROM AvaliacaoInstitucional, Questionario WHERE AvaliacaoInstitucional.questionariounidadeensino = Questionario.codigo and upper(sem_acentos(Questionario.descricao) ) like(sem_acentos('"
                + valorConsulta.toUpperCase() + "%')) ORDER BY Questionario.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code>
     * através do valor do atributo <code>nome</code> da classe
     * <code>UnidadeEnsino</code> Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @return List Contendo vários objetos da classe
     * <code>AvaliacaoInstitucionalVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<AvaliacaoInstitucionalVO> consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso,
                                                                        int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT AvaliacaoInstitucional.* FROM AvaliacaoInstitucional, UnidadeEnsino WHERE AvaliacaoInstitucional.unidadeEnsino = UnidadeEnsino.codigo and upper(sem_acentos(UnidadeEnsino.nome)) like(sem_acentos('"
                + valorConsulta.toUpperCase() + "%')) ORDER BY UnidadeEnsino.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code>
     * através do valor do atributo <code>Date dataFinal</code>. Retorna os objetos
     * com valores pertecentes ao período informado por parâmetro. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
     * o List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário
     *                        possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>AvaliacaoInstitucionalVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<AvaliacaoInstitucionalVO> consultarPorDataFinal(Date prmIni, Date prmFim, boolean controlarAcesso,
                                                                int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucional WHERE ((dataFinal >= '" + Uteis.getDataJDBC(prmIni)
                + "') and (dataFinal <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataFinal";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code>
     * através do valor do atributo <code>Date dataInicio</code>. Retorna os objetos
     * com valores pertecentes ao período informado por parâmetro. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
     * o List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário
     *                        possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>AvaliacaoInstitucionalVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<AvaliacaoInstitucionalVO> consultarPorDataInicio(Date prmIni, Date prmFim, boolean controlarAcesso,
                                                                 int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucional WHERE ((dataInicio >= '" + Uteis.getDataJDBC(prmIni)
                + "') and (dataInicio <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataInicio";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<AvaliacaoInstitucionalVO> consultarPorAvaliacaoAtiva(Date prmIni, Integer unidadeEnsino,
                                                                     String publicoAlvo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucional WHERE ((dataInicio <= '"
                + Uteis.getDataJDBCTimestamp(prmIni) + " ') " + "and (dataFinal >= '"
                + Uteis.getDataJDBCTimestamp(prmIni) + "')) and";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr += " (unidadeEnsino is null or unidadeEnsino = " + unidadeEnsino.intValue() + ") and ";
        }
        sqlStr += "(upper (AvaliacaoInstitucional.publicoAlvo) = 'TO' or upper (AvaliacaoInstitucional.publicoAlvo) = '"
                + publicoAlvo.toUpperCase() + "') ";
        sqlStr += "ORDER BY data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code>
     * através do valor do atributo <code>String publicoAlvo</code>. Retorna os
     * objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz
     * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário
     *                        possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>AvaliacaoInstitucionalVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<AvaliacaoInstitucionalVO> consultarPorPublicoAlvo(String valorConsulta, boolean controlarAcesso,
                                                                  int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucional WHERE upper (AvaliacaoInstitucional.publicoAlvo )  = '"
                + valorConsulta.toUpperCase() + "' ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code>
     * através do valor do atributo <code>String nome</code>. Retorna os objetos,
     * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
     * o List resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário
     *                        possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>AvaliacaoInstitucionalVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<AvaliacaoInstitucionalVO> consultarPorNome(String valorConsulta, Integer limite, Integer offset,
                                                           boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM AvaliacaoInstitucional WHERE sem_acentos(nome) ilike(sem_acentos(?))");
        if (Uteis.isAtributoPreenchido(usuario.getUnidadeEnsinoLogado())) {
            sqlStr.append(" and unidadeensino = ").append(usuario.getUnidadeEnsinoLogado().getCodigo());
        }
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio desc, nome ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(),
                "%" + valorConsulta + "%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code>
     * através do valor do atributo <code>Date data</code>. Retorna os objetos com
     * valores pertecentes ao período informado por parâmetro. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário
     *                        possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>AvaliacaoInstitucionalVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<AvaliacaoInstitucionalVO> consultarPorData(Date prmIni, Date prmFim, Boolean presencial, Integer limite,
                                                           Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM AvaliacaoInstitucional WHERE ((data >= '").append(Uteis.getDataJDBC(prmIni));
        sqlStr.append("') and (data <= '").append(Uteis.getDataJDBC(prmFim)).append("')) ");
        if (presencial != null) {
            sqlStr.append(" AND presencial = ").append(presencial);
        }
        sqlStr.append(" ORDER BY datainicio desc, data ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>AvaliacaoInstitucional</code>
     * através do valor do atributo <code>Integer codigo</code>. Retorna os objetos
     * com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
     * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário
     *                        possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     * <code>AvaliacaoInstitucionalVO</code> resultantes da consulta.
     * @throws Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<AvaliacaoInstitucionalVO> consultarPorCodigo(Integer valorConsulta, Boolean presencial, Integer limite,
                                                             Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM AvaliacaoInstitucional WHERE codigo >= ")
                .append(valorConsulta.intValue());
        if (presencial != null) {
            sqlStr.append(" AND presencial = ").append(presencial);
        }
        sqlStr.append(" ORDER BY datainicio desc ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     * <code>AvaliacaoInstitucionalVO</code> resultantes da consulta.
     */
    public  List<AvaliacaoInstitucionalVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,
                                                                     UsuarioVO usuario) throws Exception {
        List<AvaliacaoInstitucionalVO> vetResultado = new ArrayList<AvaliacaoInstitucionalVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados
     * (<code>ResultSet</code>) em um objeto da classe
     * <code>AvaliacaoInstitucionalVO</code>.
     *
     * @return O objeto da classe <code>AvaliacaoInstitucionalVO</code> com os dados
     * devidamente montados.
     */
    public  AvaliacaoInstitucionalVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        AvaliacaoInstitucionalVO obj = new AvaliacaoInstitucionalVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.setNome(dadosSQL.getString("nome"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
        obj.setTipoFiltroProfessor(dadosSQL.getString("tipoFiltroProfessor"));
        obj.setAno(dadosSQL.getString("ano"));
        obj.setSemestre(dadosSQL.getString("semestre"));
        obj.setAvaliacaoUltimoModulo(dadosSQL.getBoolean("avaliacaoUltimoModulo"));
        obj.setHoraInicio(dadosSQL.getString("horaInicio"));
        obj.setDiasDisponivel(dadosSQL.getInt("diasDisponivel"));
        obj.setHoraFim(dadosSQL.getString("horaFim"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setMensagem(dadosSQL.getString("mensagem"));
        obj.setPublicoAlvo(dadosSQL.getString("publicoAlvo"));
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataFinal(dadosSQL.getDate("dataFinal"));
        obj.setAvaliacaoObrigatoria(dadosSQL.getBoolean("avaliacaoObrigatoria"));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.getQuestionarioVO().setCodigo(new Integer(dadosSQL.getInt("questionario")));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.setInformarImportanciaPergunta(dadosSQL.getBoolean("informarImportanciaPergunta"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setAvaliacaoPresencial(dadosSQL.getBoolean("avaliacaoPresencial"));
        obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
        obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
        obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
        obj.getCargo().setCodigo(new Integer(dadosSQL.getInt("cargo")));
        obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento")));
        obj.getCargoAvaliado().setCodigo(new Integer(dadosSQL.getInt("cargoAvaliado")));
        obj.getDepartamentoAvaliado().setCodigo(new Integer(dadosSQL.getInt("departamentoAvaliado")));
        obj.setDataInicioAula(dadosSQL.getDate("datainicioaula"));
        obj.setDataTerminoAula(dadosSQL.getDate("dataterminoaula"));
        obj.setDataInicioPublicarResultado(dadosSQL.getDate("dataInicioPublicarResultado"));
        obj.setDataTerminoPublicarResultado(dadosSQL.getDate("dataTerminoPublicarResultado"));
        obj.setPublicarResultadoAluno(dadosSQL.getBoolean("publicarResultadoAluno"));
        obj.setPublicarResultadoProfessor(dadosSQL.getBoolean("publicarResultadoProfessor"));
        obj.setPublicarResultadoCoordenador(dadosSQL.getBoolean("publicarResultadoCoordenador"));
        obj.setPublicarResultadoRespondente(dadosSQL.getBoolean("publicarResultadoRespondente"));
        obj.setAvaliarDisciplinasReposicao(dadosSQL.getBoolean("avaliardisciplinasreposicao"));
        obj.setNivelDetalhamentoPublicarResultado(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum
                .valueOf(dadosSQL.getString("nivelDetalhamentoPublicarResultado")));
        obj.setTipoCoordenadorCurso(TipoCoordenadorCursoEnum.valueOf(dadosSQL.getString("tipoCoordenadorCurso")));
        obj.setNotificarRespondentes(dadosSQL.getBoolean("notificarRespondentes"));
        obj.setRecorrenciaDiaNotificar(dadosSQL.getInt("recorrenciaDiaNotificar"));
        obj.setDataUltimaNotificacao(dadosSQL.getDate("dataUltimaNotificacao"));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosCargo(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosDepartamento(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosCargoAvaliado(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosDepartamentoAvaliado(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            return obj;
        }
        montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosTurma(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosQuestionario(obj, nivelMontarDados, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        obj.setListaAvaliacaoInstitucionalPessoaAvaliadaVOs(
                getFacadeFactory().getAvaliacaoInstitucionalPessoaAvaliadaFacade()
                        .consultarPorAvaliacaoInstitucional(obj.getCodigo(), nivelMontarDados, usuario));
        obj.setAvaliacaoInstitucionalCursoVOs(getFacadeFactory().getAvaliacaoInstitucionalCurso().consultarPorAvaliacaoInstitucional(obj.getCodigo(), usuario));
        obj.setAvaliacaoInstitucionalUnidadeEnsinoVOs(getFacadeFactory().getAvaliacaoInstitucionalUnidadeEnsino().consultarPorAvaliacaoInstitucional(obj.getCodigo(), usuario));
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>UsuarioVO</code> relacionado ao objeto
     * <code>AvaliacaoInstitucionalVO</code>. Faz uso da chave primária da classe
     * <code>UsuarioVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosResponsavel(AvaliacaoInstitucionalVO obj, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade()
                .consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>QuestionarioVO</code> relacionado ao objeto
     * <code>AvaliacaoInstitucionalVO</code>. Faz uso da chave primária da classe
     * <code>QuestionarioVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosQuestionario(AvaliacaoInstitucionalVO obj, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        if (obj.getQuestionarioVO().getCodigo().intValue() == 0) {
            obj.setQuestionarioVO(new QuestionarioVO());
            return;
        }
        obj.setQuestionarioVO(getFacadeFactory().getQuestionarioFacade()
                .consultarPorChavePrimaria(obj.getQuestionarioVO().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>UnidadeEnsinoVO</code> relacionado ao objeto
     * <code>AvaliacaoInstitucionalVO</code>. Faz uso da chave primária da classe
     * <code>UnidadeEnsinoVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosUnidadeEnsino(AvaliacaoInstitucionalVO obj, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade()
                .consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>Cargo</code> relacionado ao objeto
     * <code>AvaliacaoInstitucionalVO</code>. Faz uso da chave primária da classe
     * <code>CargoVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosCargo(AvaliacaoInstitucionalVO obj, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        if (obj.getCargo().getCodigo().intValue() == 0) {
            return;
        }
        obj.setCargo(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getCargo().getCodigo(), false,
                nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>Departamento</code> relacionado ao objeto
     * <code>AvaliacaoInstitucionalVO</code>. Faz uso da chave primária da classe
     * <code>DepartamentoVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosDepartamento(AvaliacaoInstitucionalVO obj, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        if (obj.getDepartamento().getCodigo().intValue() == 0) {
            return;
        }
        obj.setDepartamento(getFacadeFactory().getDepartamentoFacade()
                .consultarPorChavePrimaria(obj.getDepartamento().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>Cargo</code> relacionado ao objeto
     * <code>AvaliacaoInstitucionalVO</code>. Faz uso da chave primária da classe
     * <code>CargoVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosCargoAvaliado(AvaliacaoInstitucionalVO obj, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        if (obj.getCargoAvaliado().getCodigo().intValue() == 0) {
            return;
        }
        obj.setCargoAvaliado(getFacadeFactory().getCargoFacade()
                .consultarPorChavePrimaria(obj.getCargoAvaliado().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>Departamento</code> relacionado ao objeto
     * <code>AvaliacaoInstitucionalVO</code>. Faz uso da chave primária da classe
     * <code>DepartamentoVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosDepartamentoAvaliado(AvaliacaoInstitucionalVO obj, int nivelMontarDados,
                                                       UsuarioVO usuario) throws Exception {
        if (obj.getDepartamentoAvaliado().getCodigo().intValue() == 0) {
            return;
        }
        obj.setDepartamentoAvaliado(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(
                obj.getDepartamentoAvaliado().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>Curso</code> relacionado ao objeto
     * <code>AvaliacaoInstitucionalVO</code>. Faz uso da chave primária da classe
     * <code>CursoVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosCurso(AvaliacaoInstitucionalVO obj, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        if (obj.getCurso().getCodigo().intValue() == 0) {
            obj.setCurso(new CursoVO());
            return;
        }
        obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(),
                nivelMontarDados, false, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>Turma</code> relacionado ao objeto
     * <code>AvaliacaoInstitucionalVO</code>. Faz uso da chave primária da classe
     * <code>TurmaVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosTurma(AvaliacaoInstitucionalVO obj, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            obj.setTurma(new TurmaVO());
            return;
        }
        obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(),
                nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>Turma</code> relacionado ao objeto
     * <code>AvaliacaoInstitucionalVO</code>. Faz uso da chave primária da classe
     * <code>TurmaVO</code> para realizar a consulta.
     *
     * @param obj Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosDisciplina(AvaliacaoInstitucionalVO obj, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        if (obj.getDisciplina().getCodigo().intValue() == 0) {
            obj.setDisciplina(new DisciplinaVO());
            return;
        }
        obj.setDisciplina(getFacadeFactory().getDisciplinaFacade()
                .consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>AvaliacaoInstitucionalVO</code> através de sua chave primária.
     *
     * @throws Exception Caso haja problemas de conexão ou localização do objeto
     *                   procurado.
     */
    public AvaliacaoInstitucionalVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,
                                                              UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM AvaliacaoInstitucional WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( AvaliacaoInstitucional ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as operações
     * desta classe.
     */
    public static String getIdEntidade() {
        return AvaliacaoInstitucional.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
     * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
     * controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        AvaliacaoInstitucional.idEntidade = idEntidade;
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorNome(String valorConsulta, Boolean presencial,
                                                                Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(lower(avaliacaoinstitucional.nome)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        if (presencial != null) {
            sqlStr.append(" AND avaliacaoinstitucional.avaliacaoPresencial = ").append(presencial);
        }
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio desc, avaliacaoinstitucional.nome");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorUnidadeEnsinoDataSituacaoPublicoAlvo(Integer unidadeEnsino,
                                                                                                Date data, String situacao, String publicoAlvo, Boolean avaliacaoPresencial, boolean controlarAcesso,
                                                                                                UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE avaliacaoinstitucional.dataInicio <= '");
        sqlStr.append(Uteis.getDataJDBC(data));
        sqlStr.append("' AND avaliacaoinstitucional.dataFinal >= '");
        sqlStr.append(Uteis.getDataJDBC(data));
        sqlStr.append("' ");
        if (unidadeEnsino != null && unidadeEnsino != 0 && !usuario.getIsApresentarVisaoProfessor()) {
            sqlStr.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino);
        }
        if (!situacao.equals("")) {
            sqlStr.append(" AND avaliacaoinstitucional.situacao = '").append(situacao).append("'");
        }
        if (!publicoAlvo.equals("")) {
            sqlStr.append(" AND avaliacaoinstitucional.publicoAlvo = '").append(publicoAlvo).append("'");
        }
        if (avaliacaoPresencial != null) {
            sqlStr.append(" AND avaliacaoinstitucional.avaliacaoPresencial = ").append(avaliacaoPresencial);
        }
        sqlStr.append(" ORDER BY avaliacaoinstitucional.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorUnidadeEnsinoDataSituacaoVisaoLogar(Integer unidadeEnsino,
                                                                                               Date data, String situacao, String visaoLogar, Boolean avaliacaoPresencial, boolean possuiNaoPossuiUnidade,
                                                                                               boolean controlarAcesso, UsuarioVO usuario, Integer curso, String nivelEducacional, String matricula, Integer codigoAvaliacaoInstitucional) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();

        sqlStr.append("INNER JOIN avaliacaoinstitucionalrespondente on avaliacaoinstitucionalrespondente.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");


        sqlStr.append("WHERE avaliacaoinstitucional.dataInicio <= '");
        sqlStr.append(Uteis.getDataJDBC(data));
        sqlStr.append("' AND avaliacaoinstitucional.dataFinal >= '");
        sqlStr.append(Uteis.getDataJDBC(data));
        sqlStr.append("' ");
        sqlStr.append(" AND avaliacaoinstitucional.avaliacaoUltimoModulo = false ");
        if (Uteis.isAtributoPreenchido(codigoAvaliacaoInstitucional)) {
            sqlStr.append(" AND avaliacaoinstitucional.codigo = ").append(codigoAvaliacaoInstitucional);
        }
        if (possuiNaoPossuiUnidade) {
            if (unidadeEnsino != null && unidadeEnsino != 0) {
                sqlStr.append(" AND (unidadeEnsino.codigo = ").append(unidadeEnsino);
                sqlStr.append(" OR unidadeEnsino.codigo is Null)");
            }
        } else {
            if (unidadeEnsino != null && unidadeEnsino != 0) {
                sqlStr.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino);
            }
        }
        if (visaoLogar.equals("funcionario") || visaoLogar.equals("loginAdministrador")) {
            sqlStr.append(" AND ( exists (select distinct funcionariocargo.codigo  from funcionariocargo ");
            sqlStr.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo()).append(" and unidadeEnsino.codigo = funcionariocargo.unidadeensino  and funcionariocargo.ativo )");
            sqlStr.append(" OR unidadeEnsino.codigo is Null)");
        }
        if (!situacao.equals("")) {
            sqlStr.append(" AND avaliacaoinstitucional.situacao = '").append(situacao).append("'");
        }
        if (visaoLogar.equals("aluno")) {
            sqlStr.append(" AND avaliacaoinstitucional.niveleducacional = '").append(nivelEducacional).append("' ");
            sqlStr.append(" AND avaliacaoinstitucionalrespondente.matricula = '" + matricula + "'and avaliacaoinstitucionalrespondente.respondido = false ");
            sqlStr.append(" AND avaliacaoinstitucional.publicoalvo in ('TC', 'CU', 'TU', 'ALUNO_COORDENADOR') ");
//			sqlStr.append(" AND (");
//			sqlStr.append(getWherePublicoAlvoAlunoAvaliaTodosCurso(usuario, matricula));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoAlunoAvaliaCursoEspecifico(usuario, curso, matricula));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoAlunoAvaliaTurmaEspecifica(usuario, curso, matricula));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoAlunoAvaliaCoordenador(usuario, curso, matricula));
//			sqlStr.append(" ) ");
//			sqlStr.append(" and not exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
//			sqlStr.append(" and respostaavaliacaoinstitucionaldw.unidadeensino = avaliacaoinstitucional.unidadeensino ");
//			if(Uteis.isAtributoPreenchido(matricula)) {
//				sqlStr.append(" and respostaavaliacaoinstitucionaldw.matriculaaluno = '").append(matricula).append("' ");
//			}
//			sqlStr.append(" and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo())
//					.append(" limit 1) ");
        }
        if (visaoLogar.equals("professor")) {
            sqlStr.append(" AND avaliacaoinstitucionalrespondente.pessoa = '" + usuario.getPessoa().getCodigo() + "'and avaliacaoinstitucionalrespondente.respondido = false ");
            sqlStr.append(" AND avaliacaoinstitucional.publicoalvo in ('PR', 'PROFESSORES_COORDENADORES', 'COLABORADORES_INSTITUICAO', 'PROFESSOR_TURMA', 'PROFESSOR_CURSO') ");
//			sqlStr.append(" AND ( ");
//			sqlStr.append(getWherePublicoAlvoProfessorAvaliaInstituicao(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoProfessorAvaliaCoordenador(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoColaboradorAvaliaInstituicao(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoFuncionarioAvaliaGestor(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoProfessorAvaliaTurma(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoProfessorAvaliaCurso(usuario));
//			sqlStr.append(" ) ");
            sqlStr.append(" AND ").append(getWherePublicoAlvoFuncionarioGestor(usuario));
        }
        if (visaoLogar.equals("funcionario") || visaoLogar.equals("loginAdministrador")) {
            sqlStr.append(" AND avaliacaoinstitucionalrespondente.pessoa = '" + usuario.getPessoa().getCodigo() + "'and avaliacaoinstitucionalrespondente.respondido = false ");
            sqlStr.append(" AND avaliacaoinstitucional.publicoalvo in ('DEPARTAMENTO_COORDENADORES', 'DEPARTAMENTO_DEPARTAMENTO', 'DEPARTAMENTO_CARGO', 'CARGO_DEPARTAMENTO', 'CARGO_CARGO', 'CARGO_COORDENADORES', 'FG', 'COLABORADORES_INSTITUICAO') ");
//			sqlStr.append(" AND ( ");
//			sqlStr.append(getWherePublicoAlvoFuncionarioAvaliaGestor(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoColaboradorAvaliaInstituicao(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoDepartamentoAvaliaCoordenador(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoDepartamentoAvaliaDepartamento(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoDepartamentoAvaliaCargo(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoCargoAvaliaDepartamento(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoCargoAvaliaCargo(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoCargoAvaliaCoordenador(usuario));
//			sqlStr.append(" ) ");
        }
        if (visaoLogar.equals("coordenador")) {
            sqlStr.append(" AND avaliacaoinstitucionalrespondente.pessoa = '" + usuario.getPessoa().getCodigo() + "'and avaliacaoinstitucionalrespondente.respondido = false ");
            sqlStr.append(" AND avaliacaoinstitucional.publicoalvo in ('CO', 'COORDENADORES_PROFESSOR', 'COORDENADORES_DEPARTAMENTO', 'COORDENADORES_CARGO', 'COORDENADORES_CURSO', 'COLABORADORES_INSTITUICAO') ");
//			sqlStr.append(" AND exists( ");
//			sqlStr.append(" select cursocoordenador.codigo from cursocoordenador inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
//			sqlStr.append(" inner join curso on curso.codigo = cursocoordenador.curso ");
//			sqlStr.append(" where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
//			sqlStr.append(" and ((unidadeensino.codigo is not null and cursocoordenador.unidadeensino = unidadeensino.codigo) or (unidadeensino.codigo is null)) ");
//			sqlStr.append(" and ((avaliacaoinstitucional.curso is not null and cursocoordenador.curso = avaliacaoinstitucional.curso) or (avaliacaoinstitucional.curso is null)) ");
//			sqlStr.append(" and ( exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and cursocoordenador.curso = avaliacaoinstitucionalcurso.curso) or (not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo limit 1 ))) ");
//			sqlStr.append(" and ((avaliacaoinstitucional.turma is not null and cursocoordenador.turma = avaliacaoinstitucional.turma) or (avaliacaoinstitucional.turma is null) or (avaliacaoinstitucional.turma is not null and cursocoordenador.turma is null and exists (select codigo from turma where turma.codigo = avaliacaoinstitucional.turma and turma.curso = cursocoordenador.curso))) ");
//			sqlStr.append(" and ((avaliacaoinstitucional.niveleducacional is not null and length(avaliacaoinstitucional.niveleducacional) > 0 and curso.niveleducacional = avaliacaoinstitucional.niveleducacional) or (avaliacaoinstitucional.niveleducacional is null) or (length(avaliacaoinstitucional.niveleducacional) = 0)) ");
//			sqlStr.append(" and (avaliacaoinstitucional.tipoCoordenadorCurso = '").append(TipoCoordenadorCursoEnum.AMBOS.name()).append("' or avaliacaoinstitucional.tipoCoordenadorCurso = cursocoordenador.tipoCoordenadorCurso ) ");
//			sqlStr.append(" limit 1) ");
//			sqlStr.append(" AND ( ");
//			sqlStr.append(getWherePublicoAlvoCoordenadorAvaliaCoordenador(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoCoordenadorAvaliaInstituicao(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoCoordenadorAvaliaCargo(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoFuncionarioAvaliaGestor(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoColaboradorAvaliaInstituicao(usuario));
//			sqlStr.append(" OR ");			
//			sqlStr.append(getWherePublicoAlvoCoordenadorAvaliaDepartamento(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoCoordenadorAvaliaProfessor(usuario));
//			sqlStr.append(" OR ");
//			sqlStr.append(getWherePublicoAlvoCoordenadorAvaliaCurso(usuario));
//			sqlStr.append(" ) ");
        }
        if (avaliacaoPresencial != null) {
            sqlStr.append(" AND avaliacaoinstitucional.avaliacaoPresencial = ").append(avaliacaoPresencial);
        }
        sqlStr.append(" ORDER BY avaliacaoinstitucional.codigo");
        //System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorUnidadeEnsinoDataSituacaoVisaoLogarUltimoModulo(
            Integer unidadeEnsino, Integer curso, Integer turma, Date data, String situacao, String visaoLogar,
            String nivelEducacional, Boolean avaliacaoPresencial, boolean possuiNaoPossuiUnidade, String matriculaAluno,
            boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE avaliacaoinstitucional.avaliacaoUltimoModulo = true ");
        if (possuiNaoPossuiUnidade) {
            if (unidadeEnsino != null && unidadeEnsino != 0) {
                sqlStr.append(" AND (unidadeEnsino.codigo = ").append(unidadeEnsino);
                sqlStr.append(" OR unidadeEnsino.codigo is Null)");
            }
        } else {
            if (unidadeEnsino != null && unidadeEnsino != 0) {
                sqlStr.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino);
            }
        }
        if (!situacao.equals("")) {
            sqlStr.append(" AND avaliacaoinstitucional.situacao = '").append(situacao).append("'");
        }
        if (visaoLogar.equals("aluno")) {
            sqlStr.append(" AND (avaliacaoinstitucional.publicoAlvo = '").append(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS.getValor()).append("'");
            sqlStr.append(" OR (avaliacaoinstitucional.publicoAlvo = '").append(PublicoAlvoAvaliacaoInstitucional.CURSO.getValor()).append("' and avaliacaoinstitucional.turma is not null and avaliacaoinstitucional.curso = " + curso + " ) ");
            sqlStr.append(" OR (avaliacaoinstitucional.publicoAlvo = '").append(PublicoAlvoAvaliacaoInstitucional.CURSO.getValor());
            sqlStr.append("' and avaliacaoinstitucional.turma is null and (exists(select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = " + curso + " ) or not exists(select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo))) ");
            sqlStr.append(" OR (avaliacaoinstitucional.publicoAlvo = '").append(PublicoAlvoAvaliacaoInstitucional.TURMA.getValor()).append("' and avaliacaoinstitucional.curso = " + curso + " ))");

            sqlStr.append(" AND exists (");
            sqlStr.append(" select matriculaperiodoturmadisciplina.codigo from matricula");
            sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo =  ");
            sqlStr.append(" (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
            sqlStr.append(" and mp.situacaomatriculaperiodo != 'PC'  ");
            sqlStr.append(" order by mp.ano||mp.semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc  limit 1) ");
            sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
            sqlStr.append(" inner join historico on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
            sqlStr.append(" inner join periodoauladisciplinaaluno(historico.codigo) as horario on professor_codigo is not null ");
            sqlStr.append(" where matricula.matricula = '").append(matriculaAluno).append("' ");
            sqlStr.append(" and (avaliacaoinstitucional.disciplina is null or (avaliacaoinstitucional.disciplina is not null and avaliacaoinstitucional.disciplina  = matriculaperiodoturmadisciplina.disciplina)) ");
            sqlStr.append(" and (avaliacaoinstitucional.unidadeensino is null or (avaliacaoinstitucional.unidadeensino is not null and avaliacaoinstitucional.unidadeensino  = matricula.unidadeensino)) ");
            sqlStr.append(" and (avaliacaoinstitucional.curso is null or (avaliacaoinstitucional.curso is not null and avaliacaoinstitucional.curso  = matricula.curso)) ");
//			sqlStr.append(" and (avaliacaoinstitucional.curso is null and avaliacaoinstitucional.turma is null  ");
//			sqlStr.append(" and (exists(select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
//			sqlStr.append(" and avaliacaoinstitucionalcurso.curso = curso.codigo ) or not exists(select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso ");
//			sqlStr.append(" where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo))) ");
            sqlStr.append(" and (avaliacaoinstitucional.turma is null or (avaliacaoinstitucional.turma is not null and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null and avaliacaoinstitucional.turma  = matriculaperiodoturmadisciplina.turma) or (avaliacaoinstitucional.turma is not null and matriculaperiodoturmadisciplina.turmapratica is not null and avaliacaoinstitucional.turma  = matriculaperiodoturmadisciplina.turmapratica) or (avaliacaoinstitucional.turma is not null and matriculaperiodoturmadisciplina.turmateorica is not null and avaliacaoinstitucional.turma  = matriculaperiodoturmadisciplina.turmateorica)) ");
            sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null ");
            sqlStr.append(" or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
            sqlStr.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo ) ");
            sqlStr.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");
            sqlStr.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
            sqlStr.append("   	and professor_codigo = avaliacaoinstitucionalpessoaavaliada.pessoa)) ");

            sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
            sqlStr.append("     and ( horario.datatermino::date + (avaliacaoinstitucional.diasdisponivel||' days')::interval + (avaliacaoinstitucional.horafim||' hours')::interval ) > (now()) ");
            sqlStr.append("     and (horario.datatermino::date + (avaliacaoinstitucional.horainicio||' hours')::interval) <= (now()) ");
            sqlStr.append("     and not exists (");
            sqlStr.append(" 	select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional   = avaliacaoinstitucional.codigo");
            sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.turma = matriculaperiodoturmadisciplina.turma	and respostaavaliacaoinstitucionaldw.matriculaaluno = matricula.matricula ");
            sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = matricula.aluno ");
            sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.disciplina = matriculaperiodoturmadisciplina.disciplina limit 1");
            sqlStr.append("    )");
            sqlStr.append(" limit 1) ");

        }
        if (visaoLogar.equals("professor")) {
            sqlStr.append(" AND (avaliacaoinstitucional.publicoAlvo = '")
                    .append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getValor()).append("')");
        }
        if (visaoLogar.equals("funcionario")) {
            sqlStr.append(" AND (avaliacaoinstitucional.publicoAlvo = '")
                    .append(PublicoAlvoAvaliacaoInstitucional.FUNCIONARIO_GESTOR.getValor()).append("')");
        }
        if (visaoLogar.equals("coordenador")) {
            sqlStr.append(" AND (avaliacaoinstitucional.publicoAlvo = '")
                    .append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES.getValor()).append("')");
        }
        if (avaliacaoPresencial != null) {
            sqlStr.append(" AND avaliacaoinstitucional.avaliacaoPresencial = ").append(avaliacaoPresencial);
        }
        if (nivelEducacional != null) {
            sqlStr.append(" AND avaliacaoinstitucional.nivelEducacional = '").append(nivelEducacional).append("'");
        }

        sqlStr.append(" ORDER BY avaliacaoinstitucional.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorPublicoAlvo(String valorConsulta, Integer limite,
                                                                       Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(lower(avaliacaoinstitucional.publicoAlvo)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio desc, avaliacaoinstitucional.publicoAlvo ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorDataInicio(Date dataInicio, Date dataFim, Integer limite,
                                                                      Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE avaliacaoinstitucional.dataInicio between'");
        sqlStr.append(Uteis.getDataJDBC(dataInicio));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio desc, avaliacaoinstitucional.dataInicio ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorDataFinal(Date dataInicio, Date dataFim, Integer limite,
                                                                     Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE avaliacaoinstitucional.dataFinal between'");
        sqlStr.append(Uteis.getDataJDBC(dataInicio));
        sqlStr.append("' AND '");
        sqlStr.append(Uteis.getDataJDBC(dataFim));
        sqlStr.append("'");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio desc, avaliacaoinstitucional.dataFinal ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorUnidadeEnsino(String nome, Boolean presencial,
                                                                         Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(sem_acentos(unidadeEnsino.nome)) like(sem_acentos('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%'))");
        if (presencial != null) {
            sqlStr.append(" AND avaliacaoinstitucional.avaliacaoPresencial = ").append(presencial);
        }
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio desc, unidadeEnsino.nome ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorCurso(String nome, Boolean presencial, Integer limite,
                                                                 Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(sem_acentos(curso.nome)) like(sem_acentos('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%'))");
        if (presencial != null) {
            sqlStr.append(" AND avaliacaoinstitucional.avaliacaoPresencial = ").append(presencial);
        }
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio, curso.nome ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorTurma(String nome, Boolean presencial, Integer limite,
                                                                 Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(sem_acentos(turma.identificadorTurma)) like(sem_acentos('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%'))");
        if (presencial != null) {
            sqlStr.append(" AND avaliacaoinstitucional.avaliacaoPresencial = ").append(presencial);
        }
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio, turma.identificadorTurma ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorDisciplina(String nome, Boolean presencial, Integer limite,
                                                                      Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(sem_acentos(disciplina.nome)) like(sem_acentos('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%'))");
        if (presencial != null) {
            sqlStr.append(" AND avaliacaoinstitucional.avaliacaoPresencial = ").append(presencial);
        }
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio, disciplina.nome ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorQuestionario(String nome, Boolean presencial, Integer limite,
                                                                        Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(sem_acentos(questionario.descricao)) like(sem_acentos('");
        sqlStr.append(nome.toUpperCase());
        sqlStr.append("%'))");
        if (presencial != null) {
            sqlStr.append(" AND avaliacaoinstitucional.avaliacaoPresencial = ").append(presencial);
        }
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio desc, questionario.descricao ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorSituacao(String situacao, Integer limite, Integer offset,
                                                                    boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(avaliacaoinstitucional.situacao) like('");
        sqlStr.append(situacao.toUpperCase());
        sqlStr.append("%')");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio desc, avaliacaoinstitucional.situacao ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> consultaRapidaPorSituacaoAtivaUltimoModuloPublicoAlvo(boolean aluno,
                                                                                                boolean professor, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE upper(avaliacaoinstitucional.situacao) like('AT') ");
        sqlStr.append(" and avaliacaoUltimoModulo = true");
        if (professor) {
            sqlStr.append(" and avaliacaoinstitucional.publicoAlvo = 'PR'");
        }
        if (aluno) {
            sqlStr.append(" and avaliacaoinstitucional.publicoAlvo = 'TC'");
        }
        sqlStr.append(
                " ORDER BY avaliacaoinstitucional.datainicio, avaliacaoinstitucional.situacao, avaliacaoinstitucional.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado);
    }

    public List<AvaliacaoInstitucionalVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado) throws Exception {
        List<AvaliacaoInstitucionalVO> vetResultado = new ArrayList<AvaliacaoInstitucionalVO>(0);
        while (tabelaResultado.next()) {
            AvaliacaoInstitucionalVO obj = new AvaliacaoInstitucionalVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    public void carregarDados(AvaliacaoInstitucionalVO obj, UsuarioVO usuario) throws Exception {
        carregarDados((AvaliacaoInstitucionalVO) obj, NivelMontarDados.TODOS, usuario);
    }

    /**
     * Método responsavel por validar se o Nivel de Montar Dados é Básico ou
     * Completo e faz a consulta de acordo com o nível especificado.
     *
     * @param obj
     * @param nivelMontarDados
     * @throws Exception
     * @author Carlos
     */
    public void carregarDados(AvaliacaoInstitucionalVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        SqlRowSet resultado = null;
        if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico((AvaliacaoInstitucionalVO) obj, resultado);
        }
        if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
            montarDadosCompleto((AvaliacaoInstitucionalVO) obj, resultado, usuario);
        }
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codAvaliacao, UsuarioVO usuario)
            throws Exception {
        consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (avaliacaoinstitucional.codigo= " + codAvaliacao + ")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codFuncionario, UsuarioVO usuario)
            throws Exception {
        consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE (avaliacaoinstitucional.codigo= " + codFuncionario + ")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private StringBuffer getSQLPadraoConsultaCompleta() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT avaliacaoinstitucional.codigo AS \"avaliacaoinstitucional.codigo\", avaliacaoinstitucional.nome AS \"avaliacaoinstitucional.nome\", ");
        str.append("avaliacaoInstitucional.publicoAlvo AS \"avaliacaoinstitucional.publicoAlvo\", avaliacaoInstitucional.nivelEducacional AS \"avaliacaoinstitucional.nivelEducacional\", avaliacaoInstitucional.ano AS \"avaliacaoinstitucional.ano\", avaliacaoInstitucional.semestre AS \"avaliacaoinstitucional.semestre\", dataInicio AS \"avaliacaoinstitucional.dataInicio\", ");
        str.append("avaliacaoinstitucional.dataFinal AS \"avaliacaoinstitucional.dataFinal\", avaliacaoinstitucional.descricao AS \"avaliacaoinstitucional.descricao\", avaliacaoinstitucional.mensagem AS \"avaliacaoinstitucional.mensagem\",");
        str.append("avaliacaoinstitucional.situacao AS \"avaliacaoinstitucional.situacao\", ");
        str.append("avaliacaoinstitucional.avaliacaoUltimoModulo AS \"avaliacaoinstitucional.avaliacaoUltimoModulo\", ");
        str.append("avaliacaoinstitucional.horaInicio AS \"avaliacaoinstitucional.horaInicio\", ");
        str.append("avaliacaoinstitucional.diasDisponivel AS \"avaliacaoinstitucional.diasDisponivel\", ");
        str.append("avaliacaoinstitucional.horaFim AS \"avaliacaoinstitucional.horaFim\", ");
        str.append("avaliacaoinstitucional.cargo AS \"avaliacaoinstitucional.cargo\", ");
        str.append("avaliacaoinstitucional.cargoavaliado AS \"avaliacaoinstitucional.cargoavaliado\", ");
        str.append("avaliacaoinstitucional.departamento AS \"avaliacaoinstitucional.departamento\", ");
        str.append("avaliacaoinstitucional.departamentoavaliado AS \"avaliacaoinstitucional.departamentoavaliado\", ");
        str.append("avaliacaoinstitucional.avaliacaoObrigatoria AS \"avaliacaoinstitucional.avaliacaoObrigatoria\", ");
        str.append("avaliacaoinstitucional.informarImportanciaPergunta AS \"avaliacaoinstitucional.informarImportanciaPergunta\", ");
        str.append("avaliacaoinstitucional.datainicioaula AS \"avaliacaoinstitucional.datainicioaula\", ");
        str.append("avaliacaoinstitucional.dataterminoaula AS \"avaliacaoinstitucional.dataterminoaula\", ");
        str.append("avaliacaoinstitucional.tipoFiltroProfessor AS \"avaliacaoinstitucional.tipoFiltroProfessor\", ");
        str.append("avaliacaoinstitucional.publicarResultadoRespondente AS \"avaliacaoinstitucional.publicarResultadoRespondente\", ");
        str.append("avaliacaoinstitucional.publicarResultadoAluno AS \"avaliacaoinstitucional.publicarResultadoAluno\", ");
        str.append("avaliacaoinstitucional.publicarResultadoProfessor AS \"avaliacaoinstitucional.publicarResultadoProfessor\", ");
        str.append("avaliacaoinstitucional.publicarResultadoCoordenador AS \"avaliacaoinstitucional.publicarResultadoCoordenador\", ");
        str.append("avaliacaoinstitucional.dataInicioPublicarResultado AS \"avaliacaoinstitucional.dataInicioPublicarResultado\", ");
        str.append("avaliacaoinstitucional.dataTerminoPublicarResultado AS \"avaliacaoinstitucional.dataTerminoPublicarResultado\", ");
        str.append("avaliacaoinstitucional.notificarRespondentes AS \"avaliacaoinstitucional.notificarRespondentes\", ");
        str.append("avaliacaoinstitucional.recorrenciaDiaNotificar AS \"avaliacaoinstitucional.recorrenciaDiaNotificar\", ");
        str.append("avaliacaoinstitucional.nivelDetalhamentoPublicarResultado AS \"avaliacaoinstitucional.nivelDetalhamentoPublicarResultado\", ");
        str.append("avaliacaoinstitucional.dataUltimaNotificacao AS \"avaliacaoinstitucional.dataUltimaNotificacao\", ");
        str.append("avaliacaoinstitucional.tipoCoordenadorCurso AS \"avaliacaoinstitucional.tipoCoordenadorCurso\", ");
        str.append("avaliacaoinstitucional.avaliardisciplinasreposicao AS \"avaliacaoinstitucional.avaliardisciplinasreposicao\", ");
        str.append("avaliacaoinstitucional.data AS \"avaliacaoinstitucional.data\", avaliacaoinstitucional.avaliacaoPresencial AS \"avaliacaoinstitucional.avaliacaoPresencial\", ");
        str.append("usuario.codigo AS \"responsavel.codigo\", usuario.nome AS \"responsavel.nome\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", ");
        str.append("unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", questionario.codigo AS \"questionario.codigo\", questionario.descricao AS \"questionario.descricao\", questionario.escopo AS \"questionario.escopo\", ");
        str.append("curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", turma.codigo AS \"turma.codigo\", turma.identificadorturma AS \"turma.identificadorturma\", ");
        str.append("disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\" ");
        str.append("from avaliacaoinstitucional ");
        str.append("LEFT JOIN unidadeEnsino ON unidadeEnsino.codigo = avaliacaoinstitucional.unidadeEnsino ");
        str.append("LEFT JOIN curso ON curso.codigo = avaliacaoinstitucional.curso ");
        str.append("LEFT JOIN turma ON turma.codigo = avaliacaoinstitucional.turma ");
        str.append("LEFT JOIN disciplina ON disciplina.codigo = avaliacaoinstitucional.disciplina ");
        str.append("INNER JOIN questionario ON questionario.codigo = avaliacaoinstitucional.questionario ");
        str.append("LEFT JOIN usuario ON usuario.codigo = avaliacaoinstitucional.responsavel ");

        return str;
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("SELECT avaliacaoinstitucional.codigo AS \"avaliacaoinstitucional.codigo\", avaliacaoinstitucional.nome AS \"avaliacaoinstitucional.nome\", ");
        str.append("avaliacaoInstitucional.publicoAlvo AS \"avaliacaoinstitucional.publicoAlvo\", avaliacaoInstitucional.nivelEducacional AS \"avaliacaoinstitucional.nivelEducacional\", avaliacaoInstitucional.ano AS \"avaliacaoinstitucional.ano\", avaliacaoInstitucional.semestre AS \"avaliacaoinstitucional.semestre\", dataInicio AS \"avaliacaoinstitucional.dataInicio\", ");
        str.append("avaliacaoinstitucional.situacao AS \"avaliacaoinstitucional.situacao\", avaliacaoinstitucional.dataFinal AS \"avaliacaoinstitucional.dataFinal\", ");
        str.append("unidadeEnsino.nome AS \"unidadeEnsino.nome\" , unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", ");
        str.append("avaliacaoinstitucional.avaliacaoUltimoModulo AS \"avaliacaoinstitucional.avaliacaoUltimoModulo\", ");
        str.append("avaliacaoinstitucional.horaInicio AS \"avaliacaoinstitucional.horaInicio\", ");
        str.append("avaliacaoinstitucional.cargo AS \"avaliacaoinstitucional.cargo\", ");
        str.append("avaliacaoinstitucional.cargoavaliado AS \"avaliacaoinstitucional.cargoavaliado\", ");
        str.append("avaliacaoinstitucional.departamento AS \"avaliacaoinstitucional.departamento\", ");
        str.append("avaliacaoinstitucional.departamentoavaliado AS \"avaliacaoinstitucional.departamentoavaliado\", ");
        str.append("avaliacaoinstitucional.diasDisponivel AS \"avaliacaoinstitucional.diasDisponivel\", ");
        str.append("avaliacaoinstitucional.horaFim AS \"avaliacaoinstitucional.horaFim\", ");
        str.append("avaliacaoinstitucional.datainicioaula AS \"avaliacaoinstitucional.datainicioaula\", ");
        str.append("avaliacaoinstitucional.dataterminoaula AS \"avaliacaoinstitucional.dataterminoaula\", ");
        str.append("avaliacaoinstitucional.tipoFiltroProfessor AS \"avaliacaoinstitucional.tipoFiltroProfessor\", ");
        str.append("avaliacaoinstitucional.publicarResultadoRespondente AS \"avaliacaoinstitucional.publicarResultadoRespondente\", ");
        str.append("avaliacaoinstitucional.publicarResultadoAluno AS \"avaliacaoinstitucional.publicarResultadoAluno\", ");
        str.append("avaliacaoinstitucional.publicarResultadoProfessor AS \"avaliacaoinstitucional.publicarResultadoProfessor\", ");
        str.append("avaliacaoinstitucional.publicarResultadoCoordenador AS \"avaliacaoinstitucional.publicarResultadoCoordenador\", ");
        str.append("avaliacaoinstitucional.dataInicioPublicarResultado AS \"avaliacaoinstitucional.dataInicioPublicarResultado\", ");
        str.append("avaliacaoinstitucional.dataTerminoPublicarResultado AS \"avaliacaoinstitucional.dataTerminoPublicarResultado\", ");
        str.append("avaliacaoinstitucional.notificarRespondentes AS \"avaliacaoinstitucional.notificarRespondentes\", ");
        str.append("avaliacaoinstitucional.recorrenciaDiaNotificar AS \"avaliacaoinstitucional.recorrenciaDiaNotificar\", ");
        str.append("avaliacaoinstitucional.nivelDetalhamentoPublicarResultado AS \"avaliacaoinstitucional.nivelDetalhamentoPublicarResultado\", ");
        str.append("avaliacaoinstitucional.dataUltimaNotificacao AS \"avaliacaoinstitucional.dataUltimaNotificacao\", ");
        str.append("avaliacaoinstitucional.tipoCoordenadorCurso AS \"avaliacaoinstitucional.tipoCoordenadorCurso\", ");
        str.append("avaliacaoinstitucional.avaliardisciplinasreposicao AS \"avaliacaoinstitucional.avaliardisciplinasreposicao\", ");
        str.append("avaliacaoinstitucional.avaliacaoObrigatoria AS \"avaliacaoinstitucional.avaliacaoObrigatoria\", questionario.codigo AS \"questionario.codigo\", ");
        str.append("questionario.descricao AS \"questionario.descricao\", questionario.escopo AS \"questionario.escopo\", avaliacaoinstitucional.avaliacaoPresencial AS \"avaliacaoinstitucional.avaliacaoPresencial\", ");
        str.append("curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", turma.codigo AS \"turma.codigo\", turma.identificadorturma AS \"turma.identificadorturma\", ");
        str.append("disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\" ");
        str.append("from avaliacaoinstitucional ");
        str.append("LEFT JOIN unidadeEnsino ON unidadeEnsino.codigo = avaliacaoinstitucional.unidadeEnsino ");
        str.append("LEFT JOIN curso ON curso.codigo = avaliacaoinstitucional.curso ");
        str.append("LEFT JOIN turma ON turma.codigo = avaliacaoinstitucional.turma ");
        str.append("LEFT JOIN disciplina ON disciplina.codigo = avaliacaoinstitucional.disciplina ");
        str.append("INNER JOIN questionario ON questionario.codigo = avaliacaoinstitucional.questionario ");

        return str;
    }

    private void montarDadosCompleto(AvaliacaoInstitucionalVO obj, SqlRowSet dadosSQL, UsuarioVO usuario)
            throws Exception {
        // Dados da Avaliação
        obj.setNovoObj(false);
        obj.setCodigo(new Integer(dadosSQL.getInt("avaliacaoinstitucional.codigo")));
        obj.setNome(dadosSQL.getString("avaliacaoinstitucional.nome"));
        obj.setPublicoAlvo(dadosSQL.getString("avaliacaoinstitucional.publicoAlvo"));
        obj.setData(dadosSQL.getDate("avaliacaoinstitucional.data"));
        obj.setNivelEducacional(dadosSQL.getString("avaliacaoinstitucional.nivelEducacional"));
        obj.setAno(dadosSQL.getString("avaliacaoinstitucional.ano"));
        obj.setSemestre(dadosSQL.getString("avaliacaoinstitucional.semestre"));
        obj.setAvaliacaoUltimoModulo(dadosSQL.getBoolean("avaliacaoinstitucional.avaliacaoUltimoModulo"));
        obj.setHoraInicio(dadosSQL.getString("avaliacaoinstitucional.horaInicio"));
        obj.setDiasDisponivel(dadosSQL.getInt("avaliacaoinstitucional.diasDisponivel"));
        obj.setHoraFim(dadosSQL.getString("avaliacaoinstitucional.horaFim"));
        obj.setDataInicio(dadosSQL.getDate("avaliacaoinstitucional.dataInicio"));
        obj.setDataFinal(dadosSQL.getDate("avaliacaoinstitucional.dataFinal"));
        obj.setDescricao(dadosSQL.getString("avaliacaoinstitucional.descricao"));
        obj.setMensagem(dadosSQL.getString("avaliacaoinstitucional.mensagem"));
        obj.setSituacao(dadosSQL.getString("avaliacaoinstitucional.situacao"));
        obj.setTipoFiltroProfessor(dadosSQL.getString("avaliacaoinstitucional.tipoFiltroProfessor"));
        obj.setAvaliacaoObrigatoria(dadosSQL.getBoolean("avaliacaoinstitucional.avaliacaoObrigatoria"));
        obj.setInformarImportanciaPergunta(dadosSQL.getBoolean("avaliacaoinstitucional.informarImportanciaPergunta"));
        obj.setAvaliacaoPresencial(dadosSQL.getBoolean("avaliacaoinstitucional.avaliacaoPresencial"));
        obj.getCargo().setCodigo(new Integer(dadosSQL.getInt("avaliacaoinstitucional.cargo")));
        obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("avaliacaoinstitucional.departamento")));
        obj.getCargoAvaliado().setCodigo(new Integer(dadosSQL.getInt("avaliacaoinstitucional.cargoavaliado")));
        obj.getDepartamentoAvaliado()
                .setCodigo(new Integer(dadosSQL.getInt("avaliacaoinstitucional.departamentoavaliado")));
        obj.setDataInicioAula(dadosSQL.getDate("avaliacaoinstitucional.datainicioaula"));
        obj.setDataTerminoAula(dadosSQL.getDate("avaliacaoinstitucional.dataterminoaula"));
        obj.setDataInicioPublicarResultado(dadosSQL.getDate("avaliacaoinstitucional.dataInicioPublicarResultado"));
        obj.setDataTerminoPublicarResultado(dadosSQL.getDate("avaliacaoinstitucional.dataTerminoPublicarResultado"));
        obj.setPublicarResultadoRespondente(dadosSQL.getBoolean("avaliacaoinstitucional.publicarResultadoRespondente"));
        obj.setPublicarResultadoAluno(dadosSQL.getBoolean("avaliacaoinstitucional.publicarResultadoAluno"));
        obj.setPublicarResultadoProfessor(dadosSQL.getBoolean("avaliacaoinstitucional.publicarResultadoProfessor"));
        obj.setPublicarResultadoCoordenador(dadosSQL.getBoolean("avaliacaoinstitucional.publicarResultadoCoordenador"));
        obj.setNotificarRespondentes(dadosSQL.getBoolean("avaliacaoinstitucional.notificarRespondentes"));
        obj.setRecorrenciaDiaNotificar(dadosSQL.getInt("avaliacaoinstitucional.recorrenciaDiaNotificar"));
        obj.setAvaliarDisciplinasReposicao(dadosSQL.getBoolean("avaliacaoinstitucional.avaliardisciplinasreposicao"));
        obj.setNivelDetalhamentoPublicarResultado(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum
                .valueOf(dadosSQL.getString("avaliacaoinstitucional.nivelDetalhamentoPublicarResultado")));
        obj.setTipoCoordenadorCurso(
                TipoCoordenadorCursoEnum.valueOf(dadosSQL.getString("avaliacaoinstitucional.tipoCoordenadorCurso")));
        // Dados da Unidade de Ensino
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));

        // Dados do Curso
        obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
        obj.getCurso().setNome(dadosSQL.getString("curso.nome"));

        // Dados da Turma
        obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));

        // Dados da Disciplina
        obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
        obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));

        // Dados do Responsável
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel.codigo"));
        obj.getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));

        // Dados do Questionário
        obj.getQuestionarioVO().setCodigo(dadosSQL.getInt("questionario.codigo"));
        obj.getQuestionarioVO().setDescricao(dadosSQL.getString("questionario.descricao"));
        obj.getQuestionarioVO().setEscopo(dadosSQL.getString("questionario.escopo"));

        obj.setListaAvaliacaoInstitucionalPessoaAvaliadaVOs(
                getFacadeFactory().getAvaliacaoInstitucionalPessoaAvaliadaFacade()
                        .consultarPorAvaliacaoInstitucional(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
        obj.setAvaliacaoInstitucionalCursoVOs(getFacadeFactory().getAvaliacaoInstitucionalCurso().consultarPorAvaliacaoInstitucional(obj.getCodigo(), usuario));
        obj.setAvaliacaoInstitucionalUnidadeEnsinoVOs(getFacadeFactory().getAvaliacaoInstitucionalUnidadeEnsino().consultarPorAvaliacaoInstitucional(obj.getCodigo(), usuario));
    }

    private void montarDadosBasico(AvaliacaoInstitucionalVO obj, SqlRowSet dadosSQL) throws Exception {
        // Dados da Avaliação
        obj.setNovoObj(false);
        obj.setCodigo(new Integer(dadosSQL.getInt("avaliacaoinstitucional.codigo")));
        obj.setNome(dadosSQL.getString("avaliacaoinstitucional.nome"));
        obj.setPublicoAlvo(dadosSQL.getString("avaliacaoinstitucional.publicoAlvo"));
        obj.setNivelEducacional(dadosSQL.getString("avaliacaoinstitucional.nivelEducacional"));
        obj.setTipoFiltroProfessor(dadosSQL.getString("avaliacaoinstitucional.tipoFiltroProfessor"));
        obj.setAno(dadosSQL.getString("avaliacaoinstitucional.ano"));
        obj.setSemestre(dadosSQL.getString("avaliacaoinstitucional.semestre"));
        obj.setAvaliacaoUltimoModulo(dadosSQL.getBoolean("avaliacaoinstitucional.avaliacaoUltimoModulo"));
        obj.setHoraInicio(dadosSQL.getString("avaliacaoinstitucional.horaInicio"));
        obj.setDiasDisponivel(dadosSQL.getInt("avaliacaoinstitucional.diasDisponivel"));
        obj.setHoraFim(dadosSQL.getString("avaliacaoinstitucional.horaFim"));
        obj.setDataInicio(dadosSQL.getDate("avaliacaoinstitucional.dataInicio"));
        obj.setDataFinal(dadosSQL.getDate("avaliacaoinstitucional.dataFinal"));
        obj.setSituacao(dadosSQL.getString("avaliacaoinstitucional.situacao"));
        obj.setAvaliacaoObrigatoria(dadosSQL.getBoolean("avaliacaoinstitucional.avaliacaoObrigatoria"));
        obj.setAvaliacaoPresencial(dadosSQL.getBoolean("avaliacaoinstitucional.avaliacaoPresencial"));
        obj.getCargo().setCodigo(new Integer(dadosSQL.getInt("avaliacaoinstitucional.cargo")));
        obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("avaliacaoinstitucional.departamento")));
        obj.getCargoAvaliado().setCodigo(new Integer(dadosSQL.getInt("avaliacaoinstitucional.cargoavaliado")));
        obj.getDepartamentoAvaliado()
                .setCodigo(new Integer(dadosSQL.getInt("avaliacaoinstitucional.departamentoavaliado")));
        obj.setDataInicioAula(dadosSQL.getDate("avaliacaoinstitucional.datainicioaula"));
        obj.setDataTerminoAula(dadosSQL.getDate("avaliacaoinstitucional.dataterminoaula"));
        obj.setDataInicioPublicarResultado(dadosSQL.getDate("avaliacaoinstitucional.dataInicioPublicarResultado"));
        obj.setDataTerminoPublicarResultado(dadosSQL.getDate("avaliacaoinstitucional.dataTerminoPublicarResultado"));
        obj.setPublicarResultadoRespondente(dadosSQL.getBoolean("avaliacaoinstitucional.publicarResultadoRespondente"));
        obj.setPublicarResultadoAluno(dadosSQL.getBoolean("avaliacaoinstitucional.publicarResultadoAluno"));
        obj.setPublicarResultadoProfessor(dadosSQL.getBoolean("avaliacaoinstitucional.publicarResultadoProfessor"));
        obj.setPublicarResultadoCoordenador(dadosSQL.getBoolean("avaliacaoinstitucional.publicarResultadoCoordenador"));
        obj.setNotificarRespondentes(dadosSQL.getBoolean("avaliacaoinstitucional.notificarRespondentes"));
        obj.setRecorrenciaDiaNotificar(dadosSQL.getInt("avaliacaoinstitucional.recorrenciaDiaNotificar"));
        obj.setDataUltimaNotificacao(dadosSQL.getDate("avaliacaoinstitucional.dataUltimaNotificacao"));
        obj.setAvaliarDisciplinasReposicao(dadosSQL.getBoolean("avaliacaoinstitucional.avaliardisciplinasreposicao"));
        obj.setNivelDetalhamentoPublicarResultado(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum
                .valueOf(dadosSQL.getString("avaliacaoinstitucional.nivelDetalhamentoPublicarResultado")));
        obj.setTipoCoordenadorCurso(
                TipoCoordenadorCursoEnum.valueOf(dadosSQL.getString("avaliacaoinstitucional.tipoCoordenadorCurso")));
        // Dados da Unidade de Ensino
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));

        // Dados do Curso
        obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
        obj.getCurso().setNome(dadosSQL.getString("curso.nome"));

        // Dados da Turma
        obj.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
        obj.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));

        // Dados da Disciplina
        obj.getDisciplina().setCodigo(dadosSQL.getInt("disciplina.codigo"));
        obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));

        // Dados do Questionário
        obj.getQuestionarioVO().setCodigo(dadosSQL.getInt("questionario.codigo"));
        obj.getQuestionarioVO().setDescricao(dadosSQL.getString("questionario.descricao"));
        obj.getQuestionarioVO().setEscopo(dadosSQL.getString("questionario.escopo"));
        if (obj.getAvaliacaoUltimoModulo()) {
            obj.setDataInicioAula(Uteis.obterDataAntiga(new Date(), obj.getDiasDisponivel()));
            obj.setDataTerminoAula(new Date());
        }

    }

    public void validarDadosAvaliacaoInstitucionalPessoaAvaliadaVO(AvaliacaoInstitucionalPessoaAvaliadaVO obj)
            throws Exception {
        if (!Uteis.isAtributoPreenchido(obj.getPessoaVO().getCodigo())) {
            throw new Exception("O campo Pessoa deve ser informado.");
        }
    }

    @Override
    public void addAvaliacaoInstitucionalPessoaAvaliadaVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO,
                                                          AvaliacaoInstitucionalPessoaAvaliadaVO avaliacaoInstitucionalPessoaAvaliada) throws Exception {
        validarDadosAvaliacaoInstitucionalPessoaAvaliadaVO(avaliacaoInstitucionalPessoaAvaliada);
        avaliacaoInstitucionalPessoaAvaliada.setAvaliacaoInstitucionalVO(avaliacaoInstitucionalVO);
        int index = 0;
        for (AvaliacaoInstitucionalPessoaAvaliadaVO objExistente : avaliacaoInstitucionalVO
                .getListaAvaliacaoInstitucionalPessoaAvaliadaVOs()) {
            if (objExistente.getPessoaVO().getCodigo()
                    .equals(avaliacaoInstitucionalPessoaAvaliada.getPessoaVO().getCodigo())) {
                avaliacaoInstitucionalVO.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs().set(index,
                        avaliacaoInstitucionalPessoaAvaliada);
                return;
            }
            index++;
        }
        avaliacaoInstitucionalVO.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs()
                .add(avaliacaoInstitucionalPessoaAvaliada);
        avaliacaoInstitucionalVO.getListaAvaliacaoInstitucionalPessoaAvaliadaVOs().sort((a1, a2) -> a1.getPessoaVO()
                .getNome().toLowerCase().compareTo(a2.getPessoaVO().getNome().toLowerCase()));
    }

    @Override
    public void removerAvaliacaoInstitucionalPessoaAvaliadaVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO,
                                                              AvaliacaoInstitucionalPessoaAvaliadaVO avaliacaoInstitucionalPessoaAvaliada) {
        Iterator<AvaliacaoInstitucionalPessoaAvaliadaVO> i = avaliacaoInstitucionalVO
                .getListaAvaliacaoInstitucionalPessoaAvaliadaVOs().iterator();
        while (i.hasNext()) {
            AvaliacaoInstitucionalPessoaAvaliadaVO objExistente = (AvaliacaoInstitucionalPessoaAvaliadaVO) i.next();
            if (objExistente.getPessoaVO().getCodigo()
                    .equals(avaliacaoInstitucionalPessoaAvaliada.getPessoaVO().getCodigo())) {
                i.remove();
                return;
            }
        }
    }

    public static StringBuilder getSqlHorarioAulaAluno() {
        StringBuilder sql = new StringBuilder(" and exists ( ");
        sql.append(getSqlHorarioAluno_DisciplinaNormal());
        sql.append(" union all ");
        sql.append(getSqlHorarioAluno_DisciplinaEquivalente());
        sql.append(" union all ");
        sql.append(getSqlHorarioAluno_DisciplinaEquivale());
        sql.append(" union all ");
        sql.append(getSqlHorarioAluno_DisciplinaEAD());
        sql.append(" ) ");
        return sql;
    }

    public static StringBuilder getSqlHorarioAluno_DisciplinaNormal() {
        StringBuilder sql = new StringBuilder("");
        sql.append(" ( ");
        sql.append("  select prof.codigo  ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append("  inner join disciplina as d on d.codigo = horarioturmadiaitem.disciplina");
        sql.append("  where d.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
        sql.append(
                "  and tur.turmaagrupada = false and (tur.subturma =  false or (tur.subturma and tur.tiposubturma = 'GERAL')) ");
        sql.append("  and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append(
                " and horarioturma.turma = matriculaPeriodoTurmaDisciplina.turma and matriculaPeriodoTurmaDisciplina.turmateorica is null and matriculaPeriodoTurmaDisciplina.turmapratica is null ");
        sql.append("  union all");
        sql.append("  select prof.codigo  ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append("  inner join disciplina as d on d.codigo = horarioturmadiaitem.disciplina");
        sql.append(
                "  where d.codigo = matriculaPeriodoTurmaDisciplina.disciplina and tur.subturma and tur.tiposubturma = 'PRATICA' ");
        sql.append("  and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append(" and horarioturma.turma =  matriculaPeriodoTurmaDisciplina.turmapratica  ");
        sql.append("  union all");
        sql.append("  select prof.codigo  ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append("  inner join disciplina as d on d.codigo = horarioturmadiaitem.disciplina");
        sql.append(
                "  where d.codigo = matriculaPeriodoTurmaDisciplina.disciplina and tur.subturma and tur.tiposubturma = 'TEORICA' ");
        sql.append("  and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append(" and horarioturma.turma =  matriculaPeriodoTurmaDisciplina.turmateorica ");
        sql.append(" union all");
        sql.append("  select prof.codigo  ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join turmaagrupada as ta on ta.turmaorigem = horarioturma.turma");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append("  inner join disciplina as d on d.codigo = horarioturmadiaitem.disciplina");
        sql.append("  where d.codigo = matriculaPeriodoTurmaDisciplina.disciplina and tur.subturma = false ");
        sql.append("  and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append(
                " and ta.turma = matriculaPeriodoTurmaDisciplina.turma and matriculaPeriodoTurmaDisciplina.turmateorica is null and matriculaPeriodoTurmaDisciplina.turmapratica is null ");

        sql.append("  union all");
        sql.append("  select prof.codigo  ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join turmaagrupada as ta on ta.turmaorigem = horarioturma.turma");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append("  inner join disciplina as d on d.codigo = horarioturmadiaitem.disciplina");
        sql.append(
                "  where d.codigo = matriculaPeriodoTurmaDisciplina.disciplina and tur.subturma and tur.tiposubturma = 'PRATICA' ");
        sql.append("  and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append(" and ta.turma = matriculaPeriodoTurmaDisciplina.turmapratica ");

        sql.append("  union all");
        sql.append("  select prof.codigo  ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join turmaagrupada as ta on ta.turmaorigem = horarioturma.turma");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append("  inner join disciplina as d on d.codigo = horarioturmadiaitem.disciplina");
        sql.append(
                "  where d.codigo = matriculaPeriodoTurmaDisciplina.disciplina and tur.subturma and tur.tiposubturma = 'TEORICA' ");
        sql.append("  and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral  and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append(" and ta.turma = matriculaPeriodoTurmaDisciplina.turmateorica ");

        sql.append("  limit 1");
        sql.append(" )");
        return sql;
    }

    public static StringBuilder getSqlHorarioAluno_DisciplinaEquivalente() {
        StringBuilder sql = new StringBuilder("");
        sql.append(" ( ");
        sql.append("  select prof.codigo ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join turmaagrupada as ta on ta.turmaorigem = tur.codigo ");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append(
                "  inner join disciplinaequivalente  on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina ");
        sql.append("  inner join disciplina as d on d.codigo = disciplinaequivalente.equivalente ");
        sql.append("  where tur.subturma = false ");
        sql.append("  and d.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append("  and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append(
                "  and ta.turma = matriculaPeriodoTurmaDisciplina.turma and matriculaPeriodoTurmaDisciplina.turmapratica  is null and matriculaPeriodoTurmaDisciplina.turmateorica is null ");
        sql.append("  union all ");
        sql.append("  select prof.codigo ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join turmaagrupada as ta on ta.turmaorigem = tur.codigo ");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append(
                "  inner join disciplinaequivalente  on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina ");
        sql.append("  inner join disciplina as d on d.codigo = disciplinaequivalente.equivalente ");
        sql.append("  where tur.subturma and tur.tiposubturma = 'PRATICA' ");
        sql.append("  and d.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append(" and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append(" and ta.turma = matriculaPeriodoTurmaDisciplina.turmapratica ");

        sql.append("  union all ");
        sql.append("  select prof.codigo ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join turmaagrupada as ta on ta.turmaorigem = tur.codigo ");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append(
                "  inner join disciplinaequivalente  on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina ");
        sql.append("  inner join disciplina as d on d.codigo = disciplinaequivalente.equivalente ");
        sql.append("  where tur.subturma and tur.tiposubturma = 'TEORICA' ");
        sql.append("  and d.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append(" and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append(" and ta.turma = matriculaPeriodoTurmaDisciplina.turmateorica ");

        sql.append("  limit 1");
        sql.append(" ) ");
        return sql;
    }

    public static StringBuilder getSqlHorarioAluno_DisciplinaEquivale() {
        StringBuilder sql = new StringBuilder("");
        sql.append(" ( ");
        sql.append("  select prof.codigo ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join turmaagrupada as ta on ta.turmaorigem = tur.codigo ");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append(
                "  inner join disciplinaequivalente  on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina ");
        sql.append("  inner join disciplina as d on d.codigo = disciplinaequivalente.disciplina");
        sql.append("  where tur.turmaagrupada ");
        sql.append("  and d.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append("  and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append(
                "  and ta.turma = matriculaPeriodoTurmaDisciplina.turma and matriculaPeriodoTurmaDisciplina.turmateorica is null and matriculaPeriodoTurmaDisciplina.turmapratica is null ");
        sql.append("  union all");
        sql.append("  select prof.codigo ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join turmaagrupada as ta on ta.turmaorigem = tur.codigo ");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append(
                "  inner join disciplinaequivalente  on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina ");
        sql.append("  inner join disciplina as d on d.codigo = disciplinaequivalente.disciplina");
        sql.append("  where tur.turmaagrupada and tur.subturma and tur.tiposubturma = 'PRATICA' ");
        sql.append("  and d.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append("  and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append(" and ta.turma = matriculaPeriodoTurmaDisciplina.turmapratica ");
        sql.append("  union all");
        sql.append("  select prof.codigo ");
        sql.append("  from horarioturma  ");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sql.append("  inner join turma as tur on tur.codigo = horarioturma.turma");
        sql.append("  inner join turmaagrupada as ta on ta.turmaorigem = tur.codigo ");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join pessoa as prof on prof.codigo = horarioturmadiaitem.professor");
        sql.append(
                "  inner join disciplinaequivalente  on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina ");
        sql.append("  inner join disciplina as d on d.codigo = disciplinaequivalente.disciplina");
        sql.append("  where tur.turmaagrupada and tur.subturma and tur.tiposubturma = 'TEORICA' ");
        sql.append("  and d.codigo = matriculaPeriodoTurmaDisciplina.disciplina ");
        sql.append("  and ((tur.anual and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano)");
        sql.append(
                "    or (tur.semestral and horarioturma.anovigente = matriculaPeriodoTurmaDisciplina.ano and horarioturma.semestrevigente = matriculaPeriodoTurmaDisciplina.semestre)");
        sql.append("    or (tur.anual = false and tur.semestral = false)");
        sql.append("   ) ");
        sql.append("  and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append(" and ta.turma = matriculaPeriodoTurmaDisciplina.turmateorica ");
        sql.append("  limit 1 ");
        sql.append(" ) ");
        return sql;
    }

    public static StringBuilder getSqlHorarioAluno_DisciplinaEAD() {
        StringBuilder sql = new StringBuilder("");
        sql.append(" 	(select prof.codigo as professor_codigo ");
        sql.append(" 	from turmadisciplina ");
        sql.append(" 	inner join disciplina as d on d.codigo = turmadisciplina.disciplina ");
        sql.append(" 	inner join turma as t on t.codigo = turmadisciplina.turma");
        sql.append(" 	inner join pessoa as prof on prof.codigo = matriculaPeriodoTurmaDisciplina.professor");
        sql.append(
                " 	where turmadisciplina.disciplina = matriculaPeriodoTurmaDisciplina.disciplina and turmadisciplina.definicoestutoriaonline = 'DINAMICA' ");
        sql.append("    and prof.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append(
                " 	and (( t.turmaagrupada = false and matriculaPeriodoTurmaDisciplina.turma = t.codigo) or (t.turmaagrupada and matriculaPeriodoTurmaDisciplina.turma in (select tu.codigo from turmaagrupada as ta inner join turma as tu on tu.codigo = ta.turma where ta.turmaorigem = t.codigo)))");
        sql.append(" 	limit 1) ");

        return sql;
    }

    public static StringBuilder getWherePublicoAlvoAlunoAvaliaTodosCurso(UsuarioVO usuario, String matricula)
            throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS.getValor()).append("' ");
        sqlStr.append("and exists (");
        sqlStr.append("select matricula.curso from matricula ");
        sqlStr.append("inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
        sqlStr.append("and matriculaperiodo.codigo = ( ");
        sqlStr.append("select mp.codigo from matriculaperiodo mp ");
        sqlStr.append("where mp.matricula = matricula.matricula ");
        if (Uteis.isAtributoPreenchido(matricula)) {
            sqlStr.append(" and matricula.matricula = '").append(matricula).append("'");
        }
        sqlStr.append("and mp.situacaomatriculaperiodo != 'PC' ");
        sqlStr.append("and case when length(avaliacaoinstitucional.ano) > 0 then avaliacaoinstitucional.ano = mp.ano else true end ");
        sqlStr.append("and case when length(avaliacaoinstitucional.semestre) > 0 then avaliacaoinstitucional.semestre = mp.semestre else true end  ");
        sqlStr.append("order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ");
        sqlStr.append("where matricula.aluno = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append("and avaliacaoinstitucional.unidadeensino = matricula.unidadeensino ");
        sqlStr.append("and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI', 'PR') ");
        sqlStr.append("and case when length(avaliacaoinstitucional.ano) > 0 then avaliacaoinstitucional.ano = matriculaperiodo.ano else true end ");
        sqlStr.append("and case when length(avaliacaoinstitucional.semestre) > 0 then avaliacaoinstitucional.semestre = matriculaperiodo.semestre else true end  ");
        sqlStr.append("and exists ( ");
        sqlStr.append("select historico.matriculaperiodo from matriculaperiodoturmadisciplina ");
        sqlStr.append("inner join historico on historico.matricula = matricula.matricula and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
        sqlStr.append("where historico.matriculaperiodo = matriculaperiodo.codigo ");

        // Carregar disciplinas ,caso esteja marcado avaliar disciplina trazer as de reposição
        sqlStr.append("     and ((avaliacaoinstitucional.avaliarDisciplinasReposicao = true) ");
        sqlStr.append("     or ( avaliacaoinstitucional.avaliarDisciplinasReposicao = false ");
        sqlStr.append(" 		and historico.tipohistorico not in ('AD', 'DE') ");
        sqlStr.append("     ))");

        sqlStr.append("and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null ");
        sqlStr.append("or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
        sqlStr.append("and ((matricula.gradecurricularatual = historico.matrizcurricular and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) ");
        sqlStr.append("and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina not in (select disciplina from historico his where his.matricula = historico.matricula ");
        sqlStr.append("and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.historicocursandoporcorrespondenciaapostransferencia ");
        sqlStr.append("and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and his.matrizcurricular != matricula.gradecurricularatual limit 1 )))) or (matricula.gradecurricularatual != historico.matrizcurricular ");
        sqlStr.append("and historico.historicocursandoporcorrespondenciaapostransferencia  and historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina = (select disciplina from historico his where his.matricula = historico.matricula ");
        sqlStr.append("and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula ");
        sqlStr.append("and (his.historicocursandoporcorrespondenciaapostransferencia is null or  his.historicocursandoporcorrespondenciaapostransferencia = false) and his.matrizcurricular = matricula.gradecurricularatual limit 1 ))) ");
        sqlStr.append(" and ( (not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo limit 1) ");
        sqlStr.append(" and (questionario.escopo != 'DI' or (questionario.escopo = 'DI' and exists (select horario.professor_codigo from periodoauladisciplinaaluno(historico.codigo) as horario ");
        sqlStr.append("	where horario.professor_codigo is not null)))) ");
        sqlStr.append(" or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");
        sqlStr.append(" where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo ").append(getSqlHorarioAulaAluno()).append(" limit 1) )");
        sqlStr.append(" and ((avaliacaoinstitucional.disciplina is null) or (avaliacaoinstitucional.disciplina  = historico.disciplina)) ");
        sqlStr.append("limit 1) ");
        sqlStr.append("limit 1)) ");
        return sqlStr;
    }

    public static StringBuilder getWherePublicoAlvoAlunoAvaliaCursoEspecifico(UsuarioVO usuario, Integer curso,
                                                                              String matricula) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.CURSO.getValor()).append("'");
        sqlStr.append(" 	and exists (select matricula.curso from matricula ");
        sqlStr.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = ( ");
        sqlStr.append(" 	select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
        if (Uteis.isAtributoPreenchido(matricula)) {
            sqlStr.append(" and matricula.matricula = '").append(matricula).append("'");
        }
        sqlStr.append(" 	and mp.situacaomatriculaperiodo != 'PC' ");
        sqlStr.append("     and case when length(avaliacaoinstitucional.ano) > 0 then avaliacaoinstitucional.ano = mp.ano else true end ");
        sqlStr.append("     and case when length(avaliacaoinstitucional.semestre) > 0 then avaliacaoinstitucional.semestre = mp.semestre else true end  ");
        sqlStr.append(" 	order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)");
        sqlStr.append(" 	inner join curso on curso.codigo = matricula.curso ");
        sqlStr.append(" 	where matricula.aluno = " + usuario.getPessoa().getCodigo() + " ");
        if (Uteis.isAtributoPreenchido(curso)) {
            sqlStr.append(" and matricula.curso = ").append(curso);
        }
        sqlStr.append(" 	and ((avaliacaoinstitucional.turma is not null and avaliacaoinstitucional.curso = matricula.curso) ");
        sqlStr.append(" 	or (avaliacaoinstitucional.turma is null and ( exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = matricula.curso )");
        sqlStr.append(" 	or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo ))))");
        sqlStr.append(" 	and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI', 'PR') ");
        sqlStr.append(" 	and avaliacaoinstitucional.unidadeensino = matricula.unidadeensino ");
        sqlStr.append(" 	and case when length(avaliacaoinstitucional.ano) > 0 then avaliacaoinstitucional.ano = matriculaperiodo.ano else true end ");
        sqlStr.append(" 	and case when length(avaliacaoinstitucional.semestre) > 0 then avaliacaoinstitucional.semestre = matriculaperiodo.semestre else true end ");
        sqlStr.append("     and exists ( ");
        sqlStr.append("     select historico.matriculaperiodo from matriculaperiodoturmadisciplina ");
        sqlStr.append("		inner join historico on historico.matricula = matricula.matricula and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
        sqlStr.append("		where historico.matriculaperiodo = matriculaperiodo.codigo ");

        // Carregar disciplinas ,caso esteja marcado avaliar disciplina trazer as de reposição
        sqlStr.append("     and ((avaliacaoinstitucional.avaliarDisciplinasReposicao = true) ");
        sqlStr.append("     or ( avaliacaoinstitucional.avaliarDisciplinasReposicao = false ");
        sqlStr.append(" 		and historico.tipohistorico not in ('AD', 'DE') ");
        sqlStr.append("     ))");

        sqlStr.append("   	and ((avaliacaoinstitucional.disciplina is null) or (avaliacaoinstitucional.disciplina  = historico.disciplina)) ");
        sqlStr.append("		and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null ");
        sqlStr.append("		or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
        sqlStr.append("		and ((matricula.gradecurricularatual = historico.matrizcurricular and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) ");
        sqlStr.append("		and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina not in (select disciplina from historico his where his.matricula = historico.matricula ");
        sqlStr.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.historicocursandoporcorrespondenciaapostransferencia ");
        sqlStr.append("		and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and his.matrizcurricular != matricula.gradecurricularatual limit 1 )))) or (matricula.gradecurricularatual != historico.matrizcurricular ");
        sqlStr.append("		and historico.historicocursandoporcorrespondenciaapostransferencia  and historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina = (select disciplina from historico his where his.matricula = historico.matricula ");
        sqlStr.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula ");
        sqlStr.append("		and (his.historicocursandoporcorrespondenciaapostransferencia is null or  his.historicocursandoporcorrespondenciaapostransferencia = false) and his.matrizcurricular = matricula.gradecurricularatual limit 1 ))) ");
        sqlStr.append(" 	and ( (not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo limit 1) ");
        sqlStr.append(" 	and (questionario.escopo != 'DI' or (questionario.escopo = 'DI' and exists (select horario.professor_codigo from periodoauladisciplinaaluno(historico.codigo) as horario ");
        sqlStr.append("		where horario.professor_codigo is not null)))) ");
        sqlStr.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");
        sqlStr.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo ").append(getSqlHorarioAulaAluno()).append(" limit 1) )");
        sqlStr.append("		limit 1) ");
        sqlStr.append(" 	limit 1 ");
        sqlStr.append(" )) ");
        return sqlStr;
    }

    public static StringBuilder getWherePublicoAlvoAlunoAvaliaTurmaEspecifica(UsuarioVO usuario, Integer curso,
                                                                              String matricula) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.TURMA.getValor()).append("' ");
        sqlStr.append(" 	and exists (select matricula.matricula from matricula ");
        sqlStr.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = ( ");
        sqlStr.append(" 	select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
        if (Uteis.isAtributoPreenchido(matricula)) {
            sqlStr.append(" and matricula.matricula = '").append(matricula).append("'");
        }
        sqlStr.append(" 	and mp.situacaomatriculaperiodo != 'PC' ");
        sqlStr.append("     and case when length(avaliacaoinstitucional.ano) > 0 then avaliacaoinstitucional.ano = mp.ano else true end ");
        sqlStr.append("     and case when length(avaliacaoinstitucional.semestre) > 0 then avaliacaoinstitucional.semestre = mp.semestre else true end  ");

        sqlStr.append(" 	order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)");
        sqlStr.append(" 	inner join curso on curso.codigo = matricula.curso ");
        sqlStr.append(" 	where matricula.aluno = " + usuario.getPessoa().getCodigo() + " ");
        if (Uteis.isAtributoPreenchido(curso)) {
            sqlStr.append(" and matricula.curso = ").append(curso);
        }
        sqlStr.append(" 	and avaliacaoinstitucional.curso = matricula.curso ");
        sqlStr.append(" 	and matriculaperiodo.turma = avaliacaoinstitucional.turma ");
        sqlStr.append(" 	and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI', 'PR') ");
        sqlStr.append(" 	and avaliacaoinstitucional.unidadeensino = matricula.unidadeensino ");
        sqlStr.append(" 	and case when length(avaliacaoinstitucional.ano) > 0 then avaliacaoinstitucional.ano = matriculaperiodo.ano else true end ");
        sqlStr.append(" 	and case when length(avaliacaoinstitucional.semestre) > 0 then avaliacaoinstitucional.semestre = matriculaperiodo.semestre else true end ");
        sqlStr.append("     and exists ( ");
        sqlStr.append("     select historico.matriculaperiodo from matriculaperiodoturmadisciplina  ");
        sqlStr.append("		inner join historico on historico.matricula = matricula.matricula and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
        sqlStr.append("		where historico.matriculaperiodo = matriculaperiodo.codigo ");

        // Carregar disciplinas ,caso esteja marcado avaliar disciplina trazer as de reposição
        sqlStr.append("     and ((avaliacaoinstitucional.avaliarDisciplinasReposicao = true) ");
        sqlStr.append("     or ( avaliacaoinstitucional.avaliarDisciplinasReposicao = false ");
        sqlStr.append(" 		and historico.tipohistorico not in ('AD', 'DE') ");
        sqlStr.append("     ))");

        sqlStr.append("   	and ((avaliacaoinstitucional.disciplina is null) or (avaliacaoinstitucional.disciplina  = historico.disciplina)) ");
        sqlStr.append("		and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null ");
        sqlStr.append("		or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
        sqlStr.append("		and ((matricula.gradecurricularatual = historico.matrizcurricular and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) ");
        sqlStr.append("		and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina not in (select disciplina from historico his where his.matricula = historico.matricula ");
        sqlStr.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.historicocursandoporcorrespondenciaapostransferencia ");
        sqlStr.append("		and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and his.matrizcurricular != matricula.gradecurricularatual limit 1 )))) or (matricula.gradecurricularatual != historico.matrizcurricular ");
        sqlStr.append("		and historico.historicocursandoporcorrespondenciaapostransferencia  and historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina = (select disciplina from historico his where his.matricula = historico.matricula ");
        sqlStr.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula ");
        sqlStr.append("		and (his.historicocursandoporcorrespondenciaapostransferencia is null or  his.historicocursandoporcorrespondenciaapostransferencia = false) and his.matrizcurricular = matricula.gradecurricularatual limit 1 ))) ");
        sqlStr.append(" 	and ( (not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo limit 1) ");
        sqlStr.append(" 	and (questionario.escopo != 'DI' or (questionario.escopo = 'DI' and exists (select horario.professor_codigo from periodoauladisciplinaaluno(historico.codigo) as horario ");
        sqlStr.append("		where horario.professor_codigo is not null)))) ");
        sqlStr.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");
        sqlStr.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo ").append(getSqlHorarioAulaAluno()).append(" limit 1) )");
        sqlStr.append("		limit 1) ");
        sqlStr.append(" 	limit 1 ");
        sqlStr.append(" )) ");
        return sqlStr;
    }

    public static StringBuilder getWherePublicoAlvoAlunoAvaliaCoordenador(UsuarioVO usuario, Integer curso,
                                                                          String matricula) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.ALUNO_COORDENADOR.getValor()).append("'");
        sqlStr.append(" 	and exists (select matricula.matricula from matricula ");
        sqlStr.append(" 	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = ( ");
        sqlStr.append(" 	select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
        if (Uteis.isAtributoPreenchido(matricula)) {
            sqlStr.append(" and matricula.matricula = '").append(matricula).append("'");
        }
        sqlStr.append(" 	and mp.situacaomatriculaperiodo != 'PC' ");
        sqlStr.append("     and case when length(avaliacaoinstitucional.ano) > 0 then avaliacaoinstitucional.ano = mp.ano else true end ");
        sqlStr.append("     and case when length(avaliacaoinstitucional.semestre) > 0 then avaliacaoinstitucional.semestre = mp.semestre else true end  ");
        sqlStr.append(" 	and ((avaliacaoinstitucional.turma is not null and avaliacaoinstitucional.turma = mp.turma) or (avaliacaoinstitucional.turma is null)) ");
        sqlStr.append(" 	order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ");


        sqlStr.append(" 	inner join curso on curso.codigo = matricula.curso ");
        sqlStr.append(" 	where matricula.aluno = " + usuario.getPessoa().getCodigo() + " ");
        if (Uteis.isAtributoPreenchido(curso)) {
            sqlStr.append(" and matricula.curso = ").append(curso);
        }
        sqlStr.append(" 	and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI', 'PR') ");
        sqlStr.append(" 	and avaliacaoinstitucional.unidadeensino = matricula.unidadeensino ");
        sqlStr.append(" 	and case when length(avaliacaoinstitucional.ano) > 0 then avaliacaoinstitucional.ano = matriculaperiodo.ano else true end ");
        sqlStr.append(" 	and case when length(avaliacaoinstitucional.semestre) > 0 then avaliacaoinstitucional.semestre = matriculaperiodo.semestre else true end ");
        sqlStr.append("     and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = matricula.curso) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
        sqlStr.append(" 	and ((avaliacaoinstitucional.turma is not null and avaliacaoinstitucional.turma = matriculaperiodo.turma) or (avaliacaoinstitucional.turma is null)) ");
        sqlStr.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo limit 1) ");
        sqlStr.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");
        sqlStr.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append("   	and exists (select cursocoordenador.codigo from cursocoordenador  ");
        sqlStr.append("   	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
        sqlStr.append("   	inner join pessoa as coord on funcionario.pessoa = coord.codigo ");
        sqlStr.append("   	where (cursocoordenador.unidadeensino is null or  cursocoordenador.unidadeensino = matricula.unidadeensino) ");
        sqlStr.append("   	and coord.codigo = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sqlStr.append("   	and coord.ativo ");
        sqlStr.append("   	and (avaliacaoinstitucional.tipoCoordenadorCurso = '").append(TipoCoordenadorCursoEnum.AMBOS.name()).append("' or avaliacaoinstitucional.tipoCoordenadorCurso = cursocoordenador.tipoCoordenadorCurso ) ");
        sqlStr.append("   	and cursocoordenador.curso = matricula.curso ");
        sqlStr.append("   	and ((cursocoordenador.turma is not null and  cursocoordenador.turma = matriculaperiodo.turma ) or (cursocoordenador.turma is null)) ");
        sqlStr.append("   	limit 1) )) ");

        sqlStr.append("   	and exists ( select cursocoordenador.codigo from cursocoordenador  ");
        sqlStr.append("   	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
        sqlStr.append("   	inner join pessoa as coord on funcionario.pessoa = coord.codigo ");
        sqlStr.append("   	where (cursocoordenador.unidadeensino is null or cursocoordenador.unidadeensino = matricula.unidadeensino) ");
        sqlStr.append("   	and coord.ativo ");
        sqlStr.append("   	and cursocoordenador.curso = matricula.curso ");
        sqlStr.append("     and (avaliacaoInstitucional.tipoCoordenadorCurso is null or avaliacaoInstitucional.tipoCoordenadorCurso = '").append(TipoCoordenadorCursoEnum.AMBOS).append("' or cursocoordenador.tipoCoordenadorCurso = avaliacaoInstitucional.tipoCoordenadorCurso)  ");
        sqlStr.append("   	and ((cursocoordenador.turma is not null and  cursocoordenador.turma = matriculaperiodo.turma ) or (cursocoordenador.turma is null)) ");
        sqlStr.append("   	limit 1 )");

        sqlStr.append("     and ((avaliacaoinstitucional.disciplina is not null and exists ( ");
        sqlStr.append("     select historico.matriculaperiodo from matriculaperiodoturmadisciplina ");
        sqlStr.append("		inner join historico on historico.matricula = matricula.matricula and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
        sqlStr.append("		where historico.matriculaperiodo = matriculaperiodo.codigo ");
        sqlStr.append("   	and avaliacaoinstitucional.disciplina  = historico.disciplina ");
        sqlStr.append("		and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null ");
        sqlStr.append("		or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
        sqlStr.append("		and ((matricula.gradecurricularatual = historico.matrizcurricular and (historico.historicocursandoporcorrespondenciaapostransferencia is null or historico.historicocursandoporcorrespondenciaapostransferencia = false) ");
        sqlStr.append("		and (historico.transferenciamatrizcurricularmatricula IS NULL OR (historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina not in (select disciplina from historico his where his.matricula = historico.matricula ");
        sqlStr.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.historicocursandoporcorrespondenciaapostransferencia ");
        sqlStr.append("		and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula and his.matrizcurricular != matricula.gradecurricularatual limit 1 )))) or (matricula.gradecurricularatual != historico.matrizcurricular ");
        sqlStr.append("		and historico.historicocursandoporcorrespondenciaapostransferencia  and historico.transferenciamatrizcurricularmatricula IS NOT NULL  and historico.disciplina = (select disciplina from historico his where his.matricula = historico.matricula ");
        sqlStr.append("		and his.anohistorico = historico.anohistorico and his.semestrehistorico = historico.semestrehistorico and his.disciplina = historico.disciplina and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula ");
        sqlStr.append("		and (his.historicocursandoporcorrespondenciaapostransferencia is null or  his.historicocursandoporcorrespondenciaapostransferencia = false) and his.matrizcurricular = matricula.gradecurricularatual limit 1 ))) ");
        sqlStr.append("		limit 1)) or (avaliacaoinstitucional.disciplina is null)) ");


        sqlStr.append(" 	limit 1 ");
        sqlStr.append(" )) ");
        return sqlStr;
    }

    public StringBuilder getWherePublicoAlvoProfessorAvaliaInstituicao(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getValor()).append("' ");
        sqlStr.append(" and not exists(select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append(" and respostaavaliacaoinstitucionaldw.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo())
                .append(" limit 1) ");
        sqlStr.append(" 	and exists (select distinct horarioturmadiaitem.professor as professor from horarioturma ");
        sqlStr.append(" 	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
        sqlStr.append(" 	inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
        sqlStr.append(" 	inner join turma on turma.codigo = horarioturma.turma  ");
        sqlStr.append(" 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
        sqlStr.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
        sqlStr.append(" 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
        sqlStr.append(" 	where turma.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 	and horarioturmadiaitem.professor = ").append(usuario.getPessoa().getCodigo()).append(" ");
        sqlStr.append(" and ((avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo))) ");
        sqlStr.append("	or (avaliacaoinstitucional.turma is not null and  (turma.codigo = avaliacaoinstitucional.turma or (turma.turmaagrupada and avaliacaoinstitucional.turma in (select turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo) )))) ");
        sqlStr.append(" 	and case when length(avaliacaoinstitucional.niveleducacional) > 0 then  curso.niveleducacional = avaliacaoinstitucional.niveleducacional else true end ");
        sqlStr.append(" 	and ( case when length(avaliacaoinstitucional.ano) = 4 and length(avaliacaoinstitucional.semestre) = 1  then horarioturma.anovigente = avaliacaoinstitucional.ano and horarioturma.semestrevigente = avaliacaoinstitucional.semestre else ");
        sqlStr.append(" 	case when length(avaliacaoinstitucional.ano) = 4 then horarioturma.anovigente = avaliacaoinstitucional.ano else ");
        sqlStr.append(" 	(horarioturmadia.data >= avaliacaoinstitucional.datainicioaula and horarioturmadia.data <= avaliacaoinstitucional.dataterminoaula) end end ) ");
        sqlStr.append("  limit 1) ");
        sqlStr.append(" ) ");

        return sqlStr;
    }

    public StringBuilder getWherePublicoAlvoFuncionarioGestor(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (select case ");
        sqlStr.append(" when funcionario.exercecargoadministrativo and avaliacaoinstitucional.publicoAlvo = 'FG' then true");
        sqlStr.append(" when not funcionario.exercecargoadministrativo and avaliacaoinstitucional.publicoAlvo = 'FG' then false");
        sqlStr.append(" when funcionario.exercecargoadministrativo and avaliacaoinstitucional.publicoAlvo != 'FG' then true ");
        sqlStr.append(" else true end");
        sqlStr.append(" from pessoa inner join funcionario on funcionario.pessoa = pessoa.codigo");
        sqlStr.append(" where pessoa.codigo = ").append(usuario.getPessoa().getCodigo()).append(" limit 1)");

        return sqlStr;
    }

    public StringBuilder getWherePublicoAlvoProfessorAvaliaTurma(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA.getValor()).append("' ");
        sqlStr.append(" 	and exists (select distinct horarioturmadiaitem.professor as professor from horarioturma ");
        sqlStr.append(" 	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
        sqlStr.append(" 	inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
        sqlStr.append(" 	inner join turma on turma.codigo = horarioturma.turma  ");
        sqlStr.append(" 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
        sqlStr.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
        sqlStr.append(" 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
        sqlStr.append(" 	where turma.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 	and horarioturmadiaitem.professor = ").append(usuario.getPessoa().getCodigo()).append(" ");
        sqlStr.append(" and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
        sqlStr.append(" 	and case when avaliacaoinstitucional.disciplina is not null then  horarioturmadiaitem.disciplina = avaliacaoinstitucional.disciplina else true end ");
        sqlStr.append(" 	and case when avaliacaoinstitucional.turma is not null then  (turma.codigo = avaliacaoinstitucional.turma or (turma.turmaagrupada and avaliacaoinstitucional.turma in (select turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo) ))  else true end ");
        sqlStr.append(" 	and case when length(avaliacaoinstitucional.niveleducacional) > 0 then  curso.niveleducacional = avaliacaoinstitucional.niveleducacional else true end ");
        sqlStr.append("     and not exists(select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append("     and respostaavaliacaoinstitucionaldw.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append("     and respostaavaliacaoinstitucionaldw.turma = turma.codigo ");
        sqlStr.append("     and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo())
                .append(" limit 1) ");
        sqlStr.append(" 	and ( case when length(avaliacaoinstitucional.ano) = 4 and length(avaliacaoinstitucional.semestre) = 1  then horarioturma.anovigente = avaliacaoinstitucional.ano and horarioturma.semestrevigente = avaliacaoinstitucional.semestre else ");
        sqlStr.append(" 	case when length(avaliacaoinstitucional.ano) = 4 then horarioturma.anovigente = avaliacaoinstitucional.ano else ");
        sqlStr.append(" 	(horarioturmadia.data >= avaliacaoinstitucional.datainicioaula and horarioturmadia.data <= avaliacaoinstitucional.dataterminoaula) end end ) ");
        sqlStr.append("  limit 1) ");
        sqlStr.append(" ) ");

        return sqlStr;
    }

    public StringBuilder getWherePublicoAlvoProfessorAvaliaCurso(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_CURSO.getValor()).append("' ");
        sqlStr.append(" 	and exists (select distinct horarioturmadiaitem.professor as professor from horarioturma ");
        sqlStr.append(" 	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
        sqlStr.append(" 	inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
        sqlStr.append(" 	inner join turma on turma.codigo = horarioturma.turma  ");
        sqlStr.append(" 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
        sqlStr.append(" 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
        sqlStr.append(" 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
        sqlStr.append(" 	where turma.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 	and horarioturmadiaitem.professor = ").append(usuario.getPessoa().getCodigo()).append(" ");
        sqlStr.append(" and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
        sqlStr.append(" 	and case when avaliacaoinstitucional.disciplina is not null then  horarioturmadiaitem.disciplina = avaliacaoinstitucional.disciplina else true end ");
        sqlStr.append(" 	and case when avaliacaoinstitucional.turma is not null then  (turma.codigo = avaliacaoinstitucional.turma or (turma.turmaagrupada and avaliacaoinstitucional.turma in (select turma from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo) ))  else true end ");
        sqlStr.append(" 	and case when length(avaliacaoinstitucional.niveleducacional) > 0 then  curso.niveleducacional = avaliacaoinstitucional.niveleducacional else true end ");
        sqlStr.append("     and not exists(select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append("     and respostaavaliacaoinstitucionaldw.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append("     and respostaavaliacaoinstitucionaldw.curso = curso.codigo ");
        sqlStr.append("     and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo())
                .append(" limit 1) ");
        sqlStr.append(" 	and ( case when length(avaliacaoinstitucional.ano) = 4 and length(avaliacaoinstitucional.semestre) = 1  then horarioturma.anovigente = avaliacaoinstitucional.ano and horarioturma.semestrevigente = avaliacaoinstitucional.semestre else ");
        sqlStr.append(" 	case when length(avaliacaoinstitucional.ano) = 4 then horarioturma.anovigente = avaliacaoinstitucional.ano else ");
        sqlStr.append(" 	(horarioturmadia.data >= avaliacaoinstitucional.datainicioaula and horarioturmadia.data <= avaliacaoinstitucional.dataterminoaula) end end ) ");
        sqlStr.append("  limit 1) ");
        sqlStr.append(" ) ");

        return sqlStr;
    }

    /*
     * Esta já verifica se é uma avaliacao de professor avaliando coordenado e se
     * existe ainda algum coordenador que ele não respondeu a avaliacao
     * institucional validando a seguinte regra: se na avaliacao institucional
     * estiver informado ano e semestre irá considerar os professores deste período
     * porém se não tiver informado será considerado apenas os professor que dão
     * aula no período da avaliação
     */
    public StringBuilder getWherePublicoAlvoProfessorAvaliaCoordenador(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES_COORDENADORES.getValor()).append("' ");
        sqlStr.append(" 	and exists (select funcionario.pessoa as coordenador from horarioturma ");
        sqlStr.append(" 	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
        sqlStr.append(
                " 	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
        sqlStr.append(" 	and horarioturmadiaitem.professor = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(" 	inner join turma on turma.codigo = horarioturma.turma  ");
        sqlStr.append(
                " 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
        sqlStr.append(
                " 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
        sqlStr.append(
                " 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
        sqlStr.append(
                " 	inner join cursocoordenador on curso.codigo = cursocoordenador.curso and (cursocoordenador.unidadeensino = 0 or cursocoordenador.unidadeensino = turma.unidadeensino) ");
        sqlStr.append(
                " 	and case when cursocoordenador.turma is not null then cursocoordenador.turma = turma.codigo else true end ");
        sqlStr.append("	 	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
        sqlStr.append("	 	inner join pessoa on funcionario.pessoa = pessoa.codigo ");
        sqlStr.append(" 	where turma.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(
                " 	and case when length(avaliacaoinstitucional.niveleducacional) > 0 then  curso.niveleducacional = avaliacaoinstitucional.niveleducacional else true end ");
        sqlStr.append(" and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
        sqlStr.append(" 	and pessoa.ativo = true ");
        sqlStr.append("   	and (avaliacaoinstitucional.tipoCoordenadorCurso = '")
                .append(TipoCoordenadorCursoEnum.AMBOS.name())
                .append("' or avaliacaoinstitucional.tipoCoordenadorCurso = cursocoordenador.tipoCoordenadorCurso ) ");
        sqlStr.append(" 	and pessoa.codigo != ").append(usuario.getPessoa().getCodigo()).append(" ");
        sqlStr.append(
                " 	and ( case when length(avaliacaoinstitucional.ano) = 4 and length(avaliacaoinstitucional.semestre) = 1  then horarioturma.anovigente = avaliacaoinstitucional.ano and horarioturma.semestrevigente = avaliacaoinstitucional.semestre else ");
        sqlStr.append(
                " 	case when length(avaliacaoinstitucional.ano) = 4 then horarioturma.anovigente = avaliacaoinstitucional.ano else ");
        sqlStr.append(
                " 	(horarioturmadia.data >= avaliacaoinstitucional.datainicioaula and horarioturmadia.data <= avaliacaoinstitucional.dataterminoaula) end end ) ");
        sqlStr.append(
                " 	and not exists (select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw ");
        sqlStr.append(
                " 	where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa =").append(usuario.getPessoa().getCodigo())
                .append(" ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.coordenador = funcionario.pessoa limit 1) ");
        sqlStr.append(" limit 1 ) ) ");
        return sqlStr;
    }

    /*
     * Este verifica se existe uma avaliacao institucional para os colaborador
     * avaliando a instituicao
     */
    public StringBuilder getWherePublicoAlvoColaboradorAvaliaInstituicao(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.COLABORADORES_INSTITUICAO.getValor()).append("' ");
        sqlStr.append(" 	and (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo())
                .append(" limit 1) is null ");
        sqlStr.append(" AND ( exists (select distinct funcionariocargo.codigo  from funcionariocargo ");
        sqlStr.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo()).append(" and unidadeEnsino.codigo = funcionariocargo.unidadeensino  and funcionariocargo.ativo )");
        sqlStr.append(" 	) ");
        sqlStr.append(" 	) ");
        return sqlStr;
    }

    public StringBuilder getWherePublicoAlvoFuncionarioAvaliaGestor(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.FUNCIONARIO_GESTOR.getValor()).append("' ");
        sqlStr.append(" 	and (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo()).append(" limit 1 ) is null ");
        if (usuario.getIsApresentarVisaoProfessor() || usuario.getIsApresentarVisaoCoordenador()) {
            sqlStr.append(" AND ( exists (select distinct funcionariocargo.codigo  from funcionariocargo ");
            sqlStr.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario where funcionario.pessoa = ").append(usuario.getPessoa().getCodigo()).append(" and unidadeEnsino.codigo = funcionariocargo.unidadeensino  and funcionariocargo.ativo )");
            sqlStr.append(" ) ");
        }

        sqlStr.append(" ) ");
        return sqlStr;
    }

    /**
     * Verifica se a avaliação é do departamento do usuário logado e se existe algum
     * coordenador que ainda não realizou a avaliação
     */
    public StringBuilder getWherePublicoAlvoDepartamentoAvaliaCoordenador(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_COORDENADORES.getValor()).append("' ");
        sqlStr.append(" 	and ( ");
        sqlStr.append(" 		select distinct coordenador.codigo from funcionariocargo");
        sqlStr.append(" 		inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
        sqlStr.append(" 		inner join cargo on cargo.codigo = funcionariocargo.cargo");
        sqlStr.append(
                " 		inner join cursocoordenador on (cursocoordenador.unidadeensino = 0 or cursocoordenador.unidadeensino = funcionariocargo.unidadeensino)");
        sqlStr.append(" 		inner join curso on cursocoordenador.curso = curso.codigo");
        sqlStr.append(
                " 		inner join funcionario coordenador on cursocoordenador.funcionario = coordenador.codigo");
        sqlStr.append(" 		inner join pessoa on pessoa.codigo = coordenador.pessoa");
        sqlStr.append(
                " 		left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append(" 		and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
        sqlStr.append(" 		and respostaavaliacaoinstitucionaldw.coordenador = coordenador.pessoa");
        sqlStr.append(" 		where funcionariocargo.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 		and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(
                " 	    and case when length(avaliacaoinstitucional.niveleducacional) > 0 then  curso.niveleducacional = avaliacaoinstitucional.niveleducacional else true end ");
        sqlStr.append(" and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
        sqlStr.append(" 		and cargo.departamento = avaliacaoinstitucional.departamento ");
        sqlStr.append(" 		and pessoa.ativo = true and funcionariocargo.ativo ");
        sqlStr.append("   	and (avaliacaoinstitucional.tipoCoordenadorCurso = '")
                .append(TipoCoordenadorCursoEnum.AMBOS.name())
                .append("' or avaliacaoinstitucional.tipoCoordenadorCurso = cursocoordenador.tipoCoordenadorCurso ) ");
        sqlStr.append(
                " 	    and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo limit 1) ");
        sqlStr.append(" 	    or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");
        sqlStr.append(
                "   	    where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalpessoaavaliada.pessoa = pessoa.codigo )) ");
        sqlStr.append(" 		and pessoa.codigo != ").append(usuario.getPessoa().getCodigo()).append(" ");
        sqlStr.append(" 		and respostaavaliacaoinstitucionaldw.codigo is null limit 1");
        sqlStr.append(" 	) is not null ) ");
        return sqlStr;
    }

    /**
     * Verifica se a avaliação é do departamento do usuário logado e se existe algum
     * departamento que ainda não realizou a avaliação
     */
    public StringBuilder getWherePublicoAlvoDepartamentoAvaliaDepartamento(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_DEPARTAMENTO.getValor()).append("' ");
        sqlStr.append(" 	and ( ");
        sqlStr.append(" 	select distinct departamento.codigo from funcionariocargo");
        sqlStr.append(" 	inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
        sqlStr.append(" 	inner join cargo on cargo.codigo = funcionariocargo.cargo");
        sqlStr.append(
                " 	inner join departamento on case when avaliacaoinstitucional.departamentoavaliado > 0 then departamento.codigo = avaliacaoinstitucional.departamentoavaliado");
        sqlStr.append(
                " 	else departamento.codigo in (select codigo from departamento where departamento.unidadeensino is null or departamento.unidadeensino = avaliacaoinstitucional.unidadeensino) end");
        sqlStr.append(
                " 	left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.departamento = departamento.codigo");
        sqlStr.append(" 	where funcionariocargo.unidadeensino = avaliacaoinstitucional.unidadeensino");
        sqlStr.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(" 	and cargo.departamento = avaliacaoinstitucional.departamento and funcionariocargo.ativo ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.codigo is null limit 1");
        sqlStr.append(" 	) is not null ) ");
        return sqlStr;
    }

    /**
     * Verifica se a avaliação é do departamento do usuário logado e se existe algum
     * cargo que ainda não realizou a avaliação
     */
    public StringBuilder getWherePublicoAlvoDepartamentoAvaliaCargo(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO.getValor()).append("' ");
        sqlStr.append(" 	and ( ");
        sqlStr.append(" 	select distinct cargoavaliado.codigo from funcionariocargo");
        sqlStr.append(" 	inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
        sqlStr.append(" 	inner join cargo on cargo.codigo = funcionariocargo.cargo");
        sqlStr.append(
                " 	inner join departamento on departamento.codigo = avaliacaoinstitucional.departamentoavaliado");
        sqlStr.append(
                " 	inner join cargo cargoavaliado on cargoavaliado.departamento = departamento.codigo and case when avaliacaoinstitucional.cargoavaliado > 0 then cargoavaliado.codigo = avaliacaoinstitucional.cargoavaliado ");
        sqlStr.append(" 	else true end");

        sqlStr.append(
                " 	left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.departamento = departamento.codigo");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.cargo = cargoavaliado.codigo");
        sqlStr.append(" 	where funcionariocargo.unidadeensino = avaliacaoinstitucional.unidadeensino");
        sqlStr.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(" 	and cargo.departamento = avaliacaoinstitucional.departamento  and funcionariocargo.ativo ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.codigo is null limit 1");
        sqlStr.append(" 	) is not null ) ");
        return sqlStr;
    }

    /**
     * Verifica se a avaliação é do departamento do usuário logado e se existe algum
     * cargo que ainda não realizou a avaliação
     */
    public StringBuilder getWherePublicoAlvoCargoAvaliaDepartamento(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.CARGO_DEPARTAMENTO.getValor()).append("' ");
        sqlStr.append(" 	and ( ");
        sqlStr.append(" 	select distinct departamento.codigo from funcionariocargo");
        sqlStr.append(" 	inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
        sqlStr.append(" 	inner join cargo on cargo.codigo = funcionariocargo.cargo");
        sqlStr.append(
                " 	inner join departamento on case when avaliacaoinstitucional.departamentoavaliado > 0 then departamento.codigo = avaliacaoinstitucional.departamentoavaliado");
        sqlStr.append(
                " 	else departamento.codigo in (select codigo from departamento where departamento.unidadeensino is null or departamento.unidadeensino = avaliacaoinstitucional.unidadeensino) end");
        sqlStr.append(
                " 	left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.departamento = departamento.codigo");
        sqlStr.append(" 	where funcionariocargo.unidadeensino = avaliacaoinstitucional.unidadeensino");
        sqlStr.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(" 	and cargo.codigo = avaliacaoinstitucional.cargo and funcionariocargo.ativo ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.codigo is null limit 1");
        sqlStr.append(" 	) is not null ) ");
        return sqlStr;
    }

    /**
     * Verifica se a avaliação é do departamento do usuário logado e se existe algum
     * cargo que ainda não realizou a avaliação
     */
    public StringBuilder getWherePublicoAlvoCargoAvaliaCargo(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO.getValor()).append("' ");
        sqlStr.append(" 	and ( ");
        sqlStr.append(" 	select distinct cargoavaliado.codigo from funcionariocargo");
        sqlStr.append(" 	inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
        sqlStr.append(" 	inner join cargo on cargo.codigo = funcionariocargo.cargo");
        sqlStr.append(
                " 	inner join departamento on departamento.codigo = avaliacaoinstitucional.departamentoavaliado");
        sqlStr.append(
                " 	inner join cargo cargoavaliado on cargoavaliado.departamento = departamento.codigo and case when avaliacaoinstitucional.cargoavaliado > 0 then cargoavaliado.codigo = avaliacaoinstitucional.cargoavaliado ");
        sqlStr.append(" 	else true end");
        sqlStr.append(
                " 	left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.departamento = departamento.codigo");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.cargo = cargoavaliado.codigo");
        sqlStr.append(" 	where funcionariocargo.unidadeensino = avaliacaoinstitucional.unidadeensino");
        sqlStr.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(" 	and cargo.codigo = avaliacaoinstitucional.cargo and funcionariocargo.ativo ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.codigo is null limit 1");
        sqlStr.append(" 	) is not null ) ");
        return sqlStr;
    }

    /**
     * Verifica se a avaliação é do cargo do usuário logado e se existe algum
     * coordenador que ainda não realizou a avaliação
     */
    public StringBuilder getWherePublicoAlvoCargoAvaliaCoordenador(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.CARGO_COORDENADORES.getValor()).append("' ");
        sqlStr.append(" 	and ( ");
        sqlStr.append(" 		select distinct pessoa.codigo from funcionariocargo");
        sqlStr.append(" 		inner join funcionario on funcionario.codigo = funcionariocargo.funcionario");
        sqlStr.append(" 		inner join cargo on cargo.codigo = funcionariocargo.cargo");
        sqlStr.append(
                " 		inner join cursocoordenador on (cursocoordenador.unidadeensino = 0 or cursocoordenador.unidadeensino = funcionariocargo.unidadeensino)");
        sqlStr.append(" 		inner join curso on cursocoordenador.curso = curso.codigo");
        sqlStr.append(
                " 		inner join funcionario coordenador on cursocoordenador.funcionario = coordenador.codigo");
        sqlStr.append(" 		inner join pessoa on pessoa.codigo = coordenador.pessoa");
        sqlStr.append(
                " 		left join respostaavaliacaoinstitucionaldw on respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append(" 		and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
        sqlStr.append(" 		and respostaavaliacaoinstitucionaldw.coordenador = coordenador.pessoa");
        sqlStr.append(
                " 		where funcionariocargo.unidadeensino = avaliacaoinstitucional.unidadeensino and funcionariocargo.ativo ");
        sqlStr.append(" 		and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(" 		and cargo.codigo = avaliacaoinstitucional.cargo ");
        sqlStr.append(
                " 	    and case when length(avaliacaoinstitucional.niveleducacional) > 0 then  curso.niveleducacional = avaliacaoinstitucional.niveleducacional else true end ");
        sqlStr.append(" and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
        sqlStr.append(" 		and pessoa.ativo = true and funcionariocargo.ativo ");
        sqlStr.append("   	and (avaliacaoinstitucional.tipoCoordenadorCurso = '")
                .append(TipoCoordenadorCursoEnum.AMBOS.name())
                .append("' or avaliacaoinstitucional.tipoCoordenadorCurso = cursocoordenador.tipoCoordenadorCurso ) ");
        sqlStr.append(
                " 	    and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo limit 1) ");
        sqlStr.append(" 	    or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");
        sqlStr.append(
                "   	    where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalpessoaavaliada.pessoa = pessoa.codigo )) ");
        sqlStr.append(" 		and pessoa.codigo !=  ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(" 		and respostaavaliacaoinstitucionaldw.codigo is null limit 1");
        sqlStr.append(" 	) is not null ) ");
        sqlStr.append(" ");
        return sqlStr;
    }

    public StringBuilder getWherePublicoAlvoCoordenadorAvaliaCoordenador(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES.getValor()).append("' ");
        sqlStr.append(" 	and not exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo())
                .append(" limit 1 ) ");
        sqlStr.append(" 	) ");
        return sqlStr;
    }

    public StringBuilder getWherePublicoAlvoCoordenadorAvaliaInstituicao(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.COLABORADORES_INSTITUICAO.getValor()).append("' ");
        sqlStr.append(" 	and not exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo())
                .append(" limit 1 ) ");
        sqlStr.append(" 	) ");
        return sqlStr;
    }

    public StringBuilder getWherePublicoAlvoCoordenadorAvaliaCargo(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CARGO.getValor()).append("'  ");
        sqlStr.append(" 	and not exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo())
                .append(" limit 1 ) ");
        sqlStr.append(" 	)");
        return sqlStr;
    }

    public StringBuilder getWherePublicoAlvoCoordenadorAvaliaDepartamento(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_DEPARTAMENTO.getValor()).append("' ");
        sqlStr.append(
                " 	and not exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sqlStr.append(
                " 	and respostaavaliacaoinstitucionaldw.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = ").append(usuario.getPessoa().getCodigo())
                .append(" limit 1 ) ");
        sqlStr.append(" 	) ");
        return sqlStr;
    }

    /*
     * Esta já verifica se é uma avaliacao de coordenador avaliando professor e se
     * existe ainda algum professor que ele não respondeu a avaliacao institucional
     * validando a seguinte regra: se na avaliacao institucional estiver informado
     * ano e semestre irá considerar os professores deste período porém se não tiver
     * informado será considerado apenas os professor que dão aula no período da
     * avaliação
     */
    public StringBuilder getWherePublicoAlvoCoordenadorAvaliaProfessor(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_PROFESSOR.getValor()).append("' ");
        sqlStr.append(" 	and exists (select horarioturmadiaitem.professor as professor from horarioturma ");
        sqlStr.append(" 	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
        sqlStr.append(
                " 	inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
        sqlStr.append(" 	inner join turma on turma.codigo = horarioturma.turma  ");
        sqlStr.append(
                " 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
        sqlStr.append(
                " 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
        sqlStr.append(
                " 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
        sqlStr.append(
                " 	inner join cursocoordenador on curso.codigo = cursocoordenador.curso and (cursocoordenador.unidadeensino = 0 or cursocoordenador.unidadeensino = turma.unidadeensino) ");
        sqlStr.append("	 	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
        sqlStr.append("	 	inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor ");
        sqlStr.append(" 	where  turma.unidadeensino = avaliacaoinstitucional.unidadeensino ");
        sqlStr.append(" 	and pessoa.ativo = true ");
        sqlStr.append("   	and (avaliacaoinstitucional.tipoCoordenadorCurso = '")
                .append(TipoCoordenadorCursoEnum.AMBOS.name())
                .append("' or avaliacaoinstitucional.tipoCoordenadorCurso = cursocoordenador.tipoCoordenadorCurso ) ");
        sqlStr.append(
                " 	and case when length(avaliacaoinstitucional.niveleducacional) > 0 then  curso.niveleducacional = avaliacaoinstitucional.niveleducacional else true end ");
        sqlStr.append(" and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
        sqlStr.append(
                " 	and case when cursocoordenador.turma is not null then cursocoordenador.turma = turma.codigo else true end ");
        sqlStr.append(" 	and funcionario.pessoa != pessoa.codigo ");
        sqlStr.append(
                " 	and ( case when length(avaliacaoinstitucional.ano) = 4 and length(avaliacaoinstitucional.semestre) = 1  then horarioturma.anovigente = avaliacaoinstitucional.ano and horarioturma.semestrevigente = avaliacaoinstitucional.semestre else ");
        sqlStr.append(
                " 	case when length(avaliacaoinstitucional.ano) = 4 then horarioturma.anovigente = avaliacaoinstitucional.ano else ");
        sqlStr.append(
                " 	(horarioturmadia.data >= avaliacaoinstitucional.datainicioaula and horarioturmadia.data <= avaliacaoinstitucional.dataterminoaula) end end ) ");
        sqlStr.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(
                " 	and not exists (select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw ");
        sqlStr.append(
                " 	where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo and respostaavaliacaoinstitucionaldw.pessoa = funcionario.pessoa ");
        sqlStr.append(" 	and respostaavaliacaoinstitucionaldw.professor = horarioturmadiaitem.professor limit 1)");
        sqlStr.append(" 	limit 1 ");
        sqlStr.append(" ) )");
        return sqlStr;
    }

    public StringBuilder getWherePublicoAlvoCoordenadorAvaliaCurso(UsuarioVO usuario) {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" (avaliacaoinstitucional.publicoAlvo = '")
                .append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CURSO.getValor()).append("' ");
//		sqlStr.append(" 	and exists (select cursocoordenador.codigo from cursocoordenador ");
//		sqlStr.append("	 	inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
//		sqlStr.append("	 	inner join curso on curso.codigo = cursocoordenador.curso ");
//		sqlStr.append("	 	inner join pessoa on pessoa.codigo = funcionario.pessoa ");
//		sqlStr.append(" 	where pessoa.ativo = true ");
//		sqlStr.append("   	and (avaliacaoinstitucional.tipoCoordenadorCurso = '").append(TipoCoordenadorCursoEnum.AMBOS.name()).append("' or avaliacaoinstitucional.tipoCoordenadorCurso = cursocoordenador.tipoCoordenadorCurso ) ");
//		sqlStr.append(" 	and case when length(avaliacaoinstitucional.niveleducacional) > 0 then  curso.niveleducacional = avaliacaoinstitucional.niveleducacional else true end ");
//		sqlStr.append(" and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
//		sqlStr.append(" 	and funcionario.pessoa = ").append(usuario.getPessoa().getCodigo());
//		sqlStr.append(" 	limit 1 )");
        sqlStr.append(" 	and not exists (select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw ");
        sqlStr.append(" 	where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo and respostaavaliacaoinstitucionaldw.pessoa =  ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(" 	limit 1)");
        sqlStr.append(" )");
        return sqlStr;
    }

    @Override
    public Boolean consultarExistenciaAvaliacaoInstitucionalHabilitadoApresentacaoAvaliado(
            UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception {
        StringBuilder sql = new StringBuilder(
                getSqlConsultaExistenciaAvaliacaoInstitucionalHabilitadoApresentacaoAvaliado(unidadeEnsino, usuarioVO,
                        matriculaVO));
        sql.append(" limit 1");
        return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
    }

    @Override
    public List<AvaliacaoInstitucionalVO> consultarAvaliacaoInstitucionalHabilitadoApresentacaoAvaliado(
            UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception {
        List<AvaliacaoInstitucionalVO> avaliacaoInstitucionalVOs = new ArrayList<AvaliacaoInstitucionalVO>(0);
        StringBuilder sql = new StringBuilder(
                getSqlConsultaExistenciaAvaliacaoInstitucionalHabilitadoApresentacaoAvaliado(unidadeEnsino, usuarioVO,
                        matriculaVO));
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        while (rs.next()) {
            AvaliacaoInstitucionalVO avaliacaoInstitucionalVO = new AvaliacaoInstitucionalVO();
            avaliacaoInstitucionalVO.setCodigo(rs.getInt("codigo"));
            avaliacaoInstitucionalVO.setNome(rs.getString("nome"));
            avaliacaoInstitucionalVOs.add(avaliacaoInstitucionalVO);
        }
        return avaliacaoInstitucionalVOs;
    }

    public StringBuilder getSqlConsultaExistenciaAvaliacaoInstitucionalHabilitadoApresentacaoAvaliado(
            UnidadeEnsinoVO unidadeEnsino, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select distinct avaliacaoinstitucional.codigo, avaliacaoinstitucional.nome ");
        sql.append(" from  avaliacaoinstitucional ");
        sql.append(" inner join questionario on questionario.codigo =  avaliacaoinstitucional.questionario ");
        sql.append(" left join avaliacaoinstitucionalrespondente on avaliacaoinstitucionalrespondente.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalrespondente.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
        sql.append(" left join avaliacaoinstitucionalUnidadeEnsino on avaliacaoinstitucionalUnidadeEnsino.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
        sql.append(" where (avaliacaoinstitucional.avaliacaoultimomodulo = false and avaliacaoinstitucional.dataInicioPublicarResultado <= current_date ");
        sql.append(" and avaliacaoinstitucional.dataTerminoPublicarResultado >= current_date) ");
        sql.append(" and avaliacaoinstitucional.situacao != 'EC' ");

        if (usuarioVO.getIsApresentarVisaoProfessor()) {
            sql.append(" and ((avaliacaoinstitucional.publicarResultadoRespondente ");
            sql.append(" and ((avaliacaoinstitucional.publicoAlvo = '")
                    .append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_PROFESSOR.getValor()).append("') ");
            sql.append(" or (questionario.escopo != 'GE' ");
            sql.append(" and avaliacaoinstitucional.publicoAlvo in (");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.CURSO.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.TURMA.getValor()).append("' ");
            sql.append(" ))) ");
            sql.append(" and avaliacaoinstitucionalrespondente.codigo is not null ");
            sql.append(" and avaliacaoinstitucionalrespondente.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
//			sql.append(" and exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
//			sql.append(" and respostaavaliacaoinstitucionaldw.professor = ").append(usuarioVO.getPessoa().getCodigo());
//			sql.append(" limit 1)) ");
            sql.append(" ) ");
            sql.append(" or (avaliacaoinstitucional.publicarResultadoProfessor");
            sql.append(" and avaliacaoinstitucional.publicoAlvo = '")
                    .append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES_COORDENADORES.getValor()).append("' ");
            sql.append(" and exists ( ");
            sql.append(" select horarioturma.codigo from horarioturma ");
            sql.append(" inner join turma on turma.codigo = horarioturma.turma ");
            sql.append(
                    " 	inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
            sql.append(
                    " 	inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
            sql.append(
                    " 	or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
            sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
            sql.append(
                    " inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
            sql.append(
                    " inner join cursocoordenador on curso.codigo = cursocoordenador.curso and (cursocoordenador.unidadeensino = 0 or cursocoordenador.unidadeensino = turma.unidadeensino) ");
            sql.append(
                    " and case when cursocoordenador.turma is not null then cursocoordenador.turma = turma.codigo else true end ");
            sql.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
            sql.append(" inner join pessoa as coordenador on funcionario.pessoa = coordenador.codigo ");
            sql.append(" where turma.unidadeensino = avaliacaoinstitucionalUnidadeEnsino.unidadeensino ");
            sql.append(" and horarioturmadiaitem.professor = ").append(usuarioVO.getPessoa().getCodigo());
            sql.append(" and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
            sql.append(
                    " and (length(avaliacaoinstitucional.niveleducacional) = 0 or avaliacaoinstitucional.niveleducacional  = curso.niveleducacional) ");
            sql.append(
                    " and ((length(avaliacaoinstitucional.ano) = 0 and length(avaliacaoinstitucional.semestre) = 0  and horarioturmadia.data >= avaliacaoinstitucional.dataInicioAula and horarioturmadia.data <= avaliacaoinstitucional.dataTerminoAula ) ");
            sql.append(
                    " or (length(avaliacaoinstitucional.semestre) = 0 and  length(avaliacaoinstitucional.ano) > 0 and avaliacaoinstitucional.ano = horarioturma.anovigente ) ");
            sql.append(
                    " or (length(avaliacaoinstitucional.semestre) > 0 and  length(avaliacaoinstitucional.ano) > 0 and avaliacaoinstitucional.ano = horarioturma.anovigente and avaliacaoinstitucional.semestre = horarioturma.semestrevigente)) ");
            sql.append(
                    " and exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
            sql.append(" and respostaavaliacaoinstitucionaldw.coordenador != ")
                    .append(usuarioVO.getPessoa().getCodigo());
            sql.append(
                    " and respostaavaliacaoinstitucionaldw.coordenador = coordenador.codigo and (respostaavaliacaoinstitucionaldw.curso is null or respostaavaliacaoinstitucionaldw.curso = 0 or respostaavaliacaoinstitucionaldw.curso = curso.codigo)  limit 1)");
            sql.append(" limit 1) ");
            sql.append(" ) ");
            sql.append(" or (avaliacaoinstitucional.publicarResultadoProfessor");
            sql.append(" and avaliacaoinstitucional.publicoAlvo = '")
                    .append(PublicoAlvoAvaliacaoInstitucional.FUNCIONARIO_GESTOR.getValor()).append("' ");
            sql.append(" and exists (");
            sql.append(" select horarioturma.codigo from horarioturma ");
            sql.append(" inner join turma on turma.codigo = horarioturma.turma ");
            sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
            sql.append(
                    " inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
            sql.append(" where turma.unidadeensino = avaliacaoinstitucionalUnidadeEnsino.unidadeensino ");
            sql.append(" and horarioturmadiaitem.professor = ").append(usuarioVO.getPessoa().getCodigo());
            sql.append(" limit 1) ");
            sql.append(" ) ");
            sql.append(" or (avaliacaoinstitucional.publicarResultadoProfessor");
            sql.append(" and (( avaliacaoinstitucional.publicoAlvo = '")
                    .append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getValor()).append("' ) ");
            sql.append(" or (questionario.escopo = 'GE' ");
            sql.append(" and avaliacaoinstitucional.publicoAlvo in (");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_PROFESSOR.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.CURSO.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CURSO.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.TURMA.getValor()).append("' ");
            sql.append(" ))) ");
            sql.append(" and exists (");
            sql.append(" select horarioturma.codigo from horarioturma ");
            sql.append(" inner join turma on turma.codigo = horarioturma.turma ");
            sql.append(
                    " inner join curso on (curso.codigo = turma.curso or curso.codigo in (select turma2.curso from turmaagrupada  ");
            sql.append(
                    " inner join turma turma2 on turma2.codigo = turmaagrupada.turma  where turmaagrupada.turmaorigem = turma.codigo ) ");
            sql.append(
                    " or curso.codigo in (select turma3.curso from turma turma3  where turma3.codigo = turma.turmaprincipal )) ");
            sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
            sql.append(
                    " inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
            sql.append(" where turma.unidadeensino = avaliacaoinstitucionalUnidadeEnsino.unidadeensino ");
            sql.append(" and horarioturmadiaitem.professor = ").append(usuarioVO.getPessoa().getCodigo());
            sql.append(" and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
            sql.append(" and (avaliacaoinstitucional.turma is null or avaliacaoinstitucional.turma  = turma.codigo) ");
            sql.append(
                    " and (length(avaliacaoinstitucional.niveleducacional) = 0 or avaliacaoinstitucional.niveleducacional  = curso.niveleducacional) ");
            sql.append(
                    " and ((length(avaliacaoinstitucional.ano) = 0 and length(avaliacaoinstitucional.semestre) = 0  and horarioturmadia.data >= avaliacaoinstitucional.dataInicioAula and horarioturmadia.data <= avaliacaoinstitucional.dataTerminoAula ) ");
            sql.append(
                    " or (length(avaliacaoinstitucional.semestre) = 0 and  length(avaliacaoinstitucional.ano) > 0 and avaliacaoinstitucional.ano = horarioturma.anovigente ) ");
            sql.append(
                    " or (length(avaliacaoinstitucional.semestre) > 0 and  length(avaliacaoinstitucional.ano) > 0 and avaliacaoinstitucional.ano = horarioturma.anovigente and avaliacaoinstitucional.semestre = horarioturma.semestrevigente)) ");
            sql.append(" limit 1) ");
            sql.append(" ) ");
            sql.append(" ) ");
        } else if (usuarioVO.getIsApresentarVisaoCoordenador()) {
            sql.append(" and ((avaliacaoinstitucional.publicarResultadoRespondente ");
            sql.append(" and avaliacaoinstitucionalrespondente.codigo is not null ");
            sql.append(" and avaliacaoinstitucionalrespondente.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
//			sql.append(" and avaliacaoinstitucional.publicoAlvo in (");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES_COORDENADORES.getValor())
//					.append("', ");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.CARGO_COORDENADORES.getValor()).append("', ");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_COORDENADORES.getValor())
//					.append("', ");
//
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.ALUNO_COORDENADOR.getValor()).append("' ");
//			sql.append(" )");
//			sql.append(
//					" and exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
//			sql.append(" and respostaavaliacaoinstitucionaldw.coordenador = ")
//					.append(usuarioVO.getPessoa().getCodigo());
//			sql.append(" limit 1)) ");
            sql.append(" ) ");
            sql.append(" or (avaliacaoinstitucional.publicarResultadoCoordenador ");
            sql.append(" and avaliacaoinstitucional.publicoAlvo in ( ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.TODOS_CURSOS.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_CURSO.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CURSO.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.CURSO.getValor()).append("', ");
            sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.TURMA.getValor()).append("' ");
            sql.append(" )");

            sql.append(" and exists ( ");
            sql.append(" select cursocoordenador.codigo from cursocoordenador ");
            sql.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
            sql.append(" inner join curso on curso.codigo = cursocoordenador.curso ");
            sql.append(" where (cursocoordenador.unidadeensino = 0 or cursocoordenador.unidadeEnsino = avaliacaoinstitucionalUnidadeEnsino.unidadeensino)  ");
            sql.append(" and funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
            sql.append(
                    " and (length(avaliacaoinstitucional.niveleducacional) = 0 or avaliacaoinstitucional.niveleducacional  = curso.niveleducacional) ");
            sql.append(" and (avaliacaoinstitucional.turma is null and (exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo) or not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)) or avaliacaoinstitucional.turma is not null) ");
            sql.append(
                    " and (avaliacaoinstitucional.turma is null or (cursocoordenador.turma is not null and  cursocoordenador.turma = avaliacaoinstitucional.turma) or (cursocoordenador.turma is null and  cursocoordenador.curso = (select turma.curso from turma where turma.codigo = avaliacaoinstitucional.turma )))  ");
            sql.append(
                    " and exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo  ");
            sql.append(" and ((avaliacaoinstitucional.publicoAlvo not in ('")
                    .append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES.getValor()).append("', '")
                    .append(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA.getValor()).append("', '")
                    .append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getValor())
                    .append("') and respostaavaliacaoinstitucionaldw.curso = cursocoordenador.curso) ");
            sql.append(" or (avaliacaoinstitucional.publicoAlvo in ('")
                    .append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES.getValor()).append("', '")
                    .append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getValor())
                    .append("') and respostaavaliacaoinstitucionaldw.unidadeensino = cursocoordenador.unidadeensino ) ");
            sql.append(" or (avaliacaoinstitucional.publicoAlvo = '")
                    .append(PublicoAlvoAvaliacaoInstitucional.PROFESSOR_TURMA.getValor())
                    .append("' and respostaavaliacaoinstitucionaldw.unidadeensino = cursocoordenador.unidadeensino  ");
            sql.append(
                    " and exists (select turma.codigo from turma where turma.codigo = respostaavaliacaoinstitucionaldw.turma and turma.curso = cursocoordenador.curso and (cursocoordenador.unidadeensino = 0 or turma.unidadeensino = cursocoordenador.unidadeensino)  ");
            sql.append(
                    " union select turma.codigo from turma inner join turmaagrupada on turmaagrupada.turma = turma.codigo where turma.curso = cursocoordenador.curso and (cursocoordenador.unidadeensino = 0 or turma.unidadeensino = cursocoordenador.unidadeensino)  ");
            sql.append(
                    " and RespostaAvaliacaoInstitucionalDW.turma = turmaagrupada.turmaorigem and RespostaAvaliacaoInstitucionalDW.unidadeensino = turma.unidadeensino ");
            sql.append(" ))) ");
            sql.append(" limit 1) ");
            sql.append(" limit 1) ");
            sql.append(" ) ");
            sql.append(" or (avaliacaoinstitucional.publicarResultadoCoordenador ");
            sql.append(" and avaliacaoinstitucional.publicoAlvo in ('")
                    .append(PublicoAlvoAvaliacaoInstitucional.FUNCIONARIO_GESTOR.getValor()).append("', '")
                    .append(PublicoAlvoAvaliacaoInstitucional.COLABORADORES_INSTITUICAO.getValor()).append("') ");
            sql.append(" and exists ( ");
            sql.append(" select cursocoordenador.codigo from cursocoordenador ");
            sql.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
            sql.append(" inner join curso on curso.codigo = cursocoordenador.curso ");
            sql.append(" where (cursocoordenador.unidadeensino = 0 or cursocoordenador.unidadeEnsino = avaliacaoinstitucionalUnidadeEnsino.unidadeensino)  ");
            sql.append(" and funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
            sql.append(
                    " and exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo  ");
            sql.append(" limit 1) ");
            sql.append(" limit 1) ");
            sql.append(" ) ");

            sql.append(" ) ");
        } else if (usuarioVO.getIsApresentarVisaoAdministrativa()) {
            if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
                sql.append(" and avaliacaoinstitucionalUnidadeEnsino.unidadeensino = ").append(unidadeEnsino.getCodigo());
            }
            sql.append(" and avaliacaoinstitucional.publicarResultadoRespondente ");
            sql.append(" and avaliacaoinstitucionalrespondente.codigo is not null ");
            sql.append(" and avaliacaoinstitucionalrespondente.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
//			sql.append(" and ((avaliacaoinstitucional.publicoAlvo in (");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_CARGO.getValor()).append("', ");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_CARGO.getValor()).append("', ");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.CARGO_CARGO.getValor()).append("' ");
//			sql.append(" )");
//			sql.append(
//					" and exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
//			sql.append(
//					" and respostaavaliacaoinstitucionaldw.cargo in (select funcionariocargo.cargo from  funcionariocargo ");
//			sql.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario  ");
//			sql.append(" where funcionariocargo.ativo and funcionario.pessoa = ")
//					.append(usuarioVO.getPessoa().getCodigo());
//			sql.append(" and funcionariocargo.unidadeEnsino = avaliacaoinstitucional.unidadeEnsino ) ");
//			if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
//				sql.append(" and respostaavaliacaoinstitucionaldw.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
//			}
//			sql.append(" limit 1)) ");
//			sql.append(" or ( avaliacaoinstitucional.publicoAlvo in (");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.COORDENADORES_DEPARTAMENTO.getValor())
//					.append("', ");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.DEPARTAMENTO_DEPARTAMENTO.getValor())
//					.append("', ");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.CARGO_DEPARTAMENTO.getValor()).append("' ");
//			sql.append(" )");
//			sql.append(
//					" and exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
//			sql.append(
//					" and respostaavaliacaoinstitucionaldw.departamento in (select cargo.departamento from  funcionariocargo ");
//			sql.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario  ");
//			sql.append(" inner join cargo on cargo.codigo = funcionariocargo.cargo  ");
//			sql.append(" where funcionariocargo.ativo and funcionario.pessoa = ")
//					.append(usuarioVO.getPessoa().getCodigo());
//			sql.append(" and funcionariocargo.unidadeEnsino = avaliacaoinstitucional.unidadeEnsino ) ");
//			if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
//				sql.append(" and respostaavaliacaoinstitucionaldw.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
//			}
//			sql.append(" limit 1)) ");
//
//			sql.append(" or ( avaliacaoinstitucional.publicoAlvo in (");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.FUNCIONARIO_GESTOR.getValor()).append("', ");
//			sql.append(" '").append(PublicoAlvoAvaliacaoInstitucional.COLABORADORES_INSTITUICAO.getValor())
//					.append("' ");
//			sql.append(" )");
//			sql.append(
//					" and exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
//			if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
//				sql.append(" and respostaavaliacaoinstitucionaldw.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
//			}
//			sql.append(" and exists (select cargo.departamento from  funcionariocargo ");
//			sql.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario  ");
//			sql.append(" inner join cargo on cargo.codigo = funcionariocargo.cargo  ");
//			sql.append(" where funcionariocargo.ativo and funcionario.pessoa = ")
//					.append(usuarioVO.getPessoa().getCodigo());
//			sql.append(" and funcionariocargo.unidadeEnsino = avaliacaoinstitucional.unidadeensino )");
//			sql.append(" limit 1)) ");
//
//			sql.append(" ) ");
        } else if (usuarioVO.getIsApresentarVisaoAluno() || usuarioVO.getIsApresentarVisaoPais()) {
            if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
                sql.append(" and avaliacaoinstitucionalUnidadeEnsino.unidadeensino = ").append(unidadeEnsino.getCodigo());
            }
            sql.append(" and avaliacaoinstitucional.publicarResultadoAluno ");
            sql.append(" and avaliacaoinstitucionalrespondente.codigo is not null ");
            sql.append(" and avaliacaoinstitucionalrespondente.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
//			sql.append(" AND (");
//			sql.append(getWherePublicoAlvoAlunoAvaliaTodosCurso(usuarioVO, matriculaVO.getMatricula()));
//			sql.append(" OR ");
//			sql.append(getWherePublicoAlvoAlunoAvaliaCursoEspecifico(usuarioVO, matriculaVO.getCurso().getCodigo(),
//					matriculaVO.getMatricula()));
//			sql.append(" OR ");
//			sql.append(getWherePublicoAlvoAlunoAvaliaTurmaEspecifica(usuarioVO, matriculaVO.getCurso().getCodigo(),
//					matriculaVO.getMatricula()));
//			sql.append(" OR ");
//			sql.append(getWherePublicoAlvoAlunoAvaliaCoordenador(usuarioVO, matriculaVO.getCurso().getCodigo(),
//					matriculaVO.getMatricula()));
//			sql.append(" ) ");
//			sql.append(
//					" and exists (select codigo from respostaavaliacaoinstitucionaldw where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional = avaliacaoinstitucional.codigo ");
//			sql.append(" and respostaavaliacaoinstitucionaldw.unidadeensino = ").append(unidadeEnsino.getCodigo());
//			sql.append(" and respostaavaliacaoinstitucionaldw.curso = ").append(matriculaVO.getCurso().getCodigo());
//			sql.append(" limit 1) ");
        }

        return sql;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gravarDataUltimaNotificacao(Integer avaliacaoInstitucional, UsuarioVO usuarioVO) {
        getConexao()
                .getJdbcTemplate().update(
                        "update avaliacaoinstitucional set dataultimanotificacao = current_date where codigo = ? "
                                + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO),
                        avaliacaoInstitucional);
    }

    @Override
    public List<AvaliacaoInstitucionalVO> consultarAvaliacaoInstitucionalNotificar() throws Exception {
        StringBuffer sql = getSQLPadraoConsultaCompleta();
        sql.append(" where avaliacaoinstitucional.notificarRespondentes = true and ");
        sql.append(" (((avaliacaoinstitucional.avaliacaoUltimoModulo = false and (avaliacaoinstitucional.dataultimanotificacao is null ");
        sql.append(" or (avaliacaoinstitucional.dataultimanotificacao is not null and recorrenciaDiaNotificar is not null and recorrenciaDiaNotificar > 0 ");
        sql.append(" and (avaliacaoinstitucional.dataultimanotificacao+(recorrenciaDiaNotificar::varchar||' days')::interval ) <= current_date )) ");
        sql.append(" and avaliacaoinstitucional.dataInicio <= current_date ");
        sql.append(" and avaliacaoinstitucional.dataFinal >= current_date) ");
        sql.append(") or avaliacaoinstitucional.avaliacaoUltimoModulo = true) ");
        sql.append(" and avaliacaoinstitucional.situacao = 'AT' ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<AvaliacaoInstitucionalVO> avaliacaoInstitucionalVOs = new ArrayList<AvaliacaoInstitucionalVO>(0);
        while (rs.next()) {
            AvaliacaoInstitucionalVO avaliacaoInstitucionalVO = new AvaliacaoInstitucionalVO();
            montarDadosCompleto(avaliacaoInstitucionalVO, rs, null);
            avaliacaoInstitucionalVOs.add(avaliacaoInstitucionalVO);
        }
        return avaliacaoInstitucionalVOs;
    }

    @Override
    public void realizarCloneAvaliacaoInstitucional(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO,
                                                    UsuarioVO usuarioVO) {
        if (Uteis.isAtributoPreenchido(avaliacaoInstitucionalVO)) {
            avaliacaoInstitucionalVO.setCodigo(0);
            avaliacaoInstitucionalVO.setNovoObj(true);
            avaliacaoInstitucionalVO.setSituacao("EC");
            avaliacaoInstitucionalVO.setData(new Date());
            avaliacaoInstitucionalVO.setNome(avaliacaoInstitucionalVO.getNome() + " - Clone");
            if (avaliacaoInstitucionalVO.getNome().length() > 250) {
                avaliacaoInstitucionalVO.setNome(avaliacaoInstitucionalVO.getNome().substring(0, 250));
            }
            avaliacaoInstitucionalVO.setResponsavel(new UsuarioVO());
            avaliacaoInstitucionalVO.getResponsavel().setCodigo(usuarioVO.getCodigo());
            avaliacaoInstitucionalVO.getResponsavel().setNome(usuarioVO.getNome());
            for (AvaliacaoInstitucionalPessoaAvaliadaVO avaliacaoInstitucionalPessoaAvaliadaVO : avaliacaoInstitucionalVO
                    .getListaAvaliacaoInstitucionalPessoaAvaliadaVOs()) {
                avaliacaoInstitucionalPessoaAvaliadaVO.setAvaliacaoInstitucionalVO(avaliacaoInstitucionalVO);
                avaliacaoInstitucionalPessoaAvaliadaVO.setCodigo(0);
                avaliacaoInstitucionalPessoaAvaliadaVO.setNovoObj(true);
            }
        }
    }


    @Override
    public void realizarMarcacaoCursoSelecionado(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, final List<CursoVO> cursoVOs) {
        Set<Integer> codigosSelecionados = avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs()
                .stream()
                .map(item -> item.getCursoVO().getCodigo())
                .collect(Collectors.toSet());

        cursoVOs.forEach(curso  -> {
            curso.setFiltrarCursoVO(codigosSelecionados.contains(curso.getCodigo()));
        });
        //avaliacaoInstitucionalVO.setNomeCurso(null);
    }

    @Override
    public void realizarMarcacaoUnidadeEnsinoSelecionado(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
        Set<Integer> codigosSelecionados = avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs()
                .stream()
                .map(item -> item.getUnidadeEnsinoVO().getCodigo())
                .collect(Collectors.toSet());

        unidadeEnsinoVOs.forEach(unidadeEnsino -> {
            unidadeEnsino.setFiltrarUnidadeEnsino(codigosSelecionados.contains(unidadeEnsino.getCodigo()));
        });
    }


    @Override
    public void adicionarAvaliacaoInstitucionalUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
        AvaliacaoInstitucionalUnidadeEnsinoVO avaliacaoInstitucionalUnidadeEnsinoVO = new AvaliacaoInstitucionalUnidadeEnsinoVO(
                avaliacaoInstitucionalVO, unidadeEnsinoVO);
        if (!avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs().contains(avaliacaoInstitucionalUnidadeEnsinoVO)) {
            avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs().add(avaliacaoInstitucionalUnidadeEnsinoVO);
            avaliacaoInstitucionalVO.setUnidadeEnsino(null);
            return;
        }
        avaliacaoInstitucionalUnidadeEnsinoVO = null;
    }

    @Override
    public void removerAvaliacaoInstitucionalUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario, Boolean marcarTodasUnidadeEnsino) throws Exception {
        boolean algumFoiRemovido = avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs()
                .removeIf(item -> item.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsinoVO.getCodigo()));
        //avaliacaoInstitucionalVO.setNomeUnidadeEnsino(null);
        if (algumFoiRemovido  && !marcarTodasUnidadeEnsino.equals(Boolean.FALSE) && !avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs().isEmpty() && !avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs().isEmpty()) {
            avaliacaoInstitucionalVO.setCursoVOs(getFacadeFactory().getCursoFacade().consultaRapidaPorNomePorListaUnidadeEnsinoPorNivelEducacional("",
                    avaliacaoInstitucionalVO.getAvaliacaoInstitucionalUnidadeEnsinoVOs()
                            .stream()
                            .map(AvaliacaoInstitucionalUnidadeEnsinoVO::getUnidadeEnsinoVO)
                            .collect(Collectors.toList()),
                    TipoNivelEducacional.getEnum(avaliacaoInstitucionalVO.getNivelEducacional()),
                    new DataModelo()));
            getFacadeFactory().getAvaliacaoInstitucionalFacade().realizarMarcacaoCursoSelecionado(avaliacaoInstitucionalVO, avaliacaoInstitucionalVO.getCursoVOs());

        }
        avaliacaoInstitucionalVO.setCursoVOs(null);
    }

    @Override
    public void adicionarAvaliacaoInstitucionalCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, CursoVO cursoVO)
            throws Exception {
        AvaliacaoInstitucionalCursoVO avaliacaoInstitucionalCursoVO = new AvaliacaoInstitucionalCursoVO(
                avaliacaoInstitucionalVO, cursoVO);
        if (!avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs().contains(avaliacaoInstitucionalCursoVO)) {
            avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs().add(avaliacaoInstitucionalCursoVO);
            //avaliacaoInstitucionalVO.setNomeCurso(cursoVO.getNome());
            return;
        }
        avaliacaoInstitucionalCursoVO = null;

    }

    @Override
    public void removerAvaliacaoInstitucionalCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, CursoVO cursoVO)
            throws Exception {
        avaliacaoInstitucionalVO.getAvaliacaoInstitucionalCursoVOs()
                .removeIf(item -> item.getCursoVO().getCodigo().equals(cursoVO.getCodigo()));
        avaliacaoInstitucionalVO.setNomeCurso(null);
       // avaliacaoInstitucionalVO.setCodigosCurso("");
    }

    @Override
    public List<AvaliacaoInstitucionalVO> consultarAvaliacaoInstitucionalUsuarioLogado(UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception {
        List<AvaliacaoInstitucionalVO> listaAvaliacaoAtiva = new ArrayList<AvaliacaoInstitucionalVO>(0);
        if (Uteis.isAtributoPreenchido(usuarioVO) && Uteis.isAtributoPreenchido(usuarioVO.getPessoa())) {
            if (usuarioVO.getIsApresentarVisaoProfessor()) {
                listaAvaliacaoAtiva.addAll(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorUnidadeEnsinoDataSituacaoVisaoLogar(0, new Date(), "AT", usuarioVO.getVisaoLogar(), false, true, false, usuarioVO, 0, "", "", null));
                if (usuarioVO.getTipoNivelEducacionalLogado() != null && usuarioVO.getTipoNivelEducacionalLogado().equals(TipoNivelEducacional.POS_GRADUACAO)) {
                    listaAvaliacaoAtiva.addAll(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarAvaliacaoInstitucionalUltimaAulaModuloTurmaVisaoProfessor(usuarioVO, 0));
                }
            } else if (usuarioVO.getIsApresentarVisaoAdministrativa()) {
                listaAvaliacaoAtiva.addAll(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorUnidadeEnsinoDataSituacaoVisaoLogar(0, new Date(), "AT", usuarioVO.getVisaoLogar(), false, true, false, usuarioVO, 0, "", "", null));
            } else if (usuarioVO.getIsApresentarVisaoCoordenador()) {
                listaAvaliacaoAtiva.addAll(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorUnidadeEnsinoDataSituacaoVisaoLogar(0, new Date(), "AT", usuarioVO.getVisaoLogar(), false, true, false, usuarioVO, 0, "", "", null));
            } else if (usuarioVO.getIsApresentarVisaoAluno() && matriculaVO != null && Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
                listaAvaliacaoAtiva.addAll(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorUnidadeEnsinoDataSituacaoVisaoLogar(matriculaVO.getUnidadeEnsino().getCodigo(), new Date(), "AT", usuarioVO.getVisaoLogar(), false, true, false, usuarioVO, matriculaVO.getCurso().getCodigo(), matriculaVO.getCurso().getNivelEducacional(), matriculaVO.getMatricula(), null));
                if (matriculaVO.getSituacao().equals("AT") && matriculaVO.getCurso().getNivelEducacional().equals("PO")) {
                    listaAvaliacaoAtiva.addAll(getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorUnidadeEnsinoDataSituacaoVisaoLogarUltimoModulo(matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getCurso().getCodigo(), 0, new Date(), "AT", usuarioVO.getVisaoLogar(), matriculaVO.getCurso().getNivelEducacional(), false, true, matriculaVO.getMatricula(), false, usuarioVO));
                }
            }

        }
        return listaAvaliacaoAtiva;
    }

    @Override
    public List<MatriculaPeriodoTurmaDisciplinaVO> consultarTurmaDisciplinaUltimoModuloTurmaVisaoAlunoPorAvaliacaoInstitucional(UsuarioVO usuarioVO, AvaliacaoInstitucionalVO avaliacaoInstitucional, String matricula, Boolean trazerRespondido) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append("  select horarioturma.*, curso.codigo as curso, curso.nome as nomecurso ");
        sql.append("  from (select turma.codigo as turma, turma.identificadorturma, disciplina.codigo as disciplina, disciplina.nome as nomedisciplina, pessoa.codigo as professor, pessoa.nome as nomeprofessor, min(horarioturmadiaitem.data) as datainicio, ");
        sql.append("  (select max(htdi.data) from horarioturmadia htd");
        sql.append("  inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo");
        sql.append("  where htd.horarioturma =  horarioturma.codigo");
        sql.append("  and htdi.disciplina = disciplina.codigo");
        sql.append("  and htdi.professor = pessoa.codigo)  as datatermino  ");
        sql.append("  from turma");
        sql.append("  inner join curso on (((turma.turmaagrupada is null or turma.turmaagrupada =  false) and turma.curso = curso.codigo)");
        sql.append("  or (turma.turmaagrupada =  true and curso.codigo = (select t.curso from turmaagrupada ta inner join turma as t on t.codigo = ta.turma ");
        sql.append("  where ta.turmaorigem = turma.codigo limit 1 )))");
        sql.append("  inner join horarioturma on horarioturma.turma = turma.codigo");
        sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
        sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append("  inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo");
        sql.append("  inner join pessoa on horarioturmadiaitem.professor = pessoa.codigo");
        sql.append("  where turma.considerarTurmaAvaliacaoInstitucional =  true ");
        if (Uteis.isAtributoPreenchido(avaliacaoInstitucional.getUnidadeEnsino())) {
            sql.append(" and  turma.unidadeensino = ").append(avaliacaoInstitucional.getUnidadeEnsino().getCodigo());
        }
        if (Uteis.isAtributoPreenchido(avaliacaoInstitucional.getNivelEducacional())) {
            sql.append("  and curso.niveleducacional = '").append(avaliacaoInstitucional.getNivelEducacional()).append("' ");
        }
        if (Uteis.isAtributoPreenchido(avaliacaoInstitucional.getDisciplina())) {
            sql.append("  and disciplina.codigo = ").append(avaliacaoInstitucional.getDisciplina().getCodigo());
        }
        if (Uteis.isAtributoPreenchido(avaliacaoInstitucional.getTurma())) {
            sql.append("  and turma.codigo = ").append(avaliacaoInstitucional.getTurma().getCodigo());
        }
        sql.append("  and horarioturmadiaitem.data BETWEEN '").append(Uteis.getDataJDBC(avaliacaoInstitucional.getDataInicioAula())).append("' and '").append(Uteis.getDataJDBC(avaliacaoInstitucional.getDataTerminoAula())).append("' ");
        sql.append("  group by turma.codigo, disciplina.codigo, pessoa.codigo, horarioturma.codigo, turma.identificadorturma, disciplina.nome, pessoa.nome");
        sql.append("  having (select max(htdi.data) from horarioturmadia htd");
        sql.append("  inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo");
        sql.append("  where htd.horarioturma =  horarioturma.codigo");
        sql.append("  and htdi.disciplina = disciplina.codigo");
        sql.append("  and htdi.professor = pessoa.codigo) BETWEEN '").append(Uteis.getDataJDBC(avaliacaoInstitucional.getDataInicioAula())).append("' and '").append(Uteis.getDataJDBC(avaliacaoInstitucional.getDataTerminoAula())).append("' ");
        sql.append("  ");
        sql.append("       ) as horarioturma ");
        sql.append("  ");
        sql.append("       INNER JOIN matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("' and horarioturma.turma = matriculaperiodoturmadisciplina.turma and horarioturma.disciplina = matriculaperiodoturmadisciplina.disciplina");
        sql.append("       INNER JOIN matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
        sql.append("       INNER JOIN curso on matricula.curso = curso.codigo ");
        sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
        sql.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
        sql.append(" and mp.situacaomatriculaperiodo != 'PC'  ");
        if (!avaliacaoInstitucional.getAno().trim().isEmpty()) {
            sql.append(" and mp.ano = '").append(avaliacaoInstitucional.getAno()).append("' ");
        }
        if (!avaliacaoInstitucional.getSemestre().trim().isEmpty()) {
            sql.append(" and mp.semestre = '").append(avaliacaoInstitucional.getSemestre()).append("' ");
        }

        sql.append(" order by mp.ano||mp.semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc  limit 1) ");
        sql.append("       INNER JOIN historico ON historico.matricula = matricula.matricula     AND matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
        sql.append("       and historico.matriculaperiodo = matriculaperiodo.codigo");


        sql.append("		where historico.matriculaperiodo = matriculaperiodo.codigo ");
        if (Uteis.isAtributoPreenchido(avaliacaoInstitucional.getDisciplina())) {
            sql.append("   	and historico.disciplina  = ").append(avaliacaoInstitucional.getDisciplina().getCodigo());
        }
        sql.append("		and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null ");
        sql.append("		or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null) and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
        if (trazerRespondido == null || !trazerRespondido) {
            sql.append(" and matricula.situacao = 'AT' ");
            sql.append("     and not exists (");
            sql.append(" 	select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional   = ").append(avaliacaoInstitucional.getCodigo());
            sql.append(" 	and respostaavaliacaoinstitucionaldw.turma = matriculaperiodoturmadisciplina.turma	");
            sql.append(" 	and respostaavaliacaoinstitucionaldw.pessoa = matricula.aluno");
            sql.append(" 	and respostaavaliacaoinstitucionaldw.matriculaaluno = matricula.matricula");
            sql.append(" 	and respostaavaliacaoinstitucionaldw.disciplina = matriculaperiodoturmadisciplina.disciplina limit 1");
            sql.append("    )");
        }
        sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
        sql.append(" 	and ( not exists ( select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucional.getCodigo()).append(") ");
        sql.append(" 	or exists (select avaliacaoinstitucional from avaliacaoinstitucionalpessoaavaliada  ");
        sql.append("   	where avaliacaoinstitucionalpessoaavaliada.avaliacaoinstitucional = ").append(avaliacaoInstitucional.getCodigo()).append(" ");
        sql.append("   	and horarioturma.professor = avaliacaoinstitucionalpessoaavaliada.pessoa ");
        sql.append("		)) ");
        sql.append(" order by identificadorturma, nomedisciplina ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
        while (rs.next()) {
            MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
            matriculaPeriodoTurmaDisciplinaVO.getTurma().setCodigo(rs.getInt("turma"));
            matriculaPeriodoTurmaDisciplinaVO.getTurma().setIdentificadorTurma(rs.getString("identificadorturma"));
            matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setCodigo(rs.getInt("disciplina"));
            matriculaPeriodoTurmaDisciplinaVO.getDisciplina().setNome(rs.getString("nomedisciplina"));
            matriculaPeriodoTurmaDisciplinaVO.getTurma().getCurso().setCodigo(rs.getInt("curso"));
            matriculaPeriodoTurmaDisciplinaVO.getTurma().getCurso().setNome(rs.getString("nomecurso"));
            matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(rs.getInt("professor"));
            matriculaPeriodoTurmaDisciplinaVO.getProfessor().setNome(rs.getString("nomeprofessor"));
            matriculaPeriodoTurmaDisciplinaVOs.add(matriculaPeriodoTurmaDisciplinaVO);
        }
        return matriculaPeriodoTurmaDisciplinaVOs;
    }

    @Override
    public List<TurmaDisciplinaVO> consultarTurmaDisciplinaUltimoModuloTurmaVisaoProfessorPorAvaliacaoInstitucional(UsuarioVO usuarioVO, AvaliacaoInstitucionalVO avaliacaoInstitucional, Boolean trazerRespondido) throws Exception {
        StringBuilder sql = new StringBuilder("");

        sql.append(" select distinct avaliacaoinstitucional.codigo, turma.codigo as codigoturma, turma.identificadorturma, disciplina.codigo as codigodisciplina, disciplina.nome as nomedisciplina, curso.codigo as codigocurso, curso.nome as nomecurso, ");
        sql.append(" turma.unidadeensino as unidadeensino, turma.turno as turno ");
        sql.append(" from avaliacaoinstitucional  ");
        sql.append(" inner join turma on (avaliacaoinstitucional.unidadeensino  is null or (avaliacaoinstitucional.unidadeensino  is not null and  turma.unidadeensino = avaliacaoinstitucional.unidadeensino) )");
        sql.append(" and turma.anual = false and turma.semestral = false");
        sql.append(" and ((turma.turmaagrupada and exists (select tur.codigo from turmaagrupada ");
        sql.append(" inner join turma as tur on tur.codigo = turmaagrupada.turmaorigem ");
        sql.append(" inner join curso on curso.codigo = tur.curso ");
        if (Uteis.isAtributoPreenchido(avaliacaoInstitucional.getAvaliacaoInstitucionalCursoVOs())) {
            sql.append(getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().getSqlCondicaoWhereCurso(avaliacaoInstitucional, "curso.codigo", "and"));
        }
        sql.append(" where turmaagrupada.turma = turma.codigo and curso.niveleducacional = avaliacaoinstitucional.niveleducacional))");
        sql.append(" or (coalesce(turma.turmaagrupada) = false ");
        sql.append(" and exists (select codigo from curso where curso.codigo = turma.curso and curso.niveleducacional = avaliacaoinstitucional.niveleducacional ");
        if (Uteis.isAtributoPreenchido(avaliacaoInstitucional.getAvaliacaoInstitucionalCursoVOs())) {
            sql.append(getFacadeFactory().getAvaliacaoInstitucionalAnaliticoRelFacade().getSqlCondicaoWhereCurso(avaliacaoInstitucional, "curso.codigo", "and"));
        }
        sql.append(" )))");

        sql.append(" and ((avaliacaoinstitucional.turma is null and (avaliacaoinstitucional.curso is null ");
        sql.append(" or (avaliacaoinstitucional.curso is not null and turma.curso is not null and avaliacaoinstitucional.curso = turma.curso)");
        sql.append(" or (avaliacaoinstitucional.curso is not null and turma.curso is not null and turma.turmaagrupada ");
        sql.append(" and  exists(select tur.codigo from turmaagrupada inner join turma as tur on tur.codigo = turmaagrupada.turmaorigem ");
        sql.append(" where turmaagrupada.turma = turma.codigo and  avaliacaoinstitucional.curso = tur.curso))");
        sql.append(" )) ");
        sql.append(" or (avaliacaoinstitucional.turma is not null and avaliacaoinstitucional.turma = turma.codigo))");
        sql.append("");
        sql.append(" left join curso on turma.curso = curso.codigo ");
        sql.append(" inner join horarioturma on horarioturma.turma = turma.codigo ");
        sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
        sql.append(" and horarioturmadia.data <= '").append(Uteis.getDataJDBC(avaliacaoInstitucional.getDataTerminoAula())).append("' ");
        sql.append(" and horarioturmadia.data  >= '").append(Uteis.getDataJDBC(avaliacaoInstitucional.getDataInicioAula())).append("' ");
        sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append(" and horarioturmadiaitem.professor = ? ");
        sql.append(" inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo");
        sql.append(" where avaliacaoinstitucional.avaliacaoultimomodulo  = true and avaliacaoinstitucional.situacao = 'AT'");
        sql.append(" AND (avaliacaoinstitucional.publicoAlvo = '").append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getValor()).append("')");
        sql.append("  and avaliacaoinstitucional.codigo = ? ");
        sql.append(" and exists(");
        sql.append("  select t.* from (");
        sql.append("      select max(htdi.data) as data, htdi.disciplina, htdi.professor from horarioturmadia htd ");
        sql.append("      inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo");
        sql.append("      and htd.horarioturma = horarioturma.codigo");
        sql.append("      and htdi.disciplina = horarioturmadiaitem.disciplina   ");
        sql.append("      and htdi.professor = horarioturmadiaitem.professor   ");
        sql.append("      group by htdi.disciplina, htdi.professor ");
        sql.append("   ) as t where ");
        if (trazerRespondido == null || !trazerRespondido) {
            sql.append("     (t.data::date + (avaliacaoinstitucional.diasdisponivel||' days')::interval + (avaliacaoinstitucional.horafim||' hours')::interval ) > ('").append(Uteis.getDataJDBC(avaliacaoInstitucional.getDataTerminoAula())).append("'::DATE +(avaliacaoinstitucional.horafim||' hours')::interval) ");
            sql.append("     and (t.data::date + (avaliacaoinstitucional.horainicio||' hours')::interval) <= ('").append(Uteis.getDataJDBC(avaliacaoInstitucional.getDataTerminoAula())).append("'::DATE +(avaliacaoinstitucional.horainicio||' hours')::interval) ");
            sql.append("    and not exists (");
            sql.append(" 	select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional   = avaliacaoinstitucional.codigo");
            sql.append(" 	and respostaavaliacaoinstitucionaldw.turma = turma.codigo	");
            sql.append(" 	and respostaavaliacaoinstitucionaldw.professor = horarioturmadiaitem.professor	");
            sql.append(" 	and respostaavaliacaoinstitucionaldw.disciplina = t.disciplina limit 1");
            sql.append("    )");
        } else {
            sql.append(" t.data::date <= '").append(Uteis.getDataJDBC(avaliacaoInstitucional.getDataTerminoAula())).append("' ");
        }
        sql.append(" ) ");
        sql.append(" order by identificadorturma, nomedisciplina");

        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), usuarioVO.getPessoa().getCodigo(), avaliacaoInstitucional.getCodigo());
        List<TurmaDisciplinaVO> turmaDisciplinaVOs = new ArrayList<TurmaDisciplinaVO>(0);
        while (rs.next()) {
            TurmaDisciplinaVO turmaDisciplinaVO = new TurmaDisciplinaVO();
            turmaDisciplinaVO.getTurmaDescricaoVO().setCodigo(rs.getInt("codigoturma"));
            turmaDisciplinaVO.getTurmaDescricaoVO().setIdentificadorTurma(rs.getString("identificadorturma"));
            turmaDisciplinaVO.getDisciplina().setCodigo(rs.getInt("codigodisciplina"));
            turmaDisciplinaVO.getDisciplina().setNome(rs.getString("nomedisciplina"));
            turmaDisciplinaVO.getTurmaDescricaoVO().getCurso().setCodigo(rs.getInt("codigocurso"));
            turmaDisciplinaVO.getTurmaDescricaoVO().getCurso().setNome(rs.getString("nomecurso"));
            turmaDisciplinaVO.getTurmaDescricaoVO().getTurno().setCodigo(rs.getInt("turno"));
            turmaDisciplinaVO.getTurmaDescricaoVO().getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino"));
            turmaDisciplinaVOs.add(turmaDisciplinaVO);
        }
        return turmaDisciplinaVOs;
    }

    @Override
    public List<AvaliacaoInstitucionalVO> consultarAvaliacaoInstitucionalUltimaAulaModuloTurmaVisaoProfessor(UsuarioVO usuarioVO, int limit) throws Exception {
        StringBuilder sql = new StringBuilder(getSQLPadraoConsultaBasica());
        sql.append(" where avaliacaoinstitucional.codigo in ( ");
        sql.append(" select distinct avaliacaoinstitucional.codigo ");
        sql.append(" from avaliacaoinstitucional  ");
        sql.append(" inner join turma on (avaliacaoinstitucional.unidadeensino  is null or (avaliacaoinstitucional.unidadeensino  is not null and  turma.unidadeensino = avaliacaoinstitucional.unidadeensino) )");
        sql.append(" and turma.anual = false and turma.semestral = false");
        sql.append(" and ((turma.turmaagrupada and exists (select tur.codigo from turmaagrupada ");
        sql.append(" inner join turma as tur on tur.codigo = turmaagrupada.turmaorigem ");
        sql.append(" inner join curso on curso.codigo = tur.curso ");
        sql.append(" and (avaliacaoinstitucional.turma is not null or (avaliacaoinstitucional.turma is null and exists(select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo ) or not exists(select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo))) ");
        sql.append(" where turmaagrupada.turma = turma.codigo and curso.niveleducacional = avaliacaoinstitucional.niveleducacional))");
        sql.append(" or (coalesce(turma.turmaagrupada) = false ");
        sql.append(" and exists (select codigo from curso where curso.codigo = turma.curso and curso.niveleducacional = avaliacaoinstitucional.niveleducacional ");
        sql.append(" and (avaliacaoinstitucional.turma is not null or (avaliacaoinstitucional.turma is null and exists(select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo and avaliacaoinstitucionalcurso.curso = curso.codigo ) or not exists(select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo)))) ");
        sql.append(" ))");
        sql.append(" and ((avaliacaoinstitucional.turma is null and (avaliacaoinstitucional.curso is null ");
        sql.append(" or (avaliacaoinstitucional.curso is not null and turma.curso is not null and avaliacaoinstitucional.curso = turma.curso)");
        sql.append(" or (avaliacaoinstitucional.curso is not null and turma.curso is not null and turma.turmaagrupada ");
        sql.append(" and  exists(select tur.codigo from turmaagrupada inner join turma as tur on tur.codigo = turmaagrupada.turmaorigem ");
        sql.append(" where turmaagrupada.turma = turma.codigo and  avaliacaoinstitucional.curso = tur.curso))");
        sql.append(" )) ");
        sql.append(" or (avaliacaoinstitucional.turma is not null and avaliacaoinstitucional.turma = turma.codigo))");
        sql.append("");
        sql.append(" inner join horarioturma on horarioturma.turma = turma.codigo");
        sql.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
        sql.append(" and horarioturmadia.data <= current_date");
        sql.append(" and horarioturmadia.data  >= (current_date - (avaliacaoinstitucional.diasdisponivel||' days')::interval)");
        sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sql.append(" and horarioturmadiaitem.professor = ? ");
        sql.append(" where avaliacaoinstitucional.avaliacaoultimomodulo  = true and avaliacaoinstitucional.situacao = 'AT'");
        sql.append(" AND (avaliacaoinstitucional.publicoAlvo = '").append(PublicoAlvoAvaliacaoInstitucional.PROFESSORES.getValor()).append("')");
        sql.append(" and exists(");
        sql.append("  select t.* from (");
        sql.append("      select htdi.data as data, htdi.disciplina, htdi.professor from horarioturmadia htd ");
        sql.append("      inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo");
        sql.append("      and htd.horarioturma = horarioturma.codigo");
        sql.append("      and htdi.disciplina = horarioturmadiaitem.disciplina   ");
        sql.append("      and htdi.professor = horarioturmadiaitem.professor   ");
        sql.append("      order by htdi.data desc, htdi.horariotermino desc limit 1");
        sql.append("   ) as t where");
        sql.append("     (t.data::date + (avaliacaoinstitucional.diasdisponivel||' days')::interval + (avaliacaoinstitucional.horafim||' hours')::interval ) > (now()) ");
        sql.append("     and (t.data::date + (avaliacaoinstitucional.horainicio||' hours')::interval) <= (now()) ");
        sql.append("     and not exists (");
        sql.append(" 	select respostaavaliacaoinstitucionaldw.codigo from respostaavaliacaoinstitucionaldw  where respostaavaliacaoinstitucionaldw.avaliacaoinstitucional   = avaliacaoinstitucional.codigo");
        sql.append(" 	and respostaavaliacaoinstitucionaldw.turma = turma.codigo	");
        sql.append(" 	and respostaavaliacaoinstitucionaldw.disciplina = t.disciplina limit 1");
        sql.append("    )");
        sql.append(" ) ");
        if (limit > 0) {
            sql.append(" limit ").append(limit);
        }
        sql.append(" ) ");
        return montarDadosConsultaRapida(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), usuarioVO.getPessoa().getCodigo()));
    }

    public List<AvaliacaoInstitucionalVO> consultarPorNomeAtivosFinalizados(String valorConsulta, Integer limite, Integer offset,
                                                           boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM AvaliacaoInstitucional WHERE sem_acentos(nome) ilike(sem_acentos(?))");
        if (Uteis.isAtributoPreenchido(usuario.getUnidadeEnsinoLogado())) {
            sqlStr.append(" and unidadeensino = ").append(usuario.getUnidadeEnsinoLogado().getCodigo());
        }
        sqlStr.append(" and  upper(avaliacaoinstitucional.situacao) in ('FI', 'AT') ");
        sqlStr.append(" ORDER BY avaliacaoinstitucional.datainicio desc, nome ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(),
                "%" + valorConsulta + "%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

}
