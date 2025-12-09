package negocio.facade.jdbc.financeiro;

import java.math.BigDecimal;
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
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.IndiceReajustePeriodoVO;
import negocio.comuns.financeiro.IndiceReajusteVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.IndiceReajusteInterfaceFacade;

@Lazy
@Repository
@Scope("singleton")
public class IndiceReajuste extends ControleAcesso implements IndiceReajusteInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public IndiceReajuste() throws Exception {
		super();
		setIdEntidade("IndiceReajuste");
	}

	/**
	 * @author Carlos
	 * @param atividadeDiscursivaVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(IndiceReajusteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.isNovoObj()) {
			incluir(obj, usuarioVO);
		} else {
			alterar(obj, usuarioVO);
		}
	}

	public void validarDados(IndiceReajusteVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getDescricao().equals("")) {
			throw new Exception("O campo DESCRIÇÃO deve ser informado!");
		}
	}

	public void validarUnicidadeDescricao(IndiceReajusteVO obj, UsuarioVO usuario) throws Exception {
		if (consultarExistenciaDescricaoIndiceReajuste(obj, usuario)) {
			throw new Exception("Não foi possível concluir a operação porque já existe um Índice de Reajuste com a descrição: " + obj.getDescricao().toUpperCase());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final IndiceReajusteVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj, usuario);
			validarUnicidadeDescricao(obj, usuario);
			IndiceReajuste.incluir(getIdEntidade(), usuario);
			final String sql = "INSERT INTO IndiceReajuste( descricao) VALUES ( ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getIndiceReajustePeriodoFacade().incluirIndiceReajustePeriodoVOs(obj.getCodigo(), obj.getListaIndiceReajustePeriodoVOs(), usuario);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final IndiceReajusteVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj, usuario);
			validarUnicidadeDescricao(obj, usuario);
			IndiceReajuste.alterar(getIdEntidade(), usuario);
			final String sql = "UPDATE IndiceReajuste set descricao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setInt(2, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getIndiceReajustePeriodoFacade().alterarIndiceReajustePeriodoVOs(obj.getCodigo(), obj.getListaIndiceReajustePeriodoVOs(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	public static void aplicacaoIndiceReajusteContaReceberPorAtraso(Integer codigoIndiceReajuste, Double valorBase, Date dataCalcular, Date dataVencimentoParcela, ContaReceberVO obj) throws Exception{
		Double valorAplicado = getFacadeFactory().getIndiceReajusteFacade().realizarCalculoIndiceReajuste(codigoIndiceReajuste, valorBase, dataCalcular, dataVencimentoParcela, null);
		if(valorAplicado != null && valorAplicado > 0.0){
			obj.setValorIndiceReajustePorAtraso(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(valorAplicado - valorBase)));
		}else if(valorAplicado != null && valorAplicado <= 0.0){
			obj.setValorIndiceReajustePorAtraso(BigDecimal.ZERO);
		}
		obj.setIndiceReajustePadraoPorAtraso(getFacadeFactory().getIndiceReajusteFacade().consultarPorChavePrimaria(codigoIndiceReajuste, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(IndiceReajusteVO obj, UsuarioVO usuario) throws Exception {
		try {
			IndiceReajuste.excluir(getIdEntidade(), true, usuario);
			getFacadeFactory().getIndiceReajustePeriodoFacade().excluirIndiceReajustePeriodoPorIndiceReajuste(obj, usuario);
			String sql = "DELETE FROM IndiceReajuste WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<IndiceReajusteVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuarioVO) {
		List<IndiceReajusteVO> listaIndiceReajusteVOs = new ArrayList<IndiceReajusteVO>(0);
		if (campoConsulta.equals("DESCRICAO")) {
			listaIndiceReajusteVOs = consultarPorDescricao(valorConsulta, usuarioVO);
		}
		return listaIndiceReajusteVOs;

	}

	@Override
	public List<IndiceReajusteVO> consultarPorDescricao(String descricao, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, descricao from indiceReajuste where descricao ilike '").append(descricao).append("%' order by descricao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<IndiceReajusteVO> listaIndiceReajusteVOs = new ArrayList<IndiceReajusteVO>(0);
		while (tabelaResultado.next()) {
			IndiceReajusteVO obj = new IndiceReajusteVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			listaIndiceReajusteVOs.add(obj);
		}
		return listaIndiceReajusteVOs;
	}

	@Override
	public void adicionarIndiceReajustePeriodo(List<IndiceReajustePeriodoVO> listaIndiceReajusteVOs, IndiceReajustePeriodoVO objIncluir, UsuarioVO usuarioVO) throws Exception {
		if (objIncluir.getAno() == null || objIncluir.getAno().equals("")) {
			throw new Exception("O campo ANO deve ser informado!");
		}
		objIncluir.setResponsavelVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(usuarioVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
		int index = 0;
		for (IndiceReajustePeriodoVO indiceReajustePeriodoVO : listaIndiceReajusteVOs) {
			if (objIncluir.getMes().equals(indiceReajustePeriodoVO.getMes()) && objIncluir.getAno().equals(indiceReajustePeriodoVO.getAno()) && indiceReajustePeriodoVO.getSituacaoExecucao().equals(SituacaoExecucaoEnum.AGUARDANDO_PROCESSAMENTO)) {
				listaIndiceReajusteVOs.set(index, objIncluir);
				return;
			}
			if (objIncluir.getMes().equals(indiceReajustePeriodoVO.getMes()) && objIncluir.getAno().equals(indiceReajustePeriodoVO.getAno()) && indiceReajustePeriodoVO.getSituacaoExecucao().equals(SituacaoExecucaoEnum.EM_PROCESSAMENTO)) {
				throw new Exception("Índice " + indiceReajustePeriodoVO.getMes().getMes() + " / " + indiceReajustePeriodoVO.getAno() + " em PROCESSAMENTO, favor Aguardar até concluir a operação!");
			}
			if (objIncluir.getMes().equals(indiceReajustePeriodoVO.getMes()) && objIncluir.getAno().equals(indiceReajustePeriodoVO.getAno()) && indiceReajustePeriodoVO.getSituacaoExecucao().equals(SituacaoExecucaoEnum.PROCESSADO)) {
				throw new Exception("Índice " + indiceReajustePeriodoVO.getMes().getMes() + " / " + indiceReajustePeriodoVO.getAno() + " já PROCESSADO!");
			}
			index++;
		}
		listaIndiceReajusteVOs.add(objIncluir);
		Ordenacao.ordenarListaDecrescente(listaIndiceReajusteVOs, "ordenacaoMes");

	}

	@Override
	public void removerIndiceReajustePeriodo(List<IndiceReajustePeriodoVO> listaIndiceReajusteVOs, IndiceReajustePeriodoVO objIncluir, UsuarioVO usuarioVO) throws Exception {
		int index = 0;
		for (IndiceReajustePeriodoVO indiceReajustePeriodoVO : listaIndiceReajusteVOs) {
			if (objIncluir.getMes().equals(indiceReajustePeriodoVO.getMes()) && objIncluir.getAno().equals(indiceReajustePeriodoVO.getAno()) && indiceReajustePeriodoVO.getSituacaoExecucao().equals(SituacaoExecucaoEnum.AGUARDANDO_PROCESSAMENTO)) {
				listaIndiceReajusteVOs.remove(index);
				return;
			}
			if (objIncluir.getMes().equals(indiceReajustePeriodoVO.getMes()) && objIncluir.getAno().equals(indiceReajustePeriodoVO.getAno()) && indiceReajustePeriodoVO.getSituacaoExecucao().equals(SituacaoExecucaoEnum.EM_PROCESSAMENTO)) {
				throw new Exception("Índice " + indiceReajustePeriodoVO.getMes().getMes() + " / " + indiceReajustePeriodoVO.getAno() + " em PROCESSAMENTO, favor Aguardar até concluir a operação!");
			}
			if (objIncluir.getMes().equals(indiceReajustePeriodoVO.getMes()) && objIncluir.getAno().equals(indiceReajustePeriodoVO.getAno()) && indiceReajustePeriodoVO.getSituacaoExecucao().equals(SituacaoExecucaoEnum.PROCESSADO)) {
				throw new Exception("Índice " + indiceReajustePeriodoVO.getMes().getMes() + " / " + indiceReajustePeriodoVO.getAno() + " já PROCESSADO!");
			}
			index++;
		}
	}

	@Override
	public List<String> montarListaSelectItemAno(String anoAtual, UsuarioVO usuarioVO) {
		List<String> listaAnoVOs = new ArrayList<String>(0);
		Integer ano = Integer.parseInt(anoAtual);
		ano--;
		for (int i = 0; i < 10; i++) {
			listaAnoVOs.add(ano.toString());
			ano++;
		}
		return listaAnoVOs;
	}
	
	@Override
	public Double realizarCalculoIndiceReajuste(Integer indiceReajuste, Double valorBase, Date dataCalcular, Date dataVencimentoParcela, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select calcularIndiceReajuste( ");
		sb.append(valorBase).append(", ");
		sb.append("'").append(Uteis.getDataJDBC(dataVencimentoParcela)).append("', ");
		sb.append("'").append(Uteis.getDataJDBC(dataCalcular)).append("', ");
		sb.append(indiceReajuste).append(") ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("calcularindicereajuste");
		}
		return 0.0;
	}


	public Boolean consultarExistenciaDescricaoIndiceReajuste(IndiceReajusteVO obj, UsuarioVO usuario) {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo from indicereajuste where sem_acentos(trim(descricao)) ilike sem_acentos(trim('").append(obj.getDescricao()).append("'))");
		sb.append(" and codigo != ").append(obj.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public IndiceReajusteVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemCondicaoDescontoRenegociacao.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM indicereajuste WHERE codigo = " + codigoPrm;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( indicereajuste ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public void carregarDados(IndiceReajusteVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((IndiceReajusteVO) obj, NivelMontarDados.TODOS, usuario);
	}

	@Override
	public void carregarDados(IndiceReajusteVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico((IndiceReajusteVO) obj, resultado, usuario);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto((IndiceReajusteVO) obj, resultado, usuario);
		}
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codIndiceReajuste, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (IndiceReajuste.codigo= ").append(codIndiceReajuste).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, descricao from indiceReajuste ");
		return sb;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codIndiceReajuste, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (IndiceReajuste.codigo= ").append(codIndiceReajuste).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return tabelaResultado;
	}

	private StringBuilder getSQLPadraoConsultaCompleta() {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, descricao from indiceReajuste ");
		return sb;
	}

	private IndiceReajusteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		IndiceReajusteVO obj = new IndiceReajusteVO();
		obj.setNovoObj((false));
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		return obj;
	}
	
	private void montarDadosBasico(IndiceReajusteVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
	}

	private void montarDadosCompleto(IndiceReajusteVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setListaIndiceReajustePeriodoVOs(getFacadeFactory().getIndiceReajustePeriodoFacade().consultarIndiceReajustePeriodoPorIndiceReajuste(obj.getCodigo(), usuarioVO));
	}

	public static String getIdEntidade() {
		return IndiceReajuste.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		IndiceReajuste.idEntidade = idEntidade;
	}
}
