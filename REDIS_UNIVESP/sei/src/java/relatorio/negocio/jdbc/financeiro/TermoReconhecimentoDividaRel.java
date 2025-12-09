/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberNegociadoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoBoletoBancario;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaContaReceberRelVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaDebitoRelVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaParcelasRelVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaRelSub1VO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaRelSub2VO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaRelSub3VO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaRelTituloTextoFixoVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaRelVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaRelVOSubReport;
import relatorio.negocio.interfaces.financeiro.TermoReconhecimentoDividaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * 
 * @author Philippe
 */
@Repository
@Scope("singleton")
@Lazy
public class TermoReconhecimentoDividaRel extends SuperRelatorio implements TermoReconhecimentoDividaRelInterfaceFacade {

	private static final long serialVersionUID = -1784317138036968420L;

	private static String idEntidade;

	public static String getIdEntidade() {
		return idEntidade;
	}

	public void setIdEntidade(String aIdEntidade) {
		idEntidade = aIdEntidade;
	}

	public TermoReconhecimentoDividaRel() throws Exception {
		super();
		setIdEntidade("TermoReconhecimentoDividaRel");
	}

	public List<TermoReconhecimentoDividaRelVO> criarObjeto(Integer aluno, NegociacaoContaReceberVO negociacaoContaReceberVO, String observacaoHistorico, Boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, String observacaoComplementar) throws Exception {
		List<TermoReconhecimentoDividaRelVO> tipoDescontoRelVOs = new ArrayList<TermoReconhecimentoDividaRelVO>(0);
		TermoReconhecimentoDividaRelVO obj = new TermoReconhecimentoDividaRelVO();
		if (negociacaoContaReceberVO.getTipoAluno()) {
			obj = consultarResponsavelAluno(Uteis.isAtributoPreenchido(aluno) ? aluno : negociacaoContaReceberVO.getMatriculaAluno().getAluno().getCodigo(), controlarAcesso, usuario);
			montarDadosCredor(negociacaoContaReceberVO.getMatricula(), null, obj, false, controlarAcesso, usuario);
		} else {
			obj.getSub1().add(new TermoReconhecimentoDividaRelSub1VO());
			obj.getSub1().get(0).setTipoPessoaNegociacaoContaReceber(negociacaoContaReceberVO.getTipoPessoa());
			if (negociacaoContaReceberVO.getTipoFornecedor() && Uteis.isAtributoPreenchido(negociacaoContaReceberVO.getFornecedor())) {
				montarDadosFornecedor(negociacaoContaReceberVO, obj, usuario);
				montarDadosCredor(null, negociacaoContaReceberVO.getUnidadeEnsino().getCodigo(), obj, false, controlarAcesso, usuario);
			} else {
				montarDadosPessoa(negociacaoContaReceberVO, obj);
			}
		}
		obj.setAcrescimo(negociacaoContaReceberVO.getAcrescimo());
		obj.setJuro(negociacaoContaReceberVO.getJuro());
		obj.setDesconto(negociacaoContaReceberVO.getDesconto());
		obj.setTipoDesconto(negociacaoContaReceberVO.getTipoDesconto());
		obj.setValorFinal(negociacaoContaReceberVO.getValorTotal());
		obj.setValor(negociacaoContaReceberVO.getValor());
		obj.setEntrada(negociacaoContaReceberVO.getValorEntrada());
		obj.setJustificativa(negociacaoContaReceberVO.getJustificativa());
		obj.setRenegociacaoContaReceber(negociacaoContaReceberVO.getCodigo());
		obj.setObservacaoComplementar(observacaoComplementar);
		if (negociacaoContaReceberVO.getNovaContaReceber().isEmpty()) {
			negociacaoContaReceberVO.setNovaContaReceber(consultarNovaContaReceber(negociacaoContaReceberVO, controlarAcesso, usuario));
		}
		montarDados(obj, negociacaoContaReceberVO.getContaReceberNegociadoVOs(), negociacaoContaReceberVO.getNovaContaReceber(), negociacaoContaReceberVO, observacaoHistorico, configuracaoFinanceiroVO);
		tipoDescontoRelVOs.add(obj);
		return tipoDescontoRelVOs;
	}

	public List<TermoReconhecimentoDividaRelVO> criarObjetoLayout2(Integer aluno, NegociacaoContaReceberVO negociacaoContaReceberVO, String observacaoHistorico, Boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List<TermoReconhecimentoDividaRelVO> tipoDescontoRelVOs = new ArrayList<TermoReconhecimentoDividaRelVO>(0);
		TermoReconhecimentoDividaRelVO obj = new TermoReconhecimentoDividaRelVO();
		if (negociacaoContaReceberVO.getTipoAluno()) {
			obj = consultarResponsavelAluno(aluno, controlarAcesso, usuario);
			montarDadosCredor(negociacaoContaReceberVO.getMatricula(), null, obj, false, controlarAcesso, usuario);
		} else {
			obj.getSub1().add(new TermoReconhecimentoDividaRelSub1VO());
			montarDadosPessoa(negociacaoContaReceberVO, obj);
		}
		montarDados(obj, negociacaoContaReceberVO.getContaReceberNegociadoVOs(), negociacaoContaReceberVO.getNovaContaReceber(), negociacaoContaReceberVO, observacaoHistorico, configuracaoFinanceiroVO);
		tipoDescontoRelVOs.add(obj);
		return tipoDescontoRelVOs;
	}

