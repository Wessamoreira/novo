package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.recursoshumanos.ParametroValeTransporteVO;
import negocio.comuns.recursoshumanos.TipoEmprestimoVO.EnumCampoConsultaTipoEmprestimo;
import negocio.comuns.recursoshumanos.enumeradores.TipoTarifaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.ParametroValeTransporteInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>ParametroValeTransporteVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>ParametroValeTransporteVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class ParametroValeTransporte extends SuperFacade<ParametroValeTransporteVO> implements ParametroValeTransporteInterfaceFacade<ParametroValeTransporteVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public ParametroValeTransporte() throws Exception {
		super();
		setIdEntidade("ParametroValeTransporte");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(ParametroValeTransporteVO obj, ParametroValeTransporteVO parametroValeTransporteEdicao, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (!validarAlteracaoValor(obj.getValor(), parametroValeTransporteEdicao.getValor())) {
			if (!Uteis.isAtributoPreenchido(obj.getFimVigencia())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_ParametroValeTransporte_fimVigencia"));
			}
		}

		validarDataInicioCadastrada(obj);
		persistir(obj, validarAcesso, usuarioVO);

		persistirHistorico(obj, parametroValeTransporteEdicao, validarAcesso, usuarioVO);
	}

	private void validarDataInicioPosteriorDataFim(ParametroValeTransporteVO obj) throws ConsistirException {
		if (obj.getInicioVigencia().after(obj.getFimVigencia())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ParametroValeTransporte_fimVigenciaMenorInicioVigencia"));
		}
		
	}

	private void validarDataInicioCadastrada(ParametroValeTransporteVO obj) throws ConsistirException {
		Integer retorno = consultarPorInicioVigenciaEFimVigencia(obj);
		if (retorno > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ParametroValeTransporte_dataVigenciaDuplicada"));
		}
	}

	private void persistirHistorico(ParametroValeTransporteVO obj, ParametroValeTransporteVO parametroValeTransporteEdicao, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (parametroValeTransporteEdicao.getCodigo() != 0) {
			obj.getHistoricoVOs().add(getFacadeFactory().getParametroValeTransporteHistoricoInterfaceFacade().montarDados(parametroValeTransporteEdicao, usuarioVO));
			obj.getHistoricoVOs().stream().forEach(p -> {
				try {
					getFacadeFactory().getParametroValeTransporteHistoricoInterfaceFacade().persistir(p, false, usuarioVO);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(ParametroValeTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(ParametroValeTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ParametroValeTransporte.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append("INSERT INTO public.parametrovaletransporte(");
					sql.append(" descricao, tipolinhatransporte, tipotarifa, valor, itinerario,");
					sql.append(" situacao, eventofolhapagamento, iniciovigencia, fimvigencia)");
					sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoLinhaTransporte(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoTarifa(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getItinerario(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInicioVigencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFimVigencia(), ++i, sqlInserir);

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
	public void alterar(ParametroValeTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ParametroValeTransporte.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.parametrovaletransporte SET");
				sql.append(" descricao=?, tipolinhatransporte=?, tipotarifa=?, valor=?, itinerario=?,");
				sql.append(" situacao=?, eventofolhapagamento=?, iniciovigencia=?, fimvigencia=?");
				sql.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTipoLinhaTransporte(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTipoTarifa(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getItinerario(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamento(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getInicioVigencia(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getFimVigencia(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(ParametroValeTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ParametroValeTransporte.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		getFacadeFactory().getParametroValeTransporteHistoricoInterfaceFacade().excluirPorParametroValeTransporte(obj, false, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM parametrovaletransporte WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	@Override
	public ParametroValeTransporteVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE parametrovaletransporte.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public Integer consultarPorInicioVigenciaEFimVigencia(ParametroValeTransporteVO obj) throws ConsistirException {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(codigo) as qtde FROM parametrovaletransporte ");
		sql.append(" WHERE  (iniciovigencia BETWEEN ? AND ? OR fimvigencia BETWEEN ? AND ?) AND codigo != ? ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getInicioVigencia(), obj.getFimVigencia(), obj.getInicioVigencia(), obj.getFimVigencia(), obj.getCodigo());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	@Override
	public void validarDados(ParametroValeTransporteVO obj) throws ConsistirException {
		
		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoEmprestimo_descricao"));
		}

		if (obj.getValor().intValue() == BigDecimal.ZERO.intValue()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ParametroValeTransporte_valor"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getInicioVigencia())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ParametroValeTransporte_inicioVigencia"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getFimVigencia())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ParametroValeTransporte_fimVigencia"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getEventoFolhaPagamento())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoEmprestimo_eventoFolhaPagamento_obrigatorio"));
		}

		validarDataInicioPosteriorDataFim(obj);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacao) throws Exception {
		List<ParametroValeTransporteVO> objs = new ArrayList<ParametroValeTransporteVO>();
		dataModelo.getListaFiltros().clear();
		dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_TODOS);
		dataModelo.setControlarAcesso(false);
		if (situacao.equals("TODOS")) {
			situacao = "";
		}

		switch (EnumCampoConsultaTipoEmprestimo.valueOf(dataModelo.getCampoConsulta())) {
		case DESCRICAO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
			objs = consultarTipoEmprestimo(dataModelo, "parametrovaletransporte.descricao", situacao);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalTipoEmprestimo(dataModelo, "parametrovaletransporte.descricao", situacao));
			break;
		case CODIGO:
			dataModelo.getListaFiltros().add(Integer.parseInt(dataModelo.getValorConsulta()));
			objs = consultarTipoEmprestimo(dataModelo, "", "");
			dataModelo.setTotalRegistrosEncontrados(consultarTotalTipoEmprestimo(dataModelo, "", ""));
			break;
		default:
			break;
		}
		dataModelo.setListaConsulta(objs);
	}

	private List<ParametroValeTransporteVO> consultarTipoEmprestimo(DataModelo dataModelo, String campoConsulta, String situacao) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder();
        sql.append(getSqlBasico());

        if (dataModelo.getCampoConsulta().equals(EnumCampoConsultaTipoEmprestimo.CODIGO.name())) {
        	sql.append(" WHERE parametrovaletransporte.codigo = ?");
        } else {
        	dataModelo.getListaFiltros().add(situacao + PERCENT);
        	sql.append(" WHERE LOWER(sem_acentos(").append(campoConsulta).append(") ) LIKE (LOWER(sem_acentos(?))) ");
        	sql.append(" AND parametrovaletransporte.situacao like ? ");
        	sql.append(" ORDER BY ").append(" parametrovaletransporte.codigo DESC");        	
        }

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return montarDadosLista(tabelaResultado);
    }

	private Integer consultarTotalTipoEmprestimo(DataModelo dataModelo, String campoConsulta, String situacao) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS qtde FROM parametrovaletransporte ");

        if (dataModelo.getCampoConsulta().equals(EnumCampoConsultaTipoEmprestimo.CODIGO.name())) {
        	sql.append(" WHERE parametrovaletransporte.codigo = ?");
        } else {
        	sql.append(" WHERE LOWER(sem_acentos(").append(campoConsulta).append(") ) LIKE (LOWER(sem_acentos(?))) ");
        	sql.append(" AND parametrovaletransporte.situacao like ? ");
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	/**
	 * Monta a lista de {@link ParametroValeTransporteVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<ParametroValeTransporteVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<ParametroValeTransporteVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public ParametroValeTransporteVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		ParametroValeTransporteVO obj = new ParametroValeTransporteVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));

		obj.getTipoLinhaTransporte().setCodigo(tabelaResultado.getInt("tipoLinhaTransporte"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoTarifa"))) {
			obj.setTipoTarifa(TipoTarifaEnum.valueOf(tabelaResultado.getString("tipoTarifa")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacao"))) {
			obj.setSituacao(AtivoInativoEnum.valueOf(tabelaResultado.getString("situacao")));
		}

		obj.setValor(tabelaResultado.getBigDecimal("valor"));
		obj.setItinerario(tabelaResultado.getString("itinerario"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("eventoFolhaPagamento"))) {
			obj.setEventoFolhaPagamento(getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("eventoFolhaPagamento"), null, nivelMontarDados));
		}

		obj.setInicioVigencia(tabelaResultado.getDate("iniciovigencia"));
		obj.setFimVigencia(tabelaResultado.getDate("fimvigencia"));

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT parametrovaletransporte.*, eventofolhapagamento.identificador as \"eventofolhapagamento.identificador\", eventofolhapagamento.descricao as \"eventofolhapagamento.descricao\", ");
		sql.append(" iniciovigencia, fimvigencia FROM parametrovaletransporte");
		sql.append(" LEFT JOIN eventofolhapagamento on parametrovaletransporte.eventofolhapagamento = eventofolhapagamento.codigo");

		return sql.toString();
	}

	@Override
	public void inativar(ParametroValeTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					String sql = "UPDATE parametrovaletransporte set situacao=? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;

					Uteis.setValuePreparedStatement(obj.getSituacao().getValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

					return sqlAlterar;
				}
			});

			persistirHistorico(obj, obj, validarAcesso, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public boolean validarAlteracaoValor(BigDecimal valor1, BigDecimal valor2) {
		return valor1.setScale(2, BigDecimal.ROUND_HALF_DOWN).equals(valor2.setScale(2, BigDecimal.ROUND_HALF_DOWN));
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		ParametroValeTransporte.idEntidade = idEntidade;
	}
}
