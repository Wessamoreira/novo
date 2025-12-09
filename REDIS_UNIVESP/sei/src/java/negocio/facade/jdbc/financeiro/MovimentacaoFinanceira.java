package negocio.facade.jdbc.financeiro;

import static negocio.comuns.utilitarias.Uteis.limitOffset;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraItemVO;
import negocio.comuns.financeiro.MovimentacaoFinanceiraVO;
import negocio.comuns.financeiro.UnidadeEnsinoContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.MovimentacaoFinanceiraInterfaceFacade;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MovimentacaoFinanceiraVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>MovimentacaoFinanceiraVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see MovimentacaoFinanceiraVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class MovimentacaoFinanceira extends ControleAcesso implements MovimentacaoFinanceiraInterfaceFacade {

	private static final long serialVersionUID = 8752991520788985615L;

	protected static String idEntidade;

    public MovimentacaoFinanceira() throws Exception {
        super();
        setIdEntidade("MovimentacaoFinanceira");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>MovimentacaoFinanceiraVO</code>.
     */
    public MovimentacaoFinanceiraVO novo() throws Exception {
        MovimentacaoFinanceira.incluir(getIdEntidade());
        MovimentacaoFinanceiraVO obj = new MovimentacaoFinanceiraVO();
        return obj;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(MovimentacaoFinanceiraVO obj, UsuarioVO usuario, boolean permitiRealizarMovimentacaoCaixaFechado) throws Exception {

    	
    	FluxoCaixaVO fluxoCaixaContaOrigemAbertoHoje = atualizarFluxoCaixa(obj.getContaCorrenteOrigem(), usuario);
    	
    	FluxoCaixaVO fluxoCaixaContaDestinoAbertoHoje = atualizarFluxoCaixa(obj.getContaCorrenteDestino(), usuario);

    	if (obj.isNovoObj().booleanValue()) {
			getFacadeFactory().getMovimentacaoFinanceiraFacade().incluir(obj, usuario);
		} else {
			getFacadeFactory().getMovimentacaoFinanceiraFacade().alterar(obj, usuario);
		}

    	if (Uteis.isAtributoPreenchido(fluxoCaixaContaOrigemAbertoHoje)) {
    		getFacadeFactory().getFluxoCaixaFacade().fecharCaixa(fluxoCaixaContaOrigemAbertoHoje, usuario);
    	}

    	if (Uteis.isAtributoPreenchido(fluxoCaixaContaDestinoAbertoHoje)) {
    		getFacadeFactory().getFluxoCaixaFacade().fecharCaixa(fluxoCaixaContaDestinoAbertoHoje, usuario);
    	}
	}

    /**
     * Este metodo atualiza o {@link FluxoCaixaVO}.
     * Caso o Fluxo de caixa esteja aberto fora da data atual(Anterior a data) o mesmo sera fechado é
     * sera aberto um novo fluxo de caixa com a data atual.
     * 
     * @param obj
     * @param usuario
     * @param fluxoCaixaContaCorrenteAbertoHoje
     * @param contaCorrenteVO
     * @return
     * @throws Exception
     */
	public FluxoCaixaVO atualizarFluxoCaixa(ContaCorrenteVO contaCorrenteVO, UsuarioVO usuario) throws Exception {

		FluxoCaixaVO fluxoCaixaContaCorrenteAbertoHoje = new FluxoCaixaVO();
		
		FluxoCaixaVO fluxoCaixaAbertoEmDataRetroativa = getFacadeFactory().getFluxoCaixaFacade().consultarPorFluxoCaixaAbertoEmDataRetroativa(new Date(), contaCorrenteVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

    	if (Uteis.isAtributoPreenchido(fluxoCaixaAbertoEmDataRetroativa)) {
    		getFacadeFactory().getFluxoCaixaFacade().fecharCaixa(fluxoCaixaAbertoEmDataRetroativa, usuario);
    	}

    	Boolean fluxoCaixaAbertoDataAtual = getFacadeFactory().getFluxoCaixaFacade().validarExistenciaFluxoCaixaAbertoDataAtual(new Date(), contaCorrenteVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

    	if (!fluxoCaixaAbertoDataAtual && Uteis.isAtributoPreenchido(contaCorrenteVO)) {
    		fluxoCaixaContaCorrenteAbertoHoje.setContaCaixa(contaCorrenteVO);
    		fluxoCaixaContaCorrenteAbertoHoje.setResponsavelAbertura(usuario);
    		getFacadeFactory().getFluxoCaixaFacade().atualizarDadosSaldoInicial(fluxoCaixaContaCorrenteAbertoHoje,  usuario);
    		getFacadeFactory().getFluxoCaixaFacade().incluir(fluxoCaixaContaCorrenteAbertoHoje, usuario);
    	}

		return fluxoCaixaContaCorrenteAbertoHoje;
	}
    
    @Override
   	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
   	public void persistir(final MovimentacaoFinanceiraVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
   		if (obj.getCodigo() == 0) {
   			incluir(obj,  usuarioVO);
   		} else {
   			alterar(obj,  usuarioVO);
   		}
   	}
    
    public void validarBloqueioMovimentacaoFinanceira(final MovimentacaoFinanceiraVO obj, String operacao, UsuarioVO usuario) throws Exception {
        Boolean existeSomenteContaCaixa = Boolean.TRUE;
        
        if (!obj.isSomenteContaDestino()) {
        	if (obj.getContaCorrenteOrigem().getContaCaixa()) {
    			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, operacao, obj.getData(), null, obj.getUnidadeEnsinoVO().getCodigo(), obj.getContaCorrenteOrigem().getCodigo(), TipoOrigemHistoricoBloqueioEnum.MOVIMENTACAOFINANCEIRA, usuario);
        	} else {
        		existeSomenteContaCaixa = Boolean.FALSE;
        	}
        } 
        if (obj.getContaCorrenteDestino().getContaCaixa()) {
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, operacao, obj.getData(), null, obj.getUnidadeEnsinoVO().getCodigo(), obj.getContaCorrenteDestino().getCodigo(), TipoOrigemHistoricoBloqueioEnum.MOVIMENTACAOFINANCEIRA, usuario);
        } else {
    		existeSomenteContaCaixa = Boolean.FALSE;
        }
        if (!existeSomenteContaCaixa) {
        	verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, operacao, obj.getData(), null, obj.getUnidadeEnsinoVO().getCodigo(), null, TipoOrigemHistoricoBloqueioEnum.MOVIMENTACAOFINANCEIRA, usuario);
        }
    }
    
    private void validarConciliacaoBancaria(final MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception {
		if(!obj.isDesconsiderandoContabilidadeConciliacao()) {
			for (MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO : obj.getMovimentacaoFinanceiraItemVOs()) {
		        if ((movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()))
		                && obj.getContaCorrenteOrigem().getCodigo()>0 && !obj.getContaCorrenteOrigem().getContaCaixa()) {
		        	if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(obj.getData(), obj.getContaCorrenteOrigem().getNumero(), usuario)) {
						throw new Exception("Não é possível realizar a movimentação financeira na conta corrente origem " + obj.getContaCorrenteOrigem().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(obj.getData()) + ", pois a conciliação bancária já está finalizada.");
					}
		        }
		        if ((movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()))
		                && obj.getContaCorrenteDestino().getCodigo()>0 && !obj.getContaCorrenteDestino().getContaCaixa()) {
		        	if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(obj.getData(), obj.getContaCorrenteDestino().getNumero(), usuario)) {
						throw new Exception("Não é possível realizar a movimentação financeira na conta corrente destino " + obj.getContaCorrenteDestino().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(obj.getData()) + ", pois a conciliação bancária já está finalizada.");
					}
		        }							
			}
		}
	}
    
    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>MovimentacaoFinanceiraVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>MovimentacaoFinanceiraVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception {
        try {
            MovimentacaoFinanceiraVO.validarDados(obj);
            validarSaldoDinheiroCheque(obj, usuario);
            MovimentacaoFinanceira.incluir(getIdEntidade(), true, usuario);            
            validarBloqueioMovimentacaoFinanceira(obj, "INCLUIR", usuario);
            validarConciliacaoBancaria(obj, usuario);
        	incluir(obj, "MovimentacaoFinanceira", new AtributoPersistencia()
					.add("data", obj.getData())
					.add("responsavel", obj.getResponsavel())
					.add("valor", obj.getValor())
					.add("contaCorrenteOrigem", obj.getContaCorrenteOrigem())
					.add("contaCorrenteDestino", obj.getContaCorrenteDestino())
					.add("descricao", obj.getDescricao())
					.add("situacao", obj.getSituacao())
					.add("motivoRecusa", obj.getMotivoRecusa())
					.add("unidadeEnsino", obj.getUnidadeEnsinoVO())
					.add("desconsiderandoContabilidadeConciliacao", obj.isDesconsiderandoContabilidadeConciliacao())
					,usuario);
            getFacadeFactory().getMovimentacaoFinanceiraItemFacade().incluirMovimentacaoFinanceiraItems(obj, usuario);
            if(obj.isSituacaoFinalizada() && !obj.isDesconsiderandoContabilidadeConciliacao()) {
            	getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorMovimentacaoFinanceira(obj, false, usuario);
            }
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {        	
            obj.setNovoObj(true);
            obj.setCodigo(0);
            throw e;
        }
    }

    public void validarSaldoDinheiroCheque(MovimentacaoFinanceiraVO obj, UsuarioVO usuarioVO) throws Exception {
    	if (obj.isSomenteContaDestino() == false) {
    		obj.setContaCorrenteOrigem(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteOrigem().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, null));
    		if (obj.getContaCorrenteOrigem().getContaCaixa()) {
    			Double valorTotalCheque = 0.0;
    			Double valorTotalDinheiro = 0.0;
    			Double valorTotalChequeMovimentacaoItem = 0.0;
    			
    			FluxoCaixaVO fluxoCaixa = getFacadeFactory().getFluxoCaixaFacade().consultarPorFluxoCaixaAberto(new Date(), obj.getContaCorrenteOrigem().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
    	        if (fluxoCaixa == null) {
    	            throw new Exception("Não existe um fluxo de caixa aberto para a conta caixa (" + obj.getContaCorrenteOrigem().getNumero() + "-" + obj.getContaCorrenteOrigem().getDigito() + ")");
    	        }

    	        valorTotalCheque = getFacadeFactory().getFluxoCaixaFacade().executarConsultaValorRecebidoChequeSaldoFechamentoCaixa(fluxoCaixa.getContaCaixa().getCodigo(), fluxoCaixa.getDataAbertura(), fluxoCaixa.getDataFechamento(), fluxoCaixa.getCodigo(), usuarioVO.getCodigo());
    	        valorTotalDinheiro = getFacadeFactory().getFluxoCaixaFacade().executarConsultaValorRecebidoDinheiroSaldoFechamentoCaixa(fluxoCaixa.getContaCaixa().getCodigo(), fluxoCaixa.getDataAbertura(), fluxoCaixa.getDataFechamento(), fluxoCaixa.getCodigo(), usuarioVO.getCodigo());
//    			List cheques = getFacadeFactory().getChequeFacade().consultarPorLocalizacaoSituacaoCheque(obj.getContaCorrenteOrigem().getCodigo(), "EC", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
//    			Iterator i = cheques.iterator();
//    			while (i.hasNext()) {
//    				ChequeVO cheque = (ChequeVO) i.next();
//    				valorTotalCheque = valorTotalCheque + cheque.getValor();
//    			}
//    			Double valorTotalDinheiro = Uteis.arrendondarForcando2CadasDecimais(obj.getContaCorrenteOrigem().getSaldo() - valorTotalCheque);
    			Iterator<MovimentacaoFinanceiraItemVO> j = obj.getMovimentacaoFinanceiraItemVOs().iterator();
    			while (j.hasNext()) {
    				MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO = (MovimentacaoFinanceiraItemVO) j.next();
    				if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor())) {
    					if (Uteis.arrendondarForcando2CadasDecimais(movimentacaoFinanceiraItemVO.getValor().doubleValue()) > Uteis.arrendondarForcando2CadasDecimais(valorTotalDinheiro)) {
    						throw new Exception("O saldo em dinheiro no caixa é insuficiente para realização da movimentação financeira");
    					}
    				} else if (movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())) {
    					valorTotalChequeMovimentacaoItem = valorTotalChequeMovimentacaoItem + movimentacaoFinanceiraItemVO.getValor().doubleValue();
    				}
    			}
    			if (Uteis.arrendondarForcando2CadasDecimais(valorTotalChequeMovimentacaoItem) > Uteis.arrendondarForcando2CadasDecimais(valorTotalCheque)) {
    				throw new Exception("O saldo em cheque no caixa é insuficiente para realização da movimentação financeira");
    			}
    		}
    	}
    }
    
    public void alterar(final MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception {
    	this.alterar(obj, usuario, true);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MovimentacaoFinanceiraVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>MovimentacaoFinanceiraVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MovimentacaoFinanceiraVO obj, UsuarioVO usuario, boolean validarCaixaAbertoHoje) throws Exception {
        try {
            MovimentacaoFinanceira.alterar(getIdEntidade(), true, usuario);
            MovimentacaoFinanceiraVO.validarDados(obj);
            
            validarBloqueioMovimentacaoFinanceira(obj, "ALTERAR", usuario);
            
    		for (MovimentacaoFinanceiraItemVO movimentacaoFinanceiraItemVO : obj.getMovimentacaoFinanceiraItemVOs()) {
    			
    	        if ((movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()))
    	                && obj.getContaCorrenteOrigem().getCodigo()>0 && !obj.getContaCorrenteOrigem().getContaCaixa()) {
    	        	if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(obj.getData(), obj.getContaCorrenteOrigem().getNumero(), usuario)) {
    					throw new Exception("Não é possível realizar a movimentação financeira na conta corrente origem " + obj.getContaCorrenteOrigem().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(obj.getData()) + ", pois a conciliação bancária já está finalizada.");
    				}
    	        }
    	        if ((movimentacaoFinanceiraItemVO.getFormaPagamento().getTipo().equals(TipoFormaPagamento.DINHEIRO.getValor()))
    	                && obj.getContaCorrenteDestino().getCodigo()>0 && !obj.getContaCorrenteDestino().getContaCaixa()) {
    	        	if(getFacadeFactory().getConciliacaoContaCorrenteFacade().validarConciliacaoContaCorrenteFinalizada(obj.getData(), obj.getContaCorrenteDestino().getNumero(), usuario)) {
    					throw new Exception("Não é possível realizar a movimentação financeira na conta corrente destino " + obj.getContaCorrenteDestino().getNumero()  + ", na data " + UteisData.getDataAno4Digitos(obj.getData()) + ", pois a conciliação bancária já está finalizada.");
    				}
    	        }			
    										
    		}
            
            final String sql = "UPDATE MovimentacaoFinanceira set data=?, responsavel=?,  valor=?, contaCorrenteOrigem=?, contaCorrenteDestino=?, descricao=?, "
                    + "situacao=?, motivoRecusa=?, unidadeEnsino=?, desconsiderandoContabilidadeConciliacao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setDouble(3, obj.getValor().doubleValue());
                    if (obj.getContaCorrenteOrigem().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getContaCorrenteOrigem().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    if (obj.getContaCorrenteDestino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getContaCorrenteDestino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    sqlAlterar.setString(6, obj.getDescricao());
                    sqlAlterar.setString(7, obj.getSituacao());
                    sqlAlterar.setString(8, obj.getMotivoRecusa());
                    if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getUnidadeEnsinoVO().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    sqlAlterar.setBoolean(10, obj.isDesconsiderandoContabilidadeConciliacao());
                    sqlAlterar.setInt(11, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getMovimentacaoFinanceiraItemFacade().alterarMovimentacaoFinanceiraItems(obj, usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void estornar(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, boolean possuiPermissaoMovimentarCaixaFechado, UsuarioVO usuario) throws Exception {
        try {
            for (Object obj : movimentacaoFinanceiraVO.getMovimentacaoFinanceiraItemVOs()) {
                MovimentacaoFinanceiraItemVO movFinItem = (MovimentacaoFinanceiraItemVO) obj;
                if (movFinItem.getCheque().getCodigo() != 0) {
                	ChequeVO chequeTmp = getFacadeFactory().getChequeFacade().consultarPorChavePrimaria(movFinItem.getCheque().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
                	movFinItem.getCheque().setSituacao(chequeTmp.getSituacao());
                	chequeTmp = null;
	                if (movFinItem.getCheque().getSituacao().equals(SituacaoCheque.DEVOLVIDO_AO_SACADO.getValor())) {
	                	throw new Exception("Não é possível estornar uma movimentação financeira a qual possui cheque(s) devolvidos ao sacado.");
	                }
	                if (movFinItem.getCheque().getCodigo() != 0 && !movFinItem.getCheque().getSituacao().equals(SituacaoCheque.PENDENTE.getValor()) && !movFinItem.getCheque().getSituacao().equals(SituacaoCheque.EM_CAIXA.getValor())) {
	                    throw new Exception("Não é possível estornar uma movimentação financeira a qual possui cheque(s) já baixados; Por favor devolva os cheques para estorno da movimentação financeira.");
	                }
                }
                getFacadeFactory().getMovimentacaoFinanceiraItemFacade().removerMovimentacaoCaixaAlterandoSaldoConta(movimentacaoFinanceiraVO, movFinItem, usuario);
                getFacadeFactory().getLogEstornoMovimentacaoFinanceiraItemFacade().preencherLogEstornoMovimentacaoFinanceira(movimentacaoFinanceiraVO, movFinItem, usuario);
            }
            getFacadeFactory().getFluxoCaixaFacade().alterarSaldoFluxoCaixaPorMovimentacaoCaixaEstornoMovimentacaoFinanceira(movimentacaoFinanceiraVO, possuiPermissaoMovimentarCaixaFechado, usuario);
            getFacadeFactory().getExtratoContaCorrenteFacade().validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA, movimentacaoFinanceiraVO.getCodigo(), movimentacaoFinanceiraVO.isDesconsiderarConciliacaoBancaria(), 0, false, usuario);
            getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(movimentacaoFinanceiraVO.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA, false, usuario);
            excluir(movimentacaoFinanceiraVO, usuario);
        } catch (Exception e) {
            throw e;
        }
    }    
   
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void estornarContabilidadeConciliacao(MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, UsuarioVO usuario) throws Exception {
    	Uteis.checkState(!movimentacaoFinanceiraVO.isSituacaoFinalizada(), "Não é possível desconsiderar a contabilidade e a conciliação de uma movimentação financeira que não esta Finalizada.");
    	getFacadeFactory().getExtratoContaCorrenteFacade().validarExtratoContaCorrenteComVinculoConciliacaoContaCorrenteParaEstorno(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA, movimentacaoFinanceiraVO.getCodigo(), true, 0 , false, usuario);
        getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(movimentacaoFinanceiraVO.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA, false, usuario);
        movimentacaoFinanceiraVO.setDesconsiderandoContabilidadeConciliacao(true);
        alterar(movimentacaoFinanceiraVO, "MovimentacaoFinanceira", 
				new AtributoPersistencia().add("desconsiderandoContabilidadeConciliacao", movimentacaoFinanceiraVO.isDesconsiderandoContabilidadeConciliacao()), 
				new AtributoPersistencia().add("codigo", movimentacaoFinanceiraVO.getCodigo()), usuario);
	}

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>MovimentacaoFinanceiraVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>MovimentacaoFinanceiraVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MovimentacaoFinanceiraVO obj, UsuarioVO usuario) throws Exception {
        try {
        	MovimentacaoFinanceira.excluir(getIdEntidade(), true, usuario);
        	
            validarBloqueioMovimentacaoFinanceira(obj, "EXCLUIR", usuario);
        	
            String sql = "DELETE FROM MovimentacaoFinanceira WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo().intValue()});
            getFacadeFactory().getMovimentacaoFinanceiraItemFacade().excluirMovimentacaoFinanceiraItems(obj.getCodigo(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>MovimentacaoFinanceira</code> através do valor do atributo 
     * <code>numero</code> da classe <code>ContaCorrente</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MovimentacaoFinanceiraVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNumeroContaCorrente(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT MovimentacaoFinanceira.* FROM MovimentacaoFinanceira, ContaCorrente WHERE MovimentacaoFinanceira.contaCorrenteOrigem = ContaCorrente.codigo and upper( ContaCorrente.numero ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY ContaCorrente.numero";
        if (limite != null) {
			sqlStr += " LIMIT " + limite;
			if (offset != null) {
				sqlStr += " OFFSET " + offset;
			}
		}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNumeroContaCorrenteDestino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT MovimentacaoFinanceira.* FROM MovimentacaoFinanceira, ContaCorrente WHERE MovimentacaoFinanceira.contaCorrenteDestino = ContaCorrente.codigo and upper( ContaCorrente.numero ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY ContaCorrente.numero";
        if (limite != null) {
			sqlStr += " LIMIT " + limite;
			if (offset != null) {
				sqlStr += " OFFSET " + offset;
			}
		}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT MovimentacaoFinanceira.* FROM MovimentacaoFinanceira WHERE upper( MovimentacaoFinanceira.situacao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY MovimentacaoFinanceira.data";
        if (limite != null) {
			sqlStr += " LIMIT " + limite;
			if (offset != null) {
				sqlStr += " OFFSET " + offset;
			}
		}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MovimentacaoFinanceira</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Usuario</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>MovimentacaoFinanceiraVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeUsuario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT MovimentacaoFinanceira.* FROM MovimentacaoFinanceira, Usuario WHERE MovimentacaoFinanceira.responsavel  = Usuario.codigo and lower (Usuario.nome) like ('" + valorConsulta.toLowerCase() + "%') ORDER BY Usuario.nome, MovimentacaoFinanceira.data desc";
		if (limite != null) {
			sqlStr += " LIMIT " + limite;
			if (offset != null) {
				sqlStr += " OFFSET " + offset;
			}
		}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MovimentacaoFinanceira</code> através do valor do atributo 
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MovimentacaoFinanceiraVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MovimentacaoFinanceira WHERE ((data >= '" + Uteis.getDataJDBCTimestamp(prmIni) + "') and (data <= '" + Uteis.getDataJDBCTimestamp(prmFim) + "')) ORDER BY data";
        if (limite != null) {
			sqlStr += " LIMIT " + limite;
			if (offset != null) {
				sqlStr += " OFFSET " + offset;
			}
		}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>MovimentacaoFinanceira</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>MovimentacaoFinanceiraVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MovimentacaoFinanceira WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @SuppressWarnings("static-access")
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public boolean validarMovimentacaoFinanceiraExistente(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT codigo FROM MovimentacaoFinanceira  WHERE codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigo });
		return tabelaResultado.next();
	}

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>MovimentacaoFinanceiraVO</code> resultantes da consulta.
     */
    public static List<MovimentacaoFinanceiraVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<MovimentacaoFinanceiraVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>MovimentacaoFinanceiraVO</code>.
     * @return  O objeto da classe <code>MovimentacaoFinanceiraVO</code> com os dados devidamente montados.
     */
    public static MovimentacaoFinanceiraVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        MovimentacaoFinanceiraVO obj = new MovimentacaoFinanceiraVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getTimestamp("data"));
        obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
        obj.setValor(new Double(dadosSQL.getDouble("valor")));
        obj.getContaCorrenteOrigem().setCodigo(new Integer(dadosSQL.getInt("contaCorrenteOrigem")));
        obj.getContaCorrenteDestino().setCodigo(new Integer(dadosSQL.getInt("contaCorrenteDestino")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setMotivoRecusa(dadosSQL.getString("motivoRecusa"));
        obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.setDesconsiderandoContabilidadeConciliacao(dadosSQL.getBoolean("desconsiderandoContabilidadeConciliacao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
            montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            montarDadosContaCorrenteOrigem(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            montarDadosContaCorrenteDestino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
            return obj;
        }
        
        montarDadosContaCorrenteOrigem(obj, nivelMontarDados, usuario);
        montarDadosContaCorrenteDestino(obj, nivelMontarDados, usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
        obj.setMovimentacaoFinanceiraItemVOs(MovimentacaoFinanceiraItem.consultarMovimentacaoFinanceiraItems(obj.getCodigo(), nivelMontarDados, usuario));
        obj.setListaLancamentoContabeisCredito(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA, TipoPlanoContaEnum.CREDITO, false, nivelMontarDados, usuario));
		obj.setListaLancamentoContabeisDebito(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA, TipoPlanoContaEnum.DEBITO, false, nivelMontarDados, usuario));
		 if (obj.getContaCorrenteOrigem().getContaCaixa() && !obj.getContaCorrenteOrigem().getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
	        	obj.setLancamentoContabil(getFacadeFactory().getConfiguracaoContabilFacade().consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino((obj.getContaCorrenteOrigem().getUnidadeEnsinoContaCorrenteVOs().get(0)).getUnidadeEnsino().getCodigo(), usuario));
			} else {
				for (UnidadeEnsinoContaCorrenteVO uecc : obj.getContaCorrenteOrigem().getUnidadeEnsinoContaCorrenteVOs()) {
					obj.setLancamentoContabil(getFacadeFactory().getConfiguracaoContabilFacade().consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino(uecc.getUnidadeEnsino().getCodigo(), usuario));
					if (obj.isLancamentoContabil()) {
						break;
					}
				}
			}
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }

        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosContaCorrenteOrigem(obj, nivelMontarDados, usuario);
        montarDadosContaCorrenteDestino(obj, nivelMontarDados, usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
       
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaCorrenteVO</code> relacionado ao objeto <code>MovimentacaoFinanceiraVO</code>.
     * Faz uso da chave primária da classe <code>ContaCorrenteVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaCorrenteDestino(MovimentacaoFinanceiraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrenteDestino().getCodigo().intValue() == 0) {
            obj.setContaCorrenteDestino(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrenteDestino(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteDestino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ContaCorrenteVO</code> relacionado ao objeto <code>MovimentacaoFinanceiraVO</code>.
     * Faz uso da chave primária da classe <code>ContaCorrenteVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosContaCorrenteOrigem(MovimentacaoFinanceiraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrenteOrigem().getCodigo().intValue() == 0) {
            obj.setContaCorrenteOrigem(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrenteOrigem(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteOrigem().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>MovimentacaoFinanceiraVO</code>.
     * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(MovimentacaoFinanceiraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>MovimentacaoFinanceiraVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public MovimentacaoFinanceiraVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM MovimentacaoFinanceira WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( MovimentacaoFinanceira ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public String consultarDescricaoPorCodigo(Integer codigoPrm) throws Exception {
        //ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT descricao FROM MovimentacaoFinanceira WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (tabelaResultado.next()) {
            return tabelaResultado.getString("descricao");
        }
        return "";
    }

    private StringBuffer getSQLPadraoConsultaCompleta() {
//        StringBuffer sqlStr = new StringBuffer("SELECT matricula.matricula as \"matricula.matricula\", pessoa.nome as \"pessoa.nome\", operadoraCartao.nome as \"operadoraCartao.nome\", ");
        StringBuffer sqlStr = new StringBuffer("SELECT mf.codigo as \"mfcodigo\", mf.data as \"mfdata\", usuario.codigo as \"mfcodigoresponsavel\", ");
        sqlStr.append("usuario.nome as \"mfresponsavel\", cco.codigo as \"ccocodigo\", cco.numero as \"cconumero\", ccopessoa.codigo as \"ccopessoacodigo\", ");
        sqlStr.append("ccopessoa.nome as \"ccoresponsavel\", cco.saldo as \"ccosaldo\",cco.tipocontacorrente as \"ccotipocontacorrente\",ccd.tipocontacorrente as \"ccdtipocontacorrente\", ccd.codigo as \"ccdcodigo\", ccd.numero as \"ccdnumero\", ");
        sqlStr.append("ccdpessoa.codigo as \"ccdpessoacodigo\", ccdpessoa.nome as \"ccdresponsavel\", ccd.saldo as \"ccdsaldo\", mf.valor as \"mfvalor\", ");
        sqlStr.append("mf.situacao as \"mfsituacao\", mf.motivorecusa as \"mfmotivorecusa\", mf.unidadeEnsino,mf.desconsiderandoContabilidadeConciliacao as  \"mf.desconsiderandoContabilidadeConciliacao\" ");
        sqlStr.append("FROM movimentacaoFinanceira mf ");
        sqlStr.append("LEFT JOIN movimentacaofinanceiraitem mfi  ON mfi.movimentacaofinanceira = mf.codigo ");
        sqlStr.append("LEFT JOIN cheque ON mfi.cheque = cheque.codigo ");
        sqlStr.append("LEFT JOIN contaCorrente cco ON cco.codigo = mf.contaCorrenteOrigem ");
        sqlStr.append("LEFT JOIN funcionario ccoresponsavel ON ccoresponsavel.codigo = cco.funcionarioresponsavel ");
        sqlStr.append("LEFT JOIN pessoa ccopessoa ON ccopessoa.codigo = ccoresponsavel.pessoa ");
        sqlStr.append("LEFT JOIN contaCorrente ccd ON ccd.codigo = mf.contaCorrenteDestino ");
        sqlStr.append("LEFT JOIN funcionario ccdresponsavel ON ccdresponsavel.codigo = ccd.funcionarioresponsavel ");
        sqlStr.append("LEFT JOIN pessoa ccdpessoa ON ccdpessoa.codigo = ccdresponsavel.pessoa ");
        sqlStr.append("LEFT JOIN usuario ON usuario.codigo = mf.responsavel ");
        sqlStr.append("LEFT JOIN unidadeEnsino ON unidadeEnsino.codigo = mf.unidadeEnsino ");
        return sqlStr;
    }

    /**
     * Monta a consulta com os filtros escolhidos na tela de MapaPendenciaCartaoCredito.
     */
    public List<MovimentacaoFinanceiraVO> consultarMapaPendenciaPorDataEmissaoResponsavelContaCorrenteSituacao(String situacao, Date dataEmissaoInicial, Date dataEmissaoFinal, String numeroCheque, String nomeSacadoCheque, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List<Object> parametros = new ArrayList<>(0);
        StringBuilder sqlStr = new StringBuilder("(");
        sqlStr.append(getSQLPadraoConsultaCompleta());
        sqlStr.append("WHERE ccopessoa.codigo = ").append(usuario.getPessoa().getCodigo());
        sqlStr.append(" AND mf.data >= '").append(Uteis.getDataJDBC(dataEmissaoInicial)).append(" 00:00:00' AND mf.data <= '").append(Uteis.getDataJDBC(dataEmissaoFinal)).append(" 23:59:59' ");
        if (!situacao.equals("TO")) {
            sqlStr.append("AND mf.situacao = '").append(situacao).append("' ");
        }
        if (Uteis.isAtributoPreenchido(numeroCheque)) {
        	sqlStr.append(" AND lower(cheque.numero) like lower(?) ");
        	parametros.add(numeroCheque + "%");
        }
        if (Uteis.isAtributoPreenchido(nomeSacadoCheque)) {
        	sqlStr.append(" AND lower(cheque.sacado) like lower(?) ");
        	parametros.add(nomeSacadoCheque + "%");
        }
        sqlStr.append("ORDER BY ccdpessoa.nome) ");
        sqlStr.append("UNION ");
        sqlStr.append("(").append(getSQLPadraoConsultaCompleta());
        sqlStr.append("WHERE (ccopessoa.codigo != ").append(usuario.getPessoa().getCodigo()).append(" OR ccopessoa.codigo is null OR mf.responsavel = ").append(usuario.getCodigo()).append(") ");
        sqlStr.append(" AND mf.data >= '").append(Uteis.getDataJDBC(dataEmissaoInicial)).append(" 00:00:00' AND mf.data <= '").append(Uteis.getDataJDBC(dataEmissaoFinal)).append(" 23:59:59' ");
        if (!situacao.equals("TO")) {
            sqlStr.append("AND mf.situacao = '").append(situacao).append("' ");
        }
        if (Uteis.isAtributoPreenchido(numeroCheque)) {
        	sqlStr.append(" AND lower(cheque.numero) like lower(?) ");
        	parametros.add(numeroCheque + "%");
        }
        if (Uteis.isAtributoPreenchido(nomeSacadoCheque)) {
        	sqlStr.append(" AND lower(cheque.sacado) like lower(?) ");
        	parametros.add(nomeSacadoCheque + "%");
        }
        sqlStr.append("ORDER BY ccopessoa.nome) ");
        return (montarDadosListaMapaPendenciaMovimentacaoFinanceira(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametros.toArray()), usuario));
    }

    public List<MovimentacaoFinanceiraVO> montarDadosListaMapaPendenciaMovimentacaoFinanceira(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<MovimentacaoFinanceiraVO> vetResultado = new ArrayList<MovimentacaoFinanceiraVO>(0);
        while (tabelaResultado.next()) {
            MovimentacaoFinanceiraVO obj = new MovimentacaoFinanceiraVO();
            montarDadosMapaPendenciaMovimentacaoFinanceira(obj, tabelaResultado, usuario);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Monta os dados do Objeto MapaPendenciaCartaoCredito.
     */
    private void montarDadosMapaPendenciaMovimentacaoFinanceira(MovimentacaoFinanceiraVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        // Dados do MapaPendenciaMovimentacaoFinanceiraVO
        obj.setCodigo(dadosSQL.getInt("mfcodigo"));
        obj.setData(dadosSQL.getTimestamp("mfdata"));
        obj.setMotivoRecusa(dadosSQL.getString("mfmotivorecusa"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("mfcodigoresponsavel"));
        obj.getResponsavel().setNome(dadosSQL.getString("mfresponsavel"));
        obj.getContaCorrenteOrigem().setCodigo(dadosSQL.getInt("ccocodigo"));
        obj.getContaCorrenteOrigem().setNumero(dadosSQL.getString("cconumero"));
        obj.getContaCorrenteOrigem().getFuncionarioResponsavel().getPessoa().setCodigo(dadosSQL.getInt("ccopessoacodigo"));
        obj.getContaCorrenteOrigem().getFuncionarioResponsavel().getPessoa().setNome(dadosSQL.getString("ccoresponsavel"));
        obj.getContaCorrenteOrigem().setSaldo(dadosSQL.getDouble("ccosaldo"));
        if (Uteis.isAtributoPreenchido(dadosSQL.getString("ccotipocontacorrente"))) {
        	obj.getContaCorrenteOrigem().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("ccotipocontacorrente")));
		}else {
			obj.getContaCorrenteOrigem().setTipoContaCorrenteEnum(null);
		}
        obj.getContaCorrenteDestino().setCodigo(dadosSQL.getInt("ccdcodigo"));
        obj.getContaCorrenteDestino().setNumero(dadosSQL.getString("ccdnumero"));
        obj.getContaCorrenteDestino().getFuncionarioResponsavel().getPessoa().setCodigo(dadosSQL.getInt("ccdpessoacodigo"));
        obj.getContaCorrenteDestino().getFuncionarioResponsavel().getPessoa().setNome(dadosSQL.getString("ccdresponsavel"));
        obj.getContaCorrenteDestino().setSaldo(dadosSQL.getDouble("ccdsaldo"));        
        if (Uteis.isAtributoPreenchido(dadosSQL.getString("ccdtipocontacorrente"))) {
        	obj.getContaCorrenteDestino().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("ccdtipocontacorrente")));
        }else {
        	obj.getContaCorrenteDestino().setTipoContaCorrenteEnum(null);
        }
        obj.setDesconsiderandoContabilidadeConciliacao(dadosSQL.getBoolean("mf.desconsiderandoContabilidadeConciliacao"));
        obj.setValor(dadosSQL.getDouble("mfvalor"));
        obj.setSituacao(dadosSQL.getString("mfsituacao"));
        obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.setNovoObj(Boolean.FALSE);
		if (Uteis.isAtributoPreenchido(obj.getContaCorrenteDestino().getFuncionarioResponsavel().getPessoa())) {
			obj.setResponsavelContaCorrenteDestino(usuario.getPessoa().getCodigo().equals(obj.getContaCorrenteDestino().getFuncionarioResponsavel().getPessoa().getCodigo()));
		} else {
			obj.setResponsavelContaCorrenteDestino(true);
		}
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacao(final MovimentacaoFinanceiraVO movimentacaoFinanceiraVO, final String situacao, UsuarioVO usuario) throws Exception {
    	MovimentacaoFinanceiraVO obj = consultarPorChavePrimaria(movimentacaoFinanceiraVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        validarBloqueioMovimentacaoFinanceira(obj, "ALTERAR", usuario);
    	
        final String sql = "UPDATE MovimentacaoFinanceira set situacao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setString(1, situacao);
                sqlAlterar.setInt(2, obj.getCodigo());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoMotivoRecusa(final Integer codigo, final String situacao, final String motivoRecusa, UsuarioVO usuario) throws Exception {
    	MovimentacaoFinanceiraVO obj = consultarPorChavePrimaria(codigo, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        validarBloqueioMovimentacaoFinanceira(obj, "ALTERAR", usuario);
    	
    	final String sql = "UPDATE MovimentacaoFinanceira set situacao=?, motivoRecusa=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setString(1, situacao);
                sqlAlterar.setString(2, motivoRecusa);
                sqlAlterar.setInt(3, codigo);
                return sqlAlterar;
            }
        });
    }
    
    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MovimentacaoFinanceira.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        MovimentacaoFinanceira.idEntidade = idEntidade;
    }
	
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String imprimirTextoPadrao(MovimentacaoFinanceiraVO mov, ConfiguracaoFinanceiroVO configFinanceiro, ConfiguracaoGeralSistemaVO configGeralSistema, UsuarioVO usuarioLogado) throws Exception {
		String nomeArquivoOrigem = null;
		try {
			nomeArquivoOrigem = executarImpressaoPorTextoPadraoIreport( mov, configFinanceiro, configGeralSistema, usuarioLogado);
		} catch (Exception e) {
			throw e;
		}
		return nomeArquivoOrigem;
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private String executarImpressaoPorTextoPadraoIreport(MovimentacaoFinanceiraVO mov, ConfiguracaoFinanceiroVO configFinanceiro, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		GeradorRelatorio geradorRelatorio = null;
		SuperParametroRelVO superParamentro = null;
		String caminhoRelatorio = null;
		try {
			superParamentro = new SuperParametroRelVO();
			superParamentro.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			if (configFinanceiro.getArquivoIreportMovFin().getCodigo().intValue() > 0) {
				superParamentro.setNomeDesignIreport(config.getLocalUploadArquivoFixo() + File.separator + configFinanceiro.getArquivoIreportMovFin().getPastaBaseArquivo() + File.separator + configFinanceiro.getArquivoIreportMovFin().getNome());
			} else {
				superParamentro.setNomeDesignIreport(getCaminhoClassesAplicacao() + File.separator + "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "template_movfin_ireport.jasper");
			}
			superParamentro.setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
			superParamentro.setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
			superParamentro.setNomeUsuario(usuario.getNome());
			
			superParamentro.getParametros().putAll(preencherParamentros(mov));
			geradorRelatorio = new GeradorRelatorio();
			caminhoRelatorio = geradorRelatorio.realizarExportacaoRelatorio(superParamentro);
			return caminhoRelatorio;
		} finally {
			geradorRelatorio = null;
			superParamentro = null;
			caminhoRelatorio = null;
		}
	}
    
    public HashMap<String, Object> preencherParamentros(MovimentacaoFinanceiraVO obj) throws Exception {
    	HashMap<String, Object> parametrosRel = new HashMap<String, Object>();
    	parametrosRel.put("MovFin_Data", Uteis.getData(obj.getData()));
    	parametrosRel.put("MovFin_Responsavel", obj.getResponsavel().getNome());
    	parametrosRel.put("MovFin_Responsavel_ContaOrigem", obj.getContaCorrenteOrigem().getFuncionarioResponsavel().getPessoa().getNome());
    	parametrosRel.put("MovFin_Responsavel_ContaDestino", obj.getContaCorrenteDestino().getFuncionarioResponsavel().getPessoa().getNome());
    	if(Uteis.isAtributoPreenchido(obj.getContaCorrenteOrigem().getNomeApresentacaoSistema())){
    		parametrosRel.put("MovFin_ContaOrigem", obj.getContaCorrenteOrigem().getNomeApresentacaoSistema());
    	} else {
    		parametrosRel.put("MovFin_ContaOrigem", "Banco:" + obj.getContaCorrenteOrigem().getDescricaoParaComboBox());
    	}
    	if(Uteis.isAtributoPreenchido(obj.getContaCorrenteDestino().getNomeApresentacaoSistema())){
    		parametrosRel.put("MovFin_ContaDestino", obj.getContaCorrenteDestino().getNomeApresentacaoSistema());
    	} else {
    		parametrosRel.put("MovFin_ContaDestino", "Banco:" + obj.getContaCorrenteDestino().getDescricaoParaComboBox());
    	}
    	parametrosRel.put("MovFin_Valor", Uteis.formatarDoubleParaMoeda(obj.getValor()));
    	parametrosRel.put("MovFin_Situacao", obj.getSituacao_Apresentar());
    	parametrosRel.put("MovFin_Descricao", obj.getDescricao());
    	parametrosRel.put("DataAtual_Outras", Uteis.getDiaMesPorExtensoEAno(new Date(), true));
    	parametrosRel.put("ListaItemMovimentacaoFinanceira", obj.getMovimentacaoFinanceiraItemVOs());
    	return parametrosRel;
    }
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLancamentoContabilVO(MovimentacaoFinanceiraVO mov, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
    	ControleAcesso.incluir(PerfilAcessoPermissaoFinanceiroEnum.PERMITIR_ALTERAR_LANCAMENTO_CONTABIL_MOVIMENTACAO_FINANCEIRA.getValor(), usuarioVO);
    	if (!mov.getTotalLancamentoContabeisCreditoTipoValorMov().equals(mov.getTotalLancamentoContabeisDebitoTipoValorMov())) {
			throw new Exception("Os valores dos lançamentos contábeis do tipo Movimentação Financeira não podem ser diferentes.");
		}
		if (mov.getTotalLancamentoContabeisCreditoTipoValorMov() > mov.getValor()) {
			throw new Exception("O Total do Lançamento de Crédito " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(mov.getTotalLancamentoContabeisCreditoTipoValorMov(), ",") + " não podem ser maior que o valor da Movimentação Financeira " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(mov.getValor(), ",") + ".");
		}
		if (mov.getTotalLancamentoContabeisDebitoTipoValorMov() > mov.getValor()) {
			throw new Exception("O Total do Lançamento de Dédito " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(mov.getTotalLancamentoContabeisDebitoTipoValorMov(), ",") + " não podem ser maior que o valor da Movimentação Financeira " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(mov.getValor(), ",") + ".");
		}
		List<LancamentoContabilVO> listaTemp = new ArrayList<>();
		listaTemp.addAll(mov.getListaLancamentoContabeisCredito());
		listaTemp.addAll(mov.getListaLancamentoContabeisDebito());
		  EnumMap<TipoOrigemLancamentoContabilEnum, String> mapaDeletar = new EnumMap(TipoOrigemLancamentoContabilEnum.class);
		mapaDeletar.put(TipoOrigemLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA, "'"+mov.getCodigo().toString()+"'");
		getFacadeFactory().getLancamentoContabilFacade().validarSeLancamentoContabilFoiExcluido(listaTemp, mapaDeletar, usuarioVO);
		for (LancamentoContabilVO lc : listaTemp) {
			getFacadeFactory().getLancamentoContabilFacade().persistir(lc, verificarAcesso, usuarioVO);
		}
	}
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void addLancamentoContabilVO(MovimentacaoFinanceiraVO mov, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(lancamento)) {
			UnidadeEnsinoVO unidadEnsino = null;
			if(mov.getContaCorrenteOrigem().getContaCaixa()){
				unidadEnsino = (UnidadeEnsinoVO)(mov.getContaCorrenteOrigem().getUnidadeEnsinoContaCorrenteVOs().get(0)).getUnidadeEnsino().clone();
			}else{
				for (UnidadeEnsinoContaCorrenteVO uecc : mov.getContaCorrenteOrigem().getUnidadeEnsinoContaCorrenteVOs()) {
					unidadEnsino = uecc.getUnidadeEnsino();
					ConfiguracaoContabilVO conf = getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorCodigoUnidadeEnsino(unidadEnsino.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
					if(Uteis.isAtributoPreenchido(conf)){
						break;	
					}	
				}
			}
			if(!Uteis.isAtributoPreenchido(lancamento.getPlanoContaVO())){
				throw new Exception("O campo Plano de Conta deve ser informado");
			}
			if(!Uteis.isAtributoPreenchido(lancamento.getValor())){
				throw new Exception("O campo Valor deve ser informado");
			}
			getFacadeFactory().getLancamentoContabilFacade().preencherLancamentoContabilPorMovimentacaoFinanceira(unidadEnsino, lancamento, lancamento.getPlanoContaVO(), mov, lancamento.getContaCorrenteVO(), lancamento.getValor(), lancamento.getTipoPlanoConta(), usuario);
		}
		if (lancamento.getTipoPlanoConta().isCredito()) {
			preencherListaLancamentoContabilVO(mov.getListaLancamentoContabeisCredito(), lancamento);
		} else {
			preencherListaLancamentoContabilVO(mov.getListaLancamentoContabeisDebito(), lancamento);
		}
	}

    @Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
    public void preencherListaLancamentoContabilVO(List<LancamentoContabilVO> lista, LancamentoContabilVO lancamento) throws Exception {
		int index = 0;
		for (LancamentoContabilVO objExistente : lista) {
			if (objExistente.equalsCampoSelecaoLista(lancamento)) {
				if (!objExistente.getCodigo().equals(lancamento.getCodigo())) {
					lancamento.setValor(lancamento.getValor() + objExistente.getValor());
				}
				lista.set(index, lancamento);
				return;
			}
			index++;
		}
		lista.add(lancamento);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removeLancamentoContabilVO(MovimentacaoFinanceiraVO mov, LancamentoContabilVO lancamento, UsuarioVO usuario) throws Exception {
		Iterator<LancamentoContabilVO> i = null;
		if (lancamento.getTipoPlanoConta().isCredito()) {
			i = mov.getListaLancamentoContabeisCredito().iterator();
		} else {
			i = mov.getListaLancamentoContabeisDebito().iterator();
		}
		while (i.hasNext()) {
			LancamentoContabilVO objExistente = (LancamentoContabilVO) i.next();
			if (objExistente.equalsCampoSelecaoLista(lancamento)) {
				i.remove();
				return;
			}
		}
	}
	
	@Override
	public Integer consultarTotalRegistrosPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT count (MovimentacaoFinanceira.codigo) FROM MovimentacaoFinanceira WHERE codigo = " + valorConsulta.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		} else {
			return 0;
		}
	}
	
	@Override
	public Integer consultarTotalRegistrosPorNomeUsuario(String valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT count (MovimentacaoFinanceira.codigo) FROM MovimentacaoFinanceira, Usuario WHERE MovimentacaoFinanceira.responsavel  = Usuario.codigo and lower (Usuario.nome) like ('" + valorConsulta.toLowerCase() + "%') ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		} else {
			return 0;
		}
	}
	
	@Override
	public Integer consultarTotalRegistrosPorNumeroContaCorrente(String valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT count (MovimentacaoFinanceira.codigo) FROM MovimentacaoFinanceira, ContaCorrente WHERE MovimentacaoFinanceira.contaCorrenteOrigem = ContaCorrente.codigo and upper( ContaCorrente.numero ) like('" + valorConsulta.toUpperCase() + "%') ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		} else {
			return 0;
		}
	}
	
	@Override
	public Integer consultarTotalRegistrosPorNumeroContaCorrenteDestino(String valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT count (MovimentacaoFinanceira.codigo) FROM MovimentacaoFinanceira, ContaCorrente WHERE MovimentacaoFinanceira.contaCorrenteDestino = ContaCorrente.codigo and upper( ContaCorrente.numero ) like('" + valorConsulta.toUpperCase() + "%') ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		} else {
			return 0;
		}
	}
	
	@Override
	public Integer consultarTotalRegistrosPorSituacao(String valorConsulta,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT count (MovimentacaoFinanceira.codigo) FROM MovimentacaoFinanceira WHERE upper( MovimentacaoFinanceira.situacao ) like('" + valorConsulta.toUpperCase() + "%') ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		} else {
			return 0;
		}
	}
	
	@Override
	public Integer consultarTotalRegistrosPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)  throws Exception{
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT count (MovimentacaoFinanceira.codigo) FROM MovimentacaoFinanceira WHERE ((data >= '" + Uteis.getDataJDBCTimestamp(prmIni) + "') and (data <= '" + Uteis.getDataJDBCTimestamp(prmFim) + "')) ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		} else {
			return 0;
		}
	}
	
    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>MovimentacaoFinanceiraVO</code>.
     * Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(MovimentacaoFinanceiraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, nivelMontarDados, usuario));
    }
    
    public MovimentacaoFinanceiraVO consultarPorCodigoCheque(Integer cheque, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder()
				.append(" SELECT movimentacaofinanceira.* FROM movimentacaofinanceiraitem ")
				.append(" INNER join cheque on movimentacaofinanceiraitem.cheque = cheque.codigo ")
				.append(" INNER join movimentacaofinanceira on movimentacaofinanceiraitem.movimentacaofinanceira = movimentacaofinanceira.codigo ")
				.append(" WHERE cheque.codigo = ? order by movimentacaofinanceira.codigo desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { cheque });
		return tabelaResultado.next() ? montarDados(tabelaResultado, nivelMontarDados, usuario) : null;
	}
    
    public List<MovimentacaoFinanceiraVO> consultarPorNumeroNomeSacadoCheque(String numeroCheque, String nomeSacadoCheque, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sql = new StringBuilder()
				.append(" SELECT movimentacaofinanceira.* FROM movimentacaofinanceiraitem ")
				.append(" INNER join cheque ON movimentacaofinanceiraitem.cheque = cheque.codigo ")
				.append(" INNER join movimentacaofinanceira ON movimentacaofinanceiraitem.movimentacaofinanceira = movimentacaofinanceira.codigo ")
				.append(" WHERE lower(cheque.numero) like lower(?) ")
				.append(" AND lower(cheque.sacado) like lower(?) order by movimentacaofinanceira.codigo desc ")
				.append(limitOffset(limite, offset));
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), numeroCheque + "%", nomeSacadoCheque + "%");
    	return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public Integer consultarTotalRegistrosPorNumeroNomeSacadoCheque(String numeroCheque, String nomeSacadoCheque, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sql = new StringBuilder()
				.append(" SELECT count(distinct movimentacaofinanceira.codigo) FROM movimentacaofinanceiraitem ")
				.append(" INNER join cheque ON movimentacaofinanceiraitem.cheque = cheque.codigo ")
				.append(" INNER join movimentacaofinanceira ON movimentacaofinanceiraitem.movimentacaofinanceira = movimentacaofinanceira.codigo ")
				.append(" WHERE lower(cheque.numero) like lower(?) ")
				.append(" AND lower(cheque.sacado) like lower(?) ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), numeroCheque + "%", nomeSacadoCheque + "%");
    	return tabelaResultado.next() ? new Integer(tabelaResultado.getInt("count")) : 0;
    }
}
