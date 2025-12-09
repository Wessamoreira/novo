package negocio.facade.jdbc.recursoshumanos;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.recursoshumanos.LinhaTransporteVO;
import negocio.comuns.recursoshumanos.TipoEmprestimoVO.EnumCampoConsultaTipoEmprestimo;
import negocio.comuns.recursoshumanos.enumeradores.TipoTarifaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.LinhaTransporteInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>LinhaTransporteVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>LinhaTransporteVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class LinhaTransporte extends SuperFacade<LinhaTransporteVO> implements LinhaTransporteInterfaceFacade<LinhaTransporteVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public LinhaTransporte() throws Exception {
		super();
		setIdEntidade("LinhaTransporte");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(LinhaTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			obj.setUsuarioUltimaAlteracao(usuarioVO);
			obj.setDataUltimaAlteracao(new Date());
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(LinhaTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			LinhaTransporte.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append("INSERT INTO public.linhatransporte(");
					sql.append(" descricao, tipolinhatransporte, tipotarifa, valor, itinerario,");
					sql.append(" situacao, eventofolhapagamento)");
					sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?) returning codigo ");
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
	public void alterar(LinhaTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		LinhaTransporte.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.linhatransporte SET");
				sql.append(" descricao=?, tipolinhatransporte=?, tipotarifa=?, valor=?, itinerario=?,");
				sql.append(" situacao=?, eventofolhapagamento=?, usuarioUltimaAlteracao = ?, dataUltimaAlteracao = ?");
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
				Uteis.setValuePreparedStatement(obj.getUsuarioUltimaAlteracao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataUltimaAlteracao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(LinhaTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		LinhaTransporte.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM linhatransporte WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	@Override
	public LinhaTransporteVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE linhatransporte.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public void validarDados(LinhaTransporteVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoEmprestimo_descricao"));
		}

	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacao) throws Exception {
		List<LinhaTransporteVO> objs = new ArrayList<LinhaTransporteVO>();
		dataModelo.getListaFiltros().clear();
		dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_TODOS);
		if (situacao.equals("TODOS")) {
			situacao = "";
		}

		switch (EnumCampoConsultaTipoEmprestimo.valueOf(dataModelo.getCampoConsulta())) {
		case DESCRICAO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
			objs = consultarTipoEmprestimo(dataModelo, "linhatransporte.descricao", situacao);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalTipoEmprestimo(dataModelo, "linhatransporte.descricao", situacao));
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

	private List<LinhaTransporteVO> consultarTipoEmprestimo(DataModelo dataModelo, String campoConsulta, String situacao) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder();
        sql.append(getSqlBasico());

        if (dataModelo.getCampoConsulta().equals(EnumCampoConsultaTipoEmprestimo.CODIGO.name())) {
        	sql.append(" WHERE linhaTransporte.codigo = ?");
        } else {
        	dataModelo.getListaFiltros().add(situacao + PERCENT);
        	sql.append(" WHERE LOWER(sem_acentos(").append(campoConsulta).append(") ) LIKE (LOWER(sem_acentos(?))) ");
        	sql.append(" AND linhatransporte.situacao like ? ");
        	sql.append(" ORDER BY ").append(" linhaTransporte.codigo DESC");        	
        }

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return montarDadosLista(tabelaResultado);
    }

	private Integer consultarTotalTipoEmprestimo(DataModelo dataModelo, String campoConsulta, String situacao) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS qtde FROM linhaTransporte ");

        if (dataModelo.getCampoConsulta().equals(EnumCampoConsultaTipoEmprestimo.CODIGO.name())) {
        	sql.append(" WHERE linhaTransporte.codigo = ?");
        } else {
        	sql.append(" WHERE LOWER(sem_acentos(").append(campoConsulta).append(") ) LIKE (LOWER(sem_acentos(?))) ");
        	sql.append(" AND linhatransporte.situacao like ? ");
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	/**
	 * Monta a lista de {@link LinhaTransporteVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<LinhaTransporteVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<LinhaTransporteVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public LinhaTransporteVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		LinhaTransporteVO obj = new LinhaTransporteVO();

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
		obj.getEventoFolhaPagamento().setCodigo(tabelaResultado.getInt("eventoFolhaPagamento"));
		obj.getEventoFolhaPagamento().setDescricao(tabelaResultado.getString("eventofolhapagamento.descricao"));
		obj.getEventoFolhaPagamento().setIdentificador(tabelaResultado.getString("eventofolhapagamento.identificador"));
		obj.setDataUltimaAlteracao(tabelaResultado.getDate("dataultimaalteracao"));
		obj.getUsuarioUltimaAlteracao().setCodigo(tabelaResultado.getInt("usuarioultimaalteracao"));

		if (Uteis.isAtributoPreenchido(obj.getUsuarioUltimaAlteracao().getCodigo())) {
			obj.setUsuarioUltimaAlteracao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioUltimaAlteracao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT linhatransporte.*, eventofolhapagamento.identificador as \"eventofolhapagamento.identificador\", eventofolhapagamento.descricao as \"eventofolhapagamento.descricao\" FROM linhatransporte");
		sql.append(" LEFT JOIN eventofolhapagamento on linhatransporte.eventofolhapagamento = eventofolhapagamento.codigo");

		return sql.toString();
	}

	@Override
	public void inativar(LinhaTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					String sql = "UPDATE linhatransporte set situacao=?, usuarioUltimaAlteracao = ?, dataUltimaAlteracao = ? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;

					Uteis.setValuePreparedStatement(obj.getSituacao().getValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getUsuarioUltimaAlteracao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataUltimaAlteracao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		LinhaTransporte.idEntidade = idEntidade;
	}
}
