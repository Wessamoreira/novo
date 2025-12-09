package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.AssuntoDebugEnum;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoContratoAgenteNegativacaoCobrancaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroNegativacaoCobrancaContaReceberItemInterfaceFacade;

@Service
@Lazy
public class RegistroNegativacaoCobrancaContaReceberItem extends ControleAcesso implements RegistroNegativacaoCobrancaContaReceberItemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8694194051684225588L;


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RegistroNegativacaoCobrancaContaReceberItemVO obj, final UsuarioVO usuario) throws Exception {
		try {
			incluir(obj, "RegistroNegativacaoCobrancaContaReceberItem", new AtributoPersistencia()
					.add("contaReceber", obj.getContaReceber()) 
					.add("unidadeEnsino", obj.getUnidadeEnsino()) 
					.add("unidadeEnsinoCnpj", obj.getUnidadeEnsinoCnpj()) 
					.add("curso", obj.getCurso())
					.add("codigoCurso", obj.getCodigoCurso())
					.add("aluno", obj.getAluno())
					.add("alunoBairro", obj.getAlunoBairro())
					.add("alunoCep", obj.getAlunoCep())
					.add("alunoCpf", obj.getAlunoCpf())
					.add("alunoDataNascimento", obj.getAlunoDataNascimento())
					.add("alunoEndereco", obj.getAlunoEndereco())
					.add("alunoMunicipio", obj.getAlunoMunicipio())
					.add("alunoNumero", obj.getAlunoNumero())
					.add("alunoUf", obj.getAlunoUf())
					.add("matricula", obj.getMatricula())
					.add("matriculaPeriodo", obj.getMatriculaPeriodo())
					.add("parcela", obj.getParcela())
					.add("datavencimento", obj.getDataVencimento())
					.add("valor", obj.getValor())
					.add("codigoUsuario", usuario.getCodigo())
					.add("nomeUsuario", usuario.getNome())
					.add("registroNegativacaoCobrancaContaReceber", obj.getRegistroNegativacaoCobrancaContaReceber())
					.add("dataRegistro", obj.getDataRegistro())
					.add("situacao", obj.getSituacao())
					.add("jsonIntegracao", obj.getJsonIntegracao())
					.add("jsonIntegracaoExclusao", obj.getJsonIntegracaoExclusao())
					.add("codigoIntegracaoApi", obj.getCodigoIntegracaoApi())					
					.add("motivo", obj.getMotivo())					
					.add("dataExclusao", obj.getDataExclusao())					
					.add("nossonumero", obj.getNossoNumero())
					.add("tipoContratoAgenteNegativacaoCobrancaEnum", obj.getTipoContratoAgenteNegativacaoCobrancaEnum())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		
		} catch (Exception e) {
			obj.setNovoObj(true);
			obj.setCodigo(0);
			if (e.getMessage() != null && e.getMessage().contains("check_registronegativacaocobrancacontareceberitem_contareceber_")) {
				throw new ConsistirException("Verificamos que a CONTA A RECEBER de NOSSO NÚMERO: " + obj.getNossoNumero() + " foi negativada por outro usuário, remova esta conta para dar continuidade ao cadastro.");
			}
			throw e;
		}

	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarExclusao(final RegistroNegativacaoCobrancaContaReceberItemVO obj) throws Exception {
		try {
			final String sql = "UPDATE RegistroNegativacaoCobrancaContaReceberItem set codigoUsuarioExclusao=?, nomeUsuarioExclusao=?, dataExclusao=?, motivo=?, codigoIntegracaoApi=?, jsonIntegracaoExclusao=?  WHERE ((codigo = ?)) --ul:"+obj.getCodigoUsuarioExclusao();
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if(Uteis.isAtributoPreenchido(obj.getCodigoUsuarioExclusao())) {
						sqlAlterar.setInt(1, obj.getCodigoUsuarioExclusao());
					}else {
						sqlAlterar.setNull(1, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getNomeUsuarioExclusao())) {
						sqlAlterar.setString(2, obj.getNomeUsuarioExclusao());
					}else {
						sqlAlterar.setNull(2, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataExclusao())) {
						sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getDataExclusao()));	
					}else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setString(4, obj.getMotivo());
					if(Uteis.isAtributoPreenchido(obj.getCodigoIntegracaoApi())) {
						sqlAlterar.setString(5, obj.getCodigoIntegracaoApi());
					}else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setString(6, obj.getJsonIntegracaoExclusao());
					sqlAlterar.setInt(7, obj.getCodigo());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirListaVOs(List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs, RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceberVO, UsuarioVO usuarioLogado) throws Exception {
		for (RegistroNegativacaoCobrancaContaReceberItemVO registroNegativacaoCobrancaContaReceberItemVO : registroNegativacaoCobrancaContaReceberItemVOs) {
			registroNegativacaoCobrancaContaReceberItemVO.setRegistroNegativacaoCobrancaContaReceber(registroNegativacaoCobrancaContaReceberVO.getCodigo());
			incluir(registroNegativacaoCobrancaContaReceberItemVO, usuarioLogado);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirNegativacaoCobrancaListagemVOs(AgenteNegativacaoCobrancaContaReceberVO anccr, List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs, String motivo, boolean removerNegativacaoContaReceberViaIntegracao, boolean verificarAcesso, ConfiguracaoGeralSistemaVO config, UsuarioVO responsavel) throws Exception {
		excluir("MapaNegativacaoCobrancaContaReceber", verificarAcesso, responsavel);
		for (RegistroNegativacaoCobrancaContaReceberItemVO registroNegativacaoCobrancaContaReceberItemVO : registroNegativacaoCobrancaContaReceberItemVOs) {
			if (registroNegativacaoCobrancaContaReceberItemVO.getSelecionado()) {
				try {
					registroNegativacaoCobrancaContaReceberItemVO.setMotivo(motivo);
					registroNegativacaoCobrancaContaReceberItemVO.setCodigoUsuarioExclusao(responsavel.getCodigo());
					registroNegativacaoCobrancaContaReceberItemVO.setNomeUsuarioExclusao(responsavel.getNome());
					registroNegativacaoCobrancaContaReceberItemVO.setDataExclusao(new Date());
					if(removerNegativacaoContaReceberViaIntegracao) {
						getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().executarEnvioIntegracaoWebService(anccr, registroNegativacaoCobrancaContaReceberItemVO, JobsEnum.JOB_SERASA_API_GEO_REMOVER, config, responsavel);	
					}
				} catch (Exception e) {
					registroNegativacaoCobrancaContaReceberItemVO.setCodigoUsuarioExclusao(null);
					registroNegativacaoCobrancaContaReceberItemVO.setNomeUsuarioExclusao(null);
					registroNegativacaoCobrancaContaReceberItemVO.setDataExclusao(null);
				}
				alterarExclusao(registroNegativacaoCobrancaContaReceberItemVO);
			}
		}
		
	}

	private List<RegistroNegativacaoCobrancaContaReceberItemVO> montarDadosConsultaCompleta(SqlRowSet rs) {
		List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs = new ArrayList<RegistroNegativacaoCobrancaContaReceberItemVO>(0);
		while (rs.next()) {
			registroNegativacaoCobrancaContaReceberItemVOs.add(montarDadosCompleta(rs));
		}
		return registroNegativacaoCobrancaContaReceberItemVOs;
	}

	

	private RegistroNegativacaoCobrancaContaReceberItemVO montarDadosCompleta(SqlRowSet rs) {
		RegistroNegativacaoCobrancaContaReceberItemVO obj = new RegistroNegativacaoCobrancaContaReceberItemVO();
		obj.setCodigo(rs.getInt("codigo"));
		obj.setContaReceber(rs.getInt("contaReceber"));
		obj.setUnidadeEnsino(rs.getString("unidadeEnsino"));
		obj.setUnidadeEnsinoCnpj(rs.getString("unidadeEnsinoCnpj"));
		obj.setCurso(rs.getString("curso"));
		obj.setAluno(rs.getString("aluno"));
		obj.setAlunoCpf(rs.getString("alunoCpf"));
		obj.setCodigoCurso(rs.getInt("codigoCurso"));
		obj.setMatricula(rs.getString("matricula"));
		obj.setMatriculaPeriodo(rs.getInt("matriculaPeriodo"));
		obj.setParcela(rs.getString("parcela"));
		obj.setDataVencimento(rs.getDate("dataVencimento"));	
		obj.setValor(rs.getDouble("valor"));	
		obj.setRegistroNegativacaoCobrancaContaReceber(rs.getInt("registroNegativacaoCobrancaContaReceber"));			
		obj.setCodigoUsuario(rs.getInt("codigoUsuario"));			
		obj.setNomeUsuario(rs.getString("nomeUsuario"));			
		obj.setDataRegistro(rs.getDate("dataRegistro"));			
		obj.setSituacao(rs.getString("situacao"));			
		obj.setNossoNumero(rs.getString("nossoNumero"));
		obj.setSituacaoContaReceber(rs.getString("situacaoContaReceber"));
		obj.setCodigoNegociacaoContaReceber(rs.getInt("negociacaocontareceber"));
		obj.setCodigoUsuarioExclusao(rs.getInt("codigoUsuarioExclusao"));			
		obj.setNomeUsuarioExclusao(rs.getString("nomeUsuarioExclusao"));			
		obj.setDataExclusao(rs.getDate("dataExclusao"));			
		obj.setAgente(rs.getString("agente"));
		obj.setCodigoAgente(rs.getInt("codigoAgente"));
		obj.setBloqueio(rs.getBoolean("bloqueio"));
		obj.setMotivo(rs.getString("motivo"));
		obj.setParceiro(rs.getString("parceiro"));
		obj.setCodigoIntegracaoApi(rs.getString("codigoIntegracaoApi"));
		obj.setTipoContratoAgenteNegativacaoCobrancaEnum(TipoContratoAgenteNegativacaoCobrancaEnum.valueOf(rs.getString("tipoContratoAgenteNegativacaoCobrancaEnum")));
		obj.setNovoObj(false);
		return obj;
	}

	public List<RegistroNegativacaoCobrancaContaReceberItemVO> consultarRegistroNegativacaoCobrancaContaReceberItem(
			AgenteNegativacaoCobrancaContaReceberVO agente, String situacaoContaReceber, 
			TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgenteNegativacaoCobrancaContaReceberEnum,
			Date dataInicio, Date dataFinal, String situacaoParcelaNegociada, String consultarPor,
			List<UnidadeEnsinoVO> unidadeEnsinoVOs, String tipoPessoa, MatriculaVO matriculaAluno, 
			ParceiroVO parceiro, FuncionarioVO funcionario, PessoaVO responsavelFinanceiro,
			String situacaoRegistro, FornecedorVO fornecedorVO) {
		StringBuilder sql = new StringBuilder("select distinct ");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.codigo, ");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.contaReceber,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.unidadeEnsino,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.unidadeEnsinoCnpj,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.curso,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.aluno,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.alunocpf,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.codigoCurso,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.matricula,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.matriculaPeriodo,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.parcela,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.dataVencimento,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.valor,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.registroNegativacaoCobrancaContaReceber,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.codigoUsuario,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.nomeUsuario,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.dataRegistro,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.situacao,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.nossoNumero,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.motivo,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.codigoUsuarioExclusao,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.nomeUsuarioExclusao,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.dataExclusao,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.codigoIntegracaoApi,");
		sql.append(" RegistroNegativacaoCobrancaContaReceberItem.tipoContratoAgenteNegativacaoCobrancaEnum,");
		sql.append(" contareceber.situacao as situacaocontareceber, ");
	    sql.append(" negociacaocontareceber.codigo as negociacaocontareceber, ");
	    sql.append(" agentenegativacaocobrancacontareceber.nome as agente,   ");
	    sql.append(" agentenegativacaocobrancacontareceber.codigo as codigoAgente,   ");
	    sql.append(" parceiro.nome as parceiro,  ");
	    
		sql.append(" (exists(select contanegociada.codigo from contareceber as contanegociada ");
		sql.append("   where negociacaocontareceber.codigo is not null   ");
		sql.append("   and contanegociada.tipoorigem = 'NCR' and contanegociada.situacao = 'AR'   ");
		sql.append("   and contanegociada.datavencimento < current_date  ");
		sql.append("   and  contanegociada.codorigem = cast(negociacaocontareceber.codigo as varchar) ");
		sql.append("   limit 1)) as bloqueio, ");
		
		if(situacaoRegistro == null || situacaoRegistro.equals("") || situacaoRegistro.equals("REGISTRADO")) {
			sql.append(" false as permiteestornar ");
		}else {			
			sql.append(" (not exists (select rni.codigo from RegistroNegativacaoCobrancaContaReceberItem rni inner join RegistroNegativacaoCobrancaContaReceber rn on rn.codigo = rni.RegistroNegativacaoCobrancaContaReceber  ");
			sql.append(" and rni.contaReceber = RegistroNegativacaoCobrancaContaReceberItem.contaReceber ");			
			sql.append(" and rni.dataExclusao is null ");
			sql.append(" and rni.codigo != RegistroNegativacaoCobrancaContaReceberItem.codigo ");		
			sql.append(" and rn.tipoAgente = '").append(tipoAgenteNegativacaoCobrancaContaReceberEnum.name()).append("' ");
			if(tipoAgenteNegativacaoCobrancaContaReceberEnum.equals(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO)) {
				sql.append(" and rn.agente =  ").append(agente.getCodigo()).append(" ");
			}
			sql.append(" )) ");			
			sql.append(" as permiteestornar ");
		}
	
	
		
		sql.append(" from registronegativacaocobrancacontareceber ");
		sql.append(" inner join agentenegativacaocobrancacontareceber on agentenegativacaocobrancacontareceber.codigo = registroNegativacaoCobrancaContaReceber.agente");
		sql.append(" inner join RegistroNegativacaoCobrancaContaReceberItem on registronegativacaocobrancacontareceber.codigo = RegistroNegativacaoCobrancaContaReceberItem.RegistroNegativacaoCobrancaContaReceber ");
		sql.append(" inner join contareceber on contareceber.codigo = registroNegativacaoCobrancaContaReceberItem.contareceber   ");
		sql.append(" left join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo   ");
		sql.append(" left join negociacaocontareceber on negociacaocontareceber.codigo = contarecebernegociado.negociacaocontareceber ");
		sql.append(" left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber =  contareceber.codigo ");
		sql.append(" left join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento =  negociacaorecebimento.codigo ");
		sql.append(" LEFT JOIN parceiro ON (parceiro.codigo = contareceber.parceiro) ");
		if (!tipoPessoa.equals("")) {
			if (tipoPessoa.equals("RF")) {
				sql.append(" LEFT JOIN pessoa as responsavelFinanceiro ON (contareceber.responsavelFinanceiro = responsavelFinanceiro.codigo) "); 
			}
			if (tipoPessoa.equals("FU")) {
				sql.append(" LEFT JOIN funcionario ON (funcionario.codigo = contareceber.funcionario) ");
				sql.append(" LEFT JOIN pessoa AS pessoafuncionario ON (pessoafuncionario.codigo = funcionario.pessoa) "); 
			}
			if (tipoPessoa.equals("FO")) {
				sql.append("LEFT JOIN fornecedor ON (fornecedor.codigo = contareceber.fornecedor) ");
			}
		}
		
		sql.append(" where registronegativacaocobrancacontareceber.agente = ").append(agente.getCodigo()).append(" ");
		sql.append(" and registronegativacaocobrancacontareceber.tipoagente = '").append(tipoAgenteNegativacaoCobrancaContaReceberEnum.name()).append("' ");
		if (!unidadeEnsinoVOs.isEmpty()) {
			sql.append(" and contareceber.unidadeensino in (");
			for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sql.append(ue.getCodigo() + ", ");
				}
			}
			sql.append("0) ");
		}
		if (!tipoPessoa.equals("")) {
			if (tipoPessoa.equals("AL")) {
				sql.append(" and contareceber.tipoPessoa = 'AL'");
				if (Uteis.isAtributoPreenchido(matriculaAluno.getMatricula())) {
					sql.append(" and contareceber.matriculaaluno = '").append(matriculaAluno.getMatricula()).append("' ");									
				}
			}
			if (tipoPessoa.equals("FO")) {
				sql.append(" and contareceber.tipoPessoa = 'FO'");
				if (Uteis.isAtributoPreenchido(fornecedorVO.getCodigo())) {
					sql.append(" and fornecedor.codigo = ").append(fornecedorVO.getCodigo());					
				}
			}
			if (tipoPessoa.equals("RF")) {
				sql.append(" and contareceber.tipoPessoa = 'RF'");
				if (Uteis.isAtributoPreenchido(responsavelFinanceiro.getCodigo())) {
					sql.append(" and responsavelfinanceiro.codigo = ").append(responsavelFinanceiro.getCodigo());									
				}
			}
			if (tipoPessoa.equals("FU")) {
				sql.append(" and contareceber.tipoPessoa = 'FU'");
				if (Uteis.isAtributoPreenchido(funcionario.getCodigo())) {
					sql.append(" and pessoafuncionario.codigo = ").append(funcionario.getCodigo());									
				}
			}
			if (tipoPessoa.equals("PA")) {
				sql.append(" and contareceber.tipoPessoa = 'PA'");
				if (Uteis.isAtributoPreenchido(parceiro.getCodigo())) {
					sql.append(" and parceiro.codigo = ").append(parceiro.getCodigo());									
				}
			}
		}
		if (!situacaoContaReceber.equals("TO")) {
			if (situacaoContaReceber.equals("RC")) {
				sql.append(" and contareceber.situacao in ('RE', 'NE') ");
			} else {
				sql.append(" and contareceber.situacao = '").append(situacaoContaReceber).append("' ");
			}
		}
		if(situacaoRegistro == null || situacaoRegistro.equals("") || situacaoRegistro.equals("REGISTRADO")) {
			sql.append(" and RegistroNegativacaoCobrancaContaReceberitem.dataexclusao is null ");		
		}else {
			sql.append(" and RegistroNegativacaoCobrancaContaReceberitem.dataexclusao is not null ");
		}
		if (consultarPor.equals("dataNegativacao")) {
			sql.append(" AND ((RegistroNegativacaoCobrancaContaReceberItem.dataregistro >='").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' and RegistroNegativacaoCobrancaContaReceberItem.dataregistro <= '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("')").append(")");
		} else if (consultarPor.equals("dataVencimento")) {
			sql.append(" AND ((contareceber.datavencimento >='").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' and contareceber.datavencimento <= '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("')").append(")");
		} else if (consultarPor.equals("dataExclusao")) {
			sql.append(" AND ((RegistroNegativacaoCobrancaContaReceberitem.dataexclusao >='").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' and RegistroNegativacaoCobrancaContaReceberitem.dataexclusao <= '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("')").append(")");
		} else if(consultarPor.equals("dataRecebNeg")){
			sql.append(" AND (( negociacaocontareceber.codigo is not null and negociacaocontareceber.data >='").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' and negociacaocontareceber.data <= '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("')");
			sql.append(" or (negociacaorecebimento.codigo is not null and negociacaorecebimento.data >='").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' and negociacaorecebimento.data <= '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("')");
			sql.append(" or ( negociacaocontareceber.codigo is not null and exists (select negociacaocontareceber.codigo from contareceber as cr");
			sql.append("   inner join contarecebernegociacaorecebimento ccnr on ccnr.contareceber = cr.codigo");
			sql.append("   inner join negociacaorecebimento nr on nr.codigo = ccnr.negociacaorecebimento");
			sql.append("   where cr.tipoorigem = 'NCR' and cr.situacao = 'RE'");
			sql.append("   and cr.codorigem = cast(negociacaocontareceber.codigo as varchar) ");
			sql.append("   and  nr.data >='").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio)).append("' ");
			sql.append("   and nr.data <= '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataFinal)).append("' limit 1) )");		
			sql.append(" ) ");
		}
		if (situacaoContaReceber.equals("RC") || situacaoContaReceber.equals("NE") || situacaoContaReceber.equals("TR")) {
			if (situacaoParcelaNegociada.equals("TR")) {
				sql.append(" and ( (contareceber.situacao = 'NE' and fn_verificarContemParcelaNegociadaRecebida(contareceber.codigo, true)) or contareceber.situacao != 'NE') ");
			}
			if (situacaoParcelaNegociada.equals("PR")) {
				sql.append(" and ( (contareceber.situacao = 'NE' and fn_verificarContemParcelaNegociadaRecebida(contareceber.codigo, false)) or contareceber.situacao != 'NE') ");
			}
			if (situacaoParcelaNegociada.equals("NR")) {
				sql.append(" and ( (contareceber.situacao = 'NE' and fn_verificarContemParcelaNegociadaRecebida(contareceber.codigo, false) = false) or contareceber.situacao != 'NE')");
			}
		}		
		sql.append(" order by RegistroNegativacaoCobrancaContaReceberItem.aluno, RegistroNegativacaoCobrancaContaReceberItem.matricula, RegistroNegativacaoCobrancaContaReceberItem.parcela ");		
		List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs = new ArrayList<RegistroNegativacaoCobrancaContaReceberItemVO>(0);
		SqlRowSet rs =getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			RegistroNegativacaoCobrancaContaReceberItemVO registroNegativacaoCobrancaContaReceberItemVO = montarDadosCompleta(rs);
			registroNegativacaoCobrancaContaReceberItemVO.setPermiteEstornar(rs.getBoolean("permiteEstornar"));
			registroNegativacaoCobrancaContaReceberItemVOs.add(registroNegativacaoCobrancaContaReceberItemVO);
		}
		return registroNegativacaoCobrancaContaReceberItemVOs;		
	}

	public List<RegistroNegativacaoCobrancaContaReceberItemVO> carregarHistoricoNegativacaoContaReceber(Integer contareceber) {
		StringBuilder sql = new StringBuilder("Select RegistroNegativacaoCobrancaContaReceberItem.*, contareceber.situacao as situacaocontareceber, contarecebernegociado.negociacaocontareceber, agentenegativacaocobrancacontareceber.nome as agente, agentenegativacaocobrancacontareceber.codigo as  codigoAgente, (false) as bloqueio, parceiro.nome as parceiro from RegistroNegativacaoCobrancaContaReceberItem ");
		sql.append(" inner join registronegativacaocobrancacontareceber on registronegativacaocobrancacontareceber.codigo = RegistroNegativacaoCobrancaContaReceberItem.RegistroNegativacaoCobrancaContaReceber ");
		sql.append(" inner join agentenegativacaocobrancacontareceber on agentenegativacaocobrancacontareceber.codigo = registroNegativacaoCobrancaContaReceber.agente");
		sql.append(" inner join contareceber on contareceber.codigo = registroNegativacaoCobrancaContaReceberItem.contareceber ");
		sql.append(" left join contarecebernegociado on contarecebernegociado.contareceber = contareceber.codigo ");
		sql.append(" left join parceiro on parceiro.codigo = contareceber.parceiro ");
		sql.append(" where contareceber.codigo = ").append(contareceber).append(" ");
		sql.append(" and RegistroNegativacaoCobrancaContaReceberItem.dataexclusao is null ");
		sql.append(" order by RegistroNegativacaoCobrancaContaReceberItem.dataRegistro desc ");
		return montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	public Boolean verificarMatriculaPossuiNegativacaoCobranca(Integer pessoa) {
		StringBuilder sql = new StringBuilder("select re.codigo from registronegativacaocobrancacontareceberitem  re ");
		sql.append(" inner join contareceber on contareceber.codigo = re.contareceber ");
		sql.append(" where contareceber.pessoa = ").append(pessoa).append(" and dataexclusao is null limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void estornarNegativacaoCobrancaListagemVOs(List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs, 
			AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgenteNegativacaoCobrancaContaReceberEnum, UsuarioVO responsavel) throws Exception {
		alterar("MapaNegativacaoCobrancaContaReceber", true, responsavel);
		for (RegistroNegativacaoCobrancaContaReceberItemVO registroNegativacaoCobrancaContaReceberItemVO : registroNegativacaoCobrancaContaReceberItemVOs) {
			if (registroNegativacaoCobrancaContaReceberItemVO.getSelecionado()) {
				registroNegativacaoCobrancaContaReceberItemVO.setMotivo("");
				registroNegativacaoCobrancaContaReceberItemVO.setCodigoUsuarioExclusao(0);
				registroNegativacaoCobrancaContaReceberItemVO.setNomeUsuarioExclusao("");
				registroNegativacaoCobrancaContaReceberItemVO.setDataExclusao(null);
				estornarExclusao(registroNegativacaoCobrancaContaReceberItemVO, agenteNegativacaoCobrancaContaReceberVO, tipoAgenteNegativacaoCobrancaContaReceberEnum, responsavel);
}
		}
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void estornarExclusao(final RegistroNegativacaoCobrancaContaReceberItemVO obj, AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgenteNegativacaoCobrancaContaReceberEnum, UsuarioVO responsavel) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder("UPDATE RegistroNegativacaoCobrancaContaReceberItem set codigoUsuarioExclusao=?, nomeUsuarioExclusao=?, dataExclusao=?, motivo= ? WHERE ((codigo = ?))");
			sql.append(" and not exists (select rni.codigo from RegistroNegativacaoCobrancaContaReceberItem rni inner join RegistroNegativacaoCobrancaContaReceber rn on rn.codigo = rni.RegistroNegativacaoCobrancaContaReceber  ");
			sql.append(" and rni.contaReceber = ").append(obj.getContaReceber());
			sql.append(" and rn.tipoAgente = '").append(tipoAgenteNegativacaoCobrancaContaReceberEnum.name()).append("' ");
			sql.append(" and rni.dataExclusao is null ");
			sql.append(" and rni.codigo != ").append(obj.getCodigo());
			if(tipoAgenteNegativacaoCobrancaContaReceberEnum.equals(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO)) {
				sql.append(" and rn.agente = ").append(agenteNegativacaoCobrancaContaReceberVO.getCodigo());
			}
			sql.append(" ) ");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(responsavel));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setNull(1, 0);
					sqlAlterar.setString(2, "");
					sqlAlterar.setNull(3, 0);
					sqlAlterar.setString(4, "");
					sqlAlterar.setInt(5, obj.getCodigo());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerVinculoContaReceberRegistroNegativacaoContaReceber(Integer contaReceber, UsuarioVO usuarioVO) throws Exception {
		try {
			final StringBuilder sql = new StringBuilder("UPDATE RegistroNegativacaoCobrancaContaReceberItem set contareceber = null WHERE ((contaReceber = ?)) and contaReceber is not null and contaReceber !=  0");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));			
			getConexao().getJdbcTemplate().update(sql.toString(), contaReceber);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void correcaoSerasaApiGeo(String nossoNumero) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(nossoNumero), "O nosso numero deve ser informado e separado por aspa e virgula para condição IN.");
		UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().obterUsuarioResponsavelOperacoesExternas();
		AgenteNegativacaoCobrancaContaReceberVO anccr = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(6, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		ConfiguracaoGeralSistemaVO config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(anccr.getCredorUnidadeEnsinoVO().getCodigo(), usuario);
		List<RegistroNegativacaoCobrancaContaReceberItemVO> lista = consultarRegistroNegativacaoCobrancaContaReceberItemcorrecaoSerasaApiGeo(nossoNumero);
		for (RegistroNegativacaoCobrancaContaReceberItemVO rnccri : lista) {		
			try {
				executarPersistenciaCorrecaoSerasaApiGeo(usuario, anccr, config, rnccri);	
			} catch (Exception e) {
				getAplicacaoControle().realizarEscritaErroDebug(AssuntoDebugEnum.GERAL, e);
			}
		}
	}


	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void executarPersistenciaCorrecaoSerasaApiGeo(UsuarioVO usuario, AgenteNegativacaoCobrancaContaReceberVO anccr, ConfiguracaoGeralSistemaVO config, RegistroNegativacaoCobrancaContaReceberItemVO rnccri) throws Exception {
		getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().executarEnvioIntegracaoWebService(anccr, rnccri, JobsEnum.JOB_SERASA_API_GEO_REMOVER, config, usuario);
		rnccri.setCodigoUsuarioExclusao(usuario.getCodigo());
		rnccri.setNomeUsuarioExclusao(usuario.getNome());
		rnccri.setDataExclusao(new Date());
		rnccri.setMotivo(rnccri.getMotivo() + "**");
		alterarExclusao(rnccri);
	}
	
	public List<RegistroNegativacaoCobrancaContaReceberItemVO> consultarRegistroNegativacaoCobrancaContaReceberItemcorrecaoSerasaApiGeo(String nossoNumero) {
		StringBuilder sql = new StringBuilder(" ");
		sql.append("select *, 'AR' as situacaocontareceber, 0 as negociacaocontareceber, ");
		sql.append(" 'SERASA - GEO' as agente,  6 as codigoAgente, ");
		sql.append(" false as bloqueio, '' as parceiro ");
		sql.append(" from registronegativacaocobrancacontareceberitem where dataregistro >= '2021-03-22' and codigointegracaoapi not ilike ('erro%') ");
		sql.append("and nossonumero not in (").append(nossoNumero);
		sql.append(")");
		
		List<RegistroNegativacaoCobrancaContaReceberItemVO> registroNegativacaoCobrancaContaReceberItemVOs = new ArrayList<RegistroNegativacaoCobrancaContaReceberItemVO>(0);
		SqlRowSet rs =getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			RegistroNegativacaoCobrancaContaReceberItemVO registroNegativacaoCobrancaContaReceberItemVO = montarDadosCompleta(rs);
			registroNegativacaoCobrancaContaReceberItemVOs.add(registroNegativacaoCobrancaContaReceberItemVO);
		}
		return registroNegativacaoCobrancaContaReceberItemVOs;		
	}
		
	
}
