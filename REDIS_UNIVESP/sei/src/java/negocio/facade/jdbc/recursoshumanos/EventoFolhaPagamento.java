package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.script.ScriptEngine;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.recursoshumanos.ContraChequeEventoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FormulaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateEventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.CategoriaEventoFolhaEnum;
import negocio.comuns.recursoshumanos.enumeradores.NaturezaEventoFolhaPagamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoFolhaPagamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoLancamentoFolhaPagamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.EventoFolhaPagamentoInterfaceFacade;

/*Classe de persistencia que encapsula todas as operacoes de manipulacao dos
* dados da classe <code>EventoFolhaPagamentoVO</code>. Responsavel por implementar
* operacoes como incluir, alterar, excluir e consultar pertinentes a classe
* <code>EventoFolhaPagamentoVO</code>. Encapsula toda a interacao com o banco de
* dados.
* 
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class EventoFolhaPagamento extends ControleAcesso implements EventoFolhaPagamentoInterfaceFacade {

	private static final long serialVersionUID = -5732884385625589384L;
	
	protected static String idEntidade;

	public EventoFolhaPagamento() throws Exception {
		super();
		setIdEntidade("EventoFolhaPagamento");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(EventoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		validarDadosDuplicadosIdentificador(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			obj.setDataUltimaAlteracao(new Date());
			obj.setUsuarioUltimaAlteracao(usuarioVO);
			alterar(obj, validarAcesso, usuarioVO);
		}

		getFacadeFactory().getEventoFolhaPagamentoItemInterfaceFacade().persistirTodos(obj.getEventoFolhaPagamentoItemVOs(), obj, usuarioVO);
		getFacadeFactory().getEventoFolhaPagamentoMediaInterfaceFacade().persistirTodos(obj.getEventoMediaVOs(), obj, usuarioVO);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(EventoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoFolhaPagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder(" INSERT INTO eventofolhapagamento ( identificador, descricao, situacao, tipolancamento, tipoevento, ")
					        .append(" formulavalor, formulahora, formuladia, formulareferencia, ordemcalculo, ")
					        .append(" formulafixa, prioridade, inssFolhaNormal, irrfFolhaNormal, fgtsFolhaNormal, ")
					        .append(" dsrFolhaNormal, salarioFamiliaFolhaNormal, irrfFerias, adicionalFerias, inssDecimoTerceiro, ")
					        .append(" irrfDecimoTerceiro, fgtsDecimoTerceiro, folhaPensao, feriasPensao, decimoTerceiroPensao, ")
					        .append(" participacaoLucroPensao, rais, informeRendimento, previdenciaPropria, planoSaude, ")
					        .append(" decimoTerceiroPrevidenciaPropria, decimoTerceiroPlanoSaude, dedutivelIrrf, estornaInss, estornaIrrf, ")
					        .append(" estornaFgts, estornaValeTransporte, estornaSalarioFamilia, estornaIrrfFerias, dedutivelIrrfFerias, ")
					        .append(" estornaInssDecimoTerceiro, estornaFgtsDecimoTerceiro, estornaIrrfDecimoTerceiro, dedutivelIrrfDecimoTerceiro, folhaPensaoDesconto, ")
					        .append(" feriasPensaoDesconto, decimentoTerceiroPensaoDesconto, participacaoLucroPensaoDesconto, previdenciaObrigatoria, admissao, ")
					        .append(" demissao, ferias, afastamento, eventopadrao, valetransporte, ")
					        .append(" valetransportedesconto, incideAdicionalTempoServico, incideAssociacaoSindicato,  agrupamentoFolhaNormal, agrupamentoFerias, ")
					        .append(" agrupamentoFeriasProporcionais, agrupamentoFeriasVencidas, agrupamentoDecimoTerceiro, agrupamentoDecimoTerceiroProporcionais, categoria, ")
					        .append(" naturezaEvento, incideAdicionalTempoServicoFerias, incideAdicionalTempoServico13, incideAdicionalTempoServicoRescisao, inssFerias, ")
					        .append(" previdenciaPropriaFerias, previdenciaObrigatoriaFerias, incideAssociacaoSindicatoFerias, incideplanosaudeferias, permiteDuplicarContraCheque, ")
					        .append(" categoriaDespesa, incideassociacaosindicatodecimoterceiro, incideprevidenciaobrigatoriadecimoterceiro)")
					        .append(" VALUES ( ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ?, ?, ?, ")
					        .append(" ?, ?, ? )")
					        .append(" returning codigo ")
					        .append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					int i = 0;
					
					Uteis.setValuePreparedStatement(obj.getIdentificador(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacao().name(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoLancamento().name(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoEvento().name(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getFormulaValor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFormulaHora(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFormulaDia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFormulaReferencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getOrdemCalculo(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getFormulaFixa(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPrioridade(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInssFolhaNormal(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIrrfFolhaNormal(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFgtsFolhaNormal(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getDsrFolhaNormal(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSalarioFamiliaFolhaNormal(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIrrfFerias(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAdicionalFerias(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInssDecimoTerceiro(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getIrrfDecimoTerceiro(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFgtsDecimoTerceiro(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFolhaPensao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFeriasPensao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDecimoTerceiroPensao(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getParticipacaoLucroPensao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getRais(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInformeRendimento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPrevidenciaPropria(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPlanoSaude(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getDecimoTerceiroPrevidenciaPropria(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDecimoTerceiroPlanoSaude(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDedutivelIrrf() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEstornaInss() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEstornaIrrf() , ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getEstornaFgts() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEstornaValeTransporte() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEstornaSalarioFamilia() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEstornaIrrfFerias() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDedutivelIrrfFerias() , ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getEstornaInssDecimoTerceiro() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEstornaFgtsDecimoTerceiro() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEstornaIrrfDecimoTerceiro() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDedutivelIrrfDecimoTerceiro() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFolhaPensaoDesconto() , ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getFeriasPensaoDesconto() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDecimentoTerceiroPensaoDesconto() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getParticipacaoLucroPensaoDesconto() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPrevidenciaObrigatoria() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAdmissao() , ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getDemissao() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFerias() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAfastamento() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEventoPadrao() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValeTransporte() , ++i, sqlInserir);
										
					Uteis.setValuePreparedStatement(obj.getValeTransporteDesconto() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIncideAdicionalTempoServico() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIncideAssociacaoSindicato() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAgrupamentoFolhaNormal() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAgrupamentoFerias() , ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getAgrupamentoFeriasProporcionais() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAgrupamentoFeriasVencidas() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAgrupamentoDecimoTerceiro() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getAgrupamentoDecimoTerceiroProporcionais() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCategoria() , ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getNaturezaEvento().getValor() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIncideAdicionalTempoServicoFerias() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIncideAdicionalTempoServico13(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIncideAdicionalTempoServicoRescisao() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInssFerias(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getPrevidenciaPropriaFerias() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPrevidenciaObrigatoriaFerias(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIncideAssociacaoSindicatoFerias() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIncidePlanoSaudeFerias() , ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getPermiteDuplicarContraCheque(), ++i, sqlInserir);

					Uteis.setValuePreparedStatement(obj.getCategoriaDespesaVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIncideAssociacaoSindicatoDecimoTerceiro(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIncidePrevidenciaObrigatoriaDecimoTerceiro(), ++i, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw new ConsistirException("Erro ao salvar:"+e.getMessage());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(EventoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder("UPDATE eventofolhapagamento set ")
					        .append(" identificador=?, descricao=?, situacao=?, tipolancamento=?, tipoevento=?, ")
					        .append(" formulavalor=?, formulahora=?, formuladia=?, formulareferencia=?, ordemcalculo=?, ")
					        .append(" formulafixa=?, prioridade=?, inssFolhaNormal=?, irrfFolhaNormal=?, fgtsFolhaNormal=?, ")
					        .append(" dsrFolhaNormal=?, salarioFamiliaFolhaNormal=?, irrfFerias=?, adicionalFerias=?, inssDecimoTerceiro=?, ")
					        .append(" irrfDecimoTerceiro=?, fgtsDecimoTerceiro=?, folhaPensao=?, feriasPensao=?, decimoTerceiroPensao=?, ")
					        .append(" participacaoLucroPensao=?, rais=?, informeRendimento=?, previdenciaPropria=?, planoSaude=?, ")
					        .append(" decimoTerceiroPrevidenciaPropria=?, decimoTerceiroPlanoSaude=?, dedutivelIrrf=?, estornaInss=?, estornaIrrf=?,")
					        .append(" estornaFgts=?, estornaValeTransporte=?, estornaSalarioFamilia=?, estornaIrrfFerias=?, dedutivelIrrfFerias=?, ")
					        .append(" estornaInssDecimoTerceiro=?, estornaFgtsDecimoTerceiro=?, estornaIrrfDecimoTerceiro=?, dedutivelIrrfDecimoTerceiro =?, folhaPensaoDesconto=?, ")
					        .append(" feriasPensaoDesconto=?, decimentoTerceiroPensaoDesconto=?, participacaoLucroPensaoDesconto=?, previdenciaObrigatoria=?, dataultimaalteracao=?, ")
					        .append(" usuarioresponsavelalteracao=?, admissao=?, demissao=?, ferias=?, afastamento=?, ")
					        .append(" eventopadrao=?, valetransporte=?, valetransportedesconto=?, incideAdicionalTempoServico=?, incideAssociacaoSindicato=?, ")
					        .append(" agrupamentoFolhaNormal=?, agrupamentoFerias=?,  agrupamentoFeriasProporcionais=?, agrupamentoFeriasVencidas=?, agrupamentoDecimoTerceiro=?, ")
					        .append(" agrupamentoDecimoTerceiroProporcionais=?, categoria=?, naturezaEvento=?, incideAdicionalTempoServicoFerias=?, incideAdicionalTempoServico13=?, ")
					        .append(" incideAdicionalTempoServicoRescisao=?, inssFerias=?, previdenciaPropriaFerias=?, previdenciaObrigatoriaFerias=?, incideAssociacaoSindicatoFerias=?, ")
					        .append(" incidePlanoSaudeFerias = ?, permiteDuplicarContraCheque = ?, categoriaDespesa = ?, incideassociacaosindicatodecimoterceiro=?, incideprevidenciaobrigatoriadecimoterceiro = ? ")
					        .append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					int i = 0;
					
					Uteis.setValuePreparedStatement(obj.getIdentificador(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSituacao().name(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoLancamento().name(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoEvento().name(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getFormulaValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFormulaHora(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFormulaDia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFormulaReferencia(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getOrdemCalculo(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getFormulaFixa(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPrioridade(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getInssFolhaNormal(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIrrfFolhaNormal(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFgtsFolhaNormal(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getDsrFolhaNormal(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSalarioFamiliaFolhaNormal(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIrrfFerias(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAdicionalFerias(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getInssDecimoTerceiro(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getIrrfDecimoTerceiro(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFgtsDecimoTerceiro(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFolhaPensao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFeriasPensao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDecimoTerceiroPensao(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getParticipacaoLucroPensao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getRais(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getInformeRendimento(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPrevidenciaPropria(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPlanoSaude(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getDecimoTerceiroPrevidenciaPropria(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDecimoTerceiroPlanoSaude() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDedutivelIrrf() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getEstornaInss() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getEstornaIrrf() , ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getEstornaFgts() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getEstornaValeTransporte() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getEstornaSalarioFamilia() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getEstornaIrrfFerias() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDedutivelIrrfFerias() , ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getEstornaInssDecimoTerceiro() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getEstornaFgtsDecimoTerceiro() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getEstornaIrrfDecimoTerceiro() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDedutivelIrrfDecimoTerceiro() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFolhaPensaoDesconto() , ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getFeriasPensaoDesconto() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDecimentoTerceiroPensaoDesconto() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getParticipacaoLucroPensaoDesconto() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPrevidenciaObrigatoria() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataUltimaAlteracao() , ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getUsuarioUltimaAlteracao() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAdmissao() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDemissao() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFerias() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAfastamento() , ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getEventoPadrao() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValeTransporte() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValeTransporteDesconto() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIncideAdicionalTempoServico() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIncideAssociacaoSindicato() , ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getAgrupamentoFolhaNormal() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAgrupamentoFerias() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAgrupamentoFeriasProporcionais() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAgrupamentoFeriasVencidas() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getAgrupamentoDecimoTerceiro() , ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getAgrupamentoDecimoTerceiroProporcionais() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCategoria() , ++i, sqlAlterar);					
					Uteis.setValuePreparedStatement(obj.getNaturezaEvento() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIncideAdicionalTempoServicoFerias() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIncideAdicionalTempoServico13(), ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getIncideAdicionalTempoServicoRescisao() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getInssFerias(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPrevidenciaPropriaFerias() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPrevidenciaObrigatoriaFerias(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIncideAssociacaoSindicatoFerias() , ++i, sqlAlterar);
					
					Uteis.setValuePreparedStatement(obj.getIncidePlanoSaudeFerias() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getPermiteDuplicarContraCheque() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCategoriaDespesaVO() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIncideAssociacaoSindicatoDecimoTerceiro() , ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getIncidePrevidenciaObrigatoriaDecimoTerceiro() , ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			ConsistirException ce = new ConsistirException("Erro ao salvar:"+e.getMessage());
			throw ce;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(EventoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoFolhaPagamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			getFacadeFactory().getEventoIncidenciaFolhaPagamentoInterfaceFacade().excluirPorEvento(obj.getCodigo(), false, usuarioVO);

			StringBuilder sql = new StringBuilder("DELETE FROM eventofolhapagamento WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Metodo chamado sem paginação
	 */
	@Override
	public List<EventoFolhaPagamentoVO> consultarPorFiltro(String campoConsultaEvento, String valorConsultaEvento, String valorConsultaSituacao,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)	throws Exception {
		List<Object> filtros = new ArrayList<>();

		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" WHERE 1 = 1");
		sql.append(" AND efp.situacao like(upper(?))");
		
		if (valorConsultaSituacao.equals("TODOS")) {
			valorConsultaSituacao = "%%";
		}
		filtros.add(valorConsultaSituacao);

		switch (campoConsultaEvento) {
		case "descricao":
			sql.append(" AND upper( efp.descricao ) like(upper(?)) ");
			sql.append(" ORDER BY efp.").append(campoConsultaEvento).append(" ASC ");
			break;
		case "identificador":
			sql.append(" AND upper( efp.identificador ) like(upper(?)) ");
			sql.append(" ORDER BY efp.").append(campoConsultaEvento).append(" DESC ");
			break;
		default:
			break;
		}
		filtros.add("%" + valorConsultaEvento + "%");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());			

		List<EventoFolhaPagamentoVO> eventosDaFolhaDePagamento = new ArrayList<>();
		while (tabelaResultado.next()) {
			eventosDaFolhaDePagamento.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return eventosDaFolhaDePagamento;
	}
	
	/**
	 * Metodo de consulta paginado
	 */
	@Override
	public void consultarPorFiltro(DataModelo dataModelo, String situacao, String tipoLancamento) throws Exception {
		
		List<EventoFolhaPagamentoVO> objs = new ArrayList<>(0);
		dataModelo.getListaFiltros().clear();
		
		switch (dataModelo.getCampoConsulta()) {
		case "descricao":
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			objs = consultarEventoFolhaPagamento(dataModelo, "efp.descricao", situacao, tipoLancamento);
			break;
		case "identificador":
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			objs = consultarEventoFolhaPagamento(dataModelo, "efp.identificador", situacao, tipoLancamento);
			break;
		default:
			break;
		}

		dataModelo.setListaConsulta(objs);
		
	}

	/**
	 * Consulta os eventos paginados com as informações vindas do dataModelo
	 * 
	 * @param dataModelo
	 * @param campoConsulta
	 * @return
	 * @throws Exception
	 */
	private List<EventoFolhaPagamentoVO> consultarEventoFolhaPagamento(DataModelo dataModelo,  String campoConsulta, String situacao, String tipoLancamento) throws Exception {

		EventoFolhaPagamento.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" WHERE 1 = 1");

		if (campoConsulta.equals("efp.descricao")) {
			sql.append(" AND upper( efp.descricao ) like(upper(sem_acentos(?))) ");
		} else {
			sql.append(" AND upper( efp.identificador ) like(upper(sem_acentos(?))) ");
		}

		if (!situacao.equals("TODOS")) {
			dataModelo.getListaFiltros().add(situacao);
			sql.append(" AND efp.situacao like(upper(?))");	
		}

		if (!tipoLancamento.equals("TODOS")) {
			sql.append(" AND efp.tipolancamento like(upper(?))");
			dataModelo.getListaFiltros().add(tipoLancamento);
		}
		
		sql.append(" order by ").append(campoConsulta).append(" asc ");
        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());			

		List<EventoFolhaPagamentoVO> eventosDaFolhaDePagamento = new ArrayList<>();
		while (tabelaResultado.next()) {
			eventosDaFolhaDePagamento.add(montarDados(tabelaResultado, dataModelo.getNivelMontarDados()));
		}
		
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(dataModelo, campoConsulta, situacao, tipoLancamento));
		
		return eventosDaFolhaDePagamento;
	}
	
	/**
	 * Consulta paginada do total de eventos da consulta dos filtros informados
	 * @param dataModelo
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	private Integer consultarTotalPorFiltros(DataModelo dataModelo, String campoConsulta, String situacao, String tipoLancamento) throws Exception {
		EventoFolhaPagamento.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder(getSelectSqlBasico().replace(" * ", " COUNT(efp.codigo) as qtde "));

        sql.append(" WHERE 1 = 1");
		
        if (campoConsulta.equals("efp.descricao")) {
        	sql.append(" AND upper( efp.descricao ) like(upper(?)) ");
        } else {
        	sql.append(" AND upper( efp.identificador ) like(upper(?)) ");
        }
        
        if (!situacao.equals("TODOS")) {
            sql.append(" AND efp.situacao like(upper(?))");	
        }

        if (!tipoLancamento.equals("TODOS")) {
			sql.append(" AND efp.tipolancamento like(upper(?))");
		}
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
	
	@Override
	public List<EventoFolhaPagamentoVO> consultarPorFiltroEProvento(String campoConsulta, String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" WHERE 1 = 1");
		sql.append(" AND efp.situacao like(upper(?))");
		
		switch (campoConsulta) {
		case "descricao":
			sql.append(" AND upper( efp.descricao ) like(upper(?)) ");
			break;
		case "identificador":
			sql.append(" AND upper( efp.identificador ) like(upper(?)) ");
			break;
		default:
			break;
		}
		
		sql.append(" AND efp.tipolancamento = 'PROVENTO'");
		
		if (situacao.equals("TODOS")) {
			situacao = "%%";
		}
		
		valorConsulta = "%" + valorConsulta + "%";
		sql.append(" ORDER BY efp.").append(campoConsulta).append(" DESC ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), situacao, valorConsulta);			
		
		List<EventoFolhaPagamentoVO> eventosDaFolhaDePagamento = new ArrayList<>();
		while (tabelaResultado.next()) {
			eventosDaFolhaDePagamento.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return eventosDaFolhaDePagamento;
	}

	/**
	 * Monta o objeto <code>EventoFolhaPagamentoVO<code> consultado do banco de
	 * dados.
	 * 
	 * @param tabelaResultado
	 * @return
	 */
	public EventoFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado, int montarDados)  throws Exception {

		EventoFolhaPagamentoVO obj = new EventoFolhaPagamentoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setIdentificador(tabelaResultado.getString("identificador"));
		obj.setDescricao(tabelaResultado.getString("descricao"));

		obj.setOrdemCalculo(tabelaResultado.getInt("ordemcalculo"));
		obj.setPrioridade(tabelaResultado.getInt("prioridade"));
		
		if (tabelaResultado.getString("situacao") != null) {
			obj.setSituacao(AtivoInativoEnum.valueOf(tabelaResultado.getString("situacao")));
		} else {
			obj.setSituacao(AtivoInativoEnum.INATIVO);
		}

		obj.setTipoLancamento(TipoLancamentoFolhaPagamentoEnum.valueOf(tabelaResultado.getString("tipolancamento")));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoevento"))) {
			obj.setTipoEvento(TipoEventoFolhaPagamentoEnum.valueOf(tabelaResultado.getString("tipoevento")));
		} else {
			obj.setTipoEvento(null);
		}

		if (tabelaResultado.getString("naturezaEvento") != null) {
			obj.setNaturezaEvento(NaturezaEventoFolhaPagamentoEnum.valueOf(tabelaResultado.getString("naturezaEvento")));
		} 
		
		obj.setPermiteDuplicarContraCheque(tabelaResultado.getBoolean("permiteDuplicarContraCheque"));
		
		if(montarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS)
			return obj;
		
		obj.setFormulaDia(Uteis.montarDadosVO(tabelaResultado.getInt("formuladia"), FormulaFolhaPagamentoVO.class, p -> getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p)));
		obj.setFormulaHora(Uteis.montarDadosVO(tabelaResultado.getInt("formulahora"), FormulaFolhaPagamentoVO.class, p -> getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p)));
		obj.setFormulaReferencia(Uteis.montarDadosVO(tabelaResultado.getInt("formulareferencia"), FormulaFolhaPagamentoVO.class, p -> getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p)));
		obj.setFormulaValor(Uteis.montarDadosVO(tabelaResultado.getInt("formulavalor"), FormulaFolhaPagamentoVO.class, p -> getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p)));
		obj.setFormulaFixa(Uteis.montarDadosVO(tabelaResultado.getInt("formulafixa"), FormulaFolhaPagamentoVO.class, p -> getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p)));
		
		obj.setInssFolhaNormal(tabelaResultado.getBoolean("inssFolhaNormal"));
		obj.setIrrfFolhaNormal(tabelaResultado.getBoolean("irrfFolhaNormal"));
		obj.setFgtsFolhaNormal(tabelaResultado.getBoolean("fgtsFolhaNormal"));
		obj.setDsrFolhaNormal(tabelaResultado.getBoolean("dsrFolhaNormal"));
		obj.setSalarioFamiliaFolhaNormal(tabelaResultado.getBoolean("salarioFamiliaFolhaNormal"));
		obj.setIrrfFerias(tabelaResultado.getBoolean("irrfFerias"));
		obj.setAdicionalFerias(tabelaResultado.getBoolean("adicionalFerias"));
		obj.setInssDecimoTerceiro(tabelaResultado.getBoolean("inssDecimoTerceiro"));
		obj.setIrrfDecimoTerceiro(tabelaResultado.getBoolean("irrfDecimoTerceiro"));
		obj.setFgtsDecimoTerceiro(tabelaResultado.getBoolean("fgtsDecimoTerceiro"));
		obj.setFolhaPensao(tabelaResultado.getBoolean("folhaPensao"));
		obj.setFeriasPensao(tabelaResultado.getBoolean("feriasPensao"));
		obj.setDecimoTerceiroPensao(tabelaResultado.getBoolean("decimoTerceiroPensao"));
		obj.setParticipacaoLucroPensao(tabelaResultado.getBoolean("participacaoLucroPensao"));
		obj.setRais(tabelaResultado.getBoolean("rais"));
		obj.setInformeRendimento(tabelaResultado.getBoolean("informeRendimento"));
		
		obj.setPrevidenciaPropria(tabelaResultado.getBoolean("previdenciaPropria"));
		obj.setPlanoSaude(tabelaResultado.getBoolean("planoSaude"));
		obj.setDecimoTerceiroPrevidenciaPropria(tabelaResultado.getBoolean("decimoTerceiroPrevidenciaPropria"));
		obj.setDecimoTerceiroPlanoSaude(tabelaResultado.getBoolean("decimoTerceiroPlanoSaude"));
		obj.setDedutivelIrrf(tabelaResultado.getBoolean("dedutivelIrrf"));
		obj.setEstornaInss(tabelaResultado.getBoolean("estornaInss"));
		obj.setEstornaIrrf(tabelaResultado.getBoolean("estornaIrrf"));
		obj.setEstornaFgts(tabelaResultado.getBoolean("estornaFgts"));
		obj.setEstornaValeTransporte(tabelaResultado.getBoolean("estornaValeTransporte"));
		obj.setEstornaSalarioFamilia(tabelaResultado.getBoolean("estornaSalarioFamilia"));
		obj.setEstornaIrrfFerias(tabelaResultado.getBoolean("estornaIrrfFerias"));
		obj.setDedutivelIrrfFerias(tabelaResultado.getBoolean("dedutivelIrrfFerias"));
		obj.setEstornaInssDecimoTerceiro(tabelaResultado.getBoolean("estornaInssDecimoTerceiro"));
		obj.setEstornaFgtsDecimoTerceiro(tabelaResultado.getBoolean("estornaFgtsDecimoTerceiro"));
		obj.setEstornaIrrfDecimoTerceiro(tabelaResultado.getBoolean("estornaIrrfDecimoTerceiro"));
		obj.setDedutivelIrrfDecimoTerceiro(tabelaResultado.getBoolean("dedutivelIrrfDecimoTerceiro"));
		obj.setFolhaPensaoDesconto(tabelaResultado.getBoolean("folhaPensaoDesconto"));
		obj.setFeriasPensaoDesconto(tabelaResultado.getBoolean("feriasPensaoDesconto"));
		obj.setDecimentoTerceiroPensaoDesconto(tabelaResultado.getBoolean("decimentoTerceiroPensaoDesconto"));
		obj.setParticipacaoLucroPensaoDesconto(tabelaResultado.getBoolean("participacaoLucroPensaoDesconto"));
		obj.setPrevidenciaObrigatoria(tabelaResultado.getBoolean("previdenciaObrigatoria"));
		obj.setDataUltimaAlteracao(tabelaResultado.getDate("dataultimaalteracao"));
		obj.getUsuarioUltimaAlteracao().setCodigo(tabelaResultado.getInt("usuarioresponsavelalteracao"));

		obj.setIncideAdicionalTempoServico(tabelaResultado.getBoolean("incideAdicionalTempoServico"));
		obj.setIncideAssociacaoSindicato(tabelaResultado.getBoolean("incideAssociacaoSindicato"));
		obj.setAdmissao(tabelaResultado.getBoolean("admissao"));
		obj.setDemissao(tabelaResultado.getBoolean("demissao"));
		obj.setFerias(tabelaResultado.getBoolean("ferias"));
		obj.setAfastamento(tabelaResultado.getBoolean("afastamento"));
		obj.setEventoPadrao(tabelaResultado.getBoolean("eventopadrao"));
		obj.setValeTransporte(tabelaResultado.getBoolean("valetransporte"));
		obj.setValeTransporteDesconto(tabelaResultado.getBoolean("valetransportedesconto"));

		obj.setAgrupamentoFolhaNormal(tabelaResultado.getBoolean("agrupamentoFolhaNormal"));
		obj.setAgrupamentoFerias(tabelaResultado.getBoolean("agrupamentoFerias"));
		obj.setAgrupamentoFeriasProporcionais(tabelaResultado.getBoolean("agrupamentoFeriasProporcionais"));
		obj.setAgrupamentoFeriasVencidas(tabelaResultado.getBoolean("agrupamentoFeriasVencidas"));
		obj.setAgrupamentoDecimoTerceiro(tabelaResultado.getBoolean("agrupamentoDecimoTerceiro"));
		obj.setAgrupamentoDecimoTerceiroProporcionais(tabelaResultado.getBoolean("agrupamentoDecimoTerceiroProporcionais"));
		
		obj.setIncideAdicionalTempoServicoFerias(tabelaResultado.getBoolean("incideAdicionalTempoServicoFerias"));
		obj.setIncideAdicionalTempoServico13(tabelaResultado.getBoolean("incideAdicionalTempoServico13"));
		obj.setIncideAdicionalTempoServicoRescisao(tabelaResultado.getBoolean("incideAdicionalTempoServicoRescisao"));
		obj.setInssFerias(tabelaResultado.getBoolean("inssFerias"));
		
		obj.setPrevidenciaPropriaFerias(tabelaResultado.getBoolean("previdenciaPropriaFerias"));
		obj.setPrevidenciaObrigatoriaFerias(tabelaResultado.getBoolean("previdenciaObrigatoriaFerias"));
		obj.setIncideAssociacaoSindicatoFerias(tabelaResultado.getBoolean("incideAssociacaoSindicatoFerias"));
		obj.setIncidePlanoSaudeFerias(tabelaResultado.getBoolean("incidePlanoSaudeFerias"));

		obj.setIncideAssociacaoSindicatoDecimoTerceiro(tabelaResultado.getBoolean("incideassociacaosindicatodecimoterceiro"));
		obj.setIncidePrevidenciaObrigatoriaDecimoTerceiro(tabelaResultado.getBoolean("incideprevidenciaobrigatoriadecimoterceiro"));
		
		if(montarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA)
			return obj;
		
		if (Uteis.isAtributoPreenchido(obj.getUsuarioUltimaAlteracao().getCodigo())) {
			obj.setUsuarioUltimaAlteracao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioUltimaAlteracao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}

		if(Uteis.isAtributoPreenchido(tabelaResultado.getString("categoria"))) {
			obj.setCategoria(CategoriaEventoFolhaEnum.valueOf(tabelaResultado.getString("categoria")));
		}

		if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("categoriaDespesa"))) {
			obj.setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("categoriaDespesa"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}

		return obj;
	}

	/**
	 * Sql basico para consultad da <code>EventoFolhaPagamentoVO<code>
	 * 
	 * @return
	 */
	public String getSelectSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM eventofolhapagamento efp");
		sql.append(" left JOIN formulafolhapagamento fv ON efp.formulavalor = fv.codigo");
		sql.append(" left JOIN formulafolhapagamento fh ON efp.formulahora = fh.codigo");
		sql.append(" left JOIN formulafolhapagamento fd ON efp.formuladia = fd.codigo");
		sql.append(" left JOIN formulafolhapagamento fr ON efp.formulareferencia = fr.codigo");

		return sql.toString();
	}

	/**
	 * Valida os campos obrigatorios do <code>EventoFolhaPagamentoVO<code>
	 * 
	 * @param obj <code>EventoFolhaPagamentoVO</code>
	 * 
	 * @throws ConsistirException
	 * @throws ParseException
	 */
	public void validarDados(EventoFolhaPagamentoVO obj) throws ConsistirException {
		try {
			Preconditions.checkState(!Strings.isNullOrEmpty(obj.getIdentificador()), "msg_EventoFolhaPagamento_identificador");
			Preconditions.checkState(!Strings.isNullOrEmpty(obj.getDescricao()), "msg_EventoFolhaPagamento_descricao");
		} catch (IllegalStateException e) {
			throw new ConsistirException(UteisJSF.internacionalizar(e.getMessage()));
		}
	}

	@Override
	public EventoFolhaPagamentoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM eventofolhapagamento WHERE codigo = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	private void validarDadosDuplicadosIdentificador(EventoFolhaPagamentoVO obj) throws ConsistirException {
		int retorno = consutarDadosTotalPorIdentificador(obj);
		
		if (retorno > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_EventoFolhaPagamento_duplicado"));
		}
	}
	
	private int consutarDadosTotalPorIdentificador(EventoFolhaPagamentoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(codigo) as qtde FROM eventofolhapagamento");
		sql.append(" WHERE identificador = ?");
		
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" AND codigo != ?");
		}

		SqlRowSet rs = null;
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getIdentificador(), obj.getCodigo());
		} else {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getIdentificador());
		}

        if (rs.next()) {
            return rs.getInt("qtde");
        }

    	return 0;
	}

	@Override
	public void validarDadosPorFormulaFolhaPagamento(FormulaFolhaPagamentoVO obj, String campoConsulta) throws ConsistirException {
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			int total = consultarTotalPorFormulaFolhaPagamento(obj, campoConsulta);
	
			if (total > 0) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_EventoIncidenciaFormulaFolhaPagamento_vinculado"));
			}
		}
	}

	public int consultarTotalPorFormulaFolhaPagamento(FormulaFolhaPagamentoVO obj, String campoConsulta) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT COUNT(codigo) as qtde FROM eventoFolhaPagamento");

			switch (campoConsulta) {
			case "formulaValor":
				sql.append(" WHERE formulaValor = ? ");
				break;
			case "formulaHora":
				sql.append(" WHERE formulaHora = ? ");
				break;
			case "formulaDia":
				sql.append(" WHERE formulaDia = ? ");
				break;
			case "formulaReferencia":
				sql.append(" WHERE formulaReferencia = ? ");
				break;

			default:
				break;
			}

			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getCodigo());

	        if (rs.next()) {
	            return rs.getInt("qtde");
	        }

	    	return 0;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public EventoFolhaPagamentoVO consultarPorChaveIdentificador(String identificador, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM eventofolhapagamento");
		sql.append(" WHERE identificador = ?");

		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), identificador);

		if (rs.next()) {
			return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS);
		}

		throw new Exception("Dados não encontrados (Evento Folha Pagamento).");
	}

	public int consultarProximoCodigoDoEventoFolhaPagamento() {
		StringBuilder sql = new StringBuilder("select coalesce(max(codigo),0)+1 as proximoCodigo from eventofolhapagamento");
		SqlRowSet rs = null;
		rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

        if (rs.next()) {
            return rs.getInt("proximoCodigo");
        }

    	return 1;
	}

	@Override
	public List<EventoFolhaPagamentoVO> consultarEventosFolhaCompletoPorContraChequeEvento(List<ContraChequeEventoVO> contraChequeEventos, boolean b, UsuarioVO usuario) throws Exception {
		List<EventoFolhaPagamentoVO> lista = new ArrayList<>();
		EventoFolhaPagamentoVO evento;
		for (ContraChequeEventoVO contraChequeEventoVO : contraChequeEventos) {
			evento = consultarPorChavePrimaria(contraChequeEventoVO.getEventoFolhaPagamento().getCodigo(), usuario, Uteis.NIVELMONTARDADOS_TODOS);
			if(evento != null && evento.getCodigo() > 0) {
				evento.setValorTemporario(contraChequeEventoVO.getValorReferencia());
				lista.add(evento);				
			}
		}
		return lista;
	}
	
	@Override
	public List<EventoFolhaPagamentoVO> consultarEventosFolhaCompletoPorTemplateEventoFolhaPagamentoVO(List<TemplateEventoFolhaPagamentoVO> templateEventos, boolean b, UsuarioVO usuario) {
		List<EventoFolhaPagamentoVO> lista = new ArrayList<>();
		try {
			EventoFolhaPagamentoVO evento;
			for (TemplateEventoFolhaPagamentoVO templateEventoVO : templateEventos) {
				evento = consultarPorChavePrimaria(templateEventoVO.getEventoFolhaPagamento().getCodigo(), usuario, Uteis.NIVELMONTARDADOS_TODOS);
				if(evento != null && evento.getCodigo() > 0) {
					evento.setValorTemporario(templateEventoVO.getValor());
					lista.add(evento);				
				}
			}			
		}catch (Exception e) {	e.printStackTrace();}
		
		return lista;
	}
	

	@Override
	public void inativar(EventoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					String sql = "UPDATE eventofolhapagamento set situacao=? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;

					Uteis.setValuePreparedStatement(obj.getSituacao().getValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
		
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		EventoFolhaPagamento.idEntidade = idEntidade;
	}

	
	/**
	 * Retorna o valor do evento.
	 * Caso seja inserido manualmente retorna o valor inserido ou 
	 * retorna o valor resultante da formula
	 */
	@Override
	public BigDecimal recuperarValorDoEventoCalculado(EventoFolhaPagamentoVO evento, FuncionarioCargoVO funcionarioCargo, CalculoContraCheque calculoContraCheque, ScriptEngine engine) {
		Object resultado;
		String formula = "return 0;";
		if(Uteis.isAtributoPreenchido(evento.getValorTemporario()) && evento.getValorTemporario().compareTo(new BigDecimal(0)) > 0) {
			return evento.getValorTemporario();
		} else if(Uteis.isAtributoPreenchido(evento.getFormulaFixa())) {
			formula = evento.getFormulaFixa().getFormula();
		} else if (Uteis.isAtributoPreenchido(evento.getFormulaValor())) {
			formula = evento.getFormulaValor().getFormula();
		}

		try {
			resultado = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().executarFormula(formula, funcionarioCargo, calculoContraCheque, engine);
			
			if(Uteis.isAtributoPreenchido(resultado)) {
				formula = null;
				return new BigDecimal(resultado.toString());
			}	
		} catch (Exception e) {
			formula = null;
			e.printStackTrace();
		}
		
		return BigDecimal.ZERO;
	}


	/**
	 * Retorna a string com o resultado da formula
	 * 
	 */
	@Override
	public String recuperarReferenciaDoEventoCalculado(EventoFolhaPagamentoVO evento, FuncionarioCargoVO funcionarioCargo, CalculoContraCheque calculoContraCheque, ScriptEngine engine) {
		
		Object resultado;
		String formula = "return;";
		
		if(Uteis.isAtributoPreenchido(evento.getReferencia()) && evento.getValorInformado()) {
			return evento.getReferencia();
		} else if(Uteis.isAtributoPreenchido(evento.getFormulaReferencia())) {
			formula = evento.getFormulaReferencia().getFormula();
		} else if (Uteis.isAtributoPreenchido(evento.getFormulaDia())) {
			formula = evento.getFormulaDia().getFormula();
		} else if (Uteis.isAtributoPreenchido(evento.getFormulaHora())) {
			formula = evento.getFormulaHora().getFormula();
		}

		try {
			resultado = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().executarFormula(formula, funcionarioCargo, calculoContraCheque, engine);
			
			if(resultado != null) {
				return resultado.toString();
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void calcularEventoFolhaPagamento(EventoFolhaPagamentoVO evento, FuncionarioCargoVO funcionarioCargo, CalculoContraCheque calculoContraCheque, ScriptEngine engine) {
		
		try {
			//
			calculoContraCheque.setReferenciaEvento(evento.getReferencia());

			//Valor
			evento.setValorTemporario(this.recuperarValorDoEventoCalculado(evento, funcionarioCargo, calculoContraCheque, engine));
			
			//Referencia
			evento.setReferencia(this.recuperarReferenciaDoEventoCalculado(evento, funcionarioCargo, calculoContraCheque, engine));
			
		} catch (Exception e) {
			evento.setValorTemporario(BigDecimal.ZERO);
			evento.setReferencia("Erro na fórmula");
		}
	}

	/**
	 * Monta os dados do evento e do contracheque caso exista
	 * 
	 * @param tabelaResultado
	 * @return
	 */
	public EventoFolhaPagamentoVO montarDadosDoEventoParaContraCheque(Integer eventofolhapagamento, Integer contraChequeEvento) {
		
		if(eventofolhapagamento == null)
			eventofolhapagamento = 0;
		
		if(contraChequeEvento == null)
			contraChequeEvento = 0;

		EventoFolhaPagamentoVO obj = new EventoFolhaPagamentoVO();
		
		try {
			obj = (Uteis.montarDadosVO(eventofolhapagamento, EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_TODOS)));
			obj.setContraChequeEventoVO(Uteis.montarDadosVO(contraChequeEvento, ContraChequeEventoVO.class, p -> getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarPorChavePrimaria(p.longValue())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}

	@Override
	public List<String> consultarListaDeIdentificadoresAtivo() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT identificador FROM eventofolhapagamento where situacao like 'ATIVO' order by identificador ");

		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		List<String> identificadores = new ArrayList<>();
		
		while (rs.next()) {
			identificadores.add(rs.getString("identificador"));
		}
		return identificadores;
	}

	@Override
	public List<EventoFolhaPagamentoVO> consultarEventosDeFerias() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" WHERE efp.agrupamentoFerias = ? and efp.situacao = ? ");
        sql.append(" order by efp.prioridade, efp.ordemcalculo asc ");
        
        List<Object> filtros = new ArrayList<>();
        filtros.add(true);
        filtros.add(AtivoInativoEnum.ATIVO.getValor());

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());			

		List<EventoFolhaPagamentoVO> eventosDeFeriasDaFolhaDePagamento = new ArrayList<>();
		while (tabelaResultado.next()) {
			try {
				eventosDeFeriasDaFolhaDePagamento.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));	
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return eventosDeFeriasDaFolhaDePagamento;
	}
	
	@Override
	public List<EventoFolhaPagamentoVO> consultarEventosDo13() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" WHERE efp.agrupamentoDecimoTerceiro = ? and efp.situacao = ? ");
        sql.append(" order by efp.prioridade, efp.ordemcalculo asc ");
        
        List<Object> filtros = new ArrayList<>();
        filtros.add(true);
        filtros.add(AtivoInativoEnum.ATIVO.getValor());

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());			

		List<EventoFolhaPagamentoVO> eventosDo13 = new ArrayList<>();
		while (tabelaResultado.next()) {
			try {
				eventosDo13.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));	
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return eventosDo13;
	}
	
	@Override
	public List<EventoFolhaPagamentoVO> consultarEventosDeFolhaNormal() {
		
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" WHERE efp.agrupamentoFolhaNormal = ? and efp.situacao = ? ");
        sql.append(" order by efp.prioridade, efp.ordemcalculo asc ");
        
        List<Object> filtros = new ArrayList<>();
        filtros.add(true);
        filtros.add(AtivoInativoEnum.ATIVO.getValor());

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());			

		List<EventoFolhaPagamentoVO> eventosDeFeriasDaFolhaDePagamento = new ArrayList<>();
		while (tabelaResultado.next()) {
			try {
				eventosDeFeriasDaFolhaDePagamento.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));	
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return eventosDeFeriasDaFolhaDePagamento;
	}

	/**
	 * Responsavel por adicionar na lista: listaDeEventosDoFuncionario os eventos de folha normal (agrupamento igual a Folha Normal)
	 */
	@Override
	public void adicionarEventosDeFolhaNormal(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario) {
		
		List<EventoFolhaPagamentoVO> eventosDeFolhaNormal = consultarEventosDeFolhaNormal();
		
		if(Uteis.isAtributoPreenchido(eventosDeFolhaNormal)) {
			for(EventoFolhaPagamentoVO evento : eventosDeFolhaNormal) {
				if(!listaDeEventosDoFuncionario.contains(evento)) {
					listaDeEventosDoFuncionario.add(evento);
				}
			}
		}
	}

	@Override
	public EventoFolhaPagamentoVO consultarEventoPorCategoriaEAgrupamento(CategoriaEventoFolhaEnum categoriaEvento, String agrupamento) {
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" WHERE categoria = ? ");
		sql.append(" and ").append(agrupamento).append(" = ? ");
        sql.append(" and efp.situacao = ? ");
        sql.append(" and efp.eventopadrao = ? ");
        sql.append(" limit 1 ");
        
        List<Object> filtros = new ArrayList<>();
        filtros.add(categoriaEvento.getValor());
        filtros.add(true);
        filtros.add(AtivoInativoEnum.ATIVO.getValor());
        filtros.add(true);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());			

		try {
			if(tabelaResultado.next()) {
				return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new EventoFolhaPagamentoVO();
	}

	@Override
	public List<EventoFolhaPagamentoVO> consultarEventosDeRescisao() {
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());
		sql.append(" WHERE efp.situacao = ? ");
		sql.append(" and (efp.agrupamentoFeriasVencidas = ? ");
		sql.append(" or efp.agrupamentoFeriasProporcionais = ? ");
		sql.append(" or efp.agrupamentoDecimoTerceiroProporcionais = ? )");
        sql.append(" order by efp.prioridade, efp.ordemcalculo asc ");
        
        List<Object> filtros = new ArrayList<>();
        filtros.add(AtivoInativoEnum.ATIVO.getValor());
        filtros.add(Boolean.TRUE);
        filtros.add(Boolean.TRUE);
        filtros.add(Boolean.TRUE);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());

		List<EventoFolhaPagamentoVO> eventosDeFeriasDaFolhaDePagamento = new ArrayList<>();
		while (tabelaResultado.next()) {
			try {
				eventosDeFeriasDaFolhaDePagamento.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return eventosDeFeriasDaFolhaDePagamento;
	}
}