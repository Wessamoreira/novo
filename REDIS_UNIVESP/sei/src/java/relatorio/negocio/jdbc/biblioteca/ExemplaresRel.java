/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.TipoEntradaAcervo;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import relatorio.negocio.comuns.biblioteca.ExemplaresRelVO;
import relatorio.negocio.interfaces.biblioteca.ExemplaresRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ExemplaresRel extends SuperRelatorio implements ExemplaresRelInterfaceFacade {

	@Override
	public List<ExemplaresRelVO> criarObjeto(ExemplarVO exemplarVO, Integer unidadeEnsino, String tipoCatalogoPeriodico,
			String tipoLayout, String tipoOrdenacaoRelatorio, Date dataInicioCompraExemplar, Date dataFimCompraExemplar,
			Date dataInicioAquisicaoExemplar, Date dataFimAquisicaoExemplar,  FuncionarioVO responsavelCriacao, FuncionarioVO responsavelAlteracao,
			Date periodoCriacaoInicio, Date peridoCriacaoFim, Date periodoAlteracaoInicio, Date periodoAlteracaoFim,Boolean apresentarResponsavelCriacaoCatalogo ,Boolean apresentarResponsavelAlteracaoCatalogo, Boolean considerarSubTiposCatalogo)
			throws Exception {
		List<ExemplaresRelVO> listaExemplaresRelVO = new ArrayList<ExemplaresRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada(exemplarVO, unidadeEnsino, tipoCatalogoPeriodico, tipoLayout,tipoOrdenacaoRelatorio,dataInicioCompraExemplar,dataFimCompraExemplar , dataInicioAquisicaoExemplar,dataFimAquisicaoExemplar,
				responsavelCriacao,responsavelAlteracao,periodoCriacaoInicio,peridoCriacaoFim,periodoAlteracaoInicio,periodoAlteracaoFim,apresentarResponsavelCriacaoCatalogo,apresentarResponsavelAlteracaoCatalogo, considerarSubTiposCatalogo);
		while (dadosSQL.next()) {
			listaExemplaresRelVO.add(montarDados(dadosSQL, tipoLayout,apresentarResponsavelCriacaoCatalogo,apresentarResponsavelAlteracaoCatalogo));
		}
		return listaExemplaresRelVO;
	}

//	public List<ExemplaresRelVO> criarObjeto(ExemplarVO exemplarVO, Integer unidadeEnsino, String tipoCatalogoPeriodico, String tipoLayout,String tipoOrdenacaoRelatorio,Date dataInicioCompraExemplar,Date dataFimCompraExemplar ,Date dataInicioAquisicaoExemplar,Date dataFimAquisicaoExemplar) throws Exception {
//		List<ExemplaresRelVO> listaExemplaresRelVO = new ArrayList<ExemplaresRelVO>(0);
//		SqlRowSet dadosSQL = executarConsultaParametrizada(exemplarVO, unidadeEnsino, tipoCatalogoPeriodico, tipoLayout,tipoOrdenacaoRelatorio,dataInicioCompraExemplar,dataFimCompraExemplar , dataInicioAquisicaoExemplar,dataFimAquisicaoExemplar);
//		while (dadosSQL.next()) {
//			listaExemplaresRelVO.add(montarDados(dadosSQL, tipoLayout));
//		}
//		return listaExemplaresRelVO;
//	}

	private SqlRowSet executarConsultaParametrizada(ExemplarVO exemplarVO, Integer unidadeEnsino, String tipoCatalogoPeriodico, String tipoLayout,String tipoOrdenacaoRelatorio,Date dataInicioCompraExemplar,Date dataFimCompraExemplar ,Date dataInicioAquisicaoExemplar,Date dataFimAquisicaoExemplar,
			FuncionarioVO responsavelCriacao, FuncionarioVO responsavelAlteracao, Date periodoCriacaoInicio, Date periodoCriacaoFim, Date periodoAlteracaoInicio, Date periodoAlteracaoFim,Boolean apresentarResponsavelCriacaoCatalogo, Boolean apresentarResponsavelAlteracaoCatalogo, Boolean considerarSubTiposCatalogo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT c.codigo as codigoCatalogo, c.titulo AS tituloCatalogo, c.subtitulo AS subtituloCatalogo, c.edicao AS edicaoCatalogo, editora.nome AS editora , c.dataCadastro AS dataCriacao , c.dataUltimaAtualizacao AS dataAtualizacao ,");
		if(apresentarResponsavelCriacaoCatalogo) {
			sql.append("usuariocriacao.nome AS responsavelCriacao , ");
		}
		if(apresentarResponsavelAlteracaoCatalogo) {
			sql.append(" usuarioatualizacao.nome AS responsavelAtualizacao , ");
		}
		sql.append(" COUNT(DISTINCT e.codigo) AS qtdeExemplar, c.anopublicacao AS anoPublicacao, ");
		sql.append(" array_to_string(array(SELECT COALESCE(autor.nome, '') ||' '|| COALESCE(catalogoautor.siglaautoria, '') FROM catalogoautor INNER JOIN autor ON catalogoautor.autor = autor.codigo WHERE catalogoautor.catalogo = c.codigo order by catalogoautor.ordemApresentacao), '; ' ) AS nomeAutor,  ");
		sql.append(" c.classificacaobibliografica, c.cutterpha, c.assunto, c.assinaturaPeriodico, tipoCatalogo.nome AS tipoCatalogo");
		if (tipoLayout.equals("ANALITICO_PERIODICO") || tipoLayout.equals("ANALITICO_CATALOGO")) {
			sql.append(", itemregistrosaidaacervo.tiposaida  , e.tituloexemplar, e.codigobarra, e.anoVolume, e.mes, e.edicao, e.volume, e.situacaoatual, secao.nome, e.codigo AS codigoExemplar, e.numeroExemplar AS numeroExemplar, e.anopublicacao as exemplaranopublicacao ");
		}
		sql.append(" FROM exemplar e ");
		sql.append("  INNER JOIN catalogo c ON e.catalogo      = c.codigo ");
		if(apresentarResponsavelCriacaoCatalogo) {
			sql.append("  INNER JOIN usuario usuariocriacao ON usuariocriacao.codigo  = c.responsavel ");
			
		}
		if(apresentarResponsavelAlteracaoCatalogo) {
			sql.append("  INNER JOIN usuario usuarioatualizacao ON usuarioatualizacao.codigo  = c.responsavelatualizacao ");
		
		}
		
		sql.append("  INNER JOIN biblioteca b ON e.biblioteca  = b.codigo ");
		sql.append("  INNER JOIN unidadeEnsinoBiblioteca ueb ON ueb.biblioteca = b.codigo ");
		sql.append("  LEFT JOIN editora ON editora.codigo      = c.editora ");
		sql.append("  LEFT JOIN catalogoareaconhecimento cac ON cac.catalogo = c.codigo ");
		sql.append("  LEFT JOIN tipocatalogo ON c.tipocatalogo = tipocatalogo.codigo ");
		if (tipoLayout.equals("ANALITICO_PERIODICO") || tipoLayout.equals("ANALITICO_CATALOGO")) {
			sql.append(" LEFT JOIN secao ON secao.codigo = e.secao  ");
			sql.append(" LEFT JOIN itemregistrosaidaacervo ON e.situacaoatual = '"+SituacaoExemplar.INUTILIZADO.getValor()+"' and itemregistrosaidaacervo.exemplar = e.codigo and itemregistrosaidaacervo.codigo = (select irsa.codigo from itemregistrosaidaacervo irsa where irsa.exemplar = e.codigo order by irsa.codigo desc limit 1)");
		}
		sql.append(" WHERE ueb.unidadeensino = ").append(unidadeEnsino);
		sql.append(" AND e.biblioteca = ").append(exemplarVO.getBiblioteca().getCodigo());
		// if
		// (exemplarVO.getCatalogo().getClassificacaoBibliografica().getCodigo()
		// != null &&
		// !exemplarVO.getCatalogo().getClassificacaoBibliografica().getCodigo().equals(0))
		// {
		// sql.append(" and c.classificacaoBibliografica = ").append(exemplarVO.getCatalogo().getClassificacaoBibliografica().getCodigo());
		// }
		if(Uteis.isAtributoPreenchido(responsavelCriacao)) {
			sql.append(" AND c.responsavel  = ").append(responsavelCriacao.getCodigo());
			if(Uteis.isAtributoPreenchido(periodoCriacaoInicio)&&Uteis.isAtributoPreenchido(periodoCriacaoFim)) {
				sql.append(" AND c.dataCadastro BETWEEN '").append(Uteis.getDataJDBCTimestamp(periodoCriacaoInicio)).append("'");
				sql.append(" AND '").append(Uteis.getDataJDBCTimestamp(periodoCriacaoFim)).append("'");
			}
			
		}
        if(Uteis.isAtributoPreenchido(responsavelAlteracao)) {
        	sql.append(" AND c.responsavelatualizacao = " ).append(responsavelAlteracao.getCodigo());
        	if(Uteis.isAtributoPreenchido(periodoAlteracaoInicio) && Uteis.isAtributoPreenchido(periodoAlteracaoFim)) {
        		sql.append(" AND c.dataUltimaAtualizacao BETWEEN '").append(Uteis.getDataJDBCTimestamp(periodoAlteracaoInicio)).append("'");
    			sql.append(" AND '").append(Uteis.getDataJDBCTimestamp(periodoAlteracaoFim)).append("'");
        	}
        	
        }
		if (Uteis.isAtributoPreenchido(dataInicioCompraExemplar) && Uteis.isAtributoPreenchido(dataFimCompraExemplar) && Uteis.isAtributoPreenchido(exemplarVO.getTipoEntrada()) && exemplarVO.getTipoEntrada().equals(TipoEntradaAcervo.COMPRA.getValor())) {
			sql.append(" AND e.datacompra BETWEEN '").append(Uteis.getDataJDBCTimestamp(dataInicioCompraExemplar)).append("'");
			sql.append(" AND '").append(Uteis.getDataJDBCTimestamp(dataFimCompraExemplar)).append("'");
		}
		if (Uteis.isAtributoPreenchido(dataInicioAquisicaoExemplar) && Uteis.isAtributoPreenchido(dataFimAquisicaoExemplar)) {
			sql.append(" AND e.dataAquisicao BETWEEN '").append(Uteis.getDataJDBC(dataInicioAquisicaoExemplar)).append("'");
			sql.append(" AND '").append(Uteis.getDataJDBC(dataFimAquisicaoExemplar)).append("'");
		}
		if (exemplarVO.getSecao() != null && exemplarVO.getSecao().getCodigo() > 0) {
			sql.append(" AND e.secao = ").append(exemplarVO.getSecao().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(exemplarVO.getCatalogo().getTipoCatalogo()) && Uteis.isAtributoPreenchido(exemplarVO.getCatalogo().getTipoCatalogo().getCodigo())) {
			if(!considerarSubTiposCatalogo) {
				sql.append(" AND tipoCatalogo.codigo = ").append(exemplarVO.getCatalogo().getTipoCatalogo().getCodigo());
			}else {
				sql.append(" and (tipoCatalogo.codigo = ").append(exemplarVO.getCatalogo().getTipoCatalogo().getCodigo()).append(" or tipoCatalogo.codigo in ").append("("
						+ " select * from ( with recursive tipocatalogoinferior(codigo) as ("
						+ " select distinct tp.codigo from tipocatalogo tp "
						+ " left join tipocatalogo as tipocatalogoprincipal on tipocatalogoprincipal.codigo = tp.subdivisao "
						+ " where tp.codigo = ").append(exemplarVO.getCatalogo().getTipoCatalogo().getCodigo()).append(" union "
						+ " select tp.codigo from tipocatalogo tp "
						+ " left join tipocatalogo as tipocatalogoprincipal on 	tipocatalogoprincipal.codigo = tp.subdivisao "
						+ " inner join tipocatalogoinferior on tipocatalogoinferior.codigo = tp.subdivisao "
						+ " ) select * from tipocatalogoinferior ) as t where codigo <> ").append(exemplarVO.getCatalogo().getTipoCatalogo().getCodigo()).append("))");
			}
		}
		
		if (exemplarVO.getCatalogo().getCodigo() != null && !exemplarVO.getCatalogo().getCodigo().equals(0)) {
			sql.append(" AND c.codigo = ").append(exemplarVO.getCatalogo().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(exemplarVO.getTipoEntrada())) {
			sql.append(" AND e.tipoentrada = '").append(exemplarVO.getTipoEntrada()).append("'");
		}
		if (exemplarVO.getCatalogo().getAreaConhecimentoVO().getCodigo() != null && !exemplarVO.getCatalogo().getAreaConhecimentoVO().getCodigo().equals(0)) {
			sql.append(" AND cac.areaconhecimento = ").append(exemplarVO.getCatalogo().getAreaConhecimentoVO().getCodigo());
		}
		if (tipoCatalogoPeriodico.equals("CATALOGO")) {
			sql.append(" AND c.assinaturaPeriodico = false ");
		}
		if (tipoCatalogoPeriodico.equals("PERIODICO")) {
			sql.append(" AND c.assinaturaPeriodico = true ");
		}
		if (!exemplarVO.getCatalogo().getAssunto().equals("") && tipoLayout.equals("SINTETICO_ASSUNTO")) {
			sql.append(" AND lower (sem_acentos(c.assunto)) LIKE (sem_acentos('%" + exemplarVO.getCatalogo().getAssunto().toLowerCase() + "%'))");
		}
		if (!exemplarVO.getCatalogo().getTitulo().equals("")) {
			sql.append(" AND lower (sem_acentos(c.titulo)) LIKE (sem_acentos('%" + exemplarVO.getCatalogo().getTitulo().toLowerCase() + "%'))");
		}
		if (!exemplarVO.getTituloExemplar().equals("")) {
			sql.append(" AND lower (sem_acentos(e.tituloexemplar)) LIKE (sem_acentos('%" + exemplarVO.getTituloExemplar().toLowerCase() + "%'))");
		}
		if (!exemplarVO.getCatalogo().getClassificacaoInicial().equals("")) {
			sql.append(" AND lower(trim(c.classificacaobibliografica)) >= '" + exemplarVO.getCatalogo().getClassificacaoInicial().toLowerCase().trim() + "'");
		}
		if (!exemplarVO.getCatalogo().getClassificacaoFinal().equals("")) {
			sql.append(" AND lower(trim(c.classificacaobibliografica)) <= '" + exemplarVO.getCatalogo().getClassificacaoFinal().toLowerCase().trim() + "'");
		}
		if (tipoLayout.equals("ANALITICO_PERIODICO") || tipoLayout.equals("ANALITICO_CATALOGO")) {
			sql.append(" GROUP BY c.edicao, c.titulo, c.subtitulo ,  editora.nome, c.anopublicacao, c.codigo, e.tituloexemplar, e.codigobarra,  ");
			sql.append(" e.anoVolume, e.mes, secao.nome, e.edicao, e.volume, e.situacaoatual, secao.nome, e.codigo, c.assinaturaPeriodico, tipoCatalogo.nome, itemregistrosaidaacervo.tiposaida ");
			if(apresentarResponsavelCriacaoCatalogo) {
				sql.append(", usuariocriacao.nome ");	
			}
			if(apresentarResponsavelAlteracaoCatalogo) {
				sql.append(",  usuarioatualizacao.nome ");	
			}
			
		} else {
			sql.append(" GROUP BY c.edicao, c.titulo, c.subtitulo ,  editora.nome, c.anopublicacao, c.codigo, c.assinaturaPeriodico, tipoCatalogo.nome  ");
			if(apresentarResponsavelCriacaoCatalogo) {
				sql.append(", usuariocriacao.nome ");	
			}
			if(apresentarResponsavelAlteracaoCatalogo) {
				sql.append(",  usuarioatualizacao.nome ");	
			}
		}

		if (tipoOrdenacaoRelatorio.equals("TITULO")) {
			sql.append(" ORDER BY c.titulo, c.classificacaobibliografica, c.cutterpha");
		} 
		if (tipoOrdenacaoRelatorio.equals("SUBTITULO")) {
			sql.append(" ORDER BY c.subtitulo, c.classificacaobibliografica, c.cutterpha");
		} 
		if (tipoOrdenacaoRelatorio.equals("EDICAO_TITULO")) {
			sql.append(" ORDER BY c.edicao,c.titulo, c.classificacaobibliografica, c.cutterpha ");
		} 
 
		if(tipoOrdenacaoRelatorio.equals("ANOPUBLICACAO_TITULO")){
			sql.append(" ORDER BY c.anopublicacao,c.titulo, c.classificacaobibliografica, c.cutterpha ");
		}
		if(tipoOrdenacaoRelatorio.equals("ASSUNTO_CLASSIFICACAO")){
			sql.append(" ORDER BY c.assunto,c.classificacaobibliografica, c.cutterpha");
		}
		if(tipoOrdenacaoRelatorio.equals("TITULO_CLASSIFICACAO")){
			sql.append(" ORDER BY c.titulo,c.classificacaobibliografica, c.cutterpha");
		}
		if(tipoOrdenacaoRelatorio.equals("CLASSIFICACAO_ASSUNTO")){
			sql.append(" ORDER BY  c.classificacaobibliografica, c.cutterpha, c.assunto");
		}
		if(tipoOrdenacaoRelatorio.equals("CUTTERPHA_ASSUNTO")){
			sql.append(" ORDER BY   c.CUTTERPHA, c.classificacaobibliografica, c.assunto");
		}
		if(tipoOrdenacaoRelatorio.equals("AUTORES_ASSUNTO")){
			sql.append(" ORDER BY   nomeAutor,c.assunto, c.classificacaobibliografica, c.cutterpha");
		}
		if(tipoOrdenacaoRelatorio.equals("EDITORA_ASSUNTO")){
			sql.append(" ORDER BY   editora.nome,c.assunto, c.classificacaobibliografica, c.cutterpha");
		}
		if(tipoOrdenacaoRelatorio.equals("CLASSIFICACAO_TITULO")){
			sql.append(" ORDER BY   c.classificacaobibliografica, c.cutterpha,c.titulo");
		}
		if(tipoOrdenacaoRelatorio.equals("CUTTERPHA_TITULO")){
			sql.append(" ORDER BY   c.CUTTERPHA, c.classificacaobibliografica, c.titulo");
		}
		if(tipoOrdenacaoRelatorio.equals("EDITORA_TITULO")){
			sql.append(" ORDER BY   editora.nome,c.titulo, c.classificacaobibliografica, c.cutterpha");
		}
		if(tipoOrdenacaoRelatorio.equals("AUTORES_TITULO")){
			sql.append(" ORDER BY   nomeAutor,c.titulo, c.classificacaobibliografica, c.cutterpha");
		}
		if(tipoOrdenacaoRelatorio.equals("TITULO_CLASSIFICACAO_TOMBO")){
			sql.append(" ORDER BY   c.titulo, c.classificacaobibliografica, c.cutterpha ");
		}
		if(tipoOrdenacaoRelatorio.equals("CLASSIFICACAO_TITULO_TOMBO")){
			sql.append(" ORDER BY   c.classificacaobibliografica, c.cutterpha, c.titulo ");
		}
		if(tipoOrdenacaoRelatorio.equals("SECAO_TITULO")){
			sql.append(" ORDER BY   secao.nome, c.titulo, c.classificacaobibliografica, c.cutterpha ");
		}
		if(tipoOrdenacaoRelatorio.equals("TOMBO_EDITORA_TITULO")){
			sql.append(" ORDER BY    e.codigobarra::NUMERIC(20,0) ");
		}
		if (tipoLayout.equals("ANALITICO_PERIODICO") || tipoLayout.equals("ANALITICO_CATALOGO")) {
			sql.append(", e.codigobarra::NUMERIC(20,0) ");
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return tabelaResultado;
	}

	private ExemplaresRelVO montarDados(SqlRowSet dadosSQL, String tipoLayout,
			Boolean apresentarResponsavelCriacaoCatalogo, Boolean apresentarResponsavelAlteracaoCatalogo) {
		ExemplaresRelVO exemplaresRelVO = new ExemplaresRelVO(); 
		exemplaresRelVO.setCodigoCatalogo(dadosSQL.getInt("codigoCatalogo"));
		exemplaresRelVO.setTituloCatalogo(dadosSQL.getString("tituloCatalogo"));
		exemplaresRelVO.setSubTituloCatalogo(dadosSQL.getString("subtituloCatalogo"));
		if(apresentarResponsavelCriacaoCatalogo) {
			exemplaresRelVO.setNomeResponsavelCriacao(dadosSQL.getString("responsavelCriacao"));
		}		
		if(apresentarResponsavelAlteracaoCatalogo) {
			exemplaresRelVO.setNomeResponsavelAtualizacao(dadosSQL.getString("responsavelAtualizacao"));
		}		
	
		exemplaresRelVO.setDataCriacaoCatalogo(Uteis.getDataBD(dadosSQL.getDate("dataCriacao"),"dd/MM/yyyy"));
		exemplaresRelVO.setDataAtualizacaoCatalogo(Uteis.getDataBD(dadosSQL.getDate("dataAtualizacao"),"dd/MM/yyyy"));
		
		if((exemplaresRelVO.getSubTituloCatalogo() != null && exemplaresRelVO.getSubTituloCatalogo().trim().equals("")||(exemplaresRelVO.getSubTituloCatalogo() == null))){
			exemplaresRelVO.setSubTituloCatalogo("-");
		}
		exemplaresRelVO.setEditora(dadosSQL.getString("editora"));
		exemplaresRelVO.setQtdeExemplar(dadosSQL.getInt("qtdeExemplar"));
		
		exemplaresRelVO.setNomeAutor(dadosSQL.getString("nomeAutor"));
		exemplaresRelVO.setClassificacao(dadosSQL.getString("classificacaobibliografica"));
		if((exemplaresRelVO.getClassificacao() != null && exemplaresRelVO.getClassificacao().trim().equals("")||(exemplaresRelVO.getClassificacao() == null))){
			exemplaresRelVO.setClassificacao("-");
		}
		exemplaresRelVO.setCutterpha(dadosSQL.getString("cutterpha"));
		if((exemplaresRelVO.getCutterpha() != null && exemplaresRelVO.getCutterpha().trim().equals("")||(exemplaresRelVO.getCutterpha() == null))){
			exemplaresRelVO.setCutterpha("-");
		}
		exemplaresRelVO.setAssunto(dadosSQL.getString("assunto"));
		if((exemplaresRelVO.getAssunto() != null && exemplaresRelVO.getAssunto().trim().equals("")||(exemplaresRelVO.getAssunto() == null))){
			exemplaresRelVO.setAssunto("-");
		}
		if (tipoLayout.equals("ANALITICO_PERIODICO") || tipoLayout.equals("ANALITICO_CATALOGO")) {
			exemplaresRelVO.setTituloExemplar(dadosSQL.getString("tituloexemplar"));	
			if((exemplaresRelVO.getTituloExemplar() != null && exemplaresRelVO.getTituloExemplar().trim().equals("")||(exemplaresRelVO.getTituloExemplar() == null))){
				exemplaresRelVO.setTituloExemplar("-");
			}
			exemplaresRelVO.setCodigoBarra(dadosSQL.getString("codigobarra"));
			exemplaresRelVO.setAnoVolume(dadosSQL.getString("anoVolume"));
			if((exemplaresRelVO.getAnoVolume() != null && exemplaresRelVO.getAnoVolume().trim().equals("")||(exemplaresRelVO.getAnoVolume() == null))){
				exemplaresRelVO.setAnoVolume("-");
			}
			exemplaresRelVO.setMes(dadosSQL.getString("mes"));
			if((exemplaresRelVO.getMes() != null && exemplaresRelVO.getMes().trim().equals("")||(exemplaresRelVO.getMes() == null))){
				exemplaresRelVO.setMes("-");
			}
			exemplaresRelVO.setSecao(dadosSQL.getString("nome"));
			if((exemplaresRelVO.getSecao() != null && exemplaresRelVO.getSecao().trim().equals("")||(exemplaresRelVO.getSecao() == null))){
				exemplaresRelVO.setSecao("-");
			}
			exemplaresRelVO.setVolume(dadosSQL.getString("volume"));
			exemplaresRelVO.setSituacao(dadosSQL.getString("situacaoatual"));
			exemplaresRelVO.setCodigoExemplar(dadosSQL.getString("codigoExemplar"));
			exemplaresRelVO.setAssinaturaPeriodico(dadosSQL.getBoolean("assinaturaPeriodico"));
			exemplaresRelVO.setNumeroExemplar(dadosSQL.getInt("numeroExemplar"));
			exemplaresRelVO.setTipoSaida(dadosSQL.getString("tiposaida"));
			String edicao = dadosSQL.getString("edicao");
			exemplaresRelVO.setEdicaoCatalogo(Uteis.isAtributoPreenchido(edicao) ? edicao : dadosSQL.getString("edicaoCatalogo"));
			String anoPublicacao = dadosSQL.getString("exemplaranopublicacao");
			exemplaresRelVO.setAnoPublicacao(Uteis.isAtributoPreenchido(anoPublicacao) ? anoPublicacao : dadosSQL.getString("anoPublicacao"));
		} else {
			exemplaresRelVO.setEdicaoCatalogo(dadosSQL.getString("edicaoCatalogo"));
			exemplaresRelVO.setAnoPublicacao(dadosSQL.getString("anoPublicacao"));
		}
		exemplaresRelVO.setTipoCatalogo(dadosSQL.getString("tipoCatalogo"));
		return exemplaresRelVO;
	}

	@Override
	public void validarDados(ExemplarVO exemplarVO, Integer unidadeEnsino, String layoutRelatorio,
			Date dataInicioCompraExemplar, Date dataFimCompraExemplar, Date dataInicioAquisicaoExemplar,
			Date dataFimAquisicaoExemplar, Boolean apresentarResponsavelEdataCadastro, String responsavel,
			Date dataCadastroInicio, Date dataCadastroFim, Boolean apresentarUltimoResponsavelEdataCadastro,
			String responsavelUltimaAlteracao, Date dataAlteracaoCadastroInicio, Date dataAlteracaoCadastroFim)
			throws Exception {		
		if (unidadeEnsino.equals(0)) {
			throw new ConsistirException("A Unidade De Ensino deve ser informada para a geração do relatório.");
		}
		if (exemplarVO.getBiblioteca().getCodigo() == null || exemplarVO.getBiblioteca().getCodigo().equals(0)) {
			throw new ConsistirException("O campo BIBLIOTECA deve ser informado para a geração do relatório.");
		}
		if (layoutRelatorio.equals("")) {
			throw new ConsistirException("É necessário informar o layout do relatório");
		}
		if (Uteis.isAtributoPreenchido(dataInicioCompraExemplar) && !Uteis.isAtributoPreenchido(dataFimCompraExemplar)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ExemplaresRel_dataFimCompraDeveSerInformada"));
		}
		if (!Uteis.isAtributoPreenchido(dataInicioCompraExemplar) && Uteis.isAtributoPreenchido(dataFimCompraExemplar)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ExemplaresRel_dataInicioCompraDeveSerInformada"));
		}
		
		if (Uteis.isAtributoPreenchido(dataInicioAquisicaoExemplar) && !Uteis.isAtributoPreenchido(dataFimAquisicaoExemplar)) {
			throw new Exception(UteisJSF.internacionalizar("E necessário informar a data Final da Aquisição para consulta."));
		}
		if (!Uteis.isAtributoPreenchido(dataInicioAquisicaoExemplar) && Uteis.isAtributoPreenchido(dataFimAquisicaoExemplar)) {
			throw new Exception(UteisJSF.internacionalizar("E necessário informar a data Inicial da Aquisição para consulta."));
		}
		if(apresentarResponsavelEdataCadastro) {
	  
		   if(!Uteis.isAtributoPreenchido(dataCadastroInicio) && Uteis.isAtributoPreenchido(dataCadastroFim) ) {
			 throw new Exception(UteisJSF.internacionalizar("E necessário informar a data Inicial do Periodo Cadastro para consulta."));
		  }
		   if(Uteis.isAtributoPreenchido(dataCadastroInicio) && !Uteis.isAtributoPreenchido(dataCadastroFim)) {
			throw new Exception(UteisJSF.internacionalizar("E necessário informar a data Final do Periodo Cadastro para consulta."));
			 }
		   if(Uteis.isAtributoPreenchido(dataCadastroInicio) && Uteis.isAtributoPreenchido(dataCadastroFim)) {
				if (!Uteis.isAtributoPreenchido(responsavel)) {
					throw new Exception(UteisJSF.internacionalizar("E necessário informar o Responsavel do Cadastro para consulta."));
				}

			}
		}
		if(apresentarUltimoResponsavelEdataCadastro) {
			
			   if(!Uteis.isAtributoPreenchido(dataAlteracaoCadastroInicio) && Uteis.isAtributoPreenchido(dataAlteracaoCadastroFim)) {
				throw new Exception(UteisJSF.internacionalizar("E necessário informar a data Inicial do Periodo da Ultima Alteração Cadastro para consulta."));
			  }
			   if(Uteis.isAtributoPreenchido(dataAlteracaoCadastroInicio) && !Uteis.isAtributoPreenchido(dataAlteracaoCadastroFim) ) {
					throw new Exception(UteisJSF.internacionalizar("E necessário informar a data Final do Periodo da Ultima Alteração Cadastro para consulta."));
			  }
			   if(Uteis.isAtributoPreenchido(dataAlteracaoCadastroInicio) && Uteis.isAtributoPreenchido(dataAlteracaoCadastroFim) ) {
				if (!Uteis.isAtributoPreenchido(responsavelUltimaAlteracao)) {
					throw new Exception(UteisJSF.internacionalizar(
							"E necessário informar o Responsavel da Ultima Alteração do Cadastro para consulta."));
				}
			  }
			}
	}

	public String designIReportRelatorioExemplaresRelSinteticoPorAssunto() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + "ExemplaresRelSinteticoPorAssunto" + ".jrxml");
	}
	
	public String designIReportRelatorioExemplaresRelAnaliticoCatalogo() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + "ExemplaresRelAnaliticoCatalogo" + ".jrxml");
	}	

	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + getIdEntidade() + ".jrxml");
	}

	public String designIReportRelatorioExemplaresRelAnaliticoPeriodico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + "ExemplaresRelAnaliticoPeriodico" + ".jrxml");
	}

	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator);
	}

	public static String getIdEntidade() {
		return "ExemplaresRel";
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo,String situacao) throws Exception {
		dataModelo.getListaFiltros().clear();
		dataModelo.setListaConsulta(consultarResponsavel(dataModelo,situacao));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalResponsavelCatalogo(dataModelo));
	}

	private Integer consultarTotalResponsavelCatalogo(DataModelo dataModelo) {
		    StringBuilder sql = new StringBuilder(getSqlBasicoCount());
		    SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
	        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
		
	}

	private List<FuncionarioVO> consultarResponsavel(DataModelo dataModelo,String responsavel) throws Exception {
		StringBuilder sql = new StringBuilder();
		dataModelo.setLimitePorPagina(5);
		sql.append(getSqlBasicoFuncionario());
		if(responsavel.equals("USUARIOCRIACAO")) {
		sql.append(" inner join usuario u on u.codigo = c.responsavel ");
		}else {
			sql.append(" inner join usuario u on u.codigo = c.responsavelatualizacao ");
		}
		
		sql.append(" WHERE 1=1");
		if (Uteis.isAtributoPreenchido(dataModelo.getCampoConsulta()) &&  dataModelo.getCampoConsulta().equals("nome")) {
			sql.append(" AND lower (sem_acentos(u.nome)) LIKE (sem_acentos('%" +dataModelo.getValorConsulta() + "%'))");
		}
		if (Uteis.isAtributoPreenchido(dataModelo.getCampoConsulta()) &&  dataModelo.getCampoConsulta().equals("codigo")) {
			if(Uteis.isAtributoPreenchido(dataModelo.getValorConsulta())) {
			 sql.append(" AND u.codigo = ").append(dataModelo.getValorConsulta());
			}
		}
		
		
		sql.append(" group by  u.nome , u.codigo order by u.nome desc ");		
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosLista(tabelaResultado);
	}
	
	private String getSqlBasicoFuncionario() {
		StringBuilder sql = new StringBuilder();
		sql.append("select u.nome as nome , u.codigo  as codigo from catalogo c ");		
		
		return sql.toString();
	}
	private List<FuncionarioVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<FuncionarioVO> funcionario = new ArrayList<>();
        while(tabelaResultado.next()) {
        	funcionario.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return funcionario;
	}
	
	@Override
	public FuncionarioVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
	  FuncionarioVO fun = new FuncionarioVO();
	    fun.setCodigo(tabelaResultado.getInt("codigo"));      
        fun.getPessoa().setNome(tabelaResultado.getString("nome")); 

		return fun;
	}
	private String getSqlBasicoCount() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT (*) AS qtde  FROM ( SELECT COUNT(usuario.codigo)  FROM catalogo c");
		sql.append(" inner join usuario on usuario.codigo = c.responsavel");		
		sql.append(" group by usuario.codigo ) AS qtde");

		return sql.toString();
	}
	
}
