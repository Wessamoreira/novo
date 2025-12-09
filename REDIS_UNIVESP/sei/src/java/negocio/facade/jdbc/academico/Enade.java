package negocio.facade.jdbc.academico;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.EnadeCursoVO;
import negocio.comuns.academico.EnadeVO;
import negocio.comuns.academico.TextoEnadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.MapaConvocacaoEnadeVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMapaConvocacaoEnadeEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.EnadeInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>EnadeVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>EnadeVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see EnadeVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy 
public class Enade extends ControleAcesso implements EnadeInterfaceFacade {

	protected static String idEntidade;

	public Enade() throws Exception {
		super();
		setIdEntidade("Enade");
	}

	public EnadeVO novo() throws Exception {
		Enade.incluir(getIdEntidade());
		EnadeVO obj = new EnadeVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>EnadeVO</code>. Primeiramente valida
	 * os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>EnadeVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final EnadeVO obj,UsuarioVO usuario) throws Exception {
		try {
			EnadeVO.validarDados(obj);
			Enade.incluir(getIdEntidade(), true, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO Enade( tituloEnade, portariaNormativa, dataPortaria, dataPublicacaoPortariaDOU, dataprova, codigoprojeto) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getTituloEnade());
					sqlInserir.setString(2, obj.getPortariaNormativa());
					sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataPortaria()));
					if (Uteis.isAtributoPreenchido(obj.getDataPortaria())) {
						sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataPortaria()));
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataPublicacaoPortariaDOU()));
					sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataProva()));
					sqlInserir.setString(6, obj.getCodigoProjeto());
					
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
			getFacadeFactory().getTextoEnadeFacade().incluirTextoEnadeVOs(obj);
			getFacadeFactory().getEnadeCursoFacade().incluirEnadeCursoVOs(obj, usuario);
			obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>EnadeVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados
	 * (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>EnadeVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final EnadeVO obj,UsuarioVO usuario) throws Exception {
		try {
			EnadeVO.validarDados(obj);
			Enade.alterar(getIdEntidade(), true, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE Enade set tituloEnade=?, portariaNormativa=?, dataPortaria=?, dataPublicacaoPortariaDOU=?, dataprova=?, codigoprojeto =? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getTituloEnade());
					sqlAlterar.setString(2, obj.getPortariaNormativa());
					if (Uteis.isAtributoPreenchido(obj.getDataPortaria())) {
						sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataPortaria()));
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataPublicacaoPortariaDOU()));
					sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataProva()));
					sqlAlterar.setString(6, obj.getCodigoProjeto());
					sqlAlterar.setInt(7, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getTextoEnadeFacade().alterarTextoEnadeVOs(obj);
			getFacadeFactory().getEnadeCursoFacade().alterarEnadeCursoVOs(obj, usuario);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>EnadeVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>EnadeVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(EnadeVO obj,UsuarioVO usuario) throws Exception {
		try {
			Enade.excluir(getIdEntidade(), true, usuario);
			getFacadeFactory().getTextoEnadeFacade().excluirTextoEnadePorEnade(obj);
			getFacadeFactory().getEnadeCursoFacade().excluirEnadeCursoPorEnade(obj);
			String sql = "DELETE FROM Enade WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Enade</code> através do valor do atributo
	 * <code>Date dataPublicacaoPortariaDOU</code>. Retorna os objetos com valores pertecentes ao período informado por
	 * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EnadeVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<EnadeVO> consultarPorDataPublicacaoPortariaDOU(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Enade WHERE ((dataPublicacaoPortariaDOU >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataPublicacaoPortariaDOU <= '" + Uteis.getDataJDBC(prmFim)
				+ "')) ORDER BY dataPublicacaoPortariaDOU";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Enade</code> através do valor do atributo
	 * <code>String tituloEnade</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EnadeVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<EnadeVO> consultarPorTituloEnade(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Enade WHERE upper( tituloEnade ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tituloEnade";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Enade</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EnadeVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<EnadeVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Enade WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>EnadeVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>EnadeVO</code>.
	 * 
	 * @return O objeto da classe <code>EnadeVO</code> com os dados devidamente montados.
	 */
	public static EnadeVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
            ////System.out.println(">> Montar dados(Enade) - " + new Date());
		EnadeVO obj = new EnadeVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setTituloEnade(dadosSQL.getString("tituloEnade"));
		obj.setPortariaNormativa(dadosSQL.getString("portariaNormativa"));
		obj.setDataPortaria(dadosSQL.getDate("dataPortaria"));
		obj.setDataPublicacaoPortariaDOU(dadosSQL.getDate("dataPublicacaoPortariaDOU"));
		obj.setDataProva(dadosSQL.getDate("dataprova"));
		obj.setCodigoProjeto(dadosSQL.getString("codigoProjeto"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setTextoEnadeVOs(getFacadeFactory().getTextoEnadeFacade().consultarPorEnade(obj.getCodigo()));
		obj.setEnadeCursoVOs(getFacadeFactory().getEnadeCursoFacade().consultarPorEnade(obj.getCodigo(), usuario));
		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>EnadeVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public EnadeVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Enade WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Enade ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return Enade.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Enade.idEntidade = idEntidade;
	}
	
	@Override
	public void adicionarTextoEnadeVOs(EnadeVO enadeVO, TextoEnadeVO textoEnadeVO) throws Exception{
		getFacadeFactory().getTextoEnadeFacade().validarDados(textoEnadeVO);
		int x = 0;
		for(TextoEnadeVO objExistente: enadeVO.getTextoEnadeVOs()){
			if(textoEnadeVO.getTextoEdicao().trim().isEmpty()){
				if(Uteis.removerAcentuacao(objExistente.getTexto().trim()).equals(Uteis.removerAcentuacao(textoEnadeVO.getTexto().trim()))){
					throw new ConsistirException(UteisJSF.internacionalizar("msg_TextoEnade_existente"));
				}
			}else{
				
				if(!Uteis.removerAcentuacao(objExistente.getTextoEdicao().trim()).equals(Uteis.removerAcentuacao(textoEnadeVO.getTextoEdicao().trim()))){
					if(Uteis.removerAcentuacao(objExistente.getTexto().trim()).equals(Uteis.removerAcentuacao(textoEnadeVO.getTexto().trim()))){
						throw new ConsistirException(UteisJSF.internacionalizar("msg_TextoEnade_existente"));
					}					
				}else{
					enadeVO.getTextoEnadeVOs().set(x, textoEnadeVO);
					return;
				}
			}
			x++;
		}
		
		enadeVO.getTextoEnadeVOs().add(textoEnadeVO);
	}
	@Override
	public void excluirTextoEnadeVOs(EnadeVO enadeVO, TextoEnadeVO textoEnadeVO) throws Exception{
		int x = 0;
		for(TextoEnadeVO objExistente: enadeVO.getTextoEnadeVOs()){
			if(Uteis.removerAcentuacao(objExistente.getTexto().trim()).equals(Uteis.removerAcentuacao(textoEnadeVO.getTexto().trim()))){
				enadeVO.getTextoEnadeVOs().remove(x);
				return;
			}
			x++;
		}
		
		
	}
	
	@Override
	public void adicionarEnadeCursoVOs(EnadeVO enadeVO, EnadeCursoVO enadeCursoVO) throws Exception {
		getFacadeFactory().getEnadeCursoFacade().validarDados(enadeCursoVO);
		int index = 0;
		for (EnadeCursoVO enadeCursoExistente : enadeVO.getEnadeCursoVOs()) {
			if (enadeCursoExistente.getCursoVO().getCodigo().equals(enadeCursoVO.getCursoVO().getCodigo())) {
				enadeVO.getEnadeCursoVOs().set(index, enadeCursoVO);
				return;
			}
			index++;
		}
		enadeVO.getEnadeCursoVOs().add(enadeCursoVO);
	}
	
	@Override
	public void excluirEnadeCursoVOs(EnadeVO enadeVO, EnadeCursoVO enadeCursoVO) throws Exception{
		int x = 0;
		for(EnadeCursoVO objExistente: enadeVO.getEnadeCursoVOs()){
			if(objExistente.getCursoVO().getCodigo().equals(enadeCursoVO.getCursoVO().getCodigo())){
				enadeVO.getEnadeCursoVOs().remove(x);
				return;
			}
			x++;
		}
	}
	
	@Override
	public List consultarEnade(DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		List<Object> listaFiltro = new ArrayList<>(0);
		sql.append("SELECT count(*) over() as qtde_total_registros, * FROM Enade ");
		if (dataModelo.getCampoConsulta().equals("codigo")) {
			sql.append("WHERE codigo >= ? ");
			listaFiltro.add(Integer.valueOf(dataModelo.getValorConsulta()));
		} else if (dataModelo.getCampoConsulta().equals("tituloEnade")) {
			sql.append("WHERE tituloEnade ILIKE(?) ");
			listaFiltro.add("%" + dataModelo.getValorConsulta() + "%");
		} else if (dataModelo.getCampoConsulta().equals("dataPublicacaoPortariaDOU")) {
			sql.append("WHERE ((dataPublicacaoPortariaDOU >= ?) AND (dataPublicacaoPortariaDOU <= ?)) ");
			listaFiltro.add(Uteis.getDataJDBC(dataModelo.getDataIni()));
			listaFiltro.add(Uteis.getDataJDBC(dataModelo.getDataFim()));
		}
		if (dataModelo.getCampoConsulta().equals("codigo")) {
			sql.append("ORDER BY codigo ");
		} else if (dataModelo.getCampoConsulta().equals("tituloEnade")) {
			sql.append("ORDER BY tituloEnade ");
		} else if (dataModelo.getCampoConsulta().equals("dataPublicacaoPortariaDOU")) {
			sql.append("ORDER BY dataPublicacaoPortariaDOU ");
		}
		if (Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
			sql.append(" LIMIT ").append(dataModelo.getLimitePorPagina());
			sql.append(" OFFSET ").append(dataModelo.getOffset());
		}
		dataModelo.setTotalRegistrosEncontrados(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), listaFiltro.toArray());
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	private StringBuilder getSQLPadraoConsultaCompleta() {
		StringBuilder str = new StringBuilder();
		str.append(" select distinct mapaconvocacaoenade.codigo, mapaconvocacaoenade.situacaoMapaConvocacaoEnade, unidadeensino.codigo AS \"unidadeensino.codigo\", unidadeensino.nome AS \"unidadeensino.nome\", ");
		str.append(" enadeCurso.codigo AS \"enadeCurso.codigo\", enadeCurso.PercentualCargaHorariaConcluinte AS \"enadeCurso.PercentualCargaHorariaConcluinte\", enadeCurso.PercentualCargaHorariaIngressante AS \"enadeCurso.PercentualCargaHorariaIngressante\", enadeCurso.anobaseingressante AS \"enadeCurso.anobaseingressante\", enadeCurso.periodoPrevistoTerminoIngressante AS \"enadeCurso.periodoPrevistoTerminoIngressante\", enadeCurso.periodoPrevistoTerminoConcluinte AS \"enadeCurso.periodoPrevistoTerminoConcluinte\", curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
		str.append(" enade.codigo AS \"enade.codigo\", enade.tituloenade AS \"enade.tituloenade\", usuario.codigo AS \"usuario.codigo\", usuario.nome AS \"usuario.nome\", ");
		str.append(" dataabertura, datafechamento, dataprevisaoconclusao, dataprova, enade.dataPortaria AS \"enade.dataPortaria\", curso.idcursoinep AS \"curso.idcursoinep\", mapaconvocacaoenade.arquivoalunoingressante AS \"mapaconvocacaoenade.arquivoalunoingressante\", mapaconvocacaoenade.arquivoalunoconcluinte AS \"mapaconvocacaoenade.arquivoalunoconcluinte\", arquivoconcluinte.nome AS \"arquivoconcluinte\", arquivoingressante.nome AS \"arquivoingressante\" ");
		str.append(" from mapaconvocacaoenade  ");
		str.append(" inner join mapaconvocacaoenadematricula on mapaconvocacaoenadematricula.mapaConvocacaoEnade = mapaConvocacaoEnade.codigo ");
		str.append(" inner join matricula on matricula.matricula = mapaconvocacaoenadematricula.matricula ");
		str.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		str.append(" inner join unidadeEnsino on unidadeEnsino.codigo = mapaconvocacaoenade.unidadeEnsino ");
		str.append(" inner join enadeCurso on enadeCurso.codigo = mapaconvocacaoenade.enadeCurso ");
		str.append(" inner join curso on curso.codigo = enadeCurso.curso ");
		str.append(" inner join enade on enade.codigo = enadeCurso.enade ");
		str.append(" inner join usuario on usuario.codigo = mapaconvocacaoenade.responsavel ");
		str.append(" LEFT join arquivo arquivoconcluinte on arquivoconcluinte.codigo = mapaconvocacaoenade.arquivoalunoconcluinte ");
		str.append(" LEFT join arquivo arquivoingressante on arquivoingressante.codigo = mapaconvocacaoenade.arquivoalunoingressante ");
		return str;
	}
	
	public MapaConvocacaoEnadeVO consultaRapidaPorEnadeParaTXT(String valorConsulta,  boolean controlarAcesso, UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados, Integer codigoCurso, String tipoAluno) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append("WHERE sem_acentos(lower(enade.tituloEnade)) like(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		sqlStr.append("and curso.codigo = ").append(codigoCurso);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapidaParaTXT(tabelaResultado, usuarioVO);
	}

	public MapaConvocacaoEnadeVO montarDadosConsultaRapidaParaTXT(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		MapaConvocacaoEnadeVO vetResultado = new MapaConvocacaoEnadeVO();
		while (tabelaResultado.next()) {
			montarDadosTxt(vetResultado, tabelaResultado, usuarioVO, true);
			if (tabelaResultado.getRow() == 0) {
				return vetResultado;
			}
		}
		return vetResultado;
	}
	private void montarDadosTxt(MapaConvocacaoEnadeVO obj, SqlRowSet dadosSQL, UsuarioVO usuario, Boolean arquivoTXT) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
//		obj.setIngressantes(dadosSQL.getBoolean("ingressantes"));
//		obj.setConcluintes(dadosSQL.getBoolean("concluintes"));
		obj.getEnadeCursoVO().setCodigo(dadosSQL.getInt("enadeCurso.codigo"));
		obj.getEnadeCursoVO().setPercentualCargaHorariaIngressante(dadosSQL.getInt("enadeCurso.PercentualCargaHorariaIngressante"));
		obj.getEnadeCursoVO().setPercentualCargaHorariaConcluinte(dadosSQL.getInt("enadeCurso.PercentualCargaHorariaConcluinte"));
		obj.getEnadeCursoVO().setAnoBaseIngressante(dadosSQL.getString("enadeCurso.anobaseingressante"));
		obj.getEnadeCursoVO().setPeriodoPrevistoTerminoIngressante(dadosSQL.getDate("enadeCurso.periodoPrevistoTerminoIngressante"));
		obj.getEnadeCursoVO().setPeriodoPrevistoTerminoConcluinte(dadosSQL.getDate("enadeCurso.periodoPrevistoTerminoConcluinte"));
		obj.getEnadeCursoVO().getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getEnadeCursoVO().getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.getEnadeCursoVO().getEnadeVO().setCodigo(dadosSQL.getInt("enade.codigo"));
		obj.getEnadeCursoVO().getEnadeVO().setTituloEnade(dadosSQL.getString("enade.tituloEnade"));
		obj.getEnadeCursoVO().getEnadeVO().setDataPortaria(dadosSQL.getDate("enade.dataPortaria"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
		obj.setDataAbertura(dadosSQL.getDate("dataAbertura"));
		obj.setDataFechamento(dadosSQL.getDate("dataFechamento"));
		obj.setDataPrevisaoConclusao(dadosSQL.getDate("dataPrevisaoConclusao"));
		obj.getEnadeCursoVO().getEnadeVO().setDataProva(dadosSQL.getDate("dataprova"));
		obj.getEnadeCursoVO().getCursoVO().setIdCursoInep(dadosSQL.getInt("curso.idcursoinep"));
		obj.setSituacaoMapaConvocacaoEnade(SituacaoMapaConvocacaoEnadeEnum.valueOf(dadosSQL.getString("situacaoMapaConvocacaoEnade")));
		obj.getArquivoAlunoIngressante().setCodigo(dadosSQL.getInt("mapaconvocacaoenade.arquivoalunoingressante"));
		if(Uteis.isAtributoPreenchido(obj.getArquivoAlunoIngressante())){
		obj.getArquivoAlunoIngressante().setNome(dadosSQL.getString("arquivoingressante"));
		}
		obj.getArquivoAlunoConcluinte().setCodigo(dadosSQL.getInt("mapaconvocacaoenade.arquivoalunoconcluinte"));
		if(Uteis.isAtributoPreenchido(obj.getArquivoAlunoConcluinte())){
			obj.getArquivoAlunoConcluinte().setNome(dadosSQL.getString("arquivoconcluinte"));
			}
		obj.setNovoObj(Boolean.FALSE);
	}
	
	public List<MapaConvocacaoEnadeVO> consultarPorCodigoEnade(String campoConsulta, Integer valorConsulta,  UsuarioVO usuarioVO, NivelMontarDados nivelMontarDados) throws Exception {
		List<MapaConvocacaoEnadeVO> listaMapaConvocacaoEnadeVO = new ArrayList<MapaConvocacaoEnadeVO>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select enade.codigo as codigo , enadeCurso.codigo as \"enadeCurso.codigo\", enadeCurso.PercentualCargaHorariaIngressante as \"enadeCurso.PercentualCargaHorariaIngressante\", ");
		sb.append(" enadeCurso.PercentualCargaHorariaConcluinte as \"enadeCurso.PercentualCargaHorariaConcluinte\", enadeCurso.anobaseingressante as \"enadeCurso.anobaseingressante\",    ");
		sb.append(" enadeCurso.periodoPrevistoTerminoIngressante as \"enadeCurso.periodoPrevistoTerminoIngressante\",  ");
		sb.append(" enadeCurso.periodoPrevistoTerminoConcluinte as \"enadeCurso.periodoPrevistoTerminoConcluinte\", curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\",  ");
		sb.append(" enade.codigo as \"enade.codigo\", enade.tituloEnade as \"enade.tituloEnade\", enade.dataPortaria as \"enade.dataPortaria\", dataAbertura, dataFechamento, dataPrevisaoConclusao, dataprova, curso.idcursoinep as \"curso.idcursoinep\",  ");
		sb.append(" situacaoMapaConvocacaoEnade, mapaconvocacaoenade.arquivoalunoingressante as \"mapaconvocacaoenade.arquivoalunoingressante\",  mapaconvocacaoenade.arquivoalunoconcluinte as \"mapaconvocacaoenade.arquivoalunoconcluinte\",  arquivoigressante.nome as \"arquivoigressante.nome\", arquivoconcluinte.nome as \"arquivoconcluinte.nome\"  ");
		sb.append(" from enade  ");
		sb.append(" inner join enadecurso on enadecurso.enade = enade.codigo  ");
		sb.append(" inner join mapaconvocacaoenade on mapaconvocacaoenade.enadecurso = enadecurso.codigo  ");
		sb.append(" inner join curso on curso.codigo = enadecurso.curso  ");
		sb.append(" left join arquivo arquivoigressante on arquivoigressante.codigo = mapaconvocacaoenade.arquivoalunoingressante  ");
		sb.append(" left join arquivo arquivoconcluinte on arquivoconcluinte.codigo = mapaconvocacaoenade.arquivoalunoconcluinte  ");
		sb.append(" where enade.codigo = ").append(valorConsulta);
		sb.append(" and mapaconvocacaoenade.codigo is not null and (arquivoalunoconcluinte is not null and arquivoalunoingressante is not null) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			listaMapaConvocacaoEnadeVO.add(montarDadosParaTxtEnade(tabelaResultado, usuarioVO));
		}
		return listaMapaConvocacaoEnadeVO;
	}
	
	public MapaConvocacaoEnadeVO montarDadosParaTxtEnade(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		MapaConvocacaoEnadeVO vetResultado = new MapaConvocacaoEnadeVO();
			montarDados(vetResultado, tabelaResultado, usuarioVO, true);
		return vetResultado;
	}
	
	private void montarDados(MapaConvocacaoEnadeVO obj, SqlRowSet dadosSQL, UsuarioVO usuario, Boolean arquivoTXT) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getEnadeCursoVO().setCodigo(dadosSQL.getInt("enadeCurso.codigo"));
		obj.getEnadeCursoVO().setPercentualCargaHorariaIngressante(dadosSQL.getInt("enadeCurso.PercentualCargaHorariaIngressante"));
		obj.getEnadeCursoVO().setPercentualCargaHorariaConcluinte(dadosSQL.getInt("enadeCurso.PercentualCargaHorariaConcluinte"));
		obj.getEnadeCursoVO().setAnoBaseIngressante(dadosSQL.getString("enadeCurso.anobaseingressante"));
		obj.getEnadeCursoVO().setPeriodoPrevistoTerminoIngressante(dadosSQL.getDate("enadeCurso.periodoPrevistoTerminoIngressante"));
		obj.getEnadeCursoVO().setPeriodoPrevistoTerminoConcluinte(dadosSQL.getDate("enadeCurso.periodoPrevistoTerminoConcluinte"));
		obj.getEnadeCursoVO().getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getEnadeCursoVO().getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.getEnadeCursoVO().getEnadeVO().setCodigo(dadosSQL.getInt("enade.codigo"));
		obj.getEnadeCursoVO().getEnadeVO().setTituloEnade(dadosSQL.getString("enade.tituloEnade"));
		obj.getEnadeCursoVO().getEnadeVO().setDataPortaria(dadosSQL.getDate("enade.dataPortaria"));
		obj.setDataAbertura(dadosSQL.getDate("dataAbertura"));
		obj.setDataFechamento(dadosSQL.getDate("dataFechamento"));
		obj.setDataPrevisaoConclusao(dadosSQL.getDate("dataPrevisaoConclusao"));
		obj.getEnadeCursoVO().getEnadeVO().setDataProva(dadosSQL.getDate("dataprova"));
		obj.getEnadeCursoVO().getCursoVO().setIdCursoInep(dadosSQL.getInt("curso.idcursoinep"));
		obj.setSituacaoMapaConvocacaoEnade(SituacaoMapaConvocacaoEnadeEnum.valueOf(dadosSQL.getString("situacaoMapaConvocacaoEnade")));
		obj.getArquivoAlunoIngressante().setCodigo(dadosSQL.getInt("mapaconvocacaoenade.arquivoalunoingressante"));
		obj.getArquivoAlunoConcluinte().setCodigo(dadosSQL.getInt("mapaconvocacaoenade.arquivoalunoconcluinte"));
		obj.getEnadeCursoVO().getEnadeVO().setNomeArquivoConcluinte(dadosSQL.getString("arquivoconcluinte.nome"));
		obj.getEnadeCursoVO().getEnadeVO().setNomeArquivoIngressante(dadosSQL.getString("arquivoigressante.nome"));
		obj.setNovoObj(Boolean.FALSE);
	}
}