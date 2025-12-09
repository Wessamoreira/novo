/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.compras;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.compras.RequisicaoRelVO;
import relatorio.negocio.interfaces.compras.RequisicaoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class RequisicaoRel extends SuperRelatorio implements RequisicaoRelInterfaceFacade {

	private static final long serialVersionUID = 2650586555161563831L;

	public List<RequisicaoRelVO> criarObjetoLayout1(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, Date dataInicio, Date dataFim, RequisicaoVO requisicao) throws Exception {
        List<RequisicaoRelVO> requisicaoRelVOs = new ArrayList<RequisicaoRelVO>(0);
        validarDados(listaUnidadeEnsino, dataInicio, dataFim);
        SqlRowSet dadosSQL = executarPesquisaParametrizadaLayout1(listaUnidadeEnsino, departamentoVO, categoriaDespesa, categoriaProduto, produtoServico, situacaoEntrega, dataInicio, dataFim, requisicao);
        requisicaoRelVOs = montarDadosLayout1(dadosSQL, requisicaoRelVOs);
        requisicaoRelVOs = montarQuantidadeEstoque(requisicaoRelVOs, listaUnidadeEnsino);
        return requisicaoRelVOs;
    }
    
    public List<RequisicaoRelVO> criarObjetoLayout2(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, Date dataInicio, Date dataFim, RequisicaoVO requisicao) throws Exception {
        List<RequisicaoRelVO> requisicaoRelVOs = new ArrayList<RequisicaoRelVO>(0);
        validarDados(listaUnidadeEnsino, dataInicio, dataFim);
        SqlRowSet dadosSQL = executarPesquisaParametrizadaLayout2(listaUnidadeEnsino, departamentoVO, categoriaDespesa, categoriaProduto, produtoServico, situacaoEntrega, dataInicio, dataFim, requisicao);
        requisicaoRelVOs = montarDadosLayout2(dadosSQL, requisicaoRelVOs);
        requisicaoRelVOs = montarPrecoUnitarioProdutoEstoque(requisicaoRelVOs, listaUnidadeEnsino);
        return requisicaoRelVOs;
    }
    
    public List<RequisicaoRelVO> criarObjetoLayout3(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, Date dataInicio, Date dataFim, RequisicaoVO requisicao) throws Exception {
        List<RequisicaoRelVO> requisicaoRelVOs = new ArrayList<RequisicaoRelVO>(0);
        validarDados(listaUnidadeEnsino, dataInicio, dataFim);
        SqlRowSet dadosSQL = executarPesquisaParametrizadaLayout3(listaUnidadeEnsino, departamentoVO, categoriaDespesa, categoriaProduto, produtoServico, situacaoEntrega, dataInicio, dataFim, requisicao);
        requisicaoRelVOs = montarDadosLayout3(dadosSQL, requisicaoRelVOs);
        requisicaoRelVOs = montarPrecoUnitarioProdutoEstoque(requisicaoRelVOs, listaUnidadeEnsino);
        return requisicaoRelVOs;
    }
    
    public List<RequisicaoRelVO> criarObjetoLayout4(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, RequisicaoVO requisicao, List<CentroResultadoVO> listaCentroResultadoEstoque, Date dataInicioPeriodoConsumo, Date dataFimPeriodoConsumo) throws Exception{
        List<RequisicaoRelVO> requisicaoRelVOs = new ArrayList<RequisicaoRelVO>(0);
        validarDados(listaUnidadeEnsino, dataInicioPeriodoConsumo, dataFimPeriodoConsumo);
        SqlRowSet dadosSQL = executarPesquisaParametrizadaLayout4(listaUnidadeEnsino, departamentoVO, categoriaDespesa, categoriaProduto, produtoServico, situacaoEntrega, requisicao, listaCentroResultadoEstoque, dataInicioPeriodoConsumo, dataFimPeriodoConsumo);
        requisicaoRelVOs = montarDadosLayout4(dadosSQL, requisicaoRelVOs);
       // requisicaoRelVOs = montarPrecoUnitarioProdutoEstoque(requisicaoRelVOs, unidadeEnsino);
        return requisicaoRelVOs;
    }

    public void validarDados(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataFim) throws Exception {
        if (listaUnidadeEnsino.isEmpty()) {
            throw new ConsistirException("É necessário informar a UNIDADE DE ENSINO para geração desse relatório.");
        }

		if(dataInicio == null) {
			throw new Exception("A Data Inicial deve ser informada para a geração do relatório.");
		}
		else if (dataFim == null) {
			throw new ConsistirException("A Data Final deve ser informada para a geração do relatório.");
		}else { 

			ZoneId defaultZoneId = ZoneId.systemDefault();				

			LocalDate ldDataInicio = dataInicio.toInstant().atZone(defaultZoneId).toLocalDate();
			LocalDate ldDataFim = dataFim.toInstant().atZone(defaultZoneId).toLocalDate();

			Period periodo = Period.between(ldDataInicio, ldDataFim);

			if((periodo.getYears() > 1) || (periodo.getYears() == 1 && (periodo.getMonths() > 0 || periodo.getDays() > 0))) {
				throw new ConsistirException("O intervalo deve ser menor que um ano para a geração do relatório.");
			}

			if(ldDataInicio.isAfter(LocalDate.now())){
				throw new ConsistirException("A Data Inicial não pode ser maior que a data atual..");
			}

			if (ldDataFim.isAfter(LocalDate.now())) {
				throw new ConsistirException("A Data Final não pode ser maior que a data atual.");
			}
			if (ldDataInicio.isAfter(ldDataFim)) {
				throw new ConsistirException("A Data Inicial não pode ser maior que a data Final.");
			}
		}
        
    }

    private List<RequisicaoRelVO> montarDadosLayout1(SqlRowSet dadosSQL, List<RequisicaoRelVO> requisicaoRelVOs) {
        while (dadosSQL.next()) {
            RequisicaoRelVO obj = new RequisicaoRelVO();
            obj.setCategoriaDespesa(dadosSQL.getString("categoriaDespesa"));
            obj.setCategoriaProduto(dadosSQL.getString("categoriaProduto"));
            obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
            obj.setSituacao(dadosSQL.getString("situacao"));
            obj.setDepartamento(dadosSQL.getString("nomeDepartamento"));
            obj.setDataSolicitacao(dadosSQL.getDate("dataRequisicao"));
            obj.setProduto(dadosSQL.getString("produto"));
            obj.setRequisitante(dadosSQL.getString("requisitante"));
            obj.setQuantidadeSolicitada(dadosSQL.getDouble("quantidadeSolicitada"));
            obj.setCodigoProduto(dadosSQL.getInt("codigoProduto"));
            requisicaoRelVOs.add(obj);
        }
        return requisicaoRelVOs;
    }
    
    private List<RequisicaoRelVO> montarDadosLayout2(SqlRowSet dadosSQL, List<RequisicaoRelVO> requisicaoRelVOs) {
        while (dadosSQL.next()) {
            RequisicaoRelVO obj = new RequisicaoRelVO();
            obj.setCategoriaDespesa(dadosSQL.getString("categoriaDespesa"));
            obj.setCategoriaProduto(dadosSQL.getString("categoriaProduto"));
            obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
            obj.setDepartamento(dadosSQL.getString("nomeDepartamento"));
            obj.setDataSolicitacao(dadosSQL.getDate("dataRequisicao"));
            obj.setQuantidadeSolicitada(dadosSQL.getDouble("quantidadeSolicitada"));
            obj.setProduto(dadosSQL.getString("produto"));
            obj.setCodigoProduto(dadosSQL.getInt("codigoProduto"));
            requisicaoRelVOs.add(obj);
        }
        return requisicaoRelVOs;
    }
    
    private List<RequisicaoRelVO> montarDadosLayout3(SqlRowSet dadosSQL, List<RequisicaoRelVO> requisicaoRelVOs) {
        while (dadosSQL.next()) {
            RequisicaoRelVO obj = new RequisicaoRelVO();
            obj.setCodigoRequisicao(dadosSQL.getInt("codigoRequisicao"));
            obj.setDataSolicitacao(dadosSQL.getDate("dataRequisicao"));            
            obj.setRequisitante(dadosSQL.getString("requisitante"));
            obj.setProduto(dadosSQL.getString("produto"));
            obj.setQuantidadeSolicitada(dadosSQL.getDouble("quantidade"));
            obj.setValorUnitario(dadosSQL.getDouble("valorunitario"));
            obj.setValorTotal(dadosSQL.getDouble("quantidade") * dadosSQL.getDouble("valorunitario"));
            obj.setCentroResultado(dadosSQL.getString("centroResultado"));
            obj.setSituacao(dadosSQL.getString("situacaoentrega"));
            obj.setFormaAutorizacao(dadosSQL.getString("formaAutorizacao"));
            obj.setResponsavelAutorizacao(dadosSQL.getString("responsavelAutorizacao"));
            obj.setDataAutorizacao(dadosSQL.getDate("dataAutorizacao"));
            obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));            
            obj.setSituacaoAutorizacao(dadosSQL.getString("situacaoautorizacao"));

            requisicaoRelVOs.add(obj);
            
        }
        return requisicaoRelVOs;
    }
    
    private List<RequisicaoRelVO> montarDadosLayout4(SqlRowSet dadosSQL, List<RequisicaoRelVO> requisicaoRelVOs) {
        while (dadosSQL.next()) {
            RequisicaoRelVO obj = new RequisicaoRelVO();
            obj.setCentroResultadoEstoque(dadosSQL.getString("centroResultadoEstoque"));
            obj.setCentroResultado(dadosSQL.getString("centroResultadoRequisitante"));
            obj.setCodigoCentroResultadoEstoque(dadosSQL.getInt("codigoCentroResultadoEstoque"));
            obj.setCodigoCentroResultado(dadosSQL.getInt("codigoCentroResultadoRequisistante"));
            obj.setDataConsumo(dadosSQL.getDate("dataConsumo"));
            obj.setProduto(dadosSQL.getString("produto"));
            obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
            obj.setQuantidadeRequisicaoEntrega(dadosSQL.getDouble("quantidadeRequisicaoEntrega"));
            obj.setPrecoMedioUnitario(dadosSQL.getDouble("precoMedioUnitario"));
            obj.setPrecoMedioTotal(dadosSQL.getDouble("precoMedioTotal"));
            

            requisicaoRelVOs.add(obj);
        }
        return requisicaoRelVOs;
    }

    private List<RequisicaoRelVO> montarQuantidadeEstoque(List<RequisicaoRelVO> listaObjs, List<UnidadeEnsinoVO> listaUnidadeEnsino) {
        for (RequisicaoRelVO requisicaoRelVO : listaObjs) {
            SqlRowSet dadosSQL = executarPesquisaQuantidadeEstoque(requisicaoRelVO.getCodigoProduto(), listaUnidadeEnsino);
            Integer quantidadeEstoque = 0;
            while (dadosSQL.next()) {
                Double doubleValue = dadosSQL.getDouble("quantidade");
                quantidadeEstoque += doubleValue.intValue();
            }
            requisicaoRelVO.setQuantidadeEstoque(quantidadeEstoque);
        }
        return listaObjs;
    }
    
    private List<RequisicaoRelVO> montarPrecoUnitarioProdutoEstoque(List<RequisicaoRelVO> listaObjs, List<UnidadeEnsinoVO> listaUnidadeEnsino) {
        for (RequisicaoRelVO requisicaoRelVO : listaObjs) {
            SqlRowSet dadosSQL = executarPesquisaPrecoUnitarioProdutoEstoque(requisicaoRelVO.getCodigoProduto(), listaUnidadeEnsino);
            if (dadosSQL.next()) {
            	requisicaoRelVO.setValorUnitario(dadosSQL.getDouble("precoUnitario"));
            	requisicaoRelVO.setValorTotal(dadosSQL.getDouble("precoUnitario") * requisicaoRelVO.getQuantidadeSolicitada());
            }
        }
        return listaObjs;
    }

    private SqlRowSet executarPesquisaQuantidadeEstoque(Integer codigoProduto, List<UnidadeEnsinoVO> listaUnidadeEnsino) {
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT quantidade ");
        sqlsb.append("FROM estoque  ");
        sqlsb.append("INNER JOIN produtoservico ON estoque.produto = produtoservico.codigo ");
        sqlsb.append("where produtoservico.codigo = ").append(codigoProduto);
		if (!listaUnidadeEnsino.isEmpty()) {
			boolean virgula = false;
			sqlsb.append(" AND unidadeEnsino IN(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (!virgula) {
						sqlsb.append(unidadeEnsinoVO.getCodigo());
					} else {
						sqlsb.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
					virgula = true;
				}
			}
			sqlsb.append(") ");
		}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        return tabelaResultado;
    }
    
    private SqlRowSet executarPesquisaPrecoUnitarioProdutoEstoque(Integer codigoProduto, List<UnidadeEnsinoVO> listaUnidadeEnsino) {
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT precoUnitario ");
        sqlsb.append("FROM estoque  ");
        sqlsb.append("INNER JOIN produtoservico ON estoque.produto = produtoservico.codigo ");
        sqlsb.append("where produtoservico.codigo = ").append(codigoProduto);
        
		if (!listaUnidadeEnsino.isEmpty()) {
			boolean virgula = false;
			sqlsb.append(" AND unidadeEnsino IN(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (!virgula) {
						sqlsb.append(unidadeEnsinoVO.getCodigo());
					} else {
						sqlsb.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
					virgula = true;
				}
			}
			sqlsb.append(") ");
		}
		
        sqlsb.append(" and precoUnitario <> 0.0 and precoUnitario is not null ");
        sqlsb.append(" order by estoque.codigo desc limit 1 ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        return tabelaResultado;
    }

    private SqlRowSet executarPesquisaParametrizadaLayout1(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, Date dataInicio, Date dataFim, RequisicaoVO requisicao) {
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT requisicaoitem.quantidadesolicitada AS quantidadeSolicitada, requisicao.dataRequisicao AS dataRequisicao, pessoa.nome AS requisitante, departamento.nome AS nomeDepartamento, unidadeEnsino.nome AS unidadeEnsino, requisicao.situacaoentrega AS situacao, categoriadespesa.codigo, categoriadespesa.descricao AS categoriaDespesa, categoriaproduto.codigo, categoriaproduto.nome AS categoriaProduto, produtoservico.codigo AS codigoProduto, produtoservico.nome AS produto ");
        sqlsb.append("FROM requisicao ");
		sqlsb.append("INNER JOIN usuario ON requisicao.solicitanteRequisicao = usuario.codigo ");
		sqlsb.append("INNER JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
		sqlsb.append("INNER JOIN categoriadespesa ON categoriadespesa.codigo = requisicao.categoriadespesa ");
        sqlsb.append("INNER JOIN categoriaproduto ON categoriaproduto.codigo = requisicao.categoriaproduto ");
        sqlsb.append("LEFT JOIN departamento ON departamento.codigo = requisicao.departamento ");
        sqlsb.append("LEFT JOIN requisicaoitem ON requisicaoitem.requisicao = requisicao.codigo ");
        sqlsb.append("INNER JOIN produtoservico ON produtoservico.codigo = requisicaoitem.produtoservico ");
        sqlsb.append("INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = requisicao.unidadeEnsino ");
        sqlsb.append("where 1 = 1");
        montarFiltrosComunsParaLayout(sqlsb, "RequisicaoRel1", listaUnidadeEnsino, departamentoVO, categoriaDespesa, categoriaProduto, produtoServico, situacaoEntrega, requisicao, new ArrayList<>(), dataInicio, dataFim);
        sqlsb.append(" order by departamento.nome, categoriadespesa.descricao, categoriaproduto.nome, requisicao.dataRequisicao, produtoservico.nome   ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        return tabelaResultado;
    }
    
    private SqlRowSet executarPesquisaParametrizadaLayout2(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, Date dataInicio, Date dataFim, RequisicaoVO requisicao) {
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT distinct produtoservico.codigo AS codigoProduto, departamento.nome AS nomeDepartamento, unidadeEnsino.nome AS unidadeEnsino, categoriadespesa.codigo, categoriadespesa.descricao AS categoriaDespesa, categoriaproduto.codigo, categoriaproduto.nome AS categoriaProduto, produtoservico.codigo AS codigoProduto, produtoservico.nome AS produto, ");
        sqlsb.append(" requisicaoItem.quantidadesolicitada AS quantidadesolicitada, requisicao.dataRequisicao AS dataRequisicao ");
        sqlsb.append("FROM requisicao ");
        sqlsb.append("INNER JOIN categoriadespesa ON categoriadespesa.codigo = requisicao.categoriadespesa ");
        sqlsb.append("INNER JOIN categoriaproduto ON categoriaproduto.codigo = requisicao.categoriaproduto ");
        sqlsb.append("LEFT JOIN departamento ON departamento.codigo = requisicao.departamento ");
        sqlsb.append("LEFT JOIN requisicaoitem ON requisicaoitem.requisicao = requisicao.codigo ");
        sqlsb.append("INNER JOIN produtoservico ON produtoservico.codigo = requisicaoitem.produtoservico ");
        sqlsb.append("INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = requisicao.unidadeEnsino ");
        sqlsb.append("INNER JOIN estoque ON produtoservico.codigo = estoque.produto ");
        sqlsb.append("where 1 = 1");
        montarFiltrosComunsParaLayout(sqlsb, "RequisicaoRel2", listaUnidadeEnsino, departamentoVO, categoriaDespesa, categoriaProduto, produtoServico, situacaoEntrega, requisicao, new ArrayList<>(), dataInicio, dataFim);	
        sqlsb.append(" order by departamento.nome, categoriadespesa.descricao, categoriaproduto.nome, requisicao.dataRequisicao, produtoservico.nome   ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        return tabelaResultado;
    }
    
    private SqlRowSet executarPesquisaParametrizadaLayout3(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, Date dataInicio, Date dataFim, RequisicaoVO requisicao) {
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT requisicao.codigo AS codigoRequisicao, ");
        sqlsb.append("requisicao.datarequisicao As dataRequisicao, ");
        sqlsb.append("pessoaresponsavelrequisicao.nome AS requisitante, ");
        sqlsb.append("produtoservico.nome AS produto, ");
        sqlsb.append("requisicaoitem.quantidadesolicitada AS quantidade, ");
        sqlsb.append("requisicaoitem.valorunitario, ");
        sqlsb.append("centroresultado.descricao AS centroResultado, ");
        sqlsb.append("requisicao.situacaoentrega, ");
        sqlsb.append("requisicao.tipoautorizacaorequisicao AS formaAutorizacao, ");
        sqlsb.append("pessoaresponsavelautorizacao.nome AS responsavelAutorizacao, ");
        sqlsb.append("requisicao.dataautorizacao AS dataAutorizacao, ");
        sqlsb.append("unidadeensino.nome AS unidadeEnsino, ");
        sqlsb.append("requisicao.situacaoautorizacao AS situacaoautorizacao ");
        sqlsb.append("FROM requisicao ");
        sqlsb.append("INNER JOIN usuario AS usuarioresponsavelrequisicao ON requisicao.solicitanteRequisicao = usuarioresponsavelrequisicao.codigo ");
        sqlsb.append("INNER JOIN pessoa AS pessoaresponsavelrequisicao ON usuarioresponsavelrequisicao.pessoa = pessoaresponsavelrequisicao.codigo ");
        sqlsb.append("LEFT JOIN requisicaoitem ON requisicaoitem.requisicao = requisicao.codigo ");
        sqlsb.append("INNER JOIN produtoservico ON produtoservico.codigo = requisicaoitem.produtoservico ");
        sqlsb.append("LEFT JOIN centroresultado ON requisicao.centroresultadoadministrativo = centroresultado.codigo ");
        sqlsb.append("LEFT JOIN usuario as usuarioresponsavelautorizacao ON requisicao.responsavelautorizacao = usuarioresponsavelautorizacao.codigo ");
        sqlsb.append("LEFT JOIN pessoa as pessoaresponsavelautorizacao ON usuarioresponsavelautorizacao.pessoa = pessoaresponsavelautorizacao.codigo ");
        sqlsb.append("INNER JOIN unidadeensino on requisicao.unidadeensino = unidadeensino.codigo ");
        sqlsb.append("LEFT JOIN departamento ON departamento.codigo = requisicao.departamento ");
        sqlsb.append("INNER JOIN categoriadespesa ON categoriadespesa.codigo = requisicao.categoriadespesa ");
        sqlsb.append("INNER JOIN categoriaproduto ON categoriaproduto.codigo = requisicao.categoriaproduto ");
        sqlsb.append("where 1 = 1");
        montarFiltrosComunsParaLayout(sqlsb, "RequisicaoRel3", listaUnidadeEnsino, departamentoVO, categoriaDespesa, categoriaProduto, produtoServico, situacaoEntrega, requisicao, new ArrayList<>(), dataInicio, dataFim);        
        sqlsb.append(" order by requisicao.codigo, produtoservico.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        return tabelaResultado;
    }

    private SqlRowSet executarPesquisaParametrizadaLayout4(List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, RequisicaoVO requisicao, List<CentroResultadoVO> listaCentroResultadoEstoque, Date dataInicioPeriodoConsumo, Date dataFimPeriodoConsumo) {
        StringBuilder sqlsb = new StringBuilder();
        sqlsb.append("SELECT entregarequisicao.data as dataConsumo, ");
        sqlsb.append("unidadeensino.nome as unidadeEnsino, ");
        sqlsb.append("produtoservico.nome as produto, ");
        sqlsb.append("centroresultado.descricao as centroResultadoRequisitante, ");
        sqlsb.append("centroresultadoestoque.descricao as centroResultadoEstoque, ");
        sqlsb.append("centroresultadoestoque.codigo as codigoCentroResultadoEstoque, ");
        sqlsb.append("centroresultado.codigo as codigoCentroResultadoRequisistante, ");
        sqlsb.append("sum(operacaoestoque.quantidade) as quantidadeRequisicaoEntrega,  ");
        sqlsb.append("sum(estoque.precounitario * case when coalesce(operacaoestoque.quantidade, '0') = 0 then 1 else operacaoestoque.quantidade end ) / sum(case when coalesce(operacaoestoque.quantidade, '0') = 0 then 1 else operacaoestoque.quantidade end) as precoMedioUnitario,");
        sqlsb.append("(sum(estoque.precounitario * case when coalesce(operacaoestoque.quantidade, '0') = 0 then 1 else operacaoestoque.quantidade end) / sum(case when coalesce(operacaoestoque.quantidade, '0') = 0 then 1 else operacaoestoque.quantidade end)) * sum(case when coalesce(operacaoestoque.quantidade, '0') = 0 then 1 else operacaoestoque.quantidade end) as precoMedioTotal");
        sqlsb.append(" FROM entregarequisicao ");
        sqlsb.append("inner join requisicao on requisicao.codigo = entregarequisicao.requisicao ");
        sqlsb.append("inner join usuario on usuario.codigo = requisicao.solicitanteRequisicao ");
        sqlsb.append("inner join pessoa on usuario.pessoa = pessoa.codigo ");
        sqlsb.append("inner join centroresultado on requisicao.centroresultadoadministrativo = centroresultado.codigo ");
        sqlsb.append("inner join unidadeensino on requisicao.unidadeensino = unidadeensino.codigo ");
        sqlsb.append("inner join entregarequisicaoitem on entregarequisicaoitem.entregarequisicao = entregarequisicao.codigo ");
        sqlsb.append("inner join requisicaoitem on entregarequisicaoitem.requisicaoitem = requisicaoitem.codigo ");
        sqlsb.append("inner join produtoservico on produtoservico.codigo = requisicaoitem.produtoservico ");
        sqlsb.append("inner join operacaoestoque on operacaoestoque.tipooperacaoestoqueorigemenum = 'ENTREGA_REQUISICAO_ITEM' and codorigem = entregarequisicaoitem.codigo::varchar ");
        sqlsb.append("inner join estoque on estoque.codigo = operacaoestoque.estoque ");
        sqlsb.append("inner join centroresultado as centroresultadoestoque on entregarequisicaoitem.centroresultadoestoque = centroresultadoestoque.codigo ");
        sqlsb.append("LEFT JOIN departamento ON departamento.codigo = requisicao.departamento ");
        sqlsb.append("INNER JOIN categoriadespesa ON categoriadespesa.codigo = requisicao.categoriadespesa ");
        sqlsb.append("INNER JOIN categoriaproduto ON categoriaproduto.codigo = requisicao.categoriaproduto ");
        sqlsb.append("where 1 = 1");
       // sqlsb.append(" and requisicao.unidadeEnsino = ");
       // sqlsb.append(unidadeEnsino.getCodigo());
        montarFiltrosComunsParaLayout(sqlsb, "RequisicaoRel4", listaUnidadeEnsino, departamentoVO, categoriaDespesa, categoriaProduto, produtoServico, situacaoEntrega, requisicao, listaCentroResultadoEstoque, dataInicioPeriodoConsumo, dataFimPeriodoConsumo);
        sqlsb.append("group by entregarequisicao.data, unidadeensino.nome, produtoservico.nome, centroresultado.descricao, centroresultadoestoque.descricao, centroresultadoestoque.codigo, centroresultado.codigo");
        sqlsb.append(" order by centroresultadoestoque.codigo, centroresultado.codigo, entregarequisicao.data, produtoservico.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlsb.toString());
        return tabelaResultado;
    }
    
    public void montarFiltrosComunsParaLayout(StringBuilder sqlsb, String layout, List<UnidadeEnsinoVO> listaUnidadeEnsino, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesa, CategoriaProdutoVO categoriaProduto, ProdutoServicoVO produtoServico, String situacaoEntrega, RequisicaoVO requisicao, List<CentroResultadoVO> listaCentroResultadoEstoque, Date dataInicio, Date dataFim ) {
    	
    	if (!listaUnidadeEnsino.isEmpty()) {
			boolean virgula = false;
			sqlsb.append(" AND requisicao.unidadeEnsino IN(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (!virgula) {
						sqlsb.append(unidadeEnsinoVO.getCodigo());
					} else {
						sqlsb.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
					virgula = true;
				}
			}
			sqlsb.append(") ");
		}
    	
    	if (!listaCentroResultadoEstoque.isEmpty()) {
			boolean virgula = false;
			sqlsb.append("AND centroresultadoestoque.codigo IN(");
			for (CentroResultadoVO centroResultadoEstoqueVO : listaCentroResultadoEstoque) {
				if (centroResultadoEstoqueVO.getFiltrarCentroResultado()) {
					if (!virgula) {
						sqlsb.append(centroResultadoEstoqueVO.getCodigo());
					} else {
						sqlsb.append(", ").append(centroResultadoEstoqueVO.getCodigo());
					}
					virgula = true;
				}
			}
			sqlsb.append(") ");
		}
		
        if (categoriaDespesa.getCodigo() != 0) {
            sqlsb.append(" and categoriadespesa.codigo = ").append(categoriaDespesa.getCodigo());
        }
        if (categoriaProduto.getCodigo() != 0) {
            sqlsb.append(" and categoriaproduto.codigo = ").append(categoriaProduto.getCodigo());
        }
        if (produtoServico.getCodigo() != 0) {
            sqlsb.append(" and produtoservico.codigo = ").append(produtoServico.getCodigo());
        }
        if (departamentoVO.getCodigo() != 0) {
            sqlsb.append(" and departamento.codigo = ").append(departamentoVO.getCodigo()).append(" ");
        }
       
        if (requisicao.getSolicitanteRequisicao().getCodigo() != 0) {
            sqlsb.append(" and requisicao.solicitanteRequisicao = ").append(requisicao.getSolicitanteRequisicao().getCodigo()).append(" ");
        }
        if (requisicao.getCentroResultadoAdministrativo().getCodigo() != 0) {
            sqlsb.append(" and requisicao.centroresultadoadministrativo = ").append(requisicao.getCentroResultadoAdministrativo().getCodigo()).append(" ");
        }       
    	if (!situacaoEntrega.equals("todas")) {
            sqlsb.append(" and requisicao.situacaoentrega = '").append(situacaoEntrega).append("' ");
        }

        if (Uteis.isAtributoPreenchido(requisicao.getSituacaoAutorizacao())) {
        	sqlsb.append(String.format(" and requisicao.situacaoAutorizacao = '%s'", requisicao.getSituacaoAutorizacao()));
		
	        if(Uteis.isAtributoPreenchido(requisicao.getTipoAutorizacaoRequisicaoEnum()) && !requisicao.getTipoAutorizacaoRequisicaoEnum().equals(TipoAutorizacaoRequisicaoEnum.NENHUM)) {
	        	sqlsb.append(" and requisicaoitem.tipoautorizacaorequisicao = '").append(requisicao.getTipoAutorizacaoRequisicaoEnum()).append("' ");
	        }

			if (requisicao.getAutorizado() && Uteis.isAtributoPreenchido(requisicao.getSituacaoTipoAutorizacaoRequisicaoEnum())) {
				sqlsb.append(" and requisicaoitem.quantidadeautorizada > 0");
				if (requisicao.getSituacaoTipoAutorizacaoRequisicaoEnum().equals("AC")) {//Aguardando Cotacao
					sqlsb.append(" and (requisicaoitem.cotacao is null or requisicaoitem.cotacao = 0) ");
				}else if (requisicao.getSituacaoTipoAutorizacaoRequisicaoEnum().equals("CO")) {//Cotado
					sqlsb.append(" and requisicaoitem.cotacao > 0 ");
				}else if (requisicao.getSituacaoTipoAutorizacaoRequisicaoEnum().equals("AD")) {//Aguardando Compra Direta
					sqlsb.append(" and (requisicaoitem.compraitem is null or requisicaoitem.compraitem = 0) ");
				}else {//Comprado
					sqlsb.append(" and requisicaoitem.compraitem > 0 ");
				}
			}
        	
        }       	
        
		if (layout.equals("RequisicaoRel4")) {
			if (dataInicio != null) {
				sqlsb.append(" and entregarequisicao.data >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
			}
			if (dataFim != null) {
				sqlsb.append(" and entregarequisicao.data <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
			}
		} else {
			if (dataInicio != null) {
				sqlsb.append(" and requisicao.dataRequisicao >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
			}
			if (dataFim != null) {
				sqlsb.append(" and requisicao.dataRequisicao <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
			}
		}
    
    }
    public String designIReportRelatorio(String layout) {
        return (caminhoBaseRelatorio() + layout + ".jrxml");
    }
    
    public String designIReportRelatorioExcel(String layout) {
    	return (caminhoBaseRelatorio() + layout + "Excel.jrxml");       
    }

    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "compras" + File.separator);
    }

    @Override
    public String caminhoBaseQuestionarioRelatorio() {
    	return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return "RequisicaoRel";
    }
    
    public static String getIdEntidadeExcel() {
        return "RequisicaoExcelRel";
    }
}
