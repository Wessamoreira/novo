package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.primefaces.event.FileUploadEvent;
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

import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.UnidadeConteudoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class UnidadeConteudo extends ControleAcesso implements UnidadeConteudoInterfaceFacade {

	/**
     * 
     */
	private static final long serialVersionUID = -7889285917302944461L;
	protected static String idEntidade = "Conteudo";

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return Conteudo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Conteudo.idEntidade = idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(UnidadeConteudoVO unidadeConteudo,  DisciplinaVO disciplinaVO,  Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		
		if (unidadeConteudo.isNovoObj()) {
			incluir(unidadeConteudo, disciplinaVO,  controlarAcesso, usuario, realizandoClonagem);
		} else {
			alterar(unidadeConteudo, disciplinaVO,  controlarAcesso, usuario, realizandoClonagem);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final UnidadeConteudoVO unidadeConteudoVO,  DisciplinaVO disciplinaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {

		try {
			validarDados(unidadeConteudoVO);
			incluir(getIdEntidade(), controlarAcesso, usuario);			
			final StringBuilder sql = new StringBuilder("INSERT INTO UnidadeConteudo ");
			sql.append(" (conteudo, ordem, titulo, ");
			sql.append(" caminhoBaseBackground, nomeImagemBackground, corBackground, tamanhoImagemBackgroundConteudo , origemBackgroundConteudo, temaassunto) ");
			sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			unidadeConteudoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setInt(x++, unidadeConteudoVO.getConteudo().getCodigo());
					sqlInserir.setInt(x++, unidadeConteudoVO.getOrdem());
					sqlInserir.setString(x++, unidadeConteudoVO.getTitulo());
					sqlInserir.setString(x++, unidadeConteudoVO.getCaminhoBaseBackground());
					sqlInserir.setString(x++, unidadeConteudoVO.getNomeImagemBackground());
					sqlInserir.setString(x++, unidadeConteudoVO.getCorBackground());
					sqlInserir.setString(x++, unidadeConteudoVO.getTamanhoImagemBackgroundConteudo().name());
					sqlInserir.setString(x++, unidadeConteudoVO.getOrigemBackgroundConteudo().name());
					if(!unidadeConteudoVO.getTemaAssuntoVO().getCodigo().equals(0)) {
						sqlInserir.setInt(x++, unidadeConteudoVO.getTemaAssuntoVO().getCodigo());						
					} else {
						sqlInserir.setNull(x++, 0);						
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						unidadeConteudoVO.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			 getFacadeFactory().getConteudoUnidadePaginaFacade().incluirConteudoUnidadePagina(unidadeConteudoVO.getConteudoUnidadePaginaVOs(), unidadeConteudoVO, unidadeConteudoVO.getConteudo(), false, usuario, realizandoClonagem);
		} catch (Exception e) {
			unidadeConteudoVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final UnidadeConteudoVO unidadeConteudoVO, DisciplinaVO disciplinaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		try {
			validarDados(unidadeConteudoVO);
			alterar(getIdEntidade(), controlarAcesso, usuario);			
			final StringBuilder sql = new StringBuilder("UPDATE UnidadeConteudo set ");
			sql.append(" conteudo = ?, ordem = ?, titulo = ?, ");
			sql.append(" caminhoBaseBackground = ?, nomeImagemBackground = ?, corBackground = ?, tamanhoImagemBackgroundConteudo = ? , origemBackgroundConteudo = ?, temaassunto = ? ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(x++, unidadeConteudoVO.getConteudo().getCodigo());
					sqlAlterar.setInt(x++, unidadeConteudoVO.getOrdem());
					sqlAlterar.setString(x++, unidadeConteudoVO.getTitulo());
					sqlAlterar.setString(x++, unidadeConteudoVO.getCaminhoBaseBackground());
					sqlAlterar.setString(x++, unidadeConteudoVO.getNomeImagemBackground());
					sqlAlterar.setString(x++, unidadeConteudoVO.getCorBackground());
					sqlAlterar.setString(x++, unidadeConteudoVO.getTamanhoImagemBackgroundConteudo().name());
					sqlAlterar.setString(x++, unidadeConteudoVO.getOrigemBackgroundConteudo().name());
					if(!unidadeConteudoVO.getTemaAssuntoVO().getCodigo().equals(0)) {
						sqlAlterar.setInt(x++, unidadeConteudoVO.getTemaAssuntoVO().getCodigo());						
					} else {
						sqlAlterar.setNull(x++, 0);						
					}					
					sqlAlterar.setInt(x++, unidadeConteudoVO.getCodigo());
					return sqlAlterar;
				}
			}) <= 0) {
				incluir(unidadeConteudoVO, disciplinaVO,  controlarAcesso, usuario, realizandoClonagem);
				return;
			}			
			unidadeConteudoVO.setNovoObj(false);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarBackground(final UnidadeConteudoVO unidadeConteudoVO) throws Exception {
		try {
						
			final StringBuilder sql = new StringBuilder("UPDATE UnidadeConteudo set ");
			sql.append(" caminhoBaseBackground = ?, nomeImagemBackground = ?, corBackground = ?, tamanhoImagemBackgroundConteudo = ? , origemBackgroundConteudo = ? ");
			sql.append(" where codigo = ? ");
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());					
					sqlAlterar.setString(x++, unidadeConteudoVO.getCaminhoBaseBackground());
					sqlAlterar.setString(x++, unidadeConteudoVO.getNomeImagemBackground());
					sqlAlterar.setString(x++, unidadeConteudoVO.getCorBackground());
					sqlAlterar.setString(x++, unidadeConteudoVO.getTamanhoImagemBackgroundConteudo().name());
					sqlAlterar.setString(x++, unidadeConteudoVO.getOrigemBackgroundConteudo().name());
					sqlAlterar.setInt(x++, unidadeConteudoVO.getCodigo());
					return sqlAlterar;
				}
			}) <= 0) {
				unidadeConteudoVO.setNovoObj(true);
				return;
			}			
			unidadeConteudoVO.setNovoObj(false);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void validarDados(UnidadeConteudoVO unidadeConteudoVO) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();
		if (unidadeConteudoVO.getTitulo().trim().isEmpty()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_UnidadeConteudo_titulo"));
		}
		
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
	}

	@Override
	public List<UnidadeConteudoVO> consultarUnidadeConteudoPorConteudo(Integer conteudo, Integer temaAssunto, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT UnidadeConteudo.*, ");		
		sb.append(" ((select sum(ponto) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo) )  as ponto, ");
		sb.append(" ((select sum(tempo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo ))  as tempo, ");
		sb.append(" ((select count(codigo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo))  as paginas ");
		sb.append(" FROM UnidadeConteudo ");
		//sb.append(" left join ConteudoUnidadePagina on ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo ");
		sb.append(" where UnidadeConteudo.conteudo = ").append(conteudo);
		if(!temaAssunto.equals(0)) {
			sb.append("and UnidadeConteudo.temaassunto = ").append(temaAssunto);
		}
		//sb.append(" group by UnidadeConteudo.codigo, UnidadeConteudo.titulo, UnidadeConteudo.ordem, UnidadeConteudo.conteudo ");
		sb.append(" order by UnidadeConteudo.ordem");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, controlarAcesso, usuario);
	}

	@Override
	public UnidadeConteudoVO consultarUnidadeConteudoPorConteudoEOrdem(Integer conteudo, Integer ordem, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT UnidadeConteudo.*, ");		
		sb.append(" ((select sum(ponto) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo) )  as ponto, ");
		sb.append(" ((select sum(tempo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo ))  as tempo, ");
		sb.append(" ((select count(codigo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo))  as paginas ");
		sb.append(" FROM UnidadeConteudo ");
		//sb.append(" left join ConteudoUnidadePagina on ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo ");
		sb.append(" where UnidadeConteudo.conteudo = ").append(conteudo);
		sb.append(" and UnidadeConteudo.ordem = ").append(ordem);
		//sb.append(" group by UnidadeConteudo.codigo, UnidadeConteudo.titulo, UnidadeConteudo.ordem, UnidadeConteudo.conteudo ");
		sb.append(" order by UnidadeConteudo.ordem");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, controlarAcesso, usuario);
		}
		return new UnidadeConteudoVO();
	}

	private List<UnidadeConteudoVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		List<UnidadeConteudoVO> unidadeConteudoVOs = new ArrayList<UnidadeConteudoVO>(0);
		while (rs.next()) {
			unidadeConteudoVOs.add(montarDados(rs, nivelMontarDados, controlarAcesso, usuario));
		}
		return unidadeConteudoVOs;
	}

	private UnidadeConteudoVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		UnidadeConteudoVO unidadeConteudoVO = new UnidadeConteudoVO();
		unidadeConteudoVO.setCodigo(rs.getInt("codigo"));
		unidadeConteudoVO.setOrdem(rs.getInt("ordem"));
		unidadeConteudoVO.setPonto(rs.getDouble("ponto"));
		unidadeConteudoVO.setTempo(rs.getInt("tempo"));
		unidadeConteudoVO.setPaginas(rs.getInt("paginas"));
		unidadeConteudoVO.setTitulo(rs.getString("titulo"));
		unidadeConteudoVO.getConteudo().setCodigo(rs.getInt("conteudo"));
		unidadeConteudoVO.setCaminhoBaseBackground(rs.getString("caminhoBaseBackground"));
		unidadeConteudoVO.setNomeImagemBackground(rs.getString("nomeImagemBackground"));
		unidadeConteudoVO.setCorBackground(rs.getString("corBackground"));
		if(Uteis.isAtributoPreenchido(rs.getInt("temaassunto"))) {
			unidadeConteudoVO.getTemaAssuntoVO().setCodigo(rs.getInt("temaassunto"));			
		}
		if (rs.getString("tamanhoImagemBackgroundConteudo") != null && !rs.getString("tamanhoImagemBackgroundConteudo").isEmpty()) {
			unidadeConteudoVO.setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum.valueOf(rs.getString("tamanhoImagemBackgroundConteudo")));
		} else {
			unidadeConteudoVO.setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO);
		}
		if (rs.getString("origemBackgroundConteudo") != null && !rs.getString("origemBackgroundConteudo").isEmpty()) {
			unidadeConteudoVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.valueOf(rs.getString("origemBackgroundConteudo")));
		} else {
			unidadeConteudoVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.SEM_BACKGROUND);
		}
		unidadeConteudoVO.setNovoObj(false);
		if (nivelMontarDados.equals(NivelMontarDados.TODOS)) {
			unidadeConteudoVO.setConteudoUnidadePaginaVOs(getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudo(unidadeConteudoVO.getCodigo(), nivelMontarDados, controlarAcesso, usuario));
			unidadeConteudoVO.setTemaAssuntoVO(getFacadeFactory().getTemaAssuntoFacade().consultarPorChavePrimaria(unidadeConteudoVO.getTemaAssuntoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return unidadeConteudoVO;
	}

	@Override
	public void incluirUnidadeConteudo(ConteudoVO conteudoVO, DisciplinaVO disciplinaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		for (UnidadeConteudoVO unidadeConteudoVO : conteudoVO.getUnidadeConteudoVOs()) {
			unidadeConteudoVO.setConteudo(conteudoVO);
			incluir(unidadeConteudoVO, disciplinaVO, controlarAcesso, usuario, realizandoClonagem);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void excluirUnidadeConteudoEspecifico(UnidadeConteudoVO unidadeConteudoVO) throws Exception {
		StringBuilder sb = new StringBuilder("DELETE FROM UnidadeConteudo where codigo =  " + unidadeConteudoVO.getCodigo());
		getConexao().getJdbcTemplate().execute(sb.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void excluirUnidadeConteudo(ConteudoVO conteudoVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("DELETE FROM UnidadeConteudo where conteudo =  ").append(conteudoVO.getCodigo());
		sb.append(" and codigo not in ( 0 ");
		for (UnidadeConteudoVO unidadeConteudoVO : conteudoVO.getUnidadeConteudoVOs()) {
			if (!unidadeConteudoVO.isNovoObj()) {
				sb.append(", ").append(unidadeConteudoVO.getCodigo());
			}
		}
		sb.append(")").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().execute(sb.toString());
	}

	@Override
	public void alterarUnidadeConteudo(ConteudoVO conteudoVO, DisciplinaVO disciplinaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		excluirUnidadeConteudo(conteudoVO, controlarAcesso, usuario);
		for (UnidadeConteudoVO unidadeConteudoVO : conteudoVO.getUnidadeConteudoVOs()) {
			unidadeConteudoVO.setConteudo(conteudoVO);
			persistir(unidadeConteudoVO, disciplinaVO, controlarAcesso, usuario, realizandoClonagem);
		}
	}

	@Override
	public void inativarUnidadeConteudo(UnidadeConteudoVO unidadeConteudoVO, Boolean controlarAcesso, UsuarioVO usuario) {
		// TODO Auto-generated method stub

	}

	@Override
	public UnidadeConteudoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT UnidadeConteudo.*, ");		
		sb.append(" ((select sum(ponto) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo) )  as ponto, ");
		sb.append(" ((select sum(tempo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo ))  as tempo, ");
		sb.append(" ((select count(codigo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo))  as paginas ");
		sb.append(" FROM UnidadeConteudo ");
//		sb.append(" left join ConteudoUnidadePagina on ConteudoUnidadePagina.unidadeConteudo = UnidadeConteudo.codigo ");
		sb.append(" where UnidadeConteudo.codigo = ").append(codigo);
//		sb.append(" group by UnidadeConteudo.codigo, UnidadeConteudo.titulo, UnidadeConteudo.ordem, UnidadeConteudo.conteudo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, controlarAcesso, usuario);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarOrdemConteudoUnidadePagina(UnidadeConteudoVO unidadeConteudo1, UnidadeConteudoVO unidadeConteudo2, ConteudoUnidadePaginaVO conteudoUnidadePagina1, ConteudoUnidadePaginaVO conteudoUnidadePagina2, UsuarioVO usuario) throws Exception {
		int ordem1 = conteudoUnidadePagina1.getPagina();
		int ordem2 = conteudoUnidadePagina2 == null ? 0 : conteudoUnidadePagina2.getPagina();
		boolean removeu = false;
		boolean adicionou = false;
		try {
			if (unidadeConteudo1.getCodigo().intValue() != unidadeConteudo2.getCodigo().intValue()) {
				unidadeConteudo1.getConteudoUnidadePaginaVOs().remove(ordem1-1);
				removeu = true;
				int index = 1;
				for (ConteudoUnidadePaginaVO obj : unidadeConteudo1.getConteudoUnidadePaginaVOs()) {
					obj.setPagina(index++);
					getFacadeFactory().getConteudoUnidadePaginaFacade().alterarNumeroPagina(obj, usuario);
				}
				conteudoUnidadePagina1.setUnidadeConteudo(unidadeConteudo2);
				conteudoUnidadePagina1.setPagina(ordem2+1);
				getFacadeFactory().getConteudoUnidadePaginaFacade().alterarNumeroPagina(conteudoUnidadePagina1, usuario);
				unidadeConteudo2.getConteudoUnidadePaginaVOs().add(ordem2, conteudoUnidadePagina1);
				
				adicionou = true;
				index = 1;
				for (ConteudoUnidadePaginaVO obj : unidadeConteudo2.getConteudoUnidadePaginaVOs()) {
					obj.setPagina(index++);
					getFacadeFactory().getConteudoUnidadePaginaFacade().alterarNumeroPagina(obj, usuario);									
				}
				unidadeConteudo2.setPaginas(unidadeConteudo2.getConteudoUnidadePaginaVOs().size());
				Ordenacao.ordenarLista(unidadeConteudo2.getConteudoUnidadePaginaVOs(), "pagina");
			}else{
				if(conteudoUnidadePagina2 != null) {
					conteudoUnidadePagina2.setPagina(ordem1);
					getFacadeFactory().getConteudoUnidadePaginaFacade().alterarNumeroPagina(conteudoUnidadePagina2, usuario);
				}
				conteudoUnidadePagina1.setPagina(ordem2);
				getFacadeFactory().getConteudoUnidadePaginaFacade().alterarNumeroPagina(conteudoUnidadePagina1, usuario);
			}
			unidadeConteudo1.setPaginas(unidadeConteudo1.getConteudoUnidadePaginaVOs().size());
			Ordenacao.ordenarLista(unidadeConteudo1.getConteudoUnidadePaginaVOs(), "pagina");
		} catch (Exception e) {
			if(conteudoUnidadePagina2 != null) {
				conteudoUnidadePagina2.setPagina(ordem2);
				conteudoUnidadePagina2.setUnidadeConteudo(unidadeConteudo2);
			}
			conteudoUnidadePagina1.setPagina(ordem1);
			conteudoUnidadePagina1.setUnidadeConteudo(unidadeConteudo1);
			if(removeu){
				unidadeConteudo1.getConteudoUnidadePaginaVOs().add(ordem1-1, conteudoUnidadePagina1);
				int index = 1;
				for (ConteudoUnidadePaginaVO obj : unidadeConteudo1.getConteudoUnidadePaginaVOs()) {
					obj.setPagina(index++);					
				}				
			}			
			if(adicionou){
				unidadeConteudo1.getConteudoUnidadePaginaVOs().remove(ordem2);
				int index = 1;
				for (ConteudoUnidadePaginaVO obj : unidadeConteudo2.getConteudoUnidadePaginaVOs()) {
					obj.setPagina(index++);													
				}
			}
			throw e;
		}
		
		

	}
	
	@Override
	public void adicionarPagina(UnidadeConteudoVO unidadeConteudoVO, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO){
		int x = 1;
		for(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO2: unidadeConteudoVO.getConteudoUnidadePaginaVOs()) {
			conteudoUnidadePaginaVO2.setPagina(x);
			x++;
		}
		if(unidadeConteudoVO.getConteudoUnidadePaginaVOs().size() < conteudoUnidadePaginaVO.getPagina()){	
			conteudoUnidadePaginaVO.setPagina(x);
			unidadeConteudoVO.getConteudoUnidadePaginaVOs().add(conteudoUnidadePaginaVO.getPagina() - 1, conteudoUnidadePaginaVO);
		}else{
			unidadeConteudoVO.getConteudoUnidadePaginaVOs().set(conteudoUnidadePaginaVO.getPagina() - 1, conteudoUnidadePaginaVO);
		}
		unidadeConteudoVO.setPaginas(unidadeConteudoVO.getConteudoUnidadePaginaVOs().size());
		realizarCalculoTempoEPonto(unidadeConteudoVO);
	}
	
	public void removerPagina(UnidadeConteudoVO unidadeConteudoVO, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO){
		unidadeConteudoVO.getConteudoUnidadePaginaVOs().add(conteudoUnidadePaginaVO.getPagina() - 1, conteudoUnidadePaginaVO);
		unidadeConteudoVO.setPaginas(unidadeConteudoVO.getConteudoUnidadePaginaVOs().size());
		realizarCalculoTempoEPonto(unidadeConteudoVO);
	}
	
	@Override
	public void realizarCalculoTempoEPonto(UnidadeConteudoVO unidadeConteudo){
		Integer tempo = 0;
		Double ponto = 0.0;
		for (ConteudoUnidadePaginaVO conteudoUnidadePaginaVO : unidadeConteudo.getConteudoUnidadePaginaVOs()) {
            tempo += conteudoUnidadePaginaVO.getTempo();
            ponto += conteudoUnidadePaginaVO.getPonto();
		}
		unidadeConteudo.setTempo(tempo);
		unidadeConteudo.setPonto(ponto);		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void uploadImagemBackgroundUnidadeConteudo(UnidadeConteudoVO unidadeConteudoVO, DisciplinaVO disciplina, FileUploadEvent uploadEvent, Boolean aplicarBackRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Boolean realizandoClonagem) throws Exception {

		String arquivo = "";
		if (unidadeConteudoVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.UNIDADE) && !unidadeConteudoVO.getNomeImagemBackground().trim().isEmpty()) {			
			arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + unidadeConteudoVO.getCaminhoBaseBackground() + File.separator + unidadeConteudoVO.getNomeImagemBackground();
			ArquivoHelper.delete(new File(arquivo));			
		}
		String extensao = uploadEvent.getFile().getFileName().substring(uploadEvent.getFile().getFileName().lastIndexOf("."), uploadEvent.getFile().getFileName().length());
		unidadeConteudoVO.setNomeImagemBackground(usuarioVO.getCodigo() + "_" + (new Date().getTime()) + extensao);
		unidadeConteudoVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.UNIDADE);
		unidadeConteudoVO.setCaminhoBaseBackground(PastaBaseArquivoEnum.EAD.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO.getValue() + File.separator + disciplina.getCodigo() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_BACKGROUND.getValue());
		arquivo = unidadeConteudoVO.getCaminhoBaseBackground() + File.separator + unidadeConteudoVO.getNomeImagemBackground();
		ArquivoHelper.salvarArquivoNaPastaTemp(uploadEvent, unidadeConteudoVO.getNomeImagemBackground(), unidadeConteudoVO.getCaminhoBaseBackground(), configuracaoGeralSistemaVO, usuarioVO);
		alterarBackground(unidadeConteudoVO, disciplina, aplicarBackRecursoEducacional, usuarioVO, realizandoClonagem);
	
		


	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarBackground(UnidadeConteudoVO unidadeConteudoVO, DisciplinaVO disciplina, Boolean aplicarBackRecursoEducacional, UsuarioVO usuarioVO, Boolean realizandoClonagem) throws Exception{
		alterar(unidadeConteudoVO, disciplina, false, usuarioVO, realizandoClonagem);
		getFacadeFactory().getConteudoUnidadePaginaFacade().realizarReplicacaoBackgroundParaPagina(unidadeConteudoVO.getConteudoUnidadePaginaVOs(), 
				OrigemBackgroundConteudoEnum.CONTEUDO, unidadeConteudoVO.getCaminhoBaseBackground(), unidadeConteudoVO.getNomeImagemBackground(), 
				unidadeConteudoVO.getCorBackground(), aplicarBackRecursoEducacional, 
				unidadeConteudoVO.getOrigemBackgroundConteudo(), unidadeConteudoVO.getTamanhoImagemBackgroundConteudo(), true);
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerImagemBackgroundUnidadeConteudo(UnidadeConteudoVO unidadeConteudoVO, DisciplinaVO disciplinaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Boolean realizandoClonagem)  throws Exception{
		if (unidadeConteudoVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.PAGINA) && !getFacadeFactory().getConteudoFacade().validarUsoDaImagemBackgroundNoConteudo(unidadeConteudoVO.getNomeImagemBackground(), usuarioVO)) {
				String arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + unidadeConteudoVO.getCaminhoBaseBackground() + File.separator + unidadeConteudoVO.getNomeImagemBackground();
				ArquivoHelper.delete(new File(arquivo));			
		} 
		unidadeConteudoVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.SEM_BACKGROUND);
		unidadeConteudoVO.setCaminhoBaseBackground("");
		unidadeConteudoVO.setNomeImagemBackground("");		
		alterarBackground(unidadeConteudoVO, disciplinaVO, false, usuarioVO, realizandoClonagem);	
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarReplicacaoBackgroundParaUnidade(List<UnidadeConteudoVO> unidadeConteudoVOs, 
			OrigemBackgroundConteudoEnum origemBase, String caminhoBase, String nomeArquivo, String cor, Boolean aplicarBackRecursoEducacional, 
			OrigemBackgroundConteudoEnum origemUtilizar, TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudoEnum,  Boolean gravarAlteracao) throws Exception {
		for (UnidadeConteudoVO unidadeConteudoVO : unidadeConteudoVOs) {
			if ((!unidadeConteudoVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.UNIDADE) && aplicarBackRecursoEducacional)
					|| (unidadeConteudoVO.getOrigemBackgroundConteudo().equals(origemBase))) {
				
				unidadeConteudoVO.setCaminhoBaseBackground(caminhoBase);
				unidadeConteudoVO.setNomeImagemBackground(nomeArquivo);
				unidadeConteudoVO.setOrigemBackgroundConteudo(origemUtilizar);
				unidadeConteudoVO.setTamanhoImagemBackgroundConteudo(tamanhoImagemBackgroundConteudoEnum);
				unidadeConteudoVO.setCorBackground(cor);			
				if(origemBase.equals(OrigemBackgroundConteudoEnum.UNIDADE) || origemBase.equals(OrigemBackgroundConteudoEnum.CONTEUDO)){										
					getFacadeFactory().getConteudoUnidadePaginaFacade().realizarReplicacaoBackgroundParaPagina(unidadeConteudoVO.getConteudoUnidadePaginaVOs(), origemBase, 
							caminhoBase, nomeArquivo, cor, aplicarBackRecursoEducacional, origemUtilizar, tamanhoImagemBackgroundConteudoEnum, gravarAlteracao);
				}
					
				if(gravarAlteracao && unidadeConteudoVO.getCodigo() > 0){
					alterarBackground(unidadeConteudoVO);
				}
			} 
		}
		
	}

}
