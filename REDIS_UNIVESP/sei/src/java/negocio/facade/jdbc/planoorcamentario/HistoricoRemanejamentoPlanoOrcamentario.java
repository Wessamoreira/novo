package negocio.facade.jdbc.planoorcamentario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.HistoricoRemanejamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.recursoshumanos.AfastamentoFuncionario;
import negocio.interfaces.planoorcamentario.HistoricoRemanejamentoPlanoOrcamentarioInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>AfastamentoFuncionarioVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>AfastamentoFuncionarioVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class HistoricoRemanejamentoPlanoOrcamentario extends SuperFacade<HistoricoRemanejamentoPlanoOrcamentarioVO>
	implements HistoricoRemanejamentoPlanoOrcamentarioInterfaceFacade<HistoricoRemanejamentoPlanoOrcamentarioVO> {

	private static final long serialVersionUID = 7887031494085600225L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(HistoricoRemanejamentoPlanoOrcamentarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(HistoricoRemanejamentoPlanoOrcamentarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO)
			throws Exception {
		HistoricoRemanejamentoPlanoOrcamentario.incluir(getIdEntidade(), validarAcesso, usuarioVO);

		incluir(obj, "HistoricoRemanejamentoPlanoOrcamentario",
				new AtributoPersistencia()
						.add("valor", obj.getValor())
						.add("data", Uteis.getDataJDBCTimestamp(obj.getData()))
						.add("usuario", obj.getUsuarioVO().getCodigo())
						.add("categoriadespesa",  obj.getCategoriaDespesaVO().getCodigo())
						.add("solicitacaoOrcamentoPlanoOrcamentario",  obj.getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo())
						.add("itemSolicitacaoOrcamentoPlanoOrcamentario",  obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo())
						.add("motivo",  obj.getMotivo())
						.add("valorremanejado",  obj.getValorRemanejado())
						.add("categoriadespesaremanejado",  obj.getCategoriaDespesaRemanejado().getCodigo())
						, usuarioVO);
		obj.setNovoObj(Boolean.TRUE);
	}

	@Override
	public void alterar(HistoricoRemanejamentoPlanoOrcamentarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO)
			throws Exception {
		HistoricoRemanejamentoPlanoOrcamentario.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		alterar(obj, "HistoricoRemanejamentoPlanoOrcamentario",
				new AtributoPersistencia()
						.add("valor", obj.getValor())
						.add("data", Uteis.getDataJDBCTimestamp(obj.getData()))
						.add("usuario", obj.getUsuarioVO().getCodigo())
						.add("categoriadespesa",  obj.getCategoriaDespesaVO().getCodigo())
						.add("solicitacaoOrcamentoPlanoOrcamentario",  obj.getSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo())
						.add("itemSolicitacaoOrcamentoPlanoOrcamentario",  obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCodigo())
						.add("motivo",  obj.getMotivo())
						.add("valorremanejado",  obj.getValorRemanejado())
						.add("categoriadespesaremanejado",  obj.getCategoriaDespesaRemanejado().getCodigo()),
						new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Override
	public void validarDados(HistoricoRemanejamentoPlanoOrcamentarioVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getData())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_data"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getUsuarioVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_usuario"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getCategoriaDespesaVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_categoriaDespesa"));			
		}

		if (!Uteis.isAtributoPreenchido(obj.getSolicitacaoOrcamentoPlanoOrcamentarioVO())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_planoOrcamentario"));			
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(HistoricoRemanejamentoPlanoOrcamentarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO)
			throws Exception {
		AfastamentoFuncionario.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM HistoricoRemanejamentoPlanoOrcamentario WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Override
	public HistoricoRemanejamentoPlanoOrcamentarioVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT HistoricoRemanejamentoPlanoOrcamentarioVO.* FROM HistoricoRemanejamentoPlanoOrcamentarioVO WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	/**
	 * Monta a lista de {@link HistoricoRemanejamentoPlanoOrcamentarioVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	public List<HistoricoRemanejamentoPlanoOrcamentarioVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<HistoricoRemanejamentoPlanoOrcamentarioVO> historicosRemanejamentoPlanoOrcamentarios = new ArrayList<>();

        while(tabelaResultado.next()) {
        	historicosRemanejamentoPlanoOrcamentarios.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return historicosRemanejamentoPlanoOrcamentarios;
	}

	@Override
	public HistoricoRemanejamentoPlanoOrcamentarioVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		HistoricoRemanejamentoPlanoOrcamentarioVO obj = new HistoricoRemanejamentoPlanoOrcamentarioVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setData(tabelaResultado.getDate("data"));
		obj.setValor(tabelaResultado.getBigDecimal("valor"));
		obj.setValorRemanejado(tabelaResultado.getBigDecimal("valorremanejado"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("usuario"))) {
			obj.setUsuarioVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(tabelaResultado.getInt("usuario"), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("solicitacaoOrcamentoPlanoOrcamentario"))) {
			obj.setSolicitacaoOrcamentoPlanoOrcamentarioVO(getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultarPorChavePrimaria(tabelaResultado.getInt("solicitacaoOrcamentoPlanoOrcamentario"), false, nivelMontarDados, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("categoriadespesa"))) {
			obj.setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("categoriadespesa"), false, nivelMontarDados, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("categoriadespesaremanejado"))) {
			obj.setCategoriaDespesaRemanejado(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("categoriadespesaremanejado"), false, nivelMontarDados, null));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("itemSolicitacaoOrcamentoPlanoOrcamentario"))) {
			obj.setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().consultarPorChavePrimaria(tabelaResultado.getInt("itemSolicitacaoOrcamentoPlanoOrcamentario"), null));
		}
		obj.setMotivo(tabelaResultado.getString("motivo"));

		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void remanejarItemSolicitacaoOrcamentoPlanoOrcamentario(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado, 
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado,
			UsuarioVO usuarioVO, SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, String motivo) throws Exception {

 		validaDadosRemanejamento(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado, itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado, motivo);
 		
		if(itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado.getRemanejarParaPropriaCategoriaDespesa()) {
			for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVOSelecionado : itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado.getDetalhamentoPeriodoOrcamentoVOs()) {

				for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVORemanejado : itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getDetalhamentoPeriodoOrcamentoVOs()) {
					if(detalhamentoPeriodoOrcamentoVORemanejado.getMes().equals(detalhamentoPeriodoOrcamentoVOSelecionado.getMes()) && detalhamentoPeriodoOrcamentoVORemanejado.getAno().equals(detalhamentoPeriodoOrcamentoVOSelecionado.getAno())) {						
						detalhamentoPeriodoOrcamentoVOSelecionado.setOrcamentoRequeridoGestor(detalhamentoPeriodoOrcamentoVORemanejado.getValorRemanejar() + detalhamentoPeriodoOrcamentoVOSelecionado.getValorConsumido());					
						detalhamentoPeriodoOrcamentoVOSelecionado.setOrcamentoTotal(detalhamentoPeriodoOrcamentoVORemanejado.getValorRemanejar() + detalhamentoPeriodoOrcamentoVOSelecionado.getValorConsumido());
					}
				}
			}
			getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterar(itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado	, usuarioVO, true);
			persistirHistoricoRemanejamento(itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado, itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado, usuarioVO, solicitacaoOrcamentoPlanoOrcamentarioVO, null, motivo);
	  
		} else {
		
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentario = recuperarItemSolicitacaoOrcamentoPlanoOrcamentarioPorCategoriaDespesa( itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado, solicitacaoOrcamentoPlanoOrcamentarioVO);

			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioClonado = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) SerializationUtils.clone(itemSolicitacaoOrcamentoPlanoOrcamentario);
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionadoClonado = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) SerializationUtils.clone(itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado);
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejadoClonado = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) SerializationUtils.clone(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado);
			try {

				for (DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVOSelecionado : itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado.getDetalhamentoPeriodoOrcamentoVOs()) {
					if(detalhamentoPeriodoOrcamentoVOSelecionado.getValorRemanejar() > 0) {
						detalhamentoPeriodoOrcamentoVOSelecionado.setOrcamentoTotal(Uteis.arrendondarForcando2CadasDecimais(detalhamentoPeriodoOrcamentoVOSelecionado.getOrcamentoTotal()   - detalhamentoPeriodoOrcamentoVOSelecionado.getValorRemanejar()));
					}
				}
				
				//Valida se Existe ItemSolicitacaoOrcamentoPlanoOrcamentarioVO com a categoria de despesa informada.
				if (Uteis.isAtributoPreenchido(itemSolicitacaoOrcamentoPlanoOrcamentario)) {				
						itemSolicitacaoOrcamentoPlanoOrcamentario.setValor(itemSolicitacaoOrcamentoPlanoOrcamentario.getValor() + itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getValor());
						itemSolicitacaoOrcamentoPlanoOrcamentario.setValorAutorizado(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getValorAutorizado().add(itemSolicitacaoOrcamentoPlanoOrcamentario.getValorAutorizado()));
					
					if(!itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getDescricao().trim().isEmpty() && !itemSolicitacaoOrcamentoPlanoOrcamentario.getDescricao().contains(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getDescricao())) {
						itemSolicitacaoOrcamentoPlanoOrcamentario.setDescricao(itemSolicitacaoOrcamentoPlanoOrcamentario.getDescricao() +", "+ itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getDescricao());
					}
					
					for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO : itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getDetalhamentoPeriodoOrcamentoVOs()) {
						for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO2 : itemSolicitacaoOrcamentoPlanoOrcamentario.getDetalhamentoPeriodoOrcamentoVOs()) {
							if(detalhamentoPeriodoOrcamentoVO.getMes().equals(detalhamentoPeriodoOrcamentoVO2.getMes()) && detalhamentoPeriodoOrcamentoVO.getAno().equals(detalhamentoPeriodoOrcamentoVO2.getAno())) {
								detalhamentoPeriodoOrcamentoVO2.setCodigo(detalhamentoPeriodoOrcamentoVO.getCodigo());
								detalhamentoPeriodoOrcamentoVO2.setOrcamentoRequeridoGestor(detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() + detalhamentoPeriodoOrcamentoVO2.getOrcamentoRequeridoGestor());
								detalhamentoPeriodoOrcamentoVO2.setOrcamentoRequeridoGestor(detalhamentoPeriodoOrcamentoVO.getOrcamentoTotal() + detalhamentoPeriodoOrcamentoVO2.getOrcamentoTotal());
								detalhamentoPeriodoOrcamentoVO2.setOrcamentoTotal(detalhamentoPeriodoOrcamentoVO.getOrcamentoTotal() + detalhamentoPeriodoOrcamentoVO2.getOrcamentoTotal());
							}
						}
					}
					
					getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterar(itemSolicitacaoOrcamentoPlanoOrcamentario, usuarioVO, true);
				}else {
					itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.setValorConsumido(0.0);
					for(DetalhamentoPeriodoOrcamentoVO detalahamentoPeriodoOrcamentarioVO : itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getDetalhamentoPeriodoOrcamentoVOs()) {
						detalahamentoPeriodoOrcamentarioVO.setOrcamentoRequeridoGestor(detalahamentoPeriodoOrcamentarioVO.getValorRemanejar());
						
					}
					if (!Uteis.isAtributoPreenchido(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getSolicitacaoOrcamentoPlanoOrcamentario())) {
						itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.setSolicitacaoOrcamentoPlanoOrcamentario(solicitacaoOrcamentoPlanoOrcamentarioVO);
					}
					getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().incluir(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado, usuarioVO);
				}
				itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado.setValor(itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado.getValor() - itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getValor());
				itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado.setValorAutorizado(itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado.getValorAutorizado().subtract(BigDecimal.valueOf(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getValor())));
			
				getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterar(itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado	, usuarioVO, false);
				persistirHistoricoRemanejamento(itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado, itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado, usuarioVO, solicitacaoOrcamentoPlanoOrcamentarioVO, itemSolicitacaoOrcamentoPlanoOrcamentario, motivo);
				if (!Uteis.isAtributoPreenchido(itemSolicitacaoOrcamentoPlanoOrcamentario)) {				
					solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().add(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado);
				}
			}catch (Exception e) {
				itemSolicitacaoOrcamentoPlanoOrcamentario = itemSolicitacaoOrcamentoPlanoOrcamentarioClonado;
				itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado = itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionadoClonado;
				itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado = itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejadoClonado;
				throw e;
			}
		}//fim else
	}

	private ItemSolicitacaoOrcamentoPlanoOrcamentarioVO recuperarItemSolicitacaoOrcamentoPlanoOrcamentarioPorCategoriaDespesa(
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado,
			SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) {
		
		ItemSolicitacaoOrcamentoPlanoOrcamentarioVO item = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
		Optional<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> optional = solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs().stream().
		filter(p -> p.getCategoriaDespesa().getCodigo().equals(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getCategoriaDespesa().getCodigo())).findFirst();
		if (optional.isPresent()) {
			item = optional.get();
		}
		return item;
	}

	/**
	 * Monta os dados e persite 
	 * @param itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado
	 * @param itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado
	 * @param usuarioVO
	 * @param solicitacaoOrcamentoPlanoOrcamentarioVO
	 * @param item
	 * @throws Exception
	 */
	private void persistirHistoricoRemanejamento(
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado,
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado,
			UsuarioVO usuarioVO, SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO,
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO item, String motivo) throws Exception {

		HistoricoRemanejamentoPlanoOrcamentarioVO obj =  montarDadosHistoricoRemanejamento(itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado, itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado,  usuarioVO, solicitacaoOrcamentoPlanoOrcamentarioVO, motivo);			

		persistir(obj, false, usuarioVO);
	}

	private void validaDadosRemanejamento(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado,
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado ,String motivo) throws Exception {

		boolean existeValorNegativo = itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getDetalhamentoPeriodoOrcamentoVOs().stream().anyMatch(p -> p.getValorRemanejar() < 0.0);

		if (existeValorNegativo) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_valorNegativo"));			
		}

		if (itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getValor() > itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getValorDisponivel()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_valorRemanejadoMaiorRestante"));
		}

		if (!Uteis.isAtributoPreenchido(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getDescricao())) { 
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_descricao"));
		}

		if (!Uteis.isAtributoPreenchido(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getCategoriaDespesa())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_categoriaDespesa"));
		}

		if (!Uteis.isAtributoPreenchido(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getValor())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_valor"));
		}

		if (!Uteis.isAtributoPreenchido(motivo)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_motivo"));
		}

		double valorTotalRemanejar = itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getDetalhamentoPeriodoOrcamentoVOs().stream().mapToDouble(DetalhamentoPeriodoOrcamentoVO::getValorRemanejar).sum();
		if (Uteis.arrendondarForcando2CasasDecimais(valorTotalRemanejar) != Uteis.arrendondarForcando2CasasDecimais(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getValor())) {			
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_valorRemanejadoDiferente"));
		}

		/*for (DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO : itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getDetalhamentoPeriodoOrcamentoVOs()) {
			Double valorTotalRequisicaoItem = getFacadeFactory().getRequisicaoFacade().consultarValorConsumidoPlanoOrcamentario(null, null, null, null, null,  
					detalhamentoPeriodoOrcamentoVO.getMes(), detalhamentoPeriodoOrcamentoVO.getAno(), null, itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado.getCodigo());

			if (Uteis.isAtributoPreenchido(detalhamentoPeriodoOrcamentoVO.getValorRemanejar()) &&
					detalhamentoPeriodoOrcamentoVO.getValorRemanejar() >= valorTotalRequisicaoItem) {
				throw new ConsistirException( UteisJSF.internacionalizar("msg_historicoRemanejamentoPlanoOrcamentario_valorDisponivelRemanejar")
						.replace("{0}", String.valueOf(detalhamentoPeriodoOrcamentoVO.getValorRemanejar() - valorTotalRequisicaoItem))
						.replace("{1}", detalhamentoPeriodoOrcamentoVO.getMesApresentar())
						.replace("{2}", String.valueOf(detalhamentoPeriodoOrcamentoVO.getValorRemanejar())));
			}
		}*/
	}

	private HistoricoRemanejamentoPlanoOrcamentarioVO montarDadosHistoricoRemanejamento(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO,
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado,
			UsuarioVO usuarioVO, SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, String motivo) {

		HistoricoRemanejamentoPlanoOrcamentarioVO obj = new HistoricoRemanejamentoPlanoOrcamentarioVO();
		obj.setCategoriaDespesaVO(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCategoriaDespesa());
		obj.setCategoriaDespesaRemanejado(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getCategoriaDespesa());
		obj.setData(new Date());
		obj.setValor(new BigDecimal(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValor()));
		obj.setValorRemanejado(new BigDecimal(itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado.getValor()));
		obj.setUsuarioVO(usuarioVO);
		obj.setSolicitacaoOrcamentoPlanoOrcamentarioVO(solicitacaoOrcamentoPlanoOrcamentarioVO);
		obj.setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(itemSolicitacaoOrcamentoPlanoOrcamentarioVO);
		obj.setMotivo(motivo);

		return obj;
	}

	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, UsuarioVO usuarioVO) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarHistoricoRemanejamentoPlanoOrcamentario(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalHistoricoRemanejamentoPlanoOrcamentario(dataModelo));
	}

	private List<HistoricoRemanejamentoPlanoOrcamentarioVO> consultarHistoricoRemanejamentoPlanoOrcamentario(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT codigo, usuario, data, valor, categoriadespesa, solicitacaoorcamentoplanoorcamentario, itemsolicitacaoorcamentoplanoorcamentario, motivo, categoriadespesaremanejado, valorremanejado  FROM historicoremanejamentoplanoorcamentario ");
		if (Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			sql.append(" WHERE solicitacaoOrcamentoPlanoOrcamentario = ").append(dataModelo.getValorConsulta());
		}
		dataModelo.setLimitePorPagina(10);

		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

		return montarDadosLista(tabelaResultado);
	}

	private Integer consultarTotalHistoricoRemanejamentoPlanoOrcamentario(DataModelo dataModelo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(codigo) as qtde FROM historicoremanejamentoplanoorcamentario");
		if (Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			sql.append(" WHERE solicitacaoOrcamentoPlanoOrcamentario = ").append(dataModelo.getValorConsulta());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
}