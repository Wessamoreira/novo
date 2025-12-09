/**
 * 
 */
package negocio.facade.jdbc.patrimonio;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.OcorrenciaPatrimonioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.enumeradores.SituacaoOcorrenciaPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.SituacaoPatrimonioUnidadeEnum;
import negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.patrimonio.OcorrenciaPatrimonioInterfaceFacade;

/**
 * @author Rodrigo Wind
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class OcorrenciaPatrimonio extends ControleAcesso implements OcorrenciaPatrimonioInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4658920126580528872L;
	private static String idEntidade;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.interfaces.patrimonio.OcorrenciaPatrimonioInterfaceFacade#persistir
	 * (negocio.comuns.patrimonio.OcorrenciaPatrimonioVO, boolean,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, boolean validarAcesso, boolean permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante, boolean permiteLiberarReservaForaLimiteDataMaxima, UsuarioVO usuarioVO) throws Exception {

		if (ocorrenciaPatrimonioVO.isNovoObj()) {
			incluir(ocorrenciaPatrimonioVO, validarAcesso, permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante,  permiteLiberarReservaForaLimiteDataMaxima, usuarioVO);
		} else {
			alterar(ocorrenciaPatrimonioVO, validarAcesso, permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante,  permiteLiberarReservaForaLimiteDataMaxima, usuarioVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, boolean validarAcesso, boolean permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante, boolean permiteLiberarReservaForaLimiteDataMaxima,  UsuarioVO usuarioVO) throws Exception {
		try {
			setIdEntidade(usuarioVO.getIsApresentarVisaoCoordenador() ? "OcorrenciaPatrimonioVisaoCoordenador" : usuarioVO.getIsApresentarVisaoProfessor() ? "OcorrenciaPatrimonioVisaoProfessor" : "OcorrenciaPatrimonio");
			OcorrenciaPatrimonio.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			validarDados(ocorrenciaPatrimonioVO, permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante, permiteLiberarReservaForaLimiteDataMaxima,  usuarioVO);
			final StringBuilder sql = new StringBuilder("INSERT INTO OcorrenciaPatrimonio (");
			sql.append(" situacao, dataOcorrencia, responsavelOcorrencia, tipoOcorrenciaPatrimonio, patrimonioUnidade, ");
			sql.append(" localArmazenamentoOrigem, localArmazenamentoDestino, motivo, solicitanteEmprestimo, dataPrevisaoDevolucao, ");
			sql.append(" dataFinalizacao, responsavelFinalizacao, observacao, dataInicioReserva, dataTerminoReserva, localreservado ");
			sql.append(" ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			sql.append(" returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			ocorrenciaPatrimonioVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setString(x++, ocorrenciaPatrimonioVO.getSituacao().name());
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataOcorrencia()));
					ps.setInt(x++, ocorrenciaPatrimonioVO.getResponsavelOcorrencia().getCodigo());
					ps.setString(x++, ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().name());
					if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getPatrimonioUnidade().getCodigo())) {
						ps.setInt(x++, ocorrenciaPatrimonioVO.getPatrimonioUnidade().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem())) {
						ps.setInt(x++, ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino()) && ocorrenciaPatrimonioVO.getIsApresentarLocalArmazenamentoDestino()) {
						ps.setInt(x++, ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setString(x++, ocorrenciaPatrimonioVO.getMotivo());
					if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getSolicitanteEmprestimo()) && (ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL) || ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE) || ocorrenciaPatrimonioVO.getIsTipoOcorrenciaEmprestimo()) ) {
						ps.setInt(x++, ocorrenciaPatrimonioVO.getSolicitanteEmprestimo().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getDataPrevisaoDevolucao()) && ocorrenciaPatrimonioVO.getIsApresentarDataPrevisaoDevolucao()) {
						ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataPrevisaoDevolucao()));
					} else {
						ps.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getDataFinalizacao()) && ocorrenciaPatrimonioVO.getSituacao().equals(SituacaoOcorrenciaPatrimonioEnum.FINALIZADO)) {
						ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataFinalizacao()));
					} else {
						ps.setNull(x++, 0);
					}
					if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getResponsavelFinalizacao()) && ocorrenciaPatrimonioVO.getSituacao().equals(SituacaoOcorrenciaPatrimonioEnum.FINALIZADO)) {
						ps.setInt(x++, ocorrenciaPatrimonioVO.getResponsavelFinalizacao().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setString(x++, ocorrenciaPatrimonioVO.getObservacao());
					if(Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getDataInicioReserva()) && (ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL)) || ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE)){
						ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataInicioReserva()));
					}else{
						ps.setNull(x++, 0);
					}
					if(Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getDataInicioReserva()) && (ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL) || ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE))){
						ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataTerminoReserva()));
					}else{
						ps.setNull(x++, 0);
					}
					
					if(Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getLocalReservado().getCodigo())){
						ps.setInt(x++, ocorrenciaPatrimonioVO.getLocalReservado().getCodigo());
					}else{
						ps.setNull(x++, 0);
					}
					return ps;
				}
				
			}, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			if(ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE) ||ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.TROCA_LOCAL)){
				alterarSituacaoPatrimonioUnidadeComBaseOcorrencia(ocorrenciaPatrimonioVO, usuarioVO);
			}
			ocorrenciaPatrimonioVO.setNovoObj(false);
		} catch (Exception e) {
			ocorrenciaPatrimonioVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, boolean validarAcesso, boolean permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante, boolean permiteLiberarReservaForaLimiteDataMaxima, UsuarioVO usuarioVO) throws Exception {
		setIdEntidade(usuarioVO.getIsApresentarVisaoCoordenador() ? "OcorrenciaPatrimonioVisaoCoordenador" : usuarioVO.getIsApresentarVisaoProfessor() ? "OcorrenciaPatrimonioVisaoProfessor" : "OcorrenciaPatrimonio");
		OcorrenciaPatrimonio.alterar(getIdEntidade(), validarAcesso, usuarioVO);
		validarDados(ocorrenciaPatrimonioVO, permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante,  permiteLiberarReservaForaLimiteDataMaxima, usuarioVO);
		final StringBuilder sql = new StringBuilder("UPDATE OcorrenciaPatrimonio SET ");
		sql.append(" situacao = ?, motivo = ?, dataPrevisaoDevolucao = ?, ");
		sql.append(" dataFinalizacao = ?, responsavelFinalizacao = ?, observacao =? ");
		sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setString(x++, ocorrenciaPatrimonioVO.getSituacao().name());
				ps.setString(x++, ocorrenciaPatrimonioVO.getMotivo());
				if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getDataPrevisaoDevolucao()) && ocorrenciaPatrimonioVO.getIsApresentarDataPrevisaoDevolucao()) {
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataPrevisaoDevolucao()));
				} else {
					ps.setNull(x++, 0);
				}
				if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getDataFinalizacao()) && ocorrenciaPatrimonioVO.getIsFinalizado()) {
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataFinalizacao()));
				} else {
					ps.setNull(x++, 0);
				}
				if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getResponsavelFinalizacao()) && ocorrenciaPatrimonioVO.getIsFinalizado()) {
					ps.setInt(x++, ocorrenciaPatrimonioVO.getResponsavelFinalizacao().getCodigo());
				} else {
					ps.setNull(x++, 0);
				}
				ps.setString(x++, ocorrenciaPatrimonioVO.getObservacao());
				ps.setInt(x++, ocorrenciaPatrimonioVO.getCodigo());
				return ps;
			}
		});
		alterarSituacaoPatrimonioUnidadeComBaseOcorrencia(ocorrenciaPatrimonioVO, usuarioVO);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.patrimonio.OcorrenciaPatrimonioInterfaceFacade#
	 * validarDados(negocio.comuns.patrimonio.OcorrenciaPatrimonioVO)
	 */
	@Override
	public void validarDados(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, boolean permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante,  boolean permiteLiberarReservaForaLimiteDataMaxima, UsuarioVO usuarioVO) throws ConsistirException, Exception {
		if (ocorrenciaPatrimonioVO.isNovoObj()) {
			ocorrenciaPatrimonioVO.getResponsavelOcorrencia().setCodigo(usuarioVO.getCodigo());
			ocorrenciaPatrimonioVO.getResponsavelOcorrencia().setNome(usuarioVO.getNome());
			ocorrenciaPatrimonioVO.setDataOcorrencia(new Date());
		}
		if (!Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_tipoOcorrenciaPatrimonio"));
		}
		if ((ocorrenciaPatrimonioVO.getIsTipoOcorrenciaEmprestimo() || ocorrenciaPatrimonioVO.getIsApresentarDadosReservaUnidade()) && !Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getSolicitanteEmprestimo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_solicitanteEmprestimo"));
		}
		if (ocorrenciaPatrimonioVO.getIsApresentarLocalArmazenamentoDestino() && !Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_localArmazenamentoDestino"));
		}
		if (!Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getPatrimonioUnidade().getCodigo()) && !ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL) ) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_patrimonioUnidade"));
		}
		if (!Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getLocalReservado().getCodigo()) && ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL) ) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_localDeveSerInformado"));
		}
		if (ocorrenciaPatrimonioVO.getIsTipoOcorrenciaEmprestimo() && !Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getDataPrevisaoDevolucao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_dataPrevisao"));
		}
		if (ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.DESCARTE) 
				|| ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE) 
				|| ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.TROCA_LOCAL)) {
			ocorrenciaPatrimonioVO.setDataFinalizacao(new Date());
			ocorrenciaPatrimonioVO.setSituacao(SituacaoOcorrenciaPatrimonioEnum.FINALIZADO);
			ocorrenciaPatrimonioVO.getResponsavelFinalizacao().setCodigo(usuarioVO.getCodigo());
			ocorrenciaPatrimonioVO.getResponsavelFinalizacao().setNome(usuarioVO.getNome());
		}
		
		if(ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE) ){
			ocorrenciaPatrimonioVO.setTipoPatrimonioVO(getFacadeFactory().getTipoPatrimonioFacede().consultarPorChavePrimaria(ocorrenciaPatrimonioVO.getTipoPatrimonioVO().getCodigo(), false, usuarioVO));
			if(!permiteLiberarReservaAcimaQuantidadeLimitePorRequisitante){
				if(consultarTotalOcorrenciaPatrimonioPorDataETipoPatrimonio(ocorrenciaPatrimonioVO) >= ocorrenciaPatrimonioVO.getTipoPatrimonioVO().getQuantidadeReservasSimultaneas()){
					throw new ConsistirException("O TIPO PATRIMONIO Selecionado não permite mais reservas SIMULTÂNEAS"); 
				}
			}
			if(!permiteLiberarReservaForaLimiteDataMaxima){
				Calendar dataReserva = Calendar.getInstance();
				Calendar dataAtual = Calendar.getInstance();
				
				dataReserva.setTime(ocorrenciaPatrimonioVO.getDataInicioReserva());
				dataAtual.setTime(new Date());
				int dias = dataReserva.get(Calendar.DAY_OF_YEAR) - dataAtual.get(Calendar.DAY_OF_YEAR);
				if(dias > ocorrenciaPatrimonioVO.getTipoPatrimonioVO().getQuantidadeDiasLimiteReserva()){
					throw new ConsistirException("O TIPO PATRIMONIO Selecionado não permite reversas para uma data futura maior que " + ocorrenciaPatrimonioVO.getTipoPatrimonioVO().getQuantidadeDiasLimiteReserva() 	+ " dia(s) "); 
				}
			}
		}
		if(ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL) ){
			if(!validarUnidicadeLocalRequisitanteData(ocorrenciaPatrimonioVO)){
				throw new ConsistirException("Já existe uma reserva de LOCAL para este solicitante no horário solicitado. "); 
			}
			if(!permiteLiberarReservaForaLimiteDataMaxima){
				
				Calendar dataReserva = Calendar.getInstance();
				Calendar dataAtual = Calendar.getInstance();
				
				dataReserva.setTime(ocorrenciaPatrimonioVO.getDataInicioReserva());
				dataAtual.setTime(new Date());
				int dias = dataReserva.get(Calendar.DAY_OF_YEAR) - dataAtual.get(Calendar.DAY_OF_YEAR);
				
				if(dias > ocorrenciaPatrimonioVO.getLocalReservado().getQuantidadeDiasLimiteReserva()){
					throw new ConsistirException("O LOCAL Selecionado não permite reversas para uma data futura maior que " + ocorrenciaPatrimonioVO.getLocalReservado().getQuantidadeDiasLimiteReserva() + " dias "); 
				}
			}
		}
		
	}

	@Override
	public void validarDadosPatrimonioAptoTipoOcorrencia(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, PatrimonioUnidadeVO patrimonioUnidadeVO, UsuarioVO usuarioVO) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_tipoOcorrenciaPatrimonio"));
		}
		if (ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO)) {
			if (!patrimonioUnidadeVO.getUnidadeLocado()) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_patrimonioUnidadeNaoPermiteEmprestimo"));
			}
			if (!patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().equals(SituacaoPatrimonioUnidadeEnum.DISPONIVEL)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_patrimonioUnidadeNaoDisponivelEmprestimo").replace("{0}", patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().getValorApresentar()));
			}
		} else if (ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.DESCARTE)) {
			if (patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().equals(SituacaoPatrimonioUnidadeEnum.DESCARTADO)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_patrimonioUnidadeJaDescartado"));
			}else if (!patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().equals(SituacaoPatrimonioUnidadeEnum.DISPONIVEL) 
					&& !patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().equals(SituacaoPatrimonioUnidadeEnum.SEPARADO_PARA_DESCARTE)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_patrimonioUnidadeNaoDisponivelDescate").replace("{0}", patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().getValorApresentar()));
			}
		} else if (ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.MANUTENCAO)) {
			if (!patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().equals(SituacaoPatrimonioUnidadeEnum.DISPONIVEL)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_patrimonioUnidadeNaoDisponivelManutencao").replace("{0}", patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().getValorApresentar()));
			}
		} else if (ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE)) {
			if (!patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().equals(SituacaoPatrimonioUnidadeEnum.DISPONIVEL)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_patrimonioUnidadeNaoDisponivelSeparacaoDescate").replace("{0}", patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().getValorApresentar()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.patrimonio.OcorrenciaPatrimonioInterfaceFacade#
	 * realizarRegistroDevolucaoPatrimonio
	 * (negocio.comuns.patrimonio.OcorrenciaPatrimonioVO, boolean,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroDevolucaoPatrimonio(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ocorrenciaPatrimonioVO.setDataFinalizacao(new Date());
			ocorrenciaPatrimonioVO.getResponsavelFinalizacao().setCodigo(usuarioVO.getCodigo());
			ocorrenciaPatrimonioVO.getResponsavelFinalizacao().setNome(usuarioVO.getNome());
			ocorrenciaPatrimonioVO.setSituacao(SituacaoOcorrenciaPatrimonioEnum.FINALIZADO);
			persistir(ocorrenciaPatrimonioVO, validarAcesso,false, false, usuarioVO);
		} catch (Exception e) {
			ocorrenciaPatrimonioVO.setSituacao(SituacaoOcorrenciaPatrimonioEnum.EM_ANDAMENTO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSituacaoPatrimonioUnidadeComBaseOcorrencia(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, UsuarioVO usuarioVO) throws Exception {
		SituacaoPatrimonioUnidadeEnum situacaoAnterior = ocorrenciaPatrimonioVO.getPatrimonioUnidade().getSituacaoPatrimonioUnidade();
		LocalArmazenamentoVO localArmazenamentoVO = ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem();
		try {
			SituacaoPatrimonioUnidadeEnum situacaoAlterar = ocorrenciaPatrimonioVO.getPatrimonioUnidade().getSituacaoPatrimonioUnidade();
			switch (ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio()) {
			case DESCARTE:
				situacaoAlterar = SituacaoPatrimonioUnidadeEnum.DESCARTADO;
				localArmazenamentoVO = ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino();
				break;
			case EMPRESTIMO:
				if (ocorrenciaPatrimonioVO.getSituacao().equals(SituacaoOcorrenciaPatrimonioEnum.FINALIZADO)) {
					situacaoAlterar = SituacaoPatrimonioUnidadeEnum.DISPONIVEL;
				} else {
					situacaoAlterar = SituacaoPatrimonioUnidadeEnum.EMPRESTADO;
				}
				break;
			case MANUTENCAO:
				if (ocorrenciaPatrimonioVO.getSituacao().equals(SituacaoOcorrenciaPatrimonioEnum.FINALIZADO)) {
					situacaoAlterar = SituacaoPatrimonioUnidadeEnum.DISPONIVEL;
				} else {
					situacaoAlterar = SituacaoPatrimonioUnidadeEnum.EM_MANUTENCAO;
				}
				break;
			case SEPARAR_DESCARTE:
				situacaoAlterar = SituacaoPatrimonioUnidadeEnum.SEPARADO_PARA_DESCARTE;
				localArmazenamentoVO = ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino();
				break;
			case TROCA_LOCAL:
				localArmazenamentoVO = ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino();
				break;
			default:
				break;
			}
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().setSituacaoPatrimonioUnidade(situacaoAlterar);
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().setLocalArmazenamento(localArmazenamentoVO);
			getFacadeFactory().getPatrimonioUnidadeFacade().alterarSituacaoELocalArmazenamentoPatrimonioUnidade(ocorrenciaPatrimonioVO.getPatrimonioUnidade(), false, usuarioVO);
		} catch (Exception e) {
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().setSituacaoPatrimonioUnidade(situacaoAnterior);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.interfaces.patrimonio.OcorrenciaPatrimonioInterfaceFacade#excluir
	 * (negocio.comuns.patrimonio.OcorrenciaPatrimonioVO, boolean,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			OcorrenciaPatrimonio.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			if (!ocorrenciaPatrimonioVO.isNovoObj() && ocorrenciaPatrimonioVO.getIsPermiteExcluir()) {
				StringBuilder sql = new StringBuilder("DELETE FROM OcorrenciaPatrimonio WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				getConexao().getJdbcTemplate().update(sql.toString(), ocorrenciaPatrimonioVO.getCodigo());
				ocorrenciaPatrimonioVO.getPatrimonioUnidade().setSituacaoPatrimonioUnidade(SituacaoPatrimonioUnidadeEnum.DISPONIVEL);
				ocorrenciaPatrimonioVO.getPatrimonioUnidade().getLocalArmazenamento().setCodigo(ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().getCodigo());
				ocorrenciaPatrimonioVO.getPatrimonioUnidade().getLocalArmazenamento().setLocalArmazenamento(ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().getLocalArmazenamento());
				if(!ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL)){
					getFacadeFactory().getPatrimonioUnidadeFacade().alterarSituacaoELocalArmazenamentoPatrimonioUnidade(ocorrenciaPatrimonioVO.getPatrimonioUnidade(), false, usuarioVO);
				}	
			}
		}catch (Exception e) {
			ocorrenciaPatrimonioVO.setNovoObj(false);
			throw e;
		}
	}

	public StringBuilder getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("SELECT OcorrenciaPatrimonio.*, ");
		sql.append(" patrimoniounidade.permiteReserva as \"patrimoniounidade.permiteReserva\", ");
		sql.append(" patrimoniounidade.codigoBarra as \"patrimoniounidade.codigoBarra\", ");
		sql.append(" patrimoniounidade.situacao as \"patrimoniounidade.situacao\", ");
		sql.append(" patrimoniounidade.patrimonio as \"patrimoniounidade.patrimonio\", ");
		sql.append(" patrimoniounidade.localArmazenamento as \"patrimonioUnidade.localArmazenamento\", ");
		sql.append(" patrimoniounidade.valorrecurso as \"patrimoniounidade.valorrecurso\", ");
		sql.append(" patrimoniounidade.numeroSerie as \"patrimoniounidade.numeroDeSerie\", ");
		sql.append(" patrimoniounidade.unidadeLocado as \"patrimoniounidade.unidadeLocado\", ");
		sql.append(" patrimonio.descricao as \"patrimonio.descricao\", ");
		sql.append(" patrimonio.dataentrada as \"patrimonio.dataentrada\", ");
		sql.append(" patrimonio.marca as \"patrimonio.marca\", ");
		sql.append(" patrimonio.modelo as \"patrimonio.modelo\", ");
		sql.append(" patrimonio.notafiscal as \"patrimonio.notafiscal\", ");
		
		sql.append(" tipopatrimonio.codigo as \"tipopatrimonio.codigo\", ");
		sql.append(" tipopatrimonio.descricao as \"tipopatrimonio.descricao\", ");
		sql.append(" fornecedor.nome as \"fornecedor.nome\", ");
		
		sql.append(" localArmazenamento.localArmazenamento as \"localArmazenamento.localArmazenamento\", ");
		sql.append(" localArmazenamentosuperior.localArmazenamento as \"localArmazenamentosuperior.localArmazenamento\", ");
		
		sql.append(" localArmazenamentoOrigem.localArmazenamento as \"localArmazenamentoOrigem.localArmazenamento\", ");
		sql.append(" localArmazenamentoOrigem.unidadeEnsino as \"localArmazenamentoOrigem.unidadeEnsino\", ");
		sql.append(" unidadeEnsino.nome as \"unidadeEnsino.nome\", ");					
		
		sql.append(" localArmazenamentoDestino.localArmazenamento as \"localArmazenamentoDestino.localArmazenamento\", ");
		sql.append(" responsavelOcorrencia.codigo as \"responsavelOcorrencia.codigo\", ");
		sql.append(" responsavelOcorrencia.nome as \"responsavelOcorrencia.nome\", ");
		sql.append(" responsavelFinalizacao.nome as \"responsavelFinalizacao.nome\", ");
		sql.append(" solicitanteEmprestimo.matricula as \"solicitanteEmprestimo.matricula\", ");
		sql.append(" solicitanteEmprestimo.pessoa as \"solicitanteEmprestimo.pessoa\", ");
		sql.append(" pessoaSolicitanteEmprestimo.nome as \"pessoaSolicitanteEmprestimo.nome\" ");
		sql.append(" from OcorrenciaPatrimonio ");
		sql.append(" left join patrimoniounidade on patrimoniounidade.codigo = OcorrenciaPatrimonio.patrimoniounidade ");
		sql.append(" left join patrimonio on patrimoniounidade.patrimonio = patrimonio.codigo ");
		sql.append(" left join localArmazenamento on patrimoniounidade.localArmazenamento = localArmazenamento.codigo ");
		sql.append(" left join tipopatrimonio on patrimonio.tipopatrimonio = tipopatrimonio.codigo ");
		sql.append(" left join fornecedor on patrimonio.fornecedor = fornecedor.codigo ");
		sql.append(" left join localArmazenamento as localArmazenamentosuperior on localArmazenamento.localArmazenamentoSuperior = localArmazenamentoSuperior.codigo ");
		sql.append(" left join localArmazenamento as localArmazenamentoOrigem on OcorrenciaPatrimonio.localArmazenamentoOrigem = localArmazenamentoOrigem.codigo ");
		sql.append(" left join unidadeEnsino on unidadeEnsino.codigo = localArmazenamentoOrigem.unidadeEnsino ");
		sql.append(" left join localArmazenamento as localArmazenamentoDestino on OcorrenciaPatrimonio.localArmazenamentoDestino = localArmazenamentoDestino.codigo ");
		sql.append(" left join Usuario as responsavelOcorrencia on OcorrenciaPatrimonio.responsavelOcorrencia = responsavelOcorrencia.codigo ");
		sql.append(" left join Usuario as responsavelFinalizacao on OcorrenciaPatrimonio.responsavelFinalizacao = responsavelFinalizacao.codigo ");
		sql.append(" left join Funcionario as solicitanteEmprestimo on OcorrenciaPatrimonio.solicitanteEmprestimo = solicitanteEmprestimo.codigo ");
		sql.append(" left join Pessoa as pessoaSolicitanteEmprestimo on solicitanteEmprestimo.pessoa = pessoaSolicitanteEmprestimo.codigo ");
		return sql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.patrimonio.OcorrenciaPatrimonioInterfaceFacade#
	 * consultarOcorrenciaPatrimonio(java.lang.String, java.lang.String,
	 * negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum,
	 * java.util.Date, java.util.Date,
	 * negocio.comuns.administrativo.UnidadeEnsinoVO,
	 * negocio.comuns.arquitetura.UsuarioVO, boolean, java.lang.Integer,
	 * java.lang.Integer)
	 */
	@Override
	public List<OcorrenciaPatrimonioVO> consultarOcorrenciaPatrimonio(String campoConsulta, String valorConsulta, TipoOcorrenciaPatrimonioEnum tipoOcorrenciaPatrimonio, Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado, boolean controlarAcesso, Integer limit, Integer offset, PermissaoAcessoMenuVO permissaoAcessoMenuVO) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where ");
		sql.append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "ocorrenciaPatrimonio.dataOcorrencia", true));
			if(Uteis.isAtributoPreenchido(campoConsulta)){
			if (campoConsulta.equals("CODIGO_BARRA") && !valorConsulta.trim().isEmpty()) {
				if(!valorConsulta.matches("\\d*")){
					throw new Exception(UteisJSF.internacionalizar("msg_PatrimonioUnidade_informeApenasValoresNumericos"));
				}
				sql.append(" and patrimoniounidade.codigobarra::numeric(20,0) = ").append(valorConsulta).append("::numeric(20,0) ");
			} else if (campoConsulta.equals("PATRIMONIO")) {
				sql.append(" and sem_acentos(patrimonio.descricao) ilike(sem_acentos('").append(valorConsulta).append("%')) ");
			} else if (campoConsulta.equals("LOCAL_ARMAZENAMENTO") && (tipoOcorrenciaPatrimonio == null ||(!tipoOcorrenciaPatrimonio.equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL) && !tipoOcorrenciaPatrimonio.equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE)))) {
			    sql.append(" and sem_acentos(localArmazenamento.localArmazenamento) ilike(sem_acentos('").append(valorConsulta).append("%')) ");
			}
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sql.append(" and localArmazenamentoOrigem.unidadeEnsino = ").append(unidadeEnsinoLogado.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(tipoOcorrenciaPatrimonio)) {
			sql.append(" and ocorrenciaPatrimonio.tipoOcorrenciaPatrimonio = '").append(tipoOcorrenciaPatrimonio.name()).append("' ");
		} else {
			sql.append(" and ocorrenciaPatrimonio.tipoOcorrenciaPatrimonio in (''");
			if (permissaoAcessoMenuVO.getPermitirCadastrarOcorrenciaDescarte()) {
				sql.append(", '").append(TipoOcorrenciaPatrimonioEnum.DESCARTE.name()).append("'");
			}
			if (permissaoAcessoMenuVO.getPermitirCadastrarOcorrenciaEmprestimo()) {
				sql.append(", '").append(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO.name()).append("'");
			}
			if (permissaoAcessoMenuVO.getPermitirCadastrarOcorrenciaManutencao()) {
				sql.append(", '").append(TipoOcorrenciaPatrimonioEnum.MANUTENCAO.name()).append("'");
			}
			if (permissaoAcessoMenuVO.getPermitirCadastrarOcorrenciaSeparacaoDescarte()) {
				sql.append(", '").append(TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE.name()).append("'");
			}
			if (permissaoAcessoMenuVO.getPermitirCadastrarOcorrenciaTrocaLocal()) {
				sql.append(", '").append(TipoOcorrenciaPatrimonioEnum.TROCA_LOCAL.name()).append("'");
			}
			sql.append(" ) ");
		}
		if(usuarioLogado.getVisaoLogar().equals("coordenador") || usuarioLogado.getVisaoLogar().equals("professor")){
			sql.append(" and responsavelocorrencia.codigo = ").append(usuarioLogado.getCodigo()).append(" ");
		}
		sql.append(" order by ocorrenciaPatrimonio.dataOcorrencia, patrimoniounidade.codigobarra ");
		if (limit > 0) {
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	@Override
	public List<OcorrenciaPatrimonioVO> consultarOcorrenciaPatrimonioPorUnidadePatrimonio(int codigoPatrimonioUnidade,Date dataReserva){
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where patrimoniounidade = ").append(codigoPatrimonioUnidade);
		sql.append(" and datainicioreserva::date >= '").append(UteisData.getDataJDBCTimestamp(dataReserva)).append("'");
		sql.append(" and dataterminoreserva::date <= '").append(UteisData.getDataJDBCTimestamp(dataReserva)).append("'");
		sql.append(" order by patrimoniounidade.codigoBarra ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	@Override
	public List<OcorrenciaPatrimonioVO> consultarOcorrenciaPatrimonioPorLocalReservado(int codigoLocalReservado,Date dataReserva){
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where localreservado = ").append(codigoLocalReservado);
		sql.append(" and datainicioreserva::date >= '").append(UteisData.getDataJDBCTimestamp(dataReserva)).append("'");
		sql.append(" and dataterminoreserva::date <= '").append(UteisData.getDataJDBCTimestamp(dataReserva)).append("'");
		sql.append(" order by patrimoniounidade.codigoBarra ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	private boolean validarUnidicadeLocalRequisitanteData(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO){
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select codigo from ocorrenciapatrimonio            ");
		sql.append(" where tipoocorrenciapatrimonio = 'RESERVA_LOCAL'");
		sql.append(" and (datainicioreserva BETWEEN '").append(UteisData.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataInicioReserva())).append("' and '").append(UteisData.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataTerminoReserva()));
		sql.append(" 		' or dataterminoreserva BETWEEN '").append(UteisData.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataInicioReserva())).append("' and '").append(UteisData.getDataJDBCTimestamp(ocorrenciaPatrimonioVO.getDataTerminoReserva())).append("')");
		sql.append(" and solicitanteemprestimo = ").append(ocorrenciaPatrimonioVO.getSolicitanteEmprestimo().getCodigo());
		sql.append(" limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			return false;
		}
		return true;
	}
	
	
	@Override
	public List<OcorrenciaPatrimonioVO> consultarOcorrenciaUnidadePatrimioParaGestao(int patrimonioUnidade, Date dataInicial, Date dataFinal, FuncionarioVO solicitante){
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where patrimoniounidade.codigo = ").append(patrimonioUnidade);
		sql.append(" and (datainicioreserva BETWEEN '").append(UteisData.getDataJDBCTimestamp(dataInicial)).append("' and '").append(UteisData.getDataJDBCTimestamp(dataFinal));
		sql.append(" 		' or dataterminoreserva BETWEEN '").append(UteisData.getDataJDBCTimestamp(dataInicial)).append("' and '").append(UteisData.getDataJDBCTimestamp(dataFinal)).append("')");
		if(Uteis.isAtributoPreenchido(solicitante)){
			sql.append(" and solicitanteemprestimo = ").append(solicitante.getCodigo());
		}
		sql.append(" and tipoocorrenciapatrimonio = 'RESERVA_UNIDADE'");
		sql.append(" order by patrimoniounidade.codigoBarra ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	@Override
	public List<OcorrenciaPatrimonioVO> consultarOcorrenciaLocalParaGestao(int local, Date dataInicial, Date dataFinal, FuncionarioVO solicitante){
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where localreservado = ").append(local);
		sql.append(" and (datainicioreserva BETWEEN '").append(UteisData.getDataJDBCTimestamp(dataInicial)).append("' and '").append(UteisData.getDataJDBCTimestamp(dataFinal));
		sql.append(" 		' or dataterminoreserva BETWEEN '").append(UteisData.getDataJDBCTimestamp(dataInicial)).append("' and '").append(UteisData.getDataJDBCTimestamp(dataFinal)).append("')");
		if(Uteis.isAtributoPreenchido(solicitante)){
			sql.append(" and solicitanteemprestimo = ").append(solicitante.getCodigo());
		}
		sql.append(" and tipoocorrenciapatrimonio = 'RESERVA_LOCAL'");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	private List<OcorrenciaPatrimonioVO> montarDadosConsulta(SqlRowSet rs) {
		List<OcorrenciaPatrimonioVO> ocorrenciaPatrimonioVOs = new ArrayList<OcorrenciaPatrimonioVO>(0);
		while (rs.next()) {
			ocorrenciaPatrimonioVOs.add(montarDados(rs));
		}
		return ocorrenciaPatrimonioVOs;
	}

	private OcorrenciaPatrimonioVO montarDados(SqlRowSet rs) {
		OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO = new OcorrenciaPatrimonioVO();
		ocorrenciaPatrimonioVO.setNovoObj(false);
		ocorrenciaPatrimonioVO.setCodigo(rs.getInt("codigo"));
		ocorrenciaPatrimonioVO.setDataFinalizacao(rs.getDate("dataFinalizacao"));
		ocorrenciaPatrimonioVO.setDataOcorrencia(rs.getDate("dataOcorrencia"));
		ocorrenciaPatrimonioVO.setDataPrevisaoDevolucao(rs.getDate("dataPrevisaoDevolucao"));
		ocorrenciaPatrimonioVO.setTipoOcorrenciaPatrimonio(TipoOcorrenciaPatrimonioEnum.valueOf(rs.getString("tipoOcorrenciaPatrimonio")));
		ocorrenciaPatrimonioVO.setMotivo(rs.getString("motivo"));
		ocorrenciaPatrimonioVO.setObservacao(rs.getString("observacao"));
		ocorrenciaPatrimonioVO.setDataInicioReserva(rs.getTimestamp("datainicioreserva"));
		ocorrenciaPatrimonioVO.setDataTerminoReserva(rs.getTimestamp("dataterminoreserva"));
		ocorrenciaPatrimonioVO.setSituacao(SituacaoOcorrenciaPatrimonioEnum.valueOf(rs.getString("situacao")));	
		ocorrenciaPatrimonioVO.getLocalReservado().setCodigo(rs.getInt("localreservado"));
		//DataReserva apesar para popular na tela 
		ocorrenciaPatrimonioVO.setDataReserva(ocorrenciaPatrimonioVO.getDataInicioReserva());
		if(Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getLocalReservado().getCodigo())){
			montarDadosLocalReservado(ocorrenciaPatrimonioVO);
		}
		ocorrenciaPatrimonioVO.setSituacao(SituacaoOcorrenciaPatrimonioEnum.valueOf(rs.getString("situacao")));				
		
		ocorrenciaPatrimonioVO.getResponsavelFinalizacao().setCodigo(rs.getInt("responsavelFinalizacao"));
		ocorrenciaPatrimonioVO.getResponsavelFinalizacao().setNome(rs.getString("responsavelFinalizacao.nome"));
		
		ocorrenciaPatrimonioVO.getResponsavelOcorrencia().setCodigo(rs.getInt("responsavelOcorrencia"));
		ocorrenciaPatrimonioVO.getResponsavelOcorrencia().setNome(rs.getString("responsavelOcorrencia.nome"));
		
		ocorrenciaPatrimonioVO.getSolicitanteEmprestimo().setCodigo(rs.getInt("solicitanteEmprestimo"));
		ocorrenciaPatrimonioVO.getSolicitanteEmprestimo().setMatricula(rs.getString("solicitanteEmprestimo.matricula"));
		ocorrenciaPatrimonioVO.getSolicitanteEmprestimo().getPessoa().setCodigo(rs.getInt("solicitanteEmprestimo.pessoa"));
		ocorrenciaPatrimonioVO.getSolicitanteEmprestimo().getPessoa().setNome(rs.getString("pessoaSolicitanteEmprestimo.nome"));
		
		ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().setCodigo(rs.getInt("localArmazenamentoDestino"));
		ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().setLocalArmazenamento(rs.getString("localArmazenamentoDestino.localArmazenamento"));
		
		ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().setCodigo(rs.getInt("localArmazenamentoOrigem"));
		ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().setLocalArmazenamento(rs.getString("localArmazenamentoOrigem.localArmazenamento"));
		ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().getUnidadeEnsinoVO().setCodigo(rs.getInt("localArmazenamentoOrigem.unidadeEnsino"));
		ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().getUnidadeEnsinoVO().setNome(rs.getString("unidadeEnsino.nome"));
		
		ocorrenciaPatrimonioVO.getPatrimonioUnidade().getLocalArmazenamento().setCodigo(rs.getInt("patrimonioUnidade.localArmazenamento"));
		ocorrenciaPatrimonioVO.getPatrimonioUnidade().getLocalArmazenamento().setLocalArmazenamento(rs.getString("localArmazenamento.localArmazenamento"));
		ocorrenciaPatrimonioVO.getPatrimonioUnidade().getLocalArmazenamento().getLocalArmazenamentoSuperiorVO().setLocalArmazenamento(rs.getString("localArmazenamentosuperior.localArmazenamento"));
		
		ocorrenciaPatrimonioVO.getPatrimonioUnidade().setCodigo(rs.getInt("patrimonioUnidade"));
		ocorrenciaPatrimonioVO.getPatrimonioUnidade().setCodigo(rs.getInt("patrimonioUnidade"));
		if(Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getPatrimonioUnidade().getCodigo())){
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().setCodigoBarra(rs.getString("patrimonioUnidade.codigoBarra"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().setNumeroDeSerie(rs.getString("patrimonioUnidade.numeroDeSerie"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().setValorRecurso(rs.getBigDecimal("patrimonioUnidade.valorRecurso"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().setPermiteReserva(rs.getBoolean("patrimonioUnidade.permiteReserva"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().setUnidadeLocado(rs.getBoolean("patrimonioUnidade.unidadeLocado"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().setSituacaoPatrimonioUnidade(SituacaoPatrimonioUnidadeEnum.valueOf(rs.getString("patrimonioUnidade.situacao")));
			
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().getPatrimonioVO().setCodigo(rs.getInt("patrimonioUnidade.patrimonio"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().getPatrimonioVO().setDescricao(rs.getString("patrimonio.descricao"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().getPatrimonioVO().setMarca(rs.getString("patrimonio.marca"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().getPatrimonioVO().setModelo(rs.getString("patrimonio.modelo"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().getPatrimonioVO().setNotaFiscal(rs.getString("patrimonio.notafiscal"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().getPatrimonioVO().getTipoPatrimonioVO().setDescricao(rs.getString("tipoPatrimonio.descricao"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().getPatrimonioVO().getFornecedorVO().setNome(rs.getString("fornecedor.nome"));
			ocorrenciaPatrimonioVO.getPatrimonioUnidade().getPatrimonioVO().setDataEntrada(rs.getDate("patrimonio.dataentrada"));
			ocorrenciaPatrimonioVO.getTipoPatrimonioVO().setCodigo(rs.getInt("tipoPatrimonio.codigo"));
			ocorrenciaPatrimonioVO.getTipoPatrimonioVO().setDescricao(rs.getString("tipopatrimonio.descricao"));
			
		}
		
		return ocorrenciaPatrimonioVO;
	}

	private void montarDadosLocalReservado(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO) {
		try {
			ocorrenciaPatrimonioVO.setLocalReservado(getFacadeFactory().getLocalArmazenamentoFacade().consultarPorChavePrimaria(ocorrenciaPatrimonioVO.getLocalReservado().getCodigo(), false, null).get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.patrimonio.OcorrenciaPatrimonioInterfaceFacade#
	 * consultarTotalOcorrenciaPatrimonio(java.lang.String, java.lang.String,
	 * negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum,
	 * java.util.Date, java.util.Date,
	 * negocio.comuns.administrativo.UnidadeEnsinoVO,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public Integer consultarTotalOcorrenciaPatrimonio(String campoConsulta, String valorConsulta, TipoOcorrenciaPatrimonioEnum tipoOcorrenciaPatrimonio, Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado, PermissaoAcessoMenuVO permissaoAcessoMenuVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(OcorrenciaPatrimonio.codigo) as qtde ");
		sql.append(" from OcorrenciaPatrimonio ");
		sql.append(" inner join patrimoniounidade on patrimoniounidade.codigo = OcorrenciaPatrimonio.patrimoniounidade ");
		sql.append(" inner join patrimonio on patrimoniounidade.patrimonio = patrimonio.codigo ");
		sql.append(" inner join localArmazenamento on patrimoniounidade.localArmazenamento = localArmazenamento.codigo ");
		sql.append(" left join localArmazenamento as localArmazenamentoOrigem on OcorrenciaPatrimonio.localArmazenamentoOrigem = localArmazenamentoOrigem.codigo ");
		sql.append(" left join localArmazenamento as localArmazenamentoDestino on OcorrenciaPatrimonio.localArmazenamentoDestino = localArmazenamentoDestino.codigo ");
		sql.append(" left join Usuario as responsavelOcorrencia on OcorrenciaPatrimonio.responsavelOcorrencia = responsavelOcorrencia.codigo ");
		sql.append(" left join Usuario as responsavelFinalizacao on OcorrenciaPatrimonio.responsavelFinalizacao = responsavelFinalizacao.codigo ");
		sql.append(" left join Funcionario as solicitanteEmprestimo on OcorrenciaPatrimonio.solicitanteEmprestimo = solicitanteEmprestimo.codigo ");
		sql.append(" left join Pessoa as pessoaSolicitanteEmprestimo on solicitanteEmprestimo.pessoa = pessoaSolicitanteEmprestimo.codigo ");
		sql.append(" where ");
		sql.append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "dataOcorrencia", true));
		if(Uteis.isAtributoPreenchido(campoConsulta)){
			if (campoConsulta.equals("CODIGO_BARRA") && !valorConsulta.trim().isEmpty()) {
				if(!valorConsulta.matches("\\d*")){
					throw new Exception(UteisJSF.internacionalizar("msg_PatrimonioUnidade_informeApenasValoresNumericos"));
				}
				sql.append(" and patrimoniounidade.codigobarra::numeric(20,0) = ").append(valorConsulta).append("::numeric(20,0) ");
			} else if (campoConsulta.equals("PATRIMONIO")) {
				sql.append(" and sem_acentos(patrimonio.descricao) ilike(sem_acentos('").append(valorConsulta).append("%')) ");
			} else if (campoConsulta.equals("LOCAL_ARMAZENAMENTO")) {
				sql.append(" and sem_acentos(localArmazenamento.localArmazenamento) ilike(sem_acentos('").append(valorConsulta).append("%')) ");
			}
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sql.append(" and localArmazenamentoOrigem.unidadeEnsino = ").append(unidadeEnsinoLogado.getCodigo());
		}
		if(usuarioLogado.getVisaoLogar().equals("coordenador") || usuarioLogado.getVisaoLogar().equals("professor")){
			sql.append(" and responsavelocorrencia.codigo = ").append(usuarioLogado.getCodigo()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(tipoOcorrenciaPatrimonio)) {
			sql.append(" and ocorrenciaPatrimonio.tipoOcorrenciaPatrimonio = '").append(tipoOcorrenciaPatrimonio.name()).append("' ");
		} else {
			sql.append(" and ocorrenciaPatrimonio.tipoOcorrenciaPatrimonio in (''");
			if (permissaoAcessoMenuVO.getPermitirCadastrarOcorrenciaDescarte()) {
				sql.append(", '").append(TipoOcorrenciaPatrimonioEnum.DESCARTE.name()).append("'");
			}
			if (permissaoAcessoMenuVO.getPermitirCadastrarOcorrenciaEmprestimo()) {
				sql.append(", '").append(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO.name()).append("'");
			}
			if (permissaoAcessoMenuVO.getPermitirCadastrarOcorrenciaManutencao()) {
				sql.append(", '").append(TipoOcorrenciaPatrimonioEnum.MANUTENCAO.name()).append("'");
			}
			if (permissaoAcessoMenuVO.getPermitirCadastrarOcorrenciaSeparacaoDescarte()) {
				sql.append(", '").append(TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE.name()).append("'");
			}
			if (permissaoAcessoMenuVO.getPermitirCadastrarOcorrenciaTrocaLocal()) {
				sql.append(", '").append(TipoOcorrenciaPatrimonioEnum.TROCA_LOCAL.name()).append("'");
			}
			sql.append(" ) ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	public Integer consultarTotalOcorrenciaPatrimonioPorDataETipoPatrimonio(OcorrenciaPatrimonioVO obj) throws Exception {
		StringBuilder sql = new StringBuilder(" select count (ocorrenciapatrimonio.codigo) as qtde ");
		
		sql.append(" from ocorrenciapatrimonio ");
		sql.append(" inner join patrimoniounidade on patrimoniounidade.codigo = ocorrenciapatrimonio.patrimoniounidade  ");
		sql.append(" inner join patrimonio on patrimonio.codigo = patrimoniounidade.patrimonio ");
		sql.append(" inner join tipopatrimonio on tipopatrimonio.codigo = patrimonio.tipopatrimonio ");
		sql.append(" where (ocorrenciapatrimonio.datainicioreserva >= '").append(UteisData.getDataJDBCTimestamp(obj.getDataInicioReserva())).append("' ");
		sql.append("    and ocorrenciapatrimonio.dataterminoreserva <= '").append(UteisData.getDataJDBCTimestamp(obj.getDataTerminoReserva())).append("') ");;
		sql.append(" and tipopatrimonio.codigo = ").append(obj.getTipoPatrimonioVO().getCodigo());
		sql.append(" and ocorrenciapatrimonio.solicitanteemprestimo = ").append(obj.getSolicitanteEmprestimo().getCodigo());
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {                                                             
			return rs.getInt("qtde");                                                
		}                                                                            
		return 0;                                                                    
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.patrimonio.OcorrenciaPatrimonioInterfaceFacade#
	 * consultarPorChavePrimaria(java.lang.Integer, boolean,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public OcorrenciaPatrimonioVO consultarPorChavePrimaria(Integer codigo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" where ocorrenciaPatrimonio.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs);
		}
		throw new Exception(UteisJSF.internacionalizar("msg_OcorrenciaPatrimonio_dadosNaoEncontrados"));
	}

	@Override
	public void inicializarDadosPatrimonioUnidade(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, PatrimonioUnidadeVO patrimonioUnidadeVO, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDadosPatrimonioAptoTipoOcorrencia(ocorrenciaPatrimonioVO, patrimonioUnidadeVO, usuarioVO);
			ocorrenciaPatrimonioVO.setPatrimonioUnidade(patrimonioUnidadeVO);
			ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().setCodigo(patrimonioUnidadeVO.getLocalArmazenamento().getCodigo());
			ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().setLocalArmazenamento(patrimonioUnidadeVO.getLocalArmazenamento().getLocalArmazenamento());
			ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().getUnidadeEnsinoVO().setCodigo(patrimonioUnidadeVO.getLocalArmazenamento().getUnidadeEnsinoVO().getCodigo());
			ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().getUnidadeEnsinoVO().setNome(patrimonioUnidadeVO.getLocalArmazenamento().getUnidadeEnsinoVO().getNome());
			if (ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.DESCARTE) || ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE)) {
				ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().setCodigo(patrimonioUnidadeVO.getLocalArmazenamento().getCodigo());
				ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().setLocalArmazenamento(patrimonioUnidadeVO.getLocalArmazenamento().getLocalArmazenamento());
				ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().getUnidadeEnsinoVO().setCodigo(patrimonioUnidadeVO.getLocalArmazenamento().getUnidadeEnsinoVO().getCodigo());
				ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().getUnidadeEnsinoVO().setNome(patrimonioUnidadeVO.getLocalArmazenamento().getUnidadeEnsinoVO().getNome());
			}
		} catch (Exception e) {
			ocorrenciaPatrimonioVO.setPatrimonioUnidade(null);
			ocorrenciaPatrimonioVO.setLocalArmazenamentoDestino(null);
			ocorrenciaPatrimonioVO.setLocalArmazenamentoOrigem(null);
			throw e;
		}
	}

	/**
	 * @return the idEntidade
	 */
	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "OcorrenciaPatrimonio";
		}
		return idEntidade;
	}

	/**
	 * @param idEntidade
	 *            the idEntidade to set
	 */
	public static void setIdEntidade(String idEntidade) {
		OcorrenciaPatrimonio.idEntidade = idEntidade;
	}

	 @Override
	    public String designOcorrenciaReservaRelatorio() {
	        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + "OcorrenciaPatrimonioReservaRel" + ".jrxml");
	    }
	 @Override
	    public String caminhoRelatorio() {
	        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator);
	    }
}
