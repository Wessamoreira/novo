package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeEventoRelVO;
import negocio.comuns.recursoshumanos.ContraChequeEventoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.CategoriaEventoFolhaEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoLancamentoFolhaPagamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.recursoshumanos.ContraChequeEventoInterfaceFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class ContraChequeEvento extends SuperFacade<ContraChequeEventoVO> implements ContraChequeEventoInterfaceFacade<ContraChequeEventoVO> {

	private static final long serialVersionUID = -40479772822196253L;

	protected static String idEntidade;

	public ContraChequeEvento() throws Exception {
		super();
		setIdEntidade("ContraChequeEvento");
	}

	@Override
	public void persistir(ContraChequeEventoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void persistirTodos(ContraChequeVO obj, boolean b, UsuarioVO usuario) throws Exception {
		
		for(ContraChequeEventoVO contraChequeRecalculado : obj.getContraChequeEventos()) {
			contraChequeRecalculado.setContraCheque(obj);
			contraChequeRecalculado.setPeriodo(obj.getPeriodo());
			persistir(contraChequeRecalculado, false, usuario);
		}
		
		excluirTodosQueNaoEstaoNaListaContraChequeEventoEPeriodo(obj, false, usuario);
	}

	/**
	 * Exclui todos os objetos removidos da lista contra cheque evento.
	 * 
	 * @param obj
	 * @param validarAcesso
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void excluirTodosQueNaoEstaoNaListaContraChequeEventoEPeriodo(ContraChequeVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		ContraChequeEvento.excluir(getIdEntidade(), validarAcesso, usuario);
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(obj.getCodigo());
		
		Iterator<ContraChequeEventoVO> i = obj.getContraChequeEventos().iterator();

		StringBuilder str = new StringBuilder("DELETE FROM contrachequeevento WHERE contracheque = ? ");
	    while (i.hasNext()) {
	    	ContraChequeEventoVO objeto = (ContraChequeEventoVO) i.next();
	    	str.append(" AND codigo <> ? ");
	    	condicao.add(objeto.getCodigo());
	    }
	    
	    str.append(" AND periodo = ? ");
	    condicao.add(obj.getPeriodo().getCodigo());
	    
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
	}
	
	/**
	 * Exclui todos os objetos removidos da lista contra cheque evento.
	 * 
	 * @param obj
	 * @param validarAcesso
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void excluirContraChequeEventoDoContrachequeEPeriodo(ContraChequeVO obj, CompetenciaPeriodoFolhaPagamentoVO periodo, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		ContraChequeEvento.excluir(getIdEntidade(), validarAcesso, usuario);
		
		StringBuilder str = new StringBuilder("DELETE FROM contrachequeevento WHERE contracheque = ? and periodo = ? ");
		
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(obj.getCodigo());
	    condicao.add(periodo.getCodigo());
	    
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(ContraChequeEventoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ContraChequeEvento.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					
					final StringBuilder sql = new StringBuilder(" INSERT INTO contrachequeevento ")
							.append(" ( contracheque, eventofolhapagamento, valorreferencia, provento, desconto, ")
							.append(" referencia, informadoManual, periodo, valorInformado ) ")
							.append(" VALUES ( ?, ?, ?, ?, ?, ")
							.append(" ?, ?, ?, ?) ")
							.append("returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
					
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString()); 

					int i = 0;
					
					Uteis.setValuePreparedStatement(obj.getContraCheque(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorReferencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getProvento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDesconto(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getReferencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInformadoManual(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPeriodo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorInformado(), ++i, sqlInserir);
					
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

					public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
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

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void alterar(ContraChequeEventoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		try {
			
			ContraChequeEvento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					
					final StringBuilder sql = new StringBuilder(" UPDATE contrachequeevento SET ")
							.append(" contracheque=?, eventofolhapagamento=?, valorreferencia=?, provento=?, desconto=?, ")
							.append(" referencia=?, informadoManual =?, periodo =?, valorInformado =? ")
							.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
					
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

					int i = 0;
					
					Uteis.setValuePreparedStatement(obj.getContraCheque(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorReferencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getProvento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDesconto(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getReferencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getInformadoManual(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPeriodo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorInformado(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(ContraChequeEventoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ContraChequeEvento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			String sql = " DELETE FROM contrachequeevento WHERE codigo = ? "; 
			adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ContraChequeEventoVO consultarPorChavePrimaria(Long codigo) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlPorContraCheque());
		sql.append(" where contrachequeevento.codigo = ? ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS);
        }
        throw new Exception("Dados não encontrados (Evento do Contra Cheque).");
	}

	@Override
	public void validarDados(ContraChequeEventoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getContraCheque().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraChequeItem_contraCheque"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getEventoFolhaPagamento().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraChequeItem_evento"));
		}
	}

	@Override
	public ContraChequeEventoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		ContraChequeEventoVO obj = new ContraChequeEventoVO();
		obj.setCodigo(tabelaResultado.getInt("contrachequeevento.codigo"));
		obj.setValorReferencia(tabelaResultado.getBigDecimal("contrachequeevento.valorreferencia"));

		obj.setProvento(tabelaResultado.getBigDecimal("provento"));
		obj.setDesconto(tabelaResultado.getBigDecimal("desconto"));
		obj.setReferencia(tabelaResultado.getString("referencia"));
		obj.setInformadoManual(tabelaResultado.getBoolean("informadomanual"));
		
		obj.setValorInformado(tabelaResultado.getBoolean("valorInformado"));
		
		obj.getContraCheque().setCodigo(tabelaResultado.getInt("contracheque.codigo"));
		obj.setEventoFolhaPagamento(Uteis.montarDadosVO(tabelaResultado.getInt("eventofolhapagamento.codigo"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p.intValue(), null, Uteis.NIVELMONTARDADOS_TODOS)));
		obj.getEventoFolhaPagamento().setCodigo(tabelaResultado.getInt("eventofolhapagamento.codigo"));
		obj.getEventoFolhaPagamento().setDescricao(tabelaResultado.getString("eventofolhapagamento.descricao"));
		obj.getEventoFolhaPagamento().setIdentificador(tabelaResultado.getString("identificador"));
			
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("eventofolhapagamento.tipoLancamento"))) {
			obj.getEventoFolhaPagamento().setTipoLancamento(TipoLancamentoFolhaPagamentoEnum.valueOf(tabelaResultado.getString("eventofolhapagamento.tipoLancamento")));
		}

		obj.setPeriodo(Uteis.montarDadosVO(tabelaResultado.getInt("periodo"), CompetenciaPeriodoFolhaPagamentoVO.class, p -> getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
		
		return obj;
	}

	private StringBuilder getSqlPorContraCheque() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contrachequeevento.codigo as \"contrachequeevento.codigo\", contrachequeevento.valorreferencia as \"contrachequeevento.valorreferencia\",");
		sql.append(" eventofolhapagamento.descricao as \"eventofolhapagamento.descricao\",eventofolhapagamento.codigo as \"eventofolhapagamento.codigo\", eventofolhapagamento.identificador, ");
		sql.append(" contracheque.codigo as  \"contracheque.codigo\", eventofolhapagamento.tipoLancamento as \"eventofolhapagamento.tipoLancamento\", ");
		sql.append(" contrachequeevento.provento, contrachequeevento.desconto, contrachequeevento.referencia, contrachequeevento.informadomanual, contrachequeevento.valorInformado, ");
		sql.append(" contrachequeevento.periodo FROM contrachequeevento ");
		sql.append(" INNER JOIN eventofolhapagamento ON eventofolhapagamento.codigo = contrachequeevento.eventofolhapagamento");
		sql.append(" INNER JOIN contracheque ON contracheque.codigo = contrachequeevento.contracheque");
		return sql;
	}

	@Override
	public List<ContraChequeEventoVO> consultarPorContraCheque(Integer codigo, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		ContraChequeEvento.consultar(idEntidade, verificarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPorContraCheque());
		sql.append(" WHERE contrachequeevento.contracheque = ? order by eventofolhapagamento.tipolancamento desc, eventofolhapagamento.prioridade asc, eventofolhapagamento.ordemcalculo asc");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		List<ContraChequeEventoVO> lista = new ArrayList<ContraChequeEventoVO>();
        while (rs.next()) {
        	lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSCONSULTA));
        }

		return lista;
	}

	
	/**
	 * Consula dos dados do contracheque pelos parametros
	 * 
	 * @param contraChequeVO
	 * @param competenciaPeriodoFolhaPagamentoVO
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ContraChequeEventoVO> consultarDados(ContraChequeVO contraChequeVO, CompetenciaPeriodoFolhaPagamentoVO competenciaPeriodoFolhaPagamentoVO) throws Exception {
		
		List<Integer> filtros = new ArrayList<>();
		filtros.add(contraChequeVO.getCodigo());
		
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPorContraCheque());
		sql.append(" WHERE contrachequeevento.contracheque = ? ");
		
		if(Uteis.isAtributoPreenchido(competenciaPeriodoFolhaPagamentoVO)) {
			sql.append(" AND contrachequeevento.periodo = ? ");
			filtros.add(competenciaPeriodoFolhaPagamentoVO.getCodigo());
		}
		
		sql.append(" order by eventofolhapagamento.tipolancamento desc, eventofolhapagamento.prioridade asc, eventofolhapagamento.ordemcalculo asc");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		List<ContraChequeEventoVO> lista = new ArrayList<>();
        while (rs.next()) {
        	lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS));
        }

		return lista;
	}
	
	@Override
	public void excluirContraChequeEventoPorLancamentoDoContraCheque (LancamentoFolhaPagamentoVO lancamento, UsuarioVO usuario) throws Exception {
		ContraChequeEvento.excluir(getIdEntidade(), false, usuario);

		StringBuilder str = new StringBuilder("DELETE FROM contrachequeevento WHERE contracheque in ");
		str.append(" (select codigo from contracheque where lancamentofolhapagamento = ?) ");
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		
		getConexao().getJdbcTemplate().update(str.toString(), lancamento.getCodigo());		
	}

	@Override
	public void excluirContraChequeEventoPorContraCheque(ContraChequeVO contraCheque, UsuarioVO usuario) throws Exception {
		StringBuilder str = new StringBuilder("DELETE FROM contrachequeevento WHERE contracheque = ? "); 
		adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(str.toString(), contraCheque.getCodigo());	
	}

	@Override
	public List<ContraChequeEventoRelVO> consultarContraChequeEventos(TemplateLancamentoFolhaPagamentoVO obj, CompetenciaFolhaPagamentoVO competencia, CompetenciaPeriodoFolhaPagamentoVO periodo, SecaoFolhaPagamentoVO secaoFolhaPagamentoVO) throws Exception {
		List<Object> filtros = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT e.codigo, e.identificador, e.descricao, SUM(valorreferencia) valorreferencia, SUM(provento) provento, SUM(desconto) desconto from contrachequeevento c");
		sql.append(" INNER JOIN eventofolhapagamento e ON (e.codigo = c.eventofolhapagamento)");
		sql.append(" INNER JOIN contracheque ON contracheque.codigo = c.contracheque");
		sql.append(" LEFT JOIN funcionariocargo fc ON contracheque.funcionariocargo = fc.codigo");
		sql.append(" LEFT JOIN competenciafolhapagamento cfp ON contracheque.competenciafolhapagamento = cfp.codigo");
		sql.append(" LEFT JOIN competenciaperiodofolhapagamento cp ON c.periodo = cp.codigo");
		
		sql.append(" WHERE 1 = 1");
		
		if (Uteis.isAtributoPreenchido(secaoFolhaPagamentoVO.getCodigo()) && secaoFolhaPagamentoVO.getCodigo() != 0) {
    		sql.append(" AND fc.secaofolhapagamento = ?");
    		filtros.add(secaoFolhaPagamentoVO.getCodigo());
    	}

        if (Uteis.isAtributoPreenchido(competencia.getCodigo())) {
        	sql.append(" AND cfp.codigo = ?");
        	filtros.add(competencia.getCodigo());

        	if (Uteis.isAtributoPreenchido(periodo.getCodigo())) {
        		sql.append(" AND cp.codigo = ?");
            	filtros.add(periodo.getCodigo());
        	}
        }
        sql.append(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(obj));
        sql.append(" GROUP BY e.codigo, e.descricao order by e.identificador");

		List<ContraChequeEventoRelVO> lista = new ArrayList<>();
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
        while (rs.next()) {
        	ContraChequeEventoRelVO contraChequeEventoRel = new ContraChequeEventoRelVO();
        	contraChequeEventoRel.setCodigo(rs.getInt("codigo"));
        	contraChequeEventoRel.setIdentificador(rs.getString("identificador"));
        	contraChequeEventoRel.setDescricao(rs.getString("descricao"));
        	contraChequeEventoRel.setValorReferencia(rs.getBigDecimal("valorreferencia"));
        	contraChequeEventoRel.setProvento(rs.getBigDecimal("provento"));
        	contraChequeEventoRel.setDesconto(rs.getBigDecimal("desconto"));
        	lista.add(contraChequeEventoRel);
        }
		return lista;
	}

	@Override
	public List<ContraChequeEventoRelVO> consultarContraChequeEventosSecao(TemplateLancamentoFolhaPagamentoVO obj, CompetenciaFolhaPagamentoVO competencia, CompetenciaPeriodoFolhaPagamentoVO periodo) throws Exception {
		List<Object> filtros = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct fc.secaofolhapagamento, sf.descricao, sum(cc.totalprovento) as provento, sum(cc.totaldesconto) as desconto");
		sql.append(" from contracheque cc");
		sql.append(" left  join templatelancamentofolhapagamento templatelancamentofolhapagamento on templatelancamentofolhapagamento.codigo = cc.templatelancamentofolhapagamento ");
		sql.append(" inner join competenciafolhapagamento cfp on cfp.codigo = cc.competenciafolhapagamento");
		sql.append(" inner join competenciaperiodofolhapagamento cp on cp.competenciafolhapagamento = cc.competenciafolhapagamento");
		sql.append(" inner join funcionariocargo fc on fc.codigo = cc.funcionariocargo");
		sql.append(" inner join secaofolhapagamento sf on sf.codigo = fc.secaofolhapagamento");
		
		sql.append(" WHERE 1 = 1");
		
		if (Uteis.isAtributoPreenchido(competencia.getCodigo())) {
			sql.append(" AND cfp.codigo = ?");
			filtros.add(competencia.getCodigo());
			
			if (Uteis.isAtributoPreenchido(periodo.getCodigo())) {
				sql.append(" AND cp.codigo = ?");
				filtros.add(periodo.getCodigo());
			}
		}
		
		sql.append(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(obj));
		sql.append(" GROUP BY fc.secaofolhapagamento, sf.descricao");
		sql.append(" order by fc.secaofolhapagamento");
		
		List<ContraChequeEventoRelVO> lista = new ArrayList<>();
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		while (rs.next()) {
			ContraChequeEventoRelVO contraChequeEventoRel = new ContraChequeEventoRelVO();
			contraChequeEventoRel.setCodigo(rs.getInt("secaofolhapagamento"));
			contraChequeEventoRel.setDescricao(rs.getString("descricao"));
			contraChequeEventoRel.setProvento(rs.getBigDecimal("provento"));
			contraChequeEventoRel.setDesconto(rs.getBigDecimal("desconto"));
			lista.add(contraChequeEventoRel);
		}
		return lista;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		ContraChequeEvento.idEntidade = idEntidade;
	}

	@Override
	public void excluirContraChequeEventoPorCompetenciaFiltroTemplateLancamentoEPeriodo(CompetenciaFolhaPagamentoVO competencia, TemplateLancamentoFolhaPagamentoVO template, CompetenciaPeriodoFolhaPagamentoVO periodo, UsuarioVO usuarioLogado) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" DELETE FROM contrachequeevento ce ");
		sql.append(" using contracheque cc, funcionariocargo fc ");
		sql.append(" where cc.codigo = ce.contracheque and fc.codigo = cc.funcionariocargo ");
		sql.append(" and cc.competenciafolhapagamento = ? and ce.periodo = ? ");
		sql.append(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(template));
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { competencia.getCodigo(), periodo.getCodigo() });
		
	}

	/**
	 * Cria objeto ContraChequeEventoVO a partir do EventoFolhaPagamentoVO
	 * 
	 * @param evento
	 * @param valorResultadoFormula
	 * @param textoResultadoFormula
	 * @return
	 */
	@Override
	public ContraChequeEventoVO montarContraChequeEventoAPartirDoEvento(EventoFolhaPagamentoVO evento, ContraChequeVO contraChequeVO) {

		ContraChequeEventoVO contraChequeEventoVO = new ContraChequeEventoVO();

		if(Uteis.isAtributoPreenchido(evento.getContraChequeEventoVO())) {
			contraChequeEventoVO.setCodigo(evento.getContraChequeEventoVO().getCodigo());
		}

		if(evento.getValorTemporario().compareTo(BigDecimal.ZERO) > 0 && evento.getValorInformado()) {
			contraChequeEventoVO.setValorInformado(true);
		}

		contraChequeEventoVO.setValorDoEvento(evento);
		contraChequeEventoVO.setValorReferencia(evento.getValorTemporario());
		contraChequeEventoVO.setReferencia(evento.getReferencia());
		contraChequeEventoVO.setEventoFolhaPagamento(evento);
		contraChequeEventoVO.setValorInformado(evento.getValorInformado());
		contraChequeEventoVO.setInformadoManual(evento.getInformadoManual());

		montarDadosPorEventosDoContraCheque(evento, contraChequeVO, contraChequeEventoVO);

		return contraChequeEventoVO;
	}

	/**
	 * Monta os dados pelos eventos do {@link ContraChequeVO}
	 * @param evento
	 * @param contraChequeVO
	 * @param contraChequeEventoVO
	 */
	private void montarDadosPorEventosDoContraCheque(EventoFolhaPagamentoVO evento, ContraChequeVO contraChequeVO, ContraChequeEventoVO contraChequeEventoVO) {
		if (Uteis.isAtributoPreenchido(contraChequeVO)) {
			for (ContraChequeEventoVO contraChequeEvento : contraChequeVO.getContraChequeEventos()) {
				if (evento.getCodigo().equals(contraChequeEvento.getEventoFolhaPagamento().getCodigo())) {
					//contraChequeEvento.setInformadoManual(contraChequeEventoVO.getInformadoManual());
					contraChequeEvento.setValorReferencia(contraChequeEventoVO.getValorReferencia());

					if (contraChequeEventoVO.getInformadoManual()) {
						switch (TipoLancamentoFolhaPagamentoEnum
								.valueOf(contraChequeEvento.getEventoFolhaPagamento().getTipoLancamento().getValor())) {
						case PROVENTO:
							contraChequeEventoVO.setProvento(contraChequeEvento.getValorReferencia());
							break;
						case DESCONTO:
							contraChequeEventoVO.setDesconto(contraChequeEvento.getValorReferencia());
							break;
						case BASE_CALCULO:
							contraChequeEventoVO.setBaseCalculo(contraChequeEvento.getValorReferencia());
							break;
						default:
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void cancelarContraCheque(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento, UsuarioVO usuarioLogado) {
		excluirContraChequeEventoPorCompetenciaFiltroTemplateLancamentoEPeriodo(lancamentoFolhaPagamento.getCompetenciaFolhaPagamentoVO(), lancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento(), lancamentoFolhaPagamento.getPeriodo(), usuarioLogado);
	}

	@Override
	public void excluirContraChequeEventoQueEstaoZerados(ContraChequeVO obj, boolean b, UsuarioVO usuarioVO) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" DELETE FROM contrachequeevento ");
		sql.append(" where contraCheque = ? and valorReferencia <= 0 and provento <= 0 and desconto <= 0 ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });	
	}

	@Override
	public List<ContraChequeEventoVO> consultarPorCompetenciaEventoEmprestimo(Long codigoCompetencia) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM contrachequeevento cce");
		sql.append("INNER JOIN contracheque cc ON cc.codigo = cce.contracheque");
		sql.append("INNER JOIN eventoemprestimocargofuncionario ef ON ef.eventofolhapagamento = cce.eventofolhapagamento");
		sql.append("WHERE cc.competenciafolhapagamento = 5 AND parcelapaga < numeroparcela");

        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoCompetencia);
        List<ContraChequeEventoVO> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS));
        }
        return lista;
	}

	@Override
	public List<ContraChequeEventoVO> consultarPorCompetenciaEventoFixo(Long codigoCompetencia) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM contrachequeevento cce");
		sql.append("INNER JOIN contracheque cc ON cc.codigo = cce.contracheque");
		sql.append("INNER JOIN eventofixocargofuncionario ef ON ef.eventofolhapagamento = cce.eventofolhapagamento");
		sql.append("WHERE cc.competenciafolhapagamento = 5 AND ef.numerolancamento > 0 ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoCompetencia);
		List<ContraChequeEventoVO> lista = new ArrayList<>();
		while (rs.next()) {
			lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS));
		}
		return lista;
	}

	@Override
	public BigDecimal consultarValorDaMediaDosEventosDoGrupoPorFuncionarioEPeriodo(FuncionarioCargoVO funcionarioCargoVO, String grupo, String incidencia, String dataInicial, String DataFinal) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum (contrachequeevento.provento) as quantidade from contrachequeevento "); 
		sql.append(" inner join eventofolhapagamentomedia on eventofolhapagamentomedia.eventofolhapagamento = contrachequeevento.eventofolhapagamento ");  
		sql.append(" inner join contracheque on contracheque.codigo = contrachequeevento.contracheque "); 
		sql.append(" inner join competenciafolhapagamento on competenciafolhapagamento.codigo = contracheque.competenciafolhapagamento "); 
		sql.append(" where contracheque.funcionariocargo = ? and eventofolhapagamentomedia.grupo = ? and tipoeventomedia = ? "); 
		
		sql.append(" and to_char(competenciafolhapagamento.datacompetencia::DATE, 'yyyy-mm') >= ? ");
		sql.append(" and to_char(competenciafolhapagamento.datacompetencia::DATE, 'yyyy-mm') <= ? ");

		sql.append(" group by eventofolhapagamentomedia.grupo ");

		List<Object> filtros = new ArrayList<>();
		filtros.add(funcionarioCargoVO.getCodigo());
		filtros.add(grupo);
		filtros.add(incidencia);
		filtros.add(dataInicial);
		filtros.add(DataFinal);
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		while (rs.next()) {
			try {
				return rs.getBigDecimal("quantidade").divide(new BigDecimal("12"), RoundingMode.HALF_EVEN);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return BigDecimal.ZERO;
	}
	
	@Override
	public List<ContraChequeEventoVO> consultarPorIDsContraCheque(Integer codigo, List<Integer> idsContraCheques) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlPorContraCheque());
		sql.append(" WHERE contrachequeevento.contracheque");
		sql.append(realizarGeracaoIn(idsContraCheques.size()));
		sql.append(" order by eventofolhapagamento.tipolancamento desc, eventofolhapagamento.prioridade asc, eventofolhapagamento.ordemcalculo asc");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), idsContraCheques.toArray());
		List<ContraChequeEventoVO> lista = new ArrayList<ContraChequeEventoVO>();
        while (rs.next()) {
        	lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSCONSULTA));
        }
		return lista;
	}

	@Override
	public BigDecimal consultarValorDoEventoDeIRRFDoContraCheque(ContraChequeVO contraChequeVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COALESCE(sum(contrachequeevento.desconto),0) as vlrIRRF FROM contrachequeevento ");
		sql.append(" INNER JOIN eventofolhapagamento ON eventofolhapagamento.codigo = contrachequeevento.eventofolhapagamento");
		sql.append(" INNER JOIN contracheque ON contracheque.codigo = contrachequeevento.contracheque");
		sql.append(" WHERE contrachequeevento.contracheque = ? ");
		sql.append(" AND eventofolhapagamento.categoria = ? ");
		sql.append(" AND eventofolhapagamento.agrupamentoFolhaNormal = ? ");
		sql.append(" AND eventofolhapagamento.situacao = ? ");
        sql.append(" AND eventofolhapagamento.eventopadrao = ? ");
		
        List<Object> filtros = new ArrayList<>();
        filtros.add(contraChequeVO.getCodigo());
        filtros.add(CategoriaEventoFolhaEnum.IRRF.getValor());
        filtros.add(true);
        filtros.add(AtivoInativoEnum.ATIVO.getValor());
        filtros.add(true);
        
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
        if (rs.next()) {
        	return rs.getBigDecimal("vlrIRRF");
        }
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal consultarValorDaBaseCalculoIRRFDoContraCheque(ContraChequeVO contraChequeVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COALESCE(sum(contrachequeevento.provento),0) as vlrBaseCalculoIRRF FROM contrachequeevento ");
		sql.append(" INNER JOIN eventofolhapagamento ON eventofolhapagamento.codigo = contrachequeevento.eventofolhapagamento");
		sql.append(" INNER JOIN contracheque ON contracheque.codigo = contrachequeevento.contracheque");
		sql.append(" WHERE contrachequeevento.contracheque = ? ");
		sql.append(" AND eventofolhapagamento.irrffolhanormal = ? ");
		
        List<Object> filtros = new ArrayList<>();
        filtros.add(contraChequeVO.getCodigo());
        filtros.add(true);
        
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
        if (rs.next()) {
        	return rs.getBigDecimal("vlrBaseCalculoIRRF");
        }
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal consultarValorDoEventoDaPrimeiraParcela13(ContraChequeVO contraCheque, EventoFolhaPagamentoVO eventoFolhaPagamentoVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contrachequeevento.provento from contrachequeevento ");
		sql.append(" INNER JOIN eventofolhapagamento ON eventofolhapagamento.codigo = contrachequeevento.eventofolhapagamento");
		sql.append(" INNER JOIN contracheque ON contracheque.codigo = contrachequeevento.contracheque");
		sql.append(" WHERE contrachequeevento.contracheque = ? ");
		sql.append(" AND eventofolhapagamento.codigo = ? ");
		
        List<Object> filtros = new ArrayList<>();
        filtros.add(contraCheque.getCodigo());
        filtros.add(eventoFolhaPagamentoVO.getCodigo());
        
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
        if (rs.next()) {
        	return rs.getBigDecimal("provento");
        }
		return BigDecimal.ZERO;
	}

	@Override
	public SqlRowSet consultarEventosDePensaoLancadosNoContraCheque(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		StringBuilder sql = new StringBuilder(" select distinct on (fd.codigo) fc.matriculacargo , fd.codigo as dependentePensao, cce.desconto as valorPensao from contrachequeevento cce "); 
		sql.append(" inner join contracheque cc on cc.codigo = cce.contracheque  and cc.competenciafolhapagamento = ? ");
		sql.append(" inner join funcionariocargo fc on fc.codigo = cc.funcionariocargo "); 
		sql.append(" inner join funcionarioDependente fd on fd.funcionario = fc.funcionario "); 
		sql.append(" inner join eventofolhapagamento e on e.codigo = cce.eventoFolhaPagamento and e.codigo = fd.eventoFolhaPagamento "); 
		sql.append(" where pensao = true ");
		sql.append(" order by fd.codigo ");
		
		List<Object> filtros = new ArrayList<>();
		filtros.add(competenciaFolhaPagamentoVO.getCodigo());
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
		if(rs.next())
			return rs;
		
		return null;
	}
}