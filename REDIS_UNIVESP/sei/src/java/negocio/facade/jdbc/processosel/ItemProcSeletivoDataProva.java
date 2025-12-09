package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoGabaritoDataVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ProcessoSeletivoProvaDataVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.financeiro.ProvisaoCusto;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.processosel.ItemProcSeletivoDataProvaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ItemProcSeletivoDataProvaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ItemProcSeletivoDataProvaVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see ItemProcSeletivoDataProvaVO
 * @see ControleAcesso
 * @see ProvisaoCusto
 */
@Repository
@Scope("singleton")
@Lazy
public class ItemProcSeletivoDataProva extends ControleAcesso implements ItemProcSeletivoDataProvaInterfaceFacade {

	protected static String idEntidade;

	public ItemProcSeletivoDataProva() throws Exception {
		super();
		setIdEntidade("ProcSeletivo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ItemProcSeletivoDataProvaVO</code>.
	 */
	public ItemProcSeletivoDataProvaVO novo() throws Exception {
		ItemProcSeletivoDataProva.incluir(getIdEntidade());
		ItemProcSeletivoDataProvaVO obj = new ItemProcSeletivoDataProvaVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ItemProcSeletivoDataProvaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemProcSeletivoDataProvaVO</code> que
	 *            será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ItemProcSeletivoDataProvaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ItemProcSeletivoDataProvaVO.validarDados(obj);
			final String sql = "INSERT INTO ItemProcSeletivoDataProva( dataProva, procSeletivo, hora, tipoProvaGabarito, dataInicioInscricao, dataTerminoInscricao, dataLiberacaoResultado, dataLimiteAdiarVencimentoInscricao, dataLimiteApresentarDadosVisaoCandidato) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataProva()));
					sqlInserir.setInt(2, obj.getProcSeletivo());
					sqlInserir.setString(3, obj.getHora());
					sqlInserir.setString(4, obj.getTipoProvaGabarito());
					sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataInicioInscricao()));
					sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataTerminoInscricao()));
					sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataLiberacaoResultado()));
					sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataLimiteAdiarVencimentoInscricao()));
					sqlInserir.setDate(9, Uteis.getDataJDBC(obj.getDataLimiteApresentarDadosVisaoCandidato()));
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
			getFacadeFactory().getProcessoSeletivoProvaDataFacade().incluirProcessoSeletivoProvaData(obj, usuarioVO);
			getFacadeFactory().getProcSeletivoGabaritoDataFacade().incluirProcessoSeletivoGabaritoData(obj, usuarioVO);
			obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ItemProcSeletivoDataProvaVO</code>. Sempre utiliza a chave primária
	 * da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemProcSeletivoDataProvaVO</code> que
	 *            será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemProcSeletivoDataProvaVO obj, UsuarioVO usuarioVO) throws Exception {
		ItemProcSeletivoDataProvaVO.validarDados(obj);
		final String sql = "UPDATE ItemProcSeletivoDataProva set dataProva=?, procSeletivo=?, hora=?, tipoProvaGabarito=?, dataInicioInscricao=?, dataTerminoInscricao=?, dataLiberacaoResultado=?, dataLimiteAdiarVencimentoInscricao=? , motivoalteracaodataprova = ?, dataLimiteApresentarDadosVisaoCandidato = ? WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataProva()));
				sqlAlterar.setInt(2, obj.getProcSeletivo());
				sqlAlterar.setString(3, obj.getHora());
				sqlAlterar.setString(4, obj.getTipoProvaGabarito());
				sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataInicioInscricao()));
				sqlAlterar.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getDataTerminoInscricao()));
				sqlAlterar.setTimestamp(7, Uteis.getDataJDBCTimestamp(obj.getDataLiberacaoResultado()));
				sqlAlterar.setDate(8, Uteis.getDataJDBC(obj.getDataLimiteAdiarVencimentoInscricao()));
				sqlAlterar.setString(9, obj.getMotivoAlteracaoDataProva());
				sqlAlterar.setDate(10, Uteis.getDataJDBC(obj.getDataLimiteApresentarDadosVisaoCandidato()));
				sqlAlterar.setInt(11, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		})==0){
			incluir(obj, usuarioVO);
			return;
		}
		getFacadeFactory().getProcessoSeletivoProvaDataFacade().alteraProcessoSeletivoProvaData(obj, usuarioVO);
		getFacadeFactory().getProcSeletivoGabaritoDataFacade().alteraProcessoSeletivoGabaritoData(obj, usuarioVO);

	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ItemProcSeletivoDataProvaVO</code>. Sempre localiza o registro a
	 * ser excluído através da chave primária da entidade. Primeiramente
	 * verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemProcSeletivoDataProvaVO</code> que
	 *            será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ItemProcSeletivoDataProvaVO obj, UsuarioVO usuarioVO) throws Exception {
		ItemProcSeletivoDataProva.excluir(getIdEntidade());
		String sql = "DELETE FROM ItemProcSeletivoDataProva WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	/**
	 * Responsável por realizar uma consulta de <code>ItensProvisao</code>
	 * através do valor do atributo <code>codigo</code> da classe
	 * <code>ProvisaoCusto</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ItemProcSeletivoDataProvaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoProcSeletivo(Integer valorConsulta, int nivelMontarDados, boolean controlarAcesso , UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ItemProcSeletivoDataProva.* FROM ItemProcSeletivoDataProva, ProcSeletivo WHERE ItemProcSeletivoDataProva.procSeletivo = ProcSeletivo.codigo and ProcSeletivo.codigo >= " + valorConsulta.intValue() + " ORDER BY ProcSeletivo.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados);
	}

	public List<ItemProcSeletivoDataProvaVO> consultarPorCodigoProcessoSeletivo(Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("SELECT ItemProcSeletivoDataProva.* FROM itemProcSeletivoDataProva ");
		sb.append("INNER JOIN procSeletivo ON procSeletivo.codigo = itemProcSeletivoDataProva.procSeletivo ");
		sb.append("WHERE procSeletivo.codigo = ").append(procSeletivo.intValue()).append(" ORDER BY itemProcSeletivoDataProva.dataProva, itemProcSeletivoDataProva.hora");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados);
	}
	
	@Override
	public List<ItemProcSeletivoDataProvaVO> consultarPorCodigoProcessoSeletivoProvaJaRealizada(Integer procSeletivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("SELECT ItemProcSeletivoDataProva.* FROM itemProcSeletivoDataProva ");
		sb.append("INNER JOIN procSeletivo ON procSeletivo.codigo = itemProcSeletivoDataProva.procSeletivo ");
		sb.append("WHERE itemProcSeletivoDataProva.dataProva::DATE <= current_date and procSeletivo.codigo = ").append(procSeletivo.intValue()).append(" ORDER BY itemProcSeletivoDataProva.dataProva, itemProcSeletivoDataProva.hora");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ItensProvisao</code>
	 * através do valor do atributo <code>String nrDocumento</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ItemProcSeletivoDataProvaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataProva(Date valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ItemProcSeletivoDataProva WHERE dataProva = " + Uteis.getDataJDBC(valorConsulta) + " ORDER BY dataProva";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ItensProvisao</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ItemProcSeletivoDataProvaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ItemProcSeletivoDataProva WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	@Override
	public List<ItemProcSeletivoDataProvaVO> consultarPorProcSelectivoAptoInscricao(Integer valorConsulta, boolean visaoCandidato, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("select itemprocseletivodataprova.* from itemprocseletivodataprova ");
		sql.append(" inner join procSeletivo on  itemprocseletivodataprova.procSeletivo = procSeletivo.codigo"); 
		sql.append(" where procseletivo.codigo = ").append(valorConsulta); 
		sql.append(" and itemprocseletivodataprova.dataprova >= current_timestamp ");
		
		sql.append(" and ((itemprocseletivodataprova.datainicioinscricao <= current_timestamp ");
		sql.append(" and itemprocseletivodataprova.dataterminoinscricao >= current_timestamp) ");
		
		if(!visaoCandidato){
			sql.append(" or (procseletivo.datainicio <= current_timestamp ");
			sql.append(" and procseletivo.datafim >= current_timestamp) ");
		}
		sql.append(") order by dataProva ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	public List<ItemProcSeletivoDataProvaVO> consultarPorProcSelectivo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("select itemprocseletivodataprova.* from itemprocseletivodataprova ");
		sql.append(" inner join procSeletivo on  itemprocseletivodataprova.procSeletivo = procSeletivo.codigo"); 
		sql.append(" where procseletivo.codigo = ").append(valorConsulta); 
		sql.append(" order by dataProva ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	
	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ItemProcSeletivoDataProvaVO</code> resultantes da consulta.
	 */
	public static List<ItemProcSeletivoDataProvaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<ItemProcSeletivoDataProvaVO> vetResultado = new ArrayList<ItemProcSeletivoDataProvaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ItemProcSeletivoDataProvaVO</code>.
	 * 
	 * @return O objeto da classe <code>ItemProcSeletivoDataProvaVO</code> com
	 *         os dados devidamente montados.
	 */
	public static ItemProcSeletivoDataProvaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		ItemProcSeletivoDataProvaVO obj = new ItemProcSeletivoDataProvaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDataProva(dadosSQL.getDate("dataProva"));
		obj.setDataInicioInscricao(dadosSQL.getDate("dataInicioInscricao"));
		obj.setDataTerminoInscricao(dadosSQL.getDate("dataTerminoInscricao"));
		obj.setDataLiberacaoResultado(dadosSQL.getTimestamp("dataLiberacaoResultado"));
		obj.setDataLimiteAdiarVencimentoInscricao(dadosSQL.getDate("dataLimiteAdiarVencimentoInscricao"));
		obj.setProcSeletivo(new Integer(dadosSQL.getInt("procSeletivo")));
		obj.setHora(dadosSQL.getString("hora"));
		obj.setTipoProvaGabarito(dadosSQL.getString("tipoProvaGabarito"));
		obj.setMotivoAlteracaoDataProva(dadosSQL.getString("motivoalteracaodataprova"));
		obj.setDataLimiteApresentarDadosVisaoCandidato(dadosSQL.getDate("dataLimiteApresentarDadosVisaoCandidato"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setProcessoSeletivoProvaDataVOs(getFacadeFactory().getProcessoSeletivoProvaDataFacade().consultarPorItemProcSeletivoDataProva(obj.getCodigo()));
		obj.setProcSeletivoGabaritoDataVOs(getFacadeFactory().getProcSeletivoGabaritoDataFacade().consultarPorItemProcSeletivoDataProva(obj.getCodigo()));
		return obj;
	}

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>ItemProcSeletivoDataProvaVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe <code>ItensProvisao</code>.
	 * 
	 * @param <code>provisaoCusto</code> campo chave para exclusão dos objetos
	 *        no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirItemProcSeletivoDataProva(Integer procSeletivo, UsuarioVO usuarioVO) throws Exception {
		ItemProcSeletivoDataProva.excluir(getIdEntidade());
		String sql = "DELETE FROM ItemProcSeletivoDataProva WHERE (procSeletivo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, new Object[] { procSeletivo });
	}

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>ItemProcSeletivoDataProvaVO</code> contidos em um Hashtable no BD.
	 * Faz uso da operação <code>excluirItensProvisaos</code> e
	 * <code>incluirItensProvisaos</code> disponíveis na classe
	 * <code>ItensProvisao</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarItemProcSeletivoDataProva(Integer procSeletivo, List objetos, UsuarioVO usuarioVO) throws Exception {
		String str = "DELETE FROM ItemProcSeletivoDataProva WHERE procSeletivo = " + procSeletivo;
		Iterator i = objetos.iterator();
		while (i.hasNext()) {
			ItemProcSeletivoDataProvaVO objeto = (ItemProcSeletivoDataProvaVO) i.next();
			str += " AND codigo <> " + objeto.getCodigo().intValue();
		}
		str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(str);
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ItemProcSeletivoDataProvaVO objeto = (ItemProcSeletivoDataProvaVO) e.next();
			
			if (objeto.getTipoProvaGabarito().equals("PR") && !objeto.getProcSeletivoGabaritoDataVOs().isEmpty()) {
				objeto.getProcSeletivoGabaritoDataVOs().clear();
			} else if (objeto.getTipoProvaGabarito().equals("GA") && !objeto.getProcessoSeletivoProvaDataVOs().isEmpty()) {
				objeto.getProcessoSeletivoProvaDataVOs().clear();
			}
			
			if (objeto.getCodigo().equals(0)) {
				objeto.setProcSeletivo(procSeletivo);
				incluir(objeto, usuarioVO);
			} else {
				alterar(objeto, usuarioVO);
				Date dataProvaSemAlterar = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarDataProvaPorCodigo(objeto.getCodigo());				
				if (!UteisData.getDataComMinutos(dataProvaSemAlterar).equals(UteisData.getDataComMinutos(objeto.getDataProva()))) {
					getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.ITEM_PROCESSO_SELETIVO_DATA_PROVA, objeto.getCodigo().toString(), OperacaoFuncionalidadeEnum.ALTERAR_DATA_PROVA_ITEM_PROCESSO_SELETIVO, usuarioVO, "Data Prova Anterior:" + UteisData.getDataComHora(dataProvaSemAlterar)));
				}
				dataProvaSemAlterar = null;
			}
		}
	}

	/**
	 * Operação responsável por incluir objetos da
	 * <code>ItemProcSeletivoDataProvaVO</code> no BD. Garantindo o
	 * relacionamento com a entidade principal
	 * <code>financeiro.ProvisaoCusto</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirItemProcSeletivoDataProva(Integer procSeletivo, List<ItemProcSeletivoDataProvaVO> objetos, UsuarioVO usuarioVO) throws Exception {
		try {
			for (ItemProcSeletivoDataProvaVO obj : objetos) {
				if (obj.getTipoProvaGabarito().equals("PR") && !obj.getProcSeletivoGabaritoDataVOs().isEmpty()) {
					obj.getProcSeletivoGabaritoDataVOs().clear();
				} else if (obj.getTipoProvaGabarito().equals("GA") && !obj.getProcessoSeletivoProvaDataVOs().isEmpty()) {
					obj.getProcessoSeletivoProvaDataVOs().clear();
				}
				obj.setProcSeletivo(procSeletivo);
				incluir(obj, usuarioVO);
			}
		} catch (Exception e) {
			for (ItemProcSeletivoDataProvaVO obj : objetos) {
				obj.setNovoObj(true);
				obj.setCodigo(0);
			}
			throw e;
		}
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>ItemProcSeletivoDataProvaVO</code> relacionados a um objeto da
	 * classe <code>financeiro.ProvisaoCusto</code>.
	 * 
	 * @param provisaoCusto
	 *            Atributo de <code>financeiro.ProvisaoCusto</code> a ser
	 *            utilizado para localizar os objetos da classe
	 *            <code>ItemProcSeletivoDataProvaVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>ItemProcSeletivoDataProvaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public static List consultarItemProcSeletivoDataProva(Integer procSeletivo, int nivelMontarDados) throws Exception {
		ItemProcSeletivoDataProva.consultar(getIdEntidade());
		List objetos = new ArrayList();
		String sql = "SELECT * FROM ItemProcSeletivoDataProva WHERE procSeletivo = ? order by dataProva";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { procSeletivo });
		while (resultado.next()) {
			ItemProcSeletivoDataProvaVO novoObj = new ItemProcSeletivoDataProvaVO();
			novoObj = ItemProcSeletivoDataProva.montarDados(resultado, nivelMontarDados);
			objetos.add(novoObj);
		}
		return objetos;
	}

	@Override
	public void adicionarProcessoSeletivoGabaritoDataVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, ProcSeletivoGabaritoDataVO procSeletivoGabaritoVO, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getProcessoSeletivoProvaDataFacade().validarDadosGabaritoData(procSeletivoGabaritoVO);
		procSeletivoGabaritoVO.setGabaritoVO(getFacadeFactory().getGabaritoFacade().consultaRapidaPorChavePrimaria(procSeletivoGabaritoVO.getGabaritoVO().getCodigo(), usuarioVO));
		if (procSeletivoGabaritoVO.getDisciplinaIdioma().getCodigo() != null && procSeletivoGabaritoVO.getDisciplinaIdioma().getCodigo() > 0) {
			procSeletivoGabaritoVO.setDisciplinaIdioma(getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(procSeletivoGabaritoVO.getDisciplinaIdioma().getCodigo(), null));
		}
		for (ProcSeletivoGabaritoDataVO objExist : itemProcSeletivoDataProvaVO.getProcSeletivoGabaritoDataVOs()) {
			if (objExist.getDisciplinaIdioma().getCodigo().equals(procSeletivoGabaritoVO.getDisciplinaIdioma().getCodigo()) 
					&& objExist.getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo().equals(procSeletivoGabaritoVO.getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo())) {
				objExist.setGabaritoVO(procSeletivoGabaritoVO.getGabaritoVO());
				return;
			}
		}

		itemProcSeletivoDataProvaVO.getProcSeletivoGabaritoDataVOs().add(procSeletivoGabaritoVO);

	}

	@Override
	public void removerProcessoSeletivoGabaritoDataVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, ProcSeletivoGabaritoDataVO procSeletivoGabaritoDataVO) throws Exception {
		int x = 0;
		for (ProcSeletivoGabaritoDataVO objExist : itemProcSeletivoDataProvaVO.getProcSeletivoGabaritoDataVOs()) {
			if (objExist.getDisciplinaIdioma().getCodigo().equals(procSeletivoGabaritoDataVO.getDisciplinaIdioma().getCodigo())
					&& objExist.getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo().equals(procSeletivoGabaritoDataVO.getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo())) {
				itemProcSeletivoDataProvaVO.getProcSeletivoGabaritoDataVOs().remove(x);
				return;
			}
			x++;
		}
		itemProcSeletivoDataProvaVO.getProcSeletivoGabaritoDataVOs().add(procSeletivoGabaritoDataVO);
	}

	@Override
	public void adicionarProcessoSeletivoProvaDataVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO) throws Exception {
		getFacadeFactory().getProcessoSeletivoProvaDataFacade().validarDados(processoSeletivoProvaDataVO);
		processoSeletivoProvaDataVO.setProvaProcessoSeletivo(getFacadeFactory().getProvaProcessoSeletivoFacade().consultarPorChavePrimaria(processoSeletivoProvaDataVO.getProvaProcessoSeletivo().getCodigo(), NivelMontarDados.BASICO));
		if (processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo() != null && processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo() > 0) {
			processoSeletivoProvaDataVO.setDisciplinaIdioma(getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo(), null));
		}
		for (ProcessoSeletivoProvaDataVO objExist : itemProcSeletivoDataProvaVO.getProcessoSeletivoProvaDataVOs()) {
			if (objExist.getDisciplinaIdioma().getCodigo().equals(processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo())
					&& objExist.getProvaProcessoSeletivo().getGrupoDisciplinaProcSeletivoVO().getCodigo().equals(processoSeletivoProvaDataVO.getProvaProcessoSeletivo().getGrupoDisciplinaProcSeletivoVO().getCodigo())) {
				objExist.setProvaProcessoSeletivo(processoSeletivoProvaDataVO.getProvaProcessoSeletivo());
				return;
			}
		}

		itemProcSeletivoDataProvaVO.getProcessoSeletivoProvaDataVOs().add(processoSeletivoProvaDataVO);

	}

	@Override
	public void removerProcessoSeletivoProvaDataVO(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, ProcessoSeletivoProvaDataVO processoSeletivoProvaDataVO) throws Exception {
		int x = 0;
		for (ProcessoSeletivoProvaDataVO objExist : itemProcSeletivoDataProvaVO.getProcessoSeletivoProvaDataVOs()) {
			if (objExist.getDisciplinaIdioma().getCodigo().equals(processoSeletivoProvaDataVO.getDisciplinaIdioma().getCodigo())
					&& objExist.getProvaProcessoSeletivo().getGrupoDisciplinaProcSeletivoVO().getCodigo().equals(processoSeletivoProvaDataVO.getProvaProcessoSeletivo().getGrupoDisciplinaProcSeletivoVO().getCodigo())) {
				itemProcSeletivoDataProvaVO.getProcessoSeletivoProvaDataVOs().remove(x);
				return;
			}
			x++;
		}
		itemProcSeletivoDataProvaVO.getProcessoSeletivoProvaDataVOs().add(processoSeletivoProvaDataVO);
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ItemProcSeletivoDataProvaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ItemProcSeletivoDataProvaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ItemProcSeletivoDataProva WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ItemProcSeletivoDataProva ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ItemProcSeletivoDataProva.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ItemProcSeletivoDataProva.idEntidade = idEntidade;
	}
	
	public String consultarTipoProvaGabaritoPorInscricao(Integer inscricao, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct itemprocseletivodataprova.tipoprovagabarito from procseletivo  ");
		sb.append(" inner join inscricao on inscricao.procseletivo = procseletivo.codigo  ");
		sb.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.procseletivo = procseletivo.codigo and itemprocseletivodataprova.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sb.append(" where inscricao.codigo = ").append(inscricao);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("tipoprovagabarito");
		}
		return "GA";
	}
        
	private boolean verificarDataValidaParaAplicacaoProva(Date dataProvaValidar, ItemProcSeletivoDataProvaVO itemDataProvaInicial, UsuarioVO usuario) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataProvaValidar);
		if (itemDataProvaInicial.getIgnorarDomingosComoDataPossivelParaProva()) {
			if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
				return false;
			}
		}
		if (itemDataProvaInicial.getIgnorarSabadosComoDataPossivelParaProva()) {
			if (cal.get(Calendar.DAY_OF_WEEK) == 7) {
				return false;
			}
		}
		if (itemDataProvaInicial.getIgnorarFeriadosComoDataPossivelParaProva()) {
			boolean feriadoNaData = getFacadeFactory().getFeriadoFacade().verificarFeriadoNesteDia(dataProvaValidar, 0,	ConsiderarFeriadoEnum.ACADEMICO, false, usuario);
			if (feriadoNaData) {
				return false;
			}
		}
		return true;
	}
        
    public Date replicarHoraDataOrigemDataDestino(Date dataOrigemHoraMinuto, Date dataDestinoHoraMinuto) throws Exception {
        Calendar calOrigem = Calendar.getInstance();
        calOrigem.setTime(dataOrigemHoraMinuto);
        int horaMigrar = calOrigem.get(Calendar.HOUR_OF_DAY);
        int minutoMigrar = calOrigem.get(Calendar.MINUTE);
        int segundosMigrar = calOrigem.get(Calendar.SECOND);

        Calendar calDestino = Calendar.getInstance();
        calDestino.setTime(dataDestinoHoraMinuto);
        calDestino.set(Calendar.HOUR_OF_DAY, horaMigrar);
        calDestino.set(Calendar.MINUTE, minutoMigrar);
        calDestino.set(Calendar.SECOND, segundosMigrar);
        return calDestino.getTime();
    }

        /**
         * Método responsável por adicionar um conjunto de datas de provas para o processo seletivo.
         * O mesmo utiliza as datas e horarios informandos no objeto ItemProcSeletivoDataProvaVO
         * e replica o mesmo até uma determinada data futura, também informada neste mesmo objeto.
         * Este método gera datas futuras para até 365 dias.
         */
        public void adicionarObjItemProcSeletivoDataProvaVOsAteDataFutura(ProcSeletivoVO procSeletivo, 
                ItemProcSeletivoDataProvaVO itemDataProvaInicial, Boolean objReferenteGabarito, UsuarioVO usuario) throws Exception {
            Date dataFuturoReplicar = itemDataProvaInicial.getDataFuturaReplicarDatasEHorarios();
            if (itemDataProvaInicial.getDataProva().compareTo(dataFuturoReplicar) >= 0) {
                throw new Exception("A DATA FUTURA (" + Uteis.getData(dataFuturoReplicar) + ") informada para replicar as datas/horários deve ser maior que a DATA DA PROVA INICIAL (" + Uteis.getData(itemDataProvaInicial.getDataProva()) + ") informada.");
            }
            Date dataFuturoValida = Uteis.getDataFutura(itemDataProvaInicial.getDataProva(), Calendar.DAY_OF_MONTH, 365);
            if (dataFuturoReplicar.compareTo(dataFuturoValida) >= 0) {
                // verificação para evitar que a aplicação trave, em função de uma data futura informada de forma errada
                throw new Exception("A DATA FUTURA (" + Uteis.getData(dataFuturoReplicar) + ") informada não pode avançar mais do que 365 dias com relação a DATA DA PROVA INICIAL (" + Uteis.getData(itemDataProvaInicial.getDataProva()) + ") .");
            }
            if (!verificarDataValidaParaAplicacaoProva(itemDataProvaInicial.getDataProva(), itemDataProvaInicial, usuario)) {
            	throw new Exception("A DATA DA PROVA é inválida, pois é sábado, domingo ou feriado.");
            }
            
            // diferenca entre data Termino Inscricao e data Inicio Inscricao
            int nrDiasPadraoResultadoAposDataProva = (int) Uteis.nrDiasEntreDatasDesconsiderandoHoras(itemDataProvaInicial.getDataLiberacaoResultado(), itemDataProvaInicial.getDataProva());
            int nrDiasPadraoInicioInscricaoAntesDataProva = (int) Uteis.nrDiasEntreDatasDesconsiderandoHoras(itemDataProvaInicial.getDataProva(), itemDataProvaInicial.getDataInicioInscricao());
            int nrDiasPadraoTerminoInscricaoAntesDataProva = (int) Uteis.nrDiasEntreDatasDesconsiderandoHoras(itemDataProvaInicial.getDataProva(), itemDataProvaInicial.getDataTerminoInscricao());
            int nrDiasPadraoDataLimiteAdiarVencimento = (int) Uteis.nrDiasEntreDatasDesconsiderandoHoras(itemDataProvaInicial.getDataProva(), itemDataProvaInicial.getDataLimiteAdiarVencimentoInscricao());
            int nrDiasPadraoApresentacaoResultadoFinalParaCandidato = (int) Uteis.nrDiasEntreDatasDesconsiderandoHoras(itemDataProvaInicial.getDataProva(), itemDataProvaInicial.getDataLimiteApresentarDadosVisaoCandidato());

            // adicionando a data inicial - que é o padrão de início
            if (objReferenteGabarito) {
                procSeletivo.adicionarObjItemProcSeletivoDataGabaritoVOs(itemDataProvaInicial);
            } else {
                procSeletivo.adicionarObjItemProcSeletivoDataProvaVOs(itemDataProvaInicial);
            }
            
            // inicializando a data inicial, pois a mesma precisa ser adicionada.
            ItemProcSeletivoDataProvaVO utilmoItemDataProvaAdicionado = itemDataProvaInicial;
            int contadorDatasGeradas = 0;
            while (contadorDatasGeradas <= 365) {
                // --------------------------------------------
                // Gerando a próxima data de prova / inscricao
                // --------------------------------------------
                ItemProcSeletivoDataProvaVO novoItemGerarAPartirAnterior = new ItemProcSeletivoDataProvaVO();
                novoItemGerarAPartirAnterior.setTipoProvaGabarito(itemDataProvaInicial.getTipoProvaGabarito());
                novoItemGerarAPartirAnterior.setProcSeletivo(itemDataProvaInicial.getProcSeletivo());
                
                // HORA PROVA - a hora sempre será a mesma
                novoItemGerarAPartirAnterior.setHora(itemDataProvaInicial.getHora());
                
                // DATA PROVA - a proxima data da prova, irá depender de variáveis como 
                // se é para ignorar feriados, sabados e domingos.
                Date proximaDataProva = Uteis.getDataFutura(utilmoItemDataProvaAdicionado.getDataProva(), Calendar.DAY_OF_MONTH, 1);
                boolean dataValida = verificarDataValidaParaAplicacaoProva(proximaDataProva, itemDataProvaInicial, usuario);
                int controleNrDiasAvancou = 1;
                while ((!dataValida) && (controleNrDiasAvancou <= 30)) {
                    proximaDataProva = Uteis.getDataFutura(proximaDataProva, Calendar.DAY_OF_MONTH, 1);
                    dataValida = verificarDataValidaParaAplicacaoProva(proximaDataProva, itemDataProvaInicial, usuario);
                    controleNrDiasAvancou++;
                }
                if (controleNrDiasAvancou >= 30) {
                    throw new Exception("Ocorreu um erro ao tentar determinar uma próxima data de realização de prova. Verifique a disponibilidade de datas válidas.");
                }
                novoItemGerarAPartirAnterior.setDataProva(proximaDataProva);

                // DATA INICIO INSCRICAO - uma vez que temos a data da prova, iremos manter o início da inscrição
                // com o mesmo número de dias antes que a data prova do padrão inicial fornecido pelo usuário.
                // Caso a data obtida, esteja no final de semana ou feriada ele irá retroagir para incorporar estes
                // dias em que a IE não trabalha como uma data possível para que o aluno já possa se increver.
                Date proximaDataInicioInscricao  = Uteis.getDataFutura(proximaDataProva, Calendar.DAY_OF_MONTH, (nrDiasPadraoInicioInscricaoAntesDataProva * -1));
                boolean dataInicioInscricaoNoFinalSemanaOuFeriado = (!verificarDataValidaParaAplicacaoProva(proximaDataInicioInscricao, itemDataProvaInicial, usuario));
                int controleNrDiasRecuou = 1;
                while ((dataInicioInscricaoNoFinalSemanaOuFeriado) && (controleNrDiasRecuou <= 30)) {
                    // caso a data prevista para inicio da inscricao seja no domingo, sabado ou feriado,
                    // entao o sistema irá voltar a data inicio em um dia e obter uma data anterior.
                    // Assim, o aluno poderá se inscrever no final de semana por exemplo, para uma prvoa
                    // que foi programada para segunda-feira.
                    proximaDataInicioInscricao  = Uteis.getDataFutura(proximaDataInicioInscricao, Calendar.DAY_OF_MONTH, -1);
                    dataInicioInscricaoNoFinalSemanaOuFeriado = (!verificarDataValidaParaAplicacaoProva(proximaDataInicioInscricao, itemDataProvaInicial, usuario));
                    controleNrDiasRecuou++;
                }
                proximaDataInicioInscricao = replicarHoraDataOrigemDataDestino(itemDataProvaInicial.getDataInicioInscricao(), proximaDataInicioInscricao);
                novoItemGerarAPartirAnterior.setDataInicioInscricao(proximaDataInicioInscricao);
                
                // DATA TERMINO INSCRICAO - a proxima data de termino de inscricao deverá estar com a mesma diferenca
                // de dias que havia das datas padrões iniciais fornecidas pelo usuário. Contudo, caso a mesma seja calculada
                // para um final de semana ou feriado, então a mesma será deslocada para o passado. Pois a IE precisa que o aluno
                // pague o boleto e tem tempo para processar o arquivo de retorno e garantir que o mesmo pagou.
                Date proximaDataTermino = Uteis.getDataFutura(proximaDataProva, Calendar.DAY_OF_MONTH, (nrDiasPadraoTerminoInscricaoAntesDataProva * -1));
                boolean dataTerminoInscricaoNoFinalSemanaOuFeriado = (!verificarDataValidaParaAplicacaoProva(proximaDataTermino, itemDataProvaInicial, usuario));
                controleNrDiasRecuou = 1;
                while ((dataTerminoInscricaoNoFinalSemanaOuFeriado) && (controleNrDiasRecuou <= 30)) {
                    // caso a data prevista para termino da inscricao seja no domingo, sabado ou feriado,
                    // entao o sistema irá voltar a data em um dia e obter uma data anterior.
                    // Assim, o aluno poderá pagar e haverá tempo para processar o arquivo de retorno,
                    // comprovando o pagamento da mesma.
                    proximaDataTermino  = Uteis.getDataFutura(proximaDataTermino, Calendar.DAY_OF_MONTH, -1);
                    dataTerminoInscricaoNoFinalSemanaOuFeriado = (!verificarDataValidaParaAplicacaoProva(proximaDataTermino, itemDataProvaInicial, usuario));
                    controleNrDiasRecuou++;
                }
                proximaDataTermino = replicarHoraDataOrigemDataDestino(itemDataProvaInicial.getDataTerminoInscricao(), proximaDataTermino);
                novoItemGerarAPartirAnterior.setDataTerminoInscricao(proximaDataTermino);
                
                // DATA LIMITE ADIAR VENCIMENTO - 
                Date proximaDataLimite = Uteis.getDataFutura(proximaDataProva, Calendar.DAY_OF_MONTH, (nrDiasPadraoDataLimiteAdiarVencimento * -1));
                boolean dataLimiteNoFinalSemanaOuFeriado = (!verificarDataValidaParaAplicacaoProva(proximaDataLimite, itemDataProvaInicial, usuario));
                controleNrDiasRecuou = 1;
                while ((dataLimiteNoFinalSemanaOuFeriado) && (controleNrDiasRecuou <= 30)) {
                	// caso a data prevista para termino da inscricao seja no domingo, sabado ou feriado,
                	// entao o sistema irá voltar a data em um dia e obter uma data anterior.
                	// Assim, o aluno poderá pagar e haverá tempo para processar o arquivo de retorno,
                	// comprovando o pagamento da mesma.
                	proximaDataLimite  = Uteis.getDataFutura(proximaDataLimite, Calendar.DAY_OF_MONTH, -1);
                	dataLimiteNoFinalSemanaOuFeriado = (!verificarDataValidaParaAplicacaoProva(proximaDataLimite, itemDataProvaInicial, usuario));
                	controleNrDiasRecuou++;
                }
                proximaDataLimite = replicarHoraDataOrigemDataDestino(itemDataProvaInicial.getDataLimiteAdiarVencimentoInscricao(), proximaDataLimite);
                novoItemGerarAPartirAnterior.setDataLimiteAdiarVencimentoInscricao(proximaDataLimite);
                
                // DATA LIBERACAO RESULTADO - a proxima data de termino de inscricao deverá estar com a mesma diferenca
                // de dias que havia das datas padrões iniciais fornecidas pelo usuário. Contudo, caso a mesma seja calculada
                // para um final de semana ou feriado, então a mesma será deslocada para o futuro. Pois, resultados tendem a ser
                // liberados somente em dias úteis da IE.
                Date proximaDataLiberacaoResultado = Uteis.getDataFutura(proximaDataProva, Calendar.DAY_OF_MONTH, nrDiasPadraoResultadoAposDataProva);
                boolean dataLiberacaoResultadoNoFinalSemanaOuFeriado = (!verificarDataValidaParaAplicacaoProva(proximaDataLiberacaoResultado, itemDataProvaInicial, usuario));
                controleNrDiasAvancou = 1;
                while ((dataLiberacaoResultadoNoFinalSemanaOuFeriado) && (controleNrDiasAvancou <= 30)) {
                    proximaDataLiberacaoResultado = Uteis.getDataFutura(proximaDataLiberacaoResultado, Calendar.DAY_OF_MONTH, 1);
                    dataLiberacaoResultadoNoFinalSemanaOuFeriado = (!verificarDataValidaParaAplicacaoProva(proximaDataLiberacaoResultado, itemDataProvaInicial, usuario));
                    controleNrDiasAvancou++;
                }
                proximaDataLiberacaoResultado = replicarHoraDataOrigemDataDestino(itemDataProvaInicial.getDataLiberacaoResultado(), proximaDataLiberacaoResultado);
                novoItemGerarAPartirAnterior.setDataLiberacaoResultado(proximaDataLiberacaoResultado);
                
                if (Uteis.isAtributoPreenchido(itemDataProvaInicial.getDataLimiteApresentarDadosVisaoCandidato())) {
	                Date proximaDataApresentarDadosVisaoCandidato = Uteis.getDataFutura(proximaDataProva, Calendar.DAY_OF_MONTH, (nrDiasPadraoApresentacaoResultadoFinalParaCandidato * -1));
	                boolean dataLimiteApresentarDadosVisaoCandidatoNoFinalSemanaOuFeriado = (!verificarDataValidaParaAplicacaoProva(proximaDataApresentarDadosVisaoCandidato, itemDataProvaInicial, usuario));
	                controleNrDiasAvancou = 1;
	                while ((dataLimiteApresentarDadosVisaoCandidatoNoFinalSemanaOuFeriado) && (controleNrDiasAvancou <= 30)) {
	                	proximaDataApresentarDadosVisaoCandidato = Uteis.getDataFutura(proximaDataApresentarDadosVisaoCandidato, Calendar.DAY_OF_MONTH, -1);
	                    dataLimiteApresentarDadosVisaoCandidatoNoFinalSemanaOuFeriado = (!verificarDataValidaParaAplicacaoProva(proximaDataApresentarDadosVisaoCandidato, itemDataProvaInicial, usuario));
	                    controleNrDiasAvancou++;
	                }
	                proximaDataApresentarDadosVisaoCandidato = replicarHoraDataOrigemDataDestino(itemDataProvaInicial.getDataLimiteApresentarDadosVisaoCandidato(), proximaDataApresentarDadosVisaoCandidato);
	                novoItemGerarAPartirAnterior.setDataLimiteApresentarDadosVisaoCandidato(proximaDataApresentarDadosVisaoCandidato);
                }
                
                // VERIFICAR SE AS DATAS OBTIDAS SÃO VALIDAS 
                if (Uteis.getDataBD2359(novoItemGerarAPartirAnterior.getDataProva()).compareTo(
                        Uteis.getDataBD2359(itemDataProvaInicial.getDataFuturaReplicarDatasEHorarios())) > 0) {
                    // caso o ultimo item gerado tenha a data maior que da limite futura, entao devemos sair do laco
                    break;
                }
                if (objReferenteGabarito) {
                    procSeletivo.adicionarObjItemProcSeletivoDataGabaritoVOs(novoItemGerarAPartirAnterior);
                } else {
                    procSeletivo.adicionarObjItemProcSeletivoDataProvaVOs(novoItemGerarAPartirAnterior);
                }
                utilmoItemDataProvaAdicionado = novoItemGerarAPartirAnterior;
                contadorDatasGeradas++;
            }
        }

        public List<Date> consultarPorListaProcSeletivoComboBox(List<ProcSeletivoVO> listaProcessoSeletivoVOs, UsuarioVO usuarioVO) {
    		StringBuilder sb = new StringBuilder();
    		sb.append("select distinct itemProcSeletivoDataProva.dataProva from itemProcSeletivoDataProva ");
    		sb.append(" where 1=1 ");
    		if (!listaProcessoSeletivoVOs.isEmpty()) {
    			sb.append("and itemProcSeletivoDataProva.procSeletivo in (");
    			for (ProcSeletivoVO procSeletivoVO : listaProcessoSeletivoVOs) {
    				if (procSeletivoVO.getFiltrarProcessoSeletivo()) {
    					sb.append(procSeletivoVO.getCodigo()).append(", ");
    				}
    			}
    			sb.append("0) ");
    		}
    		sb.append("ORDER BY itemProcSeletivoDataProva.dataProva ");
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    		List<Date> listaItemProcSeletivoVOs = new ArrayList<Date>(0);
    		while (tabelaResultado.next()) {
    			listaItemProcSeletivoVOs.add(tabelaResultado.getDate("dataProva"));
    		}
    		return listaItemProcSeletivoVOs;
    	}
        
        public void validarDados(ItemProcSeletivoDataProvaVO obj) throws Exception {
    		ItemProcSeletivoDataProvaVO.validarDados(obj);
    	}

    	@Override
    	public Integer consultarNumeroCandidatoNotificado(ProcSeletivoVO processoSeletivo, ItemProcSeletivoDataProvaVO itemProcessoSeletivoDataProva) throws Exception {
    		StringBuilder sqlStr = new StringBuilder("");
    		sqlStr.append("SELECT  COUNT(candidato.codigo) AS numerocandidatonotificado ");
    		sqlStr.append(" FROM inscricao ");
    		sqlStr.append(" INNER JOIN pessoa as candidato        ON candidato.codigo                 = inscricao.candidato ");
    		sqlStr.append(" INNER JOIN procseletivo               ON procseletivo.codigo              = inscricao.procseletivo ");
    		sqlStr.append(" INNER JOIN unidadeensinocurso         ON unidadeensinocurso.codigo        = inscricao.cursoopcao1 ");
    		sqlStr.append(" INNER JOIN unidadeensino              ON unidadeensino.codigo             = unidadeensinocurso.unidadeensino ");
    		sqlStr.append(" INNER JOIN ItemProcSeletivoDataProva  ON ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
    		sqlStr.append("WHERE 1=1 ");
    		sqlStr.append(" AND inscricao.situacao = 'CO'");
    		sqlStr.append(" AND inscricao.situacaoInscricao = 'ATIVO'");
    		sqlStr.append(" AND procseletivo.codigo = ").append(processoSeletivo.getCodigo());
    		sqlStr.append(" AND ItemProcSeletivoDataProva.codigo = ").append(itemProcessoSeletivoDataProva.getCodigo()).append(" ;");
    		return getConexao().getJdbcTemplate().queryForInt(sqlStr.toString());
    	}
    	
    	public Date consultarDataProvaPorCodigo(Integer codigoItemProcSeletivoDataProva) throws Exception {
    		StringBuilder sqlStr = new StringBuilder("");
    		sqlStr.append("SELECT itemprocseletivodataprova.dataprova FROM itemprocseletivodataprova  WHERE itemprocseletivodataprova.codigo = ").append(codigoItemProcSeletivoDataProva);
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    		if (tabelaResultado.next()) {
    			return  tabelaResultado.getDate("dataprova");
    		}
    		return null;
    	}
    	
    	public ItemProcSeletivoDataProvaVO consultarMaiorDatas(Integer codigoProcSeletivo) throws Exception {
    		StringBuilder sqlStr = new StringBuilder("");
    		sqlStr.append("SELECT itemprocseletivodataprova.codigo , itemprocseletivodataprova.dataprova   FROM itemprocseletivodataprova where procseletivo  = '").append(codigoProcSeletivo).append("' order by itemprocseletivodataprova.dataprova desc limit 1");
    	
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    		ItemProcSeletivoDataProvaVO item = new ItemProcSeletivoDataProvaVO();
    		if (tabelaResultado.next()) {
    			item.setCodigo(tabelaResultado.getInt("codigo"));
    			item.setDataProva(tabelaResultado.getDate("dataProva"));
    			return item;
    		}
    		
    		return item;
    	}
    	
    	
    	public ItemProcSeletivoDataProvaVO consultarPorProcSeletivoDataEHora(Integer codigoProcSeletivo, Date data, String hora, UsuarioVO usuario) throws Exception {
    		StringBuilder sqlStr = new StringBuilder("");
    		sqlStr.append("SELECT itemprocseletivodataprova.codigo, itemprocseletivodataprova.dataprova, itemprocseletivodataprova.hora  FROM itemprocseletivodataprova ");
    		sqlStr.append("where procseletivo  = '").append(codigoProcSeletivo).append("' ");
    		sqlStr.append(" and cast(itemprocseletivodataprova.dataprova as date) = '").append(Uteis.getDataJDBC(data)).append("' ");
    		sqlStr.append(" and itemprocseletivodataprova.hora = '").append(hora).append("' ");
    		sqlStr.append(" order by itemprocseletivodataprova.dataprova desc limit 1 ");
    	
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    		ItemProcSeletivoDataProvaVO item = new ItemProcSeletivoDataProvaVO();
    		if (tabelaResultado.next()) {
    			item.setCodigo(tabelaResultado.getInt("codigo"));
    			item.setDataProva(tabelaResultado.getDate("dataProva"));
    			return item;
    		}
    		
    		return item;
    	}
    	

    	@Override
    	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    	public ItemProcSeletivoDataProvaVO inicializarDadosItemImportacaoCandidatoInscricao(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, ProcSeletivoVO procSeletivoVO, Map<String, ItemProcSeletivoDataProvaVO> mapItemProcSeltivoDataProvaVOs, UsuarioVO usuario) throws Exception {
    		
    		if (mapItemProcSeltivoDataProvaVOs.containsKey(procSeletivoVO.getDescricao().toString() +"."+UteisData.getDataAno4Digitos(importarCandidatoVO.getDataProva()) +"."+ importarCandidatoVO.getHora())) {
				ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO = mapItemProcSeltivoDataProvaVOs.get(procSeletivoVO.getDescricao().toString() +"."+UteisData.getDataAno4Digitos(importarCandidatoVO.getDataProva()) +"."+ importarCandidatoVO.getHora());
				if (!Uteis.isAtributoPreenchido(itemProcSeletivoDataProvaVO.getDataInicioInscricao()) || itemProcSeletivoDataProvaVO.getDataInicioInscricao().after(importarCandidatoVO.getDataInicioInscricao())) {
					itemProcSeletivoDataProvaVO.setDataInicioInscricao(importarCandidatoVO.getDataInicioInscricao());
				}
				if (!Uteis.isAtributoPreenchido(itemProcSeletivoDataProvaVO.getDataTerminoInscricao()) || itemProcSeletivoDataProvaVO.getDataTerminoInscricao().before(importarCandidatoVO.getDataTerminoInscricao())) {
					itemProcSeletivoDataProvaVO.setDataTerminoInscricao(importarCandidatoVO.getDataTerminoInscricao());
				}
				Date dataLimite = Uteis.obterDataPassada(Uteis.getDataJDBC(importarCandidatoVO.getDataProva()), 0);
				if (Uteis.getDataJDBC(dataLimite).before(Uteis.getDataJDBC(importarCandidatoVO.getDataTerminoInscricao())) 
						&& !Uteis.getData(dataLimite).equals(Uteis.getData(importarCandidatoVO.getDataTerminoInscricao()))) {
					dataLimite = importarCandidatoVO.getDataTerminoInscricao();
				}
				itemProcSeletivoDataProvaVO.setDataLimiteAdiarVencimentoInscricao(dataLimite);
				return null;
			} 
    		if (Uteis.isAtributoPreenchido(procSeletivoVO.getCodigo())) {
    			
    			ItemProcSeletivoDataProvaVO item = consultarPorProcSeletivoDataEHora(procSeletivoVO.getCodigo(), importarCandidatoVO.getDataProva(), importarCandidatoVO.getHora(), usuario);
    			if (Uteis.isAtributoPreenchido(item.getCodigo())) {
    				mapItemProcSeltivoDataProvaVOs.put(procSeletivoVO.getDescricao().toString() +"."+UteisData.getDataAno4Digitos(importarCandidatoVO.getDataProva()) +"."+ importarCandidatoVO.getHora(), item);
    				return item;
    			}
    		} 
    		ItemProcSeletivoDataProvaVO itemDataProvaVO = new ItemProcSeletivoDataProvaVO();
    		
    		itemDataProvaVO.setDataProva(UteisData.adicionarHoraEmData(importarCandidatoVO.getDataProva(), importarCandidatoVO.getHora()));
    		itemDataProvaVO.setProcSeletivo(procSeletivoVO.getCodigo());
    		itemDataProvaVO.setHora(importarCandidatoVO.getHora());
    		itemDataProvaVO.setTipoProvaGabarito("PR");
    		Date dataLimite = Uteis.obterDataPassada(Uteis.getDataJDBC(importarCandidatoVO.getDataProva()), 0);
			if (Uteis.getDataJDBC(dataLimite).before(Uteis.getDataJDBC(importarCandidatoVO.getDataTerminoInscricao())) 
					&& !Uteis.getData(dataLimite).equals(Uteis.getData(importarCandidatoVO.getDataTerminoInscricao()))) {
				dataLimite = importarCandidatoVO.getDataTerminoInscricao();
			}
    		itemDataProvaVO.setDataLimiteAdiarVencimentoInscricao(dataLimite);
    		itemDataProvaVO.setDataLiberacaoResultado(Uteis.getDataFuturaConsiderandoDataAtual(importarCandidatoVO.getDataProva(), 3));
    		itemDataProvaVO.setDataInicioInscricao(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(importarCandidatoVO.getDataInicioInscricao()));
    		itemDataProvaVO.setDataTerminoInscricao(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(importarCandidatoVO.getDataTerminoInscricao()));
    		mapItemProcSeltivoDataProvaVOs.put(procSeletivoVO.getDescricao().toString() +"."+UteisData.getDataAno4Digitos(importarCandidatoVO.getDataProva()) +"."+ importarCandidatoVO.getHora(), itemDataProvaVO);
    		return itemDataProvaVO;
    	}
}
