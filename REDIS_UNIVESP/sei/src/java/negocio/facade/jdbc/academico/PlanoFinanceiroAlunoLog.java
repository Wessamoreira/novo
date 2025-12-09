package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoLogVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.PlanoFinanceiroAlunoLogInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class PlanoFinanceiroAlunoLog extends ControleAcesso implements PlanoFinanceiroAlunoLogInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1544290557085057731L;

	@Override
	public void realizarCriacaoLogPlanoFinanceiroAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception {
		PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);

		StringBuilder log = new StringBuilder("");
		if (planoFinanceiroAlunoVO.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().intValue() != matriculaVO.getPlanoFinanceiroAluno().getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().intValue()) {
			log.append(realizarCriacaoLinhaLog(UteisJSF.internacionalizar("prt_PlanoFinanceiroCurso_tituloForm"), planoFinanceiroAlunoVO.getPlanoFinanceiroCursoVO().getDescricao() + " - " + planoFinanceiroAlunoVO.getCondicaoPagamentoPlanoFinanceiroCursoVO().getDescricao(),matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroCursoVO().getDescricao() + " - " + matriculaVO.getPlanoFinanceiroAluno().getCondicaoPagamentoPlanoFinanceiroCursoVO().getDescricao()));
		}
		if (planoFinanceiroAlunoVO.getDescontoProgressivoMatricula().getCodigo().intValue() != matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivoMatricula().getCodigo().intValue()) {
			log.append(realizarCriacaoLinhaLog("Desconto Progressivo Matrícula", planoFinanceiroAlunoVO.getDescontoProgressivoMatricula().getNome(), matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivoMatricula().getNome()));
		}
		if (planoFinanceiroAlunoVO.getDescontoProgressivo().getCodigo().intValue() != matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo().getCodigo().intValue()) {
			log.append(realizarCriacaoLinhaLog("Desconto Progressivo Parcela", planoFinanceiroAlunoVO.getDescontoProgressivo().getNome(), matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo().getNome()));
		}
		if (planoFinanceiroAlunoVO.getDescontoProgressivoPrimeiraParcela().getCodigo().intValue() != matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivoPrimeiraParcela().getCodigo().intValue()) {
			log.append(realizarCriacaoLinhaLog("Desconto Progressivo Primeira Parcela", planoFinanceiroAlunoVO.getDescontoProgressivoPrimeiraParcela().getNome(), matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivoPrimeiraParcela().getNome()));
		}
		if (!planoFinanceiroAlunoVO.getTipoDescontoMatricula().equals(matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoMatricula())) {
			log.append(realizarCriacaoLinhaLog("Tipo Desconto Matrícula", planoFinanceiroAlunoVO.getTipoDescontoMatricula_Apresentar(), matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoMatricula_Apresentar()));
		}
		if (planoFinanceiroAlunoVO.getValorDescontoMatricula().doubleValue() != matriculaVO.getPlanoFinanceiroAluno().getValorDescontoMatricula().doubleValue()) {
			log.append(realizarCriacaoLinhaLog("Valor Desconto Matrícula", Uteis.formatarDecimalDuasCasas(planoFinanceiroAlunoVO.getValorDescontoMatricula()), Uteis.formatarDecimalDuasCasas(matriculaVO.getPlanoFinanceiroAluno().getValorDescontoMatricula())));
		}
		if (planoFinanceiroAlunoVO.getPercDescontoMatricula().doubleValue() != matriculaVO.getPlanoFinanceiroAluno().getPercDescontoMatricula().doubleValue()) {
			log.append(realizarCriacaoLinhaLog("Porcentagem Desconto Matrícula", Uteis.formatarDecimalDuasCasas(planoFinanceiroAlunoVO.getPercDescontoMatricula()), Uteis.formatarDecimalDuasCasas(matriculaVO.getPlanoFinanceiroAluno().getPercDescontoMatricula())));
		}
		if (!planoFinanceiroAlunoVO.getTipoDescontoParcela().equals(matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela())) {
			log.append(realizarCriacaoLinhaLog("Tipo Desconto Matrícula", planoFinanceiroAlunoVO.getTipoDescontoParcela_Apresentar(), matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela_Apresentar()));
		}
		if (planoFinanceiroAlunoVO.getValorDescontoParcela().doubleValue() != matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela().doubleValue()) {
			log.append(realizarCriacaoLinhaLog("Valor Desconto Parcela", Uteis.formatarDecimalDuasCasas(planoFinanceiroAlunoVO.getValorDescontoParcela()), Uteis.formatarDecimalDuasCasas(matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela())));
		}
		if (planoFinanceiroAlunoVO.getPercDescontoParcela().doubleValue() != matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela().doubleValue()) {
			log.append(realizarCriacaoLinhaLog("Porcentagem Desconto Parcela", Uteis.formatarDecimalDuasCasas(planoFinanceiroAlunoVO.getPercDescontoParcela()), Uteis.formatarDecimalDuasCasas(matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela())));
		}
		if (planoFinanceiroAlunoVO.getOrdemConvenioValorCheio() != matriculaVO.getPlanoFinanceiroAluno().getOrdemConvenioValorCheio()) {
			log.append(realizarCriacaoLinhaLog("Desc. Convênio Valor Cheio", planoFinanceiroAlunoVO.getOrdemConvenioValorCheio() ? "Sim" : "Não", matriculaVO.getPlanoFinanceiroAluno().getOrdemConvenioValorCheio() ? "Sim" : "Não"));
		}
		if (planoFinanceiroAlunoVO.getOrdemDescontoAlunoValorCheio() != matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoAlunoValorCheio()) {
			log.append(realizarCriacaoLinhaLog("Desc. Aluno Valor Cheio", planoFinanceiroAlunoVO.getOrdemDescontoAlunoValorCheio() ? "Sim" : "Não", matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoAlunoValorCheio() ? "Sim" : "Não"));
		}
		if (planoFinanceiroAlunoVO.getOrdemDescontoProgressivoValorCheio() != matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoProgressivoValorCheio()) {
			log.append(realizarCriacaoLinhaLog("Desc. Progressivo Valor Cheio", planoFinanceiroAlunoVO.getOrdemDescontoProgressivoValorCheio() ? "Sim" : "Não", matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoProgressivoValorCheio() ? "Sim" : "Não"));
		}
		if (planoFinanceiroAlunoVO.getOrdemPlanoDescontoValorCheio() != matriculaVO.getPlanoFinanceiroAluno().getOrdemPlanoDescontoValorCheio()) {
			log.append(realizarCriacaoLinhaLog("Desc. Instituição Valor Cheio", planoFinanceiroAlunoVO.getOrdemPlanoDescontoValorCheio() ? "Sim" : "Não", matriculaVO.getPlanoFinanceiroAluno().getOrdemPlanoDescontoValorCheio() ? "Sim" : "Não"));
		}
		if (planoFinanceiroAlunoVO.getOrdemConvenio().intValue() != matriculaVO.getPlanoFinanceiroAluno().getOrdemConvenio().intValue()) {
			log.append(realizarCriacaoLinhaLog("Ordem Desc. Convênio", planoFinanceiroAlunoVO.getOrdemConvenio().toString(), matriculaVO.getPlanoFinanceiroAluno().getOrdemConvenio().toString()));
		}
		if (planoFinanceiroAlunoVO.getOrdemDescontoAluno().intValue() != matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoAluno().intValue()) {
			log.append(realizarCriacaoLinhaLog("Ordem Desc. Aluno", planoFinanceiroAlunoVO.getOrdemDescontoAluno().toString(), matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoAluno().toString()));
		}
		if (planoFinanceiroAlunoVO.getOrdemDescontoProgressivo().intValue() != matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoProgressivo().intValue()) {
			log.append(realizarCriacaoLinhaLog("Ordem Desc. Progressivo", planoFinanceiroAlunoVO.getOrdemDescontoProgressivo().toString(), matriculaVO.getPlanoFinanceiroAluno().getOrdemDescontoProgressivo().toString()));
		}
		if (planoFinanceiroAlunoVO.getOrdemPlanoDesconto().intValue() != matriculaVO.getPlanoFinanceiroAluno().getOrdemPlanoDesconto().intValue()) {
			log.append(realizarCriacaoLinhaLog("Ordem Desc. Instituição", planoFinanceiroAlunoVO.getOrdemPlanoDesconto().toString(), matriculaVO.getPlanoFinanceiroAluno().getOrdemPlanoDesconto().toString()));
		}

		log.append(realizarCriacaoLogConvenio(planoFinanceiroAlunoVO, matriculaVO.getPlanoFinanceiroAluno()));
		log.append(realizarCriacaoLogPlanoDesconto(planoFinanceiroAlunoVO, matriculaVO.getPlanoFinanceiroAluno()));

		if (!Uteis.retiraTags(log.toString()).trim().isEmpty()) {
			StringBuilder newlog = new StringBuilder("");		
			newlog.append(realizarCriacaoLinhaLog("Data", Uteis.getDataComHora(planoFinanceiroAlunoVO.getData()) , Uteis.getDataComHora(new Date())));
			newlog.append(realizarCriacaoLinhaLog("Responsável", planoFinanceiroAlunoVO.getResponsavel().getNome(), usuarioVO.getNome()));
			newlog.append(log.toString());		
			incluir(newlog.toString(), matriculaPeriodoVO, usuarioVO);
		}
	}

	public String realizarCriacaoLogConvenio(PlanoFinanceiroAlunoVO pf1, PlanoFinanceiroAlunoVO pf2) throws Exception {
		StringBuilder convAnt = new StringBuilder(realizarCriacaoLogConvenioAtual(pf1));
		StringBuilder convAtual = new StringBuilder(realizarCriacaoLogConvenioAtual(pf2));
		StringBuilder plano = new StringBuilder();
		if (!convAnt.toString().equals(convAtual.toString())) {		
			plano.append("<div class=\"row\">");
			plano.append("<div class=\"col-md-6\">");
			plano.append("<span class=\"field control-label tituloCampos\" >Convênio Anterior</span>");
			plano.append(convAnt);
			plano.append("</div>");
			plano.append("<div class=\"col-md-6\">");
			plano.append("<span class=\"field control-label tituloCampos\" >Convênio Atual</span>");
			plano.append(convAtual);
			plano.append("</div>");
			plano.append("</div>");
			return plano.toString();
		}
		return "";		
	}

	public String realizarCriacaoLogConvenioAtual(PlanoFinanceiroAlunoVO pf1) throws Exception {
		StringBuilder convenioAnt = new StringBuilder("<div class=\"col-md-12 ml20\"><ul>");
		for (ConvenioVO convenioVO : pf1.getPlanoFinanceiroConvenioVOs()) {
			convenioAnt.append("<li>");
			if (convenioVO.getDescricao().trim().isEmpty()) {
				convenioVO = getFacadeFactory().getConvenioFacade().consultarPorChavePrimaria(convenioVO.getCodigo(),false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
			}
			convenioAnt.append("<span class=\"field control-label tituloCampos\" >").append(convenioVO.getDescricao()).append("</span>");
			convenioAnt.append("</li>");
		}
		convenioAnt.append("</ul></div>");
		if (!Uteis.retiraTags(convenioAnt.toString()).trim().isEmpty()) {
			return convenioAnt.toString();
		}
		return "";

	}

	public String realizarCriacaoLogPlanoDesconto(PlanoFinanceiroAlunoVO pf1, PlanoFinanceiroAlunoVO pf2) throws Exception {
		StringBuilder planoAnt = new StringBuilder(realizarCriacaoLogPlanoDescontoAtual(pf1));
		StringBuilder planoAtual = new StringBuilder(realizarCriacaoLogPlanoDescontoAtual(pf2));
		if (!planoAnt.toString().equals(planoAtual.toString())) {		
			StringBuilder plano = new StringBuilder();
			plano.append("<div class=\"row\">");
			plano.append("<div class=\"col-md-6\">");
			plano.append("<span class=\"field control-label tituloCampos\">Plano Desconto Anterior</span>");
			plano.append(planoAnt);
			plano.append("</div>");
			plano.append("<div class=\"col-md-6\">");
			plano.append("<span class=\"field control-label tituloCampos\">Plano Desconto Alterado</span>");
			plano.append(planoAtual);
			plano.append("</div>");
			plano.append("</div>");
			return realizarCriacaoLinhaLog("Plano Desconto", planoAnt.toString(), planoAtual.toString());
		}
		return "";
	}

	public String realizarCriacaoLogPlanoDescontoAtual(PlanoFinanceiroAlunoVO pf1) throws Exception {
		StringBuilder planoAnt = new StringBuilder("<div class=\"col-md-12 ml20\"><ul>");		
		for (PlanoDescontoVO planoDescontoVO : pf1.getPlanoDescontoInstitucionalVOs()) {
			planoAnt.append("<li>");
			if (planoDescontoVO.getNome().trim().isEmpty()) {
				planoDescontoVO = getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(planoDescontoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
			}
			planoAnt.append("<span class=\"field control-label tituloCampos\">").append(planoDescontoVO.getNome()).append("</span>");
			planoAnt.append("</li>");
		}
		planoAnt.append("</ul></div>");
		if (!pf1.getPlanoDescontoInstitucionalVOs().isEmpty()) {
			return planoAnt.toString(); 
		}
		return "";
	}

	private String realizarCriacaoLinhaLog(String campoAlterado, String valorAntigo, String valorNovo) {
		StringBuilder newlog = new StringBuilder("<div class=\"row\" > ");
		newlog.append("<div class=\"col-md-6\" >");
		newlog.append("<span class=\"field control-label tituloCampos\">").append(campoAlterado).append(" - Anterior</span>");
		newlog.append("<input type=\"text\" value=\"").append(valorAntigo).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");		
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-6\" >");
		newlog.append("<span class=\"field control-label tituloCampos\">").append(campoAlterado).append(" - Alterado</span>");
		newlog.append("<input type=\"text\" value=\"").append(valorNovo).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");		
		newlog.append("</div>");
		newlog.append("</div>");
		return newlog.toString();
	}

	private String realizarCriacaoLinhaLogAtual(String campoAlterado, String valorAntigo) {
		StringBuilder newlog = new StringBuilder("");
		newlog.append("<tr >");
		newlog.append("<td class=\"rich-table-cell colunaEsquerda bordaBottomCinza\" style=\"width:20%;\">");
		newlog.append(campoAlterado);
		newlog.append("</td>");
		newlog.append("<td class=\"rich-table-cell colunaEsquerda bordaBottomCinza\" style=\"width:78%;\">");
		newlog.append("<div>").append(valorAntigo).append("</div>");
		newlog.append("</td>");
		newlog.append("</tr>");
		return newlog.toString();
	}

	@Override
	public PlanoFinanceiroAlunoLogVO realizarCriacaoLogPlanoFinanceiroAlunoAtual(PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO) throws Exception {
		StringBuilder newlog = new StringBuilder("");
		newlog.append("<div class=\"row\">");
		newlog.append("<div class=\"row\">");
		newlog.append("<div class=\"col-md-2\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Data</span>");
		newlog.append("<input type=\"text\" value=\"").append(Uteis.getDataComHora(planoFinanceiroAlunoVO.getData())).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-4\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Responsável</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getResponsavel().getNome()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos\">").append(UteisJSF.internacionalizar("prt_PlanoFinanceiroCurso_tituloForm")).append("</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getPlanoFinanceiroCursoVO().getDescricao() ).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Condição de Pagamento</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getCondicaoPagamentoPlanoFinanceiroCursoVO().getDescricao()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("</div>");
		
		newlog.append("<div class=\"row\">");
		newlog.append("<div class=\"col-md-4\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Desconto Progressivo Matrícula</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getDescontoProgressivoMatricula().getNome()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-4\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Desconto Progressivo Parcela</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getDescontoProgressivo().getNome()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-4\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Desconto Progressivo Primeira Parcela</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getDescontoProgressivoPrimeiraParcela().getNome()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");		
		newlog.append("</div>");
		
		newlog.append("<div class=\"row\">");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos pn col-md-12\">Desconto Aluno Matrícula</span>");
		newlog.append("<div class=\"pn col-md-4\">");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getTipoDescontoMatricula_Apresentar()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"pn col-md-8\">");
		if(planoFinanceiroAlunoVO.getTipoDescontoMatricula().equals("VA")) {
			newlog.append("<input type=\"text\" value=\"").append(Uteis.formatarDecimalDuasCasas(planoFinanceiroAlunoVO.getValorDescontoMatricula())).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		}else {
			newlog.append("<input type=\"text\" value=\"").append(Uteis.formatarDecimalDuasCasas(planoFinanceiroAlunoVO.getPercDescontoMatricula())).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		}
		newlog.append("</div>");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos  pn col-md-12\">Desconto Aluno Matrícula</span>");
		newlog.append("<div class=\"pn col-md-4\">");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getTipoDescontoParcela_Apresentar()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"pn col-md-8\">");
		if(planoFinanceiroAlunoVO.getTipoDescontoParcela().equals("VA")) {
			newlog.append("<input type=\"text\" value=\"").append(Uteis.formatarDecimalDuasCasas(planoFinanceiroAlunoVO.getValorDescontoParcela())).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		}else {
			newlog.append("<input type=\"text\" value=\"").append(Uteis.formatarDecimalDuasCasas(planoFinanceiroAlunoVO.getPercDescontoParcela())).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		}
		newlog.append("</div>");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Desc. Convênio Valor Cheio</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getOrdemConvenioValorCheio() ? "Sim" : "Não").append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Ordem Desc. Convênio</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getOrdemConvenio().toString()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("</div>");
		
		newlog.append("<div class=\"row\">");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Desc. Aluno Valor Cheio</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getOrdemDescontoAlunoValorCheio() ? "Sim" : "Não").append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Ordem Desc. Aluno</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getOrdemDescontoAluno().toString()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Desc. Progressivo Valor Cheio</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getOrdemDescontoProgressivoValorCheio() ? "Sim" : "Não").append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Ordem Desc. Progressivo</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getOrdemDescontoProgressivo().toString()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		
		newlog.append("</div>");
		
		newlog.append("<div class=\"row\">");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Desc. Instituição Valor Cheio</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getOrdemPlanoDescontoValorCheio() ? "Sim" : "Não").append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("<div class=\"col-md-3\">");
		newlog.append("<span class=\"field control-label tituloCampos\">Ordem Desc. Instituição</span>");
		newlog.append("<input type=\"text\" value=\"").append(planoFinanceiroAlunoVO.getOrdemPlanoDesconto().toString()).append("\" class=\"form-control camposSomenteLeitura\" readonly=\"readonly\" /> ");
		newlog.append("</div>");
		newlog.append("</div>");
		String planoConv = realizarCriacaoLogConvenioAtual(planoFinanceiroAlunoVO);
		if(Uteis.isAtributoPreenchido(planoConv)) {
			newlog.append("<div class=\"row\">");	
			newlog.append(planoConv);
			newlog.append("</div>");
		}
		String planoAtual = realizarCriacaoLogPlanoDescontoAtual(planoFinanceiroAlunoVO);
		if(Uteis.isAtributoPreenchido(planoConv)) {
			newlog.append("<div class=\"row\">");
			newlog.append(planoAtual);
			newlog.append("</div>");
		}
		newlog.append("</div>");

		PlanoFinanceiroAlunoLogVO planoFinanceiroAlunoLogVO = new PlanoFinanceiroAlunoLogVO();
		planoFinanceiroAlunoLogVO.setData(planoFinanceiroAlunoVO.getData());
		planoFinanceiroAlunoLogVO.getResponsavel().setCodigo(planoFinanceiroAlunoVO.getResponsavel().getCodigo());
		planoFinanceiroAlunoLogVO.getResponsavel().setNome(planoFinanceiroAlunoVO.getResponsavel().getNome());
		planoFinanceiroAlunoLogVO.setLog(newlog.toString());
		return planoFinanceiroAlunoLogVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final String log, final MatriculaPeriodoVO matriculaPeriodoVO, final UsuarioVO usuarioVO) {
		getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql = new StringBuilder("INSERT INTO PlanoFinanceiroAlunoLog (data, responsavel, log, matriculaperiodo) VALUES (?,?,?,?) returning codigo");
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setTimestamp(1, Uteis.getDataJDBCTimestamp(new Date()));
				ps.setInt(2, usuarioVO.getCodigo());
				ps.setString(3, log);
				ps.setInt(4, matriculaPeriodoVO.getCodigo());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {

				return null;
			}
		});
	}

	public List<PlanoFinanceiroAlunoLogVO> consultarPorMatriculaPeriodo(Integer matriculaPeriodo) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT PlanoFinanceiroAlunoLog.*, usuario.nome FROM PlanoFinanceiroAlunoLog ");
		sql.append(" Inner join Usuario on Usuario.codigo = PlanoFinanceiroAlunoLog.responsavel ");
		sql.append(" where matriculaPeriodo = ").append(matriculaPeriodo).append(" order by data desc ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PlanoFinanceiroAlunoLogVO> planoFinanceiroAlunoLogVOs = new ArrayList<PlanoFinanceiroAlunoLogVO>(0);
		PlanoFinanceiroAlunoLogVO planoFinanceiroAlunoLogVO = null;
		while (rs.next()) {
			planoFinanceiroAlunoLogVO = new PlanoFinanceiroAlunoLogVO();
			planoFinanceiroAlunoLogVO.setData(rs.getDate("data"));
			planoFinanceiroAlunoLogVO.setLog(rs.getString("log"));
			planoFinanceiroAlunoLogVO.getMatriculaPeriodo().setCodigo(rs.getInt("matriculaPeriodo"));
			planoFinanceiroAlunoLogVO.getResponsavel().setCodigo(rs.getInt("responsavel"));
			planoFinanceiroAlunoLogVO.getResponsavel().setNome(rs.getString("nome"));
			planoFinanceiroAlunoLogVOs.add(planoFinanceiroAlunoLogVO);
		}
		return planoFinanceiroAlunoLogVOs;
	}

}
