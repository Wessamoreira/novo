package negocio.facade.jdbc.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirArquivoVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.administrativo.LayoutRelatorioSEIDecidirArquivoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class layoutRelatorioSEIDecidirArquivo extends SuperFacade<LayoutRelatorioSEIDecidirArquivoVO> implements LayoutRelatorioSEIDecidirArquivoInterfaceFacade<LayoutRelatorioSEIDecidirArquivoVO> {

	private static final long serialVersionUID = 5288480508571016808L;

	protected static String idEntidade;

	public layoutRelatorioSEIDecidirArquivo() throws Exception {
		super();
		setIdEntidade("layoutRelatorioSEIDecidirArquivo");
	}

	public layoutRelatorioSEIDecidirArquivo(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("layoutRelatorioSEIDecidirArquivo");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(LayoutRelatorioSEIDecidirArquivoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(LayoutRelatorioSEIDecidirArquivoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			layoutRelatorioSEIDecidirArquivo.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			incluir(obj, "layoutRelatorioSEIDecidirArquivo", new AtributoPersistencia()
					.add("arquivo", obj.getArquivoVO())
					.add("LayoutRelatorioSEIDecidir", obj.getLayoutRelatorioSEIDecidirVO())
					.add("LayoutRelatorioSEIDecidirsuperior", obj.getLayoutRelatorioSEIDecidirSuperiorVO())
					.add("utilizarComoSumario", obj.getUtilizarComoSumario())
					.add("utilizarFiltrosPrincipais", obj.getUtilizarFiltrosPrincipais())
					.add("sqlWhere", obj.getUtilizarComoSumario() ? "" : obj.getSqlWhere())
					, usuarioVO);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	public void alterar(LayoutRelatorioSEIDecidirArquivoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		layoutRelatorioSEIDecidirArquivo.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "layoutRelatorioSEIDecidirArquivo", new AtributoPersistencia()
				.add("arquivo", obj.getArquivoVO())
				.add("LayoutRelatorioSEIDecidir", obj.getLayoutRelatorioSEIDecidirVO())
				.add("LayoutRelatorioSEIDecidirsuperior", obj.getLayoutRelatorioSEIDecidirSuperiorVO())
				.add("utilizarComoSumario", obj.getUtilizarComoSumario())
				.add("utilizarFiltrosPrincipais", obj.getUtilizarFiltrosPrincipais())
				.add("sqlWhere",  obj.getUtilizarComoSumario() ? "" : obj.getSqlWhere())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
	}

	/**
	 * Exclui o {@link layoutRelatorioSEIDecidirArquivoVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(LayoutRelatorioSEIDecidirArquivoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		layoutRelatorioSEIDecidirArquivo.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM layoutRelatorioSEIDecidirArquivo WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirPorLayoutRelatorioSeiDecidir(LayoutRelatorioSEIDecidirVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		StringBuilder sql = new StringBuilder("DELETE FROM layoutRelatorioSEIDecidirArquivo WHERE ((layoutrelatorioseidecidir = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		
	}

	/**
	 * Consulta o {@link layoutRelatorioSEIDecidirArquivoVO} pelo  codigo informado.
	 */
	@Override
	public LayoutRelatorioSEIDecidirArquivoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Consulta List de {@link layoutRelatorioSEIDecidirArquivoVO} pelo  codigo do LayoutRelatorioSeiDecidir informado.
	 * @param Integer idLayoutRelatorioSeiDecidir
	 */
	@Override
	public List<LayoutRelatorioSEIDecidirArquivoVO> consultarPorLayoutRelatorioSeiDecidir(Integer idLayoutRelatorioSeiDecidir) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE layoutrelatorioseidecidir = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), idLayoutRelatorioSeiDecidir);

		return montarDadosLista(tabelaResultado);
	}

	/**
	 * Valida os campos obrigatorios do {@link layoutRelatorioSEIDecidirArquivoVO}
	 */
	@Override
	public void validarDados(LayoutRelatorioSEIDecidirArquivoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getArquivoVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_LayoutRelatorioSEIDecidirArquivo_perfilAcesso"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getLayoutRelatorioSEIDecidirVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_LayoutRelatorioSEIDecidirArquivo_LayoutRelatorioSeiDecidir"));
		}
		if(obj.getUtilizarComoSumario()) {
			obj.setSqlWhere("");
	}
	}

	/**
	 * Monta a lista de {@link layoutRelatorioSEIDecidirArquivoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<LayoutRelatorioSEIDecidirArquivoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<LayoutRelatorioSEIDecidirArquivoVO> listaLayoutRelatorioSEIDeicidirPerfilAcesso = new ArrayList<>();

        while(tabelaResultado.next()) {
        	listaLayoutRelatorioSEIDeicidirPerfilAcesso.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return listaLayoutRelatorioSEIDeicidirPerfilAcesso;
	}

	@Override
	public LayoutRelatorioSEIDecidirArquivoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		LayoutRelatorioSEIDecidirArquivoVO obj = new LayoutRelatorioSEIDecidirArquivoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setUtilizarComoSumario(tabelaResultado.getBoolean("utilizarComoSumario"));
		obj.setUtilizarFiltrosPrincipais(tabelaResultado.getBoolean("utilizarFiltrosPrincipais"));		
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("arquivo"))) {
			obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("arquivo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("layoutrelatorioseidecidir"))) {
			obj.setLayoutRelatorioSEIDecidirVO(getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("layoutrelatorioseidecidir"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("layoutrelatorioseidecidirsuperior"))) {
			obj.setLayoutRelatorioSEIDecidirSuperiorVO(getFacadeFactory().getLayoutRelatorioSEIDecidirInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("layoutrelatorioseidecidirsuperior"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, null));
		}
		obj.setSqlWhere(tabelaResultado.getString("sqlWhere"));

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM layoutRelatorioSEIDecidirArquivo");
		return sql.toString();
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		layoutRelatorioSEIDecidirArquivo.idEntidade = idEntidade;
	}

}
