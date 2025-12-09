package negocio.facade.jdbc.blackboard;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMethod;

import controle.arquitetura.DataModelo;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.SalaAulaBlackboardGrupoVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.ead.SalaAulaBlackboardOperacaoVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.blackboard.SalaAulaBlackboardOperacaoInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.boletoonline.itau.comuns.TokenVO;


@Repository
@Scope("singleton")
@Lazy
public class SalaAulaBlackboardOperacao extends ControleAcesso implements SalaAulaBlackboardOperacaoInterfaceFacade {
		
	public static final String ORIGEM_SALA_AULA_BLACKBOARD_PESSOA =  "SALA_AULA_BLACKBOARD_PESSOA";
	public static final String ORIGEM_SALA_AULA_BLACKBOARD =  "SALA_AULA_BLACKBOARD";
	public static final String ORIGEM_SALA_AULA_BLACKBOARD_FECHAMENTO =  "SALA_AULA_BLACKBOARD_FECHAMENTO";
	public static final String GRUPO_SALA_AULA_BLACKBOARD_FECHAMENTO =  "GRUPO_SALA_AULA_BLACKBOARD_FECHAMENTO";
	public static final String ORIGEM_SALA_AULA_BLACKBOARD_COPIA_CONTEUDO =  "ORIGEM_SALA_AULA_BLACKBOARD_COPIA_CONTEUDO";
	public static final String ORIGEM_SALA_AULA_BLACKBOARD_DELETAR_CONTEUDO =  "ORIGEM_SALA_AULA_BLACKBOARD_DELETAR_CONTEUDO";
	public static final String GRUPO_SALA_AULA_BLACKBOARD_EXISTENTE =  "GRUPO_SALA_AULA_BLACKBOARD_EXISTENTE";
	public static final String GRUPO_SALA_AULA_BLACKBOARD_OPERACAO =  "GRUPO_SALA_AULA_BLACKBOARD_OPERACAO";
	public static final String APURAR_NOTA_SALA_AULA_BLACBOARD =  "APURAR_NOTA_SALA_AULA_BLACBOARD";
	public static final String CONSOLIDAR_NOTA_SOMENTE_NO_SEI =  "CONSOLIDAR_NOTA_SOMENTE_NO_SEI";
	public static final String CONSOLIDAR_NOTA_SALA_AULA_BLACBOARD =  "CONSOLIDAR_NOTA_SALA_AULA_BLACBOARD";
	public static final String INATIVAR_USUARIO =  "INATIVAR_USUARIO";
	public static final String ATIVAR_USUARIO =  "ATIVAR_USUARIO";
	public static final String ATUALIZAR_USUARIO =  "ATUALIZAR_USUARIO";
	
	
	public static final String OPERACAO_INCLUIR =  "INSERT";
	public static final String OPERACAO_UPDATE =  "UPDATE";
	public static final String OPERACAO_DELETAR =  "DELETE";	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2307566686423930798L;
	
	public void realizarExecucaoSalaAulaOperacao(SalaAulaBlackboardOperacaoVO obj, UsuarioVO usuarioVO) throws Exception {
		realizarExecucaoSalaAulaOperacao(obj.getCodigo(), usuarioVO);
	}
	
	public void realizarExecucaoSalaAulaOperacao(Integer  codigo, UsuarioVO usuarioVO) throws Exception {
		if(Uteis.isAtributoPreenchido(codigo)) {		
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getAplicacaoControle().getConfiguracaoSeiBlackboardVO();
			if(Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
				Thread t =  new Thread(new RealizarExecucaoSalaAulaOperacao(codigo, usuarioVO));
				t.start();
			}
		}
	}
	
	private class RealizarExecucaoSalaAulaOperacao implements Runnable {
		private Integer  codigo;
		private UsuarioVO usuarioVO;
		
		public RealizarExecucaoSalaAulaOperacao(Integer codigo, UsuarioVO usuarioVO) {
			super();
			this.codigo = codigo;
			this.usuarioVO = usuarioVO;
		}



