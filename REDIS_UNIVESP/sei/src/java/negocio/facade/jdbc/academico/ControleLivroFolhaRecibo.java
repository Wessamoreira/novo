package negocio.facade.jdbc.academico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ControleLivroFolhaReciboInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ControleLivroFolhaReciboVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ControleLivroFolhaReciboVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ControleLivroFolhaReciboVO
 * @see SuperEntidade
*/
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ControleLivroFolhaRecibo extends ControleAcesso  implements ControleLivroFolhaReciboInterfaceFacade {
	
	private static final long serialVersionUID = 2596684044584966474L;

	protected static String idEntidade;
	
    public ControleLivroFolhaRecibo() throws Exception {
        super();
        setIdEntidade("ControleLivroFolhaRecibo");
    }
	
    public ControleLivroFolhaReciboVO novo() throws Exception {
        ControleLivroFolhaRecibo.incluir(getIdEntidade());
        ControleLivroFolhaReciboVO obj = new ControleLivroFolhaReciboVO();
        return obj;
    }

    @SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ControleLivroFolhaReciboVO obj, UsuarioVO usuario) throws Exception {
        try {
            ControleLivroFolhaReciboVO.validarDados(obj);
            ControleLivroFolhaRecibo.incluir(getIdEntidade());
            final String sql = "INSERT INTO ControleLivroFolhaRecibo( controleLivroRegistroDiploma, matricula, folhaReciboAtual,numeroRegistro, dataPublicacao, via, responsavel, dataCadastro, situacao  ) VALUES ( ?, ?, ?, ?, ?,?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    if (obj.getControleLivroRegistroDiploma().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getControleLivroRegistroDiploma().getCodigo().intValue() );
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (!obj.getMatricula().getMatricula().equals("")) {
                        sqlInserir.setString(2, obj.getMatricula().getMatricula() );
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setInt( 3, obj.getFolhaReciboAtual().intValue() );
                    sqlInserir.setInt(4, obj.getNumeroRegistro());
                    if (obj.getDataPublicacao() != null) {
                    	sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataPublicacao()));
                    } else {
                    	sqlInserir.setNull(5, 0);
                    }
                    sqlInserir.setString(6, obj.getVia());
                    sqlInserir.setInt(7, obj.getResponsavel().getCodigo());
                    sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
                    sqlInserir.setString(9, obj.getSituacao());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ControleLivroFolhaReciboVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ControleLivroFolhaReciboVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ControleLivroFolhaReciboVO obj, UsuarioVO usuario) throws Exception {
    	ControleLivroFolhaReciboVO.validarDados(obj);
        ControleLivroFolhaRecibo.alterar(getIdEntidade());
        obj.realizarUpperCaseDados();

		alterar(obj, "ControleLivroFolhaRecibo", 
				new AtributoPersistencia()
				.add("controleLivroRegistroDiploma", obj.getControleLivroRegistroDiploma())
				.add("matricula", obj.getMatricula().getMatricula())
				.add("folhaReciboAtual", obj.getFolhaReciboAtual())
				.add("numeroRegistro", obj.getNumeroRegistro())
				.add("dataPublicacao", obj.getDataPublicacao())
				.add("via", obj.getVia())
				.add("responsavel", obj.getResponsavel())
//				.add("dataCadastro", obj.getDataCadastro())
				.add("situacao", obj.getSituacao()),
				new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		obj.setNovoObj(Boolean.FALSE);
    }
    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ControleLivroFolhaReciboVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ControleLivroFolhaReciboVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ControleLivroFolhaReciboVO obj, UsuarioVO usuario) throws Exception {
		try {
			ControleLivroFolhaRecibo.excluir(getIdEntidade());
			getFacadeFactory().getMatriculaControleLivroRegistroDiplomaFacade().excluirPorControleLivroFolhaRecibo(obj.getCodigo());
			final String sql = "DELETE FROM ControleLivroFolhaRecibo WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlExcluir = con.prepareStatement(sql);
					sqlExcluir.setInt(1, obj.getCodigo().intValue());
					return sqlExcluir;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirListaControleLivroFolhaRecibo(Integer controleLivroRegistroDiploma, List<ControleLivroFolhaReciboVO> listaconControleLivroFolhaReciboVOs, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	if (listaconControleLivroFolhaReciboVOs.isEmpty()) {
    		return;
    	}
    	sb.append("DELETE FROM ControleLivroFolhaRecibo WHERE controleLivroRegistroDiploma = ").append(controleLivroRegistroDiploma);
    	sb.append(" AND codigo not in(");
    	boolean virgula = false;
    	for (ControleLivroFolhaReciboVO controleLivroFolhaReciboVO : listaconControleLivroFolhaReciboVOs) {
			if (!virgula) {
				sb.append(controleLivroFolhaReciboVO.getCodigo());
				virgula = true;
			} else {
				sb.append(", ").append(controleLivroFolhaReciboVO.getCodigo());
			}
		}
    	sb.append(") ");
		getConexao().getJdbcTemplate().update(sb.toString());
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarListaControleLivroFolhaRecibo(Integer controleLivroRegistroDiploma, List<ControleLivroFolhaReciboVO> listaconControleLivroFolhaReciboVOs, UsuarioVO usuarioVO) throws Exception {
    	excluirListaControleLivroFolhaRecibo(controleLivroRegistroDiploma, listaconControleLivroFolhaReciboVOs, usuarioVO);
    	executarValidacaoUnicidade(listaconControleLivroFolhaReciboVOs, this::chaveUnicidadeMatriculaVia, "Matrícula(s) Duplicada(s): ");
    	for (ControleLivroFolhaReciboVO controleLivroFolhaReciboVO : listaconControleLivroFolhaReciboVOs) {
    		if (!Uteis.isAtributoPreenchido(controleLivroFolhaReciboVO.getResponsavel())) {
    			controleLivroFolhaReciboVO.setResponsavel(usuarioVO);
			}
			if (controleLivroFolhaReciboVO.getCodigo().equals(0)) {
				incluir(controleLivroFolhaReciboVO, usuarioVO);
			} else {
				alterar(controleLivroFolhaReciboVO, usuarioVO);
			}
		}
    }
    
    /**
     * Responsável por realizar uma consulta de <code>ControleLivroFolhaRecibo</code> através do valor do atributo 
     * <code>matricula</code> da classe <code>Matricula</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ControleLivroFolhaReciboVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public ControleLivroFolhaReciboVO  consultarPorMatriculaMatriculaMaiorVia(String valorConsulta, TipoLivroRegistroDiplomaEnum tipoLivro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);        
        StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ControleLivroFolhaRecibo.* ");
		sqlStr.append("FROM ControleLivroFolhaRecibo ");
		sqlStr.append("INNER JOIN Matricula on Matricula.matricula = ControleLivroFolhaRecibo.matricula ");
		sqlStr.append("INNER JOIN ControleLivroRegistroDiploma on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
		sqlStr.append("WHERE ( Matricula.matricula ) like(?) ");
		sqlStr.append("AND ControleLivroRegistroDiploma.tipoLivroRegistroDiplomaEnum = '").append(tipoLivro).append("' ");
		sqlStr.append("AND ControleLivroFolhaRecibo.via = ( ");
		sqlStr.append("select MAX(ControleLivroFolhaRecibo.via) ");
		sqlStr.append("from ControleLivroFolhaRecibo ");
		sqlStr.append("INNER JOIN ControleLivroRegistroDiploma on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
		sqlStr.append("WHERE (ControleLivroFolhaRecibo.matricula) like(?) ");
		sqlStr.append("AND ControleLivroRegistroDiploma.tipoLivroRegistroDiplomaEnum = '").append(tipoLivro).append("')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta, valorConsulta);
		if (tabelaResultado.next()){
	        return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return null;
    }
    
    
    /**
     * Responsavel por realizar a consulta do <code>LivroRegistroDiploma</code> ao clicar em editar um livro.
     * @param valorConsulta
     * @param situacao
     * @param controleLivroRegistroDiplomaVO
     * @param usuarioVO
     * @return
     * @throws Exception
     */
    
    public List<ControleLivroFolhaReciboVO> consultarDadosControleLivroFolhaRecibo(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception{
   	 ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
     StringBuilder sql =  getSqlConsultaCompleta();
   	 sql.append(" where controlelivrofolharecibo.controlelivroregistrodiploma = ? ");
   	 
   	 sql.append(" order by controlelivrofolharecibo.folhareciboatual, controlelivrofolharecibo.numeroregistro  ");
   	 SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), controleLivroRegistroDiplomaVO.getCodigo());
   	 return montarDadosCompleto(controleLivroRegistroDiplomaVO,  tabelaResultado, usuarioVO );
    }
    
    

    /**
     * Responsável por realizar uma consulta de <code>ControleLivroFolhaRecibo</code> através do valor do atributo 
     * <code>codigo</code> da classe <code>ControleLivroRegistroDiploma</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ControleLivroFolhaReciboVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
    */
	public List<ControleLivroFolhaReciboVO> consultarPorCodigoSituacaoControleLivroRegistroDiploma(Integer valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ControleLivroFolhaRecibo.* ");
		sqlStr.append("FROM ControleLivroFolhaRecibo ");
		sqlStr.append("INNER JOIN ControleLivroRegistroDiploma on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
		sqlStr.append("WHERE ControleLivroRegistroDiploma.codigo = ").append(valorConsulta);
		if (!situacao.trim().isEmpty()) {
			sqlStr.append(" AND ControleLivroRegistroDiploma.situacaoFechadoAberto = '").append(situacao).append("' ");
		}
		sqlStr.append(" ORDER BY ControleLivroFolhaRecibo.folhareciboatual");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
    
	public List<ControleLivroFolhaReciboVO> consultarPorMatriculaCursoTurmaSituacaoControleLivroRegistroDiploma(String matricula, Integer curso, Integer turma, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer nrLivro) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select ControleLivroFolhaRecibo.*, ControleLivroRegistroDiploma.* from ControleLivroRegistroDiploma ");
		sqlStr.append(" inner join ControleLivroFolhaRecibo on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.controleLivroRegistroDiploma ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = ControleLivroFolhaRecibo.matricula ");
		sqlStr.append(" where 1=1 ");
		if (!curso.equals(0)) {
			sqlStr.append(" and ControleLivroRegistroDiploma.curso = ").append(curso);
		}
		if (!matricula.equals("")) {
			sqlStr.append(" and ControleLivroFolhaRecibo.matricula = '").append(matricula).append("' ");
		}
		if (!turma.equals(0)) {
			sqlStr.append(" and matriculaperiodo.turma = ").append(turma);
		}
		if (!turma.equals(0)) {
			sqlStr.append(" and ControleLivroRegistroDiploma.nrLivro = ").append(nrLivro);
		}
		if (!situacao.trim().isEmpty()) {
			sqlStr.append(" AND ControleLivroRegistroDiploma.situacaoFechadoAberto = '").append(situacao).append("' ");
		}
		sqlStr.append(" order by ControleLivroFolhaRecibo.folhareciboatual ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

    /**
     * Responsável por realizar uma consulta de <code>ControleLivroFolhaRecibo</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ControleLivroFolhaReciboVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ControleLivroFolhaReciboVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleLivroFolhaRecibo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ControleLivroFolhaReciboVO</code> resultantes da consulta.
    */
    public static List<ControleLivroFolhaReciboVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<ControleLivroFolhaReciboVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ControleLivroFolhaReciboVO</code>.
     * @return  O objeto da classe <code>ControleLivroFolhaReciboVO</code> com os dados devidamente montados.
    */
    public static ControleLivroFolhaReciboVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleLivroFolhaReciboVO obj = new ControleLivroFolhaReciboVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getControleLivroRegistroDiploma().setCodigo(dadosSQL.getInt("controleLivroRegistroDiploma"));
        obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
        obj.setFolhaReciboAtual(dadosSQL.getInt("folhaReciboAtual"));
        obj.setNumeroRegistro(dadosSQL.getInt("numeroRegistro"));
        obj.setDataPublicacao(dadosSQL.getDate("dataPublicacao"));
        obj.setVia(dadosSQL.getString("via"));
        obj.setNovoObj(Boolean.FALSE);
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
        obj.setDataCadastro(Uteis.getDataJDBCTimestamp(dadosSQL.getTimestamp("dataCadastro")));
        obj.setSituacao(dadosSQL.getString("situacao"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
        	montarDadosControleLivroRegistroDiploma(obj, nivelMontarDados, usuario);
            return obj;
        }
        if(Uteis.isAtributoPreenchido(obj.getResponsavel().getCodigo())){
        	obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            montarDadosMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }
        if(Uteis.isAtributoPreenchido(obj.getResponsavel().getCodigo())){
        	obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
        }
        montarDadosControleLivroRegistroDiploma(obj, nivelMontarDados, usuario);
        montarDadosMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        /* deixa a consulta lenta*/ ///montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX,usuario);
        /*deixa a consulta lenta*/ //  montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_TODOS,usuario);
        return obj;
    }
    
	public ControleLivroFolhaReciboVO montarDadosListaLivroFolhaReciboMatricula (MatriculaVO matricula, ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, int folhaRecibo , Boolean filtroPorProgramacaoFormatura, UsuarioVO usuario) throws Exception {

		ControleLivroFolhaReciboVO controleLivroFolhaReciboVO = new ControleLivroFolhaReciboVO();
        controleLivroFolhaReciboVO.setMatricula(matricula);
        controleLivroRegistroDiplomaVO.setNumeroRegistro(controleLivroRegistroDiplomaVO.getNumeroRegistro()+1);
        controleLivroFolhaReciboVO.setControleLivroRegistroDiploma(controleLivroRegistroDiplomaVO);
        controleLivroFolhaReciboVO.setNumeroRegistro(controleLivroRegistroDiplomaVO.getNumeroRegistro());
        controleLivroFolhaReciboVO.setFolhaReciboAtual(folhaRecibo);
        controleLivroFolhaReciboVO.setDataPublicacao(controleLivroRegistroDiplomaVO.getDataPublicacao());
        Integer via = this.obterViaMatriculaJaAdicionadaLivro(matricula.getMatricula(), controleLivroRegistroDiplomaVO.getTipoLivroRegistroDiplomaEnum()) + 1;
        controleLivroFolhaReciboVO.setVia(via.toString());        		
        controleLivroFolhaReciboVO.setResponsavel(usuario);
        controleLivroFolhaReciboVO.setDataCadastro(new Date());
        if (!filtroPorProgramacaoFormatura) {
        	controleLivroFolhaReciboVO.setSituacao(verificaSituacaoDiploma(matricula.getMatricula(), usuario));
        } else {
        	controleLivroFolhaReciboVO.setSituacao(matricula.getExisteDiploma() ? "Emitido" : "Pendente");
        }

		return controleLivroFolhaReciboVO;
	}


    /**
     * Operação responsável por montar os dados de um objeto da classe <code>MatriculaVO</code> relacionado ao objeto <code>ControleLivroFolhaReciboVO</code>.
     * Faz uso da chave primária da classe <code>MatriculaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosMatricula(ControleLivroFolhaReciboVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
            obj.setMatricula(new MatriculaVO());
            return;
        }
        obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), obj.getMatricula().getUnidadeEnsino().getCodigo(), NivelMontarDados.getEnum(nivelMontarDados),usuario));
      //  getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(obj.getMatricula().getUnidadeEnsino(), NivelMontarDados.BASICO, usuario);
        getFacadeFactory().getPessoaFacade().carregarDados(obj.getMatricula().getAluno(), NivelMontarDados.BASICO, usuario);
        // aprofundando consulta pois tambem precisa-se do estado
        CidadeVO naturalidadeVO = getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getMatricula().getAluno().getNaturalidade().getCodigo(), false, usuario);
        obj.getMatricula().getAluno().setNaturalidade(naturalidadeVO);
    }

    public static void montarDadosUnidadeEnsino(ControleLivroFolhaReciboVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if ((obj.getMatricula().getUnidadeEnsino() == null) || (obj.getMatricula().getUnidadeEnsino().getCodigo().equals(0))) {
            obj.setMatricula(new MatriculaVO());
            return;
        }
        obj.getMatricula().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getMatricula().getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ControleLivroRegistroDiplomaVO</code> relacionado ao objeto <code>ControleLivroFolhaReciboVO</code>.
     * Faz uso da chave primária da classe <code>ControleLivroRegistroDiplomaVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
    */
    public static void montarDadosControleLivroRegistroDiploma(ControleLivroFolhaReciboVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getControleLivroRegistroDiploma().getCodigo().intValue() == 0) {
            obj.setControleLivroRegistroDiploma(new ControleLivroRegistroDiplomaVO());
            return;
        }
        obj.setControleLivroRegistroDiploma(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorChavePrimaria(obj.getControleLivroRegistroDiploma().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ControleLivroFolhaReciboVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public ControleLivroFolhaReciboVO consultarPorChavePrimaria( Integer codigoPrm, int nivelMontarDados , UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ControleLivroFolhaRecibo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ControleLivroFolhaRecibo ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public Integer obterViaMatriculaJaAdicionadaLivro(String matricula, TipoLivroRegistroDiplomaEnum tipoLivroRegistroDiplomaEnum) throws Exception {    	//
    	StringBuilder sql  = new StringBuilder(" select count(distinct controlelivrofolharecibo.codigo) as qtd from controlelivrofolharecibo ");
    	sql.append(" inner join controlelivroregistrodiploma on controlelivroregistrodiploma.codigo = controlelivrofolharecibo.controlelivroregistrodiploma ");
    	sql.append(" where matricula = ? and controlelivroregistrodiploma.tipolivroregistrodiplomaenum = ? ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{matricula, tipoLivroRegistroDiplomaEnum.name()});
    	if (tabelaResultado.next()) {
    		return tabelaResultado.getInt("qtd");
    	}
    	return 0;
    }


    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return ControleLivroFolhaRecibo.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        ControleLivroFolhaRecibo.idEntidade = idEntidade;
    }
    
    @Override
    public String adicionarListaLivroFolhaReciboMatricula(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, 
    		UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO,
    		List<MatriculaVO> matriculas, List<ControleLivroFolhaReciboVO> controleLivroFolhaReciboVOs, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, Boolean filtroPorProgramacaoFormatura, UsuarioVO usuarioVO) throws Exception {
//    	controleLivroRegistroDiplomaVO.setUnidadeEnsino(unidadeEnsinoVO);
    	controleLivroRegistroDiplomaVO.setCurso(cursoVO);
		
    	int folhaRecibo = controleLivroRegistroDiplomaVO.getNrFolhaRecibo();
    	int quantidadeRegistroPorFolha = 1;
		Iterator<MatriculaVO> i = matriculas.iterator();
		ConsistirException consistirException =  new ConsistirException();
		while (i.hasNext()) {
			MatriculaVO obj = (MatriculaVO) i.next();			
			//ControleLivroFolhaReciboVO verificaRepetidosNaLista = controleLivroFolhaReciboVOs.stream().filter(c -> c.getMatricula().equals(obj)).findFirst().orElse(null);
			/* List<ControleLivroFolhaReciboVO> listaMatriculaExistente =  controleLivroFolhaReciboVOs
					 .stream().filter(c -> c.getMatricula().equals(obj))
					 .sorted(OrdenarControleLivroFolhaReciboEnum.DATA_CADASTRO_LONG.desc())
					 .collect(Collectors.toList());
			if(listaMatriculaExistente == null || listaMatriculaExistente.isEmpty() || listaMatriculaExistente.get(0).getSituacao().equals("Cancelado")) {
			*/	ControleLivroFolhaReciboVO verificaRepetidosNaLista = new ControleLivroFolhaReciboVO();
				verificaRepetidosNaLista.setMatricula(obj);
				if(realizarValidacaoeAlunoPodeSerAdicionadoLivro(verificaRepetidosNaLista, usuarioVO, programacaoFormaturaAlunoVO, consistirException, filtroPorProgramacaoFormatura)) {
					if ( controleLivroFolhaReciboVOs.size() < controleLivroRegistroDiplomaVO.getNrMaximoFolhasLivro()) {
						ControleLivroFolhaReciboVO clfrVO = getFacadeFactory().getControleLivroFolhaReciboFacade().montarDadosListaLivroFolhaReciboMatricula(obj, controleLivroRegistroDiplomaVO, folhaRecibo, filtroPorProgramacaoFormatura, usuarioVO);
						clfrVO.setAvisoDebitoDocumentos(verificaRepetidosNaLista.getAvisoDebitoDocumentos());
						controleLivroFolhaReciboVOs.add(clfrVO);
						if (quantidadeRegistroPorFolha == controleLivroRegistroDiplomaVO.getQuantidadeRegistroPorFolha()) {
							quantidadeRegistroPorFolha = 1;
							folhaRecibo++;
						} else {
							quantidadeRegistroPorFolha++;
						}

					} else {
						consistirException.adicionarListaMensagemErro("Aluno "+obj.getAluno().getNome()+" não pode ser adicionado ao livro, pois o livro já excedeu a quantidade de registros definidos.");
					}

				}
			/*} else {
				consistirException.adicionarListaMensagemErro("Aluno "+obj.getAluno().getNome()+" não pode ser adicionado, pois já existe no livro.");
			}*/
		}

		controleLivroRegistroDiplomaVO.setNrFolhaRecibo(folhaRecibo);
		return consistirException.getToStringMensagemErro();
	}

	public boolean realizarValidacaoeAlunoPodeSerAdicionadoLivro(ControleLivroFolhaReciboVO controleLivroFolhaReciboVO, UsuarioVO usuarioVO, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, ConsistirException consistirException, Boolean filtroPorProgramacaoFormatura) throws Exception {
		if ((filtroPorProgramacaoFormatura && !controleLivroFolhaReciboVO.getMatricula().getMatriculaIntegralizada()) || (!filtroPorProgramacaoFormatura && !getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(controleLivroFolhaReciboVO.getMatricula(),controleLivroFolhaReciboVO.getMatricula().getGradeCurricularAtual().getCodigo(), usuarioVO, programacaoFormaturaAlunoVO))) {
			consistirException.adicionarListaMensagemErro("Aluno " + controleLivroFolhaReciboVO.getMatricula().getAluno().getNome()+ " não pode ser adicionado ao livro, pois não cumpriu as regras de integralização curricular.");
			return false;
		}
//		String temp = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarExistenciaPendenciaDocumentacaoPorMatricula(controleLivroFolhaReciboVO.getMatricula().getAluno().getCodigo(),controleLivroFolhaReciboVO.getMatricula().getMatricula(), new ArrayList<TipoDocumentoVO>(), false);
//		if (temp != null) {
//			consistirException.adicionarListaMensagemErro("Aluno " + controleLivroFolhaReciboVO.getMatricula().getAluno().getNome() + " não pode ser adicionado ao livro, pois existe pendência na documentação.");
//			return false;
//		}
//		String temporal = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarExistenciaPendenciaDocumentacaoPorMatriculaPorGerarSuspensao(controleLivroFolhaReciboVO.getMatricula().getMatricula(), false);
//		if (temporal != null) {
//			controleLivroFolhaReciboVO.setAvisoDebitoDocumentos(("Atenção! Aluno " + controleLivroFolhaReciboVO.getMatricula().getAluno().getNome() + " está em debito de documento(s):" + temporal + ", que não geram suspensão."));
//			return true;
//		}
		return true;
	}

	public String verificaSituacaoDiploma(String matricula, UsuarioVO usuarioVO) {
	
		List<ExpedicaoDiplomaVO> situacaoDiploma = new ArrayList<ExpedicaoDiplomaVO>();
		String situacao="";
		ConsistirException consistirException =  new ConsistirException();
		try {
			situacaoDiploma = getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatriculaAluno(matricula, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO );
			if(!situacaoDiploma.isEmpty()) {
				situacao = "Emitido";
			}else {
				situacao = "Pendente";
			}
		} catch (Exception e) {
			consistirException.adicionarListaMensagemErro("Não pode verificar a situação da mátricula.");
		}
		 return situacao;
		
 }
 
 
 private StringBuilder getSqlConsultaCompleta() {
	 StringBuilder sql =  new StringBuilder("");
	 sql.append(" 	select controlelivrofolharecibo.*,  ");
	 sql.append(" 	aluno.codigo as aluno_codigo,");
	 sql.append(" 	aluno.nome as aluno_nome,");
	 sql.append(" 	aluno.email as aluno_email,");
	 sql.append(" 	aluno.rg as aluno_rg,");
	 sql.append(" 	aluno.sexo as aluno_sexo,");
	 sql.append(" 	aluno.orgaoemissor as aluno_orgaoemissor,");
	 sql.append(" 	aluno.estadoemissaorg as aluno_estadoemissaorg,");
	 sql.append(" 	aluno.cpf as aluno_cpf,");
	 sql.append(" 	aluno.datanasc as aluno_data_nascimento,");
	 sql.append(" 	mae.codigo as mae_codigo, ");
	 sql.append(" 	mae.nome as mae_nome,");
	 sql.append(" 	pai.codigo as pai_codigo, ");
	 sql.append(" 	pai.nome as pai_nome,");
	 sql.append(" 	curso.codigo as curso_codigo,");
	 sql.append(" 	curso.nome as curso_nome,");
	 sql.append(" 	curso.nomeDocumentacao as curso_nomeDocumentacao,");
	 sql.append(" 	curso.periodicidade as curso_periodicidade,");
	 sql.append(" 	curso.titulo as curso_titulo,");
	 sql.append(" 	curso.habilitacao as curso_habilitacao,");
	 sql.append(" 	curso.idCursoInep as curso_idCursoInep,");
	 sql.append(" 	curso.titulacaoDoFormandoFeminino as curso_titulacaoDoFormandoFeminino,");
	 sql.append(" 	curso.titulacaoDoFormando as curso_titulacaoDoFormando,");
	 sql.append(" 	curso.nrRegistroInterno as curso_nrRegistroInterno,");
	 sql.append(" 	unidadeensino.codigo as unidadeensino_codigo,");
	 sql.append(" 	unidadeensino.mantenedora as unidadeensino_mantenedora,");
	 sql.append(" 	unidadeensino.nome as unidadeensino_nome,");
	 sql.append(" 	unidadeensino.abreviatura as unidadeensino_abreviatura,");	 
	 sql.append(" 	unidadeensino.nomeexpedicaodiploma as unidadeensino_nomeexpedicaodiploma,");
	 sql.append(" 	unidadeensino.cnpj as unidadeensino_cnpj,");
	 sql.append(" 	unidadeensino.codigoies as unidadeensino_codigoies,");
	 sql.append(" 	cidade_unidade_ensino.codigo as cidade_unidade_ensino_codigo,");
	 sql.append(" 	cidade_unidade_ensino.nome as cidade_unidade_ensino_nome,");
	 sql.append(" 	estado_unidade_ensino.codigo as estado_unidade_ensino_codigo,");
	 sql.append(" 	estado_unidade_ensino.nome as estado_unidade_ensino_nome,");
	 sql.append(" 	estado_unidade_ensino.sigla as estado_unidade_ensino_sigla,");
	 sql.append(" 	turno.codigo as turno_codigo,");
	 sql.append(" 	turno.nome as turno_nome, ");
	 sql.append(" 	responsavel.nome as responsavel_nome, ");
	 sql.append(" 	turma.codigo as turma_codigo,");
	 sql.append(" 	turma.identificadorturma as turma_identificadorturma, ");
	 sql.append(" 	cidade.codigo as cidade_naturalidade_codigo,");
	 sql.append(" 	cidade.nome as cidade_naturalidade_nome,");
	 sql.append(" 	estado.codigo as estado_naturalidade_codigo,");
	 sql.append(" 	estado.sigla as estado_naturalidade_sigla, ");
	 sql.append(" 	estado.nome as estado_naturalidade_nome, ");
	 sql.append(" 	paiz.codigo as pais_nacionalidade_codigo,");
	 sql.append(" 	paiz.nome as pais_nacionalidade_nome,");
	 sql.append(" 	paiz.nacionalidade as pais_nacionalidade_nacionalidade,");
	 sql.append(" 	matricula.data as matricula_data,");
	 sql.append(" 	matricula.anoconclusao as matricula_anoconclusao,");	 
	 sql.append(" 	matricula.semestreconclusao as matricula_semestreconclusao,");
	 sql.append(" 	matricula.dataconclusaocurso as matricula_dataconclusaocurso, ");
	 sql.append(" 	matricula.datacolacaograu as matricula_datacolacaograu,");
	 sql.append(" 	expedicaodiploma.dataexpedicao as data_expedicao_diploma, ");
	 sql.append(" 	expedicaodiploma.codigo as expedicao_diploma_codigo, ");
	 sql.append(" 	expedicaodiploma.numeroprocesso as expedicaodiploma_numeroprocesso,");
	 sql.append(" 	matricula.gradecurricularatual as matricula_gradecurricularatual,");
	 sql.append("   pessoaFuncionarioPrimario.nome as funcionarioPrimario,");
	 sql.append("   pessoaFuncionarioSecundario.nome as FuncionarioSecundario,");
	 sql.append("   pessoaFuncionarioTerceiro.nome as FuncionarioTerceiro,");
	 sql.append("   expedicaodiploma.tituloFuncionarioPrincipal as tituloFuncionarioPrincipal,");
	 sql.append("   expedicaodiploma.tituloFuncionarioSecundario as tituloFuncionarioSecundario,");
	 sql.append("   expedicaodiploma.tituloFuncionarioTerceiro as tituloFuncionarioTerceiro,");
	 sql.append("   expedicaodiploma.serial as serial,");
	 sql.append("   expedicaodiploma.observacao as expedicao_observacao,");
	 sql.append("   expedicaodiploma.numeroRegistroDiplomaViaAnterior as numeroRegistroDiplomaViaAnterior");
	 sql.append("   ");
	 sql.append(" 	from controlelivrofolharecibo ");
	 sql.append(" 	inner join matricula on controlelivrofolharecibo.matricula = matricula.matricula");
	 sql.append(" 	inner join curso on curso.codigo = matricula.curso");
	 sql.append(" 	inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino");
	 sql.append(" 	inner join turno on turno.codigo = matricula.turno");
	 sql.append(" 	inner join pessoa as aluno on aluno.codigo = matricula.aluno");
	 sql.append("   inner join controlelivroregistrodiploma on controlelivroregistrodiploma.codigo = controlelivrofolharecibo.controlelivroregistrodiploma");
	 sql.append(" 	inner join matriculaperiodo  on matriculaperiodo.matricula = matricula.matricula");
	 sql.append(" 	and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' order by mp.ano desc, mp.semestre desc, mp.codigo desc limit 1)");
	 sql.append(" 	left join turma on turma.codigo = matriculaperiodo.turma");
	 sql.append(" 	left join filiacao as filiacao_mae on filiacao_mae.aluno  = aluno.codigo and filiacao_mae.tipo = 'MA'");
	 sql.append(" 	and filiacao_mae.codigo = (select filiacao.codigo from filiacao where filiacao.aluno  = aluno.codigo and filiacao.tipo = 'MA' order by filiacao.codigo limit 1)");
	 sql.append(" 	left join pessoa as mae on mae.codigo = filiacao_mae.pais");
	 sql.append(" 	left join filiacao as filiacao_pai on filiacao_pai.aluno  = aluno.codigo and filiacao_pai.tipo = 'PA'");
	 sql.append(" 	and filiacao_pai.codigo = (select filiacao.codigo from filiacao where filiacao.aluno  = aluno.codigo and filiacao.tipo = 'PA' order by filiacao.codigo limit 1)");
	 sql.append(" 	left join pessoa as pai on pai.codigo = filiacao_pai.pais");
	 sql.append(" 	left join usuario as responsavel on responsavel.codigo = controlelivrofolharecibo.responsavel");
	 sql.append(" 	left join cidade on cidade.codigo = aluno.naturalidade");
	 sql.append(" 	left join estado on cidade.estado = estado.codigo");
	 sql.append(" 	left join paiz on paiz.codigo = aluno.nacionalidade");
	 sql.append(" 	left join cidade as cidade_unidade_ensino on cidade_unidade_ensino.codigo = unidadeensino.cidade");
	 sql.append(" 	left join estado as estado_unidade_ensino on cidade_unidade_ensino.estado = estado_unidade_ensino.codigo");
	 sql.append(" 	left join expedicaodiploma on expedicaodiploma.matricula = matricula.matricula");
	 sql.append(" 	and expedicaodiploma.codigo = (");
	 sql.append(" 	 select ed.codigo from expedicaodiploma ed where ed.matricula = matricula.matricula  and ed.via = controlelivrofolharecibo.via order by ed.codigo limit 1");
	 sql.append(" 	)");
	 sql.append("   left join funcionario as FuncionarioPrimario on	FuncionarioPrimario.codigo = expedicaodiploma.funcionarioPrimario");
	 sql.append("   left join pessoa as pessoaFuncionarioPrimario on	pessoaFuncionarioPrimario.codigo = funcionarioPrimario.pessoa");
	 sql.append("   left join funcionario as FuncionarioSecundario on	FuncionarioSecundario.codigo = expedicaodiploma.FuncionarioSecundario");
	 sql.append("   left join pessoa as pessoaFuncionarioSecundario on	pessoaFuncionarioSecundario.codigo = FuncionarioSecundario.pessoa");
	 sql.append("   left join funcionario as FuncionarioTerceiro on	FuncionarioTerceiro.codigo = expedicaodiploma.FuncionarioTerceiro");
	 sql.append("   left join pessoa as pessoaFuncionarioTerceiro on	pessoaFuncionarioTerceiro.codigo = FuncionarioTerceiro.pessoa");
	 return sql;
 }
 
 public List<ControleLivroFolhaReciboVO> consultarDadosControleLivroFolhaRecibo(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, UsuarioVO usuarioVO) throws Exception{
	 StringBuilder sql =  getSqlConsultaCompleta();
	 sql.append(" where controlelivrofolharecibo.controlelivroregistrodiploma = ? order by controlelivrofolharecibo.folhareciboatual, controlelivrofolharecibo.numeroregistro  ");
	 return montarDadosCompleto(controleLivroRegistroDiplomaVO, getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), controleLivroRegistroDiplomaVO.getCodigo()), usuarioVO);
 } 
 
 private List<ControleLivroFolhaReciboVO> montarDadosCompleto(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, SqlRowSet rs, UsuarioVO usuario) throws Exception{
	 List<ControleLivroFolhaReciboVO> controleLivroFolhaReciboVOs  = new ArrayList<ControleLivroFolhaReciboVO>();	 
     while (rs.next()) {
    	 controleLivroFolhaReciboVOs.add(montarDadosCompleto(rs,  controleLivroRegistroDiplomaVO, usuario));
     }
     return controleLivroFolhaReciboVOs;
 }
 
 public static ControleLivroFolhaReciboVO montarDadosCompleto(SqlRowSet dadosSQL, ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO,  UsuarioVO usuario) throws Exception {
	 ControleLivroFolhaReciboVO obj = new ControleLivroFolhaReciboVO();
     obj.setCodigo(dadosSQL.getInt("codigo"));
     obj.setControleLivroRegistroDiploma(controleLivroRegistroDiplomaVO);
     obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
     obj.setFolhaReciboAtual(dadosSQL.getInt("folhaReciboatual"));
     obj.setNumeroRegistro(dadosSQL.getInt("numeroRegistro"));
     obj.setDataPublicacao(dadosSQL.getDate("dataPublicacao"));
     obj.setVia(dadosSQL.getString("via"));
     obj.setNovoObj(Boolean.FALSE);
     obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
     obj.setDataCadastro(Uteis.getDataJDBCTimestamp(dadosSQL.getTimestamp("dataCadastro")));
     obj.setSituacao(dadosSQL.getString("situacao"));
     if((obj.getSituacao().equals("Pendente") || !Uteis.isAtributoPreenchido(obj.getSituacao())) && Uteis.isAtributoPreenchido(dadosSQL.getInt("expedicao_diploma_codigo"))) {
    	 obj.setSituacao("Emitido");
     }else if(!Uteis.isAtributoPreenchido(obj.getSituacao())) {
    	 obj.setSituacao("Pendente");
     }
     obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("aluno_codigo"));
     obj.getMatricula().getAluno().setNome(dadosSQL.getString("aluno_nome"));
     obj.getMatricula().getAluno().setEmail(dadosSQL.getString("aluno_email"));
     obj.getMatricula().getAluno().setRG(dadosSQL.getString("aluno_rg"));
     obj.getMatricula().getAluno().setOrgaoEmissor(dadosSQL.getString("aluno_orgaoemissor"));
     obj.getMatricula().getAluno().setEstadoEmissaoRG(dadosSQL.getString("aluno_estadoemissaorg"));
     obj.getMatricula().getAluno().setSexo(dadosSQL.getString("aluno_sexo"));
     obj.getMatricula().getAluno().setCPF(dadosSQL.getString("aluno_cpf"));
     obj.getMatricula().getAluno().setDataNasc(dadosSQL.getDate("aluno_data_nascimento"));
    
     if(Uteis.isAtributoPreenchido(dadosSQL.getInt("mae_codigo"))) {
    	 FiliacaoVO filiacaoMae = new FiliacaoVO();
    	 filiacaoMae.setCodigo(dadosSQL.getInt("mae_codigo"));
    	 filiacaoMae.setNome(dadosSQL.getString("mae_nome"));
    	 filiacaoMae.setTipo("MA");
    obj.getMatricula().getAluno().getFiliacaoVOs().add(filiacaoMae); 
     } 
     if(Uteis.isAtributoPreenchido(dadosSQL.getInt("pai_codigo"))) {
    	 FiliacaoVO filiacaoPai = new FiliacaoVO();
    	 filiacaoPai.setCodigo(dadosSQL.getInt("pai_codigo"));
    	 filiacaoPai.setNome(dadosSQL.getString("pai_nome"));
    	 filiacaoPai.setTipo("PA");
    obj.getMatricula().getAluno().getFiliacaoVOs().add(filiacaoPai); 
     }
     obj.getMatricula().getCurso().setCodigo(dadosSQL.getInt("curso_codigo"));
     obj.getMatricula().getCurso().setNome(dadosSQL.getString("curso_nome"));
     obj.getMatricula().getCurso().setNomeDocumentacao(dadosSQL.getString("curso_nomeDocumentacao"));
     obj.getMatricula().getCurso().setNrRegistroInterno(dadosSQL.getString("curso_nrRegistroInterno"));
     if(!Uteis.isAtributoPreenchido(obj.getMatricula().getCurso().getNomeDocumentacao())) {
    	 obj.getMatricula().getCurso().setNomeDocumentacao(obj.getMatricula().getCurso().getNome());
     }
     obj.getMatricula().getCurso().setTitulo(dadosSQL.getString("curso_titulo"));
     obj.getMatricula().getCurso().setHabilitacao(dadosSQL.getString("curso_habilitacao"));
     obj.getMatricula().getCurso().setPeriodicidade(dadosSQL.getString("curso_periodicidade"));
     obj.getMatricula().getCurso().setIdCursoInep(dadosSQL.getInt("curso_idCursoInep"));
     obj.getMatricula().getCurso().setTitulacaoDoFormandoFeminino(dadosSQL.getString("curso_titulacaoDoFormandoFeminino"));
     obj.getMatricula().getCurso().setTitulacaoDoFormando(dadosSQL.getString("curso_titulacaoDoFormando"));
     obj.getMatricula().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino_codigo"));
     obj.getMatricula().getUnidadeEnsino().setMantenedora(dadosSQL.getString("unidadeensino_mantenedora"));
     obj.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino_nome"));
     obj.getMatricula().getUnidadeEnsino().setNomeExpedicaoDiploma(dadosSQL.getString("unidadeensino_nomeexpedicaodiploma"));
     obj.getMatricula().getUnidadeEnsino().setAbreviatura(dadosSQL.getString("unidadeensino_abreviatura"));
     obj.getMatricula().getUnidadeEnsino().setCNPJ(dadosSQL.getString("unidadeensino_cnpj"));
     obj.getMatricula().getUnidadeEnsino().setCodigoIES(dadosSQL.getInt("unidadeensino_codigoies"));
     obj.getMatricula().getUnidadeEnsino().getCidade().setCodigo(dadosSQL.getInt("cidade_unidade_ensino_codigo"));
     obj.getMatricula().getUnidadeEnsino().getCidade().setNome(dadosSQL.getString("cidade_unidade_ensino_nome"));
     obj.getMatricula().getUnidadeEnsino().getCidade().getEstado().setCodigo(dadosSQL.getInt("estado_unidade_ensino_codigo"));
     obj.getMatricula().getUnidadeEnsino().getCidade().getEstado().setNome(dadosSQL.getString("estado_unidade_ensino_nome"));
     obj.getMatricula().getUnidadeEnsino().getCidade().getEstado().setSigla(dadosSQL.getString("estado_unidade_ensino_sigla"));
     obj.getMatricula().getTurno().setCodigo(dadosSQL.getInt("turno_codigo"));
     obj.getMatricula().getTurno().setNome(dadosSQL.getString("turno_nome"));
     obj.getResponsavel().setNome(dadosSQL.getString("responsavel_nome"));
     obj.getMatricula().setTurma(dadosSQL.getString("turma_identificadorturma"));
     obj.getMatricula().getAluno().getNaturalidade().setCodigo(dadosSQL.getInt("cidade_naturalidade_codigo"));
     obj.getMatricula().getAluno().getNaturalidade().setNome(dadosSQL.getString("cidade_naturalidade_nome"));
     obj.getMatricula().getAluno().getNaturalidade().getEstado().setCodigo(dadosSQL.getInt("estado_naturalidade_codigo"));
     obj.getMatricula().getAluno().getNaturalidade().getEstado().setSigla(dadosSQL.getString("estado_naturalidade_sigla"));
     obj.getMatricula().getAluno().getNaturalidade().getEstado().setNome(dadosSQL.getString("estado_naturalidade_nome"));
     obj.getMatricula().getAluno().getNacionalidade().setCodigo(dadosSQL.getInt("pais_nacionalidade_codigo"));
     obj.getMatricula().getAluno().getNacionalidade().setNacionalidade(dadosSQL.getString("pais_nacionalidade_nacionalidade"));
     obj.getMatricula().getAluno().getNacionalidade().setNome(dadosSQL.getString("pais_nacionalidade_nome"));
     obj.getMatricula().setAnoConclusao(dadosSQL.getString("matricula_anoconclusao"));
     obj.getMatricula().setSemestreConclusao(dadosSQL.getString("matricula_semestreconclusao"));
     obj.getMatricula().setData(dadosSQL.getDate("matricula_data"));
     obj.getMatricula().setDataConclusaoCurso(dadosSQL.getDate("matricula_dataconclusaocurso"));
     obj.getMatricula().setDataColacaoGrau(dadosSQL.getDate("matricula_datacolacaograu"));
     obj.getMatricula().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("matricula_gradecurricularatual"));
     obj.getExpedicaoDiplomaVO().setCodigo(dadosSQL.getInt("expedicao_diploma_codigo"));
     obj.getExpedicaoDiplomaVO().setDataExpedicao(dadosSQL.getDate("data_expedicao_diploma"));
     obj.getExpedicaoDiplomaVO().setNumeroProcesso(dadosSQL.getString("expedicaodiploma_numeroprocesso"));
     obj.getExpedicaoDiplomaVO().setSerial(dadosSQL.getString("serial"));
     obj.getExpedicaoDiplomaVO().setObservacao(dadosSQL.getString("expedicao_observacao"));
     obj.getExpedicaoDiplomaVO().getFuncionarioPrimarioVO().getPessoa().setNome(dadosSQL.getString("funcionarioPrimario"));
     obj.getExpedicaoDiplomaVO().getFuncionarioSecundarioVO().getPessoa().setNome(dadosSQL.getString("funcionarioSecundario"));
     obj.getExpedicaoDiplomaVO().getFuncionarioTerceiroVO().getPessoa().setNome(dadosSQL.getString("funcionarioTerceiro"));
     obj.getExpedicaoDiplomaVO().setTituloFuncionarioPrincipal(dadosSQL.getString("tituloFuncionarioPrincipal"));
     obj.getExpedicaoDiplomaVO().setTituloFuncionarioSecundario(dadosSQL.getString("tituloFuncionarioSecundario"));
     obj.getExpedicaoDiplomaVO().setTituloFuncionarioTerceiro(dadosSQL.getString("tituloFuncionarioTerceiro"));
     obj.getExpedicaoDiplomaVO().setNumeroRegistroDiplomaViaAnterior(dadosSQL.getString("numeroRegistroDiplomaViaAnterior"));
     obj.setNovoObj(false);
     return obj;
 }

	@Override
	public List<ControleLivroFolhaReciboVO> consultarLivroFolhaReciboDiploma(String campoConsulta, String valorConsulta, UsuarioVO usuarioVO) throws Exception {
		 StringBuilder sql =  new StringBuilder("");
		 sql.append(" select ");
		 sql.append(" controlelivrofolharecibo.codigo, matricula.matricula, controlelivrofolharecibo.folhaReciboatual, controlelivrofolharecibo.dataPublicacao, ");
		 sql.append(" aluno.codigo as aluno_codigo,");
		 sql.append(" aluno.nome as aluno_nome,");
		 sql.append(" aluno.rg as aluno_rg,");
		 sql.append(" aluno.orgaoemissor as aluno_orgaoemissor,");
		 sql.append(" aluno.estadoemissaorg as aluno_estadoemissaorg,");
		 sql.append(" aluno.cpf as aluno_cpf,");
		 sql.append(" aluno.datanasc as aluno_data_nascimento,");
		 sql.append(" curso.codigo as curso_codigo,");
		 sql.append(" curso.nome as curso_nome,");
		 sql.append(" curso.idCursoInep as curso_idCursoInep,");
		 sql.append(" unidadeensino.codigo as unidadeensino_codigo,");
		 sql.append(" unidadeensino.nome as unidadeensino_nome,");
		 sql.append(" unidadeensino.credenciamento as unidadeensino_credenciamento,");
		 sql.append(" unidadeensino.codigoIES as unidadeensino_codigoIES,");
		 sql.append(" unidadeensino.razaoSocial as unidadeensino_razaoSocial,");		 
		 sql.append(" unidadeensino.codigoIESMantenedora as unidadeensino_codigoIESMantenedora,");
		 sql.append(" unidadeensino.mantenedora as unidadeensino_mantenedora,");
		 sql.append(" unidadeensino.codigoIESUnidadeCertificadora as unidadeensino_codigoIESUnidadeCertificadora,");
		 sql.append(" unidadeensino.unidadeCertificadora as unidadeensino_unidadeCertificadora,");
		 sql.append(" matricula.data as matricula_data,");
		 sql.append(" matricula.anoconclusao as matricula_anoconclusao,");
		 sql.append(" matricula.semestreconclusao as matricula_semestreconclusao,");
		 sql.append(" matricula.dataInicioCurso as matricula_dataInicioCurso,");
		 sql.append(" matricula.dataconclusaocurso as matricula_dataconclusaocurso,");
		 sql.append(" matricula.datacolacaograu as matricula_datacolacaograu,");
		 sql.append(" CASE WHEN (expedicaodiploma.numeroregistrodiploma IS NULL OR expedicaodiploma.numeroregistrodiploma = '') THEN controlelivroregistrodiploma.numeroregistro::TEXT ELSE expedicaodiploma.numeroregistrodiploma END AS \"expedicaodiploma_numeroregistro\", ");
		 sql.append(" CASE WHEN (expedicaodiploma.via IS NULL OR expedicaodiploma.via = '') THEN controlelivrofolharecibo.via::TEXT ELSE expedicaodiploma.via END AS \"expedicaodiploma_via\", ");
		 sql.append(" CASE WHEN expedicaodiploma.informarcamposlivroregistradora IS TRUE THEN expedicaodiploma.nrlivroregistradora::TEXT ELSE controlelivroregistrodiploma.nrlivro::TEXT END AS \"expedicaodiploma_nrlivroregistradora\", ");
		 sql.append(" CASE WHEN expedicaodiploma.informarcamposlivroregistradora IS TRUE THEN expedicaodiploma.nrfolhareciboregistradora::TEXT ELSE controlelivrofolharecibo.folhareciboatual::TEXT END AS \"expedicaodiploma_nrfolharecibo\", ");
		 sql.append(" expedicaodiploma.dataexpedicao as data_expedicao_diploma,");
		 sql.append(" expedicaodiploma.dataPublicacaoDiarioOficial as data_puplicacao_diario_oficial,");
		 sql.append(" expedicaodiploma.codigo as expedicao_diploma_codigo,");
		 sql.append(" expedicaodiploma.numeroprocesso as expedicaodiploma_numeroprocesso,");
		 sql.append("arquivovisual.codigo as \"arquivovisual.codigo\" , arquivovisual.codOrigem as \"arquivovisual.codOrigem\", arquivovisual.nome as \"arquivovisual.nome\", ");
		 sql.append("arquivovisual.descricao as \"arquivovisual.descricao\", arquivovisual.pastabasearquivo as \"arquivovisual.pastabasearquivo\", ");
		 sql.append("arquivovisual.servidorarquivoonline as \"arquivovisual.servidorarquivoonline\", ");
		 sql.append("arquivo.codigo as \"arquivo.codigo\" , arquivo.codOrigem as \"arquivo.codOrigem\", arquivo.nome as \"arquivo.nome\", ");
		 sql.append("arquivo.descricao as \"arquivo.descricao\", arquivo.pastabasearquivo as \"arquivo.pastabasearquivo\", ");
		 sql.append("arquivo.servidorarquivoonline as \"arquivo.servidorarquivoonline\", ");
		 sql.append("documentoassinado.tipoorigemdocumentoassinado AS \"documentoassinado.tipoorigemdocumentoassinado\", ");
		 sql.append("controlelivroregistrodiploma.nrlivro as controlelivroregistrodiploma_numerolivro, ");
		 sql.append("expedicaodiploma.dataregistrodiploma  as \"expedicaodiploma.dataregistrodiploma\", expedicaodiploma.serial as \"serial\", expedicaodiploma.emitidoporprocessotransferenciaassistida as \"emitidoporprocessotransferenciaassistida\", expedicaodiploma.nomeiespta as \"nomeiespta\", ");
		 sql.append("expedicaodiploma.cnpjpta as \"cnpjpta\", expedicaodiploma.codigomecpta as \"codigomecpta\", ");
		 sql.append("expedicaodiploma.emitidopordecisaojudicial as \"emitidopordecisaojudicial\", ");
		 sql.append("expedicaodiploma.nomejuizdecisaojudicial as \"nomejuizdecisaojudicial\", expedicaodiploma.numeroprocessodecisaojudicial as \"numeroprocessodecisaojudicial\", expedicaodiploma.decisaojudicial as \"decisaojudicial\", ");
		 sql.append("expedicaodiploma.informacoesadicionaisdecisaojudicial as \"informacoesadicionaisdecisaojudicial\", documentoAssinado.tipoOrigemDocumentoAssinado as \"documentoAssinado.tipoOrigemDocumentoAssinado\" ");
		 sql.append("	from expedicaodiploma");
		 sql.append("	inner join matricula on expedicaodiploma.matricula = matricula.matricula");
		 sql.append("	inner join curso on curso.codigo = matricula.curso");
		 sql.append("	inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino");
		 sql.append("	inner join pessoa as aluno on aluno.codigo = matricula.aluno");
		 sql.append("   LEFT JOIN controlelivrofolharecibo ON controlelivrofolharecibo.matricula = matricula.matricula ");
		 sql.append("	LEFT join controlelivroregistrodiploma on controlelivroregistrodiploma.codigo = controlelivrofolharecibo.controlelivroregistrodiploma");
		 sql.append("	inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = (");
		 sql.append("	select mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC'");
		 sql.append("	order by mp.ano desc,mp.semestre desc,mp.codigo desc limit 1) ");
		 sql.append(" left join documentoassinado on documentoassinado.matricula = matricula.matricula AND documentoassinado.tipoorigemdocumentoassinado IN ('DIPLOMA_DIGITAL', 'EXPEDICAO_DIPLOMA') ");
		 sql.append(" left join arquivo arquivovisual on arquivovisual.codigo = documentoassinado.arquivovisual ");
		 sql.append(" left join arquivo arquivo on arquivo.codigo = documentoassinado.arquivo ");
		 sql.append(" INNER JOIN LATERAL (SELECT configuracaogeralsistema.codigo, configuracaogeralsistema.databasevalidacaodiplomadigital ");
		 sql.append(" FROM configuracoes INNER JOIN configuracaogeralsistema ON configuracaogeralsistema.configuracoes = configuracoes.codigo ");
		 sql.append(" WHERE ((unidadeensino.configuracoes IS NOT NULL AND unidadeensino.configuracoes = configuracoes.codigo ) OR (unidadeensino.configuracoes IS NULL AND configuracoes.padrao )) LIMIT 1) conf ON TRUE ");
		 sql.append("	where");
		 List<Object> parametros = new ArrayList<>();
		 if (campoConsulta.equals("CPF")) {
			 sql.append(" sem_caracteres_especiais(aluno.cpf) = ? ");
			 parametros.add(Uteis.retirarMascaraCPF(valorConsulta));
		 } else if(campoConsulta.equals("nomeAluno")) {
			 sql.append(" aluno.nome ilike ? ");
			 parametros.add(PERCENT + valorConsulta + PERCENT);
		 } else if(campoConsulta.equals("numeroProcesso")) {
			 sql.append(" expedicaodiploma.numeroprocesso = ? ");
			 parametros.add(valorConsulta);
		 } else if(campoConsulta.equals("codigoValidacao")) {
			 sql.append(" expedicaodiploma.codigoValidacaoDiplomaDigital = ? ");
			 parametros.add(valorConsulta);
		 } else if(campoConsulta.equals("registroAcademico")) {
			 sql.append(" aluno.registroAcademico ilike ? ");
			 parametros.add(valorConsulta + PERCENT);
		 }
		 sql.append(" AND CASE WHEN conf.databasevalidacaodiplomadigital IS NOT NULL THEN ((expedicaodiploma.dataexpedicao::DATE <= conf.databasevalidacaodiplomadigital) ");
		 sql.append(" OR (documentoassinado.codigo IS NOT NULL AND COALESCE(documentoassinado.documentoAssinadoInvalido, FALSE) IS FALSE ");
		 sql.append(" AND expedicaodiploma.dataexpedicao::DATE > conf.databasevalidacaodiplomadigital )) ");
		 sql.append(" ELSE documentoassinado.codigo IS NOT NULL AND COALESCE(documentoassinado.documentoAssinadoInvalido, FALSE) IS FALSE END ");
		 sql.append(" AND NOT EXISTS (SELECT FROM documentoassinadopessoa WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('REJEITADO','PENDENTE')) ");
		 sql.append(" AND CASE WHEN conf.databasevalidacaodiplomadigital IS NOT NULL THEN ((expedicaodiploma.dataexpedicao::DATE <= conf.databasevalidacaodiplomadigital) ");
		 sql.append(" OR (documentoassinado.codigo IS NOT NULL AND expedicaodiploma.dataexpedicao::DATE > conf.databasevalidacaodiplomadigital ");
		 sql.append(" AND (arquivo.codigo IS NOT NULL OR arquivovisual.codigo IS NOT NULL))) ");
		 sql.append(" ELSE (arquivo.codigo IS NOT NULL OR arquivovisual.codigo IS NOT NULL) END ");
		 sql.append(" order by documentoassinado.codigo desc ");
		 SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray());
	   	 return montarDadosLivroFolhaReciboDiploma(tabelaResultado, usuarioVO );
	}
	
	 private List<ControleLivroFolhaReciboVO> montarDadosLivroFolhaReciboDiploma(SqlRowSet rs, UsuarioVO usuario) throws Exception{
		 List<ControleLivroFolhaReciboVO> controleLivroFolhaReciboVOs  = new ArrayList<ControleLivroFolhaReciboVO>();	 
	     while (rs.next()) {
	    	 controleLivroFolhaReciboVOs.add(montarDadosCompletoLivroFolhaReciboDiploma(rs, usuario));
	     }
	     return controleLivroFolhaReciboVOs;
	 }
	 
	 
	 private ControleLivroFolhaReciboVO montarDadosCompletoLivroFolhaReciboDiploma(SqlRowSet dadosSQL, UsuarioVO usuario) {
		 ControleLivroFolhaReciboVO obj = new ControleLivroFolhaReciboVO();
	     obj.setCodigo(dadosSQL.getInt("codigo"));
	     obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
	     obj.setFolhaReciboAtual(dadosSQL.getInt("folhaReciboatual"));
	     obj.setDataPublicacao(dadosSQL.getDate("dataPublicacao"));
	     obj.getControleLivroRegistroDiploma().setNrLivro(dadosSQL.getInt("controlelivroregistrodiploma_numerolivro"));
	     obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("aluno_codigo"));
	     obj.getMatricula().getAluno().setNome(dadosSQL.getString("aluno_nome"));
	     obj.getMatricula().getAluno().setRG(dadosSQL.getString("aluno_rg"));
	     obj.getMatricula().getAluno().setOrgaoEmissor(dadosSQL.getString("aluno_orgaoemissor"));
	     obj.getMatricula().getAluno().setEstadoEmissaoRG(dadosSQL.getString("aluno_estadoemissaorg"));
	     obj.getMatricula().getAluno().setCPF(dadosSQL.getString("aluno_cpf"));
	     obj.getMatricula().getAluno().setDataNasc(dadosSQL.getDate("aluno_data_nascimento"));
	     obj.getMatricula().getCurso().setCodigo(dadosSQL.getInt("curso_codigo"));
	     obj.getMatricula().getCurso().setNome(dadosSQL.getString("curso_nome"));
	     obj.getMatricula().getCurso().setIdCursoInep(dadosSQL.getInt("curso_idCursoInep"));
	     obj.getMatricula().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino_codigo"));
	     obj.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino_nome"));
	     obj.getMatricula().getUnidadeEnsino().setCredenciamento(dadosSQL.getString("unidadeensino_credenciamento"));
	     obj.getMatricula().getUnidadeEnsino().setCodigoIES(dadosSQL.getInt("unidadeensino_codigoIES"));
	     obj.getMatricula().getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("unidadeensino_razaoSocial"));
	     obj.getMatricula().getUnidadeEnsino().setCodigoIESMantenedora(dadosSQL.getInt("unidadeensino_codigoIESMantenedora"));
	     obj.getMatricula().getUnidadeEnsino().setMantenedora(dadosSQL.getString("unidadeensino_mantenedora"));	     		 
	     obj.getMatricula().getUnidadeEnsino().setCodigoIESUnidadeCertificadora(dadosSQL.getInt("unidadeensino_codigoIESUnidadeCertificadora"));
	     obj.getMatricula().getUnidadeEnsino().setUnidadeCertificadora(dadosSQL.getString("unidadeensino_unidadeCertificadora"));
	     obj.getMatricula().setAnoConclusao(dadosSQL.getString("matricula_anoconclusao"));
	     obj.getMatricula().setSemestreConclusao(dadosSQL.getString("matricula_semestreconclusao"));
	     obj.getMatricula().setData(dadosSQL.getDate("matricula_data"));
	     obj.getMatricula().setDataInicioCurso(dadosSQL.getDate("matricula_dataInicioCurso"));
	     obj.getMatricula().setDataConclusaoCurso(dadosSQL.getDate("matricula_dataconclusaocurso"));
	     obj.getMatricula().setDataColacaoGrau(dadosSQL.getDate("matricula_datacolacaograu"));
	     obj.getExpedicaoDiplomaVO().setCodigo(dadosSQL.getInt("expedicao_diploma_codigo"));
	     obj.getExpedicaoDiplomaVO().setDataExpedicao(dadosSQL.getDate("data_expedicao_diploma"));
	     obj.getExpedicaoDiplomaVO().setDataPublicacaoDiarioOficial(dadosSQL.getDate("data_puplicacao_diario_oficial"));
	     obj.getExpedicaoDiplomaVO().setNumeroProcesso(dadosSQL.getString("expedicaodiploma_numeroprocesso"));
	     obj.getExpedicaoDiplomaVO().setNumeroRegistroDiploma(dadosSQL.getString("expedicaodiploma_numeroregistro"));
	     obj.getExpedicaoDiplomaVO().setVia(dadosSQL.getString("expedicaodiploma_via"));
	     obj.getExpedicaoDiplomaVO().setLivroRegistradora(dadosSQL.getString("expedicaodiploma_nrlivroregistradora"));
	     obj.getExpedicaoDiplomaVO().setFolhaReciboRegistradora(dadosSQL.getString("expedicaodiploma_nrfolharecibo"));
	     if (Uteis.isAtributoPreenchido(dadosSQL.getString("documentoAssinado.tipoOrigemDocumentoAssinado"))) {
	    	 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.valueOf(dadosSQL.getString("documentoAssinado.tipoOrigemDocumentoAssinado")));
	     }
		 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual().setCodigo(new Integer(dadosSQL.getInt("arquivovisual.codigo")));
		 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual().setCodOrigem(dadosSQL.getInt("arquivovisual.codOrigem"));
		 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual().setNome(dadosSQL.getString("arquivovisual.nome"));
		 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual().setDescricao(dadosSQL.getString("arquivovisual.descricao"));
		 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual().setPastaBaseArquivo(dadosSQL.getString("arquivovisual.pastaBaseArquivo"));
		 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo().setCodigo(new Integer(dadosSQL.getInt("arquivo.codigo")));
		 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo().setCodOrigem(dadosSQL.getInt("arquivo.codOrigem"));
		 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo().setNome(dadosSQL.getString("arquivo.nome"));
		 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo().setDescricao(dadosSQL.getString("arquivo.descricao"));
		 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo().setPastaBaseArquivo(dadosSQL.getString("arquivo.pastaBaseArquivo"));
		 obj.getExpedicaoDiplomaVO().setDataRegistroDiploma(dadosSQL.getDate("expedicaodiploma.dataregistrodiploma"));
		 obj.getExpedicaoDiplomaVO().setSerial(dadosSQL.getString("serial"));
		 obj.getExpedicaoDiplomaVO().setEmitidoPorProcessoTransferenciaAssistida(dadosSQL.getBoolean("emitidoporprocessotransferenciaassistida"));
		 if (obj.getExpedicaoDiplomaVO().getEmitidoPorProcessoTransferenciaAssistida()) {
	        	obj.getExpedicaoDiplomaVO().setNomeIesPTA(dadosSQL.getString("nomeIesPTA"));
	        	obj.getExpedicaoDiplomaVO().setCnpjPTA(dadosSQL.getString("cnpjPTA"));
	        	obj.getExpedicaoDiplomaVO().setCodigoMecPTA(dadosSQL.getInt("codigoMecPTA"));	        	
	        }
	        obj.getExpedicaoDiplomaVO().setEmitidoPorDecisaoJudicial(dadosSQL.getBoolean("emitidoPorDecisaoJudicial"));
	        if (obj.getExpedicaoDiplomaVO().getEmitidoPorDecisaoJudicial()) {
	        	obj.getExpedicaoDiplomaVO().setNomeJuizDecisaoJudicial(dadosSQL.getString("nomeJuizDecisaoJudicial"));
	        	obj.getExpedicaoDiplomaVO().setNumeroProcessoDecisaoJudicial(dadosSQL.getString("numeroProcessoDecisaoJudicial"));
	        	obj.getExpedicaoDiplomaVO().setDecisaoJudicial(dadosSQL.getString("decisaoJudicial"));
	        	obj.getExpedicaoDiplomaVO().setInformacoesAdicionaisDecisaoJudicial(dadosSQL.getString("informacoesAdicionaisDecisaoJudicial"));
	        }
		 if (Uteis.isAtributoPreenchido(dadosSQL.getString("documentoassinado.tipoorigemdocumentoassinado"))) {
			 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().setTipoOrigemDocumentoAssinadoEnum(TipoOrigemDocumentoAssinadoEnum.valueOf(dadosSQL.getString("documentoassinado.tipoorigemdocumentoassinado")));
		 }
		 if(Uteis.isAtributoPreenchido(dadosSQL.getString("arquivovisual.servidorarquivoonline"))){
			obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivoVisual().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("arquivovisual.servidorarquivoonline")));	
		 }
		 if(Uteis.isAtributoPreenchido(dadosSQL.getString("arquivo.servidorarquivoonline"))){
			 obj.getExpedicaoDiplomaVO().getDocumentoAssinadoVO().getArquivo().setServidorArquivoOnline(ServidorArquivoOnlineEnum.valueOf(dadosSQL.getString("arquivo.servidorarquivoonline")));	
		 }
		 if (obj.getExpedicaoDiplomaVO().getEmitidoPorDecisaoJudicial()) {
				try {
					obj.getExpedicaoDiplomaVO().setDeclaracaoAcercaProcessoJudicialVOs(getFacadeFactory().getDeclaracaoAcercaProcessoJudicialInterfaceFacade().consultar(obj.getExpedicaoDiplomaVO().getCodigo()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	     obj.setNovoObj(false);
		 return obj;
	 }
	 
	 private void executarValidacaoUnicidade(List<ControleLivroFolhaReciboVO> controleLivroFolhaReciboVOs, Function<ControleLivroFolhaReciboVO, String> chaveUnicidade, String mensagemErro) throws ConsistirException {
		if (Uteis.isAtributoPreenchido(controleLivroFolhaReciboVOs)) {
			List<String> registrosDuplicados = new ArrayList<>();
			controleLivroFolhaReciboVOs.parallelStream()
					.collect(Collectors.groupingBy(chaveUnicidade, Collectors.counting()))
					.entrySet()
					.stream().filter(p -> p.getValue().intValue() > 1)
					.map(Map.Entry::getKey)
					.forEach(registrosDuplicados::add);
			if (Uteis.isAtributoPreenchido(registrosDuplicados)) {
				throw new ConsistirException(mensagemErro + registrosDuplicados.stream().collect(Collectors.joining("; ")));
			}
		}
	}

	private String chaveUnicidadeMatriculaVia(ControleLivroFolhaReciboVO controleLivroFolhaReciboVO) {
		return new StringBuilder().append(controleLivroFolhaReciboVO.getMatricula().getMatricula()).append(" - VIA: ").append(controleLivroFolhaReciboVO.getVia()).toString();
	}
	
	/**
     * Responsável por realizar uma consulta de <code>ControleLivroFolhaRecibo</code> através do valor do atributo 
     * <code>matricula</code> da classe <code>Matricula</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>ControleLivroFolhaReciboVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
	@Override
    public ControleLivroFolhaReciboVO  consultarPorMatriculaMatriculaPrimeiraEUltimaVia(String valorConsulta, Integer curso, TipoLivroRegistroDiplomaEnum tipoLivro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);        
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" select * from ( ");
		sqlStr.append(" SELECT ControleLivroFolhaRecibo.* ");
		sqlStr.append(" FROM ControleLivroFolhaRecibo ");
		sqlStr.append(" INNER JOIN Matricula on Matricula.matricula = ControleLivroFolhaRecibo.matricula ");
		sqlStr.append(" INNER JOIN ControleLivroRegistroDiploma on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
		sqlStr.append(" WHERE ( Matricula.matricula ) like(?) ");
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND ControleLivroRegistroDiploma.curso = ").append(curso).append(" ");
		}
		sqlStr.append(" AND ControleLivroRegistroDiploma.tipoLivroRegistroDiplomaEnum = '").append(tipoLivro).append("' ");
		sqlStr.append(" AND ControleLivroFolhaRecibo.via = ( ");
		sqlStr.append(" select MAX(ControleLivroFolhaRecibo.via) ");
		sqlStr.append(" from ControleLivroFolhaRecibo ");
		sqlStr.append(" INNER JOIN ControleLivroRegistroDiploma on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
		sqlStr.append(" WHERE (ControleLivroFolhaRecibo.matricula) like(?) ");
		sqlStr.append(" AND ControleLivroRegistroDiploma.tipoLivroRegistroDiplomaEnum = '").append(tipoLivro).append("')");
		sqlStr.append(") t LEFT join ( ");
		sqlStr.append(" select nrlivro as nrlivro_anterior, folhareciboatual as folharecibo_anterior, datapublicacao as datapublicacao_anterior ");
		sqlStr.append(" from ControleLivroFolhaRecibo ");
		sqlStr.append(" inner join Matricula on Matricula.matricula = ControleLivroFolhaRecibo.matricula ");
		sqlStr.append(" inner join ControleLivroRegistroDiploma on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
		sqlStr.append(" WHERE ( Matricula.matricula ) like(?) ");
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND ControleLivroRegistroDiploma.curso = ").append(curso).append(" ");
		}
		sqlStr.append(" AND ControleLivroRegistroDiploma.tipoLivroRegistroDiplomaEnum = '").append(tipoLivro).append("' ");
		sqlStr.append(" AND ControleLivroFolhaRecibo.via = ( ");
		sqlStr.append(" select min(ControleLivroFolhaRecibo.via) ");
		sqlStr.append(" from ControleLivroFolhaRecibo ");
		sqlStr.append(" INNER JOIN ControleLivroRegistroDiploma on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma ");
		sqlStr.append(" WHERE (ControleLivroFolhaRecibo.matricula) like(?) ");
		sqlStr.append(" AND ControleLivroRegistroDiploma.tipoLivroRegistroDiplomaEnum = '").append(tipoLivro).append("')");
		sqlStr.append(")  x on true ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta, valorConsulta, valorConsulta, valorConsulta);
		if (tabelaResultado.next()){
	        ControleLivroFolhaReciboVO montarDados = montarDados(tabelaResultado, nivelMontarDados, usuario);
	        if (Uteis.isAtributoPreenchido(tipoLivro)) {
				if (tipoLivro.equals(TipoLivroRegistroDiplomaEnum.DIPLOMA)) {
					montarDados.setNrFolhaReciboDiplomaPrimeiraVia(tabelaResultado.getInt("folharecibo_anterior"));
					montarDados.setNrLivroDiplomaPrimeiraVia(tabelaResultado.getInt("nrlivro_anterior"));
					montarDados.setDataPublicacaoDiplomaPrimeiraVia(tabelaResultado.getDate("datapublicacao_anterior"));
				} else if (tipoLivro.equals(TipoLivroRegistroDiplomaEnum.CERTIFICADO)) {
					montarDados.setNrFolhaReciboCertificadoPrimeiraVia(tabelaResultado.getInt("folharecibo_anterior"));
					montarDados.setNrLivroCertificadoPrimeiraVia(tabelaResultado.getInt("nrlivro_anterior"));
					montarDados.setDataPublicacaoCertificadoPrimeiraVia(tabelaResultado.getDate("datapublicacao_anterior"));
				}
			}
	        return montarDados;
		}
		return null;
    }
}