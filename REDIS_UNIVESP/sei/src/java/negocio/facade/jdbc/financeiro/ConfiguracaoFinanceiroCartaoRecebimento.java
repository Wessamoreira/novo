package negocio.facade.jdbc.financeiro;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoRecebimentoVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.VisaoParcelarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ConfiguracaoFinanceiroCartaoRecebimentoInterfaceFacade;

/**
 * @author Victor Hugo de Paula Costa 20/05/2015
 */
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoFinanceiroCartaoRecebimento extends ControleAcesso implements ConfiguracaoFinanceiroCartaoRecebimentoInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void validarDados(ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO, ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO2) throws ConsistirException  {
		configuracaoFinanceiroCartaoRecebimentoVO.setDescricaoOrigemParcelas(null);
		configuracaoFinanceiroCartaoRecebimentoVO.setListaDescricaoOrigemParcelas(null);		
		if(configuracaoFinanceiroCartaoRecebimentoVO.getVisao().equals(configuracaoFinanceiroCartaoRecebimentoVO2.getVisao())
				|| (configuracaoFinanceiroCartaoRecebimentoVO.getVisao().equals(VisaoParcelarEnum.TODAS) && !configuracaoFinanceiroCartaoRecebimentoVO2.getVisao().equals(VisaoParcelarEnum.TODAS))
				|| (!configuracaoFinanceiroCartaoRecebimentoVO.getVisao().equals(VisaoParcelarEnum.TODAS) && configuracaoFinanceiroCartaoRecebimentoVO2.getVisao().equals(VisaoParcelarEnum.TODAS))) {
		if (configuracaoFinanceiroCartaoRecebimentoVO.getParcelasAte() < configuracaoFinanceiroCartaoRecebimentoVO2.getParcelasAte()) {
			if(configuracaoFinanceiroCartaoRecebimentoVO.getParcelasAte().equals(configuracaoFinanceiroCartaoRecebimentoVO2.getParcelasAte())					
					&& configuracaoFinanceiroCartaoRecebimentoVO.getListaDescricaoOrigemParcelas().containsAll(configuracaoFinanceiroCartaoRecebimentoVO2.getListaDescricaoOrigemParcelas())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoFinanceiroCartaoRecebimento_jaExisteUmaParcelaComEsteParametro"));				
			}
		}
		if( (configuracaoFinanceiroCartaoRecebimentoVO.getParcelasAte().equals(configuracaoFinanceiroCartaoRecebimentoVO2.getParcelasAte())) 
				&& !configuracaoFinanceiroCartaoRecebimentoVO2.getItemEmEdicao()
				&& configuracaoFinanceiroCartaoRecebimentoVO.getListaDescricaoOrigemParcelas().containsAll(configuracaoFinanceiroCartaoRecebimentoVO2.getListaDescricaoOrigemParcelas())) {			
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoFinanceiroCartaoRecebimento_jaExisteUmaParcelaComEsteParametro"));				
		}
		if (configuracaoFinanceiroCartaoRecebimentoVO.getParcelasAte().equals(0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoFinanceiroCartaoRecebimento_oValorDaParcelaDeveSerMaiorQue0"));
		}
		
		if(configuracaoFinanceiroCartaoRecebimentoVO.getQtdeDiasInicialParcelarContaVencida() < 0 ||
				configuracaoFinanceiroCartaoRecebimentoVO.getQtdeDiasFinalParcelarContaVencida() < 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoFinanceiroCartaoRecebimento_aQtdeDiasParcelarNegativo"));				
		}
		
		if(configuracaoFinanceiroCartaoRecebimentoVO.getQtdeDiasInicialParcelarContaVencida() > configuracaoFinanceiroCartaoRecebimentoVO.getQtdeDiasFinalParcelarContaVencida()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoFinanceiroCartaoRecebimento_aQtdeDiasInicialMaiorQueFinal"));				
		}
		
		if(configuracaoFinanceiroCartaoRecebimentoVO.getValorMinimo().equals(configuracaoFinanceiroCartaoRecebimentoVO2.getValorMinimo())				
					&& configuracaoFinanceiroCartaoRecebimentoVO.getListaDescricaoOrigemParcelas().containsAll(configuracaoFinanceiroCartaoRecebimentoVO2.getListaDescricaoOrigemParcelas())
					&& configuracaoFinanceiroCartaoRecebimentoVO.getQtdeDiasInicialParcelarContaVencida() <= configuracaoFinanceiroCartaoRecebimentoVO2.getQtdeDiasInicialParcelarContaVencida()
					&& !configuracaoFinanceiroCartaoRecebimentoVO2.getItemEmEdicao()
					&& configuracaoFinanceiroCartaoRecebimentoVO.getQtdeDiasFinalParcelarContaVencida() >= configuracaoFinanceiroCartaoRecebimentoVO2.getQtdeDiasFinalParcelarContaVencida()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ConfiguracaoFinanceiroCartaoRecebimento_jaExisteUmValorMinimoComEsteParametro"));				
	}
	
		
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoFinanceiroCartaoRecebimentoVO recebimentoAdministrativoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (recebimentoAdministrativoVO.getCodigo().equals(0)) {
			incluir(recebimentoAdministrativoVO, verificarAcesso, usuarioVO);
		} else {
			alterar(recebimentoAdministrativoVO, verificarAcesso, usuarioVO);
		}
	}
	
	@Override
	public void persistirRebimentoAdministrativoVOs(List<ConfiguracaoFinanceiroCartaoRecebimentoVO> recebimentoAdministrativoVOs, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (ConfiguracaoFinanceiroCartaoRecebimentoVO recebimentoAdministrativoVO : recebimentoAdministrativoVOs) {
			persistir(recebimentoAdministrativoVO, verificarAcesso, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoFinanceiroCartaoRecebimento.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO configuracaofinanceirocartaorecebimento ");
			sql.append(" (parcelasate, valorminimo, tipofinanciamento, configuracaoRecebimentoCartaoOnline, qtdeDiasInicialParcelarContaVencida, qtdeDiasFinalParcelarContaVencida, ");
			sql.append(" visao, tipoorigemcontarecebermatricula, tipoorigemcontarecebermensalidade, tipoorigemcontarecebermaterialdidatico, tipoorigemcontareceberinscricaoprocessoseletivo, tipoorigemcontareceberrequerimento, ");
			sql.append(" tipoorigemcontareceberbiblioteca, tipoorigemcontarecebernegociacao, tipoorigemcontareceberoutros, tipoorigemcontareceberdevolucaocheque, tipoorigemcontareceberbolsacusteada, tipoorigemcontarecebercontratoreceita, tipoorigemcontareceberinclusaoreposicao  )");
			sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "); //10
			sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			
			configuracaoFinanceiroCartaoRecebimentoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					sqlInserir.setInt(1, configuracaoFinanceiroCartaoRecebimentoVO.getParcelasAte());
					sqlInserir.setDouble(2, configuracaoFinanceiroCartaoRecebimentoVO.getValorMinimo());
					sqlInserir.setString(3, configuracaoFinanceiroCartaoRecebimentoVO.getTipoFinanciamentoEnum().getName());
					sqlInserir.setInt(4, configuracaoFinanceiroCartaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo());
					sqlInserir.setInt(5, configuracaoFinanceiroCartaoRecebimentoVO.getQtdeDiasInicialParcelarContaVencida());
					sqlInserir.setInt(6, configuracaoFinanceiroCartaoRecebimentoVO.getQtdeDiasFinalParcelarContaVencida());
					sqlInserir.setString(7, configuracaoFinanceiroCartaoRecebimentoVO.getVisao().name());
					sqlInserir.setBoolean(8, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberMatricula());
					sqlInserir.setBoolean(9, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberMensalidade());
					sqlInserir.setBoolean(10, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberMaterialDidatico());
					sqlInserir.setBoolean(11, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberInscricaoProcessoSeletivo());
					sqlInserir.setBoolean(12, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberRequerimento());
					sqlInserir.setBoolean(13, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberBiblioteca());
					sqlInserir.setBoolean(14, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberNegociacao());
					sqlInserir.setBoolean(15, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberOutros());
					sqlInserir.setBoolean(16, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberDevolucaoCheque());
					sqlInserir.setBoolean(17, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberBolsaCusteada());
					sqlInserir.setBoolean(18, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberContratoReceita());
					sqlInserir.setBoolean(19, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberInclusaoReposicao());
					return sqlInserir;
				}

			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						configuracaoFinanceiroCartaoRecebimentoVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			configuracaoFinanceiroCartaoRecebimentoVO.setNovoObj(Boolean.TRUE);
			configuracaoFinanceiroCartaoRecebimentoVO.setCodigo(0);
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoFinanceiroCartaoRecebimento.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE configuracaofinanceirocartaorecebimento SET");
			sql.append(" parcelasate=?, valorminimo=?, tipofinanciamento=?, configuracaoRecebimentoCartaoOnline=?, qtdeDiasInicialParcelarContaVencida=?,");
			sql.append(" qtdeDiasFinalParcelarContaVencida=?, visao=?, tipoorigemcontarecebermatricula=?,  tipoorigemcontarecebermensalidade=?, tipoorigemcontarecebermaterialdidatico=?, ");
			sql.append(" tipoorigemcontareceberinscricaoprocessoseletivo=?, tipoorigemcontareceberrequerimento=?, tipoorigemcontareceberbiblioteca=?, tipoorigemcontarecebernegociacao=?, tipoorigemcontareceberoutros=?, ");
			sql.append(" tipoorigemcontareceberdevolucaocheque=?, tipoorigemcontareceberbolsacusteada=?, tipoorigemcontarecebercontratoreceita=?, tipoorigemcontareceberinclusaoreposicao=? ");
			sql.append(" WHERE ((codigo = ?))");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

			if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, configuracaoFinanceiroCartaoRecebimentoVO.getParcelasAte());
					sqlAlterar.setDouble(2, configuracaoFinanceiroCartaoRecebimentoVO.getValorMinimo());
					sqlAlterar.setString(3, configuracaoFinanceiroCartaoRecebimentoVO.getTipoFinanciamentoEnum().getName());
					sqlAlterar.setInt(4, configuracaoFinanceiroCartaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo());
					sqlAlterar.setInt(5, configuracaoFinanceiroCartaoRecebimentoVO.getQtdeDiasInicialParcelarContaVencida());
					sqlAlterar.setInt(6, configuracaoFinanceiroCartaoRecebimentoVO.getQtdeDiasFinalParcelarContaVencida());
					sqlAlterar.setString(7, configuracaoFinanceiroCartaoRecebimentoVO.getVisao().name());
					sqlAlterar.setBoolean(8, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberMatricula());
					sqlAlterar.setBoolean(9, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberMensalidade());
					sqlAlterar.setBoolean(10, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberMaterialDidatico());
					sqlAlterar.setBoolean(11, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberInscricaoProcessoSeletivo());
					sqlAlterar.setBoolean(12, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberRequerimento());
					sqlAlterar.setBoolean(13, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberBiblioteca());
					sqlAlterar.setBoolean(14, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberNegociacao());
					sqlAlterar.setBoolean(15, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberOutros());
					sqlAlterar.setBoolean(16, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberDevolucaoCheque());
					sqlAlterar.setBoolean(17, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberBolsaCusteada());
					sqlAlterar.setBoolean(18, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberContratoReceita());
					sqlAlterar.setBoolean(19, configuracaoFinanceiroCartaoRecebimentoVO.getTipoOrigemContaReceberInclusaoReposicao());
					
					sqlAlterar.setInt(20, configuracaoFinanceiroCartaoRecebimentoVO.getCodigo());
					return sqlAlterar;
				}
			}) == 0){
				incluir(configuracaoFinanceiroCartaoRecebimentoVO, verificarAcesso, usuarioVO);
				return;
			};
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ConfiguracaoFinanceiroCartaoRecebimentoVO configuracaoFinanceiroCartaoRecebimentoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoFinanceiroCartaoRecebimento.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM configuracaofinanceirocartaorecebimento WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, configuracaoFinanceiroCartaoRecebimentoVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	public ConfiguracaoFinanceiroCartaoRecebimentoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ConfiguracaoFinanceiroCartaoRecebimentoVO obj = new ConfiguracaoFinanceiroCartaoRecebimentoVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setParcelasAte(tabelaResultado.getInt("parcelasate"));
		obj.setValorMinimo(tabelaResultado.getDouble("valorminimo"));
		obj.setTipoFinanciamentoEnum(TipoFinanciamentoEnum.valueOf(tabelaResultado.getString("tipofinanciamento")));
		obj.setQtdeDiasInicialParcelarContaVencida(tabelaResultado.getInt("qtdeDiasInicialParcelarContaVencida"));
		obj.setQtdeDiasFinalParcelarContaVencida(tabelaResultado.getInt("qtdeDiasFinalParcelarContaVencida"));
		obj.setVisao(VisaoParcelarEnum.valueOf(tabelaResultado.getString("visao")));
		obj.getConfiguracaoRecebimentoCartaoOnlineVO().setCodigo(tabelaResultado.getInt("configuracaoRecebimentoCartaoOnline"));

		obj.setTipoOrigemContaReceberMatricula(tabelaResultado.getBoolean("tipoorigemcontarecebermatricula"));
		obj.setTipoOrigemContaReceberMensalidade(tabelaResultado.getBoolean("tipoorigemcontarecebermensalidade"));
		obj.setTipoOrigemContaReceberMaterialDidatico(tabelaResultado.getBoolean("tipoorigemcontarecebermaterialdidatico"));
		obj.setTipoOrigemContaReceberInscricaoProcessoSeletivo(tabelaResultado.getBoolean("tipoorigemcontareceberinscricaoprocessoseletivo"));
		obj.setTipoOrigemContaReceberRequerimento(tabelaResultado.getBoolean("tipoorigemcontareceberrequerimento"));
		obj.setTipoOrigemContaReceberBiblioteca(tabelaResultado.getBoolean("tipoorigemcontareceberbiblioteca"));
		obj.setTipoOrigemContaReceberNegociacao(tabelaResultado.getBoolean("tipoorigemcontarecebernegociacao"));
		obj.setTipoOrigemContaReceberOutros(tabelaResultado.getBoolean("tipoorigemcontareceberoutros"));
		obj.setTipoOrigemContaReceberDevolucaoCheque(tabelaResultado.getBoolean("tipoorigemcontareceberdevolucaocheque"));
		obj.setTipoOrigemContaReceberBolsaCusteada(tabelaResultado.getBoolean("tipoorigemcontareceberbolsacusteada"));
		obj.setTipoOrigemContaReceberContratoReceita(tabelaResultado.getBoolean("tipoorigemcontarecebercontratoreceita"));
		obj.setTipoOrigemContaReceberInclusaoReposicao(tabelaResultado.getBoolean("tipoorigemcontareceberinclusaoreposicao"));
		return obj;
	}
	
	
	public List<ConfiguracaoFinanceiroCartaoRecebimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ConfiguracaoFinanceiroCartaoRecebimentoVO> vetResultado = new ArrayList<ConfiguracaoFinanceiroCartaoRecebimentoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConfiguracaoFinanceiroCartaoRecebimentoVO> consultarPorCodigoConfiguracaoRecebimentoCartaoOnlineVO(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM configuracaofinanceirocartaorecebimento WHERE configuracaoRecebimentoCartaoOnline = ? order by parcelasate";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		return montarDadosConsulta(rs, nivelMontarDados, usuarioLogado);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorCodigoConfiguracaoRecebimentoCartaoOnline(final int codigoConfiguracaoRecebimentoCartaoOnline, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoFinanceiroCartaoRecebimento.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM configuracaofinanceirocartaorecebimento WHERE ((configuracaorecebimentocartaoonline = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, codigoConfiguracaoRecebimentoCartaoOnline);
		} catch (Exception e) {
			throw e;
		}
	}
}