		public void run() {
		if(Uteis.isAtributoPreenchido(codigo)) {
			ConfiguracaoSeiBlackboardVO configSeiBlackboardVO = getAplicacaoControle().getConfiguracaoSeiBlackboardVO();
			if(Uteis.isAtributoPreenchido(configSeiBlackboardVO)) {
				TokenVO token = getFacadeFactory().getConfiguracaoSeiBlackboardFacade().consultarTokenVO(configSeiBlackboardVO);
				Map<String, String> headers = new HashMap<String, String>();
				headers.put(UteisWebServiceUrl.codigo, codigo.toString());
				try {
					unirestHeaders(token, configSeiBlackboardVO.getUrlExternaSeiBlackboard() +UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA + UteisWebServiceUrl.URL_BLACKBOARD_SALAAULA_OPERACAO_CODIGO, RequestMethod.GET, headers, usuarioVO);
					getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboard(configSeiBlackboardVO, usuarioVO);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCampoMsgNotificacaoExecutadaSalaAulaBlackboardOperacao(Integer codigo, String erro) {
		StringBuilder sqlStr = new StringBuilder(" UPDATE  salaaulablackboardoperacao set msgnotificacaoexecutada = true, ");
		List<Object> lista = new ArrayList<>();
		sqlStr.append("erromsgnotificacao = ?, ");
		if(Uteis.isAtributoPreenchido(erro)) {
			lista.add(erro);
		}else {
			lista.add(null);
		}
		sqlStr.append(" updated = now() where codigo = ? ");
		lista.add(codigo);
		getConexao().getJdbcTemplate().update(sqlStr.toString(), lista.toArray());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSalaAulaBlackboardOperacaoExecutada(Integer codigo) {
		StringBuilder sqlStr = new StringBuilder(" UPDATE  salaaulablackboardoperacao set executada = true, updated = now() where codigo = ?");
		getConexao().getJdbcTemplate().update(sqlStr.toString(), codigo);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSalaAulaBlackboardOperacao(Integer codigo) {
		StringBuilder sqlStr = new StringBuilder(" UPDATE  salaaulablackboardoperacao set executada = true, erro = null, updated = now() where codigo = ?");
		getConexao().getJdbcTemplate().update(sqlStr.toString(), codigo);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCampoErroSalaAulaBlackboardOperacao(Integer codigo, String erro) {
		StringBuilder sqlStr = new StringBuilder(" UPDATE  salaaulablackboardoperacao set erro = ?, executada = true, updated = now() where codigo = ?");
		getConexao().getJdbcTemplate().update(sqlStr.toString(), erro, codigo);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPessoaSalaBlack(SalaAulaBlackboardVO salaAulaBlackboardVO, Integer pessoa, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String matricula, Integer matriculaPeriodoTurmaDisciplina, String email, UsuarioVO usuarioVO) throws Exception{
		incluirPessoaSalaBlack(salaAulaBlackboardVO, pessoa, tipoSalaAulaBlackboardPessoaEnum, matricula, matriculaPeriodoTurmaDisciplina, null, email, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPessoaSalaBlack(SalaAulaBlackboardVO salaAulaBlackboardVO, Integer pessoa, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String matricula, Integer matriculaPeriodoTurmaDisciplina, String idSalaAulaBlackboard, String email, UsuarioVO usuarioVO) throws Exception{
		incluirPessoaSalaBlack(salaAulaBlackboardVO, pessoa, tipoSalaAulaBlackboardPessoaEnum, matricula, matriculaPeriodoTurmaDisciplina, idSalaAulaBlackboard, null, null, email, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPessoaSalaBlack(SalaAulaBlackboardVO salaAulaBlackboardVO, Integer pessoa, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String matricula, Integer matriculaPeriodoTurmaDisciplina, String idSalaAulaBlackboard, String msgNotificacao, TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, String email, UsuarioVO usuarioVO) throws Exception{
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("");			
				sql.append("INSERT INTO public.salaaulablackboardoperacao ");
				sql.append(" (operacao, executada, codigoorigem, tipoorigem,  created, codigocreated, nomecreated, tipoSalaAulaBlackboardPessoaEnum, pessoa, matricula, matriculaPeriodoTurmaDisciplina, idsalaaulablackboard, msgNotificacao, templateMensagemAutomaticaEnum, email, erro) ");
				sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
				int x = 1;
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				Uteis.setValuePreparedStatement(OPERACAO_INCLUIR, x++, sqlInserir);
				if(!Uteis.isAtributoPreenchido(pessoa)) {
					Uteis.setValuePreparedStatement(true, x++, sqlInserir);
				}else {
					Uteis.setValuePreparedStatement(false, x++, sqlInserir);
				}
				Uteis.setValuePreparedStatement(Uteis.isAtributoPreenchido(salaAulaBlackboardVO) ? salaAulaBlackboardVO.getCodigo() : null, x++, sqlInserir);
				Uteis.setValuePreparedStatement(ORIGEM_SALA_AULA_BLACKBOARD_PESSOA, x++, sqlInserir);
				Uteis.setValuePreparedStatement(new Date(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getCodigo(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getNome(), x++, sqlInserir);				
				Uteis.setValuePreparedStatement(tipoSalaAulaBlackboardPessoaEnum, x++, sqlInserir);	
				Uteis.setValuePreparedStatement(pessoa, x++, sqlInserir);
				Uteis.setValuePreparedStatement(matricula, x++, sqlInserir);
				Uteis.setValuePreparedStatement(matriculaPeriodoTurmaDisciplina, x++, sqlInserir);
				Uteis.setValuePreparedStatement(idSalaAulaBlackboard, x++, sqlInserir);
				Uteis.setValuePreparedStatement(msgNotificacao, x++, sqlInserir);
				Uteis.setValuePreparedStatement(templateMensagemAutomaticaEnum, x++, sqlInserir);
				Uteis.setValuePreparedStatement(email, x++, sqlInserir);
				if(!Uteis.isAtributoPreenchido(pessoa)) {
					Uteis.setValuePreparedStatement("Não foi localizada a pessoa do e-mail "+email+".", x++, sqlInserir);
				}else {
					Uteis.setValuePreparedStatement(null, x++, sqlInserir);
				}
				return sqlInserir;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updatePessoaSalaBlack(Integer codigoOrigem, Integer codigoDestino, Integer pessoa, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String matricula, Integer matriculaPeriodoTurmaDisciplina, String idSalaAulaBlackboard, String msgNotificacao, TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum, String email, UsuarioVO usuarioVO) throws Exception{
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("");			
				sql.append("INSERT INTO public.salaaulablackboardoperacao ");
				sql.append(" (operacao, executada, codigoorigem, codigodestino, tipoorigem,  created, codigocreated, nomecreated, tipoSalaAulaBlackboardPessoaEnum, pessoa, matricula, matriculaPeriodoTurmaDisciplina, idsalaaulablackboard, msgNotificacao, templateMensagemAutomaticaEnum, email, erro) ");
				sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
				int x = 1;
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				Uteis.setValuePreparedStatement(OPERACAO_UPDATE, x++, sqlInserir);
				if(!Uteis.isAtributoPreenchido(pessoa)) {
					Uteis.setValuePreparedStatement(true, x++, sqlInserir);
				}else {
					Uteis.setValuePreparedStatement(false, x++, sqlInserir);
				}
				Uteis.setValuePreparedStatement(Uteis.isAtributoPreenchido(codigoOrigem) ? codigoOrigem : null, x++, sqlInserir);
				Uteis.setValuePreparedStatement(Uteis.isAtributoPreenchido(codigoDestino) ? codigoDestino : null, x++, sqlInserir);
				Uteis.setValuePreparedStatement(ORIGEM_SALA_AULA_BLACKBOARD_PESSOA, x++, sqlInserir);
				Uteis.setValuePreparedStatement(new Date(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getCodigo(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getNome(), x++, sqlInserir);				
				Uteis.setValuePreparedStatement(tipoSalaAulaBlackboardPessoaEnum, x++, sqlInserir);	
				Uteis.setValuePreparedStatement(pessoa, x++, sqlInserir);
				Uteis.setValuePreparedStatement(matricula, x++, sqlInserir);
				Uteis.setValuePreparedStatement(matriculaPeriodoTurmaDisciplina, x++, sqlInserir);
				Uteis.setValuePreparedStatement(idSalaAulaBlackboard, x++, sqlInserir);
				Uteis.setValuePreparedStatement(msgNotificacao, x++, sqlInserir);
				Uteis.setValuePreparedStatement(templateMensagemAutomaticaEnum, x++, sqlInserir);
				Uteis.setValuePreparedStatement(email, x++, sqlInserir);
				if(!Uteis.isAtributoPreenchido(pessoa)) {
					Uteis.setValuePreparedStatement("Não foi localizada a pessoa do e-mail "+email+".", x++, sqlInserir);
				}else {
					Uteis.setValuePreparedStatement(null, x++, sqlInserir);
				}
				return sqlInserir;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPessoaSalaBlack(Integer salaAulaBlack, Integer pessoa, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String email, UsuarioVO usuarioVO) throws Exception{
		excluirPessoaSalaBlack(salaAulaBlack, pessoa, tipoSalaAulaBlackboardPessoaEnum, null, email, usuarioVO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPessoaSalaBlack(Integer salaAulaBlack, Integer pessoa, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, String idSalaAulaBlackboard, String email, UsuarioVO usuarioVO) throws Exception{
		Uteis.checkState(!Uteis.isAtributoPreenchido(email), "O campo E-mail Institucional é obrigado para realizar essa operação.");
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("");			
				sql.append("INSERT INTO public.salaaulablackboardoperacao ");
				sql.append(" (operacao, executada, codigoorigem, tipoorigem,  created, codigocreated, nomecreated, tipoSalaAulaBlackboardPessoaEnum, pessoa, idsalaaulablackboard, email) ");
				sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
				int x = 1;
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				Uteis.setValuePreparedStatement(OPERACAO_DELETAR, x++, sqlInserir);
				Uteis.setValuePreparedStatement(false, x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlack, x++, sqlInserir);
				Uteis.setValuePreparedStatement(ORIGEM_SALA_AULA_BLACKBOARD_PESSOA, x++, sqlInserir);
				Uteis.setValuePreparedStatement(new Date(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getCodigo(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getNome(), x++, sqlInserir);				
				Uteis.setValuePreparedStatement(tipoSalaAulaBlackboardPessoaEnum, x++, sqlInserir);	
				Uteis.setValuePreparedStatement(pessoa, x++, sqlInserir);
				Uteis.setValuePreparedStatement(idSalaAulaBlackboard, x++, sqlInserir);
				Uteis.setValuePreparedStatement(email, x++, sqlInserir);
				return sqlInserir;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public boolean incluirSalaBlack(List<SalaAulaBlackboardVO> salaAulaBlackboardVOs, UsuarioVO usuarioVO) throws Exception{
		consultarSeExisteProcessamentoPendente();
		boolean possuiSalaParaGerar = false;
		for(SalaAulaBlackboardVO salaAulaBlackboardVO: salaAulaBlackboardVOs) {			
			if(salaAulaBlackboardVO.getQtdAlunosNaoEnsalados() > 0) {
				int nrSala = 1;
				possuiSalaParaGerar = true;
				TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum = salaAulaBlackboardVO.getDisciplinaVO().getClassificacaoDisciplina().isProjetoIntegrador() ? TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR_AMBIENTACAO : TipoSalaAulaBlackboardEnum.DISCIPLINA;
				List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs = consultarMatriculaPeriodoTurmaDisciplinaParaEnsalamento(salaAulaBlackboardVO.getAlunosNaoEnsalados());
				if(salaAulaBlackboardVO.getQtdeSalasExistentes() > 0) {
					List<SalaAulaBlackboardVO> salasExistenteVOs = getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardEad(tipoSalaAulaBlackboardEnum, null, salaAulaBlackboardVO.getCursoVO().getCodigo(), null, salaAulaBlackboardVO.getTurmaVO().getCodigo(), salaAulaBlackboardVO.getDisciplinaVO().getCodigo(), salaAulaBlackboardVO.getAno(), salaAulaBlackboardVO.getSemestre(), salaAulaBlackboardVO.getBimestre(), null, salaAulaBlackboardVO.getProgramacaoTutoriaOnlineVO().getCodigo(), usuarioVO);
					nrSala = realizarEnsalamentoAlunoPorSalaExistentes(salasExistenteVOs, salaAulaBlackboardVO, tipoSalaAulaBlackboardEnum, matriculaPeriodoTurmaDisciplinaVOs, nrSala, usuarioVO);
				}
				if(salaAulaBlackboardVO.getQtdeSalasNecessarias() > 0 && !matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
					realizarEnsalamentoAlunoPorSalaNecessarias(salaAulaBlackboardVO, tipoSalaAulaBlackboardEnum, matriculaPeriodoTurmaDisciplinaVOs, nrSala, usuarioVO);
				}
			}
		}
		return possuiSalaParaGerar;
		
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int realizarEnsalamentoAlunoPorSalaExistentes(List<SalaAulaBlackboardVO> salasExistenteVOs, SalaAulaBlackboardVO salaAulaBlackboardVO, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, int nrSala, UsuarioVO usuarioVO) throws Exception {		
		for (SalaAulaBlackboardVO salaExistente : salasExistenteVOs) {
			if (salaExistente.getNrSala() > nrSala) {
				nrSala = salaExistente.getNrSala();
			}
			for (Iterator<MatriculaPeriodoTurmaDisciplinaVO> iterator = matriculaPeriodoTurmaDisciplinaVOs.iterator(); iterator.hasNext();) {				
				if (salaExistente.getQtdeAlunos() <= salaAulaBlackboardVO.getAlunosPorSala()) {
					MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = iterator.next();
					incluirPessoaSalaBlack(salaExistente, matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getAluno().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getMatricula(), matriculaPeriodoTurmaDisciplinaVO.getCodigo(), null, usuarioVO);
					salaExistente.setQtdeAlunos(salaExistente.getQtdeAlunos() + 1);
					iterator.remove();					
				}else {
					break;
				}
			}
			if(matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
				nrSala++;
				return nrSala;
			}
		}
		nrSala++;
		return nrSala;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarEnsalamentoAlunoPorSalaNecessarias(SalaAulaBlackboardVO salaAulaBlackboardVO, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, int nrSala, UsuarioVO usuarioVO) throws CloneNotSupportedException, Exception {
		int salasNecessarias = salaAulaBlackboardVO.getQtdeSalasNecessarias();				
		while(salasNecessarias > 0 && !matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			SalaAulaBlackboardVO salaAulaBlackboardCriarVO = (SalaAulaBlackboardVO) salaAulaBlackboardVO.clone();
			salaAulaBlackboardCriarVO.setTipoSalaAulaBlackboardEnum(tipoSalaAulaBlackboardEnum);
			salaAulaBlackboardCriarVO.setNrSala(nrSala);
			salaAulaBlackboardCriarVO.setQtdeSalas(0);
			salaAulaBlackboardCriarVO.setQtdeAlunos(0);
			salaAulaBlackboardCriarVO.setAlunosNaoEnsalados("");
			StringBuilder alunosNaoEnsalados =  new StringBuilder();
			for (Iterator<MatriculaPeriodoTurmaDisciplinaVO> iterator = matriculaPeriodoTurmaDisciplinaVOs.iterator(); iterator.hasNext();) {
				if(salaAulaBlackboardCriarVO.getQtdeAlunos() <= salaAulaBlackboardVO.getAlunosPorSala()) {
					MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO =  iterator.next();
					if(alunosNaoEnsalados.length() > 0) {
						alunosNaoEnsalados.append(",");
					}
					alunosNaoEnsalados.append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
					salaAulaBlackboardCriarVO.setQtdeAlunos(salaAulaBlackboardCriarVO.getQtdeAlunos()+1);
					iterator.remove();
				}else {
					break;
				}
			}					
			salaAulaBlackboardCriarVO.setAlunosNaoEnsalados(alunosNaoEnsalados.toString());					
			incluirSalaBlack(salaAulaBlackboardCriarVO, ORIGEM_SALA_AULA_BLACKBOARD, null, null, usuarioVO);
			salasNecessarias--;
			nrSala++;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer incluirSalaBlack(SalaAulaBlackboardVO salaAulaBlackboardVO,String tipoOrigem, Integer codigoOrigem, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception{
		Integer codigo = (Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("");			
				sql.append("INSERT INTO public.salaaulablackboardoperacao ");
				sql.append(" (curso, agrupamentounidadeensino, turma, disciplina, gradeCurricularEstagio, programacaoTutoriaOnline, ano, semestre, ");
				sql.append(" tipoSalaAulaBlackboardEnum, nrSala, nrGrupo, datasourceid, operacao, executada, tipoorigem, ");
				sql.append(" codigoorigem, created, codigocreated, nomecreated, bimestre, listaAlunosEnsalar, idSalaAulaBlackboardConteudoMaster, ");
				sql.append(" situacaoReprovado, situacaoAprovado, situacaoCursando) ");
				sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ");
				int x = 1;
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getCursoVO(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getAgrupamentoUnidadeEnsinoVO(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getTurmaVO(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getDisciplinaVO(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getGradeCurricularEstagioVO(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getProgramacaoTutoriaOnlineVO(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getAno(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getSemestre(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getTipoSalaAulaBlackboardEnum(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getNrSala(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getNrGrupo(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getDataSourceId(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(OPERACAO_INCLUIR, x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(false, x++, sqlInserir, arg0);				
				Uteis.setValuePreparedStatement(tipoOrigem, x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(codigoOrigem, x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(new Date(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(usuarioVO.getCodigo(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(usuarioVO.getNome(), x++, sqlInserir, arg0);				
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getBimestre(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getAlunosNaoEnsalados(), x++, sqlInserir, arg0);
				if(salaAulaBlackboardVO.isConteudoMasterSelecionado()&& Uteis.isAtributoPreenchido(salaAulaBlackboardVO.getDisciplinaVO().getIdConteudoMasterBlackboard())) {
					Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getDisciplinaVO().getIdConteudoMasterBlackboard(), x++, sqlInserir, arg0);	
				}else {
					Uteis.setValuePreparedStatement(null, x++, sqlInserir, arg0);
				}
				if(filtroRelatorioAcademicoVO != null) {
					Uteis.setValuePreparedStatement(filtroRelatorioAcademicoVO.getReprovado(), x++, sqlInserir, arg0);	
					Uteis.setValuePreparedStatement(filtroRelatorioAcademicoVO.getAprovado(), x++, sqlInserir, arg0);	
					Uteis.setValuePreparedStatement(filtroRelatorioAcademicoVO.getCursando(), x++, sqlInserir, arg0);	
				}else {
					Uteis.setValuePreparedStatement(null, x++, sqlInserir, arg0);	
					Uteis.setValuePreparedStatement(null, x++, sqlInserir, arg0);	
					Uteis.setValuePreparedStatement(null, x++, sqlInserir, arg0);
				}
				return sqlInserir;
			}
		}, new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}
		});
		return codigo;
	}

public Integer incluirCopiaConteudoSalaBlack(SalaAulaBlackboardVO salaAulaBlackboardVO, String tipoOrigem, UsuarioVO usuarioVO) throws Exception{
		Integer codigo = (Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("");			
				sql.append("INSERT INTO public.salaaulablackboardoperacao ");
				sql.append(" (curso, agrupamentounidadeensino, disciplina, ano, semestre, tipoSalaAulaBlackboardEnum, nrSala, nrGrupo, operacao, executada, tipoorigem, codigoorigem, created, codigocreated, nomecreated, idSalaAulaBlackboard, idSalaAulaBlackboardConteudoMaster ) ");
				sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ");
				int x = 1;
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getCursoVO(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getAgrupamentoUnidadeEnsinoVO(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getDisciplinaVO(), x++, sqlInserir,arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getAno(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getSemestre(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getTipoSalaAulaBlackboardEnum(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getNrSala(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getNrGrupo(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(OPERACAO_INCLUIR, x++,sqlInserir, arg0);
				Uteis.setValuePreparedStatement(false, x++, sqlInserir, arg0);			
				Uteis.setValuePreparedStatement(tipoOrigem, x++,sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getCodigo(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(new Date(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(usuarioVO.getCodigo(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(usuarioVO.getNome(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getIdSalaAulaBlackboard(), x++, sqlInserir, arg0);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getIdSalaAulaBlackboardConteudoMaster(), x++, sqlInserir, arg0);
				return sqlInserir;
			}
		}, new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					return arg0.getInt("codigo");
				}
				return null;
			}
		});
		return codigo;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirSalaBlackPorFechamento(SalaAulaBlackboardVO salaAulaBlackboardVO, UsuarioVO usuarioVO) throws Exception{
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("");			
				sql.append("INSERT INTO public.salaaulablackboardoperacao ");
				sql.append(" (curso, turma, disciplina, gradeCurricularEstagio, programacaoTutoriaOnline, ano, semestre, tipoSalaAulaBlackboardEnum, nrSala, operacao, executada, codigoorigem, tipoorigem,  created, codigocreated, nomecreated, bimestre) ");
				sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
				int x = 1;
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getCursoVO(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getTurmaVO(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getDisciplinaVO(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getGradeCurricularEstagioVO(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getProgramacaoTutoriaOnlineVO(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getAno(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getSemestre(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getTipoSalaAulaBlackboardEnum(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getNrSala(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(OPERACAO_DELETAR, x++, sqlInserir);
				Uteis.setValuePreparedStatement(false, x++, sqlInserir);				
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getCodigo(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(ORIGEM_SALA_AULA_BLACKBOARD_FECHAMENTO, x++, sqlInserir);
				Uteis.setValuePreparedStatement(new Date(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getCodigo(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getNome(), x++, sqlInserir);		
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getBimestre(), x++, sqlInserir);
				return sqlInserir;
			}
		});
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGrupoSalaBlackPorFechamento(SalaAulaBlackboardVO salaAulaBlackboardVO, UsuarioVO usuarioVO) throws Exception{
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("");			
				sql.append("INSERT INTO public.salaaulablackboardoperacao ");
				sql.append(" (operacao, executada, codigoorigem, tipoorigem,  curso, turma, disciplina, ano, semestre, tipoSalaAulaBlackboardEnum, nrSala, nrgrupo, created, codigocreated, nomecreated) ");
				sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?) ");
				int x = 1;
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				Uteis.setValuePreparedStatement(OPERACAO_DELETAR, x++, sqlInserir);
				Uteis.setValuePreparedStatement(false, x++, sqlInserir);				
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getCodigo(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(GRUPO_SALA_AULA_BLACKBOARD_FECHAMENTO, x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getCursoVO(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getTurmaVO(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getDisciplinaVO(), x++, sqlInserir);				
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getAno(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getSemestre(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getTipoSalaAulaBlackboardEnum(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getNrSala(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO.getNrGrupo(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(new Date(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getCodigo(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getNome(), x++, sqlInserir);
				return sqlInserir;
			}
		});
	}
		
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultarSeExisteProcessamentoPendente() {
		StringBuilder sql  = new StringBuilder("select count(salaaulablackboardoperacao.codigo) as QTDE from salaaulablackboardoperacao where executada = false ");
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		Integer qtde = (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
		Uteis.checkState(Uteis.isAtributoPreenchido(qtde), "Não foi possível realizar essa operação, pois ainda existe "+qtde+" processamentos pendentes para Sala Aula Blackboard Operações.");
	}
	
	@Override
	public void consultarEnsalamentoPendenteProcessamento(ProgressBarVO progressBarVO) {
		StringBuilder sql  = new StringBuilder("select count(salaaulablackboardoperacao.codigo) as qtde, sum(case when salaaulablackboardoperacao.erro != '' then 1 else 0 end) as qtdeErro from salaaulablackboardoperacao where executada = false ");
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		Integer qtdeErro = 0;
		if(rs.next()) {
			if(progressBarVO.getMaxValue().equals(0)) {
				progressBarVO.setMaxValue(rs.getInt("qtde"));
			}
			progressBarVO.setProgresso(Long.valueOf(progressBarVO.getMaxValue() - rs.getInt("qtde")));
			qtdeErro = rs.getInt("qtdeErro");
		}else {
			progressBarVO.setMaxValue(0);
			progressBarVO.setProgresso(0l);
		}
		if(progressBarVO.getMaxValue().equals(0) || progressBarVO.getMaxValue().equals(qtdeErro) || (progressBarVO.getMaxValue() -  progressBarVO.getProgresso() - qtdeErro) == 0) {
			if(qtdeErro > 1) {
				progressBarVO.setStatus("Processamento Concluído, porém ocorreram "+qtdeErro+" erros. Contate o administrador do sistema para mais detalhes.");
			}else if(qtdeErro.equals(1)) {
				progressBarVO.setStatus("Processamento Concluído, porém ocorreu "+qtdeErro+" erro. Contate o administrador do sistema para mais detalhes.");
			}else {
				progressBarVO.setStatus("Processamento Concluído...");
			}
			progressBarVO.setForcarEncerramento(true);		
			progressBarVO.setAtivado(false);
			progressBarVO.setPollAtivado(false);
		}else {
			if((progressBarVO.getMaxValue() -  progressBarVO.getProgresso()) == 1) {
				progressBarVO.setStatus("Falta "+(progressBarVO.getMaxValue() -  progressBarVO.getProgresso())+" operação a ser realizada.");
			}else {
				progressBarVO.setStatus("Faltam "+(progressBarVO.getMaxValue() -  progressBarVO.getProgresso())+" operações a serem realizadas.");
			}
			progressBarVO.setAtivado(true);
			progressBarVO.setPollAtivado(true);
		}
	}
	
	@Override
	public List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplinaParaEnsalamento(String listaAlunos) throws Exception{
		String[] arrayMPTDVOs = listaAlunos.split(",");
		StringBuilder sql =  new StringBuilder("select distinct matriculaperiodoturmadisciplina.matricula, matricula.aluno, matriculaperiodoturmadisciplina.codigo, pessoa.nome as nome_aluno from matriculaperiodoturmadisciplina inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula inner join pessoa on matricula.aluno = pessoa.codigo where matriculaperiodoturmadisciplina.codigo in (?)  order by pessoa.nome ");
		StringBuilder in = new StringBuilder("");
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs =  new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		int x = 0;
		for(String mptd : arrayMPTDVOs ) {
			x++;
			if(in.length() > 0) {
				in.append(",");
			}
			in.append(mptd);
			if(x > 100) {	
				SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString().replace("?", in.toString()));
				matriculaPeriodoTurmaDisciplinaVOs.addAll(montarDadosMPTD(rs));
				x = 0;
				in = new StringBuilder("");
				sql =  new StringBuilder("select distinct matriculaperiodoturmadisciplina.matricula, matricula.aluno, matriculaperiodoturmadisciplina.codigo, pessoa.nome as nome_aluno from matriculaperiodoturmadisciplina inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula inner join pessoa on matricula.aluno = pessoa.codigo where matriculaperiodoturmadisciplina.codigo in (?)  order by pessoa.nome  ");
			}
		}
		if(x > 0) {
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString().replace("?", in.toString()));
			matriculaPeriodoTurmaDisciplinaVOs.addAll(montarDadosMPTD(rs));
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}
	
	public String getSqlcCnsultarAlunosNaoEnsaladosProjetoIntegrador(SalaAulaBlackboardGrupoVO sabGrupo) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select matriculaperiodoturmadisciplina.codigo, ");
		sql.append(" matriculaperiodoturmadisciplina.matricula, ");
		sql.append(" pessoa.codigo as aluno, ");
		sql.append(" pessoa.nome as nome_aluno ");
		sql.append(" from matriculaperiodoturmadisciplina ");
		sql.append(" inner join historico  on  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" inner join disciplina  on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" inner join matricula   on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
		sql.append(" inner join matriculaperiodo   on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" where matriculaperiodo.situacaomatriculaperiodo in ('AT', 'PR', 'FI') ");
		sql.append(" and matriculaperiodoturmadisciplina.ano = '").append(sabGrupo.getAno()).append("' ");
		sql.append(" and matriculaperiodoturmadisciplina.semestre = '").append(sabGrupo.getSemestre()).append("' ");
		sql.append(" and historico.situacao not in ('AA', 'CC', 'CH', 'AB', 'IS') ");	
		sql.append(" and disciplina.codigo =  ").append(sabGrupo.getDisciplinaVO().getCodigo());
		sql.append(" and exists  (");
		sql.append(" 	select agrupamentounidadeensinoitem.codigo from agrupamentounidadeensinoitem ");
		sql.append(" 	where agrupamentounidadeensinoitem.unidadeensino = matricula.unidadeensino and agrupamentounidadeensinoitem.agrupamentounidadeensino  = ").append(sabGrupo.getAgrupamentoUnidadeEnsinoVO().getCodigo());
		sql.append(" )");
		sql.append(" and not exists  (");
		sql.append("	select sap.codigo from salaaulablackboardpessoa sap ");
		sql.append("	inner join salaaulablackboard on salaaulablackboard.codigo  = sap.salaaulablackboard ");
		sql.append("	where sap.matricula = matriculaperiodoturmadisciplina.matricula ");
		sql.append("	and salaaulablackboard.disciplina = matriculaperiodoturmadisciplina.disciplina	");
		sql.append("	and salaaulablackboard.agrupamentounidadeensino = ").append(sabGrupo.getAgrupamentoUnidadeEnsinoVO().getCodigo());
		sql.append("	and salaaulablackboard.tiposalaaulablackboardenum = 'PROJETO_INTEGRADOR_GRUPO' ");
		sql.append("	and salaaulablackboard.ano = matriculaperiodoturmadisciplina.ano ");
		sql.append("	and salaaulablackboard.semestre = matriculaperiodoturmadisciplina.semestre ");
		sql.append(") ");		
		return sql.toString();
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public  List<MatriculaPeriodoTurmaDisciplinaVO> consultarAlunosNaoEnsaladosProjetoIntegrador(SalaAulaBlackboardGrupoVO sabGrupo) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(getSqlcCnsultarAlunosNaoEnsaladosProjetoIntegrador(sabGrupo));
		sql.append(" order by pessoa.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosMPTD(rs);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public  Integer consultarQtdAlunosNaoEnsaladosProjetoIntegrador(SalaAulaBlackboardGrupoVO sabGrupo) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select count(t.codigo) as QTDE from ( ");
		sql.append(getSqlcCnsultarAlunosNaoEnsaladosProjetoIntegrador(sabGrupo));
		sql.append(" ) as t ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	
	private List<MatriculaPeriodoTurmaDisciplinaVO> montarDadosMPTD(SqlRowSet rs){
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs =  new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		while(rs.next()) {
			MatriculaPeriodoTurmaDisciplinaVO mptd =  new MatriculaPeriodoTurmaDisciplinaVO();
			mptd.getMatriculaObjetoVO().setMatricula(rs.getString("matricula"));
			mptd.getMatriculaObjetoVO().getAluno().setCodigo(rs.getInt("aluno"));
			mptd.getMatriculaObjetoVO().getAluno().setNome(rs.getString("nome_aluno"));
			mptd.setCodigo(rs.getInt("codigo"));
			matriculaPeriodoTurmaDisciplinaVOs.add(mptd);
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	} 
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarOperacaoDeApuracaoNotasAva(String idSalaAulaBlackboard, List<UnidadeEnsinoVO> unidadeEnsinoVOs, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, Integer bimestre,
			Integer curso, Integer supervisor, Integer disciplina, String matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean realizarCalculoMediaApuracaoNotas, boolean realizarBuscarNotaBlackboard, UsuarioVO usuarioLogado, String nivelEducacionalApresentar) throws Exception {		
		consultarSeExisteProcessamentoPendente();
		List<SalaAulaBlackboardVO> listaSala= new ArrayList<>();
		String tipoOrigem = null;
		listaSala = getFacadeFactory().getSalaAulaBlackboardFacade().consultaPadraoSalaAulaBlackBoardApurarNota(idSalaAulaBlackboard, unidadeEnsinoVOs, tipoSalaAulaBlackboardEnum, ano, semestre, bimestre, curso, supervisor, disciplina, matricula, filtroRelatorioAcademicoVO, usuarioLogado, nivelEducacionalApresentar);
		if (realizarBuscarNotaBlackboard && !realizarCalculoMediaApuracaoNotas) {
			tipoOrigem = APURAR_NOTA_SALA_AULA_BLACBOARD;
		}else if(realizarBuscarNotaBlackboard && realizarCalculoMediaApuracaoNotas) {
			tipoOrigem = CONSOLIDAR_NOTA_SALA_AULA_BLACBOARD ;
		}else {
			tipoOrigem = CONSOLIDAR_NOTA_SOMENTE_NO_SEI;
		}
		Uteis.checkState(!realizarBuscarNotaBlackboard && !realizarCalculoMediaApuracaoNotas, "Uma das opções deve ser escolhida para realizar a operação de notas da Blackboard");
		Uteis.checkState(listaSala.isEmpty(), "Não foi encontrado nenhuma sala de aula para realizar a operação.");
		for (SalaAulaBlackboardVO salaAulaBlackboardVO : listaSala) {
			try {
				incluirSalaBlack(salaAulaBlackboardVO, tipoOrigem, salaAulaBlackboardVO.getCodigo(), filtroRelatorioAcademicoVO, usuarioLogado);				
			} catch (Exception e) {
				try {
					String log = "apuracao de notas sala :" + salaAulaBlackboardVO.getIdSalaAulaBlackboard() +" erro" +e.getMessage() ;
					RegistroExecucaoJobVO registroExecucaoJob = new RegistroExecucaoJobVO(JobsEnum.JOB_BLACKBOARD_APURAR_NOTAS.getName(), log, salaAulaBlackboardVO.getCodigo());
					getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJob,usuarioLogado);	
				} catch (Exception e2) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoListaGrupoSalaAulaBlackboardVO(List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVO, ClassificacaoDisciplinaEnum classificacaoDisciplina, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		consultarSeExisteProcessamentoPendente();
		for (SalaAulaBlackboardGrupoVO obj : listaSalaAulaBlackboardGrupoVO) {
			realizarGeracaoGrupoSalaAulaBlackboardVO(obj, classificacaoDisciplina, ano, semestre, usuarioVO);
		}		
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoGrupoSalaAulaBlackboardVO(SalaAulaBlackboardGrupoVO obj, ClassificacaoDisciplinaEnum classificacaoDisciplina, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(ano), "O campo Ano deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(semestre), "O campo Semestre deve ser informado.");
		Integer cursoFiltro = classificacaoDisciplina.isTcc() ? obj.getCursoVO().getCodigo() : 0;
		Integer agrupamentoUnidadeEnsinoFiltro = classificacaoDisciplina.isProjetoIntegrador() ? obj.getAgrupamentoUnidadeEnsinoVO().getCodigo() : 0;
		TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum = classificacaoDisciplina.isTcc() ? TipoSalaAulaBlackboardEnum.TCC : TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR;
		obj.getListaGrupoSalaAulaBlackboardVO().clear();
		if(obj.getVagasSalaAulaBlackboardNovo() > 0) {
			realizarProcessamentoSalaAulaBlackboardPorGrupo(obj, cursoFiltro, agrupamentoUnidadeEnsinoFiltro, tipoSalaAulaBlackboardEnum, usuarioVO);
		}
		if(obj.getVagasSalaAulaBlackboardGrupoNovo() > 0) {
			realizarProcessamentoSalaAulaBlackboardGrupo(obj, cursoFiltro, agrupamentoUnidadeEnsinoFiltro, tipoSalaAulaBlackboardEnum, usuarioVO);
		}	
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarProcessamentoSalaAulaBlackboardPorGrupo(SalaAulaBlackboardGrupoVO obj, Integer cursoFiltro, Integer agrupamentoUnidadeEnsinoFiltro, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO) throws Exception {		
		obj.setNrSala(getFacadeFactory().getSalaAulaBlackboardFacade().consultarProximoNrSalaAulaBlackboard(cursoFiltro, agrupamentoUnidadeEnsinoFiltro, obj.getDisciplinaVO().getCodigo(), obj.getAno(), obj.getSemestre(), null, tipoSalaAulaBlackboardEnum));
		for (int i = 0; i < obj.getVagasSalaAulaBlackboardNovo(); i++) {
			SalaAulaBlackboardVO sab = new SalaAulaBlackboardVO();
			sab.setTipoSalaAulaBlackboardEnum(tipoSalaAulaBlackboardEnum);
			sab.setCursoVO(obj.getCursoVO());
			sab.setAgrupamentoUnidadeEnsinoVO(obj.getAgrupamentoUnidadeEnsinoVO());
			sab.setDisciplinaVO(obj.getDisciplinaVO());
			sab.setDataSourceId(obj.getDisciplinaVO().getFonteDeDadosBlackboard());
			sab.setAno(obj.getAno());
			sab.setSemestre(obj.getSemestre());
			sab.setNrSala(obj.getNrSala());
			sab.setCodigo(incluirSalaBlack(sab, ORIGEM_SALA_AULA_BLACKBOARD, null, null, usuarioVO));
			sab.setQtdeSalasExistentes(0);
			obj.setNrSala(obj.getNrSala() + 1);
			obj.getListaGrupoSalaAulaBlackboardVO().add(sab);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarProcessamentoSalaAulaBlackboardGrupo(SalaAulaBlackboardGrupoVO obj, Integer cursoFiltro, Integer agrupamentoUnidadeEnsinoFiltro, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO) throws Exception {
		TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardGrupoEnum = tipoSalaAulaBlackboardEnum.isTcc() ? TipoSalaAulaBlackboardEnum.TCC_GRUPO : TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR_GRUPO;
		BigDecimal bigCalculo = BigDecimal.ZERO;
		bigCalculo = bigCalculo.add(new BigDecimal(obj.getDisciplinaVO().getNrMaximoAulosPorSala().toString()));
		int totalRegistroPermitidoGrupoPorSala = bigCalculo.divide(new BigDecimal(obj.getDisciplinaVO().getNrMaximoAulosPorGrupo().toString()), BigDecimal.ROUND_UP).intValue();
		List<SalaAulaBlackboardVO> sabAulas = getFacadeFactory().getSalaAulaBlackboardFacade().consultarSeExisteSalaAulaBlackboardComVagaGrupo(cursoFiltro, agrupamentoUnidadeEnsinoFiltro, obj.getDisciplinaVO().getCodigo(), obj.getAno(), obj.getSemestre(), null, totalRegistroPermitidoGrupoPorSala, tipoSalaAulaBlackboardEnum, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		for (SalaAulaBlackboardVO sab : sabAulas) {
			sab.setQtdeSalasExistentes(1);//Campo Preenchido com zero para ser utilizado na ordenacao logo abaixo
			sab.setQtdeSalas(getFacadeFactory().getSalaAulaBlackboardFacade().consultarQtdeGrupoExistenteSalaAulaBlackboard(sab.getId(), tipoSalaAulaBlackboardGrupoEnum));
			sab.setNrGrupo(getFacadeFactory().getSalaAulaBlackboardFacade().consultarProximoNrGrupoSalaAulaBlackboard(sab.getId(), tipoSalaAulaBlackboardGrupoEnum));
			obj.getListaGrupoSalaAulaBlackboardVO().add(sab);
		}
		for (int i = 0; i < obj.getVagasSalaAulaBlackboardGrupoNovo(); i++) {
			Optional<SalaAulaBlackboardVO> sabAula = obj.getListaGrupoSalaAulaBlackboardVO().stream()
					.filter(p -> p.getQtdeSalas() < totalRegistroPermitidoGrupoPorSala)
					.sorted(Comparator.comparing(SalaAulaBlackboardVO::getQtdeSalas).thenComparing(SalaAulaBlackboardVO::getQtdeSalasExistentes))
					.findFirst();
			if(sabAula.isPresent()) {
				SalaAulaBlackboardVO sabGrupo = new SalaAulaBlackboardVO();
				sabGrupo = (SalaAulaBlackboardVO) Uteis.clonar(sabAula.get());
				sabGrupo.setCodigo(0);
				sabGrupo.setTipoSalaAulaBlackboardEnum(tipoSalaAulaBlackboardGrupoEnum);
				if (Uteis.isAtributoPreenchido(sabAula.get().getQtdeSalasExistentes())) {
					incluirSalaBlack(sabGrupo, GRUPO_SALA_AULA_BLACKBOARD_EXISTENTE, sabAula.get().getCodigo(), null, usuarioVO);
					sabAula.get().setNrGrupo(sabAula.get().getNrGrupo() + 1);
				} else if (!Uteis.isAtributoPreenchido(sabAula.get().getQtdeSalasExistentes())) {
					sabGrupo.setNrGrupo(sabAula.get().getQtdeSalas() + 1);
					incluirSalaBlack(sabGrupo, GRUPO_SALA_AULA_BLACKBOARD_OPERACAO, sabAula.get().getCodigo(), null, usuarioVO);
				}
				sabAula.get().setQtdeSalas(sabAula.get().getQtdeSalas() + 1);
			}
		}
	}
		
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarFechamentoGrupoSalaAulaBlackboardPorTCC(List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVOFechamento, UsuarioVO usuarioVO) throws Exception {
		consultarSeExisteProcessamentoPendente();
		for (SalaAulaBlackboardGrupoVO sala : listaSalaAulaBlackboardGrupoVOFechamento) {
			realizarVerificacaoTrocaAlunoSalaAulaGrupoBlackboardFechamento(sala, usuarioVO);	
		}
		realizarExclusaoSalaAulaGrupoBlackboardFechamento(listaSalaAulaBlackboardGrupoVOFechamento, usuarioVO);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarExclusaoSalaAulaGrupoBlackboardFechamento(List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVOFechamento, UsuarioVO usuarioVO) throws Exception {
		for (SalaAulaBlackboardGrupoVO sala : listaSalaAulaBlackboardGrupoVOFechamento) {
			for (SalaAulaBlackboardVO grupo : sala.getListaGrupoSalaAulaBlackboardVO()) {
				if (grupo.getQtdeAlunos() <= 0) {
					excluirGrupoSalaBlackPorFechamento(grupo, usuarioVO);
				}
			}
			if (sala.getListaGrupoSalaAulaBlackboardVO().stream().allMatch(p -> p.getQtdeAlunos() <= 0)) {
				SalaAulaBlackboardVO obj = getFacadeFactory().getSalaAulaBlackboardFacade().consultarPorIdSalaAulaBlackboardPorTipoSalaAulaBlackboardEnum(sala.getIdSalaAulaBlackboard(), sala.getDisciplinaVO().getClassificacaoDisciplina().isTcc() ? TipoSalaAulaBlackboardEnum.TCC : TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR);
				excluirSalaBlackPorFechamento(obj, usuarioVO);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarFechamentoGrupoSalaAulaBlackboardPorProjetoIntegrador(List<SalaAulaBlackboardGrupoVO> listaSalaAulaBlackboardGrupoVOFechamento, UsuarioVO usuarioVO) throws Exception {
		consultarSeExisteProcessamentoPendente();
		Map<String, List<MatriculaPeriodoTurmaDisciplinaVO>> mapMptd = new HashMap<>();
		for (SalaAulaBlackboardGrupoVO sala : listaSalaAulaBlackboardGrupoVOFechamento) {
			realizarVerificacaoTrocaAlunoSalaAulaGrupoBlackboardFechamento(sala, usuarioVO);
			if(sala.getDisciplinaVO().getClassificacaoDisciplina().isProjetoIntegrador() && sala.getQtdAlunosEnsalados() < sala.getDisciplinaVO().getNrMaximoAulosPorSala() 
					&& sala.getQtdAlunosNaoEnsalados() > 0 && sala.getVagasSalaAulaBlackboard() > 0 ) {
				if(!mapMptd.containsKey(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino())) {
					mapMptd.put(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino(), consultarAlunosNaoEnsaladosProjetoIntegrador(sala));
				}
				realizarDistribuicaoSalaAulaGrupoBlackboardPorNrMininoAlunoGrupo(sala, mapMptd, usuarioVO);
			}	
		}
		for (SalaAulaBlackboardGrupoVO sala : listaSalaAulaBlackboardGrupoVOFechamento) {
			if(sala.getDisciplinaVO().getClassificacaoDisciplina().isProjetoIntegrador() && sala.getQtdAlunosEnsalados() < sala.getDisciplinaVO().getNrMaximoAulosPorSala() 
					&& sala.getQtdAlunosNaoEnsalados() > 0 && sala.getVagasSalaAulaBlackboard() > 0 
					&& mapMptd.containsKey(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()) && !mapMptd.get(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()).isEmpty()) {
				realizarDistribuicaoSalaAulaGrupoBlackboardPorNrMaximoAlunoGrupo(sala, mapMptd, usuarioVO);	
			}	
		}
	}	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarFechamentoGrupoSalaAulaBlackboardIndividual(SalaAulaBlackboardGrupoVO sala, Map<String, List<MatriculaPeriodoTurmaDisciplinaVO>> mapMptd, UsuarioVO usuarioVO) throws Exception {
		consultarSeExisteProcessamentoPendente();
		realizarVerificacaoTrocaAlunoSalaAulaGrupoBlackboardFechamento(sala, usuarioVO);
		if(sala.getDisciplinaVO().getClassificacaoDisciplina().isTcc()) {
			List<SalaAulaBlackboardGrupoVO> listaSalaTemp = new ArrayList<>();
			listaSalaTemp.add(sala);
			realizarExclusaoSalaAulaGrupoBlackboardFechamento(listaSalaTemp, usuarioVO);
		}else if(sala.getDisciplinaVO().getClassificacaoDisciplina().isProjetoIntegrador() 
				&& sala.getQtdAlunosEnsalados() < sala.getDisciplinaVO().getNrMaximoAulosPorSala()
				&& sala.getQtdAlunosNaoEnsalados() > 0	&& sala.getVagasSalaAulaBlackboard() > 0) {
			if(!mapMptd.containsKey(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino())) {
				mapMptd.put(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino(), consultarAlunosNaoEnsaladosProjetoIntegrador(sala));
			}
			realizarDistribuicaoSalaAulaGrupoBlackboardPorNrMininoAlunoGrupo(sala, mapMptd, usuarioVO);
			if(sala.getQtdAlunosEnsalados() < sala.getDisciplinaVO().getNrMaximoAulosPorSala()  
					&& sala.getQtdAlunosNaoEnsalados() > 0 && sala.getVagasSalaAulaBlackboard() > 0 
					&& mapMptd.containsKey(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()) 
					&& !mapMptd.get(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()).isEmpty()) {
				realizarDistribuicaoSalaAulaGrupoBlackboardPorNrMaximoAlunoGrupo(sala, mapMptd, usuarioVO);	
			} 
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarVerificacaoTrocaAlunoSalaAulaGrupoBlackboardFechamento(SalaAulaBlackboardGrupoVO sala, UsuarioVO usuarioVO) throws Exception {
		if(sala.getListaGrupoSalaAulaBlackboardVO().stream().flatMap(p-> p.getListaAlunos().stream()).anyMatch(sabAluno-> Uteis.isAtributoPreenchido(sabAluno.getCodigoGrupoOrigem()))) {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(sala.getDisciplinaVO().getClassificacaoDisciplina().isProjetoIntegrador() ? TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROJETO_INTEGRADOR_ALTERACAO_SALA_GRUPO : TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_TCC_ALTERACAO_SALA_GRUPO, false, null);
			for (SalaAulaBlackboardVO grupo : sala.getListaGrupoSalaAulaBlackboardVO()) {
				realizarVerificacaoOperacaoTrocaGrupoBlackboard(sala, grupo, mensagemTemplate, usuarioVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarVerificacaoOperacaoTrocaGrupoBlackboard(SalaAulaBlackboardGrupoVO sabGrupoVO, SalaAulaBlackboardVO grupo, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, UsuarioVO usuarioVO) throws Exception {
		for (SalaAulaBlackboardPessoaVO sabAluno : grupo.getListaAlunos()) {
			if(Uteis.isAtributoPreenchido(sabAluno.getCodigoGrupoOrigem())) {						
				String msgNotificacao = null;
				TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum = null;
				if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
					msgNotificacao = obterMensagemFormatadaMensagemNotificacaoAlteracaoSalaAulaBlackboardGrupo(grupo, sabAluno, mensagemTemplate.getMensagem());
					templateMensagemAutomaticaEnum = mensagemTemplate.getTemplateMensagemAutomaticaEnum();
				}
//				excluirPessoaSalaBlack(sabAluno.getCodigoGrupoOrigem(), sabAluno.getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(), sabAluno.getTipoSalaAulaBlackboardPessoaEnum(), grupo.getIdSalaAulaBlackboard(), sabAluno.getPessoaEmailInstitucionalVO().getEmail(), usuarioVO);						
//				incluirPessoaSalaBlack(grupo, sabAluno.getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(), sabAluno.getTipoSalaAulaBlackboardPessoaEnum(), sabAluno.getMatricula(), sabAluno.getMatriculaPeriodoTurmaDisciplina(), null, msgNotificacao, templateMensagemAutomaticaEnum, sabAluno.getPessoaEmailInstitucionalVO().getEmail(), usuarioVO);
				updatePessoaSalaBlack(sabAluno.getCodigoGrupoOrigem(), grupo.getCodigo(), sabAluno.getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(), sabAluno.getTipoSalaAulaBlackboardPessoaEnum(), sabAluno.getMatricula(), sabAluno.getMatriculaPeriodoTurmaDisciplina(), null, msgNotificacao, templateMensagemAutomaticaEnum, sabAluno.getPessoaEmailInstitucionalVO().getEmail(), usuarioVO);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarDistribuicaoSalaAulaGrupoBlackboardPorNrMininoAlunoGrupo(SalaAulaBlackboardGrupoVO sala, Map<String, List<MatriculaPeriodoTurmaDisciplinaVO>> mapMptd, UsuarioVO usuarioVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROJETO_INTEGRADOR_INCLUSAO_SALA_GRUPO, false, null);		
		boolean continuarProcessamento = false;
		do {
			Optional<SalaAulaBlackboardVO> findFirst = sala.getListaGrupoSalaAulaBlackboardVO().stream()
					.filter(p -> p.getQtdeAlunos() < sala.getDisciplinaVO().getNrMinimoAlunosPorGrupo())
					.sorted(Comparator.comparing(SalaAulaBlackboardVO::getQtdeAlunos))
					.findFirst();
			if(findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get())) {
				incluirPessoaSalaBlackPorMatriculaPeriodoTurmaDisciplina(sala, findFirst, mapMptd, sala.getDisciplinaVO().getNrMinimoAlunosPorGrupo(), true, mensagemTemplate, usuarioVO);
			}
			continuarProcessamento =  sala.getListaGrupoSalaAulaBlackboardVO().stream().anyMatch(p -> p.getQtdeAlunos() < sala.getDisciplinaVO().getNrMinimoAlunosPorGrupo());			
		}while(sala.getQtdAlunosEnsalados() < sala.getDisciplinaVO().getNrMaximoAulosPorSala() && continuarProcessamento &&  mapMptd.containsKey(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()) && !mapMptd.get(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()).isEmpty());
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarDistribuicaoSalaAulaGrupoBlackboardPorNrMaximoAlunoGrupo(SalaAulaBlackboardGrupoVO sala, Map<String, List<MatriculaPeriodoTurmaDisciplinaVO>> mapMptd, UsuarioVO usuarioVO) throws Exception {
		PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_PROJETO_INTEGRADOR_INCLUSAO_SALA_GRUPO, false, null);		
		boolean continuarProcessamento = false;
		do {
			Optional<SalaAulaBlackboardVO> findFirst = sala.getListaGrupoSalaAulaBlackboardVO().stream()
					.filter(p -> p.getQtdeAlunos() < sala.getDisciplinaVO().getNrMaximoAulosPorGrupo())
					.sorted(Comparator.comparing(SalaAulaBlackboardVO::getQtdeAlunos))
					.findFirst();
			if(findFirst.isPresent() && Uteis.isAtributoPreenchido(findFirst.get())) {
				incluirPessoaSalaBlackPorMatriculaPeriodoTurmaDisciplina(sala, findFirst, mapMptd, sala.getDisciplinaVO().getNrMaximoAulosPorGrupo(), false, mensagemTemplate, usuarioVO);
			}
			continuarProcessamento =  sala.getListaGrupoSalaAulaBlackboardVO().stream().anyMatch(p -> p.getQtdeAlunos() < sala.getDisciplinaVO().getNrMaximoAulosPorGrupo());			
		}while(sala.getQtdAlunosEnsalados() < sala.getDisciplinaVO().getNrMaximoAulosPorSala() && continuarProcessamento &&  mapMptd.containsKey(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()) && !mapMptd.get(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()).isEmpty());
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirPessoaSalaBlackPorMatriculaPeriodoTurmaDisciplina(SalaAulaBlackboardGrupoVO sala,  Optional<SalaAulaBlackboardVO> findFirst, Map<String, List<MatriculaPeriodoTurmaDisciplinaVO>> mapMptd, Integer qtdAlunoPermitidos, boolean isAddAlunoAteQtdPermitida, PersonalizacaoMensagemAutomaticaVO mensagemTemplate, UsuarioVO usuarioVO) throws Exception {
		if(!mapMptd.containsKey(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino())) {
			return;
		}
		String msgNotificacao = null;
		TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum = null;
		int qtdPermitida = qtdAlunoPermitidos - findFirst.get().getQtdeAlunos() ;
		for (int i = 0; i < qtdPermitida; i++) {
			MatriculaPeriodoTurmaDisciplinaVO mptd  = mapMptd.get(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()).isEmpty() ? null : mapMptd.get(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()).get(0);
			if(mptd == null ) {
				return;
			}
			SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
			sabAluno.getPessoaEmailInstitucionalVO().setNome(mptd.getMatriculaObjetoVO().getAluno().getNome());
			sabAluno.setMatricula(mptd.getMatriculaObjetoVO().getMatricula());
			findFirst.get().getListaAlunos().add(sabAluno);
			findFirst.get().setQtdeAlunos(findFirst.get().getQtdeAlunos() + 1);
			sala.setQtdAlunosEnsalados(sala.getQtdAlunosEnsalados() + 1);
			sala.setVagasSalaAulaBlackboard(sala.getVagasSalaAulaBlackboard() - 1);
			msgNotificacao = null;
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				msgNotificacao = obterMensagemFormatadaMensagemNotificacaoInclusaoSalaAulaBlackboardGrupo(findFirst.get(), sabAluno, mensagemTemplate.getMensagem());
				templateMensagemAutomaticaEnum = mensagemTemplate.getTemplateMensagemAutomaticaEnum();
			}
			incluirPessoaSalaBlack(findFirst.get(),  mptd.getMatriculaObjetoVO().getAluno().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, mptd.getMatriculaObjetoVO().getMatricula(), mptd.getCodigo(), null, msgNotificacao, templateMensagemAutomaticaEnum, null, usuarioVO);			
			mapMptd.get(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()).remove(0);
			if(mapMptd.get(sala.getIdDisciplinaPorAgrupamentoUnidadeEnsino()).isEmpty() || !isAddAlunoAteQtdPermitida) {
				break;
			}
		}	
	}
	
	public String obterMensagemFormatadaMensagemNotificacaoAlteracaoSalaAulaBlackboardGrupo(SalaAulaBlackboardVO sabAtual,  SalaAulaBlackboardPessoaVO aluno, String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), aluno.getPessoaEmailInstitucionalVO().getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), aluno.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SALA_BLACKBOARD_GRUPO_ANTERIOR.name(), aluno.getNomeGrupoOrigem());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SALA_BLACKBOARD_GRUPO_ATUAL.name(), sabAtual.getNomeGrupo());
		return mensagemTexto;
	}
	
	public String obterMensagemFormatadaMensagemNotificacaoInclusaoSalaAulaBlackboardGrupo(SalaAulaBlackboardVO sabAtual,  SalaAulaBlackboardPessoaVO aluno, String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), aluno.getPessoaEmailInstitucionalVO().getPessoaVO().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), aluno.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.SALA_BLACKBOARD_GRUPO_ATUAL.name(), sabAtual.getNomeGrupo());
		return mensagemTexto;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void executarEnvioMensagemSalaAulaBlackboardOperacao(Integer codigoOperacao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select  salaaulablackboardoperacao.msgNotificacao, salaaulablackboardoperacao.templateMensagemAutomaticaEnum, ");
		sql.append(" pessoa.codigo as pessoa_codigo, ");
		sql.append(" pessoa.nome as pessoa_nome, ");
		sql.append(" pessoa.email as pessoa_email ");
		sql.append(" from salaaulablackboardoperacao  ");
		sql.append(" left join pessoa on pessoa.codigo = salaaulablackboardoperacao.pessoa  ");
		sql.append(" where salaaulablackboardoperacao.codigo = ?  and msgnotificacaoexecutada = false ");
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoOperacao);
		if(rs.next()) {
			SalaAulaBlackboardOperacaoVO obj = new SalaAulaBlackboardOperacaoVO();
			obj.setMsgNotificacao(rs.getString("msgNotificacao"));
			if(Uteis.isAtributoPreenchido(rs.getString("templateMensagemAutomaticaEnum"))) {
				obj.setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.valueOf(rs.getString("templateMensagemAutomaticaEnum")));
			}			
			obj.getPessoaVO().setCodigo(rs.getInt("pessoa_codigo"));
			obj.getPessoaVO().setNome(rs.getString("pessoa_nome"));
			obj.getPessoaVO().setEmail(rs.getString("pessoa_email"));
			if(Uteis.isAtributoPreenchido(obj.getPessoaVO().getEmail())) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoSalaAulaBlackboardOperacao(obj, obj.getTemplateMensagemAutomaticaEnum(), usuarioVO);	
			}
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarExclusaoAlunoDaSalaAulaBlackboardTipoDisciplina(String matricula, Integer disciplina, UsuarioVO usuario) throws Exception {
		SalaAulaBlackboardPessoaVO sap = getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarSalaAulaBlackboardPessoaPorMatriculaPorDisciplinaPorTipoSalaAulaBlackboardEnum(matricula, disciplina, TipoSalaAulaBlackboardEnum.DISCIPLINA, usuario);
		if(Uteis.isAtributoPreenchido(sap)) {
			excluirPessoaSalaBlack(sap.getSalaAulaBlackboardVO().getCodigo(), sap.getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, null, sap.getPessoaEmailInstitucionalVO().getEmail(), usuario);
		}
	}
	
	private String getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select  count(*) over() as totalRegistroConsulta, salaaulablackboardoperacao.codigo, salaaulablackboardoperacao.listaAlunosEnsalar, salaaulablackboardoperacao.matricula, ");
		sql.append(" case when salaaulablackboardoperacao.ano is not null and salaaulablackboardoperacao.ano != '' then salaaulablackboardoperacao.ano else salaaulablackboard.ano end as ano, ");
		sql.append(" case when salaaulablackboardoperacao.semestre is not null and salaaulablackboardoperacao.semestre != '' then salaaulablackboardoperacao.semestre else salaaulablackboard.semestre end as semestre, ");
		sql.append(" case when salaaulablackboardoperacao.bimestre is not null then salaaulablackboardoperacao.bimestre else salaaulablackboard.bimestre end as bimestre, ");
		sql.append(" salaaulablackboardoperacao.created, salaaulablackboardoperacao.tipoOrigem, salaaulablackboardoperacao.operacao, salaaulablackboardoperacao.programacaoTutoriaOnline, salaaulablackboardoperacao.erro, ");
		sql.append(" salaaulablackboardoperacao.msgNotificacao,  salaaulablackboardoperacao.erroMsgNotificacao, salaaulablackboardoperacao.templateMensagemAutomaticaEnum, salaaulablackboardoperacao.tipoSalaAulaBlackboardPessoaEnum , salaaulablackboardoperacao.tipoSalaAulaBlackboardEnum, ");
		sql.append(" salaaulablackboardoperacao.gradeCurricularEstagio,  salaaulablackboardoperacao.idsalaAulaBlackboard, salaaulablackboardoperacao.codigoOrigem, ");
		sql.append(" disciplina.codigo as disciplina, salaaulablackboardoperacao.turma, salaaulablackboardoperacao.curso, ");
		sql.append(" disciplina.nome as disciplina_nome, disciplina.abreviatura as disciplina_abreviatura, ");
		sql.append(" case when salaaulablackboard.codigo is not null then salaaulablackboard.nrsala else salaaulablackboardoperacao.nrsala end as nrsala, ");
		sql.append(" curso.nome as curso_nome, ");
		sql.append(" pessoa.codigo as pessoa, ");
		sql.append(" pessoa.nome as pessoa_nome, ");
		sql.append(" pessoaemailinstitucional.email as pessoa_email, ");
		sql.append(" turma.identificadorTurma as  turma_identificadorTurma, ");
		sql.append(" gradecurricularestagio.nome as gradecurricularestagio_nome, ");
		sql.append(" salaaulablackboard.codigo as salaaulablackboard, ");
		sql.append(" salaaulablackboard.nome as salaaulablackboard_nome, ");
		sql.append(" salaaulablackboard.nomegrupo as salaaulablackboard_nomegrupo, ");
		sql.append(" salaaulablackboard.tipoSalaAulaBlackboardEnum as salaaulablackboard_tipoSalaAulaBlackboardEnum ");
		sql.append(" from salaaulablackboardoperacao  ");
		sql.append(" left join salaaulablackboard on ((salaaulablackboardoperacao.codigoorigem is not null and salaaulablackboard.codigo = salaaulablackboardoperacao.codigoorigem  ");
		sql.append(" and ((salaaulablackboardoperacao.tipoorigem in ('SALA_AULA_BLACKBOARD', 'GRUPO_SALA_AULA_BLACKBOARD_EXISTENTE') and salaaulablackboardoperacao.operacao = 'DELETE') ");
		sql.append(" or (salaaulablackboardoperacao.tipoorigem in ('SALA_AULA_BLACKBOARD_PESSOA','CONSOLIDAR_NOTA_SALA_AULA_BLACBOARD','APURAR_NOTA_SALA_AULA_BLACBOARD')))) ");
		sql.append(" or (salaaulablackboardoperacao.idsalaAulaBlackboard is not null and salaaulablackboardoperacao.idsalaAulaBlackboard = salaaulablackboard.id))");
		sql.append(" left join pessoaemailinstitucional on pessoaemailinstitucional.email = case when salaaulablackboardoperacao.email is null or salaaulablackboardoperacao.email = '' then null else salaaulablackboardoperacao.email end "); 
		sql.append(" left join pessoa on pessoa.codigo = (case when salaaulablackboardoperacao.pessoa is not null then salaaulablackboardoperacao.pessoa else case when pessoaemailinstitucional.codigo is not null then pessoaemailinstitucional.pessoa else null end end )  ");
		sql.append(" left join gradecurricularestagio on gradecurricularestagio.codigo = salaaulablackboardoperacao.gradecurricularestagio  ");		
		sql.append(" left join disciplina on disciplina.codigo = case when salaaulablackboardoperacao.disciplina is not null then salaaulablackboardoperacao.disciplina else salaaulablackboard.disciplina end   ");
		sql.append(" left join curso on curso.codigo =  case when salaaulablackboardoperacao.curso is not null then salaaulablackboardoperacao.curso else salaaulablackboard.curso end   ");
		sql.append(" left join turma on turma.codigo = case when salaaulablackboardoperacao.turma is not null then salaaulablackboardoperacao.turma else  salaaulablackboard.turma end  ");
		return sql.toString();
	}
	
	@Override
	public void consultarLogErroProcessamento(DataModelo controleConsulta, SalaAulaBlackboardOperacaoVO salaAulaBlackboardOperacaoFiltroVO) throws Exception{
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" where ((executada and erro != '') or (msgnotificacaoexecutada and erromsgnotificacao != '')) and ");
		sql.append(realizarGeracaoWhereDataInicioDataTermino(controleConsulta.getDataIni(), controleConsulta.getDataFim(), "salaaulablackboardoperacao.created", "salaaulablackboardoperacao.created", false));
		List<Object> filtros = new ArrayList<Object>(0);
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getDisciplinaVO().getNome())) {
			sql.append(" and (sem_acentos(disciplina.nome) ilike sem_acentos(?)  ) ");
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getDisciplinaVO().getNome()+PERCENT);
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getDisciplinaVO().getAbreviatura())) {
			sql.append(" and (sem_acentos(disciplina.abreviatura) ilike sem_acentos(?) ) ");
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getDisciplinaVO().getAbreviatura()+PERCENT);
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getPessoaVO().getNome())) {
			sql.append(" and (sem_acentos(pessoa.nome) ilike sem_acentos(?) ) ");
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getPessoaVO().getNome()+PERCENT);
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getMatricula())) {
			sql.append(" and (sem_acentos(salaaulablackboardoperacao.matricula) ilike sem_acentos(?) ) ");
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getMatricula()+PERCENT);
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getOperacao())) {
			sql.append(" and (salaaulablackboardoperacao.operacao = ? ) ");
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getOperacao());
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getTipoOrigem())) {
			if(salaAulaBlackboardOperacaoFiltroVO.getTipoOrigem().equals(GRUPO_SALA_AULA_BLACKBOARD_EXISTENTE)||salaAulaBlackboardOperacaoFiltroVO.getTipoOrigem().equals(GRUPO_SALA_AULA_BLACKBOARD_OPERACAO)) {
				sql.append(" and (salaaulablackboardoperacao.tipoOrigem = ? or salaaulablackboardoperacao.tipoOrigem = ? ) ");
				filtros.add(GRUPO_SALA_AULA_BLACKBOARD_EXISTENTE);
				filtros.add(GRUPO_SALA_AULA_BLACKBOARD_OPERACAO);
			}else {
				sql.append(" and (salaaulablackboardoperacao.tipoOrigem = ? ) ");
				filtros.add(salaAulaBlackboardOperacaoFiltroVO.getTipoOrigem());
			}
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getTipoSalaAulaBlackboardEnum()) && !salaAulaBlackboardOperacaoFiltroVO.getTipoSalaAulaBlackboardEnum().equals(TipoSalaAulaBlackboardEnum.NENHUM)) {
			sql.append(" and (salaaulablackboardoperacao.tiposalaaulablackboardenum= ? or salaaulablackboard.tiposalaaulablackboardenum = ? ) ");
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getTipoSalaAulaBlackboardEnum().name());
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getTipoSalaAulaBlackboardEnum().name());
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getErro())) {
			sql.append(" and ((sem_acentos(salaaulablackboardoperacao.erro) ilike sem_acentos(?) ) ");
			filtros.add(PERCENT+salaAulaBlackboardOperacaoFiltroVO.getErro()+PERCENT);
			sql.append(" or (sem_acentos(salaaulablackboardoperacao.erroMsgNotificacao) ilike sem_acentos(?) ) ");
			filtros.add(PERCENT+salaAulaBlackboardOperacaoFiltroVO.getErro()+PERCENT);
			sql.append(" ) ");
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getAno())) {
			sql.append(" and (salaaulablackboardoperacao.ano = ? or salaaulablackboard.ano = ? ) ");
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getAno());
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getAno());
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getSemestre())) {
			sql.append(" and (salaaulablackboardoperacao.semestre = ? or salaaulablackboard.semestre = ? ) ");
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getSemestre());
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getSemestre());
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getSalaAulaBlackboardVO().getIdSalaAulaBlackboard())) {
			sql.append(" and (sem_acentos(salaAulaBlackboard.idSalaAulaBlackboard) ilike sem_acentos(?) ) ");
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getSalaAulaBlackboardVO().getIdSalaAulaBlackboard()+PERCENT);			
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboardOperacaoFiltroVO.getSalaAulaBlackboardVO().getNomeGrupo())) {
			sql.append(" and (sem_acentos(salaAulaBlackboard.nomeGrupo) ilike sem_acentos(?) ) ");
			filtros.add(salaAulaBlackboardOperacaoFiltroVO.getSalaAulaBlackboardVO().getIdSalaAulaBlackboard()+PERCENT);			
		}
		sql.append(" order by codigo desc ");
		
		sql.append(" limit ").append(controleConsulta.getLimitePorPagina()).append(" offset ").append(controleConsulta.getOffset());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
		montarTotalizadorConsultaBasica(controleConsulta, rs);
		controleConsulta.setListaConsulta(montarDadosConsultaCompleto(rs));
	}
	
	private List<SalaAulaBlackboardOperacaoVO> montarDadosConsultaCompleto(SqlRowSet rs){
		List<SalaAulaBlackboardOperacaoVO> salaAulaBlackboardOperacaoVOs = new ArrayList<SalaAulaBlackboardOperacaoVO>(0);
		while(rs.next()) {
			salaAulaBlackboardOperacaoVOs.add(montarDadosCompleto(rs));
		}
		return salaAulaBlackboardOperacaoVOs;
		
	}
	
	private SalaAulaBlackboardOperacaoVO montarDadosCompleto(SqlRowSet rs){
		SalaAulaBlackboardOperacaoVO obj = new SalaAulaBlackboardOperacaoVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.setAno(rs.getString("ano"));
		obj.setBimestre(rs.getInt("bimestre"));
		obj.setCreated(rs.getDate("created"));
		obj.setCodigoOrigem(rs.getInt("codigoOrigem"));
		obj.setNrSala(rs.getInt("nrSala"));
		obj.setTipoOrigem(rs.getString("tipoOrigem"));
		obj.setOperacao(rs.getString("operacao"));		
		obj.setProgramacaoTutoriaOnline(rs.getInt("programacaoTutoriaOnline"));
		obj.setErro(rs.getString("erro"));
		obj.setListaAlunosEnsalar(rs.getString("listaAlunosEnsalar"));
		obj.setMatricula(rs.getString("matricula"));
		obj.setSemestre(rs.getString("semestre"));
		obj.setMsgNotificacao(rs.getString("msgNotificacao"));
		obj.setErroMsgNotificacao(rs.getString("erroMsgNotificacao"));
		if(Uteis.isAtributoPreenchido(rs.getString("templateMensagemAutomaticaEnum"))) {
			obj.setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum.valueOf(rs.getString("templateMensagemAutomaticaEnum")));
		}
		if(Uteis.isAtributoPreenchido(rs.getString("tipoSalaAulaBlackboardPessoaEnum"))) {
			obj.setTipoSalaAulaBlackboardPessoaEnum(TipoSalaAulaBlackboardPessoaEnum.valueOf(rs.getString("tipoSalaAulaBlackboardPessoaEnum")));
		}
		if(Uteis.isAtributoPreenchido(rs.getString("tipoSalaAulaBlackboardEnum"))) {
			obj.setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum.valueOf(rs.getString("tipoSalaAulaBlackboardEnum")));
		}else if(Uteis.isAtributoPreenchido(rs.getString("salaaulablackboard_tipoSalaAulaBlackboardEnum"))) {
			obj.setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum.valueOf(rs.getString("salaaulablackboard_tipoSalaAulaBlackboardEnum")));
		}
		obj.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		obj.getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
		obj.getDisciplinaVO().setAbreviatura(rs.getString("disciplina_abreviatura"));
		obj.getCursoVO().setCodigo(rs.getInt("curso"));
		obj.getCursoVO().setNome(rs.getString("curso_nome"));
		obj.getGradeCurricularEstagioVO().setCodigo(rs.getInt("gradeCurricularEstagio"));
		obj.getGradeCurricularEstagioVO().setNome(rs.getString("gradeCurricularEstagio_nome"));		
		obj.getSalaAulaBlackboardVO().setCodigo(rs.getInt("salaAulaBlackboard"));
		obj.getSalaAulaBlackboardVO().setNome(rs.getString("salaAulaBlackboard_nome"));
		obj.getSalaAulaBlackboardVO().setNomeGrupo(rs.getString("salaaulablackboard_nomegrupo"));
		obj.getPessoaVO().setCodigo(rs.getInt("pessoa"));
		obj.getPessoaVO().setNome(rs.getString("pessoa_nome"));
		obj.getPessoaVO().setEmail(rs.getString("pessoa_email"));
		obj.getTurmaVO().setCodigo(rs.getInt("turma"));
		obj.getTurmaVO().setIdentificadorTurma(rs.getString("turma_identificadorTurma"));
		return obj;
		
	}
	
		
	@Override	
	public void realizarInativacaoPessoaBlack(String email, UsuarioVO usuarioVO) throws Exception{
		if (!Uteis.isVersaoDev()) {
			realizarAtualizacaoInativacaoAtivacaoUsuarioBlack(email, INATIVAR_USUARIO, usuarioVO);
		}
	}
	
	
	@Override	
	public void realizarAtivacaoPessoaBlack(String email, UsuarioVO usuarioVO) throws Exception{
		if (!Uteis.isVersaoDev()) {
			realizarAtualizacaoInativacaoAtivacaoUsuarioBlack(email, ATIVAR_USUARIO, usuarioVO);
		}
	}
	
	@Override	
	public void realizarAtualizacaoCadastralPessoaBlack(String email, UsuarioVO usuarioVO) throws Exception{
		if (!Uteis.isVersaoDev()) {
			realizarAtualizacaoInativacaoAtivacaoUsuarioBlack(email, ATUALIZAR_USUARIO, usuarioVO);
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarAtualizacaoInativacaoAtivacaoUsuarioBlack(String email, String operacao, UsuarioVO usuarioVO) throws Exception{
		RealizarAtualizacaoInativacaoAtivacaoUsuarioBlack realizarAtualizacaoInativacaoAtivacaoUsuarioBlack = new RealizarAtualizacaoInativacaoAtivacaoUsuarioBlack(email, operacao, usuarioVO);
		Thread numMat = new Thread(realizarAtualizacaoInativacaoAtivacaoUsuarioBlack);
		numMat.start();
		while(numMat.isAlive()) {
			Thread.sleep(50);
		}
		if(Uteis.isAtributoPreenchido(realizarAtualizacaoInativacaoAtivacaoUsuarioBlack.getCodigo())) {
			realizarExecucaoSalaAulaOperacao(realizarAtualizacaoInativacaoAtivacaoUsuarioBlack.getCodigo(), usuarioVO);
		}
	}
	
	private class RealizarAtualizacaoInativacaoAtivacaoUsuarioBlack implements Runnable {
		String email;
		String operacao;
		UsuarioVO usuarioVO;
		Integer codigo;
		
		public RealizarAtualizacaoInativacaoAtivacaoUsuarioBlack(String email, String operacao, UsuarioVO usuario){
			this.email = email;
			this.usuarioVO = usuario;
			this.operacao = operacao;			
		}
		
		
		
		public Integer getCodigo() {
			return codigo;
		}



		public void setCodigo(Integer codigo) {
			this.codigo = codigo;
		}


		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public void run()  {
			try {
				setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
					@Override
					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						StringBuilder sql = new StringBuilder("");			
						sql.append("INSERT INTO public.salaaulablackboardoperacao ");
						sql.append(" (tipoorigem, operacao, executada, created, codigocreated, nomecreated, email) ");
						sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?) returning codigo ");
						int x = 1;
						PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
						Uteis.setValuePreparedStatement(operacao, x++, sqlInserir);
						Uteis.setValuePreparedStatement(operacao, x++, sqlInserir);
						Uteis.setValuePreparedStatement(false, x++, sqlInserir);
						Uteis.setValuePreparedStatement(new Date(), x++, sqlInserir);
						Uteis.setValuePreparedStatement(usuarioVO.getCodigo(), x++, sqlInserir);
						Uteis.setValuePreparedStatement(usuarioVO.getNome(), x++, sqlInserir);									
						Uteis.setValuePreparedStatement(email, x++, sqlInserir);				
						return sqlInserir;
					}
				}, new ResultSetExtractor<Integer>() {

					public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
						if (arg0.next()) {					
							
							return arg0.getInt("codigo");
						}
						return null;
					}
				}));
			} catch (Exception e) {
				
			}			
		}



	
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirLogErro(SalaAulaBlackboardVO salaAulaBlackboardVO, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum,  Integer pessoa,  String email, String matricula, Integer matriculaPeriodoTurmaDisciplina, String ano, String semestre, String idSalaAulaBlackboard, String tipoOrigem, String operacao, String erro, UsuarioVO usuarioVO) {
		try {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("");			
				sql.append("INSERT INTO public.salaaulablackboardoperacao ");
				sql.append(" (operacao, executada, curso, disciplina, codigoorigem, tipoorigem,  created, codigocreated, nomecreated, tipoSalaAulaBlackboardPessoaEnum, pessoa, matricula, matriculaPeriodoTurmaDisciplina, ano, semestre, idsalaaulablackboard, email, erro) ");
				sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
				int x = 1;
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				Uteis.setValuePreparedStatement(operacao, x++, sqlInserir);
				Uteis.setValuePreparedStatement(true, x++, sqlInserir);				
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO != null && Uteis.isAtributoPreenchido(salaAulaBlackboardVO.getCursoVO()) ? salaAulaBlackboardVO.getCursoVO().getCodigo() : null, x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO != null && Uteis.isAtributoPreenchido(salaAulaBlackboardVO.getDisciplinaVO()) ? salaAulaBlackboardVO.getDisciplinaVO().getCodigo() : null, x++, sqlInserir);
				Uteis.setValuePreparedStatement(salaAulaBlackboardVO != null && Uteis.isAtributoPreenchido(salaAulaBlackboardVO) ? salaAulaBlackboardVO.getCodigo() : null, x++, sqlInserir);
				Uteis.setValuePreparedStatement(tipoOrigem, x++, sqlInserir);
				Uteis.setValuePreparedStatement(new Date(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getCodigo(), x++, sqlInserir);
				Uteis.setValuePreparedStatement(usuarioVO.getNome(), x++, sqlInserir);				
				Uteis.setValuePreparedStatement(tipoSalaAulaBlackboardPessoaEnum, x++, sqlInserir);	
				Uteis.setValuePreparedStatement(pessoa, x++, sqlInserir);
				Uteis.setValuePreparedStatement(matricula, x++, sqlInserir);
				Uteis.setValuePreparedStatement(matriculaPeriodoTurmaDisciplina, x++, sqlInserir);
				Uteis.setValuePreparedStatement(ano, x++, sqlInserir);
				Uteis.setValuePreparedStatement(semestre, x++, sqlInserir);
				Uteis.setValuePreparedStatement(idSalaAulaBlackboard, x++, sqlInserir);
				Uteis.setValuePreparedStatement(email, x++, sqlInserir);				
				Uteis.setValuePreparedStatement(erro, x++, sqlInserir);
				
				return sqlInserir;
			}
		});
		}catch (Exception e) {
			//Aqui não deve retornar a execeção
			e.printStackTrace();
		}
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Integer consultarQuantidadeOperacaoRestantesPorSalaAulaBlackboard(SalaAulaBlackboardVO obj, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(distinct salaaulablackboardoperacao.codigo) AS QTDE FROM salaaulablackboardoperacao ");
		sql.append(" WHERE salaaulablackboardoperacao.codigoorigem = ? ");
		sql.append(" AND salaaulablackboardoperacao.executada = false ");
		sql.append(" AND salaaulablackboardoperacao.tipoorigem = '").append(ORIGEM_SALA_AULA_BLACKBOARD_PESSOA).append("' ");
		sql.append(" AND salaaulablackboardoperacao.operacao = '").append(OPERACAO_INCLUIR).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {obj.getCodigo()});
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	
}

