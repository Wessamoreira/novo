package relatorio.negocio.jdbc.biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoEmprestimo;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.negocio.comuns.biblioteca.EmprestimoFiltroRelVO;
import relatorio.negocio.comuns.biblioteca.EmprestimoRelVO;
import relatorio.negocio.interfaces.biblioteca.EmprestimoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class EmprestimoRel extends SuperRelatorio implements EmprestimoRelInterfaceFacade {

	public List consultar(EmprestimoFiltroRelVO filtroVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select DISTINCT bibli.nome as biblioteca, une.nome as unidadeEnsino, ");
		sql.append(" cata.codigo as codigoCatalogo, cata.titulo as catalogo, exemp.codigobarra as exemplar, p.nome as pessoa, emp.tipopessoa as tipoPessoa, ");
		sql.append(" itememp.situacao as situacaoEmprestimo, emp.data as dataEmprestimo, itememp.datadevolucao, itememp.dataprevisaodevolucao as dataPrevDevolucao, usr.nome as responsavelEmprestimo, usrdelv.nome as responsaveldevolucao, ");
		sql.append(" itememp.valormulta as valorMulta, emp.valortotalmulta as multaTotal, ((contareceber.codigo is null or contareceber.situacao = 'RE') and itememp.situacao = 'DE') as multapaga, tipoCatalogo.nome as tipoCatalogo, itememp.isentarcobrancamulta, itememp.tipoemprestimo as tipoItemEmprestimo, itememp.valorisencao, itememp.motivoIsencao as motivoIsencao , ");

		if (filtroVO.getSituacaoEmprestimo().equals("")) {
			sql.append(" case when itememp.situacao = 'DE' then datadevolucao::DATE - dataprevisaodevolucao::DATE ");
			sql.append(" else case when itememp.situacao = 'EX' then ");
			sql.append(" case when (dataprevisaodevolucao >= CURRENT_DATE) then datadevolucao::DATE - dataprevisaodevolucao::DATE ");
			sql.append(" else CURRENT_DATE::DATE - dataprevisaodevolucao::DATE end ");
			sql.append(" else case when itememp.situacao = 'ECA' then CURRENT_DATE::DATE - dataprevisaodevolucao::DATE ");
			sql.append(" else case when itememp.situacao = 'ESA' then CURRENT_DATE::DATE - dataprevisaodevolucao::DATE ");
			sql.append(" else case when itememp.situacao = 'DCA' then datadevolucao::DATE - dataprevisaodevolucao::DATE ");
			sql.append(" else case when itememp.situacao = 'DCA' then datadevolucao::DATE - dataprevisaodevolucao::DATE ");
			sql.append(" end end end end end end diasAtraso, ");
		}
		if (filtroVO.getSituacaoEmprestimo().equals("EM")) {
			sql.append("case when (dataprevisaodevolucao >= CURRENT_DATE) then datadevolucao::DATE - dataprevisaodevolucao::DATE ");
			sql.append("else CURRENT_DATE::DATE - dataprevisaodevolucao::DATE END diasAtraso, ");
		}
		if (filtroVO.getSituacaoEmprestimo().equals("ECA")) {
			sql.append("CURRENT_DATE::DATE - dataprevisaodevolucao::DATE as diasAtraso, ");
		}
		if (filtroVO.getSituacaoEmprestimo().equals("ESA")) {
			sql.append("CURRENT_DATE::DATE - dataprevisaodevolucao::DATE as diasAtraso, ");
		}
		if (filtroVO.getSituacaoEmprestimo().equals("DE")) {
			sql.append("datadevolucao::DATE - dataprevisaodevolucao::DATE as diasAtraso, ");
		}
		if (filtroVO.getSituacaoEmprestimo().equals("DCA")) {
			sql.append("datadevolucao::DATE - dataprevisaodevolucao::DATE as diasAtraso, ");
		}
		if (filtroVO.getSituacaoEmprestimo().equals("DSA")) {
			sql.append("datadevolucao::DATE - dataprevisaodevolucao::DATE as diasAtraso, ");
		}

		sql.append("TIMESTAMP '").append(Uteis.getDataJDBC(filtroVO.getDataInicio())).append("' as dataInicial, TIMESTAMP '").append(Uteis.getDataJDBC(filtroVO.getDataFim())).append("' as dataFinal, ").append(" VARCHAR '").append(filtroVO.getTipoPessoa()).append("' as tipoPessoaFiltro, VARCHAR '").append(filtroVO.getSituacaoEmprestimo()).append("' as situacaofiltro ");
		sql.append(" from emprestimo emp ");
		sql.append(" inner join itememprestimo itememp on emp.codigo = itememp.emprestimo ");
		sql.append(" inner join exemplar exemp on exemp.codigo = itememp.exemplar ");
		sql.append(" inner join catalogo cata on exemp.catalogo = cata.codigo ");
		sql.append(" inner join biblioteca bibli on emp.biblioteca = bibli.codigo ");
		sql.append(" LEFT JOIN unidadeensinobiblioteca ueb ON ueb.biblioteca = bibli.codigo and ueb.unidadeensino = emp.unidadeensino ");
		sql.append(" LEFT JOIN unidadeensino une ON une.codigo = ueb.unidadeensino ");
		sql.append(" and emp.unidadeensino = une.codigo ");
		sql.append(" inner join pessoa p on p.codigo = emp.pessoa ");
		sql.append(" left join usuario usr on emp.atendente = usr.codigo ");
		sql.append(" left join usuario usrdelv on itememp.responsaveldevolucao = usrdelv.codigo ");
		sql.append(" left join matricula mat on mat.matricula = emp.matricula ");
		sql.append(" left join curso cur on cur.codigo = mat.curso ");
		if (!filtroVO.getTurmaVO().getCodigo().equals(0)) {
			sql.append(" left join matriculaperiodo on matriculaperiodo.matricula = mat.matricula ");
			sql.append(" left join turma tur on tur.codigo = matriculaperiodo.turma ");
		}
		sql.append(" left join contareceber on contareceber.codigo = itememp.contareceber ");
		sql.append(" left join tipocatalogo on tipocatalogo = tipocatalogo.codigo ");
		
		if (!filtroVO.getUnidadeEnsinoVO().getCodigo().equals(0)) {
			sql.append(" WHERE une.codigo = ").append(filtroVO.getUnidadeEnsinoVO().getCodigo()).append(" AND ");
		} else {
			sql.append(" WHERE ");
		}
		
		if (!filtroVO.getBibliotecaVO().getCodigo().equals(0)) {
			sql.append(" bibli.codigo = ").append(filtroVO.getBibliotecaVO().getCodigo()).append(" AND ");
		}
		sql.append(" emp.data >=  '").append(Uteis.getDataJDBC(filtroVO.getDataInicio())).append(" 00:00:00' ");
		sql.append(" AND emp.data <=  '").append(Uteis.getDataJDBC(filtroVO.getDataFim())).append(" 23:59:59' ");

		if (!filtroVO.getCursoVO().getCodigo().equals(0)) {
			sql.append("AND cur.codigo = '").append(filtroVO.getCursoVO().getCodigo()).append("'");
		}
		// Filtro Adicionado para impressao a partir do "tipoCatalago"
		if (Uteis.isAtributoPreenchido(filtroVO.getCatalogoVO().getTipoCatalogo())) {
			if(!filtroVO.getConsiderarSubTiposCatalogo()) {
				sql.append(" and tipoCatalogo.codigo = ").append(filtroVO.getCatalogoVO().getTipoCatalogo().getCodigo());
			}else {
				sql.append(" and (tipoCatalogo.codigo = ").append(filtroVO.getCatalogoVO().getTipoCatalogo().getCodigo()).append(" or tipocatalogo.codigo in ").append("("
						+ " select * from ( with recursive tipocatalogoinferior(codigo) as ("
						+ " select distinct tp.codigo from tipocatalogo tp "
						+ " left join tipocatalogo as tipocatalogoprincipal on tipocatalogoprincipal.codigo = tp.subdivisao "
						+ " where tp.codigo = ").append(filtroVO.getCatalogoVO().getTipoCatalogo().getCodigo()).append(" union "
						+ " select tp.codigo from tipocatalogo tp "
						+ " left join tipocatalogo as tipocatalogoprincipal on 	tipocatalogoprincipal.codigo = tp.subdivisao "
						+ " inner join tipocatalogoinferior on tipocatalogoinferior.codigo = tp.subdivisao "
						+ " ) select * from tipocatalogoinferior ) as t where codigo <> ").append(filtroVO.getCatalogoVO().getTipoCatalogo().getCodigo()).append("))");
			}
		} 
		
		if (!filtroVO.getTurmaVO().getCodigo().equals(0)) {
			sql.append("AND tur.codigo = '").append(filtroVO.getTurmaVO().getCodigo()).append("'");
		}
		if (filtroVO.getSituacaoEmprestimo().equals("EM")) {
			sql.append("AND itememp.situacao = '").append("EX' ");
		}

		if (filtroVO.getSituacaoEmprestimo().equals("DE")) {
			sql.append("AND itememp.situacao = '").append("DE' ");
		}

		if (filtroVO.getSituacaoEmprestimo().equals("ECA")) {
			sql.append("AND itememp.situacao = '").append("EX'").append(" AND itememp.dataprevisaodevolucao < CURRENT_DATE ");
		}

		if (filtroVO.getSituacaoEmprestimo().equals("ESA")) {
			sql.append("AND itememp.situacao = '").append("EX'").append(" AND CURRENT_DATE < itememp.dataprevisaodevolucao ");
		}

		if (filtroVO.getSituacaoEmprestimo().equals("DCA")) {
			sql.append("AND itememp.situacao = '").append("DE'").append(" AND itememp.dataprevisaodevolucao < itememp.datadevolucao ");
		}

		if (filtroVO.getSituacaoEmprestimo().equals("DSA")) {
			sql.append("AND itememp.situacao = '").append("DE'").append(" AND itememp.datadevolucao < itememp.dataprevisaodevolucao ");
		}

		if (!filtroVO.getMatriculaVO().getAluno().getCodigo().equals(0)) {
			sql.append("AND p.codigo = ").append(filtroVO.getMatriculaVO().getAluno().getCodigo()).append(" ");
		}

		if (!filtroVO.getProfessorVO().getPessoa().getCodigo().equals(0)) {
			sql.append("AND p.codigo = ").append(filtroVO.getProfessorVO().getPessoa().getCodigo()).append(" ");
		}

		if (!filtroVO.getFuncionarioVO().getPessoa().getCodigo().equals(0)) {
			sql.append("AND p.codigo = ").append(filtroVO.getFuncionarioVO().getPessoa().getCodigo()).append(" ");
		}
		if (!filtroVO.getPessoa().getCodigo().equals(0)) {
			sql.append("AND p.codigo = ").append(filtroVO.getPessoa().getCodigo()).append(" ");
		}

		if (!filtroVO.getCatalogoVO().getTitulo().equals("")) {
			sql.append("AND cata.codigo = '").append(filtroVO.getCatalogoVO().getCodigo()).append("' ");
		}

		if (filtroVO.getTipoPessoa().equals("AL") && filtroVO.getMatriculaVO().getAluno().getCodigo().equals(0)) {
			sql.append(" AND emp.tipoPessoa = 'AL' ");
		}

		if (filtroVO.getTipoPessoa().equals("FU") && filtroVO.getFuncionarioVO().getPessoa().getCodigo().equals(0)) {
			sql.append("AND emp.tipoPessoa = 'FU' ");
		}

		if (filtroVO.getTipoPessoa().equals("PR") && filtroVO.getProfessorVO().getPessoa().getCodigo().equals(0)) {
			sql.append("AND emp.tipoPessoa = 'PR' ");
		}
		if (filtroVO.getTipoPessoa().equals("MC") && filtroVO.getPessoa().getCodigo().equals(0)) {
			sql.append("AND emp.tipoPessoa = 'MC' ");
		}
		sql.append(" GROUP BY cata.codigo, cata.titulo, emp.biblioteca, bibli.nome, une.nome, exemp.codigobarra, p.nome, contareceber.codigo, contareceber.situacao, ");
		sql.append(" emp.tipopessoa, emp.data, itememp.situacao, itememp.datadevolucao, itememp.dataprevisaodevolucao, usr.nome, itememp.valormulta, emp.valortotalmulta, tipoCatalogo.nome, itememp.isentarcobrancamulta, itememp.tipoemprestimo, itememp.valorisencao, itememp.motivoIsencao,usrdelv.nome ");
		if (filtroVO.getOrdenarPor().equals("catalogo")) {
			sql.append(" ORDER BY cata.titulo, cata.codigo, emp.data, p.nome ");
		}
		if (filtroVO.getOrdenarPor().equals("tombo")) {
			sql.append(" ORDER BY exemp.codigobarra, emp.data, p.nome");
		}
		if (filtroVO.getOrdenarPor().equals("dataEmprestimo")) {
			sql.append(" ORDER BY emp.data, p.nome, cata.titulo  ");
		}
		if (filtroVO.getOrdenarPor().equals("dataPrevisao")) {
			sql.append(" ORDER BY itememp.dataprevisaodevolucao, p.nome, cata.titulo ");
		}
		if (filtroVO.getOrdenarPor().equals("dataDevolucao")) {
			sql.append(" ORDER BY itememp.datadevolucao, p.nome, cata.titulo ");
		}
		if (filtroVO.getOrdenarPor().equals("pessoa")) {
			sql.append(" ORDER BY p.nome, emp.data, cata.titulo ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado);
	}

	private List montarDadosConsulta(SqlRowSet dadosSQL) throws Exception {
		ArrayList lista = new ArrayList(0);
		while (dadosSQL.next()) {
			lista.add(montarDados(dadosSQL));
		}
		return lista;
	}

	private EmprestimoRelVO montarDados(SqlRowSet dados) throws Exception {
		EmprestimoRelVO vo = new EmprestimoRelVO();
		vo.setUnidadeEnsino(dados.getString("unidadeEnsino"));
		if (dados.getString("situacaofiltro") != null && !dados.getString("situacaofiltro").equals("")) {
			vo.setSituacao(SituacaoEmprestimo.getDescricao(dados.getString("situacaofiltro")));
		} else {
			vo.setSituacao("Todas");
		}
		if (dados.getString("tipoPessoaFiltro") != null && !dados.getString("tipoPessoaFiltro").equals("")) {
			vo.setTipoPessoa(TipoPessoa.getDescricao(dados.getString("tipoPessoaFiltro")));
		} else {
			vo.setTipoPessoa("Todos");
		}
		vo.setBiblioteca(dados.getString("biblioteca"));
		vo.setDataInicial(dados.getDate("dataInicial"));
		vo.setDataFinal(dados.getDate("dataFinal"));
		vo.setCodigoCatalogo(dados.getInt("codigoCatalogo"));
		vo.setCatalogo(dados.getString("catalogo"));
		vo.setExemplar(dados.getString("exemplar"));
		vo.setPessoaEmprestimo(dados.getString("pessoa"));
		if (dados.getString("tipoItemEmprestimo") != null && dados.getString("tipoItemEmprestimo").equals("HR")) {
			vo.setDataEmprestimo(Uteis.getData(dados.getDate("dataEmprestimo"), "dd/MM/yyyy hh:mm"));
			vo.setDataDevolucao(Uteis.getData(dados.getTimestamp("dataDevolucao"), "dd/MM/yyyy hh:mm"));
			vo.setDataPrevDevolucao(Uteis.getData(dados.getDate("dataPrevDevolucao"), "dd/MM/yyyy hh:mm"));
		} else {
			vo.setDataEmprestimo(Uteis.getData(dados.getDate("dataEmprestimo"), "dd/MM/yyyy"));
			vo.setDataDevolucao(Uteis.getData(dados.getDate("dataDevolucao"), "dd/MM/yyyy"));
			vo.setDataPrevDevolucao(Uteis.getData(dados.getDate("dataPrevDevolucao"), "dd/MM/yyyy"));
		}
		if (dados.getString("situacaoEmprestimo").equals(SituacaoEmprestimo.DEVOLVIDO.getValor())) {
			vo.setResponsavelEmprestimo(dados.getString("responsaveldevolucao"));
		} else {
			vo.setResponsavelEmprestimo(dados.getString("responsavelEmprestimo"));
		}
		vo.setValorMulta(dados.getDouble("valorMulta"));
		vo.setTotalMultaEmprestimo(dados.getDouble("multaTotal"));
		vo.setSituacaoEmprestimo(dados.getString("situacaoEmprestimo"));
		vo.setMultaPaga(dados.getBoolean("multaPaga"));
		vo.setMultaIsenta(dados.getBoolean("isentarcobrancamulta"));
		vo.setValorMultaIsenta(dados.getDouble("valorisencao"));
		vo.setMotivoIsencao(dados.getString("motivoIsencao"));
		vo.setTipoItemEmprestimo(dados.getString("tipoItemEmprestimo"));
		

		Integer diasAtraso = new Double(dados.getDouble("diasAtraso")).intValue();
		if (diasAtraso <= 0) {
			vo.setTipoEmprestimo("EM DIA");
			vo.setDiasAtraso(0);
		} else {
			vo.setTipoEmprestimo("ATRASADO");
			vo.setDiasAtraso(diasAtraso);
		}
		vo.setTipoCatalogo(dados.getString("tipoCatalogo"));

		return vo;
	}

	public void validarDados(EmprestimoFiltroRelVO filtroVO) throws Exception {
//		if (filtroVO.getUnidadeEnsinoVO().getCodigo() == null || filtroVO.getUnidadeEnsinoVO().getCodigo() == 0) {
//			throw new Exception("Para Emissão do Relatório é necessário informar a Unidade de Ensino.");
//		}
		if (filtroVO.getBibliotecaVO().getCodigo() == null || filtroVO.getBibliotecaVO().getCodigo() == 0) {
			throw new Exception("Para Emissão do Relatório é necessário informar a Biblioteca.");
		}
		if (filtroVO.getDataInicio() == null || !Uteis.isAtributoPreenchido(filtroVO.getDataInicio())) {
			throw new Exception("Para Emissão do Relatório é necessário informar a Data Inicial.");
		}
		if (filtroVO.getDataFim() == null || !Uteis.isAtributoPreenchido(filtroVO.getDataFim())) {
			throw new Exception("Para Emissão do Relatório é necessário informar a Data Final.");
		}
	}

	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + getIdEntidade() + ".jrxml");
	}

	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator);
	}

	public static String getIdEntidade() {
		return "EmprestimoRel";
	}

}
