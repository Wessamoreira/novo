package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoHorarioEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
//import negocio.comuns.financeiro.CondicaoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TurnoInterfaceFacade;
import webservice.servicos.TurnoRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TurnoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>TurnoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see TurnoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Turno extends ControleAcesso implements TurnoInterfaceFacade {

	private static final long serialVersionUID = -8877841982409908375L;

	protected static String idEntidade;

    public Turno() throws Exception {
        super();
        setIdEntidade("Turno");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>TurnoVO</code>.
     */
    public TurnoVO novo() throws Exception {
        Turno.incluir(getIdEntidade());
        TurnoVO obj = new TurnoVO();
        return obj;
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TurnoVO</code>. Todos os tipos de consistência de dados são e devem
     * ser implementadas neste método. São validações típicas: verificação de
     * campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public void validarDados(TurnoVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Turno) deve ser informado.");
        }
        if (obj.getTipoHorario().equals(TipoHorarioEnum.HORARIO_FIXO)) {
            if (obj.getNrAulas() == null || obj.getNrAulas().intValue() == 0)  {
                throw new ConsistirException("O campo NÚMERO AULAS (Turno) deve ser informado.");
            }
            if (obj.getNrAulasAntes() == null || obj.getNrAulasAntes().intValue() == 0) {
                throw new ConsistirException("O campo NÚMERO AULAS ANTES INTERVALO (Turno) deve ser informado.");
            }
            if (obj.getDuracaoAula() == null || obj.getDuracaoAula().intValue() == 0) {
                throw new ConsistirException("O campo DURAÇÃO DA AULA (Turno) deve ser informado.");
            }
            if (obj.getHoraInicio().equals("")) {
                throw new ConsistirException("O campo HORA INÍCIO (Turno) deve ser informado.");
            }
            if (obj.getHoraInicio().length() < 5) {
                throw new ConsistirException("O campo HORA INÍCIO (Turno) está com o formato errado.");
            }
            obj.setHoraInicio(Uteis.gethoraHHMMSS(obj.getHoraInicio()));
            if (obj.getHoraInicio().equals("")) {
                throw new ConsistirException("O campo HORA INÍCIO (Turno) informado não existe.");
            }
            if (obj.getDuracaoIntervalo() == null || obj.getDuracaoIntervalo().intValue() == 0) {
                throw new ConsistirException("O campo DURAÇÃO INTERVALO (Turno) deve ser informado.");
            }
            obj.setTurnoHorarioDomingo(null);
            obj.setTurnoHorarioSegunda(null);
            obj.setTurnoHorarioTerca(null);
            obj.setTurnoHorarioQuarta(null);
            obj.setTurnoHorarioQuinta(null);
            obj.setTurnoHorarioSexta(null);
            obj.setTurnoHorarioSabado(null);
            executarPreenchimentoHorarioTurno(obj);
        } else {
            if (obj.getTurnoHorarioDomingo().isEmpty() && obj.getTurnoHorarioSegunda().isEmpty() && obj.getTurnoHorarioTerca().isEmpty() && obj.getTurnoHorarioQuarta().isEmpty()
                    && obj.getTurnoHorarioQuinta().isEmpty() && obj.getTurnoHorarioSexta().isEmpty() && obj.getTurnoHorarioSabado().isEmpty()) {
                throw new ConsistirException("Deve ser informado pelo menos um dia da semana para a criação do turno.");
            }
            obj.setDuracaoAula(0);
            obj.setDuracaoIntervalo(0);
            obj.setFinalIntervalo("");
            obj.setHoraFinal("");
            obj.setHoraInicio("");
            obj.setInicioIntervalo("");
            obj.setNrAulas(0);
            obj.setNrAulasAntes(0);
            getFacadeFactory().getTurnoHorarioFacade().validarDadosTurnoHorarioVO(obj.getTurnoHorarioDomingo(), DiaSemana.DOMINGO);
            getFacadeFactory().getTurnoHorarioFacade().validarDadosTurnoHorarioVO(obj.getTurnoHorarioSegunda(), DiaSemana.SEGUNGA);
            getFacadeFactory().getTurnoHorarioFacade().validarDadosTurnoHorarioVO(obj.getTurnoHorarioTerca(), DiaSemana.TERCA);
            getFacadeFactory().getTurnoHorarioFacade().validarDadosTurnoHorarioVO(obj.getTurnoHorarioQuarta(), DiaSemana.QUARTA);
            getFacadeFactory().getTurnoHorarioFacade().validarDadosTurnoHorarioVO(obj.getTurnoHorarioQuinta(), DiaSemana.QUINTA);
            getFacadeFactory().getTurnoHorarioFacade().validarDadosTurnoHorarioVO(obj.getTurnoHorarioSexta(), DiaSemana.SEXTA);
            getFacadeFactory().getTurnoHorarioFacade().validarDadosTurnoHorarioVO(obj.getTurnoHorarioSabado(), DiaSemana.SABADO);
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>TurnoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>TurnoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TurnoVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
//            TurnoVO.validarPeriodoIntervalo(obj);
            incluir(getIdEntidade(), verificarAcesso, usuarioVO);
            final String sql = "INSERT INTO Turno( nome, horaInicio, nrAulasAntes, nrAulas, duracaoAula, duracaoIntervalo, horaFinal, inicioIntervalo, finalIntervalo, tipoHorario, naoApresentarHorarioVisaoAluno, considerarHoraAulaSessentaMinutosGeracaoDiario, descricaoTurnoContrato , ocultarhorarioaulavisaoprofessor) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getHoraInicio());
                    sqlInserir.setInt(3, obj.getNrAulasAntes().intValue());
                    sqlInserir.setInt(4, obj.getNrAulas().intValue());
                    sqlInserir.setInt(5, obj.getDuracaoAula().intValue());
                    sqlInserir.setInt(6, obj.getDuracaoIntervalo().intValue());
                    sqlInserir.setString(7, obj.getHoraFinal());
                    sqlInserir.setString(8, obj.getInicioIntervalo());
                    sqlInserir.setString(9, obj.getFinalIntervalo());
                    sqlInserir.setString(10, obj.getTipoHorario().toString());
                    sqlInserir.setBoolean(11, obj.getNaoApresentarHorarioVisaoAluno());
                    sqlInserir.setBoolean(12, obj.getConsiderarHoraAulaSessentaMinutosGeracaoDiario());
					sqlInserir.setString(13, obj.getDescricaoTurnoContrato());
					sqlInserir.setBoolean(14, obj.getOcultarHorarioAulaVisaoProfessor());	
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            getFacadeFactory().getTurnoHorarioFacade().incluirTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioDomingo());
            getFacadeFactory().getTurnoHorarioFacade().incluirTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioSegunda());
            getFacadeFactory().getTurnoHorarioFacade().incluirTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioTerca());
            getFacadeFactory().getTurnoHorarioFacade().incluirTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioQuarta());
            getFacadeFactory().getTurnoHorarioFacade().incluirTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioQuinta());
            getFacadeFactory().getTurnoHorarioFacade().incluirTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioSexta());
            getFacadeFactory().getTurnoHorarioFacade().incluirTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioSabado());
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            obj.setCodigo(0);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>TurnoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>TurnoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TurnoVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
//            TurnoVO.validarPeriodoIntervalo(obj);
            alterar(getIdEntidade(), verificarAcesso, usuarioVO);
            final String sql = "UPDATE Turno set nome=?, horaInicio=?, nrAulasAntes=?, nrAulas=?, duracaoAula=?, duracaoIntervalo=?, horaFinal=?, inicioIntervalo=?, finalIntervalo=?, tipoHorario=?, naoApresentarHorarioVisaoAluno = ?, considerarHoraAulaSessentaMinutosGeracaoDiario = ?, descricaoTurnoContrato=?, ocultarhorarioaulavisaoprofessor=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getHoraInicio());
                    sqlAlterar.setInt(3, obj.getNrAulasAntes().intValue());
                    sqlAlterar.setInt(4, obj.getNrAulas().intValue());
                    sqlAlterar.setInt(5, obj.getDuracaoAula().intValue());
                    sqlAlterar.setInt(6, obj.getDuracaoIntervalo().intValue());
                    sqlAlterar.setString(7, obj.getHoraFinal());
                    sqlAlterar.setString(8, obj.getInicioIntervalo());
                    sqlAlterar.setString(9, obj.getFinalIntervalo());
                    sqlAlterar.setString(10, obj.getTipoHorario().toString());
                    sqlAlterar.setBoolean(11, obj.getNaoApresentarHorarioVisaoAluno());
                    sqlAlterar.setBoolean(12, obj.getConsiderarHoraAulaSessentaMinutosGeracaoDiario());
                    sqlAlterar.setString(13, obj.getDescricaoTurnoContrato());
                    sqlAlterar.setBoolean(14, obj.getOcultarHorarioAulaVisaoProfessor());
                    sqlAlterar.setInt(15, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

            getFacadeFactory().getTurnoHorarioFacade().alterarTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioDomingo(), DiaSemana.DOMINGO);
            getFacadeFactory().getTurnoHorarioFacade().alterarTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioSegunda(), DiaSemana.SEGUNGA);
            getFacadeFactory().getTurnoHorarioFacade().alterarTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioTerca(), DiaSemana.TERCA);
            getFacadeFactory().getTurnoHorarioFacade().alterarTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioQuarta(), DiaSemana.QUARTA);
            getFacadeFactory().getTurnoHorarioFacade().alterarTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioQuinta(), DiaSemana.QUINTA);
            getFacadeFactory().getTurnoHorarioFacade().alterarTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioSexta(), DiaSemana.SEXTA);
            getFacadeFactory().getTurnoHorarioFacade().alterarTurnoHorarioVOs(obj.getCodigo(), obj.getTurnoHorarioSabado(), DiaSemana.SABADO);
            getAplicacaoControle().removerTurno(obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>TurnoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>TurnoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TurnoVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
        try {
            excluir(getIdEntidade(), verificarAcesso, usuarioVO);
            String sql = "DELETE FROM Turno WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getTurnoHorarioFacade().excluirTurnoHorarioVOs(obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    /***
     *  Método responsável por trazer os turnos em que o professor  da aula em determinado ano e semestre
     * @param codigoProfessor
     * @param ano
     * @param semestre
     * @param nivelMontarDados
     * @return
     * @throws Exception
     */
    public List<TurnoVO> consultarPorProfessor(Integer codigoProfessor, int nivelMontarDados) throws Exception {
        StringBuilder sqlStr = new StringBuilder(" SELECT DISTINCT turno.finalintervalo, turno.iniciointervalo,turno.duracaoaula, turno.nraulas, turno.horafinal, turno.horainicio, turno.nome, turno.codigo,  turno.duracaointervalo,  turno.nraulasantes, turno.tipohorario, turno.naoApresentarHorarioVisaoAluno, turno.descricaoTurnoContrato ");
        sqlStr.append(" FROM turno INNER JOIN horarioprofessor ON horarioprofessor.turno = turno.codigo ");
        sqlStr.append(" WHERE horarioprofessor.professor = ?  ORDER BY turno.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{codigoProfessor});
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public void validarDadosGeracaoTurnoHorario(Integer numeroAula, Integer duracaoAula) throws ConsistirException {
		if (numeroAula == null || numeroAula == 0) {
            throw new ConsistirException(("O campo NUMERO AULA (Turno) deve ser informado!"));
        }
        if (duracaoAula == null || duracaoAula == 0) {
            throw new ConsistirException(("O campo DURAÇÃO AULA (Turno) deve ser informado!"));
        }
	}

    /** Este método tem a função de incializar a lista do dia da semana que está sendo manipulada, ou seja,
     * irá preencher a lista do dia da semana escolhido com a quantidade de objetos TurnoHorarioVO de acordo
     * com o atributo numero de aulas.
     *
     * O parametro duração de aula é adicionada ao objeto para futuro calculo da hora final da aula.
     *
     * @param turnoVO
     * @param numeroAula
     * @param diaSemana
     * @param duracaoAula
     * @throws Exception
     */
    public void inicializarDadosListaTurnoHorarioVO(TurnoVO turnoVO, Integer numeroAula, DiaSemana diaSemana, Integer duracaoAula, Boolean permiteAlterar) throws Exception {
        validarDadosGeracaoTurnoHorario(numeroAula, duracaoAula);
        List<TurnoHorarioVO> turnoHorarioVOs = new ArrayList<TurnoHorarioVO>(0);
        int x = 1;
       
        	if (diaSemana.equals(DiaSemana.DOMINGO)) {
        		turnoHorarioVOs = turnoVO.getTurnoHorarioDomingo();
            } else if (diaSemana.equals(DiaSemana.SEGUNGA)) {
            	turnoHorarioVOs = turnoVO.getTurnoHorarioSegunda();
            } else if (diaSemana.equals(DiaSemana.TERCA)) {
            	turnoHorarioVOs = turnoVO.getTurnoHorarioTerca();
            } else if (diaSemana.equals(DiaSemana.QUARTA)) {
            	turnoHorarioVOs = turnoVO.getTurnoHorarioQuarta();
            } else if (diaSemana.equals(DiaSemana.QUINTA)) {
            	turnoHorarioVOs = turnoVO.getTurnoHorarioQuinta();
            } else if (diaSemana.equals(DiaSemana.SEXTA)) {
            	turnoHorarioVOs = turnoVO.getTurnoHorarioSexta();
            } else if (diaSemana.equals(DiaSemana.SABADO)) {
            	turnoHorarioVOs = turnoVO.getTurnoHorarioSabado();
            }
        	if(!turnoHorarioVOs.isEmpty()){
        		x  = turnoHorarioVOs.size()+1;
        	}
       
        while (x <= numeroAula) {
            TurnoHorarioVO turnoHorarioVO = new TurnoHorarioVO();
            turnoHorarioVO.setDiaSemana(diaSemana);
            turnoHorarioVO.setNumeroAula(x);
            turnoHorarioVO.setDuracaoAula(duracaoAula);
            turnoHorarioVOs.add(turnoHorarioVO);
            x++;
        }
        if (diaSemana.equals(DiaSemana.DOMINGO)) {
            turnoVO.setTurnoHorarioDomingo(turnoHorarioVOs);
        } else if (diaSemana.equals(DiaSemana.SEGUNGA)) {
            turnoVO.setTurnoHorarioSegunda(turnoHorarioVOs);
        } else if (diaSemana.equals(DiaSemana.TERCA)) {
            turnoVO.setTurnoHorarioTerca(turnoHorarioVOs);
        } else if (diaSemana.equals(DiaSemana.QUARTA)) {
            turnoVO.setTurnoHorarioQuarta(turnoHorarioVOs);
        } else if (diaSemana.equals(DiaSemana.QUINTA)) {
            turnoVO.setTurnoHorarioQuinta(turnoHorarioVOs);
        } else if (diaSemana.equals(DiaSemana.SEXTA)) {
            turnoVO.setTurnoHorarioSexta(turnoHorarioVOs);
        } else if (diaSemana.equals(DiaSemana.SABADO)) {
            turnoVO.setTurnoHorarioSabado(turnoHorarioVOs);
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Turno</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TurnoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @Override
    public List<TurnoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Turno WHERE lower (sem_acentos(nome)) ilike(sem_acentos(?)) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase() + "%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List<TurnoVO> consultarPorNome(String valorConsulta, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        List<Object> filtros = new ArrayList<>();
        filtros.add(valorConsulta.toLowerCase() + "%");
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM CursoTurno, Turno WHERE ((CursoTurno.turno = Turno.codigo) and (lower (Turno.nome) like(?)))");
        if(Uteis.isAtributoPreenchido(codigoCurso)) {
        	sqlStr.append(" and (CursoTurno.curso = ?)");
        	filtros.add(codigoCurso);
        }
        sqlStr.append(" ORDER BY nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), filtros.toArray());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List<TurnoVO> consultarPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct turno.* FROM turno");
        sqlStr.append(" INNER JOIN unidadeEnsinoCurso ON unidadeEnsinoCurso.turno = turno.codigo");
        sqlStr.append(" WHERE unidadeEnsinoCurso.unidadeEnsino = ? AND unidadeEnsinoCurso.curso = ? ORDER BY turno.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{codigoUnidadeEnsino, codigoCurso});
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

//    public List<TurnoVO> consultarPorUnidadeEnsinoCondicaoRenegociacao(List<CondicaoRenegociacaoUnidadeEnsinoVO> listaCondicaoRenegociacaoUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
//        StringBuilder sqlStr = new StringBuilder("SELECT distinct (turno.codigo), turno.finalintervalo, turno.iniciointervalo, turno.duracaoaula, turno.nraulas, ");
//        sqlStr.append("turno.horafinal, turno.horainicio, turno.nome, turno.duracaointervalo, turno.nraulasantes, turno.tipohorario, turno.naoApresentarHorarioVisaoAluno, turno.considerarhoraaulasessentaminutosgeracaodiario , turno.descricaoturnocontrato ");
//        sqlStr.append("FROM turno ");
//        sqlStr.append("INNER JOIN unidadeEnsinoCurso ON unidadeEnsinoCurso.turno = turno.codigo ");
//        sqlStr.append("WHERE 1=1 ");
//        if (!listaCondicaoRenegociacaoUnidadeEnsinoVOs.isEmpty()) {
//        	sqlStr.append(" AND ").append("unidadeEnsinoCurso.unidadeEnsino in (");
//			for (CondicaoRenegociacaoUnidadeEnsinoVO condicaoUnidadeEnsinoVO : listaCondicaoRenegociacaoUnidadeEnsinoVOs) {
//				if (condicaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
//					sqlStr.append(condicaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()).append(", ");
//				}
//			}
//			sqlStr.append("0) ");
//		}
//        sqlStr.append("ORDER BY turno.nome;");
//        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
//        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
//    }

    /**
     * Responsável por realizar uma consulta de <code>Turno</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TurnoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<TurnoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Turno WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>TurnoVO</code> resultantes da consulta.
     */
    public  List<TurnoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<TurnoVO> vetResultado = new ArrayList<TurnoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>TurnoVO</code>.
     * @return  O objeto da classe <code>TurnoVO</code> com os dados devidamente montados.
     */
    public  TurnoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ////System.out.println(">> Montar dados(Turno) - " + new Date());
        TurnoVO obj = new TurnoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setHoraInicio(dadosSQL.getString("horaInicio"));
        obj.setHoraFinal(dadosSQL.getString("horaFinal"));
        obj.setNrAulasAntes(dadosSQL.getInt("nrAulasAntes"));
        obj.setDuracaoIntervalo(dadosSQL.getInt("duracaoIntervalo"));
        obj.setDescricaoTurnoContrato(dadosSQL.getString("descricaoTurnoContrato"));		
        obj.setNrAulas(dadosSQL.getInt("nrAulas"));
        obj.setDuracaoAula(dadosSQL.getInt("duracaoAula"));
        obj.setInicioIntervalo(dadosSQL.getString("inicioIntervalo"));
        obj.setFinalIntervalo(dadosSQL.getString("finalIntervalo"));
        obj.setTipoHorario(TipoHorarioEnum.valueOf(TipoHorarioEnum.class, dadosSQL.getString("tipoHorario")));
        obj.setNaoApresentarHorarioVisaoAluno(dadosSQL.getBoolean("naoApresentarHorarioVisaoAluno"));
        obj.setConsiderarHoraAulaSessentaMinutosGeracaoDiario(dadosSQL.getBoolean("considerarHoraAulaSessentaMinutosGeracaoDiario"));
        obj.setNovoObj(Boolean.FALSE);
        obj.setOcultarHorarioAulaVisaoProfessor(dadosSQL.getBoolean("ocultarhorarioaulavisaoprofessor"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        getFacadeFactory().getTurnoHorarioFacade().consultarTurnoHorarioVOsSeparadoPorDiaSemana(obj);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    public void realizarMontagemHorarios(TurnoVO obj) {
        try {
            obj.setHoraInicio(Uteis.gethoraHHMMSS(obj.getHoraInicio()));
            if ((obj.getNrAulas().intValue() != 0) || (obj.getNrAulasAntes().intValue() != 0) || (obj.getDuracaoAula().intValue() != 0)
                    || (obj.getDuracaoIntervalo() != 0) || (obj.getHoraInicio()).equals("")) {
                Integer duracaoTotalAntesIntervalo = obj.getDuracaoAula() * obj.getNrAulasAntes();
                Integer duracaoTotalDepoisIntervalo = obj.getDuracaoAula() * (obj.getNrAulas() - obj.getNrAulasAntes());
                obj.setInicioIntervalo(Uteis.somarHorario(obj.getHoraInicio(), duracaoTotalAntesIntervalo));
                obj.setFinalIntervalo(Uteis.somarHorario(obj.getInicioIntervalo(), obj.getDuracaoIntervalo()));
                obj.setHoraFinal(Uteis.somarHorario(obj.getFinalIntervalo(), duracaoTotalDepoisIntervalo));
            }

        } catch (Exception e) {
            obj.setInicioIntervalo("");
            obj.setFinalIntervalo("");
            obj.setHoraFinal("");
        }
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>TurnoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public TurnoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	return getAplicacaoControle().getTurnoVO(codigoPrm, usuario);
    }

    public TurnoVO consultarTurnoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select * from turno left join matricula on matricula.turno = turno.codigo where matricula.matricula = '" + valorConsulta + "' ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return new TurnoVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public TurnoVO consultarTurnoPorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT turno.* FROM turno LEFT JOIN turma ON turma.turno = turno.codigo WHERE turma.identificadorTurma = '" + valorConsulta + "' ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return new TurnoVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public TurnoVO consultarTurnoPorMatriculaPeriodoUnidadeEnsinoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sb = new StringBuilder();
    	sb.append("select turno.* from turno ");
    	sb.append(" inner join unidadeensinocurso uec on uec.turno = turno.codigo ");
    	sb.append(" inner join matriculaperiodo on matriculaperiodo.unidadeensinocurso = uec.codigo ");
    	sb.append(" where matriculaperiodo.codigo = ").append(valorConsulta);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if (!tabelaResultado.next()) {
    		return new TurnoVO();
    	}
    	return (montarDados(tabelaResultado, nivelMontarDados));
    }

    public List<TurnoVO> consultarPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        List<TurnoVO> listaTurno;
        String sqlStr = "SELECT distinct turno.* FROM turno INNER JOIN unidadeensinocurso uec ON uec.turno = turno.codigo WHERE uec.curso = " + valorConsulta+" order by turno.nome ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.wasNull()) {
            listaTurno = new ArrayList<TurnoVO>(0);
        }
        listaTurno = (montarDadosConsulta(tabelaResultado, nivelMontarDados));
        return listaTurno;
    }
    
    public List<TurnoVO> consultarPorCodigoCursos(List<CursoVO> cursos, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT distinct turno.* FROM turno INNER JOIN unidadeensinocurso uec ON uec.turno = turno.codigo ");
        StringBuilder sqlCurso = new StringBuilder();
        if (!cursos.isEmpty()) {
        	sqlCurso.append(" WHERE uec.curso in (");
        	String auxCurso = "";
	        for (CursoVO curso : cursos) {
	        	if (curso.getFiltrarCursoVO()) {
	        		sqlCurso.append(auxCurso).append(curso.getCodigo());
	        		auxCurso = ",";
	        	}
	        }
	        if (auxCurso.equals("")) {
	        	for (CursoVO curso : cursos) {
	        		sqlCurso.append(auxCurso).append(curso.getCodigo());
	        		auxCurso = ",";
	            }
	        }
	        sqlCurso.append(") ");
        }
        sqlStr.append(sqlCurso);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    public List<TurnoVO> consultarPorCodigoCursoUnidadeEnsino(Integer curso, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select distinct turno.* from turno ");
        sqlStr.append(" inner join unidadeEnsinocurso on unidadeEnsinocurso.turno = turno.codigo ");
        sqlStr.append(" WHERE 1=1 ");
        if (curso != null && curso != 0) {
        	sqlStr.append(" and unidadeEnsinocurso.curso = ");
        	sqlStr.append(curso);
        }
        if (unidadeEnsino != null && unidadeEnsino != 0) {
            sqlStr.append(" AND unidadeEnsinocurso.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino);
        }
        sqlStr.append(" ORDER BY turno.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.wasNull()) {
            return new ArrayList<>(0);
        }
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);

    }

    public String realizarConsultaHorarioInicioFim(TurnoVO obj, DiaSemana diaSemana, Integer nrAula) {
        if (obj.getTipoHorario().equals(TipoHorarioEnum.HORARIO_FIXO) && (!obj.getIsExisteHorarioDomingo()
                || !obj.getIsExisteHorarioSegunda() || !obj.getIsExisteHorarioTerca() || !obj.getIsExisteHorarioQuarta()
                || !obj.getIsExisteHorarioQuinta() || !obj.getIsExisteHorarioSexta() || !obj.getIsExisteHorarioSabado())) {
            executarPreenchimentoHorarioTurno(obj);
        }
        return realizarSelecaoTurnoHorario(obj, diaSemana, nrAula);
    }
    
    
    @Override
    public Integer realizarConsultaDuracaoHorarioInicioFim(TurnoVO obj, DiaSemana diaSemana, Integer nrAula) {
    	if (obj.getTipoHorario().equals(TipoHorarioEnum.HORARIO_FIXO) && (!obj.getIsExisteHorarioDomingo()
    			|| !obj.getIsExisteHorarioSegunda() || !obj.getIsExisteHorarioTerca() || !obj.getIsExisteHorarioQuarta()
    			|| !obj.getIsExisteHorarioQuinta() || !obj.getIsExisteHorarioSexta() || !obj.getIsExisteHorarioSabado())) {
    		return obj.getDuracaoAula();
    	}
    	TurnoHorarioVO turnoHorarioVO = consultarObjTurnoHorarioVOs(obj, diaSemana, nrAula);
        if (turnoHorarioVO == null) {
            return obj.getDuracaoAula();
        }
        return turnoHorarioVO.getDuracaoAula();
    }

    private String realizarSelecaoTurnoHorario(TurnoVO obj, DiaSemana diaSemana, Integer nrAula) {
        TurnoHorarioVO turnoHorarioVO = consultarObjTurnoHorarioVOs(obj, diaSemana, nrAula);
        if (turnoHorarioVO == null) {
            return "";
        }
        return turnoHorarioVO.getDescricaoHorario();
    }

    public void executarPreenchimentoHorarioTurno(TurnoVO turno) {
        int x = 1;
        String horaInicio = turno.getHoraInicio();
        String horaFinal = "";
        if (!turno.getHoraInicio().equals("")) {
            while (x <= turno.getNrAulas()) {
                horaFinal = Uteis.getCalculodeHoraSemIntervalo(horaInicio, 1, turno.getDuracaoAula());
                adicionarObjTurnoHorarioDoTipoFixo(turno, horaInicio, horaFinal, turno.getDuracaoAula(), x, false);
                if (x == turno.getNrAulasAntes()) {
                    horaInicio = turno.getFinalIntervalo();
                    //adicionarObjTurnoHorarioDoTipoFixo(obj, horaFinal, horaInicio, obj.getDuracaoAula(), x, true);
                } else {
                    horaInicio = horaFinal;
                }
                x++;
            }
        }
    }

    public TurnoHorarioVO consultarObjTurnoHorarioVOs(TurnoVO obj, DiaSemana diaSemana, Integer nrAula) {
        List<TurnoHorarioVO> turnoHorarioVOs = null;
        if (diaSemana.equals(DiaSemana.DOMINGO)) {
            turnoHorarioVOs = obj.getTurnoHorarioDomingo();
        } else if (diaSemana.equals(DiaSemana.SEGUNGA)) {
            turnoHorarioVOs = obj.getTurnoHorarioSegunda();
        } else if (diaSemana.equals(DiaSemana.TERCA)) {
            turnoHorarioVOs = obj.getTurnoHorarioTerca();
        } else if (diaSemana.equals(DiaSemana.QUARTA)) {
            turnoHorarioVOs = obj.getTurnoHorarioQuarta();
        } else if (diaSemana.equals(DiaSemana.QUINTA)) {
            turnoHorarioVOs = obj.getTurnoHorarioQuinta();
        } else if (diaSemana.equals(DiaSemana.SEXTA)) {
            turnoHorarioVOs = obj.getTurnoHorarioSexta();
        } else if (diaSemana.equals(DiaSemana.SABADO)) {
            turnoHorarioVOs = obj.getTurnoHorarioSabado();
        }
        for (TurnoHorarioVO turnoHorarioVO : turnoHorarioVOs) {
            if (turnoHorarioVO.getNumeroAula().intValue() == nrAula.intValue()) {
                return turnoHorarioVO;
            }
        }
        return null;
    }

    public Integer consultarNumeroAulaTurnoHorarioVOs(TurnoVO turno, DiaSemana diaSemana) {
        if (turno.getTipoHorario().equals(TipoHorarioEnum.HORARIO_FIXO) && (!turno.getIsExisteHorarioDomingo()
                || !turno.getIsExisteHorarioSegunda() || !turno.getIsExisteHorarioTerca() || !turno.getIsExisteHorarioQuarta()
                || !turno.getIsExisteHorarioQuinta() || !turno.getIsExisteHorarioSexta() || !turno.getIsExisteHorarioSabado())) {
            executarPreenchimentoHorarioTurno(turno);
        }
        Integer nrAula = 0;
        if (diaSemana.equals(DiaSemana.DOMINGO)) {
            nrAula = turno.getTurnoHorarioDomingo().size();
        } else if (diaSemana.equals(DiaSemana.SEGUNGA)) {
            nrAula = turno.getTurnoHorarioSegunda().size();
        } else if (diaSemana.equals(DiaSemana.TERCA)) {
            nrAula = turno.getTurnoHorarioTerca().size();
        } else if (diaSemana.equals(DiaSemana.QUARTA)) {
            nrAula = turno.getTurnoHorarioQuarta().size();
        } else if (diaSemana.equals(DiaSemana.QUINTA)) {
            nrAula = turno.getTurnoHorarioQuinta().size();
        } else if (diaSemana.equals(DiaSemana.SEXTA)) {
            nrAula = turno.getTurnoHorarioSexta().size();
        } else if (diaSemana.equals(DiaSemana.SABADO)) {
            nrAula = turno.getTurnoHorarioSabado().size();
        }
        return nrAula;
    }

    public TurnoHorarioVO inicializarDadosTurnoHorarioDoTipoFixo(String horaInicial, String horaFinal, Integer duracaoAula, Integer numeroAula, DiaSemana diaSemana, Boolean intervalo) {
        TurnoHorarioVO horarioVO = new TurnoHorarioVO();
        horarioVO.setDiaSemana(diaSemana);
        horarioVO.setDuracaoAula(duracaoAula);
        horarioVO.setHorarioFinalAula(horaFinal);
        horarioVO.setHorarioInicioAula(horaInicial);
        horarioVO.setNumeroAula(numeroAula);
        horarioVO.setIntervalo(intervalo);
        return horarioVO;
    }

    public void adicionarObjTurnoHorarioDoTipoFixo(TurnoVO obj, String horaInicial, String horaFinal, Integer duracaoAula, Integer numeroAula, Boolean intervalo) {
        obj.getTurnoHorarioDomingo().add(inicializarDadosTurnoHorarioDoTipoFixo(horaInicial, horaFinal, duracaoAula, numeroAula, DiaSemana.DOMINGO, intervalo));
        obj.getTurnoHorarioSegunda().add(inicializarDadosTurnoHorarioDoTipoFixo(horaInicial, horaFinal, duracaoAula, numeroAula, DiaSemana.SEGUNGA, intervalo));
        obj.getTurnoHorarioTerca().add(inicializarDadosTurnoHorarioDoTipoFixo(horaInicial, horaFinal, duracaoAula, numeroAula, DiaSemana.TERCA, intervalo));
        obj.getTurnoHorarioQuarta().add(inicializarDadosTurnoHorarioDoTipoFixo(horaInicial, horaFinal, duracaoAula, numeroAula, DiaSemana.QUARTA, intervalo));
        obj.getTurnoHorarioQuinta().add(inicializarDadosTurnoHorarioDoTipoFixo(horaInicial, horaFinal, duracaoAula, numeroAula, DiaSemana.QUINTA, intervalo));
        obj.getTurnoHorarioSexta().add(inicializarDadosTurnoHorarioDoTipoFixo(horaInicial, horaFinal, duracaoAula, numeroAula, DiaSemana.SEXTA, intervalo));
        obj.getTurnoHorarioSabado().add(inicializarDadosTurnoHorarioDoTipoFixo(horaInicial, horaFinal, duracaoAula, numeroAula, DiaSemana.SABADO, intervalo));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Turno.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Turno.idEntidade = idEntidade;
    }
    
    public Boolean consultarTransferenciaTurnoPorMatricula(String matricula, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select distinct count(distinct uec.turno) from matriculaperiodo ");
    	sb.append(" inner join unidadeensinocurso uec on uec.codigo = matriculaperiodo.unidadeensinocurso ");
    	sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
    	sb.append(" where matricula = '").append(matricula).append("' ");
    	sb.append(" group by matricula ");
    	sb.append(" having count(distinct uec.turno) > 1");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if (tabelaResultado.next()) {
    		return true;
    	}
    	return false;
    }
    
	@Override
	public TurnoVO consultarTurnoPorCodigoTurma(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT turno.* FROM turno LEFT JOIN turma ON turma.turno = turno.codigo WHERE turma.codigo = " + turma + "";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados);
		}
		return new TurnoVO();
	}
       
    
    public TurnoVO consultaRapidaPorMatricula(String matricula, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select turno.codigo, turno.nome from turno ");
    	sb.append(" inner join matricula on matricula.turno = turno.codigo ");
    	sb.append(" where matricula.matricula = '").append(matricula).append("'  ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	TurnoVO turnoVO = new TurnoVO();
    	if (tabelaResultado.next()) {
    		turnoVO.setCodigo(tabelaResultado.getInt("codigo"));
    		turnoVO.setNome(tabelaResultado.getString("nome"));
    	}
    	return turnoVO;
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarCriacaoHorarioTurnoFixo() throws Exception{
    	UsuarioVO usuarioVO = new UsuarioVO();
    	StringBuilder sql = new StringBuilder("select * from turno where tipoHorario = '"+TipoHorarioEnum.HORARIO_FIXO.name()+"' and codigo not in (select turno from turnohorario) ");
    	List<TurnoVO> turnoVOs = montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), Uteis.NIVELMONTARDADOS_TODOS);
    	for(TurnoVO turnoVO:turnoVOs){
    		executarPreenchimentoHorarioTurno(turnoVO);
    		alterar(turnoVO, false, usuarioVO);
    	}
    	usuarioVO = null;
    }
    
    @Override
    public List<TurnoVO> consultarTurnoPorCodigoBanner(Integer codigoBanner, Integer codigoUnidadeEnsino, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" select distinct unidadeensinocurso.turno as turno");
    	sqlStr.append(" from processomatricula  ");
    	sqlStr.append(" inner join processomatriculaunidadeensino on processomatriculaunidadeensino.processomatricula = processomatricula.codigo");
    	sqlStr.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo");
    	sqlStr.append(" inner join unidadeensinocurso on unidadeensinocurso.unidadeensino = processomatriculaunidadeensino.unidadeensino and unidadeensinocurso.turno = processomatriculacalendario.turno   and unidadeensinocurso.curso = processomatriculacalendario.curso ");
    	sqlStr.append(" where processomatriculacalendario.politicadivulgacaomatriculaonline = ").append(codigoBanner);
    	sqlStr.append(" and unidadeensinocurso.unidadeensino = ").append(codigoUnidadeEnsino);
    	sqlStr.append(" and processomatricula.situacao in ('AT', 'PR') ");
    	sqlStr.append(" and processomatricula.apresentarprocessovisaoaluno  ");
    	sqlStr.append(" and processomatricula.datainiciomatriculaonline <= now()");
    	sqlStr.append(" and processomatricula.datafimmatriculaonline >= now()");
    	sqlStr.append(" and (select turma.codigo from turma  where turma.curso = unidadeensinocurso.curso and turma.turno = unidadeensinocurso.turno ");
    	sqlStr.append(" and turma.unidadeensino = unidadeensinocurso.unidadeensino and turma.situacao = 'AB' ");
    	sqlStr.append(" and turma.periodoletivo in (");
    	sqlStr.append(" select periodoletivo.codigo from gradecurricular ");
    	sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo  where periodoletivo.periodoletivo = 1 ");
    	sqlStr.append(" and gradecurricular.curso = unidadeensinocurso.curso and gradecurricular.situacao = 'AT') ");
    	sqlStr.append(" and  (select planofinanceirocurso from condicaopagamentoplanofinanceirocurso ");
    	sqlStr.append("	where condicaopagamentoplanofinanceirocurso.tipousocondicaopagamento = 'MATRICULA_REGULAR' ");
    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.situacao = 'AT' ");
    	if(!Uteis.isAtributoPreenchido(usuarioVO) || !Uteis.isAtributoPreenchido(usuarioVO.getVisaoLogar())){
    		sqlStr.append("	and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineExterna  ");
    	}else if(Uteis.isAtributoPreenchido(usuarioVO) && usuarioVO.getIsApresentarVisaoCoordenador()){
    		sqlStr.append("	and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineCoordenador  ");
    	}else if(Uteis.isAtributoPreenchido(usuarioVO) && usuarioVO.getIsApresentarVisaoProfessor()){
    		sqlStr.append("	and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineProfessor  ");
    	}else {
    		sqlStr.append("	and condicaopagamentoplanofinanceirocurso.apresentarCondicaoVisaoAluno  ");
    	}
    	sqlStr.append("	and condicaopagamentoplanofinanceirocurso.planofinanceirocurso = case when turma.planofinanceirocurso is not null then turma.planofinanceirocurso else unidadeensinocurso.planofinanceirocurso end ");
    	sqlStr.append("	limit 1");
    	sqlStr.append("	) is not null limit 1) is not null");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        TurnoVO turnoVO = null;
        List<TurnoVO> turnoVOs = new ArrayList<TurnoVO>();
        while(rs.next()) {
        	turnoVO = new TurnoVO();
        	turnoVO.setCodigo(rs.getInt("turno"));
        	turnoVO = consultarPorChavePrimaria(turnoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
        	turnoVOs.add(turnoVO);	
        }
        return turnoVOs;
    }

    @Override
    public List<TurnoVO> consultarTurnoPorProcSeletivoUnidadeEnsinosComboBox(List<ProcSeletivoVO> procSeletivoVOs,List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception {
    	StringBuilder sb = new StringBuilder();
        sb.append("select distinct turno.codigo, turno.nome from turno ") ;
        sb.append(" inner join unidadeensinocurso ON unidadeensinocurso.turno = turno.codigo ");
        sb.append(" inner join procseletivocurso on procseletivocurso.unidadeensinocurso = unidadeEnsinoCurso.codigo ");
        sb.append(" inner join procseletivounidadeensino on procseletivounidadeensino.codigo = procseletivocurso.procseletivounidadeensino ");
        sb.append(" and procseletivounidadeensino.unidadeensino = unidadeEnsinoCurso.unidadeensino ");
        sb.append(" where 1=1 ");
        if (!procSeletivoVOs.isEmpty()) {
            sb.append(" and procseletivounidadeensino.procSeletivo in (");
            for (ProcSeletivoVO procSeletivoVO : procSeletivoVOs) {
            	if (procSeletivoVO.getFiltrarProcessoSeletivo()) {
            		sb.append(procSeletivoVO.getCodigo()).append(", ");
            	}
            }
            sb.append("0) ");
        }
        if (!unidadeEnsinoVOs.isEmpty()) {
            sb.append(" and unidadeensinocurso.unidadeensino in (");
            for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
            	if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
            		sb.append(unidadeEnsinoVO.getCodigo()).append(", ");
            	}
            }
            sb.append("0) ");
        }
        sb.append(" order by turno.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        TurnoVO obj = null;
        List<TurnoVO> listaTurnoVOs = null;
        while (tabelaResultado.next()) {
        	if (listaTurnoVOs == null) {
        		listaTurnoVOs = new ArrayList<TurnoVO>(0);
        	}
        	obj = new TurnoVO();
        	obj.setCodigo(tabelaResultado.getInt("codigo"));
        	obj.setNome(tabelaResultado.getString("nome"));
        	listaTurnoVOs.add(obj);
        }
        return listaTurnoVOs;
    }
    
    
    
    @Override
    public List<TurnoVO> consultarTurnoUsadoMatricula(List<UnidadeEnsinoVO> unidadeEnsinoVOs, int nivelMontarDados) throws Exception{
    	StringBuilder sql = new StringBuilder("select * from turno ");
    	sql.append(" where exists ( ");
    	sql.append(" select matricula.matricula from matricula where matricula.turno = turno.codigo ");
    	if(Uteis.isAtributoPreenchido(unidadeEnsinoVOs)){
    		int x = 0;
    		for(UnidadeEnsinoVO unidadeEnsinoVO: unidadeEnsinoVOs){    			
    			if(unidadeEnsinoVO.getFiltrarUnidadeEnsino()){
    				if(x==0){
    					sql.append(" and unidadeensino in (0 ");    					
    				}
    				sql.append(", ").append(unidadeEnsinoVO.getCodigo());
    				x++;
    			}
				if (x > 0) {
					sql.append(" ) ");
				}
    		}
    	}
    	sql.append(" ) order by nome ");    	
    	return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);
    }
    
    @Override
    public List<TurnoVO> consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct (turno.codigo), turno.finalintervalo, turno.iniciointervalo, turno.duracaoaula, turno.nraulas, ");
        sqlStr.append("turno.horafinal, turno.horainicio, turno.nome, turno.duracaointervalo, turno.nraulasantes, turno.tipohorario, turno.naoApresentarHorarioVisaoAluno, turno.considerarhoraaulasessentaminutosgeracaodiario , turno.descricaoturnocontrato ");
        sqlStr.append("FROM turno ");
        sqlStr.append("INNER JOIN unidadeEnsinoCurso ON unidadeEnsinoCurso.turno = turno.codigo ");
        sqlStr.append("WHERE unidadeEnsinoCurso.unidadeEnsino = ? ");
        sqlStr.append("ORDER BY turno.nome;");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoUnidadeEnsino);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
    
    @Override
    public TurnoVO consultarTurnoPorNomeUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Turno WHERE lower (sem_acentos(nome)) ilike(sem_acentos(?)) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase());
        if (!tabelaResultado.next()) {
            return new TurnoVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }
    
    @Override
    public TurnoVO consultarPorChavePrimaria(Integer chavePrimaria, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select * from turno where codigo = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), chavePrimaria);
    	TurnoVO turnoVO = new TurnoVO();
    	if (tabelaResultado.next()) {
    		turnoVO.setCodigo(tabelaResultado.getInt("codigo"));
    		turnoVO.setNome(tabelaResultado.getString("nome"));
    	}
    	return turnoVO;
    }
    
    
    @Override
    public TurnoVO consultarPorChavePrimariaUnico(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
    	String sql = "SELECT * FROM Turno WHERE codigo = ?";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
    	if (!tabelaResultado.next()) {
    		throw new ConsistirException("Dados Não Encontrados (Turno).");
    	}
    	return (montarDados(tabelaResultado, nivelMontarDados));
    }

    @Override
	public TurnoRSVO consultarTurnoMatriculaOnlineProcessoSeletivoPorCodigo(Integer codigoTurno , UsuarioVO usuario) {
		TurnoRSVO turnoRSVO = new TurnoRSVO();
		try {
			TurnoVO turnoVO = getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(codigoTurno, false,Uteis.NIVELMONTARDADOS_COMBOBOX,usuario);
			if (Uteis.isAtributoPreenchido(turnoVO)) {
				turnoRSVO.setCodigo(turnoVO.getCodigo());
				turnoRSVO.setNome(turnoVO.getNome());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return turnoRSVO;
	}
}
