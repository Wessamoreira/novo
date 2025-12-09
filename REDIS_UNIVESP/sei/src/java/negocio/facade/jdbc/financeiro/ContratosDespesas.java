package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContratoDespesaEspecificoVO;
import negocio.comuns.financeiro.ContratosDespesasVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContratosDespesasInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ContratosDespesasVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ContratosDespesasVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ContratosDespesasVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ContratosDespesas extends ControleAcesso implements ContratosDespesasInterfaceFacade {

	protected static String idEntidade;

	public ContratosDespesas() throws Exception {
		super();
		setIdEntidade("ContratosDespesas");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ContratosDespesasVO</code>.
	 */
	public ContratosDespesasVO novo() throws Exception {
		ContratosDespesas.incluir(getIdEntidade());
		ContratosDespesasVO obj = new ContratosDespesasVO();
		return obj;
	}

	public void verificarPermissaoAutorizarIndeferir(ContratosDespesasVO obj, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidade("IndeferirContratoDespesa", usuario);
	}

	@Override
	public void realizarDefinicaoContaPagarExcluir(ContratosDespesasVO obj, List<ContaPagarVO> contaPagarVOs) {
		for (ContaPagarVO contaPagarVO : contaPagarVOs) {
			if (contaPagarVO.getQuitada()) {
				contaPagarVO.setExcluir(false);
			} else {
				if (contaPagarVO.getDataVencimento().after(obj.getDataCancelamento())) {
					contaPagarVO.setExcluir(true);
				} else {
					contaPagarVO.setExcluir(false);
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCancelamentoContrato(final ContratosDespesasVO obj, List<ContaPagarVO> contaPagarVOs, UsuarioVO usuario) throws Exception {
		try {
			obj.setSituacao("IN");
			obj.getResponsavelCancelamento().setCodigo(usuario.getCodigo());
			obj.getResponsavelCancelamento().setNome(usuario.getNome());
			obj.setContratoIndeterminado(false);			
			final String sql = "UPDATE ContratosDespesas set dataCancelamento=?, motivoCancelamento=?, responsavelCancelamento=?, situacao = ?, contratoIndeterminado = ? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataCancelamento()));
					sqlAlterar.setString(2, obj.getMotivoCancelamento());
					sqlAlterar.setInt(3, obj.getResponsavelCancelamento().getCodigo().intValue());
					sqlAlterar.setString(4, obj.getSituacao());
					sqlAlterar.setBoolean(5, obj.getContratoIndeterminado());
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			StringBuilder listaCodigosContasPagarExcluir =  new StringBuilder();
			for(ContaPagarVO contaPagarVO: contaPagarVOs) {
				if(contaPagarVO.getExcluir()) {
					if(listaCodigosContasPagarExcluir.length() > 0) {
						listaCodigosContasPagarExcluir.append(", ");
					}
					listaCodigosContasPagarExcluir.append(contaPagarVO.getCodigo());
				}
			}		
			if(listaCodigosContasPagarExcluir.length() > 0) {
				Boolean validarBloqueio = !obj.verificarBloqueioCompetenciaFoiLiberado();
				getFacadeFactory().getContaPagarFacade().excluirListaContasPagar(listaCodigosContasPagarExcluir.toString(), validarBloqueio, usuario);
			}
		} catch (Exception e) {
			if (e.getMessage().contains("(Competência Fechada)")) {
				// Se tivemos um erro de bloqueio de competencia (seja na conta a receber ou outra entidade. Iremos replicar esse
				// bloqueio para a MatriculaPeriodoVO. Assim, o usuário poderá visualizar o botao de liberacao de competencia fechada (FechamentoMes)
				// e seguir o fluxo posteriormente.
				ConsistirException cEx = (ConsistirException)e;
				obj.forcarControleBloqueioCompetencia(e.getMessage(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio().getDataBloqueioVerificada());
			}			
			obj.setSituacao("AP");
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarValorDasParcelasPendentes(ContratosDespesasVO contratosDespesasVO, Double valorNovaParcela, UsuarioVO usuarioVO) throws Exception {
		try {
			getFacadeFactory().getContaPagarFacade().alterarContaPagarPorCodigoOrigemTipoOrigemSituacaoNumeroContratoDespesas(valorNovaParcela, String.valueOf(contratosDespesasVO.getCodigo()), OrigemContaPagar.CONTRATO_DESPESA.getValor(), SituacaoFinanceira.A_PAGAR.getValor(), false, !contratosDespesasVO.verificarBloqueioCompetenciaFoiLiberado(),usuarioVO);
			contratosDespesasVO.setValorParcela(valorNovaParcela);
			getConexao().getJdbcTemplate().update("UPDATE ContratosDespesas set valorParcela = ? where codigo = ?"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), valorNovaParcela, contratosDespesasVO.getCodigo());
			StringBuilder sqlCentroResultado = new StringBuilder(" update centroresultadoorigem set valor = "); 
			sqlCentroResultado.append(" ((").append(valorNovaParcela).append("*porcentagem)/100.00) from ContratosDespesas where centroresultadoorigem.tipocentroresultadoorigem = '").append(TipoCentroResultadoOrigemEnum.CONTRATO_DESPESA.name()).append("' ");
			sqlCentroResultado.append(" and centroresultadoorigem.codorigem = ContratosDespesas.codigo::varchar ");
			sqlCentroResultado.append(" and ContratosDespesas.codigo = ").append(contratosDespesasVO.getCodigo());
			getConexao().getJdbcTemplate().update(sqlCentroResultado.toString());
		} catch (Exception e) {
			if (e.getMessage().contains("(Competência Fechada)")) {
				// Se tivemos um erro de bloqueio de competencia (seja na conta a receber ou outra entidade. Iremos replicar esse
				// bloqueio para a MatriculaPeriodoVO. Assim, o usuário poderá visualizar o botao de liberacao de competencia fechada (FechamentoMes)
				// e seguir o fluxo posteriormente.
				ConsistirException cEx = (ConsistirException)e;
				contratosDespesasVO.forcarControleBloqueioCompetencia(e.getMessage(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio().getDataBloqueioVerificada());
			}
			throw e;
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ContratosDespesasVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContratosDespesasVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ContratosDespesasVO obj, UsuarioVO usuario) throws Exception {
		try {
			ContratosDespesasVO.validarDados(obj);
			ContratosDespesas.incluir(getIdEntidade(), true, usuario);
			incluir(obj, "ContratosDespesas", new AtributoPersistencia()
					.add("valorParcela", obj.getValorParcela())
					.add("diaVencimento", obj.getDiaVencimento())
					.add("mesVencimento", obj.getMesVencimento())
					.add("situacao", obj.getSituacao())
					.add("contratoIndeterminado", obj.isContratoIndeterminado())
					.add("dataTermino", !obj.isContratoIndeterminado() ? Uteis.getDataJDBC(obj.getDataTermino()) : null)
					.add("dataInicio", Uteis.getDataJDBC(obj.getDataInicio()))
					.add("descricao", obj.getDescricao())
					.add("tipoContrato", obj.getTipoContrato())
					.add("fornecedor", obj.getFornecedor())
					.add("dataAprovacao", Uteis.getDataJDBCTimestamp(obj.getDataAprovacao()))
					.add("dataPrimeiraParcela", Uteis.getDataJDBC(obj.getDataPrimeiraParcela()))
					.add("responsavelAprovacao", obj.getResponsavelAprovacao())
					.add("funcionario", obj.getFuncionario())
					.add("unidadeEnsino", obj.getUnidadeEnsino())
					.add("tipoSacado", obj.getTipoSacado())
					.add("banco", obj.getBanco())
					, usuario);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(obj.getListaCentroResultadoOrigemVOs(), obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTRATO_DESPESA, false, usuario, false);
			getFacadeFactory().getContratoDespesaEspecificoFacade().incluirContratoDespesaEspecificos(obj.getCodigo(), obj.getContratoDespesaEspecificoVOs(), usuario);
			if (obj.getSituacao().equals("AP")) {
				criarContaPagar(obj, usuario);
			}
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			if (e.getMessage().contains("(Competência Fechada)")) {
				// Se tivemos um erro de bloqueio de competencia (seja na conta a receber ou outra entidade. Iremos replicar esse
				// bloqueio para a MatriculaPeriodoVO. Assim, o usuário poderá visualizar o botao de liberacao de competencia fechada (FechamentoMes)
				// e seguir o fluxo posteriormente.
				ConsistirException cEx = (ConsistirException)e;
				obj.forcarControleBloqueioCompetencia(e.getMessage(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio().getDataBloqueioVerificada());
			}			
			
			obj.setSituacao("");
			obj.setCodigo(0);
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Método responsavel para criar as Contas a pagar pertinentes ao contrato
	 * de despesa já autorizado
	 * 
	 * @param obj
	 *            = Contrato de Despesa Autorizado
	 */
	public void criarContaPagar(ContratosDespesasVO obj, UsuarioVO usuario) throws Exception {
		if (!obj.getContratoDespesaEspecificoVOs().isEmpty()) {
			criarContaPagarContratoDespesaEspecifico(obj, usuario);
		} else {
			if (obj.getTipoContrato().equals("ME")) {
				criarContaPagarContratoDespesaMensal(obj, usuario);
			} else if (obj.getTipoContrato().equals("AN")) {
				criarContaPagarContratoDespesaAnual(obj, usuario);
			} else if (obj.getTipoContrato().equals("ES")) {
				criarContaPagarContratoDespesaEspecifico(obj, usuario);
			}
		}

	}

	public void criarContaPagarContratoDespesaMensal(ContratosDespesasVO obj, UsuarioVO usuario) throws Exception {
		int qtdeContaPagarContrato = 0;
		if (obj.getContratoIndeterminado()) {
			qtdeContaPagarContrato = 12;
		} else {
			qtdeContaPagarContrato = Uteis.getCalculaQuantidadeMesesEntreDatas(obj.getDataInicio(), obj.getDataTermino());
			qtdeContaPagarContrato++;
		}
		int nrParcelas = 1;
		Date dataVencimento = Uteis.getDateSemHora(obj.getDataInicio());
		if (Integer.parseInt(obj.getDiaVencimento()) < Uteis.getDiaMesData(obj.getDataInicio())) {
			dataVencimento = Uteis.getDataVencimentoPadrao(Integer.parseInt(obj.getDiaVencimento()), dataVencimento, 1);
		} else {
			dataVencimento = Uteis.getDataVencimentoPadrao(Integer.parseInt(obj.getDiaVencimento()), dataVencimento, 0);
		}
		while (nrParcelas <= qtdeContaPagarContrato) {
			if (dataVencimento.before(obj.getDataTermino()) || obj.getDataTermino().compareTo(dataVencimento) >= 0 || obj.getContratoIndeterminado()) {
				// String nrDoc = obj.getCodigo() + "." + nrParcelas + "." +
				// Uteis.getAnoDataAtual();
				incluirContaPagar(obj, obj.getValorParcela(), (nrParcelas + "/" + qtdeContaPagarContrato), "", dataVencimento, usuario);
			}
			dataVencimento = Uteis.getDataVencimentoPadrao(Integer.parseInt(obj.getDiaVencimento()), dataVencimento, 1);
			nrParcelas++;
		}
	}

	@Override
	public void realizarGeracaoContratoDespesaEspecificao(ContratosDespesasVO obj) throws Exception {
		if (obj.getTipoContrato().equals("")) {
			throw new ConsistirException("O campo TIPO CONTRATO (Contrato de Despesa) deve ser informado.");
		}
		if (obj.getTipoContrato().equals("ME")) {
			if (obj.getDataTermino() == null) {
				throw new ConsistirException("O campo DATA TÉRMINO (Contrato de Despesa) deve ser informado.");
			}
			if (obj.getDataInicio().after(obj.getDataTermino()) || obj.getDataInicio().compareTo(obj.getDataTermino()) > 0) {
				throw new ConsistirException("O campo DATA TERMINO deve ser maior que a DATA INÍCIO (Contrato de Despesa).");
			}
			if (Integer.parseInt(obj.getDiaVencimento()) > 31 || Integer.parseInt(obj.getDiaVencimento()) < 1) {
				throw new ConsistirException("O campo DIA VENCIMENTO (Contrato de Despesa) é inválido.");
			}
			if (Integer.parseInt(obj.getDiaVencimento()) > 31 || Integer.parseInt(obj.getDiaVencimento()) < 1) {
				throw new ConsistirException("O campo DIA VENCIMENTO (Contrato de Despesa) é inválido.");
			}
		}
		if (obj.getTipoContrato().equals("ES")) {
			if (obj.getDiaVencimento().equals("")) {
				throw new ConsistirException("O campo DIA VENCIMENTO (Contrato de Despesa) deve ser informado.");
			}
			if (Integer.parseInt(obj.getDiaVencimento()) > 31 || Integer.parseInt(obj.getDiaVencimento()) < 1) {
				throw new ConsistirException("O campo DIA VENCIMENTO (Contrato de Despesa) é inválido.");
			}
		}
		if (obj.getTipoContrato().equals("AN")) {
			if (obj.getDataTermino() == null) {
				throw new ConsistirException("O campo DATA TÉRMINO (Contrato de Despesa) deve ser informado.");
			}
			if (obj.getDataInicio().after(obj.getDataTermino()) || obj.getDataInicio().compareTo(obj.getDataTermino()) > 0) {
				throw new ConsistirException("O campo DATA TERMINO deve ser maior que a DATA INÍCIO (Contrato de Despesa).");
			}
			if (Integer.parseInt(obj.getDiaVencimento()) > 31 || Integer.parseInt(obj.getDiaVencimento()) < 1) {
				throw new ConsistirException("O campo DIA VENCIMENTO (Contrato de Despesa) é inválido.");
			}
			if ( obj.getMesVencimento().equals("") || Integer.parseInt(obj.getMesVencimento()) > 12 || Integer.parseInt(obj.getMesVencimento()) < 1) {
				throw new ConsistirException("O campo MÊS VENCIMENTO (Contrato de Despesa) é inválido.");
			}
		}
		if (obj.getValorParcela() == null || obj.getValorParcela() == 0.0) {
			throw new ConsistirException("O campo VALOR (Contrato de Despesa) deve ser informado.");
		}
		if (obj.getTipoContrato().equals("ME")) {
			criarContratoDespesaEspecificoMensal(obj);
		} else if (obj.getTipoContrato().equals("AN")) {
			criarContratoDespesaEspecificoAnual(obj);
		}
	}

	public void criarContratoDespesaEspecificoMensal(ContratosDespesasVO obj) throws Exception {
		int qtdeContaPagarContrato = 0;
		if (obj.getContratoIndeterminado()) {
			qtdeContaPagarContrato = 12;
		} else {
			qtdeContaPagarContrato = Uteis.getCalculaQuantidadeMesesEntreDatas(obj.getDataInicio(), obj.getDataTermino());
			qtdeContaPagarContrato++;
		}
		int nrParcelas = 1;
		Date dataVencimento = Uteis.getDateSemHora(obj.getDataInicio());
		if (Integer.parseInt(obj.getDiaVencimento()) < Uteis.getDiaMesData(obj.getDataInicio())) {
			dataVencimento = Uteis.getDataVencimentoPadraoConsiderandoFevereiro(Integer.parseInt(obj.getDiaVencimento()), dataVencimento, 1);
		} else {
			dataVencimento = Uteis.getDataVencimentoPadraoConsiderandoFevereiro(Integer.parseInt(obj.getDiaVencimento()), dataVencimento, 0);
		}
		obj.getContratoDespesaEspecificoVOs().clear();
		ContratoDespesaEspecificoVO contratoDespesaEspecificoVO = null;
		while (nrParcelas <= qtdeContaPagarContrato) {
			if (dataVencimento.before(obj.getDataTermino()) || obj.getDataTermino().compareTo(dataVencimento) >= 0 || obj.getContratoIndeterminado()) {
				contratoDespesaEspecificoVO = new ContratoDespesaEspecificoVO();
				contratoDespesaEspecificoVO.setDataVencimento(dataVencimento);
				contratoDespesaEspecificoVO.setNrParcela(nrParcelas);
				contratoDespesaEspecificoVO.setValorParcela(obj.getValorParcela());
				obj.getContratoDespesaEspecificoVOs().add(contratoDespesaEspecificoVO);
			}
			dataVencimento = Uteis.getDataVencimentoPadraoConsiderandoFevereiro(Integer.parseInt(obj.getDiaVencimento()), dataVencimento, 1);
			nrParcelas++;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirContaPagar(ContratosDespesasVO obj, Double valor, String serie, String nrDoc, Date dataVencimento, UsuarioVO usuario) throws Exception {
		ContaPagarVO contaPagar = new ContaPagarVO();
		contaPagar.setTipoOrigem(OrigemContaPagar.CONTRATO_DESPESA.getValor());
		contaPagar.verificarEReplicarLiberacaoBloqueioEntidadePrincipal(obj);
		contaPagar.setCodOrigem(String.valueOf(obj.getCodigo()));
		contaPagar.getFornecedor().setCodigo(obj.getFornecedor().getCodigo());
		contaPagar.getBanco().setCodigo(obj.getBanco().getCodigo());
		contaPagar.getFuncionario().setCodigo(obj.getFuncionario().getCodigo());
		contaPagar.setDataVencimento(dataVencimento);
		contaPagar.setDataFatoGerador(dataVencimento);
		contaPagar.setParcela(serie);
		contaPagar.setSituacao(SituacaoFinanceira.A_PAGAR.getValor());
		contaPagar.setValor(valor);
		contaPagar.setDescricao(obj.getDescricao());
		contaPagar.setValorPago(0.0);
		contaPagar.setValorPagamento(0.0);
		contaPagar.getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
		contaPagar.setTipoSacado(obj.getTipoSacado());
		contaPagar.setNrDocumento(nrDoc);
		contaPagar.getResponsavel().setCodigo(usuario.getCodigo());
		contaPagar.getResponsavel().setNome(usuario.getNome());
		for(CentroResultadoOrigemVO centroResultadoOrigemVO: obj.getListaCentroResultadoOrigemVOs()) {
			CentroResultadoOrigemVO cro = centroResultadoOrigemVO.getClone();
			cro.setCodigo(0);
			cro.setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.CONTA_PAGAR);
			cro.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA);
			cro.calcularValor(valor);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().preencherDadosPorCategoriaDespesa(cro, usuario);
			contaPagar.getListaCentroResultadoOrigemVOs().add(cro);
		}
		getFacadeFactory().getContaPagarFacade().incluir(contaPagar, false, true, usuario);
	}

	public void criarContaPagarContratoDespesaAnual(ContratosDespesasVO obj, UsuarioVO usuario) throws Exception {

		int qtdeContaPagarContrato = 0;
		if (obj.getContratoIndeterminado()) {
			qtdeContaPagarContrato = 1;
		} else {
			qtdeContaPagarContrato = Uteis.getCalculaQuantidadeAnosEntreDatas(obj.getDataInicio(), obj.getDataTermino());
			qtdeContaPagarContrato++;
		}
		int nrParcelas = 1;
		while (nrParcelas <= qtdeContaPagarContrato) {
			int ano = Uteis.getAnoData(obj.getDataPrimeiraParcela()) + nrParcelas - 1;
			Date dataVencimento = Uteis.getDateSemHora(Uteis.getDate(obj.getDiaVencimento() + "/" + obj.getMesVencimento() + "/" + ano));
			if (dataVencimento.before(obj.getDataInicio())) {
				ano++;
				dataVencimento = Uteis.getDateSemHora(Uteis.getDate(obj.getDiaVencimento() + "/" + obj.getMesVencimento() + "/" + ano));
			}
			if (dataVencimento.before(obj.getDataTermino()) || obj.getDataTermino().compareTo(dataVencimento) >= 0 || obj.getContratoIndeterminado()) {

				incluirContaPagar(obj, obj.getValorParcela(), (nrParcelas + "/" + qtdeContaPagarContrato), "", dataVencimento, usuario);
			}
			nrParcelas++;
		}

	}

	public void criarContratoDespesaEspecificoAnual(ContratosDespesasVO obj) throws Exception {
		int qtdeContaPagarContrato = 0;
		if (obj.getContratoIndeterminado()) {
			qtdeContaPagarContrato = 1;
		} else {
			qtdeContaPagarContrato = Uteis.getCalculaQuantidadeAnosEntreDatas(obj.getDataInicio(), obj.getDataTermino());
			qtdeContaPagarContrato++;
		}
		int nrParcelas = 1;
		while (nrParcelas <= qtdeContaPagarContrato) {
			int ano = Uteis.getAnoData(obj.getDataPrimeiraParcela()) + nrParcelas - 1;
			Date dataVencimento = Uteis.getDateSemHora(Uteis.getDate(obj.getDiaVencimento() + "/" + obj.getMesVencimento() + "/" + ano));
			if (dataVencimento.before(obj.getDataInicio())) {
				ano++;
				dataVencimento = Uteis.getDateSemHora(Uteis.getDate(obj.getDiaVencimento() + "/" + obj.getMesVencimento() + "/" + ano));
			}
			obj.getContratoDespesaEspecificoVOs().clear();
			ContratoDespesaEspecificoVO contratoDespesaEspecificoVO = null;
			if (dataVencimento.before(obj.getDataTermino()) || obj.getDataTermino().compareTo(dataVencimento) >= 0 || obj.getContratoIndeterminado()) {
				contratoDespesaEspecificoVO = new ContratoDespesaEspecificoVO();
				contratoDespesaEspecificoVO.setDataVencimento(dataVencimento);
				contratoDespesaEspecificoVO.setNrParcela(nrParcelas);
				contratoDespesaEspecificoVO.setValorParcela(obj.getValorParcela());
				obj.getContratoDespesaEspecificoVOs().add(contratoDespesaEspecificoVO);
			}
			nrParcelas++;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarContaPagarContratoDespesaEspecifico(ContratosDespesasVO obj, UsuarioVO usuario) throws Exception {		
		Ordenacao.ordenarLista(obj.getContratoDespesaEspecificoVOs(), "dataVencimento");
		int x = 1;
		for (Iterator<ContratoDespesaEspecificoVO> iterator = obj.getContratoDespesaEspecificoVOs().iterator(); iterator.hasNext();) {			
			ContratoDespesaEspecificoVO especifico = (ContratoDespesaEspecificoVO) iterator.next();
			incluirContaPagar(obj, especifico.getValorParcela(), (x + "/" + obj.getContratoDespesaEspecificoVOs().size()), especifico.getNumeroDocumento(), especifico.getDataVencimento(), usuario);
			x++;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ContratosDespesasVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContratosDespesasVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ContratosDespesasVO obj, UsuarioVO usuario) throws Exception {
		try {
			if (obj.getSituacao().equals("IN")) {
				ContratosDespesasVO.validarDados(obj);
				ContratosDespesas.alterar(getIdEntidade(), true, usuario);
				alterar(obj, "ContratosDespesas", new AtributoPersistencia()
						.add("valorParcela", obj.getValorParcela())
						.add("diaVencimento", obj.getDiaVencimento())
						.add("mesVencimento", obj.getMesVencimento())
						.add("situacao", obj.getSituacao())
						.add("contratoIndeterminado", obj.isContratoIndeterminado())
						.add("dataTermino", !obj.isContratoIndeterminado() ? Uteis.getDataJDBC(obj.getDataTermino()) : null)
						.add("dataInicio", Uteis.getDataJDBC(obj.getDataInicio()))
						.add("descricao", obj.getDescricao())
						.add("tipoContrato", obj.getTipoContrato())
						.add("fornecedor", obj.getFornecedor())
						.add("dataAprovacao", Uteis.getDataJDBCTimestamp(obj.getDataAprovacao()))
						.add("dataPrimeiraParcela", Uteis.getDataJDBC(obj.getDataPrimeiraParcela()))
						.add("responsavelAprovacao", obj.getResponsavelAprovacao())
						.add("funcionario", obj.getFuncionario())
						.add("unidadeEnsino", obj.getUnidadeEnsino())
						.add("tipoSacado", obj.getTipoSacado())
						.add("banco", obj.getBanco()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
				
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(obj.getListaCentroResultadoOrigemVOs(), obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.CONTRATO_DESPESA, false, usuario, false);
				getFacadeFactory().getContratoDespesaEspecificoFacade().alterarContratoDespesaEspecificos(obj.getCodigo(), obj.getContratoDespesaEspecificoVOs(), usuario);
				getFacadeFactory().getContaPagarFacade().excluirContasPagarTipoOrigemCodigoOrigem("CD", obj.getCodigo().toString(), "AP", obj.getDataTermino(), usuario);
			} else {
				throw new Exception("O único campo que pode ser alterado é a situaçao do contrato");
			}
		} catch (Exception e) {
			if (e.getMessage().contains("(Competência Fechada)")) {
				// Se tivemos um erro de bloqueio de competencia (seja na conta a receber ou outra entidade. Iremos replicar esse
				// bloqueio para a MatriculaPeriodoVO. Assim, o usuário poderá visualizar o botao de liberacao de competencia fechada (FechamentoMes)
				// e seguir o fluxo posteriormente.
				ConsistirException cEx = (ConsistirException)e;
				obj.forcarControleBloqueioCompetencia(e.getMessage(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio().getDataBloqueioVerificada());
			}			
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ContratosDespesasVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContratosDespesasVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ContratosDespesasVO obj, UsuarioVO usuario) throws Exception {
		try {
			ContratosDespesas.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM ContratosDespesas WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getContratoDespesaEspecificoFacade().excluirContratoDespesaEspecificos(obj.getCodigo(), new ArrayList<ContratoDespesaEspecificoVO>(0), usuario);
			getFacadeFactory().getContaPagarFacade().excluirContasPagarTipoOrigemCodigoOrigem("CD", obj.getCodigo().toString(), "AP", !obj.verificarBloqueioCompetenciaFoiLiberado(), usuario);
		} catch (Exception e) {
			if (e.getMessage().contains("(Competência Fechada)")) {
				// Se tivemos um erro de bloqueio de competencia (seja na conta a receber ou outra entidade. Iremos replicar esse
				// bloqueio para a MatriculaPeriodoVO. Assim, o usuário poderá visualizar o botao de liberacao de competencia fechada (FechamentoMes)
				// e seguir o fluxo posteriormente.
				ConsistirException cEx = (ConsistirException)e;
				obj.forcarControleBloqueioCompetencia(e.getMessage(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio(), cEx.getObjetoOrigem().getFechamentoMesVOBloqueio().getDataBloqueioVerificada());
			}			
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratosDespesas</code>
	 * através do valor do atributo <code>identificadorCentroDespesa</code> da
	 * classe <code>CentroDespesa</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratosDespesasVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorIdentificadorCentroDespesaCentroDespesa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ContratosDespesas.* FROM ContratosDespesas, CategoriaDespesa WHERE ContratosDespesas.categoriaDespesa = CategoriaDespesa.codigo and upper( CategoriaDespesa.identificadorCategoriaDespesa ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY CategoriaDespesa.identificadorCategoriaDespesa";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT ContratosDespesas.* FROM ContratosDespesas, CategoriaDespesa WHERE ContratosDespesas.categoriaDespesa = CategoriaDespesa.codigo and upper( CategoriaDespesa.identificadorCategoriaDespesa ) like('" + valorConsulta.toUpperCase() + "%') AND unidadeEnsino = '" + unidadeEnsino.intValue() + "'ORDER BY CategoriaDespesa.identificadorCategoriaDespesa";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratosDespesas</code>
	 * através do valor do atributo <code>identificadorPlanoConta</code> da
	 * classe <code>PlanoConta</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratosDespesasVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorIdentificadorPlanoContaPlanoConta(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ContratosDespesas.* FROM ContratosDespesas, PlanoConta WHERE ContratosDespesas.planoContasCredito = PlanoConta.codigo and upper( PlanoConta.identificadorPlanoConta ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY PlanoConta.identificadorPlanoConta";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT ContratosDespesas.* FROM ContratosDespesas, PlanoConta WHERE ContratosDespesas.planoContasCredito = PlanoConta.codigo and upper( PlanoConta.identificadorPlanoConta ) like('" + valorConsulta.toUpperCase() + "%') AND unidadeEnsino = '" + unidadeEnsino.intValue() + "' ORDER BY PlanoConta.identificadorPlanoConta";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratoDespesa</code>
	 * através do valor do atributo <code>String situacao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratoDespesaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContratosDespesas WHERE upper( situacao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY situacao";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM ContratosDespesas WHERE upper( situacao ) like('" + valorConsulta.toUpperCase() + "%') AND unidadeEnsino = '" + unidadeEnsino.intValue() + "' ORDER BY situacao";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratoDespesa</code>
	 * através do valor do atributo <code>Date dataTermino</code>. Retorna os
	 * objetos com valores pertecentes ao período informado por parâmetro. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratoDespesaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataTermino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContratosDespesas WHERE ((dataTermino >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataTermino <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataTermino";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM ContratosDespesas WHERE ((dataTermino >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataTermino <= '" + Uteis.getDataJDBC(prmFim) + "')) AND unidadeEnsino = '" + unidadeEnsino.intValue() + "' ORDER BY dataTermino";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratoDespesa</code>
	 * através do valor do atributo <code>Date dataInicio</code>. Retorna os
	 * objetos com valores pertecentes ao período informado por parâmetro. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratoDespesaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataInicio(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContratosDespesas WHERE ((dataInicio >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicio <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataInicio";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM ContratosDespesas WHERE ((dataInicio >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicio <= '" + Uteis.getDataJDBC(prmFim) + "')) AND unidadeensino = '" + unidadeEnsino.intValue() + "' ORDER BY dataInicio";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratoDespesa</code>
	 * através do valor do atributo <code>String tipoContrato</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratoDespesaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoContrato(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContratosDespesas WHERE upper( tipoContrato ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoContrato";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM ContratosDespesas WHERE upper( tipoContrato ) like('" + valorConsulta.toUpperCase() + "%') AND unidadeEnsino = '" + unidadeEnsino.intValue() + "' ORDER BY tipoContrato";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratoDespesa</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Fornecedor</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratoDespesaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeFornecedor(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ContratosDespesas.* FROM ContratosDespesas, Fornecedor WHERE ContratosDespesas.fornecedor = Fornecedor.codigo and upper(sem_acentos(Fornecedor.nome) ) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ORDER BY Fornecedor.nome";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT ContratosDespesas.* FROM ContratosDespesas, Fornecedor WHERE ContratosDespesas.fornecedor = Fornecedor.codigo and upper(sem_acentos(Fornecedor.nome) ) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) AND unidadeensino = '" + unidadeEnsino.intValue() + "' ORDER BY Fornecedor.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeFuncionario(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ContratosDespesas.* FROM ContratosDespesas, Funcionario, Pessoa WHERE ContratosDespesas.Funcionario = Funcionario.codigo and Pessoa.codigo = Funcionario.pessoa and upper( sem_acentos(Pessoa.nome) ) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ORDER BY Fornecedor.nome";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT ContratosDespesas.* FROM ContratosDespesas, Funcionario, Pessoa WHERE ContratosDespesas.Funcionario = Funcionario.codigo and Pessoa.codigo = Funcionario.pessoa and upper( sem_acentos(Pessoa.nome) ) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) AND unidadeensino = '" + unidadeEnsino.intValue() + "' ORDER BY Fornecedor.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeFavorecido(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "select contratosdespesas.* " + "from contratosdespesas " + "left join fornecedor on fornecedor.codigo = contratosdespesas.fornecedor " + "left join funcionario on funcionario.codigo = contratosdespesas.funcionario " + "left join pessoa on funcionario.pessoa = pessoa.codigo " + "left join banco on banco.codigo = contratosdespesas.banco " + "where (upper(sem_acentos(banco.nome))  like (sem_acentos('" + valorConsulta.toUpperCase() + "%')) or upper(sem_acentos(fornecedor.nome)) like (sem_acentos('" + valorConsulta.toUpperCase() + "%')) or upper(sem_acentos(pessoa.nome)) like (sem_acentos('" + valorConsulta.toUpperCase() + "%')))";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " AND unidadeensino = '" + unidadeEnsino.intValue() + "' ORDER BY Fornecedor.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContratosDespesas</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratosDespesasVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContratosDespesas WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM ContratosDespesas WHERE codigo >= " + valorConsulta.intValue() + " AND unidadeEnsino = '" + unidadeEnsino.intValue() + "' ORDER BY codigo";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ContratosDespesasVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ContratosDespesasVO</code>.
	 * 
	 * @return O objeto da classe <code>ContratosDespesasVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ContratosDespesasVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ContratosDespesasVO obj = new ContratosDespesasVO();
		obj.setValorParcela(new Double(dadosSQL.getDouble("valorParcela")));
		obj.setDiaVencimento(dadosSQL.getString("diaVencimento"));
		obj.setMesVencimento(dadosSQL.getString("mesVencimento"));
		obj.setMotivoCancelamento(dadosSQL.getString("motivoCancelamento"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setContratoIndeterminado(new Boolean(dadosSQL.getBoolean("contratoIndeterminado")));
		obj.setDataTermino(dadosSQL.getDate("dataTermino"));
		obj.setDataInicio(dadosSQL.getDate("dataInicio"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setTipoContrato(dadosSQL.getString("tipoContrato"));
		obj.getFornecedor().setCodigo(new Integer(dadosSQL.getInt("fornecedor")));
		obj.setDataAprovacao(Uteis.getDataJDBCTimestamp(dadosSQL.getTimestamp("dataAprovacao")));
		obj.setDataCancelamento(dadosSQL.getDate("dataCancelamento"));
		obj.setDataPrimeiraParcela(dadosSQL.getDate("dataPrimeiraParcela"));
		obj.getResponsavelAprovacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAprovacao")));
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getFuncionario().setCodigo(new Integer(dadosSQL.getInt("funcionario")));
		obj.getBanco().setCodigo(new Integer(dadosSQL.getInt("banco")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.setTipoSacado(dadosSQL.getString("tipoSacado"));		
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosBanco(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);		
		montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosResponsavelAprovacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelCancelamento(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);

		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>FornecedorVO</code> relacionado ao objeto
	 * <code>ContratoDespesaVO</code>. Faz uso da chave primária da classe
	 * <code>FornecedorVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosBanco(ContratosDespesasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBanco().getCodigo().intValue() == 0) {
			obj.setBanco(new BancoVO());
			return;
		}
		obj.setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(obj.getBanco().getCodigo(), false, nivelMontarDados, usuario));
	}

	

	public static void montarDadosFuncionario(ContratosDespesasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionario().getCodigo().intValue() == 0) {
			obj.setFuncionario(new FuncionarioVO());
			return;
		}
		obj.setFuncionario(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), 0, false, nivelMontarDados, usuario));
	}

	public static void montarDadosFornecedor(ContratosDespesasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFornecedor().getCodigo().intValue() == 0) {
			obj.setFornecedor(new FornecedorVO());
			return;
		}
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavelAprovacao(ContratosDespesasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAprovacao().getCodigo().intValue() == 0) {
			obj.setResponsavelAprovacao(new UsuarioVO());
			return;
		}
		obj.setResponsavelAprovacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAprovacao().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavelCancelamento(ContratosDespesasVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelCancelamento().getCodigo().intValue() == 0) {
			obj.setResponsavelCancelamento(new UsuarioVO());
			return;
		}
		obj.setResponsavelCancelamento(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCancelamento().getCodigo(), nivelMontarDados, usuario));
	}
	

	/*
	 * public static void montarDadosContaCorrente(ContratosDespesasVO obj, int
	 * nivelMontarDados) throws Exception { if
	 * (obj.getContaCorrente().getCodigo().intValue() == 0) {
	 * obj.setContaCorrente(new ContaCorrenteVO()); return; }
	 * obj.setContaCorrente
	 * (getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria
	 * (obj.getContaCorrente().getCodigo(), nivelMontarDados)); }
	 * 
	 * /** Operação responsável por montar os dados de um objeto da classe
	 * <code>PlanoContaVO</code> relacionado ao objeto
	 * <code>ContratosDespesasVO</code>. Faz uso da chave primária da classe
	 * <code>PlanoContaVO</code> para realizar a consulta.
	 * 
	 * @param obj Objeto no qual será montado os dados consultados.
	 */
	/*
	 * public static void montarDadosPlanoContasDebito(ContratosDespesasVO obj,
	 * int nivelMontarDados) throws Exception { if
	 * (obj.getPlanoContasDebito().getCodigo().intValue() == 0) {
	 * obj.setPlanoContasDebito(new PlanoContaVO()); return; }
	 * obj.setPlanoContasDebito
	 * (getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria
	 * (obj.getPlanoContasDebito().getCodigo(), nivelMontarDados)); }
	 * 
	 * /** Operação responsável por montar os dados de um objeto da classe
	 * <code>PlanoContaVO</code> relacionado ao objeto
	 * <code>ContratosDespesasVO</code>. Faz uso da chave primária da classe
	 * <code>PlanoContaVO</code> para realizar a consulta.
	 * 
	 * @param obj Objeto no qual será montado os dados consultados.
	 */
	/*
	 * public static void montarDadosPlanoContasCredito(ContratosDespesasVO obj,
	 * int nivelMontarDados) throws Exception { if
	 * (obj.getPlanoContasCredito().getCodigo().intValue() == 0) {
	 * obj.setPlanoContasCredito(new PlanoContaVO()); return; }
	 * obj.setPlanoContasCredito
	 * (getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria
	 * (obj.getPlanoContasCredito().getCodigo(), nivelMontarDados)); }
	 * 
	 * /** Operação responsável por localizar um objeto da classe
	 * <code>ContratosDespesasVO</code> através de sua chave primária.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou localização do
	 * objeto procurado.
	 */
	public ContratosDespesasVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ContratosDespesas WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ContratosDespesas ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ContratosDespesas.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ContratosDespesas.idEntidade = idEntidade;
	}
	
	@Override
	public void consultar(DataModelo dataModelo, String situacaoContrato, String tipoContrato, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception{
		StringBuilder sql  = new StringBuilder("select contratosDespesas.*, unidadeensino.nome as nomeunidadeensino, fornecedor.nome as nomefornecedor, pessoa.nome as nomepessoa, banco.nome as nomebanco ");
		StringBuilder sqlCount  = new StringBuilder("select count(ContratosDespesas.codigo) as qtde ");
		StringBuilder sqlPadrao  = new StringBuilder(" from ContratosDespesas ");
		sqlPadrao.append(" inner join unidadeensino on unidadeensino.codigo = ContratosDespesas.unidadeensino ");
		sqlPadrao.append(" left join fornecedor on fornecedor.codigo = ContratosDespesas.fornecedor ");
		sqlPadrao.append(" left join funcionario on funcionario.codigo = ContratosDespesas.funcionario ");
		sqlPadrao.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
		sqlPadrao.append(" left join banco on banco.codigo = ContratosDespesas.banco ");
		sqlPadrao.append(" where ");
		if(dataModelo.getCampoConsulta().equals("favorecido")) {
			if(dataModelo.getValorConsulta().trim().isEmpty()) {
				sqlPadrao.append(" 1 = 1 ");
			}else {
				sqlPadrao.append(" (sem_acentos(fornecedor.nome) ilike (sem_acentos(?)) or sem_acentos(pessoa.nome) ilike (sem_acentos(?))  or sem_acentos(banco.nome) ilike (sem_acentos(?))) ");
			}
		}else if(dataModelo.getCampoConsulta().equals("consultarPorIdentificadorCategoriaDespesa")) {
			sqlPadrao.append(" exists (select centroresultadoorigem.codigo from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
			sqlPadrao.append(" where sem_acentos(categoriadespesa.identificadorCategoriaDespesa) ilike(sem_acentos(?)) ");
			sqlPadrao.append(" and centroresultadoorigem.tipocentroresultadoorigem = '").append(TipoCentroResultadoOrigemEnum.CONTRATO_DESPESA.name()).append("'  ");
			sqlPadrao.append(" and centroresultadoorigem.codorigem = ContratosDespesas.codigo::varchar) ");
		}else if(dataModelo.getCampoConsulta().equals("consultarPordescricaoCategoriaDespesa")) {
			sqlPadrao.append(" exists (select centroresultadoorigem.codigo from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
			sqlPadrao.append(" where sem_acentos(categoriadespesa.descricao) ilike(sem_acentos(?)) ");
			sqlPadrao.append(" and centroresultadoorigem.tipocentroresultadoorigem = '").append(TipoCentroResultadoOrigemEnum.CONTRATO_DESPESA.name()).append("'  ");
			sqlPadrao.append(" and centroresultadoorigem.codorigem = ContratosDespesas.codigo::varchar) ");
			
		}
		
		if(dataModelo.getCampoConsulta().equals("codigo") ) {
			if(!dataModelo.getValorConsulta().trim().isEmpty()) {
				sqlPadrao.append(" contratosDespesas.codigo = ? ");
			}else {
				sqlPadrao.append(" 1 = 1 ");
			}
		}
		if(Uteis.isAtributoPreenchido(dataModelo.getUnidadeEnsinoVO())) {
			sqlPadrao.append(" and unidadeensino.codigo =  ").append(dataModelo.getUnidadeEnsinoVO().getCodigo());
		}		
		if(!dataModelo.getCampoConsulta().equals("codigo")) {
			sqlPadrao.append(" and ").append(realizarGeracaoWherePeriodo(dataModelo.getDataIni(), dataModelo.getDataFim(), "ContratosDespesas.dataAprovacao", false));
			if(Uteis.isAtributoPreenchido(situacaoContrato)) {
				sqlPadrao.append(" and contratosDespesas.situacao = '").append(situacaoContrato).append("'");
			}
			if(Uteis.isAtributoPreenchido(tipoContrato)) {
				sqlPadrao.append(" and contratosDespesas.tipoContrato = '").append(tipoContrato).append("'");
			}
		}
		sqlCount.append(sqlPadrao);
		sql.append(sqlPadrao);
		if(!dataModelo.getCampoConsulta().equals("codigo")) {
			sql.append(" order by ContratosDespesas.dataAprovacao, coalesce(fornecedor.nome, '')||coalesce(pessoa.nome, '')||coalesce(banco.nome, '') ");
		}else {
			sql.append(" order by ContratosDespesas.codigo ");
		}
		sql.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
		SqlRowSet rs =  null;
		SqlRowSet rsCount =  null;
		if(dataModelo.getCampoConsulta().equals("favorecido")) {
			if(dataModelo.getValorConsulta().trim().isEmpty()) {
				rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				rsCount = getConexao().getJdbcTemplate().queryForRowSet(sqlCount.toString());
			}else {
				rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getValorConsulta()+"%", dataModelo.getValorConsulta()+"%", dataModelo.getValorConsulta()+"%");
				rsCount = getConexao().getJdbcTemplate().queryForRowSet(sqlCount.toString(), dataModelo.getValorConsulta()+"%", dataModelo.getValorConsulta()+"%", dataModelo.getValorConsulta()+"%");
			}
		}else if(dataModelo.getCampoConsulta().equals("consultarPorIdentificadorCategoriaDespesa") || dataModelo.getCampoConsulta().equals("consultarPordescricaoCategoriaDespesa")) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getValorConsulta()+"%");
			rsCount = getConexao().getJdbcTemplate().queryForRowSet(sqlCount.toString(), dataModelo.getValorConsulta()+"%");
		}else {
			if(!Uteis.getIsValorNumerico(dataModelo.getValorConsulta())) {
				throw new Exception("Para o filtro CÓDIGO informe apenas números.");
			}
			if(dataModelo.getValorConsulta().trim().isEmpty()) {
				rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				rsCount = getConexao().getJdbcTemplate().queryForRowSet(sqlCount.toString());
			}else {
				rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getCampoConsulta().equals("codigo") ? Integer.parseInt(dataModelo.getValorConsulta()) : dataModelo.getValorConsulta());
				rsCount = getConexao().getJdbcTemplate().queryForRowSet(sqlCount.toString(), dataModelo.getCampoConsulta().equals("codigo") ? Integer.parseInt(dataModelo.getValorConsulta()) : dataModelo.getValorConsulta());
			}
		}		
		List<ContratosDespesasVO> contratosDespesasVOs =  new ArrayList<ContratosDespesasVO>(0);
		ContratosDespesasVO contratosDespesasVO =  null;
		while(rs.next()) {
			contratosDespesasVO = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO);
			contratosDespesasVO.getUnidadeEnsino().setNome(rs.getString("nomeunidadeEnsino"));
			if(contratosDespesasVO.getTipoSacado().equals("FU")) {
				contratosDespesasVO.getFuncionario().getPessoa().setNome(rs.getString("nomepessoa"));
			}else if(contratosDespesasVO.getTipoSacado().equals("FO")) {
				contratosDespesasVO.getFornecedor().setNome(rs.getString("nomefornecedor"));
			}else if(contratosDespesasVO.getTipoSacado().equals("BA")) {
				contratosDespesasVO.getBanco().setNome(rs.getString("nomebanco"));
			}
			contratosDespesasVOs.add(contratosDespesasVO);
		}
		
		dataModelo.setListaConsulta(contratosDespesasVOs);
		if(rsCount.next()) {
			dataModelo.setTotalRegistrosEncontrados(rsCount.getInt("qtde"));
		}else {
			dataModelo.setTotalRegistrosEncontrados(0);
		}	
	}
}
