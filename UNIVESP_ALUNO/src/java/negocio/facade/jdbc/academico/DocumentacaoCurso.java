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

import negocio.comuns.academico.DocumentacaoCursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DocumentacaoCursoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>DocumentacaoCursoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>DocumentacaoCursoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see DocumentacaoCursoVO
 * @see ControleAcesso
 * @see Curso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy 
public class DocumentacaoCurso extends ControleAcesso implements DocumentacaoCursoInterfaceFacade {

	protected static String idEntidade;

	public DocumentacaoCurso() throws Exception {
		super();
		setIdEntidade("Curso");
	}

	public DocumentacaoCursoVO novo() throws Exception {
		DocumentacaoCurso.incluir(getIdEntidade());
		DocumentacaoCursoVO obj = new DocumentacaoCursoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final DocumentacaoCursoVO obj, UsuarioVO usuario) throws Exception {
		DocumentacaoCursoVO.validarDados(obj);
		final String sql = "INSERT INTO DocumentacaoCurso( tipoDeDocumento, curso, gerarSuspensaoMatricula, impedirRenovacaoMatricula ) VALUES ( ? , ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getTipoDeDocumentoVO().getCodigo().intValue());
				sqlInserir.setInt(2, obj.getCurso().intValue());
                                sqlInserir.setBoolean(3, obj.getGerarSuspensaoMatricula());
                                sqlInserir.setBoolean(4, obj.getImpedirRenovacaoMatricula());
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

		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final DocumentacaoCursoVO obj, UsuarioVO usuario) throws Exception {
		DocumentacaoCursoVO.validarDados(obj);
		final String sql = "UPDATE DocumentacaoCurso set tipoDeDocumento=?, curso=?, gerarSuspensaoMatricula=?, impedirRenovacaoMatricula=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getTipoDeDocumentoVO().getCodigo().intValue());
				sqlAlterar.setInt(2, obj.getCurso().intValue());
                                sqlAlterar.setBoolean(3, obj.getGerarSuspensaoMatricula());
                                sqlAlterar.setBoolean(4, obj.getImpedirRenovacaoMatricula());
				sqlAlterar.setInt(5, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DocumentacaoCursoVO obj, UsuarioVO usuario) throws Exception {
		DocumentacaoCurso.excluir(getIdEntidade());
		String sql = "DELETE FROM DocumentacaoCurso WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DocumentacaoCurso.* FROM DocumentacaoCurso, Curso WHERE DocumentacaoCurso.curso = Curso.codigo and Curso.nome like('" + valorConsulta + "%') ORDER BY Curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	public List consultarPorTipoDeDocumento(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DocumentacaoCurso WHERE tipoDeDocumento >= " + valorConsulta + " ORDER BY tipoDeDocumento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DocumentacaoCurso WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>DocumentacaoCursoVO</code> resultantes da consulta.
	 */
	public List<DocumentacaoCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<DocumentacaoCursoVO> vetResultado = new ArrayList<DocumentacaoCursoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>DocumentacaoCursoVO</code>.
	 * 
	 * @return O objeto da classe <code>DocumentacaoCursoVO</code> com os dados devidamente montados.
	 */
	public DocumentacaoCursoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		DocumentacaoCursoVO obj = new DocumentacaoCursoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setCurso(new Integer(dadosSQL.getInt("curso")));
		obj.getTipoDeDocumentoVO().setCodigo(new Integer(dadosSQL.getInt("tipoDeDocumento")));
		obj.setGerarSuspensaoMatricula(dadosSQL.getBoolean("gerarSuspensaoMatricula"));
		obj.setImpedirRenovacaoMatricula(dadosSQL.getBoolean("impedirRenovacaoMatricula"));
		obj.setData(dadosSQL.getDate("data")) ;    
		obj.setNovoObj(Boolean.FALSE);
		montarDadosTipoDeDocumento(obj, usuario);
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDocumentacaoCursos(Integer curso, UsuarioVO usuario) throws Exception {
		DocumentacaoCurso.excluir(getIdEntidade());
		String sql = "DELETE FROM DocumentacaoCurso WHERE (curso = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { curso });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDocumentacaoCursos(Integer curso, List objetos, UsuarioVO usuario) throws Exception {
		excluirDocumentacaoCursos(curso, usuario);
		incluirDocumentacaoCursos(curso, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDocumentacaoCursos(Integer cursoPrm, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			DocumentacaoCursoVO obj = (DocumentacaoCursoVO) e.next();
			obj.setCurso(cursoPrm);
			incluir(obj, usuario);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>DocumentacaoCursoVO</code> relacionados a um objeto da classe
	 * <code>academico.Curso</code>.
	 * 
	 * @param curso
	 *            Atributo de <code>academico.Curso</code> a ser utilizado para localizar os objetos da classe
	 *            <code>DocumentacaoCursoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>DocumentacaoCursoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public  List consultarDocumentacaoCursos(MatriculaVO matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		// String sql = "SELECT * FROM DocumentacaoCurso WHERE curso = ?";
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT documentacaocurso.* ");
		sqlStr.append(" FROM documentacaocurso ");
		sqlStr.append(" INNER JOIN tipodocumento ON tipodocumento.codigo = documentacaocurso.tipodedocumento ");
		sqlStr.append(" WHERE curso = ").append(matricula.getCurso().getCodigo());
		 /**
         * Filtra a regra do sexo
         */
        if(matricula.getAluno().getSexo().equals("M")) {
			sqlStr.append(" AND (sexo = 'HOMEM' or sexo = 'AMBOS') ");
		} else if (matricula.getAluno().getSexo().equals("F")) {
			sqlStr.append(" AND (sexo = 'MULHER' or sexo = 'AMBOS') ");
		} else{
			sqlStr.append(" AND (sexo = 'AMBOS') ");
		}
        /**
         * Filtra a regra do estado civil
         */
        if(!matricula.getAluno().getEstadoCivil().trim().isEmpty()){
        	sqlStr.append(" AND (estadoCivil = '").append(matricula.getAluno().getEstadoCivil()).append("' or estadoCivil = '') ");
        }else{
        	sqlStr.append(" AND (estadoCivil = '') ");
        }
        /**
         * Filtra a regra da Idade
         */
        if (matricula.getAluno().getDataNasc() != null) {
        	sqlStr.append(" AND ((tipoidadeexigida  = 'MINIMA' and idade <= ").append(Uteis.getCalculaQuantidadeAnosEntreDatas(matricula.getAluno().getDataNasc(), new Date())).append(")");
        	sqlStr.append(" or (tipoidadeexigida = 'MAXIMA' and ").append(Uteis.getCalculaQuantidadeAnosEntreDatas(matricula.getAluno().getDataNasc(), new Date())).append(" <= idade )");
        	sqlStr.append(" or idade = 0) ");
        }else{
        	sqlStr.append(" AND (idade = 0) ");
        }
        
        /**
         * Filtra a regra do estrangeiro
         */
        if(matricula.getAluno().getNacionalidade().getCodigo() != null && matricula.getAluno().getNacionalidade().getCodigo() != 0 
        	&& !matricula.getAluno().getNacionalidade().getCodigo().equals(matricula.getUnidadeEnsino().getCidade().getEstado().getPaiz().getCodigo())){
        	sqlStr.append(" AND (estrangeiro = 'S' or estrangeiro = '') ");
        }else{
        	sqlStr.append(" AND (estrangeiro = 'N' or estrangeiro = '') ");
        }
        
        /**
         * Filtra a regra da forma de ingresso
         */
        sqlStr.append(" AND ( (transferencia = false and portadorDiploma = false and inscricaoProcessoSeletivo = false and enem = false) ");
        if(matricula.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor()) || matricula.getFormaIngresso().equals(FormaIngresso.VESTIBULAR.getValor())){
        	sqlStr.append(" OR (inscricaoProcessoSeletivo = true) ");
        	
        }else if(matricula.getFormaIngresso().equals(FormaIngresso.PORTADOR_DE_DIPLOMA.getValor())){
        	sqlStr.append(" or (portadorDiploma = true) ");        	
        }else if(matricula.getFormaIngresso().equals(FormaIngresso.TRANSFERENCIA_EXTERNA.getValor())){
        	sqlStr.append(" or (transferencia = true) ");        	                
        }else if(matricula.getFormaIngresso().equals(FormaIngresso.ENEM.getValor())){
        	sqlStr.append(" or (enem = true) ");        	                
        }
        
        if(matricula == null || matricula.getMatricula() == null || matricula.getMatricula().trim().isEmpty() ){
        	sqlStr.append(" or (reabertura = true) ");// and (select count(codigo) from matriculaPeriodo where situacaoMatriculaPeriodo not in ('PC') and matricula = '").append(matricula.getMatricula()).append("') > 0 ) ");        	
        }
        
        if(matricula != null && !matricula.getMatricula().trim().isEmpty() ){
        	sqlStr.append(" or ( reabertura = true and (select situacaoMatriculaPeriodo from matriculaPeriodo ");
        	sqlStr.append(" where situacaoMatriculaPeriodo in ('AC', 'TR') and matricula = '").append(matricula.getMatricula()).append("' "); 
        	sqlStr.append(" and codigo < (select max(codigo) from matriculaperiodo mp where mp.matricula = '").append(matricula.getMatricula()).append("' ) limit 1) in ('AC', 'TR')) ");        	
        }
        sqlStr.append(" )");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (resultado.next()) {
			objetos.add(montarDados(resultado, usuario));
		}
		return objetos;
	}

	@Override
	public List<DocumentacaoCursoVO> consultarPorCurso(Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT documentacaocurso.* ");
		sqlStr.append(" FROM documentacaocurso ");
		sqlStr.append(" INNER JOIN tipodocumento ON tipodocumento.codigo = documentacaocurso.tipodedocumento ");
		sqlStr.append(" WHERE curso = ").append(curso);		
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(resultado, usuario);
	}
	
	public DocumentacaoCursoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM DocumentacaoCurso WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	public  void montarDadosTipoDeDocumento(DocumentacaoCursoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getTipoDeDocumentoVO().getCodigo().intValue() == 0) {
			obj.setTipoDeDocumentoVO(new TipoDocumentoVO());
			return;
		}
		obj.setTipoDeDocumentoVO(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimaria(obj.getTipoDeDocumentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return DocumentacaoCurso.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		DocumentacaoCurso.idEntidade = idEntidade;
	}
}