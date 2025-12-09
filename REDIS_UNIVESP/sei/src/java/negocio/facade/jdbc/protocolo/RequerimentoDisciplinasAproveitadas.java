package negocio.facade.jdbc.protocolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoDisciplinasAproveitadasVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.enumeradores.SituacaoRequerimentoDisciplinasAproveitadasEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.RequerimentoDisciplinasAproveitadasInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RequerimentoDisciplinasAproveitadas extends ControleAcesso implements RequerimentoDisciplinasAproveitadasInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8059890354194824887L;
	private static String idEntidade = " Requerimento";

	public static String getIdEntidade() {
		return RequerimentoDisciplinasAproveitadas.idEntidade;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<RequerimentoDisciplinasAproveitadasVO> lista, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) {
		try {
			for (RequerimentoDisciplinasAproveitadasVO obj : lista) {
				if (!Uteis.isAtributoPreenchido(obj.getNomeDisciplinaCursada())) {
					throw new Exception("O campo NOME DISCIPLINA CURSADA da disciplina  "+obj.getDisciplina().getNome()+" deve ser informado");
				}
				if (obj.getCodigo() == 0) {
					incluir(obj, verificarAcesso, configuracaoGeralSistema, usuarioVO);
					//getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().verificarAlunoDisciplinaRequerimentoInsert(lista, usuarioVO, configuracaoGeralSistema);
				} else {
					alterar(obj, verificarAcesso, configuracaoGeralSistema, usuarioVO);
					//getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().verificarAlunoDisciplinaRequerimentoInsert(lista, usuarioVO, configuracaoGeralSistema);
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RequerimentoDisciplinasAproveitadasVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) {
		try {
			RequerimentoDisciplinasAproveitadas.incluir(getIdEntidade(), verificarAcesso, usuario);
			if (obj.getArquivoPlanoEnsino().getPastaBaseArquivoEnum() != null && !Uteis.isAtributoPreenchido(obj.getArquivoPlanoEnsino().getCodigo())) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoPlanoEnsino(), false, usuario, configuracaoGeralSistema);
			} 
			incluir(obj, "requerimentoDisciplinasAproveitadas", new AtributoPersistencia()
					.add("requerimento", obj.getRequerimentoVO())
					.add("arquivoPlanoEnsino", obj.getArquivoPlanoEnsino())
					.add("situacaoRequerimentoDisciplinasAproveitadasEnum", obj.getSituacaoRequerimentoDisciplinasAproveitadasEnum())
					.add("tipo", obj.getTipo())
					.add("disciplina", obj.getDisciplina())
					.add("nomeDisciplinaCursada", obj.getNomeDisciplinaCursada())
					.add("cargahorariacursada", obj.getCargaHorariaCursada())
					.add("cargahoraria", obj.getCargaHoraria())
					.add("nota", obj.getNota())
					.add("frequencia", obj.getFrequencia())
					.add("ano", obj.getAno())
					.add("semestre", obj.getSemestre())										
					.add("instituicao", obj.getInstituicao())
					.add("utilizanotaconceito", obj.getUtilizaNotaConceito())
					.add("mediafinalconceito", obj.getMediaFinalConceito())
					.add("cidade", obj.getCidade())
					.add("situacaohistorico", obj.getSituacaoHistorico())
					.add("motivoRevisaoAnaliseAproveitamento", obj.getMotivoRevisaoAnaliseAproveitamento())
					.add("nomeprofessor", obj.getNomeProfessor())
					.add("titulacaoprofessor", obj.getTitulacaoProfessor())
					.add("sexoprofessor", obj.getSexoProfessor()),
					usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RequerimentoDisciplinasAproveitadasVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) {
		try {
			RequerimentoDisciplinasAproveitadas.alterar(getIdEntidade(), verificarAcesso, usuario);
			if (obj.getArquivoPlanoEnsino().getPastaBaseArquivoEnum() != null && Uteis.isAtributoPreenchido(obj.getArquivoPlanoEnsino().getCodigo())) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoPlanoEnsino(), false, usuario, configuracaoGeralSistema);
			}
			alterar(obj, "requerimentoDisciplinasAproveitadas", new AtributoPersistencia()
					.add("requerimento", obj.getRequerimentoVO())
					.add("arquivoPlanoEnsino", obj.getArquivoPlanoEnsino())
					.add("situacaoRequerimentoDisciplinasAproveitadasEnum", obj.getSituacaoRequerimentoDisciplinasAproveitadasEnum())
					.add("tipo", obj.getTipo())
					.add("disciplina", obj.getDisciplina())
					.add("nomeDisciplinaCursada", obj.getNomeDisciplinaCursada())
					.add("cargahorariacursada", obj.getCargaHorariaCursada())
					.add("cargahoraria", obj.getCargaHoraria())
					.add("nota", obj.getNota())
					.add("frequencia", obj.getFrequencia())
					.add("ano", obj.getAno())
					.add("semestre", obj.getSemestre())										
					.add("instituicao", obj.getInstituicao())
					.add("utilizanotaconceito", obj.getUtilizaNotaConceito())
					.add("mediafinalconceito", obj.getMediaFinalConceito())
					.add("cidade", obj.getCidade())					
					.add("situacaohistorico", obj.getSituacaoHistorico())
					.add("motivoRevisaoAnaliseAproveitamento", obj.getMotivoRevisaoAnaliseAproveitamento())
					.add("nomeprofessor", obj.getNomeProfessor())
					.add("titulacaoprofessor", obj.getTitulacaoProfessor())
					.add("sexoprofessor", obj.getSexoProfessor())
					.add("bloquearnovasolicitacoes", obj.isBloquearNovaSolicitacoes()),
					new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarConfirmacaoIndeferido(RequerimentoDisciplinasAproveitadasVO obj,  UsuarioVO usuario) throws Exception {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getMotivoSituacao()), "O campo Motivo de Deferimento/Indeferimento deve ser informado.");
			obj.setSituacaoRequerimentoDisciplinasAproveitadasEnum(SituacaoRequerimentoDisciplinasAproveitadasEnum.INDEFERIDO);
			obj.setResponsavelIndeferimento(usuario);
			obj.setDataIndeferimento(new Date());
			obj.setResponsavelDeferimento(null);
			obj.setDataDeferimento(null);
			Integer qtdRequerimentos = getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().consultarQtdeRequerimentoDisciplinasAproveitadasPorDisciplinaPorMatriculaPorSituacaoIndeferida(obj.getDisciplina().getCodigo(), obj.getRequerimentoVO().getMatricula().getMatricula());
			obj.setQtdIndeferimentos(qtdRequerimentos+1);			
			if (Uteis.isAtributoPreenchido(obj.getRequerimentoVO().getTipoRequerimento().getQtdeMaximaIndeferidoAproveitamento()) && (obj.getQtdIndeferimentos() >= obj.getRequerimentoVO().getTipoRequerimento().getQtdeMaximaIndeferidoAproveitamento())) {
				obj.setBloquearNovaSolicitacoes(Boolean.TRUE);
			}
			alterar(obj, "requerimentoDisciplinasAproveitadas", new AtributoPersistencia()
					.add("motivoSituacao", obj.getMotivoSituacao()) 
					.add("bloquearNovaSolicitacoes", obj.isBloquearNovaSolicitacoes()) 
					.add("situacaoRequerimentoDisciplinasAproveitadasEnum", obj.getSituacaoRequerimentoDisciplinasAproveitadasEnum())
					.add("dataIndeferimento", obj.getDataIndeferimento())
					.add("responsavelIndeferimento", obj.getResponsavelIndeferimento())
					.add("dataDeferimento", null)
					.add("responsavelDeferimento", null)
					.add("disciplina", obj.getDisciplina())
					.add("nota", obj.getNota())
					.add("frequencia", obj.getFrequencia())
					.add("requerimento", obj.getRequerimentoVO().getCodigo())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarConfirmacaoDeferido(RequerimentoDisciplinasAproveitadasVO obj, UsuarioVO usuario) throws Exception {
		try {
			obj.setSituacaoRequerimentoDisciplinasAproveitadasEnum(SituacaoRequerimentoDisciplinasAproveitadasEnum.DEFERIDO);
			obj.setResponsavelDeferimento(usuario);
			obj.setDataDeferimento(new Date());
			obj.setBloquearNovaSolicitacoes(false);
			obj.setResponsavelIndeferimento(null);
			obj.setDataIndeferimento(null);
			alterar(obj, "requerimentoDisciplinasAproveitadas", new AtributoPersistencia()
					.add("motivoSituacao", obj.getMotivoSituacao())
					.add("bloquearNovaSolicitacoes", false)
					.add("situacaoRequerimentoDisciplinasAproveitadasEnum", obj.getSituacaoRequerimentoDisciplinasAproveitadasEnum())
					.add("dataDeferimento", obj.getDataDeferimento())
					.add("responsavelDeferimento", obj.getResponsavelDeferimento())
					.add("dataIndeferimento", null)
					.add("responsavelIndeferimento", null)
					.add("disciplina", obj.getDisciplina())
					.add("nota", obj.getNota())
					.add("frequencia", obj.getFrequencia())
					.add("requerimento", obj.getRequerimentoVO().getCodigo())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
			obj.setQtdIndeferimentos(getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().consultarQtdeRequerimentoDisciplinasAproveitadasPorDisciplinaPorMatriculaPorSituacaoIndeferida(obj.getDisciplina().getCodigo(), obj.getRequerimentoVO().getMatricula().getMatricula()));
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsulta() {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" rda.codigo as \"rda.codigo\",   ");
		sql.append(" rda.nota as \"rda.nota\",  rda.frequencia as \"rda.frequencia\",  ");
		sql.append(" rda.ano as \"rda.ano\", rda.semestre as \"rda.semestre\", ");
		sql.append(" rda.instituicao as \"rda.instituicao\", rda.bloquearnovasolicitacoes as \"rda.bloquearnovasolicitacoes\", ");
		sql.append(" rda.utilizanotaconceito as \"rda.utilizanotaconceito\",  rda.mediafinalconceito as \"rda.mediafinalconceito\", ");	
		sql.append(" rda.cargahorariacursada as \"rda.cargahorariacursada\", rda.nomedisciplinacursada as \"rda.nomedisciplinacursada\", ");
		sql.append(" rda.cargahoraria as \"rda.cargahoraria\", rda.tipo as \"rda.tipo\", rda.situacaohistorico as \"rda.situacaohistorico\", ");
		sql.append(" rda.nomeprofessor as \"rda.nomeprofessor\", rda.titulacaoprofessor as \"rda.titulacaoprofessor\",  rda.sexoprofessor as \"rda.sexoprofessor\", ");
		sql.append(" rda.requerimento as \"rda.requerimento\",  rda.situacaoRequerimentoDisciplinasAproveitadasEnum as \"rda.situacaoRequerimentoDisciplinasAproveitadasEnum\", ");
		sql.append(" rda.motivoSituacao as \"rda.motivoSituacao\",  rda.motivoRevisaoAnaliseAproveitamento as \"rda.motivoRevisaoAnaliseAproveitamento\", ");
		sql.append(" rda.dataDeferimento as \"rda.dataDeferimento\",  rda.dataIndeferimento as \"rda.dataIndeferimento\",  ");

		sql.append(" disciplina.codigo as \"disciplina.codigo\",  disciplina.nome as \"disciplina.nome\",  ");
		
		sql.append(" cidade.codigo as \"cidade.codigo\",  cidade.nome as \"cidade.nome\",  ");
		sql.append(" estado.codigo as \"estado.codigo\",  estado.sigla as \"estado.sigla\",  ");
		
		
		sql.append("arquivoPlanoEnsino.codigo as \"arquivoPlanoEnsino.codigo\" , arquivoPlanoEnsino.nome as \"arquivoPlanoEnsino.nome\", ");
		sql.append("arquivoPlanoEnsino.descricaoArquivo as \"arquivoPlanoEnsino.descricaoArquivo\", arquivoPlanoEnsino.pastabasearquivo as \"arquivoPlanoEnsino.pastabasearquivo\", ");
		
		sql.append(" rd.codigo as \"rd.codigo\", rd.nome as \"rd.nome\", ");
		sql.append(" ri.codigo as \"ri.codigo\", ri.nome as \"ri.nome\", req.matricula as \"req.matricula\" ");
				
		sql.append(" FROM requerimentodisciplinasaproveitadas as rda ");
		sql.append(" inner join  disciplina on disciplina.codigo = rda.disciplina ");		
		sql.append(" left join arquivo as arquivoPlanoEnsino on arquivoPlanoEnsino.codigo = rda.arquivoPlanoEnsino ");
		sql.append(" left join  cidade on cidade.codigo = rda.cidade ");		
		sql.append(" left join  estado on estado.codigo = cidade.estado ");		
		sql.append(" left join usuario as rd on rd.codigo = rda.responsaveldeferimento");
		sql.append(" left join usuario as ri on ri.codigo = rda.responsavelindeferimento");
		sql.append(" left join requerimento req on req.codigo = rda.requerimento ");
		return sql;
	}
	
	@Override
	public boolean consultarSeExisteBloqueioParaDisciplinaAproveitadaPorMatricula(Integer disciplina, String matricula) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(rda.codigo) QTDE FROM requerimentodisciplinasaproveitadas as rda ");
		sql.append(" inner join requerimento on requerimento.codigo = rda.requerimento ");
		sql.append(" where rda.disciplina = ? ");		
		sql.append(" and requerimento.matricula = ? ");
		sql.append(" and rda.bloquearNovaSolicitacoes ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), disciplina, matricula);
		return Uteis.isAtributoPreenchido(tabelaResultado, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	public Integer consultarQtdeRequerimentoDisciplinasAproveitadasPorDisciplinaPorMatriculaPorSituacaoIndeferida(Integer disciplina, String matricula) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(rda.codigo) QTDE FROM requerimentodisciplinasaproveitadas as rda ");
		sql.append(" inner join requerimento on requerimento.codigo = rda.requerimento ");
		sql.append(" where rda.disciplina = ? ");
		sql.append(" and rda.situacaoRequerimentoDisciplinasAproveitadasEnum = ? ");
		sql.append(" and requerimento.matricula = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), disciplina, SituacaoRequerimentoDisciplinasAproveitadasEnum.INDEFERIDO.name(), matricula);
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<RequerimentoDisciplinasAproveitadasVO> consultarPorRequerimentoVO(RequerimentoVO obj, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsulta();
			sqlStr.append(" where rda.requerimento = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
			List<RequerimentoDisciplinasAproveitadasVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				RequerimentoDisciplinasAproveitadasVO rda = new RequerimentoDisciplinasAproveitadasVO();
				montarDados(rda, tabelaResultado, nivelMontarDados, usuario);
				rda.setRequerimentoVO(obj);
				vetResultado.add(rda);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<RequerimentoDisciplinasAproveitadasVO> consultarRequerimentoDisciplinasAproveitadasIndeferidas(String matricula, Integer disciplina, UsuarioVO usuario) {
		try {
			StringBuilder sql = new StringBuilder("select ");
			sql.append(" rda.codigo as \"rda.codigo\", ");
			sql.append(" rda.motivoSituacao as \"rda.motivoSituacao\", ");		
			sql.append(" rda.dataIndeferimento as \"rda.dataIndeferimento\", ");
			sql.append(" ri.codigo as \"ri.codigo\", ri.nome as \"ri.nome\" ");
			sql.append(" FROM requerimentodisciplinasaproveitadas as rda ");
			sql.append(" inner join  requerimento on requerimento.codigo = rda.requerimento");
			sql.append(" left join usuario as ri on ri.codigo = rda.responsavelindeferimento");
			sql.append(" where rda.disciplina = ? ");
			sql.append(" and rda.situacaorequerimentodisciplinasaproveitadasenum = ? ");
			sql.append(" and requerimento.matricula = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), disciplina, SituacaoRequerimentoDisciplinasAproveitadasEnum.INDEFERIDO.name(), matricula);
			List<RequerimentoDisciplinasAproveitadasVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				RequerimentoDisciplinasAproveitadasVO rda = new RequerimentoDisciplinasAproveitadasVO();
				rda.setCodigo(tabelaResultado.getInt("rda.codigo"));
				rda.setMotivoSituacao(tabelaResultado.getString("rda.motivoSituacao"));
				rda.setDataIndeferimento(tabelaResultado.getDate("rda.dataindeferimento"));
				rda.getResponsavelIndeferimento().setCodigo(tabelaResultado.getInt("ri.codigo"));
				rda.getResponsavelIndeferimento().setNome(tabelaResultado.getString("ri.nome"));
				vetResultado.add(rda);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	private void montarDados(RequerimentoDisciplinasAproveitadasVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("rda.codigo"));		
		obj.setSituacaoRequerimentoDisciplinasAproveitadasEnum(SituacaoRequerimentoDisciplinasAproveitadasEnum.valueOf(dadosSQL.getString("rda.situacaoRequerimentoDisciplinasAproveitadasEnum")));
		obj.setNomeDisciplinaCursada(dadosSQL.getString("rda.nomeDisciplinaCursada"));
		obj.setNota((dadosSQL.getDouble("rda.nota")));
		obj.setFrequencia((dadosSQL.getDouble("rda.frequencia")));
		obj.getRequerimentoVO().setCodigo((dadosSQL.getInt("rda.requerimento")));		
		obj.setCargaHorariaCursada(dadosSQL.getInt("rda.cargaHorariaCursada"));
		obj.setCargaHoraria(dadosSQL.getInt("rda.cargaHoraria"));
		obj.setAno(dadosSQL.getString("rda.ano"));
		obj.setSemestre(dadosSQL.getString("rda.semestre"));
		obj.setInstituicao(dadosSQL.getString("rda.instituicao"));
		obj.setUtilizaNotaConceito(dadosSQL.getBoolean("rda.utilizaNotaConceito"));
		obj.setMediaFinalConceito(dadosSQL.getString("rda.mediaFinalConceito"));
		obj.setTipo(dadosSQL.getString("rda.tipo"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("rda.situacaoHistorico"))) {
			obj.setSituacaoHistorico(SituacaoHistorico.getEnum(dadosSQL.getString("rda.situacaoHistorico")));
		}
		obj.setNomeProfessor(dadosSQL.getString("rda.nomeprofessor"));
		obj.setTitulacaoProfessor(dadosSQL.getString("rda.titulacaoprofessor"));
		obj.setSexoProfessor(dadosSQL.getString("rda.sexoprofessor"));
		obj.setBloquearNovaSolicitacoes(dadosSQL.getBoolean("rda.bloquearnovasolicitacoes"));
		obj.setMotivoSituacao(dadosSQL.getString("rda.motivoSituacao"));
		obj.setMotivoRevisaoAnaliseAproveitamento(dadosSQL.getString("rda.motivoRevisaoAnaliseAproveitamento"));
		
		obj.getDisciplina().setCodigo((dadosSQL.getInt("disciplina.codigo")));
		obj.getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
		
		obj.getCidade().setCodigo((dadosSQL.getInt("cidade.codigo")));
		obj.getCidade().setNome(dadosSQL.getString("cidade.nome"));
		obj.getCidade().getEstado().setCodigo(dadosSQL.getInt("estado.codigo"));
		obj.getCidade().getEstado().setSigla(dadosSQL.getString("estado.sigla"));
		
		if(dadosSQL.getInt("arquivoPlanoEnsino.codigo") != 0) {
			obj.getArquivoPlanoEnsino().setCodigo((dadosSQL.getInt("arquivoPlanoEnsino.codigo")));
			obj.getArquivoPlanoEnsino().setNome(dadosSQL.getString("arquivoPlanoEnsino.nome"));			
			obj.getArquivoPlanoEnsino().setDescricaoArquivo(dadosSQL.getString("arquivoPlanoEnsino.descricaoArquivo"));
			obj.getArquivoPlanoEnsino().setPastaBaseArquivo(dadosSQL.getString("arquivoPlanoEnsino.pastaBaseArquivo"));	
		}
		
		
		if(dadosSQL.getInt("rd.codigo") != 0) {
			obj.getResponsavelDeferimento().setCodigo(dadosSQL.getInt("rd.codigo"));
			obj.getResponsavelDeferimento().setNome(dadosSQL.getString("rd.nome"));
			obj.setDataDeferimento(dadosSQL.getDate("rda.datadeferimento"));
		}
		if(dadosSQL.getInt("ri.codigo") != 0) {
			obj.getResponsavelIndeferimento().setCodigo(dadosSQL.getInt("ri.codigo"));
			obj.getResponsavelIndeferimento().setNome(dadosSQL.getString("ri.nome"));
			obj.setDataIndeferimento(dadosSQL.getDate("rda.dataindeferimento"));
		}
		obj.getRequerimentoVO().getMatricula().setMatricula(dadosSQL.getString("req.matricula"));
		obj.setQtdIndeferimentos(getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().consultarQtdeRequerimentoDisciplinasAproveitadasPorDisciplinaPorMatriculaPorSituacaoIndeferida(obj.getDisciplina().getCodigo(), obj.getRequerimentoVO().getMatricula().getMatricula()));
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarRequerimentoDisciplinasAproveitadas(RequerimentoVO requerimento, RequerimentoDisciplinasAproveitadasVO rda, UsuarioVO usuario) throws Exception {
		int index = 0;
		rda.setRequerimentoVO(requerimento);
		Uteis.checkState(!Uteis.isAtributoPreenchido(rda.getDisciplina().getCodigo()), "O campo Disciplina deve ser informado.");
		if(requerimento.getTipoRequerimento().getIsTipoAproveitamentoDisciplina()) {
		if(getFacadeFactory().getRequerimentoDisciplinasAproveitadasFacade().consultarSeExisteBloqueioParaDisciplinaAproveitadaPorMatricula(rda.getDisciplina().getCodigo(), requerimento.getMatricula().getMatricula())) {
			String msgBloqueioNovaSolicitacaoAproveitamento = getFacadeFactory().getTipoRequerimentoFacade().consultarMsgBloqueioNovaSolicitacaoAproveitamento(requerimento.getTipoRequerimento().getCodigo(), usuario);
			if(msgBloqueioNovaSolicitacaoAproveitamento.isEmpty()) {
				msgBloqueioNovaSolicitacaoAproveitamento = "Não é possível realizar o aproveitamento para essa disciplina, pois a mesma foi bloqueada pelo administrativo por situação de indeferimentos anterios.";
			}
			Uteis.checkState(true,msgBloqueioNovaSolicitacaoAproveitamento);	
		}
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(rda.getNomeDisciplinaCursada()), "O campo Disciplina Cursada deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(rda.getCargaHorariaCursada()), "O campo Carga Horária Disciplina Cursada deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(rda.getFrequencia()), "O campo Frequência deve ser informado.");
		Uteis.checkState(rda.getUtilizaNotaConceito() && !Uteis.isAtributoPreenchido(rda.getMediaFinalConceito()), "O campo Nota deve ser informado.");
		Uteis.checkState(!rda.getUtilizaNotaConceito() && !Uteis.isAtributoPreenchido(rda.getNota()), "O campo Nota deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(rda.getAno()), "O campo Ano deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(rda.getSemestre()), "O campo Semestre deve ser informado.");
		if(requerimento.getTipoRequerimento().getIsTipoAproveitamentoDisciplina()) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(rda.getInstituicao()), "O campo Instituição de Ensino deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(rda.getCidade()), "O campo Cidade deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(rda.getArquivoPlanoEnsino().getNome()), "O Arquivo do Documento Comprobatório - Plano de Ensino deve ser anexado.");
		Uteis.checkState(rda.isApresentarMotivoRevisaoAnaliseAproveitamento() && !Uteis.isAtributoPreenchido(rda.getMotivoRevisaoAnaliseAproveitamento()), "Você atingiu o limite de solicitação por disciplina para o aproveitamento por favor informe o campo Motivo de Revisão da Análise do Aproveitamento.");
		}
		if(!Uteis.isAtributoPreenchido(rda.getCargaHoraria())) {
			rda.setCargaHoraria(getFacadeFactory().getGradeDisciplinaFacade().consultarCargaHorariaDisciplinaPorDisciplinaETurma(rda.getDisciplina().getCodigo(), requerimento.getMatricula().getMatricula(), usuario));
		}
		if(requerimento.getTipoRequerimento().getIsTipoAproveitamentoDisciplina()) {
		Double percentualAlcancado = (double) ((rda.getCargaHorariaCursada() * 100) / rda.getCargaHoraria());
		
		Double percentualMinimoCargaHorariaAproveitamento = getFacadeFactory().getDisciplinaFacade().consultarPercentualMinimoCargaHorariaAproveitamento(rda.getDisciplina().getCodigo());		
		if(!Uteis.isAtributoPreenchido(percentualMinimoCargaHorariaAproveitamento)) {
			percentualMinimoCargaHorariaAproveitamento = requerimento.getTipoRequerimento().getPercentualMinimoCargaHorariaAproveitamento();	
		}
		Uteis.checkState(percentualAlcancado < percentualMinimoCargaHorariaAproveitamento, "A Disciplina "+rda.getNomeDisciplinaCursada() + " alcançou somente "+percentualAlcancado+"% da carga horária da disciplina Aproveitada, o minimo é de "+percentualMinimoCargaHorariaAproveitamento+"%");
		
		Integer qtdeMinimaDeAnosAproveitamento = getFacadeFactory().getDisciplinaFacade().consultarQtdeMinimaDeAnosAproveitamento(rda.getDisciplina().getCodigo());
		if(!Uteis.isAtributoPreenchido(qtdeMinimaDeAnosAproveitamento)) {
			qtdeMinimaDeAnosAproveitamento = requerimento.getTipoRequerimento().getQtdeMinimaDeAnosAproveitamento();	
		}
		Integer anoMininoAproveitamento = Uteis.getAnoData(new Date()) - qtdeMinimaDeAnosAproveitamento;
		Integer anoDisciplinaCursada = Integer.parseInt(rda.getAno());
		Uteis.checkState(anoDisciplinaCursada < anoMininoAproveitamento, "O ano limite para realizar o aproveitamento da disciplina "+ rda.getNomeDisciplinaCursada()  +" é "+anoMininoAproveitamento);	
		if(anoDisciplinaCursada == anoMininoAproveitamento) {
			Integer semestreMininoAproveitamento = Integer.parseInt(Uteis.getSemestreAtual());
			Integer semestreDisciplinaCursada = Integer.parseInt(rda.getSemestre());
			Uteis.checkState(semestreDisciplinaCursada < semestreMininoAproveitamento, "O semestre limite para realizar o aproveitamento da disciplina "+ rda.getNomeDisciplinaCursada()  +" é "+anoMininoAproveitamento+"/"+semestreMininoAproveitamento);	
		}
		}
		for (RequerimentoDisciplinasAproveitadasVO objExistente : requerimento.getListaRequerimentoDisciplinasAproveitadasVOs()) {
			if(objExistente.getDisciplina().getCodigo().equals(rda.getDisciplina().getCodigo())) {
				requerimento.getListaRequerimentoDisciplinasAproveitadasVOs().set(index, rda);
				return;
			}
			index++;
		}
		requerimento.getListaRequerimentoDisciplinasAproveitadasVOs().add(rda);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removerRequerimentoDisciplinasAproveitadas(RequerimentoVO requerimento, RequerimentoDisciplinasAproveitadasVO rda, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(rda.getDisciplina().getCodigo()), "Não foi possível realizar essa operação, pois o campo Disciplina não esta informado.");
		requerimento.getListaRequerimentoDisciplinasAproveitadasVOs().removeIf(p-> p.getDisciplina().getCodigo().equals(rda.getDisciplina().getCodigo()));
	}

}
