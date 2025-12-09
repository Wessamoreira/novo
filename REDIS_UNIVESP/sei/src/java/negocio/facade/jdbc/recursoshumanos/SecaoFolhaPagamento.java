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
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO.EnumCampoConsultaSecaoFolhaPagamento;
import negocio.comuns.recursoshumanos.enumeradores.CategoriaRAISEnum;
import negocio.comuns.recursoshumanos.enumeradores.ControlePontoEnum;
import negocio.comuns.recursoshumanos.enumeradores.MotivoMudancaCNPJEnum;
import negocio.comuns.recursoshumanos.enumeradores.OptanteSimpleEnum;
import negocio.comuns.recursoshumanos.enumeradores.PorteEmpresaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.SecaoFolhaPagamentoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>SecaoFolhaPagamentoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>SecaoFolhaPagamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class SecaoFolhaPagamento extends SuperFacade<SecaoFolhaPagamentoVO> implements SecaoFolhaPagamentoInterfaceFacade<SecaoFolhaPagamentoVO> {

	private static final long serialVersionUID = -3433795966938164913L;

	protected static String idEntidade;

	public SecaoFolhaPagamento() throws Exception {
		super();
		setIdEntidade("SecaoFolhaPagamento");
	}
	
	public SecaoFolhaPagamento(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("SecaoFolhaPagamento");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(SecaoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		validarDadosDuplicadosIdentificador(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(SecaoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			SecaoFolhaPagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			final String sql = " INSERT INTO secaofolhapagamento(identificador, descricao, codigoterceiros, codigopagamentogps, fpas, sat, acidentetrabalho, optantesimples, cnpjanterior,"
							 + " motivomudanca, empregados, prefixorais, naturezajuros, categoriarais, mesdatabase, dataencerramento, porteempresa, controleponto, atividadeeconomica) "
							 + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getIdentificador());
					sqlInserir.setString(2, obj.getDescricao());
					sqlInserir.setInt(3, obj.getCodigoTerceiros());
					sqlInserir.setInt(4, obj.getCodigoPagamentoGPS());
					sqlInserir.setInt(5, obj.getFPAS());
					sqlInserir.setInt(6, obj.getSAT());
					sqlInserir.setInt(7, obj.getAcidenteTrabalho());

					if (Uteis.isAtributoPreenchido(obj.getOptanteSimples())) {
						sqlInserir.setString(8, obj.getOptanteSimples().toString());
					} else {
						sqlInserir.setNull(8, 0);
					}

					sqlInserir.setString(9, obj.getCnpjAnterior());

					if (Uteis.isAtributoPreenchido(obj.getMotivoMudanca())) {
						sqlInserir.setString(10, obj.getMotivoMudanca().toString());
					} else {
						sqlInserir.setNull(10, 0);
					}

					sqlInserir.setInt(11, obj.getEmpregados());
					sqlInserir.setInt(12, obj.getPrefixoRAIS());
					sqlInserir.setInt(13, obj.getNaturezaJuros());

					if (Uteis.isAtributoPreenchido(obj.getCategoriaRAIS())) {
						sqlInserir.setString(14, obj.getCategoriaRAIS().toString());
					} else {
						sqlInserir.setNull(14, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getMesDataBase())) {
						sqlInserir.setString(15, obj.getMesDataBase().toString());
					} else {
						sqlInserir.setNull(15, 0);
					}

					sqlInserir.setDate(16, Uteis.getDataJDBC(obj.getDataEncerramento()));

					if (Uteis.isAtributoPreenchido(obj.getPorteEmpresa())) {
						sqlInserir.setString(17, obj.getPorteEmpresa().toString());
					} else {
						sqlInserir.setNull(17, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getControlePonto())) {
						sqlInserir.setString(18, obj.getControlePonto().toString());
					} else {
						sqlInserir.setNull(18, 0);
					}

					sqlInserir.setInt(19, obj.getAtividadeEconomica());

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
	public void alterar(SecaoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			SecaoFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			final String sql = " UPDATE secaofolhapagamento SET identificador=?, descricao=?, codigoterceiros=?, codigopagamentogps=?, fpas=?, sat=?, acidentetrabalho=?, optantesimples=?,"
							 + " cnpjanterior=?, motivomudanca=?, empregados=?, prefixorais=?, naturezajuros=?, categoriarais=?, mesdatabase=?, dataencerramento=?,"
							 + " porteempresa=?, controleponto=?, atividadeeconomica=? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getIdentificador());
					sqlAlterar.setString(2, obj.getDescricao());
					sqlAlterar.setInt(3, obj.getCodigoTerceiros());
					sqlAlterar.setInt(4, obj.getCodigoPagamentoGPS());
					sqlAlterar.setInt(5, obj.getFPAS());
					sqlAlterar.setInt(6, obj.getSAT());
					sqlAlterar.setInt(7, obj.getAcidenteTrabalho());

					if (Uteis.isAtributoPreenchido(obj.getOptanteSimples())) {
						sqlAlterar.setString(8, obj.getOptanteSimples().toString());
					} else {
						sqlAlterar.setNull(8, 0);
					}

					sqlAlterar.setString(9, obj.getCnpjAnterior());

					if (Uteis.isAtributoPreenchido(obj.getMotivoMudanca())) {
						sqlAlterar.setString(10, obj.getMotivoMudanca().toString());
					} else {
						sqlAlterar.setNull(10, 0);
					}

					sqlAlterar.setInt(11, obj.getEmpregados());
					sqlAlterar.setInt(12, obj.getPrefixoRAIS());
					sqlAlterar.setInt(13, obj.getNaturezaJuros());

					if (Uteis.isAtributoPreenchido(obj.getCategoriaRAIS())) {
						sqlAlterar.setString(14, obj.getCategoriaRAIS().toString());
					} else {
						sqlAlterar.setNull(14, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getMesDataBase())) {
						sqlAlterar.setString(15, obj.getMesDataBase().toString());
					} else {
						sqlAlterar.setNull(15, 0);
					}

					sqlAlterar.setDate(16, Uteis.getDataJDBC(obj.getDataEncerramento()));

					if (Uteis.isAtributoPreenchido(obj.getPorteEmpresa())) {
						sqlAlterar.setString(17, obj.getPorteEmpresa().toString());
					} else {
						sqlAlterar.setNull(17, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getControlePonto())) {
						sqlAlterar.setString(18, obj.getControlePonto().toString());
					} else {
						sqlAlterar.setNull(18, 0);
					}

					sqlAlterar.setInt(19, obj.getAtividadeEconomica());
					sqlAlterar.setInt(20, obj.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(SecaoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			String sql = "DELETE FROM SecaoFolhaPagamento WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public SecaoFolhaPagamentoVO consultarPorChavePrimaria(Long codigo) throws Exception {
		String sql = " SELECT * FROM SecaoFolhaPagamento WHERE codigo = ?";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        }
        throw new Exception("Dados não encontrados (Seção Folha Pagamento).");
	}

	@Override
	public List<SecaoFolhaPagamentoVO> consultar(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM SecaoFolhaPagamento");

		switch (EnumCampoConsultaSecaoFolhaPagamento.valueOf(campoConsulta)) {
		case CODIGO:
			sql.append(" WHERE codigo = ").append(Integer.parseInt(valorConsulta));
			break;
		case DESCRICAO:
			sql.append(" WHERE upper( descricao ) like('").append(valorConsulta.toUpperCase()).append("%') ORDER BY SecaoFolhaPagamento.codigo DESC");
			break;
		case IDENTIFICADOR:
			sql.append(" WHERE upper( identificador ) like('").append(valorConsulta.toUpperCase()).append("%') ORDER BY SecaoFolhaPagamento.codigo DESC");
			break;
		default:
			break;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<SecaoFolhaPagamentoVO> secaoFolhaPagamentoVOs = new ArrayList<>();
		while(tabelaResultado.next()) {
			secaoFolhaPagamentoVOs.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return secaoFolhaPagamentoVOs;
	}
	
	private void validarDadosDuplicadosIdentificador(SecaoFolhaPagamentoVO obj) throws ConsistirException {
		int retorno = consutarDadosTotalPorIdentificador(obj);
		
		if (retorno > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_SecaoFolhaPagamento_duplicado"));
		}
		
	}

	private int consutarDadosTotalPorIdentificador(SecaoFolhaPagamentoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(codigo) as qtde FROM secaoFolhaPagamento");
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

	/**
	 * Valida os campos obrigatorio do <code>SecaoFolhaPagamentoVO</code>
	 * 
	 * @param obj {@link SecaoFolhaPagamentoVO}
	 * @exception ConsistirException
	 */
	@Override
	public void validarDados(SecaoFolhaPagamentoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getIdentificador())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_SecaoFolhaPagamento_identificador"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_SecaoFolhaPagamento_descricao"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getPrefixoRAIS())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_SecaoFolhaPagamento_prefixoRAIS"));
		}
	}

	@Override
	public SecaoFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		SecaoFolhaPagamentoVO obj = new SecaoFolhaPagamentoVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setIdentificador(tabelaResultado.getString("identificador"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setCodigoTerceiros(tabelaResultado.getInt("codigoterceiros"));
		obj.setCodigoPagamentoGPS(tabelaResultado.getInt("codigopagamentogps"));
		obj.setFPAS(tabelaResultado.getInt("fpas"));
		obj.setSAT(tabelaResultado.getInt("sat"));
		obj.setAcidenteTrabalho(tabelaResultado.getInt("acidentetrabalho"));
		
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("optantesimples"))) {
			obj.setOptanteSimples(OptanteSimpleEnum.valueOf(tabelaResultado.getString("optantesimples")));
		}

		obj.setCnpjAnterior(tabelaResultado.getString("cnpjanterior"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("motivomudanca"))) {
			obj.setMotivoMudanca(MotivoMudancaCNPJEnum.valueOf(tabelaResultado.getString("motivomudanca")));
		}

		obj.setEmpregados(tabelaResultado.getInt("empregados"));
		obj.setPrefixoRAIS(tabelaResultado.getInt("prefixorais"));
		obj.setNaturezaJuros(tabelaResultado.getInt("naturezajuros"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("categoriarais"))) {
			obj.setCategoriaRAIS(CategoriaRAISEnum.valueOf(tabelaResultado.getString("categoriarais")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("mesdatabase"))) {
			obj.setMesDataBase(MesAnoEnum.valueOf(tabelaResultado.getString("mesdatabase")));
		}

		obj.setDataEncerramento(tabelaResultado.getDate("dataencerramento"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("porteempresa"))) {
			obj.setPorteEmpresa(PorteEmpresaEnum.valueOf(tabelaResultado.getString("porteempresa")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("controleponto"))) {
			obj.setControlePonto(ControlePontoEnum.valueOf(tabelaResultado.getString("controleponto")));
		}

		obj.setAtividadeEconomica(tabelaResultado.getInt("atividadeeconomica"));

		return obj;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		List<SecaoFolhaPagamentoVO> objs = new ArrayList<SecaoFolhaPagamentoVO>(0);
		dataModelo.getListaFiltros().clear();
		switch (EnumCampoConsultaSecaoFolhaPagamento.valueOf(dataModelo.getCampoConsulta())) {
		case DESCRICAO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			objs = consultarSecaoFolhaPagamentoPorDescricao(dataModelo);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalSecaoFolhaPagamentoPorDescricao(dataModelo));
			break;
		case IDENTIFICADOR:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			objs = consultarSecaoFolhaPagamentoPorIdentificador(dataModelo);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalSecaoFolhaPagamentoPorIdentificador(dataModelo));
			break;
		case CODIGO:
			dataModelo.getListaFiltros().add(Integer.parseInt(dataModelo.getValorConsulta()));
			objs = consultarSecaoFolhaPagamentoPorCodigo(dataModelo);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalSecaoFolhaPagamentoPorCodigo(dataModelo));
			break;
		default:
			break;
		}
		dataModelo.setListaConsulta(objs);
	}

	private List<SecaoFolhaPagamentoVO> consultarSecaoFolhaPagamentoPorIdentificador(DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM SecaoFolhaPagamento");
        sql.append(" WHERE UPPER(sem_acentos(identificador) ) like (UPPER(sem_acentos(?))) ");
        sql.append(" order by descricao ");

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        List<SecaoFolhaPagamentoVO> lista = new ArrayList<>();
        while(tabelaResultado.next()) {
        	lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }

        return lista;
	}

	private Integer consultarTotalSecaoFolhaPagamentoPorIdentificador(DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(codigo) as qtde FROM SecaoFolhaPagamento ");
        sql.append(" WHERE UPPER(sem_acentos(identificador) ) like (UPPER(sem_acentos(?)))");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return  (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private List<SecaoFolhaPagamentoVO> consultarSecaoFolhaPagamentoPorDescricao(DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM SecaoFolhaPagamento");
        sql.append(" WHERE UPPER(sem_acentos(descricao) ) like (UPPER(sem_acentos(?))) ");
        sql.append(" order by descricao ");
        
        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        List<SecaoFolhaPagamentoVO> lista = new ArrayList<>();
        while(tabelaResultado.next()) {
        	lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }

        return lista;
	}

	private Integer consultarTotalSecaoFolhaPagamentoPorDescricao(DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(codigo) as qtde FROM SecaoFolhaPagamento ");
        sql.append(" WHERE UPPER(sem_acentos(descricao) ) like (UPPER(sem_acentos(?)))");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
	
	private List<SecaoFolhaPagamentoVO> consultarSecaoFolhaPagamentoPorCodigo(DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM SecaoFolhaPagamento");
        sql.append(" WHERE codigo = ? ");
        sql.append(" order by descricao ");
        
        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        List<SecaoFolhaPagamentoVO> lista = new ArrayList<>();
        while(tabelaResultado.next()) {
        	lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }

        return lista;
	}

	private Integer consultarTotalSecaoFolhaPagamentoPorCodigo(DataModelo dataModelo) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(codigo) as qtde FROM SecaoFolhaPagamento ");
        sql.append(" WHERE codigo = ?");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
	
	@Override
	public SecaoFolhaPagamentoVO consultarPorIdentificador(String identificadorSecao) throws Exception {
		String sql = " SELECT * FROM SecaoFolhaPagamento WHERE identificador = ?";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, identificadorSecao);
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        }
        throw new Exception("Dados não encontrados (Seção Folha Pagamento).");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		SecaoFolhaPagamento.idEntidade = idEntidade;
	}

}
