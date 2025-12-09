package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.recursoshumanos.FaixaValorVO;
import negocio.comuns.recursoshumanos.ValorReferenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoValorReferenciaEnum;
import negocio.comuns.recursoshumanos.enumeradores.ValorFixoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.ValorReferenciaFolhaPagamentoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>ValorReferenciaFolhaPagamentoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>ValorReferenciaFolhaPagamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@SuppressWarnings({"unchecked", "rawtypes"})
@Service
@Scope
@Lazy
public class ValorReferenciaFolhaPagamento extends ControleAcesso implements ValorReferenciaFolhaPagamentoInterfaceFacade {

	private static final long serialVersionUID = -1084004772804472456L;

	protected static String idEntidade;

	public ValorReferenciaFolhaPagamento() throws Exception {
		super();
		setIdEntidade("ValorReferenciaFolhaPagamento");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(ValorReferenciaFolhaPagamentoVO obj, List<FaixaValorVO> listaAnteriorFaixaValores, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
		persistirFaixaValores(obj, listaAnteriorFaixaValores, usuarioVO);
	}

	/**
	 * Grava ou exclui todas as faixas de valores adicionados e/ou removidos da tabela de referencia da folha de pagamento.
	 * 
	 * @param obj
	 * @param listaAnteriorFaixaValores - lista para validar quais valores foram adicionado ou excluidos da lista.
	 * @param usuario
	 * @throws Exception
	 */
	private void persistirFaixaValores(ValorReferenciaFolhaPagamentoVO obj, List<FaixaValorVO> listaAnteriorFaixaValores, UsuarioVO usuario) throws Exception {
		if (!obj.getListaFaixaValores().isEmpty()) {

			for (FaixaValorVO faixaValor : obj.getListaFaixaValores()) {
				faixaValor.setValorReferenciaFolhaPagamento(obj);
				getFacadeFactory().getFaixaValorInterfaceFacade().persistir(faixaValor, Boolean.FALSE, usuario);
			}

			if (listaAnteriorFaixaValores != null) {
				for (FaixaValorVO faixaValor : listaAnteriorFaixaValores) {
					if (!contains(obj.getListaFaixaValores(), faixaValor)) {
						getFacadeFactory().getFaixaValorInterfaceFacade().excluir(faixaValor, true, usuario);
					}
				}
			}
		}		
	}

	public boolean contains(List<FaixaValorVO> lista, FaixaValorVO faixaValor) {
		for (FaixaValorVO faixaValorVO : lista) {
			if (faixaValor.getCodigo().equals(faixaValorVO.getCodigo())) {
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(ValorReferenciaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ValorReferenciaFolhaPagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder("INSERT INTO ValorReferenciaFolhaPagamento(");
			sql.append("identificador, descricao, datainiciovigencia, datafimvigencia, imposto, ")
			.append(" valorFixo, valor, referencia, atualizarFinalVigencia, situacao,")
			.append(" usuarioresponsavelalteracao, dataUltimaAlteracao, sql, tipoValorReferencia, valorReferenciaPadrao ")
			.append(" ) VALUES ( ")
			.append("?, ?, ?, ?, ?,")
			.append("?, ?, ?, ?, ?,")
			.append("?, ?, ?, ?, ?)")
			.append("returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getIdentificador(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataInicioVigencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataFimVigencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getImposto(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.isValorFixo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getReferencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAtualizarFinalVigencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getUsuarioUltimaAlteracao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataUltimaAlteracao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSql(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoValorReferencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorReferenciaPadrao(), ++i, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(ValorReferenciaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ValorReferenciaFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			obj.setUsuarioUltimaAlteracao(usuarioVO);
			obj.setDataUltimaAlteracao(new Date());
			final String sql = " UPDATE ValorReferenciaFolhaPagamento set identificador= ?, descricao= ?, datainiciovigencia = ?, datafimvigencia = ?, imposto = ?,"
							 + " valorFixo = ?, valor = ?, referencia = ?, atualizarFinalVigencia = ?, situacao =?, usuarioresponsavelalteracao = ?, dataUltimaAlteracao = ?,"
							 + " sql = ?, tipoValorReferencia = ?, valorReferenciaPadrao = ? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getIdentificador(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataInicioVigencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataFimVigencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getImposto(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.isValorFixo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getReferencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAtualizarFinalVigencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getUsuarioUltimaAlteracao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataUltimaAlteracao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSql(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoValorReferencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorReferenciaPadrao(), ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ValorReferenciaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (!obj.getListaFaixaValores().isEmpty()) {
				for (FaixaValorVO faixaValor : obj.getListaFaixaValores()) {
					getFacadeFactory().getFaixaValorInterfaceFacade().excluir(faixaValor, false, usuarioVO);
				}
			}
			
			String sql = "DELETE FROM ValorReferenciaFolhaPagamento WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<ValorReferenciaFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, String valorConsulta, String situacao, Date inicioVigencia, Date finalVigencia, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSQLSelectSqlBasico());
		sql.append(" WHERE 1 = 1");
		
		List<Object> listaFiltros = new ArrayList<>();

		if (Uteis.isAtributoPreenchido(inicioVigencia)) {
			sql.append(" AND ").append("datainiciovigencia >=  '" + inicioVigencia + "'" );
		} 
		
		if (Uteis.isAtributoPreenchido(finalVigencia)) {
			sql.append(" AND ").append(" datafimvigencia <= '" + finalVigencia + "'");
		}

		if (!situacao.equals("TODOS")) {
			sql.append(" AND situacao = ?");	
			listaFiltros.add(situacao);
		}

		switch (campoConsulta) {
			case "descricao":
				sql.append(" AND UPPER( descricao ) LIKE (UPPER(sem_acentos(?))) ORDER BY valorreferenciafolhapagamento.codigo DESC");
				break;
			case "identificador":
				sql.append(" AND UPPER(identificador) LIKE (UPPER(sem_acentos(?))) ORDER BY valorreferenciafolhapagamento.codigo DESC");
				break;
			case "referencia" :
				sql.append(" AND referencia = ? ");
				break;
			default:
				break;
		}
		listaFiltros.add(PERCENT + valorConsulta + PERCENT);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), listaFiltros.toArray());
		List<ValorReferenciaFolhaPagamentoVO> tabelaReferenciaFolhasPagamentos = new ArrayList<>();
		while(tabelaResultado.next()) {
			tabelaReferenciaFolhasPagamentos.add(montarDados(tabelaResultado));
		}
		return tabelaReferenciaFolhasPagamentos;
	}

	/**
	 * Monta o objeto <code>ValorReferenciaFolhaPagamentoVO<code> consultado do
	 * banco de dados.
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception 
	 * @throws InvalidResultSetAccessException 
	 */
	public ValorReferenciaFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado) throws InvalidResultSetAccessException, Exception {
		
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		
	}
	
	/**
	 * Monta o objeto <code>ValorReferenciaFolhaPagamentoVO<code> consultado do
	 * banco de dados.
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception 
	 * @throws InvalidResultSetAccessException 
	 */
	public ValorReferenciaFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado, Integer nivelMontarDados) throws InvalidResultSetAccessException, Exception {
		
		ValorReferenciaFolhaPagamentoVO obj = new ValorReferenciaFolhaPagamentoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setIdentificador(tabelaResultado.getString("identificador"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setDataInicioVigencia(tabelaResultado.getDate("datainiciovigencia"));
		obj.setDataFimVigencia(tabelaResultado.getDate("datafimvigencia"));
		obj.getImposto().setCodigo(tabelaResultado.getInt("imposto"));
		obj.setValorFixo(tabelaResultado.getBoolean("valorFixo"));
		obj.setValor(tabelaResultado.getBigDecimal("valor"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("referencia"))) {
			obj.setReferencia(ValorFixoEnum.valueOf(tabelaResultado.getString("referencia")));
		}
		obj.setAtualizarFinalVigencia(tabelaResultado.getBoolean("atualizarFinalVigencia"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacao"))) {
			obj.setSituacao(SituacaoEnum.valueOf(tabelaResultado.getString("situacao")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("usuarioresponsavelalteracao"))) {
			obj.setUsuarioUltimaAlteracao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(tabelaResultado.getInt("usuarioresponsavelalteracao"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		obj.setDataUltimaAlteracao(tabelaResultado.getDate("dataultimaalteracao"));
		obj.setSql(tabelaResultado.getString("sql"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipovalorreferencia"))) {
			obj.setTipoValorReferencia(TipoValorReferenciaEnum.valueOf(tabelaResultado.getString("tipovalorreferencia")));
		}
		
		if(nivelMontarDados.equals(Uteis.NIVELMONTARDADOS_TODOS)) {
			obj.setListaFaixaValores(getFacadeFactory().getFaixaValorInterfaceFacade().consultarPorTabelaReferenciaFolhaPagamento(obj.getCodigo(), false, null));
		}
		
		obj.setValorReferenciaPadrao(tabelaResultado.getBoolean("valorreferenciapadrao"));

		return obj;
	}

	/**
	 * Sql basico para consulta da <code>ValorReferenciaFolhaPagamentoVO<code>
	 * 
	 * @return
	 */
	public String getSQLSelectSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM valorreferenciafolhapagamento ");

		return sql.toString();
	}

	/**
	 * Valida os campos obrigatorios da <code>ValorReferenciaFolhaPagamentoVO<code>
	 * 
	 * @param obj <code>FaixaValor</code>
	 * @throws ConsistirException
	 * @throws ParseException 
	 */
	private void validarDados(ValorReferenciaFolhaPagamentoVO obj) throws ConsistirException, ParseException {

		if (!Uteis.isAtributoPreenchido(obj.getIdentificador())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ValorReferenciaFolhaPagamento_identificador"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ValorReferenciaFolhaPagamento_descricao"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getReferencia())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ValorReferenciaFolhaPagamento_referencia"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataInicioVigencia())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ValorReferenciaFolhaPagamento_inicioVigencia"));
		}

		if (UteisData.validarDataInicialMaiorFinal(obj.getDataInicioVigencia(), obj.getDataFimVigencia())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_ValorReferenciaFolhaPagamento_vigenciaFinalMenorVigenciaIncial"));
		}
		
		if (identificadorJaCadastradoEAtivo(obj)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ValorReferenciaFolhaPagamento_identificadorJaCadastrado"));
		}
	}

	/**
	 * Metodo que consulta a tabela buscando pelo identificador e com data de vigencia valida 
	 */
	private boolean identificadorJaCadastradoEAtivo(ValorReferenciaFolhaPagamentoVO obj) {
		StringBuilder sqlStr = new StringBuilder(" SELECT codigo FROM valorreferenciafolhapagamento ");
				sqlStr.append(" WHERE identificador like ? ")
				.append(" and ((datainiciovigencia <= ? and datafimvigencia >= ?)") 
				.append(" or (datainiciovigencia >= ? and datafimvigencia >= ? and datainiciovigencia <= ?)") 
				.append(" or (datainiciovigencia >= ? and datafimvigencia <= ?) ")
				.append(" or (datainiciovigencia <= ? and datafimvigencia <= ? and datafimvigencia >= ?))"); 
				
		ArrayList<Object> parametros = new ArrayList<>();
		parametros.add(obj.getIdentificador());
		
		parametros.add(obj.getDataInicioVigencia());
		parametros.add(obj.getDataFimVigencia());	
		
		parametros.add(obj.getDataInicioVigencia());
		parametros.add(obj.getDataFimVigencia());
		parametros.add(obj.getDataFimVigencia());
		
		parametros.add(obj.getDataInicioVigencia());
		parametros.add(obj.getDataFimVigencia());
		
		parametros.add(obj.getDataInicioVigencia());
		parametros.add(obj.getDataFimVigencia());
		parametros.add(obj.getDataInicioVigencia());
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametros.toArray());
		
		while(tabelaResultado.next()) {
			Integer codigo = tabelaResultado.getInt("codigo");
			if(codigo != null && !codigo.equals(obj.getCodigo()))
				return true;
		}
		return false;
	}

	public void validarDadosDuplicadosIdentificador(ValorReferenciaFolhaPagamentoVO obj) throws ConsistirException {
		int retorno = consutarDadosTotalPorIdentificador(obj);
		
		if (retorno > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TabelaReferencia_duplicado"));
		}
	}

	/**
	 * Valida se existe uma <code>TabelaRerenciaFolhaPagamento</code> cadastrada com o a referencia selecionada é
	 * com a situacao ativa.
	 * 
	 * @param obj
	 * @throws ConsistirException
	 */
	public void validarDadosDuplicidadeReferenciaComSituacaoAtiva(ValorReferenciaFolhaPagamentoVO obj) throws ConsistirException {
		int retorno = consutarDadosTotalPorReferenciaSituacao(obj);

		if (retorno > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ValorReferenciaFolhaPagamento_referenciaAtivaJaCadastrado"));
		}
	}
	
	private int consutarDadosTotalPorIdentificador(ValorReferenciaFolhaPagamentoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(codigo) as qtde FROM tabelaReferenciaFolhaPagamento");
		sql.append(" WHERE TRIM(identificador) = ?");
		
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" AND codigo != ?");
		}

		SqlRowSet rs = null;
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getIdentificador(), obj.getCodigo());
		} else {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getIdentificador());
		}

        if (rs.next()) {
            return rs.getInt("qtde");
        }

    	return 0;
	}

	private int consutarDadosTotalPorReferenciaSituacao(ValorReferenciaFolhaPagamentoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(codigo) as qtde FROM tabelaReferenciaFolhaPagamento");
		sql.append(" WHERE situacao = 'ATIVO' AND referencia = ?");
		
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" AND codigo != ?");
		}

		SqlRowSet rs = null;
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getReferencia().toString(), obj.getCodigo());
		} else {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getReferencia().toString());
		}

        if (rs.next()) {
            return rs.getInt("qtde");
        }

    	return 0;
	}

	/**
	 * Encerra a vigencia da tabela de referencia
	 * 
	 * @param obj
	 * @param verificarAcesso
	 * @param usuario
	 * 
	 * @exception Exception
	 */
	@Override
	public void alterarSituacaoDataFinalVigencia(ValorReferenciaFolhaPagamentoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ValorReferenciaFolhaPagamento.alterar(idEntidade, verificarAcesso, usuario);
			obj.setUsuarioUltimaAlteracao(usuario);
			obj.setDataUltimaAlteracao(new Date());
			final String sql = "UPDATE ValorReferenciaFolhaPagamento SET atualizarFinalVigencia = ?, situacao = ?,  usuarioresponsavelalteracao = ?, dataUltimaAlteracao = ? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setBoolean(1, obj.getAtualizarFinalVigencia());
					sqlAlterar.setString(2, obj.getSituacao().toString());
					sqlAlterar.setInt(3, obj.getUsuarioUltimaAlteracao().getCodigo());
					sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataUltimaAlteracao()));
					sqlAlterar.setInt(5, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void clonarVigencia(ValorReferenciaFolhaPagamentoVO tabelaReferenciaFolhaPagamento, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		ValorReferenciaFolhaPagamentoVO obj = tabelaReferenciaFolhaPagamento;
		obj.setIdentificador(null);
		obj.setSituacao(SituacaoEnum.ATIVO);
		this.incluir(obj, validarAcesso, usuario);
	}

	@Override
	public BigDecimal consultarValorFixo(String identificador, Date vigencia) {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT valor FROM valorreferenciafolhapagamento");
		sql.append(" WHERE UPPER(identificador) = UPPER(?) AND ? BETWEEN datainiciovigencia AND datafimvigencia");
		sql.append(" AND tipovalorreferencia = ?");

		SqlRowSet rs = null;
		rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{identificador,new java.sql.Date(vigencia.getTime()), TipoValorReferenciaEnum.FIXO.toString()});
	
	    if (rs.next()) {
	        return rs.getBigDecimal("valor");
	    }else {
	    	return BigDecimal.ZERO;
	    }
		
	}
	
	@Override
	public BigDecimal consultarValorFixoPorReferencia(ValorFixoEnum referencia, Date vigencia) {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT valor FROM valorreferenciafolhapagamento");
		sql.append(" WHERE UPPER(referencia) = UPPER(?) AND ? BETWEEN datainiciovigencia AND datafimvigencia");
		sql.append(" AND tipovalorreferencia = ?");
		
		SqlRowSet rs = null;
		rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{referencia.toString(),new java.sql.Date(vigencia.getTime()), TipoValorReferenciaEnum.FIXO.toString()});
	
	    if (rs.next()) {
	        return rs.getBigDecimal("valor");
	    }else {
	    	return BigDecimal.ZERO;
	    }
		
	}

	@Override
	public String consultarSql(String identificador, Date vigencia){
		String sqlValorReferencia = "";

		String sql = new StringJoiner(" ")
		.add(" SELECT sql FROM valorreferenciafolhapagamento")
		.add(" WHERE UPPER(identificador) = UPPER(?) AND ? BETWEEN datainiciovigencia AND datafimvigencia")
		.add(" AND tipovalorreferencia = ? limit 1").toString();
		sqlValorReferencia = getConexao().getJdbcTemplate().queryForObject(sql, new Object[]{identificador,new java.sql.Date(vigencia.getTime()), TipoValorReferenciaEnum.SQL.toString()}, String.class);

		return sqlValorReferencia;
	}
	
	public FaixaValorVO consultarFaixaValor(String identificador, Date vigencia, BigDecimal valor) {
		String sql = new StringJoiner(" ")
		.add(" SELECT faixavalor.codigo FROM valorreferenciafolhapagamento AS referencia")
		.add(" LEFT JOIN faixavalor on  referencia.codigo = faixavalor.valorreferenciafolhapagamento")
		.add(" WHERE UPPER(referencia.identificador) = UPPER(?) AND ? BETWEEN referencia.datainiciovigencia AND referencia.datafimvigencia")
		.add(" AND ? BETWEEN limiteinferior AND limitesuperior")
		.add(" AND referencia.tipovalorreferencia = ? limit 1").toString();
		
		Integer faixaValor = getConexao().getJdbcTemplate().queryForObject(sql, new Object[]{identificador,new java.sql.Date(vigencia.getTime()), valor, TipoValorReferenciaEnum.FAIXA_VALOR.toString()}, Integer.class);
		try {
			return getFacadeFactory().getFaixaValorInterfaceFacade().consultarPorChavePrimaria(faixaValor);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	public FaixaValorVO consultarFaixaValor(String identificador, Date vigencia, int valor) {
		return consultarFaixaValor(identificador, vigencia, new BigDecimal(valor));
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ValorReferenciaFolhaPagamento.idEntidade = idEntidade;
	}

	@Override
	public FaixaValorVO consultarFaixaValor(ValorReferenciaFolhaPagamentoVO valorReferencia, BigDecimal valor) {
		
		for(FaixaValorVO faixaValor : valorReferencia.getListaFaixaValores()) {
			
			if(faixaValor.getLimiteInferior().compareTo(valor) >= 0 && faixaValor.getLimiteSuperior().compareTo(valor) <= 0) {
				return faixaValor;
			}
		}
		return null;
	}

	@Override
	public BigDecimal consultarFaixaPercentual(ValorReferenciaFolhaPagamentoVO valorReferencia, BigDecimal valor) {
		
		FaixaValorVO faixaValorVO = consultarFaixaValor(valorReferencia, valor);
		
		if(faixaValorVO == null) {
			return new BigDecimal(0);
		} else {
			return faixaValorVO.getPercentual();
		}
	}

	@Override
	public BigDecimal consultarValorDeducao(ValorReferenciaFolhaPagamentoVO valorReferencia, BigDecimal valor)  {
		
		FaixaValorVO faixaValorVO = consultarFaixaValor(valorReferencia, valor);
		
		if(faixaValorVO == null) {
			return new BigDecimal(0);
		} else {
			return faixaValorVO.getValorDeduzir();
		}
	}
	
	public ValorReferenciaFolhaPagamentoVO consultarValorReferenciaPorReferencia(String referencia, Date vigencia) {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM valorreferenciafolhapagamento");
		sql.append(" WHERE ? BETWEEN datainiciovigencia AND datafimvigencia");
		sql.append(" AND referencia = ? limit 1");

		List parametros = new ArrayList<>();
		parametros.add(vigencia);
		parametros.add(referencia);
		
        try {
        	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray());
            if (rs.next()) {
            	return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return new ValorReferenciaFolhaPagamentoVO();
	}

	@Override
	public List<ValorReferenciaFolhaPagamentoVO> consultarAtivosFinalVigenciaSelecionada() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT codigo, datafimvigencia FROM valorreferenciafolhapagamento");
		sql.append(" WHERE situacao = 'ATIVO' AND atualizarfinalvigencia = true");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ValorReferenciaFolhaPagamentoVO> lista = new ArrayList<>();
        while (rs.next()) {
        	ValorReferenciaFolhaPagamentoVO obj = new ValorReferenciaFolhaPagamentoVO();
        	obj.setCodigo(rs.getInt("codigo"));
        	obj.setDataFimVigencia(rs.getDate("datafimvigencia"));
        	lista.add(obj);
        }
		return lista;
	}

	@Override
	public void alterarFinalVigencia(ValorReferenciaFolhaPagamentoVO obj, UsuarioVO usuario) {
		try {
			final String sql = "UPDATE ValorReferenciaFolhaPagamento SET datafimvigencia = ? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataFimVigencia()));
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
}