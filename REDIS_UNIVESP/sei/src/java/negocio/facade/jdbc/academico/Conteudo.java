package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.richfaces.event.FileUploadEvent;
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
import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.MomentoApresentacaoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.SituacaoConteudoEnum;
import negocio.comuns.academico.enumeradores.TipoConteudistaEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineQuestaoVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.QuestaoListaExercicioVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoOrigemEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ConteudoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Conteudo extends ControleAcesso implements ConteudoInterfaceFacade {

	/**
     * 
     */
	private static final long serialVersionUID = 4164750043900547048L;
	protected static String idEntidade;
	
	public Conteudo() throws Exception {
		super();
		setIdEntidade("Conteudo");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConteudoVO conteudo, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		validarDados(conteudo);
		if (conteudo.isNovoObj()) {
			conteudo.setDataCadastro(new Date());
			conteudo.getResponsavelCadastro().setCodigo(usuario.getCodigo());
			conteudo.getResponsavelCadastro().setNome(usuario.getNome());
			//conteudo.setVersao(consultarProximaVersao(conteudo.getDisciplina().getCodigo()));
			incluir(conteudo, controlarAcesso, usuario, realizandoClonagem);
		} else {
			conteudo.setDataAlteracao(new Date());
			conteudo.getResponsavelAlteracao().setCodigo(usuario.getCodigo());
			conteudo.getResponsavelAlteracao().setNome(usuario.getNome());
			alterar(conteudo, controlarAcesso, usuario, realizandoClonagem);
		}
	}

	public Integer consultarProximaVersao(Integer disciplina) {
		StringBuilder sb = new StringBuilder("SELECT count(codigo) as qtde from Conteudo ");
		sb.append(" where Conteudo.disciplina = ").append(disciplina);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return rs.getInt("qtde") + 1;
		}
		return 1;

	}

	public void validarDados(ConteudoVO conteudoVO) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();
		if (conteudoVO.getDescricao().trim().isEmpty()) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Conteudo_descricao"));
		}
		if (conteudoVO.getDisciplina().getCodigo() == null || conteudoVO.getDisciplina().getCodigo() == 0) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Conteudo_disciplina"));
		}
		
		if(conteudoVO.getUsoExclusivoProfessor()) {
			if(conteudoVO.getProfessor().getCodigo().equals(0)) {
				consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Conteudo_informarUmProfessor"));	
			}
		}

		if (conteudoVO.getSituacaoConteudo().equals(SituacaoConteudoEnum.ATIVO)) {
			for (UnidadeConteudoVO unidadeConteudoVO : conteudoVO.getUnidadeConteudoVOs()) {
//				if (unidadeConteudoVO.getConteudoUnidadePaginaVOs().isEmpty()) {
//					consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_UnidadeConteudo_pagina").replace("{0}", unidadeConteudoVO.getTitulo()));
//				}
				for (ConteudoUnidadePaginaVO conteudoUnidadePaginaVO : unidadeConteudoVO.getConteudoUnidadePaginaVOs()) {

					if (conteudoUnidadePaginaVO.getTipoRecursoEducacional() == null) {
						consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_conteudo"));
					}
					if ((conteudoUnidadePaginaVO.getTexto().trim().isEmpty() && (conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.TEXTO_HTML) || conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.VIDEO_URL)))) {
						consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_conteudo"));
					}
					if (conteudoUnidadePaginaVO.getNomeFisicoArquivo().trim().isEmpty() && conteudoUnidadePaginaVO.getTipoRecursoEducacional().getNecessitaUploadArquivo()) {
						consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_conteudo"));
					}
					if (conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO)) {
						if (conteudoUnidadePaginaVO.getValorGrafico().trim().isEmpty()) {
							getFacadeFactory().getConteudoUnidadePaginaFacade().realizarGeracaoGrafico(conteudoUnidadePaginaVO);
							if (conteudoUnidadePaginaVO.getValorGrafico().trim().isEmpty()) {
								consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_conteudo"));
							}
						}
					}
					if (conteudoUnidadePaginaVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.FORUM) && conteudoUnidadePaginaVO.getForum().isNovoObj() && Uteis.retiraTags(conteudoUnidadePaginaVO.getForum().getTema()).trim().isEmpty()) {
						consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_conteudo"));
					}
				}
			}
		}
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean validarDadosAlteracaoDisciplina(ConteudoVO conteudoVO, DisciplinaVO disciplina) throws Exception {
		boolean naoExisteReaVinculadoPorDiscipina = true;
		if (Uteis.isAtributoPreenchido(conteudoVO)) {
			boolean existeMatriculaTurmaComDisciplinaDiferente = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaExistePorConteudoPorDisciplinaDiferente(disciplina.getCodigo(), conteudoVO.getCodigo());
			if (existeMatriculaTurmaComDisciplinaDiferente) {
				throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_disciplinaAcessadaPorAluno"));
			}
		}
		forUnidadeConteudo: for (UnidadeConteudoVO unidadeConteudo : conteudoVO.getUnidadeConteudoVOs()) {
			for (ConteudoUnidadePaginaVO conteudoUnidadePagina : unidadeConteudo.getConteudoUnidadePaginaVOs()) {
				List<ConteudoUnidadePaginaRecursoEducacionalVO> lista = new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>();
				lista.addAll(conteudoUnidadePagina.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs());
				lista.addAll(conteudoUnidadePagina.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs());

				for (ConteudoUnidadePaginaRecursoEducacionalVO recursoEducacional : lista) {
					if ((recursoEducacional.getTipoRecursoEducacional().isTipoAvaliacaoOnline() && !recursoEducacional.getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo().equals(disciplina.getCodigo())) || (recursoEducacional.getTipoRecursoEducacional().isTipoRecursoExercicio() && !recursoEducacional.getListaExercicio().getDisciplina().getCodigo().equals(disciplina.getCodigo()))) {
						conteudoVO.setExisteReaComQuestaoParaSerClonado(true);
						naoExisteReaVinculadoPorDiscipina = false;
						break forUnidadeConteudo;
					}
					if ((recursoEducacional.getTipoRecursoEducacional().isTipoRecursoForum() && !recursoEducacional.getForum().getDisciplina().getCodigo().equals(disciplina.getCodigo()))) {
						naoExisteReaVinculadoPorDiscipina = false;
					}
				}
			}
		}
		
		return naoExisteReaVinculadoPorDiscipina;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ConteudoVO conteudoVO, Boolean controlarAcesso, final UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {

		try {
			
			Conteudo.incluir(getIdEntidade(), controlarAcesso, usuario);

			final StringBuilder sql = new StringBuilder("INSERT INTO Conteudo ");
			sql.append(" (descricao, versao, disciplina, dataCadastro, responsavelCadastro, ");
			sql.append(" tipoConteudista, nomeConteudista, emailConteudista, curriculumConteudista,");
			sql.append(" situacaoConteudo, conteudista, controlarTempo, controlarPonto , ");
			sql.append(" caminhoBaseBackground, nomeImagemBackground, corBackground, tamanhoImagemBackgroundConteudo , origemBackgroundConteudo, professor, usoexclusivoprofessor) ");
			sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			conteudoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(x++, conteudoVO.getDescricao());
					sqlInserir.setInt(x++, conteudoVO.getVersao());
					sqlInserir.setInt(x++, conteudoVO.getDisciplina().getCodigo());
					sqlInserir.setDate(x++, Uteis.getDataJDBC(conteudoVO.getDataCadastro()));
					if (!conteudoVO.getResponsavelCadastro().getCodigo().equals(0)) {
						sqlInserir.setInt(x++, conteudoVO.getResponsavelCadastro().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, conteudoVO.getTipoConteudista().name());
					sqlInserir.setString(x++, conteudoVO.getNomeConteudista());
					sqlInserir.setString(x++, conteudoVO.getEmailConteudista());
					sqlInserir.setString(x++, conteudoVO.getCurriculumConteudista());
					sqlInserir.setString(x++, conteudoVO.getSituacaoConteudo().toString());
					if (conteudoVO.getTipoConteudista().equals(TipoConteudistaEnum.INTERNO) && conteudoVO.getConteudista().getCodigo() > 0) {
						sqlInserir.setInt(x++, conteudoVO.getConteudista().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setBoolean(x++, conteudoVO.getControlarTempo());
					sqlInserir.setBoolean(x++, conteudoVO.getControlarPonto());
					sqlInserir.setString(x++, conteudoVO.getCaminhoBaseBackground());
					sqlInserir.setString(x++, conteudoVO.getNomeImagemBackground());
					sqlInserir.setString(x++, conteudoVO.getCorBackground());
					sqlInserir.setString(x++, conteudoVO.getTamanhoImagemBackgroundConteudo().name());
					sqlInserir.setString(x++, conteudoVO.getOrigemBackgroundConteudo().name());
					if(conteudoVO.getUsoExclusivoProfessor()) {
						sqlInserir.setInt(x++, conteudoVO.getProfessor().getCodigo());						
					} else {
						sqlInserir.setNull(x++, 0);						
					}
					sqlInserir.setBoolean(x++, conteudoVO.getUsoExclusivoProfessor());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						conteudoVO.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getUnidadeConteudoFacade().incluirUnidadeConteudo(conteudoVO, conteudoVO.getDisciplina(), false, usuario, realizandoClonagem);
		} catch (Exception e) {
			conteudoVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ConteudoVO conteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {

		try {
			
			conteudoVO.setResponsavelAlteracao(usuario);
			Conteudo.alterar(getIdEntidade(), controlarAcesso, usuario);

			final StringBuilder sql = new StringBuilder("UPDATE Conteudo set ");
			sql.append(" descricao = ?, versao = ?, disciplina = ?, dataAlteracao=?, responsavelAlteracao = ?, ");
			sql.append(" tipoConteudista = ?, nomeConteudista = ?, emailConteudista = ?, curriculumConteudista=?,");
			sql.append(" situacaoConteudo = ?, conteudista = ?, controlarTempo = ?, controlarPonto=?,  ");
			sql.append(" caminhoBaseBackground = ?, nomeImagemBackground = ?, corBackground = ?, tamanhoImagemBackgroundConteudo = ? , origemBackgroundConteudo = ?, professor = ?, usoExclusivoProfessor = ? ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(x++, conteudoVO.getDescricao());
					sqlAlterar.setInt(x++, conteudoVO.getVersao());
					sqlAlterar.setInt(x++, conteudoVO.getDisciplina().getCodigo());
					sqlAlterar.setDate(x++, Uteis.getDataJDBC(conteudoVO.getDataAlteracao()));
					if(conteudoVO.getResponsavelAlteracao().getCodigo() != 0) {
						sqlAlterar.setInt(x++, conteudoVO.getResponsavelAlteracao().getCodigo());						
					} else {
						sqlAlterar.setNull(x++, 0);						
					}
					sqlAlterar.setString(x++, conteudoVO.getTipoConteudista().name());
					sqlAlterar.setString(x++, conteudoVO.getNomeConteudista());
					sqlAlterar.setString(x++, conteudoVO.getEmailConteudista());
					sqlAlterar.setString(x++, conteudoVO.getCurriculumConteudista());
					sqlAlterar.setString(x++, conteudoVO.getSituacaoConteudo().toString());
					if (conteudoVO.getTipoConteudista().equals(TipoConteudistaEnum.INTERNO) && conteudoVO.getConteudista().getCodigo() > 0) {
						sqlAlterar.setInt(x++, conteudoVO.getConteudista().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setBoolean(x++, conteudoVO.getControlarTempo());
					sqlAlterar.setBoolean(x++, conteudoVO.getControlarPonto());
					sqlAlterar.setString(x++, conteudoVO.getCaminhoBaseBackground());
					sqlAlterar.setString(x++, conteudoVO.getNomeImagemBackground());
					sqlAlterar.setString(x++, conteudoVO.getCorBackground());
					sqlAlterar.setString(x++, conteudoVO.getTamanhoImagemBackgroundConteudo().name());
					sqlAlterar.setString(x++, conteudoVO.getOrigemBackgroundConteudo().name());
					if(conteudoVO.getUsoExclusivoProfessor()) {
						sqlAlterar.setInt(x++, conteudoVO.getProfessor().getCodigo());						
					} else {
						sqlAlterar.setNull(x++, 0);						
					}
					sqlAlterar.setBoolean(x++, conteudoVO.getUsoExclusivoProfessor());
					sqlAlterar.setInt(x++, conteudoVO.getCodigo());
					return sqlAlterar;
				}
			}) <= 0) {
				incluir(conteudoVO, controlarAcesso, usuario, realizandoClonagem);
				return;
			}
			getFacadeFactory().getUnidadeConteudoFacade().alterarUnidadeConteudo(conteudoVO, conteudoVO.getDisciplina(), false, usuario, realizandoClonagem);
		} catch (Exception e) {
			conteudoVO.setNovoObj(false);
			throw e;
		}
	}

	private String getSelectDadosBasicos() {
		StringBuilder sb = new StringBuilder("SELECT Conteudo.*, ");
		sb.append(" Disciplina.nome as \"Disciplina.nome\", Disciplina.cargaHoraria as \"Disciplina.cargaHoraria\", ");
		sb.append(" responsavelCadastro.nome as \"responsavelCadastro.nome\", responsavelAlteracao.nome as \"responsavelAlteracao.nome\", conteudista.nome as \"conteudista.nome\", ");
		sb.append(" professor.codigo as codigoprofessor, professor.nome as nomeprofessor from Conteudo ");
		sb.append(" inner join Disciplina on disciplina.codigo = conteudo.disciplina ");
		sb.append(" left join Usuario responsavelCadastro on responsavelCadastro.codigo = conteudo.responsavelCadastro ");
		sb.append(" left join Usuario responsavelAlteracao on responsavelAlteracao.codigo = conteudo.responsavelAlteracao ");
		sb.append(" left join pessoa conteudista on conteudista.codigo = conteudo.conteudista ");
		sb.append(" left join pessoa as professor on professor.codigo = conteudo.professor");
		return sb.toString();
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSelectConsultaRapidaConteudoComConteudoUnidadePaginaRecursoEducacional() {

		StringBuilder sb = new StringBuilder(" select conteudo.codigo as \"conteudo.codigo\" , conteudo.versao as \"conteudo.versao\" , conteudo.descricao as \"conteudo.descricao\", conteudo.situacaoConteudo as \"conteudo.situacaoConteudo\", ");
		sb.append(" conteudo.usoexclusivoprofessor as \"conteudo.usoexclusivoprofessor\",  ");
		sb.append(" disciplina.codigo as \"disciplina.codigo\",  disciplina.nome as \"disciplina.nome\", ");
		sb.append(" professor.codigo as \"professor.codigo\", professor.nome as \"professor.nome\", ");
		sb.append(" uc.codigo as \"uc.codigo\",  uc.ordem as \"uc.ordem\", uc.titulo as \"uc.titulo\", uc.caminhoBaseBackground as \"uc.caminhoBaseBackground\",  ");
		sb.append(" uc.nomeImagemBackground as \"uc.nomeImagemBackground\", uc.corBackground as \"uc.corBackground\", ");
		sb.append(" uc.tamanhoImagemBackgroundConteudo as \"uc.tamanhoImagemBackgroundConteudo\", uc.origemBackgroundConteudo as \"uc.origemBackgroundConteudo\", ");
		sb.append(" ((select sum(ponto) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = uc.codigo) )  as \"uc.ponto\", ");
		sb.append(" ((select sum(tempo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = uc.codigo ))  as \"uc.tempo\", ");
		sb.append(" ((select count(codigo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = uc.codigo))  as \"uc.paginas\", ");
		sb.append(" temaassunto.codigo as \"temaassunto.codigo\",temaassunto.nome as \"temaassunto.nome\", ");
		sb.append(" cup.codigo as \"cup.codigo\", cup.altura as \"cup.altura\", cup.largura as \"cup.largura\", cup.caminhoBaseRepositorio as \"cup.caminhoBaseRepositorio\", ");
		sb.append(" cup.nomeFisicoArquivo as \"cup.nomeFisicoArquivo\", cup.nomeRealArquivo as \"cup.nomeRealArquivo\", cup.texto as \"cup.texto\", cup.titulo as \"cup.titulo\", ");
		sb.append(" cup.caminhoBaseBackground as \"cup.caminhoBaseBackground\", cup.nomeImagemBackground as \"cup.nomeImagemBackground\", cup.corBackground as \"cup.corBackground\",  ");
		sb.append(" cup.tamanhoImagemBackgroundConteudo as \"cup.tamanhoImagemBackgroundConteudo\", cup.origemBackgroundConteudo as \"cup.origemBackgroundConteudo\", ");
		sb.append(" cupre.codigo as \"cupre.codigo\", cupre.tiporecursoeducacional as \"cupre.tiporecursoeducacional\", cupre.titulo as \"cupre.titulo\",  ");
		sb.append(" cupre.texto as \"cupre.texto\", cupre.descricao as \"cupre.descricao\", cupre.caminhobaserepositorio as \"cupre.caminhobaserepositorio\",  ");
		sb.append(" cupre.nomerealarquivo as \"cupre.nomerealarquivo\", cupre.nomefisicoarquivo as \"cupre.nomefisicoarquivo\",  ");
		sb.append(" cupre.recursoeducacional as \"cupre.recursoeducacional\", cupre.manterrecursodisponivelpagina as \"cupre.manterrecursodisponivelpagina\",   ");
		sb.append(" cupre.ordemapresentacao as \"cupre.ordemapresentacao\", cupre.momentoapresentacaorecursoeducacional as \"cupre.momentoapresentacaorecursoeducacional\",  ");
		sb.append(" cupre.requerliberacaoprofessor as \"cupre.requerliberacaoprofessor\",  ");
		sb.append(" cupre.altura as \"cupre.altura\", cupre.largura as \"cupre.largura\",  ");
		sb.append(" cupre.autoavaliacao as \"cupre.autoavaliacao\", cupre.alunoavaliaaluno as \"cupre.alunoavaliaaluno\", cupre.professoravaliaaluno as \"cupre.professoravaliaaluno\", cupre.formulacalculonotafinal as \"cupre.formulacalculonotafinal\",  ");
		sb.append(" cupre.utilizarnotaconceito as \"cupre.utilizarnotaconceito\", cupre.permitealunoavancarconteudosemlancarnota as \"cupre.permitealunoavancarconteudosemlancarnota\", cupre.faixaminimanotaautoavaliacao as \"cupre.faixaminimanotaautoavaliacao\",   ");
		sb.append(" cupre.faixamaximanotaautoavaliacao as \"cupre.faixamaximanotaautoavaliacao\", cupre.faixaminimanotaalunoavaliaaluno as \"cupre.faixaminimanotaalunoavaliaaluno\", cupre.faixamaximanotaalunoavaliaaluno as \"cupre.faixamaximanotaalunoavaliaaluno\",   ");
		sb.append(" cupre.faixaminimanotaprofessoravaliaaluno as \"cupre.faixaminimanotaprofessoravaliaaluno\", cupre.faixamaximanotaprofessoravaliaaluno as \"cupre.faixamaximanotaprofessoravaliaaluno\"   ");
		sb.append(" from conteudo  ");
		sb.append(" inner join disciplina  on disciplina.codigo = conteudo.disciplina ");
		sb.append(" inner join unidadeconteudo uc on uc.conteudo = conteudo.codigo ");
		sb.append(" inner join conteudounidadepagina cup on cup.unidadeconteudo = uc.codigo ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on cupre.conteudounidadepagina = cup.codigo ");
		sb.append(" left join pessoa as professor on professor.codigo = conteudo.professor ");
		sb.append(" left join temaassunto on temaassunto.codigo = uc.temaassunto ");
		return sb;
	}

	@Override
	public List<ConteudoVO> consultarConteudoPorCodigoDisciplina(Integer disciplina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("");
		sb.append(getSelectDadosBasicos());
		sb.append(" where Conteudo.disciplina = ").append(disciplina);
		sb.append(" and conteudo.situacaoconteudo = '").append(SituacaoConteudoEnum.ATIVO).append("'");
		if(usuario.getIsApresentarVisaoProfessor()) {
			sb.append(" and (conteudo.professor = ").append(usuario.getPessoa().getCodigo()).append(" or conteudo.professor is null)");
		}
		sb.append(" order by Conteudo.versao desc");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, controlarAcesso, usuario);
	}
	
	@Override
	public List<ConteudoVO> consultarConteudosAtivosPorCodigoDisciplinaUsoExclusivoProfessorFalso(Integer disciplina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("");
		sb.append(getSelectDadosBasicos());
		sb.append(" where Conteudo.disciplina = ").append(disciplina);
		sb.append(" and Conteudo.situacaoConteudo = '").append(SituacaoConteudoEnum.ATIVO.toString()).append("' ");
		sb.append(" and (conteudo.usoexclusivoprofessor = 'f' or conteudo.usoexclusivoprofessor is null)");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(rs, nivelMontarDados, controlarAcesso, usuario);
	}

	@Override
	public ConteudoVO consultarConteudoAtivoPorCodigoDisciplina(Integer disciplina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("");
		sb.append(getSelectDadosBasicos());
		sb.append(" where (Conteudo.disciplina = ").append(disciplina).append(" or Conteudo.disciplina IN (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina);
		sb.append(" )) and Conteudo.situacaoConteudo = '").append(SituacaoConteudoEnum.ATIVO.toString()).append("' ");
		sb.append(" order by Conteudo.versao desc limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, controlarAcesso, usuario);
		}
		return new ConteudoVO();
	}

	private List<ConteudoVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		List<ConteudoVO> conteudoVOs = new ArrayList<ConteudoVO>(0);
		while (rs.next()) {
			conteudoVOs.add(montarDados(rs, nivelMontarDados, controlarAcesso, usuario));
		}
		return conteudoVOs;
	}

	private ConteudoVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ConteudoVO conteudoVO = new ConteudoVO();
		conteudoVO.setNovoObj(false);
		conteudoVO.setCodigo(rs.getInt("codigo"));
		conteudoVO.setDescricao(rs.getString("descricao"));
		
		if (nivelMontarDados.equals(NivelMontarDados.COMBOBOX)) {
			return conteudoVO;
		}
		
		conteudoVO.setVersao(rs.getInt("versao"));
		conteudoVO.getDisciplina().setCodigo(rs.getInt("disciplina"));
		conteudoVO.getDisciplina().setNome(rs.getString("disciplina.nome"));
		conteudoVO.setDataCadastro(rs.getDate("dataCadastro"));
		conteudoVO.getResponsavelCadastro().setCodigo(rs.getInt("responsavelCadastro"));
		conteudoVO.getResponsavelCadastro().setNome(rs.getString("responsavelCadastro.nome"));
		conteudoVO.setDataAlteracao(rs.getDate("dataAlteracao"));
		conteudoVO.getResponsavelAlteracao().setCodigo(rs.getInt("responsavelAlteracao"));
		conteudoVO.getResponsavelAlteracao().setNome(rs.getString("responsavelAlteracao.nome"));
		conteudoVO.setSituacaoConteudo(SituacaoConteudoEnum.getEnum(rs.getString("situacaoConteudo")));
		conteudoVO.setTipoConteudista(TipoConteudistaEnum.valueOf(rs.getString("tipoConteudista")));
		conteudoVO.setNomeConteudista(rs.getString("nomeConteudista"));
		conteudoVO.setEmailConteudista(rs.getString("emailConteudista"));
		conteudoVO.setCurriculumConteudista(rs.getString("curriculumConteudista"));
		conteudoVO.getConteudista().setCodigo(rs.getInt("conteudista"));
		conteudoVO.getConteudista().setNome(rs.getString("conteudista.nome"));
		
		conteudoVO.setCaminhoBaseBackground(rs.getString("caminhoBaseBackground"));
		conteudoVO.setNomeImagemBackground(rs.getString("nomeImagemBackground"));
		conteudoVO.setCorBackground(rs.getString("corBackground"));
		if(Uteis.isAtributoPreenchido(rs.getInt("professor"))) {
			conteudoVO.getProfessor().setNome(rs.getString("nomeprofessor"));
			conteudoVO.getProfessor().setCodigo(rs.getInt("professor"));
		}
		conteudoVO.setUsoExclusivoProfessor(rs.getBoolean("usoexclusivoprofessor"));
		if (rs.getString("tamanhoImagemBackgroundConteudo") != null && !rs.getString("tamanhoImagemBackgroundConteudo").isEmpty()) {
			conteudoVO.setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum.valueOf(rs.getString("tamanhoImagemBackgroundConteudo")));
		} else {
			conteudoVO.setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO);
		}
		if (rs.getString("origemBackgroundConteudo") != null && !rs.getString("origemBackgroundConteudo").isEmpty()) {
			conteudoVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.valueOf(rs.getString("origemBackgroundConteudo")));
		} else {
			conteudoVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.SEM_BACKGROUND);
		}
		
		if (nivelMontarDados.equals(NivelMontarDados.TODOS)) {
			conteudoVO.setUnidadeConteudoVOs(getFacadeFactory().getUnidadeConteudoFacade().consultarUnidadeConteudoPorConteudo(conteudoVO.getCodigo(), 0, nivelMontarDados, controlarAcesso, usuario));
			if(conteudoVO.getUsoExclusivoProfessor()) {
				conteudoVO.setProfessor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(conteudoVO.getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));				
			}
			conteudoVO.setExisteAlunoCursandoConteudo(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaExistePorConteudo(conteudoVO.getCodigo()));
		}

		return conteudoVO;
	}

	@Override
	public ConteudoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(getSelectDadosBasicos());
		sb.append(" where Conteudo.codigo = ").append(codigo);
		sb.append(" order by Conteudo.versao desc");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, controlarAcesso, usuario);
		}
		return new ConteudoVO();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void ativarConteudo(final ConteudoVO conteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		SituacaoConteudoEnum situacaoAtual = conteudoVO.getSituacaoConteudo();
		try {
			validarDados(conteudoVO);
			Conteudo.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AtivarInativarConteudo", usuario);
			conteudoVO.setSituacaoConteudo(SituacaoConteudoEnum.ATIVO);
			alterar(conteudoVO, false, usuario, realizandoClonagem);
			getFacadeFactory().getListaExercicioFacade().atualizarSituacaoListasExerciciosPorConteudo(SituacaoListaExercicioEnum.ATIVA, conteudoVO, usuario);
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().atualizarSituacaoAvaliacaoOnlinePorConteudo(SituacaoEnum.ATIVO, conteudoVO, usuario);
			conteudoVO.setExisteAlunoCursandoConteudo(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaExistePorConteudo(conteudoVO.getCodigo()));
		} catch (Exception e) {
			conteudoVO.setSituacaoConteudo(situacaoAtual);
			throw e;
		}
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void emConstrucaoConteudo(final ConteudoVO conteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		SituacaoConteudoEnum situacaoAtual = conteudoVO.getSituacaoConteudo();
		try {
			validarDados(conteudoVO);
			Conteudo.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AtivarInativarConteudo", usuario);
			conteudoVO.setSituacaoConteudo(SituacaoConteudoEnum.EM_ELABORACAO);
			alterar(conteudoVO, false, usuario, realizandoClonagem);
			getFacadeFactory().getListaExercicioFacade().atualizarSituacaoListasExerciciosPorConteudo(SituacaoListaExercicioEnum.EM_ELABORACAO, conteudoVO, usuario);
			getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().atualizarSituacaoAvaliacaoOnlinePorConteudo(SituacaoEnum.EM_CONSTRUCAO, conteudoVO, usuario);
		} catch (Exception e) {
			conteudoVO.setSituacaoConteudo(situacaoAtual);
			throw e;
		}
	}	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inativarConteudo(ConteudoVO conteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		SituacaoConteudoEnum situacaoAtual = conteudoVO.getSituacaoConteudo();
		try {
			validarDados(conteudoVO);
			Conteudo.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AtivarInativarConteudo", usuario);
			conteudoVO.setSituacaoConteudo(SituacaoConteudoEnum.INATIVO);
			alterar(conteudoVO, false, usuario, realizandoClonagem);
		} catch (Exception e) {
			conteudoVO.setSituacaoConteudo(situacaoAtual);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarUnidadeConteudo(ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		validarDados(conteudoVO);
		getFacadeFactory().getUnidadeConteudoFacade().validarDados(unidadeConteudoVO);
		if (unidadeConteudoVO.getOrdem() != null && unidadeConteudoVO.getOrdem().intValue() > 0) {
			conteudoVO.getUnidadeConteudoVOs().set(unidadeConteudoVO.getOrdem() - 1, unidadeConteudoVO);
			return;
		}
		if(!conteudoVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND)){
			unidadeConteudoVO.setCorBackground(conteudoVO.getCorBackground());
			unidadeConteudoVO.setCaminhoBaseBackground(conteudoVO.getCaminhoBaseBackground());
			unidadeConteudoVO.setNomeImagemBackground(conteudoVO.getNomeImagemBackground());
			unidadeConteudoVO.setOrigemBackgroundConteudo(conteudoVO.getOrigemBackgroundConteudo());
			unidadeConteudoVO.setTamanhoImagemBackgroundConteudo(conteudoVO.getTamanhoImagemBackgroundConteudo());
		}		
		unidadeConteudoVO.setOrdem(conteudoVO.getUnidadeConteudoVOs().size() + 1);
		conteudoVO.getUnidadeConteudoVOs().add(unidadeConteudoVO);
		persistir(conteudoVO, controlarAcesso, usuario, realizandoClonagem);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirUnidadeConteudo(ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		Conteudo.excluir(getIdEntidade(), controlarAcesso, usuario);
		getFacadeFactory().getUnidadeConteudoFacade().excluirUnidadeConteudoEspecifico(unidadeConteudoVO);
		int index = 1;
		int indexRemover = 0;
		for (UnidadeConteudoVO obj : conteudoVO.getUnidadeConteudoVOs()) {
			if (obj.getCodigo().intValue() == unidadeConteudoVO.getCodigo().intValue()) {
				indexRemover = index - 1;
			} else {
				obj.setOrdem(index);
				index++;

			}
		}
		conteudoVO.getUnidadeConteudoVOs().remove(indexRemover);
		persistir(conteudoVO, controlarAcesso, usuario, realizandoClonagem);
	}

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
	public void alterarOrdemUnidadeConteudo(ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudo1, UnidadeConteudoVO unidadeConteudo2, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception {
		int ordem1 = unidadeConteudo1.getOrdem();
		int ordem2 = unidadeConteudo2.getOrdem();
		try {
			unidadeConteudo2.setOrdem(ordem1);
			unidadeConteudo1.setOrdem(ordem2);
			getFacadeFactory().getUnidadeConteudoFacade().persistir(unidadeConteudo1, conteudoVO.getDisciplina(), false, usuario, realizandoClonagem);
			getFacadeFactory().getUnidadeConteudoFacade().persistir(unidadeConteudo2, conteudoVO.getDisciplina(), false, usuario, realizandoClonagem);
			Ordenacao.ordenarLista(conteudoVO.getUnidadeConteudoVOs(), "ordem");
		} catch (Exception e) {
			unidadeConteudo2.setOrdem(ordem2);
			unidadeConteudo1.setOrdem(ordem1);
			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPaginaUnidadeConteudoParaOutraUnidadeConteudo(ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudo1, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, UsuarioVO usuario) throws Exception {
		UnidadeConteudoVO unidadeConteudo2 = null;
		int pagina = conteudoUnidadePaginaVO.getPagina();
		Boolean adicionado = false;
		Boolean removido = false;
		try {
			for (UnidadeConteudoVO unidadeConteudoVO : conteudoVO.getUnidadeConteudoVOs()) {
				if (unidadeConteudoVO.getCodigo().intValue() == conteudoUnidadePaginaVO.getUnidadeConteudo().getCodigo().intValue()) {
					unidadeConteudo2 = unidadeConteudoVO;
					break;
				}
			}
			conteudoUnidadePaginaVO.setUnidadeConteudo(unidadeConteudo1);
			conteudoUnidadePaginaVO.setPagina(unidadeConteudo1.getConteudoUnidadePaginaVOs().size() + 1);
			getFacadeFactory().getConteudoUnidadePaginaFacade().alterarNumeroPagina(conteudoUnidadePaginaVO, usuario);
			unidadeConteudo1.getConteudoUnidadePaginaVOs().add(conteudoUnidadePaginaVO);
			adicionado = true;

			unidadeConteudo2.getConteudoUnidadePaginaVOs().remove(pagina - 1);
			removido = true;
			int index = 1;
			for (ConteudoUnidadePaginaVO conteudoUnidadePaginaVO2 : unidadeConteudo2.getConteudoUnidadePaginaVOs()) {
				conteudoUnidadePaginaVO2.setPagina(index++);
				getFacadeFactory().getConteudoUnidadePaginaFacade().alterarNumeroPagina(conteudoUnidadePaginaVO, usuario);
			}
			unidadeConteudo1.setPaginas(unidadeConteudo1.getConteudoUnidadePaginaVOs().size());
			unidadeConteudo2.setPaginas(unidadeConteudo2.getConteudoUnidadePaginaVOs().size());
			Ordenacao.ordenarLista(unidadeConteudo1.getConteudoUnidadePaginaVOs(), "pagina");
			Ordenacao.ordenarLista(unidadeConteudo2.getConteudoUnidadePaginaVOs(), "pagina");
		} catch (Exception e) {
			conteudoUnidadePaginaVO.setPagina(pagina);
			conteudoUnidadePaginaVO.setUnidadeConteudo(unidadeConteudo2);
			if (adicionado) {
				unidadeConteudo1.getConteudoUnidadePaginaVOs().remove(unidadeConteudo1.getConteudoUnidadePaginaVOs().size() - 1);
			}
			if (removido) {
				unidadeConteudo2.getConteudoUnidadePaginaVOs().set(pagina - 1, conteudoUnidadePaginaVO);
			}
			throw e;
		}

	}

	@Override
	public void realizarGeracaoIndiceConteudo(ConteudoVO conteudoVO, Integer temaAssunto, String matricula, UsuarioVO usuario) throws Exception {
		if (conteudoVO.getUnidadeConteudoVOs().isEmpty()) {
			conteudoVO.setUnidadeConteudoVOs(getFacadeFactory().getUnidadeConteudoFacade().consultarUnidadeConteudoPorConteudo(conteudoVO.getCodigo(), temaAssunto, NivelMontarDados.TODOS, false, usuario));
		}
		Map<Integer, List<ConteudoUnidadePaginaVO>> conteudoUnidadePaginaVOs = getFacadeFactory().getConteudoUnidadePaginaFacade().consultarPorUnidadeConteudoPaginaPorConteudo(conteudoVO.getCodigo(), matricula, NivelMontarDados.BASICO, false, usuario);
		for (UnidadeConteudoVO unidadeConteudoVO : conteudoVO.getUnidadeConteudoVOs()) {
			unidadeConteudoVO.setExisteAnotacaoDisciplina(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().consultarExistenciaAnotacaoDisciplinaPorUnidadeConteudo(unidadeConteudoVO.getCodigo(), false, usuario));
			
			for (ConteudoUnidadePaginaVO conteudoUnidadePaginaVO : unidadeConteudoVO.getConteudoUnidadePaginaVOs()) {
				if(conteudoUnidadePaginaVOs.containsKey(unidadeConteudoVO.getCodigo())) {
					conteudoUnidadePaginaVO.setPaginaJaVisualizada(conteudoUnidadePaginaVOs.get(unidadeConteudoVO.getCodigo()).stream().anyMatch(t -> t.getCodigo().equals(conteudoUnidadePaginaVO.getCodigo()) && t.getPaginaJaVisualizada()));
				}
				if(conteudoUnidadePaginaVO.getPaginaJaVisualizada() && !conteudoUnidadePaginaVO.getNivelMontarDados().equals(NivelMontarDados.TODOS) && conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalManterDisponivelVOs().isEmpty()) {
					
					conteudoUnidadePaginaVO.setConteudoUnidadePaginaRecursoEducacionalAnteriorVOs(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(conteudoUnidadePaginaVO.getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.ANTES, NivelMontarDados.BASICO, false, usuario));
					conteudoUnidadePaginaVO.setConteudoUnidadePaginaRecursoEducacionalPosteriorVOs(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(conteudoUnidadePaginaVO.getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.DEPOIS, NivelMontarDados.BASICO, false, usuario));
					conteudoUnidadePaginaVO.setConteudoUnidadePaginaRecursoEducacionalApoioProfessor(getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarPorConteudoUnidadePagina(conteudoUnidadePaginaVO.getCodigo(), MomentoApresentacaoRecursoEducacionalEnum.APOIO_PROFESSOR, NivelMontarDados.BASICO, false, usuario));
					conteudoUnidadePaginaVO.setNivelMontarDados(NivelMontarDados.TODOS);
					
				}
				conteudoUnidadePaginaVO.setExisteAnotacaoDisciplina(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().consultarExistenciaAnotacaoDisciplinaPorUnidadeConteudoUnidadeConteudoPagina(unidadeConteudoVO.getCodigo(), conteudoUnidadePaginaVO.getCodigo(), false, usuario));
				conteudoUnidadePaginaVO.setUnidadeConteudo(unidadeConteudoVO);
			}
		}		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void uploadImagemBackgroundConteudo(ConteudoVO conteudoVO, FileUploadEvent uploadEvent, Boolean aplicarBackRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Boolean realizandoClonagem) throws Exception {


		String arquivo = "";
		if (conteudoVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.UNIDADE) && !conteudoVO.getNomeImagemBackground().trim().isEmpty() && !validarUsoDaImagemBackgroundNoConteudo(conteudoVO.getNomeImagemBackground(), usuarioVO)) {			
			arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + conteudoVO.getCaminhoBaseBackground() + File.separator + conteudoVO.getNomeImagemBackground();
			ArquivoHelper.delete(new File(arquivo));			
		}
		String extensao = uploadEvent.getUploadedFile().getName().substring(uploadEvent.getUploadedFile().getName().lastIndexOf("."), uploadEvent.getUploadedFile().getName().length());
		conteudoVO.setNomeImagemBackground(usuarioVO.getCodigo() + "_" + (new Date().getTime()) + extensao);
		conteudoVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.UNIDADE);
		conteudoVO.setCaminhoBaseBackground(PastaBaseArquivoEnum.EAD.getValue() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO.getValue() + File.separator + conteudoVO.getDisciplina().getCodigo() + File.separator + PastaBaseArquivoEnum.EAD_CONTEUDO_BACKGROUND.getValue());
		arquivo = conteudoVO.getCaminhoBaseBackground() + File.separator + conteudoVO.getNomeImagemBackground();
		ArquivoHelper.salvarArquivoNaPastaTemp(uploadEvent, conteudoVO.getNomeImagemBackground(), conteudoVO.getCaminhoBaseBackground(), configuracaoGeralSistemaVO, usuarioVO);
		if(!conteudoVO.isNovoObj()){
			alterarBackground(conteudoVO, aplicarBackRecursoEducacional, usuarioVO, realizandoClonagem);
		}
	
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarBackground(ConteudoVO conteudoVO,  Boolean aplicarBackRecursoEducacional, UsuarioVO usuarioVO, Boolean realizandoClonagem) throws Exception{
		if(!conteudoVO.isNovoObj()){
			alterar(conteudoVO, false, usuarioVO, realizandoClonagem);
		}
		getFacadeFactory().getUnidadeConteudoFacade().realizarReplicacaoBackgroundParaUnidade(conteudoVO.getUnidadeConteudoVOs(), 
				OrigemBackgroundConteudoEnum.CONTEUDO, conteudoVO.getCaminhoBaseBackground(), conteudoVO.getNomeImagemBackground(), conteudoVO.getCorBackground(), aplicarBackRecursoEducacional, 
				OrigemBackgroundConteudoEnum.CONTEUDO, conteudoVO.getTamanhoImagemBackgroundConteudo(), true);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerImagemBackgroundConteudo(ConteudoVO conteudoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Boolean realizandoClonagem)  throws Exception{
		if (conteudoVO.getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.PAGINA) && !validarUsoDaImagemBackgroundNoConteudo(conteudoVO.getNomeImagemBackground(), usuarioVO)) {
				String arquivo = configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + conteudoVO.getCaminhoBaseBackground() + File.separator + conteudoVO.getNomeImagemBackground();
				ArquivoHelper.delete(new File(arquivo));			
		} 
		conteudoVO.setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum.SEM_BACKGROUND);
		conteudoVO.setCaminhoBaseBackground("");
		conteudoVO.setNomeImagemBackground("");
		conteudoVO.setCorBackground("ffffff");
		alterarBackground(conteudoVO, false, usuarioVO, realizandoClonagem);		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public boolean validarUsoDaImagemBackgroundNoConteudo(String nomeImagem, UsuarioVO usuarioVO){
		StringBuilder sb = new StringBuilder(" select count(codigo) as qtd  from ( ");
		sb.append(" select codigo from conteudo where nomeimagembackground  = '").append(nomeImagem).append("' ");
		sb.append(" union all ");
		sb.append(" select codigo from unidadeconteudo  where nomeimagembackground ='").append(nomeImagem).append("' ");
		sb.append(" union all ");
		sb.append(" select codigo from conteudounidadepagina where nomeimagembackground  = '").append(nomeImagem).append("' ");
		sb.append(" union all ");
		sb.append(" select codigo from conteudounidadepaginarecursoeducacional where nomeimagembackground  = '").append(nomeImagem).append("' ");
		sb.append(" ) as t");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		int qtd = 0;
		if(rs.next()){
			qtd = rs.getInt("qtd");
		}
		return qtd > 1 ? true:false;
	}
	
	/**
	 * 
	 * @author Victor Hugo 16/12/2014
	 * 
	 * Método responsável por realizar os cálculos dos estudos on-line do aluno
	 * 
	 * @param auxiliar
	 * @param conteudoVO
	 * @param matricula
	 * @param codigoMatriculaPeriodoTurmaDisciplina
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public void gerarCalculosDesempenhoAlunoEstudosOnline(Map<String, Object> auxiliar, Integer codigoConteudo, String matricula, Integer codigoMatriculaPeriodoTurmaDisciplina, ModalidadeDisciplinaEnum modalidadeDisciplinaEnum, UsuarioVO usuarioVO) throws Exception {
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = null;
		Double pontoTotal = consultarPontuacaoTotalConteudo(codigoConteudo);
		if(pontoTotal.equals(0.0)) {
			pontoTotal = consultarQuantidadePaginasConteudo(codigoConteudo, usuarioVO).doubleValue();
		}
		if(!pontoTotal.equals(0.0)) {
			Double totalPontosAtingidos = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarTotalPontosAlunoAtingiuConteudo(matricula, codigoMatriculaPeriodoTurmaDisciplina, codigoConteudo);
			
			if (totalPontosAtingidos == 0.0) {
				totalPontosAtingidos = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarQuantidadePaginasAcessou(matricula, codigoMatriculaPeriodoTurmaDisciplina, codigoConteudo).doubleValue();
			}
			if (totalPontosAtingidos == 0.0) {
				auxiliar.put("percentEstudado", 0.0);
				auxiliar.put("percentARealizar", 0.0);
				auxiliar.put("percentAtrasado", 0.0);
			} else {
				auxiliar.put("percentEstudado", Uteis.arrendondarForcando2CadasDecimais(totalPontosAtingidos * 100) / pontoTotal);
				auxiliar.put("percentARealizar", (100 - (Double) auxiliar.get("percentEstudado")));
				if (modalidadeDisciplinaEnum.equals(ModalidadeDisciplinaEnum.ON_LINE)) {
					auxiliar.put("percentAtrasado", 0.0);
					calendarioAtividadeMatriculaVO = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividade(codigoMatriculaPeriodoTurmaDisciplina, TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO, TipoOrigemEnum.NENHUM, "", Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
					Long diasEntreInicioEFim = Uteis.getDataQuantidadeDiasEntreDatasDesconsiderandoDiasUteis(calendarioAtividadeMatriculaVO.getDataInicio(), calendarioAtividadeMatriculaVO.getDataFim());
					Long diasEntreDataInicioEstudoEDataAtual = null;
					if (calendarioAtividadeMatriculaVO.getDataFim().compareTo(new Date()) < 0) {
						diasEntreDataInicioEstudoEDataAtual = Uteis.getDataQuantidadeDiasEntreDatasDesconsiderandoDiasUteis(calendarioAtividadeMatriculaVO.getDataInicio(), calendarioAtividadeMatriculaVO.getDataFim());
					} else {
						diasEntreDataInicioEstudoEDataAtual = Uteis.getDataQuantidadeDiasEntreDatasDesconsiderandoDiasUteis(calendarioAtividadeMatriculaVO.getDataInicio(), new Date());
					}
					auxiliar.put("pontosPorDiaAAtingir", (pontoTotal / diasEntreInicioEFim));
					auxiliar.put("percentEsperadoAtingir", Uteis.arrendondarForcando2CadasDecimais((((Double) auxiliar.get("pontosPorDiaAAtingir") * diasEntreDataInicioEstudoEDataAtual / pontoTotal) * 100)));
					if (((Double) auxiliar.get("pontosPorDiaAAtingir") * diasEntreDataInicioEstudoEDataAtual > totalPontosAtingidos)) {
						auxiliar.put("pontosAtrasados", ((Double) auxiliar.get("pontosPorDiaAAtingir") * diasEntreDataInicioEstudoEDataAtual - totalPontosAtingidos));
						auxiliar.put("percentAtrasado", ((Double) auxiliar.get("pontosAtrasados") * pontoTotal) / 100);
						auxiliar.put("percentARealizar", (Double) auxiliar.get("percentARealizar") - (Double) auxiliar.get("percentAtrasado"));
					}
				}
			}
		} else {	
			auxiliar.put("percentEstudado", 0.0);
			auxiliar.put("percentARealizar", 0.0);
			auxiliar.put("percentAtrasado", 0.0);
		}
	}
	
	@Override
	public Double consultarPontuacaoTotalConteudo(Integer codigoConteudo) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		
		sqlStr.append("select sum(ponto) as pontototal from conteudounidadepagina");
		sqlStr.append(" inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo");
		sqlStr.append(" inner join conteudo on unidadeconteudo.conteudo = conteudo.codigo");
		sqlStr.append(" where conteudo.codigo = ").append(codigoConteudo);
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		if(rs.next()) {
			return rs.getDouble("pontototal");
		}
		return 0.0;
	}
	
	@Override
	public List<ConteudoVO> consultar(String campoConsulta, String valorConsultar, String situacaoConteudo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {		
		if (!(valorConsultar.equals(""))) {
			if (campoConsulta.equals("codigoDisciplina")) {
				Integer codigoDisciplina = Integer.parseInt(Uteis.removeCaractersEspeciais(valorConsultar));
				return consultarConteudoPorCodigoDisciplina(codigoDisciplina, situacaoConteudo, nivelMontarDados, false, usuarioVO);				
			} else if (campoConsulta.equals("disciplina")) {
				return consultarPorNomeDisciplina(Uteis.removeCaractersEspeciais(valorConsultar), situacaoConteudo, nivelMontarDados, usuarioVO);
			} else if (campoConsulta.equals("descricao")) {
				return consultarPorDescricaoConteudo(Uteis.removeCaractersEspeciais(valorConsultar), situacaoConteudo, nivelMontarDados, usuarioVO);
			} else if (campoConsulta.equals("codigoConteudo")) {
				Integer codigoConteudo = Integer.parseInt(Uteis.removeCaractersEspeciais(valorConsultar));
				List<ConteudoVO> conteudoVOs = new ArrayList<ConteudoVO>();
				ConteudoVO conteudoVO = consultarPorChavePrimaria(codigoConteudo, nivelMontarDados, false, usuarioVO);
				if(!conteudoVO.getCodigo().equals(0)) {
					conteudoVOs.add(consultarPorChavePrimaria(codigoConteudo, nivelMontarDados, false, usuarioVO));					
				}
				return conteudoVOs;				
			}
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_dados_parametroConsulta"));
		}
		return new ArrayList<ConteudoVO>();
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConteudoVO> consultarPorNomeDisciplina(String valorConsulta, String situacaoConteudo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosBasicos());
		sb.append(" where upper(sem_acentos(disciplina.nome)) like(sem_acentos('" + valorConsulta.toUpperCase() + "%'))");
		if (!situacaoConteudo.equals("TODOS")) {
			sb.append(" and conteudo.situacaoconteudo = '").append(situacaoConteudo).append("'");
		}
		sb.append(" ORDER BY Conteudo.codigo");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(rs, nivelMontarDados, false, usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConteudoVO> consultarPorDescricaoConteudo(String valorConsulta, String situacaoConteudo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosBasicos());
		sb.append(" where upper(sem_acentos(Conteudo.descricao)) like(sem_acentos('" + valorConsulta.toUpperCase() + "%'))");
		if (!situacaoConteudo.equals("TODOS")) {
			sb.append(" and conteudo.situacaoconteudo = '").append(situacaoConteudo).append("'");
		}
		sb.append(" ORDER BY Conteudo.codigo");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(rs, nivelMontarDados, false, usuarioLogado);
	}
	
	@Override
	public Integer consultarCodigoConteudoAtivoTurmaDisciplinaConteudoPorCodigoTurmaDisciplinaAnoSemestre(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre) throws Exception {
		
		StringBuilder sqlStr = new StringBuilder();
		
		sqlStr.append(" select turmadisciplinaconteudo.conteudo as conteudo from turmadisciplinaconteudo");
		sqlStr.append(" where turmadisciplinaconteudo.turma = ").append(codigoTurma);
		sqlStr.append(" and turmadisciplinaconteudo.disciplina = ").append(codigoDisciplina);
		sqlStr.append(" and turmadisciplinaconteudo.ano ||'/'||turmadisciplinaconteudo.semestre <= ").append("'"+ano+"/"+semestre+"' ");
		sqlStr.append(" order by turmadisciplinaconteudo.ano ||'/'||turmadisciplinaconteudo.semestre desc limit 1");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		Integer codigoConteudo = 0;
		
		if(rs.next()) {
			codigoConteudo = rs.getInt("conteudo");
		}
		
		return codigoConteudo;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ConteudoVO clonarConteudoVO(ConteudoVO conteudoVO, UsuarioVO usuarioVO) throws Exception {
		ConteudoVO clone = conteudoVO.clone();
		clone.setVersao(consultarUltimoNumeroVersaoPorDisciplina(clone.getDisciplina().getCodigo()));
		persistir(clone, false, usuarioVO, true);
		return clone;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarUltimoNumeroVersaoPorDisciplina(Integer codigoDisciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select max(versao) as versao from conteudo");
		sqlStr.append(" where disciplina = ").append(codigoDisciplina);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		Integer codigoConteudo = 0;
		if (rs.next()) {
			codigoConteudo = rs.getInt("versao");
		}
		return codigoConteudo + 1;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConteudoVO> consultarPorCodigoProfessor(Integer codigoProfessor, Integer codigoDisciplina, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosBasicos());
		sb.append(" where (conteudo.professor = ").append(codigoProfessor).append(" or conteudo.professor is null)");
		sb.append(" and conteudo.disciplina = ").append(codigoDisciplina);
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		return montarDadosConsulta(rs, nivelMontarDados, false, usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConteudoVO> consultarConteudosDiferentesDeInativos(Integer codigoProfessor, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {		
		StringBuilder sb = new StringBuilder(getSelectDadosBasicos());
		sb.append(" where conteudo.situacaoconteudo != 'INATIVO' ");
		if(usuarioLogado.getIsApresentarVisaoProfessor()) {
			sb.append("and (conteudo.professor = ").append(codigoProfessor).append(" or conteudo.professor is null) ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(rs, nivelMontarDados, false, usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarQuantidadePaginasConteudo(Integer codigoConteudo, UsuarioVO usuarioLogado) throws Exception {		
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(conteudounidadepagina.codigo) as qdte from  conteudounidadepagina ");
		sb.append(" inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo");
		sb.append(" inner join conteudo on conteudo.codigo = unidadeconteudo.conteudo");
		sb.append(" where conteudo.codigo = ").append(codigoConteudo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		Integer qtd = 0;
		if(rs.next()) {
			qtd = rs.getInt("qdte");
		}
		return qtd;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConteudoVO> consultarPorCodigoProfessorNivelFuncionalidades(Integer codigoProfessor, boolean permitirProfessorCadastrarConteudoQualquerDisciplina, boolean permitirProfessorCadastrarConteudoApenasAulasProgramadas, boolean permitirProfessorCadastrarApenasConteudosExclusivos, Integer codigoDisciplina, String situacaoConteudo, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosBasicos());
		if(permitirProfessorCadastrarConteudoQualquerDisciplina) {
			sb.append(" WHERE conteudo.disciplina = ").append(codigoDisciplina);
		} else if (permitirProfessorCadastrarApenasConteudosExclusivos) {
			sb.append(" WHERE conteudo.disciplina = ").append(codigoDisciplina);
			sb.append(" AND conteudo.professor = ").append(codigoProfessor);
			sb.append(" AND conteudo.usoExclusivoProfessor = '").append("t").append("'");
		} else if (permitirProfessorCadastrarConteudoApenasAulasProgramadas) {
			sb.append(" WHERE conteudo.disciplina = ").append(codigoDisciplina);
		} else {
			sb.append(" WHERE conteudo.disciplina = ").append(codigoDisciplina);
		}
		if (!situacaoConteudo.equals("TODOS")) {
			sb.append(" AND conteudo.situacaoconteudo = '").append(situacaoConteudo).append("'");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(rs, nivelMontarDados, false, usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConteudoVO> consultarPorCodigoDisciplinaTurmaAnoSemestre(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, NivelMontarDados nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosBasicos());
		sb.append(" INNER JOIN turmadisciplinaconteudo on turmadisciplinaconteudo.conteudo = conteudo.codigo");
		sb.append(" AND turmadisciplinaconteudo.turma = ").append(codigoTurma);
		sb.append(" AND turmadisciplinaconteudo.disciplina = ").append(codigoDisciplina);
		sb.append(" AND turmadisciplinaconteudo.ano = '").append(ano).append("'");
		sb.append(" AND turmadisciplinaconteudo.semestre = '").append(semestre).append("'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(rs, nivelMontarDados, false, usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ConteudoVO> consultarConteudoPorCodigoDisciplina(Integer disciplina, String situacaoConteudo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("");
		sb.append(getSelectDadosBasicos());
		sb.append(" where Conteudo.disciplina = ").append(disciplina);
		if (!situacaoConteudo.equals("TODOS")) {
			sb.append(" and conteudo.situacaoconteudo = '").append(situacaoConteudo).append("'");
		}
		if(usuario.getIsApresentarVisaoProfessor()) {
			sb.append(" and (conteudo.professor = ").append(usuario.getPessoa().getCodigo()).append(" or conteudo.professor is null)");
		}
		sb.append(" order by Conteudo.versao desc");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, controlarAcesso, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ConteudoVO consultarPorGestaoEventoConteudoTurma(GestaoEventoConteudoTurmaVO obj, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select conteudo.codigo as \"conteudo.codigo\", conteudo.descricao as \"conteudo.descricao\", conteudo.versao as \"conteudo.versao\", conteudo.disciplina \"conteudo.disciplina\",");
		sb.append(" unidadeConteudo.codigo as \"unidadeConteudo.codigo\", unidadeConteudo.ordem as \"unidadeConteudo.ordem\", unidadeConteudo.titulo as \"unidadeConteudo.titulo\", ");
		sb.append(" temaassunto.codigo as \"temaassunto.codigo\", temaassunto.nome as \"temaassunto.nome\", ");
		sb.append(" conteudounidadepagina.codigo as \"conteudounidadepagina.codigo\", conteudounidadepagina.pagina as \"conteudounidadepagina.pagina\", conteudounidadepagina.texto as \"conteudounidadepagina.texto\",conteudounidadepagina.titulo as \"conteudounidadepagina.titulo\", ");
		sb.append(" cupre.codigo as \"cupre.codigo\", cupre.descricao as \"cupre.descricao\", cupre.momentoapresentacaorecursoeducacional as \"cupre.momentoapresentacaorecursoeducacional\", cupre.ordemapresentacao as \"cupre.ordemapresentacao\", cupre.texto as \"cupre.texto\", ");
		sb.append(" cupre.titulo as \"cupre.titulo\", cupre.tiporecursoeducacional as \"cupre.tiporecursoeducacional\", cupre.requerliberacaoprofessor as \"cupre.requerliberacaoprofessor\", ");
		sb.append(" gect.situacao as \"gect.situacao\" ");
		sb.append(" from conteudo ");
		sb.append(" inner join unidadeconteudo on unidadeconteudo.conteudo = conteudo.codigo ");
		sb.append(" inner join conteudounidadepagina on conteudounidadepagina.unidadeconteudo = unidadeconteudo.codigo ");
		sb.append(" left join conteudoUnidadePaginaRecursoEducacional as cupre on cupre.conteudounidadepagina = conteudounidadepagina.codigo ");
		sb.append(" left join  temaassunto on temaassunto.codigo = unidadeConteudo.temaassunto ");
		sb.append(" left join gestaoeventoconteudoturma as gect on gect.conteudounidadepaginarecursoeducacional = cupre.codigo");
		sb.append(" and gect.turma = ").append(obj.getTurmaVO().getCodigo());
		sb.append(" and gect.disciplina = ").append(obj.getDisciplinaVO().getCodigo());
		sb.append(" and gect.ano = '").append(obj.getAno()).append("' ");
		sb.append(" and gect.semestre = '").append(obj.getSemestre()).append("' ");		
		sb.append(" where conteudo.codigo = ").append(obj.getConteudoVO().getCodigo());
		sb.append(" order by unidadeConteudo.ordem, conteudounidadepagina.pagina, cupre.ordemapresentacao  ");


		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConteudoComSuborinadas(rs, nivelMontarDados, controlarAcesso, usuario);

	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ConteudoVO montarDadosConteudoComSuborinadas(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ConteudoVO conteudoVO = new ConteudoVO();
		while (rs.next()) {
			if (!Uteis.isAtributoPreenchido(conteudoVO.getCodigo())) {
				conteudoVO.setNovoObj(false);
				conteudoVO.setCodigo(rs.getInt("conteudo.codigo"));
				conteudoVO.setDescricao(rs.getString("conteudo.descricao"));
				conteudoVO.setVersao(rs.getInt("conteudo.versao"));
				conteudoVO.getDisciplina().setCodigo(rs.getInt("conteudo.disciplina"));
			}
			UnidadeConteudoVO unidade = consultarUnidadeConteudoVO(rs.getInt("unidadeConteudo.codigo"), conteudoVO);
			if (!Uteis.isAtributoPreenchido(unidade.getCodigo())) {
				unidade.setCodigo(rs.getInt("unidadeConteudo.codigo"));
				unidade.setOrdem(rs.getInt("unidadeConteudo.ordem"));
				unidade.setTitulo(rs.getString("unidadeConteudo.titulo"));
				if(Uteis.isAtributoPreenchido(rs.getString("temaassunto.codigo"))){
					unidade.getTemaAssuntoVO().setCodigo(rs.getInt("temaassunto.codigo"));
					unidade.getTemaAssuntoVO().setNome(rs.getString("temaassunto.nome"));
				}
			}
			ConteudoUnidadePaginaVO unidadePagina = consultarConteudoUnidadePaginaVO(rs.getInt("conteudounidadepagina.codigo"), unidade);
			if (!Uteis.isAtributoPreenchido(unidadePagina.getCodigo())) {
				unidadePagina.setNovoObj(false);
				unidadePagina.setCodigo(rs.getInt("conteudounidadepagina.codigo"));
				unidadePagina.setPagina(rs.getInt("conteudounidadepagina.pagina"));
				unidadePagina.setTexto(rs.getString("conteudounidadepagina.texto"));
				unidadePagina.setTitulo(rs.getString("conteudounidadepagina.titulo"));
			}

			ConteudoUnidadePaginaRecursoEducacionalVO cupre = new ConteudoUnidadePaginaRecursoEducacionalVO();
			if(Uteis.isAtributoPreenchido(rs.getString("cupre.codigo"))){
				cupre.setCodigo(rs.getInt("cupre.codigo"));
				cupre.setDescricao(rs.getString("cupre.descricao"));
				cupre.setMomentoApresentacaoRecursoEducacional(MomentoApresentacaoRecursoEducacionalEnum.valueOf(rs.getString("cupre.momentoApresentacaoRecursoEducacional")));
				cupre.setOrdemApresentacao(rs.getInt("cupre.ordemApresentacao"));
				cupre.setTexto(rs.getString("cupre.texto"));
				cupre.setTitulo(rs.getString("cupre.titulo"));
				cupre.setTipoRecursoEducacional(TipoRecursoEducacionalEnum.valueOf(rs.getString("cupre.tipoRecursoEducacional")));
				cupre.setRequerLiberacaoProfessor(rs.getBoolean("cupre.requerLiberacaoProfessor"));
				if(Uteis.isAtributoPreenchido(rs.getString("gect.situacao"))){
					cupre.setSituacaoGestaoEventoConteudoTurma(SituacaoPBLEnum.valueOf(rs.getString("gect.situacao")));	
				}
				cupre.setNovoObj(false);
				addConteudoUnidadePaginaRecursoEducacionalVO(cupre, unidadePagina);
			}
			addConteudoUnidadePaginaVO(unidadePagina, unidade);
			addUnidadeConteudoVO(unidade, conteudoVO);
		}
		return conteudoVO;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private UnidadeConteudoVO consultarUnidadeConteudoVO(Integer codigoUnidadeConteudo, ConteudoVO conteudoVO) {
		for (UnidadeConteudoVO objsExistente : conteudoVO.getUnidadeConteudoVOs()) {
			if (objsExistente.getCodigo().equals(codigoUnidadeConteudo)) {
				return objsExistente;
			}
		}
		return new UnidadeConteudoVO();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void addUnidadeConteudoVO(UnidadeConteudoVO obj, ConteudoVO conteudoVO) {
		obj.setConteudo(conteudoVO);
		int index = 0;
		for (UnidadeConteudoVO objsExistente : conteudoVO.getUnidadeConteudoVOs()) {
			if (objsExistente.getCodigo().equals(obj.getCodigo())) {
				conteudoVO.getUnidadeConteudoVOs().set(index, obj);
				return;
			}
			index++;
		}
		conteudoVO.getUnidadeConteudoVOs().add(obj);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private ConteudoUnidadePaginaVO consultarConteudoUnidadePaginaVO(Integer codConteudoUnidadePagina, UnidadeConteudoVO unidade) {
		for (ConteudoUnidadePaginaVO objsExistente : unidade.getConteudoUnidadePaginaVOs()) {
			if (objsExistente.getCodigo().equals(codConteudoUnidadePagina)) {
				return objsExistente;
			}
		}
		return new ConteudoUnidadePaginaVO();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void addConteudoUnidadePaginaVO(ConteudoUnidadePaginaVO obj, UnidadeConteudoVO unidade) {
		obj.setUnidadeConteudo(unidade);
		int index = 0;
		for (ConteudoUnidadePaginaVO objsExistente : unidade.getConteudoUnidadePaginaVOs()) {
			if (objsExistente.getCodigo().equals(obj.getCodigo())) {
				unidade.getConteudoUnidadePaginaVOs().set(index, obj);
				return;
			}
			index++;
		}
		unidade.getConteudoUnidadePaginaVOs().add(obj);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void addConteudoUnidadePaginaRecursoEducacionalVO(ConteudoUnidadePaginaRecursoEducacionalVO obj, ConteudoUnidadePaginaVO unidadePagina) {
		obj.setConteudoUnidadePagina(unidadePagina);
		if (obj.getMomentoApresentacaoRecursoEducacional().isAntes()) {
			unidadePagina.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().add(obj);
		} else if (obj.getMomentoApresentacaoRecursoEducacional().isDepois()) {
			unidadePagina.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().add(obj);
		} else {
			unidadePagina.getConteudoUnidadePaginaRecursoEducacionalApoioProfessor().add(obj);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarClonagemReaPorSelecaoDisciplina(ConteudoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		if (obj.isClonarReasOutraDisciplina()) {
			realizarClonagemReaOutraDisciplina(obj, usuarioLogado);
		} else if (obj.isDescartarReasOutraDisciplina()){
			for (UnidadeConteudoVO unidadeConteudo : obj.getUnidadeConteudoVOs()) {
				for (ConteudoUnidadePaginaVO conteudoUnidadePagina : unidadeConteudo.getConteudoUnidadePaginaVOs()) {	
					Iterator<ConteudoUnidadePaginaRecursoEducacionalVO> i= conteudoUnidadePagina.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs().iterator();
					while (i.hasNext()) {
						ConteudoUnidadePaginaRecursoEducacionalVO cupre = (ConteudoUnidadePaginaRecursoEducacionalVO) i.next();
						if (cupre.getTipoRecursoEducacional().isTipoRecursoForum() || cupre.getTipoRecursoEducacional().isTipoAvaliacaoOnline() || cupre.getTipoRecursoEducacional().isTipoRecursoExercicio()) {							
							i.remove();
						}
					}
					Iterator<ConteudoUnidadePaginaRecursoEducacionalVO> j= conteudoUnidadePagina.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs().iterator();
					while (j.hasNext()) {
						ConteudoUnidadePaginaRecursoEducacionalVO cupre = (ConteudoUnidadePaginaRecursoEducacionalVO) j.next();
						if (cupre.getTipoRecursoEducacional().isTipoRecursoForum() || cupre.getTipoRecursoEducacional().isTipoAvaliacaoOnline() || cupre.getTipoRecursoEducacional().isTipoRecursoExercicio()) {							
							j.remove();
						}
					}
					getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().excluirConteudoUnidadePaginaRecursoEducacional(conteudoUnidadePagina, configuracaoGeralSistemaVO, false, usuarioLogado);
				}
			}
		}
		obj.setVersao(consultarUltimoNumeroVersaoPorDisciplina(obj.getDisciplina().getCodigo()));
		StringBuilder sb = new StringBuilder("");
		sb.append("UPDATE conteudo set versao = ").append(obj.getVersao()).append(", disciplina =  ").append(obj.getDisciplina().getCodigo());
		sb.append(" where codigo =  ").append(obj.getCodigo());
		getConexao().getJdbcTemplate().update(sb.toString());
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void realizarClonagemReaOutraDisciplina(ConteudoVO obj, UsuarioVO usuarioLogado) throws Exception {
		for (UnidadeConteudoVO unidadeConteudo : obj.getUnidadeConteudoVOs()) {
			for (ConteudoUnidadePaginaVO conteudoUnidadePagina : unidadeConteudo.getConteudoUnidadePaginaVOs()) {
				List<ConteudoUnidadePaginaRecursoEducacionalVO> lista = new ArrayList<ConteudoUnidadePaginaRecursoEducacionalVO>();
				lista.addAll(conteudoUnidadePagina.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs());
				lista.addAll(conteudoUnidadePagina.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs());
				for (ConteudoUnidadePaginaRecursoEducacionalVO cupre : lista) {
					if (cupre.getTipoRecursoEducacional().isTipoRecursoForum()) {
						cupre.getForum().setDisciplina(obj.getDisciplina());
						getFacadeFactory().getForumFacade().persistir(cupre.getForum(), "Forum", false, usuarioLogado);
					} else if (cupre.getTipoRecursoEducacional().isTipoAvaliacaoOnline()) {
						realizarClonagemReaAvaliacaoOnlineOutraDisciplina(obj, cupre, usuarioLogado);						
					} else if (cupre.getTipoRecursoEducacional().isTipoRecursoExercicio()) {
						realizarClonagemReaExercicioOutraDisciplina(obj, cupre, usuarioLogado);
					}
				}
			}
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void realizarClonagemReaAvaliacaoOnlineOutraDisciplina(ConteudoVO obj, ConteudoUnidadePaginaRecursoEducacionalVO cupre, UsuarioVO usuarioLogado) throws Exception {
		if (obj.isClonarQuestaoOnline()) {
			List<QuestaoVO> questaoVOs =  getFacadeFactory().getQuestaoFacade().consultarQuestaoParaClonagemReaOutraDisciplina(obj, cupre);
			for (QuestaoVO questaoVO : questaoVOs) {
				getFacadeFactory().getQuestaoFacade().realizarClonagemReaOutraDisciplinaQuestao(obj, questaoVO, false, usuarioLogado);
			}
		}
		cupre.getAvaliacaoOnlineVO().setDisciplinaVO(obj.getDisciplina());
		for (AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestao : cupre.getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs()) {
			QuestaoVO questaoExistente = getFacadeFactory().getQuestaoFacade().consultarQuestaoExistenteParaClonagemPorCodigoQuestaoPorDisciplina(avaliacaoOnlineQuestao.getQuestaoVO().getCodigo(), obj.getDisciplina().getCodigo(), true, false);
			if(Uteis.isAtributoPreenchido(questaoExistente)){
				avaliacaoOnlineQuestao.setQuestaoVO(questaoExistente);
			}else{
				avaliacaoOnlineQuestao.setQuestaoVO(getFacadeFactory().getQuestaoFacade().realizarClonagemReaOutraDisciplinaQuestao(obj, avaliacaoOnlineQuestao.getQuestaoVO(), true, usuarioLogado));
			}
		}
		getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().persistir(cupre.getAvaliacaoOnlineVO(), false, usuarioLogado);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void realizarClonagemReaExercicioOutraDisciplina(ConteudoVO obj, ConteudoUnidadePaginaRecursoEducacionalVO cupre, UsuarioVO usuarioLogado) throws Exception {
		if (obj.isClonarQuestaoExercicio()) {
			List<QuestaoVO> questaoVOs =  getFacadeFactory().getQuestaoFacade().consultarQuestaoParaClonagemReaOutraDisciplina(obj, cupre);
			for (QuestaoVO questaoVO : questaoVOs) {
				getFacadeFactory().getQuestaoFacade().realizarClonagemReaOutraDisciplinaQuestao(obj, questaoVO, false, usuarioLogado);
			}
		}
		cupre.getListaExercicio().setDisciplina(obj.getDisciplina());
		for (QuestaoListaExercicioVO questaoLista : cupre.getListaExercicio().getQuestaoListaExercicioVOs()) {
			QuestaoVO questaoExistente = getFacadeFactory().getQuestaoFacade().consultarQuestaoExistenteParaClonagemPorCodigoQuestaoPorDisciplina(questaoLista.getQuestao().getCodigo(), obj.getDisciplina().getCodigo(), false, true);
			if(Uteis.isAtributoPreenchido(questaoExistente)){
				questaoLista.setQuestao(questaoExistente);
			}else{
				questaoLista.setQuestao(getFacadeFactory().getQuestaoFacade().realizarClonagemReaOutraDisciplinaQuestao(obj, questaoLista.getQuestao(), true, usuarioLogado));
			}
		}
		getFacadeFactory().getListaExercicioFacade().persistir(cupre.getListaExercicio(), false, "ListaExercicio", usuarioLogado);	
	}
	
	public boolean validarUsoArquivoConteudo(String caminhoArquivo, String nomeArquivo) {
		StringBuilder sb = new StringBuilder(" SELECT count(codigo) AS qtd  FROM ( ")
				.append(" SELECT codigo FROM conteudounidadepagina WHERE caminhobaserepositorio = ? ")
				.append(" AND nomefisicoarquivo = ? UNION ALL ")
				.append(" SELECT codigo FROM conteudounidadepaginarecursoeducacional WHERE caminhobaserepositorio = ? ")
				.append(" AND nomefisicoarquivo = ? ) AS t ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), caminhoArquivo, nomeArquivo, caminhoArquivo, nomeArquivo);
		return rs.next() && Uteis.isAtributoPreenchido(rs.getInt("qtd")) && rs.getInt("qtd") > 1;
	}
}
