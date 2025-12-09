package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.script.ScriptException;

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

import controle.arquitetura.AplicacaoControle;
import negocio.comuns.academico.ConteudoUnidadePaginaGraficoCategoriaVO;
import negocio.comuns.academico.ConteudoUnidadePaginaGraficoPizzaVO;
import negocio.comuns.academico.ConteudoUnidadePaginaGraficoSerieVO;
import negocio.comuns.academico.ConteudoUnidadePaginaGraficoSerieValorVO;
import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.RecursoEducacionalVO;
import negocio.comuns.academico.enumeradores.MomentoApresentacaoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.PublicoAlvoForumEnum;
import negocio.comuns.academico.enumeradores.SituacaoConteudoEnum;
import negocio.comuns.academico.enumeradores.TipoGraficoEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.MinhasNotasPBLVO;
import negocio.comuns.ead.NotaConceitoAvaliacaoPBLVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoOrigemEnum;
import negocio.comuns.ead.enumeradores.TipoRecursoEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisHTML;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
//import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ConteudoUnidadePaginaRecursoEducacionalInterfaceFacade;

@Repository
@Lazy
public class ConteudoUnidadePaginaRecursoEducacional extends ControleAcesso implements ConteudoUnidadePaginaRecursoEducacionalInterfaceFacade {

