package negocio.facade.jdbc.academico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.RegistroAtividadeComplementarVO;
import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.RegistroAtividadeComplementarMatriculaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RegistroAtividadeComplementarMatricula extends ControleAcesso implements RegistroAtividadeComplementarMatriculaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public RegistroAtividadeComplementarMatricula() {
		super();
		this.setIdEntidade("RegistroAtividadeComplementarMatricula");
	}

	public RegistroAtividadeComplementarMatriculaVO novo() throws Exception {
		RegistroAtividadeComplementarMatricula.incluir(RegistroAtividadeComplementarMatricula.getIdEntidade());
		RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RegistroAtividadeComplementarMatriculaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			validarDados(obj);
			if(Uteis.isAtributoPreenchido(usuario) && !usuario.getIsApresentarVisaoAluno() && !usuario.getIsApresentarVisaoPais()) {
				obj.setSituacaoAtividadeComplementarMatricula(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO);
				obj.setDataDeferimentoIndeferimento(new Date());
				obj.getResponsavelDeferimentoIndeferimento().setCodigo(usuario.getCodigo());
				obj.getResponsavelDeferimentoIndeferimento().setNome(usuario.getNome());
			}else if(Uteis.isAtributoPreenchido(usuario) && usuario.getIsApresentarVisaoAluno() && usuario.getIsApresentarVisaoPais()) {
				obj.setSituacaoAtividadeComplementarMatricula(SituacaoAtividadeComplementarMatriculaEnum.AGUARDANDO_DEFERIMENTO);
				obj.setDataDeferimentoIndeferimento(null);
				if (obj.getArquivoVO().getNome().trim().isEmpty()) {
					throw new Exception("Deve ser informado o certificado/comprovante desta atividade complementar nos formatos gif, png, jpg, jpeg, bmp, pdf, JPEG, JPG, PNG, GIF, BMP, PDF.");
				}
			}
			
			// validarDadosAqruivo(obj);
			if (!obj.getArquivoVO().getNome().equals("")) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuario, configuracaoGeralSistemaVO);
			}
			realizarVinculoHistoricoDisciplinaAtividadeComplementar(obj);
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 29/10/2014
			  *  Classe Subordinada
			*/ 
			// RegistroAtividadeComplementarMatricula.incluir(RegistroAtividadeComplementarMatricula.getIdEntidade());
			final String sql = "INSERT INTO RegistroAtividadeComplementarMatricula( registroAtividadeComplementar, matricula, tipoAtividadeComplementar, cargaHorariaEvento, cargaHorariaConsiderada, arquivo, historico,observacao, situacaoAtividadeComplementarMatricula, responsavelDeferimentoIndeferimento, dataDeferimentoIndeferimento, motivoIndeferimento, dataCriacao ) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getRegistroAtividadeComplementar().getCodigo());
					sqlInserir.setString(2, obj.getMatriculaVO().getMatricula());
					sqlInserir.setInt(3, obj.getTipoAtividadeComplementarVO().getCodigo());
					sqlInserir.setInt(4, obj.getCargaHorariaEvento());
					sqlInserir.setInt(5, obj.getCargaHorariaConsiderada());
					if (!obj.getArquivoVO().getCodigo().equals(0)) {
						sqlInserir.setInt(6, obj.getArquivoVO().getCodigo());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (!obj.getHistoricoVO().getCodigo().equals(0)) {
						sqlInserir.setInt(7, obj.getHistoricoVO().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setString(8, obj.getObservacao());
					sqlInserir.setString(9, obj.getSituacaoAtividadeComplementarMatricula().name());
					if(Uteis.isAtributoPreenchido(obj.getResponsavelDeferimentoIndeferimento().getCodigo())) {
						sqlInserir.setInt(10, obj.getResponsavelDeferimentoIndeferimento().getCodigo());
					}else {
						sqlInserir.setNull(10,0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataDeferimentoIndeferimento())) {
						sqlInserir.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getDataDeferimentoIndeferimento()));
					}else {
						sqlInserir.setNull(11,0);
					}
					sqlInserir.setString(12, obj.getMotivoIndeferimento());
					if(Uteis.isAtributoPreenchido(obj.getDataCriacao())) {
						sqlInserir.setTimestamp(13, Uteis.getDataJDBCTimestamp(obj.getDataCriacao()));
					}else {
						sqlInserir.setNull(13,0);
					}
					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);
			if(Uteis.isAtributoPreenchido(obj.getOperacaoFuncionalidadeVO().getOrigem())){
				obj.getOperacaoFuncionalidadeVO().setCodigoOrigem(obj.getCodigo().toString());
				getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(obj.getOperacaoFuncionalidadeVO());
			}
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarVinculoHistoricoDisciplinaAtividadeComplementar(RegistroAtividadeComplementarMatriculaVO obj) throws Exception {
		if (obj.getHistoricoVO().getCodigo() > 0) {
			getFacadeFactory().getHistoricoFacade().realizarVinculoHistoricoAtividadeComplementar(obj.getHistoricoVO().getCodigo(), true);
		}
		if (obj.getHistoricoAnt() != null && obj.getHistoricoVO().getCodigo().equals(obj.getHistoricoAnt())) {
			getFacadeFactory().getHistoricoFacade().realizarVinculoHistoricoAtividadeComplementar(obj.getHistoricoAnt(), false);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RegistroAtividadeComplementarMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			if (!obj.getArquivoVO().getNome().trim().isEmpty()) {
				if (obj.getArquivoVO().getCodigo().equals(0)) {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), usuario, configuracaoGeralSistemaVO);
				} else {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), usuario, configuracaoGeralSistemaVO);
				}
			}
			realizarVinculoHistoricoDisciplinaAtividadeComplementar(obj);
			final String sql = "UPDATE registroAtividadeComplementarMatricula set arquivo = ?, tipoAtividadeComplementar = ?, cargaHorariaEvento = ? , cargaHorariaConsiderada = ?, historico = ?,observacao = ?, situacaoAtividadeComplementarMatricula = ?, responsavelDeferimentoIndeferimento = ?, dataDeferimentoIndeferimento = ?, motivoIndeferimento = ?   where codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (!obj.getArquivoVO().getCodigo().equals(0)) {
						sqlAlterar.setInt(1, obj.getArquivoVO().getCodigo());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (!obj.getTipoAtividadeComplementarVO().getCodigo().equals(0)) {
						sqlAlterar.setInt(2, obj.getTipoAtividadeComplementarVO().getCodigo());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, obj.getCargaHorariaEvento());
					sqlAlterar.setInt(4, obj.getCargaHorariaConsiderada());
					if (!obj.getHistoricoVO().getCodigo().equals(0)) {
						sqlAlterar.setInt(5, obj.getHistoricoVO().getCodigo());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setString(6, obj.getObservacao());
					sqlAlterar.setString(7, obj.getSituacaoAtividadeComplementarMatricula().name());
					if(Uteis.isAtributoPreenchido(obj.getResponsavelDeferimentoIndeferimento().getCodigo())) {
						sqlAlterar.setInt(8, obj.getResponsavelDeferimentoIndeferimento().getCodigo());
					}else {
						sqlAlterar.setNull(8,0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataDeferimentoIndeferimento())) {
						sqlAlterar.setTimestamp(9, Uteis.getDataJDBCTimestamp(obj.getDataDeferimentoIndeferimento()));
					}else {
						sqlAlterar.setNull(9,0);
					}
					sqlAlterar.setString(10, obj.getMotivoIndeferimento());
					sqlAlterar.setInt(11, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(Integer codigoRegistroAtividadeComplementar, UsuarioVO usuarioVO) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 29/10/2014
			  *  Classe Subordinada
			  */ 
			RegistroAtividadeComplementarMatricula.excluir(RegistroAtividadeComplementarMatricula.getIdEntidade());
			String sql = "DELETE FROM RegistroAtividadeComplementarMatricula where registroAtividadeComplementar=? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			RegistroAtividadeComplementarMatricula.getConexao().getJdbcTemplate().update(sql, new Object[] { codigoRegistroAtividadeComplementar });
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirArquivo(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		PastaBaseArquivoEnum pastaBaseArquivoEnum = PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR_TMP;
		String nomeArquivo = registroAtividadeComplementarMatriculaVO.getArquivoVO().getNome();
		ArquivoVO arquivoVO = registroAtividadeComplementarMatriculaVO.getArquivoVO();
		if (registroAtividadeComplementarMatriculaVO.getArquivoVO().getCodigo() > 0) {
			pastaBaseArquivoEnum = PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR;
			registroAtividadeComplementarMatriculaVO.setArquivoVO(new ArquivoVO());
			alterar(registroAtividadeComplementarMatriculaVO, configuracaoGeralSistemaVO, usuarioVO);
			getFacadeFactory().getArquivoFacade().excluir(arquivoVO, usuarioVO, configuracaoGeralSistemaVO);
		} else {
			registroAtividadeComplementarMatriculaVO.setArquivoVO(new ArquivoVO());
		}
		if (!nomeArquivo.trim().isEmpty()) {

			File file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + pastaBaseArquivoEnum.getValue() + File.separator + nomeArquivo);
			registroAtividadeComplementarMatriculaVO.setCaminhoArquivoWeb("");
			getFacadeFactory().getArquivoHelper().delete(file);
		}

	}
	
	private StringBuilder getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder();
		sql.append("select registroatividadecomplementarmatricula.*, matricula.curso, matricula.matricula, pessoa.nome, tipoAtividadeComplementar.nome as tipoAtividadeComplementarNome, ");
		sql.append(" disciplina.nome as disciplina, arquivo.nome as \"arquivo_nome\", arquivo.pastaBaseArquivo as \"arquivo_pastaBaseArquivo\", ");
		sql.append(" operacaoFuncionalidade.codigo as \"operacaoFuncionalidade.codigo\", operacaoFuncionalidade.codigoorigem as \"operacaoFuncionalidade.codigoorigem\", operacaoFuncionalidade.data as \"operacaoFuncionalidade.data\", ");
		sql.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\", curso.periodicidade, curso.nome as \"curso.nome\", registroatividadecomplementar.data as registroatividadecomplementar_data, ");
		sql.append(" pessoa_responsavel.codigo as \"pessoa_responsavel.codigo\", pessoa_responsavel.nome as \"pessoa_responsavel.nome\",registroatividadecomplementarmatricula.observacao AS registroatividadecomplementarmatricula_observacao, ");
		sql.append(" responsavelDeferimentoIndeferimento.nome as responsavelDeferimentoIndeferimento_nome, registroatividadecomplementar.nomeEvento as \"registroatividadecomplementar.nomeEvento\", registroatividadecomplementar.data as \"registroatividadecomplementar.data\" ");
		sql.append(" from registroatividadecomplementarmatricula ");
		sql.append(" inner join registroatividadecomplementar on registroatividadecomplementar.codigo = registroatividadecomplementarmatricula.registroatividadecomplementar ");
		sql.append(" inner join matricula on matricula.matricula = RegistroAtividadeComplementarMatricula.matricula ");
		sql.append(" inner join curso on matricula.curso = curso.codigo ");		
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" inner join tipoAtividadeComplementar on tipoAtividadeComplementar.codigo = registroAtividadeComplementarMatricula.tipoAtividadeComplementar");
		sql.append(" left join arquivo on arquivo.codigo = registroAtividadeComplementarMatricula.arquivo");
		sql.append(" left join historico on historico.codigo = registroAtividadeComplementarMatricula.historico");
		sql.append(" left join disciplina on historico.disciplina = disciplina.codigo ");
		sql.append(" left join operacaoFuncionalidade on operacaoFuncionalidade.origem = '").append(OrigemOperacaoFuncionalidadeEnum.REGISTRO_ATIVIDADE_COMPLEMENTAR_MATRICULA.name()).append("' and operacaoFuncionalidade.operacao = '").append(OperacaoFuncionalidadeEnum.REGISTROATIVIDADECOMPLEMENTARMATRICULA_LIBERARCARGAHORARIAMAXIMAPERIODOLETIVO.name()).append("' and operacaoFuncionalidade.codigoorigem = registroatividadecomplementarmatricula.codigo::text ");
		sql.append(" left join usuario on usuario.codigo = operacaoFuncionalidade.responsavel ");
		sql.append(" left join pessoa pessoa_responsavel on pessoa_responsavel.codigo = usuario.pessoa ");
		sql.append(" left join usuario as responsavelDeferimentoIndeferimento on responsavelDeferimentoIndeferimento.codigo = registroatividadecomplementarmatricula.responsavelDeferimentoIndeferimento ");
		return sql;
	}

	@Override
	public List<RegistroAtividadeComplementarMatriculaVO> consultarPorRegistroAtividadeComplementar(Integer codigoRegistroAtividadeComplementar, String matricula, boolean controlarAcesso, Date dataInicio, Date dataFinal, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where registroatividadecomplementar.codigo = ").append(codigoRegistroAtividadeComplementar);
		if(Uteis.isAtributoPreenchido(dataInicio)){
			sql.append(" AND RegistroAtividadeComplementarMatricula.dataCriacao >= '").append(Uteis.getDataJDBCTimestamp(dataInicio)).append("' ");
		}
		if(Uteis.isAtributoPreenchido(dataFinal)){
			sql.append(" AND RegistroAtividadeComplementarMatricula.dataCriacao <= '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("' ");
		}		
		if(Uteis.isAtributoPreenchido(matricula)) {
			sql.append(" AND matricula.matricula = '").append(matricula).append("' ");
		}
		sql.append(" order by pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (this.montarDadosConsulta(tabelaResultado, usuario));
	}
	
	@Override
	public List<RegistroAtividadeComplementarMatriculaVO> consultarPorMatriculaSituacao(String matricula, SituacaoAtividadeComplementarMatriculaEnum situacaoAtividadeComplementarMatriculaEnum, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where registroatividadecomplementarmatricula.matricula = ? ");
		if(situacaoAtividadeComplementarMatriculaEnum != null) {
			sql.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula =  '").append(situacaoAtividadeComplementarMatriculaEnum.name()).append("' ");
		}
		sql.append(" order by registroatividadecomplementar.data ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		return (this.montarDadosConsulta(tabelaResultado, usuario));
	}

	public RegistroAtividadeComplementarMatriculaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getRegistroAtividadeComplementar().setCodigo(dadosSQL.getInt("registroAtividadeComplementar"));
		obj.getRegistroAtividadeComplementar().setData(dadosSQL.getDate("registroAtividadeComplementar_data"));
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatriculaVO().getCurso().setCodigo(dadosSQL.getInt("curso"));
		obj.getMatriculaVO().getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getMatriculaVO().getCurso().setPeriodicidade(dadosSQL.getString("periodicidade"));
		obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("nome"));
		obj.getTipoAtividadeComplementarVO().setNome(dadosSQL.getString("tipoAtividadeComplementarNome"));
		obj.getTipoAtividadeComplementarVO().setCodigo(dadosSQL.getInt("tipoAtividadeComplementar"));
		obj.setObservacao(dadosSQL.getString("registroatividadecomplementarmatricula_observacao"));
		// Monta lista (combobox) individual para cada matrícula
		List<TipoAtividadeComplementarVO> resultadoConsulta = getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorCursoTurmaMatricula(null, null, false, obj.getMatriculaVO().getMatricula(), obj.getTipoAtividadeComplementarVO().getCodigo(), false, usuario);
		List<SelectItem> lista = UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false);
		obj.setListaSelectItemTipoAtividadeComplementar(lista);
		obj.setCargaHorariaEvento(dadosSQL.getInt("cargaHorariaEvento"));
		obj.setCargaHorariaConsiderada(dadosSQL.getInt("cargaHorariaConsiderada"));
		obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
		obj.getArquivoVO().setNome(dadosSQL.getString("arquivo_nome"));
		obj.getArquivoVO().setPastaBaseArquivo(dadosSQL.getString("arquivo_pastaBaseArquivo"));
		obj.getHistoricoVO().setCodigo(dadosSQL.getInt("historico"));
		obj.getHistoricoVO().getDisciplina().setNome(dadosSQL.getString("disciplina"));
		obj.setSituacaoAtividadeComplementarMatricula(SituacaoAtividadeComplementarMatriculaEnum.valueOf(dadosSQL.getString("situacaoAtividadeComplementarMatricula")));
		obj.setDataDeferimentoIndeferimento(dadosSQL.getDate("dataDeferimentoIndeferimento"));
		obj.getResponsavelDeferimentoIndeferimento().setCodigo(dadosSQL.getInt("responsavelDeferimentoIndeferimento"));
		obj.getResponsavelDeferimentoIndeferimento().setNome(dadosSQL.getString("responsavelDeferimentoIndeferimento_nome"));
		obj.setMotivoIndeferimento(dadosSQL.getString("motivoIndeferimento"));
		obj.getRegistroAtividadeComplementar().setNomeEvento(dadosSQL.getString("registroatividadecomplementar.nomeEvento"));
		obj.getRegistroAtividadeComplementar().setData(dadosSQL.getDate("registroatividadecomplementar.data"));
		if (obj.getArquivoVO().getCodigo() > 0) {
			obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
			obj.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR_TMP);
		}
		if(dadosSQL.getInt("operacaofuncionalidade.codigo") > 0 ){
			obj.getOperacaoFuncionalidadeVO().setCodigo(dadosSQL.getInt("operacaofuncionalidade.codigo"));
			obj.getOperacaoFuncionalidadeVO().setCodigoOrigem(dadosSQL.getString("operacaofuncionalidade.codigoorigem"));
			obj.getOperacaoFuncionalidadeVO().setOrigem(OrigemOperacaoFuncionalidadeEnum.REGISTRO_ATIVIDADE_COMPLEMENTAR_MATRICULA);
			obj.getOperacaoFuncionalidadeVO().setOperacao(OperacaoFuncionalidadeEnum.REGISTROATIVIDADECOMPLEMENTARMATRICULA_LIBERARCARGAHORARIAMAXIMAPERIODOLETIVO);
			obj.getOperacaoFuncionalidadeVO().setData(dadosSQL.getDate("operacaofuncionalidade.data"));
			obj.getOperacaoFuncionalidadeVO().getResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
			obj.getOperacaoFuncionalidadeVO().getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
			obj.getOperacaoFuncionalidadeVO().getResponsavel().getPessoa().setCodigo(dadosSQL.getInt("pessoa_responsavel.codigo"));
			obj.getOperacaoFuncionalidadeVO().getResponsavel().getPessoa().setNome(dadosSQL.getString("pessoa_responsavel.nome"));
			obj.getTipoAtividadeComplementarVO().setCargaHorasPermitidasPeriodoLetivo(0);
		}else{
			obj.setTipoAtividadeComplementarVO(getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().consultarCargaHorasMaximaPermitidoPeriodoLetivoDoTipoAtividadeComplementar(obj.getTipoAtividadeComplementarVO(), obj.getMatriculaVO().getCurso().getCodigo(), obj.getMatriculaVO().getMatricula(), PeriodicidadeEnum.getEnumPorValor(obj.getMatriculaVO().getCurso().getPeriodicidade()), obj.getRegistroAtividadeComplementar().getData(), obj.getCodigo()));
		}
		if (Uteis.isColunaExistente(dadosSQL, "justificativaAlteracaoCHConsiderada")) {
			obj.setJustificativaAlteracaoCHConsiderada(dadosSQL.getString("justificativaAlteracaoCHConsiderada"));
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("responsavelEditarCHConsiderada"))) {
			int codResponsavel = dadosSQL.getInt("responsavelEditarCHConsiderada");
			obj.setResponsavelEditarCHConsiderada(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(codResponsavel, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getTimestamp("dataEditarCHConsiderada"))) {
			obj.setDataEditarCHConsiderada(dadosSQL.getTimestamp("dataEditarCHConsiderada"));
		}
		obj.setDataCriacao(dadosSQL.getTimestamp("dataCriacao"));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	public Integer consultarCargaHorariaRealizada(String matricula, Integer matrizCurricular, boolean obterCargaHorariaConsiderada) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if (obterCargaHorariaConsiderada) {
			sqlStr.append(" SELECT cargahorariaconsiderada as cargaHorariaAtividadeComplementar from  ");	
		} else {
			sqlStr.append(" select case when cargahorariaconsiderada > cargahorariaexigida then cargahorariaexigida else cargahorariaconsiderada end cargaHorariaAtividadeComplementar from  ");
		}
		sqlStr.append(" (SELECT distinct gradecurricular.totalCargaHorariaAtividadeComplementar as cargahorariaexigida, (select sum (case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria  and ").append(obterCargaHorariaConsiderada).append(" = false then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) ");
		
		sqlStr.append(" as cargaHorariaRealizadaAtividadeComplementar  from (  select sum(cargahorariaconsiderada) as cargaHorariaRealizadaAtividadeComplementar, ");
		sqlStr.append(" registroatividadecomplementarmatricula.tipoAtividadeComplementar,  gradecurriculartipoatividadecomplementar.cargahoraria ");
		sqlStr.append(" from registroatividadecomplementarmatricula ");
		sqlStr.append("  inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.tipoAtividadeComplementar = registroatividadecomplementarmatricula.tipoAtividadeComplementar ");
		sqlStr.append(" where registroatividadecomplementarmatricula.matricula = matricula.matricula and gradecurriculartipoatividadecomplementar.gradecurricular = gradecurricular.codigo ");
		sqlStr.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");
		sqlStr.append(" group by registroatividadecomplementarmatricula.tipoAtividadeComplementar,  gradecurriculartipoatividadecomplementar.cargahoraria  ) as t) as cargahorariaconsiderada ");
		sqlStr.append(" from matricula ");
		sqlStr.append(" INNER JOIN gradecurricular on gradecurricular.codigo = ").append(matrizCurricular).append(" ");
		sqlStr.append(" where matricula.matricula = '").append(matricula).append("' limit 1) as t");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("cargaHorariaAtividadeComplementar");
		}
		return 0;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> consultarListaMatriculaVOs(Integer curso, Integer codigoTurma, boolean turmaAgrupada, boolean turmaSubturma, String ano, String semestre, String situacao, String matricula, Integer codigoTipoAtividadeComplementar, Integer cargaHorariaEvento, Integer cargaHorariaConsiderada, boolean controlarAcesso, UsuarioVO usuario,String observacao, Date dataBase) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT distinct matricula.matricula,  matricula.curso, pessoa.nome, curso.periodicidade FROM matricula ");
		sql.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		if (turmaAgrupada) {
			if (turmaSubturma) {
				sql.append(" INNER JOIN turmaagrupada ON turmaagrupada.turma = matriculaperiodo.turma ");
				sql.append(" INNER JOIN turma AS turma2 ON turma2.turmaprincipal = turmaagrupada.turmaorigem ");
			} else {
				sql.append(" INNER JOIN turmaagrupada ON turmaagrupada.turma = matriculaperiodo.turma ");
			}
		} else if (turmaSubturma) {
			sql.append(" INNER JOIN turma AS turma2 ON turma2.turmaprincipal = matriculaperiodo.turma ");
		}
		sql.append(" WHERE 1=1 ");

		if (!curso.equals(0)) {
			sql.append(" AND matricula.curso = ").append(curso);
		}
		if (!codigoTurma.equals(0)) {
			if (turmaAgrupada) {
				if (turmaSubturma) {
					sql.append(" AND turma2.codigo = ").append(codigoTurma);
				} else {
					sql.append(" AND turmaagrupada.turmaorigem = ").append(codigoTurma);
				}
			} else if (turmaSubturma) {
				sql.append(" AND turma2.codigo = ").append(codigoTurma);
			} else {
				sql.append(" AND matriculaperiodo.turma = ").append(codigoTurma);
			}
		}
		if (!matricula.equals("")) {
			sql.append(" AND matricula.matricula = '").append(matricula).append("' ");
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" AND matriculaperiodo.ano = '").append(ano).append("' ");
		}
		if (semestre != null && !semestre.equals("")) {
			sql.append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ");
		}
		if (situacao != null && situacao.equals("AT")) {
			sql.append(" AND matriculaperiodo.situacaoMatriculaPeriodo in ('AT','FI','CO') ");
		}
		if (situacao != null && (situacao.equals("CO") || situacao.equals("PF"))) {
			if (situacao.equals("CO")) {
				sql.append(" AND matriculaperiodo.situacaoMatriculaPeriodo = 'FI' ");
			} else if (!situacao.equals("")) {
				sql.append(" AND matriculaperiodo.situacaoMatriculaPeriodo = 'AT' ");
			}
			sql.append(" and 0 = (select count(codigo) from matriculaperiodo mp where mp.matricula = matriculaperiodo.matricula   ");
			sql.append(" and mp.situacaomatriculaperiodo not in ('PC')  and mp.codigo != matriculaperiodo.codigo ");
			sql.append(" and (mp.ano||'/'||mp.semestre) > (matriculaperiodo.ano||'/'||matriculaperiodo.semestre)) ");

			sql.append(" and (select count(distinct disciplina.codigo) from historico ");
			sql.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
			sql.append(" where disciplina.tipoDisciplina in ('OB', 'LG')  ");
			sql.append(" and historico.matricula = matricula.matricula ");
			sql.append(" and historico.situacao in ('AA', 'AP','CS', 'CC') ) ");
			sql.append(" >= (select count(distinct disciplina.codigo) from gradecurricular ");
			sql.append(" INNER JOIN periodoletivo AS pr ON gradecurricular.codigo = pr.gradecurricular ");
			sql.append(" INNER join gradedisciplina AS grDisc on grDisc.periodoletivo = pr.codigo  ");
			sql.append(" INNER join disciplina on grDisc.disciplina = disciplina.codigo  ");
			sql.append(" where gradecurricular.codigo =  matricula.gradecurricularatual  ");
			sql.append(" and disciplina.tipoDisciplina in ('OB', 'LG') ");
			sql.append(" ) ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaVOs = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		while (tabelaResultado.next()) {
			RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("nome"));
			obj.setObservacao(observacao);
			obj.setCargaHorariaEvento(cargaHorariaEvento);
			obj.setCargaHorariaConsiderada(cargaHorariaConsiderada);
			obj.getMatriculaVO().getCurso().setCodigo(tabelaResultado.getInt("curso"));			
			obj.getMatriculaVO().getCurso().setPeriodicidade(tabelaResultado.getString("periodicidade"));
			obj.getMatriculaVO().getCurso().setCodigo(curso);
			obj.setTipoAtividadeComplementarVO(getFacadeFactory().getTipoAtividadeComplementarFacade().consultarPorChavePrimaria(codigoTipoAtividadeComplementar, Uteis.NIVELMONTARDADOS_TODOS, controlarAcesso, usuario));
			obj.setTipoAtividadeComplementarVO(getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().consultarCargaHorasMaximaPermitidoPeriodoLetivoDoTipoAtividadeComplementar(obj.getTipoAtividadeComplementarVO(), obj.getMatriculaVO().getCurso().getCodigo(), obj.getMatriculaVO().getMatricula(), PeriodicidadeEnum.getEnumPorValor(obj.getMatriculaVO().getCurso().getPeriodicidade()), dataBase, obj.getCodigo()));
			// Monta lista (combobox) individual para cada matrícula
			List<TipoAtividadeComplementarVO> resultadoConsulta = getFacadeFactory().getTipoAtividadeComplementarFacade()
					.consultarPorCursoTurmaMatricula(null, null, false, obj.getMatriculaVO().getMatricula(), obj.getTipoAtividadeComplementarVO().getCodigo(), false, usuario);
			List<SelectItem> lista = UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", false);
			obj.setListaSelectItemTipoAtividadeComplementar(lista);
			listaConsultaRegistroAtividadeComplementarMatriculaVOs.add(obj);
		}
		return listaConsultaRegistroAtividadeComplementarMatriculaVOs;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> adicionarItemListaRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaVOs, List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs) {
		for (RegistroAtividadeComplementarMatriculaVO obj : listaConsultaRegistroAtividadeComplementarMatriculaVOs) {
			this.validarUnicidadeLista(listaRegistroAtividadeComplementarMatriculaVOs, obj);
		}
		Collections.sort(listaRegistroAtividadeComplementarMatriculaVOs, new Comparator<RegistroAtividadeComplementarMatriculaVO>() {
			public int compare(RegistroAtividadeComplementarMatriculaVO obj1, RegistroAtividadeComplementarMatriculaVO obj2) {
				return Uteis.removerAcentos(obj1.getMatriculaVO().getAluno().getNome()).compareToIgnoreCase(Uteis.removerAcentos(obj2.getMatriculaVO().getAluno().getNome()));
			}
		});
		return listaRegistroAtividadeComplementarMatriculaVOs;
	}

	public static void validarDadosFiltroConsulta(Integer curso, Integer turma, String matricula, String periodicidade, String semestre, String ano) throws ConsistirException {
		if (curso < 1 && turma < 1 && matricula.equals("")) {
			throw new ConsistirException("Informe ao menos um dos campos CURSO/TURMA/MATRÍCULA");
		}
		if (matricula.equals("") && periodicidade != null && (periodicidade.equals("AN") || periodicidade.equals("SE"))) {
			if (ano == null || ano.equals("")) {
				throw new ConsistirException("O campo Ano deve ser informado");
			}
			if (periodicidade.equals("SE") && (semestre == null || semestre.equals(""))) {
				throw new ConsistirException("O campo Semestre deve ser selecionado");
			}
		}
	}

	public void validarUnicidadeLista(List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs, RegistroAtividadeComplementarMatriculaVO obj) {
		int index = 0;
		for (RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO : listaRegistroAtividadeComplementarMatriculaVOs) {
			if (registroAtividadeComplementarMatriculaVO.getMatriculaVO().getMatricula().equals(obj.getMatriculaVO().getMatricula()) && registroAtividadeComplementarMatriculaVO.getTipoAtividadeComplementarVO().getCodigo().equals(obj.getTipoAtividadeComplementarVO().getCodigo())) {
				obj.getRegistroAtividadeComplementar().setCodigo(registroAtividadeComplementarMatriculaVO.getRegistroAtividadeComplementar().getCodigo());
				obj.setNovoObj(Boolean.FALSE);
				listaRegistroAtividadeComplementarMatriculaVOs.set(index, obj);
				return;
			} 
			index++;
		}
		listaRegistroAtividadeComplementarMatriculaVOs.add(obj);
	}

	public void incluirRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs, Integer codigoRegistroAtividadeComplementar, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		for (RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO : listaRegistroAtividadeComplementarMatriculaVOs) {
			if (registroAtividadeComplementarMatriculaVO.getCodigo().equals(0)) {				
				registroAtividadeComplementarMatriculaVO.getRegistroAtividadeComplementar().setCodigo(codigoRegistroAtividadeComplementar);
				this.incluir(registroAtividadeComplementarMatriculaVO, usuarioVO, configuracaoGeralSistemaVO);
			}
		}
	}

	public void alterarRegistroAtividadeComplementarMatriculaVOs(List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs, Integer codigoRegistroAtividadeComplementar, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		this.excluirRegistroAtividadeComplementarMatricula(codigoRegistroAtividadeComplementar, listaRegistroAtividadeComplementarMatriculaVOs, configuracaoGeralSistemaVO, usuarioVO);
		for (RegistroAtividadeComplementarMatriculaVO obj : listaRegistroAtividadeComplementarMatriculaVOs) {
			if (obj.getCodigo().equals(0)) {
				obj.getRegistroAtividadeComplementar().setCodigo(codigoRegistroAtividadeComplementar);
				obj.setDataCriacao(new Date());
				incluir(obj, usuarioVO, configuracaoGeralSistemaVO);
			} else {
				this.alterar(obj, configuracaoGeralSistemaVO, usuarioVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirRegistroAtividadeComplementarMatricula(Integer codigoRegistroAtividadeComplementar, List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append(" UPDATE historico set historicoDisciplinaAtividadeComplementar = false where codigo in (select historico from  registroAtividadeComplementarMatricula ");
		sb.append(" where registroAtividadeComplementar = ").append(codigoRegistroAtividadeComplementar).append(" and historico is not null ) ");
		getConexao().getJdbcTemplate().update(sb.toString());

		sb = new StringBuilder();
		sb.append(" select arquivo.codigo, arquivo.nome, arquivo.pastaBaseArquivo from  registroAtividadeComplementarMatricula ");
		sb.append(" inner join arquivo on arquivo.codigo = registroAtividadeComplementarMatricula.arquivo ");
		sb.append(" where registroAtividadeComplementar = ").append(codigoRegistroAtividadeComplementar).append(" and registroAtividadeComplementarMatricula.codigo not in (0");
		for (RegistroAtividadeComplementarMatriculaVO obj : listaRegistroAtividadeComplementarMatriculaVOs) {
			sb.append(", ").append(obj.getCodigo());
		}
		sb.append(") ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (rs.next()) {
			ArquivoVO arquivoVO = new ArquivoVO();
			arquivoVO.setNovoObj(false);
			arquivoVO.setCodigo(rs.getInt("codigo"));
			arquivoVO.setNome(rs.getString("nome"));
			arquivoVO.setPastaBaseArquivo(rs.getString("pastaBaseArquivo"));
			File file = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ATIVIDADECOMPLEMENTAR.getValue() + File.separator + arquivoVO.getNome());
			getFacadeFactory().getArquivoHelper().delete(file);
			getFacadeFactory().getArquivoFacade().excluir(arquivoVO, usuario, configuracaoGeralSistemaVO);
		}

		sb = new StringBuilder();
		sb.append("DELETE FROM registroAtividadeComplementarMatricula where registroAtividadeComplementar = ");
		sb.append(codigoRegistroAtividadeComplementar);
		sb.append(" and codigo not in (");
		boolean virgula = false;
		for (RegistroAtividadeComplementarMatriculaVO obj : listaRegistroAtividadeComplementarMatriculaVOs) {
			if (!virgula) {
				sb.append(obj.getCodigo());
				virgula = true;
			} else {
				sb.append(", ").append(obj.getCodigo());
			}
		}
		sb.append(") ");
		getConexao().getJdbcTemplate().update(sb.toString());
	}

	public List<RegistroAtividadeComplementarMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<RegistroAtividadeComplementarMatriculaVO> vetResultado = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static void validarDados(RegistroAtividadeComplementarMatriculaVO obj) throws ConsistirException {
		if (obj.getTipoAtividadeComplementarVO().getCodigo().equals(0)) {
			throw new ConsistirException("O campo TIPO ATIVIDADE COMPLEMENTAR (Atividade Complementar) deve ser informado.");
		}
//		if (obj.getCargaHorariaEvento().equals(0)) {
//			throw new ConsistirException("O campo CARGA HORÁRIA EVENTO (Atividade Complementar) deve ser informado.");
//		}
        if (obj.getCargaHorariaConsiderada() == null || obj.getCargaHorariaConsiderada().intValue() < 0) {
			throw new ConsistirException("O campo CARGA HORÁRIA REALIZADA (Atividade Complementar) deve ser maior ou igual a ZERO.");
		}  
	}
	
	public static void validarDadosLista(RegistroAtividadeComplementarVO registro) throws ConsistirException {
		List<String> lista = new ArrayList<String>(0);
		if (registro.getListaRegistroAtividadeComplementarMatriculaVOs().isEmpty()) {
			throw new ConsistirException(" É necessário informar pelo menos um aluno na listagem para realizar essa operação.");
		}
		for (RegistroAtividadeComplementarMatriculaVO obj : registro.getListaRegistroAtividadeComplementarMatriculaVOs()) {
			if (lista.contains(obj.getMatriculaVO().getMatricula() + obj.getTipoAtividadeComplementarVO().getCodigo())) {
				throw new ConsistirException("A matrícula " + obj.getMatriculaVO().getMatricula() + " possui mais de um registro com o mesmo tipo atividade complementar: " + obj.getTipoAtividadeComplementarVO().getNome() + ".");
			}
			lista.add(obj.getMatriculaVO().getMatricula() + obj.getTipoAtividadeComplementarVO().getCodigo());
			if (obj.getTipoAtividadeComplementarVO().getCodigo().equals(0)) {
				throw new ConsistirException("O campo TIPO ATIVIDADE COMPLEMENTAR da matrícula " + obj.getMatriculaVO().getMatricula() + " deve ser informado.");
			}
			if (obj.getCargaHorariaConsiderada() == null || obj.getCargaHorariaConsiderada().intValue() < 0) {
				throw new ConsistirException("O campo CARGA HORÁRIA REALIZADA da matrícula " + obj.getMatriculaVO().getMatricula() + " deve ser maior ou igual a ZERO.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getArquivoVO().getDescricao())) {
				throw new ConsistirException("O campo UPLOAD CERTIFICADO da matrícula " + obj.getMatriculaVO().getMatricula() + " deve ser informado.");
			}

			if(obj.isChRealizadaMaiorQueHorasPermitidaPeriodoLetivo()){
				throw new ConsistirException(" O registro de matrícula "+obj.getMatriculaVO().getMatricula()+ " ainda esta com a carga horária excedida. Por favor altere o valor do campo C.H Realizada ou libere a carga horária excedida");
			}
		}
	}

	public static void validarDadosAqruivo(RegistroAtividadeComplementarMatriculaVO obj) throws ConsistirException {
		if (obj.getArquivoVO().getNome().equals("") || obj.getArquivoVO().getCodigo().equals(0)) {
			throw new ConsistirException("Deve ser anexado um arquivo ao Registro Atividade Complementar");
		}
	}

	public static String getIdEntidade() {
		return RegistroAtividadeComplementarMatricula.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		RegistroAtividadeComplementarMatricula.idEntidade = idEntidade;
	}

	public void removerRegistroAtividadeComplementarListaVOs(RegistroAtividadeComplementarMatriculaVO obj, List<RegistroAtividadeComplementarMatriculaVO> listaRegistroAtividadeComplementarMatriculaVOs, UsuarioVO usuarioVO) throws Exception {
		int index = 0;
		for (RegistroAtividadeComplementarMatriculaVO vo : listaRegistroAtividadeComplementarMatriculaVOs) {
			if (vo.getTipoAtividadeComplementarVO().getCodigo().equals(obj.getTipoAtividadeComplementarVO().getCodigo()) && obj.getMatriculaVO().getMatricula().equals(vo.getMatriculaVO().getMatricula())) {
				RegistroAtividadeComplementarMatricula.excluir(RegistroAtividadeComplementarMatricula.getIdEntidade());
				String sql = "DELETE FROM RegistroAtividadeComplementarMatricula where codigo=? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
				RegistroAtividadeComplementarMatricula.getConexao().getJdbcTemplate().update(sql, new Object[] { vo.getCodigo() });
				listaRegistroAtividadeComplementarMatriculaVOs.remove(index);
				return;
			}
			index++;
		}
	}

	@Override
	public Boolean consultarPendenciaAtividadeComplementarPorMatricula(Integer pessoa, String matricula) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria::NUMERIC(20,2) else cargaHorariaRealizadaAtividadeComplementar::NUMERIC(20,2) end ) as cargaHorariaRealizadaAtividadeComplementar, totalcargahorariaatividadecomplementar from ( ");
		sqlStr.append(" select sum(cargahorariaconsiderada) as cargaHorariaRealizadaAtividadeComplementar, ");
		sqlStr.append(" registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
		sqlStr.append(" gradecurriculartipoatividadecomplementar.cargahoraria, ");
		sqlStr.append(" gradecurricular.totalcargahorariaatividadecomplementar ");
		sqlStr.append(" from registroatividadecomplementarmatricula ");
		sqlStr.append(" inner join matricula on matricula.matricula = registroatividadecomplementarmatricula.matricula ");
		sqlStr.append(" inner join gradecurricular on matricula.gradecurricularatual = gradecurricular.codigo ");
		sqlStr.append(" inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.tipoAtividadeComplementar = registroatividadecomplementarmatricula.tipoAtividadeComplementar ");
		sqlStr.append(" where matricula.matricula = '").append(matricula).append("' and gradecurriculartipoatividadecomplementar.gradecurricular = matricula.gradecurricularatual ");
		sqlStr.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");
		sqlStr.append(" group by registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
		sqlStr.append(" gradecurriculartipoatividadecomplementar.cargahoraria, gradecurricular.totalcargahorariaatividadecomplementar ");
		sqlStr.append(" ) as t group by totalcargahorariaatividadecomplementar ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("totalcargahorariaatividadecomplementar") > 0 && tabelaResultado.getDouble("totalcargahorariaatividadecomplementar") > tabelaResultado.getDouble("cargaHorariaRealizadaAtividadeComplementar");
		}

		return false;
	}
	
	public Double consultarCargaHorariaConsideradaPorMatriculaGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct sum(cargahorariaconsiderada) as cargahorariaconsiderada from (");
		sb.append(" select distinct registroatividadecomplementarmatricula.codigo, registroatividadecomplementarmatricula.cargahorariaconsiderada from registroatividadecomplementarmatricula ");
		sb.append(" inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.tipoatividadecomplementar = registroatividadecomplementarmatricula.tipoatividadecomplementar ");
		sb.append(" where gradecurriculartipoatividadecomplementar.gradecurricular = ").append(gradeCurricular);
		sb.append(" and registroatividadecomplementarmatricula.matricula = '").append(matricula).append("' ");
		sb.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");
		sb.append(" group by registroatividadecomplementarmatricula.codigo ");
		sb.append(") as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("cargahorariaconsiderada");
		}
		return 0.0;
	}
	
	public List<RegistroAtividadeComplementarMatriculaVO> consultarPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.tipoAtividadeComplementarNome, t.tipoAtividadeComplementar, sum(cargaHorariaEvento) as cargaHorariaEvento, case when sum(cargaHorariaConsiderada) > cargahorariamae and cargahorariamae > 0 then cargahorariamae else sum(cargaHorariaConsiderada) end as cargaHorariaConsiderada from (");
		sb.append("select case when gradecurriculartipoatividadecomplementarsuperior.tipoatividadecomplementar is not null then tacs.nome else tac.nome end as tipoAtividadeComplementarNome,");
		sb.append("    case when gradecurriculartipoatividadecomplementarsuperior.codigo is not null then tacs.codigo else tac.codigo end as tipoAtividadeComplementar,    ");
		sb.append("    case when sum(racm.cargaHorariaEvento) > gc.totalcargahorariaatividadecomplementar then gc.totalcargahorariaatividadecomplementar else sum(racm.cargaHorariaEvento) end as cargaHorariaEvento, ");
		sb.append("    case when sum(racm.cargaHorariaConsiderada) > gradecurriculartipoatividadecomplementar.cargahoraria then gradecurriculartipoatividadecomplementar.cargahoraria else sum(racm.cargaHorariaConsiderada) end as cargaHorariaConsiderada,  ");
		sb.append("    case when tacs.codigo is null then gradecurriculartipoatividadecomplementar.cargahoraria else gradecurriculartipoatividadecomplementarsuperior.cargahoraria end cargahorariamae,  ");
		sb.append("    gradecurriculartipoatividadecomplementar.cargahoraria as cargahoariamaximafilha  ");
		sb.append(" from registroatividadecomplementarmatricula as racm ");
		sb.append(" inner join tipoatividadecomplementar as tac on tac.codigo = racm.tipoatividadecomplementar");
		sb.append(" inner join matricula as m on m.matricula = racm.matricula");
		sb.append(" inner join gradecurricular as gc on gc.codigo = m.gradecurricularatual");
		sb.append(" left join tipoatividadecomplementar as tacs on tacs.codigo = tac.tipoAtividadeComplementarSuperior");
		sb.append(" inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.gradecurricular = gc.codigo ");
		sb.append(" and gradecurriculartipoatividadecomplementar.tipoatividadecomplementar = tac.codigo ");

		sb.append(" left join gradecurriculartipoatividadecomplementar as gradecurriculartipoatividadecomplementarsuperior  on gradecurriculartipoatividadecomplementarsuperior.gradecurricular = gc.codigo ");
		sb.append(" and gradecurriculartipoatividadecomplementarsuperior.tipoatividadecomplementar = tacs.codigo and gradecurriculartipoatividadecomplementar.cargahoraria > 0 ");
		sb.append(" where racm.matricula = '").append(matricula).append("'");
		sb.append(" and racm.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");
		sb.append(" group by racm.matricula, 1, 2, gc.totalcargahorariaatividadecomplementar, gradecurriculartipoatividadecomplementar.cargahoraria, tac.codigo, tacs.codigo, gradecurriculartipoatividadecomplementarsuperior.cargahoraria, gradecurriculartipoatividadecomplementarsuperior.codigo ");
		sb.append(" ) as t group by t.tipoAtividadeComplementarNome, t.tipoAtividadeComplementar, cargahorariamae ");
		sb.append(" order by 1"); 

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<RegistroAtividadeComplementarMatriculaVO> vetResultado = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		while (tabelaResultado.next()) {
			RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
			obj.getTipoAtividadeComplementarVO().setNome(tabelaResultado.getString("tipoAtividadeComplementarNome"));
			obj.getTipoAtividadeComplementarVO().setCodigo(tabelaResultado.getInt("tipoAtividadeComplementar"));
			obj.setCargaHorariaEvento(tabelaResultado.getInt("cargaHorariaEvento"));
			obj.setCargaHorariaConsiderada(tabelaResultado.getInt("cargaHorariaConsiderada"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirComBaseNaMatricula(String matricula, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM registroatividadecomplementarmatricula WHERE matricula = '").append(matricula).append("' ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} finally {
			sqlStr = null;
		}
	}
	
	public Boolean consultarSeExisteRegistroVinculadoATipoAtividadeComplementar(Integer gradecurricular, Integer tipoAtividadeComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select 1 from registroatividadecomplementarmatricula racm ")
			.append(" inner join gradecurriculartipoatividadecomplementar gctac on gctac.tipoatividadecomplementar = racm.tipoatividadecomplementar ")
			.append(" inner join matricula on matricula.matricula = racm.matricula ")
			.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ")
			.append(" and matriculaperiodo.gradecurricular = gctac.gradecurricular ")
			.append(" where racm.tipoatividadecomplementar = ? ")
			.append(" and gctac.gradecurricular = ?")
			.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { tipoAtividadeComplementar , gradecurricular });
		return tabelaResultado.next();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarObservacao(final RegistroAtividadeComplementarMatriculaVO obj, UsuarioVO usuario) throws Exception {
		try {			
			final String sql = "UPDATE registroAtividadeComplementarMatricula set observacao = ? where codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);					
					sqlAlterar.setString(1, obj.getObservacao());
					sqlAlterar.setInt(2, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarDeferimento(RegistroAtividadeComplementarMatriculaVO obj, UsuarioVO usuario) throws Exception {
		verificarPermissaoUsuarioFuncionalidadeComUsuarioVO("RegistroAtividadeComplementarMatricula_permiteDeferir", usuario);
		obj.setSituacaoAtividadeComplementarMatricula(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO);
		obj.setDataDeferimentoIndeferimento(new Date());
		obj.getResponsavelDeferimentoIndeferimento().setCodigo(usuario.getCodigo());
		obj.getResponsavelDeferimentoIndeferimento().setNome(usuario.getNome());
		alterarSituacao(obj, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarIndeferimento(RegistroAtividadeComplementarMatriculaVO obj, UsuarioVO usuario) throws Exception{
		verificarPermissaoUsuarioFuncionalidadeComUsuarioVO("RegistroAtividadeComplementarMatricula_permiteIndeferir", usuario);
		if(obj.getMotivoIndeferimento().trim().isEmpty()) {
			throw new Exception("O campo MOTIVO INDEFERIMENTO deve ser informado.");
		}
		obj.setSituacaoAtividadeComplementarMatricula(SituacaoAtividadeComplementarMatriculaEnum.INDEFERIDO);
		obj.setDataDeferimentoIndeferimento(new Date());
		obj.getResponsavelDeferimentoIndeferimento().setCodigo(usuario.getCodigo());
		obj.getResponsavelDeferimentoIndeferimento().setNome(usuario.getNome());
		
		alterarSituacao(obj, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacao(final RegistroAtividadeComplementarMatriculaVO obj, UsuarioVO usuario) throws Exception {
		try {		
			
			final String sql = "UPDATE registroAtividadeComplementarMatricula set situacaoAtividadeComplementarMatricula = ?, responsavelDeferimentoIndeferimento = ?, dataDeferimentoIndeferimento = ?, motivoIndeferimento = ?  where codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);					
					sqlAlterar.setString(1, obj.getSituacaoAtividadeComplementarMatricula().name());
					if(Uteis.isAtributoPreenchido(obj.getResponsavelDeferimentoIndeferimento().getCodigo())  && !obj.getSituacaoAtividadeComplementarMatricula().equals(SituacaoAtividadeComplementarMatriculaEnum.AGUARDANDO_DEFERIMENTO)) {
						sqlAlterar.setInt(2, obj.getResponsavelDeferimentoIndeferimento().getCodigo());
					}else {
						sqlAlterar.setNull(2,0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataDeferimentoIndeferimento()) && !obj.getSituacaoAtividadeComplementarMatricula().equals(SituacaoAtividadeComplementarMatriculaEnum.AGUARDANDO_DEFERIMENTO)) {
						sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataDeferimentoIndeferimento()));
					}else {
						sqlAlterar.setNull(3,0);
					}
					if(obj.getSituacaoAtividadeComplementarMatricula().equals(SituacaoAtividadeComplementarMatriculaEnum.INDEFERIDO)) {
						sqlAlterar.setString(4, obj.getMotivoIndeferimento());
					}else {
						sqlAlterar.setString(4, "");
					}
					sqlAlterar.setInt(5, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Integer consultarQuantidadeAtividadeComplementarPorUsuarioAluno(String matricula) throws Exception {
		try {
			String sql = "SELECT COUNT(codigo) as qtde FROM registroatividadecomplementarmatricula WHERE matricula = ?";
			
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  matricula);
	        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public Boolean realizarValidacaoIntegracaoAtividadeComplementar(String matricula, Integer matrizCurricular) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		
		sqlStr.append(" select * from ( ");
		sqlStr.append(" select sum(cargahorariaexigida) as cargahorariaexigida, sum(cargaHorariaRealizadaAtividadeComplementar) as cargaHorariaRealizadaAtividadeComplementar, ");
		sqlStr.append(" sum(cargaHorariaConsiderada) as cargaHorariaConsiderada, ");
		sqlStr.append(" case when sum(cargahorariapendente-cargahorariaexcedida) < 0 then  0 else sum(cargahorariapendente-cargahorariaexcedida) end as cargahorariapendente");
		sqlStr.append(" from (");
		consultarRegistroAtividadeComplementarMatricula(matricula, matrizCurricular, sqlStr);
		sqlStr.append("	union all ");
		consultarHorasMinimaAtividadeComplementar(matricula, matrizCurricular, sqlStr);
		sqlStr.append(") as atividade ");
		sqlStr.append(" ) as resultado ");
		sqlStr.append(" where (resultado.cargahorariaexigida is null and resultado.cargahorariarealizadaatividadecomplementar is null and resultado.cargahorariaconsiderada is null and resultado.cargahorariapendente is null ) = false ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("cargahorariapendente").equals("0") || tabelaResultado.getString("cargahorariapendente").equals("0.0");
		}
		
		return false;
	}
	@Override
	public void consultarRegistroAtividadeComplementarMatricula(String matricula, Integer matrizCurricular, StringBuilder sqlStr) {
		sqlStr.append("	select 'QUALQUER' as tipo, cargahorariaexigida, sum(cargaHorariaRealizadaAtividadeComplementar) as cargaHorariaRealizadaAtividadeComplementar,");
		sqlStr.append("	case when sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) > cargahorariaexigida");
		sqlStr.append("	then cargahorariaexigida else sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) end  as cargaHorariaConsiderada,");
		sqlStr.append(" case when sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) < cargahorariaexigida");
		sqlStr.append(" then cargahorariaexigida - sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) else 0.0 end as cargahorariapendente, ");
		sqlStr.append(" 0 as cargahorariaexcedida");
		sqlStr.append("	from (");
		sqlStr.append("	select coalesce(sum(racm.cargahorariaconsiderada), 0) as cargaHorariaRealizadaAtividadeComplementar, gctac.tipoatividadecomplementar as tipoAtividadeComplementar, gctac.cargahoraria, ");
		sqlStr.append("	gradecurricular.totalcargahorariaatividadecomplementar  - coalesce ((");
		sqlStr.append("	select sum(horasminimasexigida) from gradecurriculartipoatividadecomplementar where gradecurriculartipoatividadecomplementar.gradecurricular = gradecurricular.codigo");
		sqlStr.append("	and gradecurriculartipoatividadecomplementar.horasminimasexigida > 0.0");
		sqlStr.append("	),0) as cargahorariaexigida ");
		sqlStr.append("	from gradecurriculartipoatividadecomplementar gctac ");
		sqlStr.append("	inner join gradecurricular on gradecurricular.codigo = gctac.gradecurricular ");
		sqlStr.append("	left join registroatividadecomplementarmatricula racm on gctac.tipoAtividadeComplementar = racm.tipoAtividadeComplementar ");
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" and racm.matricula = '").append(matricula).append("'");	
		}
		else {
			sqlStr.append(" and racm.matricula = matricula.matricula");
		}
		if (Uteis.isAtributoPreenchido(matrizCurricular)) {
			sqlStr.append("	where  gctac.gradecurricular = ").append(matrizCurricular).append(" and gctac.horasminimasexigida = 0.0 ");	
		}else {
			sqlStr.append("	where  gctac.gradecurricular = matricula.gradecurricularatual").append(" and gctac.horasminimasexigida = 0.0 ");
		}
		sqlStr.append("	and racm.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");
		sqlStr.append("	and gctac.horasminimasexigida = 0.0");
		sqlStr.append("	group by gctac.tipoAtividadeComplementar, 	gctac.cargahoraria , gradecurricular.totalcargahorariaatividadecomplementar, gradecurricular.codigo ");
		sqlStr.append("	) as atividade group by cargahorariaexigida");
	}
	@Override
	public void consultarHorasMinimaAtividadeComplementar(String matricula, Integer matrizCurricular, StringBuilder sqlStr) {
		sqlStr.append("	select tipoAtividadeComplementar.nome  as tipo, gctac.horasminimasexigida as cargahorariaexigida,coalesce(sum(racm.cargahorariaconsiderada), 0) as cargaHorariaRealizadaAtividadeComplementar, ");
		sqlStr.append("	case when coalesce(sum(racm.cargahorariaconsiderada), 0) > gctac.cargahoraria then gctac.cargahoraria else coalesce(sum(racm.cargahorariaconsiderada), 0)  end  as cargaHorariaConsiderada, ");
		sqlStr.append("	case when coalesce(sum(racm.cargahorariaconsiderada), 0) < gctac.horasminimasexigida then gctac.horasminimasexigida - coalesce(sum(racm.cargahorariaconsiderada), 0) else 0 end as 	cargahorariapendente,");
		sqlStr.append("	case when  (case when coalesce(sum(racm.cargahorariaconsiderada), 0) > gctac.cargahoraria then gctac.cargahoraria else coalesce(sum(racm.cargahorariaconsiderada), 0) end) > gctac.horasminimasexigida ");
		sqlStr.append("	then (case when coalesce(sum(racm.cargahorariaconsiderada), 0) > gctac.cargahoraria then gctac.cargahoraria else coalesce(sum(racm.cargahorariaconsiderada), 0) end) - gctac.horasminimasexigida ");
		sqlStr.append("	else 0 end as cargahorariaexcedida");
		sqlStr.append("	from gradecurriculartipoatividadecomplementar gctac   ");
		sqlStr.append("	inner join gradecurricular on gradecurricular.codigo = gctac.gradecurricular");
		sqlStr.append("	inner join tipoAtividadeComplementar on tipoAtividadeComplementar.codigo = gctac.tipoAtividadeComplementar");
		sqlStr.append("	left join registroatividadecomplementarmatricula racm on gctac.tipoAtividadeComplementar = racm.tipoAtividadeComplementar ");
		if (Uteis.isAtributoPreenchido(matrizCurricular)) {
			sqlStr.append("	where  gctac.gradecurricular = ").append(matrizCurricular).append(" and gctac.horasminimasexigida > 0.0 ");	
		}else {
			sqlStr.append("	where  gctac.gradecurricular = matricula.gradecurricularatual").append(" and gctac.horasminimasexigida > 0.0 ");
		}
		if (Uteis.isAtributoPreenchido(matrizCurricular)) {
			if (Uteis.isAtributoPreenchido(matricula)) {
				sqlStr.append(" and racm.matricula = '").append(matricula).append("'");	
			}			
		} else {
			sqlStr.append(" and racm.matricula = matricula.matricula ");
		}
		sqlStr.append("	and racm.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");
		sqlStr.append("	group by gctac.tipoAtividadeComplementar, 	gctac.cargahoraria , gradecurricular.totalcargahorariaatividadecomplementar, gradecurricular.codigo, gctac.horasminimasexigida, ");
		sqlStr.append("	tipoAtividadeComplementar.nome ");
	}
	
	@Override
	public List<RegistroAtividadeComplementarMatriculaVO> consultarAtividadeComplementarObrigatoriaMatriz(String matricula, Integer matrizCurricular) throws Exception{
		StringBuilder sql = new StringBuilder();
		consultarHorasMinimaAtividadeComplementar(matricula, matrizCurricular, sql);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<RegistroAtividadeComplementarMatriculaVO> registroAtividadeComplementarMatriculaVOs = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		while (tabelaResultado.next()) {
			RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
			obj.setCargaHorariaPendente(Integer.parseInt(tabelaResultado.getString("cargahorariapendente")));
			obj.setCargaHorariaMinimaExigida(Integer.parseInt(tabelaResultado.getString("cargahorariaexigida")));
			obj.setCargaHorariaRealizada(Integer.parseInt(tabelaResultado.getString("cargaHorariaRealizadaAtividadeComplementar")));
			obj.setCargaHorariaConsiderada(Integer.parseInt(tabelaResultado.getString("cargahorariaconsiderada")));
			obj.setCargaHorariaExcedida(tabelaResultado.getString("cargahorariaexcedida"));
			obj.setTipoAtividadeComplementarApresentar(tabelaResultado.getString("tipo"));
			registroAtividadeComplementarMatriculaVOs.add(obj);
		}
		return registroAtividadeComplementarMatriculaVOs;
	}
	
	@Override
	public List<RegistroAtividadeComplementarMatriculaVO> consultarRegistroAtividadeComplementarHistoricoPorMatricula(String matricula,Integer codigoGradeCurricular, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT tipoAtividadeComplementarNome, codigoTipoAtividadeComplementar, ");
		sb.append(" SUM(cargaHorariaRealizadaAtividadeComplementar) cargaHorariaRealizadaAtividadeComplementar, ");
		sb.append(" SUM(cargaHorariaConsiderada) cargaHorariaConsiderada ");
		sb.append(" FROM ( ");
		sb.append("	select");
		sb.append("		tipoAtividadeComplementarNome,");
		sb.append("		codigoTipoAtividadeComplementar,");
		sb.append("		cargahorariaexigida,");
		sb.append("		sum(cargaHorariaRealizadaAtividadeComplementar) as cargaHorariaRealizadaAtividadeComplementar,");
		sb.append("		case");
		sb.append("			when sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) > cargahorariaexigida then cargahorariaexigida");
		sb.append("			else sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end )");
		sb.append("		end as cargaHorariaConsiderada,");
		sb.append("		case");
		sb.append("			when sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) < cargahorariaexigida then cargahorariaexigida - sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end )");
		sb.append("			else 0.0");
		sb.append("		end as cargahorariapendente,");
		sb.append("		0 as cargahorariaexcedida");
		sb.append("	from");
		sb.append("		(");
		sb.append("		select");
		sb.append("			coalesce(sum(racm.cargahorariaconsiderada), 0) as cargaHorariaRealizadaAtividadeComplementar,");
		sb.append("			gctac.tipoatividadecomplementar as tipoAtividadeComplementar,");
		sb.append("			gctac.cargahoraria,");
		sb.append("			case");
		sb.append("			when gradecurriculartipoatividadecomplementarsuperior.tipoatividadecomplementar is not null then tacs.nome");
		sb.append("			else tac.nome");
		sb.append("		end as tipoAtividadeComplementarNome,");
		sb.append("		case");
		sb.append("			when gradecurriculartipoatividadecomplementarsuperior.codigo is not null then tacs.codigo");
		sb.append("			else tac.codigo");
		sb.append("		end as codigoTipoAtividadeComplementar,");
		sb.append("			gradecurricular.totalcargahorariaatividadecomplementar - (");
		sb.append("			select");
		sb.append("				case when sum(horasminimasexigida) is not null then sum(horasminimasexigida) else 0 end");
		sb.append("			from");
		sb.append("				gradecurriculartipoatividadecomplementar");
		sb.append("			where");
		sb.append("				gradecurriculartipoatividadecomplementar.gradecurricular = gradecurricular.codigo");
		sb.append("				and gradecurriculartipoatividadecomplementar.horasminimasexigida > 0.0 ) as cargahorariaexigida");
		sb.append("		from");
		sb.append("			gradecurriculartipoatividadecomplementar gctac");
		sb.append("		inner join gradecurricular on");
		sb.append("			gradecurricular.codigo = gctac.gradecurricular");
		sb.append("		left join registroatividadecomplementarmatricula racm on");
		sb.append("			gctac.tipoAtividadeComplementar = racm.tipoAtividadeComplementar");
		sb.append("			and matricula = '").append(matricula).append("'");
		sb.append("		inner join tipoatividadecomplementar as tac on");
		sb.append("		tac.codigo = racm.tipoatividadecomplementar");
		sb.append("		left join tipoatividadecomplementar as tacs on");
		sb.append("		tacs.codigo = tac.tipoAtividadeComplementarSuperior");
		sb.append("		left join gradecurriculartipoatividadecomplementar as gradecurriculartipoatividadecomplementarsuperior on");
		sb.append("		gradecurriculartipoatividadecomplementarsuperior.gradecurricular = gradecurricular.codigo");
		sb.append("		and gradecurriculartipoatividadecomplementarsuperior.tipoatividadecomplementar = tacs.codigo");
		sb.append("		and gctac.cargahoraria > 0	");
		sb.append("		where");
		sb.append("			gctac.gradecurricular = ").append(codigoGradeCurricular);
		sb.append("			and gctac.horasminimasexigida = 0.0");
		sb.append("			and gctac.horasminimasexigida = 0.0");
		sb.append("		group by");
		sb.append("			gctac.tipoAtividadeComplementar,");
		sb.append("			gctac.cargahoraria ,");
		sb.append("			gradecurricular.totalcargahorariaatividadecomplementar,");
		sb.append("			gradecurricular.codigo,");
		sb.append("			tipoAtividadeComplementarNome,");
		sb.append("			codigoTipoAtividadeComplementar) as atividade");
		sb.append("	group by");
		sb.append("		cargahorariaexigida , tipoAtividadeComplementarNome , codigoTipoAtividadeComplementar");
		sb.append(" union all");
		sb.append("	select");
		sb.append("		tipoAtividadeComplementar.nome as tipo,");
		sb.append("		tipoAtividadeComplementar.codigo as codigoTipoAtividadeComplementar,");
		sb.append("		gctac.horasminimasexigida as cargahorariaexigida,");
		sb.append("		coalesce(sum(racm.cargahorariaconsiderada), 0) as cargaHorariaRealizadaAtividadeComplementar,");
		sb.append("		case");
		sb.append("			when coalesce(sum(racm.cargahorariaconsiderada), 0) > gctac.cargahoraria then gctac.cargahoraria");
		sb.append("			else coalesce(sum(racm.cargahorariaconsiderada), 0)");
		sb.append("		end as cargaHorariaConsiderada,");
		sb.append("		case");
		sb.append("			when coalesce(sum(racm.cargahorariaconsiderada), 0) < gctac.horasminimasexigida then gctac.horasminimasexigida - coalesce(sum(racm.cargahorariaconsiderada), 0)");
		sb.append("			else 0");
		sb.append("		end as cargahorariapendente,");
		sb.append("		case");
		sb.append("			when");
		sb.append("			(case");
		sb.append("				when coalesce(sum(racm.cargahorariaconsiderada), 0) > gctac.cargahoraria then gctac.cargahoraria");
		sb.append("				else coalesce(sum(racm.cargahorariaconsiderada), 0)");
		sb.append("			end) > gctac.horasminimasexigida then");
		sb.append("			(case");
		sb.append("				when coalesce(sum(racm.cargahorariaconsiderada), 0) > gctac.cargahoraria then gctac.cargahoraria");
		sb.append("				else coalesce(sum(racm.cargahorariaconsiderada), 0)");
		sb.append("			end) - gctac.horasminimasexigida");
		sb.append("			else 0");
		sb.append("		end as cargahorariaexcedida");
		sb.append("	from");
		sb.append("		gradecurriculartipoatividadecomplementar gctac");
		sb.append("	inner join gradecurricular on");
		sb.append("		gradecurricular.codigo = gctac.gradecurricular");
		sb.append("	inner join tipoAtividadeComplementar on");
		sb.append("		tipoAtividadeComplementar.codigo = gctac.tipoAtividadeComplementar");
		sb.append("	left join registroatividadecomplementarmatricula racm on");
		sb.append("		gctac.tipoAtividadeComplementar = racm.tipoAtividadeComplementar");
		sb.append("		and matricula = '").append(matricula).append("'");;
		sb.append("	where");
		sb.append("		gctac.gradecurricular = ").append(codigoGradeCurricular);;
		sb.append("		and gctac.horasminimasexigida > 0.0");
		sb.append("	group by");
		sb.append("		gctac.tipoAtividadeComplementar,");
		sb.append("		gctac.cargahoraria ,");
		sb.append("		gradecurricular.totalcargahorariaatividadecomplementar,");
		sb.append("		gradecurricular.codigo,");
		sb.append("		gctac.horasminimasexigida,");
		sb.append("		tipoAtividadeComplementar.nome,");
		sb.append("		tipoAtividadeComplementar.codigo");
		sb.append(" ) AS t");
		sb.append(" GROUP BY tipoAtividadeComplementarNome, codigoTipoAtividadeComplementar ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<RegistroAtividadeComplementarMatriculaVO> vetResultado = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		while (tabelaResultado.next()) {
			RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
			obj.getTipoAtividadeComplementarVO().setNome(tabelaResultado.getString("tipoAtividadeComplementarNome"));
			obj.getTipoAtividadeComplementarVO().setCodigo(tabelaResultado.getInt("codigotipoAtividadeComplementar"));
			obj.setCargaHorariaEvento(tabelaResultado.getInt("cargaHorariaRealizadaAtividadeComplementar"));
			obj.setCargaHorariaConsiderada(tabelaResultado.getInt("cargaHorariaConsiderada"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCargaHorariaConsiderada(final RegistroAtividadeComplementarMatriculaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE registroAtividadeComplementarMatricula set cargaHorariaConsiderada = ?, justificativaAlteracaoCHConsiderada = ?, responsavelEditarCHConsiderada = ?, dataEditarCHConsiderada = ? where codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getCargaHorariaConsiderada());
					sqlAlterar.setString(2, obj.getJustificativaAlteracaoCHConsiderada());
					sqlAlterar.setInt(3, obj.getResponsavelEditarCHConsiderada().getCodigo());
					if(Uteis.isAtributoPreenchido(obj.getDataEditarCHConsiderada())) {
						sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataEditarCHConsiderada()));
					}else {
						sqlAlterar.setNull(4,0);
					}
					sqlAlterar.setInt(5, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<RegistroAtividadeComplementarMatriculaVO> consultarRegistrosAprovadosPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select t.tipoAtividadeComplementarNome, t.codigo, t.tipoAtividadeComplementar, t.matricula from ( "); 
		sb.append(" select case when gradecurriculartipoatividadecomplementarsuperior.tipoatividadecomplementar is not null then tacs.nome else tac.nome end as tipoAtividadeComplementarNome,");
		sb.append(" case when gradecurriculartipoatividadecomplementarsuperior.codigo is not null then tacs.codigo else tac.codigo end as tipoAtividadeComplementar,"); 
		sb.append(" racm.matricula, registroatividadecomplementar.codigo");
		sb.append(" from registroatividadecomplementarmatricula as racm");
		sb.append(" inner join registroatividadecomplementar on registroatividadecomplementar.codigo = racm.registroatividadecomplementar");
		sb.append(" inner join tipoatividadecomplementar as tac on tac.codigo = racm.tipoatividadecomplementar");
		sb.append(" inner join matricula as m on m.matricula = racm.matricula");
		sb.append(" inner join gradecurricular as gc on gc.codigo = m.gradecurricularatual");
		sb.append(" left join tipoatividadecomplementar as tacs on tacs.codigo = tac.tipoAtividadeComplementarSuperior"); 
		sb.append(" inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.gradecurricular = gc.codigo"); 
		sb.append(" and gradecurriculartipoatividadecomplementar.tipoatividadecomplementar = tac.codigo");
		sb.append(" left join gradecurriculartipoatividadecomplementar as gradecurriculartipoatividadecomplementarsuperior on gradecurriculartipoatividadecomplementarsuperior.gradecurricular = gc.codigo");
		sb.append(" and gradecurriculartipoatividadecomplementarsuperior.tipoatividadecomplementar = tacs.codigo and gradecurriculartipoatividadecomplementar.cargahoraria > 0"); 
		sb.append(" where racm.matricula = '").append(matricula).append("'");
		sb.append(" AND racm.situacaoAtividadeComplementarMatricula <> 'INDEFERIDO'");
		sb.append(" group by racm.matricula, 1, 2, gc.totalcargahorariaatividadecomplementar, tac.codigo, tacs.codigo, gradecurriculartipoatividadecomplementarsuperior.codigo, registroatividadecomplementar.codigo"); 
		sb.append(" ) as t group by t.tipoAtividadeComplementarNome, t.codigo, t.tipoAtividadeComplementar, t.matricula");  
		sb.append(" order by 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<RegistroAtividadeComplementarMatriculaVO> vetResultado = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		while (tabelaResultado.next()) {
			RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
			obj.getTipoAtividadeComplementarVO().setNome(tabelaResultado.getString("tipoAtividadeComplementarNome"));
			obj.getTipoAtividadeComplementarVO().setCodigo(tabelaResultado.getInt("tipoAtividadeComplementar"));
			obj.getRegistroAtividadeComplementar().setCodigo(tabelaResultado.getInt("codigo"));
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	
}