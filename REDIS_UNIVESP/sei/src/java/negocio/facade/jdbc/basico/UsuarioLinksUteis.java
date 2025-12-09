package negocio.facade.jdbc.basico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LinksUteisVO;
import negocio.comuns.basico.UsuarioLinksUteisVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.administrativo.LinksUteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.UsuarioLinksUteisInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class UsuarioLinksUteis extends ControleAcesso implements UsuarioLinksUteisInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public static String getIdEntidade() {
		return LinksUteis.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		LinksUteis.idEntidade = idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(UsuarioLinksUteisVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			UsuarioLinksUteis.incluir(getIdEntidade(), false, usuarioVO);
			incluir(obj, "UsuarioLinksUteis", new AtributoPersistencia()
				.add("usuario", obj.getUsuarioVO())
				.add("linksuteis", obj.getLinksUteisVO()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<UsuarioLinksUteisVO> consultarPorUsuarioLinksUteis(Integer linksUteis, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
		String sqlStr = "SELECT usuariolinksuteis.codigo, pessoa.email, pessoa.cpf, usuariolinksuteis.linksuteis, usuariolinksuteis.usuario, usuario.nome as usuario_nome, usuario.codigo as usuario_codigo, pessoa.codigo as pessoa_codigo FROM usuariolinksuteis"
				+ " inner join usuario on usuario.codigo = usuariolinksuteis.usuario"
				+ " inner join pessoa on pessoa.codigo = usuario.pessoa"
				+ " where usuariolinksuteis.linksuteis = ? order by codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, linksUteis);
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));

	}
	
	
	public static List<UsuarioLinksUteisVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UsuarioLinksUteisVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		return vetResultado;
	}
	

	public static UsuarioLinksUteisVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		UsuarioLinksUteisVO obj = new UsuarioLinksUteisVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getLinksUteisVO().setCodigo(dadosSQL.getInt("linksuteis"));
		obj.getUsuarioVO().getPessoa().setNome(dadosSQL.getString("usuario_nome"));
		obj.getUsuarioVO().setCodigo(dadosSQL.getInt("usuario_codigo"));
		obj.getUsuarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa_codigo"));
		obj.getUsuarioVO().getPessoa().setEmail(dadosSQL.getString("email"));
		obj.getUsuarioVO().getPessoa().setCPF(dadosSQL.getString("cpf"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}



	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(UsuarioLinksUteisVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			UsuarioLinksUteis.alterar(getIdEntidade(), false, usuarioVO);
			alterar(obj, "UsuarioLinksUteis", new AtributoPersistencia()
				.add("usuario", obj.getUsuarioVO())
				.add("linksuteis", obj.getLinksUteisVO()), new AtributoPersistencia()
				.add("codigo", obj.getCodigo()), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UsuarioLinksUteisVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			UsuarioLinksUteis.excluir(getIdEntidade(), false, usuarioVO);
			String sql = "DELETE FROM usuariolinksuteis WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<UsuarioLinksUteisVO> usuarioLinksUteisVOs, LinksUteisVO linksUteisVO, UsuarioVO usuarioVO) throws Exception {
		for (UsuarioLinksUteisVO usuarioLinksUteisVO : usuarioLinksUteisVOs) {
			usuarioLinksUteisVO.setLinksUteisVO(linksUteisVO);
			if (!Uteis.isAtributoPreenchido(usuarioLinksUteisVO.getCodigo())) {
				incluir(usuarioLinksUteisVO, usuarioVO);
			} else {
				alterar(usuarioLinksUteisVO, usuarioVO);
			}
		}
	}
}