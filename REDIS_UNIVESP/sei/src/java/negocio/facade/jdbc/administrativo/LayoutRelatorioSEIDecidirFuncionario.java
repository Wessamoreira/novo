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

import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirFuncionarioVO;
import negocio.comuns.administrativo.LayoutRelatorioSEIDecidirVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.administrativo.LayoutRelatorioSEIDecidirFuncionarioInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class LayoutRelatorioSEIDecidirFuncionario extends SuperFacade<LayoutRelatorioSEIDecidirFuncionarioVO> implements LayoutRelatorioSEIDecidirFuncionarioInterfaceFacade<LayoutRelatorioSEIDecidirFuncionarioVO> {

	private static final long serialVersionUID = 5288480508571016808L;

	protected static String idEntidade;

	public LayoutRelatorioSEIDecidirFuncionario() throws Exception {
		super();
		setIdEntidade("LayoutRelatorioSEIDecidirFuncionario");
	}

	public LayoutRelatorioSEIDecidirFuncionario(Conexao conexao, FacadeFactory facade) {
		super();
		setConexao(conexao);
		setFacadeFactory(facade);
		setIdEntidade("LayoutRelatorioSEIDecidirFuncionario");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(LayoutRelatorioSEIDecidirFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(LayoutRelatorioSEIDecidirFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			LayoutRelatorioSEIDecidirFuncionario.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			incluir(obj, "LayoutRelatorioSEIDecidirFuncionario", new AtributoPersistencia()
					.add("Funcionario", obj.getFuncionarioVO())
					.add("LayoutRelatorioSEIDecidir", obj.getLayoutRelatorioSEIDecidirVO())
					, usuarioVO);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	public void alterar(LayoutRelatorioSEIDecidirFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		LayoutRelatorioSEIDecidirFuncionario.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "LayoutRelatorioSEIDecidirFuncionario", new AtributoPersistencia()
				.add("Funcionario", obj.getFuncionarioVO())
				.add("LayoutRelatorioSEIDecidir", obj.getLayoutRelatorioSEIDecidirVO())
				, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
	}

	/**
	 * Exclui o {@link LayoutRelatorioSEIDecidirFuncionarioVO} pelo codigo informado.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(LayoutRelatorioSEIDecidirFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		LayoutRelatorioSEIDecidirFuncionario.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM LayoutRelatorioSEIDecidirFuncionario WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirPorLayoutRelatorioSeiDecidir(LayoutRelatorioSEIDecidirVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		LayoutRelatorioSEIDecidirFuncionario.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM LayoutRelatorioSEIDecidirFuncionario WHERE ((layoutrelatorioseidecidir = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		
	}

	/**
	 * Consulta o {@link LayoutRelatorioSEIDecidirFuncionarioVO} pelo  codigo informado.
	 */
	@Override
	public LayoutRelatorioSEIDecidirFuncionarioVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Consulta List de {@link LayoutRelatorioSEIDecidirFuncionarioVO} pelo  codigo do LayoutRelatorioSeiDecidir informado.
	 * @param Integer idLayoutRelatorioSeiDecidir
	 */
	@Override
	public List<LayoutRelatorioSEIDecidirFuncionarioVO> consultarPorLayoutRelatorioSeiDecidir(Integer idLayoutRelatorioSeiDecidir) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE layoutrelatorioseidecidir = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), idLayoutRelatorioSeiDecidir);

		return montarDadosLista(tabelaResultado);
	}

	/**
	 * Valida os campos obrigatorios do {@link LayoutRelatorioSEIDecidirFuncionarioVO}
	 */
	@Override
	public void validarDados(LayoutRelatorioSEIDecidirFuncionarioVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_LayoutRelatorioSEIDecidirFuncionario_perfilAcesso"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getLayoutRelatorioSEIDecidirVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_LayoutRelatorioSEIDecidirFuncionario_LayoutRelatorioSeiDecidir"));
		}
	}
 
	/**
	 * Monta a lista de {@link LayoutRelatorioSEIDecidirFuncionarioVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<LayoutRelatorioSEIDecidirFuncionarioVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<LayoutRelatorioSEIDecidirFuncionarioVO> listaLayoutRelatorioSEIDeicidirFuncionario = new ArrayList<>();

        while(tabelaResultado.next()) {
        	listaLayoutRelatorioSEIDeicidirFuncionario.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return listaLayoutRelatorioSEIDeicidirFuncionario;
	}

	@Override
	public LayoutRelatorioSEIDecidirFuncionarioVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		LayoutRelatorioSEIDecidirFuncionarioVO obj = new LayoutRelatorioSEIDecidirFuncionarioVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("Funcionario"))) {
			obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(tabelaResultado.getInt("Funcionario"), 0 , false, Uteis.NIVELMONTARDADOS_COMBOBOX,  null));
		}
		obj.getLayoutRelatorioSEIDecidirVO().setCodigo(tabelaResultado.getInt("layoutrelatorioseidecidir"));
		

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM LayoutRelatorioSEIDecidirFuncionario");
		return sql.toString();
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		LayoutRelatorioSEIDecidirFuncionario.idEntidade = idEntidade;
	}

}
