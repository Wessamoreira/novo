package negocio.facade.jdbc.estagio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.QuestionarioRespostaOrigemMotivosPadroesEstagioVO;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.estagio.MotivosPadroesEstagioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.estagio.MotivosPadroesEstagioInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class MotivosPadroesEstagio extends ControleAcesso implements MotivosPadroesEstagioInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6654775229030356879L;
	private static String idEntidade = "MotivosPadroesEstagio";

	public static String getIdEntidade() {
		return MotivosPadroesEstagio.idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void valiarDados(MotivosPadroesEstagioVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDescricao()), UteisJSF.internacionalizar("msg_MotivosPadroesEstagio_descricao"));

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(MotivosPadroesEstagioVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			valiarDados(obj);
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MotivosPadroesEstagioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "motivosPadroesEstagio", new AtributoPersistencia()
					.add("descricao", obj.getDescricao())
					.add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum())
					.add("retornoSolicitacaoAproveitamento", obj.isRetornoSolicitacaoAproveitamento())
					.add("indeferimentoSolicitacaoAproveitamento", obj.isIndeferimentoSolicitacaoAproveitamento())
					.add("retornoSolicitacaoEquivalencia", obj.isRetornoSolicitacaoEquivalencia())
					.add("indeferimentoSolicitacaoEquivalencia", obj.isIndeferimentoSolicitacaoEquivalencia())
					.add("retornoAvaliacaoTermo", obj.isRetornoAvaliacaoTermo())
					.add("indeferimentoAvaliacaoTermo", obj.isIndeferimentoAvaliacaoTermo())
					.add("retornoAvaliacaoRelatorioFinal", obj.isRetornoAvaliacaoRelatorioFinal())
					.add("indeferimentoAvaliacaoRelatorioFinal", obj.isIndeferimentoAvaliacaoRelatorioFinal())
					.add("retornoAditivos", obj.isRetornoAditivos())
					.add("indeferimentoAditivos", obj.isIndeferimentoAditivos())
					.add("retornoRecisao", obj.isRetornoRecisao())
					.add("indeferimentoRecisao", obj.isIndeferimentoRecisao())
					, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MotivosPadroesEstagioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "motivosPadroesEstagio", new AtributoPersistencia()
					.add("descricao", obj.getDescricao())
					.add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum())
					.add("retornoSolicitacaoAproveitamento", obj.isRetornoSolicitacaoAproveitamento())
					.add("indeferimentoSolicitacaoAproveitamento", obj.isIndeferimentoSolicitacaoAproveitamento())
					.add("retornoSolicitacaoEquivalencia", obj.isRetornoSolicitacaoEquivalencia())
					.add("indeferimentoSolicitacaoEquivalencia", obj.isIndeferimentoSolicitacaoEquivalencia())
					.add("retornoAvaliacaoTermo", obj.isRetornoAvaliacaoTermo())
					.add("indeferimentoAvaliacaoTermo", obj.isIndeferimentoAvaliacaoTermo())
					.add("retornoAvaliacaoRelatorioFinal", obj.isRetornoAvaliacaoRelatorioFinal())
					.add("indeferimentoAvaliacaoRelatorioFinal", obj.isIndeferimentoAvaliacaoRelatorioFinal())
					.add("retornoAditivos", obj.isRetornoAditivos())
					.add("indeferimentoAditivos", obj.isIndeferimentoAditivos())
					.add("retornoRecisao", obj.isRetornoRecisao())
					.add("indeferimentoRecisao", obj.isIndeferimentoRecisao())
					,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MotivosPadroesEstagioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM motivosPadroesEstagio WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, MotivosPadroesEstagioVO obj) throws Exception {
		dataModelo.getListaConsulta().clear();
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultaRapidaPorFiltros(obj, dataModelo));
	}

	private List<MotivosPadroesEstagioVO> consultaRapidaPorFiltros(MotivosPadroesEstagioVO obj, DataModelo dataModelo) {
		try {
			consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1=1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			montarTotalizadorConsultaBasica(dataModelo, tabelaResultado);
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public List<MotivosPadroesEstagioVO> consultarMotivosPadroesEstagioUtilizarTipoComponente(TipoEstagioEnum tipoEstagio, Boolean retorno, Boolean indeferido, List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> listaFiltrar, UsuarioVO usuario ) {
		try {
			StringBuilder sqlStr = new StringBuilder("");
			if (TipoEstagioEnum.OBRIGATORIO_APROVEITAMENTO == tipoEstagio) {
				sqlStr.append(" "); 
				boolean addunion = false;
				if (retorno) {
					sqlStr.append(" select motivospadroesestagio.* from motivospadroesestagio "); 
					sqlStr.append(" WHERE 1=1 ");
					sqlStr.append(" and retornosolicitacaoaproveitamento = true ");
					sqlStr.append(addSqlRetirandoListagemMotivosPadroesEstagioJaExistente(listaFiltrar));
					addunion = true;
				}
				if (indeferido) {
					if (addunion) {
						sqlStr.append(" union ");	
					}
					sqlStr.append(" select motivospadroesestagio.* from motivospadroesestagio "); 
					sqlStr.append(" WHERE 1=1 ");
					sqlStr.append(" and indeferimentosolicitacaoaproveitamento = true ");
					sqlStr.append(addSqlRetirandoListagemMotivosPadroesEstagioJaExistente(listaFiltrar));
					addunion = true;
				}
				if (addunion) {
					sqlStr.append(" order by codigo ");
				}
			}
			if (TipoEstagioEnum.OBRIGATORIO_EQUIVALENCIA == tipoEstagio) {
				sqlStr.append(" "); 
				boolean addunion = false;
				if (retorno) {
					sqlStr.append(" select motivospadroesestagio.* from motivospadroesestagio "); 
					sqlStr.append(" WHERE 1=1 ");
					sqlStr.append(" and retornosolicitacaoequivalencia = true ");
					sqlStr.append(addSqlRetirandoListagemMotivosPadroesEstagioJaExistente(listaFiltrar));
					addunion = true;
				}
				if (indeferido) {
					if (addunion) {
						sqlStr.append(" union ");	
					}
					sqlStr.append(" select motivospadroesestagio.* from motivospadroesestagio "); 
					sqlStr.append(" WHERE 1=1 ");
					sqlStr.append(" and indeferimentosolicitacaoequivalencia = true ");
					sqlStr.append(addSqlRetirandoListagemMotivosPadroesEstagioJaExistente(listaFiltrar));
					addunion = true;
				}
				if (addunion) {
					sqlStr.append(" order by codigo ");
				}
			}
			if(Uteis.isAtributoPreenchido(sqlStr)) {
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);	
			}
			return new ArrayList<>();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private String addSqlRetirandoListagemMotivosPadroesEstagioJaExistente(List<QuestionarioRespostaOrigemMotivosPadroesEstagioVO> listaFiltrar) {
		try {
			StringBuilder sqlStr = new StringBuilder("");
			if (!listaFiltrar.isEmpty()) {
				sqlStr.append(" and codigo not in ( ");
			}
			for (QuestionarioRespostaOrigemMotivosPadroesEstagioVO obj : listaFiltrar) {
				sqlStr.append(obj.getMotivosPadroesEstagioVO().getCodigo());
				sqlStr.append(", ");
			}
			if (!listaFiltrar.isEmpty()) {
				sqlStr.append(" 0)");
			}
			return sqlStr.toString();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosParaConsulta(MotivosPadroesEstagioVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sqlStr.append(" and codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getDescricao())) {
			sqlStr.append(" and lower(sem_acentos(descricao)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getDescricao().toLowerCase() + PERCENT);
		}
		
		if (Uteis.isAtributoPreenchido(obj.getStatusAtivoInativoEnum())) {
			sqlStr.append(" and  statusAtivoInativoEnum = ?");
			dataModelo.getListaFiltros().add(obj.getStatusAtivoInativoEnum().name());
		}
		switch (obj.getMotivosPadroesEstagioCasoUsoEnumFiltro()) {
		case RETORNO_SOLICITACAO_APROVEITAMENTO:
			sqlStr.append(" and  retornoSolicitacaoAproveitamento = true ");
			break;
		case INDEFERIMENTO_SOLICITACAO_APROVEITAMENTO:
			sqlStr.append(" and  indeferimentoSolicitacaoAproveitamento = true ");
			break;
		case RETORNO_SOLICITACAO_EQUIVALENCIA:
			sqlStr.append(" and  retornoSolicitacaoEquivalencia = true ");
			break;
		case INDEFERIMENTO_SOLICITACAO_EQUIVALENCIA:
			sqlStr.append(" and  indeferimentoSolicitacaoEquivalencia = true ");
			break;
		case RETORNO_AVALIACAO_TERMO:
			sqlStr.append(" and  retornoAvaliacaoTermo = true ");
			break;
		case INDEFERIMENTO_AVALIACAO_TERMO:
			sqlStr.append(" and  indeferimentoAvaliacaoTermo = true ");
			break;
		case RETORNO_AVALIACAO_RELATORIO_FINAL:
			sqlStr.append(" and  retornoAvaliacaoRelatorioFinal = true ");
			break;
		case INDEFERIMENTO_AVALIACAO_RELATORIO_FINAL:
			sqlStr.append(" and  indeferimentoAvaliacaoRelatorioFinal = true ");
			break;
		case RETORNO_ADITIVOS:
			sqlStr.append(" and  retornoAditivos = true ");
			break;
		case INDEFERIMENTO_ADITIVOS:
			sqlStr.append(" and  indeferimentoAditivos = true ");
			break;
		case RETORNO_RECISAO:
			sqlStr.append(" and  retornoRecisao = true ");
			break;
		case INDEFERIMENTO_RECISAO:
			sqlStr.append(" and  indeferimentoRecisao = true ");
			break;
		default:
			break;
		}

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public MotivosPadroesEstagioVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" where motivosPadroesEstagio.codigo = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( MotivosPadroesEstagioVO ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder(getSelectTotalizadorConsultaBasica());
		sql.append(" codigo,  ");
		sql.append(" descricao, ");
		sql.append(" statusAtivoInativoEnum, ");
		sql.append(" retornoSolicitacaoAproveitamento, ");
		sql.append(" indeferimentoSolicitacaoAproveitamento, ");
		sql.append(" retornoSolicitacaoEquivalencia, ");
		sql.append(" indeferimentoSolicitacaoEquivalencia, ");
		sql.append(" retornoAvaliacaoTermo, ");
		sql.append(" indeferimentoAvaliacaoTermo, ");
		sql.append(" retornoAvaliacaoRelatorioFinal, ");
		sql.append(" indeferimentoAvaliacaoRelatorioFinal, ");
		sql.append(" retornoAditivos, ");
		sql.append(" indeferimentoAditivos, ");
		sql.append(" retornoRecisao, ");
		sql.append(" indeferimentoRecisao ");
		sql.append(" FROM motivosPadroesEstagio ");

		return sql;
	}

	private List<MotivosPadroesEstagioVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<MotivosPadroesEstagioVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private MotivosPadroesEstagioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		MotivosPadroesEstagioVO obj = new MotivosPadroesEstagioVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("statusAtivoInativoEnum")));
		obj.setRetornoSolicitacaoAproveitamento(dadosSQL.getBoolean("retornoSolicitacaoAproveitamento"));
		obj.setIndeferimentoSolicitacaoAproveitamento(dadosSQL.getBoolean("indeferimentoSolicitacaoAproveitamento"));
		obj.setRetornoSolicitacaoEquivalencia(dadosSQL.getBoolean("retornoSolicitacaoEquivalencia"));
		obj.setIndeferimentoSolicitacaoEquivalencia(dadosSQL.getBoolean("indeferimentoSolicitacaoEquivalencia"));
		obj.setRetornoAvaliacaoTermo(dadosSQL.getBoolean("retornoAvaliacaoTermo"));
		obj.setIndeferimentoAvaliacaoTermo(dadosSQL.getBoolean("indeferimentoAvaliacaoTermo"));
		obj.setRetornoAvaliacaoRelatorioFinal(dadosSQL.getBoolean("retornoAvaliacaoRelatorioFinal"));
		obj.setIndeferimentoAvaliacaoRelatorioFinal(dadosSQL.getBoolean("indeferimentoAvaliacaoRelatorioFinal"));
		obj.setRetornoAditivos(dadosSQL.getBoolean("retornoAditivos"));
		obj.setIndeferimentoAditivos(dadosSQL.getBoolean("indeferimentoAditivos"));
		obj.setRetornoRecisao(dadosSQL.getBoolean("retornoRecisao"));
		obj.setIndeferimentoRecisao(dadosSQL.getBoolean("indeferimentoRecisao"));
		return obj;

	}

}
