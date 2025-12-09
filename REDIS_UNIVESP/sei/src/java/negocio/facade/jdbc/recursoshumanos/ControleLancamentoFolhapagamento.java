package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.ControleLancamentoFolhapagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.interfaces.recursoshumanos.ControleLancamentoFolhapagamentoInterfaceFacade;

@Service
@Scope
@Lazy
public class ControleLancamentoFolhapagamento extends SuperFacade<ControleLancamentoFolhapagamentoVO> implements ControleLancamentoFolhapagamentoInterfaceFacade<ControleLancamentoFolhapagamentoVO> {

	private static final long serialVersionUID = 665981460994593603L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ControleLancamentoFolhapagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(ControleLancamentoFolhapagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleLancamentoFolhapagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);
		
		incluir(obj, "controlelancamentofolhapagamento", new AtributoPersistencia()
				.add("contracheque", obj.getContraCheque())
				.add("funcionarioCargo", obj.getFuncionarioCargo())
				.add("primeiraParcela13", obj.getPrimeiraParcela13())
				.add("segundaParcela13", obj.getSegundaParcela13())
				.add("competenciaFolhaPagamento", obj.getCompetenciaFolhaPagamentoVO())
				.add("anoCompetencia", obj.getAnoCompetencia())
				.add("mesCompetencia", obj.getMesCompetencia())
				.add("competenciaPeriodoFolhaPagamento", obj.getCompetenciaPeriodoFolhaPagamentoVO())
				.add("rescisao", obj.getRescisao())
				.add("contapagar", obj.getContaPagarVO())
				, usuarioVO);
	}

	@Override
	public void alterar(ControleLancamentoFolhapagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleLancamentoFolhapagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
		alterar(obj, "controlelancamentofolhapagamento", new AtributoPersistencia()
				.add("contracheque", obj.getContraCheque())
				.add("funcionarioCargo", obj.getFuncionarioCargo())
				.add("primeiraParcela13", obj.getPrimeiraParcela13())
				.add("segundaParcela13", obj.getSegundaParcela13())
				.add("competenciaFolhaPagamento", obj.getCompetenciaFolhaPagamentoVO())
				.add("anoCompetencia", obj.getAnoCompetencia())
				.add("mesCompetencia", obj.getMesCompetencia())
				.add("competenciaPeriodoFolhaPagamento", obj.getCompetenciaPeriodoFolhaPagamentoVO())
				.add("rescisao", obj.getRescisao())
				.add("contapagar", obj.getContaPagarVO())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
	}

	@Override
	public void excluir(ControleLancamentoFolhapagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleLancamentoFolhapagamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM controlelancamentofolhapagamento WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public void excluirPorLancamento(LancamentoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleLancamentoFolhapagamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM controlelancamentofolhapagamento WHERE ((lancamentofolhapagamento = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public ControleLancamentoFolhapagamentoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where codigo = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id.intValue());

        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}
	
	@Override
	public ControleLancamentoFolhapagamentoVO consultarPorFuncionarioCargoCompetenciaEPeriodo(FuncionarioCargoVO funcionarioCargo, CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, CompetenciaPeriodoFolhaPagamentoVO periodo) {
		
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where competenciaFolhaPagamento = ? and competenciaPeriodoFolhaPagamento = ? and funcionariocargo = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),competenciaFolhaPagamentoVO.getCodigo(), periodo.getCodigo(), funcionarioCargo.getCodigo());

        try {
        	
        	if (tabelaResultado.next())
        		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
        	
        } catch (Exception e) {
        	e.printStackTrace();
		}
        
        return new ControleLancamentoFolhapagamentoVO();
	}

	/**
	 * Consultar lista de {@link ControleLancamentoFolhapagamentoVO} pelo codigo do {@link ContraChequeVO}.
	 */
	@Override
	public List<ControleLancamentoFolhapagamentoVO> consultarPorContraCheque(Integer codigoContraCheque) throws Exception {

		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where contracheque = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoContraCheque);

		List<ControleLancamentoFolhapagamentoVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
		}

		return lista;
	}
	
	@Override
	public ControleLancamentoFolhapagamentoVO consultarPorContraCheque(ContraChequeVO contraChequeVO) throws Exception {
		
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where contraCheque = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{contraChequeVO.getCodigo()});

        if (!tabelaResultado.next()) {
            return new ControleLancamentoFolhapagamentoVO();
        }
        return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	private String getSQLBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM controlelancamentofolhapagamento ");
		return sql.toString();
	}

	public String getSQLBasicoTotal() {
		StringBuilder sql = new StringBuilder();
		return sql.append(" SELECT COUNT(codigo) as qtde FROM public.controlelancamentofolhapagamento").toString();
	}

	@Override
	public void validarDados(ControleLancamentoFolhapagamentoVO obj) throws ConsistirException {
	}

	@Override
	public ControleLancamentoFolhapagamentoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		ControleLancamentoFolhapagamentoVO obj = new ControleLancamentoFolhapagamentoVO();
		
		obj.setCodigo(tabelaResultado.getInt("codigo"));

		obj.getContraCheque().setCodigo(tabelaResultado.getInt("contracheque"));
		obj.getFuncionarioCargo().setCodigo(tabelaResultado.getInt("funcionarioCargo"));
		obj.setPrimeiraParcela13(tabelaResultado.getBoolean("primeiraParcela13"));
		obj.setSegundaParcela13(tabelaResultado.getBoolean("segundaParcela13"));
		obj.getCompetenciaFolhaPagamentoVO().setCodigo(tabelaResultado.getInt("competenciaFolhaPagamento"));
		obj.getCompetenciaPeriodoFolhaPagamentoVO().setCodigo(tabelaResultado.getInt("competenciaPeriodoFolhaPagamento"));

		obj.setAnoCompetencia(tabelaResultado.getInt("anoCompetencia"));
		obj.setMesCompetencia(tabelaResultado.getInt("mesCompetencia"));
		
		obj.setRescisao(tabelaResultado.getBoolean("rescisao"));
		
		if(nivelMontarDados != Uteis.NIVELMONTARDADOS_TODOS)
			return obj;
		
		obj.setContraCheque(Uteis.montarDadosVO(tabelaResultado.getInt("contracheque"), ContraChequeVO.class, p -> getFacadeFactory().getContraChequeInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
		obj.setFuncionarioCargo(Uteis.montarDadosVO(tabelaResultado.getInt("funcionarioCargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, null)));
		obj.setCompetenciaFolhaPagamentoVO(Uteis.montarDadosVO(tabelaResultado.getInt("competenciaFolhaPagamento"), CompetenciaFolhaPagamentoVO.class, p -> getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
		obj.setCompetenciaPeriodoFolhaPagamentoVO(Uteis.montarDadosVO(tabelaResultado.getInt("competenciaPeriodoFolhaPagamento"), CompetenciaPeriodoFolhaPagamentoVO.class, p -> getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
		
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("contapagar"))) {
			obj.setContaPagarVO(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(tabelaResultado.getInt("contapagar"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		
		return obj;
	}

	@Override
	public boolean consultarDecimoTerceiroJaLancado(FuncionarioCargoVO funcionarioCargoVO, Integer anoCompetencia) {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where funcionarioCargo = ? ");
		sql.append(" and anoCompetencia = ? ");
		sql.append(" and (primeiraParcela13 = true or segundaParcela13 = true) ");
		
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{funcionarioCargoVO.getCodigo(), anoCompetencia});

        return tabelaResultado.next() ? Boolean.TRUE : Boolean.FALSE ;
	}

	/**
	 * Valida se a segunda parcela do 13 foi lancado para o ano em questao
	 * 
	 * @param funcionarioCargo
	 * @param anoCompetencia
	 * @return
	 * true: segunda parcela ja foi lancada para esse funcionario e para o ano da competencia
	 * false: segunda parcela nao foi lancada para esse funcionario e para o ano da competencia
	 */
	@Override
	public boolean validarDecimoTerceiroSegundaParcelaLancada(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia) {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where funcionarioCargo = ? ");
		sql.append(" and anoCompetencia = ? ");
		sql.append(" and segundaParcela13 = true ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{funcionarioCargo.getCodigo(), anoCompetencia});

        return tabelaResultado.next() ? Boolean.TRUE : Boolean.FALSE ;
	}

	@Override
	public ControleLancamentoFolhapagamentoVO consultarControlePrimeiraParcela13(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia) {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where funcionarioCargo = ? ");
		sql.append(" and anoCompetencia = ? ");
		sql.append(" and primeiraParcela13 = true ");
		
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargo.getCodigo(), anoCompetencia);

        try {
        	if (tabelaResultado.next()) {
                return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
            }        	
        }catch (Exception e) {
        	e.printStackTrace();
		}
        
        return new ControleLancamentoFolhapagamentoVO();
	}

	@Override
	public void cancelarControleLancamentoFolhaPagamento(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, UsuarioVO usuarioLogado) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" update controlelancamentofolhapagamento controle set primeiraParcela13 = false, ");
		sql.append(" segundaParcela13 = false,  rescisao = false ");
		sql.append(" from funcionariocargo as fc where fc.codigo = controle.funcionariocargo ");
		sql.append(" and controle.competenciafolhapagamento = ? and controle.competenciaPeriodoFolhaPagamento = ? ");
		
		sql.append(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento()));
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		
		getConexao().getJdbcTemplate().update(sql.toString(), lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO().getCodigo(), lancamentoFolhaPagamento.getPeriodo().getCodigo());
	}

	@Override
	public Boolean validarRescisaoNaoFoiLancada(FuncionarioCargoVO funcionarioCargo, Integer anoCompetencia) {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" where funcionarioCargo = ? ");
		sql.append(" and anoCompetencia = ? ");
		sql.append(" and rescisao = true ");
		
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargo.getCodigo(), anoCompetencia);

        return tabelaResultado.next()  ? Boolean.TRUE : Boolean.FALSE;
	}
	
	
	@Override
	public void atualizarContaPagarDoContraCheque(ControleLancamentoFolhapagamentoVO controleLancamentoFolhapagamentoVO, UsuarioVO usuarioLogado) throws ConsistirException {
		try {
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

					final StringBuilder sql = new StringBuilder(" UPDATE controleLancamentoFolhapagamento SET contapagar=? WHERE codigo = ?").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));

					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(controleLancamentoFolhapagamentoVO.getContaPagarVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(controleLancamentoFolhapagamentoVO.getCodigo(), ++i, sqlAlterar);

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraCheque_erroAoSalvar"));
		}
	}

	@Override
	public void cancelarContaPagarPorCodigo(int codigoContaPagar, UsuarioVO usuarioLogado) throws ConsistirException {
		try {
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					
					final StringBuilder sql = new StringBuilder(" UPDATE controleLancamentoFolhapagamento SET contapagar=null WHERE contapagar = ?").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
					
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					
					int i = 0;
					Uteis.setValuePreparedStatement(codigoContaPagar, ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraCheque_erroAoSalvar"));
		}
	}
}