	/**
     * 
     */
	private static final long serialVersionUID = 2132761360536249458L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		validarDados(conteudoUnidadePaginaRecursoEducacionalVO);
		if (conteudoUnidadePaginaRecursoEducacionalVO.getCodigo() == 0 || conteudoUnidadePaginaRecursoEducacionalVO.isNovoObj()) {
			incluir(conteudoUnidadePaginaRecursoEducacionalVO, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
		} else {
			alterar(conteudoUnidadePaginaRecursoEducacionalVO, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {

		try {
			conteudoUnidadePaginaRecursoEducacionalVO.setDataCadastro(new Date());
			conteudoUnidadePaginaRecursoEducacionalVO.getUsuarioCadastro().setCodigo(usuario.getCodigo());
			conteudoUnidadePaginaRecursoEducacionalVO.getUsuarioCadastro().setNome(usuario.getNome());
			copiarArquivoConteudoUnidadePaginaRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVO, configuracaoGeralSistemaVO);
			copiarArquivoBackgroundConteudoUnidadePaginaRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVO, configuracaoGeralSistemaVO);
			realizarPublicacaoForum(conteudoUnidadePaginaRecursoEducacionalVO, conteudo, usuario);
			realizarPublicacaoListaExercicio(conteudoUnidadePaginaRecursoEducacionalVO, conteudo, usuario, realizandoClonagem);
			realizarPublicacaoAvaliacaoOnline(conteudoUnidadePaginaRecursoEducacionalVO, conteudo, usuario);

			final StringBuilder sql = new StringBuilder("INSERT INTO ConteudoUnidadePaginaRecursoEducacional ");

			sql.append(" (tipoRecursoEducacional, titulo, texto, altura, largura, ");
			sql.append(" caminhoBaseRepositorio, nomeRealArquivo, nomeFisicoArquivo, recursoEducacional, manterRecursoDisponivelPagina,");
			sql.append(" conteudoUnidadePagina, ordemApresentacao, momentoApresentacaoRecursoEducacional, dataCadastro, usuarioCadastro, descricao,  ");
			sql.append(" tituloGrafico, tituloEixoX, tituloEixoY, apresentarLegenda, valorGrafico, tipoGrafico, categoriaGrafico, forum, listaExercicio, ");
			sql.append(" caminhoBaseBackground, nomeImagemBackground, corBackground, tamanhoImagemBackgroundConteudo , origemBackgroundConteudo, ");
			sql.append(" autoAvaliacao ,alunoAvaliaAluno, professorAvaliaAluno, formulaCalculoNotaFinal, utilizarNotaConceito, requerLiberacaoProfessor, ");
			sql.append(" permiteAlunoAvancarConteudoSemLancarNota, faixaMinimaNotaAutoAvaliacao, faixaMaximaNotaAutoAvaliacao, faixaMinimaNotaAlunoAvaliaAluno, ");
			sql.append(" faixaMaximaNotaAlunoAvaliaAluno, faixaMinimaNotaProfessorAvaliaAluno, faixaMaximaNotaProfessorAvaliaAluno, avaliacaoonline)");
			sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			conteudoUnidadePaginaRecursoEducacionalVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().toString());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTitulo());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTexto());
					sqlInserir.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getAltura());
					sqlInserir.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getLargura());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseRepositorio());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getNomeRealArquivo());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getNomeFisicoArquivo());
					if (conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().getCodigo() > 0) {
						sqlInserir.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getManterRecursoDisponivelPagina());
					sqlInserir.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePagina().getCodigo());
					sqlInserir.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getMomentoApresentacaoRecursoEducacional().name());
					sqlInserir.setDate(x++, Uteis.getDataJDBC(conteudoUnidadePaginaRecursoEducacionalVO.getDataCadastro()));
					sqlInserir.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getUsuarioCadastro().getCodigo());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getDescricao());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTituloGrafico());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTituloEixoX());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTituloEixoY());
					sqlInserir.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getApresentarLegenda());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTipoGrafico().name());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCategoriaGrafico());
					if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && conteudoUnidadePaginaRecursoEducacionalVO.getForum().getCodigo() > 0) {
						sqlInserir.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getForum().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO) && conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().getCodigo() > 0) {
						sqlInserir.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCorBackground());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTamanhoImagemBackgroundConteudo().name());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getOrigemBackgroundConteudo().name());
					sqlInserir.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getAutoAvaliacao());
					sqlInserir.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getAlunoAvaliaAluno());
					sqlInserir.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getProfessorAvaliaAluno());
					sqlInserir.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFormulaCalculoNotaFinal());
					sqlInserir.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getUtilizarNotaConceito());
					sqlInserir.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getRequerLiberacaoProfessor());
					sqlInserir.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getPermiteAlunoAvancarConteudoSemLancarNota());
					sqlInserir.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaAutoAvaliacao());
					sqlInserir.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaAutoAvaliacao());
					sqlInserir.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaAlunoAvaliaAluno());
					sqlInserir.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaAlunoAvaliaAluno());
					sqlInserir.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaProfessorAvaliaAluno());
					sqlInserir.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaProfessorAvaliaAluno());
					if (Uteis.isAtributoPreenchido(conteudoUnidadePaginaRecursoEducacionalVO.getAvaliacaoOnlineVO())) {
						sqlInserir.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getAvaliacaoOnlineVO().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						conteudoUnidadePaginaRecursoEducacionalVO.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			incluirRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVO, conteudo.getDisciplina(), controlarAcesso, usuario);
			getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().persistirNotaConceitoAvaliacaoPBLVOS(conteudoUnidadePaginaRecursoEducacionalVO.getNotaConceitoAvaliacaoPBLVOs(), false, usuario);
		} catch (Exception e) {
			conteudoUnidadePaginaRecursoEducacionalVO.setNovoObj(true);
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void realizarPublicacaoAvaliacaoOnline(ConteudoUnidadePaginaRecursoEducacionalVO obj, ConteudoVO conteudo, UsuarioVO usuario) throws Exception {
		if (obj.getTipoRecursoEducacional().isTipoAvaliacaoOnline()) {
			if (!Uteis.isAtributoPreenchido(obj.getAvaliacaoOnlineVO().getCodigo())){
				if(conteudo.getSituacaoConteudo().isAtivo()){
					obj.getAvaliacaoOnlineVO().setSituacao(SituacaoEnum.ATIVO);
				}
				obj.getAvaliacaoOnlineVO().setNome(obj.getTitulo());
				obj.getAvaliacaoOnlineVO().setReposnsavelAtivacao(usuario);
				obj.getAvaliacaoOnlineVO().setDataAtivacao(new Date());
				obj.getAvaliacaoOnlineVO().setConteudoVO(conteudo);
			}
			if(obj.getAvaliacaoOnlineVO().getPoliticaSelecaoQuestaoEnum().equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE)){
				obj.getAvaliacaoOnlineVO().setUnidadeConteudoVO(obj.getConteudoUnidadePagina().getUnidadeConteudo());	
			}
			if (!Uteis.isAtributoPreenchido(obj.getAvaliacaoOnlineVO().getCodigo()) || Uteis.isAtributoPreenchido(obj.getAvaliacaoOnlineVO().getNome())){
				getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().persistir(obj.getAvaliacaoOnlineVO(), false, usuario);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void realizarPublicacaoForum(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConteudoVO conteudo, UsuarioVO usuario) throws Exception {
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM)) {
			if (conteudoUnidadePaginaRecursoEducacionalVO.getForum().isNovoObj()) {
				conteudoUnidadePaginaRecursoEducacionalVO.getForum().getDisciplina().setCodigo(conteudo.getDisciplina().getCodigo());
				conteudoUnidadePaginaRecursoEducacionalVO.getForum().getConteudo().setCodigo(conteudo.getCodigo());
				conteudoUnidadePaginaRecursoEducacionalVO.getForum().setPublicoAlvoForumEnum(PublicoAlvoForumEnum.ALUNO);
			}
			getFacadeFactory().getForumFacade().persistir(conteudoUnidadePaginaRecursoEducacionalVO.getForum(), "Forum", false, usuario);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void realizarPublicacaoListaExercicio(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConteudoVO conteudo, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO) && (conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().isNovoObj() || conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().getConteudoAlterado())) {
			if (realizandoClonagem) {
				getFacadeFactory().getQuestaoConteudoFacade().consultarClonarQuestaoConteudo(conteudo.getCodigo(), conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().getConteudoVO().getCodigo(), usuario);
			}
			conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().getConteudoVO().setCodigo(conteudo.getCodigo());
			conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().setSituacaoListaExercicio(SituacaoListaExercicioEnum.ATIVA);
			getFacadeFactory().getListaExercicioFacade().persistir(conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio(), false, "ListaExercicio", usuario);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void incluirRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, DisciplinaVO disciplina, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		if (conteudoUnidadePaginaRecursoEducacionalVO.getPublicarBibliotecaRecursoEducacional() && !conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && !conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO)) {

			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setTexto(conteudoUnidadePaginaRecursoEducacionalVO.getTexto());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setAltura(conteudoUnidadePaginaRecursoEducacionalVO.getAltura());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setLargura(conteudoUnidadePaginaRecursoEducacionalVO.getLargura());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().getConteudoUnidadePaginaRecursoEducacional().setCodigo(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setCaminhoBaseRepositorio(conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseRepositorio());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setNomeFisicoArquivo(conteudoUnidadePaginaRecursoEducacionalVO.getNomeFisicoArquivo());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setNomeRealArquivo(conteudoUnidadePaginaRecursoEducacionalVO.getNomeRealArquivo());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setTitulo(conteudoUnidadePaginaRecursoEducacionalVO.getTitulo());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setTipoRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setDescricao(conteudoUnidadePaginaRecursoEducacionalVO.getDescricao());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().getDisciplina().setCodigo(disciplina.getCodigo());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().getDisciplina().setNome(disciplina.getNome());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setTituloGrafico(conteudoUnidadePaginaRecursoEducacionalVO.getTituloGrafico());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setTituloEixoX(conteudoUnidadePaginaRecursoEducacionalVO.getTituloEixoX());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setTituloEixoY(conteudoUnidadePaginaRecursoEducacionalVO.getTituloEixoY());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setValorGrafico(conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setTipoGrafico(conteudoUnidadePaginaRecursoEducacionalVO.getTipoGrafico());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setCategoriaGrafico(conteudoUnidadePaginaRecursoEducacionalVO.getCategoriaGrafico());
			conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setApresentarLegenda(conteudoUnidadePaginaRecursoEducacionalVO.getApresentarLegenda());
			getFacadeFactory().getRecursoEducacionalFacade().persistir(conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional(), controlarAcesso, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		try {
			conteudoUnidadePaginaRecursoEducacionalVO.setDataAlteracao(new Date());
			conteudoUnidadePaginaRecursoEducacionalVO.getUsuarioAlteracao().setCodigo(usuario.getCodigo());
			conteudoUnidadePaginaRecursoEducacionalVO.getUsuarioAlteracao().setNome(usuario.getNome());
			copiarArquivoConteudoUnidadePaginaRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVO, configuracaoGeralSistemaVO);
			copiarArquivoBackgroundConteudoUnidadePaginaRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVO, configuracaoGeralSistemaVO);
			realizarPublicacaoForum(conteudoUnidadePaginaRecursoEducacionalVO, conteudo, usuario);
			realizarPublicacaoListaExercicio(conteudoUnidadePaginaRecursoEducacionalVO, conteudo, usuario, realizandoClonagem);
			realizarPublicacaoAvaliacaoOnline(conteudoUnidadePaginaRecursoEducacionalVO, conteudo, usuario);
			final StringBuilder sql = new StringBuilder("UPDATE ConteudoUnidadePaginaRecursoEducacional SET ");
			sql.append(" tipoRecursoEducacional = ? , titulo = ? , texto = ? , altura = ? , largura = ? , ");
			sql.append(" caminhoBaseRepositorio = ? , nomeRealArquivo = ? , nomeFisicoArquivo = ? , recursoEducacional =?, manterRecursoDisponivelPagina = ? ,");
			sql.append(" conteudoUnidadePagina = ?, ordemApresentacao = ?, momentoApresentacaoRecursoEducacional = ? , dataAlteracao = ? , usuarioAlteracao = ?, descricao = ?, ");
			sql.append(" tituloGrafico = ?, tituloEixoX = ?, tituloEixoY = ?, apresentarLegenda = ?, valorGrafico = ?, tipoGrafico = ?, categoriaGrafico=?, forum=?,  listaExercicio=?, ");
			sql.append(" caminhoBaseBackground = ?, nomeImagemBackground = ?, corBackground = ?, tamanhoImagemBackgroundConteudo = ? , origemBackgroundConteudo = ?, ");
			sql.append(" autoAvaliacao = ?, alunoAvaliaAluno = ?, professorAvaliaAluno = ?, formulaCalculoNotaFinal = ?, utilizarNotaConceito = ?,");
			sql.append(" requerLiberacaoProfessor = ?, permiteAlunoAvancarConteudoSemLancarNota = ?, faixaMinimaNotaAutoAvaliacao = ?, faixaMaximaNotaAutoAvaliacao = ?, ");
			sql.append(" faixaMinimaNotaAlunoAvaliaAluno = ?, faixaMaximaNotaAlunoAvaliaAluno = ?, faixaMinimaNotaProfessorAvaliaAluno = ?, faixaMaximaNotaProfessorAvaliaAluno = ?, ");
			sql.append(" avaliacaoonline = ? ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().toString());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTitulo());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTexto());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getAltura());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getLargura());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseRepositorio());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getNomeRealArquivo());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getNomeFisicoArquivo());
					if (conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().getCodigo() > 0) {
						sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getManterRecursoDisponivelPagina());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePagina().getCodigo());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getOrdemApresentacao());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getMomentoApresentacaoRecursoEducacional().name());
					sqlAlterar.setDate(x++, Uteis.getDataJDBC(conteudoUnidadePaginaRecursoEducacionalVO.getDataAlteracao()));
					sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getUsuarioAlteracao().getCodigo());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getDescricao());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTituloGrafico());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTituloEixoX());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTituloEixoY());
					sqlAlterar.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getApresentarLegenda());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTipoGrafico().name());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCategoriaGrafico());
					if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && conteudoUnidadePaginaRecursoEducacionalVO.getForum().getCodigo() > 0) {
						sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getForum().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO) && conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().getCodigo() > 0) {
						sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCorBackground());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTamanhoImagemBackgroundConteudo().name());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getOrigemBackgroundConteudo().name());
					sqlAlterar.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getAutoAvaliacao());
					sqlAlterar.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getAlunoAvaliaAluno());
					sqlAlterar.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getProfessorAvaliaAluno());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFormulaCalculoNotaFinal());
					sqlAlterar.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getUtilizarNotaConceito());
					sqlAlterar.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getRequerLiberacaoProfessor());
					sqlAlterar.setBoolean(x++, conteudoUnidadePaginaRecursoEducacionalVO.getPermiteAlunoAvancarConteudoSemLancarNota());
					sqlAlterar.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaAutoAvaliacao());
					sqlAlterar.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaAutoAvaliacao());
					sqlAlterar.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaAlunoAvaliaAluno());
					sqlAlterar.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaAlunoAvaliaAluno());
					sqlAlterar.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaProfessorAvaliaAluno());
					sqlAlterar.setDouble(x++, conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaProfessorAvaliaAluno());
					if (Uteis.isAtributoPreenchido(conteudoUnidadePaginaRecursoEducacionalVO.getAvaliacaoOnlineVO())) {
						sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getAvaliacaoOnlineVO().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
					return sqlAlterar;
				}
			}) <= 0) {
				incluir(conteudoUnidadePaginaRecursoEducacionalVO, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
				return;
			}
			incluirRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVO, conteudo.getDisciplina(), controlarAcesso, usuario);
			getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().persistirNotaConceitoAvaliacaoPBLVOS(conteudoUnidadePaginaRecursoEducacionalVO.getNotaConceitoAvaliacaoPBLVOs(), false, usuario);
			conteudoUnidadePaginaRecursoEducacionalVO.setNovoObj(false);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarBackground(final ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws Exception {
		try {
			
			final StringBuilder sql = new StringBuilder("UPDATE ConteudoUnidadePaginaRecursoEducacional SET ");
			sql.append(" caminhoBaseBackground = ?, nomeImagemBackground = ?, corBackground = ?, tamanhoImagemBackgroundConteudo = ? , origemBackgroundConteudo = ? ");
			sql.append(" where codigo = ? ");
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());					
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCorBackground());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getTamanhoImagemBackgroundConteudo().name());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getOrigemBackgroundConteudo().name());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
					return sqlAlterar;
				}
			}) <= 0) {
				
				
			}
			
			conteudoUnidadePaginaRecursoEducacionalVO.setNovoObj(false);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void validarDados(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTitulo().trim().isEmpty()) {
			if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
				conteudoUnidadePaginaRecursoEducacionalVO.setTitulo(conteudoUnidadePaginaRecursoEducacionalVO.getTituloGrafico());
			}
			if (conteudoUnidadePaginaRecursoEducacionalVO.getTitulo().trim().isEmpty()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaRecursoEducacional_titulo"));
			}
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional() == null) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_conteudo"));
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getDescricao().trim().isEmpty()) {
			if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoAvaliacaoPbl()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_descricaoOrientacao"));
			}
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTexto().trim().isEmpty()) {
			if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoAtaPbl()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_descricaoOrientacao"));
			} else if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.TEXTO_HTML) || conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.VIDEO_URL)) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_conteudo"));
			}
			
		}
		if(conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoTextoHtml()){
			conteudoUnidadePaginaRecursoEducacionalVO.setTexto(UteisHTML.realizarValidacaoBackgroundConteudo(conteudoUnidadePaginaRecursoEducacionalVO.getTexto()));
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getNomeFisicoArquivo().trim().isEmpty() && conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().getNecessitaUploadArquivo()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_conteudo"));
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO) && conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico().trim().isEmpty()) {
			realizarGeracaoGrafico(conteudoUnidadePaginaRecursoEducacionalVO);
			if (conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico().trim().isEmpty()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_conteudo"));
			}
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && conteudoUnidadePaginaRecursoEducacionalVO.getForum().isNovoObj() && Uteis.retiraTags(conteudoUnidadePaginaRecursoEducacionalVO.getForum().getTema()).trim().isEmpty()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_conteudo"));
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.EXERCICIO) && conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().getConteudoAlterado() && conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().getSituacaoListaExercicio().equals(SituacaoListaExercicioEnum.EM_ELABORACAO)) {
			try {
				getFacadeFactory().getListaExercicioFacade().validarDados(conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio());
			} catch (ConsistirException e) {
				consistirException.getListaMensagemErro().addAll(e.getListaMensagemErro());
			} catch (Exception e) {
				consistirException.adicionarListaMensagemErro(e.getMessage());
			}
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoAvaliacaoPbl()) {
			validarDadosQuandoAvaliacaoPbl(conteudoUnidadePaginaRecursoEducacionalVO, consistirException);
		}
		
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
	}
	
	public void validarDadosQuandoAvaliacaoPbl(ConteudoUnidadePaginaRecursoEducacionalVO obj, ConsistirException consistirException){
		String formula = obj.getFormulaCalculoNotaFinal().replace(ConteudoUnidadePaginaRecursoEducacionalVO.AUTO_AVAL, "2").replace(ConteudoUnidadePaginaRecursoEducacionalVO.ALUNO_AVAL, "3").replace(ConteudoUnidadePaginaRecursoEducacionalVO.PROF_AVAL, "4").replace(ConteudoUnidadePaginaRecursoEducacionalVO.QTDE_ALU, "5");
		try {
			Uteis.realizarCalculoFormulaCalculo(formula);
		} catch (ScriptException e) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_formulaCalculoErrado"));
		}
		if ((obj.getFormulaCalculoNotaFinal().contains(ConteudoUnidadePaginaRecursoEducacionalVO.AUTO_AVAL) && !obj.getAutoAvaliacao())) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaRecursoEducacional_requerAUTO_AVAL"));
		}
		if (obj.getFormulaCalculoNotaFinal().contains(ConteudoUnidadePaginaRecursoEducacionalVO.PROF_AVAL) && !obj.getProfessorAvaliaAluno()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaRecursoEducacional_requerPROF_AVAL"));
		}
		if (obj.getFormulaCalculoNotaFinal().contains(ConteudoUnidadePaginaRecursoEducacionalVO.ALUNO_AVAL) && !obj.getAlunoAvaliaAluno()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaRecursoEducacional_requerALUNO_AVAL"));
		}

		if (obj.getUtilizarNotaConceito()) {
			if (obj.getNotaConceitoAvaliacaoPBLVOs().isEmpty()) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_utilizaNotaConceitoListaVazia"));
			}
			boolean autoAval = false;
			boolean alunoAval = false;
			boolean profAval = false;
			for (NotaConceitoAvaliacaoPBLVO notaConceito : obj.getNotaConceitoAvaliacaoPBLVOs()) {
				if (notaConceito.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.AUTO_AVALIACAO)) {
					autoAval = true;
				}
				if (notaConceito.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO)) {
					alunoAval = true;
				}
				if (notaConceito.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO)) {
					profAval = true;
				}
			}
			if (obj.getAutoAvaliacao() && !autoAval) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_faixaNotaAutoAvaliacaoNotasConceitoVazio"));
			}
			if (obj.getAlunoAvaliaAluno() && !alunoAval) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_faixaNotaAlunoAvaliaAlunoNotasConceitoVazio"));
			}
			if (obj.getProfessorAvaliaAluno() && !profAval) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_faixaNotaProfessorAvaliaAlunoNotasConceitoVazio"));
			}
		} else {
			if (obj.getAutoAvaliacao() && obj.getFaixaMinimaNotaAutoAvaliacao() < 0.0) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_faixaNotaMinimaAutoAvaliacaoNotasVazio"));
			}
			if (obj.getAutoAvaliacao() && obj.getFaixaMaximaNotaAutoAvaliacao() < 0.0) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_faixaNotaMaximaAutoAvaliacaoNotasVazio"));
			}
			if (obj.getAlunoAvaliaAluno() && obj.getFaixaMinimaNotaAlunoAvaliaAluno() < 0.0) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_faixaNotaMinimaAlunoAvaliaAlunoNotasVazio"));
			}
			if (obj.getAlunoAvaliaAluno() && obj.getFaixaMaximaNotaAlunoAvaliaAluno() < 0.0) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_faixaNotaMaximaAlunoAvaliaAlunoNotasVazio"));
			}
			if (obj.getProfessorAvaliaAluno() && obj.getFaixaMinimaNotaProfessorAvaliaAluno() < 0.0) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_faixaNotaMinimaProfessorAvaliaAlunoNotasVazio"));
			}
			if (obj.getProfessorAvaliaAluno() && obj.getFaixaMaximaNotaProfessorAvaliaAluno() < 0.0) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_faixaNotaMaximaProfessorAvaliaAlunoNotasVazio"));
			}
		}
	}

	@Override
	public List<ConteudoUnidadePaginaRecursoEducacionalVO> consultarPorConteudoUnidadePagina(Integer conteudoUnidadePagina, MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacional, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT conteudounidadepaginarecursoeducacional.* ");
		sb.append(" FROM conteudounidadepaginarecursoeducacional ");
		sb.append(" where conteudounidadepaginarecursoeducacional.conteudoUnidadePagina = ").append(conteudoUnidadePagina);
		if (momentoApresentacaoRecursoEducacional != null) {
			sb.append(" and conteudounidadepaginarecursoeducacional.momentoApresentacaoRecursoEducacional = '").append(momentoApresentacaoRecursoEducacional.name()).append("' ");
		}
		sb.append(" order by conteudounidadepaginarecursoeducacional.ordemApresentacao");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, controlarAcesso, usuario);
	}

	@Override
	public ConteudoUnidadePaginaRecursoEducacionalVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT conteudounidadepaginarecursoeducacional.*, ");
		sb.append(" gestaoeventoconteudoturma.situacao as \"gestaoeventoconteudoturma.situacao\" ");
		sb.append(" FROM conteudounidadepaginarecursoeducacional ");
		sb.append(" left join gestaoeventoconteudoturma on gestaoeventoconteudoturma.conteudounidadepaginarecursoeducacional = conteudounidadepaginarecursoeducacional.codigo ");
		sb.append(" where conteudounidadepaginarecursoeducacional.codigo = ").append(codigo);
		sb.append(" order by conteudounidadepaginarecursoeducacional.ordemApresentacao");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, controlarAcesso, usuario);
		}
		return null;
	}

	private List<ConteudoUnidadePaginaRecursoEducacionalVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalVOs = new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>(0);
		while (rs.next()) {
			conteudoUnidadePaginaRecursoEducacionalVOs.add(montarDados(rs, nivelMontarDados, controlarAcesso, usuario));
		}
		return conteudoUnidadePaginaRecursoEducacionalVOs;
	}

	private ConteudoUnidadePaginaRecursoEducacionalVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO = new ConteudoUnidadePaginaRecursoEducacionalVO();
		conteudoUnidadePaginaRecursoEducacionalVO.setCodigo(rs.getInt("codigo"));
		conteudoUnidadePaginaRecursoEducacionalVO.setAltura(rs.getInt("altura"));
		conteudoUnidadePaginaRecursoEducacionalVO.setLargura(rs.getInt("largura"));
		conteudoUnidadePaginaRecursoEducacionalVO.setCaminhoBaseRepositorio(rs.getString("caminhoBaseRepositorio"));
		conteudoUnidadePaginaRecursoEducacionalVO.setDescricao(rs.getString("descricao"));
		conteudoUnidadePaginaRecursoEducacionalVO.setMomentoApresentacaoRecursoEducacional(MomentoApresentacaoRecursoEducacionalEnum.valueOf(rs.getString("momentoApresentacaoRecursoEducacional")));
		conteudoUnidadePaginaRecursoEducacionalVO.setNomeFisicoArquivo(rs.getString("nomeFisicoArquivo"));
		conteudoUnidadePaginaRecursoEducacionalVO.setNomeRealArquivo(rs.getString("nomeRealArquivo"));
		conteudoUnidadePaginaRecursoEducacionalVO.setOrdemApresentacao(rs.getInt("ordemApresentacao"));
		conteudoUnidadePaginaRecursoEducacionalVO.setTexto(rs.getString("texto"));
		conteudoUnidadePaginaRecursoEducacionalVO.setDataAlteracao(rs.getDate("dataAlteracao"));
		conteudoUnidadePaginaRecursoEducacionalVO.setDataCadastro(rs.getDate("dataCadastro"));
		conteudoUnidadePaginaRecursoEducacionalVO.setManterRecursoDisponivelPagina(rs.getBoolean("manterRecursoDisponivelPagina"));
		conteudoUnidadePaginaRecursoEducacionalVO.setTitulo(rs.getString("titulo"));
		conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePagina().setCodigo(rs.getInt("conteudounidadepagina"));
		conteudoUnidadePaginaRecursoEducacionalVO.getUsuarioAlteracao().setCodigo(rs.getInt("usuarioAlteracao"));
		conteudoUnidadePaginaRecursoEducacionalVO.getUsuarioCadastro().setCodigo(rs.getInt("usuarioCadastro"));
		conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().setCodigo(rs.getInt("recursoEducacional"));
		conteudoUnidadePaginaRecursoEducacionalVO.setTipoRecursoEducacional(TipoRecursoEducacionalEnum.valueOf(rs.getString("tipoRecursoEducacional")));
		conteudoUnidadePaginaRecursoEducacionalVO.setRequerLiberacaoProfessor(rs.getBoolean("requerLiberacaoProfessor"));
		if (rs.getString("tipoGrafico") != null && !rs.getString("tipoGrafico").isEmpty()) {
			conteudoUnidadePaginaRecursoEducacionalVO.setTipoGrafico(TipoGraficoEnum.valueOf(rs.getString("tipoGrafico")));
		}
		conteudoUnidadePaginaRecursoEducacionalVO.setTituloGrafico(rs.getString("tituloGrafico"));
		conteudoUnidadePaginaRecursoEducacionalVO.setTituloEixoX(rs.getString("tituloEixoX"));
		conteudoUnidadePaginaRecursoEducacionalVO.setTituloEixoY(rs.getString("tituloEixoY"));
		conteudoUnidadePaginaRecursoEducacionalVO.setValorGrafico(rs.getString("valorGrafico"));
		conteudoUnidadePaginaRecursoEducacionalVO.setCategoriaGrafico(rs.getString("categoriaGrafico"));
		conteudoUnidadePaginaRecursoEducacionalVO.setApresentarLegenda(rs.getBoolean("apresentarLegenda"));
		conteudoUnidadePaginaRecursoEducacionalVO.getAvaliacaoOnlineVO().setCodigo(rs.getInt("avaliacaoonline"));
		conteudoUnidadePaginaRecursoEducacionalVO.getForum().setCodigo(rs.getInt("forum"));
		conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().setCodigo(rs.getInt("listaExercicio"));
		conteudoUnidadePaginaRecursoEducacionalVO.setCaminhoBaseBackground(rs.getString("caminhoBaseBackground"));
		conteudoUnidadePaginaRecursoEducacionalVO.setNomeImagemBackground(rs.getString("nomeImagemBackground"));
		conteudoUnidadePaginaRecursoEducacionalVO.setCorBackground(rs.getString("corBackground"));
		if (rs.getString("tamanhoImagemBackgroundConteudo") != null && !rs.getString("tamanhoImagemBackgroundConteudo").isEmpty()) {
			conteudoUnidadePaginaRecursoEducacionalVO.setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum.valueOf(rs.getString("tamanhoImagemBackgroundConteudo")));
		} else {
			conteudoUnidadePaginaRecursoEducacionalVO.setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO);
		}
		if (rs.getString("origemBackgroundConteudo") != null && !rs.getString("origemBackgroundConteudo").isEmpty()) {
			conteudoUnidadePaginaRecursoEducacionalVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.valueOf(rs.getString("origemBackgroundConteudo")));
		} else {
			conteudoUnidadePaginaRecursoEducacionalVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.SEM_BACKGROUND);
		}
			conteudoUnidadePaginaRecursoEducacionalVO.setAutoAvaliacao(rs.getBoolean("autoAvaliacao"));
			conteudoUnidadePaginaRecursoEducacionalVO.setAlunoAvaliaAluno(rs.getBoolean("alunoAvaliaAluno"));
			conteudoUnidadePaginaRecursoEducacionalVO.setProfessorAvaliaAluno(rs.getBoolean("professorAvaliaAluno"));
			conteudoUnidadePaginaRecursoEducacionalVO.setFormulaCalculoNotaFinal(rs.getString("formulaCalculoNotaFinal"));
			conteudoUnidadePaginaRecursoEducacionalVO.setUtilizarNotaConceito(rs.getBoolean("utilizarNotaConceito"));
			conteudoUnidadePaginaRecursoEducacionalVO.setPermiteAlunoAvancarConteudoSemLancarNota(rs.getBoolean("permiteAlunoAvancarConteudoSemLancarNota"));
			conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMinimaNotaAutoAvaliacao(rs.getDouble("faixaMinimaNotaAutoAvaliacao"));
			conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMaximaNotaAutoAvaliacao(rs.getDouble("faixaMaximaNotaAutoAvaliacao"));
			conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMinimaNotaAlunoAvaliaAluno(rs.getDouble("faixaMinimaNotaAlunoAvaliaAluno"));
			conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMaximaNotaAlunoAvaliaAluno(rs.getDouble("faixaMaximaNotaAlunoAvaliaAluno"));
			conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMinimaNotaProfessorAvaliaAluno(rs.getDouble("faixaMinimaNotaProfessorAvaliaAluno"));
			conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMaximaNotaProfessorAvaliaAluno(rs.getDouble("faixaMaximaNotaProfessorAvaliaAluno"));
			if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.AVALIACAO_PBL)) {
			conteudoUnidadePaginaRecursoEducacionalVO.setNotaConceitoAvaliacaoPBLVOs(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().consultarPorCodigoConteudoUnidadePaginaRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		if (nivelMontarDados.equals(NivelMontarDados.BASICO)) {
			conteudoUnidadePaginaRecursoEducacionalVO.setNovoObj(false);
			return conteudoUnidadePaginaRecursoEducacionalVO;
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoForum()) {
			conteudoUnidadePaginaRecursoEducacionalVO.getForum().setNovoObj(false);
			conteudoUnidadePaginaRecursoEducacionalVO.setForum(getFacadeFactory().getForumFacade().consultarPorChavePrimaria(conteudoUnidadePaginaRecursoEducacionalVO.getForum().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS));
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoExercicio()) {
			conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().setNovoObj(false);
			conteudoUnidadePaginaRecursoEducacionalVO.setListaExercicio(getFacadeFactory().getListaExercicioFacade().consultarPorChavePrimaria(conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio().getCodigo()));
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoAvaliacaoOnline()) {
			conteudoUnidadePaginaRecursoEducacionalVO.getAvaliacaoOnlineVO().setNovoObj(false);
			conteudoUnidadePaginaRecursoEducacionalVO.setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(conteudoUnidadePaginaRecursoEducacionalVO.getAvaliacaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		
		conteudoUnidadePaginaRecursoEducacionalVO.setNovoObj(false);
		return conteudoUnidadePaginaRecursoEducacionalVO;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
			obj.setConteudoUnidadePagina(conteudoUnidadePaginaVO);
			persistir(obj, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
		}
		for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
			obj.setConteudoUnidadePagina(conteudoUnidadePaginaVO);
			persistir(obj, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
		}
		for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor()) {
			obj.setConteudoUnidadePagina(conteudoUnidadePaginaVO);
			persistir(obj, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {

		StringBuilder sb = new StringBuilder("SELECT * FROM conteudoUnidadePaginaRecursoEducacional where conteudoUnidadePagina =  ").append(conteudoUnidadePaginaVO.getCodigo());
		sb.append(" and codigo not in ( 0 ");
		for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
			if (!conteudoUnidadePaginaRecursoEducacionalVO.isNovoObj()) {
				sb.append(", ").append(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
			}
		}
		for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
			if (!conteudoUnidadePaginaRecursoEducacionalVO.isNovoObj()) {
				sb.append(", ").append(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
			}
		}
		for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor()) {
			if (!conteudoUnidadePaginaRecursoEducacionalVO.isNovoObj()) {
				sb.append(", ").append(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
			}
		}
		sb.append(")");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalVOs = montarDadosConsulta(rs, NivelMontarDados.BASICO, false, usuario);
		excluirArquivoConteudoRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVOs, configuracaoGeralSistemaVO);

		sb = new StringBuilder("DELETE FROM conteudoUnidadePaginaRecursoEducacional where conteudoUnidadePagina =  ").append(conteudoUnidadePaginaVO.getCodigo());
		sb.append(" and codigo not in ( 0 ");
		for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
			if (!conteudoUnidadePaginaRecursoEducacionalVO.isNovoObj()) {
				sb.append(", ").append(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
			}
		}
		for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
			if (!conteudoUnidadePaginaRecursoEducacionalVO.isNovoObj()) {
				sb.append(", ").append(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
			}
		}
		for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor()) {
			if (!conteudoUnidadePaginaRecursoEducacionalVO.isNovoObj()) {
				sb.append(", ").append(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
			}
		}
		sb.append(")").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().execute(sb.toString());
		
		for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO : conteudoUnidadePaginaRecursoEducacionalVOs) {
			if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoForum()) {
				getFacadeFactory().getForumFacade().excluir(conteudoUnidadePaginaRecursoEducacionalVO.getForum(), usuario);
			} else if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoAvaliacaoOnline()) {
				getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().excluir(conteudoUnidadePaginaRecursoEducacionalVO.getAvaliacaoOnlineVO(), false, usuario);
			} else if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoExercicio()) {
				getFacadeFactory().getListaExercicioFacade().excluir(conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio(), false, "ListaExercicio", usuario);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void excluir(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalVOs = new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>(0);
		conteudoUnidadePaginaRecursoEducacionalVOs.add(conteudoUnidadePaginaRecursoEducacionalVO);
		excluirArquivoConteudoRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVOs, configuracaoGeralSistemaVO);
		excluir("Conteudo", controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("DELETE FROM conteudoUnidadePaginaRecursoEducacional where codigo =  ").append(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo()).append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().execute(sb.toString());
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoForum()) {
			getFacadeFactory().getForumFacade().excluir(conteudoUnidadePaginaRecursoEducacionalVO.getForum(), usuario);
		} else if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoAvaliacaoOnline()) {
//			getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().excluirCalendarioTipoAvaliacaoOnlineReaPorConteudoUnidadePaginaRecursoEducacional(conteudoUnidadePaginaRecursoEducacionalVO, false, usuario);
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().excluir(conteudoUnidadePaginaRecursoEducacionalVO.getAvaliacaoOnlineVO(), false, usuario);
		} else if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().isTipoRecursoExercicio()) {
			getFacadeFactory().getListaExercicioFacade().excluir(conteudoUnidadePaginaRecursoEducacionalVO.getListaExercicio(), false, "ListaExercicio", usuario);
		}
	}

	@Override
	public void excluirArquivoConteudoRecursoEducacional(List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		for (ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO : conteudoUnidadePaginaRecursoEducacionalVOs) {
			String arquivo = "";
			if (conteudoUnidadePaginaRecursoEducacionalVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.RECURSO_EDUCACIONAL) && !conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground().trim().isEmpty()) {
				if (conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground().contains("_TMP")) {
					arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp();
				} else {
					arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
				}
				if (!getFacadeFactory().getConteudoFacade().validarUsoDaImagemBackgroundNoConteudo(conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground(), null)) {
					arquivo += (File.separator + conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground() + File.separator + conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground());
					ArquivoHelper.delete(new File(arquivo));
				}
			}
			if (!conteudoUnidadePaginaRecursoEducacionalVO.getPublicarBibliotecaRecursoEducacional() && conteudoUnidadePaginaRecursoEducacionalVO.getRecursoEducacional().getCodigo() == 0) {
				if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM)) {
					String[] nomeArquivos = conteudoUnidadePaginaRecursoEducacionalVO.getNomeFisicoArquivo().split(",");
					String[] caminhosArquivos = conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseRepositorio().split(",");
					int x = 0;
					for (String nomeArquivo : nomeArquivos) {
						if (caminhosArquivos[x].contains("_TMP")) {
							arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp();
						} else {
							arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
						}
						arquivo += (File.separator + caminhosArquivos[x] + File.separator + nomeArquivo);
						ArquivoHelper.delete(new File(arquivo));
						x++;
					}
				} else {
					if (conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseRepositorio().contains("_TMP")) {
						arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp();
					} else {
						arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
					}
					if (!getFacadeFactory().getConteudoFacade().validarUsoArquivoConteudo(conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseRepositorio(), conteudoUnidadePaginaRecursoEducacionalVO.getNomeFisicoArquivo())) {
						arquivo += (File.separator + conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseRepositorio() + File.separator + conteudoUnidadePaginaRecursoEducacionalVO.getNomeFisicoArquivo());
						ArquivoHelper.delete(new File(arquivo));
					}
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		excluirConteudoUnidadePaginaRecursoEducacional(conteudoUnidadePaginaVO, configuracaoGeralSistemaVO, controlarAcesso, usuario);
		for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
			obj.setConteudoUnidadePagina(conteudoUnidadePaginaVO);
			persistir(obj, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
		}
		for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
			obj.setConteudoUnidadePagina(conteudoUnidadePaginaVO);
			persistir(obj, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
		}
		for (ConteudoUnidadePaginaRecursoEducacionalVO obj : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor()) {
			obj.setConteudoUnidadePagina(conteudoUnidadePaginaVO);
			persistir(obj, conteudo, configuracaoGeralSistemaVO, controlarAcesso, usuario, realizandoClonagem);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void upLoadArquivoConteudoUnidadePaginaRecursoEducacionalHtml(FileUploadEvent upload, Integer disciplina, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional, Boolean publicarImagem, String nomeImagem, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		if (publicarImagem && nomeImagem.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_RecursoEducacional_titulo"));
		}
		String caminhoBase = PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator + conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional().name();
		String nomeFisico = String.valueOf(new Date().getTime()) + upload.getFile().getFileName().substring(upload.getFile().getFileName().lastIndexOf("."), upload.getFile().getFileName().length());
		ArquivoHelper.salvarArquivoRecursoEducacionalNaPastaTemp(upload, conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional(), disciplina, nomeFisico, configuracaoGeralSistemaVO, usuarioVO);
		ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(caminhoBase, nomeFisico, configuracaoGeralSistemaVO);
		caminhoBase = PastaBaseArquivoEnum.EAD.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO.getValue() + File.separator + disciplina + File.separator + conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional().name();
		StringBuilder incorporar = new StringBuilder(" ");
		incorporar.append("<img style=\"vertical-align:middle;max-height:200px; text-align:center;\" src=\"" + configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo());
		incorporar.append("/" + caminhoBase.replaceAll("\\\\", "/"));
		incorporar.append("/" + nomeFisico + "?UID=" + Calendar.getInstance().getTime() + "\"/>");
		conteudoUnidadePaginaRecursoEducacional.setTexto(conteudoUnidadePaginaRecursoEducacional.getTexto().replaceAll("</body>", "</br>" + incorporar.toString() + "</body>"));

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
	public void upLoadArquivoConteudoUnidadePaginaRecursoEducacional(FileUploadEvent upload, Integer disciplina, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		try {
			conteudoUnidadePaginaRecursoEducacional.setConteudoPaginaApresentar(null);
			conteudoUnidadePaginaRecursoEducacional.setListaImagensSlide(null);
			if (!conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM)) {
				conteudoUnidadePaginaRecursoEducacional.setNomeRealArquivo(upload.getFile().getFileName());
				conteudoUnidadePaginaRecursoEducacional.setIcone(null);
				conteudoUnidadePaginaRecursoEducacional.setCaminhoBaseRepositorio(PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator + conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional().name());
				if (conteudoUnidadePaginaRecursoEducacional.getNomeFisicoArquivo().isEmpty()) {
					conteudoUnidadePaginaRecursoEducacional.setNomeFisicoArquivo(String.valueOf(new Date().getTime()) + upload.getFile().getFileName().substring(upload.getFile().getFileName().lastIndexOf("."), upload.getFile().getFileName().length()));
				} else {
					if (conteudoUnidadePaginaRecursoEducacional.getRecursoEducacional().getCodigo() == 0 && conteudoUnidadePaginaRecursoEducacional.getCaminhoBaseRepositorio().contains("_TMP")) {
						File arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + "/" + PastaBaseArquivoEnum.EAD_TMP.getValue() + "/" + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + "/" + conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional().name() + "/" + conteudoUnidadePaginaRecursoEducacional.getNomeFisicoArquivo());
						arquivo.delete();
						arquivo = null;
					}
					
					conteudoUnidadePaginaRecursoEducacional.setNomeFisicoArquivo(conteudoUnidadePaginaRecursoEducacional.getNomeFisicoArquivo().substring(0, conteudoUnidadePaginaRecursoEducacional.getNomeFisicoArquivo().indexOf(".")) + upload.getFile().getFileName().substring(upload.getFile().getFileName().lastIndexOf("."), upload.getFile().getFileName().length()));
				}
				conteudoUnidadePaginaRecursoEducacional.setRecursoEducacional(null);
				ArquivoHelper.salvarArquivoRecursoEducacionalNaPastaTemp(upload, conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional(), disciplina, conteudoUnidadePaginaRecursoEducacional.getNomeFisicoArquivo(), configuracaoGeralSistemaVO, usuarioVO);
			} else {
				String nomeFisico = "";

				if (!conteudoUnidadePaginaRecursoEducacional.getNomeRealArquivo().contains(upload.getFile().getFileName())) {

					nomeFisico = String.valueOf(new Date().getTime()) + upload.getFile().getFileName().substring(upload.getFile().getFileName().lastIndexOf("."), upload.getFile().getFileName().length());
					if (!conteudoUnidadePaginaRecursoEducacional.getNomeFisicoArquivo().trim().isEmpty()) {
						conteudoUnidadePaginaRecursoEducacional.setCaminhoBaseRepositorio(conteudoUnidadePaginaRecursoEducacional.getCaminhoBaseRepositorio() + ", " + PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator + conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional().name());
						conteudoUnidadePaginaRecursoEducacional.setNomeFisicoArquivo(conteudoUnidadePaginaRecursoEducacional.getNomeFisicoArquivo() + ", " + nomeFisico);
						conteudoUnidadePaginaRecursoEducacional.setNomeRealArquivo(conteudoUnidadePaginaRecursoEducacional.getNomeRealArquivo() + ", " + upload.getFile().getFileName());
					} else {
						conteudoUnidadePaginaRecursoEducacional.setCaminhoBaseRepositorio(PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator + conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional().name());
						conteudoUnidadePaginaRecursoEducacional.setNomeFisicoArquivo(nomeFisico);
						conteudoUnidadePaginaRecursoEducacional.setNomeRealArquivo(upload.getFile().getFileName());
					}
					ArquivoHelper.salvarArquivoRecursoEducacionalNaPastaTemp(upload, conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional(), disciplina, nomeFisico, configuracaoGeralSistemaVO, usuarioVO);
				}
			}
//			conteudoUnidadePaginaRecursoEducacional.getConteudoPaginaApresentar(configuracaoGeralSistemaVO, 360);
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void copiarArquivoConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			if ((conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional().getNecessitaUploadArquivo()) && conteudoUnidadePaginaRecursoEducacional.getCaminhoBaseRepositorio().contains("_TMP")) {
				conteudoUnidadePaginaRecursoEducacional.setRecursoEducacional(null);
				if (conteudoUnidadePaginaRecursoEducacional.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.SLIDE_IMAGEM) && conteudoUnidadePaginaRecursoEducacional.getNomeFisicoArquivo().contains(",")) {
					String[] nomeArquivos = conteudoUnidadePaginaRecursoEducacional.getNomeFisicoArquivo().split(",");
					String[] caminhosArquivos = conteudoUnidadePaginaRecursoEducacional.getCaminhoBaseRepositorio().split(",");
					int x = 0;
					for (String nomeArquivo : nomeArquivos) {
						if (caminhosArquivos[x].contains("_TMP")) {
							ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(caminhosArquivos[x].trim(), nomeArquivo.trim(), configuracaoGeralSistemaVO);
						}
						x++;
					}
				} else {
					ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(conteudoUnidadePaginaRecursoEducacional.getCaminhoBaseRepositorio(), conteudoUnidadePaginaRecursoEducacional.getNomeFisicoArquivo(), configuracaoGeralSistemaVO);
				}
				conteudoUnidadePaginaRecursoEducacional.setCaminhoBaseRepositorio(conteudoUnidadePaginaRecursoEducacional.getCaminhoBaseRepositorio().replaceAll("_TMP", ""));
			}
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void copiarArquivoBackgroundConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			if (conteudoUnidadePaginaRecursoEducacional.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.RECURSO_EDUCACIONAL) && conteudoUnidadePaginaRecursoEducacional.getCaminhoBaseBackground().contains("_TMP")) {
				ArquivoHelper.copiarArquivoDaPastaTempParaPastaFixa(conteudoUnidadePaginaRecursoEducacional.getCaminhoBaseBackground(), conteudoUnidadePaginaRecursoEducacional.getNomeImagemBackground(), configuracaoGeralSistemaVO);
				conteudoUnidadePaginaRecursoEducacional.setCaminhoBaseBackground(conteudoUnidadePaginaRecursoEducacional.getCaminhoBaseBackground().replaceAll("_TMP", ""));
			}
			if (conteudoUnidadePaginaRecursoEducacional.getExcluirImagemBackground() && conteudoUnidadePaginaRecursoEducacional.getCodigo() > 0) {
				ConteudoUnidadePaginaRecursoEducacionalVO objAnt = consultarPorChavePrimaria(conteudoUnidadePaginaRecursoEducacional.getCodigo(), NivelMontarDados.BASICO, false, null);
				if (objAnt != null && objAnt.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.RECURSO_EDUCACIONAL) && !objAnt.getCaminhoBaseBackground().trim().isEmpty() && !objAnt.getNomeImagemBackground().trim().isEmpty()) {
					String caminho = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + objAnt.getCaminhoBaseBackground() + File.separator + objAnt.getNomeImagemBackground();
					ArquivoHelper.delete(new File(caminho));
				}
				conteudoUnidadePaginaRecursoEducacional.setExcluirImagemBackground(false);
			}
		} catch (Exception e) {
			throw e;
		} finally {

		}
	}

	@Override
	public void adicionarSerieGrafico(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, String serie, Double valor) throws Exception {
		if (serie == null || serie.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_serie"));
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoGrafico().equals(TipoGraficoEnum.PIZZA) && (valor == null || valor == 0.0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_valor"));
		}
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoGrafico().equals(TipoGraficoEnum.PIZZA)) {
			for (ConteudoUnidadePaginaGraficoPizzaVO conteudoUnidadePaginaGraficoVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoVOs()) {
				if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoVO.getSerie().trim().toUpperCase()).equals(Uteis.removerAcentuacao(serie.trim().toUpperCase()))) {
					throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_serie_adicionada"));
				}
			}
			conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoVOs().add(new ConteudoUnidadePaginaGraficoPizzaVO(serie, valor));
		} else {
			for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs()) {
				if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoSerieVO.getSerie().trim().toUpperCase()).equals(Uteis.removerAcentuacao(serie.trim().toUpperCase()))) {
					throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_serie_adicionada"));
				}
			}
			List<ConteudoUnidadePaginaGraficoSerieValorVO> conteudoUnidadePaginaGraficoSerieValorVOs = new ArrayList<ConteudoUnidadePaginaGraficoSerieValorVO>(0);
			for (int x = 1; x <= conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoCategoriaVOs().size(); x++) {
				conteudoUnidadePaginaGraficoSerieValorVOs.add(new ConteudoUnidadePaginaGraficoSerieValorVO(x, 0.0));
			}
			conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs().add(new ConteudoUnidadePaginaGraficoSerieVO(conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs().size() + 1, serie, conteudoUnidadePaginaGraficoSerieValorVOs));
		}
	}

	@Override
	public void adicionarCategoriaGrafico(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, String categoria) throws Exception {
		if (categoria == null || categoria.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_categoria"));
		}
		for (ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoCategoriaVOs()) {
			if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoCategoriaVO.getCategoria().trim().toUpperCase()).equals(Uteis.removerAcentuacao(categoria.trim().toUpperCase()))) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePaginaGrafico_categoria_adicionada"));
			}
		}
		for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs()) {
			conteudoUnidadePaginaGraficoSerieVO.getConteudoUnidadePaginaGraficoSerieValorVOs().add(new ConteudoUnidadePaginaGraficoSerieValorVO(conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoCategoriaVOs().size() + 1, 0.0));
		}
		conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoCategoriaVOs().add(new ConteudoUnidadePaginaGraficoCategoriaVO(conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoCategoriaVOs().size() + 1, categoria));

	}

	@Override
	public void removerCategoriaGrafico(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConteudoUnidadePaginaGraficoCategoriaVO categoria) throws Exception {
		int index = 0;
		for (ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoCategoriaVOs()) {
			if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoCategoriaVO.getCategoria().trim().toUpperCase()).equals(Uteis.removerAcentuacao(categoria.getCategoria().trim().toUpperCase()))) {
				conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoCategoriaVOs().remove(index);
				for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs()) {
					conteudoUnidadePaginaGraficoSerieVO.getConteudoUnidadePaginaGraficoSerieValorVOs().removeIf(item -> item.getSequencia().equals(categoria.getSequencia()));					
				}
				conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs().removeIf(item -> item.getConteudoUnidadePaginaGraficoSerieValorVOs().isEmpty());
				return;
			}
			index++;
		}
	}

	@Override
	public void removerSerieGrafico(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, String serie) throws Exception {
		int index = 0;
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoGrafico().equals(TipoGraficoEnum.PIZZA)) {
			for (ConteudoUnidadePaginaGraficoPizzaVO conteudoUnidadePaginaGraficoVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoVOs()) {
				if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoVO.getSerie().trim().toUpperCase()).equals(Uteis.removerAcentuacao(serie.trim().toUpperCase()))) {
					conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoVOs().remove(index);
					return;
				}
				index++;
			}

		} else {
			for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs()) {
				if (Uteis.removerAcentuacao(conteudoUnidadePaginaGraficoSerieVO.getSerie().trim().toUpperCase()).equals(Uteis.removerAcentuacao(serie.trim().toUpperCase()))) {
					conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs().remove(index);
					return;
				}
				index++;
			}

		}
	}

	@Override
	public void realizarGeracaoGrafico(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) {
		StringBuilder valorGrafico = new StringBuilder("");
		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoGrafico().equals(TipoGraficoEnum.PIZZA)) {
			for (ConteudoUnidadePaginaGraficoPizzaVO conteudoUnidadePaginaGraficoVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoVOs()) {
				if (!valorGrafico.toString().isEmpty()) {
					valorGrafico.append(",");
				}
				valorGrafico.append("{nome:'").append(conteudoUnidadePaginaGraficoVO.getSerie()).append("', y:").append(conteudoUnidadePaginaGraficoVO.getValor()).append(", valor:'").append(Uteis.getDoubleFormatado(conteudoUnidadePaginaGraficoVO.getValor())).append("'}");;
			}
		} else {
			StringBuilder categoriaGrafico = new StringBuilder("");
			for (ConteudoUnidadePaginaGraficoCategoriaVO conteudoUnidadePaginaGraficoCategoriaVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoCategoriaVOs()) {
				if (!categoriaGrafico.toString().isEmpty()) {
					categoriaGrafico.append(",");
				}
				categoriaGrafico.append("'").append(conteudoUnidadePaginaGraficoCategoriaVO.getCategoria()).append("'");
			}
			conteudoUnidadePaginaRecursoEducacionalVO.setCategoriaGrafico(categoriaGrafico.toString());
			for (ConteudoUnidadePaginaGraficoSerieVO conteudoUnidadePaginaGraficoSerieVO : conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs()) {
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
		conteudoUnidadePaginaRecursoEducacionalVO.setValorGrafico(valorGrafico.toString());
	}

	@Override
	public void realizarGeracaoConteudoUnidadePaginaGraficoVO(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) throws Exception {

		conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoCategoriaVOs().clear();
		conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs().clear();
		conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoVOs().clear();

		if (conteudoUnidadePaginaRecursoEducacionalVO.getTipoGrafico().equals(TipoGraficoEnum.PIZZA)) {
			List<String> series = new ArrayList<String>(0);
			if (conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico().contains(",[")) {
				String valor = conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico();
				while (valor.indexOf("[") >= 0) {
					valor = valor.substring(valor.indexOf("["), valor.length());
					series.add(valor.substring(0, valor.indexOf("]") + 1));
					valor = valor.substring(valor.indexOf("]") + 1, valor.length());
				}
			} else {
				series.add(conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico());
			}
			for (String serie : series) {
				if (serie == null || serie.trim().isEmpty()) {
					break;
				}
				serie = serie.replace("[", "").replace("]", "");
				adicionarSerieGrafico(conteudoUnidadePaginaRecursoEducacionalVO, serie.substring(0, serie.indexOf(",")).trim().replaceAll("'", ""), Double.parseDouble(serie.substring(serie.indexOf(",") + 1).trim()));
			}

		} else {
			String[] categorias = null;
			if (conteudoUnidadePaginaRecursoEducacionalVO.getCategoriaGrafico().contains(",")) {
				categorias = conteudoUnidadePaginaRecursoEducacionalVO.getCategoriaGrafico().split(",");
			} else {
				categorias = new String[0];
				categorias[0] = conteudoUnidadePaginaRecursoEducacionalVO.getCategoriaGrafico();
			}
			for (String categoria : categorias) {
				adicionarCategoriaGrafico(conteudoUnidadePaginaRecursoEducacionalVO, categoria.replaceAll("'", "").trim());
			}

			List<String> series = new ArrayList<String>(0);
			if (conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico().contains(",{")) {
				String valor = conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico();
				while (valor.indexOf("{") >= 0) {
					valor = valor.substring(valor.indexOf("{"), valor.length());
					series.add(valor.substring(0, valor.indexOf("}") + 1));
					valor = valor.substring(valor.indexOf("}") + 1, valor.length());
				}
			} else {
				series.add(conteudoUnidadePaginaRecursoEducacionalVO.getValorGrafico());
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
					valores = new String[0];
					valores[0] = serie;
				}
				int z = 1;
				for (String valor : valores) {
					conteudoUnidadePaginaGraficoSerieVO.getConteudoUnidadePaginaGraficoSerieValorVOs().add(new ConteudoUnidadePaginaGraficoSerieValorVO(z++, Double.parseDouble(valor.trim())));
				}
				conteudoUnidadePaginaRecursoEducacionalVO.getConteudoUnidadePaginaGraficoSerieVOs().add(conteudoUnidadePaginaGraficoSerieVO);
			}

		}

	}

	@Override
	public void uploadImagemBackgroundConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, Integer disciplina, FileUploadEvent uploadEvent, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {

		String arquivo = "";
		if (conteudoUnidadePaginaRecursoEducacionalVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.RECURSO_EDUCACIONAL) && !conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground().trim().isEmpty()) {
			if (conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground().contains("_TMP") ||conteudoUnidadePaginaRecursoEducacionalVO.getCodigo()>0 ) {
				if(conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground().contains("_TMP")){
					arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp();
				}else{
					arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo();
				}
				arquivo += File.separator + conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground() + File.separator + conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground();
				ArquivoHelper.delete(new File(arquivo));
				conteudoUnidadePaginaRecursoEducacionalVO.setExcluirImagemBackground(false);
			} else {
				conteudoUnidadePaginaRecursoEducacionalVO.setExcluirImagemBackground(true);
			}
		}
		String extensao = uploadEvent.getFile().getFileName().substring(uploadEvent.getFile().getFileName().lastIndexOf("."), uploadEvent.getFile().getFileName().length());
		conteudoUnidadePaginaRecursoEducacionalVO.setNomeImagemBackground(usuarioVO.getCodigo() + "_" + (new Date().getTime()) + extensao);
		conteudoUnidadePaginaRecursoEducacionalVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.RECURSO_EDUCACIONAL);
		if(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo()>0){
			conteudoUnidadePaginaRecursoEducacionalVO.setCaminhoBaseBackground(PastaBaseArquivoEnum.EAD.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO.getValue() + File.separator + disciplina + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_BACKGROUND.getValue());
		}else{
			conteudoUnidadePaginaRecursoEducacionalVO.setCaminhoBaseBackground(PastaBaseArquivoEnum.EAD_TMP.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_TMP.getValue() + File.separator + disciplina + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_BACKGROUND_TMP.getValue());
		}
		arquivo = conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground() + File.separator + conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground();
		ArquivoHelper.salvarArquivoNaPastaTemp(uploadEvent, conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground(), conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground(), configuracaoGeralSistemaVO, usuarioVO);
		
		if(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo()>0){
			alterarBackground(conteudoUnidadePaginaRecursoEducacionalVO);
		}
	}

	@Override
	public void removerImagemBackgroundConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception{
		if (conteudoUnidadePaginaRecursoEducacionalVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.RECURSO_EDUCACIONAL)) {
			if (!conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground().trim().isEmpty() 
					&& !getFacadeFactory().getConteudoFacade().validarUsoDaImagemBackgroundNoConteudo(conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground(), null)
					&& (conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground().contains("_TMP") || conteudoUnidadePaginaRecursoEducacionalVO.getCodigo() > 0)) {
				String arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseBackground() + File.separator + conteudoUnidadePaginaRecursoEducacionalVO.getNomeImagemBackground();
				ArquivoHelper.delete(new File(arquivo));
				conteudoUnidadePaginaRecursoEducacionalVO.setExcluirImagemBackground(false);
			} else {
				conteudoUnidadePaginaRecursoEducacionalVO.setExcluirImagemBackground(true);
			}
		} else {
			conteudoUnidadePaginaRecursoEducacionalVO.setExcluirImagemBackground(false);
		}
		conteudoUnidadePaginaRecursoEducacionalVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.SEM_BACKGROUND);
		conteudoUnidadePaginaRecursoEducacionalVO.setCaminhoBaseBackground("");
		conteudoUnidadePaginaRecursoEducacionalVO.setNomeImagemBackground("");
		if(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo()>0){
			alterarBackground(conteudoUnidadePaginaRecursoEducacionalVO);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarReplicacaoBackgroundParaRecursoEducacional(List<ConteudoUnidadePaginaRecursoEducacionalVO> conteudoUnidadePaginaRecursoEducacionalVOs, 
			OrigemBackgroundConteudoEnum origemBase, String caminhoBase, String nomeArquivo, String cor, Boolean aplicarBackRecursoEducacional, 
			OrigemBackgroundConteudoEnum origemUtilizar, TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudoEnum,  Boolean gravarAlteracao) throws Exception {
		for (ConteudoUnidadePaginaRecursoEducacionalVO recurso : conteudoUnidadePaginaRecursoEducacionalVOs) {
			if ((!recurso.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.RECURSO_EDUCACIONAL) && aplicarBackRecursoEducacional)
					|| (recurso.getOrigemBackgroundConteudo().equals(origemBase))) {
				
					recurso.setCaminhoBaseBackground(caminhoBase);
					recurso.setNomeImagemBackground(nomeArquivo);
					recurso.setOrigemBackgroundConteudo(origemUtilizar);
					recurso.setTamanhoImagemBackgroundConteudo(tamanhoImagemBackgroundConteudoEnum);
					recurso.setCorBackground(cor);					
					
				if(gravarAlteracao && recurso.getCodigo() > 0){
					alterarBackground(recurso);
				}
			} 
		}
		
	}
	
	@Override
	public void adicionarNotaConceito(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO) throws ConsistirException {
		getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().validarDados(notaConceitoAvaliacaoPBLVO);
		ConsistirException ce = new ConsistirException();
		for (NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO2 : conteudoUnidadePaginaRecursoEducacionalVO.getNotaConceitoAvaliacaoPBLVOs()) {
			if (notaConceitoAvaliacaoPBLVO2.getTipoAvaliacao().equals(notaConceitoAvaliacaoPBLVO.getTipoAvaliacao())) {
				if (notaConceitoAvaliacaoPBLVO2.getTipoNotaConceito().equals(notaConceitoAvaliacaoPBLVO.getTipoNotaConceito())) {
					if (notaConceitoAvaliacaoPBLVO2.getConceito().equals(notaConceitoAvaliacaoPBLVO.getConceito())) {
						ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_conceitoJaExiste"));
						break;
					}
					if (notaConceitoAvaliacaoPBLVO.getNotaCorrespondente() <= notaConceitoAvaliacaoPBLVO2.getNotaCorrespondente()) {
						if (notaConceitoAvaliacaoPBLVO2.getNotaCorrespondente().equals(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente())) {
							ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_NotaConceitoAvaliacaoPBL_notaCorrespondenteJaExiste"));
							break;
						}
					}
				}
			}
		}
		if (!ce.getListaMensagemErro().isEmpty()) {
			throw ce;
		}
		// if(notaConceitoAvaliacaoPBLVO.getTipoNotaConceito().equals("AUTO_AVAL"))
		// {
		// notaConceitoAvaliacaoPBLVO.setTipoAvaliacao(TipoAvaliacaoPBLEnum.AUTO_AVALIACAO);
		// } else if
		// (notaConceitoAvaliacaoPBLVO.getTipoNotaConceito().equals("ALUNO_AVAL"))
		// {
		// notaConceitoAvaliacaoPBLVO.setTipoAvaliacao(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO);
		// } else if
		// (notaConceitoAvaliacaoPBLVO.getTipoNotaConceito().equals("PROF_AVAL"))
		// {
		// notaConceitoAvaliacaoPBLVO.setTipoAvaliacao(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO);
		// }
		notaConceitoAvaliacaoPBLVO.setConteudoUnidadePaginaRecursoEducacionalVO(conteudoUnidadePaginaRecursoEducacionalVO);
		conteudoUnidadePaginaRecursoEducacionalVO.getNotaConceitoAvaliacaoPBLVOs().add(notaConceitoAvaliacaoPBLVO);
		atribuirFaixaNotaMinimaMaximaAvaliacaoPBL(conteudoUnidadePaginaRecursoEducacionalVO, notaConceitoAvaliacaoPBLVO);
		if(notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO)) {
			conteudoUnidadePaginaRecursoEducacionalVO.setListaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno(null);
		}else if(notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.AUTO_AVALIACAO)) {
			conteudoUnidadePaginaRecursoEducacionalVO.setListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao(null);
		}else if(notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO)) {
			conteudoUnidadePaginaRecursoEducacionalVO.setListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno(null);
		}
	}
	
	@Override
	public void removerNotaConceito(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO, UsuarioVO usuarioVO) throws ConsistirException {
		conteudoUnidadePaginaRecursoEducacionalVO.getNotaConceitoAvaliacaoPBLVOs().remove(notaConceitoAvaliacaoPBLVO);
		if (!notaConceitoAvaliacaoPBLVO.getCodigo().equals(0)) {
			try {
				getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().excluir(notaConceitoAvaliacaoPBLVO, false, usuarioVO);
			} catch (Exception e) {
				ConsistirException ce = new ConsistirException();
				ce.adicionarListaMensagemErro(e.getMessage());
				throw ce;
			}
		}
		if(notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO)) {
			conteudoUnidadePaginaRecursoEducacionalVO.setListaSelectItemNotaConceitoAvaliacaoPblAlunoAvaliaAluno(null);
		}else if(notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.AUTO_AVALIACAO)) {
			conteudoUnidadePaginaRecursoEducacionalVO.setListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao(null);
		}else if(notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO)) {
			conteudoUnidadePaginaRecursoEducacionalVO.setListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno(null);
		}
	}
	
	public void atribuirFaixaNotaMinimaMaximaAvaliacaoPBL(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO) throws ConsistirException {
		if(notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.AUTO_AVALIACAO)) {
			if (conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaAutoAvaliacao().equals(0.0) && conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaAutoAvaliacao().equals(0.0)) {
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMinimaNotaAutoAvaliacao(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMaximaNotaAutoAvaliacao(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
			}
			if (notaConceitoAvaliacaoPBLVO.getNotaCorrespondente() < conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaAutoAvaliacao()) {
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMinimaNotaAutoAvaliacao(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
			}
			if (notaConceitoAvaliacaoPBLVO.getNotaCorrespondente() > conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaAutoAvaliacao()) {
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMaximaNotaAutoAvaliacao(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
			}
		} else if(notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO)) {
			if (conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaAlunoAvaliaAluno().equals(0.0) && conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaAlunoAvaliaAluno().equals(0.0)) {
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMinimaNotaAlunoAvaliaAluno(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMaximaNotaAlunoAvaliaAluno(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
			}
			if (notaConceitoAvaliacaoPBLVO.getNotaCorrespondente() < conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaAlunoAvaliaAluno()) {
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMinimaNotaAlunoAvaliaAluno(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
			}
			if (notaConceitoAvaliacaoPBLVO.getNotaCorrespondente() > conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaAlunoAvaliaAluno()) {
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMaximaNotaAlunoAvaliaAluno(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
			}
		} else if(notaConceitoAvaliacaoPBLVO.getTipoAvaliacao().equals(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO)) {
			if (conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaProfessorAvaliaAluno().equals(0.0) && conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaProfessorAvaliaAluno().equals(0.0)) {
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMinimaNotaProfessorAvaliaAluno(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMaximaNotaProfessorAvaliaAluno(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
			}
			if (notaConceitoAvaliacaoPBLVO.getNotaCorrespondente() < conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMinimaNotaProfessorAvaliaAluno()) {
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMinimaNotaProfessorAvaliaAluno(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
			}
			if (notaConceitoAvaliacaoPBLVO.getNotaCorrespondente() > conteudoUnidadePaginaRecursoEducacionalVO.getFaixaMaximaNotaProfessorAvaliaAluno()) {
				conteudoUnidadePaginaRecursoEducacionalVO.setFaixaMaximaNotaProfessorAvaliaAluno(notaConceitoAvaliacaoPBLVO.getNotaCorrespondente());
			}
		}
	}	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCalculoNotaFinal(ConteudoUnidadePaginaRecursoEducacionalVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao : obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			realizarCalculoNotaFinalAlunoAvaliacaoPBL(obj, avaliacao, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCalculoNotaFinalAlunoAvaliacaoPBL(ConteudoUnidadePaginaRecursoEducacionalVO obj, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		Double autoAvaliacao = null;
		Double alunoAvaliaAluno = null;
		Double totalAlunoAvaliaAluno = null;
		Double profAvaliaAluno = null;

		if (obj.getAutoAvaliacao()) {
			autoAvaliacao = getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(obj, avaliacao);
		}
		if (obj.getAlunoAvaliaAluno()) {
			for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliadores : avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
				alunoAvaliaAluno = getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(obj, avaliadores);
				if (!Uteis.isAtributoPreenchido(totalAlunoAvaliaAluno) && Uteis.isAtributoPreenchido(alunoAvaliaAluno)) {
					totalAlunoAvaliaAluno = alunoAvaliaAluno;
				} else if (Uteis.isAtributoPreenchido(alunoAvaliaAluno)) {
					totalAlunoAvaliaAluno += alunoAvaliaAluno;
				}
			}
		}
		if (obj.getProfessorAvaliaAluno()) {
			profAvaliaAluno = getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(obj, avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO());
		}
		realizarValidacaoCalculoGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinal(obj, avaliacao, autoAvaliacao, totalAlunoAvaliaAluno, profAvaliaAluno, verificarAcesso, usuarioVO);
	}
	
	public void realizarValidacaoCalculoGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinal(ConteudoUnidadePaginaRecursoEducacionalVO obj, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao, Double autoAvaliacao, Double totalAlunoAvaliaAluno, Double profAvaliaAluno, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		Double notaFinal = null;
		boolean realizarCalculo = false;
		String formula = obj.getFormulaCalculoNotaFinal();
		if (autoAvaliacao != null) {
			formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.AUTO_AVAL, autoAvaliacao.toString());
			realizarCalculo = true;
		} else {
			formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.AUTO_AVAL, "0");
		}
		if (totalAlunoAvaliaAluno != null) {
			formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.ALUNO_AVAL, totalAlunoAvaliaAluno.toString());
			realizarCalculo = true;
		} else {
			formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.ALUNO_AVAL, "0");
		}
		if (profAvaliaAluno != null) {
			formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.PROF_AVAL, profAvaliaAluno.toString());
			realizarCalculo = true;
		} else {
			formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.PROF_AVAL, "0");
		}

		if (realizarCalculo) {
			formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.QTDE_ALU, String.valueOf(avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().size()));
			notaFinal = Uteis.realizarCalculoFormula(formula.replace(",", "."));
		}
		if (notaFinal != null ) {
			avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setSituacao(SituacaoPBLEnum.REALIZADO);
			avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setNotaLancada(true);
			avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setNota(notaFinal);
		} else {
			avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setSituacao(SituacaoPBLEnum.PENDENTE);
			avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setNotaLancada(false);
			avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setNota(notaFinal);
		}
		getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().persistir(avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO(), verificarAcesso, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void verificarAlunoPodeAvancarConteudoREAPendente(ConteudoVO conteudoVO, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws ConsistirException, Exception {

		for (ConteudoUnidadePaginaRecursoEducacionalVO objAnterior : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
			verificarAvancoConteudoPendente(objAnterior, conteudoVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
		}
		for (ConteudoUnidadePaginaRecursoEducacionalVO objPosterior : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
			verificarAvancoConteudoPendente(objPosterior, conteudoVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void verificarAvancoConteudoPendente(ConteudoUnidadePaginaRecursoEducacionalVO obj, ConteudoVO conteudoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		if (obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.AVALIACAO_PBL) || obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.ATA_PBL)) {
			verificarAvaliacaoPblRequerLiberacaoProfessor(obj, conteudoVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
			verificarAvaliacaoPblPendenteAluno(obj, conteudoVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
		}
		if (obj.getTipoRecursoEducacional().isTipoAvaliacaoOnline()) {
			verificarAvaliacaoOnlinePendenteAluno(obj, conteudoVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void verificarAvaliacaoPblRequerLiberacaoProfessor(ConteudoUnidadePaginaRecursoEducacionalVO obj, ConteudoVO conteudoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		GestaoEventoConteudoTurmaVO gestaoEvento = getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarGestaoEventoConteudoTurmaVOEstaPendente(obj.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), conteudoVO.getCodigo(), usuarioVO);
		if (obj.getRequerLiberacaoProfessor()) {
			if (gestaoEvento == null) {
				throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_naoPodeAvancarREAPendentes").replace("{0}", usuarioVO.getPessoa().getNome()).replace("{1}", obj.getTitulo()));
//			} else if (gestaoEvento.getSituacao().isPendente() && gestaoEvento.getDateLiberacao() != null && (UteisData.getCompareData(gestaoEvento.getDateLiberacao(), new Date()) > 0)) {
//				throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_naoPodeAvancarREAPendentesComData").replace("{0}", obj.getTitulo()).replace("{1}", gestaoEvento.getDataLiberacaoApresentar()));
//			} else if (obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.AVALIACAO_PBL) && gestaoEvento.getSituacao().isPendente() && gestaoEvento.getDateLiberacao() != null && (UteisData.getCompareData(gestaoEvento.getDateLiberacao(), new Date()) <= 0)) {
//				throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_existeNotasASeremLancadasNaoPermiteAvancarConteudoAvancoProximaPagina").replace("{0}", obj.getTitulo()));
//			} else if (obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.ATA_PBL) && !gestaoEvento.getSituacao().isRealizado() && gestaoEvento.getDateLiberacao() != null && (UteisData.getCompareData(gestaoEvento.getDateLiberacao(), new Date()) <= 0)) {
//				throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_existeAtaQueNaoPermiteAvancarConteudoProximaPagina").replace("{0}", obj.getTitulo()));
//			}
		}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void verificarAvaliacaoPblPendenteAluno(ConteudoUnidadePaginaRecursoEducacionalVO obj, ConteudoVO conteudoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		if (!obj.getPermiteAlunoAvancarConteudoSemLancarNota()) {
			GestaoEventoConteudoTurmaVO gestaoEvento = getFacadeFactory().getGestaoEventoConteudoTurmaFacade().consultarGestaoEventoConteudoTurmaVOEstaPendente(obj.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), conteudoVO.getCodigo(), usuarioVO);
			Boolean existePendencia = false;
			if (obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.AVALIACAO_PBL)) {
				if (obj.getAlunoAvaliaAluno()) {
					existePendencia = getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().consultarSeExisteAvaliacaoPblNaoRealizadaParaAvaliador(obj.getCodigo(), usuarioVO.getPessoa().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), conteudoVO.getCodigo(), TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO, usuarioVO);
					if (existePendencia) {
						throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_existeNotasASeremLancadasNaoPermiteAvancarConteudoAvancoProximaPagina").replace("{0}", obj.getTitulo()));
					}
				}
				if (obj.getAutoAvaliacao()) {
					existePendencia = getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().consultarSeExisteAvaliacaoPblNaoRealizadaParaAvaliador(obj.getCodigo(), usuarioVO.getPessoa().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), conteudoVO.getCodigo(), TipoAvaliacaoPBLEnum.AUTO_AVALIACAO, usuarioVO);
					if (existePendencia) {
						throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_existeNotasASeremLancadasNaoPermiteAvancarConteudoAvancoProximaPagina").replace("{0}", obj.getTitulo()));
					}
				}
			} else if (obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.ATA_PBL) && !gestaoEvento.getSituacao().isRealizado()) {
				throw new Exception(UteisJSF.internacionalizar("msg_GestaoEventoConteudoTurma_ataNaoPermiteAvancarConteudo").replace("{0}", obj.getTitulo()));
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void verificarAvaliacaoOnlinePendenteAluno(ConteudoUnidadePaginaRecursoEducacionalVO obj, ConteudoVO conteudoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
//		if (!obj.getPermiteAlunoAvancarConteudoSemLancarNota()) {
////			Boolean avaliacaoConcluida = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarSeExisteCalendarioAtividadeParaAluno(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA, SituacaoAtividadeEnum.CONCLUIDA, TipoOrigemEnum.REA, obj.getCodigo().toString(),  usuarioVO );
//			if (!avaliacaoConcluida) {
//				throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_existeAvaliacaoOnlineASeremResponidaNaoPermiteAvancarConteudoAvancoProximaPagina").replace("{0}", obj.getTitulo()));
//			}
//		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarQuantidadeDeAvaliacaoExistenteNoConteudoUnidadePaginaRecursoEducacional(ConteudoVO conteudo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder("select count(cupre.codigo) as qtd ");
		sb.append(" FROM conteudounidadepaginarecursoeducacional cupre ");
		sb.append(" inner join conteudounidadepagina cup on cup.codigo = cupre.conteudounidadepagina ");
		sb.append(" inner join unidadeconteudo uc on uc.codigo = cup.unidadeconteudo ");
		sb.append(" inner join conteudo on conteudo.codigo = uc.conteudo ");
		sb.append(" where cupre.tiporecursoeducacional = '").append(TipoRecursoEducacionalEnum.AVALIACAO_PBL).append("'");
		sb.append(" and  conteudo.codigo =").append(conteudo.getCodigo());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return rs.getInt("qtd");
		}
		return 0;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean consultarSeExisteAvaliacaoPblNoConteudoAtivoPorDisciplina(DisciplinaVO disciplina, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder("select case when count(1) > 0 then true else false end as validacao from conteudo  ");
		sb.append(" inner join unidadeconteudo uc on uc.conteudo = conteudo.codigo ");
		sb.append(" inner join conteudounidadepagina cup on cup.unidadeconteudo = uc.codigo ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on cupre.conteudounidadepagina = cup.codigo ");
		sb.append(" where conteudo.codigo = ( select codigo from conteudo where conteudo.disciplina = ").append(disciplina.getCodigo());
		sb.append(" and conteudo.situacaoConteudo = '").append(SituacaoConteudoEnum.ATIVO).append("' order by conteudo.versao desc limit 1 ) ");
		sb.append(" AND cupre.tiporecursoeducacional = '").append(TipoRecursoEducacionalEnum.AVALIACAO_PBL).append("'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return rs.getBoolean("validacao");
		}
		return false;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSelectConsultaRapidaGestaoEventoConteudo() {

		StringBuilder sb = new StringBuilder("SELECT cupre.codigo as \"cupre.codigo\", cupre.tiporecursoeducacional as \"cupre.tiporecursoeducacional\", cupre.titulo as \"cupre.titulo\", ");
		sb.append(" cupre.texto as \"cupre.texto\", cupre.descricao as \"cupre.descricao\", cupre.caminhobaserepositorio as \"cupre.caminhobaserepositorio\", cupre.nomerealarquivo as \"cupre.nomerealarquivo\", cupre.nomefisicoarquivo as \"cupre.nomefisicoarquivo\", ");
		sb.append(" cupre.recursoeducacional as \"cupre.recursoeducacional\", cupre.manterrecursodisponivelpagina as \"cupre.manterrecursodisponivelpagina\",  ");
		sb.append(" cupre.ordemapresentacao as \"cupre.ordemapresentacao\", cupre.momentoapresentacaorecursoeducacional as \"cupre.momentoapresentacaorecursoeducacional\", cupre.datacadastro as \"cupre.datacadastro\", ");
		sb.append(" cupre.usuariocadastro as \"cupre.usuariocadastro\", cupre.dataalteracao as \"cupre.dataalteracao\", cupre.usuarioalteracao as \"cupre.usuarioalteracao\", cupre.requerliberacaoprofessor as \"cupre.requerliberacaoprofessor\", ");
		sb.append(" cupre.altura as \"cupre.altura\", cupre.largura as \"cupre.largura\", cupre.apresentarlegenda as \"cupre.apresentarlegenda\", cupre.tipografico as \"cupre.tipografico\", ");
		sb.append(" cupre.autoavaliacao as \"cupre.autoavaliacao\", cupre.alunoavaliaaluno as \"cupre.alunoavaliaaluno\", cupre.professoravaliaaluno as \"cupre.professoravaliaaluno\", cupre.formulacalculonotafinal as \"cupre.formulacalculonotafinal\", ");
		sb.append(" cupre.utilizarnotaconceito as \"cupre.utilizarnotaconceito\", cupre.permitealunoavancarconteudosemlancarnota as \"cupre.permitealunoavancarconteudosemlancarnota\", cupre.faixaminimanotaautoavaliacao as \"cupre.faixaminimanotaautoavaliacao\",  ");
		sb.append(" cupre.faixamaximanotaautoavaliacao as \"cupre.faixamaximanotaautoavaliacao\", cupre.faixaminimanotaalunoavaliaaluno as \"cupre.faixaminimanotaalunoavaliaaluno\", cupre.faixamaximanotaalunoavaliaaluno as \"cupre.faixamaximanotaalunoavaliaaluno\",  ");
		sb.append(" cupre.faixaminimanotaprofessoravaliaaluno as \"cupre.faixaminimanotaprofessoravaliaaluno\", cupre.faixamaximanotaprofessoravaliaaluno as \"cupre.faixamaximanotaprofessoravaliaaluno\",  ");
		sb.append(" cupre.tituloEixoX as \"cupre.tituloEixoX\", cupre.tituloEixoY as \"cupre.tituloEixoY\", cupre.valorGrafico as \"cupre.valorGrafico\", ");
		sb.append(" cupre.categoriaGrafico as \"cupre.categoriaGrafico\",  ");
		sb.append(" cup.codigo as \"cup.codigo\", cup.tiporecursoeducacional as \"cup.tiporecursoeducacional\", cup.titulo as \"cup.titulo\", cup.texto as \"cup.texto\", ");
		sb.append(" uc.codigo as \"uc.codigo\", uc.titulo as \"uc.titulo\", uc.ordem as \"uc.ordem\", ");
		sb.append(" ((select count(codigo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = uc.codigo)) as \"uc.paginas\", ");
		sb.append(" conteudo.codigo as \"conteudo.codigo\" , conteudo.versao as \"conteudo.versao\" , conteudo.descricao as \"conteudo.descricao\", conteudo.situacaoConteudo as \"conteudo.situacaoConteudo\", ");
		sb.append(" conteudo.usoexclusivoprofessor as \"conteudo.usoexclusivoprofessor\"  ");
		sb.append(" FROM conteudounidadepaginarecursoeducacional cupre ");
		sb.append(" inner join conteudounidadepagina cup on cup.codigo = cupre.conteudounidadepagina ");
		sb.append(" inner join unidadeconteudo uc on uc.codigo = cup.unidadeconteudo ");
		sb.append(" inner join conteudo on conteudo.codigo = uc.conteudo ");

		return sb;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSelectConsultaRapidaMinhaNotasPbl(Integer codigoAvaliado, Integer turma, Integer disciplina, String ano, String semestre) {

		StringBuilder sb = new StringBuilder(" select conteudo.codigo as \"conteudo.codigo\" , conteudo.versao as \"conteudo.versao\" , conteudo.descricao as \"conteudo.descricao\", conteudo.situacaoConteudo as \"conteudo.situacaoConteudo\", ");
		sb.append(" conteudo.usoexclusivoprofessor as \"conteudo.usoexclusivoprofessor\",  ");
		
		sb.append(" disciplina.codigo as \"disciplina.codigo\",  disciplina.nome as \"disciplina.nome\", ");
		
		sb.append(" professor.codigo as \"professor.codigo\", professor.nome as \"professor.nome\", ");
		
		sb.append(" cup.codigo as \"cup.codigo\", cup.tiporecursoeducacional as \"cup.tiporecursoeducacional\", cup.titulo as \"cup.titulo\", cup.texto as \"cup.texto\", ");
		
		sb.append(" uc.codigo as \"uc.codigo\", uc.titulo as \"uc.titulo\", uc.ordem as \"uc.ordem\", ");
		sb.append(" ((select count(codigo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = uc.codigo)) as \"uc.paginas\", ");
		
		sb.append(" case when temaassunto.codigo is null then -1 else temaassunto.codigo end as \"temaassunto.codigo\", ");
		sb.append(" temaassunto.nome as \"temaassunto.nome\", ");
		
		sb.append(" cupre.codigo as \"cupre.codigo\", cupre.tiporecursoeducacional as \"cupre.tiporecursoeducacional\", cupre.titulo as \"cupre.titulo\",  ");
		sb.append(" cupre.texto as \"cupre.texto\", cupre.descricao as \"cupre.descricao\", cupre.caminhobaserepositorio as \"cupre.caminhobaserepositorio\", cupre.nomerealarquivo as \"cupre.nomerealarquivo\", cupre.nomefisicoarquivo as \"cupre.nomefisicoarquivo\", ");
		sb.append(" cupre.recursoeducacional as \"cupre.recursoeducacional\", cupre.manterrecursodisponivelpagina as \"cupre.manterrecursodisponivelpagina\",  ");
		sb.append(" cupre.ordemapresentacao as \"cupre.ordemapresentacao\", cupre.momentoapresentacaorecursoeducacional as \"cupre.momentoapresentacaorecursoeducacional\", cupre.datacadastro as \"cupre.datacadastro\", ");
		sb.append(" cupre.usuariocadastro as \"cupre.usuariocadastro\", cupre.dataalteracao as \"cupre.dataalteracao\", cupre.usuarioalteracao as \"cupre.usuarioalteracao\", cupre.requerliberacaoprofessor as \"cupre.requerliberacaoprofessor\", ");
		sb.append(" cupre.altura as \"cupre.altura\", cupre.largura as \"cupre.largura\", cupre.apresentarlegenda as \"cupre.apresentarlegenda\", cupre.tipografico as \"cupre.tipografico\", ");
		sb.append(" cupre.autoavaliacao as \"cupre.autoavaliacao\", cupre.alunoavaliaaluno as \"cupre.alunoavaliaaluno\", cupre.professoravaliaaluno as \"cupre.professoravaliaaluno\", cupre.formulacalculonotafinal as \"cupre.formulacalculonotafinal\", ");
		sb.append(" cupre.utilizarnotaconceito as \"cupre.utilizarnotaconceito\", cupre.permitealunoavancarconteudosemlancarnota as \"cupre.permitealunoavancarconteudosemlancarnota\", cupre.faixaminimanotaautoavaliacao as \"cupre.faixaminimanotaautoavaliacao\",  ");
		sb.append(" cupre.faixamaximanotaautoavaliacao as \"cupre.faixamaximanotaautoavaliacao\", cupre.faixaminimanotaalunoavaliaaluno as \"cupre.faixaminimanotaalunoavaliaaluno\", cupre.faixamaximanotaalunoavaliaaluno as \"cupre.faixamaximanotaalunoavaliaaluno\",  ");
		sb.append(" cupre.faixaminimanotaprofessoravaliaaluno as \"cupre.faixaminimanotaprofessoravaliaaluno\", cupre.faixamaximanotaprofessoravaliaaluno as \"cupre.faixamaximanotaprofessoravaliaaluno\",  ");
		sb.append(" cupre.tituloEixoX as \"cupre.tituloEixoX\", cupre.tituloEixoY as \"cupre.tituloEixoY\", cupre.valorGrafico as \"cupre.valorGrafico\", ");
		sb.append(" cupre.categoriaGrafico as \"cupre.categoriaGrafico\",  ");
		
		//sb.append(" gect.codigo as \"gect.codigo\", gect.situacao as \"gect.situacao\", gect.dateliberacao as \"gect.dateliberacao\",");
		sb.append(" gect.codigo as \"gect.codigo\", ");
		sb.append(" gect_conteudo.formulacalculonotafinalgeral as \"gect_conteudo.formulacalculonotafinalgeral\", ");
		
		sb.append(" autoavaliacao.codigo as \"autoavaliacao.codigo\", autoavaliacao.tipoavaliacao as \"autoavaliacao.tipoavaliacao\", autoavaliacao.nota as \"autoavaliacao.nota\", ");
		sb.append(" autoavaliacao.situacao as \"autoavaliacao.situacao\", autoavaliacao.matricula as \"autoavaliacao.matricula\", ");
		sb.append(" autoavaliacao.notalancada as \"autoavaliacao.notalancada\", autoavaliacao.matriculaperiodoturmadisciplinaavaliado as \"gectap.matriculaperiodoturmadisciplinaavaliado\",");
		sb.append(" autoavaliacao.gestaoeventoconteudoturma as \"autoavaliacao.gestaoeventoconteudoturma\" , ");
		
		sb.append(" notaautoavaliacaoconceito.codigo as \"notaautoavaliacaoconceito.codigo\" , ");
		sb.append(" notaautoavaliacaoconceito.conceito as \"notaautoavaliacaoconceito.conceito\" , ");
		sb.append(" notaautoavaliacaoconceito.notacorrespondente as \"notaautoavaliacaoconceito.notacorrespondente\" , ");

		sb.append(" professoravaliacao.codigo as \"professoravaliacao.codigo\", professoravaliacao.tipoavaliacao as \"professoravaliacao.tipoavaliacao\", professoravaliacao.nota as \"professoravaliacao.nota\", ");
		sb.append(" professoravaliacao.situacao as \"professoravaliacao.situacao\", professoravaliacao.matricula as \"professoravaliacao.matricula\", ");
		sb.append(" professoravaliacao.notalancada as \"professoravaliacao.notalancada\", professoravaliacao.matriculaperiodoturmadisciplinaavaliado as \"gectap.matriculaperiodoturmadisciplinaavaliado\",");
		sb.append(" professoravaliacao.gestaoeventoconteudoturma as \"professoravaliacao.gestaoeventoconteudoturma\" , ");
		
		sb.append(" notaprofessoravaliacaoconceito.codigo as \"notaprofessoravaliacaoconceito.codigo\" , ");
		sb.append(" notaprofessoravaliacaoconceito.conceito as \"notaprofessoravaliacaoconceito.conceito\" , ");
		sb.append(" notaprofessoravaliacaoconceito.notacorrespondente as \"notaprofessoravaliacaoconceito.notacorrespondente\" , ");


		sb.append(" resultadoavaliacao.codigo as \"resultadoavaliacao.codigo\", resultadoavaliacao.tipoavaliacao as \"resultadoavaliacao.tipoavaliacao\", resultadoavaliacao.nota as \"resultadoavaliacao.nota\", ");
		sb.append(" resultadoavaliacao.situacao as \"resultadoavaliacao.situacao\", resultadoavaliacao.matricula as \"resultadoavaliacao.matricula\", ");
		sb.append(" resultadoavaliacao.notalancada as \"resultadoavaliacao.notalancada\", resultadoavaliacao.matriculaperiodoturmadisciplinaavaliado as \"gectap.matriculaperiodoturmadisciplinaavaliado\", ");
		sb.append(" resultadoavaliacao.gestaoeventoconteudoturma as \"resultadoavaliacao.gestaoeventoconteudoturma\", ");
		
		sb.append(" resultadofinalavaliacao.codigo as \"resultadofinalavaliacao.codigo\", resultadofinalavaliacao.tipoavaliacao as \"resultadofinalavaliacao.tipoavaliacao\", resultadofinalavaliacao.nota as \"resultadofinalavaliacao.nota\", ");
		sb.append(" resultadofinalavaliacao.situacao as \"resultadofinalavaliacao.situacao\", resultadofinalavaliacao.matricula as \"resultadofinalavaliacao.matricula\", ");
		sb.append(" resultadofinalavaliacao.notalancada as \"resultadofinalavaliacao.notalancada\", resultadofinalavaliacao.matriculaperiodoturmadisciplinaavaliado as \"gectap.matriculaperiodoturmadisciplinaavaliado\", ");
		sb.append(" resultadofinalavaliacao.gestaoeventoconteudoturma as \"resultadofinalavaliacao.gestaoeventoconteudoturma\", ");
		
		sb.append("(case when (cupre.permiteAlunoAvancarConteudoSemLancarNota = false and  autoavaliacao.situacao !='").append(SituacaoPBLEnum.REALIZADO).append("') then true else false end) pendenciaAutoavaliacao, ");
		sb.append("(case when (cupre.permiteAlunoAvancarConteudoSemLancarNota = false and  count(distinct alunoavaliacaocolega.codigo) > 0) then true else false end) pendenciaAlunoavaliacao, ");
		
		sb.append(" (count(distinct case when colegaavaliacaoaluno.situacao = 'REALIZADO' and colegaavaliacaoaluno.codigo is not null then colegaavaliacaoaluno.codigo end)) as qtdNotaRealizadaColegaAluno,  ");
		sb.append(" (count(distinct colegaavaliacaoaluno.codigo)) as totalColegaAluno, ");
		sb.append(" (select sum(case when notaColega.nota is null then colegaavaliacaoalunoconceito.notacorrespondente  else notaColega.nota end) from   gestaoeventoconteudoturmaavaliacaopbl notaColega  ");
		sb.append(" left join notaconceitoavaliacaopbl colegaavaliacaoalunoconceito on  colegaavaliacaoalunoconceito.codigo = notaColega.notaconceitoavaliacaopbl  and colegaavaliacaoalunoconceito.tipoavaliacao = notaColega.tipoavaliacao  ");
		sb.append(" where notaColega.gestaoeventoconteudoturma = gect.codigo ");
		sb.append(" and notaColega.avaliado = ").append(codigoAvaliado).append(" and notaColega.avaliador !=  ").append(codigoAvaliado);
		sb.append(" and notaColega.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO).append("') as \"colegaavaliacaoaluno.somaNota\" ");

		sb.append(" from conteudo  ");
		sb.append(" inner join disciplina  on disciplina.codigo = conteudo.disciplina ");
		sb.append(" inner join unidadeconteudo uc on uc.conteudo = conteudo.codigo ");
		sb.append(" inner join conteudounidadepagina cup on cup.unidadeconteudo = uc.codigo ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on cupre.conteudounidadepagina = cup.codigo ");
		sb.append(" left join pessoa as professor on professor.codigo = conteudo.professor ");
		sb.append(" left join temaassunto on temaassunto.codigo = uc.temaassunto ");
		sb.append(" left join gestaoeventoconteudoturma gect on gect.conteudounidadepaginarecursoeducacional = cupre.codigo  and gect.tiporecurso = '").append(TipoRecursoEnum.CONTEUDO_UNIDADE_PAGINA_RECURSO_EDUCACIONAL).append("' ").append(" and gect.turma = ").append(turma).append(" and gect.disciplina =").append(disciplina).append(" and gect.ano ='").append(ano).append("' and gect.semestre = '").append(semestre).append("'  ");
		sb.append(" left join gestaoeventoconteudoturmaavaliacaopbl autoavaliacao on  autoavaliacao.gestaoeventoconteudoturma = gect.codigo  and autoavaliacao.avaliador = ").append(codigoAvaliado).append(" and autoavaliacao.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.AUTO_AVALIACAO).append("' ");
		sb.append(" left join notaconceitoavaliacaopbl notaautoavaliacaoconceito on  notaautoavaliacaoconceito.codigo = autoavaliacao.notaconceitoavaliacaopbl  and notaautoavaliacaoconceito.tipoavaliacao = autoavaliacao.tipoavaliacao ");
		sb.append(" left join gestaoeventoconteudoturmaavaliacaopbl professoravaliacao on  professoravaliacao.gestaoeventoconteudoturma = gect.codigo  and professoravaliacao.avaliado = ").append(codigoAvaliado).append(" and professoravaliacao.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO).append("' ");
		sb.append(" left join notaconceitoavaliacaopbl notaprofessoravaliacaoconceito on  notaprofessoravaliacaoconceito.codigo = professoravaliacao.notaconceitoavaliacaopbl  and notaprofessoravaliacaoconceito.tipoavaliacao = professoravaliacao.tipoavaliacao ");
		sb.append(" left join gestaoeventoconteudoturmaavaliacaopbl resultadoavaliacao on  resultadoavaliacao.gestaoeventoconteudoturma = gect.codigo  and resultadoavaliacao.avaliado = ").append(codigoAvaliado).append(" and resultadoavaliacao.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.RESULTADO_FINAL).append("' ");
		sb.append(" left join gestaoeventoconteudoturmaavaliacaopbl colegaavaliacaoaluno on  colegaavaliacaoaluno.gestaoeventoconteudoturma = gect.codigo  and colegaavaliacaoaluno.avaliado = ").append(codigoAvaliado).append("  and colegaavaliacaoaluno.avaliador != ").append(codigoAvaliado).append(" and colegaavaliacaoaluno.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO).append("' ");
		sb.append(" left join gestaoeventoconteudoturmaavaliacaopbl alunoavaliacaocolega on  alunoavaliacaocolega.gestaoeventoconteudoturma = gect.codigo  and alunoavaliacaocolega.avaliado != ").append(codigoAvaliado).append("  and alunoavaliacaocolega.avaliador = ").append(codigoAvaliado).append(" and alunoavaliacaocolega.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO).append("' and alunoavaliacaocolega.situacao !='").append(SituacaoPBLEnum.REALIZADO).append("' ");
		sb.append(" left join gestaoeventoconteudoturma gect_conteudo on gect_conteudo.conteudo = conteudo.codigo and gect_conteudo.tiporecurso = '").append(TipoRecursoEnum.CONTEUDO).append("' ");
		sb.append(" left join gestaoeventoconteudoturmaavaliacaopbl resultadofinalavaliacao on  resultadofinalavaliacao.gestaoeventoconteudoturma = gect_conteudo.codigo  and resultadofinalavaliacao.avaliado = ").append(codigoAvaliado).append(" and resultadofinalavaliacao.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.RESULTADO_FINAL_GERAL).append("' ");
		return sb;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getGroupByConsultaRapidaMinhaNotasPbl() {
		StringBuilder sb = new StringBuilder(" group by conteudo.codigo, conteudo.versao, conteudo.descricao, conteudo.situacaoConteudo, ");
		sb.append(" conteudo.usoexclusivoprofessor, disciplina.codigo,  disciplina.nome, professor.codigo, professor.nome,");
		sb.append(" cup.codigo, cup.tiporecursoeducacional, cup.titulo, cup.texto, ");
		sb.append(" uc.codigo, uc.titulo, uc.ordem, ");
		sb.append(" ((select count(codigo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = uc.codigo)), ");
		sb.append(" temaassunto.codigo,temaassunto.nome, ");
		sb.append(" cupre.codigo, cupre.tiporecursoeducacional, cupre.titulo,  ");
		sb.append(" cupre.texto, cupre.descricao, cupre.caminhobaserepositorio, cupre.nomerealarquivo, cupre.nomefisicoarquivo, ");
		sb.append(" cupre.recursoeducacional, cupre.manterrecursodisponivelpagina,  ");
		sb.append(" cupre.ordemapresentacao, cupre.momentoapresentacaorecursoeducacional, cupre.datacadastro, ");
		sb.append(" cupre.usuariocadastro, cupre.dataalteracao, cupre.usuarioalteracao, cupre.requerliberacaoprofessor, ");
		sb.append(" cupre.altura, cupre.largura, cupre.apresentarlegenda, cupre.tipografico, ");
		sb.append(" cupre.autoavaliacao, cupre.alunoavaliaaluno, cupre.professoravaliaaluno, cupre.formulacalculonotafinal, ");
		sb.append(" cupre.utilizarnotaconceito, cupre.permitealunoavancarconteudosemlancarnota, cupre.faixaminimanotaautoavaliacao,  ");
		sb.append(" cupre.faixamaximanotaautoavaliacao, cupre.faixaminimanotaalunoavaliaaluno, cupre.faixamaximanotaalunoavaliaaluno,  ");
		sb.append(" cupre.faixaminimanotaprofessoravaliaaluno , cupre.faixamaximanotaprofessoravaliaaluno,  ");
		sb.append(" cupre.tituloEixoX , cupre.tituloEixoY , cupre.valorGrafico, ");
		sb.append(" cupre.categoriaGrafico ,  ");
		sb.append(" gect_conteudo.formulacalculonotafinalgeral ,  ");
		sb.append(" gect.codigo ,  ");
		//sb.append(" gect.codigo , gect.situacao, gect.dateliberacao,  ");
		sb.append(" autoavaliacao.codigo , autoavaliacao.tipoavaliacao , autoavaliacao.nota ,  ");
		sb.append(" autoavaliacao.situacao , autoavaliacao.matricula ,autoavaliacao.gestaoeventoconteudoturma  , ");
		sb.append(" notaautoavaliacaoconceito.codigo,  ");
		sb.append(" notaautoavaliacaoconceito.conceito,  ");
		sb.append(" notaautoavaliacaoconceito.notacorrespondente, ");
		sb.append(" notaprofessoravaliacaoconceito.codigo,  ");
		sb.append(" notaprofessoravaliacaoconceito.conceito,  ");
		sb.append(" notaprofessoravaliacaoconceito.notacorrespondente, ");
		sb.append(" professoravaliacao.codigo , professoravaliacao.tipoavaliacao , professoravaliacao.nota ,  ");
		sb.append(" professoravaliacao.situacao , professoravaliacao.matricula , professoravaliacao.gestaoeventoconteudoturma  , ");
		sb.append(" resultadoavaliacao.codigo , resultadoavaliacao.tipoavaliacao , resultadoavaliacao.nota ,  ");
		sb.append(" resultadoavaliacao.situacao , resultadoavaliacao.matricula , resultadoavaliacao.gestaoeventoconteudoturma ,  ");
		sb.append(" resultadofinalavaliacao.codigo , resultadofinalavaliacao.tipoavaliacao , resultadofinalavaliacao.nota ,  ");
		sb.append(" resultadofinalavaliacao.situacao , resultadofinalavaliacao.matricula , resultadofinalavaliacao.gestaoeventoconteudoturma   ");
		return sb;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConteudoUnidadePaginaRecursoEducacionalVO consultarRapidaConteudoUnidadePaginaRecursoEducacionalPorChavePrimaria(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSelectConsultaRapidaGestaoEventoConteudo();
		sb.append(" where cupre.codigo = ").append(conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
		sb.append(" order by cupre.ordemapresentacao");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDadosRapido(rs, nivelMontarDados, usuarioVO);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<MinhasNotasPBLVO> consultarMinhaNotasPBLRapidaPorCodigoDisciplina(Integer conteudo, Integer turma, Integer disciplina, String ano, String semestre, Integer codigoPessoa, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = getSelectConsultaRapidaMinhaNotasPbl(codigoPessoa, turma, disciplina, ano, semestre);
		sb.append(" where cupre.tiporecursoeducacional = '").append(TipoRecursoEducacionalEnum.AVALIACAO_PBL).append("' ");
		if(Uteis.isAtributoPreenchido(conteudo)){
			sb.append(" and conteudo.codigo = ").append(conteudo).append(" ");
		}else if(Uteis.isAtributoPreenchido(disciplina)){
			sb.append(" and conteudo.codigo = ( select codigo from conteudo where conteudo.disciplina = ").append(disciplina);
			sb.append(" and conteudo.situacaoConteudo = '").append(SituacaoConteudoEnum.ATIVO).append("' order by conteudo.versao desc limit 1 ) ");
		}else{
			throw new Exception(UteisJSF.internacionalizar("msg_menhumFiltroInformadoParaRealizarConsulta"));
		}
		sb.append(getGroupByConsultaRapidaMinhaNotasPbl());
		sb.append(" order by temaassunto.codigo,  cupre.titulo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsultaRapidoMinhasNotaPbl(rs, nivelMontarDados, controlarAcesso, usuario);

	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private List<MinhasNotasPBLVO> montarDadosConsultaRapidoMinhasNotaPbl(SqlRowSet rs, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		List<MinhasNotasPBLVO> lista = new ArrayList<MinhasNotasPBLVO>(0);
		while (rs.next()) {
			MinhasNotasPBLVO obj = consultaSeJaExisteConteudoNaLista(rs.getInt("temaassunto.codigo"), lista);
			if (obj.getTemaAssuntoVO().getCodigo() == null || obj.getTemaAssuntoVO().getCodigo().equals(0)) {
				montarDadosMinhasNotasPBLVO(obj, rs);
			}
			ConteudoUnidadePaginaRecursoEducacionalVO cupre = montarDadosRapido(rs, nivelMontarDados, usuario);
			cupre.getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().getDisciplina().setCodigo(rs.getInt("disciplina.codigo"));
			cupre.getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().getDisciplina().setNome(rs.getString("disciplina.nome"));
			if (Uteis.isAtributoPreenchido(rs.getInt("professor.codigo"))) {
				cupre.getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().getProfessor().setNome(rs.getString("professor.nome"));
				cupre.getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().getProfessor().setCodigo(rs.getInt("professor.codigo"));
			}
			if(cupre.getUtilizarNotaConceito()){
				cupre.setNotaConceitoAvaliacaoPBLVOs(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().consultarPorCodigoConteudoUnidadePaginaRecursoEducacional(cupre.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
				cupre.setListaSelectItemNotaConceitoAvaliacaoPblAutoAvaliacao(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().montarComboboxNotaConceito(cupre.getNotaConceitoAvaliacaoPBLVOs(), TipoAvaliacaoPBLEnum.AUTO_AVALIACAO, usuario));
				cupre.setListaSelectItemNotaConceitoAvaliacaoPblProfessorAvaliaAluno(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().montarComboboxNotaConceito(cupre.getNotaConceitoAvaliacaoPBLVOs(), TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO, usuario));
			}
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setExistePendenciaAutoAvaliacao(rs.getBoolean("pendenciaAutoavaliacao"));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setExistePendenciaAlunoAvaliaAluno(rs.getBoolean("pendenciaAlunoavaliacao"));
			
			if (rs.getObject("colegaavaliacaoaluno.somaNota") != null) {
				cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setMediaColegas(Uteis.arrendondarForcando2CadasDecimais(rs.getDouble("colegaavaliacaoaluno.somaNota")/rs.getDouble("totalColegaAluno")));
				double resultado = ((rs.getDouble("qtdNotaRealizadaColegaAluno")/rs.getDouble("totalColegaAluno")) * 100);
				cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setPorcentagemAvaliacoesColegas(Uteis.arrendondarForcando2CadasDecimaisStr(resultado) + "% dos alunos já te avaliaram.");
			}		
			montarDadosAutoAvaliacaoMinhaNotaPbl(cupre, rs, usuario);
			montarDadosProfessorAvaliacaoMinhaNotaPbl(cupre, rs);
			montarDadosResultadoFinalAvaliacaoMinhaNotaPbl(cupre, rs);
			obj.getListaConteudoUnidadePaginaRecursoEducacional().add(cupre);
			adicionarConteudoNaLista(obj, lista);
		}
		return lista;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosMinhasNotasPBLVO(MinhasNotasPBLVO obj, SqlRowSet rs)throws Exception {
		obj.getTemaAssuntoVO().setCodigo(rs.getInt("temaassunto.codigo"));
		obj.getTemaAssuntoVO().setNome(rs.getString("temaassunto.nome"));
		if (Uteis.isAtributoPreenchido(rs.getInt("resultadofinalavaliacao.codigo"))) {
			obj.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO().setCodigo(rs.getInt("resultadofinalavaliacao.codigo"));
			obj.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.RESULTADO_FINAL_GERAL);
			obj.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO().setSituacao(SituacaoPBLEnum.valueOf(rs.getString("resultadofinalavaliacao.situacao")));
			obj.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO().getMatriculaVO().setMatricula(rs.getString("resultadofinalavaliacao.matricula"));
			obj.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO().getGestaoEventoConteudoTurmaVO().setCodigo(rs.getInt("resultadofinalavaliacao.gestaoeventoconteudoturma"));
			if (rs.getObject("resultadofinalavaliacao.nota") != null) {
				obj.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO().setNota(rs.getDouble("resultadofinalavaliacao.nota"));
				obj.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalGeralVO().setFormulaResolvidaMediaFinal(rs.getString("gect_conteudo.formulacalculonotafinalgeral"));
			
			}
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosAutoAvaliacaoMinhaNotaPbl(ConteudoUnidadePaginaRecursoEducacionalVO cupre, SqlRowSet rs, UsuarioVO usuario)throws Exception {
		if (Uteis.isAtributoPreenchido(rs.getInt("autoavaliacao.codigo"))) {
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setCodigo(rs.getInt("autoavaliacao.codigo"));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.AUTO_AVALIACAO);
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setSituacao(SituacaoPBLEnum.valueOf(rs.getString("autoavaliacao.situacao")));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getMatriculaVO().setMatricula(rs.getString("autoavaliacao.matricula"));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaVO().setCodigo(rs.getInt("autoavaliacao.gestaoeventoconteudoturma"));
			if (rs.getObject("autoavaliacao.nota") != null && !cupre.getUtilizarNotaConceito()) {
				cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setNota(rs.getDouble("autoavaliacao.nota"));
			}
			if (rs.getObject("notaautoavaliacaoconceito.notacorrespondente") != null && cupre.getUtilizarNotaConceito()) {
				NotaConceitoAvaliacaoPBLVO notaConceito = new NotaConceitoAvaliacaoPBLVO();
				notaConceito.setCodigo(rs.getInt("notaautoavaliacaoconceito.codigo"));
				notaConceito.setConceito(rs.getString("notaautoavaliacaoconceito.conceito"));
				notaConceito.setNotaCorrespondente(rs.getDouble("notaautoavaliacaoconceito.notacorrespondente"));
				cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setNotaConceitoAvaliacaoPBLVO(notaConceito);				
			}
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosProfessorAvaliacaoMinhaNotaPbl(ConteudoUnidadePaginaRecursoEducacionalVO cupre, SqlRowSet rs)throws Exception {
		if (Uteis.isAtributoPreenchido(rs.getInt("professoravaliacao.codigo"))) {
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setCodigo(rs.getInt("professoravaliacao.codigo"));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO);
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setSituacao(SituacaoPBLEnum.valueOf(rs.getString("professoravaliacao.situacao")));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().getMatriculaVO().setMatricula(rs.getString("professoravaliacao.matricula"));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().getGestaoEventoConteudoTurmaVO().setCodigo(rs.getInt("professoravaliacao.gestaoeventoconteudoturma"));
			if (rs.getObject("professoravaliacao.nota") != null && !cupre.getUtilizarNotaConceito()) {
				cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setNota(rs.getDouble("professoravaliacao.nota"));
			}
			if (rs.getObject("notaprofessoravaliacaoconceito.notacorrespondente") != null && cupre.getUtilizarNotaConceito()) {
				NotaConceitoAvaliacaoPBLVO notaConceito = new NotaConceitoAvaliacaoPBLVO();
				notaConceito.setCodigo(rs.getInt("notaprofessoravaliacaoconceito.codigo"));
				notaConceito.setConceito(rs.getString("notaprofessoravaliacaoconceito.conceito"));
				notaConceito.setNotaCorrespondente(rs.getDouble("notaprofessoravaliacaoconceito.notacorrespondente"));
				cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setNotaConceitoAvaliacaoPBLVO(notaConceito);				
			}
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosResultadoFinalAvaliacaoMinhaNotaPbl(ConteudoUnidadePaginaRecursoEducacionalVO cupre, SqlRowSet rs)throws Exception {
		if (Uteis.isAtributoPreenchido(rs.getInt("resultadoavaliacao.codigo"))) {
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setCodigo(rs.getInt("resultadoavaliacao.codigo"));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.RESULTADO_FINAL);
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setSituacao(SituacaoPBLEnum.valueOf(rs.getString("resultadoavaliacao.situacao")));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().getMatriculaVO().setMatricula(rs.getString("resultadoavaliacao.matricula"));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().getGestaoEventoConteudoTurmaVO().setCodigo(rs.getInt("resultadoavaliacao.gestaoeventoconteudoturma"));
			if (rs.getObject("resultadoavaliacao.nota") != null) {
				cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setNota(rs.getDouble("resultadoavaliacao.nota"));
				String formula = rs.getString("cupre.formulaCalculoNotaFinal");
				if (Uteis.isAtributoPreenchido(cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getNota())) {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.AUTO_AVAL, Uteis.arrendondarForcando2CadasDecimaisStr(cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getNota()));
				} else {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.AUTO_AVAL, "0");
				}
				if (Uteis.isAtributoPreenchido(rs.getObject("colegaavaliacaoaluno.somaNota"))) {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.ALUNO_AVAL, Uteis.arrendondarForcando2CadasDecimaisStr(new Double(rs.getDouble("colegaavaliacaoaluno.somaNota"))));
				} else {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.ALUNO_AVAL, "0");
				}
				if (Uteis.isAtributoPreenchido(cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().getNota())) {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.PROF_AVAL, Uteis.arrendondarForcando2CadasDecimaisStr(cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().getNota()));
				} else {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.PROF_AVAL, "0");
				}					
				formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.QTDE_ALU, new Integer((rs.getInt("totalColegaAluno")+ 1)).toString());
				cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setFormulaResolvidaMediaFinal(formula);
				
			}
		}
	}
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosResultadoFinalGeralMinhaNotaPbl(ConteudoUnidadePaginaRecursoEducacionalVO cupre, SqlRowSet rs)throws Exception {
		if (Uteis.isAtributoPreenchido(rs.getInt("resultadoavaliacao.codigo"))) {
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setCodigo(rs.getInt("resultadoavaliacao.codigo"));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.RESULTADO_FINAL);
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setSituacao(SituacaoPBLEnum.valueOf(rs.getString("resultadoavaliacao.situacao")));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().getMatriculaVO().setMatricula(rs.getString("resultadoavaliacao.matricula"));
			cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().getGestaoEventoConteudoTurmaVO().setCodigo(rs.getInt("resultadoavaliacao.gestaoeventoconteudoturma"));
			if (rs.getObject("resultadoavaliacao.nota") != null) {
				cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setNota(rs.getDouble("resultadoavaliacao.nota"));
				String formula = rs.getString("cupre.formulaCalculoNotaFinal");
				if (Uteis.isAtributoPreenchido(cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getNota())) {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.AUTO_AVAL, Uteis.arrendondarForcando2CadasDecimaisStr(cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getNota()));
				} else {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.AUTO_AVAL, "0");
				}
				if (Uteis.isAtributoPreenchido(rs.getObject("colegaavaliacaoaluno.somaNota"))) {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.ALUNO_AVAL, Uteis.arrendondarForcando2CadasDecimaisStr(new Double(rs.getDouble("colegaavaliacaoaluno.somaNota"))));
				} else {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.ALUNO_AVAL, "0");
				}
				if (Uteis.isAtributoPreenchido(cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().getNota())) {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.PROF_AVAL, Uteis.arrendondarForcando2CadasDecimaisStr(cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().getNota()));
				} else {
					formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.PROF_AVAL, "0");
				}					
				formula = formula.replace(ConteudoUnidadePaginaRecursoEducacionalVO.QTDE_ALU, new Integer((rs.getInt("totalColegaAluno")+ 1)).toString());
				cupre.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setFormulaResolvidaMediaFinal(formula);
				
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private MinhasNotasPBLVO consultaSeJaExisteConteudoNaLista(Integer tema, List<MinhasNotasPBLVO> lista) {
		for (MinhasNotasPBLVO obj : lista) {
			if (obj.getTemaAssuntoVO().getCodigo().equals(tema)) {
				return obj;
			}
		}
		return new MinhasNotasPBLVO();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void adicionarConteudoNaLista(MinhasNotasPBLVO tema, List<MinhasNotasPBLVO> lista) {
		int index = 0;
		for (MinhasNotasPBLVO obj : lista) {
			if (obj.getTemaAssuntoVO().getCodigo().equals(tema.getTemaAssuntoVO().getCodigo())) {
				lista.set(index, tema);
				return;
			}
			index++;
		}
		lista.add(tema);
	}

	private ConteudoUnidadePaginaRecursoEducacionalVO montarDadosRapido(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConteudoUnidadePaginaRecursoEducacionalVO obj = new ConteudoUnidadePaginaRecursoEducacionalVO();
		obj.setCodigo(rs.getInt("cupre.codigo"));
		obj.setTipoRecursoEducacional(TipoRecursoEducacionalEnum.valueOf(rs.getString("cupre.tiporecursoeducacional")));
		obj.setTitulo(rs.getString("cupre.titulo"));
		obj.setTexto(rs.getString("cupre.texto"));
		obj.setDescricao(rs.getString("cupre.descricao"));
		obj.setCaminhoBaseRepositorio(rs.getString("cupre.caminhobaserepositorio"));
		obj.setNomeFisicoArquivo(rs.getString("cupre.nomefisicoarquivo"));
		obj.setNomeRealArquivo(rs.getString("cupre.nomerealarquivo"));

		obj.getRecursoEducacional().setCodigo(rs.getInt("cupre.recursoeducacional"));
		obj.setManterRecursoDisponivelPagina(rs.getBoolean("cupre.manterrecursodisponivelpagina"));
		obj.setOrdemApresentacao(rs.getInt("cupre.ordemapresentacao"));
		obj.setMomentoApresentacaoRecursoEducacional(MomentoApresentacaoRecursoEducacionalEnum.valueOf(rs.getString("cupre.momentoapresentacaorecursoeducacional")));
		obj.setDataCadastro(rs.getDate("cupre.datacadastro"));
		obj.getUsuarioAlteracao().setCodigo(rs.getInt("cupre.usuarioalteracao"));
		obj.getUsuarioCadastro().setCodigo(rs.getInt("cupre.usuariocadastro"));
		obj.setDataAlteracao(rs.getDate("cupre.dataalteracao"));
		obj.setRequerLiberacaoProfessor(rs.getBoolean("cupre.requerliberacaoprofessor"));

		obj.setAltura(rs.getInt("cupre.altura"));
		obj.setLargura(rs.getInt("cupre.largura"));
		obj.setTipoGrafico(TipoGraficoEnum.valueOf(rs.getString("cupre.tipografico")));
		obj.setTituloEixoX(rs.getString("cupre.tituloEixoX"));
		obj.setTituloEixoY(rs.getString("cupre.tituloEixoY"));
		obj.setValorGrafico(rs.getString("cupre.valorGrafico"));
		obj.setCategoriaGrafico(rs.getString("cupre.categoriaGrafico"));
		obj.setApresentarLegenda(rs.getBoolean("cupre.apresentarlegenda"));

		obj.setAutoAvaliacao(rs.getBoolean("cupre.autoAvaliacao"));
		obj.setAlunoAvaliaAluno(rs.getBoolean("cupre.alunoAvaliaAluno"));
		obj.setProfessorAvaliaAluno(rs.getBoolean("cupre.professorAvaliaAluno"));
		obj.setFormulaCalculoNotaFinal(rs.getString("cupre.formulaCalculoNotaFinal"));
		obj.setUtilizarNotaConceito(rs.getBoolean("cupre.utilizarNotaConceito"));
		obj.setPermiteAlunoAvancarConteudoSemLancarNota(rs.getBoolean("cupre.permiteAlunoAvancarConteudoSemLancarNota"));
		obj.setFaixaMinimaNotaAutoAvaliacao(rs.getDouble("cupre.faixaMinimaNotaAutoAvaliacao"));
		obj.setFaixaMaximaNotaAutoAvaliacao(rs.getDouble("cupre.faixaMaximaNotaAutoAvaliacao"));
		obj.setFaixaMinimaNotaAlunoAvaliaAluno(rs.getDouble("cupre.faixaMinimaNotaAlunoAvaliaAluno"));
		obj.setFaixaMaximaNotaAlunoAvaliaAluno(rs.getDouble("cupre.faixaMaximaNotaAlunoAvaliaAluno"));
		obj.setFaixaMinimaNotaProfessorAvaliaAluno(rs.getDouble("cupre.faixaMinimaNotaProfessorAvaliaAluno"));
		obj.setFaixaMaximaNotaProfessorAvaliaAluno(rs.getDouble("cupre.faixaMaximaNotaProfessorAvaliaAluno"));

		obj.getConteudoUnidadePagina().setCodigo(rs.getInt("cup.codigo"));
		obj.getConteudoUnidadePagina().setTipoRecursoEducacional(TipoRecursoEducacionalEnum.valueOf(rs.getString("cup.tiporecursoeducacional")));
		obj.getConteudoUnidadePagina().setTitulo(rs.getString("cup.titulo"));
		obj.getConteudoUnidadePagina().setTexto(rs.getString("cup.texto"));

		obj.getConteudoUnidadePagina().getUnidadeConteudo().setCodigo(rs.getInt("uc.codigo"));
		obj.getConteudoUnidadePagina().getUnidadeConteudo().setTitulo(rs.getString("uc.titulo"));
		obj.getConteudoUnidadePagina().getUnidadeConteudo().setOrdem(rs.getInt("uc.ordem"));
		obj.getConteudoUnidadePagina().getUnidadeConteudo().setPaginas(rs.getInt("uc.paginas"));

		obj.getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setCodigo(rs.getInt("conteudo.codigo"));
		obj.getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setDescricao(rs.getString("conteudo.descricao"));
		obj.getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setVersao(rs.getInt("conteudo.versao"));
		obj.getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setSituacaoConteudo(SituacaoConteudoEnum.valueOf(rs.getString("conteudo.situacaoconteudo")));
		obj.getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setUsoExclusivoProfessor(rs.getBoolean("conteudo.usoexclusivoprofessor"));

		obj.setNovoObj(false);
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarRegrasParaGatilhoConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaRecursoEducacionalVO obj, AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatricula, GestaoEventoConteudoTurmaAvaliacaoPBLVO autoAvaliacao, List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaAvalidos, UsuarioVO usuario) throws Exception {
		if (obj != null &&  obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.AVALIACAO_PBL)) {
			if (obj.getAutoAvaliacao()) {
				getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().verificarSeTodasNotasForamLancadasAvaliacaoPBLVisaoAluno(obj, autoAvaliacao, usuario);
				if (obj.getPermiteAlunoAvancarConteudoSemLancarNota() && ((obj.getUtilizarNotaConceito() && autoAvaliacao.getNotaConceitoAvaliacaoPBLVO().getCodigo().equals(0)) || (!obj.getUtilizarNotaConceito() && autoAvaliacao.getNota() == null))) {
					getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().atualizarSituacaoGestaoEventoConteudoTurmaAvaliacao(autoAvaliacao.getCodigo(), SituacaoPBLEnum.PENDENTE, false, usuario);
				}
			}
			if (obj.getAlunoAvaliaAluno()) {
				for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliado : listaAvalidos) {
					getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().verificarSeTodasNotasForamLancadasAvaliacaoPBLVisaoAluno(obj, avaliado, usuario);
					if (obj.getPermiteAlunoAvancarConteudoSemLancarNota() && ((obj.getUtilizarNotaConceito() && avaliado.getNotaConceitoAvaliacaoPBLVO().getCodigo().equals(0)) || (!obj.getUtilizarNotaConceito() && avaliado.getNota() == null))) {
						getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().atualizarSituacaoGestaoEventoConteudoTurmaAvaliacao(avaliado.getCodigo(), SituacaoPBLEnum.PENDENTE, false, usuario);
					}
				}
			}

		} else if (obj.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.ATA_PBL) && !obj.getPermiteAlunoAvancarConteudoSemLancarNota() && !obj.getGestaoEventoConteudoTurmaVO().getSituacao().isRealizado()) {
			throw new Exception(UteisJSF.internacionalizar("msg_GestaoEventoConteudoTurma_ataNaoPermiteAvancarConteudo").replace("{0}", obj.getTitulo()));
		} else if (obj.getTipoRecursoEducacional().isTipoAvaliacaoOnline() && !obj.getPermiteAlunoAvancarConteudoSemLancarNota() && avaliacaoOnlineMatricula.isAvaliacaoOnlineNaoRealizada()) {
			throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_existeAvaliacaoOnlineASeremResponidaNaoPermiteAvancarConteudoAvancoProximaPagina").replace("{0}", obj.getTitulo()));
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void removerImagemSlide(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, String imagem, UsuarioVO usuarioVO) throws Exception {
		if(Uteis.isAtributoPreenchido(imagem)) {
			try {
				
				String nomeImagem = imagem.substring(imagem.lastIndexOf("/")+1, imagem.length()).trim();
				int ordem = 0;
				int count = 0;
				StringBuilder nomeFisico = new StringBuilder(); 
				for(String nameImg: conteudoUnidadePaginaRecursoEducacionalVO.getNomeFisicoArquivo().split(",")) {
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
				for(String nameCaminho: conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseRepositorio().split(",")) {
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
				
				for(String nome: conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseRepositorio().split(",")) {
					if(ordem2 != ordem) {
						if(nomeReal.length()>0) {
							nomeReal.append(", ");
						}
						nomeReal.append(nome.trim());
					}
					ordem2++;
				}
				ArquivoHelper.delete(new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null).getLocalUploadArquivoFixo()+File.separator+caminhoImagem+File.separator+nomeImagem));
				conteudoUnidadePaginaRecursoEducacionalVO.setNomeFisicoArquivo(nomeFisico.toString().trim());				
				conteudoUnidadePaginaRecursoEducacionalVO.setCaminhoBaseRepositorio(caminho.toString().trim());
				conteudoUnidadePaginaRecursoEducacionalVO.setNomeRealArquivo(nomeReal.toString().trim());								
				conteudoUnidadePaginaRecursoEducacionalVO.setConteudoPaginaApresentar(null);
				conteudoUnidadePaginaRecursoEducacionalVO.setListaImagensSlide(null);
//				conteudoUnidadePaginaRecursoEducacionalVO.getConteudoPaginaApresentar(getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null), 350);
			
				if(Uteis.isAtributoPreenchido(conteudoUnidadePaginaRecursoEducacionalVO)) {
			final StringBuilder sql = new StringBuilder("UPDATE ConteudoUnidadePaginaRecursoEducacional SET ");
			sql.append(" nomeFisicoArquivo = ?, caminhoBaseRepositorio = ?, nomeRealArquivo = ? ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());					
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getNomeFisicoArquivo());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCaminhoBaseRepositorio());
					sqlAlterar.setString(x++, conteudoUnidadePaginaRecursoEducacionalVO.getNomeRealArquivo());
					sqlAlterar.setInt(x++, conteudoUnidadePaginaRecursoEducacionalVO.getCodigo());
					return sqlAlterar;
				}
			}) <= 0) {
				
				
			}
			conteudoUnidadePaginaRecursoEducacionalVO.setNovoObj(false);
				}
		} catch (Exception e) {
			throw e;
		}
		}
	}
}
