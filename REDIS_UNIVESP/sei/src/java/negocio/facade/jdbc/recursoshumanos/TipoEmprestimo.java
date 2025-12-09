package negocio.facade.jdbc.recursoshumanos;

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
import negocio.comuns.recursoshumanos.TipoEmprestimoVO;
import negocio.comuns.recursoshumanos.TipoEmprestimoVO.EnumCampoConsultaTipoEmprestimo;
import negocio.comuns.recursoshumanos.enumeradores.TipoEmprestimoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.TipoEmprestimoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>TipoEmprestimoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>TipoEmprestimoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class TipoEmprestimo extends SuperFacade<TipoEmprestimoVO> implements TipoEmprestimoInterfaceFacade<TipoEmprestimoVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public TipoEmprestimo() throws Exception {
		super();
		setIdEntidade("TipoEmprestimo");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(TipoEmprestimoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(TipoEmprestimoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TipoEmprestimo.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder("INSERT INTO public.tipoemprestimo(descricao, banco, agencia, tipo) VALUES (?, ?, ?, ?)");
					sql.append(" returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getBanco(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAgencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoEmprestimo(), ++i, sqlInserir);

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
	public void alterar(TipoEmprestimoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		TipoEmprestimo.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE public.tipoemprestimo SET descricao=?, banco=?, agencia=?, tipo=? WHERE codigo = ? ")
						.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getBanco(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getAgencia(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTipoEmprestimo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(TipoEmprestimoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		TipoEmprestimo.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM tipoemprestimo WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	@Override
	public TipoEmprestimoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE tipoemprestimo.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(TipoEmprestimoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoEmprestimo_descricao"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getBanco().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoEmprestimo_banco"));
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		List<TipoEmprestimoVO> objs = new ArrayList<TipoEmprestimoVO>();
		dataModelo.getListaFiltros().clear();
		dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_TODOS);

		switch (EnumCampoConsultaTipoEmprestimo.valueOf(dataModelo.getCampoConsulta())) {
		case DESCRICAO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
			objs = consultarTipoEmprestimo(dataModelo, "descricao");
			dataModelo.setTotalRegistrosEncontrados(consultarTotalTipoEmprestimo(dataModelo, "descricao"));
			break;
		case BANCO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
			objs = consultarTipoEmprestimo(dataModelo, "banco.nome");
			dataModelo.setTotalRegistrosEncontrados(consultarTotalTipoEmprestimo(dataModelo, "banco.nome"));
			break;
		case CODIGO:
			dataModelo.getListaFiltros().add(Integer.parseInt(dataModelo.getValorConsulta()));
			objs = consultarTipoEmprestimo(dataModelo, "tipoemprestimo.codigo");
			dataModelo.setTotalRegistrosEncontrados(consultarTotalTipoEmprestimo(dataModelo, "tipoemprestimo.codigo"));
			break;
		default:
			break;
		}
		dataModelo.setListaConsulta(objs);
	}
	
	private List<TipoEmprestimoVO> consultarTipoEmprestimo(DataModelo dataModelo, String campoConsulta) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder();
        sql.append(getSqlBasico());

        if (dataModelo.getCampoConsulta().equals(EnumCampoConsultaTipoEmprestimo.CODIGO.name())) {
        	sql.append(" WHERE tipoemprestimo.codigo = ?");
        } else {
        	sql.append(" WHERE LOWER(sem_acentos(").append(campoConsulta).append(") ) LIKE (LOWER(sem_acentos(?))) ");
        	sql.append(" ORDER BY ").append(" tipoemprestimo.codigo DESC");        	
        }

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return montarDadosLista(tabelaResultado);
    }

	private Integer consultarTotalTipoEmprestimo(DataModelo dataModelo, String campoConsulta) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS qtde FROM tipoemprestimo ");
        sql.append(" LEFT JOIN banco on tipoemprestimo.banco = banco.codigo");
		sql.append(" LEFT JOIN agencia on tipoemprestimo.agencia = agencia.codigo ");

        if (dataModelo.getCampoConsulta().equals(EnumCampoConsultaTipoEmprestimo.CODIGO.name())) {
        	sql.append(" WHERE tipoemprestimo.codigo = ?");
        } else {
        	sql.append(" WHERE LOWER(sem_acentos(").append(campoConsulta).append(") ) LIKE (LOWER(sem_acentos(?))) ");
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	/**
	 * Monta a lista de {@link TipoEmprestimoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<TipoEmprestimoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<TipoEmprestimoVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public TipoEmprestimoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		TipoEmprestimoVO obj = new TipoEmprestimoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.getBanco().setCodigo(tabelaResultado.getInt("banco"));
		obj.getBanco().setNome(tabelaResultado.getString("banco.nome"));
		obj.getAgencia().setCodigo(tabelaResultado.getInt("agencia"));
		obj.getAgencia().setNome(tabelaResultado.getString("agencia.nome"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipo"))) {
			obj.setTipoEmprestimo(TipoEmprestimoEnum.valueOf(tabelaResultado.getString("tipo")));
		}

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT tipoemprestimo.*, banco.nome as \"banco.nome\", agencia.nome as \"agencia.nome\" FROM tipoemprestimo");
		sql.append(" LEFT JOIN banco on tipoemprestimo.banco = banco.codigo");
		sql.append(" LEFT JOIN agencia on tipoemprestimo.agencia = agencia.codigo ");

		return sql.toString();
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TipoEmprestimo.idEntidade = idEntidade;
	}
	
	@Override
	public List<TipoEmprestimoVO> consultarPorFiltro(String campoConsulta, String valorConsulta, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		sql.append(" WHERE 1 = 1");
		
		SqlRowSet tabelaResultado; 
		
		switch (campoConsulta) {
		case "codigo":
			sql.append(" AND tipoemprestimo.codigo = ? ");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Integer.valueOf(valorConsulta));
			break;
		default:
			sql.append(" AND upper( tipoemprestimo.descricao ) like(upper(?)) ");
			valorConsulta = "%" + valorConsulta + "%";
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta);
			break;
		}

		List<TipoEmprestimoVO> listaTipoEmprestimoVO = new ArrayList<>();
		while (tabelaResultado.next()) {
			listaTipoEmprestimoVO.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return listaTipoEmprestimoVO;
	}
}