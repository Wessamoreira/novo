package negocio.facade.jdbc.blackboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.LogOperacaoEnsalamentoBlackboardVO;
import negocio.comuns.blackboard.enumeradores.OperacaoEnsalacaoBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.blackboard.LogOperacaoEnsalamentoBlackboardInterfaceFacade;

@Lazy
@Scope("singleton")
@Repository
public class LogOperacaoEnsalamentoBlackboard extends ControleAcesso
		implements LogOperacaoEnsalamentoBlackboardInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7078746928012835515L;

//	@Override	
//	public void incluir(LogOperacaoEnsalamentoBlackboardVO logOperacaoEnsalamentoBlackboardVO, UsuarioVO usuarioVO)
//			throws Exception {
//		Thread incluir =  new Thread(new LogOperacaoEnsalamentoBlackboardIncluir(logOperacaoEnsalamentoBlackboardVO, usuarioVO));
//		incluir.start();
//		
//	}
	
//	class LogOperacaoEnsalamentoBlackboardIncluir extends ControleAcesso implements Runnable{
//		
//		LogOperacaoEnsalamentoBlackboardVO logOperacaoEnsalamentoBlackboardVO;
//		UsuarioVO usuarioVO;
//		
//		
//
//		public LogOperacaoEnsalamentoBlackboardIncluir(
//				LogOperacaoEnsalamentoBlackboardVO logOperacaoEnsalamentoBlackboardVO, UsuarioVO usuarioVO) {
//			super();
//			this.logOperacaoEnsalamentoBlackboardVO = logOperacaoEnsalamentoBlackboardVO;
//			this.usuarioVO = usuarioVO;
//		}
//
//
//
//		@Override
//		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//		public void run() {
//			try {
//			incluir(logOperacaoEnsalamentoBlackboardVO, "logOperacaoEnsalamentoBlackboard", new AtributoPersistencia()
//					.add("abreviaturaDisciplina", logOperacaoEnsalamentoBlackboardVO.getAbreviaturaDisciplina())
//					.add("codigoDisciplina", logOperacaoEnsalamentoBlackboardVO.getCodigoDisciplina())
//					.add("nomeDisciplina", logOperacaoEnsalamentoBlackboardVO.getNomeDisciplina())
//					.add("ano", logOperacaoEnsalamentoBlackboardVO.getAno())
//					.add("semestre", logOperacaoEnsalamentoBlackboardVO.getSemestre())
//					.add("tipoSalaAulaBlackboardPessoa", logOperacaoEnsalamentoBlackboardVO.getTipoSalaAulaBlackboardPessoa())
//					.add("matricula", logOperacaoEnsalamentoBlackboardVO.getMatricula())
//					.add("pessoa", logOperacaoEnsalamentoBlackboardVO.getPessoa())
//					.add("EmailInstitucional", logOperacaoEnsalamentoBlackboardVO.getEmailInstitucional())
//					.add("CodigoSalaAulaBlackBoard", logOperacaoEnsalamentoBlackboardVO.getCodigoSalaAulaBlackBoard())
//					.add("TipoSalaAulaBlackboard", logOperacaoEnsalamentoBlackboardVO.getTipoSalaAulaBlackboard())
//					.add("SalaAulaBlackBoard", logOperacaoEnsalamentoBlackboardVO.getSalaAulaBlackBoard())
//					.add("IdSalaAulaBlackBoard", logOperacaoEnsalamentoBlackboardVO.getIdSalaAulaBlackBoard())
//					.add("GrupoBlackBoard", logOperacaoEnsalamentoBlackboardVO.getGrupoBlackBoard())
//					.add("IdGrupoBlackBoard", logOperacaoEnsalamentoBlackboardVO.getIdGrupoBlackBoard())
//					.add("OperacaoEnsalacaoBlackboard", logOperacaoEnsalamentoBlackboardVO.getOperacaoEnsalacaoBlackboard())
//					.add("Observacao", logOperacaoEnsalamentoBlackboardVO.getObservacao())				
//					, usuarioVO);	
//			}catch (Exception e) {
//				AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.LOG_ENSALAMENTO_BLACKBOARD, e);
//			}
//		}
//		
//		
//		
//	}

	@Override
	public void consultar(DataModelo dataModelo, LogOperacaoEnsalamentoBlackboardVO filtros,  UsuarioVO usuarioVO)
			throws Exception {
		StringBuilder sql = new StringBuilder();
	    sql.append("select count(*) over() as totalRegistros, codigo, abreviaturaDisciplina, codigoDisciplina, nomeDisciplina, ano, semestre, tipoSalaAulaBlackboardPessoa, matricula, pessoa, ");
	    sql.append("EmailInstitucional, CodigoSalaAulaBlackBoard, TipoSalaAulaBlackboard, SalaAulaBlackBoard, IdSalaAulaBlackBoard, GrupoBlackBoard, idGrupoBlackBoard,  ");
	    sql.append("OperacaoEnsalacaoBlackboard, Observacao, created, codigoCreated, nomeCreated ");
	    sql.append(" from logOperacaoEnsalamentoBlackboard where ");
	    sql.append(realizarGeracaoWhereDataInicioDataTermino(dataModelo.getDataIni(), dataModelo.getDataFim(), "created", "created", false));
	    List<Object> filtro = new ArrayList<Object>();
	    if(Uteis.isAtributoPreenchido(filtros.getPessoa())) {
	    	sql.append(" and sem_acentos(pessoa) ilike sem_acentos(?) ");
	    	filtro.add(filtros.getPessoa()+PERCENT);
	    }
	    if(Uteis.isAtributoPreenchido(filtros.getMatricula())) {
	    	sql.append(" and sem_acentos(matricula) ilike sem_acentos(?) ");
	    	filtro.add(filtros.getMatricula()+PERCENT);
	    }
	    if(Uteis.isAtributoPreenchido(filtros.getEmailInstitucional())) {
	    	sql.append(" and sem_acentos(emailInstitucional) ilike sem_acentos(?) ");
	    	filtro.add(filtros.getEmailInstitucional()+PERCENT);
	    }
	    if(Uteis.isAtributoPreenchido(filtros.getCodigoDisciplina())) {
	    	sql.append(" and codigoDisciplina = ? ");
	    	filtro.add(filtros.getCodigoDisciplina()+PERCENT);
	    }
	    if(Uteis.isAtributoPreenchido(filtros.getNomeDisciplina())) {
	    	sql.append(" and sem_acentos(nomedisciplina) ilike sem_acentos(?) ");
	    	filtro.add(filtros.getNomeDisciplina()+PERCENT);
	    	
	    }
	    if(Uteis.isAtributoPreenchido(filtros.getAbreviaturaDisciplina())) {
	    	
	    	sql.append(" and sem_acentos(abreviaturaDisciplina) ilike sem_acentos(?) ");
	    	filtro.add(filtros.getNomeDisciplina()+PERCENT);
	    }
	    if(Uteis.isAtributoPreenchido(filtros.getCodigoSalaAulaBlackBoard())) {
	    	sql.append(" and codigoSalaAulaBlackBoard = ? ");
	    	filtro.add(filtros.getCodigoSalaAulaBlackBoard());
	    }
	    if(Uteis.isAtributoPreenchido(filtros.getSalaAulaBlackBoard())) {
	    	sql.append(" and sem_acentos(SalaAulaBlackBoard) ilike sem_acentos(?) ");
	    	filtro.add(filtros.getSalaAulaBlackBoard()+PERCENT);
	    }
	    if(Uteis.isAtributoPreenchido(filtros.getGrupoBlackBoard())) {
	    	sql.append(" and sem_acentos(grupoBlackBoard) ilike sem_acentos(?) ");
	    	filtro.add(filtros.getGrupoBlackBoard()+PERCENT);
	    }
	    if(Uteis.isAtributoPreenchido(filtros.getTipoSalaAulaBlackboard()) && !filtros.getTipoSalaAulaBlackboard().equals(TipoSalaAulaBlackboardEnum.NENHUM)) {
	    	sql.append(" and tipoSalaAulaBlackboard = ? ");
	    	filtro.add(filtros.getTipoSalaAulaBlackboard().name());
	    }
	   
	    if(Uteis.isAtributoPreenchido(filtros.getAno())) {
	    	sql.append(" and ano =  ? ");
	    	filtro.add(filtros.getAno());
	    }
	    if(Uteis.isAtributoPreenchido(filtros.getSemestre())) {
	    	sql.append(" and semestre =  ? ");
	    	filtro.add(filtros.getSemestre());
	    }
	    sql.append(" order by created desc limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
	    
	    SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtro.toArray());
	    dataModelo.setTotalRegistrosEncontrados(0);
	    if(rs.next()) {
	    	dataModelo.setTotalRegistrosEncontrados(rs.getInt("totalRegistros"));
	    }
	    rs.previous();
	    List<LogOperacaoEnsalamentoBlackboardVO> logs =  new ArrayList<LogOperacaoEnsalamentoBlackboardVO>(0);
	    while(rs.next()) {
	    	LogOperacaoEnsalamentoBlackboardVO log =  new LogOperacaoEnsalamentoBlackboardVO();
	    	log.setAbreviaturaDisciplina(rs.getString("abreviaturaDisciplina"));
	    	log.setNomeDisciplina(rs.getString("nomeDisciplina"));
	    	log.setCodigoDisciplina(rs.getInt("codigoDisciplina"));
	    	log.setAno(rs.getString("ano"));
	    	log.setSemestre(rs.getString("semestre"));
	    	log.setPessoa(rs.getString("pessoa"));
	    	log.setMatricula(rs.getString("matricula"));
	    	log.setEmailInstitucional(rs.getString("emailInstitucional"));
	    	log.setCodigoSalaAulaBlackBoard(rs.getInt("codigoSalaAulaBlackBoard"));
	    	log.setSalaAulaBlackBoard(rs.getString("salaAulaBlackBoard"));
	    	log.setIdSalaAulaBlackBoard(rs.getString("idSalaAulaBlackBoard"));
	    	log.setIdGrupoBlackBoard(rs.getString("idGrupoBlackBoard"));
	    	log.setGrupoBlackBoard(rs.getString("grupoBlackBoard"));
	    	log.setOperacaoEnsalacaoBlackboard(OperacaoEnsalacaoBlackboardEnum.valueOf(rs.getString("operacaoEnsalacaoBlackboard")));
	    	log.setTipoSalaAulaBlackboardPessoa(TipoSalaAulaBlackboardPessoaEnum.valueOf(rs.getString("tipoSalaAulaBlackboardPessoa")));
	    	log.setTipoSalaAulaBlackboard(TipoSalaAulaBlackboardEnum.valueOf(rs.getString("tipoSalaAulaBlackboard")));
	    	log.setCreated(rs.getDate("created"));
	    	log.setCodigoCreated(rs.getInt("codigoCreated"));
	    	log.setNomeCreated(rs.getString("nomeCreated"));
	    	log.setCodigo(rs.getInt("codigo"));
	    	log.setNovoObj(false);
	    	logs.add(log);
	    }
	    dataModelo.setListaConsulta(logs);
	    
	}

