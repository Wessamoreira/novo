package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.SituacaoComplementarHistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.SituacaoComplementarHistoricoInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class SituacaoComplementarHistorico extends ControleAcesso implements SituacaoComplementarHistoricoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1692332116320773702L;
	protected static String idEntidade = "SituacaoComplementarHistorico";

	public void validarDados(SituacaoComplementarHistoricoVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNome()), "O campo Nome (SituacaoComplementarHistorico) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSigla()), "O campo Sigla (SituacaoComplementarHistorico) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSituacaoHistorico()), "O campo Situação Historico (SituacaoComplementarHistorico) não foi informado.");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(SituacaoComplementarHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	private void incluir(SituacaoComplementarHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			incluir(obj, "situacaoComplementarHistorico",
					new AtributoPersistencia()
							.add("nome", obj.getNome())
							.add("sigla", obj.getSigla())
							.add("situacaoHistorico", obj.getSituacaoHistorico().name()),
					usuarioVO);
			obj.setNovoObj(false);
		} catch (Exception e) {
			obj.setNovoObj(true);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}

	}

	private void alterar(SituacaoComplementarHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		incluir(getIdEntidade(), verificarAcesso, usuarioVO);
		alterar(obj, "situacaoComplementarHistorico",
				new AtributoPersistencia()
						.add("nome", obj.getNome())
						.add("sigla", obj.getSigla())
						.add("situacaoHistorico", obj.getSituacaoHistorico().name()),
				new AtributoPersistencia().add("codigo", obj.getCodigo()),
				usuarioVO);

	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void excluir(SituacaoComplementarHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM situacaoComplementarHistorico WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public SituacaoComplementarHistoricoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT *  ");
			sql.append(" FROM situacaoComplementarHistorico ");
			sql.append(" WHERE codigo = ").append(codigoPrm).append(" ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( SituacaoComplementarHistoricoVO ).");
			}
			SituacaoComplementarHistoricoVO obj = new SituacaoComplementarHistoricoVO();
			montarDados(obj, tabelaResultado, nivelMontarDados);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<SituacaoComplementarHistoricoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<SituacaoComplementarHistoricoVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			SituacaoComplementarHistoricoVO obj = new SituacaoComplementarHistoricoVO();
			montarDados(obj, tabelaResultado, nivelMontarDados);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDados(SituacaoComplementarHistoricoVO obj, SqlRowSet dadosSQL, int nivelMontarDados) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("NotaFiscalEntradaImposto.codigo"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return SituacaoComplementarHistorico.idEntidade;
	}

}
