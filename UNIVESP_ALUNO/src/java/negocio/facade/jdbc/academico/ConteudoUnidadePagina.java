package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConteudoUnidadePaginaGraficoCategoriaVO;
import negocio.comuns.academico.ConteudoUnidadePaginaGraficoPizzaVO;
import negocio.comuns.academico.ConteudoUnidadePaginaGraficoSerieVO;
import negocio.comuns.academico.ConteudoUnidadePaginaGraficoSerieValorVO;
import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.RecursoEducacionalVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.academico.enumeradores.MomentoApresentacaoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.PublicoAlvoForumEnum;
import negocio.comuns.academico.enumeradores.TipoGraficoEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisHTML;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ConteudoUnidadePaginaInterfaceFacade;


@Repository
@Lazy
public class ConteudoUnidadePagina extends ControleAcesso implements ConteudoUnidadePaginaInterfaceFacade {

	/**
     * 
     */
	private static final long serialVersionUID = 4736896008123457853L;
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
	public void persistir(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		validarDados(conteudoUnidadePaginaVO);
		if (conteudoUnidadePaginaVO.isNovoObj()) {
			incluir(conteudoUnidadePaginaVO, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
		} else {
			alterar(conteudoUnidadePaginaVO, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		conteudoUnidadePaginaVO.setDataCadastro(new Date());
		conteudoUnidadePaginaVO.getUsuarioCadastro().setCodigo(usuario.getCodigo());
		conteudoUnidadePaginaVO.getUsuarioCadastro().setNome(usuario.getNome());

		try {
			if (conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
				realizarGeracaoGrafico(conteudoUnidadePaginaVO);
			}
			realizarPublicacaoForum(conteudoUnidadePaginaVO, conteudo, usuario);
			copiarIconeConteudoUnidadePaginaDaPastaTemp(conteudoUnidadePaginaVO, configuracaoGeralSistemaVO);
			realizarPublicacaoIcone(conteudoUnidadePaginaVO, configuracaoGeralSistemaVO, usuario);
			copiarArquivoConteudoUnidadePagina(conteudoUnidadePaginaVO, configuracaoGeralSistemaVO);
			copiarArquivoBackgroundConteudoUnidadePagina(conteudoUnidadePaginaVO, configuracaoGeralSistemaVO);
			incluir(getIdEntidade(), controlarAcesso, usuario);
			final StringBuilder sql = new StringBuilder("INSERT INTO ConteudoUnidadePagina ");

			sql.append(" (tipoRecursoEducacional, titulo, texto, altura, largura, ");
			sql.append(" caminhoBaseRepositorio, nomeRealArquivo, nomeFisicoArquivo, pagina, tempo,");
			sql.append(" ponto, unidadeConteudo, caminhoIconeVoltar, caminhoIconeAvancar, labelIconeAvancar,");
			sql.append(" labelIconeVoltar, recursoEducacional, dataCadastro, usuarioCadastro, ");
			sql.append(" tituloGrafico, tituloEixoX, tituloEixoY, apresentarLegenda, valorGrafico, tipoGrafico, categoriaGrafico, ");
			sql.append(" nomeIconeVoltar, nomeIconeAvancar, iconeVoltar, IconeAvancar, forum, ");
			sql.append(" caminhoBaseBackground, nomeImagemBackground, corBackground, tamanhoImagemBackgroundConteudo , origemBackgroundConteudo ) ");
			sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			conteudoUnidadePaginaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getTipoRecursoEducacional().toString());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getTitulo());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getTexto());
					sqlInserir.setInt(x++, conteudoUnidadePaginaVO.getAltura());
					sqlInserir.setInt(x++, conteudoUnidadePaginaVO.getLargura());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getCaminhoBaseRepositorio());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getNomeRealArquivo());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getNomeFisicoArquivo());
					sqlInserir.setInt(x++, conteudoUnidadePaginaVO.getPagina());
					sqlInserir.setInt(x++, conteudoUnidadePaginaVO.getTempo());
					sqlInserir.setDouble(x++, conteudoUnidadePaginaVO.getPonto());
					sqlInserir.setInt(x++, conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getCaminhoIconeVoltar());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getCaminhoIconeAvancar());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getLabelIconeAvancar());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getLabelIconeVoltar());
					if (conteudoUnidadePaginaVO.getRecursoEducacional().getCodigo() > 0) {
						sqlInserir.setInt(x++, conteudoUnidadePaginaVO.getRecursoEducacional().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setDate(x++, Uteis.getDataJDBC(conteudoUnidadePaginaVO.getDataCadastro()));
					sqlInserir.setInt(x++, conteudoUnidadePaginaVO.getUsuarioCadastro().getCodigo());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getTituloGrafico());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getTituloEixoX());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getTituloEixoY());
					sqlInserir.setBoolean(x++, conteudoUnidadePaginaVO.getApresentarLegenda());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getValorGrafico());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getTipoGrafico().name());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getCategoriaGrafico());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getNomeIconeVoltar());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getNomeIconeAvancar());
					if (conteudoUnidadePaginaVO.getIconeVoltar().getCodigo() > 0) {
						sqlInserir.setInt(x++, conteudoUnidadePaginaVO.getIconeVoltar().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (conteudoUnidadePaginaVO.getIconeAvancar().getCodigo() > 0) {
						sqlInserir.setInt(x++, conteudoUnidadePaginaVO.getIconeAvancar().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && conteudoUnidadePaginaVO.getForum().getCodigo() > 0) {
						sqlInserir.setInt(x++, conteudoUnidadePaginaVO.getForum().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getCaminhoBaseBackground());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getNomeImagemBackground());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getCorBackground());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getTamanhoImagemBackgroundConteudo().name());
					sqlInserir.setString(x++, conteudoUnidadePaginaVO.getOrigemBackgroundConteudo().name());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						conteudoUnidadePaginaVO.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().incluirConteudoUnidadePaginaRecursoEducacional(conteudoUnidadePaginaVO, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
			incluirRecursoEducacional(conteudoUnidadePaginaVO, conteudo.getDisciplina(), controlarAcesso, usuario);
			for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
				if (obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
					obj.getListaExercicio().setConteudoAlterado(false);
				}
			}
			for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
				if (obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
					obj.getListaExercicio().setConteudoAlterado(false);
				}
			}
		} catch (Exception e) {
			conteudoUnidadePaginaVO.setNovoObj(true);
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void realizarPublicacaoForum(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoVO conteudo, UsuarioVO usuario) throws Exception {
		if (conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM)) {
			if (conteudoUnidadePaginaVO.getForum().isNovoObj()) {
				conteudoUnidadePaginaVO.getForum().getDisciplina().setCodigo(conteudo.getDisciplina().getCodigo());
				conteudoUnidadePaginaVO.getForum().getConteudo().setCodigo(conteudo.getCodigo());
				conteudoUnidadePaginaVO.getForum().setPublicoAlvoForumEnum(PublicoAlvoForumEnum.ALUNO);
			}
			getFacadeFactory().getForumFacade().persistir(conteudoUnidadePaginaVO.getForum(), "Forum", false, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void realizarPublicacaoIcone(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		if (conteudoUnidadePaginaVO.getPublicarIconeVoltar() && conteudoUnidadePaginaVO.getIconeVoltar().getCodigo().intValue() == 0) {
			conteudoUnidadePaginaVO.getIconeVoltar().setCaminhoBase(conteudoUnidadePaginaVO.getCaminhoIconeVoltar());
			conteudoUnidadePaginaVO.getIconeVoltar().setNomeReal(conteudoUnidadePaginaVO.getNomeIconeVoltar());
			conteudoUnidadePaginaVO.getIconeVoltar().setCaminhoWebRepositorio(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo());
			getFacadeFactory().getIconeFacade().perisitir(conteudoUnidadePaginaVO.getIconeVoltar(), false, usuarioVO);
		}
		if (conteudoUnidadePaginaVO.getPublicarIconeAvancar() && conteudoUnidadePaginaVO.getIconeAvancar().getCodigo().intValue() == 0) {
			conteudoUnidadePaginaVO.getIconeAvancar().setCaminhoBase(conteudoUnidadePaginaVO.getCaminhoIconeAvancar());
			conteudoUnidadePaginaVO.getIconeAvancar().setNomeReal(conteudoUnidadePaginaVO.getNomeIconeAvancar());
			conteudoUnidadePaginaVO.getIconeAvancar().setCaminhoWebRepositorio(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo());
			getFacadeFactory().getIconeFacade().perisitir(conteudoUnidadePaginaVO.getIconeAvancar(), false, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluirRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, DisciplinaVO disciplina, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		if (conteudoUnidadePaginaVO.getPublicarBibliotecaRecursoEducacional()) {

			conteudoUnidadePaginaVO.getRecursoEducacional().setTexto(conteudoUnidadePaginaVO.getTexto());
			conteudoUnidadePaginaVO.getRecursoEducacional().setAltura(conteudoUnidadePaginaVO.getAltura());
			conteudoUnidadePaginaVO.getRecursoEducacional().setLargura(conteudoUnidadePaginaVO.getLargura());
			conteudoUnidadePaginaVO.getRecursoEducacional().getConteudoUnidadePagina().setCodigo(conteudoUnidadePaginaVO.getCodigo());
			conteudoUnidadePaginaVO.getRecursoEducacional().setCaminhoBaseRepositorio(conteudoUnidadePaginaVO.getCaminhoBaseRepositorio());
			conteudoUnidadePaginaVO.getRecursoEducacional().setNomeFisicoArquivo(conteudoUnidadePaginaVO.getNomeFisicoArquivo());
			conteudoUnidadePaginaVO.getRecursoEducacional().setNomeRealArquivo(conteudoUnidadePaginaVO.getNomeRealArquivo());
			conteudoUnidadePaginaVO.getRecursoEducacional().setTitulo(conteudoUnidadePaginaVO.getTitulo());
			conteudoUnidadePaginaVO.getRecursoEducacional().setTipoRecursoEducacional(conteudoUnidadePaginaVO.getTipoRecursoEducacional());
			conteudoUnidadePaginaVO.getRecursoEducacional().getDisciplina().setCodigo(disciplina.getCodigo());
			conteudoUnidadePaginaVO.getRecursoEducacional().getDisciplina().setNome(disciplina.getNome());
			conteudoUnidadePaginaVO.getRecursoEducacional().setTituloGrafico(conteudoUnidadePaginaVO.getTituloGrafico());
			conteudoUnidadePaginaVO.getRecursoEducacional().setTituloEixoX(conteudoUnidadePaginaVO.getTituloEixoX());
			conteudoUnidadePaginaVO.getRecursoEducacional().setTituloEixoY(conteudoUnidadePaginaVO.getTituloEixoY());
			conteudoUnidadePaginaVO.getRecursoEducacional().setValorGrafico(conteudoUnidadePaginaVO.getValorGrafico());
			conteudoUnidadePaginaVO.getRecursoEducacional().setTipoGrafico(conteudoUnidadePaginaVO.getTipoGrafico());
			conteudoUnidadePaginaVO.getRecursoEducacional().setCategoriaGrafico(conteudoUnidadePaginaVO.getCategoriaGrafico());
			conteudoUnidadePaginaVO.getRecursoEducacional().setApresentarLegenda(conteudoUnidadePaginaVO.getApresentarLegenda());
			getFacadeFactory().getRecursoEducacionalFacade().persistir(conteudoUnidadePaginaVO.getRecursoEducacional(), controlarAcesso, usuario);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConteudoVO conteudoVO, final ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, Boolean alterarPagina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			
			excluir(getIdEntidade(), controlarAcesso, usuario);
			List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalVOs = getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(conteudoUnidadePaginaVO.getCodigo(), null, NivelMontarDados.BASICO, false, usuario);
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().excluirArquivoConteudoRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVOs, configuracaoGeralSistemaVO);
			
			StringBuilder sql = new StringBuilder("DELETE FROM ConteudoUnidadePagina");
			sql.append(" WHERE codigo = ").append(conteudoUnidadePaginaVO.getCodigo()).append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(sql.toString());
			
			for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO : conteudoUnidadePaginaRecursoEducacionalVOs) {
				if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoForum()) {
					getFacadeFactory().getForumFacade().excluir(conteudoUnidadePaginaRecursoEducacionalVO.getForum(), usuario);
				} else if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoAvaliacaoOnline()) {
					getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().excluir(conteudoUnidadePaginaRecursoEducacionalVO.getAvaliacaoOnlineVO(), false, usuario);
				} else if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoExercicio()) {
					getFacadeFactory().getListaExercicioFacade().excluir(conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio(), false, "ListaExercicio", usuario);
				}
			}

			if (conteudoUnidadePaginaVO.getTipoRecursoEducacional().getNecessitaUploadArquivo() && !conteudoUnidadePaginaVO.getCaminhoBaseRepositorio().trim().isEmpty() && !conteudoUnidadePaginaVO.getPublicarBibliotecaRecursoEducacional() && conteudoUnidadePaginaVO.getRecursoEducacional().getCodigo() == 0) {
				String arquivo = "";
				if (conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM)) {
					String[] nomeArquivos = conteudoUnidadePaginaVO.getNomeFisicoArquivo().split(",");
					String[] caminhosArquivos = conteudoUnidadePaginaVO.getCaminhoBaseRepositorio().split(",");
					int x = 0;
					for (String nomeArquivo : nomeArquivos) {
						if(!nomeArquivo.trim().isEmpty() && caminhosArquivos.length > x) {
						if (caminhosArquivos[x].contains("_TMP")) {
							arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp();
						} else {
							arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
						}
						arquivo += (File.separator + caminhosArquivos[x].trim() + File.separator + nomeArquivo.trim());
						ArquivoHelper.delete(new File(arquivo));
						}
						x++;
					}
				} else {
					if (conteudoUnidadePaginaVO.getCaminhoBaseRepositorio().contains("_TMP")) {
						arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp();
					} else {
						arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
					}
					if (!getFacadeFactory().getConteudoFacade().validarUsoArquivoConteudo(conteudoUnidadePaginaVO.getCaminhoBaseRepositorio(), conteudoUnidadePaginaVO.getNomeFisicoArquivo())) {
						arquivo += (File.separator + conteudoUnidadePaginaVO.getCaminhoBaseRepositorio() + File.separator + conteudoUnidadePaginaVO.getNomeFisicoArquivo());
						ArquivoHelper.delete(new File(arquivo));
					}
				}
			}

			if (conteudoUnidadePaginaVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.PAGINA) && !conteudoUnidadePaginaVO.getCaminhoBaseBackground().trim().isEmpty() && !conteudoUnidadePaginaVO.getNomeImagemBackground().trim().isEmpty()) {
				String arquivo = "";
				if (conteudoUnidadePaginaVO.getCaminhoBaseRepositorio().contains("_TMP")) {
					arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp();
				} else {
					arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
				}
				if (!getFacadeFactory().getConteudoFacade().validarUsoDaImagemBackgroundNoConteudo(conteudoUnidadePaginaVO.getNomeImagemBackground(), usuario)) {
					arquivo += (File.separator + conteudoUnidadePaginaVO.getCaminhoBaseBackground() + File.separator + conteudoUnidadePaginaVO.getNomeImagemBackground());
					ArquivoHelper.delete(new File(arquivo));
				}
			}

			

			if (alterarPagina) {
				sql = new StringBuilder("UPDATE ConteudoUnidadePagina set pagina = pagina - 1 ");
				sql.append(" WHERE pagina > ").append(conteudoUnidadePaginaVO.getPagina());
				sql.append(" and  unidadeConteudo = ").append(conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo()).append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
				getConexao().getJdbcTemplate().update(sql.toString());

				for (UnidadeConteudoVO unidadeConteudoVO : conteudoVO.getUnidadeConteudoVOs()) {
					if (unidadeConteudoVO.getCodigo().intValue() == conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo().intValue()) {
						int index = 1;
						int indexRemover = 0;
						for (ConteudoUnidadePaginaVO obj : unidadeConteudoVO.getConteudoUnidadePaginaVOs()) {
							if (obj.getCodigo().intValue() == conteudoUnidadePaginaVO.getCodigo().intValue()) {
								indexRemover = index - 1;
							} else {
								obj.setPagina(index);
								index++;
							}
						}
						unidadeConteudoVO.setPaginas(unidadeConteudoVO.getConteudoUnidadePaginaVOs().size());
						unidadeConteudoVO.getConteudoUnidadePaginaVOs().remove(indexRemover);
						return;
					}
				}
			}

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarNumeroPagina(final ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, UsuarioVO usuario) throws Exception {
		try {
			conteudoUnidadePaginaVO.setDataAlteracao(new Date());
			conteudoUnidadePaginaVO.getUsuarioAlteracao().setCodigo(usuario.getCodigo());
			conteudoUnidadePaginaVO.getUsuarioAlteracao().setNome(usuario.getNome());
			final StringBuilder sql = new StringBuilder("UPDATE ConteudoUnidadePagina SET ");
			sql.append(" unidadeConteudo = ?, pagina = ?, dataAlteracao = ? , usuarioAlteracao = ? ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getPagina());
					sqlAlterar.setDate(x++, Uteis.getDataJDBC(conteudoUnidadePaginaVO.getDataAlteracao()));
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getUsuarioAlteracao().getCodigo());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getCodigo());
					return sqlAlterar;
				}
			});
			conteudoUnidadePaginaVO.setNovoObj(false);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		try {
			conteudoUnidadePaginaVO.setDataAlteracao(new Date());
			conteudoUnidadePaginaVO.getUsuarioAlteracao().setCodigo(usuario.getCodigo());
			conteudoUnidadePaginaVO.getUsuarioAlteracao().setNome(usuario.getNome());
			if (conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
				realizarGeracaoGrafico(conteudoUnidadePaginaVO);
			}
			realizarPublicacaoForum(conteudoUnidadePaginaVO, conteudo, usuario);
			copiarIconeConteudoUnidadePaginaDaPastaTemp(conteudoUnidadePaginaVO, configuracaoGeralSistemaVO);
			realizarPublicacaoIcone(conteudoUnidadePaginaVO, configuracaoGeralSistemaVO, usuario);
			copiarArquivoConteudoUnidadePagina(conteudoUnidadePaginaVO, configuracaoGeralSistemaVO);

			alterar(getIdEntidade(), controlarAcesso, usuario);
			final StringBuilder sql = new StringBuilder("UPDATE ConteudoUnidadePagina SET ");
			sql.append(" tipoRecursoEducacional = ? , titulo = ? , texto = ? , altura = ? , largura = ? , ");
			sql.append(" caminhoBaseRepositorio = ? , nomeRealArquivo = ? , nomeFisicoArquivo = ? , pagina = ? , tempo = ? ,");
			sql.append(" ponto = ? , unidadeConteudo = ? , caminhoIconeVoltar = ? , caminhoIconeAvancar = ? , labelIconeAvancar = ? ,");
			sql.append(" labelIconeVoltar = ? , recursoEducacional = ? , dataAlteracao = ? , usuarioAlteracao = ?, ");
			sql.append(" tituloGrafico = ?, tituloEixoX = ?, tituloEixoY = ?, apresentarLegenda = ?, valorGrafico = ?, tipoGrafico = ?, categoriaGrafico = ?, ");
			sql.append(" nomeIconeVoltar=?, nomeIconeAvancar = ?, iconeVoltar = ?, iconeAvancar = ?, forum = ? ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getTipoRecursoEducacional().toString());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getTitulo());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getTexto());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getAltura());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getLargura());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getCaminhoBaseRepositorio());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getNomeRealArquivo());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getNomeFisicoArquivo());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getPagina());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getTempo());
					sqlAlterar.setDouble(x++, conteudoUnidadePaginaVO.getPonto());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getCaminhoIconeVoltar());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getCaminhoIconeAvancar());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getLabelIconeAvancar());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getLabelIconeVoltar());
					if (conteudoUnidadePaginaVO.getRecursoEducacional().getCodigo() > 0) {
						sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getRecursoEducacional().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setDate(x++, Uteis.getDataJDBC(conteudoUnidadePaginaVO.getDataAlteracao()));
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getUsuarioAlteracao().getCodigo());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getTituloGrafico());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getTituloEixoX());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getTituloEixoY());
					sqlAlterar.setBoolean(x++, conteudoUnidadePaginaVO.getApresentarLegenda());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getValorGrafico());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getTipoGrafico().name());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getCategoriaGrafico());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getNomeIconeVoltar());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getNomeIconeAvancar());
					if (conteudoUnidadePaginaVO.getIconeVoltar().getCodigo() > 0) {
						sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getIconeVoltar().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (conteudoUnidadePaginaVO.getIconeAvancar().getCodigo() > 0) {
						sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getIconeAvancar().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && conteudoUnidadePaginaVO.getForum().getCodigo() > 0) {
						sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getForum().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getCodigo());
					return sqlAlterar;
				}
			}) <= 0) {
				incluir(conteudoUnidadePaginaVO, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
				return;
			}
			getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().alterarConteudoUnidadePaginaRecursoEducacional(conteudoUnidadePaginaVO, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
			incluirRecursoEducacional(conteudoUnidadePaginaVO, conteudo.getDisciplina(), controlarAcesso, usuario);
			conteudoUnidadePaginaVO.setNovoObj(false);
			for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
				if (obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
					obj.getListaExercicio().setConteudoAlterado(false);
				}
			}
			for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
				if (obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {
					obj.getListaExercicio().setConteudoAlterado(false);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarBackground(final ConteudoUnidadePaginaVO conteudoUnidadePaginaVO) throws Exception {
		try {

			final StringBuilder sql = new StringBuilder("UPDATE ConteudoUnidadePagina SET ");
			sql.append(" caminhoBaseBackground = ?, nomeImagemBackground = ?, corBackground = ?, tamanhoImagemBackgroundConteudo = ? , origemBackgroundConteudo = ? ");
			sql.append(" where codigo = ? ");
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getCaminhoBaseBackground());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getNomeImagemBackground());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getCorBackground());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getTamanhoImagemBackgroundConteudo().name());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getOrigemBackgroundConteudo().name());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getCodigo());
					return sqlAlterar;
				}
			}) <= 0) {

				return;
			}

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void validarDados(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();
		if (Uteis.retiraTags(conteudoUnidadePaginaVO.getTitulo()).trim().isEmpty()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_titulo"));
		}

		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
		
		if(conteudoUnidadePaginaVO.getTipoRecursoEducacional().isTipoRecursoTextoHtml()){
			conteudoUnidadePaginaVO.setTexto(UteisHTML.realizarValidacaoBackgroundConteudo(conteudoUnidadePaginaVO.getTexto()));	
		}
	}

	@Override
	public ConteudoUnidadePaginaVO consultarPorUnidadeConteudoPagina(Integer unidadeConteudo, Integer pagina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT * FROM ConteudoUnidadePagina");
		sb.append(" where unidadeConteudo = ").append(unidadeConteudo);
		sb.append(" and pagina = ").append(pagina);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, controlarAcesso, usuario);
		}
		// throw new Exception("Página não encontrada");
		return new ConteudoUnidadePaginaVO();
	}

	@Override
	public Map<Integer, List<ConteudoUnidadePaginaVO>> consultarPorUnidadeConteudoPaginaPorConteudo(Integer conteudo, String matricula, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT ConteudoUnidadePagina.*,  ");
		sb.append(" case when (select codigo from ConteudoRegistroAcesso where ConteudoRegistroAcesso.conteudoUnidadePagina = ConteudoUnidadePagina.codigo   ");
		sb.append(" and ConteudoRegistroAcesso.matricula = '").append(matricula).append("' limit 1) is not null then true else false end as paginaJaVisualizada ");
		sb.append(" FROM ConteudoUnidadePagina");
		sb.append(" inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo");

		sb.append(" where unidadeconteudo.conteudo = ").append(conteudo);
		sb.append(" order by ordem, pagina");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		ConteudoUnidadePaginaVO conteudoUnidadePaginaVO = null;
		Map<Integer, List<ConteudoUnidadePaginaVO>> unidadeConteudoVOs = new HashMap<Integer, List<ConteudoUnidadePaginaVO>>(0);
		while (rs.next()) {
			conteudoUnidadePaginaVO = montarDados(rs, nivelMontarDados, controlarAcesso, usuario);
			conteudoUnidadePaginaVO.setPaginaJaVisualizada(rs.getBoolean("paginaJaVisualizada"));
			if (unidadeConteudoVOs.containsKey(rs.getInt("unidadeConteudo"))) {
				unidadeConteudoVOs.get(rs.getInt("unidadeConteudo")).add(conteudoUnidadePaginaVO);
			} else {
				List<ConteudoUnidadePaginaVO> objs = new ArrayList<ConteudoUnidadePaginaVO>();
				objs.add(conteudoUnidadePaginaVO);
				unidadeConteudoVOs.put(conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo(), objs);
			}
		}
		return unidadeConteudoVOs;
	}

	@Override
	public ConteudoUnidadePaginaVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT * FROM ConteudoUnidadePagina");
		sb.append(" where codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, controlarAcesso, usuario);
		}
		throw new Exception("Página não encontrada");
	}

	@Override
	public List<ConteudoUnidadePaginaVO> consultarPorUnidadeConteudo(Integer unidadeConteudo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT ConteudoUnidadePagina.* ");
		sb.append(" FROM ConteudoUnidadePagina");
		sb.append(" where ConteudoUnidadePagina.unidadeConteudo = ").append(unidadeConteudo);
		sb.append(" order by ConteudoUnidadePagina.pagina");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, controlarAcesso, usuario);
	}

	private List<ConteudoUnidadePaginaVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		List<ConteudoUnidadePaginaVO> conteudoUnidadePaginaVOs = new ArrayList<ConteudoUnidadePaginaVO>(0);
		while (rs.next()) {
			conteudoUnidadePaginaVOs.add(montarDados(rs, nivelMontarDados, controlarAcesso, usuario));
		}
		return conteudoUnidadePaginaVOs;
	}

	private ConteudoUnidadePaginaVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ConteudoUnidadePaginaVO conteudoUnidadePaginaVO = new ConteudoUnidadePaginaVO();
		conteudoUnidadePaginaVO.setNovoObj(false);
		conteudoUnidadePaginaVO.setCodigo(rs.getInt("codigo"));
		conteudoUnidadePaginaVO.setAltura(rs.getInt("altura"));
		conteudoUnidadePaginaVO.setLargura(rs.getInt("largura"));
		conteudoUnidadePaginaVO.setCaminhoBaseRepositorio(rs.getString("caminhoBaseRepositorio"));
		conteudoUnidadePaginaVO.setCaminhoIconeAvancar(rs.getString("caminhoIconeAvancar"));
		conteudoUnidadePaginaVO.setCaminhoIconeVoltar(rs.getString("caminhoIconeVoltar"));
		conteudoUnidadePaginaVO.setLabelIconeAvancar(rs.getString("labelIconeAvancar"));
		conteudoUnidadePaginaVO.setLabelIconeVoltar(rs.getString("labelIconeVoltar"));
		conteudoUnidadePaginaVO.setNomeFisicoArquivo(rs.getString("nomeFisicoArquivo"));
		conteudoUnidadePaginaVO.setNomeRealArquivo(rs.getString("nomeRealArquivo"));
		conteudoUnidadePaginaVO.setPonto(rs.getDouble("ponto"));
		conteudoUnidadePaginaVO.setPagina(rs.getInt("pagina"));
		conteudoUnidadePaginaVO.setTexto(rs.getString("texto"));
		conteudoUnidadePaginaVO.setDataAlteracao(rs.getDate("dataAlteracao"));
		conteudoUnidadePaginaVO.setDataCadastro(rs.getDate("dataCadastro"));
		conteudoUnidadePaginaVO.setTempo(rs.getInt("tempo"));
		conteudoUnidadePaginaVO.setTitulo(rs.getString("titulo"));
		conteudoUnidadePaginaVO.getUnidadeConteudo().setCodigo(rs.getInt("unidadeconteudo"));
		conteudoUnidadePaginaVO.getUsuarioAlteracao().setCodigo(rs.getInt("usuarioAlteracao"));
		conteudoUnidadePaginaVO.getUsuarioCadastro().setCodigo(rs.getInt("usuarioCadastro"));
		conteudoUnidadePaginaVO.getRecursoEducacional().setCodigo(rs.getInt("recursoEducacional"));
		conteudoUnidadePaginaVO.setTituloGrafico(rs.getString("tituloGrafico"));
		conteudoUnidadePaginaVO.setTituloEixoX(rs.getString("tituloEixoX"));
		conteudoUnidadePaginaVO.setTituloEixoY(rs.getString("tituloEixoY"));
		conteudoUnidadePaginaVO.setValorGrafico(rs.getString("valorGrafico"));
		conteudoUnidadePaginaVO.setCategoriaGrafico(rs.getString("categoriaGrafico"));
		conteudoUnidadePaginaVO.setApresentarLegenda(rs.getBoolean("apresentarLegenda"));
		conteudoUnidadePaginaVO.setNomeIconeVoltar(rs.getString("nomeIconeVoltar"));
		conteudoUnidadePaginaVO.setNomeIconeAvancar(rs.getString("nomeIconeAvancar"));
		conteudoUnidadePaginaVO.getIconeVoltar().setCodigo(rs.getInt("iconeVoltar"));
		conteudoUnidadePaginaVO.getIconeAvancar().setCodigo(rs.getInt("iconeAvancar"));
		conteudoUnidadePaginaVO.getForum().setCodigo(rs.getInt("forum"));

		conteudoUnidadePaginaVO.setCaminhoBaseBackground(rs.getString("caminhoBaseBackground"));
		conteudoUnidadePaginaVO.setNomeImagemBackground(rs.getString("nomeImagemBackground"));
		conteudoUnidadePaginaVO.setCorBackground(rs.getString("corBackground"));
		if (rs.getString("tamanhoImagemBackgroundConteudo") != null && !rs.getString("tamanhoImagemBackgroundConteudo").isEmpty()) {
			conteudoUnidadePaginaVO.setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum.valueOf(rs.getString("tamanhoImagemBackgroundConteudo")));
		} else {
			conteudoUnidadePaginaVO.setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO);
		}
		if (rs.getString("origemBackgroundConteudo") != null && !rs.getString("origemBackgroundConteudo").isEmpty()) {
			conteudoUnidadePaginaVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.valueOf(rs.getString("origemBackgroundConteudo")));
		} else {
			conteudoUnidadePaginaVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.SEM_BACKGROUND);
		}

		if (rs.getString("tipoGrafico") != null && !rs.getString("tipoGrafico").isEmpty()) {
			conteudoUnidadePaginaVO.setTipoGrafico(TipoGraficoEnum.valueOf(rs.getString("tipoGrafico")));
		}

		conteudoUnidadePaginaVO.setTipoRecursoEducacional(TipoRecursoEducacionalEnum.valueOf(rs.getString("tipoRecursoEducacional")));
		if (conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && conteudoUnidadePaginaVO.getForum().getCodigo() > 0) {
			conteudoUnidadePaginaVO.setForum(getFacadeFactory().getForumFacade().consultarPorChavePrimaria(conteudoUnidadePaginaVO.getForum().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS));
		}
		if (nivelMontarDados.equals(NivelMontarDados.TODOS)) {
			conteudoUnidadePaginaVO.setConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(conteudoUnidadePaginaVO.getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.ANTES, nivelMontarDados, controlarAcesso, usuario));
			conteudoUnidadePaginaVO.setConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(conteudoUnidadePaginaVO.getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.DEPOIS, nivelMontarDados, controlarAcesso, usuario));
			conteudoUnidadePaginaVO.setConteudoUnidadePaginaRecursoEducacionalApoioProfessor(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(conteudoUnidadePaginaVO.getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.APOIO_PROFESSOR, nivelMontarDados, controlarAcesso, usuario));
		}
		return conteudoUnidadePaginaVO;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacionalEnum, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().validarDados(conteudoUnidadePaginaRecursoEducacionalVO);
		conteudoUnidadePaginaRecursoEducacionalVO.setConteudoUnidadePagina(conteudoUnidadePaginaVO);
		if (momentoApresentacaoRecursoEducacionalEnum.isAntes()) {
			adicionarConteudoUnidadePaginaRecursoEducacionalAntes(conteudoUnidadePaginaVO, conteudoUnidadePaginaRecursoEducacionalVO, momentoApresentacaoRecursoEducacionalEnum);
		} else if (momentoApresentacaoRecursoEducacionalEnum.isDepois()) {
			adicionarConteudoUnidadePaginaRecursoEducacionalDepois(conteudoUnidadePaginaVO, conteudoUnidadePaginaRecursoEducacionalVO, momentoApresentacaoRecursoEducacionalEnum);
		} else if (momentoApresentacaoRecursoEducacionalEnum.isApoioProfessor()) {
			adicionarConteudoUnidadePaginaRecursoEducacionalApoioProfessor(conteudoUnidadePaginaVO, conteudoUnidadePaginaRecursoEducacionalVO, momentoApresentacaoRecursoEducacionalEnum);
		}
		if (conteudoUnidadePaginaVO.getCodigo() > 0) {
			persistir(conteudoUnidadePaginaVO, conteudo, configuracaoGeralSistemaVO, true, usuario, realizandoClonagem);
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarConteudoUnidadePaginaRecursoEducacionalAntes(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacionalEnum) {
		conteudoUnidadePaginaRecursoEducacionalVO.setMomentoApresentacaoRecursoEducacional(momentoApresentacaoRecursoEducacionalEnum);
		if (conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() == 0) {
			conteudoUnidadePaginaRecursoEducacionalVO.setOrdemApresentacao(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().size() + 1);
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().add(conteudoUnidadePaginaRecursoEducacionalVO);
		} else {
			adicionarConteudoUnidadePaginaRecursoEducacionalVO(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(), conteudoUnidadePaginaRecursoEducacionalVO);
			//conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().set(conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() - 1, conteudoUnidadePaginaRecursoEducacionalVO);
		}
		int x = 1;
		for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
			obj.setOrdemApresentacao(x++);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarConteudoUnidadePaginaRecursoEducacionalDepois(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacionalEnum) {
		conteudoUnidadePaginaRecursoEducacionalVO.setMomentoApresentacaoRecursoEducacional(momentoApresentacaoRecursoEducacionalEnum);
		if (conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() == 0) {
			conteudoUnidadePaginaRecursoEducacionalVO.setOrdemApresentacao(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().size() + 1);
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().add(conteudoUnidadePaginaRecursoEducacionalVO);
		} else {
			adicionarConteudoUnidadePaginaRecursoEducacionalVO(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(), conteudoUnidadePaginaRecursoEducacionalVO);
			//conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().set(conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() - 1, conteudoUnidadePaginaRecursoEducacionalVO);
		}
		int x = 1;
		for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
			obj.setOrdemApresentacao(x++);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED) 
	public void adicionarConteudoUnidadePaginaRecursoEducacionalApoioProfessor(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacionalEnum) {
		conteudoUnidadePaginaRecursoEducacionalVO.setMomentoApresentacaoRecursoEducacional(momentoApresentacaoRecursoEducacionalEnum);
		if (conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() == 0) {
			conteudoUnidadePaginaRecursoEducacionalVO.setOrdemApresentacao(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor().size() + 1);
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor().add(conteudoUnidadePaginaRecursoEducacionalVO);
		} else {
			adicionarConteudoUnidadePaginaRecursoEducacionalVO(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor(), conteudoUnidadePaginaRecursoEducacionalVO);
			//conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor().set(conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() - 1, conteudoUnidadePaginaRecursoEducacionalVO);
		}
		int x = 1;
		for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor()) {
			obj.setOrdemApresentacao(x++);
		}
	}
	
	private	void adicionarConteudoUnidadePaginaRecursoEducacionalVO(List<ConteudoUnidadePaginaRecursoEducacionalVO> lista, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) { 
		int index = 0;
		for (ConteudoUnidadePaginaRecursoEducacionalVO objExistente : lista) {
			if(objExistente.equalsCampoSelecaoLista(conteudoUnidadePaginaRecursoEducacionalVO)) {
				lista.set(index, conteudoUnidadePaginaRecursoEducacionalVO);
				return;
			}
			index++;
		}
		lista.add(conteudoUnidadePaginaRecursoEducacionalVO);
	}
	
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().excluir(conteudoUnidadePaginaRecursoEducacionalVO, configuracaoGeralSistemaVO, false, usuarioVO);
		if (conteudoUnidadePaginaRecursoEducacionalVO.getMomentoApresentacaoRecursoEducacional().isAntes()) {
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().removeIf(p-> p.equalsCampoSelecaoLista(conteudoUnidadePaginaRecursoEducacionalVO));
			//conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().remove(conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() - 1);
			int x = 1;
			for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
				obj.setOrdemApresentacao(x++);
			}
		} else if (conteudoUnidadePaginaRecursoEducacionalVO.getMomentoApresentacaoRecursoEducacional().isDepois()) {
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().removeIf(p-> p.equalsCampoSelecaoLista(conteudoUnidadePaginaRecursoEducacionalVO));
			//conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().remove(conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() - 1);
			int x = 1;
			for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
				obj.setOrdemApresentacao(x++);
			}
		} else if (conteudoUnidadePaginaRecursoEducacionalVO.getMomentoApresentacaoRecursoEducacional().isApoioProfessor()) {
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor().removeIf(p-> p.equalsCampoSelecaoLista(conteudoUnidadePaginaRecursoEducacionalVO));
			//conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor().remove(conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() - 1);
			int x = 1;
			for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor()) {
				obj.setOrdemApresentacao(x++);
			}
		}

	}

	@Override
	public void alterarOrdemApresentacaoConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, int posicao, boolean recursoAnterior) throws Exception {
		getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().validarDados(conteudoUnidadePaginaRecursoEducacionalVO);
		if (recursoAnterior) {
			//conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().remove(conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() - 1);
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().removeIf(p-> p.equalsCampoSelecaoLista(conteudoUnidadePaginaRecursoEducacionalVO));
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().add(posicao - 1, conteudoUnidadePaginaRecursoEducacionalVO);
			int x = 1;
			for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
				obj.setOrdemApresentacao(x++);
			}
		} else {
			//conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().remove(conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao() - 1);
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().removeIf(p-> p.equalsCampoSelecaoLista(conteudoUnidadePaginaRecursoEducacionalVO));
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().add(posicao - 1, conteudoUnidadePaginaRecursoEducacionalVO);
			int x = 1;
			for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
				obj.setOrdemApresentacao(x++);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void upLoadIconeConteudoUnidadePagina(FileUploadEvent upload, ConteudoUnidadePaginaVO conteudoUnidadePagina, boolean iconeVoltar, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		String nomeIcone = String.valueOf(new Date().getTime()) + upload.getFile().getFileName().substring(upload.getFile().getFileName().lastIndexOf("."), upload.getFile().getFileName().length());
		File arquivoExistente = null;
		try {
			if (iconeVoltar) {
				if (conteudoUnidadePagina.getCaminhoIconeVoltar().contains("EAD_TMP")) {
					arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + conteudoUnidadePagina.getCaminhoIconeVoltar() + File.separator + conteudoUnidadePagina.getNomeIconeVoltar());
					ArquivoHelper.delete(arquivoExistente);
				}
				conteudoUnidadePagina.setCaminhoIconeVoltar(PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.ICONE.getValue());
				conteudoUnidadePagina.setNomeIconeVoltar(nomeIcone);
			} else {
				if (conteudoUnidadePagina.getCaminhoIconeAvancar().contains("EAD_TMP")) {
					arquivoExistente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + conteudoUnidadePagina.getCaminhoIconeAvancar() + File.separator + conteudoUnidadePagina.getNomeIconeAvancar());
					ArquivoHelper.delete(arquivoExistente);
				}
				conteudoUnidadePagina.setCaminhoIconeAvancar(PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.ICONE.getValue());
				conteudoUnidadePagina.setNomeIconeAvancar(nomeIcone);
			}
			
			ArquivoHelper.salvarIconeNaPastaTemp(new File(upload.getFile().getFileName()), nomeIcone, configuracaoGeralSistemaVO, usuarioVO);
		} catch (Exception e) {
			throw e;
		} finally {
			arquivoExistente = null;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void copiarIconeConteudoUnidadePaginaDaPastaTemp(ConteudoUnidadePaginaVO conteudoUnidadePagina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			if (conteudoUnidadePagina.getCaminhoIconeVoltar().contains("EAD_TMP")) {
				ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(conteudoUnidadePagina.getCaminhoIconeVoltar(), conteudoUnidadePagina.getNomeIconeVoltar(), configuracaoGeralSistemaVO);
				conteudoUnidadePagina.setCaminhoIconeVoltar(conteudoUnidadePagina.getCaminhoIconeVoltar().replaceAll("_TMP", ""));
			}
			if (conteudoUnidadePagina.getCaminhoIconeAvancar().contains("EAD_TMP")) {
				ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(conteudoUnidadePagina.getCaminhoIconeAvancar(), conteudoUnidadePagina.getNomeIconeAvancar(), configuracaoGeralSistemaVO);
				conteudoUnidadePagina.setCaminhoIconeAvancar(conteudoUnidadePagina.getCaminhoIconeAvancar().replaceAll("_TMP", ""));
			}
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void upLoadArquivoConteudoUnidadePaginaHtml(FileUploadEvent upload, Integer disciplina, ConteudoUnidadePaginaVO conteudoUnidadePagina, Boolean publicarImagem, String nomeImagem, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		if (publicarImagem && nomeImagem.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_RecursoEducacional_titulo"));
		}
		String caminhoBase = PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator + conteudoUnidadePagina.getTipoRecursoEducacional().name();
		String nomeFisico = String.valueOf(new Date().getTime()) + upload.getFile().getFileName().substring(upload.getFile().getFileName().lastIndexOf("."), upload.getFile().getFileName().length());
		ArquivoHelper.salvarArquivoRecursoEducacionalNaPastaTemp(upload, conteudoUnidadePagina.getTipoRecursoEducacional(), disciplina, nomeFisico, configuracaoGeralSistemaVO, usuarioVO);
		ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(caminhoBase, nomeFisico, configuracaoGeralSistemaVO);
		caminhoBase = PastaBaseArquivoEnum.EAD.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO.getValue() + File.separator + disciplina + File.separator + conteudoUnidadePagina.getTipoRecursoEducacional().name();
		StringBuilder incorporar = new StringBuilder(" ");
		incorporar.append("<img style=\"vertical-align:middle;max-height:200px; text-align:center;\" src=\"" + configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo());
		incorporar.append("/" + caminhoBase.replaceAll("\\\\", "/"));
		incorporar.append("/" + nomeFisico + "?UID=" + Calendar.getInstance().getTime() + "\"/>");
		conteudoUnidadePagina.setTexto(conteudoUnidadePagina.getTexto().replaceAll("</body>", "</br>" + incorporar.toString() + "</body>"));

		if (publicarImagem) {
			RecursoEducacionalVO recursoEducacionalVO = new RecursoEducacionalVO();

			recursoEducacionalVO.setCaminhoBaseRepositorio(caminhoBase);
			recursoEducacionalVO.setNomeFisicoArquivo(nomeFisico);
			recursoEducacionalVO.setNomeRealArquivo(upload.getFile().getFileName());
			recursoEducacionalVO.setTitulo(nomeImagem);
			recursoEducacionalVO.setTipoRecursoEducacional(TipoRecursoEducacionalEnum.IMAGEM);
			recursoEducacionalVO.getDisciplina().setCodigo(disciplina);
			getFacadeFactory().getRecursoEducacionalFacade().persistir(recursoEducacionalVO, false, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void upLoadArquivoConteudoUnidadePagina(FileUploadEvent upload, Integer disciplina, ConteudoUnidadePaginaVO conteudoUnidadePagina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			conteudoUnidadePagina.setConteudoPaginaApresentar(null);
			conteudoUnidadePagina.setListaImagensSlide(null);
			if (!conteudoUnidadePagina.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM)) {
				conteudoUnidadePagina.setCaminhoBaseRepositorio(PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator + conteudoUnidadePagina.getTipoRecursoEducacional().name());
				conteudoUnidadePagina.setNomeRealArquivo(upload.getFile().getFileName());
				if (conteudoUnidadePagina.getNomeFisicoArquivo().isEmpty()) {
					conteudoUnidadePagina.setNomeFisicoArquivo(String.valueOf(new Date().getTime()) + upload.getFile().getFileName().substring(upload.getFile().getFileName().lastIndexOf("."), upload.getFile().getFileName().length()));
				} else {
					conteudoUnidadePagina.setNomeFisicoArquivo(conteudoUnidadePagina.getNomeFisicoArquivo().substring(0, conteudoUnidadePagina.getNomeFisicoArquivo().indexOf(".")) + upload.getFile().getFileName().substring(upload.getFile().getFileName().lastIndexOf("."), upload.getFile().getFileName().length()));
				}
				conteudoUnidadePagina.setRecursoEducacional(null);
				ArquivoHelper.salvarArquivoRecursoEducacionalNaPastaTemp(upload, conteudoUnidadePagina.getTipoRecursoEducacional(), disciplina, conteudoUnidadePagina.getNomeFisicoArquivo(), configuracaoGeralSistemaVO, usuarioVO);

			} else {
				String nomeFisico = "";
				if (!conteudoUnidadePagina.getNomeRealArquivo().contains(upload.getFile().getFileName())) {
					nomeFisico = String.valueOf(new Date().getTime()) + upload.getFile().getFileName().substring(upload.getFile().getFileName().lastIndexOf("."), upload.getFile().getFileName().length());
					if (!conteudoUnidadePagina.getNomeFisicoArquivo().trim().isEmpty()) {
						conteudoUnidadePagina.setCaminhoBaseRepositorio(conteudoUnidadePagina.getCaminhoBaseRepositorio() + ", " + PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator + conteudoUnidadePagina.getTipoRecursoEducacional().name());
						conteudoUnidadePagina.setNomeFisicoArquivo(conteudoUnidadePagina.getNomeFisicoArquivo() + ", " + nomeFisico);
						conteudoUnidadePagina.setNomeRealArquivo(conteudoUnidadePagina.getNomeRealArquivo() + ", " + upload.getFile().getFileName());
					} else {
						conteudoUnidadePagina.setCaminhoBaseRepositorio(PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator + conteudoUnidadePagina.getTipoRecursoEducacional().name());
						conteudoUnidadePagina.setNomeFisicoArquivo(nomeFisico);
						conteudoUnidadePagina.setNomeRealArquivo(upload.getFile().getFileName());
					}
					ArquivoHelper.salvarArquivoRecursoEducacionalNaPastaTemp(upload, conteudoUnidadePagina.getTipoRecursoEducacional(), disciplina, nomeFisico, configuracaoGeralSistemaVO, usuarioVO);
				}
			}
//			conteudoUnidadePagina.getConteudoPaginaApresentar(configuracaoGeralSistemaVO, "360px");

		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void copiarArquivoConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePagina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			if ((conteudoUnidadePagina.getTipoRecursoEducacional().getNecessitaUploadArquivo()) && conteudoUnidadePagina.getCaminhoBaseRepositorio().contains("_TMP")) {
				conteudoUnidadePagina.setRecursoEducacional(null);
				if (conteudoUnidadePagina.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM) && conteudoUnidadePagina.getNomeFisicoArquivo().contains(",")) {
					String[] nomeArquivos = conteudoUnidadePagina.getNomeFisicoArquivo().split(",");
					String[] caminhosArquivos = conteudoUnidadePagina.getCaminhoBaseRepositorio().split(",");
					int x = 0;
					for (String nomeArquivo : nomeArquivos) {
						if (caminhosArquivos[x].contains("_TMP")) {
							ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(caminhosArquivos[x].trim(), nomeArquivo.trim(), configuracaoGeralSistemaVO);
						}
						x++;
					}
				} else {
					ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(conteudoUnidadePagina.getCaminhoBaseRepositorio(), conteudoUnidadePagina.getNomeFisicoArquivo(), configuracaoGeralSistemaVO);
				}

				conteudoUnidadePagina.setCaminhoBaseRepositorio(conteudoUnidadePagina.getCaminhoBaseRepositorio().replaceAll("_TMP", ""));
			}
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void copiarArquivoBackgroundConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePagina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			if (conteudoUnidadePagina.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.PAGINA) && conteudoUnidadePagina.getCaminhoBaseBackground().contains("_TMP")) {
				ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(conteudoUnidadePagina.getCaminhoBaseBackground(), conteudoUnidadePagina.getNomeImagemBackground(), configuracaoGeralSistemaVO);
				conteudoUnidadePagina.setCaminhoBaseBackground(conteudoUnidadePagina.getCaminhoBaseBackground().replaceAll("_TMP", ""));
				getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarReplicacaoBackgroundParaRecursoEducacional(conteudoUnidadePagina.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(), OrigemBackgroundConteudoEnum.PAGINA, conteudoUnidadePagina.getCaminhoBaseBackground(), conteudoUnidadePagina.getNomeImagemBackground(), conteudoUnidadePagina.getCorBackground(), false, OrigemBackgroundConteudoEnum.PAGINA, conteudoUnidadePagina.getTamanhoImagemBackgroundConteudo(), false);
				getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarReplicacaoBackgroundParaRecursoEducacional(conteudoUnidadePagina.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(), OrigemBackgroundConteudoEnum.PAGINA, conteudoUnidadePagina.getCaminhoBaseBackground(), conteudoUnidadePagina.getNomeImagemBackground(), conteudoUnidadePagina.getCorBackground(), false, OrigemBackgroundConteudoEnum.PAGINA, conteudoUnidadePagina.getTamanhoImagemBackgroundConteudo(), false);
				getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarReplicacaoBackgroundParaRecursoEducacional(conteudoUnidadePagina.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor(), OrigemBackgroundConteudoEnum.PAGINA, conteudoUnidadePagina.getCaminhoBaseBackground(), conteudoUnidadePagina.getNomeImagemBackground(), conteudoUnidadePagina.getCorBackground(), false, OrigemBackgroundConteudoEnum.PAGINA, conteudoUnidadePagina.getTamanhoImagemBackgroundConteudo(), false);
			}
			if (conteudoUnidadePagina.getExcluirImagemBackground() && conteudoUnidadePagina.getCodigo() > 0) {
				ConteudoUnidadePaginaVO objAnt = consultarPorChavePrimaria(conteudoUnidadePagina.getCodigo(), NivelMontarDados.BASICO, false, null);
				if (objAnt != null && objAnt.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.PAGINA) && !objAnt.getCaminhoBaseBackground().trim().isEmpty() && !objAnt.getNomeImagemBackground().trim().isEmpty()) {
					String caminho = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + objAnt.getCaminhoBaseBackground() + File.separator + objAnt.getNomeImagemBackground();
					ArquivoHelper.delete(new File(caminho));
				}
				conteudoUnidadePagina.setExcluirImagemBackground(false);
			}
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

	@Override
	public void adicionarSerieGrafico(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, String serie, Double valor) throws Exception {
		if (serie == null || serie.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_serie"));
		}
		if (conteudoUnidadePaginaVO.getTipoGrafico().equals(TipoGraficoEnum.PIZZA) && (valor == null || valor == 0.0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_valor"));
		}
		if (conteudoUnidadePaginaVO.getTipoGrafico().equals(TipoGraficoEnum.PIZZA)) {
			for (ConteudoUnidadePaginaGraficoPizzaVO conteudoUnidadePaginaGraficoVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoVOs()) {
				if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoVO.getSerie().trim().toUpperCase()).equals(Uteis.removerAcentuacao(serie.trim().toUpperCase()))) {
					throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_serie_adicionada"));
				}
			}
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoVOs().add(new ConteudoUnidadePaginaGraficoPizzaVO(serie, valor));
		} else {
			for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs()) {
				if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoSerieVO.getSerie().trim().toUpperCase()).equals(Uteis.removerAcentuacao(serie.trim().toUpperCase()))) {
					throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_serie_adicionada"));
				}
			}
			List<ConteudoUnidadePaginaGraficoSerieValorVO> conteudoUnidadePaginaGraficoSerieValorVOs = new ArrayList<ConteudoUnidadePaginaGraficoSerieValorVO>(0);
			for (int x = 1; x <= conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoCategoriaVOs().size(); x++) {
				conteudoUnidadePaginaGraficoSerieValorVOs.add(new ConteudoUnidadePaginaGraficoSerieValorVO(x, 0.0));
			}
			conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs().add(new ConteudoUnidadePaginaGraficoSerieVO(conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs().size() + 1, serie, conteudoUnidadePaginaGraficoSerieValorVOs));
		}
	}

	@Override
	public void adicionarCategoriaGrafico(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, String categoria) throws Exception {
		if (categoria == null || categoria.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_categoria"));
		}
		for (ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoCategoriaVOs()) {
			if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoCategoriaVO.getCategoria().trim().toUpperCase()).equals(Uteis.removerAcentuacao(categoria.trim().toUpperCase()))) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_categoria_adicionada"));
			}
		}
		for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs()) {
			conteudoUnidadePaginaGraficoSerieVO.getConteudoUnidadePaginaGraficoSerieValorVOs().add(new ConteudoUnidadePaginaGraficoSerieValorVO(conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoCategoriaVOs().size() + 1, 0.0));
		}
		conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoCategoriaVOs().add(new ConteudoUnidadePaginaGraficoCategoriaVO(conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoCategoriaVOs().size() + 1, categoria));

	}

	@Override
	public void removerCategoriaGrafico(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaGraficoCategoriaVO categoria) throws Exception {
		int index = 0;
		for (ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoCategoriaVOs()) {
			if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoCategoriaVO.getCategoria().trim().toUpperCase()).equals(Uteis.removerAcentuacao(categoria.getCategoria().trim().toUpperCase()))) {
				conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoCategoriaVOs().remove(index);
				for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs()) {
					conteudoUnidadePaginaGraficoSerieVO.getConteudoUnidadePaginaGraficoSerieValorVOs().removeIf(item -> item.getSequencia().equals(categoria.getSequencia()));
				}
				conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs().removeIf(item -> item.getConteudoUnidadePaginaGraficoSerieValorVOs().isEmpty());
				return;
			}
			index++;
		}
	}

	@Override
	public void removerSerieGrafico(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, String serie) throws Exception {
		int index = 0;
		if (conteudoUnidadePaginaVO.getTipoGrafico().equals(TipoGraficoEnum.PIZZA)) {
			for (ConteudoUnidadePaginaGraficoPizzaVO conteudoUnidadePaginaGraficoVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoVOs()) {
				if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoVO.getSerie().trim().toUpperCase()).equals(Uteis.removerAcentuacao(serie.trim().toUpperCase()))) {
					conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoVOs().remove(index);
					return;
				}
				index++;
			}

		} else {
			for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs()) {
				if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoSerieVO.getSerie().trim().toUpperCase()).equals(Uteis.removerAcentuacao(serie.trim().toUpperCase()))) {
					conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs().remove(index);
					return;
				}
				index++;
			}

		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarGeracaoGrafico(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO) {
		StringBuilder valorGrafico = new StringBuilder("");
		if (conteudoUnidadePaginaVO.getTipoGrafico().equals(TipoGraficoEnum.PIZZA)) {
			for (ConteudoUnidadePaginaGraficoPizzaVO conteudoUnidadePaginaGraficoVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoVOs()) {
				if (!valorGrafico.toString().isEmpty()) {
					valorGrafico.append(",");
				}
				valorGrafico.append("{nome:'").append(conteudoUnidadePaginaGraficoVO.getSerie()).append("', y:").append(conteudoUnidadePaginaGraficoVO.getValor()).append(", valor:'").append(Uteis.getDoubleFormatado(conteudoUnidadePaginaGraficoVO.getValor())).append("'}");
			}
		} else {
			StringBuilder categoriaGrafico = new StringBuilder("");
			for (ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoCategoriaVOs()) {
				if (!categoriaGrafico.toString().isEmpty()) {
					categoriaGrafico.append(",");
				}
				categoriaGrafico.append("'").append(conteudoUnidadePaginaGraficoCategoriaVO.getCategoria()).append("'");
			}
			conteudoUnidadePaginaVO.setCategoriaGrafico(categoriaGrafico.toString());
			for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs()) {
				if (!valorGrafico.toString().isEmpty()) {
					valorGrafico.append(",");
				}
				valorGrafico.append("{name:'").append(conteudoUnidadePaginaGraficoSerieVO.getSerie()).append("', ");
				valorGrafico.append("data:[");
				StringBuilder dadosGrafico = new StringBuilder("");
				for (ConteudoUnidadePaginaGraficoSerieValorVO conteudoUnidadePaginaGraficoSerieValorVO : conteudoUnidadePaginaGraficoSerieVO.getConteudoUnidadePaginaGraficoSerieValorVOs()) {
					if (!dadosGrafico.toString().isEmpty()) {
						dadosGrafico.append(",");
					}
					dadosGrafico.append(conteudoUnidadePaginaGraficoSerieValorVO.getValor());
				}
				valorGrafico.append(dadosGrafico);
				valorGrafico.append("]} ");
			}
		}
		conteudoUnidadePaginaVO.setValorGrafico(valorGrafico.toString());
	}

	@Override
	public void realizarGeracaoConteudoUnidadePaginaGraficoVO(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO) throws Exception {
		conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoCategoriaVOs().clear();
		conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs().clear();
		conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoVOs().clear();

		if (conteudoUnidadePaginaVO.getTipoGrafico().equals(TipoGraficoEnum.PIZZA)) {
			List<String> series = new ArrayList<String>(0);
			if (conteudoUnidadePaginaVO.getValorGrafico().contains(",{")) {
				String valor = conteudoUnidadePaginaVO.getValorGrafico();
				while (valor.indexOf("{") >= 0) {
					valor = valor.substring(valor.indexOf("{"), valor.length());
					series.add(valor.substring(0, valor.indexOf("}") + 1));
					valor = valor.substring(valor.indexOf("}") + 1, valor.length());
				}
			}else if (conteudoUnidadePaginaVO.getValorGrafico().contains(",[")) {
				String valor = conteudoUnidadePaginaVO.getValorGrafico();
				while (valor.indexOf("[") >= 0) {
					valor = valor.substring(valor.indexOf("["), valor.length());
					series.add(valor.substring(0, valor.indexOf("]") + 1));
					valor = valor.substring(valor.indexOf("]") + 1, valor.length());
				}
			} else {
				if (!conteudoUnidadePaginaVO.getValorGrafico().trim().isEmpty()) {
					series.add(conteudoUnidadePaginaVO.getValorGrafico());
				}
			}
			for (String serie : series) {
				if (serie == null) {
					break;
				}
				serie = serie.replace("[", "").replace("]", "");
				serie = serie.replace("{", "").replace("}", "");
				serie = serie.replace("'", "");
				if(serie.contains("nome:")) {
					adicionarSerieGrafico(conteudoUnidadePaginaVO, serie.substring(serie.indexOf("nome:")+5, serie.indexOf(",")).trim().replaceAll("'", ""), Double.parseDouble(serie.substring(serie.indexOf("y:") +2, serie.indexOf(", valor:")).trim()));
				}else {
					adicionarSerieGrafico(conteudoUnidadePaginaVO, serie.substring(0, serie.indexOf(",")).trim().replaceAll("'", ""), Double.parseDouble(serie.substring(serie.indexOf(",") + 1).trim()));
				}
			}

		} else {
			String[] categorias = null;
			if (conteudoUnidadePaginaVO.getCategoriaGrafico().contains(",")) {
				categorias = conteudoUnidadePaginaVO.getCategoriaGrafico().split(",");
			} else {
				if (!conteudoUnidadePaginaVO.getCategoriaGrafico().trim().isEmpty()) {
					categorias = new String[1];
					categorias[0] = conteudoUnidadePaginaVO.getCategoriaGrafico();
				}
			}
			if (categorias != null) {
				for (String categoria : categorias) {
					adicionarCategoriaGrafico(conteudoUnidadePaginaVO, categoria.replaceAll("'", "").trim());
				}
			}

			List<String> series = new ArrayList<String>(0);
			if (conteudoUnidadePaginaVO.getValorGrafico().contains(",{")) {
				String valor = conteudoUnidadePaginaVO.getValorGrafico();
				while (valor.indexOf("{") >= 0) {
					valor = valor.substring(valor.indexOf("{"), valor.length());
					series.add(valor.substring(0, valor.indexOf("}") + 1));
					valor = valor.substring(valor.indexOf("}") + 1, valor.length());
				}
			} else {
				if (!conteudoUnidadePaginaVO.getValorGrafico().trim().isEmpty()) {
					series.add(conteudoUnidadePaginaVO.getValorGrafico());
				}
			}
			ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO = null;
			String[] valores = null;
			int x = 1;
			for (String serie : series) {
				conteudoUnidadePaginaGraficoSerieVO = new ConteudoUnidadePaginaGraficoSerieVO(x++, serie.substring(serie.indexOf("name:'") + 6, serie.indexOf("', data:[")));
				serie = serie.substring(serie.indexOf(", data:[") + 8, serie.lastIndexOf("]"));
				if (serie.contains(",")) {
					valores = serie.split(",");
				} else {
					valores = new String[1];
					valores[0] = serie;
				}
				int z = 1;
				for (String valor : valores) {
					conteudoUnidadePaginaGraficoSerieVO.getConteudoUnidadePaginaGraficoSerieValorVOs().add(new ConteudoUnidadePaginaGraficoSerieValorVO(z++, Double.parseDouble(valor.trim())));
				}
				conteudoUnidadePaginaVO.getConteudoUnidadePaginaGraficoSerieVOs().add(conteudoUnidadePaginaGraficoSerieVO);
			}

		}

	}

	@Override
	public void alterarOrdemConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional1, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional2, UsuarioVO usuario) throws Exception {
		int ordem1 = conteudoUnidadePaginaRecursoEducacional1.getOrdemApresentacao();
		int ordem2 = conteudoUnidadePaginaRecursoEducacional2 != null ? conteudoUnidadePaginaRecursoEducacional2.getOrdemApresentacao() : 1;
		try {
			if (conteudoUnidadePaginaRecursoEducacional1 != null && conteudoUnidadePaginaRecursoEducacional2 != null && conteudoUnidadePaginaRecursoEducacional2.getMomentoApresentacaoRecursoEducacional().equals(conteudoUnidadePaginaRecursoEducacional1.getMomentoApresentacaoRecursoEducacional())) {
				conteudoUnidadePaginaRecursoEducacional2.setOrdemApresentacao(ordem1);
				conteudoUnidadePaginaRecursoEducacional1.setOrdemApresentacao(ordem2);
			} else {

				if (conteudoUnidadePaginaRecursoEducacional1.getMomentoApresentacaoRecursoEducacional().isAntes()) {
					//conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().remove(conteudoUnidadePaginaRecursoEducacional1.getOrdemApresentacao() - 1);
					conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().removeIf(p-> p.equalsCampoSelecaoLista(conteudoUnidadePaginaRecursoEducacional1));
					int x = 1;
					for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
						obj.setOrdemApresentacao(x++);
					}
				} else if (conteudoUnidadePaginaRecursoEducacional1.getMomentoApresentacaoRecursoEducacional().isDepois()) {
					//conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().remove(conteudoUnidadePaginaRecursoEducacional1.getOrdemApresentacao() - 1);
					conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().removeIf(p-> p.equalsCampoSelecaoLista(conteudoUnidadePaginaRecursoEducacional1));
					int x = 1;
					for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
						obj.setOrdemApresentacao(x++);
					}
				}

				if (conteudoUnidadePaginaRecursoEducacional2 == null && conteudoUnidadePaginaRecursoEducacional1.getMomentoApresentacaoRecursoEducacional().isAntes()) {
					conteudoUnidadePaginaRecursoEducacional1.setOrdemApresentacao(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().size() + 1);
				}
				if (conteudoUnidadePaginaRecursoEducacional2 == null && conteudoUnidadePaginaRecursoEducacional1.getMomentoApresentacaoRecursoEducacional().isDepois()) {
					conteudoUnidadePaginaRecursoEducacional1.setOrdemApresentacao(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().size() + 1);
				}
				if (conteudoUnidadePaginaRecursoEducacional2 != null) {
					conteudoUnidadePaginaRecursoEducacional1.setOrdemApresentacao(conteudoUnidadePaginaRecursoEducacional2.getOrdemApresentacao() + 1);
				}
				int x = 1;
				if (conteudoUnidadePaginaRecursoEducacional1.getMomentoApresentacaoRecursoEducacional().isDepois()) {
					conteudoUnidadePaginaRecursoEducacional1.setMomentoApresentacaoRecursoEducacional(MomentoApresentacaoRecursoEducacionalEnum.ANTES);
					conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().add(conteudoUnidadePaginaRecursoEducacional1.getOrdemApresentacao() - 1, conteudoUnidadePaginaRecursoEducacional1);
					for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
						obj.setOrdemApresentacao(x++);
					}
				} else if (conteudoUnidadePaginaRecursoEducacional1.getMomentoApresentacaoRecursoEducacional().isAntes()) {
					conteudoUnidadePaginaRecursoEducacional1.setMomentoApresentacaoRecursoEducacional(MomentoApresentacaoRecursoEducacionalEnum.DEPOIS);
					conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().add(conteudoUnidadePaginaRecursoEducacional1.getOrdemApresentacao() - 1, conteudoUnidadePaginaRecursoEducacional1);
					for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
						obj.setOrdemApresentacao(x++);
					}
				}

			}
			Ordenacao.ordenarLista(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(), "ordemApresentacao");
			Ordenacao.ordenarLista(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(), "ordemApresentacao");
			Ordenacao.ordenarLista(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor(), "ordemApresentacao");
		} catch (Exception e) {
			if (conteudoUnidadePaginaRecursoEducacional2 != null) {
				conteudoUnidadePaginaRecursoEducacional2.setOrdemApresentacao(ordem2);	
			}
			if (conteudoUnidadePaginaRecursoEducacional1 != null) {
				conteudoUnidadePaginaRecursoEducacional1.setOrdemApresentacao(ordem1);	
			}
			throw e;
		}

	}

	@Override
	public void uploadImagemBackgroundConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, Integer disciplina, FileUploadEvent uploadEvent, Boolean aplicarBackRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {

		String arquivo = "";
		if (conteudoUnidadePaginaVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.PAGINA) && !conteudoUnidadePaginaVO.getNomeImagemBackground().trim().isEmpty()) {

			arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + conteudoUnidadePaginaVO.getCaminhoBaseBackground() + File.separator + conteudoUnidadePaginaVO.getNomeImagemBackground();
			ArquivoHelper.delete(new File(arquivo));
		}

		String extensao = uploadEvent.getFile().getFileName().substring(uploadEvent.getFile().getFileName().lastIndexOf("."), uploadEvent.getFile().getFileName().length());
		conteudoUnidadePaginaVO.setNomeImagemBackground(usuarioVO.getCodigo() + "_" + (new Date().getTime()) + extensao);
		conteudoUnidadePaginaVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.PAGINA);
		if (conteudoUnidadePaginaVO.getCodigo() > 0) {
			conteudoUnidadePaginaVO.setCaminhoBaseBackground(PastaBaseArquivoEnum.EAD.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO.getValue() + File.separator + disciplina + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_BACKGROUND.getValue());
		} else {
			conteudoUnidadePaginaVO.setCaminhoBaseBackground(PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_BACKGROUND_TMP.getValue());
		}
		arquivo = conteudoUnidadePaginaVO.getCaminhoBaseBackground() + File.separator + conteudoUnidadePaginaVO.getNomeImagemBackground();
		ArquivoHelper.salvarArquivoNaPastaTemp(uploadEvent, conteudoUnidadePaginaVO.getNomeImagemBackground(), conteudoUnidadePaginaVO.getCaminhoBaseBackground(), configuracaoGeralSistemaVO, usuarioVO);
		if (conteudoUnidadePaginaVO.getCodigo() > 0) {
			alterarBackground(conteudoUnidadePaginaVO);
		}
		alterarBackgroundEdicao(conteudoUnidadePaginaVO, aplicarBackRecursoEducacional);

	}

	@Override
	public void removerImagemBackgroundConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {

		if (conteudoUnidadePaginaVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.PAGINA)) {
			if (!conteudoUnidadePaginaVO.getNomeImagemBackground().trim().isEmpty() && !getFacadeFactory().getConteudoFacade().validarUsoDaImagemBackgroundNoConteudo(conteudoUnidadePaginaVO.getNomeImagemBackground(), null) && (conteudoUnidadePaginaVO.getCaminhoBaseBackground().contains("_TMP") || conteudoUnidadePaginaVO.getCodigo() > 0)) {
				String arquivo = "";
				if (conteudoUnidadePaginaVO.getCaminhoBaseBackground().contains("_TMP")) {
					arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + conteudoUnidadePaginaVO.getCaminhoBaseBackground() + File.separator + conteudoUnidadePaginaVO.getNomeImagemBackground();
				} else {
					arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + conteudoUnidadePaginaVO.getCaminhoBaseBackground() + File.separator + conteudoUnidadePaginaVO.getNomeImagemBackground();
				}
				ArquivoHelper.delete(new File(arquivo));
			} else {
				conteudoUnidadePaginaVO.setExcluirImagemBackground(true);
			}
		} else {
			conteudoUnidadePaginaVO.setExcluirImagemBackground(false);
		}
		conteudoUnidadePaginaVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.SEM_BACKGROUND);
		conteudoUnidadePaginaVO.setCaminhoBaseBackground("");
		conteudoUnidadePaginaVO.setNomeImagemBackground("");
		conteudoUnidadePaginaVO.setCorBackground("ffffff");
		if (conteudoUnidadePaginaVO.getCodigo() > 0) {
			alterarBackground(conteudoUnidadePaginaVO);
		}
		alterarBackgroundEdicao(conteudoUnidadePaginaVO, false);

	}

	@Override
	public void alterarBackgroundEdicao(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, Boolean aplicarBackRecursoEducacional) throws Exception {

		getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarReplicacaoBackgroundParaRecursoEducacional(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(), OrigemBackgroundConteudoEnum.PAGINA, conteudoUnidadePaginaVO.getCaminhoBaseBackground(), conteudoUnidadePaginaVO.getNomeImagemBackground(), conteudoUnidadePaginaVO.getCorBackground(), aplicarBackRecursoEducacional, conteudoUnidadePaginaVO.getOrigemBackgroundConteudo(), conteudoUnidadePaginaVO.getTamanhoImagemBackgroundConteudo(), conteudoUnidadePaginaVO.getCodigo() > 0);

		getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarReplicacaoBackgroundParaRecursoEducacional(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(), OrigemBackgroundConteudoEnum.PAGINA, conteudoUnidadePaginaVO.getCaminhoBaseBackground(), conteudoUnidadePaginaVO.getNomeImagemBackground(), conteudoUnidadePaginaVO.getCorBackground(), aplicarBackRecursoEducacional, conteudoUnidadePaginaVO.getOrigemBackgroundConteudo(), conteudoUnidadePaginaVO.getTamanhoImagemBackgroundConteudo(), conteudoUnidadePaginaVO.getCodigo() > 0);
		if (conteudoUnidadePaginaVO.getCodigo() > 0) {
			alterarBackground(conteudoUnidadePaginaVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarReplicacaoBackgroundParaPagina(List<ConteudoUnidadePaginaVO> conteudoUnidadePaginaVOs, OrigemBackgroundConteudoEnum origemBase, String caminhoBase, String nomeArquivo, String cor, Boolean aplicarBackRecursoEducacional, OrigemBackgroundConteudoEnum origemUtilizar, TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudoEnum, Boolean gravarAlteracao) throws Exception {
		for (ConteudoUnidadePaginaVO conteudoUnidadePaginaVO : conteudoUnidadePaginaVOs) {
			if ((!conteudoUnidadePaginaVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.PAGINA) && aplicarBackRecursoEducacional) || (conteudoUnidadePaginaVO.getOrigemBackgroundConteudo().equals(origemBase))) {

				conteudoUnidadePaginaVO.setCaminhoBaseBackground(caminhoBase);
				conteudoUnidadePaginaVO.setNomeImagemBackground(nomeArquivo);
				conteudoUnidadePaginaVO.setOrigemBackgroundConteudo(origemUtilizar);
				conteudoUnidadePaginaVO.setTamanhoImagemBackgroundConteudo(tamanhoImagemBackgroundConteudoEnum);
				conteudoUnidadePaginaVO.setCorBackground(cor);
				if (origemBase.equals(OrigemBackgroundConteudoEnum.CONTEUDO) || origemBase.equals(OrigemBackgroundConteudoEnum.UNIDADE)) {
					if (conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().isEmpty() && conteudoUnidadePaginaVO.getCodigo() > 0) {
						conteudoUnidadePaginaVO.setConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(conteudoUnidadePaginaVO.getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.ANTES, NivelMontarDados.BASICO, false, null));
					}
					if (conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().isEmpty() && conteudoUnidadePaginaVO.getCodigo() > 0) {
						conteudoUnidadePaginaVO.setConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(conteudoUnidadePaginaVO.getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.DEPOIS, NivelMontarDados.BASICO, false, null));
					}
					getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarReplicacaoBackgroundParaRecursoEducacional(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(), origemBase, caminhoBase, nomeArquivo, cor, aplicarBackRecursoEducacional, origemUtilizar, tamanhoImagemBackgroundConteudoEnum, gravarAlteracao);

					getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().realizarReplicacaoBackgroundParaRecursoEducacional(conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(), origemBase, caminhoBase, nomeArquivo, cor, aplicarBackRecursoEducacional, origemUtilizar, tamanhoImagemBackgroundConteudoEnum, gravarAlteracao);
				}

				if (gravarAlteracao && conteudoUnidadePaginaVO.getCodigo() > 0) {
					alterarBackground(conteudoUnidadePaginaVO);
				}
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirConteudoUnidadePagina(List<ConteudoUnidadePaginaVO> conteudoUnidadePaginaVOs, UnidadeConteudoVO unidadeConteudoVO, ConteudoVO conteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		for (ConteudoUnidadePaginaVO conteudoUnidadePaginaVO : conteudoUnidadePaginaVOs) {
			conteudoUnidadePaginaVO.setUnidadeConteudo(unidadeConteudoVO);
			persistir(conteudoUnidadePaginaVO, conteudoVO, null, controlarAcesso, usuario, realizandoClonagem);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void removerImagemSlide(final ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, String imagem, UsuarioVO usuarioVO) throws Exception {
		if(Uteis.isAtributoPreenchido(imagem)) {
		try {
			String nomeImagem = imagem.substring(imagem.lastIndexOf("/")+1, imagem.length()).trim();
			int ordem = 0;
			int count = 0;
			StringBuilder nomeFisico = new StringBuilder(); 
			for(String nameImg: conteudoUnidadePaginaVO.getNomeFisicoArquivo().split(",")) {
					if(nameImg.trim().equals(nomeImagem)) {
						ordem = count;							
					}else {
						if(nomeFisico.length()>0) {
							nomeFisico.append(", ");
						}
						nomeFisico.append(nameImg.trim());
					}
					count++;
				}
			String caminhoImagem = "";
			int ordem2 = 0;
			StringBuilder caminho = new StringBuilder(); 
			for(String nameCaminho: conteudoUnidadePaginaVO.getCaminhoBaseRepositorio().split(",")) {
				if(ordem2 != ordem) {
					if(caminho.length()>0) {
						caminho.append(", ");
					}
					caminho.append(nameCaminho.trim());
				}else {
					caminhoImagem = nameCaminho.trim().replace("/", File.separator);
				}
				ordem2++;
			}
			StringBuilder nomeReal = new StringBuilder(); 
			
			for(String nome: conteudoUnidadePaginaVO.getCaminhoBaseRepositorio().split(",")) {
				if(ordem2 != ordem) {
					if(nomeReal.length()>0) {
						nomeReal.append(", ");
					}
					nomeReal.append(nome.trim());
				}
				ordem2++;
			}
			ArquivoHelper.delete(new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getLocalUploadArquivoFixo()+File.separator+caminhoImagem+File.separator+nomeImagem));				
			
			conteudoUnidadePaginaVO.setNomeFisicoArquivo(nomeFisico.toString().trim());				
			conteudoUnidadePaginaVO.setCaminhoBaseRepositorio(caminho.toString().trim());
			conteudoUnidadePaginaVO.setNomeRealArquivo(nomeReal.toString().trim());
							
			conteudoUnidadePaginaVO.setConteudoPaginaApresentar(null);
			conteudoUnidadePaginaVO.setListaImagensSlide(null);
//			conteudoUnidadePaginaVO.getConteudoPaginaApresentar(getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null), "360px");
			if(Uteis.isAtributoPreenchido(conteudoUnidadePaginaVO)) {
			final StringBuilder sql = new StringBuilder("UPDATE ConteudoUnidadePagina SET ");
			sql.append(" nomeFisicoArquivo = ?, caminhoBaseRepositorio = ?, nomeRealArquivo = ? ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());					
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getNomeFisicoArquivo());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getCaminhoBaseRepositorio());
					sqlAlterar.setString(x++, conteudoUnidadePaginaVO.getNomeRealArquivo());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaVO.getCodigo());
					return sqlAlterar;
				}
			}) <= 0) {
				
				
			}
			
			conteudoUnidadePaginaVO.setNovoObj(false);
			}
		} catch (Exception e) {
			throw e;
		}
		}
	}

//	@Override
//	public void preencherDadosConteudoUnidadePaginaSubjectItemsAurea(SubjectsAureaVO subjectsAureaVO,
//			UnidadeConteudoVO unidadeConteudoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario)
//			throws Exception {
//		// TODO Auto-generated method stub
//		
//	}

//	@Override
//	public void preencherDadosConteudoUnidadePaginaCategoriesAurea(CategoriesAureaVO categoriesAureaVO,
//			ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, UnidadeConteudoVO unidadeConteudoVO) throws Exception {
//		// TODO Auto-generated method stub
//		
//	}
}
