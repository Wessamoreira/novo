package negocio.facade.jdbc.basico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEADIPOGVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.PessoaEADIPOGInterfaceFacade;


/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PessoaEADIPOGVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PessoaEADIPOGVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PessoaEADIPOGVO
 * @see ControleAcesso
 */

@Repository @Scope("singleton") @Lazy 
public class PessoaEADIPOG extends ControleAcesso implements PessoaEADIPOGInterfaceFacade {

	private static final long serialVersionUID = -2147711325199635532L;
	protected static String idEntidade;
	protected String cpfCorrespondente;
	protected String emailCorrespondente;
	protected String email2Correspondente;

    public PessoaEADIPOG() throws Exception {
        super();
        setIdEntidade("PessoaEADIPOG");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PessoaEADIPOGVO</code>.
     */
    public PessoaEADIPOGVO novo() throws Exception {
    	PessoaEADIPOGVO obj = new PessoaEADIPOGVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PessoaEADIPOGVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PessoaEADIPOGVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(final PessoaEADIPOGVO obj) throws Exception {
    	if (verificarExistenciaRegistroPessoaEADIPOG(obj.getAluno())) {
    		alterar(obj);
    	} else {
    		incluir(obj);
    	}
    }
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PessoaEADIPOGVO obj) throws Exception {
        try {
            PessoaEADIPOGVO.validarDados(obj);
            final String sql = "INSERT INTO PessoaEADIPOG( aluno, cpfCorrespondente, emailCorrespondente, email2Correspondente, matricula, disciplina, turma, dataInicio, situacao) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
             obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
     		  PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                  sqlInserir.setInt(1, obj.getAluno());
                  sqlInserir.setString(2, obj.getCpfCorrespondente());
                  sqlInserir.setString(3, obj.getEmailCorrespondente());
                  sqlInserir.setString(4, obj.getEmail2Correspondente());
                  sqlInserir.setString(5, obj.getMatricula());
                  sqlInserir.setInt(6, obj.getDisciplina());
                  sqlInserir.setInt(7, obj.getTurma());
                  sqlInserir.setDate(8, Uteis.getDataJDBC(new Date()));
                  sqlInserir.setString(9, obj.getSituacao());
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
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PessoaEADIPOGVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PessoaEADIPOGVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PessoaEADIPOGVO obj) throws Exception {
        try {
            PessoaEADIPOGVO.validarDados(obj);
            final String sql = "UPDATE PessoaEADIPOG set cpfCorrespondente=?, emailCorrespondente=?, email2Correspondente=?, matricula=?, disciplina=?, turma=?, dataAlteracao=?, situacao=?, msgErroAtualizacaoStatus=?, erroAtualizacaoStatus=? WHERE (aluno = ?)";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                        PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                        sqlAlterar.setString(1, obj.getCpfCorrespondente());
                        sqlAlterar.setString(2, obj.getEmailCorrespondente());
                        sqlAlterar.setString(3, obj.getEmail2Correspondente());                        
                        sqlAlterar.setString(4, obj.getMatricula());
                        sqlAlterar.setInt(5, obj.getDisciplina());
                        sqlAlterar.setInt(6, obj.getTurma());
                        sqlAlterar.setDate(7, Uteis.getDataJDBC(new Date()));
                        sqlAlterar.setString(8, obj.getSituacao());
                        sqlAlterar.setString(9, obj.getMsgErroAtualizacaoStatus());
                        sqlAlterar.setBoolean(10, obj.getErroAtualizacaoStatus());
                        sqlAlterar.setInt(11, obj.getAluno().intValue());
                        return sqlAlterar;
                }

            });

        } catch (Exception e) {
            throw e;
       }
    }

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacao(final PessoaEADIPOGVO obj) throws Exception {
        try {
            PessoaEADIPOGVO.validarDados(obj);
            final String sql = "UPDATE PessoaEADIPOG set dataAlteracao=?, situacao=?, msgErroAtualizacaoStatus=?, erroAtualizacaoStatus=? WHERE (aluno = ?)";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                        PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                        sqlAlterar.setDate(1, Uteis.getDataJDBC(new Date()));
                        sqlAlterar.setString(2, obj.getSituacao());
                        sqlAlterar.setString(3, obj.getMsgErroAtualizacaoStatus());
                        sqlAlterar.setBoolean(4, obj.getErroAtualizacaoStatus());
                        sqlAlterar.setInt(5, obj.getAluno().intValue());
                        return sqlAlterar;
                }

            });

        } catch (Exception e) {
            throw e;
       }
    }
	
	public void processarStatusAluno (HistoricoVO histTemp) throws Exception {
		try {						
			// processa retorno atualizando o historico e mudando a situacao do aluno caso o mesmo tenha concluido, 
			// altera a mensagem de erro para vazio e o boolean de erro para false;
//			if (histTemp.getSituacao().equals("AP") || histTemp.getSituacao().equals("CA")) {
				HistoricoVO hist = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatricula_Ano_Semestre_Disciplina(histTemp.getMatricula().getMatricula(), "", "", histTemp.getDisciplina().getCodigo(), histTemp.getMatriculaPeriodoTurmaDisciplina().getTurma().getCodigo(), false, null);
				
				if (histTemp.getSituacao().equals("AP")) {
					hist.setSituacao(SituacaoHistorico.APROVADO.getValor());	
				} else if (histTemp.getSituacao().equals("CA")) {
					hist.setSituacao(SituacaoHistorico.CANCELADO.getValor());			
				} else if (histTemp.getSituacao().equals("RE")) {
					hist.setSituacao(SituacaoHistorico.REPROVADO.getValor());			
				} else if (histTemp.getSituacao().equals("RF")) {
					hist.setSituacao(SituacaoHistorico.REPROVADO_FALTA.getValor());			
				}				
				hist.setMediaFinal(histTemp.getMediaFinal());				
				hist.setFreguencia(histTemp.getFreguencia());
				getFacadeFactory().getHistoricoFacade().alterar(hist, null);
//			}
				incluirLogHistorico(histTemp, null);
		} catch (Exception e) {
			histTemp.setObservacao(" Erro ao atualizar historico => " + e.getMessage());
			incluirLogHistorico(histTemp, null);
			//System.out.print(" Erro ao atualizar historico => " + e.getMessage());
		}				
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirLogHistorico(final HistoricoVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO LogHistorico( disciplina, matricula, tipoHistorico, situacao, freguencia, responsavel, dataRegistro, nota1, " // 1-8
					+ "nota2, nota3, nota4, nota5, nota6, nota7, nota8, nota9, nota10, nota11, nota12, nota13, matriculaPeriodo, matriculaPeriodoTurmaDisciplina, " // 9-22
					+ "mediaFinal, configuracaoAcademico,  " // 23-24
					+ "observacao " // 25
					+ " ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
					+ ") returning codigo"
					+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 0;
					sqlInserir.setInt(++i, obj.getDisciplina().getCodigo().intValue());
					sqlInserir.setString(++i, obj.getMatricula().getMatricula());
					sqlInserir.setString(++i, obj.getTipoHistorico());
					sqlInserir.setString(++i, obj.getSituacao());
					sqlInserir.setDouble(++i, obj.getFreguencia().doubleValue());
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataRegistro()));
					if (obj.getNota1() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota1().doubleValue());
					}
					if (obj.getNota2() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota2().doubleValue());
					}
					if (obj.getNota3() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota3().doubleValue());
					}
					if (obj.getNota4() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota4().doubleValue());
					}
					if (obj.getNota5() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota5().doubleValue());
					}
					if (obj.getNota6() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota6().doubleValue());
					}
					if (obj.getNota7() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota7().doubleValue());
					}
					if (obj.getNota8() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota8().doubleValue());
					}
					if (obj.getNota9() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota9().doubleValue());
					}
					if (obj.getNota10() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota10().doubleValue());
					}
					if (obj.getNota11() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota11().doubleValue());
					}
					if (obj.getNota12() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota12().doubleValue());
					}
					if (obj.getNota13() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getNota13().doubleValue());
					}
					sqlInserir.setInt(++i, obj.getMatriculaPeriodo().getCodigo().intValue());
					if (obj.getMatriculaPeriodoTurmaDisciplina().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getMatriculaPeriodoTurmaDisciplina().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (obj.getMediaFinal() == null) {
						sqlInserir.setNull(++i, 0);
					} else {
						sqlInserir.setDouble(++i, obj.getMediaFinal().doubleValue());
					}
					if (obj.getConfiguracaoAcademico().getCodigo().intValue() != 0) {
						sqlInserir.setInt(++i, obj.getConfiguracaoAcademico().getCodigo().intValue());
					} else {
						sqlInserir.setNull(++i, 0);
					}					
					sqlInserir.setString(++i, obj.getObservacao());					
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
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}
	
	/**
     * Operação responsável por excluir no BD um objeto da classe <code>PessoaEADIPOGVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PessoaEADIPOGVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PessoaEADIPOGVO obj) throws Exception {
        try {
            PessoaEADIPOG.excluir(getIdEntidade());
            String sql = "DELETE FROM PessoaEADIPOG WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PessoaEADIPOG</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PessoaEADIPOGVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PessoaEADIPOGVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM PessoaEADIPOG WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }
    
    public List<PessoaEADIPOGVO> consultarPorSituacao(String situacao, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT * FROM PessoaEADIPOG WHERE situacao = '" + situacao + "' ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }
    
    public Boolean verificarExistenciaRegistroPessoaEADIPOG(Integer codAluno) throws Exception {
    	String sqlStr = "SELECT * FROM PessoaEADIPOG WHERE aluno = " + codAluno.intValue() + " limit 1";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	if (tabelaResultado.next()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PessoaEADIPOGVO</code> resultantes da consulta.
     */
    public static List<PessoaEADIPOGVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List<PessoaEADIPOGVO> vetResultado = new ArrayList<PessoaEADIPOGVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PessoaEADIPOGVO</code>.
     * @return  O objeto da classe <code>PessoaEADIPOGVO</code> com os dados devidamente montados.
     */
    public static PessoaEADIPOGVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        PessoaEADIPOGVO obj = new PessoaEADIPOGVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setAluno(new Integer(dadosSQL.getInt("aluno")));
        obj.setCpfCorrespondente(dadosSQL.getString("cpfCorrespondente"));
        obj.setEmailCorrespondente(dadosSQL.getString("emailCorrespondente"));
        obj.setEmail2Correspondente(dadosSQL.getString("email2Correspondente"));
        
        obj.setMatricula(dadosSQL.getString("matricula"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setDisciplina(dadosSQL.getInt("disciplina"));
        obj.setTurma(dadosSQL.getInt("turma"));
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataAlteracao(dadosSQL.getDate("dataAlteracao"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PessoaEADIPOGVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PessoaEADIPOGVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false,usuario);
        String sql = "SELECT * FROM PessoaEADIPOG WHERE codigo = " + codigoPrm;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( PessoaEADIPOG ).");
        }
        return montarDados(tabelaResultado, nivelMontarDados,usuario);
    }
    
    public PessoaEADIPOGVO consultarPorDadosAluno(Integer pessoa, String matricula, Integer disciplina, Integer turma, int nivelMontarDados, UsuarioVO usuario) throws Exception {        
        String sql = "SELECT * FROM PessoaEADIPOG WHERE 1=1 ";
    	if (pessoa != 0) {
    		sql = sql + "and aluno = " + pessoa; 
    	}
    	sql = sql + " and matricula = '" + matricula + "' and disciplina = " + disciplina + " and turma = " + turma;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( PessoaEADIPOG ).");
        }
        return montarDados(tabelaResultado,nivelMontarDados, usuario);
    }    


    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return PessoaEADIPOG.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        PessoaEADIPOG.idEntidade = idEntidade;
    }
}