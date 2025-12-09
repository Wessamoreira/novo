package negocio.facade.jdbc.basico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.FeriadoInterfaceFacade;
import webservice.servicos.objetos.DataEventosRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>FeriadoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>FeriadoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see FeriadoVO
 * @see SuperEntidade
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class Feriado extends ControleAcesso implements FeriadoInterfaceFacade {

    protected static String idEntidade;
    protected static Integer cidadePadrao;

    public Feriado() throws Exception {
        super();
        setIdEntidade("Feriado");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>FeriadoVO</code>.
     */
    public FeriadoVO novo() throws Exception {
        incluir(getIdEntidade());
        FeriadoVO obj = new FeriadoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FeriadoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FeriadoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final FeriadoVO obj, UsuarioVO usuarioVO, boolean excluirAulaProgramada) throws Exception {
        try {
//        	if(excluirAulaProgramada  && obj.getConsiderarFeriadoAcademico()){
//        		getFacadeFactory().getHorarioTurmaDiaFacade().excluirAulaProgramadaComBaseFeriado(obj, usuarioVO);
//        	}
            FeriadoVO.validarDados(obj);
            incluir(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO Feriado( descricao, data, recorrente, cidade, nacional, considerarFeriadoFinanceiro, considerarFeriadoBiblioteca, considerarFeriadoAcademico ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);

                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    sqlInserir.setBoolean(3, obj.isRecorrente().booleanValue());
                    if (obj.getCidade().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getCidade().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setBoolean(5, obj.getNacional().booleanValue());
                    sqlInserir.setBoolean(6, obj.getConsiderarFeriadoFinanceiro().booleanValue());
                    sqlInserir.setBoolean(7, obj.getConsiderarFeriadoBiblioteca().booleanValue());
                    sqlInserir.setBoolean(8, obj.getConsiderarFeriadoAcademico().booleanValue());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        return arg0.getInt("codigo");
                    }
                    return 0;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);


        } catch (Exception e) {

            throw e;
        } finally {
        }
    }

    /***
     * Método responsável por verificar se existe aula programada para o dia no qual se deseja torna feriado
     * 
     * @param feriado
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<TurmaVO> executarValidarNaoPossuiAulaProgramada(FeriadoVO feriado, UsuarioVO usuario) throws Exception {
       List<TurmaVO> turmaVOs = new ArrayList<TurmaVO>(0);
       StringBuilder sql  = new StringBuilder("");
       sql.append("  select distinct turma.codigo, turma.identificadorturma, count(horarioturmadiaitem.codigo) as totalAulas from horarioturma");
       sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
       sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
       sql.append("  inner join turma on horarioturma.turma = turma.codigo");
       sql.append("  inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
       sql.append("  where ");
       if(!feriado.getRecorrente()){
    	   sql.append("  horarioturmadia.data = '").append(Uteis.getDataJDBC(feriado.getData())).append("' ");
       }else{
    	   sql.append("  to_char(horarioturmadia.data, 'dd/MM') = '").append(Uteis.getData(feriado.getData(), "dd/MM")).append("' ");
       }
       sql.append("  and horarioturmadiaitem.disciplina is not null");
       sql.append("  and not exists (select registroaula.codigo from registroaula where registroaula.turma = horarioturma.turma ");
       sql.append("  and horarioturmadia.data = registroaula.data and registroaula.disciplina = horarioturmadiaitem.disciplina )");
       if(!feriado.getNacional() && Uteis.isAtributoPreenchido(feriado.getCidade().getCodigo())){
    	   sql.append("  and unidadeensino.cidade = ").append(feriado.getCidade().getCodigo());
       }
       sql.append(" group by turma.codigo, turma.identificadorturma  order by turma.identificadorturma ");
       SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
       while(rs.next()){
    	   TurmaVO turmaVO = new TurmaVO();
    	   turmaVO.setCodigo(rs.getInt("codigo"));
    	   turmaVO.setIdentificadorTurma(rs.getString("identificadorturma"));
    	   turmaVO.setNrVagas(rs.getInt("totalAulas"));
    	   turmaVOs.add(turmaVO);
       }
       return turmaVOs;
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FeriadoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FeriadoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final FeriadoVO obj, UsuarioVO usuarioVO, boolean excluirAulaProgramada) throws Exception {
        try {
//        	if(excluirAulaProgramada  && obj.getConsiderarFeriadoAcademico()){
//        		getFacadeFactory().getHorarioTurmaDiaFacade().excluirAulaProgramadaComBaseFeriado(obj, usuarioVO);
//        	}
            FeriadoVO.validarDados(obj);
            alterar(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE Feriado set descricao=?, data=?, recorrente=?, cidade=?, nacional=?, considerarFeriadoFinanceiro=?, considerarFeriadoBiblioteca=?, considerarFeriadoAcademico=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
                    sqlAlterar.setBoolean(3, obj.isRecorrente().booleanValue());
                    if (obj.getCidade().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getCidade().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setBoolean(5, obj.getNacional().booleanValue());
                    sqlAlterar.setBoolean(6, obj.getConsiderarFeriadoFinanceiro().booleanValue());
                    sqlAlterar.setBoolean(7, obj.getConsiderarFeriadoBiblioteca().booleanValue());
                    sqlAlterar.setBoolean(8, obj.getConsiderarFeriadoAcademico().booleanValue());
                    sqlAlterar.setInt(9, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {


            throw e;
        } finally {
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FeriadoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FeriadoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(final FeriadoVO obj,  UsuarioVO usuarioVO) throws Exception {
        try {

            excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM Feriado WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    public Integer consultarNrDiasUteisConsiderandoSabado(Date dataInicial, Date dataFinal, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {
        Integer nrDiasUteisSemFimSemana = Uteis.getCalculaDiasUteisConsiderandoSabado(dataInicial, dataFinal);
//        if (cidade == null || cidade.intValue() == 0) {
//            cidade = getCidadePadrao();
//        }
        Integer nrFeriados = consultaNumeroFeriadoNoPeriodoConsiderandoSabado(dataInicial, dataFinal, cidade, considerarFeriadoEnum);
        return nrDiasUteisSemFimSemana - nrFeriados;
    }
    
    
    public Integer consultarNrDiasUteis(Date dataInicial, Date dataFinal, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {
        Integer nrDiasUteisSemFimSemana = Uteis.getCalculaDiasUteis(dataInicial, dataFinal);
//        if (cidade == null || cidade.intValue() == 0) {
//            cidade = getCidadePadrao();
//        }
        Integer nrFeriados = consultaNumeroFeriadoNoPeriodoDesconsiderandoFimSemana(dataInicial, dataFinal, cidade, considerarFeriadoEnum);
        return nrDiasUteisSemFimSemana - nrFeriados;
    }

   public Integer consultarNrDiasUteisProgredir(Date dataInicial, Integer diasPrevisto, Integer cidade, boolean considerarDiaInicialContagemDiasUteis, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {		
		Date dataFinal = Uteis.obterDataFuturaMesContabilConsiderandoDiaInicial(dataInicial, diasPrevisto);
		if (considerarDiaInicialContagemDiasUteis) {
			dataFinal = Uteis.getDataFuturaConsiderandoDataAtual(dataInicial, diasPrevisto);
		}
		return calcularNrDiasUteisProgredir(dataInicial, dataFinal, diasPrevisto, cidade, considerarDiaInicialContagemDiasUteis, considerarFeriadoEnum);
	}

	private Integer calcularNrDiasUteisProgredir(Date dataInicial, Date dataFinal, Integer diasPrevisto, Integer cidade, boolean considerarDiaInicialContagemDiasUteis, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {
		int nrDiasFimSemana = Uteis.getCalculaDiasNaoUteis(dataInicial, dataFinal);
		int nrDiasFeriado = consultaNumeroFeriadoNoPeriodoDesconsiderandoFimSemana(dataInicial, dataFinal, cidade, considerarFeriadoEnum);
		if (nrDiasFeriado == 0 && nrDiasFimSemana == 0) {
			return diasPrevisto;
		}
		diasPrevisto += (nrDiasFeriado + nrDiasFimSemana);
		Date dataInicial2 = null;
		Date dataFinal2 = null;
		if (considerarDiaInicialContagemDiasUteis) {
			dataInicial2 = Uteis.getDataFuturaConsiderandoDataAtual(dataFinal, 2);
			dataFinal2 = Uteis.getDataFuturaConsiderandoDataAtual(dataInicial2, (nrDiasFeriado + nrDiasFimSemana));
		} else {
			dataInicial2 = Uteis.obterDataFuturaMesContabilConsiderandoDiaInicial(dataFinal, 2);
			dataFinal2 = Uteis.obterDataFuturaMesContabilConsiderandoDiaInicial(dataFinal, (nrDiasFeriado + nrDiasFimSemana) + 1);
		}
		return calcularNrDiasUteisProgredir(dataInicial2, dataFinal2, diasPrevisto, cidade, considerarDiaInicialContagemDiasUteis, considerarFeriadoEnum);
	}

    public Boolean verificarFeriadoNesteDia(Date data, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT codigo FROM Feriado ");
        sqlStr.append(" WHERE ((CAST (data AS DATE) = CAST ('").append(Uteis.getDataJDBC(data)).append("' AS DATE)) ");
        sqlStr.append(" OR (recorrente IS true AND (EXTRACT(DAY FROM data) = EXTRACT(DAY FROM CAST ('").append(Uteis.getDataJDBC(data)).append("' AS DATE))) "); 
        sqlStr.append(" AND (EXTRACT(MONTH FROM data) = EXTRACT(MONTH FROM CAST ('").append(Uteis.getDataJDBC(data)).append("' AS DATE))))) ");
        if (cidade != null && cidade > 0) {
        	sqlStr.append(" AND (nacional IS true OR cidade = ").append(cidade.intValue()).append(") ");
        }
        sqlStr.append(getWhereConsiderarFeriado(considerarFeriadoEnum.name()));
        sqlStr.append(" LIMIT 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return true;
        } else {
            return false;
        }
    }

    public Integer consultaNumeroFeriadoNoPeriodoDesconsiderandoFimSemana(Date dataInicio, Date dataFim, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {
		StringBuilder sb = new StringBuilder("select count(data) as qtde from (");
		sb.append(" SELECT data, recorrente, ");
		sb.append(" cast(to_char(CASE WHEN NOT recorrente THEN DATA ELSE CAST(('").append(Uteis.getAno(dataInicio)).append("-' || EXTRACT(MONTH FROM DATA)|| '-' || EXTRACT(DAY FROM DATA)) AS DATE) END, 'd') as int) as diaSemana ");
		sb.append(" FROM Feriado ");
		sb.append(" WHERE ( ");
		sb.append(" (recorrente = false and CAST (data AS DATE) >= CAST ('" + Uteis.getDataJDBC(dataInicio) + "' AS DATE) AND CAST (data AS DATE) <= CAST ('" + Uteis.getDataJDBC(dataFim) + "' AS DATE))  ");
		sb.append(" 	OR (recorrente IS true ");
		sb.append(" 	and cast(('").append(Uteis.getAno(dataInicio)).append("-'||EXTRACT(MONTH FROM data)||'-'||EXTRACT(DAY FROM data)) as DATE) >= CAST('").append(Uteis.getDataJDBC(dataInicio)).append("' as DATE) ");
		sb.append(" 	and cast(('").append(Uteis.getAno(dataFim)).append("-'||EXTRACT(MONTH FROM data)||'-'||EXTRACT(DAY FROM data)) as DATE) <= CAST('").append(Uteis.getDataJDBC(dataFim)).append("' as DATE) ");
		sb.append(" 	)");
		sb.append(" ) ");
		sb.append(getWhereConsiderarFeriado(considerarFeriadoEnum.name()));
		if (cidade != null && cidade > 0) {
			sb.append(" AND (nacional IS true OR cidade = " + cidade + ") group by data, recorrente ");
		} else {
			sb.append(" AND (nacional IS true) ");
			sb.append(" group by data, recorrente ");
			
		}
		sb.append(" ) as t where t.diaSemana >= 2 and t.diaSemana <=6 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;

	}
	
    private Integer consultaNumeroFeriadoNoPeriodoConsiderandoSabado(Date dataInicio, Date dataFim, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {
		StringBuilder sb = new StringBuilder("select count(data) as qtde from (");
		sb.append(" SELECT data, recorrente, cast(to_char(data, 'd') as int) as diaSemana FROM Feriado ");
		sb.append(" WHERE ( ");
		sb.append(" (CAST (data AS DATE) >= CAST ('" + Uteis.getDataJDBC(dataInicio) + "' AS DATE) AND CAST (data AS DATE) <= CAST ('" + Uteis.getDataJDBC(dataFim) + "' AS DATE))  ");
		sb.append(" 	OR (recorrente IS true ");
		sb.append(" 	and cast(('").append(Uteis.getAno(dataInicio)).append("-'||EXTRACT(MONTH FROM data)||'-'||EXTRACT(DAY FROM data)) as DATE) >= CAST('").append(Uteis.getDataJDBC(dataInicio)).append("' as DATE) ");
		sb.append(" 	and cast(('").append(Uteis.getAno(dataFim)).append("-'||EXTRACT(MONTH FROM data)||'-'||EXTRACT(DAY FROM data)) as DATE) <= CAST('").append(Uteis.getDataJDBC(dataFim)).append("' as DATE) ");
		sb.append(" 	)");
		sb.append(" ) ");
		sb.append(getWhereConsiderarFeriado(considerarFeriadoEnum.name()));
		if (cidade != null && cidade > 0) {
			sb.append(" AND (nacional IS true OR cidade = " + cidade + ") group by data, recorrente ");
		} else {
			sb.append(" AND (nacional IS true) group by data");
		}
		sb.append(" ) as t where t.diaSemana >= 2 and t.diaSemana <=7 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;

	}
    
    @Override
    public List<DataEventosRSVO> consultaDiasFeriadoNoPeriodoPorDataEventos(Date dataInicio, Date dataFim, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum, int nivelMontaDados, UsuarioVO usuario) throws Exception {
    	List<FeriadoVO> listaFeriado = consultaDiasFeriadoNoPeriodo(dataInicio, dataFim, cidade, considerarFeriadoEnum, nivelMontaDados, usuario);
    	List<DataEventosRSVO> dataEventosRSVOs = new ArrayList<>();
    	for (FeriadoVO feriado : listaFeriado) {
    		Calendar calendar = Calendar.getInstance();
			calendar.setTime(feriado.getData());
			DataEventosRSVO dataEventosRSVO = new DataEventosRSVO();
			dataEventosRSVO.setAno(calendar.get(Calendar.YEAR));
			dataEventosRSVO.setMes(calendar.get(Calendar.MONTH));
			dataEventosRSVO.setDia(calendar.get(Calendar.DAY_OF_MONTH));
			dataEventosRSVO.setColor("#b8b8b8");
			dataEventosRSVO.setTextColor("#000000");
			dataEventosRSVO.setStyleClass("horarioFeriado");
			dataEventosRSVO.setData(feriado.getData());
			dataEventosRSVOs.add(dataEventosRSVO);
			
		}
		return dataEventosRSVOs;
    }

   public List<FeriadoVO> consultaDiasFeriadoNoPeriodo(Date dataInicio, Date dataFim, Integer cidade, ConsiderarFeriadoEnum considerarFeriadoEnum, int nivelMontaDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * FROM Feriado ");
		sb.append(" WHERE ( ");
		sb.append(" (CAST (data AS DATE) >= CAST ('" + Uteis.getDataJDBC(dataInicio) + "' AS DATE) AND CAST (data AS DATE) <= CAST ('" + Uteis.getDataJDBC(dataFim) + "' AS DATE))  ");
		sb.append(" 	OR (recorrente IS true ");
		sb.append(" 	and cast(('").append(Uteis.getAno(dataInicio)).append("-'||EXTRACT(MONTH FROM data)||'-'||EXTRACT(DAY FROM data)) as DATE) >= CAST('").append(Uteis.getDataJDBC(dataInicio)).append("' as DATE) ");
		sb.append(" 	and cast(('").append(Uteis.getAno(dataFim)).append("-'||EXTRACT(MONTH FROM data)||'-'||EXTRACT(DAY FROM data)) as DATE) <= CAST('").append(Uteis.getDataJDBC(dataFim)).append("' as DATE) ");
		sb.append(" 	)");
		sb.append(" ) ");
		sb.append(getWhereConsiderarFeriado(considerarFeriadoEnum.name()));
		if (cidade != null && cidade > 0) {
			sb.append(" AND (nacional IS true OR cidade = " + cidade + ") group by data, nacional,codigo, cidade, recorrente, descricao ");
		} else {
			sb.append(" AND (nacional IS true) group by data, nacional,codigo, cidade, recorrente, descricao");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontaDados, usuario);

	}   

    public List consultarPorDescricao(String valorConsulta, String cidade, String considerarFeriado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), true, usuario);
    	List<Object> parametros = new ArrayList<>();
    	StringBuilder sb = new StringBuilder(" ");
    	sb.append(" SELECT * FROM Feriado ");
    	sb.append(" left join cidade on cidade.codigo = feriado.cidade ");
    	sb.append(" where upper(descricao) ilike ?");
    	parametros.add(PERCENT + valorConsulta + PERCENT);
    	if(Uteis.isAtributoPreenchido(cidade)){
    		sb.append(" and (nacional = true or cidade.nome ilike ? ").append(")");
    		parametros.add(cidade);
    	}
    	sb.append(getWhereConsiderarFeriado(considerarFeriado));
    	sb.append(" ORDER BY data ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), parametros.toArray());
    	return montarDadosConsulta (tabelaResultado, nivelMontarDados, usuario);
    }
    
	
    
    
    public List<FeriadoVO> consultarPorData(Date data, String considerarFeriado, Integer cidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sb = new StringBuilder(" ");
		sb.append(" SELECT * FROM Feriado where ");
        sb.append(" ((recorrente = false and CAST (data AS DATE) = CAST ('" + Uteis.getDataJDBC(data) + "' AS DATE)) ");
		sb.append(" OR ");
		sb.append(" (recorrente = true ");		
		sb.append(" and EXTRACT(MONTH FROM data) = ").append(Uteis.getMesData(data));
		sb.append(" and EXTRACT(DAY FROM data) =  ").append(Uteis.getDiaMesData(data));
		sb.append(" )) ");
		if(Uteis.isAtributoPreenchido(cidade)){
    		sb.append(" and (nacional = true or feriado.cidade= ").append(cidade).append(")");
    	}
		sb.append(getWhereConsiderarFeriado(considerarFeriado));
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, usuario));
    }
    
    public List consultarPorPeriodo(Date dataini, Date datafim, String considerarFeriado, String cidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sb = new StringBuilder(" ");
    	sb.append(" SELECT * FROM Feriado ");
    	sb.append(" left join cidade on cidade.codigo = feriado.cidade ");
    	sb.append(" WHERE ( ");
		sb.append(" (recorrente = false and CAST (data AS DATE) >= CAST ('" + Uteis.getDataJDBC(dataini) + "' AS DATE) AND CAST (data AS DATE) <= CAST ('" + Uteis.getDataJDBC(datafim) + "' AS DATE))  ");
		sb.append(" 	OR (recorrente = true ");
		sb.append(" 	and cast(('").append(Uteis.getAno(dataini)).append("-'||EXTRACT(MONTH FROM data)||'-'||EXTRACT(DAY FROM data)) as DATE) >= CAST('").append(Uteis.getDataJDBC(dataini)).append("' as DATE) ");
		sb.append(" 	and cast(('").append(Uteis.getAno(datafim)).append("-'||EXTRACT(MONTH FROM data)||'-'||EXTRACT(DAY FROM data)) as DATE) <= CAST('").append(Uteis.getDataJDBC(datafim)).append("' as DATE) ");
		sb.append(" 	)");
		sb.append(" ) ");
		if(Uteis.isAtributoPreenchido(cidade)){
    		sb.append(" and (nacional = true or upper(cidade.nome) like ('").append(cidade.toUpperCase()).append("%'))");
    	}
    	sb.append(getWhereConsiderarFeriado(considerarFeriado));
    	sb.append(" ORDER BY data ");
    	return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, usuario));
    }

    public List consultarTodosFeriados(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        //consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Feriado ORDER BY data";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr), nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Feriado</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FeriadoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorAno(String valorConsulta, String considerarFeriado, String cidade, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), true, usuario);
        StringBuilder sb = new StringBuilder(" ");
    	sb.append(" SELECT * FROM Feriado ");
    	sb.append(" left join cidade on cidade.codigo = feriado.cidade ");
    	sb.append(" where ((recorrente = false and to_char(feriado.data,'YYYY') = '").append(valorConsulta).append("') or recorrente = true) ");
    	if(Uteis.isAtributoPreenchido(cidade)){
    		sb.append(" and (nacional = true or upper(cidade.nome) like ('").append(cidade.toUpperCase()).append("%'))");
    	}
    	sb.append(getWhereConsiderarFeriado(considerarFeriado));
    	sb.append(" ORDER BY data ");
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, usuario));
    }
    
    public String getWhereConsiderarFeriado(String considerarFeriado){
    	 String sqlStr = "";
    	 if(considerarFeriado.equals(ConsiderarFeriadoEnum.FINANCEIRO.name())){
         	sqlStr += " and  considerarFeriadoFinanceiro = true ";
         }else if(considerarFeriado.equals(ConsiderarFeriadoEnum.BIBLIOTECA.name())){
         	sqlStr += " and  considerarFeriadoBiblioteca = true ";
         }else if(considerarFeriado.equals(ConsiderarFeriadoEnum.ACADEMICO.name())){
         	sqlStr += " and  considerarFeriadoAcademico = true ";
         }else if(considerarFeriado.equals(ConsiderarFeriadoEnum.FINANCEIRO_BIBLIOTECA.name())){
         	sqlStr += " and  (considerarFeriadoFinanceiro = true  or considerarFeriadoBiblioteca = true) ";
         }else if(considerarFeriado.equals(ConsiderarFeriadoEnum.FINANCEIRO_ACADEMICO.name())){
         	sqlStr += " and  (considerarFeriadoFinanceiro = true or considerarFeriadoAcademico = true) ";
         }else if(considerarFeriado.equals(ConsiderarFeriadoEnum.BIBLIOTECA_ACADEMICO.name())){
         	sqlStr += " and  (considerarFeriadoBiblioteca = true or considerarFeriadoAcademico = true) ";
         }
    	 return sqlStr;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>FeriadoVO</code> resultantes da consulta.
     */
    public List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>FeriadoVO</code>.
     * @return  O objeto da classe <code>FeriadoVO</code> com os dados devidamente montados.
     */
    public FeriadoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        FeriadoVO obj = new FeriadoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setRecorrente(new Boolean(dadosSQL.getBoolean("recorrente")));
        obj.setNacional(new Boolean(dadosSQL.getBoolean("nacional")));
        obj.setConsiderarFeriadoFinanceiro(new Boolean(dadosSQL.getBoolean("considerarFeriadoFinanceiro")));
        obj.setConsiderarFeriadoBiblioteca(new Boolean(dadosSQL.getBoolean("considerarFeriadoBiblioteca")));
        obj.setConsiderarFeriadoAcademico(new Boolean(dadosSQL.getBoolean("considerarFeriadoAcademico")));
        obj.getCidade().setCodigo(new Integer(dadosSQL.getInt("cidade")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosCidade(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CidadeVO</code> relacionado ao objeto <code>FeriadoVO</code>.
     * Faz uso da chave primária da classe <code>CidadeVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public void montarDadosCidade(FeriadoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCidade().getCodigo().intValue() == 0) {
            obj.setCidade(new CidadeVO());
            return;
        }
        obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>FeriadoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FeriadoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sql = "SELECT * FROM Feriado WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Feriado ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
   
    
    @Override
    public Integer calcularNrDiasUteis(Date dataInicio, Date dataFim, Integer cidade, Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {
    	Calendar dataCalendarInicial = Calendar.getInstance();
        Calendar dataCalendarFinal = Calendar.getInstance();
        dataCalendarInicial.setTime(dataInicio);
        dataCalendarInicial.add(Calendar.DAY_OF_MONTH, 1);
        dataCalendarFinal.setTime(dataFim);
        long qtdDiasCorrido = Uteis.nrDiasEntreDatas(Uteis.getDateHoraFinalDia(dataFim), Uteis.getDateHoraInicialDia(dataInicio));
        long quantidadeDiasNaoUteis = obterDiasNaoUteisNoPeriodo(dataCalendarInicial, dataCalendarFinal, cidade, considerarSabadoDiaUtil, considerarDomingoDiaUtil, considerarFeriadoEnum);
    	return ((int) (qtdDiasCorrido - quantidadeDiasNaoUteis)) ;
    }
    
    
    @Override
    public boolean validarDataSeVesperaFimDeSemana(Date data, Integer cidade, Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {
    	Date proximadata = UteisData.obterDataFuturaUsandoCalendar(data, 1);
    	if(!UteisData.isDiaDaSemana(proximadata, considerarSabadoDiaUtil, considerarDomingoDiaUtil)){
    		return true;
    	}else if(verificarFeriadoNesteDia(proximadata, cidade, considerarFeriadoEnum, false, null)){
    		return true;
    		//return validarDataSeVesperaFimDeSemana(proximadata, cidade, considerarFeriadoEnum);
    	}
    	return false;
    }
    
    /**
	 * @author Wendel Rodrigues
	 * @since 27 de março de 2015
	 * @version 5.0.3.2
	 * considerarSabado = caso true é considerado como día util
	 * considerarFeriado = caso true é considerado como día util
	 * considerarApenasFeriadoFinanceiro = caso true e o parametro considerarFeriado for true então será considerado apenas feriados que geram impactos no finacneiro, fazendo com que recesso escolares seja considerado financeiramente como dia útil. 
	 * Realiza o calculo da data para o próximo dia útil considerando os feriados cadastrado no sistema.
	 */
    @Override
	public Date obterDataFuturaProximoDiaUtil(Date dataInicial, Integer cidade, Boolean considerarSabado, Boolean considerarFeriado, ConsiderarFeriadoEnum considerarFeriadoEnum, UsuarioVO usuarioVO) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(dataInicial);
		while (true) {
			if ((c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) && (!considerarSabado)) {
				c.add(GregorianCalendar.DAY_OF_MONTH, 2);
				return obterDataFuturaProximoDiaUtil(c.getTime(), cidade, considerarSabado, considerarFeriado, considerarFeriadoEnum, usuarioVO);
			} else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				c.add(GregorianCalendar.DAY_OF_MONTH, 1);
				return obterDataFuturaProximoDiaUtil(c.getTime(), cidade, considerarSabado, considerarFeriado, considerarFeriadoEnum,  usuarioVO);
			} else if ((!considerarFeriado) && (verificarFeriadoNesteDia(c.getTime(), cidade, considerarFeriadoEnum, false, usuarioVO))) {
				c.add(GregorianCalendar.DAY_OF_MONTH, 1);
				return obterDataFuturaProximoDiaUtil(c.getTime(), cidade, considerarSabado, considerarFeriado, considerarFeriadoEnum, usuarioVO);
			} else return c.getTime();
		}
	}
    
    @Override
    public Date obterDataFuturaOuRetroativaApenasDiasUteis( Date data, int nrDiasProgredir, Integer cidade,  Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {
        Calendar dataCalendarInicial = Calendar.getInstance();
        Calendar dataCalendarFinal = Calendar.getInstance();
        dataCalendarInicial.setTime(data);
        dataCalendarFinal.setTime(data);

        if (nrDiasProgredir >= 0) {
            /* Obtendo data futura contando dias úteis. */
        	dataCalendarInicial.add(Calendar.DAY_OF_MONTH, 1);
            dataCalendarFinal.add(Calendar.DAY_OF_MONTH, nrDiasProgredir);
            long quantidadeDiasNaoUteis = obterDiasNaoUteisNoPeriodo(dataCalendarInicial, dataCalendarFinal, cidade, considerarSabadoDiaUtil, considerarDomingoDiaUtil, considerarFeriadoEnum);
            while (quantidadeDiasNaoUteis > 0l) {
                dataCalendarInicial.setTime(dataCalendarFinal.getTime());
                dataCalendarInicial.add(Calendar.DAY_OF_MONTH, 1);
                dataCalendarFinal.add(Calendar.DAY_OF_MONTH, (int) quantidadeDiasNaoUteis);
                quantidadeDiasNaoUteis = obterDiasNaoUteisNoPeriodo(dataCalendarInicial, dataCalendarFinal, cidade, considerarSabadoDiaUtil, considerarDomingoDiaUtil, considerarFeriadoEnum);
            }
            return dataCalendarFinal.getTime();
        } else {
            /* Obtendo data retroativa contando dias úteis. */
        	dataCalendarFinal.add(Calendar.DAY_OF_MONTH, -1);
            dataCalendarInicial.add(Calendar.DAY_OF_MONTH, nrDiasProgredir);
            long quantidadeDiasNaoUteis = obterDiasNaoUteisNoPeriodo(dataCalendarInicial, dataCalendarFinal, cidade, considerarSabadoDiaUtil, considerarDomingoDiaUtil, considerarFeriadoEnum);
            while (quantidadeDiasNaoUteis > 0l) {
                dataCalendarFinal.setTime(dataCalendarInicial.getTime());
                dataCalendarFinal.add(Calendar.DAY_OF_MONTH, -1);
                dataCalendarInicial.add(Calendar.DAY_OF_MONTH, (int) quantidadeDiasNaoUteis * -1);
                quantidadeDiasNaoUteis = obterDiasNaoUteisNoPeriodo(dataCalendarInicial, dataCalendarFinal, cidade, considerarSabadoDiaUtil, considerarDomingoDiaUtil, considerarFeriadoEnum);
            }
            return dataCalendarInicial.getTime();
        }
    }
    
    private long obterDiasNaoUteisNoPeriodo(final Calendar dataInicial, final Calendar dataFinal, Integer cidade,  Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {
        Calendar dataInicio = Calendar.getInstance();
        Calendar dataFim = Calendar.getInstance();
        dataInicio.setTime(dataInicial.getTime());
        dataFim.setTime(dataFinal.getTime());

        long diasNaoUteis = 0l;
        if (dataInicio.equals(dataFim)) {
            if (!isDiaUtil(dataInicio.getTime(), cidade, considerarSabadoDiaUtil, considerarDomingoDiaUtil, considerarFeriadoEnum)) {
                return 1;
            } else {
                return 0;
            }
        } else if (dataFim.before(dataInicio)) {
            while (UteisData.getCompareData(dataInicio.getTime(), dataFim.getTime()) >= 0) {
                if (!isDiaUtil(dataInicio.getTime(), cidade, considerarSabadoDiaUtil, considerarDomingoDiaUtil, considerarFeriadoEnum)) {
                    diasNaoUteis++;
                }
                dataInicio.add(Calendar.DAY_OF_MONTH, -1);
            }
        } else {
            while (UteisData.getCompareData(dataInicio.getTime(), dataFim.getTime()) <= 0) {
                if (!isDiaUtil(dataInicio.getTime(), cidade, considerarSabadoDiaUtil, considerarDomingoDiaUtil, considerarFeriadoEnum)) {
                    diasNaoUteis++;
                }
                dataInicio.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return diasNaoUteis;
    }
    
    @Override
    public boolean isDiaUtil(final Date date, Integer cidade,  Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, ConsiderarFeriadoEnum considerarFeriadoEnum) throws Exception {
        if (!UteisData.isDiaDaSemana(date, considerarSabadoDiaUtil, considerarDomingoDiaUtil)) {
            return false;
        }else if (verificarFeriadoNesteDia(date, cidade, considerarFeriadoEnum, false, null)) {
            return false;
        }
        return true;
    } 
    
    
    @Override
    public FeriadoVO executarValidacaoDataFeriado(List<FeriadoVO> feriadoVOs, Date data) {
		if (feriadoVOs != null) {
			for (FeriadoVO feriadoVO : feriadoVOs) {
				if (!feriadoVO.getRecorrente() && Uteis.getData(feriadoVO.getData()).equals(Uteis.getData(data))) {
					return feriadoVO;
				} else if (feriadoVO.getRecorrente() && Uteis.getDiaMesData(feriadoVO.getData()) == Uteis.getDiaMesData(data) && Uteis.getMesData(feriadoVO.getData()) == Uteis.getMesData(data)) {
					return feriadoVO;
				}
			}
		}
		return null;
	}

	
	
    
    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Feriado.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Feriado.idEntidade = idEntidade;
    }

	

	
}
