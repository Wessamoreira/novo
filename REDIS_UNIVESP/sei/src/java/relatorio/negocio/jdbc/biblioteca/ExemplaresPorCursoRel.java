package relatorio.negocio.jdbc.biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoEntradaAcervo;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import relatorio.negocio.comuns.biblioteca.ExemplaresRelVO;
import relatorio.negocio.interfaces.biblioteca.ExemplaresPorCursoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
public class ExemplaresPorCursoRel extends SuperRelatorio implements ExemplaresPorCursoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	public List<ExemplaresRelVO> criarObjeto(BibliotecaVO biblioteca, CursoVO curso, DisciplinaVO disciplina, Date dataInicioCompraExemplar, Date dataFimCompraExemplar, TipoCatalogoVO tipoCatalogo, String tipoCatalogoPeriodico , Date dataInicioAquisicaoExemplar, Date dataFimAquisicaoExemplar, Boolean considerarSubTiposCatalogo, Boolean considerarPlanoEnsinoCursoVinculadoAoCatalogo) throws Exception {
		List<ExemplaresRelVO> listaExemplaresRelVO = new ArrayList<>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada(biblioteca.getCodigo(), curso.getCodigo(), disciplina.getCodigo(),dataInicioCompraExemplar,dataFimCompraExemplar, tipoCatalogo, tipoCatalogoPeriodico ,dataInicioAquisicaoExemplar,dataFimAquisicaoExemplar, considerarSubTiposCatalogo, considerarPlanoEnsinoCursoVinculadoAoCatalogo);
		while (dadosSQL.next()) {
			listaExemplaresRelVO.add(montarDados(dadosSQL));
		}
		return listaExemplaresRelVO;
	}

	private SqlRowSet executarConsultaParametrizada(Integer biblioteca, Integer codigoCurso, Integer codigoDisciplina,Date dataInicioCompraExemplar,Date dataFimCompraExemplar, TipoCatalogoVO tipoCatalogo, String tipoCatalogoPeriodico , Date dataInicioAquisicaoExemplar ,Date dataFimAquisicaoExemplar, Boolean considerarSubTiposCatalogo, Boolean considerarPlanoEnsinoCursoVinculadoAoCatalogo) throws Exception {
		StringBuilder sql = new StringBuilder();
				sql.append(" select *, ");
		sql.append(" (select count(ex.codigo) from exemplar ex inner join catalogo cat on cat.codigo = ex.catalogo left join editora edi on edi.codigo = cat.editora  ");
		sql.append(" where ex.catalogo = T.codigo  and ex.biblioteca = t.biblioteca  ");
		sql.append(" and case "); 
		sql.append("	when T.anopublicacao is not null and T.anopublicacao != '' then T.anopublicacao = (case when ex.anopublicacao is not null and ex.anopublicacao <> '' then ex.anopublicacao else cat.anopublicacao end) "); 
		sql.append("	else true ");
		sql.append("end ");
		sql.append(" and case "); 
		sql.append("	 when t.local is not null and t.local != '' then t.local = (case when ex.local is not null and ex.local != '' then ex.local end) "); 
		sql.append("	else true "); 
		sql.append("end ");
		sql.append(" and case"); 
		sql.append("	when t.edicaoCatalogo is not null and t.edicaoCatalogo != '' then t.edicaoCatalogo = (case when ex.edicao <> '' then ex.edicao else cat.edicao end) "); 
		sql.append("	else true "); 
		sql.append("end	");
		sql.append(" and case ");
		sql.append("		when t.subtituloCatalogo is not null and t.subtituloCatalogo != '' then t.subtituloCatalogo = (case when ex.subtitulo is not null and ex.subtitulo <> '' then ex.subtitulo else cat.subtitulo end) ");
		sql.append("		else true "); 
		sql.append("	 end ");
		if (Uteis.isAtributoPreenchido(dataInicioCompraExemplar) && Uteis.isAtributoPreenchido(dataFimCompraExemplar)) {
			sql.append(" AND ex.datacompra BETWEEN '").append(Uteis.getDataJDBCTimestamp(dataInicioCompraExemplar)).append("'");
			sql.append(" AND '").append(Uteis.getDataJDBCTimestamp(dataFimCompraExemplar)).append("'");
			sql.append(" AND ex.tipoentrada = '").append(TipoEntradaAcervo.COMPRA.getValor()).append("'");
		}
		if (Uteis.isAtributoPreenchido(dataInicioAquisicaoExemplar) && Uteis.isAtributoPreenchido(dataFimAquisicaoExemplar)) {
			sql.append(" AND ex.dataAquisicao BETWEEN '").append(Uteis.getDataJDBC(dataInicioAquisicaoExemplar)).append("'");
			sql.append(" AND '").append(Uteis.getDataJDBC(dataFimAquisicaoExemplar)).append("'");
			
		}
		if (Uteis.isAtributoPreenchido(biblioteca)) {
			sql.append(" and ex.biblioteca = ").append(biblioteca);
		}
		sql.append("  ) as qtdeExemplar from (  ");
		if(considerarPlanoEnsinoCursoVinculadoAoCatalogo) {
		sql.append("SELECT distinct exemplar.biblioteca, catalogo.titulo as tituloCatalogo, editora.nome as nomeEditora, CASE WHEN exemplar.anopublicacao is not null and exemplar.anopublicacao <> '' THEN exemplar.anopublicacao ELSE catalogo.anopublicacao END AS anopublicacao, array_to_string( array(select autor.nome from catalogoautor inner join autor on autor.codigo = catalogoautor.autor where catalogoautor.catalogo = catalogo.codigo), '\n') as nomeAutor, ");
//		sql.append("(select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo) as qtdeExemplar, catalogo.edicao as edicaoCatalogo, exemplar.local ");
		sql.append(" case when exemplar.edicao is not null and exemplar.edicao <> '' then exemplar.edicao else catalogo.edicao end as edicaoCatalogo, exemplar.local, catalogo.codigo, ");
		sql.append(" case when exemplar.subtitulo is not null and exemplar.subtitulo <> '' then exemplar.subtitulo else catalogo.subtitulo end as subtituloCatalogo ");
		sql.append(" from exemplar ");
		sql.append(" inner join catalogo on exemplar.catalogo = catalogo.codigo ");
		sql.append(" left join editora on editora.codigo = catalogo.editora ");
		sql.append(" left join catalogoautor on catalogoautor.catalogo = catalogo.codigo ");
		sql.append(" left join autor on catalogoautor.autor = autor.codigo ");
		sql.append(" left join referenciabibliografica on referenciabibliografica.catalogo = catalogo.codigo ");
		sql.append(" left join planoensino on referenciabibliografica.planoensino = planoensino.codigo ");
		sql.append(" left join disciplina on referenciabibliografica.disciplina = disciplina.codigo ");
		sql.append(" left join gradedisciplina on gradedisciplina.disciplina = disciplina.codigo ");
		sql.append(" left join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
		sql.append(" left join gradecurricular on gradecurricular.codigo = periodoletivo.gradecurricular and planoensino.curso =  gradecurricular.curso");
		sql.append(" where (gradecurricular.curso = ").append(codigoCurso).append(") ");
		if (Uteis.isAtributoPreenchido(biblioteca)) {
			sql.append(" and exemplar.biblioteca = ").append(biblioteca);
		}
		if (Uteis.isAtributoPreenchido(codigoDisciplina)) {
			sql.append(" and disciplina.codigo = ").append(codigoDisciplina);
		}
		if (Uteis.isAtributoPreenchido(tipoCatalogo.getCodigo())) {
			if(!considerarSubTiposCatalogo) {
				sql.append(" and catalogo.tipoCatalogo = ").append(tipoCatalogo.getCodigo());
			}else {
				sql.append(" and (catalogo.tipoCatalogo = ").append(tipoCatalogo.getCodigo()).append(" or catalogo.tipoCatalogo in ").append("("
						+ " select * from ( with recursive tipocatalogoinferior(codigo) as ("
						+ " select distinct tp.codigo from tipocatalogo tp "
						+ " left join tipocatalogo as tipocatalogoprincipal on tipocatalogoprincipal.codigo = tp.subdivisao "
						+ " where tp.codigo = ").append(tipoCatalogo.getCodigo()).append(" union "
						+ " select tp.codigo from tipocatalogo tp "
						+ " left join tipocatalogo as tipocatalogoprincipal on 	tipocatalogoprincipal.codigo = tp.subdivisao "
						+ " inner join tipocatalogoinferior on tipocatalogoinferior.codigo = tp.subdivisao "
						+ " ) select * from tipocatalogoinferior ) as t where codigo <> ").append(tipoCatalogo.getCodigo()).append("))");
			}
		}
		if (Uteis.isAtributoPreenchido(tipoCatalogoPeriodico)) {
			if (tipoCatalogoPeriodico.equals("PERIODICO")) {
				sql.append(" and catalogo.assinaturaperiodico = 'true' ");
			} else if (tipoCatalogoPeriodico.equals("CATALOGO")){
				sql.append(" and catalogo.assinaturaperiodico = 'false' ");
			}
		}
		if (Uteis.isAtributoPreenchido(dataInicioCompraExemplar) && Uteis.isAtributoPreenchido(dataFimCompraExemplar)) {
			sql.append(" AND exemplar.datacompra BETWEEN '").append(Uteis.getDataJDBCTimestamp(dataInicioCompraExemplar)).append("'");
			sql.append(" AND '").append(Uteis.getDataJDBCTimestamp(dataFimCompraExemplar)).append("'");
			sql.append(" AND exemplar.tipoentrada = '").append(TipoEntradaAcervo.COMPRA.getValor()).append("'");
		}
		
		if (Uteis.isAtributoPreenchido(dataInicioAquisicaoExemplar) && Uteis.isAtributoPreenchido(dataFimAquisicaoExemplar)) {
			sql.append(" AND exemplar.dataAquisicao BETWEEN '").append(Uteis.getDataJDBC(dataInicioAquisicaoExemplar)).append("'");
			sql.append(" AND '").append(Uteis.getDataJDBC(dataFimAquisicaoExemplar)).append("'");
			
		}
		
		sql.append(" group by exemplar.biblioteca, edicaocatalogo, tituloCatalogo, subtituloCatalogo, nomeEditora, catalogo.anoPublicacao, nomeAutor, catalogo.codigo, exemplar.local, CASE WHEN exemplar.anopublicacao is not null and exemplar.anopublicacao <> '' THEN exemplar.anopublicacao ELSE catalogo.anopublicacao END ");
		sql.append(" union ");
		}
		sql.append(" SELECT distinct exemplar.biblioteca, catalogo.titulo as tituloCatalogo, editora.nome as nomeEditora, CASE WHEN exemplar.anopublicacao is not null and exemplar.anopublicacao <> '' THEN exemplar.anopublicacao ELSE catalogo.anopublicacao END AS anopublicacao, array_to_string( array(select autor.nome||' '||catalogoautor.siglaautoria from catalogoautor inner join autor on autor.codigo = catalogoautor.autor where catalogoautor.catalogo = catalogo.codigo order by catalogoautor.ordemApresentacao), '\n') as nomeAutor, ");
//		sql.append("(select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo) as qtdeExemplar, catalogo.edicao as edicaoCatalogo, exemplar.local ");
		sql.append(" case when exemplar.edicao is not null and exemplar.edicao <> '' then exemplar.edicao else catalogo.edicao end as edicaoCatalogo, exemplar.local, catalogo.codigo, ");
		sql.append(" case when exemplar.subtitulo is not null and exemplar.subtitulo <> '' then exemplar.subtitulo else catalogo.subtitulo end as subtituloCatalogo ");
		sql.append(" from exemplar ");
		sql.append(" left join catalogo on exemplar.catalogo = catalogo.codigo ");
		sql.append(" left join editora on editora.codigo = catalogo.editora ");
		sql.append(" left join catalogoautor on catalogoautor.catalogo = catalogo.codigo ");
		sql.append(" left join autor on catalogoautor.autor = autor.codigo ");
		sql.append(" left join referenciabibliografica on referenciabibliografica.catalogo = catalogo.codigo ");
		sql.append(" left join disciplina on referenciabibliografica.disciplina = disciplina.codigo ");
		sql.append(" left join catalogocurso on catalogocurso.catalogo = catalogo.codigo ");
		sql.append(" left join curso on curso.codigo = catalogocurso.curso ");
		sql.append(" where (curso.codigo = ").append(codigoCurso).append(") ");
		if (Uteis.isAtributoPreenchido(biblioteca)) {
			sql.append(" and exemplar.biblioteca = ").append(biblioteca);
		}
		if (Uteis.isAtributoPreenchido(codigoDisciplina)) {
			sql.append(" and disciplina.codigo = ").append(codigoDisciplina);
		}
		if (Uteis.isAtributoPreenchido(tipoCatalogo.getCodigo())) {
			
			if(!considerarSubTiposCatalogo) {
				sql.append(" and catalogo.tipoCatalogo = ").append(tipoCatalogo.getCodigo());
			}else {
				sql.append(" and (catalogo.tipoCatalogo = ").append(tipoCatalogo.getCodigo()).append(" or catalogo.tipoCatalogo in ").append("("
						+ " select * from ( with recursive tipocatalogoinferior(codigo) as ("
						+ " select distinct tp.codigo from tipocatalogo tp "
						+ " left join tipocatalogo as tipocatalogoprincipal on tipocatalogoprincipal.codigo = tp.subdivisao "
						+ " where tp.codigo = ").append(tipoCatalogo.getCodigo()).append(" union "
						+ " select tp.codigo from tipocatalogo tp "
						+ " left join tipocatalogo as tipocatalogoprincipal on 	tipocatalogoprincipal.codigo = tp.subdivisao "
						+ " inner join tipocatalogoinferior on tipocatalogoinferior.codigo = tp.subdivisao "
						+ " ) select * from tipocatalogoinferior ) as t where codigo <> ").append(tipoCatalogo.getCodigo()).append("))");
			}
		}
		if (Uteis.isAtributoPreenchido(tipoCatalogoPeriodico)) {
			if (tipoCatalogoPeriodico.equals("PERIODICO")) {
				sql.append(" and catalogo.assinaturaperiodico = 'true' ");
			} else if (tipoCatalogoPeriodico.equals("CATALOGO")){
				sql.append(" and catalogo.assinaturaperiodico = 'false' ");
			}
		}
		if (Uteis.isAtributoPreenchido(dataInicioCompraExemplar) && Uteis.isAtributoPreenchido(dataFimCompraExemplar)) {
			sql.append(" AND exemplar.datacompra BETWEEN '").append(Uteis.getDataJDBCTimestamp(dataInicioCompraExemplar)).append("'");
			sql.append(" AND '").append(Uteis.getDataJDBCTimestamp(dataFimCompraExemplar)).append("'");
			sql.append(" AND exemplar.tipoentrada = '").append(TipoEntradaAcervo.COMPRA.getValor()).append("'");
		}
		
		if (Uteis.isAtributoPreenchido(dataInicioAquisicaoExemplar) && Uteis.isAtributoPreenchido(dataFimAquisicaoExemplar)) {
			sql.append(" AND exemplar.dataAquisicao BETWEEN '").append(Uteis.getDataJDBC(dataInicioAquisicaoExemplar)).append("'");
			sql.append(" AND '").append(Uteis.getDataJDBC(dataFimAquisicaoExemplar)).append("'");
			
		}		

		sql.append(" group by exemplar.biblioteca, edicaocatalogo, tituloCatalogo, subtituloCatalogo, nomeEditora, catalogo.anoPublicacao, nomeAutor, catalogo.codigo, exemplar.local, CASE WHEN exemplar.anopublicacao is not null and exemplar.anopublicacao <> '' THEN exemplar.anopublicacao ELSE catalogo.anopublicacao END ");
		sql.append("order by tituloCatalogo ) as t ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	private ExemplaresRelVO montarDados(SqlRowSet dadosSQL) {
		ExemplaresRelVO exemplaresRelVO = new ExemplaresRelVO();
		exemplaresRelVO.setTituloCatalogo(dadosSQL.getString("tituloCatalogo"));
		exemplaresRelVO.setSubTituloCatalogo(dadosSQL.getString("subtituloCatalogo"));
		exemplaresRelVO.setEditora(dadosSQL.getString("nomeEditora"));
		exemplaresRelVO.setAnoPublicacao(dadosSQL.getString("anoPublicacao"));
		exemplaresRelVO.setNomeAutor(dadosSQL.getString("nomeAutor"));
		exemplaresRelVO.setQtdeExemplar(dadosSQL.getInt("qtdeExemplar"));
		exemplaresRelVO.setEdicaoCatalogo(dadosSQL.getString("edicaoCatalogo"));
		exemplaresRelVO.setLocal(dadosSQL.getString("local"));
		return exemplaresRelVO;
	}

	@Override
	public void validarDados(CursoVO curso, PeriodoLetivoVO periodo, DisciplinaVO disciplina,Date dataInicioCompraExemplar, Date dataFimCompraExemplar  , Date dataInicioAquisicaoExemplar ,Date dataFimAquisicaoExemplar) throws Exception {
		if (curso == null || curso.getCodigo() == 0) {
			throw new ConsistirException("O Curso deve ser informado para a geração do relatório.");
		}
		if (Uteis.isAtributoPreenchido(dataInicioCompraExemplar) && !Uteis.isAtributoPreenchido(dataFimCompraExemplar)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ExemplaresRel_dataFimCompraDeveSerInformada"));
		}
		if (!Uteis.isAtributoPreenchido(dataInicioCompraExemplar) && Uteis.isAtributoPreenchido(dataFimCompraExemplar)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ExemplaresRel_dataInicioCompraDeveSerInformada"));
		}
		if (Uteis.isAtributoPreenchido(dataInicioCompraExemplar) && Uteis.isAtributoPreenchido(dataFimCompraExemplar) && UteisData.validarDataInicialMaiorFinal(dataInicioCompraExemplar, dataFimCompraExemplar)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ExemplaresPorCursoRel_dataInicialMaiorDataFinal"));
		}
		
		if (Uteis.isAtributoPreenchido(dataInicioAquisicaoExemplar) && !Uteis.isAtributoPreenchido(dataFimAquisicaoExemplar)) {
			throw new Exception(UteisJSF.internacionalizar("E necessário informar a data Final da Aquisição para consulta."));
		}
		if (!Uteis.isAtributoPreenchido(dataInicioAquisicaoExemplar) && Uteis.isAtributoPreenchido(dataFimAquisicaoExemplar)) {
			throw new Exception(UteisJSF.internacionalizar("E necessário informar a data Inicial da Aquisição para consulta."));
		}
	}

	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + getIdEntidade() + ".jrxml");
	}

	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator);
	}

	public static String getIdEntidade() {
		return "ExemplaresPorCursoRel";
	}
}