//	@Override
//	public LogOperacaoEnsalamentoBlackboardVO novo(SalaAulaBlackboardVO salaAulaBlackboardVO,
//			SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO, OperacaoEnsalacaoBlackboardEnum operacaoEnsalacaoBlackboardEnum, String observacao, UsuarioVO usuarioVO) throws Exception {
//		LogOperacaoEnsalamentoBlackboardVO log =  new LogOperacaoEnsalamentoBlackboardVO();
//		log.setAbreviaturaDisciplina(salaAulaBlackboardVO.getDisciplinaVO().getAbreviatura());
//		log.setCodigoDisciplina(salaAulaBlackboardVO.getDisciplinaVO().getCodigo());
//		log.setNomeDisciplina(salaAulaBlackboardVO.getDisciplinaVO().getNome());
//		log.setAno(salaAulaBlackboardVO.getAno());
//		log.setSemestre(salaAulaBlackboardVO.getSemestre());
//		log.setSalaAulaBlackBoard(salaAulaBlackboardVO.getIdSalaAulaBlackboard());
//		log.setIdSalaAulaBlackBoard(salaAulaBlackboardVO.getId());
//		log.setCodigoSalaAulaBlackBoard(salaAulaBlackboardVO.getCodigo());
//		log.setEmailInstitucional(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getEmail());
//		log.setPessoa(salaAulaBlackboardPessoaVO.getPessoaEmailInstitucionalVO().getPessoaVO().getNome());
//		log.setMatricula(salaAulaBlackboardPessoaVO.getMatricula());
//		log.setGrupoBlackBoard(salaAulaBlackboardVO.getNomeGrupo());
//		log.setIdGrupoBlackBoard(salaAulaBlackboardVO.getIdGrupo());
//		log.setTipoSalaAulaBlackboard(salaAulaBlackboardVO.getTipoSalaAulaBlackboardEnum());
//		log.setTipoSalaAulaBlackboardPessoa(salaAulaBlackboardPessoaVO.getTipoSalaAulaBlackboardPessoaEnum());
//		log.setOperacaoEnsalacaoBlackboard(operacaoEnsalacaoBlackboardEnum);
//		log.setObservacao(observacao);
//		if(usuarioVO != null) {
//			log.setCodigoCreated(usuarioVO.getCodigo());
//			log.setNomeCreated(usuarioVO.getNome());
//		}
//		
//		return log;
//	}
	
	

}
