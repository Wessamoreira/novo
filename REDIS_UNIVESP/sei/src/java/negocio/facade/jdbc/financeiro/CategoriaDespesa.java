package negocio.facade.jdbc.financeiro;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.math.NumberUtils;
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

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaRateioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CategoriaDespesaVO.enumCampoConsultaCategoriaDespesa;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CategoriaDespesaInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class CategoriaDespesa extends ControleAcesso implements CategoriaDespesaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public CategoriaDespesa() throws Exception {
		super();
		setIdEntidade("CategoriaDespesa");
	}	

	protected ConfiguracaoFinanceiroVO obterMascaraPadraoConfiguracaoFinanceiro(UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		try {
			if (Uteis.isAtributoPreenchido(configuracaoFinanceiroVO)) {
				return configuracaoFinanceiroVO;
			}
			return new ConfiguracaoFinanceiroVO();
		} catch (Exception e) {
			return new ConfiguracaoFinanceiroVO();
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int obterNrNivelCategoriaDespesa(CategoriaDespesaVO principal, UsuarioVO usuario) throws Exception {
		if (principal.verificarCategoriaDespesaPrimeiroNivel()) {
			return 1;
		}
		CategoriaDespesaVO categoria = consultarPorChavePrimaria(principal.getCategoriaDespesaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obterNrNivelCategoriaDespesa(categoria, usuario) + 1;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarIdentificadorMascaraCategoriaDespesa2(CategoriaDespesaVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		String mascaraCategoriaDespesa = configuracaoFinanceiroVO.getMascaraCategoriaDespesa(), mascaraConsulta = "", mascaraCategoriaPrincipal = "";
		if (obj.verificarCategoriaDespesaPrimeiroNivel()) {
			mascaraConsulta = of(mascaraCategoriaDespesa.split("\\.")).findFirst().orElse("");
		} else {
			CategoriaDespesaVO unVO = consultarPorChavePrimaria(obj.getCategoriaDespesaPrincipal(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if (Uteis.isAtributoPreenchido(unVO.getIdentificadorCategoriaDespesa())) {
				mascaraCategoriaPrincipal = unVO.getIdentificadorCategoriaDespesa();
				long nivel = mascaraCategoriaPrincipal.chars().filter(c -> c == '.').count() + 2;
				mascaraConsulta = of(mascaraCategoriaDespesa.split("\\.")).limit(nivel).collect(joining("."));
			}
		}
		List<CategoriaDespesaVO> categoriaDespesaVOs = consultarUltimaMascaraGerada(mascaraConsulta, mascaraCategoriaPrincipal, obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		List<String> mascaras = null;
		if (Uteis.isAtributoPreenchido(categoriaDespesaVOs) && Uteis.isAtributoPreenchido(categoriaDespesaVOs.get(0).getIdentificadorCategoriaDespesa())) {
			mascaras = of(categoriaDespesaVOs.get(0).getIdentificadorCategoriaDespesa().split("\\.")).collect(toList());
		}
		obj.setIdentificadorCategoriaDespesa(Uteis.realizarGeracaoIdentificador("(Categoria de Despesa)", mascaraConsulta, mascaraCategoriaPrincipal, mascaras));
	}

	private void validarNivelCategoriaDespesa(CategoriaDespesaVO obj, UsuarioVO usuario, int nrNiveis) throws Exception {
		int nrNivelUnidOrg ;
		try {
			if (obj.getIsUmaSubCategoria()) {
				CategoriaDespesaVO principal = consultarPorChavePrimaria(obj.getCategoriaDespesaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if (Uteis.isAtributoPreenchido(principal)) {
					nrNivelUnidOrg = this.obterNrNivelCategoriaDespesa(principal, usuario) + 1;
				} else {
					nrNivelUnidOrg = 1;
				}

			} else {
				nrNivelUnidOrg = 1;
			}
		} catch (ConsistirException e) {
			nrNivelUnidOrg = 1;
		}
		if (nrNivelUnidOrg > nrNiveis) {
			throw new Exception("Não é possível CADASTRAR este PLANO DE CONTA DE NÍVEL " + nrNivelUnidOrg + ". Número de níveis permitidos: " + nrNiveis);
		}
	}

	public List<CategoriaDespesaVO> consultarUltimaMascaraGerada(String mascaraConsulta, String mascaraCategoriaDespesaPrincipal, int codigo, int nivelMontarDados) throws Exception {
		String sqlStr = "select * from CategoriaDespesa where char_length(identificadorCategoriaDespesa) = " + mascaraConsulta.length();
		if (Uteis.isAtributoPreenchido(codigo)) {
			sqlStr += " and codigo <> " + codigo;
		}
		sqlStr += " and upper(identificadorCategoriaDespesa) like('" + mascaraCategoriaDespesaPrincipal.toUpperCase() + "%') order by identificadorCategoriaDespesa desc limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CategoriaDespesaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, !obj.getInformarManualmenteIdentificadorCategoriaDespesa(), usuarioVO, configuracaoFinanceiroVO);
		} else {
			alterar(obj, !obj.getInformarManualmenteIdentificadorCategoriaDespesa(), usuarioVO, configuracaoFinanceiroVO);
			getAplicacaoControle().removerCategoriaDespesa(obj.getCodigo());
		}
		List<CategoriaDespesaRateioVO> listaTemp = new ArrayList<CategoriaDespesaRateioVO>();
		listaTemp.addAll(obj.getListaCategoriaDespesaRateioAcademico());
		listaTemp.addAll(obj.getListaCategoriaDespesaRateioAdministrativo());
		validarSeRegistroForamExcluidoDasListaSubordinadas(listaTemp, "CategoriaDespesaRateio", "CategoriaDespesa", obj.getCodigo(), usuarioVO);
		getFacadeFactory().getCategoriaDespesaRateioFacade().persistir(obj.getListaCategoriaDespesaRateioAcademico(), false, usuarioVO);
		getFacadeFactory().getCategoriaDespesaRateioFacade().persistir(obj.getListaCategoriaDespesaRateioAdministrativo(), false, usuarioVO);
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void incluir(final CategoriaDespesaVO obj, boolean criarIdentificador, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		try {
			validarNivelCategoriaDespesa(obj, usuario, configuracaoFinanceiroVO.getNrNiveisCategoriaDespesa());
			if (criarIdentificador) {
				gerarIdentificadorMascaraCategoriaDespesa2(obj, usuario, configuracaoFinanceiroVO);
			}
			Uteis.validarIdentificador("(Categoria de Despesa)", obj.getIdentificadorCategoriaDespesa(), configuracaoFinanceiroVO.getMascaraCategoriaDespesa());
			validarDados(obj);
			CategoriaDespesa.incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO CategoriaDespesa( descricao, categoriaDespesaPrincipal, identificadorCategoriaDespesa, nivelCategoriaDespesa, informarTurma, exigeNivelAdministrativoPlanoOrcamentario, exigeNivelAcademicoPlanoOrcamentario, exigeCentroCustoRequisitante, apresentarPlanoOrcamentario, tributo, cancelamento, informarmanualmenteidentificadorcategoriadespesa ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					if(obj.getIsUmaSubCategoria() && Uteis.isAtributoPreenchido(obj.getCategoriaDespesaPrincipal())){
						sqlInserir.setInt(2, obj.getCategoriaDespesaPrincipal());
					}else{
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getIdentificadorCategoriaDespesa());
					sqlInserir.setString(4, obj.getNivelCategoriaDespesa());
					sqlInserir.setString(5, obj.getInformarTurma());
					sqlInserir.setBoolean(6, obj.getExigeNivelAdministrativoPlanoOrcamentario());
					sqlInserir.setBoolean(7, obj.getExigeNivelAcademicoPlanoOrcamentario());
					sqlInserir.setBoolean(8, obj.getExigeCentroCustoRequisitante());
					sqlInserir.setBoolean(9, obj.getApresentarPlanoOrcamentario());
					sqlInserir.setBoolean(10, obj.getTributo());
					sqlInserir.setBoolean(11, obj.getCancelamento());
					sqlInserir.setBoolean(12, obj.getInformarManualmenteIdentificadorCategoriaDespesa());
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
		} catch (Exception e) {
			throw e;
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CategoriaDespesaVO obj, boolean criarIdentificador, final UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		try {
			validarDados(obj);
			validarNivelCategoriaDespesa(obj, usuarioVO, configuracaoFinanceiroVO.getNrNiveisCategoriaDespesa());
			gerarNovoIdentificadorCategoriaDespesa(obj, criarIdentificador, usuarioVO, configuracaoFinanceiroVO);
			Uteis.validarIdentificador("(Categoria de Despesa)", obj.getIdentificadorCategoriaDespesa(), configuracaoFinanceiroVO.getMascaraCategoriaDespesa());
			CategoriaDespesa.alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE CategoriaDespesa set descricao=?, categoriaDespesaPrincipal=?, identificadorCategoriaDespesa=?, nivelCategoriaDespesa=?, informarTurma=?, exigeNivelAdministrativoPlanoOrcamentario=?, exigeNivelAcademicoPlanoOrcamentario=?, exigeCentroCustoRequisitante=?, apresentarPlanoOrcamentario=?, tributo=?, cancelamento=?, informarmanualmenteidentificadorcategoriadespesa=?  WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					if(obj.getIsUmaSubCategoria() && Uteis.isAtributoPreenchido(obj.getCategoriaDespesaPrincipal())){
						sqlAlterar.setInt(2, obj.getCategoriaDespesaPrincipal());
					}else{
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setString(3, obj.getIdentificadorCategoriaDespesa());
					sqlAlterar.setString(4, obj.getNivelCategoriaDespesa());
					sqlAlterar.setString(5, obj.getInformarTurma());
					sqlAlterar.setBoolean(6, obj.getExigeNivelAdministrativoPlanoOrcamentario());
					sqlAlterar.setBoolean(7, obj.getExigeNivelAcademicoPlanoOrcamentario());
					sqlAlterar.setBoolean(8, obj.getExigeCentroCustoRequisitante());
					sqlAlterar.setBoolean(9, obj.getApresentarPlanoOrcamentario());
					sqlAlterar.setBoolean(10, obj.getTributo());
					sqlAlterar.setBoolean(11, obj.getCancelamento());
					sqlAlterar.setBoolean(12, obj.getInformarManualmenteIdentificadorCategoriaDespesa());
					sqlAlterar.setInt(13, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CategoriaDespesaVO obj, final UsuarioVO usuarioVO) throws Exception {
		try {
			CategoriaDespesa.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM CategoriaDespesa WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarCategoriaDespesas(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalCategoriaDespesas(dataModelo));		
	}

	/**
	 * Consulta Paginada das {@link CategoriaDespesaVO} retornando total de 10 registros.
	 * 
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private List<CategoriaDespesaVO> consultarCategoriaDespesas(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM categoriaDespesa");
		sql.append(" WHERE 1 = 1");
		dataModelo.setLimitePorPagina(10);

		switch (enumCampoConsultaCategoriaDespesa.valueOf(dataModelo.getCampoConsulta())) {
		case DESCRICAO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			sql.append(" AND descricao like UPPER(sem_acentos(?))");
			break;
		case IDENTIFICADOR_CENTRO_DESPESA:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			sql.append(" AND identificadorCategoriaDespesa like UPPER(sem_acentos(?))");
			break;
		default:
			break;
		}

		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Consulta o total de {@link CategoriaDespesaVO} de acordo com o filtro informado.
	 *  
	 * @param dataModelo
	 * @return
	 * @throws Exception
	 */
	private Integer consultarTotalCategoriaDespesas(DataModelo dataModelo) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT COUNT(codigo) as qtde FROM categoriaDespesa");
        sql.append(" WHERE 1 = 1");

        switch (enumCampoConsultaCategoriaDespesa.valueOf(dataModelo.getCampoConsulta())) {
        case DESCRICAO:
			sql.append(" AND descricao like UPPER(sem_acentos(?))");
			break;
		case IDENTIFICADOR_CENTRO_DESPESA:
			sql.append(" AND identificadorCategoriaDespesa like UPPER(sem_acentos(?))");
			break;
		default:
			break;
		}

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }
	
	public List<CategoriaDespesaVO> consultarPorIdentificadorCategoriaDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE upper( identificadorCategoriaDespesa ) like(?) ORDER BY identificadorCategoriaDespesa";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public List<CategoriaDespesaVO> consultarPorIdentificadorExigeCentroCustoRequisitante(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE exigecentrocustorequisitante = true and upper( identificadorCategoriaDespesa ) like(?) ORDER BY identificadorCategoriaDespesa";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	@Override
	public List<CategoriaDespesaVO> consultarPorIdentificadorCategoriaDespesaPassandoCodCategoriaDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, int codCategoriaDespesa) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE upper( identificadorCategoriaDespesa ) like('" + valorConsulta.toUpperCase() + "%') ";
		if (codCategoriaDespesa != 0) {
			sqlStr += "and codigo = " + codCategoriaDespesa + " ORDER BY identificadorCategoriaDespesa";
		} else {
			sqlStr += " ORDER BY identificadorCategoriaDespesa";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	@Override
	public CategoriaDespesaVO consultarPorIdentificadorCategoriaDespesaUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE upper( identificadorCategoriaDespesa ) = '" + valorConsulta.toUpperCase() + "' ORDER BY identificadorCategoriaDespesa";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados);
		}
		return new CategoriaDespesaVO();
	}

	public List<CategoriaDespesaVO> consultarPorIdentificadorCategoriaDespesaPlanoOrcamentario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE upper( identificadorCategoriaDespesa ) like('" + valorConsulta.toUpperCase() + "%') AND apresentarPlanoOrcamentario = true ORDER BY identificadorCategoriaDespesa";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public List<CategoriaDespesaVO> consultarPorNivelCategoriaDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE upper( nivelCategoriaDespesa ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY identificadorCategoriaDespesa";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public List<CategoriaDespesaVO> consultarPorDescricaoExigeCentroCustoRequisitante(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE exigecentrocustorequisitante = true and upper( sem_acentos(descricao) ) like(sem_acentos(?)) ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, "%" + valorConsulta.toUpperCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public List<CategoriaDespesaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE upper( sem_acentos(descricao) ) like(sem_acentos(?)) ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, "%" + valorConsulta.toUpperCase() + "%");
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public List<CategoriaDespesaVO> consultarPorDescricaoPassandoCodCategoriaDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, int codCategoriaDespesa) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE upper( sem_acentos(descricao) ) like(sem_acentos('%" + valorConsulta.toUpperCase() + "%')) ";
		if (codCategoriaDespesa != 0) {
			sqlStr += "and codigo = " + codCategoriaDespesa + " ORDER BY descricao";
		} else {
			sqlStr += "ORDER BY descricao";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	@Override
	public List<CategoriaDespesaVO> consultarPorNomeCategoriaDespesaPrincipal(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT CategoriaDespesa.* FROM CategoriaDespesa inner join CategoriaDespesa cp on cp.codigo = CategoriaDespesa.categoriaDespesaPrincipal WHERE  upper( sem_acentos(cp.descricao) ) like(sem_acentos('%" + valorConsulta.toUpperCase() + "%')) ORDER BY CategoriaDespesa.descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public List<CategoriaDespesaVO> consultarPorDescricaoPlanoOrcamentario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE upper( descricao ) like('%" + valorConsulta.toUpperCase() + "%') AND apresentarPlanoOrcamentario = true ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	
	public List<CategoriaDespesaVO> consultarPorCategoriaDespesaPrincipal(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE categoriaDespesaPrincipal >= " + valorConsulta.intValue() + " ORDER BY categoriaDespesaPrincipal";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public List<CategoriaDespesaVO> consultarPorCategoriaDespesaPrincipalFilho(Integer codigo, boolean controlarAcesso, int nivelMontarDados, boolean limitarUmRegistro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE categoriaDespesaPrincipal = " + codigo.intValue() + " ORDER BY categoriaDespesaPrincipal";
		if (limitarUmRegistro) {
			sqlStr += " limit 1 ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}


	public List<CategoriaDespesaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CategoriaDespesa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public boolean consultarSeExisteCategoriaDespesa() throws Exception {
		String sqlStr = "select codigo from categoriadespesa";
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr).next();
	}

	
	public static List<CategoriaDespesaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<CategoriaDespesaVO> vetResultado = new ArrayList<CategoriaDespesaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}


	public static CategoriaDespesaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		CategoriaDespesaVO obj = new CategoriaDespesaVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setIdentificadorCategoriaDespesa(dadosSQL.getString("identificadorCategoriaDespesa"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setNivelCategoriaDespesa(dadosSQL.getString("nivelCategoriaDespesa"));
		obj.setInformarTurma(dadosSQL.getString("informarTurma"));
		obj.setCategoriaDespesaPrincipal((dadosSQL.getInt("categoriaDespesaPrincipal")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		obj.setExigeNivelAdministrativoPlanoOrcamentario(dadosSQL.getBoolean("exigeNivelAdministrativoPlanoOrcamentario"));
		obj.setExigeNivelAcademicoPlanoOrcamentario(dadosSQL.getBoolean("exigeNivelAcademicoPlanoOrcamentario"));
		obj.setExigeCentroCustoRequisitante(dadosSQL.getBoolean("exigeCentroCustoRequisitante"));
		obj.setApresentarPlanoOrcamentario(dadosSQL.getBoolean("apresentarPlanoOrcamentario"));
		obj.setTributo(dadosSQL.getBoolean("tributo"));
		obj.setCancelamento(dadosSQL.getBoolean("cancelamento"));
		obj.setInformarManualmenteIdentificadorCategoriaDespesa(dadosSQL.getBoolean("informarmanualmenteidentificadorcategoriadespesa"));
		if ((obj.getCategoriaDespesaPrincipal() == null) || (obj.getCategoriaDespesaPrincipal().equals(0))) {
			obj.setIsUmaSubCategoria(false);
		} else {
			obj.setIsUmaSubCategoria(true);
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		getFacadeFactory().getCategoriaDespesaRateioFacade().consultaRapidaPorCategoriaDespesaVO(obj, false, Uteis.NIVELMONTARDADOS_TODOS, null);
		return obj;
	}

	
	public CategoriaDespesaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().getCategoriaDespesaVO(codigoPrm, usuario);
	}
	
	@Override
	public CategoriaDespesaVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM CategoriaDespesa WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( CategoriaDespesa ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return CategoriaDespesa.idEntidade;
	}

	
	public void setIdEntidade(String idEntidade) {
		CategoriaDespesa.idEntidade = idEntidade;
	}

	
	public void validarDados(CategoriaDespesaVO obj) throws ConsistirException, Exception {
		if (!obj.isValidarDados()) {
			return;
		}
		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException("O campo DESCRIÇÃO (Categoria de Despesa) deve ser informado.");
		}
		if(obj.getIsUmaSubCategoria() && !Uteis.isAtributoPreenchido(obj.getCategoriaDespesaPrincipal())){
			throw new ConsistirException("O campo CATEGORIA PRINCIPAL (Categoria de Despesa) deve ser informado.");
		}
		if(obj.getIsUmaSubCategoria() && Uteis.isAtributoPreenchido(obj.getCodigo()) && obj.getCategoriaDespesaPrincipal().equals(obj.getCodigo())){
			throw new ConsistirException("O campo CATEGORIA PRINCIPAL (Categoria de Despesa) deve ser diferente da CATEGORIA DE DESPESA.");
		}
		if(obj.getIsUmaSubCategoria() && Uteis.isAtributoPreenchido(obj.getCodigo()) && !obj.getCategoriaDespesaPrincipal().equals(obj.getCodigo())){
			validarCategoriaDespesaSuperior(obj.getCodigo(), obj.getCategoriaDespesaPrincipal());
		}
		if (!Uteis.isAtributoPreenchido(obj.getIdentificadorCategoriaDespesa())) {
			throw new ConsistirException("O campo IDENTIFICADOR CATEGORIA DESPESA (Categoria de Despesa) deve ser informado.");
		}
		Uteis.checkState(obj.isNivelCategoriaDespesaNaoControlar() && obj.isInformaNivelAcademicoNaoControlar(), "Um dos Níveis Administrativo ou Acadêmio devem ser controlados.");
		if ((!obj.getListaCategoriaDespesaRateioAcademico().isEmpty() || !obj.getListaCategoriaDespesaRateioAdministrativo().isEmpty())) {
			Double totalPorcentagem =  obj.getTotalPorcentagemRateioContabilAcademico() + obj.getTotalPorcentagemRateioContabilAdministrativo();
			Uteis.checkState(!totalPorcentagem.equals(100.00), "A distribuição da porcentagem entre os centros de Negócio Administrativo e Acadêmico deve ser igual a 100%.");
		}		
	}
	
	private void validarCategoriaDespesaSuperior(Integer categoriaDespesaBase, Integer categoriaDespesaSuperior) throws ConsistirException, Exception {
		CategoriaDespesaVO categoriaDespesaVO =  consultarPorChavePrimaria(categoriaDespesaSuperior, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		if(Uteis.isAtributoPreenchido(categoriaDespesaVO) && categoriaDespesaVO.getCodigo().equals(categoriaDespesaBase)) {
			throw new ConsistirException("O campo CATEGORIA PRINCIPAL (Categoria de Despesa) deve ser de uma hierarquia inferior a CATEGORIA DESPESA que está sendo alterada.");
		}else if(Uteis.isAtributoPreenchido(categoriaDespesaVO.getCategoriaDespesaPrincipal())) {
			validarCategoriaDespesaSuperior(categoriaDespesaBase, categoriaDespesaVO.getCategoriaDespesaPrincipal());
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void validarDadosCategoriaDespesaRateio(CategoriaDespesaRateioVO obj) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())) {
			throw new Exception("O campo Unidade Ensino (Categoria Despesa Rateio) deve ser informado.");
		}
		if (obj.getTipoCategoriaDespesaRateioEnum().isAdministrativo() && !Uteis.isAtributoPreenchido(obj.getDepartamentoVO().getCodigo())) {
			throw new Exception("O campo Departamento (Categoria Despesa Rateio) deve ser informado.");
		}
		if (obj.getTipoCategoriaDespesaRateioEnum().isAcademico() && !Uteis.isAtributoPreenchido(obj.getCursoVO().getCodigo())) {
			throw new Exception("O campo Curso (Categoria Despesa Rateio) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getPorcentagem())) {
			throw new Exception("O campo Porcentagem (Categoria Despesa Rateio) deve ser informado.");
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void addCategoriaDespesaRateio(CategoriaDespesaVO obj, CategoriaDespesaRateioVO categoriaRateio, UsuarioVO usuario) throws Exception {
		validarDadosCategoriaDespesaRateio(categoriaRateio);
		categoriaRateio.setCategoriaDespesaVO(obj);
		if (Uteis.isAtributoPreenchido(categoriaRateio.getUnidadeEnsinoVO())) {
			categoriaRateio.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(categoriaRateio.getUnidadeEnsinoVO().getCodigo(), false, usuario));
		}
		if (categoriaRateio.getTipoCategoriaDespesaRateioEnum().isAcademico()) {
			preencherCategoriaDespesaRateio(obj.getListaCategoriaDespesaRateioAcademico(), categoriaRateio);
		} else if (categoriaRateio.getTipoCategoriaDespesaRateioEnum().isAdministrativo()) {
			preencherCategoriaDespesaRateio(obj.getListaCategoriaDespesaRateioAdministrativo(), categoriaRateio);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void montarListaSelectItemTipoNivelCentroResultadoEnum(CategoriaDespesaVO obj, List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum) throws Exception {
		if(obj.isNivelCategoriaDespesaDepartamento()){
			listaSelectItemTipoNivelCentroResultadoEnum.add(new SelectItem(TipoNivelCentroResultadoEnum.DEPARTAMENTO, UteisJSF.internacionalizarEnum(TipoNivelCentroResultadoEnum.DEPARTAMENTO)));
		}else if(obj.isNivelCategoriaDespesaUnidadeEnsino()){
			listaSelectItemTipoNivelCentroResultadoEnum.add(new SelectItem(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO, UteisJSF.internacionalizarEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO)));
		}else if(obj.isNivelCategoriaDespesaFuncionario()){
			listaSelectItemTipoNivelCentroResultadoEnum.add(new SelectItem(TipoNivelCentroResultadoEnum.DEPARTAMENTO, UteisJSF.internacionalizarEnum(TipoNivelCentroResultadoEnum.DEPARTAMENTO)));
		}
		if(obj.isInformaNivelAcademicoCurso()){
			listaSelectItemTipoNivelCentroResultadoEnum.add(new SelectItem(TipoNivelCentroResultadoEnum.CURSO, UteisJSF.internacionalizarEnum(TipoNivelCentroResultadoEnum.CURSO)));
		}else if(obj.isInformaNivelAcademicoCursoTurno()){
			listaSelectItemTipoNivelCentroResultadoEnum.add(new SelectItem(TipoNivelCentroResultadoEnum.CURSO_TURNO, UteisJSF.internacionalizarEnum(TipoNivelCentroResultadoEnum.CURSO_TURNO)));
		}else if(obj.isInformaNivelAcademicoTurma()){
			listaSelectItemTipoNivelCentroResultadoEnum.add(new SelectItem(TipoNivelCentroResultadoEnum.TURMA, UteisJSF.internacionalizarEnum(TipoNivelCentroResultadoEnum.TURMA)));
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void preencherCategoriaDespesaRateio(List<CategoriaDespesaRateioVO> lista, CategoriaDespesaRateioVO categoriaRateio) throws Exception {
		int index = 0;
		for (CategoriaDespesaRateioVO objExistente : lista) {
			if (objExistente.equalsCampoSelecaoLista(categoriaRateio)) {
				lista.set(index, categoriaRateio);
				return;
			}
			index++;
		}
		lista.add(categoriaRateio);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removeCategoriaDespesaRateio(CategoriaDespesaVO obj, CategoriaDespesaRateioVO categoriaRateio, UsuarioVO usuario) throws Exception {
		Iterator<CategoriaDespesaRateioVO> i = null;
		if (categoriaRateio.getTipoCategoriaDespesaRateioEnum().isAcademico()) {
			i = obj.getListaCategoriaDespesaRateioAcademico().iterator();
		} else if (categoriaRateio.getTipoCategoriaDespesaRateioEnum().isAdministrativo()) {
			i = obj.getListaCategoriaDespesaRateioAdministrativo().iterator();
		}
		while (i.hasNext()) {
			CategoriaDespesaRateioVO objExistente = (CategoriaDespesaRateioVO) i.next();
			if (objExistente.equalsCampoSelecaoLista(categoriaRateio)) {
				i.remove();
				return;
			}
		}
	}

	public List<CategoriaDespesaVO> consultaRapidaCategoriaDespDRE() throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct categoriadespesa.descricao, categoriadespesa.identificadorcategoriadespesa from turma ");
        sql.append("left join contapagar on contapagar.turma = turma.codigo ");
        sql.append("inner join categoriadespesa on categoriadespesa.codigo = contapagar.centrodespesa ");
        sql.append("left join contareceber on contareceber.turma = turma.codigo ");	        
        sql.append(" where 1=1  ");
        sql.append(" and contareceber.datavencimento >= '2017-10-01' and contareceber.datavencimento <= '2017-10-31' ");
        sql.append(" and contapagar.datavencimento >= '2017-10-01' and contapagar.datavencimento <= '2017-10-31' ");
        sql.append(" order by categoriadespesa.identificadorcategoriadespesa ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<CategoriaDespesaVO> vetResultado = new ArrayList<CategoriaDespesaVO>(0);
        while (tabelaResultado.next()) {
        	CategoriaDespesaVO obj = new CategoriaDespesaVO();
            obj.setIdentificadorCategoriaDespesa(tabelaResultado.getString("identificadorcategoriadespesa"));
            obj.setDescricao(tabelaResultado.getString("descricao"));
            vetResultado.add(obj);
        }
        return vetResultado;		
	}		
		
	@Override
	public CategoriaDespesaVO consultaCategoriaDespesaPadraoConfiguracaoFinanceiroPorUnidadeEnsinoTipoCategoria(Integer unidadeEnsino, Integer operadoraCartao, String campoCategoriaDespesa) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct categoriadespesa.*, 1 as ordem from unidadeensino ");
        sql.append(" inner join configuracoes on configuracoes.codigo = ( ");
		sql.append(" case when unidadeensino.configuracoes is not null then unidadeensino.configuracoes else  ");
		sql.append(" (select c.codigo from configuracoes c where c.padrao = true ) end) ");		
		sql.append(" inner join configuracaofinanceiro on configuracaofinanceiro.configuracoes = configuracoes.codigo ");
		sql.append(" inner join configuracaofinanceirocartao on configuracaofinanceiro.codigo = configuracaofinanceirocartao.configuracaofinanceiro ");
        sql.append(" inner join categoriadespesa on categoriadespesa.codigo = configuracaofinanceirocartao.categoriaDespesa");        	        
        sql.append(" where unidadeensino.codigo = ").append(unidadeEnsino);        
        sql.append(" union ");
        sql.append(" select distinct categoriadespesa.*, 2 as ordem from unidadeensino ");
        sql.append(" inner join configuracoes on configuracoes.codigo = ( ");
		sql.append(" case when unidadeensino.configuracoes is not null then unidadeensino.configuracoes else  ");
		sql.append(" (select c.codigo from configuracoes c where c.padrao = true ) end) ");		
		sql.append(" inner join configuracaofinanceiro on configuracaofinanceiro.configuracoes = configuracoes.codigo ");
        sql.append(" inner join categoriadespesa on categoriadespesa.codigo = configuracaofinanceiro.").append(campoCategoriaDespesa);        	        
        sql.append(" where unidadeensino.codigo = ").append(unidadeEnsino);
        sql.append(" order by ordem limit 1 ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if(tabelaResultado.next()) {
        	return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        }
        return null;
	}
	
	private void gerarNovoIdentificadorCategoriaDespesa(CategoriaDespesaVO categoriaDespesaVO, boolean criarIdentificador, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		CategoriaDespesaVO categoriaDespesaVO2 = consultarPorChavePrimariaUnica(categoriaDespesaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		if ((categoriaDespesaVO.getIsUmaSubCategoria() && Uteis.isAtributoPreenchido(categoriaDespesaVO.getCategoriaDespesaPrincipal())
				&& categoriaDespesaVO2.getIsUmaSubCategoria() && Uteis.isAtributoPreenchido(categoriaDespesaVO2.getCategoriaDespesaPrincipal())
				&& !categoriaDespesaVO.getCategoriaDespesaPrincipal().equals(categoriaDespesaVO2.getCategoriaDespesaPrincipal()))
				|| (categoriaDespesaVO.getIsUmaSubCategoria() && !categoriaDespesaVO2.getIsUmaSubCategoria())
				|| (!categoriaDespesaVO.getIsUmaSubCategoria() && categoriaDespesaVO2.getIsUmaSubCategoria())
				|| (!categoriaDespesaVO.getInformarManualmenteIdentificadorCategoriaDespesa() && categoriaDespesaVO2.getInformarManualmenteIdentificadorCategoriaDespesa())) {
			if (Uteis.isAtributoPreenchido(consultarPorCategoriaDespesaPrincipalFilho(categoriaDespesaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, true, usuarioVO))) {
				throw new ConsistirException("Não é possível alterar a Categoria de Despesa Principal, pois existem dependentes desta Categoria de Despesa.");
			} else if (criarIdentificador) {
				gerarIdentificadorMascaraCategoriaDespesa2(categoriaDespesaVO, usuarioVO, configuracaoFinanceiroVO);
			}
		}
	}
}
