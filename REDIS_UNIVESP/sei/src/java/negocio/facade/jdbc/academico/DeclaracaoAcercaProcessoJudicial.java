package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DeclaracaoAcercaProcessoJudicialVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DeclaracaoAcercaProcessoJudicialInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>DeclaracaoAcercaProcessoJudicialVO</code>. Responsável
 * por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>DeclaracaoAcercaProcessoJudicialVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * 
 * @see DeclaracaoAcercaProcessoJudicialVO
 * @see ControleAcesso
 * @author Felipi Alves
 */

@Repository
@Scope("singleton")
@Lazy
public class DeclaracaoAcercaProcessoJudicial extends ControleAcesso implements DeclaracaoAcercaProcessoJudicialInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public DeclaracaoAcercaProcessoJudicial() throws Exception {
		super();
		setIdEntidade("DeclaracaoAcercaProcessoJudicial");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		DeclaracaoAcercaProcessoJudicial.idEntidade = idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void incluir(DeclaracaoAcercaProcessoJudicialVO obj, UsuarioVO usuario) throws Exception {
		incluir(obj, "declaracaoacercaprocessojudicial", new AtributoPersistencia().add("expedicaodiploma", obj.getExpedicaoDiploma().getCodigo()).add("declaracao", obj.getDeclaracao()), usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(DeclaracaoAcercaProcessoJudicialVO obj, UsuarioVO usuario) throws Exception {
		alterar(obj, "declaracaoacercaprocessojudicial", new AtributoPersistencia().add("expedicaodiploma", obj.getExpedicaoDiploma().getCodigo()).add("declaracao", obj.getDeclaracao()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
	}

	@Override
	public void excluir(List<DeclaracaoAcercaProcessoJudicialVO> declaracaoAcercaProcessoJudicialVOs, UsuarioVO usuario) throws Exception {
		if (!declaracaoAcercaProcessoJudicialVOs.isEmpty()) {
			for (DeclaracaoAcercaProcessoJudicialVO obj : declaracaoAcercaProcessoJudicialVOs.stream().filter(d -> d.getSelecionado()).collect(Collectors.toList())) {
				if (Uteis.isAtributoPreenchido(obj.getCodigo()) && obj.getSelecionado()) {
					excluir("declaracaoacercaprocessojudicial", new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
				}
				if (obj.getSelecionado()) {
					declaracaoAcercaProcessoJudicialVOs.removeIf(d -> d.getSelecionado() && obj.getSelecionado() && (d.getCodigo().equals(obj.getCodigo()) || d.getDeclaracao().equals(obj.getDeclaracao())));
				}
			}
		}
	}

	@Override
	public void excluirPorExpedicaoDiploma(Integer codigoExpedicao, UsuarioVO usuario) throws Exception {
		excluir("declaracaoacercaprocessojudicial", new AtributoPersistencia().add("expedicaoDiploma", codigoExpedicao), usuario);
	}

	@Override
	public List<DeclaracaoAcercaProcessoJudicialVO> consultar(Integer codigoExpedicao) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo, declaracao, expedicaoDiploma FROM declaracaoacercaprocessojudicial ");
		sql.append("WHERE expedicaodiploma = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoExpedicao);
		return (montarDadosConsulta(tabelaResultado));
	}

	private static DeclaracaoAcercaProcessoJudicialVO montarDados(SqlRowSet dadosSQL) throws Exception {
		DeclaracaoAcercaProcessoJudicialVO obj = new DeclaracaoAcercaProcessoJudicialVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDeclaracao(dadosSQL.getString("declaracao"));
		obj.getExpedicaoDiploma().setCodigo(dadosSQL.getInt("expedicaoDiploma"));
		return obj;
	}

	public static List<DeclaracaoAcercaProcessoJudicialVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<DeclaracaoAcercaProcessoJudicialVO> vetResultado = new ArrayList<DeclaracaoAcercaProcessoJudicialVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void persistir(List<DeclaracaoAcercaProcessoJudicialVO> declaracaoAcercaProcessoJudicialVOs, UsuarioVO usuario) throws Exception {
		if (!declaracaoAcercaProcessoJudicialVOs.isEmpty()) {
			for (DeclaracaoAcercaProcessoJudicialVO obj : declaracaoAcercaProcessoJudicialVOs) {
				if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
					alterar(obj, usuario);
				} else {
					incluir(obj, usuario);
				}
			}
		}
	}

}
