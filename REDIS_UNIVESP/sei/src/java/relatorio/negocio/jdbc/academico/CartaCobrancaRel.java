/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CartaCobrancaAlunoVO;
import negocio.comuns.financeiro.CartaCobrancaRelVO;
import negocio.comuns.financeiro.CartaCobrancaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.interfaces.academico.CartaCobrancaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
public class CartaCobrancaRel extends SuperRelatorio implements CartaCobrancaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	/**
	 * Responsável por transformar a lista de Unidade Ensino provinda do banco
	 * em lista de SelectItems para ser usada no componente selectItens na tela
	 * 
	 * @param unidadeEnsinoLogado
	 * @param usuarioLogado
	 * @return SelectItens
	 * @throws Exception
	 * 
	 */
	@Override
	public List<SelectItem> montarListaSelectItemUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("", unidadeEnsinoLogado, usuarioLogado);
		return UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome");
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, unidadeEnsinoLogado.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<CartaCobrancaRelVO> criarObjeto(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, CartaCobrancaRelVO cartaCobranca, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, String aluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs, String centroReceita) throws Exception {
		try {
			CartaCobrancaRel.emitirRelatorio(getIdEntidade(), false, usuarioVO);
			List<CartaCobrancaRelVO> listaRelatorio = execultarConsultaCartaCobranca(unidadeEnsino, curso, turma, matricula, cartaCobranca, usuarioVO, periodoInicial, periodoFinal, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO, centroReceitaVOs);
			ConfiguracaoFinanceiroVO confFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, unidadeEnsino.getCodigo());
			if (!listaRelatorio.isEmpty()) {
				CartaCobrancaVO cartaCobrancaVO = executarMontagemCartaCobrancaVO(unidadeEnsino, curso, turma, matricula, cartaCobranca, usuarioVO, periodoInicial, periodoFinal, aluno, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO, centroReceita);
				getFacadeFactory().getCartaCobrancaFacade().incluir(cartaCobrancaVO);
				executarInclusaoCartaCobrancaAluno(listaRelatorio, confFinanceiro.getTextoPadraoCartaCobranca(), cartaCobrancaVO);
			}
			return listaRelatorio;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void validarDados(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, Date dataFim) throws ConsistirException {
		if (dataFim.after(new Date())) {
			throw new ConsistirException("O campo Data Final (Relatório Carta de Cobrança) deve ser menor ou igual a data de hoje.");
		}
		if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			throw new ConsistirException("O campo Unidade De Ensino (Relatório Carta de Cobrança) deve ser informado.");
		}
//		if (!Uteis.isAtributoPreenchido(curso)) {
//			throw new ConsistirException("O campo Curso (Relatório Carta de Cobrança) deve ser informado.");
//		}
	}

	@Override
	public String designIReportRelatorio(String tipoLayout) {
		if (tipoLayout.equals("paisagem")) {
			return caminhoBaseRelatorio() + getIdEntidade() + ".jrxml";
		}
		return caminhoBaseRelatorio() + getIdEntidadeRetrato() + ".jrxml";
	}

	@Override
	public String caminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator;
	}

	public static String getIdEntidade() {
		return "CartaCobrancaRel";
	}
	
	public List<CartaCobrancaRelVO> execultarConsultaCartaCobranca(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, CartaCobrancaRelVO cartaCobranca, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct curso.codigo, matricula.matricula as matricula, aluno.nome AS aluno, aluno.cpf as cpf,  curso.nome as curso,  turma.identificadorturma turma, UnidadeEnsino.razaosocial as unidade, UnidadeEnsino.telcomercial1 as fixo, UnidadeEnsino.telcomercial2 as celular,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.nome else pessoa.nome end AS nomeResponsavel ,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.endereco else pessoa.endereco end AS enderecoResponsacel,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.numero else pessoa.numero end AS numeroResponsacel,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.complemento else pessoa.complemento end AS complementoResponsacel,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.setor else pessoa.setor end AS setor,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then estadoPais.nome else estadoAluno.nome end AS estado,");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then cidadePais.nome else cidadeAluno.nome end AS cidade, ");
		sql.append(" CASE WHEN (filiacao.pais is not null and filiacao.pais != 0 and filiacao.responsavelfinanceiro = true) then pais.cep else aluno.cep end AS cep, ");
		sql.append(" cidadeUnidade.nome as nomeCidadeUnidadeEnsino, contareceber.tipopessoa, ");		
		sql.append(" case when contareceber.tipopessoa = 'AL' and pais.codigo is not null then pais.codigo else pessoa.codigo end as codigoResponsavel, ");
		sql.append(" case when contareceber.tipopessoa = 'AL' and pais.codigo is not null then pais.cpf else pessoa.cpf end as cpfResponsavel ");
		sql.append(" from contareceber ");
		sql.append(" inner join pessoa on ((contareceber.tipopessoa = 'AL' and contareceber.pessoa = pessoa.codigo) or (contareceber.tipopessoa = 'RF' and contareceber.responsavelfinanceiro = pessoa.codigo))");
		if(cartaCobranca.isConsiderarUnidadeEnsinoFinanceira()){
			sql.append(" inner JOIN UnidadeEnsino ON (unidadeEnsino.codigo = contareceber.unidadeEnsinoFinanceira)");
		}else{
			sql.append(" inner JOIN UnidadeEnsino ON (unidadeEnsino.codigo = contareceber.unidadeEnsino)");
		}
		sql.append(" left join pessoa as aluno on contareceber.pessoa = aluno.codigo ");
		sql.append(" left join matricula on contareceber.matriculaaluno = matricula.matricula");
		sql.append(" left join curso on matricula.curso=curso.codigo");
		sql.append(" left join matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		sql.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by mp.ano ||'/'|| mp.semestre desc, case when matriculaperiodo.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, matriculaperiodo.codigo desc limit 1)");
		sql.append(" left join turma on turma.codigo=matriculaPeriodo.turma");		
		sql.append(" left join filiacao on filiacao.aluno= aluno.codigo and filiacao.responsavelfinanceiro = true and contareceber.tipopessoa = 'AL' ");
		sql.append(" left join pessoa as pais on pais.codigo= filiacao.pais ");
		sql.append(" left join cidade as cidadePais on cidadePais.codigo=pais.cidade");
		sql.append(" left join estado  as estadoPais on estadoPais.codigo=cidadePais.estado");
		sql.append(" left join cidade as cidadeAluno on cidadeAluno.codigo=pessoa.cidade");
		sql.append(" left join estado as estadoAluno on estadoAluno.codigo=cidadeAluno.estado");
		sql.append(" left join cidade as cidadeUnidade on cidadeunidade.codigo=UnidadeEnsino.cidade");
		validarFiltrosInformadosParaRelatorio(unidadeEnsino, curso, turma, matricula, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO, centroReceitaVOs, periodoInicial, periodoFinal, sql);
		sql.append(" order by aluno ");
		SqlRowSet sqlRowSet = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosCartaCobranca(sqlRowSet, unidadeEnsino, curso, turma, matricula, cartaCobranca, usuarioVO, periodoInicial, periodoFinal, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO, centroReceitaVOs); 

	}

	private void validarFiltrosInformadosParaRelatorio(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs, Date periodoInicial, Date periodoFinal, StringBuilder sql) {
		sql.append(" WHERE contareceber.situacao = 'AR' and (contareceber.tipopessoa = 'AL' or  contareceber.tipopessoa = 'RF')");
		if (matricula == null || matricula.getMatricula().equals("")) {
			sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		}
		StringBuilder sql2 = new StringBuilder("");
		for (CentroReceitaVO centroReceitaVO : centroReceitaVOs) {
			if (centroReceitaVO.getFiltrarCentroReceitaVO()) {
				sql2.append(sql2.length() == 0 ? " and contareceber.centroreceita in(" : ", ").append(centroReceitaVO.getCodigo());
			}
		}
		if (sql2.length() > 0) {
			sql2.append(") ");
			sql.append(sql2);
		}
		sql.append(" and ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO,  "contareceber"));
		if (unidadeEnsino != null && unidadeEnsino.getCodigo() != 0) {
			sql.append(" AND unidadeEnsino.codigo=").append(unidadeEnsino.getCodigo());
		}
		if (curso != null && curso.getCodigo().intValue() != 0) {
			sql.append(" AND curso.codigo=").append(curso.getCodigo());
		}
		if (turma != null && turma.getCodigo().intValue() != 0) {
			sql.append(" AND turma.codigo=").append(turma.getCodigo());
		}
		if (matricula != null && !matricula.getMatricula().trim().isEmpty()) {
			sql.append(" AND matricula.matricula = '").append(matricula.getMatricula()).append("' ");
		}
		sql.append(" AND ((contareceber.datavencimento >='").append(Uteis.getDataJDBC(periodoInicial)).append("' and contareceber.datavencimento <= '").append(Uteis.getDataJDBC(periodoFinal)).append("')").append(" and contareceber.datavencimento < CURRENT_DATE)");
	}

	private List<CartaCobrancaRelVO> montarDadosCartaCobranca(SqlRowSet sql2, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, CartaCobrancaRelVO cartaCobranca, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs) throws Exception {
		List<CartaCobrancaRelVO> listaConsulta = new ArrayList<CartaCobrancaRelVO>(0);
		while (sql2.next()) {
			CartaCobrancaRelVO obj = new CartaCobrancaRelVO();
			obj.setNomeCidade(sql2.getString("cidade"));
			obj.setNomeResponsavel(sql2.getString("nomeResponsavel"));
			obj.setEnderecoResponsavel(sql2.getString("enderecoResponsacel"));
			if (Uteis.isAtributoPreenchido(sql2.getString("numeroResponsacel"))) {
				obj.setEnderecoResponsavel(obj.getEnderecoResponsavel() + ", " + sql2.getString("numeroResponsacel"));
			}
			if (Uteis.isAtributoPreenchido(sql2.getString("complementoResponsacel"))) {
				obj.setEnderecoResponsavel(obj.getEnderecoResponsavel() + ", " + sql2.getString("complementoResponsacel"));
			}
			obj.setTipoPessoa(sql2.getString("tipoPessoa"));
			obj.setCpfResponsavel(sql2.getString("cpfResponsavel"));
			obj.setCodigoResponsavel(sql2.getInt("codigoResponsavel"));
			obj.setNomeAluno(sql2.getString("aluno"));
			obj.setCpfAluno(sql2.getString("cpf"));
			obj.setNumeroMatricula(sql2.getString("matricula"));
			obj.setNomeCurso(sql2.getString("curso"));
			obj.setEstadoResponsavel(sql2.getString("estado"));
			obj.setCepResponsavel(sql2.getString("cep"));
			obj.setTurma(sql2.getString("turma"));
			obj.setSetor(sql2.getString("setor"));
			obj.setNomeUnidadeEnsino(sql2.getString("unidade"));
			obj.setFixoUnidadeEnsino(sql2.getString("fixo"));
			obj.setCelularUnidadeEnsino(sql2.getString("celular"));
			obj.setNomeCidadeUnidadeEnsino(sql2.getString("nomeCidadeUnidadeEnsino"));
			obj.setDebitosDataInicial(Uteis.getData(periodoInicial));
			obj.setDataPorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(obj.getNomeCidadeUnidadeEnsino(), new Date(), false));
			obj.setConsiderarUnidadeEnsinoFinanceira(cartaCobranca.isConsiderarUnidadeEnsinoFinanceira());			
			obj.setDebitosLista(execultarConsultaTituloCobranca(unidadeEnsino, curso, turma, matricula, obj, usuarioVO, periodoInicial, periodoFinal, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO, centroReceitaVOs));
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}

	public List<ContaReceberVO> execultarConsultaTituloCobranca(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, CartaCobrancaRelVO cartaCobranca, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select parcela, datavencimento, valor, nossonumero ");
		sql.append(" from contareceber ");
		sql.append(" inner join pessoa on ((contareceber.tipopessoa = 'AL' and contareceber.pessoa = pessoa.codigo) or (contareceber.tipopessoa = 'RF' and contareceber.responsavelfinanceiro = pessoa.codigo))");
		if(cartaCobranca.isConsiderarUnidadeEnsinoFinanceira()){
			sql.append(" inner JOIN UnidadeEnsino ON (unidadeEnsino.codigo = contareceber.unidadeEnsinoFinanceira)");
		}else{
			sql.append(" inner JOIN UnidadeEnsino ON (unidadeEnsino.codigo = contareceber.unidadeEnsino)");
		}
		sql.append(" left join matricula on contareceber.matriculaaluno = matricula.matricula ");
		sql.append(" left join curso on matricula.curso=curso.codigo");
		sql.append(" left join matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		sql.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by mp.ano ||'/'|| mp.semestre desc, case when matriculaperiodo.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, matriculaperiodo.codigo desc limit 1)");
		sql.append(" left join turma on turma.codigo=matriculaPeriodo.turma");
		validarFiltrosInformadosParaRelatorio(unidadeEnsino, curso, turma, null, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO, centroReceitaVOs, periodoInicial, periodoFinal, sql);
		if(!cartaCobranca.getNumeroMatricula().trim().isEmpty()) {
			sql.append(" and matricula.matricula = '").append(cartaCobranca.getNumeroMatricula()).append("' ");
		}else {
			sql.append(" and pessoa.codigo = '").append(cartaCobranca.getCodigoResponsavel()).append("' ");
		}
		sql.append(" order by datavencimento, parcela");
		SqlRowSet sqls = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosTitulo(sqls);
	}

	private List<ContaReceberVO> montarDadosTitulo(SqlRowSet sqls) throws Exception {
		List<ContaReceberVO> listaConsulta = new ArrayList<ContaReceberVO>(0);
		while (sqls.next()) {
			ContaReceberVO obj = new ContaReceberVO();
			obj.setParcela(sqls.getString("parcela"));
			obj.setDataVencimento(sqls.getDate("datavencimento"));
			obj.setValor(sqls.getDouble("valor"));
			obj.setNossoNumero(sqls.getString("nossonumero"));
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}

	private CartaCobrancaVO executarMontagemCartaCobrancaVO(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, CartaCobrancaRelVO cartaCobranca, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, String aluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String centroReceita) throws Exception {
		CartaCobrancaVO cartaCobrancaVO = new CartaCobrancaVO();
		try {
			cartaCobrancaVO.setAluno(aluno);
			cartaCobrancaVO.setMatricula(matricula.getMatricula());
			cartaCobrancaVO.setCursoVO(curso);
			cartaCobrancaVO.setTurmaVO(turma);
			cartaCobrancaVO.setUsuarioVO(usuarioVO);
			cartaCobrancaVO.setUnidadeEnsinoVO(unidadeEnsino);
			cartaCobrancaVO.setDataGeracao(new Date());
			cartaCobrancaVO.setDataInicioFiltro(periodoInicial);
			cartaCobrancaVO.setDataFimFiltro(periodoFinal);
			cartaCobrancaVO.setCentroReceitaApresentar(centroReceita);
			if (matricula.getMatricula().equals("")) {
				cartaCobrancaVO.setFiltroRelatorioAcademicoVO(filtroRelatorioAcademicoVO);
			}
			cartaCobrancaVO.setFiltroRelatorioFinanceiroVO(filtroRelatorioFinanceiroVO);
		} catch (Exception e) {
			throw e;
		}
		return cartaCobrancaVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarInclusaoCartaCobrancaAluno(List<CartaCobrancaRelVO> listaCartasCobranca, String textoPadraoCartaCobranca, CartaCobrancaVO cartaCobranca) throws Exception {
		for (CartaCobrancaRelVO cartaCobrancaRelVO : listaCartasCobranca) {
			cartaCobrancaRelVO.setTextoPadraoCartaCobranca(textoPadraoCartaCobranca);
			getFacadeFactory().getCartaCobrancaAlunoFacade().incluir(executarMontagemCartaCobrancaAlunoVO(cartaCobrancaRelVO, cartaCobranca));
		}
	}

	private CartaCobrancaAlunoVO executarMontagemCartaCobrancaAlunoVO(CartaCobrancaRelVO cartaCobrancaRelVO, CartaCobrancaVO cartaCobranca) throws Exception {
		CartaCobrancaAlunoVO cartaCobrancaAlunoVO = new CartaCobrancaAlunoVO();
		try {
			cartaCobrancaAlunoVO.setAluno(cartaCobrancaRelVO.getNomeAluno());
			cartaCobrancaAlunoVO.setMatricula(cartaCobrancaRelVO.getNumeroMatricula());
			cartaCobrancaAlunoVO.setResponsavelFinanceiro(cartaCobrancaRelVO.getNomeResponsavel());
			cartaCobrancaAlunoVO.setTipoPessoa(cartaCobrancaRelVO.getTipoPessoa());
			cartaCobrancaAlunoVO.setCartacobranca(cartaCobranca.getCodigo());
			StringBuilder contas = new StringBuilder("<table class=\"rf-dt tabConsulta\">	");
			contas.append("<colgroup span=\"4\"></colgroup>");
			contas.append("<thead class=\"rf-dt-thd\">");
			contas.append("<tr class=\"rf-dt-shdr\">");
			contas.append("<th class=\"rf-dt-shdr-c\" scope=\"col\" >Parcela</th>");
			contas.append("<th class=\"rf-dt-shdr-c\" scope=\"col\" >Data Vencimento</th>");
			contas.append("<th class=\"rf-dt-shdr-c\" scope=\"col\" >Valor</th>");
			contas.append("<th class=\"rf-dt-shdr-c\" scope=\"col\" >Nosso Número</th>");
			contas.append("</tr>");
			contas.append("</thead>");
			contas.append("<tbody class=\"rf-dt-b\">");
			for(ContaReceberVO contaReceberVO: cartaCobrancaRelVO.getDebitosLista()) {
				contas.append("<tr class=\"rf-dt-r\">");
				contas.append("<td class=\"rf-dt-c colunaAlinhamento\"><span>").append(contaReceberVO.getParcela()).append("</span></td>");
				contas.append("<td class=\"rf-dt-c colunaAlinhamento\"><span>").append(contaReceberVO.getDataVencimento_Apresentar()).append("</span></td>");
				contas.append("<td class=\"rf-dt-c colunaAlinhamento\"><span>").append(Uteis.getDoubleFormatado(contaReceberVO.getValor())).append("</span></td>");
				contas.append("<td class=\"rf-dt-c colunaAlinhamento\"><span>").append(contaReceberVO.getNossoNumero()).append("</span></td>");
				contas.append("</tr>");
			}
			contas.append("</tbody>");
			contas.append("</table>");
			cartaCobrancaAlunoVO.setContas(contas.toString());
		} catch (Exception e) {
			throw e;
		}
		return cartaCobrancaAlunoVO;
	}
	
	public static String getIdEntidadeRetrato() {
		return "CartaCobrancaRetratoRel";
	}


}
