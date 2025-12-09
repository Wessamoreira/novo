package negocio.facade.jdbc.planoorcamentario;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.planoorcamentario.DetalhamentoPeriodoOrcamentarioInterfaceFacade;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class DetalhamentoPeriodoOrcamentario extends ControleAcesso implements DetalhamentoPeriodoOrcamentarioInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1812314744397943990L;
	private static String idEntidade;

    public DetalhamentoPeriodoOrcamentario() {
        super();
        setIdEntidade("DetalhamentoPeriodoOrcamentario");
    }

     @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DetalhamentoPeriodoOrcamentoVO obj, UsuarioVO usuario) throws Exception {
        incluir(obj, "DetalhamentoPeriodoOrcamentario",
					new AtributoPersistencia()
							.add("itemSolicitacaoOrcamentoPlanoOrcamentario", obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO())
							.add("mes", obj.getMes())
							.add("ano", obj.getAno())
							.add("orcamentoRequeridoGestor", obj.getOrcamentoRequeridoGestor())
							.add("orcamentoTotal", obj.getOrcamentoTotal())
							, usuario);
        obj.setNovoObj(Boolean.FALSE);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DetalhamentoPeriodoOrcamentoVO obj, UsuarioVO usuario) throws Exception {
    	if(alterar(obj, "DetalhamentoPeriodoOrcamentario",
				new AtributoPersistencia()
						.add("itemSolicitacaoOrcamentoPlanoOrcamentario", obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO())
						.add("mes", obj.getMes())
						.add("ano", obj.getAno())
						.add("orcamentoRequeridoGestor", obj.getOrcamentoRequeridoGestor())
						.add("orcamentoTotal", obj.getOrcamentoTotal()),
						new AtributoPersistencia().add("codigo", obj.getCodigo()) , usuario).equals(0) ) {
    		incluir(obj, usuario);
    	}
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DetalhamentoPeriodoOrcamentoVO obj, UsuarioVO usuario) throws Exception {
        DetalhamentoPeriodoOrcamentario.excluir(getIdEntidade());
        String sql = "DELETE FROM DetalhamentoPeriodoOrcamentario WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }


    public List<DetalhamentoPeriodoOrcamentoVO> consultarDetalhamentoPorPlanoOrcamentario(Integer planoOrcamentario,  int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        StringBuilder sql  =  new StringBuilder("");
        sql.append("   select categoriadespesa.codigo as categoriadespesa_codigo, categoriadespesa.descricao as categoriadespesa_descricao, ");
        sql.append("   detalhamentoperiodoorcamentario.ano, detalhamentoperiodoorcamentario.mes,");
        sql.append("   sum(itemSolicitacaoOrcamentoPlanoOrcamentario.valor) as orcamentoRequeridoGestor,");
        sql.append("   sum(itemSolicitacaoOrcamentoPlanoOrcamentario.valorautorizado) as orcamentoTotal   ");
        sql.append("   from detalhamentoperiodoorcamentario");
        sql.append("   inner join itemSolicitacaoOrcamentoPlanoOrcamentario on itemSolicitacaoOrcamentoPlanoOrcamentario.codigo = detalhamentoperiodoorcamentario.itemSolicitacaoOrcamentoPlanoOrcamentario");
        sql.append("   inner join SolicitacaoOrcamentoPlanoOrcamentario on itemSolicitacaoOrcamentoPlanoOrcamentario.SolicitacaoOrcamentoPlanoOrcamentario = SolicitacaoOrcamentoPlanoOrcamentario.codigo");
        sql.append("   inner join categoriadespesa on itemSolicitacaoOrcamentoPlanoOrcamentario.categoriadespesa = categoriadespesa.codigo");
        sql.append("   where SolicitacaoOrcamentoPlanoOrcamentario.planoorcamentario = ? ");
        sql.append("   group by detalhamentoperiodoorcamentario.ano, detalhamentoperiodoorcamentario.mes, categoriadespesa.codigo, categoriadespesa.descricao");
        sql.append("   order by detalhamentoperiodoorcamentario.ano, detalhamentoperiodoorcamentario.mes ");
        List<DetalhamentoPeriodoOrcamentoVO> objetos = new ArrayList<DetalhamentoPeriodoOrcamentoVO>(0);

        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), planoOrcamentario);
        DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO = null;
        DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoPorCategoriaDespesaVO = null;
        Map<String, DetalhamentoPeriodoOrcamentoVO> mapDetPer =  new HashMap<String, DetalhamentoPeriodoOrcamentoVO>();
        while (resultado.next()) {
            if(!mapDetPer.containsKey(resultado.getString("ano")+resultado.getString("mes"))) {
            	detalhamentoPeriodoOrcamentoVO =  new DetalhamentoPeriodoOrcamentoVO();

            	if (Uteis.isAtributoPreenchido(resultado.getString("mes"))) {
            		detalhamentoPeriodoOrcamentoVO.setMes(MesAnoEnum.valueOf(resultado.getString("mes")));
            	}
            	detalhamentoPeriodoOrcamentoVO.setAno(resultado.getString("ano"));
            	mapDetPer.put(resultado.getString("ano") + resultado.getString("mes"), detalhamentoPeriodoOrcamentoVO);
            }
            detalhamentoPeriodoOrcamentoVO = mapDetPer.get(resultado.getString("ano") + resultado.getString("mes"));
            detalhamentoPeriodoOrcamentoVO.setOrcamentoRequeridoGestor(detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor()+resultado.getDouble("orcamentoRequeridoGestor"));
            detalhamentoPeriodoOrcamentoVO.setOrcamentoTotal(detalhamentoPeriodoOrcamentoVO.getOrcamentoTotal()+resultado.getDouble("orcamentoTotal"));
            detalhamentoPeriodoOrcamentoPorCategoriaDespesaVO =  new DetalhamentoPeriodoOrcamentoVO();
            detalhamentoPeriodoOrcamentoPorCategoriaDespesaVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().setCodigo(resultado.getInt("categoriadespesa_codigo"));
            detalhamentoPeriodoOrcamentoPorCategoriaDespesaVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().setDescricao(resultado.getString("categoriadespesa_descricao"));
            detalhamentoPeriodoOrcamentoVO.getDetalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs().add(detalhamentoPeriodoOrcamentoPorCategoriaDespesaVO);
        }
        return objetos;
    }
    
    @Override
    public void realizarGeracaoDetalhamentoPorPeriodoPorPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentarioVO) throws Exception {
    	planoOrcamentarioVO.getDetalhamentoPeriodoOrcamento().clear();
		Map<String, DetalhamentoPeriodoOrcamentoVO> mapDetPer =  new HashMap<String, DetalhamentoPeriodoOrcamentoVO>();
		DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO2 = null;

		for(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO : planoOrcamentarioVO.getSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
			for(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO : solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
				if(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs().isEmpty()) {
					itemSolicitacaoOrcamentoPlanoOrcamentarioVO.setDetalhamentoPeriodoOrcamentoVOs(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().executarDistribuicaoValorItemMensal(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorBaseUtilizar(), solicitacaoOrcamentoPlanoOrcamentarioVO.getPlanoOrcamentario().getDataInicio(), solicitacaoOrcamentoPlanoOrcamentarioVO.getPlanoOrcamentario().getDataFinal()));	
				}
				q:
				for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO: itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs()) {
					if(!mapDetPer.containsKey(detalhamentoPeriodoOrcamentoVO.getAno()+detalhamentoPeriodoOrcamentoVO.getMes())) {
						detalhamentoPeriodoOrcamentoVO2 = new DetalhamentoPeriodoOrcamentoVO();
						detalhamentoPeriodoOrcamentoVO2.setAno(detalhamentoPeriodoOrcamentoVO.getAno());
						
						detalhamentoPeriodoOrcamentoVO2.setMes(detalhamentoPeriodoOrcamentoVO.getMes());
						
						detalhamentoPeriodoOrcamentoVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setCategoriaDespesa(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCategoriaDespesa());
						mapDetPer.put(detalhamentoPeriodoOrcamentoVO.getAno()+detalhamentoPeriodoOrcamentoVO.getMes(), detalhamentoPeriodoOrcamentoVO2);
					}
	
					detalhamentoPeriodoOrcamentoVO2 = mapDetPer.get(detalhamentoPeriodoOrcamentoVO.getAno()+detalhamentoPeriodoOrcamentoVO.getMes());
					detalhamentoPeriodoOrcamentoVO2.setOrcamentoRequeridoGestor(detalhamentoPeriodoOrcamentoVO2.getOrcamentoRequeridoGestor()+detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor());
					detalhamentoPeriodoOrcamentoVO2.setOrcamentoTotal(detalhamentoPeriodoOrcamentoVO2.getOrcamentoTotal()+detalhamentoPeriodoOrcamentoVO.getOrcamentoTotal());
					detalhamentoPeriodoOrcamentoVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setCategoriaDespesa(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCategoriaDespesa());
					for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO3 : detalhamentoPeriodoOrcamentoVO2.getDetalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs()) {
						if(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCategoriaDespesa().getCodigo().equals(detalhamentoPeriodoOrcamentoVO3.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().getCategoriaDespesa().getCodigo())) {
							detalhamentoPeriodoOrcamentoVO3.setOrcamentoRequeridoGestor(detalhamentoPeriodoOrcamentoVO3.getOrcamentoRequeridoGestor()+detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor());
							detalhamentoPeriodoOrcamentoVO3.setOrcamentoTotal(detalhamentoPeriodoOrcamentoVO3.getOrcamentoTotal()+detalhamentoPeriodoOrcamentoVO.getOrcamentoTotal());
							continue q;
						}
					}
					if (detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() > 0.0) {
						detalhamentoPeriodoOrcamentoVO2.getDetalhamentoPeriodoOrcamentoPorCategoriaDespesaVOs().add((DetalhamentoPeriodoOrcamentoVO)detalhamentoPeriodoOrcamentoVO.clone());
					}
				}
			}
		}

		planoOrcamentarioVO.getDetalhamentoPeriodoOrcamento().addAll(mapDetPer.values());
		Ordenacao.ordenarLista(planoOrcamentarioVO.getDetalhamentoPeriodoOrcamento(), "ordenacao");
    }

    @Override
    public List<DetalhamentoPeriodoOrcamentoVO> consultarDetalhamentoPorItemSolicitacaoPlanoOrcamentario(Integer itemSolicitacaoPlanoOrcamentario,  int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	StringBuilder sql  =  new StringBuilder("select *, ( ");
    	sql.append(getFacadeFactory().getRequisicaoFacade().getSqlBaseValorConsumidoPlanoOrcamentario(null, null, null, null, null, null, null, null));
    	sql.append(" and requisicaoitem.itemSolicitacaoOrcamentoPlanoOrcamentario = detalhamentoperiodoorcamentario.itemSolicitacaoOrcamentoPlanoOrcamentario ");
    	sql.append(" and extract(year from requisicao.datarequisicao) = detalhamentoperiodoorcamentario.ano::int ");
    	sql.append(" and extract(month from requisicao.datarequisicao) = ");
    	sql.append(" case detalhamentoperiodoorcamentario.mes when 'JANEIRO' then 1");
    	sql.append(" when 'FEVEREIRO' then 2");
    	sql.append(" when 'MARCO' then 3");
    	sql.append(" when 'ABRIL' then 4");
    	sql.append(" when 'MAIO' then 5");
    	sql.append(" when 'JUNHO' then 6");
    	sql.append(" when 'JULHO' then 7");
    	sql.append(" when 'AGOSTO' then 8");
    	sql.append(" when 'SETEMBRO' then 9");
    	sql.append(" when 'OUTUBRO' then 10");
    	sql.append(" when 'NOVEMBRO' then 11");
    	sql.append(" when 'DEZEMBRO' then 12");
    	sql.append(" else 0 end ) AS valorconsumido");
    
    	sql.append(" from detalhamentoperiodoorcamentario ");    	
    	sql.append(" where itemSolicitacaoOrcamentoPlanoOrcamentario = ? order by ano,  ");
    	sql.append(" case detalhamentoperiodoorcamentario.mes when 'JANEIRO' then 1");
    	sql.append(" when 'FEVEREIRO' then 2");
    	sql.append(" when 'MARCO' then 3");
    	sql.append(" when 'ABRIL' then 4");
    	sql.append(" when 'MAIO' then 5");
    	sql.append(" when 'JUNHO' then 6");
    	sql.append(" when 'JULHO' then 7");
    	sql.append(" when 'AGOSTO' then 8");
    	sql.append(" when 'SETEMBRO' then 9");
    	sql.append(" when 'OUTUBRO' then 10");
    	sql.append(" when 'NOVEMBRO' then 11");
    	sql.append(" when 'DEZEMBRO' then 12");
    	sql.append(" else 0 end");
    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{itemSolicitacaoPlanoOrcamentario});
    	return montarDadosConsulta(resultado, nivelMontarDados, usuario);
    }

    public static List<DetalhamentoPeriodoOrcamentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<DetalhamentoPeriodoOrcamentoVO> vetResultado = new ArrayList<DetalhamentoPeriodoOrcamentoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    public static DetalhamentoPeriodoOrcamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DetalhamentoPeriodoOrcamentoVO obj = new DetalhamentoPeriodoOrcamentoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        if (Uteis.isAtributoPreenchido(dadosSQL.getString("mes"))) {
        	obj.setMes(MesAnoEnum.valueOf(dadosSQL.getString("mes")));
        }
        obj.setAno(dadosSQL.getString("ano"));
        obj.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setCodigo(dadosSQL.getInt("itemSolicitacaoOrcamentoPlanoOrcamentario"));
        obj.setOrcamentoRequeridoGestor(dadosSQL.getDouble("orcamentoRequeridoGestor"));
        obj.setOrcamentoTotal(dadosSQL.getDouble("orcamentoTotal"));
        obj.setValorConsumido(dadosSQL.getDouble("valorconsumido"));

        return obj;
    }


    public static String getIdEntidade() {
        return idEntidade;
    }

    public static void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirDetalhamentoPeriodoOrcamentario(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO, UsuarioVO usuario) throws Exception {
        for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO: itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs()) {            
        	detalhamentoPeriodoOrcamentoVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setCodigo(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo());
            incluir(detalhamentoPeriodoOrcamentoVO, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDetalhamentoPeriodoOrcamentario(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO, UsuarioVO usuario) throws Exception {
        excluirDetalhamentoPeriodoOrcamentario(itemSolicitacaoOrcamentoPlanoOrcamentarioVO, usuario);
        for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO: itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs()) {            
        	detalhamentoPeriodoOrcamentoVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVO().setCodigo(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo());
        	if(Uteis.isAtributoPreenchido(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo())) {
        		alterar(detalhamentoPeriodoOrcamentoVO, usuario);
        	}else {
        		incluir(detalhamentoPeriodoOrcamentoVO, usuario);
        	}
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirDetalhamentoPeriodoOrcamentario(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO, UsuarioVO usuario) throws Exception {
        DetalhamentoPlanoOrcamentario.excluir(getIdEntidade());
        StringBuilder sql = new StringBuilder("DELETE FROM DetalhamentoPeriodoOrcamentario WHERE (itemSolicitacaoOrcamentoPlanoOrcamentario = ?) and codigo not in (0");
        for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO: itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs()) {
        	if(Uteis.isAtributoPreenchido(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo())) {
        		sql.append(", ").append(detalhamentoPeriodoOrcamentoVO.getCodigo());
        	}
        }
        sql.append(")");
        sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        getConexao().getJdbcTemplate().update(sql.toString(), new Object[]{itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo()});
    }


    /**
     * Método responsável por montar a lista de meses dependendo do Período do Plano Orcamentario e calcular o valor para cada mês.
     * Isso por departamento.
     * @param obj
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws Exception
     */
    public List<DetalhamentoPeriodoOrcamentoVO> executarDistribuicaoValorItemMensal(Double orcamentoTotalPrevisto, Date dataInicio, Date dataFim) throws Exception {
        List<DetalhamentoPeriodoOrcamentoVO> listaDetalhamentoPeriodoOrcamentarioVOs = new ArrayList<DetalhamentoPeriodoOrcamentoVO>(0);
        Integer quantidadeMeses = Uteis.obterQuantidadeMesesEntreDatas(dataInicio, dataFim).intValue();
        Double valorDistribuido = Uteis.arredondarAbaixo(calcularValorDistribuicaoPeriodo(orcamentoTotalPrevisto, dataInicio, dataFim, quantidadeMeses), 2, 1);
        Double restoValorQuebrado = calcularRestoValorQuebrado(orcamentoTotalPrevisto, valorDistribuido, quantidadeMeses);
        Boolean primeiraVez = Boolean.TRUE;
        while (dataInicio.before(dataFim) || dataInicio.equals(dataFim)) {
            DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentarioVO = new DetalhamentoPeriodoOrcamentoVO();

            detalhamentoPeriodoOrcamentarioVO.setMes(MesAnoEnum.getEnum(String.valueOf(Uteis.getMesData(dataInicio))));
            detalhamentoPeriodoOrcamentarioVO.setAno(String.valueOf(Uteis.getAnoData(dataInicio)));

            if (primeiraVez && restoValorQuebrado != 0) {
                detalhamentoPeriodoOrcamentarioVO.setOrcamentoRequeridoGestor(valorDistribuido + Uteis.arredondar(restoValorQuebrado, 2, 1));
            } else {
                detalhamentoPeriodoOrcamentarioVO.setOrcamentoRequeridoGestor(valorDistribuido);
            }
            dataInicio = Uteis.getDataFutura(dataInicio, GregorianCalendar.MONTH, 1);
            listaDetalhamentoPeriodoOrcamentarioVOs.add(detalhamentoPeriodoOrcamentarioVO);
            primeiraVez = Boolean.FALSE;
        }
        return listaDetalhamentoPeriodoOrcamentarioVOs;
    }

    @Override
    public List<DetalhamentoPeriodoOrcamentoVO> executarRedistribuicaoValorItemMensal(List<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamentoVOs, Double orcamentoTotalPrevisto) throws Exception {
    	Double valorTotal = detalhamentoPeriodoOrcamentoVOs.stream().mapToDouble(DetalhamentoPeriodoOrcamentoVO::getOrcamentoRequeridoGestor).sum();
    	Double valorJaDistribuido =  0.0;
        for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO: detalhamentoPeriodoOrcamentoVOs) {
        	if(detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() > 0) {
        		Double porcentagem  =  detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor()*100 / valorTotal;
        		detalhamentoPeriodoOrcamentoVO.setOrcamentoRequeridoGestor(Uteis.arrendondarForcando2CadasDecimais((orcamentoTotalPrevisto*porcentagem)/100));
        		valorJaDistribuido += Uteis.arrendondarForcando2CadasDecimais((orcamentoTotalPrevisto*porcentagem)/100);
        	}
        }
        if(valorJaDistribuido > orcamentoTotalPrevisto) {
        	for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO: detalhamentoPeriodoOrcamentoVOs) {
            	if(detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() > 0 && detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() > (valorJaDistribuido - orcamentoTotalPrevisto)) {
            		detalhamentoPeriodoOrcamentoVO.setOrcamentoRequeridoGestor(detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() - (valorJaDistribuido - orcamentoTotalPrevisto));
            		break;
            	}
        	}
        }else if(valorJaDistribuido < orcamentoTotalPrevisto) {
        	for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO: detalhamentoPeriodoOrcamentoVOs) {
            	if(detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() > 0) {
            		detalhamentoPeriodoOrcamentoVO.setOrcamentoRequeridoGestor(detalhamentoPeriodoOrcamentoVO.getOrcamentoRequeridoGestor() + (orcamentoTotalPrevisto - valorJaDistribuido));
            		break;
            	}
        	}
        }
        return detalhamentoPeriodoOrcamentoVOs;
    }

    public Double calcularValorDistribuicaoPeriodo(Double orcamentoTotalPrevisto, Date dataInicio, Date dataFim, Integer quantidadeMeses) {
        return orcamentoTotalPrevisto / quantidadeMeses;
    }

    public Double calcularRestoValorQuebrado(Double orcamentoTotalPrevisto, Double valorDistribuido, Integer quantidadeMeses) {
        Double resto = 0.0;
        Double valorMultiplicadoMes = Uteis.arrendondarForcando2CadasDecimais(valorDistribuido * quantidadeMeses);
        if (!orcamentoTotalPrevisto.equals(valorMultiplicadoMes)) {
            resto = orcamentoTotalPrevisto - valorMultiplicadoMes;
            return resto;
        }
        return 0.0;
    }

    public void validarDadosValorMensalOrcamentoTotalPrevisto(Double valorDetalhamento, List<DetalhamentoPeriodoOrcamentoVO> listaItemMensal) throws ConsistirException {
        Double valorFinal = 0.0;
        for (DetalhamentoPeriodoOrcamentoVO itemMensal : listaItemMensal) {
            valorFinal = valorFinal + itemMensal.getOrcamentoRequeridoGestor();
        }
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        if (Uteis.arrendondarForcando2CadasDecimais(valorFinal) > Uteis.arrendondarForcando2CadasDecimais(valorDetalhamento)) {
            throw new ConsistirException("A soma dos valores mensais informado (R$ "+ df.format(valorFinal) +"), está superior ao valor do Orçamento Total Previsto (R$ "+ df.format(valorDetalhamento) +"). ");
        }
        if (Uteis.arrendondarForcando2CadasDecimais(valorFinal) < Uteis.arrendondarForcando2CadasDecimais(valorDetalhamento)) {
            throw new ConsistirException("A soma dos valores mensais informado (R$ "+ df.format(valorFinal) +"), está inferior ao valor do Orçamento Total Previsto (R$ "+ df.format(valorDetalhamento) +").");
        }
    }

    public void validarDadosValorOrcamentoTotalPrevisto(Double valorOrcamentoTotal) throws Exception {
        if (valorOrcamentoTotal == null || valorOrcamentoTotal == 0.0) {
            throw new Exception("O campo ORÇAMENTO TOTAL PREVISTO deve ser informado.");
        }
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
   	public void alterarValorPorMesDeAcordoComValorAprovado(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO, UsuarioVO usuarioVO) throws Exception{

		 for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO: itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getDetalhamentoPeriodoOrcamentoVOs()) {
			 getConexao().getJdbcTemplate().update("update DetalhamentoPeriodoOrcamentario set orcamentoTotal = ? where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), detalhamentoPeriodoOrcamentoVO.getOrcamentoTotal(), detalhamentoPeriodoOrcamentoVO.getCodigo());   			 
		 }
    	 
   	}

}