	private TermoReconhecimentoDividaRelVO consultarResponsavelAluno(Integer aluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pais.nome AS nome, pais.telefoneres AS telResidencial, pais.celular AS celular, pais.cpf AS cpf,");
		sql.append("pais.rg AS rg, pais.setor AS setor, cidade.nome AS cidade, estado.sigla AS estado,");
		sql.append("pais.cep AS cep, pais.endereco AS endereco, pais.orgaoemissor AS orgaoemissor ");
		sql.append("FROM filiacao  ");
		sql.append("INNER JOIN pessoa ON filiacao.aluno = pessoa.codigo ");
		sql.append("INNER JOIN pessoa as pais ON filiacao.pais = pais.codigo ");
		sql.append("LEFT JOIN cidade ON pais.cidade = cidade.codigo ");
		sql.append("LEFT JOIN estado ON cidade.estado = estado.codigo ");
		sql.append("WHERE filiacao.responsavelfinanceiro = true AND filiacao.aluno = ").append(aluno);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return (montarDadosResponsavelAluno(tabelaResultado));
		}
		return consultarAluno(aluno, controlarAcesso, usuario);
	}

	private TermoReconhecimentoDividaRelVO consultarAluno(Integer aluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pessoa.nome AS nome, telefoneres AS telResidencial, celular AS celular, cpf AS cpf,");
		sql.append("rg AS rg, setor AS setor, cidade.nome AS cidade, estado.sigla AS estado,");
		sql.append("pessoa.cep AS cep, endereco AS endereco, orgaoemissor AS orgaoemissor ");
		sql.append("FROM pessoa  ");
		sql.append("LEFT JOIN cidade ON pessoa.cidade = cidade.codigo ");
		sql.append("LEFT JOIN estado ON cidade.estado = estado.codigo ");
		sql.append("WHERE pessoa.codigo = ").append(aluno);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return (montarDadosResponsavelAluno(tabelaResultado));
		}
		TermoReconhecimentoDividaRelVO termoReconhecimentoDividaRelVO = new TermoReconhecimentoDividaRelVO();
		termoReconhecimentoDividaRelVO.getSub1().add(new TermoReconhecimentoDividaRelSub1VO());
		termoReconhecimentoDividaRelVO.getSubReport().add(new TermoReconhecimentoDividaRelVOSubReport());
		return termoReconhecimentoDividaRelVO;
	}

	public List<ContaReceberVO> consultarNovaContaReceber(NegociacaoContaReceberVO negociacaoContaReceberVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT contareceber.codigo, dataVencimento, tipoBoleto, parcela, valor, nrdocumento, centroreceita.descricao as \"centroreceita.descricao\", tipoorigem, juro, multa, acrescimo, situacao, valorrecebido ");
		sql.append("FROM contareceber ");
		sql.append("left JOIN centroreceita ON centroreceita.codigo = contareceber.centroreceita ");
		sql.append(" WHERE contareceber.tipoorigem = 'NCR' ");
		sql.append(" AND contareceber.codorigem = '").append(negociacaoContaReceberVO.getCodigo()).append("'");
		sql.append(" ORDER BY contareceber.dataVencimento");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ContaReceberVO> listaObjs = new ArrayList<ContaReceberVO>(0);
		while (tabelaResultado.next()) {
			ContaReceberVO obj = new ContaReceberVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDataVencimento(tabelaResultado.getDate("dataVencimento"));
			obj.setTipoBoleto(tabelaResultado.getString("tipoBoleto"));
			obj.setParcela(tabelaResultado.getString("parcela"));
			obj.setValor(tabelaResultado.getDouble("valor"));
			obj.setNrDocumento(tabelaResultado.getString("nrdocumento"));
			obj.setTipoOrigem(tabelaResultado.getString("tipoOrigem"));
			obj.setJuro(tabelaResultado.getDouble("juro"));
			obj.setMulta(tabelaResultado.getDouble("multa"));
			obj.setAcrescimo(tabelaResultado.getDouble("acrescimo"));
			obj.setValorRecebido(tabelaResultado.getDouble("valorrecebido"));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			obj.getCentroReceita().setDescricao(tabelaResultado.getString("centroreceita.descricao"));
			listaObjs.add(obj);
		}
		return listaObjs;
	}

	private void montarDadosCredor(String matricula, Integer unidadeEnsino, TermoReconhecimentoDividaRelVO termoReconhecimentoDividaRelVO, Boolean layout2, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT unidadeEnsino.mantenedora AS mantenedora, unidadeEnsino.razaosocial AS nome, unidadeEnsino.cnpj AS cnpj, unidadeEnsino.endereco AS endereco, unidadeEnsino.setor AS setor,");
		sql.append("unidadeEnsino.telcomercial1 AS telefone, cidade.nome AS cidade, estado.sigla AS estado,");
		sql.append("unidadeEnsino.cep AS cep ");
		sql.append("FROM unidadeEnsino  ");
		sql.append("LEFT JOIN matricula ON matricula.unidadeEnsino = unidadeEnsino.codigo ");
		sql.append("LEFT JOIN cidade ON unidadeEnsino.cidade = cidade.codigo ");
		sql.append("LEFT JOIN estado ON cidade.estado = estado.codigo  ");
		if (Uteis.isAtributoPreenchido(matricula)) {
			sql.append(" WHERE matricula.matricula = '").append(matricula).append("'");
		} else if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" WHERE unidadeEnsino.codigo = ").append(unidadeEnsino);
		} else {
			sql.append(" WHERE unidadeEnsino.matriz ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			montarDadosEmpresa(tabelaResultado, termoReconhecimentoDividaRelVO, layout2);
		}
	}

	private TermoReconhecimentoDividaRelVO montarDadosResponsavelAluno(SqlRowSet dadosSQL) {
		TermoReconhecimentoDividaRelVO obj = new TermoReconhecimentoDividaRelVO();
		obj.getSub1().add(new TermoReconhecimentoDividaRelSub1VO());
		obj.getSub1().get(0).setNomeResponsavel(dadosSQL.getString("nome"));
		obj.getSub1().get(0).setTelResResponsavel(dadosSQL.getString("telResidencial"));
		obj.getSub1().get(0).setCelularResponsavel(dadosSQL.getString("celular"));
		obj.getSub1().get(0).setCpfResponsavel(dadosSQL.getString("cpf"));
		obj.getSub1().get(0).setRgResponsavel(dadosSQL.getString("rg"));
		obj.getSub1().get(0).setBairroResponsavel(dadosSQL.getString("setor"));
		obj.getSub1().get(0).setCidadeResponsavel(dadosSQL.getString("cidade"));
		obj.getSub1().get(0).setUfResponsavel(dadosSQL.getString("estado"));
		obj.getSub1().get(0).setCepResponsavel(dadosSQL.getString("cep"));
		obj.getSub1().get(0).setEnderecoResponsavel(dadosSQL.getString("endereco"));
		obj.getSub1().get(0).setOrgaoExpedidorResponsavel(dadosSQL.getString("orgaoemissor"));
		obj.getSubReport().add(new TermoReconhecimentoDividaRelVOSubReport());
		obj.getSubReport().get(0).setNomeResponsavel(dadosSQL.getString("nome"));
		return obj;
	}

	private void montarDadosEmpresa(SqlRowSet dadosSQL, TermoReconhecimentoDividaRelVO termoReconhecimentoDividaRelVO, Boolean layout2) {
		termoReconhecimentoDividaRelVO.getSub1().get(0).setNomeEmpresa(dadosSQL.getString("nome"));
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCnpjEmpresa(dadosSQL.getString("cnpj"));
		termoReconhecimentoDividaRelVO.getSub1().get(0).setEnderecoEmpresa(dadosSQL.getString("endereco"));
		termoReconhecimentoDividaRelVO.getSub1().get(0).setBairroEmpresa(dadosSQL.getString("setor"));
		termoReconhecimentoDividaRelVO.getSub1().get(0).setTelEmpresa(dadosSQL.getString("telefone"));
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCidadeEmpresa(dadosSQL.getString("cidade"));
		termoReconhecimentoDividaRelVO.getSub1().get(0).setUfEmpresa(dadosSQL.getString("estado"));
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCepEmpresa(dadosSQL.getString("cep"));
		if (layout2) {
			TermoReconhecimentoDividaRelTituloTextoFixoVO texto1 = new TermoReconhecimentoDividaRelTituloTextoFixoVO();
			texto1.setTitulo("III - DOS JUROS, MORA E DESPESA JUDICIAL");
			texto1.setTexto("        O devedor esta ciente que na falta de pagamento das parcelas citadas na clausula II, o valor devido será acrescido de correção monetária de acordo com a variação acumulada do INPC/IBGE, juros de mora de 1% (um por cento) ao mês 'pro-rata tempore', e multa de 2% (dois por cento), sobre o valor total do débito.\n    § 1º - O pagamento deverá ser feito diretamente às Agências de Cobrança indicadas pela Contratada, com\n acréscimo de 20% (vinte por cento) sobre o montante corrigido nos termos do presente Contrato, a título de honorários, sem prejuízo da inscrição do nome do(a) Aluno(a) Contratante em bancos de dados cadastrais ( Serasa, DPC, SPC etc.), cobrança judicial e outras cominações previstas em lei. No caso de necessidade do Contratante buscar o Poder Judiciário, também lhe será garantido o direito de recebimento de honorários sucumbências.");
			TermoReconhecimentoDividaRelTituloTextoFixoVO texto2 = new TermoReconhecimentoDividaRelTituloTextoFixoVO();
			texto2.setTitulo("IV - RECONHECIMENTO DA DÍVIDA");
			texto2.setTexto("        Pelo presente instrumento particular o devedor reconhece e confessa dever à empresa credora a importância citada na cláusula II, bem como esta ciente da atualização e especificada na cláusula III, comprometendo-se a liquidar o débito. Fica o co-obrigado responsável solidariamente sobre o dívida podendo responder também judicialmente.");
			TermoReconhecimentoDividaRelTituloTextoFixoVO texto3 = new TermoReconhecimentoDividaRelTituloTextoFixoVO();
			texto3.setTitulo("V - QUANTO A DÉBITOS ANTERIORES");
			texto3.setTexto("        A negociação dos débitos constantes neste instrumento não quita débitos anteriores, ressalvada a possibilidade de credor ingressar em juízo em busca da recuperação dos créditos não solvidos.");
			TermoReconhecimentoDividaRelTituloTextoFixoVO texto4 = new TermoReconhecimentoDividaRelTituloTextoFixoVO();
			texto4.setTitulo("VI - CONSIDERAÇÕES FINAIS");
			texto4.setTexto("        E por estarem assim firmados, justa e acordados as partes, assinam o presente termo de reconhecimento de dívida em três vias de igual teor e forma, na presença de uma testemunha.");
			termoReconhecimentoDividaRelVO.getSubReport().get(0).getTermoReconhecimentoDividaRelTituloTextoFixoVOs().add(texto1);
			termoReconhecimentoDividaRelVO.getSubReport().get(0).getTermoReconhecimentoDividaRelTituloTextoFixoVOs().add(texto2);
			termoReconhecimentoDividaRelVO.getSubReport().get(0).getTermoReconhecimentoDividaRelTituloTextoFixoVOs().add(texto3);
			termoReconhecimentoDividaRelVO.getSubReport().get(0).getTermoReconhecimentoDividaRelTituloTextoFixoVOs().add(texto4);
		} else {
			TermoReconhecimentoDividaRelTituloTextoFixoVO texto1 = new TermoReconhecimentoDividaRelTituloTextoFixoVO();
			texto1.setTitulo("V - LOCAL DOS PAGAMENTOS");
			texto1.setTexto("          A liquidação do(s) cheque(s) originário(s) do presente termo, deverá(ão) obedecer ao sistema de compensação bancária, porém, quando a forma de pagamento for por meio de Nota(s) Promissória(s) a liquidação deverá ocorrer nas instalações da " + dadosSQL.getString("nome") + " , no endereço acima mencionado.");
			TermoReconhecimentoDividaRelTituloTextoFixoVO texto2 = new TermoReconhecimentoDividaRelTituloTextoFixoVO();
			texto2.setTitulo("VI - ATRASO NO PAGAMENTO");
			texto2.setTexto("        O devedor reconhece, ainda, caso ocorrer atraso no pagamento da(s) parcela(s) na data aprazada, ensejará o vencimento antecipado das demais, caso exista, acrescendo-se ao valor principal de cada parcela multa de 2% e juros de mora de 1% a.m., e atualizações monetárias e 10% sobre o valor total para despesas judiciais, bem como o imediato ajuizamento da competente medida judicial cabível.");
			TermoReconhecimentoDividaRelTituloTextoFixoVO texto3 = new TermoReconhecimentoDividaRelTituloTextoFixoVO();
			texto3.setTitulo("VII - QUANTO AO TERMO FIRMADO");
			texto3.setTexto("        Este termo de reconhecimento de dívidaconsitui novação de dívida nos termo do Art. 360, inciso I do novo código civil, tornando-se título executivo nos moldes do Art. 784, III do CPC.");
			TermoReconhecimentoDividaRelTituloTextoFixoVO texto4 = new TermoReconhecimentoDividaRelTituloTextoFixoVO();
			texto4.setTitulo("VIII - TERMO FIRMADO POR PROCURAÇÃO");
			texto4.setTexto("        Caso o presente termo seja firmado por procurador, deverá este apresentar instrumento público de procuração e disponibilizar cópia autenticada do mesmo, passado este (instrumento procuratório) a fazer parte do presente termo de reconhecimento de dívida.");
			TermoReconhecimentoDividaRelTituloTextoFixoVO texto5 = new TermoReconhecimentoDividaRelTituloTextoFixoVO();
			texto5.setTitulo("IX - QUANTO A DÉBITOS ANTERIORES");
			texto5.setTexto("        A negociação dos débitos constantes neste instrumento não quita débitos anteriores ressalvada a possibilidade de credor ingressar em juízo em busca da recuperação dos créditos não solvidos.");
			TermoReconhecimentoDividaRelTituloTextoFixoVO texto6 = new TermoReconhecimentoDividaRelTituloTextoFixoVO();
			texto6.setTitulo("X - CONSIDERAÇÕES FINAIS");
			texto6.setTexto("        E por estarem assim firmados, justa e acordados as partes, assinam o presente termo de reconhecimento de dívida em três vias de igual teor e forma, na presença de uma testemunha.");
			termoReconhecimentoDividaRelVO.getSubReport().get(0).getTermoReconhecimentoDividaRelTituloTextoFixoVOs().add(texto1);
			termoReconhecimentoDividaRelVO.getSubReport().get(0).getTermoReconhecimentoDividaRelTituloTextoFixoVOs().add(texto2);
			termoReconhecimentoDividaRelVO.getSubReport().get(0).getTermoReconhecimentoDividaRelTituloTextoFixoVOs().add(texto3);
			termoReconhecimentoDividaRelVO.getSubReport().get(0).getTermoReconhecimentoDividaRelTituloTextoFixoVOs().add(texto4);
			termoReconhecimentoDividaRelVO.getSubReport().get(0).getTermoReconhecimentoDividaRelTituloTextoFixoVOs().add(texto5);
			termoReconhecimentoDividaRelVO.getSubReport().get(0).getTermoReconhecimentoDividaRelTituloTextoFixoVOs().add(texto6);
		}
		termoReconhecimentoDividaRelVO.getSubReport().get(0).setCidadeEmpresa(dadosSQL.getString("cidade"));
		termoReconhecimentoDividaRelVO.getSubReport().get(0).setUfEmpresa(dadosSQL.getString("estado"));
		termoReconhecimentoDividaRelVO.getSubReport().get(0).setMantenedora(dadosSQL.getString("mantenedora"));
	}

	private void montarDados(TermoReconhecimentoDividaRelVO termoReconhecimentoDividaRelVO, List<ContaReceberNegociadoVO> listaContaReceberNegociadoVO, List<ContaReceberVO> listaNovaContaReceber, NegociacaoContaReceberVO negociacaoContaReceberVO, String observacaoHistorico, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		Double valorTotalDebito = 0.0;
		Double valorTotalNovasParcelas = 0.0;
		termoReconhecimentoDividaRelVO.getSub2().add(new TermoReconhecimentoDividaRelSub2VO());
		termoReconhecimentoDividaRelVO.getSub3().add(new TermoReconhecimentoDividaRelSub3VO());
		Integer totalParcela = 0;
		for (ContaReceberNegociadoVO contaReceberNegociadoVO : listaContaReceberNegociadoVO) {
			TermoReconhecimentoDividaDebitoRelVO termoReconhecimentoDividaDebitoRelVO = new TermoReconhecimentoDividaDebitoRelVO();
			termoReconhecimentoDividaDebitoRelVO.setDataVencimento(contaReceberNegociadoVO.getContaReceber().getDataVencimento_Apresentar());
			termoReconhecimentoDividaDebitoRelVO.setServico(TipoBoletoBancario.getDescricao(contaReceberNegociadoVO.getContaReceber().getTipoBoleto()).toString());
			if(contaReceberNegociadoVO.getContaReceber().getCentroReceita().getDescricao().trim().isEmpty()){
				termoReconhecimentoDividaDebitoRelVO.setCentroReceita(contaReceberNegociadoVO.getContaReceber().getTipoOrigem_apresentar());
			}else{
				termoReconhecimentoDividaDebitoRelVO.setCentroReceita(contaReceberNegociadoVO.getContaReceber().getCentroReceita().getDescricao());
			}	
			termoReconhecimentoDividaDebitoRelVO.setParcela(contaReceberNegociadoVO.getContaReceber().getParcelaPorConfiguracaoFinanceira_Apresentar());
//			termoReconhecimentoDividaDebitoRelVO.setValorFinal(contaReceberNegociadoVO.getContaReceber().getCalcularValorFinal(configuracaoFinanceiroVO));
			termoReconhecimentoDividaDebitoRelVO.setValorFinal(contaReceberNegociadoVO.getValor());
			termoReconhecimentoDividaDebitoRelVO.setJuro(contaReceberNegociadoVO.getContaReceber().getJuro());
			termoReconhecimentoDividaDebitoRelVO.setMulta(contaReceberNegociadoVO.getContaReceber().getMulta());
			termoReconhecimentoDividaDebitoRelVO.setAcrescimo(contaReceberNegociadoVO.getContaReceber().getAcrescimo());
			termoReconhecimentoDividaDebitoRelVO.setValor(contaReceberNegociadoVO.getContaReceber().getValor());
			termoReconhecimentoDividaDebitoRelVO.setNrDocumento(contaReceberNegociadoVO.getContaReceber().getNrDocumento());
			termoReconhecimentoDividaDebitoRelVO.setValorDesconto(contaReceberNegociadoVO.getContaReceber().getValorDescontoCalculado());
			termoReconhecimentoDividaDebitoRelVO.setValorDescontoConvenio(contaReceberNegociadoVO.getContaReceber().getValorDescontoConvenio());
			if(contaReceberNegociadoVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue() >= 0.0){
				termoReconhecimentoDividaDebitoRelVO.setAcrescimo(termoReconhecimentoDividaDebitoRelVO.getAcrescimo() + contaReceberNegociadoVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue());
			}else if(contaReceberNegociadoVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue() < 0.0){
				termoReconhecimentoDividaDebitoRelVO.setValorDesconto(termoReconhecimentoDividaDebitoRelVO.getValorDesconto() - contaReceberNegociadoVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue());
			}
			try {
				MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(contaReceberNegociadoVO.getContaReceber().getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null, null);
				if (matriculaPeriodoVO.getApresentarAnoSemestre().trim().length() == 1) {
					termoReconhecimentoDividaDebitoRelVO.setPeriodo("--");
				} else {
					termoReconhecimentoDividaDebitoRelVO.setPeriodo(matriculaPeriodoVO.getApresentarAnoSemestre().replace(" ", ""));
				}
			} catch (Exception e) {
				termoReconhecimentoDividaDebitoRelVO.setPeriodo("--");
			}
			termoReconhecimentoDividaDebitoRelVO.setTipo(contaReceberNegociadoVO.getContaReceber().getTipoOrigem_apresentar());
			termoReconhecimentoDividaDebitoRelVO.setValorIndiceReajustePorAtraso(contaReceberNegociadoVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue());
			valorTotalDebito += contaReceberNegociadoVO.getContaReceber().getValor();
			termoReconhecimentoDividaRelVO.getSub2().get(0).getDebito().add(termoReconhecimentoDividaDebitoRelVO);
			totalParcela++;
		}
		termoReconhecimentoDividaRelVO.getSub2().get(0).setHistorico(observacaoHistorico);
		termoReconhecimentoDividaRelVO.getSub2().get(0).setTotalValorDebito(valorTotalDebito);
		termoReconhecimentoDividaRelVO.getSub2().get(0).setTotalParcelasDebito(totalParcela);
		if (configuracaoFinanceiroVO.getIndiceReajustePadraoContasPorAtrasoVO().getCodigo() != 0) {
		String descricaoIndiceReajuste = getFacadeFactory().getIndiceReajusteFacade().consultarPorChavePrimaria(configuracaoFinanceiroVO.getIndiceReajustePadraoContasPorAtrasoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, null).getDescricao();
		termoReconhecimentoDividaRelVO.getSub2().get(0).setNomeUtilizarInidiceReajustePorAtraso(descricaoIndiceReajuste);
		}

		for (ContaReceberVO contaReceberVO : listaNovaContaReceber) {
			TermoReconhecimentoDividaParcelasRelVO termoReconhecimentoParcelasRelVO = new TermoReconhecimentoDividaParcelasRelVO();
			termoReconhecimentoParcelasRelVO.setDataVencimento(contaReceberVO.getDataVencimento_Apresentar());
			termoReconhecimentoParcelasRelVO.setServico(contaReceberVO.getTipoOrigem_apresentar());
			termoReconhecimentoParcelasRelVO.setCentroReceita(contaReceberVO.getCentroReceita().getDescricao());
			termoReconhecimentoParcelasRelVO.setParcela(contaReceberVO.getParcela());
			termoReconhecimentoParcelasRelVO.setValor(contaReceberVO.getValor());
			termoReconhecimentoParcelasRelVO.setNrDocumento(contaReceberVO.getNrDocumento());
			valorTotalNovasParcelas += contaReceberVO.getValor();
			termoReconhecimentoDividaRelVO.getSub3().get(0).getParcelasNovaContaReceber().add(termoReconhecimentoParcelasRelVO);
		}
		termoReconhecimentoDividaRelVO.getSub3().get(0).setTotalValorNovaContaReceber(valorTotalNovasParcelas);
		termoReconhecimentoDividaRelVO.getSub3().get(0).setTotalParcelasNovaContaReceber(listaNovaContaReceber.size());

		termoReconhecimentoDividaRelVO.getSub1().get(0).setCursoAluno(negociacaoContaReceberVO.getMatriculaAluno().getCurso().getNome());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setNomeAluno(negociacaoContaReceberVO.getMatriculaAluno().getAluno().getNome());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setMatriculaAluno(negociacaoContaReceberVO.getMatricula());
		termoReconhecimentoDividaRelVO.getSubReport().get(0).setNomeAtendente(negociacaoContaReceberVO.getResponsavel().getPessoa().getNome());
		termoReconhecimentoDividaRelVO.getSubReport().get(0).setDataAcordoExtenso(new Date());
	}
	
	public void validarDadosContaReceber(ContaReceberVO obj, String tipoPessoa) throws Exception {
		if (tipoPessoa.equals("ALUNO")) {
			if (obj.getMatriculaAluno().getMatricula().equals("")) {
				throw new Exception("O campo MATRÍCULA deve ser informado.");
			}
		} else {
			if (obj.getResponsavelFinanceiro().getCodigo().equals(0)) {
				throw new Exception("O campo RESPONSÁVEL FINANCEIRO deve ser informado.");
			}
		}
	}

	public List<TermoReconhecimentoDividaContaReceberRelVO> criarObjetoTermoReconhecimentoDividaContaReceber(ContaReceberVO contaReceberVO, String tipoContaReceber, UsuarioVO usuarioVO ,FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO ) throws Exception {
		String tipoPessoa = realizarVerificacaoTipoPessoaContaReceber(contaReceberVO);
		validarDadosContaReceber(contaReceberVO, tipoPessoa);
		List<TermoReconhecimentoDividaContaReceberRelVO> listaTermoReconhecimento = new ArrayList<TermoReconhecimentoDividaContaReceberRelVO>(0);
		TermoReconhecimentoDividaContaReceberRelVO obj = executarConsultaParametrizadaContaReceber(tipoPessoa, tipoContaReceber, contaReceberVO.getMatriculaAluno().getMatricula(), contaReceberVO.getResponsavelFinanceiro().getCodigo(), usuarioVO,filtroRelatorioFinanceiroVO);
		if (!obj.getListaContaReceberVOs().isEmpty()) {
			listaTermoReconhecimento.add(obj);
		}
		return listaTermoReconhecimento;
	}
	
	public TermoReconhecimentoDividaContaReceberRelVO executarConsultaParametrizadaContaReceber(String tipoPessoa, String tipoContaReceber, String matricula, Integer responsavelFinanceiro, UsuarioVO usuarioVO ,FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct pessoa.nome AS nomeAluno, curso.nome AS cursoAluno, matricula.matricula, pessoa.telefoneRes, pessoa.celular, pessoa.cpf, pessoa.rg, pessoa.setor,  ");
		sb.append(" cidade.nome AS cidadeAluno, estado.nome AS estadoAluno, pessoa.cep, pessoa.endereco, pessoa.orgaoemissor, ");
		sb.append(" unidadeensino.nome AS nomeEmpresa, unidadeensino.cnpj AS cnpjEmpresa, unidadeensino.endereco AS enderecoEmpresa,  ");
		sb.append(" unidadeensino.setor AS setorEmpresa, unidadeensino.telcomercial1 AS telefoneEmpresa, estadoEmpresa.nome AS estadoEmpresa, cidadeEmpresa.nome AS cidadeEmpresa, unidadeEnsino.cep AS cepEmpresa, ");
		sb.append(" unidadeensino.mantenedora AS mantenedoraEmpresa,  ");
		
		sb.append(" responsavelFinanceiro.nome AS nomeResponsavel, responsavelFinanceiro.telefoneRes AS telefoneResponsavel, responsavelFinanceiro.celular AS celularResponsavel, ");
		sb.append(" responsavelFinanceiro.cpf AS cpfResponsavel, responsavelFinanceiro.rg AS rgResponsavel, responsavelFinanceiro.setor AS setorResponsavel, ");
		sb.append(" cidade.nome AS cidadeResponsavel, estado.nome AS estadoResponsavel, responsavelFinanceiro.cep AS cepResponsavel, responsavelFinanceiro.endereco AS enderecoResponsavel, responsavelFinanceiro.orgaoemissor AS orgaoEmissorResponsavel ");
		
		sb.append(" from contareceber ");
		
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" left join pessoa responsavelFinanceiro on responsavelFinanceiro.codigo = contareceber.responsavelFinanceiro ");
		
		sb.append(" inner join unidadeensino on unidadeensino.codigo = contareceber.unidadeensino ");
		sb.append(" left join cidade on cidade.codigo = pessoa.cidade ");
		sb.append(" left join cidade cidadeResponsavel on cidadeResponsavel.codigo = responsavelFinanceiro.cidade ");
		sb.append(" left join estado on estado.codigo = cidade.estado ");
		sb.append(" left join cidade cidadeEmpresa on cidadeEmpresa.codigo = unidadeEnsino.cidade ");
		sb.append(" left join estado estadoEmpresa on estadoEmpresa.codigo = cidadeEmpresa.estado ");
		sb.append(" WHERE contaReceber.situacao = 'AR' ");
		if (tipoPessoa.equals("ALUNO")) {
			sb.append(" AND matriculaaluno = '").append(matricula).append("' ");
		} else {
			sb.append(" AND responsavelFinanceiro.codigo = ").append(responsavelFinanceiro);
		}
		sb.append(" AND ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contareceber"));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return new TermoReconhecimentoDividaContaReceberRelVO();
		}
		return montarDadosTermoReconhecimentoContaReceber(tabelaResultado, tipoPessoa, tipoContaReceber, matricula, responsavelFinanceiro,filtroRelatorioFinanceiroVO, usuarioVO);
	}
	
	public TermoReconhecimentoDividaContaReceberRelVO montarDadosTermoReconhecimentoContaReceber(SqlRowSet dadosSQL, String tipoPessoa, String tipoContaReceber, String matricula, Integer responsavelFinanceiro,FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO,UsuarioVO usuarioVO) {
		TermoReconhecimentoDividaContaReceberRelVO obj = new TermoReconhecimentoDividaContaReceberRelVO();
		if (tipoPessoa.equals("ALUNO")) {
			obj.setNomeResponsavel(dadosSQL.getString("nomeAluno"));
			
			obj.setTelResResponsavel(dadosSQL.getString("telefoneRes"));
			obj.setCelularResponsavel(dadosSQL.getString("celular"));
			obj.setCpfResponsavel(dadosSQL.getString("cpf"));
			obj.setRgResponsavel(dadosSQL.getString("rg"));
			obj.setBairroResponsavel(dadosSQL.getString("setor"));
			obj.setCidadeResponsavel(dadosSQL.getString("cidadeAluno"));
			obj.setUfResponsavel(dadosSQL.getString("estadoAluno"));
			obj.setCepResponsavel(dadosSQL.getString("cep"));
			obj.setEnderecoResponsavel(dadosSQL.getString("endereco"));
			obj.setOrgaoExpedidorResponsavel(dadosSQL.getString("orgaoEmissor"));
		} else {
			obj.setNomeResponsavel(dadosSQL.getString("nomeResponsavel"));
			
			obj.setTelResResponsavel(dadosSQL.getString("telefoneResponsavel"));
			obj.setCelularResponsavel(dadosSQL.getString("celularResponsavel"));
			obj.setCpfResponsavel(dadosSQL.getString("cpfResponsavel"));
			obj.setRgResponsavel(dadosSQL.getString("rgResponsavel"));
			obj.setBairroResponsavel(dadosSQL.getString("setorResponsavel"));
			obj.setCidadeResponsavel(dadosSQL.getString("cidadeResponsavel"));
			obj.setUfResponsavel(dadosSQL.getString("estadoResponsavel"));
			obj.setCepResponsavel(dadosSQL.getString("cepResponsavel"));
			obj.setEnderecoResponsavel(dadosSQL.getString("enderecoResponsavel"));
			obj.setOrgaoExpedidorResponsavel(dadosSQL.getString("orgaoEmissorResponsavel"));
		}
		obj.setNomeAluno(dadosSQL.getString("nomeAluno"));
		obj.setMatriculaAluno(dadosSQL.getString("matricula"));
		obj.setCursoAluno(dadosSQL.getString("cursoAluno"));
		
		
//		DADOS DA EMPRESA
		obj.setNomeEmpresa(dadosSQL.getString("nomeEmpresa"));
		obj.setCnpjEmpresa(dadosSQL.getString("cnpjEmpresa"));
		obj.setEnderecoEmpresa(dadosSQL.getString("enderecoEmpresa"));
		obj.setBairroEmpresa(dadosSQL.getString("setorEmpresa"));
		obj.setTelEmpresa(dadosSQL.getString("telefoneEmpresa"));
		obj.setCidadeEmpresa(dadosSQL.getString("cidadeEmpresa"));
		obj.setUfEmpresa(dadosSQL.getString("estadoEmpresa"));
		obj.setCepEmpresa(dadosSQL.getString("cepEmpresa"));
		obj.setMantenedoraEmpresa(dadosSQL.getString("mantenedoraEmpresa"));
		obj.setDataAcordoExtenso(new Date());
		
		obj.setTextoTitulo1("IV - LOCAL DOS PAGAMENTOS");
		obj.setTexto1("          A liquidação do(s) cheque(s) originário(s) do presente termo, deverá(ão) obedecer ao sistema de compensação bancária, porém, quando a forma de pagamento for por meio de Nota(s) Promissória(s) a liquidação deverá ocorrer nas instalações da " + dadosSQL.getString("nomeEmpresa") + " , no endereço acima mencionado.");
		
		obj.setTextoTitulo2("V - ATRASO NO PAGAMENTO");
		obj.setTexto2("        O devedor reconhece, ainda, caso ocorrer atraso no pagamento da(s) parcela(s) na data aprazada, ensejará o vencimento antecipado das demais, caso exista, acrescendo-se ao valor principal de cada parcela multa de 2% e juros de mora de 1% a.m., e atualizações monetárias e 10% sobre o valor total para despesas judiciais, bem como o imediato ajuizamento da competente medida judicial cabível.");
		
		obj.setTextoTitulo3("VI - QUANTO AO TERMO FIRMADO");
		obj.setTexto3("        Este termo de reconhecimento de dívidaconsitui novação de dívida nos termo do Art. 360, inciso I do novo código civil, tornando-se título executivo nos moldes do Art. 784, III do CPC.");
		
		obj.setTextoTitulo4("VII - TERMO FIRMADO POR PROCURAÇÃO");
		obj.setTexto4("        Caso o presente termo seja firmado por procurador, deverá este apresentar instrumento público de procuração e disponibilizar cópia autenticada do mesmo, passado este (instrumento procuratório) a fazer parte do presente termo de reconhecimento de dívida.");
		
		obj.setTextoTitulo5("VIII - QUANTO A DÉBITOS ANTERIORES");
		obj.setTexto5("        A negociação dos débitos constantes neste instrumento não quita débitos anteriores ressalvada a possibilidade de credor ingressar em juízo em busca da recuperação dos créditos não solvidos.");
		
		obj.setTextoTitulo6("IX - CONSIDERAÇÕES FINAIS");
		obj.setTexto6("        E por estarem assim firmados, justa e acordados as partes, assinam o presente termo de reconhecimento de dívida em três vias de igual teor e forma, na presença de uma testemunha.");
		
		
		obj.setListaContaReceberVOs(consultarContaReceber(tipoPessoa, tipoContaReceber, matricula, responsavelFinanceiro,filtroRelatorioFinanceiroVO, usuarioVO ));
		
		return obj;
	}

	public String realizarVerificacaoTipoPessoaContaReceber(ContaReceberVO obj) {
		if (obj.getTipoPessoa().equals("AL")) {
			return "ALUNO";
		}
		return "RESPONSAVEL_FINANCEIRO";
	}

	public List<ContaReceberVO> consultarContaReceber(String tipoPessoa, String tipoContaReceber, String matricula , Integer responsavelFinanceiro,FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO ,UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select contareceber.codigo, pessoa.nome, matriculaaluno, parcela, datavencimento, nossonumero, nrdocumento,valor,juro, multa, acrescimo from contareceber ");
		if (tipoPessoa.equals("ALUNO")) {
			sb.append(" inner join pessoa on pessoa.codigo = contareceber.pessoa ");
		} else {
			sb.append(" inner join pessoa on pessoa.codigo = contareceber.responsavelFinanceiro ");
		}
		sb.append(" where situacao = 'AR' ");
		if (tipoPessoa.equals("ALUNO")) {
			sb.append(" and matriculaaluno = '").append(matricula).append("' ");
		} else {
			sb.append(" and pessoa.codigo = ").append(responsavelFinanceiro);
		}
		
		sb.append(" AND ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contareceber"));
		
		if (tipoContaReceber.equals("VENCIDAS")) {
			sb.append(" and dataVencimento < current_date ");
		} else if (tipoContaReceber.equals("A_VENCER")) {
			sb.append(" and dataVencimento >= current_date ");
		}
		sb.append(" order by dataVencimento");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsultaContaReceber(tabelaResultado, usuarioVO);
	}

	public List<ContaReceberVO> montarDadosConsultaContaReceber(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) {
		List<ContaReceberVO> listaContaReceber = new ArrayList<ContaReceberVO>(0);
		while (tabelaResultado.next()) {
			ContaReceberVO obj = new ContaReceberVO();
			montarDadosContaReceber(tabelaResultado, obj, usuarioVO);
			listaContaReceber.add(obj);
		}
		return listaContaReceber;
	}

	public void montarDadosContaReceber(SqlRowSet dadosSQL, ContaReceberVO obj, UsuarioVO usuarioVO) {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getPessoa().setNome(dadosSQL.getString("nome"));
		obj.getMatriculaAluno().setMatricula(dadosSQL.getString("matriculaAluno"));
		obj.setParcela(dadosSQL.getString("parcela"));
		obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		obj.setNossoNumero(dadosSQL.getString("nossoNumero"));
		obj.setNrDocumento(dadosSQL.getString("nrDocumento"));
		obj.setValor(dadosSQL.getDouble("valor"));
		obj.setJuro(dadosSQL.getDouble("juro"));
		obj.setMulta(dadosSQL.getDouble("multa"));
		obj.setAcrescimo(dadosSQL.getDouble("acrescimo"));
	}

	public String designIReportRelatorio() {
		return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
	}

	public String designIReportRelatorioContaReceber() {
		return (caminhoBaseRelatorio() + "TermoReconhecimentoDividaContaReceberRel" + ".jrxml");
	}
	
	public String designIReportRelatorioLayout2() {
		return (caminhoBaseRelatorio() + getIdEntidade() + "Layout2.jrxml");
	}

	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}
	
	@Override
	public List<TermoReconhecimentoDividaRelVO> criarObjetoLayout3(Integer aluno, NegociacaoContaReceberVO negociacaoContaReceberVO, String observacaoHistorico, Boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List<TermoReconhecimentoDividaRelVO> tipoDescontoRelVOs = new ArrayList<TermoReconhecimentoDividaRelVO>(0);
		TermoReconhecimentoDividaRelVO termoReconhecimentoDividaRelVO = new TermoReconhecimentoDividaRelVO();
		termoReconhecimentoDividaRelVO.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(Uteis.isAtributoPreenchido(aluno) ? aluno : negociacaoContaReceberVO.getMatriculaAluno().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		termoReconhecimentoDividaRelVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(negociacaoContaReceberVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
		termoReconhecimentoDividaRelVO.setAcrescimo(negociacaoContaReceberVO.getAcrescimo());
		termoReconhecimentoDividaRelVO.setJuro(negociacaoContaReceberVO.getJuro());
		termoReconhecimentoDividaRelVO.setDesconto(negociacaoContaReceberVO.getDesconto());
		termoReconhecimentoDividaRelVO.setTipoDesconto(negociacaoContaReceberVO.getTipoDesconto());
		termoReconhecimentoDividaRelVO.setValorFinal(negociacaoContaReceberVO.getValorTotal());
		termoReconhecimentoDividaRelVO.setValor(negociacaoContaReceberVO.getValor());
		termoReconhecimentoDividaRelVO.setEntrada(negociacaoContaReceberVO.getValorEntrada());
		termoReconhecimentoDividaRelVO.setRenegociacaoContaReceber(negociacaoContaReceberVO.getCodigo());
		termoReconhecimentoDividaRelVO.setNomeCurso(negociacaoContaReceberVO.getMatriculaAluno().getCurso().getNome());
		termoReconhecimentoDividaRelVO.setMatricula(negociacaoContaReceberVO.getMatricula());
		if (negociacaoContaReceberVO.getNovaContaReceber().isEmpty()) {
			negociacaoContaReceberVO.setNovaContaReceber(consultarNovaContaReceber(negociacaoContaReceberVO, controlarAcesso, usuario));
		}
		Double valorTotalDebito = 0.0;
		Double valorTotalNovasParcelas = 0.0;
		for (ContaReceberNegociadoVO contaReceberNegociadoVO : negociacaoContaReceberVO.getContaReceberNegociadoVOs()) {
			TermoReconhecimentoDividaDebitoRelVO termoReconhecimentoDividaDebitoRelVO = new TermoReconhecimentoDividaDebitoRelVO();
			termoReconhecimentoDividaDebitoRelVO.setDataVencimento(contaReceberNegociadoVO.getContaReceber().getDataVencimento_Apresentar());
			termoReconhecimentoDividaDebitoRelVO.setServico(TipoBoletoBancario.getDescricao(contaReceberNegociadoVO.getContaReceber().getTipoBoleto()).toString());
			if(contaReceberNegociadoVO.getContaReceber().getCentroReceita().getDescricao().trim().isEmpty()){
				termoReconhecimentoDividaDebitoRelVO.setCentroReceita(contaReceberNegociadoVO.getContaReceber().getTipoOrigem_apresentar());
			}else{
				termoReconhecimentoDividaDebitoRelVO.setCentroReceita(contaReceberNegociadoVO.getContaReceber().getCentroReceita().getDescricao());
			}
			termoReconhecimentoDividaDebitoRelVO.setParcela(contaReceberNegociadoVO.getContaReceber().getParcelaPorConfiguracaoFinanceira_Apresentar());
			termoReconhecimentoDividaDebitoRelVO.setValorFinal(contaReceberNegociadoVO.getValor());
			termoReconhecimentoDividaDebitoRelVO.setJuro(contaReceberNegociadoVO.getContaReceber().getJuro());
			termoReconhecimentoDividaDebitoRelVO.setMulta(contaReceberNegociadoVO.getContaReceber().getMulta());
			termoReconhecimentoDividaDebitoRelVO.setAcrescimo(contaReceberNegociadoVO.getContaReceber().getAcrescimo());
			termoReconhecimentoDividaDebitoRelVO.setValor(contaReceberNegociadoVO.getContaReceber().getValor());
			termoReconhecimentoDividaDebitoRelVO.setNrDocumento(contaReceberNegociadoVO.getContaReceber().getNrDocumento());
			termoReconhecimentoDividaDebitoRelVO.setValorDesconto(contaReceberNegociadoVO.getContaReceber().getValorDescontoCalculado());
			valorTotalDebito += termoReconhecimentoDividaDebitoRelVO.getValorFinal();
			termoReconhecimentoDividaRelVO.getTermoReconhecimentoDividaDebitoRelVOs().add(termoReconhecimentoDividaDebitoRelVO);
		}
		for (ContaReceberVO contaReceberVO : negociacaoContaReceberVO.getNovaContaReceber()) {
			TermoReconhecimentoDividaParcelasRelVO termoReconhecimentoParcelasRelVO = new TermoReconhecimentoDividaParcelasRelVO();
			termoReconhecimentoParcelasRelVO.setDataVencimento(contaReceberVO.getDataVencimento_Apresentar());
			termoReconhecimentoParcelasRelVO.setServico(contaReceberVO.getTipoOrigem_apresentar());
			termoReconhecimentoParcelasRelVO.setCentroReceita(contaReceberVO.getCentroReceita().getDescricao());
			termoReconhecimentoParcelasRelVO.setParcela(contaReceberVO.getParcela());
			termoReconhecimentoParcelasRelVO.setValor(contaReceberVO.getValor());
			termoReconhecimentoParcelasRelVO.setNrDocumento(contaReceberVO.getNrDocumento());
			valorTotalNovasParcelas += contaReceberVO.getValor();
			termoReconhecimentoDividaRelVO.getTermoReconhecimentoDividaParcelasRelVOs().add(termoReconhecimentoParcelasRelVO);
		}
		termoReconhecimentoDividaRelVO.setValorTotalDebitoAluno(Uteis.arrendondarForcando2CadasDecimais(valorTotalDebito));
		tipoDescontoRelVOs.add(termoReconhecimentoDividaRelVO);
		return tipoDescontoRelVOs;
	}
	
	@Override
	public String designIReportRelatorioLayout3() {
		return caminhoBaseRelatorio() + "TermoReconhecimentoDividaLayout3Rel.jrxml";
	}
	
	@Override
	public String imprimirPorTextoPadrao(MatriculaVO matriculaVO, TermoReconhecimentoDividaRelVO termoReconhecimento, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		String caminhoRelatorio = "";
		ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
		impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
		impressaoContratoVO.setGerarNovoArquivoAssinado(true);
		impressaoContratoVO.setTermoReconhecimentoDividaRelVO(termoReconhecimento);
		impressaoContratoVO.setNegociacaoContaReceberVO(getFacadeFactory().getNegociacaoContaReceberFacade().consultarPorChavePrimaria(termoReconhecimento.getRenegociacaoContaReceber(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, null, usuario));
		impressaoContratoVO.getNegociacaoContaReceberVO().setContaReceberNegociadoVOs(getFacadeFactory().getContaReceberNegociadoFacade().consultarContaReceberNegociados(impressaoContratoVO.getNegociacaoContaReceberVO(), Uteis.NIVELMONTARDADOS_TODOS, null, usuario));
		
		String textoStr = getFacadeFactory().getImpressaoContratoFacade().montarDadosContratoTextoPadrao(matriculaVO, impressaoContratoVO, textoPadraoDeclaracao, config, usuario);
		if (textoPadraoDeclaracao.getTipoDesigneTextoEnum().isHtml()) {
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("textoRelatorio", textoStr);
		} else {
			caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracao, "", true, config, usuario);
			getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
		}
		return caminhoRelatorio;
	}
	
	public StringBuilder adicionarFiltroTipoOrigemContaReceber(FiltroRelatorioFinanceiroVO obj, String  keyEntidade) {
		StringBuilder sqlStr = new StringBuilder("") ;
		if(obj == null){
			return sqlStr;
		}

		sqlStr.append(keyEntidade).append(".tipoOrigem in (''");
		boolean existe = false;
		if (obj.getTipoOrigemBiblioteca()) {
			sqlStr.append(", 'BIB'");
			existe = true;
		}
		if (obj.getTipoOrigemBolsaCusteadaConvenio()) {
			sqlStr.append(", 'BCC'");
			existe = true;
		}
		if (obj.getTipoOrigemContratoReceita()) {
			sqlStr.append(", 'CTR'");
			existe = true;
		}
		if (obj.getTipoOrigemDevolucaoCheque()) {
			sqlStr.append(", 'DCH'");
			existe = true;
		}
		if (obj.getTipoOrigemInclusaoReposicao()) {
			sqlStr.append(", 'IRE'");
			existe = true;
		}
		if (obj.getTipoOrigemInscricaoProcessoSeletivo()) {
			sqlStr.append(", 'IPS'");
			existe = true;
		}
		if (obj.getTipoOrigemMatricula()) {
			sqlStr.append(", 'MAT'");
			existe = true;
		}
		if (obj.getTipoOrigemMensalidade()) {
			sqlStr.append(", 'MEN'");
			existe = true;
		}
		if (obj.getTipoOrigemNegociacao()) {
			sqlStr.append(", 'NCR'");
			existe = true;
		}
		if (obj.getTipoOrigemOutros()) {
			sqlStr.append(", 'OUT'");
			existe = true;
		}
		if (obj.getTipoOrigemRequerimento()) {
			sqlStr.append(", 'REQ'");
			existe = true;
		}
		if (obj.getTipoOrigemMaterialDidatico()) {
			sqlStr.append(", 'MDI'");
			existe = true;
		}

		sqlStr.append(" ) ");
		if(!existe) {
			sqlStr = new StringBuilder(" 1 = 1");
		}
		return sqlStr;
	}
	
	private void montarDadosPessoa(NegociacaoContaReceberVO negociacaoContaReceberVO, TermoReconhecimentoDividaRelVO termoReconhecimentoDividaRelVO) {
		termoReconhecimentoDividaRelVO.getSub1().get(0).setNomeResponsavel(negociacaoContaReceberVO.getPessoa().getNome());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setTelResResponsavel(negociacaoContaReceberVO.getPessoa().getTelefoneRes());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCelularResponsavel(negociacaoContaReceberVO.getPessoa().getCelular());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCpfResponsavel(negociacaoContaReceberVO.getPessoa().getCPF());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setRgResponsavel(negociacaoContaReceberVO.getPessoa().getRG());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setBairroResponsavel(negociacaoContaReceberVO.getPessoa().getSetor());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCidadeResponsavel(negociacaoContaReceberVO.getPessoa().getCidade().getNome());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setUfResponsavel(negociacaoContaReceberVO.getPessoa().getCidade().getEstado().getNome());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCepResponsavel(negociacaoContaReceberVO.getPessoa().getCEP());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setEnderecoResponsavel(negociacaoContaReceberVO.getPessoa().getEndereco());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setOrgaoExpedidorResponsavel(negociacaoContaReceberVO.getPessoa().getOrgaoEmissor());
		termoReconhecimentoDividaRelVO.getSubReport().add(new TermoReconhecimentoDividaRelVOSubReport());
		termoReconhecimentoDividaRelVO.getSubReport().get(0).setNomeResponsavel(negociacaoContaReceberVO.getPessoa().getNome());
	}
	
	private void montarDadosFornecedor(NegociacaoContaReceberVO negociacaoContaReceberVO, TermoReconhecimentoDividaRelVO termoReconhecimentoDividaRelVO, UsuarioVO usuarioVO) throws Exception {
		FornecedorVO fornecedorVO = getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(negociacaoContaReceberVO.getFornecedor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		termoReconhecimentoDividaRelVO.getSub1().get(0).setTipoEmpresa(fornecedorVO.getTipoEmpresa());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setNomeResponsavel(fornecedorVO.getTipoEmpresa().equals("FI") ? fornecedorVO.getNome() : fornecedorVO.getRazaoSocial());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setTelResResponsavel(fornecedorVO.getTelComercial1());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCelularResponsavel(fornecedorVO.getTelComercial2());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCpfResponsavel(fornecedorVO.getCnpjOuCfp_Apresentar());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setBairroResponsavel(fornecedorVO.getSetor());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCidadeResponsavel(fornecedorVO.getCidade().getNome());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setUfResponsavel(fornecedorVO.getCidade().getEstado().getSigla());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setEnderecoResponsavel(fornecedorVO.getEndereco());
		termoReconhecimentoDividaRelVO.getSub1().get(0).setCepResponsavel(fornecedorVO.getCEP());
		termoReconhecimentoDividaRelVO.getSubReport().add(new TermoReconhecimentoDividaRelVOSubReport());
		termoReconhecimentoDividaRelVO.getSubReport().get(0).setNomeResponsavel(fornecedorVO.getTipoEmpresa().equals("FI") ? fornecedorVO.getNome() : fornecedorVO.getRazaoSocial());
	}
}
