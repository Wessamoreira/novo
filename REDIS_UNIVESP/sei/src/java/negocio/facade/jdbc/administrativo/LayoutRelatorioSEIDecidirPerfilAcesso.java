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

import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirPerfilAcessoVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.administrativo.LayoutRelatorioSEIDecidirPerfilAcessoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class LayoutRelatorioSEIDecidirPerfilAcesso extends SuperFacade<LayoutRelatorioSEIDecidirPerfilAcessoVO> implements LayoutRelatorioSEIDecidirPerfilAcessoInterfaceFacade<LayoutRelatorioSEIDecidirPerfilAcessoVO> {

	private static final long serialVersionUID = 5288480508571016808L;

	protected static String idEntidade;

	public LayoutRelatorioSEIDecidirPerfilAcesso() throws Exception {
		super();
		setIdEntidade("LayoutRelatorioSEIDecidirPerfilAcesso");
	}

	public LayoutRelatorioSEIDecidirPerfilAcesso(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("LayoutRelatorioSEIDecidirPerfilAcesso");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(LayoutRelatorioSEIDecidirPerfilAcessoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(LayoutRelatorioSEIDecidirPerfilAcessoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			LayoutRelatorioSEIDecidirPerfilAcesso.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			incluir(obj, "LayoutRelatorioSEIDecidirPerfilAcesso", new AtributoPersistencia()
					.add("perfilacesso", obj.getPerfilAcessoVO())
					.add("LayoutRelatorioSEIDecidir", obj.getLayoutRelatorioSEIDecidirVO())
					, usuarioVO);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	public void alterar(LayoutRelatorioSEIDecidirPerfilAcessoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		LayoutRelatorioSEIDecidirPerfilAcesso.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "LayoutRelatorioSEIDecidirPerfilAcesso", new AtributoPersistencia()
				.add("perfilacesso", obj.getPerfilAcessoVO())
				.add("LayoutRelatorioSEIDecidir", obj.getLayoutRelatorioSEIDecidirVO())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
	}

	/**
	 * Exclui o {@link LayoutRelatorioSEIDecidirPerfilAcessoVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(LayoutRelatorioSEIDecidirPerfilAcessoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		LayoutRelatorioSEIDecidirPerfilAcesso.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM LayoutRelatorioSEIDecidirPerfilAcesso WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirPorLayoutRelatorioSeiDecidir(LayoutRelatorioSEIDecidirVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		StringBuilder sql = new StringBuilder("DELETE FROM LayoutRelatorioSEIDecidirPerfilAcesso WHERE ((layoutrelatorioseidecidir = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		
	}

	/**
	 * Consulta o {@link LayoutRelatorioSEIDecidirPerfilAcessoVO} pelo  codigo informado.
	 */
	@Override
	public LayoutRelatorioSEIDecidirPerfilAcessoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Consulta List de {@link LayoutRelatorioSEIDecidirPerfilAcessoVO} pelo  codigo do LayoutRelatorioSeiDecidir informado.
	 * @param Integer idLayoutRelatorioSeiDecidir
	 */
	@Override
	public List<LayoutRelatorioSEIDecidirPerfilAcessoVO> consultarPorLayoutRelatorioSeiDecidir(Integer idLayoutRelatorioSeiDecidir) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE layoutrelatorioseidecidir = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), idLayoutRelatorioSeiDecidir);

		return montarDadosLista(tabelaResultado);
	}

	/**
	 * Valida os campos obrigatorios do {@link LayoutRelatorioSEIDecidirPerfilAcessoVO}
	 */
	@Override
	public void validarDados(LayoutRelatorioSEIDecidirPerfilAcessoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getPerfilAcessoVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_LayoutRelatorioSEIDecidirPerfilAcesso_perfilAcesso"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getLayoutRelatorioSEIDecidirVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_LayoutRelatorioSEIDecidirPerfilAcesso_LayoutRelatorioSeiDecidir"));
		}
	}

	/**
	 * Monta a lista de {@link LayoutRelatorioSEIDecidirPerfilAcessoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<LayoutRelatorioSEIDecidirPerfilAcessoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<LayoutRelatorioSEIDecidirPerfilAcessoVO> listaLayoutRelatorioSEIDeicidirPerfilAcesso = new ArrayList<>();

        while(tabelaResultado.next()) {
        	listaLayoutRelatorioSEIDeicidirPerfilAcesso.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return listaLayoutRelatorioSEIDeicidirPerfilAcesso;
	}

	@Override
	public LayoutRelatorioSEIDecidirPerfilAcessoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		LayoutRelatorioSEIDecidirPerfilAcessoVO obj = new LayoutRelatorioSEIDecidirPerfilAcessoVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("perfilacesso"))) {
			obj.setPerfilAcessoVO(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimariaSemPermissao(tabelaResultado.getInt("perfilacesso"), null));
		}
		obj.getLayoutRelatorioSEIDecidirVO().setCodigo(tabelaResultado.getInt("layoutrelatorioseidecidir"));
		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM LayoutRelatorioSEIDecidirPerfilAcesso");
		return sql.toString();
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		LayoutRelatorioSEIDecidirPerfilAcesso.idEntidade = idEntidade;
	}

}
