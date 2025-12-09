package negocio.facade.jdbc.estagio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.TipoConcedenteVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.estagio.TipoConcedenteInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TipoConcedente extends ControleAcesso implements TipoConcedenteInterfaceFacade {

	/**
	 * import negocio.comuns.academico.TipoConcedenteVO;
	 */
	private static final long serialVersionUID = 1147553999041578095L;

	private static String idEntidade = "TipoConcedente";

	public static String getIdEntidade() {
		return TipoConcedente.idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void valiarDados(TipoConcedenteVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNome()), UteisJSF.internacionalizar("msg_TipoConcedente_nome"));
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(TipoConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			valiarDados(obj);
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "tipoConcedente", new AtributoPersistencia().add("nome", obj.getNome())
					.add("cnpjObrigatorio", obj.isCnpjObrigatorio())
					.add("codigoMECObrigatorio", obj.getCodigoMECObrigatorio())
					.add("permitirCadastroConcedente", obj.getPermitirCadastroConcedente())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipoConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "tipoConcedente", new AtributoPersistencia().add("nome", obj.getNome())
					.add("cnpjObrigatorio", obj.isCnpjObrigatorio())
					.add("codigoMECObrigatorio", obj.getCodigoMECObrigatorio())
					.add("permitirCadastroConcedente", obj.getPermitirCadastroConcedente())
					, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TipoConcedenteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM tipoConcedente WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, TipoConcedenteVO obj) throws Exception {
		dataModelo.getListaConsulta().clear();
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultaRapidaPorFiltros(obj, dataModelo));
	}

	private List<TipoConcedenteVO> consultaRapidaPorFiltros(TipoConcedenteVO obj, DataModelo dataModelo) {
		try {
			consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1=1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY tipoConcedente.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			montarTotalizadorConsultaBasica(dataModelo, tabelaResultado);
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosParaConsulta(TipoConcedenteVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sqlStr.append(" and tipoConcedente.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getNome())) {
			sqlStr.append(" and lower(sem_acentos(tipoConcedente.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getNome().toLowerCase() + PERCENT);
		}

		
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public TipoConcedenteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where tipoConcedente.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( TipoConcedenteVO ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder(getSelectTotalizadorConsultaBasica());
		sql.append(" tipoConcedente.codigo as \"tipoConcedente.codigo\",  ");
		sql.append(" tipoConcedente.nome as \"tipoConcedente.nome\", ");
		sql.append(" tipoConcedente.cnpjObrigatorio as \"tipoConcedente.cnpjObrigatorio\", ");
		sql.append(" tipoConcedente.codigoMECObrigatorio as \"tipoConcedente.codigoMECObrigatorio\", ");
		sql.append(" tipoConcedente.permitirCadastroConcedente as \"tipoConcedente.permitirCadastroConcedente\" ");
		sql.append(" FROM tipoConcedente ");

		return sql;
	}

	private List<TipoConcedenteVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<TipoConcedenteVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private TipoConcedenteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		TipoConcedenteVO obj = new TipoConcedenteVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("tipoConcedente.codigo"));
		obj.setNome(dadosSQL.getString("tipoConcedente.nome"));
		obj.setCnpjObrigatorio(dadosSQL.getBoolean("tipoConcedente.cnpjObrigatorio"));
		obj.setCodigoMECObrigatorio(dadosSQL.getBoolean("tipoConcedente.codigoMECObrigatorio"));
		obj.setPermitirCadastroConcedente(dadosSQL.getBoolean("tipoConcedente.permitirCadastroConcedente"));
		return obj;

	}

}
