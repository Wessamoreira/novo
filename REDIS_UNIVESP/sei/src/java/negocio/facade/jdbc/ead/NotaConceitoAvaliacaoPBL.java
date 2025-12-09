package negocio.facade.jdbc.ead;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

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

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.NotaConceitoAvaliacaoPBLVO;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.NotaConceitoAvaliacaoPBLInterfaceFacade;

/**
 * @author VictorHugo - 5 de jul de 2016
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class NotaConceitoAvaliacaoPBL extends ControleAcesso implements NotaConceitoAvaliacaoPBLInterfaceFacade  {

	private static final long serialVersionUID = 1L;
	/**
	 * @author VictorHugo - 5 de jul de 2016
	 */
	
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		NotaConceitoAvaliacaoPBL.idEntidade = idEntidade;
	}

	public NotaConceitoAvaliacaoPBL() throws Exception {
		super();
		setIdEntidade("NotaConceitoAvaliacaoPBL");
	}
	
	@Override
	public void validarDados(NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();
		if(notaConceitoAvaliacaoPBLVO.getConceito().isEmpty()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_conceitoVazio"));
		}
		if(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente() < 0.0) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_notaConsiderarMenorQueZero"));
		}
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(notaConceitoAvaliacaoPBLVO);
			NotaConceitoAvaliacaoPBL.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO notaconceitoavaliacaopbl");
			sql.append(" (conteudounidadepaginarecursoeducacional, conceito, notacorrespondente, tipoavaliacao) ");
			sql.append(" VALUES (?, ?, ?, ?) ");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			notaConceitoAvaliacaoPBLVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					sqlInserir.setInt(1, notaConceitoAvaliacaoPBLVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo());
					sqlInserir.setString(2, notaConceitoAvaliacaoPBLVO.getConceito());
					sqlInserir.setDouble(3, notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
					sqlInserir.setString(4, notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().getName());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						notaConceitoAvaliacaoPBLVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			notaConceitoAvaliacaoPBLVO.setNovoObj(Boolean.TRUE);
			notaConceitoAvaliacaoPBLVO.setCodigo(0);
			throw e;
		}
	}
	
	public NotaConceitoAvaliacaoPBLVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) {
		NotaConceitoAvaliacaoPBLVO obj = new NotaConceitoAvaliacaoPBLVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setCodigo(tabelaResultado.getInt("conteudounidadepaginarecursoeducacional"));
		obj.setConceito(tabelaResultado.getString("conceito"));
		obj.setNotaCorrespondente(tabelaResultado.getDouble("notacorrespondente"));
		obj.setTipoAvaliacao(TipoAvaliacaoPBLEnum.valueOf(tabelaResultado.getString("tipoavaliacao")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			return obj;
		}
		return obj;
	}
	
	public List<NotaConceitoAvaliacaoPBLVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<NotaConceitoAvaliacaoPBLVO> vetResultado = new ArrayList<NotaConceitoAvaliacaoPBLVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaConceitoAvaliacaoPBLVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM notaconceitoavaliacaopbl WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if(rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new NotaConceitoAvaliacaoPBLVO();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (notaConceitoAvaliacaoPBLVO.getCodigo() == 0) {
			incluir(notaConceitoAvaliacaoPBLVO, verificarAcesso, usuarioVO);
		} else {
			alterar(notaConceitoAvaliacaoPBLVO, verificarAcesso, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(notaConceitoAvaliacaoPBLVO);
			NotaConceitoAvaliacaoPBL.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE notaconceitoavaliacaopbl ");
			sql.append(" SET conteudounidadepaginarecursoeducacional=?, conceito=?, notacorrespondente=?, tipoavaliacao=?");
			sql.append(" WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, notaConceitoAvaliacaoPBLVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo());
					sqlAlterar.setString(2, notaConceitoAvaliacaoPBLVO.getConceito());
					sqlAlterar.setDouble(3, notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
					sqlAlterar.setString(4, notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().getName());
					sqlAlterar.setInt(5, notaConceitoAvaliacaoPBLVO.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(notaConceitoAvaliacaoPBLVO, false, usuarioVO);
			};
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirNotaConceitoAvaliacaoPBLVOS(List<NotaConceitoAvaliacaoPBLVO> notaConceitoAvaliacaoPBLVOs, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (Iterator<NotaConceitoAvaliacaoPBLVO> iterator = notaConceitoAvaliacaoPBLVOs.iterator(); iterator.hasNext();) {
			NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO = (NotaConceitoAvaliacaoPBLVO) iterator.next();
			persistir(notaConceitoAvaliacaoPBLVO, verificarAcesso, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			NotaConceitoAvaliacaoPBL.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM NotaConceitoAvaliacaoPBL WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, notaConceitoAvaliacaoPBLVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaConceitoAvaliacaoPBLVO> consultarPorCodigoConteudoUnidadePaginaRecursoEducacional(Integer codigoConteudoUnidadePaginaRecursoEducacional, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM notaconceitoavaliacaopbl WHERE conteudoUnidadePaginaRecursoEducacional = ?";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql, codigoConteudoUnidadePaginaRecursoEducacional), nivelMontarDados, usuarioLogado));
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaConceitoAvaliacaoPBLVO> consultarPorCodigoConteudoUnidadePaginaRecursoEducacionalPorTipoAvaliacao(Integer codigoConteudoUnidadePaginaRecursoEducacional, TipoAvaliacaoPBLEnum tipoAvaliacao, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM notaconceitoavaliacaopbl WHERE conteudoUnidadePaginaRecursoEducacional = ? and tipoavaliacao=? ";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql, codigoConteudoUnidadePaginaRecursoEducacional, tipoAvaliacao.name()), nivelMontarDados, usuarioLogado));
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void validarNotaMinimaMaximaConceitoAlunoAvaliaAluno(GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao, ConteudoUnidadePaginaRecursoEducacionalVO obj) throws Exception {		
		if (!obj.getUtilizarNotaConceito() && (avaliacao.getNota() < obj.getFaixaMinimaNotaAlunoAvaliaAluno() 
				|| avaliacao.getNota() > obj.getFaixaMaximaNotaAlunoAvaliaAluno())) {
			throw new Exception(UteisJSF.internacionalizar("msg_FaixaNotaConceitoPBL_notaEntreFaixaNota")
					.replace("{0}", avaliacao.getAvaliado().getNome().toString())
					.replace("{1}", obj.getFaixaMinimaNotaAlunoAvaliaAluno().toString())
					.replace("{2}", obj.getFaixaMaximaNotaAlunoAvaliaAluno().toString()));
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void validarNotaMinimaMaximaConceitoAutoAvaliacao(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws Exception {
		/*if(!conteudoUnidadePaginaRecursoEducacionalVO.getPermiteAlunoAvancarConteudoSemLancarNota() && gestaoEventoConteudoTurmaAvaliacaoPBLVO.getNota() == null ){
			throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_existeNotasASeremLancadasNaoPermiteAvancarConteudoAvancoProximaPaginaAutoAvaliacao").replace("{0}", conteudoUnidadePaginaRecursoEducacionalVO.getDescricao()));
		}*/
		if (!conteudoUnidadePaginaRecursoEducacionalVO.getUtilizarNotaConceito() && gestaoEventoConteudoTurmaAvaliacaoPBLVO.getNota() != null && (gestaoEventoConteudoTurmaAvaliacaoPBLVO.getNota() < conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaAutoAvaliacao() 
				|| gestaoEventoConteudoTurmaAvaliacaoPBLVO.getNota() > conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaAutoAvaliacao())) {
			throw new Exception(UteisJSF.internacionalizar("msg_FaixaNotaConceitoPBL_notaEntreFaixaNota")
					.replace("{0}", gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliado().getNome().toString())
					.replace("{1}", conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaAutoAvaliacao().toString())
					.replace("{2}", conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaAutoAvaliacao().toString()));
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void validarNotaMinimaMaximaConceitoProfAvaliaAluno(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws Exception {
		if (!conteudoUnidadePaginaRecursoEducacionalVO.getUtilizarNotaConceito() && (gestaoEventoConteudoTurmaAvaliacaoPBLVO.getNota() < conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaProfessorAvaliaAluno() 
				|| gestaoEventoConteudoTurmaAvaliacaoPBLVO.getNota() > conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaProfessorAvaliaAluno())) {
			throw new Exception(UteisJSF.internacionalizar("msg_FaixaNotaConceitoPBL_notaEntreFaixaNota")
					.replace("{0}", gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliado().getNome().toString())
					.replace("{1}", conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaProfessorAvaliaAluno().toString())
					.replace("{2}", conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaProfessorAvaliaAluno().toString()));
		}
	}
	
	@Override
	public List<SelectItem> montarComboboxNotaConceito(List<NotaConceitoAvaliacaoPBLVO> notaConceitoAvaliacaoPBLVOs, TipoAvaliacaoPBLEnum tipoAvaliacaoPBL, UsuarioVO usuarioVO) throws Exception {
		List<SelectItem> listaComboBoxNotaConceito = new ArrayList<SelectItem>();
		listaComboBoxNotaConceito.add(new SelectItem(0, ""));
		for (NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO : notaConceitoAvaliacaoPBLVOs) {
			if (notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().equals(tipoAvaliacaoPBL)) {
				listaComboBoxNotaConceito.add(new SelectItem(notaConceitoAvaliacaoPBLVO.getCodigo(), notaConceitoAvaliacaoPBLVO.getConceito()));
			}
		}
		return listaComboBoxNotaConceito;
	}
}
