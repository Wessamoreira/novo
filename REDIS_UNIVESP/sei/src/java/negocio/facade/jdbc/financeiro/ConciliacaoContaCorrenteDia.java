package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoConjuntaVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.TipoTransacaoOFXEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ConciliacaoContaCorrenteDiaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ConciliacaoContaCorrenteDia extends ControleAcesso implements ConciliacaoContaCorrenteDiaInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6497267726502827756L;
	protected static String idEntidade;

	public ConciliacaoContaCorrenteDia() throws Exception {
		super();
		setIdEntidade("ConciliacaoContaCorrenteDia");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ConciliacaoContaCorrenteDiaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (ConciliacaoContaCorrenteDiaVO obj : lista) {
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
			validarConciliacaoContaCorrenteDiaExtratoQueSeramDeletados(obj, usuarioVO);
			getFacadeFactory().getConciliacaoContaCorrenteDiaExtratoInterfaceFacade().persistir(obj.getListaConciliacaoContaCorrenteExtrato(), false, usuarioVO);
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Primeiramente valida
	 * os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConciliacaoContaCorrenteDiaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConciliacaoContaCorrenteDia.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ConciliacaoContaCorrenteDia (data, conciliacaoContaCorrente ) ");
			sql.append("    VALUES ( ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setInt(++i, obj.getConciliacaoContaCorrente().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
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
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConciliacaoContaCorrenteDiaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConciliacaoContaCorrenteDia.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ConciliacaoContaCorrenteDia ");
			sql.append("   SET data=?, conciliacaoContaCorrente=? ");
			sql.append("       WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setInt(++i, obj.getConciliacaoContaCorrente().getCodigo());
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da
	 * operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConciliacaoContaCorrenteDiaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConciliacaoContaCorrenteDia.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM ConciliacaoContaCorrenteDia WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void validarConciliacaoContaCorrenteDiaExtratoQueSeramDeletados(ConciliacaoContaCorrenteDiaVO obj, UsuarioVO usuario) throws Exception{
	/*	StringBuilder sb = new StringBuilder(" select array_to_string(array_agg(codigo), ',') as codigo  FROM ConciliacaoContaCorrenteDiaExtrato  where conciliacaoContaCorrenteDia =  ").append(obj.getCodigo());
		sb.append(" and codigo not in ( 0  ");
		for (ConciliacaoContaCorrenteDiaExtratoVO objExtradoDia : obj.getListaConciliacaoContaCorrenteExtrato()) {
			if(Uteis.isAtributoPreenchido(objExtradoDia.getCodigo())){
				sb.append(", ").append(objExtradoDia.getCodigo());	
			}
		}
		sb.append(" ) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next() && Uteis.isAtributoPreenchido(tabelaResultado.getString("codigo"))) {
			getFacadeFactory().getExtratoContaCorrenteFacade().anularConciliacaoContaCorrenteDiaExtratoPorConciliacaoExtratoDia(tabelaResultado.getString("codigo"), usuario);
		}*/
		getFacadeFactory().getExtratoContaCorrenteFacade().anularExtratoContaCorrentePorConciliacaoContaCorrenteDiaQueNaoFazemMaisParte(obj, usuario);
		if(Uteis.isAtributoPreenchido(obj.getListaConciliacaoContaCorrenteExtratoExcluir())){
			for (ConciliacaoContaCorrenteDiaExtratoVO objExtradoDia : obj.getListaConciliacaoContaCorrenteExtratoExcluir()) {
				getFacadeFactory().getExtratoContaCorrenteFacade().anularConciliacaoContaCorrenteDiaExtratoPorCodigoExtrato(objExtradoDia.getCodigoSei(), usuario);	
			}
		}
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaConciliacaoContaCorrenteExtrato(), "ConciliacaoContaCorrenteDiaExtrato", "conciliacaoContaCorrenteDia", obj.getCodigo(), usuario);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void addConciliacaoContaCorrenteDiaVO(ConciliacaoContaCorrenteDiaVO objDia, List<ConciliacaoContaCorrenteDiaVO> lista) throws Exception {
		int index = 0;
		for (ConciliacaoContaCorrenteDiaVO objsExistente : lista) {
			if (UteisData.getCompararDatas(objsExistente.getData(), objDia.getData())) {
				lista.set(index, objDia);
				return;
			}
			index++;
		}
		lista.add(objDia);
	}

	
	private ConciliacaoContaCorrenteDiaVO consultarConciliacaoContaCorrenteDiaVO(Date data, List<ConciliacaoContaCorrenteDiaVO> lista) throws Exception {
		return lista.stream().filter(p-> UteisData.getCompararDatas(p.getData(), data)).findFirst().orElse(new ConciliacaoContaCorrenteDiaVO());
	}
	
	private ConciliacaoContaCorrenteDiaExtratoVO consultarConciliacaoContaCorrenteDiaExtratoVO(ConciliacaoContaCorrenteDiaVO objDia, Integer codigo) throws Exception {
		return objDia.getListaConciliacaoContaCorrenteExtrato().stream().filter(p-> p.getCodigo().equals(codigo)).findFirst().orElse(new ConciliacaoContaCorrenteDiaExtratoVO());
	}
	
	private void adicionarConciliacaoContaCorrenteDiaExtrato(ConciliacaoContaCorrenteDiaExtratoVO obj, ConciliacaoContaCorrenteDiaVO objDia) {
		int index = 0;
		for (ConciliacaoContaCorrenteDiaExtratoVO objsExistente : objDia.getListaConciliacaoContaCorrenteExtrato()) {
			if (objsExistente.getCodigo().equals(obj.getCodigo())) {
				objDia.getListaConciliacaoContaCorrenteExtrato().set(index, obj);
				return;
			}
			index++;
		}
		objDia.getListaConciliacaoContaCorrenteExtrato().add(obj);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer(" SELECT ");
		sql.append(" cccd.codigo as \"cccd.codigo\", cccd.data as \"cccd.data\", cccd.conciliacaocontacorrente as \"cccd.conciliacaocontacorrente\",  ");
		
		sql.append(" cccde.codigo as \"cccde.codigo\", cccde.conciliacaocontacorrentedia as \"cccde.conciliacaocontacorrentedia\",  ");
		sql.append(" cccde.tipotransacaoofxenum as \"cccde.tipotransacaoofxenum\", cccde.codigoofx as \"cccde.codigoofx\",  ");
		sql.append(" cccde.dataofx as \"cccde.dataofx\", cccde.valorofx as \"cccde.valorofx\", cccde.lancamentoofx as \"cccde.lancamentoofx\", ");
		sql.append(" cccde.documentoofx as \"cccde.documentoofx\", cccde.saldoregistroofx as \"cccde.saldoregistroofx\", cccde.codigosei as \"cccde.codigosei\", ");
		sql.append(" cccde.datasei as \"cccde.datasei\", cccde.valorsei as \"cccde.valorsei\", cccde.lancamentosei as \"cccde.lancamentosei\", ");
		sql.append(" cccde.saldoregistrosei as \"cccde.saldoregistrosei\", cccde.tipomovimentacaofinanceirasei as \"cccde.tipomovimentacaofinanceirasei\",  ");
		
		sql.append(" cccdec.codigo as \"cccdec.codigo\", cccdec.documentoofx as \"cccdec.documentoofx\",  ");
		sql.append(" cccdec.codigoofx as \"cccdec.codigoofx\", cccdec.valorofx as \"cccdec.valorofx\", cccdec.lancamentoofx as \"cccdec.lancamentoofx\" ");
		
		sql.append(" FROM conciliacaocontacorrentedia cccd  ");
		sql.append(" inner join conciliacaocontacorrentediaextrato cccde on cccde.conciliacaocontacorrentedia = cccd.codigo  ");
		sql.append(" left join conciliacaocontacorrentediaextratoconjunta cccdec on cccdec.conciliacaocontacorrentediaextrato = cccde.codigo  ");
		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConciliacaoContaCorrenteDiaVO> consultaRapidaPorConciliacaoContaCorrente(ConciliacaoContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE cccd.conciliacaocontacorrente = ").append(obj.getCodigo());
		sql.append(" ORDER BY cccd.data, cccde.codigoofx, cccde.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ConciliacaoContaCorrenteDiaVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			ConciliacaoContaCorrenteDiaVO cccd = consultarConciliacaoContaCorrenteDiaVO(tabelaResultado.getDate("cccd.data"), lista);
			montarDadosBasico(tabelaResultado, cccd);
			cccd.setConciliacaoContaCorrente(obj);
			addConciliacaoContaCorrenteDiaVO(cccd, lista);
		}
		return lista;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosBasico(SqlRowSet dadosSQL, ConciliacaoContaCorrenteDiaVO cccd) throws Exception {		
		if(!Uteis.isAtributoPreenchido(cccd.getData())){
			cccd.setCodigo(dadosSQL.getInt("cccd.codigo"));
			cccd.setData(dadosSQL.getDate("cccd.data"));
			cccd.getConciliacaoContaCorrente().setCodigo(dadosSQL.getInt("cccd.conciliacaocontacorrente"));
		}
		ConciliacaoContaCorrenteDiaExtratoVO cccde = consultarConciliacaoContaCorrenteDiaExtratoVO(cccd, dadosSQL.getInt("cccde.codigo"));
		if(!Uteis.isAtributoPreenchido(cccde.getCodigo())){
			cccde.setConciliacaoContaCorrenteDia(cccd);
			cccde.setCodigo(dadosSQL.getInt("cccde.codigo"));
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("cccde.tipotransacaoofxenum"))){
				cccde.setTipoTransacaoOFXEnum(TipoTransacaoOFXEnum.valueOf(dadosSQL.getString("cccde.tipotransacaoofxenum")));	
			}
			cccde.setCodigoOfx(dadosSQL.getInt("cccde.codigoofx"));
			if(Uteis.isAtributoPreenchido(dadosSQL.getDate("cccde.dataofx"))){
				cccde.setDataOfx(dadosSQL.getDate("cccde.dataofx"));	
			}
			cccde.setValorOfx(dadosSQL.getDouble("cccde.valorofx"));
			cccde.setLancamentoOfx(dadosSQL.getString("cccde.lancamentoofx"));
			cccde.setDocumentoOfx(dadosSQL.getString("cccde.documentoofx"));
			cccde.setSaldoRegistroOfx(dadosSQL.getDouble("cccde.saldoregistroofx"));
			
			
			cccde.setCodigoSei(dadosSQL.getString("cccde.codigosei"));
			if(Uteis.isAtributoPreenchido(dadosSQL.getDate("cccde.datasei"))){
				cccde.setDataSei(dadosSQL.getDate("cccde.datasei"));	
			}
			cccde.setLancamentoSei(dadosSQL.getString("cccde.lancamentosei"));
			cccde.setValorSei(dadosSQL.getDouble("cccde.valorsei"));
			cccde.setSaldoRegistroSei(dadosSQL.getDouble("cccde.saldoregistrosei"));
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("cccde.tipomovimentacaofinanceirasei"))){
				cccde.setTipoMovimentacaoFinanceiraSei(TipoMovimentacaoFinanceira.valueOf(dadosSQL.getString("cccde.tipomovimentacaofinanceirasei")));	
			}
			if (Uteis.isAtributoPreenchido(cccd.getListaCodigoExtratoContaCorrenteExistente()) && Uteis.isAtributoPreenchido(cccde.getCodigoSei())) {
				cccd.setListaCodigoExtratoContaCorrenteExistente(cccd.getListaCodigoExtratoContaCorrenteExistente() + "," + cccde.getCodigoSei());
			} else if(Uteis.isAtributoPreenchido(cccde.getCodigoSei())){
				cccd.setListaCodigoExtratoContaCorrenteExistente(cccde.getCodigoSei());
			}
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("cccdec.codigo"))){
			ConciliacaoContaCorrenteDiaExtratoConjuntaVO cccdec = new ConciliacaoContaCorrenteDiaExtratoConjuntaVO();
			cccdec.setConciliacaoContaCorrenteDiaExtrato(cccde);
			cccdec.setCodigo(dadosSQL.getInt("cccdec.codigo"));
			cccdec.setCodigoOfx(dadosSQL.getInt("cccdec.codigoofx"));
			cccdec.setValorOfx(dadosSQL.getDouble("cccdec.valorofx"));
			cccdec.setLancamentoOfx(dadosSQL.getString("cccdec.lancamentoofx"));
			cccdec.setDocumentoOfx(dadosSQL.getString("cccdec.documentoofx"));
			cccde.getListaConciliacaoContaConjuntaVO().add(cccdec);	
		}
		adicionarConciliacaoContaCorrenteDiaExtrato(cccde, cccd);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConciliacaoContaCorrenteDia.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConciliacaoContaCorrenteDia.idEntidade = idEntidade;
	}

}
